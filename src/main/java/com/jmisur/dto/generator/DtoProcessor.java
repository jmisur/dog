package com.jmisur.dto.generator;

import static com.google.common.collect.Maps.newLinkedHashMap;

import java.lang.reflect.Modifier;
import java.util.Collection;
import java.util.LinkedHashMap;

import org.jannocessor.collection.api.PowerList;
import org.jannocessor.extra.processor.AbstractGenerator;
import org.jannocessor.model.bean.NameBean;
import org.jannocessor.model.bean.structure.JavaClassBean;
import org.jannocessor.model.executable.JavaMethod;
import org.jannocessor.model.modifier.FieldModifiers;
import org.jannocessor.model.structure.JavaClass;
import org.jannocessor.model.util.Fields;
import org.jannocessor.model.util.Methods;
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

				for (ClassGenerator<?> classGenerator : gc.getGenerators()) {
					JavaClass dto = createClass(generator, classGenerator);
					Collection<XFieldBase<?>> fields = createFields(classGenerator, dto);
					createGettersSetters(dto, fields);

					// for (String methodName : classGenerator.getCopyMethods()) {
					// try {
					// CompilationUnit cu = Helper.parserClass(null, classGenerator.getClassName());
					// CompilationUnit x = (CompilationUnit) cu.accept(new CloneVisitor(), null);
					//
					// for (TypeDeclaration x2 : x.getTypes()) {
					// if (x2.getName().equals(model.getName().getText())) {
					// List<BodyDeclaration> members = x2.getMembers();
					// for (BodyDeclaration member : members) {
					// if (member instanceof MethodDeclaration) {
					// MethodDeclaration methodMember = (MethodDeclaration) member;
					// System.out.println(methodMember.getName());
					// if (methodMember.getName().equals(method.getName().getText())) {
					// JavaMethod methodCopy = New.method(Methods.PUBLIC, method.getReturnType(), method.getName().getText());
					// List<Statement> statements = methodMember.getBody().getStmts();
					// String sts = Joiner.on("\n").join(statements);
					// methodCopy.getBody().setHardcoded(sts);
					// xclass.getMethods().add(methodCopy);
					// }
					// }
					// }
					// }
					// }
					// } catch (ParseException e) {
					// e.printStackTrace();
					// }

					// }

					context.generateCode(dto, isInDebugMode());
				}
			} catch (InstantiationException e) {
				context.getLogger().error("Cannot instantiate generator class {}", generatorClassName, e);
			} catch (IllegalAccessException e) {
				context.getLogger().error("Cannot instantiate generator class {}", generatorClassName, e);
			}
		}
	}

	private void createGettersSetters(JavaClass dto, Collection<XFieldBase<?>> fields) {
		for (XFieldBase<?> field : fields) {
			if (field.isGetter()) {
				JavaMethod getter = New.method(Methods.PUBLIC, field.getType(), new NameBean(field.getName()).insertPart(0, "get").getText());
				getter.getBody().setHardcoded("return %s;", field.getName());
				dto.getMethods().add(getter);
			}
			if (field.isSetter()) {
				JavaMethod setter = New.method(Methods.PUBLIC, void.class, new NameBean(field.getName()).insertPart(0, "set").getText(),
						New.parameter(field.getType(), field.getName()));
				setter.getBody().setHardcoded("this.%s = %s;", field.getName(), field.getName());
				dto.getMethods().add(setter);
			}
		}
	}

	private Collection<XFieldBase<?>> createFields(ClassGenerator<?> classGenerator, JavaClass dto) {
		Collection<XFieldBase<?>> fields = mergeFields(classGenerator);
		for (XFieldBase<?> field : fields) {
			dto.getFields().add(New.field(getModifier(field.getModifier()), field.getType(), field.getName()));
		}
		return fields;
	}

	private FieldModifiers getModifier(int modifier) {
		switch (modifier) {
		case Modifier.PUBLIC:
			return Fields.PUBLIC;
		case Modifier.PRIVATE:
			return Fields.PRIVATE;
		case Modifier.PROTECTED:
			return Fields.PROTECTED;
		case 0:
			return Fields.DEFAULT_MODIFIER;
		default:
			throw new IllegalArgumentException("Unknown modifier " + modifier);
		}
	}

	private JavaClass createClass(JavaClass generator, ClassGenerator<?> classGenerator) {
		JavaClass dto = New.classs(classGenerator.getClassName());
		((JavaClassBean) dto).setParent(generator.getParent());
		((JavaClassBean) dto).setType(New.type(classGenerator.getClassName()));
		return dto;
	}

	private Collection<XFieldBase<?>> mergeFields(ClassGenerator<?> classGenerator) {
		LinkedHashMap<String, XFieldBase<?>> fields = newLinkedHashMap();
		if (!classGenerator.isExcludeAll()) {
			for (XFieldBase<?> field : classGenerator.getSourceXClass().getFields()) {
				fields.put(field.getName(), field);
			}
		}
		for (XFieldBase<?> field : classGenerator.getExcludedFields()) {
			fields.remove(field.getName());
		}
		for (XFieldBase<?> field : classGenerator.getFields()) {
			if (field.getSource() != null) {
				fields.put(field.getSource().getName(), field);
			} else {
				fields.put(field.getName(), field);
			}
		}
		return fields.values();
	}
}
