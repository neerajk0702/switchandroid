package com.kredivation.switchland.fragment;


import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.Typeface;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.kredivation.switchland.R;
import com.kredivation.switchland.adapters.AddHomePhotoAdapter;
import com.kredivation.switchland.database.SwitchDBHelper;
import com.kredivation.switchland.model.ChatData;
import com.kredivation.switchland.model.Data;
import com.kredivation.switchland.model.Features;
import com.kredivation.switchland.model.HomeDetails;
import com.kredivation.switchland.model.House_rules;
import com.kredivation.switchland.model.MyhomeArray;
import com.kredivation.switchland.model.ServiceContentData;
import com.kredivation.switchland.model.Type_of_traveller;
import com.kredivation.switchland.utilities.ASTProgressBar;
import com.kredivation.switchland.utilities.Contants;
import com.kredivation.switchland.utilities.FontManager;
import com.kredivation.switchland.utilities.Utility;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.io.FileOutputStream;
import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link AddHomeMyProfileFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class AddHomeMyProfileFragment extends Fragment implements View.OnClickListener {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;


    public AddHomeMyProfileFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment AddHomeMyProfileFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static AddHomeMyProfileFragment newInstance(String param1, String param2) {
        AddHomeMyProfileFragment fragment = new AddHomeMyProfileFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    private View view;
    private Context context;
    public final int REQUEST_CAMERA = 101;
    public final int SELECT_PHOTO = 102;
    private String userChoosenTask;
    ASTProgressBar astProgressBar;
    private Spinner Typeoftravellerspinner;
    private TextInputLayout input_layout_dream;
    private EditText dream;
    private String dreamStr;
    ImageView profileImage;
    File imgFile;
    String[] travleList;
    Type_of_traveller[] travel;
    String travleIdStr = "";
    HomeDetails MyHomedata;
    String homeId;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        context = getActivity();
        view = inflater.inflate(R.layout.fragment_add_home_my_profile, container, false);
        init();
        return view;
    }

    private void init() {
        dream = view.findViewById(R.id.dream);
        input_layout_dream = view.findViewById(R.id.input_layout_dream);
        profileImage = view.findViewById(R.id.profileImage);
        Typeface materialdesignicons_font = FontManager.getFontTypefaceMaterialDesignIcons(getActivity(), "fonts/materialdesignicons-webfont.otf");
        TextView previous = (TextView) view.findViewById(R.id.previous);
        previous.setTypeface(materialdesignicons_font);
        previous.setText(Html.fromHtml("&#xf141;"));
        previous.setOnClickListener(this);
        TextView nextIcon = (TextView) view.findViewById(R.id.nextIcon);
        nextIcon.setTypeface(materialdesignicons_font);
        nextIcon.setText(Html.fromHtml("&#xf142;"));
        LinearLayout nextLayout = (LinearLayout) view.findViewById(R.id.nextLayout);
        nextLayout.setOnClickListener(this);
        Button upload = view.findViewById(R.id.upload);
        upload.setOnClickListener(this);
        Typeoftravellerspinner = view.findViewById(R.id.Typeoftravellerspinner);
        getSaveData();
        Typeoftravellerspinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                travleIdStr = travel[position].getId();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    private void getAllDataFromDB() {
        final ASTProgressBar dotDialog = new ASTProgressBar(getContext());
        dotDialog.show();
        new AsyncTask<Void, Void, Boolean>() {
            @Override
            protected Boolean doInBackground(Void... voids) {
                Boolean flag = false;
                SwitchDBHelper switchDBHelper = new SwitchDBHelper(context);
                ServiceContentData sData = switchDBHelper.getMasterData();
                if (sData != null) {
                    if (sData.getData() != null) {
                        Data MData = sData.getData();
                        travel = MData.getType_of_traveller();
                        travleList = new String[travel.length];
                        if (travel != null) {
                            for (int i = 0; i < travel.length; i++) {
                                travleList[i] = travel[i].getName();
                            }
                        }
                    }
                }
                flag = true;
                return flag;
            }

            @Override
            protected void onPostExecute(Boolean flag) {
                super.onPostExecute(flag);
                ArrayAdapter<String> homeadapter = new ArrayAdapter<String>(context, R.layout.spinner_row, travleList);
                for (int i = 0; i < travel.length; i++) {
                    if (travleIdStr.equals(travel[i].getId())) {
                        Typeoftravellerspinner.setSelection(i);
                    }
                }
                Typeoftravellerspinner.setAdapter(homeadapter);
                if (dotDialog.isShowing()) {
                    dotDialog.dismiss();
                }
            }
        }.execute();
    }

    //get data from pre
    private void getSaveData() {

       /* SharedPreferences prefs = context.getSharedPreferences("HomeDetailPreferences", Context.MODE_PRIVATE);
        if (prefs != null) {
                String Myhome = prefs.getString("HomeDetail", "");
                if (Myhome != null && !Myhome.equals("")) {
                    MyHomedata = new Gson().fromJson(Myhome, new TypeToken<HomeDetails>() {
                    }.getType());

                    if (MyHomedata != null) {//for home editprofileImfLocal
                        homeId = MyHomedata.getId();
                        dreamStr = MyHomedata.getDestinations();
                        dream.setText(dreamStr);
                        travleIdStr = MyHomedata.getTraveller_type();
                        String imgFileStr = MyHomedata.getProfile_image();
                        imgFile=new File(imgFileStr);
                        if (imgFile != null && imgFile.exists()) {
                            Picasso.with(context).load(imgFile).placeholder(R.drawable.userimage).into(profileImage);
                        }else{
                            Picasso.with(context).load(imgFileStr).placeholder(R.drawable.userimage).into(profileImage);
                        }

                }
            }
        }*/
        SwitchDBHelper dbHelper = new SwitchDBHelper(getActivity());
        ArrayList<HomeDetails> homeDetails = dbHelper.getAllAddEditHomeDataList();
        if (homeDetails != null) {
            for (HomeDetails details : homeDetails) {
                MyHomedata = details;
                if (MyHomedata != null) {//for home edit
                    homeId = MyHomedata.getId();
                    dreamStr = MyHomedata.getDestinations();
                    dream.setText(dreamStr);
                    travleIdStr = MyHomedata.getTraveller_type();
                    String imgFileStr = MyHomedata.getProfile_image();
                    imgFile=new File(imgFileStr);
                    if (imgFile != null && imgFile.exists()) {
                        Picasso.with(context).load(imgFile).placeholder(R.drawable.userimage).into(profileImage);
                    }else{
                        Picasso.with(context).load(imgFileStr).placeholder(R.drawable.userimage).into(profileImage);
                    }
                }
            }
        }
        getAllDataFromDB();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.nextLayout:
                if (isValidate()) {
                    saveScreenData(true, false);
                }
                break;
            case R.id.previous:
                saveScreenData(false, false);
                break;
            case R.id.upload:
                selectImage();
                break;
        }
    }

    // ----validation -----
    private boolean isValidate() {
        dreamStr = dream.getText().toString();
        if (dreamStr.length() == 0) {
            input_layout_dream.setError("Please Enter Dream detail");
            requestFocus(dream);
            return false;
        } else {
            input_layout_dream.setErrorEnabled(false);
            input_layout_dream.setError("");
        }
        return true;
    }

    private void requestFocus(View view) {
        if (view.requestFocus()) {
            getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
        }
    }

    private void saveScreenData(boolean NextPreviousFlag, boolean DoneFlag) {
        saveData();
        Intent intent = new Intent("ViewPageChange");
        intent.putExtra("NextPreviousFlag", NextPreviousFlag);
        intent.putExtra("DoneFlag", DoneFlag);
        getActivity().sendBroadcast(intent);
    }

    //save data
    private void saveData() {
        if (MyHomedata != null) {
           /* MyHomedata.setTraveller_type(travleIdStr);
            if (imgFile != null && imgFile.exists()) {
                MyHomedata.setProfile_image(imgFile.getAbsolutePath());
            }
            MyHomedata.setDestinations(dreamStr);
            String homeStr = new Gson().toJson(MyHomedata);
            Utility.setHomeDetail(context, homeStr, true);
*/
            HomeDetails details = new HomeDetails();
            details.setId(homeId);
            details.setTraveller_type(travleIdStr);
            details.setProfile_image(imgFile.getAbsolutePath());
            details.setDestinations(dreamStr);
            SwitchDBHelper dbHelper=new SwitchDBHelper(getActivity());
            dbHelper.updateAddEditHomeProfile(details);
        }
    }

    private void selectImage() {
        final CharSequence[] items = {"Take Photo", "Choose from Library", "Cancel"};
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
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
                Bitmap imageBitmap = MediaStore.Images.Media.getBitmap(context.getContentResolver(), data.getData());
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
        Uri uri = Utility.getImageUri(context, thumbnail);

        if (uri != null) {
            setImageView(uri, thumbnail);
        }
    }

    private void setImageView(Uri uri, Bitmap imageBitmap) {
        String homeStr = "Home" + System.currentTimeMillis() + ".png";
        addBitmapAsFile(imageBitmap, homeStr);
    }

    //add bitmap into list
    private Boolean addBitmapAsFile(final Bitmap bitmap, final String fileName) {
        new AsyncTask<Void, Void, Boolean>() {
            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                astProgressBar = new ASTProgressBar(context);
                astProgressBar.show();
            }

            @Override
            protected Boolean doInBackground(Void... voids) {

                Boolean flag = false;
                File sdcardPath = Utility.getExternalStorageFilePath(context);
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
                MediaScannerConnection.scanFile(context, new String[]{imgFile.toString()}, null,
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
                Picasso.with(context).load(imgFile).placeholder(R.drawable.userimage).into(profileImage);
                astProgressBar.dismiss();
            }
        }.execute();

        return true;
    }
}
