package com.ithublive.ktclife.Admin;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.ithublive.ktclife.R;

public class Reports extends AppCompatActivity {

    ImageView imageView_back;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reports);

        imageView_back = findViewById(R.id.backBtn);
        imageView_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
    }

    public void methodNewJoiningReport(View view) {
        startActivity(new Intent(getApplicationContext(), NewJoiningReport.class));
    }

    public void methodPayoutsReport(View view) {
        startActivity(new Intent(getApplicationContext(), PayoutReport.class));
    }

    public void methodPendingPaymentsReport(View view) {
        startActivity(new Intent(getApplicationContext(), PendingPayementsReport.class));
    }

    public void methodShowActivityLogsReport(View view) {
        startActivity(new Intent(getApplicationContext(), ActivityLogReport.class));
    }

    public void methodPendingKycUsers(View view) {
        startActivity(new Intent(getApplicationContext(), PendingUsersKycList.class));
    }

    public void methodSTotalRegisterUsers(View view) {
        startActivity(new Intent(getApplicationContext(), TotalRegisterUsersListReport.class));
    }

    public void methodIdPayInReport(View view) {
        startActivity(new Intent(getApplicationContext(), IdPayInReports.class));
    }

    public void methodIdPayOutReport(View view) {
        startActivity(new Intent(getApplicationContext(), IdPayOutReports.class));
    }

    public void methodMonthlyTargetReport(View view) {
        startActivity(new Intent(getApplicationContext(), MonthlyTargetActivity.class));
    }

    public void methodCommissionReport(View view) {
        startActivity(new Intent(getApplicationContext(), CommissionReport.class));
    }

    public void methodIntPayoutReport(View view) {
        startActivity(new Intent(getApplicationContext(), IntPayoutReport.class));
    }
}