
package com.ukvalley.umeshkhivasara.beproud.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class GetSingleUserSupport {

    @SerializedName("status")
    @Expose
    private String status;
    @SerializedName("message")
    @Expose
    private String message;
    @SerializedName("data")
    @Expose
    private GetSingleUser getSingleUser;

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public GetSingleUser getGetSingleUser() {
        return getSingleUser;
    }

    public void setGetSingleUser(GetSingleUser getSingleUser) {
        this.getSingleUser = getSingleUser;
    }

}
