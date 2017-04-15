package com.ritesh.sevilla;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.util.Log;
import android.view.HapticFeedbackConstants;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;
import com.facebook.FacebookSdk;
import com.nispok.snackbar.Snackbar;
import com.nispok.snackbar.SnackbarManager;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;
import com.ritesh.sevilla.Constant.Appconstant;
import com.ritesh.sevilla.Constant.Utils;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.mime.HttpMultipartMode;
import org.apache.http.entity.mime.MultipartEntity;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.entity.mime.content.StringBody;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import fr.castorflex.android.circularprogressbar.CircularProgressBar;
import fr.castorflex.android.circularprogressbar.CircularProgressDrawable;

/**
 * Created by ritesh on 15/4/17.
 */
@SuppressWarnings("deprecation")
public class SellerSingleProductEditDetail extends AppCompatActivity {

    @BindView(R.id.toolbar_edit_product)
    Toolbar TB_edit_product;


    @BindView(R.id.tv_edit_product_disable)
    TextView TV_edit_product_category_name;


    @BindView(R.id.tv_subcategory_edit_product_text_disable)
    TextView TV_edit_product_subcategory_name;


    @BindView(R.id.tv_edit_product_image_path)
    TextView TV_edit_product_image_path;


    @BindView(R.id.edt_edit_product_name)
    EditText EDT_edit_product_name;


    @BindView(R.id.edt_edit_product_price)
    EditText EDT_edit_product_price;


    @BindView(R.id.edt_edit_product_description)
    EditText EDT_edit_product_description;


    @BindView(R.id.iv_edit_product_image)
    ImageView IV_edit_product_image;


    @BindView(R.id.rl_edit_product_select_product_image_btn)
    RelativeLayout RL_edit_product_select_product_image_btn;


    @BindView(R.id.rl_btn_update_now)
    RelativeLayout RL_btn_update_now;

    @BindView(R.id.rl_btn_update_now_click)
    RelativeLayout RL_btn_update_now_click;


    @BindView(R.id.rl_update_progress)
    RelativeLayout RL_update_progress;

    CircularProgressBar mCPB_update_new;

    @BindView(R.id.tvTextCounter_edit_product)
    CharCountTextView CCTVttvTextCounter_edit_product;


    Dialog QuickTipDialogUpdate;

    File Update_newImage;
    FileBody UpdateNewfilebody;

    String
            User_ID = "",
            Str_Get_Product_ID = "",
            Str_Update_newImage_pathName = "",
            Str_Update_newImage_path = "",
            Str_Update_new_Result = "",
            Str_Update_new_Status = "",
            Str_Update_new_Message = "",
            Str_Get_Edt_Set_product_name = "",
            Str_Get_Edt_Set_product_price = "",
            Str_Get_Edt_Set_product_discription = "",
            SellerSingleProduct_Status = "",
            SellerSingleProduct_ID = "",
            SellerSingleProduct_User_ID = "",
            SellerSingleProduct_Content = "",
            SellerSingleProduct_Name = "",
            SellerSingleProduct_image = "",
            SellerSingleProduct_cat_name = "",
            SellerSingleProduct_subcat_name = "",
            SellerSingleProduct_regular_price = "",
            SellerSingleProduct_Result = "",
            Str_timeStamp = "";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FacebookSdk.sdkInitialize(getApplicationContext());
        setContentView(R.layout.activity_seller_single_product_edit_detail);
        ButterKnife.bind(this);

        Appconstant.sh = getSharedPreferences(Appconstant.MyPREFERENCES, Context.MODE_PRIVATE);
        User_ID = Appconstant.sh.getString("id", null);
        Log.e("User_ID from SharedPref :", "" + User_ID);

        Str_Get_Product_ID = getIntent().getStringExtra("ProductID");
        Log.e("Str_Get_Product_ID Recieved :", "" + Str_Get_Product_ID);


        CCTVttvTextCounter_edit_product.setEditText(EDT_edit_product_description);
        CCTVttvTextCounter_edit_product.setMaxCharacters(150); //Will default to 150 anyway (Twitter emulation)
        CCTVttvTextCounter_edit_product.setExceededTextColor(Color.RED); //Will default to red also
        CCTVttvTextCounter_edit_product.setCharCountChangedListener(new CharCountTextView.CharCountChangedListener() {
            @Override
            public void onCountChanged(int countRemaining, boolean hasExceededLimit) {
                if (hasExceededLimit) {
                    RL_btn_update_now_click.setEnabled(false);
                } else {
                    RL_btn_update_now_click.setEnabled(true);
                }
            }
        });


        mCPB_update_new = (CircularProgressBar) findViewById(R.id.cpb_update_new);
        ((CircularProgressDrawable) mCPB_update_new.getIndeterminateDrawable()).start();
        updateValuesImage();

        setSupportActionBar(TB_edit_product);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        TB_edit_product.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed(); // Implemented by activity
            }
        });

        RL_edit_product_select_product_image_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                v.performHapticFeedback(HapticFeedbackConstants.VIRTUAL_KEY);

                update_newImagePicker();
            }
        });


        if (Utils.isConnected(getApplicationContext())) {
            SellerProductDetailJsontask task = new SellerProductDetailJsontask();
            task.execute();
        } else {

            SnackbarManager.show(
                    Snackbar.with(SellerSingleProductEditDetail.this)
                            .position(Snackbar.SnackbarPosition.TOP)
                            .margin(15, 15)
                            .backgroundDrawable(R.drawable.snackbar_custom_layout)
                            .text("Please Your Internet Connectivity..!!"));

        }


        RL_btn_update_now.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                boolean iserror = false;
                Str_Get_Edt_Set_product_name = EDT_edit_product_name.getText().toString();
                Str_Get_Edt_Set_product_price = EDT_edit_product_price.getText().toString();
                Str_Get_Edt_Set_product_discription = EDT_edit_product_description.getText().toString();

                Log.e("Publicate product Data :", "\n"
                        + "Str_Get_Edt_Set_product_name :" + "" + Str_Get_Edt_Set_product_name + "\n"
                        + "Str_Get_Edt_Set_product_price :" + "" + Str_Get_Edt_Set_product_price + "\n"
                        + "Str_Get_Edt_Set_product_discription :" + "" + Str_Get_Edt_Set_product_discription + "\n"
                        + "Str_publicate_newImage_pathName :" + "" + Str_Update_newImage_pathName + "\n"
                        + "Str_publicate_newImage_path :" + "" + Str_Update_newImage_path + "\n"
                );


                if (event.getAction() == MotionEvent.ACTION_DOWN) {

                    Log.e("Action ", "Down");
                    RL_btn_update_now_click.setVisibility(View.VISIBLE);
                    RL_btn_update_now.setVisibility(View.GONE);
                    v.performHapticFeedback(HapticFeedbackConstants.VIRTUAL_KEY);

                    return true;
                }

                if (event.getAction() == MotionEvent.ACTION_MOVE) {

                    Log.e("Action ", "Move");
                    RL_btn_update_now_click.setVisibility(View.VISIBLE);
                    RL_btn_update_now.setVisibility(View.GONE);
                    return true;

                }
                if (event.getAction() == MotionEvent.ACTION_UP) {

                    Log.e("Action ", "Up");

                    RL_btn_update_now_click.setVisibility(View.GONE);
                    RL_btn_update_now.setVisibility(View.VISIBLE);

                    if (Str_Get_Edt_Set_product_name.equals("")) {
                        iserror = true;
                        Log.e(" Error Str_Get_Edt_Set_product_name:", "Ok");
                        v.performHapticFeedback(HapticFeedbackConstants.VIRTUAL_KEY);
                        /**************** Start Animation ****************/
                        /*https://github.com/daimajia/AndroidViewAnimations/blob/master/README.md*/

                        YoYo.with(Techniques.Tada)
                                .duration(700)
                                .playOn(EDT_edit_product_name);
                        /**************** End Animation ****************/
                        SnackbarManager.show(
                                Snackbar.with(SellerSingleProductEditDetail.this)
                                        .position(Snackbar.SnackbarPosition.TOP)
                                        .margin(15, 15)
                                        .backgroundDrawable(R.drawable.snackbar_custom_layout)
                                        .textColor(R.color.text_color_black)
                                        .text("Please enter Product Name"));


                    } else if (Str_Update_newImage_path.equals("")) {
                        iserror = true;
                        Log.e(" Error Str_Update_newImage_path:", "Ok");
                        v.performHapticFeedback(HapticFeedbackConstants.VIRTUAL_KEY);
                        /**************** Start Animation ****************/
                        /*https://github.com/daimajia/AndroidViewAnimations/blob/master/README.md*/
                        YoYo.with(Techniques.Tada)
                                .duration(700)
                                .playOn(RL_edit_product_select_product_image_btn);
                        /**************** End Animation ****************/
                        SnackbarManager.show(
                                Snackbar.with(SellerSingleProductEditDetail.this)
                                        .position(Snackbar.SnackbarPosition.TOP)
                                        .margin(15, 15)
                                        .backgroundDrawable(R.drawable.snackbar_custom_layout)
                                        .textColor(R.color.text_color_black)
                                        .text("Please select Product Image"));

                    } else if (Str_Get_Edt_Set_product_price.equals("")) {
                        iserror = true;
                        Log.e(" Error Str_Get_Edt_Set_product_price:", "Ok");
                        v.performHapticFeedback(HapticFeedbackConstants.VIRTUAL_KEY);
                        /**************** Start Animation ****************/
                        /*https://github.com/daimajia/AndroidViewAnimations/blob/master/README.md*/

                        YoYo.with(Techniques.Tada)
                                .duration(700)
                                .playOn(EDT_edit_product_price);
                        /**************** End Animation ****************/
                        SnackbarManager.show(
                                Snackbar.with(SellerSingleProductEditDetail.this)
                                        .position(Snackbar.SnackbarPosition.TOP)
                                        .margin(15, 15)
                                        .backgroundDrawable(R.drawable.snackbar_custom_layout)
                                        .textColor(R.color.text_color_black)
                                        .text("Please enter Product Price"));


                    } else if (Str_Get_Edt_Set_product_discription.equals("")) {
                        iserror = true;
                        Log.e(" Error Str_Get_Edt_Set_product_discription:", "Ok");
                        v.performHapticFeedback(HapticFeedbackConstants.VIRTUAL_KEY);
                        /**************** Start Animation ****************/
                        /*https://github.com/daimajia/AndroidViewAnimations/blob/master/README.md*/

                        YoYo.with(Techniques.Tada)
                                .duration(700)
                                .playOn(EDT_edit_product_description);
                        /**************** End Animation ****************/
                        SnackbarManager.show(
                                Snackbar.with(SellerSingleProductEditDetail.this)
                                        .position(Snackbar.SnackbarPosition.TOP)
                                        .margin(15, 15)
                                        .backgroundDrawable(R.drawable.snackbar_custom_layout)
                                        .textColor(R.color.text_color_black)
                                        .text("Please enter Product Discription"));


                    }
                    if (!iserror) {

                        Log.e("No Error :", "Ok");
                        v.performHapticFeedback(HapticFeedbackConstants.VIRTUAL_KEY);

                        if (Utils.isConnected(getApplicationContext())) {
                            UpdateProductJsontask task = new UpdateProductJsontask();
                            task.execute();
                        } else {

                            SnackbarManager.show(
                                    Snackbar.with(SellerSingleProductEditDetail.this)
                                            .position(Snackbar.SnackbarPosition.TOP)
                                            .margin(15, 15)
                                            .backgroundDrawable(R.drawable.snackbar_custom_layout)
                                            .textColor(R.color.text_color_black)
                                            .text("Please Your Internet Connectivity..!!"));

                        }

                    }

                    return true;
                }


                return false;
            }
        });


    }


    private void updateValuesImage() {
        CircularProgressDrawable circularProgressDrawable;
        CircularProgressDrawable.Builder b = new CircularProgressDrawable.Builder(this)
                .colors(getResources().getIntArray(R.array.gplus_colors))
                .style(CircularProgressDrawable.STYLE_ROUNDED);
        mCPB_update_new.setIndeterminateDrawable(circularProgressDrawable = b.build());


        circularProgressDrawable.setBounds(0,
                0,
                mCPB_update_new.getWidth(),
                mCPB_update_new.getHeight());
        mCPB_update_new.setVisibility(View.INVISIBLE);
        mCPB_update_new.setVisibility(View.VISIBLE);
    }


    private void update_newImagePicker() {
        QuickTipDialogUpdate = new Dialog(SellerSingleProductEditDetail.this);
//                callFeeDialog = new Dialog(MainActivity.this);
        QuickTipDialogUpdate.requestWindowFeature(Window.FEATURE_NO_TITLE);
        QuickTipDialogUpdate.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
        QuickTipDialogUpdate.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        QuickTipDialogUpdate.setContentView(R.layout.activity_dialog_image_picker);
        final RelativeLayout RlCamera = (RelativeLayout) QuickTipDialogUpdate.findViewById(R.id.rl_inner_camera);
        final RelativeLayout RlGallery = (RelativeLayout) QuickTipDialogUpdate.findViewById(R.id.rl_inner_gallery);
        final TextView TvCancel = (TextView) QuickTipDialogUpdate.findViewById(R.id.tv_dialog_cancel);

        TvCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                QuickTipDialogUpdate.dismiss();
            }
        });

        RlCamera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Str_timeStamp = new SimpleDateFormat("ddMMyyyy_HHmmss").format(new Date());
                final SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss",
                        java.util.Locale.getDefault());
                new PickerBuilder(SellerSingleProductEditDetail.this, PickerBuilder.SELECT_FROM_CAMERA)
                        .setOnImageReceivedListener(new PickerBuilder.onImageReceivedListener() {
                            @Override
                            public void onImageReceived(Uri imageUri) {
//                                Toast.makeText(PublicateNewActivity.this, "Got image - " + imageUri, Toast.LENGTH_LONG).show();
                                IV_edit_product_image.setImageURI(imageUri);
                                Log.e("imageUri path SELECT_FROM_CAMERA :", "" + imageUri);
                                Log.e("timeStamp :", "" + Str_timeStamp);
                                Log.e("timeStamp :", "" + Str_timeStamp);
                                Log.e("format String:", "" + format.getTimeZone().toString());
                                Log.e("format :", "" + format);
                                Log.e("format :", "" + format);
                                File f = new File(imageUri.getPath());
                                Str_Update_newImage_path = imageUri.getPath();
                                Log.e("Str_Update_newImage_path SELECT_FROM_CAMERA :", "" + Str_Update_newImage_path);
                                Update_newImage = new File(Str_Update_newImage_path);
                                Str_Update_newImage_pathName = Update_newImage.getName();
                                Log.e("Str_Update_newImage_pathName :", "" + Str_Update_newImage_pathName);
                                Log.e("Update_newImage File Image:", "" + Update_newImage);
                                TV_edit_product_image_path.setText(Str_Update_newImage_pathName);
                                QuickTipDialogUpdate.dismiss();
                            }
                        })
                        .setImageName("UpdateImage_" + Str_timeStamp)
                        .setImageFolderName("SevillaFolder")
                        .withTimeStamp(false)
                        .setCropScreenColor(Color.CYAN)
                        .start();

            }
        });

        RlGallery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                new PickerBuilder(SellerSingleProductEditDetail.this, PickerBuilder.SELECT_FROM_GALLERY)
                        .setOnImageReceivedListener(new PickerBuilder.onImageReceivedListener() {
                            @Override
                            public void onImageReceived(Uri imageUri) {
                                IV_edit_product_image.setImageURI(imageUri);
                                Log.e("imageUri path SELECT_FROM_GALLERY :", "" + imageUri);
                                File f = new File(imageUri.getPath());
                                Str_Update_newImage_path = imageUri.getPath();
                                Log.e("Str_Update_newImage_path SELECT_FROM_GALLERY :", "" + Str_Update_newImage_path);
                                Update_newImage = new File(Str_Update_newImage_path);
                                Str_Update_newImage_pathName = Update_newImage.getName();
                                Log.e("Update_newImage File Image:", "" + Update_newImage);
                                Log.e("Str_Update_newImage_pathName :", "" + Str_Update_newImage_pathName);
                                TV_edit_product_image_path.setText(Str_Update_newImage_pathName);
                                QuickTipDialogUpdate.dismiss();
                            }
                        })
                        .setImageName("Publicate")
                        .setImageFolderName("SevillaImages")
                        .setCropScreenColor(Color.CYAN)
                        .setOnPermissionRefusedListener(new PickerBuilder.onPermissionRefusedListener() {
                            @Override
                            public void onPermissionRefused() {

                            }
                        })
                        .start();


            }
        });


        QuickTipDialogUpdate.show();
    }


    private class SellerProductDetailJsontask extends AsyncTask<String, Void, String> {

        boolean iserror = false;

        @Override
        protected void onPreExecute() {
            // TODO Auto-generated method stub
            //  loginprogressbar.setVisibility(View.VISIBLE);
            Log.e("******* NOW Login WEB SERVICE IS RUNNING *******", "YES");
            Log.e("******* NOW Login WEB SERVICE IS RUNNING *******", "YES");
            Log.e("Str_Get_Product_ID Recived :", "" + Str_Get_Product_ID);
            Log.e("Seller User ID :", "" + User_ID);
            RL_update_progress.setVisibility(View.VISIBLE);
            Log.e("Seller Single Product URL :", ""
                    + "http://sevilla.centrocomercial.com.es/wp-content/plugins/webserv/get_single_seller_product.php?user_id=" + User_ID
                    + "&product_id=" + Str_Get_Product_ID);

        }

        @Override
        protected String doInBackground(String... params) {
            Log.e("******* NOW Login TASK IS RUNNING *******", "YES");
            HttpClient client = new DefaultHttpClient();
            HttpPost post = new HttpPost("http://sevilla.centrocomercial.com.es/wp-content/plugins/webserv/get_single_seller_product.php?user_id=" + User_ID
                    + "&product_id=" + Str_Get_Product_ID);

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
                    SellerSingleProduct_subcat_name = jobect.getString("subcat_name");
                    SellerSingleProduct_cat_name = jobect.getString("cat_name");
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
            RL_update_progress.setVisibility(View.GONE);

            if (!iserror) {
                if (SellerSingleProduct_Result.equalsIgnoreCase("successfull")) {

                    Log.e("SellerSingleProduct_ID :", "" + SellerSingleProduct_ID);
                    Log.e("SellerSingleProduct_User_ID :", "" + SellerSingleProduct_User_ID);
                    Log.e("SellerSingleProduct_Content :", "" + SellerSingleProduct_Content);
                    Log.e("SellerSingleProduct_Name :", "" + SellerSingleProduct_Name);
                    Log.e("SellerSingleProduct_image :", "" + SellerSingleProduct_image);
                    Log.e("SellerSingleProduct_subcat_name :", "" + SellerSingleProduct_subcat_name);
                    Log.e("SellerSingleProduct_cat_name :", "" + SellerSingleProduct_cat_name);
                    Log.e("SellerSingleProduct_regular_price :", "" + SellerSingleProduct_regular_price);
                    Log.e("SellerSingleProduct_Result :", "" + SellerSingleProduct_Result);


                    TV_edit_product_category_name.setText(Html.fromHtml(SellerSingleProduct_cat_name));
                    TV_edit_product_subcategory_name.setText(Html.fromHtml(SellerSingleProduct_subcat_name));
                    EDT_edit_product_name.setText(Html.fromHtml(SellerSingleProduct_Name));
                    EDT_edit_product_price.setText(Html.fromHtml(SellerSingleProduct_regular_price));
                    EDT_edit_product_description.setText(Html.fromHtml(SellerSingleProduct_Content));


                    ImageLoader imageLoader = ImageLoader.getInstance();
                    imageLoader.getInstance().displayImage(SellerSingleProduct_image, IV_edit_product_image, new ImageLoadingListener() {
                        @Override
                        public void onLoadingStarted(String imageUri, View view) {
//                            RL_single_product_imageprogress.setVisibility(View.VISIBLE);
                        }

                        @Override
                        public void onLoadingFailed(String imageUri, View view, FailReason failReason) {
//                            RL_single_product_imageprogress.setVisibility(View.GONE);
                        }

                        @Override
                        public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {
//                            RL_single_product_imageprogress.setVisibility(View.GONE);
                        }

                        @Override
                        public void onLoadingCancelled(String imageUri, View view) {
//                            RL_single_product_imageprogress.setVisibility(View.GONE);
                        }
                    });

                } else {
                    Log.e("onPostExecute Sub_cat_id size is Zero ", "ooppss");
                    SnackbarManager.show(
                            Snackbar.with(SellerSingleProductEditDetail.this)
                                    .position(Snackbar.SnackbarPosition.TOP)
                                    .margin(15, 15)
                                    .backgroundDrawable(R.drawable.snackbar_custom_layout)
                                    .text("No Data Found"));
                }
            }
        }

    }


    private class UpdateProductJsontask extends AsyncTask<String, Void, String> {

        boolean iserror = false;

        @Override
        protected void onPreExecute() {
            // TODO Auto-generated method stub
            //  loginprogressbar.setVisibility(View.VISIBLE);
            Log.e("******* NOW UpdateProductJsontask WEB SERVICE IS RUNNING *******", "YES");
            Log.e("******* NOW UpdateProductJsontask WEB SERVICE IS RUNNING *******", "YES");


            Log.e("User_ID :", "" + User_ID
                    + "\n" + "Str_Get_Product_ID :" + "" + Str_Get_Product_ID
                    + "\n" + "Str_Get_Edt_Set_product_name :" + "" + Str_Get_Edt_Set_product_name
                    + "\n" + "Str_Get_Edt_Set_product_price :" + "" + Str_Get_Edt_Set_product_price
                    + "\n" + "Str_Get_Edt_Set_product_discription :" + "" + Str_Get_Edt_Set_product_discription
                    + "\n" + "Str_Update_newImage_path :" + "" + Str_Update_newImage_path
                    + "\n" + "Str_Update_newImage_pathName :" + "" + Str_Update_newImage_pathName
                    + "\n" + "UpdateNewfilebody :" + "" + UpdateNewfilebody);

            Log.e("onPreExecute Publicate Product URL :", ""
                    + "http://sevilla.centrocomercial.com.es/wp-content/plugins/webserv/edit_seller_product.php?user_id=" + User_ID
                    + "&product_id=" + Str_Get_Product_ID
                    + "&product_name=" + Str_Get_Edt_Set_product_name
                    + "&product_description=" + Str_Get_Edt_Set_product_discription
                    + "&product_price=" + Str_Get_Edt_Set_product_price
                    + "&product_image=" + UpdateNewfilebody);



            if (Str_Update_newImage_path.equalsIgnoreCase("")) {
                Str_Update_newImage_path = "Ritesh";
            }
            RL_update_progress.setVisibility(View.VISIBLE);


        }

        @Override
        protected String doInBackground(String... params) {
            Log.e("******* NOW UpdateProductJsontask TASK IS RUNNING *******", "YES");


            try {
                HttpClient client = new DefaultHttpClient();
                HttpPost post = new HttpPost("http://sevilla.centrocomercial.com.es/wp-content/plugins/webserv/edit_seller_product.php?user_id=" + User_ID);

                MultipartEntity reqEntity = new MultipartEntity(HttpMultipartMode.BROWSER_COMPATIBLE);
                reqEntity.addPart("product_id", new StringBody(Str_Get_Product_ID));
                reqEntity.addPart("product_name", new StringBody(Str_Get_Edt_Set_product_name));
                reqEntity.addPart("product_description", new StringBody(Str_Get_Edt_Set_product_discription));
                reqEntity.addPart("product_price", new StringBody(Str_Get_Edt_Set_product_price));
                reqEntity.addPart("product_image", new StringBody(Str_Update_newImage_path));

                if (Update_newImage == null) {
                    Log.e("UpdateImage File is :", "NULL");
                    Str_Update_newImage_path = "Ritesh";

                } else {
                    /***************** file for post the profile image to the server *****************/
                    UpdateNewfilebody = new FileBody(Update_newImage);
                    reqEntity.addPart("product_image", UpdateNewfilebody);
                    Log.e("UpdateNewfilebody image :", "" + UpdateNewfilebody.toString());
                    /***************** file for post the profile image to the server *****************/
                }

                List<NameValuePair> nameValuePairs = new ArrayList<>();
                nameValuePairs.add(new BasicNameValuePair("user_id", User_ID));
                Log.e("******* ID TO SERVER FOR MACHING*******", "" + User_ID);

                post.setEntity(reqEntity);
                HttpResponse response = client.execute(post);
                BufferedReader reader = new BufferedReader(new InputStreamReader(response.getEntity().getContent(), "UTF-8"));
                String sResponse;
                StringBuilder s = new StringBuilder();
                while ((sResponse = reader.readLine()) != null) {
                    s = s.append(sResponse);
                }

                String Jsondata = s.toString();
                Log.e("Jsondata : ", Jsondata);
                JSONObject parentObject = new JSONObject(Jsondata);
                Str_Update_new_Result = parentObject.getString("result");
                Str_Update_new_Status = parentObject.getString("status");
                Str_Update_new_Message = parentObject.getString("message");
                Log.e("*********** Str_Update_new_Result *********** : ", Str_Update_new_Result);
                Log.e("*********** Str_Update_new_Status *********** : ", Str_Update_new_Status);
                Log.e("*********** Str_Update_new_Message *********** : ", Str_Update_new_Message);

            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            } catch (ClientProtocolException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (JSONException e) {
                e.printStackTrace();
            }

            return Str_Update_new_Result;
        }

        @Override
        protected void onPostExecute(String result1) {
            // TODO Auto-generated method stub
            super.onPostExecute(result1);
            RL_update_progress.setVisibility(View.GONE);

            if (!iserror) {
                if (Str_Update_new_Result.equalsIgnoreCase("success")) {
                    Log.e("GoMainSellerScreen ", "OK");
                    Toast.makeText(SellerSingleProductEditDetail.this, " Product Updated..!!", Toast.LENGTH_SHORT).show();


                    Intent GoMainSellerScreen = new Intent(SellerSingleProductEditDetail.this, MainSellerActivity.class);
                    GoMainSellerScreen.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(GoMainSellerScreen);
                    finish();


                } else {
                    Log.e("onPostExecute Sub_cat_id size is Zero ", "ooppss");
                    SnackbarManager.show(
                            Snackbar.with(SellerSingleProductEditDetail.this)
                                    .position(Snackbar.SnackbarPosition.TOP)
                                    .margin(15, 15)
                                    .backgroundDrawable(R.drawable.snackbar_custom_layout)
                                    .text("Error"));
                }
            }
        }

    }


}
