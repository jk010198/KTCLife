package com.ithublive.ktclife.Admin;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.app.Service;
import android.content.DialogInterface;
import android.graphics.drawable.ColorDrawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
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
import com.ithublive.ktclife.Admin.Adaptor.NewJoiningListAdaptor;
import com.ithublive.ktclife.Admin.Model.NewJoiningModel;
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

public class NewJoiningReport extends AppCompatActivity {

    Dialog dialog;
    ListView listView_newjoining;
    TextView tv_no_payout;
    List<NewJoiningModel> list_newjoining;
    NewJoiningListAdaptor newJoiningListAdaptor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_joining_report);

        listView_newjoining = findViewById(R.id.lv_newjoining);
        tv_no_payout = findViewById(R.id.tv_no_payouthistory);
        list_newjoining = new ArrayList<>();
        dialog = new Dialog(NewJoiningReport.this);
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
        list_newjoining.clear();

        StringRequest stringRequest = new StringRequest(Request.Method.POST, BaseUrl.ktc_admin_reportsUrl, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                if (response.contains("conn_error")) {
                    dialog.dismiss();
                    Toast.makeText(NewJoiningReport.this, "Server error. Try Again", Toast.LENGTH_SHORT).show();
                }

                if (response.contains("#streg#") && response.contains("#enreg#")) {
                    String res = response.substring(response.indexOf("#streg#") + 6, response.indexOf("#enreg#"));

                    String[] res_part = res.split("#ERX#");

                    for (int i = 0; i <= res_part.length - 1; i++) {

                        String id = res_part[i].substring(res_part[i].indexOf("#A1#") + 4, res_part[i].indexOf("#A2#"));
                        String name = res_part[i].substring(res_part[i].indexOf("#A2#") + 4, res_part[i].indexOf("#A3#"));
                        String mobile = res_part[i].substring(res_part[i].indexOf("#A3#") + 4, res_part[i].indexOf("#A4#"));
                        String img_url = res_part[i].substring(res_part[i].indexOf("#A4#") + 4, res_part[i].indexOf("#A5#"));
                        String reference_id = res_part[i].substring(res_part[i].indexOf("#A5#") + 4, res_part[i].indexOf("#A6#"));
                        String plan = res_part[i].substring(res_part[i].indexOf("#A6#") + 4, res_part[i].indexOf("#A7#"));
                        String is_paid = res_part[i].substring(res_part[i].indexOf("#A7#") + 4, res_part[i].indexOf("#A8#"));
                        String address = res_part[i].substring(res_part[i].indexOf("#A8#") + 4, res_part[i].indexOf("#A9#"));
                        String password = res_part[i].substring(res_part[i].indexOf("#A9#") + 4, res_part[i].indexOf("#A10#"));
                        String date_added = res_part[i].substring(res_part[i].indexOf("#A10#") + 5, res_part[i].indexOf("#A11#"));
                        String cap_limit = res_part[i].substring(res_part[i].indexOf("#A11#") + 5, res_part[i].indexOf("#A12#"));
                        String ref_name = res_part[i].substring(res_part[i].indexOf("#A12#") + 5, res_part[i].indexOf("#A13#"));

                        NewJoiningModel newJoiningModel = new NewJoiningModel(id, name, mobile, img_url, reference_id, plan, is_paid, address, password,
                                date_added, cap_limit, ref_name);
                        list_newjoining.add(newJoiningModel);
                    }

                    newJoiningListAdaptor = new NewJoiningListAdaptor(NewJoiningReport.this, list_newjoining);
                    listView_newjoining.setAdapter((ListAdapter) newJoiningListAdaptor);
                    dialog.dismiss();

                    listView_newjoining.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                            NewJoiningModel njm = (NewJoiningModel) parent.getItemAtPosition(position);
                            String msg = " Userid :- " + njm.id + "\n Username :- " + njm.name + "\n Mobile :- " + njm.mobile +
                                    "\n Referer id :- " + njm.reference_id +
                                    "\n Referer Name :- " + njm.r_name + "\n Plan :- " + njm.plan +
                                    "\n Is paid :- " + njm.is_paid + "\n Address :- " + njm.address +
                                    "\n Date of joining :- " + njm.date_added + "\n Cap limit :- " + njm.cap_limit;

                            AlertDialog.Builder adb = new AlertDialog.Builder(NewJoiningReport.this);
                            adb.setTitle(R.string.dialog_title);
                            adb.setIcon(R.drawable.app_logo);
                            adb.setMessage(msg);
                            adb.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    dialogInterface.dismiss();
                                }
                            });
                            adb.show();
                        }
                    });
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(NewJoiningReport.this, "Server error.", Toast.LENGTH_SHORT).show();
                dialog.dismiss();
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> map = new HashMap<>();
                map.put("req_for", "last_30D_reg");
                return map;
            }
        };
        Volley.newRequestQueue(NewJoiningReport.this).add(stringRequest);
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
                    Toast.makeText(NewJoiningReport.this, " No internet available ", Toast.LENGTH_SHORT).show();
                }
            } else {
                dialog.dismiss();
                Toast.makeText(NewJoiningReport.this, " No internet connection available ", Toast.LENGTH_SHORT).show();
            }
            super.onPostExecute(result);
        }
    }
}