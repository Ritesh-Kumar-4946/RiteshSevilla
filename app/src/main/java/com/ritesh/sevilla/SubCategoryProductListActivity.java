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
import android.util.Log;
import android.view.HapticFeedbackConstants;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

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
 * Created by ritesh on 23/2/17.
 */
@SuppressWarnings("deprecation")
public class SubCategoryProductListActivity extends AppCompatActivity {

    @BindView(R.id.toolbar_sub_category_product)
    Toolbar toolbar_sub_category_product;

    @BindView(R.id.toolbar_text_sub_category_product)
    TextView toolbar_tv_sub_category_product;

    /*recycler data (Start 01 of 03)*/
    String
            User_ID = "",
            Str_Get_CartCount_Shared = "",
            Str_Get_Cart_Detail_Status = "",
            Str_Get_Cart_result = "",
            Str_Get_Status = "",
            Str_Get_Cart_Deatil_User_ID = "",
            Str_Get_Cart_Product_count = "",

            StatusSubCategoryProductlist = "",
            SubCategoryID = "",
            SubCategoryProductCount = "",
            MainCategory_Id_Recived = "",
            SubCategoryName = "",
            Sub_Catagories_Product = "",
            Sub_Catagories_Product_result = "",
            Sub_Category_Product_Id = "",
            Sub_Category_Product_Name = "",
            Sub_Category_Product_Price = "",
            Product_Name_For_Buy = "",
            Sub_Category_Product_Img_Src = "";



    @BindView(R.id.rl_cart_icon_sub_category_product_list)
    RelativeLayout Rl_cart_icon_sub_category_product_list;

    @BindView(R.id.rl_badgeview_cart_item_sub_category_product_list)
    RelativeLayout RL_badgeview_cart_item_sub_category_product_list;



    @BindView(R.id.rl_cart_icon_sub_category_product_list_click)
    RelativeLayout Rl_cart_icon_sub_category_product_list_click;

    @BindView(R.id.rl_badgeview_cart_item_sub_category_product_list_click)
    RelativeLayout RL_badgeview_cart_item_sub_category_product_list_click;

    @BindView(R.id.tv_badge_counter_sub_category_product_list)
    TextView TV_badge_counter_sub_category_product_list;





    List<SubCategoryProductListActivity> Sub_Category_Pro_rowItems;
    private ArrayList<String> Sub_cat_pro_images;
    private ArrayList<String> Sub_cat_pro_names;
    private ArrayList<String> Sub_cat_pro_price;
    private ArrayList<String> Sub_cat_pro_id;
    List<BeanSubCategoryProduct> beanSubCategoriesPro0;


    @BindView(R.id.rl_gridview_sub_category_product_progress)
    RelativeLayout pv_gridview_sub_cat_pro_progressview;

    CircularProgressBar mCircularProgressBarGridview_sub_cat_pro;

    @BindView(R.id.recycler_view_sub_category_product)
    RecyclerView sub_category_pro_recylerView;
    /*recycler data (End 01 of 03)*/


//    @BindView(R.id.rl_sub_category_item_cart)


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sub_category_product_list);
        ButterKnife.bind(this);

        Appconstant.sh = getSharedPreferences(Appconstant.MyPREFERENCES, Context.MODE_PRIVATE);
        User_ID = Appconstant.sh.getString("id", null);
        Log.e("User_ID from SharedPref :", "" + User_ID);
        Str_Get_CartCount_Shared = Appconstant.sh.getString("cart_count", null);
        Log.e("Cart Count From Shared Preference:", "" + Str_Get_CartCount_Shared);


//        SubCategoryID = getIntent().getExtras().getString("SubCatID");
        SubCategoryID = getIntent().getStringExtra("SubCatID");
        MainCategory_Id_Recived = getIntent().getStringExtra("MainCategoryID");
        SubCategoryName = getIntent().getStringExtra("SubCatName");
        Log.e("Recieved SubCategoryID from MainCategory :", "" + SubCategoryID);
        Log.e("Recieved MainCategory_Id_Recived from SubCategoryActivity :", "" + MainCategory_Id_Recived);
        Log.e("Recieved SubCategoryName from MainCategory :", "" + SubCategoryName);

        toolbar_tv_sub_category_product.setText(SubCategoryName);

        if (Utils.isConnected(getApplicationContext())) {
            Log.e("SubCategoryProductListDetailJsontask Call :", "OK");
            SubCategoryProductListCartDetailJsontask task = new SubCategoryProductListCartDetailJsontask();
            task.execute();
        } else {

            SnackbarManager.show(
                    Snackbar.with(SubCategoryProductListActivity.this)
                            .position(Snackbar.SnackbarPosition.TOP)
                            .margin(15, 15)
                            .backgroundDrawable(R.drawable.snackbar_custom_layout)
                            .text("Please Your Internet Connectivity..!!"));

        }




        /*recycler data (Start 02 of 03)*/
        /*image settin by universal image loader */
        DisplayImageOptions defaultOptions = new DisplayImageOptions.Builder()
                .cacheInMemory(true)
                .cacheOnDisk(true)
                .build();
        ImageLoaderConfiguration config = new ImageLoaderConfiguration
                .Builder(SubCategoryProductListActivity.this)
                .defaultDisplayImageOptions(defaultOptions)
                .build();
        ImageLoader.getInstance().init(config);
        /*image settin by universal image loader */

         /*circular progress bar (Start)*/
        mCircularProgressBarGridview_sub_cat_pro = (CircularProgressBar) findViewById(R.id.gridview_sub_category_product_progressbar_circular);
//        signupProgress.setVisibility(View.GONE);
        ((CircularProgressDrawable) mCircularProgressBarGridview_sub_cat_pro.getIndeterminateDrawable()).start();
        updateValues();
        /*circular progress bar (End)*/

        setSupportActionBar(toolbar_sub_category_product);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

//        toolbar_sub_category.setNavigationIcon(R.drawable.ic_back_arrow); // your drawable

        toolbar_sub_category_product.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed(); // Implemented by activity
            }
        });

        Rl_cart_icon_sub_category_product_list.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {

                if (event.getAction() == MotionEvent.ACTION_DOWN) {

                    Log.e("Action ", "Down");
                    Rl_cart_icon_sub_category_product_list_click.setVisibility(View.VISIBLE);
                    Rl_cart_icon_sub_category_product_list.setVisibility(View.GONE);

                    RL_badgeview_cart_item_sub_category_product_list_click.setVisibility(View.VISIBLE);
                    RL_badgeview_cart_item_sub_category_product_list.setVisibility(View.GONE);
                    v.performHapticFeedback(HapticFeedbackConstants.VIRTUAL_KEY);

//                    Toast.makeText(getApplicationContext(), "Add to cart Clicked", Toast.LENGTH_SHORT).show();
                    return true;
                }

                if (event.getAction() == MotionEvent.ACTION_MOVE) {

                    Log.e("Action ", "Move");
                    Rl_cart_icon_sub_category_product_list_click.setVisibility(View.VISIBLE);
                    Rl_cart_icon_sub_category_product_list.setVisibility(View.GONE);

                    RL_badgeview_cart_item_sub_category_product_list_click.setVisibility(View.VISIBLE);
                    RL_badgeview_cart_item_sub_category_product_list.setVisibility(View.GONE);

                    return true;

                }
                if (event.getAction() == MotionEvent.ACTION_UP) {

                    Log.e("Action ", "Up");

                    Rl_cart_icon_sub_category_product_list_click.setVisibility(View.GONE);
                    Rl_cart_icon_sub_category_product_list.setVisibility(View.VISIBLE);

                    RL_badgeview_cart_item_sub_category_product_list_click.setVisibility(View.GONE);
                    RL_badgeview_cart_item_sub_category_product_list.setVisibility(View.VISIBLE);


                    Intent MyCartPage = new Intent(getApplicationContext(), MyCart.class);
                    SubCategoryProductListActivity.this.startActivity(MyCartPage);

                    return true;
                }


                return false;
            }
        });


        Sub_Category_Pro_rowItems = new ArrayList<SubCategoryProductListActivity>();
        Sub_cat_pro_images = new ArrayList<>();
        Sub_cat_pro_names = new ArrayList<>();
        Sub_cat_pro_price = new ArrayList<>();
        Sub_cat_pro_id = new ArrayList<>();


        RecyclerView.LayoutManager mLayoutManager = new GridLayoutManager(this, 2);
        sub_category_pro_recylerView.setLayoutManager(mLayoutManager);
        sub_category_pro_recylerView.addItemDecoration(new EqualSpaceItemDecoration(5));


        if (Utils.isConnected(getApplicationContext())) {
            SubCategoryProductJsontask task = new SubCategoryProductJsontask();
            task.execute();
        } else {

            SnackbarManager.show(
                    Snackbar.with(SubCategoryProductListActivity.this)
                            .position(Snackbar.SnackbarPosition.TOP)
                            .margin(15, 15)
                            .backgroundDrawable(R.drawable.snackbar_custom_layout)
                            .text("Please Your Internet Connectivity..!!"));

        }


       /* sub_category_pro_recylerView.addOnItemTouchListener(
                new RecyclerItemClickListener(this, sub_category_pro_recylerView ,new RecyclerItemClickListener.OnItemClickListener() {
                    @Override public void onItemClick(View view, int position) {
                        // do whatever
                        Log.e("item clicked :", "" + sub_category_pro_recylerView.getAdapter().getItemViewType(position));
                    }

                    @Override public void onLongItemClick(View view, int position) {
                        // do whatever
                    }
                })
        );
*/
    }



    @Override
    protected void onStart() {
        super.onStart();
        Log.e("SubCategoryProductListActivity lifecycle", "onStart invoked");
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.e("onResume SubCategoryProductListActivity lifecycle", "onResume invoked");

        if (Utils.isConnected(getApplicationContext())) {
            Log.e("SubCategoryProductListDetailJsontask Call :", "OK");
            SubCategoryProductListCartDetailJsontask task = new SubCategoryProductListCartDetailJsontask();
            task.execute();
        } else {

            SnackbarManager.show(
                    Snackbar.with(SubCategoryProductListActivity.this)
                            .position(Snackbar.SnackbarPosition.TOP)
                            .margin(15, 15)
                            .backgroundDrawable(R.drawable.snackbar_custom_layout)
                            .text("Please Your Internet Connectivity..!!"));

        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.e("SubCategoryProductListActivity lifecycle", "onPause invoked");
    }

    @Override
    protected void onStop() {
        super.onStop();
        Log.e("SubCategoryProductListActivity lifecycle", "onStop invoked");
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        Log.e("SubCategoryProductListActivity lifecycle", "onRestart invoked");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.e("SubCategoryProductListActivity lifecycle", "onDestroy invoked");
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
        mCircularProgressBarGridview_sub_cat_pro.setIndeterminateDrawable(circularProgressDrawable = b.build());

        // /!\ Terrible hack, do not do this at home!
        circularProgressDrawable.setBounds(0, 0,
                mCircularProgressBarGridview_sub_cat_pro.getWidth(),
                mCircularProgressBarGridview_sub_cat_pro.getHeight());
        mCircularProgressBarGridview_sub_cat_pro.setVisibility(View.INVISIBLE);
        mCircularProgressBarGridview_sub_cat_pro.setVisibility(View.VISIBLE);
    }
    /*progressbar data (End)*/




    private class SubCategoryProductListCartDetailJsontask extends AsyncTask<String, Void, String> {

        boolean iserror = false;

        @Override
        protected void onPreExecute() {
            // TODO Auto-generated method stub
            //  loginprogressbar.setVisibility(View.VISIBLE);
            Log.e("******* NOW SubCategoryProductListDetailJsontask WEB SERVICE IS RUNNING *******", "YES");
            Log.e("******* NOW SubCategoryProductListDetailJsontask WEB SERVICE IS RUNNING *******", "YES");
            Log.e("User_ID onPreExecute :", "" + User_ID);

        }

        @Override
        protected String doInBackground(String... params) {
            Log.e("******* NOW SubCategoryProductListDetailJsontask IS doInBackground RUNNING *******", "YES");
            HttpClient client = new DefaultHttpClient();
            HttpPost post = new HttpPost("http://sevilla.centrocomercial.com.es/wp-content/plugins/webserv/get_cart_product.php?user_id="+User_ID);

            /*http://sevilla.centrocomercial.com.es/wp-content/plugins/webserv/get_cart_product.php?user_id=286*/
            Log.e("URL Cart Detail SubCategoryProductListDetailJsontask :", "" + "http://sevilla.centrocomercial.com.es/wp-content/plugins/webserv/get_cart_product.php?user_id="+User_ID);

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

                    TV_badge_counter_sub_category_product_list.setText(Str_Get_Cart_Product_count);
                    /**************** Start Animation **************  **/
                    YoYo.with(Techniques.Wobble)
                            .duration(700)
                            .playOn(Rl_cart_icon_sub_category_product_list);
                    YoYo.with(Techniques.Wobble)
                            .duration(700)
                            .playOn(RL_badgeview_cart_item_sub_category_product_list);
                    /**************** End Animation ****************/

                } else {
                    Log.e("onPostExecute Error ", "ooppss");
                    SnackbarManager.show(
                            Snackbar.with(SubCategoryProductListActivity.this)
                                    .position(Snackbar.SnackbarPosition.TOP)
                                    .margin(15, 15)
                                    .backgroundDrawable(R.drawable.snackbar_custom_layout)
                                    .text("No Data Found"));
                }
            }
        }

    }




    public class SubCategoryProductJsontask extends AsyncTask<String, Void, List<BeanSubCategoryProduct>> {

        boolean iserror = false;
        String result = " ";

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pv_gridview_sub_cat_pro_progressview.setVisibility(View.VISIBLE);
            Log.e(" ************ UserListData AsyncTask Start ************ :", "yes");
            Log.e("Sub_Catagories ID:", "" + SubCategoryID);
            Log.e("MainCategory ID:", "" + MainCategory_Id_Recived);
        }

        @Override
        protected List<BeanSubCategoryProduct> doInBackground(String... params) {
            HttpClient client = new DefaultHttpClient();
            HttpPost post = new HttpPost
                    ("http://sevilla.centrocomercial.com.es/wp-content/plugins/webserv/get_product_list.php?" +
                            "par_id=" + MainCategory_Id_Recived + "&subcat_id=" + SubCategoryID);

            try {
                Log.e(" ****** URL ******:", " " +
                        "http://sevilla.centrocomercial.com.es/wp-content/plugins/webserv/get_product_list.php?" +
                        "par_id=" + MainCategory_Id_Recived + "&subcat_id=" + SubCategoryID);

                HttpResponse response = client.execute(post);
                String obj = EntityUtils.toString(response.getEntity());

                beanSubCategoriesPro0 = new ArrayList<>();

                JSONObject jsonObject = new JSONObject(obj);
                StatusSubCategoryProductlist = jsonObject.getString("status");
                if (StatusSubCategoryProductlist.equalsIgnoreCase("OK")) {

                    Log.e(" StatusSubCategoryProductlist :", "OK");

                    SubCategoryProductCount = jsonObject.getString("sub_cat_pro_count");
                    Sub_Catagories_Product = jsonObject.getString("Product_detail");

                    Log.e("****** Json Sub_Catagories_Product data *******", " " + Sub_Catagories_Product);

                    JSONArray jaaray = new JSONArray(Sub_Catagories_Product);
                    for (int i = 0; i <= jaaray.length(); i++) {
                        Sub_Catagories_Product_result = jaaray.getJSONObject(i).getString("product_result");
                        Log.e("Sub_Catagories_result :", "" + Sub_Catagories_Product_result);

                        if (Sub_Catagories_Product_result.equalsIgnoreCase("Successful")) {
                            BeanSubCategoryProduct beanSubCategoryProduct = new BeanSubCategoryProduct();
                            beanSubCategoryProduct.setId(jaaray.getJSONObject(i).getString("product_id"));
                            beanSubCategoryProduct.setsubCatTProductittle(jaaray.getJSONObject(i).getString("product_title"));
                            beanSubCategoryProduct.setsubCatTProducPrice(jaaray.getJSONObject(i).getString("product_price"));
                            beanSubCategoryProduct.setImage(jaaray.getJSONObject(i).getString("product_image"));

                            beanSubCategoriesPro0.add(beanSubCategoryProduct);

                            Sub_Category_Product_Id = jaaray.getJSONObject(i).getString("product_id");
                            Sub_Category_Product_Name = jaaray.getJSONObject(i).getString("product_title");
                            Sub_Category_Product_Price = jaaray.getJSONObject(i).getString("product_price");
                            Sub_Category_Product_Img_Src = jaaray.getJSONObject(i).getString("product_image");

                            Sub_cat_pro_id.add(Sub_Category_Product_Id);
                            Sub_cat_pro_names.add(Sub_Category_Product_Name);
                            Sub_cat_pro_price.add(Sub_Category_Product_Price);
                            Sub_cat_pro_images.add(Sub_Category_Product_Img_Src);

                            Log.e(" Sub_cat_pro_id ", "" + Sub_cat_pro_id);
                            Log.e(" Sub_Category_Product_Name ", "" + Sub_Category_Product_Name);
                            Log.e(" Sub_Category_Product_Price ", "" + Sub_Category_Product_Price);
                            Log.e(" Sub_Category_Product_Img_Src ", "" + Sub_Category_Product_Img_Src);

                            /*if (Sub_Category_Product_Name != null && !Sub_Category_Product_Name.isEmpty()) {
                                Sub_cat_pro_names.add(Sub_Category_Product_Name);
                                Log.e(" Sub_Category_Product_Name ", "" + Sub_Category_Product_Name);

                            } else {
                                Log.e(" Sub_Category_Product_Name ", "Null");
                                Sub_Category_Product_Name = "Empty";
                            }


                            if (Sub_Category_Product_Price != null && !Sub_Category_Product_Price.isEmpty()) {
                                Sub_cat_pro_price.add(Sub_Category_Product_Price);
                                Log.e(" Sub_Category_Product_Price ", "" + Sub_Category_Product_Price);

                            } else {
                                Log.e(" Sub_Category_Product_Price ", "Null");
                                Sub_Category_Product_Price = "Empty";
                            }

                            if (Sub_cat_pro_images.contains("no_img.jpg*")) {

                            *//*no_img.jpg*//*
                                Log.e("no_img Found:", "ok");

                            } else {
                                Sub_cat_pro_images.add(Sub_Category_Product_Img_Src);
                                Log.e(" Sub_Category_Product_Img_Src ", "" + Sub_Category_Product_Img_Src);
                            }*/


                            String subcatPro_idlist = Sub_cat_pro_id.get(i);
                            Log.e(" ********** subcatPro_idlist **********", "" + subcatPro_idlist);

                            String subcatPro_nameslist = Sub_cat_pro_names.get(i);
                            Log.e(" ********** subcatPro_nameslist **********", "" + subcatPro_nameslist);

                            String subcatPro_pricelist = Sub_cat_pro_price.get(i);
                            Log.e(" ********** subcatPro_pricelist **********", "" + subcatPro_pricelist);

                            String subcatPro_imageslist = Sub_cat_pro_images.get(i);
                            Log.e(" ********** subcatPro_imageslist **********", "" + subcatPro_imageslist);
                        }

                    }
                }


            } catch (Exception e) {
                System.out.println("Errror in gettting by ID" + e);
                Log.e("22", "22" + e.getMessage());
                e.printStackTrace();
                iserror = true;
            }

            return beanSubCategoriesPro0;
        }

        @Override
        protected void onPostExecute(List<BeanSubCategoryProduct> mySubcatproductstring) {
            super.onPostExecute(mySubcatproductstring);
            pv_gridview_sub_cat_pro_progressview.setVisibility(View.GONE);

//            if (!iserror) {
                if (Sub_cat_pro_id.size() > 0) {
                    Log.e("onPostExecute Sub_cat_id size is not Zero ", "wow");
                    Log.e(" ********** Sub_cat_id ********** ", "" + Sub_cat_pro_id);

//                    if (Sub_Catagories_Product_result.equals("Successful")) {
//                        Log.e("onPostExecute Sub_Catagories_result is", "Successful");

                        SubCategoryProductAdapter subCategoryProductAdapter = new
                                SubCategoryProductAdapter(SubCategoryProductListActivity.this, mySubcatproductstring);

                        sub_category_pro_recylerView.setAdapter(subCategoryProductAdapter);

                        Log.e(" ********** Sub_cat_pro_id.size() > 0 **********", "YES");
                        Log.e(" ********** Sub_cat_pro_id.size() > 0 **********", "YES");

                    /*} else {
                        SnackbarManager.show(
                                Snackbar.with(SubCategoryProductListActivity.this)
                                        .position(Snackbar.SnackbarPosition.TOP)
                                        .margin(15, 15)
                                        .backgroundDrawable(R.drawable.snackbar_custom_layout)
                                        .text("No Data Found"));
                    }*/


                } else {
                    Log.e("onPostExecute Sub_cat_id size is Zero ", "ooppss");
                    SnackbarManager.show(
                            Snackbar.with(SubCategoryProductListActivity.this)
                                    .position(Snackbar.SnackbarPosition.TOP)
                                    .margin(15, 15)
                                    .backgroundDrawable(R.drawable.snackbar_custom_layout)
                                    .text("No Data Found"));
                }
//            }
            /*else {
                Log.e("********* StatusSubCategory error found*********", "NOT Success");
                Log.e("********* StatusSubCategory error found*********", "NOT Success");
                Toast.makeText(getApplicationContext(), "Oops!! Please check server connection .",
                        Toast.LENGTH_SHORT).show();

            }*/
        }

    }


    private class SubCategoryProductAdapter extends RecyclerView.Adapter<SubCategoryProductAdapter.MyViewHolder> {


        private Context mContext;
        private List<BeanSubCategoryProduct> subCatarrayList;
        private AdapterView.OnItemClickListener mOnItemClickListener;


        class MyViewHolder extends RecyclerView.ViewHolder {
            TextView subCatProductName;
            TextView subCatProductPrice;
            ImageView SubCatProductimage;
            RelativeLayout SubCatProRl_loader;
            RelativeLayout Rl_product_buy;
            CircularProgressBar SubCatProRl_circular_loader;

            public MyViewHolder(View view) {
                super(view);
                subCatProductName = (TextView) view.findViewById(R.id.tv_sub_category_product_tittle);
                subCatProductPrice = (TextView) view.findViewById(R.id.tv_sub_category_product_price);
                Rl_product_buy = (RelativeLayout) view.findViewById(R.id.rl_sub_category_item_cart);
                SubCatProRl_circular_loader = (CircularProgressBar) view.findViewById(R.id.grid_sub_category_product_progressbar_circular);
                SubCatProRl_loader = (RelativeLayout) view.findViewById(R.id.rl_grid_sub_category_product_progress);
                SubCatProductimage = (ImageView) view.findViewById(R.id.iv_sub_category_product_image);

            }
        }


        public SubCategoryProductAdapter(Context mContext, List<BeanSubCategoryProduct> subCatarrayList) {
            this.mContext = mContext;
            this.subCatarrayList = subCatarrayList;
        }


        @Override
        public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View itemView = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.activity_sub_category_product_grid_items, parent, false);

//            itemView.setOnClickListener(new MyOnClickListener());

            return new MyViewHolder(itemView);
        }


        @Override
        public void onBindViewHolder(final MyViewHolder holder, final int position) {

            holder.subCatProductName.setText(Html.fromHtml(subCatarrayList.get(position).getsubCatProductTittle()));
            holder.subCatProductPrice.setText(Html.fromHtml(subCatarrayList.get(position).getsubCatProductPrice()));

            ImageLoader imageLoader = ImageLoader.getInstance();
            imageLoader.getInstance().displayImage(subCatarrayList.get(position).getImage(), holder.SubCatProductimage, new ImageLoadingListener() {
                @Override
                public void onLoadingStarted(String imageUri, View view) {
                    holder.SubCatProRl_loader.setVisibility(View.VISIBLE);
                }

                @Override
                public void onLoadingFailed(String imageUri, View view, FailReason failReason) {
                    holder.SubCatProRl_loader.setVisibility(View.GONE);

                    Log.e("image not found :", "ok");
                }

                @Override
                public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {
                    holder.SubCatProRl_loader.setVisibility(View.GONE);
                }

                @Override
                public void onLoadingCancelled(String imageUri, View view) {
                    holder.SubCatProRl_loader.setVisibility(View.GONE);

                }
            });


            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int position = holder.getAdapterPosition();

                    Log.e(" Item Position :", "" + subCatarrayList.get(holder.getLayoutPosition()));

                    Log.e(" Adapter Position :", "" + position);

                    Log.e("SubCatSingleProductID Item id", "" + subCatarrayList.get(position).getId());
                    Product_Name_For_Buy = String.valueOf(Html.fromHtml(subCatarrayList.get(position).getsubCatProductTittle()));

                    Log.e("Item Name", "" + String.valueOf(Html.fromHtml(subCatarrayList.get(position).getsubCatProductTittle())));

                    Log.e("Item Name Product_Name_For_Buy", "" + Product_Name_For_Buy);

                    Log.e(" List Size :", "" + subCatarrayList.size());


                    Intent SubCatPage = new Intent(getApplicationContext(), SubCategorySingleProductActivity.class);
                    SubCatPage.putExtra("SubCatSingleProductID", subCatarrayList.get(position).getId());
                    SubCatPage.putExtra("SubCatName", SubCategoryName);
                    SubCategoryProductListActivity.this.startActivity(SubCatPage);


                }
            });


        }

        @Override
        public int getItemCount() {

            Log.e("subCatarrayList size :", "" + subCatarrayList.size());
            return subCatarrayList.size();
        }


    }

    class MyOnClickListener implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            int itemPosition = sub_category_pro_recylerView.indexOfChild(v);
            Log.e("Clicked and Position is ", String.valueOf(itemPosition));
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


    // Initiating Menu XML file (menu.xml)
//    @Override
//    public boolean onCreateOptionsMenu(Menu menu) {
//        MenuInflater menuInflater = getMenuInflater();
//        menuInflater.inflate(R.menu.menu, menu);
//        return true;
//    }

//    @Override
//    public boolean onPrepareOptionsMenu(Menu menu) {
//        super.onPrepareOptionsMenu(menu);
//        menu.findItem(R.id.menu_search).setVisible(false);
//        return true;
//    }

    /**
     * Event Handling for Individual menu item selected
     * Identify single menu item by it's id
     */
//    @Override
//    public boolean onOptionsItemSelected(MenuItem item) {
//
//        switch (item.getItemId()) {
//            case R.id.menu_cart:
//                Toast.makeText(SubCategoryProductListActivity.this, "Cart is Selected", Toast.LENGTH_SHORT).show();
//                return true;
//
//
//            default:
//                return super.onOptionsItemSelected(item);
//        }
//    }


}
