
package com.ukvalley.umeshkhivasara.beproud.model.userpersonaldetail;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Data {


    @SerializedName("dob")
    @Expose
    private String dob;
    @SerializedName("distributo_id")
    @Expose
    private String distributoId;
    @SerializedName("co_distributor_tilte")
    @Expose
    private String coDistributorTilte;
    @SerializedName("co_distributor_name")
    @Expose
    private String coDistributorName;
    @SerializedName("co_distributor_dob")
    @Expose
    private String coDistributorDob;
    @SerializedName("upline")
    @Expose
    private String upline;
    @SerializedName("designation")
    @Expose
    private String designation;

    public String getAnniversary_date() {
        return anniversary_date;
    }

    public void setAnniversary_date(String anniversary_date) {
        this.anniversary_date = anniversary_date;
    }

    @SerializedName("anniversary_date")
    @Expose
    private String anniversary_date;


    @SerializedName("user_name")
    @Expose
    private String firstname;



    public String getDob() {
        return dob;
    }

    public void setDob(String dob) {
        this.dob = dob;
    }

    public String getDistributoId() {
        return distributoId;
    }

    public void setDistributoId(String distributoId) {
        this.distributoId = distributoId;
    }

    public String getCoDistributorTilte() {
        return coDistributorTilte;
    }

    public void setCoDistributorTilte(String coDistributorTilte) {
        this.coDistributorTilte = coDistributorTilte;
    }

    public String getCoDistributorName() {
        return coDistributorName;
    }

    public void setCoDistributorName(String coDistributorName) {
        this.coDistributorName = coDistributorName;
    }

    public String getCoDistributorDob() {
        return coDistributorDob;
    }

    public void setCoDistributorDob(String coDistributorDob) {
        this.coDistributorDob = coDistributorDob;
    }

    public String getUpline() {
        return upline;
    }

    public void setUpline(String upline) {
        this.upline = upline;
    }

    public String getDesignation() {
        return designation;
    }

    public void setDesignation(String designation) {
        this.designation = designation;
    }

    public String getFirstname() {
        return firstname;
    }

    public void setFirstname(String firstname) {
        this.firstname = firstname;
    }

}