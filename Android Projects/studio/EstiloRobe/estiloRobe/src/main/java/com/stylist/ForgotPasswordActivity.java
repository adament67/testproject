package com.stylist;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.constants.BaseActivity;
import com.constants.CustomEditText;
import com.constants.Utils;

import com.parse.ParseException;
import com.parse.ParseUser;
import com.parse.RequestPasswordResetCallback;

public class ForgotPasswordActivity extends BaseActivity {
	CustomEditText userNameEditText;
	Button loginButton;
	ProgressDialog dialog;
    LinearLayout linLayLog;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_forgot_password);
		userNameEditText = (CustomEditText) findViewById(R.id.userNameEditText);
		linLayLog=(LinearLayout)findViewById(R.id.linLayLog);
		loginButton = (Button) findViewById(R.id.loginButton);
		loginButton.setOnClickListener(this);
		linLayLog.setOnClickListener(this);
		Utils.setupUI(findViewById(R.id.relLayout1), this);
	}

	
/*	public void onStart() {
		super.onStart();
		//.onStartSession(this,
				getResources().getString(R.string.flurryKey));
	}

	public void onStop() {
		super.onStop();
		//.onEndSession(this);
	}
*/	
	
	

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		super.onClick(v);
		switch (v.getId()) {
		case R.id.loginButton:
             if(userNameEditText.getText().toString().equals(""))
             {
            	 showToastS("Please Enter Email");
             }
             else
             {
			final ProgressDialog dialog = showDialog("Sending email...");
			ParseUser.requestPasswordResetInBackground(userNameEditText
					.getText().toString(), new RequestPasswordResetCallback() {
				public void done(ParseException e) {
					if (e == null) {
						dialog.cancel();
						showToastS("Password reset link has been send to your email.");
						finish();

						// An email was successfully sent with reset
						// instructions.
					} else {
						dialog.cancel();
						showToastS(e.getMessage());
						// Something went wrong. Look at the
						// ParseException to see what's up.
					}
				}
			});
          
			userNameEditText.setText("");
			// showToastS("Password Has Been Sent To Your Email Successfully !");
			
             }
			break;
		case R.id.linLayLog:
			finish();
			overridePendingTransition(R.anim.exit_animation_enter_from_right, 
					  R.anim.exit_animation_leave_to_right); 
			break;
		default:
			break;
		}
	}



}
