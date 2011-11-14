package org.ebayopensource.turmeric.tools.codegen.protobuf;

import java.util.ArrayList;
import java.util.List;

public class Message {
	
	String messageName;
	
	String namespace;
	
	List<Field> fields = new ArrayList<Field>();;
	
	public List<Field> getFields() {
		
		return fields;
	}

	public void setFields(List<Field> fields) {
		this.fields = fields;
	}
	
	public String getNamespace() {
		return namespace;
	}

	public void setNamespace(String namespace) {
		this.namespace = namespace;
	}
	public String getMessageName() {
		return messageName;
	}
	public void setMessageName(String messageName) {
		this.messageName = messageName;
	}

	
	

}
