package com.kredivation.switchland.adapters;

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
import com.kredivation.switchland.model.Data;
import com.kredivation.switchland.utilities.FontManager;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class ChatMessageAdapter extends RecyclerView.Adapter<ChatMessageAdapter.MyViewHolder> {

    private ArrayList<Data> megList;
    Context mContext;
    Typeface materialdesignicons_font;
    private String userId;

    public class MyViewHolder extends RecyclerView.ViewHolder {
        ImageView userImage, myImage;
        TextView commentDescription, timing, userName;
        TextView rightArrowIcon, leftArrowIcon;
        CardView cardView;
        LinearLayout rightTrangle, leftTrangle;

        public MyViewHolder(View view) {
            super(view);
            cardView = view.findViewById(R.id.cardView);
            userImage = view.findViewById(R.id.userImage);
            myImage = view.findViewById(R.id.myImage);
            commentDescription = view.findViewById(R.id.commentDescription);
            timing = view.findViewById(R.id.timing);
            userName = view.findViewById(R.id.userName);
            rightArrowIcon = view.findViewById(R.id.rightArrowIcon);
            leftArrowIcon = view.findViewById(R.id.leftArrowIcon);
            rightTrangle = view.findViewById(R.id.rightTrangle);
            leftTrangle = view.findViewById(R.id.leftTrangle);

        }
    }


    public ChatMessageAdapter(Context mContext, ArrayList<Data> list, String userId) {
        this.megList = list;
        this.mContext = mContext;
        this.userId = userId;
        materialdesignicons_font = FontManager.getFontTypefaceMaterialDesignIcons(mContext, "fonts/materialdesignicons-webfont.otf");
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.chat_msg_row, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, final int position) {
        holder.rightArrowIcon.setTypeface(materialdesignicons_font);
        holder.rightArrowIcon.setText(Html.fromHtml("&#xf142;"));
        holder.leftArrowIcon.setTypeface(materialdesignicons_font);
        holder.leftArrowIcon.setText(Html.fromHtml("&#xf141;"));
        if (userId.equals(megList.get(position).getFrom_user_id())) {
            holder.leftTrangle.setVisibility(View.VISIBLE);
            holder.rightTrangle.setVisibility(View.GONE);
            Picasso.with(mContext).load(megList.get(position).getProfile_image()).placeholder(R.drawable.avter).resize(45, 45).into(holder.myImage);
            holder.userName.setText(megList.get(position).getFull_name());
        } else {
            holder.leftTrangle.setVisibility(View.GONE);
            holder.rightTrangle.setVisibility(View.VISIBLE);
            Picasso.with(mContext).load(megList.get(position).getProfile_image()).placeholder(R.drawable.avter).resize(45, 45).into(holder.userImage);
            holder.userName.setText(megList.get(position).getFull_name());
        }
        holder.commentDescription.setText(megList.get(position).getMessage());
        holder.timing.setText(megList.get(position).getMsg_add_date());

        holder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
    }

    @Override
    public int getItemCount() {
        return megList.size();
    }


}

