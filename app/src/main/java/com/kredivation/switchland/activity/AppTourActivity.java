package com.kredivation.switchland.activity;

import android.Manifest;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.graphics.Color;
import android.location.LocationManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v4.app.ActivityCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.text.Html;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.gson.Gson;
import com.kredivation.switchland.R;
import com.kredivation.switchland.adapters.AppTourPagerAdapter;
import com.kredivation.switchland.database.SwitchDBHelper;
import com.kredivation.switchland.framework.IAsyncWorkCompletedCallback;
import com.kredivation.switchland.framework.ServiceCaller;
import com.kredivation.switchland.model.Data;
import com.kredivation.switchland.model.ServiceContentData;
import com.kredivation.switchland.runtimepermission.PermissionResultCallback;
import com.kredivation.switchland.runtimepermission.PermissionUtils;
import com.kredivation.switchland.utilities.ASTProgressBar;
import com.kredivation.switchland.utilities.CompatibilityUtility;
import com.kredivation.switchland.utilities.Contants;
import com.kredivation.switchland.utilities.Utility;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;


public class AppTourActivity extends AppCompatActivity implements ViewPager.OnPageChangeListener, View.OnClickListener, ActivityCompat.OnRequestPermissionsResultCallback, PermissionResultCallback {
    private ViewPager viewPager;
    private TextView btnNext, btnFinish;
    private LinearLayout pager_indicator;
    private int dotsCount;
    private TextView[] dots;
    private AppTourPagerAdapter mAdapter;
    private ArrayList<Integer> tourImageList;
    private Boolean CheckOrientation = false;
    ArrayList<String> permissions = new ArrayList<>();
    PermissionUtils permissionUtils;
    private int REQUEST_CODE_ASK_MULTIPLE_PERMISSIONS = 1;
    private int REQUEST_CODE_GPS_PERMISSIONS = 2;
    ASTProgressBar dotDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_app_tour);
        if (CompatibilityUtility.isTablet(AppTourActivity.this)) {
            CheckOrientation = true;
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        } else {
            CheckOrientation = false;
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        }
        dotDialog = new ASTProgressBar(AppTourActivity.this);
       /* SharedPreferences prefs = getSharedPreferences("AddHomePreferences", Context.MODE_PRIVATE);
        prefs.edit().clear().commit();*/
        runTimePermission();
        init();
    }

    //get id's
    public void init() {
        tourImageList = new ArrayList<Integer>();
        viewPager = (ViewPager) findViewById(R.id.pager);
        btnNext = (TextView) findViewById(R.id.btn_next);
        btnFinish = (TextView) findViewById(R.id.btn_finish);
        pager_indicator = (LinearLayout) findViewById(R.id.viewPagerCountDots);
        btnNext.setOnClickListener(this);
        btnFinish.setOnClickListener(this);
        setValueIntoList();
    }

    //set value based on tab or phone
    private void setValueIntoList() {

        tourImageList.add(R.layout.welcome_tour1);
        tourImageList.add(R.layout.welcome_tour2);
        tourImageList.add(R.layout.welcome_tour3);
        tourImageList.add(R.layout.welcome_tour4);
        mAdapter = new AppTourPagerAdapter(AppTourActivity.this, tourImageList, CheckOrientation);
        viewPager.setAdapter(mAdapter);
        viewPager.setCurrentItem(0);
        viewPager.addOnPageChangeListener(this);
        setUiPageViewController();
    }

    private void setUiPageViewController() {
        dotsCount = mAdapter.getCount();
        dots = new TextView[dotsCount];

        for (int i = 0; i < dotsCount; i++) {
            dots[i] = new TextView(this);
            dots[i].setText(Html.fromHtml("&#8226;"));
            dots[i].setTextSize(40);
            dots[i].setTextColor(Color.parseColor("#b3b3b3"));
            dots[i].setBackgroundColor(Color.TRANSPARENT);
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.WRAP_CONTENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT);

            params.setMargins(8, 0, 8, 8);
            pager_indicator.addView(dots[i], params);
        }
        dots[0].setTextColor(Color.parseColor("#F75C1E"));
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_next:
                viewPager.setCurrentItem((viewPager.getCurrentItem() < dotsCount) ? viewPager.getCurrentItem() + 1 : 0); //for next page show
                //  navigate();
                break;
            case R.id.btn_finish:
                navigate();
                break;
        }
    }

    //navigate to screen
    private void navigate() {
        SwitchDBHelper switchDBHelper = new SwitchDBHelper(AppTourActivity.this);
        ArrayList<Data> userData = switchDBHelper.getAllUserInfoList();
        boolean sflag = false;
        if (userData != null && userData.size() > 0) {
            for (Data data : userData) {
                if (data.getIs_home_available() == 1) {
                    sflag = true;
                } else {
                    sflag = false;
                }
            }
            Intent intent;
            if (sflag) {
                intent = new Intent(AppTourActivity.this, MainActivity.class);
            } else {
                intent = new Intent(AppTourActivity.this, CreateFirstTimePostActivity.class);
            }
            // intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
        } else {
            Intent intent = new Intent(AppTourActivity.this, SigninActivity.class);
            startActivity(intent);
        }

    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {
        for (int i = 0; i < dotsCount; i++) {
            dots[i].setTextColor(Color.parseColor("#b3b3b3"));
        }
        dots[position].setTextColor(Color.parseColor("#F75C1E"));
        if (position + 1 == dotsCount) {
            btnNext.setVisibility(View.GONE);
            btnFinish.setVisibility(View.VISIBLE);

        } else {
            btnNext.setVisibility(View.VISIBLE);
            btnFinish.setVisibility(View.GONE);
        }
    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }

    private void runTimePermission() {
        permissionUtils = new PermissionUtils(AppTourActivity.this);

        permissions.add(android.Manifest.permission.ACCESS_FINE_LOCATION);
        permissions.add(android.Manifest.permission.ACCESS_COARSE_LOCATION);
        permissions.add(android.Manifest.permission.READ_EXTERNAL_STORAGE);
        permissions.add(android.Manifest.permission.WRITE_EXTERNAL_STORAGE);
        permissions.add(android.Manifest.permission.CAMERA);
        permissions.add(android.Manifest.permission.WAKE_LOCK);
        permissions.add(Manifest.permission.SEND_SMS);
        permissions.add(android.Manifest.permission.MODIFY_AUDIO_SETTINGS);
        permissions.add(Manifest.permission.ACCESS_NOTIFICATION_POLICY);


        permissionUtils.check_permission(permissions, "Location,Storage Services Permissions are required for this App.", REQUEST_CODE_ASK_MULTIPLE_PERMISSIONS);
    }

    @Override
    public void PermissionGranted(int request_code) {
        checkGpsEnable();
        //startLocationAlarmService();
    }

    @Override
    public void PartialPermissionGranted(int request_code, ArrayList<String> granted_permissions) {
        Log.i("PERMISSION PARTIALLY", "GRANTED");
        finish();
    }

    @Override
    public void PermissionDenied(int request_code) {
        Log.i("PERMISSION", "DENIED");
        finish();
    }

    @Override
    public void NeverAskAgain(int request_code) {
        Log.i("PERMISSION", "NEVER ASK AGAIN");
        neverAskAgainAlert();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE_ASK_MULTIPLE_PERMISSIONS) {
            permissionUtils.check_permission(permissions, "Location, Phone and Storage Services Permissions are required for this App.", REQUEST_CODE_ASK_MULTIPLE_PERMISSIONS);
        }
        if (requestCode == REQUEST_CODE_GPS_PERMISSIONS) {
            checkGpsEnable();
        }
    }

    private void neverAskAgainAlert() {
        //Previously Permission Request was cancelled with 'Dont Ask Again',
        // Redirect to Settings after showing Information about why you need the permission
        android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(AppTourActivity.this);
        builder.setTitle("Need Multiple Permissions");
        builder.setCancelable(false);
        builder.setMessage("Location, Phone and Storage Services Permissions are required for this App.");
        builder.setPositiveButton("Grant", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
                Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                Uri uri = Uri.fromParts("package", getPackageName(), null);
                intent.setData(uri);
                startActivityForResult(intent, REQUEST_CODE_ASK_MULTIPLE_PERMISSIONS);
            }
        });
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
                finish();
            }
        });
        builder.show();
    }

    private void buildAlertMessageNoGps() {
        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Your GPS seems to be disabled, do you want to enable it?")
                .setCancelable(false)
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    public void onClick(final DialogInterface dialog, final int id) {
                        startActivityForResult(new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS), REQUEST_CODE_GPS_PERMISSIONS);
                    }
                })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    public void onClick(final DialogInterface dialog, final int id) {
                        dialog.cancel();
                        checkGpsEnable();
                    }
                });
        final AlertDialog alert = builder.create();
        alert.show();
    }

    //    check gps enable in device or not
    private void checkGpsEnable() {
        try {
            boolean isGPSEnabled = false;
            boolean isNetworkEnabled = false;
            final LocationManager locationManager = (LocationManager) getApplicationContext().getSystemService(LOCATION_SERVICE);
            // getting GPS status
            isGPSEnabled = locationManager
                    .isProviderEnabled(LocationManager.GPS_PROVIDER);

            // getting network status
            isNetworkEnabled = locationManager
                    .isProviderEnabled(LocationManager.NETWORK_PROVIDER);

            if (!isGPSEnabled && !isNetworkEnabled) {
                buildAlertMessageNoGps();
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void getHowItWorkData() {
        if (Utility.isOnline(AppTourActivity.this)) {
            String serviceURL = Contants.BASE_URL + Contants.Howitwork;
            JSONObject object = new JSONObject();
            try {
                object.put("api_key", Contants.API_KEY);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            ServiceCaller serviceCaller = new ServiceCaller(AppTourActivity.this);
            serviceCaller.CallCommanServiceMethod(serviceURL, object, "getHowItWorkData", new IAsyncWorkCompletedCallback() {
                @Override
                public void onDone(String result, boolean isComplete) {
                    if (isComplete) {
                        parseHowData(result);
                    } else {
                        if (dotDialog.isShowing()) {
                            dotDialog.dismiss();
                        }
                        Utility.alertForErrorMessage(Contants.Error, AppTourActivity.this);
                    }
                }
            });
        } else {
            Utility.alertForErrorMessage(Contants.OFFLINE_MESSAGE, AppTourActivity.this);//off line msg....
        }
    }

    public void parseHowData(String result) {
        if (result != null) {
            final ServiceContentData serviceData = new Gson().fromJson(result, ServiceContentData.class);
            if (serviceData != null) {
                if (serviceData.isSuccess()) {
                    if (serviceData.getData() != null) {

                    } else {
                        Utility.showToast(AppTourActivity.this, serviceData.getMsg());
                    }
                } else {
                    if (dotDialog.isShowing()) {
                        dotDialog.dismiss();
                    }
                    Utility.showToast(AppTourActivity.this, serviceData.getMsg());
                }
            }
        }
    }
}
