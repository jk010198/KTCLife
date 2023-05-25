package com.ithublive.ktclife.Admin.Adaptor;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.ithublive.ktclife.Admin.Model.UserDataModel;
import com.ithublive.ktclife.Admin.UpdateUserDataActivity;
import com.ithublive.ktclife.R;

import java.util.List;

public class UserDataListAdaptor extends RecyclerView.Adapter<UserDataListAdaptor.MyViewHolder> {

    public Context context;
    public List<UserDataModel> mFilteredList;
    public List<UserDataModel> modelList;

    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        TextView tv_name, tv_userid;
        RelativeLayout rl_userData;

        public MyViewHolder(View view) {
            super(view);
            this.tv_name = (TextView) view.findViewById(R.id.tv_username);
            this.tv_userid = (TextView) view.findViewById(R.id.tv_userid);
            this.rl_userData = view.findViewById(R.id.relativelayout_userData);
        }

        public void onClick(View view) {
            int id = view.getId();
            int adapterPosition = getAdapterPosition();
        }
    }

    public UserDataListAdaptor(List<UserDataModel> list, Context context2) {
        this.modelList = list;
        this.mFilteredList = list;
    }

    public MyViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View inflate = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.userdata_listlayout, viewGroup, false);
        this.context = viewGroup.getContext();
        return new MyViewHolder(inflate);
    }

    public void onBindViewHolder(final MyViewHolder myViewHolder, int i) {
        final UserDataModel userDataModel = (UserDataModel) this.modelList.get(i);
        myViewHolder.tv_name.setText(userDataModel.name);
        myViewHolder.tv_userid.setText(userDataModel.id);

        myViewHolder.rl_userData.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                viewCustomersData(userDataModel.id, userDataModel.name, userDataModel.ref_id, userDataModel.ref_name, userDataModel.plan, userDataModel.isPaid
                        , userDataModel.cap_limit, userDataModel.number_of_leg, userDataModel.number_of_direct_leg);
            }
        });
    }

    public int getItemCount() {
        return this.mFilteredList.size();
    }

    public void viewCustomersData(String id, String name, String ref_id, String ref_name, String plan, String ispaid, String caplimit, String number_legs,
                                  String number_direct_leg) {
        Intent intent = new Intent(context, UpdateUserDataActivity.class);
        intent.putExtra("userid", id);
        intent.putExtra("name", name);
        intent.putExtra("refid", ref_id);
        intent.putExtra("refname", ref_name);
        intent.putExtra("plan", plan);
        intent.putExtra("ispaid", ispaid);
        intent.putExtra("caplimit", caplimit);
        intent.putExtra("number_legs", number_legs);
        intent.putExtra("number_direct_legs", number_direct_leg);
        context.startActivity(intent);
    }
}