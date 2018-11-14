package com.kredivation.switchland.activity;

import android.Manifest;
import android.app.DatePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.graphics.Typeface;
import android.location.Location;
import android.os.Looper;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResponse;
import com.google.android.gms.location.SettingsClient;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.kredivation.switchland.R;
import com.kredivation.switchland.database.SwitchDBHelper;
import com.kredivation.switchland.framework.FileUploaderHelper;
import com.kredivation.switchland.model.ChatData;
import com.kredivation.switchland.model.City;
import com.kredivation.switchland.model.Country;
import com.kredivation.switchland.model.Data;
import com.kredivation.switchland.model.Features;
import com.kredivation.switchland.model.FilterHome;
import com.kredivation.switchland.model.HomeDetails;
import com.kredivation.switchland.model.Home_features;
import com.kredivation.switchland.model.Home_rules;
import com.kredivation.switchland.model.Homegallery;
import com.kredivation.switchland.model.House_rules;
import com.kredivation.switchland.model.MyhomeArray;
import com.kredivation.switchland.model.ServiceContentData;
import com.kredivation.switchland.utilities.ASTProgressBar;
import com.kredivation.switchland.utilities.CompatibilityUtility;
import com.kredivation.switchland.utilities.Contants;
import com.kredivation.switchland.utilities.FontManager;
import com.kredivation.switchland.utilities.Utility;

import java.io.File;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;

public class TravelRoutineActivity extends AppCompatActivity implements View.OnClickListener {
    private Toolbar toolbar;

    Typeface materialdesignicons_font;
    TextView etYear, enddate;
    LinearLayout dateLayout, endDateLayout;
    private Spinner citySpinner, countrySpinner;
    private String startDateStr, enddateStr;
    private Button submit;
    TextView edateIcon;
    private City[] city;
    private Country[] country;
    String[] countryList;
    ArrayList<String> cityList;
    private String homeCityID = "";
    private String homeCountryID = "";
    private String travelcityID = "";
    private String travelcountryID = "";
    private String userId;


    String homestyleId, securitId, genderId, religionId, familyId, petsId, typeOfPropertiesId, homeId;
    String sleepsid, bathroomsId, bedroomsId;
    String titleStr, aboutHomeStr;
    List<Home_rules> saveRuleList;
    List<Home_features> saveFeatureList;
    String addressStr, landmarkStr, enterzipcodeStr, saveCountryId, saveCityId, Hno;
    ArrayList<Homegallery> homePhotoList;
    String profileimgStr, travleIdStr, dreamStr;
    String monthId, yearId, cvvStr, Cardno, CardNameStr;
    HashMap<String, String> payloadList;
    HomeDetails MyHomedata;
    boolean MyHomeAdapterFlage;
    ArrayList<String> cityIdList;

    private String currentLongitude = "";
    private String currentLatitude = "";
    private FusedLocationProviderClient mFusedLocationClient;
    private LocationRequest mLocationRequest;
    private LocationCallback mLocationCallback;
    private long UPDATE_INTERVAL = 10 * 1000;  /* 10 secs */
    private long FASTEST_INTERVAL = 2000; /* 2 sec */

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_travel_routine);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if (CompatibilityUtility.isTablet(TravelRoutineActivity.this)) {
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        } else {
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        }
        init();
    }

    private void init() {
        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(TravelRoutineActivity.this);
        TextView dateIcon = findViewById(R.id.dateIcon);
        materialdesignicons_font = FontManager.getFontTypefaceMaterialDesignIcons(TravelRoutineActivity.this, "fonts/materialdesignicons-webfont.otf");
        dateIcon.setTypeface(materialdesignicons_font);
        dateIcon.setText(Html.fromHtml("&#xf0ed;"));
        etYear = findViewById(R.id.etYear);
        dateLayout = findViewById(R.id.dateLayout);
        dateLayout.setOnClickListener(this);

        edateIcon = findViewById(R.id.edateIcon);
        edateIcon.setTypeface(materialdesignicons_font);
        edateIcon.setText(Html.fromHtml("&#xf0ed;"));
        enddate = findViewById(R.id.enddate);
        endDateLayout = findViewById(R.id.endDateLayout);
        endDateLayout.setOnClickListener(this);
        citySpinner = findViewById(R.id.citySpinner);
        countrySpinner = findViewById(R.id.countrySpinner);
        submit = findViewById(R.id.submit);
        submit.setOnClickListener(this);
        TextView back = (TextView) toolbar.findViewById(R.id.back);
        back.setTypeface(materialdesignicons_font);
        back.setText(Html.fromHtml("&#xf30d;"));
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        setUiData();

    }

    private void setUiData() {
        SwitchDBHelper switchDBHelper = new SwitchDBHelper(TravelRoutineActivity.this);
        ArrayList<Data> userData = switchDBHelper.getAllUserInfoList();
        if (userData != null) {
            for (Data data : userData) {
                userId = data.getId();
            }
            //get posted home details
            ArrayList<MyhomeArray> myHomeList = switchDBHelper.getAllMyhomedata();
            for (MyhomeArray myhomeArray : myHomeList) {
                homeCountryID = myhomeArray.getCountry_id();
                homeCityID = myhomeArray.getCity_id();
            }
        }
        ServiceContentData sData = switchDBHelper.getMasterData();
        if (sData != null) {
            if (sData.getData() != null) {
                final Data MData = sData.getData();
                country = MData.getCountry();
                if (country != null) {
                    countryList = new String[country.length];
                    for (int i = 0; i < country.length; i++) {
                        countryList[i] = String.valueOf(country[i].getName());
                    }
                    ArrayAdapter<String> countryAdapter = new ArrayAdapter<String>(TravelRoutineActivity.this, R.layout.spinner_row, countryList);
                    countrySpinner.setAdapter(countryAdapter);
                    countrySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                        @Override
                        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                            travelcountryID = country[position].getId();
                            city = MData.getCity();
                            if (city != null) {
                                cityList = new ArrayList();
                                cityIdList = new ArrayList();
                                for (int i = 0; i < city.length; i++) {
                                    if (travelcountryID.equals(city[i].getCountry_id())) {
                                        cityList.add(String.valueOf(city[i].getName()));
                                        cityIdList.add(String.valueOf(city[i].getId()));
                                    }
                                }
                                if (cityList != null && cityList.size() > 0) {
                                    ArrayAdapter<String> cityAdapter = new ArrayAdapter<String>(TravelRoutineActivity.this, R.layout.spinner_row, cityList);
                                    citySpinner.setAdapter(cityAdapter);
                                }
                            }
                        }

                        @Override
                        public void onNothingSelected(AdapterView<?> parent) {

                        }
                    });
                }
            }
        }
        citySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                travelcityID = cityIdList.get(position).toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        MyHomeAdapterFlage = getIntent().getBooleanExtra("MyHomeAdapterFlage", false);
        if (MyHomeAdapterFlage) {
            startDateStr = getIntent().getStringExtra("StartDate");
            enddateStr = getIntent().getStringExtra("EndDate");
            String countryID = getIntent().getStringExtra("CountryId");//travel country id
            String cityID = getIntent().getStringExtra("CityId");//travel city id
            homeId = getIntent().getStringExtra("HomeId");
            enddate.setText(enddateStr);
            etYear.setText(startDateStr);
            getSelectedCountry(countryID);
            getSelectedCity(cityID);
        } else {
            getSaveData();//come from edit
        }
    }

    public void setStartDate() {
        String myFormat = "yyyy-MM-dd"; //In which you need put here
        final SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);
        final Calendar myCalendar = Calendar.getInstance();
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
        final DatePickerDialog pickerDialog = new DatePickerDialog(TravelRoutineActivity.this, date, myCalendar
                .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                myCalendar.get(Calendar.DAY_OF_MONTH));
        pickerDialog.getDatePicker().setMinDate(System.currentTimeMillis() - 1000);
        dateLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                pickerDialog.show();
            }
        });
    }

    public void setEndDate() {
        String myFormat = "yyyy-MM-dd"; //In which you need put here
        final SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);
        final Calendar myCalendar = Calendar.getInstance();
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
        final DatePickerDialog pickerDialog = new DatePickerDialog(TravelRoutineActivity.this, date, myCalendar
                .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                myCalendar.get(Calendar.DAY_OF_MONTH));
        pickerDialog.getDatePicker().setMinDate(System.currentTimeMillis() - 1000);
        endDateLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                pickerDialog.show();
            }
        });
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

    // ----validation -----
    private boolean isValidate() {
        startDateStr = etYear.getText().toString();
        enddateStr = enddate.getText().toString();
        if (!isDateValid(startDateStr)) {
            Utility.showToast(TravelRoutineActivity.this, "Please select Start Date!");
            return false;
        } else if (!isDateValid(enddateStr)) {
            Utility.showToast(TravelRoutineActivity.this, "Please select End Date!");
            return false;
        } else if (travelcountryID.equals("") && travelcountryID.equals("0")) {
            Utility.showToast(TravelRoutineActivity.this, "Please select County!");
            return false;
        } else if (travelcityID.equals("")) {
            Utility.showToast(TravelRoutineActivity.this, "Please select City!");
            return false;
        }
        return true;
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.dateLayout:
                setStartDate();
                break;
            case R.id.endDateLayout:
                setEndDate();
                break;
            case R.id.submit:
                if (MyHomeAdapterFlage) {
                    if (isValidate()) {
                        rePostHome();
                    }
                } else {
                    if (isValidate()) {
                        addHomeServer();
                    }
                }
                break;
        }
    }

    //get data from edit
    private void getSaveData() {
        SwitchDBHelper dbHelper = new SwitchDBHelper(TravelRoutineActivity.this);
        ArrayList<HomeDetails> homeDetails = dbHelper.getAllAddEditHomeDataList();
        if (homeDetails != null) {
            for (HomeDetails details : homeDetails) {
                MyHomedata = details;
                if (MyHomedata != null) {
                    homeId = MyHomedata.getId();
                    startDateStr = MyHomedata.getStartdate();
                    enddateStr = MyHomedata.getEnddate();
                    enddate.setText(enddateStr);
                    etYear.setText(startDateStr);
                    String countryID = MyHomedata.getTravel_country();
                    String cityID = MyHomedata.getTravel_city();
                    getSelectedCountry(countryID);
                    getSelectedCity(cityID);

                }
            }
        }
    }

    private void getSelectedCountry(String countryID) {
        if (country != null) {
            for (int i = 0; i < country.length; i++) {
                if (countryID.equals(country[i].getId())) {
                    countrySpinner.setSelection(i);
                    break;
                }
            }
        }
    }

    private void getSelectedCity(String cityID) {
        if (city != null) {
            for (int i = 0; i < city.length; i++) {
                if (cityID.equals(city[i].getId())) {
                    citySpinner.setSelection(i);
                    break;
                }
            }
        }
    }

    //get all save home data
    private void getAllHomeDataFromSharePre() {
        //SwitchDBHelper dbHelper = new SwitchDBHelper(TravelRoutineActivity.this);
        //  MyHomedata = dbHelper.getAddEditHomeDataById(MainhomeId);

        homeId = MyHomedata.getId();
        //------home Overview screen----------
        homestyleId = MyHomedata.getHome_type();
        securitId = MyHomedata.getLevel_security();
        genderId = MyHomedata.getGender();
        religionId = MyHomedata.getReligion();
        familyId = MyHomedata.getFamily_matters();
        petsId = MyHomedata.getPets();
        typeOfPropertiesId = MyHomedata.getProperty_type();
        sleepsid = MyHomedata.getSleeps();
        bathroomsId = MyHomedata.getBathrooms();
        bedroomsId = MyHomedata.getBedrooms();

        //-----------home detail screen----------
        titleStr = MyHomedata.getTitle();
        aboutHomeStr = MyHomedata.getSort_description();
        saveFeatureList = MyHomedata.getFeatureList();
        saveRuleList = MyHomedata.getHouseRuleList();

        //--------------home Location screen-------------
        addressStr = MyHomedata.getAddress1();
        landmarkStr = MyHomedata.getLandmarks();
        //zipCodeStr = MyHomedata.getLocation();
        enterzipcodeStr = MyHomedata.getZipcode();
        saveCountryId = MyHomedata.getCountry_id();
        saveCityId = MyHomedata.getCity_id();
        Hno = MyHomedata.getHouse_no();

        //---------home photo screen-----
        homePhotoList = MyHomedata.getHomeImageList();
        //---------home my profile screen-------------
        profileimgStr = MyHomedata.getProfile_image();
        travleIdStr = MyHomedata.getTraveller_type();
        dreamStr = MyHomedata.getDestinations();
        //----------home card detail----------
        monthId = MyHomedata.getMonth();
        yearId = MyHomedata.getYear();
        cvvStr = MyHomedata.getCvv();
        CardNameStr = MyHomedata.getNameoncard();
        Cardno = MyHomedata.getCardnumber();
        setValueIntoPayload();
    }

    private void setValueIntoPayload() {
        payloadList = new HashMap<String, String>();
        payloadList.put("api_key", Contants.API_KEY);
        payloadList.put("home_id", homeId);
        payloadList.put("user_id", userId);
        payloadList.put("title", titleStr);
        payloadList.put("sort_description", aboutHomeStr);
        payloadList.put("house_no", Hno);
        payloadList.put("home_type", homestyleId);
        payloadList.put("bedrooms", bedroomsId);
        payloadList.put("bathrooms", bathroomsId);
        payloadList.put("sleeps", sleepsid);
        payloadList.put("property_type", typeOfPropertiesId);
        payloadList.put("pets", petsId);
        payloadList.put("family", familyId);
        // payloadList.put("location", zipCodeStr);
        payloadList.put("latitude", currentLatitude);
        payloadList.put("longitude", currentLongitude);
        payloadList.put("destinations", dreamStr);
        payloadList.put("traveller_type", travleIdStr);
        payloadList.put("startdate", startDateStr);
        payloadList.put("enddate", enddateStr);
        payloadList.put("country", homeCountryID);
        payloadList.put("city", homeCityID);
        payloadList.put("travel_city", travelcityID);
        payloadList.put("travel_country", travelcountryID);
        payloadList.put("address1", Hno);
        payloadList.put("address2", addressStr);
        payloadList.put("zipcode", enterzipcodeStr);
        payloadList.put("gender", genderId);
        payloadList.put("religion", religionId);
        payloadList.put("landmark", landmarkStr);
        payloadList.put("level_of_security", securitId);
        payloadList.put("cardnumber", Cardno);
        payloadList.put("nameoncard", CardNameStr);
        payloadList.put("month", monthId);
        payloadList.put("year", yearId);
        payloadList.put("cvv", cvvStr);
    }

    //save edit home data into server
    public void addHomeServer() {
        if (Utility.isOnline(TravelRoutineActivity.this)) {
            final ASTProgressBar progressBar = new ASTProgressBar(TravelRoutineActivity.this);
            progressBar.show();

            String serviceURL = Contants.BASE_URL + Contants.Addhome;
            getAllHomeDataFromSharePre();
            MultipartBody.Builder multipartBody = setMultipartBodyVaule();

            FileUploaderHelper fileUploaderHelper = new FileUploaderHelper(TravelRoutineActivity.this, payloadList, multipartBody, serviceURL) {
                @Override
                public void receiveData(String result) {
                    final ServiceContentData serviceData = new Gson().fromJson(result, ServiceContentData.class);
                    if (serviceData != null) {
                        if (serviceData.isSuccess()) {
                            Utility.showToast(TravelRoutineActivity.this, serviceData.getMsg());
                            FilterHome filterHome = new FilterHome();
                            filterHome.setStartDate(startDateStr);
                            filterHome.setEndDate(enddateStr);
                            filterHome.setCountryId(travelcountryID);
                            filterHome.setCityId(travelcityID);
                            String homeFilter = new Gson().toJson(filterHome);
                            Intent intent = new Intent(TravelRoutineActivity.this, DashboardActivity.class);
                            intent.putExtra("HomeFilter", homeFilter);
                            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                            startActivity(intent);
                        } else {
                            Utility.showToast(TravelRoutineActivity.this, serviceData.getMsg());
                        }
                    } else {
                        Utility.showToast(TravelRoutineActivity.this, "Server Side error!");
                    }
                    if (progressBar.isShowing()) {
                        progressBar.dismiss();
                    }
                }
            };
            fileUploaderHelper.execute();
        } else {
            Utility.alertForErrorMessage(Contants.OFFLINE_MESSAGE, TravelRoutineActivity.this);//off line msg....
        }
    }


    // profile_img  ,feature_id[],house_rule_id[],uploaded_image[]
    //add images into MultipartBody for send as multipart
    private MultipartBody.Builder setMultipartBodyVaule() {
        MediaType MEDIA_TYPE_PNG = MediaType.parse("image/png");
        MultipartBody.Builder multipartBody = new MultipartBody.Builder().setType(MultipartBody.FORM);
        //for string add
        for (Home_features features : saveFeatureList) {
            multipartBody.addFormDataPart("feature_id[]", features.getId());
        }
        for (Home_rules rules : saveRuleList) {
            multipartBody.addFormDataPart("house_rule_id[]", rules.getId());
        }
        //image add
        File profilefile = new File(profileimgStr);//
        if (profilefile != null && profilefile.exists()) {
            multipartBody.addFormDataPart("profile_img", profilefile.getName(), RequestBody.create(MEDIA_TYPE_PNG, profilefile));
        }
        for (Homegallery himage : homePhotoList) {
            File homeFile = new File(himage.getPhoto());
            if (homeFile != null && homeFile.exists()) {
                multipartBody.addFormDataPart("uploaded_image[]", homeFile.getName(), RequestBody.create(MEDIA_TYPE_PNG, homeFile));
            }
        }
        return multipartBody;
    }

    //----------------for direct Myhome adapter call--------------
    public void rePostHome() {
        if (Utility.isOnline(TravelRoutineActivity.this)) {
            final ASTProgressBar progressBar = new ASTProgressBar(TravelRoutineActivity.this);
            progressBar.show();
            String serviceURL = Contants.BASE_URL + Contants.Addhome;
            HashMap<String, String> payloadList = new HashMap<String, String>();
            payloadList.put("api_key", Contants.API_KEY);
            payloadList.put("home_id", homeId);
            payloadList.put("user_id", userId);
            payloadList.put("startdate", startDateStr);
            payloadList.put("enddate", enddateStr);
            payloadList.put("city", homeCityID);
            payloadList.put("country", homeCountryID);
            payloadList.put("travel_city", travelcityID);
            payloadList.put("travel_country", travelcountryID);
            final MediaType MEDIA_TYPE_PNG = MediaType.parse("image/jpg");
            MultipartBody.Builder multipartBody = new MultipartBody.Builder().setType(MultipartBody.FORM);
            FileUploaderHelper fileUploaderHelper = new FileUploaderHelper(TravelRoutineActivity.this, payloadList, multipartBody, serviceURL) {
                @Override
                public void receiveData(String result) {
                    if (result != null) {
                        final ServiceContentData serviceData = new Gson().fromJson(result, ServiceContentData.class);
                        if (serviceData != null) {
                            if (serviceData.isSuccess()) {
                                Utility.showToast(TravelRoutineActivity.this, serviceData.getMsg());
                                FilterHome filterHome = new FilterHome();
                                filterHome.setStartDate(startDateStr);
                                filterHome.setEndDate(enddateStr);
                                filterHome.setCountryId(travelcountryID);
                                filterHome.setCityId(travelcityID);
                                String homeFilter = new Gson().toJson(filterHome);
                                Intent intent = new Intent(TravelRoutineActivity.this, DashboardActivity.class);
                                intent.putExtra("HomeFilter", homeFilter);
                                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                                startActivity(intent);
                            } else {
                                Utility.showToast(TravelRoutineActivity.this, serviceData.getMsg());
                            }
                        } else {
                            Utility.showToast(TravelRoutineActivity.this, "Server Side error!");
                        }
                    } else {
                        Utility.showToast(TravelRoutineActivity.this, "Server Side error!");
                    }
                    if (progressBar.isShowing()) {
                        progressBar.dismiss();
                    }
                }
            };
            fileUploaderHelper.execute();
        } else {
            Utility.alertForErrorMessage(Contants.OFFLINE_MESSAGE, TravelRoutineActivity.this);//off line msg....
        }

    }

    // Trigger new location updates at interval
    protected void startLocationUpdates() {

        // Create the location request to start receiving updates
        mLocationRequest = new LocationRequest();
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        mLocationRequest.setInterval(UPDATE_INTERVAL);
        mLocationRequest.setFastestInterval(FASTEST_INTERVAL);

        // Create LocationSettingsRequest object using location request
        LocationSettingsRequest.Builder builder = new LocationSettingsRequest.Builder();
        builder.addLocationRequest(mLocationRequest);
        LocationSettingsRequest locationSettingsRequest = builder.build();

        // Check whether location settings are satisfied
        // https://developers.google.com/android/reference/com/google/android/gms/location/SettingsClient
        SettingsClient settingsClient = LocationServices.getSettingsClient(TravelRoutineActivity.this);
        settingsClient.checkLocationSettings(locationSettingsRequest);
        Task<LocationSettingsResponse> task = settingsClient.checkLocationSettings(builder.build());

        // new Google API SDK v11 uses getFusedLocationProviderClient(this)
        if (ActivityCompat.checkSelfPermission(TravelRoutineActivity.this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(TravelRoutineActivity.this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        mLocationCallback = new LocationCallback() {
            @Override
            public void onLocationResult(LocationResult locationResult) {
                if (locationResult == null) {
                    return;
                }
               /* for (Location location : locationResult.getLocations()) {
                    // Update UI with location data
                    // ...
                }*/
                onLocationChanged(locationResult.getLastLocation());
            }
        };
        mFusedLocationClient.requestLocationUpdates(mLocationRequest, mLocationCallback, Looper.myLooper());
       /* mFusedLocationClient.requestLocationUpdates(mLocationRequest, new LocationCallback() {
                    @Override
                    public void onLocationResult(LocationResult locationResult) {
                        if (locationResult == null) {
                            return;
                        }
                        onLocationChanged(locationResult.getLastLocation());
                    }
                },
                Looper.myLooper());*/
    }

    //get location change
    public void onLocationChanged(Location location) {
        // New location has now been determined
        currentLongitude = Double.toString(location.getLongitude());
        currentLatitude = Double.toString(location.getLatitude());

        String msg = "Updated Location: " +
                Double.toString(location.getLatitude()) + "," +
                Double.toString(location.getLongitude());
        Log.d(Contants.LOG_TAG, "Current Location*******" + msg);

      /*  Toast.makeText(getContext(), msg, Toast.LENGTH_SHORT).show();
        // You can now create a LatLng Object for use with maps
         LatLng latLng = new LatLng(location.getLatitude(), location.getLongitude());*/
    }

    //get last location
    public void getLastLocation() {
        // Get last known recent location using new Google Play Services SDK (v11+)

        if (ActivityCompat.checkSelfPermission(TravelRoutineActivity.this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(TravelRoutineActivity.this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        mFusedLocationClient.getLastLocation().addOnSuccessListener(new OnSuccessListener<Location>() {
            @Override
            public void onSuccess(Location location) {
                // GPS location can be null if GPS is switched off
                if (location != null) {
                    onLocationChanged(location);
                }
            }
        })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.d(Contants.LOG_TAG, "Error trying to get last GPS location");
                        e.printStackTrace();
                    }
                });
    }

    @Override
    public void onResume() {
        super.onResume();
        if (mFusedLocationClient != null) {
            startLocationUpdates();
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        stopLocationUpdates();
    }

    //stop location update
    private void stopLocationUpdates() {
        if (mFusedLocationClient != null) {
            mFusedLocationClient.removeLocationUpdates(mLocationCallback);
        }
    }

}
