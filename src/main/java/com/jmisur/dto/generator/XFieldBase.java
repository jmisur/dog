package com.jmisur.dto.generator;

import org.apache.commons.lang.builder.ToStringBuilder;
import org.jannocessor.model.type.JavaType;
import org.jannocessor.model.util.New;

import com.google.common.collect.ImmutableMap;

public class XFieldBase<T> {
	private final String name;
	private final Class<T> type;
	private final String typeName;
	private final int modifier;
	private final XField<?> source;
	private final ImmutableMap<XOption<?>, Object> options;

	// TODO booleanoption
	private static final XOption<Boolean> setter = new XOption<Boolean>("setter", true);
	private static final XOption<Boolean> getter = new XOption<Boolean>("getter", true);
	private static final XOption<Boolean> copySetter = new XOption<Boolean>("copySetter", false);
	private static final XOption<Boolean> copyGetter = new XOption<Boolean>("copyGetter", false);

	public XFieldBase(String name, Class<T> type, int modifier, XField<?> source) {
		this(name, type, type.getSimpleName(), modifier, source);
	}

	public XFieldBase(String name, Class<T> type, String typeName, int modifier, XField<?> source) {
		this(name, type, typeName, modifier, source, ImmutableMap.<XOption<?>, Object> of());
	}

	private XFieldBase(String name, Class<T> type, String typeName, int modifier, XField<?> source, ImmutableMap<XOption<?>, Object> options) {
		this.name = name;
		this.type = type;
		this.typeName = typeName;
		this.modifier = modifier;
		this.source = source;
		this.options = options;
	}

	public XFieldBase<T> noSetter() {
		return clone(setter, false);
	}

	private <X> XFieldBase<T> clone(XOption<X> key, X value) {
		return new XFieldBase<T>(name, type, typeName, modifier, source, ImmutableMap.<XOption<?>, Object> builder().putAll(options).put(key, value).build());
	}

	public XFieldBase<T> noGetter() {
		XFieldBase<T> x = clone(getter, false);
		System.out.println("NO GETTER: " + x);
		return x;
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
		return get(setter);
	}

	private <X> X get(XOption<X> key) {
		@SuppressWarnings("unchecked")
		X value = (X) options.get(key);
		if (name.equals("fullName")) {
			System.out.println("GET: " + value + ", def: " + key.getDefaultValue());
		}
		return value != null ? value : key.getDefaultValue();
	}

	public boolean isGetter() {
		return get(getter);
	}

	public XField<?>[] getFields() {
		return new XField<?>[0];
	};

	public XFieldBase<T> copyGetter() {
		return clone(copyGetter, true);
	}

	public XFieldBase<T> copySetter() {
		return clone(copySetter, true);
	}

	public boolean isCopyGetter() {
		return get(copyGetter);
	}

	public boolean isCopySetter() {
		return get(copySetter);
	}

	@Override
	public String toString() {
		return new ToStringBuilder(this).append("name", name).append("type", type).append("typeName", typeName).append("modifier", modifier)
				.append("source", source).append("options", options).toString();
	}
}
