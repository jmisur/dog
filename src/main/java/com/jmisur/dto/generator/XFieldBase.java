package com.jmisur.dto.generator;

import org.jannocessor.model.type.JavaType;
import org.jannocessor.model.util.New;

public class XFieldBase<T> {
	private final String name;
	private final String typeName;
	private final Class<T> type;
	private final int modifier;
	private final XField<?> source;
	private boolean setter = true;
	private boolean getter = true;

	public XFieldBase(String name, Class<T> type, int modifier, XField<?> source) {
		this.name = name;
		this.type = type;
		this.typeName = type.getSimpleName();
		this.modifier = modifier;
		this.source = source;
	}

	public XFieldBase(String name, String typeName, int modifier, XField<?> source) {
		this.name = name;
		this.typeName = typeName;
		this.type = null;
		this.modifier = modifier;
		this.source = source;
	}

	public XFieldBase<T> noSetter() {
		this.setter = false;
		return this;
	}

	public XFieldBase<T> noGetter() {
		this.getter = false;
		return this;
	}

	public String getName() {
		return name;
	}

	public Class<T> getTypeAsClass() {
		return type;
	}

	public JavaType getType() {
		return type != null ? New.type(type.getCanonicalName()) : New.type(typeName);
	}

	public int getModifier() {
		return modifier;
	}

	public XField<?> getSource() {
		return source;
	}

	public boolean isSetter() {
		return setter;
	}

	public boolean isGetter() {
		return getter;
	}

	public XField<?>[] getFields() {
		return new XField<?>[0];
	};
}
