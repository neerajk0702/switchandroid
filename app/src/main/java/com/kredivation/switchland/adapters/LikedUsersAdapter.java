package com.kredivation.switchland.adapters;

import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import androidx.recyclerview.widget.RecyclerView;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.kredivation.switchland.R;
import com.kredivation.switchland.activity.ChatActivity;
import com.kredivation.switchland.model.Data;
import com.kredivation.switchland.utilities.FontManager;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class LikedUsersAdapter extends RecyclerView.Adapter<LikedUsersAdapter.MyViewHolder> {

    private ArrayList<Data> myHomeList;
    Context mContext;
    Typeface materialdesignicons_font;

    public class MyViewHolder extends RecyclerView.ViewHolder {
        ImageView homeimage;
        TextView title, chatIcon;

        public MyViewHolder(View view) {
            super(view);
            homeimage = view.findViewById(R.id.homeimage);
            title = view.findViewById(R.id.title);
            chatIcon = view.findViewById(R.id.chatIcon);
        }
    }


    public LikedUsersAdapter(Context mContext, ArrayList<Data> myHomeList) {
        this.myHomeList = myHomeList;
        this.mContext = mContext;
        this.materialdesignicons_font = FontManager.getFontTypefaceMaterialDesignIcons(mContext, "fonts/materialdesignicons-webfont.otf");
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.liked_user_item, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, final int position) {
        holder.title.setText(myHomeList.get(position).getFull_name());
        holder.chatIcon.setTypeface(materialdesignicons_font);
        holder.chatIcon.setText(Html.fromHtml("&#xf611;"));
        Picasso.with(mContext).load(myHomeList.get(position).getProfile_image()).placeholder(R.drawable.avter).into(holder.homeimage);
        holder.chatIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, ChatActivity.class);
                intent.putExtra("ChatUserId", myHomeList.get(position).getUser_id());
                intent.putExtra("ProfileImg", myHomeList.get(position).getProfile_image());
                intent.putExtra("FullName", myHomeList.get(position).getFull_name());
                intent.putExtra("HomeId", myHomeList.get(position).getHome_id());
                mContext.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return myHomeList.size();
    }


}




