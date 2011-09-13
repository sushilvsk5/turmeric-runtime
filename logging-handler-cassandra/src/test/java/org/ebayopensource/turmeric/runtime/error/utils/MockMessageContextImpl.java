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

    public boolean isElementOrderPreserved() {

        return false;
    }

    public Charset getCharset() {

        return null;
    }

    public String getDefaultNamespace() {

        return null;
    }

    public Map<String, List<String>> getNamespaceToPrefixMap() {

        return null;
    }

    public String getNsForJavaType(Class arg0) {

        return null;
    }

    public String getPayloadType() {

        return null;
    }

    public Map<String, String> getPrefixToNamespaceMap() {

        return null;
    }

    public Class getRootClass() {

        return null;
    }

    public DataElementSchema getRootElementSchema() {

        return null;
    }

    public QName getRootXMLName() {

        return null;
    }

    public String getSingleNamespace() {

        return null;
    }

    public ITypeConversionContext getTypeConversionContext() {

        return null;
    }

    public boolean isREST() {

        return false;
    }

    public String getAdminName() {

        return null;
    }

    public QName getServiceQName() {

        return null;
    }

    public ServiceId getServiceId() {

        return null;
    }

    public ServiceContext getServiceContext() {

        return null;
    }

    public ServiceOperationDesc getOperation() {

        return null;
    }

    public String getOperationName() {

        return null;
    }

    public MessageProcessingStage getProcessingStage() {

        return null;
    }

    public Message getRequestMessage() {
        return null;
    }

    public Message getResponseMessage() {

        return null;
    }

    public Object getAuthenticatedUser() {

        return null;
    }

    public void setAuthenticatedUser(Object user) {

    }

    public String getMessageProtocol() {

        return null;
    }

    public ServiceAddress getServiceAddress() {

        return null;
    }

    public ServiceAddress getClientAddress() {

        return null;
    }

    public String getRequestId() {

        return null;
    }

    public String getRequestGuid() {

        return null;
    }

    public String getRequestUri() {

        return null;
    }

    public void setRequestId(String requestId, String requestGuid) throws ServiceException {

    }

    public boolean hasErrors() {

        return false;
    }

    public List<Throwable> getErrorList() {

        return null;
    }

    public List<Throwable> getWarningList() {

        return null;
    }

    public boolean hasResponseResidentErrors() {

        return false;
    }

    public List<CommonErrorData> getResponseResidentErrorList() {

        return null;
    }

    public void addResponseResidentError(CommonErrorData errorData) {

    }

    public void addError(Throwable t) {

    }

    public void addWarning(Throwable t) {

    }

    public Object getProperty(String name) {

        return null;
    }

    public void setProperty(String name, Object value) throws ServiceException {

    }

    public Charset getEffectiveCharset() {

        return null;
    }

    public String getInvokerVersion() {

        return null;
    }

    public String getServiceVersion() {

        return null;
    }

    public SecurityContext getSecurityContext() {

        return null;
    }

    public String getServiceLayer() {

        return null;
    }

    public boolean isInboundRawMode() {

        return false;
    }

    public boolean isOutboundRawMode() {

        return false;
    }

    public void setInboundRawMode(boolean b) {

    }

    public void setOutboundRawMode(boolean b) {

    }

    public boolean isAsync() {

        return false;
    }

    public ErrorDataProvider getErrorDataProvider() throws ServiceException {

        return null;
    }

}
