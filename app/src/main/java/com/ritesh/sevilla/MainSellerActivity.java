package com.ritesh.sevilla;

import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;

import com.facebook.FacebookSdk;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by ritesh on 13/4/17.
 */

public class MainSellerActivity extends AppCompatActivity {

    @BindView(R.id.drawerLayout_seller)
    DrawerLayout DL_seller;

    @BindView(R.id.toolbar_main_seller)
    Toolbar TB_main_seller;

    @BindView(R.id.containerView_seller)
    FrameLayout frame_container_seller;

    @BindView(R.id.rl_dr_header_seller)
    RelativeLayout RL_Dr_Header_seller;

    @BindView(R.id.rl_dr_home_seller)
    RelativeLayout RL_Dr_Home_seller;

    @BindView(R.id.rl_dr_inventory_seller)
    RelativeLayout RL_Dr_Inventory_seller;

    @BindView(R.id.rl_dr_publicate_new_seller)
    RelativeLayout RL_dr_publicate_new_seller;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FacebookSdk.sdkInitialize(getApplicationContext());
        setContentView(R.layout.activity_main_seller);
        ButterKnife.bind(this);

        setSupportActionBar(TB_main_seller);

        //create default navigation drawer toggle
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, DL_seller, TB_main_seller,
                R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        DL_seller.addDrawerListener(toggle);
        toggle.syncState();

    }
}
