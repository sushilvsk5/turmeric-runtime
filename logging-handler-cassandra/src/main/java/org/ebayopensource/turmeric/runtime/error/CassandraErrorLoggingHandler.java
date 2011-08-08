package org.ebayopensource.turmeric.runtime.error;

import org.ebayopensource.turmeric.common.v1.types.ErrorData;
import org.ebayopensource.turmeric.runtime.common.exceptions.ServiceException;
import org.ebayopensource.turmeric.runtime.common.pipeline.LoggingHandler;
import org.ebayopensource.turmeric.runtime.common.pipeline.LoggingHandlerStage;
import org.ebayopensource.turmeric.runtime.common.pipeline.MessageContext;

public class CassandraErrorLoggingHandler implements LoggingHandler{

	public void init(InitContext ctx) throws ServiceException {
		// TODO Auto-generated method stub
		
	}

	public void logProcessingStage(MessageContext ctx, LoggingHandlerStage stage)
			throws ServiceException {
		// TODO Auto-generated method stub
		
	}

	public void logResponseResidentError(MessageContext ctx, ErrorData errorData)
			throws ServiceException {
		// TODO Auto-generated method stub
		
	}

	public void logError(MessageContext ctx, Throwable e)
			throws ServiceException {
		// TODO Auto-generated method stub
		
	}

	public void logWarning(MessageContext ctx, Throwable e)
			throws ServiceException {
		// TODO Auto-generated method stub
		
	}

}
