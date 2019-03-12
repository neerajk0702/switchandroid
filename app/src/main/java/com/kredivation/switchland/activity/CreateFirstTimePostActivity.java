package com.kredivation.switchland.activity;

import android.app.DatePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.support.annotation.NonNull;
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
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.facebook.login.LoginManager;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.gson.Gson;
import com.kredivation.switchland.R;
import com.kredivation.switchland.database.SwitchDBHelper;
import com.kredivation.switchland.framework.FileUploaderHelper;
import com.kredivation.switchland.framework.IAsyncWorkCompletedCallback;
import com.kredivation.switchland.framework.ServiceCaller;
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

public class CreateFirstTimePostActivity extends AppCompatActivity implements View.OnClickListener, GoogleApiClient.OnConnectionFailedListener {


    Typeface materialdesignicons_font;
    TextView enddate, startDate;
    LinearLayout dateLayout, endDateLayout;
    private Spinner citySpinner, countrySpinner, travelcountrySpinner, travelcitySpinner;
    private TextInputLayout hno_layout;
    private EditText description, hno, title;
    private String descriptionStr, hnoStr, titleStr, startDateStr, enddateStr;
    private Button submit;
    private City[] city;
    private Country[] country;
    private Bedrooms[] bedroom;
    private Sleeps[] sleeps;
    String[] sleepsList;
    String[] bedList;
    String[] countryList;
    ArrayList<String> cityList;
    ArrayList<String> cityIdList;
    private String cityID = "";
    private String countryID = "";
    private String travelcountryID = "";
    private String travelcityID = "";
    private String userId;
    private int noBed = 0;
    private int nosleep = 0;
    private Toolbar toolbar;
    private ArrayList<String> travelcityList;
    private ArrayList<String> travelcityIdList;
    private GoogleApiClient mGoogleApiClient;
    TextView bedView, guestview;
    int bedminteger = 0;
    int guestminteger = 0;
    ImageView bedaddition, bedsubtraction, guestaddition, guestsubtraction;

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
        materialdesignicons_font = FontManager.getFontTypefaceMaterialDesignIcons(CreateFirstTimePostActivity.this, "fonts/materialdesignicons-webfont.otf");

        TextView starticon = (TextView) findViewById(R.id.starticon);
        starticon.setTypeface(materialdesignicons_font);
        starticon.setText(Html.fromHtml("&#xf0ed;"));
        TextView endIcon = (TextView) findViewById(R.id.endIcon);
        endIcon.setTypeface(materialdesignicons_font);
        endIcon.setText(Html.fromHtml("&#xf0ed;"));
        bedView = findViewById(R.id.qun);
        guestview = findViewById(R.id.guestview);
        bedaddition = findViewById(R.id.addition);
        bedsubtraction = findViewById(R.id.subtraction);
        bedaddition.setOnClickListener(this);
        bedsubtraction.setOnClickListener(this);
        guestaddition = findViewById(R.id.addition1);
        guestsubtraction = findViewById(R.id.subtraction1);
        guestaddition.setOnClickListener(this);
        guestsubtraction.setOnClickListener(this);

        dateLayout = findViewById(R.id.dateLayout);
        dateLayout.setOnClickListener(this);
        enddate = findViewById(R.id.enddate);
        startDate = findViewById(R.id.startDate);
        endDateLayout = findViewById(R.id.endDateLayout);
        endDateLayout.setOnClickListener(this);
        citySpinner = findViewById(R.id.citySpinner);
        countrySpinner = findViewById(R.id.countrySpinner);
        travelcitySpinner = findViewById(R.id.travelcitySpinner);
        travelcountrySpinner = findViewById(R.id.travelcountrySpinner);

        hno_layout = findViewById(R.id.hno_layout);
        description = findViewById(R.id.description);
        hno = findViewById(R.id.hno);
        title = findViewById(R.id.title);
        submit = findViewById(R.id.submit);
        dateLayout.setOnClickListener(this);
        submit.setOnClickListener(this);
        TextView logout = findViewById(R.id.logout);
        logout.setOnClickListener(this);

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
        //for gmail login
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();

        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .enableAutoManage(this, this)
                .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                .build();
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
               /* bedroom = MData.getBedrooms();
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
                });*/

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
                                cityList = new ArrayList();
                                cityIdList = new ArrayList();
                                for (int i = 0; i < city.length; i++) {
                                    if (countryID.equals(city[i].getCountry_id())) {
                                        cityList.add(String.valueOf(city[i].getName()));
                                        cityIdList.add(String.valueOf(city[i].getId()));
                                    }
                                }
                                if (cityList != null && cityList.size() > 0) {
                                    ArrayAdapter<String> cityAdapter = new ArrayAdapter<String>(CreateFirstTimePostActivity.this, R.layout.spinner_row, cityList);
                                    citySpinner.setAdapter(cityAdapter);
                                }
                            }
                        }

                        @Override
                        public void onNothingSelected(AdapterView<?> parent) {

                        }
                    });


                    ArrayAdapter<String> travelcountryAdapter = new ArrayAdapter<String>(CreateFirstTimePostActivity.this, R.layout.spinner_row, countryList);
                    travelcountrySpinner.setAdapter(travelcountryAdapter);
                    travelcountrySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                        @Override
                        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                            travelcountryID = country[position].getId();
                            city = MData.getCity();
                            if (city != null) {
                                travelcityList = new ArrayList();
                                travelcityIdList = new ArrayList();
                                for (int i = 0; i < city.length; i++) {
                                    if (travelcountryID.equals(city[i].getCountry_id())) {
                                        travelcityList.add(String.valueOf(city[i].getName()));
                                        travelcityIdList.add(String.valueOf(city[i].getId()));
                                    }
                                }
                                if (travelcityList != null && travelcityList.size() > 0) {
                                    ArrayAdapter<String> cityAdapter = new ArrayAdapter<String>(CreateFirstTimePostActivity.this, R.layout.spinner_row, travelcityList);
                                    travelcitySpinner.setAdapter(cityAdapter);
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
                cityID = cityIdList.get(position).toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        travelcitySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                travelcityID = travelcityIdList.get(position).toString();
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
                startDate.setText(sdf.format(myCalendar.getTime()));
                // datemilisec = myCalendar.getTimeInMillis();
            }
        };
        final DatePickerDialog pickerDialog = new DatePickerDialog(CreateFirstTimePostActivity.this, date, myCalendar
                .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                myCalendar.get(Calendar.DAY_OF_MONTH));
        pickerDialog.getDatePicker().setMinDate(System.currentTimeMillis() - 1000);
        pickerDialog.show();
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

    // ----validation -----
    private boolean isValidate() {
        String emailRegex = "[A-Z0-9a-z._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,4}";
        titleStr = title.getText().toString();
        hnoStr = hno.getText().toString();
        startDateStr = startDate.getText().toString();
        enddateStr = enddate.getText().toString();
        descriptionStr = description.getText().toString();
        if (titleStr.length() == 0) {
            Utility.showToast(CreateFirstTimePostActivity.this, "Please Enter Title!");
            requestFocus(title);
            return false;
        }/* else if (hnoStr.length() == 0) {
            hno_layout.setError("Please Enter House No");
            requestFocus(hno);
            return false;
        }*/ else if (descriptionStr.length() == 0) {
            Utility.showToast(CreateFirstTimePostActivity.this, "Please Enter Description!");
            requestFocus(description);
            return false;
        } else if (!isDateValid(startDateStr)) {
            Utility.showToast(CreateFirstTimePostActivity.this, "Please select Start Date!");
            return false;
        } else if (!isDateValid(enddateStr)) {
            Utility.showToast(CreateFirstTimePostActivity.this, "Please select End Date!");
            return false;
        } else if (countryID.equals("") && countryID.equals("0")) {
            Utility.showToast(CreateFirstTimePostActivity.this, "Please select Home County!");
            return false;
        } else if (cityID.equals("")) {
            Utility.showToast(CreateFirstTimePostActivity.this, "Please select Home City!");
            return false;
        } else if (travelcountryID.equals("") && travelcountryID.equals("0")) {
            Utility.showToast(CreateFirstTimePostActivity.this, "Please select Travel Country!");
            return false;
        } else if (travelcityID.equals("")) {
            Utility.showToast(CreateFirstTimePostActivity.this, "Please select Travel City!");
            return false;
        } else if (bedminteger == 0) {
            Utility.showToast(CreateFirstTimePostActivity.this, "Please select No Of Beds!");
            return false;
        } else if (guestminteger == 0) {
            Utility.showToast(CreateFirstTimePostActivity.this, "Please select No of Guests!");
            return false;
        } else {
            hno_layout.setErrorEnabled(false);
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
            case R.id.endDateLayout:
                setEndDate();
                break;
            case R.id.submit:
                if (isValidate()) {
                    addHomeServer();
                }
                break;
            case R.id.logout:
                SharedPreferences prefs = getSharedPreferences("FCMDeviceId", Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = prefs.edit();
                editor.putBoolean("Sendflag", true);//for send device token to server
                editor.commit();
                LoginManager.getInstance().logOut();
                signOut();
                Utility.showToast(CreateFirstTimePostActivity.this, "Logout Successfully");
                SwitchDBHelper switchDBHelper = new SwitchDBHelper(CreateFirstTimePostActivity.this);
                switchDBHelper.deleteAllRows("userInfo");
                Intent intent = new Intent(CreateFirstTimePostActivity.this, SplashScreenActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
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

    //gmail logout
    private void signOut() {
        if (mGoogleApiClient.isConnected()) {
            Auth.GoogleSignInApi.signOut(mGoogleApiClient);
            mGoogleApiClient.disconnect();
            mGoogleApiClient.connect();
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
            //payloadList.put("house_no", hnoStr);
            payloadList.put("user_id", userId);
            payloadList.put("startdate", startDateStr);
            payloadList.put("enddate", enddateStr);
            payloadList.put("city", cityID);
            payloadList.put("country", countryID);
            payloadList.put("travel_city", travelcityID);
            payloadList.put("travel_country", travelcountryID);
            payloadList.put("sleeps", String.valueOf(guestminteger));//nosleep
            payloadList.put("bedrooms", String.valueOf(bedminteger));//noBed
            MultipartBody.Builder multipartBody = setMultipartBodyVaule();
            FileUploaderHelper fileUploaderHelper = new FileUploaderHelper(CreateFirstTimePostActivity.this, payloadList, multipartBody, serviceURL) {
                @Override
                public void receiveData(String result) {
                    if (result != null) {
                        final ServiceContentData serviceData = new Gson().fromJson(result, ServiceContentData.class);
                        if (serviceData != null) {
                            if (serviceData.isSuccess()) {
                                getUserInfo();
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

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    private void getUserInfo() {
        final ASTProgressBar progressBar = new ASTProgressBar(CreateFirstTimePostActivity.this);
        progressBar.show();
        JSONObject object = new JSONObject();
        try {
            object.put("api_key", Contants.API_KEY);
            object.put("user_id", userId);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        String serviceURL = Contants.BASE_URL + Contants.Userinfo;

        ServiceCaller serviceCaller = new ServiceCaller(CreateFirstTimePostActivity.this);
        serviceCaller.CallCommanServiceMethod(serviceURL, object, "UserInfo", new IAsyncWorkCompletedCallback() {
            @Override
            public void onDone(String result, boolean isComplete) {
                if (isComplete) {
                    if (result != null) {
                        final ServiceContentData serviceData = new Gson().fromJson(result, ServiceContentData.class);
                        if (serviceData != null) {
                            if (serviceData.isSuccess()) {
                                if (serviceData.getData() != null) {
                                    SwitchDBHelper switchDBHelper = new SwitchDBHelper(CreateFirstTimePostActivity.this);
                                    switchDBHelper.upsertUserInfoData(serviceData.getData(), serviceData.getIs_home_available());

                                }
                            }
                        }
                    }
                    if (progressBar.isShowing()) {
                        progressBar.dismiss();
                    }
                    Utility.showToast(CreateFirstTimePostActivity.this, "Home Added Successfully");
                    startActivity(new Intent(CreateFirstTimePostActivity.this, SplashScreenActivity.class));
                } else {
                    if (progressBar.isShowing()) {
                        progressBar.dismiss();
                    }
                    Utility.alertForErrorMessage(Contants.Error, CreateFirstTimePostActivity.this);
                }
            }
        });
    }

}
