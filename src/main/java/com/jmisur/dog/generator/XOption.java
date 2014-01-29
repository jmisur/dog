package com.jmisur.dog.generator;


public class XOption<T> {

	private final T defaultValue;
	private final String name;

	public XOption(String name, T defaultValue) {
		this.name = name;
		this.defaultValue = defaultValue;
	}

	public T getDefaultValue() {
		return defaultValue;
	}

	public String getName() {
		return name;
	}

	@Override
	public String toString() {
		return name;
	}
}
