package org.ebayopensource.turmeric.runtime.error.cassandra.model;

/**
 * The Class ErrorValue.
 */
public class ErrorValue {
    
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
     * @param serverName the new server name
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
     * @param errorId the new error id
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
     * @param errorMessage the new error message
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
     * @param serviceAdminName the new service admin name
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
     * @param operationName the new operation name
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
     * @param consumerName the new consumer name
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
     * @param timeStamp the new time stamp
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
     * @param serverSide the new server side
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
     * @param aggregationPeriod the new aggregation period
     */
    public void setAggregationPeriod(int aggregationPeriod) {
        this.aggregationPeriod = aggregationPeriod;
    }

}
