package com.ithublive.ktclife;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
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
import com.ithublive.ktclife.Utils.BaseUrl;

import java.util.HashMap;
import java.util.Map;

public class SignUpActivity extends AppCompatActivity {

    EditText editText_signup_name, editText_signup_mobilenumber, editText_signup_reference, editText_signup_password;
    String name, mobile, reference, address, password;
    Dialog dialog;
    SharedPreferences sharedPreferences_retriveUserData;
    String user_id, plan = "", retriveUserData, finalName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        sharedPreferences_retriveUserData = getSharedPreferences("" + R.string.sp_retriveUserData, MODE_PRIVATE);
        editText_signup_name = findViewById(R.id.et_signup_name);
        editText_signup_mobilenumber = findViewById(R.id.et_signup_mobilenumber);
        editText_signup_reference = findViewById(R.id.et_signup_reference);
        editText_signup_password = findViewById(R.id.et_signup_password);

        dialog = new Dialog(SignUpActivity.this);
        dialog.setContentView(R.layout.layout_progress_dialog);
        if (dialog.getWindow() != null) {
            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(0));
        }
        dialog.setCancelable(false);

        SharedPreferences shared = getSharedPreferences("" + R.string.sp_retriveUserData, MODE_PRIVATE);
        retriveUserData = (shared.getString("data", ""));

        if (retriveUserData.contains("no data found")) {
            AlertDialog.Builder adb = new AlertDialog.Builder(this);
            adb.setTitle(R.string.dialog_title);
            adb.setMessage("There are no data found");
            adb.setIcon(R.drawable.app_logo);
            adb.setCancelable(false);
            adb.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    dialogInterface.dismiss();
                    onBackPressed();
                }
            });
            adb.show();
        } else {
            if (NoLoginHomePage.isNoLoginHomePage) {
            } else {
                user_id = retriveUserData.substring(retriveUserData.indexOf("!") + 1, retriveUserData.indexOf("@"));
                editText_signup_reference.setText(user_id);
                editText_signup_reference.setEnabled(false);
            }
        }
    }

    public void methodSubmitSignUpData(View view) {
        dialog.show();

        name = editText_signup_name.getText().toString();
        mobile = editText_signup_mobilenumber.getText().toString();
        reference = editText_signup_reference.getText().toString();
        address = "";
        password = editText_signup_password.getText().toString();
        String removeSpicalChar = name.replaceAll("[^a-zA-Z0-9]", " ");
        finalName = removeSpicalChar.replaceAll("[0-9]","");

        if (finalName.isEmpty()) {
            editText_signup_name.setError("Please enter full name.");
            editText_signup_name.requestFocus();
            dialog.dismiss();
        } else if (mobile.isEmpty() || mobile.length() < 10) {
            editText_signup_mobilenumber.setError("Please enter mobile number.");
            editText_signup_mobilenumber.requestFocus();
            dialog.dismiss();
        } else if (reference.isEmpty() || reference.length() < 11) {
            editText_signup_reference.setError("Please enter reference number.");
            editText_signup_reference.requestFocus();
            dialog.dismiss();
        } else if (password.isEmpty() || password.length() > 12 || password.length() < 4) {
            editText_signup_password.setError("Please enter password min 4 and max 12.");
            editText_signup_password.requestFocus();
            dialog.dismiss();
        } else {
            registerUser("nooooooo");
        }
    }

    public void registerUser(final String re_reg) {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, BaseUrl.signUpUrl, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d("KTC Life", "Response:" + response);
                if (response.contains("#position#") && !response.contains("#direct_ref_added#")) {
                    dialog.dismiss();
                    final String newIdAdded = response.substring(response.indexOf("#position#") + 10, response.indexOf("@"));
                    Toast.makeText(SignUpActivity.this, "User Added as a direct reference.", Toast.LENGTH_LONG).show();
                    final AlertDialog.Builder adb = new AlertDialog.Builder(SignUpActivity.this);
                    adb.setTitle(R.string.dialog_title);
                    adb.setMessage(Html.fromHtml("User added as a leg under " + reference + ". \n The new id is " + newIdAdded +
                            " <font color='red'> ,Please sign in and complete KYC process.</font>."));
                    adb.setIcon(R.drawable.app_logo);
                    adb.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            dialogInterface.dismiss();
                            Intent intent = new Intent(getApplicationContext(), ProfileImageActivity.class);
                            intent.putExtra("userId", newIdAdded);
                            startActivity(intent);
                        }
                    });
                    adb.setCancelable(false);
                    adb.show();
                }

                if (response.contains("#leg_qryFail45#")) {
                    dialog.dismiss();
                    Toast.makeText(SignUpActivity.this, "Query fail. try again", Toast.LENGTH_LONG).show();
                }
                if (response.contains("#leg_qryFail59#")) {
                    dialog.dismiss();
                    Toast.makeText(SignUpActivity.this, "Query fail. try again", Toast.LENGTH_LONG).show();
                }

                if (response.contains("#mob_used#")) {
                    int used_times = Integer.parseInt(response.substring(response.indexOf("#cX#") + 4, response.indexOf("#ecX#")));
                    dialog.dismiss();
                    if (used_times == 9) {
                        showPopupWithoutBack("The provided mobile is already registered for max limit.");
                    } else {
                        String string_all_r_ids = response.substring(response.indexOf("#ecX#") + 5, response.indexOf("#eids#"));
                        string_all_r_ids = string_all_r_ids.replace(",", "\n");

                        final AlertDialog.Builder adb = new AlertDialog.Builder(SignUpActivity.this);
                        adb.setTitle(R.string.dialog_title);
                        adb.setMessage("The provided mobile is already registered  " + used_times +
                                "  times with id's as below\n " + string_all_r_ids + "\n Do you want to register it again ?? ");
                        adb.setIcon(R.drawable.app_logo);
                        adb.setPositiveButton("YES, Register Again", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                dialog.show();
                                registerUser("re_registration");
                            }
                        });
                        adb.setNegativeButton("NO", null);
                        adb.setCancelable(false);
                        adb.show();
                    }
                }

                if (response.contains("#no_ref_33#")) {
                    dialog.dismiss();
                    Toast.makeText(SignUpActivity.this, "Reference id does not exists.", Toast.LENGTH_LONG).show();
                    showPopupWithoutBack("Reference id does not exists.");
                }
                if (response.contains("#second_id_not_exists#")) {
                    dialog.dismiss();
                    Toast.makeText(SignUpActivity.this, "Secondary Reference id dose not exists.", Toast.LENGTH_LONG).show();
                    showPopupWithoutBack("Secondary Reference id dose not exists.");
                }
                if (response.contains("#notIn_SameTree#")) {
                    dialog.dismiss();
                    Toast.makeText(SignUpActivity.this, "Secondary Reference id is not your child. ", Toast.LENGTH_LONG).show();
                    showPopupWithoutBack("Secondary Reference id is not your child .");
                }
                if (response.contains("#UndLegHs8Lg")) {
                    dialog.dismiss();
                    Toast.makeText(SignUpActivity.this, "ID has 8 Legs Full.", Toast.LENGTH_LONG).show();
                    showPopupWithoutBack("Secondary ID has 8 Legs Full.");
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(SignUpActivity.this, "Server error.", Toast.LENGTH_LONG).show();
                dialog.dismiss();
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> map = new HashMap<>();
                map.put("name", finalName);
                map.put("mobile_no", mobile);
                map.put("reference", reference);
                map.put("profile_imgurl", "");
                map.put("address", address);
                map.put("plan", plan);
                map.put("password", password);
                map.put("kyc", "No");
                if (re_reg.equals("re_registration")) {
                    map.put("re_reg", "ho_re");
                }
                return map;
            }
        };
        RetryPolicy mRetryPolicy = new DefaultRetryPolicy(
                22000,
                0,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
        stringRequest.setRetryPolicy(mRetryPolicy);
        Volley.newRequestQueue(SignUpActivity.this).add(stringRequest);
    }

    public void showPopupWithoutBack(String message) {
        final AlertDialog.Builder adb = new AlertDialog.Builder(this);
        adb.setTitle(R.string.dialog_title);
        adb.setMessage(message);
        adb.setIcon(R.drawable.app_logo);
        adb.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
            }
        });
        adb.setCancelable(false);
        adb.show();
    }
}