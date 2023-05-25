package com.ithublive.ktclife;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.SpinnerAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.RetryPolicy;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.ithublive.ktclife.Utils.BaseUrl;

import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import static com.ithublive.ktclife.R.drawable.level_gold;
import static com.ithublive.ktclife.R.drawable.level_silver;

public class IdPaymentActivity extends AppCompatActivity {

    Dialog dialog;
    String last_pay_date = "";
    double t_bonus;
    EditText et_userid, et_installmentNo, et_date, et_amount;
    Button btn_getInfo, btn_makepayment;
    String retriveUserData, dbName, dbPlan, myUserid, allData, my_Wallet_Password;
    int paidCount;
    TextView tv_userInfo, tv_plan;
    Spinner planSpinner;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_id_payment);

        allData = getIntent().getStringExtra("udataall");
        myUserid = allData.substring(allData.indexOf("!") + 1, allData.indexOf("@"));

        dialog = new Dialog(IdPaymentActivity.this);
        dialog.setContentView(R.layout.layout_progress_dialog);
        if (dialog.getWindow() != null) {
            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(0));
        }
        dialog.setCancelable(false);

        et_userid = findViewById(R.id.et_userid_payinstall);
        btn_getInfo = findViewById(R.id.btn_getinfo);
        btn_makepayment = findViewById(R.id.btnMakePayment_payinstall);
        tv_userInfo = findViewById(R.id.tv_userInfo_payidinstall);
        tv_plan = findViewById(R.id.tv_plan);
        planSpinner = findViewById(R.id.planspinner);
        et_installmentNo = findViewById(R.id.et_installmentNum_payinstall);
        et_date = findViewById(R.id.et_paydate_payinstall);
        et_amount = findViewById(R.id.et_payAmount_payinstall);
    }

    public void method_getInfo(View view) {
        dialog.show();

        if (et_userid.getText().toString().trim().length() < 11) {
            Toast.makeText(this, "Please enter valid beneficiary id.", Toast.LENGTH_SHORT).show();
            dialog.dismiss();
        } else {
            StringRequest stringRequest = new StringRequest(Request.Method.POST, BaseUrl.retriveUserDataUrl, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    if (response.contains("no data found")) {
                        dialog.dismiss();
                        Toast.makeText(IdPaymentActivity.this, "No Data found.", Toast.LENGTH_SHORT).show();
                    }

                    if (response.contains(et_userid.getText().toString())) {
                        retriveUserData = response;
                        dbName = retriveUserData.substring(retriveUserData.indexOf("@") + 1, retriveUserData.indexOf("#"));
                        dbPlan = retriveUserData.substring(retriveUserData.indexOf("&") + 1, retriveUserData.indexOf("*"));
                        if (retriveUserData.contains("#pn#")) {
                            paidCount = Integer.parseInt(retriveUserData.substring(retriveUserData.indexOf("#pn#") + 4, retriveUserData.indexOf("#epn#")));
                        } else {
                            paidCount = 0;
                        }
                        methodGetBonus(myUserid);
                    }
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Toast.makeText(IdPaymentActivity.this, "Server error.", Toast.LENGTH_SHORT).show();
                    dialog.dismiss();
                }
            }) {
                @Override
                protected Map<String, String> getParams() throws AuthFailureError {
                    Map<String, String> map = new HashMap<>();
                    map.put("id", et_userid.getText().toString());
                    return map;
                }
            };
            Volley.newRequestQueue(IdPaymentActivity.this).add(stringRequest);
        }
    }

    public void method_payFromWallet(View view) {
        if (et_userid.getText().toString().isEmpty() || et_userid.getText().toString().length() < 11) {
            Toast.makeText(IdPaymentActivity.this, "Please fill properly.", Toast.LENGTH_SHORT).show();
        } else {
            if (retriveUserData.contains("#epn#")) {
                String payDetails = retriveUserData.substring(retriveUserData.indexOf("#epn#") + 5, retriveUserData.indexOf(":e_pay:"));
                String payDetails_array[] = payDetails.split(",");
                for (String data : payDetails_array) {
                    String p_date = data.substring(0, 10);
                    last_pay_date = p_date;
                }

                Date cal_now = new Date();
                int l_year = Integer.parseInt(last_pay_date.substring(0, 4));
                int l_month = Integer.parseInt(last_pay_date.substring(5, 7));
                int l_date = Integer.parseInt(last_pay_date.substring(8, 10));
                Date lastPayCalendar = new Date();//new Date(l_year-1900,l_month-1,l_date);
                lastPayCalendar.setYear(l_year - 1900);
                lastPayCalendar.setMonth(l_month - 1);
                lastPayCalendar.setDate(l_date);
                long diff = cal_now.getTime() - lastPayCalendar.getTime();
                long days_gap = diff / (24 * 60 * 60 * 1000);
                if (days_gap > 30) {
                    payFromWallet();
                } else {
                    AlertDialog.Builder dialog = new AlertDialog.Builder(IdPaymentActivity.this);
                    dialog.setTitle("Installment after 30 Days.");
                    dialog.setMessage("Your last installment paid on " + last_pay_date + ".\n You cant pay now. Pay installment after " + (30 - days_gap) + " days");
                    dialog.setIcon(R.drawable.app_logo);
                    dialog.setPositiveButton("OK", null);
                    dialog.setCancelable(false);
                    dialog.show();
                }
            } else {
                payFromWallet();
            }
        }
    }

    private void methodGetBonus(final String userId) {
        dialog.show();
        StringRequest stringRequest = new StringRequest(Request.Method.POST, BaseUrl.ktcBonusRequest, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                dialog.dismiss();
                if (response.contains("conn_error")) {
                    Toast.makeText(IdPaymentActivity.this, "Server error. Try Again", Toast.LENGTH_SHORT).show();
                }

                if (response.contains("bnus")) {
                    dialog.dismiss();
                    try {
                        t_bonus = Double.parseDouble(response.substring(response.indexOf("#bnus#") + 6, response.indexOf("#eb#")));
                    } catch (NumberFormatException ex) {
                        t_bonus = 0;
                    }
                    my_Wallet_Password = response.substring(response.indexOf("#pss#") + 5, response.indexOf("#pse#"));
                    et_userid.setEnabled(false);
                    btn_getInfo.setEnabled(false);
                    if (paidCount < 24) {
                        et_installmentNo.setText((paidCount + 1) + "");
                    }

                    Calendar calendar = Calendar.getInstance();
                    et_date.setText(calendar.get(Calendar.YEAR) + "-" + (calendar.get(Calendar.MONTH) + 1) + "-" + calendar.get(Calendar.DATE));

                    if (!(dbPlan.isEmpty())) {
                        tv_userInfo.setText(" Name: " + dbName + "\n Wallet Balance: " + t_bonus + "\n Plan: " + dbPlan);
                        et_amount.setText(dbPlan);
                    } else {
                        tv_userInfo.setText(" Name: " + dbName + "\n Wallet Balance: " + t_bonus);
                        tv_plan.setVisibility(View.VISIBLE);
                        planSpinner.setVisibility(View.VISIBLE);

                        String plans[] = {"-- Select Plans --", "1000", "2000", "3000", "4000", "5000", "10000", "15000", "20000"};
                        Adapter one_qty_unit = new ArrayAdapter<String>(IdPaymentActivity.this, android.R.layout.simple_dropdown_item_1line, plans);
                        planSpinner.setAdapter((SpinnerAdapter) one_qty_unit);
                        planSpinner.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
                            @Override
                            public void onGlobalLayout() {
                                ((TextView) planSpinner.getSelectedView()).setTextColor(Color.BLACK);
                                ((TextView) planSpinner.getSelectedView()).setTextSize(15);
                            }
                        });

                        planSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                                String selectedItem = parent.getItemAtPosition(position).toString();
                                et_amount.setText(selectedItem);
                            }

                            public void onNothingSelected(AdapterView<?> parent) {
                            }
                        });
                        ///// plans-spinner end /////
                    }
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(IdPaymentActivity.this, "Server error...", Toast.LENGTH_SHORT).show();
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
                0,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
        stringRequest.setRetryPolicy(mRetryPolicy);
        Volley.newRequestQueue(IdPaymentActivity.this).add(stringRequest);
    }

    public void selectedPlanSentToDb() {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, BaseUrl.updatePlanUrl, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                if (response.contains("Updated")) {
                    dialog.dismiss();
                }

                if (response.contains("Not updated")) {
                    dialog.dismiss();
                    Toast.makeText(IdPaymentActivity.this, "Plan is not added...", Toast.LENGTH_LONG).show();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                dialog.dismiss();
                Toast.makeText(IdPaymentActivity.this, "Plan not added... Server Error.", Toast.LENGTH_SHORT).show();
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> map = new HashMap<>();
                map.put("id", et_userid.getText().toString());
                map.put("plan", planSpinner.getSelectedItem().toString());
                return map;
            }
        };

        Volley.newRequestQueue(IdPaymentActivity.this).add(stringRequest);
    }

    public void method_cancel(View view) {
        et_userid.setText("");
        et_installmentNo.setText("");
        et_date.setText("");
        et_amount.setText("");
        tv_userInfo.setText("User Info");
        et_userid.setEnabled(true);
        btn_getInfo.setEnabled(true);
        tv_plan.setVisibility(View.GONE);
        planSpinner.setVisibility(View.GONE);
    }

    public void payFromWallet() {
        btn_makepayment.setEnabled(false);
        dialog.show();

        double d_plan = Double.parseDouble(et_amount.getText().toString());
        if (t_bonus < d_plan) {
            dialog.dismiss();
            Toast.makeText(this, "Wallet balence is less then your plan.", Toast.LENGTH_SHORT).show();
            btn_makepayment.setEnabled(true);
            return;
        }
        //////////SHOW PASSWORD ALERT DIALOG/////////////

        dialog.dismiss();
        android.app.AlertDialog.Builder adb = new android.app.AlertDialog.Builder(IdPaymentActivity.this);
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
                        new Handler().postDelayed(new Runnable() {
                            @Override
                            public void run() {
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
                    Toast.makeText(IdPaymentActivity.this, "Please fill all fields.", Toast.LENGTH_SHORT).show();
                } else if (!(wallet_password.equals(my_Wallet_Password))) {
                    dialog.dismiss();
                    Toast.makeText(IdPaymentActivity.this, "Wrong password, please enter valid passowrd.", Toast.LENGTH_SHORT).show();
                } else {
                    ////////
                    StringRequest requestToMakePayment = new StringRequest(Request.Method.POST, BaseUrl.updateUserDataByAdminApp,
                            new Response.Listener<String>() {
                                @Override
                                public void onResponse(String response) {
                                    dialog.dismiss();
                                    if (response.contains("ins_success")) {
                                        AlertDialog.Builder aleBuilder = new AlertDialog.Builder(IdPaymentActivity.this);
                                        aleBuilder.setTitle("KTC Life");
                                        String su = "th";
                                        if ((paidCount + 1) == 1 || (paidCount + 1) == 21) {
                                            su = "st";
                                        } else if ((paidCount + 1) == 2 || (paidCount + 1) == 22) {
                                            su = "nd";
                                        } else if ((paidCount + 1) == 3 || (paidCount + 1) == 23) {
                                            su = "rd";
                                        }
                                        if (dbPlan.isEmpty()) {
                                            selectedPlanSentToDb();
                                        }
                                        aleBuilder.setMessage("Your " + (paidCount + 1) + " " + su + " installment has been paid Successfully...");
                                        aleBuilder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialogInterface, int i) {
                                                startActivity(new Intent(IdPaymentActivity.this, HomeActivity.class));
                                            }
                                        });
                                        aleBuilder.setIcon(R.drawable.app_logo);
                                        aleBuilder.setCancelable(false);
                                        aleBuilder.show();
                                    }
                                    btn_makepayment.setEnabled(true);
                                }
                            },
                            new Response.ErrorListener() {
                                @Override
                                public void onErrorResponse(VolleyError error) {
                                    dialog.dismiss();
                                    Toast.makeText(IdPaymentActivity.this, "Error...", Toast.LENGTH_SHORT).show();
                                }
                            }) {
                        @Override
                        protected Map<String, String> getParams() throws AuthFailureError {
                            Map map = new HashMap();
                            map.put("myid", myUserid);
                            map.put("uid", et_userid.getText().toString());
                            map.put("inment_num", et_installmentNo.getText().toString());
                            map.put("amt", et_amount.getText().toString());
                            map.put("mode", "wallet_pay");
                            map.put("date", et_date.getText().toString().trim());
                            map.put("req_reson", "ins_payment_from_otherid");
                            return map;
                        }
                    };
                    RetryPolicy mRetryPolicy = new DefaultRetryPolicy(
                            22000,
                            0,
                            DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
                    requestToMakePayment.setRetryPolicy(mRetryPolicy);
                    Volley.newRequestQueue(IdPaymentActivity.this).add(requestToMakePayment);
                }
            }
        });
        adb.setPositiveButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
                btn_makepayment.setEnabled(true);
            }
        });
        adb.setView(view_1);
        adb.setIcon(R.drawable.app_logo);
        adb.show();
        /////////////
    }
}