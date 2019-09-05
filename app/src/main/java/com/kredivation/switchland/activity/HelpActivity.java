package com.kredivation.switchland.activity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.pm.ActivityInfo;
import android.graphics.Rect;
import android.graphics.Typeface;
import android.os.AsyncTask;
import com.google.android.material.textfield.TextInputLayout;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.appcompat.widget.Toolbar;
import android.text.Html;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.kredivation.switchland.R;
import com.kredivation.switchland.mail.Mail;
import com.kredivation.switchland.utilities.CompatibilityUtility;
import com.kredivation.switchland.utilities.FontManager;

public class HelpActivity extends AppCompatActivity {
    private TextInputLayout email_layout, name_layout, subject_layout, message_layout;
    private EditText email, name, subject, message;
    private String emailStr, nameStr, subjectStr, messageStr;
    private Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_help);
        chechPortaitAndLandSacpe();//chech Portait And LandSacpe Orientation
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        initView();
    }

    //chech Portait And LandSacpe Orientation
    public void chechPortaitAndLandSacpe() {
        if (CompatibilityUtility.isTablet(HelpActivity.this)) {
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        } else {

            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        }
    }

    private void initView() {
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
        email_layout = findViewById(R.id.email_layout);
        email = findViewById(R.id.email);
        name_layout = findViewById(R.id.name_layout);
        name = findViewById(R.id.name);
        subject_layout = findViewById(R.id.subject_layout);
        subject = findViewById(R.id.subject);
        message_layout = findViewById(R.id.message_layout);
        message = findViewById(R.id.message);
        Button loginText = findViewById(R.id.loginText);
        loginText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isValidate()) {
                    new SendMail().execute("");
                }
            }
        });
    }

    // ----validation -----
    private boolean isValidate() {
        String emailRegex = "[A-Z0-9a-z._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,4}";
        emailStr = email.getText().toString();
        nameStr = name.getText().toString();
        subjectStr = subject.getText().toString();
        messageStr = message.getText().toString();
        if (nameStr.length() == 0) {
            name_layout.setError("Please Enter your Name");
            requestFocus(name);
            return false;
        } else if (emailStr.length() == 0) {
            email_layout.setError("Please Enter your Email Id");
            requestFocus(email);
            return false;
        } else if (!emailStr.matches(emailRegex)) {
            email_layout.setError("Please Enter valid Email Id");
            requestFocus(email);
            return false;
        } else if (subjectStr.length() == 0) {
            subject_layout.setError("Please Enter your Subject");
            requestFocus(subject);
            return false;
        } else if (messageStr.length() == 0) {
            message_layout.setError("Please Enter your Message");
            requestFocus(message);
            return false;
        } else {
            email_layout.setErrorEnabled(false);
            message_layout.setErrorEnabled(false);
            subject_layout.setErrorEnabled(false);
            name_layout.setErrorEnabled(false);
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

    private class SendMail extends AsyncTask<String, Integer, Boolean> {

        private ProgressDialog progressDialog;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog = ProgressDialog.show(HelpActivity.this, "Please wait", "Your Message Sending.", true, false);
        }

        @Override
        protected void onPostExecute(Boolean aVoid) {
            super.onPostExecute(aVoid);
            try {
                if (aVoid) {
                    Toast.makeText(HelpActivity.this, "Message sent successfully.", Toast.LENGTH_LONG).show();
                } else {
                    Toast.makeText(HelpActivity.this, "Message not sent successfully.", Toast.LENGTH_LONG).show();
                }
            } catch (Exception e) {
                Log.e("MailApp", "Could not send email", e);
            }
            progressDialog.dismiss();
        }

        protected Boolean doInBackground(String... params) {
            boolean doneflag = false;
            Mail m = new Mail("amarswitch@gmail.com", "Amar@0702");

            String[] toArr = {emailStr};//{"neerajk0702@gmail.com", "89neerajsingh@gmail.com"};
            m.setTo(toArr);
            m.setFrom("amarswitch@gmail.com");
            m.setSubject(subjectStr);
            m.setBody(messageStr);
            try {
                if (m.send()) {
                    doneflag = true;
                } else {
                    doneflag = false;
                }
            } catch (Exception e) {
                //Log.e("MailApp", "Could not send email", e);
            }
            return doneflag;
        }
    }
}
