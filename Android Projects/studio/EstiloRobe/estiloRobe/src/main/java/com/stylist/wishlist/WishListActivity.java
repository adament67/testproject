package com.stylist.wishlist;

import java.util.ArrayList;

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
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;

import com.constants.BaseActivity;
import com.constants.Constants;
import com.constants.LocalDatabaseHandler;
import com.constants.Utils;

import com.parse.ParseUser;
//import com.stylist.CategoryService;
import com.stylist.R;
import com.stylist.wardrobe.CategoryDetailActivity;

public class WishListActivity extends BaseActivity {
	RelativeLayout topRelLayout;
	LinearLayout backButtonLayout, nextOrDelButtonLayout;
	ImageView nextOrDelImg;
	Boolean isUplodClicked = false;
	int W_FROM_GALLERY = 0;
	int W_FRONT_CAMERA = 0;
	int firstTime = 1;
	int categoryCount = 0;
	public static Activity act;



	com.constants.CustomTextView uploadCameraRoll, uploadcapture, headerName, backTextView;
	ListView myWishListtItemListView;
	LocalDatabaseHandler dbHelper;
	ArrayList<String> allCategoryName = new ArrayList<String>();
	ArrayList<String> allCategoryId = new ArrayList<String>();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_wish_list);
		setHeading("WISHLIST");
		backClick();
		act=this;
		topRelLayout = (RelativeLayout) findViewById(R.id.topRelLayout);
		topRelLayout.setOnClickListener(this);
		nextOrDelButtonLayout = (LinearLayout) findViewById(R.id.nextOrDelButton);
		nextOrDelImg = (ImageView) findViewById(R.id.nextOrDel);
		backButtonLayout = (LinearLayout) findViewById(R.id.backButtonLayout);
		nextOrDelImg.setImageResource(R.drawable.add);
		nextOrDelButtonLayout.setOnClickListener(this);
		backButtonLayout.setOnClickListener(this);
		myWishListtItemListView = (ListView) findViewById(R.id.myWishListItemListView);



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


	@Override
	protected void onResume() {
		// TODO Auto-generated method stub

		Log.v("232323", "Resume wishlist");
		super.onResume();
		dbHelper = new LocalDatabaseHandler(context);
		setListData();
		
	}

	protected void onPause() {
		super.onPause();
		//handler.close();
		if(dbHelper != null){

			dbHelper.close();
		}
	};

	ArrayList<CategoryWithWishlistDetail> wishlistDetails = new ArrayList<CategoryWithWishlistDetail>();

	private void setListData() {

		wishlistDetails.clear();
		allCategoryName.clear();
		allCategoryId.clear();
		Cursor c = dbHelper.returnAllCategory(ParseUser.getCurrentUser().getObjectId());
		categoryCount = c.getCount();



		Cursor cWishlist = dbHelper.getWishlistItem(ParseUser.getCurrentUser().getObjectId());
		ArrayList<WishListDetail> wishTemp;

		if (c.moveToFirst()) {
			do {
				String id = c.getString(c.getColumnIndex(LocalDatabaseHandler.CATEGORY_ID));
				BitmapFactory.Options opt=new BitmapFactory.Options();
				opt.inSampleSize=4;
				System.out.println("category name   " + id);
				wishTemp = new ArrayList<WishListDetail>();

				if (cWishlist.moveToFirst()) {

					do {
						String WishlistCatId = cWishlist
								.getString(cWishlist.getColumnIndex(LocalDatabaseHandler.CATEGORY_ID));

						String isDeleted = cWishlist
								.getString(cWishlist.getColumnIndex(LocalDatabaseHandler.IS_DELETED));
						System.out.println("isDeleted=======" + isDeleted);
						if (WishlistCatId.equals(id)) {
							wishTemp.add(new WishListDetail(
									cWishlist.getString(cWishlist.getColumnIndex(LocalDatabaseHandler.WISHLIST_ID)),
									cWishlist.getString(cWishlist.getColumnIndex(LocalDatabaseHandler.WISHLIST_BRAND)),
									cWishlist.getString(
											cWishlist.getColumnIndex(LocalDatabaseHandler.WISHLIST_COST_PRICE)),
									cWishlist.getString(cWishlist.getColumnIndex(LocalDatabaseHandler.IMAGE_PATH)),
									cWishlist.getString(
											cWishlist.getColumnIndex(LocalDatabaseHandler.WISHLIST_COMMENTS)),
									cWishlist.getString(cWishlist.getColumnIndex(LocalDatabaseHandler.IS_FAVOURITE)),
									cWishlist.getString(cWishlist.getColumnIndex(LocalDatabaseHandler.IS_NEW)),
									cWishlist.getString(cWishlist.getColumnIndex(LocalDatabaseHandler.IS_UPDATED)),
									cWishlist.getString(cWishlist.getColumnIndex(LocalDatabaseHandler.IS_DELETED)),BitmapFactory.decodeFile(cWishlist.getString(cWishlist.getColumnIndex(LocalDatabaseHandler.IMAGE_PATH)), opt),
									cWishlist.getString(cWishlist.getColumnIndex(LocalDatabaseHandler.BACKGROUND_COLOR))));

						}
						if (wishTemp.size() == 4) {
							break;
						}

					} while (cWishlist.moveToNext());

				}

				if (wishTemp.size() > 0) {
					allCategoryName.add(c.getString(c.getColumnIndex(LocalDatabaseHandler.CUSTOM_CATEGORY_NAME)));
					allCategoryId.add(c.getString(c.getColumnIndex(LocalDatabaseHandler.CATEGORY_ID)));
					wishlistDetails.add(new CategoryWithWishlistDetail(
							c.getString(c.getColumnIndex(LocalDatabaseHandler.CATEGORY_ID)),
							c.getString(c.getColumnIndex(LocalDatabaseHandler.CUSTOM_CATEGORY_NAME)), wishTemp));
				}
				System.out.println("cat wishlistDetails================" + wishlistDetails.size());

			} while (c.moveToNext());

		}
		// }

		myWishListtItemListView.setAdapter(new ListItemAdapterNew());
	}

	public class ListItemAdapterNew extends BaseAdapter {
		private LayoutInflater mInflater;

		@Override
		public int getCount() {
			return wishlistDetails.size();
		}

		@Override
		public Object getItem(int position) {
			return wishlistDetails.get(position);
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
				convertView = View.inflate(WishListActivity.this, R.layout.mycloset_vertical_list_items, null);

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


			ArrayList<WishListDetail> wishlistDetail = wishlistDetails.get(position).wishlistDetail;
			int Size = wishlistDetail.size();
			holder.mytitle.setText(allCategoryName.get(position));

			if (Size == 0) {

				//holder.i1.setImageResource(R.drawable.addnew);
				holder.i1.setVisibility(View.INVISIBLE);
				holder.i2.setVisibility(View.INVISIBLE);
				holder.i3.setVisibility(View.INVISIBLE);
				holder.i4.setVisibility(View.INVISIBLE);


				frame1.setBackgroundColor(Color.parseColor("#FFFFFF"));
				frame2.setBackgroundColor(Color.parseColor("#FFFFFF"));
				frame3.setBackgroundColor(Color.parseColor("#FFFFFF"));
				frame4.setBackgroundColor(Color.parseColor("#FFFFFF"));


			} else

			if (Size == 1) {
				holder.i1.setVisibility(View.VISIBLE);
				String[] arrRgb = (wishlistDetail.get(0).bg_color).split(",");

				frame1.setBackgroundColor(Color.rgb(Integer.parseInt(arrRgb[0].trim()), Integer.parseInt(arrRgb[1].trim()), Integer.parseInt(arrRgb[2].trim())));



				//frame1.setBackgroundColor(Color.parseColor("#FFFFFF"));
				frame2.setBackgroundColor(Color.parseColor("#FFFFFF"));
				frame3.setBackgroundColor(Color.parseColor("#FFFFFF"));
				frame4.setBackgroundColor(Color.parseColor("#FFFFFF"));







				holder.i1.setImageBitmap(wishlistDetail.get(0).imgBitmap);
				holder.i2.setImageResource(R.drawable.unchecked);
				holder.i3.setImageResource(R.drawable.unchecked);
				holder.i4.setImageResource(R.drawable.unchecked);
			} else if (Size == 2) {

				String[] arrRgb1 = (wishlistDetail.get(0).bg_color).split(",");
				String[] arrRgb2 = (wishlistDetail.get(1).bg_color).split(",");

				Log.v("SET_RGB1", Integer.parseInt(arrRgb1[0].trim()) + "," + Integer.parseInt(arrRgb1[1].trim()) + "," + Integer.parseInt(arrRgb1[2].trim()));
				Log.v("SET_RGB2", Integer.parseInt(arrRgb2[0].trim()) + "," + Integer.parseInt(arrRgb2[1].trim()) + "," + Integer.parseInt(arrRgb2[2].trim()));

				frame1.setBackgroundColor(Color.rgb(Integer.parseInt(arrRgb1[0].trim()), Integer.parseInt(arrRgb1[1].trim()), Integer.parseInt(arrRgb1[2].trim())));
				frame2.setBackgroundColor(Color.rgb(Integer.parseInt(arrRgb2[0].trim()), Integer.parseInt(arrRgb2[1].trim()), Integer.parseInt(arrRgb2[2].trim())));






				//frame1.setBackgroundColor(Color.parseColor("#FFFFFF"));
				//frame2.setBackgroundColor(Color.parseColor("#FFFFFF"));
				frame3.setBackgroundColor(Color.parseColor("#FFFFFF"));
				frame4.setBackgroundColor(Color.parseColor("#FFFFFF"));

				holder.i1.setVisibility(View.VISIBLE);
				holder.i2.setVisibility(View.VISIBLE);
				holder.i1.setImageBitmap(wishlistDetail.get(0).imgBitmap);
				holder.i2.setImageBitmap(wishlistDetail.get(1).imgBitmap);
				holder.i3.setImageResource(R.drawable.unchecked);
				holder.i4.setImageResource(R.drawable.unchecked);

			} else if (Size == 3) {

				String[] arrRgb1 = (wishlistDetail.get(0).bg_color).split(",");
				String[] arrRgb2 = (wishlistDetail.get(1).bg_color).split(",");
				String[] arrRgb3 = (wishlistDetail.get(2).bg_color).split(",");

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
				holder.i1.setImageBitmap(wishlistDetail.get(0).imgBitmap);
				holder.i2.setImageBitmap(wishlistDetail.get(1).imgBitmap);
				holder.i3.setImageBitmap(wishlistDetail.get(2).imgBitmap);
				holder.i4.setImageResource(R.drawable.unchecked);

			} else if (Size >= 4) {
				String[] arrRgb1 = (wishlistDetail.get(0).bg_color).split(",");
				String[] arrRgb2 = (wishlistDetail.get(1).bg_color).split(",");
				String[] arrRgb3 = (wishlistDetail.get(2).bg_color).split(",");
				String[] arrRgb4 = (wishlistDetail.get(3).bg_color).split(",");

				frame1.setBackgroundColor(Color.rgb(Integer.parseInt(arrRgb1[0].trim()), Integer.parseInt(arrRgb1[1].trim()), Integer.parseInt(arrRgb1[2].trim())));
				frame2.setBackgroundColor(Color.rgb(Integer.parseInt(arrRgb2[0].trim()), Integer.parseInt(arrRgb2[1].trim()), Integer.parseInt(arrRgb2[2].trim())));
				frame3.setBackgroundColor(Color.rgb(Integer.parseInt(arrRgb3[0].trim()), Integer.parseInt(arrRgb3[1].trim()), Integer.parseInt(arrRgb3[2].trim())));
				frame4.setBackgroundColor(Color.rgb(Integer.parseInt(arrRgb4[0].trim()), Integer.parseInt(arrRgb4[1].trim()), Integer.parseInt(arrRgb4[2].trim())));

				holder.i1.setVisibility(View.VISIBLE);
				holder.i2.setVisibility(View.VISIBLE);
				holder.i3.setVisibility(View.VISIBLE);
				holder.i4.setVisibility(View.VISIBLE);
				holder.i1.setImageBitmap(wishlistDetail.get(0).imgBitmap);
				holder.i2.setImageBitmap(wishlistDetail.get(1).imgBitmap);
				holder.i3.setImageBitmap(wishlistDetail.get(2).imgBitmap);
				holder.i4.setImageBitmap(wishlistDetail.get(3).imgBitmap);

			} else {


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

					try {
						b.putString("categoryId", allCategoryId.get(position));
						b.putString("categoryName", allCategoryName.get(position));
					} catch (Exception e) {
						e.printStackTrace();
					}
					goToActivity(WishListItemActivity.class, b);
				}
			});

			return convertView;
		}

		private class ViewHolder {
			ImageView i1, i2, i3, i4;
			com.constants.CustomTextView mytitle;
		}
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {

		case R.id.nextOrDelButton:
			if (categoryCount <= 0)
			{
				showToastS("In order to create wishlist you will have to create category in wardrobe.");
			}
			else
			{
				Intent intent = new Intent(context, UploadOptionWishListActivity.class);
				startActivity(intent);
				overridePendingTransition(R.anim.slide_in_up, R.anim.slide_out_up);
			}
			break;

		default:
			break;
		}
	}
	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		Log.v("232323", "destroy wishlist");
		if(dbHelper != null){
			
			dbHelper.close();
		}
	}



}
