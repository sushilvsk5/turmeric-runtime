package org.ebayopensource.turmeric.qajunittests.advertisinguniqueidservicev1.sif;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.ebayopensource.turmeric.advertising.v1.services.EchoMessageRequest;
import org.ebayopensource.turmeric.advertising.v1.services.GetRequestIDResponse;
import org.ebayopensource.turmeric.advertisinguniqueservicev1.AdvertisingUniqueIDServiceV1SharedConsumer;
import org.ebayopensource.turmeric.runtime.common.exceptions.ServiceException;
import org.ebayopensource.turmeric.runtime.common.exceptions.ServiceInvocationRuntimeException;
import org.ebayopensource.turmeric.runtime.common.types.SOAConstants;
import org.ebayopensource.turmeric.runtime.tests.common.jetty.AbstractWithServerTest;
import org.ebayopensource.turmeric.runtime.tests.common.util.HttpTestClient;
import org.ebayopensource.turmeric.runtime.tests.common.util.MetricUtil;
import static org.junit.Assert.*;
import org.junit.Ignore;
import org.junit.Test;

import com.ebay.kernel.logger.LogLevel;

public class ClientSideFailoverTests extends AbstractWithServerTest{
	public static HttpTestClient http = HttpTestClient.getInstance();
	public Map<String, String> queryParams = new HashMap<String, String>();

	/*
	 * CC.xml 
	 * <service-location>http://localhost:9090/foo</service-location>
	 * <service-location>http://localhost:9091/services/advertise/UniqueIDService/v1</service-location>
	 * <service-location>http://localhost:8080/services/advertise/UniqueIDService/v1</service-location>
	 */
	@Test
	public void testValidScenario1() throws ServiceException {
		AdvertisingUniqueIDServiceV1SharedConsumer client = 
			new AdvertisingUniqueIDServiceV1SharedConsumer("AdvertisingUniqueIDServiceV1Consumer", "failover1");
		try {
			client.setHostName(serverUri.getHost()+":"+serverUri.getPort());
			logger.debug(client.getHostName());
			logger.debug("client.getServiceInvokerOptions() " + client.getServiceInvokerOptions().getTransportName());
		} catch (MalformedURLException e) {
			// TODO Auto-generated catch blocks
			e.printStackTrace();
		}
		
		EchoMessageRequest req = new EchoMessageRequest();
		req.setIn("vasu");
		String response = client.echoMessage(req).getOut();
		assertEquals(" Echo Message = vasu", response);
	}

	@Test
	public void testValidScenario2() throws ServiceException {
		AdvertisingUniqueIDServiceV1SharedConsumer client = 
			new AdvertisingUniqueIDServiceV1SharedConsumer("AdvertisingUniqueIDServiceV1Consumer", "failover1");
		try {
			client.setHostName(serverUri.getHost()+":"+serverUri.getPort());
		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		client.getServiceInvokerOptions().setTransportName(SOAConstants.TRANSPORT_LOCAL);
		EchoMessageRequest req = new EchoMessageRequest();
		req.setIn("vasu");
		String response = client.echoMessage(req).getOut();
		assertEquals(" Echo Message = vasu", response);
	}

	@Test
	public void testValidScenario3() throws ServiceException, MalformedURLException {
		AdvertisingUniqueIDServiceV1SharedConsumer client = 
			new AdvertisingUniqueIDServiceV1SharedConsumer("AdvertisingUniqueIDServiceV1Consumer", "failover1");
		try {
			client.setHostName(serverUri.getHost()+":"+serverUri.getPort());
			logger.debug(client.getHostName());
		} catch (MalformedURLException e) {
			// TODO Auto-generated catch blocks
			e.printStackTrace();
		}
		client.getService().setServiceLocation(new URL("http://"+serverUri.getHost()+":"+serverUri.getPort()+"/services/advertise/UniqueIDService/v1"));

		EchoMessageRequest req = new EchoMessageRequest();
		req.setIn("vasu");
		String response = client.echoMessage(req).getOut();
		assertEquals(" Echo Message = vasu", response);
	}

	@Test
	public void testValidScenario4() throws ServiceException, MalformedURLException {
		List<URL> sl = new ArrayList<URL> ();
		sl.add(new URL("http://"+serverUri.getHost()+":8081/services/advertise/UniqueIDService/v1"));
		sl.add(new URL("http://"+serverUri.getHost()+":"+serverUri.getPort()+"/services/advertise/UniqueIDService/v1"));
		AdvertisingUniqueIDServiceV1SharedConsumer client = 
			new AdvertisingUniqueIDServiceV1SharedConsumer("AdvertisingUniqueIDServiceV1Consumer", "failover1");
		try {
			client.setHostName(serverUri.getHost()+":"+serverUri.getPort());
			logger.debug(client.getHostName());
		} catch (MalformedURLException e) {
			logger.log(LogLevel.DEBUG, e);
		}
		client.getService().setServiceLocations(sl);
		
		EchoMessageRequest req = new EchoMessageRequest();
		req.setIn("vasu");
		String response = client.echoMessage(req).getOut();
		assertEquals(" Echo Message = vasu", response);
	}

	@Test
	public void testValidScenario5() throws ServiceException, MalformedURLException {
		AdvertisingUniqueIDServiceV1SharedConsumer client = 
			new AdvertisingUniqueIDServiceV1SharedConsumer("AdvertisingUniqueIDServiceV1Consumer", "failover1");
		try {
			client.setHostName(serverUri.getHost()+":"+serverUri.getPort());
			logger.debug(client.getHostName());
		} catch (MalformedURLException e) {
			logger.log(LogLevel.DEBUG, e);
		}
		client.getService().setServiceLocation(new URL("http://"+serverUri.getHost()+":"+serverUri.getPort()+"/ws/spf?X-EBAY-SOA-SERVICE-VERSION=1.0.0"));
		
		EchoMessageRequest req = new EchoMessageRequest();
		req.setIn("vasu");
		String response = client.echoMessage(req).getOut();
		assertEquals(" Echo Message = vasu", response);
	}

	@Test
	public void testValidScenario6() throws ServiceException, MalformedURLException {
		List<URL> sl = new ArrayList<URL> ();
		sl.add(new URL("http://"+serverUri.getHost()+":8081/services/advertise/UniqueIDService/v1"));
		sl.add(new URL("http://"+serverUri.getHost()+":"+serverUri.getPort()+"/ws/spf"));
		AdvertisingUniqueIDServiceV1SharedConsumer client = 
			new AdvertisingUniqueIDServiceV1SharedConsumer("AdvertisingUniqueIDServiceV1Consumer", "failover1");
		try {
			client.setHostName(serverUri.getHost()+":"+serverUri.getPort());
			logger.debug(client.getHostName());
		} catch (MalformedURLException e) {
			logger.log(LogLevel.DEBUG, e);
		}
		client.getService().setServiceLocations(sl);
		EchoMessageRequest req = new EchoMessageRequest();
		req.setIn("vasu");
		String response = client.echoMessage(req).getOut();
		assertEquals(" Echo Message = vasu", response);
	}
	@Test
	public void testValidScenario7() throws ServiceException, MalformedURLException {
		AdvertisingUniqueIDServiceV1SharedConsumer client = 
			new AdvertisingUniqueIDServiceV1SharedConsumer("AdvertisingUniqueIDServiceV1Consumer", "failover2");
		try {
			client.setHostName(serverUri.getHost()+":"+serverUri.getPort());
			logger.debug(client.getHostName());
		} catch (MalformedURLException e) {
			logger.log(LogLevel.DEBUG, e);
		}
		EchoMessageRequest req = new EchoMessageRequest();
		req.setIn("vasu");
		String response = client.echoMessage(req).getOut();
		assertEquals(" Echo Message = vasu", response);
	}

	@Test
	public void testChainedServiceConfig() throws ServiceException {
		AdvertisingUniqueIDServiceV1SharedConsumer client = 
			new AdvertisingUniqueIDServiceV1SharedConsumer("AdvertisingUniqueIDServiceV1Consumer", "failover1");
		try {
			client.setHostName(serverUri.getHost()+":"+serverUri.getPort());
			logger.debug(client.getHostName());
		} catch (MalformedURLException e) {
			logger.log(LogLevel.DEBUG, e);
		}
		client.getService().getRequestContext().setTransportHeader("CLIENT-FAILOVER", "");
		GetRequestIDResponse res = client.getReqID("HTTP10");
		logger.debug(res.getRequestID());
		assertTrue(res.getRequestID().contains("AdvertisingUniqueIDServiceV1"));
	}

	@Test
	@Ignore
	public void testViewConfigBean() throws ServiceException {
		AdvertisingUniqueIDServiceV1SharedConsumer client1 = 
			new AdvertisingUniqueIDServiceV1SharedConsumer("AdvertisingUniqueIDServiceV1Consumer", "failover1");
		client1.getService().getRequestContext().setTransportHeader("CLIENT-FAILOVER", "");
		try {
			client1.setHostName(serverUri.getHost()+":"+serverUri.getPort());
			logger.debug(client1.getHostName());
		} catch (MalformedURLException e) {
			e.printStackTrace();
		}
		GetRequestIDResponse res = client1.getReqID("HTTP10");
		assertTrue(res.getRequestID().contains("AdvertisingUniqueIDServiceV1"));
		try {
			queryParams.put("id",
			"com.ebay.soa.client.AdvertisingUniqueIDServiceV2.UniqueIDServiceV2Client.failover.Invoker");
			queryParams.put("forceXml","true");
			String response = MetricUtil.invokeHttpClient(queryParams, "view");
			logger.debug("Response - " + response);
			assertTrue(response.contains("name=\"SERVICE_URL\""));
			assertTrue(response.contains("http:"+serverUri.getHost()+":"+serverUri.getPort()+"/services/advertise/UniqueIDService/v2"));
			assertTrue(response.contains("http:"+serverUri.getHost()+":"+serverUri.getPort()+"/services/advertise/UniqueIDService/v2"));
			assertTrue(response.contains("http:"+serverUri.getHost()+":"+serverUri.getPort()+"/foo"));
		} catch (Exception se) {
			logger.log(LogLevel.DEBUG, se);
			assertTrue("Error - No Exception should be thrown ", false);
		} 

	}
	@Test
	@Ignore
	public void testUpdateConfigBean() throws ServiceException  {
		AdvertisingUniqueIDServiceV1SharedConsumer client2 = 
			new AdvertisingUniqueIDServiceV1SharedConsumer("AdvertisingUniqueIDServiceV1Consumer", "failover1");

		client2.getService().getRequestContext().setTransportHeader("CLIENT-FAILOVER", "");
		try {
			client2.setHostName(serverUri.getHost()+":"+serverUri.getPort());
			logger.debug(client2.getHostName());
		} catch (MalformedURLException e) {
			// TODO Auto-generated catch blocks
			e.printStackTrace();
		}
		GetRequestIDResponse res = client2.getReqID("HTTP10");
		assertTrue(res.getRequestID().contains("AdvertisingUniqueIDServiceV1"));

		try {
			queryParams.put("id","com.ebay.soa.client.AdvertisingUniqueIDServiceV2.UniqueIDServiceV2Client.failover.Invoker");
			queryParams.put("SERVICE_URL", "http://localhost:9090/services/advertise/UniqueIDService/v2,http://localhost:9090/foo");
			String response = MetricUtil.invokeHttpClient(queryParams, "update");
			logger.debug("Response - " + response);
			assertTrue(response.contains("SERVICE_URL"));
			assertTrue(response.
					contains("http://localhost:9090/services/advertise/UniqueIDService/v2"));
			assertFalse(response.
					contains("http://localhost:8080/services/advertise/UniqueIDService/v2"));
			assertTrue(response.
					contains("http://localhost:9090/foo"));
		} catch (Exception se) {
			logger.log(LogLevel.DEBUG, se);
		} 
		finally {
		queryParams.put("id","com.ebay.soa.client.AdvertisingUniqueIDServiceV2.UniqueIDServiceV2Client.failover.Invoker");
		queryParams.put("SERVICE_URL", "http://localhost:9090/services/advertise/UniqueIDService/v2,http://localhost:8080/foo," +
				"http://localhost:8080/services/advertise/UniqueIDService/v2");
		MetricUtil.invokeHttpClient(queryParams, "update");
		} 

	}

	//	 * -ve scenarios


	@Test
	public void testMissingServiceLocations1() throws ServiceException, MalformedURLException {
		AdvertisingUniqueIDServiceV1SharedConsumer client = 
			new AdvertisingUniqueIDServiceV1SharedConsumer("AdvertisingUniqueIDServiceV1Consumer", "failoverError1");
		client.setHostName(serverUri.getHost()+":"+serverUri.getPort());
		EchoMessageRequest req = new EchoMessageRequest();
		req.setIn("vasu");
		String response = client.echoMessage(req).getOut();
		assertEquals(" Echo Message = vasu", response);
	}

	@Test
	public void testMissingServiceLocations2() throws ServiceException, MalformedURLException {
		AdvertisingUniqueIDServiceV1SharedConsumer client = 
			new AdvertisingUniqueIDServiceV1SharedConsumer("AdvertisingUniqueIDServiceV1Consumer", "failoverError2");
		
		client.setHostName(serverUri.getHost()+":"+serverUri.getPort());
		EchoMessageRequest req = new EchoMessageRequest();
		req.setIn("vasu");
		String response = client.echoMessage(req).getOut();
		assertEquals(" Echo Message = vasu", response);
	}


	@Test
	public void testNullCheck1() {

		AdvertisingUniqueIDServiceV1SharedConsumer client;
		try {
			client = new AdvertisingUniqueIDServiceV1SharedConsumer("AdvertisingUniqueIDServiceV1Consumer", "failover2");

			client.getService().setServiceLocations(null);
			EchoMessageRequest req = new EchoMessageRequest();
			req.setIn("vasu");
			String response = client.echoMessage(req).getOut();
			logger.debug(response);
		} catch (ServiceInvocationRuntimeException e) {
			// TODO Auto-generated catch block
			logger.debug(e.getMessage());
			assertTrue(e.getMessage().contains("No service address defined for invocation of service"));
			logger.log(LogLevel.DEBUG, e);

		} catch (ServiceException e) {
			logger.log(LogLevel.DEBUG, e);
		} catch (Exception e) {
			logger.log(LogLevel.DEBUG, e);
		}	
	}

	@Test
	public void testNullCheck2() throws ServiceInvocationRuntimeException{

		AdvertisingUniqueIDServiceV1SharedConsumer client;
		try {
			client = new AdvertisingUniqueIDServiceV1SharedConsumer("AdvertisingUniqueIDServiceV1Consumer", "failover2");
			//			client.getService().setServiceLocation(new URL("http://localhost:9090/services/advertise/UniqueIDService/v1"));
			List<URL> locations = new ArrayList<URL> ();
			locations.add(new URL(""));
			locations.add(new URL("http://localhost:9080/services/advertise/UniqueIDService/v1"));
			locations.add(new URL("http://"+ serverUri.getHost()+":"+serverUri.getPort()+"/services/advertise/UniqueIDService/v1"));
			client.getService().setServiceLocations(locations);
			EchoMessageRequest req = new EchoMessageRequest();
			req.setIn("vasu");
			String response = client.echoMessage(req).getOut();
			logger.debug(response);
			assertEquals(" Echo Message = vasu", response);
		} catch (ServiceException e) {
			logger.log(LogLevel.DEBUG, e);
		} catch (MalformedURLException e) {
			logger.log(LogLevel.DEBUG, e);
		}catch (Exception e) {
			logger.log(LogLevel.DEBUG, e);
		}	
	}

}

