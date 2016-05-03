package com.stylist.wardrobe;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.channels.FileChannel.MapMode;
import java.nio.charset.Charset;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

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
import com.stylist.CroppingActivity;
import com.stylist.R;
import com.stylist.SomeView;
import com.stylist.rssfeed.SimpleHTTPConnection;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.DashPathEffect;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Point;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import org.apache.http.client.methods.HttpPost;
import org.json.JSONObject;


public class UploadWardribeActivity extends BaseActivity {

	RelativeLayout saveRelLayout;
	ImageView compositeImageView;
	boolean crop;
	String newImgPath;
	Bitmap bitmap2;
	String catId, categoryName;

	Dialog uploadDialog;
	String unique_image_id;
	int catSelPos = 0, rotateCount, heightF;
	int screenWidth, screenHeight;
	public ArrayList<String> categoryNameList = new ArrayList<String>();
	public ArrayList<String> categoryId = new ArrayList<String>();
	public static UploadWardribeActivity act;
	float ratio,ratio_width;
	RelativeLayout container;
	FrameLayout cropLayout;
	Boolean isWardrobeUploaded=false;

	int minusHeight = 0;
	int resultImageWidth;
	int resultImageheight;
	int croppedImageWidth;
	int croppedImageheight;
	String itemName,occasionName,color,remarks;
	ProgressDialog progressDialog;
	//Bitmap dstBmp;
	String tempSavedFilePath;

	String rgb;
	float density;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_upload_wardribe);
		setHeading("UPLOAD WARDROBE");
		//backClick();
		getScreenDimention(this);
		DisplayMetrics display = new DisplayMetrics();
		getWindowManager().getDefaultDisplay().getMetrics(display);


		FrameLayout backBtn = (FrameLayout) this.findViewById(R.id.backButtonImage);
		backBtn.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				finishActivity();

			}
		});


		screenWidth = display.widthPixels;
		screenHeight = display.heightPixels;
		System.out.println("screenWidth  " + screenWidth);
		System.out.println("screenHeight  " + screenHeight);

		act = this;
		saveRelLayout = (RelativeLayout) findViewById(R.id.saveRelLayout);
		saveRelLayout.setOnClickListener(this);
		cropLayout = (FrameLayout) findViewById(R.id.cropLayout_nn);
		container = (RelativeLayout) findViewById(R.id.container);
		container.setDrawingCacheEnabled(true);
		cropLayout.setDrawingCacheEnabled(true);
		saveRelLayout.setVisibility(View.VISIBLE);
		categoryName = getIntent().getStringExtra("categoryName");
		rotateCount = getIntent().getIntExtra("rotateCount", 0);
		dbHelper = new LocalDatabaseHandler(this);
		catId =getIntent().getStringExtra("categoryId");
		heightF = getIntent().getIntExtra("heightF", 0);
		Utils.setupUI(findViewById(R.id.container), this);
		Bundle extras = getIntent().getExtras();
		if (extras != null) {
			crop = extras.getBoolean("crop");
			resultImageWidth = extras.getInt("width");
			resultImageheight = extras.getInt("height");
			if(resultImageheight>screenHeight)
			{
				resultImageheight=screenHeight;
			}
			minusHeight = ((screenHeight / 2) - (resultImageheight / 2));

			ratio = (resultImageheight / (float) resultImageWidth);
			ratio_width = (resultImageWidth / (float) resultImageheight);
		}
		DisplayMetrics dm = new DisplayMetrics();
		try {
			getWindowManager().getDefaultDisplay().getMetrics(dm);
		} catch (Exception ex) {
		}
		compositeImageView = (ImageView) findViewById(R.id.our_imageview);


		newImgPath = getIntent().getStringExtra("myPath");


		/*
		I/Adreno-EGL: <qeglDrvAPI_eglInitialize:410>: EGL 1.4 QUALCOMM build: AU_LINUX_ANDROID_LNX.LA.3.5.2.2.2_RB1.04.04.04.154.004_msm8226_LNX.LA.3.5.2.2.2_RB1__release_AU ()
                                                         OpenGL ES Shader Compiler Version: E031.24.00.15
                                                         Build Date: 08/06/14 Wed
                                                         Local Branch: mybranch4057433
                                                         Remote Branch: quic/LNX.LA.3.5.2.2.2_rb1
                                                         Local Patches: NONE
                                                         Reconstruct Branch: AU_LINUX_ANDROID_LNX.LA.3.5.2.2.2_RB1.04.04.04.154.004 +  NOTHING
		 */



		bitmap2 = Utils.rotateImage(Utils.orientedBitmap(newImgPath), rotateCount * 90);

		Log.v("AAAA_w", bitmap2.getWidth()+"" );
		Log.v("AAAA_h", bitmap2.getHeight()+"" );

		if (crop) {

			//---------------------aa
			Bitmap bbb = BitmapFactory.decodeFile(newImgPath);


			bitmap2 =  Utils.BITMAP_RESIZER(bitmap2, resultImageWidth, resultImageheight);


			bitmap2 = convertToMutable(bitmap2);
			Bitmap resultingImage = Bitmap.createBitmap(resultImageWidth, resultImageheight, bitmap2.getConfig());

			Canvas canvas = new Canvas(resultingImage);
			Paint paint = new Paint();
			paint.setAntiAlias(true);
			Path path = new Path();

			if (SomeView.finalPoints.size() > 0) {
				path.moveTo(SomeView.finalPoints.get(0).x, SomeView.finalPoints.get(0).y);
				for (int i = 0; i < SomeView.finalPoints.size(); i++) {
					path.lineTo(SomeView.finalPoints.get(i).x, SomeView.finalPoints.get(i).y);
				}
			}
			canvas.drawPath(path, paint);

			paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN)); //show inside pixels
			canvas.drawBitmap(bitmap2, 0, 0, paint);
			Paint paint1 = new Paint(Paint.ANTI_ALIAS_FLAG);
			paint1.setStyle(Paint.Style.STROKE);
			paint1.setPathEffect(new DashPathEffect(new float[]{7, 14}, 0));
			paint1.setStrokeWidth(3);
			paint1.setColor(lighter(Color.WHITE, 0.1f));

			canvas.drawPath(path, paint1);
			getRecttPoint();

			Bitmap cropedBitmap = getCroppedBitmapDup(resultingImage,compositeImageView);
			HashMap<String, String> rgbData = Utils.getAvgRGB(cropedBitmap);

			rgb = rgbData.get("r")+","+rgbData.get("g")+","+rgbData.get("b");

			Log.v("AVG_RGB", rgb);


		} else {

			compositeImageView.setImageBitmap(bitmap2);
			HashMap<String, String> rgbData = Utils.getAvgRGB(bitmap2);

			rgb = rgbData.get("r")+","+rgbData.get("g")+","+rgbData.get("b");

			Log.v("AVG_RGB", rgb);
		}
	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();


	}

	public static int lighter(int color, float factor) {

		float[] hsv = new float[3];
		Color.colorToHSV(color, hsv);
		hsv[2] *= hsv[2] = 1.0f - 0.8f * (1.0f - hsv[2]);
		color = Color.HSVToColor(hsv);
		return color;
	}

	@Override
	public void onClick(View v) {

		switch (v.getId()) {

			case R.id.saveRelLayout:
				uploadDialog();
				break;

			default:
				break;
		}
	}


	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
//
//		File tempDir = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES), Constants.IMAGE_DIRECTORY_NAME+"/"+Constants.IMAGE_TEMP_CROPED);
//		Log.v("File_path", tempDir.getPath());
//		DeleteRecursive(tempDir);

		/*String filePath = tempDir.getAbsolutePath();
		tempDir.delete();*/




		//sendBroadcast(new Intent(Intent.ACTION_MEDIA_MOUNTED, Uri.parse("file://" + Environment.getExternalStorageDirectory())));

		if (dbHelper != null) {

			dbHelper.close();
		}
	}




	private class UpdateUserInfo extends AsyncTask<String, Void, String> {

		@Override
		protected String doInBackground(String... params) {
			HttpPost httppost = null;
			try {
				System.out.println("imgPath==============uploadimage========" + newImgPath);
				MultipartEntity mp = new MultipartEntity(HttpMultipartMode.BROWSER_COMPATIBLE);

				httppost = new HttpPost("http://54.201.113.167/admin/webservices/Index.php/uploadimage");
				try {
					mp = new MultipartEntity(HttpMultipartMode.BROWSER_COMPATIBLE);
					mp.addPart("unique_image_id", new StringBody(unique_image_id, "text/plain",
							Charset.forName("UTF-8")));

					if (!newImgPath.equals("")) {
						File file=new File(newImgPath);
						double kilobytes = (file.length() / 1024);
						double megabytes = (kilobytes / 1024);
						Log.v("SIZE_IMAGE_FULL", "MB: "+megabytes+", KB: "+kilobytes);
						mp.addPart("file_name", new FileBody(file, "image/jpeg"));
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

					showToastS("Item has been saved to your wardrobe.");
				} else {

				}
			} catch (Exception e) {
				// TODO: handle exception
				e.printStackTrace();
			}

		}

		@Override
		protected void onPreExecute() {
			super.onPreExecute();

		}

	}


	@Override
	protected void onPause() {
		super.onPause();
		if(dbHelper != null){

			dbHelper.close();
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




	private void uploadDialog() {
		try {

			uploadDialog = addDialog(R.layout.activity_upload_wardrobe_with_description_pop);

			final EditText itemNameText, colourText, ocassionText, remarksText;
			TextView nameText;

			final com.constants.CustomTextView uploadBtn;
			//final Button cropBut = (Button) cropConfirm.findViewById(R.id.cropButn);
			nameText = (TextView)uploadDialog. findViewById(R.id.nameText);
			itemNameText = (EditText)uploadDialog. findViewById(R.id.itemNameText);
			colourText = (EditText)uploadDialog. findViewById(R.id.colourText);
			ocassionText = (EditText)uploadDialog. findViewById(R.id.ocassionText);
			remarksText = (EditText)uploadDialog. findViewById(R.id.remarksText);
			nameText.setText(categoryName);
			uploadBtn = (com.constants.CustomTextView)uploadDialog.findViewById(R.id.bottomText);
			uploadBtn.setText("Save");

			uploadBtn.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) {

					//hide soft keyboard
					InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
					imm.hideSoftInputFromWindow(uploadBtn.getWindowToken(), 0);

					if (itemNameText.getText().toString().equals(""))
						showToastS("Please enter Item name... ");

					else {
						progressDialog=showDialog("please wait !");
						itemName = itemNameText.getText().toString();
						occasionName = ocassionText.getText().toString();
						color = colourText.getText().toString();
						remarks = remarksText.getText().toString();
						unique_image_id = generateRandom();
						saveinParseAndDb(newImgPath);
						//saveOnShare(dstBmp);


					}
				}
			});



		}
		catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		uploadDialog.show();

	}



	@Override
	public void AsynctaskResultwithoutArray(String arr, String url, Map<String, Object> par) {
		// TODO Auto-generated method stub
		super.AsynctaskResultwithoutArray(arr, url, par);
		if (url.equals("insertWardrobe")) {
			showToastS("Item Uploaded !");
			uploadDialog.dismiss();
			progressDialog.dismiss();
			checkAndCloseActivity(UploadOptionWardrobeActivity.act);
			checkAndCloseActivity(CroppingActivity.cCroppingActivity);

			finish();
		}
	}


	LocalDatabaseHandler dbHelper;



	public String getTempImgBgFilename1() {

		File file = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES), Constants.IMAGE_DIRECTORY_NAME/*+"/"+Constants.IMAGE_TEMP_CROPED*/);
		if (!file.exists()) {
			file.mkdirs();
		}

		String uriSting = (file.getAbsolutePath() + "/" + "sys.jpg");
		System.out.println("uriSting-----------------" + uriSting);
		return uriSting;

	}


	public void saveOnShare(Bitmap sourceBitmap) {


		if (sourceBitmap != null && !sourceBitmap.isRecycled()) {


			FileOutputStream out = null;
			//     File imageFile = Utils.getOutputMediaFileWard(this);

			tempSavedFilePath=getTempImgBgFilename1();
			System.out.println("uriSting111111-----------------" + tempSavedFilePath);

			File imageFile = new File(tempSavedFilePath);
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
				//newImgPath = imageFile.getAbsolutePath();


				//
				//
				// saveOnShare(newImgPath,true);
				System.out.println("nnnn  imgPath"+newImgPath);
			} catch (Exception e) {
				e.printStackTrace();
			}

		} else
		{
			Utils.write("bitmap is nullllllllll" );
		}
	}


	public void saveinParseAndDb(String imgpath) {

		if (imgpath != null && !imgpath.equals("")) {
			if(!crop)
			{
				String dbPath;
				try {
					dbPath = Utils.getOutputMediaFileWard(this).getAbsolutePath();
					Utils.write("dbPath========" + dbPath);
					copyFile(new File(newImgPath), new File(dbPath));
					Utils.write("deleteImage========" + newImgPath);

					newImgPath=dbPath;
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			else
			{
				newImgPath = Utils.getOutputMediaFileWard(this).getAbsolutePath();
				Utils.write("newImgPath========="+newImgPath);
				Utils.write("tempSavedFilePath========="+tempSavedFilePath);

				try {
					copyFile(new File(tempSavedFilePath),new File(newImgPath));
				} catch (IOException e) {
					e.printStackTrace();
				}

			}
			try {



				SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");

				if (Utils.checkNetworkConnection(UploadWardribeActivity.this)) {

					Log.e("uploaded", "uploaded parse");
					dbHelper.insertWardrobeItem(unique_image_id, ParseUser.getCurrentUser().getObjectId(), itemName,
							color, remarks,
							occasionName, catId, newImgPath, format.format(new Date()), "false",rgb);//need to pass bg color

					ParseQuery<ParseObject> query = ParseQuery.getQuery("Category");
					query.whereEqualTo("LocalDatabaseID", ParseUser.getCurrentUser().getObjectId()+""+catId);
					Log.e("LocalDatabaseID", "" + ParseUser.getCurrentUser().getObjectId() + "" + catId);

					query.getFirstInBackground(new GetCallback<ParseObject>() {
						public void done(ParseObject object, ParseException e) {
							if (object == null) {
								//uploadDialog.dismiss();
								Log.e("uploaded obj null", "The getFirst null request failed.");

								progressDialog.dismiss();
								uploadDialog.dismiss();
								checkAndCloseActivity(UploadOptionWardrobeActivity.act);
								finish();

							} else {

								Log.e("uploaded success", "gogoggo.");

								ByteArrayOutputStream stream = new ByteArrayOutputStream();
								BitmapFactory.decodeFile(newImgPath).compress(Bitmap.CompressFormat.PNG, 50, stream);
								ParseFile file = new ParseFile("image.png", stream.toByteArray());

								final ParseObject wardrobe = new ParseObject("WardrobeItem");
								wardrobe.put("ImageID", unique_image_id);
								wardrobe.put("Name", itemName);
								wardrobe.put("Color", color);
								wardrobe.put("Remarks", remarks);

								wardrobe.put("CategoryId", object);

								wardrobe.put("DressCode", occasionName);


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
														progressDialog.dismiss();
														uploadDialog.dismiss();
														checkAndCloseActivity(UploadOptionWardrobeActivity.act);
														finish();
														//e.printStackTrace();

													} else {
														progressDialog.dismiss();
														uploadDialog.dismiss();

														checkAndCloseActivity(UploadOptionWardrobeActivity.act);
														finish();

													}

												}
											});
										} else {
											e.printStackTrace();

											progressDialog.dismiss();
											uploadDialog.dismiss();

											checkAndCloseActivity(UploadOptionWardrobeActivity.act);
											finish();



										}
									}

								});

							}
						}
					});
				}
				else {
					setPrefrenceBool(Constants.isUpdated, true);
					dbHelper.insertWardrobeItem(unique_image_id, ParseUser.getCurrentUser().getObjectId(), itemName,
							color, remarks,
							occasionName, catId, newImgPath, format.format(new Date()), "true",rgb);//need to pass bg color
					progressDialog.dismiss();
					uploadDialog.dismiss();
					checkAndCloseActivity(UploadOptionWardrobeActivity.act);
					finish();
					showToastS("Item has been saved to your wardrobe.");
				}


				//delete
//				File tempDir = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES), Constants.IMAGE_DIRECTORY_NAME);
//				Log.v("File_path", tempDir.getPath());
//				DeleteRecursive(tempDir);


			} catch (Exception e) {

				e.printStackTrace();

				progressDialog.dismiss();
				uploadDialog.dismiss();
				checkAndCloseActivity(UploadOptionWardrobeActivity.act);
				finish();
			}
		}
		else
		{
			progressDialog.dismiss();
			uploadDialog.dismiss();
			checkAndCloseActivity(UploadOptionWardrobeActivity.act);
			finish();

		}
	}



//	void DeleteRecursive(File fileOrDirectory) {
//
//		if (fileOrDirectory.isDirectory())
//			for (File child : fileOrDirectory.listFiles())
//				DeleteRecursive(child);
//
//		fileOrDirectory.delete();
//
//		MediaScannerConnection.scanFile(context, new String[]{fileOrDirectory.getAbsolutePath()}, null, null);

	//}


	public static Bitmap convertToMutable(Bitmap imgIn) {
		try {
			// this is the file going to use temporally to save the bytes.
			// This file will not be a image, it will store the raw image data.
			File file = new File(Environment.getExternalStorageDirectory() + File.separator + "temp.tmp");

			// Open an RandomAccessFile
			// Make sure you have added uses-permission
			// android:name="android.permission.WRITE_EXTERNAL_STORAGE"
			// into AndroidManifest.xml file
			RandomAccessFile randomAccessFile = new RandomAccessFile(file, "rw");

			// get the width and height of the source bitmap.
			int width = imgIn.getWidth();
			int height = imgIn.getHeight();
			Config type = imgIn.getConfig();

			// Copy the byte to the file
			// Assume source bitmap loaded using options.inPreferredConfig =
			// Config.ARGB_8888;
			FileChannel channel = randomAccessFile.getChannel();
			MappedByteBuffer map = channel.map(MapMode.READ_WRITE, 0, imgIn.getRowBytes() * height);
			imgIn.copyPixelsToBuffer(map);
			// recycle the source bitmap, this will be no longer used.
			imgIn.recycle();
			System.gc();// try to force the bytes from the imgIn to be released

			// Create a new bitmap to load the bitmap again. Probably the memory
			// will be available.
			imgIn = Bitmap.createBitmap(width, height, type);
			map.position(0);
			// load it back from temporary
			imgIn.copyPixelsFromBuffer(map);
			// close the temporary file and channel , then delete that also
			channel.close();
			randomAccessFile.close();

			// delete the temp file
			file.delete();

		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

		return imgIn;
	}

	@SuppressLint("NewApi")
	private void getScreenDimention(Context context) {


			DisplayMetrics metrics = new DisplayMetrics();
			getWindowManager().getDefaultDisplay().getMetrics(metrics);

			density = metrics.density;

			Utils.write("density========="+density);
			Utils.write("densityDPI========="+metrics.densityDpi);

			/*Point size = new Point();
			try {
				((Activity) context).getWindowManager().getDefaultDisplay().getRealSize(size);
				int y=size.y;
				int x=size.x;
				density=y/(float)(x);
			} catch (NoSuchMethodError e) {
			}*/


	}


	private Bitmap getCroppedBitmapDup(Bitmap resultingImage,ImageView imgView) {
	Bitmap dstBmp;


		DisplayMetrics display = new DisplayMetrics();
		getWindowManager().getDefaultDisplay().getMetrics(display);

		screenWidth = display.widthPixels;
		screenHeight = display.heightPixels;

		Log.v("1111Screen_width", screenWidth+"" );
		Log.v("1111Screen_height", screenHeight+"" );

		Log.v("1111minus_height", (screenHeight-resultingImage.getHeight())+"");
		Log.v("1111minus_heightTop", (screenHeight - resultingImage.getHeight()) / 2 + "");

		Utils.write("crop minusHeight=======" + minusHeight);

		Utils.write("crop sir  minusHeight=======" + (screenHeight - resultingImage.getHeight()) / 2);

		Utils.write("crop leftmostPoint=======" + leftmostPoint);
		Utils.write("crop topmostPoint======="+topmostPoint);
		Utils.write("crop rightmostPoint======="+rightmostPoint);
		Utils.write("crop bottommostPoint======="+bottommostPoint);
		Utils.write("crop croppedImageheight======="+croppedImageheight);
		Utils.write("crop croppedImageWidth======="+croppedImageWidth);



		int calTop=(topmostPoint-minusHeight);
		int calHeight=(croppedImageheight+((screenHeight - resultingImage.getHeight()) / 2));



		Utils.write("crop calTopth======="+calTop);
		Utils.write("crop calHeightth======="+calHeight);
		if(calTop<0) {
			calTop = 0;

		}
		if(calHeight>resultingImage.getHeight()) {
			calHeight = resultImageheight;
		}

		dstBmp = Bitmap.createBitmap(resultingImage, leftmostPoint, calTop, croppedImageWidth,calHeight);
		System.out.println("not  minus cropped     ");


		FrameLayout.LayoutParams params = (FrameLayout.LayoutParams) compositeImageView.getLayoutParams();
		params.gravity = Gravity.CENTER;
		params.topMargin = minusHeight;

		imgView.setLayoutParams(params);


		imgView.setImageBitmap(dstBmp);
		saveOnShare(dstBmp);

		return dstBmp;


     }

	int leftmostPoint, rightmostPoint, topmostPoint, bottommostPoint;

	private void getRecttPoint() {
		if(SomeView.points.size()>0)
		{
			leftmostPoint = SomeView.points.get(0).x;
			rightmostPoint = SomeView.points.get(0).x;
			topmostPoint = SomeView.points.get(0).y;
			bottommostPoint = SomeView.points.get(0).y;
			for (int i = 0; i < SomeView.points.size(); i++) {
				if (leftmostPoint > SomeView.points.get(i).x)
					leftmostPoint = SomeView.points.get(i).x;
				if (rightmostPoint < SomeView.points.get(i).x)
					rightmostPoint = SomeView.points.get(i).x;
				if (topmostPoint > SomeView.points.get(i).y)
					topmostPoint = SomeView.points.get(i).y;
				if (bottommostPoint < SomeView.points.get(i).y)
					bottommostPoint = SomeView.points.get(i).y;
			}

			if(topmostPoint<minusHeight)
				topmostPoint=minusHeight;

			if(bottommostPoint>bitmap2.getHeight())
			bottommostPoint=bitmap2.getHeight();

			Utils.write("bitmap actual height========"+bitmap2.getHeight());

			croppedImageWidth = (rightmostPoint - leftmostPoint);
			croppedImageheight = (bottommostPoint - topmostPoint);

			Utils.write("croppedImageheight===in point cal====="+croppedImageheight);
			Utils.write("croppedImagewidth===in point cal====="+croppedImageWidth);
		}
	}
}