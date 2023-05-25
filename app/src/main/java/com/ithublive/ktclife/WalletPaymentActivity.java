package com.ithublive.ktclife;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.RetryPolicy;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.textfield.TextInputLayout;
import com.ithublive.ktclife.Utils.BaseUrl;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class WalletPaymentActivity extends AppCompatActivity {

    EditText et_amount, et_note, et_ben_userid;
    Dialog dialog;
    String userID, userName, ben_name, ben_totalWalletBal, my_Walletbalance, my_Wallet_Password;
    TextView tv_showData;
    Button btn_pay, btn_clear, btn_getuserdata;
    TextInputLayout editText_amt, editText_note;
    String current_date, current_time;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wallet_payment);

        et_ben_userid = findViewById(R.id.et_userid);
        et_amount = findViewById(R.id.et_trn_amount);
        et_note = findViewById(R.id.et_note);
        tv_showData = findViewById(R.id.tv_showData);
        btn_pay = findViewById(R.id.button_Pay);
        btn_clear = findViewById(R.id.button_clear);
        btn_getuserdata = findViewById(R.id.button_GetUserInfo);

        editText_amt = (TextInputLayout) findViewById(R.id.edittext_amount);
        editText_note = (TextInputLayout) findViewById(R.id.edittext_note);

        tv_showData.setVisibility(View.INVISIBLE);
        et_amount.setVisibility(View.INVISIBLE);
        et_note.setVisibility(View.INVISIBLE);
        btn_pay.setVisibility(View.INVISIBLE);
        btn_clear.setVisibility(View.INVISIBLE);

        editText_amt.setVisibility(View.INVISIBLE);
        editText_note.setVisibility(View.INVISIBLE);

        dialog = new Dialog(WalletPaymentActivity.this);
        dialog.setContentView(R.layout.layout_progress_dialog);
        if (dialog.getWindow() != null) {
            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(0));
        }
        dialog.setCancelable(false);

        SharedPreferences shared = getSharedPreferences("" + R.string.sp_retriveUserData, MODE_PRIVATE);
        String retriveUserData = (shared.getString("data", ""));
        userID = retriveUserData.substring(retriveUserData.indexOf("!") + 1, retriveUserData.indexOf("@"));
        userName = retriveUserData.substring(retriveUserData.indexOf("@") + 1, retriveUserData.indexOf("#"));
    }

    public void getUserInfo(View view) {
        dialog.show();
        if (et_ben_userid.getText().toString().trim().length() < 11) {
            Toast.makeText(this, "Please enter valid beneficiary id.", Toast.LENGTH_SHORT).show();
            dialog.dismiss();
            return;
        } else {
            StringRequest stringRequest = new StringRequest(Request.Method.POST, BaseUrl.ktcWalletUrl, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {

                    if (response.contains("sql_con_er")) {
                        dialog.dismiss();
                        Toast.makeText(WalletPaymentActivity.this, "Database connection error.", Toast.LENGTH_SHORT).show();
                    } else if (response.contains("nahi_hai3434")) {
                        dialog.dismiss();
                        Toast.makeText(WalletPaymentActivity.this, "No Id found.", Toast.LENGTH_SHORT).show();
                    } else if (response.contains("#tr1#")) {
                        ben_name = response.substring(response.indexOf("#tr1#") + 5, response.indexOf("#tr2#"));
                        my_Walletbalance = response.substring(response.indexOf("#tr2#") + 5, response.indexOf("#tr3#"));
                        my_Wallet_Password = response.substring(response.indexOf("#tr3#") + 5, response.indexOf("#tr4#"));
                        ben_totalWalletBal = response.substring(response.indexOf("#tr5#") + 5, response.indexOf("#tr6#"));
                        //Log.d("res_payment", "pass " + my_Wallet_Password);

                        tv_showData.setVisibility(View.VISIBLE);
                        et_amount.setVisibility(View.VISIBLE);
                        et_note.setVisibility(View.VISIBLE);
                        btn_pay.setVisibility(View.VISIBLE);
                        btn_clear.setVisibility(View.VISIBLE);

                        editText_amt.setVisibility(View.VISIBLE);
                        editText_note.setVisibility(View.VISIBLE);

                        et_ben_userid.setEnabled(false);
                        btn_getuserdata.setEnabled(false);

                        tv_showData.setText("Beneficiary Name :- " + ben_name + "\n" + "Beneficiary Id :- "
                                + et_ben_userid.getText().toString() + "\n" + "My Wallet Balance :- " + my_Walletbalance);
                        dialog.dismiss();
                    }
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Toast.makeText(WalletPaymentActivity.this, "Server error.", Toast.LENGTH_LONG).show();
                    dialog.dismiss();
                }
            }) {
                @Override
                protected Map<String, String> getParams() throws AuthFailureError {
                    Map<String, String> map = new HashMap<>();
                    map.put("req_for", "get_Info");
                    map.put("req_id", userID);
                    map.put("ben_id", et_ben_userid.getText().toString());
                    return map;
                }
            };
            Volley.newRequestQueue(WalletPaymentActivity.this).add(stringRequest);
        }
    }

    public void sendingSms() {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, BaseUrl.walletSmsUrl, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                dialog.dismiss();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(WalletPaymentActivity.this, "Server error.", Toast.LENGTH_LONG).show();
                dialog.dismiss();
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                current_date = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()).format(new Date());
                current_time = new SimpleDateFormat("hh:mm:ss", Locale.getDefault()).format(new Date());
                int totalBal = Integer.parseInt(my_Walletbalance) - Integer.parseInt(et_amount.getText().toString());
                //sms sent to, who sent wallet money
                Map<String, String> map = new HashMap<>();
                map.put("sender", "yes");
                map.put("mobile", userID.substring(0, 10));
                map.put("senderMyId", userID);
                map.put("amt", et_amount.getText().toString());
                map.put("totalSenderWalletBal", "" + totalBal);
                map.put("recevierName", userName);
                map.put("date_time", current_date + "%20" + current_time + ".%20"); //'06-07-2021%2007:18.%20';
                //sms sent to, who receiver wallet money
                int benTotalBal = Integer.parseInt(ben_totalWalletBal) + Integer.parseInt(et_amount.getText().toString());
                map.put("receiver", "yes");
                map.put("mobileReceiver", et_ben_userid.getText().toString().substring(0, 10));
                map.put("receiverMyId", et_ben_userid.getText().toString());
                map.put("amtReceiver", et_amount.getText().toString());
                map.put("totalReceiverWalletBal", benTotalBal + "");
                map.put("senderName", userName);
                map.put("date_time_receiver", current_date + "%20" + current_time + ".%20"); //'06-07-2021%2007:18.%20';
                return map;
            }
        };
        RetryPolicy mRetryPolicy = new DefaultRetryPolicy(
                22000,
                //DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                0,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
        stringRequest.setRetryPolicy(mRetryPolicy);
        Volley.newRequestQueue(WalletPaymentActivity.this).add(stringRequest);
    }

    public void methodPay(View view) {
        dialog.show();
        if ((et_amount.getText().toString().isEmpty()) || (Integer.parseInt(et_amount.getText().toString().trim()) <= 0)) {
            Toast.makeText(this, "Please enter amount.", Toast.LENGTH_SHORT).show();
            dialog.dismiss();
            et_amount.setError("Please enter amount.");
            et_amount.requestFocus();
        } else if (Integer.parseInt(et_amount.getText().toString()) > Integer.parseInt(my_Walletbalance)) {
            Toast.makeText(this, "Amount is more than your wallet balance.", Toast.LENGTH_SHORT).show();
            dialog.dismiss();
            et_amount.setError("Amount is more than your wallet balance.");
            et_amount.requestFocus();
        } else if (my_Wallet_Password.isEmpty() || my_Wallet_Password == null) {
            dialog.dismiss();
            method_SetWalletPassword();
        } else {
            dialog.dismiss();
            AlertDialog.Builder adb = new AlertDialog.Builder(WalletPaymentActivity.this);
            adb.setTitle(R.string.dialog_title);
            adb.setCancelable(false);
            View view_1 = getLayoutInflater().inflate(R.layout.activity_wallet_password, null);

            final EditText et_1, et_2, et_3, et_4;
            Button btn_submit_enter_pass;

            et_1 = view_1.findViewById(R.id.et_pass_1);
            et_2 = view_1.findViewById(R.id.et_pass_2);
            et_3 = view_1.findViewById(R.id.et_pass_3);
            et_4 = view_1.findViewById(R.id.et_pass_4);

            et_1.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                }

                @Override
                public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                    if (et_1.hasFocus()) {
                        if (et_1.getText().toString().trim().length() == 1) {
                            //et_old3.clearFocus();
                            new Handler().postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    et_2.requestFocus();
                                }
                            }, 85);
                        }
                        if (et_1.getText().toString().trim().length() == 0) {
                            new Handler().postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    // et_1.requestFocus();
                                }
                            }, 85);
                        }
                    }
                }

                @Override
                public void afterTextChanged(Editable editable) {

                }
            });
            et_2.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                }

                @Override
                public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                    if (et_2.hasFocus()) {
                        if (et_2.getText().toString().trim().length() == 1) {
                            //et_old3.clearFocus();
                            new Handler().postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    et_3.requestFocus();
                                }
                            }, 85);
                        }
                        if (et_2.getText().toString().trim().length() == 0) {
                            new Handler().postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    et_1.requestFocus();
                                }
                            }, 85);
                        }
                    }
                }

                @Override
                public void afterTextChanged(Editable editable) {

                }
            });

            et_3.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                }

                @Override
                public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                    if (et_3.hasFocus()) {
                        if (et_3.getText().toString().trim().length() == 1) {
                            //et_old3.clearFocus();
                            new Handler().postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    et_4.requestFocus();
                                }
                            }, 85);
                        }
                        if (et_3.getText().toString().trim().length() == 0) {
                            new Handler().postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    et_2.requestFocus();
                                }
                            }, 85);
                        }
                    }
                }

                @Override
                public void afterTextChanged(Editable editable) {

                }
            });
            et_4.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                }

                @Override
                public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                    if (et_4.hasFocus()) {
                        if (et_4.getText().toString().trim().length() == 1) {
                            //et_old3.clearFocus();
                            new Handler().postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    //et_2.requestFocus();
                                }
                            }, 85);
                        }
                        if (et_4.getText().toString().trim().length() == 0) {
                            new Handler().postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    et_3.requestFocus();
                                }
                            }, 85);
                        }
                    }
                }

                @Override
                public void afterTextChanged(Editable editable) {

                }
            });

            btn_submit_enter_pass = view_1.findViewById(R.id.btn_submitdata_enter_pass);

            btn_submit_enter_pass.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    dialog.show();
                    final String wallet_password = et_1.getText().toString() + et_2.getText().toString() + et_3.getText().toString() + et_4.getText().toString();

                    if (et_1.getText().toString().isEmpty() || et_2.getText().toString().isEmpty() || et_3.getText().toString().isEmpty()
                            || et_4.getText().toString().isEmpty()) {
                        dialog.dismiss();
                        Toast.makeText(WalletPaymentActivity.this, "Please fill all fields.", Toast.LENGTH_SHORT).show();
                    } else if (!(wallet_password.equals(my_Wallet_Password))) {
                        dialog.dismiss();
                        Toast.makeText(WalletPaymentActivity.this, "Wrong password, please enter valid passowrd.", Toast.LENGTH_SHORT).show();
                    } else {
                        dialog.show();
                        StringRequest stringRequest = new StringRequest(Request.Method.POST, BaseUrl.ktcWalletUrl, new Response.Listener<String>() {
                            @Override
                            public void onResponse(String response) {
                                Log.d("res_payment", "res " + response);

                                if (response.contains("tr_fail")) {
                                    dialog.dismiss();
                                    Toast.makeText(WalletPaymentActivity.this, "Your transaction failed.", Toast.LENGTH_SHORT).show();
                                }

                                if (response.contains("sql_con_er")) {
                                    dialog.dismiss();
                                    Toast.makeText(WalletPaymentActivity.this, "Database connection error.", Toast.LENGTH_SHORT).show();
                                }

                                if (response.contains("trx_succ")) {
                                    dialog.dismiss();
                                    Toast.makeText(WalletPaymentActivity.this, "Your transaction was Successful.", Toast.LENGTH_LONG).show();
                                    sendingSms();
                                    String trn_id = response.substring(response.indexOf("#aa#") + 4, response.indexOf("#tee#"));
                                    final AlertDialog.Builder adb = new AlertDialog.Builder(WalletPaymentActivity.this);
                                    adb.setTitle(R.string.dialog_title);
                                    adb.setMessage("Your transaction was Successful, transaction id is " + trn_id);
                                    adb.setIcon(R.drawable.app_logo);
                                    adb.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialogInterface, int i) {

                                            et_ben_userid.setEnabled(true);
                                            et_ben_userid.setText("");
                                            btn_getuserdata.setEnabled(true);

                                            tv_showData.setVisibility(View.INVISIBLE);
                                            et_amount.setVisibility(View.INVISIBLE);
                                            et_note.setVisibility(View.INVISIBLE);
                                            btn_pay.setVisibility(View.INVISIBLE);
                                            btn_clear.setVisibility(View.INVISIBLE);

                                            editText_amt.setVisibility(View.INVISIBLE);
                                            editText_note.setVisibility(View.INVISIBLE);

                                            tv_showData.setText("");
                                            et_amount.setText("");
                                            et_note.setText("");
                                            dialogInterface.dismiss();
                                            onBackPressed();
                                        }
                                    });
                                    adb.setCancelable(false);
                                    adb.show();
                                }
                            }
                        }, new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {
                                Toast.makeText(WalletPaymentActivity.this, "Server error.", Toast.LENGTH_LONG).show();
                                dialog.dismiss();
                            }
                        }) {
                            @Override
                            protected Map<String, String> getParams() throws AuthFailureError {
                                Map<String, String> map = new HashMap<>();
                                map.put("req_for", "txx_fr");
                                map.put("req_id", userID);
                                map.put("ben_id", et_ben_userid.getText().toString());
                                map.put("amt", et_amount.getText().toString());
                                map.put("desc", et_note.getText().toString());
                                return map;
                            }
                        };
                        RetryPolicy mRetryPolicy = new DefaultRetryPolicy(
                                22000,
                                //DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                                0,
                                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
                        stringRequest.setRetryPolicy(mRetryPolicy);
                        Volley.newRequestQueue(WalletPaymentActivity.this).add(stringRequest);
                    }
                }
            });

            adb.setPositiveButton("Cancel", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    dialogInterface.dismiss();
                }
            });
            adb.setView(view_1);
            adb.setIcon(R.drawable.app_logo);
            adb.show();
        }
    }

    public void method_SetWalletPassword() {
        final AlertDialog.Builder adbX = new AlertDialog.Builder(WalletPaymentActivity.this);
        final AlertDialog adb = adbX.create();
        adb.setTitle(R.string.dialog_title);
        adb.setCancelable(false);
        View view = getLayoutInflater().inflate(R.layout.activity_wallet_set_password, null);

        final EditText et_1, et_2, et_3, et_4, et_conf_1, et_conf_2, et_conf_3, et_conf_4;
        Button btn_submit;

        et_1 = view.findViewById(R.id.et_pass_1);
        et_2 = view.findViewById(R.id.et_pass_2);
        et_3 = view.findViewById(R.id.et_pass_3);
        et_4 = view.findViewById(R.id.et_pass_4);

        et_conf_1 = view.findViewById(R.id.et_conf_pass_1);
        et_conf_2 = view.findViewById(R.id.et_conf_pass_2);
        et_conf_3 = view.findViewById(R.id.et_conf_pass_3);
        et_conf_4 = view.findViewById(R.id.et_conf_pass_4);

        et_1.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                if (et_1.hasFocus()) {
                    if (et_1.getText().toString().trim().length() == 1) {
                        //et_old3.clearFocus();
                        new Handler().postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                et_2.requestFocus();
                            }
                        }, 85);
                    }
                    if (et_1.getText().toString().trim().length() == 0) {
                        new Handler().postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                // et_1.requestFocus();
                            }
                        }, 85);
                    }
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        et_2.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                if (et_2.hasFocus()) {
                    if (et_2.getText().toString().trim().length() == 1) {
                        //et_old3.clearFocus();
                        new Handler().postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                et_3.requestFocus();
                            }
                        }, 85);
                    }
                    if (et_2.getText().toString().trim().length() == 0) {
                        new Handler().postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                et_1.requestFocus();
                            }
                        }, 85);
                    }
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        et_3.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                if (et_3.hasFocus()) {
                    if (et_3.getText().toString().trim().length() == 1) {
                        //et_old3.clearFocus();
                        new Handler().postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                et_4.requestFocus();
                            }
                        }, 85);
                    }
                    if (et_3.getText().toString().trim().length() == 0) {
                        new Handler().postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                et_2.requestFocus();
                            }
                        }, 85);
                    }
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        et_4.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                if (et_4.hasFocus()) {
                    if (et_4.getText().toString().trim().length() == 1) {
                        //et_old3.clearFocus();
                        new Handler().postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                et_conf_1.requestFocus();
                            }
                        }, 85);
                    }
                    if (et_4.getText().toString().trim().length() == 0) {
                        new Handler().postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                et_3.requestFocus();
                            }
                        }, 85);
                    }
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        et_conf_1.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                if (et_conf_1.hasFocus()) {
                    if (et_conf_1.getText().toString().trim().length() == 1) {
                        //et_old3.clearFocus();
                        new Handler().postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                et_conf_2.requestFocus();
                            }
                        }, 85);
                    }
                    if (et_conf_1.getText().toString().trim().length() == 0) {
                        new Handler().postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                //et_1.requestFocus();
                            }
                        }, 85);
                    }
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        et_conf_2.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                if (et_conf_2.hasFocus()) {
                    if (et_conf_2.getText().toString().trim().length() == 1) {
                        //et_old3.clearFocus();
                        new Handler().postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                et_conf_3.requestFocus();
                            }
                        }, 85);
                    }
                    if (et_conf_2.getText().toString().trim().length() == 0) {
                        new Handler().postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                et_conf_1.requestFocus();
                            }
                        }, 85);
                    }
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        et_conf_3.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                if (et_conf_3.hasFocus()) {
                    if (et_conf_3.getText().toString().trim().length() == 1) {
                        //et_old3.clearFocus();
                        new Handler().postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                et_conf_4.requestFocus();
                            }
                        }, 85);
                    }
                    if (et_conf_3.getText().toString().trim().length() == 0) {
                        new Handler().postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                et_conf_2.requestFocus();
                            }
                        }, 85);
                    }
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
        et_conf_4.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                if (et_conf_4.hasFocus()) {
                    if (et_conf_4.getText().toString().trim().length() == 1) {
                        //et_old3.clearFocus();
                        new Handler().postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                // et_conf_3.requestFocus();
                            }
                        }, 85);
                    }
                    if (et_conf_4.getText().toString().trim().length() == 0) {
                        new Handler().postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                et_conf_3.requestFocus();
                            }
                        }, 85);
                    }
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        btn_submit = view.findViewById(R.id.btn_submit_password);
        btn_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.show();
                if (et_1.getText().toString().isEmpty() || et_2.getText().toString().isEmpty() || et_3.getText().toString().isEmpty()
                        || et_4.getText().toString().isEmpty() || et_conf_1.getText().toString().isEmpty() || et_conf_2.getText().toString().isEmpty()
                        || et_conf_3.getText().toString().isEmpty() || et_conf_4.getText().toString().isEmpty()) {
                    dialog.dismiss();
                    Toast.makeText(WalletPaymentActivity.this, "Please fill all fields.", Toast.LENGTH_SHORT).show();
                } else if (!(et_1.getText().toString().equals(et_conf_1.getText().toString()))) {
                    dialog.dismiss();
                    Toast.makeText(WalletPaymentActivity.this, "Your password is doesn't match.", Toast.LENGTH_SHORT).show();
                } else if (!(et_2.getText().toString().equals(et_conf_2.getText().toString()))) {
                    dialog.dismiss();
                    Toast.makeText(WalletPaymentActivity.this, "Your password is doesn't match.", Toast.LENGTH_SHORT).show();
                } else if (!(et_3.getText().toString().equals(et_conf_3.getText().toString()))) {
                    dialog.dismiss();
                    Toast.makeText(WalletPaymentActivity.this, "Your password is doesn't match.", Toast.LENGTH_SHORT).show();
                } else if (!(et_4.getText().toString().equals(et_conf_4.getText().toString()))) {
                    dialog.dismiss();
                    Toast.makeText(WalletPaymentActivity.this, "Your password is doesn't match.", Toast.LENGTH_SHORT).show();
                } else {
                    dialog.show();
                    StringRequest stringRequest = new StringRequest(Request.Method.POST, BaseUrl.ktcWallet_setpasswordUrl, new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {

                            if (response.contains("connection not got")) {
                                dialog.dismiss();
                                Toast.makeText(WalletPaymentActivity.this, "Database connection error.", Toast.LENGTH_SHORT).show();
                            }

                            if (response.contains("password_set")) {
                                my_Wallet_Password = response.substring(response.indexOf("password_set") + 12);
                                dialog.dismiss();
                                adb.dismiss();
                                Toast.makeText(WalletPaymentActivity.this, "Password set successfully.", Toast.LENGTH_SHORT).show();
                                et_1.setText("");
                                et_2.setText("");
                                et_3.setText("");
                                et_4.setText("");
                                et_conf_1.setText("");
                                et_conf_2.setText("");
                                et_conf_3.setText("");
                                et_conf_4.setText("");
                            }

                            if (response.contains("password_not_set")) {
                                dialog.dismiss();
                                Toast.makeText(WalletPaymentActivity.this, "Password not set.", Toast.LENGTH_SHORT).show();
                            }
                        }
                    }, new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            Toast.makeText(WalletPaymentActivity.this, "Server error.", Toast.LENGTH_LONG).show();
                            dialog.dismiss();
                        }
                    }) {
                        @Override
                        protected Map<String, String> getParams() throws AuthFailureError {
                            Map<String, String> map = new HashMap<>();
                            map.put("id", userID);
                            map.put("wallet_password", et_conf_1.getText().toString() + et_conf_2.getText().toString() + et_conf_3.getText().toString() +
                                    et_conf_4.getText().toString());
                            return map;
                        }
                    };
                    RetryPolicy mRetryPolicy = new DefaultRetryPolicy(
                            20000,
                            //DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                            1,
                            DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
                    stringRequest.setRetryPolicy(mRetryPolicy);
                    Volley.newRequestQueue(WalletPaymentActivity.this).add(stringRequest);
                }
            }
        });

        adbX.setPositiveButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialog.dismiss();
                dialogInterface.dismiss();
            }
        });
        adb.setView(view);
        adb.setIcon(R.drawable.app_logo);
        adb.show();
    }

    public void methodAllDataClear(View view) {
        et_ben_userid.setEnabled(true);
        et_ben_userid.setText("");

        btn_getuserdata.setEnabled(true);

        tv_showData.setVisibility(View.INVISIBLE);
        et_amount.setVisibility(View.INVISIBLE);
        et_note.setVisibility(View.INVISIBLE);
        btn_pay.setVisibility(View.INVISIBLE);
        btn_clear.setVisibility(View.INVISIBLE);

        editText_amt.setVisibility(View.INVISIBLE);
        editText_note.setVisibility(View.INVISIBLE);

        tv_showData.setText("");
        et_amount.setText("");
        et_note.setText("");
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        dialog.dismiss();
    }
}