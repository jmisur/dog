package com.jmisur.dog.car;

import com.jmisur.dog.car.Car;
import com.jmisur.dog.generator.XField;
import com.jmisur.dog.generator.XParam;
import javax.annotation.Generated;

/**
 * Generated by JAnnocessor
 */
@Generated("Easily with JAnnocessor :)")
public class XCar extends XField<Car> {

    public static final Car Car = null;

    public static final XCar car = new XCar();

    private static XParam xparam;

    public final XField<String> brand = new XField<String>("brand", String.class, this);

    public XCar() {
        this("XCar", null);
    }

    public XCar(String name, XField<?> source) {
        super(name, Car.class, source);
    }

    public XField<?>[] getFields() {
        return new XField<?>[] {brand};
    }

}