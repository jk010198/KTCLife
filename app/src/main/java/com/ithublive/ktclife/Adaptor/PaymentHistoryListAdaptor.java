package com.ithublive.ktclife.Adaptor;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.app.Service;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.drawable.ColorDrawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.core.widget.NestedScrollView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.ithublive.ktclife.Model.PaymentHistory;
import com.ithublive.ktclife.R;
import com.ithublive.ktclife.Utils.BaseUrl;
import com.ithublive.ktclife.Utils.Tools;
import com.ithublive.ktclife.Utils.ViewAnimation;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketAddress;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static android.content.Context.MODE_PRIVATE;

public class PaymentHistoryListAdaptor extends RecyclerView.Adapter<PaymentHistoryListAdaptor.MyViewHolder> {

    public Context context;
    public List<PaymentHistory> mFilteredList;
    public List<PaymentHistory> modelList;
    Dialog dialog;
    List<PaymentHistory> list_paymentdetails;
    PayDetailsAdaptor payDetailsAdaptor;
    String retriveUserData;

    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        View parent_view;
        NestedScrollView nested_scroll_view;
        ImageButton bt_toggle_text;
        Button bt_hide_text;
        View lyt_expand_text;
        TextView tv_title, tv_no_details;
        RecyclerView rv_paymentdetails;

        public MyViewHolder(View view) {
            super(view);
            this.tv_title = view.findViewById(R.id.tv_title);
            this.parent_view = view.findViewById(android.R.id.content);
            this.bt_toggle_text = view.findViewById(R.id.bt_toggle_text);
            this.bt_hide_text = view.findViewById(R.id.bt_hide_text);
            this.lyt_expand_text = view.findViewById(R.id.lyt_expand_text);
            this.nested_scroll_view = view.findViewById(R.id.nested_scroll_view);
            this.rv_paymentdetails = view.findViewById(R.id.rv_paymentdetails);
            this.tv_no_details = view.findViewById(R.id.tv_no_withdrawalhistory);
        }

        public void onClick(View view) {
        }
    }

    public PaymentHistoryListAdaptor(List<PaymentHistory> list, Context context2) {
        this.modelList = list;
        this.mFilteredList = list;
    }

    public MyViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View inflate = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.layout_payment_history, viewGroup, false);
        this.context = viewGroup.getContext();
        return new MyViewHolder(inflate);
    }

    public void onBindViewHolder(final MyViewHolder myViewHolder, int i) {
        final PaymentHistory paymentHistory = (PaymentHistory) this.modelList.get(i);
        myViewHolder.lyt_expand_text.setVisibility(View.GONE);
        myViewHolder.bt_toggle_text.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SharedPreferences shared = context.getSharedPreferences("" + R.string.sp_retriveUserData, MODE_PRIVATE);
                retriveUserData = (shared.getString("data", ""));

                LinearLayoutManager linearLayoutManager = new LinearLayoutManager(context);
                myViewHolder.rv_paymentdetails.setLayoutManager(linearLayoutManager);
                list_paymentdetails = new ArrayList<>();
                Dialog dialog;

                dialog = new Dialog(context);
                dialog.setContentView(R.layout.layout_progress_dialog);
                if (dialog.getWindow() != null) {
                    dialog.getWindow().setBackgroundDrawable(new ColorDrawable(0));
                }
                dialog.setCancelable(false);
                dialog.show();
                list_paymentdetails.clear();

                StringRequest stringRequest = new StringRequest(Request.Method.POST, BaseUrl.paymentsHistoryUrl, new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        if (response.contains("conn_error")) {
                            dialog.dismiss();
                            Toast.makeText(context, "Server error. Try Again", Toast.LENGTH_SHORT).show();
                        }
                        //Log.d("list_op", response);
                        String res = response.substring(response.indexOf("details_op") + 10, response.indexOf("#ended#"));

                        String[] res_part = res.split("#breakk#");

                        for (int i = 0; i < res_part.length; i++) {
                            String installment_no = res_part[i].substring(res_part[i].indexOf("#inst_no#") + 9, res_part[i].indexOf("#pay_dt#"));
                            String pay_date = res_part[i].substring(res_part[i].indexOf("#pay_dt#") + 8, res_part[i].indexOf("#pay_amt#"));
                            String amount = res_part[i].substring(res_part[i].indexOf("#pay_amt#") + 9, res_part[i].indexOf("#withdra_dt#"));
                            String withdrawal_date = res_part[i].substring(res_part[i].indexOf("#withdra_dt#") + 12, res_part[i].indexOf("#endd#"));
                            list_paymentdetails.add(new PaymentHistory("", amount, installment_no, withdrawal_date, pay_date));
                        }

                        if (list_paymentdetails.size() > 1) {
                            myViewHolder.tv_no_details.setVisibility(View.GONE);
                        } else {
                            myViewHolder.tv_no_details.setVisibility(View.VISIBLE);
                        }

                        payDetailsAdaptor = new PayDetailsAdaptor(list_paymentdetails, context);
                        myViewHolder.rv_paymentdetails.setAdapter(payDetailsAdaptor);
                        payDetailsAdaptor.notifyDataSetChanged();
                        dialog.dismiss();
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(context, "Server error", Toast.LENGTH_SHORT).show();
                        dialog.dismiss();
                    }
                }) {
                    @Override
                    protected Map<String, String> getParams() throws AuthFailureError {
                        Map<String, String> map = new HashMap<>();
                        map.put("for", "fullDetails");
                        map.put("userid", "wd" + retriveUserData.substring(retriveUserData.indexOf("!") + 1, retriveUserData.indexOf("@")));
                        map.put("from_dt", paymentHistory.plan_paid_date);
                        map.put("to_dt", paymentHistory.withdrawal_date);
                        return map;
                    }
                };
                Volley.newRequestQueue(context).add(stringRequest);

                boolean show = toggleArrow(myViewHolder.bt_toggle_text);
                if (show) {
                    ViewAnimation.expand(myViewHolder.lyt_expand_text, new ViewAnimation.AnimListener() {
                        @Override
                        public void onFinish() {
                            Tools.nestedScrollTo(myViewHolder.nested_scroll_view, myViewHolder.lyt_expand_text);
                        }
                    });
                } else {
                    ViewAnimation.collapse(myViewHolder.lyt_expand_text);
                }
            }
        });

        myViewHolder.bt_hide_text.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                boolean show = toggleArrow(myViewHolder.bt_toggle_text);
                if (show) {
                    ViewAnimation.expand(myViewHolder.lyt_expand_text, new ViewAnimation.AnimListener() {
                        @Override
                        public void onFinish() {
                            Tools.nestedScrollTo(myViewHolder.nested_scroll_view, myViewHolder.lyt_expand_text);
                        }
                    });
                } else {
                    ViewAnimation.collapse(myViewHolder.lyt_expand_text);
                }
            }
        });

        myViewHolder.tv_title.setText("Plan :- " + paymentHistory.amount + "\nWithdra Date :- " + paymentHistory.withdrawal_date);
    }

    public boolean isConnected() {
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Service.CONNECTIVITY_SERVICE);

        if (connectivityManager != null) {
            NetworkInfo info = connectivityManager.getActiveNetworkInfo();
            if (info != null) {
                if (info.getState() == NetworkInfo.State.CONNECTED) {
                    return true;
                }
            }
        }
        return false;
    }

    class internetChecking extends AsyncTask<String, Void, Integer> {

        @Override
        protected Integer doInBackground(String... params) {

            Integer result = 0;
            try {
                Socket socket = new Socket();
                SocketAddress socketAddress = new InetSocketAddress("8.8.8.8", 53);
                socket.connect(socketAddress, 2500);
                socket.close();
                result = 1;
            } catch (IOException e) {
                e.printStackTrace();
                result = 0;
            }
            return result;
        }

        @Override
        protected void onPostExecute(Integer result) {
            if (isConnected()) {
                if (result == 1) {
                    //mainMethod();
                }
                if (result == 0) {
                    dialog.dismiss();
                    Toast.makeText(context, " No internet available ", Toast.LENGTH_SHORT).show();
                }
            } else {
                dialog.dismiss();
                Toast.makeText(context, " No internet connection available ", Toast.LENGTH_SHORT).show();
            }
            super.onPostExecute(result);
        }
    }

    public int getItemCount() {
        return this.mFilteredList.size();
    }

    public boolean toggleArrow(View view) {
        if (view.getRotation() == 0) {
            view.animate().setDuration(200).rotation(180);
            return true;
        } else {
            view.animate().setDuration(200).rotation(0);
            return false;
        }
    }

    /////////////////// inner adapter ///////////////////////
    public class PayDetailsAdaptor extends RecyclerView.Adapter<PayDetailsAdaptor.MyViewHolder> {

        public Context context;
        public List<PaymentHistory> modelList;

        public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
            TextView tv_pay, tv_plan, tv_paydate, tv_withdrawal_date;

            public MyViewHolder(View view) {
                super(view);
                tv_pay = itemView.findViewById(R.id.tv_pay);
                tv_plan = itemView.findViewById(R.id.tv_plan);
                tv_paydate = itemView.findViewById(R.id.tv_paydate);
                tv_withdrawal_date = itemView.findViewById(R.id.tv_withdrawal_date);
            }

            public void onClick(View view) {
            }
        }

        public PayDetailsAdaptor(List<PaymentHistory> list, Context context2) {
            modelList = list;
        }

        public MyViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
            View inflate = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.list_payment_details, viewGroup, false);
            context = viewGroup.getContext();
            return new MyViewHolder(inflate);
        }

        public void onBindViewHolder(final MyViewHolder myViewHolder, int i) {
            final PaymentHistory ph = (PaymentHistory) modelList.get(i);
            myViewHolder.tv_pay.setText(ph.installment_no);
            myViewHolder.tv_plan.setText(ph.amount);
            myViewHolder.tv_paydate.setText(ph.plan_paid_date);
            myViewHolder.tv_withdrawal_date.setText(ph.withdrawal_date);
        }

        public int getItemCount() {
            return modelList.size();
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public int getItemViewType(int position) {
            return position;
        }
    }
    /////////////////END INNER CLASS//////////////////////////////
}