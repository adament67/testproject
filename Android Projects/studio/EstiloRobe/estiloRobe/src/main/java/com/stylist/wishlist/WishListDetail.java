package com.stylist.wishlist;

import android.graphics.Bitmap;

import com.constants.Utils;

public class WishListDetail {
	public String wishlistItemId;
	public String brand;
	public String cost_price;
	public String IsFavourite;
	public String comments;
	public String imgPath;
	public String IsNew;
	public String IsDeleted;
	public String IsUpdated;
	public Bitmap imgBitmap;
public  String bg_color;

	public WishListDetail(  String wishlistItemId,
			String brand, String cost_price,String imgPath,
			String comments,String IsFavourite,String IsNew,String IsUpdated, String IsDeleted,Bitmap imgBitmap,String bg_color)
	{
		this.wishlistItemId = wishlistItemId;
		this.brand = brand;
		this.cost_price = cost_price;
		this.comments = comments;
		this.imgPath = imgPath;
		this.IsFavourite = IsFavourite;
		this.IsNew = IsNew;
		this.IsUpdated = IsUpdated;
		this.IsDeleted = IsDeleted;
		this.imgBitmap=imgBitmap;
		this.bg_color=bg_color;
	}



	@Override
	public String toString() {

		String out = wishlistItemId
		+"\n"+brand
				+"\n"+cost_price
				+"\n"+IsFavourite
				+"\n"+comments
				+"\n"+imgPath
				+"\n"+IsNew
				+"\n"+IsDeleted
				+"\n"+IsUpdated;

		Utils.write("util out wishlist==="+out);

		return out;
	}

}
