package com.ithublive.ktclife.Admin.Adaptor;

import android.app.Activity;
import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.ithublive.ktclife.Admin.Model.UplineModel;
import com.ithublive.ktclife.Model.WalletHistoryModel;
import com.ithublive.ktclife.R;

import java.util.List;

import static android.content.Context.MODE_PRIVATE;
import static com.ithublive.ktclife.R.drawable.level_diamond;
import static com.ithublive.ktclife.R.drawable.level_gold;
import static com.ithublive.ktclife.R.drawable.level_ruby;
import static com.ithublive.ktclife.R.drawable.level_silver;

public class UplineListAdapter extends ArrayAdapter<UplineModel> {

    public boolean isForAdmin_to_seeUsersInfo;
    private Activity context;
    public static List<UplineModel> ordertlist;
    public static String name, add, model_name, model_issue, date;
    public String user_id;
    SharedPreferences sharedPreferences;

    public UplineListAdapter(Activity context, List<UplineModel> orderlist) {
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

        View listview = inflater.inflate(R.layout.upline_listlayout, null, true);

        TextView tv_userid = listview.findViewById(R.id.tv_userid);
        ImageView imageView_level = listview.findViewById(R.id.ic_level);
        UplineModel um = ordertlist.get(position);

        tv_userid.setText(um.id + um.name );

        if (um.level.equals("silver")) {
            imageView_level.setBackgroundResource(level_silver);
        } else if (um.level.equals("gold")) {
            imageView_level.setBackgroundResource(level_gold);
        } else if (um.level.equals("diamond")) {
            imageView_level.setBackgroundResource(level_diamond);
        } else if (um.level.equals("ruby")) {
            imageView_level.setBackgroundResource(level_ruby);
        } else if (um.level.equals("No Level")) {
        }

        return listview;
    }
}