package com.stylist.wardrobe;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.constants.BaseActivity;
import com.constants.Constants;
import com.constants.CustomTextView;
import com.constants.LocalDatabaseHandler;
import com.constants.Utils;
import com.constants.WebImageLoader;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.facebook.share.model.SharePhoto;
import com.facebook.share.model.SharePhotoContent;
import com.facebook.share.widget.ShareDialog;

import com.parse.ParseUser;
//import com.stylist.CategoryService;
import com.stylist.R;

import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.ContentValues;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Parcelable;
import android.provider.MediaStore.Images;
import android.provider.MediaStore.Images.Media;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.RelativeLayout.LayoutParams;
import android.widget.TextView;

public class ItemDetailActivity extends BaseActivity {
	TextView headerName;
	LinearLayout backButtonLayout;
	com.constants.CustomEditText colourText, nameText, CategoryText, ocassionText, remarksText;
	// CustomTextView colourTextView, nameTextView, CategoryTextView,
	// ocassionTextView, remarksTextView;

	String wardItemId;
	ImageView itemImage, favouriteImage, deleteImage;
	WebImageLoader mWebImageLoader;
	int itemPoition = 0;
	TextView CategoryTextView;
	com.constants.CustomTextView shareButton, saveButton ;
	String showImagePath;
	String wardrobeName, categoryName;
	Dialog changeWardrobeNameDialog;
	// PhotoViewAttacher mAttacher;
	String wardName, wardColor, categoryNamen;
	LinearLayout singleButton, doubleButton;
	ProgressDialog shareProgressDialog;
	LocalDatabaseHandler dbHelper;
	String imgPath;
	public static Activity act;
RelativeLayout itemImageRelLayout;

	public void onStart() {
		super.onStart();
		//.onStartSession(this, getResources().getString(R.string.flurryKey));
	}

	public void onStop() {
		super.onStop();
		//.onEndSession(this);
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_item_detail);
		itemImageRelLayout=(RelativeLayout)findViewById(R.id.itemImageRelLayout);
		itemImage = (ImageView) findViewById(R.id.itemImage);
		favouriteImage = (ImageView) findViewById(R.id.favouriteImage);
		deleteImage = (ImageView) findViewById(R.id.deleteImage);
		backClick();
		act = this;

		dbHelper = new LocalDatabaseHandler(context);
		singleButton = (LinearLayout) findViewById(R.id.single_button);
		doubleButton = (LinearLayout) findViewById(R.id.doubleButton);
		singleButton.setVisibility(View.GONE);
		doubleButton.setVisibility(View.VISIBLE);
		saveButton = (com.constants.CustomTextView) findViewById(R.id.browseButton);
		saveButton.setOnClickListener(this);
		saveButton.setText("Save");
		shareButton = (com.constants.CustomTextView) findViewById(R.id.skinColorButton);
		shareButton.setOnClickListener(this);
		shareButton.setText("Share");
		handler = new LocalDatabaseHandler(context);
		backButtonLayout = (LinearLayout) findViewById(R.id.backButtonLayout);
		colourText = (com.constants.CustomEditText) findViewById(R.id.colourText);
		nameText = (com.constants.CustomEditText) findViewById(R.id.nameText);
		ocassionText = (com.constants.CustomEditText) findViewById(R.id.ocassionText);
		remarksText = (com.constants.CustomEditText) findViewById(R.id.remarksText);
		CategoryTextView = (TextView) findViewById(R.id.CategoryTextView);

		/*Typeface face= Typeface.createFromAsset(getAssets(), "fonts/lvnm.ttf");
		CategoryTextView.setTypeface(face);*/




		colourText.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				colourText.setCursorVisible(true);
			}
		});
		nameText.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				nameText.setCursorVisible(true);
			}
		});
		ocassionText.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				// ocassionTextView.setVisibility(View.GONE);
				ocassionText.setCursorVisible(true);
			}
		});
		remarksText.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				// remarksTextView.setVisibility(View.GONE);
				remarksText.setCursorVisible(true);
			}
		});

		backButtonLayout.setOnClickListener(this);
		favouriteImage.setOnClickListener(this);
		deleteImage.setOnClickListener(this);
		mWebImageLoader = new WebImageLoader(context);
		wardItemId = getIntent().getStringExtra("wardrobeItemId");
		wardrobeName = getIntent().getStringExtra("wardrobeName");
		categoryNamen = getPrefrence("categoryName");
		CategoryTextView.setText(getPrefrence("categoryName"));
		CategoryTextView.setFocusable(false);
		System.out.println("categoryNamen==================" + categoryNamen);
		setHeading(wardrobeName);

	}

	String isFav;
	LocalDatabaseHandler handler;

	private void getAllWardrobe() {
		Cursor cWardrobe = handler.returnWardrobe(wardItemId);

		System.out.println("cWardrobe   " + cWardrobe.getCount());

		if (cWardrobe.moveToFirst()) {
			imgPath = cWardrobe.getString(cWardrobe.getColumnIndex(LocalDatabaseHandler.IMAGE_PATH));

			System.out.println("imgPath=hidden warD======"+imgPath);
			System.out.println("imgPath=bitmap======" + Utils
					.orientedBitmap(cWardrobe.getString(cWardrobe.getColumnIndex(LocalDatabaseHandler.IMAGE_PATH))));
			itemImage.setImageBitmap(Utils
					.orientedBitmap(cWardrobe.getString(cWardrobe.getColumnIndex(LocalDatabaseHandler.IMAGE_PATH))));


			String[] arrRgb = cWardrobe.getString(cWardrobe.getColumnIndex(LocalDatabaseHandler.BACKGROUND_COLOR)).split(",");
			//itemImage.setBackgroundColor(Color.rgb(Integer.parseInt(arrRgb[0].trim()), Integer.parseInt(arrRgb[1].trim()), Integer.parseInt(arrRgb[2].trim())));
			itemImageRelLayout.setBackgroundColor(Color.rgb(Integer.parseInt(arrRgb[0].trim()), Integer.parseInt(arrRgb[1].trim()), Integer.parseInt(arrRgb[2].trim())));



			colourText.setText(cWardrobe.getString(cWardrobe.getColumnIndex(LocalDatabaseHandler.WARDROBE_COLOR)));
			ocassionText.setText(cWardrobe.getString(cWardrobe.getColumnIndex(LocalDatabaseHandler.DRESS_CODE)));
			nameText.setText(cWardrobe.getString(cWardrobe.getColumnIndex(LocalDatabaseHandler.WARDROBE_NAME)));
			remarksText.setText(cWardrobe.getString(cWardrobe.getColumnIndex(LocalDatabaseHandler.REMARKS)));
			isFav = cWardrobe.getString(cWardrobe.getColumnIndex(LocalDatabaseHandler.IS_FAVOURITE));
			if (isFav.equals("true"))
				favouriteImage.setImageResource(R.drawable.heart);
			else
				favouriteImage.setImageResource(R.drawable.unselect_icon);

		}

	}

	@Override
	protected void onResume() {
		super.onResume();
		nameText.requestFocus();
		getAllWardrobe();
		if (shareProgressDialog != null)
			shareProgressDialog.dismiss();
//		if (Utils.checkNetworkConnection(this)) {
//			if (getPrefrenceBool(Constants.isSyncing) != null && !getPrefrenceBool(Constants.isSyncing)) {
//				System.out.println("CategoryService home===========");
//				Intent in = new Intent(this, CategoryService.class);
//				in.putExtra("userId", ParseUser.getCurrentUser().getObjectId());
//				startService(in);
//			}
//		}
	}

	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
		handler.close();
		if(dbHelper != null){

			dbHelper.close();
		}
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		super.onClick(v);
		switch (v.getId()) {

		case R.id.browseButton:
			updateWardrobe();

			break;

		case R.id.favouriteImage:
			setPrefrenceBool(Constants.isUpdated, true);
			if (isFav.equals("false")) {
				favouriteImage.setImageResource(R.drawable.heart);
				handler.setWardrobeFavourite(wardItemId, "true");
				isFav = "true";
			} else {
				favouriteImage.setImageResource(R.drawable.unselect_icon);
				handler.setWardrobeFavourite(wardItemId, "false");
				isFav = "false";
			}
			break;

		case R.id.deleteImage:
			setPrefrenceBool(Constants.isUpdated, true);

			Utils.createSimpleDialog(context, "DELETE", "Are you sure you want to delete?", "Delete", "Cancel",
					new Utils.Method() {
						public void execute() {
							System.out.println("wardItemId    " + wardItemId);
							handler.deleteWardrobeItem(wardItemId);
							finish();

						}
					}).show();
			break;
		case R.id.skinColorButton:
			saveOnShare();

			final Dialog shareDialog = new Dialog(context, R.style.DialogSlideAnim);

			shareDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
			shareDialog.setCancelable(true);
			shareDialog.setContentView(R.layout.activity_share);
			shareDialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
			CustomTextView text2 = (CustomTextView) shareDialog.findViewById(R.id.whatsapp);
			CustomTextView text1 = (CustomTextView) shareDialog.findViewById(R.id.facebook);
			RelativeLayout uploadRelativeLayout = (RelativeLayout) shareDialog.findViewById(R.id.uploadRelativeLayout);
			DisplayMetrics displaymetrics = new DisplayMetrics();
			getWindowManager().getDefaultDisplay().getMetrics(displaymetrics);
			uploadRelativeLayout.setLayoutParams(
					new RelativeLayout.LayoutParams(displaymetrics.widthPixels, LayoutParams.WRAP_CONTENT));
			text1.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View arg0) {
					// TODO Auto-generated method stub
					// fbSharing();
					boolean installed = appInstalledOrNot("com.facebook.katana");

					if (installed) {
						// This intent will help you to launch if the package is
						// already installed

						share("com.facebook.katana", showImagePath, nameText.getText().toString());
						shareProgressDialog = showDialog("Please wait...");
						shareDialog.dismiss();
						/*
						 * Intent LaunchIntent =
						 * getPackageManager().getLaunchIntentForPackage(
						 * "com.whatsapp"); startActivity(LaunchIntent);
						 */

					} else {

						showToastL("App is not installed on your phone");
					}

				}
			});
			text2.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View arg0) {
					// TODO Auto-generated method stub
					// onClickWhatsApp(showImagePath);

					boolean installed = appInstalledOrNot("com.whatsapp");

					if (installed) {
						// This intent will help you to launch if the package is
						// already installed

						share("com.whatsapp", showImagePath, nameText.getText().toString());
						shareProgressDialog = showDialog("Please wait...");
						shareDialog.dismiss();

						/*
						 * Intent LaunchIntent =
						 * getPackageManager().getLaunchIntentForPackage(
						 * "com.whatsapp"); startActivity(LaunchIntent);
						 */

					} else {

						showToastL("App is not installed on your phone");
					}

				}
			});
			shareDialog.show();
			overridePendingTransition(R.anim.slide_in_up, R.anim.slide_out_up);

			break;
		default:
			break;
		}
	}

	public void updateWardrobe() {
		setPrefrenceBool(Constants.isUpdated, true);
		Utils.createSimpleDialog(context, "Alert", "Save the changes ?", "Save", "Cancel", new Utils.Method() {
			public void execute() {
				dbHelper.updateWardrobe(wardItemId, nameText.getText().toString(), colourText.getText().toString(),
						ocassionText.getText().toString());
				finish();
			}
		}).show();

	}

	public void share(String nameApp, String imagePath, String message) {
		try {
			List<Intent> targetedShareIntents = new ArrayList<Intent>();
			Intent share = new Intent(android.content.Intent.ACTION_SEND);
			share.setType("image/jpeg");
			List<ResolveInfo> resInfo = getPackageManager().queryIntentActivities(share, 0);
			if (!resInfo.isEmpty()) {
				for (ResolveInfo info : resInfo) {
					Intent targetedShare = new Intent(android.content.Intent.ACTION_SEND);
					targetedShare.setType("image/jpeg"); // put here your mime
															// type
					if (info.activityInfo.packageName.toLowerCase().contains(nameApp)
							|| info.activityInfo.name.toLowerCase().contains(nameApp)) {
						targetedShare.putExtra(Intent.EXTRA_SUBJECT, "EstiloRobe");
						targetedShare.putExtra(Intent.EXTRA_TEXT, message);
						targetedShare.putExtra(Intent.EXTRA_STREAM, Uri.fromFile(new File(imagePath)));
						targetedShare.setPackage(info.activityInfo.packageName);
						targetedShareIntents.add(targetedShare);
					}
				}
				Intent chooserIntent = Intent.createChooser(targetedShareIntents.remove(0), "Select app to share");
				chooserIntent.putExtra(Intent.EXTRA_INITIAL_INTENTS, targetedShareIntents.toArray(new Parcelable[] {}));
				startActivity(chooserIntent);
			}
		} catch (Exception e) {
			Log.v("VM", "Exception while sending image on" + nameApp + " " + e.getMessage());
		}
	}

	private CallbackManager callbackManager;
	private LoginManager manager;

	private void fbSharing() {
		FacebookSdk.sdkInitialize(getApplicationContext());
		callbackManager = CallbackManager.Factory.create();

		List<String> permissionNeeds = Arrays.asList("publish_actions");

		manager = LoginManager.getInstance();

		manager.logInWithPublishPermissions(this, permissionNeeds);

		manager.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
			@Override
			public void onSuccess(LoginResult loginResult) {

				System.out.println("inside registerCallback  onSuccess   " + loginResult);
				publishImage();
			}

			@Override
			public void onCancel() {
				showToastS("onCancel");
				System.out.println("inside onCancel");
			}

			@Override
			public void onError(FacebookException exception) {
				showToastS("inside onError");
				System.out.println("inside onError");
			}
		});
		// publishImage();
	}

	private void publishImage() {
		Bitmap image = ((BitmapDrawable) itemImage.getDrawable()).getBitmap();
		SharePhoto photo = new SharePhoto.Builder().setBitmap(image)
				.setCaption("Welcome To Facebook Photo Sharing on steroids!").build();

		SharePhotoContent content = new SharePhotoContent.Builder().addPhoto(photo).build();

		ShareDialog.show(context, content);
	}

	@Override
	protected void onActivityResult(int requestCode, int responseCode, Intent data) {
		super.onActivityResult(requestCode, responseCode, data);
		callbackManager.onActivityResult(requestCode, responseCode, data);
	}

	public void saveOnShare() {
		String imageName;
		Bitmap sourceBitmap = ((BitmapDrawable) itemImage.getDrawable()).getBitmap();
		if (sourceBitmap != null && !sourceBitmap.isRecycled()) {
			/*
			 * FileOutputStream out = null; File imageFile =
			 * Utils.getOutputMediaFileWard(context); try { out = new
			 * FileOutputStream(imageFile); Boolean imageSaved =
			 * sourceBitmap.compress(Bitmap.CompressFormat.PNG, 90, out);
			 *
			 * } catch (Exception e) {
			 *
			 * } finally { if (out != null) { try { out.flush(); } catch
			 * (IOException e) { // TODO Auto-generated catch block
			 * e.printStackTrace(); } try { out.close(); } catch (IOException e)
			 * { // TODO Auto-generated catch block e.printStackTrace(); } } }
			 */
			try {
				imageName = "IMG_" + System.currentTimeMillis() + ".png";
				ContentValues values = new ContentValues(3);
				values.put(Images.Media.TITLE, imageName);
				values.put(Images.Media.MIME_TYPE, "image/png");
				values.put("_data", imgPath);
				getContentResolver().insert(Media.EXTERNAL_CONTENT_URI, values);
				showImagePath = imgPath;
			} catch (Exception e) {
			}
		}

	}

	private boolean appInstalledOrNot(String uri) {
		PackageManager pm = getPackageManager();
		boolean app_installed = false;
		try {
			pm.getPackageInfo(uri, PackageManager.GET_ACTIVITIES);
			app_installed = true;
		} catch (PackageManager.NameNotFoundException e) {
			app_installed = false;
		}
		return app_installed;
	}
	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();

		if(dbHelper != null){

			dbHelper.close();
		}
	}
	public static String imagepath;
}
