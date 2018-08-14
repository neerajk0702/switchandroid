package com.kredivation.switchland.activity;

import android.content.pm.ActivityInfo;
import android.graphics.Typeface;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Html;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.SeekBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.kredivation.switchland.R;
import com.kredivation.switchland.utilities.CompatibilityUtility;
import com.kredivation.switchland.utilities.FontManager;

public class MyProfileFilterActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_profile_filter);
        chechPortaitAndLandSacpe();//chech Portait And LandSacpe Orientation
        initView();
    }
    //chech Portait And LandSacpe Orientation
    public void chechPortaitAndLandSacpe() {
        if (CompatibilityUtility.isTablet(MyProfileFilterActivity.this)) {

            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        } else {

            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        }

    }
    private void initView() {
        Typeface materialdesignicons_font = FontManager.getFontTypefaceMaterialDesignIcons(this, "fonts/materialdesignicons-webfont.otf");
        TextView locationIcon = (TextView) findViewById(R.id.locationIcon);
        locationIcon.setTypeface(materialdesignicons_font);
        locationIcon.setText(Html.fromHtml("&#xf34e;"));
        TextView calIcon = (TextView) findViewById(R.id.calIcon);
        calIcon.setTypeface(materialdesignicons_font);
        calIcon.setText(Html.fromHtml("&#xf0ed;"));
        TextView starticon = (TextView) findViewById(R.id.starticon);
        starticon.setTypeface(materialdesignicons_font);
        starticon.setText(Html.fromHtml("&#xf0ed;"));
        TextView endIcon = (TextView) findViewById(R.id.endIcon);
        endIcon.setTypeface(materialdesignicons_font);
        endIcon.setText(Html.fromHtml("&#xf0ed;"));

        SeekBar seekBar = (SeekBar)findViewById(R.id.seekBar2);

        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener(){

            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                Toast.makeText(MyProfileFilterActivity.this,
                        "Seekbar vale "+i, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
                Toast.makeText(MyProfileFilterActivity.this,
                        "Seekbar touch started", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                Toast.makeText(MyProfileFilterActivity.this,
                        "Seekbar touch stopped", Toast.LENGTH_SHORT).show();
            }
        });

        Spinner cityspinner = (Spinner) findViewById(R.id.cityspinner);
        Spinner countryspinner = (Spinner) findViewById(R.id.countryspinner);

        String country_array[] = {"Avon", "Bedfordshire","Berkshire","Buckinghamshire","Bristol","Cambridgeshire","Cheshire","Cleveland"};
        String city_array[] = {"London", "Manchester", "Leeds","Birmingham","Liverpool"};

        ArrayAdapter<String> postalAdapter = new ArrayAdapter<String>(this, R.layout.spinner_row, city_array);
        cityspinner.setAdapter(postalAdapter);
      //  postalspinner.setOnItemSelectedListener(new MyOnItemSelectedListener());

        ArrayAdapter<String> roladapter = new ArrayAdapter<String>(this, R.layout.spinner_row, country_array);
        countryspinner.setAdapter(roladapter);
        //rolspinner.setOnItemSelectedListener(new MyOnItemSelectedListener());
    }
}
