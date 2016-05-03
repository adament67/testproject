package com.stylist.styleeditor;

import java.util.ArrayList;
import java.util.HashMap;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import com.stylist.wardrobe.*;
import com.constants.BaseActivity;
import com.constants.LocalDatabaseHandler;
import com.constants.Utils;
import com.constants.WebImageLoader;

import com.parse.ParseUser;
import com.stylist.R;
import com.stylist.wardrobe.CategoryDetailActivity.FilterCategoryPhotoAdapter;


public class SelectWardrobeCategoryDetail extends BaseActivity {

	LinearLayout searchByLinearLayout;
	GridView categoryPhotosGridView;
	 public static Activity ctx;

	ArrayList<String> searchType = new ArrayList<String>();
	ArrayList<String> photoUrl = new ArrayList<String>();
	Spinner spinnerSearchBy;
	String CategoryId,categoryNamen;


	ArrayList<ClosetWardrobeDetail> warTemp = new ArrayList<ClosetWardrobeDetail>();;

	ArrayList<ClosetWardrobeDetail> matchWarTemp = new ArrayList<ClosetWardrobeDetail>();
	public static ArrayList<String> names = new ArrayList<String>();

	WebImageLoader mWebImageLoader;

	boolean isCreate = true;
	boolean isAdapter = true;


	
	public void onStart() {
		super.onStart();
		//.onStartSession(this,
				//getResources().getString(R.string.flurryKey));
	}

	public void onStop() {
		super.onStop();
		//.onEndSession(this);
	}
	
	
	

	LocalDatabaseHandler dbHelper;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_category_detail);
		searchByLinearLayout=(LinearLayout)findViewById(R.id.searchByLinearLayout);
		searchByLinearLayout.setVisibility(View.GONE);
		categoryPhotosGridView = (GridView) findViewById(R.id.categoryPhotosGridView);
		spinnerSearchBy = (Spinner) findViewById(R.id.searchBy);
		ctx=this;
		searchType.add("Colour");
		searchType.add("Name");
		searchType.add("Favourite");
		dbHelper = new LocalDatabaseHandler(context);

		spinnerSearchBy.setAdapter(new SearchTypeAdapter(SelectWardrobeCategoryDetail.this, R.id.item, searchType));
		spinnerSearchBy.setPrompt("Find By");
		Bundle	params = getIntent().getBundleExtra("android.intent.extra.INTENT");
		
		CategoryId = params.getString("categoryId");
		categoryNamen = params.getString("categoryName");
		System.out.println("CategoryId    " + CategoryId);
		Utils.setupUI(findViewById(R.id.container), this);
		mWebImageLoader = new WebImageLoader(context);
	
		setHeading("SELECT ITEM");
		backClick();
		ImageView	nextOrDel = (ImageView) findViewById(R.id.nextOrDel);
		nextOrDel.setVisibility(View.INVISIBLE);
		categoryPhotosGridView
				.setOnItemClickListener(new OnItemClickListener() {
					public void onItemClick(AdapterView<?> parent, View v,
							int position, long id) {
						setResult(RESULT_OK,
								new Intent().putExtra("intent_data", warTemp
										.get(position).imgPath));
						
						
						finish();
						overridePendingTransition(R.anim.exit_animation_enter_from_right,
								 R.anim.exit_animation_leave_to_right);
					}
				});

	}

	@Override
	protected void onPause() {
		super.onPause(); 
	}

	@Override
	protected void onResume() {
		super.onResume();
		if (!isCreate) {
			try {
				matchWarTemp.clear();
				((CategoryPhotoAdapter) categoryPhotosGridView.getAdapter())
						.notifyDataSetChanged();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		isCreate = false;
		getAllWardrobe();

		setPrefrence("categoryId", CategoryId);
		setPrefrence("categoryName", categoryNamen);
	}

	private void getAllWardrobe() {
		Cursor cWardrobe = dbHelper.returnAllWardrobeItem(CategoryId);

		System.out.println("cWardrobe   " + cWardrobe.getCount());
		BitmapFactory.Options opt=new BitmapFactory.Options();
		opt.inSampleSize=4;

		warTemp.clear();
		if (cWardrobe.moveToFirst()) {

			do {

				warTemp.add(new ClosetWardrobeDetail(
						cWardrobe
								.getString(cWardrobe
										.getColumnIndex(LocalDatabaseHandler.WARDROBE_ID)),
						cWardrobe.getString(cWardrobe
								.getColumnIndex(LocalDatabaseHandler.WARDROBE_COLOR)),
						cWardrobe.getString(cWardrobe
								.getColumnIndex(LocalDatabaseHandler.WARDROBE_NAME)),
						cWardrobe.getString(cWardrobe
								.getColumnIndex(LocalDatabaseHandler.IMAGE_PATH)),
						cWardrobe.getString(cWardrobe
								.getColumnIndex(LocalDatabaseHandler.DRESS_CODE)),
						cWardrobe.getString(cWardrobe
								.getColumnIndex(LocalDatabaseHandler.IS_FAVOURITE)),BitmapFactory.decodeFile(cWardrobe.getString(cWardrobe.getColumnIndex(LocalDatabaseHandler.IMAGE_PATH)), opt)
				,cWardrobe
						.getString(cWardrobe
								.getColumnIndex(LocalDatabaseHandler.BACKGROUND_COLOR))));

			} while (cWardrobe.moveToNext());
			if (isAdapter)
				categoryPhotosGridView
						.setAdapter(new CategoryPhotoAdapter(warTemp));
			else {
				isAdapter = true;
				try {
					((CategoryPhotoAdapter) categoryPhotosGridView.getAdapter())
					.notifyDataSetChanged();
				} catch (Exception e) {
					((FilterCategoryPhotoAdapter) categoryPhotosGridView.getAdapter())
					.notifyDataSetChanged();
				}
			}
		}
	}
	
	
	public class SearchTypeAdapter extends ArrayAdapter<String> {
		ArrayList<String> searchType;

		public SearchTypeAdapter(Context con, int textview,
				ArrayList<String> searchType) {
			super(con, textview, searchType);
			this.searchType = searchType;
		}

		@Override
		public View getDropDownView(int position, View cnvtView, ViewGroup prnt) {
			return getCustomView(position, cnvtView, prnt);
		}

		@Override
		public View getView(int pos, View cnvtView, ViewGroup prnt) {
			return getCustomView(pos, cnvtView, prnt);
		}

		@SuppressLint("ResourceAsColor")
		public View getCustomView(int position, View convertView,
				ViewGroup parent) {
			LayoutInflater inflater = getLayoutInflater();
			View mySpinner = inflater.inflate(R.layout.spinner_item, parent,
					false);
			com.constants.CustomTextView main_text = (com.constants.CustomTextView) mySpinner.findViewById(R.id.item);
			main_text.setTextColor(Color.parseColor("#B03E3E"));
			main_text.setText(searchType.get(position));
			return mySpinner;
		}

	}


	
	
	
public class CategoryPhotoAdapter extends BaseAdapter {
	ArrayList<ClosetWardrobeDetail> PhotosUrl = new ArrayList<ClosetWardrobeDetail>();

	public CategoryPhotoAdapter(ArrayList<ClosetWardrobeDetail> PhotosUrl) {
		this.PhotosUrl = PhotosUrl;
	}

	@Override
	public int getCount() {
		return PhotosUrl.size();
	}

	@Override
	public Object getItem(int position) {
		return PhotosUrl.get(position);
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
			convertView = View.inflate(SelectWardrobeCategoryDetail.this,
					R.layout.view_zoom_image, null);
			holder.categoryPhoto = (ImageView) convertView
					.findViewById(R.id.zoomImageView);
			holder.namen = (com.constants.CustomTextView) convertView
					.findViewById(R.id.namez);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		
		holder.namen.setText(PhotosUrl.get(position).name);
		holder.categoryPhoto.setImageBitmap(PhotosUrl.get(position).img);


		String[] arrRgb = (PhotosUrl.get(position).bg_color).split(",");


		if(arrRgb.length==3) {
			holder.categoryPhoto.setBackgroundColor(Color.rgb(Integer.parseInt(arrRgb[0].trim()), Integer.parseInt(arrRgb[1].trim()), Integer.parseInt(arrRgb[2].trim())));
		}
		else
		{
			Log.v("SET_RGB1",  (PhotosUrl.get(position).bg_color));
		}

		return convertView;
	}

	private class ViewHolder {
		ImageView categoryPhoto;
		com.constants.CustomTextView namen;

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
