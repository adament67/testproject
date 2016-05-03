package com.constants;

import java.io.ByteArrayOutputStream;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import com.parse.ParseUser;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.util.Log;

public class LocalDatabaseHandler extends SQLiteOpenHelper {

	private static int DB_VERSION = 19;

	boolean isCreating = false;
	SQLiteDatabase currentDB = null;

	public LocalDatabaseHandler(Context context) {
		super(context, mdbPath+DATABASE_NAME, null, DB_VERSION);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		// TODO Auto-generated method stub

		//if(!Constants.IS_DATABASE_ALREADY_EXISTS) {
			isCreating = true;
			currentDB = db;
			createTeble();
			isCreating = false;
			currentDB = null;
//		}
//		else
//		{
//			Utils.write("database not exists--------------"+Constants.IS_DATABASE_ALREADY_EXISTS);
//			isCreating = false;
//			currentDB=db;
//
//			}


		// this.db = db;

	}

	@Override
	public SQLiteDatabase getWritableDatabase() {
		// TODO Auto-generated method stub
		if (isCreating && currentDB != null) {
			return currentDB;
		}
		return super.getWritableDatabase();
	}

	@Override
	public SQLiteDatabase getReadableDatabase() {
		// TODO Auto-generated method stub
		if (isCreating && currentDB != null) {
			return currentDB;
		}
		return super.getReadableDatabase();
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		// TODO Auto-generated method stub
		//DB_VERSION=DB_VERSION+1;
		/*
		db.execSQL(CREATE_USER_TABLE);
			db.execSQL(CREATE_CATEGORY_TABLE);
			db.execSQL(CREATE_WARDROBE_TABLE);
			db.execSQL(CREATE_WISHLIST_TABLE);
			db.execSQL(CREATE_LOOKBOOK_TABLE);
			db.execSQL(CREATE_PLANNER_TABLE);
			db.execSQL(CREATE_ASK_STYLIST_TABLE);
			db.execSQL(CREATE_ASK_STYLIST_TEMP_TABLE);
		 */

		db.execSQL("DROP TABLE IF EXISTS "+USER_TABLE_NAME);
		db.execSQL("DROP TABLE IF EXISTS "+ASK_STYLIST_TABLE_NAME);
		db.execSQL("DROP TABLE IF EXISTS "+ASK_STYLIST_TEMP_TABLE_NAME);
		db.execSQL("DROP TABLE IF EXISTS "+CATEGORY_TABLE_NAME);
		db.execSQL("DROP TABLE IF EXISTS "+LOOKBOOK_TABLE_NAME);
		db.execSQL("DROP TABLE IF EXISTS "+PLANNER_TABLE_NAME);
		db.execSQL("DROP TABLE IF EXISTS "+WARDROBE_TABLE_NAME);
		db.execSQL("DROP TABLE IF EXISTS "+WISHLIST_TABLE_NAME);



		onCreate(db);
	}

	static Context ctx;
	// static DataBaseHelper dbhelper;
	// private static SQLiteDatabase db;

	public static String mdbPath;
	private static final String DATABASE_NAME = "EstilorobeDb.db";
	public static final String IS_ACTIVE = "isActive";
	public static final String CREATED_AT = "createdAt";
	public static final String IS_FAVOURITE = "is_favourite";
	public static final String IMAGE_FILE = "image_file";
	public static final String IMAGE_PATH = "image_path";

	public static final String IS_NEW = "isNew";
	public static final String IS_UPDATED = "isUpdated";
	public static final String IS_DELETED = "isDeleted";
	String isTrue = "true";
	String isFalse = "false";

	// user table
	public static final String PARSE_U_ID = "userId";
	public static final String LOCAL_U_ID = "localUserId";
	public static final String USER_ID = "nuserId";
	public static final String USER_TABLE_NAME = "User";
	public static final String USER_NAME = "userName";
	public static final String PASSWORD = "password";
	public static final String PROFILE_PIC = "profile_pic";
	public static final String GENDER = "gender";
	public static final String OCCUPATION = "occupation";
	public static final String BIRTH_DATE = "birth_date";
	// public static final String STATS = "stats";
	public static final String BUST = "bust";
	public static final String WAIST = "waist";
	public static final String HIPS = "hips";

	public static final String HEIGHT = "height";
	public static final String WEIGHT = "weight";
	public static final String SIZE = "size";
	public static final String BOTTOM_WAIST_SIZE = "bottom_waist_size";
	public static final String SKIN_COMPLEXION = "skin_complexion";
	public static final String CHARACTERISTIC = "characteristics";
	public static final String LOVE_COLOR = "loving_color";
	public static final String PREF_BRAND = "pref_brand";
	public static final String STYLE_ICON = "style_icon";
	public static final String FASHION_TYPE = "fashion_type";
	public static final String OUTFITS = "outfits";
	public static final String STYLE_DESCRIPTION = "style_description";
	public static final String ABOUT_YOURSELF = "about_yourself";
	public static final String STYLE = "style";
	public static final String SHAPE = "shape";
	public static final String IS_SYNCED = "is_synced";
	static final String CREATE_USER_TABLE = "CREATE TABLE " + USER_TABLE_NAME + " (" + LOCAL_U_ID + " INTEGER  , "
			+ PARSE_U_ID + " TEXT, " + USER_NAME + " TEXT , " + PASSWORD + " TEXT , " + PROFILE_PIC + " TEXT , "
			+ GENDER + " TEXT , " + OCCUPATION + " TEXT , " + BIRTH_DATE + " TEXT , " + BUST + " TEXT , " + WAIST
			+ " TEXT , " + HIPS + " TEXT , " + /* STATS + " TEXT , " + */HEIGHT + " TEXT , " + WEIGHT + " TEXT , "
			+ SIZE + " TEXT , " + BOTTOM_WAIST_SIZE + " TEXT , " + SKIN_COMPLEXION + " TEXT , " + CHARACTERISTIC
			+ " TEXT , " + LOVE_COLOR + " TEXT , " + PREF_BRAND + " TEXT , " + STYLE_ICON + " TEXT , " + FASHION_TYPE
			+ " TEXT , " + OUTFITS + " TEXT , " + STYLE_DESCRIPTION + " TEXT , " + ABOUT_YOURSELF + " TEXT , " + STYLE
			+ " TEXT , " + SHAPE + " TEXT , " + IS_ACTIVE + " TEXT," + IS_DELETED + " TEXT," + IS_UPDATED + " TEXT," +IS_SYNCED + " TEXT," + CREATED_AT + " TEXT );";

	// category table
	public static final String CATEGORY_TABLE_NAME = "Category";
	public static final String CATEGORY_ID = "CategoryId";
	public static final String CUSTOM_CATEGORY_NAME = "CustomCategoryName";
	static final String CREATE_CATEGORY_TABLE = "CREATE TABLE " + CATEGORY_TABLE_NAME + " (" + CATEGORY_ID
			+ " INTEGER PRIMARY KEY AUTOINCREMENT, " + CUSTOM_CATEGORY_NAME + " TEXT , " + USER_ID + " TEXT , "
			+ IS_ACTIVE + " BOOLEAN , " + IS_DELETED + " TEXT , " + CREATED_AT + " TEXT ," + IS_NEW + " TEXT ,"
			+ IS_UPDATED + " TEXT ," + " FOREIGN KEY(" + USER_ID + ") REFERENCES " + USER_TABLE_NAME + "(" + PARSE_U_ID
			+ "));";

	// lookbook table
	public static final String LOOKBOOK_TABLE_NAME = "Lookbook";
	public static final String LOOKBOOK_ID = "lookbook_id";
	public static final String LOOKBOOK_NAME = "lookbook_name";
	public static final String LOOKBOOK_OCASSION = "lookbook_ocassion";
	public static final String COMMENTS = "comments";
	static final String CREATE_LOOKBOOK_TABLE = "CREATE TABLE " + LOOKBOOK_TABLE_NAME + " (" + LOOKBOOK_ID
			+ " INTEGER PRIMARY KEY AUTOINCREMENT, " + LOOKBOOK_NAME + " TEXT , " + LOOKBOOK_OCASSION + " TEXT , "
			+ IS_FAVOURITE + " TEXT , " + COMMENTS + " TEXT , " + IMAGE_PATH + " TEXT , " + IS_NEW + " TEXT ,"
			+ IS_UPDATED + " TEXT ," + USER_ID + " TEXT , " + IS_ACTIVE + " BOOLEAN , " + IS_DELETED + " TEXT , "
			+ CREATED_AT + " TEXT ," + "FOREIGN KEY(" + USER_ID + ") REFERENCES " + USER_TABLE_NAME + "(" + PARSE_U_ID
			+ "));";

	// wardrobe table
	public static final String WARDROBE_TABLE_NAME = "Wardrobe";
	public static final String WARDROBE_ID = "wardrobe_id";
	public static final String UNIQUE_IMAGE_ID = "unique_image_id";
	public static final String WARDROBE_NAME = "name";
	public static final String WARDROBE_COLOR = "color";
	public static final String BACKGROUND_COLOR = "background_color";

	public static final String REMARKS = "remarks";
	public static final String DRESS_CODE = "dress_code";
	static final String CREATE_WARDROBE_TABLE = "CREATE TABLE " + WARDROBE_TABLE_NAME + " (" + WARDROBE_ID
			+ " INTEGER PRIMARY KEY AUTOINCREMENT, " + WARDROBE_NAME + " TEXT , " + UNIQUE_IMAGE_ID + " TEXT , " + WARDROBE_COLOR + " TEXT , "
			+ IS_FAVOURITE + " TEXT , " + BACKGROUND_COLOR + " TEXT , "+ REMARKS + " TEXT , " + DRESS_CODE + " TEXT , " + IS_NEW + " TEXT ,"
			+ IS_UPDATED + " TEXT ," + USER_ID + " TEXT , " + CATEGORY_ID + " INTEGER , " + IMAGE_PATH + " TEXT , "
			+ IS_ACTIVE + " BOOLEAN , " + IS_DELETED + " TEXT , " + CREATED_AT + " TEXT ," + "FOREIGN KEY(" + USER_ID
			+ ") REFERENCES " + USER_TABLE_NAME + "(" + PARSE_U_ID + ")," + " FOREIGN KEY (" + CATEGORY_ID
			+ ") REFERENCES " + CATEGORY_TABLE_NAME + "(" + CATEGORY_ID + "));";

	// wishlist table
	public static final String WISHLIST_TABLE_NAME = "Wishlist";
	public static final String WISHLIST_ID = "wishlist_id";
	public static final String WISHLIST_BRAND = "wishlist_brand";
	public static final String WISHLIST_COST_PRICE = "wishlist_cost_price";
	public static final String WISHLIST_COMMENTS = "wishlist_comments";
	static final String CREATE_WISHLIST_TABLE = "CREATE TABLE " + WISHLIST_TABLE_NAME + " (" + WISHLIST_ID
			+ " INTEGER PRIMARY KEY AUTOINCREMENT, " + WISHLIST_BRAND + " TEXT , " + BACKGROUND_COLOR + " TEXT , " + WISHLIST_COST_PRICE + " TEXT , "
			+ IS_FAVOURITE + " TEXT , " + WISHLIST_COMMENTS + " TEXT , " + IS_NEW + " TEXT ," + IS_UPDATED + " TEXT ,"
			+ USER_ID + " TEXT , " + CATEGORY_ID + " INTEGER , " + IMAGE_PATH + " TEXT , " + IS_ACTIVE + " BOOLEAN , "
			+ IS_DELETED + " TEXT , " + CREATED_AT + " TEXT ," + "FOREIGN KEY(" + USER_ID + ") REFERENCES "
			+ USER_TABLE_NAME + "(" + PARSE_U_ID + ")," + " FOREIGN KEY(" + CATEGORY_ID + ") REFERENCES "
			+ CATEGORY_TABLE_NAME + "(" + CATEGORY_ID + "));";

	// planner table

	public static final String PLANNER_TABLE_NAME = "Planner";
	public static final String PLANNER_ID = "plannerId";
	public static final String OCASSION = "ocassion";
	public static final String PLAN_DATE = "plannerDate";
	public static final String PLAN_TIME = "plannerTime";

	static final String CREATE_PLANNER_TABLE = "CREATE TABLE " + PLANNER_TABLE_NAME + " (" + PLANNER_ID
			+ " INTEGER PRIMARY KEY AUTOINCREMENT, " + OCASSION + " TEXT  , " + IMAGE_PATH + " TEXT , " + PLAN_DATE
			+ " TEXT ,  " + PLAN_TIME + " TEXT ," + IS_NEW + " TEXT ," + IS_UPDATED + " TEXT ," + USER_ID + " TEXT , "
			+ LOOKBOOK_ID + " INTEGER , " + IS_ACTIVE + " TEXT , " + IS_DELETED + " TEXT , " + CREATED_AT + " TEXT ,"
			+ " FOREIGN KEY(" + USER_ID + ") REFERENCES " + USER_TABLE_NAME + "(" + PARSE_U_ID + ") , FOREIGN KEY("
			+ LOOKBOOK_ID + ") REFERENCES " + LOOKBOOK_TABLE_NAME + "(" + LOOKBOOK_ID + "));";

	// askstylist table

	public static final String ASK_STYLIST_TABLE_NAME = "AskStylist";
	public static final String QUERY_ID = "queryId";
	public static final String IS_STYLIST_RESPONSED = "is_stylist_responsed";
	public static final String ISADDEDINLOOKBOOK = "is_added_in_lookbook_responsed";
	public static final String REQUEST_DATE = "requestDate";
	public static final String IS_STYLIST_LIKED = "is_stylist_liked";
	public static final String STYLIST_COMMENTS = "stylist_comments";
	public static final String PHOTOURL = "photourl";
	public static final String PREFERRED_STYLE = "preferred_style";
	public static final String NOT_INTERESTED_STYLE = "not_interested_style";

	// ////////////////////-------------------------------------------------------------------------------Ask
	// Stylist

	static final String CREATE_ASK_STYLIST_TABLE = "CREATE TABLE " + ASK_STYLIST_TABLE_NAME + " (" + QUERY_ID
			+ " INTEGER PRIMARY KEY AUTOINCREMENT, " + OCASSION + " TEXT  , " + ISADDEDINLOOKBOOK + " TEXT  , "
			+ COMMENTS + " TEXT  , " + PREFERRED_STYLE + " TEXT  , " + NOT_INTERESTED_STYLE + " TEXT  , "
			+ "parseQueryId" + " TEXT  , " + "stylistName" + " TEXT  , " + REQUEST_DATE + " TEXT  , "
			+ IS_STYLIST_RESPONSED + " TEXT ," + IS_STYLIST_LIKED + " TEXT  , " + STYLIST_COMMENTS + " TEXT  , "
			+ PHOTOURL + " TEXT  , " + IS_NEW + " TEXT ," + IS_UPDATED + " TEXT ," + USER_ID + " TEXT , " + IS_ACTIVE
			+ " TEXT , " + IS_DELETED + " TEXT , " + " FOREIGN KEY(" + USER_ID + ") REFERENCES " + USER_TABLE_NAME + "("
			+ PARSE_U_ID + "));";

	public long insertAskStylist(String user_id, String ocassion, String comments, String requestdate, String preferred_style, String not_interested_style,
			String parseQueryId) {

		SQLiteDatabase db = this.getWritableDatabase();

		ContentValues contentValues = new ContentValues();
		contentValues.put(USER_ID, user_id);
		contentValues.put(COMMENTS, comments);
		contentValues.put(OCASSION, ocassion);
		contentValues.put(PREFERRED_STYLE, preferred_style);
		contentValues.put(NOT_INTERESTED_STYLE, not_interested_style);
		contentValues.put(REQUEST_DATE, requestdate);
		contentValues.put("parseQueryId", parseQueryId);
		contentValues.put(IS_STYLIST_RESPONSED, "false");
		contentValues.put(IS_STYLIST_LIKED, "false");
		contentValues.put(ISADDEDINLOOKBOOK, "false");
		contentValues.put(STYLIST_COMMENTS, " ");
		contentValues.put(PHOTOURL, " ");
		contentValues.put(IS_NEW, "true");
		contentValues.put(IS_UPDATED, "false");
		contentValues.put(IS_ACTIVE, "true");
		contentValues.put(IS_DELETED, "false");
		contentValues.put("stylistName", "");
		return db.insertOrThrow(ASK_STYLIST_TABLE_NAME, null, contentValues);
	}

	public void updateAskStylistResponce(String stylistComments, String url, String queryId, String stylistname) {
		SQLiteDatabase db = this.getWritableDatabase();
		String strSQL = "UPDATE " + ASK_STYLIST_TABLE_NAME + " SET " + IS_STYLIST_RESPONSED + " = 'true' , "
				+ STYLIST_COMMENTS + " = '" + stylistComments + "' , " + PHOTOURL + " = '" + url + "' , "
				+ "stylistName" + " = '" + stylistname + "' " + " WHERE " + "parseQueryId" + " = '" + queryId + "'";
		System.out.println("strSQ L      " + strSQL);
		db.execSQL(strSQL);
//		ContentValues contentValues = new ContentValues();
//		
//		contentValues.put(COMMENTS, stylistComments);
//		
//		contentValues.put(IS_STYLIST_RESPONSED, "true");
//		contentValues.put(IS_STYLIST_LIKED, "false");
//		contentValues.put(ISADDEDINLOOKBOOK, "false");
//		contentValues.put(STYLIST_COMMENTS,stylistComments);
//		contentValues.put("stylistName",stylistname);
//
//		contentValues.put(PHOTOURL,url);
//		contentValues.put(IS_NEW, "false");
//		contentValues.put(IS_UPDATED, "true");
//		contentValues.put(IS_ACTIVE, "true");
//		contentValues.put(IS_DELETED, "false");
//		
//		int rowAffected = db.update(ASK_STYLIST_TABLE_NAME, contentValues, "'parseQueryId'=queryId", null);
//		
//			System.out.println("rowAffected=="+rowAffected);
//				
	}

	public void updateAskStylistWithLookbookResponce(String queryId) {

		SQLiteDatabase db = this.getWritableDatabase();

		String strSQL = "UPDATE " + ASK_STYLIST_TABLE_NAME + " SET " + ISADDEDINLOOKBOOK + " = 'true' " + " WHERE "
				+ "parseQueryId" + " = '" + queryId + "'";
		System.out.println("strSQ L      " + strSQL);
		db.execSQL(strSQL);

	}

	public Cursor returnAllAskStylist(String userId) {
		SQLiteDatabase db = this.getWritableDatabase();
		return db.rawQuery("select * from " + ASK_STYLIST_TABLE_NAME + " where " + USER_ID + " = '" + userId + "' AND "
				+ IS_DELETED + " = 'false'", null);
	}

	public Cursor returnAskStylist(String parseQueryId) {
		SQLiteDatabase db = this.getWritableDatabase();
		return db.rawQuery("select * from " + ASK_STYLIST_TABLE_NAME + " where " + "parseQueryId" + " = '"
				+ parseQueryId + "' AND " + IS_DELETED + " = 'false'", null);
	}

	public static final String ASK_STYLIST_TEMP_TABLE_NAME = "AskStylistTemp";

	static final String CREATE_ASK_STYLIST_TEMP_TABLE = "CREATE TABLE " + ASK_STYLIST_TEMP_TABLE_NAME + " (" + QUERY_ID
			+ " INTEGER PRIMARY KEY AUTOINCREMENT, " + OCASSION + " TEXT  , " + ISADDEDINLOOKBOOK + " TEXT  , "
			+ COMMENTS + " TEXT  , " + PREFERRED_STYLE + " TEXT  , " + NOT_INTERESTED_STYLE + " TEXT  , "
			+ "parseQueryId" + " TEXT  , " + "stylistName" + " TEXT  , " + REQUEST_DATE + " TEXT  , "
			+ IS_STYLIST_RESPONSED + " TEXT ," + IS_STYLIST_LIKED + " TEXT  , " + STYLIST_COMMENTS + " TEXT  , "
			+ PHOTOURL + " TEXT  , " + IS_NEW + " TEXT ," + IS_UPDATED + " TEXT ," + USER_ID + " TEXT , " + IS_ACTIVE
			+ " TEXT , " + IS_DELETED + " TEXT , " + " FOREIGN KEY(" + USER_ID + ") REFERENCES " + USER_TABLE_NAME + "("
			+ PARSE_U_ID + "));";

	public long insertAskStylistTemp(String user_id, String ocassion, String comments, String requestdate,
			String preferred_style, String not_interested_style, String parseQueryId) {

		Log.v("Query", CREATE_ASK_STYLIST_TEMP_TABLE);

		SQLiteDatabase db = this.getWritableDatabase();
		ContentValues contentValues = new ContentValues();
		contentValues.put(USER_ID, user_id);
		contentValues.put(COMMENTS, comments);
		contentValues.put(OCASSION, ocassion);
		contentValues.put(PREFERRED_STYLE, preferred_style);
		contentValues.put(NOT_INTERESTED_STYLE, not_interested_style);
		contentValues.put(REQUEST_DATE, requestdate);
		contentValues.put("parseQueryId", parseQueryId);
		contentValues.put(IS_STYLIST_RESPONSED, "false");
		contentValues.put(IS_STYLIST_LIKED, "false");
		contentValues.put(ISADDEDINLOOKBOOK, "false");
		contentValues.put(STYLIST_COMMENTS, " ");
		contentValues.put(PHOTOURL, " ");
		contentValues.put(IS_NEW, "true");
		contentValues.put(IS_UPDATED, "false");
		contentValues.put(IS_ACTIVE, "true");
		contentValues.put(IS_DELETED, "false");
		contentValues.put("stylistName", "");
		return db.insertOrThrow(ASK_STYLIST_TEMP_TABLE_NAME, null, contentValues);
	}
	public Cursor returnAllAskStylistTemp() {
		SQLiteDatabase db = this.getWritableDatabase();
		return db.rawQuery("select * from " + ASK_STYLIST_TEMP_TABLE_NAME , null);
	}
	public void deleteTempAskStylist() {
		SQLiteDatabase db = this.getWritableDatabase();
		//db.rawQuery("DELETE  FROM " + ASK_STYLIST_TEMP_TABLE_NAME, null);
		db.execSQL("delete from "+ ASK_STYLIST_TEMP_TABLE_NAME);

	}

	// =============------------------------------------------------------------------=============Queries
	// for category table

	// -------------------------------------------------------------------------------------------------category
	// table for Local DataBase

	public long insertNewCategory(String user_id, String customCategoryName,String isNew) {
		SQLiteDatabase db = this.getWritableDatabase();
		ContentValues contentValues = new ContentValues();
		contentValues.put(USER_ID, user_id);
		contentValues.put(CUSTOM_CATEGORY_NAME, customCategoryName);
		contentValues.put(IS_ACTIVE, true);
		contentValues.put(IS_NEW, isNew);
		contentValues.put(IS_UPDATED, "false");
		contentValues.put(IS_DELETED, "false");

		return db.insertOrThrow(CATEGORY_TABLE_NAME, null, contentValues);
	}

	/*
	Cursor cursor = theDatabase.query(DATABASE_TABLE, columns,null, null, null, null, null);
		cursor.moveToLast();
	 */


	public String  insertNewCategoryReturnId(String user_id, String customCategoryName,String isNew) {

		SQLiteDatabase db = this.getWritableDatabase();
		ContentValues contentValues = new ContentValues();
		contentValues.put(USER_ID, user_id);
		contentValues.put(CUSTOM_CATEGORY_NAME, customCategoryName);
		contentValues.put(IS_ACTIVE, true);
		contentValues.put(IS_NEW, isNew);
		contentValues.put(IS_UPDATED, "false");
		contentValues.put(IS_DELETED, "false");

		db.insertOrThrow(CATEGORY_TABLE_NAME, null, contentValues);

		String[] columns = {CATEGORY_ID};

		Cursor cursor = db.query(CATEGORY_TABLE_NAME, columns,null, null, null, null, null);
		cursor.moveToLast();


		return cursor.getString(cursor.getColumnIndex(CATEGORY_ID));
	}

	public Cursor returnAllCategory(String userId) {

		Utils.write("cursor>>>>>>>>>>>>>>>>>>>>>>>>>>"+this.getWritableDatabase());
		Utils.write("cursor>>>>>>>>>>>>>>>>>>>>>>>>>>  select * from " + CATEGORY_TABLE_NAME + " where " + USER_ID + " = '"
				+ userId + "' AND " + IS_DELETED + " = 'false'");


		return this.getWritableDatabase().rawQuery("select * from " + CATEGORY_TABLE_NAME + " where " + USER_ID + " = '"
				+ userId + "' AND " + IS_DELETED + " = 'false'", null);



	}

	public void renameCategoryItem(String id, String newValue) {
		SQLiteDatabase db = this.getWritableDatabase();
		System.out.println("id   " + id + "   newValue   " + newValue);

		String strSQL = "UPDATE " + CATEGORY_TABLE_NAME + " SET " + CUSTOM_CATEGORY_NAME + " = '" + newValue + "', "
				+ IS_UPDATED + " = 'true'" + " WHERE " + CATEGORY_ID + " = " + id + ";";
		System.out.println("strSQ L      " + strSQL);
		db.execSQL(strSQL);
	}

	public void setDeleteCategoryFlag(String id) {
		SQLiteDatabase db = this.getWritableDatabase();
		String strSQL = "UPDATE " + CATEGORY_TABLE_NAME + " SET " + IS_DELETED + " = 'true'" + " WHERE " + CATEGORY_ID
				+ " = " + id;
		String strSQLq = "UPDATE " + WISHLIST_TABLE_NAME + " SET " + IS_DELETED + " = 'true'" + " WHERE " + CATEGORY_ID
				+ " = " + id;
		String strSQLqq = "UPDATE " + WARDROBE_TABLE_NAME + " SET " + IS_DELETED + " = 'true'" + " WHERE " + CATEGORY_ID
				+ " = " + id;

		db.execSQL(strSQLq);
		db.execSQL(strSQLqq);
		db.execSQL(strSQL);
	}

	public Cursor returnNewCategory(String userId) {
		SQLiteDatabase db = this.getWritableDatabase();
		return db.rawQuery("select * from " + CATEGORY_TABLE_NAME + " where " + USER_ID + " = '" + userId + "' AND "
				+ IS_NEW + " = 'true'" + " AND " + IS_DELETED + " = 'false'", null);
	}

	public Cursor returnUpdatedCategory(String userId) {
		SQLiteDatabase db = this.getWritableDatabase();
		return db.rawQuery("select * from " + CATEGORY_TABLE_NAME + " where " + USER_ID + " = '" + userId + "' AND "
				+ IS_UPDATED + " = 'true'" + " AND " + IS_DELETED + " = 'false'", null);
	}

	public Cursor returnDeletedCategory(String userId) {
		SQLiteDatabase db = this.getWritableDatabase();
		return db.rawQuery("select * from " + CATEGORY_TABLE_NAME + " where " + USER_ID + " = '" + userId + "' AND "
				+ IS_DELETED + " = 'true'", null);
	}

	// ------------------------------------------------------------------------------------------------------category
	// table Adter server updation

	public void setNewCategorySavedOnServer(String id) {
		SQLiteDatabase db = this.getWritableDatabase();
		String strSQL = "UPDATE " + CATEGORY_TABLE_NAME + " SET " + IS_NEW + " = 'false', " + IS_UPDATED + " = 'false'"
				+ " WHERE " + CATEGORY_ID + " = " + id;
		System.out.println("strSQ L      " + strSQL);
		db.execSQL(strSQL);
	}

	public void setUpdateCategorySavedOnServer(String id) {
		SQLiteDatabase db = this.getWritableDatabase();
		String strSQL = "UPDATE " + CATEGORY_TABLE_NAME + " SET " + IS_UPDATED + " = 'false'" + " WHERE " + CATEGORY_ID
				+ " = " + id;
		System.out.println("strSQ L      " + strSQL);
		db.execSQL(strSQL);
	}

	public void setDeleteCategorySavedOnServer(String id) {
		SQLiteDatabase db = this.getWritableDatabase();

		String strSQL1 = "DELETE from " + WARDROBE_TABLE_NAME + " WHERE " + CATEGORY_ID + " = " + id;
		db.execSQL(strSQL1);
		String strSQL2 = "DELETE from " + WISHLIST_TABLE_NAME + " WHERE " + CATEGORY_ID + " = " + id;
		db.execSQL(strSQL2);
		String strSQL = "DELETE from " + CATEGORY_TABLE_NAME + " WHERE " + CATEGORY_ID + " = " + id;
		db.execSQL(strSQL);
	}

	// ===========================================================================================================Queries
	// for WARDROBE table

	// -------------------------------------------------------------------------------------------------WARDROBE
	// table for Local DataBase

	public long insertWardrobeItem(String unique_imageId,String user_id, String name, String color, String remark, String dresscode,
			String categotyId, String imgPath, String createdAt,String isNew,String bg_color) {

		// SQLiteDatabase db = this.getWritableDatabase();
		SQLiteDatabase db = this.getWritableDatabase();
		ContentValues contentValues = new ContentValues();
		contentValues.put(UNIQUE_IMAGE_ID, unique_imageId);
		contentValues.put(USER_ID, user_id);
		contentValues.put(WARDROBE_NAME, name);
		contentValues.put(WARDROBE_COLOR, color);
		contentValues.put(REMARKS, remark);
		contentValues.put(DRESS_CODE, dresscode);
		contentValues.put(IS_FAVOURITE, "false");
		contentValues.put(CATEGORY_ID, categotyId);
		contentValues.put(IMAGE_PATH, imgPath);
		contentValues.put(CREATED_AT, createdAt);
		contentValues.put(IS_ACTIVE, true);
		contentValues.put(IS_DELETED, "false");
		contentValues.put(IS_NEW, isNew);
		contentValues.put(BACKGROUND_COLOR, bg_color);
		contentValues.put(IS_UPDATED, "false");
		return db.insertOrThrow(WARDROBE_TABLE_NAME, null, contentValues);
	}

	public void updateWardrobe(String wardid, String name, String colour, String dressCode) {
		SQLiteDatabase db = this.getWritableDatabase();
		String strSQL = "UPDATE " + WARDROBE_TABLE_NAME + " SET " + WARDROBE_NAME + " = '" + name + "', " + IS_UPDATED
				+ " = 'true' ," + WARDROBE_COLOR + " = '" + colour + "', " + DRESS_CODE + " = '" + dressCode + "' "
				+ " WHERE " + WARDROBE_ID + " = " + wardid;
		db.execSQL(strSQL);
	}

	public Cursor getWardrobeItem(String id) {
		SQLiteDatabase db = this.getWritableDatabase();
		return db.rawQuery("select * from " + WARDROBE_TABLE_NAME + " where " + USER_ID + " = '" + id + "' AND "
				+ IS_DELETED + " = 'false'", null);

	}

	public static byte[] getBytes(Bitmap bitmap) {
		ByteArrayOutputStream stream = new ByteArrayOutputStream();
		bitmap.compress(CompressFormat.PNG, 0, stream);
		return stream.toByteArray();
	}

	public Cursor returnAllWardrobeItem(String categoryId) {
		SQLiteDatabase db = this.getWritableDatabase();
		return db.rawQuery("select * from " + WARDROBE_TABLE_NAME + " where " + CATEGORY_ID + " = '" + categoryId
				+ "' AND " + IS_DELETED + " = 'false'", null);

	}

	public void deleteWardrobeItem(String wardrobeId) {
		SQLiteDatabase db = this.getWritableDatabase();
		String strSQLqq = "UPDATE " + WARDROBE_TABLE_NAME + " SET " + IS_DELETED + " = 'true'" + " WHERE " + WARDROBE_ID
				+ " = " + wardrobeId;
		db.execSQL(strSQLqq);
	}

	public Cursor returnWardrobe(String wardrobeId) {
		SQLiteDatabase db = this.getWritableDatabase();
		return db.rawQuery("select * from " + WARDROBE_TABLE_NAME + " where " + WARDROBE_ID + " = '" + wardrobeId
				+ "' AND " + IS_DELETED + " = 'false'", null);
	}

	public void setWardrobeFavourite(String wardrobeId, String flag) {
		SQLiteDatabase db = this.getWritableDatabase();
		String strSQL = "UPDATE " + WARDROBE_TABLE_NAME + " SET " + IS_FAVOURITE + " = '" + flag + "', " + IS_UPDATED
				+ " = 'true' " + " WHERE " + WARDROBE_ID + " = " + wardrobeId;
		db.execSQL(strSQL);
	}

	// ==============================================================================================================WARDROBE
	// table for Service

	public Cursor returnNewWardrobe(String id) {
		SQLiteDatabase db = this.getWritableDatabase();
		return db.rawQuery("select * from " + WARDROBE_TABLE_NAME + " where " + USER_ID + " = '" + id + "' AND "
				+ IS_NEW + " = 'true'" + " AND " + IS_DELETED + " = 'false'", null);

	}

	public Cursor returnUpdatedWardrobe(String categoryId) {
		SQLiteDatabase db = this.getWritableDatabase();
		return db.rawQuery("select * from " + WARDROBE_TABLE_NAME + " where " + CATEGORY_ID + " = '" + categoryId
				+ "' AND " + IS_UPDATED + " = 'true'", null);
	}

	public Cursor returnDeletedWardrobe(String UserId) {
		SQLiteDatabase db = this.getWritableDatabase();
		return db.rawQuery("select * from " + WARDROBE_TABLE_NAME + " where " + USER_ID + " = '" + UserId + "' AND "
				+ IS_DELETED + " = 'true'", null);
	}

	// ------------------------------------------------------------------------------------------------------WARDROBE
	// table Adter server updation

	public void setUpdatewardrobeSavedOnServer(String id) {
		SQLiteDatabase db = this.getWritableDatabase();
		String strSQL = "UPDATE " + CATEGORY_TABLE_NAME + " SET " + IS_UPDATED + " = 'false'" + " WHERE " + WARDROBE_ID
				+ " = " + id;
		System.out.println("strSQ L      " + strSQL);
		db.execSQL(strSQL);
	}

	public void setNewWardrobeSavedOnServer(String id) {
		SQLiteDatabase db = this.getWritableDatabase();
		String strSQL = "UPDATE " + WARDROBE_TABLE_NAME + " SET " + IS_NEW + " = 'false', " + IS_UPDATED + " = 'false'"
				+ " WHERE " + WARDROBE_ID + " = " + id;
		System.out.println("strSQ L      " + strSQL);
		db.execSQL(strSQL);
	}

	public void setDeleteWardrobeSavedOnServer(String id) {
		SQLiteDatabase db = this.getWritableDatabase();
		String strSQL2 = "DELETE from " + WARDROBE_TABLE_NAME + " WHERE " + WARDROBE_ID + " = " + id;
		System.out.println("strSQ L      " + strSQL2);
		db.execSQL(strSQL2);
	}

	// ------------------------------------------------------------------------------------------------------user
	// Queries

	public long insertUser(String user_id, String user_name, String profile_pic_path, String gender, String occupation,
			String birth_date, String bust, String waist, String hips, String height, String weight,
			String size, String bottom_waist_size, String skin_coplexion, String characteristics, String love_color,
			String pref_brand, String style_icon, String fashin_type, String outfits, String style_desription,
			String about_yourself, String style, String shape, String created_at,String is_synced) {
		SQLiteDatabase db = this.getWritableDatabase();
		ContentValues contentValues = new ContentValues();
		contentValues.put(LOCAL_U_ID, 1);
		contentValues.put(PARSE_U_ID, user_id);
		contentValues.put(USER_NAME, user_name);
		contentValues.put(PROFILE_PIC, profile_pic_path);
		contentValues.put(GENDER, gender);
		contentValues.put(OCCUPATION, occupation);
		contentValues.put(BIRTH_DATE, birth_date);
		contentValues.put(BUST, bust);
		contentValues.put(WAIST, waist);
		contentValues.put(HIPS, hips);
		contentValues.put(HEIGHT, height);
		contentValues.put(WEIGHT, weight);
		contentValues.put(SIZE, size);
		contentValues.put(BOTTOM_WAIST_SIZE, bottom_waist_size);
		contentValues.put(SKIN_COMPLEXION, skin_coplexion);
		contentValues.put(CHARACTERISTIC, characteristics);
		contentValues.put(LOVE_COLOR, love_color);
		contentValues.put(PREF_BRAND, pref_brand);
		contentValues.put(STYLE_ICON, style_icon);
		contentValues.put(FASHION_TYPE, fashin_type);
		contentValues.put(OUTFITS, outfits);
		contentValues.put(STYLE_DESCRIPTION, style_desription);
		contentValues.put(ABOUT_YOURSELF, about_yourself);
		contentValues.put(STYLE, style);
		contentValues.put(SHAPE, shape);
		contentValues.put(CREATED_AT, created_at);
		contentValues.put(IS_ACTIVE, true);
		contentValues.put(IS_DELETED, false);
		contentValues.put(IS_SYNCED,is_synced);
		return db.insertOrThrow(USER_TABLE_NAME, null, contentValues);
	}

	
	
	/*public void updateUser(String user_id, String user_name, String profile_pic_path, String gender, String occupation,
			String birth_date, String bust, String waist, String hips, String height, String weight,
			String size, String bottom_waist_size, String skin_coplexion, String characteristics, String love_color,
			String pref_brand, String style_icon, String fashin_type, String outfits, String style_desription,
			String about_yourself, String style, String shape, String created_at)
	{

			SQLiteDatabase db = this.getWritableDatabase();
			String strSQL = "UPDATE " + USER_TABLE_NAME + " SET " + USER_NAME +" = "+ user_name +

					 ", SET " + PROFILE_PIC +" = "+ profile_pic_path +
					  ", SET " + GENDER +" = "+ gender +
					   ", SET " + OCCUPATION +" = "+ occupation +
					    ", SET " + BIRTH_DATE +" = "+ birth_date +
					     ", SET " + BUST +" = "+ bust +
					      ", SET " + WAIST +" = "+ waist +
					       ", SET " + HIPS +" = "+ hips +
					        ", SET " + HEIGHT +" = "+ height +
					         ", SET " + WEIGHT +" = "+ weight +
					          ", SET " + SIZE +" = "+ size +
					           ", SET " + BOTTOM_WAIST_SIZE +" = "+ bottom_waist_size +
					           ", SET " + SKIN_COMPLEXION +" = "+ skin_coplexion +
                                ", SET " + CHARACTERISTIC +" = "+ characteristics +
                                ", SET " + LOVE_COLOR +" = "+ love_color +
					  ", SET " + GENDER +" = "+ gender +
					   ", SET " + OCCUPATION +" = "+ occupation +
					    ", SET " + BIRTH_DATE +" = "+ birth_date +
					     ", SET " + BUST +" = "+ bust +
					      ", SET " + WAIST +" = "+ waist +
					       ", SET " + HIPS +" = "+ hips +
					        ", SET " + HEIGHT +" = "+ height +
					         ", SET " + WEIGHT +" = "+ weight +
					          ", SET " + SIZE +" = "+ size +
					           ", SET " + BOTTOM_WAIST_SIZE +" = "+ bottom_waist_size +


					IS_UPDATED + " = 'true' " + " WHERE "
					+ PARSE_U_ID + " = '" + user_id + "'";
			System.out.println("strSQ L      " + strSQL);
			db.execSQL(strSQL);


	}*/



	public int updateUserProfile(String user_id, String user_name, String profile_pic_path, String gender, String occupation,
							 String birth_date, String bust, String waist, String hips, String height, String weight,
							 String size, String bottom_waist_size, String skin_coplexion, String characteristics, String love_color,
							 String pref_brand, String style_icon, String fashin_type, String outfits, String style_desription,
							 String about_yourself, String style, String shape, String created_at,String is_synced){

		SQLiteDatabase db = this.getWritableDatabase();
		ContentValues cv = new ContentValues();
		cv.put(PARSE_U_ID, user_id);
		cv.put(USER_NAME,user_name);
		cv.put(PROFILE_PIC,profile_pic_path);
		cv.put(GENDER,gender);
		cv.put(OCCUPATION,occupation);
		cv.put(BIRTH_DATE,birth_date);
		cv.put(BUST,bust);
		cv.put(WAIST,waist);
		cv.put(HIPS,hips);
		cv.put(HEIGHT,height);
		cv.put(WEIGHT,weight);
		cv.put(SIZE,size);
		cv.put(BOTTOM_WAIST_SIZE,bottom_waist_size);
		cv.put(SKIN_COMPLEXION,skin_coplexion);
		cv.put(CHARACTERISTIC,characteristics);
		cv.put(LOVE_COLOR,love_color);
		cv.put(IS_UPDATED,"true");
		cv.put(PREF_BRAND,pref_brand);
		cv.put(ABOUT_YOURSELF,about_yourself);
		cv.put(STYLE_DESCRIPTION, style_desription);
		cv.put(FASHION_TYPE, fashin_type);
		cv.put(IS_SYNCED, is_synced);
		cv.put(SHAPE, shape);
		cv.put(STYLE, style);
		cv.put(STYLE_ICON, style_icon);
		cv.put(OUTFITS, outfits);
		//dump cv
		Set<Map.Entry<String, Object>> s=cv.valueSet();
		Iterator itr = s.iterator();

		Log.d("DatabaseSync", "ContentValue Length :: " + cv.size());

		while(itr.hasNext())
		{
			Map.Entry me = (Map.Entry)itr.next();
			String key = me.getKey().toString();
			Object value =  me.getValue();

			Log.d("DatabaseSync", "Key:"+key+", values:"+(String)(value == null?null:value.toString()));
		}

		//

		Log.d("SQL_TABLE_USER", CREATE_USER_TABLE);

		return db.update(USER_TABLE_NAME, cv, PARSE_U_ID +" = '" + user_id+"'", null);
	}


	
	
	public Cursor getUser() {
		SQLiteDatabase db = this.getWritableDatabase();
		return db.rawQuery("SELECT * FROM " + USER_TABLE_NAME, null);

	}

//	public Boolean deleteUser(String userId) {
//		SQLiteDatabase db = this.getWritableDatabase();
//		    return db.delete(USER_TABLE_NAME, PARSE_U_ID + " = '" + userId + "'", null) > 0;
//	//	return db.rawQuery("DELETE  FROM " + USER_TABLE_NAME + " where " + PARSE_U_ID + " = '" + userId + "'", null);
//
//	}

	public Cursor getUser(String userId) {
		SQLiteDatabase db = this.getWritableDatabase();
		return db.rawQuery("SELECT * FROM " + USER_TABLE_NAME + " where " + PARSE_U_ID + "='"+ userId + "'" , null);

	}

//	public Cursor getUserName(String userId) {
//		SQLiteDatabase db = this.getWritableDatabase();
//		return db.rawQuery("SELECT * FROM " + USER_TABLE_NAME + " where " + PARSE_U_ID + " = '" + userId + "'", null);
//
//	}

	// ///////////////////////////////////////////////////////////////////////////////////////////
	// ////////Queries for Wishlist Local Database

	public long insertWishlistItem(String user_id, String brand, String cost_price, String comments, String categotyId,
			String img_path, String createdAt, String rgb) {
		SQLiteDatabase db = this.getWritableDatabase();
		ContentValues contentValues = new ContentValues();
		contentValues.put(USER_ID, user_id);
		contentValues.put(WISHLIST_BRAND, brand);
		contentValues.put(WISHLIST_COST_PRICE, cost_price);
		contentValues.put(WISHLIST_COMMENTS, comments);
		contentValues.put(IS_FAVOURITE, "false");
		contentValues.put(CATEGORY_ID, categotyId);
		contentValues.put(IMAGE_PATH, img_path);
		contentValues.put(IS_NEW, "true");
		contentValues.put(IS_UPDATED, "false");
		contentValues.put(CREATED_AT, createdAt);
		contentValues.put(BACKGROUND_COLOR, rgb);
		contentValues.put(IS_ACTIVE, "true");
		contentValues.put(IS_DELETED, "false");
		return db.insertOrThrow(WISHLIST_TABLE_NAME, null, contentValues);
	}

	public Cursor getWishlistItem(String id) {
		SQLiteDatabase db = this.getWritableDatabase();
		return db.rawQuery("select * from " + WISHLIST_TABLE_NAME + " where " + USER_ID + " = '" + id + "' AND "
				+ IS_DELETED + " = 'false'", null);
		// return db.query(WISHLIST_TABLE_NAME, null, USER_ID + "= ?",
		// new String[] { "" + id }, null, null, null);
	}

	public Cursor returnAllWishlistItem(String categoryId) {
		SQLiteDatabase db = this.getWritableDatabase();
		// TODO Auto-generated method stub
		return db.query(WISHLIST_TABLE_NAME, null, CATEGORY_ID + " = ?", new String[] { "" + categoryId }, null, null,
				null);

	}

	public Cursor returnSingleWishlistItem(String wishlistId) {
		SQLiteDatabase db = this.getWritableDatabase();
		// TODO Auto-generated method stub
		System.out.println("wishlistId==============in local==========" + wishlistId);
		return db.query(WISHLIST_TABLE_NAME, null, WISHLIST_ID + " = ?", new String[] { "" + wishlistId }, null, null,
				null);

	}

	public void updateWishlist(String wishid, String brand, String cost, String comments) {
		SQLiteDatabase db = this.getWritableDatabase();
		String strSQL = "UPDATE " + WISHLIST_TABLE_NAME + " SET " + WISHLIST_BRAND + " = '" + brand + "', " + IS_UPDATED
				+ " = 'true' ," + WISHLIST_COST_PRICE + " = '" + cost + "', " + WISHLIST_COMMENTS + " = '" + comments
				+ "' " + " WHERE " + WISHLIST_ID + " = " + wishid;
		db.execSQL(strSQL);
	}

	// public void setWishlistFavourite(String wishlistId) {
	// String strSQL = "UPDATE " + WISHLIST_TABLE_NAME + " SET "
	// + IS_FAVOURITE + " = '" + isTrue + "' AND " + IS_UPDATED
	// + " = '" + isTrue + "' WHERE " + WISHLIST_ID + " = "
	// + wishlistId;
	// db.execSQL(strSQL);
	// }
	//
	// public void setWishlistUnFavourite(String wishlistId) {
	// String strSQL = " UPDATE " + WISHLIST_TABLE_NAME + " SET "
	// + IS_FAVOURITE + " = '" + isFalse + "' AND " + IS_UPDATED
	// + " = '" + isTrue + "' WHERE " + WISHLIST_ID + " = "
	// + wishlistId;
	// db.execSQL(strSQL);
	// }

	public void setWishlistFavourite(String wishlistId, String flag) {
		SQLiteDatabase db = this.getWritableDatabase();
		String strSQL = "UPDATE " + WISHLIST_TABLE_NAME + " SET " + IS_FAVOURITE + " = '" + flag + "', " + IS_UPDATED
				+ " = 'true' " + " WHERE " + WISHLIST_ID + " = " + wishlistId;
		db.execSQL(strSQL);
	}

	public void deleteWishListItem(String wishlistId) {
		SQLiteDatabase db = this.getWritableDatabase();
		String strSQL = "UPDATE " + WISHLIST_TABLE_NAME + "  SET " + IS_DELETED + "  =  '" + isTrue + "' WHERE "
				+ WISHLIST_ID + "  =  " + wishlistId;
		db.execSQL(strSQL);
	}

	// ==============================================================================================================Wishlist
	// table for Service

	public Cursor returnNewWistList(String userId) {
		SQLiteDatabase db = this.getWritableDatabase();
		return db.rawQuery("select * from " + WISHLIST_TABLE_NAME + " where " + USER_ID + " = '" + userId + "' AND "
				+ IS_NEW + " = 'true'" + " AND " + IS_DELETED + " = 'false'", null);
	}

	public Cursor returnUpdatedWistList(String userId) {
		SQLiteDatabase db = this.getWritableDatabase();
		return db.rawQuery("select * from " + WISHLIST_TABLE_NAME + " where " + USER_ID + " = '" + userId + "' AND "
				+ IS_UPDATED + " = 'true'" + " AND " + IS_DELETED + " = 'false'", null);
	}

	public Cursor returnDeletedWistList(String userId) {
		SQLiteDatabase db = this.getWritableDatabase();
		return db.rawQuery("select * from " + WISHLIST_TABLE_NAME + " where " + USER_ID + " = '" + userId + "' AND "
				+ IS_DELETED + " = 'true'", null);
	}

	// ------------------------------------------------------------------------------------------------------Wishlist
	// table Adter server updation

	public void setNewWistListSavedOnServer(String id) {
		SQLiteDatabase db = this.getWritableDatabase();
		String strSQL = "UPDATE " + WISHLIST_TABLE_NAME + " SET " + IS_NEW + " = 'false', " + IS_UPDATED + " = 'false'"
				+ " WHERE " + WISHLIST_ID + " = " + id;
		System.out.println("strSQ L      " + strSQL);
		db.execSQL(strSQL);
	}

	public void setUpdateWistListSavedOnServer(String id) {
		SQLiteDatabase db = this.getWritableDatabase();
		String strSQL = "UPDATE " + WISHLIST_TABLE_NAME + " SET " + IS_UPDATED + " = 'false'" + " WHERE " + WISHLIST_ID
				+ " = " + id;
		System.out.println("strSQ L      " + strSQL);
		db.execSQL(strSQL);
	}

	public void setDeleteWistListSavedOnServer(String id) {
		SQLiteDatabase db = this.getWritableDatabase();
		String strSQL2 = "DELETE from " + WISHLIST_TABLE_NAME + " WHERE " + WISHLIST_ID + " = " + id;
		db.execSQL(strSQL2);
	}

	// ///////////////////////////////////////////////////////////////////////////////////////////
	// ////////Queries for Lookbook Local Database

	public long insertLookbookItem(String user_id, String name, String ocassion, String comments, String img_path,
			String createdAt) {
		SQLiteDatabase db = this.getWritableDatabase();
		ContentValues contentValues = new ContentValues();
		contentValues.put(USER_ID, user_id);
		contentValues.put(LOOKBOOK_NAME, name);
		contentValues.put(LOOKBOOK_OCASSION, ocassion);
		contentValues.put(COMMENTS, comments);
		contentValues.put(IS_FAVOURITE, "false");
		contentValues.put(IMAGE_PATH, img_path);
		contentValues.put(CREATED_AT, createdAt);
		contentValues.put(IS_NEW, "true");
		contentValues.put(IS_UPDATED, "false");
		contentValues.put(IS_ACTIVE, "true");
		contentValues.put(IS_DELETED, "false");
		return db.insertOrThrow(LOOKBOOK_TABLE_NAME, null, contentValues);
	}

	public Cursor getLookbookItem(String id) {
		SQLiteDatabase db = this.getWritableDatabase();
		return db.rawQuery("select * from " + LOOKBOOK_TABLE_NAME + " where " + USER_ID + " = '" + id + "' AND "
				+ IS_DELETED + " = 'false'", null);
		// return db.query(LOOKBOOK_TABLE_NAME, null, USER_ID + "= ?",
		// new String[] { "" + id }, null, null, null);
	}

	// public void setLookbookUnFavourite(String lookbookId) {
	// String strSQL = "UPDATE " + LOOKBOOK_TABLE_NAME + " SET "
	// + IS_FAVOURITE + " = '" + isTrue + "' AND " + IS_UPDATED
	// + " = '" + isTrue + "' WHERE " + LOOKBOOK_ID + " = "
	// + lookbookId;
	// db.execSQL(strSQL);
	// }
	//
	// public void setLookbookFavourite(String lookbookId) {
	// String strSQL = " UPDATE " + LOOKBOOK_TABLE_NAME + " SET "
	// + IS_FAVOURITE + " = '" + isFalse + "' AND " + IS_UPDATED
	// + " = '" + isTrue + "' WHERE " + LOOKBOOK_ID + " = "
	// + lookbookId;
	// db.execSQL(strSQL);
	// }

	public void setLookbookFavourite(int lookbookid, String flag) {
		SQLiteDatabase db = this.getWritableDatabase();
		String strSQL = "UPDATE " + LOOKBOOK_TABLE_NAME + " SET " + IS_FAVOURITE + " = '" + flag + "', " + IS_UPDATED
				+ " = 'true' " + " WHERE " + LOOKBOOK_ID + " = " + lookbookid;
		System.out.println("strSQL      " + strSQL);
		db.execSQL(strSQL);
	}

	public void deleteLookbookItem(int lookbookId) {
		SQLiteDatabase db = this.getWritableDatabase();
		String strSQL = "UPDATE " + LOOKBOOK_TABLE_NAME + "  SET " + IS_DELETED + "  =  '" + isTrue + "' WHERE "
				+ LOOKBOOK_ID + "  =  " + lookbookId;
		db.execSQL(strSQL);
	}

	public void updateLookbook(int lookbookid, String name, String occasion, String comments) {
		SQLiteDatabase db = this.getWritableDatabase();
		String strSQL = "UPDATE " + LOOKBOOK_TABLE_NAME + " SET " + LOOKBOOK_NAME + " = '" + name + "', " + IS_UPDATED
				+ " = 'true' ," + LOOKBOOK_OCASSION + " = '" + occasion + "', " + COMMENTS + " = '" + comments + "' "
				+ " WHERE " + LOOKBOOK_ID + " = " + lookbookid;
		db.execSQL(strSQL);
	}

	public Cursor returnLookbook(int lookbookId) {
		// open();
		SQLiteDatabase db = this.getWritableDatabase();
		Cursor c = getLookbookItem(ParseUser.getCurrentUser().getObjectId());
		if (c.moveToFirst()) {
			do {
			} while (c.moveToNext());
		}
		return db.rawQuery("select * from " + LOOKBOOK_TABLE_NAME + " where " + LOOKBOOK_ID + " = " + lookbookId
				+ " AND " + IS_DELETED + " = 'false'", null);
	}

	// ==============================================================================================================Lookbook
	// table for Service

	public Cursor returnNewLookbook(String userId) {
		SQLiteDatabase db = this.getWritableDatabase();
		return db.rawQuery("select * from " + LOOKBOOK_TABLE_NAME + " where " + USER_ID + " = '" + userId + "' AND "
				+ IS_NEW + " = 'true'" + " AND " + IS_DELETED + " = 'false'", null);
	}

	public Cursor returnUpdatedLookbook(String userId) {
		SQLiteDatabase db = this.getWritableDatabase();
		return db.rawQuery("select * from " + LOOKBOOK_TABLE_NAME + " where " + USER_ID + " = '" + userId + "' AND "
				+ IS_UPDATED + " = 'true'" + " AND " + IS_DELETED + " = 'false'", null);
	}

	public Cursor returnDeletedLookbook(String userId) {
		SQLiteDatabase db = this.getWritableDatabase();
		return db.rawQuery("select * from " + LOOKBOOK_TABLE_NAME + " where " + USER_ID + " = '" + userId + "' AND "
				+ IS_DELETED + " = 'true'", null);
	}

	// ------------------------------------------------------------------------------------------------------Lookbook
	// table Adter server updation

	public void setNewLookbookSavedOnServer(String id) {
		SQLiteDatabase db = this.getWritableDatabase();
		String strSQL = "UPDATE " + LOOKBOOK_TABLE_NAME + " SET " + IS_NEW + " = 'false', " + IS_UPDATED + " = 'false'"
				+ " WHERE " + LOOKBOOK_ID + " = " + id;
		System.out.println("strSQ L      " + strSQL);
		db.execSQL(strSQL);
	}

	public void setUpdateLookbookSavedOnServer(String id) {
		SQLiteDatabase db = this.getWritableDatabase();
		String strSQL = "UPDATE " + LOOKBOOK_TABLE_NAME + " SET " + IS_UPDATED + " = 'false'" + " WHERE " + LOOKBOOK_ID
				+ " = " + id;
		System.out.println("strSQ L      " + strSQL);
		db.execSQL(strSQL);
	}

	public void setDeleteLookbookSavedOnServer(String id) {
		SQLiteDatabase db = this.getWritableDatabase();
		String strSQL2 = "DELETE from " + LOOKBOOK_TABLE_NAME + " WHERE " + LOOKBOOK_ID + " = " + id;
		db.execSQL(strSQL2);
	}

	// -------------------------------------------------------------------------------------------------------Planner

	public long insertPlanner(String user_id, String ocassion, String date, String time, int lookbookId,
			String img_path, String createdAt) {
		SQLiteDatabase db = this.getWritableDatabase();
		ContentValues contentValues = new ContentValues();
		contentValues.put(USER_ID, user_id);
		contentValues.put(LOOKBOOK_ID, lookbookId);
		contentValues.put(OCASSION, ocassion);
		contentValues.put(PLAN_TIME, time);
		contentValues.put(PLAN_DATE, date);
		contentValues.put(IMAGE_PATH, img_path);
		contentValues.put(CREATED_AT, createdAt);
		contentValues.put(IS_NEW, "true");
		contentValues.put(IS_UPDATED, "false");
		contentValues.put(IS_ACTIVE, "true");
		contentValues.put(IS_DELETED, "false");
		return db.insertOrThrow(PLANNER_TABLE_NAME, null, contentValues);
	}

	public Cursor returnAllPlanner(String userId) {
		SQLiteDatabase db = this.getWritableDatabase();
		return db.rawQuery("select * from " + PLANNER_TABLE_NAME + " where " + USER_ID + " = '" + userId + "' AND "
				+ IS_DELETED + " = 'false'", null);
	}

	public Cursor returnPlannerForDate(String userId, String date) {
		SQLiteDatabase db = this.getWritableDatabase();
		return db.rawQuery("select * from " + PLANNER_TABLE_NAME + " where " + USER_ID + " = '" + userId + "' AND "
				+ IS_DELETED + " = 'false'" + " AND " + PLAN_DATE + " LIKE '" + date + " %'", null);
	}

	public Cursor returnPlanner(int id) {
		SQLiteDatabase db = this.getWritableDatabase();
		return db.rawQuery("select * from " + PLANNER_TABLE_NAME + " where " + PLANNER_ID + " = " + id, null);
	}

	public void setDeleteplanner(int id) {
		SQLiteDatabase db = this.getWritableDatabase();
		String strSQL2 = "DELETE from " + PLANNER_TABLE_NAME + " WHERE " + PLANNER_ID + " = " + id;
		db.execSQL(strSQL2);
	}

	// =============================================================================================================CommonMethods
	public void createTeble() {
		SQLiteDatabase db = this.getWritableDatabase();
			db.execSQL(CREATE_USER_TABLE);
			db.execSQL(CREATE_CATEGORY_TABLE);
			db.execSQL(CREATE_WARDROBE_TABLE);
			db.execSQL(CREATE_WISHLIST_TABLE);
			db.execSQL(CREATE_LOOKBOOK_TABLE);
			db.execSQL(CREATE_PLANNER_TABLE);
			db.execSQL(CREATE_ASK_STYLIST_TABLE);
			db.execSQL(CREATE_ASK_STYLIST_TEMP_TABLE);

	}

	public boolean isTableExists(String tableName) {
		SQLiteDatabase db = this.getWritableDatabase();
		Cursor cursor = db.rawQuery("select DISTINCT tbl_name from sqlite_master where tbl_name = '" + tableName + "'",
				null);
		if (cursor != null) {
			if (cursor.getCount() > 0) {
				cursor.close();
				return true;
			}
			cursor.close();
		}
		return false;
	}


	public void updateUserInLocal(String userId,String user_name, String profile_pic_path, String gender, String occupation,
								  String birth_date, String bust, String waist, String hips, String height, String weight,
								  String size, String bottom_waist_size, String skin_coplexion, String characteristics, String love_color,
								  String pref_brand, String style_icon, String fashin_type, String outfits, String style_desription,
								  String about_yourself, String style, String shape, String created_at,String is_synced) {

		//query if user exist

		SQLiteDatabase db = this.getWritableDatabase();

		String query = "SELECT * FROM " + USER_TABLE_NAME + " WHERE " + PARSE_U_ID + "='" + userId + "'";
		Log.v("QUERY", query);

		String id = "";
		Cursor cursor = null;

		try {

			//cursor = db.rawQuery("SELECT EmployeeName FROM Employee WHERE EmpNo=?", new String[] {empNo + ""});

			cursor = db.rawQuery(query, null);

			if (cursor.getCount() > 0) {

				cursor.moveToFirst();
				id = cursor.getString(cursor.getColumnIndex(PARSE_U_ID));
				Log.v("From LOCAL id", id);


				//update


				updateUserProfile(userId, user_name, profile_pic_path, gender, occupation, birth_date, bust, waist, hips, height,

						weight, size, bottom_waist_size, skin_coplexion, characteristics, love_color, pref_brand, style_icon, fashin_type, outfits, style_desription, about_yourself,

						style, shape, created_at, is_synced);

			} else {

				//insert
				/*
				* String userId,String user_name, String profile_pic_path, String gender, String occupation,
								  String birth_date, String bust, String waist, String hips, String height, String weight,
								  String size, String bottom_waist_size, String skin_coplexion, String characteristics, String love_color,
								  String pref_brand, String style_icon, String fashin_type, String outfits, String style_desription,
								  String about_yourself, String style, String shape, String created_at*/
				insertUser(userId, user_name, profile_pic_path, gender, occupation, birth_date, bust, waist, hips, height,
						weight, size, bottom_waist_size, skin_coplexion, characteristics, love_color, pref_brand, style_icon, fashin_type, outfits, style_desription, about_yourself,
						style, shape, created_at, is_synced);

			}
		}catch (Error error)
		{
			error.printStackTrace();
		}
		catch (Exception error)
		{
			error.printStackTrace();
		}finally {

			if (cursor != null) {
				cursor.close();

			}

		}


	}



	public int updateUserInLocalfb(String userId,String user_name, String profile_pic_path){
		//query if user exist
		SQLiteDatabase db = this.getWritableDatabase();
		ContentValues cv = new ContentValues();
		cv.put(PARSE_U_ID, userId);
		cv.put(USER_NAME,user_name);
		cv.put(PROFILE_PIC,profile_pic_path);
		return db.update(USER_TABLE_NAME, cv, PARSE_U_ID +" = '" + userId+"'", null);

	}

	}



