package com.jmisur.dto.generator;

import static com.google.common.collect.Lists.newArrayList;

import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ClassGenerator<T> {

	private String className;
	private String packageName;
	private XField<T> sourceXClass;
	private List<XFieldBase<?>> fields = new ArrayList<XFieldBase<?>>();
	private List<XField<?>> excludedFields = new ArrayList<XField<?>>();
	private List<MethodReference> methods = new ArrayList<MethodReference>();
	private List<String> copyMethods = new ArrayList<String>();
	private List<XFieldBase<?>> equals = new ArrayList<XFieldBase<?>>();
	private List<XFieldBase<?>> hashCode = new ArrayList<XFieldBase<?>>();
	private boolean excludeAll;

	public ClassGenerator(String className, String packageName, XField<T> sourceXClass) {
		this.className = className;
		this.packageName = packageName;
		this.sourceXClass = sourceXClass;
	}

	public ClassGenerator<T> method(Class<?> clazz, String name) {
		methods.add(new MethodReference(clazz, name));
		return this;
	}

	public ClassGenerator<T> exclude(XField<?>... fields) {
		excludedFields.addAll(newArrayList(fields));
		return this;
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	public XFieldBase<?> build() {
		return new XFieldBase(className, className, 0, sourceXClass);
	}

	public ClassGenerator<T> field(XFieldBase<?> field) {
		checkSource(field);
		fields.add(field);
		return this;
	}

	public ClassGenerator<T> fields(XFieldBase<?>... fields) {
		for (XFieldBase<?> field : fields) {
			checkSource(field);
			this.fields.add(field);
		}
		return this;
	}

	private void checkSource(XFieldBase<?> field) {
		if (field.getSource() != null) {
			verifySource(field.getSource());
		}
	}

	private void verifySource(XField<?> source) {
		if (source == null) {
			throw new ClassDefinitionException("You specified field which is not a derived field from " + sourceXClass);
		}
		if (source == sourceXClass) {
			return;
		}
		verifySource(source.getSource());
	}

	public ClassGenerator<T> intField(String name) {
		return field(name, int.class);
	}

	public ClassGenerator<T> stringField(String name) {
		return field(name, String.class);
	}

	public ClassGenerator<T> field(String name, Class<?> type) {
		return field(name, type, Modifier.PRIVATE);
	}

	public <X> ClassGenerator<T> field(String name, Class<X> type, int modifier) {
		return field(new XFieldBase<X>(name, type, modifier, null));
	}

	public ClassGenerator<T> equals(XFieldBase<?>... fields) {
		equals.addAll(Arrays.asList(fields));
		return this;
	}

	public static class MethodReference {
		private Class<?> clazz;
		private String name;

		public MethodReference(Class<?> clazz, String name) {
			this.clazz = clazz;
			this.name = name;
		}

		public Class<?> getClazz() {
			return clazz;
		}

		public String getName() {
			return name;
		}
	}

	public ClassGenerator<T> equalsAndHashCode(XFieldBase<?>... fields) {
		equals(fields);
		hashCode(fields);
		return this;
	}

	public ClassGenerator<T> hashCode(XFieldBase<?>... fields) {
		hashCode.addAll(Arrays.asList(fields));
		return this;
	}

	public ClassGenerator<T> excludeAll() {
		excludeAll = true;
		return this;
	}

	public ClassGenerator<T> method(String name) {
		copyMethods.add(name);
		return this;
	}

	public String getClassName() {
		return className;
	}

	public String getPackageName() {
		return packageName;
	}

	public XField<T> getSourceXClass() {
		return sourceXClass;
	}

	public List<XFieldBase<?>> getFields() {
		return fields;
	}

	public List<XField<?>> getExcludedFields() {
		return excludedFields;
	}

	public List<MethodReference> getMethods() {
		return methods;
	}

	public List<String> getCopyMethods() {
		return copyMethods;
	}

	public List<XFieldBase<?>> getEquals() {
		return equals;
	}

	public List<XFieldBase<?>> getHashCode() {
		return hashCode;
	}

	public boolean isExcludeAll() {
		return excludeAll;
	}

}
