package com.ithublive.ktclife.Admin;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.ScaleGestureDetector;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkError;
import com.android.volley.NoConnectionError;
import com.android.volley.ParseError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.ServerError;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.ithublive.ktclife.OurProfileActivity;
import com.ithublive.ktclife.R;
import com.ithublive.ktclife.Utils.BaseUrl;
import com.ortiz.touchview.TouchImageView;
import com.squareup.picasso.Picasso;

import java.util.HashMap;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;

public class PendingUsersKycDetails extends AppCompatActivity {

    String intent_id, intent_name, intent_mobile, intent_profile_photo, intent_ref_id, intent_plan, intent_paid, intent_address, intent_doj, intent_kyc,
            intent_aadharno, intent_aadhar_front_photo, intent_aadhar_back_photo, intent_bank_ac_photo, intent_bank_ac_no, intent_bank_ac_ifsc;

    CircleImageView profile_photo;
    EditText et_userid, et_name, et_mobileno, et_ref, et_plan, et_paid, et_address, et_doj, et_kyc, et_aadharnumber, et_accountNumber, et_ifsc;
    ImageView iv_aadharfront, iv_aadharback, iv_bankaccount;
    Dialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pending_users_kyc_details);

        findViewId();
        intentData();
        et_userid.setText(intent_id);
        et_name.setText(intent_name);
        et_mobileno.setText(intent_mobile);
        et_ref.setText(intent_ref_id);
        et_plan.setText(intent_plan);
        et_paid.setText(intent_paid);
        et_address.setText(intent_address);
        et_doj.setText(intent_doj);
        et_kyc.setText(intent_kyc);
        et_aadharnumber.setText(intent_aadharno);
        et_accountNumber.setText(intent_bank_ac_no);
        et_ifsc.setText(intent_bank_ac_ifsc);

        dialog = new Dialog(PendingUsersKycDetails.this);
        dialog.setContentView(R.layout.layout_progress_dialog);
        if (dialog.getWindow() != null) {
            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(0));
        }
        dialog.setCancelable(false);

        if (intent_profile_photo.isEmpty()) {
            Picasso.with(PendingUsersKycDetails.this).load(R.drawable.ic_user_dummy).into(profile_photo);
        } else {
            Picasso.with(PendingUsersKycDetails.this).load(intent_profile_photo).into(profile_photo);
        }

        if (intent_aadhar_front_photo.isEmpty()) {
            Picasso.with(PendingUsersKycDetails.this).load(R.drawable.dummy_front_aadhar).into(iv_aadharfront);
        } else {
            Picasso.with(PendingUsersKycDetails.this).load(intent_aadhar_front_photo).into(iv_aadharfront);
        }

        if (intent_aadhar_back_photo.isEmpty()) {
            Picasso.with(PendingUsersKycDetails.this).load(R.drawable.dummy_back_aadhar).into(iv_aadharback);
        } else {
            Picasso.with(PendingUsersKycDetails.this).load(intent_aadhar_back_photo).into(iv_aadharback);
        }

        if (intent_bank_ac_photo.isEmpty()) {
            Picasso.with(PendingUsersKycDetails.this).load(R.drawable.dummy_cheque_passbook).into(iv_bankaccount);
        } else {
            Picasso.with(PendingUsersKycDetails.this).load(intent_bank_ac_photo).into(iv_bankaccount);
        }

        profile_photo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                LayoutInflater inflater = getLayoutInflater();
                View popup = inflater.inflate(R.layout.popupimage, null);
                final TouchImageView iv = popup.findViewById(R.id.imageview);

                AlertDialog.Builder adb = new AlertDialog.Builder(PendingUsersKycDetails.this);
                adb.setTitle("Profile Photo");
                adb.setView(popup);
                if (intent_profile_photo.isEmpty()) {
                    Picasso.with(PendingUsersKycDetails.this).load(R.drawable.ic_user_dummy).into(iv);
                } else {
                    Picasso.with(PendingUsersKycDetails.this).load(intent_profile_photo).into(iv);
                }
                AlertDialog dialog = adb.create();
                dialog.show();
            }
        });

        iv_aadharfront.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                LayoutInflater inflater = getLayoutInflater();
                View popup = inflater.inflate(R.layout.popupimage, null);
                final TouchImageView iv = popup.findViewById(R.id.imageview);

                AlertDialog.Builder adb = new AlertDialog.Builder(PendingUsersKycDetails.this);
                adb.setTitle("Front Side Aadhar");
                adb.setView(popup);
                if (intent_aadhar_front_photo.isEmpty()) {
                    Picasso.with(PendingUsersKycDetails.this).load(R.drawable.dummy_front_aadhar).into(iv);
                } else {
                    Picasso.with(PendingUsersKycDetails.this).load(intent_aadhar_front_photo).into(iv);
                }
                AlertDialog dialog = adb.create();
                dialog.show();
            }
        });

        iv_aadharback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                LayoutInflater inflater = getLayoutInflater();
                View popup = inflater.inflate(R.layout.popupimage, null);
                final TouchImageView iv = popup.findViewById(R.id.imageview);

                AlertDialog.Builder adb = new AlertDialog.Builder(PendingUsersKycDetails.this);
                adb.setTitle("Back Side Aadhar");
                adb.setView(popup);
                if (intent_aadhar_back_photo.isEmpty()) {
                    Picasso.with(PendingUsersKycDetails.this).load(R.drawable.dummy_back_aadhar).into(iv);
                } else {
                    Picasso.with(PendingUsersKycDetails.this).load(intent_aadhar_back_photo).into(iv);
                }
                AlertDialog dialog = adb.create();
                dialog.show();
            }
        });

        iv_bankaccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                LayoutInflater inflater = getLayoutInflater();
                View popup = inflater.inflate(R.layout.popupimage, null);
                final TouchImageView iv = popup.findViewById(R.id.imageview);

                AlertDialog.Builder adb = new AlertDialog.Builder(PendingUsersKycDetails.this);
                adb.setTitle("Bank Account");
                adb.setView(popup);
                if (intent_bank_ac_photo.isEmpty()) {
                    Picasso.with(PendingUsersKycDetails.this).load(R.drawable.dummy_cheque_passbook).into(iv);
                } else {
                    Picasso.with(PendingUsersKycDetails.this).load(intent_bank_ac_photo).into(iv);
                }

                AlertDialog dialog = adb.create();
                dialog.show();
            }
        });
    }

    public void methodAccept(View view) {
        dialog.show();
        kycStatusSentToDb(intent_id, "Yes");
    }

    public void methodReject(View view) {
        dialog.show();
        kycStatusSentToDb(intent_id, "No");
    }

    public void kycStatusSentToDb(String id, String status) {
        StringRequest request = new StringRequest(Request.Method.POST, BaseUrl.adminKycUpdate, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                if (response.contains("data_updated")) {
                    dialog.dismiss();
                    onBackPressed();
                    Toast.makeText(PendingUsersKycDetails.this, "Data updated...", Toast.LENGTH_SHORT).show();
                }

                if (response.contains("Not_updated")) {
                    dialog.dismiss();
                    Toast.makeText(PendingUsersKycDetails.this, "Data not updated...", Toast.LENGTH_SHORT).show();
                }

                if (response.contains("connection_not_got")) {
                    dialog.dismiss();
                    Toast.makeText(PendingUsersKycDetails.this, "Database connection failed...", Toast.LENGTH_SHORT).show();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                dialog.dismiss();
                String message = null;
                if (error instanceof NetworkError) {
                    message = "Cannot connect to Internet...Please check your connection!";
                    Toast.makeText(PendingUsersKycDetails.this, message, Toast.LENGTH_SHORT).show();
                } else if (error instanceof ServerError) {
                    message = "The server could not be found. Please try again after some time!!";
                    Toast.makeText(PendingUsersKycDetails.this, message, Toast.LENGTH_SHORT).show();
                } else if (error instanceof ParseError) {
                    message = "Parsing error! Please try again after some time!!";
                    Toast.makeText(PendingUsersKycDetails.this, message, Toast.LENGTH_SHORT).show();
                } else if (error instanceof TimeoutError || error instanceof NoConnectionError) {
                    message = "either time out or there is no connection! Please try again after some time!!";
                    Toast.makeText(PendingUsersKycDetails.this, message, Toast.LENGTH_SHORT).show();
                }
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> map = new HashMap<>();
                map.put("id", id);
                map.put("status", status);
                return map;
            }
        };
        Volley.newRequestQueue(PendingUsersKycDetails.this).add(request);
    }

    private void intentData() {
        intent_id = getIntent().getStringExtra("id");
        intent_name = getIntent().getStringExtra("name");
        intent_mobile = getIntent().getStringExtra("mobile");
        intent_profile_photo = getIntent().getStringExtra("profile_photo");
        intent_ref_id = getIntent().getStringExtra("ref_id");
        intent_plan = getIntent().getStringExtra("plan");
        intent_paid = getIntent().getStringExtra("paid");
        intent_address = getIntent().getStringExtra("address");
        intent_doj = getIntent().getStringExtra("doj");
        intent_kyc = getIntent().getStringExtra("kyc");
        intent_aadharno = getIntent().getStringExtra("aadhar_no");
        intent_aadhar_front_photo = getIntent().getStringExtra("aadhar_front_photo");
        intent_aadhar_back_photo = getIntent().getStringExtra("aadhar_back_photo");
        intent_bank_ac_photo = getIntent().getStringExtra("back_ac_photo");
        intent_bank_ac_no = getIntent().getStringExtra("bank_ac_no");
        intent_bank_ac_ifsc = getIntent().getStringExtra("bank_ac_ifsc");
    }

    private void findViewId() {
        profile_photo = findViewById(R.id.imageview_profile);
        et_userid = findViewById(R.id.et_id);
        et_name = findViewById(R.id.et_profile_name);
        et_mobileno = findViewById(R.id.et_profile_mobilenumber);
        et_ref = findViewById(R.id.et_profile_reference);
        et_plan = findViewById(R.id.et_plan);
        et_paid = findViewById(R.id.et_profile_paid);
        et_address = findViewById(R.id.et_profile_address);
        et_doj = findViewById(R.id.et_profile_doj);
        et_kyc = findViewById(R.id.et_profile_kyc);
        et_aadharnumber = findViewById(R.id.et_profile_aadharnumber);
        et_accountNumber = findViewById(R.id.et_profile_bankac_number);
        et_ifsc = findViewById(R.id.et_profile_ifsc_code);
        iv_aadharfront = findViewById(R.id.aadharcard_front_photo);
        iv_aadharback = findViewById(R.id.aadharcard_back_photo);
        iv_bankaccount = findViewById(R.id.bankaccount_photo);
    }
}