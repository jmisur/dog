package com.jmisur.dto.generator;

import java.lang.reflect.Modifier;

public class XField<T> extends XFieldBase<T> {

	public XField(String name, Class<T> type, XField<?> source) {
		super(name, type, Modifier.PRIVATE, source);
	}

	public XField(String name, Class<T> type, int modifier, XField<?> source) {
		super(name, type, modifier, source);
	}

	public <X> XFieldBase<X> as(String name, XFieldBase<X> type, int modifier) {
		return clone(name, type.getName(), modifier, this);
	}

	public XFieldBase<T> as(String name, int modifier) {
		return clone(name, getTypeAsClass(), modifier, this);
	}

	public XFieldBase<T> as(String name) {
		return clone(name, getTypeAsClass(), getModifier(), this);
	}

	public <X> XFieldBase<X> as(String name, Class<X> type) {
		return clone(name, type, getModifier(), this);
	}

	public <X> XFieldBase<X> as(String name, Class<X> type, int modifier) {
		return clone(name, type, modifier, this);
	}

	// protected <X> XFieldBase<X> asId(Class<X> idClass) {
	// return clone(getName() + "Id", idClass, getModifier(), this);
	// }

	private static <X> XFieldBase<X> clone(String name, Class<X> type, int modifier, XField<?> source) {
		return new XFieldBase<X>(name, type, modifier, source);
	}

	private static <X> XFieldBase<X> clone(String name, String typeName, int modifier, XField<?> source) {
		return new XFieldBase<X>(name, typeName, modifier, source);
	}
}
