package com.jmisur.dto.generator;

import static com.google.common.collect.Lists.newArrayList;

import java.util.List;

public class GeneratorContext {
	private List<ClassGenerator<?>> generators = newArrayList();

	public GeneratorHelper generate(String className) {
		return new GeneratorHelper(this, className);
	}

	void addClassGenerator(ClassGenerator<?> generator) {
		generators.add(generator);
	}

	public List<ClassGenerator<?>> getGenerators() {
		return generators;
	}

}
