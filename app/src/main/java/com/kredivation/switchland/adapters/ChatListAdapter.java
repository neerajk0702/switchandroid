package com.kredivation.switchland.adapters;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
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
import com.kredivation.switchland.database.SwitchDBHelper;
import com.kredivation.switchland.model.ChatData;
import com.kredivation.switchland.model.Data;
import com.kredivation.switchland.model.LikedmychoiceArray;
import com.kredivation.switchland.utilities.FontManager;
import com.kredivation.switchland.utilities.Utility;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class ChatListAdapter extends RecyclerView.Adapter<ChatListAdapter.MyViewHolder> {

    private ArrayList<Data> chatList;
    Context mContext;
    Typeface materialdesignicons_font;
    String selectChatUserId;
    SwitchDBHelper switchDBHelper;

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView userName, locationIcon, location, timeleft, date;
        ImageView userImage;
        CardView cardView;
        LinearLayout main_layout;

        public MyViewHolder(View view) {
            super(view);
            userName = view.findViewById(R.id.userName);
            userImage = view.findViewById(R.id.userImage);
            locationIcon = view.findViewById(R.id.locationIcon);
            location = view.findViewById(R.id.location);
            cardView = view.findViewById(R.id.cardView);
            timeleft = view.findViewById(R.id.timeleft);
            main_layout = view.findViewById(R.id.main_layout);
            date = view.findViewById(R.id.date);
        }
    }


    public ChatListAdapter(Context mContext, ArrayList<Data> list) {
        this.chatList = list;
        this.mContext = mContext;
        materialdesignicons_font = FontManager.getFontTypefaceMaterialDesignIcons(mContext, "fonts/materialdesignicons-webfont.otf");
        SharedPreferences prefs = mContext.getSharedPreferences("ChatUserPreferences", Context.MODE_PRIVATE);
        selectChatUserId = prefs.getString("ChatUserId", "");
        switchDBHelper = new SwitchDBHelper(mContext);
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
        holder.userName.setText(chatList.get(position).getFull_name());
        holder.locationIcon.setTypeface(materialdesignicons_font);
        holder.locationIcon.setText(Html.fromHtml("&#xf34e;"));
        Picasso.with(mContext).load(chatList.get(position).getProfile_image()).placeholder(R.drawable.userimage).resize(40, 40).into(holder.userImage);
        if (selectChatUserId != null && !selectChatUserId.equals("") && selectChatUserId.equals(chatList.get(position).getUser_id())) {
            holder.main_layout.setBackgroundColor(Color.parseColor("#F46F6C"));
            holder.userName.setTextColor(Color.parseColor("#ffffff"));
            holder.location.setTextColor(Color.parseColor("#ffffff"));
            holder.timeleft.setTextColor(Color.parseColor("#ffffff"));
            holder.date.setTextColor(Color.parseColor("#ffffff"));
        } else {
            holder.main_layout.setBackgroundColor(Color.parseColor("#ffffff"));
            holder.userName.setTextColor(Color.parseColor("#212121"));
            holder.location.setTextColor(Color.parseColor("#808080"));
            holder.timeleft.setTextColor(Color.parseColor("#ff0000"));
            holder.date.setTextColor(Color.parseColor("#212121"));
        }

        LikedmychoiceArray likedmychoiceUserHome = switchDBHelper.getLikedmychoiceDataByUserId(chatList.get(position).getUser_id(), chatList.get(position).getHome_id());
        if (likedmychoiceUserHome != null) {
            if (likedmychoiceUserHome.getTinder_date() != null && !likedmychoiceUserHome.getTinder_date().equals("")) {
                long remaningTime = Utility.getRemainigTime(likedmychoiceUserHome.getTinder_date());
                Utility.startTimer(remaningTime, holder.timeleft);
            }
        }

        holder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferences prefs = mContext.getSharedPreferences("ChatUserPreferences", Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = prefs.edit();
                editor.putString("ChatUserId", chatList.get(position).getUser_id());
                editor.commit();
                String locationStr = chatList.get(position).getCity_name() + " ," + chatList.get(position).getCountry_name();
                Intent intent = new Intent(mContext, ChatActivity.class);
                intent.putExtra("ChatUserId", chatList.get(position).getUser_id());
                intent.putExtra("ProfileImg", chatList.get(position).getProfile_image());
                intent.putExtra("FullName", chatList.get(position).getFull_name());
                intent.putExtra("HomeId", chatList.get(position).getHome_id());
                intent.putExtra("Location", locationStr);
                mContext.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return chatList.size();
    }


}
