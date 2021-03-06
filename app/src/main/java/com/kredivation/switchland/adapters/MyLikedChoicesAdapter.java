package com.kredivation.switchland.adapters;

import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import androidx.recyclerview.widget.RecyclerView;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.kredivation.switchland.R;
import com.kredivation.switchland.activity.HomeDetailActivity;
import com.kredivation.switchland.model.LikedmychoiceArray;
import com.kredivation.switchland.utilities.FontManager;
import com.kredivation.switchland.utilities.Utility;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class MyLikedChoicesAdapter extends RecyclerView.Adapter<MyLikedChoicesAdapter.MyViewHolder> {

    private ArrayList<LikedmychoiceArray> myHomeList;
    Context mContext;
    Typeface materialdesignicons_font;

    public class MyViewHolder extends RecyclerView.ViewHolder {
        LinearLayout mainLayout;
        ImageView homeimage;
        TextView title, like, location, startdate, enddate, remainingTime;
        Button travelRoutine, viewInfo;

        public MyViewHolder(View view) {
            super(view);
            mainLayout = view.findViewById(R.id.mainLayout);
            homeimage = view.findViewById(R.id.homeimage);
            title = view.findViewById(R.id.title);
            like = view.findViewById(R.id.like);
            location = view.findViewById(R.id.location);
            startdate = view.findViewById(R.id.startdate);
            enddate = view.findViewById(R.id.enddate);
            remainingTime = view.findViewById(R.id.remainingTime);
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
                .inflate(R.layout.liked_my_choices_item, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, final int position) {
        holder.title.setText(myHomeList.get(position).getTitle());
        holder.like.setTypeface(materialdesignicons_font);
        holder.like.setText(Html.fromHtml("&#xf2d1;"));
        holder.location.setText(myHomeList.get(position).getCity_name() + ", " + myHomeList.get(position).getCountry_name());
        holder.startdate.setText(myHomeList.get(position).getStartdate());
        holder.enddate.setText(myHomeList.get(position).getEnddate());
       // holder.likedUser.setPaintFlags(holder.likedUser.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);
        Picasso.with(mContext).load(myHomeList.get(position).getHome_image()).placeholder(R.drawable.home_default).into(holder.homeimage);

        if (myHomeList.get(position).getTinder_date() != null && !myHomeList.get(position).getTinder_date().equals("")) {
            long remaningTime = Utility.getRemainigTime(myHomeList.get(position).getTinder_date());
            Utility.startTimer(remaningTime, holder.remainingTime);
        }
      /*  holder.likedUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, LikedUserActivity.class);
                intent.putExtra("HomeId", myHomeList.get(position).getHome_id());
                mContext.startActivity(intent);
            }
        });*/
        holder.mainLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent homeintent = new Intent(mContext, HomeDetailActivity.class);
                homeintent.putExtra("HomeId", myHomeList.get(position).getHome_id());
                homeintent.putExtra("SenderUserId", myHomeList.get(position).getUser_id());
                homeintent.putExtra("EditFlage", false);
                mContext.startActivity(homeintent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return myHomeList.size();
    }


}



