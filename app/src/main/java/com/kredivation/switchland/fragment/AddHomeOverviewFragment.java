package com.kredivation.switchland.fragment;


import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Bundle;
import androidx.fragment.app.Fragment;

import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.kredivation.switchland.R;
import com.kredivation.switchland.database.SwitchDBHelper;
import com.kredivation.switchland.model.Bathrooms;
import com.kredivation.switchland.model.Bedrooms;
import com.kredivation.switchland.model.Data;
import com.kredivation.switchland.model.Family;
import com.kredivation.switchland.model.Genderarray;
import com.kredivation.switchland.model.HomeDetails;
import com.kredivation.switchland.model.Home_style;
import com.kredivation.switchland.model.Pets_allowed;
import com.kredivation.switchland.model.Religion;
import com.kredivation.switchland.model.Security;
import com.kredivation.switchland.model.ServiceContentData;
import com.kredivation.switchland.model.Sleeps;
import com.kredivation.switchland.model.Type_of_property;
import com.kredivation.switchland.utilities.ASTProgressBar;
import com.kredivation.switchland.utilities.FontManager;
import com.kredivation.switchland.utilities.Utility;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link AddHomeOverviewFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class AddHomeOverviewFragment extends Fragment implements View.OnClickListener {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;


    public AddHomeOverviewFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment AddHomeOverviewFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static AddHomeOverviewFragment newInstance(String param1, String param2) {
        AddHomeOverviewFragment fragment = new AddHomeOverviewFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    private View view;
    private Context context;
    private Spinner homeStyleSpinner, Typeofpropertyspinner, PetAllowedspinner, Familymattersspinner, Religionspinner, genderspinner, levelofsecuritySpinner;
    String securitiesList[], homestylefList[], bedroomsList[], bathroomsList[], sleepsList[], typeOfPropertiesList[], petsAllowedList[], familyList[], genderList[], religionList[];
    String homestyleStr, securitStr, genderStr, religionStr, familyStr, petsStr, typeOfPropertiesStr;
    Security[] securities;
    Home_style[] home_stylef;
    Bedrooms[] bedrooms;
    Bathrooms[] bathrooms;
    Sleeps[] sleeps;
    Type_of_property[] type_of_properties;
    Pets_allowed[] pets_allowed;
    Family[] family;
    Genderarray[] genderarray;
    Religion[] religion;
    HomeDetails MyHomedata;
    String homeId;
    String serverhomestyleStr, serversecuritStr, servergenderStr, serverreligionStr, serverfamilyStr, serverpetsStr, servertypeOfPropertiesStr;
    int serversleepsStr = 0, serverbathroomsStr = 0, serverbedroomsStr = 0;

    TextView bedView, viewbath, viewsleeps;
    ImageView bedaddition, bedsubtraction, subtractionbath, additionbath, subtractionsleeps, additionsleeps;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        context = getActivity();
        view = inflater.inflate(R.layout.fragment_add_home_overview, container, false);
        init();
        return view;
    }

    private void init() {
        levelofsecuritySpinner = view.findViewById(R.id.levelofsecuritySpinner);
        Religionspinner = view.findViewById(R.id.Religionspinner);
        genderspinner = view.findViewById(R.id.genderspinner);
        homeStyleSpinner = view.findViewById(R.id.homeStyleSpinner);
        Typeofpropertyspinner = view.findViewById(R.id.Typeofpropertyspinner);
        PetAllowedspinner = view.findViewById(R.id.PetAllowedspinner);
        Familymattersspinner = view.findViewById(R.id.Familymattersspinner);
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


        bedView = view.findViewById(R.id.qun);
        bedaddition = view.findViewById(R.id.addition);
        bedsubtraction = view.findViewById(R.id.subtraction);
        bedaddition.setOnClickListener(this);
        bedsubtraction.setOnClickListener(this);

        viewbath = view.findViewById(R.id.viewbath);
        subtractionbath = view.findViewById(R.id.subtractionbath);
        additionbath = view.findViewById(R.id.additionbath);
        subtractionbath.setOnClickListener(this);
        additionbath.setOnClickListener(this);

        viewsleeps = view.findViewById(R.id.viewsleeps);
        subtractionsleeps = view.findViewById(R.id.subtractionsleeps);
        additionsleeps = view.findViewById(R.id.additionsleeps);
        additionsleeps.setOnClickListener(this);
        subtractionsleeps.setOnClickListener(this);

        getAllDataFromDB();

    }

    private void setSpinerValue() {
        ArrayAdapter<String> homeadapter = new ArrayAdapter<String>(context, R.layout.spinner_row, homestylefList);
        homeStyleSpinner.setAdapter(homeadapter);
        ArrayAdapter<String> bedroomadapter = new ArrayAdapter<String>(context, R.layout.spinner_row, bedroomsList);
        ArrayAdapter<String> typedapter = new ArrayAdapter<String>(context, R.layout.spinner_row, typeOfPropertiesList);
        Typeofpropertyspinner.setAdapter(typedapter);
        ArrayAdapter<String> petadapter = new ArrayAdapter<String>(context, R.layout.spinner_row, petsAllowedList);
        PetAllowedspinner.setAdapter(petadapter);
        ArrayAdapter<String> familyadapter = new ArrayAdapter<String>(context, R.layout.spinner_row, familyList);
        Familymattersspinner.setAdapter(familyadapter);
        ArrayAdapter<String> reliadapter = new ArrayAdapter<String>(context, R.layout.spinner_row, religionList);
        Religionspinner.setAdapter(reliadapter);
        ArrayAdapter<String> genderadapter = new ArrayAdapter<String>(context, R.layout.spinner_row, genderList);
        genderspinner.setAdapter(genderadapter);
        ArrayAdapter<String> securityadapter = new ArrayAdapter<String>(context, R.layout.spinner_row, securitiesList);
        levelofsecuritySpinner.setAdapter(securityadapter);

        homeStyleSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                homestyleStr = home_stylef[position].getId();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        Typeofpropertyspinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                typeOfPropertiesStr = type_of_properties[position].getId();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        PetAllowedspinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                petsStr = pets_allowed[position].getId();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        Familymattersspinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                familyStr = family[position].getId();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        Religionspinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                religionStr = religion[position].getId();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        genderspinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                genderStr = genderarray[position].getId();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        levelofsecuritySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                securitStr = securities[position].getId();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        getSaveData();
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
                        securities = MData.getSecurity();
                        home_stylef = MData.getHome_style();
                        bedrooms = MData.getBedrooms();
                        bathrooms = MData.getBathrooms();
                        sleeps = MData.getSleeps();
                        type_of_properties = MData.getType_of_property();
                        pets_allowed = MData.getPets_allowed();
                        family = MData.getFamily();
                        genderarray = MData.getGenderarray();
                        religion = MData.getReligion();
                        if (securities != null) {
                            securitiesList = new String[securities.length];
                            for (int i = 0; i < securities.length; i++) {
                                securitiesList[i] = securities[i].getName();
                            }
                        }
                        if (home_stylef != null) {
                            homestylefList = new String[home_stylef.length];
                            for (int i = 0; i < home_stylef.length; i++) {
                                homestylefList[i] = home_stylef[i].getName();
                            }
                        }
                        if (bedrooms != null) {
                            bedroomsList = new String[bedrooms.length];
                            for (int i = 0; i < bedrooms.length; i++) {
                                bedroomsList[i] = String.valueOf(bedrooms[i].getName());
                            }
                        }
                        if (bathrooms != null) {
                            bathroomsList = new String[bathrooms.length];
                            for (int i = 0; i < bathrooms.length; i++) {
                                bathroomsList[i] = String.valueOf(bathrooms[i].getName());
                            }
                        }
                        if (sleeps != null) {
                            sleepsList = new String[sleeps.length];
                            for (int i = 0; i < sleeps.length; i++) {
                                sleepsList[i] = String.valueOf(sleeps[i].getName());
                            }
                        }
                        if (type_of_properties != null) {
                            typeOfPropertiesList = new String[type_of_properties.length];
                            for (int i = 0; i < type_of_properties.length; i++) {
                                typeOfPropertiesList[i] = type_of_properties[i].getName();
                            }
                        }
                        if (pets_allowed != null) {
                            petsAllowedList = new String[pets_allowed.length];
                            for (int i = 0; i < pets_allowed.length; i++) {
                                petsAllowedList[i] = pets_allowed[i].getName();
                            }
                        }
                        if (family != null) {
                            familyList = new String[family.length];
                            for (int i = 0; i < family.length; i++) {
                                familyList[i] = family[i].getName();
                            }
                        }
                        if (genderarray != null) {
                            genderList = new String[genderarray.length];
                            for (int i = 0; i < genderarray.length; i++) {
                                genderList[i] = genderarray[i].getName();
                            }
                        }
                        if (religion != null) {
                            religionList = new String[religion.length];
                            for (int i = 0; i < religion.length; i++) {
                                religionList[i] = religion[i].getName();
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

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.nextLayout:
                if (serverbedroomsStr == 0) {
                    Utility.showToast(getActivity(), "Please select No Of Beds!");
                } else if (serversleepsStr == 0) {
                    Utility.showToast(getActivity(), "Please select No of Guests!");
                } else if (serverbathroomsStr == 0) {
                    Utility.showToast(getActivity(), "Please select No of Bathrooms!");
                } else {
                    saveScreenData(true, false);
                }
                break;
            case R.id.previous:
                saveScreenData(false, false);
                break;
            case R.id.addition:
                increaseBed();
                break;
            case R.id.subtraction:
                if (serverbedroomsStr > 0) {
                    decreaseBed();
                }
                break;
            case R.id.additionbath:
                increaseBath();
                break;
            case R.id.subtractionbath:
                if (serverbathroomsStr > 0) {
                    decreasBath();
                }
                break;
            case R.id.additionsleeps:
                increaSleep();
                break;
            case R.id.subtractionsleeps:
                if (serversleepsStr > 0) {
                    decreaseSleep();
                }
                break;
        }
    }

    public void increaseBed() {
        serverbedroomsStr = serverbedroomsStr + 1;
        bedView.setText("" + serverbedroomsStr);
    }

    public void decreaseBed() {
        serverbedroomsStr = serverbedroomsStr - 1;
        bedView.setText("" + serverbedroomsStr);
    }

    public void increaseBath() {
        serverbathroomsStr = serverbathroomsStr + 1;
        viewbath.setText("" + serverbathroomsStr);
    }

    public void decreasBath() {
        serverbathroomsStr = serverbathroomsStr - 1;
        viewbath.setText("" + serverbathroomsStr);
    }

    public void increaSleep() {
        serversleepsStr = serversleepsStr + 1;
        viewsleeps.setText("" + serversleepsStr);
    }

    public void decreaseSleep() {
        serversleepsStr = serversleepsStr - 1;
        viewsleeps.setText("" + serversleepsStr);
    }

    private void saveScreenData(boolean NextPreviousFlag, boolean DoneFlag) {
        saveData();
        Intent intent = new Intent("ViewPageChange");
        intent.putExtra("NextPreviousFlag", NextPreviousFlag);
        intent.putExtra("DoneFlag", DoneFlag);
        getActivity().sendBroadcast(intent);
    }

    //save all data
    public void saveData() {
        if (MyHomedata != null) {
            HomeDetails details = new HomeDetails();
            details.setId(homeId);
            details.setHome_type(homestyleStr);
            details.setLevel_security(securitStr);
            details.setGender(genderStr);
            details.setReligion(religionStr);
            details.setFamily_matters(familyStr);
            details.setPets(petsStr);
            details.setProperty_type(typeOfPropertiesStr);
            details.setSleeps(String.valueOf(serversleepsStr));
            details.setBathrooms(String.valueOf(serverbathroomsStr));
            details.setBedrooms(String.valueOf(serverbedroomsStr));
            SwitchDBHelper dbHelper = new SwitchDBHelper(getActivity());
            dbHelper.updateAddEditHomeOverview(details);
        }
    }

    private void getSaveData() {
        SwitchDBHelper dbHelper = new SwitchDBHelper(getActivity());
        ArrayList<HomeDetails> homeDetails = dbHelper.getAllAddEditHomeDataList();
        if (homeDetails != null) {
            for (HomeDetails details : homeDetails) {
                MyHomedata = details;
                if (MyHomedata != null) {//for home edit
                    homeId = MyHomedata.getId();
                    serverhomestyleStr = MyHomedata.getHomestyle();
                    serversecuritStr = MyHomedata.getLevel_security();
                    servergenderStr = MyHomedata.getGender();
                    serverreligionStr = MyHomedata.getReligion();
                    serverfamilyStr = MyHomedata.getFamily_matters();
                    serverpetsStr = MyHomedata.getPets();
                    servertypeOfPropertiesStr = MyHomedata.getProperty_type();
                    serversleepsStr = Integer.parseInt(MyHomedata.getSleeps());
                    serverbathroomsStr = Integer.parseInt(MyHomedata.getBathrooms());
                    serverbedroomsStr = Integer.parseInt(MyHomedata.getBedrooms());
                    setDefaultValue();
                }
            }
        }
    }

    //set if value exist
    private void setDefaultValue() {
        for (int i = 0; i < homestylefList.length; i++) {
            if (serverhomestyleStr != null && serverhomestyleStr.equals(home_stylef[i].getId())) {
                homeStyleSpinner.setSelection(i);
                break;
            }
        }
        for (int i = 0; i < securitiesList.length; i++) {
            levelofsecuritySpinner.setSelection(i);
        }
        for (int i = 0; i < genderList.length; i++) {
            if (servergenderStr != null && servergenderStr.equals(genderarray[i].getName())) {
                genderspinner.setSelection(i);
                break;
            }
        }
        for (int i = 0; i < religionList.length; i++) {
            if (serverreligionStr != null && serverreligionStr.equals(religion[i].getName())) {
                Religionspinner.setSelection(i);
                break;
            }
        }
        for (int i = 0; i < familyList.length; i++) {
            if (serverfamilyStr != null && serverfamilyStr.equals(family[i].getId())) {
                Familymattersspinner.setSelection(i);
                break;
            }
        }
        for (int i = 0; i < petsAllowedList.length; i++) {
            if (serverpetsStr != null && serverpetsStr.equals(pets_allowed[i].getId())) {
                PetAllowedspinner.setSelection(i);
                break;
            }
        }
        for (int i = 0; i < typeOfPropertiesList.length; i++) {
            if (servertypeOfPropertiesStr != null && servertypeOfPropertiesStr.equals(type_of_properties[i].getId())) {
                Typeofpropertyspinner.setSelection(i);
                break;
            }
        }
        for (int i = 0; i < sleepsList.length; i++) {
            if (serversleepsStr == sleeps[i].getId()) {
                viewsleeps.setText(sleeps[i].getId() + "");
                break;
            }
        }
        for (int i = 0; i < bathroomsList.length; i++) {
            if (serverbathroomsStr == bathrooms[i].getId()) {
                viewbath.setText(bathrooms[i].getId() + "");
                break;
            }
        }
        for (int i = 0; i < bedroomsList.length; i++) {
            if (serverbedroomsStr == bedrooms[i].getId()) {
                bedView.setText(bedrooms[i].getId() + "");
                break;
            }
        }
    }
}
