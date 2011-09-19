/*******************************************************************************
 * Copyright (c) 2006-2011 eBay Inc. All Rights Reserved.
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *  
 *    http://www.apache.org/licenses/LICENSE-2.0
 *******************************************************************************/

package org.ebayopensource.turmeric.runtime.error.cassandra.handler;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Collections;
import java.util.List;
import java.util.Random;

import me.prettyprint.hector.api.exceptions.HectorException;

import org.ebayopensource.turmeric.common.v1.types.CommonErrorData;
import org.ebayopensource.turmeric.common.v1.types.ErrorData;
import org.ebayopensource.turmeric.runtime.common.exceptions.ServiceException;
import org.ebayopensource.turmeric.runtime.common.exceptions.ServiceExceptionInterface;
import org.ebayopensource.turmeric.runtime.common.pipeline.LoggingHandler;
import org.ebayopensource.turmeric.runtime.common.pipeline.LoggingHandlerStage;
import org.ebayopensource.turmeric.runtime.common.pipeline.MessageContext;
import org.ebayopensource.turmeric.runtime.common.types.SOAConstants;
import org.ebayopensource.turmeric.runtime.common.types.SOAHeaders;
import org.ebayopensource.turmeric.runtime.error.cassandra.dao.ErrorByIdDAO;
import org.ebayopensource.turmeric.runtime.error.cassandra.dao.ErrorCountsDAO;
import org.ebayopensource.turmeric.runtime.error.cassandra.dao.ErrorValueDAO;
import org.ebayopensource.turmeric.runtime.error.cassandra.model.ErrorById;
import org.ebayopensource.turmeric.runtime.error.cassandra.model.ErrorValue;

/**
 * The Class CassandraErrorLoggingHandler.
 * 
 * @author manuelchinea
 */
public class CassandraErrorLoggingHandler implements LoggingHandler {

    /** The Constant KEY_SEPARATOR. */
    public static final String KEY_SEPARATOR = "|";

    /** The cluster name. */
    private String clusterName;

    /** The error counts dao. */
    private ErrorCountsDAO errorCountsDao = null;

    /** The error dao. */
    private ErrorByIdDAO errorDao = null;

    /** The error value dao. */
    private ErrorValueDAO errorValueDao = null;

    /** The host address. */
    private String hostAddress;

    /** The keyspace name. */
    private String keyspaceName;

    /** The random generator. */
    private Random randomGenerator;

    /**
     * Instantiates a new cassandra error logging handler.
     */
    public CassandraErrorLoggingHandler() {
    }

    /**
     * Gets the cluster name.
     * 
     * @return the cluster name
     */
    public String getClusterName() {
        return clusterName;
    }

    /**
     * Gets the host address.
     * 
     * @return the host address
     */
    public String getHostAddress() {
        return hostAddress;
    }

    /**
     * Gets the inet address.
     * 
     * @return the inet address
     * @throws ServiceException
     *             the service exception
     */
    private String getInetAddress() throws ServiceException {
        try {
            return InetAddress.getLocalHost().getCanonicalHostName();
        }
        catch (UnknownHostException x) {
            throw new ServiceException("Unkonwn host name", x);
        }
    }

    /**
     * Gets the keyspace name.
     * 
     * @return the keyspace name
     */
    public String getKeyspaceName() {
        return keyspaceName;
    }

    /**
     * Inits the CassandraErrorLoggingHandler.
     * 
     * @param ctx
     *            the ctx
     * @throws ServiceException
     *             the service exception
     * @see org.ebayopensource.turmeric.runtime.common.pipeline.LoggingHandler#init(org.ebayopensource.turmeric.runtime.common.pipeline.LoggingHandler.InitContext)
     */
    @Override
    public void init(InitContext ctx) throws ServiceException {
        java.util.Map<String, String> options = ctx.getOptions();
        clusterName = options.get("cluster-name");
        hostAddress = options.get("host-address");
        keyspaceName = options.get("keyspace-name");
        String randomGeneratorClassName = options.get("random-generator-class-name");
        if ((randomGeneratorClassName == null) || randomGeneratorClassName.isEmpty()) {
            randomGenerator = new Random(System.currentTimeMillis());
        }
        else {
            try {
                Class<?> theClass = Class.forName(randomGeneratorClassName);
                randomGenerator = (Random) theClass.newInstance();
            }
            catch (Exception e) {
                throw new ServiceException("Error instancing random generator class", e);
            }
        }
        errorDao = new ErrorByIdDAO(clusterName, hostAddress, keyspaceName, Long.class,
                        org.ebayopensource.turmeric.runtime.error.cassandra.model.ErrorById.class, "ErrorsById");
        errorValueDao = new ErrorValueDAO(clusterName, hostAddress, keyspaceName, String.class,
                        org.ebayopensource.turmeric.runtime.error.cassandra.model.ErrorValue.class, "ErrorValues");
        errorCountsDao = new ErrorCountsDAO(clusterName, hostAddress, keyspaceName);

    }

    /**
     * Log error.
     * 
     * @param ctx
     *            the ctx
     * @param e
     *            the e
     * @throws ServiceException
     *             the service exception
     * @see org.ebayopensource.turmeric.runtime.common.pipeline.LoggingHandler#logError(org.ebayopensource.turmeric.runtime.common.pipeline.MessageContext,
     *      java.lang.Throwable)
     */
    @Override
    public void logError(MessageContext ctx, Throwable e) throws ServiceException {
        ServiceExceptionInterface serviceException = (ServiceExceptionInterface) e;
        List<CommonErrorData> errorsToStore = serviceException.getErrorMessage().getError();
        String consumerName = this.retrieveConsumerName(ctx);
        String serviceAdminName = ctx.getAdminName();
        String operationName = ctx.getOperationName();
        boolean serverSide = !ctx.getServiceId().isClientSide();
        String serverName = this.getInetAddress();
        long now = System.currentTimeMillis();
        this.persistErrors(errorsToStore, serverName, serviceAdminName, operationName, serverSide, consumerName, now);

    }

    /**
     * Log processing stage.
     * 
     * @param ctx
     *            the ctx
     * @param stage
     *            the stage
     * @throws ServiceException
     *             the service exception
     * @see org.ebayopensource.turmeric.runtime.common.pipeline.LoggingHandler#logProcessingStage(org.ebayopensource.turmeric.runtime.common.pipeline.MessageContext,
     *      org.ebayopensource.turmeric.runtime.common.pipeline.LoggingHandlerStage)
     */
    @Override
    public void logProcessingStage(MessageContext ctx, LoggingHandlerStage stage) throws ServiceException {
    }

    /**
     * Log response resident error.
     * 
     * @param ctx
     *            the ctx
     * @param errorData
     *            the error data
     * @throws ServiceException
     *             the service exception
     * @see org.ebayopensource.turmeric.runtime.common.pipeline.LoggingHandler#logResponseResidentError(org.ebayopensource.turmeric.runtime.common.pipeline.MessageContext,
     *      org.ebayopensource.turmeric.common.v1.types.ErrorData)
     */
    @Override
    public void logResponseResidentError(MessageContext ctx, ErrorData errorData) throws ServiceException {
        List<? extends ErrorData> errorsToStore = Collections.singletonList(errorData);
        String consumerName = this.retrieveConsumerName(ctx);
        String serviceAdminName = ctx.getAdminName();
        String operationName = ctx.getOperationName();
        boolean serverSide = !ctx.getServiceId().isClientSide();
        String serverName = this.getInetAddress();
        long now = System.currentTimeMillis();

        this.persistErrors(errorsToStore, serverName, serviceAdminName, operationName, serverSide, consumerName, now);

    }

    /**
     * Log warning.
     * 
     * @param ctx
     *            the ctx
     * @param e
     *            the e
     * @throws ServiceException
     *             the service exception
     * @see org.ebayopensource.turmeric.runtime.common.pipeline.LoggingHandler#logWarning(org.ebayopensource.turmeric.runtime.common.pipeline.MessageContext,
     *      java.lang.Throwable)
     */
    @Override
    public void logWarning(MessageContext ctx, Throwable e) throws ServiceException {
        this.logError(ctx, e);
    }

    /**
     * Persist errors.
     * 
     * @param errorsList
     *            the errors to store
     * @param serverName
     *            the server name
     * @param srvcAdminName
     *            the srvc admin name
     * @param opName
     *            the op name
     * @param serverSide
     *            the server side
     * @param consumerName
     *            the consumer name
     * @param timeStamp
     *            the time stamp
     * @throws ServiceException
     *             the service exception
     */
    public void persistErrors(List<? extends ErrorData> errorsList, String serverName, String srvcAdminName,
                    String opName, boolean serverSide, String consumerName, long timeStamp) throws ServiceException {
        try {

            for (ErrorData errorData : errorsList) {
                CommonErrorData commonErrorData = (CommonErrorData) errorData;
                String errorMessage = commonErrorData.getMessage();
                org.ebayopensource.turmeric.runtime.error.cassandra.model.ErrorById errorToSave = new ErrorById(
                                commonErrorData);
                ErrorValue errorValue = new ErrorValue(errorToSave, serverName, errorMessage, srvcAdminName, opName,
                                consumerName, timeStamp, serverSide, 0, randomGenerator.nextInt());
                String errorValueKey = errorValue.getKey();

                errorDao.save(errorToSave.getErrorId(), errorToSave, timeStamp, errorValueKey);

                // not good. What if 2 different error values for the same error occurs at the same time?
                errorValueDao.save(errorValueKey, errorValue);
                errorCountsDao.saveErrorCounts(errorToSave, errorValue, errorValueKey, timeStamp, 1);
            }

        }
        catch (HectorException he) {
            throw new ServiceException("Exception logging error in Cassandra cluster", he);
        }
    }

    /**
     * Retrieve consumer name.
     * 
     * @param ctx
     *            the ctx
     * @return the string
     * @throws ServiceException
     *             the service exception
     */
    private String retrieveConsumerName(MessageContext ctx) throws ServiceException {
        String result = ctx.getRequestMessage().getTransportHeader(SOAHeaders.USECASE_NAME);
        if (result == null)
            result = SOAConstants.DEFAULT_USE_CASE;
        return result;
    }

}
