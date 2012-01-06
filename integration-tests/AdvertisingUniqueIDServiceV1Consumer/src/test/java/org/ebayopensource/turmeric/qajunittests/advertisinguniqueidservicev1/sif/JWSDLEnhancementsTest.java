/*******************************************************************************
 * Copyright (c) 2006-2010 eBay Inc. All Rights Reserved.
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 *******************************************************************************/
package org.ebayopensource.turmeric.qajunittests.advertisinguniqueidservicev1.sif;

import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.net.URL;
import java.util.HashMap;
import java.util.Map;

import org.ebayopensource.turmeric.runtime.common.types.SOAHeaders;
import org.ebayopensource.turmeric.runtime.tests.common.jetty.AbstractWithServerQETest;
import org.ebayopensource.turmeric.runtime.tests.common.sif.error.MarkdownTestHelper;
import org.ebayopensource.turmeric.runtime.tests.common.util.HttpTestClient;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import com.ebay.kernel.service.invocation.client.http.Request;
import com.ebay.kernel.service.invocation.client.http.Response;

public class JWSDLEnhancementsTest extends AbstractWithServerQETest{
	
	@Test
	public void wsdlContentDisplay1() throws Exception {
		logger.debug("testWsdlContentDisplay1()");
		HttpTestClient httpClient = HttpTestClient.getInstance();
		httpClient.port = serverUri.toASCIIString().substring(17);
		
		Map queryParams = new HashMap();
		String url = serverUri.toASCIIString() + "/ws/spf?wsdl&X-TURMERIC-SERVICE-NAME=AdvertisingUniqueIDServiceV1";
		logger.debug(url);
		String wsdlFileContent = httpClient.getResponse(url, queryParams);
		logger.debug("browser content=" +wsdlFileContent);
		CharSequence wsdlCharset = "wsdl:definitions xmlns:wsdl=";
		assertTrue("Expected string - '" + wsdlCharset +"' NOT found", wsdlFileContent.contains(wsdlCharset));
		wsdlCharset = "wsdl:service name=\"AdvertisingUniqueIDServiceV1\"";
		assertTrue("Expected string - '" + wsdlCharset +"' NOT found", wsdlFileContent.contains(wsdlCharset));		
		logger.debug("testWsdlContentDisplay1()");
	}
	
	@Test
	public void wsdlContentDisplay2() throws Exception {
		logger.debug("testWsdlContentDisplay2()");
		HttpTestClient httpClient = HttpTestClient.getInstance();
		httpClient.port = serverUri.toASCIIString().substring(17);
		
		Map queryParams = new HashMap();
		String url = serverUri.toASCIIString() + "/services/advertise/UniqueIDService/v1?wsdl";
		String wsdlFileContent = httpClient.getResponse(url, queryParams);
		logger.debug("browser content=" +wsdlFileContent);
		CharSequence wsdlCharset = "wsdl:definitions xmlns:wsdl=";
		assertTrue("Expected string - '" + wsdlCharset +"' NOT found", wsdlFileContent.contains(wsdlCharset));
		wsdlCharset = "wsdl:service name=\"AdvertisingUniqueIDServiceV1\"";
		assertTrue("Expected string - '" + wsdlCharset +"' NOT found", wsdlFileContent.contains(wsdlCharset));		
		logger.debug("testWsdlContentDisplay2()");
	}
	
	@Test
	public void wsdlContentDisplay3() throws Exception {
		logger.debug("testWsdlContentDisplay2()");
		HttpTestClient httpClient = HttpTestClient.getInstance();
		httpClient.port = serverUri.toASCIIString().substring(17);
		
		Map queryParams = new HashMap();
		String body ="{\"greet\":[\"Test\"],}";
		String url1 = serverUri.toASCIIString() + "/ws/spf/foo/asdb?wsdl&X-TURMERIC-SERVICE-NAME=AdvertisingUniqueIDServiceV1";
		Response response = httpClient.getResponseDB(new Request(url1), queryParams, body, "POST");
		logger.debug("browser content=" + response);
		CharSequence wsdlCharset = "wsdl:definitions xmlns:wsdl=";
		assertTrue("Expected string - '" + wsdlCharset +"' NOT found", response.getBody().contains(wsdlCharset));
		wsdlCharset = "wsdl:service name=\"AdvertisingUniqueIDServiceV1\"";
		assertTrue("Expected string - '" + wsdlCharset +"' NOT found", response.getBody().contains(wsdlCharset));		
		logger.debug("testWsdlContentDisplay2()");
	}
	
	@Test
	public void wsdlContentDisplay4() throws Exception {
		logger.debug("testWsdlContentDisplay2()");
		Response response = null;
		HttpTestClient httpClient = HttpTestClient.getInstance();
		httpClient.port = serverUri.toASCIIString().substring(17);
		Map queryParams = new HashMap();
		try {
			String body ="{\"greet\":[\"Test\"],}";
			String url = serverUri.toASCIIString() + "/services/abc/UniqueIDService/v1?wsdl";
			response = httpClient.getResponseDB(new Request(url), queryParams, body, "POST");
			logger.debug(response.getBody());
			Assert.assertEquals(response.getStatusCode(), 404);
		} catch(Exception ex){
			logger.debug("Error - Expected string - '" + response +"' NOT found");
			fail("Error - Expected string - '" + response + "' NOT found'");
		}
		logger.debug("testWsdlContentDisplay2()");
	}
		
}