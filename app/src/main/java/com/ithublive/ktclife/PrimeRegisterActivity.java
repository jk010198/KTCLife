package com.ithublive.ktclife;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import de.hdodenhof.circleimageview.CircleImageView;

public class PrimeRegisterActivity extends AppCompatActivity {

    EditText et_shopName, et_ownerName, et_mobileNumber, et_address, et_pinNumber;
    Button button_regShop;
    CircleImageView img_shop;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_prime_register);
        img_shop = findViewById(R.id.imageview_shopReg_img92);
        et_shopName = findViewById(R.id.et_primeShopReg_shopname);
        et_ownerName = findViewById(R.id.et_primeShopReg_shopOwnername);
        et_mobileNumber = findViewById(R.id.et_primeShopReg_mobilenumber);
        et_address = findViewById(R.id.et_primeShopReg_shopaddress);
        et_pinNumber = findViewById(R.id.et_primeShopReg_pinnumber);
        button_regShop = findViewById(R.id.btn_primeShopReg_regShop);

        button_regShop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
    }
}