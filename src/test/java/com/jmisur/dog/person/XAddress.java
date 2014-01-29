package com.jmisur.dog.person;

import com.jmisur.dog.generator.XField;
import com.jmisur.dog.generator.XParam;
import com.jmisur.dog.person.Address;
import javax.annotation.Generated;

/**
 * Generated by JAnnocessor
 */
@Generated("Easily with JAnnocessor :)")
public class XAddress extends XField<Address> {

    public static final Address Address = null;

    public static final XAddress address = new XAddress();

    private static XParam xparam;

    public final XField<String> id = new XField<String>("id", String.class, this);

    public final XField<String> street = new XField<String>("street", String.class, this);

    public final XField<Long> houseNr = new XField<Long>("houseNr", Long.class, this);

    public final XField<String> name = new XField<String>("name", String.class, this);

    public XAddress() {
        this("XAddress", null);
    }

    public XAddress(String name, XField<?> source) {
        super(name, Address.class, source);
    }

    public XField<?>[] getFields() {
        return new XField<?>[] {id, street, houseNr, name};
    }

}