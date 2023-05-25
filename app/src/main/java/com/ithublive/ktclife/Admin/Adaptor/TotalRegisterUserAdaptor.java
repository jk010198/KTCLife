package com.ithublive.ktclife.Admin.Adaptor;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.recyclerview.widget.RecyclerView;

import com.ithublive.ktclife.Admin.Model.TotalUsersListModel;
import com.ithublive.ktclife.Admin.Model.UserDataModel;
import com.ithublive.ktclife.Admin.TotalRegisterUsersListReport;
import com.ithublive.ktclife.Admin.UpdateUserDataActivity;
import com.ithublive.ktclife.R;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class TotalRegisterUserAdaptor extends RecyclerView.Adapter<TotalRegisterUserAdaptor.MyViewHolder> {

    public ArrayList<TotalUsersListModel> listData;
    Context context;

    public TotalRegisterUserAdaptor(ArrayList<TotalUsersListModel> arrayListData, Context context) {
        this.listData = arrayListData;
        this.context = context;
    }

    public MyViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.userdata_listlayout, viewGroup, false);
        return new MyViewHolder(view);
    }

    public void onBindViewHolder(final MyViewHolder myViewHolder, int position) {
        final TotalUsersListModel totalUsersListModel = listData.get(position);
        myViewHolder.tv_name.setText("Username :- " + totalUsersListModel.name);
        myViewHolder.tv_userid.setText("UserId :- " + totalUsersListModel.id);
        if (totalUsersListModel.kyc.equals("")) {
            myViewHolder.tv_kyc.setText("KYC :- No");
        } else {
            myViewHolder.tv_kyc.setText("KYC :- " + totalUsersListModel.kyc);
        }

        if (totalUsersListModel.paid.toLowerCase().equals("yes")){
            myViewHolder.rl_userData.setBackgroundColor(Color.parseColor("#E8F6E9"));
        } else {
            myViewHolder.rl_userData.setBackgroundColor(Color.parseColor("#FFCCCB"));
        }

        myViewHolder.rl_userData.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                viewCustomersData(totalUsersListModel.id, totalUsersListModel.name, totalUsersListModel.mobile_number, totalUsersListModel.profile_photo,
                        totalUsersListModel.reference_id, totalUsersListModel.plan, totalUsersListModel.paid, totalUsersListModel.address,
                        totalUsersListModel.doj, totalUsersListModel.kyc, totalUsersListModel.aadhar_number, totalUsersListModel.aadhar_front_photo,
                        totalUsersListModel.aadhar_back_photo, totalUsersListModel.bank_ac_photo, totalUsersListModel.bank_ac_number,
                        totalUsersListModel.bank_ac_ifsc, totalUsersListModel.cap_limit, totalUsersListModel.no_of_leg, totalUsersListModel.password);
            }
        });
    }

    public void viewCustomersData(String id, String name, String mobile, String profileUrl, String ref_id, String plan, String ispaid, String address,
                                  String doj, String kyc, String aadhar_number, String aadhar_front_url, String aadhar_back_url, String bank_ac_url,
                                  String bank_ac_number, String bank_ac_ifsc, String cap_limit, String no_of_leg, String password) {
        Intent intent = new Intent(context, UpdateUserDataActivity.class);
        intent.putExtra("userid", id);
        intent.putExtra("name", name);
        intent.putExtra("mobile", mobile);
        intent.putExtra("profileUrl", profileUrl);
        intent.putExtra("refid", ref_id);
        intent.putExtra("plan", plan);
        intent.putExtra("ispaid", ispaid);
        intent.putExtra("address", address);
        intent.putExtra("doj", doj);
        intent.putExtra("kyc", kyc);
        intent.putExtra("aadhar_number", aadhar_number);
        intent.putExtra("aadhar_front_url", aadhar_front_url);
        intent.putExtra("aadhar_back_url", aadhar_back_url);
        intent.putExtra("bank_ac_url", bank_ac_url);
        intent.putExtra("bank_ac_number", bank_ac_number);
        intent.putExtra("bank_ac_ifsc", bank_ac_ifsc);
        intent.putExtra("cap_limit", cap_limit);
        intent.putExtra("no_of_leg", no_of_leg);
        intent.putExtra("password", password);
        context.startActivity(intent);
    }

    public int getItemCount() {
        return this.listData.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        TextView tv_name, tv_userid, tv_kyc;
        RelativeLayout rl_userData;

        public MyViewHolder(View view) {
            super(view);
            this.tv_name = (TextView) view.findViewById(R.id.tv_username);
            this.tv_userid = (TextView) view.findViewById(R.id.tv_userid);
            this.tv_kyc = (TextView) view.findViewById(R.id.tv_kyc);
            this.rl_userData = view.findViewById(R.id.relativelayout_userData);
        }
    }
}