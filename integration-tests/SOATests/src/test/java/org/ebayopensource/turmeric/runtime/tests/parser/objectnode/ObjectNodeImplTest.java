/*******************************************************************************
 * Copyright (c) 2006-2010 eBay Inc. All Rights Reserved.
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *******************************************************************************/
package org.ebayopensource.turmeric.runtime.tests.parser.objectnode;

import java.io.InputStream;

import javax.xml.namespace.QName;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;

import junit.framework.Assert;

import org.ebayopensource.turmeric.runtime.binding.impl.parser.objectnode.ObjectNodeStreamReader;
import org.ebayopensource.turmeric.runtime.binding.impl.parser.objectnode.StreamableObjectNodeImpl;
import org.ebayopensource.turmeric.runtime.binding.objectnode.ObjectNode;
import org.ebayopensource.turmeric.runtime.tests.binding.jaxb.BaseSerDeserTest;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;


public class ObjectNodeImplTest extends BaseSerDeserTest {

	@Test
	public void testIsAttributeTest() throws Exception {
		logger.debug("**** Starting testIsAttributeTest");
		InputStream inStream = new ObjectNodeImplTest().getClass()
				.getResourceAsStream("books.xml");

		XMLStreamReader xmlStreamReader = XMLInputFactory.newInstance()
				.createXMLStreamReader(inStream);

		ObjectNodeStreamReader oNodeReader = new ObjectNodeStreamReader(
				xmlStreamReader);

		StreamableObjectNodeImpl rootNode = new StreamableObjectNodeImpl(
				oNodeReader);

		ObjectNode attribNode = rootNode.nextChild().nextChild()
				.getAttribute(0);
		Assert.assertTrue (attribNode.isAttribute());
		QName nodeName=attribNode.getNodeName();
		Assert.assertTrue ("category".equals(nodeName.getLocalPart()));
		logger.debug("**** Ending testIsAttributeTest");

	}

	@Test
	public void testGetChildNodes() throws Exception {
		logger.debug("**** Starting testGetChildNodes");

		// ObjectNode representation for books.xml and traverse to the root
		// element

		// W3C DOM - For comparison. ////////////////////
		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
		factory.setIgnoringComments(true);
		factory.setCoalescing(true); // Convert CDATA to Text nodes
		factory.setNamespaceAware(false); // No namespaces: this is default
		factory.setValidating(false); // Don't validate DTD: also default

		DocumentBuilder parser = factory.newDocumentBuilder();
		Document docToCompare = parser.parse(ObjectNodeImplTest.class
				.getResourceAsStream("books.xml"));
		// /////////////////////////////////////////////////////

		InputStream inStream = ObjectNodeImplTest.class
				.getResourceAsStream("books.xml");

		XMLStreamReader xmlStreamReader = XMLInputFactory.newInstance()
				.createXMLStreamReader(inStream);

		ObjectNodeStreamReader oNodeReader = new ObjectNodeStreamReader(
				xmlStreamReader);

		ObjectNode rootNodeON = new StreamableObjectNodeImpl(oNodeReader);

		verifyNode(0, rootNodeON, docToCompare);

		logger.debug("**** Ending testGetChildNodes");
	}

	private static void verifyNode(int level, ObjectNode node,
			Node nodeToCompare) throws XMLStreamException {
		final Logger logger = LoggerFactory.getLogger(ObjectNodeImplTest.class);
		
		NodeList listToCompare = nodeToCompare.getChildNodes();
		int i = 0;
		for (ObjectNode child : node.getChildNodes()) {
			Node childNodeToCompare = null;
			// to skip mixed text content since ObjectNodeStreamReader does
			// not support/ignores mixed text contents
			do {
				childNodeToCompare = listToCompare.item(i++);
			} while (childNodeToCompare.getNodeType() != Node.ELEMENT_NODE);

			printTags(level);
			logger.debug("Node : " + child.getNodeName());
			verifyAttributes(level + 1, child, childNodeToCompare);

			if (child.hasChildNodes()) {
				Assert.assertEquals(child.getNodeName().toString(), childNodeToCompare
						.getNodeName());
				verifyNode(level + 1, child, childNodeToCompare);
			} else {
				verifyValue(level + 1, child, childNodeToCompare);
			}
		}
	}

	private static void verifyAttributes(int level, ObjectNode node,
			Node nodeToCompare) {
		final Logger logger = LoggerFactory.getLogger(ObjectNodeImplTest.class);
		
		int i = 0;
		for (ObjectNode attr : node.getAttributes()) {
			Node attrToCompare = nodeToCompare.getAttributes().item(i++);
			printTags(level);
			logger.debug("Attribute Name [" + attr.getNodeName()
					+ "], Value [" + attr.getNodeValue() + "]");
			Assert.assertEquals(attr.getNodeName().toString(), attrToCompare
					.getNodeName());
			Assert.assertEquals(attr.getNodeValue().toString(), attrToCompare
					.getNodeValue());
		}

	}

	private static void verifyValue(int level, ObjectNode node,
			Node nodeToCompare) {
		
		final Logger logger = LoggerFactory.getLogger(ObjectNodeImplTest.class);
		
		printTags(level);
		logger.debug("Value : " + node.getNodeValue());
		Assert.assertEquals(node.getNodeValue().toString(), nodeToCompare
				.getChildNodes().item(0).getNodeValue());

	}

	private static void printTags(int n) {
		for (int i = 0; i < n; i++) {
			System.out.print("\t");
		}
	}
}
