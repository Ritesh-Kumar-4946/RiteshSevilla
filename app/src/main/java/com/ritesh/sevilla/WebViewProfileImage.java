package com.ritesh.sevilla;

import android.app.ProgressDialog;
import android.graphics.Bitmap;
import android.graphics.PointF;
import android.graphics.RectF;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;
import com.sdsmdg.tastytoast.TastyToast;

import butterknife.BindView;
import butterknife.ButterKnife;

public class WebViewProfileImage extends AppCompatActivity {


    /*  @BindView(R.id.webPhotos)
      ImageView web;*/
    @BindView(R.id.imgprofile)
    TouchImageView imageProfile;


    //  TouchImageView image;
    private ProgressDialog dialog;
    String UrlP = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        dialog = new ProgressDialog(this);
        dialog.setIndeterminate(true);
        dialog.setCancelable(false);
        dialog.setMessage("Loading Image");

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_webview_profileimage);
        ButterKnife.bind(this);

        //  image = (TouchImageView) findViewById(R.id.img);

        DisplayImageOptions defaultOptions = new DisplayImageOptions.Builder().cacheInMemory(true).cacheOnDisk(true).build();
        ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(this).defaultDisplayImageOptions(defaultOptions).build();
        ImageLoader.getInstance().init(config);

        if ( UrlP.contains("Empty")) {
            TastyToast.makeText(getApplicationContext(), "Image Not Found", TastyToast.LENGTH_LONG,
                    TastyToast.INFO);
        } else {
            Bundle extra = getIntent().getExtras();
            UrlP = extra.get("URLP").toString();    // URL for profile image

            Log.e(" ********** profile image URL **********", "" + UrlP);
            Log.e(" ********** profile image URL **********", "" + UrlP);
            Log.e(" ********** profile image URL **********", "" + UrlP);
        }



        /**********************************
         * for performing Touchimage ZOOM IN ZOOM OUT TouchimageView.java file is required */

        imageProfile.setOnTouchImageViewListener(new TouchImageView.OnTouchImageViewListener() {
            @Override
            public void onMove() {
                PointF point = imageProfile.getScrollPosition();
                RectF rect = imageProfile.getZoomedRect();
                float currentZoom = imageProfile.getCurrentZoom();
                boolean isZoomed = imageProfile.isZoomed();
            }
        });

        ImageLoader imageLoader = ImageLoader.getInstance();
        imageLoader.getInstance().displayImage(UrlP, imageProfile, new ImageLoadingListener() {
            @Override
            public void onLoadingStarted(String imageUri, View view) {
                dialog.show();
            }

            @Override
            public void onLoadingFailed(String imageUri, View view, FailReason failReason) {
                dialog.dismiss();
            }

            @Override
            public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {
                dialog.dismiss();
            }

            @Override
            public void onLoadingCancelled(String imageUri, View view) {
                dialog.dismiss();
            }
        }); // Default options will be used


    }
}
