package org.ebayopensource.turmeric.tools.codegen.protobuf;

public class ProtoField extends Field {
	
	
	String fieldRestriction;
	
	String sequenceNumber;
	
	String fieldType;
	
	

	

	
	public String getFieldType() {
		return fieldType;
	}

	public void setFieldType(String fieldType) {
		this.fieldType = fieldType;
	}

	public String getFieldRestriction() {
		return fieldRestriction;
	}

	public void setFieldRestriction(String fieldRestriction) {
		this.fieldRestriction = fieldRestriction;
	}

	

	public String getSequenceNumber() {
		return sequenceNumber;
	}

	public void setSequenceNumber(String sequenceNumber) {
		this.sequenceNumber = sequenceNumber;
	}

	

}
