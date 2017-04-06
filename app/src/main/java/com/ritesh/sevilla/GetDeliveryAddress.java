package com.ritesh.sevilla;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.util.Log;
import android.view.HapticFeedbackConstants;
import android.view.MotionEvent;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.nispok.snackbar.Snackbar;
import com.nispok.snackbar.SnackbarManager;
import com.rey.material.widget.FloatingActionButton;
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
 * Created by ritesh on 6/4/17.
 */
@SuppressWarnings("deprecation")
public class GetDeliveryAddress extends AppCompatActivity {

    @BindView(R.id.toolbar_my_address)
    Toolbar TB_my_address;

    @BindView(R.id.tv_my_address_user_f_name)
    TextView TV_my_address_user_f_name;

    @BindView(R.id.tv_my_address_user_l_name)
    TextView TV_my_address_user_l_name;

    @BindView(R.id.tv_my_address_user_phone)
    TextView TV_my_address_user_phone;

    @BindView(R.id.tv_my_address_user_address)
    TextView TV_my_address_user_address;

    @BindView(R.id.tv_my_address_user_country)
    TextView TV_my_address_user_country;

    @BindView(R.id.tv_my_address_user_state)
    TextView TV_my_address_user_state;

    @BindView(R.id.tv_my_address_user_city)
    TextView TV_my_address_user_city;

    @BindView(R.id.tv_my_address_user_zip)
    TextView TV_my_address_user_zip;

    @BindView(R.id.cv_my_address_confirm)
    CardView CV_my_address_confirm;

    @BindView(R.id.cv_my_address_confirm_pressed)
    CardView CV_my_address_confirm_pressed;

    @BindView(R.id.rl_my_address_progress)
    RelativeLayout RL_my_address_progress;

    CircularProgressBar CPB_my_address;

    @BindView(R.id.fab_edit_address)
    FloatingActionButton FAB_edit_address;

    String
            User_ID = "",
            Get_user_address_ID = "",
            Get_user_address_status = "",
            Get_user_address_result = "",
            Get_user_address_F_name = "",
            Get_user_address_L_name = "",
            Get_user_address_address = "",
            Get_user_address_phone_number = "",
            Get_user_address_country = "",
            Get_user_address_state = "",
            Get_user_address_city = "",
            Get_user_address_zipcode = "",
            Get_user_address_message = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_get_delivery_address);
        ButterKnife.bind(this);


        Appconstant.sh = getSharedPreferences(Appconstant.MyPREFERENCES, Context.MODE_PRIVATE);
        User_ID = Appconstant.sh.getString("id", null);
        Log.e("User_ID from SharedPref :", "" + User_ID);

        CPB_my_address = (CircularProgressBar) findViewById(R.id.cpb_my_address);
//        signupProgress.setVisibility(View.GONE);
        ((CircularProgressDrawable) CPB_my_address.getIndeterminateDrawable()).start();
        updateValues();

        if (Utils.isConnected(getApplication())) {
            UserGetAddressJsontask task = new UserGetAddressJsontask();
            task.execute();
        } else {

            SnackbarManager.show(
                    Snackbar.with(GetDeliveryAddress.this)
                            .position(Snackbar.SnackbarPosition.TOP)
                            .margin(15, 15)
                            .backgroundDrawable(R.drawable.snackbar_custom_layout)
                            .text("Please Your Internet Connectivity..!!"));

        }


        setSupportActionBar(TB_my_address);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

//        toolbar_sub_category.setNavigationIcon(R.drawable.ic_back_arrow); // your drawable

        TB_my_address.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed(); // Implemented by activity
            }
        });


        FAB_edit_address.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent GoEditDeliveryScreen = new Intent(getApplicationContext(), EditDeliveryActivity.class);
                startActivity(GoEditDeliveryScreen);

            }
        });



        CV_my_address_confirm.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {

                if (event.getAction() == MotionEvent.ACTION_DOWN) {

                    Log.e("Action ", "Down");
                    CV_my_address_confirm_pressed.setVisibility(View.VISIBLE);
                    CV_my_address_confirm.setVisibility(View.GONE);
                    v.performHapticFeedback(HapticFeedbackConstants.VIRTUAL_KEY);

//                    Toast.makeText(getApplicationContext(), "Add to cart Clicked", Toast.LENGTH_SHORT).show();
                    return true;
                }

                if (event.getAction() == MotionEvent.ACTION_MOVE) {

                    Log.e("Action ", "Move");
                    CV_my_address_confirm_pressed.setVisibility(View.VISIBLE);
                    CV_my_address_confirm.setVisibility(View.GONE);
                    return true;

                }
                if (event.getAction() == MotionEvent.ACTION_UP) {

                    Log.e("Action ", "Up");

                    CV_my_address_confirm_pressed.setVisibility(View.GONE);
                    CV_my_address_confirm.setVisibility(View.VISIBLE);
                    /*Intent MyCartPage = new Intent(GetDeliveryAddress.this, MyCartActivity.class);
                    startActivity(MyCartPage);*/

                    SnackbarManager.show(
                            Snackbar.with(GetDeliveryAddress.this)
                                    .position(Snackbar.SnackbarPosition.TOP)
                                    .margin(15, 15)
                                    .backgroundDrawable(R.drawable.snackbar_custom_layout)
                                    .text("Confirm Clicked"));

                    return true;
                }


                return false;
            }
        });


    }


    private void updateValues() {
        CircularProgressDrawable circularProgressDrawable;
        CircularProgressDrawable.Builder b = new CircularProgressDrawable.Builder(getApplicationContext())
                .colors(getResources().getIntArray(R.array.gplus_colors))
                /*.sweepSpeed(mSpeed)
                .rotationSpeed(mSpeed)
                .strokeWidth(dpToPx(mStrokeWidth))*/
                .style(CircularProgressDrawable.STYLE_ROUNDED);
       /* if (mCurrentInterpolator != null) {
            b.sweepInterpolator(mCurrentInterpolator);
        }*/
        CPB_my_address.setIndeterminateDrawable(circularProgressDrawable = b.build());

        // /!\ Terrible hack, do not do this at home!
        circularProgressDrawable.setBounds(0,
                0,
                CPB_my_address.getWidth(),
                CPB_my_address.getHeight());
        CPB_my_address.setVisibility(View.INVISIBLE);
        CPB_my_address.setVisibility(View.VISIBLE);
    }


    private class UserGetAddressJsontask extends AsyncTask<String, Void, String> {

        boolean iserror = false;

        @Override
        protected void onPreExecute() {
            // TODO Auto-generated method stub
            //  loginprogressbar.setVisibility(View.VISIBLE);
            Log.e("******* NOW UserGetAddressJsontask WEB SERVICE IS RUNNING *******", "YES");
            Log.e("******* NOW UserGetAddressJsontask WEB SERVICE IS RUNNING *******", "YES");
            RL_my_address_progress.setVisibility(View.VISIBLE);
            Log.e("User_ID From Shared Preference :", "" + User_ID);
            Log.e("Sign in URL :",
                    "http://sevilla.centrocomercial.com.es/wp-content/plugins/webserv/get_address.php?user_id=" + User_ID);

        }

        protected String doInBackground(String... params) {
            Log.e("******* NOW UserGetAddressJsontask TASK IS in doInBackground *******", "YES");
            Log.e("******* NOW UserGetAddressJsontask TASK IS in doInBackground *******", "YES");

            HttpClient client = new DefaultHttpClient();
            HttpPost post = new HttpPost("http://sevilla.centrocomercial.com.es/wp-content/plugins/webserv/get_address.php?user_id=" + User_ID);

            try {

                HttpResponse response = client.execute(post);
                String objectAddress = EntityUtils.toString(response.getEntity());
                Log.e("*******objectAddress******** :", "" + objectAddress);

                //JSONArray js = new JSONArray(object);
                JSONObject jobectAddress = new JSONObject(objectAddress);
                Get_user_address_status = jobectAddress.getString("status");
                if (Get_user_address_status.equalsIgnoreCase("Ok")) {
                    Get_user_address_result = jobectAddress.getString("result");
                    Get_user_address_message = jobectAddress.getString("message");
                    Get_user_address_ID = jobectAddress.getString("user_id");
                    Get_user_address_F_name = jobectAddress.getString("first_name");
                    Get_user_address_L_name = jobectAddress.getString("last_name");
                    Get_user_address_address = jobectAddress.getString("address");
                    Get_user_address_phone_number = jobectAddress.getString("phone_number");
                    Get_user_address_country = jobectAddress.getString("country");
                    Get_user_address_state = jobectAddress.getString("state");
                    Get_user_address_city = jobectAddress.getString("city");
                    Get_user_address_zipcode = jobectAddress.getString("zipcode");

                }

            } catch (Exception e) {
                Log.v("22", "22" + e.getMessage());
                e.printStackTrace();
                iserror = true;
            }
            return Get_user_address_status;
        }

        @Override
        protected void onPostExecute(String result1) {
            // TODO Auto-generated method stub
            super.onPostExecute(result1);
            RL_my_address_progress.setVisibility(View.GONE);

            if (!iserror) {
                if (Get_user_address_status.equalsIgnoreCase("OK")) {

                    Log.e("No Error :", "Get_user_address_result Success    OK");

                    Log.e("Get_user_address_result :", "" + Get_user_address_result);
                    Log.e("Get_user_address_message :", "" + Get_user_address_message);
                    Log.e("Get_user_address_ID :", "" + Get_user_address_ID);
                    Log.e("Get_user_address_F_name :", "" + Get_user_address_F_name);
                    Log.e("Get_user_address_L_name :", "" + Get_user_address_L_name);
                    Log.e("Get_user_address_address :", "" + Get_user_address_address);
                    Log.e("Get_user_address_phone_number :", "" + Get_user_address_phone_number);
                    Log.e("Get_user_address_country :", "" + Get_user_address_country);
                    Log.e("Get_user_address_state :", "" + Get_user_address_state);
                    Log.e("Get_user_address_city :", "" + Get_user_address_city);
                    Log.e("Get_user_address_zipcode :", "" + Get_user_address_zipcode);



                    TV_my_address_user_f_name.setText(Html.fromHtml(Get_user_address_F_name));
                    TV_my_address_user_l_name.setText(Html.fromHtml(Get_user_address_L_name));
                    TV_my_address_user_phone.setText(Html.fromHtml(Get_user_address_phone_number));
                    TV_my_address_user_address.setText(Html.fromHtml(Get_user_address_address));
                    TV_my_address_user_country.setText(Html.fromHtml(Get_user_address_country));
                    TV_my_address_user_state.setText(Html.fromHtml(Get_user_address_state));
                    TV_my_address_user_city.setText(Html.fromHtml(Get_user_address_city));
                    TV_my_address_user_zip.setText(Html.fromHtml(Get_user_address_zipcode));

                } else if (Get_user_address_result.equalsIgnoreCase("unsuccessfull")) {

                    Log.e("********* UserGetAddressJsontask *********", "unsuccessfull ERROR");
                    Log.e("********* UserGetAddressJsontask *********", "unsuccessfull ERROR");
                    Log.e("********* UserGetAddressJsontask *********", "unsuccessfull ERROR");

                    SnackbarManager.show(
                            Snackbar.with(GetDeliveryAddress.this)
                                    .position(Snackbar.SnackbarPosition.TOP)
                                    .margin(15, 15)
                                    .backgroundDrawable(R.drawable.snackbar_custom_layout)
                                    .text("No Address Found"));

                }
            } else {
                Log.e("********* UserGetAddressJsontask *********", " ERROR");

                SnackbarManager.show(
                        Snackbar.with(GetDeliveryAddress.this)
                                .position(Snackbar.SnackbarPosition.TOP)
                                .margin(15, 15)
                                .backgroundDrawable(R.drawable.snackbar_custom_layout)
                                .text("Oops!! Please check server connection ."));

            }
        }

    }

}
