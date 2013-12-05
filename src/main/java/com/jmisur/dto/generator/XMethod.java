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

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder().append(returnType.getSimpleName()).append(" ").append(name).append("(");
		if (params != null) {
			for (XParam param : params) {
				builder.append(param.getClazz().getSimpleName()).append(" ").append(param.getName()).append(", ");
			}
			builder.deleteCharAt(builder.length() - 1);
			builder.deleteCharAt(builder.length() - 1);
		}
		builder.append(")");
		return builder.toString();
	}
}
