package com.stylist;

import com.constants.BaseActivity;

import android.app.Activity;
import android.app.ActionBar;
import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.os.Build;

public class PaymentSuccessActivity extends BaseActivity {
public static PaymentSuccessActivity pPaymentSuccessActivity;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_payment_success);
		setHeading("PayU Detail");	
		backClick();
		pPaymentSuccessActivity=this;
	}
}
