package com.ritesh.sevilla;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Rect;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.util.Base64;
import android.util.Log;
import android.view.HapticFeedbackConstants;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;
import com.nispok.snackbar.Snackbar;
import com.nispok.snackbar.SnackbarManager;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;
import com.ritesh.sevilla.Constant.Appconstant;
import com.ritesh.sevilla.Constant.Utils;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import fr.castorflex.android.circularprogressbar.CircularProgressBar;
import fr.castorflex.android.circularprogressbar.CircularProgressDrawable;

/**
 * Created by ritesh on 22/2/17.
 */
@SuppressWarnings("deprecation")
public class SubCategoryActivity extends AppCompatActivity {


    @BindView(R.id.toolbar_sub_category)
    Toolbar toolbar_sub_category;

    @BindView(R.id.toolbar_text_sub_category)
    TextView toolbar_tv_sub_category;

    /*recycler data (Start 01 of 03)*/
    String
            User_ID = "",
            Str_Get_CartCount_Shared = "",
            Str_Get_Cart_Detail_Status = "",
            Str_Get_Cart_result = "",
            Str_Get_Status = "",
            Str_Get_Cart_Deatil_User_ID = "",
            Str_Get_Cart_Product_count = "",
            StatusSubCategory = "",
            SubCategoryID = "",
            SubCategoryName = "",
            Sub_Catagories = "",
            Sub_Catagories_result = "",
            Sub_Category_Id = "",
            Main_Category_Id_Recived = "",
    //            Sub_Category_Name = "",
    Sub_Category_Name_decoded = "",
            Obj_decoded = "",
            Sub_Category_Img_Src = "",
            Sub_Category_Name_for_product_list = "";


    List<SubCategoryActivity> Sub_Category_rowItems;
    private ArrayList<String> Sub_cat_images;
    private ArrayList<String> Sub_cat_names;
    private ArrayList<String> Sub_cat_id;
    List<BeanSubCategory> beanSubCategories0;


    @BindView(R.id.rl_cart_icon_sub_category)
    RelativeLayout Rl_cart_icon_sub_category;

    @BindView(R.id.rl_badgeview_cart_item_sub_category)
    RelativeLayout RL_badgeview_cart_item_sub_category;


    @BindView(R.id.rl_cart_icon_sub_category_click)
    RelativeLayout Rl_cart_icon_sub_category_click;

    @BindView(R.id.rl_badgeview_cart_item_sub_category_click)
    RelativeLayout RL_badgeview_cart_item_sub_category_click;

    @BindView(R.id.tv_badge_counter_sub_category)
    TextView TV_badge_counter_sub_category;




    @BindView(R.id.rl_gridview_sub_category_progress)
    RelativeLayout pv_gridview_sub_cat_progressview;

    CircularProgressBar mCircularProgressBarGridview_sub_cat;

    @BindView(R.id.recycler_view_sub_category)
    RecyclerView sub_category_recylerView;
    /*recycler data (End 01 of 03)*/


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sub_category);
        ButterKnife.bind(this);

        Appconstant.sh = getSharedPreferences(Appconstant.MyPREFERENCES, Context.MODE_PRIVATE);
        User_ID = Appconstant.sh.getString("id", null);
        Log.e("User_ID from SharedPref :", "" + User_ID);
        Str_Get_CartCount_Shared = Appconstant.sh.getString("cart_count", null);
        Log.e("Cart Count From Shared Preference:", "" + Str_Get_CartCount_Shared);


//        SubCategoryID = getIntent().getExtras().getString("SubCatID");
        SubCategoryID = getIntent().getStringExtra("SubCatID");
        Main_Category_Id_Recived = getIntent().getStringExtra("MainCatID");
        SubCategoryName = getIntent().getStringExtra("SubCatName");
        Log.e("Recieved SubCategoryID from MainCategory :", "" + SubCategoryID);
        Log.e("Recieved Main_Category_Id_Recived from MainCategory :", "" + Main_Category_Id_Recived);
        Log.e("Recieved SubCategoryName from MainCategory :", "" + SubCategoryName);

        toolbar_tv_sub_category.setText(SubCategoryName);


        if (Utils.isConnected(getApplicationContext())) {
            Log.e("SubCategoryCartDetailJsontask Call :", "OK");
            SubCategoryCartDetailJsontask task = new SubCategoryCartDetailJsontask();
            task.execute();
        } else {

            SnackbarManager.show(
                    Snackbar.with(SubCategoryActivity.this)
                            .position(Snackbar.SnackbarPosition.TOP)
                            .margin(15, 15)
                            .backgroundDrawable(R.drawable.snackbar_custom_layout)
                            .text("Please Your Internet Connectivity..!!"));

        }


        /*recycler data (Start 02 of 03)*/
        /*image settin by universal image loader */
        DisplayImageOptions defaultOptionss = new DisplayImageOptions.Builder()
                .cacheInMemory(true)
                .cacheOnDisk(true)
                .build();
        ImageLoaderConfiguration configs = new ImageLoaderConfiguration
                .Builder(SubCategoryActivity.this)
                .defaultDisplayImageOptions(defaultOptionss)
                .build();
        ImageLoader.getInstance().init(configs);
        /*image settin by universal image loader */

         /*circular progress bar (Start)*/
        mCircularProgressBarGridview_sub_cat = (CircularProgressBar) findViewById(R.id.gridview_sub_category_progressbar_circular);
//        signupProgress.setVisibility(View.GONE);
        ((CircularProgressDrawable) mCircularProgressBarGridview_sub_cat.getIndeterminateDrawable()).start();
        updateValues();
        /*circular progress bar (End)*/

        setSupportActionBar(toolbar_sub_category);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

//        toolbar_sub_category.setNavigationIcon(R.drawable.ic_back_arrow); // your drawable

        toolbar_sub_category.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed(); // Implemented by activity
            }
        });





        Rl_cart_icon_sub_category.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {

                if (event.getAction() == MotionEvent.ACTION_DOWN) {

                    Log.e("Action ", "Down");
                    Rl_cart_icon_sub_category_click.setVisibility(View.VISIBLE);
                    Rl_cart_icon_sub_category.setVisibility(View.GONE);

                    RL_badgeview_cart_item_sub_category_click.setVisibility(View.VISIBLE);
                    RL_badgeview_cart_item_sub_category.setVisibility(View.GONE);
                    v.performHapticFeedback(HapticFeedbackConstants.VIRTUAL_KEY);

//                    Toast.makeText(getApplicationContext(), "Add to cart Clicked", Toast.LENGTH_SHORT).show();
                    return true;
                }

                if (event.getAction() == MotionEvent.ACTION_MOVE) {

                    Log.e("Action ", "Move");
                    Rl_cart_icon_sub_category_click.setVisibility(View.VISIBLE);
                    Rl_cart_icon_sub_category.setVisibility(View.GONE);

                    RL_badgeview_cart_item_sub_category_click.setVisibility(View.VISIBLE);
                    RL_badgeview_cart_item_sub_category.setVisibility(View.GONE);

                    return true;

                }
                if (event.getAction() == MotionEvent.ACTION_UP) {

                    Log.e("Action ", "Up");

                    Rl_cart_icon_sub_category_click.setVisibility(View.GONE);
                    Rl_cart_icon_sub_category.setVisibility(View.VISIBLE);

                    RL_badgeview_cart_item_sub_category_click.setVisibility(View.GONE);
                    RL_badgeview_cart_item_sub_category.setVisibility(View.VISIBLE);


                    Intent MyCartPage = new Intent(getApplicationContext(), MyCart.class);
                    SubCategoryActivity.this.startActivity(MyCartPage);

                    return true;
                }


                return false;
            }
        });





        Sub_Category_rowItems = new ArrayList<SubCategoryActivity>();
        Sub_cat_images = new ArrayList<>();
        Sub_cat_names = new ArrayList<>();
        Sub_cat_id = new ArrayList<>();


        RecyclerView.LayoutManager mLayoutManager = new GridLayoutManager(this, 2);
        sub_category_recylerView.setLayoutManager(mLayoutManager);
        sub_category_recylerView.addItemDecoration(new EqualSpaceItemDecoration(5));


        if (Utils.isConnected(SubCategoryActivity.this)) {
            SubCategoryCategoryJsontask task = new SubCategoryCategoryJsontask();
            task.execute();
        } else {

            SnackbarManager.show(
                    Snackbar.with(SubCategoryActivity.this)
                            .position(Snackbar.SnackbarPosition.TOP)
                            .margin(15, 15)
                            .backgroundDrawable(R.drawable.snackbar_custom_layout)
                            .text("Please Your Internet Connectivity..!!"));

        }

    }
    /*recycler data (End 02 of 03)*/


    @Override
    protected void onStart() {
        super.onStart();
        Log.e("SubCategoryActivity lifecycle", "onStart invoked");
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.e("SubCategoryActivity lifecycle", "onResume invoked");
//        TV_badge_counter_sub_category.setText(Str_Get_CartCount_Shared);
        /*if (Utils.isConnected(getApplicationContext())) {
            Log.e("onResume SubCategoryCartDetailJsontask Call :", "OK");
            SubCategoryCartDetailJsontask task = new SubCategoryCartDetailJsontask();
            task.execute();
        } else {

            SnackbarManager.show(
                    Snackbar.with(SubCategoryActivity.this)
                            .position(Snackbar.SnackbarPosition.TOP)
                            .margin(15, 15)
                            .backgroundDrawable(R.drawable.snackbar_custom_layout)
                            .text("Please Your Internet Connectivity..!!"));

        }*/
    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.e("SubCategoryActivity lifecycle", "onPause invoked");
    }

    @Override
    protected void onStop() {
        super.onStop();
        Log.e("SubCategoryActivity lifecycle", "onStop invoked");
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        Log.e("SubCategoryActivity lifecycle", "onRestart invoked");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.e("SubCategoryActivity lifecycle", "onDestroy invoked");
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
        mCircularProgressBarGridview_sub_cat.setIndeterminateDrawable(circularProgressDrawable = b.build());

        // /!\ Terrible hack, do not do this at home!
        circularProgressDrawable.setBounds(0, 0,
                mCircularProgressBarGridview_sub_cat.getWidth(),
                mCircularProgressBarGridview_sub_cat.getHeight());
        mCircularProgressBarGridview_sub_cat.setVisibility(View.INVISIBLE);
        mCircularProgressBarGridview_sub_cat.setVisibility(View.VISIBLE);
    }
    /*progressbar data (End)*/


    private class SubCategoryCategoryJsontask extends AsyncTask<String, Void, List<BeanSubCategory>> {

        boolean iserror = false;
        String result = " ";

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pv_gridview_sub_cat_progressview.setVisibility(View.VISIBLE);
            Log.e(" ************ UserListData AsyncTask Start ************ :", "yes");
            Log.e("Sub_Catagories ID:", "" + SubCategoryID);
        }

        @Override
        protected List<BeanSubCategory> doInBackground(String... params) {
            HttpClient client = new DefaultHttpClient();
            HttpPost post = new HttpPost("http://sevilla.centrocomercial.com.es/wp-content/plugins/webserv/sub_category.php?cate_id=" + SubCategoryID);

            try {
                Log.e(" ****** URL :", " http://sevilla.centrocomercial.com.es/wp-content/plugins/webserv/sub_category.php?cate_id=" + SubCategoryID);

                HttpResponse response = client.execute(post);
                String objSubCategory = EntityUtils.toString(response.getEntity());

                beanSubCategories0 = new ArrayList<>();

                JSONObject jsonObject = new JSONObject(objSubCategory);
                StatusSubCategory = jsonObject.getString("status");
                Sub_Catagories = jsonObject.getString("Product_sub_catagories");

                Log.e("************Json Product_sub_catagories data*******************", " " + Sub_Catagories);

                if (StatusSubCategory.equals("OK")) {

                    Log.e("doInBackground StatusSubCategory is", "OK");

                    JSONArray jaaray = new JSONArray(Sub_Catagories);
                    for (int i = 0; i < jaaray.length(); i++) {
                        Sub_Catagories_result = jaaray.getJSONObject(i).getString("sub_category_result");

                        Log.e("Sub_Catagories_result :", "" + Sub_Catagories_result);

                        if (Sub_Catagories_result.equalsIgnoreCase("Successful")) {

                            Log.e("doInBackground Sub_Catagories_result is", "Successful");


                            BeanSubCategory beanSubCategory = new BeanSubCategory();
                            beanSubCategory.setId(jaaray.getJSONObject(i).getString("sub_category_id"));
                            byte[] data = Base64.decode(jaaray.getJSONObject(i).getString("sub_category_name"), Base64.DEFAULT);
                            Sub_Category_Name_decoded = new String(data, "UTF-8");

                            Log.e("sub_category_name Decoded :", "" + Sub_Category_Name_decoded);

                            beanSubCategory.setsubCatTittle(Sub_Category_Name_decoded);

                            beanSubCategory.setImage(jaaray.getJSONObject(i).getString("img_src"));

                            beanSubCategories0.add(beanSubCategory);

                            Sub_Category_Id = jaaray.getJSONObject(i).getString("sub_category_id");
//                        Sub_Category_Name = jaaray.getJSONObject(i).getString("sub_category_name");
                            Sub_Category_Img_Src = jaaray.getJSONObject(i).getString("img_src");


//                        Sub_Category_Name_decoded = URLDecoder.decode(Sub_Category_Name, "UTF-8");


                            if (Sub_Category_Name_decoded != null && !Sub_Category_Name_decoded.isEmpty()) {
                                Sub_cat_names.add(Sub_Category_Name_decoded);
                                Log.e(" ********** Sub_cat_names **********", "" + Sub_Category_Name_decoded);

                            } else {
                                Sub_Category_Name_decoded = "Empty";
                            }

                            if (Sub_cat_images.contains("no_img.jpg*")) {

                            /*no_img.jpg*/
                                Log.e("no_img :", "ok");

                            } else {
                                Sub_cat_images.add(Sub_Category_Img_Src);
                                Log.e(" ********** Sub_cat_images **********", "" + Sub_cat_images);
                            }


                            Sub_cat_id.add(Sub_Category_Id);
                            Log.e(" ********** Sub_cat_id **********", "" + Sub_cat_id);


                            String subcat_idlist = Sub_cat_id.get(i);
                            Log.e(" ********** Sub_cat_idlist **********", "" + subcat_idlist);

                            String subcat_imageslist = Sub_cat_images.get(i);
                            Log.e(" ********** Sub_cat_imageslist **********", "" + subcat_imageslist);

                            String subcat_nameslist = Sub_cat_names.get(i);
                            Log.e(" ********** Sub_cat_nameslist **********", "" + subcat_nameslist);
                        } else {
                            Log.e("No Data Found :", "OOppss");
                        }
                    }
                } else if (!StatusSubCategory.equals("OK")) {
                    Log.e("No Data Found", "OK");
                }


            } catch (Exception e) {
                System.out.println("Errror in gettting by ID" + e);
                Log.e("22", "22" + e.getMessage());
                e.printStackTrace();
                iserror = true;
            }
            return beanSubCategories0;
        }

        @Override
        protected void onPostExecute(List<BeanSubCategory> mySubcatstring) {
            super.onPostExecute(mySubcatstring);
            pv_gridview_sub_cat_progressview.setVisibility(View.GONE);

            if (StatusSubCategory.equals("OK")) {

                Log.e("onPostExecute StatusSubCategory is", "OK");

                if (Sub_Catagories_result.equals("Successful")) {

                    Log.e("onPostExecute Sub_Catagories_result is", "Successful");

                    if (Sub_cat_id.size() > 0) {
                        Log.e("onPostExecute Sub_cat_id size is not Zero ", "wow");
                        Log.e(" ********** Sub_cat_id ********** ", "" + Sub_cat_id);

                        SubCategoryAdapter subCategoryAdapter = new SubCategoryAdapter(SubCategoryActivity.this, mySubcatstring);
                        sub_category_recylerView.setAdapter(subCategoryAdapter);


                        Log.e(" ********** sub_category_.size() > 0 **********", "YES");
                        Log.e(" ********** sub_category_.size() > 0 **********", "YES");
                        Log.e(" ********** sub_category_.size() > 0 **********", "YES");
                    } else {
                        Log.e("onPostExecute Sub_cat_id size is Zero ", "ooppss");
                        SnackbarManager.show(
                                Snackbar.with(SubCategoryActivity.this)
                                        .position(Snackbar.SnackbarPosition.TOP)
                                        .margin(15, 15)
                                        .backgroundDrawable(R.drawable.snackbar_custom_layout)
                                        .text("No Data Found"));
                    }

                } else {
                    SnackbarManager.show(
                            Snackbar.with(SubCategoryActivity.this)
                                    .position(Snackbar.SnackbarPosition.TOP)
                                    .margin(15, 15)
                                    .backgroundDrawable(R.drawable.snackbar_custom_layout)
                                    .text("No Data Found"));
                }


            } else if (!StatusSubCategory.equals("OK")) {
                Log.e("onPostExecute StatusSubCategory is Not Equal to :", "OK");
                SnackbarManager.show(
                        Snackbar.with(SubCategoryActivity.this)
                                .position(Snackbar.SnackbarPosition.TOP)
                                .margin(15, 15)
                                .backgroundDrawable(R.drawable.snackbar_custom_layout)
                                .text("No Data Found"));
            }
        }

    }



    private class SubCategoryCartDetailJsontask extends AsyncTask<String, Void, String> {

        boolean iserror = false;

        @Override
        protected void onPreExecute() {
            // TODO Auto-generated method stub
            //  loginprogressbar.setVisibility(View.VISIBLE);
            Log.e("******* NOW SubCategoryCartDetailJsontask WEB SERVICE IS RUNNING *******", "YES");
            Log.e("******* NOW SubCategoryCartDetailJsontask WEB SERVICE IS RUNNING *******", "YES");
            Log.e("User_ID onPreExecute :", "" + User_ID);

        }

        @Override
        protected String doInBackground(String... params) {
            Log.e("******* NOW SubCategoryCartDetailJsontask IS doInBackground RUNNING *******", "YES");
            HttpClient client = new DefaultHttpClient();
            HttpPost post = new HttpPost("http://sevilla.centrocomercial.com.es/wp-content/plugins/webserv/get_cart_product.php?user_id="+User_ID);

            /*http://sevilla.centrocomercial.com.es/wp-content/plugins/webserv/get_cart_product.php?user_id=286*/
            Log.e("URL Cart Detail SubCategory :", "" + "http://sevilla.centrocomercial.com.es/wp-content/plugins/webserv/get_cart_product.php?user_id="+User_ID);

            try {
                HttpResponse response = client.execute(post);
                String CartDetailobject = EntityUtils.toString(response.getEntity());
                Log.e("*******Cart Detail object******** :", "" + CartDetailobject);

                //JSONArray js = new JSONArray(object);
                JSONObject jobect = new JSONObject(CartDetailobject);
                Str_Get_Cart_Detail_Status = jobect.getString("status");
                if (Str_Get_Cart_Detail_Status.equalsIgnoreCase("OK")) {
                    Str_Get_Cart_Deatil_User_ID = jobect.getString("user_id");
                    Str_Get_Cart_Product_count = jobect.getString("no_of_prodcut");
                    Str_Get_Cart_result = jobect.getString("single_product_result");

                }

            } catch (Exception e) {
                Log.v("22", "22" + e.getMessage());
                e.printStackTrace();
                iserror = true;
            }

            return Str_Get_Cart_Detail_Status;
        }

        @Override
        protected void onPostExecute(String result1) {
            // TODO Auto-generated method stub
            super.onPostExecute(result1);

            if (!iserror) {
                if (Str_Get_Cart_Detail_Status.equalsIgnoreCase("OK")) {

                    Log.e("Str_Get_Cart_Deatil_User_ID :", "" + Str_Get_Cart_Deatil_User_ID);
                    Log.e("Str_Get_Cart_Product_count :", "" + Str_Get_Cart_Product_count);
                    Log.e("Str_Get_Cart_result :", "" + Str_Get_Cart_result);

                    TV_badge_counter_sub_category.setText(Str_Get_Cart_Product_count);
                    /**************** Start Animation **************  **/
                    YoYo.with(Techniques.Wobble)
                            .duration(700)
                            .playOn(Rl_cart_icon_sub_category);
                    YoYo.with(Techniques.Wobble)
                            .duration(700)
                            .playOn(RL_badgeview_cart_item_sub_category);
                    /**************** End Animation ****************/

                } else {
                    Log.e("onPostExecute Error ", "ooppss");
                    SnackbarManager.show(
                            Snackbar.with(SubCategoryActivity.this)
                                    .position(Snackbar.SnackbarPosition.TOP)
                                    .margin(15, 15)
                                    .backgroundDrawable(R.drawable.snackbar_custom_layout)
                                    .text("No Data Found"));
                }
            }
        }

    }


    private class SubCategoryAdapter extends RecyclerView.Adapter<SubCategoryActivity.SubCategoryAdapter.MyViewHolder> {

        private Context mContext;
        private List<BeanSubCategory> subCatarrayList;


        class MyViewHolder extends RecyclerView.ViewHolder {
            TextView subCatTittle;
            ImageView SubCatimage;
            RelativeLayout SubRl_loader;
            CircularProgressBar SubRl_circular_loader;

            public MyViewHolder(View view) {
                super(view);
                subCatTittle = (TextView) view.findViewById(R.id.tv_sub_category_tittle_change);
                SubRl_circular_loader = (CircularProgressBar) view.findViewById(R.id.grid_sub_category_item_progressbar_circular);
                SubRl_loader = (RelativeLayout) view.findViewById(R.id.rl_sub_category_loader);
                SubCatimage = (ImageView) view.findViewById(R.id.iv_sub_category_image);

            }
        }


        public SubCategoryAdapter(Context mContext, List<BeanSubCategory> subCatarrayList) {
            this.mContext = mContext;
            this.subCatarrayList = subCatarrayList;
        }


        @Override
        public MyViewHolder onCreateViewHolder
                (ViewGroup parent, int viewType) {
            View itemView = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.activity_sub_category_grid_items, parent, false);

            return new MyViewHolder(itemView);
        }


        @Override
        public void onBindViewHolder(final MyViewHolder holder, final int position) {

            holder.subCatTittle.setText(Html.fromHtml(subCatarrayList.get(position).getsubCatTittle()));

            ImageLoader imageLoader = ImageLoader.getInstance();
            imageLoader.getInstance().displayImage(subCatarrayList.get(position).getImage(), holder.SubCatimage, new ImageLoadingListener() {
                @Override
                public void onLoadingStarted(String imageUri, View view) {
                    holder.SubRl_loader.setVisibility(View.VISIBLE);
                }

                @Override
                public void onLoadingFailed(String imageUri, View view, FailReason failReason) {
                    holder.SubRl_loader.setVisibility(View.GONE);

                    Log.e("image not found :", "ok");
                }

                @Override
                public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {
                    holder.SubRl_loader.setVisibility(View.GONE);
                }

                @Override
                public void onLoadingCancelled(String imageUri, View view) {
                    holder.SubRl_loader.setVisibility(View.GONE);

                }
            });


            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int position = holder.getAdapterPosition();

//                    holder.Catimage_transparent_overlay.setVisibility(View.GONE);
                    Log.e(" Item Position :", "" + subCatarrayList.get(holder.getLayoutPosition()));

                    Log.e(" Adapter Position :", "" + position);

                    Log.e("Item id", "" + subCatarrayList.get(position).getId());
                    Sub_Category_Name_for_product_list = String.valueOf(Html.fromHtml(subCatarrayList.get(position).getsubCatTittle()));

                    Log.e("Item Name", "" + String.valueOf(Html.fromHtml(subCatarrayList.get(position).getsubCatTittle())));

                    Log.e("Item Name Category_Name_for_Sub_Category", "" + Sub_Category_Name_for_product_list);

                    Log.e(" List Size :", "" + subCatarrayList.size());

                    Log.e("MainCategory_ID Send for SubCategoryItemList:", "" + Main_Category_Id_Recived);


                    Intent SubCatPage = new Intent(getApplicationContext(), SubCategoryProductListActivity.class);
                    SubCatPage.putExtra("SubCatID", subCatarrayList.get(position).getId());
                    SubCatPage.putExtra("MainCategoryID", Main_Category_Id_Recived);
                    SubCatPage.putExtra("SubCatName", Sub_Category_Name_for_product_list);
//                    SubCatPage.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                    SubCategoryActivity.this.startActivity(SubCatPage);
//                    finish();


                }
            });

        }

        @Override
        public int getItemCount() {
            return subCatarrayList.size();
        }


    }


    /*this is used to set the equal space between grid items (Start)*/
                                    /*    01     */
    public class SpacesItemDecoration extends RecyclerView.ItemDecoration {
        private int space;

        public SpacesItemDecoration(int space) {
            this.space = space;
        }

        @Override
        public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
            outRect.left = space;
            outRect.right = space;
            outRect.bottom = space;

            // Add top margin only for the first item to avoid double space between items
            if (parent.getChildAdapterPosition(view) == 0)
                outRect.top = space;
        }
    }
    /*this is used to set the equal space between grid items (End)*/


    /*this is used to set the equal space between grid items (Start)*/
                                        /*    02     */
    public class EqualSpaceItemDecoration extends RecyclerView.ItemDecoration {

        private final int mSpaceHeight;

        public EqualSpaceItemDecoration(int mSpaceHeight) {
            this.mSpaceHeight = mSpaceHeight;
        }

        @Override
        public void getItemOffsets(Rect outRect, View view, RecyclerView parent,
                                   RecyclerView.State state) {
            outRect.bottom = mSpaceHeight;
            outRect.top = mSpaceHeight;
            outRect.left = mSpaceHeight;
            outRect.right = mSpaceHeight;
        }

    }
    /*this is used to set the equal space between grid items (End)*/


    /*this is used for only set vertical space on recycler gridview items (Start)*/
                                        /*    03     */
    public class VerticalSpaceItemDecoration extends RecyclerView.ItemDecoration {

        private final int verticalSpaceHeight;

        public VerticalSpaceItemDecoration(int verticalSpaceHeight) {
            this.verticalSpaceHeight = verticalSpaceHeight;
        }

        @Override
        public void getItemOffsets(Rect outRect, View view, RecyclerView parent,
                                   RecyclerView.State state) {
            outRect.bottom = verticalSpaceHeight;
        }
    }
    /*this is used for only set vertical space on recycler gridview items (End)*/

    @Override
    public void onBackPressed() {

        finish();
    }



}

