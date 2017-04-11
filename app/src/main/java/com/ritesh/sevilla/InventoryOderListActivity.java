package com.ritesh.sevilla;

import android.content.Context;
import android.graphics.Rect;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.util.Log;
import android.view.HapticFeedbackConstants;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.nispok.snackbar.Snackbar;
import com.nispok.snackbar.SnackbarManager;
import com.ritesh.sevilla.Beans.BeanInventoryList;
import com.ritesh.sevilla.Constant.Appconstant;
import com.ritesh.sevilla.Constant.Utils;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import fr.castorflex.android.circularprogressbar.CircularProgressBar;
import fr.castorflex.android.circularprogressbar.CircularProgressDrawable;

/**
 * Created by ritesh on 10/4/17.
 */
@SuppressWarnings("deprecation")
public class InventoryOderListActivity extends AppCompatActivity {

    @BindView(R.id.toolbar_inventory_order)
    Toolbar TB_inventory_order;

    /*@BindView(R.id.rl_cart_icon_inventory_order)
    RelativeLayout RL_cart_icon_inventory_order;

    @BindView(R.id.rl_cart_icon_inventory_order_click)
    RelativeLayout RL_cart_icon_inventory_order_click;

    @BindView(R.id.rl_badgeview_cart_item_inventory_order)
    RelativeLayout RL_badgeview_cart_item_inventory_order;

    @BindView(R.id.rl_badgeview_cart_item_inventory_order_click)
    RelativeLayout RL_badgeview_cart_item_inventory_order_click;*/

    @BindView(R.id.rl_gridview_inventory_order_progress)
    RelativeLayout RL_gridview_inventory_order_progress;

    CircularProgressBar CPB_inventory_order_progressbar_circular;

    /*@BindView(R.id.tv_badge_counter_inventory_order)
    TextView TV_badge_counter_inventory_order;

    @BindView(R.id.tv_badge_counter_inventory_order_click)
    TextView TV_badge_counter_inventory_order_click;*/

    @BindView(R.id.recycler_view_inventory_order)
    RecyclerView RV_inventory_order;

    RecyclerView.LayoutManager mLayoutManager;

    InventoryHistoryAdapter inventoryHistoryAdapter;


    String
            User_ID = "",
            Str_Oder_number = "",
            Str_Order_Date = "",
            Str_Order_Total_Price = "",
            Str_Order_Status = "",
            Get_Inventory_list_status = "",
            Get_Inventory_Order_List = "",
            Get_Inventory_Single_Order_Result = "";


    List<InventoryOderListActivity> inventoryhinstorylistrowItems;
    private ArrayList<String> InventoryHistory_OrderNumber;
    private ArrayList<String> InventoryHistory_Orderdate;
    private ArrayList<String> InventoryHistory_Orderprice;
    private ArrayList<String> InventoryHistory_Orderstatus;
    List<BeanInventoryList> beanInventoryHistorylist0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inventory_list);
        ButterKnife.bind(this);
        Appconstant.sh = getSharedPreferences(Appconstant.MyPREFERENCES, Context.MODE_PRIVATE);
        User_ID = Appconstant.sh.getString("id", null);
        Log.e("User_ID from SharedPref :", "" + User_ID);


        CPB_inventory_order_progressbar_circular = (CircularProgressBar) findViewById(R.id.gridview_inventory_order_progressbar_circular);
//        signupProgress.setVisibility(View.GONE);
        ((CircularProgressDrawable) CPB_inventory_order_progressbar_circular.getIndeterminateDrawable()).start();
        updateValues();


        inventoryhinstorylistrowItems = new ArrayList<InventoryOderListActivity>();
        InventoryHistory_OrderNumber = new ArrayList<>();
        InventoryHistory_Orderdate = new ArrayList<>();
        InventoryHistory_Orderprice = new ArrayList<>();
        InventoryHistory_Orderstatus = new ArrayList<>();



        RV_inventory_order.setHasFixedSize(true);

        mLayoutManager = new LinearLayoutManager(this);
        RV_inventory_order.setLayoutManager(mLayoutManager);
        RV_inventory_order.addItemDecoration(new InventoryOderListActivity.EqualSpaceItemDecoration(15));



        if (Utils.isConnected(getApplication())) {
            UserInventoryListJsontask task = new UserInventoryListJsontask();
            task.execute();
        } else {

            SnackbarManager.show(
                    Snackbar.with(InventoryOderListActivity.this)
                            .position(Snackbar.SnackbarPosition.TOP)
                            .margin(15, 15)
                            .backgroundDrawable(R.drawable.snackbar_custom_layout)
                            .text("Please Your Internet Connectivity..!!"));

        }


    }


    private class EqualSpaceItemDecoration extends RecyclerView.ItemDecoration {

        private final int mSpaceHeight;

        public EqualSpaceItemDecoration(int mSpaceHeight) {
            this.mSpaceHeight = mSpaceHeight;
        }

        @Override
        public void getItemOffsets(Rect outRect, View view, RecyclerView parent,
                                   RecyclerView.State state) {
            outRect.bottom = mSpaceHeight;
            outRect.top = mSpaceHeight;
        }

    }


    private class UserInventoryListJsontask extends AsyncTask<String, Void, List<BeanInventoryList>> {

        boolean iserror = false;
        String result = " ";

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            RL_gridview_inventory_order_progress.setVisibility(View.VISIBLE);
            Log.e(" ************ MyCartListJsontask AsyncTask Start ************ :", "yes");
            Log.e("USER_ID :", "" + User_ID);
            Log.e("Inventory URL :", "" + "\n" + "http://sevilla.centrocomercial.com.es/wp-content/plugins/webserv/order_list.php?user_id="+User_ID);
        }

        @Override
        protected List<BeanInventoryList> doInBackground(String... params) {
            HttpClient client = new DefaultHttpClient();
            HttpPost post = new HttpPost("http://sevilla.centrocomercial.com.es/wp-content/plugins/webserv/order_list.php?user_id="+User_ID);

            Log.e(" URL :", " " + "http://sevilla.centrocomercial.com.es/wp-content/plugins/webserv/order_list.php?user_id="+User_ID);

            try {
                HttpResponse response = client.execute(post);
                String obj = EntityUtils.toString(response.getEntity());
                beanInventoryHistorylist0 = new ArrayList<>();
                Log.e("************ list ************", "" + obj);

                Log.e("************Json data*******************", " " + obj);
                JSONObject jsonObject = new JSONObject(obj);
                Get_Inventory_list_status = jsonObject.getString("status");
                Get_Inventory_Order_List = jsonObject.getString("order_result");
                Log.e("Json Get_Inventory_Order_List data :", " " + Get_Inventory_Order_List);
                JSONArray jaaray = new JSONArray(Get_Inventory_Order_List);

                if (Get_Inventory_list_status.equalsIgnoreCase("OK")) {
                    Log.e("Status is :", "OK");
                    for (int i = 0; i < jaaray.length(); i++) {
                        Get_Inventory_Single_Order_Result = jaaray.getJSONObject(i).getString("result");

                        if (Get_Inventory_Single_Order_Result.equalsIgnoreCase("successful")) {
                            BeanInventoryList beanInventoryList = new BeanInventoryList();
                            beanInventoryList.setOrderNumber(jaaray.getJSONObject(i).getString("order_id"));
                            beanInventoryList.setOrderdate(jaaray.getJSONObject(i).getString("order_date"));
                            beanInventoryList.setOrderprice(jaaray.getJSONObject(i).getString("total_item_price"));
                            beanInventoryList.setOrderstatus(jaaray.getJSONObject(i).getString("order_status"));
                            beanInventoryHistorylist0.add(beanInventoryList);


                            Str_Oder_number = jaaray.getJSONObject(i).getString("order_id");
                            Str_Order_Date = jaaray.getJSONObject(i).getString("order_date");
                            Str_Order_Total_Price = jaaray.getJSONObject(i).getString("total_item_price");
                            Str_Order_Status = jaaray.getJSONObject(i).getString("order_status");

                            InventoryHistory_OrderNumber.add(Str_Oder_number);
                            InventoryHistory_Orderdate.add(Str_Order_Date);
                            InventoryHistory_Orderprice.add(Str_Order_Total_Price);
                            InventoryHistory_Orderstatus.add(Str_Order_Status);

                            Log.e(" ********** InventoryHistory_OrderNumber List**********", "" + InventoryHistory_OrderNumber);
                            Log.e(" ********** InventoryHistory_Orderdate List**********", "" + InventoryHistory_Orderdate);
                            Log.e(" ********** InventoryHistory_Orderprice List**********", "" + InventoryHistory_Orderprice);
                            Log.e(" ********** InventoryHistory_Orderstatus List**********", "" + InventoryHistory_Orderstatus);


                            String SingleOrder_Numberlist = InventoryHistory_OrderNumber.get(i);
                            Log.e(" ********** SingleOrder_Numberlist **********", "" + SingleOrder_Numberlist);

                            String SingleOrder_Datelist = InventoryHistory_Orderdate.get(i);
                            Log.e(" ********** SingleOrder_Datelist **********", "" + SingleOrder_Datelist);

                            String SingleOrder_Pricelist = InventoryHistory_Orderprice.get(i);
                            Log.e(" ********** SingleOrder_Pricelist **********", "" + SingleOrder_Pricelist);

                            String SingleOrder_Statuslist = InventoryHistory_Orderstatus.get(i);
                            Log.e(" ********** SingleOrder_Statuslist **********", "" + SingleOrder_Statuslist);

                        }
                    }
                }


            } catch (Exception e) {
                System.out.println("Errror Status Is Fail" + e);
                Log.e("22", "22" + e.getMessage());
                e.printStackTrace();
                iserror = true;
            }
            return beanInventoryHistorylist0;
        }

        @Override
        protected void onPostExecute(List<BeanInventoryList> mystring) {
            super.onPostExecute(mystring);
            RL_gridview_inventory_order_progress.setVisibility(View.GONE);

            if (InventoryHistory_OrderNumber.size() > 0) {
                Log.e(" ********** InventoryHistory_OrderNumber Size********** ", "" + InventoryHistory_OrderNumber);
                Log.e(" **********onPostExecute Get_Inventory_list_status**********", "" + Get_Inventory_list_status);
                Log.e(" **********onPostExecute Str_Oder_number**********", "" + Str_Oder_number);
                Log.e(" **********onPostExecute Str_Order_Date List**********", "" + Str_Order_Date);
                Log.e(" **********onPostExecute Str_Order_Total_Price List**********", "" + Str_Order_Total_Price);
                Log.e(" **********onPostExecute Str_Order_Status List**********", "" + Str_Order_Status);


                inventoryHistoryAdapter = new InventoryOderListActivity.InventoryHistoryAdapter(InventoryOderListActivity.this, mystring);
                RV_inventory_order.setAdapter(inventoryHistoryAdapter);


                Log.e(" ********** InventoryHistory_OrderNumber.size() > 0 **********", "YES");
                Log.e(" ********** InventoryHistory_OrderNumber.size() > 0 **********", "YES");
                Log.e(" ********** InventoryHistory_OrderNumber.size() > 0 **********", "YES");
            } else {

                Log.e("InventoryHistory_OrderNumber size is :", "0");

            }
        }

    }


    private class InventoryHistoryAdapter extends RecyclerView.Adapter<InventoryOderListActivity.InventoryHistoryAdapter.MyViewHolder> {

        private Context mContext;
        private List<BeanInventoryList> arrayList;


        class MyViewHolder extends RecyclerView.ViewHolder {
            TextView Order_Number;
            TextView Oder_PRICE;
            TextView Oder_DATE;
            TextView Oder_STATUS;
            final CardView CV_order_detail_btn;
            final CardView CV_order_detail_btn_click;

            public MyViewHolder(View view) {
                super(view);
                Order_Number = (TextView) view.findViewById(R.id.tv_order_no);
                Oder_PRICE = (TextView) view.findViewById(R.id.tv_order_total_price);
                Oder_DATE = (TextView) view.findViewById(R.id.tv_oder_date);
                Oder_STATUS = (TextView) view.findViewById(R.id.tv_oder_status);
                CV_order_detail_btn = (CardView) view.findViewById(R.id.cv_order_detail_btn);
                CV_order_detail_btn_click = (CardView) view.findViewById(R.id.cv_order_detail_btn_click);


            }
        }


        private InventoryHistoryAdapter(Context mContext, List<BeanInventoryList> arrayList) {
            this.mContext = mContext;
            this.arrayList = arrayList;
        }

        @Override
        public InventoryOderListActivity.InventoryHistoryAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View itemView = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.activity_inventory_list_items, parent, false);

            final ItemClickSupport itemClick = ItemClickSupport.addTo(RV_inventory_order);

            itemClick.setOnItemClickListener(new ItemClickSupport.OnItemClickListener() {
                @Override
                public void onItemClicked(RecyclerView recyclerView, int position, View v) {

                    Log.e("Item Clicked :", "" + position);
                }
            });


            itemClick.setOnItemLongClickListener(new ItemClickSupport.OnItemLongClickListener() {
                @Override
                public boolean onItemLongClicked(RecyclerView recyclerView, int position, View v) {

                    Log.e("Item Long Clicked :", "" + position);

                    return true;
                }
            });


            return new InventoryOderListActivity.InventoryHistoryAdapter.MyViewHolder(itemView);
        }

        @Override
        public void onBindViewHolder(final InventoryOderListActivity.InventoryHistoryAdapter.MyViewHolder holder, final int position) {

            holder.Order_Number.setText(Html.fromHtml(arrayList.get(position).getOrderNumber()));
            holder.Oder_PRICE.setText(Html.fromHtml(arrayList.get(position).getOrderprice()));
            holder.Oder_DATE.setText(Html.fromHtml(arrayList.get(position).getOrderdate()));
            holder.Oder_STATUS.setText(Html.fromHtml(arrayList.get(position).getOrderstatus()));

            holder.CV_order_detail_btn.setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View v, MotionEvent event) {

                    if (event.getAction() == MotionEvent.ACTION_DOWN) {

                        Log.e("Action ", "Down");
//                        CV_order_detail_btn_click.setVisibility(View.VISIBLE);
//                        CV_order_detail_btn.setVisibility(View.GONE);
                        v.performHapticFeedback(HapticFeedbackConstants.VIRTUAL_KEY);
//                    Toast.makeText(getApplicationContext(), "Add to cart Clicked", Toast.LENGTH_SHORT).show();

                        return true;
                    }

                    if (event.getAction() == MotionEvent.ACTION_MOVE) {

                        Log.e("Action ", "Move");
//                        Rl_checkout_zoom.setVisibility(View.VISIBLE);
//                        Rl_checkout.setVisibility(View.GONE);
                        return true;

                    }
                    if (event.getAction() == MotionEvent.ACTION_UP) {

                        Log.e("Action ", "Up");
//                        Rl_checkout_zoom.setVisibility(View.GONE);
//                        Rl_checkout.setVisibility(View.VISIBLE);



                        return true;
                    }


                    return false;
                }
            });

        }


        @Override
        public int getItemCount() {
            return arrayList.size();
        }


    }


    /*progressbar data (Start)*/
    private void updateValues() {
        CircularProgressDrawable circularProgressDrawable;
        CircularProgressDrawable.Builder b = new CircularProgressDrawable.Builder(this)
                .colors(getResources().getIntArray(R.array.gplus_colors))
                /*.sweepSpeed(mSpeed)
                .rotationSpeed(mSpeed)
                .strokeWidth(dpToPx(mStrokeWidth))*/
                .style(CircularProgressDrawable.STYLE_ROUNDED);
       /* if (mCurrentInterpolator != null) {
            b.sweepInterpolator(mCurrentInterpolator);
        }*/
        CPB_inventory_order_progressbar_circular.setIndeterminateDrawable(circularProgressDrawable = b.build());

        // /!\ Terrible hack, do not do this at home!
        circularProgressDrawable.setBounds(0,
                0,
                CPB_inventory_order_progressbar_circular.getWidth(),
                CPB_inventory_order_progressbar_circular.getHeight());
        CPB_inventory_order_progressbar_circular.setVisibility(View.INVISIBLE);
        CPB_inventory_order_progressbar_circular.setVisibility(View.VISIBLE);
    }
    /*progressbar data (End)*/









}
