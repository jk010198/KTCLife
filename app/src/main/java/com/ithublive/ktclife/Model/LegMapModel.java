package com.ithublive.ktclife.Model;

public class LegMapModel {

    public String userid, image_url, isPaid;

    public LegMapModel() {
    }

    public LegMapModel(String userid, String image_url, String isPaid) {
        this.userid = userid;
        this.image_url = image_url;
        this.isPaid = isPaid;
    }
}