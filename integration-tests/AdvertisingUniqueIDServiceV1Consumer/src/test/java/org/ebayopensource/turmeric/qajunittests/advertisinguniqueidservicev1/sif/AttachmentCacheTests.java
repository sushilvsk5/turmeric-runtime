package org.ebayopensource.turmeric.qajunittests.advertisinguniqueidservicev1.sif;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import javax.activation.DataHandler;
import javax.activation.FileDataSource;

import junit.framework.Assert;

import org.ebayopensource.turmeric.advertising.v1.services.FileAttachmentType;
import org.ebayopensource.turmeric.advertising.v1.services.TestAttachment;
import org.ebayopensource.turmeric.advertisinguniqueidservicev1.gen.SharedAdvertisingUniqueIDServiceV1Consumer;
import org.ebayopensource.turmeric.runtime.common.types.SOAConstants;
import org.ebayopensource.turmeric.runtime.tests.common.jetty.AbstractWithServerTest;
import org.ebayopensource.turmeric.runtime.tests.common.util.QEFileUtils;
import org.junit.After;
import org.junit.BeforeClass;
import org.junit.Test;

public class AttachmentCacheTests extends AbstractWithServerTest {
	File f1, fClient, fServer;
	static String currentDir;
	long MAX_SIZE;
	FileAttachmentType response = null;
	FileOutputStream out = null;
	static File f;

	@BeforeClass
	public static void setup() throws IOException {
		currentDir = System.getProperty("user.dir");
		f = new File(currentDir + File.separator + "attachmentcache");
	
	/*	if (QEFileUtils.deleteDir(f)){
			System.out.println("done* in Setup*************");
		}else{
			System.out.println("not deleting in before class");
		}
	 */	
			
	}

	/*
	 * Existing usecase default size = 2kb
	 */
	@Test
	public void testCacheONDefaultLimit1KbFile() throws Exception {
		System.out.println(" ** testCacheONDefaultLimit1KbFile ** ");
		f1 = new File(currentDir + File.separator + "1kbAttachment.txt");
		fClient = new File(currentDir + File.separator + "Client1kbAttachment.txt");
		fServer = new File(currentDir + File.separator + "Server1kbAttachment.txt");
		if (!f1.exists())
			QEFileUtils.createFileForTest(Integer.valueOf(1024), f1);
		MAX_SIZE = f1.length();
		SharedAdvertisingUniqueIDServiceV1Consumer client = new SharedAdvertisingUniqueIDServiceV1Consumer(
				"AdvertisingUniqueIDServiceV1Consumer", "attachmentcache4");
		client.setServiceLocation("http://" + serverUri.getHost() + ":"
				+ serverUri.getPort()
				+ "/services/advertise/UniqueIDService/v1");
		DataHandler dh = new DataHandler(new FileDataSource(f1));
		TestAttachment param0 = new TestAttachment();
		FileAttachmentType value = new FileAttachmentType();
		value.setData(dh);
		value.setFilePath(currentDir + File.separator + "");
		value.setFileName("1kbAttachment.txt");
		value.setSize(MAX_SIZE);
		param0.setIn(value);
		response = client.testAttachment(param0).getOut();
		assertOnResponse(response, 0, MAX_SIZE);

		System.out.println("-- testCacheONDefaultLimit1KbFile --");
	}

	@After
	public void cleanUp() {
		if (QEFileUtils.deleteDir(f))
		{
			System.out.println("done");
		}
		else
		{
			System.out.println("not done");
		}
		
		fClient.delete();
		fServer.delete();
	}

	

	@Test
	public void testCacheON0kbLimit1KbFile() throws Exception {
		System.out.println(" ** testCacheON0kbLimit1KbFile ** ");
		f1 = new File(currentDir + File.separator + "1kbAttachment.txt");
		fClient = new File(currentDir + File.separator + "Client1kbAttachment.txt");
		fServer = new File(currentDir + File.separator + "Server1kbAttachment.txt");
		if (!f1.exists())
			QEFileUtils.createFileForTest(Integer.valueOf(1024), f1);
		MAX_SIZE = f1.length();
		SharedAdvertisingUniqueIDServiceV1Consumer client = new SharedAdvertisingUniqueIDServiceV1Consumer(
				"AdvertisingUniqueIDServiceV1Consumer", "attachmentcache3");
		client.setServiceLocation("http://" + serverUri.getHost() + ":"
				+ serverUri.getPort()
				+ "/services/advertise/UniqueIDService/v1");
		DataHandler dh = new DataHandler(new FileDataSource(f1));
		TestAttachment param0 = new TestAttachment();
		FileAttachmentType value = new FileAttachmentType();
		value.setData(dh);
		value.setFilePath(currentDir + File.separator + "");
		value.setFileName("1kbAttachment.txt");
		value.setSize(MAX_SIZE);
		param0.setIn(value);
		response = client.testAttachment(param0).getOut();
		Assert.assertFalse(f.exists());
		System.out.println(" ** testCacheON0kbLimit1KbFile ** ");
	}

	@Test
	public void testCacheOFFDefaultLimit1KbFile() throws Exception {
		System.out.println(" ** testCacheOFFDefaultLimit1KbFile ** ");
		f1 = new File(currentDir + File.separator + "1kbAttachment.txt");
		fClient = new File(currentDir + File.separator + "Client1kbAttachment.txt");
		fServer = new File(currentDir + File.separator + "Server1kbAttachment.txt");

		if (f1.exists())
			QEFileUtils.createFileForTest(Integer.valueOf(1024), f1);
		MAX_SIZE = f1.length();
		SharedAdvertisingUniqueIDServiceV1Consumer client = new SharedAdvertisingUniqueIDServiceV1Consumer(
				"AdvertisingUniqueIDServiceV1Consumer", "attachmentcache2");
		client.setServiceLocation("http://" + serverUri.getHost() + ":"
				+ serverUri.getPort()
				+ "/services/advertise/UniqueIDService/v1");
		DataHandler dh = new DataHandler(new FileDataSource(f1));
		TestAttachment param0 = new TestAttachment();
		FileAttachmentType value = new FileAttachmentType();
		value.setData(dh);
		value.setFilePath(currentDir + File.separator + "");
		value.setFileName("1kbAttachment.txt");
		value.setSize(MAX_SIZE);
		param0.setIn(value);
		response = client.testAttachment(param0).getOut();
		assertOnResponse(response, 0, MAX_SIZE);
		System.out.println(" ** testCacheOFFDefaultLimit1KbFile ** ");
	}

	@Test
	public void testCacheOFFDefaultLimit2KbFile() throws Exception {
		System.out.println(" ** testCacheOFFDefaultLimit2KbFile ** ");
		f1 = new File(currentDir + File.separator + "2kbAttachment.txt");
		fClient = new File(currentDir + File.separator + "Client2kbAttachment.txt");
		fServer = new File(currentDir + File.separator + "Server2kbAttachment.txt");
		if (!f1.exists())
			QEFileUtils.createFileForTest(Integer.valueOf(2 * 1024), f1);
		MAX_SIZE = f1.length();
		SharedAdvertisingUniqueIDServiceV1Consumer client = new SharedAdvertisingUniqueIDServiceV1Consumer(
				"AdvertisingUniqueIDServiceV1Consumer", "attachmentcache2");
		client.setServiceLocation("http://" + serverUri.getHost() + ":"
				+ serverUri.getPort()
				+ "/services/advertise/UniqueIDService/v1");
		DataHandler dh = new DataHandler(new FileDataSource(f1));
		TestAttachment param0 = new TestAttachment();
		FileAttachmentType value = new FileAttachmentType();
		value.setData(dh);
		value.setFilePath(currentDir + File.separator + "");
		value.setFileName("2kbAttachment.txt");
		value.setSize(MAX_SIZE);
		param0.setIn(value);
		response = client.testAttachment(param0).getOut();
		assertOnResponse(response, 0, MAX_SIZE);
		System.out.println(" ** testCacheOFFDefaultLimit2KbFile ** ");
	}

	@Test
	public void testCacheOFFDefaultLimit3KbFile() throws Exception {
		System.out.println(" ** testCacheOFFDefaultLimit3KbFile ** ");
		f1 = new File(currentDir + File.separator + "3kbAttachment.txt");
		fClient = new File(currentDir + File.separator + "Client3kbAttachment.txt");
		fServer = new File(currentDir + File.separator + "Server3kbAttachment.txt");
		if (!f1.exists())
			QEFileUtils.createFileForTest(Integer.valueOf(3 * 1024), f1);
		MAX_SIZE = f1.length();
		SharedAdvertisingUniqueIDServiceV1Consumer client = new SharedAdvertisingUniqueIDServiceV1Consumer(
				"AdvertisingUniqueIDServiceV1Consumer", "attachmentcache2");
		client.setServiceLocation("http://" + serverUri.getHost() + ":"
				+ serverUri.getPort()
				+ "/services/advertise/UniqueIDService/v1");
		DataHandler dh = new DataHandler(new FileDataSource(f1));
		TestAttachment param0 = new TestAttachment();
		FileAttachmentType value = new FileAttachmentType();
		value.setData(dh);
		value.setFilePath(currentDir + File.separator + "");
		value.setFileName("3kbAttachment.txt");
		value.setSize(MAX_SIZE);
		param0.setIn(value);
		response = client.testAttachment(param0).getOut();
		assertOnResponse(response, 0, MAX_SIZE);
		System.out.println(" ** testCacheOFFDefaultLimit3KbFile ** ");
	}

	@Test
	public void testCacheOFF100bLimit1KbFile() throws Exception {
		System.out.println(" ** testCacheOFFDefaultLimit1KbFile ** ");
		f1 = new File(currentDir + File.separator + "1kbAttachment.txt");
		fClient = new File(currentDir + File.separator + "Client1kbAttachment.txt");
		fServer = new File(currentDir + File.separator + "Server1kbAttachment.txt");

		if (f1.exists())
			QEFileUtils.createFileForTest(Integer.valueOf(1024), f1);
		MAX_SIZE = f1.length();
		SharedAdvertisingUniqueIDServiceV1Consumer client = new SharedAdvertisingUniqueIDServiceV1Consumer(
				"AdvertisingUniqueIDServiceV1Consumer", "attachmentcache5");
		client.setServiceLocation("http://" + serverUri.getHost() + ":"
				+ serverUri.getPort()
				+ "/services/advertise/UniqueIDService/v1");
		DataHandler dh = new DataHandler(new FileDataSource(f1));
		TestAttachment param0 = new TestAttachment();
		FileAttachmentType value = new FileAttachmentType();
		value.setData(dh);
		value.setFilePath(currentDir + File.separator + "");
		value.setFileName("1kbAttachment.txt");
		value.setSize(MAX_SIZE);
		param0.setIn(value);
		response = client.testAttachment(param0).getOut();
		assertOnResponse(response, 0, MAX_SIZE);
		System.out.println(" ** testCacheOFFDefaultLimit1KbFile ** ");
	}

	@Test
	public void testCacheOFF100bLimit2KbFile() throws Exception {
		System.out.println(" ** testCacheOFFDefaultLimit2KbFile ** ");
		f1 = new File(currentDir + File.separator + "2kbAttachment.txt");
		fClient = new File(currentDir + File.separator + "Client2kbAttachment.txt");
		fServer = new File(currentDir + "\\Server2kbAttachment.txt");
		if (!f1.exists())
			QEFileUtils.createFileForTest(Integer.valueOf(2 * 1024), f1);
		MAX_SIZE = f1.length();
		SharedAdvertisingUniqueIDServiceV1Consumer client = new SharedAdvertisingUniqueIDServiceV1Consumer(
				"AdvertisingUniqueIDServiceV1Consumer", "attachmentcache5");
		client.setServiceLocation("http://" + serverUri.getHost() + ":"
				+ serverUri.getPort()
				+ "/services/advertise/UniqueIDService/v1");
		DataHandler dh = new DataHandler(new FileDataSource(f1));
		TestAttachment param0 = new TestAttachment();
		FileAttachmentType value = new FileAttachmentType();
		value.setData(dh);
		value.setFilePath(currentDir + File.separator + "");
		value.setFileName("2kbAttachment.txt");
		value.setSize(MAX_SIZE);
		param0.setIn(value);
		response = client.testAttachment(param0).getOut();
		assertOnResponse(response, 0, MAX_SIZE);
		System.out.println(" ** testCacheOFFDefaultLimit2KbFile ** ");
	}

	private void assertOnResponse(FileAttachmentType response, int cacheSize,
			long size) throws FileNotFoundException, IOException {
		// assert on server file
		String fileName = response.getFileName();
		String filePath = response.getFilePath();
		long fileSize = response.getSize().longValue();
		out = new FileOutputStream(new File(filePath + fileName));
		DataHandler dh = response.getData();
		if (!QEFileUtils.writeData(dh, MAX_SIZE, out))
			Assert.fail("File not written");
		// Assert on the temp location
		
		if (cacheSize == 1){
			Assert.assertEquals(cacheSize, f.list().length);
		}	
		else{
			Assert.assertFalse(f.exists());
		}
	}
	
	@Test
	public void testCacheONDefaultLimit2KbFile() throws Exception {
		File f30 = new File(currentDir + File.separator + "attachmentcache");
		f30.mkdir();
		System.out.println("-- testCacheONDefaultLimit2KbFile --");
		response = null;
		f1 = new File(f30.getCanonicalFile() + File.separator + "2kbAttachment.txt");
		fClient = new File(f30.getCanonicalFile() + File.separator + "Client2kbAttachment.txt");
		fServer = new File(f30.getCanonicalFile() + File.separator + "Server2kbAttachment.txt");
		if (!f1.exists())
			QEFileUtils.createFileForTest(Integer.valueOf(2048), f1);

		MAX_SIZE = f1.length();

		SharedAdvertisingUniqueIDServiceV1Consumer client = new SharedAdvertisingUniqueIDServiceV1Consumer(
				"AdvertisingUniqueIDServiceV1Consumer", "attachmentcache4");
		client.setServiceLocation("http://" + serverUri.getHost() + ":"
				+ serverUri.getPort()
				+ "/services/advertise/UniqueIDService/v1");
		DataHandler dh = new DataHandler(new FileDataSource(f1));
		TestAttachment param0 = new TestAttachment();
		FileAttachmentType value = new FileAttachmentType();
		value.setData(dh);
		value.setFilePath(currentDir + File.separator + "");
		value.setFileName("2kbAttachment.txt");
		value.setSize(MAX_SIZE);
		param0.setIn(value);
		response = client.testAttachment(param0).getOut();
		assertOnResponse(response, 1, MAX_SIZE);
		System.out.println("-- testCacheONDefaultLimit2KbFile --");
		delete(f30);
	}
	@Test
	public void testCacheONDefaultLimit3KbFile() throws Exception {
        File f20 = new File(currentDir + File.separator + "attachmentcache" + File.separator);
        if(f20.exists()){
              QEFileUtils.deleteDir(f20);
        }           
        f20.mkdir();
        response = null;
        System.out.println("-- testCacheONDefaultLimit3KbFile --");
        f1 = new File(f20.getCanonicalFile() + File.separator + "3kbAttachment.txt");       
        fClient = new File(f20.getCanonicalFile() + File.separator + "Client3kbAttachment.txt");
        fServer = new File(f20.getCanonicalFile() + File.separator + "Server3kbAttachment.txt");
        if (!f1.exists()) {
              QEFileUtils.createFileForTest(Integer.valueOf(3072), f1);
        }
        System.out.println("third line of trace "+f1.getAbsolutePath());
        MAX_SIZE = f1.length();
        SharedAdvertisingUniqueIDServiceV1Consumer client = new SharedAdvertisingUniqueIDServiceV1Consumer(
                    "AdvertisingUniqueIDServiceV1Consumer", "attachmentcache4");
        client.setServiceLocation("http://" + serverUri.getHost() + ":"
                    + serverUri.getPort()
                    + "/services/advertise/UniqueIDService/v1");
        DataHandler dh = new DataHandler(new FileDataSource(f1));
        TestAttachment param0 = new TestAttachment();
        FileAttachmentType value = new FileAttachmentType();
        value.setData(dh);
        value.setFilePath(currentDir + File.separator + "");
        value.setFileName("3kbAttachment.txt");
        value.setSize(MAX_SIZE);
        param0.setIn(value);
        response = client.testAttachment(param0).getOut();
        
        assertOnResponse(response, 1, MAX_SIZE,f20);
        
        if (f20.isDirectory()) {
                String[] files = f20.list();
              boolean success1 = f20.delete();
        }
        // Assert on the temp location
        System.out.println("-- testCacheONDefaultLimit3KbFile --");
        
  }


	@Test
	public void testCacheON100bLimit1kbFile() throws Exception {
		File f10 = new File(currentDir + File.separator + "attachmentcache");
		f10.mkdir();
		response = null;
		System.out.println("-- testCacheON4KbLimit1KbFile --");

		f1 = new File(f10.getCanonicalFile() + File.separator + "1kbAttachment.txt");
		fClient = new File(f10.getCanonicalFile() + File.separator + "Client1kbAttachment.txt");
		fServer = new File(f10.getCanonicalFile() + File.separator + "Server1kbAttachment.txt");
		if (!f1.exists())
			QEFileUtils.createFileForTest(Integer.valueOf(1024), f1);
		MAX_SIZE = f1.length();
		SharedAdvertisingUniqueIDServiceV1Consumer client = new SharedAdvertisingUniqueIDServiceV1Consumer(
				"AdvertisingUniqueIDServiceV1Consumer", "attachmentcache1");
		client.setServiceLocation("http://" + serverUri.getHost() + ":"
				+ serverUri.getPort()
				+ "/services/advertise/UniqueIDService/v1");
		DataHandler dh = new DataHandler(new FileDataSource(f1));
		TestAttachment param0 = new TestAttachment();
		FileAttachmentType value = new FileAttachmentType();
		value.setData(dh);
		value.setFilePath(currentDir + File.separator + "");
		value.setFileName("1kbAttachment.txt");
		value.setSize(MAX_SIZE);
		param0.setIn(value);
		response = client.testAttachment(param0).getOut();
		assertOnResponse(response, 1, MAX_SIZE);
		System.out.println("-- testCacheON4KbLimit1KbFile --");
		if (f10.isDirectory()) {
		      String[] files = f10.list();
		      boolean success = f10.delete();
		}
		System.out.println("-- testCacheON4KbLimit1KbFile --");
	}
	
	
	private void assertOnResponse(FileAttachmentType response, int cacheSize,
			long size,File f123) throws FileNotFoundException, IOException {
		// assert on server file
		String fileName = response.getFileName();
		String filePath = response.getFilePath();
		long fileSize = response.getSize().longValue();
		out = new FileOutputStream(new File(filePath + fileName));
		DataHandler dh = response.getData();
		if (!QEFileUtils.writeData(dh, MAX_SIZE, out))
			Assert.fail("File not written");
		// Assert on the temp location
		
		if (cacheSize == 1){
			Assert.assertEquals(cacheSize, f123.list().length);
		}	
		else{
			Assert.assertFalse(f123.exists());
		}
	}
	
	
	
	public static void delete(File file)
	throws IOException{

	if(file.isDirectory()){

		//directory is empty, then delete it
		if(file.list().length==0){

		   file.delete();
		   System.out.println("Directory is deleted : " 
                                             + file.getAbsolutePath());

		}else{

		   //list all the directory contents
    	   String files[] = file.list();

    	   for (String temp : files) {
    	      //construct the file structure
    	      File fileDelete = new File(file, temp);

    	      //recursive delete
    	     delete(fileDelete);
    	   }

    	   //check the directory again, if empty then delete it
    	   if(file.list().length==0){
       	     file.delete();
    	     System.out.println("Directory is deleted : " 
                                              + file.getAbsolutePath());
    	   }
		}

	}else{
		//if file, then delete it
		file.delete();
		System.out.println("File is deleted : " + file.getAbsolutePath());
	}
}

}
