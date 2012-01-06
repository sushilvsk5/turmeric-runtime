/*******************************************************************************
 * Copyright (c) 2006-2010 eBay Inc. All Rights Reserved.
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *******************************************************************************/
package org.ebayopensource.turmeric.runtime.tests.config;

import org.ebayopensource.turmeric.runtime.common.exceptions.ServiceCreationException;
import org.ebayopensource.turmeric.runtime.common.exceptions.ServiceException;
import org.ebayopensource.turmeric.runtime.common.exceptions.ServiceNotFoundException;
import org.ebayopensource.turmeric.runtime.spf.impl.internal.config.ServiceConfigManager;
import org.ebayopensource.turmeric.runtime.tests.common.AbstractTurmericTestCase;
import org.ebayopensource.turmeric.runtime.tests.common.util.ExceptionUtils;
import org.junit.After;
import org.junit.Test;


public class ServiceConfigNegativeTest  extends AbstractTurmericTestCase {
	/**
	 * @check  Exceptions need to be handled
	 */
	@Test
	public void serviceConfig() throws Exception {
		ServiceConfigManager configManager = ServiceConfigManager.getInstance();
		try {
			configManager.setConfigTestCase("confignegative1", "testconfig");
		} catch (ServiceException e) {
			ExceptionUtils.checkException(e, ServiceNotFoundException.class, "No configuration defined for service: test2");
		}
		try {
			configManager.setConfigTestCase("confignegative2", "testconfig");
		} catch (ServiceCreationException e) {
			ExceptionUtils.checkException(e, ServiceCreationException.class, "Error parsing configuration file META-INF/soa/services/confignegative2/test2/ServiceConfig.xml: org.xml.sax.SAXParseException");
		}
		try {
			configManager.setConfigTestCase("confignegative3", "testconfig");
		} catch (ServiceCreationException e) {
			ExceptionUtils.checkException(e, ServiceCreationException.class, "META-INF/soa/services/confignegative3/test2/ServiceConfig.xml: No service-instance-config section found");
		}
		try {
			configManager.setConfigTestCase("confignegative5", "testconfig");
		} catch (ServiceCreationException e) {
			ExceptionUtils.checkException(e, ServiceCreationException.class, "Cannot find group: NoSuchGroup");
		}
		try {
			configManager.setConfigTestCase("confignegative6", "testconfig");
		} catch (ServiceCreationException e) {
			ExceptionUtils.checkException(e, ServiceCreationException.class, "Can't find chain: NoSuchChain");
		} finally {
			configManager.setConfigTestCase("testconfig");
		}

	}
	@After
	public void tearDown() throws Exception {
		ServiceConfigManager.getInstance().setConfigTestCase("config");
	}
	}