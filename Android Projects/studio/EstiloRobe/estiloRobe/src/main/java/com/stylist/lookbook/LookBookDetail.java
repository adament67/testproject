package com.stylist.lookbook;

import android.graphics.Bitmap;

public class LookBookDetail {
	public String ocassion;
	public String name;
	public	int lookbookId;
	public	String createdAt;
	public String IsFavourite;
	public String comments;
	public String imgPath;
	public String IsNew;
	public String IsDeleted;
	public String IsUpdated;
	public Bitmap lookImg;
	public LookBookDetail( int lookbookId, String ocassion,
			String name, String imgPath,String createdAt,
			String comments,String IsFavourite,String IsNew,String IsUpdated, String IsDeleted,Bitmap lookImg)
	{
		this.lookbookId = lookbookId;
		this.ocassion = ocassion;
		this.name = name;
		this.comments = comments;
		this.imgPath = imgPath;
		this.IsFavourite = IsFavourite;
		this.IsNew = IsNew;
		this.IsUpdated = IsUpdated;
		this.IsDeleted = IsDeleted;
		this.createdAt=createdAt;
		this.lookImg=lookImg;
	}
}
