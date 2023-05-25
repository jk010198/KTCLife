package com.ithublive.ktclife;

import android.app.ActivityOptions;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.util.Pair;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.RotateAnimation;
import android.widget.Adapter;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.SpinnerAdapter;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import com.android.volley.AuthFailureError;
import com.android.volley.BuildConfig;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.google.android.material.navigation.NavigationView;
import com.ithublive.ktclife.Admin.Reports;
import com.ithublive.ktclife.Utils.BaseUrl;
import com.ithublive.ktclife.achieversgallery.AddAchieversActivity;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;

import static com.ithublive.ktclife.R.drawable.level_diamond;
import static com.ithublive.ktclife.R.drawable.level_gold;
import static com.ithublive.ktclife.R.drawable.level_ruby;
import static com.ithublive.ktclife.R.drawable.level_silver;

public class HomeActivity extends AppCompatActivity {

    FrameLayout showprofileButton, report_btn;
    DrawerLayout drawer;
    FrameLayout drawerBtn;
    boolean is_account_mode = false;
    Menu menu_navigation;
    View navigation_header;
    Toolbar toolbar;
    NavigationView navView;
    CircleImageView level_image, profile_image;
    String dbId, dbName, dbMobileno, dbReference, dbProfile_imgurl, dbAddress, dbPlan, dbPassword, dbIs_kyc, dbAadhar_number, dbAadhar_front_url,
            dbAadhar_back_url, dbAccount_details_url, sharedpref_id;
    ImageButton ib_profile, ib_cap_limit, ib_bonus, ib_tree_plan, ib_new_signup, ib_levelTarget, ib_monthly_target, ib_newIdOneTimeBonus,
            ib_income_plan, ib_showPlan, ib_payMode, ib_shareApp, ib_achievers, ib_electricitybills, ib_mobilerecharge;
    TextView tv_profile, tv_cap_limit, tv_bonus, tv_tree_plan, tv_new_signup, tv_levelTarget, tv_monthly_target, tv_newIdOneTimeBonus,
            tv_income_plan, tv_showPlan, tv_payMode, tv_achievers, tv_electricitybills, tv_mobilerecharge;
    LinearLayout tv_direct_ref;
    SharedPreferences sharedPreferences_retriveUserData;
    String retriveUserData;
    Dialog dialog;
    Animation anim, anim_levelImage;
    long animationTime = 400;
    private String levelColor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        anim = new AlphaAnimation(0.0f, 1.0f);
        anim.setDuration(50); //You can manage the blinking time with this parameter

        anim.setRepeatMode(Animation.REVERSE);
        anim.setRepeatCount(Animation.INFINITE);
        anim_levelImage = new RotateAnimation(0.0f, 360.0f, 100f, 100f);
        anim_levelImage.setDuration(6000);
        anim_levelImage.setStartOffset(10);
        anim_levelImage.setRepeatCount(Animation.INFINITE);

        level_image = findViewById(R.id.userLevel_img);
        level_image.setVisibility(View.INVISIBLE);
        ib_profile = findViewById(R.id.homepage_imagebtn_profile);

        ib_cap_limit = findViewById(R.id.homepage_imagebtn_capinglimit);
        ib_bonus = findViewById(R.id.homepage_imagebtn_bonus);
        ib_tree_plan = findViewById(R.id.homepage_imagebtn_tree_plan);
        ib_new_signup = findViewById(R.id.homepage_imagebtn_new_signup);
        ib_levelTarget = findViewById(R.id.homepage_imagebtn_levelTarget);
        ib_monthly_target = findViewById(R.id.homepage_imagebtn_MT);
        ib_newIdOneTimeBonus = findViewById(R.id.homepage_imagebtn_newIDOneTimeBonusPlan);
        ib_income_plan = findViewById(R.id.homepage_imagebtn_IncomePlan);
        ib_showPlan = findViewById(R.id.homepage_imagebtn_ShowPlan);
        ib_payMode = findViewById(R.id.homepage_imagebtn_PayMode);
        ib_shareApp = findViewById(R.id.homepage_imagebtn_ShareApp);
        ib_achievers = findViewById(R.id.homepage_imagebtn_Achievers);
        ib_electricitybills = findViewById(R.id.homepage_imagebtn_Electricitybill);
        ib_mobilerecharge = findViewById(R.id.homepage_imagebtn_Mobilerecharge);

        tv_profile = findViewById(R.id.tv_homepage_profile);
        tv_direct_ref = findViewById(R.id.tv_homepage_direct_ref);
        tv_profile = findViewById(R.id.tv_homepage_profile);
        tv_cap_limit = findViewById(R.id.tv_homepage_cap_limit);
        tv_bonus = findViewById(R.id.tv_homepage_bonus);
        tv_tree_plan = findViewById(R.id.tv_homepage_tree);
        tv_new_signup = findViewById(R.id.tv_homepage_new_signup);
        tv_levelTarget = findViewById(R.id.tv_homepage_levelTarget);
        tv_monthly_target = findViewById(R.id.tv_homepage_CustomerBonus);
        tv_newIdOneTimeBonus = findViewById(R.id.tv_homepage_newIDOneTimeBonusPlan);
        tv_income_plan = findViewById(R.id.tv_homepage_IncomePlan);
        tv_showPlan = findViewById(R.id.tv_homepage_ShowPlan);
        tv_payMode = findViewById(R.id.tv_homepage_PayMode);
        tv_achievers = findViewById(R.id.tv_homepage_Achievers);
        tv_electricitybills = findViewById(R.id.tv_homepage_Electricitybill);
        tv_mobilerecharge = findViewById(R.id.tv_homepage_Mobilerecharge);

        navView = findViewById(R.id.nav_view);
        profile_image = findViewById(R.id.userprofile_img);
        showprofileButton = findViewById(R.id.cart_button);
        report_btn = findViewById(R.id.report_button);
        drawerBtn = findViewById(R.id.drawer_btn);
        sharedPreferences_retriveUserData = getSharedPreferences("" + R.string.sp_retriveUserData, MODE_PRIVATE);

        // what does when click imagebutton code start //
        ib_profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ib_profile.startAnimation(anim);
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        ib_profile.clearAnimation();
                        startActivity(new Intent(getApplicationContext(), HomeProfileActivity.class));
                    }
                }, animationTime);
            }
        });

        ib_cap_limit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ib_cap_limit.startAnimation(anim);
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        ib_cap_limit.clearAnimation();
                        popupCappingLimit();
                    }
                }, animationTime);
            }
        });

        ib_bonus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ib_bonus.startAnimation(anim);
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        ib_bonus.clearAnimation();
                        startActivity(new Intent(getApplicationContext(), BonusActivity.class));
                    }
                }, animationTime);
            }
        });

        ib_tree_plan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ib_tree_plan.startAnimation(anim);
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        ib_tree_plan.clearAnimation();
                        startActivity(new Intent(getApplicationContext(), DisplayTeam.class));
                    }
                }, animationTime);
            }
        });

        ib_new_signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ib_new_signup.startAnimation(anim);
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        ib_new_signup.clearAnimation();
                        startActivity(new Intent(getApplicationContext(), SignUpActivity.class));
                    }
                }, animationTime);
            }
        });

        ib_levelTarget.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ib_levelTarget.startAnimation(anim);
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        ib_levelTarget.clearAnimation();
                        startActivity(new Intent(getApplicationContext(), LevelTargetActivity.class));
                    }
                }, animationTime);
            }
        });

        ib_monthly_target.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ib_monthly_target.startAnimation(anim);
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        ib_monthly_target.clearAnimation();
                        startActivity(new Intent(getApplicationContext(), ShowMonthlyTargetActivity.class));
                    }
                }, animationTime);
            }
        });

        ib_newIdOneTimeBonus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ib_newIdOneTimeBonus.startAnimation(anim);
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        ib_newIdOneTimeBonus.clearAnimation();
                        startActivity(new Intent(getApplicationContext(), ShowNewIDOneTimeBonusActivity.class));
                    }
                }, animationTime);
            }
        });

        ib_income_plan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ib_income_plan.startAnimation(anim);
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        ib_income_plan.clearAnimation();
                        startActivity(new Intent(getApplicationContext(), ShowIncomePlanActivity.class));
                    }
                }, animationTime);
            }
        });

        ib_showPlan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ib_showPlan.startAnimation(anim);
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        ib_showPlan.clearAnimation();
                        startActivity(new Intent(getApplicationContext(), ShowPlanActivity.class));
                    }
                }, animationTime);
            }
        });

        ib_payMode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ib_payMode.startAnimation(anim);
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        ib_payMode.clearAnimation();
                        startActivity(new Intent(getApplicationContext(), HomeProfileActivity.class));
                    }
                }, animationTime);
            }
        });

        ib_shareApp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder adbShareApp = new AlertDialog.Builder(HomeActivity.this);
                adbShareApp.setTitle("Share KTCLife");
                adbShareApp.setMessage("Share KTCLife app with friends and family.");
                adbShareApp.setIcon(R.drawable.app_logo);
                adbShareApp.setPositiveButton("Share", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Intent shareIntent = new Intent(Intent.ACTION_SEND);
                        shareIntent.setType("text/plain");
                        shareIntent.putExtra(Intent.EXTRA_SUBJECT, "KTCLife");
                        String shareMessage = "Join 'KTCLife': a unique business plan. Refer friend and family and earn from 300 to 30000 upon there payment.\n ";
                        shareMessage = shareMessage + "https://play.google.com/store/apps/details?id=" + BuildConfig.APPLICATION_ID + "  \n\n";
                        shareIntent.putExtra(Intent.EXTRA_TEXT, shareMessage);
                        startActivity(Intent.createChooser(shareIntent, "choose one"));
                    }
                });
                adbShareApp.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                    }
                });
                adbShareApp.show();
            }
        });

        ib_achievers.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ib_achievers.startAnimation(anim);
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        ib_achievers.clearAnimation();
                        startActivity(new Intent(getApplicationContext(), ShowAchieversActivity.class));
                    }
                }, animationTime);
            }
        });

        ib_electricitybills.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ib_electricitybills.startAnimation(anim);
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        ib_electricitybills.clearAnimation();
                        Toast.makeText(HomeActivity.this, "Under maintenance", Toast.LENGTH_SHORT).show();
                        //startActivity(new Intent(getApplicationContext(), ShowAchieversActivity.class));
                    }
                }, animationTime);
            }
        });

        ib_mobilerecharge.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ib_mobilerecharge.startAnimation(anim);
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        ib_mobilerecharge.clearAnimation();
                        Toast.makeText(HomeActivity.this, "Under maintenance", Toast.LENGTH_SHORT).show();
                        //startActivity(new Intent(getApplicationContext(), PaymentHistoryList.class));
                    }
                }, animationTime);
            }
        });
        // what does when click imagebutton code end //

        // what does when click textview code start //
        tv_profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(), HomeProfileActivity.class));
            }
        });

        tv_direct_ref.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                tv_direct_ref.startAnimation(anim);
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        tv_direct_ref.clearAnimation();
                        //startActivity(new Intent(getApplicationContext(), DirectReferenceActivity.class));
                    }
                }, animationTime);
            }
        });

        tv_cap_limit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                popupCappingLimit();
            }
        });

        tv_bonus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(), BonusActivity.class));
            }
        });

        tv_tree_plan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(), DisplayTeam.class));
            }
        });

        tv_new_signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(), SignUpActivity.class));
            }
        });

        tv_levelTarget.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(), LevelTargetActivity.class));
            }
        });

        tv_monthly_target.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(), ShowMonthlyTargetActivity.class));
            }
        });

        tv_newIdOneTimeBonus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(), ShowNewIDOneTimeBonusActivity.class));
            }
        });

        tv_income_plan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(), ShowIncomePlanActivity.class));
            }
        });

        tv_showPlan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(), ShowPlanActivity.class));
            }
        });

        tv_payMode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(), PaymentModesActivity.class));
            }
        });

        tv_achievers.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(), ShowAchieversActivity.class));
            }
        });

        tv_electricitybills.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(HomeActivity.this, "Under maintenance", Toast.LENGTH_SHORT).show();
                //startActivity(new Intent(HomeActivity.this, ElecticityBillPay.class));
            }
        });

        tv_mobilerecharge.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(HomeActivity.this, "Under maintenance", Toast.LENGTH_SHORT).show();
            }
        });
        // what does when click imagebutton code end //

        SharedPreferences shared = getSharedPreferences("" + R.string.sp_name, MODE_PRIVATE);
        sharedpref_id = (shared.getString("username", ""));

        initNavigationMenu();
        drawer.closeDrawer(GravityCompat.START);

        if (sharedpref_id.equals("95947639111")) {
            report_btn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    startActivity(new Intent(getApplicationContext(), Reports.class));
                }
            });
        } else {
            report_btn.setVisibility(View.GONE);
        }

        showprofileButton.setOnClickListener(new View.OnClickListener() {
            //@RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
            public void onClick(View view) {
                Intent sharedIntent = new Intent(getApplicationContext(), OurProfileActivity.class);
                sharedIntent.putExtra("id", dbId);
                Pair[] pairs = new Pair[1];
                pairs[0] = new Pair<View, String>(profile_image, "imageTransition");
                ActivityOptions options = null;
                if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
                    options = ActivityOptions.makeSceneTransitionAnimation(HomeActivity.this, pairs);
                }
                startActivity(sharedIntent, options.toBundle());
            }
        });

        drawerBtn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                HomeActivity.this.drawer.openDrawer((int) GravityCompat.START);
            }
        });

        dialog = new Dialog(HomeActivity.this);
        dialog.setContentView(R.layout.layout_progress_dialog);
        if (dialog.getWindow() != null) {
            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(0));
        }
        dialog.setCancelable(false);
        dialog.show();
        sentUserIdToDb();
    }

    public void sentUserIdToDb() {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, BaseUrl.retriveUserDataUrl, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                if (response.contains("no data found")) {
                    dialog.dismiss();
                    SharedPreferences.Editor edit = sharedPreferences_retriveUserData.edit();
                    edit.putString("data", "no data found");
                    edit.commit();
                    Toast.makeText(HomeActivity.this, "No data found.", Toast.LENGTH_SHORT).show();
                }

                if (response.contains(sharedpref_id)) {
                    retriveUserData = response;
                    SharedPreferences.Editor edit = sharedPreferences_retriveUserData.edit();
                    edit.putString("data", retriveUserData);
                    edit.commit();

                    dbId = retriveUserData.substring(retriveUserData.indexOf("!") + 1, retriveUserData.indexOf("@"));
                    dbName = retriveUserData.substring(retriveUserData.indexOf("@") + 1, retriveUserData.indexOf("#"));
                    dbMobileno = retriveUserData.substring(retriveUserData.indexOf("#") + 1, retriveUserData.indexOf("$"));
                    dbProfile_imgurl = retriveUserData.substring(retriveUserData.indexOf("$") + 1, retriveUserData.indexOf("%"));
                    dbReference = retriveUserData.substring(retriveUserData.indexOf("%") + 1, retriveUserData.indexOf("&"));
                    dbPlan = retriveUserData.substring(retriveUserData.indexOf("&") + 1, retriveUserData.indexOf("*"));
                    dbAddress = retriveUserData.substring(retriveUserData.indexOf("*") + 1, retriveUserData.indexOf("?"));
                    dbPassword = retriveUserData.substring(retriveUserData.indexOf("?") + 1, retriveUserData.indexOf("#1A#"));
                    dbIs_kyc = retriveUserData.substring(retriveUserData.indexOf("#4A#") + 4, retriveUserData.indexOf("#5A#"));
                    dbAadhar_number = retriveUserData.substring(retriveUserData.indexOf("#5A#") + 4, retriveUserData.indexOf("#6A#"));
                    dbAadhar_front_url = retriveUserData.substring(retriveUserData.indexOf("#6A#") + 4, retriveUserData.indexOf("#7A#"));
                    dbAadhar_back_url = retriveUserData.substring(retriveUserData.indexOf("#7A#") + 4, retriveUserData.indexOf("#8A#"));
                    dbAccount_details_url = retriveUserData.substring(retriveUserData.indexOf("#8A#") + 4, retriveUserData.indexOf("#9A#"));
                    ////////////////////////////////////////////
                    if (retriveUserData.contains("silver")) {
                        int legs = Integer.parseInt(retriveUserData.substring(retriveUserData.indexOf("silver") + 6, retriveUserData.indexOf("gold")));
                        if (legs >= 8) {
                            levelColor = "silver";
                            int silverLegs = Integer.parseInt(retriveUserData.substring(retriveUserData.indexOf("gold") + 4, retriveUserData.indexOf("ruby")));
                            if (silverLegs >= 8) {
                                levelColor = "gold";
                                int goldLegs = Integer.parseInt(retriveUserData.substring(retriveUserData.indexOf("ruby") + 4, retriveUserData.indexOf("diamond")));
                                if (goldLegs >= 8) {
                                    levelColor = "ruby";
                                    int rubyLegs = Integer.parseInt(retriveUserData.substring(retriveUserData.indexOf("diamond") + 7,
                                            retriveUserData.indexOf("end_level_count")));
                                    if (rubyLegs >= 8) {
                                        levelColor = "diamond";
                                    }
                                }
                            }
                        }

                        if (levelColor != null && levelColor.equals("silver")) {
                            Glide.with(HomeActivity.this)
                                    .load(level_silver)
                                    .into(level_image);
                            level_image.startAnimation(anim_levelImage);
                            level_image.setVisibility(View.VISIBLE);
                        } else if (levelColor != null && levelColor.equals("gold")) {
                            Glide.with(HomeActivity.this)
                                    .load(level_gold)
                                    .into(level_image);
                            level_image.startAnimation(anim_levelImage);
                            level_image.setVisibility(View.VISIBLE);
                        } else if (levelColor != null && levelColor.equals("diamond")) {
                            Glide.with(HomeActivity.this)
                                    .load(level_diamond)
                                    .into(level_image);
                            level_image.startAnimation(anim_levelImage);
                            level_image.setVisibility(View.VISIBLE);
                        } else if (levelColor != null && levelColor.equals("ruby")) {
                            Glide.with(HomeActivity.this)
                                    .load(level_ruby)
                                    .into(level_image);
                            level_image.startAnimation(anim_levelImage);
                            level_image.setVisibility(View.VISIBLE);
                        }
                    }
                    ////////////////////////////////////////////
                    if (dbIs_kyc.isEmpty() || dbIs_kyc.contains("No")) {
                        showKycPopup();
                    }

                    initNavigationMenu();
                    drawer.closeDrawer(GravityCompat.START);
                    RequestOptions requestOptions = new RequestOptions();
                    requestOptions.placeholder(R.drawable.ic_person);
                    requestOptions.error(R.drawable.ic_person);
                    Glide.with(HomeActivity.this)
                            .load(dbProfile_imgurl)
                            .apply(requestOptions)
                            .into(profile_image);
                    dialog.dismiss();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(HomeActivity.this, "Server error.", Toast.LENGTH_SHORT).show();
                dialog.dismiss();
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> map = new HashMap<>();
                map.put("id", sharedpref_id);
                return map;
            }
        };
        Volley.newRequestQueue(HomeActivity.this).add(stringRequest);
    }

    public void popupCappingLimit() {
        SharedPreferences shared = getSharedPreferences("" + R.string.sp_retriveUserData, MODE_PRIVATE);
        retriveUserData = (shared.getString("data", ""));

        if (retriveUserData.contains("no data found")) {
            dialog = new Dialog(HomeActivity.this);
            dialog.setContentView(R.layout.layout_caplimit_dialog);
            dialog.getWindow().setLayout(900, 550);
            dialog.setCancelable(true);
            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

            TextView tv_caplimit = dialog.findViewById(R.id.tv_caplimit_msg);
            tv_caplimit.setText("There are no data found.");
            Button btn_ok = dialog.findViewById(R.id.btn_caplimit_ok);

            btn_ok.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    dialog.dismiss();
                }
            });
            dialog.show();
        } else {
            String cap_limt = retriveUserData.substring(retriveUserData.indexOf("#21A#") + 5, retriveUserData.indexOf("#22A#"));

            dialog = new Dialog(HomeActivity.this);
            dialog.setContentView(R.layout.layout_caplimit_dialog);
            dialog.getWindow().setLayout(900, 550);
            dialog.setCancelable(true);
            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

            TextView tv_caplimit = dialog.findViewById(R.id.tv_caplimit_msg);
            tv_caplimit.setText("Your Capping Limit is " + cap_limt);
            Button btn_ok = dialog.findViewById(R.id.btn_caplimit_ok);

            btn_ok.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    dialog.dismiss();
                }
            });
            dialog.show();
        }
    }

    public void showKycPopup() {
        AlertDialog.Builder adb = new AlertDialog.Builder(this);
        adb.setTitle(R.string.dialog_title);
        adb.setMessage("Please complete your KYC.");

        adb.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                Intent intent = new Intent(HomeActivity.this, OurProfileActivity.class);
                intent.putExtra("id", dbId);
                startActivity(intent);
            }
        });
        adb.setNegativeButton("Cancel", null);
        adb.setCancelable(false);
        adb.setIcon(R.drawable.app_logo);
        adb.show();
    }

    private void initNavigationMenu() {
        NavigationView nav_view = (NavigationView) findViewById(R.id.nav_view);
        drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        final NavigationView navigationView = nav_view;
        ActionBarDrawerToggle r1 = new ActionBarDrawerToggle(this, this.drawer, this.toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close) {
            public void onDrawerOpened(View drawerView) {
                HomeActivity.this.updateCounter(navigationView);
                super.onDrawerOpened(drawerView);
            }
        };
        drawer.setDrawerListener(r1);
        r1.syncState();
        nav_view.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            public boolean onNavigationItemSelected(MenuItem item) {
                HomeActivity.this.onItemNavigationClicked(item);
                return true;
            }
        });
        drawer.openDrawer((int) GravityCompat.START);
        updateCounter(nav_view);
        menu_navigation = nav_view.getMenu();

        navigation_header = nav_view.getHeaderView(0);

        TextView name = (TextView) navigation_header.findViewById(R.id.username);
        TextView email = (TextView) navigation_header.findViewById(R.id.useremail);
        CircleImageView circleImageView = (CircleImageView) navigation_header.findViewById(R.id.shop_logo);

        name.setText(dbName);
        email.setText(dbId);
        circleImageView.setImageResource(R.drawable.app_logo);
    }

    public void updateCounter(NavigationView nav) {
        if (!is_account_mode) {
            Menu menu = nav.getMenu();
        }
    }

    public void onItemNavigationClicked(MenuItem item) {

        switch (item.getItemId()) {
            case R.id.menu_home:
                startActivity(new Intent(getApplicationContext(), HomeActivity.class));
                break;

            case R.id.menu_display_team:
                startActivity(new Intent(getApplicationContext(), DisplayTeam.class));
                break;

            case R.id.menu_plan:
                startActivity(new Intent(getApplicationContext(), ShowPlanActivity.class));
                break;

            case R.id.menu_signup:
                startActivity(new Intent(getApplicationContext(), SignUpActivity.class));
                break;
            case R.id.menu_help:
                AlertDialog.Builder adbDialHelp = new AlertDialog.Builder(HomeActivity.this);
                adbDialHelp.setTitle("KTCLife help");
                adbDialHelp.setMessage("call helpline number if any query....");
                adbDialHelp.setIcon(R.drawable.app_logo);
                adbDialHelp.setPositiveButton("CALL", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Intent dialIntent = new Intent(Intent.ACTION_DIAL);
                        dialIntent.setData(Uri.parse("tel:+919594763911"));
                        startActivity(dialIntent);
                    }
                });
                adbDialHelp.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                    }
                });
                adbDialHelp.show();
                break;

            case R.id.menu_shareApp:
                AlertDialog.Builder adbShareApp = new AlertDialog.Builder(HomeActivity.this);
                adbShareApp.setTitle("Share KTCLife");
                adbShareApp.setMessage("Share KTCLife app with friends and family.");
                adbShareApp.setIcon(R.drawable.app_logo);
                adbShareApp.setPositiveButton("Share", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Intent shareIntent = new Intent(Intent.ACTION_SEND);
                        shareIntent.setType("text/plain");
                        shareIntent.putExtra(Intent.EXTRA_SUBJECT, "KTCLife");
                        String shareMessage = "Join 'KTCLife': a unique business plan. Refer friend and family and earn from 300 to 30000 upon there payment.\n ";
                        shareMessage = shareMessage + "https://play.google.com/store/apps/details?id=" + BuildConfig.APPLICATION_ID + "  \n\n";
                        shareIntent.putExtra(Intent.EXTRA_TEXT, shareMessage);
                        startActivity(Intent.createChooser(shareIntent, "choose one"));
                    }
                });
                adbShareApp.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                    }
                });
                adbShareApp.show();
                break;

            case R.id.menu_logout:
                AlertDialog.Builder adb = new AlertDialog.Builder(this);
                adb.setTitle(R.string.dialog_title);
                adb.setIcon(R.drawable.app_logo);
                adb.setMessage("Do you want to logout now ?");
                adb.setCancelable(false);
                adb.setNegativeButton("No", null);
                adb.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        SharedPreferences sp = getSharedPreferences("" + R.string.sp_name, MODE_PRIVATE);
                        SharedPreferences.Editor edit = sp.edit();
                        edit.clear();
                        edit.commit();
                        SharedPreferences sp_retrivedata = getSharedPreferences("" + R.string.sp_retriveUserData, MODE_PRIVATE);
                        SharedPreferences.Editor edit_retrivedata = sp_retrivedata.edit();
                        edit_retrivedata.clear();
                        edit_retrivedata.commit();
                        Toast.makeText(HomeActivity.this, "Logout Successfully.", Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(getApplicationContext(), MainActivity.class));
                        finish();
                    }
                });
                adb.show();
                break;
        }
        drawer.closeDrawers();
        return;
    }

    @Override
    public void onBackPressed() {
        if (drawer.isDrawerVisible((int) GravityCompat.START)) {
            drawer.closeDrawer((int) GravityCompat.START);
            navView.getMenu().getItem(0).setChecked(true);
            return;
        }

        navView.getMenu().getItem(0).setChecked(true);

        dialog = new Dialog(HomeActivity.this);
        dialog.setContentView(R.layout.layout_dialog_msg_yes_no);
        dialog.getWindow().setLayout(900, 550);
        dialog.setCancelable(true);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        TextView tv_msg = dialog.findViewById(R.id.tv_msg);
        tv_msg.setText("Do you want to exit now ?");
        Button btn_yes = dialog.findViewById(R.id.btn_yes);
        Button btn_no = dialog.findViewById(R.id.btn_no);

        btn_yes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(HomeActivity.this, SplashScreenActivity.class);
                intent.putExtra("exit_code", true);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
            }
        });

        btn_no.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });
        dialog.show();
    }
}