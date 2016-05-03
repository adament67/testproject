package com.stylist.wardrobe;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;

import org.json.JSONObject;

import com.constants.BaseActivity;
import com.constants.Constants;
import com.constants.CustomTextView;
import com.constants.LocalDatabaseHandler;
import com.constants.Utils;
import com.constants.WebImageLoader;

import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseUser;
import com.parse.SaveCallback;
//import com.stylist.CategoryService;
import com.stylist.HomeActivity;
import com.stylist.R;
import com.stylist.styleeditor.SelectWardrobeCategoryDetail;
import com.stylist.styleeditor.BrowseFromWarMain.ListItemAdapterNew;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
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
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;

public class MyClosetActivity extends BaseActivity {


	public static Activity act;
	int screenWidth, screenHeight;

	//private ProgressDialog progressDialog;


	LinearLayout backButtonLayout, nextOrDelButton, linearLayoutParent;

	ListView categorylist;
	boolean isDialog = false;

	HashMap<String, ArrayList<String>> topUrl = new HashMap<String, ArrayList<String>>();
	WebImageLoader mWebImageLoader;
	int m = 0;
	com.constants.CustomTextView headerName, backTextView,dotted_textView;
	Boolean isUplodClicked = false, isCatClicked = false;;
	ImageView nextOrDel;
	RelativeLayout newCategoryRelativeLayout;
	LocalDatabaseHandler dbHelper;

	ArrayList<JSONObject> allCategory = new ArrayList<JSONObject>();
	ArrayList<String> allCategoryName = new ArrayList<String>();
	ArrayList<String> allCategoryId = new ArrayList<String>();
	boolean isCreate = true;
	boolean IsAdapter = true;
int img_width;
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
		setContentView(R.layout.activity_my_closet);


		try{

			/*progressDialog = new ProgressDialog(this);
		progressDialog.setMessage("Please Wait...");
		progressDialog.setCancelable(false);*/

			setHeading("MY CLOSET");
			act = this;
			dbHelper = new LocalDatabaseHandler(context);
			backTextView = (com.constants.CustomTextView) findViewById(R.id.back);
			nextOrDelButton = (LinearLayout) findViewById(R.id.nextOrDelButton);
			ImageView nextOrDel = (ImageView) findViewById(R.id.nextOrDel);
			dotted_textView = (com.constants.CustomTextView) findViewById(R.id.dotted_textView);
			getScreenDimention(this);
			dotted_textView.setOnClickListener(this);
			nextOrDelButton.setVisibility(View.VISIBLE);
			nextOrDelButton.setOnClickListener(this);
			nextOrDel.setImageResource(R.drawable.add);
			categorylist = (ListView) findViewById(R.id.myclosetListView);

			img_width=(screenWidth/4);

			backClick();
			RelativeLayout newCategoryRelativeLayout = (RelativeLayout) findViewById(R.id.relativeLayoutParentc);
			newCategoryRelativeLayout.setOnClickListener(this);
			linearLayoutParent = (LinearLayout) findViewById(R.id.linearLayoutParent);
			isCreate = true;

		}catch(Error e){

			e.printStackTrace();

		}

		catch(Exception e){

			e.printStackTrace();

		}



	}


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

		case R.id.nextOrDelButton:
			final Dialog newCategoryNameDialog = addDialog(R.layout.new_category);
			final EditText cateName = (EditText) newCategoryNameDialog.findViewById(R.id.category_editTextn);
			CustomTextView changeBut = (CustomTextView) newCategoryNameDialog.findViewById(R.id.createButn);
			CustomTextView cancelBut = (CustomTextView) newCategoryNameDialog.findViewById(R.id.cancelButn);
			newCategoryNameDialog.show();
			changeBut.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View arg0) {

					if (!cateName.getText().toString().equals("")) {
						if (!allCategoryName.contains(cateName.getText().toString())) {
							allCategoryName.add(cateName.getText().toString());
	if(Utils.checkNetworkConnection(MyClosetActivity.this)) {

	//progressDialog.show();

	String cateId = dbHelper.insertNewCategoryReturnId(ParseUser.getCurrentUser().getObjectId(),
			cateName.getText().toString(), "false");


	Log.e("iddddddddddddddd", cateId);

	final ParseObject category = new ParseObject("Category");
	category.put("CustomCategoryName", cateName.getText().toString());
	category.put("LocalDatabaseID", ParseUser.getCurrentUser().getObjectId() + "" + cateId);
	Log.e("LocalDatabaseID", ParseUser.getCurrentUser().getObjectId() + "" + cateId);
	category.put("IsActive", true);
	category.put("IsDeleted", false);

	category.put("UserId", ParseObject.createWithoutData("_User", ParseUser.getCurrentUser().getObjectId()));
	category.saveInBackground(new SaveCallback() {
		@Override
		public void done(ParseException e) {

			if (e != null) {
				e.printStackTrace();
			} else {

				//progressDialog.dismiss();

				//showToastS("category created");
			}
		}
	});

} else {
	dbHelper.insertNewCategory(ParseUser.getCurrentUser().getObjectId(),
			cateName.getText().toString(), "true");
}
							// IsAdapter = false;
							setAllCategory();
							setPrefrenceBool(Constants.isUpdated, true);
							newCategoryNameDialog.dismiss();
							dotted_textView.setVisibility(View.GONE);

						} else {
							showToastS("This Category Name Already Exist! Please Enter Another Category Name.");
						}
					} else {
						showToastS("Please Enter Category Name ");
					}
				}
			});

			cancelBut.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View arg0) {

					newCategoryNameDialog.dismiss();
				}
			});
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


						//.compress(Bitmap.CompressFormat.PNG, 100, out)

						if (WarCatId.equals(id)) {
							warTemp.add(new ClosetWardrobeDetail(
									cWardrobe.getString(cWardrobe.getColumnIndex(LocalDatabaseHandler.WARDROBE_ID)),
									cWardrobe.getString(cWardrobe.getColumnIndex(LocalDatabaseHandler.WARDROBE_COLOR)),
									cWardrobe.getString(cWardrobe.getColumnIndex(LocalDatabaseHandler.WARDROBE_NAME)),
									cWardrobe.getString(cWardrobe.getColumnIndex(LocalDatabaseHandler.IMAGE_PATH)),
									cWardrobe.getString(cWardrobe.getColumnIndex(LocalDatabaseHandler.DRESS_CODE)),
									cWardrobe.getString(cWardrobe.getColumnIndex(LocalDatabaseHandler.IS_FAVOURITE)),
									BitmapFactory.decodeFile(cWardrobe.getString(cWardrobe.getColumnIndex(LocalDatabaseHandler.IMAGE_PATH)), opt),cWardrobe.getString(cWardrobe.getColumnIndex(LocalDatabaseHandler.BACKGROUND_COLOR))));

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

		if (IsAdapter) {

			//change visibility-------------------------------
			if (allCategoryName.isEmpty()) {

				dotted_textView.setVisibility(View.VISIBLE);

			} else {

				dotted_textView.setVisibility(View.GONE);

			}


			//----------------------------------

			categorylist.setAdapter(new ListItemAdapterNew());

		}
		else {

				((ListItemAdapterNew) categorylist.getAdapter()).notifyDataSetChanged();
		}
	}


	@Override
	protected void onPause() {
		super.onPause();
		if(dbHelper != null){

			dbHelper.close();
		}
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
				holder.edit.setVisibility(View.VISIBLE);
				holder.delete.setVisibility(View.VISIBLE);
				holder.mytitle = (com.constants.CustomTextView) convertView.findViewById(R.id.title);

				//frames
				/*holder.f1=(LinearLayout)findViewById(R.id.frame1);
				holder.f2=(LinearLayout)findViewById(R.id.frame2);
				holder.f3=(LinearLayout)findViewById(R.id.frame3);
				holder.f4=(LinearLayout)findViewById(R.id.frame4);*/

				//images
				holder.i1 = (ImageView) convertView.findViewById(R.id.imageView1);
				holder.i2 = (ImageView) convertView.findViewById(R.id.imageView2);
				holder.i3 = (ImageView) convertView.findViewById(R.id.imageView3);
				holder.i4 = (ImageView) convertView.findViewById(R.id.imageView4);

				Utils.write("img_width==="+img_width);
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

				holder.i1.setImageResource(R.drawable.addnew);
				holder.i2.setVisibility(View.INVISIBLE);
				holder.i3.setVisibility(View.INVISIBLE);
				holder.i4.setVisibility(View.INVISIBLE);


				frame1.setBackgroundColor(Color.parseColor("#FFFFFF"));
				frame2.setBackgroundColor(Color.parseColor("#FFFFFF"));
				frame3.setBackgroundColor(Color.parseColor("#FFFFFF"));
				frame4.setBackgroundColor(Color.parseColor("#FFFFFF"));


			} else if (warDetail.size() == 1) {

				String[] arrRgb = (warDetail.get(0).bg_color).split(",");

				frame1.setBackgroundColor(Color.rgb(Integer.parseInt(arrRgb[0].trim()), Integer.parseInt(arrRgb[1].trim()), Integer.parseInt(arrRgb[2].trim())));


				//frame1.setBackgroundColor(Color.parseColor("#FFFFFF"));
				frame2.setBackgroundColor(Color.parseColor("#FFFFFF"));
				frame3.setBackgroundColor(Color.parseColor("#FFFFFF"));
				frame4.setBackgroundColor(Color.parseColor("#FFFFFF"));


				holder.i1.setImageBitmap(warDetail.get(0).img);

				holder.i1.setVisibility(View.VISIBLE);
				holder.i2.setVisibility(View.INVISIBLE);
				holder.i3.setVisibility(View.INVISIBLE);
				holder.i4.setVisibility(View.INVISIBLE);

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


				holder.i1.setImageBitmap(warDetail.get(0).img);
				holder.i2.setImageBitmap(warDetail.get(1).img);

				holder.i1.setVisibility(View.VISIBLE);
				holder.i2.setVisibility(View.VISIBLE);
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

				holder.i1.setImageBitmap(warDetail.get(0).img);
				holder.i2.setImageBitmap(warDetail.get(1).img);
				holder.i3.setImageBitmap(warDetail.get(2).img);

				holder.i1.setVisibility(View.VISIBLE);
				holder.i2.setVisibility(View.VISIBLE);
				holder.i3.setVisibility(View.VISIBLE);
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

				holder.i1.setImageBitmap(warDetail.get(0).img);
				holder.i2.setImageBitmap(warDetail.get(1).img);
				holder.i3.setImageBitmap(warDetail.get(2).img);
				holder.i4.setImageBitmap(warDetail.get(3).img);

				holder.i1.setVisibility(View.VISIBLE);
				holder.i2.setVisibility(View.VISIBLE);
				holder.i3.setVisibility(View.VISIBLE);
				holder.i4.setVisibility(View.VISIBLE);

			}else{

				frame1.setBackgroundColor(Color.parseColor("#FFFFFF"));
				frame2.setBackgroundColor(Color.parseColor("#FFFFFF"));
				frame3.setBackgroundColor(Color.parseColor("#FFFFFF"));
				frame4.setBackgroundColor(Color.parseColor("#FFFFFF"));
			}

			convertView.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View arg0) {
					Bundle b = new Bundle();

					try {
						b.putString("categoryId", allCategoryId.get(position));
						b.putString("categoryName", allCategoryName.get(position));
					} catch (Exception e) {
						e.printStackTrace();
					}
					goToActivity(CategoryDetailActivity.class, b);
				}
			});

			holder.edit.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View arg0) {
					try {
						setPrefrenceBool(Constants.isUpdated, true);

						changeCategoryName(allCategoryName.get(position), allCategoryId.get(position));

					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			});
			holder.delete.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View arg0) {
					setPrefrenceBool(Constants.isUpdated, true);

					Utils.createSimpleDialog(context, "DELETE", "Are you sure to Delete This Category ?", "Delete",
							"Cancel", new Utils.Method() {
						public void execute() {
							try {
								dbHelper.setDeleteCategoryFlag(allCategoryId.get(position));
								// IsAdapter = false;

								setAllCategory();
							} catch (Exception e) {
								e.printStackTrace();
							}

						}
					}, new Utils.Method() {
						public void execute() {
						}
					}).show();
				}
			});

			return convertView;

		}

		private class ViewHolder {
			ImageView i1, i2, i3, i4, edit, delete;
			com.constants.CustomTextView mytitle;
			//LinearLayout f1,f2,f3,f4;
		}
	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();


		try{

			if (Utils.checkNetworkConnection(this)) {
				if (getPrefrenceBool(Constants.isSyncing) != null && !getPrefrenceBool(Constants.isSyncing)) {
					System.out.println("CategoryService home===========");
//					Intent in = new Intent(MyClosetActivity.this, CategoryService.class);
//					in.putExtra("userId", ParseUser.getCurrentUser().getObjectId());
					//startService(in);
				}
			}
			if (!isDialog) {
				System.out.println("on?Resume");
				if(!Constants.IS_CAPTURED_PROBLEM_OCCURED) {
					setAllCategory();
				}
				else
				{
					Constants.IS_CAPTURED_PROBLEM_OCCURED=false;
					categorylist.setAdapter(new ListItemAdapterNew());

				}
			}else{

				if(!Constants.IS_CAPTURED_PROBLEM_OCCURED) {
					setAllCategory();
					categorylist.setAdapter(new ListItemAdapterNew());

				}
				else
				{
					Constants.IS_CAPTURED_PROBLEM_OCCURED=false;
					categorylist.setAdapter(new ListItemAdapterNew());

				}
			}

		}catch(Error e){

			e.printStackTrace();

		}

		catch(Exception e){

			e.printStackTrace();

		}


	}

	private void changeCategoryName(final String catName, final String catId) {
		final Dialog changeCategoryNameDialog = addDialog(R.layout.edit_category_name);

		final EditText cateName = (EditText) changeCategoryNameDialog.findViewById(R.id.category_editText);
		cateName.setText(catName);
		CustomTextView changeBut = (CustomTextView) changeCategoryNameDialog.findViewById(R.id.changeBut);
		CustomTextView cancelBut = (CustomTextView) changeCategoryNameDialog.findViewById(R.id.cancelBut);
		changeBut.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {

				if (cateName.getText().toString().equals("")) {
					showToastS("Category name can not be empty!");
					changeCategoryNameDialog.dismiss();
				} else if (allCategoryName.contains(cateName.getText().toString())) {

					showToastS("Category name Already Exists!");
				} else {

					Utils.hideSoftKeyboard(context);
					dbHelper.renameCategoryItem(catId, cateName.getText().toString());
					// IsAdapter = false;

					setAllCategory();
					changeCategoryNameDialog.dismiss();
				}

			}
		});
		cancelBut.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				changeCategoryNameDialog.dismiss();
			}
		});
		changeCategoryNameDialog.show();
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
