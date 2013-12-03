package com.jmisur.dto.generator;

import java.util.Arrays;
import java.util.List;

public class XMethod {

	private final String name;
	private final Class<?> returnType;
	private final XParam[] params;

	public XMethod(String name, Class<?> returnType, XParam... params) {
		this.name = name;
		this.returnType = returnType;
		this.params = params;
	}

	public String getName() {
		return name;
	}

	public Class<?> getReturnType() {
		return returnType;
	}

	public List<XParam> getParams() {
		return Arrays.asList(params);
	}
}
