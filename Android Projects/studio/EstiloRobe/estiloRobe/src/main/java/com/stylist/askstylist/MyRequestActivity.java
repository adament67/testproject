package com.stylist.askstylist;

import java.util.ArrayList;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;

import com.constants.BaseActivity;

import com.stylist.R;

public class MyRequestActivity extends BaseActivity {
	com.constants.CustomTextView headerName;
	ListView myRequestList;
	ArrayList<String> requestIds = new ArrayList<String>();
	LinearLayout backButtonLayout;
	public static MyRequestActivity mMyRequestActivity;

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
		setContentView(R.layout.activity_my_request);
		mMyRequestActivity = this;
		setHeading("MY REQUEST");
		backClick();
		requestIds.add("a1234");
		requestIds.add("a1234");
		requestIds.add("a1234");
		requestIds.add("a1234");
		requestIds.add("a1234");
		requestIds.add("a1234");
		requestIds.add("a1234");
		requestIds.add("a1234");
		requestIds.add("a1234");
		myRequestList = (ListView) findViewById(R.id.requestslistView);
		myRequestList.setAdapter(new MyRequestsItemAdapter(requestIds));

	}

	public class MyRequestsItemAdapter extends BaseAdapter {
		private LayoutInflater mInflater;
		ArrayList<String> requestsIds;

		public MyRequestsItemAdapter(ArrayList<String> requestsIds) {
			this.requestsIds = requestsIds;

		}

		@Override
		public int getCount() {
			return requestsIds.size();
		}

		@Override
		public Object getItem(int position) {
			return requestsIds.get(position);
		}

		@Override
		public long getItemId(int position) {
			return position;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			ViewHolder holder;
			if (convertView == null) {
				holder = new ViewHolder();
				convertView = View.inflate(MyRequestActivity.this, R.layout.requests_view, null);

				holder.requestId = (com.constants.CustomTextView) convertView.findViewById(R.id.requestId);
				holder.viewResponse = (Button) convertView.findViewById(R.id.viewResponseBut);

				convertView.setTag(holder);
			} else {
				// convertView = convertView;
				holder = (ViewHolder) convertView.getTag();
			}

			holder.requestId.setText(requestsIds.get(position));
			return convertView;
		}

		private class ViewHolder {
			com.constants.CustomTextView requestId;
			Button viewResponse;
		}
	}

}
