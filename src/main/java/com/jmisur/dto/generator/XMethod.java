package com.jmisur.dto.generator;

public class XMethod {

	private final String name;
	private final Class<?> returnType;
	private final Class<?>[] params;

	public XMethod(String name, Class<?> returnType, Class<?>... params) {
		this.name = name;
		this.returnType = returnType;
		this.params = params;
	}
}
