package com.mhealth.ticker.support.data;

import com.google.gson.annotations.SerializedName;

import java.util.Date;
import java.util.List;

public class Attendances {

    @SerializedName("attendances")
    private List<Attendance> attendances;

    @SerializedName("last_updated")
    private Date lastUpdated;

    public List<Attendance> getAttendances() {
        return attendances;
    }

    public void setAttendances(List<Attendance> attendances) {
        this.attendances = attendances;
    }

    public Date getLastUpdated() {
        return lastUpdated;
    }

    public void setLastUpdated(Date lastUpdated) {
        this.lastUpdated = lastUpdated;
    }
}
