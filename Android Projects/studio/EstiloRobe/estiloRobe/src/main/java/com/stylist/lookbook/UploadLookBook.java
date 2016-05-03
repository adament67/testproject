package com.stylist.lookbook;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import com.constants.BaseActivity;
import com.constants.LocalDatabaseHandler;
import com.constants.Utils;
import com.parse.ParseUser;
import com.stylist.R;
import com.stylist.styleeditor.StyleEditorActivity;

import android.content.ContentValues;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Typeface;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore.Images;
import android.provider.MediaStore.Images.Media;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class UploadLookBook extends BaseActivity {
	RelativeLayout container;
	LinearLayout singleButton, doubleButton;
	EditText nameText, ocassionText, commentsText;
	com.constants.CustomTextView /* uploadText, */ bottomText;
	Bitmap lookBookBitmap;
	String imgPath, newImgPath;
	LocalDatabaseHandler dbHelper;
	public static UploadLookBook uUploadLookBook;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		requestWindowFeature(Window.FEATURE_NO_TITLE);

		setContentView(R.layout.activity_upload_look_book);
		container = (RelativeLayout) findViewById(R.id.container);
		bottomText = (com.constants.CustomTextView) findViewById(R.id.bottomText);
		nameText = (EditText) findViewById(R.id.nameText);
		ocassionText = (EditText) findViewById(R.id.ocassionText);
		commentsText = (EditText) findViewById(R.id.commentsText);

		Typeface face= Typeface.createFromAsset(getAssets(), "fonts/lvnm.ttf");

//
//		nameText.setTypeface(face);
//		ocassionText.setTypeface(face);
//		commentsText.setTypeface(face);
//		nameText.setOnEditorActionListener(new TextView.OnEditorActionListener() {
//			@Override
//			public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
//
//				if (actionId == EditorInfo.IME_ACTION_NEXT) {
//					ocassionText.requestFocus();
//					return true;
//				}
//				return false;
//			}
//		});
//		ocassionText.setOnEditorActionListener(new TextView.OnEditorActionListener() {
//			@Override
//			public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
//
//				if (actionId == EditorInfo.IME_ACTION_NEXT) {
//					commentsText.requestFocus();
//					return true;
//				}
//				return false;
//			}
//		});
		imgPath = getIntent().getStringExtra("imgPath");
		bottomText.setText("Save");
		bottomText.setOnClickListener(this);
		// singleButton = (LinearLayout) findViewById(R.id.single_button);
		// doubleButton = (LinearLayout) findViewById(R.id.doubleButton);
		// doubleButton.setVisibility(View.GONE);
		dbHelper = new LocalDatabaseHandler(context);
		uUploadLookBook = this;
		container.setOnTouchListener(new View.OnTouchListener() {

			@Override
			public boolean onTouch(View v, MotionEvent event) {
				// TODO Auto-generated method stub
				finish();
				deleteImage(imgPath);
				overridePendingTransition(R.anim.slide_in_up, R.anim.slide_out_down);
				return false;
			}
		});
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		super.onClick(v);
		switch (v.getId()) {
		case R.id.bottomText:
			bottomText.setClickable(false);
			lookBookBitmap = BitmapFactory.decodeFile(imgPath);
			saveOnShare();
			//StyleEditorActivity.islookUploaded = true;
			upLoadLookbook();
			break;
		default:
			break;
		}
	}

	public void upLoadLookbook() {

		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		if (nameText.getText().toString().equals(""))
			showToastS("Please enter name.");
		else if (ocassionText.getText().toString().equals(""))
			showToastS("Please enter occasion.");
		else if (commentsText.getText().toString().equals(""))
		showToastS("Please enter comments.");
		else{
			long lng = dbHelper.insertLookbookItem(ParseUser.getCurrentUser().getObjectId(),
					nameText.getText().toString(), ocassionText.getText().toString(), commentsText.getText().toString(),
					newImgPath, format.format(new Date()));
			deleteImage(imgPath);
			checkAndCloseActivity(StyleEditorActivity.act);
			gotoActivityWithfinish(LookBookActivity.class, null);

		}
	}

	@Override
	public void onBackPressed() {
		// TODO Auto-generated method stub
		super.onBackPressed();
		System.out.println("on back pressed");
		deleteImage(imgPath);
		finish();
		overridePendingTransition(R.anim.slide_in_up, R.anim.slide_out_down);
	}

	public void saveOnShare() {
		String imageName;

		if (lookBookBitmap != null && !lookBookBitmap.isRecycled()) {
			FileOutputStream out = null;
			File imageFile = Utils.getOutputMediaFileLook();
			try {
				out = new FileOutputStream(imageFile);
				Boolean imageSaved = lookBookBitmap.compress(Bitmap.CompressFormat.PNG, 90, out);

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
				values.put(Images.Media.MIME_TYPE, "image/png");
				values.put("_data", imageFile.getAbsolutePath());
				getContentResolver().insert(Media.EXTERNAL_CONTENT_URI, values);
				newImgPath = imageFile.getAbsolutePath();
				System.out.println("imgPath============" + imgPath);
			} catch (Exception e) {
			}
		}

	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		bottomText.setClickable(true);

		System.out.println(">>>>>>>>>>>> onResume");
		super.onResume();

	}

	protected void onPause() {
		super.onPause();
	};

	public void deleteImage(String file_dj_path) {
		File fdelete = new File(file_dj_path);
		if (fdelete.exists()) {
			if (fdelete.delete()) {
				Log.e("-->", "file Deleted :" + file_dj_path);
				callBroadCast();
			} else {
				Log.e("-->", "file not Deleted :" + file_dj_path);
			}
		}
	}

	public void callBroadCast() {
		if (Build.VERSION.SDK_INT >= 14) {
			Log.e("-->", " >= 14");
			MediaScannerConnection.scanFile(this, new String[] { Environment.getExternalStorageDirectory().toString() },
					null, new MediaScannerConnection.OnScanCompletedListener() {
						/*
						 * (non-Javadoc)
						 * 
						 * @see android.media.MediaScannerConnection.
						 * OnScanCompletedListener#onScanCompleted(java.lang.
						 * String, android.net.Uri)
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
	
	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		
		if(dbHelper != null){
			
			dbHelper.close();
		}
	}
}
