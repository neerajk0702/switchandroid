package com.kredivation.switchland.activity;

import android.content.Context;
import android.content.pm.ActivityInfo;
import android.graphics.Rect;
import android.graphics.Typeface;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.google.gson.Gson;
import com.kredivation.switchland.R;
import com.kredivation.switchland.database.SwitchDBHelper;
import com.kredivation.switchland.fragment.ChatListFragment;
import com.kredivation.switchland.framework.IAsyncWorkCompletedCallback;
import com.kredivation.switchland.framework.ServiceCaller;
import com.kredivation.switchland.model.Data;
import com.kredivation.switchland.model.ServiceContentData;
import com.kredivation.switchland.utilities.ASTProgressBar;
import com.kredivation.switchland.utilities.CompatibilityUtility;
import com.kredivation.switchland.utilities.Contants;
import com.kredivation.switchland.utilities.FontManager;
import com.kredivation.switchland.utilities.Utility;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class ChangePasswordActivity extends AppCompatActivity implements View.OnClickListener {
    private Toolbar toolbar;
    private EditText password, newpassword, renewpassword;
    String passwordStr, newpasswordStr, renewpasswordStr;
    private TextInputLayout password_layout, newpassword_layout, renewpassword_layout;
    private String userId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_password);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if (CompatibilityUtility.isTablet(ChangePasswordActivity.this)) {
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


        password_layout = findViewById(R.id.password_layout);
        password = findViewById(R.id.password);
        newpassword_layout = findViewById(R.id.newpassword_layout);
        newpassword = findViewById(R.id.newpassword);
        renewpassword_layout = findViewById(R.id.renewpassword_layout);
        renewpassword = findViewById(R.id.renewpassword);
        Button loginText = findViewById(R.id.loginText);
        loginText.setOnClickListener(this);

        SwitchDBHelper switchDBHelper = new SwitchDBHelper(ChangePasswordActivity.this);
        ArrayList<Data> userData = switchDBHelper.getAllUserInfoList();
        if (userData != null && userData.size() > 0) {
            for (Data data : userData) {
                userId = data.getId();
            }
        }
    }

    // ----validation -----
    private boolean isValidate() {
        String emailRegex = "[A-Z0-9a-z._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,4}";
        passwordStr = password.getText().toString();
        newpasswordStr = newpassword.getText().toString();
        renewpasswordStr = renewpassword.getText().toString();

        if (passwordStr.length() == 0) {
            password_layout.setError("Please Enter your Old Password");
            requestFocus(password);
            return false;
        } else if (newpasswordStr.length() == 0) {
            newpassword_layout.setError("Please Enter New Password");
            requestFocus(newpassword);
            return false;
        } else if (renewpassword.length() == 0) {
            renewpassword_layout.setError("Please Enter Confirm Password");
            requestFocus(renewpassword);
            return false;
        } else if (!renewpassword.equals(newpasswordStr)) {
            renewpassword_layout.setError("New Password and Confirm password not matched!");
            requestFocus(renewpassword);
            return false;
        } else {
            password_layout.setErrorEnabled(false);
            newpassword_layout.setErrorEnabled(false);
            renewpassword_layout.setErrorEnabled(false);
        }
        return true;
    }

    private void requestFocus(View view) {
        if (view.requestFocus()) {
            getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.loginText:
                if (isValidate()) {
                    changePassword();
                }
                break;
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

    private void changePassword() {
        if (Utility.isOnline(ChangePasswordActivity.this)) {
            final ASTProgressBar dotDialog = new ASTProgressBar(ChangePasswordActivity.this);
            dotDialog.show();
            JSONObject object = new JSONObject();
            try {
                object.put("api_key", Contants.API_KEY);
                object.put("old_password", passwordStr);
                object.put("new_password", newpasswordStr);
                object.put("confirm_password", renewpasswordStr);
                object.put("user_id", userId);
            } catch (JSONException e) {
                e.printStackTrace();
            }

            String serviceURL = Contants.BASE_URL + Contants.ChangePassword;

            ServiceCaller serviceCaller = new ServiceCaller(ChangePasswordActivity.this);
            serviceCaller.CallCommanServiceMethod(serviceURL, object, "ChangePassword", new IAsyncWorkCompletedCallback() {
                @Override
                public void onDone(String result, boolean isComplete) {
                    if (isComplete) {
                        final ServiceContentData serviceData = new Gson().fromJson(result, ServiceContentData.class);
                        if (serviceData != null) {
                            if (serviceData.isSuccess()) {
                                Utility.showToast(ChangePasswordActivity.this, "Your Password changed successfully.");
                                finish();
                            } else {
                                Utility.showToast(ChangePasswordActivity.this, serviceData.getMsg());
                            }
                        } else {
                            Utility.showToast(ChangePasswordActivity.this, Contants.Error);
                        }
                    } else {
                        Utility.alertForErrorMessage(Contants.Error, ChangePasswordActivity.this);
                    }
                    if (dotDialog.isShowing()) {
                        dotDialog.dismiss();
                    }
                }
            });
        } else {
            Utility.alertForErrorMessage(Contants.OFFLINE_MESSAGE, ChangePasswordActivity.this);//off line msg....
        }
    }

}
