package com.kredivation.switchland.adapters;

import android.content.Context;

import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.kredivation.switchland.R;
import com.kredivation.switchland.model.Home_rules;

import java.util.ArrayList;
import java.util.List;

public class HomeDetailRuleAdapter extends RecyclerView.Adapter<HomeDetailRuleAdapter.MyViewHolder> {

    List<Home_rules> Featurelist;
    Context mContext;

    public class MyViewHolder extends RecyclerView.ViewHolder {
        LinearLayout locationLayout;
        TextView title;

        public MyViewHolder(View view) {
            super(view);
            title = view.findViewById(R.id.title);
        }
    }


    public HomeDetailRuleAdapter(Context mContext, ArrayList<Home_rules> Featurelist) {
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


