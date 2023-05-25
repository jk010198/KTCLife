package com.ithublive.ktclife;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

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
import com.ithublive.ktclife.Utils.BaseUrl;
import com.ithublive.ktclife.achieversgallery.AchieversPhotoActivity;
import com.ithublive.ktclife.achieversgallery.AddAchieversActivity;
import com.ithublive.ktclife.achieversgallery.GridItemModel;
import com.ithublive.ktclife.achieversgallery.GridViewAdapter;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class ShowAchieversActivity extends AppCompatActivity {

    private static final String TAG = MainActivity.class.getSimpleName();
    private GridView mGridView;
    Dialog dialog;
    private GridViewAdapter mGridAdapter;
    private ArrayList<GridItemModel> mGridData;

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_achievers);

        mGridView = findViewById(R.id.gridView);
        dialog = new Dialog(ShowAchieversActivity.this);
        dialog.setContentView(R.layout.layout_progress_dialog);
        if (dialog.getWindow() != null) {
            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(0));
        }
        dialog.setCancelable(false);

        //Initialize with empty data
        mGridData = new ArrayList<>();

        mGridAdapter = new GridViewAdapter(this, R.layout.grid_item_layout, mGridData);
        mGridView.setAdapter(mGridAdapter);
        //mGridView.setTooltipText("Long Press csn delete images.");

        //Grid view click event
        mGridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View v, int position, long id) {
                //Get item at position
                GridItemModel item = (GridItemModel) parent.getItemAtPosition(position);
                Intent intent = new Intent(ShowAchieversActivity.this, AchieversPhotoActivity.class);
                intent.putExtra("id", item.getId()).
                        putExtra("title", item.getTitle()).
                        putExtra("image", item.getImage());
                startActivity(intent);
            }
        });

        mGridView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int position, long l) {
                GridItemModel item = (GridItemModel) adapterView.getItemAtPosition(position);
                String imgUrl = item.getImage().substring(item.getImage().indexOf("ktc_achievers/") + 14, item.getImage().indexOf(".jpg"));
                //Toast.makeText(ShowAchieversActivity.this, "" + imgUrl, Toast.LENGTH_SHORT).show();
                android.app.AlertDialog.Builder adb = new AlertDialog.Builder(ShowAchieversActivity.this);
                adb.setTitle(R.string.dialog_title);
                adb.setMessage("Do you want to delete this photo...?");
                adb.setIcon(R.drawable.app_logo);
                adb.setNegativeButton("NO", null);
                adb.setPositiveButton("YES", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        deletePhoto(imgUrl);
                    }
                });
                adb.show();
                return true;
            }
        });

        //Start download
        new AsyncHttpTask().execute(BaseUrl.AllAchieversImageUrl);
        dialog.show();
    }

    public void deletePhoto(String imgUrl) {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, BaseUrl.ktc_life_AchieverImageDelete, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                if (response.contains("File_does_not_exists")) {
                    dialog.dismiss();
                    Toast.makeText(ShowAchieversActivity.this, "File not found...!", Toast.LENGTH_SHORT).show();
                }

                if (response.contains("File_Successfully_Delete")) {
                    dialog.dismiss();
                    Toast.makeText(ShowAchieversActivity.this, "File deleted...!", Toast.LENGTH_SHORT).show();
                    mGridData.clear();
                    new AsyncHttpTask().execute(BaseUrl.AllAchieversImageUrl);
                    dialog.show();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                dialog.dismiss();
                String message = null;
                if (error instanceof NetworkError) {
                    message = "Cannot connect to Internet...Please check your connection!";
                    Toast.makeText(ShowAchieversActivity.this, message, Toast.LENGTH_SHORT).show();
                } else if (error instanceof ServerError) {
                    message = "The server could not be found. Please try again after some time!!";
                    Toast.makeText(ShowAchieversActivity.this, message, Toast.LENGTH_SHORT).show();
                } else if (error instanceof ParseError) {
                    message = "Error! Please try again after some time!!";
                    Toast.makeText(ShowAchieversActivity.this, message, Toast.LENGTH_SHORT).show();
                } else if (error instanceof TimeoutError || error instanceof NoConnectionError) {
                    message = "Either time out or there is no connection! Please try again after some time!!";
                    Toast.makeText(ShowAchieversActivity.this, message, Toast.LENGTH_SHORT).show();
                }
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> map = new HashMap<>();
                map.put("imgUrl", imgUrl);
                return map;
            }
        };
        Volley.newRequestQueue(ShowAchieversActivity.this).add(stringRequest);
    }

    //Downloading data asynchronously
    public class AsyncHttpTask extends AsyncTask<String, Void, Integer> {

        @Override
        protected Integer doInBackground(String... params) {
            Integer result = 0;
            try {
                // Create Apache HttpClient
                HttpClient httpclient = new DefaultHttpClient();
                HttpResponse httpResponse = httpclient.execute(new HttpGet(params[0]));
                int statusCode = httpResponse.getStatusLine().getStatusCode();

                // 200 represents HTTP OK
                if (statusCode == 200) {
                    String response = streamToString(httpResponse.getEntity().getContent());
                    parseResult(response);
                    result = 1; // Successful
                } else {
                    result = 0; //"Failed
                }
            } catch (Exception e) {
            }

            return result;
        }

        @Override
        protected void onPostExecute(Integer result) {
            // Download complete. Lets update UI
            if (result == 1) {
                mGridAdapter.setGridData(mGridData);
            } else {
                Toast.makeText(ShowAchieversActivity.this, "Failed to fetch data!", Toast.LENGTH_SHORT).show();
            }
            //Hide progressbar
            dialog.dismiss();
        }
    }

    String streamToString(InputStream stream) throws IOException {
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(stream));
        String line;
        String result = "";
        while ((line = bufferedReader.readLine()) != null) {
            result += line;
        }
        // Close stream
        if (null != stream) {
            stream.close();
        }
        return result;
    }

    private void parseResult(String result) {
        String allImagesPath[] = result.split("#,#");
        GridItemModel item;
        for (int i = 0; i < allImagesPath.length; i++) {
            item = new GridItemModel();
            String image = "https://www.biztechinfomedia.com/ktclifesuccess/" + allImagesPath[i];
            item.setId("");
            item.setTitle("");
            if (image != null && image.endsWith(".jpg")) {
                item.setImage(image);
            }
            mGridData.add(item);
        }
        Collections.reverse(mGridData);

        ///////////////////////////////////////////
      /*
        try {
            JSONArray mJsonArray = new JSONArray(result);
            JSONObject mJsonObject = new JSONObject();
            GridItem item;

            for (int i = 0; i < mJsonArray.length(); i++) {
                item = new GridItem();

                mJsonObject = mJsonArray.getJSONObject(i);
                String id = mJsonObject.getString("id");
                String name = mJsonObject.getString("name");
                String image = mJsonObject.getString("image");

                item.setId(id);
                item.setTitle(name);
                if(image !=null)
                    item.setImage(image);
                mGridData.add(item);
            }


        } catch (JSONException e) {
            e.printStackTrace();
        }

       */
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        SharedPreferences shared = getSharedPreferences("" + R.string.sp_retriveUserData, MODE_PRIVATE);
        String retriveUserData = (shared.getString("data", ""));
        if (retriveUserData != null && retriveUserData.contains("!95947639111@")) {
            getMenuInflater().inflate(R.menu.menu_achievers_page, menu);
        }
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.menu_achievers_add_image) {
            startActivity(new Intent(ShowAchieversActivity.this, AddAchieversActivity.class));
        }
        return true;
    }
}