package com.ithublive.ktclife;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.SpinnerAdapter;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.RetryPolicy;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.ithublive.ktclife.Adaptor.WalletHistoryListAdaptor;
import com.ithublive.ktclife.Admin.Adaptor.UplineListAdapter;
import com.ithublive.ktclife.Admin.Model.UplineModel;
import com.ithublive.ktclife.Admin.PaymentHistoryReport;
import com.ithublive.ktclife.Model.WalletHistoryModel;
import com.ithublive.ktclife.Utils.BaseUrl;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import static com.ithublive.ktclife.R.drawable.level_diamond_profile;
import static com.ithublive.ktclife.R.drawable.level_gold;
import static com.ithublive.ktclife.R.drawable.level_ruby_profile;
import static com.ithublive.ktclife.R.drawable.level_silver;

public class AdminUpdatePayments extends AppCompatActivity {

    Dialog dialog;
    private String poppay_details;
    EditText et_uid, et_userid, et_entryDate, et_amt;
    TextView tv_userInfo;
    EditText et_installmentNum, et_pay_date, et_pay_amount, et_payMode, et_pay_id;
    Button btnUpdatePayment, buttonCapLimitUpd, btnAddBonus, btnSubmitBonus, btnPlanWithdrawalHistory;
    Button buttonCashWithdrawlEntry;
    ListView listViewOfUpLines, listViewBonusDetails;
    ImageButton btn_datePicker;
    String username, useID, my_Plan, isPaid, tv_number_legs, str_caplimit, bonusEntryDate, bonusAmount, legID, installment_num, walletBal, wd_current_date;
    Spinner bonusSpinner;
    List<String> listBonus = new ArrayList<>();
    ArrayAdapter adapterForBonus;
    String bonusTypes[] = {"-- Select bonus type --", "Direct ref bonus", "Second ref bonus", "Direct renewal bonus", "Target bonus", "Level bonus"};
    List<String> listOfUpline;
    List<UplineModel> listUpline;
    boolean booleanFlagBonusFetched;
    String plan_amt, levelColor;
    int month_no, int_amt = 0;

    ////// To display users transactions for admin//////
    ListView listView_ofPayments;
    TextView tv_noTransaction, tv_GetAllTransaction7020;
    List<WalletHistoryModel> list_wallet_history;
    WalletHistoryListAdaptor walletHistoryListAdaptor;
    ////////

    ///// To reset account (payment)////
    TextView tv_makeWithdrawlAndResetAccount;
    ///////

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_update_payments);

        dialog = new Dialog(AdminUpdatePayments.this);
        dialog.setContentView(R.layout.layout_progress_dialog);
        if (dialog.getWindow() != null) {
            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(0));
        }
        dialog.setCancelable(false);

        tv_userInfo = findViewById(R.id.tv_userInfo12);
        et_uid = findViewById(R.id.et_userid8393);
        et_installmentNum = findViewById(R.id.et_installmentNum13);
        et_pay_date = findViewById(R.id.et_paydate14);
        et_pay_amount = findViewById(R.id.et_payAmount15);
        et_payMode = findViewById(R.id.et_payMode16);
        et_pay_id = findViewById(R.id.et_payid17);
        tv_GetAllTransaction7020 = findViewById(R.id.tv_GetAllTransaction7020);
        tv_GetAllTransaction7020.setVisibility(View.GONE);
        tv_makeWithdrawlAndResetAccount = findViewById(R.id.tv_makeWithdrawlAndResetAccount);
        tv_makeWithdrawlAndResetAccount.setVisibility(View.GONE);
        btn_datePicker = findViewById(R.id.btn_datepicker_updUserPay);
        btnUpdatePayment = findViewById(R.id.btnInsertPayment16);
        buttonCapLimitUpd = findViewById(R.id.btn_9576);
        btnAddBonus = findViewById(R.id.btn_addBonus);
        btnPlanWithdrawalHistory = findViewById(R.id.btn_planWithdrawlHistory);
        buttonCashWithdrawlEntry = findViewById(R.id.btn_bonusCashWithdrawl);

        Intent intent = getIntent();
        legID = intent.getStringExtra("u_info_id");
        installment_num = intent.getStringExtra("u_installment_num");
        if (legID != null && legID.length() == 11) {
            et_uid.setText(legID);
        }

        btnUpdatePayment.setEnabled(false);
        buttonCapLimitUpd.setEnabled(false);
        btnAddBonus.setEnabled(false);
        btnPlanWithdrawalHistory.setEnabled(false);
        buttonCashWithdrawlEntry.setEnabled(false);

        btn_datePicker.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Calendar calendar = Calendar.getInstance();
                DatePickerDialog datePickerDialog = new DatePickerDialog(AdminUpdatePayments.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker datePicker, int y, int m, int d) {
                        et_pay_date.setText(y + "-" + (m + 1) + "-" + d);
                    }
                }, calendar.get(Calendar.YEAR), (calendar.get(Calendar.MONTH)), calendar.get(Calendar.DATE));
                datePickerDialog.show();
            }
        });
    }

    public void getUserInfo(View view) {
        if (et_uid.getText().toString().length() != 11) {
            et_uid.setError("invalid id");
            et_uid.requestFocus();
            return;
        }
        dialog.show();
        StringRequest stringRequest = new StringRequest(Request.Method.POST, BaseUrl.retriveUserDataUrl, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                dialog.dismiss();
                if (response.contains("no data found")) {
                    Toast.makeText(AdminUpdatePayments.this, "no data found.", Toast.LENGTH_SHORT).show();
                } else {
                    tv_GetAllTransaction7020.setVisibility(View.VISIBLE);
                    tv_makeWithdrawlAndResetAccount.setVisibility(View.VISIBLE);
                    btnUpdatePayment.setEnabled(true);
                    buttonCapLimitUpd.setEnabled(true);
                    btnAddBonus.setEnabled(true);
                    btnPlanWithdrawalHistory.setEnabled(true);
                    buttonCashWithdrawlEntry.setEnabled(true);

                    String data = response;
                    useID = data.substring(data.indexOf("!") + 1, data.indexOf("@"));
                    username = data.substring(data.indexOf("@") + 1, data.indexOf("#"));
                    my_Plan = data.substring(data.indexOf("&") + 1, data.indexOf("*"));
                    isPaid = data.substring(data.indexOf("#1A#") + 4, data.indexOf("#2A#"));
                    str_caplimit = data.substring(data.indexOf("#21A#") + 5, data.indexOf("#22A#"));
                    walletBal = data.substring(data.indexOf("#88#") + 4, data.indexOf("#89#"));
                    tv_number_legs = data.substring(data.indexOf("#3A#") + 4, data.indexOf("#4A#"));
                    poppay_details = "Pay Details:\n";

                    int paidCount = 0;
                    if (data.contains("#pn#") && isPaid.contains("yes")) {
                        paidCount = Integer.parseInt(data.substring(data.indexOf("#pn#") + 4, data.indexOf("#epn#")));
                        String payDetails = data.substring(data.indexOf("#epn#") + 5, data.indexOf(":e_pay:"));
                        String payDetails_array[] = payDetails.split(",");
                        for (String pdata : payDetails_array) {
                            String p_date = pdata.substring(0, 10);
                            month_no = Integer.parseInt(pdata.substring(pdata.indexOf("::") + 2, pdata.indexOf(":_:")));
                            plan_amt = pdata.substring(pdata.indexOf(":_:") + 3, pdata.indexOf(":$:"));
                            poppay_details = poppay_details + month_no + "\t\t" + p_date + "\t\t" + plan_amt + "\n";
                        }
                    }
                    //////// Set up data
                    tv_userInfo.setText("ID: " + useID + "\nName: " + username + "\nPlan: " + my_Plan + "\t\t\tIs Paid:   " + isPaid +
                            "\nNumber of Legs: " + tv_number_legs + "\nWallet Balence: " + walletBal + "\nCapping Limit: " + str_caplimit + "\n" + poppay_details);
                    et_installmentNum.setText((paidCount + 1) + "");
                    et_pay_amount.setText(my_Plan);
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(AdminUpdatePayments.this, "Something went wrong.", Toast.LENGTH_SHORT).show();
                dialog.dismiss();
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> map = new HashMap<>();
                map.put("id", et_uid.getText().toString());
                return map;
            }
        };
        Volley.newRequestQueue(AdminUpdatePayments.this).add(stringRequest);
    }

    public void insertNewPayment(View view) {
        btnUpdatePayment.setEnabled(false);
        if (et_uid.getText().toString().trim().length() != 11) {
            et_uid.setError("enter valid user id.");
            et_uid.requestFocus();
            btnUpdatePayment.setEnabled(true);
            return;
        } else if (et_installmentNum.getText().toString().trim().length() < 1) {
            et_installmentNum.setError("insert installment number.");
            et_installmentNum.requestFocus();
            btnUpdatePayment.setEnabled(true);
            return;
        } else if ((et_pay_date.getText().toString().trim().length() < 8) || (et_pay_date.getText().toString().trim().length() > 10)) {
            et_pay_date.setError("insert proper date.");
            et_pay_date.requestFocus();
            btnUpdatePayment.setEnabled(true);
            return;
        } else if (et_pay_amount.getText().toString().trim().length() < 4) {
            et_pay_amount.setError("insert valid amount.");
            et_pay_amount.requestFocus();
            btnUpdatePayment.setEnabled(true);
            return;
        } else {
            dialog.show();
            //////// send data to server to insert ////////
            StringRequest request = new StringRequest(Request.Method.POST, BaseUrl.updateUserDataByAdminApp,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            dialog.dismiss();
                            btnUpdatePayment.setEnabled(true);
                            if (response.contains("ins_success")) {
                                AlertDialog.Builder aleBuilder = new AlertDialog.Builder(AdminUpdatePayments.this);
                                aleBuilder.setTitle("KTC Life");
                                aleBuilder.setMessage("Payment inserted success.");
                                aleBuilder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int i) {
                                        et_installmentNum.setText("");
                                        et_pay_date.setText("");
                                        et_pay_amount.setText("");
                                        et_pay_id.setText("");
                                        et_payMode.setText("");
                                    }
                                });
                                aleBuilder.setCancelable(false);
                                aleBuilder.show();
                            } else if (response.contains("ins_fail")) {
                                AlertDialog.Builder aleBuilder = new AlertDialog.Builder(AdminUpdatePayments.this);
                                aleBuilder.setTitle("KTC Life");
                                aleBuilder.setMessage("Error. Pls Try Again.");
                                aleBuilder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int i) {
                                        et_installmentNum.setText("");
                                        et_pay_date.setText("");
                                        et_pay_amount.setText("");
                                        et_pay_id.setText("");
                                        et_payMode.setText("");
                                    }
                                });
                                aleBuilder.setCancelable(false);
                                aleBuilder.show();
                                Toast.makeText(AdminUpdatePayments.this, "Error.... Try Again later.", Toast.LENGTH_SHORT).show();
                            } else if (response.contains("db_con_error")) {
                                Toast.makeText(AdminUpdatePayments.this, "Database connection error... Please try again later.", Toast.LENGTH_SHORT).show();
                            }
                        }
                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            dialog.dismiss();
                            btnUpdatePayment.setEnabled(true);
                            Toast.makeText(AdminUpdatePayments.this, "Error.", Toast.LENGTH_SHORT).show();
                        }
                    }) {
                @Override
                protected Map<String, String> getParams() throws AuthFailureError {
                    Map map = new HashMap();
                    map.put("uid", et_uid.getText().toString());
                    map.put("inment_num", et_installmentNum.getText().toString());
                    map.put("amt", et_pay_amount.getText().toString());
                    map.put("date", et_pay_date.getText().toString());
                    map.put("mode", et_payMode.getText().toString());
                    map.put("p_id", et_pay_id.getText().toString());
                    map.put("req_reson", "ins_payment");
                    return map;
                }
            };
            RetryPolicy mRetryPolicy = new DefaultRetryPolicy(
                    22000,
                    0,
                    DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
            request.setRetryPolicy(mRetryPolicy);
            Volley.newRequestQueue(AdminUpdatePayments.this).add(request);
        }
    }

    public void updateCappingLimit(View view) {
        //////Set up POPUP
        final AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(AdminUpdatePayments.this);
        final AlertDialog alert = alertDialogBuilder.create();
        final LayoutInflater layoutInflater = LayoutInflater.from(AdminUpdatePayments.this);
        final View popupInputDialogView = layoutInflater.inflate(R.layout.layout_adminupdate_caplimit, null);
        alert.setView(popupInputDialogView);
        alert.show();

        final TextView tv_popup_leginfo = popupInputDialogView.findViewById(R.id.textView_uInfo8888);
        final EditText et_popup_newCapLimit = popupInputDialogView.findViewById(R.id.edittextCaplimit8888);
        final Button btn_popup_Update = popupInputDialogView.findViewById(R.id.buttonUpdate8888);

        tv_popup_leginfo.setText("ID: " + et_uid.getText().toString() + "\nName: " + username + "\n\nPlan: " + my_Plan + "\t\t\tIs Paid:   " + isPaid +
                "\n\nNumber of Legs: " + tv_number_legs + "\nCapping Limit: " + str_caplimit);
        btn_popup_Update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (et_popup_newCapLimit.getText().toString().trim().length() == 0) {
                    et_popup_newCapLimit.setError("insert Cap limit");
                    et_popup_newCapLimit.requestFocus();
                    return;
                }

                Dialog dialog = new Dialog(AdminUpdatePayments.this);
                dialog.setContentView(R.layout.layout_progress_dialog);
                if (dialog.getWindow() != null) {
                    dialog.getWindow().setBackgroundDrawable(new ColorDrawable(0));
                }
                dialog.setCancelable(false);

                StringRequest request_toUpdateCaplmit = new StringRequest(Request.Method.POST, BaseUrl.updateUserDataByAdminApp,
                        new Response.Listener<String>() {
                            @Override
                            public void onResponse(String response) {
                                dialog.dismiss();
                                if (response.contains("cap_upd_success")) {
                                    AlertDialog.Builder aleBuilder = new AlertDialog.Builder(AdminUpdatePayments.this);
                                    aleBuilder.setTitle("KTC Life");
                                    aleBuilder.setMessage("Capping Limit Updated Success.");
                                    aleBuilder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialogInterface, int i) {
                                            alert.dismiss();
                                        }
                                    });
                                    aleBuilder.setCancelable(false);
                                    aleBuilder.show();
                                } else if (response.contains("error_cap_updt")) {
                                    AlertDialog.Builder aleBuilder = new AlertDialog.Builder(AdminUpdatePayments.this);
                                    aleBuilder.setTitle("KTC Life");
                                    aleBuilder.setMessage("Error. Please Try Again.");
                                    aleBuilder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialogInterface, int i) {
                                        }
                                    });
                                    aleBuilder.setCancelable(false);
                                    aleBuilder.show();
                                    Toast.makeText(AdminUpdatePayments.this, "Error... Try again later.", Toast.LENGTH_SHORT).show();
                                } else if (response.contains("db_con_error")) {
                                    Toast.makeText(AdminUpdatePayments.this, "Database connection error... Please try again later.", Toast.LENGTH_SHORT).show();
                                }
                            }
                        },
                        new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {
                                dialog.dismiss();
                                Toast.makeText(AdminUpdatePayments.this, "Server error.", Toast.LENGTH_SHORT).show();
                            }
                        }) {
                    @Override
                    protected Map<String, String> getParams() throws AuthFailureError {
                        Map map = new HashMap();
                        map.put("uid", et_uid.getText().toString());
                        map.put("new_capLim", et_popup_newCapLimit.getText().toString());
                        map.put("req_reson", "upd_cap");
                        return map;
                    }
                };
                RetryPolicy mRetryPolicy = new DefaultRetryPolicy(
                        22000,
                        0,
                        DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
                request_toUpdateCaplmit.setRetryPolicy(mRetryPolicy);
                Volley.newRequestQueue(AdminUpdatePayments.this).add(request_toUpdateCaplmit);
            }
        });
    }

    public void methodAddBonus(View view) {
        getUpLineData();

        AlertDialog.Builder alertDialog = new AlertDialog.Builder(AdminUpdatePayments.this);
        final View customLayout = getLayoutInflater().inflate(R.layout.layout_addbonus, null);
        alertDialog.setView(customLayout);
        et_userid = customLayout.findViewById(R.id.et_userid);
        et_entryDate = customLayout.findViewById(R.id.et_entryDate);
        et_amt = customLayout.findViewById(R.id.et_amount);
        bonusSpinner = customLayout.findViewById(R.id.spinner_bonus);
        btnSubmitBonus = customLayout.findViewById(R.id.btn_submitAddBonus);
        listViewOfUpLines = customLayout.findViewById(R.id.listViewTopLines9292);
        ImageButton ib_dateBonusChooser = customLayout.findViewById(R.id.btn_datepicker_updUserBonus);

        ///// bonus-spinner start //////
        Adapter one_qty_unit = new ArrayAdapter<String>(AdminUpdatePayments.this, android.R.layout.simple_dropdown_item_1line, bonusTypes);
        bonusSpinner.setAdapter((SpinnerAdapter) one_qty_unit);
        bonusSpinner.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
            }
        });
        ///// bonus-spinner end ///
        et_userid.setText(useID);
        ib_dateBonusChooser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Calendar calendar = Calendar.getInstance();
                DatePickerDialog datePickerDialog = new DatePickerDialog(AdminUpdatePayments.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker datePicker, int y, int m, int d) {
                        et_entryDate.setText(y + "-" + (m + 1) + "-" + d);
                    }
                }, calendar.get(Calendar.YEAR), (calendar.get(Calendar.MONTH)), calendar.get(Calendar.DATE));
                datePickerDialog.show();
            }
        });
        btnSubmitBonus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.show();
                bonusEntryDate = et_entryDate.getText().toString();
                if (bonusEntryDate.isEmpty()) {
                    et_entryDate.setError("Please select or enter a date.");
                    et_entryDate.requestFocus();
                    dialog.dismiss();
                } else {
                    userBonusSentToDB(useID, bonusEntryDate);
                }
            }
        });

        alertDialog.setPositiveButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        AlertDialog alert = alertDialog.create();
        alert.setCanceledOnTouchOutside(false);
        alert.show();
    }

    private void getUpLineData() {
        listOfUpline = new ArrayList<String>();
        listUpline = new ArrayList<>();
        Toast.makeText(this, "Geting upline data", Toast.LENGTH_SHORT).show();

        Dialog dialog = new Dialog(AdminUpdatePayments.this);
        dialog.setContentView(R.layout.layout_progress_dialog);
        if (dialog.getWindow() != null) {
            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(0));
        }
        dialog.setCancelable(false);
        dialog.show();

        StringRequest stringRequestToGetUplinedata = new StringRequest(Request.Method.POST, BaseUrl.getUplineData,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        dialog.dismiss();
                        listOfUpline.clear();
                        listUpline.clear();
                        if (response.contains("#*") && response.contains("#$")) {
                            String response_x = response.substring(response.indexOf("#*s*#") + 5, response.indexOf("#*e1*#"));
                            String arr[] = response_x.split("\\#\\*");
                            listOfUpline.clear();
                            listUpline.clear();

                            if (response.contains("#rf$")) {
                                String dir_referer = response.substring(response.indexOf("#rf$") + 4, response.indexOf("#rf@"));
                                listOfUpline.add(dir_referer);
                                listUpline.add(new UplineModel(dir_referer, "", ""));
                            }

                            for (int i = 0; i < arr.length; i++) {
                                String uid = arr[i].substring(arr[i].indexOf("#$") + 2, arr[i].indexOf("#@"));
                                String unm = arr[i].substring(arr[i].indexOf("#@") + 2, arr[i].indexOf("#!"));

                                if (response.contains("silver")) {
                                    int legs = Integer.parseInt(arr[i].substring(arr[i].indexOf("silver") + 6, arr[i].indexOf("gold")));
                                    if (legs >= 8) {
                                        levelColor = "silver";
                                        int silverLegs = Integer.parseInt(arr[i].substring(arr[i].indexOf("gold") + 4, arr[i].indexOf("ruby")));
                                        if (silverLegs >= 8) {
                                            levelColor = "gold";
                                            int goldLegs = Integer.parseInt(arr[i].substring(arr[i].indexOf("ruby") + 4, arr[i].indexOf("diamond")));
                                            if (goldLegs >= 8) {
                                                levelColor = "ruby";
                                                int rubyLegs = Integer.parseInt(arr[i].substring(arr[i].indexOf("diamond") + 7,
                                                        arr[i].indexOf("end_level_count")));
                                                if (rubyLegs >= 8) {
                                                    levelColor = "diamond";
                                                }
                                            }
                                        }
                                    } else {
                                        levelColor = "No Level";
                                    }
                                }

                                if (listOfUpline.size() > 0 && uid.contains(listOfUpline.get(0))) {
                                    listOfUpline.set(0, listOfUpline.get(0) + "(" + unm + ")-referer");
                                } else {
                                    listOfUpline.add(uid + "(" + unm + ")");
                                }
                                /////////////////////////////////////////////////

                                if (listUpline.size() > 0 && uid.contains(listUpline.get(0).getId())) {
                                    /*String id = String.valueOf(listUpline.get(0).getId());
                                    String name = "(" + unm + ")-referer";
                                    listUpline.set(0, new UplineModel(id, name, levelColor));*/
                                } else {
                                    String name = "(" + unm + ")";

                                    if (i==0){
                                        listUpline.add(new UplineModel(uid, name, levelColor));
                                    } else if (i==1){
                                        if ((levelColor.contains("silver"))) {
                                            listUpline.add(new UplineModel(uid, name, levelColor));
                                        }
                                    } else if (i==2){
                                        if ((levelColor.contains("gold"))) {
                                            listUpline.add(new UplineModel(uid, name, levelColor));
                                        }
                                    } else if (i==3){
                                        if ((levelColor.contains("ruby"))) {
                                            listUpline.add(new UplineModel(uid, name, levelColor));
                                        }
                                    } else if (i >= 4){
                                        if ((levelColor.contains("diamond"))) {
                                            listUpline.add(new UplineModel(uid, name, levelColor));
                                        }
                                    }
                                }
                                String id = String.valueOf(listUpline.get(0).getId());
                                String ref_name = "(" + String.valueOf(listUpline.get(0).getName()) + ")-referer";
                                listUpline.set(0, new UplineModel(id, ref_name, levelColor));
                                ref_name = "";
                                levelColor = "";
                            }
                            UplineListAdapter adapter1 = new UplineListAdapter(AdminUpdatePayments.this, listUpline);

                            listViewOfUpLines.setAdapter(adapter1);
                            listViewOfUpLines.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                                @Override
                                public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                                    AlertDialog.Builder alertDialogLeg = new AlertDialog.Builder(AdminUpdatePayments.this);
                                    final View customLayoutLeg = getLayoutInflater().inflate(R.layout.layout_add_leguplinebonus, null);
                                    alertDialogLeg.setView(customLayoutLeg);
                                    alertDialogLeg.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            dialog.dismiss();
                                        }
                                    });
                                    alertDialogLeg.setNeutralButton("Cancel", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            dialog.dismiss();
                                        }
                                    });
                                    alertDialogLeg.show();
                                    EditText et_LegID = customLayoutLeg.findViewById(R.id.et_legid87);
                                    EditText et_Amt = customLayoutLeg.findViewById(R.id.et_amountLeg87);
                                    Spinner spinnerBonusType = customLayoutLeg.findViewById(R.id.spinner_bonusLeg87);
                                    Button button_Add = customLayoutLeg.findViewById(R.id.btn_submitAddBonusLeg87);
                                    et_LegID.setText(listUpline.get(position).id);
                                    ArrayAdapter adapterSpinner = new ArrayAdapter<String>(AdminUpdatePayments.this, android.R.layout.simple_spinner_dropdown_item, bonusTypes);
                                    spinnerBonusType.setAdapter(adapterSpinner);

                                    button_Add.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {
                                            String str_amt = et_Amt.getText().toString();
                                            String str_btype = spinnerBonusType.getSelectedItem().toString();

                                            if (str_amt.trim().length() == 0) {
                                                Toast.makeText(AdminUpdatePayments.this, "Enter Amount", Toast.LENGTH_SHORT).show();
                                                return;
                                            }
                                            if (str_btype.equals("-- Select bonus type --")) {
                                                Toast.makeText(AdminUpdatePayments.this, "select bonus type.", Toast.LENGTH_SHORT).show();
                                                return;
                                            }
                                            String strOnList = listOfUpline.get(position);
                                            if (strOnList.contains("#_") && strOnList.contains("_#")) {
                                                if (strOnList.contains("-referer")) {
                                                    strOnList = strOnList.substring(0, (strOnList.indexOf(")-referer") + 9));
                                                } else {
                                                    strOnList = strOnList.substring(0, (strOnList.indexOf(")") + 1));
                                                }
                                            }
                                            listOfUpline.set(position, strOnList + "\n#_" + str_amt + "_#" + str_btype);
                                            listViewOfUpLines.invalidateViews();
                                            button_Add.setEnabled(false);
                                        }
                                    });
                                }
                            });
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(AdminUpdatePayments.this, "ERROR GETTING UPLINES.", Toast.LENGTH_SHORT).show();
                        dialog.dismiss();
                    }
                }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                HashMap map = new HashMap();
                map.put("uid", et_uid.getText().toString());
                return map;
            }
        };
        Volley.newRequestQueue(AdminUpdatePayments.this).add(stringRequestToGetUplinedata);
    }

    public void userBonusSentToDB(final String userid, final String entryDate) {
        StringRequest request_toUpdateBonus = new StringRequest(Request.Method.POST, BaseUrl.setAllUplineBonusAfterPayment,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        dialog.dismiss();
                        if (!response.contains("fail")) {
                            AlertDialog.Builder aleBuilder = new AlertDialog.Builder(AdminUpdatePayments.this);
                            aleBuilder.setTitle("KTC Life");
                            aleBuilder.setIcon(R.drawable.app_logo);
                            aleBuilder.setMessage("Bonus added Success.");
                            aleBuilder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    et_entryDate.setText("");
                                    et_amt.setText("");
                                    BaseUrl.payoutReportModel.paid_bonus = "yes";
                                    onBackPressed();
                                }
                            });
                            aleBuilder.setCancelable(false);
                            aleBuilder.show();
                        } else if (response.contains("fail")) {
                            AlertDialog.Builder aleBuilder = new AlertDialog.Builder(AdminUpdatePayments.this);
                            aleBuilder.setTitle("KTC Life");
                            aleBuilder.setIcon(R.drawable.app_logo);
                            aleBuilder.setMessage("Error while add bonus to some or all users. ");
                            aleBuilder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {

                                }
                            });
                            aleBuilder.setCancelable(false);
                            aleBuilder.show();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(AdminUpdatePayments.this, "Server error.", Toast.LENGTH_SHORT).show();
                        dialog.dismiss();
                    }
                }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map map = new HashMap();
                map.put("uid", userid);
                map.put("uname", username);
                map.put("inst_num", installment_num);
                map.put("date", entryDate);
                map.put("uplinebonus", convertList(listOfUpline));
                map.put("req_reson", "Allupline_bonus");
                return map;
            }

            private String convertList(List<String> listOfUpline) {
                StringBuilder str = new StringBuilder();
                for (String s : listOfUpline) {
                    String id = s.substring(0, 11);
                    String amt = s.substring(s.indexOf("#_") + 2, s.indexOf("_#"));
                    String b_type = s.substring(s.indexOf("_#") + 2);
                    String f = "#1A#" + id + "#2A#" + amt + "#3A#" + b_type + "#4A#,";
                    str.append(f);
                }
                return str.toString();
            }
        };
        RetryPolicy mRetryPolicy = new DefaultRetryPolicy(
                22000,
                0,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
        request_toUpdateBonus.setRetryPolicy(mRetryPolicy);
        Volley.newRequestQueue(AdminUpdatePayments.this).add(request_toUpdateBonus);
    }

    public void cashWithdrawlEntry(View view) {
        AlertDialog.Builder alertDialog7 = new AlertDialog.Builder(AdminUpdatePayments.this);
        final View customLayout7 = getLayoutInflater().inflate(R.layout.layout_admin_update_cashpayment_frombonus, null);
        alertDialog7.setView(customLayout7);
        alertDialog7.setNegativeButton("Cancel", null);
        alertDialog7.setCancelable(false);
        alertDialog7.show();
        EditText et_uInfo = customLayout7.findViewById(R.id.et_userid_admin_bonusCashDeduct);
        EditText et_dateOfEntry = customLayout7.findViewById(R.id.et_date_admin_bonusCashDeduct);
        EditText et_AmountToEntry = customLayout7.findViewById(R.id.et_amount_admin_bonusCashDeduct);

        listViewBonusDetails = customLayout7.findViewById(R.id.listView_allBonusDetails);
        getBonusDetails();
        Button btn_submitCashWithdrawlEntry = customLayout7.findViewById(R.id.btn_submitCashBonusWithdrawl);

        et_uInfo.setText("ID: " + useID + "\nName: " + username + "\nBonus available: " + walletBal);
        Calendar calendar = Calendar.getInstance();
        et_dateOfEntry.setText(calendar.get(Calendar.YEAR) + "-" + (calendar.get(Calendar.MONTH) + 1) + "-" + calendar.get(Calendar.DATE));

        btn_submitCashWithdrawlEntry.setOnClickListener(v -> {
                    final double d_amt;
                    try {
                        d_amt = Double.parseDouble(et_AmountToEntry.getText().toString().trim());
                    } catch (NumberFormatException ex) {
                        et_AmountToEntry.setError("Enter proper amount");
                        et_AmountToEntry.requestFocus();
                        return;
                    }
                    if (et_uInfo.getText().toString().trim().length() < 20) {
                        et_uInfo.setError("Info not available.");
                        et_uInfo.requestFocus();
                        return;
                    } else if (et_dateOfEntry.getText().toString().trim().length() < 8) {
                        et_dateOfEntry.setError("date is not proper.");
                        et_dateOfEntry.requestFocus();
                        return;
                    } else if (et_AmountToEntry.getText().toString().trim().length() == 0) {
                        et_AmountToEntry.setError("Enter amount");
                        et_AmountToEntry.requestFocus();
                        return;
                    } else if (d_amt > Double.parseDouble(walletBal)) {
                        et_AmountToEntry.setError("Amount is exceeding available balence.");
                        et_AmountToEntry.requestFocus();
                        Toast.makeText(this, "Amount exceeding available balance.", Toast.LENGTH_SHORT).show();
                    } else {
                        dialog.show();
                        StringRequest stringRequestToEnterCashWithdrawl = new StringRequest(Request.Method.POST, BaseUrl.updateUserDataByAdminApp,
                                response -> {
                                    dialog.dismiss();
                                    if (response.contains("#6transIns_cash_done7#")) {
                                        AlertDialog.Builder alertDialog3 = new AlertDialog.Builder(AdminUpdatePayments.this);
                                        alertDialog3.setTitle("Success.");
                                        alertDialog3.setIcon(R.drawable.app_logo);
                                        alertDialog3.setMessage("Cash withdrawl entry success for ID: " + useID + "(" + username + ") for amount: " + et_AmountToEntry.getText());
                                        alertDialog3.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialogInterface, int i) {
                                                onBackPressed();
                                            }
                                        });
                                        alertDialog3.setCancelable(false);
                                        alertDialog3.show();
                                    }
                                },
                                error -> {
                                    Toast.makeText(this, "Error: " + error.getMessage(), Toast.LENGTH_SHORT).show();
                                }) {
                            @Override
                            protected Map<String, String> getParams() throws AuthFailureError {
                                Map<String, String> map = new HashMap<String, String>();
                                map.put("userId", useID);
                                map.put("amtWit", et_AmountToEntry.getText().toString());
                                map.put("witDate", et_dateOfEntry.getText().toString());
                                map.put("req_reson", "cash_wit_entry");
                                return map;
                            }
                        };
                        RetryPolicy retryPolicy = new DefaultRetryPolicy(22000, 0, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
                        stringRequestToEnterCashWithdrawl.setRetryPolicy(retryPolicy);
                        Volley.newRequestQueue(AdminUpdatePayments.this).add(stringRequestToEnterCashWithdrawl);
                    }
                }
        );
    }

    private void getBonusDetails() {
        dialog.show();
        StringRequest requestTogetBonuses = new StringRequest(Request.Method.POST, BaseUrl.fetchBonusUrl,
                response -> {
                    dialog.dismiss();
                    if (response.contains("#1a#") && response.contains("#FFF#")) {
                        String data = response.substring(response.indexOf("#b_data#") + 8, response.indexOf("#FFF#"));
                        String arr_bonuses[] = data.split("#ebd#");
                        listBonus.clear();
                        for (String str : arr_bonuses) {
                            String dir_ref_bonus = str.substring(str.indexOf("#1a#") + 4, str.indexOf("#2a#"));
                            String sec_ref_bonus = str.substring(str.indexOf("#2a#") + 4, str.indexOf("#3a#"));
                            String direct_renewal_bonus = str.substring(str.indexOf("#3a#") + 4, str.indexOf("#4a#"));
                            String target_bonus = str.substring(str.indexOf("#4a#") + 4, str.indexOf("#5a#"));
                            String level_bonus = str.substring(str.indexOf("#5a#") + 4, str.indexOf("#6a#"));
                            String income_date = str.substring(str.indexOf("#6a#") + 4, str.indexOf("#7a#"));

                            if (!dir_ref_bonus.equals("0")) {
                                listBonus.add(" Amt: " + dir_ref_bonus + "\tDirRefBonus\tDate:" + income_date);
                            }
                            if (!sec_ref_bonus.equals("0")) {
                                listBonus.add(" Amt: " + sec_ref_bonus + "\tSecRefBonus\tDate:" + income_date);
                            }
                            if (!direct_renewal_bonus.equals("0")) {
                                listBonus.add(" Amt: " + direct_renewal_bonus + "\tDirRenewalBonus\tDate:" + income_date);
                            }
                            if (!target_bonus.equals("0")) {
                                listBonus.add(" Amt: " + target_bonus + "\tTargetBonus\tDate:" + income_date);
                            }
                            if (!level_bonus.equals("0")) {
                                listBonus.add(" Amt: " + level_bonus + "\tLevelBonus\tOn:" + income_date);
                            }
                        }
                        adapterForBonus = new ArrayAdapter<String>(AdminUpdatePayments.this, android.R.layout.simple_list_item_1, listBonus);
                        listViewBonusDetails.setAdapter(adapterForBonus);
                    } else if (response.contains("no_bns_avle")) {
                        booleanFlagBonusFetched = true;
                        Toast.makeText(this, "No Bonus Available.", Toast.LENGTH_SHORT).show();
                    } else if (response.contains("dbconnerror")) {
                        booleanFlagBonusFetched = true;
                        Toast.makeText(this, "Database Connection Error. Please try again.", Toast.LENGTH_SHORT).show();
                    }
                },
                error -> {
                    dialog.dismiss();
                    booleanFlagBonusFetched = true;
                    Toast.makeText(AdminUpdatePayments.this, "Error" + error.getMessage(), Toast.LENGTH_SHORT).show();
                }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map map = new HashMap();
                map.put("id", useID);
                return map;
            }
        };
        Volley.newRequestQueue(AdminUpdatePayments.this).add(requestTogetBonuses);
    }

    public void getTransactionAll7020(View view) {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(AdminUpdatePayments.this);
        final View customLayout = getLayoutInflater().inflate(R.layout.layout_show_transactions_ofusers_foradmin, null);
        alertDialog.setView(customLayout);
        listView_ofPayments = customLayout.findViewById(R.id.listview_wallet_history_ofUsers_forAdmin);
        tv_noTransaction = customLayout.findViewById(R.id.tv_no_history_ofUsers_forAdmin);
        methodGetPaymentTransactions();
        alertDialog.show();
    }

    public void methodGetPaymentTransactions() {
        dialog.show();
        list_wallet_history = new ArrayList<>();
        list_wallet_history.clear();

        StringRequest stringRequest = new StringRequest(Request.Method.POST, BaseUrl.fetchWalletHistoryUrl, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                if (response.contains("conn_error")) {
                    dialog.dismiss();
                    Toast.makeText(AdminUpdatePayments.this, "Server error. Try Again later.", Toast.LENGTH_SHORT).show();
                }

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
                        if (!t_amount.equals("")) {
                            list_wallet_history.add(walletHistoryModel);
                        }
                    }
                    ///////Bonus history//////
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
                        String to_id = useID;
                        String t_amount = b_amt;
                        String t_time = income_date;
                        String t_desc = description;
                        String from_name = "";
                        String to_name = username;
                        WalletHistoryModel walletHistoryModel = new WalletHistoryModel(from_id, to_id, to_name, t_time, t_amount, t_desc, from_name);
                        if (!t_amount.equals("")) {
                            list_wallet_history.add(walletHistoryModel);
                        }
                    }
                    ////////////////
                    Collections.sort(list_wallet_history, new Comparator<WalletHistoryModel>() {
                        @Override
                        public int compare(WalletHistoryModel o1, WalletHistoryModel o2) {
                            return o2.date.compareTo(o1.date);
                        }
                    });

                    walletHistoryListAdaptor = new WalletHistoryListAdaptor(AdminUpdatePayments.this, list_wallet_history);
                    walletHistoryListAdaptor.isForAdmin_to_seeUsersInfo = true;
                    walletHistoryListAdaptor.user_id = useID;
                    listView_ofPayments.setAdapter((ListAdapter) walletHistoryListAdaptor);
                    dialog.dismiss();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(AdminUpdatePayments.this, "Server error", Toast.LENGTH_SHORT).show();
                dialog.dismiss();
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> map = new HashMap<>();
                map.put("id", useID);
                return map;
            }
        };
        RetryPolicy mRetryPolicy = new DefaultRetryPolicy(
                22000,
                //DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                0,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
        stringRequest.setRetryPolicy(mRetryPolicy);
        Volley.newRequestQueue(AdminUpdatePayments.this).add(stringRequest);
    }

    public void makeWithdrawlAndResetAccount(View view) {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(AdminUpdatePayments.this);
        View layoutView = getLayoutInflater().inflate(R.layout.layout_to_reset_account, null);

        interestCalulate();
        TextView tv_paymentsInfoOnDialog53829 = layoutView.findViewById(R.id.tv_paymentsInfoOnDialog53829);
        tv_paymentsInfoOnDialog53829.setText(poppay_details + "\n of ID: " + useID);
        alertDialog.setTitle("Reset Account");
        alertDialog.setIcon(R.drawable.app_logo);
        alertDialog.setView(layoutView);
        alertDialog.setNegativeButton("Cancel", null);
        alertDialog.setPositiveButton("Reset", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog1, int which) {
                dialog.show();
                wd_current_date = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault()).format(new Date());
                StringRequest request = new StringRequest(Request.Method.POST, BaseUrl.ktc_mk_AcReset,
                        new Response.Listener<String>() {
                            @Override
                            public void onResponse(String response) {
                                dialog.dismiss();
                                if (response.contains("data_updation_done")) {
                                    AlertDialog.Builder alertDialog87 = new AlertDialog.Builder(AdminUpdatePayments.this);
                                    alertDialog87.setIcon(R.drawable.app_logo);
                                    alertDialog87.setTitle("Success");
                                    alertDialog87.setMessage("The ID: " + useID + " reseted...");
                                    alertDialog87.setCancelable(false);
                                    alertDialog87.setPositiveButton("ok", null);
                                    alertDialog87.show();
                                } else {
                                    Toast.makeText(AdminUpdatePayments.this, "Error. Try again later.", Toast.LENGTH_SHORT).show();
                                }
                            }
                        },
                        new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {
                                dialog.dismiss();
                                Toast.makeText(AdminUpdatePayments.this, "Error.", Toast.LENGTH_SHORT).show();
                            }
                        }) {
                    @Override
                    protected Map<String, String> getParams() throws AuthFailureError {
                        HashMap map = new HashMap();
                        map.put("idtoresetac", useID);
                        map.put("date", wd_current_date);
                        map.put("int_amt", int_amt);
                        return map;
                    }
                };
                RetryPolicy mRetryPolicy = new DefaultRetryPolicy(
                        20000,
                        0,
                        DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
                request.setRetryPolicy(mRetryPolicy);
                Volley.newRequestQueue(AdminUpdatePayments.this).add(request);
            }
        });
        alertDialog.show();
    }

    public void interestCalulate() {
        switch (Integer.parseInt(plan_amt)) {
            case 1000:
                switch (month_no) {
                    case 6:
                        int_amt = 1000;
                        break;

                    case 12:
                        int_amt = 3000;
                        break;

                    case 18:
                        int_amt = 4000;
                        break;

                    case 24:
                        int_amt = 7000;
                        break;
                    default:
                        int_amt = 0;
                        break;
                }
                break;
            //////////////////////////////////////////
            case 2000:
                switch (month_no) {
                    case 6:
                        int_amt = 2000;
                        break;

                    case 12:
                        int_amt = 6000;
                        break;

                    case 18:
                        int_amt = 8000;
                        break;

                    case 24:
                        int_amt = 14000;
                        break;
                    default:
                        int_amt = 0;
                        break;
                }
                break;
            //////////////////////////
            case 3000:
                switch (month_no) {
                    case 6:
                        int_amt = 3000;
                        break;

                    case 12:
                        int_amt = 9000;
                        break;

                    case 18:
                        int_amt = 12000;
                        break;

                    case 24:
                        int_amt = 21000;
                        break;
                    default:
                        int_amt = 0;
                        break;
                }
                break;
            /////////////////////////////////////////////
            case 4000:
                switch (month_no) {
                    case 6:
                        int_amt = 4000;
                        break;

                    case 12:
                        int_amt = 12000;
                        break;

                    case 18:
                        int_amt = 16000;
                        break;

                    case 24:
                        int_amt = 28000;
                        break;
                    default:
                        int_amt = 0;
                        break;
                }
                break;/////////////////////////////////////
            case 5000:
                switch (month_no) {
                    case 6:
                        int_amt = 5000;
                        break;

                    case 12:
                        int_amt = 15000;
                        break;

                    case 18:
                        int_amt = 20000;
                        break;

                    case 24:
                        int_amt = 35000;
                        break;
                    default:
                        int_amt = 0;
                        break;
                }
                break;
            ////////////////////////////////////////
            case 10000:
                switch (month_no) {
                    case 6:
                        int_amt = 10000;
                        break;

                    case 12:
                        int_amt = 30000;
                        break;

                    case 18:
                        int_amt = 40000;
                        break;

                    case 24:
                        int_amt = 70000;
                        break;
                    default:
                        int_amt = 0;
                        break;
                }
                break;
            /////////////////////////////////////
            case 15000:
                switch (month_no) {
                    case 6:
                        int_amt = 15000;
                        break;

                    case 12:
                        int_amt = 45000;
                        break;

                    case 18:
                        int_amt = 75000;
                        break;

                    case 24:
                        int_amt = 105000;
                        break;
                    default:
                        int_amt = 0;
                        break;
                }
                break;
            ////////////////////////////////////////
            case 20000:
                switch (month_no) {
                    case 6:
                        int_amt = 20000;
                        break;

                    case 12:
                        int_amt = 60000;
                        break;

                    case 18:
                        int_amt = 80000;
                        break;

                    case 24:
                        int_amt = 140000;
                        break;
                    default:
                        int_amt = 0;
                        break;
                }
                break;
        }
    }

    public void planWithdrawlHistory(View view) {
        if (et_uid.getText().toString().length() != 11) {
            et_uid.setError("invalid id");
            et_uid.requestFocus();
            return;
        }
        Intent intent = new Intent(AdminUpdatePayments.this, PaymentHistoryReport.class);
        intent.putExtra("u_id", et_uid.getText().toString());
        startActivity(intent);
    }

    /*
    private void methodGetPaymentTransactions() {

        progressDialog.show();
        StringRequest stringRequest = new StringRequest(Request.Method.POST, BaseUrl.ktcBonusRequest, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                if (response.contains("conn_error")) {
                    progressDialog.dismiss();
                    Toast.makeText(AdminUpdatePayments.this, "Server error. Try Again", Toast.LENGTH_SHORT).show();
                }

                if (response.contains("bnus")) {
                    progressDialog.dismiss();
                    tv_totalBonus_ofUsers7302.setText("Wallet Total : " + response.substring(response.indexOf("#bnus#") + 6, response.indexOf("#eb#")));
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(AdminUpdatePayments.this, "Server error", Toast.LENGTH_SHORT).show();
                progressDialog.dismiss();
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> map = new HashMap<>();
                map.put("uid", legID);
                return map;
            }
        };
        RetryPolicy mRetryPolicy = new DefaultRetryPolicy(
                20000,
                //DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                1,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
        stringRequest.setRetryPolicy(mRetryPolicy);
        Volley.newRequestQueue(AdminUpdatePayments.this).add(stringRequest);
    }
     */
}