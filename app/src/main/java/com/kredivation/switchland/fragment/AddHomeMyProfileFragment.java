package com.kredivation.switchland.fragment;


import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.kredivation.switchland.R;
import com.kredivation.switchland.adapters.AddHomePhotoAdapter;
import com.kredivation.switchland.model.ChatData;
import com.kredivation.switchland.utilities.ASTProgressBar;
import com.kredivation.switchland.utilities.FontManager;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link AddHomeMyProfileFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class AddHomeMyProfileFragment extends Fragment implements View.OnClickListener{
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
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        context = getActivity();
        view =  inflater.inflate(R.layout.fragment_add_home_my_profile, container, false);
        init();
        return view;
    }
    private void init() {

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
        Button upload = view.findViewById(R.id.upload);
        upload.setOnClickListener(this);
        Typeoftravellerspinner = view.findViewById(R.id.Typeoftravellerspinner);
        String home_array[] = {"Family", "Couple", "Solo","Other"};
        ArrayAdapter<String> homeadapter = new ArrayAdapter<String>(context, R.layout.spinner_row, home_array);
        Typeoftravellerspinner.setAdapter(homeadapter);
    }
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.nextLayout:
                saveScreenData(true, false);
                break;
            case R.id.previous:
                saveScreenData(false, false);
                break;

        }
    }

    private void saveScreenData(boolean NextPreviousFlag, boolean DoneFlag) {
        Intent intent = new Intent("ViewPageChange");
        intent.putExtra("NextPreviousFlag", NextPreviousFlag);
        intent.putExtra("DoneFlag", DoneFlag);
        getActivity().sendBroadcast(intent);
    }

}
