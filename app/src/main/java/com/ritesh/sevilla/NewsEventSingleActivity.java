package com.ritesh.sevilla;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.util.Log;
import android.view.HapticFeedbackConstants;
import android.view.MotionEvent;
import android.view.View;
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
import org.json.JSONObject;

import butterknife.BindView;
import butterknife.ButterKnife;
import fr.castorflex.android.circularprogressbar.CircularProgressBar;
import fr.castorflex.android.circularprogressbar.CircularProgressDrawable;

/**
 * Created by ritesh on 31/3/17.
 */
@SuppressWarnings("deprecation")
public class NewsEventSingleActivity extends AppCompatActivity {

    @BindView(R.id.toolbar_news_event_single)
    Toolbar TB_news_event_single;


    @BindView(R.id.rl_cart_icon_news_event_single)
    RelativeLayout RL_cart_icon_news_event_single;

    @BindView(R.id.rl_badgeview_cart_item_news_event_single)
    RelativeLayout RL_badgeview_cart_item_news_event_single;

    @BindView(R.id.rl_cart_icon_news_event_single_click)
    RelativeLayout RL_cart_icon_news_event_single_click;

    @BindView(R.id.rl_badgeview_cart_item_news_event_single_click)
    RelativeLayout RL_badgeview_cart_item_news_event_single_click;


    @BindView(R.id.rl_news_event_single_image_progress)
    RelativeLayout RL_news_event_single_image_progress;

    @BindView(R.id.rl_news_event_single_progress_main)
    RelativeLayout RL_news_event_single_progress_main;

    @BindView(R.id.tv_badge_counter_news_event_single_click)
    TextView TV_badge_counter_news_event_single_click;

    @BindView(R.id.tv_badge_counter_news_event_single)
    TextView TV_badge_counter_news_event_single;

    @BindView(R.id.tv_news_event_single_tittle)
    TextView TV_news_event_single_tittle;

    @BindView(R.id.tv_news_event_single_date)
    TextView TV_news_event_single_date;

    @BindView(R.id.tv_news_event_single_description)
    TextView TV_news_event_single_description;

    @BindView(R.id.iv_news_event_single)
    ImageView IV_news_event_single;

    CircularProgressBar mCircularProgressBarNews_event_single;
    CircularProgressBar mCircularProgressBarNews_event_single_main;


    String
            User_ID = "",
            Str_Get_Cart_Detail_Status = "",
            Str_Get_Cart_result = "",
            Str_Get_Cart_Deatil_User_ID = "",
            Str_Get_Cart_Product_count = "",
            Str_Get_CartCount_Shared = "",
            Str_Get_SingleEventStatus = "",
            Event_ID = "",
            Event_ID_Recived = "",
            Str_Get_SingleEvent_ID = "",
            Str_Get_SingleEvent_Title = "",
            Str_Get_SingleEvent_Description = "",
            Str_Get_SingleEvent_Date = "",
            Str_Get_SingleEvent_Image = "",
            Str_Get_SingleEvent_Result = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_news_event_single);
        ButterKnife.bind(this);
        Appconstant.sh = getSharedPreferences(Appconstant.MyPREFERENCES, Context.MODE_PRIVATE);
        User_ID = Appconstant.sh.getString("id", null);
        Log.e("User_ID from SharedPref :", "" + User_ID);

        Event_ID_Recived = getIntent().getStringExtra("NewsEventID");
        Log.e("Event_ID_Recived From News Event List Activity :", "" + Event_ID_Recived);




        mCircularProgressBarNews_event_single_main = (CircularProgressBar) findViewById(R.id.cpb_news_event_single_progressbar_circular_main);
        mCircularProgressBarNews_event_single = (CircularProgressBar) findViewById(R.id.cpb_news_event_single_image_progressbar_circular);
//        signupProgress.setVisibility(View.GONE);
        ((CircularProgressDrawable) mCircularProgressBarNews_event_single.getIndeterminateDrawable()).start();
        ((CircularProgressDrawable) mCircularProgressBarNews_event_single_main.getIndeterminateDrawable()).start();
        updateValuesmain();
        updateValues();


        DisplayImageOptions defaultOptionss = new DisplayImageOptions.Builder()
                .cacheInMemory(true)
                .cacheOnDisk(true)
                .build();
        ImageLoaderConfiguration configs = new ImageLoaderConfiguration
                .Builder(NewsEventSingleActivity.this)
                .defaultDisplayImageOptions(defaultOptionss)
                .build();
        ImageLoader.getInstance().init(configs);




        if (Utils.isConnected(getApplicationContext())) {
            Log.e("NewsEventSingleCartDetailJsontask Call :", "OK");
            NewsEventSingleCartDetailJsontask task = new NewsEventSingleCartDetailJsontask();
            task.execute();
        } else {

            SnackbarManager.show(
                    Snackbar.with(NewsEventSingleActivity.this)
                            .position(Snackbar.SnackbarPosition.TOP)
                            .margin(15, 15)
                            .backgroundDrawable(R.drawable.snackbar_custom_layout)
                            .text("Please Your Internet Connectivity..!!"));

        }








        if (Utils.isConnected(getApplicationContext())) {
            Log.e("NewsEventSingleEventJsontask Call :", "OK");
            NewsEventSingleEventJsontask task = new NewsEventSingleEventJsontask();
            task.execute();
        } else {

            SnackbarManager.show(
                    Snackbar.with(NewsEventSingleActivity.this)
                            .position(Snackbar.SnackbarPosition.TOP)
                            .margin(15, 15)
                            .backgroundDrawable(R.drawable.snackbar_custom_layout)
                            .text("Please Your Internet Connectivity..!!"));

        }



        RL_cart_icon_news_event_single.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {

                if (event.getAction() == MotionEvent.ACTION_DOWN) {

                    Log.e("Action ", "Down");
                    RL_cart_icon_news_event_single_click.setVisibility(View.VISIBLE);
                    RL_cart_icon_news_event_single.setVisibility(View.GONE);

                    RL_badgeview_cart_item_news_event_single_click.setVisibility(View.VISIBLE);
                    RL_badgeview_cart_item_news_event_single.setVisibility(View.GONE);
                    v.performHapticFeedback(HapticFeedbackConstants.VIRTUAL_KEY);

//                    Toast.makeText(getApplicationContext(), "Add to cart Clicked", Toast.LENGTH_SHORT).show();
                    return true;
                }

                if (event.getAction() == MotionEvent.ACTION_MOVE) {

                    Log.e("Action ", "Move");
                    RL_cart_icon_news_event_single_click.setVisibility(View.VISIBLE);
                    RL_cart_icon_news_event_single.setVisibility(View.GONE);

                    RL_badgeview_cart_item_news_event_single_click.setVisibility(View.VISIBLE);
                    RL_badgeview_cart_item_news_event_single.setVisibility(View.GONE);

                    return true;

                }
                if (event.getAction() == MotionEvent.ACTION_UP) {

                    Log.e("Action ", "Up");

                    RL_cart_icon_news_event_single_click.setVisibility(View.GONE);
                    RL_cart_icon_news_event_single.setVisibility(View.VISIBLE);

                    RL_badgeview_cart_item_news_event_single_click.setVisibility(View.GONE);
                    RL_badgeview_cart_item_news_event_single.setVisibility(View.VISIBLE);


                    Intent MyCartPage = new Intent(getApplicationContext(), MyCartActivity.class);
                    NewsEventSingleActivity.this.startActivity(MyCartPage);

                    return true;
                }


                return false;
            }
        });



        

    }


    @Override
    protected void onStart() {
        super.onStart();
        Log.e("NewsEventSingle Activity lifecycle", "onStart invoked");
    }


    @Override
    protected void onResume() {
        super.onResume();
        Log.e("NewsEventSingle Activity lifecycle", "onResume invoked");

    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.e("NewsEventSingle Activity lifecycle", "onPause invoked");
    }

    @Override
    protected void onStop() {
        super.onStop();
        Log.e("NewsEventSingle Activity lifecycle", "onStop invoked");
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        Log.e("NewsEventSingle Activity lifecycle", "onRestart invoked");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.e("NewsEventSingle Activity lifecycle", "onDestroy invoked");
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
        mCircularProgressBarNews_event_single.setIndeterminateDrawable(circularProgressDrawable = b.build());

        // /!\ Terrible hack, do not do this at home!
        circularProgressDrawable.setBounds(0,
                0,
                mCircularProgressBarNews_event_single.getWidth(),
                mCircularProgressBarNews_event_single.getHeight());
        mCircularProgressBarNews_event_single.setVisibility(View.INVISIBLE);
        mCircularProgressBarNews_event_single.setVisibility(View.VISIBLE);
    }

    private void updateValuesmain() {
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
        mCircularProgressBarNews_event_single_main.setIndeterminateDrawable(circularProgressDrawable = b.build());

        // /!\ Terrible hack, do not do this at home!
        circularProgressDrawable.setBounds(0,
                0,
                mCircularProgressBarNews_event_single_main.getWidth(),
                mCircularProgressBarNews_event_single_main.getHeight());
        mCircularProgressBarNews_event_single_main.setVisibility(View.INVISIBLE);
        mCircularProgressBarNews_event_single_main.setVisibility(View.VISIBLE);
    }


    private class NewsEventSingleCartDetailJsontask extends AsyncTask<String, Void, String> {

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
            Log.e("URL Cart Detail News Event SIngle :", "" + "http://sevilla.centrocomercial.com.es/wp-content/plugins/webserv/get_cart_product.php?user_id="+User_ID);

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

                    TV_badge_counter_news_event_single.setText(Html.fromHtml(Str_Get_Cart_Product_count));
                    TV_badge_counter_news_event_single_click.setText(Html.fromHtml(Str_Get_Cart_Product_count));

                } else {
                    Log.e("onPostExecute Error ", "ooppss");
                    SnackbarManager.show(
                            Snackbar.with(NewsEventSingleActivity.this)
                                    .position(Snackbar.SnackbarPosition.TOP)
                                    .margin(15, 15)
                                    .backgroundDrawable(R.drawable.snackbar_custom_layout)
                                    .text("No Data Found"));
                }
            }
        }

    }


    private class NewsEventSingleEventJsontask extends AsyncTask<String, Void, String> {

        boolean iserror = false;

        @Override
        protected void onPreExecute() {
            // TODO Auto-generated method stub
            //  loginprogressbar.setVisibility(View.VISIBLE);
            Log.e("******* NOW NewsEventSingleEventJsontask WEB SERVICE IS RUNNING *******", "YES");
            Log.e("******* NOW NewsEventSingleEventJsontask WEB SERVICE IS RUNNING *******", "YES");
            Log.e("Event_ID_Recived onPreExecute :", "" + Event_ID_Recived);

            RL_news_event_single_progress_main.setVisibility(View.VISIBLE);
//            RL_news_event_single_image_progress.setVisibility(View.VISIBLE);

        }

        @Override
        protected String doInBackground(String... params) {
            Log.e("******* NOW NewsEventSingleEventJsontask IS doInBackground RUNNING *******", "YES");
            HttpClient client = new DefaultHttpClient();
            HttpPost post = new HttpPost("http://sevilla.centrocomercial.com.es/wp-content/plugins/webserv/single_event.php?event_id=" + Event_ID_Recived);

            /*http://sevilla.centrocomercial.com.es/wp-content/plugins/webserv/get_cart_product.php?user_id=286*/
            Log.e("URL Cart Detail Main :", "" + "http://sevilla.centrocomercial.com.es/wp-content/plugins/webserv/single_event.php?event_id=" + Event_ID_Recived);

            try {
                HttpResponse response = client.execute(post);
                String CartDetailobject = EntityUtils.toString(response.getEntity());
                Log.e("*******SingleEvent Detail object******** :", "" + CartDetailobject);

                //JSONArray js = new JSONArray(object);
                JSONObject jobect = new JSONObject(CartDetailobject);
                Str_Get_SingleEventStatus = jobect.getString("status");
                if (Str_Get_SingleEventStatus.equalsIgnoreCase("OK")) {
                    Str_Get_SingleEvent_ID = jobect.getString("event_id");
                    Str_Get_SingleEvent_Title = jobect.getString("event_title");
                    Str_Get_SingleEvent_Description = jobect.getString("description");
                    Str_Get_SingleEvent_Date = jobect.getString("eventDate");
                    Str_Get_SingleEvent_Image = jobect.getString("event_image");
                    Str_Get_SingleEvent_Result = jobect.getString("event_result");

                }

            } catch (Exception e) {
                Log.v("22", "22" + e.getMessage());
                e.printStackTrace();
                iserror = true;
            }

            return Str_Get_SingleEventStatus;
        }

        @Override
        protected void onPostExecute(String result1) {
            // TODO Auto-generated method stub
            super.onPostExecute(result1);
            RL_news_event_single_progress_main.setVisibility(View.GONE);

            if (!iserror) {
                if (Str_Get_SingleEventStatus.equalsIgnoreCase("OK")) {

                    Log.e("Str_Get_Cart_Deatil_User_ID :", "" + Str_Get_SingleEvent_ID);
                    Log.e("Str_Get_SingleEvent_Title :", "" + Str_Get_SingleEvent_Title);
                    Log.e("Str_Get_SingleEvent_Description :", "" + Str_Get_SingleEvent_Description);
                    Log.e("Str_Get_SingleEvent_Date :", "" + Str_Get_SingleEvent_Date);
                    Log.e("Str_Get_SingleEvent_Image :", "" + Str_Get_SingleEvent_Image);
                    Log.e("Str_Get_SingleEvent_Result :", "" + Str_Get_SingleEvent_Result);

                    /**************** Start Animation **************  **/
                    YoYo.with(Techniques.Wobble)
                            .duration(700)
                            .playOn(RL_cart_icon_news_event_single);
                    YoYo.with(Techniques.Wobble)
                            .duration(700)
                            .playOn(RL_badgeview_cart_item_news_event_single);
                    /**************** End Animation ****************/

                    TV_news_event_single_tittle.setText(Str_Get_SingleEvent_Title);
                    TV_news_event_single_description.setText(Str_Get_SingleEvent_Description);
                    TV_news_event_single_date.setText(Str_Get_SingleEvent_Date);


                    ImageLoader imageLoader = ImageLoader.getInstance();
                    imageLoader.getInstance().displayImage(Str_Get_SingleEvent_Image, IV_news_event_single, new ImageLoadingListener() {
                        @Override
                        public void onLoadingStarted(String imageUri, View view) {
                            RL_news_event_single_image_progress.setVisibility(View.VISIBLE);
                        }

                        @Override
                        public void onLoadingFailed(String imageUri, View view, FailReason failReason) {
                            RL_news_event_single_image_progress.setVisibility(View.GONE);

                            Log.e("image not found :", "ok");
                        }

                        @Override
                        public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {
                            RL_news_event_single_image_progress.setVisibility(View.GONE);
                        }

                        @Override
                        public void onLoadingCancelled(String imageUri, View view) {
                            RL_news_event_single_image_progress.setVisibility(View.GONE);

                        }
                    });


                } else {
                    Log.e("onPostExecute Error ", "ooppss");
                    SnackbarManager.show(
                            Snackbar.with(NewsEventSingleActivity.this)
                                    .position(Snackbar.SnackbarPosition.TOP)
                                    .margin(15, 15)
                                    .backgroundDrawable(R.drawable.snackbar_custom_layout)
                                    .text("No Data Found"));
                }
            }
        }

    }


}
