/*******************************************************************************
 * Copyright (c) 2006-2010 eBay Inc. All Rights Reserved.
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 *******************************************************************************/
package org.ebayopensource.turmeric.qajunittests.advertisinguniqueidservicev1.soap;

import java.net.MalformedURLException;

import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SOAPHeadersTest {
	protected final Logger logger = LoggerFactory.getLogger(SOAPHeadersTest.class);

	/*
	 * Test to client-side handler
	 * Verify headers reach server 
	 */
	/**
	 * @check  Exceptions need to be handled
	 */
	@Test
	public void headersThruHandler() throws MalformedURLException{
		logger.debug(" ** SOAP Headers Test thru Client-side Handler Begins ** ");
		logger.debug(" ** SOAP Headers Test thru Client-side Handler ends ** ");
	}
	/*
	 * Test through Request Context
	 * Verify headers reach server
	 */
	/**
	 * @check  Exceptions need to be handled
	 */
	@Test
	public void headersThruRequestContext(){
		logger.debug(" ** SOAP Headers Test thru Request Context Begins ** ");
		logger.debug(" ** SOAP Headers Test thru Request Context Ends ** ");
	}
	/*
	 * Test thru ServiceIntf
	 * Verify headers
	 */
	/**
	 * @check  Exceptions need to be handled
	 */
	@Test
	public void headersThruSession(){
		logger.debug(" ** SOAP Headers Test thru Session Begins ** ");		
		logger.debug(" ** SOAP Headers Test thru Session ends ** ");
	}
	/**
	 * @check  Exceptions need to be handled
	 */
	@Test
	public void multipleHeadersThruRequestContext(){
		logger.debug(" ** Multiple SOAP Headers Test thru Request Context Begins ** ");
		logger.debug(" ** Multiple SOAP Headers Test thru Request Context Ends ** ");
	}
	/*
	 * Test thru ServiceIntf
	 * Verify headers
	 */
	/**
	 * @check  Exceptions need to be handled
	 */
	@Test
	public void multipleHeadersThruSession(){
		logger.debug(" ** Multiple SOAP Headers Test thru Session Begins ** ");		
		logger.debug(" ** SOAP Headers Test thru Session ends ** ");
	}
}
