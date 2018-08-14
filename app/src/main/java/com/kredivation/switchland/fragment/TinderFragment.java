package com.kredivation.switchland.fragment;


import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.Adapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.kredivation.switchland.R;
import com.kredivation.switchland.fragment.likepagelib.KoldaListnerJava;
import com.kredivation.switchland.fragment.likepagelib.KoldaMain;
import com.kredivation.switchland.fragment.likepagelib.TinderCardAdapter;
import com.kredivation.switchland.utilities.Utility;

import java.util.Arrays;
import java.util.HashMap;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link TinderFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class TinderFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;


    public TinderFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment TinderFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static TinderFragment newInstance(String param1, String param2) {
        TinderFragment fragment = new TinderFragment();
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

    private TinderCardAdapter adapter;
    private HashMap _$_findViewCache;
    Context context;
    private View view;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        context = getActivity();
        view = inflater.inflate(R.layout.fragment_tinder, container, false);
        initView();
        return view;
    }

    private void initView() {
        ((LinearLayout) view.findViewById(R.id.activityMain)).getViewTreeObserver().addOnGlobalLayoutListener((ViewTreeObserver.OnGlobalLayoutListener) (new ViewTreeObserver.OnGlobalLayoutListener() {
            @SuppressLint({"NewApi"})
            public void onGlobalLayout() {
                int width = ((LinearLayout) view.findViewById(R.id.activityMain)).getWidth();
                int height = ((LinearLayout) view.findViewById(R.id.activityMain)).getHeight();
                if (Build.VERSION.SDK_INT >= 16) {
                    ((LinearLayout) view.findViewById(R.id.activityMain)).getViewTreeObserver().removeOnGlobalLayoutListener((ViewTreeObserver.OnGlobalLayoutListener) this);
                } else {
                    ((LinearLayout) view.findViewById(R.id.activityMain)).getViewTreeObserver().removeGlobalOnLayoutListener((ViewTreeObserver.OnGlobalLayoutListener) this);
                }

            }
        }));
        this.initializeDeck();
        this.fillData();
        this.setUpCLickListeners();
    }

    private void initializeDeck() {
        ((KoldaMain) view.findViewById(R.id.koloda)).setKolodaListener((KoldaListnerJava) (new KoldaListnerJava() {
            private int cardsSwiped;

            public final int getCardsSwiped$production_sources_for_module_app() {
                return this.cardsSwiped;
            }

            public final void setCardsSwiped$production_sources_for_module_app(int var1) {
                this.cardsSwiped = var1;
            }

            public void onNewTopCard(int position) {
            }

            public void onCardSwipedLeft(int position) {
            }

            public void onCardSwipedRight(int position) {
            }

            public void onEmptyDeck() {
            }

            public void onCardDrag(int position, View cardView, float progress) {
                DefaultImpls.onCardDrag(this, position, cardView, progress);
            }

            public void onClickRight(int position) {
                DefaultImpls.onClickRight(this, position);
            }

            public void onClickLeft(int position) {
                DefaultImpls.onClickLeft(this, position);
            }

            public void onCardSingleTap(int position) {
                DefaultImpls.onCardSingleTap(this, position);
            }

            public void onCardDoubleTap(int position) {
                DefaultImpls.onCardDoubleTap(this, position);
            }

            public void onCardLongPress(int position) {
                DefaultImpls.onCardLongPress(this, position);
            }
        }));
    }

    private void fillData() {
        Integer[] data = new Integer[]{R.drawable.a,
                R.drawable.b,
                R.drawable.c,
                R.drawable.d,
                R.drawable.e,
                R.drawable.a,
                R.drawable.b,
                R.drawable.c,
                R.drawable.d,
                R.drawable.e,
                R.drawable.e,
                R.drawable.b,
                R.drawable.d};
        this.adapter = new TinderCardAdapter(getContext(), Arrays.asList(data));
        ((KoldaMain) view.findViewById(R.id.koloda)).setAdapter((Adapter) this.adapter);
        ((KoldaMain) view.findViewById(R.id.koloda)).setNeedCircleLoading(true);
    }

    private void setUpCLickListeners() {
        ImageView dislike = view.findViewById(R.id.activityMain).findViewById(R.id.dislike);
        ImageView like = view.findViewById(R.id.activityMain).findViewById(R.id.like);
        Button skip = view.findViewById(R.id.skip);
        Utility.setBackgroundOval(context, skip, R.color.blue_color, Utility.getColor(context, R.color.gray), 0);
        Utility.setBackgroundRing(context, dislike, R.color.light_gray_color, Utility.getColor(context, R.color.light_gray_color), 0);
        Utility.setBackgroundRing(context, like, R.color.light_gray_color, Utility.getColor(context, R.color.light_gray_color), 0);
        dislike.setOnClickListener((View.OnClickListener) (new View.OnClickListener() {
            public void onClick(View it) {
                ((KoldaMain) view.findViewById(R.id.koloda)).onClickLeft();
            }
        }));
        like.setOnClickListener((View.OnClickListener) (new View.OnClickListener() {
            public void onClick(View it) {
                ((KoldaMain) view.findViewById(R.id.koloda)).onClickRight();
            }
        }));

        skip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((KoldaMain) view.findViewById(R.id.koloda)).onClickSkip();
            }
        });
    }
}
