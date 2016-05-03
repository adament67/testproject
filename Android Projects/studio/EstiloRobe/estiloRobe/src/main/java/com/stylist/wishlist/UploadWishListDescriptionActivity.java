package com.stylist.wishlist;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

import android.app.Activity;
import android.app.Dialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore.Images;
import android.provider.MediaStore.Images.Media;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.constants.BaseActivity;
import com.constants.Constants;
import com.constants.CustomTextView;
import com.constants.LocalDatabaseHandler;
import com.constants.Utils;

import com.parse.ParseUser;
import com.stylist.CroppingActivity;
import com.stylist.R;

public class UploadWishListDescriptionActivity extends BaseActivity {
	RelativeLayout container;
	// Spinner spinnerCategoryList;
	com.constants.CustomTextView saveText;
	int catSelPos = -1;
	String imgPath;
	Bitmap bitmap2;
	EditText brandText, costText, commentsText;
	int fromGallery = 0;
	LocalDatabaseHandler dbHelper;
	int isselected = 0;
	CustomTextView categoryText;
	public static Activity act;
	String rgb;
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

		requestWindowFeature(Window.FEATURE_NO_TITLE);

		setContentView(R.layout.activity_upload_wish_list_description);
		container = (RelativeLayout) findViewById(R.id.container);
		// container.setOnClickListener(this);
		// spinnerCategoryList = (Spinner) findViewById(R.id.categoryList);
		saveText = (com.constants.CustomTextView) findViewById(R.id.bottomText);
		act = this;
		saveText.setText("Save To WishList");
		saveText.setOnClickListener(this);
//		handler1 = new LocalDatabaseHandler(context);
//		handler1.open();

		dbHelper = new LocalDatabaseHandler(this);
		
		setAllCategory();
		categoryText = (CustomTextView) findViewById(R.id.categoryText);
		categoryText.setOnClickListener(this);
		categoryText.requestFocus();
		brandText = (EditText) findViewById(R.id.brandText);
		costText = (EditText) findViewById(R.id.costText);
		commentsText = (EditText) findViewById(R.id.commentsText);
		imgPath = getIntent().getStringExtra("imgPath");
		fromGallery = getIntent().getIntExtra("fromGallery", 0);

		try {
			if (fromGallery == 1) {
				bitmap2 = Utils.orientedBitmap(imgPath);
			} else {
				bitmap2 = Utils.orientedBitmap(imgPath);
			}

			HashMap<String, String> rgbData = Utils.getAvgRGB(bitmap2);
			rgb = rgbData.get("r")+","+rgbData.get("g")+","+rgbData.get("b");

			saveOnShare();

		} catch (Exception e) {
			// TODO: handle exception
		}

		/*
		 * spinnerCategoryList.setOnItemSelectedListener(new
		 * OnItemSelectedListener() {
		 * 
		 * @Override public void onItemSelected(AdapterView<?> parentView, View
		 * selectedItemView, int position, long id) {
		 * 
		 * isselected = isselected + 1;
		 * System.out.println("isselected=============" + isselected); if
		 * (isselected > 1) { System.out.println("catSelPos=============" +
		 * catSelPos); System.out.println("position=============" + position);
		 * 
		 * catSelPos = position; } }
		 * 
		 * @Override public void onNothingSelected(AdapterView<?> arg0) { //
		 * TODO Auto-generated method stub
		 * 
		 * } });
		 */
	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		isselected = 0;
		saveText.setClickable(true);

	}


	@Override
	protected void onPause() {
		super.onPause();
		if(dbHelper != null){

			dbHelper.close();
		}
	}

	@Override
	public void onBackPressed() {
		// TODO Auto-generated method stub
		super.onBackPressed();
		overridePendingTransition(R.anim.slide_in_up, R.anim.slide_out_down);
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.bottomText:
			saveText.setClickable(false);
			System.out.println("bottomText===============" + catSelPos);

			if (catSelPos == -1) {
				showToastS("Plaese select a category");
				saveText.setClickable(true);
			} else if (brandText.getText().toString().equals("")) {
				showToastS("Please enter brand");
				saveText.setClickable(true);
			} else {
				setPrefrenceBool(Constants.isUpdated, true);
				uploadWishList();
			}
			break;
		case R.id.categoryText:
			categoryDialog();

			break;
		/*
		 * case R.id.container: finish();
		 * overridePendingTransition(R.anim.slide_in_up, R.anim.slide_out_down);
		 * break;
		 */
		}
	}

	ArrayList<String> allCategoryNames = new ArrayList<String>();
	ArrayList<String> allCategoryIds = new ArrayList<String>();

	private void setAllCategory() { try {


		allCategoryNames.clear();
		allCategoryIds.clear();
		Utils.write("cursor>>>>>>>>>>>>>>>>>>>>>>>>>> userId" + ParseUser.getCurrentUser().getObjectId());
		Cursor c = dbHelper.returnAllCategory(ParseUser.getCurrentUser().getObjectId());

		Utils.write("cursor>>>>>>>>>>>>>>>>>>>>>>>>>>"+c);
		Utils.write("cursor>>>>>>>>>>>>>>>>>>>>>>>>>>"+c.getCount());

		if (c != null && c.getCount() > 0) {
			c.moveToFirst();
			while (c.isAfterLast() == false) {
				// String name = ;
				allCategoryNames.add(c.getString(c.getColumnIndex(LocalDatabaseHandler.CUSTOM_CATEGORY_NAME)));
				allCategoryIds.add(c.getString(c.getColumnIndex(LocalDatabaseHandler.CATEGORY_ID)));
				System.out.println(
						"name=========" + c.getString(c.getColumnIndex(LocalDatabaseHandler.CUSTOM_CATEGORY_NAME)));
				System.out.println("id=========" + c.getString(c.getColumnIndex(LocalDatabaseHandler.CATEGORY_ID)));
				c.moveToNext();
			}
			String[] arrayForSpinner = new String[allCategoryNames.size()];
			for (int i = 0; i < allCategoryNames.size(); i++) {
				arrayForSpinner[i] = allCategoryNames.get(i);

			}
			/*
			 * spinnerCategoryList.setAdapter(new CustomSpinnerAdapter111(this,
			 * R.layout.spinner_with_white_text, arrayForSpinner,
			 * "Select Category"));
			 */
		}
	}catch(Exception e){
		e.printStackTrace();
	}
	catch(Error e){
		e.printStackTrace();
	}
	}

	public void uploadWishList()  {

		String wishPath=Utils.getOutputMediaFileWish().getAbsolutePath();
		try {
			copyFile(new File(imgPath),new File(wishPath));
		} catch (IOException e) {
			e.printStackTrace();
		}


		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
		long lng = dbHelper.insertWishlistItem(ParseUser.getCurrentUser().getObjectId(), brandText.getText().toString(),
				costText.getText().toString(), commentsText.getText().toString(), allCategoryIds.get(catSelPos),
				wishPath, format.format(new Date()),rgb);
		if (lng != -1) {

			checkAndCloseActivity(UploadWishListActivity.act);
			checkAndCloseActivity(UploadOptionWishListActivity.act);
			checkAndCloseActivity(CroppingActivity.cCroppingActivity);
			showToastS("Item has been saved in you wishlist");
			saveText.setClickable(true);
			finish();
		}
//		File tempDir = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES), Constants.IMAGE_DIRECTORY_NAME);
//		Log.v("File_path", tempDir.getPath());
//		DeleteRecursive(tempDir);
		//sendBroadcast(new Intent(Intent.ACTION_MEDIA_MOUNTED, Uri.parse("file://" + Environment.getExternalStorageDirectory())));

	}

	void DeleteRecursive(File fileOrDirectory) {

		if (fileOrDirectory.isDirectory())
			for (File child : fileOrDirectory.listFiles())
				DeleteRecursive(child);

		fileOrDirectory.delete();
		MediaScannerConnection.scanFile(context,
				new String[]{fileOrDirectory.getAbsolutePath()}, null, null);
	}

	/*
	 * public class CustomSpinnerAdapter111 extends ArrayAdapter<String> {
	 * 
	 * Context context; String[] objects; String firstElement; boolean
	 * isFirstTime;
	 * 
	 * public CustomSpinnerAdapter111(Context context, int textViewResourceId,
	 * String[] objects, String defaultText) { super(context,
	 * textViewResourceId, objects); this.context = context; this.objects =
	 * objects; this.isFirstTime = true; setDefaultText(defaultText); }
	 * 
	 * @Override public View getDropDownView(int position, View convertView,
	 * ViewGroup parent) { if (isFirstTime) { objects[0] = firstElement;
	 * isFirstTime = false; } return getCustomView(position, convertView,
	 * parent); }
	 * 
	 * @Override public View getView(int position, View convertView, ViewGroup
	 * parent) { notifyDataSetChanged(); return getCustomView(position,
	 * convertView, parent); }
	 * 
	 * public void setDefaultText(String defaultText) { this.firstElement =
	 * objects[0]; objects[0] = defaultText; }
	 * 
	 * public View getCustomView(int position, View convertView, ViewGroup
	 * parent) {
	 * 
	 * LayoutInflater inflater = (LayoutInflater)
	 * context.getSystemService(Context.LAYOUT_INFLATER_SERVICE); View row =
	 * inflater.inflate(R.layout.spinner_with_white_text, parent, false);
	 * com.constants.CustomTextView label = (com.constants.CustomTextView)
	 * row.findViewById(R.id.itemtext); label.setText(objects[position]);
	 * 
	 * return row; }
	 * 
	 * }
	 */

	public void saveOnShare() {
		String imageName;

		if (bitmap2 != null && !bitmap2.isRecycled()) {
			/*
			 * FileOutputStream out = null; File imageFile =
			 * Utils.getOutputMediaFileWish(); try { out = new
			 * FileOutputStream(imageFile); Boolean imageSaved =
			 * bitmap2.compress( Bitmap.CompressFormat.PNG, 100, out);
			 * 
			 * } catch (Exception e) {
			 * 
			 * } finally { if (out != null) { try { out.flush(); } catch
			 * (IOException e) { // TODO Auto-generated catch block
			 * e.printStackTrace(); } try { out.close(); } catch (IOException e)
			 * { // TODO Auto-generated catch block e.printStackTrace(); } } }
			 */
			try {
				imageName = "IMG_" + System.currentTimeMillis() + ".png";
				ContentValues values = new ContentValues(3);
				values.put(Images.Media.TITLE, imageName);
				values.put(Images.Media.MIME_TYPE, "image/png");
				// values.put("_data", imageFile.getAbsolutePath());
				values.put("_data", imgPath);
				getContentResolver().insert(Media.EXTERNAL_CONTENT_URI, values);
				// imgPath = imageFile.getAbsolutePath();
				System.out.println("imgPath============" + imgPath);
			} catch (Exception e) {
			}
		}

	}

	private void categoryDialog() {
		LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		View viewList = inflater.inflate(R.layout.category_list_dialog, null);
		final Dialog categoryDialog = new Dialog(this);
		categoryDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
		categoryDialog.setContentView(viewList);
		categoryDialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
		ListView lvForDialog = (ListView) viewList.findViewById(R.id.listView_dialog);
		Value_Adapter_Dialog_Array valueAdapter = new Value_Adapter_Dialog_Array(context, allCategoryNames);
		lvForDialog.setAdapter(valueAdapter);
		lvForDialog.setOnItemClickListener(new AdapterView.OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
				// TODO Auto-generated method stub
				// arg0.setBackgroundColor(getResources().getColor(R.color.headenewRed));
				System.out.println("on item click");
				catSelPos = arg2;
				categoryText.setText(allCategoryNames.get(catSelPos));
				categoryDialog.dismiss();
			}
		});
		categoryDialog.show();
	}

	class Value_Adapter_Dialog_Array extends BaseAdapter {
		// private HashMap<String, String> mStringList;
		// private ArrayList<JSONObject> mStringFilterList;
		private LayoutInflater mInflater;
		ArrayList<String> dataRef;

		// private ValueFilter valueFilter;
		public Value_Adapter_Dialog_Array(Context context, ArrayList<String> data) {
			// this.mStringFilterList = mStringList;
			mInflater = LayoutInflater.from(context);
			// getFilter();
			dataRef = data;
		}

		// How many items are in the data set represented by this Adapter.
		@Override
		public int getCount() {
			return dataRef.size();
		}

		// Get the data item associated with the specified position in the data
		// set.
		@Override
		public Object getItem(int position) {
			return dataRef.get(position);
		}

		// Get the row id associated with the specified position in the list.
		@Override
		public long getItemId(int position) {
			return position;
		}

		// Get a View that displays the data at the specified position in the
		// data
		// set.
		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			Holder viewHolder;
			if (convertView == null) {
				viewHolder = new Holder();
				convertView = mInflater.inflate(R.layout.spinner_item_view, null);
				viewHolder.tvSpinnerItem = (TextView) convertView.findViewById(R.id.tvSpinnerItem);
				convertView.setTag(viewHolder);
			} else {
				viewHolder = (Holder) convertView.getTag();
			}
			viewHolder.tvSpinnerItem.setText(dataRef.get(position));
			return convertView;
		}

		class Holder {
			TextView tvSpinnerItem;
		}
	}
	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
//		File tempDir = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES), Constants.IMAGE_DIRECTORY_NAME+"/"+Constants.IMAGE_TEMP_CROPED);
//		Log.v("File_path", tempDir.getPath());
//		DeleteRecursive(tempDir);
		//sendBroadcast(new Intent(Intent.ACTION_MEDIA_MOUNTED, Uri.parse("file://" + Environment.getExternalStorageDirectory())));

		if(dbHelper != null){
			
			dbHelper.close();
		}
	}
}
