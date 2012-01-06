/*******************************************************************************
 * Copyright (c) 2006-2010 eBay Inc. All Rights Reserved.
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *******************************************************************************/
package org.ebayopensource.turmeric.runtime.tests.parser.json;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.Charset;
import java.util.Iterator;
import java.util.List;

import javax.xml.namespace.QName;
import javax.xml.stream.XMLStreamException;

import org.ebayopensource.turmeric.runtime.binding.impl.parser.NamespaceConvention;
import org.ebayopensource.turmeric.runtime.binding.impl.parser.ParseException;
import org.ebayopensource.turmeric.runtime.binding.impl.parser.json.JSONStreamObjectNodeImpl;
import org.ebayopensource.turmeric.runtime.binding.impl.parser.json.JSONStreamReadContext;
import org.ebayopensource.turmeric.runtime.binding.objectnode.ObjectNode;
import org.ebayopensource.turmeric.runtime.common.impl.binding.jaxb.DataBindingFacade;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 * @author wdeng
 */

public class JSONParserTest {
	private final Logger logger = LoggerFactory.getLogger(JSONParserTest.class);
	
	private static final String JSON_INPUT_WITH_NUMBER_AND_BOOLEAN = 
		"{\"jsonns.ns\":\"http://www.ebay.com/soaframework/test/JAXBDataBinding\"," + 
		"\"ns.MyMessage\":{\"body\":100,\"recipients\":[" + 
		"{\"city\":-100,\"emailAddress\":1.00,\"postCode\":-1.2e+12,\"state\":true,\"streetNumber\":false}, " + 
		"{\"city\":null,\"emailAddress\":{},\"postCode\":\"95126\",\"state\":\"CA\",\"streetNumber\":\"2245\"}" + 
		"],\"subject\":\"Test SOA JAXB XML ser/deser\"}}\n";
	private static final String JSON_INPUT_WITH_ARRAY = 
		"{\"jsonns.ns\":\"http://www.ebay.com/soaframework/test/JAXBDataBinding\"," + 
		"\"ns.MyMessage\":{\"body\":\"SOA SOA, SOS.\",\"recipients\":[" + 
		"{\"city\":\"San Jose\",\"emailAddress\":\"soa@ebay.com\",\"postCode\":\"95125\",\"state\":\"CA\",\"streetNumber\":\"2145\"}, " + 
		"{\"city\":\"San Jose\",\"emailAddress\":\"api@ebay.com\",\"postCode\":\"95126\",\"state\":\"CA\",\"streetNumber\":\"2245\"}" + 
		"],\"subject\":\"Test SOA JAXB XML ser/deser\"}}\n";
	private static final String JSON_INPUT_WITH_MAP = 
		"{\"jsonns.ns\":\"http://www.ebay.com/soaframework/test/JAXBDataBinding\"," + 
		"\"ns.MyMessage\":{\"body\":\"SOA SOA, SOS.\",\"recipients\":" +
		"{\"entry\":{\"key\":\"soa@ebay.com\",\"value\":{\"city\":\"San Jose\",\"emailAddress\":" +
		"\"soa@ebay.com\",\"postCode\":\"95125\",\"state\":\"CA\",\"streetNumber\":\"2145\"}}}," +
		"\"subject\":\"Test SOA JAXB XML ser/deser\"}}\n";
	private static final String JSON_BAD_INPUT_WITH_ARRAY = 
		"{\"jsonns:ns\":\"http://www.ebay.com/soaframework/test/JAXBDataBinding\" " + 
		"\"ns.MyMessage\"{\"body\":\"SOA SOA, SOS.\",\"recipients\"::" + 
		"{\"city\":\"San Jose\"\"emailAddress\":\"soa@ebay.com\",\"postCode\":\"95125\",\"state\":\"CA\",\"streetNumber\":\"2145\"}: " + 
		"{\"city\":\"San Jose\",\"emailAddress\":\"api@ebay.com\",\"postCode\":\"95126\",\"state\":\"CA\",\"streetNumber\":\"2245\"}" + 
		"],\"subject\":\"Test SOA JAXB XML ser/deser\"}}\n";	

	private static final String JSON_INPUT_FOR_BUG486468 = 
		"{\n" +
		"	\"jsonns.xsi\":\"http://www.w3.org/2001/XMLSchema-instance\",\n" +
		"	\"jsonns.ns2\":\"http://www.ebay.com/test/soaframework/sample/service/message\",\n" +
		"	\"jsonns.ns1\":\"http://www.ebay.com/test/soaframework/sample/errors\",\n" +
		"	\"jsonns.xs\":\"http://www.w3.org/2001/XMLSchema\",\n" +
		"	\"jsonns.sct\":\"http://www.ebayopensource.org/turmeric/common/v1/types\",\n" +
		"	\"ns2.MyMessage\":\n" +
		"	[\n" +
		"		{\n" +
		"			\"error\":\n" +
		"			[\n" +
		"				{\n" +
		"					\"ErrorParameters\":\n" +
		"					[\n" +
		"						{\n" +
		"							\"@ParamID\":\"Exception\",\n" +
		"							\"Value\":\"org.ebayopensource.turmeric.runtime.tests.sample.services.message.Test1Exception\"\n" +
		"						}\n" +
		"					],\n" +
		"					\"errorClassification\":\"CustomCode\",\n" +
		"					\"errorCode\":\"2005\",\n" +
		"					\"longMessage\":\"Internal application error: org.ebayopensource.turmeric.runtime.tests.sample.services.message.Test1Exception: Our test1 exception\",\n" +
		"					\"severityCode\":\"Error\",\n" +
		"					\"shortMessage\":\"Internal application error: org.ebayopensource.turmeric.runtime.tests.sample.services.message.Test1Exception: Our test1 exception\"\n" +
		"				}\n" +
		"			],\n" +
		"			\"recipients\":\n" +
		"				}\n" +
		"			]\n" +
		"		}\n" +
		"	]\n" +
		"}\n";
	
	private static final String JSON_INPUT_WITH_DEPTH_FIRST_ELEMENT = 
		"{\"ns.MyMessage\":{\"body\":\"SOA SOA, SOS.\",\"recipients\":" +
		"{\"entry\":{\"key\":\"soa@ebay.com\",\"value\":{\"city\":\"San Jose\",\"emailAddress\":" +
		"\"soa@ebay.com\",\"postCode\":\"95125\",\"state\":\"CA\",\"streetNumber\":\"2145\"}}}," +
		"\"subject\":\"Test SOA JAXB XML ser/deser\"}," + 
		"\"jsnns.ns\":\"http://www.ebay.com/soaframework/test/JAXBDataBinding\"}\n";
	

	private static final String GOLD_JSON_INPUT_WITH_NUMBER_AND_BOOLEAN =
		"root:{\n" +
		"	jsonns.ns:http://www.ebay.com/soaframework/test/JAXBDataBinding\n" +
		"	ns.MyMessage:{\n" +
		"		body:100\n" +
		"		recipients:{\n" +
		"			city:-100\n" +
		"			emailAddress:1.00\n" +
		"			postCode:-1.2e+12\n" +
		"			state:true\n" +
		"			streetNumber:false\n" +
		"		}\n" +
		"		recipients:{\n" +
		"			city:{}\n" +
		"			emailAddress:{}\n" +
		"			postCode:95126\n" +
		"			state:CA\n" +
		"			streetNumber:2245\n" +
		"		}\n" +
		"		subject:Test SOA JAXB XML ser/deser\n" +
		"	}\n" +
		"}\n";

	private static final String GOLD_RESULT_HASHMAP = 
			"root:{\n" +
			"	jsonns.ns:http://www.ebay.com/soaframework/test/JAXBDataBinding\n" +
			"	ns.MyMessage:{\n" +
			"		body:SOA SOA, SOS.\n" +
			"		recipients:{\n" +
			"			entry:{\n" +
			"				key:soa@ebay.com\n" +
			"				value:{\n" +
			"					city:San Jose\n" +
			"					emailAddress:soa@ebay.com\n" +
			"					postCode:95125\n" +
			"					state:CA\n" +
			"					streetNumber:2145\n" +
			"				}\n" +
			"			}\n" +
			"		}\n" +
			"		subject:Test SOA JAXB XML ser/deser\n" +
			"	}\n" +
			"}\n";

	private static final String GOLD_RESULT_ARRAY = 
			"root:{\n" +
			"	jsonns.ns:http://www.ebay.com/soaframework/test/JAXBDataBinding\n" +
			"	ns.MyMessage:{\n" +
			"		body:SOA SOA, SOS.\n" +
			"		recipients:{\n" +
			"			city:San Jose\n" +
			"			emailAddress:soa@ebay.com\n" +
			"			postCode:95125\n" +
			"			state:CA\n" +
			"			streetNumber:2145\n" +
			"		}\n" +
			"		recipients:{\n" +
			"			city:San Jose\n" +
			"			emailAddress:api@ebay.com\n" +
			"			postCode:95126\n" +
			"			state:CA\n" +
			"			streetNumber:2245\n" +
			"		}\n" +
			"		subject:Test SOA JAXB XML ser/deser\n" +
			"	}\n" +
			"}\n";
	

	
	private static final String GOLD_JSON_INPUT_PAYPAL_ISSUE = 
    		 	                 "root:{\n" +
    		 	                 "\tactionType:PAY\n" +
    		 	                 "\tcurrencyCode:USD\n" +
    		 	                 "\treceiverList:{\n" +
    		 	                 "\t\treceiver:{\n" +
    		 	                 "\t\t\tamount:1.00\n" +
    		 	                 "\t\t\temail:seller_1288085303_biz@gmail.com\n" +
    		 	                 "\t\t}\n" +
    		 	                 "\t}\n" +
    		 	                 "\treturnUrl:http://apigee.com/console/-1/handlePaypalReturn\n" +
    		 	                 "\tcancelUrl:http://apigee.com/console/-1/handlePaypalCancel?\n" +
    		 	                 "\trequestEnvelope:{\n" +
    		 	                 "\t\terrorLanguage:en_US\n" +
    		 	                 "\t\tdetailLevel:ReturnAll\n" +
    		 	                 "\t}\n" +
    		 	                 "}\n";
	@Test
	public void jSONParserPayPalSpaceIssue() throws Exception {
		String JSON_INPUT_PAYPAL_ISSUE = 
			"{" + 
			"		  \"actionType\"   :  \"PAY\"," + 
			"		  \"currencyCode\"   :  \"USD\"," + 
			"		  \"receiverList\" : {" + 
			"		                     \"receiver\":[{\"amount\":\"1.00\",\"email\":\"seller_1288085303_biz@gmail.com\"}]" + 
			"		                   }," + 
			"		  \"returnUrl\"  :  \"http://apigee.com/console/-1/handlePaypalReturn\"," + 
			"		  \"cancelUrl\"  :  \"http://apigee.com/console/-1/handlePaypalCancel?\"," + 
			"		  \"requestEnvelope\"  :  {\"errorLanguage\":\"en_US\", \"detailLevel\":\"ReturnAll\"}" + 
			"		}";
		logger.debug("**** Starting jSONParserPayPalSpaceIssue");
		logger.debug(JSON_INPUT_PAYPAL_ISSUE);
		ByteArrayInputStream is = new ByteArrayInputStream(JSON_INPUT_PAYPAL_ISSUE.getBytes());
		NamespaceConvention convention = DataBindingFacade.createDeserializationNSConvention(null);
		JSONStreamReadContext ctx = new JSONStreamReadContext(is, convention, Charset.forName("ASCII"));
		JSONStreamObjectNodeImpl root = new JSONStreamObjectNodeImpl(ctx);
		ObjectNode clone = root.cloneNode();
		root.getChildNodes();
		logger.debug(root.getTree());
		assertEquals(GOLD_JSON_INPUT_PAYPAL_ISSUE, root.getTree());
		logger.debug("**** Ending jSONParserPayPalSpaceIssue");
	}

	
	@Test
	public void jSONParserPayPalNoSpaceIssue() throws Exception {
		String JSON_INPUT_PAYPAL_ISSUE = 
			"{" + 
			"		  \"actionType\":  \"PAY\"," + 
			"		  \"currencyCode\":  \"USD\"," + 
			"		  \"receiverList\" : {" + 
			"		                     \"receiver\":[{\"amount\":\"1.00\",\"email\":\"seller_1288085303_biz@gmail.com\"}]" + 
			"		                   }," + 
			"		  \"returnUrl\":  \"http://apigee.com/console/-1/handlePaypalReturn\"," + 
			"		  \"cancelUrl\":  \"http://apigee.com/console/-1/handlePaypalCancel?\"," + 
			"		  \"requestEnvelope\"  :  {\"errorLanguage\":\"en_US\", \"detailLevel\":\"ReturnAll\"}" + 
			"		}";
		logger.debug("**** Starting jSONParserPayPalSpaceIssue");
		logger.debug(JSON_INPUT_PAYPAL_ISSUE);
		ByteArrayInputStream is = new ByteArrayInputStream(JSON_INPUT_PAYPAL_ISSUE.getBytes());
		NamespaceConvention convention = DataBindingFacade.createDeserializationNSConvention(null);
		JSONStreamReadContext ctx = new JSONStreamReadContext(is, convention, Charset.forName("ASCII"));
		JSONStreamObjectNodeImpl root = new JSONStreamObjectNodeImpl(ctx);
		ObjectNode clone = root.cloneNode();
		root.getChildNodes();
		logger.debug(root.getTree());
		assertEquals(GOLD_JSON_INPUT_PAYPAL_ISSUE, root.getTree());
		logger.debug("**** Ending jSONParserPayPalSpaceIssue");
	}

	
	@Test
	public void jSONParserWithNumberAndBoolean() throws Exception {
		logger.debug("**** Starting jSONParserWithNumberAndBoolean");
		logger.debug(JSON_INPUT_WITH_NUMBER_AND_BOOLEAN);
		ByteArrayInputStream is = new ByteArrayInputStream(JSON_INPUT_WITH_NUMBER_AND_BOOLEAN.getBytes());
		NamespaceConvention convention = DataBindingFacade.createDeserializationNSConvention(null);
		JSONStreamReadContext ctx = new JSONStreamReadContext(is, convention, Charset.forName("ASCII"));
		JSONStreamObjectNodeImpl root = new JSONStreamObjectNodeImpl(ctx);
		ObjectNode clone = root.cloneNode();
		root.getChildNodes();
		logger.debug(root.getTree());
		assertEquals(GOLD_JSON_INPUT_WITH_NUMBER_AND_BOOLEAN, root.getTree());
//		assertEquals(clone.getNodeName().getLocalPart(), root.getNodeName().getLocalPart());
		logger.debug("**** Ending jSONParserWithNumberAndBoolean");
	}

	@Test
	public void lazyReadJSONParserWithHashMap() throws Exception {
		logger.debug("**** Starting testLazyReadJSONParserWithHashMap");
		ByteArrayInputStream is = new ByteArrayInputStream(JSON_INPUT_WITH_MAP.getBytes());
		NamespaceConvention convention = DataBindingFacade.createDeserializationNSConvention(null);
		JSONStreamReadContext ctx = new JSONStreamReadContext(is, convention, Charset.forName("ASCII"));
		JSONStreamObjectNodeImpl root = new JSONStreamObjectNodeImpl(ctx);
		logger.debug(GOLD_RESULT_HASHMAP);
		logger.debug(root.toString());
		assertEquals(GOLD_RESULT_HASHMAP, root.getTree());
		logger.debug("**** Ending testLazyReadJSONParserWithHashMap");
	}
	
	@Test
	public void jSONParserWithHashMap() throws Exception {
		logger.debug("**** Starting testJSONParserWithHashMap");
		ByteArrayInputStream is = new ByteArrayInputStream(JSON_INPUT_WITH_MAP.getBytes());
		NamespaceConvention convention = DataBindingFacade.createDeserializationNSConvention(null);
		JSONStreamReadContext ctx = new JSONStreamReadContext(is, convention, Charset.forName("ASCII"));
		JSONStreamObjectNodeImpl root = new JSONStreamObjectNodeImpl(ctx);
		ObjectNode clone = root.cloneNode();
		root.getChildNodes();
		assertEquals(GOLD_RESULT_HASHMAP, root.getTree());
		assertEquals(clone.getNodeName().getLocalPart(), root.getNodeName().getLocalPart());
		logger.debug("**** Ending testJSONParserWithHashMap");
	}

	private static final String JSON_INPUT_WITH_PRIMATIVE_TYPE_ARRAY = 
		"{\"MyObject\":[{\"emailAddress\":[\"dp@ebay.com\"],\"id\":[\"1000\"],\"names\":[\"Dave\",\"David\",\"D\"]}]}";

	private static final String GOLD_RESULT_WITH_PRIMATIVE_TYPE_ARRAY = 
		"root:{\n" + 
		"	MyObject:{\n" + 
		"		emailAddress:dp@ebay.com\n" + 
		"		id:1000\n" + 
		"		names:Dave\n" + 
		"		names:David\n" + 
		"		names:D\n" + 
		"	}\n" + 
		"}\n";
	
	@Test
	public void jSONParserWithPrimativeArray() throws Exception {
		logger.debug("**** Starting testJSONParserWithPrimativeArray");
		logger.debug(JSON_INPUT_WITH_PRIMATIVE_TYPE_ARRAY);
		ByteArrayInputStream is = new ByteArrayInputStream(JSON_INPUT_WITH_PRIMATIVE_TYPE_ARRAY.getBytes());
		NamespaceConvention convention = DataBindingFacade.createDeserializationNSConvention(null);
		JSONStreamReadContext ctx = new JSONStreamReadContext(is, convention, Charset.forName("ASCII"));
		JSONStreamObjectNodeImpl root = new JSONStreamObjectNodeImpl(ctx);
		ObjectNode clone = root.cloneNode();
		root.getChildNodes();
		assertEquals(GOLD_RESULT_WITH_PRIMATIVE_TYPE_ARRAY, root.getTree());
		assertEquals(clone.getNodeName().getLocalPart(), root.getNodeName().getLocalPart());
		logger.debug("**** Ending testJSONParserWithPrimativeArray");
	}
	
	@Test
	public void jSONParserWithPrimativeArrayOnDemand() throws Exception {
		logger.debug("**** Starting testJSONParserWithPrimativeArrayOnDemand");
		logger.debug(JSON_INPUT_WITH_PRIMATIVE_TYPE_ARRAY);
		ByteArrayInputStream is = new ByteArrayInputStream(JSON_INPUT_WITH_PRIMATIVE_TYPE_ARRAY.getBytes());
		NamespaceConvention convention = DataBindingFacade.createDeserializationNSConvention(null);
		JSONStreamReadContext ctx = new JSONStreamReadContext(is, convention, Charset.forName("ASCII"));
		JSONStreamObjectNodeImpl root = new JSONStreamObjectNodeImpl(ctx);
		assertEquals(GOLD_RESULT_WITH_PRIMATIVE_TYPE_ARRAY, root.getTree());
		logger.debug("**** Ending testJSONParserWithPrimativeArrayOnDemand");
	}
	
	@Test
	public void jSONParserWithHashMap2() throws Exception {
		logger.debug("**** Starting testJSONParserWithHashMap2");
		ByteArrayInputStream is = new ByteArrayInputStream(JSON_INPUT_WITH_MAP.getBytes());
		NamespaceConvention convention = DataBindingFacade.createDeserializationNSConvention(null);
		JSONStreamReadContext ctx = new JSONStreamReadContext(is, convention, Charset.forName("ASCII"));
		JSONStreamObjectNodeImpl root = new JSONStreamObjectNodeImpl(ctx);
		int numChilds = root.getChildNodesSize();
		root.getChildNodes();
		assertEquals("Incorrect number of children returned", 2, numChilds);
		logger.debug("**** Ending testJSONParserWithHashMap2");
	}	
	
	@Test
	public void jSONParserWithHashMap3() throws Exception {
		logger.debug("**** Starting testJSONParserWithHashMap3");
		ByteArrayInputStream is = new ByteArrayInputStream(JSON_INPUT_WITH_MAP.getBytes());
		NamespaceConvention convention = DataBindingFacade.createDeserializationNSConvention(null);
		JSONStreamReadContext ctx = new JSONStreamReadContext(is, convention, Charset.forName("ASCII"));
		JSONStreamObjectNodeImpl root = new JSONStreamObjectNodeImpl(ctx);
		assertTrue("Has childrens", root.hasChildNodes());
		logger.debug("**** Ending testJSONParserWithHashMap3");
	}	
	
	@Test
	public void jSONParserWithHashMap4() throws Exception {
		logger.debug("**** Starting testJSONParserWithHashMap4");
		ByteArrayInputStream is = new ByteArrayInputStream(JSON_INPUT_WITH_MAP.getBytes());
		NamespaceConvention convention = DataBindingFacade.createDeserializationNSConvention(null);
		JSONStreamReadContext ctx = new JSONStreamReadContext(is, convention, Charset.forName("ASCII"));
		JSONStreamObjectNodeImpl root = new JSONStreamObjectNodeImpl(ctx);
		String uriNamespace = null;
		root.getChildNode(0);
	/*	QName chqname = root.getChildNode(0).getNodeName();*/
		List<ObjectNode> childList = root.getChildNodes(new QName(uriNamespace, "ns",  "jsonns"));
		assertEquals("Has one child with jssonns:ns", 1, childList.size());
		logger.debug("**** Ending testJSONParserWithHashMap4");
	}	
	
	@Test
	public void jSONParserWithHashMap5() throws Exception {
		logger.debug("**** Starting testJSONParserWithHashMap5");
		ByteArrayInputStream is = new ByteArrayInputStream(JSON_INPUT_WITH_MAP.getBytes());
		NamespaceConvention convention = DataBindingFacade.createDeserializationNSConvention(null);
		JSONStreamReadContext ctx = new JSONStreamReadContext(is, convention, Charset.forName("ASCII"));
		JSONStreamObjectNodeImpl root = new JSONStreamObjectNodeImpl(ctx);
		String uriNamespace = null;
		root.getChildNode(0);
		ObjectNode cnode = root.getChildNode(new QName(uriNamespace, "ns",  "jsonns"), 0);
		assertEquals("Has one child with jssonns:ns", "ns", cnode.getNodeName().getLocalPart());
		logger.debug("**** Ending testJSONParserWithHashMap5");
	}
	
	@Test
	public void jSONParserWithHashMap6() throws Exception {
		logger.debug("**** Starting testJSONParserWithHashMap6");
		ByteArrayInputStream is = new ByteArrayInputStream(JSON_INPUT_WITH_MAP.getBytes());
		NamespaceConvention convention = DataBindingFacade.createDeserializationNSConvention(null);
		JSONStreamReadContext ctx = new JSONStreamReadContext(is, convention, Charset.forName("ASCII"));
		JSONStreamObjectNodeImpl root = new JSONStreamObjectNodeImpl(ctx);
		String uriNamespace = null;
		ObjectNode cnode = root.getChildNode(new QName(uriNamespace, "ns",  "jsonns"), 0);
		assertEquals("Has one child with jssonns:ns", "ns", cnode.getNodeName().getLocalPart());
		logger.debug("**** Ending testJSONParserWithHashMap6");
	}
	
	@Test
	public void jSONParserWithHashMap7() throws Exception {
		logger.debug("**** Starting testJSONParserWithHashMap7");
		ByteArrayInputStream is = new ByteArrayInputStream(JSON_INPUT_WITH_MAP.getBytes());
		NamespaceConvention convention = DataBindingFacade.createDeserializationNSConvention(null);
		JSONStreamReadContext ctx = new JSONStreamReadContext(is, convention, Charset.forName("ASCII"));
		JSONStreamObjectNodeImpl root = new JSONStreamObjectNodeImpl(ctx);
		String uriNamespace = null;
	/*	QName chqname = root.getChildNode(0).getNodeName();*/
		List<ObjectNode> childList = root.getChildNodes(new QName(uriNamespace, "ns",  "jsonns"));
		assertEquals("Has one child with jssonns:ns", 1, childList.size());
		logger.debug("**** Ending testJSONParserWithHashMap7");
	}
	
	@Test
	public void jSONParserWithHashMap8() throws Exception {
		logger.debug("**** Starting testJSONParserWithHashMap8");
		ByteArrayInputStream is = new ByteArrayInputStream(JSON_INPUT_WITH_MAP.getBytes());
		NamespaceConvention convention = DataBindingFacade.createDeserializationNSConvention(null);
		JSONStreamReadContext ctx = new JSONStreamReadContext(is, convention, Charset.forName("ASCII"));
		JSONStreamObjectNodeImpl root = new JSONStreamObjectNodeImpl(ctx);
		String uriNamespace = null;
		ObjectNode cnode =  root.getChildNode(1);
		assertEquals("Has one child with jssonns:ns", "MyMessage", cnode.getNodeName().getLocalPart());
		logger.debug("**** Ending testJSONParserWithHashMap8");
	}
	
	@Test
	public void jSONParserWithHashMap9() throws Exception {
		logger.debug("**** Starting testJSONParserWithHashMap9");
		ByteArrayInputStream is = new ByteArrayInputStream(JSON_INPUT_WITH_DEPTH_FIRST_ELEMENT.getBytes());
		NamespaceConvention convention = DataBindingFacade.createDeserializationNSConvention(null);
		JSONStreamReadContext ctx = new JSONStreamReadContext(is, convention, Charset.forName("ASCII"));
		JSONStreamObjectNodeImpl root = new JSONStreamObjectNodeImpl(ctx);
		Iterator<ObjectNode> cnodeIter=  root.getChildrenIterator();
		ObjectNode cnode = cnodeIter.next();
		Iterator<ObjectNode> gcnodeIter = cnode.getChildrenIterator();
		gcnodeIter.next();
		List<ObjectNode> ggcnode = gcnodeIter.next().getChildNodes();
		List<ObjectNode> gggcnode = ggcnode.get(0).getChildNodes();
		cnodeIter.next();
		assertEquals("Has one child with jssonns:ns", "MyMessage", cnode.getNodeName().getLocalPart());
		logger.debug("**** Ending testJSONParserWithHashMap9");
	}
	
	@Test
	public void lazyReadJSONParserWithArray() throws Exception {
		logger.debug("**** Starting testLazyReadJSONParserWithArray");
		ByteArrayInputStream is = new ByteArrayInputStream(JSON_INPUT_WITH_ARRAY.getBytes());
		NamespaceConvention convention = DataBindingFacade.createDeserializationNSConvention(null);
		JSONStreamReadContext ctx = new JSONStreamReadContext(is, convention, Charset.forName("ASCII"));
		JSONStreamObjectNodeImpl root = new JSONStreamObjectNodeImpl(ctx);
		assertEquals(GOLD_RESULT_ARRAY, root.getTree());
		logger.debug("**** Ending testLazyReadJSONParserWithArray");
	}
	
	@Test
	public void jSONParserWithArray() throws Exception {
		logger.debug("**** Starting testJSONParserWithArray");
		ByteArrayInputStream is = new ByteArrayInputStream(JSON_INPUT_WITH_ARRAY.getBytes());
		NamespaceConvention convention = DataBindingFacade.createDeserializationNSConvention(null);
		JSONStreamReadContext ctx = new JSONStreamReadContext(is, convention, Charset.forName("ASCII"));
		JSONStreamObjectNodeImpl root = new JSONStreamObjectNodeImpl(ctx);
		root.getChildNodes();
		logger.debug("Tree: \n" + root.getTree());
		logger.debug("End Tree");
		assertEquals(GOLD_RESULT_ARRAY, root.getTree());
		logger.debug("**** Ending testJSONParserWithArray");
	}

	/**
	 * @check  Exceptions need to be handled
	 */
	@Test
	public void bUG486468() throws Exception {
		
		Exception e = null;
		logger.debug("**** Starting testBUG486468");
		ByteArrayInputStream is = new ByteArrayInputStream(JSON_INPUT_FOR_BUG486468.getBytes());
		NamespaceConvention convention = DataBindingFacade.createDeserializationNSConvention(null);
		JSONStreamReadContext ctx = new JSONStreamReadContext(is, convention, Charset.forName("ASCII"));
		JSONStreamObjectNodeImpl root = new JSONStreamObjectNodeImpl(ctx);
		List<ObjectNode> childList =  root.getChildNodes();
		logger.debug("**** Ending testBUG486468");
	}

	/**
	 * @check  Exceptions need to be handled
	 */
	@Test(expected=org.ebayopensource.turmeric.runtime.binding.impl.parser.ParseException.class)
	public void jSONParserWithBadInput1() throws Exception {
		
		logger.debug("**** Starting testJSONParserWithBadInput1");
		ByteArrayInputStream is = new ByteArrayInputStream(JSON_BAD_INPUT_WITH_ARRAY.getBytes());
		NamespaceConvention convention = DataBindingFacade.createDeserializationNSConvention(null);
		JSONStreamReadContext ctx = new JSONStreamReadContext(is, convention, Charset.forName("ASCII"));
		JSONStreamObjectNodeImpl root = new JSONStreamObjectNodeImpl(ctx);
			root.getChildNodes();
			fail("ParseException not thrown as expected");

		logger.debug("**** Ending testJSONParserWithBadInput1");
	}
	
	/**
	 * @check  Exceptions need to be handled
	 */
	@Test(expected=org.ebayopensource.turmeric.runtime.binding.impl.parser.ParseException.class)
	public void jSONParserWithBadInput2() throws Exception {
		
		class MyBAInputStream extends InputStream {
			
			public MyBAInputStream(byte[] buf){
				super();
			}
			
			public int read() throws IOException {
				throw new IOException();
				
			}
			
			public int read(byte[] b, int off, int len) throws IOException {
				throw new IOException();
			}
		}; 
		
		logger.debug("**** Starting testJSONParserWithBadInput2");
		MyBAInputStream is = new MyBAInputStream(JSON_BAD_INPUT_WITH_ARRAY.getBytes());
		NamespaceConvention convention = DataBindingFacade.createDeserializationNSConvention(null);
			JSONStreamReadContext ctx = new JSONStreamReadContext(is, convention, Charset.forName("ASCII"));
			JSONStreamObjectNodeImpl root = new JSONStreamObjectNodeImpl(ctx);
		
			root.getChildNodes();
			fail("ParseException not thrown as expected");

		logger.debug("**** Ending testJSONParserWithBadInput2");
	}
}
