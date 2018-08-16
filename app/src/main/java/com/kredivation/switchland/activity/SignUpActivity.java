package com.kredivation.switchland.activity;

import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.Rect;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.google.gson.Gson;
import com.kredivation.switchland.R;
import com.kredivation.switchland.framework.IAsyncWorkCompletedCallback;
import com.kredivation.switchland.framework.ServiceCaller;
import com.kredivation.switchland.model.ContentData;
import com.kredivation.switchland.model.ServiceContentData;
import com.kredivation.switchland.utilities.ASTProgressBar;
import com.kredivation.switchland.utilities.CompatibilityUtility;
import com.kredivation.switchland.utilities.Contants;
import com.kredivation.switchland.utilities.Utility;

import org.json.JSONException;
import org.json.JSONObject;

public class SignUpActivity extends AppCompatActivity implements View.OnClickListener{

    private TextInputLayout user_layout, email_layout, password_layout, cpassword_layout, first_layout, last_layout;
    private EditText user, email, password, cpassword, lastName, firstName;
    private String userStr, emailStr, passwordStr, cpasswordStr, firstNameStr, lastNameStr;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        chechPortaitAndLandSacpe();//chech Portait And LandSacpe Orientation
        initView();
    }

    public void chechPortaitAndLandSacpe() {
        if (CompatibilityUtility.isTablet(SignUpActivity.this)) {
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        } else {
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        }

    }

    private void initView() {
        TextView signIn = findViewById(R.id.signIn);
        signIn.setOnClickListener(this);
        Button signup = findViewById(R.id.signup);
        signup.setOnClickListener(this);
        user_layout = findViewById(R.id.user_layout);
        user = findViewById(R.id.user);
        email_layout = findViewById(R.id.email_layout);
        email = findViewById(R.id.email);
        password_layout = findViewById(R.id.password_layout);
        password = findViewById(R.id.password);
        cpassword_layout = findViewById(R.id.cpassword_layout);
        cpassword = findViewById(R.id.cpassword);
        first_layout = findViewById(R.id.first_layout);
        firstName = findViewById(R.id.firstName);
        last_layout = findViewById(R.id.last_layout);
        lastName = findViewById(R.id.lastName);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.signIn:
                finish();
                break;
            case R.id.signup:
                if (isValidate()) {
                    signUp();
                }
                break;
        }
    }

    // ----validation -----
    private boolean isValidate() {
        String emailRegex = "[A-Z0-9a-z._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,4}";
        firstNameStr = firstName.getText().toString();
        lastNameStr = lastName.getText().toString();
        userStr = user.getText().toString();
        emailStr = email.getText().toString();
        passwordStr = password.getText().toString();
        cpasswordStr = cpassword.getText().toString();
        if (firstNameStr.length() == 0) {
            first_layout.setError("Please Enter First Name");
            requestFocus(firstName);
            return false;
        } else if (lastNameStr.length() == 0) {
            last_layout.setError("Please Enter Last Name");
            requestFocus(lastName);
            return false;
        } else if (userStr.length() == 0) {
            user_layout.setError("Please Enter User Name");
            requestFocus(user);
            return false;
        } else if (emailStr.length() == 0) {
            email_layout.setError("Please Enter Email Id");
            requestFocus(email);
            return false;
        } else if (emailStr.equals(" ")) {
            email_layout.setError("No Spaces Allowed");
            requestFocus(email);
            return false;
        } else if (!emailStr.matches(emailRegex)) {
            email_layout.setError("Please Enter Valid Email Id");
            requestFocus(email);
            return false;
        } else if (passwordStr.length() == 0) {
            password_layout.setError("Please Enter Password");
            requestFocus(password);
            return false;
        } else if (cpasswordStr.length() == 0) {
            cpassword_layout.setError("Please Email Confirm Password");
            requestFocus(cpassword);
            return false;
        } else if (!passwordStr.equals(cpasswordStr)) {
            Utility.showToast(SignUpActivity.this,"Password and Confirm Password not Matched!");
            return false;
        } else {
            first_layout.setErrorEnabled(false);
            last_layout.setErrorEnabled(false);
            user_layout.setErrorEnabled(false);
            email_layout.setErrorEnabled(false);
            password_layout.setErrorEnabled(false);
            cpassword_layout.setErrorEnabled(false);
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

    private void signUp() {
        if (Utility.isOnline(SignUpActivity.this)) {
            final ASTProgressBar dotDialog = new ASTProgressBar(SignUpActivity.this);
            dotDialog.show();
            JSONObject object = new JSONObject();
            try {

                object.put("api_key", Contants.API_KEY);
                object.put("first_name", firstNameStr);
                object.put("last_name", lastNameStr);
                object.put("username", userStr);
                object.put("email", emailStr);
                object.put("password", passwordStr);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            String serviceURL = Contants.BASE_URL + Contants.Registration;

            ServiceCaller serviceCaller = new ServiceCaller(SignUpActivity.this);
            serviceCaller.CallCommanServiceMethod(serviceURL, object, "SignUp", new IAsyncWorkCompletedCallback() {
                @Override
                public void onDone(String result, boolean isComplete) {
                    if (isComplete) {
                        final ContentData serviceData = new Gson().fromJson(result, ContentData.class);
                        if (serviceData != null) {
                            if (serviceData.isSuccess()) {
                                Utility.showToast(SignUpActivity.this,serviceData.getMsg());
                                finish();
                            } else {
                                Utility.showToast(SignUpActivity.this,serviceData.getMsg());
                            }
                        } else {
                            Utility.showToast(SignUpActivity.this,Contants.Error);
                        }
                    } else {
                        Utility.alertForErrorMessage(Contants.Error, SignUpActivity.this);
                    }
                    if (dotDialog.isShowing()) {
                        dotDialog.dismiss();
                    }
                }
            });
        } else {
            Utility.alertForErrorMessage(Contants.OFFLINE_MESSAGE, SignUpActivity.this);//off line msg....
        }
    }
}
