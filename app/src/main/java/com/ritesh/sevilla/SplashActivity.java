package com.ritesh.sevilla;

import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.util.Base64;
import android.util.Log;
import android.widget.ImageView;

import com.ritesh.sevilla.Constant.Appconstant;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import butterknife.BindView;
import butterknife.ButterKnife;

public class SplashActivity extends AppCompatActivity {

    @BindView(R.id.iv_logo_splash)
    ImageView IV_logo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        ButterKnife.bind(this);
        /*Toast.makeText(getApplicationContext(),
                "Please click above image", Toast.LENGTH_SHORT).show();*/

        /*IV_logo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intentLogin = new Intent(getApplicationContext(), LoginSelectActivity.class);
                startActivity(intentLogin);
            }
        });*/


        // here initializing the shared preference
        Appconstant.sh = getSharedPreferences("myprefe", 0);
        Appconstant.editor = Appconstant.sh.edit();

        // check here if user is login or not
        Appconstant.str_login_test = Appconstant.sh.getString("loginTest", null);
        Appconstant.str_login_usertype = Appconstant.sh.getString("usertype", null);

        if (getIntent().getBooleanExtra("EXIT", false)) {
            finish();
            return;
        }


        new Handler().postDelayed(new Runnable() {

            @Override
            public void run() {
                /* if user login test is true on oncreate then redirect the user to result page */

                if (Appconstant.str_login_test != null
                        && !Appconstant.str_login_test.toString().trim().equals("")) {

                    Log.e("Login detail found :", "Go to Main Screen");
                    if (Appconstant.str_login_usertype.equalsIgnoreCase("Buyer")) {
                        Log.e("Login User Type :", "Buyer");
                        Intent Gomainscreen = new Intent(getApplicationContext(), MainBuyerActivity.class);
                        startActivity(Gomainscreen);
                        finish();

                    } else if (Appconstant.str_login_usertype.equalsIgnoreCase("Seller")) {
                        Log.e("Login User Type :", "Seller");
                        Intent Gomainscreen = new Intent(getApplicationContext(), MainSellerActivity.class);
                        startActivity(Gomainscreen);
                        finish();
                    }
                }
                /* if user login test is false on oncreate then redirect the user to login & registration page */
                else {

                    Log.e("Login detail not found :", "Go to Login Screen");
                    Intent Gologinscreen = new Intent(getApplicationContext(), LoginSelectActivity.class);
                    startActivity(Gologinscreen);
                    finish();

                }
            }

        }, 1000);


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
                Log.e("hash key", something);
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


}
