package com.mhealth.ticker.support.data;

import com.google.gson.annotations.SerializedName;

import java.math.BigDecimal;
import java.util.Date;

public class Attendance extends MongoObject {

    @SerializedName("id")
    private BigDecimal id;

    @SerializedName("status")
    private Integer status;

    @SerializedName("user_id")
    private Integer userId;

    @SerializedName("timestamp")
    private Date timestamp;

    public BigDecimal getId() {
        return id;
    }

    public void setId(BigDecimal id) {
        this.id = id;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public Date getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Date timestamp) {
        this.timestamp = timestamp;
    }
}
