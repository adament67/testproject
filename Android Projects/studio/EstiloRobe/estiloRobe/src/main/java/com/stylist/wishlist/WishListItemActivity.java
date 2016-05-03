package com.stylist.wishlist;

import java.util.ArrayList;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.constants.BaseActivity;
import com.constants.LocalDatabaseHandler;
import com.constants.Utils;

import com.stylist.R;

public class WishListItemActivity extends BaseActivity {
	String categoryID;
	com.constants.CustomTextView headerName;
	LinearLayout backButtonLayout;
	GridView wishListPhotosGridView;
	LocalDatabaseHandler dbHelper = new LocalDatabaseHandler(context);
	String CategoryId, categoryName;
	ArrayList<WishListDetail> wishData = new ArrayList<WishListDetail>();
	public static Activity act;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_wish_list_item);
		backClick();
		wishListPhotosGridView = (GridView) findViewById(R.id.wishListPhotosGridView);
		/*
		 * categoryName = getIntent().getStringExtra("categoryName");
		 */
		act=this;
		Bundle params = getIntent().getBundleExtra("android.intent.extra.INTENT");
		setHeading(params.getString("categoryName"));
		categoryID = params.getString("categoryId");
		categoryName = params.getString("categoryName");
	}

	public void onStart() {
		super.onStart();
		//.onStartSession(this, getResources().getString(R.string.flurryKey));
	}

	public void onStop() {
		super.onStop();
		//.onEndSession(this);
	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		getData();
	}

	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
		if(dbHelper != null){

			dbHelper.close();
		}
	}

	private void getData() {
		wishData.clear();
		System.out.println("categoryID   " + categoryID);
		BitmapFactory.Options opt=new BitmapFactory.Options();
		opt.inSampleSize=4;
		Cursor cWishlist = dbHelper.returnAllWishlistItem(categoryID);
		if (cWishlist.moveToFirst()) {
			do {
				if ((cWishlist.getString(cWishlist.getColumnIndex(LocalDatabaseHandler.IS_DELETED))).equalsIgnoreCase("false")) {


					Utils.write("8989898989==="+cWishlist.getString(cWishlist.getColumnIndex(LocalDatabaseHandler.WISHLIST_ID)));
					Utils.write("8989898989==="+cWishlist.getString(cWishlist.getColumnIndex(LocalDatabaseHandler.WISHLIST_BRAND)));
					Utils.write("8989898989==="+cWishlist.getString(cWishlist.getColumnIndex(LocalDatabaseHandler.WISHLIST_COST_PRICE)));
					Utils.write("8989898989==="+cWishlist.getString(cWishlist.getColumnIndex(LocalDatabaseHandler.IMAGE_PATH)));
					Utils.write("8989898989==="+cWishlist.getString(cWishlist.getColumnIndex(LocalDatabaseHandler.WISHLIST_COMMENTS)));
					Utils.write("8989898989==="+cWishlist.getString(cWishlist.getColumnIndex(LocalDatabaseHandler.IS_FAVOURITE)));



					wishData.add(new WishListDetail(
							cWishlist.getString(cWishlist.getColumnIndex(LocalDatabaseHandler.WISHLIST_ID)),
							cWishlist.getString(cWishlist.getColumnIndex(LocalDatabaseHandler.WISHLIST_BRAND)),
							cWishlist.getString(cWishlist.getColumnIndex(LocalDatabaseHandler.WISHLIST_COST_PRICE)),
							cWishlist.getString(cWishlist.getColumnIndex(LocalDatabaseHandler.IMAGE_PATH)),
							cWishlist.getString(cWishlist.getColumnIndex(LocalDatabaseHandler.WISHLIST_COMMENTS)),
							cWishlist.getString(cWishlist.getColumnIndex(LocalDatabaseHandler.IS_FAVOURITE)),
							cWishlist.getString(cWishlist.getColumnIndex(LocalDatabaseHandler.IS_NEW)),
							cWishlist.getString(cWishlist.getColumnIndex(LocalDatabaseHandler.IS_UPDATED)),
							cWishlist.getString(cWishlist.getColumnIndex(LocalDatabaseHandler.IS_DELETED))
							,BitmapFactory.decodeFile(cWishlist.getString(cWishlist.getColumnIndex(LocalDatabaseHandler.IMAGE_PATH)),opt),
							cWishlist.getString(cWishlist.getColumnIndex(LocalDatabaseHandler.BACKGROUND_COLOR))));
				}
			} while (cWishlist.moveToNext());
		}
		wishListPhotosGridView.setAdapter(new WishListPhotoAdapter(wishData));

		Utils.write("utilllllllllll===="+wishData.toString());

	}

	public class WishListPhotoAdapter extends BaseAdapter {
		private LayoutInflater mInflater;

		private ArrayList<WishListDetail> dataSet;

		public WishListPhotoAdapter(ArrayList<WishListDetail> dataSet){

			WishListPhotoAdapter.this.dataSet = dataSet;

		}



		@Override
		public int getCount() {
			return dataSet.size();
		}

		@Override
		public Object getItem(int position) {
			return dataSet.get(position);
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
				convertView = View.inflate(context, R.layout.view_zoom_image, null);

				holder.categoryPhoto = (ImageView) convertView.findViewById(R.id.zoomImageView);
				holder.brand = (com.constants.CustomTextView) convertView.findViewById(R.id.namez);
				convertView.setTag(holder);
			} else {
				holder = (ViewHolder) convertView.getTag();
			}
			holder.brand.setText(dataSet.get(position).brand);

			convertView.setOnClickListener(new View.OnClickListener() {

				@Override
				public void onClick(View v) {
					System.out.println("grid click");
					System.out.println("grid categoryID===========" + categoryID);
					Intent intent1 = new Intent(WishListItemActivity.this, WishListItemDetail.class);
					intent1.putExtra("wishListItemId", dataSet.get(position).wishlistItemId);
					System.out.println("wishListItemId=================" + dataSet.get(position).wishlistItemId);
					intent1.putExtra("categoryName", categoryName);
					startActivity(intent1);

				}
			});


			String[] arrRgb = (dataSet.get(position).bg_color).split(",");
			holder.categoryPhoto.setBackgroundColor(Color.rgb(Integer.parseInt(arrRgb[0].trim()), Integer.parseInt(arrRgb[1].trim()), Integer.parseInt(arrRgb[2].trim())));


			holder.categoryPhoto.setImageBitmap(dataSet.get(position).imgBitmap);
			return convertView;
		}

		private class ViewHolder {
			ImageView categoryPhoto;
			com.constants.CustomTextView brand;

		}
	}
	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		
		if(dbHelper != null){
			
			dbHelper.close();
		}
	}}
