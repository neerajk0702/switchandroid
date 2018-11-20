package com.kredivation.switchland.fragment;


import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.gson.Gson;
import com.kredivation.switchland.R;
import com.kredivation.switchland.activity.AddHomeActivity;
import com.kredivation.switchland.activity.AppTourActivity;
import com.kredivation.switchland.activity.ChangePasswordActivity;
import com.kredivation.switchland.activity.ChatActivity;
import com.kredivation.switchland.activity.ConfirmDetailActivity;
import com.kredivation.switchland.activity.EditProfileActivity;
import com.kredivation.switchland.activity.HomeDetailActivity;
import com.kredivation.switchland.activity.MyChoicesActivity;
import com.kredivation.switchland.activity.MyHomeActivity;
import com.kredivation.switchland.activity.MyLikedChoicesActivity;
import com.kredivation.switchland.activity.MyProfileFilterActivity;
import com.kredivation.switchland.activity.SettingActivity;
import com.kredivation.switchland.activity.SplashScreenActivity;
import com.kredivation.switchland.activity.TravelRoutineActivity;
import com.kredivation.switchland.adapters.MainPagerAdapter;
import com.kredivation.switchland.database.SwitchDBHelper;
import com.kredivation.switchland.framework.IAsyncWorkCompletedCallback;
import com.kredivation.switchland.framework.ServiceCaller;
import com.kredivation.switchland.model.CheckHomeContent;
import com.kredivation.switchland.model.Data;
import com.kredivation.switchland.model.MychoiceArray;
import com.kredivation.switchland.model.MyhomeArray;
import com.kredivation.switchland.model.ServiceContentData;
import com.kredivation.switchland.utilities.ASTProgressBar;
import com.kredivation.switchland.utilities.Contants;
import com.kredivation.switchland.utilities.Utility;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link MyProfileFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class MyProfileFragment extends Fragment implements View.OnClickListener {
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

    private Context context;
    private View view;
    LinearLayout settingLayout, editLayout, addHomeLayout;
    private String userId;
    private String userHomeId = "";
    ImageView proImage;
    private TextView name, email, phone;
    ASTProgressBar dotDialog;
    String LikedmychoiceStr;
    String MychoiceStr;
    String MyhomeStr;
    TextView MyHome, Mychoices, Mylike;

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
        settingLayout = view.findViewById(R.id.settingLayout);
        settingLayout.setOnClickListener(this);
        editLayout = view.findViewById(R.id.editLayout);
        editLayout.setOnClickListener(this);
        addHomeLayout = view.findViewById(R.id.addHomeLayout);
        addHomeLayout.setOnClickListener(this);

        MyHome = view.findViewById(R.id.MyHome);
        MyHome.setOnClickListener(this);
        Mychoices = view.findViewById(R.id.Mychoices);
        Mychoices.setOnClickListener(this);
        Mylike = view.findViewById(R.id.Mylike);
        Mylike.setOnClickListener(this);
        LinearLayout filterLayout = view.findViewById(R.id.filterLayout);
        filterLayout.setOnClickListener(this);
        Button travelRoutine = view.findViewById(R.id.travelRoutine);
        travelRoutine.setOnClickListener(this);
        // getUserdata();
    }

    private void getUserdata() {
        SwitchDBHelper switchDBHelper = new SwitchDBHelper(context);
        ArrayList<Data> userData = switchDBHelper.getAllUserInfoList();
        if (userData != null && userData.size() > 0) {
            for (Data data : userData) {
                userId = data.getId();
                if (data.getFull_name() != null && !data.getFirst_name().equals("")) {
                    name.setText(data.getFull_name());
                } else {
                    name.setText(data.getFirst_name() + " " + data.getLast_name());
                }
                email.setText(data.getEmail());
                phone.setText(data.getMobile_number());
               // Picasso.with(context).load(data.getProfile_image()).placeholder(R.drawable.userimage).resize(80, 80).into(proImage);
            }
            ArrayList<MyhomeArray> myHomeList = switchDBHelper.getAllMyhomedata();
            if (myHomeList != null && myHomeList.size() > 0) {
                for (MyhomeArray myhomeArray : myHomeList) {
                    userHomeId = myhomeArray.getId();
                }
            }
            if (userHomeId != null && !userHomeId.equals("")) {
                //checkHomeCompleteOrnot();
            }
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        getUserdata();
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
            case R.id.MyHome:
                Intent myintent = new Intent(getContext(), MyHomeActivity.class);
                startActivity(myintent);
                break;
            case R.id.Mychoices:
                Intent choicesintent = new Intent(getContext(), MyChoicesActivity.class);
                startActivity(choicesintent);
                break;
            case R.id.Mylike:
                Intent likeintent = new Intent(getContext(), MyLikedChoicesActivity.class);
                startActivity(likeintent);
                break;
            case R.id.filterLayout:
                Intent filterintent = new Intent(getContext(), MyProfileFilterActivity.class);
                startActivity(filterintent);
                break;
            case R.id.travelRoutine:
                openTravelScreen();
                break;
        }
    }

    private void openTravelScreen() {
        String StartDate = "";
        String EndDate = "";
        String CountryId = "";
        String CityId = "";
        String myhomeId = "";
        SwitchDBHelper dbHelper = new SwitchDBHelper(getContext());
        ArrayList<MyhomeArray> myHomeList = dbHelper.getAllMyhomedata();
        if (myHomeList != null && myHomeList.size() > 0) {
            for (MyhomeArray data : myHomeList) {
                myhomeId = data.getId();
                StartDate = data.getStartdate();
                EndDate = data.getEnddate();
                CountryId = data.getTravel_country();
                CityId = data.getTravel_city();
            }

            Intent trintent = new Intent(getContext(), TravelRoutineActivity.class);
            dbHelper.deleteAllRows("AddEditHomeData");
            trintent.putExtra("StartDate", StartDate);
            trintent.putExtra("EndDate", EndDate);
            trintent.putExtra("CountryId", CountryId);
            trintent.putExtra("CityId", CityId);
            trintent.putExtra("HomeId", myhomeId);
            trintent.putExtra("MyHomeAdapterFlage", true);
            startActivity(trintent);
        }
    }

    //check your home complete or not
    private void checkHomeCompleteOrnot() {
        if (Utility.isOnline(getContext())) {
            JSONObject object = new JSONObject();
            try {
                object.put("api_key", Contants.API_KEY);
                object.put("home_id", userHomeId);
                object.put("user_id", userId);
            } catch (JSONException e) {
                e.printStackTrace();
            }

            String serviceURL = Contants.BASE_URL + Contants.Homecompeleted;

            ServiceCaller serviceCaller = new ServiceCaller(getContext());
            serviceCaller.CallCommanServiceMethod(serviceURL, object, "checkHomeCompleteOrnot", new IAsyncWorkCompletedCallback() {
                @Override
                public void onDone(String result, boolean isComplete) {
                    if (isComplete) {
                        parsecheckHomeCompleteOrnot(result);
                    } else {
                        Utility.alertForErrorMessage(Contants.Error, getContext());
                    }
                }
            });
        } else {
            Utility.alertForErrorMessage(Contants.OFFLINE_MESSAGE, getContext());//off line msg....
        }
    }

    public void parsecheckHomeCompleteOrnot(String result) {
        if (result != null) {
            final CheckHomeContent serviceData = new Gson().fromJson(result, CheckHomeContent.class);
            if (serviceData != null) {
                if (serviceData.isSuccess()) {

                } else {
                    alertForNoHomeAvailable(serviceData.getMsg());
                }
            }
        }
    }

    //no home available alert
    public void alertForNoHomeAvailable(String msg) {
        final AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        Typeface roboto_regular = Typeface.createFromAsset(getContext().getAssets(), "fonts/roboto.regular.ttf");
        final AlertDialog alert = builder.create();
        View view = alert.getLayoutInflater().inflate(R.layout.home_complete_popup, null);
        TextView message = view.findViewById(R.id.message);
        message.setTypeface(roboto_regular);
        message.setText(msg);
        Button ok = view.findViewById(R.id.ok);
        ok.setTypeface(roboto_regular);
        alert.setCustomTitle(view);
        ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alert.dismiss();
                Intent homeintent = new Intent(getContext(), HomeDetailActivity.class);
                homeintent.putExtra("HomeId", userHomeId);
                homeintent.putExtra("EditFlage", true);
                startActivity(homeintent);
            }
        });
        alert.show();
    }
}
