package com.stylist.askstylist;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
//import java.util.logging.Handler;
//import java.util.logging.Handler;
import java.util.logging.LogRecord;

import org.json.JSONException;
import org.json.JSONObject;

import android.R.string;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Looper;
import android.os.Message;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.constants.AsyncTaskAppWithoutArray;
import com.constants.BaseActivity;
import com.constants.Constants;
import com.constants.CustomTextView;
import com.constants.LocalDatabaseHandler;
import com.constants.Utils;

import com.parse.ParseUser;
//import com.stylist.BackServiceForCloset;
//import com.stylist.CategoryService;
import com.stylist.R;

public class AskStylistActivity extends BaseActivity {

	public static AskStylistActivity aAskStylistActivity;
	LinearLayout singleButton, doubleButton;
	CustomTextView skinColorButton, browseButton;
	EditText ocassion, comments, preferredStyle, notComfort;
	LocalDatabaseHandler dbHelper;
	int uploadToOriginal = 0;
	

	private OnServiceStartListener mListener;
	public interface OnServiceStartListener{

		void onStartService(Bundle data);
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_ask_stylist);

		mListener = (OnServiceStartListener)AskStylistRequestListActivity.aAskStylistRequestListActivity;


		Utils.setupUI(findViewById(R.id.linTop), this);
		setHeading("SEND A QUERY");
		backClick();
		aAskStylistActivity = this;
		Utils.checkNetworkConnection(this);
		singleButton = (LinearLayout) findViewById(R.id.single_button);
		doubleButton = (LinearLayout) findViewById(R.id.doubleButton);
		singleButton.setVisibility(View.VISIBLE);
		doubleButton.setVisibility(View.GONE);
		browseButton = (CustomTextView) findViewById(R.id.bottomText);
		browseButton.setOnClickListener(this);
		browseButton.setText("Send Query");
		ocassion = (EditText) findViewById(R.id.occasion);
		comments = (EditText) findViewById(R.id.n_comments);
		preferredStyle = (EditText) findViewById(R.id.preferredStyle);
		notComfort = (EditText) findViewById(R.id.notComfort);
		ocassion.requestFocus();
		dbHelper=new LocalDatabaseHandler(this);
	}
@Override
protected void onResume() {
	// TODO Auto-generated method stub
	super.onResume();
//	if (Utils.checkNetworkConnection(this)) {
//		if (getPrefrenceBool(Constants.isSyncing) != null && !getPrefrenceBool(Constants.isSyncing)) {
//			System.out.println("CategoryService home===========");
//			Intent in = new Intent(this, CategoryService.class);
//			in.putExtra("userId", ParseUser.getCurrentUser().getObjectId());
//			startService(in);
//		}
//	}

}
	public void onStart() {
		super.onStart();
		//.onStartSession(this, getResources().getString(R.string.flurryKey));
	}

	public void onStop() {
		super.onStop();
		//.onEndSession(this);
	}

	boolean abd = false;
	SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		super.onClick(v);
		switch (v.getId()) {
		case R.id.bottomText:

			if (ocassion.getText().toString().trim().equalsIgnoreCase("")) {
				showToastS("Please fill occasion of wearing field!");
			} else if (comments.getText().toString().trim().equalsIgnoreCase("")) {
				showToastS("Please fill comments field!");
			} else if (!checkUserName()) {
				Utils.createSimpleDialog(aAskStylistActivity, "Alert", "Please fill all necessary information in user profile!", "OK");
				//showToastS("Please fill all necessary information in user profile!");
		} else {
				if(Utils.checkNetworkConnection(this)) {
					Bundle askStylistData = new Bundle();
					askStylistData.putInt("fromAskStylist", 1);
					askStylistData.putString("ocassion", ocassion.getText().toString());
					askStylistData.putString("comments", comments.getText().toString());
					askStylistData.putString("preferredStyle", preferredStyle.getText().toString());
					askStylistData.putString("notComfort", notComfort.getText().toString());
					askStylistData.putString("date", format.format(new Date()));
					askStylistData.putString("objectId", "");
					mListener.onStartService(askStylistData);
					finish();
				}
				else
				{
					showToastS("Query can not be asked offline!");
				}

			}
			break;

		case R.id.skinColorButton:

		default:
			break;
		}

	}

	public Boolean checkUserName() {
		LocalDatabaseHandler databaseHandler = new LocalDatabaseHandler(context);
		final Cursor c = databaseHandler.getUser(ParseUser.getCurrentUser().getObjectId());
		// System.out.println("call to saveUserProfile " + c.getCount());
		if (c.moveToFirst()) {
			if (c.getString(c.getColumnIndex(LocalDatabaseHandler.HIPS)) != null
					&& c.getString(c.getColumnIndex(LocalDatabaseHandler.HIPS)) != "") {
				return true;
			} else {
				return false;
			}
		} else {
			return false;
		}
	}


	public void uploadAskStylistQuery() {
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("UserId", ParseUser.getCurrentUser().getObjectId());
		params.put("occasion", ocassion.getText().toString());
		params.put("comments", comments.getText().toString());
		params.put("preferredStyle", preferredStyle.getText().toString());
		params.put("unComfortStyle", comments.getText().toString());
		// System.out.println("==============uploadAskStylistQuery==============");
		new AsyncTaskAppWithoutArray(true, params, callBackListnerwithoutArray, context, "insertAskStylistQuery");
	}

	@Override
	public void AsynctaskResultwithoutArray(String arr, String url, Map<String, Object> par) {
		// TODO Auto-generated method stub
		super.AsynctaskResultwithoutArray(arr, url, par);
		if (url.equals("insertAskStylistQuery")) {
			try {

				final JSONObject ooo;
				ooo = new JSONObject(arr);
				// if (uploadToOriginal == 1) {
				long lng = dbHelper.insertAskStylist(ParseUser.getCurrentUser().getObjectId(),
						ocassion.getText().toString(), comments.getText().toString(), format.format(new Date()),
						preferredStyle.getText().toString(), notComfort.getText().toString(),
						ooo.getString("objectId"));

				uploadToOriginal = 0;
				
				finish();
				}catch (Error error)
			{
				error.printStackTrace();

			}
			catch (Exception e) {
				// TODO: handle exception
			}}
		
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
