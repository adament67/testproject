package com.stylist.wishlist;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.channels.FileChannel.MapMode;
import java.util.ArrayList;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.DashPathEffect;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Point;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.constants.BaseActivity;
import com.constants.Constants;
import com.constants.Utils;
import com.stylist.R;
import com.stylist.SomeView;

public class UploadWishListActivity extends BaseActivity {
	ImageView itemImage;
	String imgPath;
	TextView headerName;
	LinearLayout backButtonLayout;
	String catId;
	int catSelPos = -1, rotateCount = 0;
	Boolean crop;
	int screenWidth, screenHeight, heightF;
	public ArrayList<String> categoryName = new ArrayList<String>();
	public ArrayList<String> categoryId = new ArrayList<String>();
	RelativeLayout saveRelLayout;
	public static UploadWishListActivity act;
	int minusHeight=0;
	int resultImageWidth;
	int resultImageheight;
	int croppedImageWidth;
	int croppedImageheight;
	Bitmap bitmap2;
float ratio_width;
	int density;
	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		saveRelLayout.setClickable(true);
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_upload_wish_list);
		getScreenDimention(this);


		getScreenDimention(this);
		DisplayMetrics display = new DisplayMetrics();
		getWindowManager().getDefaultDisplay().getMetrics(display);

		screenWidth = display.widthPixels;
		screenHeight = display.heightPixels;
		System.out.println("screenWidth  " + screenWidth);
		System.out.println("screenHeight  " + screenHeight);




		itemImage = (ImageView) findViewById(R.id.our_imageview);
		//setHeading("UPLOAD WISHLIST");
		//backClick();
		FrameLayout backBtn = (FrameLayout) this.findViewById(R.id.backButtonImage);
		backBtn.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				finishActivity();

			}
		});
		saveRelLayout = (RelativeLayout) findViewById(R.id.saveRelLayout);
		saveRelLayout.setOnClickListener(this);
		saveRelLayout.setVisibility(View.VISIBLE);
		act = this;
		imgPath = getIntent().getStringExtra("myPath");
		
		System.out.println("imgPath========"+imgPath);

		rotateCount = getIntent().getIntExtra("rotateCount", 0);
		heightF = getIntent().getIntExtra("heightF", 0);
		Utils.setupUI(findViewById(R.id.container), this);
		Bundle extras = getIntent().getExtras();
		bitmap2 = Utils.rotateImage(Utils.orientedBitmap(imgPath),
				rotateCount * 90);
		if (extras != null) {
			crop = extras.getBoolean("crop");
			resultImageWidth = extras.getInt("width");
			resultImageheight = extras.getInt("height");

			if(resultImageheight>screenHeight)
			{
				resultImageheight=screenHeight;
			}

			minusHeight = ((screenHeight / 2) - (resultImageheight / 2));
		}
		DisplayMetrics dm = new DisplayMetrics();
		try {
			getWindowManager().getDefaultDisplay().getMetrics(dm);
		} catch (Exception ex) {
		}
		if (crop) {
			try {
				int width = bitmap2.getWidth();
				int height = bitmap2.getHeight();
				float ratio = (height / (float) width);
				ratio_width = (resultImageWidth / (float) resultImageheight);

				width = screenWidth;

				height = (int) (width / ratio);

				bitmap2 = Utils.BITMAP_RESIZER(bitmap2,
						resultImageWidth, resultImageheight);
				bitmap2 = convertToMutable(bitmap2);
				System.out.println("heightF========" + height);
				Bitmap resultingImage = Bitmap.createBitmap(resultImageWidth,
						resultImageheight, bitmap2.getConfig());
				Canvas canvas = new Canvas(resultingImage);
				Paint paint = new Paint();
				paint.setAntiAlias(true);
				Path path = new Path();
				if(SomeView.finalPoints.size()>0)
				{
				path.moveTo(SomeView.finalPoints.get(0).x,
						SomeView.finalPoints.get(0).y);
				for (int i = 0; i < SomeView.finalPoints.size(); i++) {
					path.lineTo(SomeView.finalPoints.get(i).x,
							SomeView.finalPoints.get(i).y);
				}
				}
				canvas.drawPath(path, paint);
				paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
				canvas.drawBitmap(bitmap2, 0, 0, paint);

				Paint paint1 = new Paint(Paint.ANTI_ALIAS_FLAG);
				paint1.setStyle(Paint.Style.STROKE);
				paint1.setPathEffect(new DashPathEffect(new float[]{7, 14}, 0));
				paint1.setStrokeWidth(3);
				paint1.setColor(lighter(Color.WHITE, 0.1f));
				canvas.drawPath(path, paint1);
				getRecttPoint();

				Bitmap cropedBitmap = getCroppedBitmapDup(resultingImage,itemImage);

			} catch (Exception e) {
				// TODO: handle exception
			}
		}

		else {
			itemImage.setImageBitmap(bitmap2);

		}
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
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.saveRelLayout:
			saveRelLayout.setClickable(false);
			Intent i = new Intent(context,
					UploadWishListDescriptionActivity.class);
			System.out.println("newPath imgPath========"+imgPath);
			i.putExtra("imgPath", imgPath);
			i.putExtra("rotateCount", rotateCount);
			startActivityForResult(i, 1);
			overridePendingTransition(R.anim.slide_in_up, R.anim.slide_out_up);
			break;
		default:
			break;
		}
	}

	public void saveOnShare(Bitmap sourceBitmap) {
		if (sourceBitmap != null && !sourceBitmap.isRecycled()) {

			FileOutputStream out = null;
			String tempSavedFilePath=getTempImgBgFilename1();
			File imageFile = new File(tempSavedFilePath);
			try {
				out = new FileOutputStream(imageFile);
				Boolean imageSaved = sourceBitmap.compress(
						Bitmap.CompressFormat.PNG, 100, out);

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
				imgPath = imageFile.getAbsolutePath();
				System.out.println("nnnn  imgPath"+imgPath);
			} catch (Exception e) {
			}

		}
	}





	public String getTempImgBgFilename1() {

		File file = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES), Constants.IMAGE_DIRECTORY_NAME/*+"/"+Constants.IMAGE_TEMP_CROPED*/);
		if (!file.exists()) {
			file.mkdirs();
		}

		String uriSting = (file.getAbsolutePath()  +"/"+ "sys.jpg");
		System.out.println("uriSting-----------------" + uriSting);
		return uriSting;

	}




	public static Bitmap convertToMutable(Bitmap imgIn) {
		try {
			// this is the file going to use temporally to save the bytes.
			// This file will not be a image, it will store the raw image data.
			File file = new File(Environment.getExternalStorageDirectory()
					+ File.separator + "temp.tmp");

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
			MappedByteBuffer map = channel.map(MapMode.READ_WRITE, 0,
					imgIn.getRowBytes() * height);
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

		if (Build.VERSION.SDK_INT >= 7) {
			Point size = new Point();
			try {
				((Activity) context).getWindowManager().getDefaultDisplay().getRealSize(size);
				density=1;
			} catch (NoSuchMethodError e) {
			}

		} else {
			DisplayMetrics metrics = new DisplayMetrics();
			((Activity) context).getWindowManager().getDefaultDisplay().getMetrics(metrics);
			density=metrics.densityDpi;
		}
		Utils.write("density========="+density);

	}
	private Bitmap getCroppedBitmapDup(Bitmap resultingImage,ImageView imgView) {
		Bitmap dstBmp;


		Log.v("111Bitmap_width", resultingImage.getWidth() + "");
		Log.v("111Bitmap_height", resultingImage.getHeight() + "");



		DisplayMetrics display = new DisplayMetrics();
		getWindowManager().getDefaultDisplay().getMetrics(display);

		screenWidth = display.widthPixels;
		screenHeight = display.heightPixels;

		Log.v("1111Screen_width", screenWidth+"" );
		Log.v("1111Screen_height", screenHeight+"" );

		Log.v("1111minus_height", (screenHeight-resultingImage.getHeight())+"");
		Log.v("1111minus_heightTop", (screenHeight - resultingImage.getHeight()) / 2 + "");


		Utils.write("crop leftmostPoint======="+leftmostPoint);
		Utils.write("crop topmostPoint======="+topmostPoint);
		Utils.write("crop rightmostPoint======="+rightmostPoint);
		Utils.write("crop bottommostPoint======="+bottommostPoint);
		Utils.write("crop croppedImageheight======="+croppedImageheight);
		Utils.write("crop croppedImageWidth======="+croppedImageWidth);



		int calTop=(topmostPoint-((screenHeight-resultingImage.getHeight())/2));
		int calHeight=(croppedImageheight+minusHeight);

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


		FrameLayout.LayoutParams params = (FrameLayout.LayoutParams) imgView.getLayoutParams();
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
