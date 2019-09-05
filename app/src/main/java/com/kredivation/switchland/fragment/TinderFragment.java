package com.kredivation.switchland.fragment;


import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.kredivation.switchland.R;
import com.kredivation.switchland.activity.DashboardActivity;
import com.kredivation.switchland.adapters.HomeTinderCardAdapter;
import com.kredivation.switchland.database.SwitchDBHelper;
import com.kredivation.switchland.fragment.likepagelib.KoldaListnerJava;
import com.kredivation.switchland.fragment.likepagelib.KoldaMain;
import com.kredivation.switchland.fragment.likepagelib.TinderCardAdapter;
import com.kredivation.switchland.framework.IAsyncWorkCompletedCallback;
import com.kredivation.switchland.framework.ServiceCaller;
import com.kredivation.switchland.model.Data;
import com.kredivation.switchland.model.FilterHome;
import com.kredivation.switchland.model.Home_data;
import com.kredivation.switchland.model.Home_liked_disliked;
import com.kredivation.switchland.model.MyhomeArray;
import com.kredivation.switchland.model.ServiceContentData;
import com.kredivation.switchland.utilities.ASTProgressBar;
import com.kredivation.switchland.utilities.Contants;
import com.kredivation.switchland.utilities.Utility;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link TinderFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class TinderFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String homeFilter;
    private String mParam2;


    public TinderFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment TinderFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static TinderFragment newInstance(String param1, String param2) {
        TinderFragment fragment = new TinderFragment();
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
            homeFilter = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    private TinderCardAdapter adapter;
    HomeTinderCardAdapter cardAdapter;
    private HashMap _$_findViewCache;
    Context context;
    private View view;
    ASTProgressBar likeDialog;
    private String userId;
    String startDate = "";
    String endDate = "";
    String countryId = "";
    String cityId = "";
    ArrayList<Home_data> homeList;
    ArrayList<Home_data> matchhomeList;
    FilterHome filterHome;
    String slieepId;
    String bedroomId = "";
    String genderId = "";
    String religionId = "";
    String travleId = "";
    // String likehomeId = "";
    // String likedUserId = "";
    KoldaMain frameLayout;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        context = getActivity();
        view = inflater.inflate(R.layout.fragment_tinder, container, false);
        initView();
        return view;
    }

    private void initView() {

        if (homeFilter != null && !homeFilter.equals("")) {
            filterHome = new Gson().fromJson(homeFilter, new TypeToken<FilterHome>() {
            }.getType());
        }

        ((LinearLayout) view.findViewById(R.id.activityMain)).getViewTreeObserver().addOnGlobalLayoutListener((ViewTreeObserver.OnGlobalLayoutListener) (new ViewTreeObserver.OnGlobalLayoutListener() {
            @SuppressLint({"NewApi"})
            public void onGlobalLayout() {
                int width = ((LinearLayout) view.findViewById(R.id.activityMain)).getWidth();
                int height = ((LinearLayout) view.findViewById(R.id.activityMain)).getHeight();
                if (Build.VERSION.SDK_INT >= 16) {
                    ((LinearLayout) view.findViewById(R.id.activityMain)).getViewTreeObserver().removeOnGlobalLayoutListener((ViewTreeObserver.OnGlobalLayoutListener) this);
                } else {
                    ((LinearLayout) view.findViewById(R.id.activityMain)).getViewTreeObserver().removeGlobalOnLayoutListener((ViewTreeObserver.OnGlobalLayoutListener) this);
                }

            }
        }));
        homeList = new ArrayList();
        matchhomeList = new ArrayList<>();
        this.initializeDeck();
        this.setUpCLickListeners();
        getUserdata();
    }

    private void initializeDeck() {
        ((KoldaMain) view.findViewById(R.id.koloda)).setKolodaListener((KoldaListnerJava) (new KoldaListnerJava() {
            private int cardsSwiped;

            public final int getCardsSwiped$production_sources_for_module_app() {
                return this.cardsSwiped;
            }

            public final void setCardsSwiped$production_sources_for_module_app(int var1) {
                this.cardsSwiped = var1;
            }

            public void onNewTopCard(int position) {
            }

            public void onCardSwipedLeft(int position) {
                likeDislike("2", position);
                // removeMasterListData(position);
            }

            public void onCardSwipedRight(int position) {
                likeDislike("1", position);
                //removeMasterListData(position);
            }

            public void onEmptyDeck() {
                /*if (homeList != null && homeList.size() > 0) {//show only when home exist in same country
                    alertForNoHomeAvailable();
                }*/
                //show only when home exist in same country

            }

            public void onCardDrag(int position, View cardView, float progress) {
                DefaultImpls.onCardDrag(this, position, cardView, progress);
            }

            public void onClickRight(int position) {
                DefaultImpls.onClickRight(this, position);
                //  Toast.makeText(getActivity(), "onClickRight=" + position, Toast.LENGTH_LONG).show();
                likeDislike("1", position);
                //removeMasterListData(position);
            }

            public void onClickLeft(int position) {
                DefaultImpls.onClickLeft(this, position);
                //Toast.makeText(getActivity(), "onClickLeft=" + position, Toast.LENGTH_LONG).show();
                likeDislike("2", position);
                //removeMasterListData(position);
            }

            public void onCardSingleTap(int position) {
                DefaultImpls.onCardSingleTap(this, position);
                // Toast.makeText(getActivity(), "onCardSingleTap="+position, Toast.LENGTH_LONG).show();
            }

            public void onCardDoubleTap(int position) {
                DefaultImpls.onCardDoubleTap(this, position);
                // Toast.makeText(getActivity(), "onCardDoubleTap="+position, Toast.LENGTH_LONG).show();
            }

            public void onCardLongPress(int position) {
                DefaultImpls.onCardLongPress(this, position);
                //Toast.makeText(getActivity(), "onCardLongPress="+position, Toast.LENGTH_LONG).show();
            }

        }));

        frameLayout = view.findViewById(R.id.koloda);
        frameLayout.setNeedCircleLoading(false);//for circle loading data after finish all card
        // setAdapterValue(matchhomeList);

    }

    private void setAdapterValue(ArrayList<Home_data> homes) {
        //this.adapter = new TinderCardAdapter(getContext(), Arrays.asList(data));
        //((KoldaMain) view.findViewById(R.id.koloda)).setAdapter((Adapter) this.adapter);
        cardAdapter = new HomeTinderCardAdapter(context, homes);
        frameLayout.setAdapter(cardAdapter);
        //((KoldaMain) view.findViewById(R.id.koloda)).setAdapter(cardAdapter);

    }

    private void setUpCLickListeners() {
        ImageView reminder = view.findViewById(R.id.activityMain).findViewById(R.id.reminder);
        ImageView dislike = view.findViewById(R.id.activityMain).findViewById(R.id.dislike);
        ImageView like = view.findViewById(R.id.activityMain).findViewById(R.id.like);
        final Button skip = view.findViewById(R.id.skip);
        // Utility.setBackgroundOval(context, skip, R.color.Black_A, Utility.getColor(context, R.color.gray), 0);
        //  Utility.setBackgroundRing(context, dislike, R.color.light_gray_color, Utility.getColor(context, R.color.light_gray_color), 0);
        // Utility.setBackgroundRing(context, like, R.color.light_gray_color, Utility.getColor(context, R.color.light_gray_color), 0);
        dislike.setOnClickListener((View.OnClickListener) (new View.OnClickListener() {
            public void onClick(View it) {
                ((KoldaMain) view.findViewById(R.id.koloda)).onClickLeft();
                //likeDislike("2",0);
            }
        }));
        like.setOnClickListener((View.OnClickListener) (new View.OnClickListener() {
            public void onClick(View it) {
                ((KoldaMain) view.findViewById(R.id.koloda)).onClickRight();
                // likeDislike("1",0);
            }
        }));

        skip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((KoldaMain) view.findViewById(R.id.koloda)).onClickSkip();
               /* if (homeList != null && homeList.size() > 0) {
                    homeList.remove(0);
                } else {
                    Toast.makeText(getActivity(), "No More Home Available", Toast.LENGTH_LONG).show();
                }*/
            }
        });

        reminder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferences prefs = context.getSharedPreferences("RewindPreferences", Context.MODE_PRIVATE);
                if (prefs != null) {
                    String likehomeId = prefs.getString("LikehomeId", "");
                    String likedUserId = prefs.getString("LikedUserId", "");
                    if (likehomeId != null && !likehomeId.equals("")) {
                        getLikeDislikCard(likehomeId, likedUserId, "3", 0);//3 for rewind
                    } else {
                        Toast.makeText(getActivity(), "No Home Available for rewind!", Toast.LENGTH_LONG).show();
                    }
                }

            }
        });
    }

    private void likeDislike(String status, int pos) {
        // int position=pos-1;
        if (matchhomeList != null && matchhomeList.size() > 0) {
            Home_data homeData = matchhomeList.get(0);
            if (homeData != null) {
                getLikeDislikCard(homeData.getId(), homeData.getUser_id(), status, 0);
            }
        } else {
            Toast.makeText(getActivity(), "No More Home Available", Toast.LENGTH_LONG).show();
        }
    }

    private void getUserdata() {
        SwitchDBHelper switchDBHelper = new SwitchDBHelper(context);
        if (filterHome != null && !filterHome.equals("")) {//come from filter screen
            startDate = filterHome.getStartDate();
            endDate = filterHome.getEndDate();
            cityId = filterHome.getCityId();//its travel cityid
            countryId = filterHome.getCountryId();//its travel country
            slieepId = filterHome.getSleepsId();
            bedroomId = filterHome.getBedroomsId();
            genderId = filterHome.getGenderId();
            religionId = filterHome.getReligionId();
            travleId = filterHome.getTravleId();
        } else {
            //get posted home details
            ArrayList<MyhomeArray> myHomeList = switchDBHelper.getAllMyhomedata();
            for (MyhomeArray myhomeArray : myHomeList) {
                startDate = myhomeArray.getStartdate();
                endDate = myhomeArray.getEnddate();
                countryId = myhomeArray.getTravel_country();
                cityId = myhomeArray.getTravel_city();
            }
        }
        ArrayList<Data> userData = switchDBHelper.getAllUserInfoList();
        if (userData != null && userData.size() > 0) {
            for (Data data : userData) {
                userId = data.getId();
            }
            getAllHome();
        }
    }

    private void getAllHome() {
        if (Utility.isOnline(getContext())) {
            final ASTProgressBar dotDialog = new ASTProgressBar(getContext());
            dotDialog.show();
            JSONObject object = new JSONObject();
            try {
                object.put("api_key", Contants.API_KEY);
                object.put("user_id", userId);
                object.put("startdate", startDate);
                object.put("enddate", endDate);
                object.put("country_id", countryId);
            } catch (JSONException e) {
                e.printStackTrace();
            }

            String serviceURL = Contants.BASE_URL + Contants.Getallhomes;

            ServiceCaller serviceCaller = new ServiceCaller(getContext());
            serviceCaller.CallCommanServiceMethod(serviceURL, object, "getAllHome", new IAsyncWorkCompletedCallback() {
                @Override
                public void onDone(String result, boolean isComplete) {
                    if (isComplete) {
                        parseAllHomeServiceData(result);
                    } else {
                        Utility.alertForErrorMessage(Contants.Error, getContext());
                    }
                    if (dotDialog.isShowing()) {
                        dotDialog.dismiss();
                    }
                }
            });
        } else {
            Utility.alertForErrorMessage(Contants.OFFLINE_MESSAGE, getContext());//off line msg....
        }
    }

    public void parseAllHomeServiceData(String result) {
        if (result != null) {
            final ServiceContentData serviceData = new Gson().fromJson(result, ServiceContentData.class);
            if (serviceData != null) {
                if (serviceData.isSuccess()) {
                    if (serviceData.getData() != null) {
                        new AsyncTask<Void, Void, Boolean>() {
                            @Override
                            protected Boolean doInBackground(Void... voids) {
                                Boolean flag = false;
                                Home_data[] home_data = serviceData.getData().getHome_data();
                                Home_liked_disliked[] home_liked_disliked = serviceData.getData().getHome_liked_disliked();
                                if (home_data != null) {
                                    homeList.clear();
                                    //  matchhomeList.clear();
                                    for (Home_data homeData : home_data) {
                                        if (!checkedHomeLikeOrNot(home_liked_disliked, homeData.getId())) {
                                            homeList.add(homeData);
                                        }
                                        //homeList.add(homeData);
                                    }
                                }
                                flag = true;
                                return flag;
                            }

                            @Override
                            protected void onPostExecute(Boolean flag) {
                                super.onPostExecute(flag);
                                if (flag) {
                                    // cardAdapter.notifyDataSetChanged();
                                    // initializeDeck();
                                    getCityWiseHome();
                                }
                            }
                        }.execute();
                    }
                } else {
                    Utility.alertForErrorMessage(serviceData.getMsg(), getContext());
                }
            }
        }
    }

    //remove home from list aftre like dislike
    private void removeMasterListData(int pos) {
        String homeId = matchhomeList.get(pos).getId();
        for (int i = 0; i < homeList.size(); i++) {
            if (homeList.get(i).getId().equals(homeId)) {
                homeList.remove(i);
            }
        }
        matchhomeList.remove(pos);
        boolean homeFlag = true;
        if (homeList != null && homeList.size() > 0) {
            for (Home_data homeData : homeList) {
                if (homeData.getCity_id().equals(cityId)) {
                    homeFlag = false;
                }
            }
            if (matchhomeList.size() == 0) {
                if (homeFlag) {
                    alertForNoHomeAvailable();
                }
            }
        }

    }

    //check home like dislike or not
    private boolean checkedHomeLikeOrNot(Home_liked_disliked[] home_liked_disliked, String homeId) {
        boolean selectFlag = false;
        if (home_liked_disliked != null && home_liked_disliked.length > 0) {
            for (Home_liked_disliked likeHome : home_liked_disliked) {
                if (likeHome.getHome_id().equals(homeId)) {
                    selectFlag = true;
                    break;
                }
            }
        }
        return selectFlag;
    }

    //show only same city home
    private void getCityWiseHome() {

        if (homeList != null && homeList.size() > 0) {
            for (Home_data homeData : homeList) {
                if (homeData.getCity_id().equals(cityId)) {
                    matchhomeList.add(homeData);
                }
            }
            if (matchhomeList.size() == 0 && homeList.size() > 0) {//check if not match available and other city home available then show popup
                alertForNoHomeAvailable();
            } else {
                setAdapterValue(matchhomeList);
                //cardAdapter.notifyDataSetChanged();
            }
        }
    }

    //no home available alert
    public void alertForNoHomeAvailable() {
        final AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        Typeface roboto_regular = Typeface.createFromAsset(getContext().getAssets(), "fonts/roboto.regular.ttf");
        final AlertDialog alert = builder.create();
        View alertview = alert.getLayoutInflater().inflate(R.layout.no_home_available, null);
        TextView title = alertview.findViewById(R.id.title);
        title.setTypeface(roboto_regular);
        Button ok = alertview.findViewById(R.id.ok);
        ok.setTypeface(roboto_regular);
        alert.setCustomTitle(alertview);
        ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                matchhomeList.addAll(homeList);
                if (matchhomeList != null && matchhomeList.size() > 0) {
                    //   ((KoldaMain) view.findViewById(R.id.koloda))._$_clearFindViewByIdCache();
                    //((KoldaMain) view.findViewById(R.id.koloda)).removeAllViews();
                    setAdapterValue(matchhomeList);
                    //cardAdapter.notifyDataSetChanged();
                }
                alert.dismiss();
            }
        });
        alert.show();
    }

    @Override
    public void onResume() {
        super.onResume();
        //cardAdapter.notifyDataSetChanged();
        // initView();
    }

    private void getLikeDislikCard(final String homeId, final String likeUserId, final String status, int pos) {
        if (Utility.isOnline(getContext())) {
            likeDialog = new ASTProgressBar(getContext());
            likeDialog.show();
            JSONObject object = new JSONObject();
            try {
                object.put("api_key", Contants.API_KEY);
                object.put("user_id", likeUserId);
                object.put("home_id", homeId);
                object.put("sender_id", userId);
                object.put("tender_status", status);
            } catch (JSONException e) {
                e.printStackTrace();
            }

            String serviceURL = Contants.BASE_URL + Contants.Likedislike;

            ServiceCaller serviceCaller = new ServiceCaller(getContext());
            serviceCaller.CallCommanServiceMethod(serviceURL, object, "getLikeDislikCard", new IAsyncWorkCompletedCallback() {
                @Override
                public void onDone(String result, boolean isComplete) {
                    if (isComplete) {
                        rewindValue(homeId, likeUserId);
                        parseLikeDislikeServiceData(result, status, pos);
                    } else {
                        if (likeDialog.isShowing()) {
                            likeDialog.dismiss();
                        }
                        Utility.alertForErrorMessage(Contants.Error, getContext());
                    }
                }
            });
        } else {
            Utility.alertForErrorMessage(Contants.OFFLINE_MESSAGE, getContext());//off line msg....
        }
    }

    public void parseLikeDislikeServiceData(String result, String status, int pos) {
        if (result != null) {
            final ServiceContentData serviceData = new Gson().fromJson(result, ServiceContentData.class);
            if (serviceData != null) {
                if (serviceData.isSuccess()) {
                    if (status.equals("3")) {//call after rewind
                        // getAllHome();
                        Intent intent = new Intent(getActivity(), DashboardActivity.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(intent);
                    } else {
                        removeMasterListData(pos);
                        if (homeList.size() == 0) {//call when no home available
                            // getAllHome();
                            Intent intent = new Intent(getActivity(), DashboardActivity.class);
                            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                            startActivity(intent);
                        }
                    }
                }
            } else {
                Utility.alertForErrorMessage(Contants.Error, getContext());
            }
        }
        if (likeDialog.isShowing()) {
            likeDialog.dismiss();
        }
    }

    //store for rewind
    public void rewindValue(String likehomeId, String likedUserId) {
        try {
            SharedPreferences prefs = context.getSharedPreferences("RewindPreferences", Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = prefs.edit();
            editor.putString("LikehomeId", likehomeId);
            editor.putString("LikedUserId", likedUserId);
            editor.commit();
        } catch (Exception e) {
            // should never happen
        }
    }

}
