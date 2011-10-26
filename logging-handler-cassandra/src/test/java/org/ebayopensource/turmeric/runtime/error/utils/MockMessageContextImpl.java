/*******************************************************************************
 * Copyright (c) 2006-2011 eBay Inc. All Rights Reserved.
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *  
 *    http://www.apache.org/licenses/LICENSE-2.0
 *******************************************************************************/
package org.ebayopensource.turmeric.runtime.error.utils;

import java.nio.charset.Charset;
import java.util.List;
import java.util.Map;

import javax.xml.namespace.QName;

import org.ebayopensource.turmeric.common.v1.types.CommonErrorData;
import org.ebayopensource.turmeric.runtime.binding.ITypeConversionContext;
import org.ebayopensource.turmeric.runtime.binding.schema.DataElementSchema;
import org.ebayopensource.turmeric.runtime.common.errors.ErrorDataProvider;
import org.ebayopensource.turmeric.runtime.common.exceptions.ServiceException;
import org.ebayopensource.turmeric.runtime.common.pipeline.Message;
import org.ebayopensource.turmeric.runtime.common.pipeline.MessageContext;
import org.ebayopensource.turmeric.runtime.common.pipeline.MessageProcessingStage;
import org.ebayopensource.turmeric.runtime.common.security.SecurityContext;
import org.ebayopensource.turmeric.runtime.common.service.ServiceContext;
import org.ebayopensource.turmeric.runtime.common.service.ServiceId;
import org.ebayopensource.turmeric.runtime.common.service.ServiceOperationDesc;
import org.ebayopensource.turmeric.runtime.common.types.ServiceAddress;

public class MockMessageContextImpl implements MessageContext {

    @Override
    public void addError(Throwable t) {

    }

    @Override
    public void addResponseResidentError(CommonErrorData errorData) {

    }

    @Override
    public void addWarning(Throwable t) {

    }

    @Override
    public String getAdminName() {

        return null;
    }

    @Override
    public Object getAuthenticatedUser() {

        return null;
    }

    @Override
    public Charset getCharset() {

        return null;
    }

    @Override
    public ServiceAddress getClientAddress() {

        return null;
    }

    @Override
    public String getDefaultNamespace() {

        return null;
    }

    @Override
    public Charset getEffectiveCharset() {

        return null;
    }

    @Override
    public ErrorDataProvider getErrorDataProvider() throws ServiceException {

        return null;
    }

    @Override
    public List<Throwable> getErrorList() {

        return null;
    }

    @Override
    public String getInvokerVersion() {

        return null;
    }

    @Override
    public String getMessageProtocol() {

        return null;
    }

    @Override
    public Map<String, List<String>> getNamespaceToPrefixMap() {

        return null;
    }

    @Override
    public String getNsForJavaType(Class arg0) {

        return null;
    }

    @Override
    public ServiceOperationDesc getOperation() {

        return null;
    }

    @Override
    public String getOperationName() {

        return null;
    }

    @Override
    public String getPayloadType() {

        return null;
    }

    @Override
    public Map<String, String> getPrefixToNamespaceMap() {

        return null;
    }

    @Override
    public MessageProcessingStage getProcessingStage() {

        return null;
    }

    @Override
    public Object getProperty(String name) {

        return null;
    }

    @Override
    public String getRequestGuid() {

        return null;
    }

    @Override
    public String getRequestId() {

        return null;
    }

    @Override
    public Message getRequestMessage() {
        return null;
    }

    @Override
    public String getRequestUri() {

        return null;
    }

    @Override
    public Message getResponseMessage() {

        return null;
    }

    @Override
    public List<CommonErrorData> getResponseResidentErrorList() {

        return null;
    }

    @Override
    public Class getRootClass() {

        return null;
    }

    @Override
    public DataElementSchema getRootElementSchema() {

        return null;
    }

    @Override
    public QName getRootXMLName() {

        return null;
    }

    @Override
    public SecurityContext getSecurityContext() {

        return null;
    }

    @Override
    public ServiceAddress getServiceAddress() {

        return null;
    }

    @Override
    public ServiceContext getServiceContext() {

        return null;
    }

    @Override
    public ServiceId getServiceId() {

        return null;
    }

    @Override
    public String getServiceLayer() {

        return null;
    }

    @Override
    public QName getServiceQName() {

        return null;
    }

    @Override
    public String getServiceVersion() {

        return null;
    }

    @Override
    public String getSingleNamespace() {

        return null;
    }

    @Override
    public ITypeConversionContext getTypeConversionContext() {

        return null;
    }

    @Override
    public List<Throwable> getWarningList() {

        return null;
    }

    @Override
    public boolean hasErrors() {

        return false;
    }

    @Override
    public boolean hasResponseResidentErrors() {

        return false;
    }

    @Override
    public boolean isAsync() {

        return false;
    }

    @Override
    public boolean isElementOrderPreserved() {

        return false;
    }

    @Override
    public boolean isInboundRawMode() {

        return false;
    }

    @Override
    public boolean isOutboundRawMode() {

        return false;
    }

    @Override
    public boolean isREST() {

        return false;
    }

    @Override
    public void setAuthenticatedUser(Object user) {

    }

    @Override
    public void setInboundRawMode(boolean b) {

    }

    @Override
    public void setOutboundRawMode(boolean b) {

    }

    @Override
    public void setProperty(String name, Object value) throws ServiceException {

    }

    @Override
    public void setRequestId(String requestId, String requestGuid) throws ServiceException {

    }

}