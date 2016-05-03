package com.stylist.wishlist;

import com.constants.BaseActivity;
import com.constants.Constants;
import com.constants.Utils;

import com.stylist.CroppingActivity;
import com.stylist.R;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.Activity;
import android.app.Dialog;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.media.ExifInterface;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class UploadOptionWishListActivity extends BaseActivity {
	public static UploadOptionWishListActivity act;

	TextView uploadCameraRoll, uploadcapture, headerName, backTextView;
	private static final int CAPTURE_IMG = 2;
	private static final int GALLERY_IMG = 1;
	public static final int GALLERY_INTENT_CALLED = 054;
	public static final int GALLERY_KITKAT_INTENT_CALLED = 045;
	int photoUrlPos = 0, rotateCount = 0, crop = 1;
	Dialog cropConfirm, zoom_crop_dialog, only_cropDialog;
	String showImagePath = null;
	Uri imageUri;
	Bitmap bitmap1;
	public static final int MEDIA_TYPE_IMAGE = 1;
	private final int CAMERA_RESULT = 1;
	private static int RESULT_LOAD_IMAGE = 2;

	private static final String IMAGE_DIRECTORY_NAME = "temp Imagebg";


	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

			setContentView(R.layout.activity_upload_option_wish_list);
		
		act = this;
		uploadCameraRoll = (TextView) findViewById(R.id.uploadCameraRoll);
		uploadcapture = (TextView) findViewById(R.id.uploadcapture);
		backTextView = (TextView) findViewById(R.id.back);
		
		LinearLayout	upperlinLayout = (LinearLayout) findViewById(R.id.upperlinLayout);

		upperlinLayout.setOnClickListener(this);
		backTextView.setOnClickListener(this);
		uploadcapture.setOnClickListener(this);
		uploadCameraRoll.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		super.onClick(v);
		switch (v.getId()) {
		case R.id.uploadCameraRoll:
//			if (Build.VERSION.SDK_INT < 19) {
//				Intent intent = new Intent();
//				intent.setType("image/jpeg");
//				intent.setAction(Intent.ACTION_GET_CONTENT);
//				startActivityForResult(
//						Intent.createChooser(intent, "Select Image"),
//						GALLERY_INTENT_CALLED);
//			} else {
//				Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
//				intent.addCategory(Intent.CATEGORY_OPENABLE);
//				intent.setType("image/jpeg");
//				startActivityForResult(intent, GALLERY_KITKAT_INTENT_CALLED);
//			}
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
		case R.id.uploadcapture:

			try {




				Intent intentPicture = new Intent(
						MediaStore.ACTION_IMAGE_CAPTURE);
				startActivityForResult(intentPicture,
						CAMERA_RESULT);










//				Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
//				imageUri = getOutputMediaFileUri(MEDIA_TYPE_IMAGE);
//				intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
//				intent.putExtra(MediaStore.EXTRA_VIDEO_QUALITY, 1);
//				startActivityForResult(intent, CAMERA_RESULT);

			} catch (Exception e) {
				e.printStackTrace();
				// TODO: handle exception
			} catch (Error e) {
				e.printStackTrace();
				// TODO: handle exception
			}
			break;
		case R.id.back:
			finish();
			overridePendingTransition(R.anim.slide_in_up, R.anim.slide_out_down);
			break;

		case R.id.upperlinLayout:
			finish();
			overridePendingTransition(R.anim.slide_in_up, R.anim.slide_out_down);
			break;
		default:
			break;
		}
	}


	public Uri getOutputMediaFileUri(int type) {
		return Uri.fromFile(getOutputMediaFile(type));
	}

	private static File getOutputMediaFile(int type) {

		// External sdcard location
		File mediaStorageDir = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES), Constants.IMAGE_DIRECTORY_NAME/*+"/"+Constants.IMAGE_TEMP_ORIGNAL*/);

		Log.v("12222222222222", mediaStorageDir.getPath());

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

			mediaFile = new File(mediaStorageDir.getPath() +"/"+ "system" + ".jpg");

		} else {
			return null;
		}

		return mediaFile;
	}


	@Override
	public void onBackPressed() {
		// TODO Auto-generated method stub
		super.onBackPressed();
//		File tempDir = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES), Constants.IMAGE_DIRECTORY_NAME);
//		Log.v("File_path", tempDir.getPath());
//		DeleteRecursive(tempDir);
		finish();
		overridePendingTransition(R.anim.slide_in_up, R.anim.slide_out_down);
	}

	public void DeleteRecursive(File fileOrDirectory) {

		if (fileOrDirectory.isDirectory())
			for (File child : fileOrDirectory.listFiles())
				DeleteRecursive(child);

		fileOrDirectory.delete();
		MediaScannerConnection.scanFile(context,
				new String[]{fileOrDirectory.getAbsolutePath()}, null, null);

	}

	@SuppressLint("NewApi")
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		try {
//
			if (resultCode == RESULT_OK && requestCode == CAMERA_RESULT) {
				try {
					rotateCount = 0;






					if (data.getData() != null) {
						showImagePath = getPath(this, data.getData());

						Utils.write("showImagePath== getPath========"+showImagePath);

						showImagePath = compressImage(showImagePath);

						Utils.write("showImagePath== compressImage========"+showImagePath);


						cropDialog();



//						File imgFile = new File(showImagePath);
//						if (imgFile.exists()) {
//							Bitmap myBitmap = BitmapFactory.decodeFile(imgFile
//									.getAbsolutePath());
//							userimage.setImageBitmap(myBitmap);
//						}

					} else {
						Bitmap bitmap = data.getExtras().getParcelable("data");
						FileOutputStream out = null;
						try {

							String imagename = "img" + requestCode + ".jpg";
						out = new FileOutputStream(new File(
									Environment.getExternalStorageDirectory() + "/"
										+ imagename));








							bitmap.compress(Bitmap.CompressFormat.PNG, 100, out);

							showImagePath = Environment.getExternalStorageDirectory() + "/"
									+ imagename;
							showImagePath = compressImage(showImagePath);


							Utils.write("showImagePath== custom cap========"+showImagePath);





							cropDialog();
//							File imgFile = new File(showImagePath);
//
//							if (imgFile.exists()) {
//								Bitmap myBitmap = BitmapFactory.decodeFile(imgFile
//										.getAbsolutePath());
//								userimage.setImageBitmap(myBitmap);
//							}

						} catch (Exception e) {
							e.printStackTrace();
						} finally {
							try {
								if (out != null) {
									out.close();
								}
							} catch (IOException e) {
								e.printStackTrace();
							}
						}

					}











//					if(imageUri != null){
//						System.out.println("CCCCCCCCCCCCCCCCCCCCCCCCCCCCC " + imageUri + "")
//
//						//showImagePath = compressImage(imageUri + "");
//						cropDialog();
//
//					}else{
//
//						Toast.makeText(UploadOptionWishListActivity.this, "Unable to capture image, Please try again!", Toast.LENGTH_SHORT).show();
//						Constants.IS_CAPTURED_PROBLEM_OCCURED=true;
//					}

				} catch (Error e) {
					e.printStackTrace();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
//

			else if (requestCode == RESULT_LOAD_IMAGE && resultCode == RESULT_OK && null != data) {
				try {
					rotateCount = 0;
					Uri selectedImage = data.getData();
					String[] filePathColumn = {MediaStore.Images.Media.DATA};
					Cursor cursor = getContentResolver().query(selectedImage, filePathColumn, null, null, null);
					cursor.moveToFirst();
					int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
					showImagePath = cursor.getString(columnIndex);
					cursor.close();


					showImagePath = compressImage(showImagePath + "");
					System.out.println("CCCCCCCCCCCCCCCCCCCCCCCCCCCCC " + showImagePath);
					cropDialog();

				} catch (Error e) {
					e.printStackTrace();

				} catch (Exception e) {
					e.printStackTrace();

				}
			}

		}
	catch (Exception e)
	{
		e.printStackTrace();
	}}



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

		float maxHeight = 3264.0f;
		float maxWidth = 1836.0f;

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
		File file = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES), Constants.IMAGE_DIRECTORY_NAME/*+"/"+Constants.IMAGE_TEMP_ORIGNAL*/);

		Log.v("12222222222", file.getPath());

		if (!file.exists()) {
			file.mkdirs();
		}
		String uriSting = (file.getAbsolutePath() +"/" +"system" + ".jpg");
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
	@SuppressWarnings("deprecation")
	public String getRealPathFromURI(Uri contentUri) {
		String[] proj = { MediaStore.Images.Media.DATA };
		System.out.println("contentUri=====" + contentUri);

		Cursor cursor = managedQuery(contentUri, proj, null, null, null);
		int column_index = cursor
				.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
		cursor.moveToFirst();
		return cursor.getString(column_index);
	}

	private void cropDialog() {
		try {

			System.out.println("FROM_GALLERY= in cropDialog====" + showImagePath);
			final String showImgPathNew = showImagePath;
			cropConfirm = addDialog(R.layout.crop_confirm);
			final ImageView imageView = (ImageView) cropConfirm.findViewById(R.id.imageView);
			try {
				bitmap1 = Utils.orientedBitmap(showImgPathNew);
				System.out.println("bitmap1========="+bitmap1);
				imageView.setImageBitmap(bitmap1);
			} catch (Exception e) {
				// TODO: handle exception
			}
			Button cropBut = (Button) cropConfirm.findViewById(R.id.cropButn);
			Button cancelBut = (Button) cropConfirm
					.findViewById(R.id.cancelButnc);
			Button rotateBut = (Button) cropConfirm
					.findViewById(R.id.rotateButnc);

			rotateBut.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View arg0) {
					bitmap1 = Utils.rotateImage(bitmap1, 90);
					imageView.setImageBitmap(bitmap1);
					rotateCount++;
				}
			});

			cropBut.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View arg0) {
					try {

						Intent cropIntent = new Intent(context,
								CroppingActivity.class);
						cropIntent.putExtra("fromWish", 1);
						cropIntent.putExtra("myPath", showImgPathNew);
						cropIntent.putExtra("rotateCount", rotateCount);
						startActivity(cropIntent);
						showToastS("crop with your finger");
					//	finish();

					} catch (Exception e) {
						// TODO: handle exception
					}
				}
			});
			cancelBut.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View arg0) {
					//cropConfirm.dismiss();
					Intent intent = new Intent(context,
							UploadWishListActivity.class);
					intent.putExtra("crop", false);
					intent.putExtra("myPath", showImgPathNew);
					intent.putExtra("rotateCount", rotateCount);
					startActivity(intent);
					//finish();
				}
			});
			cropConfirm.show();

		} catch (Exception e) {
			// TODO: handle exception
		}
	}



	@TargetApi(Build.VERSION_CODES.KITKAT)
	@SuppressLint("NewApi")
	public String getPath(final Context context, final Uri uri) {

		final boolean isKitKat = Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT;

		if (isKitKat && DocumentsContract.isDocumentUri(context, uri)) {

			if (isExternalStorageDocument(uri)) {
				final String docId = DocumentsContract.getDocumentId(uri);
				final String[] split = docId.split(":");
				final String type = split[0];

				if ("primary".equalsIgnoreCase(type)) {
					return Environment.getExternalStorageDirectory() + "/"
							+ split[1];
				}

			}
			// DownloadsProvider
			else if (isDownloadsDocument(uri)) {

				final String id = DocumentsContract.getDocumentId(uri);
				final Uri contentUri = ContentUris.withAppendedId(
						Uri.parse("content://downloads/public_downloads"),
						Long.valueOf(id));

				return getDataColumn(context, contentUri, null, null);
			}
			// MediaProvider
			else if (isMediaDocument(uri)) {
				final String docId = DocumentsContract.getDocumentId(uri);
				final String[] split = docId.split(":");
				final String type = split[0];

				Uri contentUri = null;
				if ("image".equals(type)) {
					contentUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
				} else if ("video".equals(type)) {
					contentUri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI;
				} else if ("audio".equals(type)) {
					contentUri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
				}

				final String selection = "_id=?";
				final String[] selectionArgs = new String[] { split[1] };

				return getDataColumn(context, contentUri, selection,
						selectionArgs);
			}
		}
		// MediaStore (and general)
		else if ("content".equalsIgnoreCase(uri.getScheme())) {
			return getDataColumn(context, uri, null, null);
		}
		// File
		else if ("file".equalsIgnoreCase(uri.getScheme())) {
			return uri.getPath();
		}

		return null;
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
	public String getDataColumn(Context context, Uri uri, String selection,
			String[] selectionArgs) {

		Cursor cursor = null;
		final String column = "_data";
		final String[] projection = { column };

		try {
			cursor = context.getContentResolver().query(uri, projection,
					selection, selectionArgs, null);
			if (cursor != null && cursor.moveToFirst()) {
				final int column_index = cursor.getColumnIndexOrThrow(column);
				return cursor.getString(column_index);
			}
		} finally {
			if (cursor != null)
				cursor.close();
		}
		return null;
	}

	public boolean isExternalStorageDocument(Uri uri) {
		return "com.android.externalstorage.documents".equals(uri
				.getAuthority());
	}

	public boolean isDownloadsDocument(Uri uri) {
		return "com.android.providers.downloads.documents".equals(uri
				.getAuthority());
	}

	public boolean isMediaDocument(Uri uri) {
		return "com.android.providers.media.documents".equals(uri
				.getAuthority());
	}


	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		Log.v("232323", "Resume upload option");
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();

		Log.v("232323", "Destory upload option");

	}

}
