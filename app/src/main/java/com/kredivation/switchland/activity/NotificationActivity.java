package com.kredivation.switchland.activity;

import android.annotation.SuppressLint;
import android.app.Notification;
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

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.Locale;

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
        @SuppressLint("WrongConstant") RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(NotificationActivity.this, LinearLayoutManager.VERTICAL, false);
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


                                            Collections.sort(datalist, new Comparator<Data>() {
                                                //2016-07-08 12:06:30
                                                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.ENGLISH);

                                                public int compare(Data m1, Data m2) {
                                                    Date date1 = null;
                                                    Date date2 = null;
                                                    int shortdate = 0;
                                                    try {
                                                        date1 = sdf.parse(m1.getAdded_date());
                                                        date2 = sdf.parse(m2.getAdded_date());
                                                        // Log.d(Contants.LOG_TAG, "date**********   " + date1 + "***" + date2);
                                                        //Log.d(Contants.LOG_TAG, " output.format(date1);**********   " + output.format(date1));
                                                        if (date1 != null && date2 != null) {
                                                            shortdate = date1.getTime() > date2.getTime() ? -1 : 1;//descending
                                                            //shortdate = date1.getTime() > date2.getTime() ? 1 : -1;     //ascending
                                                        }
                                                    } catch (ParseException e) {
                                                        e.printStackTrace();
                                                    }
                                                    return shortdate;
                                                }
                                            });



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
