package com.constants;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.io.Writer;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.MediaScannerConnection;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.ContextThemeWrapper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.constants.AsyncTaskAppWithoutArray.AsynctaskListnerwithoutArray;
import com.google.android.gms.appindexing.Action;
import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.common.api.GoogleApiClient;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.stylist.BackServiceForCloset;
//import com.stylist.CategoryService;
import com.stylist.HomeActivity;
import com.stylist.LoginActivity;
import com.stylist.R;

public class BaseActivity extends FragmentActivity implements AsynctaskListnerwithoutArray, OnClickListener {
	public Activity context = this;
	public AsynctaskListnerwithoutArray callBackListnerwithoutArray;


	Intent service;
	/**
	 * ATTENTION: This was auto-generated to implement the App Indexing API.
	 * See https://g.co/AppIndexing/AndroidStudio for more information.
	 */
	private GoogleApiClient client;

	protected void startSyncservice(Context activityContect, int fromAskStylist) {
		service = new Intent(activityContect, BackServiceForCloset.class);
		service.putExtra("fromAskStylist", fromAskStylist);
		startService(service);
	}

	protected void showToastS(String text) {
		Toast t = Toast.makeText(context, text, Toast.LENGTH_SHORT);

		t.show();
	}

	public Bitmap getPhoto(byte[] image) {
		return BitmapFactory.decodeByteArray(image, 0, image.length);
	}

	public void onClickWhatsApp(String showImagePath) {
		System.out.println("showImagePath   " + showImagePath);
		PackageManager pm = getPackageManager();
		try {
			Intent waIntent = new Intent(Intent.ACTION_SEND);
			waIntent.setType("image/*");
			waIntent.putExtra(Intent.EXTRA_STREAM, Uri.fromFile(new File(showImagePath)));
			PackageInfo info = pm.getPackageInfo("com.whatsapp", PackageManager.GET_META_DATA);
			waIntent.setPackage("com.whatsapp");
			startActivity(Intent.createChooser(waIntent, "Share with"));
		} catch (Error error)
		{
			error.printStackTrace();
		}catch (Exception e) {
			e.printStackTrace();
			Toast.makeText(this, "WhatsApp not Installed", Toast.LENGTH_SHORT).show();
		}
	}

//	@Override
//	protected void onStart() {
//		// TODO Auto-generated method stub
//		super.onStart();
//		// ATTENTION: This was auto-generated to implement the App Indexing API.
//		// See https://g.co/AppIndexing/AndroidStudio for more information.
//		client.connect();
//
//		// ATTENTION: This was auto-generated to implement the App Indexing API.
//		// See https://g.co/AppIndexing/AndroidStudio for more information.
//		Action viewAction = Action.newAction(
//				Action.TYPE_VIEW, // TODO: choose an action type.
//				"Base Page", // TODO: Define a title for the content shown.
//				// TODO: If you have web page content that matches this app activity's content,
//				// make sure this auto-generated web page URL is correct.
//				// Otherwise, set the URL to null.
//				Uri.parse("http://host/path"),
//				// TODO: Make sure this auto-generated app deep link URI is correct.
//				Uri.parse("android-app://com.constants/http/host/path")
//		);
//		AppIndex.AppIndexApi.start(client, viewAction);
//	}
//
//	@Override
//	protected void onStop() {
//		// TODO Auto-generated method stub
//		super.onStop();
//		// ATTENTION: This was auto-generated to implement the App Indexing API.
//		// See https://g.co/AppIndexing/AndroidStudio for more information.
//		Action viewAction = Action.newAction(
//				Action.TYPE_VIEW, // TODO: choose an action type.
//				"Base Page", // TODO: Define a title for the content shown.
//				// TODO: If you have web page content that matches this app activity's content,
//				// make sure this auto-generated web page URL is correct.
//				// Otherwise, set the URL to null.
//				Uri.parse("http://host/path"),
//				// TODO: Make sure this auto-generated app deep link URI is correct.
//				Uri.parse("android-app://com.constants/http/host/path")
//		);
//		AppIndex.AppIndexApi.end(client, viewAction);
//
//		// ATTENTION: This was auto-generated to implement the App Indexing API.
//		// See https://g.co/AppIndexing/AndroidStudio for more information.
//		client.disconnect();
//	}

	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();








	}


	public String formatDateto(int day, int year, int month) {
		String dateString1 = "";

		switch (month) {
			case 1:
				dateString1 = "Jan " + day + ", " + year;
				break;
			case 2:
				dateString1 = "Feb " + day + ", " + year;
				break;
			case 3:
				dateString1 = "Mar " + day + ", " + year;
				break;
			case 4:
				dateString1 = "Apr " + day + ", " + year;
				break;
			case 5:
				dateString1 = "May " + day + ", " + year;
				break;
			case 6:
				dateString1 = "Jun " + day + ", " + year;
				break;
			case 7:
				dateString1 = "Jul " + day + ", " + year;
				break;
			case 8:
				dateString1 = "Aug " + day + ", " + year;

				break;
			case 9:
				dateString1 = "Sep " + day + ", " + year;
				break;
			case 10:
				dateString1 = "Oct " + day + ", " + year;
				break;
			case 11:
				dateString1 = "Nov " + day + ", " + year;
				break;
			case 12:
				dateString1 = "Dec " + day + ", " + year;
				break;

			default:
				break;
		}

		return dateString1;

	}

	public Dialog addDialog(int resId) {

		Dialog d = new Dialog(context);

		d.requestWindowFeature(Window.FEATURE_NO_TITLE);
		d.setCancelable(true);
		d.setContentView(resId);
		d.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
		return d;

	}

	public Dialog addDialog1(Context context, int resId) {

		Dialog d = new Dialog(context);

		d.requestWindowFeature(Window.FEATURE_NO_TITLE);
		d.setCancelable(true);
		d.setContentView(resId);
		d.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
		return d;

	}

	protected ProgressDialog showDialog(String msg) {
		// TODO Auto-generated method stub
		ProgressDialog dialog = new ProgressDialog(context);
		dialog.setMessage(msg);
		dialog.setCancelable(false);
		dialog.show();
		return dialog;
	}

	protected void setHeading(String heading) {
		TextView headerTitle = (TextView) this.findViewById(R.id.headerName);
		headerTitle.setText(heading.toUpperCase());

	}

	protected void backClick() {
		ImageView backBtn = (ImageView) this.findViewById(R.id.backButtonImage);
		backBtn.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				finishActivity();

			}
		});
	}

	protected void setFont(ViewGroup group) {

		int count = group.getChildCount();
		View v;
		for (int i = 0; i < count; i++) {
			v = group.getChildAt(i);
			if (v instanceof TextView || v instanceof EditText || v instanceof Button) {
				// ((TextView) v).setTypeface(tf);
			} else if (v instanceof ViewGroup)
				setFont((ViewGroup) v);
		}
	}

	protected static void checkAndCloseActivity(Activity context) {

		if (context != null && !context.isFinishing()) {
			context.finish();
		}
	}

	protected void showToastL(String text) {
		Toast.makeText(context, text, Toast.LENGTH_LONG).show();
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		Thread.setDefaultUncaughtExceptionHandler(handleAppCrash);

//		if (Utils.checkNetworkConnection(this)) {
//			if(( getPrefrenceBool(Constants.isSyncing) != null) && !(getPrefrenceBool(Constants.isSyncing)))
//			{					
//				setPrefrenceBool(Constants.isSyncing, true);
//				System.out.println("CategoryService home===========");
//				Intent in = new Intent(BaseActivity.this, CategoryService.class);
//				in.putExtra("userId", ParseUser.getCurrentUser().getObjectId());
//				startService(in);			
//			}
//		}
		context = this;
		callBackListnerwithoutArray = BaseActivity.this;
		// ATTENTION: This was auto-generated to implement the App Indexing API.
		// See https://g.co/AppIndexing/AndroidStudio for more information.
		client = new GoogleApiClient.Builder(this).addApi(AppIndex.API).build();
	}

	protected boolean checkNetworkConnection() {
		ConnectivityManager connectivityManager = (ConnectivityManager) context
				.getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
		return activeNetworkInfo != null && activeNetworkInfo.isConnected();
	}

	@Override
	public void onClick(View v) {

	}

	public void goToActivity(Class<?> classActivity, Bundle bundle) {
		Intent intent = new Intent(context, classActivity);
		if (bundle != null)
			intent.putExtra("android.intent.extra.INTENT", bundle);
		context.startActivity(intent);
		context.overridePendingTransition(R.anim.animation_enter_from_right, R.anim.animation_leave_out_to_left);
	}

	protected void gotoActivityWithfinish(Class<?> classActivity, Bundle bundle) {
		Intent intent = new Intent(context, classActivity);
		if (bundle != null)
			intent.putExtra("android.intent.extra.INTENT", bundle);
		Utils.hideSoftKeyboard(context);
		context.startActivity(intent);
		context.finish();
		context.overridePendingTransition(R.anim.animation_enter_from_right, R.anim.animation_leave_out_to_left);
	}

	@Override
	public void onBackPressed() {
		// TODO Auto-generated method stub
		super.onBackPressed();
		context.finish();

		overridePendingTransition(R.anim.exit_animation_enter_from_right, R.anim.exit_animation_leave_to_right);
	}

	protected void gotoactivityonbackpressed(Class<?> classActivity, Bundle bundle) {
		Intent intent = new Intent(context, classActivity);
		if (bundle != null)
			intent.putExtra("android.intent.extra.INTENT", bundle);
		Utils.hideSoftKeyboard(context);
		context.startActivity(intent);
		context.finish();
	}

	protected void finishActivity() {

		Utils.hideSoftKeyboard(context);
		context.finish();
		overridePendingTransition(R.anim.exit_animation_enter_from_right, R.anim.exit_animation_leave_to_right);
	}

	public void setPrefrence(String key, String value) {
		SharedPreferences prefrence = context.getSharedPreferences("Stylist", 0);
		Editor editor = prefrence.edit();
		editor.putString(key, value);
		editor.commit();
	}

	public String getPrefrence(String key) {
		SharedPreferences prefrence = context.getSharedPreferences("Stylist", 0);
		String data = prefrence.getString(key, "");
		return data;
	}

	public Boolean getPrefrenceBool(String key) {
		SharedPreferences prefrence = context.getSharedPreferences("Stylist", 0);
		Boolean data = prefrence.getBoolean(key, false);
		return data;
	}

	public void setPrefrenceBool(String key, Boolean value) {
		SharedPreferences prefrence = context.getSharedPreferences("Stylist", 0);
		Editor editor = prefrence.edit();
		editor.putBoolean(key, value);
		editor.commit();
	}

	public String getPrefrence(String key, String def) {
		SharedPreferences prefrence = context.getSharedPreferences("Stylist", 0);
		String data = prefrence.getString(key, def);
		return data;
	}

	public void clearSharedPreferences() {

		SharedPreferences prefrence = context.getSharedPreferences("Stylist", 0);
		Editor editor = prefrence.edit();
		editor.clear();
		editor.commit();
	}

	protected void ExitAlert() {
		Context c = new ContextThemeWrapper(context, R.style.CustomFontTheme);
		AlertDialog.Builder builder = new AlertDialog.Builder(c);
		builder.setMessage("Are you sure?").setTitle("EXIT")
				.setPositiveButton("OK", new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int id) {
						dialog.cancel();
						context.finish();
					}
				}).setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int id) {
				dialog.cancel();
			}
		});

		AlertDialog alert = builder.create();
		alert.show();

	}

	protected void logout() {
		try {

			Utils.createSimpleDialog(context, context.getString(R.string.app_name), "Are you sure you want to logout?",
					"Ok", "Cancel", new Utils.Method() {
						public void execute() {
							final ProgressDialog dialog = new ProgressDialog(context);
							dialog.setMessage("Logging out...");
							dialog.setCancelable(false);
							dialog.show();
							ParseQuery<ParseObject> query = ParseQuery.getQuery("UserToken");
							query.whereEqualTo("TokenID", getPrefrence(Constants.googledeviceid));
							query.whereEqualTo("UserID", ParseUser.getCurrentUser());
							query.findInBackground(new FindCallback<ParseObject>() {
								public void done(List<ParseObject> scoreList, ParseException e) {
									if (e == null) {
										if (scoreList.size() > 0)
											scoreList.get(0).deleteInBackground();
										ParseUser.logOut();
										clearSharedPreferences();

										setPrefrenceBool("KEY_FB", false);

										dialog.dismiss();
										gotoActivityWithfinish(LoginActivity.class, null);
										context.overridePendingTransition(R.anim.animation_enter_from_right,
												R.anim.animation_leave_out_to_left);
									} else {
										Log.d("score", "Error: " + e.getMessage());
									}
								}
							});
						}
					}).show();
		} catch (Error error)
		{
			error.printStackTrace();
		}catch (Exception e) {
			e.printStackTrace();
		}
	}

	protected void showAddPictureAlert() {

		final CharSequence[] Type = {"Camera", "Photo Gallery", "Cancel"};
		Context c = new ContextThemeWrapper(context, R.style.CustomFontTheme);
		AlertDialog.Builder builder = new AlertDialog.Builder(c);
		builder.setTitle(/* getResources().getString(R.string.add_photo) */"Select Image Source");
		builder.setItems(Type, new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int pos) {
				if (pos == 0) {
					Intent intentPicture = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
					context.startActivityForResult(intentPicture, 100);

				} else if (pos == 1) {

					Intent in = new Intent(Intent.ACTION_PICK,
							MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
					context.startActivityForResult(in, 100);

				}

			}
		});
		builder.show();

	}


	public void syncAlertDialog(Boolean isSynced) {
		System.out.println("sync alert");
		LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		View viewList = inflater.inflate(R.layout.activity_sync_alert, null);
		final Dialog dialogMarketList = new Dialog(this);
		dialogMarketList.requestWindowFeature(Window.FEATURE_NO_TITLE);
		dialogMarketList.setContentView(viewList);

		dialogMarketList.getWindow().setBackgroundDrawableResource(android.R.color.transparent);

		CustomTextView synctext = (CustomTextView) viewList.findViewById(R.id.synctext);
		CustomTextView ok = (CustomTextView) viewList.findViewById(R.id.ok);

		ok.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				dialogMarketList.dismiss();
			}
		});
		if (isSynced)
			synctext.setText("Your query has been sent to the stylist.");
		else
			synctext.setText("Your query was not sent as the data syncing was not successful.");

	}


	public void deleteImage(String path) {
		String file_dj_path = path;
		File fdelete = new File(file_dj_path);
		if (fdelete.exists()) {
			if (fdelete.delete()) {
				Log.e("-->", "file Deleted :" + file_dj_path);
				callBroadCast();
			} else {
				Log.e("-->", "file not Deleted :" + file_dj_path);
			}
		}
		else
		{
			Log.e("-->", "else :" + file_dj_path);

		}
	}

	public void callBroadCast() {
		if (Build.VERSION.SDK_INT >= 14) {
			Log.e("-->", " >= 14");
			MediaScannerConnection.scanFile(this, new String[]{Environment.getExternalStorageDirectory().toString()}, null, new MediaScannerConnection.OnScanCompletedListener() {
				/*
				 *   (non-Javadoc)
                 * @see android.media.MediaScannerConnection.OnScanCompletedListener#onScanCompleted(java.lang.String, android.net.Uri)
                 */
				public void onScanCompleted(String path, Uri uri) {
					Log.e("ExternalStorage", "Scanned " + path + ":");
					Log.e("ExternalStorage", "-> uri=" + uri);
				}
			});
		} else {
			Log.e("-->", " < 14");
			sendBroadcast(new Intent(Intent.ACTION_MEDIA_MOUNTED,
					Uri.parse("file://" + Environment.getExternalStorageDirectory())));
		}
	}



//
//
	void deleteRecursive(File fileOrDirectory) {

		if (fileOrDirectory.isDirectory())
			for (File child : fileOrDirectory.listFiles())
				deleteRecursive(child);
		fileOrDirectory.delete();
	}

	public void deleteFolder() {

		try {
			Utils.write("delete folder called");

			File dir1 = new File(

					//Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES),
					//Constants.IMAGE_DIRECTORY_NAME

					Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES),
					Constants.IMAGE_DIRECTORY_NAME);

			deleteRecursive(dir1);

		} catch (Error e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}

	}
//
//
//
//



	 public void copyFile(File src, File dst) throws IOException {
Utils.write("src======"+src);
		 Utils.write("dst======"+dst);
		InputStream in = new FileInputStream(src);
		OutputStream out = new FileOutputStream(dst);

		byte[] buf = new byte[1024];
		int len;
		while ((len = in.read(buf)) > 0) {
			out.write(buf, 0, len);
		}
		in.close();
		out.close();
	}




	public String getTempImgBgFilename() {

		File file = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES), Constants.IMAGE_DIRECTORY_NAME+"/"+Constants.IMAGE_TEMP_CROPED);
		if (!file.exists()) {
			file.mkdirs();
		}

		String uriSting = (file.getAbsolutePath() + "/" + System.currentTimeMillis() + ".jpg");
		System.out.println("uriSting-----------------" + uriSting);
		return uriSting;

	}


	protected void sendEmail(String message) {
		Log.i("Send email", "");
		String[] TO = {"mukesh.trivialworks@gmail.com"};
		String[] CC = {""};
		Intent emailIntent = new Intent(Intent.ACTION_SEND);

		emailIntent.setData(Uri.parse("mailto:"));
		emailIntent.setType("text/plain");
		emailIntent.putExtra(Intent.EXTRA_EMAIL, TO);
		emailIntent.putExtra(Intent.EXTRA_CC, CC);
		emailIntent.putExtra(Intent.EXTRA_SUBJECT, "Crash Report: ");
		emailIntent.putExtra(Intent.EXTRA_TEXT, message);

		try {
			startActivity(Intent.createChooser(emailIntent, "Send mail..."));
			finish();
			Log.i("Finished...", "");
		} catch (Error error)
		{
			error.printStackTrace();
		}
		catch (ActivityNotFoundException ex) {

			Log.i("Finished...", "ACTIVITY NOT FOUND");

			//Toast.makeText(MainActivity.this, "There is no email client installed.", Toast.LENGTH_SHORT).show();
		}catch (Exception e)
		{
			e.printStackTrace();
		}
	}

	private Thread.UncaughtExceptionHandler handleAppCrash =
			new Thread.UncaughtExceptionHandler() {
				@Override
				public void uncaughtException(Thread thread, Throwable ex) {
					Log.e("error", ex.toString());
					//send email here
					String timestamp = System.currentTimeMillis() + "";
					final Writer result = new StringWriter();
					final PrintWriter printWriter = new PrintWriter(result);
					ex.printStackTrace(printWriter);
					String stacktrace = result.toString();
					printWriter.close();
					String filename = timestamp + ".stacktrace";


					sendEmail(stacktrace);

				}
			};

	@Override
	public void AsynctaskResultwithoutArray(String arr, String url, Map<String, Object> par) {
		// TODO Auto-generated method stub

	}



	/*public void DeleteRecursive(File fileOrDirectory) {

		if (fileOrDirectory.isDirectory())
			for (File child : fileOrDirectory.listFiles())
				DeleteRecursive(child);

		fileOrDirectory.delete();

	}*/

}