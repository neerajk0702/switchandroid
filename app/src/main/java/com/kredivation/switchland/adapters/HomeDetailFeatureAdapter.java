package com.kredivation.switchland.adapters;

import android.content.Context;
import android.graphics.Typeface;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.kredivation.switchland.R;
import com.kredivation.switchland.model.Features;
import com.kredivation.switchland.model.Home_features;
import com.kredivation.switchland.utilities.FontManager;

import java.util.ArrayList;
import java.util.List;

public class HomeDetailFeatureAdapter extends RecyclerView.Adapter<HomeDetailFeatureAdapter.MyViewHolder> {

    List<Home_features> Featurelist;
    Context mContext;
    Typeface materialdesignicons_font;

    public class MyViewHolder extends RecyclerView.ViewHolder {
        LinearLayout locationLayout;
        TextView title;

        public MyViewHolder(View view) {
            super(view);
            title = view.findViewById(R.id.title);
        }
    }


    public HomeDetailFeatureAdapter(Context mContext, ArrayList<Home_features> Featurelist) {
        this.Featurelist = Featurelist;
        this.mContext = mContext;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.text_row, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, final int position) {
        holder.title.setText(Featurelist.get(position).getName());

    }

    @Override
    public int getItemCount() {
        return Featurelist.size();
    }
}


