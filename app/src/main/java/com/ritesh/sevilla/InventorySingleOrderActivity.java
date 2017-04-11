package com.ritesh.sevilla;

import android.content.Context;
import android.graphics.Bitmap;
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
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.nispok.snackbar.Snackbar;
import com.nispok.snackbar.SnackbarManager;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;
import com.ritesh.sevilla.Beans.BeanInventorySingleOrder;
import com.ritesh.sevilla.Beans.BeanInventorySingleOrder;
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
import cn.pedant.SweetAlert.SweetAlertDialog;
import fr.castorflex.android.circularprogressbar.CircularProgressBar;
import fr.castorflex.android.circularprogressbar.CircularProgressDrawable;

/**
 * Created by ritesh on 10/4/17.
 */
@SuppressWarnings("deprecation")
public class InventorySingleOrderActivity extends AppCompatActivity {

    @BindView(R.id.toolbar_single_order)
    Toolbar TB_single_order;

    @BindView(R.id.tv_single_order_total_number)
    TextView TV_single_order_total_number;

    @BindView(R.id.tv_single_order_total_items_price)
    TextView TV_single_order_total_items_price;


    List<InventorySingleOrderActivity> inventorySingleOrderrowItems;
    private ArrayList<String> InventorySingleOrder_id;
    private ArrayList<String> InventorySingleOrder_Name;
    private ArrayList<String> InventorySingleOrder_Image;
    private ArrayList<String> InventorySingleOrder_Price;
    private ArrayList<String> InventorySingleOrder_Quantity;
    private ArrayList<String> InventorySingleOrder_DeliveryCharge;
    private ArrayList<String> InventorySingleOrder_DeliveryDate;
    List<BeanInventorySingleOrder> beanInventorySingleOrderlist0;


    @BindView(R.id.rl_listview_single_order_progress)
    RelativeLayout RL_listview_single_order_progress;

    CircularProgressBar mCPB_single_order;

    @BindView(R.id.recycler_view_single_order)
    RecyclerView RV_view_single_order;

//    CategoryAdapter categoryAdapter;

    RecyclerView.LayoutManager mLayoutManager;

    InventorySingleOrderAdapter inventorySingleOrderAdapter;


    String
            User_ID = "",
            SingleOrderID = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inventory_single_detail);
        ButterKnife.bind(this);


        Appconstant.sh = getSharedPreferences(Appconstant.MyPREFERENCES, Context.MODE_PRIVATE);
        User_ID = Appconstant.sh.getString("id", null);
        Log.e("User_ID from SharedPref :", "" + User_ID);


        /*recycler data (Start 02 of 03)*/
        /*image settin by universal image loader */
        DisplayImageOptions defaultOptions = new DisplayImageOptions.Builder().cacheInMemory(true).cacheOnDisk(true).build();
        ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(InventorySingleOrderActivity.this).defaultDisplayImageOptions(defaultOptions).build();
        ImageLoader.getInstance().init(config);
        /*image settin by universal image loader */

         /*circular progress bar (Start)*/
        mCPB_single_order = (CircularProgressBar) findViewById(R.id.cpb_single_order);
//        signupProgress.setVisibility(View.GONE);
        ((CircularProgressDrawable) mCPB_single_order.getIndeterminateDrawable()).start();
        updateValues();
        /*circular progress bar (End)*/


        setSupportActionBar(TB_single_order);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

//        toolbar_sub_category.setNavigationIcon(R.drawable.ic_back_arrow); // your drawable

        TB_single_order.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed(); // Implemented by activity
            }
        });


        inventorySingleOrderrowItems = new ArrayList<InventorySingleOrderActivity>();
        InventorySingleOrder_id = new ArrayList<>();
        InventorySingleOrder_Name = new ArrayList<>();
        InventorySingleOrder_Image = new ArrayList<>();
        InventorySingleOrder_Price = new ArrayList<>();
        InventorySingleOrder_Quantity = new ArrayList<>();
        InventorySingleOrder_DeliveryCharge = new ArrayList<>();
        InventorySingleOrder_DeliveryDate = new ArrayList<>();

        RV_view_single_order.setHasFixedSize(true);

        mLayoutManager = new LinearLayoutManager(this);
        RV_view_single_order.setLayoutManager(mLayoutManager);
        RV_view_single_order.addItemDecoration(new InventorySingleOrderActivity.EqualSpaceItemDecoration(15));




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
        mCPB_single_order.setIndeterminateDrawable(circularProgressDrawable = b.build());

        // /!\ Terrible hack, do not do this at home!
        circularProgressDrawable.setBounds(0,
                0,
                mCPB_single_order.getWidth(),
                mCPB_single_order.getHeight());
        mCPB_single_order.setVisibility(View.INVISIBLE);
        mCPB_single_order.setVisibility(View.VISIBLE);
    }
    /*progressbar data (End)*/

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


    /*private class InventorySingleOrderListJsontask extends AsyncTask<String, Void, List<BeanInventorySingleOrder>> {

        boolean iserror = false;
        String result = " ";

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            RL_listview_single_order_progress.setVisibility(View.VISIBLE);
            Log.e(" ************ MyCartListJsontask AsyncTask Start ************ :", "yes");
            Log.e("USER_ID :", "" + User_ID);
            Log.e("Single Order ID :", "" + SingleOrderID);
        }

        @Override
        protected List<BeanInventorySingleOrder> doInBackground(String... params) {
            HttpClient client = new DefaultHttpClient();
            HttpPost post = new HttpPost("http://sevilla.centrocomercial.com.es/wp-content/plugins/webserv/order_details.php?user_id="+User_ID+"&order_id="+SingleOrderID);

            Log.e("Single Oder List URL :", " " + "http://sevilla.centrocomercial.com.es/wp-content/plugins/webserv/order_details.php?user_id="+User_ID+"&order_id="+SingleOrderID);

            try {
                HttpResponse response = client.execute(post);
                String obj = EntityUtils.toString(response.getEntity());
                beanInventorySingleOrderlist0 = new ArrayList<>();
                Log.e("************ InventorySingleOrderlist ************", "" + obj);

                Log.e("************Json InventorySingleOrderlist data*******************", " " + obj);
                JSONObject jsonObject = new JSONObject(obj);
                status = jsonObject.getString("status");
                Str_My_Cart_product_count = jsonObject.getString("cart_prodcut_count");
                Str_My_Cart_total_price = jsonObject.getString("total_price");
                Str_My_Cart_List_detail = jsonObject.getString("Product_cart_detail");
                Log.e("Json Str_My_Cart_List_detail data :", " " + Str_My_Cart_List_detail);

                JSONArray jaaray = new JSONArray(Str_My_Cart_List_detail);

                if (status.equalsIgnoreCase("OK")) {
                    Log.e("Status is :", "OK");
                    for (int i = 0; i < jaaray.length(); i++) {
                        Str_My_Cart_List_result = jaaray.getJSONObject(i).getString("result");

                        if (Str_My_Cart_List_result.equalsIgnoreCase("successful")) {
                            BeanInventorySingleOrder BeanInventorySingleOrder = new BeanInventorySingleOrder();
                            BeanInventorySingleOrder.setMyCartSingleProductid(jaaray.getJSONObject(i).getString("single_product_id"));
                            BeanInventorySingleOrder.setMyCartSingleProductQuantity(jaaray.getJSONObject(i).getString("item_quantity"));
                            BeanInventorySingleOrder.setMyCartSingleProductName(jaaray.getJSONObject(i).getString("single_product_title"));
                            BeanInventorySingleOrder.setMyCartSingleProductPrice(jaaray.getJSONObject(i).getString("single_product_price"));
                            BeanInventorySingleOrder.setMyCartSingleProductImage(jaaray.getJSONObject(i).getString("single_product_img"));
                            BeanInventorySingleOrder.setMyCartSingleProductDeliveryCharge(jaaray.getJSONObject(i).getString("delivery_price"));
                            BeanInventorySingleOrder.setMyCartSingleProductDeliveryDate(jaaray.getJSONObject(i).getString("delivery_date"));
                            BeanInventorySingleOrder0.add(BeanInventorySingleOrder);


                            Str_My_Cart_List_single_product_id = jaaray.getJSONObject(i).getString("single_product_id");
                            Str_My_Cart_List_single_product_title = jaaray.getJSONObject(i).getString("single_product_title");
                            Str_My_Cart_List_single_product_price = jaaray.getJSONObject(i).getString("single_product_price");
                            Str_My_Cart_List_single_product_img = jaaray.getJSONObject(i).getString("single_product_img");
                            Str_My_Cart_List_single_item_quantity = jaaray.getJSONObject(i).getString("item_quantity");
                            Str_My_Cart_List_DeliveryCharge = jaaray.getJSONObject(i).getString("delivery_price");
                            Str_My_Cart_List_DeliveryDate = jaaray.getJSONObject(i).getString("delivery_date");

                            MyCartSingleProduct_id.add(Str_My_Cart_List_single_product_id);
                            MyCartSingleProduct_Name.add(Str_My_Cart_List_single_product_title);
                            MyCartSingleProduct_Price.add(Str_My_Cart_List_single_product_price);
                            MyCartSingleProduct_Image.add(Str_My_Cart_List_single_product_img);
                            MyCartSingleProduct_Quantity.add(Str_My_Cart_List_single_item_quantity);
                            MyCartSingleProduct_DeliveryCharge.add(Str_My_Cart_List_DeliveryCharge);
                            MyCartSingleProduct_DeliveryDate.add(Str_My_Cart_List_DeliveryDate);

                            Log.e(" ********** MyCartSingleProduct_id List**********", "" + MyCartSingleProduct_id);
                            Log.e(" ********** MyCartSingleProduct_Name List**********", "" + MyCartSingleProduct_Name);
                            Log.e(" ********** MyCartSingleProduct_Price List**********", "" + MyCartSingleProduct_Price);
                            Log.e(" ********** MyCartSingleProduct_Image List**********", "" + MyCartSingleProduct_Image);
                            Log.e(" ********** MyCartSingleProduct_Quantity List**********", "" + MyCartSingleProduct_Quantity);
                            Log.e(" ********** MyCartSingleProduct_DeliveryCharge List**********", "" + MyCartSingleProduct_DeliveryCharge);
                            Log.e(" ********** MyCartSingleProduct_DeliveryDate List**********", "" + MyCartSingleProduct_DeliveryDate);


                            String MyCartSingleProduct_idlist = MyCartSingleProduct_id.get(i);
                            Log.e(" ********** MyCartSingleProduct_idlist **********", "" + MyCartSingleProduct_idlist);

                            String MyCartSingleProduct_Namelist = MyCartSingleProduct_Name.get(i);
                            Log.e(" ********** MyCartSingleProduct_Namelist **********", "" + MyCartSingleProduct_Namelist);

                            String MyCartSingleProduct_Pricelist = MyCartSingleProduct_Price.get(i);
                            Log.e(" ********** MyCartSingleProduct_Pricelist **********", "" + MyCartSingleProduct_Pricelist);

                            String MyCartSingleProduct_Imagelist = MyCartSingleProduct_Image.get(i);
                            Log.e(" ********** MyCartSingleProduct_Imagelist **********", "" + MyCartSingleProduct_Imagelist);

                            String MyCartSingleProduct_Quantitylist = MyCartSingleProduct_Quantity.get(i);
                            Log.e(" ********** MyCartSingleProduct_Quantitylist **********", "" + MyCartSingleProduct_Quantitylist);

                            String MyCartSingleProduct_DeliveryChargelist = MyCartSingleProduct_DeliveryCharge.get(i);
                            Log.e(" ********** MyCartSingleProduct_DeliveryChargelist **********", "" + MyCartSingleProduct_DeliveryChargelist);

                            String MyCartSingleProduct_DeliveryDatelist = MyCartSingleProduct_DeliveryDate.get(i);
                            Log.e(" ********** MyCartSingleProduct_DeliveryDatelist **********", "" + MyCartSingleProduct_DeliveryDatelist);
                        }
                    }
                }


            } catch (Exception e) {
                System.out.println("Errror Status Is Fail" + e);
                Log.e("22", "22" + e.getMessage());
                e.printStackTrace();
                iserror = true;
            }
            return beanInventorySingleOrderlist0;
        }

        @Override
        protected void onPostExecute(List<BeanInventorySingleOrder> mystring) {
            super.onPostExecute(mystring);
            RL_listview_single_order_progress.setVisibility(View.GONE);

            if (MyCartSingleProduct_id.size() > 0) {
                Log.e(" ********** MyCartSingleProduct_id Size********** ", "" + MyCartSingleProduct_id);

                Log.e(" **********onPostExecute Str_My_Cart_product_count**********", "" + Str_My_Cart_product_count);
                Log.e(" **********onPostExecute Str_My_Cart_total_price**********", "" + Str_My_Cart_total_price);

                Log.e(" **********onPostExecute MyCartSingleProduct_id List**********", "" + MyCartSingleProduct_id);
                Log.e(" **********onPostExecute MyCartSingleProduct_Name List**********", "" + MyCartSingleProduct_Name);
                Log.e(" **********onPostExecute MyCartSingleProduct_Price List**********", "" + MyCartSingleProduct_Price);
                Log.e(" **********onPostExecute MyCartSingleProduct_Image List**********", "" + MyCartSingleProduct_Image);
                Log.e(" **********onPostExecute MyCartSingleProduct_Quantity List**********", "" + MyCartSingleProduct_Quantity);
                Log.e(" **********onPostExecute MyCartSingleProduct_DeliveryCharge List**********", "" + MyCartSingleProduct_DeliveryCharge);
                Log.e(" **********onPostExecute MyCartSingleProduct_DeliveryDate List**********", "" + MyCartSingleProduct_DeliveryDate);


                TV_total_number.setText(Html.fromHtml(Str_My_Cart_product_count));
                TV_total_items_price.setText(Html.fromHtml("\u20ac" + " " + Str_My_Cart_total_price));


                categoryAdapter = new MyCartActivity.CategoryAdapter(MyCartActivity.this, mystring);
                mycartlist_recylerView.setAdapter(categoryAdapter);


                Log.e(" ********** listvideoid.size() > 0 **********", "YES");
                Log.e(" ********** listvideoid.size() > 0 **********", "YES");
                Log.e(" ********** listvideoid.size() > 0 **********", "YES");
            } else {

                Log.e("cat_id size is :", "0");

            }
        }

    }*/


    private class InventorySingleOrderAdapter extends RecyclerView.Adapter<InventorySingleOrderActivity.InventorySingleOrderAdapter.MyViewHolder> {

        private Context mContext;
        private List<BeanInventorySingleOrder> arrayList;


        class MyViewHolder extends RecyclerView.ViewHolder {
            TextView InventorySingleOrderItem_Name;
            TextView InventorySingleOrderItem_quantity;
            TextView InventorySingleOrderItem_price;
            TextView InventorySingleOrderItem_Delivery_price;
            TextView InventorySingleOrderItem_Delivery_date;
            ImageView InventorySingleOrderItem_image;
            RelativeLayout Rl_InventorySingleOrderItem_image_loader;
            CircularProgressBar CPB_MyCartItem_image_circular_loader;

            private MyViewHolder(View view) {
                super(view);
                InventorySingleOrderItem_Name = (TextView) view.findViewById(R.id.tv_single_order_single_product_name);
                InventorySingleOrderItem_quantity = (TextView) view.findViewById(R.id.tv_single_order_item_quantity_number);
                InventorySingleOrderItem_price = (TextView) view.findViewById(R.id.tv_single_order_single_item_price);
                InventorySingleOrderItem_Delivery_price = (TextView) view.findViewById(R.id.tv_single_order_delivery_charges);
                InventorySingleOrderItem_Delivery_date = (TextView) view.findViewById(R.id.tv_single_order_delivery_date);
                CPB_MyCartItem_image_circular_loader = (CircularProgressBar) view.findViewById(R.id.cpb_single_order_grid_item_image_progressbar_circular);
                Rl_InventorySingleOrderItem_image_loader = (RelativeLayout) view.findViewById(R.id.rl_single_order_list_item_image_progress);
                InventorySingleOrderItem_image = (ImageView) view.findViewById(R.id.iv_single_order_item);


            }
        }


        private InventorySingleOrderAdapter(Context mContext, List<BeanInventorySingleOrder> arrayList) {
            this.mContext = mContext;
            this.arrayList = arrayList;
        }

        @Override
        public InventorySingleOrderActivity.InventorySingleOrderAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View itemView = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.activity_inventory_single_detail_list, parent, false);

            final ItemClickSupport itemClick = ItemClickSupport.addTo(RV_view_single_order);

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


            return new InventorySingleOrderActivity.InventorySingleOrderAdapter.MyViewHolder(itemView);
        }

        @Override
        public void onBindViewHolder(final InventorySingleOrderActivity.InventorySingleOrderAdapter.MyViewHolder holder, final int position) {

            holder.InventorySingleOrderItem_Name.setText(Html.fromHtml(arrayList.get(position).getInventorySingleOrderName()));
            holder.InventorySingleOrderItem_quantity.setText(Html.fromHtml(arrayList.get(position).getInventorySingleOrderQuantity()));
            holder.InventorySingleOrderItem_price.setText(Html.fromHtml(arrayList.get(position).getInventorySingleOrderPrice()));
            holder.InventorySingleOrderItem_price.setText(Html.fromHtml(arrayList.get(position).getInventorySingleOrderDeliveryCharge()));
            holder.InventorySingleOrderItem_Delivery_date.setText(Html.fromHtml(arrayList.get(position).getInventorySingleOrderDeliveryDate()));

            ImageLoader imageLoader = ImageLoader.getInstance();
            imageLoader.getInstance().displayImage(arrayList.get(position).getInventorySingleOrderImage(), holder.InventorySingleOrderItem_image, new ImageLoadingListener() {
                @Override
                public void onLoadingStarted(String imageUri, View view) {
                    holder.Rl_InventorySingleOrderItem_image_loader.setVisibility(View.VISIBLE);
                }

                @Override
                public void onLoadingFailed(String imageUri, View view, FailReason failReason) {
                    holder.Rl_InventorySingleOrderItem_image_loader.setVisibility(View.GONE);
                }

                @Override
                public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {
                    holder.Rl_InventorySingleOrderItem_image_loader.setVisibility(View.GONE);
                }

                @Override
                public void onLoadingCancelled(String imageUri, View view) {
                    holder.Rl_InventorySingleOrderItem_image_loader.setVisibility(View.GONE);

                }
            });




            /*itemClick.setOnItemClickListener(new OnItemClickListener() {
                @Override
                public void onItemClick(RecyclerView parent, View child, int position, long id) {
                    mToast.setText("Item clicked: " + position);
                    mToast.show();
                }
            });

            itemClick.setOnItemLongClickListener(new OnItemLongClickListener() {
                @Override
                public boolean onItemLongClick(RecyclerView parent, View child, int position, long id) {
                    mToast.setText("Item long pressed: " + position);
                    mToast.show();
                    return true;
                }
            });*/

            /*holder.CV_DeleteItem.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    new SweetAlertDialog(InventorySingleOrderActivity.this, SweetAlertDialog.WARNING_TYPE)
                            .setTitleText("Are you sure?")
                            .setContentText("Won't be able to recover this Action!")
                            .setCancelText("No,cancel..!")
                            .setConfirmText("Yes,Remove!")
                            .showCancelButton(true)
                            .setCancelClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                @Override
                                public void onClick(SweetAlertDialog sDialog) {

                                    Log.e("Cancell Click :", "OK");
                                    // reuse previous dialog instance, keep widget user state, reset them if you need
                                    sDialog.setTitleText("Cancelled!")
                                            .setContentText("Your Item is safe :)")
                                            .setConfirmText("OK")
                                            .showCancelButton(false)
                                            .setCancelClickListener(null)
                                            .setConfirmClickListener(null)
                                            .changeAlertType(SweetAlertDialog.ERROR_TYPE);
                                }
                            })
                            .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                @Override
                                public void onClick(SweetAlertDialog sDialog) {
                                    Log.e("Deleted Click :", "OK");
                                    int position = holder.getAdapterPosition();
                                    Log.e("CV_DeleteItem Item Position :", "" + arrayList.get(holder.getLayoutPosition()));
                                    Log.e("CV_DeleteItem Adapter Position :", "" + position);

                                    Str_My_Cart_List_single_product_id = arrayList.get(position).getMyCartSingleProductid();
                                    Log.e("CV_DeleteItem Item ID Str_My_Cart_List_single_product_id :", "" + Str_My_Cart_List_single_product_id);
                                    Str_My_Cart_List_single_product_title = arrayList.get(position).getMyCartSingleProductName();
                                    Log.e("CV_DeleteItem Item TITTLE Str_My_Cart_List_single_product_title :", "" + Str_My_Cart_List_single_product_title);


                                    categoryAdapter.deleteItem(position);  //https://github.com/thedeveloperworldisyours/FullRecycleView (FOR REMOVING ITEM ON LIST)

//                                    Toast.makeText(getApplicationContext(), "Comming Soon", Toast.LENGTH_SHORT).show();

                                    if (Utils.isConnected(getApplicationContext())) {
                                        MyCartActivity.MyCartListItemDeleteJsontask task = new MyCartActivity.MyCartListItemDeleteJsontask();
                                        task.execute();
                                    } else {

                                        SnackbarManager.show(
                                                Snackbar.with(MyCartActivity.this)
                                                        .position(Snackbar.SnackbarPosition.TOP)
                                                        .margin(15, 15)
                                                        .backgroundDrawable(R.drawable.snackbar_custom_layout)
                                                        .text("Please Your Internet Connectivity..!!"));

                                    }
                                    sDialog.dismiss();
                                }
                            })
                            .show();

                }
            });*/


            /*holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int position = holder.getAdapterPosition();

//                    holder.Catimage_transparent_overlay.setVisibility(View.GONE);
                    Log.e(" Item Position :", "" + arrayList.get(holder.getLayoutPosition()));

                    Log.e(" Adapter Position :", "" + position);

                    Log.e("Item Name Str_My_Cart_List_single_product_id", "" + arrayList.get(position).getMyCartSingleProductid());
                    Str_My_Cart_List_single_product_id = arrayList.get(position).getMyCartSingleProductid();
                    Log.e("Item ID Str_My_Cart_List_single_product_id :", "" + Str_My_Cart_List_single_product_id);

                    Str_My_Cart_List_single_product_title = String.valueOf(Html.fromHtml(arrayList.get(position).getMyCartSingleProductName()));

                    Log.e("Item Name Str_My_Cart_List_single_product_title", "" + String.valueOf(Html.fromHtml(arrayList.get(position).getMyCartSingleProductName())));

                    Log.e("Item Name Str_My_Cart_List_single_product_title", "" + Str_My_Cart_List_single_product_title);

                    Log.e(" List Size :", "" + arrayList.size());



                   *//* Intent SubCatPage = new Intent(getApplicationContext(), SubCategoryActivity.class);
                    SubCatPage.putExtra("SubCatID", arrayList.get(position).getId());
                    SubCatPage.putExtra("MainCatID", MainCategory_ID);
                    SubCatPage.putExtra("SubCatName", Category_Name_for_Sub_Category);
//                    SubCatPage.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                    MainActivity.this.startActivity(SubCatPage);
//                    finish();*//*

                }
            });*/


        }


        @Override
        public int getItemCount() {
            return arrayList.size();
        }


    }


}
