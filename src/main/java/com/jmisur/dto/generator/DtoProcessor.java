package com.jmisur.dto.generator;

import java.util.List;

import org.jannocessor.collection.api.PowerList;
import org.jannocessor.extra.processor.AbstractGenerator;
import org.jannocessor.model.structure.JavaClass;
import org.jannocessor.model.util.New;
import org.jannocessor.processor.api.ProcessingContext;

import com.jmisur.dto.Generator;

public class DtoProcessor extends AbstractGenerator<JavaClass> {

	public DtoProcessor(String destPackage, boolean inDebugMode) {
		super(destPackage, inDebugMode);
	}

	@Override
	protected void generateCodeFrom(PowerList<JavaClass> generators, ProcessingContext context) {
		context.getLogger().info("Processing {} classes", generators.size());

		for (JavaClass generator : generators) {
			Class<?> generatorClass = generator.getType().getTypeClass();
			String generatorClassName = generatorClass.getCanonicalName();

			if (!Generator.class.isAssignableFrom(generatorClass)) {
				context.getLogger().error("Class {} does not implement {} interface", generatorClassName, Generator.class.getCanonicalName());
			}

			try {
				GeneratorContext gc = new GeneratorContext();
				Generator instance = (Generator) generatorClass.newInstance();
				instance.generate(gc);
				List<ClassGenerator<?>> helpers = gc.getGenerators();
				for (ClassGenerator<?> classGenerator : helpers) {
					context.generateCode(New.classs(classGenerator.getClassName()), isInDebugMode());
				}
			} catch (InstantiationException e) {
				context.getLogger().error("Cannot instantiate generator class {}", generatorClassName, e);
			} catch (IllegalAccessException e) {
				context.getLogger().error("Cannot instantiate generator class {}", generatorClassName, e);
			}

			// context.generateCode(dto, true);
		}
	}
}
