package com.jmisur.dog.tree.generated;

import com.jmisur.dog.Mapper;
import com.jmisur.dog.mapper.AbstractMappingDefinition;
import com.jmisur.dog.tree.YHouse;
import com.jmisur.dog.tree.YTree;

@Mapper
public class MyMappingDefinition extends AbstractMappingDefinition {

	YTree tree = YTree.tree;
	YHouse house = YHouse.house;

	// map with existing instance
	// common conversions
	// converter as instance
	// converter as static method
	// MappingInstanceOneWay with oneway methods
	// converter as autowired or injected to constructor
	// converter instance with auto-discovery
	// YClass methods as converters
	@Override
	public void map() {
		// default!!
		map(tree, house) //
				.field(tree.height.to(house.squaredmeters)) //
		.field(tree.isHigh.to(house.length)) //
		.field(tree.leafCount.to(house.windowCount)); // compatible types
	}

	public Double toDouble(Long source) {
		return (double) (source * source);
	}

	public Long toLong(Double source) {
		return (long) Math.sqrt(source);
	}

	public Integer toInteger(Integer source) {
		return source;
	}

	public Long toLong(Boolean isHigh) {
		return isHigh ? 1L : 0L;
	}

	public Boolean toBoolean(Long length) {
		return length > 0;
	}
}