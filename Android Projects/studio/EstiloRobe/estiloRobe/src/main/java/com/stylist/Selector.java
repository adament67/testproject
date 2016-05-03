package com.stylist;

import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.constants.BaseActivity;
import com.constants.Utils;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Iterator;
import java.util.Locale;
import java.util.Set;

public class Selector extends BaseActivity {
 RelativeLayout uploadRelativeLayout;
 int fromEditor;
 public static Selector sSelector;
	private Uri fileUri;
	public static final int GALLERY_INTENT_CALLED = 054;
	public static final int GALLERY_KITKAT_INTENT_CALLED = 045;


	public static final int KEY_GALL = 5;
	public static final int KEY_CAM = 6;

	private final int CAMERA_RESULT = 1;
	private static int RESULT_LOAD_IMAGE = 2;
	public static final int MEDIA_TYPE_IMAGE = 1;
	private static final String IMAGE_DIRECTORY_NAME = "Detechip Imagebg";
	public static String filePath = "";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.selector);
		fromEditor=getIntent().getIntExtra("fromEditor",2);
		TextView wardrobe = (TextView) findViewById(R.id.wardrobe);
		TextView wishlist = (TextView) findViewById(R.id.wishlist);
		TextView back = (TextView) findViewById(R.id.back);
		uploadRelativeLayout=(RelativeLayout)findViewById(R.id.uploadRelativeLayout);
		wardrobe.setOnClickListener(this);
		wishlist.setOnClickListener(this);	
		back.setOnClickListener(this);	
		sSelector=this;
		System.out.println("fromEditor==========="+fromEditor);
		if(fromEditor==1)
		{
			wardrobe.setText("Browse My Wardrobe");
			wishlist.setText("Browse My Wishlist");

			
		}
		else
		{
			wardrobe.setText("Upload From Gallery");
			wishlist.setText("Take a Photo");
		}
		uploadRelativeLayout.setOnTouchListener(new View.OnTouchListener() {
			
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				// TODO Auto-generated method stub
				setResult(RESULT_CANCELED,
						new Intent().putExtra("intent_data", "0"));
				finish();
				overridePendingTransition(R.anim.slide_in_up, R.anim.slide_out_down);
				return false;
			}
		});
	}
	
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.wardrobe:

				try {
					Intent i = new Intent(Intent.ACTION_PICK,
							android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);

					startActivityForResult(i, RESULT_LOAD_IMAGE);
				} catch (Exception e) {
					e.printStackTrace();
					// TODO: handle exception
				} catch (Error e) {
					e.printStackTrace();
					// TODO: handle exception
				}
			break;

		case R.id.wishlist:
			try {

				Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
				fileUri = getOutputMediaFileUri(MEDIA_TYPE_IMAGE);
				intent.putExtra(MediaStore.EXTRA_OUTPUT, fileUri);
				intent.putExtra(MediaStore.EXTRA_VIDEO_QUALITY, 1);
				startActivityForResult(intent, CAMERA_RESULT);

			} catch (Exception e) {
				e.printStackTrace();
				// TODO: handle exception
			} catch (Error e) {
				e.printStackTrace();
				// TODO: handle exception
			}
			break;
		case R.id.back:
			/*setResult(RESULT_CANCELED,
					new Intent().putExtra("intent_data", "0"));*/
			finish();
			/*overridePendingTransition(R.anim.slide_in_up, R.anim.slide_out_down);*/
			break;
		default:
			break;
		}
	}
	
	
	@Override
		public void onBackPressed() {
			// TODO Auto-generated method stub
			super.onBackPressed();
			/*setResult(RESULT_CANCELED,
					new Intent().putExtra("intent_data", "0"));*/
			finish();
			overridePendingTransition(R.anim.slide_in_up, R.anim.slide_out_down);
		}
	public Uri getOutputMediaFileUri(int type) {
		return Uri.fromFile(getOutputMediaFile(type));
	}

	private static File getOutputMediaFile(int type) {

		// External sdcard location
		File mediaStorageDir = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES),
				IMAGE_DIRECTORY_NAME);

		// Create the storage directory if it does not exist
		if (!mediaStorageDir.exists()) {
			if (!mediaStorageDir.mkdirs()) {

				return null;
			}
		}

		// Create a media file name
		String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(new Date());
		File mediaFile;
		if (type == MEDIA_TYPE_IMAGE) {

			mediaFile = new File(mediaStorageDir.getPath() + File.separator + "IMG_" + timeStamp + ".jpg");

		} else {
			return null;
		}

		return mediaFile;
	}

	@Override
	protected void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
		// save file url in bundle as it will be null on scren orientation
		// changes

		outState.putParcelable("file_uri", fileUri);
	}

	@Override
	protected void onRestoreInstanceState(Bundle savedInstanceState) {
		super.onRestoreInstanceState(savedInstanceState);
		// get the file url

		fileUri = savedInstanceState.getParcelable("file_uri");
	}


	public static void dumpIntent(Intent i){

		Bundle bundle = i.getExtras();
		if (bundle != null) {
			Set<String> keys = bundle.keySet();
			Iterator<String> it = keys.iterator();
			Log.e("Dump_data","Dumping Intent start");
			while (it.hasNext()) {
				String key = it.next();
				Log.e("Dump_data","[" + key + "=" + bundle.get(key)+"]");
			}
			Log.e("Dump_data","Dumping Intent end");
		}
	}


//	@Override
//	protected void onActivityResult(int requestCode, int responseCode, Intent data) {
//		super.onActivityResult(requestCode, responseCode, data);
//		// Log.i(Tag, "Receive the camera result");
//		if (responseCode == RESULT_OK && requestCode == CAMERA_RESULT) {
//			try {
//
//
//
//
//				dumpIntent(data);
//
//
///*
//				filePath = compressImage(fileUri + "");
//				Utils.write("filePath===="+filePath);
//
//				Intent intent=new Intent();
//				intent.putExtra("FILE_PATH",filePath);
//				setResult(7,intent);*/
//				//finish();//finishing activity
//
//
//			} catch (Error e) {
//				e.printStackTrace();
//			} catch (Exception e) {
//				e.printStackTrace();
//			}
//		} else if (requestCode == RESULT_LOAD_IMAGE && responseCode == RESULT_OK ) {
//			try {
//
//
//				dumpIntent(data);
//
//				/*for (String key : data.getExtras().keySet()) {
//					Object value = data.getExtras().get(key);
//					Log.d("Log_Selector_Cam", String.format("%s %s (%s)", key,
//							value.toString(), value.getClass().getName()));
//				}*/
//
//
//
//				/*Uri selectedImage = data.getData();
//				String[] filePathColumn = { MediaStore.Images.Media.DATA };
//				Cursor cursor = getContentResolver().query(selectedImage, filePathColumn, null, null, null);
//				cursor.moveToFirst();
//				int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
//				filePath = cursor.getString(columnIndex);
//				cursor.close();
//
//				filePath = compressImage(filePath + "");
//
//
//				filePath = compressImage(fileUri + "");
//				Utils.write("filePath====" + filePath);
//
//				Intent intent=new Intent();
//				intent.putExtra("FILE_PATH",filePath);
//				setResult(8, intent);
//				//finish();//finishing activity
//
//
//				Utils.write("filePath===="+filePath);*/
//			} catch (Error e) {
//				e.printStackTrace();
//
//			} catch (Exception e) {
//				e.printStackTrace();
//
//			}
//		}
//	}




	@Override
	protected void onActivityResult(int requestCode, int responseCode, Intent data) {
		super.onActivityResult(requestCode, responseCode, data);
		// Log.i(Tag, "Receive the camera result");
		if (responseCode == RESULT_OK && requestCode == CAMERA_RESULT) {
			try {

				System.out.println("CCCCCCCCCCCCCCCCCCCCCCCCCCCCC "+compressImage(fileUri + ""));
				filePath = compressImage(fileUri + "");
				System.out.println("filePath======= "+    filePath);


				Intent intent=new Intent();
				intent.putExtra("IMAGE_PATH", filePath);
				setResult(9,intent);
				finish();//finishing activity


			} catch (Error e) {
				e.printStackTrace();
			} catch (Exception e) {
				e.printStackTrace();
			}
		} else if (requestCode == RESULT_LOAD_IMAGE && responseCode == RESULT_OK && null != data) {
			try {

				Uri selectedImage = data.getData();
				String[] filePathColumn = { MediaStore.Images.Media.DATA };
				Cursor cursor = getContentResolver().query(selectedImage, filePathColumn, null, null, null);
				cursor.moveToFirst();
				int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
				filePath = cursor.getString(columnIndex);
				cursor.close();

				filePath = compressImage(filePath + "");
				System.out.println("filePath======= "+    filePath);



				Intent intent=new Intent();
				intent.putExtra("IMAGE_PATH", filePath);
				setResult(9,intent);
				finish();//finishing activity


			} catch (Error e) {
				e.printStackTrace();

			} catch (Exception e) {
				e.printStackTrace();

			}
		}
	}

	// //////////////////////
	public String compressImage(String imageUri) {

		String filePath1 = getRealPathFromURI(imageUri);
		Bitmap scaledBitmap = null;

		BitmapFactory.Options options = new BitmapFactory.Options();

		// by setting this field as true, the actual bitmap pixels are not
		// loaded in the memory. Just the bounds are loaded. If
		// you try the use the bitmap here, you will get null.
		options.inJustDecodeBounds = true;
		Bitmap bmp = BitmapFactory.decodeFile(filePath1, options);

		int actualHeight = options.outHeight;
		int actualWidth = options.outWidth;

		// max Height and width values of the compressed image is taken as
		// 816x612

//		float maxHeight = 3264.0f;
//		float maxWidth = 1836.0f;
		float maxHeight = 816.0f;
		float maxWidth = 612.0f;
		float imgRatio = actualWidth / actualHeight;
		float maxRatio = maxWidth / maxHeight;

		// width and height values are set maintaining the aspect ratio of the
		// image

		System.out.println("...............1  " + "actualHeight:" + actualHeight + "actualWidth:" + actualWidth
				+ "maxHeight:" + maxHeight + "maxWidth:" + maxWidth + "imgRatio:" + imgRatio + "maxRatio:" + maxRatio);

		if (actualHeight > maxHeight || actualWidth > maxWidth) {
			if (imgRatio < maxRatio) {
				System.out.println("a.................");
				imgRatio = maxHeight / actualHeight;
				actualWidth = (int) (imgRatio * actualWidth);
				actualHeight = (int) maxHeight;
			} else if (imgRatio > maxRatio) {
				System.out.println("a.................");
				imgRatio = maxWidth / actualWidth;
				actualHeight = (int) (imgRatio * actualHeight);
				actualWidth = (int) maxWidth;
			} else {
				System.out.println("c.................");
				actualHeight = (int) maxHeight;
				actualWidth = (int) maxWidth;

			}
		}
		System.out.println("...............2  " + "actualHeight:" + actualHeight + "actualWidth:" + actualWidth
				+ "maxHeight:" + maxHeight + "maxWidth:" + maxWidth + "imgRatio:" + imgRatio + "maxRatio:" + maxRatio);
		// setting inSampleSize value allows to load a scaled down version of
		// the original image

		options.inSampleSize = calculateInSampleSize(options, actualWidth, actualHeight);

		// inJustDecodeBounds set to false to load the actual bitmap
		options.inJustDecodeBounds = false;

		// this options allow android to claim the bitmap memory if it runs low
		// on memory
		options.inPurgeable = true;
		options.inInputShareable = true;
		options.inTempStorage = new byte[16 * 1024];

		try {
			// load the bitmap from its path
			bmp = BitmapFactory.decodeFile(filePath1, options);
		} catch (OutOfMemoryError exception) {
			exception.printStackTrace();

		}
		try {
			scaledBitmap = Bitmap.createBitmap(actualWidth, actualHeight, Bitmap.Config.ARGB_8888);
		} catch (OutOfMemoryError exception) {
			exception.printStackTrace();
		}

		float ratioX = actualWidth / (float) options.outWidth;
		float ratioY = actualHeight / (float) options.outHeight;
		float middleX = actualWidth / 2.0f;
		float middleY = actualHeight / 2.0f;

		Matrix scaleMatrix = new Matrix();
		scaleMatrix.setScale(ratioX, ratioY, middleX, middleY);

		Canvas canvas = new Canvas(scaledBitmap);
		canvas.setMatrix(scaleMatrix);
		canvas.drawBitmap(bmp, middleX - bmp.getWidth() / 2, middleY - bmp.getHeight() / 2,
				new Paint(Paint.FILTER_BITMAP_FLAG));

		// check the rotation of the image and display it properly
		ExifInterface exif;
		try {
			exif = new ExifInterface(filePath1);

			int orientation = exif.getAttributeInt(ExifInterface.TAG_ORIENTATION, 0);
			Log.d("EXIF", "Exif: " + orientation);
			Matrix matrix = new Matrix();
			if (orientation == 6) {
				matrix.postRotate(90);
				Log.d("EXIF", "Exif: " + orientation);
			} else if (orientation == 3) {
				matrix.postRotate(180);
				Log.d("EXIF", "Exif: " + orientation);
			} else if (orientation == 8) {
				matrix.postRotate(270);
				Log.d("EXIF", "Exif: " + orientation);
			}
			scaledBitmap = Bitmap.createBitmap(scaledBitmap, 0, 0, scaledBitmap.getWidth(), scaledBitmap.getHeight(),
					matrix, true);
		} catch (IOException e) {
			e.printStackTrace();
		}

		FileOutputStream out = null;
		String filename = getFilename();
		try {
			out = new FileOutputStream(filename);

			// write the compressed bitmap at the destination specified by
			// filename.
			scaledBitmap.compress(Bitmap.CompressFormat.JPEG, 100, out);

		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		System.out.println("...............3  " + "actualHeight:" + actualHeight + "actualWidth:" + actualWidth
				+ "maxHeight:" + maxHeight + "maxWidth:" + maxWidth + "imgRatio:" + imgRatio + "maxRatio:" + maxRatio);
		return filename;

	}

	public String getFilename() {
		File file = new File(Environment.getExternalStorageDirectory().getPath(), "MyFolder/Images");
		if (!file.exists()) {
			file.mkdirs();
		}
		String uriSting = (file.getAbsolutePath() + "/" + System.currentTimeMillis() + ".jpg");
		System.out.println("uriSting-----------------" + uriSting);
		return uriSting;

	}

	private String getRealPathFromURI(String contentURI) {

		Uri contentUri = Uri.parse(contentURI);
		Cursor cursor = getContentResolver().query(contentUri, null, null, null, null);
		if (cursor == null) {
			return contentUri.getPath();
		} else {
			cursor.moveToFirst();
			int index = cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA);
			return cursor.getString(index);
		}
	}

	public int calculateInSampleSize(BitmapFactory.Options options, int reqWidth, int reqHeight) {
		final int height = options.outHeight;
		final int width = options.outWidth;
		int inSampleSize = 1;

		if (height > reqHeight || width > reqWidth) {
			final int heightRatio = Math.round((float) height / (float) reqHeight);
			final int widthRatio = Math.round((float) width / (float) reqWidth);
			inSampleSize = heightRatio < widthRatio ? heightRatio : widthRatio;
		}
		final float totalPixels = width * height;
		final float totalReqPixelsCap = reqWidth * reqHeight * 2;
		while (totalPixels / (inSampleSize * inSampleSize) > totalReqPixelsCap) {
			inSampleSize++;
		}

		return inSampleSize;
	}
}
