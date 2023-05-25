package com.ithublive.ktclife.Admin.Adaptor;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.ithublive.ktclife.Admin.Model.PayoutReportModel;
import com.ithublive.ktclife.Admin.Model.PendingUsersKycModel;
import com.ithublive.ktclife.AdminUpdatePayments;
import com.ithublive.ktclife.R;
import com.ithublive.ktclife.Utils.BaseUrl;

import java.util.List;

public class PendingKycListAdaptor extends ArrayAdapter<PendingUsersKycModel> {

    private Activity context;
    public static List<PendingUsersKycModel> pendingUsersKycModelList;
    public static String name, add, model_issue, date;

    public PendingKycListAdaptor(Activity context, List<PendingUsersKycModel> pendingUsersKycModelList1) {
        super(context, R.layout.payoutreport_listlayout, pendingUsersKycModelList1);
        this.context = context;
        this.pendingUsersKycModelList = pendingUsersKycModelList1;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        LayoutInflater inflater = context.getLayoutInflater();

        View listview = inflater.inflate(R.layout.pendingkyc_listlayout, null, true);

        TextView tv_userid = listview.findViewById(R.id.tv_userid);
        TextView tv_username = listview.findViewById(R.id.tv_name);
        TextView tv_mobile = listview.findViewById(R.id.tv_mobile);
        TextView tv_doj = listview.findViewById(R.id.tv_doj);
        TextView tv_kyc = listview.findViewById(R.id.tv_kyc);

        PendingUsersKycModel pukm = pendingUsersKycModelList.get(position);

        tv_userid.setText("Userid :- " + pukm.id);
        tv_username.setText("Name :- " + pukm.name);
        tv_mobile.setText("Mobile :- " + pukm.mobile_number);
        tv_doj.setText("Date of Joining :- " + pukm.doj);
        tv_kyc.setText("KYC :- " + pukm.getKYC());
        return listview;
    }
}