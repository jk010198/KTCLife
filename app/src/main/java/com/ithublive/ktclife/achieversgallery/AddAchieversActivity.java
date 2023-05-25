package com.ithublive.ktclife.achieversgallery;

import android.Manifest;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.app.Service;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.drawable.ColorDrawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.provider.MediaStore;
import android.provider.Settings;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkError;
import com.android.volley.NetworkResponse;
import com.android.volley.NoConnectionError;
import com.android.volley.ParseError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.ServerError;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.Volley;
import com.ithublive.ktclife.OurProfileActivity;
import com.ithublive.ktclife.R;
import com.ithublive.ktclife.Utils.BaseUrl;
import com.ithublive.ktclife.VolleyMultipartRequest;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketAddress;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import de.hdodenhof.circleimageview.CircleImageView;

public class AddAchieversActivity extends AppCompatActivity {

    CircleImageView profileImg, add_profileimg;
    private static final int REQUEST_PERMISSIONS = 100;
    private static final int PICK_IMAGE_REQUEST = 1;
    private Bitmap bitmap;
    private String filePath = "empty";
    boolean isNetConnected;
    Dialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_achievers);

        profileImg = findViewById(R.id.imageview_achiever7293);
        add_profileimg = findViewById(R.id.add_achiever_button5382);
        dialog = new Dialog(AddAchieversActivity.this);
        dialog.setContentView(R.layout.layout_progress_dialog);
        if (dialog.getWindow() != null) {
            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(0));
        }
        dialog.setCancelable(false);

        add_profileimg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if ((ContextCompat.checkSelfPermission(getApplicationContext(),
                        Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) && (ContextCompat.checkSelfPermission(getApplicationContext(),
                        Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED)) {
                    if ((ActivityCompat.shouldShowRequestPermissionRationale(AddAchieversActivity.this,
                            Manifest.permission.WRITE_EXTERNAL_STORAGE)) && (ActivityCompat.shouldShowRequestPermissionRationale(AddAchieversActivity.this,
                            Manifest.permission.READ_EXTERNAL_STORAGE))) {
                        ActivityCompat.requestPermissions(AddAchieversActivity.this,
                                new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE},
                                REQUEST_PERMISSIONS);
                    } else {
                        ActivityCompat.requestPermissions(AddAchieversActivity.this,
                                new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE},
                                REQUEST_PERMISSIONS);
                    }
                } else {
                    bringImagePicker();
                }
            }
        });
    }

    public void bringImagePicker() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE_REQUEST);
    }

    public void methodAddPhoto(View view) {
        internetChecker internetChecker = new internetChecker();
        internetChecker.execute();
        dialog.show();
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                if (isNetConnected) {
                    if (!(filePath.contains("empty"))) {
                        profileImgUploder();
                    } else {
                        dialog.dismiss();
                        Toast.makeText(AddAchieversActivity.this, "file empty", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    dialog.dismiss();
                    Toast.makeText(AddAchieversActivity.this, "Internet connection is not available...", Toast.LENGTH_SHORT).show();
                }
            }
        }, 2000);
    }

    public void profileImgUploder() {
        VolleyMultipartRequest volleyMultipartRequest = new VolleyMultipartRequest(Request.Method.POST, BaseUrl.achievers_imageUploadUrl,
                new Response.Listener<NetworkResponse>() {
                    @Override
                    public void onResponse(NetworkResponse response) {
                        try {
                            JSONObject obj = new JSONObject(new String(response.data));
                            String res = obj.toString();
                            dialog.dismiss();
                            if (res.contains("File uploaded successfully!")) {
                                Toast.makeText(AddAchieversActivity.this, "Uploaded...!", Toast.LENGTH_SHORT).show();
                                onBackPressed();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        dialog.dismiss();
                        onBackPressed();
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        dialog.dismiss();
                        String message = null;
                    }
                }) {
            @Override
            protected Map<String, DataPart> getByteData() {
                Map<String, DataPart> params = new HashMap<>();
                Random random = new Random();
                params.put("fileToUpload", new DataPart(
                        BaseUrl.dbImageUrl + random.nextInt(2009) + random.nextInt(4000) + ".jpg",
                        getFileDataFromDrawable(bitmap)));
                return params;
            }
        };
        Volley.newRequestQueue(AddAchieversActivity.this).add(volleyMultipartRequest);
    }

    public byte[] getFileDataFromDrawable(Bitmap bitmap) {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 70, byteArrayOutputStream); //80
        return byteArrayOutputStream.toByteArray();
    }

    // internet checking code start
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
                    Toast.makeText(AddAchieversActivity.this, "Internet is not available.", Toast.LENGTH_SHORT).show();
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
        adb.setTitle("Please connect to the internet to proceed furture.");
        adb.setIcon(R.drawable.app_logo);
        adb.setCancelable(false);
        adb.setPositiveButton("Connect", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                startActivity(new Intent(Settings.ACTION_WIFI_SETTINGS));
            }
        });
        adb.setNegativeButton("Cancel", null);
        adb.show();
    }

    public String getPath(Uri uri) {
        Cursor cursor = getContentResolver().query(uri, null, null, null, null);
        cursor.moveToFirst();
        String document_id = cursor.getString(0);
        document_id = document_id.substring(document_id.lastIndexOf(":") + 1);
        cursor.close();

        cursor = getContentResolver().query(
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                null, MediaStore.Images.Media._ID + " = ? ", new String[]{document_id}, null);
        cursor.moveToFirst();
        String path = cursor.getString(cursor.getColumnIndex(MediaStore.Images.Media.DATA));
        cursor.close();
        return path;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {
            Uri picUri = data.getData();
            filePath = getPath(picUri);
            if (filePath != null) {
                try {
                    bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), picUri);
                    profileImg.setImageBitmap(bitmap);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            } else {
                Toast.makeText(this, "no img selected.", Toast.LENGTH_SHORT).show();
            }
        }
    }
}


/*public void imgUploder() {
        VolleyMultipartRequest volleyMultipartRequest = new VolleyMultipartRequest(Request.Method.POST, BaseUrl.achievers_imageUploadUrl,
                new Response.Listener<NetworkResponse>() {
                    @Override
                    public void onResponse(NetworkResponse response) {
                        try {
                            JSONObject obj = new JSONObject(new String(response.data));
                            String res = obj.toString();
                            progressDialog.dismiss();
                            if (res.contains("File uploaded successfully!")) {

                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        progressDialog.dismiss();
                        String message = null;
                        if (error instanceof NetworkError) {
                            message = "Cannot connect to Internet...Please check your connection!";
                            Toast.makeText(AddAchieversActivity.this, message, Toast.LENGTH_SHORT).show();
                        } else if (error instanceof ServerError) {
                            message = "The server could not be found. Please try again after some time!!";
                            Toast.makeText(AddAchieversActivity.this, message, Toast.LENGTH_SHORT).show();
                        } else if (error instanceof ParseError) {
                            message = "Error! Please try again after some time!!";
                            Toast.makeText(AddAchieversActivity.this, message, Toast.LENGTH_SHORT).show();
                        } else if (error instanceof TimeoutError || error instanceof NoConnectionError) {
                            message = "Either time out or there is no connection! Please try again after some time!!";
                            Toast.makeText(AddAchieversActivity.this, message, Toast.LENGTH_SHORT).show();
                        } else if (error instanceof AuthFailureError) {
                            message = "Error indicating that there was an Authentication Failure while performing the request";
                            Toast.makeText(AddAchieversActivity.this, message, Toast.LENGTH_SHORT).show();
                        }
                    }
                }) {
            @Override
            protected Map<String, DataPart> getByteData() {
                Map<String, DataPart> params = new HashMap<>();
                Random random = new Random();
                params.put("fileToUpload", new DataPart(
                        dbImageUrl + "_" + random.nextInt(2009) + random.nextInt(4000) + random.nextInt(4000) + ".jpg",
                        getFileDataFromDrawable(bitmap)));
                return params;
            }
        };
        Volley.newRequestQueue(AddAchieversActivity.this).add(volleyMultipartRequest);
    }*/