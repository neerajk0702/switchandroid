package com.kredivation.switchland.adapters;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.kredivation.switchland.R;
import com.kredivation.switchland.activity.ChatActivity;
import com.kredivation.switchland.activity.EditProfileActivity;
import com.kredivation.switchland.model.ChatData;
import com.kredivation.switchland.model.Data;
import com.kredivation.switchland.utilities.FontManager;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class ChatListAdapter extends RecyclerView.Adapter<ChatListAdapter.MyViewHolder> {

    private ArrayList<Data> chatList;
    Context mContext;
    Typeface materialdesignicons_font;

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView userName, locationIcon, location;
        ImageView userImage;
        CardView cardView;

        public MyViewHolder(View view) {
            super(view);
            userName = view.findViewById(R.id.userName);
            userImage = view.findViewById(R.id.userImage);
            locationIcon = view.findViewById(R.id.locationIcon);
            location = view.findViewById(R.id.location);
            cardView = view.findViewById(R.id.cardView);
        }
    }


    public ChatListAdapter(Context mContext, ArrayList<Data> list) {
        this.chatList = list;
        this.mContext = mContext;
        materialdesignicons_font = FontManager.getFontTypefaceMaterialDesignIcons(mContext, "fonts/materialdesignicons-webfont.otf");
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.chat_list_row, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, final int position) {
        holder.location.setText(chatList.get(position).getCity_name() + " ," + chatList.get(position).getCountry_name());
        String location = "";
        if (chatList.get(position).getCity_name() != null && !chatList.get(position).getCity_name().equals("")) {
            location = chatList.get(position).getCity_name() + ", ";
        }
        if (chatList.get(position).getCountry_name() != null && !chatList.get(position).getCountry_name().equals("")) {
            location.concat(chatList.get(position).getCountry_name());
        }
        holder.location.setText(location);
        holder.userName.setText(chatList.get(position).getFull_name());
        holder.locationIcon.setTypeface(materialdesignicons_font);
        holder.locationIcon.setText(Html.fromHtml("&#xf34e;"));
        Picasso.with(mContext).load(chatList.get(position).getProfile_image()).placeholder(R.drawable.userimage).resize(80, 80).into(holder.userImage);

        holder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, ChatActivity.class);
                intent.putExtra("ChatUserId", chatList.get(position).getUser_id());
                intent.putExtra("ProfileImg", chatList.get(position).getProfile_image());
                intent.putExtra("FullName", chatList.get(position).getFull_name());
                intent.putExtra("HomeId", chatList.get(position).getHome_id());
                mContext.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return chatList.size();
    }


}
