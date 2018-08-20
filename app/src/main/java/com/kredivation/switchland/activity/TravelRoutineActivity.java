package com.kredivation.switchland.activity;

import android.app.DatePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.graphics.Typeface;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

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
import com.kredivation.switchland.model.House_rules;
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
    String[] cityList;
    private String cityID = "";
    private String countryID = "";
    private String userId;


    String homestyleId, securitId, genderId, religionId, familyId, petsId, typeOfPropertiesId;
    int sleepsid, bathroomsId, bedroomsId;
    String titleStr, aboutHomeStr;
    List<House_rules> saveRuleList;
    List<Features> saveFeatureList;
    String addressStr, landmarkStr, zipCodeStr, enterzipcodeStr, saveCountryId, saveCityId, Hno;
    ArrayList<ChatData> homePhotoList;
    String profileimgStr, travleIdStr, dreamStr;
    String monthId, yearId, cvvStr, Cardno, CardNameStr;
    HashMap<String, String> payloadList;

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
        endDateLayout = findViewById(R.id.dateLayout);
        citySpinner = findViewById(R.id.citySpinner);
        countrySpinner = findViewById(R.id.countrySpinner);
        submit = findViewById(R.id.submit);
        dateLayout.setOnClickListener(this);
        edateIcon.setOnClickListener(this);
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
                            countryID = country[position].getId();
                            city = MData.getCity();
                            if (city != null) {
                                cityList = new String[city.length];
                                for (int i = 0; i < city.length; i++) {
                                    if (countryID.equals(city[i].getCountry_id())) {
                                        cityList[i] = String.valueOf(city[i].getName());
                                    }
                                }
                                if (cityList != null && cityList.length > 0) {
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
                if (countryID.equals(city[position].getCountry_id())) {
                    cityID = city[position].getId();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
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
        edateIcon.setOnClickListener(new View.OnClickListener() {
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
        } else if (countryID.equals("") && countryID.equals("0")) {
            Utility.showToast(TravelRoutineActivity.this, "Please select County!");
            return false;
        } else if (cityID.equals("")) {
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
            case R.id.edateIcon:
                setEndDate();
                break;
            case R.id.submit:
                if (isValidate()) {
                    addHomeServer();
                }
                break;
        }
    }

    //get all save home data
    private void getAllHomeDataFromSharePre() {
        SharedPreferences prefs = getSharedPreferences("AddHomePreferences", Context.MODE_PRIVATE);
        if (prefs != null) {
            //------home Overview screen----------
            homestyleId = prefs.getString("homestyleStr", "");
            securitId = prefs.getString("securitStr", "");
            genderId = prefs.getString("genderStr", "");
            religionId = prefs.getString("religionStr", "");
            familyId = prefs.getString("familyStr", "");
            petsId = prefs.getString("petsStr", "");
            typeOfPropertiesId = prefs.getString("typeOfPropertiesStr", "");
            sleepsid = prefs.getInt("sleepsStr", 0);
            bathroomsId = prefs.getInt("bathroomsStr", 0);
            bedroomsId = prefs.getInt("bedroomsStr", 0);

            //-----------home detail screen----------
            titleStr = prefs.getString("Title", "");
            aboutHomeStr = prefs.getString("AboutHome", "");
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
            //--------------home Location screen-------------
            addressStr = prefs.getString("Address", "");
            landmarkStr = prefs.getString("LandMark", "");
            zipCodeStr = prefs.getString("ZipCode", "");
            enterzipcodeStr = prefs.getString("EnterZipcode", "");
            saveCountryId = prefs.getString("CountryId", "");
            saveCityId = prefs.getString("CityId", "");
            Hno = prefs.getString("Hno", "");

            //---------home photo screen-----
            String homeStr = prefs.getString("HomeImage", "");
            if (homeStr != null && !homeStr.equals("") && !homeStr.equals("[]")) {
                homePhotoList = new Gson().fromJson(homeStr, new TypeToken<ArrayList<ChatData>>() {
                }.getType());
            }

            //---------home my profile screen-------------
            profileimgStr = prefs.getString("ProfileImage", "");
            travleIdStr = prefs.getString("TravleId", "");
            dreamStr = prefs.getString("DreamDetail", "");
            // savetravelPos = prefs.getInt("savetravelPos", 0);

            //----------home card detail----------
            monthId = prefs.getString("monthId", "");
            //monthPos = prefs.getInt("monthPos", 0);
            yearId = prefs.getString("yearId", "");
            // yearPos = prefs.getInt("yearPos", 0);
            cvvStr = prefs.getString("CVV", "");
            CardNameStr = prefs.getString("Name", "");
            Cardno = prefs.getString("Cardno", "");
        }
        setValueIntoPayload();
    }

    private void setValueIntoPayload() {
        payloadList = new HashMap<String, String>();
        payloadList.put("api_key", Contants.API_KEY);
        payloadList.put("home_id", "");
        payloadList.put("user_id", userId);
        payloadList.put("title", titleStr);
        payloadList.put("sort_description", aboutHomeStr);
        payloadList.put("house_no", Hno);
        payloadList.put("home_type", homestyleId);
        payloadList.put("bedrooms", String.valueOf(bedroomsId));
        payloadList.put("bathrooms", String.valueOf(bathroomsId));
        payloadList.put("sleeps", String.valueOf(sleepsid));
        payloadList.put("property_type", typeOfPropertiesId);
        payloadList.put("pets", petsId);
        payloadList.put("family", familyId);
        payloadList.put("location", addressStr);//need ask
        payloadList.put("latitude", "20.7");
        payloadList.put("longitude", "30.7");
        payloadList.put("destinations", dreamStr);
        payloadList.put("traveller_type", travleIdStr);
        payloadList.put("startdate", startDateStr);
        payloadList.put("enddate", enddateStr);
        payloadList.put("country", countryID);
        payloadList.put("city", cityID);
        payloadList.put("address1", Hno);
        payloadList.put("address2", addressStr);
        if (zipCodeStr != null && !zipCodeStr.equals("")) {
            payloadList.put("zipcode", zipCodeStr);
        }
        if (enterzipcodeStr != null && !enterzipcodeStr.equals("")) {
            payloadList.put("zipcode", enterzipcodeStr);
        }
        payloadList.put("gender", genderId);
        payloadList.put("religion", religionId);
        payloadList.put("landmark", landmarkStr);
        payloadList.put("level_of_security", securitId);
        payloadList.put("cardnumber", Cardno);
        payloadList.put("nameoncard", CardNameStr);
        payloadList.put("month", monthId);
        payloadList.put("Year", yearId);
        payloadList.put("Cvv", cvvStr);
    }

    //save add home data into server
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
                            SharedPreferences prefs = getSharedPreferences("AddHomePreferences", Context.MODE_PRIVATE);
                            prefs.edit().clear().commit();
                            Utility.showToast(TravelRoutineActivity.this, serviceData.getMsg());
                            Intent intent = new Intent(TravelRoutineActivity.this, MainActivity.class);
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
        for (Features features : saveFeatureList) {
            multipartBody.addFormDataPart("feature_id[]", features.getId());
        }
        for (House_rules rules : saveRuleList) {
            multipartBody.addFormDataPart("house_rule_id[]", rules.getId());
        }
        //image add
        File profilefile = new File(profileimgStr);
        if (profilefile != null && profilefile.exists()) {
            multipartBody.addFormDataPart("profile_img", profilefile.getName(), RequestBody.create(MEDIA_TYPE_PNG, profilefile));
        }
        for (ChatData chatData : homePhotoList) {
            File homeFile = chatData.getImageFile();
            if (homeFile != null && homeFile.exists()) {
                multipartBody.addFormDataPart("uploaded_image[]", homeFile.getName(), RequestBody.create(MEDIA_TYPE_PNG, homeFile));
            }
        }
        return multipartBody;
    }

}
