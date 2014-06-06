package com.jmisur.dog.mapper;

import static com.google.common.collect.Lists.newArrayList;

import java.util.ArrayList;
import java.util.List;

public abstract class AbstractMappingDefinition {

	private final List<MapperInstance> mappers = newArrayList();

	protected MapperInstance map(YClass a, YClass b) {
		MapperInstance mapper = new MapperInstance(a, b);
		mappers.add(mapper);
		return mapper;
	}

	public List<MapperInstance> getMappers() {
		return mappers;
	}

	public abstract void map();

	public static class MapperInstance {
		private final YClass a;
		private final YClass b;
		private final List<MappedField<?, ?>> fields = newArrayList();

		public MapperInstance(YClass a, YClass b) {
			this.a = a;
			this.b = b;
		}

		public MapperInstance field(MappedField<?, ?> field) {
			fields.add(field);
			return this;
		}

		public YClass getA() {
			return a;
		}

		public YClass getB() {
			return b;
		}

		public List<MappedField<?, ?>> getFields() {
			return fields;
		}

	}

	public List<Class<?>> getMapperHelpers() {
		ArrayList<Class<?>> list = new ArrayList<Class<?>>();
		list.add(this.getClass());
		return list;
	}

}