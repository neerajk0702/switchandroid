package com.kredivation.switchland.adapters;

import android.app.Activity;
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

import java.util.ArrayList;

public class ChatListAdapter extends RecyclerView.Adapter<ChatListAdapter.MyViewHolder> {

    private ArrayList<ChatData> locationList;
    Context mContext;
    Typeface materialdesignicons_font;

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView userName,locationIcon,location;
        LinearLayout locationLayout;
        ImageView userImage;

        public MyViewHolder(View view) {
            super(view);
            userName = (TextView) view.findViewById(R.id.userName);
            userImage=view.findViewById(R.id.userImage);
            locationIcon = (TextView) view.findViewById(R.id.locationIcon);
            location = (TextView) view.findViewById(R.id.location);
        }
    }


    public ChatListAdapter(Context mContext, ArrayList<ChatData> list) {
        this.locationList = list;
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
    public void onBindViewHolder(MyViewHolder holder, int position) {
        holder.location.setText(locationList.get(position).getLocation());
        holder.userName.setText(locationList.get(position).getUsername());

        holder.locationIcon.setTypeface(materialdesignicons_font);
        holder.locationIcon.setText(Html.fromHtml("&#xf34e;"));

      /*  holder.locationLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((Activity)mContext).finish();
            }
        });*/
    }

    @Override
    public int getItemCount() {
        return locationList.size();
    }


}
