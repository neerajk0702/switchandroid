package com.kredivation.switchland.fragment;


import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.kredivation.switchland.R;
import com.kredivation.switchland.adapters.AddHomePagerAdapter;
import com.kredivation.switchland.utilities.FontManager;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link AddHomeOverviewFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class AddHomeOverviewFragment extends Fragment implements View.OnClickListener {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;


    public AddHomeOverviewFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment AddHomeOverviewFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static AddHomeOverviewFragment newInstance(String param1, String param2) {
        AddHomeOverviewFragment fragment = new AddHomeOverviewFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    private View view;
    private Context context;
    private Spinner homeStyleSpinner, bedroomspinner, bathroomsspinner, Sleepspinner, Typeofpropertyspinner, PetAllowedspinner, Familymattersspinner;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        context = getActivity();
        view = inflater.inflate(R.layout.fragment_add_home_overview, container, false);
        init();
        return view;
    }

    private void init() {
        homeStyleSpinner = view.findViewById(R.id.homeStyleSpinner);
        bedroomspinner = view.findViewById(R.id.bedroomspinner);
        bathroomsspinner = view.findViewById(R.id.bathroomsspinner);
        Typeofpropertyspinner = view.findViewById(R.id.Typeofpropertyspinner);
        Sleepspinner = view.findViewById(R.id.Sleepspinner);
        PetAllowedspinner = view.findViewById(R.id.PetAllowedspinner);
        Familymattersspinner = view.findViewById(R.id.Familymattersspinner);
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
        setSpinerValue();
    }

    private void setSpinerValue() {

        String home_array[] = {"City Pad", "By the sea", "Ski chalet"};
        final String bedrooom_array[] = {"1", "2", "3", "4", "5","6", "7", "8", "9", "10"};
        final String bathroom_array[] = {"1", "2", "3", "4", "5", "6", "7", "8", "9", "10"};
        final String sleep_array[] = {"1", "2", "3", "4", "5","6", "7", "8", "9", "10"};
        final String type_array[] = {"Main Home", "Vacation Home", "Time Share"};
        final String pet_array[] = {"I don't Mind", "Pet Friendly", "No Pet"};
        final String family_array[] = {"Bring the kids ", "Just for grown-ups"};


        ArrayAdapter<String> homeadapter = new ArrayAdapter<String>(context, R.layout.spinner_row, home_array);
        homeStyleSpinner.setAdapter(homeadapter);
        ArrayAdapter<String> bedroomadapter = new ArrayAdapter<String>(context, R.layout.spinner_row, bedrooom_array);
        bedroomspinner.setAdapter(bedroomadapter);
        ArrayAdapter<String> bathroomadapter = new ArrayAdapter<String>(context, R.layout.spinner_row, bathroom_array);
        bathroomsspinner.setAdapter(bathroomadapter);
        ArrayAdapter<String> sleepadapter = new ArrayAdapter<String>(context, R.layout.spinner_row, sleep_array);
        Sleepspinner.setAdapter(sleepadapter);
        ArrayAdapter<String> typedapter = new ArrayAdapter<String>(context, R.layout.spinner_row, type_array);
        Typeofpropertyspinner.setAdapter(typedapter);
        ArrayAdapter<String> petadapter = new ArrayAdapter<String>(context, R.layout.spinner_row, pet_array);
        PetAllowedspinner.setAdapter(petadapter);
        ArrayAdapter<String> familyadapter = new ArrayAdapter<String>(context, R.layout.spinner_row, family_array);
        Familymattersspinner.setAdapter(familyadapter);

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
