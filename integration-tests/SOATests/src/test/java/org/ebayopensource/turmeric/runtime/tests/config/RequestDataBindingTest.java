/*******************************************************************************
 * Copyright (c) 2006-2010 eBay Inc. All Rights Reserved.
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *******************************************************************************/
package org.ebayopensource.turmeric.runtime.tests.config;

import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.not;

import java.util.Properties;

import org.ebayopensource.turmeric.runtime.common.impl.utils.ParseUtils;
import org.ebayopensource.turmeric.runtime.common.types.SOAConstants;
import org.ebayopensource.turmeric.runtime.spf.impl.internal.config.ServiceConfigManager;
import org.ebayopensource.turmeric.runtime.tests.common.jetty.AbstractWithServerTest;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import com.ebay.kernel.bean.configuration.BeanConfigCategoryInfo;
import com.ebay.kernel.service.invocation.SvcChannelStatus;
import com.ebay.kernel.service.invocation.SvcInvocationConfig;
import com.ebay.kernel.service.invocation.actionmanager.RemoteSvcInvocationActionManagerAdapter;
import com.ebay.kernel.service.invocation.client.http.HttpClient;
import com.ebay.kernel.service.invocation.client.http.Request;
import com.ebay.kernel.service.invocation.client.http.Response;

public class RequestDataBindingTest extends AbstractWithServerTest {
	private static final String serviceName = "defaultBinding";
	private String oldSysProp=null;

	@Before
	public void setUp() throws Exception{
		oldSysProp = System.getProperty(ParseUtils.SYS_PROP_CONFIG_SCHEMA_CHECK);
		System.setProperty(ParseUtils.SYS_PROP_CONFIG_SCHEMA_CHECK, "ERROR");
		ServiceConfigManager.getInstance().setConfigTestCase("testconfig", true);
	}
	
	@After
	public void tearDown(){
	  if(oldSysProp == null){
		  Properties p = System.getProperties();
		  p.remove(ParseUtils.SYS_PROP_CONFIG_SCHEMA_CHECK);
		  System.setProperties(p);
	  }else{
		  System.setProperty(ParseUtils.SYS_PROP_CONFIG_SCHEMA_CHECK, oldSysProp);
	  }
	}
	/**
	 * @check  Exceptions need to be handled
	 */
	@Test
	public  void dataBinding() throws Exception {
		String host = serverUri.getHost();
		String port = String.valueOf( serverUri.getPort() );

		SvcInvocationConfig svcInvocationConfig = new SvcInvocationConfig(
				BeanConfigCategoryInfo.createBeanConfigCategoryInfo(
						"org.ebayopensource.turmeric.runtime.tests.config.RequestDataBindingTest",
						null, "test", false, false, null, null, true),
				"RequestDataBindingTest", SvcChannelStatus.MARK_UP, host, port,
				false, false);
		svcInvocationConfig.createConnectionConfig(4, 8);
		HttpClient httpClient = new HttpClient(svcInvocationConfig,
				new RemoteSvcInvocationActionManagerAdapter(
						svcInvocationConfig, 2, 10000));
		Request request = new Request(serverUri.toURL());
		request.addHeader("CONTENT-TYPE", "text/plain; charset=UTF-8");
		request.addHeader("X-TURMERIC-SERVICE-NAME", "{"
				+ SOAConstants.DEFAULT_SERVICE_NAMESPACE + "}" + serviceName);
		request.addHeader("X-TURMERIC-OPERATION-NAME", "echoString");
		request.addHeader("X-TURMERIC-RESPONSE-DATA-FORMAT", "NV");
		request.addParameter("Message(0)", "Milpitas");
		request.setMethod(Request.POST);

		Response response = httpClient.invoke(request);
		String body = response.getBody();

		Assert.assertThat("body doesn't contain exception", body,
				not(containsString("exception-id")));
		Assert.assertThat("body contains message", body,
				containsString("Message"));
		Assert.assertThat("body contains message", body,
				containsString("Milpitas"));
	}
}
