package com.stylist.lookbook;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
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
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.constants.AsyncTaskAppWithoutArray;
import com.constants.BaseActivity;
import com.constants.LocalDatabaseHandler;
import com.constants.Utils;
import com.constants.WebImageLoader;
import com.parse.ParseUser;
import com.stylist.LooksbyStylistDetailActivity;
import com.stylist.R;
import com.stylist.askstylist.AskStylistModelClass;
import com.stylist.askstylist.AskStylistRequestListActivity.StylistRequestListAdapter;

public class LooksByStylistActivity extends BaseActivity {
	WebImageLoader mWebImageLoader = new WebImageLoader(context);
	ArrayList<AskStylistModelClass> askStylistItem = new ArrayList<AskStylistModelClass>();
	GridView myLookBookItemGridView;
	LocalDatabaseHandler dbHelper; 
    public static LooksByStylistActivity lLooksByStylistActivity;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_looks_by_stylist);
		backClick();
		dbHelper=new LocalDatabaseHandler(this);
		lLooksByStylistActivity=this;
		setHeading("Looks By stylist");
		myLookBookItemGridView = (GridView) findViewById(R.id.myLookBookItemGridView);
		LinearLayout single_button = (LinearLayout) findViewById(R.id.single_button);
		single_button.setVisibility(View.GONE);

	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		getStylistResponse();
	}


	private void getStylistResponse() {
		askStylistItem.clear();
		Cursor cStylistResponse = dbHelper.returnAllAskStylist(ParseUser
				.getCurrentUser().getObjectId());
		System.out.println("out cursor");
		if (cStylistResponse.moveToFirst()) {
			do {
				System.out.println("in cursor");
				String isDeleted = cStylistResponse.getString(cStylistResponse
						.getColumnIndex(LocalDatabaseHandler.IS_DELETED));
				System.out.println("isDeleted=======" + isDeleted);
				if (isDeleted.equalsIgnoreCase("false")) {
					askStylistItem
							.add(new AskStylistModelClass(
									cStylistResponse
											.getInt(cStylistResponse
													.getColumnIndex(LocalDatabaseHandler.QUERY_ID)),
									cStylistResponse.getString(cStylistResponse
											.getColumnIndex(LocalDatabaseHandler.OCASSION)),
									cStylistResponse.getString(cStylistResponse
											.getColumnIndex(LocalDatabaseHandler.COMMENTS)),
									cStylistResponse.getString(cStylistResponse
											.getColumnIndex(LocalDatabaseHandler.REQUEST_DATE)),
									cStylistResponse.getString(cStylistResponse
											.getColumnIndex(LocalDatabaseHandler.IS_STYLIST_RESPONSED)),
									cStylistResponse.getString(cStylistResponse
											.getColumnIndex(LocalDatabaseHandler.IS_STYLIST_LIKED)),
									cStylistResponse.getString(cStylistResponse
											.getColumnIndex(LocalDatabaseHandler.STYLIST_COMMENTS)),
									cStylistResponse.getString(cStylistResponse
											.getColumnIndex(LocalDatabaseHandler.PHOTOURL)),cStylistResponse.getString(cStylistResponse
													.getColumnIndex("parseQueryId"))));
					System.out.println("stylist====="+cStylistResponse.getString(cStylistResponse
											.getColumnIndex(LocalDatabaseHandler.PHOTOURL)));

				}
			} while (cStylistResponse.moveToNext());
		}
		myLookBookItemGridView.setAdapter(new MyLookBookItemAdapterNew());

	}
	
	
	
	
	/*private void getStylistResponse() {
		Map<String, Object> params1 = new HashMap<String, Object>();
		handler.open();

		
		 * new AsyncTaskAppWithoutArray(true, params1,
		 * LooksByStylistActivity.this, context, "getStylistResponse");
		 

	}
*/
	/*
	 * @Override public void AsynctaskResultwithoutArray(String arr, String url,
	 * Map<String, Object> par) { // TODO Auto-generated method stub
	 * super.AsynctaskResultwithoutArray(arr, url, par);
	 * System.out.println("arr==========" + arr);
	 * 
	 * if (url.equals("getStylistResponse")) askStylistItem.clear(); try { int
	 * index = 0; JSONArray jArray = new JSONArray(arr);
	 * System.out.println("jArray========" + jArray); int length =
	 * jArray.length(); while (index < length) { JSONObject obj =
	 * jArray.getJSONObject(index); if (obj.getBoolean("HasStylistResponsed")) {
	 * askStylistItem.add(new AskStylistModelClass(obj .getString("objectId"),
	 * obj.getString("Occasion"), obj.getString("StylistComment"), obj
	 * .getString("StylistPhotoURL"), obj .getString("createdAt"), obj
	 * .getBoolean("HasStylistResponsed"), obj .getString("Comments"), false));
	 * } index++; } myLookBookItemGridView.setAdapter(new
	 * MyLookBookItemAdapterNew()); } catch (JSONException e) { // TODO
	 * Auto-generated catch block e.printStackTrace(); } }
	 */

	public class MyLookBookItemAdapterNew extends BaseAdapter {
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
		public View getView(final int position, View convertView,
				ViewGroup parent) {
			ViewHolder holder;

			if (convertView == null) {
				holder = new ViewHolder();
				convertView = View.inflate(LooksByStylistActivity.this,
						R.layout.look_book_item_list, null);

				/*
				 * convertView.setOnClickListener(new View.OnClickListener() {
				 * 
				 * @Override public void onClick(View v) { // TODO
				 * Auto-generated method stub Intent intent = new
				 * Intent(context, LooksbyStylistDetailActivity.class);
				 * intent.putExtra("position", position); startActivity(intent);
				 * } });
				 */
				holder.mytitle = (TextView) convertView
						.findViewById(R.id.title);
				holder.date = (TextView) convertView.findViewById(R.id.date);
				holder.i1 = (ImageView) convertView
						.findViewById(R.id.imageView1);

				convertView.setTag(holder);
			} else {
				holder = (ViewHolder) convertView.getTag();
			}

			SimpleDateFormat outformat = new SimpleDateFormat("dd MMM yyyy");
			SimpleDateFormat informat = new SimpleDateFormat(
					"yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");

			try {
				holder.date.setText(outformat.format(informat
						.parse(askStylistItem.get(position).queryRequestDate)));
			} catch (ParseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			holder.mytitle.setText(askStylistItem.get(position).occasion);
			System.out.println("photourl========"+askStylistItem.get(position).photoUrl);
			System.out.println("photourl========"+Utils.orientedBitmap(askStylistItem.get(position).photoUrl));

			holder.i1.setImageBitmap(Utils.orientedBitmap(askStylistItem.get(position).photoUrl));
			/*mWebImageLoader.DisplayImage(askStylistItem.get(position).photoUrl,
					holder.i1)*/;

			return convertView;
		}

		private class ViewHolder {
			ImageView i1/* , imageViewDel */;
			TextView mytitle;
			TextView date;
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
