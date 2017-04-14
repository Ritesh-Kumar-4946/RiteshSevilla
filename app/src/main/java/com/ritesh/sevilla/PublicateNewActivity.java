package com.ritesh.sevilla;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
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
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;
import com.facebook.FacebookSdk;
import com.jaredrummler.materialspinner.MaterialSpinner;
import com.nispok.snackbar.Snackbar;
import com.nispok.snackbar.SnackbarManager;
import com.ritesh.sevilla.Constant.Appconstant;
import com.ritesh.sevilla.Constant.Utils;
import com.ritesh.sevilla.EditDeliverAddressPhoneFields.VerifyPhoneFragment;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.mime.HttpMultipartMode;
import org.apache.http.entity.mime.MultipartEntity;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.entity.mime.content.StringBody;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import fr.castorflex.android.circularprogressbar.CircularProgressBar;
import fr.castorflex.android.circularprogressbar.CircularProgressDrawable;
import minube.com.library.TextLengthBar;
import minube.com.library.TextLengthBarState;

/**
 * Created by ritesh on 11/4/17.
 */
@SuppressWarnings("deprecation")
public class PublicateNewActivity extends AppCompatActivity {

    @BindView(R.id.toolbar_publicate_new)
    Toolbar TB_publicate_new;

    @BindView(R.id.spinner_publicate_new_category)
    MaterialSpinner SP_publicate_new_MaincategoryList;

    @BindView(R.id.spinner_publicate_new_subcategory)
    MaterialSpinner SP_publicate_new_SubcategoryList;


    @BindView(R.id.edt_product_name)
    EditText EDT_product_name;

    @BindView(R.id.edt_product_price)
    EditText EDT_product_price;

    @BindView(R.id.edt_product_description)
    EditText EDT_product_description;


    @BindView(R.id.tv_product_image_path)
    TextView TV_product_image_path;


    @BindView(R.id.iv_product_image)
    ImageView IV_product_image;

    @BindView(R.id.rl_select_product_image_btn)
    RelativeLayout RL_select_product_image_btn;

    @BindView(R.id.rl_btn_publicate_now)
    RelativeLayout RL_btn_publicate_now;

    @BindView(R.id.rl_btn_publicate_now_click)
    RelativeLayout RL_btn_publicate_now_click;

    @BindView(R.id.rl_publicate_new_progress)
    RelativeLayout RL_publicate_new_progress;

    CircularProgressBar mCPBpublicate_new;

    @BindView(R.id.tvTextCounter)
    CharCountTextView CCTVtvTextCounter;

    /*@BindView(R.id.text_length_bar)
    TextLengthBar textLengthBar;*/

    Dialog QuickTipDialog;

    File publicate_newImage;
    FileBody PublicateNewfilebody;


    String
            User_ID = "",
            Str_publicate_newImage_pathName = "",
            Str_publicate_newImage_path = "",
            Str_publicate_new_Result = "",
            Str_publicate_new_Status = "",
            Str_publicate_new_Message = "",
            Str_Set_product_name = "",
            Str_Set_product_price = "",
            Str_Set_product_discription = "",

    Str_Get_Main_Category_Status = "",
            Str_Get_Main_Category_List = "",
            Str_Get_Main_Category_List_result = "",
            Str_Get_Main_Category_id = "",
            Str_Get_Main_Category_Name = "",
            Str_Get_Main_Category_id_Position = "",
            Str_Get_Main_Category_User_Default = "",
            Str_Get_User_MainCategory_Selected_Value = "",
            Str_Get_MainCategory_Value = "",
            Str_Set_MainCategory_Value = "",
            Str_GetSet_MainCategory_ID = "",

    Str_Get_Sub_Category_Status = "",
            Str_Get_Sub_Category_List = "",
            Str_Get_Sub_Category_List_result = "",
            Str_Get_Sub_Category_id = "",
            Str_Get_Sub_Category_Name = "",
            Str_Get_Sub_Category_id_Position = "",
            Str_Get_Sub_Category_User_Default = "",
            Str_Get_User_SubCategory_Selected_Value = "",
            Str_Get_SubCategory_Value = "",
            Str_Set_SubCategory_Value = "",
            Str_GetSet_SubCategory_ID = "";

    ArrayList<String> MAIN_CATEGORY_LIST = new ArrayList<String>();
    ArrayList<String> MAIN_CATEGORY_LISTID = new ArrayList<String>();

    ArrayList<String> SUB_CATEGORY_LIST = new ArrayList<String>();
    ArrayList<String> SUB_CATEGORY_LISTID = new ArrayList<String>();

    boolean ISerror = false;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FacebookSdk.sdkInitialize(getApplicationContext());
        setContentView(R.layout.activity_publicate_new);
        ButterKnife.bind(this);

        Appconstant.sh = getSharedPreferences(Appconstant.MyPREFERENCES, Context.MODE_PRIVATE);
        User_ID = Appconstant.sh.getString("id", null);
        Log.e("User_ID from SharedPref :", "" + User_ID);


        CCTVtvTextCounter.setEditText(EDT_product_description);
        CCTVtvTextCounter.setMaxCharacters(150); //Will default to 150 anyway (Twitter emulation)
        CCTVtvTextCounter.setExceededTextColor(Color.RED); //Will default to red also
        CCTVtvTextCounter.setCharCountChangedListener(new CharCountTextView.CharCountChangedListener() {
            @Override
            public void onCountChanged(int countRemaining, boolean hasExceededLimit) {
                if (hasExceededLimit) {
                    RL_btn_publicate_now.setEnabled(false);
                } else {
                    RL_btn_publicate_now.setEnabled(true);
                }
            }
        });

        mCPBpublicate_new = (CircularProgressBar) findViewById(R.id.cpb_publicate_new);
        ((CircularProgressDrawable) mCPBpublicate_new.getIndeterminateDrawable()).start();
        updateValuesImage();

        setSupportActionBar(TB_publicate_new);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        if (Utils.isConnected(getApplicationContext())) {
            MainCategoryListJsontask task = new MainCategoryListJsontask();
            task.execute();
        } else {

            SnackbarManager.show(
                    Snackbar.with(getApplicationContext())
                            .position(Snackbar.SnackbarPosition.TOP)
                            .margin(15, 15)
                            .backgroundDrawable(R.drawable.snackbar_custom_layout)
                            .text("Please Your Internet Connectivity..!!"));

        }

        /*textLengthBar.setStates(buildStates());
        textLengthBar.attachToEditText(EDT_product_description);*/

        TB_publicate_new.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed(); // Implemented by activity
            }
        });

        RL_select_product_image_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                v.performHapticFeedback(HapticFeedbackConstants.VIRTUAL_KEY);

                publicate_newImagePicker();
            }
        });


        RL_btn_publicate_now.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                boolean iserror = false;
                Str_Set_product_name = EDT_product_name.getText().toString();
                Str_Set_product_price = EDT_product_price.getText().toString();
                Str_Set_product_discription = EDT_product_description.getText().toString();

                Log.e("Publicate product Data :", "\n"
                        + "Str_Set_product_name :" + "" + Str_Set_product_name + "\n"
                        + "Str_Set_product_price :" + "" + Str_Set_product_price + "\n"
                        + "Str_Set_product_discription :" + "" + Str_Set_product_discription + "\n"
                        + "Str_publicate_newImage_pathName :" + "" + Str_publicate_newImage_pathName + "\n"
                        + "Str_publicate_newImage_path :" + "" + Str_publicate_newImage_path + "\n"
                        + "Str_GetSet_MainCategory_ID :" + "" + Str_GetSet_MainCategory_ID + "\n"
                        + "Str_Get_User_MainCategory_Selected_Value :" + "" + Str_Get_User_MainCategory_Selected_Value + "\n"
                        + "Str_GetSet_SubCategory_ID :" + "" + Str_GetSet_SubCategory_ID + "\n"
                        + "Str_Get_User_SubCategory_Selected_Value :" + "" + Str_Get_User_SubCategory_Selected_Value + "\n"
                );


                if (event.getAction() == MotionEvent.ACTION_DOWN) {

                    Log.e("Action ", "Down");
                    RL_btn_publicate_now_click.setVisibility(View.VISIBLE);
                    RL_btn_publicate_now.setVisibility(View.GONE);
                    v.performHapticFeedback(HapticFeedbackConstants.VIRTUAL_KEY);

//                    Toast.makeText(getApplicationContext(), "Add to cart Clicked", Toast.LENGTH_SHORT).show();
                    return true;
                }

                if (event.getAction() == MotionEvent.ACTION_MOVE) {

                    Log.e("Action ", "Move");
                    RL_btn_publicate_now_click.setVisibility(View.VISIBLE);
                    RL_btn_publicate_now.setVisibility(View.GONE);
                    return true;

                }
                if (event.getAction() == MotionEvent.ACTION_UP) {

                    Log.e("Action ", "Up");

                    RL_btn_publicate_now_click.setVisibility(View.GONE);
                    RL_btn_publicate_now.setVisibility(View.VISIBLE);


                    if (Str_Get_User_MainCategory_Selected_Value.equals("")) {
                        iserror = true;
                        Log.e(" Error :", "Ok");
                        v.performHapticFeedback(HapticFeedbackConstants.VIRTUAL_KEY);
                        /**************** Start Animation ****************/
                /*https://github.com/daimajia/AndroidViewAnimations/blob/master/README.md*/
                        YoYo.with(Techniques.Tada)
                                .duration(700)
                                .playOn(SP_publicate_new_MaincategoryList);
                        /**************** End Animation ****************/
                        SnackbarManager.show(
                                Snackbar.with(PublicateNewActivity.this)
                                        .position(Snackbar.SnackbarPosition.TOP)
                                        .margin(15, 15)
                                        .backgroundDrawable(R.drawable.snackbar_custom_layout)
                                        .textColor(R.color.text_color_black)
                                        .text("Please choose Main Category"));


                    } else if (Str_Get_User_SubCategory_Selected_Value.equals("")) {
                        iserror = true;
                        Log.e(" Error :", "Ok");
                        v.performHapticFeedback(HapticFeedbackConstants.VIRTUAL_KEY);
                        /**************** Start Animation ****************/
                /*https://github.com/daimajia/AndroidViewAnimations/blob/master/README.md*/
                        YoYo.with(Techniques.Tada)
                                .duration(700)
                                .playOn(SP_publicate_new_SubcategoryList);
                        /**************** End Animation ****************/
                        SnackbarManager.show(
                                Snackbar.with(PublicateNewActivity.this)
                                        .position(Snackbar.SnackbarPosition.TOP)
                                        .margin(15, 15)
                                        .backgroundDrawable(R.drawable.snackbar_custom_layout)
                                        .textColor(R.color.text_color_black)
                                        .text("Please choose Sub Category"));


                    } else if (Str_Set_product_name.equals("")) {
                        iserror = true;
                        Log.e(" Error :", "Ok");
                        v.performHapticFeedback(HapticFeedbackConstants.VIRTUAL_KEY);
                        /**************** Start Animation ****************/
                /*https://github.com/daimajia/AndroidViewAnimations/blob/master/README.md*/

                        YoYo.with(Techniques.Tada)
                                .duration(700)
                                .playOn(EDT_product_name);
                        /**************** End Animation ****************/
                        SnackbarManager.show(
                                Snackbar.with(PublicateNewActivity.this)
                                        .position(Snackbar.SnackbarPosition.TOP)
                                        .margin(15, 15)
                                        .backgroundDrawable(R.drawable.snackbar_custom_layout)
                                        .textColor(R.color.text_color_black)
                                        .text("Please enter Product Name"));


                    } else if (Str_publicate_newImage_path.equals("")) {
                        iserror = true;
                        Log.e(" Error :", "Ok");
                        v.performHapticFeedback(HapticFeedbackConstants.VIRTUAL_KEY);
                        /**************** Start Animation ****************/
                /*https://github.com/daimajia/AndroidViewAnimations/blob/master/README.md*/
                        YoYo.with(Techniques.Tada)
                                .duration(700)
                                .playOn(RL_select_product_image_btn);
                        /**************** End Animation ****************/
                        SnackbarManager.show(
                                Snackbar.with(PublicateNewActivity.this)
                                        .position(Snackbar.SnackbarPosition.TOP)
                                        .margin(15, 15)
                                        .backgroundDrawable(R.drawable.snackbar_custom_layout)
                                        .textColor(R.color.text_color_black)
                                        .text("Please select Product Image"));


                    } else if (Str_Set_product_price.equals("")) {
                        iserror = true;
                        Log.e(" Error :", "Ok");
                        v.performHapticFeedback(HapticFeedbackConstants.VIRTUAL_KEY);
                        /**************** Start Animation ****************/
                /*https://github.com/daimajia/AndroidViewAnimations/blob/master/README.md*/

                        YoYo.with(Techniques.Tada)
                                .duration(700)
                                .playOn(EDT_product_price);
                        /**************** End Animation ****************/
                        SnackbarManager.show(
                                Snackbar.with(PublicateNewActivity.this)
                                        .position(Snackbar.SnackbarPosition.TOP)
                                        .margin(15, 15)
                                        .backgroundDrawable(R.drawable.snackbar_custom_layout)
                                        .textColor(R.color.text_color_black)
                                        .text("Please enter Product Price"));


                    } else if (Str_Set_product_discription.equals("")) {
                        iserror = true;
                        Log.e(" Error :", "Ok");
                        v.performHapticFeedback(HapticFeedbackConstants.VIRTUAL_KEY);
                        /**************** Start Animation ****************/
                /*https://github.com/daimajia/AndroidViewAnimations/blob/master/README.md*/

                        YoYo.with(Techniques.Tada)
                                .duration(700)
                                .playOn(EDT_product_description);
                        /**************** End Animation ****************/
                        SnackbarManager.show(
                                Snackbar.with(PublicateNewActivity.this)
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
                            PublicateNewProductJsontask task = new PublicateNewProductJsontask();
                            task.execute();
                        } else {

                            SnackbarManager.show(
                                    Snackbar.with(PublicateNewActivity.this)
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



         /*%%%%%%%%%%%%%%      Spinner MainCategory (Start)        %%%%%%%%%%%%%%*/

         /*spinner click method and not clicked method for country (Start)*/
        SP_publicate_new_MaincategoryList.setOnItemSelectedListener(new MaterialSpinner.OnItemSelectedListener<String>() {

            @Override
            public void onItemSelected(MaterialSpinner view, int position, long id, String item) {
                SP_publicate_new_MaincategoryList.performHapticFeedback(HapticFeedbackConstants.VIRTUAL_KEY);
                Log.e("Category Detail :", "Position :" + "" + position
                        + "\t" + "ID :" + "" + id
                        + "\t" + "Name :" + "" + item);

                /*android.support.design.widget.Snackbar.make(view, "Clicked " + item,
                        android.support.design.widget.Snackbar.LENGTH_SHORT).show();*/
                Str_Get_User_MainCategory_Selected_Value = item;
                Str_Get_MainCategory_Value = item;
                Str_Set_MainCategory_Value = Str_Get_MainCategory_Value;
                long pos = id;
                int posi = position;
                Log.e("pos :", "" + pos);
                Log.e("posi ID:", "" + posi);
                Str_Get_Main_Category_id_Position = String.valueOf(posi);
                Str_GetSet_MainCategory_ID = MAIN_CATEGORY_LISTID.get(position);
                Log.e("Str_GetSet_MainCategory_ID :", "" + Str_GetSet_MainCategory_ID);
                Log.e("Str_Get_Main_Category_id_Position :", "" + Str_Get_Main_Category_id_Position);
                Log.e("Str_Get_MainCategory_Value :", "" + Str_Get_MainCategory_Value);
                Log.e("Str_Set_MainCategory_Value :", "" + Str_Set_MainCategory_Value);
                Log.e("Str_Get_User_MainCategory_Selected_Value :", "" + Str_Get_User_MainCategory_Selected_Value);


                if (Utils.isConnected(getApplicationContext())) {
                    Log.e("SubCategoryListJsontask Calling :", "OK");
                    SubCategoryListJsontask task = new SubCategoryListJsontask();
                    task.execute();
                } else {

                    SnackbarManager.show(
                            Snackbar.with(getApplicationContext())
                                    .position(Snackbar.SnackbarPosition.TOP)
                                    .margin(15, 15)
                                    .backgroundDrawable(R.drawable.snackbar_custom_layout)
                                    .text("Please Your Internet Connectivity..!!"));

                }


            }
        });
        SP_publicate_new_MaincategoryList.setOnNothingSelectedListener(new MaterialSpinner.OnNothingSelectedListener() {

            @Override
            public void onNothingSelected(MaterialSpinner spinner) {
                android.support.design.widget.Snackbar.make(spinner, "Please select Category First..!!",
                        android.support.design.widget.Snackbar.LENGTH_SHORT).show();


            }
        });
        /*%%%%%%%%%%%%%%      Spinner MainCategory (End)        %%%%%%%%%%%%%%*/





         /*%%%%%%%%%%%%%%      Spinner SubCategory (Start)        %%%%%%%%%%%%%%*/

         /*spinner click method and not clicked method for country (Start)*/
        SP_publicate_new_SubcategoryList.setOnItemSelectedListener(new MaterialSpinner.OnItemSelectedListener<String>() {

            @Override
            public void onItemSelected(MaterialSpinner view, int position, long id, String item) {
                SP_publicate_new_SubcategoryList.performHapticFeedback(HapticFeedbackConstants.VIRTUAL_KEY);
                Log.e("SubCategory Detail :", "Position :" + "" + position
                        + "\t" + "ID :" + "" + id
                        + "\t" + "Name :" + "" + item);

                /*android.support.design.widget.Snackbar.make(view, "Clicked " + item,
                        android.support.design.widget.Snackbar.LENGTH_SHORT).show();*/
                Str_Get_User_SubCategory_Selected_Value = item;
                Str_Get_SubCategory_Value = item;
                Str_Set_SubCategory_Value = Str_Get_SubCategory_Value;
                long pos = id;
                int posi = position;
                Log.e("pos :", "" + pos);
                Log.e("posi ID:", "" + posi);
                Str_Get_Sub_Category_id_Position = String.valueOf(posi);
                Str_GetSet_SubCategory_ID = SUB_CATEGORY_LISTID.get(position);
                Log.e("Str_GetSet_SubCategory_ID :", "" + Str_GetSet_SubCategory_ID);
                Log.e("Str_Get_Sub_Category_id_Position :", "" + Str_Get_Sub_Category_id_Position);
                Log.e("Str_Get_SubCategory_Value :", "" + Str_Get_SubCategory_Value);
                Log.e("Str_Set_SubCategory_Value :", "" + Str_Set_SubCategory_Value);
                Log.e("Str_Get_User_SubCategory_Selected_Value :", "" + Str_Get_User_SubCategory_Selected_Value);


            }
        });

        SP_publicate_new_SubcategoryList.setOnNothingSelectedListener(new MaterialSpinner.OnNothingSelectedListener() {

            @Override
            public void onNothingSelected(MaterialSpinner spinner) {
                android.support.design.widget.Snackbar.make(spinner, "Please select SubCategory First..!!",
                        android.support.design.widget.Snackbar.LENGTH_SHORT).show();


            }
        });
        /*%%%%%%%%%%%%%%      Spinner SubCategory (End)        %%%%%%%%%%%%%%*/


    }


    private void updateValuesImage() {
        CircularProgressDrawable circularProgressDrawable;
        CircularProgressDrawable.Builder b = new CircularProgressDrawable.Builder(this)
                .colors(getResources().getIntArray(R.array.gplus_colors))
                .style(CircularProgressDrawable.STYLE_ROUNDED);
        mCPBpublicate_new.setIndeterminateDrawable(circularProgressDrawable = b.build());


        circularProgressDrawable.setBounds(0,
                0,
                mCPBpublicate_new.getWidth(),
                mCPBpublicate_new.getHeight());
        mCPBpublicate_new.setVisibility(View.INVISIBLE);
        mCPBpublicate_new.setVisibility(View.VISIBLE);
    }


    private void publicate_newImagePicker() {
        QuickTipDialog = new Dialog(PublicateNewActivity.this);
//                callFeeDialog = new Dialog(MainActivity.this);
        QuickTipDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        QuickTipDialog.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
        QuickTipDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        QuickTipDialog.setContentView(R.layout.activity_dialog_image_picker);
        final RelativeLayout RlCamera = (RelativeLayout) QuickTipDialog.findViewById(R.id.rl_inner_camera);
        final RelativeLayout RlGallery = (RelativeLayout) QuickTipDialog.findViewById(R.id.rl_inner_gallery);
        final TextView TvCancel = (TextView) QuickTipDialog.findViewById(R.id.tv_dialog_cancel);

        TvCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                QuickTipDialog.dismiss();
            }
        });

        RlCamera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                new PickerBuilder(PublicateNewActivity.this, PickerBuilder.SELECT_FROM_CAMERA)
                        .setOnImageReceivedListener(new PickerBuilder.onImageReceivedListener() {
                            @Override
                            public void onImageReceived(Uri imageUri) {
//                                Toast.makeText(PublicateNewActivity.this, "Got image - " + imageUri, Toast.LENGTH_LONG).show();
                                IV_product_image.setImageURI(imageUri);
                                Log.e("imageUri path SELECT_FROM_CAMERA :", "" + imageUri);
                                File f = new File(imageUri.getPath());
                                Str_publicate_newImage_path = imageUri.getPath();
                                Log.e("Str_publicate_newImage_path SELECT_FROM_CAMERA :", "" + Str_publicate_newImage_path);
                                publicate_newImage = new File(Str_publicate_newImage_path);
                                Str_publicate_newImage_pathName = publicate_newImage.getName();
                                Log.e("Str_publicate_newImage_pathName :", "" + Str_publicate_newImage_pathName);
                                Log.e("publicate_newImage File Image:", "" + publicate_newImage);
                                TV_product_image_path.setText(Str_publicate_newImage_pathName);
                                QuickTipDialog.dismiss();
                            }
                        })
                        .setImageName("PublicateImage")
                        .setImageFolderName("SevillaFolder")
                        .withTimeStamp(false)
                        .setCropScreenColor(Color.CYAN)
                        .start();

            }
        });

        RlGallery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                new PickerBuilder(PublicateNewActivity.this, PickerBuilder.SELECT_FROM_GALLERY)
                        .setOnImageReceivedListener(new PickerBuilder.onImageReceivedListener() {
                            @Override
                            public void onImageReceived(Uri imageUri) {
                                IV_product_image.setImageURI(imageUri);
//                                Toast.makeText(PublicateNewActivity.this, "Got image - " + imageUri, Toast.LENGTH_LONG).show();
                                Log.e("imageUri path SELECT_FROM_GALLERY :", "" + imageUri);
                                File f = new File(imageUri.getPath());
                                Str_publicate_newImage_path = imageUri.getPath();
                                Log.e("Str_publicate_newImage_path SELECT_FROM_GALLERY :", "" + Str_publicate_newImage_path);
                                publicate_newImage = new File(Str_publicate_newImage_path);
                                Str_publicate_newImage_pathName = publicate_newImage.getName();
                                Log.e("publicate_newImage File Image:", "" + publicate_newImage);
                                Log.e("Str_publicate_newImage_pathName :", "" + Str_publicate_newImage_pathName);
                                TV_product_image_path.setText(Str_publicate_newImage_pathName);
                                QuickTipDialog.dismiss();
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


        QuickTipDialog.show();
    }


    private class MainCategoryListJsontask extends AsyncTask<String, Void, ArrayList<String>> {

        boolean iserror = false;

        @Override
        protected void onPreExecute() {
            // TODO Auto-generated method stub
            //  loginprogressbar.setVisibility(View.VISIBLE);
            Log.e("******* MainCategoryListJsontask IS RUNNING *******", "YES");
            Log.e("******* MainCategoryListJsontask IS RUNNING *******", "YES");
            Log.e("Main Category URL :", ""
                    + "http://sevilla.centrocomercial.com.es/wp-content/plugins/webserv/categories_name.php");
            RL_publicate_new_progress.setVisibility(View.VISIBLE);
        }

        @Override
        protected ArrayList<String> doInBackground(String... params) {
            Log.e("******* NOW MainCategoryListJsontask BACKGROUND TASK IS RUNNING *******", "YES");
            Log.e("******* NOW MainCategoryListJsontask BACKGROUND TASK IS RUNNING *******", "YES");

            HttpClient clientMainCategory = new DefaultHttpClient();
            HttpPost postMainCategory = new HttpPost("http://sevilla.centrocomercial.com.es/wp-content/plugins/webserv/categories_name.php");

            try {
                HttpResponse responseMainCategory = clientMainCategory.execute(postMainCategory);
                String objectMainCategory = EntityUtils.toString(responseMainCategory.getEntity());
                Log.e("doinBackgrouns MainCategoryListJsontask List Responce :", "" + objectMainCategory);

                MAIN_CATEGORY_LIST.add("Select Main Category :");
                MAIN_CATEGORY_LISTID.add("0");
                JSONObject jsonObjectMainCategory = new JSONObject(objectMainCategory);
                Str_Get_Main_Category_Status = jsonObjectMainCategory.getString("status");
                Str_Get_Main_Category_List = jsonObjectMainCategory.getString("catagories");
                Log.e("**** Json Main Category List data *********", " " + Str_Get_Main_Category_List);

                if (Str_Get_Main_Category_Status.equals("OK")) {
                    Log.e("doInBackground Str_Get_Main_Category_Status is", "OK");

                    JSONArray jsonArrayMainCategory = new JSONArray(Str_Get_Main_Category_List);
                    for (int i = 0; i < jsonArrayMainCategory.length(); i++) {
                        JSONObject jsonMainCategoryObject = jsonArrayMainCategory.getJSONObject(i);
                        Str_Get_Main_Category_Name = jsonMainCategoryObject.getString("category_name");
                        Str_Get_Main_Category_id = jsonMainCategoryObject.getString("category_id");
                        Str_Get_Main_Category_Name = String.valueOf(Html.fromHtml(Str_Get_Main_Category_Name));
                        MAIN_CATEGORY_LIST.add(Str_Get_Main_Category_Name);
                        MAIN_CATEGORY_LISTID.add(Str_Get_Main_Category_id);

                        Str_Get_Main_Category_List_result = jsonArrayMainCategory.getJSONObject(i).getString("catagories_result");


                        Log.e(" HTML List Str_Get_Main_Category_Name :", "" + Str_Get_Main_Category_Name);
                        Log.e(" HTML List Str_Get_Main_Category_id :", "" + Str_Get_Main_Category_id);
                        Log.e("Convert String Formate List Str_Get_Main_Category_Name :", "" + Str_Get_Main_Category_Name);
                        Log.e("List Str_Get_Main_Category_Name :", "" + Str_Get_Main_Category_Name);
                        Log.e("Str_Get_Main_Category_List_result :", "" + Str_Get_Main_Category_List_result);
                        Log.e("Str_Get_Main_Category_Id :", "" + Str_Get_Main_Category_id);

                    }

                }


                return MAIN_CATEGORY_LIST;

            } catch (Exception e) {
                Log.v("22", "22" + e.getMessage());
                e.printStackTrace();
                iserror = true;
            }
            return null;
        }

        @Override
        protected void onPostExecute(ArrayList<String> resultMainCategory) {
            // TODO Auto-generated method stub
            super.onPostExecute(resultMainCategory);
            RL_publicate_new_progress.setVisibility(View.GONE);

            if (!iserror) {

                if (resultMainCategory == null) {
                    Log.e("result1 :", "Null");
                } else if (resultMainCategory.isEmpty()) {

                    Log.e("result1 :", "empty");
                } else {
                    ArrayAdapter<String> adapter = new ArrayAdapter<String>(getApplicationContext(),
                            android.R.layout.simple_spinner_dropdown_item, resultMainCategory);
                    SP_publicate_new_MaincategoryList.setItems(resultMainCategory);
                    Str_Get_Main_Category_User_Default = resultMainCategory.get(0);
                    Log.e("Str_Get_Main_Category_User_Default ", "" + Str_Get_Main_Category_User_Default);
                    Log.e(" Main Category list result :", "" + resultMainCategory.size());

                }

            }

        }

    }


    private class SubCategoryListJsontask extends AsyncTask<String, Void, ArrayList<String>> {

        boolean iserror = false;

        @Override
        protected void onPreExecute() {
            // TODO Auto-generated method stub
            //  loginprogressbar.setVisibility(View.VISIBLE);
            Log.e("******* MainCategoryListJsontask IS RUNNING *******", "YES");
            Log.e("******* MainCategoryListJsontask IS RUNNING *******", "YES");
            Log.e("Sub Category URL :", ""
                    + "http://sevilla.centrocomercial.com.es/wp-content/plugins/webserv/sub_category_name.php?cate_id=" + Str_GetSet_MainCategory_ID);
            RL_publicate_new_progress.setVisibility(View.VISIBLE);
            SUB_CATEGORY_LIST.clear();
            SUB_CATEGORY_LISTID.clear();
        }

        @Override
        protected ArrayList<String> doInBackground(String... params) {
            Log.e("******* NOW SubCategoryListJsontask BACKGROUND TASK IS RUNNING *******", "YES");
            Log.e("******* NOW SubCategoryListJsontask BACKGROUND TASK IS RUNNING *******", "YES");

            HttpClient clientSubCategory = new DefaultHttpClient();
            HttpPost postSubCategory = new HttpPost("http://sevilla.centrocomercial.com.es/wp-content/plugins/webserv/sub_category_name.php?cate_id=" + Str_GetSet_MainCategory_ID);

            try {
                HttpResponse responseMainCategory = clientSubCategory.execute(postSubCategory);
                String objectSubCategory = EntityUtils.toString(responseMainCategory.getEntity());
                Log.e("doinBackground SubCategoryListJsontask List Response :", "" + objectSubCategory);

                SUB_CATEGORY_LIST.add("Select Sub Category :");
                SUB_CATEGORY_LISTID.add("0");
                JSONObject jsonObjectSubCategory = new JSONObject(objectSubCategory);
                Str_Get_Sub_Category_Status = jsonObjectSubCategory.getString("status");
                Str_Get_Sub_Category_List = jsonObjectSubCategory.getString("sub_catagories");
                Log.e("**** Json Sub Category List data *********", " " + Str_Get_Sub_Category_List);

                if (Str_Get_Sub_Category_Status.equals("OK")) {
                    Log.e("doInBackground Str_Get_Sub_Category_Status is", "OK");

                    JSONArray jsonArraySubCategory = new JSONArray(Str_Get_Sub_Category_List);
                    for (int i = 0; i < jsonArraySubCategory.length(); i++) {
                        JSONObject jsonSubCategoryObject = jsonArraySubCategory.getJSONObject(i);
                        Str_Get_Sub_Category_Name = jsonSubCategoryObject.getString("sub_category_name");
                        Str_Get_Sub_Category_id = jsonSubCategoryObject.getString("sub_category_id");
                        Str_Get_Sub_Category_Name = String.valueOf(Html.fromHtml(Str_Get_Sub_Category_Name));
                        SUB_CATEGORY_LIST.add(Str_Get_Sub_Category_Name);
                        SUB_CATEGORY_LISTID.add(Str_Get_Sub_Category_id);

                        Str_Get_Sub_Category_List_result = jsonArraySubCategory.getJSONObject(i).getString("sub_category_result");


                        Log.e(" HTML List Str_Get_Sub_Category_Name :", "" + Str_Get_Sub_Category_Name);
                        Log.e(" HTML List Str_Get_Sub_Category_id :", "" + Str_Get_Sub_Category_id);
                        Log.e("Convert String Formate List Str_Get_Sub_Category_Name :", "" + Str_Get_Sub_Category_Name);
                        Log.e("List Str_Get_Sub_Category_Name :", "" + Str_Get_Sub_Category_Name);
                        Log.e("Str_Get_Sub_Category_List_result :", "" + Str_Get_Sub_Category_List_result);
                        Log.e("Str_Get_Sub_Category_id :", "" + Str_Get_Sub_Category_id);

                    }

                }


                return SUB_CATEGORY_LIST;

            } catch (Exception e) {
                Log.v("22", "22" + e.getMessage());
                e.printStackTrace();
                iserror = true;
            }
            return null;
        }

        @Override
        protected void onPostExecute(ArrayList<String> resultSubCategory) {
            // TODO Auto-generated method stub
            super.onPostExecute(resultSubCategory);
            RL_publicate_new_progress.setVisibility(View.GONE);

            if (!iserror) {

                if (resultSubCategory == null) {
                    Log.e("result1 :", "Null");
                } else if (resultSubCategory.isEmpty()) {

                    Log.e("result1 :", "empty");
                } else {
                    ArrayAdapter<String> adapter = new ArrayAdapter<String>(getApplicationContext(),
                            android.R.layout.simple_spinner_dropdown_item, resultSubCategory);
                    SP_publicate_new_SubcategoryList.setItems(resultSubCategory);
                    Str_Get_Sub_Category_User_Default = resultSubCategory.get(0);
                    Log.e("Str_Get_Sub_Category_User_Default ", "" + Str_Get_Sub_Category_User_Default);

                    Log.e(" Sub Category list result :", "" + resultSubCategory.size());
                }

            } else {
                SnackbarManager.show(Snackbar.with(PublicateNewActivity.this)
                        .position(Snackbar.SnackbarPosition.TOP)
                        .margin(15, 15)
                        .backgroundDrawable(R.drawable.snackbar_custom_layout)
                        .text("No SubCategory found..!!"));
            }

        }

    }


    private class PublicateNewProductJsontask extends AsyncTask<String, Void, String> {

        boolean iserror = false;

        @Override
        protected void onPreExecute() {
            // TODO Auto-generated method stub
            //  loginprogressbar.setVisibility(View.VISIBLE);
            Log.e("******* NOW PublicateNewProductJsontask WEB SERVICE IS RUNNING *******", "YES");
            Log.e("******* NOW PublicateNewProductJsontask WEB SERVICE IS RUNNING *******", "YES");


            Log.e("User_ID :", "" + User_ID
                    + "\n" + "Str_Set_product_name :" + "" + Str_Set_product_name
                    + "\n" + "Str_Set_product_price :" + "" + Str_Set_product_price
                    + "\n" + "Str_Set_product_discription :" + "" + Str_Set_product_discription
                    + "\n" + "Str_publicate_newImage_path :" + "" + Str_publicate_newImage_path
                    + "\n" + "Str_publicate_newImage_pathName :" + "" + Str_publicate_newImage_pathName
                    + "\n" + "PublicateNewfilebody :" + "" + PublicateNewfilebody
                    + "\n" + "Str_GetSet_MainCategory_ID :" + "" + Str_GetSet_MainCategory_ID
                    + "\n" + "Str_Get_User_MainCategory_Selected_Value :" + "" + Str_Get_User_MainCategory_Selected_Value
                    + "\n" + "Str_GetSet_SubCategory_ID :" + "" + Str_GetSet_SubCategory_ID
                    + "\n" + "Str_Get_User_SubCategory_Selected_Value :" + "" + Str_Get_User_SubCategory_Selected_Value);

            Log.e("onPreExecute Publicate Product URL :", ""
                    + "http://sevilla.centrocomercial.com.es/wp-content/plugins/webserv/add_seller_product.php?user_id=" + User_ID
                    + "&product_name=" + Str_Set_product_name
                    + "&product_description=" + Str_Set_product_discription
                    + "&product_price=" + Str_Set_product_price
                    + "&product_image=" + PublicateNewfilebody
                    + "&cat_id=" + Str_GetSet_MainCategory_ID
                    + "&subcat_id=" + Str_GetSet_SubCategory_ID);

            if (Str_publicate_newImage_path.equalsIgnoreCase("")) {
                Str_publicate_newImage_path = "Ritesh";
            }
            RL_publicate_new_progress.setVisibility(View.VISIBLE);


        }

        @Override
        protected String doInBackground(String... params) {
            Log.e("******* NOW PublicateNewProductJsontask TASK IS RUNNING *******", "YES");


            try {
                HttpClient client = new DefaultHttpClient();
                HttpPost post = new HttpPost("http://sevilla.centrocomercial.com.es/wp-content/plugins/webserv/add_seller_product.php?user_id=" + User_ID);

                Log.e("doInBackground Publicate Product URL :", ""
                        + "http://sevilla.centrocomercial.com.es/wp-content/plugins/webserv/add_seller_product.php?user_id=" + User_ID
                        + "&product_name=" + Str_Set_product_name
                        + "&product_description=" + Str_Set_product_discription
                        + "&product_price=" + Str_Set_product_price
                        + "&product_image=" + PublicateNewfilebody
                        + "&cat_id=" + Str_GetSet_MainCategory_ID
                        + "&subcat_id=" + Str_GetSet_SubCategory_ID);

                MultipartEntity reqEntity = new MultipartEntity(HttpMultipartMode.BROWSER_COMPATIBLE);
                reqEntity.addPart("product_name", new StringBody(Str_Set_product_name));
                reqEntity.addPart("product_description", new StringBody(Str_Set_product_discription));
                reqEntity.addPart("product_price", new StringBody(Str_Set_product_price));
                reqEntity.addPart("cat_id", new StringBody(Str_GetSet_MainCategory_ID));
                reqEntity.addPart("subcat_id", new StringBody(Str_GetSet_SubCategory_ID));

                if (publicate_newImage == null) {
                    Log.e("ProfileImage File is :", "NULL");
                    Str_publicate_newImage_path = "Ritesh";

                } else {
                    /******************* file for post the profile image to the server *******************/
                    PublicateNewfilebody = new FileBody(publicate_newImage);
                    reqEntity.addPart("product_image", PublicateNewfilebody);
                    Log.e("PublicateNewfilebody image :", "" + PublicateNewfilebody.toString());
                    /******************* file for post the profile image to the server *******************/
                }

                /*List<NameValuePair> nameValuePairs = new ArrayList<>();
                nameValuePairs.add(new BasicNameValuePair("user_id", User_ID));
                Log.e("******* ID TO SERVER FOR MACHING*******", "" + User_ID);*/

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
                Str_publicate_new_Result = parentObject.getString("result");
                Str_publicate_new_Status = parentObject.getString("status");
                Str_publicate_new_Message = parentObject.getString("message");
                Log.e("*********** Str_publicate_new_Result *********** : ", Str_publicate_new_Result);
                Log.e("*********** Str_publicate_new_Status *********** : ", Str_publicate_new_Status);
                Log.e("*********** Str_publicate_new_Message *********** : ", Str_publicate_new_Message);

            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            } catch (ClientProtocolException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (JSONException e) {
                e.printStackTrace();
            }

            return Str_publicate_new_Result;
        }

        @Override
        protected void onPostExecute(String result1) {
            // TODO Auto-generated method stub
            super.onPostExecute(result1);
            RL_publicate_new_progress.setVisibility(View.GONE);

            if (!iserror) {
                if (Str_publicate_new_Result.equalsIgnoreCase("success")) {
                    Log.e("GoMainSellerScreen ", "OK");
                    SnackbarManager.show(
                            Snackbar.with(PublicateNewActivity.this)
                                    .position(Snackbar.SnackbarPosition.TOP)
                                    .margin(15, 15)
                                    .backgroundDrawable(R.drawable.snackbar_custom_layout)
                                    .text("Product Published..!!"));

                    Intent GoMainSellerScreen = new Intent(PublicateNewActivity.this, MainSellerActivity.class);
                    GoMainSellerScreen.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(GoMainSellerScreen);
                    finish();


                } else {
                    Log.e("onPostExecute Sub_cat_id size is Zero ", "ooppss");
                    SnackbarManager.show(
                            Snackbar.with(PublicateNewActivity.this)
                                    .position(Snackbar.SnackbarPosition.TOP)
                                    .margin(15, 15)
                                    .backgroundDrawable(R.drawable.snackbar_custom_layout)
                                    .text("Error"));
                }
            }
        }

    }


}
