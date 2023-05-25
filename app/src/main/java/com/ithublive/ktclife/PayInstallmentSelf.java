package com.ithublive.ktclife;

import android.Manifest;
import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
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

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.RetryPolicy;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.ithublive.ktclife.Utils.BaseUrl;
import com.paytm.pgsdk.PaytmOrder;
import com.paytm.pgsdk.PaytmPGService;
import com.paytm.pgsdk.PaytmPaymentTransactionCallback;
import com.razorpay.Checkout;
import com.razorpay.PaymentResultListener;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

public class PayInstallmentSelf extends AppCompatActivity implements PaymentResultListener { //implements PaytmPaymentTransactionCallback

    String allData, uname, plan, userId;
    double t_bonus;
    TextView tvInfo;
    Button button_pay;
    EditText etInstNum, etp_date, etAmt, etModePay, etUid;
    int paidCount;
    Dialog dialog;
    String my_Wallet_Password;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pay_installment_self);

        Checkout.preload(getApplicationContext());

        allData = getIntent().getStringExtra("udataall");
        etUid = findViewById(R.id.et_userid_payinstall);
        tvInfo = findViewById(R.id.tv_userInfo12_payinstall);
        etInstNum = findViewById(R.id.et_installmentNum13_payinstall);
        etp_date = findViewById(R.id.et_paydate14_payinstall);
        etAmt = findViewById(R.id.et_payAmount15_payinstall);
        etModePay = findViewById(R.id.et_payMode16_payinstall);
        button_pay = findViewById(R.id.btnMakePayment16_payinstall);

        button_pay.setEnabled(false);
        etUid.setEnabled(false);
        etInstNum.setEnabled(false);
        etp_date.setEnabled(false);
        etAmt.setEnabled(false);
        etModePay.setEnabled(false);
        userId = allData.substring(allData.indexOf("!") + 1, allData.indexOf("@"));
        etUid.setText(userId);

        dialog = new Dialog(PayInstallmentSelf.this);
        dialog.setContentView(R.layout.layout_progress_dialog);
        if (dialog.getWindow() != null) {
            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(0));
        }
        dialog.setCancelable(false);

        methodGetBonus(userId);
        uname = allData.substring(allData.indexOf("@") + 1, allData.indexOf("#"));
        plan = allData.substring(allData.indexOf("&") + 1, allData.indexOf("*"));
        paidCount = Integer.parseInt(allData.substring(allData.indexOf("#pn#") + 4, allData.indexOf("#epn#")));

        tvInfo.setText(" Name: " + uname + "\n Plan: " + plan);
        if (paidCount < 24) {
            etInstNum.setText((paidCount + 1) + "");
        }

        Calendar calendar = Calendar.getInstance();
        etAmt.setText(plan + "");
        etp_date.setText(calendar.get(Calendar.YEAR) + "-" + (calendar.get(Calendar.MONTH) + 1) + "-" + calendar.get(Calendar.DATE));
    }

    private void methodGetBonus(final String userId) {
        dialog.show();
        StringRequest stringRequest = new StringRequest(Request.Method.POST, BaseUrl.ktcBonusRequest, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                dialog.dismiss();
                if (response.contains("conn_error")) {
                    Toast.makeText(PayInstallmentSelf.this, "Server error. Try Again", Toast.LENGTH_SHORT).show();
                }

                if (response.contains("bnus")) {
                    dialog.dismiss();
                    button_pay.setEnabled(true);
                    try {
                        t_bonus = Double.parseDouble(response.substring(response.indexOf("#bnus#") + 6, response.indexOf("#eb#")));
                    } catch (NumberFormatException ex) {
                        t_bonus = 0;
                    }
                    my_Wallet_Password = response.substring(response.indexOf("#pss#") + 5, response.indexOf("#pse#"));
                    tvInfo.setText(" Name: " + uname + "\n Plan: " + plan + "\n Wallet Balance: " + t_bonus);
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(PayInstallmentSelf.this, "Server error", Toast.LENGTH_SHORT).show();
                dialog.dismiss();
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> map = new HashMap<>();
                map.put("uid", userId);
                return map;
            }
        };
        RetryPolicy mRetryPolicy = new DefaultRetryPolicy(
                22000,
                //DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                0,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
        stringRequest.setRetryPolicy(mRetryPolicy);
        Volley.newRequestQueue(PayInstallmentSelf.this).add(stringRequest);
    }

    public void payFromWallet(View view) {
        makePayment();
    }

    public void makePayment() {
        final Activity activity = this;
        Checkout checkout = new Checkout();
        checkout.setKeyID("rzp_test_xUzM9yrVh66Bmo");
        checkout.setImage(R.drawable.app_logo);
        double finalAMT = Float.parseFloat(etAmt.getText().toString()) * 100;
        try {
            JSONObject options = new JSONObject();
            options.put("name", "KTCLIFE SUCCESS PRIVATE LIMITED");
            options.put("description", "Installment number:- " + etInstNum.getText().toString());
            options.put("image", "https://s3.amazonaws.com/rzp-mobile/images/rzp.png");
            //options.put("order_id", "order_DBJOWzybf0sJbb");//from response of step 3.
            options.put("theme.color", "#606fe5");
            options.put("currency", "INR");
            options.put("amount", finalAMT + "");//pass amount in currency subunits
            options.put("prefill.email", "example@example.com");
            options.put("prefill.contact", etUid.getText().toString().substring(0, 10));
            /*JSONObject retryObj = new JSONObject();
            retryObj.put("enabled", true);
            retryObj.put("max_count", 4);
            options.put("retry", retryObj);*/
            checkout.open(activity, options);
        } catch (Exception e) {
            Toast.makeText(activity, "Error in starting Razorpay Checkout", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onPaymentSuccess(String payId) {
        StringRequest requestToMakePayment = new StringRequest(Request.Method.POST, BaseUrl.updateUserDataByAdminApp,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        dialog.dismiss();
                        if (response.contains("ins_success")) {
                            AlertDialog.Builder aleBuilder = new AlertDialog.Builder(PayInstallmentSelf.this);
                            aleBuilder.setTitle("KTC Life");
                            String su = "th";
                            if ((paidCount + 1) == 1 || (paidCount + 1) == 21) {
                                su = "st";
                            } else if ((paidCount + 1) == 2 || (paidCount + 1) == 22) {
                                su = "nd";
                            } else if ((paidCount + 1) == 3 || (paidCount + 1) == 23) {
                                su = "rd";
                            }
                            aleBuilder.setMessage("Your " + (paidCount + 1) + " " + su + " installment has been paid Successfully...! your payment id is " + payId);
                            aleBuilder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    startActivity(new Intent(PayInstallmentSelf.this, HomeActivity.class));
                                }
                            });
                            aleBuilder.setIcon(R.drawable.app_logo);
                            aleBuilder.setCancelable(false);
                            aleBuilder.show();
                        }
                        button_pay.setEnabled(true);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        dialog.dismiss();
                        Toast.makeText(PayInstallmentSelf.this, "Error...", Toast.LENGTH_SHORT).show();
                    }
                }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map map = new HashMap();
                map.put("uid", userId);
                map.put("inment_num", (paidCount + 1) + "");
                map.put("amt", plan);
                map.put("mode", "Razorpay, id is :- " + payId);
                map.put("date", etp_date.getText().toString().trim());
                map.put("req_reson", "ins_payment_razorpay");
                return map;
            }
        };
        RetryPolicy mRetryPolicy = new DefaultRetryPolicy(
                22000,
                0,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
        requestToMakePayment.setRetryPolicy(mRetryPolicy);
        Volley.newRequestQueue(PayInstallmentSelf.this).add(requestToMakePayment);
    }

    @Override
    public void onPaymentError(int i, String s) {
        Toast.makeText(this, "" + s, Toast.LENGTH_SHORT).show();
    }
}