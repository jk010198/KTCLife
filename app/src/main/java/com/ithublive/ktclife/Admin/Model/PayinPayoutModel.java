package com.ithublive.ktclife.Admin.Model;

public class PayinPayoutModel {

    public String userid, pay_date, ins_no, amount, pay_mode, withdrawal_date;

    public PayinPayoutModel() {
    }

    public PayinPayoutModel(String userid, String pay_date, String ins_no, String amount, String pay_mode, String withdrawal_date) {
        this.userid = userid;
        this.pay_date = pay_date;
        this.ins_no = ins_no;
        this.amount = amount;
        this.pay_mode = pay_mode;
        this.withdrawal_date = withdrawal_date;
    }

    public String getUserid() {
        return userid;
    }

    public void setUserid(String userid) {
        this.userid = userid;
    }

    public String getPay_date() {
        return pay_date;
    }

    public void setPay_date(String pay_date) {
        this.pay_date = pay_date;
    }

    public String getIns_no() {
        return ins_no;
    }

    public void setIns_no(String ins_no) {
        this.ins_no = ins_no;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public String getPay_mode() {
        return pay_mode;
    }

    public void setPay_mode(String pay_mode) {
        this.pay_mode = pay_mode;
    }

    public String getWithdrawal_date() {
        return withdrawal_date;
    }

    public void setWithdrawal_date(String withdrawal_date) {
        this.withdrawal_date = withdrawal_date;
    }
}