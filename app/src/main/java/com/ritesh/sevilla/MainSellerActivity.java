package com.ritesh.sevilla;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Rect;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.util.Log;
import android.view.HapticFeedbackConstants;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;
import com.facebook.FacebookSdk;
import com.nispok.snackbar.Snackbar;
import com.nispok.snackbar.SnackbarManager;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;
import com.ritesh.sevilla.Beans.BeanMainCategory;
import com.ritesh.sevilla.Beans.BeanSellerProduct;
import com.ritesh.sevilla.Constant.Appconstant;
import com.ritesh.sevilla.Constant.HttpUrlPath;
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
import de.hdodenhof.circleimageview.CircleImageView;
import fr.castorflex.android.circularprogressbar.CircularProgressBar;
import fr.castorflex.android.circularprogressbar.CircularProgressDrawable;

/**
 * Created by ritesh on 13/4/17.
 */
@SuppressWarnings("deprecation")
public class MainSellerActivity extends AppCompatActivity {

    @BindView(R.id.drawerLayout_seller)
    DrawerLayout DL_seller;

    @BindView(R.id.toolbar_main_seller)
    Toolbar TB_main_seller;

    @BindView(R.id.containerView_seller)
    FrameLayout frame_container_seller;

    @BindView(R.id.tv_user_progile_name_seller)
    TextView TV_user_progile_name_seller;

    @BindView(R.id.tv_user_profile_type_seller)
    TextView Tv_user_profile_type_seller;

    @BindView(R.id.rl_no_product_found)
    RelativeLayout RL_no_product_found;

    @BindView(R.id.rl_dr_header_seller)
    RelativeLayout RL_Dr_Header_seller;

    @BindView(R.id.rl_dr_home_seller)
    RelativeLayout RL_Dr_Home_seller;

    @BindView(R.id.rl_dr_order_sales_mode)
    RelativeLayout RL_dr_order_sales_mode;

    @BindView(R.id.rl_dr_my_shipment)
    RelativeLayout RL_dr_my_shipment;

    @BindView(R.id.rl_dr_publicate_new_seller)
    RelativeLayout RL_dr_publicate_new_seller;

    @BindView(R.id.rl_dr_edit_profile_seller)
    RelativeLayout RL_dr_edit_profile_seller;

    @BindView(R.id.rl_dr_contact_support_seller)
    RelativeLayout RL_dr_contact_support_seller;

    @BindView(R.id.rl_dr_about_us_seller)
    RelativeLayout RL_dr_about_us_seller;

    @BindView(R.id.rl_dr_news_event_seller)
    RelativeLayout RL_dr_news_event_seller;

    @BindView(R.id.rl_dr_logout_seller)
    RelativeLayout RL_dr_logout_seller;

    @BindView(R.id.rl_gridview_main_seller_progress)
    RelativeLayout RL_gridview_main_seller_progress;

    CircularProgressBar CPB_Gridview_main_Seller;

    @BindView(R.id.rl_profileprogressbar_seller)
    RelativeLayout RL_profileprogressbar_seller;

    @BindView(R.id.profileprogressbar_seller)
    ProgressBar PB_seller;

    @BindView(R.id.profile_image_seller)
    CircleImageView CIV_image_seller;

    @BindView(R.id.recycler_view_seller)
    RecyclerView RV_seller;

    String
            User_ID = "",
            Str_Get_Status = "",
            Str_Get_User_ID = "",
            Str_Get_user_name = "",
            Str_Get_user_image = "",
            Str_Get_SellerProduct_list = "",
            Str_Get_SellerProduct_list_result = "",
            Str_Get_SellerProduct_status = "",
            Str_Get_SellerProduct_list_Main_result = "",
            Str_Get_SellerProduct_id = "",
            Seller_Product_id = "",
            Seller_Product_id_for_single = "",
            Seller_Product_name_for_single = "",
            Str_Get_SellerProduct_name = "",
            Str_Get_SellerProduct_image = "",
            Str_Get_User_Type = "";

    List<MainSellerActivity> rowSellerProductItems;
    private ArrayList<String> SellerProduct_images;
    private ArrayList<String> SellerProduct_names;
    private ArrayList<String> SellerProduct_id;
    List<BeanSellerProduct> beanSellerProduct0;


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

        DisplayImageOptions defaultOptions = new DisplayImageOptions.Builder().cacheInMemory(true).cacheOnDisk(true).build();
        ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(MainSellerActivity.this).defaultDisplayImageOptions(defaultOptions).build();
        ImageLoader.getInstance().init(config);

        rowSellerProductItems = new ArrayList<MainSellerActivity>();
        SellerProduct_images = new ArrayList<>();
        SellerProduct_names = new ArrayList<>();
        SellerProduct_id = new ArrayList<>();

        CPB_Gridview_main_Seller = (CircularProgressBar) findViewById(R.id.gridview_main_seller_progressbar_circular);
//        signupProgress.setVisibility(View.GONE);
        ((CircularProgressDrawable) CPB_Gridview_main_Seller.getIndeterminateDrawable()).start();
        updateValues();


        Appconstant.sh = getSharedPreferences(Appconstant.MyPREFERENCES, Context.MODE_PRIVATE);
        User_ID = Appconstant.sh.getString("id", null);
        Str_Get_User_Type = Appconstant.sh.getString("usertype", null);
        Log.e("User_ID from SharedPref :", "" + User_ID);
        Log.e("MainSellerActivity Login User Type :", "" + Str_Get_User_Type);


        RecyclerView.LayoutManager mLayoutManager = new GridLayoutManager(this, 2);
        RV_seller.setLayoutManager(mLayoutManager);
        RV_seller.addItemDecoration(new EqualSpaceItemDecoration(5));


        if (Utils.isConnected(getApplicationContext())) {
            GetUserDetailJsontask task = new GetUserDetailJsontask();
            task.execute();
        } else {

            SnackbarManager.show(
                    Snackbar.with(MainSellerActivity.this)
                            .position(Snackbar.SnackbarPosition.TOP)
                            .margin(15, 15)
                            .backgroundDrawable(R.drawable.snackbar_custom_layout)
                            .text("Please Your Internet Connectivity..!!"));

        }

        if (Utils.isConnected(getApplicationContext())) {
            SellerProductJsontask task = new SellerProductJsontask();
            task.execute();
        } else {

            SnackbarManager.show(
                    Snackbar.with(MainSellerActivity.this)
                            .position(Snackbar.SnackbarPosition.TOP)
                            .margin(15, 15)
                            .backgroundDrawable(R.drawable.snackbar_custom_layout)
                            .text("Please Your Internet Connectivity..!!"));

        }

        RL_Dr_Home_seller.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                v.performHapticFeedback(HapticFeedbackConstants.VIRTUAL_KEY);
                Intent GoMainScreen = new Intent(getApplicationContext(), MainSellerActivity.class);
                startActivity(GoMainScreen);
            }
        });

        RL_dr_order_sales_mode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                v.performHapticFeedback(HapticFeedbackConstants.VIRTUAL_KEY);
                Toast.makeText(MainSellerActivity.this, "Sales Clicked", Toast.LENGTH_SHORT).show();

            }
        });

        RL_dr_my_shipment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                v.performHapticFeedback(HapticFeedbackConstants.VIRTUAL_KEY);
                Toast.makeText(MainSellerActivity.this, "Shipment Clicked", Toast.LENGTH_SHORT).show();

            }
        });

        RL_dr_publicate_new_seller.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                v.performHapticFeedback(HapticFeedbackConstants.VIRTUAL_KEY);
//                Toast.makeText(MainSellerActivity.this, "Publicate Clicked", Toast.LENGTH_SHORT).show();
                Intent GoPublicateScreen = new Intent(getApplicationContext(), PublicateNewActivity.class);
                startActivity(GoPublicateScreen);

            }
        });

        RL_dr_edit_profile_seller.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                v.performHapticFeedback(HapticFeedbackConstants.VIRTUAL_KEY);
//                Toast.makeText(MainSellerActivity.this, "Edit Clicked", Toast.LENGTH_SHORT).show();
                Intent GoEditScreen = new Intent(getApplicationContext(), EditProfileActivity.class);
                startActivity(GoEditScreen);

            }
        });

        RL_dr_contact_support_seller.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                v.performHapticFeedback(HapticFeedbackConstants.VIRTUAL_KEY);
//                Toast.makeText(MainSellerActivity.this, "Support Clicked", Toast.LENGTH_SHORT).show();
                Intent GoContactSupportScreen = new Intent(getApplicationContext(), ContactSupportActivity.class);
                startActivity(GoContactSupportScreen);

            }
        });

        RL_dr_about_us_seller.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                v.performHapticFeedback(HapticFeedbackConstants.VIRTUAL_KEY);
                Toast.makeText(MainSellerActivity.this, "About Clicked", Toast.LENGTH_SHORT).show();

            }
        });

        RL_dr_news_event_seller.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                v.performHapticFeedback(HapticFeedbackConstants.VIRTUAL_KEY);
//                Toast.makeText(MainSellerActivity.this, "Event Clicked", Toast.LENGTH_SHORT).show();
                Intent GoNewsEventScreen = new Intent(getApplicationContext(), NewsEventActivity.class);
                startActivity(GoNewsEventScreen);

            }
        });

        RL_no_product_found.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                v.performHapticFeedback(HapticFeedbackConstants.VIRTUAL_KEY);
//                Toast.makeText(MainSellerActivity.this, "Event Clicked", Toast.LENGTH_SHORT).show();
                Intent GoPublicateScreen = new Intent(getApplicationContext(), PublicateNewActivity.class);
                startActivity(GoPublicateScreen);

            }
        });

        RL_dr_logout_seller.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                v.performHapticFeedback(HapticFeedbackConstants.VIRTUAL_KEY);
//                Toast.makeText(MainSellerActivity.this, "Logout Clicked", Toast.LENGTH_SHORT).show();
                AlertDialog.Builder builder = new AlertDialog.Builder(MainSellerActivity.this);
                builder.setMessage("Are you sure you want to Logout?")
                        .setCancelable(false)
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                Appconstant.editor.remove("loginTest");
                                Appconstant.editor.clear();
                                Appconstant.editor.commit();
//                                disconnectFromFacebook();
                                Toast.makeText(MainSellerActivity.this, "You have successfully logout",
                                        Toast.LENGTH_SHORT).show();
                                Intent GoLoginscreen = new Intent(MainSellerActivity.this, LoginSelectActivity.class);
                                GoLoginscreen.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                GoLoginscreen.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                startActivity(GoLoginscreen);
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
        });


    }


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
        CPB_Gridview_main_Seller.setIndeterminateDrawable(circularProgressDrawable = b.build());

        // /!\ Terrible hack, do not do this at home!
        circularProgressDrawable.setBounds(0,
                0,
                CPB_Gridview_main_Seller.getWidth(),
                CPB_Gridview_main_Seller.getHeight());
        CPB_Gridview_main_Seller.setVisibility(View.INVISIBLE);
        CPB_Gridview_main_Seller.setVisibility(View.VISIBLE);
    }


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
            outRect.left = mSpaceHeight;
            outRect.right = mSpaceHeight;
        }

    }



    private class GetUserDetailJsontask extends AsyncTask<String, Void, String> {

        boolean iserror = false;

        @Override
        protected void onPreExecute() {
            // TODO Auto-generated method stub
            //  loginprogressbar.setVisibility(View.VISIBLE);
            Log.e("******* NOW EditProfileGetUserDetailJsontask WEB SERVICE IS RUNNING *******", "YES");
            Log.e("******* NOW EditProfileGetUserDetailJsontask WEB SERVICE IS RUNNING *******", "YES");
            Log.e("User_ID onPreExecute :", "" + User_ID);
            RL_profileprogressbar_seller.setVisibility(View.VISIBLE);

        }

        @Override
        protected String doInBackground(String... params) {
            Log.e("******* NOW Login TASK IS RUNNING *******", "YES");
            HttpClient client = new DefaultHttpClient();
            HttpPost post = new HttpPost("http://sevilla.centrocomercial.com.es/wp-content/plugins/webserv/get_user.php?user_id=" + User_ID);

            Log.e("URL :", "" + "http://sevilla.centrocomercial.com.es/wp-content/plugins/webserv/get_user.php?user_id=" + User_ID);

            try {
                HttpResponse response = client.execute(post);
                String object = EntityUtils.toString(response.getEntity());
                Log.e("*******object******** :", "" + object);

                //JSONArray js = new JSONArray(object);
                JSONObject jobect = new JSONObject(object);
                Str_Get_Status = jobect.getString("status");
                if (Str_Get_Status.equalsIgnoreCase("1")) {
                    Str_Get_User_ID = jobect.getString("ID");
                    Str_Get_user_name = jobect.getString("user_name");
                    Str_Get_user_image = jobect.getString("user_image");

                }

            } catch (Exception e) {
                Log.v("22", "22" + e.getMessage());
                e.printStackTrace();
                iserror = true;
            }

            return Str_Get_Status;
        }

        @Override
        protected void onPostExecute(String result1) {
            // TODO Auto-generated method stub
            super.onPostExecute(result1);

            if (!iserror) {
                if (Str_Get_Status.equalsIgnoreCase("1")) {

                    Log.e("Str_Get_User_ID :", "" + Str_Get_User_ID);
                    Log.e("Str_Get_user_name :", "" + Str_Get_user_name);
                    Log.e("Str_Get_user_image :", "" + Str_Get_user_image);

                    TV_user_progile_name_seller.setText(Str_Get_user_name);
                    Tv_user_profile_type_seller.setText(Str_Get_User_Type);


                    ImageLoader imageLoader = ImageLoader.getInstance();
                    imageLoader.getInstance().displayImage(Str_Get_user_image, CIV_image_seller, new ImageLoadingListener() {
                        @Override
                        public void onLoadingStarted(String imageUri, View view) {
                            RL_profileprogressbar_seller.setVisibility(View.VISIBLE);
                        }

                        @Override
                        public void onLoadingFailed(String imageUri, View view, FailReason failReason) {
                            RL_profileprogressbar_seller.setVisibility(View.GONE);
                        }

                        @Override
                        public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {
                            RL_profileprogressbar_seller.setVisibility(View.GONE);
                        }

                        @Override
                        public void onLoadingCancelled(String imageUri, View view) {
                            RL_profileprogressbar_seller.setVisibility(View.GONE);
                        }
                    });

                } else {
                    Log.e("onPostExecute Sub_cat_id size is Zero ", "ooppss");
                    SnackbarManager.show(
                            Snackbar.with(MainSellerActivity.this)
                                    .position(Snackbar.SnackbarPosition.TOP)
                                    .margin(15, 15)
                                    .backgroundDrawable(R.drawable.snackbar_custom_layout)
                                    .text("No Data Found"));
                }
            }


        }

    }




    private class SellerProductJsontask extends AsyncTask<String, Void, List<BeanSellerProduct>> {

        boolean iserror = false;
        String result = " ";

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            RL_gridview_main_seller_progress.setVisibility(View.VISIBLE);
            Log.e(" ************ SellerProductJsontask AsyncTask Start ************ :", "yes");

            Log.e("onPreExecute User_ID :", "" + User_ID);
            Log.e("onPreExecute SellerProductJsontask URL :", ""
                    + "http://sevilla.centrocomercial.com.es/wp-content/plugins/webserv/get_seller_product_list.php?user_id="+User_ID);
        }

        @Override
        protected List<BeanSellerProduct> doInBackground(String... params) {
            HttpClient client = new DefaultHttpClient();
            HttpPost post = new HttpPost("http://sevilla.centrocomercial.com.es/wp-content/plugins/webserv/get_seller_product_list.php?user_id="+User_ID);

            Log.e(" ************doInBackground URL SellerProductJsontask :", " " + "http://sevilla.centrocomercial.com.es/wp-content/plugins/webserv/get_seller_product_list.php?user_id="+User_ID);

            try {
                HttpResponse response = client.execute(post);
                String objSellerProduct = EntityUtils.toString(response.getEntity());
                beanSellerProduct0 = new ArrayList<>();
                Log.e("************ All SellerProduct ************", "" + objSellerProduct);

                Log.e("************Json data*******************", " " + objSellerProduct);
                JSONObject jsonObject = new JSONObject(objSellerProduct);
                Str_Get_SellerProduct_status = jsonObject.getString("status");
                Str_Get_SellerProduct_list_Main_result = jsonObject.getString("result");
                Str_Get_SellerProduct_list = jsonObject.getString("seller_product_list");
                Log.e("************Json Product_Catagories data*******************", " " + Str_Get_SellerProduct_list);

                JSONArray jaaray = new JSONArray(Str_Get_SellerProduct_list);

                for (int i = 0; i < jaaray.length(); i++) {
                    Str_Get_SellerProduct_list_result = jaaray.getJSONObject(i).getString("post_content");

                    if (Str_Get_SellerProduct_list_result.equalsIgnoreCase("successful")) {
                        BeanSellerProduct beanSellerProduct = new BeanSellerProduct();
                        beanSellerProduct.setSellerProductid(jaaray.getJSONObject(i).getString("product_id"));
                        beanSellerProduct.setSellerProductName(jaaray.getJSONObject(i).getString("post_title"));
                        beanSellerProduct.setSellerProductImage(jaaray.getJSONObject(i).getString("prodcut_image"));
                        beanSellerProduct0.add(beanSellerProduct);


                        Str_Get_SellerProduct_id = jaaray.getJSONObject(i).getString("product_id");
                        Str_Get_SellerProduct_name = jaaray.getJSONObject(i).getString("post_title");
                        Str_Get_SellerProduct_image = jaaray.getJSONObject(i).getString("prodcut_image");

                        SellerProduct_id.add(Str_Get_SellerProduct_id);
                        SellerProduct_names.add(Str_Get_SellerProduct_name);
                        SellerProduct_images.add(Str_Get_SellerProduct_image);
                        Log.e(" ********** SellerProduct_id **********", "" + SellerProduct_id);
                        Log.e(" ********** SellerProduct_names **********", "" + SellerProduct_names);
                        Log.e(" ********** SellerProduct_images **********", "" + SellerProduct_images);


                        String SellerProduct_idlist = SellerProduct_id.get(i);
                        Log.e(" ********** SellerProduct_idlist **********", "" + SellerProduct_idlist);

                        String SellerProduct_nameslist = SellerProduct_names.get(i);
                        Log.e(" ********** SellerProduct_nameslist **********", "" + SellerProduct_nameslist);

                        String SellerProduct_imageslist = SellerProduct_images.get(i);
                        Log.e(" ********** SellerProduct_imageslist **********", "" + SellerProduct_imageslist);
                    }
                }

            } catch (Exception e) {
                System.out.println("Errror in gettting by ID" + e);
                Log.e("22", "22" + e.getMessage());
                e.printStackTrace();
                iserror = true;
            }
            return beanSellerProduct0;
        }

        @Override
        protected void onPostExecute(List<BeanSellerProduct> mystring) {
            super.onPostExecute(mystring);
            RL_gridview_main_seller_progress.setVisibility(View.GONE);

            if (SellerProduct_id.size() > 0) {
                Log.e(" ********** SellerProduct_id ********** ", "" + SellerProduct_id);
                RL_no_product_found.setVisibility(View.GONE);
                SellerProductAdapter sellerProductAdapter = new SellerProductAdapter(MainSellerActivity.this, mystring);
                RV_seller.setAdapter(sellerProductAdapter);


                Log.e(" ********** Seller Product.size() > 0 **********", "YES");
                Log.e(" ********** Seller Product.size() > 0 **********", "YES");
                Log.e(" ********** Seller Product.size() > 0 **********", "YES");
            } else {

                /**************** End Animation ****************/
                Log.e("SellerProduct_id size is :", "0");

                SnackbarManager.show(
                        Snackbar.with(MainSellerActivity.this)
                                .position(Snackbar.SnackbarPosition.TOP)
                                .margin(15, 15)
                                .backgroundDrawable(R.drawable.snackbar_custom_layout)
                                .text("No Product Found"));

                RL_no_product_found.setVisibility(View.VISIBLE);

            }
        }

    }




    private class SellerProductAdapter extends RecyclerView.Adapter<MainSellerActivity.SellerProductAdapter.MyViewHolder> {

        private Context mContext;
        private List<BeanSellerProduct> arrayList;


        class MyViewHolder extends RecyclerView.ViewHolder {
            TextView seller_productName;
            ImageView seller_productimage;
            ImageView seller_productimage_transparent_overlay;
            RelativeLayout Rl_seller_productloader;
            CircularProgressBar Rl_seller_productcircular_loader;

            public MyViewHolder(View view) {
                super(view);
                seller_productName = (TextView) view.findViewById(R.id.tv_gv_main_seller_product);
                Rl_seller_productcircular_loader = (CircularProgressBar) view.findViewById(R.id.grid_main_seller_product_item_progressbar_circular);
                Rl_seller_productloader = (RelativeLayout) view.findViewById(R.id.rl_grid_main_seller_product_item_progress);
                seller_productimage = (ImageView) view.findViewById(R.id.gv_seller_product_image);
                seller_productimage_transparent_overlay = (ImageView) view.findViewById(R.id.iv_tranparent_overlay_seller_product);

            }
        }


        public SellerProductAdapter(Context mContext, List<BeanSellerProduct> arrayList) {
            this.mContext = mContext;
            this.arrayList = arrayList;
        }

        @Override
        public MainSellerActivity.SellerProductAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View itemView = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.activity_main_seller_product_grid_items, parent, false);

            return new MainSellerActivity.SellerProductAdapter.MyViewHolder(itemView);
        }

        @Override
        public void onBindViewHolder(final MainSellerActivity.SellerProductAdapter.MyViewHolder holder, final int position) {

            holder.seller_productName.setText(Html.fromHtml(arrayList.get(position).getSellerProductName()));

            ImageLoader imageLoader = ImageLoader.getInstance();
            imageLoader.getInstance().displayImage(arrayList.get(position).getSellerProductImage(), holder.seller_productimage, new ImageLoadingListener() {
                @Override
                public void onLoadingStarted(String imageUri, View view) {
                    holder.Rl_seller_productloader.setVisibility(View.VISIBLE);
                }

                @Override
                public void onLoadingFailed(String imageUri, View view, FailReason failReason) {
                    holder.Rl_seller_productloader.setVisibility(View.GONE);
                }

                @Override
                public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {
                    holder.Rl_seller_productloader.setVisibility(View.GONE);
                }

                @Override
                public void onLoadingCancelled(String imageUri, View view) {
                    holder.Rl_seller_productloader.setVisibility(View.GONE);

                }
            });


            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int position = holder.getAdapterPosition();

//                    holder.Catimage_transparent_overlay.setVisibility(View.GONE);
                    Log.e("SellerProduct Item Position :", "" + arrayList.get(holder.getLayoutPosition()));

                    Log.e(" Adapter SellerProduct Position :", "" + position);

                    Log.e("Main SellerProduct id", "" + arrayList.get(position).getSellerProductid());
                    Seller_Product_id = arrayList.get(position).getSellerProductid();
                    Log.e("Seller_Product_id :", "" + Seller_Product_id);
                    Seller_Product_name_for_single = String.valueOf(Html.fromHtml(arrayList.get(position).getSellerProductName()));

                    Log.e("Item Name", "" + String.valueOf(Html.fromHtml(arrayList.get(position).getSellerProductName())));

                    Log.e("Item Name Seller_Product_name_for_single", "" + Seller_Product_name_for_single);

                    Log.e(" List Size :", "" + arrayList.size());

                    Intent GoSellerSingleProductScreen = new Intent(MainSellerActivity.this, SellerSingleProductDetail.class);
                    GoSellerSingleProductScreen.putExtra("SellerProductid", arrayList.get(position).getSellerProductid());
                    GoSellerSingleProductScreen.putExtra("SellerProductname", Seller_Product_name_for_single);
                    startActivity(GoSellerSingleProductScreen);

                }
            });


        }


        @Override
        public int getItemCount() {
            return arrayList.size();
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
                        MainSellerActivity.this.finish();

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
