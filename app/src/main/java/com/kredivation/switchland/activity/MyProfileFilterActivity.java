package com.kredivation.switchland.activity;

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
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.kredivation.switchland.R;
import com.kredivation.switchland.database.SwitchDBHelper;
import com.kredivation.switchland.model.City;
import com.kredivation.switchland.model.Country;
import com.kredivation.switchland.model.Data;
import com.kredivation.switchland.model.ServiceContentData;
import com.kredivation.switchland.utilities.CompatibilityUtility;
import com.kredivation.switchland.utilities.FontManager;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class MyProfileFilterActivity extends AppCompatActivity {
    private Toolbar toolbar;
    private String userId;
    private City[] city;
    private Country[] country;
    Spinner cityspinner, countryspinner;
    String[] countryList;
    String[] cityList;
    private String cityId = "";
    private String countryId = "";
    ImageView profileImage;
    TextView uNeme, uemail, phone, cityName, countryName;

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
        setcountry();
    }

    private void setcountry() {
        SwitchDBHelper switchDBHelper = new SwitchDBHelper(MyProfileFilterActivity.this);
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
                    ArrayAdapter<String> countryAdapter = new ArrayAdapter<String>(MyProfileFilterActivity.this, R.layout.spinner_row, countryList);
                    countryspinner.setAdapter(countryAdapter);
                    countryspinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                        @Override
                        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                            countryId = country[position].getId();
                            city = MData.getCity();
                            if (city != null) {
                                cityList = new String[city.length];
                                for (int i = 0; i < city.length; i++) {
                                    if (countryId.equals(city[i].getCountry_id())) {
                                        cityList[i] = String.valueOf(city[i].getName());
                                    }
                                }
                                if (cityList != null && cityList.length > 0) {
                                    ArrayAdapter<String> cityAdapter = new ArrayAdapter<String>(MyProfileFilterActivity.this, R.layout.spinner_row, cityList);
                                    cityspinner.setAdapter(cityAdapter);
                                }
                            }
                        }

                        @Override
                        public void onNothingSelected(AdapterView<?> parent) {

                        }
                    });
                }
                cityspinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                        if (countryId.equals(city[position].getCountry_id())) {
                            cityId = city[position].getId();
                        }
                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> parent) {

                    }
                });
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
}
