package com.stylist.planner;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Locale;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;

import com.constants.BaseActivity;
import com.constants.CustomTextView;
import com.constants.LocalDatabaseHandler;
import com.constants.Utils;

import com.parse.ParseUser;
import com.stylist.R;

public class PlannerDetailList extends BaseActivity {
	ListView myWishListtItemListView;
	String date, enddate;
	public static PlannerDetailList act;

	public void onStart() {
		super.onStart();
		//.onStartSession(this, getResources().getString(R.string.flurryKey));
	}

	public void onStop() {
		super.onStop();
		//.onEndSession(this);
	}

	LinearLayout nextOrDelButton;
	public static PlannerDetailList pPlannerDetailList;
	LocalDatabaseHandler dbHelper;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.planner_detail_list);
		setHeading("Planner Details");
		backClick();
		act = this;
		dbHelper = new LocalDatabaseHandler(context);
		myWishListtItemListView = (ListView) findViewById(R.id.plannerList);
		nextOrDelButton = (LinearLayout) findViewById(R.id.nextOrDelButton);
		ImageView nextOrDel = (ImageView) findViewById(R.id.nextOrDel);
		nextOrDelButton.setVisibility(View.VISIBLE);
		nextOrDel.setImageResource(R.drawable.add);
		Bundle params = getIntent().getBundleExtra("android.intent.extra.INTENT");
		pPlannerDetailList = this;
		date = params.getString("date");
		// date = date + "T00:00:00.000Z";
		System.out.println("date   " + date);
		enddate = params.getString("enddate");
		// enddate = enddate + "T00:00:00.000Z";
		nextOrDelButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {

				Bundle b = new Bundle();
				b.putString("selectedDate", date);
				goToActivity(AddLook.class, b);
			}
		});

	}

	ArrayList<ArrayList<String>> plannerList = new ArrayList<ArrayList<String>>();

	public class ListItemAdapterNew extends BaseAdapter {
		private LayoutInflater mInflater;

		@Override
		public int getCount() {
			return plannerList.size();
		}

		@Override
		public Object getItem(int position) {
			return plannerList.get(position);
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
				convertView = View.inflate(context, R.layout.planner_list_item, null);
				//holder.finalTime = (CustomTextView) convertView.findViewById(R.id.finalTime);
				holder.mytitle = (CustomTextView) convertView.findViewById(R.id.ocassion);
				holder.finalDate = (CustomTextView) convertView.findViewById(R.id.finalDate);
				holder.createdDate = (CustomTextView) convertView.findViewById(R.id.createdDate);
				holder.forwardImage = (ImageView) convertView.findViewById(R.id.forwardImage);
				// holder.deleteImage = (ImageView)
				// convertView.findViewById(R.id.deleteImage);
				convertView.setTag(holder);
			} else {
				holder = (ViewHolder) convertView.getTag();
			}
			ArrayList<String> temp = plannerList.get(position);
			SimpleDateFormat df = new SimpleDateFormat("dd-MM-yyyy", Locale.US);
			SimpleDateFormat outputFormat = new SimpleDateFormat("MM-yyyy-dd");
			SimpleDateFormat inputFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
           System.out.println("Created==attttt========="+temp.get(1));
        
			try {
				holder.mytitle.setText(temp.get(2));
				holder.createdDate.setText("Created at: " + temp.get(1));
//				holder.finalTime.setText("Event Time: " + temp.get(5));
				holder.finalDate.setText("Remind me at: " + temp.get(0)+" "+temp.get(5));
			} catch (Exception e) {
				e.printStackTrace();
			}

			convertView.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View arg0) {
					try {

						Intent i = new Intent(context, PlannerDetailItem.class);
						i.putExtra("LookId", plannerList.get(position).get(3));
						i.putExtra("PlannerId", plannerList.get(position).get(4));
						i.putExtra("planDate", plannerList.get(position).get(0));
						i.putExtra("createdAt", plannerList.get(position).get(1));
						i.putExtra("eventTime", plannerList.get(position).get(5));
						i.putExtra("notification", false);
						startActivity(i);

					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			});
			/*
			 * holder.deleteImage.setOnClickListener(new OnClickListener() {
			 * 
			 * @Override public void onClick(View arg0) { try {
			 * 
			 * deletePlannerList(position);
			 * 
			 * } catch (Exception e) { e.printStackTrace(); } } });
			 */
			return convertView;
		}

		private class ViewHolder {
			ImageView forwardImage;
			CustomTextView mytitle, createdDate, finalDate/*, finalTime*/;
		}
	}

	public void deletePlannerList(final int pos) {
		Utils.createSimpleDialog(context, "Alert", "Are You sure to delete plan ?", "Yes", "Cancel",
				new Utils.Method() {
					public void execute() {
						System.out.println("plannerList.size()====" + plannerList.size());
						dbHelper.setDeleteplanner(Integer.parseInt(plannerList.get(pos).get(4)));
						plannerList.remove(pos);

						System.out.println("plannerList.size()====" + plannerList.size());

						myWishListtItemListView.setAdapter(new ListItemAdapterNew());
						// finish();
					}
				}).show();

	}


	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		plannerList.clear();
		try {

			Cursor c = dbHelper.returnAllPlanner(ParseUser.getCurrentUser().getObjectId());

			System.out.println(" date=========================" + date);
			SimpleDateFormat outputFormat = new SimpleDateFormat("yyyy-MM-dd");
			SimpleDateFormat inputFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			if (c.moveToFirst()) {

				ArrayList<String> temp;
				do {
					temp = new ArrayList<String>();
					if (date.equalsIgnoreCase(c.getString(c.getColumnIndex(LocalDatabaseHandler.PLAN_DATE)))) {

					String[] PLAN_DATEarr = c.getString(c.getColumnIndex(LocalDatabaseHandler.PLAN_DATE))
								.split("-");
						
					for(int i=0;i<PLAN_DATEarr.length;i++)
					{
						System.out.println("PLAN_DATEarr==========="+PLAN_DATEarr[i]);
					}
						
						String[] CREATED_ATarr = outputFormat
								.format(inputFormat
										.parse(c.getString(c.getColumnIndex(LocalDatabaseHandler.CREATED_AT))))
								.split("-");
						System.out.println("CREATED_AT==========="+c.getString(c.getColumnIndex(LocalDatabaseHandler.CREATED_AT)));
						System.out.println("plan at==========="+c.getString(c.getColumnIndex(LocalDatabaseHandler.PLAN_DATE)));
						for(int i=0;i<CREATED_ATarr.length;i++)
						{
							System.out.println("CREATED_ATarr==========="+CREATED_ATarr[i]);
						}
						temp.add(formatDateto(Integer.parseInt(PLAN_DATEarr[0]), Integer.parseInt(PLAN_DATEarr[2]),
								Integer.parseInt(PLAN_DATEarr[1])));
						temp.add(formatDateto(Integer.parseInt(CREATED_ATarr[2]), Integer.parseInt(CREATED_ATarr[0]),
								Integer.parseInt(CREATED_ATarr[1])));
						temp.add(c.getString(c.getColumnIndex(LocalDatabaseHandler.OCASSION)));
						temp.add(c.getString(c.getColumnIndex(LocalDatabaseHandler.LOOKBOOK_ID)));
						temp.add(c.getString(c.getColumnIndex(LocalDatabaseHandler.PLANNER_ID)));
						temp.add(c.getString(c.getColumnIndex(LocalDatabaseHandler.PLAN_TIME)));
						plannerList.add(temp);
					}
					System.out.println(" date=in database========================"
							+ c.getString(c.getColumnIndex(LocalDatabaseHandler.PLAN_DATE)));

				} while (c.moveToNext());
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
		myWishListtItemListView.setAdapter(new ListItemAdapterNew());
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
