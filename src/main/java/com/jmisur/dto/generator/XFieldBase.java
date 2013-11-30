package com.jmisur.dto.generator;



public class XFieldBase<T> {
	protected final String __name;
	protected final Class<T> __type;
	protected final int __modifier;
	protected final XField<?> __source;
	protected boolean __setter;

	public XFieldBase(String name, Class<T> type, int modifier, XField<?> source) {
		__name = name;
		__type = type;
		__modifier = modifier;
		__source = source;
	}

	public XFieldBase<T> noSetter() {
		__setter = false;
		return this;
	}
}
