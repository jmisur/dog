package com.jmisur.dog.tree;

import com.jmisur.dog.DtoM;

@DtoM
public class Tree {

	Integer leafCount;

	Long height;

	String color;

	Boolean isHigh;

	public Integer getLeafCount() {
		return leafCount;
	}

	public void setLeafCount(Integer leafCount) {
		this.leafCount = leafCount;
	}

	public Long getHeight() {
		return height;
	}

	public void setHeight(Long height) {
		this.height = height;
	}

	public String getColor() {
		return color;
	}

	public void setColor(String color) {
		this.color = color;
	}

	public Boolean getIsHigh() {
		return isHigh;
	}

	public void setIsHigh(Boolean isHigh) {
		this.isHigh = isHigh;
	}
}
