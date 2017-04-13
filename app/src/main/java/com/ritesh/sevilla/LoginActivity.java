package com.ritesh.sevilla;

import android.content.Context;
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
import android.widget.TextView;
import android.widget.Toast;

import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;
import com.nispok.snackbar.Snackbar;
import com.nispok.snackbar.SnackbarManager;
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
import fr.castorflex.android.circularprogressbar.CircularProgressBar;
import fr.castorflex.android.circularprogressbar.CircularProgressDrawable;

/**
 * Created by ritesh on 7/2/17.
 */
@SuppressWarnings("deprecation")
public class LoginActivity extends AppCompatActivity {

    @BindView(R.id.cv_login_email)
    CardView CV_Btn_login;

    @BindView(R.id.tv_signup_below)
    TextView TV_signupScreen;

    @BindView(R.id.edt_login_user_email)
    EditText EDT_login_user_email;

    @BindView(R.id.edt_login_password)
    EditText EDT_login_password;

    @BindView(R.id.rl_login_progress)
    RelativeLayout pv_login_progressview;

    CircularProgressBar mCircularProgressBarlogin;

    boolean iserror = false;
    String Str_login_email = "", Str_login_password = "", result = "", error = "";
    String STR_User_Server_Id = "", STR_Email_ID = "", STR_User_Name = "", STR_Phone_Number = "", STR_User_Type = "";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);


         /*circular progress bar (Start)*/
        mCircularProgressBarlogin = (CircularProgressBar) findViewById(R.id.login_progressbar_circular);
//        signupProgress.setVisibility(View.GONE);
        ((CircularProgressDrawable) mCircularProgressBarlogin.getIndeterminateDrawable()).start();
        updateValues();
        /*circular progress bar (End)*/

        Appconstant.sh = getSharedPreferences(Appconstant.MyPREFERENCES, Context.MODE_PRIVATE);


        CV_Btn_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                /*Toast.makeText(getApplicationContext(),
                        "Login", Toast.LENGTH_SHORT).show();*/


                Str_login_email = EDT_login_user_email.getText().toString().trim();
                Str_login_password = EDT_login_password.getText().toString().trim();

                Log.e(" Login Fields data :", "\n"
                        + "Str_login_email :" + "" + Str_login_email + "\n"
                        + "Str_login_password :" + "" + Str_login_password + "\n");

                if (Str_login_email.equals("")) {
                    iserror = true;
                    v.performHapticFeedback(HapticFeedbackConstants.VIRTUAL_KEY);
//                    v.playSoundEffect(SoundEffectConstants.CLICK);
                    /**************** Start Animation **************  **/
                    YoYo.with(Techniques.Tada)
                            .duration(700)
                            .playOn(EDT_login_user_email);
                    /**************** End Animation ****************/

                    /*Toast.makeText(getApplicationContext(),
                            "Please enter your Email Id", Toast.LENGTH_SHORT).show();*/

                    SnackbarManager.show(
                            Snackbar.with(LoginActivity.this)
                                    .position(Snackbar.SnackbarPosition.TOP)
                                    .margin(15, 15)
                                    .backgroundDrawable(R.drawable.snackbar_custom_layout)
                                    .text("Please enter your Email Id"));

                } else if (!isValidEmail(Str_login_email)) {
                    iserror = true;
                    v.performHapticFeedback(HapticFeedbackConstants.VIRTUAL_KEY);
                    //	emailedit.requestFocus();
                    /**************** Start Animation ****************/
                    YoYo.with(Techniques.Shake)
                            .duration(700)
                            .playOn(EDT_login_user_email);
                    /**************** End Animation ****************/
                    /*Toast.makeText(getApplicationContext(),
                            "Please enter valid email address.", Toast.LENGTH_SHORT).show();*/

                    SnackbarManager.show(
                            Snackbar.with(LoginActivity.this)
                                    .position(Snackbar.SnackbarPosition.TOP)
                                    .margin(15, 15)
                                    .backgroundDrawable(R.drawable.snackbar_custom_layout)
                                    .text("Please enter valid email address."));


                } else if (Str_login_password.equals("")) {
                    iserror = true;
                    v.performHapticFeedback(HapticFeedbackConstants.VIRTUAL_KEY);
                    /**************** Start Animation ****************/
                    YoYo.with(Techniques.Tada)
                            .duration(700)
                            .playOn(EDT_login_password);
                    /**************** End Animation ****************/

                    /*Toast.makeText(getApplicationContext(),
                            "Please enter your Password", Toast.LENGTH_SHORT).show();*/

                    SnackbarManager.show(
                            Snackbar.with(LoginActivity.this)
                                    .position(Snackbar.SnackbarPosition.TOP)
                                    .margin(15, 15)
                                    .backgroundDrawable(R.drawable.snackbar_custom_layout)
                                    .text("Please enter your Password"));

                } else if (Str_login_password.length() < 5) {
                    iserror = true;
                    v.performHapticFeedback(HapticFeedbackConstants.VIRTUAL_KEY);
                    /**************** Start Animation ****************/
                    YoYo.with(Techniques.Shake)
                            .duration(700)
                            .playOn(EDT_login_password);
                    /**************** End Animation ****************/

                    /*Toast.makeText(getApplicationContext(), "Please enter more than 5 character in password.",
                            Toast.LENGTH_SHORT).show();*/

                    SnackbarManager.show(
                            Snackbar.with(LoginActivity.this)
                                    .position(Snackbar.SnackbarPosition.TOP)
                                    .margin(15, 15)
                                    .backgroundDrawable(R.drawable.snackbar_custom_layout)
                                    .text("Wrong password."));


                } else if (!iserror) {

                    v.performHapticFeedback(HapticFeedbackConstants.VIRTUAL_KEY);
                    /*Toast.makeText(getApplicationContext(),
                            "Good", Toast.LENGTH_SHORT).show();*/

                    /*SnackbarManager.show(
                            Snackbar.with(LoginActivity.this)
                                    .position(Snackbar.SnackbarPosition.TOP)
                                    .margin(15, 15)
                                    .backgroundDrawable(R.drawable.snackbar_custom_layout)
                                    .text("Good All Value Correct"));*/

//                    v.playSoundEffect(android.view.SoundEffectConstants.CLICK);


                    if (Utils.isConnected(getApplicationContext())) {
                        LoginJsontask task = new LoginJsontask();
                        task.execute();
                    } else {

                        SnackbarManager.show(
                                Snackbar.with(LoginActivity.this)
                                        .position(Snackbar.SnackbarPosition.TOP)
                                        .margin(15, 15)
                                        .backgroundDrawable(R.drawable.snackbar_custom_layout)
                                        .text("Please Your Internet Connectivity..!!"));

                    }

                }


            }
        });

        TV_signupScreen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intentSignup = new Intent(getApplicationContext(), SignupActivity.class);
                startActivity(intentSignup);
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
        mCircularProgressBarlogin.setIndeterminateDrawable(circularProgressDrawable = b.build());

        // /!\ Terrible hack, do not do this at home!
        circularProgressDrawable.setBounds(0,
                0,
                mCircularProgressBarlogin.getWidth(),
                mCircularProgressBarlogin.getHeight());
        mCircularProgressBarlogin.setVisibility(View.INVISIBLE);
        mCircularProgressBarlogin.setVisibility(View.VISIBLE);
    }
    /*progressbar data (End)*/


    public class LoginJsontask extends AsyncTask<String, Void, String> {

        boolean iserror = false;

        @Override
        protected void onPreExecute() {
            // TODO Auto-generated method stub
            //  loginprogressbar.setVisibility(View.VISIBLE);
            Log.e("******* NOW Login WEB SERVICE IS RUNNING *******", "YES");
            Log.e("******* NOW Login WEB SERVICE IS RUNNING *******", "YES");
            pv_login_progressview.setVisibility(View.VISIBLE);

            Log.e("Sign in URL :",
                    "http://sevilla.centrocomercial.com.es/wp-content/plugins/webserv/user_login.php?user_email="
                            + Str_login_email + "&user_pass=" + Str_login_password);

        }

        protected String doInBackground(String... params) {
            Log.e("******* NOW Login TASK IS RUNNING *******", "YES");
            Log.e("******* NOW Login TASK IS RUNNING *******", "YES");

            HttpClient client = new DefaultHttpClient();
            HttpPost post = new HttpPost("http://sevilla.centrocomercial.com.es/wp-content/plugins/webserv/user_login.php?user_email="
                    + Str_login_email + "&user_pass=" + Str_login_password);

            try {

                HttpResponse response = client.execute(post);
                String object = EntityUtils.toString(response.getEntity());
                Log.e("*******object******** :", "" + object);

                //JSONArray js = new JSONArray(object);
                JSONObject jobect = new JSONObject(object);
                result = jobect.getString("result");
                if (result.equalsIgnoreCase("success")) {
                    STR_User_Server_Id = jobect.getString("ID");
                    STR_Email_ID = jobect.getString("user_email");
                    STR_User_Name = jobect.getString("user_name");
                    STR_Phone_Number = jobect.getString("phone_number");
                    STR_User_Type = jobect.getString("seller_type");

                }

            } catch (Exception e) {
                Log.v("22", "22" + e.getMessage());
                e.printStackTrace();
                iserror = true;
            }
            return result;
        }

        @Override
        protected void onPostExecute(String result1) {
            // TODO Auto-generated method stub
            super.onPostExecute(result1);
            pv_login_progressview.setVisibility(View.GONE);

            if (!iserror) {
                if (result.equalsIgnoreCase("success")) {

                    Appconstant.editor.putString("id", STR_User_Server_Id);
                    Appconstant.editor.putString("email", STR_Email_ID);
                    Appconstant.editor.putString("username", STR_User_Name);
                    Appconstant.editor.putString("mobile", STR_Phone_Number);
                    Appconstant.editor.putString("usertype", STR_User_Type);
                    Appconstant.editor.putString("loginTest", "true");
                    Appconstant.editor.commit();

                    Log.e("Login ID :", "" + STR_User_Server_Id);
                    Log.e("Login STR_Email_ID :", "" + STR_Email_ID);
                    Log.e("Login STR_User_Name :", "" + STR_User_Name);
                    Log.e("Login STR_Phone_Number :", "" + STR_Phone_Number);
                    Log.e("Login STR_User_Type :", "" + STR_User_Type);

                    if (STR_User_Type.equalsIgnoreCase("Buyer")) {

                        Toast.makeText(LoginActivity.this, "Login Successfully ", Toast.LENGTH_SHORT).show();
                        Intent in = new Intent(LoginActivity.this, MainBuyerActivity.class);
                        in.putExtra("EXIT", "0");
                        in.putExtra("USERTYPE", STR_User_Type);
                        in.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                        LoginActivity.this.startActivity(in);
                        finish();
                    } else {

                        Toast.makeText(LoginActivity.this, "Login Successfully ", Toast.LENGTH_SHORT).show();
                        Intent in = new Intent(LoginActivity.this, MainSellerActivity.class);
                        in.putExtra("EXIT", "0");
                        in.putExtra("USERTYPE", STR_User_Type);
                        in.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                        LoginActivity.this.startActivity(in);
                        finish();
                    }

                } else if (result.equalsIgnoreCase("unsuccessful")) {

                    /**************** Start Animation ****************/
                    YoYo.with(Techniques.Swing)
                            .duration(700)
                            .playOn(EDT_login_user_email);

                    YoYo.with(Techniques.Swing)
                            .duration(700)
                            .playOn(EDT_login_password);
                    /**************** End Animation ****************/

                    Log.e("********* Login Detail SEND *********", "NOT Success USERNAME PASSWORD ERROR");
                    Log.e("********* Login Detail SEND *********", "NOT Success USERNAME PASSWORD ERROR");
                    Log.e("********* Login Detail SEND *********", "NOT Success USERNAME PASSWORD ERROR");
//                    ((CircularProgressDrawable) mCircularProgressBar.getIndeterminateDrawable()).stop();

//                    ((CircularProgressDrawable) mCircularProgressBar.getIndeterminateDrawable()).progressiveStop();

                    Toast.makeText(getApplicationContext(), "Username or Password is wrong",
                            Toast.LENGTH_SHORT).show();
//									// TODO Auto-generated method stub
                }
            } else {
                Log.e("********* Login Detail SEND *********", "NOT Success");
                Log.e("********* Login Detail SEND *********", "NOT Success");
                Log.e("********* Login Detail SEND *********", "NOT Success");

                Toast.makeText(getApplicationContext(), "Oops!! Please check server connection .",
                        Toast.LENGTH_SHORT).show();

            }
        }

    }


}
