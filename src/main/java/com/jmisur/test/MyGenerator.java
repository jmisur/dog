package com.jmisur.test;

import static com.jmisur.test.domain.XAddress.address;
import static com.jmisur.test.domain.XPerson.person;

import java.lang.reflect.Modifier;

import com.jmisur.dto.Generator;
import com.jmisur.dto.GeneratorHelper;
import com.jmisur.dto.generator.GeneratorContext;
import com.jmisur.dto.generator.XFieldBase;
import com.jmisur.test.domain.Person;

@GeneratorHelper
public class MyGenerator implements Generator {

	// annotations @NotPublished
	// default configuration - all fields with getters/setters, type to id
	// exclusion
	// inclusion if fields explicitely
	// custom methods
	// copy methods
	// equalsAndHashCode
	// comparable
	// JSR303
	// field getter/setter options, nogetter
	// copying included type Person-Address
	// type -> id
	// remember source fields for mapping
	// multiple source classes
	// package
	// validate inputs / strings, classnames, method names, package name, field names
	// superclass
	// interface
	// add annotations, serializable etc
	// convert subobjects to X by default

	public void generate(GeneratorContext c) {
		// default config -- generate all
		c.generate("PersonData01").from(person);
		c.generate("AddressData01").from(address);

		// exclusion
		c.generate("PersonData10").from(person).exclude(person.firstName);
		c.generate("PersonData11").from(person).exclude(person.firstName, person.address);
		c.generate("PersonData12").from(person).excludeAll().field(person.firstName);
		c.generate("PersonData13").from(person).excludeAll().fields(person.firstName, person.lastName);

		// custom fields & options
		c.generate("PersonData20").from(person).field("fullName", String.class);
		c.generate("PersonData21").from(person).field("fullName", String.class, Modifier.PROTECTED);
		c.generate("PersonData22").from(person).field(c.field("fullName", String.class, Modifier.PUBLIC).noGetter().noSetter());
		c.generate("PersonData23").from(person).field(c.stringField("fullName").noSetter());
		c.generate("PersonData24").from(person).stringField("fullName");
		c.generate("PersonData25").from(person).stringField("fullName").intField("age");

		// overwrite field
		c.generate("PersonData30").from(person).field(person.firstName.as("name"));
		c.generate("PersonData31").from(person).field(person.address.as("addr", Modifier.PROTECTED));
		c.generate("PersonData32").from(person).field(person.address.as("addr", Person.class, Modifier.PROTECTED));
		XFieldBase<?> customAddressDto = c.generate("AddressData30").from(address).build();
		c.generate("PersonData33").from(person).field(person.address.as("addr", customAddressDto, Modifier.PROTECTED));

		// // package
		// c.generate("PersonData").from(person);
		//
		//
		// // id
		// c.generate("PersonData").from(person).field(person.address.asId().noSetter());
		// // nested field
		// c.generate("PersonData").from(person).field(person.address.name);
		// c.generate("PersonData").from(person).field(person.address.name.as("addressName"));
		//
		// // custom method
		// c.generate("PersonData").from(person).method(PersonUtils.class, "isVisible");

		// copy method
		// c.generate("PersonData").from(person).method("getAllStuff");
		c.generate("PersonData").from(person).method(person.isAorB()).method(person.setMaNameDude(String.class, Integer.class));

		//
		// // equals and hashcode
		// XFieldBase<String> name = person.firstName.as("name");
		// c.generate("PersonData").from(person).equals(name, person.firstName, person.address);
		// c.generate("PersonData").from(person).hashCode(person.firstName, person.address);
		// c.generate("PersonData").from(person).equalsAndHashCode(person.firstName, person.address);
	}
}
