package com.ritesh.sevilla;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.facebook.FacebookSdk;
import com.nispok.snackbar.Snackbar;
import com.nispok.snackbar.SnackbarManager;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;
import com.ritesh.sevilla.Constant.Appconstant;
import com.ritesh.sevilla.Constant.Utils;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONObject;

import butterknife.BindView;
import butterknife.ButterKnife;
import de.hdodenhof.circleimageview.CircleImageView;
import fr.castorflex.android.circularprogressbar.CircularProgressBar;
import fr.castorflex.android.circularprogressbar.CircularProgressDrawable;

/**
 * Created by ritesh on 14/4/17.
 */
@SuppressWarnings("deprecation")
public class SellerSingleProductDetail extends AppCompatActivity {

    @BindView(R.id.toolbar_seller_single_product)
    Toolbar TB_seller_single_product;

    @BindView(R.id.tv_seller_single_product)
    TextView TV_seller_single_product;

    @BindView(R.id.tv_single_product_tittle)
    TextView TV_single_product_tittle;

    @BindView(R.id.tv_single_product_amount)
    TextView TV_single_product_amount;

    @BindView(R.id.tv_single_product_detail)
    TextView TV_single_product_detail;

    @BindView(R.id.iv_single_product_image)
    ImageView IV_single_product_image;


    @BindView(R.id.cv_single_product_edit)
    CardView CV_single_product_edit;

    @BindView(R.id.cv_single_product_edit_pressed)
    CardView CV_single_product_edit_pressed;


    @BindView(R.id.rl_single_product_edit)
    RelativeLayout RL_single_product_edit;

    @BindView(R.id.rl_single_product_main_progress)
    RelativeLayout RL_single_product_main_progress;

    CircularProgressBar CPB_single_product_main_progressbar;


    @BindView(R.id.rl_single_product_imageprogress)
    RelativeLayout RL_single_product_imageprogress;

    CircularProgressBar CPB_single_product_image_progressbar;


    String
            User_ID = "",
            SellerSingleProduct_Status = "",
            SellerSingleProduct_ID = "",
            SellerSingleProduct_User_ID = "",
            SellerSingleProduct_Content = "",
            SellerSingleProduct_Name = "",
            SellerSingleProduct_Result = "",
            SellerSingleProduct_image = "",
            SellerSingleProduct_regular_price = "",
            SellerSingleProductId_Recived = "",
            SellerSingleProductName_Recived = "";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FacebookSdk.sdkInitialize(getApplicationContext());
        setContentView(R.layout.activity_seller_single_product_detail);
        ButterKnife.bind(this);

        Appconstant.sh = getSharedPreferences(Appconstant.MyPREFERENCES, Context.MODE_PRIVATE);
        User_ID = Appconstant.sh.getString("id", null);
        Log.e("User_ID from SharedPref :", "" + User_ID);


        setSupportActionBar(TB_seller_single_product);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        TB_seller_single_product.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed(); // Implemented by activity
            }
        });



        /*circular progress bar (Start)*/
        CPB_single_product_main_progressbar = (CircularProgressBar) findViewById(R.id.single_product_main_progressbar);
        ((CircularProgressDrawable) CPB_single_product_main_progressbar.getIndeterminateDrawable()).start();
        updateValuesMain();


        CPB_single_product_image_progressbar = (CircularProgressBar) findViewById(R.id.single_product_image_progressbar);
        ((CircularProgressDrawable) CPB_single_product_image_progressbar.getIndeterminateDrawable()).start();
        updateValuesImage();


        SellerSingleProductId_Recived = getIntent().getStringExtra("SellerProductid");
        SellerSingleProductName_Recived = getIntent().getStringExtra("SellerProductname");
        Log.e("SellerSingleProductId_Recived from List :", "" + SellerSingleProductId_Recived);
        Log.e("SellerSingleProductName_Recived from List :", "" + SellerSingleProductName_Recived);


        if (Utils.isConnected(getApplicationContext())) {
            SellerSingleProductDetailJsontask task = new SellerSingleProductDetailJsontask();
            task.execute();
        } else {

            SnackbarManager.show(
                    Snackbar.with(SellerSingleProductDetail.this)
                            .position(Snackbar.SnackbarPosition.TOP)
                            .margin(15, 15)
                            .backgroundDrawable(R.drawable.snackbar_custom_layout)
                            .text("Please Your Internet Connectivity..!!"));

        }


    }


    /*progressbar data (Start)*/
    private void updateValuesMain() {
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
        CPB_single_product_main_progressbar.setIndeterminateDrawable(circularProgressDrawable = b.build());

        // /!\ Terrible hack, do not do this at home!
        circularProgressDrawable.setBounds(0,
                0,
                CPB_single_product_main_progressbar.getWidth(),
                CPB_single_product_main_progressbar.getHeight());
        CPB_single_product_main_progressbar.setVisibility(View.INVISIBLE);
        CPB_single_product_main_progressbar.setVisibility(View.VISIBLE);
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
        CPB_single_product_image_progressbar.setIndeterminateDrawable(circularProgressDrawable = b.build());

        // /!\ Terrible hack, do not do this at home!
        circularProgressDrawable.setBounds(0,
                0,
                CPB_single_product_image_progressbar.getWidth(),
                CPB_single_product_image_progressbar.getHeight());
        CPB_single_product_image_progressbar.setVisibility(View.INVISIBLE);
        CPB_single_product_image_progressbar.setVisibility(View.VISIBLE);
    }
    /*progressbar data (End)*/


    private class SellerSingleProductDetailJsontask extends AsyncTask<String, Void, String> {

        boolean iserror = false;

        @Override
        protected void onPreExecute() {
            // TODO Auto-generated method stub
            //  loginprogressbar.setVisibility(View.VISIBLE);
            Log.e("******* NOW Login WEB SERVICE IS RUNNING *******", "YES");
            Log.e("******* NOW Login WEB SERVICE IS RUNNING *******", "YES");
            Log.e("SellerSingleProductId_Recived Recived :", "" + SellerSingleProductId_Recived);
            Log.e("SellerSingleProductName_Recived Recived :", "" + SellerSingleProductName_Recived);
            Log.e("Seller User ID :", "" + User_ID);
            RL_single_product_main_progress.setVisibility(View.VISIBLE);
            RL_single_product_imageprogress.setVisibility(View.VISIBLE);
            Log.e("Seller Single Product URL :", ""
                    + "http://sevilla.centrocomercial.com.es/wp-content/plugins/webserv/get_single_seller_product.php?user_id=" + User_ID
                    + "&product_id=" + SellerSingleProductId_Recived);

        }

        @Override
        protected String doInBackground(String... params) {
            Log.e("******* NOW Login TASK IS RUNNING *******", "YES");
            HttpClient client = new DefaultHttpClient();
            HttpPost post = new HttpPost("http://sevilla.centrocomercial.com.es/wp-content/plugins/webserv/get_single_seller_product.php?user_id=" + User_ID
                    + "&product_id=" + SellerSingleProductId_Recived);

            try {
                HttpResponse response = client.execute(post);
                String object = EntityUtils.toString(response.getEntity());
                Log.e("*******object******** :", "" + object);

                //JSONArray js = new JSONArray(object);
                JSONObject jobect = new JSONObject(object);
                SellerSingleProduct_Status = jobect.getString("status");
                if (SellerSingleProduct_Status.equalsIgnoreCase("Ok")) {
                    SellerSingleProduct_ID = jobect.getString("ID");
                    SellerSingleProduct_User_ID = jobect.getString("post_author");
                    SellerSingleProduct_Content = jobect.getString("post_content");
                    SellerSingleProduct_Name = jobect.getString("post_title");
                    SellerSingleProduct_image = jobect.getString("product_iamge");
                    SellerSingleProduct_regular_price = jobect.getString("regular_price");
                    SellerSingleProduct_Result = jobect.getString("result");

                }

            } catch (Exception e) {
                Log.v("22", "22" + e.getMessage());
                e.printStackTrace();
                iserror = true;
            }

            return SellerSingleProduct_Result;
        }

        @Override
        protected void onPostExecute(String result1) {
            // TODO Auto-generated method stub
            super.onPostExecute(result1);
            RL_single_product_main_progress.setVisibility(View.GONE);

            if (!iserror) {
                if (SellerSingleProduct_Result.equalsIgnoreCase("successfull")) {

                    Log.e("SellerSingleProduct_ID :", "" + SellerSingleProduct_ID);
                    Log.e("SellerSingleProduct_User_ID :", "" + SellerSingleProduct_User_ID);
                    Log.e("SellerSingleProduct_Content :", "" + SellerSingleProduct_Content);
                    Log.e("SellerSingleProduct_Name :", "" + SellerSingleProduct_Name);
                    Log.e("SellerSingleProduct_image :", "" + SellerSingleProduct_image);
                    Log.e("SellerSingleProduct_regular_price :", "" + SellerSingleProduct_regular_price);
                    Log.e("SellerSingleProduct_Result :", "" + SellerSingleProduct_Result);


                    TV_single_product_tittle.setText(Html.fromHtml(SellerSingleProduct_Name));
                    TV_single_product_amount.setText(Html.fromHtml(SellerSingleProduct_regular_price));
                    TV_single_product_detail.setText(Html.fromHtml(SellerSingleProduct_Content));


                    ImageLoader imageLoader = ImageLoader.getInstance();
                    imageLoader.getInstance().displayImage(SellerSingleProduct_image, IV_single_product_image, new ImageLoadingListener() {
                        @Override
                        public void onLoadingStarted(String imageUri, View view) {
                            RL_single_product_imageprogress.setVisibility(View.VISIBLE);
                        }

                        @Override
                        public void onLoadingFailed(String imageUri, View view, FailReason failReason) {
                            RL_single_product_imageprogress.setVisibility(View.GONE);
                        }

                        @Override
                        public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {
                            RL_single_product_imageprogress.setVisibility(View.GONE);
                        }

                        @Override
                        public void onLoadingCancelled(String imageUri, View view) {
                            RL_single_product_imageprogress.setVisibility(View.GONE);
                        }
                    });

                } else {
                    Log.e("onPostExecute Sub_cat_id size is Zero ", "ooppss");
                    SnackbarManager.show(
                            Snackbar.with(SellerSingleProductDetail.this)
                                    .position(Snackbar.SnackbarPosition.TOP)
                                    .margin(15, 15)
                                    .backgroundDrawable(R.drawable.snackbar_custom_layout)
                                    .text("No Data Found"));
                }
            }
        }

    }


}
