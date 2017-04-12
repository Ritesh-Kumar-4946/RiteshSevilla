package com.ritesh.sevilla;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.HapticFeedbackConstants;
import android.view.MotionEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;
import com.facebook.FacebookSdk;
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
 * Created by ritesh on 12/4/17.
 */
@SuppressWarnings("deprecation")
public class ContactSupportActivity extends AppCompatActivity {

    @BindView(R.id.toolbar_contact_support)
    Toolbar TB_contact_support;


    @BindView(R.id.edt_contact_support_name)
    EditText EDT_contact_support_name;


    @BindView(R.id.edt_contact_support_email)
    EditText EDT_contact_support_email;


    @BindView(R.id.edt_contact_support_phone)
    EditText EDT_contact_support_phone;


    @BindView(R.id.edt_contact_support_query)
    EditText EDT_contact_support_query;


    @BindView(R.id.rl_btn_contact_support_now)
    RelativeLayout RL_btn_contact_support_now;


    @BindView(R.id.rl_btn_contact_support_now_click)
    RelativeLayout RL_btn_contact_support_now_click;


    @BindView(R.id.rl_contact_support_progress)
    RelativeLayout RL_contact_support_progress;

    CircularProgressBar CPB_contact_support;


    String
            Str_Set_Cont_Supp_User_name = "",
            Str_Set_Cont_Supp_User_email = "",
            Str_Set_Cont_Supp_User_phone = "",
            Str_Set_Cont_Supp_User_query = "",
            Str_Get_Cont_Supp_User_name = "",
            Str_Get_Cont_Supp_User_email = "",
            Str_Get_Cont_Supp_User_phone = "",
            Str_Get_Cont_Supp_User_query = "",
            Str_Get_Result = "";

    boolean iserror = false;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FacebookSdk.sdkInitialize(getApplicationContext());
        setContentView(R.layout.activity_contact_support);
        ButterKnife.bind(this);


        CPB_contact_support = (CircularProgressBar) findViewById(R.id.cpb_contact_support);
//        signupProgress.setVisibility(View.GONE);
        ((CircularProgressDrawable) CPB_contact_support.getIndeterminateDrawable()).start();
        updateValues();


        setSupportActionBar(TB_contact_support);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

//        toolbar_sub_category.setNavigationIcon(R.drawable.ic_back_arrow); // your drawable

        TB_contact_support.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed(); // Implemented by activity
            }
        });


        RL_btn_contact_support_now.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {

                Str_Set_Cont_Supp_User_name = EDT_contact_support_name.getText().toString();
                Str_Set_Cont_Supp_User_email = EDT_contact_support_email.getText().toString();
                Str_Set_Cont_Supp_User_phone = EDT_contact_support_phone.getText().toString();
                Str_Set_Cont_Supp_User_query = EDT_contact_support_query.getText().toString();

                Log.e(" Contact Support Fields data :", "\n"
                        + "Str_Set_Cont_Supp_User_name :" + "" + Str_Set_Cont_Supp_User_name + "\n"
                        + "Str_Set_Cont_Supp_User_email :" + "" + Str_Set_Cont_Supp_User_email + "\n"
                        + "Str_Set_Cont_Supp_User_phone :" + "" + Str_Set_Cont_Supp_User_phone + "\n"
                        + "Str_Set_Cont_Supp_User_query :" + "" + Str_Set_Cont_Supp_User_query);

                if (event.getAction() == MotionEvent.ACTION_DOWN) {

                    Log.e("Action ", "Down");
                    RL_btn_contact_support_now_click.setVisibility(View.VISIBLE);
                    RL_btn_contact_support_now.setVisibility(View.GONE);
                    v.performHapticFeedback(HapticFeedbackConstants.VIRTUAL_KEY);

//                    Toast.makeText(getApplicationContext(), "Add to cart Clicked", Toast.LENGTH_SHORT).show();
                    return true;
                }

                if (event.getAction() == MotionEvent.ACTION_MOVE) {

                    Log.e("Action ", "Move");
                    RL_btn_contact_support_now_click.setVisibility(View.VISIBLE);
                    RL_btn_contact_support_now.setVisibility(View.GONE);

                    return true;

                }
                if (event.getAction() == MotionEvent.ACTION_UP) {

                    Log.e("Action ", "Up");

                    RL_btn_contact_support_now_click.setVisibility(View.GONE);
                    RL_btn_contact_support_now.setVisibility(View.VISIBLE);


                    if (Str_Set_Cont_Supp_User_name.equals("")) {
                        iserror = true;
                        v.performHapticFeedback(HapticFeedbackConstants.VIRTUAL_KEY);
                        /**************** Start Animation ****************/
                /*https://github.com/daimajia/AndroidViewAnimations/blob/master/README.md*/

                        YoYo.with(Techniques.Tada)
                                .duration(700)
                                .playOn(EDT_contact_support_name);
                        /**************** End Animation ****************/

                    /*Toast.makeText(getApplicationContext(), "Please enter your Name",
                            Toast.LENGTH_SHORT).show();*/

                        SnackbarManager.show(
                                Snackbar.with(ContactSupportActivity.this)
                                        .position(Snackbar.SnackbarPosition.TOP)
                                        .margin(15, 15)
                                        .backgroundDrawable(R.drawable.snackbar_custom_layout)
                                        .textColor(R.color.text_color_black)
                                        .text("Please enter your Name"));


                    } else if (Str_Set_Cont_Supp_User_email.equals("")) {
                        iserror = true;
                        v.performHapticFeedback(HapticFeedbackConstants.VIRTUAL_KEY);
                        /**************** Start Animation ****************/
                        YoYo.with(Techniques.Tada)
                                .duration(700)
                                .playOn(EDT_contact_support_email);
                        /**************** End Animation ****************/

                    /*Toast.makeText(getApplicationContext(),
                            "Please enter your Email Id", Toast.LENGTH_SHORT).show();*/

                        SnackbarManager.show(
                                Snackbar.with(ContactSupportActivity.this)
                                        .position(Snackbar.SnackbarPosition.TOP)
                                        .margin(15, 15)
                                        .backgroundDrawable(R.drawable.snackbar_custom_layout)
                                        .textColor(R.color.text_color_black)
                                        .text("Please enter your Email Id"));

                    } else if (!isValidEmail(Str_Set_Cont_Supp_User_email)) {
                        iserror = true;
                        v.performHapticFeedback(HapticFeedbackConstants.VIRTUAL_KEY);
                        //	emailedit.requestFocus();
                        /**************** Start Animation ****************/
                        YoYo.with(Techniques.Shake)
                                .duration(700)
                                .playOn(EDT_contact_support_email);
                        /**************** End Animation ****************/
                    /*Toast.makeText(getApplicationContext(),
                            "Please enter valid email address.", Toast.LENGTH_SHORT).show();*/

                        SnackbarManager.show(
                                Snackbar.with(ContactSupportActivity.this)
                                        .position(Snackbar.SnackbarPosition.TOP)
                                        .margin(15, 15)
                                        .backgroundDrawable(R.drawable.snackbar_custom_layout)
                                        .textColor(R.color.text_color_black)
                                        .text("Please enter valid email address."));


                    } else if (Str_Set_Cont_Supp_User_phone.equals("")) {
                        iserror = true;
                        v.performHapticFeedback(HapticFeedbackConstants.VIRTUAL_KEY);
                        /**************** Start Animation ****************/
                        YoYo.with(Techniques.Tada)
                                .duration(700)
                                .playOn(EDT_contact_support_phone);
                        /**************** End Animation ****************/

                    /*Toast.makeText(getApplicationContext(),
                            "Please enter your Email Id", Toast.LENGTH_SHORT).show();*/

                        SnackbarManager.show(
                                Snackbar.with(ContactSupportActivity.this)
                                        .position(Snackbar.SnackbarPosition.TOP)
                                        .margin(15, 15)
                                        .backgroundDrawable(R.drawable.snackbar_custom_layout)
                                        .textColor(R.color.text_color_black)
                                        .text("Please enter your Phone Number"));

                    } else if (Str_Set_Cont_Supp_User_query.equals("")) {
                        iserror = true;
                        v.performHapticFeedback(HapticFeedbackConstants.VIRTUAL_KEY);
                        /**************** Start Animation ****************/
                        YoYo.with(Techniques.Tada)
                                .duration(700)
                                .playOn(EDT_contact_support_query);
                        /**************** End Animation ****************/

                    /*Toast.makeText(getApplicationContext(),
                            "Please enter your Email Id", Toast.LENGTH_SHORT).show();*/

                        SnackbarManager.show(
                                Snackbar.with(ContactSupportActivity.this)
                                        .position(Snackbar.SnackbarPosition.TOP)
                                        .margin(15, 15)
                                        .backgroundDrawable(R.drawable.snackbar_custom_layout)
                                        .textColor(R.color.text_color_black)
                                        .text("Please enter your Query"));

                    } else if (!iserror) {

                        v.performHapticFeedback(HapticFeedbackConstants.VIRTUAL_KEY);
                        if (Utils.isConnected(getApplicationContext())) {
                            SendQueryJsontask task = new SendQueryJsontask();
                            task.execute();
                        } else {

                            SnackbarManager.show(
                                    Snackbar.with(ContactSupportActivity.this)
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
        CPB_contact_support.setIndeterminateDrawable(circularProgressDrawable = b.build());

        // /!\ Terrible hack, do not do this at home!
        circularProgressDrawable.setBounds(0,
                0,
                CPB_contact_support.getWidth(),
                CPB_contact_support.getHeight());
        CPB_contact_support.setVisibility(View.INVISIBLE);
        CPB_contact_support.setVisibility(View.VISIBLE);
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


    private class SendQueryJsontask extends AsyncTask<String, Void, String> {

        @Override
        protected void onPreExecute() {
            // TODO Auto-generated method stub
            super.onPreExecute();
            RL_contact_support_progress.setVisibility(View.VISIBLE);

            try {
                Str_Set_Cont_Supp_User_name = URLEncoder.encode(Str_Set_Cont_Supp_User_name, "UTF-8");
                Str_Set_Cont_Supp_User_phone = URLEncoder.encode(Str_Set_Cont_Supp_User_phone, "UTF-8");
                Str_Set_Cont_Supp_User_query = URLEncoder.encode(Str_Set_Cont_Supp_User_query, "UTF-8");
            } catch (UnsupportedEncodingException e) {
                Log.e("Encode error", "" + e.getMessage());
                e.printStackTrace();
            }
            Log.e(" Contact Support data SendQueryJsontask :", "\n"
                    + "Str_Set_Cont_Supp_User_name :" + "" + Str_Set_Cont_Supp_User_name + "\n"
                    + "Str_Set_Cont_Supp_User_phone :" + "" + Str_Set_Cont_Supp_User_phone + "\n"
                    + "Str_Set_Cont_Supp_User_email :" + "" + Str_Set_Cont_Supp_User_email + "\n"
                    + "Str_Set_Cont_Supp_User_query :" + "" + Str_Set_Cont_Supp_User_query);

            Log.e("SendQueryJsontask URL :",
                    "http://sevilla.centrocomercial.com.es/wp-content/plugins/webserv/contactus.php?name=" + Str_Set_Cont_Supp_User_name
                            + "&email=" + Str_Set_Cont_Supp_User_email
                            + "&telephone=" + Str_Set_Cont_Supp_User_phone
                            + "&message=" + Str_Set_Cont_Supp_User_query);

        }

        @Override
        protected String doInBackground(String... params) {
            HttpClient client = new DefaultHttpClient();

            HttpPost post = new HttpPost("http://sevilla.centrocomercial.com.es/wp-content/plugins/webserv/contactus.php?name=" + Str_Set_Cont_Supp_User_name
                    + "&email=" + Str_Set_Cont_Supp_User_email
                    + "&telephone=" + Str_Set_Cont_Supp_User_phone
                    + "&message=" + Str_Set_Cont_Supp_User_query);

            try {

                HttpResponse response = client.execute(post);
                String object = EntityUtils.toString(response.getEntity());
                Log.e("************ object holds our SendQuery data ************ :", "" + object);
                //JSONArray js = new JSONArray(object);


                JSONObject jobect = new JSONObject(object);
                Str_Get_Result = jobect.getString("result");
                if (Str_Get_Result.equalsIgnoreCase("success")) {
                    Str_Get_Cont_Supp_User_name = jobect.getString("name");
                    Str_Get_Cont_Supp_User_email = jobect.getString("email");
                    Str_Get_Cont_Supp_User_phone = jobect.getString("telephone");
                    Str_Get_Cont_Supp_User_query = jobect.getString("message");
                }
            } catch (Exception e) {
                Log.e(" ******** Exception **********", "" + e.getMessage());
                e.printStackTrace();
                iserror = true;
            }
            return Str_Get_Result;
        }

        @Override
        protected void onPostExecute(String result1) {
            // TODO Auto-generated method stub
            super.onPostExecute(result1);

            RL_contact_support_progress.setVisibility(View.GONE);

            //if (!iserror == false)
            if (!iserror) {
                if (Str_Get_Result.equalsIgnoreCase("success")) {

                    Log.e("Str_Get_Cont_Supp_User_name :", "" + Str_Get_Cont_Supp_User_name);
                    Log.e("Str_Get_Cont_Supp_User_email :", "" + Str_Get_Cont_Supp_User_email);
                    Log.e("Str_Get_Cont_Supp_User_phone :", "" + Str_Get_Cont_Supp_User_phone);
                    Log.e("Str_Get_Cont_Supp_User_query :", "" + Str_Get_Cont_Supp_User_query);


                    SnackbarManager.show(
                            Snackbar.with(ContactSupportActivity.this)
                                    .position(Snackbar.SnackbarPosition.TOP)
                                    .margin(15, 15)
                                    .backgroundDrawable(R.drawable.snackbar_custom_layout)
                                    .textColor(R.color.text_color_black)
                                    .text("Your Query Send Successfully"));
                    /*
                    Intent GoLoginScreen = new Intent(ContactSupportActivity.this, LoginActivity.class);
                    startActivity(GoLoginScreen);*/

                } else {
                    SnackbarManager.show(
                            Snackbar.with(ContactSupportActivity.this)
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

}
