package com.stylist.styleeditor;

import java.util.ArrayList;
import java.util.HashMap;

import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Point;
import android.os.Build;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;

import com.constants.BaseActivity;
import com.constants.LocalDatabaseHandler;
import com.constants.Utils;
import com.constants.WebImageLoader;
import com.parse.ParseUser;
import com.stylist.R;
import com.stylist.wardrobe.ClosetCategoryDetail;
import com.stylist.wardrobe.ClosetWardrobeDetail;

public class BrowseFromWarMain extends BaseActivity {
	LinearLayout backButtonLayout, nextOrDelButton, linearLayoutParent;
    public static Activity ctx;
	ListView categorylist;
	boolean isDialog = false;

	HashMap<String, ArrayList<String>> topUrl = new HashMap<String, ArrayList<String>>();
	WebImageLoader mWebImageLoader;
	int m = 0;
	com.constants.CustomTextView headerName, backTextView;
	Boolean isUplodClicked = false, isCatClicked = false;;
	ImageView nextOrDel;
	RelativeLayout newCategoryRelativeLayout;
	LocalDatabaseHandler dbHelper;

	ArrayList<JSONObject> allCategory = new ArrayList<JSONObject>();
	ArrayList<String> allCategoryName = new ArrayList<String>();
	ArrayList<String> allCategoryId = new ArrayList<String>();
	boolean isCreate = true;
	boolean IsAdapter = true;

	/*
	 * public void onStart() { super.onStart(); //.onStartSession(this,
	 * getResources().getString(R.string.flurryKey)); }
	 * 
	 * public void onStop() { super.onStop(); //.onEndSession(this); }
	 */

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_my_closet);
		setHeading("MY CLOSET");
		ctx=this;
		backTextView = (com.constants.CustomTextView) findViewById(R.id.back);
		nextOrDelButton = (LinearLayout) findViewById(R.id.nextOrDelButton);
		nextOrDel = (ImageView) findViewById(R.id.nextOrDel);
		nextOrDelButton.setVisibility(View.INVISIBLE);
		nextOrDelButton.setVisibility(View.INVISIBLE);
		mWebImageLoader = new WebImageLoader(context);
		categorylist = (ListView) findViewById(R.id.myclosetListView);
		backClick();
		RelativeLayout newCategoryRelativeLayout = (RelativeLayout) findViewById(R.id.relativeLayoutParentc);
		newCategoryRelativeLayout.setOnClickListener(this);
		linearLayoutParent = (LinearLayout) findViewById(R.id.linearLayoutParent);
		dbHelper = new LocalDatabaseHandler(context);

		getScreenDimention(this);

		img_width=(screenWidth/4);

	}



	int screenWidth, screenHeight, img_width;

	@SuppressLint("NewApi")
	private void getScreenDimention(Context context) {

		if (Build.VERSION.SDK_INT >= 7) {
			Point size = new Point();
			try {
				((Activity) context).getWindowManager().getDefaultDisplay()
						.getRealSize(size);
				screenWidth = size.x;
				screenHeight = size.y;
			} catch (NoSuchMethodError e) {
			}

		} else {
			DisplayMetrics metrics = new DisplayMetrics();
			((Activity) context).getWindowManager().getDefaultDisplay()
					.getMetrics(metrics);
			screenWidth = metrics.widthPixels;
			screenHeight = metrics.heightPixels;
		}

	}





	public static final int BrowseFromWardrobeActivityResult = 31;

	@Override
	protected void onActivityResult(int requestCode, int responseCode, Intent data) {
		// TODO Auto-generated method stub
		super.onActivityResult(requestCode, responseCode, data);
		System.out.println("on act result");
		if (requestCode == 31 && responseCode == RESULT_OK) {
			String index = data.getStringExtra("intent_data");
			setResult(RESULT_OK, new Intent().putExtra("intent_data", index));
			System.out.println("on act result if");
			finish();
		}
		if (requestCode == 23 && responseCode == RESULT_OK) {
			String index = data.getStringExtra("intent_data");
			setResult(RESULT_OK, new Intent().putExtra("intent_data", index));
			System.out.println("on act result else");
			finish();
		}
	}

	HashMap<String, ArrayList<JSONObject>> wardrobeItem = new HashMap<String, ArrayList<JSONObject>>();

	private int selectedSpinnrPos = -1;

	// Dialog addMasterCategory;

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {

		case R.id.relativeLayoutParentc:
			Utils.setupUI(findViewById(R.id.relativeLayoutParentc), context);
			break;

		}
	}

	ArrayList<ClosetCategoryDetail> wardrobeDetails = new ArrayList<ClosetCategoryDetail>();

	private void setAllCategory() {
		allCategoryName.clear();
		wardrobeDetails.clear();
		allCategoryId.clear();
		Cursor c = dbHelper.returnAllCategory(ParseUser.getCurrentUser().getObjectId());
		Cursor cWardrobe = dbHelper.getWardrobeItem(ParseUser.getCurrentUser().getObjectId());

		System.out.println("c    " + c.getCount());
		System.out.println("cWardrobe   " + cWardrobe.getCount());
		ArrayList<ClosetWardrobeDetail> warTemp;

		BitmapFactory.Options opt=new BitmapFactory.Options();
		opt.inSampleSize=4;

		if (c.moveToFirst()) {

			do {
				String id = c.getString(c.getColumnIndex(LocalDatabaseHandler.CATEGORY_ID));

				System.out.println("category name   " + id);
				allCategoryName.add(c.getString(c.getColumnIndex(LocalDatabaseHandler.CUSTOM_CATEGORY_NAME)));
				allCategoryId.add(c.getString(c.getColumnIndex(LocalDatabaseHandler.CATEGORY_ID)));
				warTemp = new ArrayList<ClosetWardrobeDetail>();
				int firstTime = 1;
				if (cWardrobe.moveToFirst()) {

					do {
						String WarCatId = cWardrobe
								.getString(cWardrobe.getColumnIndex(LocalDatabaseHandler.CATEGORY_ID));
//remove last pram null
						if (WarCatId.equals(id)) {
							warTemp.add(new ClosetWardrobeDetail(
									cWardrobe.getString(cWardrobe.getColumnIndex(LocalDatabaseHandler.WARDROBE_ID)),
									cWardrobe.getString(cWardrobe.getColumnIndex(LocalDatabaseHandler.WARDROBE_COLOR)),
									cWardrobe.getString(cWardrobe.getColumnIndex(LocalDatabaseHandler.WARDROBE_NAME)),
									cWardrobe.getString(cWardrobe.getColumnIndex(LocalDatabaseHandler.IMAGE_PATH)),
									cWardrobe.getString(cWardrobe.getColumnIndex(LocalDatabaseHandler.DRESS_CODE)),
									cWardrobe.getString(cWardrobe.getColumnIndex(LocalDatabaseHandler.IS_FAVOURITE)),
									BitmapFactory.decodeFile(cWardrobe.getString(cWardrobe.getColumnIndex(LocalDatabaseHandler.IMAGE_PATH)),opt),
									cWardrobe.getString(cWardrobe.getColumnIndex(LocalDatabaseHandler.BACKGROUND_COLOR))));


						}
						if (warTemp.size() == 4) {
							break;
						}

					} while (cWardrobe.moveToNext());

				}

				wardrobeDetails
						.add(new ClosetCategoryDetail(c.getString(c.getColumnIndex(LocalDatabaseHandler.CATEGORY_ID)),
								c.getString(c.getColumnIndex(LocalDatabaseHandler.CUSTOM_CATEGORY_NAME)), warTemp));

			} while (c.moveToNext());
		}

		if (IsAdapter)
			categorylist.setAdapter(new ListItemAdapterNew());
		else
			((ListItemAdapterNew) categorylist.getAdapter()).notifyDataSetChanged();
	}

	public class ListItemAdapterNew extends BaseAdapter {
		private LayoutInflater mInflater;

		@Override
		public int getCount() {
			return allCategoryName.size();
		}

		@Override
		public Object getItem(int position) {
			return allCategoryName.get(position);
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
				convertView = View.inflate(context, R.layout.mycloset_vertical_list_items, null);
				holder.edit = (ImageView) convertView.findViewById(R.id.edit);
				holder.delete = (ImageView) convertView.findViewById(R.id.delete);
				holder.edit.setVisibility(View.INVISIBLE);
				holder.delete.setVisibility(View.INVISIBLE);
				holder.mytitle = (com.constants.CustomTextView) convertView.findViewById(R.id.title);
				holder.i1 = (ImageView) convertView.findViewById(R.id.imageView1);
				holder.i2 = (ImageView) convertView.findViewById(R.id.imageView2);
				holder.i3 = (ImageView) convertView.findViewById(R.id.imageView3);
				holder.i4 = (ImageView) convertView.findViewById(R.id.imageView4);

				Utils.write("img_width===" + img_width);
				FrameLayout.LayoutParams layoutParams=new FrameLayout.LayoutParams(img_width,img_width);
				//layoutParams.setMargins(2,2,2,2);

				layoutParams.gravity = Gravity.CENTER;
				holder.i1.setLayoutParams(layoutParams);
				holder.i2.setLayoutParams(layoutParams);
				holder.i3.setLayoutParams(layoutParams);
				holder.i4.setLayoutParams(layoutParams);
				convertView.setTag(holder);



			} else {
				holder = (ViewHolder) convertView.getTag();
			}


			FrameLayout frame1 = (FrameLayout)convertView.findViewById(R.id.frame1);
			FrameLayout frame2 = (FrameLayout)convertView.findViewById(R.id.frame2);
			FrameLayout frame3 = (FrameLayout)convertView.findViewById(R.id.frame3);
			FrameLayout frame4 = (FrameLayout)convertView.findViewById(R.id.frame4);






			holder.mytitle.setText(allCategoryName.get(position));
			ArrayList<ClosetWardrobeDetail> warDetail;

			if (wardrobeDetails.size() > 0) {
				warDetail = wardrobeDetails.get(position).wardeobeDetail;
			} else {
				warDetail = new ArrayList<ClosetWardrobeDetail>();

			}
			if (warDetail.size() == 0) {
				holder.i1.setVisibility(View.INVISIBLE);
				holder.i2.setVisibility(View.INVISIBLE);
				holder.i3.setVisibility(View.INVISIBLE);
				holder.i4.setVisibility(View.INVISIBLE);


				frame1.setBackgroundColor(Color.parseColor("#FFFFFF"));
				frame2.setBackgroundColor(Color.parseColor("#FFFFFF"));
				frame3.setBackgroundColor(Color.parseColor("#FFFFFF"));
				frame4.setBackgroundColor(Color.parseColor("#FFFFFF"));



			} else if (warDetail.size() == 1) {
				holder.i1.setVisibility(View.VISIBLE);





				String[] arrRgb1 = (warDetail.get(0).bg_color).split(",");

				frame1.setBackgroundColor(Color.rgb(Integer.parseInt(arrRgb1[0].trim()), Integer.parseInt(arrRgb1[1].trim()), Integer.parseInt(arrRgb1[2].trim())));





				holder.i1.setImageBitmap(warDetail.get(0).img);
				holder.i2.setVisibility(View.INVISIBLE);
				holder.i3.setVisibility(View.INVISIBLE);
				holder.i4.setVisibility(View.INVISIBLE);




				//frame1.setBackgroundColor(Color.parseColor("#FFFFFF"));
				frame2.setBackgroundColor(Color.parseColor("#FFFFFF"));
				frame3.setBackgroundColor(Color.parseColor("#FFFFFF"));
				frame4.setBackgroundColor(Color.parseColor("#FFFFFF"));




			} else if (warDetail.size() == 2) {


				String[] arrRgb1 = (warDetail.get(0).bg_color).split(",");
				String[] arrRgb2 = (warDetail.get(1).bg_color).split(",");

				Log.v("SET_RGB1", Integer.parseInt(arrRgb1[0].trim())+","+ Integer.parseInt(arrRgb1[1].trim())+","+ Integer.parseInt(arrRgb1[2].trim()));
				Log.v("SET_RGB2", Integer.parseInt(arrRgb2[0].trim()) + "," + Integer.parseInt(arrRgb2[1].trim()) + "," + Integer.parseInt(arrRgb2[2].trim()));

				frame1.setBackgroundColor(Color.rgb(Integer.parseInt(arrRgb1[0].trim()), Integer.parseInt(arrRgb1[1].trim()), Integer.parseInt(arrRgb1[2].trim())));
				frame2.setBackgroundColor(Color.rgb(Integer.parseInt(arrRgb2[0].trim()), Integer.parseInt(arrRgb2[1].trim()), Integer.parseInt(arrRgb2[2].trim())));

				//frame1.setBackgroundColor(Color.parseColor("#FFFFFF"));
				//frame2.setBackgroundColor(Color.parseColor("#FFFFFF"));
				frame3.setBackgroundColor(Color.parseColor("#FFFFFF"));
				frame4.setBackgroundColor(Color.parseColor("#FFFFFF"));


				holder.i1.setVisibility(View.VISIBLE);
				holder.i2.setVisibility(View.VISIBLE);
				holder.i1.setImageBitmap(warDetail.get(0).img);
				holder.i2.setImageBitmap(warDetail.get(1).img);
				holder.i3.setVisibility(View.INVISIBLE);
				holder.i4.setVisibility(View.INVISIBLE);
			} else if (warDetail.size() == 3) {



				String[] arrRgb1 = (warDetail.get(0).bg_color).split(",");
				String[] arrRgb2 = (warDetail.get(1).bg_color).split(",");
				String[] arrRgb3 = (warDetail.get(2).bg_color).split(",");

				frame1.setBackgroundColor(Color.rgb(Integer.parseInt(arrRgb1[0].trim()), Integer.parseInt(arrRgb1[1].trim()), Integer.parseInt(arrRgb1[2].trim())));
				frame2.setBackgroundColor(Color.rgb(Integer.parseInt(arrRgb2[0].trim()), Integer.parseInt(arrRgb2[1].trim()), Integer.parseInt(arrRgb2[2].trim())));
				frame3.setBackgroundColor(Color.rgb(Integer.parseInt(arrRgb3[0].trim()), Integer.parseInt(arrRgb3[1].trim()), Integer.parseInt(arrRgb3[2].trim())));

				//frame1.setBackgroundColor(Color.parseColor("#FFFFFF"));
				//frame2.setBackgroundColor(Color.parseColor("#FFFFFF"));
				//frame3.setBackgroundColor(Color.parseColor("#FFFFFF"));
				frame4.setBackgroundColor(Color.parseColor("#FFFFFF"));



				holder.i1.setVisibility(View.VISIBLE);
				holder.i2.setVisibility(View.VISIBLE);
				holder.i3.setVisibility(View.VISIBLE);
				holder.i1.setImageBitmap(warDetail.get(0).img);
				holder.i2.setImageBitmap(warDetail.get(1).img);
				holder.i3.setImageBitmap(warDetail.get(2).img);
				holder.i4.setVisibility(View.INVISIBLE);
			} else if (warDetail.size() >= 4) {


				String[] arrRgb1 = (warDetail.get(0).bg_color).split(",");
				String[] arrRgb2 = (warDetail.get(1).bg_color).split(",");
				String[] arrRgb3 = (warDetail.get(2).bg_color).split(",");
				String[] arrRgb4 = (warDetail.get(3).bg_color).split(",");

				frame1.setBackgroundColor(Color.rgb(Integer.parseInt(arrRgb1[0].trim()), Integer.parseInt(arrRgb1[1].trim()), Integer.parseInt(arrRgb1[2].trim())));
				frame2.setBackgroundColor(Color.rgb(Integer.parseInt(arrRgb2[0].trim()), Integer.parseInt(arrRgb2[1].trim()), Integer.parseInt(arrRgb2[2].trim())));
				frame3.setBackgroundColor(Color.rgb(Integer.parseInt(arrRgb3[0].trim()), Integer.parseInt(arrRgb3[1].trim()), Integer.parseInt(arrRgb3[2].trim())));
				frame4.setBackgroundColor(Color.rgb(Integer.parseInt(arrRgb4[0].trim()), Integer.parseInt(arrRgb4[1].trim()), Integer.parseInt(arrRgb4[2].trim())));



				holder.i1.setVisibility(View.VISIBLE);
				holder.i2.setVisibility(View.VISIBLE);
				holder.i3.setVisibility(View.VISIBLE);
				holder.i4.setVisibility(View.VISIBLE);
				holder.i1.setImageBitmap(warDetail.get(0).img);
				holder.i2.setImageBitmap(warDetail.get(1).img);
				holder.i3.setImageBitmap(warDetail.get(2).img);
				holder.i4.setImageBitmap(warDetail.get(3).img);
			}
			else
			{
				frame1.setBackgroundColor(Color.parseColor("#FFFFFF"));
				frame2.setBackgroundColor(Color.parseColor("#FFFFFF"));
				frame3.setBackgroundColor(Color.parseColor("#FFFFFF"));
				frame4.setBackgroundColor(Color.parseColor("#FFFFFF"));


				holder.i1.setVisibility(View.INVISIBLE);
				// holder.i1.setImageResource(R.drawable.add_button);
				holder.i2.setVisibility(View.INVISIBLE);
				holder.i3.setVisibility(View.INVISIBLE);
				holder.i4.setVisibility(View.INVISIBLE);
			}

			convertView.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View arg0) {
					Bundle b = new Bundle();
					Intent i3 = new Intent(context, SelectWardrobeCategoryDetail.class);
					try {
						b.putString("categoryId", allCategoryId.get(position));
						b.putString("categoryName", allCategoryName.get(position));
					} catch (Exception e) {
						e.printStackTrace();
					}
					i3.putExtra("android.intent.extra.INTENT", b);
					startActivityForResult(i3, 23);
				}
			});

			return convertView;
		}

		private class ViewHolder {
			ImageView i1, i2, i3, i4, edit, delete;
			com.constants.CustomTextView mytitle;
		}
	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		if (!isDialog) {
			System.out.println("                            on?Resume");
			setAllCategory();
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
