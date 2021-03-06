package com.kredivation.switchland.fragment;


import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Typeface;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;

import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.kredivation.switchland.R;
import com.kredivation.switchland.database.SwitchDBHelper;
import com.kredivation.switchland.model.City;
import com.kredivation.switchland.model.Country;
import com.kredivation.switchland.model.Data;
import com.kredivation.switchland.model.HomeDetails;
import com.kredivation.switchland.model.ServiceContentData;
import com.kredivation.switchland.utilities.FontManager;
import com.kredivation.switchland.utilities.Utility;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link AddHomeLocationFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class AddHomeLocationFragment extends Fragment implements OnMapReadyCallback, View.OnClickListener {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;


    public AddHomeLocationFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment AddHomeLocationFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static AddHomeLocationFragment newInstance(String param1, String param2) {
        AddHomeLocationFragment fragment = new AddHomeLocationFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    private View view;
    private Context context;
    private GoogleMap mMap;
    private Spinner citySpinner, countrySpinner;
    private LinearLayout addressLayout;
    boolean lookUpFlag = false;

    private EditText zipCode, address, enterzipcode, landmark, hno;
    private String addressStr, enterzipcodeStr, landmarkStr, hnoStr;
    Button lookUp;
    String[] countryList;
    ArrayList<String> cityList;
    private String cityId = "";
    private String countryId = "";
    private City[] city;
    private Country[] country;
    String saveCountryId;
    String saveCityId;
    int cityPos = 0;
    HomeDetails MyHomedata;
    String homeId;
    ArrayList<String> cityIdList;
    String lat = "";
    String longt = "";
    String title;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        context = getActivity();
        view = inflater.inflate(R.layout.fragment_add_home_location, container, false);
        init();
        return view;
    }

    private void init() {

        Typeface materialdesignicons_font = FontManager.getFontTypefaceMaterialDesignIcons(getActivity(), "fonts/materialdesignicons-webfont.otf");
        TextView nextIcon = (TextView) view.findViewById(R.id.nextIcon);
        nextIcon.setTypeface(materialdesignicons_font);
        nextIcon.setText(Html.fromHtml("&#xf142;"));
        TextView previous = (TextView) view.findViewById(R.id.previous);
        previous.setTypeface(materialdesignicons_font);
        previous.setText(Html.fromHtml("&#xf141;"));
        previous.setOnClickListener(this);
        LinearLayout nextLayout = (LinearLayout) view.findViewById(R.id.nextLayout);
        nextLayout.setOnClickListener(this);
        TextView enterAddress = view.findViewById(R.id.enterAddress);
        enterAddress.setOnClickListener(this);
          /*SupportMapFragment mapFragment = (SupportMapFragment) getActivity().getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);*/
        SupportMapFragment mapFragment = (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        citySpinner = view.findViewById(R.id.citySpinner);
        countrySpinner = view.findViewById(R.id.countrySpinner);
        addressLayout = view.findViewById(R.id.addressLayout);

        zipCode = view.findViewById(R.id.zipCode);
        address = view.findViewById(R.id.address);
        enterzipcode = view.findViewById(R.id.enterzipcode);
        landmark = view.findViewById(R.id.landmark);
        lookUp = view.findViewById(R.id.lookUp);
        lookUp.setOnClickListener(this);
        hno = view.findViewById(R.id.hno);
        getSaveData();
        setcountry();
    }

    private void setcountry() {
        SwitchDBHelper switchDBHelper = new SwitchDBHelper(context);
        ServiceContentData sData = switchDBHelper.getMasterData();
        if (sData != null) {
            if (sData.getData() != null) {
                final Data MData = sData.getData();
                country = MData.getCountry();
                if (country != null) {
                    countryList = new String[country.length];
                    int countryPos = 0;
                    for (int i = 0; i < country.length; i++) {
                        countryList[i] = String.valueOf(country[i].getName());
                        if (saveCountryId != null) {
                            if (saveCountryId.equals(country[i].getId())) {
                                countryPos = i;//save country pos for selected
                            }
                        }
                    }
                    ArrayAdapter<String> countryAdapter = new ArrayAdapter<String>(context, R.layout.spinner_row, countryList);
                    countrySpinner.setAdapter(countryAdapter);
                    countrySpinner.setSelection(countryPos);
                    countrySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                        @Override
                        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                            countryId = country[position].getId();
                            city = MData.getCity();
                            if (city != null) {
                                cityList = new ArrayList();
                                cityIdList = new ArrayList();
                                for (int i = 0; i < city.length; i++) {
                                    if (countryId.equals(city[i].getCountry_id())) {
                                        cityList.add(String.valueOf(city[i].getName()));
                                        cityIdList.add(String.valueOf(city[i].getId()));
                                        if (saveCityId != null) {
                                            if (saveCityId.equals(city[i].getId())) {
                                                cityPos = i;//save country pos for selected
                                            }
                                        }
                                    }
                                }
                                if (cityList != null && cityList.size() > 0) {
                                    ArrayAdapter<String> cityAdapter = new ArrayAdapter<String>(context, R.layout.spinner_row, cityList);
                                    citySpinner.setSelection(cityPos);
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
                        cityId = cityIdList.get(position).toString();
                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> parent) {

                    }
                });
            }
        }
    }

    //get data from pre
    private void getSaveData() {

        SwitchDBHelper dbHelper = new SwitchDBHelper(getActivity());
        ArrayList<HomeDetails> homeDetails = dbHelper.getAllAddEditHomeDataList();
        if (homeDetails != null) {
            for (HomeDetails details : homeDetails) {
                MyHomedata = details;
                if (MyHomedata != null) {//for home edit
                    homeId = MyHomedata.getId();
                    hnoStr = MyHomedata.getHouse_no();
                    addressStr = MyHomedata.getAddress1();
                    String address2 = MyHomedata.getAddress2();
                    landmarkStr = MyHomedata.getLandmarks();
                    enterzipcodeStr = MyHomedata.getZipcode();
                    //zipCodeStr = MyHomedata.getLocation();
                    saveCountryId = MyHomedata.getCountry_id();
                    saveCityId = MyHomedata.getCity_id();
                    hno.setText(hnoStr);
                    //zipCode.setText(zipCodeStr);
                    String completeAdd="";
                    if (addressStr != null && !addressStr.equalsIgnoreCase("null")){
                        completeAdd=addressStr;
                    } if (address2 != null && !address2.equalsIgnoreCase("null")){
                        completeAdd=completeAdd+address2;
                    }
                    if(completeAdd!=null){
                        address.setText(completeAdd);
                    }
                    enterzipcode.setText(enterzipcodeStr);
                    landmark.setText(landmarkStr);
                    title = MyHomedata.getTitle();
                    lat = MyHomedata.getLatitude();
                    longt = MyHomedata.getLongitude();
                    //searchLocation(enterzipcodeStr);

                }
            }
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.nextLayout:
                if (lookUpFlag) {
                    saveScreenData(true, false);
                } else {
                    Utility.showToast(context, "Please click LookUp!");
                }
                break;
            case R.id.previous:
                saveScreenData(false, false);
                break;
            case R.id.lookUp:
                if (isValidate()) {
                    lookUpFlag = true;
                    if (mMap != null) {
                        mMap.clear();//remove all marker
                    }
                    searchLocation(enterzipcodeStr);
                    Utility.showToast(context, "Home Location Save");
                }
                break;
        }
    }

    // ----validation -----
    private boolean isValidate() {
        String emailRegex = "[A-Z0-9a-z._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,4}";
        addressStr = address.getText().toString();
        enterzipcodeStr = enterzipcode.getText().toString();
        landmarkStr = landmark.getText().toString();
        hnoStr = hno.getText().toString();
        if (hnoStr.length() == 0) {
            Utility.showToast(getActivity(), "Please Enter House Number!");
            requestFocus(hno);
            return false;
        } else if (addressStr.length() == 0) {
            Utility.showToast(getActivity(), "Please Enter Address!");
            requestFocus(address);
            return false;
        } else if (enterzipcodeStr.length() == 0) {
            Utility.showToast(getActivity(), "Please Enter Zipcode!");
            requestFocus(enterzipcode);
            return false;
        } /*else if (landmarkStr.length() == 0) {
            input_layout_Landmark.setError("Please Enter Landmark");
            requestFocus(landmark);
            return false;
        }*/ else if (countryId == null || countryId.equals("0") || countryId.equals("")) {
            Utility.showToast(context, "Please select country!");
            return false;
        } else if (cityId == null || cityId.equals("0") || cityId.equals("")) {
            Utility.showToast(context, "Please select city!");
            return false;
        }
        return true;
    }

    private void requestFocus(View view) {
        if (view.requestFocus()) {
            getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
        }
    }

    private void saveScreenData(boolean NextPreviousFlag, boolean DoneFlag) {
        saveData();
        Intent intent = new Intent("ViewPageChange");
        intent.putExtra("NextPreviousFlag", NextPreviousFlag);
        intent.putExtra("DoneFlag", DoneFlag);
        getActivity().sendBroadcast(intent);
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
        // mMap.setMyLocationEnabled(true);
        // LatLng sy = new LatLng(-34, 151);
        // mMap.addMarker(new MarkerOptions().position(sy).title("Marker in Sydney"));
        // mMap.moveCamera(CameraUpdateFactory.newLatLng(sy));
        if (MyHomedata != null) {
            if (lat != null && !lat.equals("") && longt != null && !longt.equals("")) {
                LatLng sy = new LatLng(Double.parseDouble(MyHomedata.getLatitude()), Double.parseDouble(MyHomedata.getLongitude()));
                mMap.addMarker(new MarkerOptions().position(sy).title(MyHomedata.getTitle()));
                mMap.moveCamera(CameraUpdateFactory.newLatLng(sy));
                mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(sy, 12));
            } else {
                searchLocation(enterzipcodeStr);
            }
        }
    }

    //save data
    private void saveData() {

        if (MyHomedata != null) {
            HomeDetails details = new HomeDetails();
            details.setId(homeId);
            details.setAddress1(addressStr);
            details.setHouse_no(hnoStr);
            details.setLandmarks(landmarkStr);
            details.setZipcode(enterzipcodeStr);
            details.setLocation("");
            details.setCountry_id(countryId);
            details.setCity_id(cityId);
            details.setLatitude(lat);
            details.setLongitude(longt);
            SwitchDBHelper dbHelper = new SwitchDBHelper(getActivity());
            dbHelper.updateAddEditHomeLocation(details);
        }
    }

    public void searchLocation(String enterzipcodeStr) {
        List<Address> addressList = null;

        if (enterzipcodeStr != null && !enterzipcodeStr.equals("")) {
            Geocoder geocoder = new Geocoder(getActivity());
            try {
                addressList = geocoder.getFromLocationName(enterzipcodeStr, 1);

            } catch (IOException e) {
                e.printStackTrace();
            }
            if (addressList != null && addressList.size() > 0) {
                Address address = addressList.get(0);
                LatLng latLng = new LatLng(address.getLatitude(), address.getLongitude());
                if (mMap != null) {
                    mMap.addMarker(new MarkerOptions().position(latLng).title(title));
                    // mMap.animateCamera(CameraUpdateFactory.newLatLng(latLng));
                    mMap.animateCamera(CameraUpdateFactory.zoomIn());
                    mMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));
                    mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, 12));
                }
                // mMap.animateCamera(CameraUpdateFactory.zoomIn());
                // mMap.animateCamera(CameraUpdateFactory.zoomTo(20), 6000, null);
                lat = String.valueOf(address.getLatitude());
                longt = String.valueOf(address.getLongitude());
                Toast.makeText(getActivity(), address.getLatitude() + " " + address.getLongitude(), Toast.LENGTH_LONG).show();
            }
        }
    }
}