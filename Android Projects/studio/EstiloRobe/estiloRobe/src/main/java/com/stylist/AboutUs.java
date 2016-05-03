package com.stylist;

import android.os.Bundle;
import android.widget.LinearLayout;

import com.constants.BaseActivity;

public class AboutUs extends BaseActivity {
	com.constants.CustomTextView refer,shareText;
	LinearLayout singleButton,doubleButton;
	public static AboutUs aAboutUs;
		@Override
		protected void onCreate(Bundle savedInstanceState) {
			super.onCreate(savedInstanceState);
			setContentView(R.layout.styleit_about);
			setHeading("About Us");
			backClick();
			aAboutUs=this;
		}

		
	
	}
