package com.stylist.planner;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Locale;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.constants.BaseActivity;
import com.constants.Constants;
import com.constants.CustomTextView;
import com.constants.LocalDatabaseHandler;
import com.constants.Utils;

import com.parse.ParseUser;
//import com.stylist.CategoryService;
import com.stylist.R;

@SuppressLint("SimpleDateFormat")
public class PlannerActivity extends BaseActivity {
	CustomTextView headerName;
	LinearLayout backButtonLayout;
	// CustomTextView addOutfit;
	int index = 0;
	public static PlannerActivity pPlannerActivity;
	String curentDate;
	public static PlannerActivity act;
	public List<String> dayString = new ArrayList<String>();
	public ArrayList<String> nameOfEvent = new ArrayList<String>();
	public ArrayList<String> startDates = new ArrayList<String>();
	public ArrayList<String> endDates = new ArrayList<String>();
	public ArrayList<String> descriptions = new ArrayList<String>();

	public GregorianCalendar month, itemmonth;// calendar instances.
	public CalendarAdapter adapter;// adapter instance
	public Handler handler;// for grabbing some event values for showing the dot
							// marker.
	public ArrayList<String> items; // container to store calendar items which
									// needs showing the event marker
	ArrayList<String> event;
	LinearLayout closecal;
	ArrayList<String> date;
	ArrayList<String> desc;
	private ImageView cal_nofi;
	private String selectedGridDate, newmonth;
	private Calendar todayscalendar;
	private int year, thismonth, day;
	View currentDateView;
	CustomTextView bottomText1;
	View previousSelectedGridView=null;
	String previousSelectedDate;
	LocalDatabaseHandler dbHelper;



	CustomTextView todayText;
	GridView gridview;
	String currentGridDate;

	public void onStart() {
		super.onStart();
		//.onStartSession(this, getResources().getString(R.string.flurryKey));
	}

	public void onStop() {
		super.onStop();
		//.onEndSession(this);
	}

	// HashMap<String, ArrayList<JSONObject>> plannerArray = new HashMap<String,
	// ArrayList<JSONObject>>();

	private String formatedDate11(Date date1) {
		// TODO Auto-generated method stub
		SimpleDateFormat format = new SimpleDateFormat("d");
		String date = format.format(date1);
		// System.out.println(" date " + date);
		if (date.endsWith("1") && !date.endsWith("11"))
			format = new SimpleDateFormat("MMM d'st'");
		else if (date.endsWith("2") && !date.endsWith("12"))
			format = new SimpleDateFormat("MMM d'nd'");
		else if (date.endsWith("3") && !date.endsWith("13"))
			format = new SimpleDateFormat("MMM d'rd'");
		else
			format = new SimpleDateFormat("MMM d'th'");

		return format.format(date1);
	}

	private String formatedDate11(String date1) {
		String suf = "", dateString1 = "";
		try {

			// TODO Auto-generated method stub

			String[] separatedTime = selectedGridDate.split("-");
			String gridvalueString = separatedTime[2].replaceFirst("^0*", "");// taking
																				// last
																				// part
																				// of
																				// date.
																				// ie;
																				// 2
																				// from
																				// 2012-12-02.
			int gridvalue = Integer.parseInt(gridvalueString);
			String newdate = separatedTime[0] + "-" + (Integer.parseInt(separatedTime[1]) - 1) + "-" + separatedTime[2];
			int d = Integer.parseInt(separatedTime[2]);
			int y = Integer.parseInt(separatedTime[0]);
			int m = Integer.parseInt(separatedTime[1]);
			// System.out.println("d " + d + " m " + m + " y " + y);

			SimpleDateFormat format1 = new SimpleDateFormat("");
			SimpleDateFormat format = new SimpleDateFormat("d");
			String date = format.format(format1.parse(date1));
			// System.out.println(" date " + date);

			if (date.endsWith("1") && !date.endsWith("11")) {
				suf = "'st";
				format = new SimpleDateFormat("MMM d'st'");
			} else if (date.endsWith("2") && !date.endsWith("12")) {
				suf = "'nd";
				format = new SimpleDateFormat("MMM d'nd'");
			} else if (date.endsWith("3") && !date.endsWith("13")) {
				suf = "'rd";
				format = new SimpleDateFormat("MMM d'rd'");
			} else {
				suf = "'th";
				format = new SimpleDateFormat("MMM d'th'");
			}
			switch (m) {
			case 1:
				dateString1 = "JAN " + d + suf;
				break;
			case 2:
				dateString1 = "	FEB " + d + suf;
				break;
			case 3:
				dateString1 = "MAR " + d + suf;
				break;
			case 4:
				dateString1 = "APR " + d + suf;
				break;
			case 5:
				dateString1 = "MAY " + d + suf;
				break;
			case 6:
				dateString1 = "JUN " + d + suf;
				break;
			case 7:
				dateString1 = "JUL " + d + suf;
				break;
			case 8:
				dateString1 = "AUG " + d + suf;

				break;
			case 9:
				dateString1 = "SEP " + d + suf;
				break;
			case 10:
				dateString1 = "OCT " + d + suf;
				break;
			case 11:
				dateString1 = "NOV " + d + suf;
				break;
			case 12:
				dateString1 = "DEC " + d + suf;
				break;

			}

		} catch (Exception e) {
			e.printStackTrace();
		}
		return dateString1;
	}

	private String formatedDate() {
		// TODO Auto-generated method stub
		SimpleDateFormat format = new SimpleDateFormat("d");
		String date = format.format(new Date());

		if (date.endsWith("1") && !date.endsWith("11"))
			format = new SimpleDateFormat("MMMM d'st' yyyy");
		else if (date.endsWith("2") && !date.endsWith("12"))
			format = new SimpleDateFormat("MMMM d'nd' yyyy");
		else if (date.endsWith("3") && !date.endsWith("13"))
			format = new SimpleDateFormat("MMMM d'rd' yyyy");
		else
			format = new SimpleDateFormat("MMMM d'th' yyyy");

		return format.format(new Date());
	}


	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_planner);
		Locale.setDefault(Locale.US);
		setHeading("PLANNER");
		act = this;
		backClick();
		todayscalendar = Calendar.getInstance();
		year = todayscalendar.get(Calendar.YEAR);
		thismonth = todayscalendar.get(Calendar.MONTH) + 1;
		if (thismonth < 9) {
			newmonth = "0" + thismonth;
		} else {
			newmonth = thismonth + "";
		}
		day = todayscalendar.get(Calendar.DAY_OF_MONTH);

		selectedGridDate = year + "-" + newmonth + "-" + day;

		SimpleDateFormat df = new SimpleDateFormat("", Locale.US);
		curentDate = df.format(new Date());
		pPlannerActivity = this;
		closecal = (LinearLayout) findViewById(R.id.closecal);
		month = (GregorianCalendar) GregorianCalendar.getInstance();
		itemmonth = (GregorianCalendar) month.clone();
		todayText = (CustomTextView) findViewById(R.id.textView1);
		items = new ArrayList<String>();
		adapter = new CalendarAdapter(this, month);
		index = 0;
		gridview = (GridView) findViewById(R.id.gridview);
		dbHelper = new LocalDatabaseHandler(context);
		handler = new Handler();
		handler.post(calendarUpdater);
		bottomText1 = (CustomTextView) findViewById(R.id.bottomText);
		bottomText1.setText(("ADD OUTFIT TO " + formatedDate11(new Date())).toUpperCase());
		bottomText1.setOnClickListener(this);
		CustomTextView title = (CustomTextView) findViewById(R.id.title);
		String titleN = android.text.format.DateFormat.format("MMMM yyyy", month).toString();

		title.setText(titleN.toUpperCase());

		todayText.setText("Today, " + formatedDate().toUpperCase());
		RelativeLayout previous = (RelativeLayout) findViewById(R.id.previous);

		previous.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				setPreviousMonth();
				refreshCalendar();
			}
		});

		RelativeLayout next = (RelativeLayout) findViewById(R.id.next);
		next.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				setNextMonth();
				refreshCalendar();

			}
		});

		gridview.setOnItemClickListener(new OnItemClickListener() {
			public void onItemClick(AdapterView<?> parent, View v, int position, long id) {
				// removing the previous view if added
				if (((LinearLayout) closecal).getChildCount() > 0) {
					((LinearLayout) closecal).removeAllViews();
				}
				SimpleDateFormat df = new SimpleDateFormat("dd-MM-yyyy", Locale.US);
				curentDate = df.format(new Date());

				desc = new ArrayList<String>();
				date = new ArrayList<String>();
			
				CustomTextView dayView = (CustomTextView) v.findViewById(R.id.date);

					if (previousSelectedGridView != null && previousSelectedDate != null
							&& previousSelectedDate.equals(curentDate)) {
						CustomTextView previousdayView = (CustomTextView) previousSelectedGridView
								.findViewById(R.id.date);
						previousdayView.setTextColor(Color.WHITE);
						previousdayView.setBackgroundResource(R.drawable.cal_selected);
					}
			
			if(previousSelectedGridView==null)
			{
				previousSelectedGridView = v;
			}
				


				selectedGridDate = dayString.get(position);
				currentGridDate = dayString.get(position);
				System.out.println("previousSelectedDate=======" + previousSelectedDate);
				System.out.println("currentGridDate=======" + currentGridDate);

				String[] separatedTime = selectedGridDate.split("-");
				String gridvalueString = separatedTime[0].replaceFirst("^0*", "");

				int gridvalue = Integer.parseInt(gridvalueString);
				String newdate = separatedTime[0] + "-" + (Integer.parseInt(separatedTime[1]) - 1) + "-"
						+ separatedTime[2];
				SimpleDateFormat format = new SimpleDateFormat("dd-MM-yyyy");
				try {
					Date gridDate = format.parse(selectedGridDate);
					Date currDate = format.parse(curentDate);
					long grid = gridDate.getTime();
					long curr = System.currentTimeMillis();
					if (gridDate.compareTo(currDate) >= 0) {	
//						Utils.write("out==========" );

						if(previousSelectedGridView !=null && gridDate.compareTo(currDate) >= 0 )
						{
//							Utils.write("comparebf============" );
							CustomTextView previousdayView = (CustomTextView) previousSelectedGridView.findViewById(R.id.date);
							if(previousSelectedDate.equals(curentDate))
							{
								previousdayView.setTextColor(Color.WHITE);
								previousdayView.setBackgroundResource(R.drawable.cal_selected);
							}
							else
							{
							previousdayView.setTextColor(Color.BLACK);
							previousdayView.setBackgroundResource(R.drawable.unchecked);
							}
						}
						dayView.setTextColor(Color.WHITE);
						dayView.setBackgroundResource(R.drawable.yellow);
						previousSelectedGridView = v;
						bottomText1.setText(("ADD OUTFIT TO " + formatedDate11(gridDate)).toUpperCase());
					} else {
						
						showToastS("Plan can not be created in past date!");
						selectedGridDate = previousSelectedDate;
					}

				} catch (Exception e) {
					e.printStackTrace();
				}

				Utils.write("gridvalue=========="+gridvalue );
				Utils.write("position=========="+position );
				if ((gridvalue > 10) && (position < 8)) {
					Utils.write("setPreviousMonth");
					setPreviousMonth();
					refreshCalendar();
				} else if ((gridvalue < 7) && (position > 28)) {
					Utils.write("setNextMonth");
					setNextMonth();
					refreshCalendar();
				}

				for (int i = 0; i < startDates.size(); i++) {
					if (startDates.get(i).equals(selectedGridDate)) {
						desc.add(nameOfEvent.get(i));
					}
				}
				if (desc.size() > 0) {
					for (int i = 0; i < desc.size(); i++) {
						CustomTextView rowTextView = new CustomTextView(context);
						// set some properties of rowTextView or something
						rowTextView.setText("Event:" + desc.get(i));
						rowTextView.setTextColor(Color.BLACK);
						closecal.addView(rowTextView);
					}
				}
				desc = null;

				previousSelectedDate = selectedGridDate;
				if (eventDate.contains(currentGridDate)) {
					// System.out.println("currentGridDate");
					Bundle b = new Bundle();
					b.putString("date", currentGridDate);
					b.putString("enddate", dayString.get(position + 1));
					goToActivity(PlannerDetailList.class, b);				}

			}

		});

	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		index = 0;
//		if (Utils.checkNetworkConnection(this)) {
//			if (getPrefrenceBool(Constants.isSyncing) != null && !getPrefrenceBool(Constants.isSyncing)) {
//				Intent in = new Intent(this, CategoryService.class);
//				in.putExtra("userId", ParseUser.getCurrentUser().getObjectId());
//				startService(in);
//			}
//		}
		Cursor c = dbHelper.returnAllPlanner(ParseUser.getCurrentUser().getObjectId());
		eventDate.clear();
		if (c.moveToFirst()) {
			do {
				try {
				
					String date1 = c.getString(c.getColumnIndex(LocalDatabaseHandler.PLAN_DATE));
					if (!eventDate.contains(date1)) {
						eventDate.add(date1);					
					}

				} catch (Exception e) {
					e.printStackTrace();
				}
			} while (c.moveToNext());
		}
		gridview.setAdapter(adapter);
	}

	ArrayList<String> eventDate = new ArrayList<String>();
	SimpleDateFormat dfDate_day = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
	SimpleDateFormat sfDate_day = new SimpleDateFormat("dd-MM-yyyy");
	SimpleDateFormat dfnew = new SimpleDateFormat("dd-MM-yyyy'T'HH:mm:ss.SSS'Z'");

	protected void setNextMonth() {
		// previousSelectedGridView = null;
		if (month.get(GregorianCalendar.MONTH) == month.getActualMaximum(GregorianCalendar.MONTH)) {
			month.set((month.get(GregorianCalendar.YEAR) + 1), month.getActualMinimum(GregorianCalendar.MONTH), 1);
		} else {
			month.set(GregorianCalendar.MONTH, month.get(GregorianCalendar.MONTH) + 1);
		}

	}

	protected void setPreviousMonth() {
		// previousSelectedGridView = null;
		if (month.get(GregorianCalendar.MONTH) == month.getActualMinimum(GregorianCalendar.MONTH)) {
			month.set((month.get(GregorianCalendar.YEAR) - 1), month.getActualMaximum(GregorianCalendar.MONTH), 1);
		} else {
			month.set(GregorianCalendar.MONTH, month.get(GregorianCalendar.MONTH) - 1);
		}

	}

	protected void showToast(String string) {
		Toast.makeText(this, string, Toast.LENGTH_SHORT).show();

	}

	public void refreshCalendar() {
		CustomTextView title = (CustomTextView) findViewById(R.id.title);
		index = 0;
		adapter.refreshDays();
		adapter.notifyDataSetChanged();
		handler.post(calendarUpdater); // generate some calendar items

		title.setText(android.text.format.DateFormat.format("MMMM yyyy", month));
	}

	public Runnable calendarUpdater = new Runnable() {

		@Override
		public void run() {
			items.clear();

			// Print dates of the current week
			DateFormat df = new SimpleDateFormat("dd-MM-yyyy", Locale.US);
			String itemvalue;
			event = readCalendarEvent(context);
			Log.d("=====Event====", event.toString());
			Log.d("=====Date ARRAY====", startDates.toString());

			for (int i = 0; i < startDates.size(); i++) {
				itemvalue = df.format(itemmonth.getTime());
				itemmonth.add(GregorianCalendar.DATE, 1);
				items.add(startDates.get(i).toString());
			}
			index = 0;
			adapter.setItems(items);
			adapter.notifyDataSetChanged();
		}
	};

	public class CalendarAdapter extends BaseAdapter {
		private Context mContext;

		private java.util.Calendar month;
		public GregorianCalendar pmonth; // calendar instance for previous month
		/**
		 * calendar instance for previous month for getting complete view
		 */
		public GregorianCalendar pmonthmaxset;
		private GregorianCalendar selectedDate;
		int firstDay;
		int maxWeeknumber;
		int maxP;
		int calMaxP;
		int lastWeekDay;
		int leftDays;
		int mnthlength;
		String itemvalue, curentDateString;
		DateFormat df;

		private ArrayList<String> items;

		private View previousView;

		public CalendarAdapter(Context c, GregorianCalendar monthCalendar) {
			// dayString = new ArrayList<String>();
			Locale.setDefault(Locale.US);
			month = monthCalendar;
			selectedDate = (GregorianCalendar) monthCalendar.clone();
			mContext = c;
			month.set(GregorianCalendar.DAY_OF_MONTH, 1);
			this.items = new ArrayList<String>();
			df = new SimpleDateFormat("dd-MM-yyyy", Locale.US);
			curentDateString = df.format(selectedDate.getTime());
			refreshDays();
		}

		public void setItems(ArrayList<String> items) {
			for (int i = 0; i != items.size(); i++) {
				if (items.get(i).length() == 1) {
					items.set(i, "0" + items.get(i));
				}
			}
			this.items = items;
		}

		public int getCount() {
			return dayString.size();
		}

		public Object getItem(int position) {
			return dayString.get(position);
		}

		public long getItemId(int position) {
			return 0;
		}

		// create a new view for each item referenced by the Adapter
		public View getView(int position, View convertView, ViewGroup parent) {
			View v = convertView;
			CustomTextView dayView;
			if (convertView == null) { // if it's not recycled, initialize some
										// attributes
				LayoutInflater vi = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
				v = vi.inflate(R.layout.calendar_item, null);

			}
			dayView = (CustomTextView) v.findViewById(R.id.date);
			// separates daystring into parts.
			String[] separatedTime = dayString.get(position).split("-");
			// taking last part of date. ie; 2 from 2012-12-02
			String gridvalue = separatedTime[0].replaceFirst("^0*", "");
			// checking whether the day is in current month or not.
			if ((Integer.parseInt(gridvalue) > 1) && (position < firstDay)) {
				// setting offdays to white color.
				dayView.setTextColor(Color.GRAY);
				dayView.setClickable(false);
				dayView.setFocusable(false);
			} else if ((Integer.parseInt(gridvalue) < 7) && (position > 28)) {
				dayView.setTextColor(Color.GRAY);
				dayView.setClickable(false);
				dayView.setFocusable(false);
			} else {
				dayView.setTextColor(Color.BLACK);
			}

			if (dayString.get(position).equals(curentDateString)) {
				dayView.setTextColor(Color.WHITE);
				dayView.setBackgroundResource(R.drawable.cal_selected);
				// if (previousView != null) {
				// CustomTextView previousdayView = (CustomTextView)
				// previousView
				// .findViewById(R.id.date);
				//
				// previousdayView.setTextColor(Color.BLACK);
				// dayView.setBackgroundResource(R.drawable.cal_selector);
				// }
				// previousView = v;
			} else {
				dayView.setTextColor(Color.BLACK);
				dayView.setBackgroundResource(R.drawable.cal_selector);
			}
			// System.out.println("eventDate "+eventDate.get(index));
			// System.out.println("dayString "+dayString.get(position));
			if (eventDate.contains(
					dayString.get(position))/*
											 * index < eventDate.size() &&
											 * eventDate.get(index).equals(
											 * dayString.get(position))
											 */) {
				// System.out.println("eventDate " + eventDate.get(index));
				dayView.setTextColor(Color.WHITE);
				dayView.setBackgroundResource(R.drawable.green);
				index++;
			}

			dayView.setText(gridvalue);

			// create date string for comparison
			String date = dayString.get(position);

			if (date.length() == 1) {
				date = "0" + date;
			}
			String monthStr = "" + (month.get(GregorianCalendar.MONTH) + 1);
			if (monthStr.length() == 1) {
				monthStr = "0" + monthStr;
			}

			// show icon if date is not empty and it exists in the items array
			cal_nofi = (ImageView) v.findViewById(R.id.cal_nofi);
			if (date.length() > 0 && items != null && items.contains(date)) {
				cal_nofi.setVisibility(View.VISIBLE);
			} else {
				cal_nofi.setVisibility(View.INVISIBLE);
			}
			return v;
		}

		// public View setSelected(View view) {
		// if (previousView != null) {
		// CustomTextView dayView = (CustomTextView) previousView
		// .findViewById(R.id.date);
		//
		// dayView.setTextColor(Color.BLACK);
		// dayView.setBackgroundResource(R.drawable.cal_selector);
		// }
		// previousView = view;
		// CustomTextView dayView = (CustomTextView) view
		// .findViewById(R.id.date);
		//
		// dayView.setTextColor(Color.WHITE);
		// dayView.setBackgroundResource(R.drawable.cal_selected);
		// return view;
		// }

		public void refreshDays() {
			// clear items
			items.clear();
			dayString.clear();
			Locale.setDefault(Locale.ENGLISH);
			pmonth = (GregorianCalendar) month.clone();
			// month start day. ie; sun, mon, etc
			firstDay = month.get(GregorianCalendar.DAY_OF_WEEK);
			// finding number of weeks in current month.
			maxWeeknumber = month.getActualMaximum(GregorianCalendar.WEEK_OF_MONTH);
			// allocating maximum row number for the gridview.
			mnthlength = maxWeeknumber * 7;
			maxP = getMaxP(); // previous month maximum day 31,30....
			calMaxP = maxP - (firstDay - 1);// calendar offday starting 24,25
											// ...
			/**
			 * Calendar instance for getting a complete gridview including the
			 * three month's (previous,current,next) dates.
			 */
			pmonthmaxset = (GregorianCalendar) pmonth.clone();
			/**
			 * setting the start date as previous month's required date.
			 */
			pmonthmaxset.set(GregorianCalendar.DAY_OF_MONTH, calMaxP + 1);

			/**
			 * filling calendar gridview.
			 */
			for (int n = 0; n < mnthlength; n++) {

				itemvalue = df.format(pmonthmaxset.getTime());
				pmonthmaxset.add(GregorianCalendar.DATE, 1);
				dayString.add(itemvalue);

			}
		}

		private int getMaxP() {
			int maxP;
			if (month.get(GregorianCalendar.MONTH) == month.getActualMinimum(GregorianCalendar.MONTH)) {
				pmonth.set((month.get(GregorianCalendar.YEAR) - 1), month.getActualMaximum(GregorianCalendar.MONTH), 1);
			} else {
				pmonth.set(GregorianCalendar.MONTH, month.get(GregorianCalendar.MONTH) - 1);
			}
			maxP = pmonth.getActualMaximum(GregorianCalendar.DAY_OF_MONTH);

			return maxP;
		}

	}

	@SuppressLint("SimpleDateFormat")
	public String getDate(long milliSeconds) {
		SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy");
		Calendar calendar = Calendar.getInstance();
		calendar.setTimeInMillis(milliSeconds);
		return formatter.format(calendar.getTime());
	}

	public ArrayList<String> readCalendarEvent(Context context) {
		// Cursor cursor = context.getContentResolver()
		// .query(Uri.parse("content://com.android.calendar/events"),
		// new String[] { "calendar_id", "title", "description",
		// "dtstart", "dtend", "eventLocation" }, null,
		// null, null);
		// cursor.moveToFirst();
		// // fetching calendars name
		// String CNames[] = new String[cursor.getCount()];
		//
		// // fetching calendars id
		// nameOfEvent.clear();
		// startDates.clear();
		// endDates.clear();
		// descriptions.clear();
		// try {
		// for (int i = 0; i < CNames.length; i++) {
		//
		// nameOfEvent.add(cursor.getString(1));
		// startDates.add(getDate(Long.parseLong(cursor.getString(3))));
		// endDates.add(getDate(Long.parseLong(cursor.getString(4))));
		// descriptions.add(cursor.getString(2));
		// CNames[i] = cursor.getString(1);
		// cursor.moveToNext();
		//
		// }
		// } catch (NumberFormatException e) {
		// // TODO: handle exception
		// }
		//
		return nameOfEvent;
	}

	/*
	 * @Override public void onBackPressed() {
	 * 
	 * super.onBackPressed(); finish();
	 * overridePendingTransition(R.anim.slide_in_up, R.anim.slide_out_down); }
	 */

	@Override
	public void onClick(View v) {

		switch (v.getId()) {
		case R.id.bottomText:

			// System.out.println("currentGridDate=======" + currentGridDate);

			Bundle b = new Bundle();

			b.putString("selectedDate", currentGridDate);
			goToActivity(AddLook.class, b);
			break;
		}
	}

	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();

		if (dbHelper != null) {

			dbHelper.close();
		}
	}
}
