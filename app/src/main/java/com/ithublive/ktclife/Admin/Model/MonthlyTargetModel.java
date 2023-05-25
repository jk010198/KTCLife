package com.ithublive.ktclife.Admin.Model;

public class MonthlyTargetModel {

    public String ref_id, ref_name, added_id, amount;

    public MonthlyTargetModel() {
    }

    public MonthlyTargetModel(String ref_id, String ref_name, String added_id, String amount) {
        this.ref_id = ref_id;
        this.ref_name = ref_name;
        this.added_id = added_id;
        this.amount = amount;
    }

    public String getRef_id() {
        return ref_id;
    }

    public void setRef_id(String ref_id) {
        this.ref_id = ref_id;
    }

    public String getRef_name() {
        return ref_name;
    }

    public void setRef_name(String ref_name) {
        this.ref_name = ref_name;
    }

    public String getAdded_id() {
        return added_id;
    }

    public void setAdded_id(String added_id) {
        this.added_id = added_id;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }
}