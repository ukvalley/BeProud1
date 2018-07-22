package com.ukvalley.umeshkhivasara.beproud.model;

import com.google.gson.annotations.SerializedName;

public class SignupResponsemodel {
    @SerializedName("status")
    private String status;
    @SerializedName("message")
    private String message;

    public SignupResponsemodel(String status, String message) {
        this.status = status;
        this.message = message;
    }
    public SignupResponsemodel() {
    }
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
}