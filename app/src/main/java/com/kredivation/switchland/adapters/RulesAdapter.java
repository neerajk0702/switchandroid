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

import com.kredivation.switchland.R;
import com.kredivation.switchland.model.Features;
import com.kredivation.switchland.model.Home_rules;
import com.kredivation.switchland.model.House_rules;
import com.kredivation.switchland.utilities.FontManager;

import java.util.ArrayList;
import java.util.List;

public class RulesAdapter extends RecyclerView.Adapter<RulesAdapter.MyViewHolder> {

    List<Home_rules> rulelist;
    Context mContext;

    public class MyViewHolder extends RecyclerView.ViewHolder {
        LinearLayout locationLayout;
        CheckBox feature;

        public MyViewHolder(View view) {
            super(view);
            feature = view.findViewById(R.id.feature);
        }
    }


    public RulesAdapter(Context mContext, ArrayList<Home_rules> rulelist) {
        this.rulelist = rulelist;
        this.mContext = mContext;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.features_row, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, final int position) {
        holder.feature.setText(rulelist.get(position).getName());

        if (rulelist.get(position).isSelected()) {
            holder.feature.setChecked(true);
        } else {
            holder.feature.setChecked(false);
        }
        holder.feature.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    rulelist.get(position).setSelected(true);
                } else {
                    rulelist.get(position).setSelected(false);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return rulelist.size();
    }
}


