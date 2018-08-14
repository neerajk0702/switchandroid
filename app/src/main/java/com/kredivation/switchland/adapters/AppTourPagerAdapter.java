package com.kredivation.switchland.adapters;

import android.content.Context;
import android.graphics.Typeface;
import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.kredivation.switchland.R;
import com.kredivation.switchland.utilities.FontManager;

import java.util.ArrayList;


/**
 * Created by Neeraj on 8/4/2017.
 */
public class AppTourPagerAdapter extends PagerAdapter {

    private Context mContext;
    private LayoutInflater inflater;
    private ArrayList<Integer> tourImageList;
    private Typeface roboto_light;
    private Boolean CheckOrientation;

    public AppTourPagerAdapter(Context mContext, ArrayList<Integer> tourImageList, Boolean CheckOrientation) {
        this.mContext = mContext;
        this.tourImageList = tourImageList;
        this.CheckOrientation = CheckOrientation;
        inflater = LayoutInflater.from(mContext);
        roboto_light = FontManager.getFontTypeface(mContext, "fonts/roboto.light.ttf");
    }

    @Override
    public int getCount() {
        if (tourImageList.size() != 0) {
            return tourImageList.size();
        }
        return 0;
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view.equals(object);

    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        ViewHolder holder;
        View itemView = null;
        if (itemView == null) {
            holder = new ViewHolder();
            // itemView = inflater.inflate(R.layout.app_tour_item, container, false);
            itemView = inflater.inflate(tourImageList.get(position), container, false);
            holder.tourImg = (ImageView) itemView.findViewById(R.id.tourImg);
            holder.tourText1 = (TextView) itemView.findViewById(R.id.tourText1);
            holder.tourText1.setTypeface(roboto_light);
            itemView.setTag(holder);

        } else {
            holder = (ViewHolder) itemView.getTag();
        }
        if (position == 0) {
            //holder.tourText1.setText(Html.fromHtml("Search your neighborhood trusted <b>certified pharmacies</b>"));
        } else if (position == 1) {
           // holder.tourText1.setText(Html.fromHtml("Select from 50000+ <b>medicines</b> and <b>OTC products</b>"));
        } else if (position == 2) {
            //holder.tourText1.setText(Html.fromHtml("Place your order with <b>easy options</b>"));
        } else if (position == 3) {
           // holder.tourText1.setText(Html.fromHtml("Get fast <b>doorstep</b> delivery"));
        }
        //holder.imageView.setImageResource(tourImageList.get(position));
//        Picasso.with(mContext).load(tourImageList.get(position)).resize(1024, 1204).into(holder.imageView);
        container.addView(itemView);
        return itemView;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((View) object);
    }

    public class ViewHolder {
        public ImageView tourImg;
        public TextView tourText1;
    }
}
