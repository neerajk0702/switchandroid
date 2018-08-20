package com.kredivation.switchland.adapters;

import android.content.Context;
import android.graphics.Typeface;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.kredivation.switchland.R;
import com.kredivation.switchland.model.LikedmychoiceArray;
import com.kredivation.switchland.model.MychoiceArray;
import com.kredivation.switchland.utilities.FontManager;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class MyLikedChoicesAdapter extends RecyclerView.Adapter<MyLikedChoicesAdapter.MyViewHolder> {

    private ArrayList<LikedmychoiceArray> myHomeList;
    Context mContext;
    Typeface materialdesignicons_font;

    public class MyViewHolder extends RecyclerView.ViewHolder {
        LinearLayout mainLayout;
        ImageView homeimage;
        TextView title, like, locationIcon, location, startdate, enddate;
        Button travelRoutine, viewInfo;

        public MyViewHolder(View view) {
            super(view);
            mainLayout = view.findViewById(R.id.mainLayout);
            homeimage = view.findViewById(R.id.homeimage);
            title = view.findViewById(R.id.title);
            like = view.findViewById(R.id.like);
            locationIcon = view.findViewById(R.id.locationIcon);
            location = view.findViewById(R.id.location);
            startdate = view.findViewById(R.id.startdate);
            enddate = view.findViewById(R.id.enddate);
        }
    }


    public MyLikedChoicesAdapter(Context mContext, ArrayList<LikedmychoiceArray> myHomeList) {
        this.myHomeList = myHomeList;
        this.mContext = mContext;
        materialdesignicons_font = FontManager.getFontTypefaceMaterialDesignIcons(mContext, "fonts/materialdesignicons-webfont.otf");
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.my_choices_row, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        holder.locationIcon.setTypeface(materialdesignicons_font);
        holder.locationIcon.setText(Html.fromHtml("&#xf34e;"));
        holder.title.setText(myHomeList.get(position).getTitle());
        holder.like.setTypeface(materialdesignicons_font);
        holder.like.setText(Html.fromHtml("&#xf2d1;"));
        holder.location.setText(myHomeList.get(position).getLocation());
        holder.startdate.setText("From " + myHomeList.get(position).getStartdate());
        holder.startdate.setText("To " + myHomeList.get(position).getEnddate());
        Picasso.with(mContext).load(myHomeList.get(position).getHome_image()).placeholder(R.drawable.home_default).into(holder.homeimage);
    }

    @Override
    public int getItemCount() {
        return myHomeList.size();
    }


}



