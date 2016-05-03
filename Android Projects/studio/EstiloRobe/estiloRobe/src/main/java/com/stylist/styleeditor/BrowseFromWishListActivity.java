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
import android.graphics.Point;
import android.os.Build;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;

import com.constants.BaseActivity;
import com.constants.LocalDatabaseHandler;
import com.constants.Utils;

import com.parse.ParseUser;
import com.stylist.R;
import com.stylist.wishlist.CategoryWithWishlistDetail;
import com.stylist.wishlist.WishListDetail;
public class BrowseFromWishListActivity extends BaseActivity {
	public HashMap<String, ArrayList<String>> categoryWithURL = new HashMap<String, ArrayList<String>>();
	LocalDatabaseHandler dbHelper;
	ArrayList<String> allCategoryName = new ArrayList<String>();
	ArrayList<String> allCategoryId = new ArrayList<String>();
	ListView myWishListtItemListView;
    public static Activity ctx;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_browse_from_wish_list);
		setHeading("WISHLIST");
		backClick();
		 dbHelper = new LocalDatabaseHandler(context);
		ctx=this;
		myWishListtItemListView = (ListView) findViewById(R.id.myWishListItemListView);
		myWishListtItemListView
				.setOnItemClickListener(new OnItemClickListener() {
					public void onItemClick(AdapterView<?> parent, View view,
							int position, long id) {
						Intent i3 = new Intent(context,
								SelectWishListItemActivity.class);
						i3.putExtra("categoryName",
								allCategoryName.get(position));
						i3.putExtra("categoryId", allCategoryId.get(position));
						startActivityForResult(i3, 33);
						overridePendingTransition(
								R.anim.animation_enter_from_right,
								R.anim.animation_leave_out_to_left);
					}
				});


		getScreenDimention(this);
		img_width=(screenWidth/4);

	}
	
	public void onStart() {
		super.onStart();
		//.onStartSession(this,
				//getResources().getString(R.string.flurryKey));
	}

	public void onStop() {
		super.onStop();
		//.onEndSession(this);
	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		System.out.println(">>>>>>>>>>>> onResume");
		super.onResume();
		setListData();
	}

	protected void onPause() {
		super.onPause();
	};

	ArrayList<CategoryWithWishlistDetail> wishlistDetails = new ArrayList<CategoryWithWishlistDetail>();

	private void setListData() {
		wishlistDetails.clear();
		allCategoryName.clear();
		allCategoryId.clear();


		BitmapFactory.Options opt=new BitmapFactory.Options();
		opt.inSampleSize=4;


		Cursor c = dbHelper.returnAllCategory(ParseUser.getCurrentUser()
				.getObjectId());
		Cursor cWishlist = dbHelper.getWishlistItem(ParseUser.getCurrentUser()
				.getObjectId());
		ArrayList<WishListDetail> wishTemp;
		if (c.moveToFirst()) {
			do {
				String id = c.getString(c
						.getColumnIndex(LocalDatabaseHandler.CATEGORY_ID));
			
				System.out.println("category name   " + id);
				wishTemp = new ArrayList<WishListDetail>();

				if (cWishlist.moveToFirst()) {

					do {
						String WishlistCatId = cWishlist
								.getString(cWishlist
										.getColumnIndex(LocalDatabaseHandler.CATEGORY_ID));
						String isDeleted = cWishlist
								.getString(cWishlist
										.getColumnIndex(LocalDatabaseHandler.IS_DELETED));
						if (WishlistCatId.equals(id) && isDeleted.equalsIgnoreCase("false")) {
							wishTemp.add(new WishListDetail(
									cWishlist
											.getString(cWishlist
													.getColumnIndex(LocalDatabaseHandler.WISHLIST_ID)),
									cWishlist.getString(cWishlist
											.getColumnIndex(LocalDatabaseHandler.WISHLIST_BRAND)),
									cWishlist.getString(cWishlist
											.getColumnIndex(LocalDatabaseHandler.WISHLIST_COST_PRICE)),
									cWishlist.getString(cWishlist
											.getColumnIndex(LocalDatabaseHandler.IMAGE_PATH)),
									cWishlist.getString(cWishlist
											.getColumnIndex(LocalDatabaseHandler.WISHLIST_COMMENTS)),
									cWishlist.getString(cWishlist
											.getColumnIndex(LocalDatabaseHandler.IS_FAVOURITE)),
											cWishlist.getString(cWishlist
													.getColumnIndex(LocalDatabaseHandler.IS_NEW)),
													cWishlist.getString(cWishlist
															.getColumnIndex(LocalDatabaseHandler.IS_UPDATED)),
															cWishlist.getString(cWishlist
																	.getColumnIndex(LocalDatabaseHandler.IS_DELETED)),
									BitmapFactory.decodeFile(cWishlist.getString(cWishlist.getColumnIndex(LocalDatabaseHandler.IMAGE_PATH)),opt)
									,cWishlist.getString(cWishlist.getColumnIndex(LocalDatabaseHandler.BACKGROUND_COLOR)))
									);

						}
						if (wishTemp.size() == 4) {
							break;
						}

					} while (cWishlist.moveToNext());

				}
				if(wishTemp.size()>0){
					allCategoryName
					.add(c.getString(c
							.getColumnIndex(LocalDatabaseHandler.CUSTOM_CATEGORY_NAME)));
			allCategoryId.add(c.getString(c
					.getColumnIndex(LocalDatabaseHandler.CATEGORY_ID)));
					wishlistDetails
					.add(new CategoryWithWishlistDetail(
							c.getString(c
									.getColumnIndex(LocalDatabaseHandler.CATEGORY_ID)),
							c.getString(c
									.getColumnIndex(LocalDatabaseHandler.CUSTOM_CATEGORY_NAME)),
							wishTemp));
				}
				
			
			} while (c.moveToNext());
		}
		myWishListtItemListView.setAdapter(new ListItemAdapterNew());
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
		public View getView(int position, View convertView, ViewGroup parent) {
			ViewHolder holder;

			if (convertView == null) {
				holder = new ViewHolder();
				convertView = View.inflate(BrowseFromWishListActivity.this,
						R.layout.mycloset_vertical_list_items, null);

				holder.mytitle = (com.constants.CustomTextView) convertView
						.findViewById(R.id.title);
				holder.i1 = (ImageView) convertView
						.findViewById(R.id.imageView1);
				holder.i2 = (ImageView) convertView
						.findViewById(R.id.imageView2);
				holder.i3 = (ImageView) convertView
						.findViewById(R.id.imageView3);
				holder.i4 = (ImageView) convertView
						.findViewById(R.id.imageView4);


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


			ArrayList<WishListDetail> wishlistDetail = wishlistDetails
					.get(position).wishlistDetail;
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

				String[] arrRgb1 = (wishlistDetail.get(0).bg_color).split(",");

				frame1.setBackgroundColor(Color.rgb(Integer.parseInt(arrRgb1[0].trim()), Integer.parseInt(arrRgb1[1].trim()), Integer.parseInt(arrRgb1[2].trim())));



				//frame1.setBackgroundColor(Color.parseColor("#FFFFFF"));
				frame2.setBackgroundColor(Color.parseColor("#FFFFFF"));
				frame3.setBackgroundColor(Color.parseColor("#FFFFFF"));
				frame4.setBackgroundColor(Color.parseColor("#FFFFFF"));




				holder.i1.setVisibility(View.VISIBLE);
				holder.i1
						.setImageBitmap(wishlistDetail.get(0).imgBitmap);
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
				holder.i1
						.setImageBitmap(wishlistDetail.get(0).imgBitmap);
				holder.i2
						.setImageBitmap(wishlistDetail.get(1).imgBitmap);
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
				holder.i1
						.setImageBitmap(wishlistDetail.get(0).imgBitmap);
				holder.i2
						.setImageBitmap(wishlistDetail.get(1).imgBitmap);
				holder.i3
						.setImageBitmap(wishlistDetail.get(2).imgBitmap);
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
				holder.i1
						.setImageBitmap(wishlistDetail.get(0).imgBitmap);
				holder.i2
						.setImageBitmap(wishlistDetail.get(1).imgBitmap);
				holder.i3
						.setImageBitmap(wishlistDetail.get(2).imgBitmap);
				holder.i4
						.setImageBitmap(wishlistDetail.get(3).imgBitmap);
			} else {






				frame1.setBackgroundColor(Color.parseColor("#FFFFFF"));
				frame2.setBackgroundColor(Color.parseColor("#FFFFFF"));
				frame3.setBackgroundColor(Color.parseColor("#FFFFFF"));
				frame4.setBackgroundColor(Color.parseColor("#FFFFFF"));








				holder.i1.setVisibility(View.VISIBLE);
				holder.i1.setImageResource(R.drawable.addnew);
				holder.i2.setVisibility(View.INVISIBLE);
				holder.i3.setVisibility(View.INVISIBLE);
				holder.i4.setVisibility(View.INVISIBLE);
			}
			return convertView;
		}

		private class ViewHolder {
			ImageView i1, i2, i3, i4;
			com.constants.CustomTextView mytitle;
		}
	}
	
	
	
	
	@Override
	protected void onActivityResult(int requestCode, int responseCode,
			Intent data) {
		// TODO Auto-generated method stub
		super.onActivityResult(requestCode, responseCode, data);
		System.out.println("on act");
		if (requestCode == 33 && responseCode == RESULT_OK) {
			String index = data.getStringExtra("intent_data");
			System.out.println("on act if========="+data);
			setResult(RESULT_OK, new Intent().putExtra("intent_data", index));
			finish();
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
