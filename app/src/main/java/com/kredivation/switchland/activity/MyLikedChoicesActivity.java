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
import com.google.gson.reflect.TypeToken;
import com.kredivation.switchland.R;
import com.kredivation.switchland.adapters.MyChoicesAdapter;
import com.kredivation.switchland.adapters.MyLikedChoicesAdapter;
import com.kredivation.switchland.database.SwitchDBHelper;
import com.kredivation.switchland.model.Data;
import com.kredivation.switchland.model.LikedmychoiceArray;
import com.kredivation.switchland.model.MychoiceArray;
import com.kredivation.switchland.utilities.CompatibilityUtility;
import com.kredivation.switchland.utilities.FontManager;

import java.util.ArrayList;
import java.util.Arrays;

public class MyLikedChoicesActivity extends AppCompatActivity {
    private Toolbar toolbar;
    RecyclerView recyclerView;
    private String userId;
    private ArrayList<LikedmychoiceArray> myHomeList;

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

        SwitchDBHelper switchDBHelper = new SwitchDBHelper(MyLikedChoicesActivity.this);
        myHomeList = switchDBHelper.getAllLikedmychoiceData();
        getUserData();
        recyclerView = findViewById(R.id.recycler_view);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(MyLikedChoicesActivity.this, LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        if (myHomeList != null && myHomeList.size() > 0) {
            MyLikedChoicesAdapter myHomeAdapter = new MyLikedChoicesAdapter(MyLikedChoicesActivity.this, myHomeList);
            recyclerView.setAdapter(myHomeAdapter);
        }
    }

    private void getUserData() {
        SwitchDBHelper switchDBHelper = new SwitchDBHelper(MyLikedChoicesActivity.this);
        ArrayList<Data> userData = switchDBHelper.getAllUserInfoList();
        if (userData != null && userData.size() > 0) {
            for (Data data : userData) {
                userId = data.getId();
            }
        }
    }
}
