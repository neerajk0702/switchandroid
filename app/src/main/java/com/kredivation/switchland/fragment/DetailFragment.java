package com.kredivation.switchland.fragment;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.os.AsyncTask;
import android.os.Bundle;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

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
import com.kredivation.switchland.database.SwitchDBHelper;
import com.kredivation.switchland.framework.IAsyncWorkCompletedCallback;
import com.kredivation.switchland.framework.ServiceCaller;
import com.kredivation.switchland.model.Data;
import com.kredivation.switchland.model.HomeDetails;
import com.kredivation.switchland.model.Home_features;
import com.kredivation.switchland.model.Home_rules;
import com.kredivation.switchland.model.ServiceContentData;
import com.kredivation.switchland.utilities.ASTProgressBar;
import com.kredivation.switchland.utilities.Contants;
import com.kredivation.switchland.utilities.Utility;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class DetailFragment extends Fragment implements OnMapReadyCallback {


    public DetailFragment() {
        // Required empty public constructor
    }

    String HomeDetail, SenderUserId;

    public static DetailFragment newInstance(String param1, String param2) {
        DetailFragment fragment = new DetailFragment();
        Bundle args = new Bundle();
        args.putString("HomeDetail", param1);
        args.putString("SenderUserId", param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            HomeDetail = getArguments().getString("HomeDetail");
            SenderUserId = getArguments().getString("SenderUserId");
        }
    }

    View view;
    Context context;
    TextView gender, religion, securityLevel, Homestyle, Propertytype, PetAllowed, FamilyMatters, HouseNo, uNeme, uemail, phone;
    private TextView address, citycountry, zipcode;
    private RecyclerView amenitiesrecycler_view, houseRulesrecycler_view;
    private GoogleMap mMap;
    String[] featureName;
    String[] hRuleName;
    ArrayList<Home_features> featureList;
    ArrayList<Home_rules> hRuleList;
    private String userId;
    String username;
    String emailStr, phoneStr;
    HomeDetails details;
    ImageView profileImage;
    ASTProgressBar dotDialog;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_detail, container, false);
        context = getActivity();
        init();
        return view;
    }

    private void init() {
        amenitiesrecycler_view = view.findViewById(R.id.amenitiesrecycler_view);
        houseRulesrecycler_view = view.findViewById(R.id.houseRulesrecycler_view);
        gender = view.findViewById(R.id.gender);
        religion = view.findViewById(R.id.religion);
        securityLevel = view.findViewById(R.id.securityLevel);
        Homestyle = view.findViewById(R.id.Homestyle);
        Propertytype = view.findViewById(R.id.Propertytype);
        PetAllowed = view.findViewById(R.id.PetAllowed);
        FamilyMatters = view.findViewById(R.id.FamilyMatters);
        HouseNo = view.findViewById(R.id.HouseNo);
        address = view.findViewById(R.id.address);
        citycountry = view.findViewById(R.id.citycountry);
        zipcode = view.findViewById(R.id.zipcode);
        uNeme = view.findViewById(R.id.uNeme);
        uemail = view.findViewById(R.id.uemail);
        phone = view.findViewById(R.id.phone);
        profileImage = view.findViewById(R.id.profileImage);
        featureList = new ArrayList();
        hRuleList = new ArrayList();
        SupportMapFragment mapFragment = (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        details = new HomeDetails();
        parseAllHomeServiceData();
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        if (ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
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


    public void parseAllHomeServiceData() {
        if (HomeDetail != null) {

            new AsyncTask<Void, Void, Boolean>() {
                @Override
                protected Boolean doInBackground(Void... voids) {
                    Boolean flag = false;

                    try {

                        JSONObject jsonRootObject = new JSONObject(HomeDetail);
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
                                String travel_country = databject.optString("travel_country").toString();
                                details.setTravel_country(travel_country);
                                String travel_city = databject.optString("travel_city").toString();
                                details.setTravel_city(travel_city);
                                String travel_city_name = databject.optString("travel_city_name").toString();
                                details.setTravel_country_name(travel_city_name);
                                String travel_country_name = databject.optString("travel_country_name").toString();
                                details.setTravel_city_name(travel_country_name);


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
                               /* JSONArray homegalleryAttay = databject.optJSONArray("homegallery");
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
                                }*/
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
                    }
                }
            }.execute();
        }
    }

    private void setvalue() {
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false);
        amenitiesrecycler_view.setLayoutManager(mLayoutManager);
        amenitiesrecycler_view.setNestedScrollingEnabled(false);
        amenitiesrecycler_view.setHasFixedSize(false);
        amenitiesrecycler_view.setItemAnimator(new DefaultItemAnimator());
        HomeDetailFeatureAdapter adapter = new HomeDetailFeatureAdapter(context, featureList);
        RecyclerView.LayoutManager houseLayoutManager = new LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false);
        amenitiesrecycler_view.setAdapter(adapter);

        houseRulesrecycler_view.setLayoutManager(houseLayoutManager);
        houseRulesrecycler_view.setNestedScrollingEnabled(false);
        houseRulesrecycler_view.setHasFixedSize(false);
        houseRulesrecycler_view.setItemAnimator(new DefaultItemAnimator());
        HomeDetailRuleAdapter ruleAdapter = new HomeDetailRuleAdapter(context, hRuleList);
        houseRulesrecycler_view.setAdapter(ruleAdapter);

        // Title.setText(details.getTitle());
        //description.setText(details.getSort_description());
        gender.setText(details.getGender());
        religion.setText(details.getReligion());
        securityLevel.setText(details.getLevel_security());
        Homestyle.setText(details.getHomestyle());
        Propertytype.setText(details.getPropertytype());
        PetAllowed.setText(details.getPetsallowed());
        FamilyMatters.setText(details.getFamily_matters());
        HouseNo.setText(details.getHouse_no());
        //address, citycountry, zipcode;
        address.setText(details.getAddress1() + ", " + details.getAddress2() + ", " + details.getLandmarks());
        citycountry.setText(details.getCity_name() + ", " + details.getCountry_name());
        zipcode.setText(details.getZipcode());
        searchLocation();
        if (SenderUserId != null && !SenderUserId.equals("")) {//get other user home detail
            getSenderUserInfo();
        } else {
            getUserData();
        }

    }

    private void getUserData() {
        SwitchDBHelper switchDBHelper = new SwitchDBHelper(context);
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
                uNeme.setText(username);
                uemail.setText(emailStr);
                phone.setText(phoneStr);
                Picasso.with(context).load(details.getProfile_image()).resize(80, 80).placeholder(R.drawable.avter).into(profileImage);
            }
        }
    }

    public void searchLocation() {
        if (details.getLatitude() != null && !details.getLatitude().equals("") && details.getLongitude() != null && !details.getLongitude().equals("")) {
            LatLng sy = new LatLng(Double.parseDouble(details.getLatitude()), Double.parseDouble(details.getLongitude()));
            mMap.addMarker(new MarkerOptions().position(sy).title(details.getTitle()));
            mMap.moveCamera(CameraUpdateFactory.newLatLng(sy));
            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(sy, 12));
        } else {
            List<Address> addressList = null;
            if (details.getZipcode() != null && !details.getZipcode().equals("")) {
                Geocoder geocoder = new Geocoder(context);
                try {
                    addressList = geocoder.getFromLocationName(details.getZipcode(), 1);

                } catch (IOException e) {
                    e.printStackTrace();
                }
                if (addressList != null && addressList.size() > 0) {
                    Address address = addressList.get(0);
                    LatLng latLng = new LatLng(address.getLatitude(), address.getLongitude());
                    mMap.addMarker(new MarkerOptions().position(latLng).title(details.getZipcode()));
                    // mMap.animateCamera(CameraUpdateFactory.newLatLng(latLng));
                    mMap.animateCamera(CameraUpdateFactory.zoomIn());
                    mMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));
                    mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, 12));

                    // mMap.animateCamera(CameraUpdateFactory.zoomIn());
                    // mMap.animateCamera(CameraUpdateFactory.zoomTo(20), 6000, null);
                    Toast.makeText(context, address.getLatitude() + " " + address.getLongitude(), Toast.LENGTH_LONG).show();
                }
            }
        }
    }

    private void getSenderUserInfo() {
        if (Utility.isOnline(context)) {
            dotDialog = new ASTProgressBar(context);
            dotDialog.show();
            JSONObject object = new JSONObject();
            try {
                object.put("api_key", Contants.API_KEY);
                object.put("user_id", SenderUserId);
            } catch (JSONException e) {
               // e.printStackTrace();
            }

            String serviceURL = Contants.BASE_URL + Contants.Userinfo;

            ServiceCaller serviceCaller = new ServiceCaller(context);
            serviceCaller.CallCommanServiceMethod(serviceURL, object, "getSenderUserInfo", new IAsyncWorkCompletedCallback() {
                @Override
                public void onDone(String result, boolean isComplete) {
                    if (isComplete) {
                        parseSenderUserServiceData(result);
                    } else {
                        if (dotDialog.isShowing()) {
                            dotDialog.dismiss();
                        }
                        Utility.alertForErrorMessage(Contants.Error, context);
                    }
                }
            });
        }
    }

    public void parseSenderUserServiceData(String result) {
        if (result != null) {
            final ServiceContentData serviceData = new Gson().fromJson(result, ServiceContentData.class);
            if (serviceData != null) {
                if (serviceData.isSuccess()) {
                    if (serviceData.getData() != null) {
                        uNeme.setText(serviceData.getData().getFull_name());
                        uemail.setText(serviceData.getData().getEmail());
                        phone.setText(serviceData.getData().getMobile_number());
                        Picasso.with(context).load(serviceData.getData().getProfile_image()).placeholder(R.drawable.userimage).resize(80, 80).into(profileImage);
                    }
                }
                if (dotDialog.isShowing()) {
                    dotDialog.dismiss();
                }
            }
        }
    }
}
