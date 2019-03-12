package com.kredivation.switchland.fragment;


import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.kredivation.switchland.R;
import com.kredivation.switchland.activity.HomeDetailActivity;
import com.kredivation.switchland.adapters.FotosAdapter;
import com.kredivation.switchland.adapters.SlidingImage_Adapter_For_ItemDetails;
import com.kredivation.switchland.model.Homegallery;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 */
public class FotosFragmnet extends Fragment {


    public FotosFragmnet() {
        // Required empty public constructor
    }

    String HomeDetail, imageArray;
    RecyclerView rvList;

    public static FotosFragmnet newInstance(String param1, String param2) {
        FotosFragmnet fragment = new FotosFragmnet();
        Bundle args = new Bundle();
        args.putString("HomeDetail", param1);
        args.putString("imageArray", param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            HomeDetail = getArguments().getString("HomeDetail");
            imageArray = getArguments().getString("imageArray");
        }
    }

    View view;
    Context context;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_fotos_fragmnet, container, false);
        context = getActivity();
        init();
        return view;
    }

    private void init() {
        rvList = view.findViewById(R.id.imagerv);
        rvList.setHasFixedSize(false);
        LinearLayoutManager mLayoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false);
        rvList.setLayoutManager(mLayoutManager);
        ArrayList<Homegallery> hImagList = new ArrayList<>();
        try {
            JSONArray homegalleryAttay = new JSONArray(imageArray);
            if (homegalleryAttay != null) {
                imageArray = homegalleryAttay.toString();
                for (int i = 0; i < homegalleryAttay.length(); i++) {
                    JSONObject homeObject = homegalleryAttay.getJSONObject(i);
                    String hid = homeObject.optString("id").toString();
                    String homeid = homeObject.optString("home_id").toString();
                    String photo = homeObject.optString("photo").toString();

                    Homegallery home_image = new Homegallery();
                    home_image.setId(hid);
                    home_image.setHome_id(homeid);
                    home_image.setPhoto(photo);
                    hImagList.add(home_image);
                }
                rvList.setAdapter(new FotosAdapter(getActivity(), hImagList));
            }
        } catch (JSONException e) {
            //e.printStackTrace();
        }


    }
}
