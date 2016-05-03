package com.stylist.planner;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONObject;

import android.database.Cursor;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;

import com.constants.AsyncTaskAppWithoutArray;
import com.constants.BaseActivity;
import com.constants.CustomTextView;
import com.constants.LocalDatabaseHandler;
import com.constants.Utils;
import com.facebook.CallbackManager;
import com.facebook.login.LoginManager;

import com.stylist.HomeActivity;
import com.stylist.R;
import com.stylist.planner.PlannerDetailList.ListItemAdapterNew;

public class PlannerDetailItem extends BaseActivity {
	RelativeLayout saveRelLayout;
	CustomTextView headerName, eventdate, title, CreatedDate;
	LinearLayout backButtonLayout, shareImageLayout;
	ImageView itemImage;
	ListView myLookBookItemListView;
	String plannerId, eventTime;
	String lookId;
	boolean notification;
	public static PlannerDetailItem pPlannerDetailItem;
	ImageView nextOrDel;
	LinearLayout nextOrDelButton;
	public void onStart() {
		super.onStart();
		//.onStartSession(this,
			//	getResources().getString(R.string.flurryKey));
	}

	public void onStop() {
		super.onStop();
		//.onEndSession(this);
	}

	String createdAt, planDate;
	LocalDatabaseHandler dbHelper;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_planner_detail_item);
		setHeading("Planner Detail");
		backClick();
		pPlannerDetailItem=this;
		nextOrDelButton = (LinearLayout) findViewById(R.id.nextOrDelButton);
		nextOrDel = (ImageView) findViewById(R.id.nextOrDel);
		nextOrDelButton.setVisibility(View.VISIBLE);
		nextOrDelButton.setOnClickListener(this);
		nextOrDel.setImageResource(R.drawable.delete_cat_white);

		lookId = getIntent().getStringExtra("LookId");
		plannerId = getIntent().getStringExtra("PlannerId");
		notification = getIntent().getBooleanExtra("notification", false);
		createdAt = getIntent().getStringExtra("createdAt");
		planDate = getIntent().getStringExtra("planDate");
		eventTime = getIntent().getStringExtra("eventTime");
		if(plannerId==null)
		{
			 Bundle getBundle = null;
			getBundle = this.getIntent().getBundleExtra("android.intent.extra.INTENT");//getIntent().getExtras();

			Utils.write(">>>>>>  " + getBundle);
		 lookId = getBundle.getString("LookId");
			plannerId = getBundle.getString("PlannerId");
			eventTime = getBundle.getString("eventTime");
			notification = getBundle.getBoolean("notification", false);
			System.out.println("lookId ffffeee==  " + lookId);
			System.out.println("plannerId ffffeee==  " + plannerId);


		}



		System.out.println("eventTime===="+eventTime);
		/*nextOrDel.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				deletePlannerList();
			}
		});*/
		System.out.println("notification   " + notification);
		dbHelper = new LocalDatabaseHandler(context);
		shareImageLayout = (LinearLayout) findViewById(R.id.shareImageLayout);
		eventdate = (CustomTextView) findViewById(R.id.ocassionText);
		title = (CustomTextView) findViewById(R.id.lookbookNameText);
		CreatedDate = (CustomTextView) findViewById(R.id.commentsText);
		itemImage = (ImageView) findViewById(R.id.itemImage);

		setListData();

	}


	private void setListData() {
		Cursor clook = dbHelper.returnLookbook(Integer.parseInt(lookId));
		System.out.println("plannerId ffff==  " + plannerId);

		Cursor cplanner = dbHelper.returnPlanner(Integer.parseInt(plannerId));

		System.out.println("lookId ==  " + lookId);
		System.out.println("plannerId ==  " + plannerId);
		System.out.println("plannerId ==  " + Integer.parseInt(plannerId));
		System.out.println("clook count  == " + clook.getCount());
		System.out.println("cplanne countr==   " + cplanner.getCount());
		if (clook.moveToFirst()) {
			do {

				itemImage
						.setImageBitmap(BitmapFactory.decodeFile(clook.getString(clook
								.getColumnIndex(LocalDatabaseHandler.IMAGE_PATH))));
			} while (clook.moveToNext());
		}
		if (cplanner.moveToFirst()) {

			try {

				do {
					title.setText(cplanner.getString(cplanner
							.getColumnIndex(LocalDatabaseHandler.OCASSION)));
					System.out.println("occasion ==  " + cplanner.getString(cplanner
							.getColumnIndex(LocalDatabaseHandler.OCASSION)));
					if(planDate==null)
					{
						String[] PLAN_DATEarr =cplanner.getString(cplanner.getColumnIndex(LocalDatabaseHandler.PLAN_DATE))
								.split("-");
						Log.e("time in planner det==",cplanner.getString(cplanner
								.getColumnIndex(LocalDatabaseHandler.PLAN_TIME)));
						eventdate.setText(formatDateto(Integer.parseInt(PLAN_DATEarr[2]), Integer.parseInt(PLAN_DATEarr[0]),
								Integer.parseInt(PLAN_DATEarr[1]))+ "      " + cplanner.getString(cplanner
								.getColumnIndex(LocalDatabaseHandler.PLAN_TIME)));
						System.out.println("plan created at"+formatDateto(Integer.parseInt(PLAN_DATEarr[2]), Integer.parseInt(PLAN_DATEarr[0]),
								Integer.parseInt(PLAN_DATEarr[1])));
						CreatedDate.setText(formatDateto(Integer.parseInt(PLAN_DATEarr[2]), Integer.parseInt(PLAN_DATEarr[0]),
								Integer.parseInt(PLAN_DATEarr[1])));
					}
					else
					{
						eventdate.setText(planDate + ", " + eventTime);
						CreatedDate.setText(createdAt);
					}

				} while (clook.moveToNext());

			} catch (Exception e) {
				// TODO: handle exception
			}
		}

	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		super.onClick(v);
		switch (v.getId()) {
		/*case R.id.delImagen:
			Utils.createSimpleDialog(context, "DELETE",
					"Are you sure to Delete This plan?", "OK", "OK",
					new Utils.Method() {
						public void execute() {
							handler.setDeleteplanner(Integer
									.parseInt(plannerId));
							finish();
						}
					}).show();

			break;*/
		case R.id.nextOrDelButton:
			deletePlannerList();
			break;
	
		default:
			break;
		}
	}

	private CallbackManager callbackManager;
	private LoginManager manager;

	public void deletePlanner(String pos) {
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("plannerID", pos);
		new AsyncTaskAppWithoutArray(true, params, callBackListnerwithoutArray,
				context, "deletePlannerListItem");
	}

	String showImagePath;

	SimpleDateFormat outputFormat = new SimpleDateFormat("yyyy-MM-dd");
	SimpleDateFormat inputFormat = new SimpleDateFormat(
			"yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");

	@Override
	public void onBackPressed() {
		// TODO Auto-generated method stub
		super.onBackPressed();
		if (notification) {
			checkAndCloseActivity(PlannerDetailList.act);
			checkAndCloseActivity(PlannerActivity.act);
			checkAndCloseActivity(HomeActivity.act);
		}
		finish();
	}
	public void deletePlannerList() {
		Utils.createSimpleDialog(context, "Alert", "Are You sure to delete plan ?", "Yes", "Cancel",
				new Utils.Method() {
					public void execute() {
						
						dbHelper.setDeleteplanner(Integer.parseInt(plannerId));
					finish();
					}
				}).show();

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