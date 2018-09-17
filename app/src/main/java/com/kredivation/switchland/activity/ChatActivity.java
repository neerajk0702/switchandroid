package com.kredivation.switchland.activity;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.kredivation.switchland.R;
import com.kredivation.switchland.adapters.ChatListAdapter;
import com.kredivation.switchland.adapters.ChatMessageAdapter;
import com.kredivation.switchland.database.SwitchDBHelper;
import com.kredivation.switchland.framework.IAsyncWorkCompletedCallback;
import com.kredivation.switchland.framework.ServiceCaller;
import com.kredivation.switchland.model.ChatServiceContentData;
import com.kredivation.switchland.model.Data;
import com.kredivation.switchland.model.Home_data;
import com.kredivation.switchland.model.Home_liked_disliked;
import com.kredivation.switchland.model.MyhomeArray;
import com.kredivation.switchland.model.ServiceContentData;
import com.kredivation.switchland.utilities.ASTProgressBar;
import com.kredivation.switchland.utilities.CompatibilityUtility;
import com.kredivation.switchland.utilities.Contants;
import com.kredivation.switchland.utilities.FontManager;
import com.kredivation.switchland.utilities.Utility;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Timer;
import java.util.TimerTask;

public class ChatActivity extends AppCompatActivity implements View.OnClickListener {
    private Toolbar toolbar;
    private String userId;
    String ChatUserId, ProfileImg, FullName, HomeId;
    RecyclerView recyclerView;
    ChatMessageAdapter adapter;
    ASTProgressBar chatlistProgress;
    ArrayList<Data> megList;
    private EditText et_comment;
    private TextView add_comment;
    Timer timer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if (CompatibilityUtility.isTablet(ChatActivity.this)) {
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        } else {
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        }
        init();
    }

    private void init() {
        Typeface materialdesignicons_font = FontManager.getFontTypefaceMaterialDesignIcons(this, "fonts/materialdesignicons-webfont.otf");
        TextView back = (TextView) toolbar.findViewById(R.id.back);
        back.setTypeface(materialdesignicons_font);
        back.setText(Html.fromHtml("&#xf30d;"));
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        et_comment = findViewById(R.id.et_comment);
        add_comment = findViewById(R.id.add_comment);
        add_comment.setTypeface(materialdesignicons_font);
        add_comment.setText(Html.fromHtml("&#xf48a;"));
        add_comment.setOnClickListener(this);
        Button switchbt = findViewById(R.id.switchbt);
        switchbt.setOnClickListener(this);
        getUserdata();
        megList = new ArrayList<>();
        recyclerView = findViewById(R.id.recycler_view);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(ChatActivity.this, LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        adapter = new ChatMessageAdapter(ChatActivity.this, megList, userId);
        recyclerView.setAdapter(adapter);
    }

    private void getUserdata() {
        ChatUserId = getIntent().getStringExtra("ChatUserId");
        ProfileImg = getIntent().getStringExtra("ProfileImg");
        FullName = getIntent().getStringExtra("FullName");
        HomeId = getIntent().getStringExtra("HomeId");
        SwitchDBHelper switchDBHelper = new SwitchDBHelper(ChatActivity.this);
        ArrayList<MyhomeArray> myHomeList = switchDBHelper.getAllMyhomedata();
        ArrayList<Data> userData = switchDBHelper.getAllUserInfoList();
        if (userData != null && userData.size() > 0) {
            for (Data data : userData) {
                userId = data.getId();
            }
        }
        callGetMessageService();
    }

    private void callGetMessageService() {
        final Handler handler = new Handler();
        timer = new Timer();
        TimerTask doAsynchronousTask = new TimerTask() {
            @Override
            public void run() {
                handler.post(new Runnable() {
                    public void run() {
                        try {
                            getAllChatMessage();
                        } catch (Exception e) {
                            // TODO Auto-generated catch block
                        }
                    }
                });
            }
        };
        timer.schedule(doAsynchronousTask, 0, 3000); //execute in every 50000 ms
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (timer != null) {
            timer.cancel();
        }
    }

    private void getAllChatMessage() {
        if (Utility.isOnline(ChatActivity.this)) {
            chatlistProgress = new ASTProgressBar(ChatActivity.this);
            //chatlistProgress.show();
            JSONObject object = new JSONObject();
            try {
                object.put("api_key", Contants.API_KEY);
                object.put("from_user_id", userId);
                object.put("to_user_id", ChatUserId);
            } catch (JSONException e) {
                e.printStackTrace();
            }

            String serviceURL = Contants.BASE_URL + Contants.AllMessages;

            ServiceCaller serviceCaller = new ServiceCaller(ChatActivity.this);
            serviceCaller.CallCommanServiceMethod(serviceURL, object, "getAllChatMessage", new IAsyncWorkCompletedCallback() {
                @Override
                public void onDone(String result, boolean isComplete) {
                    if (isComplete) {
                        parseMessageData(result);
                    } else {
                        if (chatlistProgress.isShowing()) {
                            chatlistProgress.dismiss();
                        }
                        Utility.alertForErrorMessage(Contants.Error, ChatActivity.this);
                    }
                }
            });
        } else {
            Utility.alertForErrorMessage(Contants.OFFLINE_MESSAGE, ChatActivity.this);//off line msg....
        }
    }

    public void parseMessageData(String result) {
        if (result != null) {
            final ChatServiceContentData serviceData = new Gson().fromJson(result, ChatServiceContentData.class);
            if (serviceData != null) {
                if (serviceData.isSuccess()) {
                    if (serviceData.getData() != null) {
                        new AsyncTask<Void, Void, Boolean>() {
                            @Override
                            protected Boolean doInBackground(Void... voids) {
                                Boolean flag = false;
                                megList.clear();
                                for (Data data : serviceData.getData()) {
                                    megList.add(data);
                                }
                                flag = true;
                                return flag;
                            }

                            @Override
                            protected void onPostExecute(Boolean flag) {
                                super.onPostExecute(flag);
                                if (flag) {
                                    Collections.reverse(megList);
                                    adapter.notifyDataSetChanged();
                                }
                                if (chatlistProgress.isShowing()) {
                                    chatlistProgress.dismiss();
                                }
                            }
                        }.execute();
                    } else {
                        if (chatlistProgress.isShowing()) {
                            chatlistProgress.dismiss();
                        }
                    }
                } else {
                    if (chatlistProgress.isShowing()) {
                        chatlistProgress.dismiss();
                    }
                }
            }
        }
    }

    private void sendMessage(String commentStr) {
        if (Utility.isOnline(ChatActivity.this)) {
            chatlistProgress = new ASTProgressBar(ChatActivity.this);
            //   chatlistProgress.show();
            JSONObject object = new JSONObject();
            try {
                object.put("api_key", Contants.API_KEY);
                object.put("from_user_id", userId);
                object.put("to_user_id", ChatUserId);
                object.put("message", commentStr);
            } catch (JSONException e) {
                e.printStackTrace();
            }

            String serviceURL = Contants.BASE_URL + Contants.insertMessages;

            ServiceCaller serviceCaller = new ServiceCaller(ChatActivity.this);
            serviceCaller.CallCommanServiceMethod(serviceURL, object, "sendMessage", new IAsyncWorkCompletedCallback() {
                @Override
                public void onDone(String result, boolean isComplete) {
                    if (isComplete) {
                        parseInsertMessageData(result);
                    } else {
                        if (chatlistProgress.isShowing()) {
                            chatlistProgress.dismiss();
                        }
                        Utility.alertForErrorMessage(Contants.Error, ChatActivity.this);
                    }
                }
            });
        } else {
            Utility.alertForErrorMessage(Contants.OFFLINE_MESSAGE, ChatActivity.this);//off line msg....
        }
    }

    public void parseInsertMessageData(String result) {
        if (result != null) {
            final ChatServiceContentData serviceData = new Gson().fromJson(result, ChatServiceContentData.class);
            if (serviceData != null) {
                if (serviceData.isSuccess()) {
                    et_comment.setText("");
                } else {
                    Toast.makeText(ChatActivity.this, "Message not Send!", Toast.LENGTH_LONG).show();
                }
            }
        }
        if (chatlistProgress.isShowing()) {
            chatlistProgress.dismiss();
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.add_comment:
                String commentStr = et_comment.getText().toString();
                if (commentStr.length() > 0) {
                    sendMessage(commentStr);
                } else {
                    Toast.makeText(ChatActivity.this, "Please enter message!", Toast.LENGTH_LONG).show();
                }
                break;
            case R.id.switchbt:
                Intent intent = new Intent(ChatActivity.this, ConfirmDetailActivity.class);
                intent.putExtra("ChatUserId", ChatUserId);
                intent.putExtra("ProfileImg", ProfileImg);
                intent.putExtra("FullName", FullName);
                intent.putExtra("HomeId", HomeId);
                startActivity(intent);
                break;
        }
    }
}