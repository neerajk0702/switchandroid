package com.kredivation.switchland.activity;

import android.Manifest;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Handler;
import android.support.v4.app.ActivityCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.gson.Gson;
import com.kredivation.switchland.R;
import com.kredivation.switchland.adapters.HomeDetailFeatureAdapter;
import com.kredivation.switchland.adapters.HomeDetailRuleAdapter;
import com.kredivation.switchland.adapters.MyChoicesAdapter;
import com.kredivation.switchland.adapters.MyHomeAdapter;
import com.kredivation.switchland.adapters.SlidingImage_Adapter_For_ItemDetails;
import com.kredivation.switchland.database.SwitchDBHelper;
import com.kredivation.switchland.framework.IAsyncWorkCompletedCallback;
import com.kredivation.switchland.framework.ServiceCaller;
import com.kredivation.switchland.model.Data;
import com.kredivation.switchland.model.HomeDetails;
import com.kredivation.switchland.model.Home_features;
import com.kredivation.switchland.model.Home_rules;
import com.kredivation.switchland.model.Homegallery;
import com.kredivation.switchland.model.ServiceContentData;
import com.kredivation.switchland.utilities.ASTProgressBar;
import com.kredivation.switchland.utilities.CompatibilityUtility;
import com.kredivation.switchland.utilities.Contants;
import com.kredivation.switchland.utilities.FontManager;
import com.kredivation.switchland.utilities.Utility;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

public class HomeDetailActivity extends AppCompatActivity implements View.OnClickListener, OnMapReadyCallback {
    private Toolbar toolbar;
    private String userId;
    ASTProgressBar dotDialog;
    String HomeId;
    boolean EditFlage = false;
    private static ViewPager mPager;
    private static int currentPage = 0;
    private static int NUM_PAGES = 0;
    private RecyclerView amenitiesrecycler_view, houseRulesrecycler_view;
    TextView Title, description, gender, religion, securityLevel, Homestyle, Propertytype, PetAllowed, FamilyMatters, Bedrooms, Beds, Bathroom, HouseNo, uNeme, uemail, phone;
    ImageView profileImage;
    Button edit;
    private GoogleMap mMap;
    String[] hImageName;
    String[] featureName;
    String[] hRuleName;
    HomeDetails details;
    String username;
    String emailStr, phoneStr;
    ArrayList<Home_features> featureList;
    ArrayList<Home_rules> hRuleList;
    ArrayList<Homegallery> hImagList;
    private String SenderUserId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_detail);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if (CompatibilityUtility.isTablet(HomeDetailActivity.this)) {
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
        SwitchDBHelper switchDBHelper = new SwitchDBHelper(HomeDetailActivity.this);
        //myHomeList = switchDBHelper.getAllMyhomedata();
        amenitiesrecycler_view = findViewById(R.id.amenitiesrecycler_view);
        houseRulesrecycler_view = findViewById(R.id.houseRulesrecycler_view);
        edit = findViewById(R.id.edit);
        edit.setOnClickListener(this);
        Title = findViewById(R.id.Title);
        description = findViewById(R.id.description);
        gender = findViewById(R.id.gender);
        religion = findViewById(R.id.religion);
        securityLevel = findViewById(R.id.securityLevel);
        Homestyle = findViewById(R.id.Homestyle);
        Propertytype = findViewById(R.id.Propertytype);
        PetAllowed = findViewById(R.id.PetAllowed);
        FamilyMatters = findViewById(R.id.FamilyMatters);
        Bedrooms = findViewById(R.id.Bedrooms);
        Beds = findViewById(R.id.Beds);
        Bathroom = findViewById(R.id.Bathroom);
        HouseNo = findViewById(R.id.HouseNo);
        profileImage = findViewById(R.id.profileImage);
        uNeme = findViewById(R.id.uNeme);
        uemail = findViewById(R.id.uemail);
        phone = findViewById(R.id.phone);
        if (EditFlage) {
            edit.setVisibility(View.VISIBLE);
        } else {
            edit.setVisibility(View.GONE);
            SenderUserId = getIntent().getStringExtra("SenderUserId");//get user id from HomeTinderCardAdapter for show only homedetail not for edit
        }
        details = new HomeDetails();
        hImagList = new ArrayList();
        featureList = new ArrayList();
        hRuleList = new ArrayList();
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        getUserData();
    }

    private void getUserData() {
        SwitchDBHelper switchDBHelper = new SwitchDBHelper(HomeDetailActivity.this);
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
                Picasso.with(HomeDetailActivity.this).load(data.getProfile_image()).placeholder(R.drawable.userimage).resize(100, 100).into(profileImage);
            }
            getHomeDetail();
        }
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        //mMap.setMyLocationEnabled(true);
    }

    //get home detail
    private void getHomeDetail() {
        if (Utility.isOnline(HomeDetailActivity.this)) {
            dotDialog = new ASTProgressBar(HomeDetailActivity.this);
            dotDialog.show();
            JSONObject object = new JSONObject();
            try {
                object.put("api_key", Contants.API_KEY);
                if (SenderUserId != null && !SenderUserId.equals("")) {
                    object.put("user_id", SenderUserId);
                } else {
                    object.put("user_id", userId);
                }
                object.put("home_id", HomeId);
            } catch (JSONException e) {
                e.printStackTrace();
            }

            String serviceURL = Contants.BASE_URL + Contants.Homedetails;

            ServiceCaller serviceCaller = new ServiceCaller(HomeDetailActivity.this);
            serviceCaller.CallCommanServiceMethod(serviceURL, object, "getHomeDetail", new IAsyncWorkCompletedCallback() {
                @Override
                public void onDone(String result, boolean isComplete) {
                    if (isComplete) {
                        parseAllHomeServiceData(result);
                    } else {
                        if (dotDialog.isShowing()) {
                            dotDialog.dismiss();
                        }
                        Utility.alertForErrorMessage(Contants.Error, HomeDetailActivity.this);
                        finish();
                    }
                }
            });
        } else {
            Utility.alertForErrorMessage(Contants.OFFLINE_MESSAGE, HomeDetailActivity.this);//off line msg....
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
                                String id = databject.optString("id").toString();
                                details.setId(id);
                                String user_id = databject.optString("user_id").toString();
                                details.setUser_id(user_id);
                                String home_type = databject.optString("home_type").toString();
                                details.setHome_type(home_type);
                                String bathrooms = databject.optString("bathrooms").toString();
                                details.setBathrooms(bathrooms);
                                String bedrooms = databject.optString("bedrooms").toString();
                                details.setBedrooms(bedrooms);
                                String sleeps = databject.optString("sleeps").toString();
                                details.setSleeps(sleeps);
                                String property_type = databject.optString("property_type").toString();
                                details.setProperty_type(property_type);
                                String pets = databject.optString("pets").toString();
                                details.setPets(pets);
                                String family_matters = databject.optString("family_matters").toString();
                                details.setFamily_matters(family_matters);
                                String title = databject.optString("title").toString();
                                details.setTitle(title);
                                String sort_description = databject.optString("sort_description").toString();
                                details.setSort_description(sort_description);
                                String house_no = databject.optString("house_no").toString();
                                details.setHouse_no(house_no);
                                String location = databject.optString("location").toString();
                                details.setLocation(location);
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
                                String profile_image = databject.optString("profile_image").toString();
                                details.setProfile_image(profile_image);
                                String startdate = databject.optString("startdate").toString();
                                details.setStartdate(startdate);
                                String enddate = databject.optString("enddate").toString();
                                details.setEnddate(enddate);
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
                                String city_name = databject.optString("city_name").toString();
                                details.setCity_name(city_name);
                                String country_name = databject.optString("country_name").toString();
                                details.setCountry_name(country_name);
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

                                JSONArray home_featuresAttay = databject.optJSONArray("home_features");
                                if (home_featuresAttay != null) {
                                    featureName = new String[home_featuresAttay.length()];
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
                                        featureName[i] = featurename;
                                    }
                                    details.setFeatureList(featureList);
                                }

                                JSONArray home_rulesAttay = databject.optJSONArray("home_rules");
                                if (home_rulesAttay != null) {
                                    hRuleName = new String[home_rulesAttay.length()];
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
                                        hRuleName[i] = featurename;
                                    }
                                    details.setHouseRuleList(hRuleList);
                                }
                                JSONArray homegalleryAttay = databject.optJSONArray("homegallery");
                                if (homegalleryAttay != null) {
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
                        e.printStackTrace();
                    }

                    return flag;
                }

                @Override
                protected void onPostExecute(Boolean flag) {
                    super.onPostExecute(flag);
                    if (flag) {
                        setvalue();
                    } else {
                        Utility.showToast(HomeDetailActivity.this, "Home Detail Not available!");
                        finish();
                    }
                    if (dotDialog.isShowing()) {
                        dotDialog.dismiss();
                    }
                }
            }.execute();
        }
    }

    private void setvalue() {
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(HomeDetailActivity.this, LinearLayoutManager.HORIZONTAL, false);
        amenitiesrecycler_view.setLayoutManager(mLayoutManager);
        amenitiesrecycler_view.setNestedScrollingEnabled(false);
        amenitiesrecycler_view.setHasFixedSize(false);
        amenitiesrecycler_view.setItemAnimator(new DefaultItemAnimator());
        HomeDetailFeatureAdapter adapter = new HomeDetailFeatureAdapter(HomeDetailActivity.this, featureList);
        RecyclerView.LayoutManager houseLayoutManager = new LinearLayoutManager(HomeDetailActivity.this, LinearLayoutManager.VERTICAL, false);
        amenitiesrecycler_view.setAdapter(adapter);

        houseRulesrecycler_view.setLayoutManager(houseLayoutManager);
        houseRulesrecycler_view.setNestedScrollingEnabled(false);
        houseRulesrecycler_view.setHasFixedSize(false);
        houseRulesrecycler_view.setItemAnimator(new DefaultItemAnimator());
        HomeDetailRuleAdapter ruleAdapter = new HomeDetailRuleAdapter(HomeDetailActivity.this, hRuleList);
        houseRulesrecycler_view.setAdapter(ruleAdapter);

        Title.setText(details.getTitle());
        description.setText(details.getSort_description());
        gender.setText(details.getGender());
        religion.setText(details.getReligion());
        securityLevel.setText(details.getLevel_security());
        Homestyle.setText(details.getHomestyle());
        Propertytype.setText(details.getPropertytype());
        PetAllowed.setText(details.getPetsallowed());
        FamilyMatters.setText(details.getFamily_matters());
        Bedrooms.setText(details.getBedrooms());
        Beds.setText(details.getBedrooms());
        Bathroom.setText(details.getBathrooms());
        HouseNo.setText(details.getHouse_no());
        uNeme.setText(username);
        uemail.setText(emailStr);
        phone.setText(phoneStr);
        details.getLatitude();
        details.getLongitude();
        if (details.getLatitude() != null && !details.getLatitude().equals("") && details.getLongitude() != null && !details.getLongitude().equals("")) {
            LatLng sy = new LatLng(-Double.parseDouble(details.getLatitude()), Double.parseDouble(details.getLongitude()));
            mMap.addMarker(new MarkerOptions().position(sy).title(details.getTitle()));
            mMap.moveCamera(CameraUpdateFactory.newLatLng(sy));
        }

        slideimage();

    }

    private void slideimage() {

        mPager = (ViewPager) findViewById(R.id.pager);
        if (hImagList != null && hImagList.size() > 0) {
            mPager.setAdapter(new SlidingImage_Adapter_For_ItemDetails(HomeDetailActivity.this, hImagList));



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
            case R.id.edit:
                if (details != null) {
                    Intent homeintent = new Intent(HomeDetailActivity.this, AddHomeActivity.class);
                   /* String homeStr = new Gson().toJson(details);
                    Utility.setHomeDetail(HomeDetailActivity.this, homeStr, true);*/
                    SwitchDBHelper dbHelper = new SwitchDBHelper(HomeDetailActivity.this);
                    dbHelper.deleteAllRows("AddEditHomeData");
                    dbHelper.insertAddEditHomeData(details);
                    startActivity(homeintent);
                }
                break;
        }
    }
}
