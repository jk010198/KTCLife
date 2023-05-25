package com.ithublive.ktclife.Admin.Model;

public class UserDataModel implements Comparable {

    public String id, name, ref_id, ref_name, plan, isPaid, cap_limit, number_of_leg, number_of_direct_leg;

    public UserDataModel() {
    }

    public UserDataModel(String id, String name, String ref_id, String ref_name, String plan, String isPaid, String cap_limit, String number_of_leg, String number_of_direct_leg) {
        this.id = id;
        this.name = name;
        this.ref_id = ref_id;
        this.ref_name = ref_name;
        this.plan = plan;
        this.isPaid = isPaid;
        this.cap_limit = cap_limit;
        this.number_of_leg = number_of_leg;
        this.number_of_direct_leg = number_of_direct_leg;
    }

    @Override
    public int compareTo(Object o) {
        return name.compareTo(((UserDataModel) o).name);
    }


}
