package com.stylist;

import java.util.Map;
import java.util.Random;

import android.app.ProgressDialog;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;

import com.constants.BaseActivity;
import com.constants.Constants;
import com.constants.GetDeviceTokenId;
import com.constants.Utils;
import com.constants.GetDeviceTokenId.AsynctaskListner;

import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseUser;
import com.parse.SignUpCallback;

public class SignupActivity extends BaseActivity implements AsynctaskListner {
	Button createAccountBut;
	EditText userNameEdit, passEdit, emailEdit, referralEditText;
	String usernametxt, passwordtxt, email/* , dob */;
	com.constants.CustomTextView loginAlready, referral;
	String message = "";
	 ProgressDialog dialog;
	AsynctaskListner callbacklistner;
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
		setContentView(R.layout.activity_signup);
		callbacklistner =SignupActivity.this;
		createAccountBut = (Button) findViewById(R.id.createAccount);
		userNameEdit = (EditText) findViewById(R.id.userSigEditText);
		passEdit = (EditText) findViewById(R.id.passSigEditText);
		emailEdit = (EditText) findViewById(R.id.emailEditText);

		loginAlready = (com.constants.CustomTextView) findViewById(R.id.loginAlready);
		userNameEdit.requestFocus();
		referral = (com.constants.CustomTextView) findViewById(R.id.referral);
		referralEditText = (EditText) findViewById(R.id.referralEditText);
		Utils.setupUI(findViewById(R.id.linLayout1), this);

		Typeface font = Typeface.createFromAsset(context.getAssets(),
				"fonts/lvnm.ttf");
		referral.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				referral.setVisibility(View.GONE);
				referralEditText.setVisibility(View.VISIBLE);
			}
		});

		loginAlready.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				gotoActivityWithfinish(LoginActivity.class, null);
			}
		});

		createAccountBut.setOnClickListener(new OnClickListener() {
			public void onClick(View arg0) {
				setPrefrenceBool(Constants.isUpdated, false);
				usernametxt = userNameEdit.getText().toString().trim();
				passwordtxt = passEdit.getText().toString().trim();
				email = emailEdit.getText().toString().trim();
				if (userNameEdit.getText().toString().trim().equals("")
						&& passEdit.getText().toString().trim().equals("")
						&& emailEdit.getText().toString().trim().equals("")) {
					showToastS("Please Enter All values");
				} else if (userNameEdit.getText().toString().trim().equals("")) {
					showToastS("Please Enter User Name");
				} else if (passEdit.getText().toString().trim().equals("")
						|| passEdit.getText().toString().trim().length() < 6) {
					showToastS("Please Enter Password of minimum six characters");
				} else if (emailEdit.getText().toString().trim().equals("")) {
					showToastS("Please Enter Email Id");
				} else if (checkNetworkConnection()) {
					    dialog = showDialog("Signing up...");
					String random_code = generateRandom();
					ParseUser user = new ParseUser();
					user.setUsername(usernametxt);
					user.setPassword(passwordtxt);
					user.setEmail(email);
					user.put("UserPromoCode", random_code);
					user.put("IsActive", true);

					user.signUpInBackground(new SignUpCallback() {
						public void done(ParseException e) {
							
							if (e == null) {
								System.out.println("signUpInBackground");
								
								new GetDeviceTokenId(context, callbacklistner).execute();
								
							} else {
								e.printStackTrace();
								showToastS(e.getMessage());
							}
							
							dialog.dismiss();
						}
					});
				} 
				else {
					showToastS(Constants.networkMsg);
				}
			}
		});
		
	}
	
	public void saveInstalledUser()
	{
		ParseObject obj = new ParseObject("UserToken");
		obj.put("TokenID",getPrefrence( Constants.googledeviceid));
		obj.put("UserID",ParseUser.getCurrentUser());
		obj.saveInBackground();
	}

	@Override
	public void AsynctaskResultwithoutArray(String arr, String url,
			Map<String, Object> par) {
		// TODO Auto-generated method stub
		super.AsynctaskResultwithoutArray(arr, url, par);
		System.out.println("arr   " + arr);

	}

	String generateRandom() {
		char[] alphNum = "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789".toCharArray();

		Random rnd = new Random();

		StringBuilder sb = new StringBuilder((1 + rnd.nextInt(9)));
		for (int i = 0; i < 6; i++)
			sb.append(alphNum[rnd.nextInt(alphNum.length)]);

		String id = sb.toString();

		System.out.println("random number==========" + id);
		return id;
	}	
	
	@Override
	public void AsynctaskResult(String result, boolean action) {
		// TODO Auto-generated method stub
		System.out.println("AsynctaskResult result    "+result);
		setPrefrence( Constants.googledeviceid,result);

		saveInstalledUser();
		if(dialog!=null)
		dialog.dismiss();
		showToastS("Account Created Successfully");
		gotoActivityWithfinish(HomeActivity.class, null);
		checkAndCloseActivity(LoginActivity.act);
		checkAndCloseActivity(SplashScreenActivity.act);
	}
}
