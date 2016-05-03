package com.stylist;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.constants.BaseActivity;
import com.parse.ParseUser;

public class ReferAppActivity extends BaseActivity {
com.constants.CustomTextView refer,shareText;
LinearLayout singleButton,doubleButton;
public static ReferAppActivity rReferAppActivity;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_refer_app);
		setHeading("REFER & EARN");
		refer=(com.constants.CustomTextView)findViewById(R.id.refer);
		refer.setText(ParseUser.getCurrentUser().getString("UserPromoCode"));
		backClick();
		singleButton = (LinearLayout) findViewById(R.id.single_button);
		doubleButton = (LinearLayout) findViewById(R.id.doubleButton);
		singleButton.setVisibility(View.VISIBLE);
		doubleButton.setVisibility(View.GONE);
		shareText=(com.constants.CustomTextView)findViewById(R.id.bottomText);
		shareText.setText("Share & Earn 5 tokens");
		shareText.setOnClickListener(this);
		rReferAppActivity=this;
	}

	
	@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			super.onClick(v);
			switch (v.getId()) {
			case R.id.bottomText:
				setHeading("Share & Earn");
				try {
					Intent in = new Intent(Intent.ACTION_SEND);
					in.setType("text/plain");
					in.putExtra(Intent.EXTRA_SUBJECT, "EstiloRobe");
					String sAux = "\nI have five token. Sign Up with my code" +ParseUser.getCurrentUser().getString("UserPromoCode")+" to avail the token!"+ "\n\n";
					sAux = sAux + "https://play.google.com/store/apps/details?id="
							+ context.getPackageName() + " \n\n";
					in.putExtra(Intent.EXTRA_TEXT, sAux);
					context.startActivity(Intent.createChooser(in, "Start inviting friends"));
				} catch (Exception e) {
					e.printStackTrace();
				}
				break;

			default:
				break;
			}
		}
}
