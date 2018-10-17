package com.kredivation.switchland.activity;

import android.content.pm.ActivityInfo;
import android.graphics.Typeface;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.view.View;
import android.widget.TextView;

import com.google.gson.Gson;
import com.kredivation.switchland.R;
import com.kredivation.switchland.adapters.LikedUsersAdapter;
import com.kredivation.switchland.adapters.NotificationAdapter;
import com.kredivation.switchland.database.SwitchDBHelper;
import com.kredivation.switchland.framework.IAsyncWorkCompletedCallback;
import com.kredivation.switchland.framework.ServiceCaller;
import com.kredivation.switchland.model.ChatServiceContentData;
import com.kredivation.switchland.model.Data;
import com.kredivation.switchland.utilities.ASTProgressBar;
import com.kredivation.switchland.utilities.CompatibilityUtility;
import com.kredivation.switchland.utilities.Contants;
import com.kredivation.switchland.utilities.FontManager;
import com.kredivation.switchland.utilities.Utility;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;

public class NotificationActivity extends AppCompatActivity {
    private Toolbar toolbar;
    RecyclerView recyclerView;
    private String userId;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notification);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if (CompatibilityUtility.isTablet(NotificationActivity.this)) {
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        } else {
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        }
        init();
    }

    private void init() {
        Typeface materialdesignicons_font = FontManager.getFontTypefaceMaterialDesignIcons(this, "fonts/materialdesignicons-webfont.otf");
        TextView back = (TextView) toolbar.findViewById(R.id.back);
        back.setTypeface(materialdesignicons_font);
        back.setText(Html.fromHtml("&#xf30d;"));
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        recyclerView = findViewById(R.id.recycler_view);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(NotificationActivity.this, LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        getUserData();
    }

    private void getUserData() {
        SwitchDBHelper switchDBHelper = new SwitchDBHelper(NotificationActivity.this);
        ArrayList<Data> userData = switchDBHelper.getAllUserInfoList();
        if (userData != null && userData.size() > 0) {
            for (Data data : userData) {
                userId = data.getId();
            }
            getNotification();
        }
    }

    private void getNotification() {
        if (Utility.isOnline(NotificationActivity.this)) {
            final ASTProgressBar chatlistProgress = new ASTProgressBar(NotificationActivity.this);
            chatlistProgress.show();
            JSONObject object = new JSONObject();
            try {
                object.put("api_key", Contants.API_KEY);
                object.put("user_id", userId);
            } catch (JSONException e) {
                e.printStackTrace();
            }

            String serviceURL = Contants.BASE_URL + Contants.getNotificationMessage;

            ServiceCaller serviceCaller = new ServiceCaller(NotificationActivity.this);
            serviceCaller.CallCommanServiceMethod(serviceURL, object, "getNotification", new IAsyncWorkCompletedCallback() {
                @Override
                public void onDone(String result, boolean isComplete) {
                    if (isComplete) {
                        final ChatServiceContentData serviceData = new Gson().fromJson(result, ChatServiceContentData.class);
                        if (serviceData != null) {
                            if (serviceData.isSuccess()) {
                                if (serviceData.getData() != null) {
                                    ArrayList<Data> datalist = new ArrayList<Data>(Arrays.asList(serviceData.getData()));
                                    if (datalist != null) {
                                        if (datalist != null && datalist.size() > 0) {
                                            NotificationAdapter usersAdapter = new NotificationAdapter(NotificationActivity.this, datalist);
                                            recyclerView.setAdapter(usersAdapter);
                                        }
                                    }
                                } else {
                                    Utility.showToast(NotificationActivity.this, "Notification Not found!");
                                }
                            } else {
                                Utility.showToast(NotificationActivity.this, "Notification Not found!");
                            }
                        }
                    } else {
                        Utility.alertForErrorMessage(Contants.Error, NotificationActivity.this);
                    }
                    if (chatlistProgress.isShowing()) {
                        chatlistProgress.dismiss();
                    }
                }
            });
        } else {
            Utility.alertForErrorMessage(Contants.OFFLINE_MESSAGE, NotificationActivity.this);//off line msg....
        }
    }
}
