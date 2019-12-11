package com.kredivation.switchland.adapters;

import android.content.Context;
import android.graphics.Typeface;

import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.kredivation.switchland.R;
import com.kredivation.switchland.database.SwitchDBHelper;
import com.kredivation.switchland.framework.IAsyncWorkCompletedCallback;
import com.kredivation.switchland.framework.ServiceCaller;
import com.kredivation.switchland.model.Data;
import com.kredivation.switchland.model.HomeImageDeleteContentData;
import com.kredivation.switchland.model.Homegallery;
import com.kredivation.switchland.model.ServiceContentData;
import com.kredivation.switchland.utilities.ASTProgressBar;
import com.kredivation.switchland.utilities.Contants;
import com.kredivation.switchland.utilities.FontManager;
import com.kredivation.switchland.utilities.Utility;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.util.ArrayList;

public class AddHomePhotoAdapter extends RecyclerView.Adapter<AddHomePhotoAdapter.MyViewHolder> {

    private ArrayList<Homegallery> locationList;
    Context mContext;
    Typeface materialdesignicons_font;

    public class MyViewHolder extends RecyclerView.ViewHolder {
        LinearLayout locationLayout;
        ImageView homeimage,homedelet;
        TextView textname;

        public MyViewHolder(View view) {
            super(view);
            homeimage = view.findViewById(R.id.homeimage);
            textname = view.findViewById(R.id.textname);
            homedelet = view.findViewById(R.id.homedelet);
        }
    }


    public AddHomePhotoAdapter(Context mContext, ArrayList<Homegallery> list) {
        this.locationList = list;
        this.mContext = mContext;
        materialdesignicons_font = FontManager.getFontTypefaceMaterialDesignIcons(mContext, "fonts/materialdesignicons-webfont.otf");
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.add_home_photo_item, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        int count=position+1;

        holder.textname.setText("Photo " + count);
        if (locationList.get(position).getPhoto().contains("http")) {
            Picasso.with(mContext).load(locationList.get(position).getPhoto()).into(holder.homeimage);
        } else {
            File fileImage = new File(locationList.get(position).getPhoto());
            Picasso.with(mContext).load(fileImage).into(holder.homeimage);
        }
        holder.homedelet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (locationList.get(position).getPhoto().contains("http")) {
                deleteHomeImage(locationList.get(position).getHome_id(),locationList.get(position).getId(),position);
                }else{
                    locationList.remove(position);
                    notifyDataSetChanged();
                    Toast.makeText(mContext,"Home image delete successfully",Toast.LENGTH_LONG).show();
                }
            }
        });

    }

    @Override
    public int getItemCount() {
        return locationList.size();
    }

    private void deleteHomeImage(String homeID,String imageId,int position) {
        if (Utility.isOnline(mContext)) {
            String userId = "";
            SwitchDBHelper switchDBHelper = new SwitchDBHelper(mContext);
            ArrayList<Data> userData = switchDBHelper.getAllUserInfoList();
            if (userData != null && userData.size() > 0) {
                for (Data data : userData) {
                    userId = data.getId();
                }
            }
            final ASTProgressBar dotDialog = new ASTProgressBar(mContext);
            dotDialog.show();
            JSONObject object = new JSONObject();
            try {
                object.put("api_key", Contants.API_KEY);
                object.put("user_id", userId);
                object.put("image_id", imageId);
                object.put("home_id", homeID);
            } catch (JSONException e) {
                //e.printStackTrace();
            }

            String serviceURL = Contants.BASE_URL + Contants.deletehomeimage;

            ServiceCaller serviceCaller = new ServiceCaller(mContext);
            serviceCaller.CallCommanServiceMethod(serviceURL, object, "HomeImageDelete", new IAsyncWorkCompletedCallback() {
                @Override
                public void onDone(String result, boolean isComplete) {
                    if (isComplete) {
                        if (result != null) {
                            final HomeImageDeleteContentData serviceData = new Gson().fromJson(result, HomeImageDeleteContentData.class);
                            if (serviceData != null) {
                                if (serviceData.isSuccess()) {
                                    locationList.remove(position);
                                    notifyDataSetChanged();
                                    Toast.makeText(mContext,"Home image delete successfully",Toast.LENGTH_LONG).show();
                                } else {
                                    Toast.makeText(mContext,"Home image has been not deleted!",Toast.LENGTH_LONG).show();
                                }
                            }
                        }
                    } else {
                        Utility.alertForErrorMessage(Contants.Error, mContext);
                    }
                    if (dotDialog.isShowing()) {
                        dotDialog.dismiss();
                    }
                }
            });
        } else {
            Utility.alertForErrorMessage(Contants.OFFLINE_MESSAGE, mContext);//off line msg....
        }
    }
}
