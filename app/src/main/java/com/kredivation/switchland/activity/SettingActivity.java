package com.kredivation.switchland.activity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.graphics.Typeface;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.facebook.login.LoginManager;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.kredivation.switchland.R;
import com.kredivation.switchland.database.SwitchDBHelper;
import com.kredivation.switchland.utilities.CompatibilityUtility;
import com.kredivation.switchland.utilities.FontManager;
import com.kredivation.switchland.utilities.SwitchViewPager;
import com.kredivation.switchland.utilities.Utility;

public class SettingActivity extends AppCompatActivity implements View.OnClickListener, GoogleApiClient.OnConnectionFailedListener {
    private Toolbar toolbar;
    private GoogleApiClient mGoogleApiClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if (CompatibilityUtility.isTablet(SettingActivity.this)) {
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        } else {
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        }
        init();
    }

    private void init() {
        LinearLayout changepassLayout = findViewById(R.id.changepassLayout);
        changepassLayout.setOnClickListener(this);
        LinearLayout logoutLayout = findViewById(R.id.logoutLayout);
        logoutLayout.setOnClickListener(this);
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
        TextView howitwork = findViewById(R.id.howitwork);
        howitwork.setOnClickListener(this);
        TextView invitefriend = findViewById(R.id.invitefriend);
        invitefriend.setOnClickListener(this);
        TextView notification = findViewById(R.id.notification);
        notification.setOnClickListener(this);
        //for gmail login
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();

        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .enableAutoManage(this, this)
                .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                .build();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.logoutLayout:
                SharedPreferences prefs = getSharedPreferences("FCMDeviceId", Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = prefs.edit();
                editor.putBoolean("Sendflag", true);//for send device token to server
                editor.commit();
                LoginManager.getInstance().logOut();
                signOut();
                Utility.showToast(SettingActivity.this, "Logout Successfully");
                SwitchDBHelper switchDBHelper = new SwitchDBHelper(SettingActivity.this);
                switchDBHelper.deleteAllRows("userInfo");
                Intent intent = new Intent(SettingActivity.this, SplashScreenActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
                break;
            case R.id.changepassLayout:
                Intent passintent = new Intent(SettingActivity.this, ChangePasswordActivity.class);
                startActivity(passintent);
                break;
            case R.id.howitwork:
                Intent appintent = new Intent(SettingActivity.this, AppTourActivity.class);
                startActivity(appintent);
                break;
            case R.id.invitefriend:
                inviteFriend();
                break;
            case R.id.notification:
                Intent notificationintent = new Intent(SettingActivity.this, NotificationActivity.class);
                startActivity(notificationintent);
                break;
        }
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

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    //gmail logout
    private void signOut() {
        if (mGoogleApiClient.isConnected()) {
            Auth.GoogleSignInApi.signOut(mGoogleApiClient);
            mGoogleApiClient.disconnect();
            mGoogleApiClient.connect();
        }
    }
}
