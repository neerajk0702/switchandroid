package com.kredivation.switchland.activity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.util.Base64;
import android.util.Log;
import android.view.Window;
import android.view.WindowManager;


import com.google.firebase.messaging.FirebaseMessaging;
import com.google.gson.Gson;
import com.kredivation.switchland.R;
import com.kredivation.switchland.database.SwitchDBHelper;
import com.kredivation.switchland.firebase.MyFirebaseMessagingService;
import com.kredivation.switchland.framework.IAsyncWorkCompletedCallback;
import com.kredivation.switchland.framework.ServiceCaller;
import com.kredivation.switchland.model.ChatServiceContentData;
import com.kredivation.switchland.model.ContentData;
import com.kredivation.switchland.model.Data;
import com.kredivation.switchland.model.LikedmychoiceArray;
import com.kredivation.switchland.model.MychoiceArray;
import com.kredivation.switchland.model.MyhomeArray;
import com.kredivation.switchland.model.ServiceContentData;
import com.kredivation.switchland.utilities.ASTProgressBar;
import com.kredivation.switchland.utilities.CompatibilityUtility;
import com.kredivation.switchland.utilities.Contants;
import com.kredivation.switchland.utilities.Utility;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;


public class SplashScreenActivity extends AppCompatActivity {
    private Boolean CheckOrientation = false;
    private String userId;
    ASTProgressBar dotDialog;
    boolean howItsWork;
    boolean postavaliableOrnot = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_ACTION_BAR);// Removes action bar
        requestWindowFeature(Window.FEATURE_NO_TITLE);// Removes title bar
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_splash_screen);
        chechPortaitAndLandSacpe();//chech Portait And LandSacpe Orientation
        dotDialog = new ASTProgressBar(SplashScreenActivity.this);
        SharedPreferences prefs = getSharedPreferences("HowItsWorkPreferences", Context.MODE_PRIVATE);
        howItsWork = prefs.getBoolean("HowItsWork", false);
        getUserData();
        Utility.clearRewindValue(SplashScreenActivity.this);
        // waitForLogin(); //wait for 3 seconds

    }

    //chech Portait And LandSacpe Orientation
    public void chechPortaitAndLandSacpe() {
        if (CompatibilityUtility.isTablet(SplashScreenActivity.this)) {
            CheckOrientation = true;
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        } else {
            CheckOrientation = false;
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        }
    }

    private void getUserData() {
        SwitchDBHelper switchDBHelper = new SwitchDBHelper(SplashScreenActivity.this);
        ArrayList<Data> userData = switchDBHelper.getAllUserInfoList();
        if (userData != null && userData.size() > 0) {
            for (Data data : userData) {
                userId = data.getId();
                if (data.getIs_home_available() == 1) {
                    postavaliableOrnot = true;
                } else {
                    postavaliableOrnot = false;
                }
            }
            getMsterData();
        } else {
            Intent intent;
            if (!howItsWork) {
                intent = new Intent(SplashScreenActivity.this, AppTourActivity.class);
            } else {
                intent = new Intent(SplashScreenActivity.this, SigninActivity.class);
            }
            startActivity(intent);
        }
        getHSAKey();
        SharedPreferences prefs = getSharedPreferences("FCMDeviceId", Context.MODE_PRIVATE);
        if (prefs != null) {
            boolean Sendflag = prefs.getBoolean("Sendflag", false);
            if (Sendflag) {
                String device_token = prefs.getString("device_token", null);
                sendRegistrationToServer(device_token);
            }
        }

    }

    //wait for 3 seconds
    private void waitForLogin() {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                /*SharedPreferences prefs = getSharedPreferences("NoVerifyOrNotPreferences", Context.MODE_PRIVATE);
                Boolean NoVerify = false;
                if (prefs != null) {
                    NoVerify = prefs.getBoolean("NoVerify", false);
                }
                if (NoVerify) {
                    DbHelper dbhelper = new DbHelper(SplashScreenActivity.this);
                    Data data = dbhelper.getUserData();
                    if (data != null) {
                        Intent intent = new Intent(SplashScreenActivity.this, DashboardActivity.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(intent);
                    } else {
                        Intent intent = new Intent(SplashScreenActivity.this, LoginActivity.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(intent);
                    }
                } else {
                    Intent intent = new Intent(SplashScreenActivity.this, LoginActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intent);
                }*/
                Intent intent = new Intent(SplashScreenActivity.this, CreateFirstTimePostActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
            }
        }, 3000);
    }

    public void getHSAKey() {
        try {
            PackageInfo info = getPackageManager().getPackageInfo(
                    getPackageName(), PackageManager.GET_SIGNATURES);
            for (Signature signature : info.signatures) {
                MessageDigest md = MessageDigest.getInstance("SHA");
                md.update(signature.toByteArray());
                Log.d(Contants.LOG_TAG, "KeyHash*****:" + Base64.encodeToString(md.digest(), Base64.DEFAULT));
            }
        } catch (PackageManager.NameNotFoundException e) {
        } catch (NoSuchAlgorithmException e) {
        }
    }

    private void getMsterData() {
        if (Utility.isOnline(SplashScreenActivity.this)) {
            dotDialog.show();
            String serviceURL = Contants.BASE_URL + Contants.Getdata;
            JSONObject object = new JSONObject();
            try {
                object.put("api_key", Contants.API_KEY);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            ServiceCaller serviceCaller = new ServiceCaller(SplashScreenActivity.this);
            serviceCaller.CallCommanServiceMethod(serviceURL, object, "MasterData", new IAsyncWorkCompletedCallback() {
                @Override
                public void onDone(String result, boolean isComplete) {
                    if (isComplete) {
                        parseMasterServiceData(result);
                    } else {
                        if (dotDialog.isShowing()) {
                            dotDialog.dismiss();
                        }
                        Utility.alertForErrorMessage(Contants.Error, SplashScreenActivity.this);
                    }
                }
            });
        } else {
            Utility.alertForErrorMessage(Contants.OFFLINE_MESSAGE, SplashScreenActivity.this);//off line msg....
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
                                SwitchDBHelper switchDBHelper = new SwitchDBHelper(SplashScreenActivity.this);
                                switchDBHelper.deleteAllRows("MasterData");
                                switchDBHelper.insertMasterData(serviceData);
                                flag = true;
                                return flag;
                            }

                            @Override
                            protected void onPostExecute(Boolean flag) {
                                super.onPostExecute(flag);
                                getUserInfo();
                            }
                        }.execute();
                    } else {
                        Utility.showToast(SplashScreenActivity.this, serviceData.getMsg());
                    }
                } else {
                    Utility.showToast(SplashScreenActivity.this, serviceData.getMsg());
                }
            }
        }
    }


    private void getUserInfo() {
        if (Utility.isOnline(SplashScreenActivity.this)) {
            JSONObject object = new JSONObject();
            try {
                object.put("api_key", Contants.API_KEY);
                object.put("user_id", userId);
            } catch (JSONException e) {
                e.printStackTrace();
            }

            String serviceURL = Contants.BASE_URL + Contants.Userinfo;

            ServiceCaller serviceCaller = new ServiceCaller(SplashScreenActivity.this);
            serviceCaller.CallCommanServiceMethod(serviceURL, object, "UserInfo", new IAsyncWorkCompletedCallback() {
                @Override
                public void onDone(String result, boolean isComplete) {
                    if (isComplete) {
                        parseUserServiceData(result);
                    } else {
                        if (dotDialog.isShowing()) {
                            dotDialog.dismiss();
                        }
                        Utility.alertForErrorMessage(Contants.Error, SplashScreenActivity.this);
                    }
                }
            });
        } else {
            Utility.alertForErrorMessage(Contants.OFFLINE_MESSAGE, SplashScreenActivity.this);//off line msg....
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
                                SwitchDBHelper switchDBHelper = new SwitchDBHelper(SplashScreenActivity.this);
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
                if (dotDialog.isShowing()) {
                    dotDialog.dismiss();
                }
            }
        }
    }

    //get my home data
    private void getMyHome() {
        if (Utility.isOnline(SplashScreenActivity.this)) {
            //  homeDialog = new ASTProgressBar(SplashScreenActivity.this);
            JSONObject object = new JSONObject();
            try {
                object.put("api_key", Contants.API_KEY);
                object.put("user_id", userId);
            } catch (JSONException e) {
                e.printStackTrace();
            }

            String serviceURL = Contants.BASE_URL + Contants.MyHome;

            ServiceCaller serviceCaller = new ServiceCaller(SplashScreenActivity.this);
            serviceCaller.CallCommanServiceMethod(serviceURL, object, "getMyHome", new IAsyncWorkCompletedCallback() {
                @Override
                public void onDone(String result, boolean isComplete) {
                    if (isComplete) {
                        parseHomeServiceData(result);
                    } else {
                        if (dotDialog.isShowing()) {
                            dotDialog.dismiss();
                        }
                        Utility.alertForErrorMessage(Contants.Error, SplashScreenActivity.this);
                    }
                }
            });
        } else {
            Utility.alertForErrorMessage(Contants.OFFLINE_MESSAGE, SplashScreenActivity.this);//off line msg....
        }
    }

    public void parseHomeServiceData(String result) {
        if (result != null) {
            final ServiceContentData serviceData = new Gson().fromJson(result, ServiceContentData.class);
            if (serviceData != null) {
                SwitchDBHelper switchDBHelper = new SwitchDBHelper(SplashScreenActivity.this);
                switchDBHelper.deleteAllRows("Myhomedata");
                switchDBHelper.deleteAllRows("MychoiceData");
                switchDBHelper.deleteAllRows("LikedmychoiceData");
                if (serviceData.isSuccess()) {
                    if (serviceData.getData() != null) {
                        new AsyncTask<Void, Void, Boolean>() {
                            @Override
                            protected Boolean doInBackground(Void... voids) {
                                Boolean flag = false;

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
                                    navigate();
                                }
                                if (dotDialog.isShowing()) {
                                    dotDialog.dismiss();
                                }
                            }
                        }.execute();
                    }
                } else {
                    navigate();
                }
                if (dotDialog.isShowing()) {
                    dotDialog.dismiss();
                }
            }
        }
    }

    private void navigate() {
        Intent intent;
        if (!howItsWork) {
            intent = new Intent(SplashScreenActivity.this, AppTourActivity.class);
        } else {
            if (postavaliableOrnot) {
                intent = new Intent(SplashScreenActivity.this, DashboardActivity.class);
            } else {
                intent = new Intent(SplashScreenActivity.this, CreateFirstTimePostActivity.class);
            }
        }
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }

    private void sendRegistrationToServer(String token) {

        if (Utility.isOnline(SplashScreenActivity.this)) {

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

            ServiceCaller serviceCaller = new ServiceCaller(SplashScreenActivity.this);
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
