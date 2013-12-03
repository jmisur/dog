package com.jmisur.dto.generator;

public class XParam {

	private String name;
	private Class<?> clazz;

	public XParam(Class<?> clazz, String name) {
		this.clazz = clazz;
		this.name = name;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Class<?> getClazz() {
		return clazz;
	}

	public void setClazz(Class<?> clazz) {
		this.clazz = clazz;
	}

}
