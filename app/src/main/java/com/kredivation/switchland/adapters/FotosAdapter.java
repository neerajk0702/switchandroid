package com.kredivation.switchland.adapters;

import android.content.Context;
import android.graphics.Typeface;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.kredivation.switchland.R;
import com.kredivation.switchland.model.Homegallery;
import com.kredivation.switchland.utilities.FontManager;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class FotosAdapter extends RecyclerView.Adapter<FotosAdapter.MyViewHolder> {

    ArrayList<Homegallery> list;
    Context mContext;
    Typeface materialdesignicons_font;

    public class MyViewHolder extends RecyclerView.ViewHolder {
        LinearLayout locationLayout;
        ImageView sliding_image;

        public MyViewHolder(View view) {
            super(view);
            sliding_image = view.findViewById(R.id.sliding_image);
        }
    }


    public FotosAdapter(Context mContext, ArrayList<Homegallery> list) {
        this.list = list;
        this.mContext = mContext;
        materialdesignicons_font = FontManager.getFontTypefaceMaterialDesignIcons(mContext, "fonts/materialdesignicons-webfont.otf");
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.foto_item, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, final int position) {
        Picasso.with(mContext).load(list.get(position).getPhoto()).placeholder(R.drawable.userimage).into(holder.sliding_image);
    }

    @Override
    public int getItemCount() {
        return list.size();
    }
}

