package org.ebayopensource.turmeric.tools.codegen.protobuf;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.ebayopensource.turmeric.runtime.codegen.common.FastSerFormatValidationError;
import org.ebayopensource.turmeric.tools.codegen.AbstractServiceGeneratorTestCase;
import org.ebayopensource.turmeric.tools.codegen.fastserformat.protobuf.validator.FastSerFormatNotSupportedException;
import org.junit.Ignore;
import org.junit.Test;

public class TestForFailureCases extends AbstractServiceGeneratorTestCase {
	
	public static String getPackageFromNamespace(String namespace) {
    	
    	//Using the method used by JAXB directly to avoid potential conflicts with JAXB generated code
    	//Therefore commenting out the old code which is based on JAXB 2.0 spec
    	return com.sun.tools.xjc.api.XJC.getDefaultPackageName(namespace);
    }
	@Ignore
	@Test
	public void testNegativeCases() throws MalformedURLException{
		
		File destDir = testingdir.getDir();
		
	List<String> wsdls = new ArrayList<String>();
	wsdls.add("TestWsdlNotSupported");
	wsdls.add("TestWsdlNotSupported2");
	wsdls.add("TestWsdlNotSupported3");
	wsdls.add("TestWsdlNotSupportedAny");
	wsdls.add("TestWsdlPolymorphism");
	
	
	Map<String,List<String>> messageMap = new HashMap<String,List<String>>();
	List<String> messages = new ArrayList<String>();
	messages.add("UNIQUE_NOT_SUPPORTED");
	messageMap.put("TestWsdlNotSupported",messages);
	messages = new ArrayList<String>();
	
	messages.add("NOTATION_NOT_SUPPORTED");
	messages.add("MIN_AND_MAX_OCCURS_ARE_NOT_SUPPORTED_IN_SEQUENCE");
	messages.add("ATTRIBUTE_SUBSTITUTIONGROUP_IS_NOT_SUPPORTED");
	messages.add("ANONYMOUS_TYPE_NOT_SUPPORTED");
	
	messageMap.put("TestWsdlNotSupported2",messages);
	messages = new ArrayList<String>();
	
	
	
	messages.add("KEY_NOT_SUPPORTED");
	messages.add("SELECTOR_NOT_SUPPORTED");
	messages.add("FIELD_NOT_SUPPORTED");
	messages.add("KEY_REF_NOT_SUPPORTED");
	messages.add("REDEFINE_NOT_SUPPORTED");
	messages.add("ELEMENT_WITHOUT_TYPE_NOT_SUPPORTED");
	messages.add("MIN_AND_MAX_OCCURS_ARE_NOT_SUPPORTED_IN_SEQUENCE");
	messages.add("MIN_AND_MAX_OCCURS_ARE_NOT_SUPPORTED_IN_CHOICE");
	messages.add("ANY_NOT_SUPPORTED");
	messages.add("ANY_ATTRIBUTE_NOT_SUPPORTED");
	messages.add("ATTRIBUTE_SUBSTITUTIONGROUP_IS_NOT_SUPPORTED");
	messages.add("ANONYMOUS_TYPE_NOT_SUPPORTED");
	
	
	messageMap.put("TestWsdlNotSupported3",messages);
	
	messages = new ArrayList<String>();
	messages.add("ANY_ATTRIBUTE_NOT_SUPPORTED");
	messages.add("ANY_NOT_SUPPORTED");
	messages.add("UNIQUE_NOT_SUPPORTED");
	messages.add("MIN_AND_MAX_OCCURS_ARE_NOT_SUPPORTED_IN_CHOICE");
	messages.add("SELECTOR_NOT_SUPPORTED");
	messages.add("FIELD_NOT_SUPPORTED");
	
	messageMap.put("TestWsdlNotSupportedAny",messages);
	messages = new ArrayList<String>();
	messages.add("POLYMORPHISM_NOT_SUPPORTED");
	
	messageMap.put("TestWsdlPolymorphism",messages);
	//TestWsdlNotSupported.wsdl
		
	File bin = new File(destDir,"bin");
	File gensrc = new File(destDir,"gen-src");
	
	
	URL [] urls = {new URL("file:/"+ destDir.getAbsolutePath()+"/bin/"),destDir.toURI().toURL(),gensrc.toURI().toURL()};
	URLClassLoader loader = new URLClassLoader(urls,Thread.currentThread().getContextClassLoader());
	Thread.currentThread().setContextClassLoader(loader);


	
	File file = new File("generated/gen-meta-src/META-INF/soa/services/proto/CalculatorService/CalculatorService.proto");
	
	for(String name :wsdls){
			try {
				File wsdlpath = getProtobufRelatedInput(name+".wsdl");
				generateJaxbClasses(wsdlpath.getAbsolutePath(), destDir.getAbsolutePath(),bin);
			} catch (FastSerFormatNotSupportedException e) {
				
				List<FastSerFormatValidationError> error = e.getErrors();
				for(FastSerFormatValidationError s: error){
					messageMap.get(name).contains(s.getError() );
				}
				
			} catch (Exception e) {
				
			}
	}
	}
	
	
	public void generateJaxbClasses(String path,String destDir,File binDir) throws Exception{
		String [] testArgs = {"-serviceName","CalculatorService",
				  "-genType","ServiceFromWSDLIntf",	
				  "-wsdl",path,
				  "-gip","com.ebay.test.soaframework.tools.codegen",
				  "-dest",destDir,
				  "-src",destDir,
				  "-bin",binDir.getAbsolutePath(),
				  "-slayer","INTERMEDIATE",
				  "-fastserformat","protobuf",
				  "-enabledNamespaceFolding",
				  "-scv","1.0.0",
				  "-pr",destDir};
		
		 performDirectCodeGen(testArgs, binDir);
		
	}


}
