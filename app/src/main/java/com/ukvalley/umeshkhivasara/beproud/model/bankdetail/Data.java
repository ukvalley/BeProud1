
package com.ukvalley.umeshkhivasara.beproud.model.bankdetail;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Data {

    @SerializedName("pannumber")
    @Expose
    private String pannumber;
    @SerializedName("panimage")
    @Expose
    private String panimage;
    @SerializedName("ifsccode")
    @Expose
    private String ifsccode;
    @SerializedName("bankname")
    @Expose
    private String bankname;
    @SerializedName("branchname")
    @Expose
    private String branchname;
    @SerializedName("accountnum")
    @Expose
    private String accountnum;



    @SerializedName("adharno")
    @Expose
    private String adharno;



    @SerializedName("adharimage")
    @Expose
    private  String adharimage;

    public String getPannumber() {
        return pannumber;
    }

    public void setPannumber(String pannumber) {
        this.pannumber = pannumber;
    }

    public String getPanimage() {
        return panimage;
    }

    public void setPanimage(String panimage) {
        this.panimage = panimage;
    }

    public String getIfsccode() {
        return ifsccode;
    }

    public void setIfsccode(String ifsccode) {
        this.ifsccode = ifsccode;
    }

    public String getBankname() {
        return bankname;
    }

    public void setBankname(String bankname) {
        this.bankname = bankname;
    }

    public String getBranchname() {
        return branchname;
    }

    public void setBranchname(String branchname) {
        this.branchname = branchname;
    }

    public String getAccountnum() {
        return accountnum;
    }

    public void setAccountnum(String accountnum) {
        this.accountnum = accountnum;
    }

    public String getAdharimage() {
        return adharimage;
    }

    public void setAdharimage(String adharimage) {
        this.adharimage = adharimage;
    }

    public String getAdharno() {
        return adharno;
    }

    public void setAdharno(String adharno) {
        this.adharno = adharno;
    }

}
