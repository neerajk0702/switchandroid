package com.kredivation.switchland.activity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.view.View;
import android.widget.TextView;

import com.kredivation.switchland.R;
import com.kredivation.switchland.adapters.AddHomePagerAdapter;
import com.kredivation.switchland.database.SwitchDBHelper;
import com.kredivation.switchland.model.Bathrooms;
import com.kredivation.switchland.model.Bedrooms;
import com.kredivation.switchland.model.Data;
import com.kredivation.switchland.model.Family;
import com.kredivation.switchland.model.Genderarray;
import com.kredivation.switchland.model.Home_style;
import com.kredivation.switchland.model.Pets_allowed;
import com.kredivation.switchland.model.Religion;
import com.kredivation.switchland.model.Security;
import com.kredivation.switchland.model.ServiceContentData;
import com.kredivation.switchland.model.Sleeps;
import com.kredivation.switchland.model.Type_of_property;
import com.kredivation.switchland.utilities.ASTProgressBar;
import com.kredivation.switchland.utilities.CompatibilityUtility;
import com.kredivation.switchland.utilities.FontManager;

public class AddHomeActivity extends AppCompatActivity {
    private Toolbar toolbar;
    ViewPager mPager;
    private String homestyleStr, securitStr, genderStr, religionStr, familyStr, petsStr, typeOfPropertiesStr, sleepsStr, bathroomsStr, bedroomsStr;
    private String securitiesId, homestylefId, bedroomsId, bathroomsId, sleepsId, typeOfPropertiesId, petsAllowedId, familyId, genderId, religionId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_home);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if (CompatibilityUtility.isTablet(AddHomeActivity.this)) {
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        } else {
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        }
        init();
    }

    private void init() {
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
        AddHomePagerAdapter mAdapter = new AddHomePagerAdapter(getSupportFragmentManager());
        mPager = (ViewPager) findViewById(R.id.viewPager_itemList);
        mPager.setAdapter(mAdapter);
    }

    //for geting next previous click action
    BroadcastReceiver receiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent.getAction().equalsIgnoreCase("ViewPageChange")) {
                boolean DoneFlag = intent.getBooleanExtra("DoneFlag", false);
                boolean NextPreviousFlag = intent.getBooleanExtra("NextPreviousFlag", false);

                if (NextPreviousFlag) {
                    int currentPage = mPager.getCurrentItem();
                    mPager.setCurrentItem(currentPage + 1, true);
                } else {
                    int currentPagepre = mPager.getCurrentItem();
                    if (currentPagepre > 0) {
                        mPager.setCurrentItem(currentPagepre - 1, true);
                    }
                }

            }
        }
    };

    @Override
    public void onResume() {
        super.onResume();
        registerReceiver(receiver, new IntentFilter("ViewPageChange"));
    }

    @Override
    public void onDestroy() {
        if (receiver != null) {
            unregisterReceiver(receiver);
            receiver = null;
        }
        super.onDestroy();
    }


    private void getSaveOverviewData() {
        SharedPreferences prefs = getSharedPreferences("AddHomePreferences", Context.MODE_PRIVATE);
        if (prefs != null) {
            //-----overview screen data---------
            homestyleStr = prefs.getString("homestyleStr", "");
            securitStr = prefs.getString("securitStr", "");
            genderStr = prefs.getString("genderStr", "");
            religionStr = prefs.getString("religionStr", "");
            familyStr = prefs.getString("familyStr", "");
            petsStr = prefs.getString("petsStr", "");
            typeOfPropertiesStr = prefs.getString("typeOfPropertiesStr", "");
            sleepsStr = prefs.getString("sleepsStr", "");
            bathroomsStr = prefs.getString("bathroomsStr", "");
            bedroomsStr = prefs.getString("bedroomsStr", "");
            //-----add home detail screen data---------
            getAllDataFromDB();
        }
    }

    private void getAllDataFromDB() {
        final ASTProgressBar dotDialog = new ASTProgressBar(AddHomeActivity.this);
        dotDialog.show();
        new AsyncTask<Void, Void, Boolean>() {
            @Override
            protected Boolean doInBackground(Void... voids) {
                Boolean flag = false;
                SwitchDBHelper switchDBHelper = new SwitchDBHelper(AddHomeActivity.this);
                ServiceContentData sData = switchDBHelper.getMasterData();
                if (sData != null) {
                    if (sData.getData() != null) {
                        Data MData = sData.getData();
                        Security[] securities = MData.getSecurity();
                        Home_style[] home_stylef = MData.getHome_style();
                        Bedrooms[] bedrooms = MData.getBedrooms();
                        Bathrooms[] bathrooms = MData.getBathrooms();
                        Sleeps[] sleeps = MData.getSleeps();
                        Type_of_property[] type_of_properties = MData.getType_of_property();
                        Pets_allowed[] pets_allowed = MData.getPets_allowed();
                        Family[] family = MData.getFamily();
                        Genderarray[] genderarray = MData.getGenderarray();
                        Religion[] religion = MData.getReligion();
                        if (securities != null) {
                            for (int i = 0; i < securities.length; i++) {
                                if (securitStr.equals(securities[i].getName())) {
                                    securitiesId = securities[i].getId();
                                }
                            }
                        }
                        if (home_stylef != null) {
                            for (int i = 0; i < home_stylef.length; i++) {
                                if (homestyleStr.equals(home_stylef[i].getName())) {
                                    homestylefId = home_stylef[i].getId();
                                }
                            }
                        }
                        if (bedrooms != null) {
                            for (int i = 0; i < bedrooms.length; i++) {
                                if (bedroomsStr.equals(bedrooms[i].getName())) {
                                    bedroomsId = String.valueOf(bedrooms[i].getId());
                                }
                            }
                        }
                        if (bathrooms != null) {
                            for (int i = 0; i < bathrooms.length; i++) {
                                if (bathroomsStr.equals(bathrooms[i].getName())) {
                                    bathroomsId = String.valueOf(bathrooms[i].getId());
                                }
                            }
                        }
                        if (sleeps != null) {
                            for (int i = 0; i < sleeps.length; i++) {
                                if (sleepsStr.equals(sleeps[i].getName())) {
                                    sleepsId = String.valueOf(sleeps[i].getId());
                                }
                            }
                        }
                        if (type_of_properties != null) {
                            for (int i = 0; i < type_of_properties.length; i++) {
                                if (typeOfPropertiesStr.equals(type_of_properties[i].getName())) {
                                    typeOfPropertiesId = type_of_properties[i].getId();
                                }
                            }
                        }
                        if (pets_allowed != null) {
                            for (int i = 0; i < pets_allowed.length; i++) {
                                if (petsStr.equals(pets_allowed[i].getName())) {
                                    petsAllowedId = pets_allowed[i].getId();
                                }
                            }
                        }
                        if (family != null) {
                            for (int i = 0; i < family.length; i++) {
                                if (familyStr.equals(family[i].getName())) {
                                    familyId = family[i].getId();
                                }
                            }
                        }
                        if (genderarray != null) {
                            for (int i = 0; i < genderarray.length; i++) {
                                if (genderStr.equals(genderarray[i].getName())) {
                                    genderId = genderarray[i].getId();
                                }
                            }
                        }
                        if (religion != null) {
                            for (int i = 0; i < religion.length; i++) {
                                if (religionStr.equals(religion[i].getName())) {
                                    religionId = religion[i].getId();
                                }
                            }
                        }
                    }
                }
                flag = true;
                return flag;
            }

            @Override
            protected void onPostExecute(Boolean flag) {
                super.onPostExecute(flag);
                if (dotDialog.isShowing()) {
                    dotDialog.dismiss();
                }
            }
        }.execute();
    }

}
