package com.kredivation.switchland.activity;

import android.content.pm.ActivityInfo;
import android.graphics.Typeface;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.appcompat.widget.Toolbar;
import android.text.Html;
import android.view.View;
import android.widget.TextView;

import com.google.gson.Gson;
import com.kredivation.switchland.R;
import com.kredivation.switchland.adapters.MyLikedChoicesAdapter;
import com.kredivation.switchland.database.SwitchDBHelper;
import com.kredivation.switchland.framework.IAsyncWorkCompletedCallback;
import com.kredivation.switchland.framework.ServiceCaller;
import com.kredivation.switchland.model.Data;
import com.kredivation.switchland.model.LikedmychoiceArray;
import com.kredivation.switchland.model.MychoiceArray;
import com.kredivation.switchland.model.MyhomeArray;
import com.kredivation.switchland.model.ServiceContentData;
import com.kredivation.switchland.utilities.ASTProgressBar;
import com.kredivation.switchland.utilities.CompatibilityUtility;
import com.kredivation.switchland.utilities.Contants;
import com.kredivation.switchland.utilities.FontManager;
import com.kredivation.switchland.utilities.Utility;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class MyLikedChoicesActivity extends AppCompatActivity {
    private Toolbar toolbar;
    RecyclerView recyclerView;
    private String userId;
    private ArrayList<LikedmychoiceArray> myHomeList;
    ASTProgressBar dotDialog;
    SwitchDBHelper switchDBHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_liked_choices);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if (CompatibilityUtility.isTablet(MyLikedChoicesActivity.this)) {
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        } else {
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        }
        init();
    }

    private void init() {
        dotDialog = new ASTProgressBar(MyLikedChoicesActivity.this);
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
        switchDBHelper = new SwitchDBHelper(MyLikedChoicesActivity.this);
        recyclerView = findViewById(R.id.recycler_view);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(MyLikedChoicesActivity.this, LinearLayoutManager.HORIZONTAL, false);
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setHasFixedSize(false);
        getUserData();
    }

    private void getUserData() {
        SwitchDBHelper switchDBHelper = new SwitchDBHelper(MyLikedChoicesActivity.this);
        ArrayList<Data> userData = switchDBHelper.getAllUserInfoList();
        if (userData != null && userData.size() > 0) {
            for (Data data : userData) {
                userId = data.getId();
            }
        }
        getMyHome();
    }

    private void setAdapter() {
        myHomeList = switchDBHelper.getAllLikedmychoiceData();
        if (myHomeList != null && myHomeList.size() > 0) {
            MyLikedChoicesAdapter myHomeAdapter = new MyLikedChoicesAdapter(MyLikedChoicesActivity.this, myHomeList);
            recyclerView.setAdapter(myHomeAdapter);
        }
    }

    //get my home data
    private void getMyHome() {
        if (Utility.isOnline(MyLikedChoicesActivity.this)) {
            //  homeDialog = new ASTProgressBar(SplashScreenActivity.this);
            dotDialog.show();
            JSONObject object = new JSONObject();
            try {
                object.put("api_key", Contants.API_KEY);
                object.put("user_id", userId);
            } catch (JSONException e) {
                e.printStackTrace();
            }

            String serviceURL = Contants.BASE_URL + Contants.MyHome;

            ServiceCaller serviceCaller = new ServiceCaller(MyLikedChoicesActivity.this);
            serviceCaller.CallCommanServiceMethod(serviceURL, object, "getMyHome", new IAsyncWorkCompletedCallback() {
                @Override
                public void onDone(String result, boolean isComplete) {
                    if (isComplete) {
                        parseHomeServiceData(result);
                    } else {
                        if (dotDialog.isShowing()) {
                            dotDialog.dismiss();
                        }
                        Utility.alertForErrorMessage(Contants.Error, MyLikedChoicesActivity.this);
                    }
                }
            });
        } else {
            Utility.alertForErrorMessage(Contants.OFFLINE_MESSAGE, MyLikedChoicesActivity.this);//off line msg....
        }
    }

    public void parseHomeServiceData(String result) {
        if (result != null) {
            final ServiceContentData serviceData = new Gson().fromJson(result, ServiceContentData.class);
            if (serviceData != null) {
                SwitchDBHelper switchDBHelper = new SwitchDBHelper(MyLikedChoicesActivity.this);
                switchDBHelper.deleteAllRows("Myhomedata");
                switchDBHelper.deleteAllRows("MychoiceData");
                switchDBHelper.deleteAllRows("LikedmychoiceData");
                if (serviceData.isSuccess()) {
                    if (serviceData.getData() != null) {
                        for (MyhomeArray myhomeArray : serviceData.getData().getMyhomeArray()) {
                            switchDBHelper.insertMyhomedata(myhomeArray);
                        }
                        for (MychoiceArray mychoiceArray : serviceData.getData().getMychoiceArray()) {
                            switchDBHelper.inserMychoiceData(mychoiceArray);
                        }
                        for (LikedmychoiceArray mychoiceArray : serviceData.getData().getLikedmychoiceArray()) {
                            switchDBHelper.inserLikedmychoiceData(mychoiceArray);
                        }
                    }
                }
            }
        }
        if (dotDialog.isShowing()) {
            dotDialog.dismiss();
        }
        setAdapter();
    }
}
