package com.jmisur.dto;

public class Generator {

	private String className;
	private String packageName;

	public Generator(String className) {
		if (className == null || className.length() == 0) {
			throw new IllegalArgumentException("Class name must not be null or empty");
		}
		int dot = className.lastIndexOf(".");
		if (dot == -1) {
			this.className = className;
		} else {
			this.className = className.substring(dot, className.length() - 1);
			this.packageName = className.substring(0, dot);
		}

	}

	public static Generator generate(String className) {
		return new Generator(className);
	}

	public Generator in(String packageName) {
		if (packageName == null || packageName.length() == 0) {
			throw new IllegalArgumentException("Package name must not be null or empty");
		}

		this.packageName = packageName;
		return this;
	}

	public <T> ClassGenerator<T> from(XField<T> sourceXClass) {
		return new ClassGenerator<T>(className, packageName, sourceXClass);
	}

}
