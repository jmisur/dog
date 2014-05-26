package com.jmisur.dog.generator;

import static com.google.common.collect.Lists.newArrayList;
import static com.google.common.collect.Sets.newHashSet;

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
import org.jannocessor.model.type.JavaTypeKind;
import org.jannocessor.model.util.Classes;
import org.jannocessor.model.util.Fields;
import org.jannocessor.model.util.Methods;
import org.jannocessor.model.util.New;
import org.jannocessor.model.variable.JavaField;
import org.jannocessor.model.variable.JavaParameter;
import org.jannocessor.processor.api.ProcessingContext;

import com.jmisur.dog.Dto;

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
			createStaticFields(model, name, xclass);
			List<JavaField> fields = createFields(model, xclass);
			createGetFields(xclass, fields);
			Set<String> gettersSetters = gettersSetters(xclass);
			createMethods(model, xclass, gettersSetters);

			generate(xclass);
		}
	}

	private void createMethods(JavaClass model, JavaClass xclass, Set<String> gettersSetters) {
		for (JavaMethod method : model.getMethods()) {
			String methodName = method.getName().getText();
			if (!gettersSetters.contains(methodName)) {

				StringBuilder params = new StringBuilder(", ");
				List<JavaParameter> signatureParams = newArrayList();
				for (JavaParameter param : method.getParameters()) {
					params.append("new ").append(XParam.class.getSimpleName()).append("(");
					params.append(param.getType().getSimpleName()).append(".class").append(", ");
					params.append("\"").append(param.getName().getText()).append("\"), ");

					JavaParameter signatureParam = New.parameter(param.getType(), param.getName().getText());
					signatureParams.add(signatureParam);
				}
				params.deleteCharAt(params.length() - 1);
				params.deleteCharAt(params.length() - 1);

				JavaMethod methodRef = New.method(Methods.PUBLIC, XMethod.class, methodName, signatureParams);
				methodRef.getBody().setHardcoded("return new %s(this, \"%s\", %s.class%s);", XMethod.class.getSimpleName(), methodName,
						method.getReturnType().getSimpleName(), params.toString());
				xclass.getMethods().add(methodRef);
			}
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
		JavaMethod getFields = New.method(Methods.PUBLIC, xfieldArray(), "getFields");
		getFields.getBody().setHardcoded("return new %s {%s};", xfieldArray().getSimpleName(), getFieldNames(fields));
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
		JavaType xfieldType = xfield();
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

	private void createStaticFields(JavaClass model, String name, JavaClass xclass) {
		// type placeholder
		JavaField placeholder = New.field(Fields.PUBLIC_STATIC_FINAL, model.getType(), model.getName().getCapitalized());
		placeholder.getValue().setHardcoded("null");
		xclass.getFields().add(placeholder);

		// X instance
		JavaField instance = New.field(Fields.PUBLIC_STATIC_FINAL, xclass.getType(), model.getName().getUncapitalized());
		instance.getValue().setHardcoded("new %s()", name);
		xclass.getFields().add(instance);

		// TODO hack to import XParam
		JavaField xparam = New.field(Fields.PRIVATE_STATIC, XParam.class, "xparam");
		xclass.getFields().add(xparam);
	}

	private void createConstructor(JavaClass model, JavaClass xclass) {
		JavaConstructor constructor = New.constructor(New.parameter(String.class, "name"), New.parameter(xclass(), "source"));
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
		JavaClass xclass = New.classs(Classes.PUBLIC, name, xclass());
		((JavaClassBean) xclass).setParent(model.getParent());
		((JavaClassBean) xclass).setType(New.type(name));
		return xclass;
	}

	private boolean isDto(JavaType fieldType) {
		if (fieldType.getKind() != JavaTypeKind.DECLARED) {
			return false;
		}
		return fieldType.getTypeClass().isAnnotationPresent(Dto.class);
	}

	// private static JavaType xfieldType(JavaType fieldType) {
	// return type(XField.class, fieldType);
	// }

	private static JavaType xfield() {
		return New.type(XField.class);
	}

	private static JavaType xclass() {
		return New.type(XClass.class);
	}

	private static JavaType xfieldArray() {
		return New.type("%s[]", XField.class.getCanonicalName());
	}

	// private static JavaType type(Class<?> clazz, JavaType fieldType) {
	// String name = null;
	// switch (fieldType.getKind()) {
	// case DECLARED:
	// name = fieldType.getSimpleName().getText();
	// break;
	// case INT:
	// name = Integer.class.getSimpleName();
	// break;
	// // TODO others
	// }
	// return type(clazz, name);
	// }
	//
	// private static JavaType type(Class<?> clazz, String param) {
	// return New.type("%s<%s>", clazz.getCanonicalName(), param);
	// }
	//
	// private static JavaType typeArray(Class<?> clazz, String param) {
	// return New.type("%s<%s>[]", clazz.getCanonicalName(), param);
	// }

}
