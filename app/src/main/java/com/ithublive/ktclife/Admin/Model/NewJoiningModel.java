package com.ithublive.ktclife.Admin.Model;

public class NewJoiningModel implements Comparable {

    public String id;
    public String name;
    public String mobile;
    public String profile_imgurl;
    public String reference_id;
    public String plan;
    public String is_paid;
    public String address;
    public String password;
    public String date_added;
    public String cap_limit;
    public String r_name;

    public NewJoiningModel() {

    }

    public NewJoiningModel(String id, String name, String mobile, String profile_imgurl, String reference_id, String plan, String is_paid,
                           String address, String password, String date_added, String cap_limit ,String r_name) {
        this.id = id;
        this.name = name;
        this.mobile = mobile;
        this.profile_imgurl = profile_imgurl;
        this.reference_id = reference_id;
        this.plan = plan;
        this.is_paid = is_paid;
        this.address = address;
        this.password = password;
        this.date_added = date_added;
        this.cap_limit = cap_limit;
        this.r_name=r_name;
    }

    @Override
    public int compareTo(Object o) {
        return name.compareTo(((NewJoiningModel) o).name);
    }

}