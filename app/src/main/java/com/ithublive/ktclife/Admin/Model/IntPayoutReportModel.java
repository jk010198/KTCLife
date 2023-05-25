package com.ithublive.ktclife.Admin.Model;

public class IntPayoutReportModel {

    public String userid, paydate, amount, withdrawaldate;

    public IntPayoutReportModel() {
    }

    public IntPayoutReportModel(String userid, String paydate, String amount, String withdrawaldate) {
        this.userid = userid;
        this.paydate = paydate;
        this.amount = amount;
        this.withdrawaldate = withdrawaldate;
    }

    public String getUserid() {
        return userid;
    }

    public void setUserid(String userid) {
        this.userid = userid;
    }

    public String getPaydate() {
        return paydate;
    }

    public void setPaydate(String paydate) {
        this.paydate = paydate;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public String getWithdrawaldate() {
        return withdrawaldate;
    }

    public void setWithdrawaldate(String withdrawaldate) {
        this.withdrawaldate = withdrawaldate;
    }
}