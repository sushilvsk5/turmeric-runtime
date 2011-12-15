/*******************************************************************************
 * Copyright (c) 2006-2010 eBay Inc. All Rights Reserved.
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *******************************************************************************/
/**
 * 
 */
package org.ebayopensource.turmeric.runtime.tests.jaxb.reflection;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotSame;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;

import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author wdeng
 *
 */
public class IntrospectionTest  {
	
	private final Logger logger = LoggerFactory.getLogger(IntrospectionTest.class);

	@Test
	public void privateFieldPublicPropertyMarshalUnmarshal() throws Exception {
		logger.debug("Start testPrivateFieldPublicPropertyMarshalUnmarshal.");
		PrivateFieldPublicProperty obj = new PrivateFieldPublicProperty("JAXB");
		marshalUnmarshalObject(obj, false);
	}

	@Test
	public void publicFieldPublicPropertyMarshalUnmarshal() throws Exception {
		logger.debug("Start testPublicFieldPublicPropertyMarshalUnmarshal.");
		PublicFieldPublicProperty obj = new PublicFieldPublicProperty("JAXB");
		marshalUnmarshalObject(obj, false);
	}

	@Test
	public void privateFieldPrivatePropertyMarshalUnmarshal() throws Exception {
		logger.debug("Start testPrivateFieldPrivatePropertyMarshalUnmarshal.");
		PrivateFieldPrivateProperty obj = new PrivateFieldPrivateProperty("JAXB");
		marshalUnmarshalObject(obj, true);
	}
	
	private void marshalUnmarshalObject(Object obj, boolean negative) throws Exception {
		JAXBContext ctx = JAXBContext.newInstance(new Class[]{obj.getClass()});
		Marshaller m = ctx.createMarshaller();
		ByteArrayOutputStream os = new ByteArrayOutputStream();
		m.marshal(obj,os);
		String xml = os.toString();
		System.out.println(xml);
		ByteArrayInputStream is = new ByteArrayInputStream(os.toByteArray());
		Unmarshaller u = ctx.createUnmarshaller();
		Object newObj = u.unmarshal(is);
		if (!negative)
			assertEquals(obj, newObj);
		else
			assertNotSame(obj, newObj);
	}
}
