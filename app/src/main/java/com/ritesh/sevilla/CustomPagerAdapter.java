package com.ritesh.sevilla;

import android.content.Context;
import android.os.Parcelable;
import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import java.util.ArrayList;

/**
 * Created by ritesh on 15/2/17.
 */

public class CustomPagerAdapter extends PagerAdapter {

    private ArrayList<Integer> IMAGES;
    private LayoutInflater inflater;
    private Context context;


    public CustomPagerAdapter(Context context, ArrayList<Integer> IMAGES) {
        this.context = context;
        this.IMAGES = IMAGES;
        inflater = LayoutInflater.from(context);
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((View) object);
    }

    @Override
    public int getCount() {
        return IMAGES.size();
    }

    @Override
    public Object instantiateItem(ViewGroup view, final int position) {
        View imageLayout = inflater.inflate(R.layout.pager_item, view, false);

        assert imageLayout != null;
        final ImageView imageView = (ImageView) imageLayout .findViewById(R.id.image);

        imageView.setImageResource(IMAGES.get(position));

        view.addView(imageLayout, 0);

        imageView.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                // Perform action on click

                Toast.makeText(context.getApplicationContext(), "Home Clicked :" + "" + position, Toast.LENGTH_SHORT).show();

            }
        });

        return imageLayout;
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view.equals(object);
    }

    @Override
    public void restoreState(Parcelable state, ClassLoader loader) {
    }

    @Override
    public Parcelable saveState() {
        return null;
    }


}