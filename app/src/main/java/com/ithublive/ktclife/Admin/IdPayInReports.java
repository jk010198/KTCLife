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
import com.ithublive.ktclife.Adaptor.WalletHistoryListAdaptor;
import com.ithublive.ktclife.Admin.Adaptor.IdPayInListAdaptor;
import com.ithublive.ktclife.Admin.Model.PayinPayoutModel;
import com.ithublive.ktclife.Model.WalletHistoryModel;
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

public class IdPayInReports extends AppCompatActivity {

    Dialog dialog;
    ListView listView_payout;
    TextView tv_no_payout, tv_totalamount;
    List<PayinPayoutModel> list_payout;
    IdPayInListAdaptor idPayInListAdaptor;
    EditText etFromDate, etToDate;
    ImageButton ib_fromdate, ib_todate;
    Button btn_getReport;
    int totalAmount;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pay_in_reports);

        BaseUrl.payoutReportModel = null;
        listView_payout = findViewById(R.id.lv_payoutlist);
        tv_no_payout = findViewById(R.id.tv_no_payouthistory);
        tv_totalamount = findViewById(R.id.tv_totalamt);
        list_payout = new ArrayList<>();
        dialog = new Dialog(IdPayInReports.this);
        dialog.setContentView(R.layout.layout_progress_dialog);
        if (dialog.getWindow() != null) {
            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(0));
        }
        dialog.setCancelable(false);

        etFromDate = findViewById(R.id.et_report_payment_fromDate);
        etToDate = findViewById(R.id.et_report_payment_ToDate);

        ib_fromdate = findViewById(R.id.btn_datepicker_reportFromDate);
        ib_todate = findViewById(R.id.btn_datepicker_reportToDate);
        btn_getReport = findViewById(R.id.btn_get_payinreport);
        ib_fromdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Calendar calendar = Calendar.getInstance();
                DatePickerDialog datePickerDialog = new DatePickerDialog(IdPayInReports.this, new DatePickerDialog.OnDateSetListener() {
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
                DatePickerDialog datePickerDialog = new DatePickerDialog(IdPayInReports.this, new DatePickerDialog.OnDateSetListener() {
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
        list_payout.clear();
        StringRequest stringRequest = new StringRequest(Request.Method.POST, BaseUrl.ktc_admin_reportsUrl, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d("idpay_in", "" + response);
                if (response.contains("conn_error")) {
                    dialog.dismiss();
                    Toast.makeText(IdPayInReports.this, "Server error. please try again later...", Toast.LENGTH_SHORT).show();
                }

                if (response.contains("no_tr_found")) {
                    Toast.makeText(IdPayInReports.this, "No transaction found.", Toast.LENGTH_SHORT).show();
                    dialog.dismiss();
                }

                if (response.contains("#streg#") && response.contains("#enreg#")) {
                    String res = response.substring(response.indexOf("#streg#") + 7, response.indexOf("#enreg#"));

                    String[] res_part = res.split("#ERX#");

                    for (int i = 0; i < res_part.length; i++) {
                        String user_id = res_part[i].substring(res_part[i].indexOf("#1a#") + 4, res_part[i].indexOf("#2a#"));
                        String pay_date = res_part[i].substring(res_part[i].indexOf("#2a#") + 4, res_part[i].indexOf("#3a#"));
                        String ins_no = res_part[i].substring(res_part[i].indexOf("#3a#") + 4, res_part[i].indexOf("#4a#"));
                        String t_amount = res_part[i].substring(res_part[i].indexOf("#4a#") + 4, res_part[i].indexOf("#5a#"));
                        String pay_mode = res_part[i].substring(res_part[i].indexOf("#5a#") + 4, res_part[i].indexOf("#6a#"));
                        String pay_id = res_part[i].substring(res_part[i].indexOf("#6a#") + 4, res_part[i].indexOf("#7a#"));
                        String bonus_paid = res_part[i].substring(res_part[i].indexOf("#7a#") + 4, res_part[i].indexOf("#8a#"));
                        String withdrawal_date = res_part[i].substring(res_part[i].indexOf("#8a#") + 4, res_part[i].indexOf("#9a#"));

                        PayinPayoutModel payinPayoutModel = new PayinPayoutModel(user_id, pay_date, ins_no, t_amount, pay_mode, "");
                        list_payout.add(payinPayoutModel);
                        totalAmount += Integer.parseInt(t_amount);
                    }

                    idPayInListAdaptor = new IdPayInListAdaptor(IdPayInReports.this, list_payout);
                    listView_payout.setAdapter((ListAdapter) idPayInListAdaptor);
                    dialog.dismiss();
                    tv_totalamount.setText("Total Amount :- " + totalAmount);
                    listView_payout.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        }
                    });
                } else {
                    Toast.makeText(IdPayInReports.this, "else", Toast.LENGTH_SHORT).show();
                    dialog.dismiss();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(IdPayInReports.this, "Server error.", Toast.LENGTH_SHORT).show();
                dialog.dismiss();
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> map = new HashMap<>();
                map.put("req_for", "SpecficDt_IdpayIn");
                map.put("fm_dt", etFromDate.getText().toString().trim());
                map.put("to_dt", etToDate.getText().toString().trim());
                return map;
            }
        };
        Volley.newRequestQueue(IdPayInReports.this).add(stringRequest);
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
                    Toast.makeText(IdPayInReports.this, "No internet available.", Toast.LENGTH_SHORT).show();
                }
            } else {
                dialog.dismiss();
                Toast.makeText(IdPayInReports.this, "No internet connection available.", Toast.LENGTH_SHORT).show();
            }
            super.onPostExecute(result);
        }
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        listView_payout.setAdapter(idPayInListAdaptor);
        idPayInListAdaptor.notifyDataSetChanged();
    }

    @Override
    protected void onPause() {
        super.onPause();
    }
}