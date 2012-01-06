/*******************************************************************************
 * Copyright (c) 2006-2010 eBay Inc. All Rights Reserved.
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *******************************************************************************/
package org.ebayopensource.turmeric.runtime.tests.parser.nv;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.xml.namespace.QName;
import javax.xml.stream.XMLStreamException;

import org.ebayopensource.turmeric.runtime.binding.impl.parser.NamespaceConvention;
import org.ebayopensource.turmeric.runtime.binding.impl.parser.nv.NVStreamParser;
import org.ebayopensource.turmeric.runtime.binding.impl.parser.nv.NVStreamWriter;
import org.ebayopensource.turmeric.runtime.binding.utils.URLDecoderInputStream;
import org.ebayopensource.turmeric.runtime.common.impl.internal.schema.DataElementSchemaImpl;
import org.junit.Ignore;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class UTF8DecodingTest {

	private final Logger logger = LoggerFactory
			.getLogger(UTF8DecodingTest.class);

	/**
	 * @param args
	 */
	@Test
	public void payPalIinput() throws Exception {
		logger.debug("Starting payPalIinput");
		doTest("%CE%B5%CE%BB%CE%BB%CE%B7%CE%BD%CE%B9%CE%BA%CE%AE");
		logger.debug("Finishing payPalIinput");
	}

	private static final String UNENCODED = "a%\u03a3\u304b";
	private static final String ENCODED = "a%25%CE%A3%E3%81%8B";
	private static final String ENCODING = "UTF-8";
	private static final String NS = "http://foo.com";
	private static final String LOCAL_NAME = "message";

	@Test
	public void encodeTest() throws Exception {
		logger.debug("Starting encodeTest");
		String nvEncoded = nvEncode(UNENCODED);
		String javaNative = UNENCODED;
		String javaEncoded = URLEncoder.encode(UNENCODED, ENCODING);
		logger.debug("Encode Test");
		logger.debug("  Original:   " + nativeToAscii(UNENCODED));
		logger.debug("  NV Encode:  " + nativeToAscii(nvEncoded));
		logger.debug("  URLEncoder: " + nativeToAscii(javaEncoded));
		logger.debug("  Expected:   " + nativeToAscii(ENCODED));
		assertEquals(nativeToAscii(ENCODED), nativeToAscii(nvEncoded));
		logger.debug("Finishing encodeTest");
	}

	@Test
	public void decodeTest() throws Exception {
		logger.debug("Starting decodeTest");
		String nvDecoded = nvDecode(ENCODED);
		String javaDecoded = URLDecoder.decode(ENCODED, ENCODING);
		logger.debug("Decode Test");
		logger.debug("  Original:   " + nativeToAscii(ENCODED));
		logger.debug("  NV Decode:  " + nativeToAscii(nvDecoded));
		logger.debug("  URLDecoder: " + nativeToAscii(javaDecoded));
		logger.debug("  Expected:   " + nativeToAscii(UNENCODED));
		assertEquals(nativeToAscii(UNENCODED), nativeToAscii(nvDecoded));
		logger.debug("Finishing decodeTest");
	}

	@Test
	public void roundTripTest() throws Exception {
		logger.debug("Starting roundTripTest");
		String nvDecoded = nvDecode(ENCODED);
		String nvEncoded = nvEncode(nvDecoded);
		logger.debug("Round Trip Test");
		logger.debug("  Original:  " + nativeToAscii(ENCODED));
		logger.debug("  NV Decode: " + nativeToAscii(nvDecoded));
		logger.debug("  Expected:  " + nativeToAscii(UNENCODED));
		logger.debug("  NV Encode: " + nativeToAscii(nvEncoded));
		logger.debug("  Expected:  " + nativeToAscii(ENCODED));
		assertEquals(nativeToAscii(ENCODED), nativeToAscii(nvEncoded));
		assertEquals(nativeToAscii(UNENCODED), nativeToAscii(nvDecoded));
		logger.debug("Finishing roundTripTest");
	}

	private static String nativeToAscii(String value) {
		StringBuilder buf = new StringBuilder();
		for (char c : value.toCharArray()) {
			switch (c) {
			case '\\':
				buf.append("\\\\");
				break;
			case '\t':
				buf.append("\\t");
				break;
			case '\n':
				buf.append("\\n");
				break;
			case '\r':
				buf.append("\\r");
				break;
			case '\f':
				buf.append("\\f");
				break;
			default:
				if ((c < 0x0020) || (c > 0x007e)) {
					String hexValue = Integer.toHexString(c).toUpperCase();
					while (hexValue.length() < 4) {
						hexValue = "0" + hexValue;
					}
					buf.append("\\u" + hexValue);
				} else {
					buf.append("" + c);
				}
			}
		}
		return buf.toString();
	}

	private static String nvEncode(final String unencodedValue)
			throws XMLStreamException, IOException {
		ByteArrayOutputStream baos = new ByteArrayOutputStream();

		Map<String, String> opts = new HashMap<String, String>();
		opts.put(NVStreamWriter.KEY_USE_SCHEMA_INFO, "true");
		opts.put(NVStreamWriter.KEY_QUOTE_VALUE, "false");
		opts.put(NVStreamWriter.KEY_ENCODE_VALUE, "true");
		List<String> namespaces = new ArrayList<String>(1);
		namespaces.add(NS);
		Map<String, List<String>> ns2Prefix = new HashMap<String, List<String>>();
		Map<String, String> prefix2NS = new HashMap<String, String>();
		NamespaceConvention.buildNsPrefixes(namespaces, ns2Prefix, prefix2NS);

		NamespaceConvention conv = NamespaceConvention
				.createSerializationNSConvention(NS, prefix2NS, ns2Prefix);
		QName name = new QName(NS, LOCAL_NAME);
		NVStreamWriter writer = new NVStreamWriter(conv, baos,
				Charset.forName(ENCODING), name, new DataElementSchemaImpl(
						name, 1), opts, false);
		writer.writeStartElement("", LOCAL_NAME, NS);
		writer.writeCharacters(unencodedValue);
		writer.writeEndElement();
		writer.close();
		baos.close();

		// Convert to string and remove "message=".
		return new String(baos.toByteArray(), ENCODING).replace(LOCAL_NAME
				+ "=", "");
	}

	private static String nvDecode(final String encodedValue)
			throws UnsupportedEncodingException, XMLStreamException {
		String message = LOCAL_NAME + "=" + encodedValue;
		ByteArrayInputStream bais = new ByteArrayInputStream(
				message.getBytes(ENCODING));
		NVStreamParser parser = new NVStreamParser(bais,
				Charset.forName(ENCODING), null);
		parser.parseLine();
		String nvDecoded = parser.getValue();
		return nvDecoded;
	}

	private void doTest(String inputString) throws Exception {
		String decoded = URLDecoder.decode(inputString, "UTF-8");
		ByteArrayInputStream is = new ByteArrayInputStream(
				inputString.getBytes());
		InputStreamReader isr = new InputStreamReader(
				new URLDecoderInputStream(is), "UTF-8");

		char[] buf = new char[30];
		int count = isr.read(buf);
		if (-1 == count) {
			assertFalse("no input", false);
		}
		assertEquals(decoded, new String(buf, 0, count));
	}

}
