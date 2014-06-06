package com.jmisur.dog.mapper;

import static com.google.common.collect.Maps.newHashMap;

import java.util.List;
import java.util.Map;

import org.jannocessor.collection.api.PowerList;
import org.jannocessor.extra.processor.AbstractGenerator;
import org.jannocessor.model.bean.NameBean;
import org.jannocessor.model.bean.structure.JavaClassBean;
import org.jannocessor.model.executable.JavaMethod;
import org.jannocessor.model.structure.JavaClass;
import org.jannocessor.model.util.Fields;
import org.jannocessor.model.util.Methods;
import org.jannocessor.model.util.New;
import org.jannocessor.model.variable.JavaField;
import org.jannocessor.processor.api.ProcessingContext;

import com.jmisur.dog.mapper.AbstractMappingDefinition.MapperInstance;

public class MapperProcessor extends AbstractGenerator<JavaClass> {

	public MapperProcessor(String destPackage, boolean inDebugMode) {
		super(destPackage, inDebugMode);
	}

	@Override
	protected void generateCodeFrom(PowerList<JavaClass> mappers, ProcessingContext context) {
		context.getLogger().info("Processing {} classes", mappers.size());

		for (JavaClass generator : mappers) {
			Class<?> mapperClass = generator.getType().getTypeClass();

			if (!validMapper(context, mapperClass)) {
				break;
			}

			AbstractMappingDefinition instance = createMapperInstance(context, mapperClass);
			if (instance == null) {
				break;
			}
			instance.map();

			processClassGenerators(context, generator, instance);
		}
	}

	private void processClassGenerators(ProcessingContext context, JavaClass generator, AbstractMappingDefinition instance) {
		JavaClass dto = createClass(generator);
		Map<Class<?>, String> fields = newHashMap();
		createFields(dto, instance.getMapperHelpers(), fields);
		for (MapperInstance mapper : instance.getMappers()) {
			createMethods(dto, mapper);
			// createGettersSetters(dto, fields, context, classGenerator);
			// createCopyMethods(context, classGenerator, dto, fields);
			generate(dto);
		}
	}

	private void createMethods(JavaClass dto, MapperInstance mapper) {
		createMap(dto, mapper, true);
		createMap(dto, mapper, false);
	}

	private void createMap(JavaClass dto, MapperInstance mapper, boolean aToB) {
		Class<?> sourceClass = aToB ? mapper.getA().getType() : mapper.getB().getType();
		Class<?> destClass = aToB ? mapper.getB().getType() : mapper.getA().getType();

		JavaMethod mapMethod = New.method(Methods.PUBLIC, New.type(destClass), "map", New.parameter(sourceClass, "source"));

		StringBuilder body = new StringBuilder();
		body.append("%s dest = new %s();\n");

		for (MappedField<?, ?> field : mapper.getFields()) {
			YField<?> sourceField = aToB ? field.getA() : field.getB();
			YField<?> destField = aToB ? field.getB() : field.getA();

			body.append(setter(destField, mapping(sourceField, destField)));
		}

		body.append("return dest;");
		mapMethod.getBody().setHardcoded(body.toString(), destClass.getSimpleName(), destClass.getSimpleName());

		dto.getMethods().add(mapMethod);
	}

	private String mapping(YField<?> sourceField, YField<?> destField) {
		if (sourceField.getClazz() == destField.getClazz()) {
			return getter(sourceField);
		} else {
			return "converter." + toMethod(destField) + "(" + getter(sourceField) + ")";
		}
	}

	private String toMethod(YField<?> aField) {
		return New.name(aField.getClazz().getSimpleName()).insertPart(0, "to").getText();
	}

	private String setter(YField<?> field, String getter) {
		return "dest." + setterName(field.getName()) + "(" + getter + ");\n";
	}

	private String setterName(String name) {
		return new NameBean(name).insertPart(0, "set").getText();
	}

	private String getter(YField<?> field) {
		return "source." + getterName(field.getName()) + "()";
	}

	private String getterName(String name) {
		return new NameBean(name).insertPart(0, "get").getText();
	}

	private boolean validMapper(ProcessingContext context, Class<?> generatorClass) {
		if (!AbstractMappingDefinition.class.isAssignableFrom(generatorClass)) {
			context.getLogger().error("Class {} does not implement {} interface", generatorClass.getCanonicalName(),
					AbstractMappingDefinition.class.getCanonicalName());
			return false;
		}

		return true;
	}

	private AbstractMappingDefinition createMapperInstance(ProcessingContext context, Class<?> generatorClass) {
		try {
			return (AbstractMappingDefinition) generatorClass.newInstance();
		} catch (InstantiationException e) {
			context.getLogger().error("Cannot instantiate mapper class {}", generatorClass.getCanonicalName(), e);
		} catch (IllegalAccessException e) {
			context.getLogger().error("Cannot instantiate mapper class {}", generatorClass.getCanonicalName(), e);
		}
		return null;
	}

	private void createFields(JavaClass dto, List<Class<?>> mapperHelpers, Map<Class<?>, String> names) {
		for (Class<?> mapperHelper : mapperHelpers) {
			JavaField javaField = New.field(Fields.PUBLIC, mapperHelper, "converter");
			javaField.getValue().setHardcoded("new %s()", mapperHelper.getSimpleName());
			dto.getFields().add(javaField);
		}
	}

	private JavaClass createClass(JavaClass generator) {
		String className = generator.getType().getTypeClass().getSimpleName() + "Generated";
		JavaClassBean dto = (JavaClassBean) New.classs(className);

		// package
		// if (mapper.getPackageName() != null) {
		// dto.setParent(New.packagee(mapper.getPackageName()));
		// } else {
		dto.setParent(generator.getParent());
		// }

		// class name
		dto.setType(New.type(className));

		// superclass
		// if (mapper.getSuperclass() != null) {
		// dto.setSuperclass(New.type(AbstractMapper.class));
		// }

		// interfaces
		// List<Class<?>> interfaces = mapper.getInterfaces();
		// if (interfaces != null) {
		// dto.setInterfaces(New.types(interfaces.toArray(new Class<?>[interfaces.size()])));
		// }

		// modifiers
		// List<ClassModifierValue> modifiers = new ArrayList<ClassModifierValue>();
		// if (!mapper.isDefault()) {
		// modifiers.add(ClassModifierValue.PUBLIC);
		// }
		// if (mapper.isFinal()) {
		// modifiers.add(ClassModifierValue.FINAL);
		// }
		// if (mapper.isAbstract()) {
		// modifiers.add(ClassModifierValue.ABSTRACT);
		// }
		// dto.setModifiers(New.classModifiers(modifiers.toArray(new ClassModifierValue[modifiers.size()])));
		return dto;
	}

}
