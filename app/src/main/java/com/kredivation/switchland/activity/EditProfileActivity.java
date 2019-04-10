package com.kredivation.switchland.activity;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.graphics.Bitmap;
import android.graphics.Rect;
import android.graphics.Typeface;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.AsyncTask;
import android.provider.MediaStore;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import com.google.gson.Gson;
import com.kredivation.switchland.R;
import com.kredivation.switchland.database.SwitchDBHelper;
import com.kredivation.switchland.framework.FileUploaderHelper;
import com.kredivation.switchland.framework.IAsyncWorkCompletedCallback;
import com.kredivation.switchland.framework.ServiceCaller;
import com.kredivation.switchland.model.ChatData;
import com.kredivation.switchland.model.City;
import com.kredivation.switchland.model.Country;
import com.kredivation.switchland.model.Data;
import com.kredivation.switchland.model.Features;
import com.kredivation.switchland.model.House_rules;
import com.kredivation.switchland.model.ServiceContentData;
import com.kredivation.switchland.utilities.ASTProgressBar;
import com.kredivation.switchland.utilities.CompatibilityUtility;
import com.kredivation.switchland.utilities.Contants;
import com.kredivation.switchland.utilities.FontManager;
import com.kredivation.switchland.utilities.Utility;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.HashMap;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;

public class EditProfileActivity extends AppCompatActivity implements View.OnClickListener {
    private Toolbar toolbar;
    private Spinner citySpinner, countrySpinner;
    String[] countryList;
    ArrayList<String> cityList;
    private String cityId = "";
    private String countryId = "";
    private String userId;
    private City[] city;
    private Country[] country;
    private TextInputLayout phone_layout, hno_layout, first_layout, last_layout, area_layout, zip_layout;
    private EditText phone, hno, lastName, firstName, area, zipcode;
    TextView user, email;
    private String phoneStr, hnoStr, lastNameStr, firstNameStr, areaStr, zipcodeStr;
    ImageView proImage;
    public final int REQUEST_CAMERA = 101;
    public final int SELECT_PHOTO = 102;
    private String userChoosenTask;
    File imgFile;
    int citySelectPos = 0;
    private EditText password, newpassword, renewpassword;
    String passwordStr, newpasswordStr, renewpasswordStr;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if (CompatibilityUtility.isTablet(EditProfileActivity.this)) {
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
        /*TextView editIcon = findViewById(R.id.editIcon);
        editIcon.setTypeface(materialdesignicons_font);
        editIcon.setText(Html.fromHtml("&#xf100;"));
        editIcon.setOnClickListener(this);*/
        user = findViewById(R.id.user);
        email = findViewById(R.id.email);
        phone_layout = findViewById(R.id.phone_layout);
        hno_layout = findViewById(R.id.hno_layout);
        first_layout = findViewById(R.id.first_layout);
        last_layout = findViewById(R.id.last_layout);
        area_layout = findViewById(R.id.area_layout);
        zip_layout = findViewById(R.id.zip_layout);
        phone = findViewById(R.id.phone);
        hno = findViewById(R.id.hno);
        lastName = findViewById(R.id.lastName);
        firstName = findViewById(R.id.firstName);
        area = findViewById(R.id.area);
        zipcode = findViewById(R.id.zipcode);
        Button submit = findViewById(R.id.submit);
        submit.setOnClickListener(this);
        proImage = findViewById(R.id.proImage);
        proImage.setOnClickListener(this);

        password = findViewById(R.id.password);
        newpassword = findViewById(R.id.newpassword);
        renewpassword = findViewById(R.id.renewpassword);
        Button save = findViewById(R.id.save);
        save.setOnClickListener(this);

        SwitchDBHelper switchDBHelper = new SwitchDBHelper(EditProfileActivity.this);
        ArrayList<Data> userData = switchDBHelper.getAllUserInfoList();
        if (userData != null && userData.size() > 0) {
            for (Data data : userData) {
                userId = data.getId();
                /*if (data.getFull_name() != null && !data.getFull_name().equals("")) {
                    firstName.setText(data.getFull_name());
                } else {
                    firstName.setText(data.getFirst_name());
                }*/
                firstName.setText(data.getFirst_name());
                lastName.setText(data.getLast_name());
                email.setText(data.getEmail());
                phone.setText(data.getMobile_number());
                user.setText(data.getUsername());
                countryId = data.getCountry_id();
                cityId = data.getCity_id();
                if (data.getAddress_line_1() != null && !data.getAddress_line_1().equals("")) {
                    hno.setText(data.getAddress_line_1());
                }
                if (data.getAddress_line_2() != null && !data.getAddress_line_2().equals("")) {
                    area.setText(data.getAddress_line_2());
                }
                if (data.getZipcode() != null && !data.getZipcode().equals("")) {
                    zipcode.setText(data.getZipcode());
                }
                if (data.getProfile_image() != null) {
                    Picasso.with(EditProfileActivity.this).load(data.getProfile_image()).placeholder(R.drawable.userimage).resize(80, 80).into(proImage);

                }
            }
        }
        citySpinner = findViewById(R.id.citySpinner);
        countrySpinner = findViewById(R.id.countrySpinner);
        setcountry();
    }

    private void setcountry() {
        SwitchDBHelper switchDBHelper = new SwitchDBHelper(EditProfileActivity.this);
        ServiceContentData sData = switchDBHelper.getMasterData();
        int countrySelectPos = 0;

        if (sData != null) {
            if (sData.getData() != null) {
                final Data MData = sData.getData();
                country = MData.getCountry();
                if (country != null) {
                    countryList = new String[country.length];
                    for (int i = 0; i < country.length; i++) {
                        countryList[i] = String.valueOf(country[i].getName());
                        if (countryId != null) {
                            if (countryId.equals(country[i].getId())) {
                                countrySelectPos = i;
                            }
                        }
                    }
                    ArrayAdapter<String> countryAdapter = new ArrayAdapter<String>(EditProfileActivity.this, R.layout.spinner_row, countryList);
                    countrySpinner.setAdapter(countryAdapter);
                    countrySpinner.setSelection(countrySelectPos);
                    countrySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                        @Override
                        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                            countryId = country[position].getId();
                            city = MData.getCity();
                            if (city != null) {
                                cityList = new ArrayList();
                                for (int i = 0; i < city.length; i++) {
                                    if (countryId.equals(city[i].getCountry_id())) {
                                        cityList.add(String.valueOf(city[i].getName()));
                                        if (cityId != null) {
                                            if (cityId.equals(city[i].getId())) {
                                                citySelectPos = i;
                                            }
                                        }
                                    }
                                }
                                if (cityList != null && cityList.size() > 0) {
                                    ArrayAdapter<String> cityAdapter = new ArrayAdapter<String>(EditProfileActivity.this, R.layout.spinner_row, cityList);
                                    citySpinner.setAdapter(cityAdapter);
                                    citySpinner.setSelection(citySelectPos);
                                }
                            }
                        }

                        @Override
                        public void onNothingSelected(AdapterView<?> parent) {

                        }
                    });
                }
                citySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                        cityId = city[position].getId();
                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> parent) {

                    }
                });
            }
        }
    }


    private void requestFocus(View view) {
        if (view.requestFocus()) {
            getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.submit:
                if (isValidate()) {
                    updateProfile();
                }
                break;
            case R.id.proImage:
                selectImage();
                break;
            case R.id.save:
                if (isChangePassValidate()) {
                    changePassword();
                }
                break;
        }
    }

    // ----validation -----
    private boolean isValidate() {
        String emailRegex = "[A-Z0-9a-z._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,4}";
        firstNameStr = firstName.getText().toString();
        lastNameStr = lastName.getText().toString();
        phoneStr = phone.getText().toString();
        hnoStr = hno.getText().toString();
        areaStr = area.getText().toString();
        zipcodeStr = zipcode.getText().toString();
        if (firstNameStr.length() == 0) {
            first_layout.setError("Please Enter First Name");
            requestFocus(firstName);
            return false;
        } else if (lastNameStr.length() == 0) {
            last_layout.setError("Please Enter Last Name");
            requestFocus(lastName);
            return false;
        } else if (phoneStr.length() == 0) {
            phone_layout.setError("Please Enter Phone Number");
            requestFocus(phone);
            return false;
        } else if (hnoStr.length() == 0) {
            hno_layout.setError("Please Enter H.no,Building Name etc");
            requestFocus(hno);
            return false;
        } else if (areaStr.length() == 0) {
            area_layout.setError("Please Enter Area,Locality,Street etc.");
            requestFocus(area);
            return false;
        } else if (zipcodeStr.length() == 0) {
            zip_layout.setError("Please Enter Zipcode");
            requestFocus(zipcode);
            return false;
        } else if (countryId == null || countryId.equals("0") || countryId.equals("")) {
            Utility.showToast(EditProfileActivity.this, "Please select country!");
            return false;
        } else if (cityId == null || cityId.equals("0") || cityId.equals("")) {
            Utility.showToast(EditProfileActivity.this, "Please select city!");
            return false;
        } else {
            first_layout.setErrorEnabled(false);
            last_layout.setErrorEnabled(false);
            zip_layout.setErrorEnabled(false);
            area_layout.setErrorEnabled(false);
            hno_layout.setErrorEnabled(false);
            phone_layout.setErrorEnabled(false);
        }
        return true;
    }

    private void updateProfile() {
        if (Utility.isOnline(EditProfileActivity.this)) {
            final ASTProgressBar dotDialog = new ASTProgressBar(EditProfileActivity.this);
            dotDialog.show();
            JSONObject object = new JSONObject();
            try {
                object.put("api_key", Contants.API_KEY);
                object.put("user_id", userId);
                object.put("first_name", firstNameStr);
                object.put("last_name", lastNameStr);
                object.put("mobile_number", phoneStr);
                object.put("address_line_1", hnoStr);
                object.put("address_line_2", areaStr);
                object.put("zipcode", zipcodeStr);
                object.put("country_id", countryId);
                object.put("city_id", cityId);
            } catch (JSONException e) {
                e.printStackTrace();
            }

            String serviceURL = Contants.BASE_URL + Contants.Updateprofile;

            ServiceCaller serviceCaller = new ServiceCaller(EditProfileActivity.this);
            serviceCaller.CallCommanServiceMethod(serviceURL, object, "UpdateProfile", new IAsyncWorkCompletedCallback() {
                @Override
                public void onDone(String result, boolean isComplete) {
                    if (isComplete) {
                        // parseUpdateServiceData(result);
                        if (dotDialog.isShowing()) {
                            dotDialog.dismiss();
                        }
                        getUserInfo();
                        Utility.showToast(EditProfileActivity.this, "Profile Update Success");
                    } else {
                        Utility.alertForErrorMessage(Contants.Error, EditProfileActivity.this);
                    }
                    if (dotDialog.isShowing()) {
                        dotDialog.dismiss();
                    }
                }
            });
        } else {
            Utility.alertForErrorMessage(Contants.OFFLINE_MESSAGE, EditProfileActivity.this);//off line msg....
        }
    }

    public void parseUpdateServiceData(String result) {
        if (result != null) {
            final ServiceContentData serviceData = new Gson().fromJson(result, ServiceContentData.class);
            if (serviceData != null) {
                if (serviceData.isSuccess()) {
                    if (serviceData.getData() != null) {
                        new AsyncTask<Void, Void, Boolean>() {
                            @Override
                            protected Boolean doInBackground(Void... voids) {
                                Boolean flag = false;
                                SwitchDBHelper switchDBHelper = new SwitchDBHelper(EditProfileActivity.this);
                                switchDBHelper.upsertUserInfoData(serviceData.getData(), serviceData.getIs_home_available());
                                flag = true;
                                return flag;
                            }

                            @Override
                            protected void onPostExecute(Boolean flag) {
                                super.onPostExecute(flag);
                                if (flag) {
                                    Utility.showToast(EditProfileActivity.this, "Profile Update Success");
                                }
                            }
                        }.execute();
                    } else {
                        Utility.showToast(EditProfileActivity.this, serviceData.getMsg());
                    }
                } else {
                    Utility.showToast(EditProfileActivity.this, serviceData.getMsg());
                }
            }
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

    private void selectImage() {
        final CharSequence[] items = {"Take Photo", "Choose from Library", "Cancel"};
        AlertDialog.Builder builder = new AlertDialog.Builder(EditProfileActivity.this);
        builder.setTitle("Select File!");
        builder.setItems(items, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int item) {
                if (items[item].equals("Take Photo")) {
                    userChoosenTask = "Take Photo";
                    cameraIntent();
                } else if (items[item].equals("Choose from Library")) {
                    userChoosenTask = "Choose from Library";
                    galleryIntent();
                } else if (items[item].equals("Cancel")) {
                    dialog.dismiss();
                }
            }
        });
        builder.show();
    }

    //open camera
    private void cameraIntent() {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(intent, REQUEST_CAMERA);
    }

    //select image from android.widget.Gallery
    private void galleryIntent() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);//
        startActivityForResult(Intent.createChooser(intent, "Select Photo"), SELECT_PHOTO);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == SELECT_PHOTO) {
                onSelectFromGalleryResult(data);
            } else if (requestCode == REQUEST_CAMERA) {
                onCaptureImageResult(data);
            }
        }
    }

    private void onSelectFromGalleryResult(Intent data) {
        Uri uri = null;
        if (data != null) {
            try {
                uri = data.getData();
                Bitmap imageBitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), data.getData());
                if (uri != null) {
                    setImageView(uri, imageBitmap);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private void onCaptureImageResult(Intent data) {

        Bitmap thumbnail = (Bitmap) data.getExtras().get("data");
        Uri uri = Utility.getImageUri(EditProfileActivity.this, thumbnail);

        if (uri != null) {
            setImageView(uri, thumbnail);
        }
    }

    private void setImageView(Uri uri, Bitmap imageBitmap) {
        String homeStr = "Profile" + System.currentTimeMillis() + ".png";
        addBitmapAsFile(imageBitmap, homeStr);
    }

    //add bitmap into list
    private Boolean addBitmapAsFile(final Bitmap bitmap, final String fileName) {
        final ASTProgressBar astProgressBar = new ASTProgressBar(EditProfileActivity.this);
        new AsyncTask<Void, Void, Boolean>() {


            @Override
            protected void onPreExecute() {
                super.onPreExecute();

                astProgressBar.show();
            }

            @Override
            protected Boolean doInBackground(Void... voids) {

                Boolean flag = false;
                File sdcardPath = Utility.getExternalStorageFilePath(EditProfileActivity.this);
                sdcardPath.mkdirs();
                //File imgFile = new File(sdcardPath, System.currentTimeMillis() + ".png");
                imgFile = new File(sdcardPath, fileName);

                try {
                    FileOutputStream fOut = new FileOutputStream(imgFile);
                    bitmap.compress(Bitmap.CompressFormat.PNG, 50, fOut);
                    fOut.flush();
                    fOut.close();

                } catch (Exception e) {
                    e.printStackTrace();
                    return false;
                }
                MediaScannerConnection.scanFile(EditProfileActivity.this, new String[]{imgFile.toString()}, null,
                        new MediaScannerConnection.OnScanCompletedListener() {
                            public void onScanCompleted(String path, Uri uri) {
                                if (Contants.IS_DEBUG_LOG) {
                                    if (Contants.IS_DEBUG_LOG) {
                                        Log.d(Contants.LOG_TAG, "Scanned " + path + ":");
                                        Log.d(Contants.LOG_TAG, "-> uri=" + uri);
                                    }
                                }
                            }
                        });
                return flag;
            }

            @Override
            protected void onPostExecute(Boolean flag) {
                super.onPostExecute(flag);
                Picasso.with(EditProfileActivity.this).load(imgFile).into(proImage);
                if (astProgressBar.isShowing()) {
                    astProgressBar.dismiss();
                }
                updateUserImg();
            }
        }.execute();

        return true;
    }

    public void updateUserImg() {
        if (Utility.isOnline(EditProfileActivity.this)) {
            final ASTProgressBar progressBar = new ASTProgressBar(EditProfileActivity.this);
            progressBar.show();
            HashMap<String, String> payloadList = new HashMap<String, String>();
            payloadList.put("api_key", Contants.API_KEY);
            payloadList.put("user_id", userId);
            String serviceURL = Contants.BASE_URL + Contants.UpdateUserImage;
            MultipartBody.Builder multipartBody = setMultipartBodyVaule();

            FileUploaderHelper fileUploaderHelper = new FileUploaderHelper(EditProfileActivity.this, payloadList, multipartBody, serviceURL) {
                @Override
                public void receiveData(String result) {
                    final ServiceContentData serviceData = new Gson().fromJson(result, ServiceContentData.class);
                    if (serviceData != null) {
                        if (serviceData.isSuccess()) {
                            Utility.showToast(EditProfileActivity.this, serviceData.getMsg());
                        } else {
                            Utility.showToast(EditProfileActivity.this, serviceData.getMsg());
                        }
                    } else {
                        Utility.showToast(EditProfileActivity.this, "Server Side error!");
                    }
                    if (progressBar.isShowing()) {
                        progressBar.dismiss();
                    }
                }
            };
            fileUploaderHelper.execute();
        } else {
            Utility.alertForErrorMessage(Contants.OFFLINE_MESSAGE, EditProfileActivity.this);//off line msg....
        }
    }

    //add images into MultipartBody for send as multipart
    private MultipartBody.Builder setMultipartBodyVaule() {
        MediaType MEDIA_TYPE_PNG = MediaType.parse("image/png");
        MultipartBody.Builder multipartBody = new MultipartBody.Builder().setType(MultipartBody.FORM);
        //image add
        if (imgFile != null && imgFile.exists()) {
            multipartBody.addFormDataPart("profie_pic", imgFile.getName(), RequestBody.create(MEDIA_TYPE_PNG, imgFile));
        }

        return multipartBody;
    }

    private void getUserInfo() {
        final ASTProgressBar progressBar = new ASTProgressBar(EditProfileActivity.this);
        progressBar.show();
        JSONObject object = new JSONObject();
        try {
            object.put("api_key", Contants.API_KEY);
            object.put("user_id", userId);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        String serviceURL = Contants.BASE_URL + Contants.Userinfo;

        ServiceCaller serviceCaller = new ServiceCaller(EditProfileActivity.this);
        serviceCaller.CallCommanServiceMethod(serviceURL, object, "UserInfo", new IAsyncWorkCompletedCallback() {
            @Override
            public void onDone(String result, boolean isComplete) {
                if (isComplete) {
                    if (result != null) {
                        final ServiceContentData serviceData = new Gson().fromJson(result, ServiceContentData.class);
                        if (serviceData != null) {
                            if (serviceData.isSuccess()) {
                                if (serviceData.getData() != null) {
                                    SwitchDBHelper switchDBHelper = new SwitchDBHelper(EditProfileActivity.this);
                                    switchDBHelper.upsertUserInfoData(serviceData.getData(), serviceData.getIs_home_available());

                                }
                            }
                        }
                    }
                    if (progressBar.isShowing()) {
                        progressBar.dismiss();
                    }
                } else {
                    if (progressBar.isShowing()) {
                        progressBar.dismiss();
                    }
                    Utility.alertForErrorMessage(Contants.Error, EditProfileActivity.this);
                }
            }
        });
    }

    //------------password change code------------
    // ----validation -----
    private boolean isChangePassValidate() {
        String emailRegex = "[A-Z0-9a-z._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,4}";
        passwordStr = password.getText().toString();
        newpasswordStr = newpassword.getText().toString();
        renewpasswordStr = renewpassword.getText().toString();

        if (passwordStr.length() == 0) {
            Utility.showToast(EditProfileActivity.this, "Please Enter your Old Password.");
            requestFocus(password);
            return false;
        } else if (newpasswordStr.length() == 0) {
            Utility.showToast(EditProfileActivity.this, "Please Enter New Password.");
            requestFocus(newpassword);
            return false;
        } else if (renewpassword.length() == 0) {
            Utility.showToast(EditProfileActivity.this, "Please Enter Confirm Password.");
            requestFocus(renewpassword);
            return false;
        } else if (!renewpassword.equals(newpasswordStr)) {
            Utility.showToast(EditProfileActivity.this, "New Password and Confirm password not matched.");
            requestFocus(renewpassword);
            return false;
        }
        return true;
    }

    private void changePassword() {
        if (Utility.isOnline(EditProfileActivity.this)) {
            final ASTProgressBar dotDialog = new ASTProgressBar(EditProfileActivity.this);
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

            ServiceCaller serviceCaller = new ServiceCaller(EditProfileActivity.this);
            serviceCaller.CallCommanServiceMethod(serviceURL, object, "ChangePassword", new IAsyncWorkCompletedCallback() {
                @Override
                public void onDone(String result, boolean isComplete) {
                    if (isComplete) {
                        final ServiceContentData serviceData = new Gson().fromJson(result, ServiceContentData.class);
                        if (serviceData != null) {
                            if (serviceData.isSuccess()) {
                                Utility.showToast(EditProfileActivity.this, "Your Password changed successfully.");
                            } else {
                                Utility.showToast(EditProfileActivity.this, serviceData.getMsg());
                            }
                        } else {
                            Utility.showToast(EditProfileActivity.this, Contants.Error);
                        }
                    } else {
                        Utility.alertForErrorMessage(Contants.Error, EditProfileActivity.this);
                    }
                    if (dotDialog.isShowing()) {
                        dotDialog.dismiss();
                    }
                }
            });
        } else {
            Utility.alertForErrorMessage(Contants.OFFLINE_MESSAGE, EditProfileActivity.this);//off line msg....
        }
    }

}
