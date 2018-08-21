package com.kredivation.switchland.adapters;

import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.Transformation;
import com.bumptech.glide.load.resource.bitmap.CenterCrop;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.bumptech.glide.request.RequestOptions;
import com.kredivation.switchland.R;
import com.kredivation.switchland.activity.HomeDetailActivity;
import com.kredivation.switchland.model.Home_data;
import com.kredivation.switchland.utilities.FontManager;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class HomeTinderCardAdapter extends BaseAdapter {
    private ArrayList<Home_data> homeList;
    private final Context context;
    Typeface materialdesignicons_font;

    public HomeTinderCardAdapter(Context context, ArrayList<Home_data> homeList) {
        super();
        this.homeList = homeList;
        this.context = context;
        materialdesignicons_font = FontManager.getFontTypefaceMaterialDesignIcons(context, "fonts/materialdesignicons-webfont.otf");
    }

    @Override
    public int getCount() {
        return homeList.size();
    }

    @Override
    public Object getItem(int position) {
        return homeList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return (long) position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;

        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.item_koloda, parent, false);
            viewHolder = new ViewHolder(convertView);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        viewHolder.detailIcon.setTypeface(materialdesignicons_font);
        viewHolder.detailIcon.setText(Html.fromHtml("&#xf2fd;"));
        viewHolder.title.setText(homeList.get(position).getTitle());
        viewHolder.location.setText(homeList.get(position).getCity_name() + "," + homeList.get(position).getCountry_name());
        viewHolder.date.setText(homeList.get(position).getStartdate());
        RequestOptions transforms = (new RequestOptions()).transforms(new Transformation[]{(Transformation) (new CenterCrop()), (Transformation) (new RoundedCorners(20))});
        Glide.with(context).load(homeList.get(position).getProfile_image()).apply(transforms).into(viewHolder.kolodaImage);

        viewHolder.detailIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent homeintent = new Intent(context, HomeDetailActivity.class);
                homeintent.putExtra("HomeId", homeList.get(position).getId());
                homeintent.putExtra("EditFlage", false);
                context.startActivity(homeintent);
            }
        });
        return convertView;
    }

    private class ViewHolder {
        TextView title, detailIcon, location, date;
        TextView itemDescription;
        ImageView kolodaImage;

        public ViewHolder(View view) {
            kolodaImage = view.findViewById(R.id.kolodaImage);
            title = view.findViewById(R.id.title);
            detailIcon = view.findViewById(R.id.detailIcon);
            location = view.findViewById(R.id.location);
            date = view.findViewById(R.id.date);
        }
    }
}
