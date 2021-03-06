package com.jmisur.dog.generator;

import java.util.Arrays;
import java.util.List;

public class XMethod {

	private final String name;
	private final Class<?> returnType;
	private final XParam[] params;
	private final XClass source;

	public XMethod(XClass source, String name, Class<?> returnType, XParam... params) {
		this.source = source;
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

	public XClass getSource() {
		return source;
	}
}
