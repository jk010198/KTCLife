package com.ithublive.ktclife.Admin.Adaptor;

import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.graphics.drawable.ColorDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
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
import com.ithublive.ktclife.Admin.Model.MonthlyTargetModel;
import com.ithublive.ktclife.Admin.MonthlyTargetActivity;
import com.ithublive.ktclife.R;
import com.ithublive.ktclife.Utils.BaseUrl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;

public class CommissionListAdaptor extends ArrayAdapter<CommissionReportModel> {

    private Activity context;
    public static List<CommissionReportModel> monthlyTargetList;
    public static String name, add, model_name, model_issue, date;
    Dialog dialog;
    String popupDetails;

    public CommissionListAdaptor(Activity context, List<CommissionReportModel> monthlytargetlist) {
        super(context, R.layout.monthlytarget_listlayout, monthlytargetlist);
        this.context = context;
        this.monthlyTargetList = monthlytargetlist;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        LayoutInflater inflater = context.getLayoutInflater();

        View listview = inflater.inflate(R.layout.commissionreport_listlayout, null, true);

        TextView tv_userid = listview.findViewById(R.id.tv_userid);
        TextView tv_username = listview.findViewById(R.id.tv_name);
        TextView tv_amount = listview.findViewById(R.id.tv_amount);
        TextView tv_ids = listview.findViewById(R.id.tv_ids);

        CircleImageView imageInfo = listview.findViewById(R.id.userInfo);
        CommissionReportModel crm = monthlyTargetList.get(position);

        tv_userid.setText("Userid :- " + crm.userid);
        tv_username.setText("Name :- " + crm.name);
        tv_amount.setText("Amount :- " + crm.amount);
        tv_ids.setText("Id's :- " + crm.ids);

        imageInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getDetails(crm.userid, crm.name, crm.amount, crm.ids);
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

                if (response.contains("#start_op#") && response.contains("#end_op#")) {
                    String res = response.substring(response.indexOf("#start_op#") + 7, response.indexOf("#end_op#"));

                    String[] res_part = res.split("#STOP#");
                    popupDetails = "";
                    for (int i = 0; i < res_part.length; i++) {
                        String user_id = res_part[i].substring(res_part[i].indexOf("#userid#") + 8, res_part[i].indexOf("#direct_ref#"));
                        String direct_ref = res_part[i].substring(res_part[i].indexOf("#direct_ref#") + 12, res_part[i].indexOf("#sec_ref#"));
                        String sec_ref = res_part[i].substring(res_part[i].indexOf("#sec_ref#") + 9, res_part[i].indexOf("#direct_renewal#"));
                        String direct_renew = res_part[i].substring(res_part[i].indexOf("#direct_renewal#") + 16, res_part[i].indexOf("#target#"));
                        String target = res_part[i].substring(res_part[i].indexOf("#target#") + 8, res_part[i].indexOf("#level#"));
                        String level = res_part[i].substring(res_part[i].indexOf("#level#") + 7, res_part[i].indexOf("#date#"));
                        String date = res_part[i].substring(res_part[i].indexOf("#date#") + 6, res_part[i].indexOf("#desc#"));
                        String desc = res_part[i].substring(res_part[i].indexOf("#desc#") + 6, res_part[i].indexOf("#1a#"));

                        if (!(direct_ref.equals("0"))) {
                            popupDetails = popupDetails + "\nDirect Ref Bonus :-" + direct_ref;
                        }

                        if (!(sec_ref.equals("0"))) {
                            popupDetails = popupDetails + "\nSecond Ref Bonus :-" + sec_ref;
                        }

                        if (!(direct_renew.equals("0"))) {
                            popupDetails = popupDetails + "\nDirect Renewal Bonus :-" + direct_renew;
                        }

                        if (!(target.equals("0"))) {
                            popupDetails = popupDetails + "\nTarget Bonus :-" + target;
                        }

                        if (!(level.equals("0"))) {
                            popupDetails = popupDetails + "\nLevel Bonus :-" + level;
                        }

                        popupDetails = popupDetails + "\nDate :-" + date + "\nDescription :-" + desc +
                                "\n-------------------------------------------\n";
                    }
                    dialog.dismiss();

                    //////// Set up POPUP /////////
                    final AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(context);
                    final AlertDialog alert = alertDialogBuilder.create();
                    final LayoutInflater layoutInflater = LayoutInflater.from(context);
                    final View popupInputDialogView = layoutInflater.inflate(R.layout.layout_popup_commissionreport, null);
                    alert.setView(popupInputDialogView);
                    alert.show();
                    alert.setCancelable(false);
                    final TextView tv_popup_leginfo = popupInputDialogView.findViewById(R.id.tv_popupInfoOfLeg);
                    final TextView tv_data = popupInputDialogView.findViewById(R.id.tv_data);

                    tv_popup_leginfo.setText("Userid :- " + id + "\nName :- " + name + "\nAmount :- " + amt
                            + "\nTotal Id's Payment :- " + addedIds);
                    tv_data.setText(popupDetails);
                    ///////////////

                    Button btn_close_Dialog = popupInputDialogView.findViewById(R.id.buttonClosePopupLeginfo);
                    btn_close_Dialog.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            alert.dismiss();
                        }
                    });
                } else {
                    Toast.makeText(context, "error.", Toast.LENGTH_SHORT).show();
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
                map.put("req_for", "CommissionReportDetails");
                map.put("id", id);
                map.put("fm_dt", CommissionReport.etFromDate.getText().toString().trim());
                map.put("to_dt", CommissionReport.etToDate.getText().toString().trim());
                return map;
            }
        };
        Volley.newRequestQueue(context).add(stringRequest);
    }
}