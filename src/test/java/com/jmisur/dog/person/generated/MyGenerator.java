package com.jmisur.dog.person.generated;

import static com.jmisur.dog.person.XAddress.address;
import static com.jmisur.dog.person.XPerson.Person;
import static com.jmisur.dog.person.XPerson.person;

import java.lang.reflect.Modifier;
import java.util.ArrayList;

import com.jmisur.dog.AbstractGenerator;
import com.jmisur.dog.Generator;
import com.jmisur.dog.generator.XFieldBase;
import com.jmisur.dog.person.Person;

@Generator
public class MyGenerator extends AbstractGenerator {

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
	// type -> id
	// multiple source classes
	// package
	// validate inputs / strings, classnames, method names, package name, field names
	// superclass / hierarchy
	// interface
	// add annotations, serializable etc
	// convert subobjects to X by default
	// embed object asEmbedded().field(...).method(..)
	// custom types/dtos in method params/return types - change via builder
	// all builder methods on field/method return new instance, otherwise it alters Xclass default fields
	// enhancing with plugins via Xprocessor/DtoProcessor/XFieldBase subtype
	// XField.build() return something else

	@Override
	public void generate() {
		// default config -- generate all
		generate("PersonData01").from(person);
		generate("AddressData01").from(address);

		// exclusion
		generate("PersonData10").from(person).exclude(person.firstName);
		generate("PersonData11").from(person).exclude(person.firstName, person.address);
		generate("PersonData12").from(person).excludeAll().field(person.firstName);
		generate("PersonData13").from(person).excludeAll().fields(person.firstName, person.lastName);

		// nested field
		generate("PersonData14").from(person).field(person.address.name); // TODO exclude address here?
		generate("PersonData15").from(person).field(person.address.name.as("addressName"));

		// custom fields & options
		generate("PersonData20").from(person).field("fullName", String.class);
		generate("PersonData21").from(person).field("fullName", String.class, Modifier.PROTECTED);
		generate("PersonData22").from(person).field(field("fullName", String.class, Modifier.PUBLIC).noGetter().noSetter());
		generate("PersonData24").from(person).stringField("fullName");
		generate("PersonData25").from(person).stringField("fullName").intField("age");

		// field options
		generate("PersonData26").from(person).field(person.firstName.noGetter().copySetter());
		generate("PersonData27").from(person).field(person.firstName.copySetter().copyGetter());
		generate("PersonData28").from(person).field(stringField("fullName").noSetter()); // TODO builder should not have copyGetter method

		// overwrite field
		generate("PersonData30").from(person).field(person.firstName.as("name"));
		generate("PersonData31").from(person).field(person.address.as("addr", Modifier.PROTECTED));
		generate("PersonData32").from(person).field(person.address.as("addr", Person.class, Modifier.PROTECTED));
		XFieldBase<?> customAddressDto = generate("AddressData30").from(address).build();
		generate("PersonData33").from(person).field(person.address.as("addr", customAddressDto, Modifier.PROTECTED));

		// copy method
		generate("PersonData40").from(person).method(person.isAorB());
		// copy method with specific params
		generate("PersonData41").from(person).method(person.setSomeInt(String, Integer));
		// copy method with custom params
		generate("PersonData42").from(person).method(person.setSomeInt(String, Int)).method(person.getSomeStuff(Person, BigDecimal));

		// superclass
		XFieldBase<?> superc = generate("PersonData50Super").from(person).exclude(person.address).superclass(ArrayList.class).build();
		generate("PersonData50").from(person).excludeAll().field(person.address).superclass(superc);

		// copy method with domain class exchange to dto class
		// generate("PersonData43").from(person).method(person.getSomeStuff(Person, BigDecimal));

		// // package
		// c.generate("PersonData").from(person);
		//
		//
		// // id
		// c.generate("PersonData").from(person).field(person.address.asId().noSetter());
		//
		// // custom method
		// c.generate("PersonData").from(person).method(PersonUtils.class, "isVisible");

		//
		// // equals and hashcode
		// XFieldBase<String> name = person.firstName.as("name");
		// c.generate("PersonData").from(person).equals(name, person.firstName, person.address);
		// c.generate("PersonData").from(person).hashCode(person.firstName, person.address);
		// c.generate("PersonData").from(person).equalsAndHashCode(person.firstName, person.address);
	}

}
