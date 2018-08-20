package com.kredivation.switchland.activity;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.gson.Gson;
import com.kredivation.switchland.R;
import com.kredivation.switchland.adapters.MyHomeAdapter;
import com.kredivation.switchland.database.SwitchDBHelper;
import com.kredivation.switchland.framework.IAsyncWorkCompletedCallback;
import com.kredivation.switchland.framework.ServiceCaller;
import com.kredivation.switchland.model.Data;
import com.kredivation.switchland.model.ServiceContentData;
import com.kredivation.switchland.utilities.ASTProgressBar;
import com.kredivation.switchland.utilities.CompatibilityUtility;
import com.kredivation.switchland.utilities.Contants;
import com.kredivation.switchland.utilities.FontManager;
import com.kredivation.switchland.utilities.Utility;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class HomeDetailActivity extends AppCompatActivity implements View.OnClickListener {
    private Toolbar toolbar;
    private String userId;
    ASTProgressBar dotDialog;
    String HomeId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_detail);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if (CompatibilityUtility.isTablet(HomeDetailActivity.this)) {
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        } else {
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        }
        init();
    }

    private void init() {
        HomeId = getIntent().getStringExtra("HomeId");
        Typeface materialdesignicons_font = FontManager.getFontTypefaceMaterialDesignIcons(this, "fonts/materialdesignicons-webfont.otf");
        TextView back = toolbar.findViewById(R.id.back);
        back.setTypeface(materialdesignicons_font);
        back.setText(Html.fromHtml("&#xf30d;"));
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        Button edit = findViewById(R.id.edit);
        edit.setOnClickListener(this);
        SwitchDBHelper switchDBHelper = new SwitchDBHelper(HomeDetailActivity.this);
        //myHomeList = switchDBHelper.getAllMyhomedata();

        getUserData();
    }

    private void getUserData() {
        SwitchDBHelper switchDBHelper = new SwitchDBHelper(HomeDetailActivity.this);
        ArrayList<Data> userData = switchDBHelper.getAllUserInfoList();
        if (userData != null && userData.size() > 0) {
            for (Data data : userData) {
                userId = data.getId();
            }
           // getHomeDetail();
        }
    }

    //get home detail
    private void getHomeDetail() {
        if (Utility.isOnline(HomeDetailActivity.this)) {
            dotDialog = new ASTProgressBar(HomeDetailActivity.this);
            JSONObject object = new JSONObject();
            try {
                object.put("api_key", Contants.API_KEY);
                object.put("user_id", userId);
                object.put("home_id", HomeId);
            } catch (JSONException e) {
                e.printStackTrace();
            }

            String serviceURL = Contants.BASE_URL + Contants.Homedetails;

            ServiceCaller serviceCaller = new ServiceCaller(HomeDetailActivity.this);
            serviceCaller.CallCommanServiceMethod(serviceURL, object, "getHomeDetail", new IAsyncWorkCompletedCallback() {
                @Override
                public void onDone(String result, boolean isComplete) {
                    if (isComplete) {
                        parseAllHomeServiceData(result);
                    } else {
                        if (dotDialog.isShowing()) {
                            dotDialog.dismiss();
                        }
                        Utility.alertForErrorMessage(Contants.Error, HomeDetailActivity.this);
                    }
                }
            });
        } else {
            Utility.alertForErrorMessage(Contants.OFFLINE_MESSAGE, HomeDetailActivity.this);//off line msg....
        }
    }

    public void parseAllHomeServiceData(String result) {
        if (result != null) {
            final ServiceContentData serviceData = new Gson().fromJson(result, ServiceContentData.class);
            if (serviceData != null) {
                if (serviceData.isSuccess()) {
                    if (serviceData.getData() != null) {
                        new AsyncTask<Void, Void, Boolean>() {
                            @Override
                            protected Boolean doInBackground(Void... voids) {
                                Boolean flag = false;
                               /* SwitchDBHelper switchDBHelper = new SwitchDBHelper(MyHomeActivity.this);
                                switchDBHelper.deleteAllRows("MasterData");
                                switchDBHelper.deleteAllRows("MychoiceData");
                                switchDBHelper.deleteAllRows("LikedmychoiceData");
                                switchDBHelper.insertMyhomedata(serviceData.getData());
                                for (MychoiceArray mychoiceArray : serviceData.getData().getMychoiceArray()) {
                                    switchDBHelper.inserMychoiceData(mychoiceArray);
                                }*/
                                flag = true;
                                return flag;
                            }

                            @Override
                            protected void onPostExecute(Boolean flag) {
                                super.onPostExecute(flag);
                                if (flag) {

                                }
                                if (dotDialog.isShowing()) {
                                    dotDialog.dismiss();
                                }
                            }
                        }.execute();
                    }
                }
                if (dotDialog.isShowing()) {
                    dotDialog.dismiss();
                }
            }
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.edit:
                Intent homeintent = new Intent(HomeDetailActivity.this, AddHomeActivity.class);
               /* MyhomeArray myHome = myHomeList.get(position);
                String homeStr = new Gson().toJson(myHome);
                Utility.setHomeDetail(mContext, homeStr, true);*/
                startActivity(homeintent);
                break;
        }
    }
}
