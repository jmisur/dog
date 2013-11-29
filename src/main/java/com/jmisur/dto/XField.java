package com.jmisur.dto;

public class XField<T> extends XFieldBase<T> {

	public XField(String name, Class<T> type, int modifier, XField<?> source) {
		super(name, type, modifier, null);
	}

	public <X> XFieldBase<X> as(String name, XField<X> type, int modifier) {
		return clone(name, type.__type, modifier, this);
	}

	public XFieldBase<T> as(String name, int modifier) {
		return clone(name, __type, modifier, this);
	}

	public XFieldBase<T> as(String name) {
		return clone(name, __type, __modifier, this);
	}

	protected <X> XFieldBase<X> asId(Class<X> idClass) {
		return clone(__name + "Id", idClass, __modifier, this);
	}

	private static <X> XFieldBase<X> clone(String name, Class<X> type, int modifier, XField<?> source) {
		return new XFieldBase<X>(name, type, modifier, source);
	}
}
