package com.stylist.askstylist;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.provider.MediaStore.Images;
import android.provider.MediaStore.Images.Media;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.constants.AsyncTaskAppWithoutArray;
import com.constants.BaseActivity;
import com.constants.Constants;
import com.constants.LocalDatabaseHandler;
import com.constants.Utils;
import com.constants.WebImageLoader;

import com.parse.ParseUser;
import com.stylist.HomeActivity;
import com.stylist.R;
import com.stylist.lookbook.LookBookActivity;
import com.stylist.lookbook.LooksByStylistActivity;

public class StylistResponseDetailActivity extends BaseActivity {
	LocalDatabaseHandler dbHelper;

	RelativeLayout saveRelLayout, requested_layout;
	com.constants.CustomTextView saveText, bottomText, user, date;
	LinearLayout singleButton, doubleButton;
	ImageView requested_image;
	String parseQueryId;
	String occasion, comments, img_path, createdAt;
	String imgPath, isStylistAddedLiookBook = "";
	int fromNotificationInt = 0;
	public StylistResponseDetailActivity sStylistResponseDetailActivity;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_stylist_response_detail);
		setHeading("STYLIST RESPONSE");
		backClick();
		Utils.checkNetworkConnection(this);
		dbHelper = new LocalDatabaseHandler(context);
		singleButton = (LinearLayout) findViewById(R.id.single_button);
		doubleButton = (LinearLayout) findViewById(R.id.doubleButton);
		singleButton.setVisibility(View.VISIBLE);
		doubleButton.setVisibility(View.GONE);
		bottomText = (com.constants.CustomTextView) findViewById(R.id.bottomText);
		bottomText.setOnClickListener(this);
		bottomText.setText("Add to My Lookbook");
		bottomText.setEnabled(false);
		saveRelLayout = (RelativeLayout) findViewById(R.id.saveRelLayout);
		saveRelLayout.setOnClickListener(this);
		saveRelLayout.setVisibility(View.VISIBLE);
		saveText = (com.constants.CustomTextView) findViewById(R.id.saveText);
		saveText.setText("Response");
		requested_image = (ImageView) findViewById(R.id.requested_image);
		user = (com.constants.CustomTextView) findViewById(R.id.user);
		date = (com.constants.CustomTextView) findViewById(R.id.date);
		requested_layout = (RelativeLayout) findViewById(R.id.requested_layout);
		user.setText(ParseUser.getCurrentUser().getUsername());
		parseQueryId = getIntent().getExtras().getString("queryId");
		fromNotificationInt = getIntent().getExtras().getInt("fromNotificationInt");
		sStylistResponseDetailActivity = this;
		new Handler().postDelayed(new Runnable() {
			@Override
			public void run() {
				bottomText.setEnabled(true);
			}
		}, 2000);

		getStylistResponse();

	}

	private void getStylistResponse() {
		Cursor cStylistResponse = dbHelper.returnAllAskStylist(ParseUser.getCurrentUser().getObjectId());
		System.out.println("out cursor");
		SimpleDateFormat outformat = new SimpleDateFormat("dd MMM yyyy");
		SimpleDateFormat informat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
		if (cStylistResponse.moveToFirst()) {
			do {
				String isDeleted = cStylistResponse
						.getString(cStylistResponse.getColumnIndex(LocalDatabaseHandler.IS_DELETED));
				if (isDeleted.equalsIgnoreCase("false")) {
					if ((cStylistResponse.getString(cStylistResponse.getColumnIndex("parseQueryId"))).trim()
							.equalsIgnoreCase(parseQueryId.trim())) {
						System.out.println("PPPPPPPPPPPPAREEEE=====" + parseQueryId);
						user.setText(ParseUser.getCurrentUser().getUsername());
						requested_image.setImageBitmap(Utils.orientedBitmap(cStylistResponse
								.getString(cStylistResponse.getColumnIndex(LocalDatabaseHandler.PHOTOURL))));

						try {
							date.setText(outformat.format(informat.parse(cStylistResponse
									.getString(cStylistResponse.getColumnIndex(LocalDatabaseHandler.REQUEST_DATE)))));
						}catch (Error error)
						{
							error.printStackTrace();

						} catch (ParseException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
						catch (Exception error)
						{
							error.printStackTrace();

						}
					}

				}
			} while (cStylistResponse.moveToNext());
		}
	}

	public void onStart() {
		super.onStart();
		//.onStartSession(this, getResources().getString(R.string.flurryKey));
	}

	public void onStop() {
		super.onStop();
		//.onEndSession(this);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.saveRelLayout:
			System.out.println("saveRelLayout click");
			Intent intent = new Intent(context, StylistResponseActivity.class);
			intent.putExtra("queryId", parseQueryId);
			startActivity(intent);
			break;
		case R.id.bottomText:
			// saveOnShare();
			setPrefrenceBool(Constants.isUpdated, true);
			System.out.println("img_path========" + img_path);

			if (isStylistAddedLiookBook.equalsIgnoreCase("true")) {
				Toast.makeText(context, "This item Already added in LookBook", Toast.LENGTH_SHORT).show();
			} else {
				long lng = dbHelper.insertLookbookItem(ParseUser.getCurrentUser().getObjectId(),
						"Stylist Response Item", occasion, comments, img_path, createdAt);
				dbHelper.updateAskStylistWithLookbookResponce(parseQueryId);
				gotoActivityWithfinish(LooksByStylistActivity.class, null);
			}
		default:
			break;
		}
	}

	public void saveOnShare() {
		String imageName;
		requested_layout.setDrawingCacheEnabled(true);
		Bitmap sourceBitmap = requested_layout.getDrawingCache();
		if (sourceBitmap != null && !sourceBitmap.isRecycled()) {
			FileOutputStream out = null;
			File imageFile = Utils.getOutputMediaFile();
			try {
				out = new FileOutputStream(imageFile);
				Boolean imageSaved = sourceBitmap.compress(Bitmap.CompressFormat.PNG, 100, out);
			}catch (Error error)
			{
				error.printStackTrace();

			} catch (Exception e) {

			} finally {
				if (out != null) {
					try {
						out.flush();
					} catch (Error error)
					{
						error.printStackTrace();

					}catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					try {
						out.close();
					}catch (Error error)
					{
						error.printStackTrace();

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
			} catch (Error error)
			{
				error.printStackTrace();

			}catch (Exception e) {
				e.printStackTrace();
			}
		}

	}

	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();

		if (dbHelper != null) {

			dbHelper.close();
		}
	}
}
