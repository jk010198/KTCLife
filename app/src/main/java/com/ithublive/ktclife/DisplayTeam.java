package com.ithublive.ktclife;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.ithublive.ktclife.Model.DispTeamModel;
import com.ithublive.ktclife.Utils.BaseUrl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;

public class DisplayTeam extends AppCompatActivity {

    RecyclerView recy_view_dispTeam;
    TextView tv_nolegs;
    String id;
    List<DispTeamModel> list_dispteam;
    DispTeamListAdaptor dispTeamListAdaptor;
    Dialog dialog;
    TextView tv_legs;
    boolean isBackpress;
    private String keyword;
    private String username = "";
    private SharedPreferences leg_expansion;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display_team);

        tv_legs = findViewById(R.id.textview_legs);
        tv_legs.setMovementMethod(new ScrollingMovementMethod());
        recy_view_dispTeam = findViewById(R.id.recyclerview_display_team);
        tv_nolegs = findViewById(R.id.tv_nolegs_msgDisplayTeam);
        dialog = new Dialog(DisplayTeam.this);
        dialog.setContentView(R.layout.layout_progress_dialog);
        if (dialog.getWindow() != null) {
            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(0));
        }
        dialog.setCancelable(false);

        SharedPreferences shared = getSharedPreferences("" + R.string.sp_name, MODE_PRIVATE);
        id = (shared.getString("username", ""));
        leg_expansion = getSharedPreferences("legs_data", MODE_PRIVATE);
        SharedPreferences.Editor edit = leg_expansion.edit();
        edit.putString("userid", id);
        edit.commit();

        list_dispteam = new ArrayList<>();

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(DisplayTeam.this);
        recy_view_dispTeam.setLayoutManager(linearLayoutManager);
        getUserDataFromDB(id);
    }

    public void getUserDataFromDB(String id) {
        dialog.show();
        StringRequest stringRequest = new StringRequest(Request.Method.POST, BaseUrl.retriveUserDataUrl, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                String data = response;
                list_dispteam.clear();
                if (response.contains("@")) {
                    username = data.substring(data.indexOf("@") + 1, data.indexOf("#"));
                }
                if (data.contains("#refs#")) {
                    String idUrl = data.substring(data.indexOf("#refs#") + 6, data.indexOf("#urls#"));

                    if (!(idUrl.isEmpty())) {
                        String idUrl_part[] = idUrl.split(",");
                        for (String singleUserData : idUrl_part) {
                            DispTeamModel dispTeamModel = new DispTeamModel();
                            dispTeamModel.profile_imgurl = singleUserData.substring(singleUserData.indexOf(":_:") + 3, singleUserData.indexOf(":$:"));
                            dispTeamModel.name = singleUserData.substring(singleUserData.indexOf("::") + 2, singleUserData.indexOf(":_:"));
                            dispTeamModel.userid = singleUserData.substring(0, singleUserData.indexOf("::"));
                            dispTeamModel.isPaid = singleUserData.substring(singleUserData.indexOf(":$:") + 3, singleUserData.indexOf(":#:"));
                            list_dispteam.add(dispTeamModel);
                        }
                    }
                } else {
                }

                if (!(list_dispteam.size() > 0)) {
                    tv_nolegs.setVisibility(View.VISIBLE);
                    dialog.dismiss();
                } else {
                    tv_nolegs.setVisibility(View.INVISIBLE);
                    dialog.dismiss();
                }
                /////////////////
                if (isBackpress) {
                    String tv_leg_names = tv_legs.getText().toString();
                    if (tv_leg_names.contains(">")) {
                        keyword = ">";
                        int index = tv_leg_names.lastIndexOf(keyword);
                        String new_leg_names = tv_leg_names.substring(0, index);
                        tv_legs.setText(new_leg_names);
                    } else {
                    }
                } else {
                    if (tv_legs.getText().toString().isEmpty()) {
                        tv_legs.setText(username);
                    } else {
                        String str = tv_legs.getText().toString() + " > " + username;
                        tv_legs.setText(str);
                    }
                }
                ///////////////////

                dispTeamListAdaptor = new DispTeamListAdaptor(list_dispteam, DisplayTeam.this);
                recy_view_dispTeam.setAdapter(dispTeamListAdaptor);
                dispTeamListAdaptor.notifyDataSetChanged();
                getLegIndividualDetailInfo();
                dialog.dismiss();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                dialog.dismiss();
                Toast.makeText(DisplayTeam.this, "Something went wrong.", Toast.LENGTH_SHORT).show();
                tv_nolegs.setVisibility(View.VISIBLE);
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> map = new HashMap<>();
                map.put("id", id);
                return map;
            }
        };

        Volley.newRequestQueue(DisplayTeam.this).add(stringRequest);
    }

    public void getLegIndividualDetailInfo() {
        dialog.show();
        StringRequest stringRequest = new StringRequest(Request.Method.POST, BaseUrl.ktc_get_additional_info_tree, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                dialog.dismiss();
                if (response.contains("#M1#")) {
                    String[] allSublegsInfo = response.split("#EINF#");
                    for (DispTeamModel dtm : list_dispteam) {
                        for (String str : allSublegsInfo) {
                            if (str.contains(dtm.userid)) {
                                dtm.setPaidLegsNum(Integer.parseInt(str.substring(str.indexOf("#t0#") + 4, str.indexOf("#t1#"))));
                                dtm.setUnPaidLegsNum(Integer.parseInt(str.substring(str.indexOf("#t1#") + 4, str.indexOf("#t2#"))));
                                dtm.levelColor = "no_color";

                                if (str.contains("silver")) {
                                    int legs = Integer.parseInt(str.substring(str.indexOf("silver") + 6, str.indexOf("gold")));
                                    if (legs >= 8) {
                                        dtm.levelColor = "silver";
                                        int silverLegs = Integer.parseInt(str.substring(str.indexOf("gold") + 4, str.indexOf("ruby")));
                                        if (silverLegs >= 8) {
                                            dtm.levelColor = "gold";
                                            int goldLegs = Integer.parseInt(str.substring(str.indexOf("ruby") + 4, str.indexOf("diamond")));
                                            if (goldLegs >= 8) {
                                                dtm.levelColor = "ruby";
                                                int rubyLegs = Integer.parseInt(str.substring(str.indexOf("diamond") + 7,
                                                        str.indexOf("end_level_count")));
                                                if (rubyLegs >= 8) {
                                                    dtm.levelColor = "diamond";
                                                }
                                            }
                                        }
                                    } else {
                                        dtm.levelColor = "No Level";
                                    }
                                }
                                //////////////////////////////////
                            }
                        }
                    }
                    dispTeamListAdaptor = new DispTeamListAdaptor(list_dispteam, DisplayTeam.this);
                    recy_view_dispTeam.setAdapter(dispTeamListAdaptor);
                    dispTeamListAdaptor.notifyDataSetChanged();
                    dialog.dismiss();
                }
                if (response.contains("idisempty")) {
                    dialog.dismiss();
                    Toast.makeText(DisplayTeam.this, "No data found.", Toast.LENGTH_SHORT).show();
                }

                if (response.contains("nodatafound")) {
                    dialog.dismiss();
                    Toast.makeText(DisplayTeam.this, "No data found.", Toast.LENGTH_SHORT).show();
                }

                if (response.contains("dbConErr")) {
                    dialog.dismiss();
                    Toast.makeText(DisplayTeam.this, "Database connection error.", Toast.LENGTH_SHORT).show();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(DisplayTeam.this, "Something went wrong.", Toast.LENGTH_SHORT).show();
                dialog.dismiss();
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> map = new HashMap<>();
                map.put("id", id);
                return map;
            }
        };
        Volley.newRequestQueue(DisplayTeam.this).add(stringRequest);
    }

    @Override
    public void onBackPressed() {
        isBackpress = true;
        id = "";
        String leg_exp = (leg_expansion.getString("userid", ""));

        if (leg_exp.length() > 11) {
            String retriveData = leg_exp.substring(leg_exp.length() - 22, leg_exp.length() - 11);

            id = retriveData;
            leg_expansion = getSharedPreferences("legs_data", MODE_PRIVATE);
            SharedPreferences.Editor edit = leg_expansion.edit();
            String str = leg_exp.substring(0, leg_exp.length() - 11);
            edit.putString("userid", str);
            edit.commit();

            Log.d("spData148", "sentto db " + id);
            getUserDataFromDB(retriveData);
        } else {
            super.onBackPressed();
        }
    }

    //////////////INNER ADAPTOR CLASS ///////////
    public class DispTeamListAdaptor extends RecyclerView.Adapter<DispTeamListAdaptor.MyViewHolder> {

        public Context context;
        public List<DispTeamModel> modelList;

        public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
            CircleImageView profile_img, userInfobtn, levelColorImage, iv_id_delete;
            public TextView tv_name, tv_userid;
            RelativeLayout relativeLayout;
            TextView tv_paidNos, tv_unpaidNos;

            public MyViewHolder(View view) {
                super(view);
                tv_name = view.findViewById(R.id.tv_name);
                tv_userid = view.findViewById(R.id.tv_userid);
                profile_img = view.findViewById(R.id.profile_img);
                userInfobtn = view.findViewById(R.id.userInfo);
                relativeLayout = view.findViewById(R.id.relativelayout);
                tv_paidNos = view.findViewById(R.id.dispTeamPaidNos);
                tv_unpaidNos = view.findViewById(R.id.dispTeamUnPaidNos);
                levelColorImage = view.findViewById(R.id.levelColorImage);
                iv_id_delete = view.findViewById(R.id.iv_id_delete);

                SharedPreferences shared = context.getSharedPreferences("" + R.string.sp_retriveUserData, MODE_PRIVATE);
                String retriveUserData = (shared.getString("data", ""));
                String id = retriveUserData.substring(retriveUserData.indexOf("!") + 1, retriveUserData.indexOf("@"));
                if (id.equals("95947639111")) {
                    iv_id_delete.setVisibility(View.VISIBLE);
                } else {
                    iv_id_delete.setVisibility(View.INVISIBLE);
                }
            }

            public void onClick(View view) {
            }
        }

        public DispTeamListAdaptor(List<DispTeamModel> list, Context context2) {
            modelList = list;
        }

        public MyViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
            View inflate = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.direct_ref_listlayout, viewGroup, false);
            context = viewGroup.getContext();
            return new MyViewHolder(inflate);
        }

        public void onBindViewHolder(final MyViewHolder myViewHolder, int i) {
            final DispTeamModel dispTeamModel = (DispTeamModel) modelList.get(i);
            myViewHolder.tv_name.setText(dispTeamModel.getName());
            myViewHolder.tv_name.setTextColor(Color.BLACK);
            if (dispTeamModel.isPaid.equals("yes")) {
                myViewHolder.tv_name.setTextColor(Color.BLACK);
                myViewHolder.tv_userid.setTextColor(getResources().getColor(R.color.green_dark));
            } else if (dispTeamModel.isPaid.equals("no")) {
                myViewHolder.tv_name.setTextColor(Color.RED);
                myViewHolder.tv_userid.setTextColor(Color.RED);
            }
            myViewHolder.tv_userid.setText(dispTeamModel.getUserid());
            myViewHolder.tv_paidNos.setText("Paid Legs: " + dispTeamModel.getPaidLegsNum());
            myViewHolder.tv_unpaidNos.setText("Unpaid Legs: " + dispTeamModel.getUnPaidLegsNum());

            if (dispTeamModel.getLevelColor() != null && dispTeamModel.getLevelColor().equals("silver")) {
                myViewHolder.levelColorImage.setVisibility(View.VISIBLE);
                myViewHolder.levelColorImage.setImageResource(R.drawable.level_silver);
            } else if (dispTeamModel.levelColor != null && dispTeamModel.levelColor.equals("gold")) {
                myViewHolder.levelColorImage.setVisibility(View.VISIBLE);
                myViewHolder.levelColorImage.setImageResource(R.drawable.level_gold);
            } else if (dispTeamModel.levelColor != null && dispTeamModel.levelColor.equals("diamond")) {
                myViewHolder.levelColorImage.setVisibility(View.VISIBLE);
                myViewHolder.levelColorImage.setImageResource(R.drawable.level_diamond);
            } else if (dispTeamModel.levelColor != null && dispTeamModel.levelColor.equals("ruby")) {
                myViewHolder.levelColorImage.setVisibility(View.VISIBLE);
                myViewHolder.levelColorImage.setImageResource(R.drawable.level_ruby);
            }

            myViewHolder.iv_id_delete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    android.app.AlertDialog.Builder adb = new android.app.AlertDialog.Builder(DisplayTeam.this);
                    adb.setTitle(R.string.dialog_title);
                    adb.setMessage("Do you want to delete this id...?");
                    adb.setIcon(R.drawable.app_logo);
                    adb.setNegativeButton("NO", null);
                    adb.setPositiveButton("YES", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            deleteId(myViewHolder.tv_userid.getText().toString().trim());
                        }
                    });
                    adb.show();
                }
            });

            Glide.with(context)
                    .load(R.drawable.app_logo)
                    .into(myViewHolder.profile_img);
            if (dispTeamModel.profile_imgurl.isEmpty()) {
            } else {
                try {
                    Glide.with(context)
                            .load(dispTeamModel.profile_imgurl)
                            .into(myViewHolder.profile_img);
                } catch (Exception e) {
                    Glide.with(context)
                            .load(R.drawable.app_logo)
                            .into(myViewHolder.profile_img);
                }
            }
            myViewHolder.relativeLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    isBackpress = false;
                    id = dispTeamModel.userid;
                    String value = leg_expansion.getString("userid", "");
                    StringBuffer str = new StringBuffer(value);
                    str.append(id);
                    SharedPreferences.Editor edit = leg_expansion.edit();
                    edit.putString("userid", String.valueOf(str));
                    edit.commit();

                    getUserDataFromDB(dispTeamModel.userid);
                }
            });

            myViewHolder.userInfobtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    getLegIndivisualInfo(myViewHolder.tv_userid.getText().toString());
                }
            });
        }

        public void deleteId(String userIdSentDb) {
            StringRequest stringRequest_delid = new StringRequest(Request.Method.POST, BaseUrl.deleteUserId, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    if (response.contains("!_paid_no_legs!")) {
                        popup();
                    } else if (response.contains("#_paid_yes_legs#")) {
                        popup();
                    } else if (response.contains("$_paid_yes_legs")) {
                        popup();
                    } else if (response.contains("@_paid_no_legs@")) {
                        android.app.AlertDialog.Builder adb = new android.app.AlertDialog.Builder(DisplayTeam.this);
                        adb.setTitle(R.string.dialog_title);
                        adb.setMessage("This id deleted successfully...");
                        adb.setIcon(R.drawable.app_logo);
                        adb.setNeutralButton("OK", null);
                        adb.show();
                    }
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    dialog.dismiss();
                    Toast.makeText(context, "Server Error...", Toast.LENGTH_SHORT).show();
                }
            }) {
                @Override
                protected Map<String, String> getParams() throws AuthFailureError {
                    Map<String, String> map = new HashMap<>();
                    map.put("id", userIdSentDb);
                    return map;
                }
            };
            Volley.newRequestQueue(DisplayTeam.this).add(stringRequest_delid);
        }

        public void popup() {
            android.app.AlertDialog.Builder adb = new android.app.AlertDialog.Builder(DisplayTeam.this);
            adb.setTitle(R.string.dialog_title);
            adb.setMessage("You can not delete this Id.");
            adb.setIcon(R.drawable.app_logo);
            adb.setNeutralButton("OK", null);
            adb.show();
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

        public void viewCustomersData(String userid) {
            Intent intent = new Intent(context, DisplayTeam.class);
            intent.putExtra("userid", userid);
            intent.putExtra("isForLegDetails", "yes4563");
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            context.startActivity(intent);
        }

        private void getLegIndivisualInfo(final String legID) {
            dialog = new Dialog(DisplayTeam.this);
            dialog.setContentView(R.layout.layout_progress_dialog);
            if (dialog.getWindow() != null) {
                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(0));
            }
            dialog.setCancelable(false);
            dialog.show();
            StringRequest stringRequest = new StringRequest(Request.Method.POST, BaseUrl.retriveUserDataUrl, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    dialog.dismiss();
                    if (response.contains("no data found")) {
                        Toast.makeText(context, "no data found.", Toast.LENGTH_SHORT).show();
                    } else {
                        String data = response;
                        String username = data.substring(data.indexOf("@") + 1, data.indexOf("#"));
                        String my_Plan = data.substring(data.indexOf("&") + 1, data.indexOf("*"));
                        String isPaid = data.substring(data.indexOf("#1A#") + 4, data.indexOf("#2A#"));
                        String join_date = data.substring(data.indexOf("#2A#") + 4, data.indexOf("#3A#"));
                        join_date = join_date.substring(0, 10);
                        String tv_number_legs = data.substring(data.indexOf("#3A#") + 4, data.indexOf("#4A#"));
                        String poppay_details = "Pay Details:\n";
                        int paidCount = 0;
                        if (data.contains("#pn#") && isPaid.contains("yes")) {
                            paidCount = Integer.parseInt(data.substring(data.indexOf("#pn#") + 4, data.indexOf("#epn#")));
                            String payDetails = data.substring(data.indexOf("#epn#") + 5, data.indexOf(":e_pay:"));
                            String payDetails_array[] = payDetails.split(",");
                            for (String pdata : payDetails_array) {
                                //2020-07-29 21:45:20::3:_:2000:$:
                                String p_date = pdata.substring(0, 10);
                                int month_no = Integer.parseInt(pdata.substring(pdata.indexOf("::") + 2, pdata.indexOf(":_:")));
                                String plan_amt = pdata.substring(pdata.indexOf(":_:") + 3, pdata.indexOf(":$:"));
                                poppay_details = poppay_details + month_no + "\t\t" + p_date + "\t\t" + plan_amt + "\n";
                            }
                        }
                        //// Set up POPUP //////////
                        final AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(context);
                        final AlertDialog alert = alertDialogBuilder.create();
                        final LayoutInflater layoutInflater = LayoutInflater.from(context);
                        final View popupInputDialogView = layoutInflater.inflate(R.layout.layout_popup_leginfo, null);
                        alert.setView(popupInputDialogView);
                        alert.show();
                        alert.setCancelable(false);
                        final TextView tv_popup_leginfo = popupInputDialogView.findViewById(R.id.tv_popupInfoOfLeg);

                        tv_popup_leginfo.setText("ID: " + legID + "\nName: " + username + "\n\nPlan: " + my_Plan + "\t\t\tIs Paid:   " + isPaid +
                                "\n\nJoin Date: " + join_date + "\n\nNumber of Legs: " + tv_number_legs + "\n" + poppay_details);
                        ////////////////////
                        Button btn_close_Dialog = popupInputDialogView.findViewById(R.id.buttonClosePopupLeginfo);
                        ImageButton imageButton = popupInputDialogView.findViewById(R.id.btn_dialog_ktc_close);
                        imageButton.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                alert.dismiss();
                            }
                        });
                        btn_close_Dialog.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                alert.dismiss();
                            }
                        });
                        ///// check if admin ///////
                        TextView tv_adminTask_Dialog = popupInputDialogView.findViewById(R.id.tv_adminTasksOnpopupLegInfo);
                        tv_adminTask_Dialog.setVisibility(View.GONE);
                        SharedPreferences shared = context.getSharedPreferences("" + R.string.sp_retriveUserData, MODE_PRIVATE);
                        String retriveUserData = (shared.getString("data", ""));
                        String id = retriveUserData.substring(retriveUserData.indexOf("!") + 1, retriveUserData.indexOf("@"));
                        if (id.equals("95947639111")) {
                            tv_adminTask_Dialog.setVisibility(View.VISIBLE);
                        }

                        tv_adminTask_Dialog.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                Intent intent = new Intent(context, AdminUpdatePayments.class);
                                intent.putExtra("u_info_id", legID);
                                context.startActivity(intent);
                            }
                        });
                        /////////////////
                    }
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Toast.makeText(context, "Something went wrong.", Toast.LENGTH_SHORT).show();
                    dialog.dismiss();
                }
            }) {
                @Override
                protected Map<String, String> getParams() throws AuthFailureError {
                    Map<String, String> map = new HashMap<>();
                    map.put("id", legID);
                    return map;
                }
            };
            Volley.newRequestQueue(context).add(stringRequest);
        }
    }
    /////////END INNER CLASS///////////////
}