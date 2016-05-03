package com.stylist.wardrobe;

import java.util.ArrayList;

public class CategoryModelClass {
	public String categoryId;
	public String categoryName;
//	public ArrayList<String> photoUrl;
	String photoUrl;

	public CategoryModelClass( String categoryName, String categoryId, /* ArrayList<*/String photoUrl) {
		this.categoryId= categoryId;
		this.categoryName= categoryName;
		this.photoUrl=photoUrl;
	}

}
