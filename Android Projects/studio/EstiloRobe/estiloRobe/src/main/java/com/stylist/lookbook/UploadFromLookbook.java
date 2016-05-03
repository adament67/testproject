package com.stylist.lookbook;

import java.util.ArrayList;
import java.util.Locale;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.constants.BaseActivity;
import com.constants.CustomTextView;
import com.constants.LocalDatabaseHandler;
import com.constants.WebImageLoader;

import com.parse.ParseUser;
import com.stylist.R;
import com.stylist.lookbook.LookBookActivity.MyLookBookItemAdapterNew;

public class UploadFromLookbook extends BaseActivity {
	TextView headerName;
	LinearLayout backButtonLayout;
	ArrayList<String> names = new ArrayList<String>();
	ArrayList<String> ocassions = new ArrayList<String>();
	ArrayList<String> photoUrl = new ArrayList<String>();
	ArrayList<Bitmap> photoUrlBitmap = new ArrayList<Bitmap>();

	ArrayList<String> lookBookId = new ArrayList<String>();
	ArrayList<String> createdAt = new ArrayList<String>();
	ArrayList<String> matchedPhotoUrl = new ArrayList<String>();
	ArrayList<Bitmap> matchedphotoUrlBitmap = new ArrayList<Bitmap>();
	ArrayList<String> matchedNames = new ArrayList<String>();
	GridView myLookBookItemGridView;
	WebImageLoader mWebImageLoader;
	String lookBookItemId, spinnerItem = "Search by";
	Spinner searchBy;
	String[] arrayForSpinner = {"Search by", "Name", "Occasion", "Favourites" };
	EditText searchImageEditText;
	CustomTextView bottomText;
	int isselected = 0;
public static UploadFromLookbook uUploadFromLookbook;
	public void onStart() {
		super.onStart();
		//.onStartSession(this, getResources().getString(R.string.flurryKey));
	}

	public void onStop() {
		super.onStop();
		//.onEndSession(this);
	}

	// ImageView noDataImg;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_look_book);
		myLookBookItemGridView = (GridView) findViewById(R.id.myLookBookItemGridView);
		mWebImageLoader = new WebImageLoader(context);
		setHeading("LOOKBOOK");
		backClick();
		uUploadFromLookbook=this;
		bottomText = (CustomTextView) findViewById(R.id.bottomText);
		searchImageEditText = (EditText) findViewById(R.id.searchImageEditText);
		bottomText.setVisibility(View.GONE);
		dbHelper = new LocalDatabaseHandler(context);
		searchImageEditText.addTextChangedListener(new TextWatcher() {
			@Override
			public void afterTextChanged(Editable s) {
			}

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count, int after) {
			}

			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {

				if (!spinnerItem.equalsIgnoreCase("Favourites") && (!spinnerItem.equalsIgnoreCase("Search by"))) {
					if (s.length() != 0)
						filter(s.toString());
					else
						myLookBookItemGridView.setAdapter(new MyLookBookItemAdapterNew(photoUrlBitmap, names));
					/*
					 * .setAdapter(new CategoryPhotoAdapter( allPhotoUrl));
					 */

				}
			}
		});

		myLookBookItemGridView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
				System.out.println("grid item click");
				System.out.println("lookBookId   " + lookBookId.get(arg2));
				Intent i = new Intent();
				i.putExtra("intent_lookid", lookBookId.get(arg2));
				i.putExtra("intent_imgUrl", photoUrl.get(arg2));
				setResult(RESULT_OK, i);
				finish();
				overridePendingTransition(R.anim.exit_animation_enter_from_right, R.anim.exit_animation_leave_to_right);
			}
		});
		searchBy = (Spinner) findViewById(R.id.searchBy);
		searchBy.setAdapter(new CustomSpinnerAdapter(this, R.layout.spinner_item, arrayForSpinner, "Search By"));

		searchBy.setOnItemSelectedListener(new OnItemSelectedListener() {
			@Override
			public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
//				if (isselected > 1) {

					matchedPhotoUrl.clear();
					matchedNames.clear();
				matchedphotoUrlBitmap.clear();
					System.out.println("" + spinnerItem);
					if (spinnerItem.equalsIgnoreCase("Favourites")) {
						for (int i = 0; i < lookBookList.size(); i++) {
							if (lookBookList.get(i).IsFavourite
									.equalsIgnoreCase("true")) {
								System.out.println("is fa true");
								matchedPhotoUrl.add(photoUrl.get(i));
								matchedphotoUrlBitmap.add(photoUrlBitmap.get(i));
								matchedNames.add(names.get(i));

							}
						}
						System.out.println("matchedNames======="
								+ matchedNames.size());

						myLookBookItemGridView
								.setAdapter(new MyLookBookItemAdapterNew(
										matchedphotoUrlBitmap, matchedNames));

					} else {
						filter(searchImageEditText.getText().toString());
					}

					System.out.println("spinnerItem=on item sel===" + spinnerItem);
//				}
				}
				@Override
				public void onNothingSelected(AdapterView<?> arg0) {
					// TODO Auto-generated method stub
				}
			});


			

	}

	@Override
	protected void onResume() {
		super.onResume();
		setListData();
	}

	LocalDatabaseHandler dbHelper ;
	ArrayList<LookBookDetail> lookBookList = new ArrayList<LookBookDetail>();

	private void setListData() {
		lookBookList.clear();
		photoUrl.clear();
		photoUrlBitmap.clear();
		names.clear();
		lookBookId.clear();
		Cursor cLookBook = dbHelper .getLookbookItem(ParseUser.getCurrentUser().getObjectId());

		if (cLookBook.moveToFirst()) {
			do {	BitmapFactory.Options opt=new BitmapFactory.Options();
				opt.inSampleSize=2;

				String isDeleted = cLookBook.getString(cLookBook.getColumnIndex(LocalDatabaseHandler.IS_DELETED));
				System.out.println("isDeleted=======" + isDeleted);
				if (isDeleted.equalsIgnoreCase("false")) {
					lookBookList.add(new LookBookDetail(
							cLookBook.getInt(cLookBook.getColumnIndex(LocalDatabaseHandler.LOOKBOOK_ID)),
							cLookBook.getString(cLookBook.getColumnIndex(LocalDatabaseHandler.LOOKBOOK_OCASSION)),
							cLookBook.getString(cLookBook.getColumnIndex(LocalDatabaseHandler.LOOKBOOK_NAME)),
							cLookBook.getString(cLookBook.getColumnIndex(LocalDatabaseHandler.IMAGE_PATH)),
							cLookBook.getString(cLookBook.getColumnIndex(LocalDatabaseHandler.CREATED_AT)),

							cLookBook.getString(cLookBook.getColumnIndex(LocalDatabaseHandler.COMMENTS)),
							cLookBook.getString(cLookBook.getColumnIndex(LocalDatabaseHandler.IS_FAVOURITE)),
							cLookBook.getString(cLookBook.getColumnIndex(LocalDatabaseHandler.IS_NEW)),
							cLookBook.getString(cLookBook.getColumnIndex(LocalDatabaseHandler.IS_UPDATED)),
							cLookBook.getString(cLookBook.getColumnIndex(LocalDatabaseHandler.IS_DELETED)),
							BitmapFactory.decodeFile(cLookBook.getString(cLookBook.getColumnIndex(LocalDatabaseHandler.IMAGE_PATH)),opt)));
					photoUrl.add(cLookBook.getString(cLookBook.getColumnIndex(LocalDatabaseHandler.IMAGE_PATH)));
					photoUrlBitmap.add(BitmapFactory.decodeFile(cLookBook.getString(cLookBook.getColumnIndex(LocalDatabaseHandler.IMAGE_PATH)),opt));
					names.add(cLookBook.getString(cLookBook.getColumnIndex(LocalDatabaseHandler.LOOKBOOK_NAME)));
					lookBookId.add(cLookBook.getString(cLookBook.getColumnIndex(LocalDatabaseHandler.LOOKBOOK_ID)));
				}
			} while (cLookBook.moveToNext());
		}

		if (names.size() == 0) {
			myLookBookItemGridView.setVisibility(View.GONE);
		} else {
			myLookBookItemGridView.setVisibility(View.VISIBLE);
			myLookBookItemGridView.setAdapter(new MyLookBookItemAdapterNew(photoUrlBitmap, names));
		}
	}

	public class MyLookBookItemAdapterNew extends BaseAdapter {
		private LayoutInflater mInflater;
		ArrayList<Bitmap> photoUrln = new ArrayList<Bitmap>();
		ArrayList<String> namesn = new ArrayList<String>();

		MyLookBookItemAdapterNew(ArrayList<Bitmap> photoUrln, ArrayList<String> namesn) {
			this.photoUrln = photoUrln;
			this.namesn = namesn;
		}

		@Override
		public int getCount() {
			return namesn.size();

		}

		@Override
		public Object getItem(int position) {
			return namesn.get(position);
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
				convertView = View.inflate(UploadFromLookbook.this, R.layout.look_book_item_list, null);
				holder.mytitle = (TextView) convertView.findViewById(R.id.title);
				holder.date = (TextView) convertView.findViewById(R.id.date);
				holder.i1 = (ImageView) convertView.findViewById(R.id.imageView1);

				convertView.setTag(holder);
			} else {
				holder = (ViewHolder) convertView.getTag();
			}

			holder.date.setText(lookBookList.get(position).createdAt);

			holder.mytitle.setText(namesn.get(position));
			holder.i1.setImageBitmap(photoUrln.get(position));
			return convertView;
		}

		private class ViewHolder {
			ImageView i1/* , imageViewDel */;
			TextView mytitle;
			TextView date;
		}
	}

	public void filter(String charText) {
		matchedPhotoUrl.clear();
		matchedNames.clear();
		String charText11 = searchImageEditText.getText().toString().toLowerCase(Locale.getDefault());
		System.out.println("charText11==============" + charText11);
		if (searchImageEditText.getText().toString().length() == 0) {
			matchedPhotoUrl.addAll(photoUrl);
			matchedphotoUrlBitmap.addAll(photoUrlBitmap);

			matchedNames.addAll(names);
		} else {

			/*
			 * if(spinnerItem.equalsIgnoreCase("Name")) {
			 */
			for (int i = 0; i < names.size(); i++) {
				if (names.get(i).toLowerCase(Locale.getDefault()).contains(charText11)) {
					matchedphotoUrlBitmap.add(photoUrlBitmap.get(i));
					matchedNames.add(names.get(i));
					System.out.println("spinnerItem=======" + spinnerItem);
				}
			}
			/* } */
		}
		System.out.println("matchedPhotoUrl=" + matchedPhotoUrl);
		System.out.println("matchedNames=" + matchedNames);
		myLookBookItemGridView.setAdapter(new MyLookBookItemAdapterNew(matchedphotoUrlBitmap, matchedNames));
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
		/*	this.isFirstTime = true;
			setDefaultText(defaultText);*/
		}

		@Override
		public View getDropDownView(int position, View convertView, ViewGroup parent) {
			/*if (isFirstTime) {
				objects[0] = firstElement;
				isFirstTime = false;
			}*/
			return getCustomView(position, convertView, parent);
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			notifyDataSetChanged();
			return getCustomView(position, convertView, parent);
		}
/*
		public void setDefaultText(String defaultText) {
			this.firstElement = objects[0];
			objects[0] = defaultText;
		}
*/
		public View getCustomView(int position, View convertView, ViewGroup parent) {

			LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			View row = inflater.inflate(R.layout.spinner_item, parent, false);
			TextView label = (TextView) row.findViewById(R.id.item);
			label.setText(objects[position]);

			return row;
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
