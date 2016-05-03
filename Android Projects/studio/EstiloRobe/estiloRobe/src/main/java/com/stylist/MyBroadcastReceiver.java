package com.stylist;

import com.constants.Constants;
import com.constants.Utils;
import com.parse.ParseUser;
import com.stylist.askstylist.SyncAlertActivity;
import com.stylist.planner.PlannerDetailItem;

import android.annotation.SuppressLint;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.support.v4.app.NotificationCompat;

@SuppressLint("NewApi")
public class MyBroadcastReceiver extends BroadcastReceiver {
	MediaPlayer mp;

	public static int NOTIFICATION_ID = 1;
	String ocassion, comments, preferredStyle, notComfort,time;

	@Override
	public void onReceive(Context context, Intent intent) {
		WakeLocker.acquire(context);
		NotificationManager nm = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
		Boolean fromService = intent.getBooleanExtra("fromService", false);
		Boolean fromQuery = intent.getBooleanExtra("fromQuery", false);
		Boolean fromHome = intent.getBooleanExtra("fromHome", false);
		Boolean isSynced = intent.getBooleanExtra("isSynced", false);
		Utils.write("ask stylist  fromQuery  MyBroadcastReceiver==" + fromQuery);

		Boolean fromPlanner = intent.getBooleanExtra("fromPlanner", false);


		if (fromService) {

			String message = intent.getStringExtra("alarmMessage");
			System.out.println("mybroadcast fromService================" + fromService);

			NotificationManager mNotificationManager = (NotificationManager) context
					.getSystemService(Context.NOTIFICATION_SERVICE);

			Intent notificationIntent = null;
			if (Constants.isHomeOpened) {

				Utils.write("mybroadcast check entry isHomeOpened");
				Intent inten = new Intent(context, SyncAlertActivity.class);
				inten.putExtra("fromServiceHomeOpened", true);
				inten.putExtra("isSynced", isSynced);
				inten.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
				context.startActivity(inten);

			} else {

				Utils.write("mybroadcast check entry notHomeOpened2");
				if (ParseUser.getCurrentUser() != null)
					notificationIntent = new Intent(context, HomeActivity.class);
				else
					notificationIntent = new Intent(context, LoginActivity.class);
				notificationIntent.putExtra("notification", true);

				PendingIntent contentIntent = PendingIntent.getActivity(context, NOTIFICATION_ID, notificationIntent,
						PendingIntent.FLAG_CANCEL_CURRENT);

				NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(context)
						.setSmallIcon(R.drawable.app_icon).setContentTitle("EstiloRobe").setContentText(message);

				mBuilder.setVibrate(new long[] { 1000, 1000, 1000, 1000, 1000 });

				// Set Vibrate, Sound and Light
				int defaults = 0;
				defaults = defaults | Notification.DEFAULT_LIGHTS;

				mBuilder.setDefaults(defaults);

				// Cancel the notification after its selection
				mBuilder.setAutoCancel(true);

				mBuilder.setContentIntent(contentIntent);
				mNotificationManager.notify(NOTIFICATION_ID, mBuilder.build());
			}
		} else if (fromQuery) {
			Utils.write("ask stylist else if fromQuery  MyBroadcastReceiver==" + fromQuery);
			Intent inten = new Intent(context, SyncAlertActivity.class);
			inten.putExtra("isSynced", isSynced);
			inten.putExtra("fromServiceHomeOpened", false);
			Utils.write("mybroadcast fromQuery");
			inten.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
			context.startActivity(inten);


		}else if (fromPlanner) {

			CharSequence from = "EstiloRobe";
			CharSequence message = intent.getStringExtra("alarm_message");
			CharSequence plannerId = intent.getStringExtra("plannerId");
			CharSequence lookId = intent.getStringExtra("lookId");
			CharSequence date = intent.getStringExtra("date");
			CharSequence time = intent.getStringExtra("time");
			NotificationManager mNotificationManager = (NotificationManager) context
					.getSystemService(Context.NOTIFICATION_SERVICE);
			System.out.println("mybroadcast fromPlanner============"+plannerId);Bundle planBundle=new Bundle();
			Intent notificationIntent = null;
			notificationIntent = new Intent(context, PlannerDetailItem.class);

			planBundle.putString("PlannerId", intent.getStringExtra("plannerId"));
			planBundle.putString("LookId", intent.getStringExtra("lookId"));
			planBundle.putString("eventTime", intent.getStringExtra("time"));
			planBundle.putBoolean("notification", true);


			Utils.write(">>>>>>  " + planBundle);

			notificationIntent.putExtra("android.intent.extra.INTENT", planBundle);



			PendingIntent contentIntent = PendingIntent.getActivity(context, 5, notificationIntent, PendingIntent.FLAG_CANCEL_CURRENT);

			NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(context)
					.setSmallIcon(R.drawable.app_icon).setContentTitle("EstiloRobe")
					.setContentText("you have planned a look for wearing today");

			mBuilder.setVibrate(new long[] { 1000, 1000, 1000, 1000, 1000 });

			// Set Vibrate, Sound and Light
			int defaults = 0;
			defaults = defaults | Notification.DEFAULT_LIGHTS;
			// defaults = defaults | Notification.DEFAULT_VIBRATE;
			// defaults = defaults | Notification.DEFAULT_SOUND;
			mBuilder.setDefaults(defaults);

			// Cancel the notification after its selection
			mBuilder.setAutoCancel(true);

			mBuilder.setContentIntent(contentIntent);
			mNotificationManager.notify(NOTIFICATION_ID, mBuilder.build());

		}
	}

}
