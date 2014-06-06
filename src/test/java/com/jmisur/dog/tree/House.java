package com.jmisur.dog.tree;

import com.jmisur.dog.DtoM;

@DtoM
public class House {

	Integer windowCount;

	Double squaredmeters;

	String color;

	Long length;

	public Integer getWindowCount() {
		return windowCount;
	}

	public void setWindowCount(Integer windowCount) {
		this.windowCount = windowCount;
	}

	public Double getSquaredmeters() {
		return squaredmeters;
	}

	public void setSquaredmeters(Double squaredmeters) {
		this.squaredmeters = squaredmeters;
	}

	public String getColor() {
		return color;
	}

	public void setColor(String color) {
		this.color = color;
	}

	public Long getLength() {
		return length;
	}

	public void setLength(Long length) {
		this.length = length;
	}
}
