package com.jmisur.dog.generator;

import static com.google.common.collect.Lists.newArrayList;

public class GeneratorHelper {

	private final String className;
	private String packageName;
	private final GeneratorContext generatorContext;

	public GeneratorHelper(GeneratorContext generatorContext, String className) {
		this.generatorContext = generatorContext;
		if (className == null || className.length() == 0) {
			throw new IllegalArgumentException("Class name must not be null or empty");
		}
		int dot = className.lastIndexOf(".");
		if (dot == -1) {
			this.className = className;
		} else {
			this.className = className.substring(dot + 1, className.length());
			this.packageName = className.substring(0, dot);
		}

	}

	public GeneratorHelper in(String packageName) {
		if (packageName == null || packageName.length() == 0) {
			throw new IllegalArgumentException("Package name must not be null or empty");
		}

		this.packageName = packageName;
		return this;
	}

	public ClassGenerator from(XClass... sourceXClasses) {
		ClassGenerator generator = new ClassGenerator(className, packageName, newArrayList(sourceXClasses));
		generatorContext.addClassGenerator(generator);
		return generator;
	}

}
