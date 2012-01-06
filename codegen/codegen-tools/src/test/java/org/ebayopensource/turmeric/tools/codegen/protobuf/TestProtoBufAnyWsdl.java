package org.ebayopensource.turmeric.tools.codegen.protobuf;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.net.URI;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.tools.JavaCompiler;
import javax.tools.JavaFileObject;
import javax.tools.SimpleJavaFileObject;
import javax.tools.ToolProvider;
import javax.xml.datatype.DatatypeConfigurationException;
import javax.xml.datatype.DatatypeFactory;
import javax.xml.namespace.QName;

import org.ebayopensource.turmeric.tools.codegen.AbstractServiceGeneratorTestCase;
import org.ebayopensource.turmeric.tools.codegen.ServiceGenerator;
import org.ebayopensource.turmeric.tools.codegen.handler.UserResponseHandler;
import org.ebayopensource.turmeric.tools.codegen.util.CodeGenClassLoader;
import org.ebayopensource.turmeric.tools.codegen.util.CodeGenUtil;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

import com.google.protobuf.ByteString;

public class TestProtoBufAnyWsdl extends AbstractServiceGeneratorTestCase {

	static Set<String> compiledTypes = new HashSet<String>();
	File destDir = null;
	String nam = null;
	ProtoFileParser parser = null;
	List<String> protoMessageList = new ArrayList<String>();

	public static String getPackageFromNamespace(String namespace) {

		// Using the method used by JAXB directly to avoid potential conflicts
		// with JAXB generated code
		// Therefore commenting out the old code which is based on JAXB 2.0 spec
		return com.sun.tools.xjc.api.XJC.getDefaultPackageName(namespace);
	}

	List<String> wsdlFileName = new ArrayList<String>();
	File protoFile = null;
	Map<String, String> xsdToProtoType = new HashMap<String, String>();
	Map<String, String> xsdToJaxbType = new HashMap<String, String>();

	@Before
	public void init() {
		destDir = testingdir.getDir();

		/*
		 * Add wsdl which needs to be tested for protobuf to the list Make sure
		 * you also add the expected text result file for the wsdl.
		 */
		wsdlFileName.add("TestWsdlXsdTypes");
		// wsdlFileName.add("TestWsdlBug19005");
		// wsdlFileName.add("TestWsdlComplexType");
		// wsdlFileName.add("TestWsdlComplexTypeExtended");
		// wsdlFileName.add("TestWsdlIDNotSupported");
		// wsdlFileName.add("authentication");
		// wsdlFileName.add("FindingService");
		// wsdlFileName.add("TestWsdlBug");

		// wsdlFileName.add("TestKeywordsAsNames");
		// wsdlFileName.add("TestAllVariationsWsdl");
		// wsdlFileName.add("TestWsdlEnum");
		// wsdlFileName.add("TestWsdlChoiceAttrGroup");
		// wsdlFileName.add("IntOps");
		// wsdlFileName.add("TestWsdlChoiceAttrGroup");

		// wsdlFileName.add("TestWsdlComplexTypeCC");
		// wsdlFileName.add("test");

		// wsdlFileName.add("TestWsdlOtherTypes");
		// wsdlFileName.add("TestWsdlListTypes");

		// wsdlFileName.add("TestWsdlComplexTypeSC1");
		// wsdlFileName.add("TestWsdlComplexTypeSC2");
		// wsdlFileName.add("TestWsdlBug19062");
		// wsdlFileName.add("TestWsdlBug19094");

		// wsdlFileName.add("TestingBugs");

		/*
		 * List for xsd types conversion to protobuf.
		 */

		xsdToProtoType.put("int", "sint32");
		xsdToProtoType.put("integer", "sint32");
		xsdToProtoType.put("byte", "sint32");
		xsdToProtoType.put("boolean", "bool");
		xsdToProtoType.put("string", "string");
		xsdToProtoType.put("normalizedString", "string");
		xsdToProtoType.put("positiveInteger", "string");
		xsdToProtoType.put("token", "string");
		xsdToProtoType.put("hexBinary", "bytes");
		xsdToProtoType.put("base64Binary", "bytes");
		xsdToProtoType.put("integer", "sint32");
		xsdToProtoType.put("long", "sint64");
		xsdToProtoType.put("short", "sint32");
		xsdToProtoType.put("unsignedLong", "string");
		xsdToProtoType.put("unsignedInt", "sint64");
		xsdToProtoType.put("unsignedShort", "sint32");
		xsdToProtoType.put("unsignedByte", "sint32");
		xsdToProtoType.put("decimal", "string");
		xsdToProtoType.put("double", "double");
		xsdToProtoType.put("float", "float");
		xsdToProtoType.put("double", "double");

		xsdToProtoType.put("date", "sint64");
		xsdToProtoType.put("duration", "sint64");
		xsdToProtoType.put("dateTime", "sint64");
		xsdToProtoType.put("time", "sint64");
		xsdToProtoType.put("gYearMonth", "sint64");
		xsdToProtoType.put("stringbig", "string");
		xsdToProtoType.put("gYear", "sint64");
		xsdToProtoType.put("gMonthDay", "sint64");
		xsdToProtoType.put("gMonth", "sint64");
		xsdToProtoType.put("gDay", "sint64");
		xsdToProtoType.put("QName", "string");
		xsdToProtoType.put("anyURI", "string");
		xsdToProtoType.put("ID", "string");
		xsdToProtoType.put("NMTOKEN", "string");
		xsdToProtoType.put("NMTOKENS", "string");
		xsdToProtoType.put("ENTITY", "string");
		xsdToProtoType.put("language", "string");
		xsdToProtoType.put("Name", "string");
		xsdToProtoType.put("NCName", "string");
		xsdToProtoType.put("negativeInteger", "string");
		xsdToProtoType.put("nonNegativeInteger", "string");
		xsdToProtoType.put("nonPositiveInteger", "string");
		xsdToProtoType.put("normalizedString", "string");
		xsdToProtoType.put("ENTITIES", "string");

		/*
		 * List for xsd types conversion to jaxb.
		 */

		xsdToJaxbType.put("int", "integer");
		xsdToJaxbType.put("byte", "byte");
		xsdToJaxbType.put("boolean", "boolean");
		xsdToJaxbType.put("string", "string");
		xsdToJaxbType.put("normalizedString", "string");
		xsdToJaxbType.put("positiveInteger", "biginteger");
		xsdToJaxbType.put("token", "string");
		xsdToJaxbType.put("hexBinary", "byte[]");
		xsdToJaxbType.put("base64Binary", "byte[]");
		xsdToJaxbType.put("integer", "integer");
		xsdToJaxbType.put("long", "long");
		xsdToJaxbType.put("short", "short");
		xsdToJaxbType.put("unsignedLong", "biginteger");
		xsdToJaxbType.put("stringbig", "biginteger");
		xsdToJaxbType.put("unsignedInt", "long");
		xsdToJaxbType.put("unsignedShort", "integer");
		xsdToJaxbType.put("unsignedByte", "short");
		xsdToJaxbType.put("decimal", "bigdecimal");
		xsdToJaxbType.put("double", "double");
		xsdToJaxbType.put("float", "float");
		xsdToJaxbType.put("double", "double");

		xsdToJaxbType.put("date", "xmlgregoriancalendar");
		xsdToJaxbType.put("duration", "duration");
		xsdToJaxbType.put("dateTime", "xmlgregoriancalendar");
		xsdToJaxbType.put("time", "xmlgregoriancalendar");
		xsdToJaxbType.put("gYearMonth", "xmlgregoriancalendar");
		xsdToJaxbType.put("gYear", "xmlgregoriancalendar");
		xsdToJaxbType.put("gMonthDay", "xmlgregoriancalendar");
		xsdToJaxbType.put("gMonth", "xmlgregoriancalendar");
		xsdToJaxbType.put("gDay", "xmlgregoriancalendar");
		xsdToJaxbType.put("QName", "QName");
		xsdToJaxbType.put("anyURI", "string");
		xsdToJaxbType.put("language", "string");
		xsdToJaxbType.put("ID", "string");
		xsdToJaxbType.put("NMTOKEN", "string");
		xsdToJaxbType.put("NMTOKENS", "string");
		xsdToJaxbType.put("ENTITY", "string");
		xsdToJaxbType.put("Name", "string");
		xsdToJaxbType.put("NCName", "string");
		xsdToJaxbType.put("negativeInteger", "biginteger");
		xsdToJaxbType.put("nonNegativeInteger", "biginteger");
		xsdToJaxbType.put("nonPositiveInteger", "biginteger");
		xsdToJaxbType.put("ENTITIES", "string");

	}

	private void cleanDestination(File wsdlpath, String name1) {
		compiledTypes.clear();
		CodeGenUtil.deleteContentsOfDir(new File(destDir + "/gen-src"));
		CodeGenUtil.deleteContentsOfDir(new File(destDir + "/gen-meta-src"));
		CodeGenUtil.deleteContentsOfDir(new File(destDir + "/meta-src"));
		CodeGenUtil.deleteContentsOfDir(new File(destDir + "/bin"));

	}

	private void setClassloader(URL[] urls) {
		URLClassLoader loader = new URLClassLoader(urls, Thread.currentThread()
				.getContextClassLoader());
		Thread.currentThread().setContextClassLoader(loader);
	}

	private File getProtoFilePath(String adminName) {
		protoFile = new File(destDir, "meta-src/META-INF/soa/services/proto/"
				+ adminName + "/" + adminName + ".proto");

		return protoFile;
	}

	private void getProtoMessages() {

		parser = ProtoFileParser.newInstance(getProtoFilePath(nam));
		protoMsg = parser.parse();

	}

	List<Message> protoMsg = null;

	/*
	 * Adding the message name to a list. Enum message is appended with _e.
	 */

	private List<String> addToMessageList() {

		for (Message m : protoMsg) {
			if (m.getClass().getName().contains("Enum")) {
				EnumMessage em = (EnumMessage) m;
				protoMessageList.add(em.getEnumName() + "_e");
				continue;
			}
			protoMessageList.add(m.getMessageName());
		}

		return protoMessageList;
	}

	/*
	 * assert if the tags assigned to PMD information at the bottom of the proto
	 * file is correct and same as the one in the message section of the proto
	 * file
	 */
	private void assertTagAssignedToPMDInfo() {

		Map<String, List<String>> msgMap1 = pmdInfoForAllMessages(protoFile,
				protoMessageList, parser);

		Map<String, List<String>> msgMap2 = getParamSqequenceInfoForAllMessages(protoFile);
		for (String msgName : protoMessageList) {

			List<String> pmdInfo1 = msgMap1.get(msgName);
			List<String> pmdInfoNew = new ArrayList<String>();
			for (String s : pmdInfo1) {
				pmdInfoNew.add(s.replace("@", "").trim());
			}
			if (msgName.contains("_e")) {
				msgName = msgName.substring(0, msgName.length() - 2) + "Enum";
			}
			List<String> pmdInfo2 = msgMap2.get(msgName);
			for (String s : pmdInfo2) {
				boolean hasit = false;
				for (String s1 : pmdInfoNew) {
					if (s.equalsIgnoreCase(s1)) {
						hasit = true;
					}
				}
				if (!hasit)
					Assert.assertTrue("PMD info is incorrect for field " + s
							+ " in message " + msgName, hasit);
			}

		}

	}

	@Test
	@Ignore("protobuf tests are not included for opensource since the proto compilation logic doest not work in linux env")
	public void testEproto() throws Exception {

		File wsdlpath = null;
		File fileExp = null;
		File gensrc = new File(destDir, "gen-src");
		File bin = new File(destDir, "bin");

		URL[] urls = { new URL("file:/" + destDir.getAbsolutePath() + "/bin/"),
				destDir.toURI().toURL(), gensrc.toURI().toURL() };

		setClassloader(urls);

		for (String name1 : wsdlFileName) {

			cleanDestination(wsdlpath, name1);
			nam = name1;

			String wsdlNSToPkg = getPackageFromNamespace("http://codegen.tools.soaframework.test.ebay.com");
			// wsdlNSToPkg =
			// getPackageFromNamespace("http://www.ebay.com/marketplace/search/v1/services");

			wsdlpath = getProtobufRelatedInput(name1 + ".wsdl");
			generateJaxbClasses(wsdlpath.getAbsolutePath(),
					destDir.getAbsolutePath(), bin, nam);

			getProtoMessages();
			assertTagAssignment();

			addToMessageList();
			// This method needs to be included.
			// assertTagAssignedToPMDInfo();

			fileExp = getProtobufRelatedInput(name1 + ".txt");

			Class<?> protoClass = Thread.currentThread()
					.getContextClassLoader()
					.loadClass(wsdlNSToPkg + ".proto." + nam);

			Class<?> eprotoClass = null;
			Class<?> jaxbClass = null;
			List<Message> enumMessagesGen = new ArrayList<Message>();
			List<Message> typeMessagesGen = new ArrayList<Message>();
			for (Message m : protoMsg) {
				if (m.getClass().getName().contains("Enum")) {
					enumMessagesGen.add(m);
					continue;
				}
				typeMessagesGen.add(m);
			}
			List<Message> enumMessagesExp = new ArrayList<Message>();
			List<Message> typeMessagesExp = new ArrayList<Message>();

			WSDLInformationParser info = new WSDLInformationParser(fileExp);

			List<Message> msgInfo = info.parse();

			for (Message ms : msgInfo) {
				if (ms.getClass().getName().contains("Enum")) {
					enumMessagesExp.add(ms);
					continue;
				}
				typeMessagesExp.add(ms);
			}
			boolean hasMsg = false;
			boolean hasEle = false;
			boolean hasEnumMsg = false;

			if (enumMessagesGen.size() == enumMessagesExp.size()) {

				for (Message m : enumMessagesGen) {

					if (m.getClass().getName().contains("EnumMessage")) {
						for (Message ms : enumMessagesExp) {
							if (m.getMessageName().equals(ms.getMessageName())) {
								hasEnumMsg = true;
								EnumMessage em = (EnumMessage) m;
								Assert.assertTrue(((EnumMessage) ms)
										.getValues().size() == em.getValues()
										.size());
							}
						}
						Assert.assertTrue("Enum message " + m.getMessageName()
								+ " is not found in exp", hasEnumMsg);
						hasEnumMsg = false;
						continue;
					}
				}

			} else {
				Assert.assertTrue(
						"No of enum message is not equal in genenerated file and expexted file. For wsdl "
								+ nam, false);
			}

			if (typeMessagesGen.size() == typeMessagesExp.size()) {

				for (Message m : typeMessagesGen) {

					String messageName = m.getMessageName();
					hasMsg = false;
					for (Message ms : typeMessagesExp) {
						if (ms.getMessageName().equals(messageName)) {
							hasMsg = true;
							if (m.getFields().size() == ms.getFields().size()) {
								for (Field el : ms.getFields()) {
									XsdField xel = (XsdField) el;
									hasEle = false;
									for (Field f : m.getFields()) {
										ProtoField pf = (ProtoField) f;
										if (f.getFieldName().equalsIgnoreCase(
												xel.getJaxbName())) {
											hasEle = true;
											Assert.assertTrue(pf.getFieldName()
													.equalsIgnoreCase(
															xel.getJaxbName()));
											if (el.isOptional())
												Assert.assertTrue(
														"field restriction optional is wrong for"
																+ m.getMessageName()
																+ " and field name"
																+ pf.getFieldName(),
														pf.getFieldRestriction()
																.equals("optional"));
											if (el.isList())
												Assert.assertTrue(
														"field restriction repeated is wrong for"
																+ m.getMessageName()
																+ " and field name"
																+ pf.getFieldName(),
														pf.getFieldRestriction()
																.equals("repeated"));
											if (!el.isList()
													&& !el.isOptional())
												Assert.assertTrue(
														"field restriction is wrong for"
																+ m.getMessageName()
																+ " and field name"
																+ pf.getFieldName(),
														pf.getFieldRestriction()
																.equals("required"));
											try {
												Assert.assertTrue(
														"Data type is wrong for"
																+ m.getMessageName()
																+ " and field name"
																+ pf.getFieldName(),
														xsdToProtoType
																.get(xel.getFieldType())
																.equals(pf
																		.getFieldType()));
											} catch (Exception e) {
												System.out.println(f
														.getFieldName());
											}
											break;
										}
									}
									Assert.assertTrue(ms.getMessageName()
											+ " message " + xel.getJaxbName()
											+ "element is not found in exp",
											hasEle);
								}
								break;
							} else {
								System.out
										.println("Mismatch in number of elements in"
												+ m.getMessageName());
							}

						}

					}
					Assert.assertTrue(m.getMessageName()
							+ "is not found in generated proto", hasMsg);

				}

			} else {
				System.out.println("Mismatch found in the number of message");
				boolean found = false;
				for (Message m : typeMessagesGen) {
					for (Message mi : typeMessagesExp) {

						if (m.getMessageName().equals(mi.getMessageName())) {
							found = true;
							break;
						}
					}
					if (!found) {
						System.out.println(m.getMessageName()
								+ " is not found in expected messages");

					}
					found = false;
				}

				for (Message mi : typeMessagesExp) {
					for (Message m : typeMessagesGen) {

						if (m.getMessageName().equals(mi.getMessageName())) {
							found = true;
							break;
						}
					}
					if (!found) {
						System.out.println(mi.getMessageName()
								+ " is not found in generated messages");

					}
					found = false;
				}

			}
			Set<String> setTypes = xsdToProtoType.keySet();
			for (Message ms : msgInfo) {
				String messageName = ms.getMessageName();
				if (ms.getClass().getName().contains("Enum")
						&& messageName.contains("Enum")) {
					messageName = messageName.trim().substring(0,
							messageName.length() - 4);
				}
				String eprotoPath = destDir.getAbsolutePath() + "/gen-src/"
						+ packagetoDirPath(wsdlNSToPkg) + "/proto/extended/E"
						+ messageName + ".java";

				compile(msgInfo, eprotoPath, bin, setTypes, wsdlNSToPkg,
						messageName);

				eprotoPath = destDir.getAbsolutePath() + "/gen-src/"
						+ packagetoDirPath(wsdlNSToPkg) + "/proto/extended/E"
						+ messageName + ".java";
				compileEproto(eprotoPath, bin);

			}

			for (Message ms : msgInfo) {

				String messageName = ms.getMessageName();

				if (ms.getClass().getName().contains("Enum")
						&& messageName.contains("Enum")) {
					messageName = messageName.trim().substring(0,
							messageName.length() - 4);
				}
				/*
				 * String eprotoPath ="generated/gen-src/"+
				 * packagetoDirPath(wsdlNSToPkg)+"/proto/extended/E"
				 * +messageName+".java";
				 * 
				 * List<String> files = new ArrayList<String>(); File f = new
				 * File(eprotoPath); files.add(f.getAbsolutePath()); JavacHelper
				 * helper = new JavacHelper(System.out);
				 * helper.compileJavaSource(files,bin.getAbsolutePath() );
				 */

				jaxbClass = Thread.currentThread().getContextClassLoader()
						.loadClass(wsdlNSToPkg + "." + messageName);
				eprotoClass = Thread
						.currentThread()
						.getContextClassLoader()
						.loadClass(
								wsdlNSToPkg + ".proto.extended.E" + messageName);

				if (ms.getClass().getName().contains("Enum")) {
					EnumMessage ems = (EnumMessage) ms;
					Iterator<String> it = ems.getValues().keySet().iterator();
					for (; it.hasNext();) {
						invokeEnumNewInstance(jaxbClass, eprotoClass, it.next());
					}
					continue;
				}
				Object obj = makeObject(ms, msgInfo, wsdlNSToPkg, xsdToJaxbType);
				invokeNewInstance(jaxbClass, eprotoClass, protoClass,
						messageName, obj);

			}

			for (Message ms : msgInfo) {
				String messageName = ms.getMessageName();

				if (ms.getClass().getName().contains("Enum")
						&& messageName.contains("Enum")) {
					messageName = messageName.substring(0,
							messageName.length() - 4);
				}

				String methodPrefix = "get";

				eprotoClass = Thread
						.currentThread()
						.getContextClassLoader()
						.loadClass(
								wsdlNSToPkg + ".proto.extended.E" + messageName);
				if (ms.getClass().getName().contains("Enum")) {

					invokeEnumGetter(eprotoClass, protoClass, 11, messageName
							+ "Enum");

					continue;
				}
				Object obj = makeProtoObject(msgInfo, ms, wsdlNSToPkg);
				for (Field ele : ms.getFields()) {
					if (ele.getFieldType().equals("boolean") && !ele.isList()) {
						methodPrefix = "is";
					}
					invokeGetters(eprotoClass, eprotoClass, methodPrefix
							+ firstLetterUpperCase(ele.getFieldName()),
							messageName, obj);
					methodPrefix = "get";
				}

			}

			for (Message m : msgInfo) {

				String messageName = m.getMessageName();

				if (m.getClass().getName().contains("Enum")) {
					messageName = messageName.substring(0,
							(messageName.length() - 4));
				}

				eprotoClass = Thread
						.currentThread()
						.getContextClassLoader()
						.loadClass(
								wsdlNSToPkg + ".proto.extended.E" + messageName);

				jaxbClass = Thread.currentThread().getContextClassLoader()
						.loadClass(wsdlNSToPkg + "." + messageName);

				// code to check if the jaxb getter methods are overriden and
				// return types are same:
				Method[] jaxbMtd = jaxbClass.getMethods();
				Method[] methods = eprotoClass.getMethods();
				for (Method jmtd : jaxbMtd) {

					for (Method mtd : methods) {
						if (jmtd.getName().equals(mtd.getName())) {

							System.out
									.println("Method in jaxb pojo "
											+ jmtd.getName()
											+ "is overriden in eproto "
											+ mtd.getName());
							if (jmtd.getReturnType()
									.equals(mtd.getReturnType())) {

								System.out.println("return types are equal");

							} else {
								Assert.fail("return types are not equal");
							}

						}
					}

				}
				// end of code

			}

			for (Message ms : msgInfo) {
				boolean hasParseMtd = false;
				String messageName = ms.getMessageName();

				if (ms.getClass().getName().contains("Enum")) {
					continue;
				}

				eprotoClass = Thread
						.currentThread()
						.getContextClassLoader()
						.loadClass(
								wsdlNSToPkg + ".proto.extended.E" + messageName);

				for (Method parse : eprotoClass.getDeclaredMethods()) {

					if (parse.getName().equals("parseFrom")) {
						hasParseMtd = true;
					}

				}
				// Assert.assertTrue("parse From method is not found in "+ms.getMessageName(),
				// hasParseMtd);
			}

		}

		/*
		 * MyComplexType complextype = (MyComplexType)
		 * jaxbClasses.get("MyComplexType").newInstance();
		 * complextype.setElemA(344.55f); SampleComplexType sample =
		 * (SampleComplexType)
		 * jaxbClasses.get("SampleComplexType").newInstance();
		 * sample.getEnt().add(23); sample.setValue1("test1");
		 * sample.setValue10("test10"); sample.setValue2("test2");
		 * sample.setValue4("test3"); sample.setValue8("test8");
		 * sample.setValue9("test9"); complextype.setElemB(sample);
		 * ExtendMyComplexType extended = (ExtendMyComplexType)
		 * jaxbClasses.get("ExtendMyComplexType").newInstance();
		 * extended.setElemB(sample); extended.setElemA(344.33f);
		 * extended.setElemC("test");
		 * 
		 * 
		 * invokeNewInstance(jaxbClasses.get("ExtendMyComplexType"),eprotoClasses
		 * .get("ExtendMyComplexType"),
		 * protoClass,"ExtendMyComplexType",extended);
		 */

	}

	public Map<String, List<String>> getParamSqequenceInfoForAllMessages(
			File file) {

		ProtoFileParser parser1 = ProtoFileParser
				.newInstance(getProtoFilePath(nam));
		List<Message> msg = parser1.parse();

		return getMessageInfo(msg);
	}

	public Map<String, List<String>> getMessageInfo(List<Message> msg) {
		Map<String, List<String>> msgFields = new HashMap<String, List<String>>();

		for (Message m : msg) {
			String msName = m.getMessageName();
			List<String> list = new ArrayList<String>();
			if (m.getClass().getName().contains("Enum")) {

				EnumMessage em = (EnumMessage) m;
				Iterator<String> it = em.getValues().keySet().iterator();

				while (it.hasNext()) {
					String s = it.next();
					String fsn = em.getEnumName() + s
							+ Integer.toString(em.getValues().get(s.trim()));
					list.add(fsn);
				}
				msgFields.put(msName, list);
				continue;
			}

			List<Field> fields = m.getFields();
			for (Field f : fields) {
				ProtoField pf = (ProtoField) f;
				String fsn = f.getFieldName().trim()
						+ pf.getSequenceNumber().trim();
				list.add(fsn);
			}
			msgFields.put(msName, list);
		}
		return msgFields;

	}

	public Map<String, List<String>> pmdInfoForAllMessages(File file,
			List<String> listofMessageName, ProtoFileParser parser) {

		List<String> list1 = FileUtil.readFileAsLines(file);
		List<String> onlyPMD = new ArrayList<String>();
		for (String s : list1) {
			if (s.trim().startsWith("//PMD")) {
				onlyPMD.add(s);
			}
		}
		List<PMDInfo> list = parser.parsePMDData(onlyPMD);

		Map<String, List<String>> msgMap3 = new HashMap<String, List<String>>();
		for (String msgName : listofMessageName) {
			List<String> pmd = new ArrayList<String>();
			for (PMDInfo info : list) {
				if (msgName.contains("_e")) {
					String msgN = msgName.substring(0, msgName.length() - 2);
					if (info.getMessageName().trim()
							.equalsIgnoreCase((msgN.trim()))) {

						pmd.add(msgN + info.getFieldName().toUpperCase()
								+ info.getSequenceNumber());
						continue;
					}
				}
				if (info.getMessageName().trim()
						.equalsIgnoreCase((msgName.trim()))) {

					pmd.add(info.getFieldName() + info.getSequenceNumber());
				}

			}
			msgMap3.put(msgName, pmd);
		}

		return msgMap3;
	}

	public void assertTagAssignment() {

		int repFieldCount = 0;
		int optFieldCount = 0;

		int highSequenceNo = 0;

		for (Message m : protoMsg) {

			if (m.getClass().getName().contains("EnumMessage")) {
				continue;
			}
			for (Field f : m.getFields()) {
				ProtoField pf = (ProtoField) f;
				if (pf.getFieldRestriction().equals("repeated")
						|| pf.getFieldRestriction().equals("required")) {
					repFieldCount++;
				}
				if (pf.getFieldRestriction().equals("optional")) {
					optFieldCount++;
				}
			}

			if ((repFieldCount >= 10)) {
				highSequenceNo = repFieldCount + optFieldCount;
			}
			if ((repFieldCount < 10 && optFieldCount > 0)) {
				highSequenceNo = 10 + optFieldCount;
			}

			if ((repFieldCount < 10 && optFieldCount == 0)) {
				highSequenceNo = repFieldCount;
			}

			boolean assigned = false;

			for (int i = 1; i <= highSequenceNo; i++) {

				if (i > repFieldCount && i <= 10) {
					i = 11;

				}
				if (i >= 11) {
					for (Field f : m.getFields()) {
						ProtoField pf = (ProtoField) f;
						if (Integer.valueOf(pf.getSequenceNumber().trim()) == i) {
							assigned = true;
							if (repFieldCount < 10)
								Assert.assertTrue(
										"Tag value "
												+ i
												+ " is assigned to other than optional field in message "
												+ m.getMessageName(),
										pf.getFieldRestriction().equals(
												"optional"));
						}
					}
					Assert.assertTrue(
							"Tag value " + i + " is not assigned for message "
									+ m.getMessageName(), assigned);
					assigned = false;
					continue;
				}
				for (Field f : m.getFields()) {

					ProtoField pf = (ProtoField) f;
					if (Integer.valueOf(pf.getSequenceNumber().trim()) == i) {
						assigned = true;
						Assert.assertTrue(
								"Tag value "
										+ i
										+ " is assigned to other than repeated or required field in message "
										+ m.getMessageName(),
								pf.getFieldRestriction().equals("repeated")
										|| pf.getFieldRestriction().equals(
												"required"));
					}
				}
				Assert.assertTrue("Tag value " + i
						+ " is not assigned for message " + m.getMessageName(),
						assigned);
				assigned = false;
			}

			repFieldCount = 0;
			optFieldCount = 0;
			highSequenceNo = 0;
		}

	}

	public void compile(List<Message> msg, String eprotoPath, File bin,
			Set<String> setTypes, String wsdlNSToPkg, String msgName)
			throws Exception {

		for (Message m : msg) {
			if (compiledTypes.contains(msgName))
				continue;
			if (m.getMessageName().equals(msgName)) {
				for (Field el : m.getFields()) {
					if (el.getFieldType().equals(msgName))
						continue;
					if (compiledTypes.contains(el.getFieldType()))
						continue;
					if (el.getFieldType().contains("Enum.")) {
						String[] str = el.getFieldType().split("Enum.");
						eprotoPath = destDir.getAbsolutePath() + "/gen-src/"
								+ packagetoDirPath(wsdlNSToPkg)
								+ "/proto/extended/E" + str[0].trim() + ".java";
						compileEproto(eprotoPath, bin);
						compiledTypes.add(el.getFieldType());
					}
					if (!setTypes.contains(el.getFieldType())
							&& !el.getFieldType().contains("Enum.")) {
						eprotoPath = destDir.getAbsolutePath() + "/gen-src/"
								+ packagetoDirPath(wsdlNSToPkg)
								+ "/proto/extended/E" + el.getFieldType()
								+ ".java";
						compile(msg, eprotoPath, bin, setTypes, wsdlNSToPkg,
								el.getFieldType());
					}
				}
				eprotoPath = destDir.getAbsolutePath() + "/gen-src/"
						+ packagetoDirPath(wsdlNSToPkg) + "/proto/extended/E"
						+ m.getMessageName() + ".java";
				compileEproto(eprotoPath, bin);
				compiledTypes.add(m.getMessageName());
				return;
			}
		}

	}

	public void compileEproto(String eprotoPath, File bin) throws Exception {

		String[] inclPackagePrefixes = new String[1];
		String[] exclPackagePrefixes = new String[1];
		String[] exclClasses = new String[1];
		List<String> files = new ArrayList<String>();
		File f = new File(eprotoPath);
		files.add(f.getAbsolutePath());
		ClassLoader current = Thread.currentThread().getContextClassLoader();
		CodeGenClassLoader cgc = new CodeGenClassLoader(current,
				current.getParent(), inclPackagePrefixes, exclPackagePrefixes,
				exclClasses);
		Object abc = Class
				.forName("com.ebay.turmeric.tools.codegen.util.JavacHelper",
						true, cgc).getConstructor(OutputStream.class)
				.newInstance(System.out);
		abc.getClass().getMethod("compileJavaSource", List.class, String.class)
				.invoke(abc, files, bin.getAbsolutePath());
		// JavacHelper helper = new JavacHelper(System.out);
		// helper.compileJavaSource(files,bin.getAbsolutePath() );

	}

	public Object makeProtoObject(List<Message> msgList, Message ms,
			String wsdlNSToPkg) throws ClassNotFoundException,
			IllegalArgumentException, IllegalAccessException,
			InvocationTargetException {

		Class<?> protoClass = Thread.currentThread().getContextClassLoader()
				.loadClass(wsdlNSToPkg + ".proto." + nam);
		Object builderObj = null;

		Object builtObj = null;
		Class<?>[] innerClasses = protoClass.getDeclaredClasses();

		builderObj = createBuilder(ms, innerClasses, builderObj);
		Object descriptorObj = null;
		Class<?> bc = builderObj.getClass();
		Method[] mthd = bc.getDeclaredMethods();
		for (Field el : ms.getFields()) {

			XsdField xel = (XsdField) el;
			/*
			 * Class<?> bc = builderObj.getClass(); Method [] mthd =
			 * bc.getDeclaredMethods(); for(Method m: mthd){
			 * if(m.getName().equals("getDescriptorForType")){ Object builder =
			 * m.invoke(builderObj);
			 * 
			 * Class<?> bu = builder.getClass(); Method [] buMthd =
			 * bu.getDeclaredMethods();
			 * 
			 * for(Method m1 : buMthd){
			 * 
			 * if(m1.getName().equals("findFieldByName")){
			 * 
			 * descriptorObj = m1.invoke(builder,el.getElementName()); break; }
			 * } break; }
			 * 
			 * 
			 * }
			 */
			settingFieldValue(msgList, ms, builderObj, xel, wsdlNSToPkg);

		}
		for (Method m : mthd) {
			if (m.getName().equals("build")) {
				builtObj = m.invoke(builderObj);
				System.out.println("");
				break;

			}
		}
		return builtObj;

	}

	public void settingFieldValue(List<Message> msgList, Message ms,
			Object builderObj, XsdField el, String wsdlNSToPkg)
			throws IllegalArgumentException, IllegalAccessException,
			InvocationTargetException, ClassNotFoundException {
		String methodPrefix = "set";
		if (el.isList()) {
			methodPrefix = "add";
		}
		String dataType = xsdToProtoType.get(el.getFieldType());
		if (dataType == null)
			dataType = el.getFieldType();
		Method[] mtds = builderObj.getClass().getDeclaredMethods();
		for (Method m : mtds) {

			if (m.getName().equalsIgnoreCase(
					methodPrefix + firstLetterUpperCase(el.getFieldName()))) {

				if (el.isEnums()) {
					Class<?> protoClass = Thread.currentThread()
							.getContextClassLoader()
							.loadClass(wsdlNSToPkg + ".proto." + nam);
					Class<?> eprotoClass = Thread
							.currentThread()
							.getContextClassLoader()
							.loadClass(
									wsdlNSToPkg + ".proto.extended.E"
											+ ms.getMessageName());

					String[] enumMessage = el.getFieldType().split("Enum.");

					m.invoke(
							builderObj,
							invokeEnum(eprotoClass, protoClass, 11,
									enumMessage[0]));
					continue;
				}
				if (dataType.equals("string")) {

					if (el.isList()) {

						m.invoke(builderObj, "234");
						break;
					}
					m.invoke(builderObj, "234");
					break;
				} else if (dataType.equals("sint32")) {
					if (el.isList()) {

						m.invoke(builderObj, 123);
						break;
					}
					m.invoke(builderObj, 123);
					break;
				} else if (dataType.equals("sint64")) {
					if (el.isList()) {

						m.invoke(builderObj, 12232l);
						break;
					}
					m.invoke(builderObj, 12232l);
					break;
				} else if (dataType.equals("double")) {
					if (el.isList()) {

						m.invoke(builderObj, 323.5d);
						break;
					}
					m.invoke(builderObj, 13345d);
					break;
				} else if (dataType.equals("float")) {
					if (el.isList()) {

						m.invoke(builderObj, new Float("3.4"));
						break;
					}
					m.invoke(builderObj, new Float("2.9"));
					break;
				} else if (dataType.equals("long")) {
					if (el.isList()) {

						m.invoke(builderObj, 3232l);
						break;
					}
					m.invoke(builderObj, 12122l);
					break;
				} else if (dataType.equals("bool")) {
					if (el.isList()) {

						m.invoke(builderObj, true);
						break;
					}
					m.invoke(builderObj, true);
					break;
				} else if (dataType.equals("bytes")) {
					byte[] bytes = new byte[10];
					if (el.isList()) {

						m.invoke(builderObj, ByteString.copyFrom(bytes));
						break;
					}
					m.invoke(builderObj, ByteString.copyFrom(bytes));
					break;
				} else {
					for (Message mi : msgList) {
						if (mi.getMessageName().equals(dataType)) {
							if (el.isList()) {

								m.invoke(
										builderObj,
										makeProtoObject(msgList, mi,
												wsdlNSToPkg));
								break;
							}
							m.invoke(builderObj,
									makeProtoObject(msgList, mi, wsdlNSToPkg));
							break;
						}
					}

					break;
				}

			}
		}
	}

	public Object createBuilder(Message ms, Class<?>[] innerClasses,
			Object builderObj) throws IllegalArgumentException,
			IllegalAccessException, InvocationTargetException {

		for (Class<?> cls : innerClasses) {
			if (cls.getName().contains(ms.getMessageName() + "OrBuilder")) {
				continue;
			}
			if (cls.getName().contains(ms.getMessageName())) {

				Method[] mths = cls.getDeclaredMethods();

				for (Method m : mths) {
					System.out.println(m.getName());
					if (m.getName().equals("newBuilder")) {

						builderObj = m.invoke(null);
						break;
					}
				}

			}

		}

		return builderObj;
	}

	public Object makeObject(Message ms, List<Message> listMsg,
			String wsdlNSToPkg, Map<String, String> xsdToJaxbType)
			throws ClassNotFoundException, IllegalAccessException,
			InstantiationException, DatatypeConfigurationException {

		Class<?> jaxbClass = Thread.currentThread().getContextClassLoader()
				.loadClass(wsdlNSToPkg + "." + ms.getMessageName());
		Object obj = jaxbClass.newInstance();
		for (Field el : ms.getFields()) {
			XsdField xel = (XsdField) el;
			try {
				invokeMtd(ms, listMsg, xel, obj, wsdlNSToPkg, xsdToJaxbType);

			} catch (IllegalArgumentException e) {
				Assert.assertTrue(
						e.getMessage() + "caused by :" + e.getCause(), false);
			} catch (InvocationTargetException e) {
				Assert.assertTrue(
						e.getMessage() + "caused by :" + e.getCause(), false);
			}

		}

		return obj;
	}

	public String firstLetterUpperCase(String word) {
		char[] ch = word.toCharArray();
		String str = Character.toString(ch[0]).toUpperCase()
				+ word.substring(1);
		return str;
	}

	public void invokeMtd(Message ms, List<Message> listMsg, XsdField el,
			Object jaxbObj, String wsdlNSToPkg,
			Map<String, String> xsdToJaxbType) throws IllegalArgumentException,
			IllegalAccessException, InvocationTargetException,
			ClassNotFoundException, InstantiationException,
			DatatypeConfigurationException {
		String methodPrefix = "set";
		Method[] mths = jaxbObj.getClass().getMethods();
		String dataType = xsdToJaxbType.get(el.getFieldType());
		if (dataType == null)
			dataType = el.getFieldType();

		for (Method m : mths) {
			if (el.isList()) {
				methodPrefix = "get";
				if ((methodPrefix + firstLetterUpperCase(el.getJaxbName()))
						.equals(m.getName())) {

					Object obj = m.invoke(jaxbObj);
					for (Method mthd : obj.getClass().getDeclaredMethods()) {
						if (mthd.getName().equals("add")
								&& mthd.getParameterTypes().length == 1) {
							if (dataType.equals("string")) {

								mthd.invoke(obj, "4567");
								break;
							} else if (dataType.equals("integer")) {

								mthd.invoke(obj, 100);
								break;
							} else if (dataType.equals("float")) {

								mthd.invoke(obj, 10.9f);
								break;
							} else if (dataType.equals("long")) {

								mthd.invoke(obj, 8971l);
								break;
							} else if (dataType.equals("boolean")) {

								mthd.invoke(obj, true);
								break;
							} else if (dataType.equals("double")) {

								mthd.invoke(obj, new Double("100000"));
								break;
							} else if (dataType.equals("duration")) {

								mthd.invoke(obj, DatatypeFactory.newInstance()
										.newDuration(1000));
								break;
							} else if (dataType.equals("xmlgregoriancalendar")) {
								GregorianCalendar greCal = new GregorianCalendar();
								greCal.setTimeInMillis(10000);

								mthd.invoke(obj, DatatypeFactory.newInstance()
										.newXMLGregorianCalendar(greCal));
								break;
							} else if (dataType.equals("biginteger")) {

								mthd.invoke(obj, new BigInteger("34"));
								break;
							} else if (dataType.equals("bigdecimal")) {

								mthd.invoke(obj, new BigDecimal("232.3"));
								break;
							} else if (dataType.equals("byte")) {

								mthd.invoke(obj, new Byte("23"));
								break;
							} else if (dataType.equals("QName")) {

								mthd.invoke(obj, new QName("das"));
								break;
							} else if (dataType.equals("ENTITY")) {

								mthd.invoke(obj, "das");
								break;
							} else if (dataType.equals("Name")) {

								mthd.invoke(obj, "das");
								break;
							} else if (dataType.equals("NCName")) {

								mthd.invoke(obj, "das");
								break;
							} else if (dataType.equals("negativeInteger")) {

								mthd.invoke(obj, new BigInteger("34"));
								break;
							} else if (dataType.equals("nonNegativeInteger")) {

								mthd.invoke(obj, new BigInteger("34"));
								break;
							} else if (dataType.equals("nonPositiveInteger")) {

								mthd.invoke(obj, new BigInteger("34"));
								break;
							} else if (dataType.equals("normalizedString")) {

								mthd.invoke(obj, "das");
								break;
							} else if (dataType.equals("positiveInteger")) {

								mthd.invoke(obj, new BigInteger("34"));
								break;
							} else if (dataType.equals("ENTITIES")) {
								List<String> list = new ArrayList<String>();
								mthd.invoke(obj, list);
								break;
							} else if (dataType.equals("short")) {

								mthd.invoke(obj, new Short("23"));
								break;

							} else if (dataType.equals("byte[]")) {

								List<byte[]> list = new ArrayList<byte[]>();

								mthd.invoke(obj, list);
								break;
							} else {
								for (Message info : listMsg) {
									if (info.getMessageName().equals(dataType)) {

										mthd.invoke(
												obj,
												makeObject(info, listMsg,
														wsdlNSToPkg,
														xsdToJaxbType));
									}

								}
								break;

							}
						}
					}
					continue;
				}
			}

			if ((methodPrefix + firstLetterUpperCase(el.getJaxbName()))
					.equalsIgnoreCase(m.getName())) {

				if (dataType.contains("Enum.")) {

					Class<?> jaxbClass = Thread
							.currentThread()
							.getContextClassLoader()
							.loadClass(
									wsdlNSToPkg
											+ "."
											+ getEnumClassNamFromDataType(el
													.getFieldType()));
					Method mtd = null;
					try {
						mtd = jaxbClass.getMethod("fromValue", String.class);
					} catch (SecurityException e) {
					} catch (NoSuchMethodException e) {
					}
					for (Message msg : listMsg) {

						if (msg.getMessageName().equals(
								getEnumClassNamFromDataType(el.getFieldType())
										+ "Enum")) {
							EnumMessage ems = (EnumMessage) msg;
							Iterator<String> it = ems.getValues().keySet()
									.iterator();

							Object obj = mtd.invoke(null, it.next().trim()
									.toLowerCase());
							m.invoke(jaxbObj, obj);
							break;
						}
					}

				}

				if (dataType.equals("string")) {

					m.invoke(jaxbObj, "3456");
				} else if (dataType.equals("integer")) {
					m.invoke(jaxbObj, 100);
				} else if (dataType.equals("float")) {
					m.invoke(jaxbObj, 100.00f);
				} else if (dataType.equals("long")) {
					m.invoke(jaxbObj, 10000l);
				} else if (dataType.equals("boolean")) {
					m.invoke(jaxbObj, true);
				} else if (dataType.equals("duration")) {
					m.invoke(jaxbObj, DatatypeFactory.newInstance()
							.newDuration(1000));
				} else if (dataType.equals("QName")) {
					m.invoke(jaxbObj, new QName("sadsa"));
				} else if (dataType.equals("double")) {
					m.invoke(jaxbObj, 1000d);
				} else if (dataType.equals("xmlgregoriancalendar")) {
					GregorianCalendar greCal = new GregorianCalendar();
					greCal.setTimeInMillis(10000);

					m.invoke(jaxbObj, DatatypeFactory.newInstance()
							.newXMLGregorianCalendar(greCal));
				} else if (dataType.equals("biginteger")) {
					m.invoke(jaxbObj, new BigInteger("23"));
				} else if (dataType.equals("bigdecimal")) {
					m.invoke(jaxbObj, new BigDecimal("23.8"));
				} else if (dataType.equals("byte")) {
					m.invoke(jaxbObj, new Byte("3"));
				} else if (dataType.equals("short")) {
					m.invoke(jaxbObj, new Short("39"));
				} else if (dataType.equals("byte[]")) {
					byte[] bytes = new byte[10];
					m.invoke(jaxbObj, bytes);
				} else if (dataType.equals("ENTITY")) {

					m.invoke(jaxbObj, "das");
					break;
				} else if (dataType.equals("Name")) {

					m.invoke(jaxbObj, "das");
					break;
				} else if (dataType.equals("NCName")) {

					m.invoke(jaxbObj, "das");
					break;
				} else if (dataType.equals("negativeInteger")) {

					m.invoke(jaxbObj, new BigInteger("34"));
					break;
				} else if (dataType.equals("nonNegativeInteger")) {

					m.invoke(jaxbObj, new BigInteger("34"));
					break;
				} else if (dataType.equals("nonPositiveInteger")) {

					m.invoke(jaxbObj, new BigInteger("34"));
					break;
				} else if (dataType.equals("normalizedString")) {

					m.invoke(jaxbObj, "das");
					break;
				} else if (dataType.equals("positiveInteger")) {

					m.invoke(jaxbObj, new BigInteger("34"));
					break;
				} else if (dataType.equals("ENTITIES")) {
					List<String> list = new ArrayList<String>();
					m.invoke(jaxbObj, list);
					break;
				} else {
					for (Message info : listMsg) {
						if (info.getMessageName().equals(dataType)) {
							m.invoke(
									jaxbObj,
									makeObject(info, listMsg, wsdlNSToPkg,
											xsdToJaxbType));
						}

					}

				}
			}
		}

	}

	private String getEnumClassNamFromDataType(String dataType) {

		return dataType.split("Enum.")[1];

	}

	public void compile(List<URI> sources) {
		List<SimpleJavaFileObject> jfoList = new ArrayList<SimpleJavaFileObject>();
		for (URI uri : sources) {
			SimpleJavaFileObject jfo = new ExtSimpleFileObject(uri,
					JavaFileObject.Kind.SOURCE);
			jfoList.add(jfo);
		}
		List<String> optionList = new ArrayList<String>();
		// set compiler's classpath to be same as the runtime's
		optionList.addAll(Arrays.asList("-classpath",
				System.getProperty("java.class.path")));

		JavaCompiler compiler = ToolProvider.getSystemJavaCompiler();
		PrintWriter out = new PrintWriter(System.out);
		JavaCompiler.CompilationTask task = compiler.getTask(out, null, null,
				optionList, null, jfoList);
		task.call();

	}

	class ExtSimpleFileObject extends SimpleJavaFileObject {

		public ExtSimpleFileObject(URI uri, JavaFileObject.Kind knd) {
			super(uri, knd);

		}
	}

	public class TestResponseHandler implements UserResponseHandler {

		@Override
		public boolean getBooleanResponse(String promptMsg) {
			// TODO Auto-generated method stub
			return false;
		}

	}

	public void generateJaxbClasses(String path, String destDir, File binDir,
			String serviceName) throws Exception {

		String[] testArgs = { "-serviceName", serviceName, "-genType",
				"ServiceFromWSDLIntf", "-wsdl", path, "-mdest",
				destDir + "/meta-src", "-gip",
				"com.ebay.test.soaframework.tools.codegen", "-dest", destDir,
				"-src", destDir, "-bin", binDir.getAbsolutePath(), "-slayer",
				"INTERMEDIATE", "-nonXSDFormats", "protobuf",
				"-enabledNamespaceFolding", "-scv", "1.0.0", "-pr", destDir };
		ServiceGenerator sgen = new ServiceGenerator();
		sgen.startCodeGen(testArgs);

	}

	public List<String> getTypeInformation(File fieldInfo) {
		List<String> info = new ArrayList<String>();
		BufferedReader reader = null;
		try {
			FileReader in = new FileReader(fieldInfo);
			reader = new BufferedReader(in);
			String line = reader.readLine();
			while (line != null) {
				info.add(line);
				line = reader.readLine();
			}

		} catch (FileNotFoundException e) {
		} catch (IOException e) {
		} finally {
			try {
				reader.close();
			} catch (IOException e) {
			}
		}
		return info;

	}

	public String invokeEnumNewInstance(Class<?> jaxbClass,
			Class<?> eprotoClass, String value) {
		Object obj = null;
		try {
			Method mtd = jaxbClass.getMethod("fromValue", String.class);

			Method method = eprotoClass.getMethod("newInstance", jaxbClass);
			obj = method.invoke(null,
					mtd.invoke(null, value.trim().toLowerCase()));

		} catch (SecurityException e) {
		} catch (NoSuchMethodException e) {
		} catch (IllegalArgumentException e) {
		} catch (IllegalAccessException e) {
		} catch (InvocationTargetException e) {
		}

		return obj.getClass().getName();

	}

	public String invokeNewInstance(Class<?> jaxbClass, Class<?> eprotoClass,
			Class<?> protoClass, String typeName, Object jaxbObject) {

		Class<?> classForType = null;
		Object obj = null;
		try {
			Method method = eprotoClass.getMethod("newInstance", jaxbClass);
			Class<?> innerClasses[] = protoClass.getDeclaredClasses();
			Constructor<?>[] constructor = eprotoClass
					.getDeclaredConstructors();
			for (Class<?> cl : innerClasses) {
				if (!cl.getName().contains("OrBuilder"))
					if (cl.getName().contains(typeName)) {
						classForType = cl;
					}

			}

			Constructor<?>[] constr = eprotoClass.getDeclaredConstructors();
			Constructor<?> builderCon = null;

			// FitmentFieldValue$Builder
			for (Constructor<?> ct : constr) {
				if (ct.getParameterTypes().length == 1) {

					builderCon = ct;
					ct.setAccessible(true);
				}

			}

			/*
			 * Method m = classForType.getMethod("newBuilder"); Object ob =
			 * m.invoke(null);
			 * 
			 * Class<?> builders = ob.getClass();
			 * 
			 * 
			 * Method buildMethod = builders.getMethod("build"); Constructor<?>
			 * [] builder = builders.getDeclaredConstructors(); Constructor<?>
			 * cons = null; for(Constructor<?> ct : builder){
			 * 
			 * if(ct.getParameterTypes().length == 0){ cons = ct;
			 * ct.setAccessible(true); }
			 * 
			 * }
			 */
			Object builtObj = null; // buildMethod.invoke(cons.newInstance());
			Method[] methods = classForType.getDeclaredMethods();
			for (Method mt : methods) {
				if (mt.getName().equals("getDefaultInstance")) {
					builtObj = mt.invoke(null);
					break;
				}
			}

			Object eprotoObj = builderCon.newInstance(builtObj);

			obj = method.invoke(eprotoObj, jaxbObject);

		} catch (SecurityException e) {
		} catch (NoSuchMethodException e) {
		} catch (IllegalArgumentException e) {
		} catch (IllegalAccessException e) {
		} catch (InvocationTargetException e) {
		} catch (InstantiationException e) {
		}
		return obj.getClass().getName();

	}

	public String invokeEnumGetter(Class<?> eprotoClass, Class<?> protoClass,
			int index, String typeName) {
		Class<?> classForType = null;
		Class<?> classForEnum = null;
		Class<?> classEnum = null;

		Object obj = null;
		try {

			Class<?> innerClasses[] = protoClass.getDeclaredClasses();

			for (Class<?> cl : innerClasses) {
				if (!cl.getName().contains("OrBuilder"))
					if (cl.getName().contains(typeName)) {
						classForType = cl;
						break;
					}

			}

			for (Class<?> enumcls : classForType.getDeclaredClasses()) {
				if (!enumcls.getName().contains("Builder"))
					if (enumcls.getName().contains(typeName)) {
						classEnum = enumcls;
						break;
					}
			}

			Object builder = null;

			for (Method m : classForType.getDeclaredMethods()) {

				if (m.getName().equals("newBuilder")) {
					builder = m.invoke(null);
					break;
				}
			}

			Object protoenum = null;
			Class<?> buildercls = builder.getClass();

			for (Method build : buildercls.getDeclaredMethods()) {

				if (build.getName().equals("build")) {
					protoenum = build.invoke(builder);
					break;
				}
			}

			Object enumObj = null;

			Method val = classEnum.getDeclaredMethod("valueOf", Integer.TYPE);

			enumObj = val.invoke(null, new Integer(11));

			Method method = eprotoClass.getDeclaredMethod("getEnum", classEnum);

			obj = method.invoke(null, enumObj);

		} catch (SecurityException e) {
		} catch (NoSuchMethodException e) {
		} catch (IllegalArgumentException e) {
		} catch (IllegalAccessException e) {
		} catch (InvocationTargetException e) {
		}
		return obj.getClass().getName();
	}

	public Object invokeEnum(Class<?> eprotoClass, Class<?> protoClass,
			int index, String typeName) {
		Class<?> classForType = null;
		Class<?> classForEnum = null;
		Class<?> classEnum = null;

		Object obj = null;
		try {

			Class<?> innerClasses[] = protoClass.getDeclaredClasses();

			for (Class<?> cl : innerClasses) {
				if (!cl.getName().contains("OrBuilder"))
					if (cl.getName().contains(typeName)) {
						classForType = cl;
					}

			}

			Class<?>[] inner = classForType.getDeclaredClasses();

			for (Class<?> cls : innerClasses) {

				if (!cls.getName().contains("builder"))
					if (cls.getName().contains(typeName)) {
						classForEnum = cls;
						break;
					}

			}
			if (typeName.contains("Enum"))
				typeName = typeName.substring(0, typeName.length() - 4);
			for (Class<?> c : classForEnum.getDeclaredClasses()) {
				if (!c.getName().contains("Builder"))
					if (c.getName().contains(typeName)) {
						classEnum = c;
						break;
					}
			}

			Method mt = classEnum.getDeclaredMethod("valueOf", Integer.TYPE);
			obj = mt.invoke(null, index);

		} catch (SecurityException e) {
		} catch (NoSuchMethodException e) {
		} catch (IllegalArgumentException e) {
		} catch (IllegalAccessException e) {
		} catch (InvocationTargetException e) {
		}
		return obj;
	}

	public String invokeGetters(Class<?> eprotoClass, Class<?> protoClass,
			String methodName, String typeName, Object builder) {

		Class<?> classForType = null;
		Object obj = null;
		try {
			Method method = eprotoClass.getMethod(methodName);
			Class<?> innerClasses[] = protoClass.getDeclaredClasses();
			Constructor<?>[] constructor = eprotoClass
					.getDeclaredConstructors();
			for (Class<?> cl : innerClasses) {
				if (!cl.getName().contains(typeName + "OrBuilder"))
					if (cl.getName().contains(typeName)) {
						classForType = cl;
					}

			}

			Constructor<?>[] constr = eprotoClass.getDeclaredConstructors();
			Constructor<?> builderCon = null;

			// FitmentFieldValue$Builder
			for (Constructor<?> ct : constr) {
				if (ct.getParameterTypes().length == 1) {

					builderCon = ct;
					ct.setAccessible(true);
				}

			}

			Object eprotoObj = builderCon.newInstance(builder);

			obj = method.invoke(eprotoObj);

		} catch (SecurityException e) {
		} catch (NoSuchMethodException e) {
		} catch (IllegalArgumentException e) {
		} catch (IllegalAccessException e) {
		} catch (InvocationTargetException e) {
		} catch (InstantiationException e) {
		}
		return obj.getClass().getName();

	}

	public List<String> assertReturnTypes(Class<?> eprotoClass) {
		List<String> returntype = new ArrayList<String>();

		Method[] methods = eprotoClass.getDeclaredMethods();
		for (Method m : methods) {
			returntype.add(m.getReturnType().getName());
		}

		return returntype;

	}

	public void compile(String relativePath) {

		JavaCompiler compiler = ToolProvider.getSystemJavaCompiler();
		int result = compiler.run(null, null, null, relativePath);

		System.out.println("Compile result code = " + result);
	}

	public String packagetoDirPath(String pkg) {

		return pkg.replace(".", "/");

	}

	public void compileJProto(String src_dir, String dst_dir, String protoloc) {
		String s = null;
		try {

			// run the Unix "ps -ef" command
			// using the Runtime exec method:
			Process p = Runtime.getRuntime().exec(
					"protoc.exe -I=" + src_dir + " --java_out=" + dst_dir + " "
							+ protoloc);

			BufferedReader stdInput = new BufferedReader(new InputStreamReader(
					p.getInputStream()));

			BufferedReader stdError = new BufferedReader(new InputStreamReader(
					p.getErrorStream()));

			// read the output from the command
			System.out.println("Here is the standard output of the command:\n");
			while ((s = stdInput.readLine()) != null) {
				System.out.println(s);
			}

			while ((s = stdError.readLine()) != null) {
				System.out.println(s);
			}
		} catch (IOException e) {
			System.out.println("exception happened - here's what I know: ");
			e.printStackTrace();
			System.exit(-1);
		}

	}

}
