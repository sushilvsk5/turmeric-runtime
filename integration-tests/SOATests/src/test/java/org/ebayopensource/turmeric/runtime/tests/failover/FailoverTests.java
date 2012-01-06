package org.ebayopensource.turmeric.runtime.tests.failover;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import junit.framework.TestCase;

import org.ebayopensource.turmeric.runtime.sif.service.Service;
import org.ebayopensource.turmeric.runtime.sif.service.ServiceFactory;
import org.ebayopensource.turmeric.runtime.tests.common.jetty.SimpleJettyServer;
import org.ebayopensource.turmeric.runtime.tests.common.sif.error.MarkdownTestHelper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class FailoverTests extends TestCase {
	private static Thread serverThread;
	
	private final Logger logger = LoggerFactory.getLogger(FailoverTests.class);
	
	static class RunPoxyServer implements Runnable {

		@Override
		public void run() {
			SimpleJettyServer.main(null);
		}
		
	}
	public void setUp(){
		serverThread = new Thread(new RunPoxyServer());
		serverThread.start();
		MarkdownTestHelper.markupClientManually("test1", null, null);
	}
	@Override
	public void tearDown(){
		serverThread.stop();
	}
	
	public void testSimple() throws Exception{
		URL url = new URL("http://localhost:8080/ws/spf");
		Service service = ServiceFactory.create("Test1Service", "keepAlive", url);
		String outMessage = (String) service.createDispatch("echoString").invoke("hello");
		logger.debug("outMessage:"+outMessage);
	}
	
	public void testMultiple() throws Exception{
		List<URL> urls = new ArrayList<URL>();
		URL url = new URL("http://nothing:8080/ws/spf");
		urls.add(url);
		url = new URL("http://localhost:8080/ws/spf");
		urls.add(url);
		
		Service service = ServiceFactory.create("test1", "failover");
		service.setServiceLocations(urls);
		String outMessage = (String) service.createDispatch("echoString").invoke("hello");
		logger.debug("outMessage:"+outMessage);
		assertEquals("hello", outMessage);
	}
	
	public void testMultipleFail() throws Exception{
		List<URL> urls = new ArrayList<URL>();
		URL url = new URL("http://nothing:8080/ws/spf");
		urls.add(url);
		url = new URL("http://nothingagain:8080/ws/spf");
		urls.add(url);
		
		Service service = ServiceFactory.create("test1b", "failover");
		service.setServiceLocations(urls);
		Map<String, String> options = new HashMap<String, String>();
		service.getInvokerOptions().getTransportOptions().setProperties(options);
		try{
			service.createDispatch("echoString").invoke("hello");
			assertTrue(false);
		}catch(Exception e){
			assertTrue(true);
		}
		
			
	}
	
	public void testMultipleWithConfigFile() throws Exception{
		Service service = ServiceFactory.create("test1", "failover"); //shd pick up the cc.xml?
		String outMessage = (String) service.createDispatch("echoString").invoke("hello");
		logger.debug("outMessage:"+outMessage);
	}
	
	public void testMultipleFailWithConfigFile() throws Exception{
		Service service = ServiceFactory.create("test1a", "failover"); //shd pick up the cc.xml?
		try{
			String outMessage = (String) service.createDispatch("echoString").invoke("hello");
			fail("Should have failed."); //shoiuld have failed
		}catch(Exception e){
			
		}
	}
	
	public void testSuccessiveInvocations() throws Exception{
		List<URL> urls = new ArrayList<URL>();
		URL url = new URL("http://localhost:8080/ws/spf");
		urls.add(url);
		Service service = ServiceFactory.create("test1", "failover");
		service.setServiceLocations(urls);
		String outMessage = (String) service.createDispatch("echoString").invoke("hello");
		assertEquals("hello", outMessage);
		url = new URL("http://nothing:8080/ws/spf");
		service.setServiceLocation(url);
		try{
			outMessage = (String) service.createDispatch("echoString").invoke("hello");
			fail("Should have failed");
		}catch(Exception e){
			
		}
	}
}
