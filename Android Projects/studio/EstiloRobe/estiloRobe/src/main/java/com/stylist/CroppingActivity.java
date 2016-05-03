package com.stylist;

import com.constants.BaseActivity;
import com.crop.FreeHandCropView;

import com.stylist.wardrobe.UploadOptionWardrobeActivity;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.net.Uri;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;
import android.widget.LinearLayout;

public class CroppingActivity extends Activity {
	//GlobalVariable globalVariable;	
	ImageView cropView;
	String imgPath;
	public static CroppingActivity cCroppingActivity;
	int fromWish=0,rotateCount=0;
	String categoryName,categoryId;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		
	}		

	
	public void onStart() {
		super.onStart();
		//.onStartSession(this,
			//	getResources().getString(R.string.flurryKey));
	}

	public void onStop() {
		super.onStop();
		//.onEndSession(this);
	}
	

    @Override
    protected void onResume() {
        super.onResume();
        fromWish=getIntent().getIntExtra("fromWish", 0);	
    	imgPath=getIntent().getStringExtra("myPath");	
		categoryName=getIntent().getStringExtra("categoryName");
		categoryId=getIntent().getStringExtra("categoryId");
		rotateCount = getIntent().getIntExtra("rotateCount", 0);
		cCroppingActivity=this;
		System.out.println("in cropping fromWish==========" + fromWish);
        setContentView(new SomeView(CroppingActivity.this, imgPath, fromWish, categoryName, rotateCount, categoryId));
    }









    public void finishCroppingActivity() {
		// TODO Auto-generated method stub
    	
    	if (UploadOptionWardrobeActivity.act != null && !UploadOptionWardrobeActivity.act.isFinishing()) {
    		UploadOptionWardrobeActivity.act.finish();
		}
    	
    	finish();

	}
    
}
