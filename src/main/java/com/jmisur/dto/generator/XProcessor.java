package com.jmisur.dto.generator;

import static com.google.common.collect.Sets.newHashSet;
import japa.parser.ParseException;
import japa.parser.ast.CompilationUnit;
import japa.parser.ast.visitor.CloneVisitor;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.jannocessor.collection.api.PowerList;
import org.jannocessor.extra.processor.AbstractGenerator;
import org.jannocessor.model.bean.structure.JavaClassBean;
import org.jannocessor.model.executable.JavaConstructor;
import org.jannocessor.model.executable.JavaMethod;
import org.jannocessor.model.structure.JavaClass;
import org.jannocessor.model.type.JavaType;
import org.jannocessor.model.util.Classes;
import org.jannocessor.model.util.Fields;
import org.jannocessor.model.util.Methods;
import org.jannocessor.model.util.New;
import org.jannocessor.model.variable.JavaField;
import org.jannocessor.processor.api.ProcessingContext;

import com.jmisur.dto.Dto;

public class XProcessor extends AbstractGenerator<JavaClass> {

	public XProcessor(String destPackage, boolean inDebugMode) {
		super(destPackage, inDebugMode);
	}

	@Override
	protected void generateCodeFrom(PowerList<JavaClass> models, ProcessingContext context) {
		context.getLogger().info("Processing {} classes", models.size());

		for (JavaClass model : models) {
			String name = "X" + model.getName().getText();

			JavaClass xclass = createClass(model, name);
			createDefaultConstructor(xclass);
			createConstructor(model, xclass);
			createStaticField(model, name, xclass);
			List<JavaField> fields = createFields(model, xclass);
			createGetFields(xclass, fields);

			Set<String> gs = gettersSetters(xclass);
			for (JavaMethod method : model.getMethods()) {
				if (!gs.contains(method.getName().getText())) {
					JavaMethod methodCopy = New.method(Methods.PUBLIC, void.class, method.getName().getText());
					methodCopy.getBody().setHardcoded(method.getCode().copy().getHardcoded()); // "return new %s(\n%s\n*/");
					xclass.getMethods().add(methodCopy);

					CompilationUnit cu;
					try {
						cu = Helper.parserClass(null, model.getType().getTypeClass());
						CloneVisitor visitor = new CloneVisitor();
						cu.accept(visitor, null);
					} catch (ParseException e) {
						e.printStackTrace();
					}
				}
			}

			generate(xclass);
			model.getName().appendPart("XXX");
			generate(model);
		}
	}

	private Set<String> gettersSetters(JavaClass xclass) {
		Set<String> gs = newHashSet();
		for (JavaField field : xclass.getFields()) {
			gs.add(field.getName().copy().insertPart(0, "get").getText());
			gs.add(field.getName().copy().insertPart(0, "is").getText());
			gs.add(field.getName().copy().insertPart(0, "set").getText());
		}
		return gs;
	}

	private void createGetFields(JavaClass xclass, List<JavaField> fields) {
		JavaMethod getFields = New.method(Methods.PUBLIC, xfieldAnyTypeArray(), "getFields");
		getFields.getBody().setHardcoded("return new %s {%s};", xfieldAnyTypeArray().getSimpleName(), getFieldNames(fields));
		xclass.getMethods().add(getFields);
	}

	private String getFieldNames(List<JavaField> fields) {
		StringBuilder str = new StringBuilder();
		for (JavaField field : fields) {
			str.append(field.getName().getText());
			str.append(", ");
		}
		str.deleteCharAt(str.length() - 1);
		str.deleteCharAt(str.length() - 1);
		return str.toString();
	}

	private List<JavaField> createFields(JavaClass model, JavaClass xclass) {
		List<JavaField> fields = new ArrayList<JavaField>();
		for (JavaField modelField : model.getFields()) {
			String fieldName = modelField.getName().getText();
			JavaType fieldType = modelField.getType();
			String fieldTypeName = fieldType.getSimpleName().getText();

			JavaField field = null;
			if (isDto(fieldType)) {
				field = createDtoField(fieldName, fieldTypeName);
			} else {
				field = createXField(fieldName, fieldType, fieldTypeName);
			}
			fields.add(field);
		}
		xclass.getFields().addAll(fields);
		return fields;
	}

	private JavaField createXField(String fieldName, JavaType fieldType, String fieldTypeName) {
		JavaField field;
		JavaType xfieldType = xfieldType(fieldType.getTypeClass());
		field = New.field(Fields.PUBLIC_FINAL, xfieldType, fieldName);
		field.getValue().setHardcoded("new %s(\"%s\", %s.class, this)", xfieldType.getSimpleName(), fieldName, fieldTypeName);
		return field;
	}

	private JavaField createDtoField(String fieldName, String fieldTypeName) {
		JavaField field;
		fieldTypeName = "X" + fieldTypeName;
		field = New.field(Fields.PUBLIC_FINAL, New.type(fieldTypeName), fieldName);
		field.getValue().setHardcoded("new %s(\"%s\", this)", fieldTypeName, fieldName);
		return field;
	}

	private void createStaticField(JavaClass model, String name, JavaClass xclass) {
		// static field
		JavaField instance = New.field(Fields.PUBLIC_STATIC_FINAL, xclass.getType(), model.getName().getUncapitalized());
		instance.getValue().setHardcoded("new %s()", name);
		xclass.getFields().add(instance);
	}

	private void createConstructor(JavaClass model, JavaClass xclass) {
		JavaConstructor constructor = New.constructor(New.parameter(String.class, "name"), New.parameter(xfieldAnyType(), "source"));
		constructor.getBody().setHardcoded("super(name, %s.class, source);", model.getName());
		xclass.getConstructors().add(constructor);
	}

	private void createDefaultConstructor(JavaClass xclass) {
		// constructor
		JavaConstructor defaultConstructor = New.constructor();
		defaultConstructor.getBody().setHardcoded("this(\"%s\", null);", xclass.getName().getText());
		xclass.getConstructors().add(defaultConstructor);
	}

	private JavaClass createClass(JavaClass model, String name) {
		// class signature and superclass
		JavaClass xclass = New.classs(Classes.PUBLIC, name, xfieldType(model.getType().getTypeClass()));
		((JavaClassBean) xclass).setParent(model.getParent());
		((JavaClassBean) xclass).setType(New.type(name));
		return xclass;
	}

	private boolean isDto(JavaType fieldType) {
		return fieldType.getTypeClass().isAnnotationPresent(Dto.class);
	}

	private static JavaType xfieldType(Class<?> param) {
		return type(XField.class, param);
	}

	private static JavaType xfieldAnyType() {
		return type(XField.class, "?");
	}

	private static JavaType xfieldAnyTypeArray() {
		return typeArray(XField.class, "?");
	}

	private static JavaType type(Class<?> clazz, Class<?> param) {
		return type(clazz, param.getSimpleName());
	}

	private static JavaType type(Class<?> clazz, String param) {
		return New.type("%s<%s>", clazz.getCanonicalName(), param);
	}

	private static JavaType typeArray(Class<?> clazz, String param) {
		return New.type("%s<%s>[]", clazz.getCanonicalName(), param);
	}

}
