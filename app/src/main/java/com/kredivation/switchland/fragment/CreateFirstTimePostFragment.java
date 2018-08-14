package com.kredivation.switchland.fragment;


import android.app.DatePickerDialog;
import android.content.Context;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.Fragment;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.kredivation.switchland.R;
import com.kredivation.switchland.utilities.FontManager;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link CreateFirstTimePostFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class CreateFirstTimePostFragment extends Fragment implements View.OnClickListener{
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;


    public CreateFirstTimePostFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment CreateFirstTimePostFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static CreateFirstTimePostFragment newInstance(String param1, String param2) {
        CreateFirstTimePostFragment fragment = new CreateFirstTimePostFragment();
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

    Typeface materialdesignicons_font;
    TextView etYear, enddate;
    LinearLayout dateLayout, endDateLayout;
    private Spinner noOfBedSpinner, noOfGuestSpinner, citySpinner, countrySpinner;
    private TextInputLayout des_layout, hno_layout, title_layout;
    private EditText description, hno, title;
    private String descriptionStr, hnoStr, titleStr;
    private Button submit;
    private View view;
    private Context context;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        context = getActivity();
        view = inflater.inflate(R.layout.fragment_create_first_time_post, container, false);
        initView();
        return view;
    }

    private void initView() {
        TextView dateIcon = view.findViewById(R.id.dateIcon);
        materialdesignicons_font = FontManager.getFontTypefaceMaterialDesignIcons(getContext(), "fonts/materialdesignicons-webfont.otf");
        dateIcon.setTypeface(materialdesignicons_font);
        dateIcon.setText(Html.fromHtml("&#xf0ed;"));
        etYear =  view.findViewById(R.id.etYear);
        dateLayout =  view.findViewById(R.id.dateLayout);
        dateLayout.setOnClickListener(this);

        TextView edateIcon =  view.findViewById(R.id.edateIcon);
        edateIcon.setTypeface(materialdesignicons_font);
        edateIcon.setText(Html.fromHtml("&#xf0ed;"));
        enddate =  view.findViewById(R.id.enddate);
        endDateLayout =  view.findViewById(R.id.dateLayout);
        noOfBedSpinner =  view.findViewById(R.id.noOfBedSpinner);
        noOfGuestSpinner =  view.findViewById(R.id.noOfGuestSpinner);
        citySpinner =  view.findViewById(R.id.citySpinner);
        countrySpinner =  view.findViewById(R.id.countrySpinner);

        des_layout =  view.findViewById(R.id.des_layout);
        hno_layout =  view.findViewById(R.id.hno_layout);
        title_layout =  view.findViewById(R.id.title_layout);
        description =  view.findViewById(R.id.description);
        hno =  view.findViewById(R.id.hno);
        title =  view.findViewById(R.id.title);
        submit =  view.findViewById(R.id.submit);
        dateLayout.setOnClickListener(this);
        endDateLayout.setOnClickListener(this);
        submit.setOnClickListener(this);

        final String bed_array[] = {"Savings Account", "Current Account", "Money Market Investment Accounts"};
        ArrayAdapter<String> bankAdapter = new ArrayAdapter<String>(getContext(), R.layout.spinner_row, bed_array);
        noOfBedSpinner.setAdapter(bankAdapter);
        noOfBedSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                //  TextView textview = view.findViewById(R.id.cust_view);
                String str = bed_array[position];
                if (str.equals("Savings Account")) {

                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }
    public void setStartDate() {
        String myFormat = "yyyy-MM-dd"; //In which you need put here
        final SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);
        final Calendar myCalendar = Calendar.getInstance();
        final DatePickerDialog.OnDateSetListener date = new DatePickerDialog.OnDateSetListener() {

            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear,
                                  int dayOfMonth) {
                // TODO Auto-generated method stub
                myCalendar.set(Calendar.YEAR, year);
                myCalendar.set(Calendar.MONTH, monthOfYear);
                myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                etYear.setText(sdf.format(myCalendar.getTime()));
                // datemilisec = myCalendar.getTimeInMillis();
            }
        };
        dateLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                new DatePickerDialog(getContext(), date, myCalendar
                        .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                        myCalendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });
    }

    public void setEndDate() {
        String myFormat = "yyyy-MM-dd"; //In which you need put here
        final SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);
        final Calendar myCalendar = Calendar.getInstance();
        final DatePickerDialog.OnDateSetListener date = new DatePickerDialog.OnDateSetListener() {

            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear,
                                  int dayOfMonth) {
                // TODO Auto-generated method stub
                myCalendar.set(Calendar.YEAR, year);
                myCalendar.set(Calendar.MONTH, monthOfYear);
                myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                enddate.setText(sdf.format(myCalendar.getTime()));
                // datemilisec = myCalendar.getTimeInMillis();
            }
        };
        endDateLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                new DatePickerDialog(getContext(), date, myCalendar
                        .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                        myCalendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });
    }



    // ----validation -----
    private boolean isValidate() {
        String emailRegex = "[A-Z0-9a-z._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,4}";
        titleStr = title.getText().toString();
        hnoStr = hno.getText().toString();
        descriptionStr = description.getText().toString();
        if (titleStr.length() == 0) {
            title_layout.setError("Please Enter Title");
            requestFocus(title);
            return false;
        } else if (hnoStr.length() == 0) {
            hno_layout.setError("Please Enter House No");
            requestFocus(hno);
            return false;
        } else if (descriptionStr.length() == 0) {
            des_layout.setError("Please Enter Description");
            requestFocus(description);
            return false;
        } else {
            title_layout.setErrorEnabled(false);
            hno_layout.setErrorEnabled(false);
            des_layout.setErrorEnabled(false);
        }
        return true;
    }

    private void requestFocus(View view) {
        if (view.requestFocus()) {
            getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
        }
    }
    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.dateLayout:
                setStartDate();
                break;
            case R.id.endDateLayout:
                setEndDate();
                break;
            case R.id.submit:
                if (isValidate()) {

                }
                break;
        }
    }
}
