package com.ithublive.ktclife;

import android.app.Service;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.AuthFailureError;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.snackbar.Snackbar;
import com.ithublive.ktclife.Utils.BaseUrl;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketAddress;
import java.util.Map;

public class SplashScreenActivity extends AppCompatActivity {

    int internetNotAvailReloadCounter;
    AlertDialog.Builder adbLockDown;
    // MONTHLY TARGET IMAGES HARDCODED.....
    // SHow income plan Image HardCoded...
    int myAppVersion = 23;//(16-17 Minor changes )15to16 - New server transfer)(14-15 Achievers Images and Monthly Target 2 photos) 13-14(https)12 to 13(30 days payment gap)// 11-12(admin changes)//10-11(silver_gold color error//9 to 10 (display silver or gold.//8-9(29-12-2020(minor update mistake) // 7 to 8 (29-12-2020)(new table system)//5_nov(5to6)//25-sep (4 to5 )// 19 aug (3 to 4). 10_aug_20 (2 to 3) minor changes+ addition.
    ProgressBar progressBar;
    String appClose, appCloseMsg, appCloseMsgBtn1, appCloseMsgBtn2, notifUser, notifTitle, notifMsg, placesses;
    SharedPreferences sp;
    ImageView img_splash;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);

        img_splash = findViewById(R.id.img_splash56);
        Animation animation = AnimationUtils.loadAnimation(this, R.anim.anim_splash_logo);
        img_splash.startAnimation(animation);
        progressBar = findViewById(R.id.progressBarsplashscreen);
        progressBar.setVisibility(View.VISIBLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

        sp = getSharedPreferences("" + R.string.sp_name, MODE_PRIVATE);

        Intent in = getIntent();
        internetNotAvailReloadCounter = in.getIntExtra("reloadCounter", 1);
        boolean exitcode = in.getBooleanExtra("exit_code", false);
        if (exitcode) {
            finish();
            System.exit(0);
        }
        NetCheckTask task = new NetCheckTask();
        task.execute();
    }

    public boolean isConnected() {
        ConnectivityManager connectivityManager = (ConnectivityManager) getApplicationContext().
                getSystemService(Service.CONNECTIVITY_SERVICE);

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

    class NetCheckTask extends AsyncTask<String, Void, Integer> {

        @Override
        protected Integer doInBackground(String... params) {

            Integer result = 0;
            try {
                Socket socket = new Socket();
                SocketAddress socketAddress = new InetSocketAddress("8.8.8.8", 53);
                socket.connect(socketAddress, 5800);
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
                    /////////Checking For Latest Version///////
                    StringRequest requestToCheckVersion = new StringRequest(StringRequest.Method.POST, BaseUrl.versionCheckUrl,

                            new Response.Listener<String>() {
                                @Override
                                public void onResponse(String response) {
                                    try {
                                        //////////////////////
                                        int serverAppVersion = Integer.parseInt(response.substring(response.indexOf("*A0*") + 4, response.indexOf("*A1*")));
                                        appClose = response.substring(response.indexOf("*A1*") + 4, response.indexOf("*A2*"));
                                        appCloseMsg = response.substring(response.indexOf("*A2*") + 4, response.indexOf("*A3*"));
                                        appCloseMsgBtn1 = response.substring(response.indexOf("*A3*") + 4, response.indexOf("*A4*"));
                                        appCloseMsgBtn2 = response.substring(response.indexOf("*A4*") + 4, response.indexOf("*A5*"));
                                        notifUser = response.substring(response.indexOf("*A5*") + 4, response.indexOf("*A6*"));
                                        notifTitle = response.substring(response.indexOf("*A6*") + 4, response.indexOf("#A7#"));
                                        notifMsg = response.substring(response.indexOf("#A7#") + 4, response.indexOf("#A8#"));
                                        BaseUrl.monthlyTargetImgUrlSring1 = response.substring(response.indexOf("#A8#") + 4, response.indexOf("#A9#"));
                                        //  BaseUrl.monthlyTargetImgUrlSring2 = response.substring(response.indexOf("#A81#") + 4, response.indexOf("#A91#"));

                                        ////////////////////
                                        // Toast.makeText(SplashScreen.this, "Response: " + response, Toast.LENGTH_SHORT).show();
                                        // Toast.makeText(SplashScreen.this, "NotifMsg: " + notifMsg, Toast.LENGTH_SHORT).show();

                                        // serverAppVersion = Integer.parseInt(response.trim());
                                        if (myAppVersion >= serverAppVersion) {
                                            new Handler().postDelayed(new Runnable() {
                                                @Override
                                                public void run() {
                                                    if (appClose.equals("no")) {
                                                        if (sp.contains("username") && sp.contains("password")) {
                                                            startActivity(new Intent(SplashScreenActivity.this, HomeActivity.class));
                                                            overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
                                                            finish();
                                                        } else {
                                                            startActivity(new Intent(SplashScreenActivity.this, NoLoginHomePage.class));
                                                            overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
                                                        }
                                                    } else if (appClose.equals("yes")) {
                                                        adbLockDown = new AlertDialog.Builder(SplashScreenActivity.this);
                                                        adbLockDown.setTitle("KTC Life.");
                                                        // adbLockDown.setMessage("WE HAVE CLOSED OUR SERVICES TILL LOCK-DOWN. \n\nSORRY FOR THE INCONVENIENCE.");
                                                        adbLockDown.setMessage(appCloseMsg);
                                                        //adbLockDown.setPositiveButtonIcon()
                                                        adbLockDown.setIcon(R.drawable.app_logo);
                                                        adbLockDown.setCancelable(false);
                                                        adbLockDown.setPositiveButton(appCloseMsgBtn1, new DialogInterface.OnClickListener() {
                                                            @Override
                                                            public void onClick(DialogInterface dialogInterface, int i) {
                                                                onBackPressed();
                                                            }
                                                        });
                                                        adbLockDown.show();

                                                    }
                                                }
                                            }, 800);
                                        } else {
                                            AlertDialog.Builder adb = new AlertDialog.Builder(SplashScreenActivity.this);
                                            adb.setTitle("Update KTC Life.");
                                            adb.setIcon(R.drawable.app_logo);
                                            adb.setMessage("The new version of KTCLife App is available now. To use KTCLife please update the app from PlayStore. \n\nThank you.");
                                            adb.setPositiveButton("Update App", new DialogInterface.OnClickListener() {
                                                @Override
                                                public void onClick(DialogInterface dialog, int which) {
                                                    Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id="
                                                            + BuildConfig.APPLICATION_ID));
                                                    startActivity(intent);
                                                }
                                            });
                                            adb.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                                                @Override
                                                public void onClick(DialogInterface dialogInterface, int i) {
                                                    finish();
                                                }
                                            });
                                            adb.show();
                                        }
                                    } catch (Exception ex) {
                                        Toast.makeText(SplashScreenActivity.this, "Error...", Toast.LENGTH_SHORT).show();
                                    }
                                }
                            }, new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            Log.d("KTCLIFE_VOLLY", "ERROR:" + error.getMessage());
                            Toast.makeText(SplashScreenActivity.this, "Server Response Error.", Toast.LENGTH_SHORT).show();
                        }
                    }) {
                        @Override
                        protected Map<String, String> getParams() throws AuthFailureError {
                            return null;//super.getParams();
                        }
                    };
                    Volley.newRequestQueue(SplashScreenActivity.this).add(requestToCheckVersion);
                    /////////////////////
                }

                if (result == 0) {
                    progressBar.setVisibility(View.INVISIBLE);
                    Snackbar snackbar = Snackbar.make(findViewById(android.R.id.content), "No Internet Connection...", Snackbar.LENGTH_INDEFINITE);
                    snackbar.setAction("Reload.", new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            finish();
                            startActivity(getIntent());
                        }
                    });
                    snackbar.setActionTextColor(getResources().getColor(R.color.red));
                    snackbar.show();
                }
            } else {
                progressBar.setVisibility(View.INVISIBLE);
                if (internetNotAvailReloadCounter >= 3) {
                    Snackbar snackbar = Snackbar.make(findViewById(android.R.id.content), "No Internet Connection available...", Snackbar.LENGTH_INDEFINITE);

                    snackbar.setAction("Reload.", new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            finish();
                            startActivity(getIntent());
                        }
                    });
                    snackbar.setActionTextColor(getResources().getColor(R.color.green));
                    snackbar.show();
                } else {
                    finish();
                    Intent intent = getIntent();
                    intent.putExtra("reloadCounter", (internetNotAvailReloadCounter + 1));
                    startActivity(getIntent());
                }
            }
            super.onPostExecute(result);
        }
    }

    @Override
    public void onBackPressed() {
        finish();
        System.exit(0);
    }
}
// glide image change...
// new menu designs on Home Page.
// menu to show app version.