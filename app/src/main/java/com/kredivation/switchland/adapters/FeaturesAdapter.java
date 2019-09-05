package com.kredivation.switchland.adapters;

import android.content.Context;
import android.graphics.Typeface;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.LinearLayout;

import com.kredivation.switchland.R;
import com.kredivation.switchland.model.Home_features;
import com.kredivation.switchland.utilities.FontManager;

import java.util.ArrayList;

public class FeaturesAdapter extends RecyclerView.Adapter<FeaturesAdapter.MyViewHolder> {

    ArrayList<Home_features> Featurelist;
    Context mContext;
    Typeface materialdesignicons_font;

    public class MyViewHolder extends RecyclerView.ViewHolder {
        LinearLayout locationLayout;
        CheckBox feature;

        public MyViewHolder(View view) {
            super(view);
            feature = view.findViewById(R.id.feature);
        }
    }


    public FeaturesAdapter(Context mContext,  ArrayList<Home_features> Featurelist) {
        this.Featurelist = Featurelist;
        this.mContext = mContext;
        materialdesignicons_font = FontManager.getFontTypefaceMaterialDesignIcons(mContext, "fonts/materialdesignicons-webfont.otf");
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.features_row, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, final int position) {
        holder.feature.setText(Featurelist.get(position).getName());
        if (Featurelist.get(position).isSelected()) {
            holder.feature.setChecked(true);
        } else {
            holder.feature.setChecked(false);
        }
        holder.feature.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    Featurelist.get(position).setSelected(true);
                } else {
                    Featurelist.get(position).setSelected(false);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return Featurelist.size();
    }
}

