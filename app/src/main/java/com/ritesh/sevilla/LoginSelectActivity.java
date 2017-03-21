package com.ritesh.sevilla;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.HttpMethod;
import com.facebook.Profile;
import com.facebook.appevents.AppEventsLogger;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.nispok.snackbar.Snackbar;
import com.nispok.snackbar.SnackbarManager;
import com.ritesh.sevilla.Constant.Appconstant;
import com.ritesh.sevilla.Constant.HttpUrlPath;
import com.ritesh.sevilla.Constant.Utils;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import fr.castorflex.android.circularprogressbar.CircularProgressBar;
import fr.castorflex.android.circularprogressbar.CircularProgressDrawable;

@SuppressWarnings("deprecation")
public class LoginSelectActivity extends AppCompatActivity {

    @BindView(R.id.cv_login_email_page)
    CardView login_page;

    @BindView(R.id.cv_login_facebook)
    CardView Btn_CV_facebook_login;

    String Str_Fb_UserEmail = "", Str_Fb_ID = "", Str_Fb_Name = "", Str_Fb_Gender = "",
            Str_Fb_Profile_url = "", Str_Fb_Tocken = "", result = "",
            STR_User_Socail_Server_Id = "", STR_User_Socail_Email_ID = "", STR_User_Socail_User_Name = "",
            STR_User_Socail_User_image = "", STR_User_Socail_Status_Message = "";

    private LoginButton loginButton;
    private CallbackManager callbackManager;

    @BindView(R.id.rl_login_Signup_select_progress)
    RelativeLayout pv_login_Signup_select_progressview;

    CircularProgressBar mCircularSignup_selectProgressBarlogin;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FacebookSdk.sdkInitialize(getApplicationContext());
        AppEventsLogger.activateApp(this);
        FacebookSdk.sdkInitialize(getApplicationContext());
        callbackManager = CallbackManager.Factory.create();

        LoginManager.getInstance().registerCallback(callbackManager, new FacebookCallback<LoginResult>() {

            @Override
            public void onSuccess(LoginResult loginResult) {
//                setFacebookData(loginResult);
            }

            @Override
            public void onCancel() {

            }

            @Override
            public void onError(FacebookException error) {

            }
        });

        setContentView(R.layout.activity_login_select);
        ButterKnife.bind(this);
        loginButton = (LoginButton) findViewById(R.id.login_button);


        keyhash();


         /*circular progress bar (Start)*/
        mCircularSignup_selectProgressBarlogin = (CircularProgressBar) findViewById(R.id.login_Signup_select_progressbar_circular);
//        signupProgress.setVisibility(View.GONE);
        ((CircularProgressDrawable) mCircularSignup_selectProgressBarlogin.getIndeterminateDrawable()).start();
        updateValues();
        /*circular progress bar (End)*/

        Appconstant.sh = getSharedPreferences(Appconstant.MyPREFERENCES, Context.MODE_PRIVATE);


        login_page.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intentLogin = new Intent(getApplicationContext(), LoginActivity.class);
                startActivity(intentLogin);
            }
        });

        Btn_CV_facebook_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loginButton.performClick();
            }
        });


        loginButton.setReadPermissions(Arrays.asList("public_profile", "email", "user_birthday",
                "user_friends", "user_photos", "user_likes", "user_relationship_details",
                "user_relationships"));
        loginButton.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                Log.e("facebook longin success :",
                        "User ID: " + "" + loginResult.getAccessToken().getUserId()
                                + "\n" + "Auth Token: " + "" + loginResult.getAccessToken().getToken());

                Str_Fb_Tocken = loginResult.getAccessToken().getToken();
                Str_Fb_ID = loginResult.getAccessToken().getUserId();
                Log.e(" Str_Fb_Tocken :", "" + Str_Fb_Tocken);
                Log.e(" Str_Fb_ID :", "" + Str_Fb_ID);


                // App code
                GraphRequest request = GraphRequest.newMeRequest(loginResult.getAccessToken(),
                        new GraphRequest.GraphJSONObjectCallback() {
                            @Override
                            public void onCompleted(JSONObject object, GraphResponse response) {
                                Log.e("LoginActivity", response.toString());
                                try {
                                    Log.e("Try First Block :", "OKK");

                                    Profile profile = Profile.getCurrentProfile();
                                    Str_Fb_ID = object.getString("id");
                                    Log.e("Str_Fb_ID :", "" + Str_Fb_ID);

                                    try {
                                        URL profile_pic = new URL("http://graph.facebook.com/" + Str_Fb_ID + "/picture?type=large");
                                        Str_Fb_Profile_url = String.valueOf(profile_pic);
                                        Log.e(" Str_Fb_Profile_url :", "" + Str_Fb_Profile_url);
                                        Str_Fb_Name = object.getString("name");
                                        Log.e("Str_Fb_Name :", "" + Str_Fb_Name);
                                        Str_Fb_Gender = object.getString("gender");
                                        Log.e("Str_Fb_Gender :", "" + Str_Fb_Gender);
                                        Str_Fb_UserEmail = object.getString("email");
                                        Log.e("Str_Fb_UserEmail :", "" + Str_Fb_UserEmail);
                                        Log.e("profile_pic :", "" + profile_pic);

                                        if (profile != null) {
                                            String facebook_id = profile.getId();
                                            String f_name = profile.getFirstName();
                                            String l_name = profile.getLastName();
                                            Log.e(" facebook_id :", "" + facebook_id);
                                            Log.e(" f_name :", "" + f_name);
                                            Log.e(" l_name :", "" + l_name);
                                        }

                                        /*LoginToDashBoardActivity task = new LoginToDashBoardActivity();
                                        task.execute();*/
                                        if (!Str_Fb_ID.equals("")) {
//                                            disconnectFromFacebook();
                                            Log.e("Str_Fb_ID :", "Not Null " + "Go to DashBoard Screen");

                                            if (Utils.isConnected(getApplicationContext())) {
                                                SocialLogintask task = new SocialLogintask();
                                                task.execute();
                                            } else {

                                                SnackbarManager.show(
                                                        Snackbar.with(LoginSelectActivity.this)
                                                                .position(Snackbar.SnackbarPosition.TOP)
                                                                .margin(15, 15)
                                                                .backgroundDrawable(R.drawable.snackbar_custom_layout)
                                                                .text("Please Your Internet Connectivity..!!"));

                                            }

                                        } else {
                                            Log.e("Str_Fb_ID :", "Null");
                                        }

                                    } catch (MalformedURLException e) {
                                        e.printStackTrace();
                                    }
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }

                            }
                        });
                Bundle parameters = new Bundle();
                parameters.putString("fields", "id,name,email,gender,birthday");
                request.setParameters(parameters);
                request.executeAsync();
            }

            @Override
            public void onCancel() {

                Log.e("Facebook login Cancel :", "Yes");
                Log.e("Facebook login Cancel :", "Yes");
                Log.e("Facebook login Cancel :", "Yes");
            }

            @Override
            public void onError(FacebookException error) {
                Log.e("Facebook login Error :", "Yes");
                Log.e("Facebook login Error :", "Yes");
                Log.e("Facebook login Error :", "Yes");

                Log.e("LoginActivity Error :", "" + error.getCause().toString());
            }

        });


    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        callbackManager.onActivityResult(requestCode, resultCode, data);
    }


    /*call this method for programatically logout from facebook (Start)*/
    public void disconnectFromFacebook() {

        if (AccessToken.getCurrentAccessToken() == null) {

            Log.e(" You are already loged out from facebook :", "ok");
            return; // already logged out
        }

        new GraphRequest(AccessToken.getCurrentAccessToken(), "/me/permissions/", null, HttpMethod.DELETE, new GraphRequest
                .Callback() {
            @Override
            public void onCompleted(GraphResponse graphResponse) {
                Log.e("Successfully logout from facebook :", "Congratulations");
                AccessToken.setCurrentAccessToken(null);
                LoginManager.getInstance().logOut();
                Log.e("Successfully logout from facebook :", "Congrates");

            }
        }).executeAsync();
    }
    /*call this method for programatically logout from facebook (End)*/


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
        mCircularSignup_selectProgressBarlogin.setIndeterminateDrawable(circularProgressDrawable = b.build());

        // /!\ Terrible hack, do not do this at home!
        circularProgressDrawable.setBounds(0, 0,
                mCircularSignup_selectProgressBarlogin.getWidth(),
                mCircularSignup_selectProgressBarlogin.getHeight());
        mCircularSignup_selectProgressBarlogin.setVisibility(View.INVISIBLE);
        mCircularSignup_selectProgressBarlogin.setVisibility(View.VISIBLE);
    }
    /*progressbar data (End)*/


    public class SocialLogintask extends AsyncTask<String, Void, String> {

        boolean iserror = false;

        @Override
        protected void onPreExecute() {
            // TODO Auto-generated method stub
            //  loginprogressbar.setVisibility(View.VISIBLE);
            Log.e("******* NOW Login WEB SERVICE IS RUNNING *******", "YES");
            Log.e("******* NOW Login WEB SERVICE IS RUNNING *******", "YES");

            /*temp = temp.replaceAll(" ", "%20");*/
            try {
                Str_Fb_Name = URLEncoder.encode(Str_Fb_Name, "UTF-8");
                Log.e("Fb ID onPreExecute:", "" + Str_Fb_ID);
                Log.e("Fb User Email onPreExecute:", "" + Str_Fb_UserEmail);
                Log.e("Fb User Name onPreExecute:", "" + Str_Fb_Name);
                Log.e("Fb User Image Path onPreExecute:", "" + Str_Fb_Profile_url);
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
            pv_login_Signup_select_progressview.setVisibility(View.VISIBLE);

        }

        protected String doInBackground(String... params) {
            Log.e("******* NOW Login TASK IS RUNNING *******", "YES");
            Log.e("******* NOW Login TASK IS RUNNING *******", "YES");

            HttpClient client = new DefaultHttpClient();
//            HttpPost post = new HttpPost(HttpUrlPath.urlPathSocial + "social_login.php?");
            HttpPost post = new HttpPost("http://sevilla.centrocomercial.com.es/wp-content/plugins/webserv/social_login.php?social_id=" + Str_Fb_ID
                    + "&user_email=" + Str_Fb_UserEmail + "&user_name=" + Str_Fb_Name + "&user_image=" + Str_Fb_Profile_url);

            Log.e("FB URL :", "\n" +
                    "http://sevilla.centrocomercial.com.es/wp-content/plugins/webserv/social_login.php?social_id=" + Str_Fb_ID
                    + "&user_email=" + Str_Fb_UserEmail + "&user_name=" + Str_Fb_Name + "&user_image=" + Str_Fb_Profile_url);
            try {
                /*List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(7);
                nameValuePairs.add(new BasicNameValuePair("user_email", Str_Fb_UserEmail));
                nameValuePairs.add(new BasicNameValuePair("social_id", Str_Fb_ID));
                nameValuePairs.add(new BasicNameValuePair("user_image", Str_Fb_Profile_url));
                nameValuePairs.add(new BasicNameValuePair("user_name", Str_Fb_Name));

                post.setEntity(new UrlEncodedFormEntity(nameValuePairs));*/
                HttpResponse response = client.execute(post);
                String object = EntityUtils.toString(response.getEntity());
                Log.e("*******Socail object******** :", "" + object);

                //JSONArray js = new JSONArray(object);
                JSONObject jobect = new JSONObject(object);
                result = jobect.getString("result");
                if (result.equalsIgnoreCase("successfull")) {
                    STR_User_Socail_Server_Id = jobect.getString("ID");
                    STR_User_Socail_Email_ID = jobect.getString("user_email");
                    STR_User_Socail_User_Name = jobect.getString("user_name");
//                    STR_User_Socail_User_Name = URLDecoder.decode(STR_User_Socail_User_Name, "UTF-8");
                    STR_User_Socail_User_image = jobect.getString("user_image");
                    STR_User_Socail_Status_Message = jobect.getString("message");
                }
                /*else if (result.equals("no data updated")) {
                    STR_User_Socail_Server_Id = jobect.getString("ID");
                    STR_User_Socail_Email_ID = jobect.getString("email");
                    STR_User_Socail_User_Name = jobect.getString("user_name");
                    STR_User_Socail_User_Name = URLDecoder.decode(STR_User_Socail_User_Name, "UTF-8");
                    STR_User_Socail_User_image = jobect.getString("image");
                }*/

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
            pv_login_Signup_select_progressview.setVisibility(View.GONE);

            if (!iserror) {
                if (result.equalsIgnoreCase("successfull")) {

                    Appconstant.editor.putString("id", STR_User_Socail_Server_Id);
                    Appconstant.editor.putString("F_email", STR_User_Socail_Email_ID);
                    Appconstant.editor.putString("F_username", STR_User_Socail_User_Name);
                    Appconstant.editor.putString("F_userimage", STR_User_Socail_User_image);
                    Appconstant.editor.putString("loginTest", "true");
                    Appconstant.editor.commit();


                    Log.e(" Result onPostExecute:", "Successfull");

                    Log.e(" STR_User_Socail_Server_Id onPostExecute:", "" + STR_User_Socail_Server_Id);
                    Log.e(" STR_User_Socail_Email_ID onPostExecute:", "" + STR_User_Socail_Email_ID);
                    Log.e(" STR_User_Socail_User_Name onPostExecute:", "" + STR_User_Socail_User_Name);
                    Log.e(" STR_User_Socail_User_image onPostExecute:", "" + STR_User_Socail_User_image);
                    Log.e(" STR_User_Socail_Status_Message onPostExecute:", "" + STR_User_Socail_Status_Message);
                    Toast.makeText(LoginSelectActivity.this, "Login Successfully ", Toast.LENGTH_SHORT).show();

                    Intent intentFbLogin = new Intent(getApplicationContext(), MainActivity.class);
                    intentFbLogin.putExtra("EXIT", "0");
                    intentFbLogin.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intentFbLogin);
                    finish();
                } else if (result.equalsIgnoreCase("no data updated")) {


                    Appconstant.editor.putString("id", STR_User_Socail_Server_Id);
                    Appconstant.editor.putString("F_email", STR_User_Socail_Email_ID);
                    Appconstant.editor.putString("F_username", STR_User_Socail_User_Name);
                    Appconstant.editor.putString("F_userimage", STR_User_Socail_User_image);
                    Appconstant.editor.putString("loginTest", "true");
                    Appconstant.editor.commit();


                    Log.e(" Result :", "no data updated");

                    Log.e(" STR_User_Socail_Server_Id :", "" + STR_User_Socail_Server_Id);
                    Log.e(" STR_User_Socail_Email_ID :", "" + STR_User_Socail_Email_ID);
                    Log.e(" STR_User_Socail_User_Name :", "" + STR_User_Socail_User_Name);
                    Log.e(" STR_User_Socail_User_image :", "" + STR_User_Socail_User_image);

                    Toast.makeText(LoginSelectActivity.this, "Login Successfully ", Toast.LENGTH_SHORT).show();
                    Intent intentFbLogin = new Intent(getApplicationContext(), MainActivity.class);
                    intentFbLogin.putExtra("EXIT", "0");
                    intentFbLogin.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intentFbLogin);
                    finish();
                }
            } else {
                Log.e("********* Login Detail SEND *********", "NOT Success");
                Log.e("********* Login Detail SEND *********", "NOT Success");
                Toast.makeText(getApplicationContext(), "Oops!! Please check server connection .",
                        Toast.LENGTH_SHORT).show();

            }
        }

    }


    public void keyhash() {
        PackageInfo info;
        try {
            info = getPackageManager().getPackageInfo("com.ritesh.sevilla", PackageManager.GET_SIGNATURES);
            for (Signature signature : info.signatures) {
                MessageDigest md;
                md = MessageDigest.getInstance("SHA");
                md.update(signature.toByteArray());
                String something = new String(Base64.encode(md.digest(), 0));
                //String something = new String(Base64.encodeBytes(md.digest()));
                Log.e("hash key :", "" + something);
                System.out.println("hash key === " + something);
            }
        } catch (PackageManager.NameNotFoundException e1) {
            Log.e("name not found", e1.toString());
        } catch (NoSuchAlgorithmException e) {
            Log.e("no such an algorithm", e.toString());
        } catch (Exception e) {
            Log.e("exception", e.toString());
        }
    }


    @Override
    public void onBackPressed() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Are you sure you want to exit?")
                .setCancelable(false)
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        moveTaskToBack(true);
                        LoginSelectActivity.this.finish();

                        finish();
                    }
                })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                    }
                });
        AlertDialog alert = builder.create();
        alert.show();
    }


}