package org.ebayopensource.turmeric.qajunittests.advertisinguniqueidservicev1.sif;

import java.net.MalformedURLException;
import java.util.HashMap;
import java.util.Map;

import org.ebayopensource.turmeric.advertising.v1.services.EchoMessageRequest;
import org.ebayopensource.turmeric.advertisinguniqueidservicev1.gen.SharedAdvertisingUniqueIDServiceV1Consumer;
import org.ebayopensource.turmeric.runtime.common.exceptions.ServiceException;
import org.ebayopensource.turmeric.runtime.common.types.SOAConstants;
import org.ebayopensource.turmeric.runtime.tests.common.jetty.AbstractWithServerTest;
import org.ebayopensource.turmeric.runtime.tests.common.util.HttpTestClient;
import org.ebayopensource.turmeric.runtime.tests.common.util.MetricUtil;
import org.junit.Ignore;
import org.junit.Test;
import static org.junit.Assert.*;

public class MultiClientConfigTests extends AbstractWithServerTest {

	@Test
	public void testFeatureEnvt() throws ServiceException {
		SharedAdvertisingUniqueIDServiceV1Consumer testClient1 = new SharedAdvertisingUniqueIDServiceV1Consumer(
				"AdvertisingUniqueIDServiceV1Consumer", "feature");
		try {
			testClient1.setHostName(serverUri.getHost() + ":"
					+ serverUri.getPort());
			logger.debug(testClient1.getHostName());
		} catch (MalformedURLException e) {

		}
		EchoMessageRequest param0 = new EchoMessageRequest();
		param0.setIn("MCC Test");
		assertEquals(testClient1.echoMessage(param0).getOut(),
				" Echo Message = MCC Test");
		assertEquals(testClient1.getService().getResponseContext()
				.getTransportHeader("X-TURMERIC-RESPONSE-DATA-FORMAT"), "XML");
	}

	@Test
	public void testProductionEnvt() throws ServiceException {
		SharedAdvertisingUniqueIDServiceV1Consumer testClient1 = new SharedAdvertisingUniqueIDServiceV1Consumer(
				"AdvertisingUniqueIDServiceV1Consumer", "production");
		EchoMessageRequest param0 = new EchoMessageRequest();
		param0.setIn("MCC Test");
		testClient1.getServiceInvokerOptions().setTransportName(
				SOAConstants.TRANSPORT_LOCAL);
		assertEquals(testClient1.echoMessage(param0).getOut(),
				" Echo Message = MCC Test");
		assertEquals(testClient1.getService().getResponseContext()
				.getTransportHeader("X-TURMERIC-RESPONSE-DATA-FORMAT"), "NV");
	}

	/*
	 * Default envt picked when no envtName is given is Production
	 */
	@Test
	public void testDefaultEnvt() throws ServiceException {
		SharedAdvertisingUniqueIDServiceV1Consumer testClient1 = new SharedAdvertisingUniqueIDServiceV1Consumer(
				"AdvertisingUniqueIDServiceV1Consumer");
		EchoMessageRequest param0 = new EchoMessageRequest();
		param0.setIn("MCC Test");
		testClient1.getServiceInvokerOptions().setTransportName(
				SOAConstants.TRANSPORT_LOCAL);
		assertEquals(testClient1.echoMessage(param0).getOut(),
				" Echo Message = MCC Test");
		assertEquals(testClient1.getService().getResponseContext()
				.getTransportHeader("X-TURMERIC-RESPONSE-DATA-FORMAT"), "NV");
	}

	/*
	 * Custom Environment { myTestEnvt Setup }
	 */
	@Test
	public void testCustomEnvt() throws ServiceException {
		SharedAdvertisingUniqueIDServiceV1Consumer testClient3 = new SharedAdvertisingUniqueIDServiceV1Consumer(
				"AdvertisingUniqueIDServiceV1Consumer", "MixedMode2");
		EchoMessageRequest param0 = new EchoMessageRequest();
		param0.setIn("MCC Test");
		testClient3.getServiceInvokerOptions().setTransportName(
				SOAConstants.TRANSPORT_LOCAL);
		assertEquals(testClient3.echoMessage(param0).getOut(),
				" Echo Message = MCC Test");
		assertEquals(testClient3.getService().getResponseContext()
				.getTransportHeader("X-TURMERIC-RESPONSE-DATA-FORMAT"), "XML");
	}

	@Test
	public void testNullEnvtValue() {

		EchoMessageRequest param0 = new EchoMessageRequest();
		param0.setIn("MCC Test");
		try {
			SharedAdvertisingUniqueIDServiceV1Consumer testClient4 = new SharedAdvertisingUniqueIDServiceV1Consumer(
					"AdvertisingUniqueIDServiceV1Consumer", null);
			testClient4.getServiceInvokerOptions().setTransportName(
					SOAConstants.TRANSPORT_LOCAL);
			assertEquals(testClient4.echoMessage(param0).getOut(),
					" Echo Message = MCC Test");
			assertEquals(testClient4.getService().getResponseContext()
					.getTransportHeader("X-TURMERIC-RESPONSE-DATA-FORMAT"),
					"NV");
		} catch (ServiceException e) {
			assertTrue(e.getMessage().contains("environment can not be null"));
		}

	}

	/* Negative Cases */

	/*
	 * Missing client config folder from the envt.with below folder structure.
	 * SOAAsyncMCCTestConsumer
	 * /meta-src/META-INF/soa/client/config/SOAAsyncMCCTestConsumer_client
	 * /staging/ a. Appropriate error message should be thrown
	 */
	@Test
	public void testMissingCCFolder() {
		SharedAdvertisingUniqueIDServiceV1Consumer testClient5;

		// SharedSOAAsyncServiceConsumerEnvMapper testClient5;
		String errorMessage = "Unable to load file: META-INF/soa/client/config/"
				+ "AdvertisingUniqueIDServiceV1Consumer/staging/AdvertisingUniqueIDServiceV1/"
				+ "ClientConfig.xml";
		try {
			testClient5 = new SharedAdvertisingUniqueIDServiceV1Consumer(
					"AdvertisingUniqueIDServiceV1Consumer", "staging");
			EchoMessageRequest param0 = new EchoMessageRequest();
			param0.setIn("MCC Test");
			assertEquals(testClient5.echoMessage(param0).getOut(),
					" Echo Message = MCC Test");
		} catch (Exception e) {
			assertTrue(e.getMessage().contains(errorMessage));
		}
	}

	/*
	 * Missing client config folder from the envt.with below folder structure.
	 * SOAAsyncMCCTestConsumer/meta-src/META-INF/soa/client/config/
	 * SOAAsyncMCCTestConsumer_client/sandbox/ SOAAsyncService/ a. Appropriate
	 * error message should be thrown
	 */
	@Test
	public void testMissingCC() {
		SharedAdvertisingUniqueIDServiceV1Consumer testClient6;
		String errorMessage = "Unable to load file: META-INF/soa/client/config/"
				+ "AdvertisingUniqueIDServiceV1Consumer/sandbox/AdvertisingUniqueIDServiceV1/"
				+ "ClientConfig.xml";
		try {
			testClient6 = new SharedAdvertisingUniqueIDServiceV1Consumer(
					"AdvertisingUniqueIDServiceV1Consumer", "sandbox");
			EchoMessageRequest param0 = new EchoMessageRequest();
			param0.setIn("MCC Test");
			assertEquals(testClient6.echoMessage(param0).getOut(),
					" Echo Message = MCC Test");
		} catch (Exception e) {
			assertTrue(e.getMessage().contains(errorMessage));
		}

	}

	/*
	 * Invalid client name a. Appropriate error message should be thrown
	 */
	@Test
	public void testMissingClientName() {
		SharedAdvertisingUniqueIDServiceV1Consumer testClient7;
		String errorMessage = "Unable to load file: META-INF/soa/client/config/"
				+ "AdvertisingUniqueIDServiceV1Consumer_1/feature/AdvertisingUniqueIDServiceV1/"
				+ "ClientConfig.xml";
		try {
			testClient7 = new SharedAdvertisingUniqueIDServiceV1Consumer(
					"AdvertisingUniqueIDServiceV1Consumer_1", "feature");
			assertEquals(testClient7.testEnhancedRest(null).getOut(), "10");
		} catch (Exception e) {
			assertTrue(e.getMessage().contains(errorMessage));
		}

	}

	@Test
	public void testMissingEnvt() {
		SharedAdvertisingUniqueIDServiceV1Consumer testClient8;
		String errorMessage = "Unable to load file: META-INF/soa/client/config/"
				+ "AdvertisingUniqueIDServiceV1Consumer/myErrorEnvt/AdvertisingUniqueIDServiceV1/"
				+ "ClientConfig.xml";
		try {
			testClient8 = new SharedAdvertisingUniqueIDServiceV1Consumer(
					"AdvertisingUniqueIDServiceV1Consumer", "myErrorEnvt");
			assertEquals(testClient8.testEnhancedRest(null).getOut(), "10");
			assertTrue("consumer creation should fail and throw exception",
					false);
		} catch (Exception e) {
			assertTrue(e.getMessage().contains(errorMessage));
		}
	}

	/*
	 * When envtName is null, envMapper variable is not set
	 * BaseSOAAsyncMCCTestConsumer testClient = new
	 * BaseSOAAsyncMCCTestConsumer("", null); a. Appropriate error message
	 * should be thrown
	 */
	@Test
	public void testNullEnvtWhenEnvtMapperVariableisNotSet() throws Exception {
		SharedAdvertisingUniqueIDServiceV1Consumer testClient8;

		testClient8 = new SharedAdvertisingUniqueIDServiceV1Consumer(
				"AdvertisingUniqueIDServiceV1Consumer", null);
		testClient8
				.setHostName(serverUri.getHost() + ":" + serverUri.getPort());
		testClient8.getService().getInvokerOptions();
		EchoMessageRequest req = new EchoMessageRequest();
		req.setIn("test");
		assertEquals(testClient8.echoMessage(req).getOut(),
				" Echo Message = test");
		// Assert.assertTrue("consumer creation should fail and throw exception"
		// , false);
	}

	/*
	 * Client Name is null WhenEnvtMapperVariableisNotSet
	 * BaseSOAAsyncMCCTestConsumer testClient = new
	 * BaseSOAAsyncMCCTestConsumer(null, "production"); a. Appropriate error
	 * message should be thrown
	 */
	@Test
	public void testNullClientNameWhenEnvtMapperVariableisNotSet()
			throws Exception {
		SharedAdvertisingUniqueIDServiceV1Consumer testClient8;
		testClient8 = new SharedAdvertisingUniqueIDServiceV1Consumer(
				"AdvertisingUniqueIDServiceV1Consumer", "production");
		testClient8
				.setHostName(serverUri.getHost() + ":" + serverUri.getPort());

		EchoMessageRequest req = new EchoMessageRequest();
		req.setIn("test");
		assertEquals(testClient8.echoMessage(req).getOut(),
				" Echo Message = test");

	}

	/*
	 * Client Name is null When EnvtMapper Variable is Set
	 * BaseSOAAsyncMCCTestConsumer testClient = new
	 * BaseSOAAsyncMCCTestConsumer(null, "production"); a. Appropriate error
	 * message should be thrown
	 * http://quickbugstage.arch.ebay.com/show_bug.cgi?id=8279
	 */
	@Test
	public void testNullClientNameWhenEnvtMapperVariableisSet()
			throws Exception {
		SharedAdvertisingUniqueIDServiceV1Consumer testClient8;
		String errorMessage = "Unable to load file: META-INF/soa/client/config/"
				+ "SOAAsyncMCCTestServiceConsumer1_Client/myErrorEnvt/SOAAsyncService/"
				+ "ClientConfig.xml";
		testClient8 = new SharedAdvertisingUniqueIDServiceV1Consumer(
				"AdvertisingUniqueIDServiceV1Consumer", "production");
		testClient8
				.setHostName(serverUri.getHost() + ":" + serverUri.getPort());
		EchoMessageRequest req = new EchoMessageRequest();
		req.setIn("test");
		assertEquals(testClient8.echoMessage(req).getOut(),
				" Echo Message = test");
	}

	/*
	 * Related to BUGDB00651611
	 */
	@Test
	@Ignore
	public void testWithClientConfigBean() throws Exception {
		HttpTestClient http = HttpTestClient.getInstance();
		Map<String, String> queryParams = new HashMap<String, String>();
		String response = null;
		logger.debug(" ** testWithClientConfigBean ** ");
		queryParams.clear();
		String testURL = "http://"
				+ serverUri.getHost()
				+ ":"
				+ serverUri.getPort()
				+ "/ws/spf?X-TURMERIC-SOA-SERVICE-NAME=AdvertisingUniqueIDServiceV1&X-EBAY-SOA-OPERATION-NAME=getRequestID";
		response = http.getResponse(testURL, queryParams);
		// http://localhost:8080/ws/spf?X-EBAY-SOA-SERVICE-NAME=AdvertisingUniqueIDServiceV1&X-EBAY-SOA-OPERATION-NAME=getRequestID
		queryParams.put("X-TURMERIC-SOA-SERVICE-NAME",
				"AdvertisingUniqueIDServiceV1");
		queryParams.put("X-TURMERIC-SOA-OPERATION-NAME", "getRequestID");
		http.getResponse(
				"http://" + serverUri.getHost() + ":" + serverUri.getPort()
						+ "/ws/spf", queryParams);
		queryParams.clear();
		queryParams
				.put("id",
						"com.ebay.soa.client.AdvertisingUniqueIDServiceV2.UniqueIDServiceV2Client.dev.Invoker");
		queryParams.put("REQUEST_BINDING", "JSON");
		response = MetricUtil.invokeHttpClient(queryParams, "update");
		queryParams.put("forceXml", "true");
		response = MetricUtil.invokeHttpClient(queryParams, "view");
		logger.debug("Response - " + response);
		assertTrue(
				"Error - Request Binding is not updated to JSON ",
				MetricUtil.parseXML(response, "REQUEST_BINDING").contentEquals(
						"JSON"));
		logger.debug(" ** testWithClientConfigBean ** ");
		queryParams.clear();
		queryParams
				.put("id",
						"com.ebay.soa.client.AdvertisingUniqueIDServiceV2.UniqueIDServiceV2Client.dev.Invoker");
		queryParams.put("REQUEST_BINDING", "XML");
		response = MetricUtil.invokeHttpClient(queryParams, "update");
	}
}
