package com.jmisur.dog.generator;

import java.lang.reflect.Modifier;
import java.math.BigDecimal;
import java.util.List;

public abstract class AbstractGenerator {

	protected static final String String = null;
	protected static final Integer Integer = null;
	protected static final int Int = 0;
	protected static final long Long = 0;
	protected static final Object Object = null;
	protected static final BigDecimal BigDecimal = null;

	private final GeneratorContext context = new GeneratorContext();
	private String currentPackage = "";

	public GeneratorHelper generate(String str) {
		if (!str.contains(".")) {
			str = currentPackage + str;
		}
		return context.generate(str);
	}

	public abstract void generate();

	public XFieldBase field(String name, Class<?> clazz) {
		return new XFieldBase(name, clazz, Modifier.PRIVATE, null);
	}

	public XFieldBase field(String name, Class<?> clazz, int modifier) {
		return new XFieldBase(name, clazz, modifier, null);
	}

	public XFieldBase stringField(String name) {
		return new XFieldBase(name, String.class, Modifier.PRIVATE, null);
	}

	public List<ClassGenerator> getGenerators() {
		return context.getGenerators();
	}

	public void package_(String pkg) {
		if (pkg == null) {
			pkg = "";
		}
		if (!pkg.endsWith(".")) {
			pkg += ".";
		}
		this.currentPackage = pkg;
	}

}
