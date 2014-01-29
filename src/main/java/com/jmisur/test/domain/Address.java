package com.jmisur.test.domain;

import com.jmisur.dog.Dto;

@Dto
public class Address {

	private String id;
	private String street;
	private Long houseNr;
	private String name;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getStreet() {
		return street;
	}

	public void setStreet(String street) {
		this.street = street;
	}

	public Long getHouseNr() {
		return houseNr;
	}

	public void setHouseNr(Long houseNr) {
		this.houseNr = houseNr;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
}
