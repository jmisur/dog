package com.jmisur.dog.generator;

import java.lang.reflect.Modifier;

public class XField extends XFieldBase {

	public XField(String name, Class<?> type, XFieldBase source) {
		this(name, type, Modifier.PRIVATE, source);
	}

	public XField(String name, Class<?> type, int modifier, XFieldBase source) {
		super(name, type, modifier, source);
	}

	public XFieldBase as(String name) {
		return clone(name, getTypeAsClass(), getName(), getModifier(), this);
	}

	public XFieldBase as(String name, int modifier) {
		return clone(name, getTypeAsClass(), getName(), modifier, this);
	}

	public <X> XFieldBase as(String name, XClass type) {
		checkTypeIsDerived(type);
		return clone(name, null, type.getName(), getModifier(), this);
	}

	public <X> XFieldBase as(String name, XClass type, int modifier) {
		checkTypeIsDerived(type);
		return clone(name, null, type.getName(), modifier, this);
	}

	public <X> XFieldBase as(String name, Class<?> type) {
		return clone(name, type, type.getName(), getModifier(), this);
	}

	public <X> XFieldBase as(String name, Class<?> type, int modifier) {
		return clone(name, type, type.getName(), modifier, this);
	}

	public <X> XFieldBase as(XClass type) {
		checkTypeIsDerived(type);
		return clone(getName(), null, type.getName(), getModifier(), this);
	}

	public <X> XFieldBase as(XClass type, int modifier) {
		checkTypeIsDerived(type);
		return clone(getName(), null, type.getName(), modifier, this);
	}

	public <X> XFieldBase as(Class<?> type) {
		return clone(getName(), type, type.getName(), getModifier(), this);
	}

	public <X> XFieldBase as(Class<?> type, int modifier) {
		return clone(getName(), type, type.getName(), modifier, this);
	}

	private static XFieldBase clone(String name, Class<?> type, String typeName, int modifier, XFieldBase source) {
		return new XFieldBase(name, type, typeName, modifier, source);
	}

	private void checkTypeIsDerived(XClass type) {
		// TODO diff between custom type with name and XClass itself, maybe return XGeneratedClass from build()?
	}
}
