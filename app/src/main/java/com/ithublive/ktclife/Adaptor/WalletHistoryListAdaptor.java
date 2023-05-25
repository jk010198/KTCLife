package com.ithublive.ktclife.Adaptor;

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

import com.ithublive.ktclife.Model.WalletHistoryModel;
import com.ithublive.ktclife.R;

import java.util.List;

import static android.content.Context.MODE_PRIVATE;

public class WalletHistoryListAdaptor extends ArrayAdapter<WalletHistoryModel> {

    public boolean isForAdmin_to_seeUsersInfo;
    private Activity context;
    public static List<WalletHistoryModel> ordertlist;
    public static String name, add, model_name, model_issue, date;
    public String user_id;
    SharedPreferences sharedPreferences;

    public WalletHistoryListAdaptor(Activity context, List<WalletHistoryModel> orderlist) {
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

        View listview = inflater.inflate(R.layout.wallethistory_listlayout, null, true);

        TextView tv_userid = listview.findViewById(R.id.tv_userid);
        TextView tv_ben_id = listview.findViewById(R.id.tv_ben_id);
        TextView tv_date = listview.findViewById(R.id.tv_date);
        TextView tv_note = listview.findViewById(R.id.tv_notes);
        TextView tv_rs = listview.findViewById(R.id.tv_amount);
        ImageView imageView_credited = listview.findViewById(R.id.ic_creditedtransfer);
        ImageView imageView_debeted = listview.findViewById(R.id.ic_debittransfer);
        WalletHistoryModel ap = ordertlist.get(position);

        tv_userid.setText("From :- " + ap.user_name + " (" + ap.userid + ")");
        tv_ben_id.setText("To :- " + ap.ben_name + " (" + ap.ben_id + ")");
        tv_date.setText("Transaction Date :-" + ap.date);
        tv_note.setText("Message :- " + ap.notes);
        tv_rs.setText("₹ " + ap.amount);
        if (user_id.equals(ap.ben_id)) {
            imageView_credited.setVisibility(View.VISIBLE);
            imageView_debeted.setVisibility(View.GONE);
        } else if (user_id.equals(ap.userid)) {
            imageView_credited.setVisibility(View.GONE);
            imageView_debeted.setVisibility(View.VISIBLE);
        }
        return listview;
    }
}