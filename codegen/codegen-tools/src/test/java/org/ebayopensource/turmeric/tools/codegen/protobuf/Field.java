package org.ebayopensource.turmeric.tools.codegen.protobuf;

public class Field {

	
	
	String fieldName;
	
	String fieldType;
	
	public String getFieldType() {
		return fieldType;
	}

	public void setFieldType(String fieldType) {
		this.fieldType = fieldType;
	}

	public String getFieldName() {
		return fieldName;
	}

	public void setFieldName(String fieldName) {
		this.fieldName = fieldName;
	}
	
	boolean enums=false;
	
	boolean list = false;
	
	boolean optional = false;
	
	public boolean isOptional() {
		return optional;
	}

	public void setOptional(boolean optional) {
		this.optional = optional;
	}

	public boolean isList() {
		return list;
	}

	public void setList(boolean list) {
		this.list = list;
	}
	
	public boolean isEnums() {
		return enums;
	}

	public void setEnums(boolean enums) {
		this.enums = enums;
	}

}
