package com.stylist.wardrobe;

import java.util.ArrayList;

public class ClosetCategoryDetail {
	public String categoryId;
	public String categoryName;
	public ArrayList<ClosetWardrobeDetail> wardeobeDetail;

	public ClosetCategoryDetail(String categoryId, String categoryName,
			ArrayList<ClosetWardrobeDetail> wardeobeDetail) {
		this.categoryId = categoryId;
		this.categoryName = categoryName;
		this.wardeobeDetail = wardeobeDetail;
	}

}
