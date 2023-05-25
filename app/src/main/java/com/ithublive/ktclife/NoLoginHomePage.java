package com.ithublive.ktclife;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import de.hdodenhof.circleimageview.CircleImageView;

public class NoLoginHomePage extends AppCompatActivity {

    CircleImageView tv_loginurl;
    TextView tv_newsignup, tv_leveltarget, tv_new_id_one_time_bonus, tv_customerBonus, tv_show_plan, tv_income_plan, tv_login, tv_paymentmode, tv_achievers,
            tv_electricitybills, tv_mobilerecharge;
    ImageButton ib_newsignup, ib_leveltarget, ib_new_id_one_time_bonus, ib_monthlytarget, ib_showplan, ib_incomeplan, ib_login, ib_paymentmode, ib_help,
            ib_share, ib_achievers, ib_electricitybills, ib_mobilerecharge;
    static boolean isNoLoginHomePage = false;
    long animationTime = 400;
    Animation anim;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_no_login_home_page);

        anim = new AlphaAnimation(0.0f, 1.0f);
        anim.setDuration(50); //You can manage the blinking time with this parameter
        anim.setStartOffset(20);
        anim.setRepeatMode(Animation.REVERSE);
        anim.setRepeatCount(Animation.INFINITE);
        ib_newsignup = findViewById(R.id.nologinhomepage_imagebtn_new_signup);
        ib_leveltarget = findViewById(R.id.nologinhomepage_imagebtn_levelTarget);
        ib_new_id_one_time_bonus = findViewById(R.id.nologinhomepage_imagebtn_newIDOneTimeBonusPlan);
        ib_monthlytarget = findViewById(R.id.nologinhomepage_imagebtn_MT);
        ib_showplan = findViewById(R.id.nologinhomepage_imagebtn_ShowPlan);
        ib_incomeplan = findViewById(R.id.nologinhomepage_imagebtn_IncomePlan);
        ib_login = findViewById(R.id.nologinhomepage_imagebtn_login);
        ib_paymentmode = findViewById(R.id.nologinhomepage_imagebtn_PayMode);
        ib_help = findViewById(R.id.nologinhomepage_imagebtn_help);
        ib_share = findViewById(R.id.nologinhomepage_imagebtn_share);
        ib_achievers = findViewById(R.id.nologinhomepage_imagebtn_Achievers);
        ib_electricitybills = findViewById(R.id.nologinhomepage_imagebtn_Electricitybill);
        ib_mobilerecharge = findViewById(R.id.nologinhomepage_imagebtn_Mobilerecharge);

        tv_newsignup = findViewById(R.id.tv_nologinhomepage_new_signup);
        tv_leveltarget = findViewById(R.id.tv_nologinhomepage_levelTarget);
        tv_new_id_one_time_bonus = findViewById(R.id.tv_nologinhomepage_newIDOneTimeBonusPlan);
        tv_customerBonus = findViewById(R.id.tv_nologinhomepage_CustomerBonus);
        tv_show_plan = findViewById(R.id.tv_nologinhomepage_ShowPlan);
        tv_income_plan = findViewById(R.id.tv_nologinhomepage_IncomePlan);
        tv_login = findViewById(R.id.tv_nologinhomepage_login);
        tv_paymentmode = findViewById(R.id.tv_nologinhomepage_PayMode);
        tv_loginurl = findViewById(R.id.tv_nologinhomepage_loginlink);
        tv_achievers = findViewById(R.id.tv_nologinhomepage_Achievers);
        tv_electricitybills = findViewById(R.id.tv_nologinhomepage_Electricitybill);
        tv_mobilerecharge = findViewById(R.id.tv_nologinhomepage_Mobilerecharge);
        ib_newsignup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                isNoLoginHomePage = true;
                ib_newsignup.startAnimation(anim);
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        ib_newsignup.clearAnimation();
                        startActivity(new Intent(getApplicationContext(), SignUpActivity.class));
                    }
                }, animationTime);
            }
        });

        ib_leveltarget.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ib_leveltarget.startAnimation(anim);
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        ib_leveltarget.clearAnimation();
                        startActivity(new Intent(getApplicationContext(), LevelTargetActivity.class));
                    }
                }, animationTime);
            }
        });

        ib_new_id_one_time_bonus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ib_new_id_one_time_bonus.startAnimation(anim);
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        ib_new_id_one_time_bonus.clearAnimation();
                        startActivity(new Intent(getApplicationContext(), ShowNewIDOneTimeBonusActivity.class));
                    }
                }, animationTime);
            }
        });

        ib_monthlytarget.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ib_monthlytarget.startAnimation(anim);
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        ib_monthlytarget.clearAnimation();
                        startActivity(new Intent(getApplicationContext(), ShowMonthlyTargetActivity.class));
                    }
                }, animationTime);
            }
        });

        ib_showplan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ib_showplan.startAnimation(anim);
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        ib_showplan.clearAnimation();
                        startActivity(new Intent(getApplicationContext(), ShowPlanActivity.class));
                    }
                }, animationTime);
            }
        });

        ib_incomeplan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ib_incomeplan.startAnimation(anim);
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        ib_incomeplan.clearAnimation();
                        startActivity(new Intent(getApplicationContext(), ShowIncomePlanActivity.class));
                    }
                }, animationTime);
            }
        });

        ib_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ib_login.startAnimation(anim);
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        ib_login.clearAnimation();
                        startActivity(new Intent(getApplicationContext(), MainActivity.class));
                    }
                }, animationTime);

            }
        });

        ib_paymentmode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ib_paymentmode.startAnimation(anim);
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        ib_paymentmode.clearAnimation();
                        startActivity(new Intent(getApplicationContext(), PaymentModesActivity.class));
                    }
                }, animationTime);
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
                        //startActivity(new Intent(getApplicationContext(), SignUpActivity.class));
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
                        //startActivity(new Intent(getApplicationContext(), SignUpActivity.class));
                    }
                }, animationTime);
            }
        });

        ib_help.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder adbDialHelp = new AlertDialog.Builder(NoLoginHomePage.this);
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
            }
        });

        ib_share.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder adbShareApp = new AlertDialog.Builder(NoLoginHomePage.this);
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
        tv_newsignup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                isNoLoginHomePage = true;
                startActivity(new Intent(getApplicationContext(), SignUpActivity.class));
            }
        });

        tv_leveltarget.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(), LevelTargetActivity.class));
            }
        });

        tv_customerBonus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(), ShowMonthlyTargetActivity.class));
            }
        });
        tv_new_id_one_time_bonus.setOnClickListener(new View.OnClickListener() {
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
        tv_show_plan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(), ShowPlanActivity.class));
                //startActivity(new Intent(getApplicationContext(), PlansActivity.class));
            }
        });

        tv_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(), MainActivity.class));
            }
        });

        tv_paymentmode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(), PaymentModesActivity.class));
            }
        });

        tv_loginurl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(), MainActivity.class));
            }
        });
    }

    @Override
    public void onBackPressed() {

        AlertDialog.Builder adb = new AlertDialog.Builder(this);
        adb.setTitle(R.string.dialog_title);
        adb.setMessage("Do you want to exit now ?");
        adb.setIcon(R.drawable.app_logo);
        adb.setNegativeButton("NO", null);
        adb.setPositiveButton("YES", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                Intent intent = new Intent(NoLoginHomePage.this, SplashScreenActivity.class);
                intent.putExtra("exit_code", true);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
            }
        });
        adb.show();
    }
}