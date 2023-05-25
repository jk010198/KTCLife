package com.ithublive.ktclife.Admin.Adaptor;

import android.app.Activity;
import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.ithublive.ktclife.Admin.Model.PayinPayoutModel;
import com.ithublive.ktclife.Model.WalletHistoryModel;
import com.ithublive.ktclife.R;

import java.util.List;

import static android.content.Context.MODE_PRIVATE;

public class IdPayInListAdaptor extends ArrayAdapter<PayinPayoutModel> {
    public boolean isForAdmin_to_seeUsersInfo;

    private Activity context;
    public static List<PayinPayoutModel> ordertlist;
    public static String name, add, model_name, model_issue, date;
    public String user_id;
    SharedPreferences sharedPreferences;

    public IdPayInListAdaptor(Activity context, List<PayinPayoutModel> orderlist) {
        super(context, R.layout.wallethistory_listlayout, orderlist);
        this.context = context;
        this.ordertlist = orderlist;
        sharedPreferences = context.getSharedPreferences("" + R.string.sp_retriveUserData, MODE_PRIVATE);
        String retriveUserData = (sharedPreferences.getString("data", ""));
        if (isForAdmin_to_seeUsersInfo) {

        } else {
            user_id = retriveUserData.substring(retriveUserData.indexOf("!") + 1, retriveUserData.indexOf("@"));
        }
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        LayoutInflater inflater = context.getLayoutInflater();

        View listview = inflater.inflate(R.layout.payin_payout_listlayout, null, true);

        TextView tv_userid = listview.findViewById(R.id.tv_userid);
        TextView tv_paymode = listview.findViewById(R.id.tv_paymode);
        TextView tv_date = listview.findViewById(R.id.tv_date);
        TextView tv_note = listview.findViewById(R.id.tv_notes);
        TextView tv_rs = listview.findViewById(R.id.tv_amount);
        ImageView imageView_credited = listview.findViewById(R.id.ic_creditedtransfer);
        ImageView imageView_debited = listview.findViewById(R.id.ic_debittransfer);
        PayinPayoutModel ap = ordertlist.get(position);

        tv_userid.setText("UserId :- " + ap.userid);
        tv_paymode.setText("Pay mode :- " + ap.pay_mode);
        tv_date.setText("Date :-" + ap.pay_date);
        tv_note.setText("Installment no :- " + ap.ins_no);
        tv_rs.setText("₹ " + ap.amount);
        if (ap.withdrawal_date.equals("")) {
            imageView_credited.setVisibility(View.VISIBLE);
            imageView_debited.setVisibility(View.GONE);
        } else {
            imageView_credited.setVisibility(View.GONE);
            imageView_debited.setVisibility(View.VISIBLE);
        }
        return listview;
    }
}