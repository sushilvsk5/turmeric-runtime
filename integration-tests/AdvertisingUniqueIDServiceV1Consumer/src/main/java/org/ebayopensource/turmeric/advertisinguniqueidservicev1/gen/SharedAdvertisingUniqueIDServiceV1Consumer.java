/*******************************************************************************
 * Copyright (c) 2006-2010 eBay Inc. All Rights Reserved.
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 *******************************************************************************/

package org.ebayopensource.turmeric.advertisinguniqueidservicev1.gen;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;
import java.util.concurrent.Future;

import javax.xml.ws.AsyncHandler;
import javax.xml.ws.Response;

import org.ebayopensource.turmeric.advertising.v1.services.ChainedTransportHeaders;
import org.ebayopensource.turmeric.advertising.v1.services.ChainedTransportHeadersResponse;
import org.ebayopensource.turmeric.advertising.v1.services.EchoMessageRequest;
import org.ebayopensource.turmeric.advertising.v1.services.EchoMessageResponse;
import org.ebayopensource.turmeric.advertising.v1.services.GetGenericClientInfoRequest;
import org.ebayopensource.turmeric.advertising.v1.services.GetGenericClientInfoResponse;
import org.ebayopensource.turmeric.advertising.v1.services.GetItemRequest;
import org.ebayopensource.turmeric.advertising.v1.services.GetItemResponse;
import org.ebayopensource.turmeric.advertising.v1.services.GetMessagesForTheDayRequest;
import org.ebayopensource.turmeric.advertising.v1.services.GetMessagesForTheDayResponse;
import org.ebayopensource.turmeric.advertising.v1.services.GetRequestIDResponse;
import org.ebayopensource.turmeric.advertising.v1.services.GetTransportHeaders;
import org.ebayopensource.turmeric.advertising.v1.services.GetTransportHeadersResponse;
import org.ebayopensource.turmeric.advertising.v1.services.GetVersion;
import org.ebayopensource.turmeric.advertising.v1.services.GetVersionResponse;
import org.ebayopensource.turmeric.advertising.v1.services.TestAttachment;
import org.ebayopensource.turmeric.advertising.v1.services.TestAttachmentResponse;
import org.ebayopensource.turmeric.advertising.v1.services.TestEnhancedRest;
import org.ebayopensource.turmeric.advertising.v1.services.TestEnhancedRestResponse;
import org.ebayopensource.turmeric.advertising.v1.services.TestPrimitiveTypesRequest;
import org.ebayopensource.turmeric.advertising.v1.services.TestPrimitiveTypesResponse;
import org.ebayopensource.turmeric.runtime.common.exceptions.ServiceException;
import org.ebayopensource.turmeric.runtime.common.exceptions.ServiceRuntimeException;
import org.ebayopensource.turmeric.runtime.common.registration.ClassLoaderRegistry;
import org.ebayopensource.turmeric.runtime.common.types.Cookie;
import org.ebayopensource.turmeric.runtime.common.types.SOAHeaders;
import org.ebayopensource.turmeric.runtime.sif.service.Service;
import org.ebayopensource.turmeric.runtime.sif.service.ServiceFactory;
import org.ebayopensource.turmeric.runtime.sif.service.ServiceInvokerOptions;
import org.ebayopensource.turmeric.runtime.tests.AsyncAdvertisingUniqueIDServiceV1;


/**
 * Note : Generated file, any changes will be lost upon regeneration.
 * This class is not thread safe
 * 
 */
public class SharedAdvertisingUniqueIDServiceV1Consumer
    implements AsyncAdvertisingUniqueIDServiceV1
{
	private boolean m_useDefaultClientConfig;
    private URL m_serviceLocation = null;
    private final static String SVC_ADMIN_NAME = "AdvertisingUniqueIDServiceV1";
    private String m_clientName;
    private String m_environment = "production";
    private AsyncAdvertisingUniqueIDServiceV1 m_proxy = null;
    private String m_authToken = null;
    private Cookie[] m_cookies;
    private Service m_service = null;
    private final static String HTTP_NON_SECURE = "http";
    private String m_hostName;
    private String m_urlPath;
    private int m_port;
    private String m_protocolScheme;

    /**
     * This constructor should be used, when a ClientConfig.xml is located in the 
     * "client" bundle, so that a ClassLoader of this Shared Consumer can be used.
     * 
     * @param clientName
     * @throws ServiceException
     * 
     */
    public SharedAdvertisingUniqueIDServiceV1Consumer(String clientName)
        throws ServiceException
    {
        this(clientName, null);
    }

    /**
     * This constructor should be used, when a ClientConfig.xml is located in the 
     * "client" bundle, so that a ClassLoader of this Shared Consumer can be used.
     * 
     * @param clientName
     * @param environment
     * @throws ServiceException
     * 
     */
    public SharedAdvertisingUniqueIDServiceV1Consumer(String clientName, String environment)
        throws ServiceException
    {
        this(clientName, environment, null, false);
    }

    /**
     * This constructor should be used, when a ClientConfig.xml is located 
     * in some application bundle. Shared Consumer then will call ClassLoaderRegistry 
     * to register a ClassLoader of an application bundle.
     * 
     * @param clientName
     * @param caller
     * @param useDefaultClientConfig
     * @throws ServiceException
     * 
     */
    public SharedAdvertisingUniqueIDServiceV1Consumer(String clientName, Class caller, boolean useDefaultClientConfig)
        throws ServiceException
    {
        this(clientName, null, caller, useDefaultClientConfig);
    }

    /**
     * This constructor should be used, when a ClientConfig.xml is located 
     * in some application bundle. Shared Consumer then will call ClassLoaderRegistry 
     * to register a ClassLoader of an application bundle.
     * 
     * @param clientName
     * @param environment
     * @param caller
     * @param useDefaultClientConfig
     * @throws ServiceException
     * 
     */
    public SharedAdvertisingUniqueIDServiceV1Consumer(String clientName, String environment, Class caller, boolean useDefaultClientConfig)
        throws ServiceException
    {
        if (clientName == null) {
            throw new ServiceException("clientName can not be null");
        }
        m_clientName = clientName;
        if (environment!= null) {
            m_environment = environment;
        }
        m_environment = getDefaultEnvironmentName();
        m_useDefaultClientConfig = useDefaultClientConfig;
        ClassLoaderRegistry.instanceOf().registerServiceClient(m_clientName, m_environment, SVC_ADMIN_NAME, (SharedAdvertisingUniqueIDServiceV1Consumer.class), caller, m_useDefaultClientConfig);
        URL targetLocation = getService().getServiceLocation();
        setTargetLocationComponents(targetLocation);
    }

   

    /**
     * Use this method to initialize ConsumerApp after creating a Consumer instance
     * 
     */
    public void init()
        throws ServiceException
    {
        getService();
    }

    public void setServiceLocation(String serviceLocation)
    throws MalformedURLException
{
    URL serviceLocationUrl = new URL(serviceLocation);
    setTargetLocationComponents(serviceLocationUrl);
    if (m_service!= null) {
        m_service.setServiceLocation(serviceLocationUrl);
    }
}

    private void setUserProvidedSecurityCredentials(Service service) {
        if (m_authToken!= null) {
            service.setSessionTransportHeader(SOAHeaders.AUTH_TOKEN, m_authToken);
        }
        if (m_cookies!= null) {
            for (int i = 0; (i<m_cookies.length); i ++) {
                service.setCookie(m_cookies[i]);
            }
        }
    }

    /**
     * Use this method to set User Credentials (Token) 
     * 
     */
    protected void setAuthToken(String authToken) {
        m_authToken = authToken;
    }

    /**
     * Use this method to set User Credentials (Cookie)
     * 
     */
    protected void setCookies(Cookie[] cookies) {
        m_cookies = cookies;
    }

    /**
     * Use this method to get the Invoker Options on the Service and set them to user-preferences
     * 
     */
    public ServiceInvokerOptions getServiceInvokerOptions()
        throws ServiceException
    {
        m_service = getService();
        return m_service.getInvokerOptions();
    }

    protected AsyncAdvertisingUniqueIDServiceV1 getProxy()
        throws ServiceException
    {
        m_service = getService();
        m_proxy = m_service.getProxy();
        return m_proxy;
    }

    /**
     * Method returns an instance of Service which has been initilized for this Consumer
     * 
     */
    public Service getService()
        throws ServiceException
    {
        if (m_service == null) {
            m_service = ServiceFactory.create(SVC_ADMIN_NAME, m_environment, m_clientName, m_serviceLocation);
        }
        setUserProvidedSecurityCredentials(m_service);
        return m_service;
    }
    
    protected String getDefaultEnvironmentName() {
        return m_environment;
    }

    	
    private void setTargetLocationComponents(URL targetLocation) {
        if (targetLocation!= null) {
            m_protocolScheme = targetLocation.getProtocol();
            m_hostName = targetLocation.getHost();
            m_urlPath = targetLocation.getPath();
            m_port = targetLocation.getPort();
        }
        if (isEmptyString(m_protocolScheme)) {
            m_protocolScheme = HTTP_NON_SECURE;
        }
        if (isEmptyString(m_hostName)) {
            m_hostName = "localhost";
        }
        if (isEmptyString(m_urlPath)) {
            m_urlPath = "";
        }
        if (m_port< 0) {
            m_port = 0;
        }
    }
    
    public URL getServiceLocation()
    throws MalformedURLException
{
    String location = getLocationFromComponents();
    URL serviceLocationUrl = new URL(location);
    return serviceLocationUrl;
}

    private static boolean isEmptyString(String givenString) {
        return ((givenString == null)||(givenString.trim().length() == 0));
    }
    
    /**
     * @param hostName	Actual hostname of the end point location, 
     * 			Can contain :<port> as well
     * @param protocolScheme	specifies the transport protocol scheme
     * 
     */
    public void setHostName(String hostName)
        throws MalformedURLException
    {
        m_hostName = ((hostName!= null)?hostName:m_hostName);
        String newURL = getLocationFromComponents();
        setServiceLocation(newURL);
    }

    /**
     * Returns the host name of the active end-point(from the servicelocation)
     * 
     */
    public String getHostName()
        throws MalformedURLException
    {
        URL targetLocation = getServiceLocation();
        return ((targetLocation == null)?null:targetLocation.getHost());
    }

    private String getLocationFromComponents() {
        String location = (m_protocolScheme + "://" + m_hostName + ((m_port> 0)? (":" + m_port) : "" ) + m_urlPath);
        return location;
    }
    
    public Future<?> getRequestIDAsync(AsyncHandler<GetRequestIDResponse> param0) {
        Future<?> result = null;
        try {
            m_proxy = getProxy();
        } catch (ServiceException serviceException) {
            throw ServiceRuntimeException.wrap(serviceException);
        }
        result = m_proxy.getRequestIDAsync(param0);
        return result;
    }

    public Response<GetRequestIDResponse> getRequestIDAsync() {
        Response<GetRequestIDResponse> result = null;
        try {
            m_proxy = getProxy();
        } catch (ServiceException serviceException) {
            throw ServiceRuntimeException.wrap(serviceException);
        }
        result = m_proxy.getRequestIDAsync();
        return result;
    }

    public Future<?> testAttachmentAsync(TestAttachment param0, AsyncHandler<TestAttachmentResponse> param1) {
        Future<?> result = null;
        try {
            m_proxy = getProxy();
        } catch (ServiceException serviceException) {
            throw ServiceRuntimeException.wrap(serviceException);
        }
        result = m_proxy.testAttachmentAsync(param0, param1);
        return result;
    }

    public Response<TestAttachmentResponse> testAttachmentAsync(TestAttachment param0) {
        Response<TestAttachmentResponse> result = null;
        try {
            m_proxy = getProxy();
        } catch (ServiceException serviceException) {
            throw ServiceRuntimeException.wrap(serviceException);
        }
        result = m_proxy.testAttachmentAsync(param0);
        return result;
    }

    public Future<?> getGenericClientInfoAsync(GetGenericClientInfoRequest param0, AsyncHandler<GetGenericClientInfoResponse> param1) {
        Future<?> result = null;
        try {
            m_proxy = getProxy();
        } catch (ServiceException serviceException) {
            throw ServiceRuntimeException.wrap(serviceException);
        }
        result = m_proxy.getGenericClientInfoAsync(param0, param1);
        return result;
    }

    public Response<GetGenericClientInfoResponse> getGenericClientInfoAsync(GetGenericClientInfoRequest param0) {
        Response<GetGenericClientInfoResponse> result = null;
        try {
            m_proxy = getProxy();
        } catch (ServiceException serviceException) {
            throw ServiceRuntimeException.wrap(serviceException);
        }
        result = m_proxy.getGenericClientInfoAsync(param0);
        return result;
    }

   

    public Future<?> chainedTransportHeadersAsync(ChainedTransportHeaders param0, AsyncHandler<ChainedTransportHeadersResponse> param1) {
        Future<?> result = null;
        try {
            m_proxy = getProxy();
        } catch (ServiceException serviceException) {
            throw ServiceRuntimeException.wrap(serviceException);
        }
        result = m_proxy.chainedTransportHeadersAsync(param0, param1);
        return result;
    }

    public Response<ChainedTransportHeadersResponse> chainedTransportHeadersAsync(ChainedTransportHeaders param0) {
        Response<ChainedTransportHeadersResponse> result = null;
        try {
            m_proxy = getProxy();
        } catch (ServiceException serviceException) {
            throw ServiceRuntimeException.wrap(serviceException);
        }
        result = m_proxy.chainedTransportHeadersAsync(param0);
        return result;
    }

    public Future<?> getTransportHeadersAsync(GetTransportHeaders param0, AsyncHandler<GetTransportHeadersResponse> param1) {
        Future<?> result = null;
        try {
            m_proxy = getProxy();
        } catch (ServiceException serviceException) {
            throw ServiceRuntimeException.wrap(serviceException);
        }
        result = m_proxy.getTransportHeadersAsync(param0, param1);
        return result;
    }

    public Response<GetTransportHeadersResponse> getTransportHeadersAsync(GetTransportHeaders param0) {
        Response<GetTransportHeadersResponse> result = null;
        try {
            m_proxy = getProxy();
        } catch (ServiceException serviceException) {
            throw ServiceRuntimeException.wrap(serviceException);
        }
        result = m_proxy.getTransportHeadersAsync(param0);
        return result;
    }

    public Future<?> testEnhancedRestAsync(TestEnhancedRest param0, AsyncHandler<TestEnhancedRestResponse> param1) {
        Future<?> result = null;
        try {
            m_proxy = getProxy();
        } catch (ServiceException serviceException) {
            throw ServiceRuntimeException.wrap(serviceException);
        }
        result = m_proxy.testEnhancedRestAsync(param0, param1);
        return result;
    }

    public Response<TestEnhancedRestResponse> testEnhancedRestAsync(TestEnhancedRest param0) {
        Response<TestEnhancedRestResponse> result = null;
        try {
            m_proxy = getProxy();
        } catch (ServiceException serviceException) {
            throw ServiceRuntimeException.wrap(serviceException);
        }
        result = m_proxy.testEnhancedRestAsync(param0);
        return result;
    }

    public Future<?> echoMessageAsync(EchoMessageRequest param0, AsyncHandler<EchoMessageResponse> param1) {
        Future<?> result = null;
        try {
            m_proxy = getProxy();
        } catch (ServiceException serviceException) {
            throw ServiceRuntimeException.wrap(serviceException);
        }
        result = m_proxy.echoMessageAsync(param0, param1);
        return result;
    }

    public Response<EchoMessageResponse> echoMessageAsync(EchoMessageRequest param0) {
        Response<EchoMessageResponse> result = null;
        try {
            m_proxy = getProxy();
        } catch (ServiceException serviceException) {
            throw ServiceRuntimeException.wrap(serviceException);
        }
        result = m_proxy.echoMessageAsync(param0);
        return result;
    }

    public Future<?> getItemAsync(GetItemRequest param0, AsyncHandler<GetItemResponse> param1) {
        Future<?> result = null;
        try {
            m_proxy = getProxy();
        } catch (ServiceException serviceException) {
            throw ServiceRuntimeException.wrap(serviceException);
        }
        result = m_proxy.getItemAsync(param0, param1);
        return result;
    }

    public Response<GetItemResponse> getItemAsync(GetItemRequest param0) {
        Response<GetItemResponse> result = null;
        try {
            m_proxy = getProxy();
        } catch (ServiceException serviceException) {
            throw ServiceRuntimeException.wrap(serviceException);
        }
        result = m_proxy.getItemAsync(param0);
        return result;
    }

    public List<Response<?>> poll(boolean param0, boolean param1)
        throws InterruptedException
    {
        List<Response<?>> result = null;
        try {
            m_proxy = getProxy();
        } catch (ServiceException serviceException) {
            throw ServiceRuntimeException.wrap(serviceException);
        }
        result = m_proxy.poll(param0, param1);
        return result;
    }

   
    public GetRequestIDResponse getRequestID() {
        GetRequestIDResponse result = null;
        try {
            m_proxy = getProxy();
        } catch (ServiceException serviceException) {
            throw ServiceRuntimeException.wrap(serviceException);
        }
        result = m_proxy.getRequestID();
        return result;
    }

    public TestAttachmentResponse testAttachment(TestAttachment param0) {
        TestAttachmentResponse result = null;
        try {
            m_proxy = getProxy();
        } catch (ServiceException serviceException) {
            throw ServiceRuntimeException.wrap(serviceException);
        }
        result = m_proxy.testAttachment(param0);
        return result;
    }

    public GetGenericClientInfoResponse getGenericClientInfo(GetGenericClientInfoRequest param0) {
        GetGenericClientInfoResponse result = null;
        try {
            m_proxy = getProxy();
        } catch (ServiceException serviceException) {
            throw ServiceRuntimeException.wrap(serviceException);
        }
        result = m_proxy.getGenericClientInfo(param0);
        return result;
    }

    

    public ChainedTransportHeadersResponse chainedTransportHeaders(ChainedTransportHeaders param0) {
        ChainedTransportHeadersResponse result = null;
        try {
            m_proxy = getProxy();
        } catch (ServiceException serviceException) {
            throw ServiceRuntimeException.wrap(serviceException);
        }
        result = m_proxy.chainedTransportHeaders(param0);
        return result;
    }

    public GetTransportHeadersResponse getTransportHeaders(GetTransportHeaders param0) {
        GetTransportHeadersResponse result = null;
        try {
            m_proxy = getProxy();
        } catch (ServiceException serviceException) {
            throw ServiceRuntimeException.wrap(serviceException);
        }
        result = m_proxy.getTransportHeaders(param0);
        return result;
    }

    public TestEnhancedRestResponse testEnhancedRest(TestEnhancedRest param0) {
        TestEnhancedRestResponse result = null;
        try {
            m_proxy = getProxy();
        } catch (ServiceException serviceException) {
            throw ServiceRuntimeException.wrap(serviceException);
        }
        result = m_proxy.testEnhancedRest(param0);
        return result;
    }

    public EchoMessageResponse echoMessage(EchoMessageRequest param0) {
        EchoMessageResponse result = null;
        try {
            m_proxy = getProxy();
        } catch (ServiceException serviceException) {
            throw ServiceRuntimeException.wrap(serviceException);
        }
        result = m_proxy.echoMessage(param0);
        return result;
    }

    public GetItemResponse getItem(GetItemRequest param0) {
        GetItemResponse result = null;
        try {
            m_proxy = getProxy();
        } catch (ServiceException serviceException) {
            throw ServiceRuntimeException.wrap(serviceException);
        }
        result = m_proxy.getItem(param0);
        return result;
    }

	@Override
	public GetVersionResponse getVersion(GetVersion getVersion) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Future<?> getVersionAsync(GetVersion param0,
			AsyncHandler<GetVersionResponse> handler) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Response<GetVersionResponse> getVersionAsync(GetVersion param0) {
		// TODO Auto-generated method stub
		return null;
	}
/*
	@Override
	public TestJAXWSCompliance1Response testJAXWSCompliance1(
			TestJAXWSCompliance1 param0) {
		TestJAXWSCompliance1Response result = null;
		 try {
	            m_proxy = getProxy();
	        } catch (ServiceException serviceException) {
	            throw ServiceRuntimeException.wrap(serviceException);
	        }
	        result = m_proxy.testJAXWSCompliance1(param0);
	        return result;
	}

	@Override
	public TestJAXWSCompliance2Response testJAXWSCompliance2(
			TestJAXWSCompliance2 param0) {
		  TestJAXWSCompliance2Response result = null;
	        try {
	            m_proxy = getProxy();
	        } catch (ServiceException serviceException) {
	            throw ServiceRuntimeException.wrap(serviceException);
	        }
	        result = m_proxy.testJAXWSCompliance2(param0);
	        return result;
	}

	@Override
	public Future<?> testJAXWSCompliance1Async(TestJAXWSCompliance1 param0,
			AsyncHandler<TestJAXWSCompliance1Response> handler) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Response<TestJAXWSCompliance1Response> testJAXWSCompliance1Async(
			TestJAXWSCompliance1 param0) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Future<?> testJAXWSCompliance2Async(TestJAXWSCompliance2 param0,
			AsyncHandler<TestJAXWSCompliance2Response> handler) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Response<TestJAXWSCompliance2Response> testJAXWSCompliance2Async(
			TestJAXWSCompliance2 param0) {
		// TODO Auto-generated method stub
		return null;
	}
*/
	@Override
	public TestPrimitiveTypesResponse testPrimitiveTypes(
			TestPrimitiveTypesRequest param0) {
		   TestPrimitiveTypesResponse result = null;
	        try {
	            m_proxy = getProxy();
	        } catch (ServiceException serviceException) {
	            throw ServiceRuntimeException.wrap(serviceException);
	        }
	        result = m_proxy.testPrimitiveTypes(param0);
	        return result;
	}

	@Override
	public Future<?> testPrimitiveTypesAsync(TestPrimitiveTypesRequest param0,
			AsyncHandler<TestPrimitiveTypesResponse> handler) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Response<TestPrimitiveTypesResponse> testPrimitiveTypesAsync(
			TestPrimitiveTypesRequest param0) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public GetMessagesForTheDayResponse testSchemaValidationWithUPA(
			GetMessagesForTheDayRequest testSchemaValidationWithUPA) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Future<?> testSchemaValidationWithUPAAsync(
			GetMessagesForTheDayRequest param0,
			AsyncHandler<GetMessagesForTheDayResponse> handler) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Response<GetMessagesForTheDayResponse> testSchemaValidationWithUPAAsync(
			GetMessagesForTheDayRequest param0) {
		// TODO Auto-generated method stub
		return null;
	}

}
