package com.ritesh.sevilla;

import android.content.Context;
import android.graphics.Rect;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.ritesh.sevilla.Beans.BeanMyCartList;
import com.ritesh.sevilla.Constant.Appconstant;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import fr.castorflex.android.circularprogressbar.CircularProgressBar;
import fr.castorflex.android.circularprogressbar.CircularProgressDrawable;

/**
 * Created by ritesh on 10/4/17.
 */

public class InventorySingleOrderActivity extends AppCompatActivity {

    @BindView(R.id.toolbar_single_order)
    Toolbar TB_single_order;

    @BindView(R.id.tv_single_order_total_number)
    TextView TV_single_order_total_number;

    @BindView(R.id.tv_single_order_total_items_price)
    TextView TV_single_order_total_items_price;


    List<InventorySingleOrderActivity> inventorySingleOrderrowItems;
    private ArrayList<String> InventorySingleOrder_id;
    private ArrayList<String> InventorySingleOrder_Name;
    private ArrayList<String> InventorySingleOrder_Image;
    private ArrayList<String> InventorySingleOrder_Price;
    private ArrayList<String> InventorySingleOrder_Quantity;
    private ArrayList<String> InventorySingleOrder_DeliveryCharge;
    private ArrayList<String> InventorySingleOrder_DeliveryDate;
    List<BeanMyCartList> beanMyCartlist0;


    @BindView(R.id.rl_listview_my_cart_progress)
    RelativeLayout RL_listview_single_order_progress;

    CircularProgressBar mCPB_inventory_single_order;

    @BindView(R.id.recycler_view_my_cart)
    RecyclerView mycartlist_recylerView;

//    CategoryAdapter categoryAdapter;

    RecyclerView.LayoutManager mLayoutManager;


    String
            User_ID = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inventory_single_detail);
        ButterKnife.bind(this);


        Appconstant.sh = getSharedPreferences(Appconstant.MyPREFERENCES, Context.MODE_PRIVATE);
        User_ID = Appconstant.sh.getString("id", null);
        Log.e("User_ID from SharedPref :", "" + User_ID);


        /*recycler data (Start 02 of 03)*/
        /*image settin by universal image loader */
        DisplayImageOptions defaultOptions = new DisplayImageOptions.Builder().cacheInMemory(true).cacheOnDisk(true).build();
        ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(InventorySingleOrderActivity.this).defaultDisplayImageOptions(defaultOptions).build();
        ImageLoader.getInstance().init(config);
        /*image settin by universal image loader */

         /*circular progress bar (Start)*/
        mCPB_inventory_single_order = (CircularProgressBar) findViewById(R.id.listview_single_order_progressbar_circular);
//        signupProgress.setVisibility(View.GONE);
        ((CircularProgressDrawable) mCPB_inventory_single_order.getIndeterminateDrawable()).start();
        updateValues();
        /*circular progress bar (End)*/


        setSupportActionBar(TB_single_order);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

//        toolbar_sub_category.setNavigationIcon(R.drawable.ic_back_arrow); // your drawable

        TB_single_order.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed(); // Implemented by activity
            }
        });


        inventorySingleOrderrowItems = new ArrayList<InventorySingleOrderActivity>();
        InventorySingleOrder_id = new ArrayList<>();
        InventorySingleOrder_Name = new ArrayList<>();
        InventorySingleOrder_Image = new ArrayList<>();
        InventorySingleOrder_Price = new ArrayList<>();
        InventorySingleOrder_Quantity = new ArrayList<>();
        InventorySingleOrder_DeliveryCharge = new ArrayList<>();
        InventorySingleOrder_DeliveryDate = new ArrayList<>();

        mycartlist_recylerView.setHasFixedSize(true);

        mLayoutManager = new LinearLayoutManager(this);
        mycartlist_recylerView.setLayoutManager(mLayoutManager);
        
        


    }



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
        mCPB_inventory_single_order.setIndeterminateDrawable(circularProgressDrawable = b.build());

        // /!\ Terrible hack, do not do this at home!
        circularProgressDrawable.setBounds(0,
                0,
                mCPB_inventory_single_order.getWidth(),
                mCPB_inventory_single_order.getHeight());
        mCPB_inventory_single_order.setVisibility(View.INVISIBLE);
        mCPB_inventory_single_order.setVisibility(View.VISIBLE);
    }
    /*progressbar data (End)*/

    private class EqualSpaceItemDecoration extends RecyclerView.ItemDecoration {

        private final int mSpaceHeight;

        public EqualSpaceItemDecoration(int mSpaceHeight) {
            this.mSpaceHeight = mSpaceHeight;
        }

        @Override
        public void getItemOffsets(Rect outRect, View view, RecyclerView parent,
                                   RecyclerView.State state) {
            outRect.bottom = mSpaceHeight;
            outRect.top = mSpaceHeight;
        }

    }

}
