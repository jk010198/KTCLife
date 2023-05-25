package com.ithublive.ktclife.Admin;

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
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.ithublive.ktclife.Admin.Adaptor.PayoutReportListAdaptor;
import com.ithublive.ktclife.Admin.Model.PayoutReportModel;
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

public class PayoutReport extends AppCompatActivity {

    Dialog dialog;
    ListView listView_payout;
    TextView tv_no_payout;
    List<PayoutReportModel> list_payout;
    PayoutReportListAdaptor payoutReportListAdaptor;
    EditText etFromDate, etToDate;
    ImageButton ib_fromdate, ib_todate;
    Button btn_getReport;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payout_report);

        BaseUrl.payoutReportModel = null;
        listView_payout = findViewById(R.id.lv_payoutlist);
        tv_no_payout = findViewById(R.id.tv_no_payouthistory);
        list_payout = new ArrayList<>();
        dialog = new Dialog(PayoutReport.this);
        dialog.setContentView(R.layout.layout_progress_dialog);
        if (dialog.getWindow() != null) {
            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(0));
        }
        dialog.setCancelable(false);


        etFromDate = findViewById(R.id.et_report_payment_fromDate);
        etToDate = findViewById(R.id.et_report_payment_ToDate);

        ib_fromdate = findViewById(R.id.btn_datepicker_reportFromDate);
        ib_todate = findViewById(R.id.btn_datepicker_reportToDate);
        btn_getReport = findViewById(R.id.btn_get_report_payment3403);
        ib_fromdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Calendar calendar = Calendar.getInstance();
                DatePickerDialog datePickerDialog = new DatePickerDialog(PayoutReport.this, new DatePickerDialog.OnDateSetListener() {
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
                DatePickerDialog datePickerDialog = new DatePickerDialog(PayoutReport.this, new DatePickerDialog.OnDateSetListener() {
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

    @Override
    protected void onRestart() {
        super.onRestart();
        Toast.makeText(this, "Refreshing View...", Toast.LENGTH_SHORT).show();
        listView_payout.setAdapter(payoutReportListAdaptor);
        payoutReportListAdaptor.notifyDataSetChanged();
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    public void mainMethod() {
        dialog.show();
        list_payout.clear();
        StringRequest stringRequest = new StringRequest(Request.Method.POST, BaseUrl.ktc_admin_reportsUrl, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                if (response.contains("conn_error")) {
                    dialog.dismiss();
                    Toast.makeText(PayoutReport.this, "Server error. Try Again", Toast.LENGTH_SHORT).show();
                }

                if (response.contains("#streg#") && response.contains("#enreg#")) {
                    String res = response.substring(response.indexOf("#streg#") + 7, response.indexOf("#enreg#"));

                    String[] res_part = res.split("#ERX#");

                    for (int i = 0; i <= res_part.length - 1; i++) {
                        String id = res_part[i].substring(res_part[i].indexOf("#A1#") + 4, res_part[i].indexOf("#A2#"));
                        String date = res_part[i].substring(res_part[i].indexOf("#A2#") + 4, res_part[i].indexOf("#A3#"));
                        String install_no = res_part[i].substring(res_part[i].indexOf("#A3#") + 4, res_part[i].indexOf("#A4#"));
                        String amount = res_part[i].substring(res_part[i].indexOf("#A4#") + 4, res_part[i].indexOf("#A5#"));
                        String pay_mode = res_part[i].substring(res_part[i].indexOf("#A5#") + 4, res_part[i].indexOf("#A6#"));
                        String pay_id = res_part[i].substring(res_part[i].indexOf("#A6#") + 4, res_part[i].indexOf("#A7#"));
                        String name = res_part[i].substring(res_part[i].indexOf("#A7#") + 4, res_part[i].indexOf("#A8#"));
                        String img_url = res_part[i].substring(res_part[i].indexOf("#A8#") + 4, res_part[i].indexOf("#A9#"));
                        String address = res_part[i].substring(res_part[i].indexOf("#A9#") + 4, res_part[i].indexOf("#A10#"));
                        String paid_bonus = res_part[i].substring(res_part[i].indexOf("#A10#") + 5, res_part[i].indexOf("#A11#"));

                        PayoutReportModel payoutReportModel = new PayoutReportModel(id, date, install_no, amount, pay_mode, pay_id, name, img_url, address, paid_bonus);
                        list_payout.add(payoutReportModel);
                    }

                    payoutReportListAdaptor = new PayoutReportListAdaptor(PayoutReport.this, list_payout);
                    listView_payout.setAdapter((ListAdapter) payoutReportListAdaptor);
                    dialog.dismiss();
                    listView_payout.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        }
                    });
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(PayoutReport.this, "Server error", Toast.LENGTH_SHORT).show();
                dialog.dismiss();
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> map = new HashMap<>();
                map.put("req_for", "SpecficDt_planpays");
                map.put("fm_dt", etFromDate.getText().toString().trim());
                map.put("to_dt", etToDate.getText().toString().trim());
                return map;
            }
        };
        Volley.newRequestQueue(PayoutReport.this).add(stringRequest);
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
                    Toast.makeText(PayoutReport.this, " No internet available ", Toast.LENGTH_SHORT).show();
                }
            } else {
                dialog.dismiss();
                Toast.makeText(PayoutReport.this, " No internet connection available ", Toast.LENGTH_SHORT).show();
            }
            super.onPostExecute(result);
        }
    }

}
