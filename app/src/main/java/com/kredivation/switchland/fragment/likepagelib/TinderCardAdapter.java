package com.kredivation.switchland.fragment.likepagelib;

import android.content.Context;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.Transformation;
import com.bumptech.glide.load.resource.bitmap.CenterCrop;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.bumptech.glide.request.RequestOptions;
import com.kredivation.switchland.R;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class TinderCardAdapter extends BaseAdapter {
    private final List dataList;
    private final Context context;
    private final List data;

    public int getCount() {
        return this.dataList.size();
    }

    public Integer getItem(int position) {
        return (Integer) this.dataList.get(position);
    }


    public long getItemId(int position) {
        return (long) position;
    }

    public final void setData( List data) {
        this.dataList.clear();
        this.dataList.addAll(data);
        this.notifyDataSetChanged();
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        View view = convertView;
        TinderCardAdapter.DataViewHolder holder;
        if (convertView == null) {
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_koloda, parent, false);
            holder = new TinderCardAdapter.DataViewHolder(view);
            view.setTag(holder);
        } else {
            Object var10000 = convertView.getTag();
            if (var10000 == null) {
                // throw new TypeCastException("null cannot be cast to non-null type com.kredivation.switchland.adapters.KolodaSampleAdapter.DataViewHolder");
            }

            holder = (DataViewHolder) var10000;
        }
        holder.bindData$production_sources_for_module_app(this.context, this.getItem(position));
        return view;
    }


    public final Context getContext() {
        return this.context;
    }

    public final List getData() {
        return this.data;
    }

    public TinderCardAdapter(Context context, List data) {
        super();
        this.context = context;
        this.data = data;
        List var4 = (List) (new ArrayList());
        this.dataList = var4;
        if (this.data != null) {
            this.dataList.addAll((Collection) this.data);
        }

    }

    public static final class DataViewHolder extends RecyclerView.ViewHolder {
        private ImageView picture;

        public final ImageView getPicture() {
            return this.picture;
        }

        public final void setPicture(ImageView var1) {
            this.picture = var1;
        }

        public final void bindData$production_sources_for_module_app(Context context, int data) {

            RequestOptions transforms = (new RequestOptions()).transforms(new Transformation[]{(Transformation) (new CenterCrop()), (Transformation) (new RoundedCorners(20))});
            Glide.with(context).load(data).apply(transforms).into(this.picture);
        }

        public DataViewHolder(View view) {
            super(view);
            this.picture = view.findViewById(R.id.kolodaImage);
        }
    }
}
