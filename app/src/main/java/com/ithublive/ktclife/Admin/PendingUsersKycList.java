package com.ithublive.ktclife.Admin;

import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.app.Service;
import android.content.Intent;
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
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.ithublive.ktclife.Admin.Adaptor.PayoutReportListAdaptor;
import com.ithublive.ktclife.Admin.Adaptor.PendingKycListAdaptor;
import com.ithublive.ktclife.Admin.Model.NewJoiningModel;
import com.ithublive.ktclife.Admin.Model.PayoutReportModel;
import com.ithublive.ktclife.Admin.Model.PendingUsersKycModel;
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

public class PendingUsersKycList extends AppCompatActivity {

    Dialog dialog;
    ListView listView_pendingkyc;
    TextView tv_no_pendingkyc;
    List<PendingUsersKycModel> list_pendingkyc;
    PendingKycListAdaptor pendingKycListAdaptor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pending_users_kyc_list);

        listView_pendingkyc = findViewById(R.id.lv_pendingkyclist);
        tv_no_pendingkyc = findViewById(R.id.tv_no_pendingkyc);
        list_pendingkyc = new ArrayList<>();
        dialog = new Dialog(PendingUsersKycList.this);
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

        list_pendingkyc.clear();
        StringRequest stringRequest = new StringRequest(Request.Method.POST, BaseUrl.ktc_admin_reportsUrl, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                if (response.contains("conn_error")) {
                    dialog.dismiss();
                    Toast.makeText(PendingUsersKycList.this, "Server error. Please try again later.", Toast.LENGTH_SHORT).show();
                }
                if (response.contains("#streg#") && response.contains("#enreg#")) {
                    String res = response.substring(response.indexOf("#streg#") + 7, response.indexOf("#enreg#"));

                    String[] res_part = res.split("#ERX#");

                    for (int i = 0; i < res_part.length; i++) {
                        String id = res_part[i].substring(res_part[i].indexOf("#A1#") + 4, res_part[i].indexOf("#A2#"));
                        String name = res_part[i].substring(res_part[i].indexOf("#A2#") + 4, res_part[i].indexOf("#A3#"));
                        String mobile = res_part[i].substring(res_part[i].indexOf("#A3#") + 4, res_part[i].indexOf("#A4#"));
                        String profile_imgurl = res_part[i].substring(res_part[i].indexOf("#A4#") + 4, res_part[i].indexOf("#A5#"));
                        String reference_id = res_part[i].substring(res_part[i].indexOf("#A5#") + 4, res_part[i].indexOf("#A6#"));
                        String plan = res_part[i].substring(res_part[i].indexOf("#A6#") + 4, res_part[i].indexOf("#A7#"));
                        String paid = res_part[i].substring(res_part[i].indexOf("#A7#") + 4, res_part[i].indexOf("#A8#"));
                        String address = res_part[i].substring(res_part[i].indexOf("#A8#") + 4, res_part[i].indexOf("#A9#"));
                        String doj = res_part[i].substring(res_part[i].indexOf("#A9#") + 4, res_part[i].indexOf("#A10#"));
                        String kyc = res_part[i].substring(res_part[i].indexOf("#A10#") + 5, res_part[i].indexOf("#A11#"));
                        String aadhar_number = res_part[i].substring(res_part[i].indexOf("#A11#") + 5, res_part[i].indexOf("#A12#"));
                        String aadhar_front_url = res_part[i].substring(res_part[i].indexOf("#A12#") + 5, res_part[i].indexOf("#A13#"));
                        String aadhar_back_url = res_part[i].substring(res_part[i].indexOf("#A13#") + 5, res_part[i].indexOf("#A14#"));
                        String account_details_url = res_part[i].substring(res_part[i].indexOf("#A14#") + 5, res_part[i].indexOf("#A15#"));
                        String account_number = res_part[i].substring(res_part[i].indexOf("#A15#") + 5, res_part[i].indexOf("#A16#"));
                        String account_ifsc = res_part[i].substring(res_part[i].indexOf("#A16#") + 5, res_part[i].indexOf("#A17#"));

                        PendingUsersKycModel pendingUsersKycModel = new PendingUsersKycModel(id, name, mobile, profile_imgurl, reference_id, plan, paid,
                                address, doj, kyc, aadhar_number, aadhar_front_url, aadhar_back_url, account_details_url, account_number, account_ifsc);
                        list_pendingkyc.add(pendingUsersKycModel);
                    }

                    pendingKycListAdaptor = new PendingKycListAdaptor(PendingUsersKycList.this, list_pendingkyc);
                    listView_pendingkyc.setAdapter((ListAdapter) pendingKycListAdaptor);
                    dialog.dismiss();
                    listView_pendingkyc.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                            PendingUsersKycModel pukm = (PendingUsersKycModel) parent.getItemAtPosition(position);

                            Intent intent = new Intent(PendingUsersKycList.this, PendingUsersKycDetails.class);
                            intent.putExtra("id", pukm.id);
                            intent.putExtra("name", pukm.name);
                            intent.putExtra("mobile", pukm.mobile_number);
                            intent.putExtra("profile_photo", pukm.profile_photo);
                            intent.putExtra("ref_id", pukm.reference_id);
                            intent.putExtra("plan", pukm.plan);
                            intent.putExtra("paid", pukm.paid);
                            intent.putExtra("address", pukm.address);
                            intent.putExtra("doj", pukm.doj);
                            intent.putExtra("kyc", pukm.KYC);
                            intent.putExtra("aadhar_no", pukm.aadhar_number);
                            intent.putExtra("aadhar_front_photo", pukm.aadhar_front_photo);
                            intent.putExtra("aadhar_back_photo", pukm.aadhar_back_photo);
                            intent.putExtra("back_ac_photo", pukm.bank_ac_photo);
                            intent.putExtra("bank_ac_no", pukm.bank_ac_number);
                            intent.putExtra("bank_ac_ifsc", pukm.bank_ac_ifsc);
                            startActivity(intent);
                        }
                    });
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(PendingUsersKycList.this, "Server error.", Toast.LENGTH_SHORT).show();
                dialog.dismiss();
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> map = new HashMap<>();
                map.put("req_for", "pending_users_kyc");
                return map;
            }
        };
        Volley.newRequestQueue(PendingUsersKycList.this).add(stringRequest);
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
                    Toast.makeText(PendingUsersKycList.this, " No internet available ", Toast.LENGTH_SHORT).show();
                }
            } else {
                dialog.dismiss();
                Toast.makeText(PendingUsersKycList.this, " No internet connection available ", Toast.LENGTH_SHORT).show();
            }
            super.onPostExecute(result);
        }
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        listView_pendingkyc.setAdapter(pendingKycListAdaptor);
        pendingKycListAdaptor.notifyDataSetChanged();
    }

    @Override
    protected void onPause() {
        super.onPause();
    }
}