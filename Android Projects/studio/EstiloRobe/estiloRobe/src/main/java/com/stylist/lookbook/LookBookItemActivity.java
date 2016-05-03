package com.stylist.lookbook;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
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
import android.view.Window;
import android.view.View.OnClickListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.RelativeLayout.LayoutParams;

import com.constants.BaseActivity;
import com.constants.CustomEditText;
import com.constants.CustomTextView;
import com.constants.LocalDatabaseHandler;
import com.constants.LoogEditDIalog;
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
import com.stylist.CroppingActivity;
import com.stylist.LoginActivity;
import com.stylist.R;
import com.stylist.styleeditor.StyleEditorActivity;
import com.stylist.wardrobe.UploadWardribeActivity;

public class LookBookItemActivity extends BaseActivity  implements LoogEditDIalog.OnLookEditDialogListener{
	RelativeLayout saveRelLayout;
	CustomTextView headerName, saveText;
	// CustomEditText ocassionText, nameText, commentsText;
	LinearLayout backButtonLayout, shareImageLayout;
	ImageView itemImage, favouriteImagen, delImagen;
	ListView myLookBookItemListView;
	WebImageLoader mWebImageLoader;
	ProgressDialog shareProgressDialog;
	public static LookBookItemActivity lLookBookItemActivity;
	int pos = 0;
	String isFav = "false";
	LocalDatabaseHandler dbHelper ;
	com.constants.CustomTextView bottomText;
	String lookName;

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
		setContentView(R.layout.activity_look_book_item);
		backClick();
		dbHelper = new LocalDatabaseHandler(context);
		saveRelLayout = (RelativeLayout) findViewById(R.id.saveRelLayout);
		saveRelLayout.setVisibility(View.VISIBLE);
		saveText = (CustomTextView) findViewById(R.id.saveText);
		saveText.setText("SHARE");
		bottomText = (com.constants.CustomTextView) findViewById(R.id.bottomText);
		bottomText.setText("Info");
		lLookBookItemActivity = this;
		bottomText.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				// updateLookbook();
				lookEditDialog();
//                Utils.write("lookitem name"+lookBookList.get(pos).name );
//				Utils.write("look ocassion"+lookBookList.get(pos).ocassion );
//				Utils.write("look comments"+lookBookList.get(pos).comments );
//				LoogEditDIalog dialog = new LoogEditDIalog();
//				Bundle args = new Bundle();
//				args.putString(LoogEditDIalog.KEY_LOOKBOOK_NAME, lookBookList.get(pos).name );
//				args.putString(LoogEditDIalog.KEY_OCCASION_NAME, lookBookList.get(pos).ocassion);
//				args.putString(LoogEditDIalog.KEY_COMMENTS, lookBookList.get(pos).comments);
//
//				dialog.setArguments(args);
//
//				dialog.show(getSupportFragmentManager(), "LOOK_EDIT_DIALOG");
			}
		});
		shareImageLayout = (LinearLayout) findViewById(R.id.shareImageLayout);
		/*
		 * ocassionText = (CustomEditText) findViewById(R.id.ocassionText);
		 * nameText = (CustomEditText) findViewById(R.id.lookbookNameText);
		 * commentsText = (CustomEditText) findViewById(R.id.commentsText);
		 */
		itemImage = (ImageView) findViewById(R.id.itemImage);
		favouriteImagen = (ImageView) findViewById(R.id.favouriteImagen);
		delImagen = (ImageView) findViewById(R.id.delImagen);
		favouriteImagen.setOnClickListener(this);
		delImagen.setOnClickListener(this);
		pos = getIntent().getIntExtra("position", 0);
		mWebImageLoader = new WebImageLoader(context);
		saveText.setOnClickListener(this);

	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		setListData();
		setHeading(lookBookList.get(pos).name);
		lookName = lookBookList.get(pos).name;
		if (shareProgressDialog != null)
			shareProgressDialog.dismiss();

	}

	ArrayList<LookBookDetail> lookBookList = new ArrayList<LookBookDetail>();

	private void setListData() {
		lookBookList.clear();
		Cursor cLookBook = dbHelper.getLookbookItem(ParseUser.getCurrentUser().getObjectId());
		BitmapFactory.Options opt=new BitmapFactory.Options();
		opt.inSampleSize=1;

		if (cLookBook.moveToFirst()) {
			do {
				String isDeleted = cLookBook.getString(cLookBook.getColumnIndex(LocalDatabaseHandler.IS_DELETED));
				if (isDeleted.equalsIgnoreCase("false")) {
					lookBookList.add(new LookBookDetail(
							cLookBook.getInt(cLookBook.getColumnIndex(LocalDatabaseHandler.LOOKBOOK_ID)),
							cLookBook.getString(cLookBook.getColumnIndex(LocalDatabaseHandler.LOOKBOOK_OCASSION)),
							cLookBook.getString(cLookBook.getColumnIndex(LocalDatabaseHandler.LOOKBOOK_NAME)),
							cLookBook.getString(cLookBook.getColumnIndex(LocalDatabaseHandler.IMAGE_PATH)),
							cLookBook.getString(cLookBook.getColumnIndex(LocalDatabaseHandler.CREATED_AT)),

							cLookBook.getString(cLookBook.getColumnIndex(LocalDatabaseHandler.COMMENTS)),
							cLookBook.getString(cLookBook.getColumnIndex(LocalDatabaseHandler.IS_FAVOURITE)),
							cLookBook.getString(cLookBook.getColumnIndex(LocalDatabaseHandler.IS_NEW)),
							cLookBook.getString(cLookBook.getColumnIndex(LocalDatabaseHandler.IS_UPDATED)),
							cLookBook.getString(cLookBook.getColumnIndex(LocalDatabaseHandler.IS_DELETED)),
							BitmapFactory.decodeFile(cLookBook.getString(cLookBook.getColumnIndex(LocalDatabaseHandler.IMAGE_PATH)),opt)));

				}
			} while (cLookBook.moveToNext());
		}
		/*
		 * nameText.setText(lookBookList.get(pos).name);
		 * ocassionText.setText(lookBookList.get(pos).ocassion);
		 * commentsText.setText(lookBookList.get(pos).comments);
		 */
		itemImage.setImageBitmap(lookBookList.get(pos).lookImg);
		System.out.println(">>>>>>>>>>>>>   " + lookBookList.get(pos).IsFavourite);
		if (lookBookList.get(pos).IsFavourite.equalsIgnoreCase("false")) {
			favouriteImagen.setImageResource(R.drawable.unselect_icon);
			isFav = "false";
		} else {
			isFav = "true";
			favouriteImagen.setImageResource(R.drawable.heart);

		}
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		super.onClick(v);
		switch (v.getId()) {
		case R.id.favouriteImagen:
			if (isFav.equals("false")) {
				favouriteImagen.setImageResource(R.drawable.heart);
				dbHelper.setLookbookFavourite(lookBookList.get(pos).lookbookId, "true");
				isFav = "true";
			} else {
				favouriteImagen.setImageResource(R.drawable.unselect_icon);
				dbHelper.setLookbookFavourite(lookBookList.get(pos).lookbookId, "false");
				isFav = "false";
			}

			break;
		case R.id.delImagen:
			Utils.createSimpleDialog(context, "DELETE", "Are you sure to Delete This Item ?", "DELETE", "CANCEL",
					new Utils.Method() {

						@Override
						public void execute() {
							// TODO Auto-generated method stub
							deleteLookBook(pos);
						}
					}).show();

			break;
		case R.id.saveText:

			final Dialog shareDialog = new Dialog(context, R.style.DialogSlideAnim);

			shareDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
			shareDialog.setCancelable(true);
			shareDialog.setContentView(R.layout.activity_share);
			shareDialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
			CustomTextView text1 = (CustomTextView) shareDialog.findViewById(R.id.whatsapp);
			CustomTextView text2 = (CustomTextView) shareDialog.findViewById(R.id.facebook);
			RelativeLayout uploadRelativeLayout = (RelativeLayout) shareDialog.findViewById(R.id.uploadRelativeLayout);
			DisplayMetrics displaymetrics = new DisplayMetrics();
			getWindowManager().getDefaultDisplay().getMetrics(displaymetrics);
			uploadRelativeLayout.setLayoutParams(
					new RelativeLayout.LayoutParams(displaymetrics.widthPixels, LayoutParams.WRAP_CONTENT));
			text2.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View arg0) {
					// TODO Auto-generated method stub

					boolean installed = appInstalledOrNot("com.facebook.katana");

					if (installed) {
						// This intent will help you to launch if the package is
						// already installed

						share("com.facebook.katana", lookBookList.get(pos).imgPath, lookName);
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
			text1.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View arg0) {
					// TODO Auto-generated method stub
					// onClickWhatsApp(showImagePath);

					boolean installed = appInstalledOrNot("com.whatsapp");

					if (installed) {
						// This intent will help you to launch if the package is
						// already installed

						share("com.whatsapp", lookBookList.get(pos).imgPath, lookName);
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
						targetedShare.putExtra(Intent.EXTRA_SUBJECT, "Sample Photo");
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
		}catch (Error error)
		{
			error.printStackTrace();

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
	}

	private void publishImage() {
		Bitmap image = ((BitmapDrawable) itemImage.getDrawable()).getBitmap();
		//
		SharePhoto photo = new SharePhoto.Builder().setBitmap(image)
				.setCaption("Welcome To Facebook Photo Sharing on steroids!").build();

		SharePhotoContent content = new SharePhotoContent.Builder().addPhoto(photo).build();

		ShareDialog.show(context, content);
	}

	public void deleteLookBook(int pos) {
		dbHelper.deleteLookbookItem(lookBookList.get(pos).lookbookId);
		showToastS("Item Deleted");
		finish();
	}

	public void updateLookbook(final String newname, final String newoccasion, final String newcomments) {
		
		Utils.createSimpleDialog(context, "Alert", "Save the changes ?", "Save", "Cancel", new Utils.Method() {
			public void execute() {
				dbHelper.updateLookbook(lookBookList.get(pos).lookbookId, newname, newoccasion, newcomments);
				lookName = newname;
				finish();
			}
		}).show();
		
		

	}

	private boolean appInstalledOrNot(String uri) {
		PackageManager pm = getPackageManager();
		boolean app_installed = false;
		try {
			pm.getPackageInfo(uri, PackageManager.GET_ACTIVITIES);
			app_installed = true;
		} catch (Error error)
		{
			error.printStackTrace();

		}catch (PackageManager.NameNotFoundException e) {
			app_installed = false;
			e.printStackTrace();
		}catch (Exception error)
		{
			error.printStackTrace();

		}
		return app_installed;
	}

	String showImagePath;

	@Override
	protected void onActivityResult(int requestCode, int responseCode, Intent data) {
		super.onActivityResult(requestCode, responseCode, data);
		System.out.println("on Activity Result");
		callbackManager.onActivityResult(requestCode, responseCode, data);
	}

	private void lookEditDialog() {
		try {
		final	String name,occasion,comments;
			final Dialog lookEditDialog = addDialog(R.layout.look_edit_dialog);
			final EditText occasionText = (EditText) lookEditDialog.findViewById(R.id.ocassionText);
			final EditText nameText = (EditText) lookEditDialog.findViewById(R.id.lookbookNameText);
			final EditText commentsText = (EditText) lookEditDialog.findViewById(R.id.commentsText);

			Typeface face= Typeface.createFromAsset(getAssets(), "fonts/lvnm.ttf");
			occasionText.setTypeface(face);
			nameText.setTypeface(face);
			commentsText.setTypeface(face);

			final CustomTextView save = (CustomTextView) lookEditDialog.findViewById(R.id.saveButn);
			CustomTextView cancelBut = (CustomTextView) lookEditDialog.findViewById(R.id.cancelButnc);
			name=lookBookList.get(pos).name;
			occasion=lookBookList.get(pos).ocassion;
			comments=lookBookList.get(pos).comments;

			nameText.setText(lookBookList.get(pos).name);
			occasionText.setText(lookBookList.get(pos).ocassion);
			commentsText.setText(lookBookList.get(pos).comments);

//			nameText.setCursorVisible(false);
//			nameText.setOnClickListener(new View.OnClickListener() {
//
//				@Override
//				public void onClick(View v) {
//					// TODO Auto-generated method stub
//					nameText.setCursorVisible(true);
//				}
//			});
//
//			occasionText.setOnClickListener(new View.OnClickListener() {
//
//				@Override
//				public void onClick(View v) {
//					// TODO Auto-generated method stub
//					occasionText.setCursorVisible(true);
//				}
//			});
//			commentsText.setOnClickListener(new View.OnClickListener() {
//
//				@Override
//				public void onClick(View v) {
//					// TODO Auto-generated method stub
//					commentsText.setCursorVisible(true);
//				}
//			});



			save.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View arg0) {
					try {
//						if((!name.equals(nameText.getText().toString()))||(!occasion.equals( occasionText.getText().toString()))||(!comments.equals(commentsText.getText().toString())))
//						{
						if (name.equals(""))
							showToastS("Please enter name.");
//						else
//						if (occasion.equals(""))
//							showToastS("Please enter ocassion.");
//						else
//						if (comments.equals(""))
//							showToastS("Please enter comments.");
                         else
						{
							//hide keyboard
							InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
							imm.hideSoftInputFromWindow(save.getWindowToken(), 0);

							updateLookbook(nameText.getText().toString(), occasionText.getText().toString(),
									commentsText.getText().toString());
							lookEditDialog.dismiss();

						}



						//}
//						else
//						{
//
//							//hide keyboard
//							InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
//							imm.hideSoftInputFromWindow(save.getWindowToken(), 0);
//
//							lookEditDialog.dismiss();
//
//						}


					} catch (Exception e) {
						// TODO: handle exception
					}
				}
			});
			cancelBut.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View arg0) {
					lookEditDialog.dismiss();

				}
			});
			lookEditDialog.show();
			overridePendingTransition(R.anim.slide_in_up, R.anim.slide_out_up);

		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		catch (Error error)
		{
			error.printStackTrace();

		}
	}
	
	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		
		if(dbHelper != null){
			
			dbHelper.close();
		}
	}

	@Override
	public void onPositiveClick(Bundle args, View view) {

		try {

				updateLookbook(args.getString(LoogEditDIalog.KEY_LOOKBOOK_NAME), args.getString(LoogEditDIalog.KEY_OCCASION_NAME),
						args.getString(LoogEditDIalog.KEY_COMMENTS));

				//dissmiss current dialog
			//overridePendingTransition(R.anim.slide_in_up, R.anim.slide_out_up);

		} catch (Error error)
		{
			error.printStackTrace();

		}catch (Exception e) {
			// TODO: handle exception
		}

	}


	@Override
	public void onNegiviteClick(View view) {

	}

}