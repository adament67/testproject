package com.stylist.askstylist;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Toast;

import com.constants.AsyncTaskAppWithoutArray;
import com.constants.BaseActivity;
import com.constants.Constants;
import com.constants.CustomTextView;
import com.constants.LocalDatabaseHandler;
import com.constants.Utils;

import com.parse.ParseUser;
//import com.stylist.BackServiceForCloset;
import com.stylist.R;

public class AskStylistRequestListActivity extends BaseActivity implements AskStylistActivity.OnServiceStartListener {
	ListView requestList;
	ImageView nextOrDel;
	LinearLayout nextOrDelButton;
	ArrayList<String> occasion = new ArrayList<String>();
	ArrayList<String> date = new ArrayList<String>();
	ArrayList<String> comments = new ArrayList<String>();
	ArrayList<String> isStylistResponsed = new ArrayList<String>();
	ArrayList<AskStylistModelClass> askStylistItem = new ArrayList<AskStylistModelClass>();
	LocalDatabaseHandler dbHelper;
	CustomTextView dotted_textView, addnew_textView;
	LinearLayout dotted_linLayout;
	public static AskStylistRequestListActivity aAskStylistRequestListActivity;
LinearLayout syncingInProgressLayout;
	public void onStart() {
		super.onStart();
		//.onStartSession(this, getResources().getString(R.string.flurryKey));
	}

	public void onStop() {
		super.onStop();
		//.onEndSession(this);
	}


	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_ask_stylist_request_list);
		requestList = (ListView) findViewById(R.id.requestList);
		setHeading("MY REQUEST");
		backClick();
		Utils.checkNetworkConnection(this);
		syncingInProgressLayout=(LinearLayout)findViewById(R.id.syncingInProgressLayout);
		syncingInProgressLayout.setVisibility(View.GONE);
		addnew_textView = (CustomTextView) findViewById(R.id.addnew_textView);
		addnew_textView.setOnClickListener(this);
		nextOrDelButton = (LinearLayout) findViewById(R.id.nextOrDelButton);
		nextOrDel = (ImageView) findViewById(R.id.nextOrDel);
		nextOrDelButton.setVisibility(View.VISIBLE);
		nextOrDelButton.setOnClickListener(this);
		nextOrDel.setImageResource(R.drawable.add);
		dotted_textView = (CustomTextView) findViewById(R.id.dotted_textView);
		aAskStylistRequestListActivity = this;
		dotted_linLayout = (LinearLayout) findViewById(R.id.dotted_linLayout);
		dbHelper = new LocalDatabaseHandler(context);
	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		syncingInProgressLayout.setVisibility(View.GONE);
		nextOrDelButton.setVisibility(View.VISIBLE);
Utils.write(" askstylist on resume");
		getStylistResponse();
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
			case R.id.nextOrDelButton:
				goToActivity(AskStylistActivity.class, null);
				break;
			case R.id.addnew_textView:
				goToActivity(AskStylistActivity.class, null);
				break;
			default:
				break;
		}
	}

	private void getStylistResponse() {
		askStylistItem.clear();
		Cursor cStylistResponse = dbHelper.returnAllAskStylist(ParseUser.getCurrentUser().getObjectId());
		if (cStylistResponse.moveToFirst()) {
			do {
				String isDeleted = cStylistResponse
						.getString(cStylistResponse.getColumnIndex(LocalDatabaseHandler.IS_DELETED));
				if (isDeleted.equalsIgnoreCase("false")) {
					askStylistItem.add(new AskStylistModelClass(
							cStylistResponse.getInt(cStylistResponse.getColumnIndex(LocalDatabaseHandler.QUERY_ID)),
							cStylistResponse.getString(cStylistResponse.getColumnIndex(LocalDatabaseHandler.OCASSION)),
							cStylistResponse.getString(cStylistResponse.getColumnIndex(LocalDatabaseHandler.COMMENTS)),
							cStylistResponse
									.getString(cStylistResponse.getColumnIndex(LocalDatabaseHandler.REQUEST_DATE)),
							cStylistResponse.getString(
									cStylistResponse.getColumnIndex(LocalDatabaseHandler.IS_STYLIST_RESPONSED)),
							cStylistResponse
									.getString(cStylistResponse.getColumnIndex(LocalDatabaseHandler.IS_STYLIST_LIKED)),
							cStylistResponse
									.getString(cStylistResponse.getColumnIndex(LocalDatabaseHandler.STYLIST_COMMENTS)),
							cStylistResponse.getString(cStylistResponse.getColumnIndex(LocalDatabaseHandler.PHOTOURL)),
							cStylistResponse.getString(cStylistResponse.getColumnIndex("parseQueryId"))));
Utils.write("ask stylist reequest list ========ocassion==="+cStylistResponse.getString(cStylistResponse.getColumnIndex(LocalDatabaseHandler.OCASSION)));
				}
			} while (cStylistResponse.moveToNext());
		}
		if (askStylistItem.size() <= 0) {
			dotted_linLayout.setVisibility(View.VISIBLE);
			Utils.write(" askstylist <=on resume");
		}
		else {
			dotted_linLayout.setVisibility(View.GONE);
			requestList.setAdapter(new StylistRequestListAdapter());
			Utils.write(" askstylist >=on resume");
		}

	}




	Bundle data;

	@Override
	public void onStartService(final Bundle data) {

		this.data = data;
		if(Utils.checkNetworkConnection(this)) {
			if (getPrefrenceBool(Constants.isUpdated) == true) {
				if (!getPrefrenceBool(Constants.isSyncing)) {
					Utils.createSimpleDialog(context, "Alert",
							" Your query has been saved. You have item on your phone which have not been synced to Server. In order to send query you should sync.",
							"Sync", "Cancel", new Utils.Method() {
								@Override
								public void execute() {
									// TODO Auto-generated method stub


											long lng = dbHelper.insertAskStylistTemp(ParseUser.getCurrentUser().getObjectId(),
													data.getString("ocassion"), data.getString("comments"), data.getString("date"),
													data.getString("preferredStyle"), data.getString("unComfortStyle"),
													" ");



								startSyncservice(AskStylistRequestListActivity.this,1);


								}
							}, new Utils.Method() {
								@Override
								public void execute() {
									// TODO Auto-generated method stub
									showToastS("your query is not sent");
								}
							});
				} else {
                        Utils.write(" ask stylist is syncing true======="+data.getString("ocassion"));
					long lng = dbHelper.insertAskStylistTemp(ParseUser.getCurrentUser().getObjectId(),
							data.getString("ocassion"), data.getString("comments"), data.getString("date"),
							data.getString("preferredStyle"), data.getString("unComfortStyle"),
							" ");

				}
			}
			else
			{

				uploadAskStylistQuery();

			}
		}
		else
		{
			showToastS("Query can not be asked offline!");
		}
	}




	public void uploadAskStylistQuery() {
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("UserId", ParseUser.getCurrentUser().getObjectId());
		params.put("occasion", data.getString("ocassion"));
		params.put("comments", data.getString("comments"));
		params.put("preferredStyle",data.getString("preferredStyle"));
		params.put("unComfortStyle", data.getString("unComfortStyle"));
		new AsyncTaskAppWithoutArray(true, params, callBackListnerwithoutArray, context, "insertAskStylistQuery");
	}

	@Override
	public void AsynctaskResultwithoutArray(String arr, String url, Map<String, Object> par) {
		// TODO Auto-generated method stub
		super.AsynctaskResultwithoutArray(arr, url, par);
		if (url.equals("insertAskStylistQuery")) {
			try {
				Utils.write("ask stylist===========insertAskStylistQuery");

				final JSONObject ooo;
				ooo = new JSONObject(arr);
				long lng = dbHelper.insertAskStylist(ParseUser.getCurrentUser().getObjectId(),
						data.getString("ocassion"),data.getString("comments"), data.getString("date"),
						data.getString("preferredStyle"), data.getString("unComfortStyle"),
						ooo.getString("objectId"));

                     getStylistResponse();
			}catch (Error error)
			{
				error.printStackTrace();

			}
			catch (Exception e) {
				// TODO: handle exception
				e.printStackTrace();
			}}
		else
		{
			Utils.write("ask stylist===========not in insertAskStylistQuery");

		}

	}
	public class StylistRequestListAdapter extends BaseAdapter {
		private LayoutInflater mInflater;

		@Override
		public int getCount() {
			return askStylistItem.size();
		}

		@Override
		public Object getItem(int position) {
			return askStylistItem.get(position);
		}

		@Override
		public long getItemId(int position) {
			return position;
		}

		@Override
		public View getView(final int position, View convertView, ViewGroup parent) {
			ViewHolder holder;
			if (convertView == null) {
				holder = new ViewHolder();
				convertView = View.inflate(context, R.layout.request_to_stylist, null);
				holder.check = (ImageView) convertView.findViewById(R.id.check);
				holder.occasion = (CustomTextView) convertView.findViewById(R.id.occasion);
				holder.date = (CustomTextView) convertView.findViewById(R.id.date);
				holder.comments = (CustomTextView) convertView.findViewById(R.id.comments);
				holder.response_status = (CustomTextView) convertView.findViewById(R.id.response_status);
				convertView.setTag(holder);
			} else {
				holder = (ViewHolder) convertView.getTag();
			}

			convertView.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View arg0) {
					// TODO Auto-generated method stub
					if (askStylistItem.get(position).hasStylistResponsed.equalsIgnoreCase("true")) {						
						Intent intent = new Intent(context, StylistResponseDetailActivity.class);
						intent.putExtra("queryId", askStylistItem.get(position).parseQueryId);
						intent.putExtra("fromNotificationInt", 0);
						startActivity(intent);
					}
				}
			});
			Utils.write("ask stylist request adapter========" + askStylistItem.get(position).occasion);
			holder.occasion.setText(askStylistItem.get(position).occasion);
			SimpleDateFormat outformat = new SimpleDateFormat("dd MMM yyyy");
			SimpleDateFormat informat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
			try {
				if (askStylistItem.get(position).queryRequestDate != null
						&& askStylistItem.get(position).queryRequestDate != "")
					holder.date
							.setText(outformat.format(informat.parse(askStylistItem.get(position).queryRequestDate)));
			} catch (Error error)
			{
				error.printStackTrace();

			}catch (ParseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			holder.comments.setText(askStylistItem.get(position).comments);
			if (askStylistItem.get(position).hasStylistResponsed.equalsIgnoreCase("false")) {
				holder.check.setImageResource(R.drawable.not_responsed_buttun);
				holder.response_status.setTextColor(getResources().getColor(R.color.blackColor));
				holder.response_status.setText("Pending");

			} else {
				holder.check.setImageResource(R.drawable.correct_green);
				holder.response_status.setTextColor(getResources().getColor(R.color.green));
				holder.response_status.setText("Answered");
			}
			return convertView;
		}

		private class ViewHolder {
			ImageView check;
			CustomTextView occasion, date, comments, response_status;

		}
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
