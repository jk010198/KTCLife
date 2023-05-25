package com.ithublive.ktclife;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.app.Service;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.ColorDrawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.provider.Settings;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.ithublive.ktclife.Utils.BaseUrl;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketAddress;
import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity {

    EditText editText_id, editText_password;
    String id, password;
    SharedPreferences sp;
    Dialog dialog;
    boolean isNetConnected;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        editText_id = findViewById(R.id.et_signin_id);
        editText_password = findViewById(R.id.et_signin_password);
        dialog = new Dialog(MainActivity.this);
        dialog.setContentView(R.layout.layout_progress_dialog);
        if (dialog.getWindow() != null) {
            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(0));
        }
        dialog.setCancelable(false);

        //// for checking user session //////
        sp = getSharedPreferences("" + R.string.sp_name, MODE_PRIVATE);

        if (sp.contains("username") && sp.contains("password")) {

            startActivity(new Intent(MainActivity.this, HomeActivity.class));
            finish();
        }
    }

    public void methodSignIn(View view) {
        internetChecker internetChecker = new internetChecker();
        internetChecker.execute();

        dialog.show();

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                if (isNetConnected) {

                    id = editText_id.getText().toString();
                    password = editText_password.getText().toString();

                    if (id.isEmpty() || id.length() < 9) {
                        editText_id.setError("Please enter id.");
                        dialog.dismiss();
                    } else if (password.isEmpty() || password.length() > 12) {
                        editText_password.setError("Please enter password.");
                        dialog.dismiss();
                    } else {
                        // Internet Available
                        StringRequest stringRequest = new StringRequest(Request.Method.POST, BaseUrl.signInUrl, new Response.Listener<String>() {
                            @Override
                            public void onResponse(String response) {

                                if (response.contains("connection not got")) {
                                    dialog.dismiss();
                                    Toast.makeText(MainActivity.this, "Server error.", Toast.LENGTH_SHORT).show();
                                }

                                if (response.contains("user found")) {
                                    dialog.dismiss();
                                    SharedPreferences.Editor edit = sp.edit();
                                    edit.putString("username", id);
                                    edit.putString("password", password);
                                    edit.commit();
                                    startActivity(new Intent(getApplicationContext(), HomeActivity.class));
                                    Toast.makeText(MainActivity.this, "Signin Successful...!", Toast.LENGTH_SHORT).show();
                                }

                                if (response.contains("user not found")) {
                                    dialog.dismiss();
                                    Toast.makeText(MainActivity.this, "Signin Unsuccessful...!", Toast.LENGTH_SHORT).show();
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
                                map.put("id", id);
                                map.put("password", password);
                                return map;
                            }
                        };
                        Volley.newRequestQueue(MainActivity.this).add(stringRequest);
                    }
                } else {
                    dialog.dismiss();
                }
            }
        }, 2000);
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
                Intent intent = new Intent(getApplicationContext(), SplashScreenActivity.class);
                intent.putExtra("exit_code", true);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                finish();
            }
        });
        adb.show();
    }

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
                    Toast.makeText(MainActivity.this, "Internet is not available.", Toast.LENGTH_SHORT).show();
                }
            } else {
                isNetConnected = false;
                noInternetDialog();
            }
            super.onPostExecute(result);
        }
    }

    public void noInternetDialog() {

        AlertDialog.Builder adb = new AlertDialog.Builder(this);
        adb.setTitle("Please connect to the internet to proceed furture");
        adb.setCancelable(false);
        adb.setIcon(R.drawable.app_logo);
        adb.setPositiveButton("Connect", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                startActivity(new Intent(Settings.ACTION_WIFI_SETTINGS));
            }
        });
        adb.setNegativeButton("Cancel", null);
        adb.show();
    }
}