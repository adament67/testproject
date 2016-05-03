package com.stylist.wardrobe;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.charset.Charset;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;
import java.util.Random;

import org.apache.http.client.methods.HttpPost;
import org.json.JSONObject;

import com.constants.BaseActivity;
import com.constants.Constants;
import com.constants.LocalDatabaseHandler;
import com.constants.Utils;

import com.parse.GetCallback;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.parse.SaveCallback;
import com.parse.entity.mime.HttpMultipartMode;
import com.parse.entity.mime.MultipartEntity;
import com.parse.entity.mime.content.FileBody;
import com.parse.entity.mime.content.StringBody;
//import com.stylist.CategoryService;
import com.stylist.R;
import com.stylist.rssfeed.SimpleHTTPConnection;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.ContentValues;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.ContactsContract.Profile;
import android.provider.MediaStore;
import android.provider.MediaStore.Images;
import android.provider.MediaStore.Images.Media;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

public class UploadWardrobeWithDescriptionPop extends BaseActivity {
	String categgenerateRandomoryName, catId, imgPath,newImgPath;
	com.constants.CustomTextView uploadBtn;
	EditText itemNameText, colourText, ocassionText, remarksText;
	Bitmap bitmap2;
	TextView nameText;
	String unique_image_id;
	// LinearLayout upprlayout;
	// LocalDatabaseHandler handler1;
	public static Activity act;




	public void onStart() {
		super.onStart();
		//.onStartSession(this, getResources().getString(R.string.flurryKey));
	}

	public void onStop() {
		super.onStop();
		//.onEndSession(this);
	}

	LocalDatabaseHandler dbHelper;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		/*progressDialog = new ProgressDialog(this);
		progressDialog.setMessage("Please Wait...");
		progressDialog.setCancelable(false);*/

		requestWindowFeature(Window.FEATURE_NO_TITLE);

		setContentView(R.layout.activity_upload_wardrobe_with_description_pop);
		uploadBtn = (com.constants.CustomTextView) findViewById(R.id.bottomText);
		uploadBtn.setOnClickListener(this);
		uploadBtn.setText("Save");
		act = this;
		/*
		 * upprlayout = (LinearLayout) findViewById(R.id.upprlayout);
		 * upprlayout.setOnClickListener(this);
		 */
		nameText = (TextView) findViewById(R.id.nameText);
		itemNameText = (EditText) findViewById(R.id.itemNameText);
		colourText = (EditText) findViewById(R.id.colourText);
		ocassionText = (EditText) findViewById(R.id.ocassionText);
		remarksText = (EditText) findViewById(R.id.remarksText);
//		categoryName = getPrefrence("categoryName");
//		nameText.setText(categoryName);
		catId = getPrefrence("categoryId");
		imgPath = getIntent().getStringExtra("imgPath");

		dbHelper = new LocalDatabaseHandler(this);

		/*
		 * handler1 = new LocalDatabaseHandler(context); handler1.open();
		 */
		try {
			bitmap2 = Utils.orientedBitmap(imgPath);
		} catch (Exception e) {
			// TODO: handle exception
		}
		// saveOnShare();
	}

	@Override
	protected void onPause() {
		super.onPause();
		if(dbHelper != null){

			dbHelper.close();
		}
	}

	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();

		if (dbHelper != null) {

			dbHelper.close();
		}
	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		uploadBtn.setClickable(true);

		// if (Utils.checkNetworkConnection(this)) {
		// if (getPrefrenceBool(Constants.isSyncing) != null &&
		// !getPrefrenceBool(Constants.isSyncing)) {
		// System.out.println("CategoryService home===========");
		// Intent in = new Intent(this, CategoryService.class);
		// in.putExtra("userId", ParseUser.getCurrentUser().getObjectId());
		// startService(in);
		// }
		// }
		// handler1.open();
	}
	//ProgressDialog uploadDialog;
	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		super.onClick(v);
		switch (v.getId()) {

		case R.id.bottomText:
		 //uploadDialog=	showDialog("Please wait!");
			uploadBtn.setClickable(false);
			uploadWardrobe();
			break;
		/*
		 * case R.id.upprlayout: finish();
		 * overridePendingTransition(R.anim.slide_in_up, R.anim.slide_out_down);
		 * break;
		 */
		default:
			break;
		}
	}




	//private ProgressDialog progressDialog;


	public void
	saveOnShare(Bitmap sourceBitmap) {


		if (sourceBitmap != null && !sourceBitmap.isRecycled()) {
			FileOutputStream out = null;
			File imageFile = Utils.getOutputMediaFileWard(context);
			try {
				out = new FileOutputStream(imageFile);
				Boolean imageSaved = sourceBitmap.compress(Bitmap.CompressFormat.PNG, 100, out);
			} catch (Exception e) {
			} finally {
				if (out != null) {
					try {
						out.flush();
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					try {
						out.close();
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			}
			try {
				String imageName = "IMG_" + ".png";
				ContentValues values = new ContentValues(3);
				values.put(MediaStore.Images.Media.TITLE, imageName);
				values.put(MediaStore.Images.Media.MIME_TYPE, "image/PNG");
				values.put("_data", imageFile.getAbsolutePath());
				getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);
				newImgPath = imageFile.getAbsolutePath();
				SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");


				if (Utils.checkNetworkConnection(UploadWardrobeWithDescriptionPop.this))
				{

					//progressDialog.show();

					Log.e("aaaaaaaaaaaaa", "uploaded parse");
					dbHelper.insertWardrobeItem(unique_image_id, ParseUser.getCurrentUser().getObjectId(), itemNameText.getText().toString(),
							colourText.getText().toString(), remarksText.getText().toString(),
							ocassionText.getText().toString(), catId, newImgPath, format.format(new Date()), "true","");//need to set bg color

					ParseQuery<ParseObject> query = ParseQuery.getQuery("Category");
					query.whereEqualTo("LocalDatabaseID", ParseUser.getCurrentUser().getObjectId()+""+catId);
					Log.e("LocalDatabaseID", "" + ParseUser.getCurrentUser().getObjectId() + "" + catId);

					query.getFirstInBackground(new GetCallback<ParseObject>() {
						public void done(ParseObject object, ParseException e) {
							if (object == null) {
								//uploadDialog.dismiss();
								Log.d("score", "The getFirst request failed.");
							} else {


								ByteArrayOutputStream stream = new ByteArrayOutputStream();
								BitmapFactory.decodeFile(newImgPath).compress(Bitmap.CompressFormat.PNG, 100, stream);
								ParseFile file = new ParseFile("image.png", stream.toByteArray());

								final ParseObject wardrobe = new ParseObject("WardrobeItem");
								wardrobe.put("ImageID", unique_image_id);
								wardrobe.put("Name", itemNameText.getText().toString());
								wardrobe.put("Color", colourText.getText().toString());
								wardrobe.put("Remarks", remarksText.getText().toString());

								wardrobe.put("CategoryId", object);

								wardrobe.put("DressCode", ocassionText.getText().toString());


								wardrobe.put("IsFavourite", false);
								wardrobe.put("IsActive", true);
								wardrobe.put("IsDeleted", false);
								wardrobe.put("ImageFile", file);
								wardrobe.put("UserId", ParseObject.createWithoutData("_User", ParseUser.getCurrentUser().getObjectId()));
								file.saveInBackground(new SaveCallback() {
									public void done(ParseException e) {
										// If successful add file to user and signUpInBackground
										if (null == e) {
											wardrobe.saveInBackground(new SaveCallback() {
												@Override
												public void done(ParseException e) {

													if (e != null) {
														//uploadDialog.dismiss();
														e.printStackTrace();
													} else {
													//	uploadDialog.dismiss();
														//progressDialog.dismiss();

														//showToastS("item uploaded");
													}
												}
											});
										} else {
											///uploadDialog.dismiss();
											e.printStackTrace();
										}
									}

								});

							}
						}
					});
				}
				else
				{
					setPrefrenceBool(Constants.isUpdated, true);
					dbHelper.insertWardrobeItem(unique_image_id, ParseUser.getCurrentUser().getObjectId(), itemNameText.getText().toString(),
							colourText.getText().toString(), remarksText.getText().toString(),
							ocassionText.getText().toString(), catId, newImgPath, format.format(new Date()), "false","");//need to set bg color

					//uploadDialog.dismiss();

				}



				deleteFile(imgPath);
			} catch (Exception e) {
			}
		}
else
		{
			//uploadDialog.dismiss();
		}
	}


	@Override
	public void onBackPressed() {
		// TODO Auto-generated method stub
		super.onBackPressed();
		overridePendingTransition(R.anim.slide_in_up, R.anim.slide_out_down);
	}

	private class UpdateUserInfo extends AsyncTask<String, Void, String> {

		@Override
		protected String doInBackground(String... params) {
			HttpPost httppost = null;
			try {
				System.out.println("imgPath==============uploadimage========" + imgPath);
				MultipartEntity mp = new MultipartEntity(HttpMultipartMode.BROWSER_COMPATIBLE);
				// http://mobileappsdev.net/estilorobe/webservices/Index.php/uploadimage

				// http://52.37.36.159/webservices/Index.php/uploadimage
				httppost = new HttpPost("http://54.201.113.167/admin/webservices/Index.php/uploadimage");
				try {
					mp = new MultipartEntity(HttpMultipartMode.BROWSER_COMPATIBLE);
					mp.addPart("unique_image_id", new StringBody(unique_image_id, "text/plain",
							Charset.forName("UTF-8")));
					// mp.addPart("display_name",
					// new StringBody(ParseUser.getCurrentUser().getUsername(),
					// "text/plain", Charset.forName("UTF-8")));
					if (!imgPath.equals("")) {
						mp.addPart("file_name", new FileBody(new File(imgPath), "image/jpeg"));
					}
					httppost.setEntity(mp);
				} catch (Exception e) {
					e.printStackTrace();
				}

			} catch (Exception e) {
				e.printStackTrace();
			}
			return SimpleHTTPConnection.sendByPOST(httppost);
		}

		@Override
		protected void onPostExecute(String result) {
			System.out.println("Result Save Profile Detail -->  " + result);
			try {
				JSONObject obj = new JSONObject(result);
				if (obj.optString("status") != null && obj.optString("status").equalsIgnoreCase("Success")) {
					/*
					 * Toast.makeText(UploadWardrobeWithDescriptionPop.this,
					 * "updated sucessfully", Toast.LENGTH_SHORT) .show();
					 */
					finish();

				} else {
					Toast.makeText(UploadWardrobeWithDescriptionPop.this, "Error! Try again", Toast.LENGTH_SHORT)
							.show();

				}
			} catch (Exception e) {
				// TODO: handle exception
			}

		}

		@Override
		protected void onPreExecute() {
			super.onPreExecute();

		}

	}

	public void uploadWardrobe() {

		if (itemNameText.getText().toString().equals(""))
			showToastS("Please enter Item name... ");

		else {
			unique_image_id=generateRandom();
			saveOnShare(BitmapFactory.decodeFile(imgPath));

			/*
			 * long lng =
			 * handler1.insertWardrobeItem(ParseUser.getCurrentUser().
			 * getObjectId(), itemNameText.getText().toString(),
			 * colourText.getText().toString(),
			 * remarksText.getText().toString(),
			 * ocassionText.getText().toString(), catId, imgPath,
			 * format.format(new Date()));
			 */
			finish();



			checkAndCloseActivity(UploadWardribeActivity.act);
			checkAndCloseActivity(UploadOptionWardrobeActivity.act);
			showToastS("Item has been saved to your wardrobe.");
			new UpdateUserInfo().execute(imgPath);
		}
	}

	@Override
	public void AsynctaskResultwithoutArray(String arr, String url, Map<String, Object> par) {
		// TODO Auto-generated method stub
		super.AsynctaskResultwithoutArray(arr, url, par);
		if (url.equals("insertWardrobe")) {
			System.out.println("Item Uploaded======");
			showToastS("Item Uploaded !");
			checkAndCloseActivity(UploadWardribeActivity.act);
			checkAndCloseActivity(UploadOptionWardrobeActivity.act);
			finish();



		}
	}


	String generateRandom() {
		char[] alphNum = "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789".toCharArray();

		Random rnd = new Random();

		StringBuilder sb = new StringBuilder((1 + rnd.nextInt(9)));
		for (int i = 0; i < 6; i++)
			sb.append(alphNum[rnd.nextInt(alphNum.length)]);

		String id = sb.toString();

		System.out.println("random number==========" + id);
		return id;
	}
	
	


	}


