package com.stylist.wishlist;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.ContentValues;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Parcelable;
import android.provider.MediaStore.Images;
import android.provider.MediaStore.Images.Media;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.RelativeLayout.LayoutParams;
import android.widget.TextView;

import com.constants.BaseActivity;
import com.constants.Constants;
import com.constants.CustomTextView;
import com.constants.LocalDatabaseHandler;
import com.constants.Utils;
import com.constants.WebImageLoader;

import com.stylist.R;

public class WishListItemDetail extends BaseActivity {
	String wishListItemId, categoryIDN, showImagePath, categoryName;
	com.constants.CustomEditText brandText, costText, commentsText;
	//CustomTextView brandTextView, costTextView, commentsTextView;
	ImageView itemImage, favouriteImagew, deleteImage;
	Button shareBut, deleteBut;
	WebImageLoader mWebImageLoader;
	ArrayList<String> photoUrl = new ArrayList<String>();
	String isFav = "false";
	LocalDatabaseHandler handler = new LocalDatabaseHandler(context);
	ArrayList<String> objectIds = new ArrayList<String>();
	LinearLayout singleButton, doubleButton;
	LocalDatabaseHandler dbHelper;
	com.constants.CustomTextView saveButton, shareButton;
	TextView catNameText;
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
		setContentView(R.layout.activity_wish_list_item_detail);
		setHeading("WISHLIST ITEM DETAIL");
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

		itemImageRelLayout=(RelativeLayout)findViewById(R.id.itemImageRelLayout);

		itemImage = (ImageView) findViewById(R.id.itemImage);
		wishListItemId = getIntent().getStringExtra("wishListItemId");
		System.out.println("wishListItemId============" + wishListItemId);
		categoryIDN = getIntent().getStringExtra("categoryIDN");
		categoryName = getIntent().getStringExtra("categoryName");
		catNameText = (TextView) findViewById(R.id.name_txt);
		catNameText.setText(categoryName);
		brandText = (com.constants.CustomEditText) findViewById(R.id.brand_txtn);
		costText = (com.constants.CustomEditText) findViewById(R.id.cost_txtn);
		commentsText = (com.constants.CustomEditText) findViewById(R.id.comments_txtn);




		favouriteImagew = (ImageView) findViewById(R.id.favouriteImage);
		favouriteImagew.setOnClickListener(this);
		deleteImage = (ImageView) findViewById(R.id.deleteImage);
		deleteImage.setOnClickListener(this);
		mWebImageLoader = new WebImageLoader(context);


		brandText.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				brandText.setCursorVisible(true);

			}
		});
		costText.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				costText.setCursorVisible(true);



			}
		});
		commentsText.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				commentsText.setCursorVisible(true);

			}
		});
		getWishlistData();
	}

	String isFavourite;

	private void getWishlistData() {
		Cursor cWishlist = handler.returnSingleWishlistItem(wishListItemId);
		System.out.println("cWishlist==============" + cWishlist);

		if (cWishlist.moveToFirst()) {
			do {

				String[] arrRgb = cWishlist.getString(cWishlist.getColumnIndex(LocalDatabaseHandler.BACKGROUND_COLOR)).split(",");
				//itemImage.setBackgroundColor(Color.rgb(Integer.parseInt(arrRgb[0].trim()), Integer.parseInt(arrRgb[1].trim()), Integer.parseInt(arrRgb[2].trim())));
				itemImageRelLayout.setBackgroundColor(Color.rgb(Integer.parseInt(arrRgb[0].trim()), Integer.parseInt(arrRgb[1].trim()), Integer.parseInt(arrRgb[2].trim())));






				costText.setText(
						cWishlist.getString(cWishlist.getColumnIndex(LocalDatabaseHandler.WISHLIST_COST_PRICE)));

				commentsText
						.setText(cWishlist.getString(cWishlist.getColumnIndex(LocalDatabaseHandler.WISHLIST_COMMENTS)));

				brandText.setText(cWishlist.getString(cWishlist.getColumnIndex(LocalDatabaseHandler.WISHLIST_BRAND)));
				

				showImagePath = cWishlist.getString(cWishlist.getColumnIndex(LocalDatabaseHandler.IMAGE_PATH));
				itemImage.setImageBitmap(Utils.orientedBitmap(
						cWishlist.getString(cWishlist.getColumnIndex(LocalDatabaseHandler.IMAGE_PATH))));

				isFavourite = cWishlist.getString(cWishlist.getColumnIndex(LocalDatabaseHandler.IS_FAVOURITE));
				if (isFavourite.equalsIgnoreCase("true")) {
					favouriteImagew.setImageResource(R.drawable.heart);
					isFav = "true";
				} else {
					favouriteImagew.setImageResource(R.drawable.unselect_icon);
					isFav = "false";
				}
			} while (cWishlist.moveToNext());
		}

	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		super.onClick(v);
		switch (v.getId()) {
		case R.id.favouriteImage:
			setPrefrenceBool(Constants.isUpdated, true);

			if (isFav.equals("false")) {
				favouriteImagew.setImageResource(R.drawable.heart);
				handler.setWishlistFavourite(wishListItemId, "true");
				isFav = "true";
			} else {
				favouriteImagew.setImageResource(R.drawable.unselect_icon);
				handler.setWishlistFavourite(wishListItemId, "false");
				isFav = "false";
			}

			break;
		case R.id.deleteImage:
			setPrefrenceBool(Constants.isUpdated, true);

			Utils.createSimpleDialog(context, "DELETE", "Are you sure you want to delete?", "Delete", "Cancel",
					new Utils.Method() {
						public void execute() {
							handler.deleteWishListItem(wishListItemId);
							showToastS("Item has been deleted successfully.");
							finish();
						}
					}).show();
			break;
		case R.id.browseButton:

			updateWishList();
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
						share("com.facebook.katana", showImagePath, catNameText.getText().toString());
						shareProgressDialog = showDialog("Please wait...");
						shareDialog.dismiss();


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
						share("com.whatsapp", showImagePath, catNameText.getText().toString());
						shareProgressDialog = showDialog("Please wait...");
						shareDialog.dismiss();


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

	ProgressDialog shareProgressDialog;

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

	public void updateWishList() {
		Utils.createSimpleDialog(context, "Alert", "Save the changes ?", "Save", "Cancel", new Utils.Method() {
			public void execute() {
				dbHelper.updateWishlist(wishListItemId, brandText.getText().toString(), costText.getText().toString(),
						commentsText.getText().toString());
				finish();
			}
		}).show();

	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();

		if (shareProgressDialog != null)
			shareProgressDialog.dismiss();
	}

	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
		if(dbHelper != null){

			dbHelper.close();
		}
		handler.close();
	}

	public void saveOnShare() {
		String imageName;
		Bitmap sourceBitmap = ((BitmapDrawable) itemImage.getDrawable()).getBitmap();
		if (sourceBitmap != null && !sourceBitmap.isRecycled()) {


			try {
				imageName = "IMG_" + System.currentTimeMillis() + ".png";
				ContentValues values = new ContentValues(3);
				values.put(Images.Media.TITLE, imageName);
				values.put(Images.Media.MIME_TYPE, "image/png");
				// values.put("_data", imageFile.getAbsolutePath());
				values.put("_data", showImagePath);
				getContentResolver().insert(Media.EXTERNAL_CONTENT_URI, values);
				// showImagePath = imageFile.getAbsolutePath();
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
