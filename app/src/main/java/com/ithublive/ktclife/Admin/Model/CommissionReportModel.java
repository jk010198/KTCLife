package com.ithublive.ktclife.Admin.Model;

public class CommissionReportModel {

    public String userid, amount, date, name, ids;

    public CommissionReportModel() {
    }

    public CommissionReportModel(String userid, String amount, String date, String name, String ids) {
        this.userid = userid;
        this.amount = amount;
        this.date = date;
        this.name = name;
        this.ids = ids;
    }

    public String getUserid() {
        return userid;
    }

    public void setUserid(String userid) {
        this.userid = userid;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getIds() {
        return ids;
    }

    public void setIds(String ids) {
        this.ids = ids;
    }
}