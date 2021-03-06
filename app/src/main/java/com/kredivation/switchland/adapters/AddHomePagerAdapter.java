package com.kredivation.switchland.adapters;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;

import com.kredivation.switchland.fragment.AddHomeCardDetailFragment;
import com.kredivation.switchland.fragment.AddHomeDetailFragment;
import com.kredivation.switchland.fragment.AddHomeLocationFragment;
import com.kredivation.switchland.fragment.AddHomeMyProfileFragment;
import com.kredivation.switchland.fragment.AddHomeOverviewFragment;
import com.kredivation.switchland.fragment.AddHomePhotoFragment;

public class AddHomePagerAdapter extends FragmentStatePagerAdapter {
    int mNumOfPage = 6;

    public AddHomePagerAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {

        switch (position) {
            case 0:
                AddHomeOverviewFragment tab1 = AddHomeOverviewFragment.newInstance("", "");
                return tab1;
            case 1:
                AddHomeDetailFragment tab2 = AddHomeDetailFragment.newInstance("", "");
                return tab2;
            case 2:
                AddHomeLocationFragment tab3 = AddHomeLocationFragment.newInstance("", "");
                return tab3;
            case 3:
                AddHomePhotoFragment tab4 = AddHomePhotoFragment.newInstance("", "");
                return tab4;
            case 4:
                AddHomeMyProfileFragment tab5 = AddHomeMyProfileFragment.newInstance("", "");
                return tab5;
            case 5:
                AddHomeCardDetailFragment tab6 = AddHomeCardDetailFragment.newInstance("", "");
                return tab6;
            default:
                return null;
        }
    }

    @Override
    public int getCount() {
        return mNumOfPage;
    }
}

