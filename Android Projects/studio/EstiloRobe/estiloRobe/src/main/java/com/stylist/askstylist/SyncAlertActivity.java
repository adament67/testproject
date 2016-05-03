package com.stylist.askstylist;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.json.JSONObject;

import com.constants.AsyncTaskAppWithoutArray;
import com.constants.BaseActivity;
import com.constants.Constants;
import com.constants.CustomTextView;
import com.constants.LocalDatabaseHandler;
import com.constants.Utils;
import com.parse.FunctionCallback;
import com.parse.ParseCloud;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseUser;
import com.parse.SaveCallback;
//import com.stylist.BackServiceForCloset;
import com.stylist.R;
import com.stylist.wardrobe.ClosetWardrobeDetail;

import android.app.ProgressDialog;
import android.content.Intent;
import android.database.Cursor;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;

public class SyncAlertActivity extends BaseActivity {
	Boolean isSynced=false,fromServiceHomeOpened;
	CustomTextView synctext,ok;
	String ocassion,comments,preferredStyle,notComfort;
	LocalDatabaseHandler dbHelper ;
	LinearLayout syncingInProgressLayout;
int items=0,total;
	ProgressDialog progressDialog;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_sync_alert);
		synctext=(CustomTextView)findViewById(R.id.synctext);
		ok=(CustomTextView)findViewById(R.id.ok);
		isSynced=getIntent().getBooleanExtra("isSynced", false);
		fromServiceHomeOpened=getIntent().getBooleanExtra("fromServiceHomeOpened", false);
		
		dbHelper = new LocalDatabaseHandler(context);


		if(fromServiceHomeOpened)
		{
			synctext.setText("Your data has been synced sucessfully.");

		}
		else {
			if (isSynced) {
				synctext.setText("Your query has been sent to the stylist.");

			} else {
				synctext.setText("Your query was not sent as the data syncing was not successful.");
			}
		}

		
		ok.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub

				if (fromServiceHomeOpened) {
					Utils.write("ask stylist fromServiceHomeOpened== "+fromServiceHomeOpened);
					fromServiceHomeOpened=false;

				}
				else {

					//online

					Utils.write("ask stylist else if fromQuery  fromServiceHomeOpened else ==" );
					progressDialog = showDialog("please wait");
					setPrefrenceBool(Constants.isUpdated, false);
					setPrefrenceBool(Constants.isSyncing, false);
					Cursor cAskStylistTemp = dbHelper.returnAllAskStylistTemp();
					Utils.write("ask stylist sync alert ok size====" + total);
					total = cAskStylistTemp.getCount();
					if (cAskStylistTemp.moveToFirst()) {

						do {

							Utils.write("cAskStylistTemp size=i===" + items);
							ocassion = cAskStylistTemp.getString(cAskStylistTemp.getColumnIndex(LocalDatabaseHandler.OCASSION));
							comments = cAskStylistTemp.getString(cAskStylistTemp.getColumnIndex(LocalDatabaseHandler.COMMENTS));
							preferredStyle = cAskStylistTemp.getString(cAskStylistTemp.getColumnIndex(LocalDatabaseHandler.PREFERRED_STYLE));
							notComfort = cAskStylistTemp.getString(cAskStylistTemp.getColumnIndex(LocalDatabaseHandler.NOT_INTERESTED_STYLE));

							HashMap<String, String> query = new HashMap<String, String>();

							query.put("UserId", ParseUser.getCurrentUser().getObjectId());
							query.put("occasion", ocassion);
							query.put("comments", comments);
							query.put("preferredStyle", preferredStyle);
							query.put("unComfortStyle", notComfort);

							dataSet.add(query);


							//uploadAskStylistQuery();

						} while (cAskStylistTemp.moveToNext());

						Log.v("60606060", dataSet.size()+"");

						uploadAskStylistQuery(dataSet.get(items));

					}

				}
			}
		});
	

	}


	ArrayList<HashMap<String, String>> dataSet = new ArrayList<HashMap<String, String>>();

	public void uploadAskStylistQuery(HashMap<String, String> query) {

		Map<String, Object> params = new HashMap<String, Object>();
		Utils.write("ocassion=======" + query.get("ocassion"));
		params.put("UserId", ParseUser.getCurrentUser().getObjectId());
		params.put("occasion",query.get("occasion"));
		params.put("comments", query.get("comments"));
		params.put("preferredStyle", query.get("preferredStyle"));
		params.put("unComfortStyle", query.get("unComfortStyle"));
		 System.out.println("ask stylist==============uploadAskStylistQuery==============");

		Iterator<Map.Entry<String, String>> i = query.entrySet().iterator();
		while(i.hasNext()){
			String key = i.next().getKey();
			System.out.println("6060======="+key+", "+query.get(key));
		}


		uploadQuery("insertAskStylistQuery", params );


	}

	@Override
	public void AsynctaskResultwithoutArray(String arr, String url, Map<String, Object> par) {
		// TODO Auto-generated method stub
		super.AsynctaskResultwithoutArray(arr, url, par);



	}

	
	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		
		if(dbHelper != null){
			
			dbHelper.close();
		}
	}



	private void uploadQuery(final String url, final Map<String, Object> bundleData){

		if (Utils.checkNetworkConnection(context)) {
			ParseCloud.callFunctionInBackground(url, bundleData,
					new FunctionCallback<String>() {
						@Override
						public void done(String res, ParseException e) {

							if (e == null) {
								try {

									//sucess

										try {

											items++;
											Log.v("60606===============", items+"");



											SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");

											final JSONObject ooo;
											ooo = new JSONObject(res);


											long lng = dbHelper.insertAskStylist(ParseUser.getCurrentUser().getObjectId(),
													(String)bundleData.get("occasion"), (String)bundleData.get("comments"), format.format(new Date()),
													(String)bundleData.get("preferredStyle"), (String)bundleData.get("unComfortStyle"),
													ooo.getString("objectId"));





											if(items<total){

												Log.v("60606=======continue", "sync query");
												uploadAskStylistQuery(dataSet.get(items));

											}else{

												Log.v("60606========finish", "sync query finsh");

												dbHelper.deleteTempAskStylist();
												progressDialog.dismiss();
												checkAndCloseActivity(AskStylistActivity.aAskStylistActivity);
												finish();


											}


										} catch (Error error)
										{
											error.printStackTrace();

										}catch (Exception exp) {
											// TODO: handle exception
											exp.printStackTrace();
											progressDialog.dismiss();
										}


								} catch (Error error)
								{
									error.printStackTrace();

								}
								catch (Exception e2) {
									e2.printStackTrace();

									progressDialog.dismiss();

								}
							} else {
								e.printStackTrace();

								progressDialog.dismiss();
							}

							//dismiss
							progressDialog.dismiss();
						}
					});
		} else {

			//dismiss
			progressDialog.dismiss();

			Utils.showToastShort(Constants.networkMsg, context);
		}


	}


}

