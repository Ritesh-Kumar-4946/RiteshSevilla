package com.ritesh.sevilla;

import android.app.AlertDialog;
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
import android.support.v7.widget.CardView;
import android.text.Html;
import android.util.Log;
import android.view.HapticFeedbackConstants;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;
import com.nispok.snackbar.Snackbar;
import com.nispok.snackbar.SnackbarManager;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
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
import org.apache.http.entity.mime.MultipartEntityBuilder;
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
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import cn.pedant.SweetAlert.SweetAlertDialog;
import de.hdodenhof.circleimageview.CircleImageView;
import fr.castorflex.android.circularprogressbar.CircularProgressBar;
import fr.castorflex.android.circularprogressbar.CircularProgressDrawable;

/**
 * Created by ritesh on 9/3/17.
 */
@SuppressWarnings("deprecation")
public class EditProfileActivity extends AppCompatActivity {

    @BindView(R.id.rl_edit_profile_btn)
    RelativeLayout Rl_edit_profile_btn;

    @BindView(R.id.civ_edit_profile_image)
    CircleImageView Civ_edit_profile_image;

    @BindView(R.id.tv_edit_user_profile_name)
    TextView TV_edit_user_profile_name;


    @BindView(R.id.edt_et_edit_profile_username)
    EditText EDT_et_edit_profile_username;

    @BindView(R.id.edt_et_edit_profile_phone_number)
    EditText EDT_et_edit_profile_phone_number;
/*
    @BindView(R.id.edt_et_edit_profile_password)
    EditText EDT_et_edit_profile_password;

    @BindView(R.id.edt_et_edit_profile_re_password)
    EditText EDT_et_edit_profile_re_password;*/

    @BindView(R.id.cv_et_sign_btn_edit_profile)
    CardView CV_et_sign_btn_edit_profile;

    @BindView(R.id.cv_et_sign_btn_edit_profile_click)
    CardView CV_et_sign_btn_edit_profile_click;


    @BindView(R.id.rl_edit_profile_image_progress)
    RelativeLayout RL_edit_profile_image_progress;
    CircularProgressBar mCircularProgressBarEditProfileImage;


    @BindView(R.id.rl_edit_profile_main_progress)
    RelativeLayout RL_edit_profile_main_progress;
    CircularProgressBar mCircularProgressBarEditProfile;


    String
            User_ID = "",
            Str_Get_User_ID = "",
            Str_Get_message = "",
            Str_Get_user_image = "",
            Str_Set_user_image = "",
            Str_Get_user_name = "",
            Str_Set_user_name = "",
            Str_Get_phone_number = "",
            Str_Set_phone_number = "",
            Str_Set_password = "",
            Str_Set_re_password = "",
            Str_profileImage_path = "",
            result = "",
            Str_Get_Status = "";

    Dialog QuickTipDialog;

    File ProfileImage;
    FileBody ProdileImagefilebody;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);
        ButterKnife.bind(this);

        Appconstant.sh = getSharedPreferences(Appconstant.MyPREFERENCES, Context.MODE_PRIVATE);
        User_ID = Appconstant.sh.getString("id", null);
        Log.e("User_ID from SharedPref :", "" + User_ID);

        if (Utils.isConnected(getApplicationContext())) {
            EditProfileGetUserDetailJsontask task = new EditProfileGetUserDetailJsontask();
            task.execute();
        } else {

            SnackbarManager.show(
                    Snackbar.with(EditProfileActivity.this)
                            .position(Snackbar.SnackbarPosition.TOP)
                            .margin(15, 15)
                            .backgroundDrawable(R.drawable.snackbar_custom_layout)
                            .text("Please Your Internet Connectivity..!!"));

        }


        /*circular progress bar (Start)*/
        mCircularProgressBarEditProfileImage = (CircularProgressBar) findViewById(R.id.civ_edit_profile_image_progressbar_circular);
        ((CircularProgressDrawable) mCircularProgressBarEditProfileImage.getIndeterminateDrawable()).start();
        updateValuesImage();

        mCircularProgressBarEditProfile = (CircularProgressBar) findViewById(R.id.civ_edit_profile_main_progressbar_circular);
        ((CircularProgressDrawable) mCircularProgressBarEditProfile.getIndeterminateDrawable()).start();
        updateValuesMain();
        /*circular progress bar (End)*/


        /*image settin by universal image loader */
        DisplayImageOptions defaultOptions = new DisplayImageOptions.Builder().cacheInMemory(true).cacheOnDisk(true).build();
        ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(EditProfileActivity.this).defaultDisplayImageOptions(defaultOptions).build();
        ImageLoader.getInstance().init(config);
        /*image settin by universal image loader */


        CV_et_sign_btn_edit_profile.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {

                if (event.getAction() == MotionEvent.ACTION_DOWN) {

                    Log.e("Action ", "Down");

                    CV_et_sign_btn_edit_profile_click.setVisibility(View.VISIBLE);
                    CV_et_sign_btn_edit_profile.setVisibility(View.GONE);
                    v.performHapticFeedback(HapticFeedbackConstants.VIRTUAL_KEY);


                    return true;
                }

                if (event.getAction() == MotionEvent.ACTION_MOVE) {

                    Log.e("Action ", "Move");

                    CV_et_sign_btn_edit_profile_click.setVisibility(View.VISIBLE);
                    CV_et_sign_btn_edit_profile.setVisibility(View.GONE);

                    return true;

                }
                if (event.getAction() == MotionEvent.ACTION_UP) {

                    Log.e("Action ", "Up");
                    CV_et_sign_btn_edit_profile.setVisibility(View.VISIBLE);
                    CV_et_sign_btn_edit_profile_click.setVisibility(View.GONE);

                    Str_Set_user_name = EDT_et_edit_profile_username.getText().toString().trim();
                    Str_Set_phone_number = EDT_et_edit_profile_phone_number.getText().toString().trim();
//                    Str_Set_password = EDT_et_edit_profile_password.getText().toString().trim();
//                    Str_Set_re_password = EDT_et_edit_profile_re_password.getText().toString().trim();

                    Log.e(" Update Fields data :", "\n"
                            + "Str_Set_user_name :" + "" + Str_Set_user_name + "\n"
                            + "Str_Set_password :" + "" + Str_Set_password + "\n"
                            + "Str_Set_re_password :" + "" + Str_Set_re_password + "\n"
                            + "Str_Set_phone_number :" + "" + Str_Set_phone_number + "\n");

                    if (Utils.isConnected(getApplicationContext())) {
                        UpdateProfileJsontask task = new UpdateProfileJsontask();
                        task.execute();
                    } else {

                        SnackbarManager.show(
                                Snackbar.with(EditProfileActivity.this)
                                        .position(Snackbar.SnackbarPosition.TOP)
                                        .margin(15, 15)
                                        .backgroundDrawable(R.drawable.snackbar_custom_layout)
                                        .text("Please Your Internet Connectivity..!!"));

                    }


//                    Toast.makeText(EditProfileActivity.this, "Comming Soon", Toast.LENGTH_SHORT).show();
//                    CV_et_sign_btn_edit_profile_click.setVisibility(View.GONE);
//                    CV_et_sign_btn_edit_profile.setVisibility(View.VISIBLE);
                    return true;
                }


                return false;
            }
        });


        Rl_edit_profile_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                v.performHapticFeedback(HapticFeedbackConstants.VIRTUAL_KEY);
//                Toast.makeText(EditProfileActivity.this, "Image Picker", Toast.LENGTH_SHORT).show();

                ProfileImagePicker();
            }
        });


    }


    /*progressbar data (Start)*/
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
        mCircularProgressBarEditProfileImage.setIndeterminateDrawable(circularProgressDrawable = b.build());

        // /!\ Terrible hack, do not do this at home!
        circularProgressDrawable.setBounds(0,
                0,
                mCircularProgressBarEditProfileImage.getWidth(),
                mCircularProgressBarEditProfileImage.getHeight());
        mCircularProgressBarEditProfileImage.setVisibility(View.INVISIBLE);
        mCircularProgressBarEditProfileImage.setVisibility(View.VISIBLE);
    }

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
        mCircularProgressBarEditProfile.setIndeterminateDrawable(circularProgressDrawable = b.build());

        // /!\ Terrible hack, do not do this at home!
        circularProgressDrawable.setBounds(0,
                0,
                mCircularProgressBarEditProfile.getWidth(),
                mCircularProgressBarEditProfile.getHeight());
        mCircularProgressBarEditProfile.setVisibility(View.INVISIBLE);
        mCircularProgressBarEditProfile.setVisibility(View.VISIBLE);
    }
    /*progressbar data (End)*/


    private class EditProfileGetUserDetailJsontask extends AsyncTask<String, Void, String> {

        boolean iserror = false;

        @Override
        protected void onPreExecute() {
            // TODO Auto-generated method stub
            //  loginprogressbar.setVisibility(View.VISIBLE);
            Log.e("******* NOW EditProfileGetUserDetailJsontask WEB SERVICE IS RUNNING *******", "YES");
            Log.e("******* NOW EditProfileGetUserDetailJsontask WEB SERVICE IS RUNNING *******", "YES");
            Log.e("User_ID onPreExecute :", "" + User_ID);
            RL_edit_profile_image_progress.setVisibility(View.VISIBLE);
            RL_edit_profile_main_progress.setVisibility(View.VISIBLE);

        }

        @Override
        protected String doInBackground(String... params) {
            Log.e("******* NOW Login TASK IS RUNNING *******", "YES");
            HttpClient client = new DefaultHttpClient();
            HttpPost post = new HttpPost("http://sevilla.centrocomercial.com.es/wp-content/plugins/webserv/get_user.php?user_id=" + User_ID);

            Log.e("URL :", "" + "http://sevilla.centrocomercial.com.es/wp-content/plugins/webserv/get_user.php?user_id=" + User_ID);

            try {
                HttpResponse response = client.execute(post);
                String object = EntityUtils.toString(response.getEntity());
                Log.e("*******object******** :", "" + object);

                //JSONArray js = new JSONArray(object);
                JSONObject jobect = new JSONObject(object);
                Str_Get_Status = jobect.getString("status");
                if (Str_Get_Status.equalsIgnoreCase("1")) {
                    Str_Get_User_ID = jobect.getString("ID");
                    Str_Get_user_name = jobect.getString("user_name");
                    Str_Get_user_image = jobect.getString("user_image");
                    Str_Get_phone_number = jobect.getString("phone_number");
                    Str_Get_message = jobect.getString("message");

                }

            } catch (Exception e) {
                Log.v("22", "22" + e.getMessage());
                e.printStackTrace();
                iserror = true;
            }

            return Str_Get_Status;
        }

        @Override
        protected void onPostExecute(String result1) {
            // TODO Auto-generated method stub
            super.onPostExecute(result1);
            RL_edit_profile_main_progress.setVisibility(View.GONE);

            if (!iserror) {
                if (Str_Get_Status.equalsIgnoreCase("1")) {

                    Log.e("Str_Get_User_ID :", "" + Str_Get_User_ID);
                    Log.e("Str_Get_user_name :", "" + Str_Get_user_name);
                    Log.e("Str_Get_user_image :", "" + Str_Get_user_image);
                    Log.e("Str_Get_phone_number :", "" + Str_Get_phone_number);
                    Log.e("Str_Get_message :", "" + Str_Get_message);

                    EDT_et_edit_profile_username.setText(Str_Get_user_name, TextView.BufferType.EDITABLE);
                    EDT_et_edit_profile_phone_number.setText(Str_Get_phone_number, TextView.BufferType.EDITABLE);
                    TV_edit_user_profile_name.setText(Html.fromHtml(Str_Get_user_name));


                    ImageLoader imageLoader = ImageLoader.getInstance();
                    imageLoader.getInstance().displayImage(Str_Get_user_image, Civ_edit_profile_image, new ImageLoadingListener() {
                        @Override
                        public void onLoadingStarted(String imageUri, View view) {
                            RL_edit_profile_image_progress.setVisibility(View.VISIBLE);
                        }

                        @Override
                        public void onLoadingFailed(String imageUri, View view, FailReason failReason) {
                            RL_edit_profile_image_progress.setVisibility(View.GONE);
                        }

                        @Override
                        public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {
                            RL_edit_profile_image_progress.setVisibility(View.GONE);
                        }

                        @Override
                        public void onLoadingCancelled(String imageUri, View view) {
                            RL_edit_profile_image_progress.setVisibility(View.GONE);
                        }
                    });

                } else {
                    Log.e("onPostExecute Sub_cat_id size is Zero ", "ooppss");
                    SnackbarManager.show(
                            Snackbar.with(EditProfileActivity.this)
                                    .position(Snackbar.SnackbarPosition.TOP)
                                    .margin(15, 15)
                                    .backgroundDrawable(R.drawable.snackbar_custom_layout)
                                    .text("No Data Found"));
                }
            }
        }

    }

    private class UpdateProfileJsontask extends AsyncTask<String, Void, String> {

        boolean iserror = false;

        @Override
        protected void onPreExecute() {
            // TODO Auto-generated method stub
            //  loginprogressbar.setVisibility(View.VISIBLE);
            Log.e("******* NOW UpdateProfileJsontask WEB SERVICE IS RUNNING *******", "YES");
            Log.e("******* NOW UpdateProfileJsontask WEB SERVICE IS RUNNING *******", "YES");
            Log.e("User_ID onPreExecute :", "" + User_ID);
            Log.e("Str_Set_user_name :","" + Str_Set_user_name);
            Log.e("Str_Set_password :", "" + Str_Set_password);
            Log.e("Str_Set_re_password :", "" + Str_Set_re_password);
            Log.e("Str_Set_phone_number :", "" + Str_Set_phone_number);
            Log.e("Str_profileImage_path :", "" + Str_profileImage_path);

            if (Str_profileImage_path.equalsIgnoreCase("")){
                Str_profileImage_path = "Ritesh";
            }
            RL_edit_profile_main_progress.setVisibility(View.VISIBLE);

        }

        @Override
        protected String doInBackground(String... params) {
            Log.e("******* NOW Login TASK IS RUNNING *******", "YES");


            try {
                HttpClient client = new DefaultHttpClient();
                HttpPost post = new HttpPost("http://sevilla.centrocomercial.com.es/wp-content/plugins/webserv/edit_user.php?user_id="+User_ID);
                Log.e("URL :", "" + "http://sevilla.centrocomercial.com.es/wp-content/plugins/webserv/edit_user.php?user_id="+User_ID+
                        "&user_name="+Str_Set_user_name+"&phone_number="+Str_Set_phone_number+"&user_pass="+Str_Set_password+"&user_image="+ProdileImagefilebody);

                MultipartEntity reqEntity = new MultipartEntity(HttpMultipartMode.BROWSER_COMPATIBLE);
                reqEntity.addPart("user_name", new StringBody(Str_Set_user_name));
                reqEntity.addPart("phone_number", new StringBody(Str_Set_phone_number));
                reqEntity.addPart("user_pass", new StringBody(Str_Set_password));

                if (ProfileImage == null) {
                    Log.e("ProfileImage File is :", "NULL");
                    Str_profileImage_path = "Ritesh";

                } else {
                    /******************* file for post the profile image to the server *******************/
                    ProdileImagefilebody = new FileBody(ProfileImage);
                    reqEntity.addPart("user_image", ProdileImagefilebody);
                    Log.e("ProdileImagefilebody image :", "" + ProdileImagefilebody.toString());
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
                result = parentObject.getString("status");
                Log.e("*********** RESULT *********** : ", result);

            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            } catch (ClientProtocolException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (JSONException e) {
                e.printStackTrace();
            }

            return result;
        }

        @Override
        protected void onPostExecute(String result1) {
            // TODO Auto-generated method stub
            super.onPostExecute(result1);
            RL_edit_profile_main_progress.setVisibility(View.GONE);

            if (!iserror) {
                if (result.equalsIgnoreCase("1")) {

                    SnackbarManager.show(
                            Snackbar.with(EditProfileActivity.this)
                                    .position(Snackbar.SnackbarPosition.TOP)
                                    .margin(15, 15)
                                    .backgroundDrawable(R.drawable.snackbar_custom_layout)
                                    .text("Profile Updated..!!"));

                    if (Utils.isConnected(getApplicationContext())) {
                        EditProfileGetUserDetailJsontask task = new EditProfileGetUserDetailJsontask();
                        task.execute();
                    } else {

                        SnackbarManager.show(
                                Snackbar.with(EditProfileActivity.this)
                                        .position(Snackbar.SnackbarPosition.TOP)
                                        .margin(15, 15)
                                        .backgroundDrawable(R.drawable.snackbar_custom_layout)
                                        .text("Please Your Internet Connectivity..!!"));

                    }


                } else {
                    Log.e("onPostExecute Sub_cat_id size is Zero ", "ooppss");
                    SnackbarManager.show(
                            Snackbar.with(EditProfileActivity.this)
                                    .position(Snackbar.SnackbarPosition.TOP)
                                    .margin(15, 15)
                                    .backgroundDrawable(R.drawable.snackbar_custom_layout)
                                    .text("No Data Found"));
                }
            }
        }

    }


    private void ProfileImagePicker() {
        QuickTipDialog = new Dialog(EditProfileActivity.this);
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

                new PickerBuilder(EditProfileActivity.this, PickerBuilder.SELECT_FROM_CAMERA)
                        .setOnImageReceivedListener(new PickerBuilder.onImageReceivedListener() {
                            @Override
                            public void onImageReceived(Uri imageUri) {
//                                Toast.makeText(EditProfileActivity.this, "Got image - " + imageUri, Toast.LENGTH_LONG).show();
                                Civ_edit_profile_image.setImageURI(imageUri);
                                Log.e("imageUri path SELECT_FROM_CAMERA :", "" + imageUri);
                                File f = new File(imageUri.getPath());
                                Str_profileImage_path = imageUri.getPath();
                                Log.e("Str_profileImage_path SELECT_FROM_CAMERA :", "" + Str_profileImage_path);
                                ProfileImage = new File(Str_profileImage_path);
                                Log.e("profile File Image:", "" + ProfileImage);
                                QuickTipDialog.dismiss();
                            }
                        })
                        .setImageName("testImage")
                        .setImageFolderName("testFolder")
                        .withTimeStamp(false)
                        .setCropScreenColor(Color.CYAN)
                        .start();

            }
        });

        RlGallery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                new PickerBuilder(EditProfileActivity.this, PickerBuilder.SELECT_FROM_GALLERY)
                        .setOnImageReceivedListener(new PickerBuilder.onImageReceivedListener() {
                            @Override
                            public void onImageReceived(Uri imageUri) {
                                Civ_edit_profile_image.setImageURI(imageUri);
//                                Toast.makeText(EditProfileActivity.this, "Got image - " + imageUri, Toast.LENGTH_LONG).show();
                                Log.e("imageUri path SELECT_FROM_GALLERY :", "" + imageUri);
                                File f = new File(imageUri.getPath());
                                Str_profileImage_path = imageUri.getPath();
                                Log.e("Str_profileImage_path SELECT_FROM_GALLERY :", "" + Str_profileImage_path);
                                ProfileImage = new File(Str_profileImage_path);
                                Log.e("profile File Image:", "" + ProfileImage);
                                QuickTipDialog.dismiss();
                            }
                        })
                        .setImageName("test")
                        .setImageFolderName("testFolder")
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


}
