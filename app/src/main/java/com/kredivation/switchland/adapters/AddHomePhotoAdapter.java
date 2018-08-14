package com.kredivation.switchland.adapters;

import android.content.Context;
import android.graphics.Typeface;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.kredivation.switchland.R;
import com.kredivation.switchland.model.ChatData;
import com.kredivation.switchland.utilities.FontManager;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class AddHomePhotoAdapter extends RecyclerView.Adapter<AddHomePhotoAdapter.MyViewHolder> {

    private ArrayList<ChatData> locationList;
    Context mContext;
    Typeface materialdesignicons_font;

    public class MyViewHolder extends RecyclerView.ViewHolder {
        LinearLayout locationLayout;
        ImageView homeimage;

        public MyViewHolder(View view) {
            super(view);
            homeimage = view.findViewById(R.id.homeimage);
        }
    }


    public AddHomePhotoAdapter(Context mContext, ArrayList<ChatData> list) {
        this.locationList = list;
        this.mContext = mContext;
        materialdesignicons_font = FontManager.getFontTypefaceMaterialDesignIcons(mContext, "fonts/materialdesignicons-webfont.otf");
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.add_home_photo_item, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {

        Picasso.with(mContext).load(locationList.get(position).getImageFile()).into(holder.homeimage);

    }

    @Override
    public int getItemCount() {
        return locationList.size();
    }


}
