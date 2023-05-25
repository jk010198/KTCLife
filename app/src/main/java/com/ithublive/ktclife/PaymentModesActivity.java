package com.ithublive.ktclife;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;

public class PaymentModesActivity extends AppCompatActivity {

    ImageButton imageButton_phonePay, imageButton_googlePay, imageButton_paytm;
    final int UPI_ID = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment_modes);

        imageButton_phonePay = findViewById(R.id.imagebtn_PhonePay);
        imageButton_googlePay = findViewById(R.id.imagebtn_googlePay);
        imageButton_paytm = findViewById(R.id.imagebtn_PayTm);

        imageButton_phonePay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                payUsingUPI("", "8286357171@upi", "ktc", "KTC LIFE payment");
            }
        });

        imageButton_googlePay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                payUsingUPI("", "8286357171@upi", "ktc", "KTC LIFE payment");
            }
        });

        imageButton_paytm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                payUsingUPI("", "8286357171@upi", "", "KTC LIFE payment");
            }
        });

    }

    public void payUsingUPI(String amount, String upi_id, String user_name, String notes) {
        Uri uri = Uri.parse("upi://pay").buildUpon()
                .appendQueryParameter("pa", upi_id)
                .appendQueryParameter("pn", user_name)
                .appendQueryParameter("tn", notes)
                .appendQueryParameter("am", amount)
                .appendQueryParameter("cu", "INR")
                .build();

        Intent upi_intent = new Intent(Intent.ACTION_VIEW);
        upi_intent.setData(uri);

        Intent chooser = Intent.createChooser(upi_intent, "Pay with");

        if (null != chooser.resolveActivity(getPackageManager())) {
            startActivityForResult(chooser, UPI_ID);
        } else {
            Toast.makeText(this, "No UPI app found, please install one to continue.", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        switch (requestCode) {
            case UPI_ID:

                if ((RESULT_OK == resultCode) || (resultCode == 11)) {
                    if (data != null) {
                        String text = data.getStringExtra("response");
                        Log.d("UPI", "OnActivityResult" + text);
                        ArrayList<String> datalist = new ArrayList<>();
                        datalist.add(text);
                        upiPaymentDataOperation(datalist);
                    } else {
                        Log.d("UPI", "OnActivityResult" + "Return data is null");
                        ArrayList<String> datalist = new ArrayList<>();
                        datalist.add("nothing");
                        upiPaymentDataOperation(datalist);
                    }
                } else {
                    Log.d("UPI", "OnActivityResult" + "Return data is null"); // when user simply back without payment.
                    ArrayList<String> datalist = new ArrayList<>();
                    datalist.add("nothing");
                    upiPaymentDataOperation(datalist);
                }
                break;
        }
    }

    private void upiPaymentDataOperation(ArrayList<String> data) {

        if (isConnectionAvailable(PaymentModesActivity.this)) {
            String str = data.get(0);
            //Log.d("UPIPAYJK", "upiPaymentDataOperation: " + str);
            String paymentCancel = "";
            if (str == null) str = "discard";
            String status = "";
            String approvalRefNo = "";
            String response[] = str.split("&");
            for (int i = 0; i < response.length; i++) {
                String equalStr[] = response[i].split("=");
                if (str.contains("nothing")) {
                } else {
                    if (equalStr.length >= 2) {
                        if (equalStr[0].toLowerCase().equals("Status".toLowerCase())) {
                            status = equalStr[1].toLowerCase();
                        } else if (equalStr[0].toLowerCase().equals("ApprovalRefNo".toLowerCase()) || equalStr[0].toLowerCase().equals("txnRef".toLowerCase())) {
                            approvalRefNo = equalStr[1];
                        }
                    } else {
                        paymentCancel = "Transaction cancelled by user.";
                    }
                }
            }

            if (status.equals("success")) {
                //Code to handle successful transaction here.
                Toast.makeText(PaymentModesActivity.this, "Transaction successful.", Toast.LENGTH_SHORT).show();
                //Log.d("UPI", "responseStr: " + approvalRefNo);
            } else if ("Transaction cancelled by user.".equals(paymentCancel)) {
                Toast.makeText(PaymentModesActivity.this, "Transaction cancelled by user.", Toast.LENGTH_SHORT).show();
            } else {
                //  Toast.makeText(PaymentModesActivity.this, "Transaction failed.Please try again", Toast.LENGTH_SHORT).show();
            }
        } else {
            Toast.makeText(PaymentModesActivity.this, "Internet connection is not available. Please check and try again.", Toast.LENGTH_SHORT).show();
        }

    }

    public static boolean isConnectionAvailable(Context context) {
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        if (connectivityManager != null) {
            NetworkInfo netInfo = connectivityManager.getActiveNetworkInfo();
            if (netInfo != null && netInfo.isConnected()
                    && netInfo.isConnectedOrConnecting()
                    && netInfo.isAvailable()) {
                return true;
            }
        }
        return false;
    }
}