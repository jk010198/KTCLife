package com.ithublive.ktclife;

import android.Manifest;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.app.Service;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.drawable.ColorDrawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.provider.MediaStore;
import android.provider.Settings;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkError;
import com.android.volley.NetworkResponse;
import com.android.volley.NoConnectionError;
import com.android.volley.ParseError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.ServerError;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.github.drjacky.imagepicker.ImagePicker;
import com.ithublive.ktclife.Admin.PendingUsersKycDetails;
import com.ithublive.ktclife.Utils.BaseUrl;
import com.ortiz.touchview.TouchImageView;
import com.squareup.picasso.Picasso;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import org.jetbrains.annotations.NotNull;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketAddress;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URLConnection;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.Random;

import de.hdodenhof.circleimageview.CircleImageView;
import kotlin.Unit;
import kotlin.jvm.functions.Function1;
import kotlin.jvm.internal.Intrinsics;

public class OurProfileActivity extends AppCompatActivity {

    String Id, ProfileUrl, Plan, Password, oldImageName, oldAccountDetailsName, dbIs_kyc, dbAadhar_number, dbAadhar_front_url,
            dbAadhar_back_url, dbAccount_details_url;
    CircleImageView profileImg, add_profileimg;
    EditText et_name, et_mobileno, et_ref, et_address, et_password, et_aadharnumber, et_accountNumber, et_confAccountNumber, et_ifsc, et_otp,
            et_mobileforOTP, et_conf_password;
    Button btn_profile_getotp, btn_profile_submitdata;
    TextView tv_kyc_status;
    ImageView backbtn, aadhar_front, aadhar_back, bank_account;
    int otpNumber = 0;
    String storagePermission[], current_date, current_time;
    private static final int STORAGE_REQUEST = 100;
    int imageViewCode;
    String plans[] = {"-- Select Plans --", "1000", "2000", "3000", "4000", "5000", "10000", "15000", "20000"};
    Spinner planSpinner;
    private static final int PICK_IMAGE_REQUEST = 1;
    private Bitmap profile_bitmap, frontaadhar_bitmap, backaadhar_bitmap, account_bitmap;
    private String filePath = "empty", frontaadhar_filePath = "empty", backaadhar_filePath = "empty", account_filePath = "empty";
    int spinnerPosition;
    boolean isNetConnected;
    Dialog dialog;
    String name, address, plan, password, aadharNumber, accountNumber, confAccountNumber, ifscCode, conf_password, mobileNumberForOTP;
    TextView tv_view_front_aadhar, tv_view_back_aadhar, tv_view_bank_ac;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_our_profile);

        tv_kyc_status = findViewById(R.id.tv_kyc_status);
        et_name = findViewById(R.id.et_profile_name);
        et_mobileno = findViewById(R.id.et_profile_mobilenumber);
        et_ref = findViewById(R.id.et_profile_reference);
        et_address = findViewById(R.id.et_profile_address);
        et_aadharnumber = findViewById(R.id.et_profile_aadharnumber);
        aadhar_front = findViewById(R.id.aadharcard_front_photo);
        aadhar_back = findViewById(R.id.aadharcard_back_photo);
        et_accountNumber = findViewById(R.id.et_profile_bankac_number);
        et_confAccountNumber = findViewById(R.id.et_profile_confbankac_number);
        et_ifsc = findViewById(R.id.et_profile_ifsc_code);
        bank_account = findViewById(R.id.bankaccount_photo);
        et_otp = findViewById(R.id.et_profile_otp);
        et_mobileforOTP = findViewById(R.id.et_profile_mobilenumberforotp);
        btn_profile_getotp = findViewById(R.id.btn_profile_getotp);
        btn_profile_submitdata = findViewById(R.id.btn_profile_submitdata);
        et_password = findViewById(R.id.et_profile_password);
        et_conf_password = findViewById(R.id.et_profile_confpassword);
        profileImg = findViewById(R.id.imageview_profile);
        add_profileimg = findViewById(R.id.add_image_button);
        planSpinner = findViewById(R.id.profile_planspinner);
        backbtn = findViewById(R.id.backBtn);
        tv_view_front_aadhar = findViewById(R.id.tv_seefront_aadhar);
        tv_view_back_aadhar = findViewById(R.id.tv_seeback_aadhar);
        tv_view_bank_ac = findViewById(R.id.tv_seeaccount);

        et_aadharnumber.setEnabled(true);
        btn_profile_submitdata.setEnabled(false);

        dialog = new Dialog(OurProfileActivity.this);
        dialog.setContentView(R.layout.layout_progress_dialog);
        if (dialog.getWindow() != null) {
            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(0));
        }
        dialog.setCancelable(false);

        Id = getIntent().getStringExtra("id");
        getUserDataFromDB();

        // allowing permissions of gallery
        storagePermission = new String[]{Manifest.permission.READ_EXTERNAL_STORAGE};
        backbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        // spinner set-up
        ArrayAdapter adaptor = new ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item, plans);
        planSpinner.setAdapter(adaptor);
        planSpinner.setDropDownVerticalOffset(110);
        planSpinner.getGravity();

        add_profileimg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!checkStoragePermission()) {
                    imageViewCode = 1; //1 for profile img
                    requestStoragePermission();
                } else {
                    imageViewCode = 1; //1 for profile img
                    bringImagePicker();
                }
            }
        });

        aadhar_front.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!checkStoragePermission()) {
                    imageViewCode = 2; //2 for aadhar front
                    requestStoragePermission();
                } else {
                    imageViewCode = 2; //2 for aadhar front
                    bringImagePicker();
                }
            }
        });

        aadhar_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!checkStoragePermission()) {
                    imageViewCode = 3; //3 for aadhar back
                    requestStoragePermission();
                } else {
                    imageViewCode = 3; //3 for aadhar back
                    bringImagePicker();
                }
            }
        });

        bank_account.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!checkStoragePermission()) {
                    imageViewCode = 4; //4 for bank details
                    requestStoragePermission();
                } else {
                    imageViewCode = 4; //4 for bank details
                    bringImagePicker();
                }
            }
        });

        profileImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                LayoutInflater inflater = getLayoutInflater();
                View popup = inflater.inflate(R.layout.popupimage, null);
                final TouchImageView iv = popup.findViewById(R.id.imageview);

                androidx.appcompat.app.AlertDialog.Builder adb = new androidx.appcompat.app.AlertDialog.Builder(OurProfileActivity.this);
                adb.setTitle("Profile Photo");
                adb.setView(popup);
                if (ProfileUrl.isEmpty()) {
                    Picasso.with(OurProfileActivity.this).load(R.drawable.ic_user_dummy).into(iv);
                } else {
                    Picasso.with(OurProfileActivity.this).load(ProfileUrl).into(iv);
                }
                androidx.appcompat.app.AlertDialog dialog = adb.create();
                dialog.show();
            }
        });

        tv_view_front_aadhar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                LayoutInflater inflater = getLayoutInflater();
                View popup = inflater.inflate(R.layout.popupimage, null);
                final TouchImageView iv = popup.findViewById(R.id.imageview);

                androidx.appcompat.app.AlertDialog.Builder adb = new androidx.appcompat.app.AlertDialog.Builder(OurProfileActivity.this);
                adb.setTitle("Front Side Aadhar");
                adb.setView(popup);
                if (dbAadhar_front_url.isEmpty()) {
                    Picasso.with(OurProfileActivity.this).load(R.drawable.dummy_front_aadhar).into(iv);
                } else {
                    Picasso.with(OurProfileActivity.this).load(dbAadhar_front_url).into(iv);
                }
                androidx.appcompat.app.AlertDialog dialog = adb.create();
                dialog.show();
            }
        });

        tv_view_back_aadhar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                LayoutInflater inflater = getLayoutInflater();
                View popup = inflater.inflate(R.layout.popupimage, null);
                final TouchImageView iv = popup.findViewById(R.id.imageview);

                androidx.appcompat.app.AlertDialog.Builder adb = new androidx.appcompat.app.AlertDialog.Builder(OurProfileActivity.this);
                adb.setTitle("Back Side Aadhar");
                adb.setView(popup);
                if (dbAadhar_back_url.isEmpty()) {
                    Picasso.with(OurProfileActivity.this).load(R.drawable.dummy_back_aadhar).into(iv);
                } else {
                    Picasso.with(OurProfileActivity.this).load(dbAadhar_back_url).into(iv);
                }
                androidx.appcompat.app.AlertDialog dialog = adb.create();
                dialog.show();
            }
        });

        tv_view_bank_ac.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                LayoutInflater inflater = getLayoutInflater();
                View popup = inflater.inflate(R.layout.popupimage, null);
                final TouchImageView iv = popup.findViewById(R.id.imageview);

                androidx.appcompat.app.AlertDialog.Builder adb = new androidx.appcompat.app.AlertDialog.Builder(OurProfileActivity.this);
                adb.setTitle("Bank Details");
                adb.setView(popup);
                if (dbAccount_details_url.isEmpty()) {
                    Picasso.with(OurProfileActivity.this).load(R.drawable.dummy_cheque_passbook).into(iv);
                } else {
                    Picasso.with(OurProfileActivity.this).load(dbAccount_details_url).into(iv);
                }
                androidx.appcompat.app.AlertDialog dialog = adb.create();
                dialog.show();
            }
        });
    }

    public void methodGetOtp(View view) {
        internetChecker internetChecker = new internetChecker();
        internetChecker.execute();
        dialog.show();
        aadharNumber = et_aadharnumber.getText().toString();
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                if (isNetConnected) {
                    if (ProfileUrl.isEmpty()) {
                        oldImageName = ProfileUrl;
                    } else {
                        oldImageName = ProfileUrl.substring(ProfileUrl.indexOf("ktc_life_profileimg/") + 22, ProfileUrl.indexOf(".jpg"));
                    }

                    if (dbAccount_details_url.isEmpty()) {
                        oldAccountDetailsName = dbAccount_details_url;
                    } else {
                        oldAccountDetailsName = dbAccount_details_url.substring(dbAccount_details_url.indexOf("ktc_life_document/") + 20, dbAccount_details_url.indexOf(".jpg"));
                    }

                    name = et_name.getText().toString();
                    address = et_address.getText().toString();
                    plan = planSpinner.getSelectedItem().toString();
                    password = et_password.getText().toString();
                    conf_password = et_conf_password.getText().toString();
                    aadharNumber = et_aadharnumber.getText().toString();
                    accountNumber = et_accountNumber.getText().toString();
                    confAccountNumber = et_confAccountNumber.getText().toString();
                    ifscCode = et_ifsc.getText().toString();
                    mobileNumberForOTP = et_mobileforOTP.getText().toString();

                    String removeSpicalChar = name.replaceAll("[^a-zA-Z0-9]", " ");
                    name = removeSpicalChar.replaceAll("[0-9]", "");

                    if (mobileNumberForOTP.isEmpty()) {
                        dialog.dismiss();
                        et_mobileforOTP.setError("Please enter mobile number for otp.");
                        Toast.makeText(OurProfileActivity.this, "Please enter mobile number for otp.", Toast.LENGTH_SHORT).show();
                    } else if (mobileNumberForOTP.length() < 10) {
                        dialog.dismiss();
                        et_mobileforOTP.setError("Please enter valid mobile number for otp.");
                        Toast.makeText(OurProfileActivity.this, "Please enter valid mobile number for otp.", Toast.LENGTH_SHORT).show();
                    } else if (password.length() > 12) {
                        Toast.makeText(OurProfileActivity.this, "Password length should be below 12.", Toast.LENGTH_SHORT).show();
                        et_password.setError("Password length should be below 12.");
                        dialog.dismiss();
                    } else if (conf_password.length() > 12) {
                        Toast.makeText(OurProfileActivity.this, "Password length should be below 12.", Toast.LENGTH_SHORT).show();
                        et_conf_password.setError("Password length should be below 12.");
                        dialog.dismiss();
                    } else if (!(password.equals(conf_password))) {
                        Toast.makeText(OurProfileActivity.this, "Password and confirm password does not match.", Toast.LENGTH_SHORT).show();
                        et_conf_password.setError("Password and confirm password does not match.");
                        dialog.dismiss();
                    } else if (!(accountNumber.equals(confAccountNumber))) {
                        Toast.makeText(OurProfileActivity.this, "Account number and confirm account number does not match.", Toast.LENGTH_SHORT).show();
                        et_confAccountNumber.setError("Account number and confirm account number does not match.");
                        dialog.dismiss();
                    } else if ((ifscCode.length() < 11) && ifscCode.length() > 0) {
                        Toast.makeText(OurProfileActivity.this, "Please enter valid ifsc code.", Toast.LENGTH_SHORT).show();
                        et_ifsc.setError("Please enter valid ifsc code.");
                        dialog.dismiss();
                    } else if (aadharNumber.isEmpty()) {
                        Toast.makeText(OurProfileActivity.this, "Please enter valid aadhar number.", Toast.LENGTH_SHORT).show();
                        et_aadharnumber.setError("Please enter aadhar number.");
                        dialog.dismiss();
                    } else if (aadharNumber.length() < 12) {
                        Toast.makeText(OurProfileActivity.this, "Please enter valid aadhar number.", Toast.LENGTH_SHORT).show();
                        et_aadharnumber.setError("Please enter valid aadhar number.");
                        dialog.dismiss();
                    } else if (name.length() < 1) {
                        Toast.makeText(OurProfileActivity.this, "Please enter name.", Toast.LENGTH_SHORT).show();
                        et_password.setError("Please enter name.");
                        dialog.dismiss();
                    } else if (profileImg.getDrawable() == null) {
                        Toast.makeText(OurProfileActivity.this, "Please select profile photo.", Toast.LENGTH_SHORT).show();
                        dialog.dismiss();
                    } else if (aadhar_front.getDrawable() == null) {
                        Toast.makeText(OurProfileActivity.this, "Please select front side aadhar card photo.", Toast.LENGTH_SHORT).show();
                        dialog.dismiss();
                    } else if (aadhar_back.getDrawable() == null) {
                        Toast.makeText(OurProfileActivity.this, "Please select back side aadhar card photo.", Toast.LENGTH_SHORT).show();
                        dialog.dismiss();
                    } else {
                        //Internet Available
                        if ((!(filePath.equals("empty")) && (!(frontaadhar_filePath.equals("empty"))) && (!(backaadhar_filePath.equals("empty"))))) {

                            StringRequest request = new StringRequest(Request.Method.POST, BaseUrl.aadharNumberChecking, new Response.Listener<String>() {
                                @Override
                                public void onResponse(String response) {
                                    if (response.contains("aadhar number not found")) {
                                        et_aadharnumber.setEnabled(false);
                                        generateOTP();
                                        //Toast.makeText(OurProfileActivity.this, "Aadhar number is not found in database.", Toast.LENGTH_SHORT).show();
                                    }

                                    if (response.contains("aadhar number and id same")) {
                                        et_aadharnumber.setEnabled(false);
                                        generateOTP();
                                        //Toast.makeText(OurProfileActivity.this, "Aadhar number is yours.", Toast.LENGTH_SHORT).show();
                                    }

                                    if (response.contains("aadhar number and id is not same")) {
                                        dialog.dismiss();
                                        Toast.makeText(OurProfileActivity.this, "Aadhar number is already exists in database.", Toast.LENGTH_SHORT).show();
                                    }
                                }
                            }, new Response.ErrorListener() {
                                @Override
                                public void onErrorResponse(VolleyError error) {
                                    dialog.dismiss();
                                    String message = null;
                                    if (error instanceof NetworkError) {
                                        message = "Cannot connect to Internet...Please check your connection!";
                                        Toast.makeText(OurProfileActivity.this, message, Toast.LENGTH_SHORT).show();
                                    } else if (error instanceof ServerError) {
                                        message = "The server could not be found. Please try again after some time!!";
                                        Toast.makeText(OurProfileActivity.this, message, Toast.LENGTH_SHORT).show();
                                    } else if (error instanceof ParseError) {
                                        message = "Parsing error! Please try again after some time!!";
                                        Toast.makeText(OurProfileActivity.this, message, Toast.LENGTH_SHORT).show();
                                    } else if (error instanceof TimeoutError || error instanceof NoConnectionError) {
                                        message = "either time out or there is no connection! Please try again after some time!!";
                                        Toast.makeText(OurProfileActivity.this, message, Toast.LENGTH_SHORT).show();
                                    } else if (error instanceof AuthFailureError) {
                                        message = "Error indicating that there was an Authentication Failure while performing the request";
                                        Toast.makeText(OurProfileActivity.this, message, Toast.LENGTH_SHORT).show();
                                    }
                                }
                            }) {
                                @Override
                                protected Map<String, String> getParams() throws AuthFailureError {
                                    Map<String, String> map = new HashMap<>();
                                    map.put("id", Id);
                                    map.put("aadhar_number", aadharNumber);
                                    return map;
                                }
                            };
                            Volley.newRequestQueue(OurProfileActivity.this).add(request);

                        } else {
                            dialog.dismiss();
                            Toast.makeText(OurProfileActivity.this, "Please attach documents properly.", Toast.LENGTH_SHORT).show();
                        }
                    }
                } else {
                    dialog.dismiss();
                    Toast.makeText(OurProfileActivity.this, "Please check your internet connection.", Toast.LENGTH_SHORT).show();
                }
            }
        }, 3000);
    }

    public void generateOTP() {
        mobileNumberForOTP = et_mobileforOTP.getText().toString();
        if (mobileNumberForOTP.length() < 10) {
            dialog.dismiss();
            Toast.makeText(this, "Please enter valid mobile number for otp.", Toast.LENGTH_SHORT).show();
        } else {
            et_mobileforOTP.setEnabled(false);
            Random rnd = new Random();
            otpNumber = rnd.nextInt(999999);
            current_date = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()).format(new Date());
            current_time = new SimpleDateFormat("hh:mm:ss", Locale.getDefault()).format(new Date());

            StringRequest request = new StringRequest(Request.Method.POST, BaseUrl.smsUrl, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    if (response.contains("otp sent")) {
                        dialog.dismiss();
                        btn_profile_submitdata.setEnabled(true);
                        Toast.makeText(OurProfileActivity.this, "OTP sent to your mobile number.", Toast.LENGTH_SHORT).show();
                    }
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    dialog.dismiss();
                    String message = null;
                    if (error instanceof NetworkError) {
                        message = "Cannot connect to Internet...Please check your connection!";
                        Toast.makeText(OurProfileActivity.this, message, Toast.LENGTH_SHORT).show();
                    } else if (error instanceof ServerError) {
                        message = "The server could not be found. Please try again after some time!!";
                        Toast.makeText(OurProfileActivity.this, message, Toast.LENGTH_SHORT).show();
                    } else if (error instanceof ParseError) {
                        message = "Parsing error! Please try again after some time!!";
                        Toast.makeText(OurProfileActivity.this, message, Toast.LENGTH_SHORT).show();
                    } else if (error instanceof TimeoutError || error instanceof NoConnectionError) {
                        message = "either time out or there is no connection! Please try again after some time!!";
                        Toast.makeText(OurProfileActivity.this, message, Toast.LENGTH_SHORT).show();
                    }
                }
            }) {
                @Override
                protected Map<String, String> getParams() throws AuthFailureError {
                    Map<String, String> map = new HashMap<>();
                    map.put("mobile", mobileNumberForOTP);
                    map.put("otp", otpNumber + "");
                    map.put("date_time", current_date + "%20" + current_time);
                    return map;
                }
            };
            Volley.newRequestQueue(OurProfileActivity.this).add(request);
        }
    }

    public void methodUpdateData(View view) {
        internetChecker internetChecker = new internetChecker();
        internetChecker.execute();
        dialog.show();
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                if (isNetConnected) {
                    name = et_name.getText().toString();
                    address = et_address.getText().toString();
                    plan = planSpinner.getSelectedItem().toString();
                    password = et_password.getText().toString();
                    conf_password = et_conf_password.getText().toString();
                    aadharNumber = et_aadharnumber.getText().toString();
                    accountNumber = et_accountNumber.getText().toString();
                    confAccountNumber = et_confAccountNumber.getText().toString();
                    ifscCode = et_ifsc.getText().toString();

                    String removeSpicalChar = name.replaceAll("[^a-zA-Z0-9]", " ");
                    name = removeSpicalChar.replaceAll("[0-9]", "");

                    if (password.length() > 12) {
                        Toast.makeText(OurProfileActivity.this, "Password length should be below 12.", Toast.LENGTH_SHORT).show();
                        et_password.setError("Password length should be below 12.");
                        dialog.dismiss();
                    } else if (conf_password.length() > 12) {
                        Toast.makeText(OurProfileActivity.this, "Password length should be below 12.", Toast.LENGTH_SHORT).show();
                        et_conf_password.setError("Password length should be below 12.");
                        dialog.dismiss();
                    } else if (!(password.equals(conf_password))) {
                        Toast.makeText(OurProfileActivity.this, "Password and confirm password does not match.", Toast.LENGTH_SHORT).show();
                        et_conf_password.setError("Password and confirm password does not match.");
                        dialog.dismiss();
                    } else if (!(accountNumber.equals(confAccountNumber))) {
                        Toast.makeText(OurProfileActivity.this, "Account number and confirm account number does not match.", Toast.LENGTH_SHORT).show();
                        et_confAccountNumber.setError("Account number and confirm account number does not match.");
                        dialog.dismiss();
                    } else if ((ifscCode.length() < 11) && ifscCode.length() > 0) {
                        Toast.makeText(OurProfileActivity.this, "Please enter valid ifsc code.", Toast.LENGTH_SHORT).show();
                        et_ifsc.setError("Please enter valid ifsc code.");
                        dialog.dismiss();
                    } else if (aadharNumber.isEmpty()) {
                        Toast.makeText(OurProfileActivity.this, "Please enter valid aadhar number.", Toast.LENGTH_SHORT).show();
                        et_aadharnumber.setError("Please enter aadhar number.");
                        dialog.dismiss();
                    } else if (aadharNumber.length() < 12) {
                        Toast.makeText(OurProfileActivity.this, "Please enter valid aadhar number.", Toast.LENGTH_SHORT).show();
                        et_aadharnumber.setError("Please enter valid aadhar number.");
                        dialog.dismiss();
                    } else if (name.length() < 1) {
                        Toast.makeText(OurProfileActivity.this, "Please enter name.", Toast.LENGTH_SHORT).show();
                        et_password.setError("Please enter name.");
                        dialog.dismiss();
                    } else if (profileImg.getDrawable() == null) {
                        Toast.makeText(OurProfileActivity.this, "Please select profile photo.", Toast.LENGTH_SHORT).show();
                        dialog.dismiss();
                    } else if (aadhar_front.getDrawable() == null) {
                        Toast.makeText(OurProfileActivity.this, "Please select front side aadhar card photo.", Toast.LENGTH_SHORT).show();
                        dialog.dismiss();
                    } else if (aadhar_back.getDrawable() == null) {
                        Toast.makeText(OurProfileActivity.this, "Please select back side aadhar card photo.", Toast.LENGTH_SHORT).show();
                        dialog.dismiss();
                    } else if (!(et_otp.getText().toString().equals(String.valueOf(otpNumber)))) {
                        Toast.makeText(OurProfileActivity.this, "Please enter valid otp.", Toast.LENGTH_SHORT).show();
                        dialog.dismiss();
                    } else {
                        // Internet Available
                        if ((!(filePath.equals("")) && (!(frontaadhar_filePath.equals(""))) && (!(backaadhar_filePath.equals(""))))) {
                            profileImgUploder();
                            aadharFrontImgUploder();
                            aadharBackImgUploder();
                            accountDetailsImgUploder();
                            updateData();
                        } else {
                            dialog.dismiss();
                            Toast.makeText(OurProfileActivity.this, "Please attach documents properly.", Toast.LENGTH_SHORT).show();
                        }
                    }
                } else {
                    Toast.makeText(OurProfileActivity.this, "Internet is not available.", Toast.LENGTH_SHORT).show();
                    dialog.dismiss();
                }
            }
        }, 5000);
    }

    public void updateData() {
        StringRequest request = new StringRequest(Request.Method.POST, BaseUrl.updateUserProfileUrl, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                if (response.contains("connection not got")) {
                    btn_profile_submitdata.setEnabled(true);
                    dialog.dismiss();
                    Toast.makeText(OurProfileActivity.this, "Server error.", Toast.LENGTH_SHORT).show();
                }

                if (response.contains("data updated")) {
                    dialog.dismiss();
                    clearMyCache(OurProfileActivity.this);
                    Toast.makeText(OurProfileActivity.this, "Updated & Cache Cleared..!", Toast.LENGTH_SHORT).show();
                    et_aadharnumber.setEnabled(true);
                    et_mobileforOTP.setEnabled(true);
                    btn_profile_submitdata.setEnabled(false);
                    et_otp.setText("");
                    et_mobileforOTP.setText("");
                    et_password.setText("");
                    et_conf_password.setText("");
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            getUserDataFromDB();
                            showPopoUp();
                        }
                    }, 2000);
                }

                if (response.contains("data is not updated")) {
                    btn_profile_submitdata.setEnabled(true);
                    dialog.dismiss();
                    Toast.makeText(OurProfileActivity.this, "Data is not Updated...", Toast.LENGTH_SHORT).show();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                btn_profile_submitdata.setEnabled(true);
                dialog.dismiss();
                String message = null;
                if (error instanceof NetworkError) {
                    message = "Cannot connect to Internet...Please check your connection!";
                    Toast.makeText(OurProfileActivity.this, message, Toast.LENGTH_SHORT).show();
                } else if (error instanceof ServerError) {
                    message = "The server could not be found. Please try again after some time!!";
                    Toast.makeText(OurProfileActivity.this, message, Toast.LENGTH_SHORT).show();
                } else if (error instanceof ParseError) {
                    message = "Error! Please try again after some time!!";
                    Toast.makeText(OurProfileActivity.this, message, Toast.LENGTH_SHORT).show();
                } else if (error instanceof TimeoutError || error instanceof NoConnectionError) {
                    message = "Either time out or there is no connection! Please try again after some time!!";
                    Toast.makeText(OurProfileActivity.this, message, Toast.LENGTH_SHORT).show();
                }
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> map = new HashMap<>();
                map.put("id", Id);
                map.put("newimg_url", BaseUrl.dbProfileImageUrl + Id + ".jpg");
                map.put("oldimg_name", oldImageName);
                map.put("address", address);
                map.put("password", password);
                map.put("is_kyc", "Under Review");
                map.put("aadhar_number", aadharNumber);
                map.put("aadhar_frontphoto", BaseUrl.dbDocumentImageUrl + Id + "_aadharfront.jpg");
                map.put("aadhar_backphoto", BaseUrl.dbDocumentImageUrl + Id + "_aadharback.jpg");
                map.put("account_number", accountNumber);
                map.put("account_ifsc", ifscCode);
                map.put("account_details", BaseUrl.dbDocumentImageUrl + Id + "_account.jpg");
                map.put("account_olddetails", oldAccountDetailsName);
                map.put("user_type", "user");
                return map;
            }
        };
        Volley.newRequestQueue(OurProfileActivity.this).add(request);
    }

    public void showPopoUp() {
        AlertDialog.Builder adb = new AlertDialog.Builder(this);
        adb.setTitle(R.string.dialog_title);
        adb.setMessage("Data updated...");
        adb.setIcon(R.drawable.app_logo);
        adb.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
            }
        });
        adb.show();
    }

    public void bringImagePicker() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE_REQUEST);
    }

    public byte[] getFileDataFromDrawable(Bitmap bitmap) {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 70, byteArrayOutputStream); //80
        return byteArrayOutputStream.toByteArray();
    }

    public String getPath(Uri uri) {
        Cursor cursor = getContentResolver().query(uri, null, null, null, null);
        cursor.moveToFirst();
        String document_id = cursor.getString(0);
        document_id = document_id.substring(document_id.lastIndexOf(":") + 1);
        cursor.close();

        cursor = getContentResolver().query(
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                null, MediaStore.Images.Media._ID + " = ? ", new String[]{document_id}, null);
        cursor.moveToFirst();
        String path = cursor.getString(cursor.getColumnIndex(MediaStore.Images.Media.DATA));
        cursor.close();
        return path;
    }

    public void getUserDataFromDB() {
        dialog.show();

        StringRequest stringRequest = new StringRequest(Request.Method.POST, BaseUrl.retriveUserDataUrl, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                if (response.contains(Id)) {
                    String data = response;
                    et_name.setText(data.substring(data.indexOf("@") + 1, data.indexOf("#")));
                    et_mobileno.setText(data.substring(data.indexOf("#") + 1, data.indexOf("$")));
                    ProfileUrl = data.substring(data.indexOf("$") + 1, data.indexOf("%"));
                    et_ref.setText(data.substring(data.indexOf("%") + 1, data.indexOf("&")));
                    Plan = data.substring(data.indexOf("&") + 1, data.indexOf("*"));
                    et_address.setText(data.substring(data.indexOf("*") + 1, data.indexOf("?")));
                    Password = data.substring(data.indexOf("?") + 1, data.indexOf("#1A#"));
                    dbIs_kyc = data.substring(data.indexOf("#4A#") + 4, data.indexOf("#5A#"));
                    dbAadhar_number = data.substring(data.indexOf("#5A#") + 4, data.indexOf("#6A#"));
                    dbAadhar_front_url = data.substring(data.indexOf("#6A#") + 4, data.indexOf("#7A#"));
                    dbAadhar_back_url = data.substring(data.indexOf("#7A#") + 4, data.indexOf("#8A#"));
                    dbAccount_details_url = data.substring(data.indexOf("#8A#") + 4, data.indexOf("#9A#"));
                    filePath = data.substring(data.indexOf("$") + 1, data.indexOf("%"));
                    frontaadhar_filePath = data.substring(data.indexOf("#6A#") + 4, data.indexOf("#7A#"));
                    backaadhar_filePath = data.substring(data.indexOf("#7A#") + 4, data.indexOf("#8A#"));
                    account_filePath = data.substring(data.indexOf("#8A#") + 4, data.indexOf("#9A#"));
                    et_accountNumber.setText(data.substring(data.indexOf("#9A#") + 4, data.indexOf("#10A#")));
                    et_confAccountNumber.setText(data.substring(data.indexOf("#9A#") + 4, data.indexOf("#10A#")));
                    et_ifsc.setText(data.substring(data.indexOf("#10A#") + 5, data.indexOf("#11A#")));

                    if (dbIs_kyc.equals("Yes")) {
                        aadhar_front.setClickable(false);
                        aadhar_back.setClickable(false);
                    } else {
                        aadhar_front.setClickable(true);
                        aadhar_back.setClickable(true);
                    }

                    if (dbIs_kyc.isEmpty() || dbIs_kyc.equals("") || dbIs_kyc.equals("No")) {
                        tv_kyc_status.setText("KYC :- No");
                    } else if (dbIs_kyc.equals("Yes")) {
                        tv_kyc_status.setText("KYC :- Yes");
                    }

                    if (dbIs_kyc.equals("Under Review")) {
                        tv_kyc_status.setText("KYC :- Under Review");
                    }

                    switch (Plan) {
                        case "-- Select Plans --":
                            spinnerPosition = 0;
                            break;

                        case "1000":
                            spinnerPosition = 1;
                            break;

                        case "2000":
                            spinnerPosition = 2;
                            break;

                        case "3000":
                            spinnerPosition = 3;
                            break;

                        case "4000":
                            spinnerPosition = 4;
                            break;

                        case "5000":
                            spinnerPosition = 5;
                            break;

                        case "10000":
                            spinnerPosition = 6;
                            break;
                        case "15000":
                            spinnerPosition = 7;
                            break;
                        case "20000":
                            spinnerPosition = 8;
                            break;
                    }
                    planSpinner.setSelection(spinnerPosition);
                    planSpinner.setEnabled(false);

                    et_aadharnumber.setText(dbAadhar_number);

                    if (ProfileUrl.isEmpty()) {
                        Picasso.with(OurProfileActivity.this).load(R.drawable.ic_user_dummy).into(aadhar_front);
                    } else {
                        Picasso.with(OurProfileActivity.this).load(ProfileUrl).into(profileImg);
                    }

                    if (dbAadhar_front_url.isEmpty()) {
                        tv_view_front_aadhar.setVisibility(View.GONE);
                        Picasso.with(OurProfileActivity.this).load(R.drawable.dummy_front_aadhar).into(aadhar_front);
                    } else {
                        tv_view_front_aadhar.setVisibility(View.VISIBLE);
                        Picasso.with(OurProfileActivity.this).load(dbAadhar_front_url).into(aadhar_front);
                    }

                    if (dbAadhar_back_url.isEmpty()) {
                        tv_view_back_aadhar.setVisibility(View.GONE);
                        Picasso.with(OurProfileActivity.this).load(R.drawable.dummy_back_aadhar).into(aadhar_back);
                    } else {
                        tv_view_back_aadhar.setVisibility(View.VISIBLE);
                        Picasso.with(OurProfileActivity.this).load(dbAadhar_back_url).into(aadhar_back);
                    }

                    if (dbAccount_details_url.isEmpty()) {
                        tv_view_bank_ac.setVisibility(View.GONE);
                        Picasso.with(OurProfileActivity.this).load(R.drawable.dummy_cheque_passbook).into(bank_account);
                    } else {
                        tv_view_bank_ac.setVisibility(View.VISIBLE);
                        Picasso.with(OurProfileActivity.this).load(dbAccount_details_url).into(bank_account);
                    }
                    dialog.dismiss();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                dialog.dismiss();
                String message = null;
                if (error instanceof NetworkError) {
                    message = "Cannot connect to Internet...Please check your connection!";
                    Toast.makeText(OurProfileActivity.this, message, Toast.LENGTH_SHORT).show();
                } else if (error instanceof ServerError) {
                    message = "The server could not be found. Please try again after some time!!";
                    Toast.makeText(OurProfileActivity.this, message, Toast.LENGTH_SHORT).show();
                } else if (error instanceof ParseError) {
                    message = "Error! Please try again after some time!!";
                    Toast.makeText(OurProfileActivity.this, message, Toast.LENGTH_SHORT).show();
                } else if (error instanceof TimeoutError || error instanceof NoConnectionError) {
                    message = "Either time out or there is no connection! Please try again after some time!!";
                    Toast.makeText(OurProfileActivity.this, message, Toast.LENGTH_SHORT).show();
                }
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> map = new HashMap<>();
                map.put("id", Id);
                return map;
            }
        };
        Volley.newRequestQueue(OurProfileActivity.this).add(stringRequest);
    }

    private void clearMyCache(Context context) {
        try {
            File dir = context.getCacheDir();
            deleteDir(dir);
        } catch (Exception e) {
        }
    }

    public static boolean deleteDir(File dir) {
        if (dir != null && dir.isDirectory()) {
            String[] children = dir.list();
            for (int i = 0; i < children.length; i++) {
                boolean success = deleteDir(new File(dir, children[i]));
                if (!success) {
                    return false;
                }
            }
            boolean b = dir.delete();
            Log.d("ktcLog", "deleted Dir:" + b + " =>" + dir.getName());
            return b;
        } else if (dir != null && dir.isFile()) {
            boolean b = dir.delete();
            Log.d("ktcLog", "deleted File:" + b + " =>" + dir.getName());
            return b;
        } else {
            return false;
        }
    }

    // internet checking code start
    public boolean isConnected() {
        ConnectivityManager connectivityManager = (ConnectivityManager) this.getSystemService(Service.CONNECTIVITY_SERVICE);
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

    public void methodEditMobileNumberForOTP(View view) {
        et_mobileforOTP.setEnabled(true);
        btn_profile_submitdata.setEnabled(false);
    }

    class internetChecker extends AsyncTask<String, Void, Integer> {

        @Override
        protected Integer doInBackground(String... params) {

            Integer result = 0;
            try {
                Socket socket = new Socket();
                SocketAddress socketAddress = new InetSocketAddress("8.8.8.8", 53);
                socket.connect(socketAddress, 1500);
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
                    isNetConnected = true;
                }
                if (result == 0) {
                    isNetConnected = false;
                    Toast.makeText(OurProfileActivity.this, "Internet is not available.", Toast.LENGTH_SHORT).show();
                }
            } else {
                isNetConnected = false;
                noInternetDialog();
            }
            super.onPostExecute(result);
        }
    }
    // internet checking code end

    public void noInternetDialog() {
        AlertDialog.Builder adb = new AlertDialog.Builder(this);
        adb.setTitle("Please connect to the internet to proceed furture.");
        adb.setIcon(R.drawable.app_logo);
        adb.setCancelable(false);
        adb.setPositiveButton("Connect", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                startActivity(new Intent(Settings.ACTION_WIFI_SETTINGS));
            }
        });
        adb.setNegativeButton("Cancel", null);
        adb.show();
    }

    public void profileImgUploder() {
        VolleyMultipartRequest volleyMultipartRequest = new VolleyMultipartRequest(Request.Method.POST, BaseUrl.imageUploadUrl,
                new Response.Listener<NetworkResponse>() {
                    @Override
                    public void onResponse(NetworkResponse response) {
                        try {
                            JSONObject obj = new JSONObject(new String(response.data));
                            String res = obj.toString();
                            dialog.dismiss();
                            if (res.contains("File uploaded successfully!")) {

                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        dialog.dismiss();
                        String message = null;
                        if (error instanceof NetworkError) {
                            message = "Cannot connect to Internet...Please check your connection!";
                            Toast.makeText(OurProfileActivity.this, message, Toast.LENGTH_SHORT).show();
                        } else if (error instanceof ServerError) {
                            message = "The server could not be found. Please try again after some time!!";
                            Toast.makeText(OurProfileActivity.this, message, Toast.LENGTH_SHORT).show();
                        } else if (error instanceof ParseError) {
                            message = "Error! Please try again after some time!!";
                            Toast.makeText(OurProfileActivity.this, message, Toast.LENGTH_SHORT).show();
                        } else if (error instanceof TimeoutError || error instanceof NoConnectionError) {
                            message = "Either time out or there is no connection! Please try again after some time!!";
                            Toast.makeText(OurProfileActivity.this, message, Toast.LENGTH_SHORT).show();
                        } else if (error instanceof AuthFailureError) {
                            message = "Error indicating that there was an Authentication Failure while performing the request";
                            Toast.makeText(OurProfileActivity.this, message, Toast.LENGTH_SHORT).show();
                        }
                    }
                }) {
            @Override
            protected Map<String, DataPart> getByteData() {
                Map<String, DataPart> params = new HashMap<>();
                params.put("fileToUpload", new DataPart(Id + ".jpg", getFileDataFromDrawable(profile_bitmap)));
                return params;
            }
        };
        Volley.newRequestQueue(OurProfileActivity.this).add(volleyMultipartRequest);
    }

    public void aadharFrontImgUploder() {
        VolleyMultipartRequest volleyMultipartRequest = new VolleyMultipartRequest(Request.Method.POST, BaseUrl.documentUploadUrl,
                new Response.Listener<NetworkResponse>() {
                    @Override
                    public void onResponse(NetworkResponse response) {
                        try {
                            JSONObject obj = new JSONObject(new String(response.data));
                            String res = obj.toString();
                            dialog.dismiss();
                            if (res.contains("File uploaded successfully!")) {

                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        dialog.dismiss();
                        String message = null;
                        if (error instanceof NetworkError) {
                            message = "Cannot connect to Internet...Please check your connection!";
                            Toast.makeText(OurProfileActivity.this, message, Toast.LENGTH_SHORT).show();
                        } else if (error instanceof ServerError) {
                            message = "The server could not be found. Please try again after some time!!";
                            Toast.makeText(OurProfileActivity.this, message, Toast.LENGTH_SHORT).show();
                        } else if (error instanceof ParseError) {
                            message = "Error! Please try again after some time!!";
                            Toast.makeText(OurProfileActivity.this, message, Toast.LENGTH_SHORT).show();
                        } else if (error instanceof TimeoutError || error instanceof NoConnectionError) {
                            message = "Either time out or there is no connection! Please try again after some time!!";
                            Toast.makeText(OurProfileActivity.this, message, Toast.LENGTH_SHORT).show();
                        } else if (error instanceof AuthFailureError) {
                            message = "Error indicating that there was an Authentication Failure while performing the request";
                            Toast.makeText(OurProfileActivity.this, message, Toast.LENGTH_SHORT).show();
                        }
                    }
                }) {
            @Override
            protected Map<String, DataPart> getByteData() {
                Map<String, DataPart> params = new HashMap<>();
                params.put("fileToUpload", new DataPart(Id + "_aadharfront.jpg", getFileDataFromDrawable(frontaadhar_bitmap)));
                return params;
            }
        };
        Volley.newRequestQueue(OurProfileActivity.this).add(volleyMultipartRequest);
    }

    public void aadharBackImgUploder() {
        VolleyMultipartRequest volleyMultipartRequest = new VolleyMultipartRequest(Request.Method.POST, BaseUrl.documentUploadUrl,
                new Response.Listener<NetworkResponse>() {
                    @Override
                    public void onResponse(NetworkResponse response) {
                        try {
                            JSONObject obj = new JSONObject(new String(response.data));
                            String res = obj.toString();
                            dialog.dismiss();
                            if (res.contains("File uploaded successfully!")) {

                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        dialog.dismiss();
                        String message = null;
                        if (error instanceof NetworkError) {
                            message = "Cannot connect to Internet...Please check your connection!";
                            Toast.makeText(OurProfileActivity.this, message, Toast.LENGTH_SHORT).show();
                        } else if (error instanceof ServerError) {
                            message = "The server could not be found. Please try again after some time!!";
                            Toast.makeText(OurProfileActivity.this, message, Toast.LENGTH_SHORT).show();
                        } else if (error instanceof ParseError) {
                            message = "Error! Please try again after some time!!";
                            Toast.makeText(OurProfileActivity.this, message, Toast.LENGTH_SHORT).show();
                        } else if (error instanceof TimeoutError || error instanceof NoConnectionError) {
                            message = "Either time out or there is no connection! Please try again after some time!!";
                            Toast.makeText(OurProfileActivity.this, message, Toast.LENGTH_SHORT).show();
                        } else if (error instanceof AuthFailureError) {
                            message = "Error indicating that there was an Authentication Failure while performing the request";
                            Toast.makeText(OurProfileActivity.this, message, Toast.LENGTH_SHORT).show();
                        }
                    }
                }) {
            @Override
            protected Map<String, DataPart> getByteData() {
                Map<String, DataPart> params = new HashMap<>();
                params.put("fileToUpload", new DataPart(Id + "_aadharback.jpg", getFileDataFromDrawable(backaadhar_bitmap)));
                return params;
            }
        };
        Volley.newRequestQueue(OurProfileActivity.this).add(volleyMultipartRequest);
    }

    public void accountDetailsImgUploder() {
        VolleyMultipartRequest volleyMultipartRequest = new VolleyMultipartRequest(Request.Method.POST, BaseUrl.documentUploadUrl,
                new Response.Listener<NetworkResponse>() {
                    @Override
                    public void onResponse(NetworkResponse response) {
                        try {
                            JSONObject obj = new JSONObject(new String(response.data));
                            String res = obj.toString();
                            dialog.dismiss();
                            if (res.contains("File uploaded successfully!")) {

                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        dialog.dismiss();
                        String message = null;
                        if (error instanceof NetworkError) {
                            message = "Cannot connect to Internet...Please check your connection!";
                            Toast.makeText(OurProfileActivity.this, message, Toast.LENGTH_SHORT).show();
                        } else if (error instanceof ServerError) {
                            message = "The server could not be found. Please try again after some time!!";
                            Toast.makeText(OurProfileActivity.this, message, Toast.LENGTH_SHORT).show();
                        } else if (error instanceof ParseError) {
                            message = "Error! Please try again after some time!!";
                            Toast.makeText(OurProfileActivity.this, message, Toast.LENGTH_SHORT).show();
                        } else if (error instanceof TimeoutError || error instanceof NoConnectionError) {
                            message = "Either time out or there is no connection! Please try again after some time!!";
                            Toast.makeText(OurProfileActivity.this, message, Toast.LENGTH_SHORT).show();
                        } else if (error instanceof AuthFailureError) {
                            message = "Error indicating that there was an Authentication Failure while performing the request";
                            Toast.makeText(OurProfileActivity.this, message, Toast.LENGTH_SHORT).show();
                        }
                    }
                }) {
            @Override
            protected Map<String, DataPart> getByteData() {
                Map<String, DataPart> params = new HashMap<>();
                params.put("fileToUpload", new DataPart(Id + "_account.jpg", getFileDataFromDrawable(account_bitmap)));
                return params;
            }
        };
        Volley.newRequestQueue(OurProfileActivity.this).add(volleyMultipartRequest);
    }

    // checking storage permissions
    private Boolean checkStoragePermission() {
        boolean result1 = ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) == (PackageManager.PERMISSION_GRANTED);
        return result1;
    }

    // Requesting  gallery permission
    private void requestStoragePermission() {
        ActivityCompat.requestPermissions(this, storagePermission, STORAGE_REQUEST);
    }

    // Requesting gallery
    // permission if not given
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case STORAGE_REQUEST: {
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    bringImagePicker();
                } else {
                    //requestStoragePermission();
                    Toast.makeText(this, "Please Enable Storage Permissions", Toast.LENGTH_LONG).show();
                }
            }
            break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {
            if (imageViewCode == 1) {
                Uri picUri = data.getData();
                filePath = getPath(picUri);
                if (filePath != null) {
                    try {
                        profile_bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), picUri);
                        profileImg.setImageBitmap(profile_bitmap);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                } else {
                    Toast.makeText(this, "no img selected.", Toast.LENGTH_SHORT).show();
                }
            } else if (imageViewCode == 2) {
                Uri picUri = data.getData();
                frontaadhar_filePath = getPath(picUri);
                if (frontaadhar_filePath != null) {
                    try {
                        frontaadhar_bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), picUri);
                        aadhar_front.setImageBitmap(frontaadhar_bitmap);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                } else {
                    Toast.makeText(this, "no img selected.", Toast.LENGTH_SHORT).show();
                }
            } else if (imageViewCode == 3) {
                Uri picUri = data.getData();
                backaadhar_filePath = getPath(picUri);
                if (backaadhar_filePath != null) {
                    try {
                        backaadhar_bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), picUri);
                        aadhar_back.setImageBitmap(backaadhar_bitmap);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                } else {
                    Toast.makeText(this, "no img selected.", Toast.LENGTH_SHORT).show();
                }
            } else if (imageViewCode == 4) {
                Uri picUri = data.getData();
                account_filePath = getPath(picUri);
                if (account_filePath != null) {
                    try {
                        account_bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), picUri);
                        bank_account.setImageBitmap(account_bitmap);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                } else {
                    Toast.makeText(this, "no img selected.", Toast.LENGTH_SHORT).show();
                }
            }
        }
    }
}

/*
Dear Customer, Your plan payment id <XXXXXXXXXX> is credited by Rs.XXX on <DD/MM/YYYY>. Thank You KTCLIFE SUCCESS PRIVATE LIMITED
 */