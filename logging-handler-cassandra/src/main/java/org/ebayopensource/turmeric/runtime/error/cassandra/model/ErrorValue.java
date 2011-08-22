package org.ebayopensource.turmeric.runtime.error.cassandra.model;

public class ErrorValue {
    private Long errorId;
    private String serverName;
    private String errorMessage;
    private String serviceAdminName;
    private String operationName;
    private String consumerName;
    private long timeStamp;
    private boolean serverSide;
    private int aggregationPeriod;

    public String getServerName() {
        return serverName;
    }

    public void setServerName(String serverName) {
        this.serverName = serverName;
    }

    public Long getErrorId() {
        return errorId;
    }

    public void setErrorId(Long errorId) {
        this.errorId = errorId;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }

    public String getServiceAdminName() {
        return serviceAdminName;
    }

    public void setServiceAdminName(String serviceAdminName) {
        this.serviceAdminName = serviceAdminName;
    }

    public String getOperationName() {
        return operationName;
    }

    public void setOperationName(String operationName) {
        this.operationName = operationName;
    }

    public String getConsumerName() {
        return consumerName;
    }

    public void setConsumerName(String consumerName) {
        this.consumerName = consumerName;
    }

    public long getTimeStamp() {
        return timeStamp;
    }

    public void setTimeStamp(long timeStamp) {
        this.timeStamp = timeStamp;
    }

    public boolean isServerSide() {
        return serverSide;
    }

    public void setServerSide(boolean serverSide) {
        this.serverSide = serverSide;
    }

    public int getAggregationPeriod() {
        return aggregationPeriod;
    }

    public void setAggregationPeriod(int aggregationPeriod) {
        this.aggregationPeriod = aggregationPeriod;
    }

}
