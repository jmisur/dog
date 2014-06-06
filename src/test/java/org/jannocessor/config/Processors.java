package org.jannocessor.config;

import org.jannocessor.model.structure.JavaClass;
import org.jannocessor.processor.annotation.Annotated;
import org.jannocessor.processor.annotation.Types;

import com.jmisur.dog.Mapper;
import com.jmisur.dog.mapper.MapperProcessor;

public class Processors {

	// @Annotated(DtoM.class)
	// @Types(JavaClass.class)
	// public YProcessor xProcessor() {
	// return new YProcessor("xxx", false);
	// }

	@Annotated(Mapper.class)
	@Types(JavaClass.class)
	public MapperProcessor dtoProcessor() {
		return new MapperProcessor("xxx", false);
	}
}
