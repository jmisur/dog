package com.jmisur.dog.mapper;


public class MappedField<A, B> {
	private final YField<A> a;
	private final YField<B> b;
	private Class<?> converter;

	public MappedField(YField<A> a, YField<B> b) {
		this.a = a;
		this.b = b;
	}

	public MappedField<A, B> with(Class<?> converter) {
		this.converter = converter;
		return this;
	}

	public YField<A> getA() {
		return a;
	}

	public YField<B> getB() {
		return b;
	}

	public Class<?> getConverter() {
		return converter;
	}

}