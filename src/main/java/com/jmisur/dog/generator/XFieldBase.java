package com.jmisur.dog.generator;

import com.google.common.collect.ImmutableMap;

import org.jannocessor.model.type.JavaType;
import org.jannocessor.model.util.New;

public class XFieldBase {

	private final String name;
	private final Class<?> type;
	private final String typeName;
	private final int modifier;
	private final XFieldBase source;
	private final ImmutableMap<XOption<?>, Object> options;

	// TODO booleanoption
	private static final XOption<Boolean> setter = new XOption<Boolean>("setter", true);
	private static final XOption<Boolean> getter = new XOption<Boolean>("getter", true);
	private static final XOption<Boolean> copySetter = new XOption<Boolean>("copySetter", false);
	private static final XOption<Boolean> copyGetter = new XOption<Boolean>("copyGetter", false);

	public XFieldBase(String name, Class<?> type, int modifier, XFieldBase source) {
		this(name, type, type.getSimpleName(), modifier, source);
	}

	public XFieldBase(String name, Class<?> type, String typeName, int modifier, XFieldBase source) {
		this(name, type, typeName, modifier, source, ImmutableMap.<XOption<?>, Object> of());
	}

	private XFieldBase(String name, Class<?> type, String typeName, int modifier, XFieldBase source, ImmutableMap<XOption<?>, Object> options) {
		this.name = name;
		this.type = type;
		this.typeName = typeName;
		this.modifier = modifier;
		this.source = source;
		this.options = options;
	}

	public XFieldBase noSetter() {
		return clone(setter, false);
	}

	private <X> XFieldBase clone(XOption<X> key, X value) {
		return new XFieldBase(name, type, typeName, modifier, source, ImmutableMap.<XOption<?>, Object> builder().putAll(options).put(key, value).build());
	}

	public XFieldBase noGetter() {
		return clone(getter, false);
	}

	public String getName() {
		return name;
	}

	public Class<?> getTypeAsClass() {
		return type;
	}

	public JavaType getType() {
		return type != null ? New.type(type.getCanonicalName()) : New.type(typeName);
	}

	public int getModifier() {
		return modifier;
	}

	public XFieldBase getSource() {
		return source;
	}

	public boolean isSetter() {
		return get(setter);
	}

	private <X> X get(XOption<X> key) {
		@SuppressWarnings("unchecked")
		X value = (X) options.get(key);
		return value != null ? value : key.getDefaultValue();
	}

	public boolean isGetter() {
		return get(getter);
	}

	public XField[] getFields() {
		return new XField[0];
	}

	public XFieldBase copyGetter() {
		return clone(getter, false).clone(copyGetter, true);
	}

	public XFieldBase copySetter() {
		return clone(setter, false).clone(copySetter, true);
	}

	public boolean isCopyGetter() {
		return get(copyGetter);
	}

	public boolean isCopySetter() {
		return get(copySetter);
	}

	@Override
	public String toString() {
		return typeName + " " + name;
	}

	public XClass getSourceXClass() {
		XFieldBase result = source;
		while (result != null && !(source instanceof XClass)) {
			result = source.getSource();
		}
		return (XClass) result;
	}
}
