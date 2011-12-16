package org.ebayopensource.turmeric.qajunittests.advertisinguniqueidservicev1.sif;

import static org.junit.Assert.assertNull;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.RandomAccessFile;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

import javax.activation.DataHandler;
import javax.activation.FileDataSource;
import javax.xml.ws.AsyncHandler;
import javax.xml.ws.Response;
import javax.xml.ws.WebServiceException;

import junit.framework.Assert;

import org.ebayopensource.turmeric.advertising.v1.services.ChainedTransportHeaders;
import org.ebayopensource.turmeric.advertising.v1.services.ChainedTransportHeadersResponse;
import org.ebayopensource.turmeric.advertising.v1.services.FileAttachmentType;
import org.ebayopensource.turmeric.advertising.v1.services.GetTransportHeaders;
import org.ebayopensource.turmeric.advertising.v1.services.GetTransportHeadersResponse;
import org.ebayopensource.turmeric.advertising.v1.services.TestAttachment;
import org.ebayopensource.turmeric.advertising.v1.services.TestAttachmentResponse;
import org.ebayopensource.turmeric.advertisinguniqueidservicev1.gen.SharedAdvertisingUniqueIDServiceV1Consumer;
import org.ebayopensource.turmeric.runtime.common.exceptions.ServiceException;
import org.ebayopensource.turmeric.runtime.tests.common.jetty.AbstractWithServerTest;
import org.ebayopensource.turmeric.runtime.tests.common.util.MetricUtil;
import org.ebayopensource.turmeric.runtime.tests.gen.AdvertisingUniqueIDServiceV1Proxy;
import org.junit.After;
import org.junit.BeforeClass;
import org.junit.Ignore;
import org.junit.Test;

import com.ebay.kernel.util.FileUtils;


public class AsyncClientStreamingAttachmentTests extends AbstractWithServerTest {
	FileAttachmentType response = null;
	
	public static final long DEFAULT_SLEEP_TIME = 100;
	static File f1, f2, f3,f4,f5,f6;
	static long MAX_SIZE1, MAX_SIZE2;
	int CHUNK_SIZE = 4096;
	long sizeCounter = 0;
	BufferedInputStream br = null;
	FileOutputStream out = null;
	static String currentDir;
	
	@BeforeClass
	public static void getParams() throws IOException {
		currentDir = System.getProperty("user.dir");
		f1 = new File(currentDir + "\\3mbAttachment.txt");
		f2 = new File(currentDir + "\\Server3mbAttachment.txt");

		f3 = new File(currentDir + "\\Client3mbAttachment.txt");
		f4 = new File(currentDir + "\\3gbAttachment.txt");
		f5 = new File(currentDir + "\\Client3gbAttachment.txt");
		f6 = new File(currentDir + "\\Server3gbAttachment.txt");
//		if (f1.exists()) f1.delete();
		if (f2.exists()) f2.delete();
		if (f3.exists()) f3.delete();
//		if (f4.exists()) f4.delete(); // don't delete this file but on demand - it's 3 GB, it takes a while to recreate!
		if (f5.exists()) f5.delete();
//		if (f6.exists()) f6.delete();
		if (!f4.exists()) 
			createFileForTest("gb", f4);
		if (!f1.exists()) 
			createFileForTest("mb", f1);
		MAX_SIZE1 = f1.length();
		MAX_SIZE2 = f4.length();

	}
	
	@After
	public void cleanUp() {
				if (f2.exists()) f2.delete();
				if (f3.exists()) f3.delete();
				if (f5.exists()) f5.delete();
//				if (f6.exists()) f6.delete();
	
	}
	
	@Test@
	Ignore("client streaming feature is not supported in opensource.Ignoring tests")
	public void testAsyncPullClientStreamingTrue() throws Exception {
		logger.debug("-- testAsyncPullClientStreamingTrue --");
		SharedAdvertisingUniqueIDServiceV1Consumer client = new SharedAdvertisingUniqueIDServiceV1Consumer("AdvertisingUniqueIDServiceV1Consumer", "ClientStreaming");
		GetTransportHeaders param0 = new GetTransportHeaders();
		param0.getIn().add(0, "X-EBAY-SOA-CCTEST-HEADER1");
		Response<GetTransportHeadersResponse> resp = client.getTransportHeadersAsync(param0);
		while (!resp.isDone()) {
			Thread.sleep(1000L);
		}
		logger.debug("isDone is true, now process response.");
		logger.debug(resp.get().getOut().get(0));
		Assert.assertEquals("X-EBAY-SOA-CCTEST-HEADER1 BAR", resp.get().getOut().get(0));
		logger.debug("-- testAsyncPullClientStreamingTrue --");
	}

	@Test
	@Ignore("client streaming feature is not supported in opensource.Ignoring tests")
	public void testAsyncPushClientStreamingTrue() throws Exception {
		logger.debug("-- testAsyncPushClientStreamingTrue --");
		SharedAdvertisingUniqueIDServiceV1Consumer client = new SharedAdvertisingUniqueIDServiceV1Consumer("AdvertisingUniqueIDServiceV1Consumer", "ClientStreaming");
		GetTransportHeaders param0 = new GetTransportHeaders();
		param0.getIn().add(0, "X-EBAY-SOA-CCTEST-HEADER1");
		AttachmentAsyncHandler<GetTransportHeadersResponse> attHandler = new AttachmentAsyncHandler<GetTransportHeadersResponse>();
		Future<?> attFutureObj = null;
		//			client.getService().setExecutor(null);
		attFutureObj = client.getTransportHeadersAsync(param0, attHandler);
		//			attFutureObj.isDone()
		while (!attFutureObj.isDone() && !attFutureObj.isDone()) {
			try {
//				logger.debug("sleep");
				Thread.sleep(DEFAULT_SLEEP_TIME);
			} catch (InterruptedException e) {
				// Ignore
			}
		}
		logger.debug(attHandler.resp.get().getOut().toString());
		Assert.assertTrue(attHandler.resp.get().getOut().contains("X-EBAY-SOA-CCTEST-HEADER1 BAR"));
		logger.debug("-- testAsyncPushClientStreamingTrue --");
	}
	@Test
	@Ignore("client streaming feature is not supported in opensource.Ignoring tests")
	public void testAsyncPollClientStreamingTrue() throws ServiceException, InterruptedException, ExecutionException {
		logger.debug("-- testPollClientStreamingTrue --");
		SharedAdvertisingUniqueIDServiceV1Consumer client = 
			new SharedAdvertisingUniqueIDServiceV1Consumer(
					"AdvertisingUniqueIDServiceV1Consumer", "ClientStreaming");
		AdvertisingUniqueIDServiceV1Proxy m_proxy = client.getService().getProxy();
		GetTransportHeaders param0 = new GetTransportHeaders();
		param0.getIn().add(0, "X-EBAY-SOA-CCTEST-HEADER1");
		m_proxy.getTransportHeadersAsync(param0);

		List<Response<?>> list = m_proxy.poll(true, false);
		for (Iterator<Response<?>> iter = list.iterator(); iter.hasNext();) {
			Response<?> resp = (Response<?>) iter.next();
			logger.debug("After Poll: " + resp.get().getClass());
		}
		GetTransportHeadersResponse resp = client.getTransportHeaders(param0);
		Assert.assertEquals("X-EBAY-SOA-CCTEST-HEADER1 BAR", resp.getOut().get(0));
		logger.debug("-- testPollClientStreamingTrue --");
	}
	
	@Test
	@Ignore("client streaming feature is not supported in opensource.Ignoring tests")
	public void testClientStreamingTrueChainedScenario() throws ServiceException {
		logger.debug("-- testPushClientStreamingTrue --");
		SharedAdvertisingUniqueIDServiceV1Consumer client = 
			new SharedAdvertisingUniqueIDServiceV1Consumer(
					"AdvertisingUniqueIDServiceV1Consumer", "ESB1");
		ChainedTransportHeaders param0 = new ChainedTransportHeaders();
		param0.getIn().add("X-EBAY-SOA-TEST");
		ChainedTransportHeadersResponse resp = client.chainedTransportHeaders(param0);
		logger.debug("ResponseHeader[0] = " + resp.getOut().get(0));
		Map<String, String> queryParams = new HashMap<String, String>();
		queryParams.put("id","com.ebay.soa.client.http.UniqueIDServiceV2Client.AdvertisingUniqueIDServiceV2.HTTP11");
		queryParams.put("USE_RESPONSE_STREAMING", "true");
		String out = MetricUtil.invokeHttpClient(queryParams, "update");
		logger.debug("Setting client streaming ON remotely returned: " + out);
		Assert.assertTrue("Response.out doesn't contain USE_RESPONSE_STREAMING=\"true\", but it's: " + out,
				out.contains("attribute name=\"USE_RESPONSE_STREAMING\" value=\"true\""));
		
		queryParams.put("id","com.ebay.soa.client.http.UniqueIDServiceV2Client.AdvertisingUniqueIDServiceV2.HTTP11");
		queryParams.put("USE_RESPONSE_STREAMING", "false");
		out = MetricUtil.invokeHttpClient(queryParams, "update");
		Assert.assertTrue("Response.out doesn't contain USE_RESPONSE_STREAMING=\"false\", but it's: " + out, 
				out.contains("attribute name=\"USE_RESPONSE_STREAMING\" value=\"false\""));
		logger.debug("-- testPushClientStreamingTrue --");
	}
	
	@Test
	@Ignore("client streaming feature is not supported in opensource.Ignoring tests")
	public void testAsyncPullClientStreamingTrueWith3GBAttachment() throws Exception {
		logger.debug("-- testAsyncPullClientStreamingTrueWith3GBAttachment --");
		SharedAdvertisingUniqueIDServiceV1Consumer client = 
			new SharedAdvertisingUniqueIDServiceV1Consumer(
					"AdvertisingUniqueIDServiceV1Consumer", "ClientStreaming");
		DataHandler dh = new DataHandler(new FileDataSource(new File(currentDir + "\\3mbAttachment.txt")));
		TestAttachment param0 = new TestAttachment();
		FileAttachmentType value = new FileAttachmentType();
		value.setData(dh);
		value.setFileName(currentDir + "\\3gbAttachment.txt");
		value.setSize(f4.length());
		param0.setIn(value);
		Response<TestAttachmentResponse> resp = client.testAttachmentAsync(param0);
		while (!resp.isDone()) { // take a nap
			Thread.sleep(1000L);
		}
		logger.debug("isDone is true, now process response.");
		
		response = resp.get().getOut();
		assertOnResponseAttachment(f6, MAX_SIZE2, "Client3gbAttachment.txt");
		
		logger.debug("-- testAsyncPullClientStreamingTrueWith3GBAttachment --");
	}
	
	@Test
	@Ignore("client streaming feature is not supported in opensource.Ignoring tests")
	public void testAsyncPullClientStreamingTrueWith3MBAttachment() throws Exception {
		logger.debug("-- testAsyncPullClientStreamingTrueWith3MBAttachment --");
		SharedAdvertisingUniqueIDServiceV1Consumer client = 
			new SharedAdvertisingUniqueIDServiceV1Consumer(
					"AdvertisingUniqueIDServiceV1Consumer", "ClientStreaming");
		DataHandler dh = new DataHandler(new FileDataSource(f1));
		TestAttachment param0 = new TestAttachment();
		FileAttachmentType value = new FileAttachmentType();
		value.setData(dh);
		value.setFileName(currentDir + "\\3mbAttachment.txt");
		value.setSize(MAX_SIZE1);
		param0.setIn(value);
		Response<TestAttachmentResponse> resp = client.testAttachmentAsync(param0);
		while (!resp.isDone()) {
			Thread.sleep(5000);
		}
		logger.debug("isDone is true, now process response.");
		
		response = resp.get().getOut();
		assertOnResponseAttachment(f2, MAX_SIZE1, "Client3mbAttachment.txt");
		
		logger.debug("-- testAsyncPullClientStreamingTrueWith3MBAttachment --");
	}


	@Test
	@Ignore("client streaming feature is not supported in opensource.Ignoring tests")
	public void testAsyncPushClientStreamingTrueWith3GBAttachment() throws Exception {
		logger.debug("-- testAsyncPushClientStreamingTrueWith3GBAttachment --");
		SharedAdvertisingUniqueIDServiceV1Consumer client = 
			new SharedAdvertisingUniqueIDServiceV1Consumer(
					"AdvertisingUniqueIDServiceV1Consumer", "ClientStreaming");
		AttachmentAsyncHandler<TestAttachmentResponse> attHandler = new AttachmentAsyncHandler<TestAttachmentResponse>();
		Future<?> attFutureObj = null;
		DataHandler dh = new DataHandler(new FileDataSource(new File(currentDir + "\\SmallFile.txt")));
		TestAttachment param0 = new TestAttachment();
		FileAttachmentType value = new FileAttachmentType();
		value.setData(dh);
		value.setFileName(currentDir + "\\3gbAttachment.txt");
		value.setSize(f4.length());
		param0.setIn(value);
		//			client.getService().setExecutor(null);
		attFutureObj = client.testAttachmentAsync(param0, attHandler);
		while (!attFutureObj.isDone()) {
			Thread.sleep(1000L);
		}
		response = attHandler.resp.get().getOut();
		assertOnResponseAttachment(f6, MAX_SIZE2, "Client3gbAttachment.txt");
		
		logger.debug("-- testAsyncPushClientStreamingTrueWith3GBAttachment --");
	}
	
	@Test
	@Ignore("client streaming feature is not supported in opensource.Ignoring tests")
	public void testAsyncPushClientStreamingTrueWith3MBAttachment() throws ServiceException, InterruptedException, ExecutionException, IOException {
		logger.debug("-- testAsyncPushClientStreamingTrueWith3MBAttachment --");
		SharedAdvertisingUniqueIDServiceV1Consumer client = new SharedAdvertisingUniqueIDServiceV1Consumer("AdvertisingUniqueIDServiceV1Consumer", "ClientStreaming");
		AttachmentAsyncHandler<TestAttachmentResponse> attHandler = new AttachmentAsyncHandler<TestAttachmentResponse>();
		Future<?> attFutureObj = null;
		DataHandler dh = new DataHandler(new FileDataSource(new File(currentDir + "\\3mbAttachment.txt")));
		TestAttachment param0 = new TestAttachment();
		FileAttachmentType value = new FileAttachmentType();
		value.setData(dh);
		value.setFileName(currentDir + "\\3mbAttachment.txt");
		value.setSize(f1.length());
		param0.setIn(value);
		try {
			attFutureObj = client.testAttachmentAsync(param0, attHandler);
		} catch (WebServiceException e1) {
			assertNull(e1);
		}
		while (!attFutureObj.isDone()) {
			try {
				Thread.sleep(DEFAULT_SLEEP_TIME);
			} catch (InterruptedException e) {
			}
		}
		response = attHandler.resp.get().getOut();
		out = new FileOutputStream(new File(response.getFileName()));
		assertOnResponseAttachment(f2, MAX_SIZE1, "Client3mbAttachment.txt");
		dh = attHandler.resp.get().getOut().getData();
		if (!writeData(dh, MAX_SIZE1)) Assert.assertFalse(true); 
		Assert.assertTrue(attHandler.resp.get().getOut().getFileName().contains("Client3mbAttachment.txt"));
		Assert.assertEquals(attHandler.resp.get().getOut().getSize().longValue(), MAX_SIZE1);
		Assert.assertTrue(f2.exists());
		Assert.assertEquals(f2.length(), MAX_SIZE1);
		logger.debug("-- testAsyncPushClientStreamingTrueWith3MBAttachment --");
	}


	@Test
	@Ignore("client streaming feature is not supported in opensource.Ignoring tests")
	public void testDefaultCaseWith3MBAttachmentRemote() throws Exception {
		logger.debug("-- testDefaultCaseWith3MBAttachmentRemote --");
		MAX_SIZE1 = f1.length();
		SharedAdvertisingUniqueIDServiceV1Consumer client = 
			new SharedAdvertisingUniqueIDServiceV1Consumer(
					"AdvertisingUniqueIDServiceV1Consumer", "ClientStreaming");
		DataHandler dh = new DataHandler(new FileDataSource(f1));
		TestAttachment param0 = new TestAttachment();
		FileAttachmentType value = new FileAttachmentType();
		value.setData(dh);
		value.setFileName(currentDir + "\\3mbAttachment.txt");
		value.setSize(MAX_SIZE1);
		param0.setIn(value);
		response = client.testAttachment(param0).getOut();
		assertOnResponseAttachment(f2, MAX_SIZE1, "Client3mbAttachment.txt");
		
		logger.debug("-- testDefaultCaseWith3MBAttachmentRemote --");

	}
	@Test
	@Ignore("client streaming feature is not supported in opensource.Ignoring tests")
	public void testDefaultCaseWith3MBAttachmentLocal() throws Exception {
		logger.debug("-- testDefaultCaseWith3MBAttachmentLocal --");
		MAX_SIZE1 = f1.length();
		SharedAdvertisingUniqueIDServiceV1Consumer client = 
			new SharedAdvertisingUniqueIDServiceV1Consumer(
					"AdvertisingUniqueIDServiceV1Consumer", "local");
		DataHandler dh = new DataHandler(new FileDataSource(f1));
		TestAttachment param0 = new TestAttachment();
		FileAttachmentType value = new FileAttachmentType();
		value.setData(dh);
		value.setFileName(currentDir + "\\3mbAttachment.txt");
		value.setSize(MAX_SIZE1);
		param0.setIn(value);
		response = client.testAttachment(param0).getOut();
		assertOnResponseAttachment(f2, MAX_SIZE1, "Client3mbAttachment.txt");
		
		logger.debug("-- testDefaultCaseWith3MBAttachmentLocal --");

	}
	@Test
	@Ignore("client streaming feature is not supported in opensource.Ignoring tests")
	public void testDefaultCaseWith3GBAttachmentLocal() throws Exception {
		logger.debug("-- testDefaultCaseWith3GBAttachmentLocal --");
		
		SharedAdvertisingUniqueIDServiceV1Consumer client = 
			new SharedAdvertisingUniqueIDServiceV1Consumer(
					"AdvertisingUniqueIDServiceV1Consumer", "local");
		client.getServiceInvokerOptions().getTransportOptions().setSkipSerialization(Boolean.TRUE);
		DataHandler dh = new DataHandler(new FileDataSource(f4));
		TestAttachment param0 = new TestAttachment();
		FileAttachmentType value = new FileAttachmentType();
		value.setData(dh);
		value.setFileName(currentDir + "\\3gbAttachment.txt");
		value.setSize(MAX_SIZE2);
		param0.setIn(value);
		response = client.testAttachment(param0).getOut();
		assertOnResponseAttachment(f6, MAX_SIZE2, "Client3gbAttachment.txt");
 
		logger.debug("-- testDefaultCaseWith3GBAttachmentLocal --");
	}
	
	@Test
	@Ignore("client streaming feature is not supported in opensource.Ignoring tests")
	public void testDefaultCaseWith3GBAttachmentRemote() throws Exception {
		logger.debug("-- testDefaultCaseWith3GBAttachmentRemote --");
		MAX_SIZE2 = f4.length();
		SharedAdvertisingUniqueIDServiceV1Consumer client = 
			new SharedAdvertisingUniqueIDServiceV1Consumer(
				"AdvertisingUniqueIDServiceV1Consumer", "ClientStreaming");
		DataHandler dh = new DataHandler(new FileDataSource(f4));
		TestAttachment param0 = new TestAttachment();
		FileAttachmentType value = new FileAttachmentType();
		value.setData(dh);
		value.setFileName(currentDir + "\\3gbAttachment.txt");
		value.setSize(MAX_SIZE2);
		param0.setIn(value);
		response = client.testAttachment(param0).getOut();
		assertOnResponseAttachment(f6, MAX_SIZE2, "Client3gbAttachment.txt");
		
		logger.debug("-- testDefaultCaseWith3GBAttachmentRemote --");
	}
	
	private void assertOnResponseAttachment(
			File serverFile, long size, String clientFileName)
	throws FileNotFoundException, IOException {
		// assert on server file
		logger.debug("Server file size = " + serverFile.length());
		Assert.assertEquals("Unexpected output file size", serverFile.length(), size);
		Assert.assertTrue("Output file " + serverFile.getAbsolutePath() + " doesn't exist", serverFile.exists());
		
		// assert on Response
		Assert.assertTrue("Unexpected file name: " + response.getFileName(), 
				response.getFileName().contains(clientFileName));
		Assert.assertEquals("Unexpected response attachment size", size, response.getSize().longValue());

		// write response attachment to output file
		File outFile = new File(response.getFileName());
		out = new FileOutputStream(outFile);
		DataHandler dh = response.getData();
		if (!writeData(dh, size)) Assert.fail("Did not write the received data");
		
		// assert on output file
		Assert.assertEquals("Unexpected size for output file " + outFile.getAbsolutePath(), 
				size, outFile.length());
	}

	public boolean writeData(DataHandler dh, long size) throws IOException{
		byte[] dataBuf = new byte[CHUNK_SIZE];
		InputStream in = dh.getInputStream();
		br = new BufferedInputStream(in);
		try {
			int numBytes;
			while ((numBytes = br.read(dataBuf)) != -1) {
				sizeCounter += numBytes;
				if (sizeCounter <= size) {
					out.write(dataBuf, 0, numBytes);
				}
			}
		} finally {
			br.close();
		}
		return true;
	}
	
	public static void createFileForTest(String size, File file) throws IOException {
			RandomAccessFile out = new RandomAccessFile(file, "rw");
			FileChannel fc = out.getChannel();
			long length = 3145728;
			if (size.contentEquals("gb")) {
				length = 1024 * 1024 * 1024;
				int j  = 0;
				long offset = 0;
				while (j++ < 3) {
	//				logger.debug("offset - " + offset);
	//				logger.debug("length - " + length);
	//				logger.debug("j - " + j);
					MappedByteBuffer MappByteBuff = fc.map(FileChannel.MapMode.READ_WRITE, offset, length);
					for (int i = 0; i < length; i++)
					{
						MappByteBuff.put((byte) 'B');
					}
					offset = offset + length;
	//				logger.debug(fc.size());
				}
			} else { 	
				MappedByteBuffer MappByteBuff = fc.map(FileChannel.MapMode.READ_WRITE, 0, length);
				for (int i = 0; i < length; i++)
				{
					MappByteBuff.put((byte) 'B');
				}
			}
			fc.close();
		}
	@Ignore
	public void create3GBFile() throws Exception {
		String threeMegaJunk = FileUtils.readFile(f1.getAbsolutePath(), "UTF-8");
		FileOutputStream fos = new FileOutputStream(f4);
		try {
			for (int i = 0; i < 1024; i++) {
				byte[] threeMegaBytes = threeMegaJunk.getBytes("UTF-8");
				fos.write(threeMegaBytes);
			}
		} finally {
			try {
				fos.close();
			} catch (IOException e) {
				new RuntimeException("Couldn't close output stream " + f4.getAbsolutePath(), e).printStackTrace();
			}
		}
	}
	
	protected class AttachmentAsyncHandler<T> implements AsyncHandler<T> {
			private Response<T> resp;
	
			private boolean isDone = false;
	
			public void handleResponse(Response<T> resp) {
				this.resp = resp;
				String currThreadNm = Thread.currentThread().getName();
				logger.debug("AttachmentAsyncHandler:handleResponse:Executing thread " + currThreadNm);
				try {
	//				logger.debug("AttachmentAsyncHandler:handleResponse:");
					logger.debug(this.resp.get().toString());
	//				TestAttachmentResponse response = (TestAttachmentResponse)this.resp.get();
	//				logger.debug(response.getOut().getFileName());
	//				logger.debug(response.getOut().getSize());
				} catch (InterruptedException e) {
				} catch (ExecutionException e) {
				} finally {
					isDone = true;
				}
			}
			public Response<T> getReturn() {
				return resp;
			}
	
			public boolean isDone() {
				return isDone;
			}
	
		}

	
}
