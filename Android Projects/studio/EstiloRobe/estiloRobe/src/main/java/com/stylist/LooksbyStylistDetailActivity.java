package com.stylist;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Dialog;
import android.content.ContentValues;
import android.content.Intent;
import android.content.pm.ResolveInfo;
import android.graphics.Bitmap;
import android.graphics.Matrix;
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
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.RelativeLayout.LayoutParams;

import com.constants.AsyncTaskAppWithoutArray;
import com.constants.BaseActivity;
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

import com.stylist.askstylist.AskStylistModelClass;

public class LooksbyStylistDetailActivity extends BaseActivity {
	RelativeLayout saveRelLayout;
	CustomTextView headerName, ocassionText, nameText, /* saveText, */
			commentsText;
	LinearLayout backButtonLayout, shareImageLayout;
	ImageView itemImage/* , favouriteImagen, delImagen */;
	ListView myLookBookItemListView;
	WebImageLoader mWebImageLoader;
	int pos = 0;
	Boolean isFav = false;
	LocalDatabaseHandler dbHelper;
	ArrayList<AskStylistModelClass> askStylistItem = new ArrayList<AskStylistModelClass>();
	WebImageLoader webImgLoader = new WebImageLoader(context);
public static LooksbyStylistDetailActivity llooksbyStylistDetailActivity;
	public void onStart() {
		super.onStart();
		//.onStartSession(this,
				//getResources().getString(R.string.flurryKey));
	}

	public void onStop() {
		super.onStop();
		//.onEndSession(this);
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_looksby_stylist_detail);
		setHeading("LOOK BOOK ITEM");
		backClick();
		llooksbyStylistDetailActivity=this;
		shareImageLayout = (LinearLayout) findViewById(R.id.shareImageLayout);
		ocassionText = (CustomTextView) findViewById(R.id.ocassionText);
		nameText = (CustomTextView) findViewById(R.id.lookbookNameText);
		commentsText = (CustomTextView) findViewById(R.id.commentsText);
		itemImage = (ImageView) findViewById(R.id.itemImage);

		pos = getIntent().getIntExtra("position", 0);
		mWebImageLoader = new WebImageLoader(context);

		dbHelper = new LocalDatabaseHandler(context);	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		getStylistResponse();
	}

	private void getStylistResponse() {
		Map<String, Object> params1 = new HashMap<String, Object>();

		new AsyncTaskAppWithoutArray(true, params1,
				LooksbyStylistDetailActivity.this, context,
				"getStylistResponse");
	}

	@Override
	public void AsynctaskResultwithoutArray(String arr, String url,
			Map<String, Object> par) {
		// TODO Auto-generated method stub
		super.AsynctaskResultwithoutArray(arr, url, par);
		System.out.println("arr==========" + arr);

		if (url.equals("getStylistResponse"))
			askStylistItem.clear();

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
