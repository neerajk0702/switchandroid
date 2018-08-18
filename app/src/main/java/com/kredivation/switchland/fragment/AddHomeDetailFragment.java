package com.kredivation.switchland.fragment;


import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.kredivation.switchland.R;
import com.kredivation.switchland.adapters.AddHomePhotoAdapter;
import com.kredivation.switchland.adapters.FeaturesAdapter;
import com.kredivation.switchland.adapters.RulesAdapter;
import com.kredivation.switchland.database.SwitchDBHelper;
import com.kredivation.switchland.model.Bathrooms;
import com.kredivation.switchland.model.Bedrooms;
import com.kredivation.switchland.model.ChatData;
import com.kredivation.switchland.model.Data;
import com.kredivation.switchland.model.Family;
import com.kredivation.switchland.model.Features;
import com.kredivation.switchland.model.Genderarray;
import com.kredivation.switchland.model.Home_style;
import com.kredivation.switchland.model.House_rules;
import com.kredivation.switchland.model.Pets_allowed;
import com.kredivation.switchland.model.Religion;
import com.kredivation.switchland.model.Security;
import com.kredivation.switchland.model.ServiceContentData;
import com.kredivation.switchland.model.Sleeps;
import com.kredivation.switchland.model.Type_of_property;
import com.kredivation.switchland.utilities.ASTProgressBar;
import com.kredivation.switchland.utilities.FontManager;
import com.kredivation.switchland.utilities.Utility;

import java.security.PrivateKey;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link AddHomeDetailFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class AddHomeDetailFragment extends Fragment implements View.OnClickListener {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;


    public AddHomeDetailFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment AddHomeDetailFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static AddHomeDetailFragment newInstance(String param1, String param2) {
        AddHomeDetailFragment fragment = new AddHomeDetailFragment();
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

    private View view;
    private Context context;
    private TextInputLayout input_layout_title, input_layout_des;
    private EditText title, about;
    private String titleStr, aboutStr;
    List<Features> Featurelist;
    List<House_rules> ruleList;
    List<House_rules> saveRuleList;
    List<Features> saveFeatureList;

    @Override

    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        context = getActivity();
        view = inflater.inflate(R.layout.fragment_add_home_detail, container, false);
        init();
        return view;
    }

    private void init() {

        Typeface materialdesignicons_font = FontManager.getFontTypefaceMaterialDesignIcons(getActivity(), "fonts/materialdesignicons-webfont.otf");
        TextView nextIcon = (TextView) view.findViewById(R.id.nextIcon);
        nextIcon.setTypeface(materialdesignicons_font);
        nextIcon.setText(Html.fromHtml("&#xf142;"));
        TextView previous = (TextView) view.findViewById(R.id.previous);
        previous.setTypeface(materialdesignicons_font);
        previous.setText(Html.fromHtml("&#xf141;"));
        previous.setOnClickListener(this);
        LinearLayout nextLayout = (LinearLayout) view.findViewById(R.id.nextLayout);
        nextLayout.setOnClickListener(this);

        input_layout_title = view.findViewById(R.id.input_layout_title);
        title = view.findViewById(R.id.title);
        input_layout_des = view.findViewById(R.id.input_layout_des);
        about = view.findViewById(R.id.about);

        getSaveData();
        getAllDataFromDB();
    }

    private void getAllDataFromDB() {
        final ASTProgressBar dotDialog = new ASTProgressBar(getContext());
        dotDialog.show();
        new AsyncTask<Void, Void, Boolean>() {
            @Override
            protected Boolean doInBackground(Void... voids) {
                Boolean flag = false;
                SwitchDBHelper switchDBHelper = new SwitchDBHelper(context);
                ServiceContentData sData = switchDBHelper.getMasterData();
                if (sData != null) {
                    Featurelist = new ArrayList<>();
                    ruleList = new ArrayList<>();
                    if (sData.getData() != null) {
                        Data MData = sData.getData();
                        Features[] features = MData.getFeatures();
                        House_rules[] house_rules = MData.getHouse_rules();
                        if (features != null) {
                            for (Features fer : features) {
                                if (checkedFeatureSelectedOrNot(fer)) {
                                    fer.setSelected(true);
                                }
                                Featurelist.add(fer);
                            }
                        }
                        if (house_rules != null) {
                            for (House_rules rul : house_rules) {
                                if (checkedRuleSelectedOrNot(rul)) {
                                    rul.setSelected(true);
                                }
                                ruleList.add(rul);
                            }
                        }
                    }
                }
                flag = true;
                return flag;
            }

            @Override
            protected void onPostExecute(Boolean flag) {
                super.onPostExecute(flag);
                setFeaturesAndRules();
                if (dotDialog.isShowing()) {
                    dotDialog.dismiss();
                }
            }
        }.execute();
    }

    //check feature selected or not
    private boolean checkedFeatureSelectedOrNot(Features fer) {
        boolean selectFlag = false;
        if (saveFeatureList != null && saveFeatureList.size() > 0) {
            for (Features savefe : saveFeatureList) {
                if (savefe.getId().equals(fer.getId())) {
                    if (savefe.isSelected()) {
                        selectFlag = true;
                        break;
                    }
                }
            }
        }
        return selectFlag;
    }

    private boolean checkedRuleSelectedOrNot(House_rules rul) {
        boolean selectFlag = false;
        if (saveRuleList != null && saveRuleList.size() > 0) {
            for (House_rules savefe : saveRuleList) {
                if (savefe.getId().equals(rul.getId())) {
                    if (savefe.isSelected()) {
                        selectFlag = true;
                        break;
                    }
                }
            }
        }
        return selectFlag;
    }

    private void setFeaturesAndRules() {
        RecyclerView recyclerView = view.findViewById(R.id.features_recycler_view);
        recyclerView.setHasFixedSize(false);
        recyclerView.setNestedScrollingEnabled(false);
        StaggeredGridLayoutManager gaggeredGridLayoutManager = new StaggeredGridLayoutManager(2, LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(gaggeredGridLayoutManager);
        FeaturesAdapter mAdapter = new FeaturesAdapter(context, Featurelist, saveFeatureList);
        recyclerView.setAdapter(mAdapter);


        RecyclerView rulerecyclerView = view.findViewById(R.id.rules_recycler_view);
        rulerecyclerView.setHasFixedSize(false);
        rulerecyclerView.setNestedScrollingEnabled(false);
        StaggeredGridLayoutManager rulegaggeredGridLayoutManager = new StaggeredGridLayoutManager(2, LinearLayoutManager.VERTICAL);
        rulerecyclerView.setLayoutManager(rulegaggeredGridLayoutManager);
        RulesAdapter ruleAdapter = new RulesAdapter(context, ruleList, saveRuleList);
        rulerecyclerView.setAdapter(ruleAdapter);

    }

    //get data from pre
    private void getSaveData() {
        SharedPreferences prefs = context.getSharedPreferences("AddHomePreferences", Context.MODE_PRIVATE);
        if (prefs != null) {
            String titleStr = prefs.getString("Title", "");
            String aboutHomeStr = prefs.getString("AboutHome", "");
            title.setText(titleStr);
            about.setText(aboutHomeStr);
            String RuleStr = prefs.getString("RuleStr", "");
            String FeatureStr = prefs.getString("FeatureStr", "");
            if (RuleStr != null && !RuleStr.equals("")) {
                saveRuleList = new Gson().fromJson(RuleStr, new TypeToken<List<House_rules>>() {
                }.getType());
            }
            if (FeatureStr != null && !FeatureStr.equals("")) {
                saveFeatureList = new Gson().fromJson(FeatureStr, new TypeToken<List<Features>>() {
                }.getType());
            }
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.nextLayout:
                if (isValidate()) {
                    saveScreenData(true, false);
                }
                break;
            case R.id.previous:
                saveScreenData(false, false);
                break;
        }
    }

    // ----validation -----
    private boolean isValidate() {
        String emailRegex = "[A-Z0-9a-z._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,4}";
        titleStr = title.getText().toString();
        aboutStr = about.getText().toString();

        if (titleStr.length() == 0) {
            input_layout_title.setError("Please Enter Title");
            requestFocus(title);
            return false;
        } else if (aboutStr.length() == 0) {
            input_layout_des.setError("Please Enter about your Home");
            requestFocus(about);
            return false;
        } else if (Featurelist.size() == 0) {
            Utility.showToast(context, "Please Select Features");
            return false;
        } else if (ruleList.size() == 0) {
            Utility.showToast(context, "Please Select House Rules");
            return false;
        } else {
            input_layout_title.setErrorEnabled(false);
            input_layout_des.setErrorEnabled(false);
        }
        return true;
    }

    private void requestFocus(View view) {
        if (view.requestFocus()) {
            getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
        }
    }

    private void saveScreenData(boolean NextPreviousFlag, boolean DoneFlag) {
        saveData();
        Intent intent = new Intent("ViewPageChange");
        intent.putExtra("NextPreviousFlag", NextPreviousFlag);
        intent.putExtra("DoneFlag", DoneFlag);
        getActivity().sendBroadcast(intent);
    }

    //save data
    private void saveData() {
        String featureIdList = new Gson().toJson(Featurelist);
        String ruleIdList = new Gson().toJson(ruleList);
        SharedPreferences prefs = context.getSharedPreferences("AddHomePreferences", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString("RuleStr", ruleIdList);
        editor.putString("FeatureStr", featureIdList);
        editor.putString("Title", titleStr);
        editor.putString("AboutHome", aboutStr);
        editor.commit();
    }
}
