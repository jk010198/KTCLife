package com.ithublive.ktclife;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.ithublive.ktclife.Model.LegMapModel;
import com.ithublive.ktclife.Utils.BaseUrl;

import java.util.HashMap;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;

public class DisplayTeamOLD extends AppCompatActivity {

    TextView tv_name1, tv_name2, tv_name3, tv_name4, tv_name5, tv_name6, tv_name7, tv_name8;
    TextView tv_userid1, tv_userid2, tv_userid3, tv_userid4, tv_userid5, tv_userid6, tv_userid7, tv_userid8;
    TextView tv_direct_ref_1, tv_direct_ref_2, tv_direct_ref_3, tv_direct_ref_4, tv_direct_ref_5, tv_direct_ref_6, tv_direct_ref_7, tv_direct_ref_8;
    RelativeLayout rl1, rl2, rl3, rl4, rl5, rl6, rl7, rl8;
    CircleImageView profile_img1, profile_img2, profile_img3, profile_img4, profile_img5, profile_img6, profile_img7, profile_img8;
    CircleImageView userInfo1, userInfo2, userInfo3, userInfo4, userInfo5, userInfo6, userInfo7, userInfo8;
    String id, username = "", keyword;
    SharedPreferences leg_expansion, sp;
    TextView tv_legs, tv_noleg_msg;
    HashMap<String, LegMapModel> idUrl_map = new HashMap<String, LegMapModel>();//Creating HashMap
    ProgressDialog progressDialog_legInfo;
    boolean isHavingLeg = true, isBackpress;

    CircleImageView ib_user1_one, ib_user1_two, ib_user1_three, ib_user1_four, ib_user1_five, ib_user1_six, ib_user1_seven, ib_user1_eight,
            ib_user2_one, ib_user2_two, ib_user2_three, ib_user2_four, ib_user2_five, ib_user2_six, ib_user2_seven, ib_user2_eight,
            ib_user3_one, ib_user3_two, ib_user3_three, ib_user3_four, ib_user3_five, ib_user3_six, ib_user3_seven, ib_user3_eight,
            ib_user4_one, ib_user4_two, ib_user4_three, ib_user4_four, ib_user4_five, ib_user4_six, ib_user4_seven, ib_user4_eight,
            ib_user5_one, ib_user5_two, ib_user5_three, ib_user5_four, ib_user5_five, ib_user5_six, ib_user5_seven, ib_user5_eight,
            ib_user6_one, ib_user6_two, ib_user6_three, ib_user6_four, ib_user6_five, ib_user6_six, ib_user6_seven, ib_user6_eight,
            ib_user7_one, ib_user7_two, ib_user7_three, ib_user7_four, ib_user7_five, ib_user7_six, ib_user7_seven, ib_user7_eight,
            ib_user8_one, ib_user8_two, ib_user8_three, ib_user8_four, ib_user8_five, ib_user8_six, ib_user8_seven, ib_user8_eight;

 //   @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_display_team);
//        tv_legs = findViewById(R.id.textview_legs);
//        tv_legs.setMovementMethod(new ScrollingMovementMethod());
//        tv_noleg_msg = findViewById(R.id.tv_nolegs_msg);
//        tv_noleg_msg.setVisibility(View.INVISIBLE);
//        SharedPreferences shared = getSharedPreferences("" + R.string.sp_name, MODE_PRIVATE);
//        id = (shared.getString("username", ""));
//        leg_expansion = getSharedPreferences("legs_data", MODE_PRIVATE);
//        SharedPreferences.Editor edit = leg_expansion.edit();
//        edit.putString("userid", id);
//        edit.commit();
//        {
//            tv_direct_ref_1 = findViewById(R.id.tv_direct_ref);
//            tv_direct_ref_2 = findViewById(R.id.tv_direct_ref_2);
//            tv_direct_ref_3 = findViewById(R.id.tv_direct_ref_3);
//            tv_direct_ref_4 = findViewById(R.id.tv_direct_ref_4);
//            tv_direct_ref_5 = findViewById(R.id.tv_direct_ref_5);
//            tv_direct_ref_6 = findViewById(R.id.tv_direct_ref_6);
//            tv_direct_ref_7 = findViewById(R.id.tv_direct_ref_7);
//            tv_direct_ref_8 = findViewById(R.id.tv_direct_ref_8);
//
//            ib_user1_one = findViewById(R.id.ib_user1_one);
//            ib_user1_two = findViewById(R.id.ib_user1_two);
//            ib_user1_three = findViewById(R.id.ib_user1_three);
//            ib_user1_four = findViewById(R.id.ib_user1_four);
//            ib_user1_five = findViewById(R.id.ib_user1_five);
//            ib_user1_six = findViewById(R.id.ib_user1_six);
//            ib_user1_seven = findViewById(R.id.ib_user1_seven);
//            ib_user1_eight = findViewById(R.id.ib_user1_eight);
//
//            ib_user2_one = findViewById(R.id.ib_user2_one);
//            ib_user2_two = findViewById(R.id.ib_user2_two);
//            ib_user2_three = findViewById(R.id.ib_user2_three);
//            ib_user2_four = findViewById(R.id.ib_user2_four);
//            ib_user2_five = findViewById(R.id.ib_user2_five);
//            ib_user2_six = findViewById(R.id.ib_user2_six);
//            ib_user2_seven = findViewById(R.id.ib_user2_seven);
//            ib_user2_eight = findViewById(R.id.ib_user2_eight);
//
//            ib_user3_one = findViewById(R.id.ib_user3_one);
//            ib_user3_two = findViewById(R.id.ib_user3_two);
//            ib_user3_three = findViewById(R.id.ib_user3_three);
//            ib_user3_four = findViewById(R.id.ib_user3_four);
//            ib_user3_five = findViewById(R.id.ib_user3_five);
//            ib_user3_six = findViewById(R.id.ib_user3_six);
//            ib_user3_seven = findViewById(R.id.ib_user3_seven);
//            ib_user3_eight = findViewById(R.id.ib_user3_eight);
//
//            ib_user4_one = findViewById(R.id.ib_user4_one);
//            ib_user4_two = findViewById(R.id.ib_user4_two);
//            ib_user4_three = findViewById(R.id.ib_user4_three);
//            ib_user4_four = findViewById(R.id.ib_user4_four);
//            ib_user4_five = findViewById(R.id.ib_user4_five);
//            ib_user4_six = findViewById(R.id.ib_user4_six);
//            ib_user4_seven = findViewById(R.id.ib_user4_seven);
//            ib_user4_eight = findViewById(R.id.ib_user4_eight);
//
//            ib_user5_one = findViewById(R.id.ib_user5_one);
//            ib_user5_two = findViewById(R.id.ib_user5_two);
//            ib_user5_three = findViewById(R.id.ib_user5_three);
//            ib_user5_four = findViewById(R.id.ib_user5_four);
//            ib_user5_five = findViewById(R.id.ib_user5_five);
//            ib_user5_six = findViewById(R.id.ib_user5_six);
//            ib_user5_seven = findViewById(R.id.ib_user5_seven);
//            ib_user5_eight = findViewById(R.id.ib_user5_eight);
//
//            ib_user6_one = findViewById(R.id.ib_user6_one);
//            ib_user6_two = findViewById(R.id.ib_user6_two);
//            ib_user6_three = findViewById(R.id.ib_user6_three);
//            ib_user6_four = findViewById(R.id.ib_user6_four);
//            ib_user6_five = findViewById(R.id.ib_user6_five);
//            ib_user6_six = findViewById(R.id.ib_user6_six);
//            ib_user6_seven = findViewById(R.id.ib_user6_seven);
//            ib_user6_eight = findViewById(R.id.ib_user6_eight);
//
//            ib_user7_one = findViewById(R.id.ib_user7_one);
//            ib_user7_two = findViewById(R.id.ib_user7_two);
//            ib_user7_three = findViewById(R.id.ib_user7_three);
//            ib_user7_four = findViewById(R.id.ib_user7_four);
//            ib_user7_five = findViewById(R.id.ib_user7_five);
//            ib_user7_six = findViewById(R.id.ib_user7_six);
//            ib_user7_seven = findViewById(R.id.ib_user7_seven);
//            ib_user7_eight = findViewById(R.id.ib_user7_eight);
//
//            ib_user8_one = findViewById(R.id.ib_user8_one);
//            ib_user8_two = findViewById(R.id.ib_user8_two);
//            ib_user8_three = findViewById(R.id.ib_user8_three);
//            ib_user8_four = findViewById(R.id.ib_user8_four);
//            ib_user8_five = findViewById(R.id.ib_user8_five);
//            ib_user8_six = findViewById(R.id.ib_user8_six);
//            ib_user8_seven = findViewById(R.id.ib_user8_seven);
//            ib_user8_eight = findViewById(R.id.ib_user8_eight);
//
//            rl1 = findViewById(R.id.relativelayout1);
//            rl2 = findViewById(R.id.relativelayout2);
//            rl3 = findViewById(R.id.relativelayout3);
//            rl4 = findViewById(R.id.relativelayout4);
//            rl5 = findViewById(R.id.relativelayout5);
//            rl6 = findViewById(R.id.relativelayout6);
//            rl7 = findViewById(R.id.relativelayout7);
//            rl8 = findViewById(R.id.relativelayout8);
//
//            profile_img1 = findViewById(R.id.profile_img);
//            profile_img2 = findViewById(R.id.profile_img_2);
//            profile_img3 = findViewById(R.id.profile_img_3);
//            profile_img4 = findViewById(R.id.profile_img_4);
//            profile_img5 = findViewById(R.id.profile_img_5);
//            profile_img6 = findViewById(R.id.profile_img_6);
//            profile_img7 = findViewById(R.id.profile_img_7);
//            profile_img8 = findViewById(R.id.profile_img_8);
//
//            userInfo1 = findViewById(R.id.userInfo);
//            userInfo2 = findViewById(R.id.userInfo2);
//            userInfo3 = findViewById(R.id.userInfo3);
//            userInfo4 = findViewById(R.id.userInfo4);
//            userInfo5 = findViewById(R.id.userInfo5);
//            userInfo6 = findViewById(R.id.userInfo6);
//            userInfo7 = findViewById(R.id.userInfo7);
//            userInfo8 = findViewById(R.id.userInfo8);
//
//            tv_name1 = findViewById(R.id.tv_name);
//            tv_name2 = findViewById(R.id.tv_name_2);
//            tv_name3 = findViewById(R.id.tv_name_3);
//            tv_name4 = findViewById(R.id.tv_name_4);
//            tv_name5 = findViewById(R.id.tv_name_5);
//            tv_name6 = findViewById(R.id.tv_name_6);
//            tv_name7 = findViewById(R.id.tv_name_7);
//            tv_name8 = findViewById(R.id.tv_name_8);
//
//            tv_userid1 = findViewById(R.id.tv_userid);
//            tv_userid2 = findViewById(R.id.tv_userid_2);
//            tv_userid3 = findViewById(R.id.tv_userid_3);
//            tv_userid4 = findViewById(R.id.tv_userid_4);
//            tv_userid5 = findViewById(R.id.tv_userid_5);
//            tv_userid6 = findViewById(R.id.tv_userid_6);
//            tv_userid7 = findViewById(R.id.tv_userid_7);
//            tv_userid8 = findViewById(R.id.tv_userid_8);
//        }
//        progressDialog_legInfo = new ProgressDialog(DisplayTeamOLD.this);
//        progressDialog_legInfo.setTitle(R.string.dialog_title);
//        progressDialog_legInfo.setMessage("Please wait, fetching data");
//        progressDialog_legInfo.setCancelable(false);
//        progressDialog_legInfo.setIcon(R.drawable.app_logo);
//
//        getUserDataFromDB();
//
//        rl1.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                isBackpress = false;
//                id = tv_userid1.getText().toString();
//                String value = leg_expansion.getString("userid", "");
//                StringBuffer str = new StringBuffer(value);
//                str.append(id);
//                SharedPreferences.Editor edit = leg_expansion.edit();
//                edit.putString("userid", String.valueOf(str));
//                edit.commit();
//                getUserDataFromDB();
//            }
//        });
//
//        rl2.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                isBackpress = false;
//                id = tv_userid2.getText().toString();
//
//                String value = leg_expansion.getString("userid", "");
//                StringBuffer str = new StringBuffer(value);
//                str.append(id);
//                SharedPreferences.Editor edit = leg_expansion.edit();
//                edit.putString("userid", String.valueOf(str));
//                edit.commit();
//                getUserDataFromDB();
//            }
//        });
//
//        rl3.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                isBackpress = false;
//                id = tv_userid3.getText().toString();
//                String value = leg_expansion.getString("userid", "");
//                StringBuffer str = new StringBuffer(value);
//                str.append(id);
//                SharedPreferences.Editor edit = leg_expansion.edit();
//                edit.putString("userid", String.valueOf(str));
//                edit.commit();
//                getUserDataFromDB();
//            }
//        });
//
//        rl4.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                isBackpress = false;
//                id = tv_userid4.getText().toString();
//                String value = leg_expansion.getString("userid", "");
//                StringBuffer str = new StringBuffer(value);
//                str.append(id);
//                SharedPreferences.Editor edit = leg_expansion.edit();
//                edit.putString("userid", String.valueOf(str));
//                edit.commit();
//                getUserDataFromDB();
//            }
//        });
//
//        rl5.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                isBackpress = false;
//                id = tv_userid5.getText().toString();
//                String value = leg_expansion.getString("userid", "");
//                StringBuffer str = new StringBuffer(value);
//                str.append(id);
//                SharedPreferences.Editor edit = leg_expansion.edit();
//                edit.putString("userid", String.valueOf(str));
//                edit.commit();
//                getUserDataFromDB();
//            }
//        });
//
//        rl6.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                isBackpress = false;
//                id = tv_userid6.getText().toString();
//                String value = leg_expansion.getString("userid", "");
//                StringBuffer str = new StringBuffer(value);
//                str.append(id);
//                SharedPreferences.Editor edit = leg_expansion.edit();
//                edit.putString("userid", String.valueOf(str));
//                edit.commit();
//                getUserDataFromDB();
//            }
//        });
//
//        rl7.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                isBackpress = false;
//                id = tv_userid7.getText().toString();
//                String value = leg_expansion.getString("userid", "");
//                StringBuffer str = new StringBuffer(value);
//                str.append(id);
//                SharedPreferences.Editor edit = leg_expansion.edit();
//                edit.putString("userid", String.valueOf(str));
//                edit.commit();
//                getUserDataFromDB();
//            }
//        });
//
//        rl8.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                isBackpress = false;
//                id = tv_userid8.getText().toString();
//                String value = leg_expansion.getString("userid", "");
//                StringBuffer str = new StringBuffer(value);
//                str.append(id);
//                SharedPreferences.Editor edit = leg_expansion.edit();
//                edit.putString("userid", String.valueOf(str));
//                edit.commit();
//                getUserDataFromDB();
//            }
//        });
//
//        userInfo1.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                getLegIndivisualInfo(tv_userid1.getText().toString());
//            }
//        });
//
//        userInfo2.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                getLegIndivisualInfo(tv_userid2.getText().toString());
//            }
//        });
//
//        userInfo3.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                getLegIndivisualInfo(tv_userid3.getText().toString());
//            }
//        });
//
//        userInfo4.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                getLegIndivisualInfo(tv_userid4.getText().toString());
//            }
//        });
//
//        userInfo5.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                getLegIndivisualInfo(tv_userid5.getText().toString());
//            }
//        });
//
//        userInfo6.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                getLegIndivisualInfo(tv_userid6.getText().toString());
//            }
//        });
//
//        userInfo7.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                getLegIndivisualInfo(tv_userid7.getText().toString());
//            }
//        });
//
//        userInfo8.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                getLegIndivisualInfo(tv_userid8.getText().toString());
//            }
//        });
//    }

    private void getLegIndivisualInfo(final String legID) {
        progressDialog_legInfo.show();
        StringRequest stringRequest = new StringRequest(Request.Method.POST, BaseUrl.retriveUserDataUrl, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                if (response.contains("no data found")) {
                    Toast.makeText(DisplayTeamOLD.this, "no data found.", Toast.LENGTH_SHORT).show();
                    rl1.setVisibility(View.INVISIBLE);
                    rl2.setVisibility(View.INVISIBLE);
                    rl3.setVisibility(View.INVISIBLE);
                    rl4.setVisibility(View.INVISIBLE);
                    rl5.setVisibility(View.INVISIBLE);
                    rl6.setVisibility(View.INVISIBLE);
                    rl7.setVisibility(View.INVISIBLE);
                    rl8.setVisibility(View.INVISIBLE);
                    tv_noleg_msg.setVisibility(View.INVISIBLE);
                } else {
                    rl1.setVisibility(View.VISIBLE);
                    rl2.setVisibility(View.VISIBLE);
                    rl3.setVisibility(View.VISIBLE);
                    rl4.setVisibility(View.VISIBLE);
                    rl5.setVisibility(View.VISIBLE);
                    rl6.setVisibility(View.VISIBLE);
                    rl7.setVisibility(View.VISIBLE);
                    rl8.setVisibility(View.VISIBLE);
                    tv_noleg_msg.setVisibility(View.VISIBLE);

                    String data = response;
                    String username = data.substring(data.indexOf("@") + 1, data.indexOf("#"));
                    String my_Plan = data.substring(data.indexOf("&") + 1, data.indexOf("*"));
                    String isPaid = data.substring(data.indexOf("#1A#") + 4, data.indexOf("#2A#"));
                    String join_date = data.substring(data.indexOf("#2A#") + 4, data.indexOf("#3A#"));
                    join_date = join_date.substring(0, 10);
                    String tv_number_legs = data.substring(data.indexOf("#3A#") + 4, data.indexOf("#4A#")) +
                            "         Direct ref. : " + data.substring(data.indexOf("#20A#") + 5, data.indexOf("#21A#"));
                    String poppay_details = "Pay Details:\n";
                    int paidCount = 0;
                    if (data.contains("#pn#") && isPaid.contains("yes")) {
                        paidCount = Integer.parseInt(data.substring(data.indexOf("#pn#") + 4, data.indexOf("#epn#")));
                        String payDetails = data.substring(data.indexOf("#epn#") + 5, data.indexOf(":e_pay:"));
                        String payDetails_array[] = payDetails.split(",");
                        for (String pdata : payDetails_array) {
                            String p_date = pdata.substring(0, 10);
                            int month_no = Integer.parseInt(pdata.substring(pdata.indexOf("::") + 2, pdata.indexOf(":_:")));
                            String plan_amt = pdata.substring(pdata.indexOf(":_:") + 3, pdata.indexOf(":$:"));
                            poppay_details = poppay_details + month_no + "\t\t" + p_date + "\t\t" + plan_amt + "\n";
                        }
                    }
                    //////// Set up POPUP
                    final AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(DisplayTeamOLD.this);
                    final AlertDialog alert = alertDialogBuilder.create();
                    final LayoutInflater layoutInflater = LayoutInflater.from(DisplayTeamOLD.this);
                    final View popupInputDialogView = layoutInflater.inflate(R.layout.layout_popup_leginfo, null);
                    alert.setView(popupInputDialogView);
                    alert.show();
                    alert.setCancelable(false);
                    final TextView tv_popup_leginfo = popupInputDialogView.findViewById(R.id.tv_popupInfoOfLeg);


                    tv_popup_leginfo.setText("ID: " + legID + "\nName: " + username + "\n\nPlan: " + my_Plan + "\t\t\tIs Paid:   " + isPaid +
                            "\n\nJoin Date: " + join_date + "\n\nNumber of Legs: " + tv_number_legs + "\n" + poppay_details);
                    ////////////////////
                    Button btn_close_Dialog = popupInputDialogView.findViewById(R.id.buttonClosePopupLeginfo);
                    ///// check if admin ///////
                    TextView tv_adminTask_Dialog = popupInputDialogView.findViewById(R.id.tv_adminTasksOnpopupLegInfo);
                    tv_adminTask_Dialog.setVisibility(View.GONE);
                    SharedPreferences shared = getSharedPreferences("" + R.string.sp_retriveUserData, MODE_PRIVATE);
                    String retriveUserData = (shared.getString("data", ""));
                    String id = retriveUserData.substring(retriveUserData.indexOf("!") + 1, retriveUserData.indexOf("@"));
                    if (id.equals("95947639111")) {
                        tv_adminTask_Dialog.setVisibility(View.VISIBLE);
                    }
                    ////////////

                    ImageButton imageButton = popupInputDialogView.findViewById(R.id.btn_dialog_ktc_close);
                    imageButton.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            alert.dismiss();
                        }
                    });
                    btn_close_Dialog.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            //alert.cancel();
                            alert.dismiss();
                        }
                    });
                    tv_adminTask_Dialog.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            Intent intent = new Intent(DisplayTeamOLD.this, AdminUpdatePayments.class);
                            intent.putExtra("u_info_id", legID);
                            startActivity(intent);
                        }
                    });

                }
                progressDialog_legInfo.dismiss();

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(DisplayTeamOLD.this, "Something went wrong.", Toast.LENGTH_SHORT).show();
                progressDialog_legInfo.dismiss();
                rl1.setVisibility(View.INVISIBLE);
                rl2.setVisibility(View.INVISIBLE);
                rl3.setVisibility(View.INVISIBLE);
                rl4.setVisibility(View.INVISIBLE);
                rl5.setVisibility(View.INVISIBLE);
                rl6.setVisibility(View.INVISIBLE);
                rl7.setVisibility(View.INVISIBLE);
                rl8.setVisibility(View.INVISIBLE);
                tv_noleg_msg.setVisibility(View.INVISIBLE);
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> map = new HashMap<>();
                map.put("id", legID);
                return map;
            }
        };
        Volley.newRequestQueue(DisplayTeamOLD.this).add(stringRequest);
    }

    public void getUserDataFromDB() {
        progressDialog_legInfo.show();
        StringRequest stringRequest = new StringRequest(Request.Method.POST, BaseUrl.retriveUserDataUrl, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                ib_user1_one.setImageResource(R.drawable.ic_no_leg);
                ib_user1_two.setImageResource(R.drawable.ic_no_leg);
                ib_user1_three.setImageResource(R.drawable.ic_no_leg);
                ib_user1_four.setImageResource(R.drawable.ic_no_leg);
                ib_user1_five.setImageResource(R.drawable.ic_no_leg);
                ib_user1_six.setImageResource(R.drawable.ic_no_leg);
                ib_user1_seven.setImageResource(R.drawable.ic_no_leg);
                ib_user1_eight.setImageResource(R.drawable.ic_no_leg);

                ib_user2_one.setImageResource(R.drawable.ic_no_leg);
                ib_user2_two.setImageResource(R.drawable.ic_no_leg);
                ib_user2_three.setImageResource(R.drawable.ic_no_leg);
                ib_user2_four.setImageResource(R.drawable.ic_no_leg);
                ib_user2_five.setImageResource(R.drawable.ic_no_leg);
                ib_user2_six.setImageResource(R.drawable.ic_no_leg);
                ib_user2_seven.setImageResource(R.drawable.ic_no_leg);
                ib_user2_eight.setImageResource(R.drawable.ic_no_leg);

                ib_user3_one.setImageResource(R.drawable.ic_no_leg);
                ib_user3_two.setImageResource(R.drawable.ic_no_leg);
                ib_user3_three.setImageResource(R.drawable.ic_no_leg);
                ib_user3_four.setImageResource(R.drawable.ic_no_leg);
                ib_user3_five.setImageResource(R.drawable.ic_no_leg);
                ib_user3_six.setImageResource(R.drawable.ic_no_leg);
                ib_user3_seven.setImageResource(R.drawable.ic_no_leg);
                ib_user3_eight.setImageResource(R.drawable.ic_no_leg);

                ib_user4_one.setImageResource(R.drawable.ic_no_leg);
                ib_user4_two.setImageResource(R.drawable.ic_no_leg);
                ib_user4_three.setImageResource(R.drawable.ic_no_leg);
                ib_user4_four.setImageResource(R.drawable.ic_no_leg);
                ib_user4_five.setImageResource(R.drawable.ic_no_leg);
                ib_user4_six.setImageResource(R.drawable.ic_no_leg);
                ib_user4_seven.setImageResource(R.drawable.ic_no_leg);
                ib_user4_eight.setImageResource(R.drawable.ic_no_leg);

                ib_user5_one.setImageResource(R.drawable.ic_no_leg);
                ib_user5_two.setImageResource(R.drawable.ic_no_leg);
                ib_user5_three.setImageResource(R.drawable.ic_no_leg);
                ib_user5_four.setImageResource(R.drawable.ic_no_leg);
                ib_user5_five.setImageResource(R.drawable.ic_no_leg);
                ib_user5_six.setImageResource(R.drawable.ic_no_leg);
                ib_user5_seven.setImageResource(R.drawable.ic_no_leg);
                ib_user5_eight.setImageResource(R.drawable.ic_no_leg);

                ib_user6_one.setImageResource(R.drawable.ic_no_leg);
                ib_user6_two.setImageResource(R.drawable.ic_no_leg);
                ib_user6_three.setImageResource(R.drawable.ic_no_leg);
                ib_user6_four.setImageResource(R.drawable.ic_no_leg);
                ib_user6_five.setImageResource(R.drawable.ic_no_leg);
                ib_user6_six.setImageResource(R.drawable.ic_no_leg);
                ib_user6_seven.setImageResource(R.drawable.ic_no_leg);
                ib_user6_eight.setImageResource(R.drawable.ic_no_leg);

                ib_user7_one.setImageResource(R.drawable.ic_no_leg);
                ib_user7_two.setImageResource(R.drawable.ic_no_leg);
                ib_user7_three.setImageResource(R.drawable.ic_no_leg);
                ib_user7_four.setImageResource(R.drawable.ic_no_leg);
                ib_user7_five.setImageResource(R.drawable.ic_no_leg);
                ib_user7_six.setImageResource(R.drawable.ic_no_leg);
                ib_user7_seven.setImageResource(R.drawable.ic_no_leg);
                ib_user7_eight.setImageResource(R.drawable.ic_no_leg);

                ib_user8_one.setImageResource(R.drawable.ic_no_leg);
                ib_user8_two.setImageResource(R.drawable.ic_no_leg);
                ib_user8_three.setImageResource(R.drawable.ic_no_leg);
                ib_user8_four.setImageResource(R.drawable.ic_no_leg);
                ib_user8_five.setImageResource(R.drawable.ic_no_leg);
                ib_user8_six.setImageResource(R.drawable.ic_no_leg);
                ib_user8_seven.setImageResource(R.drawable.ic_no_leg);
                ib_user8_eight.setImageResource(R.drawable.ic_no_leg);

                if (response.contains("no data found") || response.contains("id is empty") || response.contains("connection not got")) {
                    Toast.makeText(DisplayTeamOLD.this, "no data found.", Toast.LENGTH_SHORT).show();
                    rl1.setVisibility(View.INVISIBLE);
                    rl2.setVisibility(View.INVISIBLE);
                    rl3.setVisibility(View.INVISIBLE);
                    rl4.setVisibility(View.INVISIBLE);
                    rl5.setVisibility(View.INVISIBLE);
                    rl6.setVisibility(View.INVISIBLE);
                    rl7.setVisibility(View.INVISIBLE);
                    rl8.setVisibility(View.INVISIBLE);
                    tv_noleg_msg.setVisibility(View.VISIBLE);
                } else {
                    rl1.setVisibility(View.VISIBLE);
                    rl2.setVisibility(View.VISIBLE);
                    rl3.setVisibility(View.VISIBLE);
                    rl4.setVisibility(View.VISIBLE);
                    rl5.setVisibility(View.VISIBLE);
                    rl6.setVisibility(View.VISIBLE);
                    rl7.setVisibility(View.VISIBLE);
                    rl8.setVisibility(View.VISIBLE);
                    tv_noleg_msg.setVisibility(View.INVISIBLE);

                    String data = response;
                    username = data.substring(data.indexOf("@") + 1, data.indexOf("#"));
//                    tv_userid1.setText(data.substring(data.indexOf("#4A#") + 4, data.indexOf("#5A#")));
//                    tv_name1.setText(data.substring(data.indexOf("#5A#") + 4, data.indexOf("#6A#")));
//                    tv_userid2.setText(data.substring(data.indexOf("#6A#") + 4, data.indexOf("#7A#")));
//                    tv_name2.setText(data.substring(data.indexOf("#7A#") + 4, data.indexOf("#8A#")));
//                    tv_userid3.setText(data.substring(data.indexOf("#8A#") + 4, data.indexOf("#9A#")));
//                    tv_name3.setText(data.substring(data.indexOf("#9A#") + 4, data.indexOf("#10A#")));
//                    tv_userid4.setText(data.substring(data.indexOf("#10A#") + 5, data.indexOf("#11A#")));
//                    tv_name4.setText(data.substring(data.indexOf("#11A#") + 5, data.indexOf("#12A#")));
//                    tv_userid5.setText(data.substring(data.indexOf("#12A#") + 5, data.indexOf("#13A#")));
//                    tv_name5.setText(data.substring(data.indexOf("#13A#") + 5, data.indexOf("#14A#")));
//                    tv_userid6.setText(data.substring(data.indexOf("#14A#") + 5, data.indexOf("#15A#")));
//                    tv_name6.setText(data.substring(data.indexOf("#15A#") + 5, data.indexOf("#16A#")));
//                    tv_userid7.setText(data.substring(data.indexOf("#16A#") + 5, data.indexOf("#17A#")));
//                    tv_name7.setText(data.substring(data.indexOf("#17A#") + 5, data.indexOf("#18A#")));
//                    tv_userid8.setText(data.substring(data.indexOf("#18A#") + 5, data.indexOf("#19A#")));
//                    tv_name8.setText(data.substring(data.indexOf("#19A#") + 5, data.indexOf("#20A#")));
                    String refInfo;
                    if (data.contains("#pn#")) {
                        refInfo = data.substring(data.indexOf("#urls#") + 6, data.indexOf("#pn#"));
                    } else {
                        refInfo = data.substring(data.indexOf("#urls#") + 6,data.indexOf("#88#"));
                    }
                    if (!(refInfo.isEmpty())) {
                        String[] singleUserData = refInfo.split(",");

                        for (int i = 0; i < singleUserData.length; i++) {

                            String[] singleIdUrl_part = singleUserData[i].split("::|:_:");
                            for (int j = 0; j <= 0; j++) {
                                idUrl_map.put(singleIdUrl_part[0], new LegMapModel(singleIdUrl_part[0], singleIdUrl_part[1],
                                        singleIdUrl_part[2]));
                            }
                        }
                    }

                    if (isBackpress) {
                        String tv_leg_names = tv_legs.getText().toString();
                        if (tv_leg_names.contains(">")) {
                            keyword = ">";
                            int index = tv_leg_names.lastIndexOf(keyword);
                            String new_leg_names = tv_leg_names.substring(0, index);
                            tv_legs.setText(new_leg_names);
                        } else {
                        }
                    } else {
                        if (tv_legs.getText().toString().isEmpty()) {
                            tv_legs.setText(username);
                        } else {
                            String str = tv_legs.getText().toString() + " > " + username;
                            tv_legs.setText(str);
                        }
                    }

                    if (tv_name1.getText().toString().isEmpty()) {
                        rl1.setVisibility(View.GONE);
                        isHavingLeg = false;
                    } else {
                        rl1.setVisibility(View.VISIBLE);
                        String isPaid = idUrl_map.get(tv_userid1.getText().toString()).isPaid;

                        if (isPaid.equals("yes")) {
                            tv_name1.setTextColor(Color.GREEN);
                            tv_userid1.setTextColor(Color.GREEN);
                        } else if (isPaid.equals("no")) {
                            tv_name1.setTextColor(Color.RED);
                            tv_userid1.setTextColor(Color.RED);
                        }
                        Glide.with(DisplayTeamOLD.this)
                                .load(idUrl_map.get(tv_userid1.getText().toString()).image_url)
                                .into(profile_img1);
                        isHavingLeg = true;
                    }

                    if (tv_name2.getText().toString().isEmpty()) {
                        rl2.setVisibility(View.GONE);
                    } else {
                        rl2.setVisibility(View.VISIBLE);
                        String isPaid = idUrl_map.get(tv_userid2.getText().toString()).isPaid;

                        if (isPaid.equals("yes")) {
                            tv_name2.setTextColor(Color.GREEN);
                            tv_userid2.setTextColor(Color.GREEN);
                        } else if (isPaid.equals("no")) {
                            tv_name2.setTextColor(Color.RED);
                            tv_userid2.setTextColor(Color.RED);
                        }
                        Glide.with(DisplayTeamOLD.this)
                                .load(idUrl_map.get(tv_userid2.getText().toString()).image_url)
                                .into(profile_img2);
                    }

                    if (tv_name3.getText().toString().isEmpty()) {
                        rl3.setVisibility(View.GONE);
                    } else {
                        rl3.setVisibility(View.VISIBLE);
                        String isPaid = idUrl_map.get(tv_userid3.getText().toString()).isPaid;

                        if (isPaid.equals("yes")) {
                            tv_name3.setTextColor(Color.GREEN);
                            tv_userid3.setTextColor(Color.GREEN);
                        } else if (isPaid.equals("no")) {
                            tv_name3.setTextColor(Color.RED);
                            tv_userid3.setTextColor(Color.RED);
                        }

                        Glide.with(DisplayTeamOLD.this)
                                .load(idUrl_map.get(tv_userid3.getText().toString()).image_url)
                                .into(profile_img3);
                    }

                    if (tv_name4.getText().toString().isEmpty()) {
                        rl4.setVisibility(View.GONE);
                    } else {
                        rl4.setVisibility(View.VISIBLE);
                        String isPaid = idUrl_map.get(tv_userid4.getText().toString()).isPaid;

                        if (isPaid.equals("yes")) {
                            tv_name4.setTextColor(Color.GREEN);
                            tv_userid4.setTextColor(Color.GREEN);
                        } else if (isPaid.equals("no")) {
                            tv_name4.setTextColor(Color.RED);
                            tv_userid4.setTextColor(Color.RED);
                        }
                        Glide.with(DisplayTeamOLD.this)
                                .load(idUrl_map.get(tv_userid4.getText().toString()).image_url)
                                .into(profile_img4);
                    }

                    if (tv_name5.getText().toString().isEmpty()) {
                        rl5.setVisibility(View.GONE);
                    } else {
                        rl5.setVisibility(View.VISIBLE);
                        String isPaid = idUrl_map.get(tv_userid5.getText().toString()).isPaid;

                        if (isPaid.equals("yes")) {
                            tv_name5.setTextColor(Color.GREEN);
                            tv_userid5.setTextColor(Color.GREEN);
                        } else if (isPaid.equals("no")) {
                            tv_name5.setTextColor(Color.RED);
                            tv_userid5.setTextColor(Color.RED);
                        }

                        Glide.with(DisplayTeamOLD.this)
                                .load(idUrl_map.get(tv_userid5.getText().toString()).image_url)
                                .into(profile_img5);
                    }

                    if (tv_name6.getText().toString().isEmpty()) {
                        rl6.setVisibility(View.GONE);
                    } else {
                        rl6.setVisibility(View.VISIBLE);
                        String isPaid = idUrl_map.get(tv_userid6.getText().toString()).isPaid;

                        if (isPaid.equals("yes")) {
                            tv_name6.setTextColor(Color.GREEN);
                            tv_userid6.setTextColor(Color.GREEN);
                        } else if (isPaid.equals("no")) {
                            tv_name6.setTextColor(Color.RED);
                            tv_userid6.setTextColor(Color.RED);
                        }

                        Glide.with(DisplayTeamOLD.this)
                                .load(idUrl_map.get(tv_userid6.getText().toString()).image_url)
                                .into(profile_img6);
                    }

                    if (tv_name7.getText().toString().isEmpty()) {
                        rl7.setVisibility(View.GONE);
                    } else {
                        rl7.setVisibility(View.VISIBLE);
                        String isPaid = idUrl_map.get(tv_userid7.getText().toString()).isPaid;

                        if (isPaid.equals("yes")) {
                            tv_name7.setTextColor(Color.GREEN);
                            tv_userid7.setTextColor(Color.GREEN);
                        } else if (isPaid.equals("no")) {
                            tv_name7.setTextColor(Color.RED);
                            tv_userid7.setTextColor(Color.RED);
                        }

                        Glide.with(DisplayTeamOLD.this)
                                .load(idUrl_map.get(tv_userid7.getText().toString()).image_url)
                                .into(profile_img7);
                    }
                    if (tv_name8.getText().toString().isEmpty()) {
                        rl8.setVisibility(View.GONE);
                    } else {
                        rl8.setVisibility(View.VISIBLE);
                        String isPaid = idUrl_map.get(tv_userid8.getText().toString()).isPaid;

                        if (isPaid.equals("yes")) {
                            tv_name8.setTextColor(Color.GREEN);
                            tv_userid8.setTextColor(Color.GREEN);
                        } else if (isPaid.equals("no")) {
                            tv_name8.setTextColor(Color.RED);
                            tv_userid8.setTextColor(Color.RED);
                        }

                        Glide.with(DisplayTeamOLD.this)
                                .load(idUrl_map.get(tv_userid8.getText().toString()).image_url)
                                .into(profile_img8);
                    }

                    if (isHavingLeg) {
                        tv_noleg_msg.setVisibility(View.INVISIBLE);
                    } else {
                        tv_noleg_msg.setVisibility(View.VISIBLE);
                    }
                }
                setIspaidImage();
                progressDialog_legInfo.dismiss();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(DisplayTeamOLD.this, "Something went wrong.", Toast.LENGTH_SHORT).show();

                rl1.setVisibility(View.INVISIBLE);
                rl2.setVisibility(View.INVISIBLE);
                rl3.setVisibility(View.INVISIBLE);
                rl4.setVisibility(View.INVISIBLE);
                rl5.setVisibility(View.INVISIBLE);
                rl6.setVisibility(View.INVISIBLE);
                rl7.setVisibility(View.INVISIBLE);
                rl8.setVisibility(View.INVISIBLE);
                tv_noleg_msg.setVisibility(View.VISIBLE);

                progressDialog_legInfo.dismiss();
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> map = new HashMap<>();
                map.put("id", id);
                return map;
            }
        };

        Volley.newRequestQueue(DisplayTeamOLD.this).add(stringRequest);
    }

    public void setIspaidImage() {

        progressDialog_legInfo.show();
        StringRequest stringRequest = new StringRequest(Request.Method.POST, BaseUrl.ktc_get_additional_info_tree, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                progressDialog_legInfo.dismiss();
/*
#M1##LID#70451100951#INF##t0#7#t1#1#t2##EINF#
    #LID#74002837931#INF##t0#1#t1#1#t2##EINF#
    #LID#77380225151#INF##t0#3#t1#0#t2##EINF#
    #LID#80976879291#INF##t0#6#t1#0#t2##EINF#
    #LID#90055560321#INF##t0#2#t1#3#t2##EINF#
    #LID#97681874971#INF##t0#8#t1#0#t2##EINF#
    #LID#98197474611#INF##t0#8#t1#0#t2##EINF#
    #LID#98675504021#INF##t0#2#t1#0#t2##EINF#
 */
                if (response.contains("#M2#")) {
                    String one = response.substring(response.indexOf("#L1#") + 4, response.indexOf("#L2#"));
                    String two = response.substring(response.indexOf("#L2#") + 4, response.indexOf("#L3#"));
                    String three = response.substring(response.indexOf("#L3#") + 4, response.indexOf("#L4#"));
                    String four = response.substring(response.indexOf("#L4#") + 4, response.indexOf("#L5#"));
                    String five = response.substring(response.indexOf("#L5#") + 4, response.indexOf("#L6#"));
                    String six = response.substring(response.indexOf("#L6#") + 4, response.indexOf("#L7#"));
                    String seven = response.substring(response.indexOf("#L7#") + 4, response.indexOf("#L8#"));
                    String eight = response.substring(response.indexOf("#L8#") + 4);

                    if (one.contains("t0")) {
                        int paidLegCount = Integer.parseInt(one.substring(one.indexOf("#t0#") + 4, one.indexOf("#t1#")));
                        int unpaidLegCount = Integer.parseInt(one.substring(one.indexOf("#t1#") + 4, one.indexOf("#t2#")));
                        tv_direct_ref_1.setText("Direct ref:- " + one.substring(one.indexOf("#t2#") + 4, one.indexOf("#t3#")));

                        switch (paidLegCount) {
                            case 0:
                                switch (unpaidLegCount) {
                                    case 0:
                                        break;

                                    case 1:
                                        ib_user1_one.setImageResource(R.drawable.radio_red_color);
                                        break;

                                    case 2:
                                        ib_user1_one.setImageResource(R.drawable.radio_red_color);
                                        ib_user1_two.setImageResource(R.drawable.radio_red_color);
                                        break;

                                    case 3:
                                        ib_user1_one.setImageResource(R.drawable.radio_red_color);
                                        ib_user1_two.setImageResource(R.drawable.radio_red_color);
                                        ib_user1_three.setImageResource(R.drawable.radio_red_color);
                                        break;

                                    case 4:
                                        ib_user1_one.setImageResource(R.drawable.radio_red_color);
                                        ib_user1_two.setImageResource(R.drawable.radio_red_color);
                                        ib_user1_three.setImageResource(R.drawable.radio_red_color);
                                        ib_user1_four.setImageResource(R.drawable.radio_red_color);
                                        break;

                                    case 5:
                                        ib_user1_one.setImageResource(R.drawable.radio_red_color);
                                        ib_user1_two.setImageResource(R.drawable.radio_red_color);
                                        ib_user1_three.setImageResource(R.drawable.radio_red_color);
                                        ib_user1_four.setImageResource(R.drawable.radio_red_color);
                                        ib_user1_five.setImageResource(R.drawable.radio_red_color);
                                        break;

                                    case 6:
                                        ib_user1_one.setImageResource(R.drawable.radio_red_color);
                                        ib_user1_two.setImageResource(R.drawable.radio_red_color);
                                        ib_user1_three.setImageResource(R.drawable.radio_red_color);
                                        ib_user1_four.setImageResource(R.drawable.radio_red_color);
                                        ib_user1_five.setImageResource(R.drawable.radio_red_color);
                                        ib_user1_six.setImageResource(R.drawable.radio_red_color);
                                        break;

                                    case 7:
                                        ib_user1_one.setImageResource(R.drawable.radio_red_color);
                                        ib_user1_two.setImageResource(R.drawable.radio_red_color);
                                        ib_user1_three.setImageResource(R.drawable.radio_red_color);
                                        ib_user1_four.setImageResource(R.drawable.radio_red_color);
                                        ib_user1_five.setImageResource(R.drawable.radio_red_color);
                                        ib_user1_six.setImageResource(R.drawable.radio_red_color);
                                        ib_user1_seven.setImageResource(R.drawable.radio_red_color);
                                        break;

                                    case 8:
                                        ib_user1_one.setImageResource(R.drawable.radio_red_color);
                                        ib_user1_two.setImageResource(R.drawable.radio_red_color);
                                        ib_user1_three.setImageResource(R.drawable.radio_red_color);
                                        ib_user1_four.setImageResource(R.drawable.radio_red_color);
                                        ib_user1_five.setImageResource(R.drawable.radio_red_color);
                                        ib_user1_six.setImageResource(R.drawable.radio_red_color);
                                        ib_user1_seven.setImageResource(R.drawable.radio_red_color);
                                        ib_user1_eight.setImageResource(R.drawable.radio_red_color);
                                        break;
                                }
                                break;

                            case 1:
                                ib_user1_one.setImageResource(R.drawable.radio_green_color);

                                switch (unpaidLegCount) {
                                    case 0:
                                        break;

                                    case 1:
                                        ib_user1_two.setImageResource(R.drawable.radio_red_color);
                                        break;

                                    case 2:
                                        ib_user1_two.setImageResource(R.drawable.radio_red_color);
                                        ib_user1_three.setImageResource(R.drawable.radio_red_color);
                                        break;

                                    case 3:
                                        ib_user1_two.setImageResource(R.drawable.radio_red_color);
                                        ib_user1_three.setImageResource(R.drawable.radio_red_color);
                                        ib_user1_four.setImageResource(R.drawable.radio_red_color);
                                        break;

                                    case 4:
                                        ib_user1_two.setImageResource(R.drawable.radio_red_color);
                                        ib_user1_three.setImageResource(R.drawable.radio_red_color);
                                        ib_user1_four.setImageResource(R.drawable.radio_red_color);
                                        ib_user1_five.setImageResource(R.drawable.radio_red_color);
                                        break;

                                    case 5:
                                        ib_user1_two.setImageResource(R.drawable.radio_red_color);
                                        ib_user1_three.setImageResource(R.drawable.radio_red_color);
                                        ib_user1_four.setImageResource(R.drawable.radio_red_color);
                                        ib_user1_five.setImageResource(R.drawable.radio_red_color);
                                        ib_user1_six.setImageResource(R.drawable.radio_red_color);
                                        break;

                                    case 6:
                                        ib_user1_two.setImageResource(R.drawable.radio_red_color);
                                        ib_user1_three.setImageResource(R.drawable.radio_red_color);
                                        ib_user1_four.setImageResource(R.drawable.radio_red_color);
                                        ib_user1_five.setImageResource(R.drawable.radio_red_color);
                                        ib_user1_six.setImageResource(R.drawable.radio_red_color);
                                        ib_user1_seven.setImageResource(R.drawable.radio_red_color);
                                        break;

                                    case 7:
                                        ib_user1_two.setImageResource(R.drawable.radio_red_color);
                                        ib_user1_three.setImageResource(R.drawable.radio_red_color);
                                        ib_user1_four.setImageResource(R.drawable.radio_red_color);
                                        ib_user1_five.setImageResource(R.drawable.radio_red_color);
                                        ib_user1_six.setImageResource(R.drawable.radio_red_color);
                                        ib_user1_seven.setImageResource(R.drawable.radio_red_color);
                                        ib_user1_eight.setImageResource(R.drawable.radio_red_color);
                                        break;
                                }
                                break;

                            case 2:
                                ib_user1_one.setImageResource(R.drawable.radio_green_color);
                                ib_user1_two.setImageResource(R.drawable.radio_green_color);

                                switch (unpaidLegCount) {
                                    case 0:
                                        break;

                                    case 1:
                                        ib_user1_three.setImageResource(R.drawable.radio_red_color);
                                        break;

                                    case 2:
                                        ib_user1_three.setImageResource(R.drawable.radio_red_color);
                                        ib_user1_four.setImageResource(R.drawable.radio_red_color);
                                        break;

                                    case 3:
                                        ib_user1_three.setImageResource(R.drawable.radio_red_color);
                                        ib_user1_four.setImageResource(R.drawable.radio_red_color);
                                        ib_user1_five.setImageResource(R.drawable.radio_red_color);
                                        break;

                                    case 4:
                                        ib_user1_three.setImageResource(R.drawable.radio_red_color);
                                        ib_user1_four.setImageResource(R.drawable.radio_red_color);
                                        ib_user1_five.setImageResource(R.drawable.radio_red_color);
                                        ib_user1_six.setImageResource(R.drawable.radio_red_color);
                                        break;

                                    case 5:
                                        ib_user1_three.setImageResource(R.drawable.radio_red_color);
                                        ib_user1_four.setImageResource(R.drawable.radio_red_color);
                                        ib_user1_five.setImageResource(R.drawable.radio_red_color);
                                        ib_user1_six.setImageResource(R.drawable.radio_red_color);
                                        ib_user1_seven.setImageResource(R.drawable.radio_red_color);
                                        break;

                                    case 6:
                                        ib_user1_three.setImageResource(R.drawable.radio_red_color);
                                        ib_user1_four.setImageResource(R.drawable.radio_red_color);
                                        ib_user1_five.setImageResource(R.drawable.radio_red_color);
                                        ib_user1_six.setImageResource(R.drawable.radio_red_color);
                                        ib_user1_seven.setImageResource(R.drawable.radio_red_color);
                                        ib_user1_eight.setImageResource(R.drawable.radio_red_color);
                                        break;
                                }
                                break;

                            case 3:
                                ib_user1_one.setImageResource(R.drawable.radio_green_color);
                                ib_user1_two.setImageResource(R.drawable.radio_green_color);
                                ib_user1_three.setImageResource(R.drawable.radio_green_color);

                                switch (unpaidLegCount) {
                                    case 0:
                                        break;

                                    case 1:
                                        ib_user1_four.setImageResource(R.drawable.radio_red_color);
                                        break;

                                    case 2:
                                        ib_user1_four.setImageResource(R.drawable.radio_red_color);
                                        ib_user1_five.setImageResource(R.drawable.radio_red_color);
                                        break;

                                    case 3:
                                        ib_user1_four.setImageResource(R.drawable.radio_red_color);
                                        ib_user1_five.setImageResource(R.drawable.radio_red_color);
                                        ib_user1_six.setImageResource(R.drawable.radio_red_color);
                                        break;

                                    case 4:
                                        ib_user1_four.setImageResource(R.drawable.radio_red_color);
                                        ib_user1_five.setImageResource(R.drawable.radio_red_color);
                                        ib_user1_six.setImageResource(R.drawable.radio_red_color);
                                        ib_user1_seven.setImageResource(R.drawable.radio_red_color);
                                        break;

                                    case 5:
                                        ib_user1_four.setImageResource(R.drawable.radio_red_color);
                                        ib_user1_five.setImageResource(R.drawable.radio_red_color);
                                        ib_user1_six.setImageResource(R.drawable.radio_red_color);
                                        ib_user1_seven.setImageResource(R.drawable.radio_red_color);
                                        ib_user1_eight.setImageResource(R.drawable.radio_red_color);
                                        break;
                                }
                                break;

                            case 4:
                                ib_user1_one.setImageResource(R.drawable.radio_green_color);
                                ib_user1_two.setImageResource(R.drawable.radio_green_color);
                                ib_user1_three.setImageResource(R.drawable.radio_green_color);
                                ib_user1_four.setImageResource(R.drawable.radio_green_color);

                                switch (unpaidLegCount) {
                                    case 0:
                                        break;

                                    case 1:
                                        ib_user1_five.setImageResource(R.drawable.radio_red_color);
                                        break;

                                    case 2:
                                        ib_user1_five.setImageResource(R.drawable.radio_red_color);
                                        ib_user1_six.setImageResource(R.drawable.radio_red_color);
                                        break;

                                    case 3:
                                        ib_user1_five.setImageResource(R.drawable.radio_red_color);
                                        ib_user1_six.setImageResource(R.drawable.radio_red_color);
                                        ib_user1_seven.setImageResource(R.drawable.radio_red_color);
                                        break;

                                    case 4:
                                        ib_user1_five.setImageResource(R.drawable.radio_red_color);
                                        ib_user1_six.setImageResource(R.drawable.radio_red_color);
                                        ib_user1_seven.setImageResource(R.drawable.radio_red_color);
                                        ib_user1_eight.setImageResource(R.drawable.radio_red_color);
                                        break;
                                }
                                break;

                            case 5:
                                ib_user1_one.setImageResource(R.drawable.radio_green_color);
                                ib_user1_two.setImageResource(R.drawable.radio_green_color);
                                ib_user1_three.setImageResource(R.drawable.radio_green_color);
                                ib_user1_four.setImageResource(R.drawable.radio_green_color);
                                ib_user1_five.setImageResource(R.drawable.radio_green_color);

                                switch (unpaidLegCount) {
                                    case 0:
                                        break;
                                    case 1:
                                        ib_user1_six.setImageResource(R.drawable.radio_red_color);
                                        break;

                                    case 2:
                                        ib_user1_six.setImageResource(R.drawable.radio_red_color);
                                        ib_user1_seven.setImageResource(R.drawable.radio_red_color);
                                        break;

                                    case 3:
                                        ib_user1_six.setImageResource(R.drawable.radio_red_color);
                                        ib_user1_seven.setImageResource(R.drawable.radio_red_color);
                                        ib_user1_eight.setImageResource(R.drawable.radio_red_color);
                                        break;
                                }
                                break;

                            case 6:
                                ib_user1_one.setImageResource(R.drawable.radio_green_color);
                                ib_user1_two.setImageResource(R.drawable.radio_green_color);
                                ib_user1_three.setImageResource(R.drawable.radio_green_color);
                                ib_user1_four.setImageResource(R.drawable.radio_green_color);
                                ib_user1_five.setImageResource(R.drawable.radio_green_color);
                                ib_user1_six.setImageResource(R.drawable.radio_green_color);

                                switch (unpaidLegCount) {
                                    case 0:
                                        break;

                                    case 1:
                                        ib_user1_seven.setImageResource(R.drawable.radio_red_color);
                                        break;

                                    case 2:
                                        ib_user1_seven.setImageResource(R.drawable.radio_red_color);
                                        ib_user1_eight.setImageResource(R.drawable.radio_red_color);
                                        break;
                                }
                                break;

                            case 7:
                                ib_user1_one.setImageResource(R.drawable.radio_green_color);
                                ib_user1_two.setImageResource(R.drawable.radio_green_color);
                                ib_user1_three.setImageResource(R.drawable.radio_green_color);
                                ib_user1_four.setImageResource(R.drawable.radio_green_color);
                                ib_user1_five.setImageResource(R.drawable.radio_green_color);
                                ib_user1_six.setImageResource(R.drawable.radio_green_color);
                                ib_user1_seven.setImageResource(R.drawable.radio_green_color);

                                switch (unpaidLegCount) {
                                    case 0:
                                        break;

                                    case 1:
                                        ib_user1_eight.setImageResource(R.drawable.radio_red_color);
                                        break;
                                }
                                break;

                            case 8:
                                ib_user1_one.setImageResource(R.drawable.radio_green_color);
                                ib_user1_two.setImageResource(R.drawable.radio_green_color);
                                ib_user1_three.setImageResource(R.drawable.radio_green_color);
                                ib_user1_four.setImageResource(R.drawable.radio_green_color);
                                ib_user1_five.setImageResource(R.drawable.radio_green_color);
                                ib_user1_six.setImageResource(R.drawable.radio_green_color);
                                ib_user1_seven.setImageResource(R.drawable.radio_green_color);
                                ib_user1_eight.setImageResource(R.drawable.radio_green_color);

                                switch (unpaidLegCount) {
                                    case 0:
                                        break;
                                }
                                break;

                        }
                    }

                    //////////////// second user data set
                    if (two.contains("t0")) {
                        int paidLegCount = Integer.parseInt(two.substring(two.indexOf("#t0#") + 4, two.indexOf("#t1#")));
                        int unpaidLegCount = Integer.parseInt(two.substring(two.indexOf("#t1#") + 4, two.indexOf("#t2#")));
                        tv_direct_ref_2.setText("Direct ref:- " + two.substring(two.indexOf("#t2#") + 4, two.indexOf("#t3#")));

                        switch (paidLegCount) {
                            case 0:
                                switch (unpaidLegCount) {
                                    case 0:
                                        break;

                                    case 1:
                                        ib_user2_one.setImageResource(R.drawable.radio_red_color);
                                        break;

                                    case 2:
                                        ib_user2_one.setImageResource(R.drawable.radio_red_color);
                                        ib_user2_two.setImageResource(R.drawable.radio_red_color);
                                        break;

                                    case 3:
                                        ib_user2_one.setImageResource(R.drawable.radio_red_color);
                                        ib_user2_two.setImageResource(R.drawable.radio_red_color);
                                        ib_user2_three.setImageResource(R.drawable.radio_red_color);
                                        break;

                                    case 4:
                                        ib_user2_one.setImageResource(R.drawable.radio_red_color);
                                        ib_user2_two.setImageResource(R.drawable.radio_red_color);
                                        ib_user2_three.setImageResource(R.drawable.radio_red_color);
                                        ib_user2_four.setImageResource(R.drawable.radio_red_color);
                                        break;

                                    case 5:
                                        ib_user2_one.setImageResource(R.drawable.radio_red_color);
                                        ib_user2_two.setImageResource(R.drawable.radio_red_color);
                                        ib_user2_three.setImageResource(R.drawable.radio_red_color);
                                        ib_user2_four.setImageResource(R.drawable.radio_red_color);
                                        ib_user2_five.setImageResource(R.drawable.radio_red_color);
                                        break;

                                    case 6:
                                        ib_user2_one.setImageResource(R.drawable.radio_red_color);
                                        ib_user2_two.setImageResource(R.drawable.radio_red_color);
                                        ib_user2_three.setImageResource(R.drawable.radio_red_color);
                                        ib_user2_four.setImageResource(R.drawable.radio_red_color);
                                        ib_user2_five.setImageResource(R.drawable.radio_red_color);
                                        ib_user2_six.setImageResource(R.drawable.radio_red_color);
                                        break;

                                    case 7:
                                        ib_user2_one.setImageResource(R.drawable.radio_red_color);
                                        ib_user2_two.setImageResource(R.drawable.radio_red_color);
                                        ib_user2_three.setImageResource(R.drawable.radio_red_color);
                                        ib_user2_four.setImageResource(R.drawable.radio_red_color);
                                        ib_user2_five.setImageResource(R.drawable.radio_red_color);
                                        ib_user2_six.setImageResource(R.drawable.radio_red_color);
                                        ib_user2_seven.setImageResource(R.drawable.radio_red_color);
                                        break;

                                    case 8:
                                        ib_user2_one.setImageResource(R.drawable.radio_red_color);
                                        ib_user2_two.setImageResource(R.drawable.radio_red_color);
                                        ib_user2_three.setImageResource(R.drawable.radio_red_color);
                                        ib_user2_four.setImageResource(R.drawable.radio_red_color);
                                        ib_user2_five.setImageResource(R.drawable.radio_red_color);
                                        ib_user2_six.setImageResource(R.drawable.radio_red_color);
                                        ib_user2_seven.setImageResource(R.drawable.radio_red_color);
                                        ib_user2_eight.setImageResource(R.drawable.radio_red_color);
                                        break;
                                }
                                break;

                            case 1:
                                ib_user2_one.setImageResource(R.drawable.radio_green_color);

                                switch (unpaidLegCount) {
                                    case 0:
                                        break;

                                    case 1:
                                        ib_user2_two.setImageResource(R.drawable.radio_red_color);
                                        break;

                                    case 2:
                                        ib_user2_two.setImageResource(R.drawable.radio_red_color);
                                        ib_user2_three.setImageResource(R.drawable.radio_red_color);
                                        break;

                                    case 3:
                                        ib_user2_two.setImageResource(R.drawable.radio_red_color);
                                        ib_user2_three.setImageResource(R.drawable.radio_red_color);
                                        ib_user2_four.setImageResource(R.drawable.radio_red_color);
                                        break;

                                    case 4:
                                        ib_user2_two.setImageResource(R.drawable.radio_red_color);
                                        ib_user2_three.setImageResource(R.drawable.radio_red_color);
                                        ib_user2_four.setImageResource(R.drawable.radio_red_color);
                                        ib_user2_five.setImageResource(R.drawable.radio_red_color);
                                        break;

                                    case 5:
                                        ib_user2_two.setImageResource(R.drawable.radio_red_color);
                                        ib_user2_three.setImageResource(R.drawable.radio_red_color);
                                        ib_user2_four.setImageResource(R.drawable.radio_red_color);
                                        ib_user2_five.setImageResource(R.drawable.radio_red_color);
                                        ib_user2_six.setImageResource(R.drawable.radio_red_color);
                                        break;

                                    case 6:
                                        ib_user2_two.setImageResource(R.drawable.radio_red_color);
                                        ib_user2_three.setImageResource(R.drawable.radio_red_color);
                                        ib_user2_four.setImageResource(R.drawable.radio_red_color);
                                        ib_user2_five.setImageResource(R.drawable.radio_red_color);
                                        ib_user2_six.setImageResource(R.drawable.radio_red_color);
                                        ib_user2_seven.setImageResource(R.drawable.radio_red_color);
                                        break;

                                    case 7:
                                        ib_user2_two.setImageResource(R.drawable.radio_red_color);
                                        ib_user2_three.setImageResource(R.drawable.radio_red_color);
                                        ib_user2_four.setImageResource(R.drawable.radio_red_color);
                                        ib_user2_five.setImageResource(R.drawable.radio_red_color);
                                        ib_user2_six.setImageResource(R.drawable.radio_red_color);
                                        ib_user2_seven.setImageResource(R.drawable.radio_red_color);
                                        ib_user2_eight.setImageResource(R.drawable.radio_red_color);
                                        break;
                                }
                                break;

                            case 2:
                                ib_user2_one.setImageResource(R.drawable.radio_green_color);
                                ib_user2_two.setImageResource(R.drawable.radio_green_color);

                                switch (unpaidLegCount) {
                                    case 0:
                                        break;

                                    case 1:
                                        ib_user2_three.setImageResource(R.drawable.radio_red_color);
                                        break;

                                    case 2:
                                        ib_user2_three.setImageResource(R.drawable.radio_red_color);
                                        ib_user2_four.setImageResource(R.drawable.radio_red_color);
                                        break;

                                    case 3:
                                        ib_user2_three.setImageResource(R.drawable.radio_red_color);
                                        ib_user2_four.setImageResource(R.drawable.radio_red_color);
                                        ib_user2_five.setImageResource(R.drawable.radio_red_color);
                                        break;

                                    case 4:
                                        ib_user2_three.setImageResource(R.drawable.radio_red_color);
                                        ib_user2_four.setImageResource(R.drawable.radio_red_color);
                                        ib_user2_five.setImageResource(R.drawable.radio_red_color);
                                        ib_user2_six.setImageResource(R.drawable.radio_red_color);
                                        break;

                                    case 5:
                                        ib_user2_three.setImageResource(R.drawable.radio_red_color);
                                        ib_user2_four.setImageResource(R.drawable.radio_red_color);
                                        ib_user2_five.setImageResource(R.drawable.radio_red_color);
                                        ib_user2_six.setImageResource(R.drawable.radio_red_color);
                                        ib_user2_seven.setImageResource(R.drawable.radio_red_color);
                                        break;

                                    case 6:
                                        ib_user2_three.setImageResource(R.drawable.radio_red_color);
                                        ib_user2_four.setImageResource(R.drawable.radio_red_color);
                                        ib_user2_five.setImageResource(R.drawable.radio_red_color);
                                        ib_user2_six.setImageResource(R.drawable.radio_red_color);
                                        ib_user2_seven.setImageResource(R.drawable.radio_red_color);
                                        ib_user2_eight.setImageResource(R.drawable.radio_red_color);
                                        break;
                                }
                                break;

                            case 3:
                                ib_user2_one.setImageResource(R.drawable.radio_green_color);
                                ib_user2_two.setImageResource(R.drawable.radio_green_color);
                                ib_user2_three.setImageResource(R.drawable.radio_green_color);

                                switch (unpaidLegCount) {
                                    case 0:
                                        break;

                                    case 1:
                                        ib_user2_four.setImageResource(R.drawable.radio_red_color);
                                        break;

                                    case 2:
                                        ib_user2_four.setImageResource(R.drawable.radio_red_color);
                                        ib_user2_five.setImageResource(R.drawable.radio_red_color);
                                        break;

                                    case 3:
                                        ib_user2_four.setImageResource(R.drawable.radio_red_color);
                                        ib_user2_five.setImageResource(R.drawable.radio_red_color);
                                        ib_user2_six.setImageResource(R.drawable.radio_red_color);
                                        break;

                                    case 4:
                                        ib_user2_four.setImageResource(R.drawable.radio_red_color);
                                        ib_user2_five.setImageResource(R.drawable.radio_red_color);
                                        ib_user2_six.setImageResource(R.drawable.radio_red_color);
                                        ib_user2_seven.setImageResource(R.drawable.radio_red_color);
                                        break;

                                    case 5:
                                        ib_user2_four.setImageResource(R.drawable.radio_red_color);
                                        ib_user2_five.setImageResource(R.drawable.radio_red_color);
                                        ib_user2_six.setImageResource(R.drawable.radio_red_color);
                                        ib_user2_seven.setImageResource(R.drawable.radio_red_color);
                                        ib_user2_eight.setImageResource(R.drawable.radio_red_color);
                                        break;
                                }
                                break;

                            case 4:
                                ib_user2_one.setImageResource(R.drawable.radio_green_color);
                                ib_user2_two.setImageResource(R.drawable.radio_green_color);
                                ib_user2_three.setImageResource(R.drawable.radio_green_color);
                                ib_user2_four.setImageResource(R.drawable.radio_green_color);

                                switch (unpaidLegCount) {
                                    case 0:
                                        break;

                                    case 1:
                                        ib_user2_five.setImageResource(R.drawable.radio_red_color);
                                        break;

                                    case 2:
                                        ib_user2_five.setImageResource(R.drawable.radio_red_color);
                                        ib_user2_six.setImageResource(R.drawable.radio_red_color);
                                        break;

                                    case 3:
                                        ib_user2_five.setImageResource(R.drawable.radio_red_color);
                                        ib_user2_six.setImageResource(R.drawable.radio_red_color);
                                        ib_user2_seven.setImageResource(R.drawable.radio_red_color);
                                        break;

                                    case 4:
                                        ib_user2_five.setImageResource(R.drawable.radio_red_color);
                                        ib_user2_six.setImageResource(R.drawable.radio_red_color);
                                        ib_user2_seven.setImageResource(R.drawable.radio_red_color);
                                        ib_user2_eight.setImageResource(R.drawable.radio_red_color);
                                        break;
                                }
                                break;

                            case 5:
                                ib_user2_one.setImageResource(R.drawable.radio_green_color);
                                ib_user2_two.setImageResource(R.drawable.radio_green_color);
                                ib_user2_three.setImageResource(R.drawable.radio_green_color);
                                ib_user2_four.setImageResource(R.drawable.radio_green_color);
                                ib_user2_five.setImageResource(R.drawable.radio_green_color);

                                switch (unpaidLegCount) {
                                    case 0:
                                        break;
                                    case 1:
                                        ib_user2_six.setImageResource(R.drawable.radio_red_color);
                                        break;

                                    case 2:
                                        ib_user2_six.setImageResource(R.drawable.radio_red_color);
                                        ib_user2_seven.setImageResource(R.drawable.radio_red_color);
                                        break;

                                    case 3:
                                        ib_user2_six.setImageResource(R.drawable.radio_red_color);
                                        ib_user2_seven.setImageResource(R.drawable.radio_red_color);
                                        ib_user2_eight.setImageResource(R.drawable.radio_red_color);
                                        break;
                                }
                                break;

                            case 6:
                                ib_user2_one.setImageResource(R.drawable.radio_green_color);
                                ib_user2_two.setImageResource(R.drawable.radio_green_color);
                                ib_user2_three.setImageResource(R.drawable.radio_green_color);
                                ib_user2_four.setImageResource(R.drawable.radio_green_color);
                                ib_user2_five.setImageResource(R.drawable.radio_green_color);
                                ib_user2_six.setImageResource(R.drawable.radio_green_color);

                                switch (unpaidLegCount) {
                                    case 0:
                                        break;

                                    case 1:
                                        ib_user2_seven.setImageResource(R.drawable.radio_red_color);
                                        break;

                                    case 2:
                                        ib_user2_seven.setImageResource(R.drawable.radio_red_color);
                                        ib_user2_eight.setImageResource(R.drawable.radio_red_color);
                                        break;
                                }
                                break;

                            case 7:
                                ib_user2_one.setImageResource(R.drawable.radio_green_color);
                                ib_user2_two.setImageResource(R.drawable.radio_green_color);
                                ib_user2_three.setImageResource(R.drawable.radio_green_color);
                                ib_user2_four.setImageResource(R.drawable.radio_green_color);
                                ib_user2_five.setImageResource(R.drawable.radio_green_color);
                                ib_user2_six.setImageResource(R.drawable.radio_green_color);
                                ib_user2_seven.setImageResource(R.drawable.radio_green_color);

                                switch (unpaidLegCount) {
                                    case 0:
                                        break;

                                    case 1:
                                        ib_user2_eight.setImageResource(R.drawable.radio_red_color);
                                        break;
                                }
                                break;

                            case 8:
                                ib_user2_one.setImageResource(R.drawable.radio_green_color);
                                ib_user2_two.setImageResource(R.drawable.radio_green_color);
                                ib_user2_three.setImageResource(R.drawable.radio_green_color);
                                ib_user2_four.setImageResource(R.drawable.radio_green_color);
                                ib_user2_five.setImageResource(R.drawable.radio_green_color);
                                ib_user2_six.setImageResource(R.drawable.radio_green_color);
                                ib_user2_seven.setImageResource(R.drawable.radio_green_color);
                                ib_user2_eight.setImageResource(R.drawable.radio_green_color);

                                switch (unpaidLegCount) {
                                    case 0:
                                        break;
                                }
                                break;

                        }
                    }

                    // third user data-set
                    if (three.contains("t0")) {
                        int paidLegCount = Integer.parseInt(three.substring(three.indexOf("#t0#") + 4, three.indexOf("#t1#")));
                        int unpaidLegCount = Integer.parseInt(three.substring(three.indexOf("#t1#") + 4, three.indexOf("#t2#")));
                        tv_direct_ref_3.setText("Direct ref:- " + three.substring(three.indexOf("#t2#") + 4, three.indexOf("#t3#")));

                        switch (paidLegCount) {
                            case 0:
                                switch (unpaidLegCount) {
                                    case 0:
                                        break;

                                    case 1:
                                        ib_user3_one.setImageResource(R.drawable.radio_red_color);
                                        break;

                                    case 2:
                                        ib_user3_one.setImageResource(R.drawable.radio_red_color);
                                        ib_user3_two.setImageResource(R.drawable.radio_red_color);
                                        break;

                                    case 3:
                                        ib_user3_one.setImageResource(R.drawable.radio_red_color);
                                        ib_user3_two.setImageResource(R.drawable.radio_red_color);
                                        ib_user3_three.setImageResource(R.drawable.radio_red_color);
                                        break;

                                    case 4:
                                        ib_user3_one.setImageResource(R.drawable.radio_red_color);
                                        ib_user3_two.setImageResource(R.drawable.radio_red_color);
                                        ib_user3_three.setImageResource(R.drawable.radio_red_color);
                                        ib_user3_four.setImageResource(R.drawable.radio_red_color);
                                        break;

                                    case 5:
                                        ib_user3_one.setImageResource(R.drawable.radio_red_color);
                                        ib_user3_two.setImageResource(R.drawable.radio_red_color);
                                        ib_user3_three.setImageResource(R.drawable.radio_red_color);
                                        ib_user3_four.setImageResource(R.drawable.radio_red_color);
                                        ib_user3_five.setImageResource(R.drawable.radio_red_color);
                                        break;

                                    case 6:
                                        ib_user3_one.setImageResource(R.drawable.radio_red_color);
                                        ib_user3_two.setImageResource(R.drawable.radio_red_color);
                                        ib_user3_three.setImageResource(R.drawable.radio_red_color);
                                        ib_user3_four.setImageResource(R.drawable.radio_red_color);
                                        ib_user3_five.setImageResource(R.drawable.radio_red_color);
                                        ib_user3_six.setImageResource(R.drawable.radio_red_color);
                                        break;

                                    case 7:
                                        ib_user3_one.setImageResource(R.drawable.radio_red_color);
                                        ib_user3_two.setImageResource(R.drawable.radio_red_color);
                                        ib_user3_three.setImageResource(R.drawable.radio_red_color);
                                        ib_user3_four.setImageResource(R.drawable.radio_red_color);
                                        ib_user3_five.setImageResource(R.drawable.radio_red_color);
                                        ib_user3_six.setImageResource(R.drawable.radio_red_color);
                                        ib_user3_seven.setImageResource(R.drawable.radio_red_color);
                                        break;

                                    case 8:
                                        ib_user3_one.setImageResource(R.drawable.radio_red_color);
                                        ib_user3_two.setImageResource(R.drawable.radio_red_color);
                                        ib_user3_three.setImageResource(R.drawable.radio_red_color);
                                        ib_user3_four.setImageResource(R.drawable.radio_red_color);
                                        ib_user3_five.setImageResource(R.drawable.radio_red_color);
                                        ib_user3_six.setImageResource(R.drawable.radio_red_color);
                                        ib_user3_seven.setImageResource(R.drawable.radio_red_color);
                                        ib_user3_eight.setImageResource(R.drawable.radio_red_color);
                                        break;
                                }
                                break;

                            case 1:
                                ib_user3_one.setImageResource(R.drawable.radio_green_color);

                                switch (unpaidLegCount) {
                                    case 0:
                                        break;

                                    case 1:
                                        ib_user3_two.setImageResource(R.drawable.radio_red_color);
                                        break;

                                    case 2:
                                        ib_user3_two.setImageResource(R.drawable.radio_red_color);
                                        ib_user3_three.setImageResource(R.drawable.radio_red_color);
                                        break;

                                    case 3:
                                        ib_user3_two.setImageResource(R.drawable.radio_red_color);
                                        ib_user3_three.setImageResource(R.drawable.radio_red_color);
                                        ib_user3_four.setImageResource(R.drawable.radio_red_color);
                                        break;

                                    case 4:
                                        ib_user3_two.setImageResource(R.drawable.radio_red_color);
                                        ib_user3_three.setImageResource(R.drawable.radio_red_color);
                                        ib_user3_four.setImageResource(R.drawable.radio_red_color);
                                        ib_user3_five.setImageResource(R.drawable.radio_red_color);
                                        break;

                                    case 5:
                                        ib_user3_two.setImageResource(R.drawable.radio_red_color);
                                        ib_user3_three.setImageResource(R.drawable.radio_red_color);
                                        ib_user3_four.setImageResource(R.drawable.radio_red_color);
                                        ib_user3_five.setImageResource(R.drawable.radio_red_color);
                                        ib_user3_six.setImageResource(R.drawable.radio_red_color);
                                        break;

                                    case 6:
                                        ib_user3_two.setImageResource(R.drawable.radio_red_color);
                                        ib_user3_three.setImageResource(R.drawable.radio_red_color);
                                        ib_user3_four.setImageResource(R.drawable.radio_red_color);
                                        ib_user3_five.setImageResource(R.drawable.radio_red_color);
                                        ib_user3_six.setImageResource(R.drawable.radio_red_color);
                                        ib_user3_seven.setImageResource(R.drawable.radio_red_color);
                                        break;

                                    case 7:
                                        ib_user3_two.setImageResource(R.drawable.radio_red_color);
                                        ib_user3_three.setImageResource(R.drawable.radio_red_color);
                                        ib_user3_four.setImageResource(R.drawable.radio_red_color);
                                        ib_user3_five.setImageResource(R.drawable.radio_red_color);
                                        ib_user3_six.setImageResource(R.drawable.radio_red_color);
                                        ib_user3_seven.setImageResource(R.drawable.radio_red_color);
                                        ib_user3_eight.setImageResource(R.drawable.radio_red_color);
                                        break;
                                }
                                break;

                            case 2:
                                ib_user3_one.setImageResource(R.drawable.radio_green_color);
                                ib_user3_two.setImageResource(R.drawable.radio_green_color);

                                switch (unpaidLegCount) {
                                    case 0:
                                        break;

                                    case 1:
                                        ib_user3_three.setImageResource(R.drawable.radio_red_color);
                                        break;

                                    case 2:
                                        ib_user3_three.setImageResource(R.drawable.radio_red_color);
                                        ib_user3_four.setImageResource(R.drawable.radio_red_color);
                                        break;

                                    case 3:
                                        ib_user3_three.setImageResource(R.drawable.radio_red_color);
                                        ib_user3_four.setImageResource(R.drawable.radio_red_color);
                                        ib_user3_five.setImageResource(R.drawable.radio_red_color);
                                        break;

                                    case 4:
                                        ib_user3_three.setImageResource(R.drawable.radio_red_color);
                                        ib_user3_four.setImageResource(R.drawable.radio_red_color);
                                        ib_user3_five.setImageResource(R.drawable.radio_red_color);
                                        ib_user3_six.setImageResource(R.drawable.radio_red_color);
                                        break;

                                    case 5:
                                        ib_user3_three.setImageResource(R.drawable.radio_red_color);
                                        ib_user3_four.setImageResource(R.drawable.radio_red_color);
                                        ib_user3_five.setImageResource(R.drawable.radio_red_color);
                                        ib_user3_six.setImageResource(R.drawable.radio_red_color);
                                        ib_user3_seven.setImageResource(R.drawable.radio_red_color);
                                        break;

                                    case 6:
                                        ib_user3_three.setImageResource(R.drawable.radio_red_color);
                                        ib_user3_four.setImageResource(R.drawable.radio_red_color);
                                        ib_user3_five.setImageResource(R.drawable.radio_red_color);
                                        ib_user3_six.setImageResource(R.drawable.radio_red_color);
                                        ib_user3_seven.setImageResource(R.drawable.radio_red_color);
                                        ib_user3_eight.setImageResource(R.drawable.radio_red_color);
                                        break;
                                }
                                break;

                            case 3:
                                ib_user3_one.setImageResource(R.drawable.radio_green_color);
                                ib_user3_two.setImageResource(R.drawable.radio_green_color);
                                ib_user3_three.setImageResource(R.drawable.radio_green_color);

                                switch (unpaidLegCount) {
                                    case 0:
                                        break;

                                    case 1:
                                        ib_user3_four.setImageResource(R.drawable.radio_red_color);
                                        break;

                                    case 2:
                                        ib_user3_four.setImageResource(R.drawable.radio_red_color);
                                        ib_user3_five.setImageResource(R.drawable.radio_red_color);
                                        break;

                                    case 3:
                                        ib_user3_four.setImageResource(R.drawable.radio_red_color);
                                        ib_user3_five.setImageResource(R.drawable.radio_red_color);
                                        ib_user3_six.setImageResource(R.drawable.radio_red_color);
                                        break;

                                    case 4:
                                        ib_user3_four.setImageResource(R.drawable.radio_red_color);
                                        ib_user3_five.setImageResource(R.drawable.radio_red_color);
                                        ib_user3_six.setImageResource(R.drawable.radio_red_color);
                                        ib_user3_seven.setImageResource(R.drawable.radio_red_color);
                                        break;

                                    case 5:
                                        ib_user3_four.setImageResource(R.drawable.radio_red_color);
                                        ib_user3_five.setImageResource(R.drawable.radio_red_color);
                                        ib_user3_six.setImageResource(R.drawable.radio_red_color);
                                        ib_user3_seven.setImageResource(R.drawable.radio_red_color);
                                        ib_user3_eight.setImageResource(R.drawable.radio_red_color);
                                        break;
                                }
                                break;

                            case 4:
                                ib_user3_one.setImageResource(R.drawable.radio_green_color);
                                ib_user3_two.setImageResource(R.drawable.radio_green_color);
                                ib_user3_three.setImageResource(R.drawable.radio_green_color);
                                ib_user3_four.setImageResource(R.drawable.radio_green_color);

                                switch (unpaidLegCount) {
                                    case 0:
                                        break;

                                    case 1:
                                        ib_user3_five.setImageResource(R.drawable.radio_red_color);
                                        break;

                                    case 2:
                                        ib_user3_five.setImageResource(R.drawable.radio_red_color);
                                        ib_user3_six.setImageResource(R.drawable.radio_red_color);
                                        break;

                                    case 3:
                                        ib_user3_five.setImageResource(R.drawable.radio_red_color);
                                        ib_user3_six.setImageResource(R.drawable.radio_red_color);
                                        ib_user3_seven.setImageResource(R.drawable.radio_red_color);
                                        break;

                                    case 4:
                                        ib_user3_five.setImageResource(R.drawable.radio_red_color);
                                        ib_user3_six.setImageResource(R.drawable.radio_red_color);
                                        ib_user3_seven.setImageResource(R.drawable.radio_red_color);
                                        ib_user3_eight.setImageResource(R.drawable.radio_red_color);
                                        break;
                                }
                                break;

                            case 5:
                                ib_user3_one.setImageResource(R.drawable.radio_green_color);
                                ib_user3_two.setImageResource(R.drawable.radio_green_color);
                                ib_user3_three.setImageResource(R.drawable.radio_green_color);
                                ib_user3_four.setImageResource(R.drawable.radio_green_color);
                                ib_user3_five.setImageResource(R.drawable.radio_green_color);

                                switch (unpaidLegCount) {
                                    case 0:
                                        break;
                                    case 1:
                                        ib_user3_six.setImageResource(R.drawable.radio_red_color);
                                        break;

                                    case 2:
                                        ib_user3_six.setImageResource(R.drawable.radio_red_color);
                                        ib_user3_seven.setImageResource(R.drawable.radio_red_color);
                                        break;

                                    case 3:
                                        ib_user3_six.setImageResource(R.drawable.radio_red_color);
                                        ib_user3_seven.setImageResource(R.drawable.radio_red_color);
                                        ib_user3_eight.setImageResource(R.drawable.radio_red_color);
                                        break;
                                }
                                break;

                            case 6:
                                ib_user3_one.setImageResource(R.drawable.radio_green_color);
                                ib_user3_two.setImageResource(R.drawable.radio_green_color);
                                ib_user3_three.setImageResource(R.drawable.radio_green_color);
                                ib_user3_four.setImageResource(R.drawable.radio_green_color);
                                ib_user3_five.setImageResource(R.drawable.radio_green_color);
                                ib_user3_six.setImageResource(R.drawable.radio_green_color);

                                switch (unpaidLegCount) {
                                    case 0:
                                        break;

                                    case 1:
                                        ib_user3_seven.setImageResource(R.drawable.radio_red_color);
                                        break;

                                    case 2:
                                        ib_user3_seven.setImageResource(R.drawable.radio_red_color);
                                        ib_user3_eight.setImageResource(R.drawable.radio_red_color);
                                        break;
                                }
                                break;

                            case 7:
                                ib_user3_one.setImageResource(R.drawable.radio_green_color);
                                ib_user3_two.setImageResource(R.drawable.radio_green_color);
                                ib_user3_three.setImageResource(R.drawable.radio_green_color);
                                ib_user3_four.setImageResource(R.drawable.radio_green_color);
                                ib_user3_five.setImageResource(R.drawable.radio_green_color);
                                ib_user3_six.setImageResource(R.drawable.radio_green_color);
                                ib_user3_seven.setImageResource(R.drawable.radio_green_color);

                                switch (unpaidLegCount) {
                                    case 0:
                                        break;

                                    case 1:
                                        ib_user3_eight.setImageResource(R.drawable.radio_red_color);
                                        break;
                                }
                                break;

                            case 8:
                                ib_user3_one.setImageResource(R.drawable.radio_green_color);
                                ib_user3_two.setImageResource(R.drawable.radio_green_color);
                                ib_user3_three.setImageResource(R.drawable.radio_green_color);
                                ib_user3_four.setImageResource(R.drawable.radio_green_color);
                                ib_user3_five.setImageResource(R.drawable.radio_green_color);
                                ib_user3_six.setImageResource(R.drawable.radio_green_color);
                                ib_user3_seven.setImageResource(R.drawable.radio_green_color);
                                ib_user3_eight.setImageResource(R.drawable.radio_green_color);

                                switch (unpaidLegCount) {
                                    case 0:
                                        break;
                                }
                                break;

                        }
                    }

                    ///// fourth user data-set
                    if (four.contains("t0")) {
                        int paidLegCount = Integer.parseInt(four.substring(four.indexOf("#t0#") + 4, four.indexOf("#t1#")));
                        int unpaidLegCount = Integer.parseInt(four.substring(four.indexOf("#t1#") + 4, four.indexOf("#t2#")));
                        tv_direct_ref_4.setText("Direct ref:- " + four.substring(four.indexOf("#t2#") + 4, four.indexOf("#t3#")));

                        switch (paidLegCount) {
                            case 0:
                                switch (unpaidLegCount) {
                                    case 0:
                                        break;

                                    case 1:
                                        ib_user4_one.setImageResource(R.drawable.radio_red_color);
                                        break;

                                    case 2:
                                        ib_user4_one.setImageResource(R.drawable.radio_red_color);
                                        ib_user4_two.setImageResource(R.drawable.radio_red_color);
                                        break;

                                    case 3:
                                        ib_user4_one.setImageResource(R.drawable.radio_red_color);
                                        ib_user4_two.setImageResource(R.drawable.radio_red_color);
                                        ib_user4_three.setImageResource(R.drawable.radio_red_color);
                                        break;

                                    case 4:
                                        ib_user4_one.setImageResource(R.drawable.radio_red_color);
                                        ib_user4_two.setImageResource(R.drawable.radio_red_color);
                                        ib_user4_three.setImageResource(R.drawable.radio_red_color);
                                        ib_user4_four.setImageResource(R.drawable.radio_red_color);
                                        break;

                                    case 5:
                                        ib_user4_one.setImageResource(R.drawable.radio_red_color);
                                        ib_user4_two.setImageResource(R.drawable.radio_red_color);
                                        ib_user4_three.setImageResource(R.drawable.radio_red_color);
                                        ib_user4_four.setImageResource(R.drawable.radio_red_color);
                                        ib_user4_five.setImageResource(R.drawable.radio_red_color);
                                        break;

                                    case 6:
                                        ib_user4_one.setImageResource(R.drawable.radio_red_color);
                                        ib_user4_two.setImageResource(R.drawable.radio_red_color);
                                        ib_user4_three.setImageResource(R.drawable.radio_red_color);
                                        ib_user4_four.setImageResource(R.drawable.radio_red_color);
                                        ib_user4_five.setImageResource(R.drawable.radio_red_color);
                                        ib_user4_six.setImageResource(R.drawable.radio_red_color);
                                        break;

                                    case 7:
                                        ib_user4_one.setImageResource(R.drawable.radio_red_color);
                                        ib_user4_two.setImageResource(R.drawable.radio_red_color);
                                        ib_user4_three.setImageResource(R.drawable.radio_red_color);
                                        ib_user4_four.setImageResource(R.drawable.radio_red_color);
                                        ib_user4_five.setImageResource(R.drawable.radio_red_color);
                                        ib_user4_six.setImageResource(R.drawable.radio_red_color);
                                        ib_user4_seven.setImageResource(R.drawable.radio_red_color);
                                        break;

                                    case 8:
                                        ib_user4_one.setImageResource(R.drawable.radio_red_color);
                                        ib_user4_two.setImageResource(R.drawable.radio_red_color);
                                        ib_user4_three.setImageResource(R.drawable.radio_red_color);
                                        ib_user4_four.setImageResource(R.drawable.radio_red_color);
                                        ib_user4_five.setImageResource(R.drawable.radio_red_color);
                                        ib_user4_six.setImageResource(R.drawable.radio_red_color);
                                        ib_user4_seven.setImageResource(R.drawable.radio_red_color);
                                        ib_user4_eight.setImageResource(R.drawable.radio_red_color);
                                        break;
                                }
                                break;

                            case 1:
                                ib_user4_one.setImageResource(R.drawable.radio_green_color);

                                switch (unpaidLegCount) {
                                    case 0:
                                        break;

                                    case 1:
                                        ib_user4_two.setImageResource(R.drawable.radio_red_color);
                                        break;

                                    case 2:
                                        ib_user4_two.setImageResource(R.drawable.radio_red_color);
                                        ib_user4_three.setImageResource(R.drawable.radio_red_color);
                                        break;

                                    case 3:
                                        ib_user4_two.setImageResource(R.drawable.radio_red_color);
                                        ib_user4_three.setImageResource(R.drawable.radio_red_color);
                                        ib_user4_four.setImageResource(R.drawable.radio_red_color);
                                        break;

                                    case 4:
                                        ib_user4_two.setImageResource(R.drawable.radio_red_color);
                                        ib_user4_three.setImageResource(R.drawable.radio_red_color);
                                        ib_user4_four.setImageResource(R.drawable.radio_red_color);
                                        ib_user4_five.setImageResource(R.drawable.radio_red_color);
                                        break;

                                    case 5:
                                        ib_user4_two.setImageResource(R.drawable.radio_red_color);
                                        ib_user4_three.setImageResource(R.drawable.radio_red_color);
                                        ib_user4_four.setImageResource(R.drawable.radio_red_color);
                                        ib_user4_five.setImageResource(R.drawable.radio_red_color);
                                        ib_user4_six.setImageResource(R.drawable.radio_red_color);
                                        break;

                                    case 6:
                                        ib_user4_two.setImageResource(R.drawable.radio_red_color);
                                        ib_user4_three.setImageResource(R.drawable.radio_red_color);
                                        ib_user4_four.setImageResource(R.drawable.radio_red_color);
                                        ib_user4_five.setImageResource(R.drawable.radio_red_color);
                                        ib_user4_six.setImageResource(R.drawable.radio_red_color);
                                        ib_user4_seven.setImageResource(R.drawable.radio_red_color);
                                        break;

                                    case 7:
                                        ib_user4_two.setImageResource(R.drawable.radio_red_color);
                                        ib_user4_three.setImageResource(R.drawable.radio_red_color);
                                        ib_user4_four.setImageResource(R.drawable.radio_red_color);
                                        ib_user4_five.setImageResource(R.drawable.radio_red_color);
                                        ib_user4_six.setImageResource(R.drawable.radio_red_color);
                                        ib_user4_seven.setImageResource(R.drawable.radio_red_color);
                                        ib_user4_eight.setImageResource(R.drawable.radio_red_color);
                                        break;
                                }
                                break;

                            case 2:
                                ib_user4_one.setImageResource(R.drawable.radio_green_color);
                                ib_user4_two.setImageResource(R.drawable.radio_green_color);

                                switch (unpaidLegCount) {
                                    case 0:
                                        break;

                                    case 1:
                                        ib_user4_three.setImageResource(R.drawable.radio_red_color);
                                        break;

                                    case 2:
                                        ib_user4_three.setImageResource(R.drawable.radio_red_color);
                                        ib_user4_four.setImageResource(R.drawable.radio_red_color);
                                        break;

                                    case 3:
                                        ib_user4_three.setImageResource(R.drawable.radio_red_color);
                                        ib_user4_four.setImageResource(R.drawable.radio_red_color);
                                        ib_user4_five.setImageResource(R.drawable.radio_red_color);
                                        break;

                                    case 4:
                                        ib_user4_three.setImageResource(R.drawable.radio_red_color);
                                        ib_user4_four.setImageResource(R.drawable.radio_red_color);
                                        ib_user4_five.setImageResource(R.drawable.radio_red_color);
                                        ib_user4_six.setImageResource(R.drawable.radio_red_color);
                                        break;

                                    case 5:
                                        ib_user4_three.setImageResource(R.drawable.radio_red_color);
                                        ib_user4_four.setImageResource(R.drawable.radio_red_color);
                                        ib_user4_five.setImageResource(R.drawable.radio_red_color);
                                        ib_user4_six.setImageResource(R.drawable.radio_red_color);
                                        ib_user4_seven.setImageResource(R.drawable.radio_red_color);
                                        break;

                                    case 6:
                                        ib_user4_three.setImageResource(R.drawable.radio_red_color);
                                        ib_user4_four.setImageResource(R.drawable.radio_red_color);
                                        ib_user4_five.setImageResource(R.drawable.radio_red_color);
                                        ib_user4_six.setImageResource(R.drawable.radio_red_color);
                                        ib_user4_seven.setImageResource(R.drawable.radio_red_color);
                                        ib_user4_eight.setImageResource(R.drawable.radio_red_color);
                                        break;
                                }
                                break;

                            case 3:
                                ib_user4_one.setImageResource(R.drawable.radio_green_color);
                                ib_user4_two.setImageResource(R.drawable.radio_green_color);
                                ib_user4_three.setImageResource(R.drawable.radio_green_color);

                                switch (unpaidLegCount) {
                                    case 0:
                                        break;

                                    case 1:
                                        ib_user4_four.setImageResource(R.drawable.radio_red_color);
                                        break;

                                    case 2:
                                        ib_user4_four.setImageResource(R.drawable.radio_red_color);
                                        ib_user4_five.setImageResource(R.drawable.radio_red_color);
                                        break;

                                    case 3:
                                        ib_user4_four.setImageResource(R.drawable.radio_red_color);
                                        ib_user4_five.setImageResource(R.drawable.radio_red_color);
                                        ib_user4_six.setImageResource(R.drawable.radio_red_color);
                                        break;

                                    case 4:
                                        ib_user4_four.setImageResource(R.drawable.radio_red_color);
                                        ib_user4_five.setImageResource(R.drawable.radio_red_color);
                                        ib_user4_six.setImageResource(R.drawable.radio_red_color);
                                        ib_user4_seven.setImageResource(R.drawable.radio_red_color);
                                        break;

                                    case 5:
                                        ib_user4_four.setImageResource(R.drawable.radio_red_color);
                                        ib_user4_five.setImageResource(R.drawable.radio_red_color);
                                        ib_user4_six.setImageResource(R.drawable.radio_red_color);
                                        ib_user4_seven.setImageResource(R.drawable.radio_red_color);
                                        ib_user4_eight.setImageResource(R.drawable.radio_red_color);
                                        break;
                                }
                                break;

                            case 4:
                                ib_user4_one.setImageResource(R.drawable.radio_green_color);
                                ib_user4_two.setImageResource(R.drawable.radio_green_color);
                                ib_user4_three.setImageResource(R.drawable.radio_green_color);
                                ib_user4_four.setImageResource(R.drawable.radio_green_color);

                                switch (unpaidLegCount) {
                                    case 0:
                                        break;

                                    case 1:
                                        ib_user4_five.setImageResource(R.drawable.radio_red_color);
                                        break;

                                    case 2:
                                        ib_user4_five.setImageResource(R.drawable.radio_red_color);
                                        ib_user4_six.setImageResource(R.drawable.radio_red_color);
                                        break;

                                    case 3:
                                        ib_user4_five.setImageResource(R.drawable.radio_red_color);
                                        ib_user4_six.setImageResource(R.drawable.radio_red_color);
                                        ib_user4_seven.setImageResource(R.drawable.radio_red_color);
                                        break;

                                    case 4:
                                        ib_user4_five.setImageResource(R.drawable.radio_red_color);
                                        ib_user4_six.setImageResource(R.drawable.radio_red_color);
                                        ib_user4_seven.setImageResource(R.drawable.radio_red_color);
                                        ib_user4_eight.setImageResource(R.drawable.radio_red_color);
                                        break;
                                }
                                break;

                            case 5:
                                ib_user4_one.setImageResource(R.drawable.radio_green_color);
                                ib_user4_two.setImageResource(R.drawable.radio_green_color);
                                ib_user4_three.setImageResource(R.drawable.radio_green_color);
                                ib_user4_four.setImageResource(R.drawable.radio_green_color);
                                ib_user4_five.setImageResource(R.drawable.radio_green_color);

                                switch (unpaidLegCount) {
                                    case 0:
                                        break;
                                    case 1:
                                        ib_user4_six.setImageResource(R.drawable.radio_red_color);
                                        break;

                                    case 2:
                                        ib_user4_six.setImageResource(R.drawable.radio_red_color);
                                        ib_user4_seven.setImageResource(R.drawable.radio_red_color);
                                        break;

                                    case 3:
                                        ib_user4_six.setImageResource(R.drawable.radio_red_color);
                                        ib_user4_seven.setImageResource(R.drawable.radio_red_color);
                                        ib_user4_eight.setImageResource(R.drawable.radio_red_color);
                                        break;
                                }
                                break;

                            case 6:
                                ib_user4_one.setImageResource(R.drawable.radio_green_color);
                                ib_user4_two.setImageResource(R.drawable.radio_green_color);
                                ib_user4_three.setImageResource(R.drawable.radio_green_color);
                                ib_user4_four.setImageResource(R.drawable.radio_green_color);
                                ib_user4_five.setImageResource(R.drawable.radio_green_color);
                                ib_user4_six.setImageResource(R.drawable.radio_green_color);

                                switch (unpaidLegCount) {
                                    case 0:
                                        break;

                                    case 1:
                                        ib_user4_seven.setImageResource(R.drawable.radio_red_color);
                                        break;

                                    case 2:
                                        ib_user4_seven.setImageResource(R.drawable.radio_red_color);
                                        ib_user4_eight.setImageResource(R.drawable.radio_red_color);
                                        break;
                                }
                                break;

                            case 7:
                                ib_user4_one.setImageResource(R.drawable.radio_green_color);
                                ib_user4_two.setImageResource(R.drawable.radio_green_color);
                                ib_user4_three.setImageResource(R.drawable.radio_green_color);
                                ib_user4_four.setImageResource(R.drawable.radio_green_color);
                                ib_user4_five.setImageResource(R.drawable.radio_green_color);
                                ib_user4_six.setImageResource(R.drawable.radio_green_color);
                                ib_user4_seven.setImageResource(R.drawable.radio_green_color);

                                switch (unpaidLegCount) {
                                    case 0:
                                        break;

                                    case 1:
                                        ib_user4_eight.setImageResource(R.drawable.radio_red_color);
                                        break;
                                }
                                break;

                            case 8:
                                ib_user4_one.setImageResource(R.drawable.radio_green_color);
                                ib_user4_two.setImageResource(R.drawable.radio_green_color);
                                ib_user4_three.setImageResource(R.drawable.radio_green_color);
                                ib_user4_four.setImageResource(R.drawable.radio_green_color);
                                ib_user4_five.setImageResource(R.drawable.radio_green_color);
                                ib_user4_six.setImageResource(R.drawable.radio_green_color);
                                ib_user4_seven.setImageResource(R.drawable.radio_green_color);
                                ib_user4_eight.setImageResource(R.drawable.radio_green_color);

                                switch (unpaidLegCount) {
                                    case 0:
                                        break;
                                }
                                break;

                        }
                    }

                    // fifth user data-set
                    if (five.contains("t0")) {
                        int paidLegCount = Integer.parseInt(five.substring(five.indexOf("#t0#") + 4, five.indexOf("#t1#")));
                        int unpaidLegCount = Integer.parseInt(five.substring(five.indexOf("#t1#") + 4, five.indexOf("#t2#")));
                        tv_direct_ref_5.setText("Direct ref:- " + five.substring(five.indexOf("#t2#") + 4, five.indexOf("#t3#")));

                        switch (paidLegCount) {
                            case 0:
                                switch (unpaidLegCount) {
                                    case 0:
                                        break;

                                    case 1:
                                        ib_user5_one.setImageResource(R.drawable.radio_red_color);
                                        break;

                                    case 2:
                                        ib_user5_one.setImageResource(R.drawable.radio_red_color);
                                        ib_user5_two.setImageResource(R.drawable.radio_red_color);
                                        break;

                                    case 3:
                                        ib_user5_one.setImageResource(R.drawable.radio_red_color);
                                        ib_user5_two.setImageResource(R.drawable.radio_red_color);
                                        ib_user5_three.setImageResource(R.drawable.radio_red_color);
                                        break;

                                    case 4:
                                        ib_user5_one.setImageResource(R.drawable.radio_red_color);
                                        ib_user5_two.setImageResource(R.drawable.radio_red_color);
                                        ib_user5_three.setImageResource(R.drawable.radio_red_color);
                                        ib_user5_four.setImageResource(R.drawable.radio_red_color);
                                        break;

                                    case 5:
                                        ib_user5_one.setImageResource(R.drawable.radio_red_color);
                                        ib_user5_two.setImageResource(R.drawable.radio_red_color);
                                        ib_user5_three.setImageResource(R.drawable.radio_red_color);
                                        ib_user5_four.setImageResource(R.drawable.radio_red_color);
                                        ib_user5_five.setImageResource(R.drawable.radio_red_color);
                                        break;

                                    case 6:
                                        ib_user5_one.setImageResource(R.drawable.radio_red_color);
                                        ib_user5_two.setImageResource(R.drawable.radio_red_color);
                                        ib_user5_three.setImageResource(R.drawable.radio_red_color);
                                        ib_user5_four.setImageResource(R.drawable.radio_red_color);
                                        ib_user5_five.setImageResource(R.drawable.radio_red_color);
                                        ib_user5_six.setImageResource(R.drawable.radio_red_color);
                                        break;

                                    case 7:
                                        ib_user5_one.setImageResource(R.drawable.radio_red_color);
                                        ib_user5_two.setImageResource(R.drawable.radio_red_color);
                                        ib_user5_three.setImageResource(R.drawable.radio_red_color);
                                        ib_user5_four.setImageResource(R.drawable.radio_red_color);
                                        ib_user5_five.setImageResource(R.drawable.radio_red_color);
                                        ib_user5_six.setImageResource(R.drawable.radio_red_color);
                                        ib_user5_seven.setImageResource(R.drawable.radio_red_color);
                                        break;

                                    case 8:
                                        ib_user5_one.setImageResource(R.drawable.radio_red_color);
                                        ib_user5_two.setImageResource(R.drawable.radio_red_color);
                                        ib_user5_three.setImageResource(R.drawable.radio_red_color);
                                        ib_user5_four.setImageResource(R.drawable.radio_red_color);
                                        ib_user5_five.setImageResource(R.drawable.radio_red_color);
                                        ib_user5_six.setImageResource(R.drawable.radio_red_color);
                                        ib_user5_seven.setImageResource(R.drawable.radio_red_color);
                                        ib_user5_eight.setImageResource(R.drawable.radio_red_color);
                                        break;
                                }
                                break;

                            case 1:
                                ib_user5_one.setImageResource(R.drawable.radio_green_color);

                                switch (unpaidLegCount) {
                                    case 0:
                                        break;

                                    case 1:
                                        ib_user5_two.setImageResource(R.drawable.radio_red_color);
                                        break;

                                    case 2:
                                        ib_user5_two.setImageResource(R.drawable.radio_red_color);
                                        ib_user5_three.setImageResource(R.drawable.radio_red_color);
                                        break;

                                    case 3:
                                        ib_user5_two.setImageResource(R.drawable.radio_red_color);
                                        ib_user5_three.setImageResource(R.drawable.radio_red_color);
                                        ib_user5_four.setImageResource(R.drawable.radio_red_color);
                                        break;

                                    case 4:
                                        ib_user5_two.setImageResource(R.drawable.radio_red_color);
                                        ib_user5_three.setImageResource(R.drawable.radio_red_color);
                                        ib_user5_four.setImageResource(R.drawable.radio_red_color);
                                        ib_user5_five.setImageResource(R.drawable.radio_red_color);
                                        break;

                                    case 5:
                                        ib_user5_two.setImageResource(R.drawable.radio_red_color);
                                        ib_user5_three.setImageResource(R.drawable.radio_red_color);
                                        ib_user5_four.setImageResource(R.drawable.radio_red_color);
                                        ib_user5_five.setImageResource(R.drawable.radio_red_color);
                                        ib_user5_six.setImageResource(R.drawable.radio_red_color);
                                        break;

                                    case 6:
                                        ib_user5_two.setImageResource(R.drawable.radio_red_color);
                                        ib_user5_three.setImageResource(R.drawable.radio_red_color);
                                        ib_user5_four.setImageResource(R.drawable.radio_red_color);
                                        ib_user5_five.setImageResource(R.drawable.radio_red_color);
                                        ib_user5_six.setImageResource(R.drawable.radio_red_color);
                                        ib_user5_seven.setImageResource(R.drawable.radio_red_color);
                                        break;

                                    case 7:
                                        ib_user5_two.setImageResource(R.drawable.radio_red_color);
                                        ib_user5_three.setImageResource(R.drawable.radio_red_color);
                                        ib_user5_four.setImageResource(R.drawable.radio_red_color);
                                        ib_user5_five.setImageResource(R.drawable.radio_red_color);
                                        ib_user5_six.setImageResource(R.drawable.radio_red_color);
                                        ib_user5_seven.setImageResource(R.drawable.radio_red_color);
                                        ib_user5_eight.setImageResource(R.drawable.radio_red_color);
                                        break;
                                }
                                break;

                            case 2:
                                ib_user5_one.setImageResource(R.drawable.radio_green_color);
                                ib_user5_two.setImageResource(R.drawable.radio_green_color);

                                switch (unpaidLegCount) {
                                    case 0:
                                        break;

                                    case 1:
                                        ib_user5_three.setImageResource(R.drawable.radio_red_color);
                                        break;

                                    case 2:
                                        ib_user5_three.setImageResource(R.drawable.radio_red_color);
                                        ib_user5_four.setImageResource(R.drawable.radio_red_color);
                                        break;

                                    case 3:
                                        ib_user5_three.setImageResource(R.drawable.radio_red_color);
                                        ib_user5_four.setImageResource(R.drawable.radio_red_color);
                                        ib_user5_five.setImageResource(R.drawable.radio_red_color);
                                        break;

                                    case 4:
                                        ib_user5_three.setImageResource(R.drawable.radio_red_color);
                                        ib_user5_four.setImageResource(R.drawable.radio_red_color);
                                        ib_user5_five.setImageResource(R.drawable.radio_red_color);
                                        ib_user5_six.setImageResource(R.drawable.radio_red_color);
                                        break;

                                    case 5:
                                        ib_user5_three.setImageResource(R.drawable.radio_red_color);
                                        ib_user5_four.setImageResource(R.drawable.radio_red_color);
                                        ib_user5_five.setImageResource(R.drawable.radio_red_color);
                                        ib_user5_six.setImageResource(R.drawable.radio_red_color);
                                        ib_user5_seven.setImageResource(R.drawable.radio_red_color);
                                        break;

                                    case 6:
                                        ib_user5_three.setImageResource(R.drawable.radio_red_color);
                                        ib_user5_four.setImageResource(R.drawable.radio_red_color);
                                        ib_user5_five.setImageResource(R.drawable.radio_red_color);
                                        ib_user5_six.setImageResource(R.drawable.radio_red_color);
                                        ib_user5_seven.setImageResource(R.drawable.radio_red_color);
                                        ib_user5_eight.setImageResource(R.drawable.radio_red_color);
                                        break;
                                }
                                break;

                            case 3:
                                ib_user5_one.setImageResource(R.drawable.radio_green_color);
                                ib_user5_two.setImageResource(R.drawable.radio_green_color);
                                ib_user5_three.setImageResource(R.drawable.radio_green_color);

                                switch (unpaidLegCount) {
                                    case 0:
                                        break;

                                    case 1:
                                        ib_user5_four.setImageResource(R.drawable.radio_red_color);
                                        break;

                                    case 2:
                                        ib_user5_four.setImageResource(R.drawable.radio_red_color);
                                        ib_user5_five.setImageResource(R.drawable.radio_red_color);
                                        break;

                                    case 3:
                                        ib_user5_four.setImageResource(R.drawable.radio_red_color);
                                        ib_user5_five.setImageResource(R.drawable.radio_red_color);
                                        ib_user5_six.setImageResource(R.drawable.radio_red_color);
                                        break;

                                    case 4:
                                        ib_user5_four.setImageResource(R.drawable.radio_red_color);
                                        ib_user5_five.setImageResource(R.drawable.radio_red_color);
                                        ib_user5_six.setImageResource(R.drawable.radio_red_color);
                                        ib_user5_seven.setImageResource(R.drawable.radio_red_color);
                                        break;

                                    case 5:
                                        ib_user5_four.setImageResource(R.drawable.radio_red_color);
                                        ib_user5_five.setImageResource(R.drawable.radio_red_color);
                                        ib_user5_six.setImageResource(R.drawable.radio_red_color);
                                        ib_user5_seven.setImageResource(R.drawable.radio_red_color);
                                        ib_user5_eight.setImageResource(R.drawable.radio_red_color);
                                        break;
                                }
                                break;

                            case 4:
                                ib_user5_one.setImageResource(R.drawable.radio_green_color);
                                ib_user5_two.setImageResource(R.drawable.radio_green_color);
                                ib_user5_three.setImageResource(R.drawable.radio_green_color);
                                ib_user5_four.setImageResource(R.drawable.radio_green_color);

                                switch (unpaidLegCount) {
                                    case 0:
                                        break;

                                    case 1:
                                        ib_user5_five.setImageResource(R.drawable.radio_red_color);
                                        break;

                                    case 2:
                                        ib_user5_five.setImageResource(R.drawable.radio_red_color);
                                        ib_user5_six.setImageResource(R.drawable.radio_red_color);
                                        break;

                                    case 3:
                                        ib_user5_five.setImageResource(R.drawable.radio_red_color);
                                        ib_user5_six.setImageResource(R.drawable.radio_red_color);
                                        ib_user5_seven.setImageResource(R.drawable.radio_red_color);
                                        break;

                                    case 4:
                                        ib_user5_five.setImageResource(R.drawable.radio_red_color);
                                        ib_user5_six.setImageResource(R.drawable.radio_red_color);
                                        ib_user5_seven.setImageResource(R.drawable.radio_red_color);
                                        ib_user5_eight.setImageResource(R.drawable.radio_red_color);
                                        break;
                                }
                                break;

                            case 5:
                                ib_user5_one.setImageResource(R.drawable.radio_green_color);
                                ib_user5_two.setImageResource(R.drawable.radio_green_color);
                                ib_user5_three.setImageResource(R.drawable.radio_green_color);
                                ib_user5_four.setImageResource(R.drawable.radio_green_color);
                                ib_user5_five.setImageResource(R.drawable.radio_green_color);

                                switch (unpaidLegCount) {
                                    case 0:
                                        break;
                                    case 1:
                                        ib_user5_six.setImageResource(R.drawable.radio_red_color);
                                        break;

                                    case 2:
                                        ib_user5_six.setImageResource(R.drawable.radio_red_color);
                                        ib_user5_seven.setImageResource(R.drawable.radio_red_color);
                                        break;

                                    case 3:
                                        ib_user5_six.setImageResource(R.drawable.radio_red_color);
                                        ib_user5_seven.setImageResource(R.drawable.radio_red_color);
                                        ib_user5_eight.setImageResource(R.drawable.radio_red_color);
                                        break;
                                }
                                break;

                            case 6:
                                ib_user5_one.setImageResource(R.drawable.radio_green_color);
                                ib_user5_two.setImageResource(R.drawable.radio_green_color);
                                ib_user5_three.setImageResource(R.drawable.radio_green_color);
                                ib_user5_four.setImageResource(R.drawable.radio_green_color);
                                ib_user5_five.setImageResource(R.drawable.radio_green_color);
                                ib_user5_six.setImageResource(R.drawable.radio_green_color);

                                switch (unpaidLegCount) {
                                    case 0:
                                        break;

                                    case 1:
                                        ib_user5_seven.setImageResource(R.drawable.radio_red_color);
                                        break;

                                    case 2:
                                        ib_user5_seven.setImageResource(R.drawable.radio_red_color);
                                        ib_user5_eight.setImageResource(R.drawable.radio_red_color);
                                        break;
                                }
                                break;

                            case 7:
                                ib_user5_one.setImageResource(R.drawable.radio_green_color);
                                ib_user5_two.setImageResource(R.drawable.radio_green_color);
                                ib_user5_three.setImageResource(R.drawable.radio_green_color);
                                ib_user5_four.setImageResource(R.drawable.radio_green_color);
                                ib_user5_five.setImageResource(R.drawable.radio_green_color);
                                ib_user5_six.setImageResource(R.drawable.radio_green_color);
                                ib_user5_seven.setImageResource(R.drawable.radio_green_color);

                                switch (unpaidLegCount) {
                                    case 0:
                                        break;

                                    case 1:
                                        ib_user5_eight.setImageResource(R.drawable.radio_red_color);
                                        break;
                                }
                                break;

                            case 8:
                                ib_user5_one.setImageResource(R.drawable.radio_green_color);
                                ib_user5_two.setImageResource(R.drawable.radio_green_color);
                                ib_user5_three.setImageResource(R.drawable.radio_green_color);
                                ib_user5_four.setImageResource(R.drawable.radio_green_color);
                                ib_user5_five.setImageResource(R.drawable.radio_green_color);
                                ib_user5_six.setImageResource(R.drawable.radio_green_color);
                                ib_user5_seven.setImageResource(R.drawable.radio_green_color);
                                ib_user5_eight.setImageResource(R.drawable.radio_green_color);

                                switch (unpaidLegCount) {
                                    case 0:
                                        break;
                                }
                                break;
                        }
                    }

                    // sixth user data-set
                    if (six.contains("t0")) {
                        int paidLegCount = Integer.parseInt(six.substring(six.indexOf("#t0#") + 4, six.indexOf("#t1#")));
                        int unpaidLegCount = Integer.parseInt(six.substring(six.indexOf("#t1#") + 4, six.indexOf("#t2#")));
                        tv_direct_ref_6.setText("Direct ref:- " + six.substring(six.indexOf("#t2#") + 4, six.indexOf("#t3#")));

                        switch (paidLegCount) {
                            case 0:
                                switch (unpaidLegCount) {
                                    case 0:
                                        break;

                                    case 1:
                                        ib_user6_one.setImageResource(R.drawable.radio_red_color);
                                        break;

                                    case 2:
                                        ib_user6_one.setImageResource(R.drawable.radio_red_color);
                                        ib_user6_two.setImageResource(R.drawable.radio_red_color);
                                        break;

                                    case 3:
                                        ib_user6_one.setImageResource(R.drawable.radio_red_color);
                                        ib_user6_two.setImageResource(R.drawable.radio_red_color);
                                        ib_user6_three.setImageResource(R.drawable.radio_red_color);
                                        break;

                                    case 4:
                                        ib_user6_one.setImageResource(R.drawable.radio_red_color);
                                        ib_user6_two.setImageResource(R.drawable.radio_red_color);
                                        ib_user6_three.setImageResource(R.drawable.radio_red_color);
                                        ib_user6_four.setImageResource(R.drawable.radio_red_color);
                                        break;

                                    case 5:
                                        ib_user6_one.setImageResource(R.drawable.radio_red_color);
                                        ib_user6_two.setImageResource(R.drawable.radio_red_color);
                                        ib_user6_three.setImageResource(R.drawable.radio_red_color);
                                        ib_user6_four.setImageResource(R.drawable.radio_red_color);
                                        ib_user6_five.setImageResource(R.drawable.radio_red_color);
                                        break;

                                    case 6:
                                        ib_user6_one.setImageResource(R.drawable.radio_red_color);
                                        ib_user6_two.setImageResource(R.drawable.radio_red_color);
                                        ib_user6_three.setImageResource(R.drawable.radio_red_color);
                                        ib_user6_four.setImageResource(R.drawable.radio_red_color);
                                        ib_user6_five.setImageResource(R.drawable.radio_red_color);
                                        ib_user6_six.setImageResource(R.drawable.radio_red_color);
                                        break;

                                    case 7:
                                        ib_user6_one.setImageResource(R.drawable.radio_red_color);
                                        ib_user6_two.setImageResource(R.drawable.radio_red_color);
                                        ib_user6_three.setImageResource(R.drawable.radio_red_color);
                                        ib_user6_four.setImageResource(R.drawable.radio_red_color);
                                        ib_user6_five.setImageResource(R.drawable.radio_red_color);
                                        ib_user6_six.setImageResource(R.drawable.radio_red_color);
                                        ib_user6_seven.setImageResource(R.drawable.radio_red_color);
                                        break;

                                    case 8:
                                        ib_user6_one.setImageResource(R.drawable.radio_red_color);
                                        ib_user6_two.setImageResource(R.drawable.radio_red_color);
                                        ib_user6_three.setImageResource(R.drawable.radio_red_color);
                                        ib_user6_four.setImageResource(R.drawable.radio_red_color);
                                        ib_user6_five.setImageResource(R.drawable.radio_red_color);
                                        ib_user6_six.setImageResource(R.drawable.radio_red_color);
                                        ib_user6_seven.setImageResource(R.drawable.radio_red_color);
                                        ib_user6_eight.setImageResource(R.drawable.radio_red_color);
                                        break;
                                }
                                break;

                            case 1:
                                ib_user6_one.setImageResource(R.drawable.radio_green_color);

                                switch (unpaidLegCount) {
                                    case 0:
                                        break;

                                    case 1:
                                        ib_user6_two.setImageResource(R.drawable.radio_red_color);
                                        break;

                                    case 2:
                                        ib_user6_two.setImageResource(R.drawable.radio_red_color);
                                        ib_user6_three.setImageResource(R.drawable.radio_red_color);
                                        break;

                                    case 3:
                                        ib_user6_two.setImageResource(R.drawable.radio_red_color);
                                        ib_user6_three.setImageResource(R.drawable.radio_red_color);
                                        ib_user6_four.setImageResource(R.drawable.radio_red_color);
                                        break;

                                    case 4:
                                        ib_user6_two.setImageResource(R.drawable.radio_red_color);
                                        ib_user6_three.setImageResource(R.drawable.radio_red_color);
                                        ib_user6_four.setImageResource(R.drawable.radio_red_color);
                                        ib_user6_five.setImageResource(R.drawable.radio_red_color);
                                        break;

                                    case 5:
                                        ib_user6_two.setImageResource(R.drawable.radio_red_color);
                                        ib_user6_three.setImageResource(R.drawable.radio_red_color);
                                        ib_user6_four.setImageResource(R.drawable.radio_red_color);
                                        ib_user6_five.setImageResource(R.drawable.radio_red_color);
                                        ib_user6_six.setImageResource(R.drawable.radio_red_color);
                                        break;

                                    case 6:
                                        ib_user6_two.setImageResource(R.drawable.radio_red_color);
                                        ib_user6_three.setImageResource(R.drawable.radio_red_color);
                                        ib_user6_four.setImageResource(R.drawable.radio_red_color);
                                        ib_user6_five.setImageResource(R.drawable.radio_red_color);
                                        ib_user6_six.setImageResource(R.drawable.radio_red_color);
                                        ib_user6_seven.setImageResource(R.drawable.radio_red_color);
                                        break;

                                    case 7:
                                        ib_user6_two.setImageResource(R.drawable.radio_red_color);
                                        ib_user6_three.setImageResource(R.drawable.radio_red_color);
                                        ib_user6_four.setImageResource(R.drawable.radio_red_color);
                                        ib_user6_five.setImageResource(R.drawable.radio_red_color);
                                        ib_user6_six.setImageResource(R.drawable.radio_red_color);
                                        ib_user6_seven.setImageResource(R.drawable.radio_red_color);
                                        ib_user6_eight.setImageResource(R.drawable.radio_red_color);
                                        break;
                                }
                                break;

                            case 2:
                                ib_user6_one.setImageResource(R.drawable.radio_green_color);
                                ib_user6_two.setImageResource(R.drawable.radio_green_color);

                                switch (unpaidLegCount) {
                                    case 0:
                                        break;

                                    case 1:
                                        ib_user6_three.setImageResource(R.drawable.radio_red_color);
                                        break;

                                    case 2:
                                        ib_user6_three.setImageResource(R.drawable.radio_red_color);
                                        ib_user6_four.setImageResource(R.drawable.radio_red_color);
                                        break;

                                    case 3:
                                        ib_user6_three.setImageResource(R.drawable.radio_red_color);
                                        ib_user6_four.setImageResource(R.drawable.radio_red_color);
                                        ib_user6_five.setImageResource(R.drawable.radio_red_color);
                                        break;

                                    case 4:
                                        ib_user6_three.setImageResource(R.drawable.radio_red_color);
                                        ib_user6_four.setImageResource(R.drawable.radio_red_color);
                                        ib_user6_five.setImageResource(R.drawable.radio_red_color);
                                        ib_user6_six.setImageResource(R.drawable.radio_red_color);
                                        break;

                                    case 5:
                                        ib_user6_three.setImageResource(R.drawable.radio_red_color);
                                        ib_user6_four.setImageResource(R.drawable.radio_red_color);
                                        ib_user6_five.setImageResource(R.drawable.radio_red_color);
                                        ib_user6_six.setImageResource(R.drawable.radio_red_color);
                                        ib_user6_seven.setImageResource(R.drawable.radio_red_color);
                                        break;

                                    case 6:
                                        ib_user6_three.setImageResource(R.drawable.radio_red_color);
                                        ib_user6_four.setImageResource(R.drawable.radio_red_color);
                                        ib_user6_five.setImageResource(R.drawable.radio_red_color);
                                        ib_user6_six.setImageResource(R.drawable.radio_red_color);
                                        ib_user6_seven.setImageResource(R.drawable.radio_red_color);
                                        ib_user6_eight.setImageResource(R.drawable.radio_red_color);
                                        break;
                                }
                                break;

                            case 3:
                                ib_user6_one.setImageResource(R.drawable.radio_green_color);
                                ib_user6_two.setImageResource(R.drawable.radio_green_color);
                                ib_user6_three.setImageResource(R.drawable.radio_green_color);

                                switch (unpaidLegCount) {
                                    case 0:
                                        break;

                                    case 1:
                                        ib_user6_four.setImageResource(R.drawable.radio_red_color);
                                        break;

                                    case 2:
                                        ib_user6_four.setImageResource(R.drawable.radio_red_color);
                                        ib_user6_five.setImageResource(R.drawable.radio_red_color);
                                        break;

                                    case 3:
                                        ib_user6_four.setImageResource(R.drawable.radio_red_color);
                                        ib_user6_five.setImageResource(R.drawable.radio_red_color);
                                        ib_user6_six.setImageResource(R.drawable.radio_red_color);
                                        break;

                                    case 4:
                                        ib_user6_four.setImageResource(R.drawable.radio_red_color);
                                        ib_user6_five.setImageResource(R.drawable.radio_red_color);
                                        ib_user6_six.setImageResource(R.drawable.radio_red_color);
                                        ib_user6_seven.setImageResource(R.drawable.radio_red_color);
                                        break;

                                    case 5:
                                        ib_user6_four.setImageResource(R.drawable.radio_red_color);
                                        ib_user6_five.setImageResource(R.drawable.radio_red_color);
                                        ib_user6_six.setImageResource(R.drawable.radio_red_color);
                                        ib_user6_seven.setImageResource(R.drawable.radio_red_color);
                                        ib_user6_eight.setImageResource(R.drawable.radio_red_color);
                                        break;
                                }
                                break;

                            case 4:
                                ib_user6_one.setImageResource(R.drawable.radio_green_color);
                                ib_user6_two.setImageResource(R.drawable.radio_green_color);
                                ib_user6_three.setImageResource(R.drawable.radio_green_color);
                                ib_user6_four.setImageResource(R.drawable.radio_green_color);

                                switch (unpaidLegCount) {
                                    case 0:
                                        break;

                                    case 1:
                                        ib_user6_five.setImageResource(R.drawable.radio_red_color);
                                        break;

                                    case 2:
                                        ib_user6_five.setImageResource(R.drawable.radio_red_color);
                                        ib_user6_six.setImageResource(R.drawable.radio_red_color);
                                        break;

                                    case 3:
                                        ib_user6_five.setImageResource(R.drawable.radio_red_color);
                                        ib_user6_six.setImageResource(R.drawable.radio_red_color);
                                        ib_user6_seven.setImageResource(R.drawable.radio_red_color);
                                        break;

                                    case 4:
                                        ib_user6_five.setImageResource(R.drawable.radio_red_color);
                                        ib_user6_six.setImageResource(R.drawable.radio_red_color);
                                        ib_user6_seven.setImageResource(R.drawable.radio_red_color);
                                        ib_user6_eight.setImageResource(R.drawable.radio_red_color);
                                        break;
                                }
                                break;

                            case 5:
                                ib_user6_one.setImageResource(R.drawable.radio_green_color);
                                ib_user6_two.setImageResource(R.drawable.radio_green_color);
                                ib_user6_three.setImageResource(R.drawable.radio_green_color);
                                ib_user6_four.setImageResource(R.drawable.radio_green_color);
                                ib_user6_five.setImageResource(R.drawable.radio_green_color);

                                switch (unpaidLegCount) {
                                    case 0:
                                        break;
                                    case 1:
                                        ib_user6_six.setImageResource(R.drawable.radio_red_color);
                                        break;

                                    case 2:
                                        ib_user6_six.setImageResource(R.drawable.radio_red_color);
                                        ib_user6_seven.setImageResource(R.drawable.radio_red_color);
                                        break;

                                    case 3:
                                        ib_user6_six.setImageResource(R.drawable.radio_red_color);
                                        ib_user6_seven.setImageResource(R.drawable.radio_red_color);
                                        ib_user6_eight.setImageResource(R.drawable.radio_red_color);
                                        break;
                                }
                                break;

                            case 6:
                                ib_user6_one.setImageResource(R.drawable.radio_green_color);
                                ib_user6_two.setImageResource(R.drawable.radio_green_color);
                                ib_user6_three.setImageResource(R.drawable.radio_green_color);
                                ib_user6_four.setImageResource(R.drawable.radio_green_color);
                                ib_user6_five.setImageResource(R.drawable.radio_green_color);
                                ib_user6_six.setImageResource(R.drawable.radio_green_color);

                                switch (unpaidLegCount) {
                                    case 0:
                                        break;

                                    case 1:
                                        ib_user6_seven.setImageResource(R.drawable.radio_red_color);
                                        break;

                                    case 2:
                                        ib_user6_seven.setImageResource(R.drawable.radio_red_color);
                                        ib_user6_eight.setImageResource(R.drawable.radio_red_color);
                                        break;
                                }
                                break;

                            case 7:
                                ib_user6_one.setImageResource(R.drawable.radio_green_color);
                                ib_user6_two.setImageResource(R.drawable.radio_green_color);
                                ib_user6_three.setImageResource(R.drawable.radio_green_color);
                                ib_user6_four.setImageResource(R.drawable.radio_green_color);
                                ib_user6_five.setImageResource(R.drawable.radio_green_color);
                                ib_user6_six.setImageResource(R.drawable.radio_green_color);
                                ib_user6_seven.setImageResource(R.drawable.radio_green_color);

                                switch (unpaidLegCount) {
                                    case 0:
                                        break;

                                    case 1:
                                        ib_user6_eight.setImageResource(R.drawable.radio_red_color);
                                        break;
                                }
                                break;

                            case 8:
                                ib_user6_one.setImageResource(R.drawable.radio_green_color);
                                ib_user6_two.setImageResource(R.drawable.radio_green_color);
                                ib_user6_three.setImageResource(R.drawable.radio_green_color);
                                ib_user6_four.setImageResource(R.drawable.radio_green_color);
                                ib_user6_five.setImageResource(R.drawable.radio_green_color);
                                ib_user6_six.setImageResource(R.drawable.radio_green_color);
                                ib_user6_seven.setImageResource(R.drawable.radio_green_color);
                                ib_user6_eight.setImageResource(R.drawable.radio_green_color);

                                switch (unpaidLegCount) {
                                    case 0:
                                        break;
                                }
                                break;
                        }
                    }

                    // seventh user data-set
                    if (seven.contains("t0")) {
                        int paidLegCount = Integer.parseInt(seven.substring(seven.indexOf("#t0#") + 4, seven.indexOf("#t1#")));
                        int unpaidLegCount = Integer.parseInt(seven.substring(seven.indexOf("#t1#") + 4, seven.indexOf("#t2#")));
                        tv_direct_ref_7.setText("Direct ref:- " + seven.substring(seven.indexOf("#t2#") + 4, seven.indexOf("#t3#")));

                        switch (paidLegCount) {
                            case 0:
                                switch (unpaidLegCount) {
                                    case 0:
                                        break;

                                    case 1:
                                        ib_user7_one.setImageResource(R.drawable.radio_red_color);
                                        break;

                                    case 2:
                                        ib_user7_one.setImageResource(R.drawable.radio_red_color);
                                        ib_user7_two.setImageResource(R.drawable.radio_red_color);
                                        break;

                                    case 3:
                                        ib_user7_one.setImageResource(R.drawable.radio_red_color);
                                        ib_user7_two.setImageResource(R.drawable.radio_red_color);
                                        ib_user7_three.setImageResource(R.drawable.radio_red_color);
                                        break;

                                    case 4:
                                        ib_user7_one.setImageResource(R.drawable.radio_red_color);
                                        ib_user7_two.setImageResource(R.drawable.radio_red_color);
                                        ib_user7_three.setImageResource(R.drawable.radio_red_color);
                                        ib_user7_four.setImageResource(R.drawable.radio_red_color);
                                        break;

                                    case 5:
                                        ib_user7_one.setImageResource(R.drawable.radio_red_color);
                                        ib_user7_two.setImageResource(R.drawable.radio_red_color);
                                        ib_user7_three.setImageResource(R.drawable.radio_red_color);
                                        ib_user7_four.setImageResource(R.drawable.radio_red_color);
                                        ib_user7_five.setImageResource(R.drawable.radio_red_color);
                                        break;

                                    case 6:
                                        ib_user7_one.setImageResource(R.drawable.radio_red_color);
                                        ib_user7_two.setImageResource(R.drawable.radio_red_color);
                                        ib_user7_three.setImageResource(R.drawable.radio_red_color);
                                        ib_user7_four.setImageResource(R.drawable.radio_red_color);
                                        ib_user7_five.setImageResource(R.drawable.radio_red_color);
                                        ib_user7_six.setImageResource(R.drawable.radio_red_color);
                                        break;

                                    case 7:
                                        ib_user7_one.setImageResource(R.drawable.radio_red_color);
                                        ib_user7_two.setImageResource(R.drawable.radio_red_color);
                                        ib_user7_three.setImageResource(R.drawable.radio_red_color);
                                        ib_user7_four.setImageResource(R.drawable.radio_red_color);
                                        ib_user7_five.setImageResource(R.drawable.radio_red_color);
                                        ib_user7_six.setImageResource(R.drawable.radio_red_color);
                                        ib_user7_seven.setImageResource(R.drawable.radio_red_color);
                                        break;

                                    case 8:
                                        ib_user7_one.setImageResource(R.drawable.radio_red_color);
                                        ib_user7_two.setImageResource(R.drawable.radio_red_color);
                                        ib_user7_three.setImageResource(R.drawable.radio_red_color);
                                        ib_user7_four.setImageResource(R.drawable.radio_red_color);
                                        ib_user7_five.setImageResource(R.drawable.radio_red_color);
                                        ib_user7_six.setImageResource(R.drawable.radio_red_color);
                                        ib_user7_seven.setImageResource(R.drawable.radio_red_color);
                                        ib_user7_eight.setImageResource(R.drawable.radio_red_color);
                                        break;
                                }
                                break;

                            case 1:
                                ib_user7_one.setImageResource(R.drawable.radio_green_color);

                                switch (unpaidLegCount) {
                                    case 0:
                                        break;

                                    case 1:
                                        ib_user7_two.setImageResource(R.drawable.radio_red_color);
                                        break;

                                    case 2:
                                        ib_user7_two.setImageResource(R.drawable.radio_red_color);
                                        ib_user7_three.setImageResource(R.drawable.radio_red_color);
                                        break;

                                    case 3:
                                        ib_user7_two.setImageResource(R.drawable.radio_red_color);
                                        ib_user7_three.setImageResource(R.drawable.radio_red_color);
                                        ib_user7_four.setImageResource(R.drawable.radio_red_color);
                                        break;

                                    case 4:
                                        ib_user7_two.setImageResource(R.drawable.radio_red_color);
                                        ib_user7_three.setImageResource(R.drawable.radio_red_color);
                                        ib_user7_four.setImageResource(R.drawable.radio_red_color);
                                        ib_user7_five.setImageResource(R.drawable.radio_red_color);
                                        break;

                                    case 5:
                                        ib_user7_two.setImageResource(R.drawable.radio_red_color);
                                        ib_user7_three.setImageResource(R.drawable.radio_red_color);
                                        ib_user7_four.setImageResource(R.drawable.radio_red_color);
                                        ib_user7_five.setImageResource(R.drawable.radio_red_color);
                                        ib_user7_six.setImageResource(R.drawable.radio_red_color);
                                        break;

                                    case 6:
                                        ib_user7_two.setImageResource(R.drawable.radio_red_color);
                                        ib_user7_three.setImageResource(R.drawable.radio_red_color);
                                        ib_user7_four.setImageResource(R.drawable.radio_red_color);
                                        ib_user7_five.setImageResource(R.drawable.radio_red_color);
                                        ib_user7_six.setImageResource(R.drawable.radio_red_color);
                                        ib_user7_seven.setImageResource(R.drawable.radio_red_color);
                                        break;

                                    case 7:
                                        ib_user7_two.setImageResource(R.drawable.radio_red_color);
                                        ib_user7_three.setImageResource(R.drawable.radio_red_color);
                                        ib_user7_four.setImageResource(R.drawable.radio_red_color);
                                        ib_user7_five.setImageResource(R.drawable.radio_red_color);
                                        ib_user7_six.setImageResource(R.drawable.radio_red_color);
                                        ib_user7_seven.setImageResource(R.drawable.radio_red_color);
                                        ib_user7_eight.setImageResource(R.drawable.radio_red_color);
                                        break;
                                }
                                break;

                            case 2:
                                ib_user7_one.setImageResource(R.drawable.radio_green_color);
                                ib_user7_two.setImageResource(R.drawable.radio_green_color);

                                switch (unpaidLegCount) {
                                    case 0:
                                        break;

                                    case 1:
                                        ib_user7_three.setImageResource(R.drawable.radio_red_color);
                                        break;

                                    case 2:
                                        ib_user7_three.setImageResource(R.drawable.radio_red_color);
                                        ib_user7_four.setImageResource(R.drawable.radio_red_color);
                                        break;

                                    case 3:
                                        ib_user7_three.setImageResource(R.drawable.radio_red_color);
                                        ib_user7_four.setImageResource(R.drawable.radio_red_color);
                                        ib_user7_five.setImageResource(R.drawable.radio_red_color);
                                        break;

                                    case 4:
                                        ib_user7_three.setImageResource(R.drawable.radio_red_color);
                                        ib_user7_four.setImageResource(R.drawable.radio_red_color);
                                        ib_user7_five.setImageResource(R.drawable.radio_red_color);
                                        ib_user7_six.setImageResource(R.drawable.radio_red_color);
                                        break;

                                    case 5:
                                        ib_user7_three.setImageResource(R.drawable.radio_red_color);
                                        ib_user7_four.setImageResource(R.drawable.radio_red_color);
                                        ib_user7_five.setImageResource(R.drawable.radio_red_color);
                                        ib_user7_six.setImageResource(R.drawable.radio_red_color);
                                        ib_user7_seven.setImageResource(R.drawable.radio_red_color);
                                        break;

                                    case 6:
                                        ib_user7_three.setImageResource(R.drawable.radio_red_color);
                                        ib_user7_four.setImageResource(R.drawable.radio_red_color);
                                        ib_user7_five.setImageResource(R.drawable.radio_red_color);
                                        ib_user7_six.setImageResource(R.drawable.radio_red_color);
                                        ib_user7_seven.setImageResource(R.drawable.radio_red_color);
                                        ib_user7_eight.setImageResource(R.drawable.radio_red_color);
                                        break;
                                }
                                break;

                            case 3:
                                ib_user7_one.setImageResource(R.drawable.radio_green_color);
                                ib_user7_two.setImageResource(R.drawable.radio_green_color);
                                ib_user7_three.setImageResource(R.drawable.radio_green_color);

                                switch (unpaidLegCount) {
                                    case 0:
                                        break;

                                    case 1:
                                        ib_user7_four.setImageResource(R.drawable.radio_red_color);
                                        break;

                                    case 2:
                                        ib_user7_four.setImageResource(R.drawable.radio_red_color);
                                        ib_user7_five.setImageResource(R.drawable.radio_red_color);
                                        break;

                                    case 3:
                                        ib_user7_four.setImageResource(R.drawable.radio_red_color);
                                        ib_user7_five.setImageResource(R.drawable.radio_red_color);
                                        ib_user7_six.setImageResource(R.drawable.radio_red_color);
                                        break;

                                    case 4:
                                        ib_user7_four.setImageResource(R.drawable.radio_red_color);
                                        ib_user7_five.setImageResource(R.drawable.radio_red_color);
                                        ib_user7_six.setImageResource(R.drawable.radio_red_color);
                                        ib_user7_seven.setImageResource(R.drawable.radio_red_color);
                                        break;

                                    case 5:
                                        ib_user7_four.setImageResource(R.drawable.radio_red_color);
                                        ib_user7_five.setImageResource(R.drawable.radio_red_color);
                                        ib_user7_six.setImageResource(R.drawable.radio_red_color);
                                        ib_user7_seven.setImageResource(R.drawable.radio_red_color);
                                        ib_user7_eight.setImageResource(R.drawable.radio_red_color);
                                        break;
                                }
                                break;

                            case 4:
                                ib_user7_one.setImageResource(R.drawable.radio_green_color);
                                ib_user7_two.setImageResource(R.drawable.radio_green_color);
                                ib_user7_three.setImageResource(R.drawable.radio_green_color);
                                ib_user7_four.setImageResource(R.drawable.radio_green_color);

                                switch (unpaidLegCount) {
                                    case 0:
                                        break;

                                    case 1:
                                        ib_user7_five.setImageResource(R.drawable.radio_red_color);
                                        break;

                                    case 2:
                                        ib_user7_five.setImageResource(R.drawable.radio_red_color);
                                        ib_user7_six.setImageResource(R.drawable.radio_red_color);
                                        break;

                                    case 3:
                                        ib_user7_five.setImageResource(R.drawable.radio_red_color);
                                        ib_user7_six.setImageResource(R.drawable.radio_red_color);
                                        ib_user7_seven.setImageResource(R.drawable.radio_red_color);
                                        break;

                                    case 4:
                                        ib_user7_five.setImageResource(R.drawable.radio_red_color);
                                        ib_user7_six.setImageResource(R.drawable.radio_red_color);
                                        ib_user7_seven.setImageResource(R.drawable.radio_red_color);
                                        ib_user7_eight.setImageResource(R.drawable.radio_red_color);
                                        break;
                                }
                                break;

                            case 5:
                                ib_user7_one.setImageResource(R.drawable.radio_green_color);
                                ib_user7_two.setImageResource(R.drawable.radio_green_color);
                                ib_user7_three.setImageResource(R.drawable.radio_green_color);
                                ib_user7_four.setImageResource(R.drawable.radio_green_color);
                                ib_user7_five.setImageResource(R.drawable.radio_green_color);

                                switch (unpaidLegCount) {
                                    case 0:
                                        break;
                                    case 1:
                                        ib_user7_six.setImageResource(R.drawable.radio_red_color);
                                        break;

                                    case 2:
                                        ib_user7_six.setImageResource(R.drawable.radio_red_color);
                                        ib_user7_seven.setImageResource(R.drawable.radio_red_color);
                                        break;

                                    case 3:
                                        ib_user7_six.setImageResource(R.drawable.radio_red_color);
                                        ib_user7_seven.setImageResource(R.drawable.radio_red_color);
                                        ib_user7_eight.setImageResource(R.drawable.radio_red_color);
                                        break;
                                }
                                break;

                            case 6:
                                ib_user7_one.setImageResource(R.drawable.radio_green_color);
                                ib_user7_two.setImageResource(R.drawable.radio_green_color);
                                ib_user7_three.setImageResource(R.drawable.radio_green_color);
                                ib_user7_four.setImageResource(R.drawable.radio_green_color);
                                ib_user7_five.setImageResource(R.drawable.radio_green_color);
                                ib_user7_six.setImageResource(R.drawable.radio_green_color);

                                switch (unpaidLegCount) {
                                    case 0:
                                        break;

                                    case 1:
                                        ib_user7_seven.setImageResource(R.drawable.radio_red_color);
                                        break;

                                    case 2:
                                        ib_user7_seven.setImageResource(R.drawable.radio_red_color);
                                        ib_user7_eight.setImageResource(R.drawable.radio_red_color);
                                        break;
                                }
                                break;

                            case 7:
                                ib_user7_one.setImageResource(R.drawable.radio_green_color);
                                ib_user7_two.setImageResource(R.drawable.radio_green_color);
                                ib_user7_three.setImageResource(R.drawable.radio_green_color);
                                ib_user7_four.setImageResource(R.drawable.radio_green_color);
                                ib_user7_five.setImageResource(R.drawable.radio_green_color);
                                ib_user7_six.setImageResource(R.drawable.radio_green_color);
                                ib_user7_seven.setImageResource(R.drawable.radio_green_color);

                                switch (unpaidLegCount) {
                                    case 0:
                                        break;

                                    case 1:
                                        ib_user7_eight.setImageResource(R.drawable.radio_red_color);
                                        break;
                                }
                                break;

                            case 8:
                                ib_user7_one.setImageResource(R.drawable.radio_green_color);
                                ib_user7_two.setImageResource(R.drawable.radio_green_color);
                                ib_user7_three.setImageResource(R.drawable.radio_green_color);
                                ib_user7_four.setImageResource(R.drawable.radio_green_color);
                                ib_user7_five.setImageResource(R.drawable.radio_green_color);
                                ib_user7_six.setImageResource(R.drawable.radio_green_color);
                                ib_user7_seven.setImageResource(R.drawable.radio_green_color);
                                ib_user7_eight.setImageResource(R.drawable.radio_green_color);

                                switch (unpaidLegCount) {
                                    case 0:
                                        break;
                                }
                                break;

                        }
                    }

                    /// eight user data-set
                    if (eight.contains("t0")) {
                        int paidLegCount = Integer.parseInt(eight.substring(eight.indexOf("#t0#") + 4, eight.indexOf("#t1#")));
                        int unpaidLegCount = Integer.parseInt(eight.substring(eight.indexOf("#t1#") + 4, eight.indexOf("#t2#")));
                        tv_direct_ref_8.setText("Direct ref:- " + eight.substring(eight.indexOf("#t2#") + 4, eight.indexOf("#t3#")));

                        switch (paidLegCount) {
                            case 0:
                                switch (unpaidLegCount) {
                                    case 0:
                                        break;

                                    case 1:
                                        ib_user8_one.setImageResource(R.drawable.radio_red_color);
                                        break;

                                    case 2:
                                        ib_user8_one.setImageResource(R.drawable.radio_red_color);
                                        ib_user8_two.setImageResource(R.drawable.radio_red_color);
                                        break;

                                    case 3:
                                        ib_user8_one.setImageResource(R.drawable.radio_red_color);
                                        ib_user8_two.setImageResource(R.drawable.radio_red_color);
                                        ib_user8_three.setImageResource(R.drawable.radio_red_color);
                                        break;

                                    case 4:
                                        ib_user8_one.setImageResource(R.drawable.radio_red_color);
                                        ib_user8_two.setImageResource(R.drawable.radio_red_color);
                                        ib_user8_three.setImageResource(R.drawable.radio_red_color);
                                        ib_user8_four.setImageResource(R.drawable.radio_red_color);
                                        break;

                                    case 5:
                                        ib_user8_one.setImageResource(R.drawable.radio_red_color);
                                        ib_user8_two.setImageResource(R.drawable.radio_red_color);
                                        ib_user8_three.setImageResource(R.drawable.radio_red_color);
                                        ib_user8_four.setImageResource(R.drawable.radio_red_color);
                                        ib_user8_five.setImageResource(R.drawable.radio_red_color);
                                        break;

                                    case 6:
                                        ib_user8_one.setImageResource(R.drawable.radio_red_color);
                                        ib_user8_two.setImageResource(R.drawable.radio_red_color);
                                        ib_user8_three.setImageResource(R.drawable.radio_red_color);
                                        ib_user8_four.setImageResource(R.drawable.radio_red_color);
                                        ib_user8_five.setImageResource(R.drawable.radio_red_color);
                                        ib_user8_six.setImageResource(R.drawable.radio_red_color);
                                        break;

                                    case 7:
                                        ib_user8_one.setImageResource(R.drawable.radio_red_color);
                                        ib_user8_two.setImageResource(R.drawable.radio_red_color);
                                        ib_user8_three.setImageResource(R.drawable.radio_red_color);
                                        ib_user8_four.setImageResource(R.drawable.radio_red_color);
                                        ib_user8_five.setImageResource(R.drawable.radio_red_color);
                                        ib_user8_six.setImageResource(R.drawable.radio_red_color);
                                        ib_user8_seven.setImageResource(R.drawable.radio_red_color);
                                        break;

                                    case 8:
                                        ib_user8_one.setImageResource(R.drawable.radio_red_color);
                                        ib_user8_two.setImageResource(R.drawable.radio_red_color);
                                        ib_user8_three.setImageResource(R.drawable.radio_red_color);
                                        ib_user8_four.setImageResource(R.drawable.radio_red_color);
                                        ib_user8_five.setImageResource(R.drawable.radio_red_color);
                                        ib_user8_six.setImageResource(R.drawable.radio_red_color);
                                        ib_user8_seven.setImageResource(R.drawable.radio_red_color);
                                        ib_user8_eight.setImageResource(R.drawable.radio_red_color);
                                        break;
                                }
                                break;

                            case 1:
                                ib_user8_one.setImageResource(R.drawable.radio_green_color);

                                switch (unpaidLegCount) {
                                    case 0:
                                        break;

                                    case 1:
                                        ib_user8_two.setImageResource(R.drawable.radio_red_color);
                                        break;

                                    case 2:
                                        ib_user8_two.setImageResource(R.drawable.radio_red_color);
                                        ib_user8_three.setImageResource(R.drawable.radio_red_color);
                                        break;

                                    case 3:
                                        ib_user8_two.setImageResource(R.drawable.radio_red_color);
                                        ib_user8_three.setImageResource(R.drawable.radio_red_color);
                                        ib_user8_four.setImageResource(R.drawable.radio_red_color);
                                        break;

                                    case 4:
                                        ib_user8_two.setImageResource(R.drawable.radio_red_color);
                                        ib_user8_three.setImageResource(R.drawable.radio_red_color);
                                        ib_user8_four.setImageResource(R.drawable.radio_red_color);
                                        ib_user8_five.setImageResource(R.drawable.radio_red_color);
                                        break;

                                    case 5:
                                        ib_user8_two.setImageResource(R.drawable.radio_red_color);
                                        ib_user8_three.setImageResource(R.drawable.radio_red_color);
                                        ib_user8_four.setImageResource(R.drawable.radio_red_color);
                                        ib_user8_five.setImageResource(R.drawable.radio_red_color);
                                        ib_user8_six.setImageResource(R.drawable.radio_red_color);
                                        break;

                                    case 6:
                                        ib_user8_two.setImageResource(R.drawable.radio_red_color);
                                        ib_user8_three.setImageResource(R.drawable.radio_red_color);
                                        ib_user8_four.setImageResource(R.drawable.radio_red_color);
                                        ib_user8_five.setImageResource(R.drawable.radio_red_color);
                                        ib_user8_six.setImageResource(R.drawable.radio_red_color);
                                        ib_user8_seven.setImageResource(R.drawable.radio_red_color);
                                        break;

                                    case 7:
                                        ib_user8_two.setImageResource(R.drawable.radio_red_color);
                                        ib_user8_three.setImageResource(R.drawable.radio_red_color);
                                        ib_user8_four.setImageResource(R.drawable.radio_red_color);
                                        ib_user8_five.setImageResource(R.drawable.radio_red_color);
                                        ib_user8_six.setImageResource(R.drawable.radio_red_color);
                                        ib_user8_seven.setImageResource(R.drawable.radio_red_color);
                                        ib_user8_eight.setImageResource(R.drawable.radio_red_color);
                                        break;
                                }
                                break;

                            case 2:
                                ib_user8_one.setImageResource(R.drawable.radio_green_color);
                                ib_user8_two.setImageResource(R.drawable.radio_green_color);

                                switch (unpaidLegCount) {
                                    case 0:
                                        break;

                                    case 1:
                                        ib_user8_three.setImageResource(R.drawable.radio_red_color);
                                        break;

                                    case 2:
                                        ib_user8_three.setImageResource(R.drawable.radio_red_color);
                                        ib_user8_four.setImageResource(R.drawable.radio_red_color);
                                        break;

                                    case 3:
                                        ib_user8_three.setImageResource(R.drawable.radio_red_color);
                                        ib_user8_four.setImageResource(R.drawable.radio_red_color);
                                        ib_user8_five.setImageResource(R.drawable.radio_red_color);
                                        break;

                                    case 4:
                                        ib_user8_three.setImageResource(R.drawable.radio_red_color);
                                        ib_user8_four.setImageResource(R.drawable.radio_red_color);
                                        ib_user8_five.setImageResource(R.drawable.radio_red_color);
                                        ib_user8_six.setImageResource(R.drawable.radio_red_color);
                                        break;

                                    case 5:
                                        ib_user8_three.setImageResource(R.drawable.radio_red_color);
                                        ib_user8_four.setImageResource(R.drawable.radio_red_color);
                                        ib_user8_five.setImageResource(R.drawable.radio_red_color);
                                        ib_user8_six.setImageResource(R.drawable.radio_red_color);
                                        ib_user8_seven.setImageResource(R.drawable.radio_red_color);
                                        break;

                                    case 6:
                                        ib_user8_three.setImageResource(R.drawable.radio_red_color);
                                        ib_user8_four.setImageResource(R.drawable.radio_red_color);
                                        ib_user8_five.setImageResource(R.drawable.radio_red_color);
                                        ib_user8_six.setImageResource(R.drawable.radio_red_color);
                                        ib_user8_seven.setImageResource(R.drawable.radio_red_color);
                                        ib_user8_eight.setImageResource(R.drawable.radio_red_color);
                                        break;
                                }
                                break;

                            case 3:
                                ib_user8_one.setImageResource(R.drawable.radio_green_color);
                                ib_user8_two.setImageResource(R.drawable.radio_green_color);
                                ib_user8_three.setImageResource(R.drawable.radio_green_color);

                                switch (unpaidLegCount) {
                                    case 0:
                                        break;

                                    case 1:
                                        ib_user8_four.setImageResource(R.drawable.radio_red_color);
                                        break;

                                    case 2:
                                        ib_user8_four.setImageResource(R.drawable.radio_red_color);
                                        ib_user8_five.setImageResource(R.drawable.radio_red_color);
                                        break;

                                    case 3:
                                        ib_user8_four.setImageResource(R.drawable.radio_red_color);
                                        ib_user8_five.setImageResource(R.drawable.radio_red_color);
                                        ib_user8_six.setImageResource(R.drawable.radio_red_color);
                                        break;

                                    case 4:
                                        ib_user8_four.setImageResource(R.drawable.radio_red_color);
                                        ib_user8_five.setImageResource(R.drawable.radio_red_color);
                                        ib_user8_six.setImageResource(R.drawable.radio_red_color);
                                        ib_user8_seven.setImageResource(R.drawable.radio_red_color);
                                        break;

                                    case 5:
                                        ib_user8_four.setImageResource(R.drawable.radio_red_color);
                                        ib_user8_five.setImageResource(R.drawable.radio_red_color);
                                        ib_user8_six.setImageResource(R.drawable.radio_red_color);
                                        ib_user8_seven.setImageResource(R.drawable.radio_red_color);
                                        ib_user8_eight.setImageResource(R.drawable.radio_red_color);
                                        break;
                                }
                                break;

                            case 4:
                                ib_user8_one.setImageResource(R.drawable.radio_green_color);
                                ib_user8_two.setImageResource(R.drawable.radio_green_color);
                                ib_user8_three.setImageResource(R.drawable.radio_green_color);
                                ib_user8_four.setImageResource(R.drawable.radio_green_color);

                                switch (unpaidLegCount) {
                                    case 0:
                                        break;

                                    case 1:
                                        ib_user8_five.setImageResource(R.drawable.radio_red_color);
                                        break;

                                    case 2:
                                        ib_user8_five.setImageResource(R.drawable.radio_red_color);
                                        ib_user8_six.setImageResource(R.drawable.radio_red_color);
                                        break;

                                    case 3:
                                        ib_user8_five.setImageResource(R.drawable.radio_red_color);
                                        ib_user8_six.setImageResource(R.drawable.radio_red_color);
                                        ib_user8_seven.setImageResource(R.drawable.radio_red_color);
                                        break;

                                    case 4:
                                        ib_user8_five.setImageResource(R.drawable.radio_red_color);
                                        ib_user8_six.setImageResource(R.drawable.radio_red_color);
                                        ib_user8_seven.setImageResource(R.drawable.radio_red_color);
                                        ib_user8_eight.setImageResource(R.drawable.radio_red_color);
                                        break;
                                }
                                break;

                            case 5:
                                ib_user8_one.setImageResource(R.drawable.radio_green_color);
                                ib_user8_two.setImageResource(R.drawable.radio_green_color);
                                ib_user8_three.setImageResource(R.drawable.radio_green_color);
                                ib_user8_four.setImageResource(R.drawable.radio_green_color);
                                ib_user8_five.setImageResource(R.drawable.radio_green_color);

                                switch (unpaidLegCount) {
                                    case 0:
                                        break;
                                    case 1:
                                        ib_user8_six.setImageResource(R.drawable.radio_red_color);
                                        break;

                                    case 2:
                                        ib_user8_six.setImageResource(R.drawable.radio_red_color);
                                        ib_user8_seven.setImageResource(R.drawable.radio_red_color);
                                        break;

                                    case 3:
                                        ib_user8_six.setImageResource(R.drawable.radio_red_color);
                                        ib_user8_seven.setImageResource(R.drawable.radio_red_color);
                                        ib_user8_eight.setImageResource(R.drawable.radio_red_color);
                                        break;
                                }
                                break;

                            case 6:
                                ib_user8_one.setImageResource(R.drawable.radio_green_color);
                                ib_user8_two.setImageResource(R.drawable.radio_green_color);
                                ib_user8_three.setImageResource(R.drawable.radio_green_color);
                                ib_user8_four.setImageResource(R.drawable.radio_green_color);
                                ib_user8_five.setImageResource(R.drawable.radio_green_color);
                                ib_user8_six.setImageResource(R.drawable.radio_green_color);

                                switch (unpaidLegCount) {
                                    case 0:
                                        break;

                                    case 1:
                                        ib_user8_seven.setImageResource(R.drawable.radio_red_color);
                                        break;

                                    case 2:
                                        ib_user8_seven.setImageResource(R.drawable.radio_red_color);
                                        ib_user8_eight.setImageResource(R.drawable.radio_red_color);
                                        break;
                                }
                                break;

                            case 7:
                                ib_user8_one.setImageResource(R.drawable.radio_green_color);
                                ib_user8_two.setImageResource(R.drawable.radio_green_color);
                                ib_user8_three.setImageResource(R.drawable.radio_green_color);
                                ib_user8_four.setImageResource(R.drawable.radio_green_color);
                                ib_user8_five.setImageResource(R.drawable.radio_green_color);
                                ib_user8_six.setImageResource(R.drawable.radio_green_color);
                                ib_user8_seven.setImageResource(R.drawable.radio_green_color);

                                switch (unpaidLegCount) {
                                    case 0:
                                        break;

                                    case 1:
                                        ib_user8_eight.setImageResource(R.drawable.radio_red_color);
                                        break;
                                }
                                break;

                            case 8:
                                ib_user8_one.setImageResource(R.drawable.radio_green_color);
                                ib_user8_two.setImageResource(R.drawable.radio_green_color);
                                ib_user8_three.setImageResource(R.drawable.radio_green_color);
                                ib_user8_four.setImageResource(R.drawable.radio_green_color);
                                ib_user8_five.setImageResource(R.drawable.radio_green_color);
                                ib_user8_six.setImageResource(R.drawable.radio_green_color);
                                ib_user8_seven.setImageResource(R.drawable.radio_green_color);
                                ib_user8_eight.setImageResource(R.drawable.radio_green_color);

                                switch (unpaidLegCount) {
                                    case 0:
                                        break;
                                }
                                break;
                        }
                    }


                }

                if (response.contains("idisempty")) {
                    progressDialog_legInfo.dismiss();
                    Toast.makeText(DisplayTeamOLD.this, "no data found.", Toast.LENGTH_SHORT).show();
                }

                if (response.contains("nodatafound")) {
                    progressDialog_legInfo.dismiss();
                    Toast.makeText(DisplayTeamOLD.this, "no data found.", Toast.LENGTH_SHORT).show();
                }

                if (response.contains("dbConErr")) {
                    progressDialog_legInfo.dismiss();
                    Toast.makeText(DisplayTeamOLD.this, "Database connection error.", Toast.LENGTH_SHORT).show();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(DisplayTeamOLD.this, "Something went wrong.", Toast.LENGTH_SHORT).show();
                progressDialog_legInfo.dismiss();
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> map = new HashMap<>();
                map.put("id", id);
                return map;
            }
        };
        Volley.newRequestQueue(DisplayTeamOLD.this).add(stringRequest);
    }

    @Override
    public void onBackPressed() {
        isBackpress = true;
        id = "";
        String leg_exp = (leg_expansion.getString("userid", ""));

        if (leg_exp.length() > 11) {
            String retriveData = leg_exp.substring(leg_exp.length() - 22, leg_exp.length() - 11);

            id = retriveData;
            leg_expansion = getSharedPreferences("legs_data", MODE_PRIVATE);
            SharedPreferences.Editor edit = leg_expansion.edit();
            String str = leg_exp.substring(0, leg_exp.length() - 11);
            edit.putString("userid", str);
            edit.commit();

            Log.d("spData148", "sentto db " + id);
            colorChangedBlack();
            getUserDataFromDB();
        } else {
            super.onBackPressed();
        }
    }

    public void colorChangedBlack() {
        tv_name1.setTextColor(Color.BLACK);
        tv_name2.setTextColor(Color.BLACK);
        tv_name3.setTextColor(Color.BLACK);
        tv_name4.setTextColor(Color.BLACK);
        tv_name5.setTextColor(Color.BLACK);
        tv_name6.setTextColor(Color.BLACK);
        tv_name7.setTextColor(Color.BLACK);
        tv_name8.setTextColor(Color.BLACK);

        tv_userid1.setTextColor(Color.BLACK);
        tv_userid2.setTextColor(Color.BLACK);
        tv_userid3.setTextColor(Color.BLACK);
        tv_userid4.setTextColor(Color.BLACK);
        tv_userid5.setTextColor(Color.BLACK);
        tv_userid6.setTextColor(Color.BLACK);
        tv_userid7.setTextColor(Color.BLACK);
        tv_userid8.setTextColor(Color.BLACK);
    }
}
