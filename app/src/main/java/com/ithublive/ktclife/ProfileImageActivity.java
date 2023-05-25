package com.ithublive.ktclife;

import android.Manifest;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.RetryPolicy;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.ithublive.ktclife.Utils.BaseUrl;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;

public class ProfileImageActivity extends AppCompatActivity {

    CircleImageView add_image, user_profileImg;
    private static final int REQUEST_PERMISSIONS = 100;
    private Bitmap bitmap;
    Dialog dialog;
    private String filePath = "";
    private static final int PICK_IMAGE_REQUEST = 1;
    String imagename;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_image);

        user_profileImg = findViewById(R.id.user_image);
        add_image = findViewById(R.id.add_image_button);

        dialog = new Dialog(ProfileImageActivity.this);
        dialog.setContentView(R.layout.layout_progress_dialog);
        if (dialog.getWindow() != null) {
            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(0));
        }
        dialog.setCancelable(false);

        imagename = getIntent().getStringExtra("userId");

        add_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if ((ContextCompat.checkSelfPermission(getApplicationContext(),
                        Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) &&
                        (ContextCompat.checkSelfPermission(getApplicationContext(),
                                Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED)) {
                    if ((ActivityCompat.shouldShowRequestPermissionRationale(ProfileImageActivity.this,
                            Manifest.permission.WRITE_EXTERNAL_STORAGE)) && (ActivityCompat.shouldShowRequestPermissionRationale(ProfileImageActivity.this,
                            Manifest.permission.READ_EXTERNAL_STORAGE))) {

                        ActivityCompat.requestPermissions(ProfileImageActivity.this,
                                new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE},
                                REQUEST_PERMISSIONS);
                    } else {
                        ActivityCompat.requestPermissions(ProfileImageActivity.this,
                                new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE},
                                REQUEST_PERMISSIONS);
                    }
                } else {
                    bringImagePicker();
                }
            }
        });
    }

    public void methodSubmitProfile(View view) {
        dialog.show();

        if (filePath.isEmpty()) {
            dialog.dismiss();
            Toast.makeText(this, "Please select profile image.", Toast.LENGTH_SHORT).show();
        } else {
            if (!(filePath.contains("empty"))) {
                VolleyMultipartRequest volleyMultipartRequest = new VolleyMultipartRequest(Request.Method.POST, BaseUrl.imageUploadUrl,
                        new Response.Listener<NetworkResponse>() {
                            @Override
                            public void onResponse(NetworkResponse response) {
                                try {
                                    JSONObject obj = new JSONObject(new String(response.data));
                                    String res = obj.toString();
                                    if (res.contains("File uploaded successfully!")) {
                                        dialog.dismiss();
                                      //  Log.d("res_mlm", "res: " + "dismiss");
                                        //imageUrlSetDatabase();
                                    }
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }
                        },
                        new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {
                                dialog.dismiss();
                                Toast.makeText(ProfileImageActivity.this, "Server Error.", Toast.LENGTH_SHORT).show();
                                Log.d("res_mlm", "Error: " + error.toString());
                            }
                        }) {
                    @Override
                    protected Map<String, DataPart> getByteData() {
                        Map<String, DataPart> params = new HashMap<>();
                        params.put("fileToUpload", new DataPart(imagename + ".jpg", getFileDataFromDrawable(bitmap)));
                        return params;
                    }
                };
                volleyMultipartRequest.setRetryPolicy(new RetryPolicy() {
                    @Override
                    public int getCurrentTimeout() {
                        return 5000;
                    }

                    @Override
                    public int getCurrentRetryCount() {
                        return 5000;
                    }

                    @Override
                    public void retry(VolleyError error) throws VolleyError {
                    }
                });
                //adding the request to volley
                Volley.newRequestQueue(this).add(volleyMultipartRequest);
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        imageUrlSetDatabase();
                    }
                }, 5000);
            } else {
                Toast.makeText(this, "Something went wrong, please try again.", Toast.LENGTH_SHORT).show();
            }
        }
    }

    public void imageUrlSetDatabase() {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, BaseUrl.setProfileImgUrl, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                if (response.contains("update_url")) {
                    dialog.dismiss();
                    Toast.makeText(ProfileImageActivity.this, "Profile Added Successfully.", Toast.LENGTH_LONG).show();
                    showPopup();
                } else {
                    dialog.dismiss();
                    Toast.makeText(ProfileImageActivity.this, "Profile is not uploaded.", Toast.LENGTH_SHORT).show();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(ProfileImageActivity.this, "Server error.", Toast.LENGTH_LONG).show();
                dialog.dismiss();
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> map = new HashMap<>();
                map.put("id", imagename);
                map.put("profile_imgurl", BaseUrl.dbProfileImageUrl + imagename + ".jpg");
                return map;
            }
        };

        Volley.newRequestQueue(ProfileImageActivity.this).add(stringRequest);
    }

    public void showPopup() {
        final AlertDialog.Builder adb = new AlertDialog.Builder(this);
        adb.setTitle(R.string.dialog_title);
        adb.setMessage("Profile uploaded successfully.");
        adb.setIcon(R.drawable.app_logo);
        adb.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
                startActivity(new Intent(getApplicationContext(), MainActivity.class));
            }
        });
        adb.setCancelable(false);
        adb.show();
    }

    public void bringImagePicker() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE_REQUEST);
    }

    public byte[] getFileDataFromDrawable(Bitmap bitmap) {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        //  bitmap.compress(Bitmap.CompressFormat.PNG, 80, byteArrayOutputStream);
        bitmap.compress(Bitmap.CompressFormat.JPEG, 70, byteArrayOutputStream); //80
        return byteArrayOutputStream.toByteArray();
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
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {
            Uri picUri = data.getData();
            filePath = getPath(picUri);
            if (filePath != null) {
                try {
                    Log.d("ImageUpD", "FilePath: " + String.valueOf(filePath));
                    bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), picUri);
                    user_profileImg.setImageBitmap(bitmap);

                } catch (IOException e) {
                    e.printStackTrace();
                }
            } else {
                Toast.makeText(ProfileImageActivity.this, "no image selected", Toast.LENGTH_LONG).show();
            }
        }
    }

    public void methodUploadLaterProfile(View view) {
        startActivity(new Intent(this, MainActivity.class));
    }
}