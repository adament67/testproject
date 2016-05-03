package com.stylist.lookbook;

import java.text.SimpleDateFormat;
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
import com.constants.Constants;
import com.constants.CustomTextView;
import com.constants.LocalDatabaseHandler;
import com.constants.Utils;
import com.constants.WebImageLoader;

import com.parse.ParseUser;
//import com.stylist.CategoryService;
import com.stylist.R;
import com.stylist.wardrobe.CategoryDetailActivity.FilterCategoryPhotoAdapter;

public class LookBookActivity extends BaseActivity {
	TextView headerName;
	LinearLayout backButtonLayout;
	ArrayList<String> matchedPhotoUrl = new ArrayList<String>();
	ArrayList<Bitmap> matchedPhotoUrlBitmap = new ArrayList<Bitmap>();

	ArrayList<String> matchedNames = new ArrayList<String>();
	ArrayList<String> photoUrl = new ArrayList<String>();
	ArrayList<Bitmap> photoUrlBitmap = new ArrayList<Bitmap>();
	ArrayList<String> names = new ArrayList<String>();
	GridView myLookBookItemGridView;
	String lookBookItemId, spinnerItem = "Search by";;
	boolean isstylistLook = false;
	Spinner searchBy;
	String[] arrayForSpinner = { "Search by", "Name", "Occasion", "Favourites" };
	EditText searchImageEditText;
	CustomTextView bottomText;
	LocalDatabaseHandler dbHelper;
	int isselected = 0;
	public static LookBookActivity lLookBookActivity;
	LinearLayout container;

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
		setContentView(R.layout.activity_look_book);
		myLookBookItemGridView = (GridView) findViewById(R.id.myLookBookItemGridView);
		setHeading("LOOKBOOK");
		backClick();
		bottomText = (CustomTextView) findViewById(R.id.bottomText);
		searchImageEditText = (EditText) findViewById(R.id.searchImageEditText);
		bottomText.setText("Looks by stylist");
		lLookBookActivity = this;
		container = (LinearLayout) findViewById(R.id.container);
		dbHelper = new LocalDatabaseHandler(context);
		container.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				Utils.hideSoftKeyboard(context);
			}
		});
		bottomText.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				goToActivity(LooksByStylistActivity.class, null);
			}
		});
		searchImageEditText.addTextChangedListener(new TextWatcher() {
			@Override
			public void afterTextChanged(Editable s) {
				// Utils.hideSoftKeyboard(context);

			}

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count, int after) {
			}

			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {
				if (!(spinnerItem.equalsIgnoreCase("Favourites")) && (!(spinnerItem.equalsIgnoreCase("Search by")))) {

					if (s.length() != 0) {
						filter(s.toString());
					} else {
						myLookBookItemGridView.setAdapter(new MyLookBookItemAdapterNew(photoUrlBitmap, names));
					}
				}
			}
		});

		searchBy = (Spinner) findViewById(R.id.searchBy);
		searchBy.setAdapter(new CustomSpinnerAdapter(this, R.layout.spinner_item, arrayForSpinner, "Search By"));

		searchBy.setOnItemSelectedListener(new OnItemSelectedListener() {
			@Override
			public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
				/*
				 * if (position == 0) { spinnerItem = "Name"; }
				 */
				/*
				 * isselected = isselected + 1;
				 * System.out.println("position==================" + position);
				 * if (isselected > 1) {
				 */
				spinnerItem = arrayForSpinner[position];
				matchedPhotoUrl.clear();
				matchedPhotoUrlBitmap.clear();
				matchedNames.clear();
				System.out.println("" + spinnerItem);
				if (spinnerItem.equalsIgnoreCase("Favourites")) {

					for (int i = 0; i < lookBookList.size(); i++) {
						if (lookBookList.get(i).IsFavourite.equalsIgnoreCase("true")) {
							System.out.println("is fa true");
							matchedPhotoUrl.add(photoUrl.get(i));
							matchedPhotoUrlBitmap.add(photoUrlBitmap.get(i));
							matchedNames.add(names.get(i));

						}
					}
					System.out.println("matchedNames=======" + matchedNames.size());
					searchImageEditText.setText("");
					myLookBookItemGridView.setAdapter(new MyLookBookItemAdapterNew(matchedPhotoUrlBitmap, matchedNames));

				} else {
					searchImageEditText.setText("");
					filter(searchImageEditText.getText().toString());
				}

				System.out.println("spinnerItem=on item sel===" + spinnerItem);
				// }
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
		/*if (Utils.checkNetworkConnection(this)) {
			if (getPrefrenceBool(Constants.isSyncing) != null && !getPrefrenceBool(Constants.isSyncing)) {
				System.out.println("CategoryService home===========");
				Intent in = new Intent(this, CategoryService.class);
				in.putExtra("userId", ParseUser.getCurrentUser().getObjectId());
				startService(in);
			}
		}*/
		searchBy.setAdapter(new CustomSpinnerAdapter(this, R.layout.spinner_item, arrayForSpinner, "Search By"));
		searchImageEditText.setText("");
		setListData();

	}

	protected void onPause() {
		super.onPause();
	};

	ArrayList<LookBookDetail> lookBookList = new ArrayList<LookBookDetail>();

	private void setListData() {

		lookBookList.clear();
		photoUrl.clear();
		photoUrlBitmap.clear();
		names.clear();
		Cursor cLookBook = dbHelper.getLookbookItem(ParseUser.getCurrentUser().getObjectId());
		BitmapFactory.Options opt=new BitmapFactory.Options();
		opt.inSampleSize=2;
		if (cLookBook.moveToFirst()) {
			do {
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
							BitmapFactory.decodeFile(cLookBook.getString(cLookBook.getColumnIndex(LocalDatabaseHandler.IMAGE_PATH)), opt)));
					photoUrl.add(cLookBook.getString(cLookBook.getColumnIndex(LocalDatabaseHandler.IMAGE_PATH)));
					photoUrlBitmap.add(BitmapFactory.decodeFile(cLookBook.getString(cLookBook.getColumnIndex(LocalDatabaseHandler.IMAGE_PATH)), opt));
					names.add(cLookBook.getString(cLookBook.getColumnIndex(LocalDatabaseHandler.LOOKBOOK_NAME)));

				}
			} while (cLookBook.moveToNext());
		}

		myLookBookItemGridView.setAdapter(new MyLookBookItemAdapterNew(photoUrlBitmap, names));
	}

	public class MyLookBookItemAdapterNew extends BaseAdapter {
		private LayoutInflater mInflater;
		ArrayList<Bitmap> photoUrlnew = new ArrayList<Bitmap>();
		ArrayList<String> namesnew = new ArrayList<String>();

		MyLookBookItemAdapterNew(ArrayList<Bitmap> photoUrln, ArrayList<String> namesn) {
			this.photoUrlnew = photoUrln;
			this.namesnew = namesn;
		}

		@Override
		public int getCount() {
			return namesnew.size();

		}

		@Override
		public Object getItem(int position) {
			return namesnew.get(position);
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
				convertView = View.inflate(LookBookActivity.this, R.layout.look_book_item_list, null);

				convertView.setOnClickListener(new View.OnClickListener() {

					@Override
					public void onClick(View v) {
						// TODO Auto-generated method stub
						Intent intent = new Intent(context, LookBookItemActivity.class);
						intent.putExtra("position", position);
						startActivity(intent);
					}
				});
				holder.mytitle = (TextView) convertView.findViewById(R.id.title);
				holder.date = (TextView) convertView.findViewById(R.id.date);
				holder.i1 = (ImageView) convertView.findViewById(R.id.imageView1);

				convertView.setTag(holder);
			} else {
				holder = (ViewHolder) convertView.getTag();
			}

			SimpleDateFormat input = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			SimpleDateFormat output = new SimpleDateFormat("MMMM dd, yyyy");

			try {

				holder.date.setText(output.format(input.parse(lookBookList.get(position).createdAt)));

			}catch (Error error)
			{
				error.printStackTrace();

			} catch (Exception e) {
				e.printStackTrace();
			}
			holder.mytitle.setText(namesnew.get(position));
			holder.i1.setImageBitmap(photoUrlnew.get(position));
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
		matchedPhotoUrlBitmap.clear();
		matchedNames.clear();
		System.out.println("in fil");
		String charText11 = searchImageEditText.getText().toString().toLowerCase(Locale.getDefault());
		if (charText11.length() <= 0) {
			matchedPhotoUrl.addAll(photoUrl);
			matchedPhotoUrlBitmap.addAll(photoUrlBitmap);
			matchedNames.addAll(names);
		} else if (spinnerItem.equalsIgnoreCase("name")) {
			for (int i = 0; i < names.size(); i++) {
				if (names.get(i).toLowerCase(Locale.getDefault()).contains(charText11)) {
					matchedPhotoUrl.add(photoUrl.get(i));
					matchedPhotoUrlBitmap.add(photoUrlBitmap.get(i));
					matchedNames.add(names.get(i));
					System.out.println("in name=======" + matchedNames.size());
				}
			}

		} else if (spinnerItem.equalsIgnoreCase("occasion")) {
			for (int i = 0; i < names.size(); i++) {
				if (lookBookList.get(i).ocassion.toLowerCase(Locale.getDefault()).contains(charText11)) {
					matchedPhotoUrl.add(photoUrl.get(i));
					matchedPhotoUrlBitmap.add(photoUrlBitmap.get(i));

					matchedNames.add(names.get(i));
					System.out.println("in occasion=======" + matchedNames.size());
				}
			}

		}
		System.out.println("matchedPhotoUrl=" + matchedPhotoUrl);
		System.out.println("matchedPhotoUrl size=" + matchedPhotoUrl.size());

		System.out.println("matchedNames=" + matchedNames);
		System.out.println("matchedNames size=" + matchedNames.size());

		myLookBookItemGridView.setAdapter(new MyLookBookItemAdapterNew(matchedPhotoUrlBitmap, matchedNames));
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
