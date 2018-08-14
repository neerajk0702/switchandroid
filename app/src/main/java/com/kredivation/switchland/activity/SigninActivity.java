package com.kredivation.switchland.activity;

import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.Rect;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
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
import com.kredivation.switchland.framework.IAsyncWorkCompletedCallback;
import com.kredivation.switchland.framework.ServiceCaller;
import com.kredivation.switchland.model.ServiceContentData;
import com.kredivation.switchland.utilities.ASTProgressBar;
import com.kredivation.switchland.utilities.CompatibilityUtility;
import com.kredivation.switchland.utilities.Contants;
import com.kredivation.switchland.utilities.FontManager;
import com.kredivation.switchland.utilities.Utility;

import org.json.JSONException;
import org.json.JSONObject;


public class SigninActivity extends AppCompatActivity implements View.OnClickListener {


    private TextInputLayout email_layout, password_layout;
    private EditText user, password;
    private String login_user, login_password;
    private Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signin);
        chechPortaitAndLandSacpe();//chech Portait And LandSacpe Orientation
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        initView();
    }

    //chech Portait And LandSacpe Orientation
    public void chechPortaitAndLandSacpe() {
        if (CompatibilityUtility.isTablet(SigninActivity.this)) {
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        } else {
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        }

    }

    private void initView() {
        Typeface materialdesignicons_font = FontManager.getFontTypefaceMaterialDesignIcons(this, "fonts/materialdesignicons-webfont.otf");
        //userIcon = (TextView) findViewById(R.id.userIcon);
        //userIcon.setTypeface(materialdesignicons_font);
        // userIcon.setText(Html.fromHtml("&#xf1f0;"));
        email_layout = (TextInputLayout) findViewById(R.id.email_layout);
        user = (EditText) findViewById(R.id.user);
        password_layout = (TextInputLayout) findViewById(R.id.password_layout);
        password = (EditText) findViewById(R.id.password);
        Button loginText = (Button) findViewById(R.id.loginText);
        loginText.setOnClickListener(this);
        //userIcon = (TextView) findViewById(R.id.userIcon);
        //userIcon.setTypeface(materialdesignicons_font);
        // userIcon.setText(Html.fromHtml("&#xf1f0;"));
        TextView back = (TextView) toolbar.findViewById(R.id.back);
        back.setTypeface(materialdesignicons_font);
        back.setText(Html.fromHtml("&#xf30d;"));
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        TextView signup = this.findViewById(R.id.signup);
        signup.setOnClickListener(this);
        TextView forgotPassword = findViewById(R.id.forgotPassword);
        forgotPassword.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.loginText:
                if (isValidate()) {
                    login();
                }
                break;
            case R.id.signup:
                Intent intent2 = new Intent(SigninActivity.this, SignUpActivity.class);
                startActivity(intent2);
                break;
            case R.id.forgotPassword:
                Intent forgot = new Intent(SigninActivity.this, ForgotPasswordActivity.class);
                startActivity(forgot);
                break;
        }
    }


    // ----validation -----
    private boolean isValidate() {
        String emailRegex = "[A-Z0-9a-z._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,4}";
        login_user = user.getText().toString();
        login_password = password.getText().toString();

        if (login_user.length() == 0) {
            email_layout.setError("Please Enter User Name");
            requestFocus(user);
            return false;
        } else if (login_user.contains(" ")) {
            email_layout.setError("No Spaces Allowed");
            requestFocus(user);
            return false;
        } else if (login_password.length() == 0) {
            password_layout.setError("Please Enter Password");
            requestFocus(user);
            return false;
        } else {
            email_layout.setErrorEnabled(false);
            password_layout.setErrorEnabled(false);
        }
        return true;
    }

    private void requestFocus(View view) {
        if (view.requestFocus()) {
            getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
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

    private void login() {
        if (Utility.isOnline(SigninActivity.this)) {
            final ASTProgressBar dotDialog = new ASTProgressBar(SigninActivity.this);
            dotDialog.show();
            JSONObject object = new JSONObject();
            try {
                object.put("api_key", Contants.API_KEY);
                object.put("username", login_user);
                object.put("password", login_password);
            } catch (JSONException e) {
                e.printStackTrace();
            }

            String serviceURL = Contants.BASE_URL + Contants.LOGIN_URL;

            ServiceCaller serviceCaller = new ServiceCaller(SigninActivity.this);
            serviceCaller.CallCommanServiceMethod(serviceURL, object, "login", new IAsyncWorkCompletedCallback() {
                @Override
                public void onDone(String result, boolean isComplete) {
                    if (isComplete) {
                        parseLoginServiceData(result);
                    } else {
                        Utility.alertForErrorMessage(Contants.Error, SigninActivity.this);
                    }
                    if (dotDialog.isShowing()) {
                        dotDialog.dismiss();
                    }
                }
            });
        } else {
            Utility.alertForErrorMessage(Contants.OFFLINE_MESSAGE, SigninActivity.this);//off line msg....
        }
    }

    public void parseLoginServiceData(String result) {
        if (result != null) {
            final ServiceContentData serviceData = new Gson().fromJson(result, ServiceContentData.class);
            if (serviceData != null) {
                if (serviceData.isSuccess()) {
                    if (serviceData.getData() != null) {
                        new AsyncTask<Void, Void, Boolean>() {
                            @Override
                            protected Boolean doInBackground(Void... voids) {
                                Boolean flag = false;
                                SwitchDBHelper switchDBHelper = new SwitchDBHelper(SigninActivity.this);
                                switchDBHelper.deleteAllRows("userInfo");
                                switchDBHelper.upsertUserInfoData(serviceData.getData());
                                flag = true;
                                return flag;
                            }

                            @Override
                            protected void onPostExecute(Boolean flag) {
                                super.onPostExecute(flag);
                                if (flag) {
                                    Utility.showToast(SigninActivity.this,"Login Success");
                                    Intent intent = new Intent(SigninActivity.this, DashboardActivity.class);
                                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                                    startActivity(intent);
                                }
                            }
                        }.execute();
                    } else {
                        Utility.showToast(SigninActivity.this,serviceData.getMsg());
                    }
                } else {
                    Utility.showToast(SigninActivity.this,serviceData.getMsg());
                }
            }
        }
    }
}
