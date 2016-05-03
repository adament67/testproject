package com.stylist;

import android.app.Application;
import android.graphics.Bitmap;
import android.support.multidex.MultiDexApplication;

import com.facebook.FacebookSdk;

import com.parse.Parse;
import com.parse.ParseFacebookUtils;
import com.parse.ParseInstallation;
import com.parse.PushService;

public class ParseApplication extends MultiDexApplication {


	Bitmap profileImage;

	public Bitmap getProfileImage() {
		return profileImage;
	}

	public void setProfileImage(Bitmap profileImage) {
		this.profileImage = profileImage;
	}

	@SuppressWarnings("deprecation")
	@Override
	public void onCreate() {
		super.onCreate();
		Parse.enableLocalDatastore(this);
		FacebookSdk.sdkInitialize(this);
		Parse.initialize(this, "8so6ZXrPYW1dNBUYXxv2v0nvskgSbBaCrMbEYxK3","QE8xu9efxkCi8J0mgVF8bYARCDplX6fhPBQbz7qn");
		ParseInstallation.getCurrentInstallation().saveInBackground();
		ParseFacebookUtils.initialize(this);
		//.setLogEnabled(false);
		//.init(this, "6RB98QDV2YRGJQWQRVN3");
		//  PushService.setDefaultPushCallback(this, SplashFirstActivity.class);
	}
}
