package com.kredivation.switchland.activity;

import android.content.pm.ActivityInfo;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.kredivation.switchland.R;
import com.kredivation.switchland.database.SwitchDBHelper;
import com.kredivation.switchland.framework.IAsyncWorkCompletedCallback;
import com.kredivation.switchland.framework.ServiceCaller;
import com.kredivation.switchland.model.City;
import com.kredivation.switchland.model.Country;
import com.kredivation.switchland.model.Data;
import com.kredivation.switchland.model.HomeDetails;
import com.kredivation.switchland.model.Home_features;
import com.kredivation.switchland.model.Home_rules;
import com.kredivation.switchland.model.Homegallery;
import com.kredivation.switchland.model.MyhomeArray;
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

public class ConfirmDetailActivity extends AppCompatActivity implements View.OnClickListener {
    private Toolbar toolbar;
    private String userId;
    String ChatUserId, ProfileImg, FullName, HomeId;
    HomeDetails details;
    ASTProgressBar dotDialog;
    ArrayList<Home_features> featureList;
    ArrayList<Home_rules> hRuleList;
    ArrayList<Homegallery> hImagList;
    String[] hImageName;
    String[] featureName;
    String[] hRuleName;
    TextView userName, country, city, startdate, enddate, bed, title, descripction;
    ImageView userImage, homeImage;
    SwitchDBHelper switchDBHelper;

    ImageView hosteruserImage, hosterhomeImage;
    TextView hosteruseruserName, hostercountry, hostercity, hosterstartdate, hosterenddate, hosterbed, hostertitle, hosterdescripction;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_confirm_detail);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if (CompatibilityUtility.isTablet(ConfirmDetailActivity.this)) {
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

        userImage = findViewById(R.id.userImage);
        userName = findViewById(R.id.userName);
        country = findViewById(R.id.country);
        city = findViewById(R.id.city);
        startdate = findViewById(R.id.startdate);
        enddate = findViewById(R.id.enddate);
        bed = findViewById(R.id.bed);
        homeImage = findViewById(R.id.homeImage);
        title = findViewById(R.id.title);
        descripction = findViewById(R.id.descripction);
        Button continuetopay = findViewById(R.id.continuetopay);
        continuetopay.setOnClickListener(this);

        hosteruserImage = findViewById(R.id.hosteruserImage);
        hosterhomeImage = findViewById(R.id.hosterhomeImage);
        hosteruseruserName = findViewById(R.id.hosteruseruserName);
        hostercountry = findViewById(R.id.hostercountry);
        hostercity = findViewById(R.id.hostercity);
        hosterstartdate = findViewById(R.id.hosterstartdate);
        hosterenddate = findViewById(R.id.hosterenddate);
        hosterbed = findViewById(R.id.hosterbed);
        hostertitle = findViewById(R.id.hostertitle);
        hosterdescripction = findViewById(R.id.hosterdescripction);

        getUserdata();
    }

    private void getUserdata() {
        ChatUserId = getIntent().getStringExtra("ChatUserId");
        ProfileImg = getIntent().getStringExtra("ProfileImg");
        FullName = getIntent().getStringExtra("FullName");
        HomeId = getIntent().getStringExtra("HomeId");
        Picasso.with(ConfirmDetailActivity.this).load(ProfileImg).placeholder(R.drawable.userimage).resize(80, 80).into(hosteruserImage);
        hosteruseruserName.setText(FullName);

        switchDBHelper = new SwitchDBHelper(ConfirmDetailActivity.this);
        ArrayList<MyhomeArray> myHomeList = switchDBHelper.getAllMyhomedata();
        ArrayList<Data> userData = switchDBHelper.getAllUserInfoList();
        if (userData != null && userData.size() > 0) {
            for (Data data : userData) {
                userId = data.getId();
                Picasso.with(ConfirmDetailActivity.this).load(data.getProfile_image()).placeholder(R.drawable.userimage).resize(80, 80).into(userImage);
                if (data.getFull_name() != null && !data.getFirst_name().equals("")) {
                    userName.setText(data.getFull_name());
                } else {
                    userName.setText(data.getFirst_name() + " " + data.getLast_name());
                }
            }
        }
        if (myHomeList != null && myHomeList.size() > 0) {
            for (MyhomeArray myhomeArray : myHomeList) {
                startdate.setText(myhomeArray.getStartdate());
                enddate.setText(myhomeArray.getEnddate());
                bed.setText(myhomeArray.getSleeps());
                title.setText(myhomeArray.getTitle());
                descripction.setText(myhomeArray.getSort_description());
                Picasso.with(ConfirmDetailActivity.this).load(myhomeArray.getProfile_image()).placeholder(R.drawable.noimage).into(homeImage);
                setCityAndCountry(myhomeArray.getCountry_id(), myhomeArray.getCity_id());
            }
        }

        hImagList = new ArrayList();
        featureList = new ArrayList();
        hRuleList = new ArrayList();
        details = new HomeDetails();
        getSenderHomeDetail();
    }

    private void setCityAndCountry(String countryId, String cityId) {
        ServiceContentData sData = switchDBHelper.getMasterData();
        if (sData != null) {
            if (sData.getData() != null) {
                Data MData = sData.getData();
                Country[] countryList = MData.getCountry();
                City[] cityList = MData.getCity();
                for (Country countryData : countryList) {
                    if (countryId.equals(countryData.getId())) {
                        country.setText(countryData.getName());
                    }
                }
                for (City citydata : cityList) {
                    if (cityId.equals(citydata.getId())) {
                        city.setText(citydata.getName());
                    }

                }
            }
        }
    }

    //get home detail
    private void getSenderHomeDetail() {
        if (Utility.isOnline(ConfirmDetailActivity.this)) {
            dotDialog = new ASTProgressBar(ConfirmDetailActivity.this);
            dotDialog.show();
            JSONObject object = new JSONObject();
            try {
                object.put("api_key", Contants.API_KEY);
                object.put("user_id", ChatUserId);
                object.put("home_id", HomeId);
            } catch (JSONException e) {
                e.printStackTrace();
            }

            String serviceURL = Contants.BASE_URL + Contants.Homedetails;

            ServiceCaller serviceCaller = new ServiceCaller(ConfirmDetailActivity.this);
            serviceCaller.CallCommanServiceMethod(serviceURL, object, "getSenderHomeDetail", new IAsyncWorkCompletedCallback() {
                @Override
                public void onDone(String result, boolean isComplete) {
                    if (isComplete) {
                        parseAllHomeServiceData(result);
                    } else {
                        if (dotDialog.isShowing()) {
                            dotDialog.dismiss();
                        }
                        Utility.alertForErrorMessage(Contants.Error, ConfirmDetailActivity.this);
                        finish();
                    }
                }
            });
        } else {
            Utility.alertForErrorMessage(Contants.OFFLINE_MESSAGE, ConfirmDetailActivity.this);//off line msg....
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
                        Utility.showToast(ConfirmDetailActivity.this, "Home Detail Not available!");
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
        if (details != null) {
            hostercountry.setText(details.getCountry_name());
            hostercity.setText(details.getCity_name());
            hosterstartdate.setText(details.getStartdate());
            hosterenddate.setText(details.getEnddate());
            hosterbed.setText(details.getSleeps());
            hostertitle.setText(details.getTitle());
            hosterdescripction.setText(details.getSort_description());
            Picasso.with(ConfirmDetailActivity.this).load(details.getProfile_image()).placeholder(R.drawable.noimage).into(hosterhomeImage);

        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.continuetopay:
                break;
        }
    }
}
