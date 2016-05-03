package com.stylist;

import com.constants.BaseActivity;
import com.parse.ParseUser;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;

public class SplashFirstActivity extends BaseActivity {
	int SPLASH_TIME = 2000;// 4000
	 Bitmap bitmap;
	    ProgressDialog pDialog;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_splash_first);
		
		new Handler().postDelayed(new Runnable() {

			@Override
			public void run() {
				gotoActivityWithfinish1(HomeActivity.class, null);
			}
		}, SPLASH_TIME);

	}

	private void gotoActivityWithfinish1(Class<?> classActivity, Bundle bundle) {
		Intent intent = new Intent(context, classActivity);
		if (bundle != null)
			intent.putExtra("android.intent.extra.INTENT", bundle);
		context.startActivity(intent);
		context.finish();
		context.overridePendingTransition(R.anim.animation_enter_from_right, R.anim.animation_leave_out_to_left);
	}
}
