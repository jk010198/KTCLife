package com.ithublive.ktclife.Admin.Adaptor;

import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ListAdapter;
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
import com.ithublive.ktclife.Admin.Model.MonthlyTargetModel;
import com.ithublive.ktclife.Admin.Model.PayoutReportModel;
import com.ithublive.ktclife.Admin.MonthlyTargetActivity;
import com.ithublive.ktclife.AdminUpdatePayments;
import com.ithublive.ktclife.R;
import com.ithublive.ktclife.Utils.BaseUrl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;

public class MonthlyTargetListAdaptor extends ArrayAdapter<MonthlyTargetModel> {

    private Activity context;
    public static List<MonthlyTargetModel> monthlyTargetList;
    public static String name, add, model_name, model_issue, date;
    Dialog dialog;
    String popupDetails;

    public MonthlyTargetListAdaptor(Activity context, List<MonthlyTargetModel> monthlytargetlist) {
        super(context, R.layout.monthlytarget_listlayout, monthlytargetlist);
        this.context = context;
        this.monthlyTargetList = monthlytargetlist;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        LayoutInflater inflater = context.getLayoutInflater();

        View listview = inflater.inflate(R.layout.monthlytarget_listlayout, null, true);

        TextView tv_userid = listview.findViewById(R.id.tv_refid);
        TextView tv_username = listview.findViewById(R.id.tv_refname);
        TextView tv_amount = listview.findViewById(R.id.tv_amount);
        TextView tv_ids = listview.findViewById(R.id.tv_added_ids);

        CircleImageView imageInfo = listview.findViewById(R.id.userInfo);
        MonthlyTargetModel mtm = monthlyTargetList.get(position);

        tv_userid.setText("Reference Id :- " + mtm.ref_id);
        tv_username.setText("Reference Name :- " + mtm.ref_name);
        tv_amount.setText("Amount :- " + mtm.amount);
        tv_ids.setText("Id Added :- " + mtm.added_id);

        imageInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getDetails(mtm.ref_id, mtm.ref_name, mtm.amount, mtm.added_id);
            }
        });

        return listview;
    }

    public void getDetails(String id, String name, String amt, String addedIds) {

        dialog = new Dialog(context);
        dialog.setContentView(R.layout.layout_progress_dialog);
        if (dialog.getWindow() != null) {
            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(0));
        }
        dialog.setCancelable(false);
        dialog.show();

        StringRequest stringRequest = new StringRequest(Request.Method.POST, BaseUrl.ktc_admin_reportsUrl, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                if (response.contains("conn_error")) {
                    dialog.dismiss();
                    Toast.makeText(context, "Server error. Try Again", Toast.LENGTH_SHORT).show();
                }

                if (response.contains("no_tr_found")) {
                    Toast.makeText(context, "No transaction found.", Toast.LENGTH_SHORT).show();
                    dialog.dismiss();
                }

                if (response.contains("#streg#") && response.contains("#enreg#")) {
                    String res = response.substring(response.indexOf("#streg#") + 7, response.indexOf("#enreg#"));

                    String[] res_part = res.split("#ERX#");
                    popupDetails = "";
                    for (int i = 0; i < res_part.length; i++) {
                        String user_id = res_part[i].substring(res_part[i].indexOf("#userId#") + 8, res_part[i].indexOf("#ref_id#"));
                        String user_name = res_part[i].substring(res_part[i].indexOf("#name#") + 6, res_part[i].indexOf("#amt#"));
                        String amt = res_part[i].substring(res_part[i].indexOf("#amt#") + 5, res_part[i].indexOf("#paydate#"));
                        String pay_date = res_part[i].substring(res_part[i].indexOf("#paydate#") + 9, res_part[i].indexOf("#9a#"));
                        popupDetails = popupDetails + user_id + "\t\t" + user_name + "\t\t" + amt + "\t\t" + pay_date + "\n";
                    }
                    dialog.dismiss();

                    //////// Set up POPUP
                    final AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(context);
                    final AlertDialog alert = alertDialogBuilder.create();
                    final LayoutInflater layoutInflater = LayoutInflater.from(context);
                    final View popupInputDialogView = layoutInflater.inflate(R.layout.layout_popup_monthlytarget, null);
                    alert.setView(popupInputDialogView);
                    alert.show();
                    alert.setCancelable(false);
                    final TextView tv_popup_leginfo = popupInputDialogView.findViewById(R.id.tv_popupInfoOfLeg);
                    final TextView tv_data = popupInputDialogView.findViewById(R.id.tv_data);

                    tv_popup_leginfo.setText("Reference Id :- " + id + "\n\nReference Name :- " + name + "\n\nAmount :- " + amt
                            + "\n\nTotal Id Join :- " + addedIds);
                    tv_data.setText(popupDetails);
                    ////////////////////

                    Button btn_close_Dialog = popupInputDialogView.findViewById(R.id.buttonClosePopupLeginfo);
                    ImageButton imageButton = popupInputDialogView.findViewById(R.id.btn_dialog_ktc_close);
                    imageButton.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            alert.dismiss();
                        }
                    });
                    btn_close_Dialog.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            alert.dismiss();
                        }
                    });

                } else {
                    Toast.makeText(context, "else", Toast.LENGTH_SHORT).show();
                    dialog.dismiss();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(context, "Server error.", Toast.LENGTH_SHORT).show();
                dialog.dismiss();
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> map = new HashMap<>();
                map.put("req_for", "getDetails");
                map.put("id", id);
                map.put("fm_dt", MonthlyTargetActivity.etFromDate.getText().toString().trim());
                map.put("to_dt", MonthlyTargetActivity.etToDate.getText().toString().trim());
                return map;
            }
        };
        Volley.newRequestQueue(context).add(stringRequest);
    }
}