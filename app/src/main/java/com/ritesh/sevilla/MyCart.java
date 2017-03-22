package com.ritesh.sevilla;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Rect;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
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
import com.ritesh.sevilla.Constant.Appconstant;
import com.ritesh.sevilla.Constant.HttpUrlPath;
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
 * Created by ritesh on 25/2/17.
 */

public class MyCart extends AppCompatActivity {


    @BindView(R.id.rl_sub_category_button_cart)
    RelativeLayout Rl_checkout;

    @BindView(R.id.rl_sub_category_button_cart_zoon)
    RelativeLayout Rl_checkout_zoom;

    @BindView(R.id.toolbar_my_cart)
    Toolbar toolbar_MyCart;

    String
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


    List<MainActivity> mycartlistrowItems;
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




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_cart);
        ButterKnife.bind(this);

        setSupportActionBar(toolbar_MyCart);
        Appconstant.sh = getSharedPreferences(Appconstant.MyPREFERENCES, Context.MODE_PRIVATE);
        User_ID = Appconstant.sh.getString("id", null);
        Log.e("User_ID from SharedPref :", "" + User_ID);


        /*recycler data (Start 02 of 03)*/
        /*image settin by universal image loader */
        DisplayImageOptions defaultOptions = new DisplayImageOptions.Builder().cacheInMemory(true).cacheOnDisk(true).build();
        ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(MyCart.this).defaultDisplayImageOptions(defaultOptions).build();
        ImageLoader.getInstance().init(config);
        /*image settin by universal image loader */

         /*circular progress bar (Start)*/
        mCircularProgressBarGridview_mycartlist = (CircularProgressBar) findViewById(R.id.listview_my_cart_progressbar_circular);
//        signupProgress.setVisibility(View.GONE);
        ((CircularProgressDrawable) mCircularProgressBarGridview_mycartlist.getIndeterminateDrawable()).start();
        updateValues();
        /*circular progress bar (End)*/



        mycartlistrowItems = new ArrayList<MainActivity>();
        MyCartSingleProduct_id = new ArrayList<>();
        MyCartSingleProduct_Name = new ArrayList<>();
        MyCartSingleProduct_Image = new ArrayList<>();
        MyCartSingleProduct_Price = new ArrayList<>();
        MyCartSingleProduct_Quantity = new ArrayList<>();
        MyCartSingleProduct_DeliveryCharge = new ArrayList<>();
        MyCartSingleProduct_DeliveryDate = new ArrayList<>();


        RecyclerView.LayoutManager mLayoutManager = new GridLayoutManager(this, 2);
        mycartlist_recylerView.setLayoutManager(mLayoutManager);
        mycartlist_recylerView.addItemDecoration(new MyCart.EqualSpaceItemDecoration(5));


        if (Utils.isConnected(getApplicationContext())) {
            MyCartListJsontask task = new MyCartListJsontask();
            task.execute();
        } else {

            SnackbarManager.show(
                    Snackbar.with(MyCart.this)
                            .position(Snackbar.SnackbarPosition.TOP)
                            .margin(15, 15)
                            .backgroundDrawable(R.drawable.snackbar_custom_layout)
                            .text("Please Your Internet Connectivity..!!"));

        }











        Rl_checkout.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {

                if (event.getAction() == MotionEvent.ACTION_DOWN) {

                    Log.e("Action ", "Down");
                    Rl_checkout_zoom.setVisibility(View.VISIBLE);
                    Rl_checkout.setVisibility(View.GONE);
                    v.performHapticFeedback(HapticFeedbackConstants.VIRTUAL_KEY);
                    Toast.makeText(getApplicationContext(), "Add to cart Clicked", Toast.LENGTH_SHORT).show();

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
                    return true;
                }


                return false;
            }
        });


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
            outRect.left = mSpaceHeight;
            outRect.right = mSpaceHeight;
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
        }

        @Override
        protected List<BeanMyCartList> doInBackground(String... params) {
            HttpClient client = new DefaultHttpClient();
            HttpPost post = new HttpPost("http://sevilla.centrocomercial.com.es/wp-content/plugins/webserv/get_cart_product_list.php?user_id="+User_ID);

            Log.e(" URL :", " " + "http://sevilla.centrocomercial.com.es/wp-content/plugins/webserv/get_cart_product_list.php?user_id="+User_ID);

            try {
                HttpResponse response = client.execute(post);
                String obj = EntityUtils.toString(response.getEntity());
                beanMyCartlist0 = new ArrayList<>();
                Log.e("************ list ************", "" + obj);

                Log.e("************Json data*******************", " " + obj);
                JSONObject jsonObject = new JSONObject(obj);
                Str_My_Cart_List_detail = jsonObject.getString("Product_cart_detail");
                Log.e("Json Str_My_Cart_List_detail data :", " " + Str_My_Cart_List_detail);

                JSONArray jaaray = new JSONArray(Str_My_Cart_List_detail);

                for (int i = 0; i < jaaray.length(); i++) {
                    Str_My_Cart_List_result = jaaray.getJSONObject(i).getString("result");

                    if (Str_My_Cart_List_result.equalsIgnoreCase("successful")) {
                        BeanMyCartList beanMyCartList = new BeanMyCartList();
                        beanMyCartList.setMyCartSingleProductid(jaaray.getJSONObject(i).getString("single_product_id"));
                        beanMyCartList.setMyCartSingleProductName(jaaray.getJSONObject(i).getString("single_product_title"));
                        beanMyCartList.setMyCartSingleProductImage(jaaray.getJSONObject(i).getString("single_product_img"));
                        beanMyCartList.setMyCartSingleProductPrice(jaaray.getJSONObject(i).getString("single_product_price"));
                        beanMyCartList.setMyCartSingleProductQuantity(jaaray.getJSONObject(i).getString("item_quantity"));
                        beanMyCartList.setMyCartSingleProductDeliveryCharge(jaaray.getJSONObject(i).getString("    "));
                        beanMyCartList.setMyCartSingleProductDeliveryDate(jaaray.getJSONObject(i).getString("     "));
                        beanMyCartlist0.add(beanMyCartList);


                        Str_My_Cart_List_single_product_id = jaaray.getJSONObject(i).getString("single_product_id");
                        Str_My_Cart_List_single_product_title = jaaray.getJSONObject(i).getString("single_product_title");
                        Str_My_Cart_List_single_product_price = jaaray.getJSONObject(i).getString("single_product_price");
                        Str_My_Cart_List_single_product_img = jaaray.getJSONObject(i).getString("single_product_img");
                        Str_My_Cart_List_single_item_quantity = jaaray.getJSONObject(i).getString("item_quantity");
                        Str_My_Cart_List_DeliveryCharge = jaaray.getJSONObject(i).getString("     ");
                        Str_My_Cart_List_DeliveryDate = jaaray.getJSONObject(i).getString("      ");

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

            } catch (Exception e) {
                System.out.println("Errror in gettting by ID" + e);
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

                CategoryAdapter categoryAdapter = new MyCart.CategoryAdapter(MyCart.this, mystring);
                mycartlist_recylerView.setAdapter(categoryAdapter);


                Log.e(" ********** listvideoid.size() > 0 **********", "YES");
                Log.e(" ********** listvideoid.size() > 0 **********", "YES");
                Log.e(" ********** listvideoid.size() > 0 **********", "YES");
            } else {

                Log.e("cat_id size is :", "0");

            }
        }

    }





    private class CategoryAdapter extends RecyclerView.Adapter<MyCart.CategoryAdapter.MyViewHolder> {

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


            }
        }


        private CategoryAdapter(Context mContext, List<BeanMyCartList> arrayList) {
            this.mContext = mContext;
            this.arrayList = arrayList;
        }

        @Override
        public MyCart.CategoryAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View itemView = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.activity_my_cart_grid_items, parent, false);

            return new MyCart.CategoryAdapter.MyViewHolder(itemView);
        }

        @Override
        public void onBindViewHolder(final MyCart.CategoryAdapter.MyViewHolder holder, final int position) {

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


    }


}
