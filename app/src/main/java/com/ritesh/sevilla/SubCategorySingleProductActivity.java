package com.ritesh.sevilla;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.text.Html;
import android.util.Log;
import android.view.HapticFeedbackConstants;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;
import com.facebook.FacebookSdk;
import com.nispok.snackbar.Snackbar;
import com.nispok.snackbar.SnackbarManager;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;
import com.ritesh.sevilla.Constant.Appconstant;
import com.ritesh.sevilla.Constant.Utils;
import com.shawnlin.numberpicker.NumberPicker;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONObject;

import butterknife.BindView;
import butterknife.ButterKnife;
import fr.castorflex.android.circularprogressbar.CircularProgressBar;
import fr.castorflex.android.circularprogressbar.CircularProgressDrawable;

/**
 * Created by ritesh on 24/2/17.
 */
@SuppressWarnings("deprecation")
public class SubCategorySingleProductActivity extends AppCompatActivity {

    @BindView(R.id.np_quantity_numbers)
    NumberPicker numberPicker;

    @BindView(R.id.tv_ity)
    TextView tv_qunt;

    /*@BindView(R.id.toolbar_single_product_item)
    Toolbar toolbar_sub_category_single_Product;*/

    @BindView(R.id.cv_sub_category_product_add_cart)
    CardView CV_add_cart;

    @BindView(R.id.cv_sub_category_product_add_cart_pressed)
    CardView CV_add_cart_pressed;

    @BindView(R.id.cv_sub_category_single_item_image)
    CardView CV_sub_category_single_item_image;

    @BindView(R.id.iv_single_item_image)
    ImageView IV_single_product_image;

    @BindView(R.id.tv_sub_category_product_tittle)
    TextView TV_single_product_name;

    @BindView(R.id.tv_sub_category_single_product_amount)
    TextView TV_single_product_amount;

    /*@BindView(R.id.tv_sub_category_single_item_text)
    TextView TV_single_product_Toolbar_text;*/

    @BindView(R.id.tv_sub_category_product_detail)
    TextView TV_single_product_description;

    @BindView(R.id.rl_single_product_progress)
    RelativeLayout Rl_singleProduct_circularProgress;

    CircularProgressBar CircularProBar_singleProduct;

    @BindView(R.id.rl_single_product_imageprogress)
    RelativeLayout Rl_singleProduct_ImagecircularProgress;

    /*@BindView(R.id.rl_back_icon)
    RelativeLayout Rl_back_btn;*/

    /*@BindView(R.id.rl_cart_icon)
    RelativeLayout Rl_cart_btn;*/

    /*@BindView(R.id.rl_badgeview_cart_item)
    RelativeLayout Rl_cart_badge;*/

    /*@BindView(R.id.tv_badge_counter)
    TextView TV_cart_badge;*/

    CircularProgressBar CircularProBar_singleProductImage;


    @BindView(R.id.rl_cart_icon_sub_category_single_product)
    RelativeLayout RL_cart_icon_sub_category_single_product;

    @BindView(R.id.rl_badgeview_cart_item_sub_category_single_product)
    RelativeLayout RL_badgeview_cart_item_sub_category_single_product;


    @BindView(R.id.rl_cart_icon_sub_category_single_product_click)
    RelativeLayout RL_cart_icon_sub_category_single_product_click;

    @BindView(R.id.rl_badgeview_cart_item_sub_category_single_product_click)
    RelativeLayout RL_badgeview_cart_item_sub_category_single_product_click;

    @BindView(R.id.tv_badge_counter_sub_category_single_product)
    TextView TV_badge_counter_sub_category_single_product;


    String
            User_ID = "",
            Str_Get_Cart_Detail_Status = "",
            Str_Get_Cart_result = "",
            Str_Get_CartCount_Shared = "",
            Str_Get_Cart_Deatil_User_ID = "",
            Str_Get_Cart_Product_count = "",

    SubCategorySingleProductId_Recived = "",
            SubCategoryName_Recived = "",
            StatusSingle = "",
            SingleProductID = "",
            SingleProductName = "",
            SingleProductPrice = "",
            SingleProductDescription = "",
            SingleProductImage = "",
            CartQuantity = "0",
            statusResult = "",
            Single_item_result = "",
            Single_Item_id = "",
            Single_Item_quantity = "",
            Single_Item_no_of_prodcut = "",
            Single_Item_product_title = "",
            Single_Item_product_price = "",
            Single_Item_img = "",
            Single_Item_total_price = "",
            SingleProductResult = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FacebookSdk.sdkInitialize(getApplicationContext());
        setContentView(R.layout.activity_sub_category_single_product);
        ButterKnife.bind(this);


        SubCategorySingleProductId_Recived = getIntent().getStringExtra("SubCatSingleProductID");
        SubCategoryName_Recived = getIntent().getStringExtra("SubCatName");
        Log.e("SubCategorySingleProductId_Recived from List :", "" + SubCategorySingleProductId_Recived);
        Log.e("SubCategoryName_Recived from List :", "" + SubCategoryName_Recived);


        Appconstant.sh = getSharedPreferences(Appconstant.MyPREFERENCES, Context.MODE_PRIVATE);
        User_ID = Appconstant.sh.getString("id", null);
        Str_Get_CartCount_Shared = Appconstant.sh.getString("cart_count", null);
        Log.e("User_ID from SharedPref :", "" + User_ID);
        Log.e("Cart Count From Shared Preference:", "" + Str_Get_CartCount_Shared);


        if (Utils.isConnected(getApplicationContext())) {
            Log.e("SubCategorySingleProductCartDetailJsontask Call :", "OK");
            SubCategorySingleProductCartDetailJsontask task = new SubCategorySingleProductCartDetailJsontask();
            task.execute();
        } else {

            SnackbarManager.show(
                    Snackbar.with(SubCategorySingleProductActivity.this)
                            .position(Snackbar.SnackbarPosition.TOP)
                            .margin(15, 15)
                            .backgroundDrawable(R.drawable.snackbar_custom_layout)
                            .text("Please Your Internet Connectivity..!!"));

        }


//        TV_single_product_Toolbar_text.setText(SubCategoryName_Recived);


        /*setSupportActionBar(toolbar_sub_category_single_Product);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);*/

//        toolbar_sub_category.setNavigationIcon(R.drawable.ic_back_arrow); // your drawable





         /*circular progress bar (Start)*/
        CircularProBar_singleProduct = (CircularProgressBar) findViewById(R.id.single_product_progressbar_circular);
        ((CircularProgressDrawable) CircularProBar_singleProduct.getIndeterminateDrawable()).start();
        CircularProBar_singleProductImage = (CircularProgressBar) findViewById(R.id.single_product_imageprogressbar_circular);
        ((CircularProgressDrawable) CircularProBar_singleProductImage.getIndeterminateDrawable()).start();
        updateValues();
        updateValuesImage();
        /*circular progress bar (End)*/

//        Rl_cart_btn.setEnabled(false);
//        Rl_cart_btn.setEnabled(true);
//        Rl_cart_btn.setClickable(false);
//        Rl_cart_btn.setClickable(true);

        /*image settin by universal image loader */
        DisplayImageOptions defaultOptions = new DisplayImageOptions.Builder().cacheInMemory(true).cacheOnDisk(true).build();
        ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(SubCategorySingleProductActivity.this).defaultDisplayImageOptions(defaultOptions).build();
        ImageLoader.getInstance().init(config);
        /*image settin by universal image loader */

// set divider color
        numberPicker.setDividerColor(ContextCompat.getColor(this, R.color.colorPrimary));
        numberPicker.setDividerColorResource(R.color.colorPrimary);

        // set formatter
        numberPicker.setFormatter(getString(R.string.number_picker_formatter));
        numberPicker.setFormatter(R.string.number_picker_formatter);

        // set text color
        numberPicker.setTextColor(ContextCompat.getColor(this, R.color.colorPrimary));
        numberPicker.setTextColorResource(R.color.colorPrimary);

        // set text size
        numberPicker.setTextSize(getResources().getDimension(R.dimen.text_size));
        numberPicker.setTextSize(R.dimen.text_size);

        // set typeface
        numberPicker.setTypeface(Typeface.create(getString(R.string.roboto_light), Typeface.NORMAL));
        numberPicker.setTypeface(getString(R.string.roboto_light), Typeface.NORMAL);
        numberPicker.setTypeface(getString(R.string.roboto_light));
        numberPicker.setTypeface(R.string.roboto_light, Typeface.NORMAL);
        numberPicker.setTypeface(R.string.roboto_light);


        User_ID = Appconstant.sh.getString("id", null);
        Log.e("User_ID from SharedPref :", "" + User_ID);

        if (Utils.isConnected(getApplicationContext())) {
            SubCategorySingleProductDetailJsontask task = new SubCategorySingleProductDetailJsontask();
            task.execute();
        } else {

            SnackbarManager.show(
                    Snackbar.with(SubCategorySingleProductActivity.this)
                            .position(Snackbar.SnackbarPosition.TOP)
                            .margin(15, 15)
                            .backgroundDrawable(R.drawable.snackbar_custom_layout)
                            .text("Please Your Internet Connectivity..!!"));

        }


       /* toolbar_sub_category_single_Product.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed(); // Implemented by activity
            }
        });*/

       /* Rl_back_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed(); // Implemented by activity
            }
        });*/


        CV_add_cart.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {

                if (event.getAction() == MotionEvent.ACTION_DOWN) {

                    Log.e("Action ", "Down");
                    CV_add_cart_pressed.setVisibility(View.VISIBLE);
                    CV_add_cart.setVisibility(View.GONE);
                    v.performHapticFeedback(HapticFeedbackConstants.VIRTUAL_KEY);

                    if (!CartQuantity.equals("0")) {

                        if (Utils.isConnected(getApplicationContext())) {
                            AddItemOnCart task = new AddItemOnCart();
                            task.execute();
                        } else {

                            SnackbarManager.show(
                                    Snackbar.with(SubCategorySingleProductActivity.this)
                                            .position(Snackbar.SnackbarPosition.TOP)
                                            .margin(15, 15)
                                            .backgroundDrawable(R.drawable.snackbar_custom_layout)
                                            .text("Please Your Internet Connectivity..!!"));

                        }

                    } else {
                        /**************** Start Animation ****************/
                        YoYo.with(Techniques.Shake)
                                .duration(700)
                                .playOn(numberPicker);
                        /**************** End Animation ****************/

                    /*Toast.makeText(getApplicationContext(), "Please enter more than 5 character in password.",
                            Toast.LENGTH_SHORT).show();*/

                        SnackbarManager.show(
                                Snackbar.with(SubCategorySingleProductActivity.this)
                                        .position(Snackbar.SnackbarPosition.TOP)
                                        .margin(15, 15)
                                        .backgroundDrawable(R.drawable.snackbar_custom_layout)
                                        .text("Add Some Item On Cart First!!!"));
                        Log.e("CartQuantity is Zero ", "OK");
                    }


//                    Toast.makeText(getApplicationContext(), "Add to cart Clicked", Toast.LENGTH_SHORT).show();
                    return true;
                }

                if (event.getAction() == MotionEvent.ACTION_MOVE) {

                    Log.e("Action ", "Move");
                    CV_add_cart_pressed.setVisibility(View.VISIBLE);
                    CV_add_cart.setVisibility(View.GONE);

                    return true;

                }
                if (event.getAction() == MotionEvent.ACTION_UP) {

                    Log.e("Action ", "Up");

                    CV_add_cart_pressed.setVisibility(View.GONE);
                    CV_add_cart.setVisibility(View.VISIBLE);
                    return true;
                }


                return false;
            }
        });


        RL_cart_icon_sub_category_single_product.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {

                if (event.getAction() == MotionEvent.ACTION_DOWN) {

                    Log.e("Action ", "Down");
                    RL_cart_icon_sub_category_single_product_click.setVisibility(View.VISIBLE);
                    RL_cart_icon_sub_category_single_product.setVisibility(View.GONE);

                    RL_badgeview_cart_item_sub_category_single_product_click.setVisibility(View.VISIBLE);
                    RL_badgeview_cart_item_sub_category_single_product.setVisibility(View.GONE);
                    v.performHapticFeedback(HapticFeedbackConstants.VIRTUAL_KEY);

//                    Toast.makeText(getApplicationContext(), "Add to cart Clicked", Toast.LENGTH_SHORT).show();
                    return true;
                }

                if (event.getAction() == MotionEvent.ACTION_MOVE) {

                    Log.e("Action ", "Move");
                    RL_cart_icon_sub_category_single_product_click.setVisibility(View.VISIBLE);
                    RL_cart_icon_sub_category_single_product.setVisibility(View.GONE);

                    RL_badgeview_cart_item_sub_category_single_product_click.setVisibility(View.VISIBLE);
                    RL_badgeview_cart_item_sub_category_single_product.setVisibility(View.GONE);

                    return true;

                }
                if (event.getAction() == MotionEvent.ACTION_UP) {

                    Log.e("Action ", "Up");

                    RL_cart_icon_sub_category_single_product_click.setVisibility(View.GONE);
                    RL_cart_icon_sub_category_single_product.setVisibility(View.VISIBLE);

                    RL_badgeview_cart_item_sub_category_single_product_click.setVisibility(View.GONE);
                    RL_badgeview_cart_item_sub_category_single_product.setVisibility(View.VISIBLE);


                     Intent MyCartPage = new Intent(getApplicationContext(), MyCartActivity.class);
                    SubCategorySingleProductActivity.this.startActivity(MyCartPage);

                    return true;
                }


                return false;
            }
        });


        CV_sub_category_single_item_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                v.performHapticFeedback(HapticFeedbackConstants.VIRTUAL_KEY);
                Toast.makeText(getApplicationContext(), "Image Clicked", Toast.LENGTH_SHORT).show();
            }
        });

        /*numberPicker.setOnScrollListener(new NumberPicker.OnScrollListener() {
            private int oldValue;
            Boolean quantity = false;
            @Override
            public void onScrollStateChange(NumberPicker view, int scrollState) {
                if (scrollState == NumberPicker.OnScrollListener.SCROLL_STATE_IDLE) {
                    //We get the different between oldValue and the new value
//                    int valueDiff = numberPicker.getValue() - oldValue;

                    //Update oldValue to the new value for the next scroll
                    oldValue = numberPicker.getValue();

                    Toast.makeText(SubCategorySingleProductActivity.this, "Quantity is : " + oldValue , Toast.LENGTH_SHORT).show();
                    //Do action with valueDiff
                    quantity = true;
                    if (quantity){
                        Log.e("quantity is :","True");
                        CartQuantity = String.valueOf(oldValue);
                        Log.e("CartQuantity :" , "" + CartQuantity);
                        tv_qunt.setText(CartQuantity);
                    }
                    else {
                        Log.e("quantity is :","False");
                        Log.e("CartQuantity :" , "" + CartQuantity);
                    }
//                    Tv_Add_cart_quantity.setText(oldValue);

                }
            }
        });*/

        numberPicker.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {
            @Override
            public void onValueChange(NumberPicker picker, int oldVal, int newVal) {
//                Toast.makeText(SubCategorySingleProductActivity.this, "New Value : " + newVal , Toast.LENGTH_SHORT).show();
                CartQuantity = String.valueOf(newVal);
                Log.e("CartQuantity on changed:", "" + CartQuantity);
                tv_qunt.setText(CartQuantity);

            }
        });

    }


    @Override
    protected void onStart() {
        super.onStart();
        Log.e("SubCategorySingleProductActivity lifecycle", "onStart invoked");
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.e("SubCategorySingleProductActivity lifecycle", "onResume invoked");
//        TV_badge_counter_sub_category_single_product.setText(Str_Get_CartCount_Shared);

        if (Utils.isConnected(getApplicationContext())) {
            Log.e("onResume SubCategorySingleProductCartDetailJsontask Call :", "OK");
            SubCategorySingleProductCartDetailJsontask task = new SubCategorySingleProductCartDetailJsontask();
            task.execute();
        } else {

            SnackbarManager.show(
                    Snackbar.with(SubCategorySingleProductActivity.this)
                            .position(Snackbar.SnackbarPosition.TOP)
                            .margin(15, 15)
                            .backgroundDrawable(R.drawable.snackbar_custom_layout)
                            .text("Please Your Internet Connectivity..!!"));

        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.e("SubCategorySingleProductActivity lifecycle", "onPause invoked");
    }

    @Override
    protected void onStop() {
        super.onStop();
        Log.e("SubCategorySingleProductActivity lifecycle", "onStop invoked");
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        Log.e("SubCategorySingleProductActivity lifecycle", "onRestart invoked");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.e("SubCategorySingleProductActivity lifecycle", "onDestroy invoked");
    }


    private class SubCategorySingleProductCartDetailJsontask extends AsyncTask<String, Void, String> {

        boolean iserror = false;

        @Override
        protected void onPreExecute() {
            // TODO Auto-generated method stub
            //  loginprogressbar.setVisibility(View.VISIBLE);
            Log.e("******* NOW SubCategorySingleProductCartDetailJsontask WEB SERVICE IS RUNNING *******", "YES");
            Log.e("******* NOW SubCategorySingleProductCartDetailJsontask WEB SERVICE IS RUNNING *******", "YES");
            Log.e("User_ID onPreExecute :", "" + User_ID);

        }

        @Override
        protected String doInBackground(String... params) {
            Log.e("******* NOW SubCategorySingleProductCartDetailJsontask IS doInBackground RUNNING *******", "YES");
            HttpClient client = new DefaultHttpClient();
            HttpPost post = new HttpPost("http://sevilla.centrocomercial.com.es/wp-content/plugins/webserv/get_cart_product.php?user_id=" + User_ID);

            /*http://sevilla.centrocomercial.com.es/wp-content/plugins/webserv/get_cart_product.php?user_id=286*/
            Log.e("URL Cart Detail SubCategorySingleProductCartDetailJsontask :", "" + "http://sevilla.centrocomercial.com.es/wp-content/plugins/webserv/get_cart_product.php?user_id=" + User_ID);

            try {
                HttpResponse response = client.execute(post);
                String CartDetailobject = EntityUtils.toString(response.getEntity());
                Log.e("*******Cart Detail object******** :", "" + CartDetailobject);

                //JSONArray js = new JSONArray(object);
                JSONObject jobect = new JSONObject(CartDetailobject);
                Str_Get_Cart_Detail_Status = jobect.getString("status");
                if (Str_Get_Cart_Detail_Status.equalsIgnoreCase("OK")) {
                    Str_Get_Cart_Deatil_User_ID = jobect.getString("user_id");
                    Str_Get_Cart_Product_count = jobect.getString("no_of_prodcut");
                    Str_Get_Cart_result = jobect.getString("single_product_result");

                }

            } catch (Exception e) {
                Log.v("22", "22" + e.getMessage());
                e.printStackTrace();
                iserror = true;
            }

            return Str_Get_Cart_Detail_Status;
        }

        @Override
        protected void onPostExecute(String result1) {
            // TODO Auto-generated method stub
            super.onPostExecute(result1);

            if (!iserror) {
                if (Str_Get_Cart_Detail_Status.equalsIgnoreCase("OK")) {

                    Log.e("Str_Get_Cart_Deatil_User_ID :", "" + Str_Get_Cart_Deatil_User_ID);
                    Log.e("Str_Get_Cart_Product_count :", "" + Str_Get_Cart_Product_count);
                    Log.e("Str_Get_Cart_result :", "" + Str_Get_Cart_result);


                    Appconstant.editor.putString("cart_count", Str_Get_Cart_Product_count);
                    Appconstant.editor.commit();

                    TV_badge_counter_sub_category_single_product.setText(Str_Get_Cart_Product_count);
                    /**************** Start Animation **************  **/
                    YoYo.with(Techniques.Wobble)
                            .duration(700)
                            .playOn(RL_cart_icon_sub_category_single_product);
                    YoYo.with(Techniques.Wobble)
                            .duration(700)
                            .playOn(RL_badgeview_cart_item_sub_category_single_product);
                    /**************** End Animation ****************/




                } else {
                    Log.e("onPostExecute Error ", "ooppss");
                    SnackbarManager.show(
                            Snackbar.with(SubCategorySingleProductActivity.this)
                                    .position(Snackbar.SnackbarPosition.TOP)
                                    .margin(15, 15)
                                    .backgroundDrawable(R.drawable.snackbar_custom_layout)
                                    .text("No Data Found"));
                }
            }
        }

    }


    private class SubCategorySingleProductDetailJsontask extends AsyncTask<String, Void, String> {

        boolean iserror = false;

        @Override
        protected void onPreExecute() {
            // TODO Auto-generated method stub
            //  loginprogressbar.setVisibility(View.VISIBLE);
            Log.e("******* NOW Login WEB SERVICE IS RUNNING *******", "YES");
            Log.e("******* NOW Login WEB SERVICE IS RUNNING *******", "YES");
            Log.e("SubCategorySingleProductId Recived :", "" + SubCategorySingleProductId_Recived);
            Rl_singleProduct_circularProgress.setVisibility(View.VISIBLE);
            Rl_singleProduct_ImagecircularProgress.setVisibility(View.VISIBLE);

        }

        @Override
        protected String doInBackground(String... params) {
            Log.e("******* NOW Login TASK IS RUNNING *******", "YES");
            HttpClient client = new DefaultHttpClient();
            HttpPost post = new HttpPost("http://sevilla.centrocomercial.com.es/wp-content/plugins/" +
                    "webserv/get_single_product.php?product_id=" + SubCategorySingleProductId_Recived);

            try {
                HttpResponse response = client.execute(post);
                String object = EntityUtils.toString(response.getEntity());
                Log.e("*******object******** :", "" + object);

                //JSONArray js = new JSONArray(object);
                JSONObject jobect = new JSONObject(object);
                StatusSingle = jobect.getString("status");
                if (StatusSingle.equalsIgnoreCase("OK")) {
                    SingleProductID = jobect.getString("single_product_id");
                    SingleProductName = jobect.getString("single_product_title");
                    SingleProductPrice = jobect.getString("single_product_price");
                    SingleProductDescription = jobect.getString("single_product_content");
                    SingleProductImage = jobect.getString("single_product_img");
                    SingleProductResult = jobect.getString("single_product_result");

                }

            } catch (Exception e) {
                Log.v("22", "22" + e.getMessage());
                e.printStackTrace();
                iserror = true;
            }

            return SingleProductResult;
        }

        @Override
        protected void onPostExecute(String result1) {
            // TODO Auto-generated method stub
            super.onPostExecute(result1);
            Rl_singleProduct_circularProgress.setVisibility(View.GONE);

            if (!iserror) {
                if (SingleProductResult.equalsIgnoreCase("Successful")) {

                    Log.e("SingleProductID :", "" + SingleProductID);
                    Log.e("SingleProductName :", "" + SingleProductName);
                    Log.e("SingleProductPrice :", "" + SingleProductPrice);
                    Log.e("SingleProductDescription :", "" + SingleProductDescription);
                    Log.e("SingleProductImage :", "" + SingleProductImage);


                    TV_single_product_name.setText(Html.fromHtml(SingleProductName));
                    TV_single_product_amount.setText(Html.fromHtml(SingleProductPrice));
                    TV_single_product_description.setText(Html.fromHtml(SingleProductDescription));


                    ImageLoader imageLoader = ImageLoader.getInstance();
                    imageLoader.getInstance().displayImage(SingleProductImage, IV_single_product_image, new ImageLoadingListener() {
                        @Override
                        public void onLoadingStarted(String imageUri, View view) {
                            Rl_singleProduct_ImagecircularProgress.setVisibility(View.VISIBLE);
                        }

                        @Override
                        public void onLoadingFailed(String imageUri, View view, FailReason failReason) {
                            Rl_singleProduct_ImagecircularProgress.setVisibility(View.GONE);
                        }

                        @Override
                        public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {
                            Rl_singleProduct_ImagecircularProgress.setVisibility(View.GONE);
                        }

                        @Override
                        public void onLoadingCancelled(String imageUri, View view) {
                            Rl_singleProduct_ImagecircularProgress.setVisibility(View.GONE);
                        }
                    });

                } else {
                    Log.e("onPostExecute Sub_cat_id size is Zero ", "ooppss");
                    SnackbarManager.show(
                            Snackbar.with(SubCategorySingleProductActivity.this)
                                    .position(Snackbar.SnackbarPosition.TOP)
                                    .margin(15, 15)
                                    .backgroundDrawable(R.drawable.snackbar_custom_layout)
                                    .text("No Data Found"));
                }
            }
        }

    }


    private class AddItemOnCart extends AsyncTask<String, Void, String> {

        boolean iserrorr = false;

        @Override
        protected void onPreExecute() {
            // TODO Auto-generated method stub
            //  loginprogressbar.setVisibility(View.VISIBLE);
            Log.e("******* NOW AddItemOnCart WEB SERVICE IS RUNNING *******", "YES");
            Log.e("******* NOW AddItemOnCart WEB SERVICE IS RUNNING *******", "YES");
            Log.e("onPreExecute user_id :", "" + User_ID);
            Log.e("onPreExecute product_id :", "" + SingleProductID);
            Log.e("onPreExecute quantity :", "" + CartQuantity);
            Log.e("", "" + "http://sevilla.centrocomercial.com.es/wp-content/plugins/webserv/" + "add_cart.php?"
                    + "product_id=" + SingleProductID + "&user_id=" + User_ID + "&quantity=" + CartQuantity);

            Rl_singleProduct_circularProgress.setVisibility(View.VISIBLE);

        }

        protected String doInBackground(String... params) {
            Log.e("******* NOW AddItemOnCart TASK IS doInBackground *******", "YES");
            Log.e("******* NOW AddItemOnCart TASK IS doInBackground *******", "YES");

            HttpClient client = new DefaultHttpClient();
            HttpPost post = new HttpPost("http://sevilla.centrocomercial.com.es/wp-content/plugins/webserv/"
                    + "add_cart.php?" + "product_id=" + SingleProductID + "&user_id=" + User_ID + "&quantity=" + CartQuantity);

            try {

                HttpResponse response = client.execute(post);
                String object = EntityUtils.toString(response.getEntity());
                Log.e("*******object******** :", "" + object);

                //JSONArray js = new JSONArray(object);
                JSONObject jobect = new JSONObject(object);
                statusResult = jobect.getString("status");
                if (statusResult.equalsIgnoreCase("OK")) {
                    Single_Item_id = jobect.getString("single_product_id");
                    Single_Item_quantity = jobect.getString("item_quantity");
                    Single_Item_no_of_prodcut = jobect.getString("no_of_prodcut");
                    Single_Item_product_title = jobect.getString("single_product_title");
                    Single_Item_product_price = jobect.getString("single_product_price");
                    Single_Item_img = jobect.getString("single_product_img");
                    Single_Item_total_price = jobect.getString("single_item_total_price");
                    Single_item_result = jobect.getString("single_product_result");
                }

            } catch (Exception e) {
                Log.v("22", "22" + e.getMessage());
                e.printStackTrace();
                iserrorr = true;
            }
            return Single_item_result;
        }

        @Override
        protected void onPostExecute(String result1) {
            // TODO Auto-generated method stub
            super.onPostExecute(result1);
            Rl_singleProduct_circularProgress.setVisibility(View.GONE);
            if (!iserrorr) {
                if (Single_item_result.equalsIgnoreCase("Successful")) {
                    Log.e("onPostExecute statusResult :", "" + statusResult);
                    Log.e("onPostExecute Single_item_result :", "" + Single_item_result);
                    Log.e("onPostExecute Single_Item_id :", "" + Single_Item_id);
                    Log.e("onPostExecute Single_Item_quantity :", "" + Single_Item_quantity);
                    Log.e("onPostExecute Single_Item_no_of_prodcut :", "" + Single_Item_no_of_prodcut);
                    Log.e("onPostExecute Single_Item_product_title :", "" + Single_Item_product_title);
                    Log.e("onPostExecute Single_Item_img :", "" + Single_Item_img);
                    Log.e("onPostExecute Single_Item_product_price :", "" + Single_Item_product_price);
                    Log.e("onPostExecute Single_Item_total_price :", "" + Single_Item_total_price);

//                    Toast.makeText(SubCategorySingleProductActivity.this, "Adding product To Cart", Toast.LENGTH_SHORT).show();

                    /*Rl_cart_badge.setVisibility(View.VISIBLE);
                    TV_cart_badge.setText(Single_Item_no_of_prodcut);*/

                    SnackbarManager.show(
                            Snackbar.with(SubCategorySingleProductActivity.this)
                                    .position(Snackbar.SnackbarPosition.TOP)
                                    .margin(15, 15)
                                    .backgroundDrawable(R.drawable.snackbar_custom_layout)
                                    .text("Adding product To Cart"));

                    if (Utils.isConnected(getApplicationContext())) {
                        Log.e("SubCategorySingleProductCartDetailJsontask Call :", "OK");
                        SubCategorySingleProductCartDetailJsontask task = new SubCategorySingleProductCartDetailJsontask();
                        task.execute();
                    } else {

                        SnackbarManager.show(
                                Snackbar.with(SubCategorySingleProductActivity.this)
                                        .position(Snackbar.SnackbarPosition.TOP)
                                        .margin(15, 15)
                                        .backgroundDrawable(R.drawable.snackbar_custom_layout)
                                        .text("Please Your Internet Connectivity..!!"));

                    }


                } else {
                    Toast.makeText(getApplicationContext(), "Failed",
                            Toast.LENGTH_SHORT).show();
                }

            }
        }

    }


    // Initiating Menu XML file (menu.xml)
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.menu, menu);
        return true;
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        super.onPrepareOptionsMenu(menu);
        menu.findItem(R.id.menu_search).setVisible(false);
        return true;
    }

    /**
     * Event Handling for Individual menu item selected
     * Identify single menu item by it's id
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case R.id.menu_cart:
                Toast.makeText(SubCategorySingleProductActivity.this, "Cart is Selected", Toast.LENGTH_SHORT).show();
                return true;


            default:
                return super.onOptionsItemSelected(item);
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
        CircularProBar_singleProduct.setIndeterminateDrawable(circularProgressDrawable = b.build());

        // /!\ Terrible hack, do not do this at home!
        circularProgressDrawable.setBounds(0,
                0,
                CircularProBar_singleProduct.getWidth(),
                CircularProBar_singleProduct.getHeight());
        CircularProBar_singleProduct.setVisibility(View.INVISIBLE);
        CircularProBar_singleProduct.setVisibility(View.VISIBLE);
    }

    private void updateValuesImage() {
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
        CircularProBar_singleProductImage.setIndeterminateDrawable(circularProgressDrawable = b.build());

        // /!\ Terrible hack, do not do this at home!
        circularProgressDrawable.setBounds(0,
                0,
                CircularProBar_singleProductImage.getWidth(),
                CircularProBar_singleProductImage.getHeight());
        CircularProBar_singleProductImage.setVisibility(View.INVISIBLE);
        CircularProBar_singleProductImage.setVisibility(View.VISIBLE);
    }
    /*progressbar data (End)*/


}
