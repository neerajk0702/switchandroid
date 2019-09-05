package com.kredivation.switchland.fragment;


import android.content.Context;
import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;

import com.kredivation.switchland.R;
import com.kredivation.switchland.adapters.CardStackAdapter;
import com.kredivation.switchland.cardstatck.cardstack.CardStack;
import com.kredivation.switchland.cardstatck.cardstack.DefaultStackEventListener;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link LikeDislikeFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class LikeDislikeFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;


    public LikeDislikeFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment LikeDislikeFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static LikeDislikeFragment newInstance(String param1, String param2) {
        LikeDislikeFragment fragment = new LikeDislikeFragment();
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
    CardStack cardStack;
    CardStackAdapter mCardAdapter;
    ImageButton rejectBtn, acceptBtn;
    @Nullable
    private CardStact cardStactinterface;
    Context context;
    View view;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        context=getActivity();
        view= inflater.inflate(R.layout.fragment_like_dislike, container, false);
        init();
        return view;
    }
    private void init(){
        cardStack = view.findViewById(R.id.frame);
        rejectBtn = view.findViewById(R.id.rejectBtn);
        acceptBtn = view.findViewById(R.id.acceptBtn);
        mCardAdapter = new CardStackAdapter(getActivity().getApplicationContext(), 0);
        callCardStack();
        rejectBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
            }
        });

        acceptBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


            }
        });
    }
    //cardStack view will set it as visible and load the information into stack.
    public void callCardStack() {
        cardStack.setContentResource(R.layout.card_stack_item);
        //Adding 30 dummy info for CardStack
        for (int i = 0; i <= 30; i++)
            mCardAdapter.add("" + i);
        cardStack.setAdapter(mCardAdapter);
        cardStack.setListener(new DefaultStackEventListener(300));

    }

    public void setCardListner(CardStact cardStactinterface) {
        this.cardStactinterface = cardStactinterface;
    }

    public interface CardStact {
        void goNext();

        void gopervies();
    }
}
