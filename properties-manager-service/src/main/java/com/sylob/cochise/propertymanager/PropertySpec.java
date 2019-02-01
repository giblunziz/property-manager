package com.sylob.cochise.propertymanager;

public class PropertySpec {
	
	private String name;
	private String initialeValue;
	private String resolvedValue;
	private String dynamicValue;
	public String getDynamicValue() {
		return dynamicValue;
	}
	public void setDynamicValue(String dynamicValue) {
		this.dynamicValue = dynamicValue;
	}
	private PropertyStateType state = PropertyStateType.UNSET;
	private PropertyNatureType nature = PropertyNatureType.STATIC;
	
	public PropertyNatureType getNature() {
		return nature;
	}
	public void setNature(PropertyNatureType nature) {
		this.nature = nature;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getInitialeValue() {
		return initialeValue;
	}
	public void setInitialeValue(String initialeValue) {
		this.initialeValue = initialeValue;
	}
	public String getResolvedValue() {
		return resolvedValue;
	}
	public void setResolvedValue(String resolvedValue) {
		this.resolvedValue = resolvedValue;
	}
	public PropertyStateType getState() {
		return state;
	}
	public void setState(PropertyStateType state) {
		this.state = state;
	}
	
	

}
