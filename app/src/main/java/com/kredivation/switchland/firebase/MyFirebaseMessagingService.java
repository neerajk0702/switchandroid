package com.kredivation.switchland.firebase;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;
import android.util.Log;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;
import com.kredivation.switchland.R;
import com.kredivation.switchland.activity.DashboardActivity;
import com.kredivation.switchland.activity.MainActivity;
import com.kredivation.switchland.database.SwitchDBHelper;
import com.kredivation.switchland.framework.IAsyncWorkCompletedCallback;
import com.kredivation.switchland.framework.ServiceCaller;
import com.kredivation.switchland.model.Data;
import com.kredivation.switchland.utilities.ASTProgressBar;
import com.kredivation.switchland.utilities.Contants;
import com.kredivation.switchland.utilities.Utility;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Map;
import java.util.Random;

public class MyFirebaseMessagingService extends FirebaseMessagingService {

    private static final String TAG = "MyFirebaseMsgService";
    private NotificationManager notificationManager;
    int REQ_PAGE_COMMUNICATOR = 101;

    /**
     * Called when message is received.
     *
     * @param remoteMessage Object representing the message received from Firebase Cloud Messaging.
     */
    // [START receive_message]
    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        // [START_EXCLUDE]
        // There are two types of messages data messages and notification messages. Data messages
        // are handled
        // here in onMessageReceived whether the app is in the foreground or background. Data
        // messages are the type
        // traditionally used with GCM. Notification messages are only received here in
        // onMessageReceived when the app
        // is in the foreground. When the app is in the background an automatically generated
        // notification is displayed.
        // When the user taps on the notification they are returned to the app. Messages
        // containing both notification
        // and data payloads are treated as notification messages. The Firebase console always
        // sends notification
        // messages. For more see: https://firebase.google.com/docs/cloud-messaging/concept-options
        // [END_EXCLUDE]

        // TODO(developer): Handle FCM messages here.
        // Not getting messages here? See why this may be: https://goo.gl/39bRNJ
        Log.d(TAG, "From: " + remoteMessage.getFrom());

        // Check if message contains a data payload.
        if (remoteMessage.getData().size() > 0) {
            Log.d(TAG, "Message data payload: " + remoteMessage.getData());
            sendNotification(remoteMessage.getData());
           //   showNotification(remoteMessage.getData());

        }

        // Check if message contains a notification payload.
        if (remoteMessage.getNotification() != null) {
            Log.d(TAG, "Message Notification Body: " + remoteMessage.getNotification().getBody());
            //sendNotification(remoteMessage.getNotification().getBody());
        }

        // Also if you intend on generating your own notifications as a result of a received FCM
        // message, here is where that should be initiated. See sendNotification method below.
    }

    /**
     * Called if InstanceID token is updated. This may occur if the security of
     * the previous token had been compromised. Note that this is called when the InstanceID token
     * is initially generated so this is where you would retrieve the token.
     */
    @Override
    public void onNewToken(String token) {
        Log.d(TAG, "Refreshed token: " + token);

        // If you want to send messages to this application instance or
        // manage this apps subscriptions on the server side, send the
        // Instance ID token to your app server.
        sendRegistrationToServer(token);
    }
    // [END on_new_token]


    /**
     * Persist token to third-party servers.
     * <p>
     * Modify this method to associate the user's FCM InstanceID token with any server-side account
     * maintained by your application.
     *
     * @param token The new token.
     */
    private void sendRegistrationToServer(String token) {
        Utility.setDeviceIDIntoSharedPreferences(MyFirebaseMessagingService.this, token, true);
    }

    /**
     * Create and show a simple notification containing the received FCM message.
     */
    private void sendNotification(final Map<String, String> dataMap) {
        int requestCode = new Random().nextInt(1001);
        new AsyncTask<String, Void, Notification>() {
            @Override
            protected Notification doInBackground(String... params) {
                String notificationMessage = "";
                String data = dataMap.get("data");
                try {
                    JSONObject object = new JSONObject(data);
                    notificationMessage = object.opt("message").toString();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                Uri alarmSound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
               /* Map.Entry<String,String> entry = dataMap.entrySet().iterator().next();
                String key= entry.getKey();
                String notificationMessage=entry.getValue();*/
                //String notificationMessage = dataMap.containsKey("message") ? dataMap.get("message") : "";
                //  String siteID = dataMap.containsKey("siteID") ? dataMap.get("siteID") : "0";

                SharedPreferences notifSharedPref = getSharedPreferences("NotificationPre", Context.MODE_PRIVATE);
                notifSharedPref.edit().putString("key" + notifSharedPref.getAll().size(), notificationMessage).apply();
                Map<String, String> map = (Map<String, String>) notifSharedPref.getAll();
                // Invoking the default notification service
                String id = getApplication().getPackageName();
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    if (notificationManager == null) {
                        notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
                    }
                    NotificationChannel mChannel = notificationManager.getNotificationChannel(id);
                    if (mChannel == null) {
                        mChannel = new NotificationChannel(id, getString(R.string.app_name), NotificationManager.IMPORTANCE_DEFAULT);
                        notificationManager.createNotificationChannel(mChannel);
                    }
                }
                NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(MyFirebaseMessagingService.this, id);
                mBuilder.setContentTitle(getString(R.string.app_name));
                mBuilder.setContentText(map.size() + " " + "Message Received");
                mBuilder.setTicker(getString(R.string.app_name) + " " + "New Message Received");
                mBuilder.setSmallIcon(R.drawable.logo);
                mBuilder.setColor(Color.parseColor("#FF6600"));
                mBuilder.setWhen(System.currentTimeMillis());
                mBuilder.setChannelId(id);
                mBuilder.setPriority(Notification.PRIORITY_MAX);
                NotificationCompat.InboxStyle inboxStyle = new NotificationCompat.InboxStyle();
                // Sets a title for the Inbox style big view
                inboxStyle.setBigContentTitle(getString(R.string.app_name));
                for (String s : map.keySet()) {
                    inboxStyle.addLine(map.get(s));
                }
                if (alarmSound != null) {
                    mBuilder.setSound(alarmSound);
                }
                mBuilder.setStyle(inboxStyle);
                // When the user presses the notification, it is auto-removed
                mBuilder.setAutoCancel(true);
                // Creates an implicit intent
               /* PendingIntent pendingIntent = PendingIntent.getActivity(MyFirebaseMessagingService.this, requestCode, getIntent(MyFirebaseMessagingService.this, Long.parseLong(siteID), Long.parseLong(customerID), dataMap.get("redirectUrl")),
                        PendingIntent.FLAG_UPDATE_CURRENT);*/
                PendingIntent pendingIntent = PendingIntent.getActivity(MyFirebaseMessagingService.this, requestCode, getIntent(MyFirebaseMessagingService.this, Long.parseLong("1"), Long.parseLong("1"), ""),
                        PendingIntent.FLAG_UPDATE_CURRENT);
                mBuilder.setContentIntent(pendingIntent);
                return mBuilder.build();
            }

            @Override
            protected void onPostExecute(Notification summaryNotification) {
                super.onPostExecute(summaryNotification);
                if (summaryNotification == null) {
                    return;
                }
                //cancelAllNotifications();
                NotificationManagerCompat notificationManagernew = NotificationManagerCompat.from(MyFirebaseMessagingService.this);
                notificationManagernew.notify(requestCode, summaryNotification);
            }

        }.execute("");
    }

    /**
     * it will create an intent and bind all value got from GCM Server and also
     * set the page index value in which we have to redirect on page
     */
    public Intent getIntent(Context context, long siteID, long customerID, String redirectUrl) {
        Bundle bundle = new Bundle();
        bundle.putLong("siteID", siteID);
        bundle.putLong("customerID", customerID);

        Intent intent = new Intent(context, DashboardActivity.class);
        intent.putExtra("bindValue", bundle);
        return intent;
    }

    //clear all notification
    public void cancelAllNotifications() {
        SharedPreferences notifSharedPref = getSharedPreferences("NotificationPre", Context.MODE_PRIVATE);
        notifSharedPref.edit().clear().apply();
        NotificationManager notificationManager = (NotificationManager) this.getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.cancelAll();
    }

    void showNotification(final Map<String, String> dataMap) {
        String notificationMessage = "";
        String data = dataMap.get("data");
        try {
            JSONObject object = new JSONObject(data);
            notificationMessage = object.opt("message").toString();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        int requestCode = new Random().nextInt(1001);
        String id = getApplication().getPackageName();
        Uri alarmSound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        //   mChannel = new NotificationChannel(id, getString(R.string.app_name), NotificationManager.IMPORTANCE_DEFAULT);
        NotificationManager mNotificationManager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(id, "Switch", NotificationManager.IMPORTANCE_DEFAULT);
            channel.setDescription("Switch cha");
            mNotificationManager.createNotificationChannel(channel);
        }
        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(getApplicationContext(), id)
                .setSmallIcon(R.mipmap.ic_launcher) // notification icon
                .setContentTitle("Switch") // title for notification
                .setContentText(notificationMessage)// message for notification
                .setWhen(System.currentTimeMillis())
                .setSound(alarmSound) // set alarm sound for notification
                .setChannelId(id)
                .setAutoCancel(true); // clear notification after click
        Intent intent = new Intent(getApplicationContext(), DashboardActivity.class);
        PendingIntent pi = PendingIntent.getActivity(this, requestCode, intent, PendingIntent.FLAG_UPDATE_CURRENT);
        mBuilder.setContentIntent(pi);
        mNotificationManager.notify(requestCode, mBuilder.build());
    }
}
