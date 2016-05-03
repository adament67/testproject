package com.constants;


import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.media.ExifInterface;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Environment;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class Utils {
	static String green = "#ed394c";

	public static String setLeadingZero(int no) {
		String str = "" + no;
		if (no < 10)
			str = "0" + no;
		return str.trim();
	}

	private static int exifToDegrees(int exifOrientation) {
		if (exifOrientation == ExifInterface.ORIENTATION_ROTATE_90) {
			return 90;
		} else if (exifOrientation == ExifInterface.ORIENTATION_ROTATE_180) {
			return 180;
		} else if (exifOrientation == ExifInterface.ORIENTATION_ROTATE_270) {
			return 270;
		}
		return 0;
	}

	

	public static void setTitleTypeFace(Dialog d, Typeface face) {
		// TODO Auto-generated method stub

		try {
			Field mAlert = AlertDialog.class.getDeclaredField("mAlert");
			mAlert.setAccessible(true);
			Object alertController = mAlert.get(d);

			Field mTitleView = alertController.getClass().getDeclaredField(
					"mTitleView");
			mTitleView.setAccessible(true);

			TextView title1 = (TextView) mTitleView.get(alertController);
			title1.setTypeface(face);

		} catch (Error error)
		{
			error.printStackTrace();
		}catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static Dialog createSimpleDialog1(Context context, String title,
			String msg, String btnLabel1, final Method method1) {
		String chars = title;
		SpannableString str = new SpannableString(chars);
		str.setSpan(new ForegroundColorSpan(Color.parseColor(green)), 0,
				chars.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);

		AlertDialog.Builder builder = new AlertDialog.Builder(context);
		builder.setTitle(str);
		builder.setMessage(msg);

		builder.setPositiveButton(btnLabel1,
				new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int which) {

						if (method1 != null)
							method1.execute();
					}
				});

		Dialog d = builder.show();
		int dividerId = d.getContext().getResources()
				.getIdentifier("android:id/titleDivider", null, null);
		View divider = d.findViewById(dividerId);
		divider.setBackgroundColor(Color.parseColor(green));

		return d;

	}

	public static Dialog createSimpleDialog(Context context, String title,
			String msg, String btnLabel1, String btnLabel2, final Method method1) {
		String chars = title;
		SpannableString str = new SpannableString(chars);
		str.setSpan(new ForegroundColorSpan(Color.parseColor(green)), 0,
				chars.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);

		AlertDialog.Builder builder = new AlertDialog.Builder(context);
		builder.setTitle(str);
		builder.setMessage(msg);

		builder.setPositiveButton(btnLabel1,
				new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int which) {

						if (method1 != null)
							method1.execute();
					}
				});

		builder.setNegativeButton(btnLabel2,
				new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int which) {

						// if (method2 != null)
						// method2.execute();
					}
				});
		// Typeface
		// face=Typeface.createFromAsset(context.getAssets(),"fonts/RobotoSlab-Regular.ttf");
		Dialog d = builder.show();
		// TextView textView = (TextView) d.findViewById(android.R.id.message);
		// textView.setTypeface(face);

		/*
		 * TextView titletv = (TextView) d.findViewById(android.R.id.title);
		 * System.out.println("titletv-->"+titletv); titletv.setTypeface(face);
		 */
		// setTitleTypeFace(d,face);

		// TextView btnLabel1tv = (TextView)
		// d.findViewById(android.R.id.button1);
		// btnLabel1tv.setTypeface(face);
		//
		// TextView btnLabel2tv = (TextView)
		// d.findViewById(android.R.id.button2);
		// btnLabel2tv.setTypeface(face);

		int dividerId = d.getContext().getResources()
				.getIdentifier("android:id/titleDivider", null, null);
		View divider = d.findViewById(dividerId);
		divider.setBackgroundColor(Color.parseColor(green));

		return d;

	}

	public static Dialog createSimpleDialog(Context context, String title,
			String msg, String btnLabel1) {
		String chars = title;
		SpannableString str = new SpannableString(chars);
		str.setSpan(new ForegroundColorSpan(Color.parseColor(green)), 0,
				chars.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);

		AlertDialog.Builder builder = new AlertDialog.Builder(context);
		builder.setTitle(str);
		builder.setMessage(msg);

		builder.setPositiveButton(btnLabel1,
				new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int which) {

						dialog.dismiss();
					}
				});

		Dialog d = builder.show();
		Typeface face = Typeface.createFromAsset(context.getAssets(),
				"fonts/lvnm.ttf");

		TextView textView = (TextView) d.findViewById(android.R.id.message);
		textView.setTypeface(face);

		/*
		 * TextView titletv = (TextView) d.findViewById(android.R.id.title);
		 * titletv.setTypeface(face);
		 */
		setTitleTypeFace(d, face);

		TextView btnLabel1tv = (TextView) d.findViewById(android.R.id.button1);
		btnLabel1tv.setTypeface(face);

		int dividerId = d.getContext().getResources()
				.getIdentifier("android:id/titleDivider", null, null);
		View divider = d.findViewById(dividerId);
		divider.setBackgroundColor(Color.parseColor(green));

		return d;

	}

	public static Dialog createSimpleDialog(Context context, String title,
			String msg, String btnLabel1, String btnLabel2,
			final Method method1, final Method method2) {
		String chars = title;
		SpannableString str = new SpannableString(chars);
		str.setSpan(new ForegroundColorSpan(Color.parseColor(green)), 0,
				chars.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
		AlertDialog.Builder builder = new AlertDialog.Builder(context);
		builder.setTitle(str);
		builder.setMessage(msg);
		builder.setPositiveButton(btnLabel1,
				new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int which) {

						if (method1 != null)
							method1.execute();
					}
				});

		builder.setNegativeButton(btnLabel2,
				new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int which) {

						if (method2 != null)
							method2.execute();
					}
				});

		Dialog d = builder.show();

		TextView textView = (TextView) d.findViewById(android.R.id.message);

		// Typeface
		// face=Typeface.createFromAsset(context.getAssets(),"fonts/lvnm.ttf");
		// textView.setTypeface(face);
		//
		// TextView titletv = (TextView) d.findViewById(android.R.id.title);
		// titletv.setTypeface(face);
		//
		//
		//
		// setTitleTypeFace(d,face);
		//
		//
		//
		// TextView btnLabel1tv = (TextView)
		// d.findViewById(android.R.id.button1);
		// btnLabel1tv.setTypeface(face);
		//
		// TextView btnLabel2tv = (TextView)
		// d.findViewById(android.R.id.button2);
		// btnLabel2tv.setTypeface(face);
		int dividerId = d.getContext().getResources()
				.getIdentifier("android:id/titleDivider", null, null);
		View divider = d.findViewById(dividerId);
		divider.setBackgroundColor(Color.parseColor(green));

		return d;

	}

	public static void showToastShort(String text, Context context) {
		Toast.makeText(context, text, Toast.LENGTH_SHORT).show();
	}

	public static boolean isEmailValid(String email) {
		boolean isValid = false;
		String expression = "^[a-z0-9!#$%&'*+/=?^_`{|}~-]+(?:\\.[a-z0-9!#$%&'*+/=?^_`{|}~-]+)*@(?:[a-z0-9](?:[a-z0-9-]*[a-z0-9])?\\.)+[a-z0-9](?:[a-z0-9-]*[a-z0-9])?$";
		CharSequence inputStr = email;

		Pattern pattern = Pattern.compile(expression, Pattern.CASE_INSENSITIVE);
		Matcher matcher = pattern.matcher(inputStr);
		if (matcher.matches()) {
			isValid = true;
		}
		return isValid;
	}

	public static boolean checkNetworkConnection(Context c) {
		ConnectivityManager connectivityManager = (ConnectivityManager) c
				.getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo activeNetworkInfo = connectivityManager
				.getActiveNetworkInfo();
		return activeNetworkInfo != null && activeNetworkInfo.isConnected();
	}

	public static Dialog exitDialog(Context context, String msg,
			final Method method) {
		String chars = "Stylist";
		SpannableString str = new SpannableString(chars);
		str.setSpan(new ForegroundColorSpan(Color.parseColor(green)), 0,
				chars.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
		AlertDialog.Builder builder = new AlertDialog.Builder(context);
		builder.setTitle(str);
		builder.setMessage(msg);
		builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int which) {
				if (method != null)
					method.execute();
			}
		});

		builder.setNegativeButton("Cancel",
				new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int which) {
						dialog.dismiss();
					}
				});
		Dialog d = builder.show();
		int dividerId = d.getContext().getResources()
				.getIdentifier("android:id/titleDivider", null, null);
		View divider = d.findViewById(dividerId);
		divider.setBackgroundColor(Color.parseColor(green));

		return d;
		// return builder.create();
	}

	public static interface Method {
		void execute();
	}

	public static void write(String message) {
		System.out.println(message);
	}

	/*
	 * public static void hideSoftKeyboard(Activity activity) { try { if
	 * (activity != null) { InputMethodManager inputMethodManager =
	 * (InputMethodManager) activity
	 * .getSystemService(Activity.INPUT_METHOD_SERVICE); View v =
	 * activity.getCurrentFocus(); if (v != null) { IBinder binder =
	 * activity.getCurrentFocus() .getWindowToken(); if (binder != null) {
	 * inputMethodManager.hideSoftInputFromWindow(binder, 0); } } } } catch
	 * (Exception e) { e.printStackTrace(); } }
	 */

	public static void showSoftKeyboard(Activity activity, EditText eText) {
		try {
			if (activity != null) {
				InputMethodManager imm = (InputMethodManager) activity
						.getSystemService(activity.INPUT_METHOD_SERVICE);
				imm.showSoftInput(eText, InputMethodManager.SHOW_IMPLICIT);
			}
		} catch (Error error)
		{
			error.printStackTrace();
		}catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static void hideSoftKeyboard(Activity activity) {
		try {
			InputMethodManager inputManager = (InputMethodManager) activity
					.getSystemService(Context.INPUT_METHOD_SERVICE);
			if (activity.getCurrentFocus().getWindowToken() != null) {
				inputManager.hideSoftInputFromWindow(activity.getCurrentFocus()
						.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
			}

		}catch (Error error)
		{
			error.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	public static void setupUI(View view, final Activity activity) {

		// Set up touch listener for non-text box views to hide keyboard.
		System.out.println("touch");
		if (!(view instanceof EditText)) {

			view.setOnTouchListener(new OnTouchListener() {
				@Override
				public boolean onTouch(View v, MotionEvent event) {
					// TODO Auto-generated method stub
					hideSoftKeyboard(activity);
					return false;
				}

			});
		}

	}

	
	
	
	
	public static Bitmap orientedBitmap(String imgpath) {
			//Bitmap bmp = new_decode(new File(imgpath) );

		Bitmap bmp = BitmapFactory.decodeFile(imgpath);

		try {
			ExifInterface exif = new ExifInterface(imgpath);
			int rotation = exif.getAttributeInt(ExifInterface.TAG_ORIENTATION,
					ExifInterface.ORIENTATION_NORMAL);
			int rotationInDegrees = exifToDegrees(rotation);
			Matrix matrix = new Matrix();
			if (rotation != 0f) {
				matrix.preRotate(rotationInDegrees);
				bmp = Bitmap.createBitmap(bmp, 0, 0, bmp.getWidth(),
						bmp.getHeight(), matrix, true);
			}
		} catch (Error error)
		{
			error.printStackTrace();
		}catch (Exception e) {
			e.printStackTrace();
		}
		return bmp;
	}

	
	
	  public static Bitmap new_decode(File f) {

	        // decode image size

	        BitmapFactory.Options o = new BitmapFactory.Options();
	        o.inJustDecodeBounds = true;
	        o.inDither = false; // Disable Dithering mode

	        o.inPurgeable = true; // Tell to gc that whether it needs free memory,
	                                // the Bitmap can be cleared

	        o.inInputShareable = true; // Which kind of reference will be used to
	                                    // recover the Bitmap data after being
	                                    // clear, when it will be used in the future
	        try {
	            BitmapFactory.decodeStream(new FileInputStream(f), null, o);
	        } catch (Error error)
			{
				error.printStackTrace();
			}catch (FileNotFoundException e1) {
	            // TODO Auto-generated catch block
	            e1.printStackTrace();
	        }
			catch (Exception error)
			{
				error.printStackTrace();
			}
	        // Find the correct scale value. It should be the power of 2.
	        final int REQUIRED_SIZE = 300;
	        int width_tmp = o.outWidth, height_tmp = o.outHeight;
	        int scale = 1;
	        while (true) {
	            if (width_tmp / 1.5 < REQUIRED_SIZE && height_tmp / 1.5 < REQUIRED_SIZE)
	                break;
	            width_tmp /= 1.5;
	            height_tmp /= 1.5;
	            scale *= 1.5;
	        }

	        // decode with inSampleSize
	        BitmapFactory.Options o2 = new BitmapFactory.Options();
	        // o2.inSampleSize=scale;
	        o.inDither = false; // Disable Dithering mode

	        o.inPurgeable = true; // Tell to gc that whether it needs free memory,
	                                // the Bitmap can be cleared

	        o.inInputShareable = true; // Which kind of reference will be used to
	                                    // recover the Bitmap data after being
	                                    // clear, when it will be used in the future
	        // return BitmapFactory.decodeStream(new FileInputStream(f), null, o2);
	        try {

//	          return BitmapFactory.decodeStream(new FileInputStream(f), null,
//	                  null);
	            Bitmap bitmap= BitmapFactory.decodeStream(new FileInputStream(f), null, null);
	            System.out.println(" IW " + width_tmp);
	            System.out.println("IHH " + height_tmp);           
	               int iW = width_tmp;
	                int iH = height_tmp;

	               return Bitmap.createScaledBitmap(bitmap, iW, iH, true);

	        } catch (OutOfMemoryError e) {
	            // TODO: handle exception
	            e.printStackTrace();
	            // clearCache();

	            // System.out.println("bitmap creating success");
	            System.gc();
	            return null;
	            // System.runFinalization();
	            // Runtime.getRuntime().gc();
	            // System.gc();
	            // decodeFile(f);
	        }catch (Error error)
			{
				error.printStackTrace();
				return  null;
			} catch (FileNotFoundException e) {
	            // TODO Auto-generated catch block
	            e.printStackTrace();
	            return null;
	        }
			catch (Exception error)
			{
				error.printStackTrace();
				return  null;
			}
	    }
	
	
	
	
	public static Bitmap rotateImage(Bitmap src, float degree) {
		Matrix matrix = new Matrix();
		// create new matrix object
		// setup rotation degree
		matrix.postRotate(degree);
		// return new bitmap rotated using matrix
		return Bitmap.createBitmap(src, 0, 0, src.getWidth(), src.getHeight(),
				matrix, true);
	}
	
	
	
	
	
	
	
	public static Bitmap getResizedBitmap(Bitmap bm, int newWidth, int newHeight) {
		int width = bm.getWidth();
		int height = bm.getHeight();
		float scaleWidth = ((float) newWidth) / width;
		float scaleHeight = ((float) newHeight) / height;
		// CREATE A MATRIX FOR THE MANIPULATION
		Matrix matrix = new Matrix();
		// RESIZE THE BIT MAP
		matrix.postScale(scaleWidth, scaleHeight);

		// "RECREATE" THE NEW BITMAP
		Bitmap resizedBitmap = Bitmap.createBitmap(bm, 0, 0, width, height,
				matrix, false);
		//bm.recycle();
		return resizedBitmap;
	}
	
	public static Bitmap getResizedBitmapWithoutRecycle(Bitmap bm, int newWidth, int newHeight) {
		int width = bm.getWidth();
		int height = bm.getHeight();
		float scaleWidth = ((float) newWidth) / width;
		float scaleHeight = ((float) newHeight) / height;
		// CREATE A MATRIX FOR THE MANIPULATION
		Matrix matrix = new Matrix();
		// RESIZE THE BIT MAP
		matrix.postScale(scaleWidth, scaleHeight);

		// "RECREATE" THE NEW BITMAP
		Bitmap resizedBitmap = Bitmap.createBitmap(bm, 0, 0, width, height,
				matrix, false);
		
		return resizedBitmap;
	}
	
	
	public static Bitmap BITMAP_RESIZER(Bitmap bitmap,int newWidth,int newHeight) {

	    Bitmap scaledBitmap = Bitmap.createBitmap(newWidth, newHeight, Config.ARGB_8888);

	    float ratioX = newWidth / (float) bitmap.getWidth();
	    float ratioY = newHeight / (float) bitmap.getHeight();
	    float middleX = newWidth / 2.0f;
	    float middleY = newHeight / 2.0f;

	    Matrix scaleMatrix = new Matrix();
	    scaleMatrix.setScale(ratioX, ratioY, middleX, middleY);

	    Canvas canvas = new Canvas(scaledBitmap);
	    canvas.setMatrix(scaleMatrix);
	    canvas.drawBitmap(bitmap, middleX - bitmap.getWidth() / 2, middleY - bitmap.getHeight() / 2, new Paint(Paint.FILTER_BITMAP_FLAG));

	    return scaledBitmap;

	    }
	
	
	
	
	
	
	
	
	
	public static File getOutputMediaFile() {
		long time = System.currentTimeMillis();
		String localPathDirectory = Environment.getExternalStorageDirectory()
				+ "/" +
				"/EstiloRobe";
		File mediaStorageDir = new File(localPathDirectory);
		if (!mediaStorageDir.exists()) {
			// if you cannot make this folder return
			if (!mediaStorageDir.mkdirs()) {
				return null;
			}
		}

		File mediaFile;
		// and make a media file:
		mediaFile = new File(mediaStorageDir.getPath() + File.separator
				+ "IMG_" + time + ".png");

		return mediaFile;
	}

	public static File getOutputMediaFileWard(Context context) {


		long time = System.currentTimeMillis();
		String localPathDirectory = Environment.getExternalStorageDirectory().getAbsolutePath()
				+ "/EstiloRobe";
		/*String localPathDirectory = context.getExternalFilesDir(null)
				+ "/stylist/wardrobe";*/
		File mediaStorageDir = new File(localPathDirectory);
		if (!mediaStorageDir.exists()) {
			// if you cannot make this folder return
			if (!mediaStorageDir.mkdirs()) {
				return null;
			}
		}

		File mediaFile;
		// and make a media file:
		mediaFile = new File(mediaStorageDir.getPath() + File.separator
				+ "IMG_" + time + ".png");

		return mediaFile;
	}
	
	public static File getOutputMediaFileWish() {
		long time = System.currentTimeMillis();
		String localPathDirectory = Environment.getExternalStorageDirectory()
				+ "/EstiloRobe";
		File mediaStorageDir = new File(localPathDirectory);
		if (!mediaStorageDir.exists()) {
			// if you cannot make this folder return
			if (!mediaStorageDir.mkdirs()) {
				return null;
			}
		}

		File mediaFile;
		// and make a media file:
		mediaFile = new File(mediaStorageDir.getPath() + File.separator
				+ "IMG_" + time + ".png");

		return mediaFile;
	}
	public static File getOutputMediaFileLook() {
		long time = System.currentTimeMillis();
		String localPathDirectory = Environment.getExternalStorageDirectory()
				+ "/EstiloRobe";
		File mediaStorageDir = new File(localPathDirectory);
		if (!mediaStorageDir.exists()) {
			// if you cannot make this folder return
			if (!mediaStorageDir.mkdirs()) {
				return null;
			}
		}

		File mediaFile;
		// and make a media file:
		mediaFile = new File(mediaStorageDir.getPath() + File.separator
				+ "IMG_" + time + ".png");

		return mediaFile;
	}
	public static File getOutputMediaFileLooksByStylist() {
		long time = System.currentTimeMillis();
		String localPathDirectory = Environment.getExternalStorageDirectory()
				+ "/EstiloRobe";
		File mediaStorageDir = new File(localPathDirectory);
		if (!mediaStorageDir.exists()) {
			// if you cannot make this folder return
			if (!mediaStorageDir.mkdirs()) {
				return null;
			}
		}

		File mediaFile;
		// and make a media file:
		mediaFile = new File(mediaStorageDir.getPath() + File.separator
				+ "IMG_" + time + ".png");

		return mediaFile;
	}
	public static File getOutputMediaFileProfile() {
		long time = System.currentTimeMillis();
		String localPathDirectory = Environment.getExternalStorageDirectory()
				+ "/EstiloRobe";
		File mediaStorageDir = new File(localPathDirectory);
		
		if (!mediaStorageDir.exists()) {
			// if you cannot make this folder return
			if (!mediaStorageDir.mkdirs()) {
				return null;
			}
		}

		File mediaFile;
		// and make a media file:
		mediaFile = new File(mediaStorageDir.getPath() + File.separator
				+ "IMG_" + time + ".png");

		return mediaFile;
	}

	public static File getOutputMediaFileTemp(Context context) {
		long time = System.currentTimeMillis();
		String localPathDirectory = Environment.getExternalStorageDirectory().getAbsolutePath()
				+ "/.temp";
		/*String localPathDirectory = context.getExternalFilesDir(null)
				+ "/stylist/wardrobe";*/
		File mediaStorageDir = new File(localPathDirectory);
		if (!mediaStorageDir.exists()) {
			// if you cannot make this folder return
			if (!mediaStorageDir.mkdirs()) {
				return null;
			}
		}

		File mediaFile;
		// and make a media file:
		mediaFile = new File(mediaStorageDir.getPath() + File.separator
				+ "IMG_1.png");

		return mediaFile;
	}




	public boolean appInstalledOrNot(Context con,String uri) {
		PackageManager pm = con.getPackageManager();
		boolean app_installed = false;
		try {
			pm.getPackageInfo(uri, PackageManager.GET_ACTIVITIES);
			app_installed = true;
		}catch (Error error)
		{
			error.printStackTrace();
		} catch (PackageManager.NameNotFoundException e) {
			app_installed = false;
		}catch (Exception error)
		{
			error.printStackTrace();
		}
		return app_installed;
	}
	


	public static HashMap<String, String> getAvgRGB(Bitmap source) {

		Bitmap bitmap = source; //assign your bitmap here
		int redColors = 0;
		int greenColors = 0;
		int blueColors = 0;
		int pixelCount = 0;

		for (int y = 0; y < bitmap.getHeight(); y++) {
			for (int x = 0; x < bitmap.getWidth(); x++) {
				int c = bitmap.getPixel(x, y);
				pixelCount++;
				redColors += Color.red(c);
				greenColors += Color.green(c);
				blueColors += Color.blue(c);
			}
		}
		// calculate average of bitmap r,g,b values
		int red = (redColors / pixelCount);
		int green = (greenColors / pixelCount);
		int blue = (blueColors / pixelCount);


		HashMap<String, String> rgbCount = new HashMap<>();

		rgbCount.put("r", red + "");
		rgbCount.put("g", green + "");
		rgbCount.put("b", blue + "");

	return  rgbCount;
	}
	
}