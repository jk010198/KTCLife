package com.ithublive.ktclife.Admin;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.app.Service;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListAdapter;

import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.ithublive.ktclife.Admin.Adaptor.PendingKycListAdaptor;
import com.ithublive.ktclife.Admin.Adaptor.TotalRegisterUserAdaptor;
import com.ithublive.ktclife.Admin.Model.PendingUsersKycModel;
import com.ithublive.ktclife.Admin.Model.TotalUsersListModel;
import com.ithublive.ktclife.Model.WalletHistoryModel;
import com.ithublive.ktclife.R;
import com.ithublive.ktclife.Utils.BaseUrl;
import com.trendyol.bubblescrollbarlib.BubbleScrollBar;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketAddress;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class TotalRegisterUsersListReport extends AppCompatActivity {

    RecyclerView rv_userList;
    LinearLayoutManager linearLayoutManager;
    ArrayList<TotalUsersListModel> userListData;
    Dialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_total_register_users_list_report);

        rv_userList = findViewById(R.id.rv_userslist);
        dialog = new Dialog(TotalRegisterUsersListReport.this);
        dialog.setContentView(R.layout.layout_progress_dialog);
        if (dialog.getWindow() != null) {
            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(0));
        }
        dialog.setCancelable(false);

        rv_userList.setHasFixedSize(true);
        linearLayoutManager = new LinearLayoutManager(this);
        rv_userList.setLayoutManager(linearLayoutManager);
        rv_userList.addItemDecoration(new DividerItemDecoration(this, linearLayoutManager.getOrientation()));

        userListData = new ArrayList<>();

        internetChecking internetChecking = new internetChecking();
        internetChecking.execute();
    }

    public void mainMethod() {
        dialog.show();
        userListData.clear();
        StringRequest stringRequest = new StringRequest(Request.Method.POST, BaseUrl.ktc_admin_reportsUrl, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                if (response.contains("conn_error")) {
                    dialog.dismiss();
                    Toast.makeText(TotalRegisterUsersListReport.this, "Server error. Try Again", Toast.LENGTH_SHORT).show();
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
                        String cap_limit = res_part[i].substring(res_part[i].indexOf("#A17#") + 5, res_part[i].indexOf("#A18#"));
                        String no_of_leg = res_part[i].substring(res_part[i].indexOf("#A18#") + 5, res_part[i].indexOf("#A19#"));
                        String password = res_part[i].substring(res_part[i].indexOf("#A19#") + 5, res_part[i].indexOf("#A20#"));

                        TotalUsersListModel totalUsersListModel = new TotalUsersListModel(id, name.toUpperCase().trim(), mobile, profile_imgurl,
                                reference_id, plan, paid.toUpperCase(), address, doj, kyc, aadhar_number, aadhar_front_url, aadhar_back_url,
                                account_details_url, account_number, account_ifsc, cap_limit, no_of_leg, password);
                        userListData.add(totalUsersListModel);
                    }

                    Collections.sort(userListData);
                    TotalRegisterUserAdaptor totalRegisterUserAdaptor = new TotalRegisterUserAdaptor(userListData, TotalRegisterUsersListReport.this);
                    rv_userList.setAdapter(totalRegisterUserAdaptor);
                    dialog.dismiss();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                dialog.dismiss();
                Toast.makeText(TotalRegisterUsersListReport.this, "Server error.", Toast.LENGTH_SHORT).show();
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> map = new HashMap<>();
                map.put("req_for", "total_users");
                return map;
            }
        };
        Volley.newRequestQueue(TotalRegisterUsersListReport.this).add(stringRequest);
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
                    Toast.makeText(TotalRegisterUsersListReport.this, " No internet available ", Toast.LENGTH_SHORT).show();
                }
            } else {
                Toast.makeText(TotalRegisterUsersListReport.this, " No internet connection available ", Toast.LENGTH_SHORT).show();
            }
            super.onPostExecute(result);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.search_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {

            case R.id.search:
                SearchView searchView = (SearchView) item.getActionView();
                searchView.setQueryHint("Search Userid...");
                searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
                    @Override
                    public boolean onQueryTextSubmit(String s) {
                        return false;
                    }

                    @Override
                    public boolean onQueryTextChange(String text) {
                        filter(text);
                        return true;
                    }
                });
                return true;
            default:
                break;
        }
        return false;
    }

    private void filter(String filterText) {
        // creating a new array list to filter our data.
        ArrayList<TotalUsersListModel> filteredlist = new ArrayList<>();
        // running a for loop to compare elements.
        for (TotalUsersListModel item : userListData) {
            // checking if the entered string matched with any item of our recycler view.
            if (item.getId().contains(filterText)) {
                // if the item is matched we are adding it to our filtered list.
                filteredlist.add(item);
            }
        }

        if (filteredlist.isEmpty()) {
            Toast.makeText(this, "No Data Found..", Toast.LENGTH_SHORT).show();
            rv_userList.setAdapter(new TotalRegisterUserAdaptor(filteredlist, TotalRegisterUsersListReport.this));
        } else {
            rv_userList.setAdapter(new TotalRegisterUserAdaptor(filteredlist, TotalRegisterUsersListReport.this));
        }
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        internetChecking internetChecking = new internetChecking();
        internetChecking.execute();
    }

    @Override
    protected void onPause() {
        super.onPause();
        internetChecking internetChecking = new internetChecking();
        internetChecking.execute();
    }
}