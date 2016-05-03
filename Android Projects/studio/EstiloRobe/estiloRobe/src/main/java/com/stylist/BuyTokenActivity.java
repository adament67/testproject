package com.stylist;

import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.LinearLayout;

import com.constants.BaseActivity;

public class BuyTokenActivity extends BaseActivity {
	com.constants.CustomTextView editText_amountBet, two_token, five_token,
			ten_token,addMoney;
LinearLayout singleButton,doubleButton;
public static BuyTokenActivity buyTokenActivity;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_buy_token);
		backClick();
		setHeading("Buy Token");
		editText_amountBet = (com.constants.CustomTextView) findViewById(R.id.editText_amountBet);
		two_token = (com.constants.CustomTextView) findViewById(R.id.two_token);
		five_token = (com.constants.CustomTextView) findViewById(R.id.five_token);
		ten_token = (com.constants.CustomTextView) findViewById(R.id.ten_token);
		singleButton = (LinearLayout) findViewById(R.id.single_button);
		doubleButton = (LinearLayout) findViewById(R.id.doubleButton);
		singleButton.setVisibility(View.VISIBLE);
		doubleButton.setVisibility(View.GONE);
		addMoney=(com.constants.CustomTextView)findViewById(R.id.bottomText);
		addMoney.setText("Proceed to earn"+editText_amountBet.getText().toString());
		
		buyTokenActivity=this;
		two_token.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				editText_amountBet.setText("100 "+"\u20B9");
				addMoney.setText("Proceed to pay "+editText_amountBet.getText().toString());

			}
		});

		five_token.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				editText_amountBet.setText("200 "+"\u20B9");
				addMoney.setText("Proceed to pay "+editText_amountBet.getText().toString());

			}
		});
		ten_token.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				editText_amountBet.setText("300 "+"\u20B9");
				addMoney.setText("Proceed to pay "+editText_amountBet.getText().toString());

			}
		});
		addMoney.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				//showToastS("PayU integration is in progress.");				
              	  gotoActivityWithfinish(PayUMoneyActivity.class, null);
			}
		});
		
	}

}
