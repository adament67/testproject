package com.stylist;

import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.constants.BaseActivity;
import com.constants.Utils;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Iterator;
import java.util.Locale;
import java.util.Set;

public class SelectorStyleEditor extends BaseActivity {
	RelativeLayout uploadRelativeLayout;
	int fromEditor;
	public static SelectorStyleEditor sSelector;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.selector);
		fromEditor=getIntent().getIntExtra("fromEditor",2);
		TextView wardrobe = (TextView) findViewById(R.id.wardrobe);
		TextView wishlist = (TextView) findViewById(R.id.wishlist);
		TextView back = (TextView) findViewById(R.id.back);
		uploadRelativeLayout=(RelativeLayout)findViewById(R.id.uploadRelativeLayout);
		wardrobe.setOnClickListener(this);
		wishlist.setOnClickListener(this);
		back.setOnClickListener(this);
		sSelector=this;
		System.out.println("fromEditor==========="+fromEditor);
		if(fromEditor==1)
		{
			wardrobe.setText("Browse My Wardrobe");
			wishlist.setText("Browse My Wishlist");


		}
		else
		{
			wardrobe.setText("Upload From Gallery");
			wishlist.setText("Take a Photo");
		}
		uploadRelativeLayout.setOnTouchListener(new View.OnTouchListener() {

			@Override
			public boolean onTouch(View v, MotionEvent event) {
				// TODO Auto-generated method stub
				setResult(RESULT_CANCELED,
						new Intent().putExtra("intent_data", "0"));
				finish();
				overridePendingTransition(R.anim.slide_in_up, R.anim.slide_out_down);
				return false;
			}
		});
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
			case R.id.wardrobe:
				setResult(RESULT_OK,
						new Intent().putExtra("intent_data", "1"));
				finish();
				overridePendingTransition(R.anim.slide_in_up, R.anim.slide_out_down);
				break;

			case R.id.wishlist:
				setResult(RESULT_OK,
						new Intent().putExtra("intent_data", "2"));
				finish();
				overridePendingTransition(R.anim.slide_in_up, R.anim.slide_out_down);
				break;
			case R.id.back:
				setResult(RESULT_CANCELED,
						new Intent().putExtra("intent_data", "0"));
				finish();
				overridePendingTransition(R.anim.slide_in_up, R.anim.slide_out_down);
				break;
			default:
				break;
		}
	}


	@Override
	public void onBackPressed() {
		// TODO Auto-generated method stub
		super.onBackPressed();
		setResult(RESULT_CANCELED,
				new Intent().putExtra("intent_data", "0"));
		finish();
		overridePendingTransition(R.anim.slide_in_up, R.anim.slide_out_down);
	}

}