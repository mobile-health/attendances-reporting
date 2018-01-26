package com.mhealth.ticker.support.data;

import com.google.gson.annotations.SerializedName;
import org.codehaus.jackson.annotate.JsonIgnoreProperties;
import org.codehaus.jackson.annotate.JsonProperty;

/**
 * Created by luhonghai on 4/10/17.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class ResponseMessage {

    @SerializedName("result")
    @JsonProperty("result")
    private String message;

    public ResponseMessage() {

    }

    public ResponseMessage(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
