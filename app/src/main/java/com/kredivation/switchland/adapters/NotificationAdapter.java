package com.kredivation.switchland.adapters;

import android.content.Context;
import android.graphics.Typeface;
import androidx.recyclerview.widget.RecyclerView;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.kredivation.switchland.R;
import com.kredivation.switchland.model.Data;
import com.kredivation.switchland.utilities.FontManager;

import java.util.ArrayList;

public class NotificationAdapter extends RecyclerView.Adapter<NotificationAdapter.MyViewHolder> {

    private ArrayList<Data> myHomeList;
    Context mContext;
    Typeface materialdesignicons_font;

    public class MyViewHolder extends RecyclerView.ViewHolder {
        TextView title, date, dateicon;

        public MyViewHolder(View view) {
            super(view);
            title = view.findViewById(R.id.title);
            date = view.findViewById(R.id.date);
            dateicon = view.findViewById(R.id.dateicon);
        }
    }


    public NotificationAdapter(Context mContext, ArrayList<Data> myHomeList) {
        this.myHomeList = myHomeList;
        this.mContext = mContext;
        this.materialdesignicons_font = FontManager.getFontTypefaceMaterialDesignIcons(mContext, "fonts/materialdesignicons-webfont.otf");
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.notification_row, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, final int position) {
        holder.title.setText(myHomeList.get(position).getMessage());
        holder.date.setText(myHomeList.get(position).getAdded_date());
        holder.dateicon.setTypeface(materialdesignicons_font);
        holder.dateicon.setText(Html.fromHtml("&#xf0f5;"));

    }

    @Override
    public int getItemCount() {
        return myHomeList.size();
    }


}





