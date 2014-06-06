package com.jmisur.dog.mapper;

public class YClass {
	private final String name;
	private final Class<?> type;

	public YClass(String name, Class<?> type) {
		this.name = name;
		this.type = type;
	}

	public String getName() {
		return name;
	}

	public Class<?> getType() {
		return type;
	}
}
