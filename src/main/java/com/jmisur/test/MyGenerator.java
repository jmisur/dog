package com.jmisur.test;

import static com.jmisur.test.XPerson.person;

import com.jmisur.dto.Generator;
import com.jmisur.dto.GeneratorHelper;
import com.jmisur.dto.generator.GeneratorContext;

@GeneratorHelper
public class MyGenerator implements Generator {

	public void generate(GeneratorContext c) {
		c.generate("PersonDto").from(person).field(person.address.name.as("holo"));
	}
}
