package com.jmisur.dto.generator;

import org.jannocessor.collection.api.PowerList;
import org.jannocessor.extra.processor.AbstractGenerator;
import org.jannocessor.model.bean.structure.JavaClassBean;
import org.jannocessor.model.executable.JavaConstructor;
import org.jannocessor.model.structure.JavaClass;
import org.jannocessor.model.type.JavaType;
import org.jannocessor.model.util.Classes;
import org.jannocessor.model.util.Fields;
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

			// class signature and superclass
			JavaClass dto = New.classs(Classes.PUBLIC, name, xfieldType(model.getType().getTypeClass()));
			((JavaClassBean) dto).setParent(model.getParent());
			((JavaClassBean) dto).setType(New.type(name));

			// constructor
			JavaConstructor defaultConstructor = New.constructor();
			defaultConstructor.getBody().setHardcoded("this(null, null);");
			dto.getConstructors().add(defaultConstructor);

			JavaConstructor constructor = New.constructor(New.parameter(String.class, "name"), New.parameter(xfieldAnyType(), "source"));
			constructor.getBody().setHardcoded("super(name, %s.class, source);", model.getName());
			dto.getConstructors().add(constructor);

			// static field
			JavaField instance = New.field(Fields.PUBLIC_STATIC_FINAL, dto.getType(), model.getName().getUncapitalized());
			instance.getValue().setHardcoded("new %s()", name);
			dto.getFields().add(instance);

			for (JavaField modelField : model.getFields()) {
				String fieldName = modelField.getName().getText();
				JavaType fieldType = modelField.getType();
				String fieldTypeName = fieldType.getSimpleName().getText();

				JavaField field = null;
				if (isDto(fieldType)) {
					fieldTypeName = "X" + fieldTypeName;
					field = New.field(Fields.PUBLIC_FINAL, New.type(fieldTypeName), fieldName);
					field.getValue().setHardcoded("new %s(\"%s\", this)", fieldTypeName, fieldName);
				} else {
					JavaType xfieldType = xfieldType(fieldType.getTypeClass());
					field = New.field(Fields.PUBLIC_FINAL, xfieldType, fieldName);
					field.getValue().setHardcoded("new %s(\"%s\", %s.class, this)", xfieldType.getSimpleName(), fieldName, fieldTypeName);
				}
				dto.getFields().add(field);
			}

			context.generateCode(dto, true);
		}
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

	private static JavaType type(Class<?> clazz, Class<?> param) {
		return type(clazz, param.getSimpleName());
	}

	private static JavaType type(Class<?> clazz, String param) {
		return New.type("%s<%s>", clazz.getCanonicalName(), param);
	}

}
