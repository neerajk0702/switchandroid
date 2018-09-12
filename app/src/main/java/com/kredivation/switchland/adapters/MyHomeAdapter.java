package com.kredivation.switchland.adapters;

import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.gson.Gson;
import com.kredivation.switchland.R;
import com.kredivation.switchland.activity.AddHomeActivity;
import com.kredivation.switchland.activity.HomeDetailActivity;
import com.kredivation.switchland.activity.TravelRoutineActivity;
import com.kredivation.switchland.database.SwitchDBHelper;
import com.kredivation.switchland.framework.IAsyncWorkCompletedCallback;
import com.kredivation.switchland.framework.ServiceCaller;
import com.kredivation.switchland.model.ChatData;
import com.kredivation.switchland.model.HomeDetails;
import com.kredivation.switchland.model.MyhomeArray;
import com.kredivation.switchland.model.ServiceContentData;
import com.kredivation.switchland.utilities.ASTProgressBar;
import com.kredivation.switchland.utilities.Contants;
import com.kredivation.switchland.utilities.FontManager;
import com.kredivation.switchland.utilities.Utility;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class MyHomeAdapter extends RecyclerView.Adapter<MyHomeAdapter.MyViewHolder> {

    private ArrayList<MyhomeArray> myHomeList;
    Context mContext;
    Typeface materialdesignicons_font;

    public class MyViewHolder extends RecyclerView.ViewHolder {
        LinearLayout mainLayout;
        ImageView homeimage;
        TextView title, nobed, nobath, locationIcon, location, startdate, enddate;
        Button travelRoutine, viewInfo;

        public MyViewHolder(View view) {
            super(view);
            mainLayout = view.findViewById(R.id.mainLayout);
            homeimage = view.findViewById(R.id.homeimage);
            title = view.findViewById(R.id.title);
            nobed = view.findViewById(R.id.nobed);
            nobath = view.findViewById(R.id.nobath);
            locationIcon = view.findViewById(R.id.locationIcon);
            location = view.findViewById(R.id.location);
            startdate = view.findViewById(R.id.startdate);
            enddate = view.findViewById(R.id.enddate);
            travelRoutine = view.findViewById(R.id.travelRoutine);
            viewInfo = view.findViewById(R.id.viewInfo);
        }
    }


    public MyHomeAdapter(Context mContext, ArrayList<MyhomeArray> myHomeList) {
        this.myHomeList = myHomeList;
        this.mContext = mContext;
        materialdesignicons_font = FontManager.getFontTypefaceMaterialDesignIcons(mContext, "fonts/materialdesignicons-webfont.otf");
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.my_home_row, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, final int position) {
        holder.locationIcon.setTypeface(materialdesignicons_font);
        holder.locationIcon.setText(Html.fromHtml("&#xf34e;"));
        holder.title.setText(myHomeList.get(position).getTitle());
        holder.nobed.setText(myHomeList.get(position).getBedrooms() + " Beds");
        holder.nobath.setText(myHomeList.get(position).getBathrooms() + " Baths");
        holder.location.setText(myHomeList.get(position).getLocation());
        holder.startdate.setText(myHomeList.get(position).getStartdate());
        holder.enddate.setText(myHomeList.get(position).getEnddate());
        Picasso.with(mContext).load(myHomeList.get(position).getProfile_image()).placeholder(R.drawable.home_default).into(holder.homeimage);
        holder.travelRoutine.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                HomeDetails details = new HomeDetails();
                MyhomeArray myhome = myHomeList.get(position);
               /* if(myhome!=null){
                    details.setId(myhome.getId());
                    details.setUser_id(myhome.getUser_id());
                    details.setHome_type(myhome.getHome_type());
                    details.setBathrooms(myhome.getBathrooms());
                    details.setBedrooms(myhome.getBedrooms());
                    details.setSleeps(myhome.getSleeps());
                    details.setProperty_type(myhome.getProperty_type());
                    details.setPets(myhome.getPets());
                    details.setFamily_matters(myhome.getFamily_matters());
                    details.setTitle(myhome.getTitle());
                    details.setSort_description(myhome.getSort_description());
                    details.setHouse_no(myhome.getHouse_no());
                    details.setLocation(myhome.getLocation());
                    details.setLatitude(myhome.getLatitude());
                    details.setLongitude(myhome.getLongitude());
                    details.setDestinations(myhome.getDestinations());
                    details.setTraveller_type(myhome.getTraveller_type());
                    details.setTravelling_anywhere(myhome.getTravelling_anywhere());
                    details.setProfile_image(myhome.getProfile_image());
                    details.setStartdate(myhome.getStartdate());
                    details.setEnddate(myhome.getEnddate());
                    details.setCountry_id(myhome.getCountry_id());
                    details.setCity_id(myhome.getCity_id());
                    details.setAddress1(myhome.getAddress1());
                    details.setAddress2(myhome.getAddress2());
                    details.setZipcode(myhome.getZipcode());
                    details.setGender(myhome.getGender());
                    details.setReligion(myhome.getReligion());
                    details.setLandmarks(myhome.getLandmarks());
                    details.setLevel_security(myhome.getLevel_security());
                    details.setProfile_completeness(myhome.getProfile_completeness());
                    details.setStatus(myhome.getStatus());
                    details.setAdded_date(myhome.getAdded_date());
                    details.setUpdated_date(myhome.getUpdated_date());
                    details.setCity_name(myhome.get);
                    details.setCountry_name(myhome.getco);
                    details.setFamily(myhome.());
                    details.setHomestyle(myhome.getHo);
                    details.setPropertytype(myhome.getpr);
                    details.setTravellertype(travellertype);
                    details.setPetsallowed(petsallowed);
                    details.setCardnumber(cardnumber);
                    details.setNameoncard(nameoncard);
                    details.setMonth(month);
                    details.setYear(year);
                    details.setCvv(cvv);
                    details.setFeatureList(featureList);
                    details.setHouseRuleList(hRuleList);
                    details.setHomeImageList(hImagList);
                }*/

                Intent intent = new Intent(mContext, TravelRoutineActivity.class);
                SwitchDBHelper dbHelper = new SwitchDBHelper(mContext);
                dbHelper.deleteAllRows("AddEditHomeData");
                intent.putExtra("StartDate", myhome.getStartdate());
                intent.putExtra("EndDate", myhome.getEnddate());
                intent.putExtra("CountryId", myhome.getCountry_id());
                intent.putExtra("CityId", myhome.getCity_id());
                intent.putExtra("HomeId", myhome.getId());
                intent.putExtra("MyHomeAdapterFlage", true);
                mContext.startActivity(intent);
            }
        });
        holder.viewInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent homeintent = new Intent(mContext, HomeDetailActivity.class);
                homeintent.putExtra("HomeId", myHomeList.get(position).getId());
                homeintent.putExtra("EditFlage", true);
                mContext.startActivity(homeintent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return myHomeList.size();
    }


}

