package com.ritesh.sevilla;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.text.Html;
import android.util.Log;
import android.view.HapticFeedbackConstants;
import android.view.MotionEvent;
import android.view.View;
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

    @BindView(R.id.edt_et_edit_profile_password)
    EditText EDT_et_edit_profile_password;

    @BindView(R.id.edt_et_edit_profile_re_password)
    EditText EDT_et_edit_profile_re_password;

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
            Str_Get_user_name = "",
            Str_Get_phone_number = "",
            Str_Get_Status = "";

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
                    Toast.makeText(EditProfileActivity.this, "Comming Soon", Toast.LENGTH_SHORT).show();
                    CV_et_sign_btn_edit_profile_click.setVisibility(View.GONE);
                    CV_et_sign_btn_edit_profile.setVisibility(View.VISIBLE);
                    return true;
                }


                return false;
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
                    imageLoader.getInstance().displayImage(Str_Get_message, Civ_edit_profile_image, new ImageLoadingListener() {
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


}
