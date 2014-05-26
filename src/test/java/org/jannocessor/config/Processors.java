package org.jannocessor.config;

import org.jannocessor.model.structure.JavaClass;
import org.jannocessor.processor.annotation.Annotated;
import org.jannocessor.processor.annotation.Types;

import com.jmisur.dog.Dto;
import com.jmisur.dog.generator.XProcessor;

public class Processors {

	@Annotated(Dto.class)
	@Types(JavaClass.class)
	public XProcessor xProcessor() {
		return new XProcessor("xxx", false);
	}

	// @Annotated(Generator.class)
	// @Types(JavaClass.class)
	// public DtoProcessor dtoProcessor() {
	// return new DtoProcessor("xxx", false);
	// }
}
