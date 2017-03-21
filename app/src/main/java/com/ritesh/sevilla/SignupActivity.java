package com.ritesh.sevilla;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.util.Log;
import android.view.HapticFeedbackConstants;
import android.view.View;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;
import com.jaredrummler.materialspinner.MaterialSpinner;
import com.nispok.snackbar.Snackbar;
import com.nispok.snackbar.SnackbarManager;
import com.ritesh.sevilla.Constant.Utils;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

import butterknife.BindView;
import butterknife.ButterKnife;
import fr.castorflex.android.circularprogressbar.CircularProgressBar;
import fr.castorflex.android.circularprogressbar.CircularProgressDrawable;

/**
 * Created by ritesh on 8/2/17.
 */
@SuppressWarnings("deprecation")
public class SignupActivity extends AppCompatActivity {

    @BindView(R.id.edt_et_signup_username)
    EditText ET_signup_username;

    @BindView(R.id.edt_et_signup_email)
    EditText ET_signup_email;

    @BindView(R.id.edt_et_signup_phone_number)
    EditText ET_signup_phone_number;

    @BindView(R.id.edt_et_sign_password)
    EditText ET_signup_password;

    @BindView(R.id.edt_et_sign_re_password)
    EditText ET_signup_re_password;

    @BindView(R.id.cv_et_sign_btn_signup)
    CardView Btn_CV_signup;

    @BindView(R.id.sp_spinner)
    MaterialSpinner spinner;

    @BindView(R.id.rl_signup_progress)
    RelativeLayout signupProgress;

    CircularProgressBar mCircularProgressBar;

    boolean iserror = false;

    String str_username = "", str_emailid = "", str_password = "", str_confirm_password = "",
            str_phone_number = "", result = "", error = "";

    String STR_User_ID = "", STR_Phone_Number = "", STR_User_Name = "", STR_User_Email = "",
            STR_Password = "", STR_error_Message = "";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);
        ButterKnife.bind(this);

        /*circular progress bar (Start)*/
        mCircularProgressBar = (CircularProgressBar) findViewById(R.id.signup_progressbar_circular);
//        signupProgress.setVisibility(View.GONE);
        ((CircularProgressDrawable) mCircularProgressBar.getIndeterminateDrawable()).start();
        updateValues();
        /*circular progress bar (End)*/


        Btn_CV_signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                str_username = ET_signup_username.getText().toString().trim();
                str_emailid = ET_signup_email.getText().toString().trim();
                str_password = ET_signup_password.getText().toString().trim();
                str_confirm_password = ET_signup_re_password.getText().toString().trim();
                str_phone_number = ET_signup_phone_number.getText().toString().trim();

                Log.e(" Sign Up Fields data :", "\n"
                        + "str_username :" + "" + str_username + "\n"
                        + "str_emailid :" + "" + str_emailid + "\n"
                        + "str_phone_number :" + "" + str_phone_number + "\n"
                        + "str_password :" + "" + str_password + "\n"
                        + "str_confirm_password :" + "" + str_confirm_password);


                if (str_username.equals("")) {
                    iserror = true;
                    v.performHapticFeedback(HapticFeedbackConstants.VIRTUAL_KEY);
                    /**************** Start Animation ****************/
                /*https://github.com/daimajia/AndroidViewAnimations/blob/master/README.md*/

                    YoYo.with(Techniques.Tada)
                            .duration(700)
                            .playOn(ET_signup_username);
                    /**************** End Animation ****************/

                    /*Toast.makeText(getApplicationContext(), "Please enter your Name",
                            Toast.LENGTH_SHORT).show();*/

                    SnackbarManager.show(
                            Snackbar.with(SignupActivity.this)
                                    .position(Snackbar.SnackbarPosition.TOP)
                                    .margin(15, 15)
                                    .backgroundDrawable(R.drawable.snackbar_custom_layout)
                                    .textColor(R.color.text_color_black)
                                    .text("Please enter your Name"));


                } else if (str_emailid.equals("")) {
                    iserror = true;
                    v.performHapticFeedback(HapticFeedbackConstants.VIRTUAL_KEY);
                    /**************** Start Animation ****************/
                    YoYo.with(Techniques.Tada)
                            .duration(700)
                            .playOn(ET_signup_email);
                    /**************** End Animation ****************/

                    /*Toast.makeText(getApplicationContext(),
                            "Please enter your Email Id", Toast.LENGTH_SHORT).show();*/

                    SnackbarManager.show(
                            Snackbar.with(SignupActivity.this)
                                    .position(Snackbar.SnackbarPosition.TOP)
                                    .margin(15, 15)
                                    .backgroundDrawable(R.drawable.snackbar_custom_layout)
                                    .textColor(R.color.text_color_black)
                                    .text("Please enter your Email Id"));

                } else if (!isValidEmail(str_emailid)) {
                    iserror = true;
                    v.performHapticFeedback(HapticFeedbackConstants.VIRTUAL_KEY);
                    //	emailedit.requestFocus();
                    /**************** Start Animation ****************/
                    YoYo.with(Techniques.Shake)
                            .duration(700)
                            .playOn(ET_signup_email);
                    /**************** End Animation ****************/
                    /*Toast.makeText(getApplicationContext(),
                            "Please enter valid email address.", Toast.LENGTH_SHORT).show();*/

                    SnackbarManager.show(
                            Snackbar.with(SignupActivity.this)
                                    .position(Snackbar.SnackbarPosition.TOP)
                                    .margin(15, 15)
                                    .backgroundDrawable(R.drawable.snackbar_custom_layout)
                                    .textColor(R.color.text_color_black)
                                    .text("Please enter valid email address."));


                } else if (str_phone_number.equals("")) {
                    iserror = true;
                    v.performHapticFeedback(HapticFeedbackConstants.VIRTUAL_KEY);
                    /**************** Start Animation ****************/
                    YoYo.with(Techniques.Tada)
                            .duration(700)
                            .playOn(ET_signup_phone_number);
                    /**************** End Animation ****************/

                    /*Toast.makeText(getApplicationContext(),
                            "Please enter your Email Id", Toast.LENGTH_SHORT).show();*/

                    SnackbarManager.show(
                            Snackbar.with(SignupActivity.this)
                                    .position(Snackbar.SnackbarPosition.TOP)
                                    .margin(15, 15)
                                    .backgroundDrawable(R.drawable.snackbar_custom_layout)
                                    .textColor(R.color.text_color_black)
                                    .text("Please enter your Phone Number"));

                } else if (str_password.equals("")) {
                    iserror = true;
                    v.performHapticFeedback(HapticFeedbackConstants.VIRTUAL_KEY);
                    /**************** Start Animation ****************/
                    YoYo.with(Techniques.Tada)
                            .duration(700)
                            .playOn(ET_signup_password);
                    /**************** End Animation ****************/

                    /*Toast.makeText(getApplicationContext(),
                            "Please enter your Password", Toast.LENGTH_SHORT).show();*/

                    SnackbarManager.show(
                            Snackbar.with(SignupActivity.this)
                                    .position(Snackbar.SnackbarPosition.TOP)
                                    .margin(15, 15)
                                    .backgroundDrawable(R.drawable.snackbar_custom_layout)
                                    .textColor(R.color.text_color_black)
                                    .text("Please enter your Password"));

                } else if (str_password.length() < 5) {
                    iserror = true;
                    v.performHapticFeedback(HapticFeedbackConstants.VIRTUAL_KEY);
                    /**************** Start Animation ****************/
                    YoYo.with(Techniques.Shake)
                            .duration(700)
                            .playOn(ET_signup_password);
                    /**************** End Animation ****************/

                    /*Toast.makeText(getApplicationContext(), "Please enter more than 5 character in password.",
                            Toast.LENGTH_SHORT).show();*/

                    SnackbarManager.show(
                            Snackbar.with(SignupActivity.this)
                                    .position(Snackbar.SnackbarPosition.TOP)
                                    .margin(15, 15)
                                    .backgroundDrawable(R.drawable.snackbar_custom_layout)
                                    .textColor(R.color.text_color_black)
                                    .text("Please enter more than 5 character \n in password."));


                } else if (!str_confirm_password.equals(str_password)) {
                    iserror = true;
                    v.performHapticFeedback(HapticFeedbackConstants.VIRTUAL_KEY);
                    /**************** Start Animation ****************/
                    YoYo.with(Techniques.Swing)
                            .duration(700)
                            .playOn(ET_signup_password);

                    YoYo.with(Techniques.Swing)
                            .duration(700)
                            .playOn(ET_signup_re_password);
                    /**************** End Animation ****************/

                    /*Toast.makeText(getApplicationContext(),
                            "oopsss....\n Password not Match Please try again", Toast.LENGTH_SHORT).show();*/


                    SnackbarManager.show(
                            Snackbar.with(SignupActivity.this)
                                    .position(Snackbar.SnackbarPosition.TOP)
                                    .margin(15, 15)
                                    .backgroundDrawable(R.drawable.snackbar_custom_layout)
                                    .textColor(R.color.text_color_black)
                                    .text("oopsss....\\n Password not Match Please try again"));

                } else if (!iserror) {
                    v.performHapticFeedback(HapticFeedbackConstants.VIRTUAL_KEY);
                    /*Toast.makeText(getApplicationContext(),
                            "Good", Toast.LENGTH_SHORT).show();*/

                    SnackbarManager.show(
                            Snackbar.with(SignupActivity.this)
                                    .position(Snackbar.SnackbarPosition.TOP)
                                    .margin(15, 15)
                                    .backgroundDrawable(R.drawable.snackbar_custom_layout)
                                    .textColor(R.color.text_color_black)
                                    .text("Good All Value Correct"));

                    if (Utils.isConnected(getApplicationContext())) {
                        SignUpJsontask task = new SignUpJsontask();
                        task.execute();
                    } else {

                        SnackbarManager.show(
                                Snackbar.with(SignupActivity.this)
                                        .position(Snackbar.SnackbarPosition.TOP)
                                        .margin(15, 15)
                                        .backgroundDrawable(R.drawable.snackbar_custom_layout)
                                        .textColor(R.color.text_color_black)
                                        .text("Please Your Internet Connectivity..!!"));

                    }

                }


            }
        });

    }


    /*email address field validator (Start)*/
    public final static boolean isValidEmail(CharSequence target) {
        if (target == null) {
            return false;
        } else {
            return android.util.Patterns.EMAIL_ADDRESS.matcher(target)
                    .matches();
        }
    }
    /*email address field validator (End)*/


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
        mCircularProgressBar.setIndeterminateDrawable(circularProgressDrawable = b.build());

        // /!\ Terrible hack, do not do this at home!
        circularProgressDrawable.setBounds(0,
                0,
                mCircularProgressBar.getWidth(),
                mCircularProgressBar.getHeight());
        mCircularProgressBar.setVisibility(View.INVISIBLE);
        mCircularProgressBar.setVisibility(View.VISIBLE);
    }
    /*progressbar data (End)*/


    private class SignUpJsontask extends AsyncTask<String, Void, String> {

        @Override
        protected void onPreExecute() {
            // TODO Auto-generated method stub
            super.onPreExecute();
            signupProgress.setVisibility(View.VISIBLE);

            try {
                str_username = URLEncoder.encode(str_username, "UTF-8");
            } catch (UnsupportedEncodingException e) {
                Log.e("Encode error", "" + e.getMessage());
                e.printStackTrace();
            }
            Log.e(" Sign Up Fields data in Signup JSON TASK :", "\n"
                    + "str_username :" + "" + str_username + "\n"
                    + "str_emailid :" + "" + str_emailid + "\n"
                    + "str_phone_number :" + "" + str_phone_number + "\n"
                    + "str_password :" + "" + str_password + "\n"
                    + "str_confirm_password :" + "" + str_confirm_password);

            Log.e("SignUp URL :", "http://sevilla.centrocomercial.com.es/wp-content/plugins/webserv/user_signup.php?user_name="
                    +str_username+"&user_email="+str_emailid+"&user_pass="+str_password+"&phone_number="+str_phone_number);

        }

        @Override
        protected String doInBackground(String... params) {
            HttpClient client = new DefaultHttpClient();

            HttpPost post = new HttpPost("http://sevilla.centrocomercial.com.es/wp-content/plugins/webserv/user_signup.php?user_name="+str_username+"&user_email="+str_emailid+"&user_pass="+str_password+"&phone_number="+str_phone_number);

            try {

                HttpResponse response = client.execute(post);
                String object = EntityUtils.toString(response.getEntity());
                Log.e("************ object holds our data ************ :", "" + object);
                //JSONArray js = new JSONArray(object);


                JSONObject jobect = new JSONObject(object);
                result = jobect.getString("result");
                if (result.equalsIgnoreCase("success")) {
                    STR_User_ID = jobect.getString("ID");
                    STR_User_Name = jobect.getString("user_name");
                    STR_User_Email = jobect.getString("user_email");
                    STR_Phone_Number = jobect.getString("phone_number");
                } else {
                    if (result.equalsIgnoreCase("fail")) {
                        STR_error_Message = jobect.getString("message");
                    }
                }
            } catch (Exception e) {
                Log.e(" ******** Exception **********", "************ Exception ************" + e.getMessage());
                e.printStackTrace();
                iserror = true;
            }
            return result;
        }

        @Override
        protected void onPostExecute(String result1) {
            // TODO Auto-generated method stub
            super.onPostExecute(result1);

            //if (!iserror == false)
            if (!iserror) {
                if (result.equalsIgnoreCase("success")) {
                    signupProgress.setVisibility(View.GONE);

                    Log.e("STR_userID :", "" + STR_User_ID);
                    Log.e("STR_user_name :", "" + STR_User_Name);
                    Log.e("STR_user_email :", "" + STR_User_Email);
                    Log.e("STR_Phone_Number :", "" + STR_Phone_Number);
                    Log.e("STR_Password :", "" + STR_Password);

                    Intent GoLoginScreen = new Intent(SignupActivity.this, LoginActivity.class);
                    startActivity(GoLoginScreen);

                    Toast.makeText(getApplicationContext(), "Register Success", Toast.LENGTH_SHORT).show();

                } else if (result.equalsIgnoreCase("fail")) {
                    signupProgress.setVisibility(View.GONE);

                    SnackbarManager.show(
                            Snackbar.with(SignupActivity.this)
                                    .position(Snackbar.SnackbarPosition.TOP)
                                    .margin(15, 15)
                                    .backgroundDrawable(R.drawable.snackbar_custom_layout)
                                    .textColor(R.color.text_color_black)
                                    .text("Email already exists"));
                } else {
                    signupProgress.setVisibility(View.GONE);

                    SnackbarManager.show(
                            Snackbar.with(SignupActivity.this)
                                    .position(Snackbar.SnackbarPosition.TOP)
                                    .margin(15, 15)
                                    .backgroundDrawable(R.drawable.snackbar_custom_layout)
                                    .textColor(R.color.text_color_black)
                                    .text("oopsss....\\n Server Error"));
                }
            } else {
                signupProgress.setVisibility(View.GONE);
                SnackbarManager.show(
                        Snackbar.with(SignupActivity.this)
                                .position(Snackbar.SnackbarPosition.TOP)
                                .margin(15, 15)
                                .backgroundDrawable(R.drawable.snackbar_custom_layout)
                                .textColor(R.color.text_color_black)
                                .text("oopsss....\\n Oops!! Please check server connection"));
//
            }
        }

    }


}
