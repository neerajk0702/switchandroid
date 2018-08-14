package com.kredivation.switchland.adapters;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.kredivation.switchland.R;


public class CardStackAdapter extends ArrayAdapter<String> {


    public CardStackAdapter(Context context, int resource) {
        super(context, 0);
    }

    @Override
    public View getView(int position, final View contentView, ViewGroup parent) {
        TextView v = (contentView.findViewById(R.id.helloText));
        v.setText(getItem(position));
        return contentView;
    }

}
