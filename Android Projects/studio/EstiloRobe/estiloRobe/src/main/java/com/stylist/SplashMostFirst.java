package com.stylist;

import com.constants.BaseActivity;
import com.constants.Constants;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

public class SplashMostFirst extends BaseActivity {
	int SPLASH_TIME = 2000;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_splash_most_first);
		setPrefrenceBool(Constants.isSyncing, false);

		new Handler().postDelayed(new Runnable() {
			@Override
			public void run() {
				gotoActivityWithfinish1(SplashScreenActivity.class, null);
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
