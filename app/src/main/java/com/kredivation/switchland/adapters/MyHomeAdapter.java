package com.kredivation.switchland.adapters;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.support.v7.app.AlertDialog;
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
import com.kredivation.switchland.activity.MyHomeDetailActivity;
import com.kredivation.switchland.activity.SettingActivity;
import com.kredivation.switchland.activity.SigninActivity;
import com.kredivation.switchland.activity.SplashScreenActivity;
import com.kredivation.switchland.activity.TravelRoutineActivity;
import com.kredivation.switchland.database.SwitchDBHelper;
import com.kredivation.switchland.framework.IAsyncWorkCompletedCallback;
import com.kredivation.switchland.framework.ServiceCaller;
import com.kredivation.switchland.model.ChatData;
import com.kredivation.switchland.model.HomeDetails;
import com.kredivation.switchland.model.LikedmychoiceArray;
import com.kredivation.switchland.model.MychoiceArray;
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
    ASTProgressBar homeDialog;

    public class MyViewHolder extends RecyclerView.ViewHolder {
        LinearLayout mainLayout;
        ImageView homeimage;
        TextView title, nobed, nobath, locationIcon, location, startdate, enddate;
        Button travelRoutine, viewInfo;
        ImageView homedelete;

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
            homedelete = view.findViewById(R.id.homedelete);
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
        holder.location.setText(myHomeList.get(position).getCity_name() + "," + myHomeList.get(position).getCountry_name());
        holder.startdate.setText(myHomeList.get(position).getStartdate());
        holder.enddate.setText(myHomeList.get(position).getEnddate());
        Picasso.with(mContext).load(myHomeList.get(position).getProfile_image()).placeholder(R.drawable.home_default).into(holder.homeimage);
        holder.travelRoutine.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                HomeDetails details = new HomeDetails();
                MyhomeArray myhome = myHomeList.get(position);


                Intent intent = new Intent(mContext, TravelRoutineActivity.class);
                SwitchDBHelper dbHelper = new SwitchDBHelper(mContext);
                dbHelper.deleteAllRows("AddEditHomeData");
                intent.putExtra("StartDate", myhome.getStartdate());
                intent.putExtra("EndDate", myhome.getEnddate());
                intent.putExtra("CountryId", myhome.getTravel_country());
                intent.putExtra("CityId", myhome.getTravel_city());
                intent.putExtra("HomeId", myhome.getId());
                intent.putExtra("MyHomeAdapterFlage", true);
                mContext.startActivity(intent);
            }
        });
        holder.viewInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent homeintent = new Intent(mContext, MyHomeDetailActivity.class);
                homeintent.putExtra("HomeId", myHomeList.get(position).getId());
                homeintent.putExtra("EditFlage", true);
                mContext.startActivity(homeintent);
            }
        });
        holder.homedelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                deletePopup(position);
            }
        });
    }

    @Override
    public int getItemCount() {
        return myHomeList.size();
    }

    private void homeDelete(int postion) {
        if (Utility.isOnline(mContext)) {
            ASTProgressBar dotDialog = new ASTProgressBar(mContext);
            dotDialog.show();
            JSONObject object = new JSONObject();
            try {
                object.put("api_key", Contants.API_KEY);
                object.put("home_id", myHomeList.get(postion).getId());
            } catch (JSONException e) {
                e.printStackTrace();
            }

            String serviceURL = Contants.BASE_URL + Contants.deletehome;

            ServiceCaller serviceCaller = new ServiceCaller(mContext);
            serviceCaller.CallCommanServiceMethod(serviceURL, object, "homeDelete", new IAsyncWorkCompletedCallback() {
                @Override
                public void onDone(String result, boolean isComplete) {
                    if (isComplete) {
                        final ServiceContentData serviceData = new Gson().fromJson(result, ServiceContentData.class);
                        if (serviceData != null) {
                            if (serviceData.isSuccess()) {
                                Intent intent = new Intent(mContext, SplashScreenActivity.class);
                                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                                mContext.startActivity(intent);
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

    private void deletePopup(int postion) {

        AlertDialog.Builder alertDialog = new AlertDialog.Builder(mContext);
        alertDialog.setTitle("Warning");
        alertDialog.setMessage("Are you Sure you want to detele this Home ?");
        alertDialog.setPositiveButton("Delete", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                homeDelete(postion);
                dialog.dismiss();
            }
        });

        alertDialog.setNegativeButton("No", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();

            }
        });
        alertDialog.show();
    }

/*    //get my home data
    private void getMyHome(int postion) {
        if (Utility.isOnline(mContext)) {
            homeDialog = new ASTProgressBar(mContext);
            JSONObject object = new JSONObject();
            try {
                object.put("api_key", Contants.API_KEY);
                object.put("user_id", myHomeList.remove(postion).getUser_id());
            } catch (JSONException e) {
                e.printStackTrace();
            }

            String serviceURL = Contants.BASE_URL + Contants.MyHome;

            ServiceCaller serviceCaller = new ServiceCaller(mContext);
            serviceCaller.CallCommanServiceMethod(serviceURL, object, "getMyHome", new IAsyncWorkCompletedCallback() {
                @Override
                public void onDone(String result, boolean isComplete) {
                    if (isComplete) {
                        parseHomeServiceData(result,postion);
                    } else {
                        if (homeDialog.isShowing()) {
                            homeDialog.dismiss();
                        }
                        Utility.alertForErrorMessage(Contants.Error, mContext);
                    }
                }
            });
        } else {
            Utility.alertForErrorMessage(Contants.OFFLINE_MESSAGE, mContext);//off line msg....
        }
    }

    public void parseHomeServiceData(String result,int postion) {
        if (result != null) {
            final ServiceContentData serviceData = new Gson().fromJson(result, ServiceContentData.class);
            if (serviceData != null) {
                SwitchDBHelper switchDBHelper = new SwitchDBHelper(mContext);
                switchDBHelper.deleteAllRows("Myhomedata");
                switchDBHelper.deleteAllRows("MychoiceData");
                switchDBHelper.deleteAllRows("LikedmychoiceData");
                if (serviceData.isSuccess()) {
                    if (serviceData.getData() != null) {
                        new AsyncTask<Void, Void, Boolean>() {
                            @Override
                            protected Boolean doInBackground(Void... voids) {
                                Boolean flag = false;

                                for (MyhomeArray myhomeArray : serviceData.getData().getMyhomeArray()) {
                                    switchDBHelper.insertMyhomedata(myhomeArray);
                                }
                                for (MychoiceArray mychoiceArray : serviceData.getData().getMychoiceArray()) {
                                    switchDBHelper.inserMychoiceData(mychoiceArray);
                                }
                                for (LikedmychoiceArray mychoiceArray : serviceData.getData().getLikedmychoiceArray()) {
                                    switchDBHelper.inserLikedmychoiceData(mychoiceArray);
                                }
                                flag = true;
                                return flag;
                            }

                            @Override
                            protected void onPostExecute(Boolean flag) {
                                super.onPostExecute(flag);
                                if (flag) {
                                }

                                if (homeDialog.isShowing()) {
                                    homeDialog.dismiss();
                                }
                            }
                        }.execute();
                    }
                }
                if (homeDialog.isShowing()) {
                    homeDialog.dismiss();
                }
            }
        }
    }*/
}

