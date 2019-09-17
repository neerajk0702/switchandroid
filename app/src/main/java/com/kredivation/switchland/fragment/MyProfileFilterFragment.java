package com.kredivation.switchland.fragment;


import android.app.DatePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import androidx.fragment.app.Fragment;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.google.gson.Gson;
import com.kredivation.switchland.R;
import com.kredivation.switchland.activity.DashboardActivity;
import com.kredivation.switchland.activity.TravelRoutineActivity;
import com.kredivation.switchland.database.SwitchDBHelper;
import com.kredivation.switchland.model.Bedrooms;
import com.kredivation.switchland.model.City;
import com.kredivation.switchland.model.Country;
import com.kredivation.switchland.model.Data;
import com.kredivation.switchland.model.FilterHome;
import com.kredivation.switchland.model.Genderarray;
import com.kredivation.switchland.model.MyhomeArray;
import com.kredivation.switchland.model.Religion;
import com.kredivation.switchland.model.ServiceContentData;
import com.kredivation.switchland.model.Sleeps;
import com.kredivation.switchland.model.Type_of_traveller;
import com.kredivation.switchland.utilities.FontManager;
import com.kredivation.switchland.utilities.Utility;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link MyProfileFilterFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class MyProfileFilterFragment extends Fragment implements View.OnClickListener {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;


    public MyProfileFilterFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment MyProfileFilterFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static MyProfileFilterFragment newInstance(String param1, String param2) {
        MyProfileFilterFragment fragment = new MyProfileFilterFragment();
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

    private String userId;
    private City[] city;
    private Country[] country;
    Spinner cityspinner, countryspinner, Religionspinner, Typeoftravellerspinner, genderspinner;
    String[] countryList;
    ArrayList<String> cityList;
    ArrayList<String> cityIdList;
    private String cityId = "";
    private String countryId = "";
    // ImageView profileImage;
    //  TextView uNeme, uemail, phone, cityName, countryName ,travlingDtae;
    String startDate = "";
    String endDate = "";
    //String countryNameStr = "";
    //String cityNameStr = "";
    Data MData;
    LinearLayout dateLayout, endDateLayout;
    TextView edateIcon;
    TextView etYear, enddate, showAllfilter;
    LinearLayout moreFilterLayout;
    private String startDateStr, enddateStr, servercountryId, servercityId;
    Bedrooms[] bedrooms;
    Sleeps[] sleeps;
    Genderarray[] genderarray;
    Religion[] religion;
    Type_of_traveller[] travel;
    String travleIdStr = "";
    String[] travleList;
    String bedroomsList[], sleepsList[], genderList[], religionList[];
    String genderStr, religionStr;
    String sleepsStr, bedroomsStr, travleTypeId;
    Button submit;
    boolean cityselect = false;
    View view;
    Context context;
    TextView bedView, guestview;
    int bedminteger = 0;
    int guestminteger = 0;
    ImageView bedaddition, bedsubtraction, guestaddition, guestsubtraction;
    String travelCountryName, travelCityName;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        context = getActivity();
        view = inflater.inflate(R.layout.fragment_my_profile_filter, container, false);
        initView();
        return view;
    }

    private void initView() {
        Typeface materialdesignicons_font = FontManager.getFontTypefaceMaterialDesignIcons(context, "fonts/materialdesignicons-webfont.otf");
  /*      TextView locationIcon = (TextView) view.findViewById(R.id.locationIcon);
        locationIcon.setTypeface(materialdesignicons_font);
        locationIcon.setText(Html.fromHtml("&#xf34e;"));
        TextView calIcon = (TextView) view.findViewById(R.id.calIcon);
        calIcon.setTypeface(materialdesignicons_font);
        calIcon.setText(Html.fromHtml("&#xf0ed;"));*/
        TextView starticon = (TextView) view.findViewById(R.id.starticon);
        starticon.setTypeface(materialdesignicons_font);
        starticon.setText(Html.fromHtml("&#xf0ed;"));
        TextView endIcon = (TextView) view.findViewById(R.id.endIcon);
        endIcon.setTypeface(materialdesignicons_font);
        endIcon.setText(Html.fromHtml("&#xf0ed;"));
        bedView = view.findViewById(R.id.qun);
        guestview = view.findViewById(R.id.guestview);
        bedaddition = view.findViewById(R.id.addition);
        bedsubtraction = view.findViewById(R.id.subtraction);
        bedaddition.setOnClickListener(this);
        bedsubtraction.setOnClickListener(this);
        guestaddition = view.findViewById(R.id.addition1);
        guestsubtraction = view.findViewById(R.id.subtraction1);
        guestaddition.setOnClickListener(this);
        guestsubtraction.setOnClickListener(this);

       /*
         profileImage = view.findViewById(R.id.profileImage);
       uNeme = view.findViewById(R.id.uNeme);
        uemail = view.findViewById(R.id.uemail);
        phone = view.findViewById(R.id.phone);
        cityName = view.findViewById(R.id.city);
       countryName = view.findViewById(R.id.country);*/
        dateLayout = view.findViewById(R.id.startDateLayout);
        dateLayout.setOnClickListener(this);
        endDateLayout = view.findViewById(R.id.endDateLayout);
        endDateLayout.setOnClickListener(this);
        etYear = view.findViewById(R.id.startDate);
        enddate = view.findViewById(R.id.endDate);
        //bedroomspinner = view.findViewById(R.id.bedroomspinner);
        Religionspinner = view.findViewById(R.id.Religionspinner);
        Typeoftravellerspinner = view.findViewById(R.id.Typeoftravellerspinner);
        genderspinner = view.findViewById(R.id.genderspinner);
        //  Sleepspinner = view.findViewById(R.id.Sleepspinner);
        submit = view.findViewById(R.id.submit);
        submit.setOnClickListener(this);

        showAllfilter = view.findViewById(R.id.showAllfilter);
        showAllfilter.setOnClickListener(this);
        moreFilterLayout = view.findViewById(R.id.moreFilterLayout);
       /* new
        Button travelRoutine = view.findViewById(R.id.travelRoutine);
        travelRoutine.setOnClickListener(this);*/
        /*SeekBar seekBar = (SeekBar)findViewById(R.id.seekBar2);

        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener(){

            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                Toast.makeText(context,
                        "Seekbar vale "+i, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
                Toast.makeText(context,
                        "Seekbar touch started", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                Toast.makeText(context,
                        "Seekbar touch stopped", Toast.LENGTH_SHORT).show();
            }
        });
*/
        cityspinner = (Spinner) view.findViewById(R.id.cityspinner);
        countryspinner = (Spinner) view.findViewById(R.id.countryspinner);
        getAllDataFromDB();
    }


    private void setSpinerValue() {
        ArrayAdapter<String> bedroomadapter = new ArrayAdapter<String>(context, R.layout.spinner_row, bedroomsList);
        // bedroomspinner.setAdapter(bedroomadapter);
        ArrayAdapter<String> sleepadapter = new ArrayAdapter<String>(context, R.layout.spinner_row, sleepsList);
        // Sleepspinner.setAdapter(sleepadapter);
        ArrayAdapter<String> reliadapter = new ArrayAdapter<String>(context, R.layout.spinner_row, religionList);
        Religionspinner.setAdapter(reliadapter);
        ArrayAdapter<String> genderadapter = new ArrayAdapter<String>(context, R.layout.spinner_row, genderList);
        genderspinner.setAdapter(genderadapter);
        ArrayAdapter<String> typeoftravellerspinneradapter = new ArrayAdapter<String>(context, R.layout.spinner_row, travleList);
        Typeoftravellerspinner.setAdapter(typeoftravellerspinneradapter);
        Typeoftravellerspinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                travleIdStr = travel[position].getId();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
       /* bedroomspinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                bedroomsStr = String.valueOf(bedrooms[position].getId());
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        Sleepspinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                sleepsStr = String.valueOf(sleeps[position].getId());
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
*/
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

        getMyHomeDetail();
    }

    private void getAllDataFromDB() {
        SwitchDBHelper switchDBHelper = new SwitchDBHelper(context);
        ServiceContentData sData = switchDBHelper.getMasterData();
        if (sData != null) {
            if (sData.getData() != null) {
                MData = sData.getData();
                bedrooms = MData.getBedrooms();
                sleeps = MData.getSleeps();
                genderarray = MData.getGenderarray();
                religion = MData.getReligion();
                travel = MData.getType_of_traveller();
                travleList = new String[travel.length];
                if (travel != null) {
                    for (int i = 0; i < travel.length; i++) {
                        travleList[i] = travel[i].getName();
                    }
                }
                if (bedrooms != null) {
                    bedroomsList = new String[bedrooms.length];
                    for (int i = 0; i < bedrooms.length; i++) {
                        bedroomsList[i] = String.valueOf(bedrooms[i].getName());
                    }
                }
                if (sleeps != null) {
                    sleepsList = new String[sleeps.length];
                    for (int i = 0; i < sleeps.length; i++) {
                        sleepsList[i] = String.valueOf(sleeps[i].getName());
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
                country = MData.getCountry();
                if (country != null) {
                    countryList = new String[country.length];
                    for (int i = 0; i < country.length; i++) {
                        countryList[i] = String.valueOf(country[i].getName());
                    }
                    ArrayAdapter<String> countryAdapter = new ArrayAdapter<String>(context, R.layout.spinner_row, countryList);
                    countryspinner.setAdapter(countryAdapter);
                    countryspinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                        @Override
                        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                            countryId = country[position].getId();
                            travelCountryName = countryList[position].toString();
                            setCityAdapter(countryId);
                        }

                        @Override
                        public void onNothingSelected(AdapterView<?> parent) {

                        }
                    });
                }
            }
        }
        setSpinerValue();
    }

    private void setCityAdapter(String countryId) {
        city = MData.getCity();
        cityList = new ArrayList();
        cityIdList = new ArrayList();
        if (city != null) {
            for (int i = 0; i < city.length; i++) {
                if (countryId.equals(city[i].getCountry_id())) {
                    cityList.add(String.valueOf(city[i].getName()));
                    cityIdList.add(String.valueOf(city[i].getId()));
                }
            }
            if (cityList != null && cityList.size() > 0) {
                ArrayAdapter<String> cityAdapter = new ArrayAdapter<String>(context, R.layout.spinner_row, cityList);
                cityspinner.setAdapter(cityAdapter);
            }
        }
        cityspinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                cityId = city[position].getId();
                travelCityName = cityList.get(position).toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        getCitySelectPos();
    }

    private void getUserdata() {
      /*  SwitchDBHelper switchDBHelper = new SwitchDBHelper(context);
        ArrayList<Data> userData = switchDBHelper.getAllUserInfoList();
        if (userData != null && userData.size() > 0) {
            for (Data data : userData) {
                userId = data.getId();
                if (data.getFull_name() != null && !data.getFull_name().equals("")) {
                    uNeme.setText(data.getFull_name());
                } else {
                    uNeme.setText(data.getFirst_name() + " " + data.getLast_name());
                }
                uemail.setText(data.getEmail());
                phone.setText(data.getMobile_number());
                Picasso.with(context).load(data.getProfile_image()).placeholder(R.drawable.userimage).resize(100, 100).into(profileImage);
            }
        }*/
    }

    @Override
    public void onResume() {
        super.onResume();
        getUserdata();
    }

    //get post home details
    private void getMyHomeDetail() {
        SwitchDBHelper switchDBHelper = new SwitchDBHelper(context);

        ArrayList<MyhomeArray> myHomeList = switchDBHelper.getAllMyhomedata();
        for (MyhomeArray myhomeArray : myHomeList) {
            startDate = myhomeArray.getStartdate();
            endDate = myhomeArray.getEnddate();
            servercountryId = myhomeArray.getTravel_country();
            servercityId = myhomeArray.getTravel_city();
            sleepsStr = myhomeArray.getSleeps();
            bedroomsStr = myhomeArray.getBedrooms();
            travleTypeId = myhomeArray.getProperty_type();
            genderStr = myhomeArray.getGender();
            religionStr = myhomeArray.getReligion();
            //  travlingDtae.setText(startDate);
            etYear.setText(startDate);
            enddate.setText(endDate);
            //  homeTitle.setText(myhomeArray.getTitle());
            //  countryName.setText(myhomeArray.getTravel_country_name());
            //  cityName.setText(myhomeArray.getTravel_city_name());
        }
        //setCityAdapter(servercountryId);
        setDefaultValue();
    }

    //set if value exist
    private void setDefaultValue() {
        for (int i = 0; i < genderList.length; i++) {
            if (genderStr.equals(genderarray[i].getId())) {
                genderspinner.setSelection(i);
                break;
            }
        }
        for (int i = 0; i < religionList.length; i++) {
            if (religionStr.equals(religion[i].getId())) {
                Religionspinner.setSelection(i);
                break;
            }
        }

      /*  for (int i = 0; i < sleepsList.length; i++) {
            if (sleepsStr.equals(sleeps[i].getId())) {
                Sleepspinner.setSelection(i);
                break;
            }
        }
        for (int i = 0; i < bedroomsList.length; i++) {
            if (bedroomsStr.equals(bedrooms[i].getId())) {
                bedroomspinner.setSelection(i);
                break;
            }
        }*/
        for (int i = 0; i < travleList.length; i++) {
            if (travleTypeId.equals(travel[i].getId())) {
                Typeoftravellerspinner.setSelection(i);
                break;
            }
        }
        getCountrySelectPos();
    }

    private void getCountrySelectPos() {
        int pos = 0;
        for (int i = 0; i < country.length; i++) {
            if (servercountryId.equals(country[i].getId())) {
                // countryNameStr = String.valueOf(country[i].getName());
                pos = i;
                break;
            }
        }
        countryspinner.setSelection(pos);

    }

    private void getCitySelectPos() {
        int pos = 0;
        for (int i = 0; i < cityIdList.size(); i++) {
            if (servercityId.equals(cityIdList.get(i))) {
                pos = i;
                break;
            }
        }
        cityspinner.setSelection(pos);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.startDateLayout:
                setStartDate();
                break;
            case R.id.endDateLayout:
                setEndDate();
                break;
            case R.id.submit:
                if (isValidate()) {
                    FilterHome filterHome = new FilterHome();
                    filterHome.setStartDate(startDateStr);
                    filterHome.setEndDate(enddateStr);
                    filterHome.setBedroomsId(String.valueOf(bedminteger));//bedroomsStr
                    filterHome.setSleepsId(String.valueOf(guestminteger));//sleepsStr
                    filterHome.setGenderId(genderStr);
                    filterHome.setReligionId(religionStr);
                    filterHome.setTravleId(travleIdStr);
                    filterHome.setCountryId(countryId);
                    filterHome.setCityId(cityId);
                    filterHome.setTravelCityName(travelCityName);
                    filterHome.setTravelCountryName(travelCountryName);
                    String homeFilter = new Gson().toJson(filterHome);
                    Intent intent = new Intent(context, DashboardActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                    intent.putExtra("HomeFilter", homeFilter);
                    startActivity(intent);
                }
                break;
            case R.id.showAllfilter:
                moreFilterLayout.setVisibility(View.VISIBLE);
                break;
            case R.id.travelRoutine:
                openTravelScreen();
                break;
            case R.id.addition:
                increaseBed();
                break;
            case R.id.subtraction:
                if (bedminteger > 0) {
                    decreaseBed();
                }
                break;
            case R.id.addition1:
                increaseguestview();
                break;
            case R.id.subtraction1:
                if (guestminteger > 0) {
                    decreaseguestview();
                }
                break;
        }
    }

    public void setStartDate() {
        String myFormat = "yyyy-MM-dd"; //In which you need put here
        final SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);
        final Calendar myCalendar = Calendar.getInstance();
        try {
            Date sdate = sdf.parse(startDate);
            myCalendar.setTime(sdate);
        } catch (ParseException e) {
        }
        final DatePickerDialog.OnDateSetListener date = new DatePickerDialog.OnDateSetListener() {

            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear,
                                  int dayOfMonth) {
                // TODO Auto-generated method stub
                myCalendar.set(Calendar.YEAR, year);
                myCalendar.set(Calendar.MONTH, monthOfYear);
                myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                etYear.setText(sdf.format(myCalendar.getTime()));
                // datemilisec = myCalendar.getTimeInMillis();
            }
        };
        final DatePickerDialog pickerDialog = new DatePickerDialog(context, date, myCalendar
                .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                myCalendar.get(Calendar.DAY_OF_MONTH));
        pickerDialog.getDatePicker().setMinDate(System.currentTimeMillis() - 1000);
        pickerDialog.show();
    }

    public void setEndDate() {
        String myFormat = "yyyy-MM-dd"; //In which you need put here
        final SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);
        final Calendar myCalendar = Calendar.getInstance();
        try {
            Date sdate = sdf.parse(endDate);
            myCalendar.setTime(sdate);
        } catch (ParseException e) {
        }
        final DatePickerDialog.OnDateSetListener date = new DatePickerDialog.OnDateSetListener() {

            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear,
                                  int dayOfMonth) {
                // TODO Auto-generated method stub
                myCalendar.set(Calendar.YEAR, year);
                myCalendar.set(Calendar.MONTH, monthOfYear);
                myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                enddate.setText(sdf.format(myCalendar.getTime()));
                // datemilisec = myCalendar.getTimeInMillis();
            }
        };
        final DatePickerDialog pickerDialog = new DatePickerDialog(context, date, myCalendar
                .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                myCalendar.get(Calendar.DAY_OF_MONTH));
        pickerDialog.getDatePicker().setMinDate(System.currentTimeMillis() - 1000);
        pickerDialog.show();
    }

    public static boolean isDateValid(String date) {
        String myFormat = "yyyy-MM-dd"; //In which you need put here
        try {
            DateFormat df = new SimpleDateFormat(myFormat);
            df.setLenient(false);
            df.parse(date);
            return true;
        } catch (ParseException e) {
            return false;
        }
    }

    public void increaseBed() {
        bedminteger = bedminteger + 1;
        bedView.setText("" + bedminteger);
    }

    public void decreaseBed() {
        bedminteger = bedminteger - 1;
        bedView.setText("" + bedminteger);
    }

    public void increaseguestview() {
        guestminteger = guestminteger + 1;
        guestview.setText("" + guestminteger);
    }

    public void decreaseguestview() {
        guestminteger = guestminteger - 1;
        guestview.setText("" + guestminteger);
    }

    // ----validation -----
    private boolean isValidate() {

        startDateStr = etYear.getText().toString();
        enddateStr = enddate.getText().toString();
        if (!isDateValid(startDateStr)) {
            Utility.showToast(context, "Please select Start Date!");
            return false;
        } else if (!isDateValid(enddateStr)) {
            Utility.showToast(context, "Please select End Date!");
            return false;
        } else if (countryId.equals("") && countryId.equals("0")) {
            Utility.showToast(context, "Please select County!");
            return false;
        } else if (cityId.equals("")) {
            Utility.showToast(context, "Please select City!");
            return false;
        }
        return true;
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
}
