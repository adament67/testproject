package com.stylist.wishlist;

import java.util.ArrayList;

public class CategoryWithWishlistDetail {

	public String categoryId;
	public String categoryName;
    public ArrayList<WishListDetail> wishlistDetail;
	public CategoryWithWishlistDetail(String categoryId, 
			String categoryName,ArrayList<WishListDetail> wishlistDetail) {
		this.categoryId = categoryId;
		this.categoryName = categoryName;
		this.wishlistDetail=wishlistDetail;
	}



}
