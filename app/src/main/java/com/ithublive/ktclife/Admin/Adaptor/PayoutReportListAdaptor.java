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
import com.ithublive.ktclife.AdminUpdatePayments;
import com.ithublive.ktclife.R;
import com.ithublive.ktclife.Utils.BaseUrl;

import java.util.List;

public class PayoutReportListAdaptor extends ArrayAdapter<PayoutReportModel> {

    private Activity context;
    public static List<PayoutReportModel> ordertlist;
    public static String name, add, model_name, model_issue, date;

    public PayoutReportListAdaptor(Activity context, List<PayoutReportModel> orderlist) {
        super(context, R.layout.payoutreport_listlayout, orderlist);
        this.context = context;
        this.ordertlist = orderlist;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        LayoutInflater inflater = context.getLayoutInflater();

        View listview = inflater.inflate(R.layout.payoutreport_listlayout, null, true);

        TextView tv_userid = listview.findViewById(R.id.tv_userid);
        TextView tv_username = listview.findViewById(R.id.tv_name7933);
        TextView tv_amount = listview.findViewById(R.id.tv_amount);
        TextView tv_installment = listview.findViewById(R.id.tv_installmentno);
        TextView tv_paymode = listview.findViewById(R.id.tv_paymode8392);
        TextView tv_datetime = listview.findViewById(R.id.tv_datetime);
        TextView tv_paid_bonus = listview.findViewById(R.id.tv_paid_bonus);
        ImageButton imagePayBonus = listview.findViewById(R.id.imageButton_listPayout392);
        PayoutReportModel njm = ordertlist.get(position);

        tv_userid.setText("Userid :- " + njm.id);
        tv_username.setText("Name :- " + njm.name);
        tv_amount.setText("Amount :- " + njm.amount);
        tv_installment.setText("Installment No :- " + njm.installment_no);
        tv_paymode.setText("Pay mode:" + njm.pay_mode);
        tv_datetime.setText("Date time :- " + njm.pay_date);
        tv_paid_bonus.setText("Bonus Paid :- " + njm.paid_bonus);
        if (njm.paid_bonus.equals("no")) {
            tv_paid_bonus.setTextColor(Color.RED);
        } else if (njm.paid_bonus.equals("yes")) {
            tv_paid_bonus.setTextColor(Color.GREEN);
        }
        imagePayBonus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (njm.paid_bonus.equals("yes")) {
                    Animation anim;
                    anim = new AlphaAnimation(0.0f, 1.0f);
                    anim.setDuration(50); //You can manage the blinking time with this parameter
                    anim.setStartOffset(20);
                    anim.setRepeatMode(Animation.REVERSE);
                    anim.setRepeatCount(Animation.INFINITE);
                    imagePayBonus.startAnimation(anim);
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            imagePayBonus.clearAnimation();
                        }
                    }, 400);
                    Toast.makeText(context, "Bonus already paid..." + njm.id, Toast.LENGTH_SHORT).show();
                }
                else if (njm.paid_bonus.equals("no")) {
                    Toast.makeText(context, "Pay Bonus to " + njm.id, Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(context, AdminUpdatePayments.class);
                    intent.putExtra("u_info_id", njm.id);
                    intent.putExtra("u_installment_num", njm.installment_no);
                    BaseUrl.payoutReportModel = njm;
                    context.startActivity(intent);
                }
            }
        });
        return listview;
    }
}