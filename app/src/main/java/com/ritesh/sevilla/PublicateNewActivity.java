package com.ritesh.sevilla;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
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

import com.facebook.FacebookSdk;
import com.jaredrummler.materialspinner.MaterialSpinner;
import com.nispok.snackbar.Snackbar;
import com.nispok.snackbar.SnackbarManager;
import com.ritesh.sevilla.Constant.Appconstant;
import com.ritesh.sevilla.Constant.Utils;

import org.apache.http.entity.mime.content.FileBody;

import java.io.File;
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

public class PublicateNewActivity extends AppCompatActivity {

    @BindView(R.id.toolbar_publicate_new)
    Toolbar TB_publicate_new;

    @BindView(R.id.spinner_publicate_new_category)
    MaterialSpinner SP_publicate_new_category;

    @BindView(R.id.spinner_publicate_new_subcategory)
    MaterialSpinner SP_publicate_new_subcategory;


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
            Str_publicate_newImage_path = "";

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
                    /*Intent MyCartPage = new Intent(GetDeliveryAddress.this, MyCartActivity.class);
                    startActivity(MyCartPage);*/

                    SnackbarManager.show(
                            Snackbar.with(PublicateNewActivity.this)
                                    .position(Snackbar.SnackbarPosition.TOP)
                                    .margin(15, 15)
                                    .backgroundDrawable(R.drawable.snackbar_custom_layout)
                                    .text("Publicate Clicked"));

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


    /*private List<TextLengthBarState> buildStates() {

        List<TextLengthBarState> states = new ArrayList<>();

        states.add(new TextLengthBarState.Builder(50, "Add %d more characters for a great review.")
                .backgroundColor(R.color.first_state_color)
                .icon(R.drawable.ic_first_step)
                .build());

        states.add(new TextLengthBarState.Builder(100, "Help others by adding %d more characters.")
                .backgroundColor(R.color.second_state_color)
                .icon(R.drawable.ic_second_state)
                .build());

        states.add(new TextLengthBarState.Builder(150, "You're doing great.")
                .backgroundColor(R.color.third_state_color)
                .icon(R.drawable.ic_third_state)
                .build());

        return states;
    }*/



}
