package org.ebayopensource.turmeric.runtime.error.cassandra.model;

import static org.ebayopensource.turmeric.runtime.error.cassandra.handler.CassandraErrorLoggingHandler.KEY_SEPARATOR;

/**
 * The Class ErrorValue.
 */
public class ErrorValue {

    /**
     * Instantiates a new error value.
     * 
     * @param errorId
     *            the error id
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
     */
    public ErrorValue(Long errorId, String serverName, String errorMessage, String serviceAdminName,
                    String operationName, String consumerName, long timeStamp, boolean serverSide, int aggregationPeriod) {
        super();
        this.errorId = errorId;
        this.serverName = serverName;
        this.errorMessage = errorMessage;
        this.serviceAdminName = serviceAdminName;
        this.operationName = operationName;
        this.consumerName = consumerName;
        this.timeStamp = timeStamp;
        this.serverSide = serverSide;
        this.aggregationPeriod = aggregationPeriod;
    }

    /**
     * Instantiates a new error value.
     */
    public ErrorValue() {

    }

    /** The error id. */
    private Long errorId;

    /** The server name. */
    private String serverName;

    /** The error message. */
    private String errorMessage;

    /** The service admin name. */
    private String serviceAdminName;

    /** The operation name. */
    private String operationName;

    /** The consumer name. */
    private String consumerName;

    /** The time stamp. */
    private long timeStamp;

    /** The server side. */
    private boolean serverSide;

    /** The aggregation period. */
    private int aggregationPeriod;

    /** The key. */
    private String key;

    /**
     * Gets the server name.
     * 
     * @return the server name
     */
    public String getServerName() {
        return serverName;
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
     * Gets the error id.
     * 
     * @return the error id
     */
    public Long getErrorId() {
        return errorId;
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
     * Gets the error message.
     * 
     * @return the error message
     */
    public String getErrorMessage() {
        return errorMessage;
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
     * Gets the service admin name.
     * 
     * @return the service admin name
     */
    public String getServiceAdminName() {
        return serviceAdminName;
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
     * Gets the operation name.
     * 
     * @return the operation name
     */
    public String getOperationName() {
        return operationName;
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
     * Gets the consumer name.
     * 
     * @return the consumer name
     */
    public String getConsumerName() {
        return consumerName;
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
     * Gets the time stamp.
     * 
     * @return the time stamp
     */
    public long getTimeStamp() {
        return timeStamp;
    }

    /**
     * Sets the time stamp.
     * 
     * @param timeStamp
     *            the new time stamp
     */
    public void setTimeStamp(long timeStamp) {
        this.timeStamp = timeStamp;
    }

    /**
     * Checks if is server side.
     * 
     * @return true, if is server side
     */
    public boolean isServerSide() {
        return serverSide;
    }

    /**
     * Sets the server side.
     * 
     * @param serverSide
     *            the new server side
     */
    public void setServerSide(boolean serverSide) {
        this.serverSide = serverSide;
    }

    /**
     * Gets the aggregation period.
     * 
     * @return the aggregation period
     */
    public int getAggregationPeriod() {
        return aggregationPeriod;
    }

    /**
     * Sets the aggregation period.
     * 
     * @param aggregationPeriod
     *            the new aggregation period
     */
    public void setAggregationPeriod(int aggregationPeriod) {
        this.aggregationPeriod = aggregationPeriod;
    }

    /**
     * Gets the key.
     *
     * @return the key
     */
    public String getKey() {
        if (key == null) {
            key = this.errorId + KEY_SEPARATOR + serverName + KEY_SEPARATOR + this.serviceAdminName + KEY_SEPARATOR
                            + this.operationName+KEY_SEPARATOR+serverSide;
        }

        return key;
    }

}
