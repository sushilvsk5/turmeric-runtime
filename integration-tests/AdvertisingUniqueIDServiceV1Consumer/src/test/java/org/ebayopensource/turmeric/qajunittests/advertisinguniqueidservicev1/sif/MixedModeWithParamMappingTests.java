package org.ebayopensource.turmeric.qajunittests.advertisinguniqueidservicev1.sif;

import java.net.MalformedURLException;
import java.util.HashMap;
import java.util.Map;

import org.ebayopensource.turmeric.runtime.common.exceptions.ServiceException;
import org.ebayopensource.turmeric.runtime.tests.common.jetty.AbstractWithServerQETest;
import org.ebayopensource.turmeric.runtime.tests.common.util.HttpTestClient;
import org.junit.Test;
import static org.junit.Assert.*;

import com.ebay.kernel.service.invocation.client.http.Request;
import com.ebay.kernel.service.invocation.client.http.Response;

public class MixedModeWithParamMappingTests extends AbstractWithServerQETest{
	public static HttpTestClient http = HttpTestClient.getInstance();
	public Map<String, String> queryParams = new HashMap<String, String>();
	String response = null;
	
	@Test
	public void testRegularScenario1WithValidPayload() throws org.ebayopensource.turmeric.runtime.common.exceptions.ServiceException, MalformedURLException {
		logger.debug(" ** testRegularScenario1WithValidPayload ** ");
		Request request = new Request(
				serverUri.toASCIIString()+"/services/advertise/UniqueIDService/v1/testSchemaValidationWithUPA/2/1230/foo");
		request.addHeader("X-TURMERIC-OPERATION-NAME", "testSchemaValidationWithUPA");
		//queryParams.put("X-TURMERIC-OPERATION-NAME", "testSchemaValidationWithUPA");
		String body = "<?xml version='1.0' encoding='UTF-8'?>" + 
			"<testSchemaValidationWithUPA xmlns=\"http://www.ebay.com/marketplace/advertising/v1/services\">" +
			"<clientId>schemavalidation</clientId><siteId>0</siteId><language>us-ENG</language>" + 
			"</testSchemaValidationWithUPA>";
		Response response = http.getResponse(request, queryParams, body, "POST");
		logger.debug(response.getBody());
		assertTrue(response.getBody().
				contains("Call reached IMPL as schemaValidation went thru fine.siteid - 1230clientid - foolang - 2"));
		logger.debug(" ** testRegularScenario1WithValidPayload ** ");
	}
	
	@Test
	public void testRegularScenarioWithMissingValuesInPayload() throws ServiceException, MalformedURLException {
		logger.debug(" ** testRegularScenarioWithMissingValuesInPayload ** ");
		Request request = new Request(
				serverUri.toASCIIString()+"/services/advertise/UniqueIDService/v1/testSchemaValidationWithUPA/2/1230/foo");
		request.addHeader("X-TURMERIC-OPERATION-NAME", "testSchemaValidationWithUPA");
		//queryParams.put("X-TURMERIC-OPERATION-NAME", "testSchemaValidationWithUPA");
		String body = "<?xml version='1.0' encoding='UTF-8'?>" + 
			"<testSchemaValidationWithUPA xmlns=\"http://www.ebay.com/marketplace/advertising/v1/services\">" +
			"<clientId></clientId><siteId></siteId><language></language>" + 
			"</testSchemaValidationWithUPA>";
		Response response = http.getResponse(request, queryParams, body, "POST");
		logger.debug(response.getBody());
		assertTrue(response.getBody()
				.contains("Call reached IMPL as schemaValidation went thru fine.siteid - 1230clientid - foolang - 2"));
		logger.debug(" ** testRegularScenarioWithMissingValuesInPayload ** ");
	}
	
	@Test
	public void testWithPostOperationMapping1() throws ServiceException, MalformedURLException {
		logger.debug(" ** testWithPostOperationMapping ** ");
		String body = "<?xml version='1.0' encoding='UTF-8'?><testEnhancedRest" +
		" xmlns:ms=\"http://www.ebay.com/marketplace/services\"" +
		" xmlns:ns3=\"http://www.ebay.com/soa/test/user\"" +
		" xmlns:ns2=\"http://www.ebay.com/soa/test/payment\"" +
		" xmlns=\"http://www.ebay.com/marketplace/advertising/v1/services\">" +
		" <in>hello</in></testEnhancedRest>";
		Request request = new Request(serverUri.toASCIIString()+"/services/advertise/UniqueIDService/v1/enhanced/foo");
		//request.addHeader("X-TURMERIC-OPERATION-NAME", "testEnhancedRest");
		queryParams.put("X-TURMERIC-OPERATION-NAME", "testEnhancedRest");
		Response response = http.getResponse(
				request, queryParams, body,  "POST");
		logger.debug("test" + response.getBody());
		assertTrue(response.getBody().contains("<ns2:out>test</ns2:out>"));
		
	}
	
	
	@Test
	public void testWithPostOperationMapping2() throws ServiceException, MalformedURLException {
		logger.debug(" ** testWithPostOperationMapping ** ");
		String body = "<?xml version='1.0' encoding='UTF-8'?><testEnhancedRest" +
		" xmlns:ms=\"http://www.ebay.com/marketplace/services\"" +
		" xmlns:ns3=\"http://www.ebay.com/soa/test/user\"" +
		" xmlns:ns2=\"http://www.ebay.com/soa/test/payment\"" +
		" xmlns=\"http://www.ebay.com/marketplace/advertising/v1/services\">" +
		" <in>hello</in></testEnhancedRest>";
		Request request = new Request(serverUri.toASCIIString()+"/services/advertise/UniqueIDService/v1/enhanced/foo");
		//request.addHeader("X-TURMERIC-OPERATION-NAME", "testEnhancedRest");
		queryParams.put("X-TURMERIC-OPERATION-NAME", "testEnhancedRest");
		Response response = http.getResponse(
				request, queryParams, body,  "POST");
		logger.debug("test" + response.getBody());
		assertTrue(response.getBody().contains("<ns2:out>test</ns2:out>"));
		
	}
	
	/*@Test
	public void testMixedModePositiveCaseRemoteMode() throws ServiceException, MalformedURLException {
		AdvertisingUniqueIDServiceV1SharedConsumer client = new AdvertisingUniqueIDServiceV1SharedConsumer("AdvertisingUniqueIDServiceV1Consumer", "MixedMode3");
		client.getService().setServiceLocation(new URL("http://localhost:9090/services/advertise/UniqueIDService/v1/enhanced/foo"));
		TestEnhancedRest param0 = new TestEnhancedRest();
		param0.getIn().add("bar");
		logger.debug(client.testEnhancedRest(param0).getOut());
//		Assert.assertEquals(client.echoMessage(param0).getOut(), " Response foo");
	}
	
	
	@Test
	public void testMixedModePositiveCaseRemoteModeWithGetEnabled() throws ServiceException {
		AdvertisingUniqueIDServiceV1SharedConsumer client = new AdvertisingUniqueIDServiceV1SharedConsumer("AdvertisingUniqueIDServiceV1Consumer", "MixedMode3");
		TestEnhancedRest param0 = new TestEnhancedRest();
		param0.getIn().add(0, "MixedMode");
		client.getServiceInvokerOptions().setREST(Boolean.TRUE);
		logger.debug(client.testEnhancedRest(param0).getOut());
//		Assert.assertEquals(client.testEnhancedRest(param0).getOut(), " Response foo");
	}
	@Test
	public void testMixedModePositiveCaseRemoteModeWithURL() throws ServiceException, MalformedURLException {
		AdvertisingUniqueIDServiceV1SharedConsumer client = new AdvertisingUniqueIDServiceV1SharedConsumer("AdvertisingUniqueIDServiceV1Consumer", "MixedMode3");
		TestEnhancedRest param0 = new TestEnhancedRest();
		param0.getIn().add(0, "MixedMode");
		client.getService().setServiceLocation(new URL("http://localhost:8080/services/advertise/UniqueIDService/v1"));
		logger.debug(client.testEnhancedRest(param0).getOut());
//		Assert.assertEquals(client.echoMessage(param0).getOut(), " Response MixedMode");
	}
	@Test
	public void testMixedModePositiveCaseRemoteModeWithURLGet() throws ServiceException, MalformedURLException {
		AdvertisingUniqueIDServiceV1SharedConsumer client = new AdvertisingUniqueIDServiceV1SharedConsumer("AdvertisingUniqueIDServiceV1Consumer", "MixedMode3");
		client.getServiceInvokerOptions().setREST(Boolean.TRUE);
		TestEnhancedRest param0 = new TestEnhancedRest();
		param0.getIn().add(0, "MixedMode");
		client.getService().setServiceLocation(new URL("http://localhost:8080/services/advertise/UniqueIDService/v1"));
		logger.debug(client.testEnhancedRest(param0).getOut());
//		Assert.assertEquals(client.testEnhancedRest(param0).getOut(), " Response MixedMode");
	}
	
	@Test
	public void testMixedModePositiveCaseLocalMode() throws ServiceException {
		AdvertisingUniqueIDServiceV1SharedConsumer client = new AdvertisingUniqueIDServiceV1SharedConsumer("AdvertisingUniqueIDServiceV1Consumer", "MixedMode3");
		TestEnhancedRest param0 = new TestEnhancedRest();
		param0.getIn().add(0, "MixedMode");
		client.getServiceInvokerOptions().setTransportName(SOAConstants.TRANSPORT_LOCAL);
		logger.debug(client.testEnhancedRest(param0).getOut());
//		Assert.assertEquals(client.testEnhancedRest(param0).getOut(), " Response foo");
	}
	@Test
	public void testMixedModePositiveCaseOtherOperationRemote() throws ServiceException {
		AdvertisingUniqueIDServiceV1SharedConsumer client = new AdvertisingUniqueIDServiceV1SharedConsumer("AdvertisingUniqueIDServiceV1Consumer", "MixedMode3");
		TestEnhancedRest param0 = new TestEnhancedRest();
		param0.getIn().add(0, "MixedMode");
		logger.debug(client.testEnhancedRest(param0).getOut());
//		Assert.assertEquals(client.testEnhancedRest(param0).getOut(), " Response foo" );
	}
	@Test
	public void testMixedModePositiveCaseOtherOperationLocal() throws ServiceException {
		AdvertisingUniqueIDServiceV1SharedConsumer client = new AdvertisingUniqueIDServiceV1SharedConsumer("AdvertisingUniqueIDServiceV1Consumer", "MixedMode3");
		client.getServiceInvokerOptions().setTransportName(SOAConstants.TRANSPORT_LOCAL);
		
		TestEnhancedRest param0 = new TestEnhancedRest();
		param0.getIn().add(0, "MixedMode");
		logger.debug(client.testEnhancedRest(param0).getOut());
//		Assert.assertEquals(client.testEnhancedRest(param0).getOut(), " Response foo" );
	}*/
	
}
