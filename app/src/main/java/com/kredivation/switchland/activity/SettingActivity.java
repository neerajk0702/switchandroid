package com.kredivation.switchland.activity;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.Typeface;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.kredivation.switchland.R;
import com.kredivation.switchland.database.SwitchDBHelper;
import com.kredivation.switchland.utilities.CompatibilityUtility;
import com.kredivation.switchland.utilities.FontManager;
import com.kredivation.switchland.utilities.SwitchViewPager;
import com.kredivation.switchland.utilities.Utility;

public class SettingActivity extends AppCompatActivity implements View.OnClickListener {
    private Toolbar toolbar;

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
        LinearLayout filterLayout = findViewById(R.id.filterLayout);
        filterLayout.setOnClickListener(this);
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
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.logoutLayout:
                Utility.showToast(SettingActivity.this, "Logout Successfully");
                SwitchDBHelper switchDBHelper = new SwitchDBHelper(SettingActivity.this);
                switchDBHelper.deleteAllRows("userInfo");
                Intent intent = new Intent(SettingActivity.this, AppTourActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
                break;
            case R.id.filterLayout:
                Intent filterintent = new Intent(SettingActivity.this, MyProfileFilterActivity.class);
                startActivity(filterintent);
                break;
            case R.id.changepassLayout:
                Intent passintent = new Intent(SettingActivity.this, ChangePasswordActivity.class);
                startActivity(passintent);
                break;
        }
    }
}
