package com.kredivation.switchland.activity;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.text.Html;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.kredivation.switchland.R;
import com.kredivation.switchland.adapters.AppTourPagerAdapter;
import com.kredivation.switchland.utilities.CompatibilityUtility;

import java.util.ArrayList;


public class AppTourActivity extends AppCompatActivity implements ViewPager.OnPageChangeListener, View.OnClickListener {
    private ViewPager viewPager;
    private TextView btnNext, btnFinish;
    private LinearLayout pager_indicator;
    private int dotsCount;
    private TextView[] dots;
    private AppTourPagerAdapter mAdapter;
    private ArrayList<Integer> tourImageList;
    private Boolean CheckOrientation = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_app_tour);
        if (CompatibilityUtility.isTablet(AppTourActivity.this)) {
            CheckOrientation = true;
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        } else {
            CheckOrientation = false;
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        }
        init();
    }

    //get id's
    public void init() {
        tourImageList = new ArrayList<Integer>();
        viewPager = (ViewPager) findViewById(R.id.pager);
        btnNext = (TextView) findViewById(R.id.btn_next);
        btnFinish = (TextView) findViewById(R.id.btn_finish);
        pager_indicator = (LinearLayout) findViewById(R.id.viewPagerCountDots);
        btnNext.setOnClickListener(this);
        btnFinish.setOnClickListener(this);
        setValueIntoList();
    }

    //set value based on tab or phone
    private void setValueIntoList() {

        tourImageList.add(R.layout.welcome_tour1);
        tourImageList.add(R.layout.welcome_tour2);
        tourImageList.add(R.layout.welcome_tour3);
        tourImageList.add(R.layout.welcome_tour4);
        mAdapter = new AppTourPagerAdapter(AppTourActivity.this, tourImageList,CheckOrientation);
        viewPager.setAdapter(mAdapter);
        viewPager.setCurrentItem(0);
        viewPager.addOnPageChangeListener(this);
        setUiPageViewController();
    }

    private void setUiPageViewController() {
        dotsCount = mAdapter.getCount();
        dots = new TextView[dotsCount];

        for (int i = 0; i < dotsCount; i++) {
            dots[i] = new TextView(this);
            dots[i].setText(Html.fromHtml("&#8226;"));
            dots[i].setTextSize(40);
            dots[i].setTextColor(Color.parseColor("#b3b3b3"));
            dots[i].setBackgroundColor(Color.TRANSPARENT);
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.WRAP_CONTENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT);

            params.setMargins(8, 0, 8, 8);
            pager_indicator.addView(dots[i], params);
        }
        dots[0].setTextColor(Color.parseColor("#F75C1E"));
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_next:

                //  viewPager.setCurrentItem((viewPager.getCurrentItem() < dotsCount)
                //   ? viewPager.getCurrentItem() + 1 : 0); //for next page show
                navigate();
                break;
            case R.id.btn_finish:
                navigate();
                break;
        }
    }

    //navigate to screen
    private void navigate() {
        Intent intent = new Intent(AppTourActivity.this, SigninActivity.class);
       // intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);

    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {
        for (int i = 0; i < dotsCount; i++) {
            dots[i].setTextColor(Color.parseColor("#b3b3b3"));
        }
        dots[position].setTextColor(Color.parseColor("#F75C1E"));
        if (position + 1 == dotsCount) {
            btnNext.setVisibility(View.GONE);
            btnFinish.setVisibility(View.VISIBLE);

        } else {
            btnNext.setVisibility(View.VISIBLE);
            btnFinish.setVisibility(View.GONE);
        }
    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }
}
