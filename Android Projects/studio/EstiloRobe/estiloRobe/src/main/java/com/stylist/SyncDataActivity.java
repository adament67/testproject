package com.stylist;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.constants.BaseActivity;
import com.constants.Constants;
import com.constants.CustomTextView;
import com.constants.Utils;

public class SyncDataActivity extends BaseActivity {
	LinearLayout singleButton, doubleButton;
    CustomTextView syncText,dotted_textView;
    public static SyncDataActivity sSyncDataActivity;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_sync_data);
		backClick();
		setHeading("Sync");
		sSyncDataActivity=this;
		singleButton = (LinearLayout) findViewById(R.id.single_button);
		doubleButton = (LinearLayout) findViewById(R.id.doubleButton);
		singleButton.setVisibility(View.VISIBLE);
		doubleButton.setVisibility(View.GONE);
		dotted_textView=(CustomTextView)findViewById(R.id.dotted_textView);
		syncText=(CustomTextView)findViewById(R.id.bottomText);
		syncText.setText("Sync your data");
		if(getPrefrenceBool(Constants.isUpdated)==false)
		{
			dotted_textView.setText("No data is updated");
		}
		else
		{
			dotted_textView.setText(getResources().getString(R.string.sync_text));
		}
		syncText.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				//syncText.setClickable(false);

				if (Utils.checkNetworkConnection(SyncDataActivity.this)){

					if(getPrefrenceBool(Constants.isSyncing)!=null && !getPrefrenceBool(Constants.isSyncing))
					{

					startSyncservice(SyncDataActivity.this, 0);
						showToastS("Service Started");
						finish();
					}
					else{
						showToastS("Syncing is in progress. we will notify you on completion.");
					}

				}else{
					showToastS("Check your network connection!");

				}

			}
		});
		
	}
	
	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		syncText.setClickable(true);

	}
}
