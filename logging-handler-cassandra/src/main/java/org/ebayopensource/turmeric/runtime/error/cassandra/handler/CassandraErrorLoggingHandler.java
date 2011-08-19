/*******************************************************************************
 * Copyright (c) 2006-2010 eBay Inc. All Rights Reserved.
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *  
 *    http://www.apache.org/licenses/LICENSE-2.0
 *******************************************************************************/

package org.ebayopensource.turmeric.runtime.error.cassandra.handler;

import java.util.List;

import me.prettyprint.cassandra.model.QuorumAllConsistencyLevelPolicy;
import me.prettyprint.cassandra.serializers.LongSerializer;
import me.prettyprint.cassandra.serializers.StringSerializer;
import me.prettyprint.hector.api.Cluster;
import me.prettyprint.hector.api.ConsistencyLevelPolicy;
import me.prettyprint.hector.api.Keyspace;
import me.prettyprint.hector.api.beans.HColumn;
import me.prettyprint.hector.api.exceptions.HectorException;
import me.prettyprint.hector.api.factory.HFactory;
import me.prettyprint.hector.api.mutation.MutationResult;
import me.prettyprint.hector.api.mutation.Mutator;

import org.apache.cassandra.db.marshal.LongType;
import org.ebayopensource.turmeric.common.v1.types.CommonErrorData;
import org.ebayopensource.turmeric.common.v1.types.ErrorData;
import org.ebayopensource.turmeric.runtime.common.exceptions.ServiceException;
import org.ebayopensource.turmeric.runtime.common.exceptions.ServiceExceptionInterface;
import org.ebayopensource.turmeric.runtime.common.pipeline.LoggingHandler;
import org.ebayopensource.turmeric.runtime.common.pipeline.LoggingHandlerStage;
import org.ebayopensource.turmeric.runtime.common.pipeline.MessageContext;
import org.ebayopensource.turmeric.runtime.common.types.SOAConstants;
import org.ebayopensource.turmeric.runtime.common.types.SOAHeaders;
import org.ebayopensource.turmeric.runtime.error.cassandra.dao.ErrorDAO;
import org.ebayopensource.turmeric.runtime.error.cassandra.model.Error;
import org.ebayopensource.turmeric.utils.cassandra.HectorManager;
import org.ebayopensource.turmeric.runtime.error.cassandra.model.*;

public class CassandraErrorLoggingHandler implements LoggingHandler {
    ErrorDAO errorDao = null;
    
    public CassandraErrorLoggingHandler(){
        errorDao = new ErrorDAO("Test Cluster", "192.168.2.41", "TurmericMonitoring", Long.class, org.ebayopensource.turmeric.runtime.error.cassandra.model.Error.class, "Errors");
    }

    public void init(InitContext ctx) throws ServiceException {
        // TODO Auto-generated method stub

    }

    public void logProcessingStage(MessageContext ctx, LoggingHandlerStage stage) throws ServiceException {
        // TODO Auto-generated method stub

    }

    public void logResponseResidentError(MessageContext ctx, ErrorData errorData) throws ServiceException {
        // TODO Auto-generated method stub

    }

    public void logError(MessageContext ctx, Throwable e) throws ServiceException {
        ServiceExceptionInterface serviceException = (ServiceExceptionInterface) e;
        List<CommonErrorData> errorsToStore = serviceException.getErrorMessage().getError();
        String consumerName = retrieveConsumerName(ctx);
        String serviceAdminName = ctx.getAdminName();
        String operationName = ctx.getOperationName();
        boolean serverSide = !ctx.getServiceId().isClientSide();
        persistErrors(errorsToStore, serviceAdminName, operationName, serverSide, consumerName);

    }

    public void persistErrors(List<CommonErrorData> errorsToStore, String srvcAdminName, String opName, boolean serverSide, String consumerName) throws ServiceException {
        try {
            long now = System.currentTimeMillis();
            for (CommonErrorData commonErrorData : errorsToStore) {
                String errorMessage = commonErrorData.getMessage();
                org.ebayopensource.turmeric.runtime.error.cassandra.model.Error errorToSave = new Error();
                errorToSave.setErrorId(commonErrorData.getErrorId());
                errorToSave.setName(commonErrorData.getErrorName());
                errorToSave.setCategory(commonErrorData.getCategory().toString());
                errorToSave.setSeverity(commonErrorData.getSeverity().toString());
                errorToSave.setDomain(commonErrorData.getDomain());
                errorToSave.setSubDomain(commonErrorData.getSubdomain());
                errorToSave.setOrganization(commonErrorData.getOrganization());
                errorDao.save(errorToSave.getErrorId(), errorToSave);
            }

        }
        catch (HectorException he) {
            he.printStackTrace();
            throw new ServiceException("Exception logging error in Cassandra cluster", he);
        }
    }

    public void logWarning(MessageContext ctx, Throwable e) throws ServiceException {
        // TODO Auto-generated method stub

    }
    
    private String retrieveConsumerName(MessageContext ctx) throws ServiceException {
        String result = ctx.getRequestMessage().getTransportHeader(SOAHeaders.USECASE_NAME);
        if (result == null)
            result = SOAConstants.DEFAULT_USE_CASE;
        return result;
    }

}
