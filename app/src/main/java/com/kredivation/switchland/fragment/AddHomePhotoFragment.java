package com.kredivation.switchland.fragment;


import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Typeface;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.MediaStore;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import androidx.fragment.app.Fragment;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.kredivation.switchland.R;
import com.kredivation.switchland.activity.MyChoicesActivity;
import com.kredivation.switchland.adapters.AddHomePhotoAdapter;
import com.kredivation.switchland.database.SwitchDBHelper;
import com.kredivation.switchland.model.HomeDetails;
import com.kredivation.switchland.model.Homegallery;
import com.kredivation.switchland.utilities.ASTProgressBar;
import com.kredivation.switchland.utilities.Contants;
import com.kredivation.switchland.utilities.FontManager;
import com.kredivation.switchland.utilities.Utility;

import java.io.File;
import java.io.FileOutputStream;
import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link AddHomePhotoFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class AddHomePhotoFragment extends Fragment implements View.OnClickListener {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;


    public AddHomePhotoFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment AddHomePhotoFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static AddHomePhotoFragment newInstance(String param1, String param2) {
        AddHomePhotoFragment fragment = new AddHomePhotoFragment();
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
    ArrayList<Homegallery> locationList;
    AddHomePhotoAdapter mAdapter;
    RecyclerView recyclerView;
    HomeDetails MyHomedata;
    String homeId;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        context = getActivity();
        view = inflater.inflate(R.layout.fragment_add_home_photo, container, false);
        init();
        return view;
    }

    private void init() {
        locationList = new ArrayList<Homegallery>();
        Typeface materialdesignicons_font = FontManager.getFontTypefaceMaterialDesignIcons(getActivity(), "fonts/materialdesignicons-webfont.otf");
        TextView nextIcon = (TextView) view.findViewById(R.id.nextIcon);
        nextIcon.setTypeface(materialdesignicons_font);
        nextIcon.setText(Html.fromHtml("&#xf142;"));
        TextView previous = (TextView) view.findViewById(R.id.previous);
        previous.setTypeface(materialdesignicons_font);
        previous.setText(Html.fromHtml("&#xf141;"));
        previous.setOnClickListener(this);
        LinearLayout nextLayout = (LinearLayout) view.findViewById(R.id.nextLayout);
        nextLayout.setOnClickListener(this);
        ImageView addimg = view.findViewById(R.id.addimg);
        addimg.setOnClickListener(this);

        recyclerView = view.findViewById(R.id.recycler_view);
        recyclerView.setHasFixedSize(false);
        //StaggeredGridLayoutManager gaggeredGridLayoutManager = new StaggeredGridLayoutManager(2, LinearLayoutManager.VERTICAL);
        //recyclerView.setLayoutManager(gaggeredGridLayoutManager);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false);
        recyclerView.setLayoutManager(mLayoutManager);
        getSaveData();
    }

    @Override
    public void onResume() {
        super.onResume();

    }

    //get data from pre
    private void getSaveData() {
       /* SharedPreferences prefs = context.getSharedPreferences("HomeDetailPreferences", Context.MODE_PRIVATE);
        if (prefs != null) {
            String Myhome = prefs.getString("HomeDetail", "");
            if (Myhome != null && !Myhome.equals("")) {
                MyHomedata = new Gson().fromJson(Myhome, new TypeToken<HomeDetails>() {
                }.getType());

                if (MyHomedata != null) {//for home edit
                    homeId = MyHomedata.getId();
                    locationList = new ArrayList<>();
                    // locationList = MyHomedata.getHomeImageList();
                    if (MyHomedata.getHomegallery() != null && MyHomedata.getHomegallery().length > 0) {
                        for (Homegallery home : MyHomedata.getHomegallery()) {
                            locationList.add(home);
                        }
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
                    locationList = new ArrayList<>();
                    // locationList = MyHomedata.getHomeImageList();
                    if (MyHomedata.getHomegallery() != null && MyHomedata.getHomegallery().length > 0) {
                        for (Homegallery home : MyHomedata.getHomegallery()) {//for server image
                            locationList.add(home);
                        }
                    }
                    if (MyHomedata.getHomeImageList() != null && MyHomedata.getHomeImageList().size() > 0) {
                        for (Homegallery home : MyHomedata.getHomeImageList()) {//for local select images
                            locationList.add(home);
                        }
                    }
                }
            }
        }
        mAdapter = new AddHomePhotoAdapter(context, locationList);
        recyclerView.setAdapter(mAdapter);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.nextLayout:
                if (locationList.size() > 0) {
                    saveScreenData(true, false);
                } else {
                    Utility.showToast(context, "Please Select Home Images");
                }
                break;
            case R.id.previous:
                saveScreenData(false, false);
                break;
            case R.id.addimg:
                selectImage();
                break;
        }
    }

    //save data
    private void saveData() {

        if (MyHomedata != null) {
           /* MyHomedata.setHomeImageList(locationList);
            String homeStr = new Gson().toJson(MyHomedata);
            Utility.setHomeDetail(context, homeStr, true);*/
            HomeDetails details = new HomeDetails();
            details.setId(homeId);
            details.setHomeImageList(locationList);
            SwitchDBHelper dbHelper = new SwitchDBHelper(getActivity());
            dbHelper.updateAddEditHomeImage(details);
        }
    }

    private void saveScreenData(boolean NextPreviousFlag, boolean DoneFlag) {
        saveData();
        Intent intent = new Intent("ViewPageChange");
        intent.putExtra("NextPreviousFlag", NextPreviousFlag);
        intent.putExtra("DoneFlag", DoneFlag);
        getActivity().sendBroadcast(intent);
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
            File imgFile;

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
                // Picasso.with(context).load(imgFile).into(faultImage);
                setImageIntoList(imgFile);
            }
        }.execute();

        return true;
    }

    //set image into list and notify adapter
    private void setImageIntoList(File imgFile) {
        Homegallery homegallery = new Homegallery();
        homegallery.setPhoto(imgFile.getAbsolutePath());
        locationList.add(homegallery);
        //mAdapter.notifyDataSetChanged();
        mAdapter = new AddHomePhotoAdapter(context, locationList);
        recyclerView.setAdapter(mAdapter);

        if (astProgressBar.isShowing()) {
            astProgressBar.dismiss();
        }
    }


}
