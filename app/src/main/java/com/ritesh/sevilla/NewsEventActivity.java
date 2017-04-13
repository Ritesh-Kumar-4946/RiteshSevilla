package com.ritesh.sevilla;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Rect;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.Html;
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
import com.ritesh.sevilla.Beans.BeanNewsEvent;
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
 * Created by ritesh on 30/3/17.
 */
@SuppressWarnings("deprecation")
public class NewsEventActivity extends AppCompatActivity {

    @BindView(R.id.toolbar_news_event)
    Toolbar TB_news_event;

    String
            User_ID = "",
            EventID_send = "",
            Str_Get_CartCount_Shared = "",
            Str_Get_Cart_Detail_Status = "",
            Str_Get_Cart_result = "",
            Str_Get_Status = "",
            Str_Get_Cart_Deatil_User_ID = "",
            Str_Get_Cart_Product_count = "",
            StatusNews_Event = "",
            Str_News_Event_list_tittle = "",
            Str_News_Event_list_description = "",
            Str_News_Event_list_date = "",
            Str_News_Event_list_image = "",
            Str_News_Event_list_id = "",
            SubCategoryID = "",
            SubCategoryName = "",
            NewsEventList_Result = "",
            Sub_Catagories_result = "",
            Sub_Category_Id = "",
    Sub_Category_Name_decoded = "",
            Obj_decoded = "",
            Sub_Category_Img_Src = "",
            Sub_Category_Name_for_product_list = "";

    List<NewsEventActivity> News_Event_rowItems;
    private ArrayList<String> News_Event_id;
    private ArrayList<String> News_Event_tittles;
    private ArrayList<String> News_Event_Description;
    private ArrayList<String> News_Event_date;
    private ArrayList<String> News_Event_images;
    List<BeanNewsEvent> beanNewsEvents0;


    @BindView(R.id.rl_cart_icon_news_event)
    RelativeLayout RL_cart_icon_news_event;

    @BindView(R.id.rl_badgeview_cart_item_news_event)
    RelativeLayout RL_badgeview_cart_item_news_event;


    @BindView(R.id.rl_cart_icon_news_event_click)
    RelativeLayout RL_cart_icon_news_event_click;

    @BindView(R.id.rl_badgeview_cart_item_news_event_click)
    RelativeLayout RL_badgeview_cart_item_news_event_click;

    @BindView(R.id.tv_badge_counter_news_event)
    TextView TV_badge_counter_news_event;


    @BindView(R.id.rl_gridview_news_event_progress)
    RelativeLayout RL_gridview_news_event_progress;

    CircularProgressBar mCircularProgressBarGridview_news_event;

    @BindView(R.id.recycler_view_news_event)
    RecyclerView RV_news_event;

    RecyclerView.LayoutManager mLayoutManager;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_news_event);
        ButterKnife.bind(this);
        Appconstant.sh = getSharedPreferences(Appconstant.MyPREFERENCES, Context.MODE_PRIVATE);
        User_ID = Appconstant.sh.getString("id", null);
        Log.e("User_ID from SharedPref :", "" + User_ID);


        if (Utils.isConnected(getApplicationContext())) {
            NewsEventListCartDetailJsontask task = new NewsEventListCartDetailJsontask();
            task.execute();
        } else {

            SnackbarManager.show(
                    Snackbar.with(NewsEventActivity.this)
                            .position(Snackbar.SnackbarPosition.TOP)
                            .margin(15, 15)
                            .backgroundDrawable(R.drawable.snackbar_custom_layout)
                            .text("Please Your Internet Connectivity..!!"));

        }



        DisplayImageOptions defaultOptionss = new DisplayImageOptions.Builder()
                .cacheInMemory(true)
                .cacheOnDisk(true)
                .build();
        ImageLoaderConfiguration configs = new ImageLoaderConfiguration
                .Builder(NewsEventActivity.this)
                .defaultDisplayImageOptions(defaultOptionss)
                .build();
        ImageLoader.getInstance().init(configs);
        /*image settin by universal image loader */

         /*circular progress bar (Start)*/
        mCircularProgressBarGridview_news_event = (CircularProgressBar) findViewById(R.id.gridview_news_event_progressbar_circular);
//        signupProgress.setVisibility(View.GONE);
        ((CircularProgressDrawable) mCircularProgressBarGridview_news_event.getIndeterminateDrawable()).start();
        updateValues();
        /*circular progress bar (End)*/

        setSupportActionBar(TB_news_event);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);



        TB_news_event.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed(); // Implemented by activity
            }
        });

        News_Event_rowItems = new ArrayList<NewsEventActivity>();
        News_Event_id = new ArrayList<>();
        News_Event_tittles = new ArrayList<>();
        News_Event_Description = new ArrayList<>();
        News_Event_date = new ArrayList<>();
        News_Event_images = new ArrayList<>();

        RV_news_event.setHasFixedSize(true);

        mLayoutManager = new LinearLayoutManager(this);
        RV_news_event.setLayoutManager(mLayoutManager);
        RV_news_event.addItemDecoration(new NewsEventActivity.EqualSpaceItemDecoration(15));



        if (Utils.isConnected(getApplicationContext())) {
            NewsEventJsontask task = new NewsEventJsontask();
            task.execute();
        } else {

            SnackbarManager.show(
                    Snackbar.with(NewsEventActivity.this)
                            .position(Snackbar.SnackbarPosition.TOP)
                            .margin(15, 15)
                            .backgroundDrawable(R.drawable.snackbar_custom_layout)
                            .text("Please Your Internet Connectivity..!!"));

        }



        RL_cart_icon_news_event.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {

                if (event.getAction() == MotionEvent.ACTION_DOWN) {

                    Log.e("Action ", "Down");
                    RL_cart_icon_news_event_click.setVisibility(View.VISIBLE);
                    RL_cart_icon_news_event.setVisibility(View.GONE);

                    RL_badgeview_cart_item_news_event_click.setVisibility(View.VISIBLE);
                    RL_badgeview_cart_item_news_event.setVisibility(View.GONE);
                    v.performHapticFeedback(HapticFeedbackConstants.VIRTUAL_KEY);

//                    Toast.makeText(getApplicationContext(), "Add to cart Clicked", Toast.LENGTH_SHORT).show();
                    return true;
                }

                if (event.getAction() == MotionEvent.ACTION_MOVE) {

                    Log.e("Action ", "Move");
                    RL_cart_icon_news_event_click.setVisibility(View.VISIBLE);
                    RL_cart_icon_news_event.setVisibility(View.GONE);

                    RL_badgeview_cart_item_news_event_click.setVisibility(View.VISIBLE);
                    RL_badgeview_cart_item_news_event.setVisibility(View.GONE);

                    return true;

                }
                if (event.getAction() == MotionEvent.ACTION_UP) {

                    Log.e("Action ", "Up");

                    RL_cart_icon_news_event_click.setVisibility(View.GONE);
                    RL_cart_icon_news_event.setVisibility(View.VISIBLE);

                    RL_badgeview_cart_item_news_event_click.setVisibility(View.GONE);
                    RL_badgeview_cart_item_news_event.setVisibility(View.VISIBLE);


                    Intent MyCartPage = new Intent(getApplicationContext(), MyCartActivity.class);
                    NewsEventActivity.this.startActivity(MyCartPage);

                    return true;
                }


                return false;
            }
        });



    }


    private class NewsEventJsontask extends AsyncTask<String, Void, List<BeanNewsEvent>> {

        boolean iserror = false;
        String result = " ";

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            RL_gridview_news_event_progress.setVisibility(View.VISIBLE);
            Log.e(" ************ UserListData AsyncTask Start ************ :", "yes");
            Log.e("Sub_Catagories ID:", "" + SubCategoryID);
        }

        @Override
        protected List<BeanNewsEvent> doInBackground(String... params) {
            HttpClient client = new DefaultHttpClient();
            HttpPost post = new HttpPost("http://sevilla.centrocomercial.com.es/wp-content/plugins/webserv/event_list.php");

            try {
                Log.e(" ****** EVENT NEWS URL :", "http://sevilla.centrocomercial.com.es/wp-content/plugins/webserv/event_list.php");

                HttpResponse response = client.execute(post);
                String objSubCategory = EntityUtils.toString(response.getEntity());

                beanNewsEvents0 = new ArrayList<>();

                JSONObject jsonObject = new JSONObject(objSubCategory);
                StatusNews_Event = jsonObject.getString("status");
                NewsEventList_Result = jsonObject.getString("result");

                Log.e("************Json NewsEventList_Result data*******************", " " + NewsEventList_Result);

                if (StatusNews_Event.equals("OK")) {

                    Log.e("doInBackground StatusNews_Event is", "OK");
                    JSONArray jaaray = new JSONArray(NewsEventList_Result);
                    for (int i = 0; i < jaaray.length(); i++) {
                        Sub_Catagories_result = jaaray.getJSONObject(i).getString("event_result");

                        Log.e("Sub_Catagories_result :", "" + Sub_Catagories_result);

                        if (Sub_Catagories_result.equalsIgnoreCase("successfull")) {

                            Log.e("doInBackground Sub_Catagories_result is", "successfull");

                            BeanNewsEvent beanNewsEvent = new BeanNewsEvent();
                            beanNewsEvent.setEventNewsId(jaaray.getJSONObject(i).getString("event_id"));
                            beanNewsEvent.setEventNewsTittle(jaaray.getJSONObject(i).getString("event_title"));
                            beanNewsEvent.setEventNewsDescription(jaaray.getJSONObject(i).getString("description"));
                            beanNewsEvent.setEventNewsDate(jaaray.getJSONObject(i).getString("eventDate"));
                            beanNewsEvent.setEventNewsImage(jaaray.getJSONObject(i).getString("event_image"));

                            beanNewsEvents0.add(beanNewsEvent);

                            Str_News_Event_list_id = jaaray.getJSONObject(i).getString("event_id");
                            Str_News_Event_list_tittle = jaaray.getJSONObject(i).getString("event_title");
                            Str_News_Event_list_description = jaaray.getJSONObject(i).getString("description");
                            Str_News_Event_list_date = jaaray.getJSONObject(i).getString("eventDate");
                            Str_News_Event_list_image = jaaray.getJSONObject(i).getString("event_image");


                            News_Event_id.add(Str_News_Event_list_id);
                            News_Event_tittles.add(Str_News_Event_list_tittle);
                            News_Event_Description.add(Str_News_Event_list_description);
                            News_Event_date.add(Str_News_Event_list_date);
                            News_Event_images.add(Str_News_Event_list_image);



                            Log.e("News_Event_list_id :", "" + Str_News_Event_list_id);
                            Log.e("News_Event_list_tittle :", "" + Str_News_Event_list_tittle);
                            Log.e("News_Event_list_description :", "" + Str_News_Event_list_description);
                            Log.e("News_Event_list_date :", "" + Str_News_Event_list_date);
                            Log.e("News_Event_list_image :", "" + Str_News_Event_list_image);

                        } else {
                            Log.e("No Data Found :", "OOppss");
                        }
                    }
                } else if (!StatusNews_Event.equals("OK")) {
                    Log.e("No Data Found", "OK");
                }


            } catch (Exception e) {
                System.out.println("Errror in gettting by ID" + e);
                Log.e("22", "22" + e.getMessage());
                e.printStackTrace();
                iserror = true;
            }
            return beanNewsEvents0;
        }

        @Override
        protected void onPostExecute(List<BeanNewsEvent> myNewsEventstring) {
            super.onPostExecute(myNewsEventstring);
            RL_gridview_news_event_progress.setVisibility(View.GONE);

            if (StatusNews_Event.equals("OK")) {

                Log.e("onPostExecute StatusNews_Event is", "OK");

                    if (News_Event_id.size() > 0) {
                        Log.e("onPostExecute News_Event_id size is not Zero ", "wow");
                        Log.e(" ********** News_Event_id ********** ", "" + News_Event_id);

                        /**************** Start Animation **************  **/
                        YoYo.with(Techniques.Wobble)
                                .duration(700)
                                .playOn(RL_cart_icon_news_event);
                        YoYo.with(Techniques.Wobble)
                                .duration(700)
                                .playOn(RL_badgeview_cart_item_news_event);
                        /**************** End Animation ****************/


                        NewsEventAdapter newsEventAdapter = new NewsEventAdapter(NewsEventActivity.this, myNewsEventstring);
                        RV_news_event.setAdapter(newsEventAdapter);


                        Log.e(" ********** sub_category_.size() > 0 **********", "YES");
                        Log.e(" ********** sub_category_.size() > 0 **********", "YES");
                        Log.e(" ********** sub_category_.size() > 0 **********", "YES");
                    } else {
                        Log.e("onPostExecute Sub_cat_id size is Zero ", "ooppss");
                        SnackbarManager.show(
                                Snackbar.with(NewsEventActivity.this)
                                        .position(Snackbar.SnackbarPosition.TOP)
                                        .margin(15, 15)
                                        .backgroundDrawable(R.drawable.snackbar_custom_layout)
                                        .text("No Data Found"));
                    }



            } else if (!StatusNews_Event.equals("OK")) {
                Log.e("onPostExecute StatusSubCategory is Not Equal to :", "OK");
                SnackbarManager.show(
                        Snackbar.with(NewsEventActivity.this)
                                .position(Snackbar.SnackbarPosition.TOP)
                                .margin(15, 15)
                                .backgroundDrawable(R.drawable.snackbar_custom_layout)
                                .text("No Data Found"));
            }
        }

    }



    private class NewsEventListCartDetailJsontask extends AsyncTask<String, Void, String> {

        boolean iserror = false;

        @Override
        protected void onPreExecute() {
            // TODO Auto-generated method stub
            //  loginprogressbar.setVisibility(View.VISIBLE);
            Log.e("******* NOW NewsEventListCartDetailJsontask WEB SERVICE IS RUNNING *******", "YES");
            Log.e("******* NOW NewsEventListCartDetailJsontask WEB SERVICE IS RUNNING *******", "YES");
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

                    TV_badge_counter_news_event.setText(Html.fromHtml(Str_Get_Cart_Product_count));

                } else {
                    Log.e("onPostExecute Error ", "ooppss");
                    SnackbarManager.show(
                            Snackbar.with(NewsEventActivity.this)
                                    .position(Snackbar.SnackbarPosition.TOP)
                                    .margin(15, 15)
                                    .backgroundDrawable(R.drawable.snackbar_custom_layout)
                                    .text("No Data Found"));
                }
            }
        }

    }




    private class NewsEventAdapter extends RecyclerView.Adapter<NewsEventActivity.NewsEventAdapter.MyViewHolder> {

        private Context mContext;
        private List<BeanNewsEvent> newseventarrayList;


        class MyViewHolder extends RecyclerView.ViewHolder {
            TextView NewsEventTittle;
            TextView NewsEventDate;
            TextView NewsEventDiscription;
            ImageView NewsEventimage;
            RelativeLayout NewsEventRl_Imageloader;
            CircularProgressBar NewsEventImageCPB_circular_loader;

            public MyViewHolder(View view) {
                super(view);
                NewsEventTittle = (TextView) view.findViewById(R.id.tv_news_event_items_name);
                NewsEventDiscription = (TextView) view.findViewById(R.id.tv_news_event_item_description);
                NewsEventDate = (TextView) view.findViewById(R.id.tv_news_event_items_date);
                NewsEventImageCPB_circular_loader = (CircularProgressBar) view.findViewById(R.id.cpb_news_event_items_image_progressbar_circular);
                NewsEventRl_Imageloader = (RelativeLayout) view.findViewById(R.id.rl_news_event_items_image_progress);
                NewsEventimage = (ImageView) view.findViewById(R.id.iv_news_event_items);

            }

        }


        public NewsEventAdapter(Context mContext, List<BeanNewsEvent> newseventarrayList) {
            this.mContext = mContext;
            this.newseventarrayList = newseventarrayList;
        }


        @Override
        public MyViewHolder onCreateViewHolder
                (ViewGroup parent, int viewType) {
            View itemView = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.activity_news_event_list_items, parent, false);

            return new MyViewHolder(itemView);
        }


        @Override
        public void onBindViewHolder(final NewsEventActivity.NewsEventAdapter.MyViewHolder holder, final int position) {

            holder.NewsEventTittle.setText(Html.fromHtml(newseventarrayList.get(position).getEventNewsTittle()));
            holder.NewsEventDiscription.setText(Html.fromHtml(newseventarrayList.get(position).getEventNewsDescription()));
            holder.NewsEventDate.setText(Html.fromHtml(newseventarrayList.get(position).getEventNewsDate()));

            ImageLoader imageLoader = ImageLoader.getInstance();
            imageLoader.getInstance().displayImage(newseventarrayList.get(position).getEventNewsImage(), holder.NewsEventimage, new ImageLoadingListener() {

                @Override
                public void onLoadingStarted(String imageUri, View view) {
                    holder.NewsEventRl_Imageloader.setVisibility(View.VISIBLE);
                }

                @Override
                public void onLoadingFailed(String imageUri, View view, FailReason failReason) {
                    holder.NewsEventRl_Imageloader.setVisibility(View.GONE);

                    Log.e("image not found :", "ok");
                }

                @Override
                public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {
                    holder.NewsEventRl_Imageloader.setVisibility(View.GONE);
                }

                @Override
                public void onLoadingCancelled(String imageUri, View view) {
                    holder.NewsEventRl_Imageloader.setVisibility(View.GONE);

                }
            });


            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int position = holder.getAdapterPosition();

                    Log.e(" Item Position :", "" + newseventarrayList.get(holder.getLayoutPosition()));

                    Log.e(" Adapter Position :", "" + position);

                    Log.e("Item id", "" + newseventarrayList.get(position).getEventNewsId());
                    EventID_send = newseventarrayList.get(position).getEventNewsId();
                    Log.e("EventID_send :", "" + EventID_send);

                    Sub_Category_Name_for_product_list = String.valueOf(Html.fromHtml(newseventarrayList.get(position).getEventNewsTittle()));

                    Log.e("Item Name", "" + String.valueOf(Html.fromHtml(newseventarrayList.get(position).getEventNewsTittle())));

                    Log.e("Item Name Category_Name_for_Sub_Category", "" + Sub_Category_Name_for_product_list);

                    Log.e(" List Size :", "" + newseventarrayList.size());


                    Intent GoNewsEventSinglePage = new Intent(getApplicationContext(), NewsEventSingleActivity.class);
                    GoNewsEventSinglePage.putExtra("NewsEventID", EventID_send);
                    NewsEventActivity.this.startActivity(GoNewsEventSinglePage);


                }
            });

        }

        @Override
        public int getItemCount() {
            return newseventarrayList.size();
        }


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
        mCircularProgressBarGridview_news_event.setIndeterminateDrawable(circularProgressDrawable = b.build());

        // /!\ Terrible hack, do not do this at home!
        circularProgressDrawable.setBounds(0, 0,
                mCircularProgressBarGridview_news_event.getWidth(),
                mCircularProgressBarGridview_news_event.getHeight());
        mCircularProgressBarGridview_news_event.setVisibility(View.INVISIBLE);
        mCircularProgressBarGridview_news_event.setVisibility(View.VISIBLE);
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
        }

    }


}
