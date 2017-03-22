package com.ritesh.sevilla;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Rect;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.view.ViewPager;
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
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
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
import com.facebook.AccessToken;
import com.facebook.FacebookSdk;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.HttpMethod;
import com.facebook.login.LoginManager;
import com.nispok.snackbar.Snackbar;
import com.nispok.snackbar.SnackbarManager;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;
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
import java.util.Timer;
import java.util.TimerTask;

import butterknife.BindView;
import butterknife.ButterKnife;
import de.hdodenhof.circleimageview.CircleImageView;
import fr.castorflex.android.circularprogressbar.CircularProgressBar;
import fr.castorflex.android.circularprogressbar.CircularProgressDrawable;
import me.relex.circleindicator.CircleIndicator;

/**
 * Created by ritesh on 9/2/17.
 */
@SuppressWarnings("deprecation")
public class MainActivity extends AppCompatActivity {

    @BindView(R.id.drawerLayout)
    DrawerLayout drawer;
    @BindView(R.id.toolbar_main)
    Toolbar toolbar_Main;


    @BindView(R.id.containerView)
    FrameLayout frame_container;

    @BindView(R.id.rl_dr_header)
    RelativeLayout rl_Dr_Header;

    @BindView(R.id.rl_dr_home)
    RelativeLayout rl_Dr_Home;

    @BindView(R.id.rl_dr_place_seller)
    RelativeLayout rl_Dr_Place_Seller;

    @BindView(R.id.rl_dr_inventory)
    RelativeLayout rl_Dr_Inventory;

    @BindView(R.id.rl_dr_publicate_new)
    RelativeLayout rl_Dr_Publicate_New;

    @BindView(R.id.rl_dr_edit_profile)
    RelativeLayout rl_Dr_Edit_Profile;

    @BindView(R.id.rl_dr_message)
    RelativeLayout rl_Dr_Message;

    @BindView(R.id.rl_dr_contact_support)
    RelativeLayout rl_Dr_Contact_Support;

    @BindView(R.id.rl_dr_about_us)
    RelativeLayout rl_Dr_About_Us;

    @BindView(R.id.rl_dr_news_event)
    RelativeLayout rl_Dr_News_Event;

    @BindView(R.id.rl_dr_logout)
    RelativeLayout rl_Dr_Logout;

    @BindView(R.id.rl_cart_icon_main)
    RelativeLayout rl_cart_main;

    @BindView(R.id.rl_badgeview_cart_item_main)
    RelativeLayout rl_cart_badgeview_main;

    @BindView(R.id.tv_badge_counter_main_category)
    TextView TV_cart_badge_counter_textview;

    @BindView(R.id.profile_image)
    CircleImageView profileImageView;

    @BindView(R.id.profileprogressbar)
    ProgressBar profileimageprogressbar;

    @BindView(R.id.tv_user_progile_name)
    TextView TV_user_profile_name;

    @BindView(R.id.tv_user_profile_type)
    TextView TV_user_profile_type;

    String STR_Fb_Name = "", STR_Fb_Email = "", STR_Fb_User_Image = "", STR_Fb_ID = "";


    /*recycler data (Start 01 of 03)*/
    String Product_Catagories = "", Product_Catagories_result = "", Category_Id = "",
            Category_Name = "", Category_Img_Src = "", Category_Name_for_Sub_Category = "",
            MainCategory_ID = "";


    String
            User_ID = "",
            Str_Get_Cart_Deatil_User_ID = "",
            Str_Get_Cart_Product_count = "",
            Str_Get_User_ID = "",
            Str_Get_user_image = "",
            Str_Get_user_name = "",
            Str_Set_user_name = "",
            Str_profileImage_path = "",
            result = "",
            Str_Get_Cart_Detail_Status = "",
            Str_Get_Cart_result = "",
            Str_Get_Status = "";

    List<MainActivity> rowItems;
    private ArrayList<String> cat_images;
    private ArrayList<String> cat_names;
    private ArrayList<String> cat_id;
    List<BeanMainCategory> beanMainCategories0;

    @BindView(R.id.rl_gridview_main_progress)
    RelativeLayout pv_gridview_main_progressview;

    CircularProgressBar mCircularProgressBarGridview_main;

    @BindView(R.id.recycler_view)
    RecyclerView main_recylerView;
    /*recycler data (End 01 of 03)*/


    @BindView(R.id.vp_viewpager)
    ViewPager VP_banner_slidder;

    @BindView(R.id.ci_indicator)
    CircleIndicator CI_indicator;

    private static int currentPage = 0;
    private static int NUM_PAGES = 0;
    private static final Integer[] IMAGES = {R.drawable.ic_ll_one_one, R.drawable.ic_ll_one_two,
            R.drawable.ic_ll_two_one, R.drawable.ic_ll_two_two};
    private ArrayList<Integer> ImagesArray = new ArrayList<Integer>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FacebookSdk.sdkInitialize(getApplicationContext());
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        setSupportActionBar(toolbar_Main);

        //create default navigation drawer toggle
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer, toolbar_Main,
                R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();


        Appconstant.sh = getSharedPreferences(Appconstant.MyPREFERENCES, Context.MODE_PRIVATE);
        User_ID = Appconstant.sh.getString("id", null);
        Log.e("User_ID from SharedPref :", "" + User_ID);

        if (Utils.isConnected(getApplicationContext())) {
            GetUserDetailJsontask task = new GetUserDetailJsontask();
            task.execute();
        } else {

            SnackbarManager.show(
                    Snackbar.with(MainActivity.this)
                            .position(Snackbar.SnackbarPosition.TOP)
                            .margin(15, 15)
                            .backgroundDrawable(R.drawable.snackbar_custom_layout)
                            .text("Please Your Internet Connectivity..!!"));

        }


        if (Utils.isConnected(getApplicationContext())) {
            Log.e("MainCartDetailJsontask Call :", "OK");
            MainCartDetailJsontask task = new MainCartDetailJsontask();
            task.execute();
        } else {

            SnackbarManager.show(
                    Snackbar.with(MainActivity.this)
                            .position(Snackbar.SnackbarPosition.TOP)
                            .margin(15, 15)
                            .backgroundDrawable(R.drawable.snackbar_custom_layout)
                            .text("Please Your Internet Connectivity..!!"));

        }


        /*recycler data (Start 02 of 03)*/
        /*image settin by universal image loader */
        DisplayImageOptions defaultOptions = new DisplayImageOptions.Builder().cacheInMemory(true).cacheOnDisk(true).build();
        ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(MainActivity.this).defaultDisplayImageOptions(defaultOptions).build();
        ImageLoader.getInstance().init(config);
        /*image settin by universal image loader */

         /*circular progress bar (Start)*/
        mCircularProgressBarGridview_main = (CircularProgressBar) findViewById(R.id.gridview_main_progressbar_circular);
//        signupProgress.setVisibility(View.GONE);
        ((CircularProgressDrawable) mCircularProgressBarGridview_main.getIndeterminateDrawable()).start();
        updateValues();
        /*circular progress bar (End)*/


        rowItems = new ArrayList<MainActivity>();
        cat_images = new ArrayList<>();
        cat_names = new ArrayList<>();
        cat_id = new ArrayList<>();


        RecyclerView.LayoutManager mLayoutManager = new GridLayoutManager(this, 2);
        main_recylerView.setLayoutManager(mLayoutManager);
        main_recylerView.addItemDecoration(new MainActivity.EqualSpaceItemDecoration(5));


        if (Utils.isConnected(getApplicationContext())) {
            MainCategoryJsontask task = new MainCategoryJsontask();
            task.execute();
        } else {

            SnackbarManager.show(
                    Snackbar.with(MainActivity.this)
                            .position(Snackbar.SnackbarPosition.TOP)
                            .margin(15, 15)
                            .backgroundDrawable(R.drawable.snackbar_custom_layout)
                            .text("Please Your Internet Connectivity..!!"));

        }
        /*recycler data (End 02 of 03)*/


        if (Appconstant.sh.contains("F_id")) {
            STR_Fb_ID = Appconstant.sh.getString("F_id", "");
        }

        if (Appconstant.sh.contains("F_username")) {
            STR_Fb_Name = Appconstant.sh.getString("F_username", "");
//            TV_user_profile_name.setText(STR_Fb_Name);
        }

        if (Appconstant.sh.contains("F_email")) {
            STR_Fb_Email = Appconstant.sh.getString("F_email", "");
        }

        if (Appconstant.sh.contains("F_userimage")) {
            STR_Fb_User_Image = Appconstant.sh.getString("F_userimage", "");
        }


/*
        VP_banner_slidder.setAdapter(new SamplePagerAdapter());
        CI_indicator.setViewPager(VP_banner_slidder);*/
        init();

        Log.e("STR_Fb_Name :", "" + STR_Fb_Name);
        Log.e("STR_Fb_Email :", "" + STR_Fb_Email);
        Log.e("STR_Fb_User_Image :", "" + STR_Fb_User_Image);
        Log.e("STR_Fb_ID :", "" + STR_Fb_ID);

        /*image setting by universal image loader */
        DisplayImageOptions defaultOptionsrecycler = new DisplayImageOptions.Builder().cacheInMemory(true).cacheOnDisk(true).build();
        ImageLoaderConfiguration configrecycler = new ImageLoaderConfiguration.Builder(MainActivity.this).defaultDisplayImageOptions(defaultOptionsrecycler).build();
        ImageLoader.getInstance().init(configrecycler);
        /*image settin by universal image loader */


         /*set profile image*/
        ImageLoader imageLoader = ImageLoader.getInstance();
        imageLoader.getInstance().displayImage(STR_Fb_User_Image, profileImageView, new ImageLoadingListener() {
            @Override
            public void onLoadingStarted(String imageUri, View view) {
                profileimageprogressbar.setVisibility(View.VISIBLE);
            }

            @Override
            public void onLoadingFailed(String imageUri, View view, FailReason failReason) {
                profileimageprogressbar.setVisibility(View.GONE);
            }

            @Override
            public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {
                profileimageprogressbar.setVisibility(View.GONE);
            }

            @Override
            public void onLoadingCancelled(String imageUri, View view) {
                profileimageprogressbar.setVisibility(View.GONE);
            }
        });


        profileImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String URLProfileimage = STR_Fb_User_Image;
                Intent intentprofileimage = new Intent(MainActivity.this, WebViewProfileImage.class);
                intentprofileimage.putExtra("URLP", URLProfileimage);

                Log.e("********** URLProfileimage ********** :", " " + STR_Fb_User_Image);
                Log.e("********** URLProfileimage ********** :", " " + STR_Fb_User_Image);
                Log.e("********** URLProfileimage ********** :", " " + STR_Fb_User_Image);

                startActivity(intentprofileimage);
            }
        });


        rl_Dr_Home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                v.performHapticFeedback(HapticFeedbackConstants.VIRTUAL_KEY);
                Toast.makeText(getApplicationContext(), "Home Clicked", Toast.LENGTH_SHORT).show();
            }
        });

        rl_Dr_Header.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                v.performHapticFeedback(HapticFeedbackConstants.VIRTUAL_KEY);
                Toast.makeText(getApplicationContext(), "Header Clicked", Toast.LENGTH_SHORT).show();
            }
        });

        rl_Dr_Place_Seller.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                v.performHapticFeedback(HapticFeedbackConstants.VIRTUAL_KEY);
                Toast.makeText(getApplicationContext(), "Place Seller Clicked", Toast.LENGTH_SHORT).show();
            }
        });

        rl_Dr_Inventory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                v.performHapticFeedback(HapticFeedbackConstants.VIRTUAL_KEY);
                Toast.makeText(getApplicationContext(), "Inventory Clicked", Toast.LENGTH_SHORT).show();
                Intent GoDeliveryScreen = new Intent(getApplicationContext(), DeliveryActivity.class);
                startActivity(GoDeliveryScreen);
            }
        });

        rl_Dr_Publicate_New.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                v.performHapticFeedback(HapticFeedbackConstants.VIRTUAL_KEY);
                Toast.makeText(getApplicationContext(), "Publicate New Clicked", Toast.LENGTH_SHORT).show();
            }
        });

        rl_Dr_Edit_Profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                v.performHapticFeedback(HapticFeedbackConstants.VIRTUAL_KEY);
                Toast.makeText(getApplicationContext(), "Edit Profile Clicked", Toast.LENGTH_SHORT).show();
                Intent GoEditScreen = new Intent(getApplicationContext(), EditProfileActivity.class);
                startActivity(GoEditScreen);
            }
        });

        rl_Dr_Message.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                v.performHapticFeedback(HapticFeedbackConstants.VIRTUAL_KEY);
                Toast.makeText(getApplicationContext(), "Message Clicked", Toast.LENGTH_SHORT).show();
            }
        });

        rl_Dr_Contact_Support.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                v.performHapticFeedback(HapticFeedbackConstants.VIRTUAL_KEY);
                Toast.makeText(getApplicationContext(), "Contact Support Clicked", Toast.LENGTH_SHORT).show();
            }
        });

        rl_Dr_About_Us.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                v.performHapticFeedback(HapticFeedbackConstants.VIRTUAL_KEY);
                Toast.makeText(getApplicationContext(), "About Us Clicked", Toast.LENGTH_SHORT).show();
            }
        });

        rl_Dr_News_Event.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                v.performHapticFeedback(HapticFeedbackConstants.VIRTUAL_KEY);
                Toast.makeText(getApplicationContext(), "News Event Clicked", Toast.LENGTH_SHORT).show();
            }
        });

        rl_Dr_Logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                v.performHapticFeedback(HapticFeedbackConstants.VIRTUAL_KEY);

                AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                builder.setMessage("Are you sure you want to Logout?")
                        .setCancelable(false)
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                Appconstant.editor.remove("loginTest");
                                Appconstant.editor.commit();
                                disconnectFromFacebook();
                                Toast.makeText(getApplicationContext(), "You have successfully logout",
                                        Toast.LENGTH_SHORT).show();
                                Intent GoLoginscreen = new Intent(getApplicationContext(), LoginSelectActivity.class);
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

    @Override
    protected void onStart() {
        super.onStart();
        Log.e("lifecycle", "onStart invoked");
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.e("lifecycle", "onResume invoked");
    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.e("lifecycle", "onPause invoked");
    }

    @Override
    protected void onStop() {
        super.onStop();
        Log.e("lifecycle", "onStop invoked");
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        Log.e("lifecycle", "onRestart invoked");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.e("lifecycle", "onDestroy invoked");
    }


    public void disconnectFromFacebook() {

        if (AccessToken.getCurrentAccessToken() == null) {

            Log.e(" You are already loged out from facebook :", "ok");
            return; // already logged out
        }

        new GraphRequest(AccessToken.getCurrentAccessToken(), "/me/permissions/", null, HttpMethod.DELETE, new GraphRequest
                .Callback() {
            @Override
            public void onCompleted(GraphResponse graphResponse) {
                Log.e("Successfully logout from facebook :", "Congratulations");
                AccessToken.setCurrentAccessToken(null);
                LoginManager.getInstance().logOut();
                Log.e("Successfully logout from facebook :", "Congrates");

            }
        }).executeAsync();
    }


    private void init() {
        for (int i = 0; i < IMAGES.length; i++)
            ImagesArray.add(IMAGES[i]);


        VP_banner_slidder.setAdapter(new CustomPagerAdapter(MainActivity.this, ImagesArray));
        CI_indicator.setViewPager(VP_banner_slidder);
        NUM_PAGES = IMAGES.length;

        // Auto start of viewpager
        final Handler handler = new Handler();
        final Runnable Update = new Runnable() {
            public void run() {
                if (currentPage == NUM_PAGES) {
                    currentPage = 0;
                }
                VP_banner_slidder.setCurrentItem(currentPage++, true);
            }
        };
        Timer swipeTimer = new Timer();
        swipeTimer.schedule(new TimerTask() {
            @Override
            public void run() {
                handler.post(Update);
            }
        }, 3000, 3000);

        // Pager listener over indicator
        CI_indicator.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {

            @Override
            public void onPageSelected(int position) {
                currentPage = position;
                Log.e("Curent Page :", "" + currentPage);
//                Toast.makeText(MainActivity.this, "Curent Page" + "" + currentPage, Toast.LENGTH_SHORT).show();

            }

            @Override
            public void onPageScrolled(int pos, float arg1, int arg2) {

            }

            @Override
            public void onPageScrollStateChanged(int pos) {

            }
        });

    }


    /*recycler data (Start 03  of 03)*/

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
        mCircularProgressBarGridview_main.setIndeterminateDrawable(circularProgressDrawable = b.build());

        // /!\ Terrible hack, do not do this at home!
        circularProgressDrawable.setBounds(0,
                0,
                mCircularProgressBarGridview_main.getWidth(),
                mCircularProgressBarGridview_main.getHeight());
        mCircularProgressBarGridview_main.setVisibility(View.INVISIBLE);
        mCircularProgressBarGridview_main.setVisibility(View.VISIBLE);
    }
    /*progressbar data (End)*/

    private class MainCategoryJsontask extends AsyncTask<String, Void, List<BeanMainCategory>> {

        boolean iserror = false;
        String result = " ";

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pv_gridview_main_progressview.setVisibility(View.VISIBLE);
            Log.e(" ************ UserListData AsyncTask Start ************ :", "yes");
        }

        @Override
        protected List<BeanMainCategory> doInBackground(String... params) {
            HttpClient client = new DefaultHttpClient();
            HttpPost post = new HttpPost(HttpUrlPath.urlPathMainCategory);

            Log.e(" ************ URL :", " " + HttpUrlPath.urlPathMainCategory);

            try {
                HttpResponse response = client.execute(post);
                String obj = EntityUtils.toString(response.getEntity());
                beanMainCategories0 = new ArrayList<>();
                Log.e("************ All App USER list ************", "" + obj);

                Log.e("************Json data*******************", " " + obj);
                JSONObject jsonObject = new JSONObject(obj);
                Product_Catagories = jsonObject.getString("Product_catagories");
                Log.e("************Json Product_Catagories data*******************", " " + Product_Catagories);

                JSONArray jaaray = new JSONArray(Product_Catagories);

                for (int i = 0; i < jaaray.length(); i++) {
                    Product_Catagories_result = jaaray.getJSONObject(i).getString("catagories_result");

                    if (Product_Catagories_result.equalsIgnoreCase("Successful")) {
                        BeanMainCategory beanMainCategory = new BeanMainCategory();
                        beanMainCategory.setId(jaaray.getJSONObject(i).getString("category_id"));
                        beanMainCategory.setUsername(jaaray.getJSONObject(i).getString("category_name"));
                        beanMainCategory.setImage(jaaray.getJSONObject(i).getString("img_src"));
                        beanMainCategories0.add(beanMainCategory);


                        Category_Id = jaaray.getJSONObject(i).getString("category_id");
                        Category_Name = jaaray.getJSONObject(i).getString("category_name");
                        Category_Img_Src = jaaray.getJSONObject(i).getString("img_src");

                        cat_id.add(Category_Id);
                        cat_images.add(Category_Img_Src);
                        cat_names.add(Category_Name);
                        Log.e(" ********** cat_id **********", "" + cat_id);
                        Log.e(" ********** cat_images **********", "" + cat_images);
                        Log.e(" ********** cat_names **********", "" + cat_names);


                        String cat_idlist = cat_id.get(i);
                        Log.e(" ********** cat_idlist **********", "" + cat_idlist);

                        String cat_imageslist = cat_images.get(i);
                        Log.e(" ********** cat_imageslist **********", "" + cat_imageslist);

                        String cat_nameslist = cat_names.get(i);
                        Log.e(" ********** cat_nameslist **********", "" + cat_nameslist);
                    }
                }

            } catch (Exception e) {
                System.out.println("Errror in gettting by ID" + e);
                Log.e("22", "22" + e.getMessage());
                e.printStackTrace();
                iserror = true;
            }
            return beanMainCategories0;
        }

        @Override
        protected void onPostExecute(List<BeanMainCategory> mystring) {
            super.onPostExecute(mystring);
            pv_gridview_main_progressview.setVisibility(View.GONE);

            if (cat_id.size() > 0) {
                Log.e(" ********** cat_id ********** ", "" + cat_id);

                /**************** Start Animation **************  **/
                YoYo.with(Techniques.Wobble)
                        .duration(700)
                        .playOn(rl_cart_main);
                YoYo.with(Techniques.Wobble)
                        .duration(700)
                        .playOn(rl_cart_badgeview_main);
                /**************** End Animation ****************/
                CategoryAdapter categoryAdapter = new MainActivity.CategoryAdapter(MainActivity.this, mystring);
                main_recylerView.setAdapter(categoryAdapter);


                Log.e(" ********** listvideoid.size() > 0 **********", "YES");
                Log.e(" ********** listvideoid.size() > 0 **********", "YES");
                Log.e(" ********** listvideoid.size() > 0 **********", "YES");
            } else {

                /**************** Start Animation **************  **/
                YoYo.with(Techniques.Wobble)
                        .duration(700)
                        .playOn(rl_cart_main);
                YoYo.with(Techniques.Wobble)
                        .duration(700)
                        .playOn(rl_cart_badgeview_main);
                /**************** End Animation ****************/
                Log.e("cat_id size is :", "0");

            }
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

                    TV_user_profile_name.setText(Str_Get_user_name);


                    ImageLoader imageLoader = ImageLoader.getInstance();
                    imageLoader.getInstance().displayImage(Str_Get_user_image, profileImageView, new ImageLoadingListener() {
                        @Override
                        public void onLoadingStarted(String imageUri, View view) {
//                            RL_edit_profile_image_progress.setVisibility(View.VISIBLE);
                        }

                        @Override
                        public void onLoadingFailed(String imageUri, View view, FailReason failReason) {
//                            RL_edit_profile_image_progress.setVisibility(View.GONE);
                        }

                        @Override
                        public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {
//                            RL_edit_profile_image_progress.setVisibility(View.GONE);
                        }

                        @Override
                        public void onLoadingCancelled(String imageUri, View view) {
//                            RL_edit_profile_image_progress.setVisibility(View.GONE);
                        }
                    });

                } else {
                    Log.e("onPostExecute Sub_cat_id size is Zero ", "ooppss");
                    SnackbarManager.show(
                            Snackbar.with(MainActivity.this)
                                    .position(Snackbar.SnackbarPosition.TOP)
                                    .margin(15, 15)
                                    .backgroundDrawable(R.drawable.snackbar_custom_layout)
                                    .text("No Data Found"));
                }
            }



        }

    }

    private class MainCartDetailJsontask extends AsyncTask<String, Void, String> {

        boolean iserror = false;

        @Override
        protected void onPreExecute() {
            // TODO Auto-generated method stub
            //  loginprogressbar.setVisibility(View.VISIBLE);
            Log.e("******* NOW MainCartDeatilJsontask WEB SERVICE IS RUNNING *******", "YES");
            Log.e("******* NOW MainCartDeatilJsontask WEB SERVICE IS RUNNING *******", "YES");
            Log.e("User_ID onPreExecute :", "" + User_ID);

        }

        @Override
        protected String doInBackground(String... params) {
            Log.e("******* NOW MainCartDeatilJsontask IS doInBackground RUNNING *******", "YES");
            HttpClient client = new DefaultHttpClient();
            HttpPost post = new HttpPost("http://sevilla.centrocomercial.com.es/wp-content/plugins/webserv/get_cart_product.php?user_id="+User_ID);

            /*http://sevilla.centrocomercial.com.es/wp-content/plugins/webserv/get_cart_product.php?user_id=286*/
            Log.e("URL Cart Detail Main :", "" + "http://sevilla.centrocomercial.com.es/wp-content/plugins/webserv/get_cart_product.php?user_id="+User_ID);

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

                    TV_cart_badge_counter_textview.setText(Str_Get_Cart_Product_count);

                } else {
                    Log.e("onPostExecute Error ", "ooppss");
                    SnackbarManager.show(
                            Snackbar.with(MainActivity.this)
                                    .position(Snackbar.SnackbarPosition.TOP)
                                    .margin(15, 15)
                                    .backgroundDrawable(R.drawable.snackbar_custom_layout)
                                    .text("No Data Found"));
                }
            }
        }

    }




    private class CategoryAdapter extends RecyclerView.Adapter<MainActivity.CategoryAdapter.MyViewHolder> {

        private Context mContext;
        private List<BeanMainCategory> arrayList;


        class MyViewHolder extends RecyclerView.ViewHolder {
            TextView CatName;
            ImageView Catimage;
            ImageView Catimage_transparent_overlay;
            RelativeLayout Rl_loader;
            CircularProgressBar Rl_circular_loader;

            public MyViewHolder(View view) {
                super(view);
                CatName = (TextView) view.findViewById(R.id.gv_main_tv);
                Rl_circular_loader = (CircularProgressBar) view.findViewById(R.id.grid_main_item_progressbar_circular);
                Rl_loader = (RelativeLayout) view.findViewById(R.id.rl_grid_main_item_progress);
                Catimage = (ImageView) view.findViewById(R.id.gv_image);
                Catimage_transparent_overlay = (ImageView) view.findViewById(R.id.iv_tranparent_overlay);

            }
        }


        public CategoryAdapter(Context mContext, List<BeanMainCategory> arrayList) {
            this.mContext = mContext;
            this.arrayList = arrayList;
        }

        @Override
        public MainActivity.CategoryAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View itemView = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.activity_main_category_grid_items, parent, false);

            return new MainActivity.CategoryAdapter.MyViewHolder(itemView);
        }

        @Override
        public void onBindViewHolder(final MainActivity.CategoryAdapter.MyViewHolder holder, final int position) {

            holder.CatName.setText(Html.fromHtml(arrayList.get(position).getUsername()));

            ImageLoader imageLoader = ImageLoader.getInstance();
            imageLoader.getInstance().displayImage(arrayList.get(position).getImage(), holder.Catimage, new ImageLoadingListener() {
                @Override
                public void onLoadingStarted(String imageUri, View view) {
                    holder.Rl_loader.setVisibility(View.VISIBLE);
                }

                @Override
                public void onLoadingFailed(String imageUri, View view, FailReason failReason) {
                    holder.Rl_loader.setVisibility(View.GONE);
                }

                @Override
                public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {
                    holder.Rl_loader.setVisibility(View.GONE);
                }

                @Override
                public void onLoadingCancelled(String imageUri, View view) {
                    holder.Rl_loader.setVisibility(View.GONE);

                }
            });


            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int position = holder.getAdapterPosition();

//                    holder.Catimage_transparent_overlay.setVisibility(View.GONE);
                    Log.e(" Item Position :", "" + arrayList.get(holder.getLayoutPosition()));

                    Log.e(" Adapter Position :", "" + position);

                    Log.e("Main Categery id", "" + arrayList.get(position).getId());
                    MainCategory_ID = arrayList.get(position).getId();
                    Log.e("MainCategory_ID :", "" + MainCategory_ID);
                    Category_Name_for_Sub_Category = String.valueOf(Html.fromHtml(arrayList.get(position).getUsername()));

                    Log.e("Item Name", "" + String.valueOf(Html.fromHtml(arrayList.get(position).getUsername())));

                    Log.e("Item Name Category_Name_for_Sub_Category", "" + Category_Name_for_Sub_Category);

                    Log.e(" List Size :", "" + arrayList.size());

                    Intent SubCatPage = new Intent(getApplicationContext(), SubCategoryActivity.class);
                    SubCatPage.putExtra("SubCatID", arrayList.get(position).getId());
                    SubCatPage.putExtra("MainCatID", MainCategory_ID);
                    SubCatPage.putExtra("SubCatName", Category_Name_for_Sub_Category);
//                    SubCatPage.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                    MainActivity.this.startActivity(SubCatPage);
//                    finish();

                }
            });


        }


        @Override
        public int getItemCount() {
            return arrayList.size();
        }


    }


    /*this is used to set the equal space between grid items (Start)*/
                                        /*    02     */
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
    /*this is used to set the equal space between grid items (End)*/

    /*recycler data (End 03 of 03)*/


    // Initiating Menu XML file (menu.xml)
//    @Override
//    public boolean onCreateOptionsMenu(Menu menu) {
//        MenuInflater menuInflater = getMenuInflater();
//        menuInflater.inflate(R.menu.menu, menu);
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
//            case R.id.menu_search:
    // Single menu item is selected do something
    // Ex: launching new activity/screen or show alert message
//                Toast.makeText(MainActivity.this, "Search is Selected", Toast.LENGTH_SHORT).show();
//                return true;

//            case R.id.menu_cart:
//                Toast.makeText(MainActivity.this, "Cart is Selected", Toast.LENGTH_SHORT).show();
                /*Intent SubCatPage = new Intent(getApplicationContext(), MyCart.class);
                MainActivity.this.startActivity(SubCatPage);*/


//                return true;
//
//
//            default:
//                return super.onOptionsItemSelected(item);
//        }
//    }
    @Override
    public void onBackPressed() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Are you sure you want to exit?")
                .setCancelable(false)
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        moveTaskToBack(true);
                        MainActivity.this.finish();

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
