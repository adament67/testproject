package com.stylist.askstylist;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.ProgressDialog;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.constants.AsyncTaskAppWithoutArray;
import com.constants.BaseActivity;
import com.constants.CustomTextView;
import com.constants.LocalDatabaseHandler;
import com.constants.Utils;
import com.constants.WebImageLoader;
import com.parse.FunctionCallback;
import com.parse.ParseCloud;
import com.parse.ParseException;
import com.parse.ParseUser;
import com.stylist.R;

public class StylistResponseActivity extends BaseActivity {

	RelativeLayout saveRelLayout;
	CustomTextView saveText, user, date, stylistComment, /* nameInComment, */response;
	ArrayList<AskStylistModelClass> askStylistItem = new ArrayList<AskStylistModelClass>();
	ImageView requestedImg, likedImg;
	int position;
	WebImageLoader webImgLoader;
	Boolean isLiked;

	LocalDatabaseHandler dbHelper;
	String parseQueryId;
	public static StylistResponseActivity sStylistResponseActivity;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_stylist_response);
		setHeading("STYLIST RESPONSE DETAIL");
		backClick();
		sStylistResponseActivity = this;
		Utils.checkNetworkConnection(this);
		requestedImg = (ImageView) findViewById(R.id.requestedImg);
		likedImg = (ImageView) findViewById(R.id.likedImg);
		user = (CustomTextView) findViewById(R.id.user);
		date = (CustomTextView) findViewById(R.id.date);
		response = (CustomTextView) findViewById(R.id.response);
		user.setText(ParseUser.getCurrentUser().getUsername());
		dbHelper = new LocalDatabaseHandler(context);
		parseQueryId = getIntent().getExtras().getString("queryId");
		getStylistResponse();

		likedImg.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				if (isLiked == true) {
					likedImg.setImageResource(R.drawable.unselect_icon);
					// likeStylist(false);
					isLiked = false;
				} else {
					likedImg.setImageResource(R.drawable.heart);
					// likeStylist(true);
					isLiked = true;

				}

			}
		});

	}

	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		if (dbHelper != null) {
			dbHelper.close();
		}
	}

//	private void getStylistResponse() {
//		// TODO Auto-generated method stub
//		Cursor cStylistResponse = dbHelper.returnAllAskStylist(ParseUser.getCurrentUser().getObjectId());
//		// if (cursor != null) {
//		if (cStylistResponse.moveToFirst()) {
//			String isDeleted = cStylistResponse
//					.getString(cStylistResponse.getColumnIndex(LocalDatabaseHandler.IS_DELETED));
//			if (isDeleted.equalsIgnoreCase("false")) {
//				if ((cStylistResponse.getString(cStylistResponse.getColumnIndex("parseQueryId"))).trim()
//						.equalsIgnoreCase(parseQueryId.trim())) {
//					System.out.println("STYLIST_COMMENTS================;lj============="+cStylistResponse
//							.getString(cStylistResponse.getColumnIndex(LocalDatabaseHandler.STYLIST_COMMENTS)));
//					response.setText(cStylistResponse
//							.getString(cStylistResponse.getColumnIndex(LocalDatabaseHandler.STYLIST_COMMENTS)));
//					
//					System.out.println("PHOTOURL============="+cStylistResponse
//							.getString(cStylistResponse.getColumnIndex(LocalDatabaseHandler.PHOTOURL)));
//					requestedImg.setImageBitmap(Utils.orientedBitmap(cStylistResponse
//							.getString(cStylistResponse.getColumnIndex(LocalDatabaseHandler.PHOTOURL))));
//					user.setText(ParseUser.getCurrentUser().getUsername());
//
//					System.out.println("date   " + cStylistResponse
//							.getString(cStylistResponse.getColumnIndex(LocalDatabaseHandler.REQUEST_DATE)));
//					SimpleDateFormat sfDate_day = new SimpleDateFormat("yyyy-MM-dd");
//					SimpleDateFormat dfnew = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
//					try {
//						String dates = sfDate_day.format(dfnew.parse(cStylistResponse
//								.getString(cStylistResponse.getColumnIndex(LocalDatabaseHandler.REQUEST_DATE))));
//						String[] arr = dates.split("-");
//						date.setText(formatDateto(Integer.parseInt(arr[2]), Integer.parseInt(arr[0]),
//								Integer.parseInt(arr[1])));
//
//					} catch (Exception e) {
//						e.printStackTrace();
//					}
//				}
//			}
//		}
//	}
	// }

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
						requestedImg.setImageBitmap(Utils.orientedBitmap(cStylistResponse
								.getString(cStylistResponse.getColumnIndex(LocalDatabaseHandler.PHOTOURL))));
						response.setText(cStylistResponse
								.getString(cStylistResponse.getColumnIndex(LocalDatabaseHandler.STYLIST_COMMENTS)));
						try {
							date.setText(outformat.format(informat.parse(cStylistResponse
									.getString(cStylistResponse.getColumnIndex(LocalDatabaseHandler.REQUEST_DATE)))));
						}catch (Error error)
						{
							error.printStackTrace();

						} catch (java.text.ParseException e) {
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
	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		getStylistResponse();

	}

}
