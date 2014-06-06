package com.jmisur.dog.mapper;

public class YField<T> {
	private final String name;
	private final Class<T> clazz;
	private final YClass source;

	public YField(String name, Class<T> clazz, YClass source) {
		this.name = name;
		this.clazz = clazz;
		this.source = source;
	}

	public <X> MappedField<T, X> to(YField<X> other) {
		return new MappedField<T, X>(this, other);
	}

	public String getName() {
		return name;
	}

	public Class<T> getClazz() {
		return clazz;
	}

	public YClass getSource() {
		return source;
	}

}