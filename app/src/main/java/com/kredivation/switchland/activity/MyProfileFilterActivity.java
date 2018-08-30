package com.kredivation.switchland.activity;

import android.app.DatePickerDialog;
import android.content.pm.ActivityInfo;
import android.graphics.Typeface;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.kredivation.switchland.R;
import com.kredivation.switchland.database.SwitchDBHelper;
import com.kredivation.switchland.model.Bathrooms;
import com.kredivation.switchland.model.Bedrooms;
import com.kredivation.switchland.model.City;
import com.kredivation.switchland.model.Country;
import com.kredivation.switchland.model.Data;
import com.kredivation.switchland.model.Family;
import com.kredivation.switchland.model.Genderarray;
import com.kredivation.switchland.model.MyhomeArray;
import com.kredivation.switchland.model.Pets_allowed;
import com.kredivation.switchland.model.Religion;
import com.kredivation.switchland.model.ServiceContentData;
import com.kredivation.switchland.model.Sleeps;
import com.kredivation.switchland.model.Type_of_property;
import com.kredivation.switchland.model.Type_of_traveller;
import com.kredivation.switchland.utilities.CompatibilityUtility;
import com.kredivation.switchland.utilities.FontManager;
import com.kredivation.switchland.utilities.Utility;
import com.squareup.picasso.Picasso;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;

public class MyProfileFilterActivity extends AppCompatActivity implements View.OnClickListener {
    private Toolbar toolbar;
    private String userId;
    private City[] city;
    private Country[] country;
    Spinner cityspinner, countryspinner, bedroomspinner, Religionspinner, Typeoftravellerspinner, genderspinner, Sleepspinner;
    String[] countryList;
    ArrayList<String> cityList;
    private String cityId = "";
    private String countryId = "";
    ImageView profileImage;
    TextView uNeme, uemail, phone, cityName, countryName, travlingDtae;
    String startDate = "";
    String endDate = "";
    String countryNameStr = "";
    String cityNameStr = "";
    Data MData;
    LinearLayout dateLayout, endDateLayout;
    TextView edateIcon;
    TextView etYear, enddate, homeTitle, showAllfilter;
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_profile_filter);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        chechPortaitAndLandSacpe();//chech Portait And LandSacpe Orientation
        initView();
    }

    //chech Portait And LandSacpe Orientation
    public void chechPortaitAndLandSacpe() {
        if (CompatibilityUtility.isTablet(MyProfileFilterActivity.this)) {

            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        } else {

            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        }

    }

    private void initView() {
        Typeface materialdesignicons_font = FontManager.getFontTypefaceMaterialDesignIcons(this, "fonts/materialdesignicons-webfont.otf");
        TextView locationIcon = (TextView) findViewById(R.id.locationIcon);
        locationIcon.setTypeface(materialdesignicons_font);
        locationIcon.setText(Html.fromHtml("&#xf34e;"));
        TextView calIcon = (TextView) findViewById(R.id.calIcon);
        calIcon.setTypeface(materialdesignicons_font);
        calIcon.setText(Html.fromHtml("&#xf0ed;"));
        TextView starticon = (TextView) findViewById(R.id.starticon);
        starticon.setTypeface(materialdesignicons_font);
        starticon.setText(Html.fromHtml("&#xf0ed;"));
        TextView endIcon = (TextView) findViewById(R.id.endIcon);
        endIcon.setTypeface(materialdesignicons_font);
        endIcon.setText(Html.fromHtml("&#xf0ed;"));
        TextView back = (TextView) toolbar.findViewById(R.id.back);
        back.setTypeface(materialdesignicons_font);
        back.setText(Html.fromHtml("&#xf30d;"));
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        profileImage = findViewById(R.id.profileImage);
        uNeme = findViewById(R.id.uNeme);
        uemail = findViewById(R.id.uemail);
        phone = findViewById(R.id.phone);

        cityName = findViewById(R.id.city);
        countryName = findViewById(R.id.country);
        travlingDtae = findViewById(R.id.travlingDtae);
        dateLayout = findViewById(R.id.startDateLayout);
        dateLayout.setOnClickListener(this);
        endDateLayout = findViewById(R.id.endDateLayout);
        endDateLayout.setOnClickListener(this);
        etYear = findViewById(R.id.startDate);
        enddate = findViewById(R.id.endDate);
        homeTitle = findViewById(R.id.homeTitle);
        bedroomspinner = findViewById(R.id.bedroomspinner);
        Religionspinner = findViewById(R.id.Religionspinner);
        Typeoftravellerspinner = findViewById(R.id.Typeoftravellerspinner);
        genderspinner = findViewById(R.id.genderspinner);
        Sleepspinner = findViewById(R.id.Sleepspinner);
        submit = findViewById(R.id.submit);
        submit.setOnClickListener(this);

        showAllfilter = findViewById(R.id.showAllfilter);
        showAllfilter.setOnClickListener(this);
        moreFilterLayout = findViewById(R.id.moreFilterLayout);
        /*SeekBar seekBar = (SeekBar)findViewById(R.id.seekBar2);

        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener(){

            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                Toast.makeText(MyProfileFilterActivity.this,
                        "Seekbar vale "+i, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
                Toast.makeText(MyProfileFilterActivity.this,
                        "Seekbar touch started", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                Toast.makeText(MyProfileFilterActivity.this,
                        "Seekbar touch stopped", Toast.LENGTH_SHORT).show();
            }
        });
*/
        cityspinner = (Spinner) findViewById(R.id.cityspinner);
        countryspinner = (Spinner) findViewById(R.id.countryspinner);
        getUserdata();
        getAllDataFromDB();
    }


    private void setSpinerValue() {
        ArrayAdapter<String> bedroomadapter = new ArrayAdapter<String>(MyProfileFilterActivity.this, R.layout.spinner_row, bedroomsList);
        bedroomspinner.setAdapter(bedroomadapter);
        ArrayAdapter<String> sleepadapter = new ArrayAdapter<String>(MyProfileFilterActivity.this, R.layout.spinner_row, sleepsList);
        Sleepspinner.setAdapter(sleepadapter);
        ArrayAdapter<String> reliadapter = new ArrayAdapter<String>(MyProfileFilterActivity.this, R.layout.spinner_row, religionList);
        Religionspinner.setAdapter(reliadapter);
        ArrayAdapter<String> genderadapter = new ArrayAdapter<String>(MyProfileFilterActivity.this, R.layout.spinner_row, genderList);
        genderspinner.setAdapter(genderadapter);
        ArrayAdapter<String> typeoftravellerspinneradapter = new ArrayAdapter<String>(MyProfileFilterActivity.this, R.layout.spinner_row, travleList);
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
        bedroomspinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                //  bedroomsStr = bedrooms[position].getId();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        Sleepspinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                // sleepsStr = sleeps[position].getId();
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

        getMyHomeDetail();
    }

    private void getAllDataFromDB() {
        SwitchDBHelper switchDBHelper = new SwitchDBHelper(MyProfileFilterActivity.this);
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
                    ArrayAdapter<String> countryAdapter = new ArrayAdapter<String>(MyProfileFilterActivity.this, R.layout.spinner_row, countryList);
                    countryspinner.setAdapter(countryAdapter);
                    countryspinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                        @Override
                        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                            countryId = country[position].getId();
                            //setCityAdapter(countryId);
                        }

                        @Override
                        public void onNothingSelected(AdapterView<?> parent) {

                        }
                    });
                }
                // getCitySelectPos();
                cityspinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                        cityId = city[position].getId();
                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> parent) {

                    }
                });
            }
        }
        setSpinerValue();
    }

    private void setCityAdapter(String countryId) {
        city = MData.getCity();
        if (city != null) {
            cityList = new ArrayList();
            for (int i = 0; i < city.length; i++) {
                if (countryId.equals(city[i].getCountry_id())) {
                    cityList.add(String.valueOf(city[i].getName()));
                }
            }
            if (cityList != null && cityList.size() > 0) {
                ArrayAdapter<String> cityAdapter = new ArrayAdapter<String>(MyProfileFilterActivity.this, R.layout.spinner_row, cityList);
                cityspinner.setAdapter(cityAdapter);
            }
        }
    }

    private void getUserdata() {
        SwitchDBHelper switchDBHelper = new SwitchDBHelper(MyProfileFilterActivity.this);
        ArrayList<Data> userData = switchDBHelper.getAllUserInfoList();
        if (userData != null && userData.size() > 0) {
            for (Data data : userData) {
                userId = data.getId();
                if (data.getFull_name() != null && !data.getFirst_name().equals("")) {
                    uNeme.setText(data.getFull_name());
                } else {
                    uNeme.setText(data.getFirst_name() + " " + data.getLast_name());
                }
                uemail.setText(data.getEmail());
                phone.setText(data.getMobile_number());
                Picasso.with(MyProfileFilterActivity.this).load(data.getProfile_image()).placeholder(R.drawable.userimage).resize(100, 100).into(profileImage);
            }
        }
    }

    //get post home details
    private void getMyHomeDetail() {
        SwitchDBHelper switchDBHelper = new SwitchDBHelper(MyProfileFilterActivity.this);

        ArrayList<MyhomeArray> myHomeList = switchDBHelper.getAllMyhomedata();
        for (MyhomeArray myhomeArray : myHomeList) {
            startDate = myhomeArray.getStartdate();
            endDate = myhomeArray.getEnddate();
            servercountryId = myhomeArray.getCountry_id();
            servercityId = myhomeArray.getCity_id();
            sleepsStr = myhomeArray.getSleeps();
            bedroomsStr = myhomeArray.getBedrooms();
            travleTypeId = myhomeArray.getProperty_type();
            genderStr = myhomeArray.getGender();
            religionStr = myhomeArray.getReligion();
            travlingDtae.setText(startDate);
            etYear.setText(startDate);
            enddate.setText(endDate);
            homeTitle.setText(myhomeArray.getTitle());
        }
        setCityAdapter(servercountryId);
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

        for (int i = 0; i < sleepsList.length; i++) {
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
        }
        for (int i = 0; i < travleList.length; i++) {
            if (travleTypeId.equals(travel[i].getId())) {
                Typeoftravellerspinner.setSelection(i);
                break;
            }
        }
        getCountrySelectPos();
        getCitySelectPos();
    }

    private void getCountrySelectPos() {
        int pos = 0;
        for (int i = 0; i < country.length; i++) {
            if (servercountryId.equals(country[i].getId())) {
                countryNameStr = String.valueOf(country[i].getName());
                pos = i;
                break;
            }
        }
        countryspinner.setSelection(pos);
        countryName.setText(countryNameStr);
    }

    private void getCitySelectPos() {
        int pos = 0;
        for (int i = 0; i < cityList.size() ; i++) {
            if (servercountryId.equals(city[i].getCountry_id()) && servercityId.equals(city[i].getId())) {
                cityNameStr = String.valueOf(city[i].getName());
                pos = i;
                break;
            }
        }
        cityspinner.setSelection(pos);
        cityName.setText(cityNameStr);
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
                    //addHomeServer();
                }
                break;
            case R.id.showAllfilter:
                moreFilterLayout.setVisibility(View.VISIBLE);
                break;
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
        final DatePickerDialog pickerDialog = new DatePickerDialog(MyProfileFilterActivity.this, date, myCalendar
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
        final DatePickerDialog pickerDialog = new DatePickerDialog(MyProfileFilterActivity.this, date, myCalendar
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
            Utility.showToast(MyProfileFilterActivity.this, "Please select Start Date!");
            return false;
        } else if (!isDateValid(enddateStr)) {
            Utility.showToast(MyProfileFilterActivity.this, "Please select End Date!");
            return false;
        } else if (countryId.equals("") && countryId.equals("0")) {
            Utility.showToast(MyProfileFilterActivity.this, "Please select County!");
            return false;
        } else if (cityId.equals("")) {
            Utility.showToast(MyProfileFilterActivity.this, "Please select City!");
            return false;
        }
        return true;
    }
}
