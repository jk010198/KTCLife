package com.ithublive.ktclife.Admin;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.ithublive.ktclife.R;
import com.ithublive.ktclife.Utils.BaseUrl;

import java.util.HashMap;
import java.util.Map;

import static android.content.Intent.ACTION_DIAL;

public class PendingPayementsReport extends AppCompatActivity {

    ListView listView;
    String arr_content[];
    Dialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pending_payements_report);

        listView = findViewById(R.id.listView_pending_payments_32Days);
        dialog = new Dialog(PendingPayementsReport.this);
        dialog.setContentView(R.layout.layout_progress_dialog);
        if (dialog.getWindow() != null) {
            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(0));
        }
        dialog.setCancelable(false);
        dialog.show();

        getPaymentsDetails();
    }

    private void getPaymentsDetails() {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, BaseUrl.ktc_admin_reportsUrl,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        dialog.dismiss();
                        if (response.contains("conn_error") && response.length() < 20) {
                            Toast.makeText(PendingPayementsReport.this, "DB Connection error. Please try again later.", Toast.LENGTH_SHORT).show();
                        } else if (response.contains("#L_32#") && response.contains("#U1#")) {
                            response = response.replace("#L_32#", "");
                            response = response.replace("#eL_32#", "");
                            String arr[] = response.split("#ERU#");
                            if (arr.length > 0) {
                                arr_content = new String[arr.length];
                                for (int i = 0; i < arr.length; i++) {
                                    arr_content[i] = (arr[i].substring(arr[i].indexOf("#U1#") + 4, arr[i].indexOf("#U2#")) + "(" +
                                            arr[i].substring(arr[i].indexOf("#U2#") + 4, arr[i].indexOf("#U3#")) + ")\nLast Paid Date: " +
                                            arr[i].substring(arr[i].indexOf("#U3#") + 4, arr[i].indexOf("#U4#")) + " InstallmentNo: " +
                                            arr[i].substring(arr[i].indexOf("#U4#") + 4, arr[i].indexOf("#U5#")) + "\n Mobile: " +
                                            arr[i].substring(arr[i].indexOf("#U5#") + 4, arr[i].indexOf("#U6#")) + "  Plan:" +
                                            arr[i].substring(arr[i].indexOf("#U6#") + 4, arr[i].indexOf("#U7#")));
                                }
                                ArrayAdapter adapter = new ArrayAdapter(PendingPayementsReport.this, android.R.layout.simple_list_item_1, arr_content);
                                listView.setAdapter(adapter);
                                listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                                    @Override
                                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                                        String mobile = arr[position].substring(arr[position].indexOf("#U5#") + 4, arr[position].indexOf("#U6#"));
                                        startActivity(new Intent(ACTION_DIAL, Uri.parse("tel:" + mobile)));
                                    }
                                });
                            } else {
                                Toast.makeText(PendingPayementsReport.this, "No Data found.", Toast.LENGTH_SHORT).show();
                            }
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        dialog.dismiss();
                        Toast.makeText(PendingPayementsReport.this, "Error...", Toast.LENGTH_SHORT).show();
                    }
                }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map map = new HashMap();
                map.put("req_for", "since_32_daysNotPaid_rpt_toCall");
                return map;
            }
        };
        Volley.newRequestQueue(PendingPayementsReport.this).add(stringRequest);
    }
}
//   select * from
//(
//select user_id,ktc_users.name , max(pay_date) as md, max(installment_no)
//from ktc_plan_payments join ktc_users on ktc_plan_payments.user_id=ktc_users.id
//
//
//group by user_id,ktc_users.name
//) as abcd
//
//where datediff(now(),md)>32
