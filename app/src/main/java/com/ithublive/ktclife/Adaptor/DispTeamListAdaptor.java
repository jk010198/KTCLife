package com.ithublive.ktclife.Adaptor;

public class DispTeamListAdaptor {}
//extends RecyclerView.Adapter<DispTeamListAdaptor.MyViewHolder> {
//
//    public Context context;
//
//    //  public List<DispTeamModel> mFilteredList;
//
//    public List<DispTeamModel> modelList;
//    ProgressDialog progressDialog_legInfo;
//
//    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
//        CircleImageView profile_img, userInfobtn;
//        TextView tv_name, tv_userid, tv_direct_ref;
//        RelativeLayout relativeLayout;
//        TextView tv_paidNos, tv_unpaidNos;
//        CircleImageView ib_user_one, ib_user_two, ib_user_three, ib_user_four, ib_user_five, ib_user_six, ib_user_seven, ib_user_eight;
//
//        public MyViewHolder(View view) {
//            super(view);
//            tv_name = (TextView) view.findViewById(R.id.tv_name);
//            tv_userid = (TextView) view.findViewById(R.id.tv_userid);
//            profile_img = view.findViewById(R.id.profile_img);
//            userInfobtn = view.findViewById(R.id.userInfo);
//            relativeLayout = view.findViewById(R.id.relativelayout);
//            tv_paidNos = view.findViewById(R.id.dispTeamPaidNos);
//            tv_unpaidNos = view.findViewById(R.id.dispTeamUnPaidNos);
//
////            ib_user_one = view.findViewById(R.id.ib_user_one);
////            ib_user_two = view.findViewById(R.id.ib_user_two);
////            ib_user_three = view.findViewById(R.id.ib_user_three);
////            ib_user_four = view.findViewById(R.id.ib_user_four);
////            ib_user_five = view.findViewById(R.id.ib_user_five);
////            ib_user_six = view.findViewById(R.id.ib_user_six);
////            ib_user_seven = view.findViewById(R.id.ib_user_seven);
////            ib_user_eight = view.findViewById(R.id.ib_user_eight);
//
//            tv_direct_ref = view.findViewById(R.id.tv_direct_ref);
//        }
//
//        public void onClick(View view) {
//            int id = view.getId();
//            int adapterPosition = getAdapterPosition();
//            Toast.makeText(context, "Clicked" + adapterPosition, Toast.LENGTH_SHORT).show();
//        }
//    }
//
//    public DispTeamListAdaptor(List<DispTeamModel> list, Context context2) {
//        modelList = list;
//    }
//
//    public MyViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
//        View inflate = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.direct_ref_listlayout, viewGroup, false);
//        context = viewGroup.getContext();
//        return new MyViewHolder(inflate);
//    }
//
//    public void onBindViewHolder(final MyViewHolder myViewHolder, int i) {
//        final DispTeamModel dispTeamModel = (DispTeamModel) modelList.get(i);
//        myViewHolder.tv_name.setText(dispTeamModel.getName());
//        if (dispTeamModel.isPaid.equals("yes")) {
//            myViewHolder.tv_name.setTextColor(Color.GREEN);
//            myViewHolder.tv_userid.setTextColor(Color.GREEN);
//        } else if (dispTeamModel.isPaid.equals("no")) {
//            myViewHolder.tv_name.setTextColor(Color.RED);
//            myViewHolder.tv_userid.setTextColor(Color.RED);
//        }
//        myViewHolder.tv_userid.setText(dispTeamModel.getUserid());
//        myViewHolder.tv_paidNos.setText("Paid Legs: " + dispTeamModel.getPaidLegsNum());
//        myViewHolder.tv_unpaidNos.setText("Unpaid Legs: " + dispTeamModel.getUnPaidLegsNum());
////        myViewHolder.tv_paidNos.setTextColor(Color.GREEN);
////        myViewHolder.tv_unpaidNos.setTextColor(Color.RED);
//        if (dispTeamModel.profile_imgurl.isEmpty()) {
//            Glide.with(context)
//                    .load(R.drawable.ic_user_dummy)
//                    .into(myViewHolder.profile_img);
//        } else {
//            Glide.with(context)
//                    .load(dispTeamModel.profile_imgurl)
//                    .into(myViewHolder.profile_img);
//        }
//
//        myViewHolder.relativeLayout.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Toast.makeText(context, "Clicked..." + myViewHolder.tv_userid.getText(), Toast.LENGTH_SHORT).show();
//                viewCustomersData(dispTeamModel.userid);
//            }
//        });
//
//        myViewHolder.userInfobtn.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                //Toast.makeText(context, "" + dispTeamModel.name, Toast.LENGTH_SHORT).show();
//                getLegIndivisualInfo(myViewHolder.tv_userid.getText().toString());
//            }
//        });
//    }
//
//    public int getItemCount() {
//        return modelList.size();
//    }
//
//    public void viewCustomersData(String userid) {
//        //Toast.makeText(context, "" + userid, Toast.LENGTH_SHORT).show();
////        Intent intent = new Intent(context, DirectRefDisplayTeamActivity.class);
//        Intent intent = new Intent(context, DisplayTeam.class);
//        intent.putExtra("userid", userid);
//        intent.putExtra("isForLegDetails", "yes4563");
//        //intent.putExtra("isbackPressed", false); ///
//        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//        context.startActivity(intent);
//    }
//
//    private void getLegIndivisualInfo(final String legID) {
//        progressDialog_legInfo = new ProgressDialog(context);
//        progressDialog_legInfo.setTitle(R.string.dialog_title);
//        progressDialog_legInfo.setMessage("Please wait, fetching data");
//        progressDialog_legInfo.setCancelable(false);
//        progressDialog_legInfo.setIcon(R.drawable.app_logo);
//        progressDialog_legInfo.show();
//        StringRequest stringRequest = new StringRequest(Request.Method.POST, BaseUrl.retriveUserDataUrl, new Response.Listener<String>() {
//            @Override
//            public void onResponse(String response) {
//                progressDialog_legInfo.dismiss();
//                if (response.contains("no data found")) {
//                    Toast.makeText(context, "no data found.", Toast.LENGTH_SHORT).show();
//                } else {
//                    String data = response;
//                    String username = data.substring(data.indexOf("@") + 1, data.indexOf("#"));
//                    String my_Plan = data.substring(data.indexOf("&") + 1, data.indexOf("*"));
//                    String isPaid = data.substring(data.indexOf("#1A#") + 4, data.indexOf("#2A#"));
//                    String join_date = data.substring(data.indexOf("#2A#") + 4, data.indexOf("#3A#"));
//                    join_date = join_date.substring(0, 10);
//                    String tv_number_legs = data.substring(data.indexOf("#3A#") + 4, data.indexOf("#4A#")) +
//                            "         Direct ref. : " + data.substring(data.indexOf("#20A#") + 5, data.indexOf("#21A#"));
//                    String poppay_details = "Pay Details:\n";
//                    int paidCount = 0;
//                    if (data.contains("#pn#") && isPaid.contains("yes")) {
//                        paidCount = Integer.parseInt(data.substring(data.indexOf("#pn#") + 4, data.indexOf("#epn#")));
//                        String payDetails = data.substring(data.indexOf("#epn#") + 5, data.indexOf(":e_pay:"));
//                        String payDetails_array[] = payDetails.split(",");
//                        for (String pdata : payDetails_array) {
//                            //2020-07-29 21:45:20::3:_:2000:$:
//                            String p_date = pdata.substring(0, 10);
//                            int month_no = Integer.parseInt(pdata.substring(pdata.indexOf("::") + 2, pdata.indexOf(":_:")));
//                            String plan_amt = pdata.substring(pdata.indexOf(":_:") + 3, pdata.indexOf(":$:"));
//                            poppay_details = poppay_details + month_no + "\t\t" + p_date + "\t\t" + plan_amt + "\n";
//                        }
//                    }
//                    //////// Set up POPUP
//                    final AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(context);
//                    final AlertDialog alert = alertDialogBuilder.create();
//                    final LayoutInflater layoutInflater = LayoutInflater.from(context);
//                    final View popupInputDialogView = layoutInflater.inflate(R.layout.layout_popup_leginfo, null);
//                    alert.setView(popupInputDialogView);
//                    alert.show();
//                    alert.setCancelable(false);
//                    final TextView tv_popup_leginfo = popupInputDialogView.findViewById(R.id.tv_popupInfoOfLeg);
//
//                    tv_popup_leginfo.setText("ID: " + legID + "\nName: " + username + "\n\nPlan: " + my_Plan + "\t\t\tIs Paid:   " + isPaid +
//                            "\n\nJoin Date: " + join_date + "\n\nNumber of Legs: " + tv_number_legs + "\n" + poppay_details);
//                    ////////////////////
//                    Button btn_close_Dialog = popupInputDialogView.findViewById(R.id.buttonClosePopupLeginfo);
//                    ImageButton imageButton = popupInputDialogView.findViewById(R.id.btn_dialog_ktc_close);
//                    imageButton.setOnClickListener(new View.OnClickListener() {
//                        @Override
//                        public void onClick(View view) {
//                            alert.dismiss();
//                        }
//                    });
//                    btn_close_Dialog.setOnClickListener(new View.OnClickListener() {
//                        @Override
//                        public void onClick(View view) {
//                            //alert.cancel();
//                            alert.dismiss();
//                        }
//                    });
//                    ///// check if admin ///////
//                    TextView tv_adminTask_Dialog = popupInputDialogView.findViewById(R.id.tv_adminTasksOnpopupLegInfo);
//                    tv_adminTask_Dialog.setVisibility(View.GONE);
//                    SharedPreferences shared = context.getSharedPreferences("" + R.string.sp_retriveUserData, MODE_PRIVATE);
//                    String retriveUserData = (shared.getString("data", ""));
//                    String id = retriveUserData.substring(retriveUserData.indexOf("!") + 1, retriveUserData.indexOf("@"));
//                    if (id.equals("95947639111")) {
//                        tv_adminTask_Dialog.setVisibility(View.VISIBLE);
//                    }
//
//                    tv_adminTask_Dialog.setOnClickListener(new View.OnClickListener() {
//                        @Override
//                        public void onClick(View view) {
//                            Intent intent = new Intent(context, AdminUpdatePayments.class);
//                            intent.putExtra("u_info_id", legID);
//                            context.startActivity(intent);
//                        }
//                    });
//                    ////////////
//                }
//            }
//        }, new Response.ErrorListener() {
//            @Override
//            public void onErrorResponse(VolleyError error) {
//                Toast.makeText(context, "Something went wrong.", Toast.LENGTH_SHORT).show();
//                progressDialog_legInfo.dismiss();
//            }
//        }) {
//            @Override
//            protected Map<String, String> getParams() throws AuthFailureError {
//                Map<String, String> map = new HashMap<>();
//                map.put("id", legID);
//                return map;
//            }
//        };
//        Volley.newRequestQueue(context).add(stringRequest);
//    }
//}