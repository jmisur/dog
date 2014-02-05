package com.jmisur.dog;

import java.lang.reflect.Modifier;
import java.math.BigDecimal;
import java.util.List;

import com.jmisur.dog.generator.ClassGenerator;
import com.jmisur.dog.generator.GeneratorContext;
import com.jmisur.dog.generator.GeneratorHelper;
import com.jmisur.dog.generator.XFieldBase;

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
		if (!str.contains(".")) str = currentPackage + str;
		return context.generate(str);
	}

	public abstract void generate();

	public <X> XFieldBase<X> field(String name, Class<X> clazz) {
		return new XFieldBase<X>(name, clazz, Modifier.PRIVATE, null);
	}

	public <X> XFieldBase<X> field(String name, Class<X> clazz, int modifier) {
		return new XFieldBase<X>(name, clazz, modifier, null);
	}

	public XFieldBase<String> stringField(String name) {
		return new XFieldBase<String>(name, String.class, Modifier.PRIVATE, null);
	}

	public List<ClassGenerator<?>> getGenerators() {
		return context.getGenerators();
	}

	public void setPackage(String pkg) {
		if (pkg == null) pkg = "";
		if (!pkg.endsWith(".")) pkg += ".";
		this.currentPackage = pkg;
	}

}
