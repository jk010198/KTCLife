package com.ithublive.ktclife;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.Adapter;
import android.widget.ArrayAdapter;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.SpinnerAdapter;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;

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
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.ithublive.ktclife.Admin.PendingUsersKycDetails;
import com.ithublive.ktclife.Admin.Reports;
import com.ithublive.ktclife.Utils.BaseUrl;
import com.ortiz.touchview.TouchImageView;
import com.squareup.picasso.Picasso;

import java.time.format.DateTimeFormatter;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import de.hdodenhof.circleimageview.CircleImageView;

import static com.ithublive.ktclife.R.drawable.level_diamond_profile;
import static com.ithublive.ktclife.R.drawable.level_gold;
import static com.ithublive.ktclife.R.drawable.level_ruby_profile;
import static com.ithublive.ktclife.R.drawable.level_silver;

public class HomeProfileActivity extends AppCompatActivity {

    ImageView iv_admin_payment_btn;
    CircleImageView iv_history_btn;
    TableRow tableRow;
    Spinner planSpinner;
    String last_pay_date = "";
    TextView tvBottomNotice;
    String retriveUserData, isPaid, join_date, my_Plan, levelColor;
    TextView tv_name, tv_userid, tv_ref_id_name, tv_plan, tv_join_date, tv_number_legs, tv_level;
    CircleImageView homepage_profile_image, homepage_profile_imageBG;
    Dialog dialog;
    ImageView iv_pay_1, iv_pay_2, iv_pay_3, iv_pay_4, iv_pay_5, iv_pay_6, iv_pay_7, iv_pay_8, iv_pay_9, iv_pay_10, iv_pay_11,
            iv_pay_12, iv_pay_13, iv_pay_14, iv_pay_15, iv_pay_16, iv_pay_17, iv_pay_18, iv_pay_19,
            iv_pay_20, iv_pay_21, iv_pay_22, iv_pay_23, iv_pay_24, iv_pay_25;
    TextView tv_plan_1, tv_plan_2, tv_plan_3, tv_plan_4, tv_plan_5, tv_plan_6, tv_plan_7, tv_plan_8, tv_plan_9, tv_plan_10, tv_plan_11,
            tv_plan_12, tv_plan_13, tv_plan_14, tv_plan_15, tv_plan_16, tv_plan_17, tv_plan_18, tv_plan_19,
            tv_plan_20, tv_plan_21, tv_plan_22, tv_plan_23, tv_plan_24, tv_plan_25;
    TextView tv_payDate_1, tv_payDate_2, tv_payDate_3, tv_payDate_4, tv_payDate_5, tv_payDate_6, tv_payDate_7, tv_payDate_8, tv_payDate_9, tv_payDate_10, tv_payDate_11,
            tv_payDate_12, tv_payDate_13, tv_payDate_14, tv_payDate_15, tv_payDate_16, tv_payDate_17, tv_payDate_18, tv_payDate_19,
            tv_payDate_20, tv_payDate_21, tv_payDate_22, tv_payDate_23, tv_payDate_24, tv_payDate_25;
    TextView tv_payCapLimYear_1, tv_payCapLimYear_2, tv_payCapLimYear_3, tv_payCapLimYear_4, tv_payCapLimYear_5, tv_payCapLimYear_6, tv_payCapLimYear_7, tv_payCapLimYear_8, tv_payCapLimYear_9, tv_payCapLimYear_10, tv_payCapLimYear_11,
            tv_payCapLimYear_12, tv_payCapLimYear_13, tv_payCapLimYear_14, tv_payCapLimYear_15, tv_payCapLimYear_16, tv_payCapLimYear_17, tv_payCapLimYear_18, tv_payCapLimYear_19,
            tv_payCapLimYear_20, tv_payCapLimYear_21, tv_payCapLimYear_22, tv_payCapLimYear_23, tv_payCapLimYear_24, tv_payCapLimYear_25;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_profile);

        SharedPreferences shared = getSharedPreferences("" + R.string.sp_retriveUserData, MODE_PRIVATE);
        retriveUserData = (shared.getString("data", ""));

        dialog = new Dialog(HomeProfileActivity.this);
        dialog.setContentView(R.layout.layout_progress_dialog);
        if (dialog.getWindow() != null) {
            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(0));
        }
        dialog.setCancelable(false);
        dialog.show();

        getReadySomeViews();

        iv_admin_payment_btn = findViewById(R.id.admin_payment_btn);
        iv_history_btn = findViewById(R.id.history_btn);

        tvBottomNotice = findViewById(R.id.tvBottomNotice);
        homepage_profile_image = findViewById(R.id.profile_img);
        homepage_profile_imageBG = findViewById(R.id.profile_imgBG);
        tv_name = findViewById(R.id.tv_homepage_username);
        tv_userid = findViewById(R.id.tv_homepage_userid);
        tv_ref_id_name = findViewById(R.id.tv_homepage_refid_name);
        tv_plan = findViewById(R.id.tv_homepage_plan);
        tv_join_date = findViewById(R.id.tv_homepage_joindate);
        tv_number_legs = findViewById(R.id.tv_homepage_numberlegs);
        tv_level = findViewById(R.id.tv_homepage_Level);

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
            my_Plan = retriveUserData.substring(retriveUserData.indexOf("&") + 1, retriveUserData.indexOf("*"));
            isPaid = retriveUserData.substring(retriveUserData.indexOf("#1A#") + 4, retriveUserData.indexOf("#2A#"));
            tv_name.setText("Name :- " + retriveUserData.substring(retriveUserData.indexOf("@") + 1, retriveUserData.indexOf("#")));
            tv_userid.setText("Id :- " + retriveUserData.substring(retriveUserData.indexOf("!") + 1, retriveUserData.indexOf("@")));
            tv_ref_id_name.setText("Refered by :- " + retriveUserData.substring(retriveUserData.indexOf("#26A#") + 5, retriveUserData.indexOf("#27A#")) + "("
                    + retriveUserData.substring(retriveUserData.indexOf("%") + 1, retriveUserData.indexOf("&")) + ")");
            tv_plan.setText("Plan :- " + retriveUserData.substring(retriveUserData.indexOf("&") + 1, retriveUserData.indexOf("*")) + "    Paid :- " + isPaid);
            join_date = retriveUserData.substring(retriveUserData.indexOf("#2A#") + 4, retriveUserData.indexOf("#3A#"));
            tv_join_date.setText("Joining date :- " + join_date.substring(0, 10));
            tv_number_legs.setText("Number of legs :- " + retriveUserData.substring(retriveUserData.indexOf("#3A#") + 4, retriveUserData.indexOf("#4A#")));

            if (retriveUserData.contains("silver")) {
                int legs = Integer.parseInt(retriveUserData.substring(retriveUserData.indexOf("silver") + 6, retriveUserData.indexOf("gold")));
                if (legs >= 8) {
                    levelColor = "silver";
                    homepage_profile_imageBG.setBackgroundResource(level_silver);
                    int silverLegs = Integer.parseInt(retriveUserData.substring(retriveUserData.indexOf("gold") + 4, retriveUserData.indexOf("ruby")));
                    if (silverLegs >= 8) {
                        levelColor = "gold";
                        homepage_profile_imageBG.setBackgroundResource(level_gold);
                        int goldLegs = Integer.parseInt(retriveUserData.substring(retriveUserData.indexOf("ruby") + 4, retriveUserData.indexOf("diamond")));
                        if (goldLegs >= 8) {
                            levelColor = "ruby";
                            homepage_profile_imageBG.setBackgroundResource(level_ruby_profile);
                            int rubyLegs = Integer.parseInt(retriveUserData.substring(retriveUserData.indexOf("diamond") + 7,
                                    retriveUserData.indexOf("end_level_count")));
                            if (rubyLegs >= 8) {
                                levelColor = "diamond";
                                homepage_profile_imageBG.setBackgroundResource(level_diamond_profile);
                            }
                        }
                    }
                } else {
                    levelColor = "No Level";
                }
            }
            tv_level.setText("Level :- " + levelColor);

            if (isPaid.contains("no")) {
                tv_name.setTextColor(Color.RED);
                tv_userid.setTextColor(Color.RED);
            } else {
                tv_name.setTextColor(Color.parseColor("#006400"));
                tv_userid.setTextColor(Color.parseColor("#006400"));
            }
            Glide.with(HomeProfileActivity.this)
                    .load(retriveUserData.substring(retriveUserData.indexOf("$") + 1, retriveUserData.indexOf("%")))
                    .into(homepage_profile_image);
            if (my_Plan.isEmpty() || my_Plan.equals("null")) {
                TableLayout tableLayout = findViewById(R.id.tablePayment24);
                tableLayout.setVisibility(View.GONE);
                showPlanSelectorPopup();
            }

            homepage_profile_image.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    LayoutInflater inflater = getLayoutInflater();
                    View popup = inflater.inflate(R.layout.popupimage, null);
                    final TouchImageView iv = popup.findViewById(R.id.imageview);

                    AlertDialog.Builder adb = new AlertDialog.Builder(HomeProfileActivity.this);
                    adb.setTitle("Profile Photo");
                    adb.setView(popup);
                    if (retriveUserData.substring(retriveUserData.indexOf("$") + 1, retriveUserData.indexOf("%")).isEmpty()) {
                        Picasso.with(HomeProfileActivity.this).load(R.drawable.ic_user_dummy).into(iv);
                    } else {
                        Picasso.with(HomeProfileActivity.this)
                                .load(retriveUserData.substring(retriveUserData.indexOf("$") + 1, retriveUserData.indexOf("%"))).into(iv);
                    }
                    AlertDialog dialog = adb.create();
                    dialog.show();
                }
            });

            //#pn#1#epn#
            if (retriveUserData.contains("#pn#")) {
                int paidCount = Integer.parseInt(retriveUserData.substring(retriveUserData.indexOf("#pn#") + 4, retriveUserData.indexOf("#epn#")));
                String payDetails = retriveUserData.substring(retriveUserData.indexOf("#epn#") + 5, retriveUserData.indexOf(":e_pay:"));
                String payDetails_array[] = payDetails.split(",");
                for (String data : payDetails_array) {
                    String p_date = data.substring(0, 10);
                    last_pay_date = p_date;
                    int month_no = Integer.parseInt(data.substring(data.indexOf("::") + 2, data.indexOf(":_:")));
                    String plan_amt = data.substring(data.indexOf(":_:") + 3, data.indexOf(":$:"));
                    designPaidNumbers(month_no, p_date, plan_amt);
                }
                tableRowEventGen(paidCount);
            }
            dialog.dismiss();
        }

        String userId = retriveUserData.substring(retriveUserData.indexOf("!") + 1, retriveUserData.indexOf("@"));
        if (userId.equals("95947639111")) {
            iv_admin_payment_btn.setVisibility(View.VISIBLE);
            iv_history_btn.setVisibility(View.VISIBLE);
            iv_admin_payment_btn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    startActivity(new Intent(HomeProfileActivity.this, AdminUpdatePayments.class));
                }
            });
        } else {
            iv_admin_payment_btn.setVisibility(View.GONE);
            iv_history_btn.setVisibility(View.VISIBLE);
        }

        iv_history_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(HomeProfileActivity.this, PaymentHistoryList.class);
                startActivity(intent);
            }
        });
    }

    private void tableRowEventGen(final int paidCount) {
        String su = "th";
        if ((paidCount + 1) == 1 || (paidCount + 1) == 21) {
            su = "st";
        } else if ((paidCount + 1) == 2 || (paidCount + 1) == 22) {
            su = "nd";
        } else if ((paidCount + 1) == 3 || (paidCount + 1) == 23) {
            su = "rd";
        }
        switch (paidCount) {
            case 0: {
                tableRow = findViewById(R.id.tr_1);
                break;
            }
            case 1: {
                tableRow = findViewById(R.id.tr_2);
                break;
            }
            case 2: {
                tableRow = findViewById(R.id.tr_3);
                break;
            }
            case 3: {
                tableRow = findViewById(R.id.tr_4);
                break;
            }
            case 4: {
                tableRow = findViewById(R.id.tr_5);
                break;
            }
            case 5: {
                tableRow = findViewById(R.id.tr_6);
                break;
            }
            case 6: {
                tableRow = findViewById(R.id.tr_7);
                break;
            }
            case 7: {
                tableRow = findViewById(R.id.tr_8);
                break;
            }
            case 8: {
                tableRow = findViewById(R.id.tr_9);
                break;
            }
            case 9: {
                tableRow = findViewById(R.id.tr_10);
                break;
            }
            case 10: {
                tableRow = findViewById(R.id.tr_11);
                break;
            }
            case 11: {
                tableRow = findViewById(R.id.tr_12);
                break;
            }
            case 12: {
                tableRow = findViewById(R.id.tr_13);
                break;
            }
            case 13: {
                tableRow = findViewById(R.id.tr_14);
                break;
            }
            case 14: {
                tableRow = findViewById(R.id.tr_15);
                break;
            }
            case 15: {
                tableRow = findViewById(R.id.tr_16);
                break;
            }
            case 16: {
                tableRow = findViewById(R.id.tr_17);
                break;
            }
            case 17: {
                tableRow = findViewById(R.id.tr_18);
                break;
            }
            case 18: {
                tableRow = findViewById(R.id.tr_19);
                break;
            }
            case 19: {
                tableRow = findViewById(R.id.tr_20);
                break;
            }
            case 20: {
                tableRow = findViewById(R.id.tr_21);
                break;
            }
            case 21: {
                tableRow = findViewById(R.id.tr_22);
                break;
            }
            case 22: {
                tableRow = findViewById(R.id.tr_23);
                break;
            }
            case 23: {
                tableRow = findViewById(R.id.tr_24);
                break;
            }

            case 24: {
                tableRow = findViewById(R.id.tr_25);
                break;
            }
        }

        final String finalSu = su;
        tableRow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder alertPayInstallment = new AlertDialog.Builder(HomeProfileActivity.this);
                alertPayInstallment.setTitle("Pay Installment ??");
                alertPayInstallment.setMessage("Do you want to pay your " + (paidCount + 1) + " " + finalSu + " installment...??");
                alertPayInstallment.setIcon(R.drawable.app_logo);
                alertPayInstallment.setPositiveButton("YES", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        // DateTimeFormatter
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
                            Intent intentPay = new Intent(HomeProfileActivity.this, PayInstallmentSelf.class);
                            intentPay.putExtra("udataall", retriveUserData);
                            startActivity(intentPay);
                        } else {
                            AlertDialog.Builder dialog = new AlertDialog.Builder(HomeProfileActivity.this);
                            dialog.setTitle("Installment after 30 Days.");
                            dialog.setMessage("Your last installment paid on " + last_pay_date + ".\n You cant pay now. Pay installment after " + (30 - days_gap) + " days");
                            dialog.setIcon(R.drawable.app_logo);
                            dialog.setPositiveButton("OK", null);
                            dialog.setCancelable(false);
                            dialog.show();
                        }
                    }
                });
                alertPayInstallment.setNeutralButton("CANCEL", null);
                alertPayInstallment.setCancelable(true);
                alertPayInstallment.show();

            }
        });
        tvBottomNotice.setText("click " + (paidCount + 1) + " " + su + " row to make payment.");
    }

    private void designPaidNumbers(int month_no, String p_date, String plan_amt) {
        switch (month_no) {
            case 25:
                iv_pay_25 = findViewById(R.id.iv_pay25);
                iv_pay_25.setImageResource(R.drawable.ic_paid_green);
                tv_payDate_25.setText(p_date);
                tv_plan_25.setText(plan_amt);
                break;
            case 24:
                iv_pay_24 = findViewById(R.id.iv_pay24);
                iv_pay_24.setImageResource(R.drawable.ic_paid_green);
                tv_payDate_24.setText(p_date);
                tv_plan_24.setText(plan_amt);
                break;
            case 23:
                iv_pay_23 = findViewById(R.id.iv_pay23);
                iv_pay_23.setImageResource(R.drawable.ic_paid_green);
                tv_payDate_23.setText(p_date);
                tv_plan_23.setText(plan_amt);
                break;
            case 22:
                iv_pay_22 = findViewById(R.id.iv_pay22);
                iv_pay_22.setImageResource(R.drawable.ic_paid_green);
                tv_payDate_22.setText(p_date);
                tv_plan_22.setText(plan_amt);
                break;
            case 21:
                iv_pay_21 = findViewById(R.id.iv_pay21);
                iv_pay_21.setImageResource(R.drawable.ic_paid_green);
                tv_payDate_21.setText(p_date);
                tv_plan_21.setText(plan_amt);
                break;
            case 20:
                iv_pay_20 = findViewById(R.id.iv_pay20);
                iv_pay_20.setImageResource(R.drawable.ic_paid_green);
                tv_payDate_20.setText(p_date);
                tv_plan_20.setText(plan_amt);
                break;
            case 19:
                iv_pay_19 = findViewById(R.id.iv_pay19);
                iv_pay_19.setImageResource(R.drawable.ic_paid_green);
                tv_payDate_19.setText(p_date);
                tv_plan_19.setText(plan_amt);
                break;
            case 18:
                iv_pay_18 = findViewById(R.id.iv_pay18);
                iv_pay_18.setImageResource(R.drawable.ic_paid_green);
                tv_payDate_18.setText(p_date);
                tv_plan_18.setText(plan_amt);
                break;
            case 17:
                iv_pay_17 = findViewById(R.id.iv_pay17);
                iv_pay_17.setImageResource(R.drawable.ic_paid_green);
                tv_payDate_17.setText(p_date);
                tv_plan_17.setText(plan_amt);
                break;
            case 16:
                iv_pay_16 = findViewById(R.id.iv_pay16);
                iv_pay_16.setImageResource(R.drawable.ic_paid_green);
                tv_payDate_16.setText(p_date);
                tv_plan_16.setText(plan_amt);
                break;
            case 15:
                iv_pay_15 = findViewById(R.id.iv_pay15);
                iv_pay_15.setImageResource(R.drawable.ic_paid_green);
                tv_payDate_15.setText(p_date);
                tv_plan_15.setText(plan_amt);
                break;
            case 14:
                iv_pay_14 = findViewById(R.id.iv_pay14);
                iv_pay_14.setImageResource(R.drawable.ic_paid_green);
                tv_payDate_14.setText(p_date);
                tv_plan_14.setText(plan_amt);
                break;
            case 13:
                iv_pay_13 = findViewById(R.id.iv_pay13);
                iv_pay_13.setImageResource(R.drawable.ic_paid_green);
                tv_payDate_13.setText(p_date);
                tv_plan_13.setText(plan_amt);
                break;
            case 12:
                iv_pay_12 = findViewById(R.id.iv_pay12);
                iv_pay_12.setImageResource(R.drawable.ic_paid_green);
                tv_payDate_12.setText(p_date);
                tv_plan_12.setText(plan_amt);
                break;
            case 11:
                iv_pay_11 = findViewById(R.id.iv_pay11);
                iv_pay_11.setImageResource(R.drawable.ic_paid_green);
                tv_payDate_11.setText(p_date);
                tv_plan_11.setText(plan_amt);
                break;
            case 10:
                iv_pay_10 = findViewById(R.id.iv_pay10);
                iv_pay_10.setImageResource(R.drawable.ic_paid_green);
                tv_payDate_10.setText(p_date);
                tv_plan_10.setText(plan_amt);
                break;
            case 9:
                iv_pay_9 = findViewById(R.id.iv_pay9);
                iv_pay_9.setImageResource(R.drawable.ic_paid_green);
                tv_payDate_9.setText(p_date);
                tv_plan_9.setText(plan_amt);
                break;
            case 8:
                iv_pay_8 = findViewById(R.id.iv_pay8);
                iv_pay_8.setImageResource(R.drawable.ic_paid_green);
                tv_payDate_8.setText(p_date);
                tv_plan_8.setText(plan_amt);
                break;
            case 7:
                iv_pay_7 = findViewById(R.id.iv_pay7);
                iv_pay_7.setImageResource(R.drawable.ic_paid_green);
                tv_payDate_7.setText(p_date);
                tv_plan_7.setText(plan_amt);
                break;
            case 6:
                iv_pay_6 = findViewById(R.id.iv_pay6);
                iv_pay_6.setImageResource(R.drawable.ic_paid_green);
                tv_payDate_6.setText(p_date);
                tv_plan_6.setText(plan_amt);
                break;
            case 5:
                iv_pay_5 = findViewById(R.id.iv_pay5);
                iv_pay_5.setImageResource(R.drawable.ic_paid_green);
                tv_payDate_5.setText(p_date);
                tv_plan_5.setText(plan_amt);
                break;
            case 4:
                iv_pay_4 = findViewById(R.id.iv_pay4);
                iv_pay_4.setImageResource(R.drawable.ic_paid_green);
                tv_payDate_4.setText(p_date);
                tv_plan_4.setText(plan_amt);
                break;
            case 3:
                iv_pay_3 = findViewById(R.id.iv_pay3);
                iv_pay_3.setImageResource(R.drawable.ic_paid_green);
                tv_payDate_3.setText(p_date);
                tv_plan_3.setText(plan_amt);
                break;
            case 2:
                iv_pay_2 = findViewById(R.id.iv_pay2);
                iv_pay_2.setImageResource(R.drawable.ic_paid_green);
                tv_payDate_2.setText(p_date);
                tv_plan_2.setText(plan_amt);
                break;
            case 1:
                iv_pay_1 = findViewById(R.id.iv_pay1);
                iv_pay_1.setImageResource(R.drawable.ic_paid_green);
                tv_payDate_1.setText(p_date);
                tv_plan_1.setText(plan_amt);
                break;
            case 0:
                break;
        }
    }

    public void showPlanSelectorPopup() {
        AlertDialog.Builder adb = new AlertDialog.Builder(this);
        adb.setTitle(R.string.dialog_title);
        adb.setMessage("Please select plans");
        View view = getLayoutInflater().inflate(R.layout.plan_selector_dialog, null);

        planSpinner = view.findViewById(R.id.planspinner);

        //////// plans-spinner start /////
        String plans[] = {"-- Select Plans --", "1000", "2000", "3000", "4000", "5000", "10000", "15000", "20000"};
        Adapter one_qty_unit = new ArrayAdapter<String>(HomeProfileActivity.this, android.R.layout.simple_dropdown_item_1line, plans);
        planSpinner.setAdapter((SpinnerAdapter) one_qty_unit);
        planSpinner.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                ((TextView) planSpinner.getSelectedView()).setTextColor(Color.BLACK);
                ((TextView) planSpinner.getSelectedView()).setTextSize(15);
            }
        });
        ///// plans-spinner end ///////

        adb.setPositiveButton("Submit", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

                dialog = new Dialog(HomeProfileActivity.this);
                dialog.setContentView(R.layout.layout_progress_dialog);
                if (dialog.getWindow() != null) {
                    dialog.getWindow().setBackgroundDrawable(new ColorDrawable(0));
                }
                dialog.setCancelable(false);
                dialog.show();

                if (planSpinner.getSelectedItem().toString().equals("-- Select Plans --")) {
                    Toast.makeText(HomeProfileActivity.this, "Please select plans.", Toast.LENGTH_SHORT).show();
                    dialog.dismiss();
                    showPlanSelectorPopup();
                } else {
                    selectedPlanSentToDb();
                }
            }
        });
        adb.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
            }
        });
        adb.setCancelable(false);
        adb.setIcon(R.drawable.app_logo);
        adb.setView(view);
        adb.show();
    }

    public void selectedPlanSentToDb() {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, BaseUrl.updatePlanUrl, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                if (response.contains("Updated")) {
                    dialog.dismiss();
                    Toast.makeText(HomeProfileActivity.this, "Plan added successfully.", Toast.LENGTH_LONG).show();
                    AlertDialog.Builder adb = new AlertDialog.Builder(HomeProfileActivity.this);
                    adb.setTitle(R.string.dialog_title);
                    adb.setMessage("Plan added successfully.");
                    adb.setIcon(R.drawable.app_logo);
                    adb.setCancelable(false);
                    adb.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            dialogInterface.dismiss();
                            startActivity(new Intent(getApplicationContext(), HomeActivity.class));
                        }
                    });
                    adb.show();
                }

                if (response.contains("Not updated")) {
                    dialog.dismiss();
                    Toast.makeText(HomeProfileActivity.this, "Plan is not added successfully.", Toast.LENGTH_LONG).show();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                dialog.dismiss();
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> map = new HashMap<>();
                String dbId = retriveUserData.substring(retriveUserData.indexOf("!") + 1, retriveUserData.indexOf("@"));
                map.put("id", dbId);
                map.put("plan", planSpinner.getSelectedItem().toString());
                return map;
            }
        };

        Volley.newRequestQueue(HomeProfileActivity.this).add(stringRequest);
    }

    private void getReadySomeViews() {
        tv_plan_1 = findViewById(R.id.tv_plan1);
        tv_plan_2 = findViewById(R.id.tv_plan2);
        tv_plan_3 = findViewById(R.id.tv_plan3);
        tv_plan_4 = findViewById(R.id.tv_plan4);
        tv_plan_5 = findViewById(R.id.tv_plan5);
        tv_plan_6 = findViewById(R.id.tv_plan6);
        tv_plan_7 = findViewById(R.id.tv_plan7);
        tv_plan_8 = findViewById(R.id.tv_plan8);
        tv_plan_9 = findViewById(R.id.tv_plan9);
        tv_plan_10 = findViewById(R.id.tv_plan10);
        tv_plan_11 = findViewById(R.id.tv_plan11);
        tv_plan_12 = findViewById(R.id.tv_plan12);
        tv_plan_13 = findViewById(R.id.tv_plan13);
        tv_plan_14 = findViewById(R.id.tv_plan14);
        tv_plan_15 = findViewById(R.id.tv_plan15);
        tv_plan_16 = findViewById(R.id.tv_plan16);
        tv_plan_17 = findViewById(R.id.tv_plan17);
        tv_plan_18 = findViewById(R.id.tv_plan18);
        tv_plan_19 = findViewById(R.id.tv_plan19);
        tv_plan_20 = findViewById(R.id.tv_plan20);
        tv_plan_21 = findViewById(R.id.tv_plan21);
        tv_plan_22 = findViewById(R.id.tv_plan22);
        tv_plan_23 = findViewById(R.id.tv_plan23);
        tv_plan_24 = findViewById(R.id.tv_plan24);
        tv_plan_25 = findViewById(R.id.tv_plan25);
        tv_payDate_1 = findViewById(R.id.tv_paydate1);
        tv_payDate_2 = findViewById(R.id.tv_paydate2);
        tv_payDate_3 = findViewById(R.id.tv_paydate3);
        tv_payDate_4 = findViewById(R.id.tv_paydate4);
        tv_payDate_5 = findViewById(R.id.tv_paydate5);
        tv_payDate_6 = findViewById(R.id.tv_paydate6);
        tv_payDate_7 = findViewById(R.id.tv_paydate7);
        tv_payDate_8 = findViewById(R.id.tv_paydate8);
        tv_payDate_9 = findViewById(R.id.tv_paydate9);
        tv_payDate_10 = findViewById(R.id.tv_paydate10);
        tv_payDate_11 = findViewById(R.id.tv_paydate11);
        tv_payDate_12 = findViewById(R.id.tv_paydate12);
        tv_payDate_13 = findViewById(R.id.tv_paydate13);
        tv_payDate_14 = findViewById(R.id.tv_paydate14);
        tv_payDate_15 = findViewById(R.id.tv_paydate15);
        tv_payDate_16 = findViewById(R.id.tv_paydate16);
        tv_payDate_17 = findViewById(R.id.tv_paydate17);
        tv_payDate_18 = findViewById(R.id.tv_paydate18);
        tv_payDate_19 = findViewById(R.id.tv_paydate19);
        tv_payDate_20 = findViewById(R.id.tv_paydate20);
        tv_payDate_21 = findViewById(R.id.tv_paydate21);
        tv_payDate_22 = findViewById(R.id.tv_paydate22);
        tv_payDate_23 = findViewById(R.id.tv_paydate23);
        tv_payDate_24 = findViewById(R.id.tv_paydate24);
        tv_payDate_25 = findViewById(R.id.tv_paydate25);
        tv_payCapLimYear_1 = findViewById(R.id.tv_pay_caplim1);
        tv_payCapLimYear_2 = findViewById(R.id.tv_pay_caplim2);
        tv_payCapLimYear_3 = findViewById(R.id.tv_pay_caplim3);
        tv_payCapLimYear_4 = findViewById(R.id.tv_pay_caplim4);
        tv_payCapLimYear_5 = findViewById(R.id.tv_pay_caplim5);
        tv_payCapLimYear_6 = findViewById(R.id.tv_pay_caplim6);
        tv_payCapLimYear_7 = findViewById(R.id.tv_pay_caplim7);
        tv_payCapLimYear_8 = findViewById(R.id.tv_pay_caplim8);
        tv_payCapLimYear_9 = findViewById(R.id.tv_pay_caplim9);
        tv_payCapLimYear_10 = findViewById(R.id.tv_pay_caplim10);
        tv_payCapLimYear_11 = findViewById(R.id.tv_pay_caplim11);
        tv_payCapLimYear_12 = findViewById(R.id.tv_pay_caplim12);
        tv_payCapLimYear_13 = findViewById(R.id.tv_pay_caplim13);
        tv_payCapLimYear_14 = findViewById(R.id.tv_pay_caplim14);
        tv_payCapLimYear_15 = findViewById(R.id.tv_pay_caplim15);
        tv_payCapLimYear_16 = findViewById(R.id.tv_pay_caplim16);
        tv_payCapLimYear_17 = findViewById(R.id.tv_pay_caplim17);
        tv_payCapLimYear_18 = findViewById(R.id.tv_pay_caplim18);
        tv_payCapLimYear_19 = findViewById(R.id.tv_pay_caplim19);
        tv_payCapLimYear_20 = findViewById(R.id.tv_pay_caplim20);
        tv_payCapLimYear_21 = findViewById(R.id.tv_pay_caplim21);
        tv_payCapLimYear_22 = findViewById(R.id.tv_pay_caplim22);
        tv_payCapLimYear_23 = findViewById(R.id.tv_pay_caplim23);
        tv_payCapLimYear_24 = findViewById(R.id.tv_pay_caplim24);
        tv_payCapLimYear_25 = findViewById(R.id.tv_pay_caplim25);
    }
}