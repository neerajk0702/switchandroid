package com.kredivation.switchland.firebase;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;
import com.kredivation.switchland.activity.DashboardActivity;

import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

/**
 * <h4>Created</h4> 07/14/16
 *
 * @author Altametrics Inc.
 */
public class FNFirebaseMessagingService extends FirebaseMessagingService {
	String appNameWithoutSpace;
	String appName;
    private NotificationManager notificationManager;
	int REQ_PAGE_COMMUNICATOR = 101;
	@Override
	public void onMessageReceived(RemoteMessage remoteMessage) {
		super.onMessageReceived(remoteMessage);
		//onMessageReceived(getApplicationContext(), remoteMessage.getData());
	}

	/*@SuppressWarnings({ "unchecked" })
	public void onMessageReceived(final Context context, final Map<String, String> dataMap) {
		BNELoginInfo loginInfo = FNApplicationHelper.application().savedLoginInfo();
		String loggedInUserName = loginInfo != null ? loginInfo.readableEmailId() : null;
		String userId = dataMap != null ? dataMap.get("userID") : null;
		if (FNObjectUtil.isEmptyStr(loggedInUserName) || FNObjectUtil.isEmptyStr(userId) || !userId.equalsIgnoreCase(loggedInUserName)) {
			return;
		}
		new AsyncTask<String, Void, Notification>() {
			@Override
			protected Notification doInBackground(String... params) {
				String appName = appName(context);
				String notificationMessage = dataMap.get("notificationMessage");
				Uri alarmSound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
				String customerID = dataMap.containsKey("customerID") ? dataMap.get("customerID") : "0";
				String siteID = dataMap.containsKey("siteID") ? dataMap.get("siteID") : "0";
				int requestCode = new Random().nextInt(1001);
				SharedPreferences notifSharedPref = context.getSharedPreferences(FNConstants.notifSharedPrefName, Context.MODE_PRIVATE);
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
                        mChannel = new NotificationChannel(id, appName, NotificationManager.IMPORTANCE_DEFAULT);
                        notificationManager.createNotificationChannel(mChannel);
                    }
                }
               	NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(context, id);
				mBuilder.setContentTitle(appName);
				mBuilder.setContentText(map.size() + " " + FNStringUtil.getStringForID(R.string.messageReceived));
				mBuilder.setTicker(appName + " " + FNStringUtil.getStringForID(R.string.newMessageReceived));
				mBuilder.setSmallIcon(R.mipmap.notif_icon);
				mBuilder.setColor(FNUIUtil.getColor(context, R.color.notifIconColor));
                mBuilder.setPriority(Notification.PRIORITY_MAX);
				NotificationCompat.InboxStyle inboxStyle = new NotificationCompat.InboxStyle();
				// Sets a title for the Inbox style big view
				inboxStyle.setBigContentTitle(appName);
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
				PendingIntent pendingIntent = PendingIntent.getActivity(context, requestCode, getIntent(context, Long.parseLong(siteID), Long.parseLong(customerID), dataMap.get("redirectUrl")),
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
				NotificationManagerCompat notificationManagernew = NotificationManagerCompat.from(context);
				notificationManagernew.notify(REQ_PAGE_COMMUNICATOR, summaryNotification);
			}

		}.execute("");
	}*/

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
}
