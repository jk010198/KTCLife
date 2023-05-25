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
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
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
import com.ithublive.ktclife.Admin.Adaptor.IdPayInListAdaptor;
import com.ithublive.ktclife.Admin.Adaptor.MonthlyTargetListAdaptor;
import com.ithublive.ktclife.Admin.Model.MonthlyTargetModel;
import com.ithublive.ktclife.Admin.Model.PayinPayoutModel;
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

public class MonthlyTargetActivity extends AppCompatActivity {

    Dialog dialog;
    ListView listView_monthlyTarget;
    TextView tv_no_payout, tv_totalamount;
    List<MonthlyTargetModel> list_monthlyTarget;
    MonthlyTargetListAdaptor monthlyTargetListAdaptor;
    public static EditText etFromDate, etToDate;
    ImageButton ib_fromdate, ib_todate;
    Button btn_getReport;
    int totalAmount;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_monthly_target);

        listView_monthlyTarget = findViewById(R.id.lv_monthlypayoutlist);
        tv_no_payout = findViewById(R.id.tv_no_payouthistory);
        tv_totalamount = findViewById(R.id.tv_totalamt);
        list_monthlyTarget = new ArrayList<>();

        dialog = new Dialog(MonthlyTargetActivity.this);
        dialog.setContentView(R.layout.layout_progress_dialog);
        if (dialog.getWindow() != null) {
            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(0));
        }
        dialog.setCancelable(false);

        etFromDate = findViewById(R.id.et_report_payment_fromDate);
        etToDate = findViewById(R.id.et_report_payment_ToDate);

        ib_fromdate = findViewById(R.id.btn_datepicker_reportFromDate);
        ib_todate = findViewById(R.id.btn_datepicker_reportToDate);
        btn_getReport = findViewById(R.id.btn_get_monthlytarget_report);

        ib_fromdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Calendar calendar = Calendar.getInstance();
                DatePickerDialog datePickerDialog = new DatePickerDialog(MonthlyTargetActivity.this, new DatePickerDialog.OnDateSetListener() {
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
                DatePickerDialog datePickerDialog = new DatePickerDialog(MonthlyTargetActivity.this, new DatePickerDialog.OnDateSetListener() {
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
                    Toast.makeText(MonthlyTargetActivity.this, "Server error. Please try again later...", Toast.LENGTH_SHORT).show();
                }

                if (response.contains("no_tr_found")) {
                    Toast.makeText(MonthlyTargetActivity.this, "No transaction found.", Toast.LENGTH_SHORT).show();
                    dialog.dismiss();
                }

                if (response.contains("#streg#") && response.contains("#enreg#")) {
                    String res = response.substring(response.indexOf("#streg#") + 7, response.indexOf("#enreg#"));

                    String[] res_part = res.split("#ERX#");

                    for (int i = 0; i < res_part.length; i++) {
                        String ref_id = res_part[i].substring(res_part[i].indexOf("#ref_id#") + 8, res_part[i].indexOf("#name#"));
                        String ref_name = res_part[i].substring(res_part[i].indexOf("#name#") + 6, res_part[i].indexOf("#ids#"));
                        String ids_count = res_part[i].substring(res_part[i].indexOf("#ids#") + 5, res_part[i].indexOf("#amt#"));
                        String amt = res_part[i].substring(res_part[i].indexOf("#amt#") + 5, res_part[i].indexOf("#9a#"));

                        MonthlyTargetModel monthlyTargetModel = new MonthlyTargetModel(ref_id, ref_name, ids_count, amt);
                        list_monthlyTarget.add(monthlyTargetModel);
                    }
                    monthlyTargetListAdaptor = new MonthlyTargetListAdaptor(MonthlyTargetActivity.this, list_monthlyTarget);
                    listView_monthlyTarget.setAdapter((ListAdapter) monthlyTargetListAdaptor);
                    dialog.dismiss();
                } else {
                    Toast.makeText(MonthlyTargetActivity.this, "Error.", Toast.LENGTH_SHORT).show();
                    dialog.dismiss();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(MonthlyTargetActivity.this, "Server error.", Toast.LENGTH_SHORT).show();
                dialog.dismiss();
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> map = new HashMap<>();
                map.put("req_for", "MonthlyReport");
                map.put("fm_dt", etFromDate.getText().toString().trim());
                map.put("to_dt", etToDate.getText().toString().trim());
                return map;
            }
        };
        Volley.newRequestQueue(MonthlyTargetActivity.this).add(stringRequest);
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
                    Toast.makeText(MonthlyTargetActivity.this, "No internet available.", Toast.LENGTH_SHORT).show();
                }
            } else {
                dialog.dismiss();
                Toast.makeText(MonthlyTargetActivity.this, "No internet connection available.", Toast.LENGTH_SHORT).show();
            }
            super.onPostExecute(result);
        }
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        //Toast.makeText(this, "Refreshing View...", Toast.LENGTH_SHORT).show();
        listView_monthlyTarget.setAdapter(monthlyTargetListAdaptor);
        monthlyTargetListAdaptor.notifyDataSetChanged();
    }

    @Override
    protected void onPause() {
        super.onPause();
    }
}