package com.ukvalley.umeshkhivasara.beproud.model;

import com.google.gson.annotations.SerializedName;

public class Datamodel {
    @SerializedName("status")
    private String status;
    @SerializedName("message")
    private String message;
    @SerializedName("data")
    private String data;
   /* @SerializedName("pagination")
    private  String pagination;*/










    public Datamodel(String status, String message, String data, String pagination) {
        this.status = status;
        this.message = message;
        this.data=data;
//        this.pagination=pagination;
    }
    public Datamodel() {
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
    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }
  /*  public String getPagination() {
        return pagination;
    }

    public void setPagination(String pagination) {
        this.pagination = pagination;
    }*/

}