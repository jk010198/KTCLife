package com.ithublive.ktclife.Admin;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Dialog;
import android.app.Service;
import android.content.SharedPreferences;
import android.graphics.drawable.ColorDrawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.ithublive.ktclife.Adaptor.PaymentHistoryListAdaptor;
import com.ithublive.ktclife.Admin.Adaptor.PaymentHistoryReportListAdaptor;
import com.ithublive.ktclife.Model.PaymentHistory;
import com.ithublive.ktclife.PaymentHistoryList;
import com.ithublive.ktclife.R;
import com.ithublive.ktclife.Utils.BaseUrl;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketAddress;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PaymentHistoryReport extends AppCompatActivity {

    Dialog dialog;
    RecyclerView rv_paymenthistory;
    TextView tv_no_history;
    List<PaymentHistory> list_paymenthistory;
    ArrayList<PaymentHistory> all_paymenthistory;
    PaymentHistoryReportListAdaptor paymentHistoryReportListAdaptor;
    public static String intent_id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment_history_report);

        intent_id = getIntent().getStringExtra("u_id");
        rv_paymenthistory = findViewById(R.id.recyclerview_paymenthistory);
        tv_no_history = findViewById(R.id.tv_no_payment_history);
        list_paymenthistory = new ArrayList<>();
        all_paymenthistory = new ArrayList<>();

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(PaymentHistoryReport.this);
        rv_paymenthistory.setLayoutManager(linearLayoutManager);

        dialog = new Dialog(PaymentHistoryReport.this);
        dialog.setContentView(R.layout.layout_progress_dialog);
        if (dialog.getWindow() != null) {
            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(0));
        }
        dialog.setCancelable(false);

        internetChecking internetChecking = new internetChecking();
        internetChecking.execute();
    }

    public void mainMethod() {
        dialog.show();
        list_paymenthistory.clear();

        StringRequest stringRequest = new StringRequest(Request.Method.POST, BaseUrl.paymentsHistoryUrl, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                if (response.contains("conn_error")) {
                    dialog.dismiss();
                    Toast.makeText(PaymentHistoryReport.this, "Server error. Try Again", Toast.LENGTH_SHORT).show();
                }

                if (response.contains("#end_count#")) {

                    String res = response.substring(response.indexOf("#end_count#") + 11, response.indexOf("details_op"));
                    String[] res_part = res.split("#breakk#");

                    for (int i = 0; i < res_part.length; i++) {
                        String installment_no = res_part[i].substring(res_part[i].indexOf("#inst_no#") + 9, res_part[i].indexOf("#pay_dt#"));
                        String pay_date = res_part[i].substring(res_part[i].indexOf("#pay_dt#") + 8, res_part[i].indexOf("#pay_amt#"));
                        String amount = res_part[i].substring(res_part[i].indexOf("#pay_amt#") + 9, res_part[i].indexOf("#withdra_dt#"));
                        String withdrawal_date = res_part[i].substring(res_part[i].indexOf("#withdra_dt#") + 12, res_part[i].indexOf("#endd#"));

                        if (installment_no.equals("1")) {
                            list_paymenthistory.add(new PaymentHistory("", amount, installment_no, withdrawal_date, pay_date));
                        }
                    }

                    paymentHistoryReportListAdaptor = new PaymentHistoryReportListAdaptor(list_paymenthistory, PaymentHistoryReport.this);
                    rv_paymenthistory.setAdapter(paymentHistoryReportListAdaptor);
                    paymentHistoryReportListAdaptor.notifyDataSetChanged();
                    dialog.dismiss();
                } else {
                    dialog.dismiss();
                    tv_no_history.setVisibility(View.VISIBLE);
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(PaymentHistoryReport.this, "Server error", Toast.LENGTH_SHORT).show();
                dialog.dismiss();
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> map = new HashMap<>();
                map.put("userid", "wd" + intent_id);
                return map;
            }
        };
        Volley.newRequestQueue(PaymentHistoryReport.this).add(stringRequest);
    }

    public boolean isConnected() {
        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Service.CONNECTIVITY_SERVICE);

        if (connectivityManager != null) {
            NetworkInfo info = connectivityManager.getActiveNetworkInfo();
            if (info != null) {
                if (info.getState() == NetworkInfo.State.CONNECTED) {
                    return true;
                }
            }
        }
        return false;
    }

    class internetChecking extends AsyncTask<String, Void, Integer> {

        @Override
        protected Integer doInBackground(String... params) {

            Integer result = 0;
            try {
                Socket socket = new Socket();
                SocketAddress socketAddress = new InetSocketAddress("8.8.8.8", 53);
                socket.connect(socketAddress, 2500);
                socket.close();
                result = 1;
            } catch (IOException e) {
                e.printStackTrace();
                result = 0;
            }
            return result;
        }

        @Override
        protected void onPostExecute(Integer result) {
            if (isConnected()) {
                if (result == 1) {
                    mainMethod();
                }
                if (result == 0) {
                    dialog.dismiss();
                    Toast.makeText(PaymentHistoryReport.this, " No internet available ", Toast.LENGTH_SHORT).show();
                }
            } else {
                dialog.dismiss();
                Toast.makeText(PaymentHistoryReport.this, " No internet connection available ", Toast.LENGTH_SHORT).show();
            }
            super.onPostExecute(result);
        }
    }
}