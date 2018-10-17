package com.kredivation.switchland.fragment;


import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.gson.Gson;
import com.kredivation.switchland.R;
import com.kredivation.switchland.activity.MyHomeActivity;
import com.kredivation.switchland.adapters.ChatListAdapter;
import com.kredivation.switchland.database.SwitchDBHelper;
import com.kredivation.switchland.framework.IAsyncWorkCompletedCallback;
import com.kredivation.switchland.framework.ServiceCaller;
import com.kredivation.switchland.model.ChatData;
import com.kredivation.switchland.model.ChatServiceContentData;
import com.kredivation.switchland.model.Data;
import com.kredivation.switchland.model.Home_data;
import com.kredivation.switchland.model.Home_liked_disliked;
import com.kredivation.switchland.model.MyhomeArray;
import com.kredivation.switchland.model.ServiceContentData;
import com.kredivation.switchland.utilities.ASTProgressBar;
import com.kredivation.switchland.utilities.Contants;
import com.kredivation.switchland.utilities.Utility;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ChatListFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ChatListFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;


    public ChatListFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment ChatListFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static ChatListFragment newInstance(String param1, String param2) {
        ChatListFragment fragment = new ChatListFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    ASTProgressBar dotDialog;

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
    private String userId;
    private ArrayList<Data> chatList;
    RecyclerView recyclerView;
    ChatListAdapter mAdapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        context = getActivity();
        view = inflater.inflate(R.layout.fragment_chat_list, container, false);
        init();
        return view;
    }

    private void init() {
        recyclerView = view.findViewById(R.id.recycler_view);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
    }

    @Override
    public void onResume() {
        super.onResume();
        getUserdata();
    }

    private void getUserdata() {
        String homeId = "";
        SwitchDBHelper switchDBHelper = new SwitchDBHelper(context);
        ArrayList<Data> userData = switchDBHelper.getAllUserInfoList();
        if (userData != null && userData.size() > 0) {
            for (Data data : userData) {
                userId = data.getId();
            }
            ArrayList<MyhomeArray> myHomeList = switchDBHelper.getAllMyhomedata();
            if (myHomeList != null && myHomeList.size() > 0) {
                for (MyhomeArray myhomeArray : myHomeList) {
                    homeId = myhomeArray.getId();
                    break;
                }
                getChatMemberlist(homeId);
            }
        }
    }

    private void getChatMemberlist(String homeId) {
        if (Utility.isOnline(getContext())) {
            dotDialog = new ASTProgressBar(getContext());
            //dotDialog.show();
            JSONObject object = new JSONObject();
            try {
                object.put("api_key", Contants.API_KEY);
                object.put("user_id", userId);
                object.put("home_id", homeId);
            } catch (JSONException e) {
                e.printStackTrace();
            }

            String serviceURL = Contants.BASE_URL + Contants.Getchatmember;

            ServiceCaller serviceCaller = new ServiceCaller(getContext());
            serviceCaller.CallCommanServiceMethod(serviceURL, object, "getChatMemberlist", new IAsyncWorkCompletedCallback() {
                @Override
                public void onDone(String result, boolean isComplete) {
                    if (isComplete) {
                        parseChatListServiceData(result);
                    } else {
                        if (dotDialog.isShowing()) {
                            dotDialog.dismiss();
                        }
                        Utility.alertForErrorMessage(Contants.Error, getContext());
                    }
                }
            });
        } else {
            Utility.alertForErrorMessage(Contants.OFFLINE_MESSAGE, getContext());//off line msg....
        }
    }

    public void parseChatListServiceData(String result) {
        if (result != null) {
            final ChatServiceContentData serviceData = new Gson().fromJson(result, ChatServiceContentData.class);
            if (serviceData != null) {
                if (serviceData.isSuccess()) {
                    if (serviceData.getData() != null) {
                        new AsyncTask<Void, Void, Boolean>() {
                            @Override
                            protected Boolean doInBackground(Void... voids) {
                                Boolean flag = false;
                                chatList = new ArrayList<>();
                                Data[] chat_data = serviceData.getData();
                                if (chat_data != null) {
                                    for (Data data : chat_data) {
                                        chatList.add(data);
                                    }
                                    flag = true;
                                }
                                return flag;
                            }

                            @Override
                            protected void onPostExecute(Boolean flag) {
                                super.onPostExecute(flag);
                                if (flag) {
                                    mAdapter = new ChatListAdapter(context, chatList);
                                    recyclerView.setAdapter(mAdapter);
                                }
                                if (dotDialog.isShowing()) {
                                    dotDialog.dismiss();
                                }
                            }
                        }.execute();
                    } else {
                        if (dotDialog.isShowing()) {
                            dotDialog.dismiss();
                        }
                    }
                } else {
                    //Toast.makeText(getContext(), serviceData.getMsg(), Toast.LENGTH_LONG).show();
                    if (dotDialog.isShowing()) {
                        dotDialog.dismiss();
                    }
                }
            } else {
                if (dotDialog.isShowing()) {
                    dotDialog.dismiss();
                }
            }
        } else {
            if (dotDialog.isShowing()) {
                dotDialog.dismiss();
            }
        }
    }

}
