package com.stylist.styleeditor;

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
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.constants.BaseActivity;
import com.constants.LocalDatabaseHandler;
import com.constants.Utils;

import com.stylist.R;
import com.stylist.wishlist.WishListDetail;

public class SelectWishListItemActivity extends BaseActivity {
	String categoryID;
	com.constants.CustomTextView headerName;
	LinearLayout backButtonLayout;
	GridView wishListPhotosGridView;
	LocalDatabaseHandler dbHelper ;
	String CategoryId, categoryName;
	ArrayList<WishListDetail> wishData = new ArrayList<WishListDetail>();
	 public static Activity ctx;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_select_wish_list_item);
		categoryID = getIntent().getStringExtra("categoryId");
		backClick();
		ctx=this;
		 dbHelper = new LocalDatabaseHandler(context);
		wishListPhotosGridView = (GridView) findViewById(R.id.wishListPhotosGridView);
		setHeading(getIntent().getStringExtra("categoryName"));
		wishListPhotosGridView
				.setOnItemClickListener(new OnItemClickListener() {
					public void onItemClick(AdapterView<?> parent, View v,
							int position, long id) {
						System.out.println("in grid ========="+wishData.get(position).imgPath);
						setResult(RESULT_OK,
								new Intent().putExtra("intent_data", wishData.get(position).imgPath));
						finish();
						overridePendingTransition(R.anim.exit_animation_enter_from_right,
								 R.anim.exit_animation_leave_to_right);
					}
				});
	}


	
	public void onStart() {
		super.onStart();
		//.onStartSession(this,
			//	getResources().getString(R.string.flurryKey));
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
}

private void getData() {
	wishData.clear();

	BitmapFactory.Options opt=new BitmapFactory.Options();
	opt.inSampleSize=4;


	System.out.println("categoryID==in getdata==============="+categoryID);
	Cursor cWishlist = dbHelper.returnAllWishlistItem(categoryID);
			if (cWishlist.moveToFirst()) {
				do {
					if(cWishlist.getString(cWishlist.getColumnIndex(LocalDatabaseHandler.IS_DELETED)).equalsIgnoreCase("false"))
					{
					wishData.add(new WishListDetail(
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
													.getColumnIndex(LocalDatabaseHandler.IS_DELETED)),BitmapFactory.decodeFile(cWishlist.getString(cWishlist.getColumnIndex(LocalDatabaseHandler.IMAGE_PATH)), opt),
							cWishlist.getString(cWishlist
							.getColumnIndex(LocalDatabaseHandler.BACKGROUND_COLOR))));
					}
				System.out.println("brand============="+cWishlist.getString(cWishlist
									.getColumnIndex(LocalDatabaseHandler.WISHLIST_BRAND)));
				} while (cWishlist.moveToNext());
			}
			System.out.println("size====="+wishData);
			wishListPhotosGridView.setAdapter(new WishListPhotoAdapter());

}


public class WishListPhotoAdapter extends BaseAdapter {
	private LayoutInflater mInflater;

	@Override
	public int getCount() {
		return wishData.size();
	}

	@Override
	public Object getItem(int position) {
		return wishData.get(position);
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
			convertView = View.inflate(context, R.layout.view_zoom_image,
					null);

			holder.categoryPhoto = (ImageView) convertView
					.findViewById(R.id.zoomImageView);
			holder.brand = (com.constants.CustomTextView) convertView
					.findViewById(R.id.namez);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		holder.brand.setText(wishData.get(position).brand);


		String[] arrRgb = (wishData.get(position).bg_color).split(",");


		if(arrRgb.length==3) {
			holder.categoryPhoto.setBackgroundColor(Color.rgb(Integer.parseInt(arrRgb[0].trim()), Integer.parseInt(arrRgb[1].trim()), Integer.parseInt(arrRgb[2].trim())));
		}


		holder.categoryPhoto.setImageBitmap(Utils.orientedBitmap(wishData.get(position).imgPath));		
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
}


}
