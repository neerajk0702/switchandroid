package com.kredivation.switchland.activity;

import android.Manifest;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Rect;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Settings;

import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

import com.google.android.material.navigation.NavigationView;

import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.view.Menu;
import android.view.MenuItem;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.kredivation.switchland.R;
import com.kredivation.switchland.adapters.CustomDrawerAdapter;
import com.kredivation.switchland.database.SwitchDBHelper;
import com.kredivation.switchland.fragment.TinderMainFragment;
import com.kredivation.switchland.model.Data;
import com.kredivation.switchland.model.DrawerItem;
import com.kredivation.switchland.runtimepermission.PermissionResultCallback;
import com.kredivation.switchland.runtimepermission.PermissionUtils;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class DashboardActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener,
        ActivityCompat.OnRequestPermissionsResultCallback, PermissionResultCallback {
    DrawerLayout drawer;

    ArrayList<String> permissions = new ArrayList<>();
    PermissionUtils permissionUtils;
    private int REQUEST_CODE_ASK_MULTIPLE_PERMISSIONS = 1;
    private int REQUEST_CODE_GPS_PERMISSIONS = 2;
    String homeFilter;
    TextView loginUsrName, loginUserEmailId;
    ImageView sliderProfileImg;
    ListView menuList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        runTimePermission();
        drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.getDrawerArrowDrawable().setColor(getResources().getColor(R.color.Black_A));
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        homeFilter = getIntent().getStringExtra("HomeFilter");//come from Myprofilefilter, Travelroutin screen
        View headerLayout = navigationView.getHeaderView(0);
        loginUsrName = findViewById(R.id.loginUsrName);
        loginUserEmailId = findViewById(R.id.loginUserEmailId);
        sliderProfileImg = findViewById(R.id.proImage);
        menuList = findViewById(R.id.menuList);
        setUpDashboardFragment();
        // setProfileInfo();
        setCoustomNavMenu();
    }

    private void setCoustomNavMenu() {
        ArrayList<DrawerItem> dataList = new ArrayList<DrawerItem>();
        dataList.add(new DrawerItem("Home Screen", R.drawable.avter, 1));
        dataList.add(new DrawerItem("My Home", R.drawable.avter, 2));
        dataList.add(new DrawerItem("My Choices", R.drawable.avter, 3));
        dataList.add(new DrawerItem("Liked My Choices", R.drawable.avter, 4));
        dataList.add(new DrawerItem("Notification", R.drawable.avter, 5));
        dataList.add(new DrawerItem("My Profile", R.drawable.avter, 6));
        dataList.add(new DrawerItem("Setting", R.drawable.avter, 7));
        dataList.add(new DrawerItem("How it works", R.drawable.avter, 8));
        dataList.add(new DrawerItem("Invite Friends", R.drawable.avter, 9));
        dataList.add(new DrawerItem("Help and Support", R.drawable.avter, 10));
        dataList.add(new DrawerItem("Policies", R.drawable.avter, 11));
        dataList.add(new DrawerItem("Terms and Condition", R.drawable.avter, 12));
        menuList.setAdapter(new CustomDrawerAdapter(
                DashboardActivity.this,
                R.layout.custom_drawer_item,
                dataList));
    }


    private void setProfileInfo() {
        SwitchDBHelper switchDBHelper = new SwitchDBHelper(DashboardActivity.this);
        ArrayList<Data> userData = switchDBHelper.getAllUserInfoList();
        if (userData != null && userData.size() > 0) {
            for (Data data : userData) {
                if (data.getFull_name() != null && !data.getFull_name().equals("")) {
                    loginUsrName.setText(data.getFull_name());
                } else {
                    loginUsrName.setText(data.getFirst_name() + " " + data.getLast_name());
                }
                loginUserEmailId.setText(data.getEmail());
                Picasso.with(DashboardActivity.this).load(data.getProfile_image()).resize(55, 55).placeholder(R.drawable.userimage).into(sliderProfileImg);
            }
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        setProfileInfo();
    }

    //open default fragment
    private void setUpDashboardFragment() {
        // Fragment fragment = DashboardFragment.newInstance(homeFilter, "");
        Fragment fragment = TinderMainFragment.newInstance(homeFilter, "");
        moveFragment(fragment);
    }

    private void moveFragment(Fragment fragment) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction()
                .replace(R.id.container, fragment)
                //.addToBackStack(null)
                .commit();
    }

    //hide navigation view
    public void NavHide() {
        drawer.closeDrawer(GravityCompat.START);
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.dashboard, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.notification) {
            startActivity(new Intent(DashboardActivity.this, NotificationActivity.class));
        } else if (id == R.id.how) {
            Intent intent = new Intent(DashboardActivity.this, AppTourActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
        } else if (id == R.id.invite) {
            inviteFriend();
        } else if (id == R.id.help) {
            Intent myintent = new Intent(DashboardActivity.this, HelpActivity.class);
            startActivity(myintent);
        } else if (id == R.id.Policies) {
            // startActivity(new Intent(DashboardActivity.this, MyProfileFilterActivity.class));
        } else if (id == R.id.Terms) {

        } else if (id == R.id.home) {
            Intent intent = new Intent(DashboardActivity.this, DashboardActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
        } else if (id == R.id.myhome) {
            Intent myintent = new Intent(DashboardActivity.this, MyHomeActivity.class);
            startActivity(myintent);
        } else if (id == R.id.Mychoices) {
            Intent choicesintent = new Intent(DashboardActivity.this, MyChoicesActivity.class);
            startActivity(choicesintent);
        } else if (id == R.id.likedmychoice) {
            Intent likeintent = new Intent(DashboardActivity.this, MyLikedChoicesActivity.class);
            startActivity(likeintent);
        } else if (id == R.id.Myprofile) {
            Intent editintent = new Intent(DashboardActivity.this, EditProfileActivity.class);
            startActivity(editintent);
        } else if (id == R.id.setting) {
            Intent intent = new Intent(DashboardActivity.this, SettingActivity.class);
            startActivity(intent);
        }
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    private void inviteFriend() {
        try {
            Intent i = new Intent(Intent.ACTION_SEND);
            i.setType("text/plain");
            i.putExtra(Intent.EXTRA_SUBJECT, "Switch");
            String strShareMessage = "\nLet me recommend you this application\n\n";
            strShareMessage = strShareMessage + "https://play.google.com/store/apps/details?id=" + getPackageName();
            Uri screenshotUri = Uri.parse("android.resource://packagename/drawable/ic_switchland_logo");
            i.setType("image/png");
            i.putExtra(Intent.EXTRA_STREAM, screenshotUri);
            i.putExtra(Intent.EXTRA_TEXT, strShareMessage);
            startActivity(Intent.createChooser(i, "Share via"));
        } catch (Exception e) {
            //e.toString();
        }
    }

    //for hid keyboard when tab outside edittext box
    @Override
    public boolean dispatchTouchEvent(MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_DOWN) {
            View v = getCurrentFocus();
            if (v instanceof EditText) {
                Rect outRect = new Rect();
                v.getGlobalVisibleRect(outRect);
                if (!outRect.contains((int) event.getRawX(), (int) event.getRawY())) {
                    v.clearFocus();
                    InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
                }
            }
        }
        return super.dispatchTouchEvent(event);
    }

    private void runTimePermission() {
        permissionUtils = new PermissionUtils(DashboardActivity.this);

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
        android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(DashboardActivity.this);
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
}
