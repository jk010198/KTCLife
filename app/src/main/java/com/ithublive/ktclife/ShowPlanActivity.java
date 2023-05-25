package com.ithublive.ktclife;

import android.os.Bundle;
import android.text.Html;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.CompoundButton;
import android.widget.RadioButton;
import android.widget.Spinner;
import android.widget.TableLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.res.ResourcesCompat;

import com.google.android.material.textview.MaterialTextView;

public class ShowPlanActivity extends AppCompatActivity {

    AutoCompleteTextView autoCompleteTextView;
    String arr[] = {"-- Choose Plan --", "1000", "2000", "3000", "4000", "5000", "10000", "15000", "20000"};
    TableLayout tableLayout;
    TextView tv_plan_1, tv_plan_2, tv_plan_3, tv_plan_4, tv_plan_5, tv_plan_6, tv_plan_7, tv_plan_8, tv_plan_9, tv_plan_10, tv_plan_11,
            tv_plan_12, tv_plan_13, tv_plan_14, tv_plan_15, tv_plan_16, tv_plan_17, tv_plan_18, tv_plan_19,
            tv_plan_20, tv_plan_21, tv_plan_22, tv_plan_23, tv_plan_24, tv_plan_25;

    MaterialTextView tvPlanInfoText;
    RadioButton rbMarathi, rbHindi, rbEnglish;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_plan);

        tvPlanInfoText = findViewById(R.id.tvInfoPlan302);
        rbMarathi = findViewById(R.id.rbMarathi67);
        rbHindi = findViewById(R.id.rbHindi67);
        rbEnglish = findViewById(R.id.rbEnglish67);
        rbEnglish.setChecked(true);
        tvPlanInfoText.setText("This is a membership plan. You can choose any plan starting from 1000 to 20000 provided as above. Plan installments can be paid at the interval of 30 days, no any late fees are there.\n Cash back can be done after any 6th, 12th, 18th or 24rd installments. i.e. If you want cash back at 6th month you need not to pay 6th installment. If you pay 6th installment you can cash back after 11th installment, you need not to pay 12th installment to cash back, and so on for 18th and 24th month cash back.\n   Apart from 7,13,19 or 25 You cant cash back at any other number of installment.\n You are agreed to above terms. Thank you." + "\n\nNote: If you refer any person to the business, you will get extra benifites according to plans.");
        rbHindi.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    tvPlanInfoText.setText("यह मेम्बरशिप प्लान है| ऊपर दिए 1000 से 20000 का कोई भी प्लान लेने के बाद आप दूसरा क़िस्त ३० दिन बाद कभी भी भर सकते हो , कोई भी लेट फीस नहीं है| \n  ६ किस्त के बाद ७ वा किस्त न भरके आप cash back ले सकते हो, अगर आप पेमेंट करते हो तब आप १२ किस्त भरने के बाद १३ वा किस्त ना भरके cash back ले सकते हो| इसी प्रकार आप १८ के बाद या २४ के बाद cash back ले सकते हो|\n   २५ वे किस्त के समय आपका cash back हो जायेगा| ७,१३,१९ व २५ छोड़ के बिच के कोई किस्त पे आप cash back नहीं ले सकते हो,\n इस बात से आप सहमत हो, आपका धन्यवाद| \n\n  Note: अगर आप किसी को रेफ़र करते हो तो उसका बोनस आपको प्लान के अनुसार अलग से मिलेगा| ");
                }
            }
        });
        rbMarathi.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    tvPlanInfoText.setText("हा मेंबरशीप प्लान आहे. वर देलेल्या 1000 ते 20000 पैकी कोणताही प्लान घेतल्यानंतर आपण दुसरा हप्ता ३० दिवसानंतर कधीही भरू शकता, कोणतीही लेट फीस नाहीये. \n  ६ हप्त्या नंतर ७ वा हप्ता न भरून आपण cash back घेऊ शकता, जर आपण पेमेंट केले तर १२ वा हप्ता भरून १३ वा हप्ता न भरता cash back घेऊ शकता. अश्याच प्रकारे आपण १८ किंवा २४ नंतर cash back घेऊ शकता.\n   २५ व्या हप्त्या वेळी आपले cash back होईलच. ७,१३,१९ व २५ सोडून मधल्या कोणत्या हि हप्त्या वर आपण cash back नाही घेऊ शकणार,\n ह्या बाबी शी आपण सहमत आहात. आपले आभार.\n\n Note: जर आपण कोणाला रेफ़र केले तर त्याचा अतिरिक्त बोनस प्लान नुसार मिळेल. ");
                }
            }
        });
        rbEnglish.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    tvPlanInfoText.setText("This is a membership plan. You can choose any plan starting from 1000 to 1Lakh provided as above. Plan installments can be paid at the interval of 30 days, no any late fees are there.\" +\n" +
                            "                \"Cash back can be done after any 5th, 11th, 17th or 23rd installments. i.e. If you want to cash back at 6th month you need  not to pay 6th installment. If you pay 6th installment you can take cash back after 11th installment, you need not to pay 12th installment to cash back, and so on for 18th and 24th month cash back.\" +\n" +
                            "                \" You cant withdrawl at any other number of installment. If you dont pay you cant withdrawl. You are agreed to above terms. Thank you.\" +\n" +
                            "                \"Note: If you refer any person to the business, you will get extra benifites according to plans.");
                }
            }
        });

        TextView textView_description = findViewById(R.id.tv_desc_showPlan8593);
        textView_description.setText(Html.fromHtml("Below table shows Month number and Deposited Amount. <br>  Red month shows cash back amount at that month."));
        tableLayout = findViewById(R.id.tablePlan);
        tableLayout.setVisibility(View.GONE);
        autoCompleteTextView = findViewById(R.id.autoCompletetv);

        String plan[] = (getResources().getStringArray(R.array.planList));
        autoCompleteTextView.setAdapter(new ArrayAdapter<String>(ShowPlanActivity.this, R.layout.showplan_dropdown, plan));

        autoCompleteTextView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                //Toast.makeText(ShowPlanActivity.this, arr[i], Toast.LENGTH_SHORT).show();
                String plan = arr[i];
                if (plan.equals("1000")) {
                    tableLayout.setVisibility(View.VISIBLE);
                    tv_plan_1.setText("1000");
                    tv_plan_2.setText("2000");
                    tv_plan_3.setText("3000");
                    tv_plan_4.setText("4000");
                    tv_plan_5.setText("5000");
                    tv_plan_6.setText("6000"); //1
                    tv_plan_7.setText("7000");
                    tv_plan_8.setText("8000");
                    tv_plan_9.setText("9000");
                    tv_plan_10.setText("10000");
                    tv_plan_11.setText("11000");
                    tv_plan_12.setText("12000"); //2
                    tv_plan_13.setText("15000");
                    tv_plan_14.setText("14000");
                    tv_plan_15.setText("15000");
                    tv_plan_16.setText("16000");
                    tv_plan_17.setText("17000");
                    tv_plan_18.setText("18000"); //3
                    tv_plan_19.setText("22000");
                    tv_plan_20.setText("20000");
                    tv_plan_21.setText("21000");
                    tv_plan_22.setText("22000");
                    tv_plan_23.setText("23000");
                    tv_plan_24.setText("24000");
                    tv_plan_25.setText("31000"); //6
                } else if (plan.equals("2000")) {
                    tableLayout.setVisibility(View.VISIBLE);
                    tv_plan_1.setText("2000");
                    tv_plan_2.setText("4000");
                    tv_plan_3.setText("6000");
                    tv_plan_4.setText("8000");
                    tv_plan_5.setText("10000");
                    tv_plan_6.setText("12000");
                    tv_plan_7.setText("14000");
                    tv_plan_8.setText("16000");
                    tv_plan_9.setText("18000");
                    tv_plan_10.setText("20000");
                    tv_plan_11.setText("22000");
                    tv_plan_12.setText("24000");
                    tv_plan_13.setText("30000");
                    tv_plan_14.setText("28000");
                    tv_plan_15.setText("30000");
                    tv_plan_16.setText("32000");
                    tv_plan_17.setText("34000");
                    tv_plan_18.setText("36000");
                    tv_plan_19.setText("44000");
                    tv_plan_20.setText("40000");
                    tv_plan_21.setText("42000");
                    tv_plan_22.setText("44000");
                    tv_plan_23.setText("46000");
                    tv_plan_24.setText("48000");
                    tv_plan_25.setText("62000");
                } else if (plan.equals("3000")) {
                    tableLayout.setVisibility(View.VISIBLE);
                    tv_plan_1.setText("3000");
                    tv_plan_2.setText("6000");
                    tv_plan_3.setText("9000");
                    tv_plan_4.setText("12000");
                    tv_plan_5.setText("15000");
                    tv_plan_6.setText("18000");
                    tv_plan_7.setText("21000");
                    tv_plan_8.setText("24000");
                    tv_plan_9.setText("27000");
                    tv_plan_10.setText("30000");
                    tv_plan_11.setText("33000");
                    tv_plan_12.setText("36000");
                    tv_plan_13.setText("45000");
                    tv_plan_14.setText("42000");
                    tv_plan_15.setText("45000");
                    tv_plan_16.setText("48000");
                    tv_plan_17.setText("51000");
                    tv_plan_18.setText("54000");
                    tv_plan_19.setText("66000");
                    tv_plan_20.setText("60000");
                    tv_plan_21.setText("63000");
                    tv_plan_22.setText("66000");
                    tv_plan_23.setText("69000");
                    tv_plan_24.setText("72000");
                    tv_plan_25.setText("93000");
                } else if (plan.equals("4000")) {
                    tableLayout.setVisibility(View.VISIBLE);
                    tv_plan_1.setText("4000");
                    tv_plan_2.setText("8000");
                    tv_plan_3.setText("12000");
                    tv_plan_4.setText("16000");
                    tv_plan_5.setText("20000");
                    tv_plan_6.setText("24000");
                    tv_plan_7.setText("28000");
                    tv_plan_8.setText("32000");
                    tv_plan_9.setText("36000");
                    tv_plan_10.setText("40000");
                    tv_plan_11.setText("44000");
                    tv_plan_12.setText("48000");
                    tv_plan_13.setText("60000");
                    tv_plan_14.setText("56000");
                    tv_plan_15.setText("60000");
                    tv_plan_16.setText("64000");
                    tv_plan_17.setText("68000");
                    tv_plan_18.setText("72000");
                    tv_plan_19.setText("88000");
                    tv_plan_20.setText("80000");
                    tv_plan_21.setText("84000");
                    tv_plan_22.setText("88000");
                    tv_plan_23.setText("92000");
                    tv_plan_24.setText("96000");
                    tv_plan_25.setText("124000");
                } else if (plan.equals("5000")) {
                    tableLayout.setVisibility(View.VISIBLE);
                    tv_plan_1.setText("5000");
                    tv_plan_2.setText("10000");
                    tv_plan_3.setText("15000");
                    tv_plan_4.setText("20000");
                    tv_plan_5.setText("25000");
                    tv_plan_6.setText("30000");
                    tv_plan_7.setText("35000");
                    tv_plan_8.setText("40000");
                    tv_plan_9.setText("45000");
                    tv_plan_10.setText("50000");
                    tv_plan_11.setText("55000");
                    tv_plan_12.setText("60000");
                    tv_plan_13.setText("75000");
                    tv_plan_14.setText("70000");
                    tv_plan_15.setText("75000");
                    tv_plan_16.setText("80000");
                    tv_plan_17.setText("85000");
                    tv_plan_18.setText("90000");
                    tv_plan_19.setText("110000");
                    tv_plan_20.setText("100000");
                    tv_plan_21.setText("105000");
                    tv_plan_22.setText("110000");
                    tv_plan_23.setText("115000");
                    tv_plan_24.setText("120000");
                    tv_plan_25.setText("155000");
                }
                ////// New Plans 10000//////////////
                else if (plan.equals("10000")) {
                    tableLayout.setVisibility(View.VISIBLE);
                    tv_plan_1.setText("10000");
                    tv_plan_2.setText("20000");
                    tv_plan_3.setText("30000");
                    tv_plan_4.setText("40000");
                    tv_plan_5.setText("50000");
                    tv_plan_6.setText("60000");
                    tv_plan_7.setText("70000");
                    tv_plan_8.setText("80000");
                    tv_plan_9.setText("90000");
                    tv_plan_10.setText("100000");
                    tv_plan_11.setText("110000");
                    tv_plan_12.setText("120000");
                    tv_plan_13.setText("150000");
                    tv_plan_14.setText("140000");
                    tv_plan_15.setText("150000");
                    tv_plan_16.setText("160000");
                    tv_plan_17.setText("170000");
                    tv_plan_18.setText("180000");
                    tv_plan_19.setText("220000");
                    tv_plan_20.setText("200000");
                    tv_plan_21.setText("210000");
                    tv_plan_22.setText("220000");
                    tv_plan_23.setText("230000");
                    tv_plan_24.setText("240000");
                    tv_plan_25.setText("310000");
                }
                ////// New Plans 15000//////////////
                else if (plan.equals("15000")) {
                    tableLayout.setVisibility(View.VISIBLE);
                    tv_plan_1.setText("15000");
                    tv_plan_2.setText("30000");
                    tv_plan_3.setText("45000");
                    tv_plan_4.setText("60000");
                    tv_plan_5.setText("75000");
                    tv_plan_6.setText("90000");
                    tv_plan_7.setText("105000");
                    tv_plan_8.setText("120000");
                    tv_plan_9.setText("135000");
                    tv_plan_10.setText("150000");
                    tv_plan_11.setText("165000");
                    tv_plan_12.setText("180000");
                    tv_plan_13.setText("225000");
                    tv_plan_14.setText("210000");
                    tv_plan_15.setText("225000");
                    tv_plan_16.setText("240000");
                    tv_plan_17.setText("255000");
                    tv_plan_18.setText("270000");
                    tv_plan_19.setText("345000");
                    tv_plan_20.setText("300000");
                    tv_plan_21.setText("315000");
                    tv_plan_22.setText("330000");
                    tv_plan_23.setText("345000");
                    tv_plan_24.setText("360000");
                    tv_plan_25.setText("465000");
                }
                ////// New Plans 20000//////////////
                else if (plan.equals("20000")) {
                    tableLayout.setVisibility(View.VISIBLE);
                    tv_plan_1.setText("20000");
                    tv_plan_2.setText("40000");
                    tv_plan_3.setText("60000");
                    tv_plan_4.setText("80000");
                    tv_plan_5.setText("100000");
                    tv_plan_6.setText("120000");
                    tv_plan_7.setText("140000");
                    tv_plan_8.setText("160000");
                    tv_plan_9.setText("180000");
                    tv_plan_10.setText("200000");
                    tv_plan_11.setText("220000");
                    tv_plan_12.setText("240000");
                    tv_plan_13.setText("300000");
                    tv_plan_14.setText("280000");
                    tv_plan_15.setText("300000");
                    tv_plan_16.setText("320000");
                    tv_plan_17.setText("340000");
                    tv_plan_18.setText("360000");
                    tv_plan_19.setText("460000");
                    tv_plan_20.setText("400000");
                    tv_plan_21.setText("420000");
                    tv_plan_22.setText("440000");
                    tv_plan_23.setText("460000");
                    tv_plan_24.setText("480000");
                    tv_plan_25.setText("620000");
                } else if (plan.equals("-- Choose Plan --")){
                    tableLayout.setVisibility(View.GONE);
                }
            }
        });

        tv_plan_1 = findViewById(R.id.tv_plan_show1);
        tv_plan_2 = findViewById(R.id.tv_plan_show2);
        tv_plan_3 = findViewById(R.id.tv_plan_show3);
        tv_plan_4 = findViewById(R.id.tv_plan_show4);
        tv_plan_5 = findViewById(R.id.tv_plan_show5);
        tv_plan_6 = findViewById(R.id.tv_plan_show6);
        tv_plan_7 = findViewById(R.id.tv_plan_show7);
        tv_plan_8 = findViewById(R.id.tv_plan_show8);
        tv_plan_9 = findViewById(R.id.tv_plan_show9);
        tv_plan_10 = findViewById(R.id.tv_plan_show10);
        tv_plan_11 = findViewById(R.id.tv_plan_show11);
        tv_plan_12 = findViewById(R.id.tv_plan_show12);
        tv_plan_13 = findViewById(R.id.tv_plan_show13);
        tv_plan_14 = findViewById(R.id.tv_plan_show14);
        tv_plan_15 = findViewById(R.id.tv_plan_show15);
        tv_plan_16 = findViewById(R.id.tv_plan_show16);
        tv_plan_17 = findViewById(R.id.tv_plan_show17);
        tv_plan_18 = findViewById(R.id.tv_plan_show18);
        tv_plan_19 = findViewById(R.id.tv_plan_show19);
        tv_plan_20 = findViewById(R.id.tv_plan_show20);
        tv_plan_21 = findViewById(R.id.tv_plan_show21);
        tv_plan_22 = findViewById(R.id.tv_plan_show22);
        tv_plan_23 = findViewById(R.id.tv_plan_show23);
        tv_plan_24 = findViewById(R.id.tv_plan_show24);
        tv_plan_25 = findViewById(R.id.tv_plan_show25);
    }
}
