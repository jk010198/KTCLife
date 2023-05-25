package com.ithublive.ktclife;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.davemorrissey.labs.subscaleview.ImageSource;
import com.davemorrissey.labs.subscaleview.SubsamplingScaleImageView;

public class PlansActivity extends AppCompatActivity {

    SubsamplingScaleImageView iv1, iv2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_plans);

        iv1 = findViewById(R.id.iv_plan1);
        iv2 = findViewById(R.id.iv_plan2);

        iv1.setImage(ImageSource.resource(R.drawable.plan_1));
        iv2.setImage(ImageSource.resource(R.drawable.plan_2));
    }
}