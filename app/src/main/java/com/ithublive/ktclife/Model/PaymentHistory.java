package com.ithublive.ktclife.Model;

public class PaymentHistory {

    public String id, amount, installment_no, withdrawal_date, plan_paid_date;

    public PaymentHistory() {
    }

    public PaymentHistory(String id, String amount, String installment_no, String withdrawal_date, String plan_paid_date) {
        this.id = id;
        this.amount = amount;
        this.installment_no = installment_no;
        this.withdrawal_date = withdrawal_date;
        this.plan_paid_date = plan_paid_date;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public String getInstallment_no() {
        return installment_no;
    }

    public void setInstallment_no(String installment_no) {
        this.installment_no = installment_no;
    }

    public String getWithdrawal_date() {
        return withdrawal_date;
    }

    public void setWithdrawal_date(String withdrawal_date) {
        this.withdrawal_date = withdrawal_date;
    }

    public String getPlan_paid_date() {
        return plan_paid_date;
    }

    public void setPlan_paid_date(String plan_paid_date) {
        this.plan_paid_date = plan_paid_date;
    }
}

