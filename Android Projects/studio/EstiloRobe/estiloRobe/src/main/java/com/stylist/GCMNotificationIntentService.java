package com.stylist;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import com.constants.Constants;
import com.constants.LocalDatabaseHandler;
import com.constants.Utils;
import com.google.android.gms.gcm.GoogleCloudMessaging;
import com.parse.ParseUser;
import com.stylist.askstylist.StylistResponseDetailActivity;

import android.app.IntentService;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.SystemClock;
import android.provider.MediaStore.Images;
import android.provider.MediaStore.Images.Media;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

public class GCMNotificationIntentService extends IntentService {

	public static int NOTIFICATION_ID = 1;
	private NotificationManager mNotificationManager;
	NotificationCompat.Builder builder;
	Context context;
	Bitmap bitmap;
	LocalDatabaseHandler dbHelper;

	public GCMNotificationIntentService() {
		super("GcmIntentService");

	}
	String url, stylistComment, stylistName, obj_id;
	public static final String TAG = "GCMNotificationIntentService";
	@Override
	protected void onHandleIntent(Intent intent) {
		WakeLocker.acquire(getApplicationContext());
		Bundle extras = intent.getExtras();
		GoogleCloudMessaging gcm = GoogleCloudMessaging.getInstance(this);
		context = this;
		String messageType = gcm.getMessageType(intent);
		if (!extras.isEmpty()) {

			dumpIntent(intent);

			if (GoogleCloudMessaging.MESSAGE_TYPE_SEND_ERROR.equals(messageType)) {
				sendNotification("Send error: " + extras.toString());
			} else if (GoogleCloudMessaging.MESSAGE_TYPE_DELETED.equals(messageType)) {
				sendNotification("Deleted messages on server: " + extras.toString());
			} else if (GoogleCloudMessaging.MESSAGE_TYPE_MESSAGE.equals(messageType)) {
				System.out.println("RESPONSe=======================================" + extras);
				System.out.println("RESPONSE URL===============" + extras.getString("img_url"));

				new LoadImage().execute(extras.getString("img_url"));
				
				stylistComment = extras.getString("Sty_Comment");
				stylistName = extras.getString("Sty_Name");
				obj_id = extras.getString("Query_Id");
				System.out.println("stylistComment=======================================" + stylistComment);
				System.out.println("stylistName=======================================" + stylistName);
				System.out.println("RESPONSe= obj_id======================================" + obj_id);
			//	dbHelper.updateAskStylistResponce(stylistComment, "", obj_id, stylistName);

				Log.i(TAG, "Completed work @ " + SystemClock.elapsedRealtime());
				sendNotification(extras);
				Log.i(TAG, "Received: " + extras.toString());
			}

		}
		GcmBroadcastReceiver.completeWakefulIntent(intent);
		WakeLocker.release();
		NOTIFICATION_ID++;
		Constants.notificationCount = NOTIFICATION_ID;
	}

	public static void dumpIntent(Intent i) {
		Bundle bundle = i.getExtras();
		if (bundle != null) {
			Set<String> keys = bundle.keySet();
			Iterator<String> it = keys.iterator();

			while (it.hasNext()) {
				String key = it.next();
				Log.e("Intent Data", "[" + key + "=" + bundle.get(key) + "]");
			}

		}
	}

	private class LoadImage extends AsyncTask<String, String, Bitmap> {
		@Override
		protected void onPreExecute() {
			super.onPreExecute();

		}

		protected Bitmap doInBackground(String... args) {
			try {
				bitmap = BitmapFactory.decodeStream((InputStream) new URL(args[0]).getContent());
				System.out.println("STYLIST RESPONSE BITMAP=============" + bitmap);
			} catch (Error e) {
				e.printStackTrace();
			} catch (Exception e) {
				e.printStackTrace();
			}
			return bitmap;
		}

		protected void onPostExecute(Bitmap image) {
			System.out.println("STYLIST RESPONSE BITMAP=image============" + image);

			saveOnShare(image);
		}
	}

	private void getStylistResponse(String img_path, Context context) {

		LocalDatabaseHandler dbHelper = new LocalDatabaseHandler(context);
		System.out.println("STY RESimg_path=======" + img_path);
		dbHelper.updateAskStylistResponce(stylistComment, img_path, obj_id, stylistName);

	}

	String imgPath;

	private void sendNotification(Bundle extras) {
		try {
			if (extras != null) {
				PendingIntent resultPendingIntent = null;
				List<String> jasondata_list;
				if (ParseUser.getCurrentUser() != null) {
					Intent resultIntent = new Intent(this, StylistResponseDetailActivity.class);
					resultIntent.putExtra("qureyId", obj_id);
					resultIntent.putExtra("fromNotificationInt", 1);
					resultPendingIntent = PendingIntent.getActivity(this, 0, resultIntent,
							PendingIntent.FLAG_CANCEL_CURRENT);
					if (resultPendingIntent != null) {
						NotificationCompat.Builder mNotifyBuilder = null;
						NotificationManager mNotificationManager;
						mNotificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
						mNotifyBuilder = new NotificationCompat.Builder(this).setContentTitle("EstiloRobe")
								.setContentText("You've gotten a stylist response").setSmallIcon(R.drawable.app_icon);
						// Set pending intent
						mNotifyBuilder.setContentIntent(resultPendingIntent);
						// Set Vibrate, Sound and Light
						int defaults = 0;
						defaults = defaults | Notification.DEFAULT_LIGHTS;
						defaults = defaults | Notification.DEFAULT_VIBRATE;
						defaults = defaults | Notification.DEFAULT_SOUND;
						mNotifyBuilder.setDefaults(defaults);
						// Set the content for Notification
						mNotifyBuilder.setContentText("You've gotten a stylist response");
						// Set autocancel
						mNotifyBuilder.setAutoCancel(true);
						// Post a notification
						mNotificationManager.notify(NOTIFICATION_ID, mNotifyBuilder.build());
					}
				} else {

					/*if(getApplicationContext().isFinishing()){

					}
*/
					Intent resultIntent = new Intent(this, StylistResponseDetailActivity.class);
					resultIntent.putExtra("qureyId", obj_id);
					resultIntent.putExtra("fromNotificationInt", 1);
					resultPendingIntent = PendingIntent.getActivity(this, 0, resultIntent,
							PendingIntent.FLAG_CANCEL_CURRENT);
					if (resultPendingIntent != null) {
						NotificationCompat.Builder mNotifyBuilder = null;
						NotificationManager mNotificationManager;
						mNotificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
						mNotifyBuilder = new NotificationCompat.Builder(this).setContentTitle("EstiloRobe")
								.setContentText("You've gotten a stylist response").setSmallIcon(R.drawable.app_icon);
						// Set pending intent
						mNotifyBuilder.setContentIntent(resultPendingIntent);
						// Set Vibrate, Sound and Light
						int defaults = 0;
						defaults = defaults | Notification.DEFAULT_LIGHTS;
						defaults = defaults | Notification.DEFAULT_VIBRATE;
						defaults = defaults | Notification.DEFAULT_SOUND;
						mNotifyBuilder.setDefaults(defaults);
						// Set the content for Notification
						mNotifyBuilder.setContentText("You've gotten a stylist response");
						// Set autocancel
						mNotifyBuilder.setAutoCancel(true);
						// Post a notification
						mNotificationManager.notify(NOTIFICATION_ID, mNotifyBuilder.build());
					}
				}
			}
		} catch (Error e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	public void saveOnShare(Bitmap sourceBitmap) {
		String imageName;

		if (sourceBitmap != null && !sourceBitmap.isRecycled()) {
			FileOutputStream out = null;
			File imageFile = Utils.getOutputMediaFileLooksByStylist();
			try {
				out = new FileOutputStream(imageFile);
				Boolean imageSaved = sourceBitmap.compress(Bitmap.CompressFormat.PNG, 100, out);
			} catch (Exception e) {

			} finally {
				if (out != null) {
					try {
						out.flush();
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					try {
						out.close();
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			}
			try {
				imageName = "IMG_" + System.currentTimeMillis() + ".png";
				ContentValues values = new ContentValues(3);
				values.put(Images.Media.TITLE, imageName);
				values.put(Images.Media.MIME_TYPE, "image/PNG");
				values.put("_data", imageFile.getAbsolutePath());
				getContentResolver().insert(Media.EXTERNAL_CONTENT_URI, values);
				imgPath = imageFile.getAbsolutePath();
				System.out.println("imgPath=======" + imgPath);
				// getStylistResponse(context);
				getStylistResponse(imgPath, getApplicationContext());

			} catch (Exception e) {
			}
		}

	}

	private void sendNotification(String msg) {	PendingIntent resultPendingIntent = null;
	List<String> jasondata_list;
	if (ParseUser.getCurrentUser() != null) {
		Intent resultIntent = new Intent(this, StylistResponseDetailActivity.class);
		resultIntent.putExtra("qureyId", obj_id);
		resultIntent.putExtra("fromNotificationInt", 1);
		resultPendingIntent = PendingIntent.getActivity(this, 0, resultIntent,
				PendingIntent.FLAG_CANCEL_CURRENT);
		if (resultPendingIntent != null) {
			NotificationCompat.Builder mNotifyBuilder = null;
			NotificationManager mNotificationManager;
			mNotificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
			mNotifyBuilder = new NotificationCompat.Builder(this).setContentTitle("EstiloRobe")
					.setContentText("You've gotten a stylist response").setSmallIcon(R.drawable.app_icon);
			// Set pending intent
			mNotifyBuilder.setContentIntent(resultPendingIntent);
			// Set Vibrate, Sound and Light
			int defaults = 0;
			defaults = defaults | Notification.DEFAULT_LIGHTS;
			defaults = defaults | Notification.DEFAULT_VIBRATE;
			defaults = defaults | Notification.DEFAULT_SOUND;
			mNotifyBuilder.setDefaults(defaults);
			// Set the content for Notification
			mNotifyBuilder.setContentText("You've gotten a stylist response");
			// Set autocancel
			mNotifyBuilder.setAutoCancel(true);
			// Post a notification
			mNotificationManager.notify(NOTIFICATION_ID, mNotifyBuilder.build());
		}
	}}

	@Override
	public void onDestroy() {
		// TODO Auto-generated method stub

		super.onDestroy();
		if (dbHelper != null) {

			dbHelper.close();
		}

	}

}