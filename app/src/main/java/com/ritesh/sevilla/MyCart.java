package com.ritesh.sevilla;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.HapticFeedbackConstants;
import android.view.MotionEvent;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.Toast;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by ritesh on 25/2/17.
 */

public class MyCart extends AppCompatActivity {


    @BindView(R.id.rl_sub_category_button_cart)
    RelativeLayout Rl_checkout;

    @BindView(R.id.rl_sub_category_button_cart_zoon)
    RelativeLayout Rl_checkout_zoom;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_cart);
        ButterKnife.bind(this);

        Rl_checkout.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {

                if (event.getAction() == MotionEvent.ACTION_DOWN) {

                    Log.e("Action ", "Down");
                    Rl_checkout_zoom.setVisibility(View.VISIBLE);
                    Rl_checkout.setVisibility(View.GONE);
                    v.performHapticFeedback(HapticFeedbackConstants.VIRTUAL_KEY);
                    Toast.makeText(getApplicationContext(), "Add to cart Clicked", Toast.LENGTH_SHORT).show();

                    return true;
                }

                if (event.getAction() == MotionEvent.ACTION_MOVE) {

                    Log.e("Action ", "Move");
                    Rl_checkout_zoom.setVisibility(View.VISIBLE);
                    Rl_checkout.setVisibility(View.GONE);
                    return true;

                }
                if (event.getAction() == MotionEvent.ACTION_UP) {

                    Log.e("Action ", "Up");
                    Rl_checkout_zoom.setVisibility(View.GONE);
                    Rl_checkout.setVisibility(View.VISIBLE);
                    return true;
                }


                return false;
            }
        });



    }


}
