package com.stylist;

import java.util.List;

import net.simonvt.menudrawer.MenuDrawer;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.net.Uri;
import android.os.Bundle;
import android.os.Parcelable;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.constants.BaseActivity;
import com.constants.Constants;
import com.constants.CustomTextView;
import com.constants.LocalDatabaseHandler;
import com.constants.Utils;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;

public class SettingsActivity extends BaseActivity {
	CustomTextView headerName;
	LinearLayout backButtonLayout;
	RelativeLayout sync_layout;
	private Context mContext;
	private MenuDrawer viewMenuDrawer;
	CustomTextView rateapp, sync_text, buy_token;
	CustomTextView logout;
	CustomTextView referralTextView;
	CustomTextView shareapp;
	CustomTextView username, trans, about, count_notif;
	ImageView profile_photo, sync_image;
	ProgressBar syncProgressBar;

	Context context = this;

	public static SettingsActivity sSettingsActivity;
Bitmap profBitmap,fbBitmap;
	public SettingsActivity(final Context context, MenuDrawer v,Bitmap b,Bitmap fbBitmap) {
		this.mContext = context;
		this.viewMenuDrawer = v;
		findViewById();
		setListener(mContext);
		sSettingsActivity = this;

            if(b!=null) {
				this.profBitmap = b;
			}



		if(fbBitmap!=null) {
			this.fbBitmap = fbBitmap;
			Log.v("fb_img", fbBitmap.toString());
		}


		dbHelper = new LocalDatabaseHandler(this);
		getUser();
	}



	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		getUser();
		System.out.println("in setting resume");
	}

	private void findViewById() {
		about = (CustomTextView) viewMenuDrawer.findViewById(R.id.about);
		rateapp = (CustomTextView) viewMenuDrawer.findViewById(R.id.rate);
		logout = (CustomTextView) viewMenuDrawer.findViewById(R.id.logout);
		count_notif = (CustomTextView) viewMenuDrawer.findViewById(R.id.count_notif);
		referralTextView = (CustomTextView) viewMenuDrawer.findViewById(R.id.referralTextView);
		profile_photo = (ImageView) viewMenuDrawer.findViewById(R.id.profile_photo);
		sync_image = (ImageView) viewMenuDrawer.findViewById(R.id.sync_image);
		username = (CustomTextView) viewMenuDrawer.findViewById(R.id.username);
		sync_text = (CustomTextView) viewMenuDrawer.findViewById(R.id.sync_text);
		buy_token = (CustomTextView) viewMenuDrawer.findViewById(R.id.buy_token);
		trans = (CustomTextView) viewMenuDrawer.findViewById(R.id.trans);
		sync_layout = (RelativeLayout) viewMenuDrawer.findViewById(R.id.sync_layout);
		syncProgressBar = (ProgressBar) viewMenuDrawer.findViewById(R.id.syncProgressBar);

	}

	private void setListener(final Context context2) {
		rateapp.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				try {
					context2.startActivity(new Intent(Intent.ACTION_VIEW,
							Uri.parse("market://details?id=" + context2.getPackageName())));
				} catch (android.content.ActivityNotFoundException anfe) {
					context2.startActivity(new Intent(Intent.ACTION_VIEW,
							Uri.parse("https://play.google.com/store/apps/details?id=" + context2.getPackageName())));
				}
			}
		});
		trans.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				gotoActivity1(Transaction.class, null);
			}
		});
		sync_layout.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub

				gotoActivity1(SyncDataActivity.class, null);

				// add dialog
				//syncDialog();
			}
		});

		buy_token.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				gotoActivity1(BuyTokenActivity.class, null);
			}
		});

		logout.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				logout1();
			}
		});
		referralTextView.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub

				gotoActivity1(ReferAppActivity.class, null);
			}
		});
		about.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				gotoActivity1(AboutUs.class, null);
			}
		});
	}

	private void shareApp() {
		// TODO Auto-generated method stub
		try {
			Intent in = new Intent(Intent.ACTION_SEND);
			in.setType("text/plain");
			in.putExtra(Intent.EXTRA_SUBJECT, "My application name");
			String sAux = "\nLet me recommend you this application\n\n";
			sAux = sAux + "https://play.google.com/store/apps/details?id=" + getPackageName() + " \n\n";
			in.putExtra(Intent.EXTRA_TEXT, sAux);
			startActivity(Intent.createChooser(in, "choose one"));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}




//Dialog syncDialog;
	/*private void syncDialog() {


		try{


			AlertDialog.Builder builder = new AlertDialog.Builder(this);
			builder.setMessage("Sync your data");

			builder.create().show();

		}catch(Exception e){


		}



		*//*try {

			final Dialog d = new Dialog(HomeActivity.act);

			d.requestWindowFeature(Window.FEATURE_NO_TITLE);
			d.setCancelable(true);
			d.setContentView(R.layout.activity_sync_data);
			d.getWindow().setBackgroundDrawableResource(android.R.color.transparent);


			final com.constants.CustomTextView uploadBtn,dotted_textView;
			uploadBtn = (com.constants.CustomTextView)d.findViewById(R.id.bottomText);
			uploadBtn.setText("Save");
			dotted_textView=(CustomTextView)findViewById(R.id.dotted_textView);
			uploadBtn.setText("Sync your data");
			if(getPrefrenceBool(Constants.isUpdated)==false)
			{
				dotted_textView.setText("No data is updated");
			}
			else
			{
				dotted_textView.setText(getResources().getString(R.string.sync_text));
			}
			uploadBtn.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) {


					if (Utils.checkNetworkConnection(SettingsActivity.this)) {

						if (getPrefrenceBool(Constants.isSyncing) != null && !getPrefrenceBool(Constants.isSyncing)) {
							startSyncservice(SettingsActivity.this, 0);
						} else {
							showToastS("Syncing is in progress. we will notify you on completion.");
						}

					} else {
						showToastS("Check your network connection!");

					}

					d.dismiss();
					gotoActivityWithfinish(HomeActivity.class, null);
				}
			});

			d.show();

		}
		catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
*//*

	}*/














	ProgressDialog dialog;

	private void logout1() {
		try {

			Utils.createSimpleDialog(mContext, mContext.getString(R.string.app_name),
					"Are you sure you want to logout?", "Ok", "Cancel", new Utils.Method() {
						public void execute() {
							dialog = new ProgressDialog(mContext);
							dialog.setMessage("Logging out...");
							dialog.setCancelable(false);
							dialog.show();
							ParseQuery<ParseObject> query = ParseQuery.getQuery("UserToken");
							query.whereEqualTo("TokenID", getPrefrence(Constants.googledeviceid));
							query.whereEqualTo("UserID", ParseUser.getCurrentUser());
							query.findInBackground(new FindCallback<ParseObject>() {
								public void done(List<ParseObject> scoreList, ParseException e) {
									if (e == null) {
										Log.d("score", "Retrieved " + scoreList.size() + " scores");
                                  if(scoreList.size()>=0)
										scoreList.get(0).deleteInBackground();

										ParseUser.logOut();
										clearSharedPreferences1();

										setPrefrenceBool("KEY_FB", false);

										dialog.dismiss();
										gotoActivityWithfinish1(LoginActivity.class, null);

									} else {
										Log.d("score", "Error: " + e.getMessage());
									}
								}
							});

						}
					}).show();

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void clearSharedPreferences1() {

		SharedPreferences prefrence = mContext.getSharedPreferences("Stylist", 0);
		Editor editor = prefrence.edit();
		editor.clear();
		editor.commit();
	}

	private void gotoActivityWithfinish1(Class<?> classActivity, Bundle bundle) {
		viewMenuDrawer.closeMenu();
		Intent intent = new Intent(mContext, classActivity);
		if (bundle != null)
			intent.putExtra("android.intent.extra.INTENT", bundle);
		mContext.startActivity(intent);
		checkAndCloseActivity(HomeActivity.act);
		finish();

	}

	private void gotoActivity1(Class<?> classActivity, Bundle bundle) {

		Intent intent = new Intent(mContext, classActivity);
		if (bundle != null)
			intent.putExtra("android.intent.extra.INTENT", bundle);
		mContext.startActivity(intent);
//		viewMenuDrawer.closeMenu();

	}

	LocalDatabaseHandler handler;

	public String getPrefrence(String key) {
		SharedPreferences prefrence = mContext.getSharedPreferences("Stylist", 0);
		String data = prefrence.getString(key, "");
		return data;
	}

	public Boolean getPrefrenceBool(String key) {
		SharedPreferences prefrence = mContext.getSharedPreferences("Stylist", 0);
		Boolean data = prefrence.getBoolean(key, false);
		return data;
	}

	public void setPrefrenceBool(String key, Boolean value) {
		SharedPreferences prefrence = mContext.getSharedPreferences("Stylist", 0);
		Editor editor = prefrence.edit();
		editor.putBoolean(key, value);
		editor.commit();
	}


	LocalDatabaseHandler dbHelper;

	private void getUser() {
		count_notif.setText("" + Constants.notificationCount);
		System.out.println("sett get userrrrrrrrrrrrrr");
		if (getPrefrenceBool(Constants.isUpdated)) {
			sync_image.setImageResource(R.drawable.red_circle);
		} else {
			sync_image.setImageResource(R.drawable.green_circle);

		}
		if (getPrefrenceBool(Constants.isSyncing) != null && getPrefrenceBool(Constants.isSyncing)) {
			syncProgressBar.setVisibility(View.VISIBLE);
		} else {
			syncProgressBar.setVisibility(View.GONE);

		}


//		if (getPrefrence("username") != null && getPrefrence("username") != "") {
//			username.setText(getPrefrence("username"));
//		} else {ad
//			System.out.println("resume   >>>>>>>>>>  from parse");

		//}
		username.setText(ParseUser.getCurrentUser().getUsername());


//		if(HomeActivity.userName!=null && !HomeActivity.userName.equals(""))
//		username.setText(HomeActivity.userName);
		//setPrefrence("profilePath", profPicPath);
//if(getPrefrence("profilePath")!=null)
//{
//	Bitmap b  = Utils.orientedBitmap(getPrefrence("profilePath"));
//	if(b!=null) {
//		float dx = (float) b.getHeight() / b.getWidth();
//
//		b = Utils.getResizedBitmap(b, 200, (int) (200 * dx));
//
//		//circle image
//		b = ProfileActivity.getRoundedCornerBitmap(b, 140);
//		profile_photo.setImageBitmap(b);
//
//	}
//	setPrefrence("profilePath",null);
//}
		//else
Utils.write("profPicPath==="+getPrefrence("profilePath"));
		if(getPrefrenceBool("KEY_FB")) {
			if (fbBitmap != null) {

				fbBitmap = getRoundedCornerBitmap(fbBitmap, 100);

				profile_photo.setImageBitmap(fbBitmap);

				Log.v("1111111", "fbbbbbbbbbbb");

			} else if (profBitmap != null) {

				profBitmap = getRoundedCornerBitmap(profBitmap, 100);

				profile_photo.setImageBitmap(profBitmap);


				Log.v("1111111", "sgfsgss");
//
			}
		}
		else if (getPrefrence("profilePath")!=null && !(getPrefrence("profilePath").equals(""))) {

			Bitmap bitmap = getRoundedCornerBitmap(Utils.orientedBitmap(getPrefrence("profilePath")), 100);

			profile_photo.setImageBitmap(bitmap);

			Log.v("1111111", "pref");

		}
		else
		{
			profile_photo.setImageResource(R.drawable.img);

			Log.v("1111111", "default");

		}
	}
//else{
//				Log.v("CURSOR", "NULL");
//
//			}
//
//		}else{
//
//			Log.v("DB_HELPER", "NULL");
//		}




		/*if (getPrefrence("profilePath") != null && getPrefrence("profilePath") != "") {

			Log.v("FB_CASE", "Setbitmsap "+BitmapFactory.decodeFile(getPrefrence("profilePath")));

			profile_photo.setImageBitmap(BitmapFactory.decodeFile(getPrefrence("profilePath")));
		}
		else
		{


			ParseApplication appLevel = (ParseApplication)getApplicationContext();

			Log.v("APP_LEVEL", "Setbitmsap " + appLevel.getProfileImage());

			if(appLevel.getProfileImage() != null){

				profile_photo.setImageBitmap(appLevel.getProfileImage());
			}else{

				//
			}*/





		//}


//	public void setProfileImage(Bitmap profileImage) {
//
//		Log.v("111111111", profileImage.toString());
//
//		if(profile_photo != null){
//
//			this.profile_photo.setImageBitmap(profileImage);
//			Log.v("111111111", "ifffffffffffff");
//
//
//		}else{
//
//			ImageView imageView = (ImageView)findViewById(R.id.profile_photo);
//			imageView.setImageBitmap(profileImage);
//
//			Log.v("111111111", "elseeeeeeeeeeeeee");
//		}
//
//
//
//	}







	public void setProfileImage(Bitmap profileImage) {

		Log.v("111111111", profileImage.toString());

		if(profile_photo != null){
			Bitmap profile_photo = getRoundedCornerBitmap(profileImage, 100);

			this.profile_photo.setImageBitmap(profileImage);
			Log.v("111111111", "ifffffffffffff");


		}else{
			Bitmap profile_photo = getRoundedCornerBitmap(profileImage, 100);

			ImageView imageView = (ImageView)findViewById(R.id.profile_photo);
			imageView.setImageBitmap(profileImage);

			Log.v("111111111", "elseeeeeeeeeeeeee");
		}
	}






	//round image
	public static Bitmap getRoundedCornerBitmap(Bitmap bitmap, int pixels) {
		Bitmap output = Bitmap.createBitmap(bitmap.getWidth(), bitmap.getHeight(), Bitmap.Config.ARGB_8888);
		Canvas canvas = new Canvas(output);

		final int color = 0xff424242;
		final Paint paint = new Paint();
		final Rect rect = new Rect(0, 0, bitmap.getWidth(), bitmap.getHeight());
		final RectF rectF = new RectF(rect);
		final float roundPx = pixels;

		paint.setAntiAlias(true);
		canvas.drawARGB(0, 0, 0, 0);
		paint.setColor(color);
		canvas.drawRoundRect(rectF, roundPx, roundPx, paint);

		paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
		canvas.drawBitmap(bitmap, rect, rect, paint);

		return output;
	}


}


