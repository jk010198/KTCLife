package com.ithublive.ktclife.Admin;

import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.app.Service;
import android.graphics.drawable.ColorDrawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.ithublive.ktclife.Admin.Adaptor.CommissionListAdaptor;
import com.ithublive.ktclife.Admin.Adaptor.MonthlyTargetListAdaptor;
import com.ithublive.ktclife.Admin.Model.CommissionReportModel;
import com.ithublive.ktclife.Admin.Model.MonthlyTargetModel;
import com.ithublive.ktclife.R;
import com.ithublive.ktclife.Utils.BaseUrl;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketAddress;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CommissionReport extends AppCompatActivity {

    Dialog dialog;
    ListView listView_monthlyTarget;
    TextView tv_no_payout, tv_totalamount;
    List<CommissionReportModel> list_monthlyTarget;
    CommissionListAdaptor commissionListAdaptor;
    public static EditText etFromDate, etToDate;
    ImageButton ib_fromdate, ib_todate;
    Button btn_getReport;
    int totalAmount;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_commission_report);

        listView_monthlyTarget = findViewById(R.id.lv_commissionlist);
        tv_no_payout = findViewById(R.id.tv_no_payouthistory);
        tv_totalamount = findViewById(R.id.tv_totalamt);
        list_monthlyTarget = new ArrayList<>();

        dialog = new Dialog(CommissionReport.this);
        dialog.setContentView(R.layout.layout_progress_dialog);
        if (dialog.getWindow() != null) {
            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(0));
        }
        dialog.setCancelable(false);

        etFromDate = findViewById(R.id.et_report_payment_fromDate);
        etToDate = findViewById(R.id.et_report_payment_ToDate);

        ib_fromdate = findViewById(R.id.btn_datepicker_reportFromDate);
        ib_todate = findViewById(R.id.btn_datepicker_reportToDate);
        btn_getReport = findViewById(R.id.btn_get_commission_report);
        ib_fromdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Calendar calendar = Calendar.getInstance();
                DatePickerDialog datePickerDialog = new DatePickerDialog(CommissionReport.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker datePicker, int y, int m, int d) {
                        etFromDate.setText(y + "-" + (m + 1) + "-" + d);
                    }
                }, calendar.get(Calendar.YEAR), (calendar.get(Calendar.MONTH)), calendar.get(Calendar.DATE));
                datePickerDialog.show();
            }
        });

        ib_todate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Calendar calendar = Calendar.getInstance();
                DatePickerDialog datePickerDialog = new DatePickerDialog(CommissionReport.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker datePicker, int y, int m, int d) {
                        etToDate.setText(y + "-" + (m + 1) + "-" + d);
                    }
                }, calendar.get(Calendar.YEAR), (calendar.get(Calendar.MONTH)), calendar.get(Calendar.DATE));
                datePickerDialog.show();
            }
        });

        btn_getReport.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (etFromDate.getText().toString().trim().length() < 8) {
                    return;
                }
                if (etToDate.getText().toString().trim().length() < 8) {
                    return;
                }
                internetChecking internetChecking = new internetChecking();
                internetChecking.execute();
            }
        });
    }

    public void mainMethod() {
        dialog.show();
        totalAmount = 0;
        list_monthlyTarget.clear();
        StringRequest stringRequest = new StringRequest(Request.Method.POST, BaseUrl.ktc_admin_reportsUrl, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                if (response.contains("conn_error")) {
                    dialog.dismiss();
                    Toast.makeText(CommissionReport.this, "Server error. Please try again later...", Toast.LENGTH_SHORT).show();
                }

                if (response.contains("no_tr_found")) {
                    Toast.makeText(CommissionReport.this, "No transaction found.", Toast.LENGTH_SHORT).show();
                    dialog.dismiss();
                }

                if (response.contains("#start_op#") && response.contains("#end_op#")) {
                    String res = response.substring(response.indexOf("#start_op#") + 10, response.indexOf("#end_op#"));

                    String[] res_part = res.split("#STOP#");

                    for (int i = 0; i < res_part.length; i++) {
                        String user_id = res_part[i].substring(res_part[i].indexOf("#userid#") + 8, res_part[i].indexOf("#Amt#"));
                        String amt = res_part[i].substring(res_part[i].indexOf("#Amt#") + 5, res_part[i].indexOf("#date#"));
                        String date = res_part[i].substring(res_part[i].indexOf("#date#") + 6, res_part[i].indexOf("#name#"));
                        String name = res_part[i].substring(res_part[i].indexOf("#name#") + 6, res_part[i].indexOf("#ids#"));
                        String ids = res_part[i].substring(res_part[i].indexOf("#ids#") + 5, res_part[i].indexOf("#1a#"));

                        CommissionReportModel commissionReportModel = new CommissionReportModel(user_id, amt, date, name, ids);
                        if (!(amt.equals("0"))) {
                            list_monthlyTarget.add(commissionReportModel);
                            totalAmount += Integer.parseInt(amt);
                        }
                    }
                    commissionListAdaptor = new CommissionListAdaptor(CommissionReport.this, list_monthlyTarget);
                    listView_monthlyTarget.setAdapter((ListAdapter) commissionListAdaptor);
                    tv_totalamount.setText("Total Amount :- " + totalAmount);
                    dialog.dismiss();
                } else {
                    Toast.makeText(CommissionReport.this, "Error.", Toast.LENGTH_SHORT).show();
                    dialog.dismiss();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(CommissionReport.this, "Server error.", Toast.LENGTH_SHORT).show();
                dialog.dismiss();
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> map = new HashMap<>();
                map.put("req_for", "CommissionReport");
                map.put("fm_dt", etFromDate.getText().toString().trim());
                map.put("to_dt", etToDate.getText().toString().trim());
                return map;
            }
        };
        Volley.newRequestQueue(CommissionReport.this).add(stringRequest);
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
                    Toast.makeText(CommissionReport.this, "No internet available.", Toast.LENGTH_SHORT).show();
                }
            } else {
                dialog.dismiss();
                Toast.makeText(CommissionReport.this, "No internet connection available.", Toast.LENGTH_SHORT).show();
            }
            super.onPostExecute(result);
        }
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        listView_monthlyTarget.setAdapter(commissionListAdaptor);
        commissionListAdaptor.notifyDataSetChanged();
    }

    @Override
    protected void onPause() {
        super.onPause();
    }
}