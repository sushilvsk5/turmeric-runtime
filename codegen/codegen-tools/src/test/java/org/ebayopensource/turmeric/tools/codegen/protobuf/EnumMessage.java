package org.ebayopensource.turmeric.tools.codegen.protobuf;

import java.util.HashMap;
import java.util.Map;

public class EnumMessage extends Message {
	
	
	String enumName;
	
	Map<String,Integer> values=new HashMap<String,Integer>();;

	public String getEnumName() {
		return enumName;
	}

	public void setEnumName(String enumName) {
		this.enumName = enumName;
	}

	public Map<String,Integer> getValues() {
		
		return values;
	}

	public void setValues(Map<String,Integer> values) {
		this.values = values;
	}

}
