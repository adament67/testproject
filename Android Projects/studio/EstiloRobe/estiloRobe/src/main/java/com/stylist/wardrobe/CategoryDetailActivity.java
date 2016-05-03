package com.stylist.wardrobe;

import java.util.ArrayList;
import java.util.Locale;

import com.constants.BaseActivity;
import com.constants.Constants;
import com.constants.CustomTextView;
import com.constants.LocalDatabaseHandler;
import com.constants.Utils;

import com.parse.ParseUser;
//import com.stylist.CategoryService;
import com.stylist.R;
import com.stylist.styleeditor.StyleEditorActivity;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Point;
import android.os.Build;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Spinner;

@SuppressLint("ResourceAsColor")
public class CategoryDetailActivity extends BaseActivity {
	CustomTextView headerName;
	LinearLayout backButtonLayout, nextOrDelButton;;
	GridView categoryPhotosGridView;
	Spinner spinnerSearchBy;
	EditText searchImageEditText;
	ListView myCategListView;
	ImageView nextOrDel;
	String catId, spinnerItem = "Search by";
	ArrayList<ClosetWardrobeDetail> warTemp = new ArrayList<ClosetWardrobeDetail>();;
	ArrayList<ClosetWardrobeDetail> matchWarTemp = new ArrayList<ClosetWardrobeDetail>();;
	String  categoryNamen;
	String[] arrayForSpinner = { "Search by", "Color", "Name", "Favourites", "Dress Code" };
	LocalDatabaseHandler dbHelper;
	int isselected = 0;
	private boolean userIsInteracting;
	public static Activity act;
	int screenWidth, screenHeight;
	int img_width;
	
		boolean isCreate = true;
	boolean isAdapter = true;

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
		setContentView(R.layout.activity_category_detail);

		try{

			backClick();
			dbHelper = new LocalDatabaseHandler(context);
			Constants.uploadOptionClicked = false;
			categoryPhotosGridView = (GridView) findViewById(R.id.categoryPhotosGridView);
			spinnerSearchBy = (Spinner) findViewById(R.id.searchBy);
			searchImageEditText = (EditText) findViewById(R.id.searchImageEditText);
			Utils.setupUI(findViewById(R.id.categoryPhotosGridView), this);
			nextOrDelButton = (LinearLayout) findViewById(R.id.nextOrDelButton);
			nextOrDel = (ImageView) findViewById(R.id.nextOrDel);
			act = this;
			nextOrDelButton.setOnClickListener(this);
			nextOrDelButton.setVisibility(View.VISIBLE);
			nextOrDel.setImageResource(R.drawable.add);
			getScreenDimention(this);

			spinnerSearchBy
					.setAdapter(new CustomSpinnerAdapter(this, R.layout.simple_spinner_text, arrayForSpinner, "Search by"));
			Bundle params = getIntent().getBundleExtra("android.intent.extra.INTENT");
			setHeading(params.getString("categoryName"));
			categoryNamen = params.getString("categoryName");
			catId = params.getString("categoryId");
			img_width=((screenWidth/2)-15);
			searchImageEditText.addTextChangedListener(new TextWatcher() {
				@Override
				public void afterTextChanged(Editable s) {
				}

				@Override
				public void beforeTextChanged(CharSequence s, int start, int count, int after) {
				}

				@Override
				public void onTextChanged(CharSequence s, int start, int before, int count) {
					System.out.println("<<<<<<<<<<<<<<<<<<  spinner item " + spinnerItem);
					if ((!spinnerItem.equalsIgnoreCase("Favourites")) && (!spinnerItem.equalsIgnoreCase("Search by"))) {
						System.out.println("Search byhjghkljhkljh;l");
						if (s.length() != 0) {
							System.out.println("<<<<<<<<<<<<<<<<<<  filter call");
							filter(s.toString());
						} else {
							System.out.println("<<<<<<<<<<<<<<<<<<  adapter call");
							categoryPhotosGridView.setAdapter(new CategoryPhotoAdapter(warTemp));
						}

					}
				}
			});
			spinnerSearchBy.setOnItemSelectedListener(new OnItemSelectedListener() {

				@Override
				public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
					isselected = isselected + 1;
					System.out.println("position==================" + position);
					// if (isselected > 1) {

					spinnerItem = arrayForSpinner[position];


					if (spinnerItem.equalsIgnoreCase("Favourites")) {
						System.out.println("favouritesxdxdv");
						filterFav();
						searchImageEditText.setText("");
					} else {
						searchImageEditText.setText("");
						matchWarTemp.clear();
						matchWarTemp.addAll(warTemp);
						categoryPhotosGridView.setAdapter(new FilterCategoryPhotoAdapter(matchWarTemp));
					}
				}

				@Override
				public void onNothingSelected(AdapterView<?> arg0) { //
				}
			});

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







	@Override
	public void onUserInteraction() {
		super.onUserInteraction();
		userIsInteracting = true;
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		
       if(dbHelper != null){
			
			dbHelper.close();
		}
		try {
			matchWarTemp.clear();
			setPrefrence("categoryId", "");
			setPrefrence("categoryName", "");
			((CategoryPhotoAdapter) categoryPhotosGridView.getAdapter()).notifyDataSetChanged();
		} catch (Exception e) {
			((FilterCategoryPhotoAdapter) categoryPhotosGridView.getAdapter()).notifyDataSetChanged();
		}
	}

	@Override
	protected void onPause() {

		super.onPause();
		if(dbHelper != null){

			dbHelper.close();
		}
	}

	@Override
	protected void onResume() {
		super.onResume();

		try{

			isselected = 0;

			if (!isCreate) {
				try {
					matchWarTemp.clear();
					((FilterCategoryPhotoAdapter) categoryPhotosGridView.getAdapter()).notifyDataSetChanged();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}

			isCreate = false;
			getAllWardrobe();

			searchImageEditText.setText("");
			setPrefrence("categoryId", catId);
			setPrefrence("categoryName", categoryNamen);

		}catch(Error e){

			e.printStackTrace();

		}

		catch(Exception e){

			e.printStackTrace();

		}

	}

	private void getAllWardrobe() {
		Cursor cWardrobe = dbHelper.returnAllWardrobeItem(catId);
		System.out.println("cWardrobebdfsdfbhndrhdr   " + cWardrobe.getCount());
		warTemp.clear();

		BitmapFactory.Options opt=new BitmapFactory.Options();
		opt.inSampleSize=4;
		if (cWardrobe.moveToFirst()) {
			do {

				warTemp.add(new ClosetWardrobeDetail(
						cWardrobe.getString(cWardrobe.getColumnIndex(LocalDatabaseHandler.WARDROBE_ID)),
						cWardrobe.getString(cWardrobe.getColumnIndex(LocalDatabaseHandler.WARDROBE_COLOR)),
						cWardrobe.getString(cWardrobe.getColumnIndex(LocalDatabaseHandler.WARDROBE_NAME)),
						cWardrobe.getString(cWardrobe.getColumnIndex(LocalDatabaseHandler.IMAGE_PATH)),
						cWardrobe.getString(cWardrobe.getColumnIndex(LocalDatabaseHandler.DRESS_CODE)),
						cWardrobe.getString(cWardrobe.getColumnIndex(LocalDatabaseHandler.IS_FAVOURITE)),
						BitmapFactory.decodeFile(cWardrobe.getString(cWardrobe.getColumnIndex(LocalDatabaseHandler.IMAGE_PATH)),opt),
						cWardrobe.getString(cWardrobe.getColumnIndex(LocalDatabaseHandler.BACKGROUND_COLOR))));

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









			//categoryPhotosGridView.setAdapter(new CategoryPhotoAdapter(warTemp));


		}
	}

	@Override
	public void onClick(View v) {
		super.onClick(v);
		switch (v.getId()) {

		case R.id.nextOrDelButton:

			Constants.uploadOptionClicked = true;
			Intent intent = new Intent(context, UploadOptionWardrobeActivity.class);
			startActivity(intent);
			overridePendingTransition(R.anim.slide_in_up, R.anim.slide_out_up);
			break;

		default:
			break;
		}
	}

	public void filter(String charText) {
		matchWarTemp.clear();
		System.out.println("<<<<<<<<<<<<<<<<<<  in filter <<<<<<<<<<<<<<<<");
		String charText11 = searchImageEditText.getText().toString().toLowerCase(Locale.getDefault());
		if (searchImageEditText.getText().toString().length() == 0) {
			matchWarTemp.addAll(warTemp);
		} else {
			System.out.println("<<<<<<<<<<<<<<<<<<  charText11 " + charText11);
			for (ClosetWardrobeDetail photoInfo : warTemp) {
				if (spinnerItem.equalsIgnoreCase("Color")) {
					System.out.println("colour match");
					if (photoInfo.color.toLowerCase(Locale.getDefault()).contains(charText11)) {

						matchWarTemp.add(new ClosetWardrobeDetail(photoInfo.wardrobeItemId, photoInfo.color,
								photoInfo.name, photoInfo.imgPath, photoInfo.dress_code, photoInfo.IsFavourite,null,""));
					}
				} else if (spinnerItem.equalsIgnoreCase("Name")) {
					if (photoInfo.name.toLowerCase(Locale.getDefault()).contains(charText11)) {
						matchWarTemp.add(new ClosetWardrobeDetail(photoInfo.wardrobeItemId, photoInfo.color,
								photoInfo.name, photoInfo.imgPath, photoInfo.dress_code, photoInfo.IsFavourite,null,""));
						System.out.println("photoInfo.photoUrl==" + photoInfo.imgPath);
						System.out.println("photoInfo.name==" + photoInfo.name);
					}
				} else if (spinnerItem.equalsIgnoreCase("Dress Code")) {
					if (photoInfo.dress_code.toLowerCase(Locale.getDefault()).contains(charText11)) {
						matchWarTemp.add(new ClosetWardrobeDetail(photoInfo.wardrobeItemId, photoInfo.color,
								photoInfo.name, photoInfo.imgPath, photoInfo.dress_code, photoInfo.IsFavourite,null,""));
						System.out.println("photoInfo.photoUrl==" + photoInfo.imgPath);
						System.out.println("photoInfo.name==" + photoInfo.name);
					}
				}
			}
			System.out.println("<<<<<<<<<<<<<<<<<<  matchWarTemp " + matchWarTemp.size());

		}
		categoryPhotosGridView.setAdapter(new FilterCategoryPhotoAdapter(matchWarTemp));
	}

	public void filterFav() {
		matchWarTemp.clear();
		for (ClosetWardrobeDetail photoInfo : warTemp) {
			if (photoInfo.IsFavourite.equals("true")) {
				System.out.println("fav match");
				matchWarTemp.add(new ClosetWardrobeDetail(photoInfo.wardrobeItemId, photoInfo.color, photoInfo.name,
						photoInfo.imgPath, photoInfo.dress_code, photoInfo.IsFavourite,null,""));
				System.out.println("photoInfo.photoUrl==" + photoInfo.imgPath);
				System.out.println("photoInfo.name==" + photoInfo.name);

			}
		}

		categoryPhotosGridView.setAdapter(new FilterCategoryPhotoAdapter(matchWarTemp));
	}

	public class CustomSpinnerAdapter extends ArrayAdapter<String> {

		Context context;
		String[] objects;
		String firstElement;
		boolean isFirstTime;

		public CustomSpinnerAdapter(Context context, int textViewResourceId, String[] objects, String defaultText) {
			super(context, textViewResourceId, objects);
			this.context = context;
			this.objects = objects;
			/*
			 * this.isFirstTime = true; setDefaultText(defaultText);
			 */
		}

		@Override
		public View getDropDownView(int position, View convertView, ViewGroup parent) {
			/*
			 * if (isFirstTime) { objects[0] = firstElement; isFirstTime =
			 * false; }
			 */

			return getCustomView(position, convertView, parent);
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			notifyDataSetChanged();

			return getCustomView(position, convertView, parent);
		}

		/*
		 * public void setDefaultText(String defaultText) { this.firstElement =
		 * objects[0]; objects[0] = defaultText; }
		 */

		public View getCustomView(int position, View convertView, ViewGroup parent) {
			LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			View row = inflater.inflate(R.layout.spinner_item, parent, false);
			CustomTextView label = (CustomTextView) row.findViewById(R.id.item);
			label.setText(objects[position]);

			return row;
		}

	}

	public class CategoryPhotoAdapter extends BaseAdapter {
		ArrayList<ClosetWardrobeDetail> PhotosUrl = new ArrayList<ClosetWardrobeDetail>();

		public CategoryPhotoAdapter(ArrayList<ClosetWardrobeDetail> PhotosUrl) {
			this.PhotosUrl = PhotosUrl;
			System.out.println("PhotosUrl.size=======" + PhotosUrl.size());
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
		public View getView(final int position, View convertView, ViewGroup parent) {
			ViewHolder holder;
			if (convertView == null) {
				holder = new ViewHolder();
				convertView = View.inflate(CategoryDetailActivity.this, R.layout.view_zoom_image, null);
				holder.categoryPhoto = (ImageView) convertView.findViewById(R.id.zoomImageView);
				holder.namen = (CustomTextView) convertView.findViewById(R.id.namez);

				convertView.setTag(holder);
			} else {
				holder = (ViewHolder) convertView.getTag();
			}

			holder.namen.setText(PhotosUrl.get(position).name);

			holder.categoryPhoto.setImageBitmap(PhotosUrl.get(position).img);


			String[] arrRgb = (PhotosUrl.get(position).bg_color).split(",");
			holder.categoryPhoto.setBackgroundColor(Color.rgb(Integer.parseInt(arrRgb[0].trim()), Integer.parseInt(arrRgb[1].trim()), Integer.parseInt(arrRgb[2].trim())));

			convertView.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					Utils.hideSoftKeyboard(context);
					searchImageEditText.setText("");

					Intent intent1 = new Intent(context, ItemDetailActivity.class);
					intent1.putExtra("wardrobeItemId", warTemp.get(position).wardrobeItemId);
					intent1.putExtra("wardrobeName", warTemp.get(position).name);
					intent1.putExtra("categoryNamen", categoryNamen);
					startActivity(intent1);
				}
			});

			convertView.setOnLongClickListener(new View.OnLongClickListener() {

				@Override
				public boolean onLongClick(View arg0) {
					// TODO Auto-generated method stub

					try {
						AlertDialog.Builder builder = new AlertDialog.Builder(context);
						builder.setMessage("Are you sure to Delete This Item ?").setTitle("Alert")
								.setPositiveButton("OK", new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog, int id) {
								dialog.cancel();
								dbHelper.deleteWardrobeItem(PhotosUrl.get(position).wardrobeItemId);
							
								getAllWardrobe();
							}
						}).setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog, int id) {
								dialog.cancel();
							}
						});

						AlertDialog alert = builder.create();
						alert.show();

					} catch (Exception e) {
						e.printStackTrace();
					}
					return true;
				}
			});

			return convertView;
		}

		private class ViewHolder {
			ImageView categoryPhoto;
			CustomTextView namen;

		}
	}

	public class FilterCategoryPhotoAdapter extends BaseAdapter {
		ArrayList<ClosetWardrobeDetail> PhotosUrl = new ArrayList<ClosetWardrobeDetail>();

		public FilterCategoryPhotoAdapter(ArrayList<ClosetWardrobeDetail> PhotosUrl) {
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
		public View getView(final int position, View convertView, ViewGroup parent) {
			ViewHolder holder;
			if (convertView == null) {
				holder = new ViewHolder();
				convertView = View.inflate(CategoryDetailActivity.this, R.layout.view_zoom_image, null);
				holder.categoryPhoto = (ImageView) convertView.findViewById(R.id.zoomImageView);
				holder.namen = (CustomTextView) convertView.findViewById(R.id.namez);
//				LinearLayout.LayoutParams layoutParams=new LinearLayout.LayoutParams(img_width,img_width);
//				holder.categoryPhoto.setLayoutParams(layoutParams);
				convertView.setTag(holder);
			} else {
				holder = (ViewHolder) convertView.getTag();
			}

			holder.namen.setText(PhotosUrl.get(position).name);
			holder.categoryPhoto.setImageBitmap(PhotosUrl.get(position).img);


			String[] arrRgb = (PhotosUrl.get(position).bg_color).split(",");
			holder.categoryPhoto.setBackgroundColor(Color.rgb(Integer.parseInt(arrRgb[0].trim()), Integer.parseInt(arrRgb[1].trim()), Integer.parseInt(arrRgb[2].trim())));


			convertView.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					Utils.hideSoftKeyboard(context);
					searchImageEditText.setText("");

					Intent intent1 = new Intent(context, ItemDetailActivity.class);
					intent1.putExtra("wardrobeItemId", PhotosUrl.get(position).wardrobeItemId);
					intent1.putExtra("wardrobeName", PhotosUrl.get(position).name);
					startActivity(intent1);
				}
			});

			/*convertView.setOnLongClickListener(new View.OnLongClickListener() {

				@Override
				public boolean onLongClick(View arg0) {

					try {
						AlertDialog.Builder builder = new AlertDialog.Builder(context);
						builder.setMessage("Are you sure to Delete This Item ?").setTitle("Alert")
								.setPositiveButton("OK", new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog, int id) {
								dialog.cancel();
								dbHelper.deleteWardrobeItem(PhotosUrl.get(position).wardrobeItemId);
							
								getAllWardrobe();
							}
						}).setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog, int id) {
								dialog.cancel();
							}
						});

						AlertDialog alert = builder.create();
						alert.show();

					} catch (Exception e) {
						e.printStackTrace();
					}
					return true;
				}
			});*/

			return convertView;
		}

		private class ViewHolder {
			ImageView categoryPhoto;
			CustomTextView namen;

		}
	}
	
}
