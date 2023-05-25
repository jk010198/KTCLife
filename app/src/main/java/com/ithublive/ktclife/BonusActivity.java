package com.ithublive.ktclife;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.app.Service;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.ColorDrawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.RetryPolicy;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.ithublive.ktclife.Adaptor.WalletHistoryListAdaptor;
import com.ithublive.ktclife.Model.WalletHistoryModel;
import com.ithublive.ktclife.Utils.BaseUrl;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketAddress;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class BonusActivity extends AppCompatActivity {

    TextView tv_currentBonus, tv_totalBonus, tv_level, textView_credit, textView_debit;
    Dialog dialog;
    String userID, userName, my_Wallet_Password, levelColor;
    int credit, debit;
    ListView rv_wallet_history;
    TextView tv_no_history;
    String retriveUserData;
    List<WalletHistoryModel> list_wallet_history;
    WalletHistoryListAdaptor walletHistoryListAdaptor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bonus);

        tv_currentBonus = findViewById(R.id.textView_currentbonus);
        tv_totalBonus = findViewById(R.id.textView_totalbonus);
        tv_level = findViewById(R.id.textView_level);
        textView_credit = findViewById(R.id.textView_credit);
        textView_debit = findViewById(R.id.textView_debit);

        dialog = new Dialog(BonusActivity.this);
        dialog.setContentView(R.layout.layout_progress_dialog);
        if (dialog.getWindow() != null) {
            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(0));
        }
        dialog.setCancelable(false);

        SharedPreferences shared = getSharedPreferences("" + R.string.sp_retriveUserData, MODE_PRIVATE);
        retriveUserData = (shared.getString("data", ""));
        userID = retriveUserData.substring(retriveUserData.indexOf("!") + 1, retriveUserData.indexOf("@"));
        userName = retriveUserData.substring(retriveUserData.indexOf("@") + 1, retriveUserData.indexOf("#"));
        if (retriveUserData.contains("silver")) {
            int legs = Integer.parseInt(retriveUserData.substring(retriveUserData.indexOf("silver") + 6, retriveUserData.indexOf("gold")));
            if (legs >= 8) {
                levelColor = "silver";
                int silverLegs = Integer.parseInt(retriveUserData.substring(retriveUserData.indexOf("gold") + 4, retriveUserData.indexOf("ruby")));
                if (silverLegs >= 8) {
                    levelColor = "gold";
                    int goldLegs = Integer.parseInt(retriveUserData.substring(retriveUserData.indexOf("ruby") + 4, retriveUserData.indexOf("diamond")));
                    if (goldLegs >= 8) {
                        levelColor = "ruby";
                        int rubyLegs = Integer.parseInt(retriveUserData.substring(retriveUserData.indexOf("diamond") + 7,
                                retriveUserData.indexOf("end_level_count")));
                        if (rubyLegs >= 8) {
                            levelColor = "diamond";
                        }
                    }
                }
            }
        }
        tv_level.setText("Level :- " + levelColor);
        if (tv_level.getText().toString().contains("null")){
            tv_level.setText("Level :- No Level");
        }

        methodGetBonus();

        rv_wallet_history = findViewById(R.id.rv_wallet_history);
        tv_no_history = findViewById(R.id.tv_no_history);
        list_wallet_history = new ArrayList<>();
        internetChecking internetChecking = new internetChecking();
        internetChecking.execute();
    }

    public void method_trnbalance(View view) {
        startActivity(new Intent(getApplicationContext(), WalletPaymentActivity.class));
    }

    private void methodGetBonus() {
        dialog.show();
        StringRequest stringRequest = new StringRequest(Request.Method.POST, BaseUrl.ktcBonusRequest, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                if (response.contains("conn_error")) {
                    dialog.dismiss();
                    Toast.makeText(BonusActivity.this, "Server error. Please try again later.", Toast.LENGTH_SHORT).show();
                }

                if (response.contains("bnus")) {
                    dialog.dismiss();
                    tv_totalBonus.setText("Wallet Total : " + response.substring(response.indexOf("#bnus#") + 6, response.indexOf("#eb#")));
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(BonusActivity.this, "Server error", Toast.LENGTH_SHORT).show();
                dialog.dismiss();
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> map = new HashMap<>();
                map.put("uid", userID);
                return map;
            }
        };
        RetryPolicy mRetryPolicy = new DefaultRetryPolicy(
                20000,
                1,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
        stringRequest.setRetryPolicy(mRetryPolicy);
        Volley.newRequestQueue(BonusActivity.this).add(stringRequest);
    }

    public void methodGetPassword() {
        dialog.show();
        StringRequest stringRequest = new StringRequest(Request.Method.POST, BaseUrl.ktcWalletUrl, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                if (response.contains("sql_con_er")) {
                    dialog.dismiss();
                    Toast.makeText(BonusActivity.this, "Database connection error.", Toast.LENGTH_SHORT).show();
                }

                if (response.contains("#tr1#")) {
                    my_Wallet_Password = response.substring(response.indexOf("#tr3#") + 5, response.indexOf("#tr4#"));
                    dialog.dismiss();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(BonusActivity.this, "Server error.", Toast.LENGTH_LONG).show();
                dialog.dismiss();
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> map = new HashMap<>();
                map.put("req_for", "get_Info");
                map.put("req_id", userID);
                return map;
            }
        };
        RetryPolicy mRetryPolicy = new DefaultRetryPolicy(
                20000,
                //DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                1,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
        stringRequest.setRetryPolicy(mRetryPolicy);
        Volley.newRequestQueue(BonusActivity.this).add(stringRequest);
    }

    public void method_changewalletPassword(View view) {
        methodGetPassword();
        final AlertDialog.Builder adb_A = new AlertDialog.Builder(BonusActivity.this);
        final AlertDialog adb_1 = adb_A.create();
        adb_1.setTitle(R.string.dialog_title);
        View view_1 = getLayoutInflater().inflate(R.layout.activity_wallet_change_password, null);

        final EditText et_new1, et_new2, et_new3, et_new4, et_old1, et_old2, et_old3, et_old4;
        Button btn_submit_change_pass;

        et_new1 = view_1.findViewById(R.id.et_pass_1);
        et_new2 = view_1.findViewById(R.id.et_pass_2);
        et_new3 = view_1.findViewById(R.id.et_pass_3);
        et_new4 = view_1.findViewById(R.id.et_pass_4);

        et_old1 = view_1.findViewById(R.id.et_oldpass_1);
        et_old2 = view_1.findViewById(R.id.et_oldpass_2);
        et_old3 = view_1.findViewById(R.id.et_oldpass_3);
        et_old4 = view_1.findViewById(R.id.et_oldpass_4);

        btn_submit_change_pass = view_1.findViewById(R.id.btn_submit_changepassword);

        et_old1.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                if (et_old1.hasFocus()) {
                    if (et_old1.getText().toString().trim().length() == 1) {
                        //et_old1.clearFocus();
                        new Handler().postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                et_old2.requestFocus();
                            }
                        }, 85);
                    }
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        et_old2.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                if (et_old2.hasFocus()) {
                    if (et_old2.getText().toString().trim().length() == 1) {
                        new Handler().postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                et_old3.requestFocus();
                            }
                        }, 85);
                    }
                    if (et_old2.getText().toString().trim().length() == 0) {
                        new Handler().postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                et_old1.requestFocus();
                            }
                        }, 85);
                    }
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {
            }
        });

        et_old3.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (et_old3.hasFocus()) {
                    if (et_old3.getText().toString().trim().length() == 1) {
                        //et_old3.clearFocus();
                        new Handler().postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                et_old4.requestFocus();
                            }
                        }, 85);
                    }
                    if (et_old3.getText().toString().trim().length() == 0) {
                        new Handler().postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                et_old2.requestFocus();
                            }
                        }, 85);
                    }
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {
            }
        });

        et_old4.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (et_old4.hasFocus()) {
                    if (et_old4.getText().toString().trim().length() == 1) {
                        //et_old4.clearFocus();
                        new Handler().postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                et_new1.requestFocus();
                            }
                        }, 85);
                    }
                    if (et_old4.getText().toString().trim().length() == 0) {
                        new Handler().postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                et_old3.requestFocus();
                            }
                        }, 85);
                    }
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {
            }
        });

        et_new1.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (et_new1.hasFocus()) {
                    if (et_new1.getText().toString().trim().length() == 1) {
                        new Handler().postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                et_new2.requestFocus();
                            }
                        }, 85);
                    }
                    if (et_new1.getText().toString().trim().length() == 0) {
                        new Handler().postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                //et_old4.requestFocus();
                            }
                        }, 85);
                    }
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {
            }
        });

        et_new2.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (et_new2.hasFocus()) {
                    if (et_new2.getText().toString().trim().length() == 1) {
                        new Handler().postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                et_new3.requestFocus();
                            }
                        }, 85);
                    }
                    if (et_new2.getText().toString().trim().length() == 0) {
                        new Handler().postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                et_new1.requestFocus();
                            }
                        }, 85);
                    }
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {
            }
        });

        et_new3.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (et_new3.hasFocus()) {
                    if (et_new3.getText().toString().trim().length() == 1) {
                        new Handler().postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                et_new4.requestFocus();
                            }
                        }, 85);
                    }
                    if (et_new3.getText().toString().trim().length() == 0) {
                        new Handler().postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                et_new2.requestFocus();
                            }
                        }, 85);
                    }
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {
            }
        });

        et_new4.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (et_new4.hasFocus()) {
                    if (et_new4.getText().toString().trim().length() == 1) {
                    }
                    if (et_new4.getText().toString().trim().length() == 0) {
                        new Handler().postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                et_new3.requestFocus();
                            }
                        }, 85);
                    }
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {
            }
        });

        btn_submit_change_pass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final String new_wallet_password = et_new1.getText().toString() + et_new2.getText().toString() + et_new3.getText().toString()
                        + et_new4.getText().toString();
                final String old_wallet_password = et_old1.getText().toString() + et_old2.getText().toString() + et_old3.getText().toString()
                        + et_old4.getText().toString();

                if (et_new1.getText().toString().isEmpty() || et_new2.getText().toString().isEmpty() || et_new3.getText().toString().isEmpty()
                        || et_new4.getText().toString().isEmpty() || et_old1.getText().toString().isEmpty() || et_old2.getText().toString().isEmpty()
                        || et_old3.getText().toString().isEmpty() || et_old4.getText().toString().isEmpty()) {
                    Toast.makeText(BonusActivity.this, "Please fill all fields.", Toast.LENGTH_SHORT).show();
                } else if (!(my_Wallet_Password.equals(old_wallet_password))) {
                    Toast.makeText(BonusActivity.this, "Wrong old password, please enter valid password.", Toast.LENGTH_SHORT).show();
                } else {
                    dialog.show();
                    StringRequest stringRequest = new StringRequest(Request.Method.POST, BaseUrl.ktc_trnchange_passwordUrl, new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            Log.d("res_payment", "res " + response);

                            if (response.contains("Not_updated")) {
                                dialog.dismiss();
                                Toast.makeText(BonusActivity.this, "Your password is not update.", Toast.LENGTH_SHORT).show();
                            }

                            if (response.contains("connection not got")) {
                                dialog.dismiss();
                                Toast.makeText(BonusActivity.this, "Database error.", Toast.LENGTH_SHORT).show();
                            }

                            if (response.contains("pass_Updated")) {
                                et_new1.setText("");
                                et_new2.setText("");
                                et_new3.setText("");
                                et_new4.setText("");
                                et_old1.setText("");
                                et_old2.setText("");
                                et_old3.setText("");
                                et_old4.setText("");
                                dialog.dismiss();

                                Toast.makeText(BonusActivity.this, "Your password update is successful.", Toast.LENGTH_LONG).show();
                                final AlertDialog.Builder adb = new AlertDialog.Builder(BonusActivity.this);
                                adb.setTitle(R.string.dialog_title);
                                adb.setMessage("Your password update is successful.");
                                adb.setIcon(R.drawable.app_logo);
                                adb.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int i) {
                                        dialogInterface.dismiss();
                                        adb_1.dismiss();
                                    }
                                });
                                adb.setCancelable(false);
                                adb.show();
                            }
                        }
                    }, new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            Toast.makeText(BonusActivity.this, "Server error.", Toast.LENGTH_LONG).show();
                            dialog.dismiss();
                        }
                    }) {
                        @Override
                        protected Map<String, String> getParams() throws AuthFailureError {
                            Map<String, String> map = new HashMap<>();
                            map.put("id", userID);
                            map.put("trn_password", new_wallet_password);
                            return map;
                        }
                    };
                    RetryPolicy mRetryPolicy = new DefaultRetryPolicy(
                            20000,
                            1,
                            DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
                    stringRequest.setRetryPolicy(mRetryPolicy);
                    Volley.newRequestQueue(BonusActivity.this).add(stringRequest);
                }
            }
        });

        adb_A.setPositiveButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
            }
        });
        adb_1.setView(view_1);
        adb_1.setIcon(R.drawable.app_logo);
        adb_1.show();
    }

    public void mainMethod() {
        dialog.show();
        list_wallet_history.clear();

        StringRequest stringRequest = new StringRequest(Request.Method.POST, BaseUrl.fetchWalletHistoryUrl, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                if (response.contains("conn_error")) {
                    dialog.dismiss();
                    Toast.makeText(BonusActivity.this, "Server error. Try Again", Toast.LENGTH_SHORT).show();
                }

                if (response.contains("#trhs#") && response.contains("#EEE#")) {
                    if (response.contains("#trhs#") && response.contains("#FFF#")) {
                        String res = response.substring(response.indexOf("#trhs#") + 6, response.indexOf("#FFF#"));

                        String[] res_part = res.split("#enx#");
                        for (int i = 0; i < res_part.length; i++) {
                            String from_id = res_part[i].substring(res_part[i].indexOf("#1a#") + 4, res_part[i].indexOf("#2a#"));
                            String to_id = res_part[i].substring(res_part[i].indexOf("#2a#") + 4, res_part[i].indexOf("#3a#"));
                            String t_amount = res_part[i].substring(res_part[i].indexOf("#3a#") + 4, res_part[i].indexOf("#4a#"));
                            String t_time = res_part[i].substring(res_part[i].indexOf("#4a#") + 4, res_part[i].indexOf("#5a#"));
                            String t_desc = res_part[i].substring(res_part[i].indexOf("#5a#") + 4, res_part[i].indexOf("#6a#"));
                            String trn_id = res_part[i].substring(res_part[i].indexOf("#6a#") + 4, res_part[i].indexOf("#7a#"));
                            String from_name = res_part[i].substring(res_part[i].indexOf("#7a#") + 4, res_part[i].indexOf("#8a#"));
                            String to_name = res_part[i].substring(res_part[i].indexOf("#8a#") + 4, res_part[i].indexOf("#9a#"));

                            WalletHistoryModel walletHistoryModel = new WalletHistoryModel(from_id, to_id, to_name, t_time, t_amount, t_desc, from_name);
                            if (!(t_amount.equals("") || t_amount.equals("0"))) {
                                list_wallet_history.add(walletHistoryModel);

                                if (userID.equals(to_id)) {
                                    credit += Integer.parseInt(t_amount);
                                }

                                if (userID.equals(from_id)) {
                                    debit += Integer.parseInt(t_amount);
                                }
                            }
                        }

                        textView_credit.setText("Credit :- " + credit);
                        textView_debit.setText("Debit :- " + debit);
                    }

                    ///////Bonus history///////
                    String sss = response.substring(response.indexOf("#bnhs#") + 6, response.indexOf("#EEE#"));
                    String[] sss_part = sss.split("#eny#");
                    for (int i = 0; i < sss_part.length; i++) {
                        String dir_ref_bonus = sss_part[i].substring(sss_part[i].indexOf("#1b#") + 4, sss_part[i].indexOf("#2b#"));
                        String sec_ref_bonus = sss_part[i].substring(sss_part[i].indexOf("#2b#") + 4, sss_part[i].indexOf("#3b#"));
                        String direct_renewal_bonus = sss_part[i].substring(sss_part[i].indexOf("#3b#") + 4, sss_part[i].indexOf("#4b#"));
                        String target_bonus = sss_part[i].substring(sss_part[i].indexOf("#4b#") + 4, sss_part[i].indexOf("#5b#"));
                        String level_bonus = sss_part[i].substring(sss_part[i].indexOf("#5b#") + 4, sss_part[i].indexOf("#6b#"));
                        String income_date = sss_part[i].substring(sss_part[i].indexOf("#6b#") + 4, sss_part[i].indexOf("#7b#"));
                        String description = sss_part[i].substring(sss_part[i].indexOf("#7b#") + 4, sss_part[i].indexOf("#8b#"));
                        String b_type = "";
                        String b_amt = "";
                        if (description.length() <= 4) {
                            description = "Bonus by downline payment";
                        }
                        if (!dir_ref_bonus.equals("0")) {
                            b_type = "dir_ref_bonus";
                            b_amt = dir_ref_bonus;
                        }
                        if (!sec_ref_bonus.equals("0")) {
                            b_type = "sec_ref_bonus";
                            b_amt = sec_ref_bonus;
                        }
                        if (!direct_renewal_bonus.equals("0")) {
                            b_type = "direct_renewal_bonus";
                            b_amt = direct_renewal_bonus;
                        }
                        if (!target_bonus.equals("0")) {
                            b_type = "target_bonus";
                            b_amt = target_bonus;
                        }
                        if (!level_bonus.equals("0")) {
                            b_type = "level_bonus";
                            b_amt = level_bonus;
                        }
                        String from_id = "KTC LIFE bonus payment";
                        String to_id = userID;
                        String t_amount = b_amt;
                        String t_time = income_date;
                        String t_desc = description;
                        // String trn_id = "";
                        String from_name = "";
                        String to_name = userName;
                        WalletHistoryModel walletHistoryModel = new WalletHistoryModel(from_id, to_id, to_name, t_time, t_amount, t_desc, from_name);
                        if (!(t_amount.equals("") || t_amount.equals("0"))) {
                            list_wallet_history.add(walletHistoryModel);

                            if (userID.equals(to_id)) {
                                credit += Integer.parseInt(t_amount);
                            }

                            if (userID.equals(from_id)) {
                                debit += Integer.parseInt(t_amount);
                            }
                        }
                    }
                    textView_credit.setText("Credit :- " + credit);
                    textView_debit.setText("Debit :- " + debit);
                    ////// Compareable with date ///////
                    Collections.sort(list_wallet_history, new Comparator<WalletHistoryModel>() {
                        @Override
                        public int compare(WalletHistoryModel o1, WalletHistoryModel o2) {
                            return o2.date.compareTo(o1.date);
                        }
                    });
                    //////////////
                    walletHistoryListAdaptor = new WalletHistoryListAdaptor(BonusActivity.this, list_wallet_history);
                    rv_wallet_history.setAdapter((ListAdapter) walletHistoryListAdaptor);
                    dialog.dismiss();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(BonusActivity.this, "Server error", Toast.LENGTH_SHORT).show();
                dialog.dismiss();
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> map = new HashMap<>();
                map.put("id", userID);
                return map;
            }
        };
        RetryPolicy mRetryPolicy = new DefaultRetryPolicy(
                22000,
                //DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                0,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
        stringRequest.setRetryPolicy(mRetryPolicy);
        Volley.newRequestQueue(BonusActivity.this).add(stringRequest);
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

    public void method_idpayment(View view) {
        Intent intentPay = new Intent(BonusActivity.this, IdPaymentActivity.class);
        intentPay.putExtra("udataall", retriveUserData);
        startActivity(intentPay);
    }

    class internetChecking extends AsyncTask<String, Void, Integer> {

        @Override
        protected Integer doInBackground(String... params) {

            Integer result = 0;
            try {
                Socket socket = new Socket();
                SocketAddress socketAddress = new InetSocketAddress("8.8.8.8", 53);
                socket.connect(socketAddress, 12500);
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
                    Toast.makeText(BonusActivity.this, " No internet available ", Toast.LENGTH_SHORT).show();
                }
            } else {
                dialog.dismiss();
                Toast.makeText(BonusActivity.this, " No internet connection available ", Toast.LENGTH_SHORT).show();
            }
            super.onPostExecute(result);
        }
    }
}