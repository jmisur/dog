package com.jmisur.dto.generator;

import static com.google.common.collect.Lists.newArrayList;
import static com.google.common.collect.Maps.newLinkedHashMap;
import japa.parser.ParseException;
import japa.parser.ast.CompilationUnit;
import japa.parser.ast.body.BodyDeclaration;
import japa.parser.ast.body.MethodDeclaration;
import japa.parser.ast.body.Parameter;
import japa.parser.ast.body.TypeDeclaration;
import japa.parser.ast.stmt.Statement;
import japa.parser.ast.visitor.CloneVisitor;

import java.lang.reflect.Modifier;
import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.List;

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
import org.jannocessor.model.variable.JavaField;
import org.jannocessor.model.variable.JavaParameter;
import org.jannocessor.processor.api.ProcessingContext;

import com.google.common.base.Joiner;

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

			if (!com.jmisur.dto.AbstractGenerator.class.isAssignableFrom(generatorClass)) {
				context.getLogger().error("Class {} does not implement {} interface", generatorClassName,
						com.jmisur.dto.AbstractGenerator.class.getCanonicalName());
			}

			try {
				com.jmisur.dto.AbstractGenerator instance = (com.jmisur.dto.AbstractGenerator) generatorClass.newInstance();
				instance.generate();

				for (ClassGenerator<?> classGenerator : instance.getGenerators()) {
					JavaClass dto = createClass(generator, classGenerator);
					Collection<XFieldBase<?>> fields = createFields(classGenerator, dto);
					createGettersSetters(dto, fields);

					for (XMethod method : classGenerator.getCopyMethods()) {
						try {
							System.out.println("Wanted to parse: " + classGenerator.getSourceXClass());
							CompilationUnit cu = Helper.parserClass(null, classGenerator.getSourceXClass().getTypeAsClass());
							CompilationUnit x = (CompilationUnit) cu.accept(new CloneVisitor(), null);

							for (TypeDeclaration x2 : x.getTypes()) {
								if (x2.getName().equals(classGenerator.getSourceXClass().getType().getSimpleName().getText())) {
									List<BodyDeclaration> members = x2.getMembers();
									for (BodyDeclaration member : members) {
										if (member instanceof MethodDeclaration) {
											MethodDeclaration methodMember = (MethodDeclaration) member;
											if (methodMember.getName().equals(method.getName()) && paramsEquals(methodMember, method)) {
												JavaMethod methodCopy = New.method(Methods.PUBLIC, method.getReturnType(), method.getName(),
														asJavaParameters(method.getParams()));
												List<Statement> statements = methodMember.getBody().getStmts();
												String sts = Joiner.on("\n").join(statements);
												methodCopy.getBody().setHardcoded(sts);
												dto.getMethods().add(methodCopy);
											}
										}
									}
								}
							}
						} catch (ParseException e) {
							e.printStackTrace();
						}

					}

					context.generateCode(dto, isInDebugMode());
				}
			} catch (InstantiationException e) {
				context.getLogger().error("Cannot instantiate generator class {}", generatorClassName, e);
			} catch (IllegalAccessException e) {
				context.getLogger().error("Cannot instantiate generator class {}", generatorClassName, e);
			}
		}
	}

	private boolean paramsEquals(MethodDeclaration methodMember, XMethod method) {
		List<Parameter> params1 = methodMember.getParameters();
		List<XParam> params2 = method.getParams();

		if (params1 == null) {
			return params2.isEmpty() ? true : false;
		} else {
			int paramCount = params1.size();

			if (paramCount != params2.size()) {
				return false;
			}

			for (int i = 0; i < paramCount; i++) {
				Parameter parameter = params1.get(i);
				System.out.println(parameter.getType());
				XParam parameter2 = params2.get(i);
				System.out.println(parameter2.getClazz());

				if (!parameter.getType().toString().equals(parameter2.getClazz().getSimpleName())) {
					return false;
				}
			}
			return true;
		}
	}

	private List<JavaParameter> asJavaParameters(List<XParam> params) {
		List<JavaParameter> result = newArrayList();
		for (XParam param : params) {
			result.add(New.parameter(param.getClazz(), param.getName()));
		}
		return result;
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
			JavaField javaField = New.field(getModifier(field.getModifier()), field.getType(), field.getName());
			dto.getFields().add(javaField);
			System.out.println(dto.getName().getText() + ": " + javaField.getName().getText());
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
			if (field.getSource() == null || field.getSource() == classGenerator.getSourceXClass()) {
				// is existing or custom without source
				fields.put(field.getName(), field);
			} else {
				// custom field based on existing, overwrite under original name
				fields.put(field.getSource().getName(), field);
			}
		}
		System.out.println("FIELDS: " + fields.keySet());
		return fields.values();
	}
}
