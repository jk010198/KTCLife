package com.ithublive.ktclife.Model;

public class SignUpModel {

    public String id, name, mobileno, reference, profile_imgurl, address, plan, password;

    public SignUpModel() {
    }

    public SignUpModel(String id, String name, String mobileno, String reference, String profile_imgurl, String address,
                       String plan, String password) {
        this.id = id;
        this.name = name;
        this.mobileno = mobileno;
        this.reference = reference;
        this.profile_imgurl = profile_imgurl;
        this.address = address;
        this.plan = plan;
        this.password = password;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getMobileno() {
        return mobileno;
    }

    public void setMobileno(String mobileno) {
        this.mobileno = mobileno;
    }

    public String getReference() {
        return reference;
    }

    public void setReference(String reference) {
        this.reference = reference;
    }

    public String getProfile_imgurl() {
        return profile_imgurl;
    }

    public void setProfile_imgurl(String profile_imgurl) {
        this.profile_imgurl = profile_imgurl;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getPlan() {
        return plan;
    }

    public void setPlan(String plan) {
        this.plan = plan;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}