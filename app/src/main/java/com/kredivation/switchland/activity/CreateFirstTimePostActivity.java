package com.kredivation.switchland.activity;

import android.app.DatePickerDialog;
import android.content.Context;
import android.content.Intent;
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
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.google.gson.Gson;
import com.kredivation.switchland.R;
import com.kredivation.switchland.database.SwitchDBHelper;
import com.kredivation.switchland.framework.FileUploaderHelper;
import com.kredivation.switchland.model.Bedrooms;
import com.kredivation.switchland.model.City;
import com.kredivation.switchland.model.Country;
import com.kredivation.switchland.model.Data;
import com.kredivation.switchland.model.ServiceContentData;
import com.kredivation.switchland.model.Sleeps;
import com.kredivation.switchland.utilities.ASTProgressBar;
import com.kredivation.switchland.utilities.CompatibilityUtility;
import com.kredivation.switchland.utilities.Contants;
import com.kredivation.switchland.utilities.FontManager;
import com.kredivation.switchland.utilities.Utility;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Locale;

import okhttp3.MediaType;
import okhttp3.MultipartBody;

public class CreateFirstTimePostActivity extends AppCompatActivity implements View.OnClickListener {


    Typeface materialdesignicons_font;
    TextView etYear, enddate;
    LinearLayout dateLayout, endDateLayout;
    private Spinner noOfBedSpinner, noOfGuestSpinner, citySpinner, countrySpinner;
    private TextInputLayout des_layout, hno_layout, title_layout;
    private EditText description, hno, title;
    private String descriptionStr, hnoStr, titleStr, startDateStr, enddateStr;
    private Button submit;
    TextView edateIcon;
    private City[] city;
    private Country[] country;
    private Bedrooms[] bedroom;
    private Sleeps[] sleeps;
    String[] sleepsList;
    String[] bedList;
    String[] countryList;
    String[] cityList;
    private String cityID = "";
    private String countryID = "";
    private String userId;
    private int noBed = 0;
    private int nosleep = 0;
    private Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_create_first_time_post);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if (CompatibilityUtility.isTablet(CreateFirstTimePostActivity.this)) {
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        } else {
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        }
        init();
    }


    private void init() {
        TextView dateIcon = findViewById(R.id.dateIcon);
        materialdesignicons_font = FontManager.getFontTypefaceMaterialDesignIcons(CreateFirstTimePostActivity.this, "fonts/materialdesignicons-webfont.otf");
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
        noOfBedSpinner = findViewById(R.id.noOfBedSpinner);
        noOfGuestSpinner = findViewById(R.id.noOfGuestSpinner);
        citySpinner = findViewById(R.id.citySpinner);
        countrySpinner = findViewById(R.id.countrySpinner);

        des_layout = findViewById(R.id.des_layout);
        hno_layout = findViewById(R.id.hno_layout);
        title_layout = findViewById(R.id.title_layout);
        description = findViewById(R.id.description);
        hno = findViewById(R.id.hno);
        title = findViewById(R.id.title);
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
        setValue();
    }

    private void setValue() {
        SwitchDBHelper switchDBHelper = new SwitchDBHelper(CreateFirstTimePostActivity.this);
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
                bedroom = MData.getBedrooms();
                if (bedroom != null) {
                    bedList = new String[bedroom.length];
                    for (int i = 0; i < bedroom.length; i++) {
                        bedList[i] = String.valueOf(bedroom[i].getName());
                    }
                    ArrayAdapter<String> bankAdapter = new ArrayAdapter<String>(CreateFirstTimePostActivity.this, R.layout.spinner_row, bedList);
                    noOfBedSpinner.setAdapter(bankAdapter);
                    ArrayAdapter<String> sleepAdapter = new ArrayAdapter<String>(CreateFirstTimePostActivity.this, R.layout.spinner_row, bedList);
                    noOfGuestSpinner.setAdapter(sleepAdapter);
                }
                noOfBedSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                        noBed = bedroom[position].getId();
                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> parent) {

                    }
                });
                sleeps = MData.getSleeps();
                if (sleeps != null) {
                    sleepsList = new String[sleeps.length];
                    for (int i = 0; i < sleeps.length; i++) {
                        sleepsList[i] = String.valueOf(sleeps[i].getName());
                    }
                    ArrayAdapter<String> sleepAdapter = new ArrayAdapter<String>(CreateFirstTimePostActivity.this, R.layout.spinner_row, sleepsList);
                    noOfGuestSpinner.setAdapter(sleepAdapter);
                }
                noOfGuestSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                        nosleep = sleeps[position].getId();
                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> parent) {

                    }
                });

                country = MData.getCountry();
                if (country != null) {
                    countryList = new String[country.length];
                    for (int i = 0; i < country.length; i++) {
                        countryList[i] = String.valueOf(country[i].getName());
                    }
                    ArrayAdapter<String> countryAdapter = new ArrayAdapter<String>(CreateFirstTimePostActivity.this, R.layout.spinner_row, countryList);
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
                                    ArrayAdapter<String> cityAdapter = new ArrayAdapter<String>(CreateFirstTimePostActivity.this, R.layout.spinner_row, cityList);
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
        final DatePickerDialog pickerDialog = new DatePickerDialog(CreateFirstTimePostActivity.this, date, myCalendar
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
        final DatePickerDialog pickerDialog = new DatePickerDialog(CreateFirstTimePostActivity.this, date, myCalendar
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
        String emailRegex = "[A-Z0-9a-z._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,4}";
        titleStr = title.getText().toString();
        hnoStr = hno.getText().toString();
        startDateStr = etYear.getText().toString();
        enddateStr = enddate.getText().toString();
        descriptionStr = description.getText().toString();
        if (titleStr.length() == 0) {
            title_layout.setError("Please Enter Title");
            requestFocus(title);
            return false;
        } else if (hnoStr.length() == 0) {
            hno_layout.setError("Please Enter House No");
            requestFocus(hno);
            return false;
        } else if (descriptionStr.length() == 0) {
            des_layout.setError("Please Enter Description");
            requestFocus(description);
            return false;
        } else if (!isDateValid(startDateStr)) {
            Utility.showToast(CreateFirstTimePostActivity.this, "Please select Start Date!");
            return false;
        } else if (!isDateValid(enddateStr)) {
            Utility.showToast(CreateFirstTimePostActivity.this, "Please select End Date!");
            return false;
        } else if (countryID.equals("") && countryID.equals("0")) {
            Utility.showToast(CreateFirstTimePostActivity.this, "Please select County!");
            return false;
        } else if (cityID.equals("")) {
            Utility.showToast(CreateFirstTimePostActivity.this, "Please select City!");
            return false;
        } else if (noBed == 0) {
            Utility.showToast(CreateFirstTimePostActivity.this, "Please select No Of Beds!");
            return false;
        } else if (nosleep == 0) {
            Utility.showToast(CreateFirstTimePostActivity.this, "Please select No of Guests!");
            return false;
        } else {
            title_layout.setErrorEnabled(false);
            hno_layout.setErrorEnabled(false);
            des_layout.setErrorEnabled(false);
        }
        return true;
    }

    private void requestFocus(View view) {
        if (view.requestFocus()) {
            getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
        }
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

    public void addHomeServer() {
        if (Utility.isOnline(CreateFirstTimePostActivity.this)) {
            final ASTProgressBar progressBar = new ASTProgressBar(CreateFirstTimePostActivity.this);
            progressBar.show();
            String serviceURL = Contants.BASE_URL + Contants.Addhome;
            HashMap<String, String> payloadList = new HashMap<String, String>();
            payloadList.put("api_key", Contants.API_KEY);
            payloadList.put("title", titleStr);
            payloadList.put("sort_description", descriptionStr);
            payloadList.put("house_no", hnoStr);
            payloadList.put("user_id", userId);
            payloadList.put("startdate", startDateStr);
            payloadList.put("enddate", enddateStr);
            payloadList.put("city", cityID);
            payloadList.put("country", countryID);
            payloadList.put("sleeps", String.valueOf(nosleep));
            payloadList.put("bedrooms", String.valueOf(noBed));
            MultipartBody.Builder multipartBody = setMultipartBodyVaule();
            FileUploaderHelper fileUploaderHelper = new FileUploaderHelper(CreateFirstTimePostActivity.this, payloadList, multipartBody, serviceURL) {
                @Override
                public void receiveData(String result) {
                    if (result != null) {
                        final ServiceContentData serviceData = new Gson().fromJson(result, ServiceContentData.class);
                        if (serviceData != null) {
                            if (serviceData.isSuccess()) {
                                Utility.showToast(CreateFirstTimePostActivity.this, serviceData.getMsg());
                                startActivity(new Intent(CreateFirstTimePostActivity.this,SplashScreenActivity.class));
                            } else {
                                Utility.showToast(CreateFirstTimePostActivity.this, serviceData.getMsg());
                            }
                        } else {
                            Utility.showToast(CreateFirstTimePostActivity.this, "Server Side error!");
                        }
                    } else {
                        Utility.showToast(CreateFirstTimePostActivity.this, "Server Side error!");
                    }
                    if (progressBar.isShowing()) {
                        progressBar.dismiss();
                    }
                }
            };
            fileUploaderHelper.execute();
        } else {
            Utility.alertForErrorMessage(Contants.OFFLINE_MESSAGE, CreateFirstTimePostActivity.this);//off line msg....
        }

    }

    //add pm install images into MultipartBody for send as multipart
    private MultipartBody.Builder setMultipartBodyVaule() {
        final MediaType MEDIA_TYPE_PNG = MediaType.parse("image/jpg");
        MultipartBody.Builder multipartBody = new MultipartBody.Builder().setType(MultipartBody.FORM);
      /*  if (equpImagList != null && equpImagList.size() > 0) {
            for (SaveOffLineData data : equpImagList) {
                if (data != null) {
                    if (data.getImagePath() != null) {
                        File inputFile = new File(data.getImagePath());
                        if (inputFile.exists()) {
                            multipartBody.addFormDataPart("PMInstalEqupImages", data.getImageName(), RequestBody.create(MEDIA_TYPE_PNG, inputFile));
                        }
                    }
                }
            }
        }
*/
        return multipartBody;
    }
}
