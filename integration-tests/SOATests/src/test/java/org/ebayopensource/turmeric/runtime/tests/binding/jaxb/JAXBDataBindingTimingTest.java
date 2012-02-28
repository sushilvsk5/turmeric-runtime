package org.ebayopensource.turmeric.runtime.tests.binding.jaxb;

import java.io.ByteArrayOutputStream;

import org.ebayopensource.turmeric.runtime.common.binding.DataBindingDesc;
import org.ebayopensource.turmeric.runtime.common.binding.Deserializer;
import org.ebayopensource.turmeric.runtime.common.binding.DeserializerFactory;
import org.ebayopensource.turmeric.runtime.common.binding.SerializerFactory;
import org.ebayopensource.turmeric.runtime.common.impl.binding.jaxb.fastinfoset.JAXBFastInfosetDeserializerFactory;
import org.ebayopensource.turmeric.runtime.common.impl.binding.jaxb.fastinfoset.JAXBFastInfosetSerializerFactory;
import org.ebayopensource.turmeric.runtime.common.impl.binding.jaxb.json.JAXBJSONDeserializerFactory;
import org.ebayopensource.turmeric.runtime.common.impl.binding.jaxb.json.JAXBJSONSerializerFactory;
import org.ebayopensource.turmeric.runtime.common.impl.binding.jaxb.nv.JAXBNVDeserializerFactory;
import org.ebayopensource.turmeric.runtime.common.impl.binding.jaxb.nv.JAXBNVSerializerFactory;
import org.ebayopensource.turmeric.runtime.common.impl.binding.jaxb.xml.JAXBXMLDeserializerFactory;
import org.ebayopensource.turmeric.runtime.common.impl.binding.jaxb.xml.JAXBXMLSerializerFactory;
import org.ebayopensource.turmeric.runtime.common.pipeline.InboundMessage;
import org.ebayopensource.turmeric.runtime.common.pipeline.Message;
import org.ebayopensource.turmeric.runtime.common.pipeline.MessageContext;
import org.ebayopensource.turmeric.runtime.common.types.SOAHeaders;
import org.ebayopensource.turmeric.runtime.spf.impl.internal.config.ServiceConfigManager;
import org.ebayopensource.turmeric.runtime.tests.common.util.TestUtils;
import org.ebayopensource.turmeric.runtime.tests.service1.sample.types1.MyMessage;

import com.cts.turmeric.runtime.common.impl.binding.jaxb.jackson.JAXBSMILEDeserializerFactory;
import com.cts.turmeric.runtime.common.impl.binding.jaxb.jackson.JAXBSMILESerializerFactory;

public class JAXBDataBindingTimingTest extends BaseSerDeserTest {

	private static final SerializerFactory fiSerFactory = new JAXBFastInfosetSerializerFactory();
	private static final DeserializerFactory fiDeserFactory = new JAXBFastInfosetDeserializerFactory();

	private static final SerializerFactory xmlSerFactory = new JAXBXMLSerializerFactory();
	private static final DeserializerFactory xmlDeserFactory = new JAXBXMLDeserializerFactory();

	private static final SerializerFactory jsonStreamSerFactory = new JAXBJSONSerializerFactory();
	private static final DeserializerFactory jsonStreamDeserFactory = new JAXBJSONDeserializerFactory();

	private static final SerializerFactory nvSerFactory = new JAXBNVSerializerFactory();
	private static final DeserializerFactory nvDeserFactory = new JAXBNVDeserializerFactory();
	
	private static final SerializerFactory jacksonJsonSerFactory = new com.cts.turmeric.runtime.common.impl.binding.jaxb.jackson.JAXBJSONSerializerFactory();
	private static final DeserializerFactory jacksonJsonDeserFactory = new com.cts.turmeric.runtime.common.impl.binding.jaxb.jackson.JAXBJSONDeserializerFactory();
	
	private static final SerializerFactory jacksonSmileSerFactory = new JAXBSMILESerializerFactory();
	private static final DeserializerFactory jacksonSmileDeserFactory = new JAXBSMILEDeserializerFactory();
	
	private static final SerializerFactory aaltoXmlSerFactory = new com.cts.turmeric.runtime.common.impl.binding.jaxb.aalto.JAXBXMLSerializerFactory();
	private static final DeserializerFactory aaltoXmlDeserFactory = new com.cts.turmeric.runtime.common.impl.binding.jaxb.aalto.JAXBXMLDeserializerFactory();

	public JAXBDataBindingTimingTest(String testName) {		
		super();
		try{
		super.setUp();
		ServiceConfigManager.getInstance().setConfigTestCase("cts", true);
		}catch(Exception e){
			e.printStackTrace();
		}
		//UnitTestContext.getInstance().addModules(new ModuleInterface[] {com.ebay.kernel.Module.getInstance()});
	}

	public void testJAXBBindingTiming() throws Exception {
		System.out.println("**** Starting testJAXBBindingTiming");
		MyMessage msg = TestUtils.createTestMessage(3);

		// Warn up
		doTimingTest(msg, false, fiDeserFactory, fiSerFactory, 1, null);
		doTimingTest(msg, false, xmlDeserFactory, xmlSerFactory, 1, null);
		doTimingTest(msg, false, jsonStreamDeserFactory, jsonStreamSerFactory, 1, null);
		doTimingTest(msg, false, nvDeserFactory, nvSerFactory, 1, null);
		doTimingTest(msg, true,  nvDeserFactory, nvSerFactory, 1, null);
		doTimingTest(msg, false,  jacksonJsonDeserFactory, jacksonJsonSerFactory, 1, null);
		doTimingTest(msg, false,  jacksonSmileDeserFactory, jacksonSmileSerFactory, 1, null);
		doTimingTest(msg, false,  aaltoXmlDeserFactory, aaltoXmlSerFactory, 1, null);

		// Time it.
		doTimingTest(msg, false, fiDeserFactory, fiSerFactory, 100, "Fast Infoset");
		doTimingTest(msg, false, xmlDeserFactory, xmlSerFactory, 100, "Wstx XML");
		doTimingTest(msg, false, jsonStreamDeserFactory, jsonStreamSerFactory, 100, "Streaming JSON");
		doTimingTest(msg, false, nvDeserFactory, nvSerFactory, 100, "NV");
		doTimingTest(msg, true,  nvDeserFactory, nvSerFactory, 100, "Ordered NV");
		doTimingTest(msg, false,  jacksonJsonDeserFactory, jacksonJsonSerFactory, 100, "Jackson JSON");
		doTimingTest(msg, false,  jacksonSmileDeserFactory, jacksonSmileSerFactory, 100, "Jackson Binary JSON");
		doTimingTest(msg, false,  aaltoXmlDeserFactory, aaltoXmlSerFactory, 100, "Aalto XML");
		
		System.out.println("**** Ending testJAXBBindingTiming");
	}
	
	public void testTimingWithDifferentPayloadSize() throws Throwable {
		/*jaxbBindingTimingSubStructRepeatnTimes(3);
		jaxbBindingTimingSubStructRepeatnTimes(50);
		jaxbBindingTimingSubStructRepeatnTimes(100);
		jaxbBindingTimingSubStructRepeatnTimes(150);*/
		jaxbBindingTimingSubStructRepeatnTimes(1000);
	}

	public void jaxbBindingTimingSubStructRepeatnTimes(int n) throws Throwable {
		//System.setProperty("com.ebay.jni.JniDirectory", "D:/views/ichernyshev_soa/BuildOutput/modules50/jni");
		//com.ebay.jni.memtrace.NativeMemTrace.initialize();
		//com.ebay.jni.memtrace.NativeMemTrace.setThreadExecSamplingInterval(50);

		System.out.println("**** Starting jaxbBindingTimingSubStructRepeatnTimes n=" + n);
		MyMessage msg = TestUtils.createTestMessage(n);

		int warmupLength = 5;
		for (int i=0; i<warmupLength; i++) {
			long memUsage = Runtime.getRuntime().totalMemory() - Runtime.getRuntime().freeMemory();
			memUsage /= 1024;
			System.out.println("Warming up: cycle " + (i+1) + " of " + warmupLength +
				". Mem usage = " + memUsage + "KB");
			for (int j=0; j<200; j++) {
				doTimingTest(msg, false, fiDeserFactory, fiSerFactory, 1, null);
				doTimingTest(msg, false, xmlDeserFactory, xmlSerFactory, 1, null);
				doTimingTest(msg, false, jsonStreamDeserFactory, jsonStreamSerFactory, 1, null);
				doTimingTest(msg, false, nvDeserFactory, nvSerFactory, 1, null);
				doTimingTest(msg, true,  nvDeserFactory, nvSerFactory, 1, null);
				doTimingTest(msg, false,  jacksonJsonDeserFactory, jacksonJsonSerFactory, 1, null);
				doTimingTest(msg, false,  jacksonSmileDeserFactory, jacksonSmileSerFactory, 1, null);
				doTimingTest(msg, false,  aaltoXmlDeserFactory, aaltoXmlSerFactory, 1, null);
			}
		}

		runGc();

		long memUsage = Runtime.getRuntime().totalMemory() - Runtime.getRuntime().freeMemory();
		memUsage /= 1024;
		System.out.println("Measuring time. Mem usage = " + memUsage + "KB");

		doTimingTest(msg, false, fiDeserFactory, fiSerFactory, 1000, "Fast Infoset");
		doTimingTest(msg, false, xmlDeserFactory, xmlSerFactory, 1000, "Wstx XML");
		doTimingTest(msg, false, jsonStreamDeserFactory, jsonStreamSerFactory, 1000, "Streaming JSON");
		doTimingTest(msg, false, nvDeserFactory, nvSerFactory, 1000, "NV");
		doTimingTest(msg, true,  nvDeserFactory, nvSerFactory, 1000, "Ordered NV");
		doTimingTest(msg, false, nvDeserFactory, nvSerFactory, 1000, "NV IR", true);
		doTimingTest(msg, true,  nvDeserFactory, nvSerFactory, 1000, "Ordered NV IR", true);
		doTimingTest(msg, false, jacksonJsonDeserFactory, jacksonJsonSerFactory, 1000, "Jackson JSON");
		doTimingTest(msg, false, jacksonSmileDeserFactory, jacksonSmileSerFactory, 1000, "Jackson Binary JSON");
		doTimingTest(msg, false, aaltoXmlDeserFactory, aaltoXmlSerFactory, 1000, "Aalto XML");

/*		com.ebay.util.MemTraceTestUtils.enableCpuThreadSampling();
		System.out.println("Sampling CPU");
		//doTimingTest(msg, true,  xmlDeserFactory, xmlSerFactory, 3500, "Wstx XML", true);
		doTimingTest(msg, true,  nvDeserFactory, nvSerFactory, 3500, "Ordered NV IR", true);
		com.ebay.util.MemTraceTestUtils.disableThreadSamplingAndPrintResults();*/

		System.out.println("**** Ending jaxbBindingTimingSubStructRepeatnTimes");
	}

	private void runGc() throws Exception{
		System.gc();
		System.gc();
		Thread.sleep(3000);
		System.gc();
	}

	private void doTimingTest(Object msg, boolean ordered,
		DeserializerFactory deserF, SerializerFactory serF,
		int times, String title)
		throws Exception
	{
		doTimingTest(msg, ordered, deserF, serF, times, title, false);
	}

	private void doTimingTest(Object msg, boolean ordered,
		DeserializerFactory deserF, SerializerFactory serF,
		int times, String title, boolean useImpliedRoot)
		throws Exception
	{
		this.m_deserFactory = deserF;
		this.m_serFactory = serF;
		super.setUp();
		DataBindingDesc xmlDbDesc = new DataBindingDesc(deserF.getPayloadType(), TestUtils.getMimeType(deserF.getPayloadType()), serF, deserF, null, null, null, null);

		if (title != null) {
			runGc();
		}
		
		JAXBTestBuilder jaxbtest = new JAXBTestBuilder();
		jaxbtest.setTestServer(jetty);
		jaxbtest.setOrdered(false);
		jaxbtest.setSymmetricDBDesc(xmlDbDesc);
		jaxbtest.setSerializerFactory(m_serFactory);
		jaxbtest.setDeserializerFactory(m_deserFactory);

		long serStart = System.nanoTime();
		ByteArrayOutputStream out = new ByteArrayOutputStream();
		for (int i=0; i< times; i++) {
			
			MessageContext ctx = jaxbtest.createTestMessageContext();
			ctx.getRequestMessage();
			out.reset();
			JAXBTestHelper.serialize(ctx, out, msg);
		}
		double serTime = System.nanoTime() - serStart;

		byte[] data = out.toByteArray();
		jaxbtest.setPayload(data);
		//Object resultMsg = null;

		if (title != null) {
			runGc();
		}

		long deserStart = System.nanoTime();
		for (int i=0; i< times; i++) {
			MessageContext ctx = jaxbtest.createTestMessageContext();
			Message inMsg = ctx.getRequestMessage();
			inMsg.setTransportHeader(SOAHeaders.NV_IMPLIED_ROOT, String.valueOf(useImpliedRoot));

			Deserializer deser = deserF.getDeserializer();
			deser.deserialize((InboundMessage)ctx.getRequestMessage(), MyMessage.class);
		}
		double deserTime = System.nanoTime() - deserStart;
		double totalTime = System.nanoTime() - serStart;

//		assertEquals(msg, resultMsg);

		//if (loggingOn) {
		//	String xml1 = out.toString();
		//	System.out.println(xml1);
		//}

		if (title != null) {
			serTime /= (double)times;
			deserTime /= (double)times;
			totalTime /= (double)times;
			serTime /= 1000000.0;
			deserTime /= 1000000.0;
			totalTime /= 1000000.0;

			System.out.format("%1$20s -- \tser: %2$10f  \tdeser: %3$10f \ttotal: %4$10f \tmsg size: %5$6d\n",
				title, new Double(serTime), new Double(deserTime), new Double(totalTime), new Integer(data.length));
		}
	}

	public static void main (String[] argv) throws Throwable {
		JAXBDataBindingTimingTest testObject = new JAXBDataBindingTimingTest("LnP test");
		testObject.startServer();
		testObject.testTimingWithDifferentPayloadSize();
		System.out.println("Done");
		testObject.stopServer();
		try {
			Thread.sleep(1000000000);
		} catch (Exception e) {}
	}
}
