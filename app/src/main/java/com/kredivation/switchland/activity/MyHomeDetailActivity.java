package com.kredivation.switchland.activity;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Handler;

import androidx.viewpager.widget.ViewPager;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.appcompat.widget.Toolbar;
import android.text.Html;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.kredivation.switchland.R;
import com.kredivation.switchland.adapters.SlidingImage_Adapter_For_ItemDetails;
import com.kredivation.switchland.database.SwitchDBHelper;
import com.kredivation.switchland.framework.IAsyncWorkCompletedCallback;
import com.kredivation.switchland.framework.ServiceCaller;
import com.kredivation.switchland.model.Data;
import com.kredivation.switchland.model.HomeDetails;
import com.kredivation.switchland.model.Home_features;
import com.kredivation.switchland.model.Home_rules;
import com.kredivation.switchland.model.Homegallery;
import com.kredivation.switchland.utilities.ASTProgressBar;
import com.kredivation.switchland.utilities.CompatibilityUtility;
import com.kredivation.switchland.utilities.Contants;
import com.kredivation.switchland.utilities.FontManager;
import com.kredivation.switchland.utilities.Utility;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

public class MyHomeDetailActivity extends AppCompatActivity implements View.OnClickListener {
    private Toolbar toolbar;
    private String userId;
    ASTProgressBar dotDialog;
    String HomeId;
    boolean EditFlage = false;
    private ViewPager mPager;
    private int currentPage = 0;
    private int NUM_PAGES = 0;
    TextView Title, Bedrooms, Beds, Bathroom;
    String[] hImageName;
    HomeDetails details;
    String username;
    String emailStr, phoneStr;
    ArrayList<Homegallery> hImagList;
    TextView locationview;
    String imageArray;
    TextView enddate, startdate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_home_detail);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if (CompatibilityUtility.isTablet(MyHomeDetailActivity.this)) {
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        } else {
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        }
        init();
    }

    private void init() {
        HomeId = getIntent().getStringExtra("HomeId");
        EditFlage = getIntent().getBooleanExtra("EditFlage", false);
        Typeface materialdesignicons_font = FontManager.getFontTypefaceMaterialDesignIcons(this, "fonts/materialdesignicons-webfont.otf");
        TextView back = toolbar.findViewById(R.id.back);
        back.setTypeface(materialdesignicons_font);
        back.setText(Html.fromHtml("&#xf30d;"));
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        enddate = findViewById(R.id.enddate);
        startdate = findViewById(R.id.startdate);
        Button edithome = findViewById(R.id.edithome);
        edithome.setOnClickListener(this);
        Button travel = findViewById(R.id.travel);
        travel.setOnClickListener(this);
        locationview = findViewById(R.id.locationview);
        Title = findViewById(R.id.Title);
        Bedrooms = findViewById(R.id.Bedrooms);
        Beds = findViewById(R.id.Beds);
        Bathroom = findViewById(R.id.Bathroom);
        hImagList = new ArrayList<>();
        details = new HomeDetails();
        getUserData();
    }

    private void getUserData() {
        SwitchDBHelper switchDBHelper = new SwitchDBHelper(MyHomeDetailActivity.this);
        ArrayList<Data> userData = switchDBHelper.getAllUserInfoList();
        if (userData != null && userData.size() > 0) {
            for (Data data : userData) {
                userId = data.getId();
                if (data.getFirst_name() != null) {
                    username = data.getFirst_name();
                } else {
                    username = data.getFirst_name() + ", " + data.getLast_name();
                }
                emailStr = data.getEmail();
                phoneStr = data.getMobile_number();
            }
            getHomeDetail();
        }
    }

    //get home detail
    private void getHomeDetail() {
        if (Utility.isOnline(MyHomeDetailActivity.this)) {
            dotDialog = new ASTProgressBar(MyHomeDetailActivity.this);
            dotDialog.show();
            JSONObject object = new JSONObject();
            try {
                object.put("api_key", Contants.API_KEY);
                object.put("user_id", userId);
                object.put("home_id", HomeId);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            String serviceURL = Contants.BASE_URL + Contants.Homedetails;

            ServiceCaller serviceCaller = new ServiceCaller(MyHomeDetailActivity.this);
            serviceCaller.CallCommanServiceMethod(serviceURL, object, "getHomeDetail", new IAsyncWorkCompletedCallback() {
                @Override
                public void onDone(String result, boolean isComplete) {
                    if (isComplete) {
                        parseAllHomeServiceData(result);
                    } else {
                        if (dotDialog.isShowing()) {
                            dotDialog.dismiss();
                        }
                        Utility.alertForErrorMessage(Contants.Error, MyHomeDetailActivity.this);
                        finish();
                    }
                }
            });
        } else {
            Utility.alertForErrorMessage(Contants.OFFLINE_MESSAGE, MyHomeDetailActivity.this);//off line msg....
        }
    }

    public void parseAllHomeServiceData(final String result) {
        if (result != null) {

            new AsyncTask<Void, Void, Boolean>() {
                @Override
                protected Boolean doInBackground(Void... voids) {
                    Boolean flag = false;

                    try {

                        JSONObject jsonRootObject = new JSONObject(result);
                        boolean jsonStatus = jsonRootObject.optBoolean("success");
                        if (jsonStatus) {
                            JSONObject databject = jsonRootObject.getJSONObject("data");
                            if (databject != null) {
                                String bathrooms = databject.optString("bathrooms").toString();
                                details.setBathrooms(bathrooms);
                                String bedrooms = databject.optString("bedrooms").toString();
                                details.setBedrooms(bedrooms);
                                String sleeps = databject.optString("sleeps").toString();
                                details.setSleeps(sleeps);
                                String title = databject.optString("title").toString();
                                details.setTitle(title);
                                String sort_description = databject.optString("sort_description").toString();
                                details.setSort_description(sort_description);
                                String profile_image = databject.optString("profile_image").toString();
                                details.setProfile_image(profile_image);
                                String city_name = databject.optString("city_name").toString();
                                details.setCity_name(city_name);
                                String country_name = databject.optString("country_name").toString();
                                details.setCountry_name(country_name);
                                String startdate = databject.optString("startdate").toString();
                                details.setStartdate(startdate);
                                String enddate = databject.optString("enddate").toString();
                                details.setEnddate(enddate);
                                String travel_country = databject.optString("travel_country").toString();
                                details.setTravel_country(travel_country);
                                String travel_city = databject.optString("travel_city").toString();
                                details.setTravel_city(travel_city);
                                String id = databject.optString("id").toString();
                                details.setId(id);

                                String user_id = databject.optString("user_id").toString();
                                details.setUser_id(user_id);
                                String home_type = databject.optString("home_type").toString();
                                details.setHome_type(home_type);
                                String property_type = databject.optString("property_type").toString();
                                details.setProperty_type(property_type);
                                String pets = databject.optString("pets").toString();
                                details.setPets(pets);
                                String family_matters = databject.optString("family_matters").toString();
                                details.setFamily_matters(family_matters);
                                String house_no = databject.optString("house_no").toString();
                                details.setHouse_no(house_no);
                                // String location = databject.optString("location").toString();
                                // details.setLocation(location);
                                String latitude = databject.optString("latitude").toString();
                                details.setLatitude(latitude);
                                String longitude = databject.optString("longitude").toString();
                                details.setLongitude(longitude);
                                String destinations = databject.optString("destinations").toString();
                                details.setDestinations(destinations);
                                String traveller_type = databject.optString("traveller_type").toString();
                                details.setTraveller_type(traveller_type);
                                String travelling_anywhere = databject.optString("travelling_anywhere").toString();
                                details.setTravelling_anywhere(travelling_anywhere);
                                String country_id = databject.optString("country_id").toString();
                                details.setCountry_id(country_id);
                                String city_id = databject.optString("city_id").toString();
                                details.setCity_id(city_id);
                                String address1 = databject.optString("address1").toString();
                                details.setAddress1(address1);
                                String address2 = databject.optString("address2").toString();
                                details.setAddress2(address2);
                                String zipcode = databject.optString("zipcode").toString();
                                details.setZipcode(zipcode);
                                String gender = databject.optString("gender").toString();
                                details.setGender(gender);
                                String religion = databject.optString("religion").toString();
                                details.setReligion(religion);
                                String landmarks = databject.optString("landmarks").toString();
                                details.setLandmarks(landmarks);
                                String level_security = databject.optString("level_security").toString();
                                details.setLevel_security(level_security);
                                String profile_completeness = databject.optString("profile_completeness").toString();
                                details.setProfile_completeness(profile_completeness);
                                String status = databject.optString("status").toString();
                                details.setStatus(status);
                                String added_date = databject.optString("added_date").toString();
                                details.setAdded_date(added_date);
                                String updated_date = databject.optString("updated_date").toString();
                                details.setUpdated_date(updated_date);
                                String family = databject.optString("family").toString();
                                details.setFamily(family);
                                String homestyle = databject.optString("homestyle").toString();
                                details.setHomestyle(homestyle);
                                String propertytype = databject.optString("propertytype").toString();
                                details.setPropertytype(propertytype);
                                String travellertype = databject.optString("travellertype").toString();
                                details.setTravellertype(travellertype);
                                String petsallowed = databject.optString("petsallowed").toString();
                                details.setPetsallowed(petsallowed);
                                String cardnumber = databject.optString("cardnumber").toString();
                                details.setCardnumber(cardnumber);
                                String nameoncard = databject.optString("nameoncard").toString();
                                details.setNameoncard(nameoncard);
                                String month = databject.optString("month").toString();
                                details.setMonth(month);
                                String year = databject.optString("year").toString();
                                details.setYear(year);
                                String cvv = databject.optString("cvv").toString();
                                details.setCvv(cvv);
                                String travel_city_name = databject.optString("travel_city_name").toString();
                                details.setTravel_country_name(travel_city_name);
                                String travel_country_name = databject.optString("travel_country_name").toString();
                                details.setTravel_city_name(travel_country_name);

                                JSONArray home_featuresAttay = databject.optJSONArray("home_features");
                                if (home_featuresAttay != null) {
                                    ArrayList<Home_features> featureList=new ArrayList<>();
                                    for (int i = 0; i < home_featuresAttay.length(); i++) {
                                        JSONObject featureObject = home_featuresAttay.getJSONObject(i);
                                        String featireid = featureObject.optString("id").toString();
                                        String featurename = featureObject.optString("name").toString();
                                        String featurestatus = featureObject.optString("status").toString();
                                        Home_features home_features = new Home_features();
                                        home_features.setId(featireid);
                                        home_features.setName(featurename);
                                        home_features.setStatus(featurestatus);
                                        featureList.add(home_features);
                                    }
                                    details.setFeatureList(featureList);
                                }

                                JSONArray home_rulesAttay = databject.optJSONArray("home_rules");
                                if (home_rulesAttay != null) {
                                    ArrayList<Home_rules> hRuleList= new ArrayList<>();
                                    for (int i = 0; i < home_rulesAttay.length(); i++) {
                                        JSONObject ruleObject = home_rulesAttay.getJSONObject(i);
                                        String featireid = ruleObject.optString("id").toString();
                                        String featurename = ruleObject.optString("name").toString();
                                        String featurestatus = ruleObject.optString("status").toString();
                                        Home_rules home_rule = new Home_rules();
                                        home_rule.setId(featireid);
                                        home_rule.setName(featurename);
                                        home_rule.setStatus(featurestatus);
                                        hRuleList.add(home_rule);
                                    }
                                    details.setHouseRuleList(hRuleList);
                                }
                                JSONArray homegalleryAttay = databject.optJSONArray("homegallery");

                                if (homegalleryAttay != null) {
                                    imageArray = homegalleryAttay.toString();
                                    hImageName = new String[homegalleryAttay.length()];
                                    for (int i = 0; i < homegalleryAttay.length(); i++) {
                                        JSONObject homeObject = homegalleryAttay.getJSONObject(i);
                                        String hid = homeObject.optString("id").toString();
                                        String homeid = homeObject.optString("home_id").toString();
                                        String photo = homeObject.optString("photo").toString();

                                        Homegallery home_image = new Homegallery();
                                        home_image.setId(hid);
                                        home_image.setHome_id(homeid);
                                        home_image.setPhoto(photo);
                                        hImagList.add(home_image);
                                        hImageName[i] = photo;
                                    }
                                    details.setHomeImageList(hImagList);
                                }
                            }
                            flag = true;
                        }
                    } catch (JSONException e) {
                        //  e.printStackTrace();
                    }

                    return flag;
                }

                @Override
                protected void onPostExecute(Boolean flag) {
                    super.onPostExecute(flag);
                    if (flag) {
                        setvalue(result);
                    } else {
                        Utility.showToast(MyHomeDetailActivity.this, "Home Detail Not available!");
                        finish();
                    }
                    if (dotDialog.isShowing()) {
                        dotDialog.dismiss();
                    }
                }
            }.execute();
        }
    }

    private void setvalue(String result) {
        Title.setText(details.getTitle());
        Bedrooms.setText(details.getBedrooms() + " BedRooms");
        Beds.setText(details.getBedrooms() + " Guest");
        Bathroom.setText(details.getBathrooms() + " Bathrooms");
        locationview.setText(details.getCity_name() + "," + details.getCountry_name());
        startdate.setText(details.getStartdate() + "");
        enddate.setText(details.getEnddate() + "");
        if (EditFlage) {
          /*  uNeme.setText(username);
            uemail.setText(emailStr);
            phone.setText(phoneStr);*/
        }
        slideimage();
    }

    private void slideimage() {

        mPager = (ViewPager) findViewById(R.id.pager);
        if (hImagList != null && hImagList.size() > 0) {
            mPager.setAdapter(new SlidingImage_Adapter_For_ItemDetails(MyHomeDetailActivity.this, hImagList));



       /* CirclePageIndicator indicator = (CirclePageIndicator) findViewById(R.id.indicator);
        indicator.setViewPager(mPager);
        final float density = getResources().getDisplayMetrics().density;
        //Set circle indicator radius
        indicator.setRadius(5 * density);*/

            NUM_PAGES = hImagList.size();

            // Auto start of viewpager
            final Handler handler = new Handler();
            final Runnable Update = new Runnable() {
                public void run() {
                    if (currentPage == NUM_PAGES) {
                        currentPage = 0;
                    }
                    mPager.setCurrentItem(currentPage++, true);
                }
            };
            Timer swipeTimer = new Timer();
            swipeTimer.schedule(new TimerTask() {
                @Override
                public void run() {
                    handler.post(Update);
                }
            }, 3000, 3000);

            // Pager listener over indicator
      /*  indicator.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {

            @Override
            public void onPageSelected(int position) {
                currentPage = position;

            }

            @Override
            public void onPageScrolled(int pos, float arg1, int arg2) {

            }

            @Override
            public void onPageScrollStateChanged(int pos) {

            }
        });
*/
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.edithome:
                if (details != null) {
                    Intent homeintent = new Intent(MyHomeDetailActivity.this, AddHomeActivity.class);
                   /* String homeStr = new Gson().toJson(details);
                    Utility.setHomeDetail(MyHomeDetailActivity.this, homeStr, true);*/
                    SwitchDBHelper dbHelper = new SwitchDBHelper(MyHomeDetailActivity.this);
                    dbHelper.deleteAllRows("AddEditHomeData");
                    dbHelper.insertAddEditHomeData(details);
                    startActivity(homeintent);
                }
                break;
            case R.id.travel:
                SwitchDBHelper dbHelper = new SwitchDBHelper(MyHomeDetailActivity.this);
                Intent trintent = new Intent(MyHomeDetailActivity.this, TravelRoutineActivity.class);
                dbHelper.deleteAllRows("AddEditHomeData");
                trintent.putExtra("StartDate", details.getStartdate());
                trintent.putExtra("EndDate", details.getEnddate());
                trintent.putExtra("CountryId", details.getTravel_country());
                trintent.putExtra("CityId", details.getTravel_city());
                trintent.putExtra("HomeId", details.getId());
                trintent.putExtra("MyHomeAdapterFlage", true);
                startActivity(trintent);
                break;
        }
    }
}
