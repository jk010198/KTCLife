package com.ithublive.ktclife.Admin.Model;

public class TotalUsersListModel implements Comparable {

    public String id, name, mobile_number, profile_photo, reference_id, plan, paid, address, doj, kyc, aadhar_number, aadhar_front_photo,
            aadhar_back_photo, bank_ac_photo, bank_ac_number, bank_ac_ifsc, cap_limit, no_of_leg, password;

    public TotalUsersListModel() {
    }

    public TotalUsersListModel(String id, String name, String mobile_number, String profile_photo, String reference_id, String plan, String paid,
                               String address, String doj, String kyc, String aadhar_number, String aadhar_front_photo, String aadhar_back_photo,
                               String bank_ac_photo, String bank_ac_number, String bank_ac_ifsc, String cap_limit, String no_of_leg, String password) {
        this.id = id;
        this.name = name;
        this.mobile_number = mobile_number;
        this.profile_photo = profile_photo;
        this.reference_id = reference_id;
        this.plan = plan;
        this.paid = paid;
        this.address = address;
        this.doj = doj;
        this.kyc = kyc;
        this.aadhar_number = aadhar_number;
        this.aadhar_front_photo = aadhar_front_photo;
        this.aadhar_back_photo = aadhar_back_photo;
        this.bank_ac_photo = bank_ac_photo;
        this.bank_ac_number = bank_ac_number;
        this.bank_ac_ifsc = bank_ac_ifsc;
        this.cap_limit = cap_limit;
        this.no_of_leg = no_of_leg;
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

    public String getMobile_number() {
        return mobile_number;
    }

    public void setMobile_number(String mobile_number) {
        this.mobile_number = mobile_number;
    }

    public String getProfile_photo() {
        return profile_photo;
    }

    public void setProfile_photo(String profile_photo) {
        this.profile_photo = profile_photo;
    }

    public String getReference_id() {
        return reference_id;
    }

    public void setReference_id(String reference_id) {
        this.reference_id = reference_id;
    }

    public String getPlan() {
        return plan;
    }

    public void setPlan(String plan) {
        this.plan = plan;
    }

    public String getPaid() {
        return paid;
    }

    public void setPaid(String paid) {
        this.paid = paid;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getDoj() {
        return doj;
    }

    public void setDoj(String doj) {
        this.doj = doj;
    }

    public String getKyc() {
        return kyc;
    }

    public void setKyc(String kyc) {
        this.kyc = kyc;
    }

    public String getAadhar_number() {
        return aadhar_number;
    }

    public void setAadhar_number(String aadhar_number) {
        this.aadhar_number = aadhar_number;
    }

    public String getAadhar_front_photo() {
        return aadhar_front_photo;
    }

    public void setAadhar_front_photo(String aadhar_front_photo) {
        this.aadhar_front_photo = aadhar_front_photo;
    }

    public String getAadhar_back_photo() {
        return aadhar_back_photo;
    }

    public void setAadhar_back_photo(String aadhar_back_photo) {
        this.aadhar_back_photo = aadhar_back_photo;
    }

    public String getBank_ac_photo() {
        return bank_ac_photo;
    }

    public void setBank_ac_photo(String bank_ac_photo) {
        this.bank_ac_photo = bank_ac_photo;
    }

    public String getBank_ac_number() {
        return bank_ac_number;
    }

    public void setBank_ac_number(String bank_ac_number) {
        this.bank_ac_number = bank_ac_number;
    }

    public String getBank_ac_ifsc() {
        return bank_ac_ifsc;
    }

    public void setBank_ac_ifsc(String bank_ac_ifsc) {
        this.bank_ac_ifsc = bank_ac_ifsc;
    }

    public String getCap_limit() {
        return cap_limit;
    }

    public void setCap_limit(String cap_limit) {
        this.cap_limit = cap_limit;
    }

    public String getNo_of_leg() {
        return no_of_leg;
    }

    public void setNo_of_leg(String no_of_leg) {
        this.no_of_leg = no_of_leg;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    @Override
    public int compareTo(Object o) {
        return name.compareTo(((TotalUsersListModel) o).name);
    }
}