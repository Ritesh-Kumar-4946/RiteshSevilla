/*
 * Copyright (c) 2014-2015 Amberfog.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.ritesh.sevilla.EditDeliverAddressPhoneFields;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.util.Log;
import android.view.HapticFeedbackConstants;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.RelativeLayout;

import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;
import com.jaredrummler.materialspinner.MaterialSpinner;
import com.nispok.snackbar.Snackbar;
import com.nispok.snackbar.SnackbarManager;
import com.ritesh.sevilla.Constant.Appconstant;
import com.ritesh.sevilla.Constant.Utils;
import com.ritesh.sevilla.GetDeliveryAddress;
import com.ritesh.sevilla.LoginActivity;
import com.ritesh.sevilla.R;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import fr.castorflex.android.circularprogressbar.CircularProgressBar;
import fr.castorflex.android.circularprogressbar.CircularProgressDrawable;

@SuppressWarnings("deprecation")
public class VerifyPhoneFragment extends BaseFlagFragment {

    public VerifyPhoneFragment() {
    }

    @BindView(R.id.toolbar_delivery_main)
    Toolbar TB_delivery_main;

    @BindView(R.id.edt_et_address_user_first_name)
    EditText ET_address_user_first_name;

    @BindView(R.id.edt_et_address_user_last_name)
    EditText ET_address_user_last_name;

    @BindView(R.id.edt_et_address_user_address)
    EditText ET_address_user_address;

    @BindView(R.id.et_address_phone)
    EditText ET_address_phone;

    @BindView(R.id.edt_et_address_user_zip_code)
    EditText ET_address_user_zip_code;


    @BindView(R.id.spinner_user_country)
    MaterialSpinner SP_user_country;

    @BindView(R.id.spinner_user_state)
    MaterialSpinner SP_user_state;

    @BindView(R.id.spinner_user_city)
    MaterialSpinner SP_user_city;

    @BindView(R.id.cv_et_address_continue_payment)
    CardView CV_et_address_continue_payment;

    @BindView(R.id.cv_et_address_continue_payment_click)
    CardView CV_et_address_continue_payment_click;


    @BindView(R.id.rl_address_progress)
    RelativeLayout RL_address_progress;


    /*DotLoader textDotLoaderCountry;
    @BindView(R.id.rl_country_dot_loader)
    RelativeLayout RL_country_dot_loader;
    @BindView(R.id.rl_address_spineer_user_state)
    RelativeLayout Rl_address_spineer_user_country;


    DotLoader textDotLoaderState;
    @BindView(R.id.rl_state_dot_loader)
    RelativeLayout RL_state_dot_loader;
    @BindView(R.id.rl_address_spineer_user_state)
    RelativeLayout RL_address_spineer_user_state;


    DotLoader textDotLoaderCity;
    @BindView(R.id.rl_city_dot_loader)
    RelativeLayout RL_city_dot_loader;
    @BindView(R.id.rl_address_spineer_user_city)
    RelativeLayout RL_address_spineer_user_city;*/


    CircularProgressBar CPB_address_progressbar_circular;

    String

            Str_set_user_f_name = "",
            Str_set_user_l_name = "",
            Str_set_user_street_address = "",
            Str_set_user_phone = "",
            Str_get_user_country = "",
            Str_set_user_country = "",
            Str_get_user_state = "",
            Str_set_user_state = "",
            Str_get_user_city = "",
            Str_set_user_city = "",
            Str_set_user_zip_code = "",


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
            Get_user_address_message = "",

    Get_user_country_status = "",
            Get_user_country_list_result = "",
            Get_user_country_list = "",
            Get_user_country_id = "",
            Get_user_country_result = "",
            Get_user_country_sortname = "",
            Get_user_country_name = "",
            Get_user_country_default = "",
            Get_user_country_SelectedValue = "",
            GetSet_user_country_ID = "",

    Get_user_state_status = "",
            Get_user_state_list_result = "",
            Get_user_state_list = "",
            Get_user_state_country_id = "",
            Get_user_state_id = "",
            Get_user_state_name = "",
            Get_user_state_result = "",
            Get_user_state_default = "",
            Get_user_state_SelectedValue = "",
            GetSet_user_state_ID = "",

    Get_user_city_status = "",
            Get_user_city_list_result = "",
            Get_user_city_list = "",
            Get_user_city_result = "",
            Get_user_city_state_id = "",
            Get_user_city_id = "",
            Get_user_city_name = "",
            Get_user_city_default = "",
            Get_user_city_SelectedValue = "",

    Set_user_address_ID = "",
            Set_user_address_F_name = "",
            Set_user_address_L_name = "",
            Set_user_address_address = "",
            Set_user_address_phone_number = "",
            Set_user_address_country = "",
            Set_user_address_state = "",
            Set_user_address_city = "",
            Set_user_address_zipcode = "",
            Set_user_address_message = "";

    ArrayList<String> USER_ADDRESS_COUNTRY_LIST = new ArrayList<String>();
    ArrayList<String> USER_ADDRESS_STATE_LIST = new ArrayList<String>();
    ArrayList<String> USER_ADDRESS_CITY_LIST = new ArrayList<String>();


    boolean ISerror = false;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.activity_edit_delivery_address, container, false);
        ButterKnife.bind(this, rootView);


        Appconstant.sh = getActivity().getSharedPreferences(Appconstant.MyPREFERENCES, Context.MODE_PRIVATE);
        User_ID = Appconstant.sh.getString("id", null);
        Log.e("User_ID from SharedPref :", "" + User_ID);


        /*textDotLoaderCountry = (DotLoader) rootView.findViewById(R.id.text_dot_loader_country);
        for (int i = 1; i < 4; i++) {
            textDotLoaderCountry.postDelayed(new DotIncrementRunnable(3 + i, textDotLoaderCountry), i * 3000);
        }



        textDotLoaderState = (DotLoader) rootView.findViewById(R.id.text_dot_loader_state);
        for (int i = 1; i < 4; i++) {
            textDotLoaderState.postDelayed(new DotIncrementRunnable(3 + i, textDotLoaderState), i * 3000);
        }



        textDotLoaderCity = (DotLoader) rootView.findViewById(R.id.text_dot_loader_city);
        for (int i = 1; i < 4; i++) {
            textDotLoaderCity.postDelayed(new DotIncrementRunnable(3 + i, textDotLoaderCity), i * 3000);
        }*/


        if (Utils.isConnected(getActivity())) {
            UserGetAddressJsontask task = new UserGetAddressJsontask();
            task.execute();
        } else {

            SnackbarManager.show(
                    Snackbar.with(getActivity())
                            .position(Snackbar.SnackbarPosition.TOP)
                            .margin(15, 15)
                            .backgroundDrawable(R.drawable.snackbar_custom_layout)
                            .text("Please Your Internet Connectivity..!!"));

        }


        CPB_address_progressbar_circular = (CircularProgressBar) rootView.findViewById(R.id.cpb_address_progressbar_circular);
//        signupProgress.setVisibility(View.GONE);
        ((CircularProgressDrawable) CPB_address_progressbar_circular.getIndeterminateDrawable()).start();
        updateValues();





        /*%%%%%%%%%%%%%%      Spinner Country (Start)        %%%%%%%%%%%%%%*/

         /*spinner click method and not clicked methos (Start)*/
        SP_user_country.setOnItemSelectedListener(new MaterialSpinner.OnItemSelectedListener<String>() {

            @Override
            public void onItemSelected(MaterialSpinner view, int position, long id, String item) {
                SP_user_country.performHapticFeedback(HapticFeedbackConstants.VIRTUAL_KEY);
                /*Toast.makeText(getActivity(), "Position :" + "  " + position + "  Clicked " + "" + item,
                        Toast.LENGTH_SHORT).show();*/

                Log.e("Country Detail :", "Position :" + "" + position
                        + "\t" + "ID :" + "" + id
                        + "\t" + "Name :" + "" + item);

                /*android.support.design.widget.Snackbar.make(view, "Clicked " + item,
                        android.support.design.widget.Snackbar.LENGTH_SHORT).show();*/
                Get_user_country_SelectedValue = item;
                Str_get_user_country = item;
                Str_set_user_country = Str_get_user_country;
                long pos = id;
                int posi = position;
                Log.e("pos :", "" + pos);
                Log.e("posi ID:", "" + posi);
                GetSet_user_country_ID = String.valueOf(posi);
                Log.e("GetSet_user_country_ID :", "" + GetSet_user_country_ID);
                Log.e("Str_get_user_country :", "" + Str_get_user_country);
                Log.e("Str_set_user_country :", "" + Str_set_user_country);
                Log.e("Get_user_country_SelectedValue :", "" + Get_user_country_SelectedValue);

                if (Utils.isConnected(getActivity())) {
                    UserStateListJsontask task = new UserStateListJsontask();
                    task.execute();
                } else {

                    SnackbarManager.show(
                            Snackbar.with(getActivity())
                                    .position(Snackbar.SnackbarPosition.TOP)
                                    .margin(15, 15)
                                    .backgroundDrawable(R.drawable.snackbar_custom_layout)
                                    .text("Please Your Internet Connectivity..!!"));

                }

            }
        });
        SP_user_country.setOnNothingSelectedListener(new MaterialSpinner.OnNothingSelectedListener() {

            @Override
            public void onNothingSelected(MaterialSpinner spinner) {
                /*Toast.makeText(getActivity(), "Please select Country First..!!",
                        Toast.LENGTH_SHORT).show();*/

                android.support.design.widget.Snackbar.make(spinner, "Please select Country First..!!",
                        android.support.design.widget.Snackbar.LENGTH_SHORT).show();


            }
        });
        /*%%%%%%%%%%%%%%      Spinner Country (End)        %%%%%%%%%%%%%%*/



        /*%%%%%%%%%%%%%%      Spinner State (Start)        %%%%%%%%%%%%%%*/
        SP_user_state.setOnItemSelectedListener(new MaterialSpinner.OnItemSelectedListener<String>() {

            @Override
            public void onItemSelected(MaterialSpinner view, int position, long id, String item) {
                SP_user_state.performHapticFeedback(HapticFeedbackConstants.VIRTUAL_KEY);
                /*Toast.makeText(getActivity(), "Position :" + "  " + position + "  Clicked " + "" + item,
                        Toast.LENGTH_SHORT).show();*/

                Log.e("State Detail :", "Position :" + "" + position
                        + "\t" + "ID :" + "" + id
                        + "\t" + "Name :" + "" + item);

                /*android.support.design.widget.Snackbar.make(view, "Clicked " + item,
                        android.support.design.widget.Snackbar.LENGTH_SHORT).show();*/
                Get_user_state_SelectedValue = item;
                Str_get_user_state = item;
                Str_set_user_state = Str_get_user_state;
                long pos = id;
                int posi = position;
                Log.e("pos :", "" + pos);
                Log.e("posi ID:", "" + posi);
                GetSet_user_state_ID = String.valueOf(posi);
                Log.e("GetSet_user_state_ID :", "" + GetSet_user_state_ID);
                Log.e("Str_get_user_state :", "" + Str_get_user_state);
                Log.e("Str_set_user_state :", "" + Str_set_user_state);

                if (Utils.isConnected(getActivity())) {
                    UserCityListJsontask task = new UserCityListJsontask();
                    task.execute();
                } else {

                    SnackbarManager.show(
                            Snackbar.with(getActivity())
                                    .position(Snackbar.SnackbarPosition.TOP)
                                    .margin(15, 15)
                                    .backgroundDrawable(R.drawable.snackbar_custom_layout)
                                    .text("Please Your Internet Connectivity..!!"));

                }


            }
        });
        SP_user_state.setOnNothingSelectedListener(new MaterialSpinner.OnNothingSelectedListener() {

            @Override
            public void onNothingSelected(MaterialSpinner spinner) {
                /*Toast.makeText(getActivity(), "Please select Country First..!!",
                        Toast.LENGTH_SHORT).show();*/

                android.support.design.widget.Snackbar.make(spinner, "Please select State First..!!",
                        android.support.design.widget.Snackbar.LENGTH_SHORT).show();


            }
        });
        /*%%%%%%%%%%%%%%      Spinner State (End)        %%%%%%%%%%%%%%*/



        /*%%%%%%%%%%%%%%      Spinner City (Start)        %%%%%%%%%%%%%%*/
        SP_user_city.setOnItemSelectedListener(new MaterialSpinner.OnItemSelectedListener<String>() {

            @Override
            public void onItemSelected(MaterialSpinner view, int position, long id, String item) {
                SP_user_city.performHapticFeedback(HapticFeedbackConstants.VIRTUAL_KEY);
                /*Toast.makeText(getActivity(), "Position :" + "  " + position + "  Clicked " + "" + item,
                        Toast.LENGTH_SHORT).show();*/

                Log.e("City Detail :", "Position :" + "" + position
                        + "\t" + "ID :" + "" + id
                        + "\t" + "Name :" + "" + item);

                /*android.support.design.widget.Snackbar.make(view, "Clicked " + item,
                        android.support.design.widget.Snackbar.LENGTH_SHORT).show();*/

                Get_user_city_SelectedValue = item;
                Str_get_user_city = item;
                Str_set_user_city = Str_get_user_city;
                Log.e("Get_user_city_SelectedValue :", "" + Get_user_city_SelectedValue);
                Log.e("Str_get_user_city :", "" + Str_get_user_city);
                Log.e("Str_set_user_city :", "" + Str_set_user_city);


            }
        });
        SP_user_city.setOnNothingSelectedListener(new MaterialSpinner.OnNothingSelectedListener() {

            @Override
            public void onNothingSelected(MaterialSpinner spinner) {
                /*Toast.makeText(getActivity(), "Please select Country First..!!",
                        Toast.LENGTH_SHORT).show();*/

                android.support.design.widget.Snackbar.make(spinner, "Please select City First..!!",
                        android.support.design.widget.Snackbar.LENGTH_SHORT).show();


            }
        });
        /*%%%%%%%%%%%%%%      Spinner City (End)        %%%%%%%%%%%%%%*/


        CV_et_address_continue_payment.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {


                Str_set_user_f_name = ET_address_user_first_name.getText().toString().trim();
                Str_set_user_l_name = ET_address_user_last_name.getText().toString().trim();
                Str_set_user_street_address = ET_address_user_address.getText().toString().trim();
                Str_set_user_phone = ET_address_phone.getText().toString().trim();
                Str_set_user_zip_code = ET_address_user_zip_code.getText().toString().trim();


                Log.e("Address Data :", "\n"
                        + "Str_set_user_f_name :" + "" + Str_set_user_f_name + "\n"
                        + "Str_set_user_l_name :" + "" + Str_set_user_l_name + "\n"
                        + "Str_set_user_street_address :" + "" + Str_set_user_street_address + "\n"
                        + "Str_set_user_phone :" + "" + Str_set_user_phone + "\n"
                        + "Str_set_user_zip_code :" + "" + Str_set_user_zip_code + "\n"
                        + "Str_set_user_country :" + "" + Str_set_user_country + "\n"
                        + "Str_set_user_state :" + "" + Str_set_user_state + "\n"
                        + "Str_set_user_city :" + "" + Str_set_user_city + "\n"
                );


                if (event.getAction() == MotionEvent.ACTION_DOWN) {

                    Log.e("Action ", "Down");
                    CV_et_address_continue_payment_click.setVisibility(View.VISIBLE);
                    CV_et_address_continue_payment.setVisibility(View.GONE);
                    v.performHapticFeedback(HapticFeedbackConstants.VIRTUAL_KEY);

//                    Toast.makeText(getApplicationContext(), "Add to cart Clicked", Toast.LENGTH_SHORT).show();
                    return true;
                }

                if (event.getAction() == MotionEvent.ACTION_MOVE) {

                    Log.e("Action ", "Move");
                    CV_et_address_continue_payment_click.setVisibility(View.VISIBLE);
                    CV_et_address_continue_payment.setVisibility(View.GONE);
                    return true;

                }
                if (event.getAction() == MotionEvent.ACTION_UP) {

                    Log.e("Action ", "Up");

                    CV_et_address_continue_payment_click.setVisibility(View.GONE);
                    CV_et_address_continue_payment.setVisibility(View.VISIBLE);
                    /*Intent MyCartPage = new Intent(GetDeliveryAddress.this, MyCartActivity.class);
                    startActivity(MyCartPage);*/

                    if (Str_set_user_f_name.equals("")) {
                        ISerror = true;
                        v.performHapticFeedback(HapticFeedbackConstants.VIRTUAL_KEY);
//                    v.playSoundEffect(SoundEffectConstants.CLICK);
                        /**************** Start Animation **************  **/
                        YoYo.with(Techniques.Tada)
                                .duration(700)
                                .playOn(ET_address_user_first_name);
                        /**************** End Animation ****************/

                    /*Toast.makeText(getApplicationContext(),
                            "Please enter your Email Id", Toast.LENGTH_SHORT).show();*/

                        SnackbarManager.show(
                                Snackbar.with(getActivity())
                                        .position(Snackbar.SnackbarPosition.TOP)
                                        .margin(15, 15)
                                        .backgroundDrawable(R.drawable.snackbar_custom_layout)
                                        .text("Please enter your First Name"));

                    } else if (Str_set_user_l_name.equals("")) {
                        ISerror = true;
                        v.performHapticFeedback(HapticFeedbackConstants.VIRTUAL_KEY);
//                    v.playSoundEffect(SoundEffectConstants.CLICK);
                        /**************** Start Animation **************  **/
                        YoYo.with(Techniques.Tada)
                                .duration(700)
                                .playOn(ET_address_user_last_name);
                        /**************** End Animation ****************/

                    /*Toast.makeText(getApplicationContext(),
                            "Please enter your Email Id", Toast.LENGTH_SHORT).show();*/

                        SnackbarManager.show(
                                Snackbar.with(getActivity())
                                        .position(Snackbar.SnackbarPosition.TOP)
                                        .margin(15, 15)
                                        .backgroundDrawable(R.drawable.snackbar_custom_layout)
                                        .text("Please enter your Last Name"));

                    } else if (Str_set_user_street_address.equals("")) {
                        ISerror = true;
                        v.performHapticFeedback(HapticFeedbackConstants.VIRTUAL_KEY);
//                    v.playSoundEffect(SoundEffectConstants.CLICK);
                        /**************** Start Animation **************  **/
                        YoYo.with(Techniques.Tada)
                                .duration(700)
                                .playOn(ET_address_user_address);
                        /**************** End Animation ****************/

                    /*Toast.makeText(getApplicationContext(),
                            "Please enter your Email Id", Toast.LENGTH_SHORT).show();*/

                        SnackbarManager.show(
                                Snackbar.with(getActivity())
                                        .position(Snackbar.SnackbarPosition.TOP)
                                        .margin(15, 15)
                                        .backgroundDrawable(R.drawable.snackbar_custom_layout)
                                        .text("Please enter your Street Address"));

                    } else if (Str_set_user_phone.equals("")) {
                        ISerror = true;
                        v.performHapticFeedback(HapticFeedbackConstants.VIRTUAL_KEY);
//                    v.playSoundEffect(SoundEffectConstants.CLICK);
                        /**************** Start Animation **************  **/
                        YoYo.with(Techniques.Tada)
                                .duration(700)
                                .playOn(ET_address_phone);
                        /**************** End Animation ****************/

                    /*Toast.makeText(getApplicationContext(),
                            "Please enter your Email Id", Toast.LENGTH_SHORT).show();*/

                        SnackbarManager.show(
                                Snackbar.with(getActivity())
                                        .position(Snackbar.SnackbarPosition.TOP)
                                        .margin(15, 15)
                                        .backgroundDrawable(R.drawable.snackbar_custom_layout)
                                        .text("Please enter your Phone Number"));

                    } else if (Str_set_user_country.equals("")) {
                        ISerror = true;
                        v.performHapticFeedback(HapticFeedbackConstants.VIRTUAL_KEY);
//                    v.playSoundEffect(SoundEffectConstants.CLICK);
                        /**************** Start Animation **************  **/
                        YoYo.with(Techniques.Tada)
                                .duration(700)
                                .playOn(SP_user_country);
                        /**************** End Animation ****************/

                    /*Toast.makeText(getApplicationContext(),
                            "Please enter your Email Id", Toast.LENGTH_SHORT).show();*/

                        SnackbarManager.show(
                                Snackbar.with(getActivity())
                                        .position(Snackbar.SnackbarPosition.TOP)
                                        .margin(15, 15)
                                        .backgroundDrawable(R.drawable.snackbar_custom_layout)
                                        .text("Please select your Country"));

                    } else if (Str_set_user_state.equals("")) {
                        ISerror = true;
                        v.performHapticFeedback(HapticFeedbackConstants.VIRTUAL_KEY);
//                    v.playSoundEffect(SoundEffectConstants.CLICK);
                        /**************** Start Animation **************  **/
                        YoYo.with(Techniques.Tada)
                                .duration(700)
                                .playOn(SP_user_state);
                        /**************** End Animation ****************/

                    /*Toast.makeText(getApplicationContext(),
                            "Please enter your Email Id", Toast.LENGTH_SHORT).show();*/

                        SnackbarManager.show(
                                Snackbar.with(getActivity())
                                        .position(Snackbar.SnackbarPosition.TOP)
                                        .margin(15, 15)
                                        .backgroundDrawable(R.drawable.snackbar_custom_layout)
                                        .text("Please select your State"));

                    } else if (Str_set_user_city.equals("")) {
                        ISerror = true;
                        v.performHapticFeedback(HapticFeedbackConstants.VIRTUAL_KEY);
//                    v.playSoundEffect(SoundEffectConstants.CLICK);
                        /**************** Start Animation **************  **/
                        YoYo.with(Techniques.Tada)
                                .duration(700)
                                .playOn(SP_user_city);
                        /**************** End Animation ****************/

                    /*Toast.makeText(getApplicationContext(),
                            "Please enter your Email Id", Toast.LENGTH_SHORT).show();*/

                        SnackbarManager.show(
                                Snackbar.with(getActivity())
                                        .position(Snackbar.SnackbarPosition.TOP)
                                        .margin(15, 15)
                                        .backgroundDrawable(R.drawable.snackbar_custom_layout)
                                        .text("Please select your City"));

                    } else if (Str_set_user_zip_code.equals("")) {
                        ISerror = true;
                        v.performHapticFeedback(HapticFeedbackConstants.VIRTUAL_KEY);
//                    v.playSoundEffect(SoundEffectConstants.CLICK);
                        /**************** Start Animation **************  **/
                        YoYo.with(Techniques.Tada)
                                .duration(700)
                                .playOn(SP_user_city);
                        /**************** End Animation ****************/

                    /*Toast.makeText(getApplicationContext(),
                            "Please enter your Email Id", Toast.LENGTH_SHORT).show();*/

                        SnackbarManager.show(
                                Snackbar.with(getActivity())
                                        .position(Snackbar.SnackbarPosition.TOP)
                                        .margin(15, 15)
                                        .backgroundDrawable(R.drawable.snackbar_custom_layout)
                                        .text("Please select your City"));

                    } else if (!ISerror) {

                        v.performHapticFeedback(HapticFeedbackConstants.VIRTUAL_KEY);
                    /*Toast.makeText(getApplicationContext(),
                            "Good", Toast.LENGTH_SHORT).show();*/

                        SnackbarManager.show(
                                Snackbar.with(getActivity())
                                        .position(Snackbar.SnackbarPosition.TOP)
                                        .margin(15, 15)
                                        .backgroundDrawable(R.drawable.snackbar_custom_layout)
                                        .text("Good All Value Correct"));

//                    v.playSoundEffect(android.view.SoundEffectConstants.CLICK);


                    }

                    /*SnackbarManager.show(
                            Snackbar.with(getActivity())
                                    .position(Snackbar.SnackbarPosition.TOP)
                                    .margin(15, 15)
                                    .backgroundDrawable(R.drawable.snackbar_custom_layout)
                                    .text("Confirm Clicked"));
*/
                    return true;
                }


                return false;
            }
        });


        initUI(rootView);
        return rootView;
    }


    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        initCodes(getActivity());
    }


    @Override
    protected void send() {
        hideKeyboard(mPhoneEdit);
        mPhoneEdit.setError(null);
        String phone = validate();
        if (phone == null) {
            mPhoneEdit.requestFocus();
            mPhoneEdit.setError(getString(R.string.label_error_incorrect_phone));
            return;
        }
//        Toast.makeText(getActivity(), "send to " + phone, Toast.LENGTH_SHORT).show();
    }


    /*progressbar data (Start)*/
    private void updateValues() {
        CircularProgressDrawable circularProgressDrawable;
        CircularProgressDrawable.Builder b = new CircularProgressDrawable.Builder(getActivity())
                .colors(getResources().getIntArray(R.array.gplus_colors))
                /*.sweepSpeed(mSpeed)
                .rotationSpeed(mSpeed)
                .strokeWidth(dpToPx(mStrokeWidth))*/
                .style(CircularProgressDrawable.STYLE_ROUNDED);
       /* if (mCurrentInterpolator != null) {
            b.sweepInterpolator(mCurrentInterpolator);
        }*/
        CPB_address_progressbar_circular.setIndeterminateDrawable(circularProgressDrawable = b.build());

        // /!\ Terrible hack, do not do this at home!
        circularProgressDrawable.setBounds(0,
                0,
                CPB_address_progressbar_circular.getWidth(),
                CPB_address_progressbar_circular.getHeight());
        CPB_address_progressbar_circular.setVisibility(View.INVISIBLE);
        CPB_address_progressbar_circular.setVisibility(View.VISIBLE);
    }
    /*progressbar data (End)*/


    private class UserCountryListJsontask extends AsyncTask<String, Void, ArrayList<String>> {

        boolean iserror = false;

        @Override
        protected void onPreExecute() {
            // TODO Auto-generated method stub
            //  loginprogressbar.setVisibility(View.VISIBLE);
            Log.e("******* UserCountryListJsontask IS RUNNING *******", "YES");
            Log.e("******* UserCountryListJsontask IS RUNNING *******", "YES");
            RL_address_progress.setVisibility(View.VISIBLE);
//            RL_country_dot_loader.setVisibility(View.VISIBLE);
        }

        @Override
        protected ArrayList<String> doInBackground(String... params) {
            Log.e("******* NOW BACKGROUND TASK IS RUNNING *******", "YES");
            Log.e("******* NOW BACKGROUND TASK IS RUNNING *******", "YES");

            HttpClient client = new DefaultHttpClient();
            HttpPost post = new HttpPost("http://sevilla.centrocomercial.com.es/wp-content/plugins/webserv/country_list.php");

            try {
                HttpResponse response = client.execute(post);
                String objectCountry = EntityUtils.toString(response.getEntity());
                Log.e("doinBackgrouns UserCountry List Responce :", "" + objectCountry);

                USER_ADDRESS_COUNTRY_LIST.add("Select Your Country :");
                JSONObject jsonObject = new JSONObject(objectCountry);
                Get_user_country_status = jsonObject.getString("status");
                Get_user_country_list_result = jsonObject.getString("country_result");
                Get_user_country_list = jsonObject.getString("country_list");
                Log.e("**** Json Country List data *********", " " + Get_user_country_list);

                if (Get_user_country_status.equals("OK")) {
                    Log.e("doInBackground Get_user_country_status is", "OK");

                    JSONArray jsonArray = new JSONArray(Get_user_country_list);
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject jsonCountryObject = jsonArray.getJSONObject(i);
                        Get_user_country_name = jsonCountryObject.getString("country_name");
                        USER_ADDRESS_COUNTRY_LIST.add(Get_user_country_name);
                        Log.e(" doinBackground Get_user_country_name :", "" + Get_user_country_name);

                        Get_user_country_result = jsonArray.getJSONObject(i).getString("result");
                        Get_user_country_id = jsonArray.getJSONObject(i).getString("country_id");
                        Get_user_country_sortname = jsonArray.getJSONObject(i).getString("sortname");
                        Get_user_country_name = jsonArray.getJSONObject(i).getString("country_name");

                        Log.e("Get_user_country_result :", "" + Get_user_country_result);
                        Log.e("Get_user_country_id :", "" + Get_user_country_id);
                        Log.e("Get_user_country_sortname :", "" + Get_user_country_sortname);
                        Log.e("Get_user_country_name :", "" + Get_user_country_name);

                    }

                }


                return USER_ADDRESS_COUNTRY_LIST;

            } catch (Exception e) {
                Log.v("22", "22" + e.getMessage());
                e.printStackTrace();
                iserror = true;
            }
            return null;
        }

        @Override
        protected void onPostExecute(ArrayList<String> result1) {
            // TODO Auto-generated method stub
            super.onPostExecute(result1);
            RL_address_progress.setVisibility(View.GONE);
//            RL_country_dot_loader.setVisibility(View.GONE);
//            Rl_address_spineer_user_country.setVisibility(View.VISIBLE);

            if (!iserror) {

                if (result1 == null) {
                    Log.e("result1 :", "Null");
                } else if (result1.isEmpty()) {

                    Log.e("result1 :", "empty");
                } else {
                    ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(),
                            android.R.layout.simple_spinner_dropdown_item, result1);
                    SP_user_country.setItems(result1);
                    Get_user_country_default = result1.get(0);
                    Log.e("Get_user_country_default ", "" + Get_user_country_default);

                    Log.e(" Country list result :", "" + result1.size());
                }

            }

        }

    }


    private class UserStateListJsontask extends AsyncTask<String, Void, ArrayList<String>> {

        boolean iserror = false;

        @Override
        protected void onPreExecute() {
            // TODO Auto-generated method stub
            //  loginprogressbar.setVisibility(View.VISIBLE);
            Log.e("******* UserStateListJsontask IS RUNNING *******", "YES");
            Log.e("******* UserStateListJsontask IS RUNNING *******", "YES");
            RL_address_progress.setVisibility(View.VISIBLE);
//            RL_state_dot_loader.setVisibility(View.VISIBLE);
        }

        @Override
        protected ArrayList<String> doInBackground(String... params) {
            Log.e("******* NOW BACKGROUND UserStateListJsontask TASK IS RUNNING *******", "YES");
            Log.e("******* NOW BACKGROUND UserStateListJsontask TASK IS RUNNING *******", "YES");

            HttpClient clientState = new DefaultHttpClient();
            HttpPost postState = new HttpPost("http://sevilla.centrocomercial.com.es/wp-content/plugins/webserv/state_list.php?country_id=" + GetSet_user_country_ID);

            Log.e("http://sevilla.centrocomercial.com.es/wp-content/plugins/webserv/state_list.php?country_id=", "" + GetSet_user_country_ID);
            try {
                HttpResponse responseState = clientState.execute(postState);
                String objectState = EntityUtils.toString(responseState.getEntity());
                Log.e("doinBackground UserState List Responce :", "" + objectState);

                USER_ADDRESS_STATE_LIST.add("Select Your State :");
                JSONObject jsonObjectState = new JSONObject(objectState);
                Get_user_state_status = jsonObjectState.getString("status");
                Get_user_state_list_result = jsonObjectState.getString("state_result");
                Get_user_state_list = jsonObjectState.getString("state_list");
                Log.e("**** Json State List data *********", " " + Get_user_state_list);

                if (Get_user_state_status.equals("OK")) {
                    Log.e("doInBackground Get_user_state_status is", "OK");

                    JSONArray jsonArray = new JSONArray(Get_user_state_list);
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject jsonStateObject = jsonArray.getJSONObject(i);
                        Get_user_state_name = jsonStateObject.getString("statename");
                        USER_ADDRESS_STATE_LIST.add(Get_user_state_name);
                        Log.e(" doinBackground Get_user_state_name :", "" + Get_user_state_name);

                        Get_user_state_result = jsonArray.getJSONObject(i).getString("result");
                        Get_user_state_country_id = jsonArray.getJSONObject(i).getString("country_id");
                        Get_user_state_id = jsonArray.getJSONObject(i).getString("state_id");
                        Get_user_state_name = jsonArray.getJSONObject(i).getString("statename");

                        Log.e("Get_user_state_result :", "" + Get_user_state_result);
                        Log.e("Get_user_state_country_id :", "" + Get_user_state_country_id);
                        Log.e("Get_user_state_id :", "" + Get_user_state_id);
                        Log.e("Get_user_state_name :", "" + Get_user_state_name);

                    }

                }


                return USER_ADDRESS_STATE_LIST;

            } catch (Exception e) {
                Log.v("22", "22" + e.getMessage());
                e.printStackTrace();
                iserror = true;
            }
            return null;
        }

        @Override
        protected void onPostExecute(ArrayList<String> resultState) {
            // TODO Auto-generated method stub
            super.onPostExecute(resultState);
            RL_address_progress.setVisibility(View.GONE);
//            RL_state_dot_loader.setVisibility(View.GONE);
//            RL_address_spineer_user_state.setVisibility(View.VISIBLE);
            if (!iserror) {

                if (resultState == null) {
                    Log.e("result1 :", "Null");
                } else if (resultState.isEmpty()) {

                    Log.e("result1 :", "empty");
                } else {
                    ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(),
                            android.R.layout.simple_spinner_dropdown_item, resultState);
                    SP_user_state.setItems(resultState);
                    Get_user_state_default = resultState.get(0);
                    Log.e("Get_user_state_default ", "" + Get_user_state_default);

                    Log.e(" State list result :", "" + resultState.size());
                }

            }

        }

    }


    private class UserCityListJsontask extends AsyncTask<String, Void, ArrayList<String>> {

        boolean iserror = false;

        @Override
        protected void onPreExecute() {
            // TODO Auto-generated method stub
            //  loginprogressbar.setVisibility(View.VISIBLE);
            Log.e("******* UserCityListJsontask IS RUNNING *******", "YES");
            Log.e("******* UserCityListJsontask IS RUNNING *******", "YES");
            RL_address_progress.setVisibility(View.VISIBLE);
//            RL_city_dot_loader.setVisibility(View.VISIBLE);
        }

        @Override
        protected ArrayList<String> doInBackground(String... params) {
            Log.e("******* NOW BACKGROUND UserCityListJsontask TASK IS RUNNING *******", "YES");
            Log.e("******* NOW BACKGROUND UserCityListJsontask TASK IS RUNNING *******", "YES");

            HttpClient clientCity = new DefaultHttpClient();
            HttpPost postCity = new HttpPost("http://sevilla.centrocomercial.com.es/wp-content/plugins/webserv/city_list.php?state_id=" + GetSet_user_state_ID);

            Log.e("http://sevilla.centrocomercial.com.es/wp-content/plugins/webserv/state_list.php?country_id=", "" + GetSet_user_state_ID);
            try {
                HttpResponse responseCity = clientCity.execute(postCity);
                String objectCity = EntityUtils.toString(responseCity.getEntity());
                Log.e("doinBackground UserCity List Responce :", "" + objectCity);

                USER_ADDRESS_CITY_LIST.add("Select Your City :");
                JSONObject jsonObjectCity = new JSONObject(objectCity);
                Get_user_city_status = jsonObjectCity.getString("status");
                Get_user_city_list_result = jsonObjectCity.getString("city_result");
                Get_user_city_list = jsonObjectCity.getString("city_list");
                Log.e("**** Json city List data *********", " " + Get_user_city_list);

                if (Get_user_city_status.equals("OK")) {
                    Log.e("doInBackground Get_user_city_status is", "OK");

                    JSONArray jsonArray = new JSONArray(Get_user_city_list);
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject jsonCityObject = jsonArray.getJSONObject(i);
                        Get_user_city_name = jsonCityObject.getString("cityname");
                        USER_ADDRESS_CITY_LIST.add(Get_user_city_name);
                        Log.e(" doinBackground Get_user_city_name :", "" + Get_user_city_name);

                        Get_user_city_result = jsonArray.getJSONObject(i).getString("result");
                        Get_user_city_state_id = jsonArray.getJSONObject(i).getString("state_id");
                        Get_user_city_id = jsonArray.getJSONObject(i).getString("city_id");
                        Get_user_city_name = jsonArray.getJSONObject(i).getString("cityname");

                        Log.e("Get_user_state_result :", "" + Get_user_state_result);
                        Log.e("Get_user_state_country_id :", "" + Get_user_state_country_id);
                        Log.e("Get_user_state_id :", "" + Get_user_state_id);
                        Log.e("Get_user_state_name :", "" + Get_user_state_name);

                    }

                }


                return USER_ADDRESS_CITY_LIST;

            } catch (Exception e) {
                Log.v("22", "22" + e.getMessage());
                e.printStackTrace();
                iserror = true;
            }
            return null;
        }

        @Override
        protected void onPostExecute(ArrayList<String> resultState) {
            // TODO Auto-generated method stub
            super.onPostExecute(resultState);
            RL_address_progress.setVisibility(View.GONE);
//            RL_city_dot_loader.setVisibility(View.GONE);
//            RL_address_spineer_user_city.setVisibility(View.VISIBLE);
            if (!iserror) {

                if (resultState == null) {
                    Log.e("resultState :", "Null");
                } else if (resultState.isEmpty()) {

                    Log.e("resultState :", "empty");
                } else {
                    ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(),
                            android.R.layout.simple_spinner_dropdown_item, resultState);
                    SP_user_city.setItems(resultState);
                    Get_user_city_default = resultState.get(0);
                    Log.e("Get_user_city_default ", "" + Get_user_city_default);

                    Log.e(" city list result :", "" + resultState.size());
                }

            }

        }

    }


    private class UserGetAddressJsontask extends AsyncTask<String, Void, String> {

        boolean iserror = false;

        @Override
        protected void onPreExecute() {
            // TODO Auto-generated method stub
            //  loginprogressbar.setVisibility(View.VISIBLE);
            Log.e("******* NOW UserGetAddressJsontask WEB SERVICE IS RUNNING *******", "YES");
            Log.e("******* NOW UserGetAddressJsontask WEB SERVICE IS RUNNING *******", "YES");
            RL_address_progress.setVisibility(View.VISIBLE);
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
            RL_address_progress.setVisibility(View.GONE);

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

                    ET_address_user_first_name.setText(Html.fromHtml(Get_user_address_F_name));
                    ET_address_user_last_name.setText(Html.fromHtml(Get_user_address_L_name));
                    ET_address_user_address.setText(Html.fromHtml(Get_user_address_address));
                    ET_address_phone.setText(Html.fromHtml(Get_user_address_phone_number));
                    ET_address_user_zip_code.setText(Html.fromHtml(Get_user_address_zipcode));

                } else if (Get_user_address_result.equalsIgnoreCase("unsuccessfull")) {

                    Log.e("********* UserGetAddressJsontask *********", "unsuccessfull ERROR");
                    Log.e("********* UserGetAddressJsontask *********", "unsuccessfull ERROR");
                    Log.e("********* UserGetAddressJsontask *********", "unsuccessfull ERROR");

                    SnackbarManager.show(
                            Snackbar.with(getActivity())
                                    .position(Snackbar.SnackbarPosition.TOP)
                                    .margin(15, 15)
                                    .backgroundDrawable(R.drawable.snackbar_custom_layout)
                                    .text("Get Address Unsuccessfull"));

                }
            } else {
                Log.e("********* UserGetAddressJsontask *********", " ERROR");

                SnackbarManager.show(
                        Snackbar.with(getActivity())
                                .position(Snackbar.SnackbarPosition.TOP)
                                .margin(15, 15)
                                .backgroundDrawable(R.drawable.snackbar_custom_layout)
                                .text("Oops!! Please check server connection ."));

            }


            if (Utils.isConnected(getActivity())) {
                UserCountryListJsontask task = new UserCountryListJsontask();
                task.execute();
            } else {

                SnackbarManager.show(
                        Snackbar.with(getActivity())
                                .position(Snackbar.SnackbarPosition.TOP)
                                .margin(15, 15)
                                .backgroundDrawable(R.drawable.snackbar_custom_layout)
                                .text("Please Your Internet Connectivity..!!"));

            }


        }

    }




    /*private static class DotIncrementRunnable implements Runnable {
        private int mNumberOfDots;
        private DotLoader mDotLoader;

        private DotIncrementRunnable(int numberOfDots, DotLoader dotLoader) {
            mNumberOfDots = numberOfDots;
            mDotLoader = dotLoader;
        }

        @Override
        public void run() {
            mDotLoader.setNumberOfDots(mNumberOfDots);
        }
    }*/


}
