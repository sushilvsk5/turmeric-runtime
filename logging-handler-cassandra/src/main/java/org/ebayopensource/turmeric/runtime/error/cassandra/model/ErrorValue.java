/*******************************************************************************
 * Copyright (c) 2006-2011 eBay Inc. All Rights Reserved.
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *  
 *    http://www.apache.org/licenses/LICENSE-2.0
 *******************************************************************************/
package org.ebayopensource.turmeric.runtime.error.cassandra.model;

import static org.ebayopensource.turmeric.runtime.error.cassandra.handler.CassandraErrorLoggingHandler.KEY_SEPARATOR;

/**
 * The Class ErrorValue.
 */
public class ErrorValue {

    /** The aggregation period. */
    protected Long aggregationPeriod;

    /** The category. */
    protected String category;

    /** The consumer name. */
    protected String consumerName;

    /** The domain. */
    protected String domain;

    /** The error id. */
    protected Long errorId;

    /** The error message. */
    protected String errorMessage;

    /** The key. */
    protected String key;

    /** The name. */
    protected String name;

    /** The operation name. */
    protected String operationName;

    /** The organization. */
    protected String organization;

    /** The random number. */
    protected int randomNumber;

    /** The server name. */
    protected String serverName;

    /** The server side. */
    protected String serverSide;

    /** The service admin name. */
    protected String serviceAdminName;

    /** The severity. */
    protected String severity;

    /** The sub domain. */
    protected String subDomain;

    /** The time stamp. */
    protected Long tstamp;

    /**
     * Instantiates a new error value.
     */
    public ErrorValue() {

    }

    /**
     * Instantiates a new error value.
     * 
     * @param error
     *            the error
     * @param serverName
     *            the server name
     * @param errorMessage
     *            the error message
     * @param serviceAdminName
     *            the service admin name
     * @param operationName
     *            the operation name
     * @param consumerName
     *            the consumer name
     * @param timeStamp
     *            the time stamp
     * @param serverSide
     *            the server side
     * @param aggregationPeriod
     *            the aggregation period
     * @param randomNumber
     *            the random number
     */
    public ErrorValue(ErrorById error, String serverName, String errorMessage, String serviceAdminName,
                    String operationName, String consumerName, long timeStamp, boolean serverSide,
                    int aggregationPeriod, int randomNumber) {
        this.errorId = error.getErrorId();
        this.serverName = serverName;
        this.errorMessage = errorMessage;
        this.serviceAdminName = serviceAdminName;
        this.operationName = operationName;
        this.consumerName = consumerName;
        this.tstamp = timeStamp;
        this.serverSide = Boolean.toString(serverSide);
        this.aggregationPeriod = Long.valueOf(aggregationPeriod);
        this.randomNumber = randomNumber;
        this.category = error.getCategory();
        this.domain = error.getDomain();
        this.errorId = error.getErrorId();
        this.name = error.getName();
        this.organization = error.getOrganization();
        this.severity = error.getSeverity();
        this.subDomain = error.getSubDomain();
    }

    /**
     * Gets the aggregation period.
     * 
     * @return the aggregation period
     */
    public Long getAggregationPeriod() {
        return aggregationPeriod;
    }

    /**
     * Gets the consumer name.
     * 
     * @return the consumer name
     */
    public String getConsumerName() {
        return consumerName;
    }

    /**
     * Gets the error id.
     * 
     * @return the error id
     */
    public Long getErrorId() {
        return errorId;
    }

    /**
     * Gets the error message.
     * 
     * @return the error message
     */
    public String getErrorMessage() {
        return errorMessage;
    }

    /**
     * Gets the key.
     * 
     * @return the key
     */
    public String getKey() {
        if (key == null) {
            key = this.tstamp + KEY_SEPARATOR + this.randomNumber;
        }

        return key;
    }

    /**
     * Gets the operation name.
     * 
     * @return the operation name
     */
    public String getOperationName() {
        return operationName;
    }

    /**
     * Gets the server name.
     * 
     * @return the server name
     */
    public String getServerName() {
        return serverName;
    }

    /**
     * Gets the service admin name.
     * 
     * @return the service admin name
     */
    public String getServiceAdminName() {
        return serviceAdminName;
    }

    /**
     * Gets the time stamp.
     * 
     * @return the time stamp
     */
    public long getTimeStamp() {
        return tstamp;
    }

    /**
     * Checks if is server side.
     * 
     * @return true, if is server side
     */
    public boolean isServerSide() {
        return Boolean.parseBoolean(serverSide);
    }

    /**
     * Sets the aggregation period.
     * 
     * @param aggregationPeriod
     *            the new aggregation period
     */
    public void setAggregationPeriod(Long aggregationPeriod) {
        this.aggregationPeriod = aggregationPeriod;
    }

    /**
     * Sets the consumer name.
     * 
     * @param consumerName
     *            the new consumer name
     */
    public void setConsumerName(String consumerName) {
        this.consumerName = consumerName;
    }

    /**
     * Sets the error id.
     * 
     * @param errorId
     *            the new error id
     */
    public void setErrorId(Long errorId) {
        this.errorId = errorId;
    }

    /**
     * Sets the error message.
     * 
     * @param errorMessage
     *            the new error message
     */
    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }

    /**
     * Sets the operation name.
     * 
     * @param operationName
     *            the new operation name
     */
    public void setOperationName(String operationName) {
        this.operationName = operationName;
    }

    /**
     * Sets the server name.
     * 
     * @param serverName
     *            the new server name
     */
    public void setServerName(String serverName) {
        this.serverName = serverName;
    }

    /**
     * Sets the server side.
     * 
     * @param serverSide
     *            the new server side
     */
    public void setServerSide(boolean serverSide) {
        this.serverSide = Boolean.toString(serverSide);
    }

    /**
     * Sets the service admin name.
     * 
     * @param serviceAdminName
     *            the new service admin name
     */
    public void setServiceAdminName(String serviceAdminName) {
        this.serviceAdminName = serviceAdminName;
    }

    /**
     * Sets the time stamp.
     * 
     * @param timeStamp
     *            the new time stamp
     */
    public void setTimeStamp(long timeStamp) {
        this.tstamp = timeStamp;
    }

}
