package com.stylist.wardrobe;

import android.graphics.Bitmap;

public class ClosetWardrobeDetail   {
	public String wardrobeItemId;
	public String color;
	public String name;
	public String dress_code;
	public String IsFavourite;
	public String imgPath;
	public Bitmap img;
	public String bg_color;

	public String getBg_color() {
		return bg_color;
	}

	public void setBg_color(String bg_color) {
		this.bg_color = bg_color;
	}

	public ClosetWardrobeDetail(  String wardrobeItemId,
			String color, String name,    String imgPath,
			String dress_code,String IsFavourite, Bitmap img,String bg_color) {
		this.wardrobeItemId = wardrobeItemId;
		this.dress_code = dress_code;
		this.color = color;
		this.name = name;
		this.imgPath = imgPath;
		this.IsFavourite = IsFavourite;
		this.img = img;
		this.bg_color=bg_color;
	}
}
