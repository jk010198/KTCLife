package com.ithublive.ktclife.Admin.Model;

public class PayoutReportModel implements Comparable {

    public String id, pay_date, installment_no, amount, pay_mode, pay_id, name, img_url, address, paid_bonus;

    public PayoutReportModel() {
    }

    public PayoutReportModel(String id, String pay_date, String installment_no, String amount, String pay_mode, String pay_id, String name,
                             String img_url, String address,String paid_bonus) {
        this.id = id;
        this.pay_date = pay_date;
        this.installment_no = installment_no;
        this.amount = amount;
        this.pay_mode = pay_mode;
        this.pay_id = pay_id;
        this.name = name;
        this.img_url = img_url;
        this.address = address;
        this.paid_bonus = paid_bonus;
    }

    @Override
    public int compareTo(Object o) {
        return name.compareTo(((PayoutReportModel) o).name);
    }
}