package com.stylist;

import java.io.ByteArrayOutputStream;

import java.io.File;
import java.nio.charset.Charset;
import java.util.ArrayList;

import java.util.HashMap;
import java.util.Map;


import org.apache.http.client.methods.HttpPost;
import org.json.JSONArray;
import org.json.JSONObject;

import com.constants.AsyncTaskAppWithoutArray;
import com.constants.AsyncTaskAppWithoutArray.AsynctaskListnerwithoutArray;

import com.constants.Constants;

import com.constants.LocalDatabaseHandler;
import com.constants.Utils;

import com.parse.GetCallback;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.parse.SaveCallback;
import com.parse.entity.mime.HttpMultipartMode;
import com.parse.entity.mime.MultipartEntity;
import com.parse.entity.mime.content.FileBody;
import com.parse.entity.mime.content.StringBody;
import com.stylist.rssfeed.SimpleHTTPConnection;


import android.app.AlarmManager;

import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import android.os.AsyncTask;
import android.os.Bundle;

import android.os.IBinder;

import android.util.Log;

import android.widget.Toast;

public class BackServiceForCloset extends Service implements AsynctaskListnerwithoutArray {

	public static final int KEY_OPEN_DIALOG = 1;
	  ArrayList<String> pathNameList=new ArrayList<String>();
	
	Context context;
	private static BackServiceForCloset backserviceForCloset;
	String UserId;
	Runnable runnable;
	boolean setNewCaegory = false, updateCategory = false, DeleteCategory = false;
	boolean setNewWardrobe = false, updateWardrobe = false, DeleteWardrobe = false,setprofile = false;
	int fromAskStylist = 0;
	Boolean isSynced = true;
	LocalDatabaseHandler dbHelper;

String newImgPath,unique_image_id;
	//handler


	@Override
	public IBinder onBind(Intent arg0) {
		return null;
	}

	public static BackServiceForCloset getInstance() {
		return backserviceForCloset;
	}

	@Override
	public void onCreate() {
		super.onCreate();
		backserviceForCloset = this;
		System.out.println("============in =BackServiceForCloset==============");
		context = this;
		// UserId = ParseUser.getCurrentUser().getObjectId();
		dbHelper = new LocalDatabaseHandler(context);
	}


	public String getPrefrence(String key) {
		SharedPreferences prefrence = context.getSharedPreferences("Stylist", 0);
		String data = prefrence.getString(key, "");
		return data;
	}

	public Boolean getPrefrenceBoolean(String key) {
		SharedPreferences prefrence = context.getSharedPreferences("Stylist", 0);
		Boolean data = prefrence.getBoolean(key, false);
		return data;
	}

	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		// TODO Auto-generated method stub
		setPrefrenceBool(Constants.isSyncing, true);
		Toast.makeText(context, "Syncing is started. we will notify you on completion.", Toast.LENGTH_LONG).show();
		UserId = ParseUser.getCurrentUser().getObjectId();// intent.getStringExtra("userId");
		CheckForNetwork();
		fromAskStylist = intent.getIntExtra("fromAskStylist", 0);
		Utils.write("ask stylist  fromAskStylist"+fromAskStylist);
		//.onStartSession(this, getResources().getString(R.string.flurryKey));
		return START_STICKY;
	}


	
	private void CheckForNetwork() {
		// TODO Auto-generated method stub

		if (checkNetworkConnection()) {
			System.out.println("call to handler");
			methodCall();
		}
	}

	private void methodCall() {
	
			
		if (!checkNetworkConnection()) {
			isSynced = false;
			stopBackService();
		} else {
			System.out.println("BackServiceForCloset   methodCall   ");
			saveUserProfile();

			if (!setNewCaegory)
				saveNewCategory();
		}
	}

	private void saveNewCategory() {
		if (!checkNetworkConnection()) {
			isSynced = false;
			stopBackService();
		} else {
			System.out.println("call to saveNewCategory");
			try {
				setNewCaegory = true;
				JSONArray jsonArray = new JSONArray();
				final Cursor c = dbHelper.returnNewCategory(UserId);
				if (c.moveToFirst()) {
					do {
						String LocalDatabaseID = UserId
								+ c.getString(c.getColumnIndex(LocalDatabaseHandler.CATEGORY_ID));
						String categoryName = c.getString(c.getColumnIndex(LocalDatabaseHandler.CUSTOM_CATEGORY_NAME));

						JSONObject jobj = new JSONObject();
						jobj.put("categoryName", categoryName);
						jobj.put("LocalDatabaseID", LocalDatabaseID);
						jsonArray.put(jobj);

					} while (c.moveToNext());
					Map<String, Object> params = new HashMap<String, Object>();
					params.put("UserId", UserId);
					params.put("array", jsonArray);

					new AsyncTaskAppWithoutArray(false, params, BackServiceForCloset.this, context,
							"insertCategoryList");
				} else {
					saveNewWardrobe();
				}

			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	@Override
	public void AsynctaskResultwithoutArray(String arr, String url, Map<String, Object> par) {
		if (url.equals("insertCategoryList")) {
			updateCategoryTable();
		}

		if (url.equals("deleteCategoryList")) {
			DeleteCategoryTable();
		}
		if (url.equals("insertWardrobeList")) {
			updateWardrobeTable();
		}
		if (url.equals("deleteWardrobeList")) {
			deleteWardrobe();
		}

	}

	private void updateCategoryTable() {
		if (!checkNetworkConnection()) {
			isSynced = false;
			stopBackService();
		} else {

			final Cursor c = dbHelper.returnNewCategory(ParseUser.getCurrentUser().getObjectId());
			if (c.moveToFirst()) {

				do {
					dbHelper.setNewCategorySavedOnServer(
							c.getString(c.getColumnIndex(LocalDatabaseHandler.CATEGORY_ID)));

				} while (c.moveToNext());
			}
			saveNewWardrobe();
		}
	}

	private void saveNewWardrobe() {
		pathNameList.clear();

		if (!checkNetworkConnection()) {
			isSynced = false;
			stopBackService();
		} else {
			// System.out.println("call to saveNewWardrobe");
			try {
				setNewWardrobe = true;
				final Cursor c = dbHelper.returnNewWardrobe(UserId);
				System.out.println("call to saveNewWardrobe " + c.getCount());
				if (c.moveToFirst()) {
					ArrayList<Object> wardrobeTemp;
					do {
						wardrobeTemp = new ArrayList<Object>();
						String LocalDatabaseID = UserId
								+ c.getString(c.getColumnIndex(LocalDatabaseHandler.CATEGORY_ID));
						JSONObject jobj = new JSONObject();
						wardrobeTemp.add(c.getString(c.getColumnIndex(LocalDatabaseHandler.WARDROBE_NAME)));
						wardrobeTemp.add(c.getString(c.getColumnIndex(LocalDatabaseHandler.WARDROBE_COLOR)));
						if (c.getString(c.getColumnIndex(LocalDatabaseHandler.IS_FAVOURITE)).equals("true"))
							wardrobeTemp.add(true);
						else
							wardrobeTemp.add(false);
						ByteArrayOutputStream stream = new ByteArrayOutputStream();
						BitmapFactory.decodeFile(c.getString(c.getColumnIndex(LocalDatabaseHandler.IMAGE_PATH)))
								.compress(Bitmap.CompressFormat.PNG, 100, stream);
						wardrobeTemp.add(stream.toByteArray());
						wardrobeTemp.add(c.getString(c.getColumnIndex(LocalDatabaseHandler.REMARKS)));
						wardrobeTemp.add(c.getString(c.getColumnIndex(LocalDatabaseHandler.DRESS_CODE)));
						wardrobeTemp.add(LocalDatabaseID);
						wardrobeTemp
								.add(LocalDatabaseID + c.getString(c.getColumnIndex(LocalDatabaseHandler.WARDROBE_ID)));
						imgPath = c.getString(c.getColumnIndex(LocalDatabaseHandler.IMAGE_PATH));
						wardrobe.add(wardrobeTemp);
	                      pathNameList.add(c.getString(c.getColumnIndex(LocalDatabaseHandler.UNIQUE_IMAGE_ID)));

						unique_image_id = c.getString(c.getColumnIndex(LocalDatabaseHandler.UNIQUE_IMAGE_ID));

					} while (c.moveToNext());

					insertWardrobe();
				} else {
					updateCategoryOnServer();
				}

			} catch (Exception e) {
				e.printStackTrace();
			}
		}

	}

	private void updateCategoryOnServer() {
		if (!checkNetworkConnection()) {
			isSynced = false;
			stopBackService();
		} else {
			// System.out.println("call to updateCategoryOnServer");
			try {
				updateCategory = true;
				JSONArray jsonArray = new JSONArray();
				final Cursor c = dbHelper.returnUpdatedCategory(UserId);

				if (c.moveToFirst()) {

					do {
						String LocalDatabaseID = UserId
								+ c.getString(c.getColumnIndex(LocalDatabaseHandler.CATEGORY_ID));
						JSONObject jobj = new JSONObject();
						jobj.put("categoryName",
								c.getString(c.getColumnIndex(LocalDatabaseHandler.CUSTOM_CATEGORY_NAME)));
						jobj.put("LocalDatabaseID", LocalDatabaseID);
						jsonArray.put(jobj);
						dbHelper.setUpdateCategorySavedOnServer(
								c.getString(c.getColumnIndex(LocalDatabaseHandler.CATEGORY_ID)));
					} while (c.moveToNext());
					Map<String, Object> params = new HashMap<String, Object>();
					params.put("UserId", UserId);
					params.put("array", jsonArray);

					new AsyncTaskAppWithoutArray(false, params, BackServiceForCloset.this, context,
							"UpdateCategoryList");
				} else {
					if (!updateWardrobe)
						updateWardrobe();
					// if (!updateWishlist)
					// updateWishlistOnServer();
				}

			} catch (Exception e) {
				e.printStackTrace();
			}
		}

	}

	private void DeleteCategoryOnServer() {
		if (!checkNetworkConnection()) {
			isSynced = false;
			stopBackService();
		} else {
			// System.out.println("call to DeleteCategoryOnServer");
			try {
				JSONArray jsonArray = new JSONArray();
				final Cursor c = dbHelper.returnDeletedCategory(UserId);
				// System.out.println("call to DeleteCategoryOnServer " +
				// c.getCount());
				if (c.moveToFirst()) {

					do {
						String LocalDatabaseID = UserId
								+ c.getString(c.getColumnIndex(LocalDatabaseHandler.CATEGORY_ID));
						JSONObject jobj = new JSONObject();
						jobj.put("categoryName",
								c.getString(c.getColumnIndex(LocalDatabaseHandler.CUSTOM_CATEGORY_NAME)));
						jobj.put("LocalDatabaseID", LocalDatabaseID);
						jsonArray.put(jobj);

					} while (c.moveToNext());
					Map<String, Object> params = new HashMap<String, Object>();
					params.put("UserId", UserId);
					params.put("array", jsonArray);

					new AsyncTaskAppWithoutArray(false, params, BackServiceForCloset.this, context,
							"deleteCategoryList");
				} else {
					DeleteWardrobe();
				}

				DeleteCategory = true;
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	ArrayList<ArrayList<Object>> wardrobe = new ArrayList<ArrayList<Object>>();
	String imgPath, objectId;

	private void insertWardrobe() {
		if (!checkNetworkConnection()) {
			isSynced = false;
			stopBackService();
		} else {
			if (wardrobe.size() > 0) {
				final ArrayList<Object> wardrobeTemp = wardrobe.get(0);

				ParseQuery<ParseObject> query = ParseQuery.getQuery("Category");
				query.whereEqualTo("LocalDatabaseID", (String) wardrobeTemp.get(6));
				query.getFirstInBackground(new GetCallback<ParseObject>() {
					public void done(ParseObject object, ParseException e) {
						if (object == null) {
							Log.d("score", "The getFirst request failed.");
						} else {
							final ParseObject pobj = new ParseObject("WardrobeItem");
							pobj.put("Name", (String) wardrobeTemp.get(0));
							pobj.put("Color", (String) wardrobeTemp.get(1));
							System.out.println(" wardrobeTemp.get(2)    " + wardrobeTemp.get(2));
							if ((boolean) wardrobeTemp.get(2))
								pobj.put("IsFavourite", true);
							else
								pobj.put("IsFavourite", false);

							ParseFile file = new ParseFile("image.png", (byte[]) wardrobeTemp.get(3));
							pobj.put("ImageFile", file);
							pobj.put("IsActive", true);
							pobj.put("IsDeleted", false);
							pobj.put("UserId", ParseUser.getCurrentUser());
							pobj.put("Remark", (String) wardrobeTemp.get(4));
							pobj.put("DressCode", (String) wardrobeTemp.get(5));
							pobj.put("CategoryId", object);
							pobj.put("ImageID", pathNameList.get(0));						
							pobj.put("LocalDatabaseID", (String) wardrobeTemp.get(7));
							pobj.saveInBackground(new SaveCallback() {
								public void done(ParseException e) {
									// Handle success or failure here ...
									objectId = pobj.getObjectId();
									pathNameList.remove(0);
									wardrobe.remove(0);
									insertWardrobe();
								}
							});
						}
					}
				});

			} else
				updateWardrobeTable();
		}
	}

	ArrayList<ArrayList<Object>> wardrobeUpdate = new ArrayList<ArrayList<Object>>();

	private void updateWardrobe() {
		if (!checkNetworkConnection()) {
			isSynced = false;
			stopBackService();
		} else {

			System.out.println("call to updateWardrobe");
			try {
				updateWardrobe = true;
				final Cursor c = dbHelper.returnUpdatedWardrobe(UserId);
				// System.out.println("call to updateWardrobe " + c.getCount());
				if (c.moveToFirst()) {
					ArrayList<Object> wardrobeTemp;
					do {
						wardrobeTemp = new ArrayList<Object>();
						String LocalDatabaseID = UserId
								+ c.getString(c.getColumnIndex(LocalDatabaseHandler.CATEGORY_ID))
								+ c.getString(c.getColumnIndex(LocalDatabaseHandler.WARDROBE_ID));

						if (c.getString(c.getColumnIndex(LocalDatabaseHandler.IS_FAVOURITE)).equals("true"))
							wardrobeTemp.add(true);
						else
							wardrobeTemp.add(false);
						wardrobeTemp.add(LocalDatabaseID);
						wardrobeUpdate.add(wardrobeTemp);

					} while (c.moveToNext());

					updateWardrobeOnServer();
				} else {
					DeleteCategoryOnServer();

				
				}

			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	private void updateWardrobeOnServer() {
		if (!checkNetworkConnection()) {
			isSynced = false;
			stopBackService();
		} else {

			if (wardrobeUpdate.size() > 0) {
				final ArrayList<Object> wardrobeTemp = wardrobeUpdate.get(0);

				ParseQuery<ParseObject> query = ParseQuery.getQuery("WardrobeItem");
				query.whereEqualTo("LocalDatabaseID", (String) wardrobeTemp.get(1));
				query.getFirstInBackground(new GetCallback<ParseObject>() {
					public void done(ParseObject object, ParseException e) {
						if (object == null) {
							Log.d("score", "The getFirst request failed.");
						} else {

							if (((String) wardrobeTemp.get(0)).equals("true"))
								object.put("IsFavourite", true);
							else
								object.put("IsFavourite", false);

							object.saveInBackground(new SaveCallback() {
								public void done(ParseException e) {
									// Handle success or failure here ...
									wardrobeUpdate.remove(0);
									updateWardrobeOnServer();
								}

							});

						}
					}
				});

			} else
				updateWardrobeLocal();
		}
	}

	private void updateWardrobeLocal() {
		if (!checkNetworkConnection()) {
			isSynced = false;
			stopBackService();
		} else {

			try {

				final Cursor c = dbHelper.returnUpdatedWardrobe(UserId);
				if (c.moveToFirst()) {
					do {
						dbHelper.setUpdatewardrobeSavedOnServer(
								c.getString(c.getColumnIndex(LocalDatabaseHandler.WARDROBE_ID)));
					} while (c.moveToNext());

				}
			} catch (Exception e) {
				e.printStackTrace();
			}
			// saveNewQuery();
		}
	}

	private void DeleteWardrobe() {
		if (!checkNetworkConnection()) {
			isSynced = false;
			stopBackService();
		} else {

			System.out.println("call to DeleteWardrobe");
			try {
				JSONArray jsonArray = new JSONArray();
				final Cursor c = dbHelper.returnDeletedWardrobe(UserId);
				if (c.moveToFirst()) {
					do {
						String LocalDatabaseID = UserId
								+ c.getString(c.getColumnIndex(LocalDatabaseHandler.CATEGORY_ID))
								+ c.getString(c.getColumnIndex(LocalDatabaseHandler.WARDROBE_ID));
						JSONObject jobj = new JSONObject();
						jobj.put("LocalDatabaseID", LocalDatabaseID);
						jsonArray.put(jobj);

					} while (c.moveToNext());
					Map<String, Object> params = new HashMap<String, Object>();
					params.put("UserId", UserId);
					params.put("array", jsonArray);
					new AsyncTaskAppWithoutArray(false, params, BackServiceForCloset.this, context,
							"deleteWardrobeList");

				} else {
					DeleteWardrobe = true;


					stopBackService();
				}
				
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	public static Bundle convertmaptobundle(Map<String, Object> webservices_params) {
		Bundle b = new Bundle();

		if (webservices_params != null) {
			for (String key : webservices_params.keySet()) {
				Object value = webservices_params.get(key);
				if (value instanceof Integer) {
					b.putString(key, String.valueOf((Integer) value));
				} else if (value instanceof Long) {
					b.putString(key, String.valueOf((Long) value));
				} else if (value instanceof String) {
					b.putString(key, (String) value);
				}
			}
		}

		return b;
	}

	public boolean checkNetworkConnection() {
		ConnectivityManager connectivityManager = (ConnectivityManager) context
				.getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
		return activeNetworkInfo != null && activeNetworkInfo.isConnected();
	}

	private void updateWardrobeTable() {
		if (!checkNetworkConnection()) {
			isSynced = false;
			stopBackService();
		} else {

			final Cursor c = dbHelper.returnNewWardrobe(UserId);
			if (c.moveToFirst()) {

				do {
					dbHelper.setNewWardrobeSavedOnServer(
							c.getString(c.getColumnIndex(LocalDatabaseHandler.WARDROBE_ID)));

				} while (c.moveToNext());
			}
			updateCategoryOnServer();
		}
	}

	private void DeleteCategoryTable() {
		if (!checkNetworkConnection()) {
			isSynced = false;
			stopBackService();
		} else {

			final Cursor c = dbHelper.returnNewCategory(ParseUser.getCurrentUser().getObjectId());
			if (c.moveToFirst()) {

				do {
					dbHelper.setDeleteCategorySavedOnServer(
							c.getString(c.getColumnIndex(LocalDatabaseHandler.CATEGORY_ID)));

				} while (c.moveToNext());
			}
			DeleteWardrobe();
		}
	}

	private void deleteWardrobe() {
		if (!checkNetworkConnection()) {
			isSynced = false;
			stopBackService();
		} else {
			final Cursor c = dbHelper.returnDeletedWardrobe(UserId);
			if (c.moveToFirst()) {

				do {
					dbHelper.setDeleteWardrobeSavedOnServer(
							c.getString(c.getColumnIndex(LocalDatabaseHandler.WARDROBE_ID)));
				} while (c.moveToNext());
			}
			DeleteWardrobe = true;
			stopBackService();
		}
	}

	private void stopBackService() {
		// dialog.dismiss();

		BackServiceForCloset.this.stopSelf();
	}
	
	
	@Override
	public void onDestroy() {
		// TODO Auto-generated method stub

		super.onDestroy();
		
if(dbHelper != null){
			
			dbHelper.close();
		}
		
		System.out.println("Ask stylist on  destroy");
		System.out.println("Ask stylist fromAskStylist======" + fromAskStylist);

		if (fromAskStylist == 1) {
			System.out.println("on  destroy fromAskStylist=======" + fromAskStylist);
			setAlarmn();
		} else {

			setAlarm();
		}
if(fromAskStylist==1) {

	fromAskStylist=0;
	System.out.println("Constants.isSyncing===========aft" + getPrefrenceBoolean(Constants.isSyncing));
	//.onEndSession(this);
}
		else
{
	setPrefrenceBool(Constants.isUpdated, false);
	setPrefrenceBool(Constants.isSyncing, false);
	System.out.println("Constants.isSyncing===========aft" + getPrefrenceBoolean(Constants.isSyncing));
	//.onEndSession(this);
}
		// handlr.removeCallbacks(runnable);

	stopBackService();

		Log.v("Service", "destory");
	}

	public void setPrefrenceBool(String key, Boolean value) {
		SharedPreferences prefrence = context.getSharedPreferences("Stylist", 0);
		Editor editor = prefrence.edit();
		editor.putBoolean(key, value);
		editor.commit();
	}


	static int RQS_1 = 1;
	private void saveUserProfile() {
		System.out.println("call to saveUserProfile");
		try {
			final Cursor c = dbHelper.getUser(ParseUser.getCurrentUser().getObjectId());
			System.out.println("call to saveUserProfile " + c.getCount());
			if (c.moveToFirst()) {
				ParseUser parseUser = ParseUser.getCurrentUser();
				parseUser.setUsername(c.getString(c.getColumnIndex(LocalDatabaseHandler.USER_NAME)));
				System.out.println("user name==  " + c.getString(c.getColumnIndex(LocalDatabaseHandler.USER_NAME)));
				parseUser.put("Gender", c.getString(c.getColumnIndex(LocalDatabaseHandler.GENDER)));
				parseUser.put("Bust", c.getString(c.getColumnIndex(LocalDatabaseHandler.BUST)));
				parseUser.put("Waist", c.getString(c.getColumnIndex(LocalDatabaseHandler.WAIST)));
				parseUser.put("Hips", c.getString(c.getColumnIndex(LocalDatabaseHandler.HIPS)));
				parseUser.put("Shape", c.getString(c.getColumnIndex(LocalDatabaseHandler.SHAPE)));
				parseUser.put("Height", c.getString(c.getColumnIndex(LocalDatabaseHandler.HEIGHT)));
				parseUser.put("Weight", c.getString(c.getColumnIndex(LocalDatabaseHandler.WEIGHT)));
				parseUser.put("Size", c.getString(c.getColumnIndex(LocalDatabaseHandler.SIZE)));
				parseUser.put("BottomWaistSize", c.getString(c.getColumnIndex(LocalDatabaseHandler.BOTTOM_WAIST_SIZE)));
				parseUser.put("SkinComplexion", c.getString(c.getColumnIndex(LocalDatabaseHandler.SKIN_COMPLEXION)));
				parseUser.put("Characteristics", c.getString(c.getColumnIndex(LocalDatabaseHandler.CHARACTERISTIC)));
				parseUser.put("ColorPrefered", c.getString(c.getColumnIndex(LocalDatabaseHandler.LOVE_COLOR)));
				parseUser.put("BrandPrefered", c.getString(c.getColumnIndex(LocalDatabaseHandler.PREF_BRAND)));
				parseUser.put("StyleIcon", c.getString(c.getColumnIndex(LocalDatabaseHandler.STYLE_ICON)));
				parseUser.put("FashionType", c.getString(c.getColumnIndex(LocalDatabaseHandler.FASHION_TYPE)));
				parseUser.put("Outfits", c.getString(c.getColumnIndex(LocalDatabaseHandler.OUTFITS)));
				parseUser.put("StyleDescription",
						c.getString(c.getColumnIndex(LocalDatabaseHandler.STYLE_DESCRIPTION)));
				parseUser.put("AboutYourself", c.getString(c.getColumnIndex(LocalDatabaseHandler.ABOUT_YOURSELF)));

				System.out.println("imapge path " + c.getString(c.getColumnIndex(LocalDatabaseHandler.PROFILE_PIC)));
				if (c.getString(c.getColumnIndex(LocalDatabaseHandler.PROFILE_PIC)) != null) {
					ByteArrayOutputStream stream = new ByteArrayOutputStream();
					BitmapFactory.decodeFile(c.getString(c.getColumnIndex(LocalDatabaseHandler.PROFILE_PIC)))
							.compress(Bitmap.CompressFormat.PNG, 50, stream);
					ParseFile file = new ParseFile("image.png", stream.toByteArray());
				parseUser.put("ProfilePicture", file);
				}

				parseUser.put("occupation", c.getString(c.getColumnIndex(LocalDatabaseHandler.OCCUPATION)));
				parseUser.saveInBackground();
				newImgPath=c.getString(c.getColumnIndex(LocalDatabaseHandler.PROFILE_PIC));
				new UpdateUserInfo().execute(newImgPath);
				setprofile = true;

			}

			setprofile = true;
		} catch (Exception e) {
			e.printStackTrace();
		}

		System.out.println("setprofile   " + setprofile);
	}
	private void setAlarm() {
		System.out.println(" not fromQuery");
		Intent intent = new Intent(getBaseContext(), MyBroadcastReceiver.class);
		intent.putExtra("fromService", true);
		intent.putExtra("fromQuery", false);
		intent.putExtra("isSynced", isSynced);
		intent.putExtra("fromHome", false);
		intent.putExtra("alarmMessage", "Data is synced successfully!");
		// System.out.println(" not fromQuery");
		PendingIntent pendingIntent = PendingIntent.getBroadcast(getBaseContext(),1, intent,
				PendingIntent.FLAG_UPDATE_CURRENT);
		AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
		alarmManager.set(AlarmManager.RTC_WAKEUP, System.currentTimeMillis() + (5000), pendingIntent);

	}

	private void setAlarmn() {
		Utils.write("ask stylist  fromQuery  setAlarmn" + fromAskStylist);
		Intent intenta = new Intent(getBaseContext(), MyBroadcastReceiver.class);
		intenta.putExtra("fromQuery", true);
		intenta.putExtra("fromHome", false);
		intenta.putExtra("isSynced", isSynced);
		intenta.putExtra("fromService", false);
		intenta.putExtra("alarmMessage", " Query has been sent successfully!");

		PendingIntent pendingIntent = PendingIntent.getBroadcast(getBaseContext(), 2, intenta,
				PendingIntent.FLAG_UPDATE_CURRENT);
		AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
		alarmManager.set(AlarmManager.RTC_WAKEUP, System.currentTimeMillis() + (15000), pendingIntent);
	}









	private class UpdateUserInfo extends AsyncTask<String, Void, String> {

		@Override
		protected String doInBackground(String... params) {
			HttpPost httppost = null;
			try {
				System.out.println("imgPath==============uploadimage========" + newImgPath);
				MultipartEntity mp = new MultipartEntity(HttpMultipartMode.BROWSER_COMPATIBLE);

				// http://mobileappsdev.net/estilorobe/webservices/Index.php/uploadimage

				// http://52.37.36.159/webservices/Index.php/uploadimage
				httppost = new HttpPost("http://54.201.113.167/admin/webservices/Index.php/uploadimage");
				try {
					mp = new MultipartEntity(HttpMultipartMode.BROWSER_COMPATIBLE);
					mp.addPart("unique_image_id", new StringBody(unique_image_id, "text/plain",
							Charset.forName("UTF-8")));
					// mp.addPart("display_name",
					// new StringBody(ParseUser.getCurrentUser().getUsername(),
					// "text/plain", Charset.forName("UTF-8")));
					if (!newImgPath.equals("")) {
						File file=new File(newImgPath);
						double kilobytes = (file.length() / 1024);
						double megabytes = (kilobytes / 1024);
						Log.v("SIZE_IMAGE_FULL", "MB: "+megabytes+", KB: "+kilobytes);
						mp.addPart("file_name", new FileBody(file, "image/jpeg"));
					}
					httppost.setEntity(mp);
				} catch (Exception e) {
					e.printStackTrace();
				}

			} catch (Exception e) {
				e.printStackTrace();
			}
			return SimpleHTTPConnection.sendByPOST(httppost);
		}

		@Override
		protected void onPostExecute(String result) {
			System.out.println("Result Save Profile Detail -->  " + result);
			try {
				JSONObject obj = new JSONObject(result);
				if (obj.optString("status") != null && obj.optString("status").equalsIgnoreCase("Success")) {
					/*
					 * Toast.makeText(UploadWardrobeWithDescriptionPop.this,
					 * "updated sucessfully", Toast.LENGTH_SHORT) .show();
					 */
					//	finish();


				} else {
//					Toast.makeText(UploadWardribeActivity.this, "Error! Try again", Toast.LENGTH_SHORT)
//							.show();

				}
			} catch (Exception e) {
				// TODO: handle exception
			}

		}

		@Override
		protected void onPreExecute() {
			super.onPreExecute();

		}

	}






}
