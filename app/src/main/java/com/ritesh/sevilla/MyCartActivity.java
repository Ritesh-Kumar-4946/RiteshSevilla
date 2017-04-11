package com.ritesh.sevilla;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
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
import android.view.HapticFeedbackConstants;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.nispok.snackbar.Snackbar;
import com.nispok.snackbar.SnackbarManager;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;
import com.paypal.android.sdk.payments.PayPalConfiguration;
import com.paypal.android.sdk.payments.PayPalPayment;
import com.paypal.android.sdk.payments.PayPalService;
import com.paypal.android.sdk.payments.PaymentActivity;
import com.paypal.android.sdk.payments.PaymentConfirmation;
import com.ritesh.sevilla.Beans.BeanMyCartList;
import com.ritesh.sevilla.Constant.Appconstant;
import com.ritesh.sevilla.Constant.Utils;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import cn.pedant.SweetAlert.SweetAlertDialog;
import fr.castorflex.android.circularprogressbar.CircularProgressBar;
import fr.castorflex.android.circularprogressbar.CircularProgressDrawable;

/**
 * Created by ritesh on 25/2/17.
 */
@SuppressWarnings("deprecation")
public class MyCartActivity extends AppCompatActivity {

    @BindView(R.id.toolbar_my_cart)
    Toolbar TB_my_cart;

    @BindView(R.id.rl_sub_category_button_cart)
    RelativeLayout Rl_checkout;

    @BindView(R.id.rl_sub_category_button_cart_zoon)
    RelativeLayout Rl_checkout_zoom;

    @BindView(R.id.tv_total_number)
    TextView TV_total_number;

    @BindView(R.id.tv_total_items_price)
    TextView TV_total_items_price;

    String
            Str_Get_DeleteCart_Status = "",
            Str_Get_DeleteCart_Message = "",
            User_ID = "",
            Str_My_Cart_List_detail = "",
            Str_My_Cart_product_count = "",
            Str_My_Cart_total_price = "",
            Str_My_Cart_List_result = "",
            Str_My_Cart_List_item_quantity = "",
            Str_My_Cart_List_single_product_title = "",
            Str_My_Cart_List_single_product_id = "",
            Str_My_Cart_List_single_product_price = "",
            Str_My_Cart_List_single_product_img = "",
            Str_My_Cart_List_single_item_quantity = "",
            Str_My_Cart_List_DeliveryCharge = "",
            Str_My_Cart_List_DeliveryDate = "",
            status = "";


    String
            Get_user_address_ID = "",
            Get_user_address_status = "",
            Get_user_address_result = "",
            Get_user_address_F_name = "",
            Get_user_address_L_name = "",
            Get_user_address_address = "",
            Get_user_address_phone_number = "",
            Get_user_address_country = "",
            Get_user_address_state = "",
            Get_user_address_city = "",
            Get_user_address_zipcode = "",
            Get_user_address_message = "";


    List<MyCartActivity> mycartlistrowItems;
    private ArrayList<String> MyCartSingleProduct_id;
    private ArrayList<String> MyCartSingleProduct_Name;
    private ArrayList<String> MyCartSingleProduct_Image;
    private ArrayList<String> MyCartSingleProduct_Price;
    private ArrayList<String> MyCartSingleProduct_Quantity;
    private ArrayList<String> MyCartSingleProduct_DeliveryCharge;
    private ArrayList<String> MyCartSingleProduct_DeliveryDate;
    List<BeanMyCartList> beanMyCartlist0;

    @BindView(R.id.rl_listview_my_cart_progress)
    RelativeLayout pv_gridview_mycartlist_progressview;

    CircularProgressBar mCircularProgressBarGridview_mycartlist;

    @BindView(R.id.recycler_view_my_cart)
    RecyclerView mycartlist_recylerView;

    CategoryAdapter categoryAdapter;

    RecyclerView.LayoutManager mLayoutManager;

    public static final int PAYPAL_REQUEST_CODE = 123;
    //Paypal Configuration Object
    private static PayPalConfiguration config = new PayPalConfiguration()
            // Start with mock environment.  When ready, switch to sandbox (ENVIRONMENT_SANDBOX)
            // or live (ENVIRONMENT_PRODUCTION)
            .environment(PayPalConfiguration.ENVIRONMENT_SANDBOX)
            .clientId(PayPalConfig.PAYPAL_CLIENT_ID);


    /*Another double click to exit on back pressed 01- 02 (Start)*/
    boolean doubleBackToExitPressedOnce = false;
    /*Another double click to exit on back pressed 01- 02 (End)*/

    /*double click to exit on back pressed 01- 02 (Start)*/
    /*private boolean doubleBackToExitPressedOnce;
    private Handler mHandler = new Handler();*/
    /*double click to exit on back pressed 01- 02 (End)*/

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_cart);
        ButterKnife.bind(this);

        Appconstant.sh = getSharedPreferences(Appconstant.MyPREFERENCES, Context.MODE_PRIVATE);
        User_ID = Appconstant.sh.getString("id", null);
        Log.e("User_ID from SharedPref :", "" + User_ID);


        Intent intent = new Intent(this, PayPalService.class);
        intent.putExtra(PayPalService.EXTRA_PAYPAL_CONFIGURATION, config);
        startService(intent);

        /*recycler data (Start 02 of 03)*/
        /*image settin by universal image loader */
        DisplayImageOptions defaultOptions = new DisplayImageOptions.Builder().cacheInMemory(true).cacheOnDisk(true).build();
        ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(MyCartActivity.this).defaultDisplayImageOptions(defaultOptions).build();
        ImageLoader.getInstance().init(config);
        /*image settin by universal image loader */

         /*circular progress bar (Start)*/
        mCircularProgressBarGridview_mycartlist = (CircularProgressBar) findViewById(R.id.listview_my_cart_progressbar_circular);
//        signupProgress.setVisibility(View.GONE);
        ((CircularProgressDrawable) mCircularProgressBarGridview_mycartlist.getIndeterminateDrawable()).start();
        updateValues();
        /*circular progress bar (End)*/


        setSupportActionBar(TB_my_cart);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

//        toolbar_sub_category.setNavigationIcon(R.drawable.ic_back_arrow); // your drawable

        TB_my_cart.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed(); // Implemented by activity
            }
        });


        // getPayment();

        mycartlistrowItems = new ArrayList<MyCartActivity>();
        MyCartSingleProduct_id = new ArrayList<>();
        MyCartSingleProduct_Name = new ArrayList<>();
        MyCartSingleProduct_Image = new ArrayList<>();
        MyCartSingleProduct_Price = new ArrayList<>();
        MyCartSingleProduct_Quantity = new ArrayList<>();
        MyCartSingleProduct_DeliveryCharge = new ArrayList<>();
        MyCartSingleProduct_DeliveryDate = new ArrayList<>();

        mycartlist_recylerView.setHasFixedSize(true);

        mLayoutManager = new LinearLayoutManager(this);
        mycartlist_recylerView.setLayoutManager(mLayoutManager);
        mycartlist_recylerView.addItemDecoration(new MyCartActivity.EqualSpaceItemDecoration(15));


        if (Utils.isConnected(getApplicationContext())) {
            MyCartListJsontask task = new MyCartListJsontask();
            task.execute();
        } else {

            SnackbarManager.show(
                    Snackbar.with(MyCartActivity.this)
                            .position(Snackbar.SnackbarPosition.TOP)
                            .margin(15, 15)
                            .backgroundDrawable(R.drawable.snackbar_custom_layout)
                            .text("Please Your Internet Connectivity..!!"));

        }

//        Rl_checkout.setClickable(false);

        Rl_checkout.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {

                if (event.getAction() == MotionEvent.ACTION_DOWN) {

                    Log.e("Action ", "Down");
                    Rl_checkout_zoom.setVisibility(View.VISIBLE);
                    Rl_checkout.setVisibility(View.GONE);
                    v.performHapticFeedback(HapticFeedbackConstants.VIRTUAL_KEY);
//                    Toast.makeText(getApplicationContext(), "Add to cart Clicked", Toast.LENGTH_SHORT).show();

                    return true;
                }

                if (event.getAction() == MotionEvent.ACTION_MOVE) {

                    Log.e("Action ", "Move");
                    Rl_checkout_zoom.setVisibility(View.VISIBLE);
                    Rl_checkout.setVisibility(View.GONE);
                    return true;

                }
                if (event.getAction() == MotionEvent.ACTION_UP) {

                    Log.e("Action ", "Up");
                    Rl_checkout_zoom.setVisibility(View.GONE);
                    Rl_checkout.setVisibility(View.VISIBLE);
//                    getPayment();     // paypal integration for one to one payment

                    if (Utils.isConnected(getApplication())) {
                        UserGetAddressJsontask task = new UserGetAddressJsontask();
                        task.execute();
                    } else {

                        SnackbarManager.show(
                                Snackbar.with(MyCartActivity.this)
                                        .position(Snackbar.SnackbarPosition.TOP)
                                        .margin(15, 15)
                                        .backgroundDrawable(R.drawable.snackbar_custom_layout)
                                        .text("Please Your Internet Connectivity..!!"));

                    }
                    return true;
                }


                return false;
            }
        });


    }


    @Override
    protected void onStart() {
        super.onStart();
        Log.e("MyCartActivity Activity lifecycle", "onStart invoked");
    }


    @Override
    protected void onResume() {
        super.onResume();
        Log.e("MyCartActivity Activity lifecycle", "onResume invoked");

    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.e("MyCartActivity Activity lifecycle", "onPause invoked");
    }

    @Override
    protected void onStop() {
        super.onStop();
        Log.e("MyCartActivity Activity lifecycle", "onStop invoked");
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        Log.e("MyCartActivity Activity lifecycle", "onRestart invoked");
    }

    /*@Override
    protected void onDestroy() {
        super.onDestroy();
        Log.e("MyCartActivity Activity lifecycle", "onDestroy invoked");
    }*/


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
        mCircularProgressBarGridview_mycartlist.setIndeterminateDrawable(circularProgressDrawable = b.build());

        // /!\ Terrible hack, do not do this at home!
        circularProgressDrawable.setBounds(0,
                0,
                mCircularProgressBarGridview_mycartlist.getWidth(),
                mCircularProgressBarGridview_mycartlist.getHeight());
        mCircularProgressBarGridview_mycartlist.setVisibility(View.INVISIBLE);
        mCircularProgressBarGridview_mycartlist.setVisibility(View.VISIBLE);
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


    private class MyCartListJsontask extends AsyncTask<String, Void, List<BeanMyCartList>> {

        boolean iserror = false;
        String result = " ";

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pv_gridview_mycartlist_progressview.setVisibility(View.VISIBLE);
            Log.e(" ************ MyCartListJsontask AsyncTask Start ************ :", "yes");
            Log.e("USER_ID :", "" + User_ID);
        }

        @Override
        protected List<BeanMyCartList> doInBackground(String... params) {
            HttpClient client = new DefaultHttpClient();
            HttpPost post = new HttpPost("http://sevilla.centrocomercial.com.es/wp-content/plugins/webserv/get_cart_product_list.php?user_id=" + User_ID);

            Log.e(" URL :", " " + "http://sevilla.centrocomercial.com.es/wp-content/plugins/webserv/get_cart_product_list.php?user_id=" + User_ID);

            try {
                HttpResponse response = client.execute(post);
                String obj = EntityUtils.toString(response.getEntity());
                beanMyCartlist0 = new ArrayList<>();
                Log.e("************ list ************", "" + obj);

                Log.e("************Json data*******************", " " + obj);
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
                            BeanMyCartList beanMyCartList = new BeanMyCartList();
                            beanMyCartList.setMyCartSingleProductid(jaaray.getJSONObject(i).getString("single_product_id"));
                            beanMyCartList.setMyCartSingleProductQuantity(jaaray.getJSONObject(i).getString("item_quantity"));
                            beanMyCartList.setMyCartSingleProductName(jaaray.getJSONObject(i).getString("single_product_title"));
                            beanMyCartList.setMyCartSingleProductPrice(jaaray.getJSONObject(i).getString("single_product_price"));
                            beanMyCartList.setMyCartSingleProductImage(jaaray.getJSONObject(i).getString("single_product_img"));
                            beanMyCartList.setMyCartSingleProductDeliveryCharge(jaaray.getJSONObject(i).getString("delivery_price"));
                            beanMyCartList.setMyCartSingleProductDeliveryDate(jaaray.getJSONObject(i).getString("delivery_date"));
                            beanMyCartlist0.add(beanMyCartList);


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
            return beanMyCartlist0;
        }

        @Override
        protected void onPostExecute(List<BeanMyCartList> mystring) {
            super.onPostExecute(mystring);
            pv_gridview_mycartlist_progressview.setVisibility(View.GONE);

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

    }


    private class CategoryAdapter extends RecyclerView.Adapter<MyCartActivity.CategoryAdapter.MyViewHolder> {

        private Context mContext;
        private List<BeanMyCartList> arrayList;


        class MyViewHolder extends RecyclerView.ViewHolder {
            TextView MyCartItem_Tittle;
            TextView MyCartItem_quantity;
            TextView MyCartItem_price;
            TextView MyCartItem_Delivery_price;
            TextView MyCartItem_Delivery_date;
            ImageView MyCartItem_image;
            RelativeLayout Rl_MyCartItem_image_loader;
            CardView CV_DeleteItem;
            CircularProgressBar CPB_MyCartItem_image_circular_loader;

            public MyViewHolder(View view) {
                super(view);
                MyCartItem_Tittle = (TextView) view.findViewById(R.id.tv_my_cart_single_product_name);
                MyCartItem_quantity = (TextView) view.findViewById(R.id.tv_item_quantity_number);
                MyCartItem_price = (TextView) view.findViewById(R.id.tv_my_cart_single_item);
                MyCartItem_Delivery_price = (TextView) view.findViewById(R.id.tv_my_cart_delivery_charges);
                MyCartItem_Delivery_date = (TextView) view.findViewById(R.id.tv_my_cart_delivery_date);
                CPB_MyCartItem_image_circular_loader = (CircularProgressBar) view.findViewById(R.id.cpb_my_cart_grid_item_image_progressbar_circular);
                Rl_MyCartItem_image_loader = (RelativeLayout) view.findViewById(R.id.rl_my_cart_grid_item_image_progress);
                MyCartItem_image = (ImageView) view.findViewById(R.id.iv_my_cart_item);
                CV_DeleteItem = (CardView) view.findViewById(R.id.cv_my_cart_remove_item);


            }
        }


        private CategoryAdapter(Context mContext, List<BeanMyCartList> arrayList) {
            this.mContext = mContext;
            this.arrayList = arrayList;
        }

        @Override
        public MyCartActivity.CategoryAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View itemView = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.activity_my_cart_list_items, parent, false);

            final ItemClickSupport itemClick = ItemClickSupport.addTo(mycartlist_recylerView);

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


            return new MyCartActivity.CategoryAdapter.MyViewHolder(itemView);
        }

        @Override
        public void onBindViewHolder(final MyCartActivity.CategoryAdapter.MyViewHolder holder, final int position) {

            holder.MyCartItem_Tittle.setText(Html.fromHtml(arrayList.get(position).getMyCartSingleProductName()));
            holder.MyCartItem_quantity.setText(Html.fromHtml(arrayList.get(position).getMyCartSingleProductQuantity()));
            holder.MyCartItem_price.setText(Html.fromHtml(arrayList.get(position).getMyCartSingleProductPrice()));
            holder.MyCartItem_Delivery_price.setText(Html.fromHtml(arrayList.get(position).getMyCartSingleProductDeliveryCharge()));
            holder.MyCartItem_Delivery_date.setText(Html.fromHtml(arrayList.get(position).getMyCartSingleProductDeliveryDate()));

            ImageLoader imageLoader = ImageLoader.getInstance();
            imageLoader.getInstance().displayImage(arrayList.get(position).getMyCartSingleProductImage(), holder.MyCartItem_image, new ImageLoadingListener() {
                @Override
                public void onLoadingStarted(String imageUri, View view) {
                    holder.Rl_MyCartItem_image_loader.setVisibility(View.VISIBLE);
                }

                @Override
                public void onLoadingFailed(String imageUri, View view, FailReason failReason) {
                    holder.Rl_MyCartItem_image_loader.setVisibility(View.GONE);
                }

                @Override
                public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {
                    holder.Rl_MyCartItem_image_loader.setVisibility(View.GONE);
                }

                @Override
                public void onLoadingCancelled(String imageUri, View view) {
                    holder.Rl_MyCartItem_image_loader.setVisibility(View.GONE);

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

            holder.CV_DeleteItem.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    new SweetAlertDialog(MyCartActivity.this, SweetAlertDialog.WARNING_TYPE)
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
                                        MyCartListItemDeleteJsontask task = new MyCartListItemDeleteJsontask();
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
            });


            holder.itemView.setOnClickListener(new View.OnClickListener() {
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



                   /* Intent SubCatPage = new Intent(getApplicationContext(), SubCategoryActivity.class);
                    SubCatPage.putExtra("SubCatID", arrayList.get(position).getId());
                    SubCatPage.putExtra("MainCatID", MainCategory_ID);
                    SubCatPage.putExtra("SubCatName", Category_Name_for_Sub_Category);
//                    SubCatPage.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                    MainActivity.this.startActivity(SubCatPage);
//                    finish();*/

                }
            });


        }


        @Override
        public int getItemCount() {
            return arrayList.size();
        }

        public void add(BeanMyCartList item, int position) {
            arrayList.add(position, item);
            notifyItemInserted(position);
        }

        public void remove(BeanMyCartList item) {
            int position = arrayList.indexOf(item);
            arrayList.remove(position);
            notifyItemRemoved(position);
        }


        void deleteItem(int index) {
            arrayList.remove(index);
            notifyItemRemoved(index);
        }


    }


    private class MyCartListItemDeleteJsontask extends AsyncTask<String, Void, String> {

        boolean iserror = false;

        @Override
        protected void onPreExecute() {
            // TODO Auto-generated method stub
            //  loginprogressbar.setVisibility(View.VISIBLE);
            Log.e("******* NOW MyCartListItemDeleteJsontask WEB SERVICE IS RUNNING *******", "YES");
            Log.e("******* NOW MyCartListItemDeleteJsontask WEB SERVICE IS RUNNING *******", "YES");
            Log.e("User_ID onPreExecute :", "" + User_ID);

            pv_gridview_mycartlist_progressview.setVisibility(View.VISIBLE);
            Log.e("Str_My_Cart_List_single_product_id onPreExecute :", "" + Str_My_Cart_List_single_product_id);

        }

        @Override
        protected String doInBackground(String... params) {
            Log.e("******* NOW MyCartListItemDeleteJsontask IS doInBackground RUNNING *******", "YES");
            HttpClient client = new DefaultHttpClient();
            HttpPost post = new HttpPost("http://sevilla.centrocomercial.com.es/wp-content/plugins/webserv/delete_cart.php?product_id=" + Str_My_Cart_List_single_product_id + "&user_id=" + User_ID);


            Log.e("URL Cart Detail MyCartListItemDeleteJsontask :", "" + "http://sevilla.centrocomercial.com.es/wp-content/plugins/webserv/delete_cart.php?product_id=" + Str_My_Cart_List_single_product_id + "&user_id=" + User_ID);

            try {
                HttpResponse response = client.execute(post);
                String CartDeleteobject = EntityUtils.toString(response.getEntity());
                Log.e("*******Cart Item Delete object******** :", "" + CartDeleteobject);

                //JSONArray js = new JSONArray(object);
                JSONObject jobect = new JSONObject(CartDeleteobject);
                Str_Get_DeleteCart_Status = jobect.getString("status");
                if (Str_Get_DeleteCart_Status.equalsIgnoreCase("OK")) {
                    Str_Get_DeleteCart_Message = jobect.getString("message");

                }

            } catch (Exception e) {
                Log.v("22", "22" + e.getMessage());
                e.printStackTrace();
                iserror = true;
            }

            return Str_Get_DeleteCart_Status;
        }

        @Override
        protected void onPostExecute(String result1) {
            // TODO Auto-generated method stub
            super.onPostExecute(result1);
            pv_gridview_mycartlist_progressview.setVisibility(View.GONE);
            if (!iserror) {
                if (Str_Get_DeleteCart_Status.equalsIgnoreCase("OK")) {


                    new SweetAlertDialog(MyCartActivity.this, SweetAlertDialog.SUCCESS_TYPE)
                            .setTitleText("Item Removed")
                            .setContentText("Your Cart is updated!")
                            .show();

                    Log.e("Str_Get_DeleteCart_Status :", "" + Str_Get_DeleteCart_Status);
                    Log.e("Str_Get_DeleteCart_Message :", "" + Str_Get_DeleteCart_Message);


                    if (Utils.isConnected(getApplicationContext())) {
                        MyCartListJsontask task = new MyCartListJsontask();
                        task.execute();
                    } else {

                        SnackbarManager.show(
                                Snackbar.with(MyCartActivity.this)
                                        .position(Snackbar.SnackbarPosition.TOP)
                                        .margin(15, 15)
                                        .backgroundDrawable(R.drawable.snackbar_custom_layout)
                                        .text("Please Your Internet Connectivity..!!"));

                    }
//                    categoryAdapter.notifyDataSetChanged();

                } else {
                    Log.e("onPostExecute Error ", "ooppss");
                    SnackbarManager.show(
                            Snackbar.with(MyCartActivity.this)
                                    .position(Snackbar.SnackbarPosition.TOP)
                                    .margin(15, 15)
                                    .backgroundDrawable(R.drawable.snackbar_custom_layout)
                                    .text("No Data Found"));
                }
            }
        }

    }


    private void getPayment() {
        //Getting the amount from editText
        //Creating a paypalpayment
        PayPalPayment payment = new PayPalPayment(new BigDecimal(Str_My_Cart_total_price), "USD", "Amount Payable For Sevilla", PayPalPayment.PAYMENT_INTENT_SALE);

        //Creating Paypal Payment activity intent
        Intent intent = new Intent(this, PaymentActivity.class);

        //putting the paypal configuration to the intent
        intent.putExtra(PayPalService.EXTRA_PAYPAL_CONFIGURATION, config);

        //Puting paypal payment to the intent
        intent.putExtra(PaymentActivity.EXTRA_PAYMENT, payment);

        //Starting the intent activity for result
        //the request code will be used on the method onActivityResult
        startActivityForResult(intent, PAYPAL_REQUEST_CODE);

    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        //If the result is from paypal
        if (requestCode == PAYPAL_REQUEST_CODE) {

            //If the result is OK i.e. user has not canceled the payment
            if (resultCode == Activity.RESULT_OK) {
                //Getting the payment confirmation
                PaymentConfirmation confirm = data.getParcelableExtra(PaymentActivity.EXTRA_RESULT_CONFIRMATION);

                //if confirmation is not null
                if (confirm != null) {
                    try {
                        //Getting the payment details
                        String paymentDetails = confirm.toJSONObject().toString();
                        Log.e("paymentExample :", "" + paymentDetails);

/*
                        {
                            "response": {
                            "state": "approved",
                                    "id": "PAY-4H348668VK615112JK7N7DEY",
                                    "create_time": "2016-09-16T13:20:27Z",
                                    "intent": "sale"
                        },
                            "client": {
                            "platform": "Android",
                                    "paypal_sdk_version": "2.14.5",
                                    "product_name": "PayPal-Android-SDK",
                                    "environment": "sandbox"
                        },
                            "response_type": "payment"
                        }*/


                        JSONObject ParentObject = new JSONObject(paymentDetails);
                        JSONObject ResponseObject = ParentObject.getJSONObject("response");

                        String Status = ResponseObject.getString("state");
                        String id = ResponseObject.getString("id");
                        String create_time = ResponseObject.getString("create_time");


                        if (Status.equalsIgnoreCase("approved")) {
                            Log.e("Approved", "YES");
//                            Toast.makeText(BuyCoinsActivity.this, "Amount Has Been Paid", Toast.LENGTH_SHORT).show();

                            SnackbarManager.show(
                                    Snackbar.with(MyCartActivity.this)
                                            .position(Snackbar.SnackbarPosition.TOP)
                                            .margin(15, 15)
                                            .backgroundDrawable(R.drawable.snackbar_custom_layout)
                                            .text("Payment Complete..!!"));
//                            new JsonAddCoin().execute();

                        } else {

//                            Toast.makeText(BuyCoinsActivity.this, "Error in transaction", Toast.LENGTH_SHORT).show();
                            SnackbarManager.show(
                                    Snackbar.with(MyCartActivity.this)
                                            .position(Snackbar.SnackbarPosition.TOP)
                                            .margin(15, 15)
                                            .backgroundDrawable(R.drawable.snackbar_custom_layout)
                                            .text("Payment Error!!"));

                        }


                    } catch (JSONException e) {
                        Log.e("paymentExample", "an extremely unlikely failure occurred: ", e);
                    }
                }
            } else if (resultCode == Activity.RESULT_CANCELED) {
                Log.e("paymentExample", "The user canceled.");
            } else if (resultCode == PaymentActivity.RESULT_EXTRAS_INVALID) {
                Log.e("paymentExample", "An invalid Payment or PayPalConfiguration was submitted. Please see the docs.");
            }
        }
    }


    private class UserGetAddressJsontask extends AsyncTask<String, Void, String> {

        boolean iserror = false;

        @Override
        protected void onPreExecute() {
            // TODO Auto-generated method stub
            //  loginprogressbar.setVisibility(View.VISIBLE);
            Log.e("******* NOW UserGetAddressJsontask WEB SERVICE IS RUNNING *******", "YES");
            Log.e("******* NOW UserGetAddressJsontask WEB SERVICE IS RUNNING *******", "YES");
            pv_gridview_mycartlist_progressview.setVisibility(View.VISIBLE);
            Log.e("User_ID From Shared Preference :", "" + User_ID);
            Log.e("Sign in URL :",
                    "http://sevilla.centrocomercial.com.es/wp-content/plugins/webserv/get_address.php?user_id=" + User_ID);

        }

        protected String doInBackground(String... params) {
            Log.e("******* NOW UserGetAddressJsontask TASK IS in doInBackground *******", "YES");
            Log.e("******* NOW UserGetAddressJsontask TASK IS in doInBackground *******", "YES");

            HttpClient client = new DefaultHttpClient();
            HttpPost post = new HttpPost("http://sevilla.centrocomercial.com.es/wp-content/plugins/webserv/get_address.php?user_id=" + User_ID);

            try {

                HttpResponse response = client.execute(post);
                String objectAddress = EntityUtils.toString(response.getEntity());
                Log.e("*******objectAddress******** :", "" + objectAddress);

                //JSONArray js = new JSONArray(object);
                JSONObject jobectAddress = new JSONObject(objectAddress);
                Get_user_address_status = jobectAddress.getString("status");
                if (Get_user_address_status.equalsIgnoreCase("Ok")) {
                    Get_user_address_result = jobectAddress.getString("result");
                    Get_user_address_message = jobectAddress.getString("message");
                    Get_user_address_ID = jobectAddress.getString("user_id");
                    Get_user_address_F_name = jobectAddress.getString("first_name");
                    Get_user_address_L_name = jobectAddress.getString("last_name");
                    Get_user_address_address = jobectAddress.getString("address");
                    Get_user_address_phone_number = jobectAddress.getString("phone_number");
                    Get_user_address_country = jobectAddress.getString("country");
                    Get_user_address_state = jobectAddress.getString("state");
                    Get_user_address_city = jobectAddress.getString("city");
                    Get_user_address_zipcode = jobectAddress.getString("zipcode");

                }

            } catch (Exception e) {
                Log.v("22", "22" + e.getMessage());
                e.printStackTrace();
                iserror = true;
            }
            return Get_user_address_status;
        }

        @Override
        protected void onPostExecute(String result1) {
            // TODO Auto-generated method stub
            super.onPostExecute(result1);
            pv_gridview_mycartlist_progressview.setVisibility(View.GONE);

            if (!iserror) {
                if (Get_user_address_status.equalsIgnoreCase("OK")) {

                    Log.e("No Error :", "Get_user_address_result Success    OK");

                    Log.e("Get_user_address_result :", "" + Get_user_address_result);
                    Log.e("Get_user_address_message :", "" + Get_user_address_message);
                    Log.e("Get_user_address_ID :", "" + Get_user_address_ID);
                    Log.e("Get_user_address_F_name :", "" + Get_user_address_F_name);
                    Log.e("Get_user_address_L_name :", "" + Get_user_address_L_name);
                    Log.e("Get_user_address_address :", "" + Get_user_address_address);
                    Log.e("Get_user_address_phone_number :", "" + Get_user_address_phone_number);
                    Log.e("Get_user_address_country :", "" + Get_user_address_country);
                    Log.e("Get_user_address_state :", "" + Get_user_address_state);
                    Log.e("Get_user_address_city :", "" + Get_user_address_city);
                    Log.e("Get_user_address_zipcode :", "" + Get_user_address_zipcode);

                    Rl_checkout.setVisibility(View.VISIBLE);
                    Intent GoDeliveryScreen = new Intent(getApplicationContext(), GetDeliveryAddress.class);
                    startActivity(GoDeliveryScreen);

                } else if (Get_user_address_status.equalsIgnoreCase("Not Ok")) {

                    Log.e("********* UserGetAddressJsontask *********", "unsuccessfull ERROR");
                    Log.e("********* UserGetAddressJsontask *********", "unsuccessfull ERROR");
                    Log.e("********* UserGetAddressJsontask *********", "unsuccessfull ERROR");

                    Intent GoEditDeliveryScreen = new Intent(getApplicationContext(), EditDeliveryActivity.class);
                    startActivity(GoEditDeliveryScreen);

                }
            } else {
                Log.e("********* UserGetAddressJsontask *********", " ERROR");

                SnackbarManager.show(
                        Snackbar.with(MyCartActivity.this)
                                .position(Snackbar.SnackbarPosition.TOP)
                                .margin(15, 15)
                                .backgroundDrawable(R.drawable.snackbar_custom_layout)
                                .text("Oops!! Please check server connection ."));

            }
        }

    }




    /*Another double click to exit on back pressed 02- 02 (Start)*/
    /*@Override
    public void onBackPressed() {
        if (doubleBackToExitPressedOnce) {
            super.onBackPressed();
            return;
        }

        this.doubleBackToExitPressedOnce = true;
        Toast.makeText(this, "Please click BACK again to exit", Toast.LENGTH_SHORT).show();

        new Handler().postDelayed(new Runnable() {

            @Override
            public void run() {
                doubleBackToExitPressedOnce=false;
            }
        }, 2000);
    }*/
    /*Another double click to exit on back pressed 02- 02 (End)*/





    /*double click to exit on back pressed 02- 02 (Start)*/
    /*private final Runnable mRunnable = new Runnable() {
        @Override
        public void run() {
            doubleBackToExitPressedOnce = false;
        }
    };

    @Override
    protected void onDestroy() {
        super.onDestroy();

        if (mHandler != null) {
            mHandler.removeCallbacks(mRunnable);
        }
    }

    @Override
    public void onBackPressed() {
        if (doubleBackToExitPressedOnce) {
            super.onBackPressed();
            return;
        }

        this.doubleBackToExitPressedOnce = true;
        Toast.makeText(this, "Please click BACK again to exit", Toast.LENGTH_SHORT).show();

        mHandler.postDelayed(mRunnable, 2000);
    }*/
    /*double click to exit on back pressed 02- 02 (End)*/

}
