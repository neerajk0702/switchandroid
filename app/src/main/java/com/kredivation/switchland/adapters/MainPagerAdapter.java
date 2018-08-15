package com.kredivation.switchland.adapters;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.kredivation.switchland.fragment.ChatListFragment;
import com.kredivation.switchland.fragment.CreateFirstTimePostFragment;
import com.kredivation.switchland.fragment.MyProfileFragment;
import com.kredivation.switchland.fragment.TinderFragment;

public class MainPagerAdapter extends FragmentStatePagerAdapter {

    //integer to count number of tabs
    int tabCount;

    //Constructor to the class
    public MainPagerAdapter(FragmentManager fm, int tabCount) {
        super(fm);
        //Initializing tab count
        this.tabCount = tabCount;
    }

    //Overriding method getItem
    @Override
    public Fragment getItem(int position) {
        //Returning the current tabs
        switch (position) {
            case 0:
                Fragment tab1 = MyProfileFragment.newInstance("", "");
                return tab1;
            case 1:

                Fragment tab2 = TinderFragment.newInstance("", "");
                return tab2;
            case 2:
                Fragment tab3 = ChatListFragment.newInstance("", "");
                return tab3;
            default:
                return null;
        }
    }

    //Overriden method getCount to get the number of tabs
    @Override
    public int getCount() {
        return tabCount;
    }
}
