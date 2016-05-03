package com.stylist.planner;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

import android.app.AlarmManager;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.TimePicker;

import com.constants.BaseActivity;
import com.constants.Constants;
import com.constants.CustomEditText;
import com.constants.CustomTextView;
import com.constants.DateTimePickerDialog;
import com.constants.Helper;
import com.constants.LocalDatabaseHandler;
import com.constants.WebImageLoader;

import com.parse.ParseUser;
import com.stylist.MyBroadcastReceiver;
import com.stylist.R;
import com.stylist.lookbook.UploadFromLookbook;

public class AddLook extends BaseActivity {
	LocalDatabaseHandler dbHelper;
	// CustomTextView addLook,save;
	WebImageLoader loader;
	ImageView image;
	CustomTextView datetime, save, addLook;
	CustomEditText ocassion;
	SimpleDateFormat formata = new SimpleDateFormat("dd-MM-yyyy hh:mm aa");
	String selectedDate;
	public static AddLook aAddLook;
	Dialog saveDialog;
	LinearLayout nextOrDelButton;
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
		setContentView(R.layout.add_look);
		setHeading("Add Look");
		backClick();
		aAddLook = this;
		Bundle params = getIntent().getBundleExtra("android.intent.extra.INTENT");
		selectedDate = params.getString("selectedDate");
		addLook = (CustomTextView) findViewById(R.id.addLook);
		addLook.setOnClickListener(this);
		nextOrDelButton = (LinearLayout) findViewById(R.id.nextOrDelButton);
		ImageView nextOrDel = (ImageView) findViewById(R.id.nextOrDel);
		nextOrDelButton.setOnClickListener(this);
		nextOrDel.setImageResource(R.drawable.add);
		save = (CustomTextView) findViewById(R.id.bottomText);
		save.setText("Save");
		save.setOnClickListener(this);
		image = (ImageView) findViewById(R.id.image);
		loader = new WebImageLoader(context);
		datetime = (CustomTextView) findViewById(R.id.datetime);
		datetime.setOnClickListener(this);
		ocassion = (CustomEditText) findViewById(R.id.ocassion);
		dbHelper = new LocalDatabaseHandler(context);
	}

	

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.addLook:
			Intent i = new Intent(this, UploadFromLookbook.class);
			startActivityForResult(i, 111);
			nextOrDelButton.setVisibility(View.VISIBLE);
			addLook.setVisibility(View.GONE);
			break;
		case R.id.datetime:
			showTimePickerDialog();
			break;
		case R.id.nextOrDelButton:
			Intent in = new Intent(this, UploadFromLookbook.class);
			startActivityForResult(in, 111);
			break;
		case R.id.bottomText:

			if (url.equals("")) {
				showToastS("Please select a look.");
			} else if (ocassion.getText().toString().equals("")) {
				showToastS("Please enter the occasion.");
			} else if (datetime.getText().toString().equals("")) {
				showToastS("Please enter the time you would like us to remind");
			}
		 else if (datetime.getText().toString().equals("")) {












			showToastS("Please enter the time you would like us to remind");
		}else {
				try {
					// setPrefrenceBool(Constants.isUpdated,true);

					long lng = dbHelper.insertPlanner(ParseUser.getCurrentUser().getObjectId(),
							ocassion.getText().toString(), selectedDate, datetime.getText().toString(), lookbookid, url,
							dfDate_day.format(new Date()));
					System.out.println("selectedDate=======" + selectedDate);
					System.out.println("datetime=======" +  datetime.getText().toString());
					System.out.println("selectedDate=======" + formata.parse(selectedDate + " " + datetime.getText().toString()));

					long diffInMs = (formata.parse(selectedDate + " " + datetime.getText().toString())).getTime()
							- (new Date()).getTime();
					
					
					long diffInSec = TimeUnit.MILLISECONDS.toSeconds(diffInMs);
					System.out.println("diffInSec   " + diffInSec);
					Cursor c = dbHelper.returnAllPlanner(ParseUser.getCurrentUser().getObjectId());

					if (c.moveToLast()) {
						if (diffInSec <= 0)
						{
							diffInSec = 1;
							System.out.println("diffInSec   " + diffInSec);
						}
						System.out.println("plaaerIdAddLook=======" + c.getString(c.getColumnIndex(LocalDatabaseHandler.PLANNER_ID)));

						System.out.println("occasionAddLook=======" + c.getString(c.getColumnIndex(LocalDatabaseHandler.OCASSION)));

						setAlarm(diffInSec, ocassion.getText().toString(),datetime.getText().toString(),
								c.getString(c.getColumnIndex(LocalDatabaseHandler.PLANNER_ID)),
								c.getString(c.getColumnIndex(LocalDatabaseHandler.LOOKBOOK_ID)));

					}

				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				showToastL("Event has been added to planner.");
				finish();
			}

			break;

		default:
			break;
		}
	}

	String url = "";
	int lookbookid;

	@Override
	protected void onActivityResult(int requestCode, int responseCode, Intent data) {
		// TODO Auto-generated method stub
		super.onActivityResult(requestCode, responseCode, data);
		System.out.println("in add ");
		if (requestCode == 111 && responseCode == RESULT_OK) {
			try {
				nextOrDelButton.setVisibility(View.GONE);
				System.out.println("in add if");
				// intent_locName = data.getStringExtra("intent_locName");
				url = data.getStringExtra("intent_imgUrl");
				lookbookid = Integer.parseInt(data.getStringExtra("intent_lookid"));
				System.out.println("lookid  " + lookbookid);
				// loader.DisplayImage(url, image);
				image.setImageBitmap(BitmapFactory.decodeFile(url));
			} catch (Error e) {
				e.printStackTrace();
			} catch (Exception e) {
				e.printStackTrace();
			}

		}
	}

	private void showTimePickerDialog() {
		Calendar mcurrentTime = Calendar.getInstance();
		int hour = mcurrentTime.get(Calendar.HOUR_OF_DAY);
		int minute = mcurrentTime.get(Calendar.MINUTE);

		TimePickerDialog mTimePicker;
		mTimePicker = new TimePickerDialog(AddLook.this, new TimePickerDialog.OnTimeSetListener() {
			@Override
			public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute) {
				String AM_PM;
				if (selectedHour < 12) {
					AM_PM = "AM";
				} else {
					selectedHour -= 12;
					AM_PM = "PM";
				}
				System.out.println("current date--selectedHour--->" + selectedHour);
				System.out.println("current date-selectedMinute---->" + selectedMinute);
				Boolean timeOk=compareTime(selectedHour,selectedMinute);
				if(timeOk) {
					datetime.setText(selectedHour + ":" + selectedMinute + " " + AM_PM);
				}
				else
				{
					showToastS("Plan can not be created in previous time! ");
				}
			}
		}, hour, minute, false);// Yes 24 hour time
		mTimePicker.setTitle("Select Time");
		mTimePicker.show();
	}

	public void showDateTimeDialog(final TextView date_time_tv) {
		// TODO Auto-generated method stub

		DateTimePickerDialog dtpDialog = new DateTimePickerDialog(context);

		Calendar c = Calendar.getInstance();

		dtpDialog.setDateTime(c, selectedDate);

		dtpDialog.setButton(AlertDialog.BUTTON_POSITIVE, "Save", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int which) {

				try {
					if (dialog.getClass() != DateTimePickerDialog.class) {
						return;
					}
					switch (which) {
					case DialogInterface.BUTTON_POSITIVE:
						String initial_time = date_time_tv.getText().toString();
						DateTimePickerDialog reminderDl = (DateTimePickerDialog) dialog;

						Date date = reminderDl.getDate();

						String date_time = Helper.dateToString(date, Helper.NORMAL_FORMAT);

						Calendar c = Calendar.getInstance();
						String data = dfDate_day.format(c.getTime());// current
																		// date
																		// time

						System.out.println("current date----->" + data);

						boolean flag = compareDateTime(date_time, data);
						System.out.println("current date--flag--->" + flag);

						if (flag) {
							date_time_tv.setText(date_time);
						} else {
							date_time_tv.setText(data);
						}

						if (!initial_time.equals("") && compareDateTime(initial_time, date_time)) {
							date_time_tv.setText(initial_time);
						}
						break;
					case DialogInterface.BUTTON_NEGATIVE:
						// date_time_text.setText("Date Time");
						break;
					default:
						break;
					}
				} catch (Exception ex) {
					Log.e("MainActivity", ex.getMessage());
				}
			}
		});
		dtpDialog.setButton(AlertDialog.BUTTON_NEGATIVE, "Cancel", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int which) {

				dialog.dismiss();
			}
		});

		dtpDialog.show();
	}

	SimpleDateFormat dfDate_day = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

	private boolean compareDateTime(String date_time, String curr_date_time) {

		boolean flag = true;

		try {
			Date date = dfDate_day.parse(date_time);
			Date curr_date = dfDate_day.parse(curr_date_time);
			if (date.compareTo(curr_date) < 0) {
				System.out.println("date is less than my curr_date");
				flag = false;
			}
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return flag;
	}

	final static int RQS_1 = 1;

	private void setAlarm(long i, String msg, String time,String plannerId, String lookId) {
		System.out.println("plannerId=in look setalarm==="+ plannerId);
		Intent intent = new Intent(getBaseContext(), MyBroadcastReceiver.class);
		intent.putExtra("fromService", false);
		intent.putExtra("fromPlanner", true);
		intent.putExtra("alarm_message", msg);
		intent.putExtra("plannerId", plannerId);
		intent.putExtra("lookId", lookId);
		intent.putExtra("time", time);
		PendingIntent pendingIntent = PendingIntent.getBroadcast(getBaseContext(), RQS_1, intent,
				PendingIntent.FLAG_UPDATE_CURRENT);
		AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
		alarmManager.set(AlarmManager.RTC_WAKEUP, System.currentTimeMillis() + (i * 1000), pendingIntent);

	}
	int hour,min;
	String AM_PM;
	Boolean compareTime(int selectedHour,int selectedMin )
	{
		Calendar c = Calendar.getInstance();
		hour = c.get(Calendar.HOUR_OF_DAY);
		min = c.get(Calendar.MINUTE);
		int ds = c.get(Calendar.AM_PM);
		if(ds==0)
			AM_PM="am";
		else
			AM_PM="pm";

if(ds==0)
{

}
		else
{
	selectedHour=selectedHour+12;
}
		System.out.println("hour=======" + hour);

		System.out.println("selectedHour=======" + selectedHour);
		System.out.println("min=======" + min);
		if(((hour==selectedHour ) || (hour<selectedHour )))
		{
			if((min==selectedMin ) || (min<selectedMin ))
			return true;
		}
		return false;
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
