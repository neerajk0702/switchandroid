package com.kredivation.switchland.adapters;

import android.content.Context;
import android.os.Parcelable;
import androidx.viewpager.widget.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;


import com.kredivation.switchland.R;
import com.kredivation.switchland.model.Homegallery;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;


public class SlidingImage_Adapter_For_ItemDetails extends PagerAdapter {


    private ArrayList<Homegallery> hImagList;
    private LayoutInflater inflater;
    private Context context;


    public SlidingImage_Adapter_For_ItemDetails(Context context, ArrayList<Homegallery> hImagList) {
        this.context = context;
        this.hImagList = hImagList;
        inflater = LayoutInflater.from(context);
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((View) object);
    }

    @Override
    public int getCount() {
        return hImagList.size();
    }

    @Override
    public Object instantiateItem(ViewGroup view, int position) {
        View imageLayout = inflater.inflate(R.layout.sliding_image_for_item_details, view, false);

        assert imageLayout != null;
        final ImageView imageView = (ImageView) imageLayout.findViewById(R.id.sliding_image);
        Picasso.with(context).load(hImagList.get(position).getPhoto()).placeholder(R.drawable.userimage).into(imageView);

        view.addView(imageLayout, 0);

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