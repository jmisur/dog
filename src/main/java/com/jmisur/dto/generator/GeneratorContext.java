package com.jmisur.dto.generator;

import java.util.ArrayList;
import java.util.List;

public class GeneratorContext {
	private List<ClassGenerator<?>> generators = new ArrayList<ClassGenerator<?>>();

	public GeneratorHelper generate(String className) {
		return new GeneratorHelper(this, className);
	}

	List<ClassGenerator<?>> getGenerators() {
		return generators;
	}

	void addClassGenerator(ClassGenerator<?> generator) {
		generators.add(generator);
	}

}
