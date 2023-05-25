package com.ithublive.ktclife;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

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

public class DirectRefDisplayTeamActivity extends AppCompatActivity {

    TextView tv_name1, tv_name2, tv_name3, tv_name4, tv_name5, tv_name6, tv_name7, tv_name8;
    TextView tv_userid1, tv_userid2, tv_userid3, tv_userid4, tv_userid5, tv_userid6, tv_userid7, tv_userid8;
    TextView tv_directref1, tv_directref2, tv_directref3, tv_directref4, tv_directref5, tv_directref6, tv_directref7, tv_directref8;
    RelativeLayout rl1, rl2, rl3, rl4, rl5, rl6, rl7, rl8;
    CircleImageView profile_img1, profile_img2, profile_img3, profile_img4, profile_img5, profile_img6, profile_img7, profile_img8;
    String id, username = "", keyword, idUrl;
    SharedPreferences leg_expansion, sp;
    TextView tv_legs, tv_noleg_msg;
    HashMap<String, LegMapModel> idUrl_map = new HashMap<String, LegMapModel>();//Creating HashMap
    Dialog dialog;
    boolean isHavingLeg = true, isBackpress;

    CircleImageView ib_directref_user1_one, ib_directref_user1_two, ib_directref_user1_three, ib_directref_user1_four, ib_directref_user1_five, ib_directref_user1_six, ib_directref_user1_seven, ib_directref_user1_eight,
            ib_directref_user2_one, ib_directref_user2_two, ib_directref_user2_three, ib_directref_user2_four, ib_directref_user2_five, ib_directref_user2_six, ib_directref_user2_seven, ib_directref_user2_eight,
            ib_directref_user3_one, ib_directref_user3_two, ib_directref_user3_three, ib_directref_user3_four, ib_directref_user3_five, ib_directref_user3_six, ib_directref_user3_seven, ib_directref_user3_eight,
            ib_directref_user4_one, ib_directref_user4_two, ib_directref_user4_three, ib_directref_user4_four, ib_directref_user4_five, ib_directref_user4_six, ib_directref_user4_seven, ib_directref_user4_eight,
            ib_directref_user5_one, ib_directref_user5_two, ib_directref_user5_three, ib_directref_user5_four, ib_directref_user5_five, ib_directref_user5_six, ib_directref_user5_seven, ib_directref_user5_eight,
            ib_directref_user6_one, ib_directref_user6_two, ib_directref_user6_three, ib_directref_user6_four, ib_directref_user6_five, ib_directref_user6_six, ib_directref_user6_seven, ib_directref_user6_eight,
            ib_directref_user7_one, ib_directref_user7_two, ib_directref_user7_three, ib_directref_user7_four, ib_directref_user7_five, ib_directref_user7_six, ib_directref_user7_seven, ib_directref_user7_eight,
            ib_directref_user8_one, ib_directref_user8_two, ib_directref_user8_three, ib_directref_user8_four, ib_directref_user8_five, ib_directref_user8_six, ib_directref_user8_seven, ib_directref_user8_eight;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_direct_ref_display_team);

        tv_legs = findViewById(R.id.textview_directref_legs);
        tv_noleg_msg = findViewById(R.id.tv_directref_nolegs_msg);
        tv_noleg_msg.setVisibility(View.INVISIBLE);

        id = getIntent().getStringExtra("userid");

        leg_expansion = getSharedPreferences("legs_data", MODE_PRIVATE);
        SharedPreferences.Editor edit = leg_expansion.edit();
        edit.putString("userid", id);
        edit.commit();

        dialog = new Dialog(DirectRefDisplayTeamActivity.this);
        dialog.setContentView(R.layout.layout_progress_dialog);
        if (dialog.getWindow() != null) {
            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(0));
        }
        dialog.setCancelable(false);

        tv_directref1 = findViewById(R.id.tv_directref);
        tv_directref2 = findViewById(R.id.tv_directref_2);
        tv_directref3 = findViewById(R.id.tv_directref_3);
        tv_directref4 = findViewById(R.id.tv_directref_4);
        tv_directref5 = findViewById(R.id.tv_directref_5);
        tv_directref6 = findViewById(R.id.tv_directref_6);
        tv_directref7 = findViewById(R.id.tv_directref_7);
        tv_directref8 = findViewById(R.id.tv_directref_8);

        ib_directref_user1_one = findViewById(R.id.ib_directref_user1_one);
        ib_directref_user1_two = findViewById(R.id.ib_directref_user1_two);
        ib_directref_user1_three = findViewById(R.id.ib_directref_user1_three);
        ib_directref_user1_four = findViewById(R.id.ib_directref_user1_four);
        ib_directref_user1_five = findViewById(R.id.ib_directref_user1_five);
        ib_directref_user1_six = findViewById(R.id.ib_directref_user1_six);
        ib_directref_user1_seven = findViewById(R.id.ib_directref_user1_seven);
        ib_directref_user1_eight = findViewById(R.id.ib_directref_user1_eight);

        ib_directref_user2_one = findViewById(R.id.ib_directref_user2_one);
        ib_directref_user2_two = findViewById(R.id.ib_directref_user2_two);
        ib_directref_user2_three = findViewById(R.id.ib_directref_user2_three);
        ib_directref_user2_four = findViewById(R.id.ib_directref_user2_four);
        ib_directref_user2_five = findViewById(R.id.ib_directref_user2_five);
        ib_directref_user2_six = findViewById(R.id.ib_directref_user2_six);
        ib_directref_user2_seven = findViewById(R.id.ib_directref_user2_seven);
        ib_directref_user2_eight = findViewById(R.id.ib_directref_user2_eight);

        ib_directref_user3_one = findViewById(R.id.ib_directref_user3_one);
        ib_directref_user3_two = findViewById(R.id.ib_directref_user3_two);
        ib_directref_user3_three = findViewById(R.id.ib_directref_user3_three);
        ib_directref_user3_four = findViewById(R.id.ib_directref_user3_four);
        ib_directref_user3_five = findViewById(R.id.ib_directref_user3_five);
        ib_directref_user3_six = findViewById(R.id.ib_directref_user3_six);
        ib_directref_user3_seven = findViewById(R.id.ib_directref_user3_seven);
        ib_directref_user3_eight = findViewById(R.id.ib_directref_user3_eight);

        ib_directref_user4_one = findViewById(R.id.ib_directref_user4_one);
        ib_directref_user4_two = findViewById(R.id.ib_directref_user4_two);
        ib_directref_user4_three = findViewById(R.id.ib_directref_user4_three);
        ib_directref_user4_four = findViewById(R.id.ib_directref_user4_four);
        ib_directref_user4_five = findViewById(R.id.ib_directref_user4_five);
        ib_directref_user4_six = findViewById(R.id.ib_directref_user4_six);
        ib_directref_user4_seven = findViewById(R.id.ib_directref_user4_seven);
        ib_directref_user4_eight = findViewById(R.id.ib_directref_user4_eight);

        ib_directref_user5_one = findViewById(R.id.ib_directref_user5_one);
        ib_directref_user5_two = findViewById(R.id.ib_directref_user5_two);
        ib_directref_user5_three = findViewById(R.id.ib_directref_user5_three);
        ib_directref_user5_four = findViewById(R.id.ib_directref_user5_four);
        ib_directref_user5_five = findViewById(R.id.ib_directref_user5_five);
        ib_directref_user5_six = findViewById(R.id.ib_directref_user5_six);
        ib_directref_user5_seven = findViewById(R.id.ib_directref_user5_seven);
        ib_directref_user5_eight = findViewById(R.id.ib_directref_user5_eight);

        ib_directref_user6_one = findViewById(R.id.ib_directref_user6_one);
        ib_directref_user6_two = findViewById(R.id.ib_directref_user6_two);
        ib_directref_user6_three = findViewById(R.id.ib_directref_user6_three);
        ib_directref_user6_four = findViewById(R.id.ib_directref_user6_four);
        ib_directref_user6_five = findViewById(R.id.ib_directref_user6_five);
        ib_directref_user6_six = findViewById(R.id.ib_directref_user6_six);
        ib_directref_user6_seven = findViewById(R.id.ib_directref_user6_seven);
        ib_directref_user6_eight = findViewById(R.id.ib_directref_user6_eight);

        ib_directref_user7_one = findViewById(R.id.ib_directref_user7_one);
        ib_directref_user7_two = findViewById(R.id.ib_directref_user7_two);
        ib_directref_user7_three = findViewById(R.id.ib_directref_user7_three);
        ib_directref_user7_four = findViewById(R.id.ib_directref_user7_four);
        ib_directref_user7_five = findViewById(R.id.ib_directref_user7_five);
        ib_directref_user7_six = findViewById(R.id.ib_directref_user7_six);
        ib_directref_user7_seven = findViewById(R.id.ib_directref_user7_seven);
        ib_directref_user7_eight = findViewById(R.id.ib_directref_user7_eight);

        ib_directref_user8_one = findViewById(R.id.ib_directref_user8_one);
        ib_directref_user8_two = findViewById(R.id.ib_directref_user8_two);
        ib_directref_user8_three = findViewById(R.id.ib_directref_user8_three);
        ib_directref_user8_four = findViewById(R.id.ib_directref_user8_four);
        ib_directref_user8_five = findViewById(R.id.ib_directref_user8_five);
        ib_directref_user8_six = findViewById(R.id.ib_directref_user8_six);
        ib_directref_user8_seven = findViewById(R.id.ib_directref_user8_seven);
        ib_directref_user8_eight = findViewById(R.id.ib_directref_user8_eight);

        rl1 = findViewById(R.id.relativelayout1_directref);
        rl2 = findViewById(R.id.relativelayout2_directref);
        rl3 = findViewById(R.id.relativelayout3_directref);
        rl4 = findViewById(R.id.relativelayout4_directref);
        rl5 = findViewById(R.id.relativelayout5_directref);
        rl6 = findViewById(R.id.relativelayout6_directref);
        rl7 = findViewById(R.id.relativelayout7_directref);
        rl8 = findViewById(R.id.relativelayout8_directref);

        profile_img1 = findViewById(R.id.directref_profile_img);
        profile_img2 = findViewById(R.id.directref_profile_img_2);
        profile_img3 = findViewById(R.id.directref_profile_img_3);
        profile_img4 = findViewById(R.id.directref_profile_img_4);
        profile_img5 = findViewById(R.id.directref_profile_img_5);
        profile_img6 = findViewById(R.id.directref_profile_img_6);
        profile_img7 = findViewById(R.id.directref_profile_img_7);
        profile_img8 = findViewById(R.id.directref_profile_img_8);

        tv_name1 = findViewById(R.id.tv_directref_name);
        tv_name2 = findViewById(R.id.tv_directref_name_2);
        tv_name3 = findViewById(R.id.tv_directref_name_3);
        tv_name4 = findViewById(R.id.tv_directref_name_4);
        tv_name5 = findViewById(R.id.tv_directref_name_5);
        tv_name6 = findViewById(R.id.tv_directref_name_6);
        tv_name7 = findViewById(R.id.tv_directref_name_7);
        tv_name8 = findViewById(R.id.tv_directref_name_8);

        tv_userid1 = findViewById(R.id.tv_directref_userid);
        tv_userid2 = findViewById(R.id.tv_directref_userid_2);
        tv_userid3 = findViewById(R.id.tv_directref_userid_3);
        tv_userid4 = findViewById(R.id.tv_directref_userid_4);
        tv_userid5 = findViewById(R.id.tv_directref_userid_5);
        tv_userid6 = findViewById(R.id.tv_directref_userid_6);
        tv_userid7 = findViewById(R.id.tv_directref_userid_7);
        tv_userid8 = findViewById(R.id.tv_directref_userid_8);

        sentUserIdToDb();

        profile_img1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                isBackpress = false;
                id = tv_userid1.getText().toString();
                String value = leg_expansion.getString("userid", "");
                StringBuffer str = new StringBuffer(value);
                str.append(id);
                SharedPreferences.Editor edit = leg_expansion.edit();
                edit.putString("userid", String.valueOf(str));
                edit.commit();
                sentUserIdToDb();
            }
        });

        profile_img2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                isBackpress = false;
                id = tv_userid2.getText().toString();

                String value = leg_expansion.getString("userid", "");
                StringBuffer str = new StringBuffer(value);
                str.append(id);
                SharedPreferences.Editor edit = leg_expansion.edit();
                edit.putString("userid", String.valueOf(str));
                edit.commit();

                sentUserIdToDb();
            }
        });

        profile_img3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                isBackpress = false;
                id = tv_userid3.getText().toString();
                String value = leg_expansion.getString("userid", "");
                StringBuffer str = new StringBuffer(value);
                str.append(id);
                SharedPreferences.Editor edit = leg_expansion.edit();
                edit.putString("userid", String.valueOf(str));
                edit.commit();
                sentUserIdToDb();
            }
        });

        profile_img4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                isBackpress = false;
                id = tv_userid4.getText().toString();
                String value = leg_expansion.getString("userid", "");
                StringBuffer str = new StringBuffer(value);
                str.append(id);
                SharedPreferences.Editor edit = leg_expansion.edit();
                edit.putString("userid", String.valueOf(str));
                edit.commit();
                sentUserIdToDb();
            }
        });

        profile_img5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                isBackpress = false;
                id = tv_userid5.getText().toString();
                String value = leg_expansion.getString("userid", "");
                StringBuffer str = new StringBuffer(value);
                str.append(id);
                SharedPreferences.Editor edit = leg_expansion.edit();
                edit.putString("userid", String.valueOf(str));
                edit.commit();
                sentUserIdToDb();
            }
        });

        profile_img6.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                isBackpress = false;
                id = tv_userid6.getText().toString();
                String value = leg_expansion.getString("userid", "");
                StringBuffer str = new StringBuffer(value);
                str.append(id);
                SharedPreferences.Editor edit = leg_expansion.edit();
                edit.putString("userid", String.valueOf(str));
                edit.commit();
                sentUserIdToDb();
            }
        });

        profile_img7.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                isBackpress = false;
                id = tv_userid7.getText().toString();
                String value = leg_expansion.getString("userid", "");
                StringBuffer str = new StringBuffer(value);
                str.append(id);
                SharedPreferences.Editor edit = leg_expansion.edit();
                edit.putString("userid", String.valueOf(str));
                edit.commit();
                sentUserIdToDb();
            }
        });

        profile_img8.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                isBackpress = false;
                id = tv_userid8.getText().toString();
                String value = leg_expansion.getString("userid", "");
                StringBuffer str = new StringBuffer(value);
                str.append(id);
                SharedPreferences.Editor edit = leg_expansion.edit();
                edit.putString("userid", String.valueOf(str));
                edit.commit();
                sentUserIdToDb();
            }
        });
    }

    public void sentUserIdToDb() {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, BaseUrl.retriveUserDataUrl, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                ib_directref_user1_one.setImageResource(R.drawable.ic_no_leg);
                ib_directref_user1_two.setImageResource(R.drawable.ic_no_leg);
                ib_directref_user1_three.setImageResource(R.drawable.ic_no_leg);
                ib_directref_user1_four.setImageResource(R.drawable.ic_no_leg);
                ib_directref_user1_five.setImageResource(R.drawable.ic_no_leg);
                ib_directref_user1_six.setImageResource(R.drawable.ic_no_leg);
                ib_directref_user1_seven.setImageResource(R.drawable.ic_no_leg);
                ib_directref_user1_eight.setImageResource(R.drawable.ic_no_leg);

                ib_directref_user2_one.setImageResource(R.drawable.ic_no_leg);
                ib_directref_user2_two.setImageResource(R.drawable.ic_no_leg);
                ib_directref_user2_three.setImageResource(R.drawable.ic_no_leg);
                ib_directref_user2_four.setImageResource(R.drawable.ic_no_leg);
                ib_directref_user2_five.setImageResource(R.drawable.ic_no_leg);
                ib_directref_user2_six.setImageResource(R.drawable.ic_no_leg);
                ib_directref_user2_seven.setImageResource(R.drawable.ic_no_leg);
                ib_directref_user2_eight.setImageResource(R.drawable.ic_no_leg);

                ib_directref_user3_one.setImageResource(R.drawable.ic_no_leg);
                ib_directref_user3_two.setImageResource(R.drawable.ic_no_leg);
                ib_directref_user3_three.setImageResource(R.drawable.ic_no_leg);
                ib_directref_user3_four.setImageResource(R.drawable.ic_no_leg);
                ib_directref_user3_five.setImageResource(R.drawable.ic_no_leg);
                ib_directref_user3_six.setImageResource(R.drawable.ic_no_leg);
                ib_directref_user3_seven.setImageResource(R.drawable.ic_no_leg);
                ib_directref_user3_eight.setImageResource(R.drawable.ic_no_leg);

                ib_directref_user4_one.setImageResource(R.drawable.ic_no_leg);
                ib_directref_user4_two.setImageResource(R.drawable.ic_no_leg);
                ib_directref_user4_three.setImageResource(R.drawable.ic_no_leg);
                ib_directref_user4_four.setImageResource(R.drawable.ic_no_leg);
                ib_directref_user4_five.setImageResource(R.drawable.ic_no_leg);
                ib_directref_user4_six.setImageResource(R.drawable.ic_no_leg);
                ib_directref_user4_seven.setImageResource(R.drawable.ic_no_leg);
                ib_directref_user4_eight.setImageResource(R.drawable.ic_no_leg);

                ib_directref_user5_one.setImageResource(R.drawable.ic_no_leg);
                ib_directref_user5_two.setImageResource(R.drawable.ic_no_leg);
                ib_directref_user5_three.setImageResource(R.drawable.ic_no_leg);
                ib_directref_user5_four.setImageResource(R.drawable.ic_no_leg);
                ib_directref_user5_five.setImageResource(R.drawable.ic_no_leg);
                ib_directref_user5_six.setImageResource(R.drawable.ic_no_leg);
                ib_directref_user5_seven.setImageResource(R.drawable.ic_no_leg);
                ib_directref_user5_eight.setImageResource(R.drawable.ic_no_leg);

                ib_directref_user6_one.setImageResource(R.drawable.ic_no_leg);
                ib_directref_user6_two.setImageResource(R.drawable.ic_no_leg);
                ib_directref_user6_three.setImageResource(R.drawable.ic_no_leg);
                ib_directref_user6_four.setImageResource(R.drawable.ic_no_leg);
                ib_directref_user6_five.setImageResource(R.drawable.ic_no_leg);
                ib_directref_user6_six.setImageResource(R.drawable.ic_no_leg);
                ib_directref_user6_seven.setImageResource(R.drawable.ic_no_leg);
                ib_directref_user6_eight.setImageResource(R.drawable.ic_no_leg);

                ib_directref_user7_one.setImageResource(R.drawable.ic_no_leg);
                ib_directref_user7_two.setImageResource(R.drawable.ic_no_leg);
                ib_directref_user7_three.setImageResource(R.drawable.ic_no_leg);
                ib_directref_user7_four.setImageResource(R.drawable.ic_no_leg);
                ib_directref_user7_five.setImageResource(R.drawable.ic_no_leg);
                ib_directref_user7_six.setImageResource(R.drawable.ic_no_leg);
                ib_directref_user7_seven.setImageResource(R.drawable.ic_no_leg);
                ib_directref_user7_eight.setImageResource(R.drawable.ic_no_leg);

                ib_directref_user8_one.setImageResource(R.drawable.ic_no_leg);
                ib_directref_user8_two.setImageResource(R.drawable.ic_no_leg);
                ib_directref_user8_three.setImageResource(R.drawable.ic_no_leg);
                ib_directref_user8_four.setImageResource(R.drawable.ic_no_leg);
                ib_directref_user8_five.setImageResource(R.drawable.ic_no_leg);
                ib_directref_user8_six.setImageResource(R.drawable.ic_no_leg);
                ib_directref_user8_seven.setImageResource(R.drawable.ic_no_leg);
                ib_directref_user8_eight.setImageResource(R.drawable.ic_no_leg);

                if (response.contains("no data found") || response.contains("id is empty") || response.contains("connection not got")) {
                    Toast.makeText(DirectRefDisplayTeamActivity.this, "no data found.", Toast.LENGTH_SHORT).show();
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
                    tv_userid1.setText(data.substring(data.indexOf("#4A#") + 4, data.indexOf("#5A#")));
                    tv_name1.setText(data.substring(data.indexOf("#5A#") + 4, data.indexOf("#6A#")));
                    tv_userid2.setText(data.substring(data.indexOf("#6A#") + 4, data.indexOf("#7A#")));
                    tv_name2.setText(data.substring(data.indexOf("#7A#") + 4, data.indexOf("#8A#")));
                    tv_userid3.setText(data.substring(data.indexOf("#8A#") + 4, data.indexOf("#9A#")));
                    tv_name3.setText(data.substring(data.indexOf("#9A#") + 4, data.indexOf("#10A#")));
                    tv_userid4.setText(data.substring(data.indexOf("#10A#") + 5, data.indexOf("#11A#")));
                    tv_name4.setText(data.substring(data.indexOf("#11A#") + 5, data.indexOf("#12A#")));
                    tv_userid5.setText(data.substring(data.indexOf("#12A#") + 5, data.indexOf("#13A#")));
                    tv_name5.setText(data.substring(data.indexOf("#13A#") + 5, data.indexOf("#14A#")));
                    tv_userid6.setText(data.substring(data.indexOf("#14A#") + 5, data.indexOf("#15A#")));
                    tv_name6.setText(data.substring(data.indexOf("#15A#") + 5, data.indexOf("#16A#")));
                    tv_userid7.setText(data.substring(data.indexOf("#16A#") + 5, data.indexOf("#17A#")));
                    tv_name7.setText(data.substring(data.indexOf("#17A#") + 5, data.indexOf("#18A#")));
                    tv_userid8.setText(data.substring(data.indexOf("#18A#") + 5, data.indexOf("#19A#")));
                    tv_name8.setText(data.substring(data.indexOf("#19A#") + 5, data.indexOf("#20A#")));

                    if (data.contains("#pn#")) {
                        idUrl = data.substring(data.indexOf("#urls#") + 6, data.indexOf("#pn#"));
                    } else {
                        idUrl = data.substring(data.indexOf("#urls#") + 6);
                    }

                    if (!(idUrl.isEmpty())) {
                        String[] idUrl_part = idUrl.split(",");

                        for (int i = 0; i <= idUrl_part.length - 1; i++) {

                            String[] singleIdUrl_part = idUrl_part[i].split("::|:_:");

                            for (int j = 0; j <= 0; j++) {

                                idUrl_map.put(singleIdUrl_part[0], new LegMapModel(singleIdUrl_part[0], singleIdUrl_part[1],
                                        singleIdUrl_part[2]));

                                Log.d("qqaaxxdd1", "id is " + singleIdUrl_part[0] +
                                        "imgurl is " + singleIdUrl_part[1] +
                                        "is paid is " + singleIdUrl_part[2]);
                            }
                        }
                    }

                    if (isBackpress) {

                        String tv_leg_names = tv_legs.getText().toString();
                        keyword = ">";

                        int index = tv_leg_names.lastIndexOf(keyword);

                        String new_leg_names = tv_leg_names.substring(0, index);
                        tv_legs.setText(new_leg_names);
                        Log.d("spData148", "> postion is " + index);
                        Log.d("spData148", "> new legs " + tv_legs.getText().toString());

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

                        if (isPaid.equals("no")) {
                            tv_name1.setTextColor(Color.RED);
                            tv_userid1.setTextColor(Color.RED);
                        }

                        Glide.with(DirectRefDisplayTeamActivity.this)
                                .load(idUrl_map.get(tv_userid1.getText().toString()).image_url)
                                .into(profile_img1);
                        isHavingLeg = true;
                    }

                    if (tv_name2.getText().toString().isEmpty()) {
                        rl2.setVisibility(View.GONE);
                    } else {
                        rl2.setVisibility(View.VISIBLE);
                        String isPaid = idUrl_map.get(tv_userid2.getText().toString()).isPaid;

                        if (isPaid.equals("no")) {
                            tv_name2.setTextColor(Color.RED);
                            tv_userid2.setTextColor(Color.RED);
                        }

                        Glide.with(DirectRefDisplayTeamActivity.this)
                                .load(idUrl_map.get(tv_userid2.getText().toString()).image_url)
                                .into(profile_img2);
                    }

                    if (tv_name3.getText().toString().isEmpty()) {
                        rl3.setVisibility(View.GONE);
                    } else {
                        rl3.setVisibility(View.VISIBLE);
                        String isPaid = idUrl_map.get(tv_userid3.getText().toString()).isPaid;

                        if (isPaid.equals("no")) {
                            tv_name3.setTextColor(Color.RED);
                            tv_userid3.setTextColor(Color.RED);
                        }

                        Glide.with(DirectRefDisplayTeamActivity.this)
                                .load(idUrl_map.get(tv_userid3.getText().toString()).image_url)
                                .into(profile_img3);
                    }

                    if (tv_name4.getText().toString().isEmpty()) {
                        rl4.setVisibility(View.GONE);
                    } else {
                        rl4.setVisibility(View.VISIBLE);
                        String isPaid = idUrl_map.get(tv_userid4.getText().toString()).isPaid;

                        if (isPaid.equals("no")) {
                            tv_name4.setTextColor(Color.RED);
                            tv_userid4.setTextColor(Color.RED);
                        }

                        Glide.with(DirectRefDisplayTeamActivity.this)
                                .load(idUrl_map.get(tv_userid4.getText().toString()).image_url)
                                .into(profile_img4);
                    }

                    if (tv_name5.getText().toString().isEmpty()) {
                        rl5.setVisibility(View.GONE);
                    } else {
                        rl5.setVisibility(View.VISIBLE);
                        String isPaid = idUrl_map.get(tv_userid5.getText().toString()).isPaid;

                        if (isPaid.equals("no")) {
                            tv_name5.setTextColor(Color.RED);
                            tv_userid5.setTextColor(Color.RED);
                        }

                        Glide.with(DirectRefDisplayTeamActivity.this)
                                .load(idUrl_map.get(tv_userid5.getText().toString()).image_url)
                                .into(profile_img5);
                    }

                    if (tv_name6.getText().toString().isEmpty()) {
                        rl6.setVisibility(View.GONE);
                    } else {
                        rl6.setVisibility(View.VISIBLE);
                        String isPaid = idUrl_map.get(tv_userid6.getText().toString()).isPaid;

                        if (isPaid.equals("no")) {
                            tv_name6.setTextColor(Color.RED);
                            tv_userid6.setTextColor(Color.RED);
                        }

                        Glide.with(DirectRefDisplayTeamActivity.this)
                                .load(idUrl_map.get(tv_userid6.getText().toString()).image_url)
                                .into(profile_img6);
                    }

                    if (tv_name7.getText().toString().isEmpty()) {
                        rl7.setVisibility(View.GONE);
                    } else {
                        rl7.setVisibility(View.VISIBLE);
                        String isPaid = idUrl_map.get(tv_userid7.getText().toString()).isPaid;

                        if (isPaid.equals("no")) {
                            tv_name7.setTextColor(Color.RED);
                            tv_userid7.setTextColor(Color.RED);
                        }

                        Glide.with(DirectRefDisplayTeamActivity.this)
                                .load(idUrl_map.get(tv_userid7.getText().toString()).image_url)
                                .into(profile_img7);
                    }
                    if (tv_name8.getText().toString().isEmpty()) {
                        rl8.setVisibility(View.GONE);
                    } else {
                        rl8.setVisibility(View.VISIBLE);
                        String isPaid = idUrl_map.get(tv_userid8.getText().toString()).isPaid;

                        if (isPaid.equals("no")) {
                            tv_name8.setTextColor(Color.RED);
                            tv_userid8.setTextColor(Color.RED);
                        }

                        Glide.with(DirectRefDisplayTeamActivity.this)
                                .load(idUrl_map.get(tv_userid8.getText().toString()).image_url)
                                .into(profile_img8);
                    }

                    if (isHavingLeg) {
                        tv_noleg_msg.setVisibility(View.INVISIBLE);
                    } else {
                        tv_noleg_msg.setVisibility(View.VISIBLE);
                    }
                }
                dialog.dismiss();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                dialog.dismiss();
                Toast.makeText(DirectRefDisplayTeamActivity.this, "Something went wrong.", Toast.LENGTH_SHORT).show();
                rl1.setVisibility(View.INVISIBLE);
                rl2.setVisibility(View.INVISIBLE);
                rl3.setVisibility(View.INVISIBLE);
                rl4.setVisibility(View.INVISIBLE);
                rl5.setVisibility(View.INVISIBLE);
                rl6.setVisibility(View.INVISIBLE);
                rl7.setVisibility(View.INVISIBLE);
                rl8.setVisibility(View.INVISIBLE);
                tv_noleg_msg.setVisibility(View.VISIBLE);
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> map = new HashMap<>();
                map.put("id", id);
                return map;
            }
        };

        Volley.newRequestQueue(DirectRefDisplayTeamActivity.this).add(stringRequest);
    }

    public void setIspaidImage() {
        dialog.show();
        StringRequest stringRequest = new StringRequest(Request.Method.POST, BaseUrl.ktc_get_additional_info_tree, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                dialog.dismiss();

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
                        tv_directref1.setText("Direct ref:- " + one.substring(one.indexOf("#t2#") + 4, one.indexOf("#t3#")));

                        switch (paidLegCount) {
                            case 0:
                                switch (unpaidLegCount) {
                                    case 0:
                                        break;

                                    case 1:
                                        ib_directref_user1_one.setImageResource(R.drawable.radio_red_color);
                                        break;

                                    case 2:
                                        ib_directref_user1_one.setImageResource(R.drawable.radio_red_color);
                                        ib_directref_user1_two.setImageResource(R.drawable.radio_red_color);
                                        break;

                                    case 3:
                                        ib_directref_user1_one.setImageResource(R.drawable.radio_red_color);
                                        ib_directref_user1_two.setImageResource(R.drawable.radio_red_color);
                                        ib_directref_user1_three.setImageResource(R.drawable.radio_red_color);
                                        break;

                                    case 4:
                                        ib_directref_user1_one.setImageResource(R.drawable.radio_red_color);
                                        ib_directref_user1_two.setImageResource(R.drawable.radio_red_color);
                                        ib_directref_user1_three.setImageResource(R.drawable.radio_red_color);
                                        ib_directref_user1_four.setImageResource(R.drawable.radio_red_color);
                                        break;

                                    case 5:
                                        ib_directref_user1_one.setImageResource(R.drawable.radio_red_color);
                                        ib_directref_user1_two.setImageResource(R.drawable.radio_red_color);
                                        ib_directref_user1_three.setImageResource(R.drawable.radio_red_color);
                                        ib_directref_user1_four.setImageResource(R.drawable.radio_red_color);
                                        ib_directref_user1_five.setImageResource(R.drawable.radio_red_color);
                                        break;

                                    case 6:
                                        ib_directref_user1_one.setImageResource(R.drawable.radio_red_color);
                                        ib_directref_user1_two.setImageResource(R.drawable.radio_red_color);
                                        ib_directref_user1_three.setImageResource(R.drawable.radio_red_color);
                                        ib_directref_user1_four.setImageResource(R.drawable.radio_red_color);
                                        ib_directref_user1_five.setImageResource(R.drawable.radio_red_color);
                                        ib_directref_user1_six.setImageResource(R.drawable.radio_red_color);
                                        break;

                                    case 7:
                                        ib_directref_user1_one.setImageResource(R.drawable.radio_red_color);
                                        ib_directref_user1_two.setImageResource(R.drawable.radio_red_color);
                                        ib_directref_user1_three.setImageResource(R.drawable.radio_red_color);
                                        ib_directref_user1_four.setImageResource(R.drawable.radio_red_color);
                                        ib_directref_user1_five.setImageResource(R.drawable.radio_red_color);
                                        ib_directref_user1_six.setImageResource(R.drawable.radio_red_color);
                                        ib_directref_user1_seven.setImageResource(R.drawable.radio_red_color);
                                        break;

                                    case 8:
                                        ib_directref_user1_one.setImageResource(R.drawable.radio_red_color);
                                        ib_directref_user1_two.setImageResource(R.drawable.radio_red_color);
                                        ib_directref_user1_three.setImageResource(R.drawable.radio_red_color);
                                        ib_directref_user1_four.setImageResource(R.drawable.radio_red_color);
                                        ib_directref_user1_five.setImageResource(R.drawable.radio_red_color);
                                        ib_directref_user1_six.setImageResource(R.drawable.radio_red_color);
                                        ib_directref_user1_seven.setImageResource(R.drawable.radio_red_color);
                                        ib_directref_user1_eight.setImageResource(R.drawable.radio_red_color);
                                        break;
                                }
                                break;

                            case 1:
                                ib_directref_user1_one.setImageResource(R.drawable.radio_green_color);

                                switch (unpaidLegCount) {
                                    case 0:
                                        break;

                                    case 1:
                                        ib_directref_user1_two.setImageResource(R.drawable.radio_red_color);
                                        break;

                                    case 2:
                                        ib_directref_user1_two.setImageResource(R.drawable.radio_red_color);
                                        ib_directref_user1_three.setImageResource(R.drawable.radio_red_color);
                                        break;

                                    case 3:
                                        ib_directref_user1_two.setImageResource(R.drawable.radio_red_color);
                                        ib_directref_user1_three.setImageResource(R.drawable.radio_red_color);
                                        ib_directref_user1_four.setImageResource(R.drawable.radio_red_color);
                                        break;

                                    case 4:
                                        ib_directref_user1_two.setImageResource(R.drawable.radio_red_color);
                                        ib_directref_user1_three.setImageResource(R.drawable.radio_red_color);
                                        ib_directref_user1_four.setImageResource(R.drawable.radio_red_color);
                                        ib_directref_user1_five.setImageResource(R.drawable.radio_red_color);
                                        break;

                                    case 5:
                                        ib_directref_user1_two.setImageResource(R.drawable.radio_red_color);
                                        ib_directref_user1_three.setImageResource(R.drawable.radio_red_color);
                                        ib_directref_user1_four.setImageResource(R.drawable.radio_red_color);
                                        ib_directref_user1_five.setImageResource(R.drawable.radio_red_color);
                                        ib_directref_user1_six.setImageResource(R.drawable.radio_red_color);
                                        break;

                                    case 6:
                                        ib_directref_user1_two.setImageResource(R.drawable.radio_red_color);
                                        ib_directref_user1_three.setImageResource(R.drawable.radio_red_color);
                                        ib_directref_user1_four.setImageResource(R.drawable.radio_red_color);
                                        ib_directref_user1_five.setImageResource(R.drawable.radio_red_color);
                                        ib_directref_user1_six.setImageResource(R.drawable.radio_red_color);
                                        ib_directref_user1_seven.setImageResource(R.drawable.radio_red_color);
                                        break;

                                    case 7:
                                        ib_directref_user1_two.setImageResource(R.drawable.radio_red_color);
                                        ib_directref_user1_three.setImageResource(R.drawable.radio_red_color);
                                        ib_directref_user1_four.setImageResource(R.drawable.radio_red_color);
                                        ib_directref_user1_five.setImageResource(R.drawable.radio_red_color);
                                        ib_directref_user1_six.setImageResource(R.drawable.radio_red_color);
                                        ib_directref_user1_seven.setImageResource(R.drawable.radio_red_color);
                                        ib_directref_user1_eight.setImageResource(R.drawable.radio_red_color);
                                        break;
                                }
                                break;

                            case 2:
                                ib_directref_user1_one.setImageResource(R.drawable.radio_green_color);
                                ib_directref_user1_two.setImageResource(R.drawable.radio_green_color);

                                switch (unpaidLegCount) {
                                    case 0:
                                        break;

                                    case 1:
                                        ib_directref_user1_three.setImageResource(R.drawable.radio_red_color);
                                        break;

                                    case 2:
                                        ib_directref_user1_three.setImageResource(R.drawable.radio_red_color);
                                        ib_directref_user1_four.setImageResource(R.drawable.radio_red_color);
                                        break;

                                    case 3:
                                        ib_directref_user1_three.setImageResource(R.drawable.radio_red_color);
                                        ib_directref_user1_four.setImageResource(R.drawable.radio_red_color);
                                        ib_directref_user1_five.setImageResource(R.drawable.radio_red_color);
                                        break;

                                    case 4:
                                        ib_directref_user1_three.setImageResource(R.drawable.radio_red_color);
                                        ib_directref_user1_four.setImageResource(R.drawable.radio_red_color);
                                        ib_directref_user1_five.setImageResource(R.drawable.radio_red_color);
                                        ib_directref_user1_six.setImageResource(R.drawable.radio_red_color);
                                        break;

                                    case 5:
                                        ib_directref_user1_three.setImageResource(R.drawable.radio_red_color);
                                        ib_directref_user1_four.setImageResource(R.drawable.radio_red_color);
                                        ib_directref_user1_five.setImageResource(R.drawable.radio_red_color);
                                        ib_directref_user1_six.setImageResource(R.drawable.radio_red_color);
                                        ib_directref_user1_seven.setImageResource(R.drawable.radio_red_color);
                                        break;

                                    case 6:
                                        ib_directref_user1_three.setImageResource(R.drawable.radio_red_color);
                                        ib_directref_user1_four.setImageResource(R.drawable.radio_red_color);
                                        ib_directref_user1_five.setImageResource(R.drawable.radio_red_color);
                                        ib_directref_user1_six.setImageResource(R.drawable.radio_red_color);
                                        ib_directref_user1_seven.setImageResource(R.drawable.radio_red_color);
                                        ib_directref_user1_eight.setImageResource(R.drawable.radio_red_color);
                                        break;
                                }
                                break;

                            case 3:
                                ib_directref_user1_one.setImageResource(R.drawable.radio_green_color);
                                ib_directref_user1_two.setImageResource(R.drawable.radio_green_color);
                                ib_directref_user1_three.setImageResource(R.drawable.radio_green_color);

                                switch (unpaidLegCount) {
                                    case 0:
                                        break;

                                    case 1:
                                        ib_directref_user1_four.setImageResource(R.drawable.radio_red_color);
                                        break;

                                    case 2:
                                        ib_directref_user1_four.setImageResource(R.drawable.radio_red_color);
                                        ib_directref_user1_five.setImageResource(R.drawable.radio_red_color);
                                        break;

                                    case 3:
                                        ib_directref_user1_four.setImageResource(R.drawable.radio_red_color);
                                        ib_directref_user1_five.setImageResource(R.drawable.radio_red_color);
                                        ib_directref_user1_six.setImageResource(R.drawable.radio_red_color);
                                        break;

                                    case 4:
                                        ib_directref_user1_four.setImageResource(R.drawable.radio_red_color);
                                        ib_directref_user1_five.setImageResource(R.drawable.radio_red_color);
                                        ib_directref_user1_six.setImageResource(R.drawable.radio_red_color);
                                        ib_directref_user1_seven.setImageResource(R.drawable.radio_red_color);
                                        break;

                                    case 5:
                                        ib_directref_user1_four.setImageResource(R.drawable.radio_red_color);
                                        ib_directref_user1_five.setImageResource(R.drawable.radio_red_color);
                                        ib_directref_user1_six.setImageResource(R.drawable.radio_red_color);
                                        ib_directref_user1_seven.setImageResource(R.drawable.radio_red_color);
                                        ib_directref_user1_eight.setImageResource(R.drawable.radio_red_color);
                                        break;
                                }
                                break;

                            case 4:
                                ib_directref_user1_one.setImageResource(R.drawable.radio_green_color);
                                ib_directref_user1_two.setImageResource(R.drawable.radio_green_color);
                                ib_directref_user1_three.setImageResource(R.drawable.radio_green_color);
                                ib_directref_user1_four.setImageResource(R.drawable.radio_green_color);

                                switch (unpaidLegCount) {
                                    case 0:
                                        break;

                                    case 1:
                                        ib_directref_user1_five.setImageResource(R.drawable.radio_red_color);
                                        break;

                                    case 2:
                                        ib_directref_user1_five.setImageResource(R.drawable.radio_red_color);
                                        ib_directref_user1_six.setImageResource(R.drawable.radio_red_color);
                                        break;

                                    case 3:
                                        ib_directref_user1_five.setImageResource(R.drawable.radio_red_color);
                                        ib_directref_user1_six.setImageResource(R.drawable.radio_red_color);
                                        ib_directref_user1_seven.setImageResource(R.drawable.radio_red_color);
                                        break;

                                    case 4:
                                        ib_directref_user1_five.setImageResource(R.drawable.radio_red_color);
                                        ib_directref_user1_six.setImageResource(R.drawable.radio_red_color);
                                        ib_directref_user1_seven.setImageResource(R.drawable.radio_red_color);
                                        ib_directref_user1_eight.setImageResource(R.drawable.radio_red_color);
                                        break;
                                }
                                break;

                            case 5:
                                ib_directref_user1_one.setImageResource(R.drawable.radio_green_color);
                                ib_directref_user1_two.setImageResource(R.drawable.radio_green_color);
                                ib_directref_user1_three.setImageResource(R.drawable.radio_green_color);
                                ib_directref_user1_four.setImageResource(R.drawable.radio_green_color);
                                ib_directref_user1_five.setImageResource(R.drawable.radio_green_color);

                                switch (unpaidLegCount) {
                                    case 0:
                                        break;
                                    case 1:
                                        ib_directref_user1_six.setImageResource(R.drawable.radio_red_color);
                                        break;

                                    case 2:
                                        ib_directref_user1_six.setImageResource(R.drawable.radio_red_color);
                                        ib_directref_user1_seven.setImageResource(R.drawable.radio_red_color);
                                        break;

                                    case 3:
                                        ib_directref_user1_six.setImageResource(R.drawable.radio_red_color);
                                        ib_directref_user1_seven.setImageResource(R.drawable.radio_red_color);
                                        ib_directref_user1_eight.setImageResource(R.drawable.radio_red_color);
                                        break;
                                }
                                break;

                            case 6:
                                ib_directref_user1_one.setImageResource(R.drawable.radio_green_color);
                                ib_directref_user1_two.setImageResource(R.drawable.radio_green_color);
                                ib_directref_user1_three.setImageResource(R.drawable.radio_green_color);
                                ib_directref_user1_four.setImageResource(R.drawable.radio_green_color);
                                ib_directref_user1_five.setImageResource(R.drawable.radio_green_color);
                                ib_directref_user1_six.setImageResource(R.drawable.radio_green_color);

                                switch (unpaidLegCount) {
                                    case 0:
                                        break;

                                    case 1:
                                        ib_directref_user1_seven.setImageResource(R.drawable.radio_red_color);
                                        break;

                                    case 2:
                                        ib_directref_user1_seven.setImageResource(R.drawable.radio_red_color);
                                        ib_directref_user1_eight.setImageResource(R.drawable.radio_red_color);
                                        break;
                                }
                                break;

                            case 7:
                                ib_directref_user1_one.setImageResource(R.drawable.radio_green_color);
                                ib_directref_user1_two.setImageResource(R.drawable.radio_green_color);
                                ib_directref_user1_three.setImageResource(R.drawable.radio_green_color);
                                ib_directref_user1_four.setImageResource(R.drawable.radio_green_color);
                                ib_directref_user1_five.setImageResource(R.drawable.radio_green_color);
                                ib_directref_user1_six.setImageResource(R.drawable.radio_green_color);
                                ib_directref_user1_seven.setImageResource(R.drawable.radio_green_color);

                                switch (unpaidLegCount) {
                                    case 0:
                                        break;

                                    case 1:
                                        ib_directref_user1_eight.setImageResource(R.drawable.radio_red_color);
                                        break;
                                }
                                break;

                            case 8:
                                ib_directref_user1_one.setImageResource(R.drawable.radio_green_color);
                                ib_directref_user1_two.setImageResource(R.drawable.radio_green_color);
                                ib_directref_user1_three.setImageResource(R.drawable.radio_green_color);
                                ib_directref_user1_four.setImageResource(R.drawable.radio_green_color);
                                ib_directref_user1_five.setImageResource(R.drawable.radio_green_color);
                                ib_directref_user1_six.setImageResource(R.drawable.radio_green_color);
                                ib_directref_user1_seven.setImageResource(R.drawable.radio_green_color);
                                ib_directref_user1_eight.setImageResource(R.drawable.radio_green_color);

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
                        tv_directref2.setText("Direct ref:- " + two.substring(two.indexOf("#t2#") + 4, two.indexOf("#t3#")));

                        switch (paidLegCount) {
                            case 0:
                                switch (unpaidLegCount) {
                                    case 0:
                                        break;

                                    case 1:
                                        ib_directref_user2_one.setImageResource(R.drawable.radio_red_color);
                                        break;

                                    case 2:
                                        ib_directref_user2_one.setImageResource(R.drawable.radio_red_color);
                                        ib_directref_user2_two.setImageResource(R.drawable.radio_red_color);
                                        break;

                                    case 3:
                                        ib_directref_user2_one.setImageResource(R.drawable.radio_red_color);
                                        ib_directref_user2_two.setImageResource(R.drawable.radio_red_color);
                                        ib_directref_user2_three.setImageResource(R.drawable.radio_red_color);
                                        break;

                                    case 4:
                                        ib_directref_user2_one.setImageResource(R.drawable.radio_red_color);
                                        ib_directref_user2_two.setImageResource(R.drawable.radio_red_color);
                                        ib_directref_user2_three.setImageResource(R.drawable.radio_red_color);
                                        ib_directref_user2_four.setImageResource(R.drawable.radio_red_color);
                                        break;

                                    case 5:
                                        ib_directref_user2_one.setImageResource(R.drawable.radio_red_color);
                                        ib_directref_user2_two.setImageResource(R.drawable.radio_red_color);
                                        ib_directref_user2_three.setImageResource(R.drawable.radio_red_color);
                                        ib_directref_user2_four.setImageResource(R.drawable.radio_red_color);
                                        ib_directref_user2_five.setImageResource(R.drawable.radio_red_color);
                                        break;

                                    case 6:
                                        ib_directref_user2_one.setImageResource(R.drawable.radio_red_color);
                                        ib_directref_user2_two.setImageResource(R.drawable.radio_red_color);
                                        ib_directref_user2_three.setImageResource(R.drawable.radio_red_color);
                                        ib_directref_user2_four.setImageResource(R.drawable.radio_red_color);
                                        ib_directref_user2_five.setImageResource(R.drawable.radio_red_color);
                                        ib_directref_user2_six.setImageResource(R.drawable.radio_red_color);
                                        break;

                                    case 7:
                                        ib_directref_user2_one.setImageResource(R.drawable.radio_red_color);
                                        ib_directref_user2_two.setImageResource(R.drawable.radio_red_color);
                                        ib_directref_user2_three.setImageResource(R.drawable.radio_red_color);
                                        ib_directref_user2_four.setImageResource(R.drawable.radio_red_color);
                                        ib_directref_user2_five.setImageResource(R.drawable.radio_red_color);
                                        ib_directref_user2_six.setImageResource(R.drawable.radio_red_color);
                                        ib_directref_user2_seven.setImageResource(R.drawable.radio_red_color);
                                        break;

                                    case 8:
                                        ib_directref_user2_one.setImageResource(R.drawable.radio_red_color);
                                        ib_directref_user2_two.setImageResource(R.drawable.radio_red_color);
                                        ib_directref_user2_three.setImageResource(R.drawable.radio_red_color);
                                        ib_directref_user2_four.setImageResource(R.drawable.radio_red_color);
                                        ib_directref_user2_five.setImageResource(R.drawable.radio_red_color);
                                        ib_directref_user2_six.setImageResource(R.drawable.radio_red_color);
                                        ib_directref_user2_seven.setImageResource(R.drawable.radio_red_color);
                                        ib_directref_user2_eight.setImageResource(R.drawable.radio_red_color);
                                        break;
                                }
                                break;

                            case 1:
                                ib_directref_user2_one.setImageResource(R.drawable.radio_green_color);

                                switch (unpaidLegCount) {
                                    case 0:
                                        break;

                                    case 1:
                                        ib_directref_user2_two.setImageResource(R.drawable.radio_red_color);
                                        break;

                                    case 2:
                                        ib_directref_user2_two.setImageResource(R.drawable.radio_red_color);
                                        ib_directref_user2_three.setImageResource(R.drawable.radio_red_color);
                                        break;

                                    case 3:
                                        ib_directref_user2_two.setImageResource(R.drawable.radio_red_color);
                                        ib_directref_user2_three.setImageResource(R.drawable.radio_red_color);
                                        ib_directref_user2_four.setImageResource(R.drawable.radio_red_color);
                                        break;

                                    case 4:
                                        ib_directref_user2_two.setImageResource(R.drawable.radio_red_color);
                                        ib_directref_user2_three.setImageResource(R.drawable.radio_red_color);
                                        ib_directref_user2_four.setImageResource(R.drawable.radio_red_color);
                                        ib_directref_user2_five.setImageResource(R.drawable.radio_red_color);
                                        break;

                                    case 5:
                                        ib_directref_user2_two.setImageResource(R.drawable.radio_red_color);
                                        ib_directref_user2_three.setImageResource(R.drawable.radio_red_color);
                                        ib_directref_user2_four.setImageResource(R.drawable.radio_red_color);
                                        ib_directref_user2_five.setImageResource(R.drawable.radio_red_color);
                                        ib_directref_user2_six.setImageResource(R.drawable.radio_red_color);
                                        break;

                                    case 6:
                                        ib_directref_user2_two.setImageResource(R.drawable.radio_red_color);
                                        ib_directref_user2_three.setImageResource(R.drawable.radio_red_color);
                                        ib_directref_user2_four.setImageResource(R.drawable.radio_red_color);
                                        ib_directref_user2_five.setImageResource(R.drawable.radio_red_color);
                                        ib_directref_user2_six.setImageResource(R.drawable.radio_red_color);
                                        ib_directref_user2_seven.setImageResource(R.drawable.radio_red_color);
                                        break;

                                    case 7:
                                        ib_directref_user2_two.setImageResource(R.drawable.radio_red_color);
                                        ib_directref_user2_three.setImageResource(R.drawable.radio_red_color);
                                        ib_directref_user2_four.setImageResource(R.drawable.radio_red_color);
                                        ib_directref_user2_five.setImageResource(R.drawable.radio_red_color);
                                        ib_directref_user2_six.setImageResource(R.drawable.radio_red_color);
                                        ib_directref_user2_seven.setImageResource(R.drawable.radio_red_color);
                                        ib_directref_user2_eight.setImageResource(R.drawable.radio_red_color);
                                        break;
                                }
                                break;

                            case 2:
                                ib_directref_user2_one.setImageResource(R.drawable.radio_green_color);
                                ib_directref_user2_two.setImageResource(R.drawable.radio_green_color);

                                switch (unpaidLegCount) {
                                    case 0:
                                        break;

                                    case 1:
                                        ib_directref_user2_three.setImageResource(R.drawable.radio_red_color);
                                        break;

                                    case 2:
                                        ib_directref_user2_three.setImageResource(R.drawable.radio_red_color);
                                        ib_directref_user2_four.setImageResource(R.drawable.radio_red_color);
                                        break;

                                    case 3:
                                        ib_directref_user2_three.setImageResource(R.drawable.radio_red_color);
                                        ib_directref_user2_four.setImageResource(R.drawable.radio_red_color);
                                        ib_directref_user2_five.setImageResource(R.drawable.radio_red_color);
                                        break;

                                    case 4:
                                        ib_directref_user2_three.setImageResource(R.drawable.radio_red_color);
                                        ib_directref_user2_four.setImageResource(R.drawable.radio_red_color);
                                        ib_directref_user2_five.setImageResource(R.drawable.radio_red_color);
                                        ib_directref_user2_six.setImageResource(R.drawable.radio_red_color);
                                        break;

                                    case 5:
                                        ib_directref_user2_three.setImageResource(R.drawable.radio_red_color);
                                        ib_directref_user2_four.setImageResource(R.drawable.radio_red_color);
                                        ib_directref_user2_five.setImageResource(R.drawable.radio_red_color);
                                        ib_directref_user2_six.setImageResource(R.drawable.radio_red_color);
                                        ib_directref_user2_seven.setImageResource(R.drawable.radio_red_color);
                                        break;

                                    case 6:
                                        ib_directref_user2_three.setImageResource(R.drawable.radio_red_color);
                                        ib_directref_user2_four.setImageResource(R.drawable.radio_red_color);
                                        ib_directref_user2_five.setImageResource(R.drawable.radio_red_color);
                                        ib_directref_user2_six.setImageResource(R.drawable.radio_red_color);
                                        ib_directref_user2_seven.setImageResource(R.drawable.radio_red_color);
                                        ib_directref_user2_eight.setImageResource(R.drawable.radio_red_color);
                                        break;
                                }
                                break;

                            case 3:
                                ib_directref_user2_one.setImageResource(R.drawable.radio_green_color);
                                ib_directref_user2_two.setImageResource(R.drawable.radio_green_color);
                                ib_directref_user2_three.setImageResource(R.drawable.radio_green_color);

                                switch (unpaidLegCount) {
                                    case 0:
                                        break;

                                    case 1:
                                        ib_directref_user2_four.setImageResource(R.drawable.radio_red_color);
                                        break;

                                    case 2:
                                        ib_directref_user2_four.setImageResource(R.drawable.radio_red_color);
                                        ib_directref_user2_five.setImageResource(R.drawable.radio_red_color);
                                        break;

                                    case 3:
                                        ib_directref_user2_four.setImageResource(R.drawable.radio_red_color);
                                        ib_directref_user2_five.setImageResource(R.drawable.radio_red_color);
                                        ib_directref_user2_six.setImageResource(R.drawable.radio_red_color);
                                        break;

                                    case 4:
                                        ib_directref_user2_four.setImageResource(R.drawable.radio_red_color);
                                        ib_directref_user2_five.setImageResource(R.drawable.radio_red_color);
                                        ib_directref_user2_six.setImageResource(R.drawable.radio_red_color);
                                        ib_directref_user2_seven.setImageResource(R.drawable.radio_red_color);
                                        break;

                                    case 5:
                                        ib_directref_user2_four.setImageResource(R.drawable.radio_red_color);
                                        ib_directref_user2_five.setImageResource(R.drawable.radio_red_color);
                                        ib_directref_user2_six.setImageResource(R.drawable.radio_red_color);
                                        ib_directref_user2_seven.setImageResource(R.drawable.radio_red_color);
                                        ib_directref_user2_eight.setImageResource(R.drawable.radio_red_color);
                                        break;
                                }
                                break;

                            case 4:
                                ib_directref_user2_one.setImageResource(R.drawable.radio_green_color);
                                ib_directref_user2_two.setImageResource(R.drawable.radio_green_color);
                                ib_directref_user2_three.setImageResource(R.drawable.radio_green_color);
                                ib_directref_user2_four.setImageResource(R.drawable.radio_green_color);

                                switch (unpaidLegCount) {
                                    case 0:
                                        break;

                                    case 1:
                                        ib_directref_user2_five.setImageResource(R.drawable.radio_red_color);
                                        break;

                                    case 2:
                                        ib_directref_user2_five.setImageResource(R.drawable.radio_red_color);
                                        ib_directref_user2_six.setImageResource(R.drawable.radio_red_color);
                                        break;

                                    case 3:
                                        ib_directref_user2_five.setImageResource(R.drawable.radio_red_color);
                                        ib_directref_user2_six.setImageResource(R.drawable.radio_red_color);
                                        ib_directref_user2_seven.setImageResource(R.drawable.radio_red_color);
                                        break;

                                    case 4:
                                        ib_directref_user2_five.setImageResource(R.drawable.radio_red_color);
                                        ib_directref_user2_six.setImageResource(R.drawable.radio_red_color);
                                        ib_directref_user2_seven.setImageResource(R.drawable.radio_red_color);
                                        ib_directref_user2_eight.setImageResource(R.drawable.radio_red_color);
                                        break;
                                }
                                break;

                            case 5:
                                ib_directref_user2_one.setImageResource(R.drawable.radio_green_color);
                                ib_directref_user2_two.setImageResource(R.drawable.radio_green_color);
                                ib_directref_user2_three.setImageResource(R.drawable.radio_green_color);
                                ib_directref_user2_four.setImageResource(R.drawable.radio_green_color);
                                ib_directref_user2_five.setImageResource(R.drawable.radio_green_color);

                                switch (unpaidLegCount) {
                                    case 0:
                                        break;
                                    case 1:
                                        ib_directref_user2_six.setImageResource(R.drawable.radio_red_color);
                                        break;

                                    case 2:
                                        ib_directref_user2_six.setImageResource(R.drawable.radio_red_color);
                                        ib_directref_user2_seven.setImageResource(R.drawable.radio_red_color);
                                        break;

                                    case 3:
                                        ib_directref_user2_six.setImageResource(R.drawable.radio_red_color);
                                        ib_directref_user2_seven.setImageResource(R.drawable.radio_red_color);
                                        ib_directref_user2_eight.setImageResource(R.drawable.radio_red_color);
                                        break;
                                }
                                break;

                            case 6:
                                ib_directref_user2_one.setImageResource(R.drawable.radio_green_color);
                                ib_directref_user2_two.setImageResource(R.drawable.radio_green_color);
                                ib_directref_user2_three.setImageResource(R.drawable.radio_green_color);
                                ib_directref_user2_four.setImageResource(R.drawable.radio_green_color);
                                ib_directref_user2_five.setImageResource(R.drawable.radio_green_color);
                                ib_directref_user2_six.setImageResource(R.drawable.radio_green_color);

                                switch (unpaidLegCount) {
                                    case 0:
                                        break;

                                    case 1:
                                        ib_directref_user2_seven.setImageResource(R.drawable.radio_red_color);
                                        break;

                                    case 2:
                                        ib_directref_user2_seven.setImageResource(R.drawable.radio_red_color);
                                        ib_directref_user2_eight.setImageResource(R.drawable.radio_red_color);
                                        break;
                                }
                                break;

                            case 7:
                                ib_directref_user2_one.setImageResource(R.drawable.radio_green_color);
                                ib_directref_user2_two.setImageResource(R.drawable.radio_green_color);
                                ib_directref_user2_three.setImageResource(R.drawable.radio_green_color);
                                ib_directref_user2_four.setImageResource(R.drawable.radio_green_color);
                                ib_directref_user2_five.setImageResource(R.drawable.radio_green_color);
                                ib_directref_user2_six.setImageResource(R.drawable.radio_green_color);
                                ib_directref_user2_seven.setImageResource(R.drawable.radio_green_color);

                                switch (unpaidLegCount) {
                                    case 0:
                                        break;

                                    case 1:
                                        ib_directref_user2_eight.setImageResource(R.drawable.radio_red_color);
                                        break;
                                }
                                break;

                            case 8:
                                ib_directref_user2_one.setImageResource(R.drawable.radio_green_color);
                                ib_directref_user2_two.setImageResource(R.drawable.radio_green_color);
                                ib_directref_user2_three.setImageResource(R.drawable.radio_green_color);
                                ib_directref_user2_four.setImageResource(R.drawable.radio_green_color);
                                ib_directref_user2_five.setImageResource(R.drawable.radio_green_color);
                                ib_directref_user2_six.setImageResource(R.drawable.radio_green_color);
                                ib_directref_user2_seven.setImageResource(R.drawable.radio_green_color);
                                ib_directref_user2_eight.setImageResource(R.drawable.radio_green_color);

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
                        tv_directref3.setText("Direct ref:- " + three.substring(three.indexOf("#t2#") + 4, three.indexOf("#t3#")));

                        switch (paidLegCount) {
                            case 0:
                                switch (unpaidLegCount) {
                                    case 0:
                                        break;

                                    case 1:
                                        ib_directref_user3_one.setImageResource(R.drawable.radio_red_color);
                                        break;

                                    case 2:
                                        ib_directref_user3_one.setImageResource(R.drawable.radio_red_color);
                                        ib_directref_user3_two.setImageResource(R.drawable.radio_red_color);
                                        break;

                                    case 3:
                                        ib_directref_user3_one.setImageResource(R.drawable.radio_red_color);
                                        ib_directref_user3_two.setImageResource(R.drawable.radio_red_color);
                                        ib_directref_user3_three.setImageResource(R.drawable.radio_red_color);
                                        break;

                                    case 4:
                                        ib_directref_user3_one.setImageResource(R.drawable.radio_red_color);
                                        ib_directref_user3_two.setImageResource(R.drawable.radio_red_color);
                                        ib_directref_user3_three.setImageResource(R.drawable.radio_red_color);
                                        ib_directref_user3_four.setImageResource(R.drawable.radio_red_color);
                                        break;

                                    case 5:
                                        ib_directref_user3_one.setImageResource(R.drawable.radio_red_color);
                                        ib_directref_user3_two.setImageResource(R.drawable.radio_red_color);
                                        ib_directref_user3_three.setImageResource(R.drawable.radio_red_color);
                                        ib_directref_user3_four.setImageResource(R.drawable.radio_red_color);
                                        ib_directref_user3_five.setImageResource(R.drawable.radio_red_color);
                                        break;

                                    case 6:
                                        ib_directref_user3_one.setImageResource(R.drawable.radio_red_color);
                                        ib_directref_user3_two.setImageResource(R.drawable.radio_red_color);
                                        ib_directref_user3_three.setImageResource(R.drawable.radio_red_color);
                                        ib_directref_user3_four.setImageResource(R.drawable.radio_red_color);
                                        ib_directref_user3_five.setImageResource(R.drawable.radio_red_color);
                                        ib_directref_user3_six.setImageResource(R.drawable.radio_red_color);
                                        break;

                                    case 7:
                                        ib_directref_user3_one.setImageResource(R.drawable.radio_red_color);
                                        ib_directref_user3_two.setImageResource(R.drawable.radio_red_color);
                                        ib_directref_user3_three.setImageResource(R.drawable.radio_red_color);
                                        ib_directref_user3_four.setImageResource(R.drawable.radio_red_color);
                                        ib_directref_user3_five.setImageResource(R.drawable.radio_red_color);
                                        ib_directref_user3_six.setImageResource(R.drawable.radio_red_color);
                                        ib_directref_user3_seven.setImageResource(R.drawable.radio_red_color);
                                        break;

                                    case 8:
                                        ib_directref_user3_one.setImageResource(R.drawable.radio_red_color);
                                        ib_directref_user3_two.setImageResource(R.drawable.radio_red_color);
                                        ib_directref_user3_three.setImageResource(R.drawable.radio_red_color);
                                        ib_directref_user3_four.setImageResource(R.drawable.radio_red_color);
                                        ib_directref_user3_five.setImageResource(R.drawable.radio_red_color);
                                        ib_directref_user3_six.setImageResource(R.drawable.radio_red_color);
                                        ib_directref_user3_seven.setImageResource(R.drawable.radio_red_color);
                                        ib_directref_user3_eight.setImageResource(R.drawable.radio_red_color);
                                        break;
                                }
                                break;

                            case 1:
                                ib_directref_user3_one.setImageResource(R.drawable.radio_green_color);

                                switch (unpaidLegCount) {
                                    case 0:
                                        break;

                                    case 1:
                                        ib_directref_user3_two.setImageResource(R.drawable.radio_red_color);
                                        break;

                                    case 2:
                                        ib_directref_user3_two.setImageResource(R.drawable.radio_red_color);
                                        ib_directref_user3_three.setImageResource(R.drawable.radio_red_color);
                                        break;

                                    case 3:
                                        ib_directref_user3_two.setImageResource(R.drawable.radio_red_color);
                                        ib_directref_user3_three.setImageResource(R.drawable.radio_red_color);
                                        ib_directref_user3_four.setImageResource(R.drawable.radio_red_color);
                                        break;

                                    case 4:
                                        ib_directref_user3_two.setImageResource(R.drawable.radio_red_color);
                                        ib_directref_user3_three.setImageResource(R.drawable.radio_red_color);
                                        ib_directref_user3_four.setImageResource(R.drawable.radio_red_color);
                                        ib_directref_user3_five.setImageResource(R.drawable.radio_red_color);
                                        break;

                                    case 5:
                                        ib_directref_user3_two.setImageResource(R.drawable.radio_red_color);
                                        ib_directref_user3_three.setImageResource(R.drawable.radio_red_color);
                                        ib_directref_user3_four.setImageResource(R.drawable.radio_red_color);
                                        ib_directref_user3_five.setImageResource(R.drawable.radio_red_color);
                                        ib_directref_user3_six.setImageResource(R.drawable.radio_red_color);
                                        break;

                                    case 6:
                                        ib_directref_user3_two.setImageResource(R.drawable.radio_red_color);
                                        ib_directref_user3_three.setImageResource(R.drawable.radio_red_color);
                                        ib_directref_user3_four.setImageResource(R.drawable.radio_red_color);
                                        ib_directref_user3_five.setImageResource(R.drawable.radio_red_color);
                                        ib_directref_user3_six.setImageResource(R.drawable.radio_red_color);
                                        ib_directref_user3_seven.setImageResource(R.drawable.radio_red_color);
                                        break;

                                    case 7:
                                        ib_directref_user3_two.setImageResource(R.drawable.radio_red_color);
                                        ib_directref_user3_three.setImageResource(R.drawable.radio_red_color);
                                        ib_directref_user3_four.setImageResource(R.drawable.radio_red_color);
                                        ib_directref_user3_five.setImageResource(R.drawable.radio_red_color);
                                        ib_directref_user3_six.setImageResource(R.drawable.radio_red_color);
                                        ib_directref_user3_seven.setImageResource(R.drawable.radio_red_color);
                                        ib_directref_user3_eight.setImageResource(R.drawable.radio_red_color);
                                        break;
                                }
                                break;

                            case 2:
                                ib_directref_user3_one.setImageResource(R.drawable.radio_green_color);
                                ib_directref_user3_two.setImageResource(R.drawable.radio_green_color);

                                switch (unpaidLegCount) {
                                    case 0:
                                        break;

                                    case 1:
                                        ib_directref_user3_three.setImageResource(R.drawable.radio_red_color);
                                        break;

                                    case 2:
                                        ib_directref_user3_three.setImageResource(R.drawable.radio_red_color);
                                        ib_directref_user3_four.setImageResource(R.drawable.radio_red_color);
                                        break;

                                    case 3:
                                        ib_directref_user3_three.setImageResource(R.drawable.radio_red_color);
                                        ib_directref_user3_four.setImageResource(R.drawable.radio_red_color);
                                        ib_directref_user3_five.setImageResource(R.drawable.radio_red_color);
                                        break;

                                    case 4:
                                        ib_directref_user3_three.setImageResource(R.drawable.radio_red_color);
                                        ib_directref_user3_four.setImageResource(R.drawable.radio_red_color);
                                        ib_directref_user3_five.setImageResource(R.drawable.radio_red_color);
                                        ib_directref_user3_six.setImageResource(R.drawable.radio_red_color);
                                        break;

                                    case 5:
                                        ib_directref_user3_three.setImageResource(R.drawable.radio_red_color);
                                        ib_directref_user3_four.setImageResource(R.drawable.radio_red_color);
                                        ib_directref_user3_five.setImageResource(R.drawable.radio_red_color);
                                        ib_directref_user3_six.setImageResource(R.drawable.radio_red_color);
                                        ib_directref_user3_seven.setImageResource(R.drawable.radio_red_color);
                                        break;

                                    case 6:
                                        ib_directref_user3_three.setImageResource(R.drawable.radio_red_color);
                                        ib_directref_user3_four.setImageResource(R.drawable.radio_red_color);
                                        ib_directref_user3_five.setImageResource(R.drawable.radio_red_color);
                                        ib_directref_user3_six.setImageResource(R.drawable.radio_red_color);
                                        ib_directref_user3_seven.setImageResource(R.drawable.radio_red_color);
                                        ib_directref_user3_eight.setImageResource(R.drawable.radio_red_color);
                                        break;
                                }
                                break;

                            case 3:
                                ib_directref_user3_one.setImageResource(R.drawable.radio_green_color);
                                ib_directref_user3_two.setImageResource(R.drawable.radio_green_color);
                                ib_directref_user3_three.setImageResource(R.drawable.radio_green_color);

                                switch (unpaidLegCount) {
                                    case 0:
                                        break;

                                    case 1:
                                        ib_directref_user3_four.setImageResource(R.drawable.radio_red_color);
                                        break;

                                    case 2:
                                        ib_directref_user3_four.setImageResource(R.drawable.radio_red_color);
                                        ib_directref_user3_five.setImageResource(R.drawable.radio_red_color);
                                        break;

                                    case 3:
                                        ib_directref_user3_four.setImageResource(R.drawable.radio_red_color);
                                        ib_directref_user3_five.setImageResource(R.drawable.radio_red_color);
                                        ib_directref_user3_six.setImageResource(R.drawable.radio_red_color);
                                        break;

                                    case 4:
                                        ib_directref_user3_four.setImageResource(R.drawable.radio_red_color);
                                        ib_directref_user3_five.setImageResource(R.drawable.radio_red_color);
                                        ib_directref_user3_six.setImageResource(R.drawable.radio_red_color);
                                        ib_directref_user3_seven.setImageResource(R.drawable.radio_red_color);
                                        break;

                                    case 5:
                                        ib_directref_user3_four.setImageResource(R.drawable.radio_red_color);
                                        ib_directref_user3_five.setImageResource(R.drawable.radio_red_color);
                                        ib_directref_user3_six.setImageResource(R.drawable.radio_red_color);
                                        ib_directref_user3_seven.setImageResource(R.drawable.radio_red_color);
                                        ib_directref_user3_eight.setImageResource(R.drawable.radio_red_color);
                                        break;
                                }
                                break;

                            case 4:
                                ib_directref_user3_one.setImageResource(R.drawable.radio_green_color);
                                ib_directref_user3_two.setImageResource(R.drawable.radio_green_color);
                                ib_directref_user3_three.setImageResource(R.drawable.radio_green_color);
                                ib_directref_user3_four.setImageResource(R.drawable.radio_green_color);

                                switch (unpaidLegCount) {
                                    case 0:
                                        break;

                                    case 1:
                                        ib_directref_user3_five.setImageResource(R.drawable.radio_red_color);
                                        break;

                                    case 2:
                                        ib_directref_user3_five.setImageResource(R.drawable.radio_red_color);
                                        ib_directref_user3_six.setImageResource(R.drawable.radio_red_color);
                                        break;

                                    case 3:
                                        ib_directref_user3_five.setImageResource(R.drawable.radio_red_color);
                                        ib_directref_user3_six.setImageResource(R.drawable.radio_red_color);
                                        ib_directref_user3_seven.setImageResource(R.drawable.radio_red_color);
                                        break;

                                    case 4:
                                        ib_directref_user3_five.setImageResource(R.drawable.radio_red_color);
                                        ib_directref_user3_six.setImageResource(R.drawable.radio_red_color);
                                        ib_directref_user3_seven.setImageResource(R.drawable.radio_red_color);
                                        ib_directref_user3_eight.setImageResource(R.drawable.radio_red_color);
                                        break;
                                }
                                break;

                            case 5:
                                ib_directref_user3_one.setImageResource(R.drawable.radio_green_color);
                                ib_directref_user3_two.setImageResource(R.drawable.radio_green_color);
                                ib_directref_user3_three.setImageResource(R.drawable.radio_green_color);
                                ib_directref_user3_four.setImageResource(R.drawable.radio_green_color);
                                ib_directref_user3_five.setImageResource(R.drawable.radio_green_color);

                                switch (unpaidLegCount) {
                                    case 0:
                                        break;
                                    case 1:
                                        ib_directref_user3_six.setImageResource(R.drawable.radio_red_color);
                                        break;

                                    case 2:
                                        ib_directref_user3_six.setImageResource(R.drawable.radio_red_color);
                                        ib_directref_user3_seven.setImageResource(R.drawable.radio_red_color);
                                        break;

                                    case 3:
                                        ib_directref_user3_six.setImageResource(R.drawable.radio_red_color);
                                        ib_directref_user3_seven.setImageResource(R.drawable.radio_red_color);
                                        ib_directref_user3_eight.setImageResource(R.drawable.radio_red_color);
                                        break;
                                }
                                break;

                            case 6:
                                ib_directref_user3_one.setImageResource(R.drawable.radio_green_color);
                                ib_directref_user3_two.setImageResource(R.drawable.radio_green_color);
                                ib_directref_user3_three.setImageResource(R.drawable.radio_green_color);
                                ib_directref_user3_four.setImageResource(R.drawable.radio_green_color);
                                ib_directref_user3_five.setImageResource(R.drawable.radio_green_color);
                                ib_directref_user3_six.setImageResource(R.drawable.radio_green_color);

                                switch (unpaidLegCount) {
                                    case 0:
                                        break;

                                    case 1:
                                        ib_directref_user3_seven.setImageResource(R.drawable.radio_red_color);
                                        break;

                                    case 2:
                                        ib_directref_user3_seven.setImageResource(R.drawable.radio_red_color);
                                        ib_directref_user3_eight.setImageResource(R.drawable.radio_red_color);
                                        break;
                                }
                                break;

                            case 7:
                                ib_directref_user3_one.setImageResource(R.drawable.radio_green_color);
                                ib_directref_user3_two.setImageResource(R.drawable.radio_green_color);
                                ib_directref_user3_three.setImageResource(R.drawable.radio_green_color);
                                ib_directref_user3_four.setImageResource(R.drawable.radio_green_color);
                                ib_directref_user3_five.setImageResource(R.drawable.radio_green_color);
                                ib_directref_user3_six.setImageResource(R.drawable.radio_green_color);
                                ib_directref_user3_seven.setImageResource(R.drawable.radio_green_color);

                                switch (unpaidLegCount) {
                                    case 0:
                                        break;

                                    case 1:
                                        ib_directref_user3_eight.setImageResource(R.drawable.radio_red_color);
                                        break;
                                }
                                break;

                            case 8:
                                ib_directref_user3_one.setImageResource(R.drawable.radio_green_color);
                                ib_directref_user3_two.setImageResource(R.drawable.radio_green_color);
                                ib_directref_user3_three.setImageResource(R.drawable.radio_green_color);
                                ib_directref_user3_four.setImageResource(R.drawable.radio_green_color);
                                ib_directref_user3_five.setImageResource(R.drawable.radio_green_color);
                                ib_directref_user3_six.setImageResource(R.drawable.radio_green_color);
                                ib_directref_user3_seven.setImageResource(R.drawable.radio_green_color);
                                ib_directref_user3_eight.setImageResource(R.drawable.radio_green_color);

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
                        tv_directref4.setText("Direct ref:- " + four.substring(four.indexOf("#t2#") + 4, four.indexOf("#t3#")));

                        switch (paidLegCount) {
                            case 0:
                                switch (unpaidLegCount) {
                                    case 0:
                                        break;

                                    case 1:
                                        ib_directref_user4_one.setImageResource(R.drawable.radio_red_color);
                                        break;

                                    case 2:
                                        ib_directref_user4_one.setImageResource(R.drawable.radio_red_color);
                                        ib_directref_user4_two.setImageResource(R.drawable.radio_red_color);
                                        break;

                                    case 3:
                                        ib_directref_user4_one.setImageResource(R.drawable.radio_red_color);
                                        ib_directref_user4_two.setImageResource(R.drawable.radio_red_color);
                                        ib_directref_user4_three.setImageResource(R.drawable.radio_red_color);
                                        break;

                                    case 4:
                                        ib_directref_user4_one.setImageResource(R.drawable.radio_red_color);
                                        ib_directref_user4_two.setImageResource(R.drawable.radio_red_color);
                                        ib_directref_user4_three.setImageResource(R.drawable.radio_red_color);
                                        ib_directref_user4_four.setImageResource(R.drawable.radio_red_color);
                                        break;

                                    case 5:
                                        ib_directref_user4_one.setImageResource(R.drawable.radio_red_color);
                                        ib_directref_user4_two.setImageResource(R.drawable.radio_red_color);
                                        ib_directref_user4_three.setImageResource(R.drawable.radio_red_color);
                                        ib_directref_user4_four.setImageResource(R.drawable.radio_red_color);
                                        ib_directref_user4_five.setImageResource(R.drawable.radio_red_color);
                                        break;

                                    case 6:
                                        ib_directref_user4_one.setImageResource(R.drawable.radio_red_color);
                                        ib_directref_user4_two.setImageResource(R.drawable.radio_red_color);
                                        ib_directref_user4_three.setImageResource(R.drawable.radio_red_color);
                                        ib_directref_user4_four.setImageResource(R.drawable.radio_red_color);
                                        ib_directref_user4_five.setImageResource(R.drawable.radio_red_color);
                                        ib_directref_user4_six.setImageResource(R.drawable.radio_red_color);
                                        break;

                                    case 7:
                                        ib_directref_user4_one.setImageResource(R.drawable.radio_red_color);
                                        ib_directref_user4_two.setImageResource(R.drawable.radio_red_color);
                                        ib_directref_user4_three.setImageResource(R.drawable.radio_red_color);
                                        ib_directref_user4_four.setImageResource(R.drawable.radio_red_color);
                                        ib_directref_user4_five.setImageResource(R.drawable.radio_red_color);
                                        ib_directref_user4_six.setImageResource(R.drawable.radio_red_color);
                                        ib_directref_user4_seven.setImageResource(R.drawable.radio_red_color);
                                        break;

                                    case 8:
                                        ib_directref_user4_one.setImageResource(R.drawable.radio_red_color);
                                        ib_directref_user4_two.setImageResource(R.drawable.radio_red_color);
                                        ib_directref_user4_three.setImageResource(R.drawable.radio_red_color);
                                        ib_directref_user4_four.setImageResource(R.drawable.radio_red_color);
                                        ib_directref_user4_five.setImageResource(R.drawable.radio_red_color);
                                        ib_directref_user4_six.setImageResource(R.drawable.radio_red_color);
                                        ib_directref_user4_seven.setImageResource(R.drawable.radio_red_color);
                                        ib_directref_user4_eight.setImageResource(R.drawable.radio_red_color);
                                        break;
                                }
                                break;

                            case 1:
                                ib_directref_user4_one.setImageResource(R.drawable.radio_green_color);

                                switch (unpaidLegCount) {
                                    case 0:
                                        break;

                                    case 1:
                                        ib_directref_user4_two.setImageResource(R.drawable.radio_red_color);
                                        break;

                                    case 2:
                                        ib_directref_user4_two.setImageResource(R.drawable.radio_red_color);
                                        ib_directref_user4_three.setImageResource(R.drawable.radio_red_color);
                                        break;

                                    case 3:
                                        ib_directref_user4_two.setImageResource(R.drawable.radio_red_color);
                                        ib_directref_user4_three.setImageResource(R.drawable.radio_red_color);
                                        ib_directref_user4_four.setImageResource(R.drawable.radio_red_color);
                                        break;

                                    case 4:
                                        ib_directref_user4_two.setImageResource(R.drawable.radio_red_color);
                                        ib_directref_user4_three.setImageResource(R.drawable.radio_red_color);
                                        ib_directref_user4_four.setImageResource(R.drawable.radio_red_color);
                                        ib_directref_user4_five.setImageResource(R.drawable.radio_red_color);
                                        break;

                                    case 5:
                                        ib_directref_user4_two.setImageResource(R.drawable.radio_red_color);
                                        ib_directref_user4_three.setImageResource(R.drawable.radio_red_color);
                                        ib_directref_user4_four.setImageResource(R.drawable.radio_red_color);
                                        ib_directref_user4_five.setImageResource(R.drawable.radio_red_color);
                                        ib_directref_user4_six.setImageResource(R.drawable.radio_red_color);
                                        break;

                                    case 6:
                                        ib_directref_user4_two.setImageResource(R.drawable.radio_red_color);
                                        ib_directref_user4_three.setImageResource(R.drawable.radio_red_color);
                                        ib_directref_user4_four.setImageResource(R.drawable.radio_red_color);
                                        ib_directref_user4_five.setImageResource(R.drawable.radio_red_color);
                                        ib_directref_user4_six.setImageResource(R.drawable.radio_red_color);
                                        ib_directref_user4_seven.setImageResource(R.drawable.radio_red_color);
                                        break;

                                    case 7:
                                        ib_directref_user4_two.setImageResource(R.drawable.radio_red_color);
                                        ib_directref_user4_three.setImageResource(R.drawable.radio_red_color);
                                        ib_directref_user4_four.setImageResource(R.drawable.radio_red_color);
                                        ib_directref_user4_five.setImageResource(R.drawable.radio_red_color);
                                        ib_directref_user4_six.setImageResource(R.drawable.radio_red_color);
                                        ib_directref_user4_seven.setImageResource(R.drawable.radio_red_color);
                                        ib_directref_user4_eight.setImageResource(R.drawable.radio_red_color);
                                        break;
                                }
                                break;

                            case 2:
                                ib_directref_user4_one.setImageResource(R.drawable.radio_green_color);
                                ib_directref_user4_two.setImageResource(R.drawable.radio_green_color);

                                switch (unpaidLegCount) {
                                    case 0:
                                        break;

                                    case 1:
                                        ib_directref_user4_three.setImageResource(R.drawable.radio_red_color);
                                        break;

                                    case 2:
                                        ib_directref_user4_three.setImageResource(R.drawable.radio_red_color);
                                        ib_directref_user4_four.setImageResource(R.drawable.radio_red_color);
                                        break;

                                    case 3:
                                        ib_directref_user4_three.setImageResource(R.drawable.radio_red_color);
                                        ib_directref_user4_four.setImageResource(R.drawable.radio_red_color);
                                        ib_directref_user4_five.setImageResource(R.drawable.radio_red_color);
                                        break;

                                    case 4:
                                        ib_directref_user4_three.setImageResource(R.drawable.radio_red_color);
                                        ib_directref_user4_four.setImageResource(R.drawable.radio_red_color);
                                        ib_directref_user4_five.setImageResource(R.drawable.radio_red_color);
                                        ib_directref_user4_six.setImageResource(R.drawable.radio_red_color);
                                        break;

                                    case 5:
                                        ib_directref_user4_three.setImageResource(R.drawable.radio_red_color);
                                        ib_directref_user4_four.setImageResource(R.drawable.radio_red_color);
                                        ib_directref_user4_five.setImageResource(R.drawable.radio_red_color);
                                        ib_directref_user4_six.setImageResource(R.drawable.radio_red_color);
                                        ib_directref_user4_seven.setImageResource(R.drawable.radio_red_color);
                                        break;

                                    case 6:
                                        ib_directref_user4_three.setImageResource(R.drawable.radio_red_color);
                                        ib_directref_user4_four.setImageResource(R.drawable.radio_red_color);
                                        ib_directref_user4_five.setImageResource(R.drawable.radio_red_color);
                                        ib_directref_user4_six.setImageResource(R.drawable.radio_red_color);
                                        ib_directref_user4_seven.setImageResource(R.drawable.radio_red_color);
                                        ib_directref_user4_eight.setImageResource(R.drawable.radio_red_color);
                                        break;
                                }
                                break;

                            case 3:
                                ib_directref_user4_one.setImageResource(R.drawable.radio_green_color);
                                ib_directref_user4_two.setImageResource(R.drawable.radio_green_color);
                                ib_directref_user4_three.setImageResource(R.drawable.radio_green_color);

                                switch (unpaidLegCount) {
                                    case 0:
                                        break;

                                    case 1:
                                        ib_directref_user4_four.setImageResource(R.drawable.radio_red_color);
                                        break;

                                    case 2:
                                        ib_directref_user4_four.setImageResource(R.drawable.radio_red_color);
                                        ib_directref_user4_five.setImageResource(R.drawable.radio_red_color);
                                        break;

                                    case 3:
                                        ib_directref_user4_four.setImageResource(R.drawable.radio_red_color);
                                        ib_directref_user4_five.setImageResource(R.drawable.radio_red_color);
                                        ib_directref_user4_six.setImageResource(R.drawable.radio_red_color);
                                        break;

                                    case 4:
                                        ib_directref_user4_four.setImageResource(R.drawable.radio_red_color);
                                        ib_directref_user4_five.setImageResource(R.drawable.radio_red_color);
                                        ib_directref_user4_six.setImageResource(R.drawable.radio_red_color);
                                        ib_directref_user4_seven.setImageResource(R.drawable.radio_red_color);
                                        break;

                                    case 5:
                                        ib_directref_user4_four.setImageResource(R.drawable.radio_red_color);
                                        ib_directref_user4_five.setImageResource(R.drawable.radio_red_color);
                                        ib_directref_user4_six.setImageResource(R.drawable.radio_red_color);
                                        ib_directref_user4_seven.setImageResource(R.drawable.radio_red_color);
                                        ib_directref_user4_eight.setImageResource(R.drawable.radio_red_color);
                                        break;
                                }
                                break;

                            case 4:
                                ib_directref_user4_one.setImageResource(R.drawable.radio_green_color);
                                ib_directref_user4_two.setImageResource(R.drawable.radio_green_color);
                                ib_directref_user4_three.setImageResource(R.drawable.radio_green_color);
                                ib_directref_user4_four.setImageResource(R.drawable.radio_green_color);

                                switch (unpaidLegCount) {
                                    case 0:
                                        break;

                                    case 1:
                                        ib_directref_user4_five.setImageResource(R.drawable.radio_red_color);
                                        break;

                                    case 2:
                                        ib_directref_user4_five.setImageResource(R.drawable.radio_red_color);
                                        ib_directref_user4_six.setImageResource(R.drawable.radio_red_color);
                                        break;

                                    case 3:
                                        ib_directref_user4_five.setImageResource(R.drawable.radio_red_color);
                                        ib_directref_user4_six.setImageResource(R.drawable.radio_red_color);
                                        ib_directref_user4_seven.setImageResource(R.drawable.radio_red_color);
                                        break;

                                    case 4:
                                        ib_directref_user4_five.setImageResource(R.drawable.radio_red_color);
                                        ib_directref_user4_six.setImageResource(R.drawable.radio_red_color);
                                        ib_directref_user4_seven.setImageResource(R.drawable.radio_red_color);
                                        ib_directref_user4_eight.setImageResource(R.drawable.radio_red_color);
                                        break;
                                }
                                break;

                            case 5:
                                ib_directref_user4_one.setImageResource(R.drawable.radio_green_color);
                                ib_directref_user4_two.setImageResource(R.drawable.radio_green_color);
                                ib_directref_user4_three.setImageResource(R.drawable.radio_green_color);
                                ib_directref_user4_four.setImageResource(R.drawable.radio_green_color);
                                ib_directref_user4_five.setImageResource(R.drawable.radio_green_color);

                                switch (unpaidLegCount) {
                                    case 0:
                                        break;
                                    case 1:
                                        ib_directref_user4_six.setImageResource(R.drawable.radio_red_color);
                                        break;

                                    case 2:
                                        ib_directref_user4_six.setImageResource(R.drawable.radio_red_color);
                                        ib_directref_user4_seven.setImageResource(R.drawable.radio_red_color);
                                        break;

                                    case 3:
                                        ib_directref_user4_six.setImageResource(R.drawable.radio_red_color);
                                        ib_directref_user4_seven.setImageResource(R.drawable.radio_red_color);
                                        ib_directref_user4_eight.setImageResource(R.drawable.radio_red_color);
                                        break;
                                }
                                break;

                            case 6:
                                ib_directref_user4_one.setImageResource(R.drawable.radio_green_color);
                                ib_directref_user4_two.setImageResource(R.drawable.radio_green_color);
                                ib_directref_user4_three.setImageResource(R.drawable.radio_green_color);
                                ib_directref_user4_four.setImageResource(R.drawable.radio_green_color);
                                ib_directref_user4_five.setImageResource(R.drawable.radio_green_color);
                                ib_directref_user4_six.setImageResource(R.drawable.radio_green_color);

                                switch (unpaidLegCount) {
                                    case 0:
                                        break;

                                    case 1:
                                        ib_directref_user4_seven.setImageResource(R.drawable.radio_red_color);
                                        break;

                                    case 2:
                                        ib_directref_user4_seven.setImageResource(R.drawable.radio_red_color);
                                        ib_directref_user4_eight.setImageResource(R.drawable.radio_red_color);
                                        break;
                                }
                                break;

                            case 7:
                                ib_directref_user4_one.setImageResource(R.drawable.radio_green_color);
                                ib_directref_user4_two.setImageResource(R.drawable.radio_green_color);
                                ib_directref_user4_three.setImageResource(R.drawable.radio_green_color);
                                ib_directref_user4_four.setImageResource(R.drawable.radio_green_color);
                                ib_directref_user4_five.setImageResource(R.drawable.radio_green_color);
                                ib_directref_user4_six.setImageResource(R.drawable.radio_green_color);
                                ib_directref_user4_seven.setImageResource(R.drawable.radio_green_color);

                                switch (unpaidLegCount) {
                                    case 0:
                                        break;

                                    case 1:
                                        ib_directref_user4_eight.setImageResource(R.drawable.radio_red_color);
                                        break;
                                }
                                break;

                            case 8:
                                ib_directref_user4_one.setImageResource(R.drawable.radio_green_color);
                                ib_directref_user4_two.setImageResource(R.drawable.radio_green_color);
                                ib_directref_user4_three.setImageResource(R.drawable.radio_green_color);
                                ib_directref_user4_four.setImageResource(R.drawable.radio_green_color);
                                ib_directref_user4_five.setImageResource(R.drawable.radio_green_color);
                                ib_directref_user4_six.setImageResource(R.drawable.radio_green_color);
                                ib_directref_user4_seven.setImageResource(R.drawable.radio_green_color);
                                ib_directref_user4_eight.setImageResource(R.drawable.radio_green_color);

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
                        tv_directref5.setText("Direct ref:- " + five.substring(five.indexOf("#t2#") + 4, five.indexOf("#t3#")));

                        switch (paidLegCount) {
                            case 0:
                                switch (unpaidLegCount) {
                                    case 0:
                                        break;

                                    case 1:
                                        ib_directref_user5_one.setImageResource(R.drawable.radio_red_color);
                                        break;

                                    case 2:
                                        ib_directref_user5_one.setImageResource(R.drawable.radio_red_color);
                                        ib_directref_user5_two.setImageResource(R.drawable.radio_red_color);
                                        break;

                                    case 3:
                                        ib_directref_user5_one.setImageResource(R.drawable.radio_red_color);
                                        ib_directref_user5_two.setImageResource(R.drawable.radio_red_color);
                                        ib_directref_user5_three.setImageResource(R.drawable.radio_red_color);
                                        break;

                                    case 4:
                                        ib_directref_user5_one.setImageResource(R.drawable.radio_red_color);
                                        ib_directref_user5_two.setImageResource(R.drawable.radio_red_color);
                                        ib_directref_user5_three.setImageResource(R.drawable.radio_red_color);
                                        ib_directref_user5_four.setImageResource(R.drawable.radio_red_color);
                                        break;

                                    case 5:
                                        ib_directref_user5_one.setImageResource(R.drawable.radio_red_color);
                                        ib_directref_user5_two.setImageResource(R.drawable.radio_red_color);
                                        ib_directref_user5_three.setImageResource(R.drawable.radio_red_color);
                                        ib_directref_user5_four.setImageResource(R.drawable.radio_red_color);
                                        ib_directref_user5_five.setImageResource(R.drawable.radio_red_color);
                                        break;

                                    case 6:
                                        ib_directref_user5_one.setImageResource(R.drawable.radio_red_color);
                                        ib_directref_user5_two.setImageResource(R.drawable.radio_red_color);
                                        ib_directref_user5_three.setImageResource(R.drawable.radio_red_color);
                                        ib_directref_user5_four.setImageResource(R.drawable.radio_red_color);
                                        ib_directref_user5_five.setImageResource(R.drawable.radio_red_color);
                                        ib_directref_user5_six.setImageResource(R.drawable.radio_red_color);
                                        break;

                                    case 7:
                                        ib_directref_user5_one.setImageResource(R.drawable.radio_red_color);
                                        ib_directref_user5_two.setImageResource(R.drawable.radio_red_color);
                                        ib_directref_user5_three.setImageResource(R.drawable.radio_red_color);
                                        ib_directref_user5_four.setImageResource(R.drawable.radio_red_color);
                                        ib_directref_user5_five.setImageResource(R.drawable.radio_red_color);
                                        ib_directref_user5_six.setImageResource(R.drawable.radio_red_color);
                                        ib_directref_user5_seven.setImageResource(R.drawable.radio_red_color);
                                        break;

                                    case 8:
                                        ib_directref_user5_one.setImageResource(R.drawable.radio_red_color);
                                        ib_directref_user5_two.setImageResource(R.drawable.radio_red_color);
                                        ib_directref_user5_three.setImageResource(R.drawable.radio_red_color);
                                        ib_directref_user5_four.setImageResource(R.drawable.radio_red_color);
                                        ib_directref_user5_five.setImageResource(R.drawable.radio_red_color);
                                        ib_directref_user5_six.setImageResource(R.drawable.radio_red_color);
                                        ib_directref_user5_seven.setImageResource(R.drawable.radio_red_color);
                                        ib_directref_user5_eight.setImageResource(R.drawable.radio_red_color);
                                        break;
                                }
                                break;

                            case 1:
                                ib_directref_user5_one.setImageResource(R.drawable.radio_green_color);

                                switch (unpaidLegCount) {
                                    case 0:
                                        break;

                                    case 1:
                                        ib_directref_user5_two.setImageResource(R.drawable.radio_red_color);
                                        break;

                                    case 2:
                                        ib_directref_user5_two.setImageResource(R.drawable.radio_red_color);
                                        ib_directref_user5_three.setImageResource(R.drawable.radio_red_color);
                                        break;

                                    case 3:
                                        ib_directref_user5_two.setImageResource(R.drawable.radio_red_color);
                                        ib_directref_user5_three.setImageResource(R.drawable.radio_red_color);
                                        ib_directref_user5_four.setImageResource(R.drawable.radio_red_color);
                                        break;

                                    case 4:
                                        ib_directref_user5_two.setImageResource(R.drawable.radio_red_color);
                                        ib_directref_user5_three.setImageResource(R.drawable.radio_red_color);
                                        ib_directref_user5_four.setImageResource(R.drawable.radio_red_color);
                                        ib_directref_user5_five.setImageResource(R.drawable.radio_red_color);
                                        break;

                                    case 5:
                                        ib_directref_user5_two.setImageResource(R.drawable.radio_red_color);
                                        ib_directref_user5_three.setImageResource(R.drawable.radio_red_color);
                                        ib_directref_user5_four.setImageResource(R.drawable.radio_red_color);
                                        ib_directref_user5_five.setImageResource(R.drawable.radio_red_color);
                                        ib_directref_user5_six.setImageResource(R.drawable.radio_red_color);
                                        break;

                                    case 6:
                                        ib_directref_user5_two.setImageResource(R.drawable.radio_red_color);
                                        ib_directref_user5_three.setImageResource(R.drawable.radio_red_color);
                                        ib_directref_user5_four.setImageResource(R.drawable.radio_red_color);
                                        ib_directref_user5_five.setImageResource(R.drawable.radio_red_color);
                                        ib_directref_user5_six.setImageResource(R.drawable.radio_red_color);
                                        ib_directref_user5_seven.setImageResource(R.drawable.radio_red_color);
                                        break;

                                    case 7:
                                        ib_directref_user5_two.setImageResource(R.drawable.radio_red_color);
                                        ib_directref_user5_three.setImageResource(R.drawable.radio_red_color);
                                        ib_directref_user5_four.setImageResource(R.drawable.radio_red_color);
                                        ib_directref_user5_five.setImageResource(R.drawable.radio_red_color);
                                        ib_directref_user5_six.setImageResource(R.drawable.radio_red_color);
                                        ib_directref_user5_seven.setImageResource(R.drawable.radio_red_color);
                                        ib_directref_user5_eight.setImageResource(R.drawable.radio_red_color);
                                        break;
                                }
                                break;

                            case 2:
                                ib_directref_user5_one.setImageResource(R.drawable.radio_green_color);
                                ib_directref_user5_two.setImageResource(R.drawable.radio_green_color);

                                switch (unpaidLegCount) {
                                    case 0:
                                        break;

                                    case 1:
                                        ib_directref_user5_three.setImageResource(R.drawable.radio_red_color);
                                        break;

                                    case 2:
                                        ib_directref_user5_three.setImageResource(R.drawable.radio_red_color);
                                        ib_directref_user5_four.setImageResource(R.drawable.radio_red_color);
                                        break;

                                    case 3:
                                        ib_directref_user5_three.setImageResource(R.drawable.radio_red_color);
                                        ib_directref_user5_four.setImageResource(R.drawable.radio_red_color);
                                        ib_directref_user5_five.setImageResource(R.drawable.radio_red_color);
                                        break;

                                    case 4:
                                        ib_directref_user5_three.setImageResource(R.drawable.radio_red_color);
                                        ib_directref_user5_four.setImageResource(R.drawable.radio_red_color);
                                        ib_directref_user5_five.setImageResource(R.drawable.radio_red_color);
                                        ib_directref_user5_six.setImageResource(R.drawable.radio_red_color);
                                        break;

                                    case 5:
                                        ib_directref_user5_three.setImageResource(R.drawable.radio_red_color);
                                        ib_directref_user5_four.setImageResource(R.drawable.radio_red_color);
                                        ib_directref_user5_five.setImageResource(R.drawable.radio_red_color);
                                        ib_directref_user5_six.setImageResource(R.drawable.radio_red_color);
                                        ib_directref_user5_seven.setImageResource(R.drawable.radio_red_color);
                                        break;

                                    case 6:
                                        ib_directref_user5_three.setImageResource(R.drawable.radio_red_color);
                                        ib_directref_user5_four.setImageResource(R.drawable.radio_red_color);
                                        ib_directref_user5_five.setImageResource(R.drawable.radio_red_color);
                                        ib_directref_user5_six.setImageResource(R.drawable.radio_red_color);
                                        ib_directref_user5_seven.setImageResource(R.drawable.radio_red_color);
                                        ib_directref_user5_eight.setImageResource(R.drawable.radio_red_color);
                                        break;
                                }
                                break;

                            case 3:
                                ib_directref_user5_one.setImageResource(R.drawable.radio_green_color);
                                ib_directref_user5_two.setImageResource(R.drawable.radio_green_color);
                                ib_directref_user5_three.setImageResource(R.drawable.radio_green_color);

                                switch (unpaidLegCount) {
                                    case 0:
                                        break;

                                    case 1:
                                        ib_directref_user5_four.setImageResource(R.drawable.radio_red_color);
                                        break;

                                    case 2:
                                        ib_directref_user5_four.setImageResource(R.drawable.radio_red_color);
                                        ib_directref_user5_five.setImageResource(R.drawable.radio_red_color);
                                        break;

                                    case 3:
                                        ib_directref_user5_four.setImageResource(R.drawable.radio_red_color);
                                        ib_directref_user5_five.setImageResource(R.drawable.radio_red_color);
                                        ib_directref_user5_six.setImageResource(R.drawable.radio_red_color);
                                        break;

                                    case 4:
                                        ib_directref_user5_four.setImageResource(R.drawable.radio_red_color);
                                        ib_directref_user5_five.setImageResource(R.drawable.radio_red_color);
                                        ib_directref_user5_six.setImageResource(R.drawable.radio_red_color);
                                        ib_directref_user5_seven.setImageResource(R.drawable.radio_red_color);
                                        break;

                                    case 5:
                                        ib_directref_user5_four.setImageResource(R.drawable.radio_red_color);
                                        ib_directref_user5_five.setImageResource(R.drawable.radio_red_color);
                                        ib_directref_user5_six.setImageResource(R.drawable.radio_red_color);
                                        ib_directref_user5_seven.setImageResource(R.drawable.radio_red_color);
                                        ib_directref_user5_eight.setImageResource(R.drawable.radio_red_color);
                                        break;
                                }
                                break;

                            case 4:
                                ib_directref_user5_one.setImageResource(R.drawable.radio_green_color);
                                ib_directref_user5_two.setImageResource(R.drawable.radio_green_color);
                                ib_directref_user5_three.setImageResource(R.drawable.radio_green_color);
                                ib_directref_user5_four.setImageResource(R.drawable.radio_green_color);

                                switch (unpaidLegCount) {
                                    case 0:
                                        break;

                                    case 1:
                                        ib_directref_user5_five.setImageResource(R.drawable.radio_red_color);
                                        break;

                                    case 2:
                                        ib_directref_user5_five.setImageResource(R.drawable.radio_red_color);
                                        ib_directref_user5_six.setImageResource(R.drawable.radio_red_color);
                                        break;

                                    case 3:
                                        ib_directref_user5_five.setImageResource(R.drawable.radio_red_color);
                                        ib_directref_user5_six.setImageResource(R.drawable.radio_red_color);
                                        ib_directref_user5_seven.setImageResource(R.drawable.radio_red_color);
                                        break;

                                    case 4:
                                        ib_directref_user5_five.setImageResource(R.drawable.radio_red_color);
                                        ib_directref_user5_six.setImageResource(R.drawable.radio_red_color);
                                        ib_directref_user5_seven.setImageResource(R.drawable.radio_red_color);
                                        ib_directref_user5_eight.setImageResource(R.drawable.radio_red_color);
                                        break;
                                }
                                break;

                            case 5:
                                ib_directref_user5_one.setImageResource(R.drawable.radio_green_color);
                                ib_directref_user5_two.setImageResource(R.drawable.radio_green_color);
                                ib_directref_user5_three.setImageResource(R.drawable.radio_green_color);
                                ib_directref_user5_four.setImageResource(R.drawable.radio_green_color);
                                ib_directref_user5_five.setImageResource(R.drawable.radio_green_color);

                                switch (unpaidLegCount) {
                                    case 0:
                                        break;
                                    case 1:
                                        ib_directref_user5_six.setImageResource(R.drawable.radio_red_color);
                                        break;

                                    case 2:
                                        ib_directref_user5_six.setImageResource(R.drawable.radio_red_color);
                                        ib_directref_user5_seven.setImageResource(R.drawable.radio_red_color);
                                        break;

                                    case 3:
                                        ib_directref_user5_six.setImageResource(R.drawable.radio_red_color);
                                        ib_directref_user5_seven.setImageResource(R.drawable.radio_red_color);
                                        ib_directref_user5_eight.setImageResource(R.drawable.radio_red_color);
                                        break;
                                }
                                break;

                            case 6:
                                ib_directref_user5_one.setImageResource(R.drawable.radio_green_color);
                                ib_directref_user5_two.setImageResource(R.drawable.radio_green_color);
                                ib_directref_user5_three.setImageResource(R.drawable.radio_green_color);
                                ib_directref_user5_four.setImageResource(R.drawable.radio_green_color);
                                ib_directref_user5_five.setImageResource(R.drawable.radio_green_color);
                                ib_directref_user5_six.setImageResource(R.drawable.radio_green_color);

                                switch (unpaidLegCount) {
                                    case 0:
                                        break;

                                    case 1:
                                        ib_directref_user5_seven.setImageResource(R.drawable.radio_red_color);
                                        break;

                                    case 2:
                                        ib_directref_user5_seven.setImageResource(R.drawable.radio_red_color);
                                        ib_directref_user5_eight.setImageResource(R.drawable.radio_red_color);
                                        break;
                                }
                                break;

                            case 7:
                                ib_directref_user5_one.setImageResource(R.drawable.radio_green_color);
                                ib_directref_user5_two.setImageResource(R.drawable.radio_green_color);
                                ib_directref_user5_three.setImageResource(R.drawable.radio_green_color);
                                ib_directref_user5_four.setImageResource(R.drawable.radio_green_color);
                                ib_directref_user5_five.setImageResource(R.drawable.radio_green_color);
                                ib_directref_user5_six.setImageResource(R.drawable.radio_green_color);
                                ib_directref_user5_seven.setImageResource(R.drawable.radio_green_color);

                                switch (unpaidLegCount) {
                                    case 0:
                                        break;

                                    case 1:
                                        ib_directref_user5_eight.setImageResource(R.drawable.radio_red_color);
                                        break;
                                }
                                break;

                            case 8:
                                ib_directref_user5_one.setImageResource(R.drawable.radio_green_color);
                                ib_directref_user5_two.setImageResource(R.drawable.radio_green_color);
                                ib_directref_user5_three.setImageResource(R.drawable.radio_green_color);
                                ib_directref_user5_four.setImageResource(R.drawable.radio_green_color);
                                ib_directref_user5_five.setImageResource(R.drawable.radio_green_color);
                                ib_directref_user5_six.setImageResource(R.drawable.radio_green_color);
                                ib_directref_user5_seven.setImageResource(R.drawable.radio_green_color);
                                ib_directref_user5_eight.setImageResource(R.drawable.radio_green_color);

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
                        tv_directref6.setText("Direct ref:- " + six.substring(six.indexOf("#t2#") + 4, six.indexOf("#t3#")));

                        switch (paidLegCount) {
                            case 0:
                                switch (unpaidLegCount) {
                                    case 0:
                                        break;

                                    case 1:
                                        ib_directref_user6_one.setImageResource(R.drawable.radio_red_color);
                                        break;

                                    case 2:
                                        ib_directref_user6_one.setImageResource(R.drawable.radio_red_color);
                                        ib_directref_user6_two.setImageResource(R.drawable.radio_red_color);
                                        break;

                                    case 3:
                                        ib_directref_user6_one.setImageResource(R.drawable.radio_red_color);
                                        ib_directref_user6_two.setImageResource(R.drawable.radio_red_color);
                                        ib_directref_user6_three.setImageResource(R.drawable.radio_red_color);
                                        break;

                                    case 4:
                                        ib_directref_user6_one.setImageResource(R.drawable.radio_red_color);
                                        ib_directref_user6_two.setImageResource(R.drawable.radio_red_color);
                                        ib_directref_user6_three.setImageResource(R.drawable.radio_red_color);
                                        ib_directref_user6_four.setImageResource(R.drawable.radio_red_color);
                                        break;

                                    case 5:
                                        ib_directref_user6_one.setImageResource(R.drawable.radio_red_color);
                                        ib_directref_user6_two.setImageResource(R.drawable.radio_red_color);
                                        ib_directref_user6_three.setImageResource(R.drawable.radio_red_color);
                                        ib_directref_user6_four.setImageResource(R.drawable.radio_red_color);
                                        ib_directref_user6_five.setImageResource(R.drawable.radio_red_color);
                                        break;

                                    case 6:
                                        ib_directref_user6_one.setImageResource(R.drawable.radio_red_color);
                                        ib_directref_user6_two.setImageResource(R.drawable.radio_red_color);
                                        ib_directref_user6_three.setImageResource(R.drawable.radio_red_color);
                                        ib_directref_user6_four.setImageResource(R.drawable.radio_red_color);
                                        ib_directref_user6_five.setImageResource(R.drawable.radio_red_color);
                                        ib_directref_user6_six.setImageResource(R.drawable.radio_red_color);
                                        break;

                                    case 7:
                                        ib_directref_user6_one.setImageResource(R.drawable.radio_red_color);
                                        ib_directref_user6_two.setImageResource(R.drawable.radio_red_color);
                                        ib_directref_user6_three.setImageResource(R.drawable.radio_red_color);
                                        ib_directref_user6_four.setImageResource(R.drawable.radio_red_color);
                                        ib_directref_user6_five.setImageResource(R.drawable.radio_red_color);
                                        ib_directref_user6_six.setImageResource(R.drawable.radio_red_color);
                                        ib_directref_user6_seven.setImageResource(R.drawable.radio_red_color);
                                        break;

                                    case 8:
                                        ib_directref_user6_one.setImageResource(R.drawable.radio_red_color);
                                        ib_directref_user6_two.setImageResource(R.drawable.radio_red_color);
                                        ib_directref_user6_three.setImageResource(R.drawable.radio_red_color);
                                        ib_directref_user6_four.setImageResource(R.drawable.radio_red_color);
                                        ib_directref_user6_five.setImageResource(R.drawable.radio_red_color);
                                        ib_directref_user6_six.setImageResource(R.drawable.radio_red_color);
                                        ib_directref_user6_seven.setImageResource(R.drawable.radio_red_color);
                                        ib_directref_user6_eight.setImageResource(R.drawable.radio_red_color);
                                        break;
                                }
                                break;

                            case 1:
                                ib_directref_user6_one.setImageResource(R.drawable.radio_green_color);

                                switch (unpaidLegCount) {
                                    case 0:
                                        break;

                                    case 1:
                                        ib_directref_user6_two.setImageResource(R.drawable.radio_red_color);
                                        break;

                                    case 2:
                                        ib_directref_user6_two.setImageResource(R.drawable.radio_red_color);
                                        ib_directref_user6_three.setImageResource(R.drawable.radio_red_color);
                                        break;

                                    case 3:
                                        ib_directref_user6_two.setImageResource(R.drawable.radio_red_color);
                                        ib_directref_user6_three.setImageResource(R.drawable.radio_red_color);
                                        ib_directref_user6_four.setImageResource(R.drawable.radio_red_color);
                                        break;

                                    case 4:
                                        ib_directref_user6_two.setImageResource(R.drawable.radio_red_color);
                                        ib_directref_user6_three.setImageResource(R.drawable.radio_red_color);
                                        ib_directref_user6_four.setImageResource(R.drawable.radio_red_color);
                                        ib_directref_user6_five.setImageResource(R.drawable.radio_red_color);
                                        break;

                                    case 5:
                                        ib_directref_user6_two.setImageResource(R.drawable.radio_red_color);
                                        ib_directref_user6_three.setImageResource(R.drawable.radio_red_color);
                                        ib_directref_user6_four.setImageResource(R.drawable.radio_red_color);
                                        ib_directref_user6_five.setImageResource(R.drawable.radio_red_color);
                                        ib_directref_user6_six.setImageResource(R.drawable.radio_red_color);
                                        break;

                                    case 6:
                                        ib_directref_user6_two.setImageResource(R.drawable.radio_red_color);
                                        ib_directref_user6_three.setImageResource(R.drawable.radio_red_color);
                                        ib_directref_user6_four.setImageResource(R.drawable.radio_red_color);
                                        ib_directref_user6_five.setImageResource(R.drawable.radio_red_color);
                                        ib_directref_user6_six.setImageResource(R.drawable.radio_red_color);
                                        ib_directref_user6_seven.setImageResource(R.drawable.radio_red_color);
                                        break;

                                    case 7:
                                        ib_directref_user6_two.setImageResource(R.drawable.radio_red_color);
                                        ib_directref_user6_three.setImageResource(R.drawable.radio_red_color);
                                        ib_directref_user6_four.setImageResource(R.drawable.radio_red_color);
                                        ib_directref_user6_five.setImageResource(R.drawable.radio_red_color);
                                        ib_directref_user6_six.setImageResource(R.drawable.radio_red_color);
                                        ib_directref_user6_seven.setImageResource(R.drawable.radio_red_color);
                                        ib_directref_user6_eight.setImageResource(R.drawable.radio_red_color);
                                        break;
                                }
                                break;

                            case 2:
                                ib_directref_user6_one.setImageResource(R.drawable.radio_green_color);
                                ib_directref_user6_two.setImageResource(R.drawable.radio_green_color);

                                switch (unpaidLegCount) {
                                    case 0:
                                        break;

                                    case 1:
                                        ib_directref_user6_three.setImageResource(R.drawable.radio_red_color);
                                        break;

                                    case 2:
                                        ib_directref_user6_three.setImageResource(R.drawable.radio_red_color);
                                        ib_directref_user6_four.setImageResource(R.drawable.radio_red_color);
                                        break;

                                    case 3:
                                        ib_directref_user6_three.setImageResource(R.drawable.radio_red_color);
                                        ib_directref_user6_four.setImageResource(R.drawable.radio_red_color);
                                        ib_directref_user6_five.setImageResource(R.drawable.radio_red_color);
                                        break;

                                    case 4:
                                        ib_directref_user6_three.setImageResource(R.drawable.radio_red_color);
                                        ib_directref_user6_four.setImageResource(R.drawable.radio_red_color);
                                        ib_directref_user6_five.setImageResource(R.drawable.radio_red_color);
                                        ib_directref_user6_six.setImageResource(R.drawable.radio_red_color);
                                        break;

                                    case 5:
                                        ib_directref_user6_three.setImageResource(R.drawable.radio_red_color);
                                        ib_directref_user6_four.setImageResource(R.drawable.radio_red_color);
                                        ib_directref_user6_five.setImageResource(R.drawable.radio_red_color);
                                        ib_directref_user6_six.setImageResource(R.drawable.radio_red_color);
                                        ib_directref_user6_seven.setImageResource(R.drawable.radio_red_color);
                                        break;

                                    case 6:
                                        ib_directref_user6_three.setImageResource(R.drawable.radio_red_color);
                                        ib_directref_user6_four.setImageResource(R.drawable.radio_red_color);
                                        ib_directref_user6_five.setImageResource(R.drawable.radio_red_color);
                                        ib_directref_user6_six.setImageResource(R.drawable.radio_red_color);
                                        ib_directref_user6_seven.setImageResource(R.drawable.radio_red_color);
                                        ib_directref_user6_eight.setImageResource(R.drawable.radio_red_color);
                                        break;
                                }
                                break;

                            case 3:
                                ib_directref_user6_one.setImageResource(R.drawable.radio_green_color);
                                ib_directref_user6_two.setImageResource(R.drawable.radio_green_color);
                                ib_directref_user6_three.setImageResource(R.drawable.radio_green_color);

                                switch (unpaidLegCount) {
                                    case 0:
                                        break;

                                    case 1:
                                        ib_directref_user6_four.setImageResource(R.drawable.radio_red_color);
                                        break;

                                    case 2:
                                        ib_directref_user6_four.setImageResource(R.drawable.radio_red_color);
                                        ib_directref_user6_five.setImageResource(R.drawable.radio_red_color);
                                        break;

                                    case 3:
                                        ib_directref_user6_four.setImageResource(R.drawable.radio_red_color);
                                        ib_directref_user6_five.setImageResource(R.drawable.radio_red_color);
                                        ib_directref_user6_six.setImageResource(R.drawable.radio_red_color);
                                        break;

                                    case 4:
                                        ib_directref_user6_four.setImageResource(R.drawable.radio_red_color);
                                        ib_directref_user6_five.setImageResource(R.drawable.radio_red_color);
                                        ib_directref_user6_six.setImageResource(R.drawable.radio_red_color);
                                        ib_directref_user6_seven.setImageResource(R.drawable.radio_red_color);
                                        break;

                                    case 5:
                                        ib_directref_user6_four.setImageResource(R.drawable.radio_red_color);
                                        ib_directref_user6_five.setImageResource(R.drawable.radio_red_color);
                                        ib_directref_user6_six.setImageResource(R.drawable.radio_red_color);
                                        ib_directref_user6_seven.setImageResource(R.drawable.radio_red_color);
                                        ib_directref_user6_eight.setImageResource(R.drawable.radio_red_color);
                                        break;
                                }
                                break;

                            case 4:
                                ib_directref_user6_one.setImageResource(R.drawable.radio_green_color);
                                ib_directref_user6_two.setImageResource(R.drawable.radio_green_color);
                                ib_directref_user6_three.setImageResource(R.drawable.radio_green_color);
                                ib_directref_user6_four.setImageResource(R.drawable.radio_green_color);

                                switch (unpaidLegCount) {
                                    case 0:
                                        break;

                                    case 1:
                                        ib_directref_user6_five.setImageResource(R.drawable.radio_red_color);
                                        break;

                                    case 2:
                                        ib_directref_user6_five.setImageResource(R.drawable.radio_red_color);
                                        ib_directref_user6_six.setImageResource(R.drawable.radio_red_color);
                                        break;

                                    case 3:
                                        ib_directref_user6_five.setImageResource(R.drawable.radio_red_color);
                                        ib_directref_user6_six.setImageResource(R.drawable.radio_red_color);
                                        ib_directref_user6_seven.setImageResource(R.drawable.radio_red_color);
                                        break;

                                    case 4:
                                        ib_directref_user6_five.setImageResource(R.drawable.radio_red_color);
                                        ib_directref_user6_six.setImageResource(R.drawable.radio_red_color);
                                        ib_directref_user6_seven.setImageResource(R.drawable.radio_red_color);
                                        ib_directref_user6_eight.setImageResource(R.drawable.radio_red_color);
                                        break;
                                }
                                break;

                            case 5:
                                ib_directref_user6_one.setImageResource(R.drawable.radio_green_color);
                                ib_directref_user6_two.setImageResource(R.drawable.radio_green_color);
                                ib_directref_user6_three.setImageResource(R.drawable.radio_green_color);
                                ib_directref_user6_four.setImageResource(R.drawable.radio_green_color);
                                ib_directref_user6_five.setImageResource(R.drawable.radio_green_color);

                                switch (unpaidLegCount) {
                                    case 0:
                                        break;
                                    case 1:
                                        ib_directref_user6_six.setImageResource(R.drawable.radio_red_color);
                                        break;

                                    case 2:
                                        ib_directref_user6_six.setImageResource(R.drawable.radio_red_color);
                                        ib_directref_user6_seven.setImageResource(R.drawable.radio_red_color);
                                        break;

                                    case 3:
                                        ib_directref_user6_six.setImageResource(R.drawable.radio_red_color);
                                        ib_directref_user6_seven.setImageResource(R.drawable.radio_red_color);
                                        ib_directref_user6_eight.setImageResource(R.drawable.radio_red_color);
                                        break;
                                }
                                break;

                            case 6:
                                ib_directref_user6_one.setImageResource(R.drawable.radio_green_color);
                                ib_directref_user6_two.setImageResource(R.drawable.radio_green_color);
                                ib_directref_user6_three.setImageResource(R.drawable.radio_green_color);
                                ib_directref_user6_four.setImageResource(R.drawable.radio_green_color);
                                ib_directref_user6_five.setImageResource(R.drawable.radio_green_color);
                                ib_directref_user6_six.setImageResource(R.drawable.radio_green_color);

                                switch (unpaidLegCount) {
                                    case 0:
                                        break;

                                    case 1:
                                        ib_directref_user6_seven.setImageResource(R.drawable.radio_red_color);
                                        break;

                                    case 2:
                                        ib_directref_user6_seven.setImageResource(R.drawable.radio_red_color);
                                        ib_directref_user6_eight.setImageResource(R.drawable.radio_red_color);
                                        break;
                                }
                                break;

                            case 7:
                                ib_directref_user6_one.setImageResource(R.drawable.radio_green_color);
                                ib_directref_user6_two.setImageResource(R.drawable.radio_green_color);
                                ib_directref_user6_three.setImageResource(R.drawable.radio_green_color);
                                ib_directref_user6_four.setImageResource(R.drawable.radio_green_color);
                                ib_directref_user6_five.setImageResource(R.drawable.radio_green_color);
                                ib_directref_user6_six.setImageResource(R.drawable.radio_green_color);
                                ib_directref_user6_seven.setImageResource(R.drawable.radio_green_color);

                                switch (unpaidLegCount) {
                                    case 0:
                                        break;

                                    case 1:
                                        ib_directref_user6_eight.setImageResource(R.drawable.radio_red_color);
                                        break;
                                }
                                break;

                            case 8:
                                ib_directref_user6_one.setImageResource(R.drawable.radio_green_color);
                                ib_directref_user6_two.setImageResource(R.drawable.radio_green_color);
                                ib_directref_user6_three.setImageResource(R.drawable.radio_green_color);
                                ib_directref_user6_four.setImageResource(R.drawable.radio_green_color);
                                ib_directref_user6_five.setImageResource(R.drawable.radio_green_color);
                                ib_directref_user6_six.setImageResource(R.drawable.radio_green_color);
                                ib_directref_user6_seven.setImageResource(R.drawable.radio_green_color);
                                ib_directref_user6_eight.setImageResource(R.drawable.radio_green_color);

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
                        tv_directref7.setText("Direct ref:- " + seven.substring(seven.indexOf("#t2#") + 4, seven.indexOf("#t3#")));

                        switch (paidLegCount) {
                            case 0:
                                switch (unpaidLegCount) {
                                    case 0:
                                        break;

                                    case 1:
                                        ib_directref_user7_one.setImageResource(R.drawable.radio_red_color);
                                        break;

                                    case 2:
                                        ib_directref_user7_one.setImageResource(R.drawable.radio_red_color);
                                        ib_directref_user7_two.setImageResource(R.drawable.radio_red_color);
                                        break;

                                    case 3:
                                        ib_directref_user7_one.setImageResource(R.drawable.radio_red_color);
                                        ib_directref_user7_two.setImageResource(R.drawable.radio_red_color);
                                        ib_directref_user7_three.setImageResource(R.drawable.radio_red_color);
                                        break;

                                    case 4:
                                        ib_directref_user7_one.setImageResource(R.drawable.radio_red_color);
                                        ib_directref_user7_two.setImageResource(R.drawable.radio_red_color);
                                        ib_directref_user7_three.setImageResource(R.drawable.radio_red_color);
                                        ib_directref_user7_four.setImageResource(R.drawable.radio_red_color);
                                        break;

                                    case 5:
                                        ib_directref_user7_one.setImageResource(R.drawable.radio_red_color);
                                        ib_directref_user7_two.setImageResource(R.drawable.radio_red_color);
                                        ib_directref_user7_three.setImageResource(R.drawable.radio_red_color);
                                        ib_directref_user7_four.setImageResource(R.drawable.radio_red_color);
                                        ib_directref_user7_five.setImageResource(R.drawable.radio_red_color);
                                        break;

                                    case 6:
                                        ib_directref_user7_one.setImageResource(R.drawable.radio_red_color);
                                        ib_directref_user7_two.setImageResource(R.drawable.radio_red_color);
                                        ib_directref_user7_three.setImageResource(R.drawable.radio_red_color);
                                        ib_directref_user7_four.setImageResource(R.drawable.radio_red_color);
                                        ib_directref_user7_five.setImageResource(R.drawable.radio_red_color);
                                        ib_directref_user7_six.setImageResource(R.drawable.radio_red_color);
                                        break;

                                    case 7:
                                        ib_directref_user7_one.setImageResource(R.drawable.radio_red_color);
                                        ib_directref_user7_two.setImageResource(R.drawable.radio_red_color);
                                        ib_directref_user7_three.setImageResource(R.drawable.radio_red_color);
                                        ib_directref_user7_four.setImageResource(R.drawable.radio_red_color);
                                        ib_directref_user7_five.setImageResource(R.drawable.radio_red_color);
                                        ib_directref_user7_six.setImageResource(R.drawable.radio_red_color);
                                        ib_directref_user7_seven.setImageResource(R.drawable.radio_red_color);
                                        break;

                                    case 8:
                                        ib_directref_user7_one.setImageResource(R.drawable.radio_red_color);
                                        ib_directref_user7_two.setImageResource(R.drawable.radio_red_color);
                                        ib_directref_user7_three.setImageResource(R.drawable.radio_red_color);
                                        ib_directref_user7_four.setImageResource(R.drawable.radio_red_color);
                                        ib_directref_user7_five.setImageResource(R.drawable.radio_red_color);
                                        ib_directref_user7_six.setImageResource(R.drawable.radio_red_color);
                                        ib_directref_user7_seven.setImageResource(R.drawable.radio_red_color);
                                        ib_directref_user7_eight.setImageResource(R.drawable.radio_red_color);
                                        break;
                                }
                                break;

                            case 1:
                                ib_directref_user7_one.setImageResource(R.drawable.radio_green_color);

                                switch (unpaidLegCount) {
                                    case 0:
                                        break;

                                    case 1:
                                        ib_directref_user7_two.setImageResource(R.drawable.radio_red_color);
                                        break;

                                    case 2:
                                        ib_directref_user7_two.setImageResource(R.drawable.radio_red_color);
                                        ib_directref_user7_three.setImageResource(R.drawable.radio_red_color);
                                        break;

                                    case 3:
                                        ib_directref_user7_two.setImageResource(R.drawable.radio_red_color);
                                        ib_directref_user7_three.setImageResource(R.drawable.radio_red_color);
                                        ib_directref_user7_four.setImageResource(R.drawable.radio_red_color);
                                        break;

                                    case 4:
                                        ib_directref_user7_two.setImageResource(R.drawable.radio_red_color);
                                        ib_directref_user7_three.setImageResource(R.drawable.radio_red_color);
                                        ib_directref_user7_four.setImageResource(R.drawable.radio_red_color);
                                        ib_directref_user7_five.setImageResource(R.drawable.radio_red_color);
                                        break;

                                    case 5:
                                        ib_directref_user7_two.setImageResource(R.drawable.radio_red_color);
                                        ib_directref_user7_three.setImageResource(R.drawable.radio_red_color);
                                        ib_directref_user7_four.setImageResource(R.drawable.radio_red_color);
                                        ib_directref_user7_five.setImageResource(R.drawable.radio_red_color);
                                        ib_directref_user7_six.setImageResource(R.drawable.radio_red_color);
                                        break;

                                    case 6:
                                        ib_directref_user7_two.setImageResource(R.drawable.radio_red_color);
                                        ib_directref_user7_three.setImageResource(R.drawable.radio_red_color);
                                        ib_directref_user7_four.setImageResource(R.drawable.radio_red_color);
                                        ib_directref_user7_five.setImageResource(R.drawable.radio_red_color);
                                        ib_directref_user7_six.setImageResource(R.drawable.radio_red_color);
                                        ib_directref_user7_seven.setImageResource(R.drawable.radio_red_color);
                                        break;

                                    case 7:
                                        ib_directref_user7_two.setImageResource(R.drawable.radio_red_color);
                                        ib_directref_user7_three.setImageResource(R.drawable.radio_red_color);
                                        ib_directref_user7_four.setImageResource(R.drawable.radio_red_color);
                                        ib_directref_user7_five.setImageResource(R.drawable.radio_red_color);
                                        ib_directref_user7_six.setImageResource(R.drawable.radio_red_color);
                                        ib_directref_user7_seven.setImageResource(R.drawable.radio_red_color);
                                        ib_directref_user7_eight.setImageResource(R.drawable.radio_red_color);
                                        break;
                                }
                                break;

                            case 2:
                                ib_directref_user7_one.setImageResource(R.drawable.radio_green_color);
                                ib_directref_user7_two.setImageResource(R.drawable.radio_green_color);

                                switch (unpaidLegCount) {
                                    case 0:
                                        break;

                                    case 1:
                                        ib_directref_user7_three.setImageResource(R.drawable.radio_red_color);
                                        break;

                                    case 2:
                                        ib_directref_user7_three.setImageResource(R.drawable.radio_red_color);
                                        ib_directref_user7_four.setImageResource(R.drawable.radio_red_color);
                                        break;

                                    case 3:
                                        ib_directref_user7_three.setImageResource(R.drawable.radio_red_color);
                                        ib_directref_user7_four.setImageResource(R.drawable.radio_red_color);
                                        ib_directref_user7_five.setImageResource(R.drawable.radio_red_color);
                                        break;

                                    case 4:
                                        ib_directref_user7_three.setImageResource(R.drawable.radio_red_color);
                                        ib_directref_user7_four.setImageResource(R.drawable.radio_red_color);
                                        ib_directref_user7_five.setImageResource(R.drawable.radio_red_color);
                                        ib_directref_user7_six.setImageResource(R.drawable.radio_red_color);
                                        break;

                                    case 5:
                                        ib_directref_user7_three.setImageResource(R.drawable.radio_red_color);
                                        ib_directref_user7_four.setImageResource(R.drawable.radio_red_color);
                                        ib_directref_user7_five.setImageResource(R.drawable.radio_red_color);
                                        ib_directref_user7_six.setImageResource(R.drawable.radio_red_color);
                                        ib_directref_user7_seven.setImageResource(R.drawable.radio_red_color);
                                        break;

                                    case 6:
                                        ib_directref_user7_three.setImageResource(R.drawable.radio_red_color);
                                        ib_directref_user7_four.setImageResource(R.drawable.radio_red_color);
                                        ib_directref_user7_five.setImageResource(R.drawable.radio_red_color);
                                        ib_directref_user7_six.setImageResource(R.drawable.radio_red_color);
                                        ib_directref_user7_seven.setImageResource(R.drawable.radio_red_color);
                                        ib_directref_user7_eight.setImageResource(R.drawable.radio_red_color);
                                        break;
                                }
                                break;

                            case 3:
                                ib_directref_user7_one.setImageResource(R.drawable.radio_green_color);
                                ib_directref_user7_two.setImageResource(R.drawable.radio_green_color);
                                ib_directref_user7_three.setImageResource(R.drawable.radio_green_color);

                                switch (unpaidLegCount) {
                                    case 0:
                                        break;

                                    case 1:
                                        ib_directref_user7_four.setImageResource(R.drawable.radio_red_color);
                                        break;

                                    case 2:
                                        ib_directref_user7_four.setImageResource(R.drawable.radio_red_color);
                                        ib_directref_user7_five.setImageResource(R.drawable.radio_red_color);
                                        break;

                                    case 3:
                                        ib_directref_user7_four.setImageResource(R.drawable.radio_red_color);
                                        ib_directref_user7_five.setImageResource(R.drawable.radio_red_color);
                                        ib_directref_user7_six.setImageResource(R.drawable.radio_red_color);
                                        break;

                                    case 4:
                                        ib_directref_user7_four.setImageResource(R.drawable.radio_red_color);
                                        ib_directref_user7_five.setImageResource(R.drawable.radio_red_color);
                                        ib_directref_user7_six.setImageResource(R.drawable.radio_red_color);
                                        ib_directref_user7_seven.setImageResource(R.drawable.radio_red_color);
                                        break;

                                    case 5:
                                        ib_directref_user7_four.setImageResource(R.drawable.radio_red_color);
                                        ib_directref_user7_five.setImageResource(R.drawable.radio_red_color);
                                        ib_directref_user7_six.setImageResource(R.drawable.radio_red_color);
                                        ib_directref_user7_seven.setImageResource(R.drawable.radio_red_color);
                                        ib_directref_user7_eight.setImageResource(R.drawable.radio_red_color);
                                        break;
                                }
                                break;

                            case 4:
                                ib_directref_user7_one.setImageResource(R.drawable.radio_green_color);
                                ib_directref_user7_two.setImageResource(R.drawable.radio_green_color);
                                ib_directref_user7_three.setImageResource(R.drawable.radio_green_color);
                                ib_directref_user7_four.setImageResource(R.drawable.radio_green_color);

                                switch (unpaidLegCount) {
                                    case 0:
                                        break;

                                    case 1:
                                        ib_directref_user7_five.setImageResource(R.drawable.radio_red_color);
                                        break;

                                    case 2:
                                        ib_directref_user7_five.setImageResource(R.drawable.radio_red_color);
                                        ib_directref_user7_six.setImageResource(R.drawable.radio_red_color);
                                        break;

                                    case 3:
                                        ib_directref_user7_five.setImageResource(R.drawable.radio_red_color);
                                        ib_directref_user7_six.setImageResource(R.drawable.radio_red_color);
                                        ib_directref_user7_seven.setImageResource(R.drawable.radio_red_color);
                                        break;

                                    case 4:
                                        ib_directref_user7_five.setImageResource(R.drawable.radio_red_color);
                                        ib_directref_user7_six.setImageResource(R.drawable.radio_red_color);
                                        ib_directref_user7_seven.setImageResource(R.drawable.radio_red_color);
                                        ib_directref_user7_eight.setImageResource(R.drawable.radio_red_color);
                                        break;
                                }
                                break;

                            case 5:
                                ib_directref_user7_one.setImageResource(R.drawable.radio_green_color);
                                ib_directref_user7_two.setImageResource(R.drawable.radio_green_color);
                                ib_directref_user7_three.setImageResource(R.drawable.radio_green_color);
                                ib_directref_user7_four.setImageResource(R.drawable.radio_green_color);
                                ib_directref_user7_five.setImageResource(R.drawable.radio_green_color);

                                switch (unpaidLegCount) {
                                    case 0:
                                        break;
                                    case 1:
                                        ib_directref_user7_six.setImageResource(R.drawable.radio_red_color);
                                        break;

                                    case 2:
                                        ib_directref_user7_six.setImageResource(R.drawable.radio_red_color);
                                        ib_directref_user7_seven.setImageResource(R.drawable.radio_red_color);
                                        break;

                                    case 3:
                                        ib_directref_user7_six.setImageResource(R.drawable.radio_red_color);
                                        ib_directref_user7_seven.setImageResource(R.drawable.radio_red_color);
                                        ib_directref_user7_eight.setImageResource(R.drawable.radio_red_color);
                                        break;
                                }
                                break;

                            case 6:
                                ib_directref_user7_one.setImageResource(R.drawable.radio_green_color);
                                ib_directref_user7_two.setImageResource(R.drawable.radio_green_color);
                                ib_directref_user7_three.setImageResource(R.drawable.radio_green_color);
                                ib_directref_user7_four.setImageResource(R.drawable.radio_green_color);
                                ib_directref_user7_five.setImageResource(R.drawable.radio_green_color);
                                ib_directref_user7_six.setImageResource(R.drawable.radio_green_color);

                                switch (unpaidLegCount) {
                                    case 0:
                                        break;

                                    case 1:
                                        ib_directref_user7_seven.setImageResource(R.drawable.radio_red_color);
                                        break;

                                    case 2:
                                        ib_directref_user7_seven.setImageResource(R.drawable.radio_red_color);
                                        ib_directref_user7_eight.setImageResource(R.drawable.radio_red_color);
                                        break;
                                }
                                break;

                            case 7:
                                ib_directref_user7_one.setImageResource(R.drawable.radio_green_color);
                                ib_directref_user7_two.setImageResource(R.drawable.radio_green_color);
                                ib_directref_user7_three.setImageResource(R.drawable.radio_green_color);
                                ib_directref_user7_four.setImageResource(R.drawable.radio_green_color);
                                ib_directref_user7_five.setImageResource(R.drawable.radio_green_color);
                                ib_directref_user7_six.setImageResource(R.drawable.radio_green_color);
                                ib_directref_user7_seven.setImageResource(R.drawable.radio_green_color);

                                switch (unpaidLegCount) {
                                    case 0:
                                        break;

                                    case 1:
                                        ib_directref_user7_eight.setImageResource(R.drawable.radio_red_color);
                                        break;
                                }
                                break;

                            case 8:
                                ib_directref_user7_one.setImageResource(R.drawable.radio_green_color);
                                ib_directref_user7_two.setImageResource(R.drawable.radio_green_color);
                                ib_directref_user7_three.setImageResource(R.drawable.radio_green_color);
                                ib_directref_user7_four.setImageResource(R.drawable.radio_green_color);
                                ib_directref_user7_five.setImageResource(R.drawable.radio_green_color);
                                ib_directref_user7_six.setImageResource(R.drawable.radio_green_color);
                                ib_directref_user7_seven.setImageResource(R.drawable.radio_green_color);
                                ib_directref_user7_eight.setImageResource(R.drawable.radio_green_color);

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
                        tv_directref8.setText("Direct ref:- " + eight.substring(eight.indexOf("#t2#") + 4, eight.indexOf("#t3#")));

                        switch (paidLegCount) {
                            case 0:
                                switch (unpaidLegCount) {
                                    case 0:
                                        break;

                                    case 1:
                                        ib_directref_user8_one.setImageResource(R.drawable.radio_red_color);
                                        break;

                                    case 2:
                                        ib_directref_user8_one.setImageResource(R.drawable.radio_red_color);
                                        ib_directref_user8_two.setImageResource(R.drawable.radio_red_color);
                                        break;

                                    case 3:
                                        ib_directref_user8_one.setImageResource(R.drawable.radio_red_color);
                                        ib_directref_user8_two.setImageResource(R.drawable.radio_red_color);
                                        ib_directref_user8_three.setImageResource(R.drawable.radio_red_color);
                                        break;

                                    case 4:
                                        ib_directref_user8_one.setImageResource(R.drawable.radio_red_color);
                                        ib_directref_user8_two.setImageResource(R.drawable.radio_red_color);
                                        ib_directref_user8_three.setImageResource(R.drawable.radio_red_color);
                                        ib_directref_user8_four.setImageResource(R.drawable.radio_red_color);
                                        break;

                                    case 5:
                                        ib_directref_user8_one.setImageResource(R.drawable.radio_red_color);
                                        ib_directref_user8_two.setImageResource(R.drawable.radio_red_color);
                                        ib_directref_user8_three.setImageResource(R.drawable.radio_red_color);
                                        ib_directref_user8_four.setImageResource(R.drawable.radio_red_color);
                                        ib_directref_user8_five.setImageResource(R.drawable.radio_red_color);
                                        break;

                                    case 6:
                                        ib_directref_user8_one.setImageResource(R.drawable.radio_red_color);
                                        ib_directref_user8_two.setImageResource(R.drawable.radio_red_color);
                                        ib_directref_user8_three.setImageResource(R.drawable.radio_red_color);
                                        ib_directref_user8_four.setImageResource(R.drawable.radio_red_color);
                                        ib_directref_user8_five.setImageResource(R.drawable.radio_red_color);
                                        ib_directref_user8_six.setImageResource(R.drawable.radio_red_color);
                                        break;

                                    case 7:
                                        ib_directref_user8_one.setImageResource(R.drawable.radio_red_color);
                                        ib_directref_user8_two.setImageResource(R.drawable.radio_red_color);
                                        ib_directref_user8_three.setImageResource(R.drawable.radio_red_color);
                                        ib_directref_user8_four.setImageResource(R.drawable.radio_red_color);
                                        ib_directref_user8_five.setImageResource(R.drawable.radio_red_color);
                                        ib_directref_user8_six.setImageResource(R.drawable.radio_red_color);
                                        ib_directref_user8_seven.setImageResource(R.drawable.radio_red_color);
                                        break;

                                    case 8:
                                        ib_directref_user8_one.setImageResource(R.drawable.radio_red_color);
                                        ib_directref_user8_two.setImageResource(R.drawable.radio_red_color);
                                        ib_directref_user8_three.setImageResource(R.drawable.radio_red_color);
                                        ib_directref_user8_four.setImageResource(R.drawable.radio_red_color);
                                        ib_directref_user8_five.setImageResource(R.drawable.radio_red_color);
                                        ib_directref_user8_six.setImageResource(R.drawable.radio_red_color);
                                        ib_directref_user8_seven.setImageResource(R.drawable.radio_red_color);
                                        ib_directref_user8_eight.setImageResource(R.drawable.radio_red_color);
                                        break;
                                }
                                break;

                            case 1:
                                ib_directref_user8_one.setImageResource(R.drawable.radio_green_color);

                                switch (unpaidLegCount) {
                                    case 0:
                                        break;

                                    case 1:
                                        ib_directref_user8_two.setImageResource(R.drawable.radio_red_color);
                                        break;

                                    case 2:
                                        ib_directref_user8_two.setImageResource(R.drawable.radio_red_color);
                                        ib_directref_user8_three.setImageResource(R.drawable.radio_red_color);
                                        break;

                                    case 3:
                                        ib_directref_user8_two.setImageResource(R.drawable.radio_red_color);
                                        ib_directref_user8_three.setImageResource(R.drawable.radio_red_color);
                                        ib_directref_user8_four.setImageResource(R.drawable.radio_red_color);
                                        break;

                                    case 4:
                                        ib_directref_user8_two.setImageResource(R.drawable.radio_red_color);
                                        ib_directref_user8_three.setImageResource(R.drawable.radio_red_color);
                                        ib_directref_user8_four.setImageResource(R.drawable.radio_red_color);
                                        ib_directref_user8_five.setImageResource(R.drawable.radio_red_color);
                                        break;

                                    case 5:
                                        ib_directref_user8_two.setImageResource(R.drawable.radio_red_color);
                                        ib_directref_user8_three.setImageResource(R.drawable.radio_red_color);
                                        ib_directref_user8_four.setImageResource(R.drawable.radio_red_color);
                                        ib_directref_user8_five.setImageResource(R.drawable.radio_red_color);
                                        ib_directref_user8_six.setImageResource(R.drawable.radio_red_color);
                                        break;

                                    case 6:
                                        ib_directref_user8_two.setImageResource(R.drawable.radio_red_color);
                                        ib_directref_user8_three.setImageResource(R.drawable.radio_red_color);
                                        ib_directref_user8_four.setImageResource(R.drawable.radio_red_color);
                                        ib_directref_user8_five.setImageResource(R.drawable.radio_red_color);
                                        ib_directref_user8_six.setImageResource(R.drawable.radio_red_color);
                                        ib_directref_user8_seven.setImageResource(R.drawable.radio_red_color);
                                        break;

                                    case 7:
                                        ib_directref_user8_two.setImageResource(R.drawable.radio_red_color);
                                        ib_directref_user8_three.setImageResource(R.drawable.radio_red_color);
                                        ib_directref_user8_four.setImageResource(R.drawable.radio_red_color);
                                        ib_directref_user8_five.setImageResource(R.drawable.radio_red_color);
                                        ib_directref_user8_six.setImageResource(R.drawable.radio_red_color);
                                        ib_directref_user8_seven.setImageResource(R.drawable.radio_red_color);
                                        ib_directref_user8_eight.setImageResource(R.drawable.radio_red_color);
                                        break;
                                }
                                break;

                            case 2:
                                ib_directref_user8_one.setImageResource(R.drawable.radio_green_color);
                                ib_directref_user8_two.setImageResource(R.drawable.radio_green_color);

                                switch (unpaidLegCount) {
                                    case 0:
                                        break;

                                    case 1:
                                        ib_directref_user8_three.setImageResource(R.drawable.radio_red_color);
                                        break;

                                    case 2:
                                        ib_directref_user8_three.setImageResource(R.drawable.radio_red_color);
                                        ib_directref_user8_four.setImageResource(R.drawable.radio_red_color);
                                        break;

                                    case 3:
                                        ib_directref_user8_three.setImageResource(R.drawable.radio_red_color);
                                        ib_directref_user8_four.setImageResource(R.drawable.radio_red_color);
                                        ib_directref_user8_five.setImageResource(R.drawable.radio_red_color);
                                        break;

                                    case 4:
                                        ib_directref_user8_three.setImageResource(R.drawable.radio_red_color);
                                        ib_directref_user8_four.setImageResource(R.drawable.radio_red_color);
                                        ib_directref_user8_five.setImageResource(R.drawable.radio_red_color);
                                        ib_directref_user8_six.setImageResource(R.drawable.radio_red_color);
                                        break;

                                    case 5:
                                        ib_directref_user8_three.setImageResource(R.drawable.radio_red_color);
                                        ib_directref_user8_four.setImageResource(R.drawable.radio_red_color);
                                        ib_directref_user8_five.setImageResource(R.drawable.radio_red_color);
                                        ib_directref_user8_six.setImageResource(R.drawable.radio_red_color);
                                        ib_directref_user8_seven.setImageResource(R.drawable.radio_red_color);
                                        break;

                                    case 6:
                                        ib_directref_user8_three.setImageResource(R.drawable.radio_red_color);
                                        ib_directref_user8_four.setImageResource(R.drawable.radio_red_color);
                                        ib_directref_user8_five.setImageResource(R.drawable.radio_red_color);
                                        ib_directref_user8_six.setImageResource(R.drawable.radio_red_color);
                                        ib_directref_user8_seven.setImageResource(R.drawable.radio_red_color);
                                        ib_directref_user8_eight.setImageResource(R.drawable.radio_red_color);
                                        break;
                                }
                                break;

                            case 3:
                                ib_directref_user8_one.setImageResource(R.drawable.radio_green_color);
                                ib_directref_user8_two.setImageResource(R.drawable.radio_green_color);
                                ib_directref_user8_three.setImageResource(R.drawable.radio_green_color);

                                switch (unpaidLegCount) {
                                    case 0:
                                        break;

                                    case 1:
                                        ib_directref_user8_four.setImageResource(R.drawable.radio_red_color);
                                        break;

                                    case 2:
                                        ib_directref_user8_four.setImageResource(R.drawable.radio_red_color);
                                        ib_directref_user8_five.setImageResource(R.drawable.radio_red_color);
                                        break;

                                    case 3:
                                        ib_directref_user8_four.setImageResource(R.drawable.radio_red_color);
                                        ib_directref_user8_five.setImageResource(R.drawable.radio_red_color);
                                        ib_directref_user8_six.setImageResource(R.drawable.radio_red_color);
                                        break;

                                    case 4:
                                        ib_directref_user8_four.setImageResource(R.drawable.radio_red_color);
                                        ib_directref_user8_five.setImageResource(R.drawable.radio_red_color);
                                        ib_directref_user8_six.setImageResource(R.drawable.radio_red_color);
                                        ib_directref_user8_seven.setImageResource(R.drawable.radio_red_color);
                                        break;

                                    case 5:
                                        ib_directref_user8_four.setImageResource(R.drawable.radio_red_color);
                                        ib_directref_user8_five.setImageResource(R.drawable.radio_red_color);
                                        ib_directref_user8_six.setImageResource(R.drawable.radio_red_color);
                                        ib_directref_user8_seven.setImageResource(R.drawable.radio_red_color);
                                        ib_directref_user8_eight.setImageResource(R.drawable.radio_red_color);
                                        break;
                                }
                                break;

                            case 4:
                                ib_directref_user8_one.setImageResource(R.drawable.radio_green_color);
                                ib_directref_user8_two.setImageResource(R.drawable.radio_green_color);
                                ib_directref_user8_three.setImageResource(R.drawable.radio_green_color);
                                ib_directref_user8_four.setImageResource(R.drawable.radio_green_color);

                                switch (unpaidLegCount) {
                                    case 0:
                                        break;

                                    case 1:
                                        ib_directref_user8_five.setImageResource(R.drawable.radio_red_color);
                                        break;

                                    case 2:
                                        ib_directref_user8_five.setImageResource(R.drawable.radio_red_color);
                                        ib_directref_user8_six.setImageResource(R.drawable.radio_red_color);
                                        break;

                                    case 3:
                                        ib_directref_user8_five.setImageResource(R.drawable.radio_red_color);
                                        ib_directref_user8_six.setImageResource(R.drawable.radio_red_color);
                                        ib_directref_user8_seven.setImageResource(R.drawable.radio_red_color);
                                        break;

                                    case 4:
                                        ib_directref_user8_five.setImageResource(R.drawable.radio_red_color);
                                        ib_directref_user8_six.setImageResource(R.drawable.radio_red_color);
                                        ib_directref_user8_seven.setImageResource(R.drawable.radio_red_color);
                                        ib_directref_user8_eight.setImageResource(R.drawable.radio_red_color);
                                        break;
                                }
                                break;

                            case 5:
                                ib_directref_user8_one.setImageResource(R.drawable.radio_green_color);
                                ib_directref_user8_two.setImageResource(R.drawable.radio_green_color);
                                ib_directref_user8_three.setImageResource(R.drawable.radio_green_color);
                                ib_directref_user8_four.setImageResource(R.drawable.radio_green_color);
                                ib_directref_user8_five.setImageResource(R.drawable.radio_green_color);

                                switch (unpaidLegCount) {
                                    case 0:
                                        break;
                                    case 1:
                                        ib_directref_user8_six.setImageResource(R.drawable.radio_red_color);
                                        break;

                                    case 2:
                                        ib_directref_user8_six.setImageResource(R.drawable.radio_red_color);
                                        ib_directref_user8_seven.setImageResource(R.drawable.radio_red_color);
                                        break;

                                    case 3:
                                        ib_directref_user8_six.setImageResource(R.drawable.radio_red_color);
                                        ib_directref_user8_seven.setImageResource(R.drawable.radio_red_color);
                                        ib_directref_user8_eight.setImageResource(R.drawable.radio_red_color);
                                        break;
                                }
                                break;

                            case 6:
                                ib_directref_user8_one.setImageResource(R.drawable.radio_green_color);
                                ib_directref_user8_two.setImageResource(R.drawable.radio_green_color);
                                ib_directref_user8_three.setImageResource(R.drawable.radio_green_color);
                                ib_directref_user8_four.setImageResource(R.drawable.radio_green_color);
                                ib_directref_user8_five.setImageResource(R.drawable.radio_green_color);
                                ib_directref_user8_six.setImageResource(R.drawable.radio_green_color);

                                switch (unpaidLegCount) {
                                    case 0:
                                        break;

                                    case 1:
                                        ib_directref_user8_seven.setImageResource(R.drawable.radio_red_color);
                                        break;

                                    case 2:
                                        ib_directref_user8_seven.setImageResource(R.drawable.radio_red_color);
                                        ib_directref_user8_eight.setImageResource(R.drawable.radio_red_color);
                                        break;
                                }
                                break;

                            case 7:
                                ib_directref_user8_one.setImageResource(R.drawable.radio_green_color);
                                ib_directref_user8_two.setImageResource(R.drawable.radio_green_color);
                                ib_directref_user8_three.setImageResource(R.drawable.radio_green_color);
                                ib_directref_user8_four.setImageResource(R.drawable.radio_green_color);
                                ib_directref_user8_five.setImageResource(R.drawable.radio_green_color);
                                ib_directref_user8_six.setImageResource(R.drawable.radio_green_color);
                                ib_directref_user8_seven.setImageResource(R.drawable.radio_green_color);

                                switch (unpaidLegCount) {
                                    case 0:
                                        break;

                                    case 1:
                                        ib_directref_user8_eight.setImageResource(R.drawable.radio_red_color);
                                        break;
                                }
                                break;

                            case 8:
                                ib_directref_user8_one.setImageResource(R.drawable.radio_green_color);
                                ib_directref_user8_two.setImageResource(R.drawable.radio_green_color);
                                ib_directref_user8_three.setImageResource(R.drawable.radio_green_color);
                                ib_directref_user8_four.setImageResource(R.drawable.radio_green_color);
                                ib_directref_user8_five.setImageResource(R.drawable.radio_green_color);
                                ib_directref_user8_six.setImageResource(R.drawable.radio_green_color);
                                ib_directref_user8_seven.setImageResource(R.drawable.radio_green_color);
                                ib_directref_user8_eight.setImageResource(R.drawable.radio_green_color);

                                switch (unpaidLegCount) {
                                    case 0:
                                        break;
                                }
                                break;
                        }
                    }
                }

                if (response.contains("idisempty")) {
                    dialog.dismiss();
                    Toast.makeText(DirectRefDisplayTeamActivity.this, "no data found.", Toast.LENGTH_SHORT).show();
                }

                if (response.contains("nodatafound")) {
                    dialog.dismiss();
                    Toast.makeText(DirectRefDisplayTeamActivity.this, "no data found.", Toast.LENGTH_SHORT).show();
                }

                if (response.contains("dbConErr")) {
                    dialog.dismiss();
                    Toast.makeText(DirectRefDisplayTeamActivity.this, "Database connection error.", Toast.LENGTH_SHORT).show();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(DirectRefDisplayTeamActivity.this, "Something went wrong.", Toast.LENGTH_SHORT).show();
                dialog.dismiss();
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> map = new HashMap<>();
                map.put("id", id);
                return map;
            }
        };
        Volley.newRequestQueue(DirectRefDisplayTeamActivity.this).add(stringRequest);
    }

    @Override
    public void onBackPressed() {
        isBackpress = true;
        id = "";
        String leg_exp = (leg_expansion.getString("userid", ""));

        if (leg_exp.length() > 11) {
            String retriveData = leg_exp.substring(leg_exp.length() - 22, leg_exp.length() - 11);

            //Log.d("spData148", "retrive data " + retriveData);
            id = retriveData;
            leg_expansion = getSharedPreferences("legs_data", MODE_PRIVATE);
            SharedPreferences.Editor edit = leg_expansion.edit();
            String str = leg_exp.substring(0, leg_exp.length() - 11);
            //Log.d("spData148", "return save data to sp " + str);
            edit.putString("userid", str);
            edit.commit();

            Log.d("spData148", "sentto db " + id);
            colorChangedBlack();
            sentUserIdToDb();

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