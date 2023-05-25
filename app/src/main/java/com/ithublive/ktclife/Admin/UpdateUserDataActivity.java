package com.ithublive.ktclife.Admin;

import android.Manifest;
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
import android.os.Bundle;
import android.os.Handler;
import android.provider.MediaStore;
import android.provider.Settings;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
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
import com.google.android.material.textfield.TextInputLayout;
import com.ithublive.ktclife.OurProfileActivity;
import com.ithublive.ktclife.PayInstallmentSelf;
import com.ithublive.ktclife.R;
import com.ithublive.ktclife.Utils.BaseUrl;
import com.ithublive.ktclife.VolleyMultipartRequest;
import com.ortiz.touchview.TouchImageView;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketAddress;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.Random;

import de.hdodenhof.circleimageview.CircleImageView;

public class UpdateUserDataActivity extends AppCompatActivity {

    EditText et_userid, et_name, et_mobileno, et_refid, et_address, et_plan, et_paid, et_doj, et_kyc, et_caplimit, et_noofleg, et_password,
            et_aadharno, et_bank_ac_no, et_bank_ifsc, et_mobilenoforotp, et_otp;
    CircleImageView profilePhoto, add_profile_photo;
    ImageView iv_front_aadhar, iv_back_aadhar, iv_bank_ac;
    TextView tv_view_front_aadhar, tv_view_back_aadhar, tv_view_bank_ac;
    String intent_profileUrl, intent_frontAadharUrl, intent_backAadharUrl, intent_bankAcUrl;
    Button btn_submitdata, btn_otp, btn_editmobileno;
    TextInputLayout edittext_update_mobilenumber;

    int imageViewCode, otpNumber = 0;
    private static final int PICK_IMAGE_REQUEST = 1;
    private static final int STORAGE_REQUEST = 100, CALL_REQ = 101;
    String storagePermission[], callPermission[], aadharNumber, accountNumber, ifscCode, mobileNumberForOTP, current_date, current_time, oldImageName, oldAccountDetailsUrl,
            oldFrontAadhar, oldBackAadhar;
    private Bitmap profile_bitmap, frontaadhar_bitmap, backaadhar_bitmap, account_bitmap;
    private String filePath = "empty", frontaadhar_filePath = "empty", backaadhar_filePath = "empty", account_filePath = "empty";
    boolean isNetConnected;
    Dialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_user_data);

        storagePermission = new String[]{Manifest.permission.READ_EXTERNAL_STORAGE};
        callPermission = new String[]{Manifest.permission.CALL_PHONE};
        findingViews();
        dataSet();

        if (ContextCompat.checkSelfPermission(UpdateUserDataActivity.this, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(UpdateUserDataActivity.this, new String[]{Manifest.permission.CALL_PHONE}, 101);
        }

        dialog = new Dialog(UpdateUserDataActivity.this);
        dialog.setContentView(R.layout.layout_progress_dialog);
        if (dialog.getWindow() != null) {
            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(0));
        }
        dialog.setCancelable(false);

        profilePhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!(intent_profileUrl.isEmpty())) {
                    LayoutInflater inflater = getLayoutInflater();
                    View popup = inflater.inflate(R.layout.popupimage, null);
                    final TouchImageView iv = popup.findViewById(R.id.imageview);

                    AlertDialog.Builder adb = new AlertDialog.Builder(UpdateUserDataActivity.this);
                    adb.setTitle("Profile Photo");
                    adb.setView(popup);
                    if (intent_profileUrl.isEmpty()) {
                        Picasso.with(UpdateUserDataActivity.this).load(R.drawable.ic_user_dummy).into(iv);
                    } else {
                        Picasso.with(UpdateUserDataActivity.this).load(intent_profileUrl).into(iv);
                    }
                    AlertDialog dialog = adb.create();
                    dialog.show();
                }
            }
        });

        tv_view_front_aadhar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!(intent_frontAadharUrl.isEmpty())) {
                    LayoutInflater inflater = getLayoutInflater();
                    View popup = inflater.inflate(R.layout.popupimage, null);
                    final TouchImageView iv = popup.findViewById(R.id.imageview);

                    AlertDialog.Builder adb = new AlertDialog.Builder(UpdateUserDataActivity.this);
                    adb.setTitle("Front Side Aadhar");
                    adb.setView(popup);
                    if (intent_frontAadharUrl.isEmpty()) {
                        Picasso.with(UpdateUserDataActivity.this).load(R.drawable.dummy_front_aadhar).into(iv);
                    } else {
                        Picasso.with(UpdateUserDataActivity.this).load(intent_frontAadharUrl).into(iv);
                    }
                    AlertDialog dialog = adb.create();
                    dialog.show();
                }
            }
        });

        tv_view_back_aadhar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!(intent_backAadharUrl.isEmpty())) {
                    LayoutInflater inflater = getLayoutInflater();
                    View popup = inflater.inflate(R.layout.popupimage, null);
                    final TouchImageView iv = popup.findViewById(R.id.imageview);

                    AlertDialog.Builder adb = new AlertDialog.Builder(UpdateUserDataActivity.this);
                    adb.setTitle("Back Side Aadhar");
                    adb.setView(popup);
                    if (intent_backAadharUrl.isEmpty()) {
                        Picasso.with(UpdateUserDataActivity.this).load(R.drawable.dummy_back_aadhar).into(iv);
                    } else {
                        Picasso.with(UpdateUserDataActivity.this).load(intent_backAadharUrl).into(iv);
                    }
                    AlertDialog dialog = adb.create();
                    dialog.show();
                }
            }
        });

        tv_view_bank_ac.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!(intent_bankAcUrl.isEmpty())) {
                    LayoutInflater inflater = getLayoutInflater();
                    View popup = inflater.inflate(R.layout.popupimage, null);
                    final TouchImageView iv = popup.findViewById(R.id.imageview);

                    AlertDialog.Builder adb = new AlertDialog.Builder(UpdateUserDataActivity.this);
                    adb.setTitle("Account Details");
                    adb.setView(popup);
                    if (intent_bankAcUrl.isEmpty()) {
                        Picasso.with(UpdateUserDataActivity.this).load(R.drawable.dummy_cheque_passbook).into(iv);
                    } else {
                        Picasso.with(UpdateUserDataActivity.this).load(intent_bankAcUrl).into(iv);
                    }
                    AlertDialog dialog = adb.create();
                    dialog.show();
                }
            }
        });

        add_profile_photo.setOnClickListener(new View.OnClickListener() {
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

        iv_front_aadhar.setOnClickListener(new View.OnClickListener() {
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

        iv_back_aadhar.setOnClickListener(new View.OnClickListener() {
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

        iv_bank_ac.setOnClickListener(new View.OnClickListener() {
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

        et_mobileno.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!checkCallPermission()) {
                    requestCallPermission();
                } else {
                    String number = et_mobileno.getText().toString();
                    Intent callIntent = new Intent(Intent.ACTION_CALL);
                    callIntent.setData(Uri.parse("tel:" + number));
                    startActivity(callIntent);
                    Toast.makeText(UpdateUserDataActivity.this, "Calling to " + et_name.getText().toString(), Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    public void methodGetOtp(View view) {
        internetChecker internetChecker = new internetChecker();
        internetChecker.execute();
        dialog.show();
        aadharNumber = et_aadharno.getText().toString();
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                if (isNetConnected) {
                    if (intent_profileUrl.isEmpty()) {
                        oldImageName = intent_profileUrl;
                    } else {
                        oldImageName = intent_profileUrl.substring(intent_profileUrl.indexOf("ktc_life_profileimg/") + 22, intent_profileUrl.indexOf(".jpg"));
                    }

                    if (intent_bankAcUrl.isEmpty()) {
                        oldAccountDetailsUrl = intent_bankAcUrl;
                    } else {
                        oldAccountDetailsUrl = intent_bankAcUrl.substring(intent_bankAcUrl.indexOf("ktc_life_document/") + 20, intent_bankAcUrl.indexOf(".jpg"));
                    }

                    if (intent_frontAadharUrl.isEmpty()) {
                        oldFrontAadhar = intent_frontAadharUrl;
                    } else {
                        oldFrontAadhar = intent_frontAadharUrl.substring(intent_frontAadharUrl.indexOf("ktc_life_document/") + 20, intent_frontAadharUrl.indexOf(".jpg"));
                    }

                    if (intent_backAadharUrl.isEmpty()) {
                        oldBackAadhar = intent_backAadharUrl;
                    } else {
                        oldBackAadhar = intent_backAadharUrl.substring(intent_backAadharUrl.indexOf("ktc_life_document/") + 20, intent_backAadharUrl.indexOf(".jpg"));
                    }

                    aadharNumber = et_aadharno.getText().toString();
                    accountNumber = et_bank_ac_no.getText().toString();
                    ifscCode = et_bank_ifsc.getText().toString();
                    mobileNumberForOTP = et_mobilenoforotp.getText().toString();

                    if (mobileNumberForOTP.isEmpty()) {
                        dialog.dismiss();
                        Toast.makeText(UpdateUserDataActivity.this, "Please enter mobile number for otp.", Toast.LENGTH_SHORT).show();
                    } else if (mobileNumberForOTP.length() < 10) {
                        dialog.dismiss();
                        Toast.makeText(UpdateUserDataActivity.this, "Please enter valid mobile number for otp.", Toast.LENGTH_SHORT).show();
                    } else if ((ifscCode.length() < 11) && ifscCode.length() > 0) {
                        Toast.makeText(UpdateUserDataActivity.this, "Please enter valid ifsc code.", Toast.LENGTH_SHORT).show();
                        et_bank_ifsc.setError("Please enter valid ifsc code.");
                        dialog.dismiss();
                    } else if (aadharNumber.isEmpty()) {
                        Toast.makeText(UpdateUserDataActivity.this, "Please enter aadhar number.", Toast.LENGTH_SHORT).show();
                        et_aadharno.setError("Please enter aadhar number.");
                        dialog.dismiss();
                    } else if (aadharNumber.length() < 12) {
                        Toast.makeText(UpdateUserDataActivity.this, "Please enter valid aadhar number.", Toast.LENGTH_SHORT).show();
                        et_aadharno.setError("Please enter valid aadhar number.");
                        dialog.dismiss();
                    } else if (profilePhoto.getDrawable() == null) {
                        Toast.makeText(UpdateUserDataActivity.this, "Please select profile photo.", Toast.LENGTH_SHORT).show();
                        dialog.dismiss();
                    } else if (iv_front_aadhar.getDrawable() == null) {
                        Toast.makeText(UpdateUserDataActivity.this, "Please select front side aadhar card photo.", Toast.LENGTH_SHORT).show();
                        dialog.dismiss();
                    } else if (iv_back_aadhar.getDrawable() == null) {
                        Toast.makeText(UpdateUserDataActivity.this, "Please select back side aadhar card photo.", Toast.LENGTH_SHORT).show();
                        dialog.dismiss();
                    } else {
                        // Internet Available
                        if ((!(filePath.equals("")) && (!(frontaadhar_filePath.equals(""))) && (!(backaadhar_filePath.equals(""))))) {
                            StringRequest request = new StringRequest(Request.Method.POST, BaseUrl.aadharNumberChecking, new Response.Listener<String>() {
                                @Override
                                public void onResponse(String response) {
                                    if (response.contains("aadhar number not found")) {
                                        et_aadharno.setEnabled(false);
                                        generateOTP();
                                        //Toast.makeText(OurProfileActivity.this, "Aadhar number is not found in database.", Toast.LENGTH_SHORT).show();
                                    }

                                    if (response.contains("aadhar number and id same")) {
                                        et_aadharno.setEnabled(false);
                                        generateOTP();
                                        //Toast.makeText(OurProfileActivity.this, "Aadhar number is yours.", Toast.LENGTH_SHORT).show();
                                    }

                                    if (response.contains("aadhar number and id is not same")) {
                                        dialog.dismiss();
                                        Toast.makeText(UpdateUserDataActivity.this, "Aadhar number is already exists in database.", Toast.LENGTH_SHORT).show();
                                    }
                                }
                            }, new Response.ErrorListener() {
                                @Override
                                public void onErrorResponse(VolleyError error) {
                                    dialog.dismiss();
                                    String message = null;
                                    if (error instanceof NetworkError) {
                                        message = "Cannot connect to Internet...Please check your connection!";
                                        Toast.makeText(UpdateUserDataActivity.this, message, Toast.LENGTH_SHORT).show();
                                    } else if (error instanceof ServerError) {
                                        message = "The server could not be found. Please try again after some time!!";
                                        Toast.makeText(UpdateUserDataActivity.this, message, Toast.LENGTH_SHORT).show();
                                    } else if (error instanceof ParseError) {
                                        message = "Parsing error! Please try again after some time!!";
                                        Toast.makeText(UpdateUserDataActivity.this, message, Toast.LENGTH_SHORT).show();
                                    } else if (error instanceof TimeoutError || error instanceof NoConnectionError) {
                                        message = "either time out or there is no connection! Please try again after some time!!";
                                        Toast.makeText(UpdateUserDataActivity.this, message, Toast.LENGTH_SHORT).show();
                                    } else if (error instanceof AuthFailureError) {
                                        message = "Error indicating that there was an Authentication Failure while performing the request";
                                        Toast.makeText(UpdateUserDataActivity.this, message, Toast.LENGTH_SHORT).show();
                                    }
                                }
                            }) {
                                @Override
                                protected Map<String, String> getParams() throws AuthFailureError {
                                    Map<String, String> map = new HashMap<>();
                                    map.put("id", et_userid.getText().toString().trim());
                                    map.put("aadhar_number", aadharNumber);
                                    return map;
                                }
                            };
                            Volley.newRequestQueue(UpdateUserDataActivity.this).add(request);
                        } else {
                            dialog.dismiss();
                            Toast.makeText(UpdateUserDataActivity.this, "Please attach documents properly.", Toast.LENGTH_SHORT).show();
                        }
                    }
                } else {
                    dialog.dismiss();
                    Toast.makeText(UpdateUserDataActivity.this, "Please check your internet connection.", Toast.LENGTH_SHORT).show();
                }
            }
        }, 3000);
    }

    public void generateOTP() {
        mobileNumberForOTP = et_mobilenoforotp.getText().toString();
        if (mobileNumberForOTP.length() < 10) {
            dialog.dismiss();
            Toast.makeText(this, "Please enter valid mobile number for otp.", Toast.LENGTH_SHORT).show();
        } else {
            et_mobilenoforotp.setEnabled(false);
            Random rnd = new Random();
            otpNumber = rnd.nextInt(999999);
            current_date = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()).format(new Date());
            current_time = new SimpleDateFormat("hh:mm:ss", Locale.getDefault()).format(new Date());

            StringRequest request = new StringRequest(Request.Method.POST, BaseUrl.smsUrl, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    if (response.contains("otp sent")) {
                        dialog.dismiss();
                        et_mobilenoforotp.setEnabled(false);
                        btn_otp.setEnabled(false);
                        btn_editmobileno.setEnabled(true);
                        btn_submitdata.setEnabled(true);
                        Toast.makeText(UpdateUserDataActivity.this, "OTP sent to your mobile number.", Toast.LENGTH_SHORT).show();
                    }
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    dialog.dismiss();
                    String message = null;
                    if (error instanceof NetworkError) {
                        message = "Cannot connect to Internet...Please check your connection!";
                        Toast.makeText(UpdateUserDataActivity.this, message, Toast.LENGTH_SHORT).show();
                    } else if (error instanceof ServerError) {
                        message = "The server could not be found. Please try again after some time!!";
                        Toast.makeText(UpdateUserDataActivity.this, message, Toast.LENGTH_SHORT).show();
                    } else if (error instanceof ParseError) {
                        message = "Parsing error! Please try again after some time!!";
                        Toast.makeText(UpdateUserDataActivity.this, message, Toast.LENGTH_SHORT).show();
                    } else if (error instanceof TimeoutError || error instanceof NoConnectionError) {
                        message = "either time out or there is no connection! Please try again after some time!!";
                        Toast.makeText(UpdateUserDataActivity.this, message, Toast.LENGTH_SHORT).show();
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
            Volley.newRequestQueue(UpdateUserDataActivity.this).add(request);
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
                    aadharNumber = et_aadharno.getText().toString();
                    accountNumber = et_bank_ac_no.getText().toString();
                    ifscCode = et_bank_ifsc.getText().toString();

                    if (aadharNumber.isEmpty()) {
                        Toast.makeText(UpdateUserDataActivity.this, "Please enter valid aadhar number.", Toast.LENGTH_SHORT).show();
                        et_aadharno.setError("Please enter aadhar number.");
                        dialog.dismiss();
                    } else if (aadharNumber.length() < 12) {
                        Toast.makeText(UpdateUserDataActivity.this, "Please enter valid aadhar number.", Toast.LENGTH_SHORT).show();
                        et_aadharno.setError("Please enter valid aadhar number.");
                        dialog.dismiss();
                    } else if (profilePhoto.getDrawable() == null) {
                        Toast.makeText(UpdateUserDataActivity.this, "Please select profile photo.", Toast.LENGTH_SHORT).show();
                        dialog.dismiss();
                    } else if (iv_front_aadhar.getDrawable() == null) {
                        Toast.makeText(UpdateUserDataActivity.this, "Please select front side aadhar card photo.", Toast.LENGTH_SHORT).show();
                        dialog.dismiss();
                    } else if (iv_back_aadhar.getDrawable() == null) {
                        Toast.makeText(UpdateUserDataActivity.this, "Please select back side aadhar card photo.", Toast.LENGTH_SHORT).show();
                        dialog.dismiss();
                    } else if (!(et_otp.getText().toString().equals(String.valueOf(otpNumber)))) {
                        Toast.makeText(UpdateUserDataActivity.this, "Please enter valid otp.", Toast.LENGTH_SHORT).show();
                        dialog.dismiss();
                    } else {
                        // Internet Available
                        if ((!(filePath.equals("")) && (!(frontaadhar_filePath.equals(""))) && (!(backaadhar_filePath.equals(""))))) {
                            profileImgUploder();
                            aadharFrontImgUploder();
                            aadharBackImgUploder();
                            accountDetailsImgUploder();
                            uploadData();
                        } else {
                            dialog.dismiss();
                            Toast.makeText(UpdateUserDataActivity.this, "Please attach documents properly.", Toast.LENGTH_SHORT).show();
                        }
                    }
                } else {
                    dialog.dismiss();
                }
            }
        }, 5000);
    }

    public void uploadData() {
        StringRequest request = new StringRequest(Request.Method.POST, BaseUrl.updateUserProfileUrl, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                if (response.contains("connection not got")) {
                    btn_submitdata.setEnabled(true);
                    dialog.dismiss();
                    Toast.makeText(UpdateUserDataActivity.this, "Server error.", Toast.LENGTH_SHORT).show();
                }

                if (response.contains("data updated")) {
                    dialog.dismiss();
                    clearMyCache(UpdateUserDataActivity.this);
                    Toast.makeText(UpdateUserDataActivity.this, "Updated & Cache Cleared..!", Toast.LENGTH_SHORT).show();

                    android.app.AlertDialog.Builder adb = new android.app.AlertDialog.Builder(UpdateUserDataActivity.this);
                    adb.setTitle(R.string.dialog_title);
                    adb.setMessage("Data updated...!");
                    adb.setIcon(R.drawable.app_logo);
                    adb.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            dialogInterface.dismiss();
                            onBackPressed();
                        }
                    });
                    adb.show();
                }

                if (response.contains("data is not updated")) {
                    btn_submitdata.setEnabled(true);
                    dialog.dismiss();
                    Toast.makeText(UpdateUserDataActivity.this, "Data is not Updated...", Toast.LENGTH_SHORT).show();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                btn_submitdata.setEnabled(true);
                dialog.dismiss();
                String message = null;
                if (error instanceof NetworkError) {
                    message = "Cannot connect to Internet...Please check your connection!";
                    Toast.makeText(UpdateUserDataActivity.this, message, Toast.LENGTH_SHORT).show();
                } else if (error instanceof ServerError) {
                    message = "The server could not be found. Please try again after some time!!";
                    Toast.makeText(UpdateUserDataActivity.this, message, Toast.LENGTH_SHORT).show();
                } else if (error instanceof ParseError) {
                    message = "Error! Please try again after some time!!";
                    Toast.makeText(UpdateUserDataActivity.this, message, Toast.LENGTH_SHORT).show();
                } else if (error instanceof TimeoutError || error instanceof NoConnectionError) {
                    message = "Either time out or there is no connection! Please try again after some time!!";
                    Toast.makeText(UpdateUserDataActivity.this, message, Toast.LENGTH_SHORT).show();
                }
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> map = new HashMap<>();
                map.put("id", et_userid.getText().toString());
                map.put("newimg_url", BaseUrl.dbProfileImageUrl + et_userid.getText().toString() + ".jpg");
                map.put("oldimg_name", oldImageName);
                map.put("address", et_address.getText().toString());
                map.put("password", et_password.getText().toString());
                map.put("is_kyc", "Under Review");
                map.put("aadhar_number", aadharNumber);
                map.put("aadhar_frontphoto", BaseUrl.dbDocumentImageUrl + et_userid.getText().toString() + "_aadharfront.jpg");
                map.put("aadhar_oldfrontphoto_url", oldFrontAadhar);
                map.put("aadhar_backphoto", BaseUrl.dbDocumentImageUrl + et_userid.getText().toString() + "_aadharback.jpg");
                map.put("aadhar_oldbackphoto_url", oldBackAadhar);
                map.put("account_number", accountNumber);
                map.put("account_ifsc", ifscCode);
                map.put("account_details", BaseUrl.dbDocumentImageUrl + et_userid.getText().toString() + "_account.jpg");
                map.put("account_olddetails", oldAccountDetailsUrl);
                map.put("user_type", "admin");
                return map;
            }
        };
        Volley.newRequestQueue(UpdateUserDataActivity.this).add(request);
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

    public void methodEditMobileNumberForOTP(View view) {
        et_mobilenoforotp.setEnabled(true);
        btn_otp.setEnabled(true);
        btn_editmobileno.setEnabled(false);
        btn_submitdata.setEnabled(false);
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
                    Toast.makeText(UpdateUserDataActivity.this, "Internet is not available.", Toast.LENGTH_SHORT).show();
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
        android.app.AlertDialog.Builder adb = new android.app.AlertDialog.Builder(this);
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

    public void bringImagePicker() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE_REQUEST);
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
                            Toast.makeText(UpdateUserDataActivity.this, message, Toast.LENGTH_SHORT).show();
                        } else if (error instanceof ServerError) {
                            message = "The server could not be found. Please try again after some time!!";
                            Toast.makeText(UpdateUserDataActivity.this, message, Toast.LENGTH_SHORT).show();
                        } else if (error instanceof ParseError) {
                            message = "Error! Please try again after some time!!";
                            Toast.makeText(UpdateUserDataActivity.this, message, Toast.LENGTH_SHORT).show();
                        } else if (error instanceof TimeoutError || error instanceof NoConnectionError) {
                            message = "Either time out or there is no connection! Please try again after some time!!";
                            Toast.makeText(UpdateUserDataActivity.this, message, Toast.LENGTH_SHORT).show();
                        } else if (error instanceof AuthFailureError) {
                            message = "Error indicating that there was an Authentication Failure while performing the request";
                            Toast.makeText(UpdateUserDataActivity.this, message, Toast.LENGTH_SHORT).show();
                        }
                    }
                }) {
            @Override
            protected Map<String, DataPart> getByteData() {
                Map<String, DataPart> params = new HashMap<>();
                params.put("fileToUpload", new DataPart(et_userid.getText().toString() + ".jpg", getFileDataFromDrawable(profile_bitmap)));
                return params;
            }
        };
        Volley.newRequestQueue(UpdateUserDataActivity.this).add(volleyMultipartRequest);
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
                            Toast.makeText(UpdateUserDataActivity.this, message, Toast.LENGTH_SHORT).show();
                        } else if (error instanceof ServerError) {
                            message = "The server could not be found. Please try again after some time!!";
                            Toast.makeText(UpdateUserDataActivity.this, message, Toast.LENGTH_SHORT).show();
                        } else if (error instanceof ParseError) {
                            message = "Error! Please try again after some time!!";
                            Toast.makeText(UpdateUserDataActivity.this, message, Toast.LENGTH_SHORT).show();
                        } else if (error instanceof TimeoutError || error instanceof NoConnectionError) {
                            message = "Either time out or there is no connection! Please try again after some time!!";
                            Toast.makeText(UpdateUserDataActivity.this, message, Toast.LENGTH_SHORT).show();
                        } else if (error instanceof AuthFailureError) {
                            message = "Error indicating that there was an Authentication Failure while performing the request";
                            Toast.makeText(UpdateUserDataActivity.this, message, Toast.LENGTH_SHORT).show();
                        }
                    }
                }) {
            @Override
            protected Map<String, DataPart> getByteData() {
                Map<String, DataPart> params = new HashMap<>();
                params.put("fileToUpload", new DataPart(et_userid.getText().toString() + "_aadharfront.jpg", getFileDataFromDrawable(frontaadhar_bitmap)));
                return params;
            }
        };
        Volley.newRequestQueue(UpdateUserDataActivity.this).add(volleyMultipartRequest);
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
                            Toast.makeText(UpdateUserDataActivity.this, message, Toast.LENGTH_SHORT).show();
                        } else if (error instanceof ServerError) {
                            message = "The server could not be found. Please try again after some time!!";
                            Toast.makeText(UpdateUserDataActivity.this, message, Toast.LENGTH_SHORT).show();
                        } else if (error instanceof ParseError) {
                            message = "Error! Please try again after some time!!";
                            Toast.makeText(UpdateUserDataActivity.this, message, Toast.LENGTH_SHORT).show();
                        } else if (error instanceof TimeoutError || error instanceof NoConnectionError) {
                            message = "Either time out or there is no connection! Please try again after some time!!";
                            Toast.makeText(UpdateUserDataActivity.this, message, Toast.LENGTH_SHORT).show();
                        } else if (error instanceof AuthFailureError) {
                            message = "Error indicating that there was an Authentication Failure while performing the request";
                            Toast.makeText(UpdateUserDataActivity.this, message, Toast.LENGTH_SHORT).show();
                        }
                    }
                }) {
            @Override
            protected Map<String, DataPart> getByteData() {
                Map<String, DataPart> params = new HashMap<>();
                params.put("fileToUpload", new DataPart(et_userid.getText().toString() + "_aadharback.jpg", getFileDataFromDrawable(backaadhar_bitmap)));
                return params;
            }
        };
        Volley.newRequestQueue(UpdateUserDataActivity.this).add(volleyMultipartRequest);
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
                            Toast.makeText(UpdateUserDataActivity.this, message, Toast.LENGTH_SHORT).show();
                        } else if (error instanceof ServerError) {
                            message = "The server could not be found. Please try again after some time!!";
                            Toast.makeText(UpdateUserDataActivity.this, message, Toast.LENGTH_SHORT).show();
                        } else if (error instanceof ParseError) {
                            message = "Error! Please try again after some time!!";
                            Toast.makeText(UpdateUserDataActivity.this, message, Toast.LENGTH_SHORT).show();
                        } else if (error instanceof TimeoutError || error instanceof NoConnectionError) {
                            message = "Either time out or there is no connection! Please try again after some time!!";
                            Toast.makeText(UpdateUserDataActivity.this, message, Toast.LENGTH_SHORT).show();
                        } else if (error instanceof AuthFailureError) {
                            message = "Error indicating that there was an Authentication Failure while performing the request";
                            Toast.makeText(UpdateUserDataActivity.this, message, Toast.LENGTH_SHORT).show();
                        }
                    }
                }) {
            @Override
            protected Map<String, DataPart> getByteData() {
                Map<String, DataPart> params = new HashMap<>();
                params.put("fileToUpload", new DataPart(et_userid.getText().toString() + "_account.jpg", getFileDataFromDrawable(account_bitmap)));
                return params;
            }
        };
        Volley.newRequestQueue(UpdateUserDataActivity.this).add(volleyMultipartRequest);
    }

    public byte[] getFileDataFromDrawable(Bitmap bitmap) {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 70, byteArrayOutputStream); //80
        return byteArrayOutputStream.toByteArray();
    }

    // checking storage permissions
    private Boolean checkStoragePermission() {
        boolean result1 = ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) == (PackageManager.PERMISSION_GRANTED);
        return result1;
    }

    // checking Calling permissions
    private Boolean checkCallPermission() {
        boolean result1 = ContextCompat.checkSelfPermission(this, Manifest.permission.CALL_PHONE) == (PackageManager.PERMISSION_GRANTED);
        return result1;
    }

    // Requesting  gallery permission
    private void requestStoragePermission() {
        ActivityCompat.requestPermissions(this, storagePermission, STORAGE_REQUEST);
    }

    // Requesting call permission
    private void requestCallPermission() {
        ActivityCompat.requestPermissions(this, callPermission, CALL_REQ);
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

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case STORAGE_REQUEST: {
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    bringImagePicker();
                } else {
                    //requestStoragePermission();
                    Toast.makeText(this, "Please give Storage Permission.", Toast.LENGTH_LONG).show();
                }
            }

            case CALL_REQ: {
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                } else {
                    //requestStoragePermission();
                    Toast.makeText(this, "Please give Calling Permission.", Toast.LENGTH_LONG).show();
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
                        profilePhoto.setImageBitmap(profile_bitmap);
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
                        iv_front_aadhar.setImageBitmap(frontaadhar_bitmap);
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
                        iv_back_aadhar.setImageBitmap(backaadhar_bitmap);
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
                        iv_bank_ac.setImageBitmap(account_bitmap);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                } else {
                    Toast.makeText(this, "no img selected.", Toast.LENGTH_SHORT).show();
                }
            }
        }
    }

    public void dataSet() {
        et_userid.setText(getIntent().getStringExtra("userid"));
        et_name.setText(getIntent().getStringExtra("name"));
        et_mobileno.setText(getIntent().getStringExtra("mobile"));
        et_refid.setText(getIntent().getStringExtra("refid"));
        et_address.setText(getIntent().getStringExtra("address"));
        et_plan.setText(getIntent().getStringExtra("plan"));
        et_paid.setText(getIntent().getStringExtra("ispaid"));
        et_doj.setText(getIntent().getStringExtra("doj"));
        et_caplimit.setText(getIntent().getStringExtra("cap_limit"));
        et_noofleg.setText(getIntent().getStringExtra("no_of_leg"));
        if (getIntent().getStringExtra("kyc").equals("") || getIntent().getStringExtra("kyc").equals("No")) {
            et_kyc.setText("No");
            tv_view_front_aadhar.setVisibility(View.GONE);
            tv_view_back_aadhar.setVisibility(View.GONE);
            tv_view_bank_ac.setVisibility(View.GONE);
            btn_editmobileno.setEnabled(false);
            btn_submitdata.setEnabled(false);
        } else {
            tv_view_front_aadhar.setVisibility(View.VISIBLE);
            tv_view_back_aadhar.setVisibility(View.VISIBLE);
            tv_view_bank_ac.setVisibility(View.VISIBLE);
            et_kyc.setText(getIntent().getStringExtra("kyc"));
            btn_editmobileno.setEnabled(false);
            btn_submitdata.setEnabled(false);
        }
        et_aadharno.setText(getIntent().getStringExtra("aadhar_number"));
        et_bank_ac_no.setText(getIntent().getStringExtra("bank_ac_number"));
        et_bank_ifsc.setText(getIntent().getStringExtra("bank_ac_ifsc"));
        et_password.setText(getIntent().getStringExtra("password"));
        intent_profileUrl = getIntent().getStringExtra("profileUrl");
        filePath = getIntent().getStringExtra("profileUrl");
        intent_frontAadharUrl = getIntent().getStringExtra("aadhar_front_url");
        frontaadhar_filePath = getIntent().getStringExtra("aadhar_front_url");
        intent_backAadharUrl = getIntent().getStringExtra("aadhar_back_url");
        backaadhar_filePath = getIntent().getStringExtra("aadhar_back_url");
        intent_bankAcUrl = getIntent().getStringExtra("bank_ac_url");
        account_filePath = getIntent().getStringExtra("bank_ac_url");

        if (intent_profileUrl.isEmpty()) {
            Picasso.with(UpdateUserDataActivity.this).load(R.drawable.ic_user_dummy).into(profilePhoto);
        } else {
            Picasso.with(UpdateUserDataActivity.this).load(intent_profileUrl).into(profilePhoto);
        }

        if (intent_frontAadharUrl.isEmpty()) {
            Picasso.with(UpdateUserDataActivity.this).load(R.drawable.dummy_front_aadhar).into(iv_front_aadhar);
        } else {
            Picasso.with(UpdateUserDataActivity.this).load(intent_frontAadharUrl).into(iv_front_aadhar);
        }

        if (intent_backAadharUrl.isEmpty()) {
            Picasso.with(UpdateUserDataActivity.this).load(R.drawable.dummy_back_aadhar).into(iv_back_aadhar);
        } else {
            Picasso.with(UpdateUserDataActivity.this).load(intent_backAadharUrl).into(iv_back_aadhar);
        }

        if (intent_bankAcUrl.isEmpty()) {
            Picasso.with(UpdateUserDataActivity.this).load(R.drawable.dummy_cheque_passbook).into(iv_bank_ac);
        } else {
            Picasso.with(UpdateUserDataActivity.this).load(intent_bankAcUrl).into(iv_bank_ac);
        }
    }

    public void findingViews() {
        edittext_update_mobilenumber = findViewById(R.id.edittext_update_mobilenumber);

        et_userid = findViewById(R.id.et_update_id);
        et_name = findViewById(R.id.et_update_name);
        et_mobileno = findViewById(R.id.et_update_mobilenumber);
        et_refid = findViewById(R.id.et_update_reference);
        et_address = findViewById(R.id.et_update_address);
        et_plan = findViewById(R.id.et_update_plan);
        et_paid = findViewById(R.id.et_update_paid);
        et_doj = findViewById(R.id.et_update_doj);
        et_kyc = findViewById(R.id.et_update_kyc);
        et_caplimit = findViewById(R.id.et_update_caplimit);
        et_noofleg = findViewById(R.id.et_update_noofleg);
        et_aadharno = findViewById(R.id.et_update_aadharnumber);
        et_bank_ac_no = findViewById(R.id.et_update_bankac_number);
        et_bank_ifsc = findViewById(R.id.et_update_ifsc_code);
        et_password = findViewById(R.id.et_update_password);
        et_mobilenoforotp = findViewById(R.id.et_update_mobilenumberforotp);
        et_otp = findViewById(R.id.et_update_otp);

        profilePhoto = findViewById(R.id.imageview_profile);
        add_profile_photo = findViewById(R.id.add_image_button);
        iv_front_aadhar = findViewById(R.id.aadharcard_front_photo);
        iv_back_aadhar = findViewById(R.id.aadharcard_back_photo);
        iv_bank_ac = findViewById(R.id.bankaccount_photo);

        tv_view_front_aadhar = findViewById(R.id.tv_seefront_aadhar);
        tv_view_back_aadhar = findViewById(R.id.tv_seeback_aadhar);
        tv_view_bank_ac = findViewById(R.id.tv_seeaccount);

        btn_otp = findViewById(R.id.btn_update_getotp);
        btn_editmobileno = findViewById(R.id.btn_update_editmobile);
        btn_submitdata = findViewById(R.id.btn_submitdata);
    }
}