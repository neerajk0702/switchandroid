package com.kredivation.switchland.activity;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import android.view.View;
import android.widget.Button;

import com.kredivation.switchland.R;

public class LoginRegisterActivity extends AppCompatActivity implements View.OnClickListener {

    private Button loginText, signTxt;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_register);
        init();
    }


    public void init() {
        loginText = findViewById(R.id.loginText);
        signTxt = findViewById(R.id.signTxt);
        loginText.setOnClickListener(this);
        signTxt.setOnClickListener(this);

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.loginText:
                Intent intent;
                intent = new Intent(LoginRegisterActivity.this, SigninActivity.class);
                startActivity(intent);
                break;
            case R.id.signTxt:

        }
    }
}
