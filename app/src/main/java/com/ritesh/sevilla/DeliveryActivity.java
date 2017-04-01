package com.ritesh.sevilla;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.ritesh.sevilla.DeliverAddressPhoneFields.VerifyPhoneFragment;


/**
 * Created by ritesh on 9/3/17.
 */

public class DeliveryActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_delivery_address_flags);
        if (savedInstanceState == null) {
            getFragmentManager().beginTransaction()
                    .add(R.id.container, new VerifyPhoneFragment())
                    .commit();
        }


        /*signup data is on VeryfyPhoneFragment.class*/
        /*signup data is on VeryfyPhoneFragment.class*/
        /*signup data is on VeryfyPhoneFragment.class*/
        /*signup data is on VeryfyPhoneFragment.class*/
        /*signup data is on VeryfyPhoneFragment.class*/
        /*signup data is on VeryfyPhoneFragment.class*/
        /*signup data is on VeryfyPhoneFragment.class*/
    }
}
