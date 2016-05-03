package com.stylist.wardrobe;

public class ClosetModelClass{
	public String categoryId;
	public String color;
	public String name;
//	public ArrayList<String> photoUrl;
	boolean isFav;
	String remark;
	String dressCode;
	String image;

	public ClosetModelClass( String name, String categoryId,  String image,boolean isFav,
			 String remark, String dressCode,  String color) {
		this.categoryId= categoryId;
		this.name= name;
		this.image=image;
		this.dressCode= dressCode;
		this.remark= remark;
		this.color=color;
		this.isFav= isFav;
	}

}
