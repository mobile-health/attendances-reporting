package com.mhealth.ticker.support.data;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.UUID;

public class MongoObject {

    public static final String CREATED_AT = "created_at";
    public static final String MODIFIED_AT = "modified_at";

    @SerializedName("rawUuid")
    @Expose
    private String rawUuid;

    public MongoObject() {
        validate();
    }

    public void validate() {
        if (rawUuid == null) {
            rawUuid = UUID.randomUUID().toString();
        }
    }

    public String getRawUuid() {
        return rawUuid;
    }

    public void setRawUuid(String rawUuid) {
        this.rawUuid = rawUuid;
    }

}
