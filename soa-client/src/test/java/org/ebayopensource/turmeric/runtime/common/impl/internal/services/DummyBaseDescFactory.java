package org.ebayopensource.turmeric.runtime.common.impl.internal.services;

import java.util.Collection;

import javax.wsdl.Definition;
import javax.xml.namespace.QName;

import org.ebayopensource.turmeric.runtime.common.exceptions.ServiceCreationException;
import org.ebayopensource.turmeric.runtime.common.exceptions.ServiceException;
import org.ebayopensource.turmeric.runtime.common.impl.internal.config.OptionList;
import org.ebayopensource.turmeric.runtime.common.impl.internal.service.BaseServiceDescFactory;
import org.ebayopensource.turmeric.runtime.common.impl.internal.service.ServiceDesc;
import org.ebayopensource.turmeric.runtime.common.service.HeaderMappingsDesc;
import org.ebayopensource.turmeric.runtime.common.service.ServiceContext;
import org.ebayopensource.turmeric.runtime.common.service.ServiceId;

public class DummyBaseDescFactory extends BaseServiceDescFactory<ServiceDesc> {

	protected DummyBaseDescFactory(String factoryName, boolean isClientSide,
			boolean requiresLoadAll) {
		super(factoryName, isClientSide, requiresLoadAll);
	}

	@Override
	public ServiceContext getServiceContext(ServiceDesc desc)
			throws ServiceException {
		return null;
	}

	@Override
	protected void postServiceDescLoad(ServiceDesc svcDesc) {
		
	}

	@Override
	protected Collection loadAllServiceNames() throws ServiceException {
		return null;
	}

	@Override
	protected ServiceDesc createServiceDesc(ServiceId id)
			throws ServiceException {
		return null;
	}

	@Override
	protected ServiceDesc createServiceDesc(ServiceId id, boolean rawMode)
			throws ServiceException {
		return null;
	}

	@Override
	protected ServiceDesc createServiceDesc(ServiceDesc commonDesc,
			ServiceId newServiceId, QName srvQName, Definition definition,
			boolean rawMode) throws ServiceException {
		return null;
	}

	@Override
	protected String getDefaultLoggingHandlerClassName() {
		return null;
	}
	
	/**
	 * We want to test this one in isolation, but the Abstract class makes this protected, so we need
	 * to make the visibility to public for testing.  We'll pass everything through to the underlying class
	 * though.
	 * 
	 * {@inheritDoc}
	 * 
	 */
	@Override
	public HeaderMappingsDesc loadHeaderMappings(String adminName,
			OptionList options, boolean inbound)
			throws ServiceCreationException {
		return super.loadHeaderMappings(adminName, options, inbound);
	}
}
