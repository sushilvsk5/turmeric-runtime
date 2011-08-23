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
        // TODO Auto-generated method stub
        return false;
    }

    public Charset getCharset() {
        // TODO Auto-generated method stub
        return null;
    }

    public String getDefaultNamespace() {
        // TODO Auto-generated method stub
        return null;
    }

    public Map<String, List<String>> getNamespaceToPrefixMap() {
        // TODO Auto-generated method stub
        return null;
    }

    public String getNsForJavaType(Class arg0) {
        // TODO Auto-generated method stub
        return null;
    }

    public String getPayloadType() {
        // TODO Auto-generated method stub
        return null;
    }

    public Map<String, String> getPrefixToNamespaceMap() {
        // TODO Auto-generated method stub
        return null;
    }

    public Class getRootClass() {
        // TODO Auto-generated method stub
        return null;
    }

    public DataElementSchema getRootElementSchema() {
        // TODO Auto-generated method stub
        return null;
    }

    public QName getRootXMLName() {
        // TODO Auto-generated method stub
        return null;
    }

    public String getSingleNamespace() {
        // TODO Auto-generated method stub
        return null;
    }

    public ITypeConversionContext getTypeConversionContext() {
        // TODO Auto-generated method stub
        return null;
    }

    public boolean isREST() {
        // TODO Auto-generated method stub
        return false;
    }

    public String getAdminName() {
        // TODO Auto-generated method stub
        return null;
    }

    public QName getServiceQName() {
        // TODO Auto-generated method stub
        return null;
    }

    public ServiceId getServiceId() {
        // TODO Auto-generated method stub
        return null;
    }

    public ServiceContext getServiceContext() {
        // TODO Auto-generated method stub
        return null;
    }

    public ServiceOperationDesc getOperation() {
        // TODO Auto-generated method stub
        return null;
    }

    public String getOperationName() {
        // TODO Auto-generated method stub
        return null;
    }

    public MessageProcessingStage getProcessingStage() {
        // TODO Auto-generated method stub
        return null;
    }

    public Message getRequestMessage() {
        return null;
    }

    public Message getResponseMessage() {
        // TODO Auto-generated method stub
        return null;
    }

    public Object getAuthenticatedUser() {
        // TODO Auto-generated method stub
        return null;
    }

    public void setAuthenticatedUser(Object user) {
        // TODO Auto-generated method stub

    }

    public String getMessageProtocol() {
        // TODO Auto-generated method stub
        return null;
    }

    public ServiceAddress getServiceAddress() {
        // TODO Auto-generated method stub
        return null;
    }

    public ServiceAddress getClientAddress() {
        // TODO Auto-generated method stub
        return null;
    }

    public String getRequestId() {
        // TODO Auto-generated method stub
        return null;
    }

    public String getRequestGuid() {
        // TODO Auto-generated method stub
        return null;
    }

    public String getRequestUri() {
        // TODO Auto-generated method stub
        return null;
    }

    public void setRequestId(String requestId, String requestGuid) throws ServiceException {
        // TODO Auto-generated method stub

    }

    public boolean hasErrors() {
        // TODO Auto-generated method stub
        return false;
    }

    public List<Throwable> getErrorList() {
        // TODO Auto-generated method stub
        return null;
    }

    public List<Throwable> getWarningList() {
        // TODO Auto-generated method stub
        return null;
    }

    public boolean hasResponseResidentErrors() {
        // TODO Auto-generated method stub
        return false;
    }

    public List<CommonErrorData> getResponseResidentErrorList() {
        // TODO Auto-generated method stub
        return null;
    }

    public void addResponseResidentError(CommonErrorData errorData) {
        // TODO Auto-generated method stub

    }

    public void addError(Throwable t) {
        // TODO Auto-generated method stub

    }

    public void addWarning(Throwable t) {
        // TODO Auto-generated method stub

    }

    public Object getProperty(String name) {
        // TODO Auto-generated method stub
        return null;
    }

    public void setProperty(String name, Object value) throws ServiceException {
        // TODO Auto-generated method stub

    }

    public Charset getEffectiveCharset() {
        // TODO Auto-generated method stub
        return null;
    }

    public String getInvokerVersion() {
        // TODO Auto-generated method stub
        return null;
    }

    public String getServiceVersion() {
        // TODO Auto-generated method stub
        return null;
    }

    public SecurityContext getSecurityContext() {
        // TODO Auto-generated method stub
        return null;
    }

    public String getServiceLayer() {
        // TODO Auto-generated method stub
        return null;
    }

    public boolean isInboundRawMode() {
        // TODO Auto-generated method stub
        return false;
    }

    public boolean isOutboundRawMode() {
        // TODO Auto-generated method stub
        return false;
    }

    public void setInboundRawMode(boolean b) {
        // TODO Auto-generated method stub

    }

    public void setOutboundRawMode(boolean b) {
        // TODO Auto-generated method stub

    }

    public boolean isAsync() {
        // TODO Auto-generated method stub
        return false;
    }

    public ErrorDataProvider getErrorDataProvider() throws ServiceException {
        // TODO Auto-generated method stub
        return null;
    }

}
