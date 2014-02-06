package com.jmisur.dog.generator;

import java.lang.reflect.Modifier;

public class XField<T> extends XFieldBase<T> {

	public XField(String name, Class<T> type, XClass<?> source) {
		this(name, type, Modifier.PRIVATE, source);
	}

	public XField(String name, Class<T> type, int modifier, XClass<?> source) {
		super(name, type, modifier, source);
	}

	public <X> XFieldBase<X> as(String name, XFieldBase<X> type, int modifier) {
		return clone(name, null, type.getName(), modifier, getSource());
	}

	public XFieldBase<T> as(String name, int modifier) {
		return clone(name, getTypeAsClass(), getName(), modifier, getSource());
	}

	public XFieldBase<T> as(String name) {
		return clone(name, getTypeAsClass(), getName(), getModifier(), getSource());
	}

	public <X> XFieldBase<X> as(String name, Class<X> type) {
		return clone(name, type, type.getCanonicalName(), getModifier(), getSource());
	}

	public <X> XFieldBase<X> as(String name, Class<X> type, int modifier) {
		return clone(name, type, type.getCanonicalName(), modifier, getSource());
	}

	private static <X> XFieldBase<X> clone(String name, Class<X> type, String typeName, int modifier, XClass<?> source) {
		return new XFieldBase<X>(name, type, typeName, modifier, source);
	}

}
