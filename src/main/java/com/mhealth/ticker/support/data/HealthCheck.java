package com.mhealth.ticker.support.data;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.math.BigDecimal;
import java.util.Date;

public class HealthCheck extends MongoObject {

    @SerializedName("name")
    @Expose
    private String name;

    @SerializedName("start_time")
    @Expose
    private Date startTime;

    @SerializedName("end_time")
    @Expose
    private Date endTime;

    @SerializedName("execution_time")
    @Expose
    private BigDecimal executionTime;

    @SerializedName("status")
    @Expose
    private boolean status;

    @SerializedName("message")
    @Expose
    private String message;

    @SerializedName("session")
    @Expose
    private String session;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean isStatus() {
        return status;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getSession() {
        return session;
    }

    public void setSession(String session) {
        this.session = session;
    }

    public Date getStartTime() {
        return startTime;
    }

    public void setStartTime(Date startTime) {
        this.startTime = startTime;
    }

    public Date getEndTime() {
        return endTime;
    }

    public void setEndTime(Date endTime) {
        this.endTime = endTime;
    }

    public BigDecimal getExecutionTime() {
        return executionTime;
    }

    public void setExecutionTime(BigDecimal executionTime) {
        this.executionTime = executionTime;
    }
}
