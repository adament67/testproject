package com.stylist;

import java.io.File;

import com.constants.BaseActivity;
import com.constants.Constants;
import com.constants.LocalDatabaseHandler;
import com.constants.Utils;
import com.google.android.gms.gcm.GoogleCloudMessaging;
import com.parse.ParseUser;

import android.annotation.SuppressLint;
import android.app.ActionBar.LayoutParams;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Point;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

public class SplashScreenActivity extends BaseActivity {
	Button signInButton, registerButton;
	public static SplashScreenActivity act;
	Boolean is_login = false;
	GoogleCloudMessaging gcm;
	String regId;
	int screenWidth, screenHeight;
	FrameLayout backRelLayout;
	LinearLayout linearSplashCircleLogo;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_splash_screen);
		context = this;
		getScreenDimention(this);
		LocalDatabaseHandler. mdbPath = Environment.getExternalStorageDirectory().getAbsolutePath()+"/EstiloRobe/db/";
		File f = new File(LocalDatabaseHandler. mdbPath);
		if(!f.exists()){
			f.mkdirs();
		}
		else
		{
			Constants.IS_DATABASE_ALREADY_EXISTS=true;
			Utils.write("file already exists");
		}
		if (ParseUser.getCurrentUser() != null)
			gotoActivityWithfinish1(SplashFirstActivity.class, null);
		signInButton = (Button) findViewById(R.id.signInButton);
		registerButton = (Button) findViewById(R.id.registerButton);
		signInButton.setOnClickListener(this);
		registerButton.setOnClickListener(this);

		act = this;
		//LocalDatabaseHandler handler = new LocalDatabaseHandler(context);
		
		dbHelper = new LocalDatabaseHandler(this);
	}

	LocalDatabaseHandler dbHelper;




	private void gotoActivityWithfinish1(Class<?> classActivity, Bundle bundle) {
		Intent intent = new Intent(context, classActivity);
		if (bundle != null)
			intent.putExtra("android.intent.extra.INTENT", bundle);
		context.startActivity(intent);
		context.finish();
		context.overridePendingTransition(R.anim.animation_enter_from_right, R.anim.animation_leave_out_to_left);
	}





	@SuppressLint("NewApi")
	private void getScreenDimention(Context context) {

		if (Build.VERSION.SDK_INT >= 7) {
			Point size = new Point();
			try {
				((Activity) context).getWindowManager().getDefaultDisplay()
						.getRealSize(size);
				screenWidth = size.x;
				screenHeight = size.y;
			} catch (NoSuchMethodError e) {
			}

		} else {
			DisplayMetrics metrics = new DisplayMetrics();
			((Activity) context).getWindowManager().getDefaultDisplay()
					.getMetrics(metrics);
			screenWidth = metrics.widthPixels;
			screenHeight = metrics.heightPixels;
		}
System.out.println("screenWidth========="+screenWidth);
System.out.println("screenHeight========="+screenHeight);
	}
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.signInButton:
			goToActivity(LoginActivity.class, null);
			break;

		case R.id.registerButton:
			goToActivity(SignupActivity.class, null);
			break;
		default:
			break;
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
