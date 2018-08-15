package com.kredivation.switchland.activity;

import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;

import com.kredivation.switchland.R;
import com.kredivation.switchland.adapters.MainPagerAdapter;

public class MainActivity extends AppCompatActivity implements TabLayout.OnTabSelectedListener {

    private TabLayout tabLayout;
    private ViewPager viewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        // setSupportActionBar(toolbar);

        //Initializing the tablayout
        tabLayout = (TabLayout) findViewById(R.id.tabLayout);

       /* tabLayout.addTab(tabLayout.newTab().setText("Tab1"));
        tabLayout.addTab(tabLayout.newTab().setText("Tab2"));
        tabLayout.addTab(tabLayout.newTab().setText("Tab3"));*/

        tabLayout.addTab(tabLayout.newTab());
        tabLayout.addTab(tabLayout.newTab());
        tabLayout.addTab(tabLayout.newTab());
        tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);

        viewPager = (ViewPager) findViewById(R.id.pager);
        MainPagerAdapter adapter = new MainPagerAdapter(getSupportFragmentManager(), tabLayout.getTabCount());
        viewPager.setAdapter(adapter);
        viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
        //Adding onTabSelectedListener to swipe views
        tabLayout.setOnTabSelectedListener(this);
        TabLayout.Tab tab = tabLayout.getTabAt(1);
        tab.select();
        setupTabIcons();
    }

    private void setupTabIcons() {
        tabLayout.getTabAt(0).setIcon(R.drawable.ic_user);
        tabLayout.getTabAt(1).setIcon(R.drawable.ic_switchicon);
        tabLayout.getTabAt(2).setIcon(R.drawable.ic_chat);
    }

    @Override
    public void onTabSelected(TabLayout.Tab tab) {
        viewPager.setCurrentItem(tab.getPosition());
        if (tab.getPosition() == 0) {
            tabLayout.getTabAt(0).setIcon(R.drawable.ic_user_select);
        } else {
            tabLayout.getTabAt(0).setIcon(R.drawable.ic_user);
        }
        if (tab.getPosition() == 1) {
            tabLayout.getTabAt(1).setIcon(R.drawable.ic_switchicon);
        } else {
            tabLayout.getTabAt(1).setIcon(R.drawable.ic_switchicon_not);
        }
        if (tab.getPosition() == 2) {
            tabLayout.getTabAt(2).setIcon(R.drawable.ic_chat_select);
        } else {
            tabLayout.getTabAt(2).setIcon(R.drawable.ic_chat);
        }
    }

    @Override
    public void onTabUnselected(TabLayout.Tab tab) {

    }

    @Override
    public void onTabReselected(TabLayout.Tab tab) {

    }
}
