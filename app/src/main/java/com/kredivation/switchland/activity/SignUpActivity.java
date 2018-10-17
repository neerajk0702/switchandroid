package com.kredivation.switchland.activity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.graphics.Rect;
import android.net.Uri;
import android.os.AsyncTask;
import android.support.annotation.NonNull;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.OptionalPendingResult;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.gson.Gson;
import com.kredivation.switchland.R;
import com.kredivation.switchland.database.SwitchDBHelper;
import com.kredivation.switchland.framework.IAsyncWorkCompletedCallback;
import com.kredivation.switchland.framework.ServiceCaller;
import com.kredivation.switchland.model.ContentData;
import com.kredivation.switchland.model.LikedmychoiceArray;
import com.kredivation.switchland.model.MychoiceArray;
import com.kredivation.switchland.model.MyhomeArray;
import com.kredivation.switchland.model.ServiceContentData;
import com.kredivation.switchland.utilities.ASTProgressBar;
import com.kredivation.switchland.utilities.CompatibilityUtility;
import com.kredivation.switchland.utilities.Contants;
import com.kredivation.switchland.utilities.Utility;

import org.json.JSONException;
import org.json.JSONObject;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.Arrays;

public class SignUpActivity extends AppCompatActivity implements View.OnClickListener, GoogleApiClient.OnConnectionFailedListener {

    private TextInputLayout user_layout, email_layout, password_layout, cpassword_layout, first_layout, last_layout;
    private EditText user, email, password, cpassword, lastName, firstName;
    private String userStr, emailStr, passwordStr, cpasswordStr, firstNameStr, lastNameStr;
    SignInButton btn_gsign_in;
    private static final int RC_SIGN_IN = 7;
    private GoogleApiClient mGoogleApiClient;
    CallbackManager callbackManager;
    LoginButton facebooklogin;
    private static final String EMAIL = "email";
    private static final String USER_POSTS = "user_posts";
    private static final String AUTH_TYPE = "rerequest";

    private String oauth_uid = "";
    private String name = "";
    private String first_name = "";
    private String last_name = "";
    private String socialemail = "";
    private String gender = "";
    private String picture = "";
    private String oauth_provider = "";

    ASTProgressBar dotDialog;
    private int is_home_available;
    private String userId;
    boolean howItsWork;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        FacebookSdk.sdkInitialize(getApplicationContext());
        callbackManager = CallbackManager.Factory.create();
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
        SharedPreferences prefs = getSharedPreferences("HowItsWorkPreferences", Context.MODE_PRIVATE);
        howItsWork = prefs.getBoolean("HowItsWork", false);
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
        facebooklogin = findViewById(R.id.facebooklogin);
        btn_gsign_in = findViewById(R.id.btn_gsign_in);
        btn_gsign_in.setOnClickListener(this);
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();

        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .enableAutoManage(this, this)
                .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                .build();

        // Customizing G+ button
        // btn_gsign_in.setSize(SignInButton.SIZE_STANDARD);
        signOut();
        datatoView();
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
            case R.id.btn_gsign_in:
                signIn();
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
            Utility.showToast(SignUpActivity.this, "Password and Confirm Password not Matched!");
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
                        final ServiceContentData serviceData = new Gson().fromJson(result, ServiceContentData.class);
                        if (serviceData != null) {
                            if (serviceData.isSuccess()) {
                                Utility.showToast(SignUpActivity.this, serviceData.getMsg());
                                finish();
                            } else {
                                Utility.showToast(SignUpActivity.this, serviceData.getMsg());
                            }
                        } else {
                            Utility.showToast(SignUpActivity.this, Contants.Error);
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


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        callbackManager.onActivityResult(requestCode, resultCode, data);
        if (requestCode == RC_SIGN_IN) {
            GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
            handleSignInResult(result);
        }
    }

    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    private void signIn() {
        Intent signInIntent = Auth.GoogleSignInApi.getSignInIntent(mGoogleApiClient);
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }

    private void revokeAccess() {
        Auth.GoogleSignInApi.revokeAccess(mGoogleApiClient).setResultCallback(
                new ResultCallback<Status>() {
                    @Override
                    public void onResult(Status status) {
                    }
                });
    }

    private void handleSignInResult(GoogleSignInResult result) {
        if (result.isSuccess()) {
            // Signed in successfully, show authenticated UI.
            GoogleSignInAccount acct = result.getSignInAccount();
            //oauth_uid = acct.getIdToken();
            oauth_uid = acct.getId();
            name = acct.getDisplayName();
            Uri personPhotoUrl = null;
            if (acct.getPhotoUrl() != null) {
                personPhotoUrl = acct.getPhotoUrl();
                picture = personPhotoUrl.toString();
            }
            socialemail = acct.getEmail();
            oauth_provider = "GooglePlus";//Facebook/GooglePlus
            socialsignUp();
        }
    }

    //gmail logout
    private void signOut() {
        if (mGoogleApiClient.isConnected()) {
            Auth.GoogleSignInApi.signOut(mGoogleApiClient);
            mGoogleApiClient.disconnect();
            mGoogleApiClient.connect();
        }
    }

    @Override
    public void onStart() {
        super.onStart();

        OptionalPendingResult<GoogleSignInResult> opr = Auth.GoogleSignInApi.silentSignIn(mGoogleApiClient);
        if (opr.isDone()) {
            // If the user's cached credentials are valid, the OptionalPendingResult will be "done"
            // and the GoogleSignInResult will be available instantly.
            GoogleSignInResult result = opr.get();
            handleSignInResult(result);
        } else {
            //   showProgressDialog();
            opr.setResultCallback(new ResultCallback<GoogleSignInResult>() {
                @Override
                public void onResult(GoogleSignInResult googleSignInResult) {
                    //  hideProgressDialog();
                    handleSignInResult(googleSignInResult);
                }
            });
        }
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
    }

    //get data from UI
    public void datatoView() {
        //LoginManager.getInstance().logInWithReadPermissions(this, Arrays.asList("EMAIL","public_profile"));
        facebooklogin.setReadPermissions(Arrays.asList(EMAIL, "public_profile"));
        //  facebooklogin.setAuthType(AUTH_TYPE);
        facebooklogin.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {

                System.out.println("onSuccess");
                String accessToken = loginResult.getAccessToken().getToken();
                Log.i("accessToken", accessToken);
                GraphRequest request = GraphRequest.newMeRequest(loginResult.getAccessToken(), new GraphRequest.GraphJSONObjectCallback() {
                    @Override
                    public void onCompleted(JSONObject object, GraphResponse response) {
                        Bundle bFacebookData = getFacebookData(object);

                        socialemail = bFacebookData.getString("email");
                        first_name = bFacebookData.getString("first_name");
                        last_name = bFacebookData.getString("last_name");
                        if (bFacebookData.getString("profile_pic") != null && !bFacebookData.getString("profile_pic").equals("")) {
                            picture = bFacebookData.getString("profile_pic");
                        }
                        oauth_uid = bFacebookData.getString("idFacebook");
                        if (bFacebookData.getString("gender") != null && !bFacebookData.getString("gender").equals("")) {
                            gender = bFacebookData.getString("gender");
                        }
                        oauth_provider = "Facebook";//Facebook/GooglePlus
                        socialsignUp();
                    }
                });
                Bundle parameters = new Bundle();
                parameters.putString("fields", "id, first_name, last_name, email,gender, birthday, location"); // Parámetros que pedimos a facebook
                request.setParameters(parameters);
                request.executeAsync();
            }

            @Override
            public void onCancel() {
                System.out.println("onCancel");
            }

            @Override
            public void onError(FacebookException exception) {
                System.out.println("onError");
            }
        });

    }

    private Bundle getFacebookData(JSONObject object) {
        try {
            Bundle bundle = new Bundle();
            String id = object.getString("id");
            try {
                URL profile_pic = new URL("https://graph.facebook.com/" + id + "/picture?width=200&height=150");
                Log.i("profile_pic", profile_pic + "");
                bundle.putString("profile_pic", profile_pic.toString());
            } catch (MalformedURLException e) {
                e.printStackTrace();
                return null;
            }
            bundle.putString("idFacebook", id);
            if (object.has("first_name"))
                bundle.putString("first_name", object.getString("first_name"));
            if (object.has("last_name"))
                bundle.putString("last_name", object.getString("last_name"));
            if (object.has("email"))
                bundle.putString("email", object.getString("email"));
            if (object.has("gender"))
                bundle.putString("gender", object.getString("gender"));
            if (object.has("birthday"))
                bundle.putString("birthday", object.getString("birthday"));
            if (object.has("location"))
                bundle.putString("location", object.getJSONObject("location").getString("name"));

            return bundle;
        } catch (JSONException e) {
        }
        return null;
    }

    private void socialsignUp() {
        if (Utility.isOnline(SignUpActivity.this)) {
            dotDialog = new ASTProgressBar(SignUpActivity.this);
            dotDialog.show();
            JSONObject object = new JSONObject();
            try {

                object.put("api_key", Contants.API_KEY);
                object.put("oauth_uid", oauth_uid);
                object.put("name", name);
                object.put("first_name", first_name);
                object.put("last_name", last_name);
                object.put("email", socialemail);
                object.put("gender", gender);
                object.put("picture", picture);
                object.put("oauth_provider", oauth_provider);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            String serviceURL = Contants.BASE_URL + Contants.socialLogin;

            ServiceCaller serviceCaller = new ServiceCaller(SignUpActivity.this);
            serviceCaller.CallCommanServiceMethod(serviceURL, object, "socialsignUp", new IAsyncWorkCompletedCallback() {
                @Override
                public void onDone(String result, boolean isComplete) {
                    if (isComplete) {
                        final ServiceContentData serviceData = new Gson().fromJson(result, ServiceContentData.class);
                        if (serviceData != null) {
                            if (serviceData.isSuccess()) {
                                Utility.showToast(SignUpActivity.this, serviceData.getMsg());
                                parseLoginServiceData(result);
                            } else {
                                Utility.showToast(SignUpActivity.this, serviceData.getMsg());
                                if (dotDialog.isShowing()) {
                                    dotDialog.dismiss();
                                }
                            }
                        } else {
                            Utility.showToast(SignUpActivity.this, Contants.Error);
                            if (dotDialog.isShowing()) {
                                dotDialog.dismiss();
                            }
                        }
                    } else {
                        Utility.alertForErrorMessage(Contants.Error, SignUpActivity.this);
                        if (dotDialog.isShowing()) {
                            dotDialog.dismiss();
                        }
                    }
                }
            });
        } else {
            Utility.alertForErrorMessage(Contants.OFFLINE_MESSAGE, SignUpActivity.this);//off line msg....
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
                                SwitchDBHelper switchDBHelper = new SwitchDBHelper(SignUpActivity.this);
                                switchDBHelper.deleteAllRows("userInfo");
                                switchDBHelper.upsertUserInfoData(serviceData.getData(), serviceData.getIs_home_available());
                                is_home_available = serviceData.getIs_home_available();
                                userId = serviceData.getData().getId();
                                flag = true;
                                return flag;
                            }

                            @Override
                            protected void onPostExecute(Boolean flag) {
                                super.onPostExecute(flag);
                                if (flag) {
                                    SharedPreferences prefs = getSharedPreferences("FCMDeviceId", Context.MODE_PRIVATE);
                                    if (prefs != null) {
                                        boolean Sendflag = prefs.getBoolean("Sendflag", false);
                                        if (Sendflag) {
                                            String device_token = prefs.getString("device_token", null);
                                            sendRegistrationToServer(device_token);
                                        }
                                    }
                                    getMsterData();
                                }
                            }
                        }.execute();
                    } else {
                        Utility.showToast(SignUpActivity.this, serviceData.getMsg());
                    }
                } else {
                    Utility.showToast(SignUpActivity.this, serviceData.getMsg());
                    if (dotDialog.isShowing()) {
                        dotDialog.dismiss();
                    }
                }
            } else {
                Utility.alertForErrorMessage(Contants.Error, SignUpActivity.this);
                if (dotDialog.isShowing()) {
                    dotDialog.dismiss();
                }
            }
        }
    }

    private void getMsterData() {
        if (Utility.isOnline(SignUpActivity.this)) {
            String serviceURL = Contants.BASE_URL + Contants.Getdata;
            JSONObject object = new JSONObject();
            try {
                object.put("api_key", Contants.API_KEY);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            ServiceCaller serviceCaller = new ServiceCaller(SignUpActivity.this);
            serviceCaller.CallCommanServiceMethod(serviceURL, object, "MasterData", new IAsyncWorkCompletedCallback() {
                @Override
                public void onDone(String result, boolean isComplete) {
                    if (isComplete) {
                        parseMasterServiceData(result);
                    } else {
                        if (dotDialog.isShowing()) {
                            dotDialog.dismiss();
                        }
                        Utility.alertForErrorMessage(Contants.Error, SignUpActivity.this);
                    }
                }
            });
        } else {
            Utility.alertForErrorMessage(Contants.OFFLINE_MESSAGE, SignUpActivity.this);//off line msg....
        }
    }

    public void parseMasterServiceData(String result) {
        if (result != null) {
            final ServiceContentData serviceData = new Gson().fromJson(result, ServiceContentData.class);
            if (serviceData != null) {
                if (serviceData.isSuccess()) {
                    if (serviceData.getData() != null) {
                        new AsyncTask<Void, Void, Boolean>() {
                            @Override
                            protected Boolean doInBackground(Void... voids) {
                                Boolean flag = false;
                                SwitchDBHelper switchDBHelper = new SwitchDBHelper(SignUpActivity.this);
                                switchDBHelper.deleteAllRows("MasterData");
                                switchDBHelper.insertMasterData(serviceData);
                                flag = true;
                                return flag;
                            }

                            @Override
                            protected void onPostExecute(Boolean flag) {
                                super.onPostExecute(flag);
                                if (userId != null && !userId.equals("") && !userId.equals("")) {
                                    getUserInfo();
                                } else {
                                    Utility.showToast(SignUpActivity.this, "Login not Success");
                                    if (dotDialog.isShowing()) {
                                        dotDialog.dismiss();
                                    }
                                }
                            }
                        }.execute();
                    } else {
                        Utility.showToast(SignUpActivity.this, serviceData.getMsg());
                    }
                } else {
                    Utility.showToast(SignUpActivity.this, serviceData.getMsg());
                }
            }
        }
    }

    private void getUserInfo() {
        if (Utility.isOnline(SignUpActivity.this)) {
            JSONObject object = new JSONObject();
            try {
                object.put("api_key", Contants.API_KEY);
                object.put("user_id", userId);
            } catch (JSONException e) {
                e.printStackTrace();
            }

            String serviceURL = Contants.BASE_URL + Contants.Userinfo;

            ServiceCaller serviceCaller = new ServiceCaller(SignUpActivity.this);
            serviceCaller.CallCommanServiceMethod(serviceURL, object, "UserInfo", new IAsyncWorkCompletedCallback() {
                @Override
                public void onDone(String result, boolean isComplete) {
                    if (isComplete) {
                        parseUserServiceData(result);
                    } else {
                        if (dotDialog.isShowing()) {
                            dotDialog.dismiss();
                        }
                        Utility.alertForErrorMessage(Contants.Error, SignUpActivity.this);
                    }
                }
            });
        } else {
            Utility.alertForErrorMessage(Contants.OFFLINE_MESSAGE, SignUpActivity.this);//off line msg....
        }
    }

    public void parseUserServiceData(String result) {
        if (result != null) {
            final ServiceContentData serviceData = new Gson().fromJson(result, ServiceContentData.class);
            if (serviceData != null) {
                if (serviceData.isSuccess()) {
                    if (serviceData.getData() != null) {
                        new AsyncTask<Void, Void, Boolean>() {
                            @Override
                            protected Boolean doInBackground(Void... voids) {
                                Boolean flag = false;
                                SwitchDBHelper switchDBHelper = new SwitchDBHelper(SignUpActivity.this);
                                switchDBHelper.upsertUserInfoData(serviceData.getData(), serviceData.getIs_home_available());
                                flag = true;
                                return flag;
                            }

                            @Override
                            protected void onPostExecute(Boolean flag) {
                                super.onPostExecute(flag);
                                if (flag) {
                                    getMyHome();
                                }
                            }
                        }.execute();
                    }
                }
            }
        }
    }

    //get my home data
    private void getMyHome() {
        if (Utility.isOnline(SignUpActivity.this)) {
            JSONObject object = new JSONObject();
            try {
                object.put("api_key", Contants.API_KEY);
                object.put("user_id", userId);
            } catch (JSONException e) {
                e.printStackTrace();
            }

            String serviceURL = Contants.BASE_URL + Contants.MyHome;

            ServiceCaller serviceCaller = new ServiceCaller(SignUpActivity.this);
            serviceCaller.CallCommanServiceMethod(serviceURL, object, "getMyHome", new IAsyncWorkCompletedCallback() {
                @Override
                public void onDone(String result, boolean isComplete) {
                    if (isComplete) {
                        parseHomeServiceData(result);
                    } else {
                        if (dotDialog.isShowing()) {
                            dotDialog.dismiss();
                        }
                        Utility.alertForErrorMessage(Contants.Error, SignUpActivity.this);
                    }
                }
            });
        } else {
            Utility.alertForErrorMessage(Contants.OFFLINE_MESSAGE, SignUpActivity.this);//off line msg....
        }
    }

    public void parseHomeServiceData(String result) {
        if (result != null) {
            final ServiceContentData serviceData = new Gson().fromJson(result, ServiceContentData.class);
            if (serviceData != null) {
                if (serviceData.isSuccess()) {
                    if (serviceData.getData() != null) {
                        new AsyncTask<Void, Void, Boolean>() {
                            @Override
                            protected Boolean doInBackground(Void... voids) {
                                Boolean flag = false;
                                SwitchDBHelper switchDBHelper = new SwitchDBHelper(SignUpActivity.this);
                                switchDBHelper.deleteAllRows("Myhomedata");
                                switchDBHelper.deleteAllRows("MychoiceData");
                                switchDBHelper.deleteAllRows("LikedmychoiceData");
                                //  switchDBHelper.deleteAllRows("LikedmychoiceData");
                                for (MyhomeArray myhomeArray : serviceData.getData().getMyhomeArray()) {
                                    switchDBHelper.insertMyhomedata(myhomeArray);
                                }
                                for (MychoiceArray mychoiceArray : serviceData.getData().getMychoiceArray()) {
                                    switchDBHelper.inserMychoiceData(mychoiceArray);
                                }
                                for (LikedmychoiceArray mychoiceArray : serviceData.getData().getLikedmychoiceArray()) {
                                    switchDBHelper.inserLikedmychoiceData(mychoiceArray);
                                }
                                flag = true;
                                return flag;
                            }

                            @Override
                            protected void onPostExecute(Boolean flag) {
                                super.onPostExecute(flag);
                                if (flag) {
                                    moveScreen();
                                }

                            }
                        }.execute();
                    }
                } else {
                    moveScreen();
                }
            }
        }
    }

    private void moveScreen() {
        if (dotDialog.isShowing()) {
            dotDialog.dismiss();
        }
        Utility.showToast(SignUpActivity.this, "Login Success");
        Intent intent;
        if (!howItsWork) {
            intent = new Intent(SignUpActivity.this, AppTourActivity.class);
        } else {
            if (is_home_available == 1) {
                intent = new Intent(SignUpActivity.this, MainActivity.class);
            } else {
                intent = new Intent(SignUpActivity.this, CreateFirstTimePostActivity.class);
            }
        }
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }

    private void sendRegistrationToServer(String token) {

        if (Utility.isOnline(SignUpActivity.this)) {

            JSONObject object = new JSONObject();
            try {
                object.put("api_key", Contants.API_KEY);
                object.put("user_id", userId);
                object.put("device_token", token);
                object.put("device_type", Utility.getDeviceName());
                object.put("os_verion", Utility.getOsVersion());
            } catch (JSONException e) {
                e.printStackTrace();
            }

            String serviceURL = Contants.BASE_URL + Contants.updateDeviceToken;

            ServiceCaller serviceCaller = new ServiceCaller(SignUpActivity.this);
            serviceCaller.CallCommanServiceMethod(serviceURL, object, "sendRegistrationToServer", new IAsyncWorkCompletedCallback() {
                @Override
                public void onDone(String result, boolean isComplete) {
                    if (isComplete) {
                        final ContentData serviceData = new Gson().fromJson(result, ContentData.class);
                        if (serviceData != null) {
                            if (serviceData.isSuccess()) {
                                SharedPreferences prefs = getSharedPreferences("FCMDeviceId", Context.MODE_PRIVATE);
                                SharedPreferences.Editor editor = prefs.edit();
                                editor.putBoolean("Sendflag", false);
                                editor.commit();
                            }
                        }
                    }
                }
            });
        }
    }
}
