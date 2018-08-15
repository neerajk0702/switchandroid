package com.kredivation.switchland.fragment;


import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.kredivation.switchland.R;
import com.kredivation.switchland.activity.AddHomeActivity;
import com.kredivation.switchland.activity.AppTourActivity;
import com.kredivation.switchland.activity.ChangePasswordActivity;
import com.kredivation.switchland.activity.EditProfileActivity;
import com.kredivation.switchland.activity.SettingActivity;
import com.kredivation.switchland.activity.SplashScreenActivity;
import com.kredivation.switchland.adapters.MainPagerAdapter;
import com.kredivation.switchland.database.SwitchDBHelper;
import com.kredivation.switchland.model.Data;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link MyProfileFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class MyProfileFragment extends Fragment implements TabLayout.OnTabSelectedListener, View.OnClickListener {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;


    public MyProfileFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment MyProfileFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static MyProfileFragment newInstance(String param1, String param2) {
        MyProfileFragment fragment = new MyProfileFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    private TabLayout tabLayout;
    private Context context;
    private View view;
    LinearLayout settingLayout, editLayout, addHomeLayout;
    private String userId;
    ImageView proImage;
    private TextView name, email, phone;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        context = getActivity();
        view = inflater.inflate(R.layout.fragment_my_profile, container, false);
        init();
        return view;
    }

    private void init() {
        proImage = view.findViewById(R.id.proImage);
        name = view.findViewById(R.id.name);
        email = view.findViewById(R.id.email);
        phone = view.findViewById(R.id.phone);
        tabLayout = view.findViewById(R.id.tabLayout);

        tabLayout.addTab(tabLayout.newTab().setText("My Home"));
        tabLayout.addTab(tabLayout.newTab().setText("My Choices"));
        tabLayout.addTab(tabLayout.newTab().setText("Liked My Choices"));

        tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);

        //viewPager = view.findViewById(R.id.pager);
        MainPagerAdapter adapter = new MainPagerAdapter(getActivity().getSupportFragmentManager(), tabLayout.getTabCount());
        // viewPager.setAdapter(adapter);
        // viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
        //Adding onTabSelectedListener to swipe views
        tabLayout.setOnTabSelectedListener(this);
        //TabLayout.Tab tab = tabLayout.getTabAt(1);
        // tab.select();
        //setupTabIcons();
        settingLayout = view.findViewById(R.id.settingLayout);
        settingLayout.setOnClickListener(this);
        editLayout = view.findViewById(R.id.editLayout);
        editLayout.setOnClickListener(this);
        addHomeLayout = view.findViewById(R.id.addHomeLayout);
        addHomeLayout.setOnClickListener(this);
        getUserdata();
    }

    private void getUserdata() {
        SwitchDBHelper switchDBHelper = new SwitchDBHelper(context);
        ArrayList<Data> userData = switchDBHelper.getAllUserInfoList();
        if (userData != null && userData.size() > 0) {
            for (Data data : userData) {
                userId = data.getId();
                name.setText(data.getFirst_name() + " " + data.getLast_name());
                email.setText(data.getEmail());
                phone.setText(data.getMobile_number());
                Picasso.with(context).load(data.getProfile_image()).placeholder(R.drawable.pimage).resize(80, 80).into(proImage);
            }
        }
    }
   /* private void setupTabIcons() {
        tabLayout.getTabAt(0).setIcon(R.drawable.ic_user);
        tabLayout.getTabAt(1).setIcon(R.drawable.ic_switchicon);
        tabLayout.getTabAt(2).setIcon(R.drawable.ic_chat);
    }*/

    @Override
    public void onTabSelected(TabLayout.Tab tab) {
        // viewPager.setCurrentItem(tab.getPosition());
    }

    @Override
    public void onTabUnselected(TabLayout.Tab tab) {

    }

    @Override
    public void onTabReselected(TabLayout.Tab tab) {

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.settingLayout:
                Intent intent = new Intent(context, SettingActivity.class);
                startActivity(intent);
                break;
            case R.id.editLayout:
                Intent editintent = new Intent(context, EditProfileActivity.class);
                startActivity(editintent);
                break;
            case R.id.addHomeLayout:
                Intent homeintent = new Intent(context, AddHomeActivity.class);
                startActivity(homeintent);
                break;
        }
    }
}
