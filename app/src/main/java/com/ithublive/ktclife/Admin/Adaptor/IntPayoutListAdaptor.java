package com.ithublive.ktclife.Admin.Adaptor;

import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.ithublive.ktclife.Admin.CommissionReport;
import com.ithublive.ktclife.Admin.Model.CommissionReportModel;
import com.ithublive.ktclife.Admin.Model.IntPayoutReportModel;
import com.ithublive.ktclife.R;
import com.ithublive.ktclife.Utils.BaseUrl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;

public class IntPayoutListAdaptor extends ArrayAdapter<IntPayoutReportModel> {

    private Activity context;
    public static List<IntPayoutReportModel> monthlyTargetList;
    public static String name, add, model_name, model_issue, date;

    public IntPayoutListAdaptor(Activity context, List<IntPayoutReportModel> monthlytargetlist) {
        super(context, R.layout.monthlytarget_listlayout, monthlytargetlist);
        this.context = context;
        this.monthlyTargetList = monthlytargetlist;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        LayoutInflater inflater = context.getLayoutInflater();

        View listview = inflater.inflate(R.layout.intpayout_listlayout, null, true);

        TextView tv_userid = listview.findViewById(R.id.tv_userid);
        TextView tv_paydate = listview.findViewById(R.id.tv_paydate);
        TextView tv_amount = listview.findViewById(R.id.tv_amount);
        TextView tv_wd_date = listview.findViewById(R.id.tv_wd_date);

        IntPayoutReportModel intpayoutmodel = monthlyTargetList.get(position);

        tv_userid.setText("Userid :- " + intpayoutmodel.userid);
        tv_paydate.setText("Int Credit Date :- " + intpayoutmodel.paydate);
        tv_amount.setText("Amount :- " + intpayoutmodel.amount);
        tv_wd_date.setText("Withdrawal Date :- " + intpayoutmodel.withdrawaldate);

        return listview;
    }
}