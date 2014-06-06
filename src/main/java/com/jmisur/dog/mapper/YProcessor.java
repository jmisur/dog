package com.jmisur.dog.mapper;

import java.util.ArrayList;
import java.util.List;

import org.jannocessor.collection.Power;
import org.jannocessor.collection.api.PowerList;
import org.jannocessor.extra.processor.AbstractGenerator;
import org.jannocessor.model.bean.structure.JavaClassBean;
import org.jannocessor.model.bean.type.JavaDeclaredTypeBean;
import org.jannocessor.model.executable.JavaConstructor;
import org.jannocessor.model.structure.JavaClass;
import org.jannocessor.model.type.JavaType;
import org.jannocessor.model.type.JavaTypeKind;
import org.jannocessor.model.util.Classes;
import org.jannocessor.model.util.Fields;
import org.jannocessor.model.util.New;
import org.jannocessor.model.variable.JavaField;
import org.jannocessor.processor.api.ProcessingContext;

public class YProcessor extends AbstractGenerator<JavaClass> {

	public YProcessor(String destPackage, boolean inDebugMode) {
		super(destPackage, inDebugMode);
	}

	@Override
	protected void generateCodeFrom(PowerList<JavaClass> models, ProcessingContext context) {
		context.getLogger().info("Processing {} classes", models.size());

		for (JavaClass model : models) {
			String name = "Y" + model.getName().getText();

			JavaClass yclass = createClass(model, name);
			createDefaultConstructor(yclass);
			createConstructor(model, yclass);
			createStaticFields(model, name, yclass);
			createFields(model, yclass);
			// createGetFields(xclass, fields);
			// Set<String> gettersSetters = gettersSetters(xclass);
			// createMethods(model, xclass, gettersSetters);

			generate(yclass);
		}
	}

	private List<JavaField> createFields(JavaClass model, JavaClass yclass) {
		List<JavaField> fields = new ArrayList<JavaField>();
		for (JavaField modelField : model.getFields()) {
			String fieldName = modelField.getName().getText();
			JavaType fieldType = modelField.getType();
			String fieldTypeName = fieldType.getSimpleName().getText();

			JavaField field = createYField(fieldName, fieldType, fieldTypeName);
			fields.add(field);
		}
		yclass.getFields().addAll(fields);
		return fields;
	}

	private JavaField createYField(String fieldName, JavaType fieldType, String fieldTypeName) {
		JavaField field;
		JavaType yfieldType = yfield(wrapped(fieldTypeName));
		field = New.field(Fields.PUBLIC_FINAL, yfieldType, fieldName);
		// JavaTypeParameter typeParam = New.typeParameter(fieldTypeName);
		field.getValue().setHardcoded("new %s<%s>(\"%s\", %s.class, this)", yfieldType.getSimpleName(), wrapped(fieldTypeName), fieldName, fieldTypeName);
		return field;
	}

	private void createStaticFields(JavaClass model, String name, JavaClass yclass) {
		// Y instance
		JavaField instance = New.field(Fields.PUBLIC_STATIC_FINAL, yclass.getType(), model.getName().getUncapitalized());
		instance.getValue().setHardcoded("new %s()", name);
		yclass.getFields().add(instance);

		// TODO hack to import YField
		// JavaField yparam = New.field(Fields.PRIVATE_STATIC, YField.class, "yfield");
		// yclass.getFields().add(yparam);
	}

	private void createConstructor(JavaClass model, JavaClass yclass) {
		JavaConstructor constructor = New.constructor(New.parameter(String.class, "name"), New.parameter(yclass(), "source"));
		constructor.getBody().setHardcoded("super(name, %s.class);", model.getName());
		yclass.getConstructors().add(constructor);
	}

	private void createDefaultConstructor(JavaClass xclass) {
		// constructor
		JavaConstructor defaultConstructor = New.constructor();
		defaultConstructor.getBody().setHardcoded("this(\"%s\", null);", xclass.getName().getText());
		xclass.getConstructors().add(defaultConstructor);
	}

	private JavaClass createClass(JavaClass model, String name) {
		// class signature and superclass
		JavaClass xclass = New.classs(Classes.PUBLIC, name, yclass());
		((JavaClassBean) xclass).setParent(model.getParent());
		((JavaClassBean) xclass).setType(New.type(name));
		return xclass;
	}

	private static JavaType yfield(String genericTypeName) {
		JavaDeclaredTypeBean type = new JavaDeclaredTypeBean();
		type.setSimpleName(New.name(YField.class.getSimpleName()));
		type.setPackageName(New.name(YField.class.getPackage().getName()));
		type.setKind(JavaTypeKind.DECLARED);
		type.setTypeClass(YField.class);
		type.setTypeArguments(Power.list(New.type(genericTypeName)));
		return type;
	}

	private static JavaType yclass() {
		return New.type(YClass.class);
	}

	private static String wrapped(String fieldTypeName) {
		if (fieldTypeName.equals("int")) {
			return "Integer";
		}
		return fieldTypeName;
	}

}
