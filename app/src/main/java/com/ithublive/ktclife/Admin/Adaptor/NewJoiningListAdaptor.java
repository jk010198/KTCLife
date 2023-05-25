package com.ithublive.ktclife.Admin.Adaptor;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.ithublive.ktclife.Admin.Model.NewJoiningModel;
import com.ithublive.ktclife.R;

import java.util.List;

public class NewJoiningListAdaptor extends ArrayAdapter<NewJoiningModel> {

    private Activity context;
    public static List<NewJoiningModel> ordertlist;
    public static String name, add, model_name, model_issue, date;

    public NewJoiningListAdaptor(Activity context, List<NewJoiningModel> orderlist) {
        super(context, R.layout.wallethistory_listlayout, orderlist);
        this.context = context;
        this.ordertlist = orderlist;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        LayoutInflater inflater = context.getLayoutInflater();

        View listview = inflater.inflate(R.layout.newjoining_listlayout, null, true);

        TextView tv_userid = listview.findViewById(R.id.tv_userid);
        TextView tv_username = listview.findViewById(R.id.tv_username);
        TextView tv_plan = listview.findViewById(R.id.tv_plan);
        TextView tv_doj = listview.findViewById(R.id.tv_doj);
        TextView tv_referername = listview.findViewById(R.id.tv_referername);

        NewJoiningModel njm = ordertlist.get(position);

        tv_userid.setText("Userid :- "+njm.id);
        tv_username.setText("Username :- "+njm.name);
        tv_plan.setText("Plan :- "+njm.plan);
        tv_doj.setText("Date of Joining :- "+njm.date_added);
        tv_referername.setText("Referer :- " + njm.r_name + " (" + njm.reference_id + ")");

        return listview;
    }
}