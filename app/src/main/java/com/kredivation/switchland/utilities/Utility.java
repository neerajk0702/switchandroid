package com.kredivation.switchland.utilities;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.ContentUris;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Point;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.graphics.drawable.StateListDrawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Environment;
import android.os.Handler;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.support.annotation.ColorInt;
import android.support.annotation.ColorRes;
import android.support.annotation.DrawableRes;
import android.support.annotation.RequiresApi;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.content.ContextCompat;
import android.support.v4.util.LruCache;
import android.util.Base64;
import android.util.Log;
import android.view.Display;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.OvershootInterpolator;
import android.view.animation.ScaleAnimation;
import android.view.inputmethod.InputMethodManager;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;


import com.kredivation.switchland.R;
import com.kredivation.switchland.activity.DashboardActivity;
import com.squareup.picasso.Picasso;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;


/**
 * Created by Neeraj on 3/8/2017.
 */
public class Utility {
    public static final int MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE = 123;
    LruCache<String, Bitmap> mMemoryCache;

    public static File getFilePath(Context context) {
        File directory = new File(context.getFilesDir(), Contants.APP_DIRECTORY);
        return directory;
    }

    /**
     * @return Application's version code from the {@code PackageManager}.
     */
    public static int getAppVersion(Context context) {
        int version = 1;
        try {
            PackageInfo packageInfo = context.getPackageManager()
                    .getPackageInfo(context.getPackageName(), 0);
            version = packageInfo.versionCode;

        } catch (PackageManager.NameNotFoundException e) {
            // should never happen
            // throw new RuntimeException("Could not get package name: " + e);
        }
        return version;
    }

    //get app version name
    public static String getAppVersionName(Context context) {
        String version = "";
        try {
            PackageInfo packageInfo = context.getPackageManager()
                    .getPackageInfo(context.getPackageName(), 0);
            version = packageInfo.versionName;

        } catch (PackageManager.NameNotFoundException e) {
            // should never happen
            // throw new RuntimeException("Could not get package name: " + e);

        }
        return version;
    }

    //get app package name
    public static String getAppPackageName(Context context) {
        String pkName = "";
        try {
            PackageInfo packageInfo = context.getPackageManager()
                    .getPackageInfo(context.getPackageName(), 0);
            pkName = packageInfo.packageName;

        } catch (PackageManager.NameNotFoundException e) {
            // should never happen
            // throw new RuntimeException("Could not get package name: " + e);

        }
        return pkName;
    }

    public static String getDeviceName() {
        String manufacturer = Build.MANUFACTURER;
        String model = Build.MODEL;
        if (model.startsWith(manufacturer)) {
            return (model);
        } else {
            return (manufacturer) + " " + model;
        }
    }

    public static Date parseDateFromString(String strDate) {
        Date date = null;

        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        try {
            date = format.parse(strDate);

        } catch (ParseException e) {
            e.printStackTrace();
        }
        return date;
    }

    public static Date toCalendar(final String iso8601string) throws ParseException {
        //GregorianCalendar calendar = (GregorianCalendar) GregorianCalendar.getInstance();
        String s = iso8601string;
        Date date = null;
        try {
            s = s.replace("Z", "+00:00");
            try {
                s = s.substring(0, 26) + s.substring(27);
            } catch (IndexOutOfBoundsException e) {
            }
            date = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS").parse(s);
        } catch (Exception tc) {
            s = s.replace("Z", "+00:00");
            try {
                s = s.substring(0, 22) + s.substring(23);
            } catch (IndexOutOfBoundsException e) {
            }
            date = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss").parse(s);
        }
        //calendar.setTime(date);
        return date;
    }

    //convert date for get day month year hours min am/pm
    public static String convertDate(Date date) {
        StringBuilder sb = new StringBuilder();
        String dayOfTheWeek = (String) android.text.format.DateFormat.format("EEEE", date);//Thursday
        String stringMonth = (String) android.text.format.DateFormat.format("MMM", date); //Jun
        String intMonth = (String) android.text.format.DateFormat.format("MM", date); //06
        String year = (String) android.text.format.DateFormat.format("yyyy", date); //2013
        String day = (String) android.text.format.DateFormat.format("dd", date); //20
        String hours = (String) android.text.format.DateFormat.format("h", date); //20
        String min = (String) android.text.format.DateFormat.format("mm", date); //20
        String ampm = (String) android.text.format.DateFormat.format("aa", date); //20
        //Log.d(Contants.LOG_TAG, "dayOfTheWeek*********" + dayOfTheWeek);
        //Log.d(Contants.LOG_TAG, "year*********" + year);
        //Log.d(Contants.LOG_TAG, "stringMonth*********" + stringMonth+"  "+intMonth);
           /* Log.d(Contants.LOG_TAG, "day*********" + day);
            Log.d(Contants.LOG_TAG, "hours*********" + hours);
            Log.d(Contants.LOG_TAG, "min*********" + min);
            Log.d(Contants.LOG_TAG, "ampm*********" + ampm);*/
        List<String> list = new ArrayList<String>();
        list.add(dayOfTheWeek);
        list.add(day);
        list.add(stringMonth);
        list.add(hours);
        list.add(min);
        list.add(ampm);
        list.add(year);
        list.add(intMonth);
        String delim = "";
        for (String i : list) {
            sb.append(delim).append(i);
            delim = ",";
        }
        return sb.toString();
    }


    //calculate total days between to days
    public static int daysBetween(Date startDate, Date endDate) {
        return (int) ((endDate.getTime() - startDate.getTime()) / (1000 * 60 * 60 * 24));
    }

    //encode image into base64 string
    public static String encodeToBase64(Bitmap image, Bitmap.CompressFormat compressFormat, int quality) {
        ByteArrayOutputStream byteArrayOS = new ByteArrayOutputStream();
        if (image != null) {
            image.compress(compressFormat, quality, byteArrayOS);
        }
        return Base64.encodeToString(byteArrayOS.toByteArray(), Base64.DEFAULT);
    }

    //decode base64 string into image
    public static Bitmap decodeBase64(String input) {
        Bitmap bitmap = null;
        if (input != null) {
            byte[] decodedBytes = Base64.decode(input, 0);
            bitmap = BitmapFactory.decodeByteArray(decodedBytes, 0, decodedBytes.length);
        }
        return bitmap;
    }

    //check online
    public static boolean isOnline(Context context) {
        ConnectivityManager connectivityManager;
        boolean connected = false;
        try {
            connectivityManager = (ConnectivityManager) context
                    .getSystemService(Context.CONNECTIVITY_SERVICE);

            NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
            connected = networkInfo != null && networkInfo.isAvailable() &&
                    networkInfo.isConnected();
            return connected;


        } catch (Exception e) {
            //System.out.println("CheckConnectivity Exception: " + e.getMessage());
            //Log.v("connectivity", e.toString());
        }
        return connected;
    }

    //alert for error message
    public static void alertForErrorMessage(String errorMessage, Context mContext) {
        final AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
        Typeface roboto_regular = Typeface.createFromAsset(mContext.getAssets(), "fonts/roboto.regular.ttf");
        final AlertDialog alert = builder.create();
        // alert.getWindow().getAttributes().windowAnimations = R.style.alertAnimation;
        View view = alert.getLayoutInflater().inflate(R.layout.custom_error_alert, null);
        TextView title = (TextView) view.findViewById(R.id.title);
        title.setTypeface(roboto_regular);
        TextView ok = (TextView) view.findViewById(R.id.Ok);
        ok.setTypeface(roboto_regular);
        title.setText(errorMessage);
        alert.setCustomTitle(view);
        ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alert.dismiss();
            }
        });
        alert.show();
    }

    public static void alertNative(Context mContext) {
        android.support.v7.app.AlertDialog.Builder builder =
                new android.support.v7.app.AlertDialog.Builder(mContext, R.style.AlertDialogTheme);
        android.support.v7.app.AlertDialog dialog = builder.create();
        //dialog.getWindow().getAttributes().windowAnimations = R.style.alertAnimation;
        dialog.setMessage("hello");
        dialog.setTitle("hi");
        dialog.setButton(Dialog.BUTTON_POSITIVE, "Ok", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

            }
        });
        dialog.setButton(Dialog.BUTTON_NEGATIVE, "Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

            }
        });
        dialog.show();
        dialog.getButton(dialog.BUTTON_NEGATIVE).setTextColor(Color.parseColor("#ffffff"));
        dialog.getButton(dialog.BUTTON_POSITIVE).setTextColor(Color.parseColor("#ffffff"));
    }

    //set medicine id
    public static void setMedicineId(Context context, int id) {
        try {
            SharedPreferences prefs = context.getSharedPreferences("MedicineIdPreferences", Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = prefs.edit();
            editor.putInt("medicineId", id);
            editor.commit();
        } catch (Exception e) {
            // should never happen
            //   throw new RuntimeException("Could not get language: " + e);
        }
    }


    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    public static String getPath(Context context, final Uri uri) {

        final boolean isKitKat = Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT;

        // DocumentProvider
        if (isKitKat && DocumentsContract.isDocumentUri(context, uri)) {
            // ExternalStorageProvider
            if (isExternalStorageDocument(uri)) {
                final String docId = DocumentsContract.getDocumentId(uri);
                final String[] split = docId.split(":");
                final String type = split[0];

                if ("primary".equalsIgnoreCase(type)) {
                    return Environment.getExternalStorageDirectory() + "/" + split[1];
                }

                // TODO handle non-primary volumes
            }
            // DownloadsProvider
            else if (isDownloadsDocument(uri)) {

                final String id = DocumentsContract.getDocumentId(uri);
                final Uri contentUri = ContentUris.withAppendedId(
                        Uri.parse("content://downloads/public_downloads"), Long.valueOf(id));

                return getDataColumn(context, contentUri, null, null);
            }
            // MediaProvider
            else if (isMediaDocument(uri)) {
                final String docId = DocumentsContract.getDocumentId(uri);
                final String[] split = docId.split(":");
                final String type = split[0];

                Uri contentUri = null;
                if ("image".equals(type)) {
                    contentUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
                } else if ("video".equals(type)) {
                    contentUri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI;
                } else if ("audio".equals(type)) {
                    contentUri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
                }

                final String selection = "_id=?";
                final String[] selectionArgs = new String[]{
                        split[1]
                };

                return getDataColumn(context, contentUri, selection, selectionArgs);
            }
        }
        // MediaStore (and general)
        else if ("content".equalsIgnoreCase(uri.getScheme())) {
            return getDataColumn(context, uri, null, null);
        }
        // File
        else if ("file".equalsIgnoreCase(uri.getScheme())) {
            return uri.getPath();
        }

        return null;
    }

    public static String getDataColumn(Context context, Uri uri, String selection,
                                       String[] selectionArgs) {

        Cursor cursor = null;
        final String column = "_data";
        final String[] projection = {
                column
        };

        try {
            cursor = context.getContentResolver().query(uri, projection, selection, selectionArgs,
                    null);
            if (cursor != null && cursor.moveToFirst()) {
                final int column_index = cursor.getColumnIndexOrThrow(column);
                return cursor.getString(column_index);
            }
        } finally {
            if (cursor != null)
                cursor.close();
        }
        return null;
    }


    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is ExternalStorageProvider.
     */
    public static boolean isExternalStorageDocument(Uri uri) {
        return "com.android.externalstorage.documents".equals(uri.getAuthority());
    }

    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is DownloadsProvider.
     */
    public static boolean isDownloadsDocument(Uri uri) {
        return "com.android.providers.downloads.documents".equals(uri.getAuthority());
    }

    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is MediaProvider.
     */
    public static boolean isMediaDocument(Uri uri) {
        return "com.android.providers.media.documents".equals(uri.getAuthority());
    }

    //set State id
    public static void setStateId(Context context, int id) {
        try {
            SharedPreferences prefs = context.getSharedPreferences("StateIdPreferences", Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = prefs.edit();
            editor.putInt("StateId", id);
            editor.putBoolean("SelectFlage", true);
            editor.commit();
        } catch (Exception e) {
            // should never happen
            //   throw new RuntimeException("Could not get language: " + e);
        }
    }

    //set City id
    public static void setCityId(Context context, int id) {
        try {
            SharedPreferences prefs = context.getSharedPreferences("CityIdPreferences", Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = prefs.edit();
            editor.putInt("CityId", id);
            editor.putBoolean("SelectFlage", true);
            editor.commit();
        } catch (Exception e) {
            // should never happen
            //   throw new RuntimeException("Could not get language: " + e);
        }
    }

    //set State id for add new Address
    public static void setAddNewAddressStateId(Context context, int id) {
        try {
            SharedPreferences prefs = context.getSharedPreferences("AddNewAddressStatePreferences", Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = prefs.edit();
            editor.putInt("StateId", id);
            editor.putBoolean("SelectFlage", true);
            editor.commit();
        } catch (Exception e) {
            // should never happen
            //   throw new RuntimeException("Could not get language: " + e);
        }
    }

    //set City id for add new Address
    public static void setAddNewAddressCityId(Context context, int id) {
        try {
            SharedPreferences prefs = context.getSharedPreferences("AddNewAddressCityPreferences", Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = prefs.edit();
            editor.putInt("CityId", id);
            editor.putBoolean("SelectFlage", true);
            editor.commit();
        } catch (Exception e) {
            // should never happen
            //   throw new RuntimeException("Could not get language: " + e);
        }
    }

    //set device id for  GCM
    public static void setDeviceIDIntoSharedPreferences(Context context, String device_token, boolean sendflag) {
        SharedPreferences prefs = context.getSharedPreferences("FCMDeviceId", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString("device_token", device_token);
        editor.putBoolean("Sendflag", sendflag);
        if (Contants.IS_DEBUG_LOG) {
            Log.d(Contants.LOG_TAG, "device_token set*********" + device_token);
        }
        editor.commit();
    }

    //get device id for  GCM
    public static String getDeviceIDFromSharedPreferences(Context context) {
        String device_token = null;
        SharedPreferences prefs = context.getSharedPreferences("FCMDeviceId", Context.MODE_PRIVATE);
        if (prefs != null) {
            device_token = prefs.getString("device_token", null);
            if (Contants.IS_DEBUG_LOG) {
                Log.d(Contants.LOG_TAG, "device_token get*********" + device_token);
            }
        }
        return device_token;
    }

    //add to cart animation
    public static void addCartAnimation(Context context) {
        ScaleAnimation scale = new ScaleAnimation(0, 1, 0, 1, ScaleAnimation.RELATIVE_TO_SELF, .5f, ScaleAnimation.RELATIVE_TO_SELF, .5f);
        scale.setDuration(500);
        scale.setInterpolator(new OvershootInterpolator());
        DashboardActivity rootActivity = (DashboardActivity) context;
        //rootActivity.basketicon.startAnimation(scale);
    }

    // convert i/o stream into byte array

    public static byte[] getBytes(InputStream inputStream) throws IOException {
        ByteArrayOutputStream byteBuffer = new ByteArrayOutputStream();
        int bufferSize = 1024;
        byte[] buffer = new byte[bufferSize];

        int len = 0;
        while ((len = inputStream.read(buffer)) != -1) {
            byteBuffer.write(buffer, 0, len);
        }
        return byteBuffer.toByteArray();
    }

    public static String getRealPathFromURI(Context context, Uri contentURI) {
        String result;
        Cursor cursor = context.getContentResolver().query(contentURI, null, null, null, null);
        if (cursor == null) { // Source is Dropbox or other similar local file path
            result = contentURI.getPath();
        } else {
            cursor.moveToFirst();
            int idx = cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA);
            result = cursor.getString(idx);
            cursor.close();
        }
        return result;
    }

    public static Uri getImageUri(Context inContext, Bitmap inImage) {
        Uri uri = null;
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        inImage.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
        String path = MediaStore.Images.Media.insertImage(inContext.getContentResolver(), inImage, "Title", null);
        if (path != null) {
            uri = Uri.parse(path);
        }
        return uri;
    }

    //clear all bask stack fragment
    public static void clearBackStack(Context context) {
        //fragmentManager.popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
        FragmentManager fragmentManager = ((FragmentActivity) context).getSupportFragmentManager();
        if (fragmentManager.getBackStackEntryCount() > 0) {
            FragmentManager.BackStackEntry first = fragmentManager.getBackStackEntryAt(0);
            fragmentManager.popBackStack(first.getId(), FragmentManager.POP_BACK_STACK_INCLUSIVE);
        }

    }

    //set image from mUri
    public static void setImageFromUri(final Context context, final Uri uri, final ImageView imageView) {
        new AsyncTask<Void, Void, Boolean>() {
            File imgFile;

            @Override
            protected Boolean doInBackground(Void... voids) {

                Boolean flag = false;
                File sdcardPath = Utility.getFilePath(context);
                sdcardPath.mkdirs();
                imgFile = new File(sdcardPath, System.currentTimeMillis() + ".png");
                try {
                    InputStream iStream = context.getContentResolver().openInputStream(uri);
                    byte[] inputData = Utility.getBytes(iStream);

                    FileOutputStream fOut = new FileOutputStream(imgFile);
                    fOut.write(inputData);
                    //   bitmap.compress(Bitmap.CompressFormat.PNG, 100, fOut);
                    fOut.flush();
                    fOut.close();
                    iStream.close();

                } catch (Exception e) {
                    e.printStackTrace();
                    return false;
                }
                return flag;
            }

            @Override
            protected void onPostExecute(Boolean flag) {
                super.onPostExecute(flag);
                if (imgFile != null) {
                    Picasso.with(context).load(imgFile).resize(100, 100).into(imageView);
                }
            }
        }.execute();

    }

 /*   //chack run time EXTERNAL_STORAGE permission
    public boolean checkExternalStoragePermission(Context context) {
        int currentAPIVersion = Build.VERSION.SDK_INT;
        if (currentAPIVersion >= Build.VERSION_CODES.M) {
            if (ContextCompat.checkSelfPermission(context, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                if (ActivityCompat.shouldShowRequestPermissionRationale((Activity) context, Manifest.permission.READ_EXTERNAL_STORAGE)) {
                    AlertDialog.Builder alertBuilder = new AlertDialog.Builder(context);
                    alertBuilder.setCancelable(true);
                    alertBuilder.setTitle("Permission Necessary");
                    alertBuilder.setMessage("External Storage Permission is Necessary");
                    alertBuilder.setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                        @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
                        public void onClick(DialogInterface dialog, int which) {
                            requestPermissions(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE);
                        }
                    });
                    AlertDialog alert = alertBuilder.create();
                    alert.show();
                } else {
                    requestPermissions(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE);
                }
                return false;
            } else {
                return true;
            }
        } else {
            return true;
        }
    }*/


    //set Access Token for send with all service call
    public static void setAccessToken(Context context, String accessToken) {
        try {
            SharedPreferences prefs = context.getSharedPreferences("AccessTokenPreferences", Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = prefs.edit();
            editor.putString("AccessToken", accessToken);
            editor.commit();
        } catch (Exception e) {
            // should never happen
            //   throw new RuntimeException("Could not get language: " + e);
        }
    }

    //set loginId for send with all service call
    public static void setLoginId(Context context, int loginId) {
        try {
            SharedPreferences prefs = context.getSharedPreferences("LoginIdPreferences", Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = prefs.edit();
            editor.putInt("LoginId", loginId);
            editor.commit();
        } catch (Exception e) {
            // should never happen
            //   throw new RuntimeException("Could not get language: " + e);
        }
    }
     /* //call service through OkHttpClient
     private void doPostRequest(final String url, final String json) {
        new AsyncTask<Void, Void, String>() {

            @Override
            protected String doInBackground(Void... voids) {
                OkHttpClient client = new OkHttpClient();
                MediaType JSON = MediaType.parse("application/json; charset=utf-8");

                RequestBody body = RequestBody.create(JSON, json);
                Request request = new Request.Builder()
                        .url(url)
                        .post(body)
                        .header("ontent-Type", "application/json")
                        .build();
                Response response = null;
                try {
                    response = client.newCall(request).execute();
                    Log.d(Contants.LOG_TAG, "Access response*****" + response.body().string());
                } catch (IOException e) {
                    e.printStackTrace();
                }
                return null;
            }

            @Override
            protected void onPostExecute(String responce) {
                super.onPostExecute(responce);
            }
        }.execute();
    }*/

    //get app tour run only first time or not
    public static Boolean getAppTourRunStatus(Context context) {
        Boolean tourFlag;
        try {
            SharedPreferences prefs = context.getSharedPreferences("AppTourPreferences", Context.MODE_PRIVATE);
            tourFlag = prefs.getBoolean("AppTourFlag", false);
            return tourFlag;
        } catch (Exception e) {
            Log.d(Contants.LOG_TAG, "Exception  - getLanguage" + e.getMessage());
        }
        return null;
    }

    //set app tour run only first time flag
    public static void setAppTourRunStatus(Boolean flag, Context context) {
        try {
            SharedPreferences prefs = context.getSharedPreferences("AppTourPreferences", Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = prefs.edit();
            editor.putBoolean("AppTourFlag", flag);
            editor.commit();
        } catch (Exception e) {
            // should never happen
            //   throw new RuntimeException("Could not get language: " + e);
        }
    }

    //set firstime activity run only first time flag
    public static void setFirstTimeActivityStatus(Boolean flag, Context context) {
        try {
            SharedPreferences prefs = context.getSharedPreferences("FirstTimeActivity", Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = prefs.edit();
            editor.putBoolean("FirstTimeFlag", flag);
            editor.commit();
        } catch (Exception e) {
            // should never happen
            //   throw new RuntimeException("Could not get language: " + e);
        }
    }

    //get app tour run only first time or not
    public static Boolean getFirstTimeActivityStatus(Context context) {
        Boolean tourFlag;
        try {
            SharedPreferences prefs = context.getSharedPreferences("FirstTimeActivity", Context.MODE_PRIVATE);
            tourFlag = prefs.getBoolean("FirstTimeFlag", false);
            return tourFlag;
        } catch (Exception e) {
            Log.d(Contants.LOG_TAG, "Exception  - getLanguage" + e.getMessage());
        }
        return null;
    }


    public static String getUserPhoneNo(Context context) {
        String phone;
        try {
            SharedPreferences prefs = context.getSharedPreferences("UserPhonePreferences", Context.MODE_PRIVATE);
            phone = prefs.getString("Phonenumber", "");
            return phone;
        } catch (Exception e) {
            Log.d(Contants.LOG_TAG, "Exception  - Phone" + e.getMessage());
        }
        return null;
    }

    public static void setUserPhoneNo(Context context, String phone) {
        try {
            SharedPreferences prefs = context.getSharedPreferences("UserPhonePreferences", Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = prefs.edit();
            editor.putString("Phonenumber", phone);
            editor.commit();
        } catch (Exception e) {
            // should never happen
            //   throw new RuntimeException("Could not get language: " + e);
        }
    }

    //get DLP directory for download files
    public static File getExternalStorageFilePath(Context context) {
        File DLPDirectory = new File(Environment.getExternalStorageDirectory(), Contants.APP_DIRECTORY);
        return DLPDirectory;
    }

    public static void showToast(Context context, String msg) {
        final Toast toast = Toast.makeText(context, msg, Toast.LENGTH_SHORT);
        toast.setGravity(Gravity.CENTER, 0, 0);
        toast.show();
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                toast.cancel();
            }
        }, 1500);
    }

    public static int getColor(Context context, @ColorRes int colorId) {
        try {
            return ContextCompat.getColor(context, colorId);
        } catch (Exception e) {
            return colorId;
        }
    }

    /**
     * Sets Background {@link Drawable} to any given view. Checks for the SDK
     * version and calls the appropriate method. This method is made to handle
     * the deprecation issue of setBackgroudDrawable method.
     *
     * @param view
     * @param drawable
     */
    public static void setBackground(Drawable drawable, View view) {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.JELLY_BEAN) {
            setBackgroundOld(view, drawable);
        } else {
            setBackgroundNew(view, drawable);
        }
    }

    public static void setBackground(View v) {
        setBackground(null, v);
    }

    public static void setBackground(Context context, @DrawableRes int resId, View v) {
        setBackground(getDrawable(context, resId), v);
    }

    public static Drawable getDrawable(Context context, @DrawableRes int drawableResId) {
        return ContextCompat.getDrawable(context, drawableResId);
    }

    @SuppressWarnings("deprecation")
    private static void setBackgroundOld(View view, Drawable drawable) {
        view.setBackgroundDrawable(drawable);
    }

    @SuppressLint("NewApi")
    private static void setBackgroundNew(View view, Drawable drawable) {
        view.setBackground(drawable);
    }

    public static void setBackground(Context context, View v, @ColorRes int bgColor, @ColorRes int strokeColor, int strokeWidth, float[] radii, ASTEnum shape) {
        GradientDrawable drawable = getShape(getColor(context, bgColor), getColor(context, strokeColor), strokeWidth, radii, shape);
        setBackground(drawable, v);
    }

    public static void setBackgroundOval(Context context, View v, @ColorRes int bgColor, @ColorRes int strokeColor, int strokeWidth) {
        GradientDrawable drawable = getOvalShape(getColor(context, bgColor), getColor(context, strokeColor), strokeWidth);
        setBackground(drawable, v);
    }

    public static void setBackgroundOval(View v, @ColorInt int bgColor, @ColorInt int strokeColor) {
        GradientDrawable drawable = getOvalShape(bgColor, strokeColor, 0);
        setBackground(drawable, v);
    }

    public static void setBackgroundRing(Context context, View v, @ColorRes int bgColor, @ColorRes int strokeColor, int strokeWidth) {
        GradientDrawable drawable = getRingShape(getColor(context, bgColor), getColor(context, strokeColor), strokeWidth);
        setBackground(drawable, v);
    }

    public static void setBackgroundRing(View v, @ColorInt int bgColor, @ColorInt int strokeColor) {
        GradientDrawable drawable = getRingShape(bgColor, strokeColor, 0);
        setBackground(drawable, v);
    }

    public static GradientDrawable getShape(@ColorInt int bgColor, @ColorInt int strokeColor, int strokeWidth, float[] radii, ASTEnum shape) {
        GradientDrawable gradientDrawable = new GradientDrawable(GradientDrawable.Orientation.BOTTOM_TOP, new int[]{});
        gradientDrawable.setStroke(strokeWidth, strokeColor);
        gradientDrawable.setColor(bgColor);
        switch (shape) {
            case RECT_SHAPE:
                gradientDrawable.setGradientType(GradientDrawable.LINEAR_GRADIENT);
                float radius = radii[0];
                gradientDrawable.setCornerRadius(radius);
                break;
            case ROUND_CORNER:
                gradientDrawable.setCornerRadii(radii);
                break;
            case OVAL_SHAPE:
                gradientDrawable.setShape(GradientDrawable.OVAL);
                break;
            case RING_SHAPE:
                gradientDrawable.setShape(GradientDrawable.RING);
                break;
        }
        return gradientDrawable;
    }

    public static GradientDrawable getRectShape(@ColorInt int bgColor, @ColorInt int strokeColor, int strokeWidth, float radius) {
        return getShape(bgColor, strokeColor, strokeWidth, new float[]{radius}, ASTEnum.RECT_SHAPE);
    }

    public static GradientDrawable getRectShape(@ColorInt int bgColor, float radius) {
        return getShape(bgColor, Color.TRANSPARENT, 2, new float[]{radius}, ASTEnum.RECT_SHAPE);
    }

    public static GradientDrawable getRoundCorner(@ColorInt int bgColor, @ColorInt int strokeColor, int strokeWidth, float[] radii) {
        return getShape(bgColor, strokeColor, strokeWidth, radii, ASTEnum.ROUND_CORNER);
    }

    public static GradientDrawable getRoundCorner(@ColorInt int bgColor, float[] radii) {
        return getShape(bgColor, Color.TRANSPARENT, 0, radii, ASTEnum.ROUND_CORNER);
    }

    public static GradientDrawable getOvalShape(@ColorInt int bgColor, @ColorInt int strokeColor, int strokeWidth) {
        return getShape(bgColor, strokeColor, strokeWidth, new float[]{0}, ASTEnum.OVAL_SHAPE);
    }

    public static GradientDrawable getOvalShape(@ColorInt int bgColor) {
        return getShape(bgColor, Color.TRANSPARENT, 0, new float[]{0}, ASTEnum.OVAL_SHAPE);
    }

    public static GradientDrawable getRingShape(@ColorInt int bgColor, @ColorInt int strokeColor, int strokeWidth) {
        return getShape(bgColor, strokeColor, strokeWidth, new float[]{0}, ASTEnum.RING_SHAPE);
    }

    public static GradientDrawable getRingShape(@ColorInt int bgColor) {
        return getShape(bgColor, Color.TRANSPARENT, 0, new float[]{0}, ASTEnum.RING_SHAPE);
    }

    public static int getHeight(Context context) {
        Display display = ((Activity) context).getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        return size.y;
    }

    public static int getScreenWidth(Context context) {
        return context.getResources().getConfiguration().screenWidthDp;
    }

    public static int getScreenHeight(Context context) {
        return context.getResources().getConfiguration().screenHeightDp;
    }

    public static void showKeyBoard(Activity context, View view) {
        if (view == null) {
            return;
        }
        context.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);
        InputMethodManager inputMethodManager = (InputMethodManager) context.getSystemService(Activity.INPUT_METHOD_SERVICE);
        inputMethodManager.toggleSoftInput(InputMethodManager.SHOW_FORCED, InputMethodManager.HIDE_IMPLICIT_ONLY);
    }

    public static void setFirstTimePost(Context context, boolean fflag) {
        try {
            SharedPreferences prefs = context.getSharedPreferences("FirstTimePostPreferences", Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = prefs.edit();
            editor.putBoolean("FirstTime", fflag);
            editor.commit();
        } catch (Exception e) {
            // should never happen
            //   throw new RuntimeException("Could not get language: " + e);
        }
    }

    public static boolean geFirstTimePost(Context context) {
        boolean fflag = false;
        SharedPreferences prefs = context.getSharedPreferences("FirstTimePostPreferences", Context.MODE_PRIVATE);
        if (prefs != null) {
            fflag = prefs.getBoolean("FirstTime", false);
        }
        return fflag;
    }

    public static void setHomeDetail(Context context, String home, boolean HomeEdit) {
        try {
            SharedPreferences prefs = context.getSharedPreferences("HomeDetailPreferences", Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = prefs.edit();
            editor.putString("HomeDetail", home);
            editor.putBoolean("HomeEdit", HomeEdit);
            editor.commit();
        } catch (Exception e) {
            // should never happen
            //   throw new RuntimeException("Could not get language: " + e);
        }
    }

    public static void setHowItsWork(Context context, boolean flag) {
        try {
            SharedPreferences prefs = context.getSharedPreferences("HowItsWorkPreferences", Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = prefs.edit();
            editor.putBoolean("HowItsWork", flag);
            editor.commit();
        } catch (Exception e) {
            // should never happen
        }
    }

    public static String getOsVersion() {
        int index = Build.VERSION.SDK_INT;
        return String.valueOf(index);
    }

}
