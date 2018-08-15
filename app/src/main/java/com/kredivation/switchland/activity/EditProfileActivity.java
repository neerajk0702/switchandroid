package com.kredivation.switchland.activity;

import android.content.pm.ActivityInfo;
import android.graphics.Typeface;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import com.kredivation.switchland.R;
import com.kredivation.switchland.database.SwitchDBHelper;
import com.kredivation.switchland.model.City;
import com.kredivation.switchland.model.Country;
import com.kredivation.switchland.model.Data;
import com.kredivation.switchland.model.ServiceContentData;
import com.kredivation.switchland.utilities.CompatibilityUtility;
import com.kredivation.switchland.utilities.FontManager;
import com.kredivation.switchland.utilities.Utility;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class EditProfileActivity extends AppCompatActivity implements View.OnClickListener {
    private Toolbar toolbar;
    private Spinner citySpinner, countrySpinner;
    String[] countryList;
    String[] cityList;
    private String cityID = "";
    private String countryID = "";
    private String userId;
    private City[] city;
    private Country[] country;
    private TextInputLayout phone_layout, hno_layout, first_layout, last_layout, area_layout, zip_layout;
    private EditText phone, hno, lastName, firstName, area, zipcode;
    TextView user, email;
    private String userStr, emailStr, passwordStr, cpasswordStr, firstNameStr, lastNameStr;
    ImageView proImage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if (CompatibilityUtility.isTablet(EditProfileActivity.this)) {
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        } else {
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        }
        init();
    }

    private void init() {
        Typeface materialdesignicons_font = FontManager.getFontTypefaceMaterialDesignIcons(this, "fonts/materialdesignicons-webfont.otf");
        TextView back = (TextView) toolbar.findViewById(R.id.back);
        back.setTypeface(materialdesignicons_font);
        back.setText(Html.fromHtml("&#xf30d;"));
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        user = findViewById(R.id.user);
        email = findViewById(R.id.email);

        hno_layout = findViewById(R.id.hno_layout);
        first_layout = findViewById(R.id.first_layout);
        last_layout = findViewById(R.id.last_layout);
        area_layout = findViewById(R.id.area_layout);
        zip_layout = findViewById(R.id.zip_layout);
        phone = findViewById(R.id.phone);
        hno = findViewById(R.id.hno);
        lastName = findViewById(R.id.lastName);
        firstName = findViewById(R.id.firstName);
        area = findViewById(R.id.area);
        zipcode = findViewById(R.id.zipcode);
        Button submit = findViewById(R.id.submit);
        submit.setOnClickListener(this);
        proImage = findViewById(R.id.proImage);
        SwitchDBHelper switchDBHelper = new SwitchDBHelper(EditProfileActivity.this);
        ArrayList<Data> userData = switchDBHelper.getAllUserInfoList();
        if (userData != null && userData.size() > 0) {
            for (Data data : userData) {
                userId = data.getId();
                firstName.setText(data.getFirst_name());
                lastName.setText(data.getLast_name());
                email.setText(data.getEmail());
                phone.setText(data.getMobile_number());
                Picasso.with(EditProfileActivity.this).load(data.getProfile_image()).placeholder(R.drawable.pimage).resize(80, 80).into(proImage);
            }
        }

        citySpinner = findViewById(R.id.citySpinner);
        countrySpinner = findViewById(R.id.countrySpinner);
        setcountry();
    }

    private void setcountry() {
        SwitchDBHelper switchDBHelper = new SwitchDBHelper(EditProfileActivity.this);
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
                    ArrayAdapter<String> countryAdapter = new ArrayAdapter<String>(EditProfileActivity.this, R.layout.spinner_row, countryList);
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
                                    ArrayAdapter<String> cityAdapter = new ArrayAdapter<String>(EditProfileActivity.this, R.layout.spinner_row, cityList);
                                    citySpinner.setAdapter(cityAdapter);
                                }
                            }
                        }

                        @Override
                        public void onNothingSelected(AdapterView<?> parent) {

                        }
                    });
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
        }
    }

   /* // ----validation -----
    private boolean isValidate() {
        String emailRegex = "[A-Z0-9a-z._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,4}";
        passwordStr = password.getText().toString();
        newpasswordStr = newpassword.getText().toString();
        renewpasswordStr = renewpassword.getText().toString();

        if (passwordStr.length() == 0) {
            password_layout.setError("Please Enter your Old Password");
            requestFocus(password);
            return false;
        } else if (newpasswordStr.length() == 0) {
            newpassword_layout.setError("Please Enter New Password");
            requestFocus(newpassword);
            return false;
        } else if (renewpassword.length() == 0) {
            renewpassword_layout.setError("Please Enter Confirm Password");
            requestFocus(renewpassword);
            return false;
        } else if (!renewpassword.equals(newpasswordStr)) {
            renewpassword_layout.setError("New Password and Confirm password not matched!");
            requestFocus(renewpassword);
            return false;
        } else if (countryID.equals("") && countryID.equals("0")) {
            Utility.showToast(EditProfileActivity.this, "Please select County!");
            return false;
        } else if (cityID.equals("")) {
            Utility.showToast(EditProfileActivity.this, "Please select City!");
            return false;
        } else {
            password_layout.setErrorEnabled(false);
            newpassword_layout.setErrorEnabled(false);
            renewpassword_layout.setErrorEnabled(false);
        }
        return true;
    }*/

    private void requestFocus(View view) {
        if (view.requestFocus()) {
            getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.submit:
               /* if (isValidate()) {
                    changePassword();
                }*/
                break;
        }
    }
}
