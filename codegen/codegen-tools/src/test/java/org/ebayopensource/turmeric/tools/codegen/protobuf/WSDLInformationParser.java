package org.ebayopensource.turmeric.tools.codegen.protobuf;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class WSDLInformationParser {
	
	File wsdlInfoFile;
	
	static int typesCount = 0 ;
	
	public WSDLInformationParser(File file) {
		
		wsdlInfoFile = file;
	}
	
	public List<Message> parse(){
		
		List<String> lines = FileUtil.readFileAsLines(wsdlInfoFile);
		List<Message> msg =new ArrayList<Message>();
		Message msgInfo = null;
		for(String str :lines){
			
			String [] info = str.split("=");
			if(info.length == 2){
				
				msgInfo = new Message();
				
				msgInfo.setMessageName(info[1]);
				msgInfo.setNamespace(info[0]);
				msg.add(msgInfo);
				
			}
			if(info.length == 4){
				
				if(info[3].startsWith("enum")){
					EnumMessage enumMsgInfo = new EnumMessage();
					enumMsgInfo.setMessageName(msgInfo.getMessageName());
					enumMsgInfo.setNamespace(msgInfo.getNamespace());
					
					String values = info[3].substring(info[3].indexOf("[") +1, info[3].length()-1);
					String vals [] = values.split(",");
					for(int i=0;i < vals.length;i++){
						enumMsgInfo.getValues().put(vals[i], i);
					}
					msg.remove(msgInfo);
					msg.add(enumMsgInfo);
					continue;
				}
				XsdField ele = new XsdField();
				ele.setFieldName(info[0]);
				ele.setJaxbName(info[1]);
				if(info[2].contains("Enum.")){
					ele.setEnums(true);
				}
				ele.setFieldType(info[2]);
				
				if(info[3].trim().toLowerCase().equals("optional")){
					
					ele.setOptional(true);
				}else if(info[3].trim().toLowerCase().equals("required")){
					ele.setOptional(false);
				}else if(info[3].trim().toLowerCase().equals("repeated")){
					
					ele.setList(true);
				}
				msgInfo.getFields().add(ele);
				
				
				
			}
			
		
			
		
		}
		
		for(Message m :msg){
			System.out.println(m.getMessageName());
			System.out.println(m.getNamespace());
			
			for(Field e :m.getFields()){
				System.out.println(e.getFieldName());
			}
		}
		return msg;
		
	
		
	}

}
