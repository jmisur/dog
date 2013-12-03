package com.jmisur.dto;

import java.lang.reflect.Modifier;
import java.math.BigDecimal;
import java.util.List;

import com.jmisur.dto.generator.ClassGenerator;
import com.jmisur.dto.generator.GeneratorContext;
import com.jmisur.dto.generator.GeneratorHelper;
import com.jmisur.dto.generator.XFieldBase;

public abstract class AbstractGenerator {
	protected static final String String = null;
	protected static final int Integer = 0;
	protected static final long Long = 0;
	protected static final Object Object = null;
	protected static final BigDecimal BigDecimal = null;

	private GeneratorContext context = new GeneratorContext();

	public GeneratorHelper generate(String str) {
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
}
