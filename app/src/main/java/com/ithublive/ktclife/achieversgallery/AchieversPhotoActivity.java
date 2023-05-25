package com.ithublive.ktclife.achieversgallery;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;

import com.ithublive.ktclife.R;
import com.ortiz.touchview.TouchImageView;
import com.squareup.picasso.Picasso;

public class AchieversPhotoActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_achievers_photo);

        TouchImageView imageView = findViewById(R.id.iv_singleAchieverDisplay);
        Intent intent = getIntent();
        String imgUrl = intent.getStringExtra("image");
        Picasso.with(this).load(imgUrl).into(imageView);
    }
}
