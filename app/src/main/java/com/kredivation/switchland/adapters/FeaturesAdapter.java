package com.kredivation.switchland.adapters;

import android.content.Context;
import android.graphics.Typeface;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.kredivation.switchland.R;
import com.kredivation.switchland.model.ChatData;
import com.kredivation.switchland.model.Features;
import com.kredivation.switchland.utilities.FontManager;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class FeaturesAdapter extends RecyclerView.Adapter<FeaturesAdapter.MyViewHolder> {

    List<Features> Featurelist;
    Context mContext;
    Typeface materialdesignicons_font;
    List<Features> saveFeatureList;

    public class MyViewHolder extends RecyclerView.ViewHolder {
        LinearLayout locationLayout;
        CheckBox feature;

        public MyViewHolder(View view) {
            super(view);
            feature = view.findViewById(R.id.feature);
        }
    }


    public FeaturesAdapter(Context mContext, List<Features> Featurelist, List<Features> saveFeatureList) {
        this.Featurelist = Featurelist;
        this.mContext = mContext;
        this.saveFeatureList = saveFeatureList;
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

