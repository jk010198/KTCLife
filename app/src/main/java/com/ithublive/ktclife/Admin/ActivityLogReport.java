package com.ithublive.ktclife.Admin;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import android.widget.Toast;

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

public class ActivityLogReport extends AppCompatActivity {
    TextView textviewLogActivity99282;
    Dialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_log_report);

        textviewLogActivity99282 = findViewById(R.id.textviewLogActivity99282);

        dialog = new Dialog(ActivityLogReport.this);
        dialog.setContentView(R.layout.layout_progress_dialog);
        if (dialog.getWindow() != null) {
            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(0));
        }
        dialog.setCancelable(false);
        dialog.show();

        getActivityLog();
    }

    private void getActivityLog() {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, BaseUrl.ktc_admin_reportsUrl,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        dialog.dismiss();
                        if (response.contains("conn_error")) {
                            Toast.makeText(ActivityLogReport.this, "DB Connection error. Please try again later.", Toast.LENGTH_SHORT).show();
                        } else {
                            textviewLogActivity99282.setText(response);
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        dialog.dismiss();
                        Toast.makeText(ActivityLogReport.this, "Error...", Toast.LENGTH_SHORT).show();
                    }
                }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map map = new HashMap();
                map.put("req_for", "activitylogReq");
                return map;
            }
        };
        Volley.newRequestQueue(ActivityLogReport.this).add(stringRequest);
    }
}