package com.jmisur.dto.generator;

import static com.google.common.collect.Lists.newArrayList;

import java.lang.reflect.Modifier;
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

	public <X> XFieldBase<X> field(String name, Class<X> clazz) {
		return new XFieldBase<X>(name, clazz, Modifier.PRIVATE, null);
	}

	public <X> XFieldBase<X> field(String name, Class<X> clazz, int modifier) {
		return new XFieldBase<X>(name, clazz, modifier, null);
	}

	public XFieldBase<String> stringField(String name) {
		return new XFieldBase<String>(name, String.class, Modifier.PRIVATE, null);
	}
}
