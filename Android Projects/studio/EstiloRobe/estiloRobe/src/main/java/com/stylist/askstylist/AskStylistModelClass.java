package com.stylist.askstylist;


public class AskStylistModelClass {
	public	String stylistComments,photoUrl,queryRequestDate,occasion,comments,parseQueryId;
    public String hasStylistResponsed,hasLiked;
    int objectId;

	public AskStylistModelClass(int objectId, String occasion,String comments,String queryRequestDate,String hasStylistResponsed,
			String hasLiked,String stylistComments,String photoUrl,String parseQueryId) {
		// TODO Auto-generated constructor stub
		this.occasion = occasion;
		this.stylistComments = stylistComments;
		this.photoUrl = photoUrl;
		this.queryRequestDate = queryRequestDate;
		this.hasStylistResponsed = hasStylistResponsed;
		this.comments = comments;
		this.hasLiked=hasLiked;
		this.objectId=objectId;
		this.parseQueryId=parseQueryId;
	}
}
