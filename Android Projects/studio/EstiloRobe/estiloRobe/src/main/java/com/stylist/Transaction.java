package com.stylist;

import android.os.Bundle;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.constants.BaseActivity;

public class Transaction extends BaseActivity {
	LinearLayout singleButton, doubleButton;
    TextView syncText;
    public static Transaction tTransaction;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.transaction);
		backClick();
		setHeading("History");
		tTransaction=this;
	}
}
