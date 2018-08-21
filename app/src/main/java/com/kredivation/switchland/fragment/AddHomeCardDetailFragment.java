package com.kredivation.switchland.fragment;


import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.Fragment;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.kredivation.switchland.R;
import com.kredivation.switchland.activity.AppTourActivity;
import com.kredivation.switchland.activity.SplashScreenActivity;
import com.kredivation.switchland.activity.TravelRoutineActivity;
import com.kredivation.switchland.database.SwitchDBHelper;
import com.kredivation.switchland.model.Bathrooms;
import com.kredivation.switchland.model.Bedrooms;
import com.kredivation.switchland.model.Data;
import com.kredivation.switchland.model.Family;
import com.kredivation.switchland.model.Genderarray;
import com.kredivation.switchland.model.HomeDetails;
import com.kredivation.switchland.model.Home_style;
import com.kredivation.switchland.model.Month;
import com.kredivation.switchland.model.MyhomeArray;
import com.kredivation.switchland.model.Pets_allowed;
import com.kredivation.switchland.model.Religion;
import com.kredivation.switchland.model.Security;
import com.kredivation.switchland.model.ServiceContentData;
import com.kredivation.switchland.model.Sleeps;
import com.kredivation.switchland.model.Type_of_property;
import com.kredivation.switchland.model.Year;
import com.kredivation.switchland.utilities.ASTProgressBar;
import com.kredivation.switchland.utilities.FontManager;
import com.kredivation.switchland.utilities.Utility;
import com.squareup.picasso.Picasso;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link AddHomeCardDetailFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class AddHomeCardDetailFragment extends Fragment implements View.OnClickListener {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;


    public AddHomeCardDetailFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment AddHomeCardDetailFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static AddHomeCardDetailFragment newInstance(String param1, String param2) {
        AddHomeCardDetailFragment fragment = new AddHomeCardDetailFragment();
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
    private Spinner monthspinner, yearspinner;
    private String[] monthList, yearList;
    Month[] months;
    Year[] years;
    String monthId;
    int monthPos = 0;
    String yearId;
    int yearPos = 0;
    EditText cvv, name, cardno;
    TextInputLayout input_layout_name, input_layout_cardno, input_layout_cvv;
    String cvvStr, nameStr, cardnoStr;
    HomeDetails MyHomedata;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        context = getActivity();
        view = inflater.inflate(R.layout.fragment_add_home_card_detail, container, false);
        init();
        return view;
    }

    private void init() {
        Typeface materialdesignicons_font = FontManager.getFontTypefaceMaterialDesignIcons(getActivity(), "fonts/materialdesignicons-webfont.otf");
        TextView previous = (TextView) view.findViewById(R.id.previous);
        previous.setTypeface(materialdesignicons_font);
        previous.setText(Html.fromHtml("&#xf141;"));
        previous.setOnClickListener(this);
        TextView submit = view.findViewById(R.id.submit);
        submit.setOnClickListener(this);
        monthspinner = view.findViewById(R.id.monthspinner);
        yearspinner = view.findViewById(R.id.yearspinner);
        cvv = view.findViewById(R.id.cvv);
        name = view.findViewById(R.id.name);
        cardno = view.findViewById(R.id.cardno);
        input_layout_name = view.findViewById(R.id.input_layout_name);
        input_layout_cardno = view.findViewById(R.id.input_layout_cardno);
        input_layout_cvv = view.findViewById(R.id.input_layout_cvv);
        getSaveData();
        getAllDataFromDB();
    }

    //get data from pre
    private void getSaveData() {
     /*   SharedPreferences prefs = context.getSharedPreferences("AddHomePreferences", Context.MODE_PRIVATE);
        if (prefs != null) {
            monthId = prefs.getString("monthId", "");
            monthPos = prefs.getInt("monthPos", 0);
            yearId = prefs.getString("yearId", "");
            yearPos = prefs.getInt("yearPos", 0);
            cvvStr = prefs.getString("CVV", "");
            nameStr = prefs.getString("Name", "");
            cardnoStr = prefs.getString("Cardno", "");
            cvv.setText(cvvStr);
            name.setText(nameStr);
            cardno.setText(cardnoStr);
        }*/

        SharedPreferences prefs = context.getSharedPreferences("HomeDetailPreferences", Context.MODE_PRIVATE);
        if (prefs != null) {
            if (prefs.getBoolean("HomeEdit", false)) {
                String Myhome = prefs.getString("HomeDetail", "");
                if (Myhome != null && !Myhome.equals("")) {
                    MyHomedata = new Gson().fromJson(Myhome, new TypeToken<HomeDetails>() {
                    }.getType());

                   /* if (MyHomedata != null) {//for home edit
                        String titleStr = MyHomedata.getTitle();
                        String aboutHomeStr = MyHomedata.getSort_description();
                        saveRuleList = MyHomedata.getRuleList();
                        saveFeatureList = MyHomedata.getFeaturelist();
                        title.setText(titleStr);
                        about.setText(aboutHomeStr);
                    }*/
                }
            }
        }
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
                    if (sData.getData() != null) {
                        Data MData = sData.getData();
                        months = MData.getMonth();
                        years = MData.getYear();
                        if (months != null) {
                            monthList = new String[months.length];
                            for (int i = 0; i < months.length; i++) {
                                monthList[i] = months[i].getName();
                            }
                        }
                        if (years != null) {
                            yearList = new String[years.length];
                            for (int i = 0; i < years.length; i++) {
                                yearList[i] = years[i].getName();
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
                setSpinerValue();
                if (dotDialog.isShowing()) {
                    dotDialog.dismiss();
                }
            }
        }.execute();
    }

    private void setSpinerValue() {
        ArrayAdapter<String> countryAdapter = new ArrayAdapter<String>(context, R.layout.spinner_row, monthList);
        monthspinner.setAdapter(countryAdapter);
        monthspinner.setSelection(monthPos);
        monthspinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                monthId = months[position].getId();
                monthPos = position;
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        ArrayAdapter<String> yearAdapter = new ArrayAdapter<String>(context, R.layout.spinner_row, yearList);
        yearspinner.setAdapter(yearAdapter);
        yearspinner.setSelection(yearPos);
        yearspinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                yearPos = position;
                yearId = years[position].getId();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.submit:
                if (isValidate()) {
                    saveData();
                    Intent intent = new Intent(context, TravelRoutineActivity.class);
                    startActivity(intent);
                }
                break;
            case R.id.previous:
                saveScreenData(false, false);
                break;
        }
    }

    // ----validation -----
    private boolean isValidate() {
        cvvStr = cvv.getText().toString();
        nameStr = name.getText().toString();
        cardnoStr = cardno.getText().toString();
        if (nameStr.length() == 0) {
            input_layout_name.setError("Please Enter Card Holder Name");
            requestFocus(name);
            return false;
        } else if (cardnoStr.length() == 0) {
            input_layout_cardno.setError("Please Enter Card Number");
            requestFocus(cardno);
            return false;
        } else if (cvvStr.length() == 0) {
            input_layout_cvv.setError("Please Enter CVV!");
            requestFocus(cvv);
            return false;
        } else if (yearId == null || yearId.equals("") || yearId.equals("0")) {
            Utility.showToast(context, "Please Select Year!");
            return false;
        } else if (monthId == null || monthId.equals("") || monthId.equals("0")) {
            Utility.showToast(context, "Please Select Month!");
            return false;
        } else {
            input_layout_name.setErrorEnabled(false);
            input_layout_cardno.setErrorEnabled(false);
            input_layout_cvv.setErrorEnabled(false);
            input_layout_name.setError("");
            input_layout_cardno.setError("");
            input_layout_cvv.setError("");
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
       /* SharedPreferences prefs = context.getSharedPreferences("AddHomePreferences", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString("monthId", monthId);
        editor.putInt("monthPos", monthPos);
        editor.putString("yearId", yearId);
        editor.putInt("yearPos", yearPos);
        editor.putString("CVV", cvvStr);
        editor.putString("Name", nameStr);
        editor.putString("Cardno", cardnoStr);
        editor.commit();*/

        /*if (MyHomedata != null) {
            MyHomedata.setTitle(titleStr);
            MyHomedata.setSort_description(aboutStr);
            MyHomedata.setFeaturelist(Featurelist);
            MyHomedata.setRuleList(ruleList);
            String homeStr = new Gson().toJson(MyHomedata);
            Utility.setHomeDetail(context, homeStr,true);
        }*/
    }
}
