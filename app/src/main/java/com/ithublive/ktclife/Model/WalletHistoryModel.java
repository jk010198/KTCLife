package com.ithublive.ktclife.Model;

public class WalletHistoryModel {

    public String userid, ben_id, ben_name, date, amount, notes, user_name;

    public WalletHistoryModel() {
    }

    public WalletHistoryModel(String userid, String ben_id, String ben_name, String date, String amount, String notes, String user_name) {
        this.userid = userid;
        this.ben_id = ben_id;
        this.ben_name = ben_name;
        this.date = date;
        this.amount = amount;
        this.notes = notes;
        this.user_name = user_name;
    }

    public String getUserid() {
        return userid;
    }

    public void setUserid(String userid) {
        this.userid = userid;
    }

    public String getBen_id() {
        return ben_id;
    }

    public void setBen_id(String ben_id) {
        this.ben_id = ben_id;
    }

    public String getBen_name() {
        return ben_name;
    }

    public void setBen_name(String ben_name) {
        this.ben_name = ben_name;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public String getUser_name() {
        return user_name;
    }

    public void setUser_name(String user_name) {
        this.user_name = user_name;
    }
}