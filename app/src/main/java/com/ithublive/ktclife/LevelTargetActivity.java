package com.ithublive.ktclife;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.ithublive.ktclife.Utils.BaseUrl;

public class LevelTargetActivity extends AppCompatActivity {

    ImageView bonus_imageview;
    Dialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tree_monthly_bonus);

        bonus_imageview = findViewById(R.id.imageview_bonus);
        dialog = new Dialog(LevelTargetActivity.this);
        dialog.setContentView(R.layout.layout_progress_dialog);
        if (dialog.getWindow() != null) {
            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(0));
        }
        dialog.setCancelable(false);
        dialog.show();

        Glide.with(LevelTargetActivity.this)
                .load(BaseUrl.dbLevelTargetImageUrl)
                .into(bonus_imageview);
        dialog.dismiss();
    }
}
