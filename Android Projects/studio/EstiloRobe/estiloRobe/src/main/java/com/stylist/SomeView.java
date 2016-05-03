package com.stylist;

import java.util.ArrayList;
import java.util.List;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.DashPathEffect;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Point;
import android.os.AsyncTask;
import android.os.Build;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;

import com.constants.Utils;
import com.stylist.wardrobe.UploadOptionWardrobeActivity;
import com.stylist.wardrobe.UploadWardribeActivity;
import com.stylist.wishlist.UploadOptionWishListActivity;
import com.stylist.wishlist.UploadWishListActivity;

public class SomeView extends ImageView implements View.OnTouchListener {

	public Paint paint, fPaint;
	public static List<Point> points;
	public static List<Point> finalPoints;
	int DIST = 2;
	boolean flgPathDraw = true;
	Point mfirstpoint = new Point(0,0);
	boolean bfirstpoint = false;
	Point mlastpoint = new Point(0,0);
	Bitmap bitmap;
	Context mContext;
	int screenWidth, screenHeight;
	int rotateCount, fromWish;
	String imgPath, categoryName,categoryId;
	int pointx = 0, pointy = 0;
	int height, width;
	float ratio;

	private void setFinalPaint() {
		// TODO Auto-generated method stub
		fPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
		fPaint.setStyle(Paint.Style.STROKE);
		fPaint.setPathEffect(new DashPathEffect(new float[] { 7, 14 }, 0));
		fPaint.setStrokeWidth(5);
		fPaint.setColor(lighter(Color.WHITE, 0.2f));
	}

	public static Bitmap RotateBitmap(Bitmap source, float angle)
	{
		Matrix matrix = new Matrix();
		matrix.postRotate(angle);
		return Bitmap.createBitmap(source, 0, 0, source.getWidth(), source.getHeight(), matrix, true);
	}


	public SomeView(Context c, String imgPath, int fromWish,
			String categoryName, int rotateCount,String categoryId) {
		super(c);

		//getScreenDimention(c);
		DisplayMetrics display = new DisplayMetrics();
		((CroppingActivity)c).getWindowManager().getDefaultDisplay().getMetrics(display);

		screenWidth = display.widthPixels;
		screenHeight = display.heightPixels;




		this.imgPath = imgPath;
		this.fromWish = fromWish;
		this.categoryName = categoryName;
		this.rotateCount = rotateCount;
        this.categoryId=categoryId;
		Bitmap bbb = BitmapFactory.decodeFile(imgPath);

		bbb = RotateBitmap(bbb, 90*rotateCount);

		bitmap = bbb;


		Log.v("screenWidth  ", + screenWidth+"");
		Log.v("screenHeight  ",  screenHeight+"");

		Log.v("Actual_width", bbb.getWidth() + "");
		Log.v("Actual_height", bbb.getHeight() + "");




		ratio = bitmap.getWidth() / ((float) bitmap.getHeight());
		width = bitmap.getWidth();

		System.out.println("orignal width   " + bitmap.getWidth());
		System.out.println("width   " + width);

		// if (width > screenWidth)
		width = screenWidth;
		height = (int) (width / ratio);
		System.out.println("height   " + height);
		System.out.println("ratio  " + ratio);
        if(height>screenHeight)
         {
	      height=screenHeight;
		 }
		bitmap = Utils.BITMAP_RESIZER(bitmap, width, height);

		Log.v("Resize_width", bitmap.getWidth() + "");
		Log.v("Resize_height", bitmap.getHeight()+"");

		Log.v("minus_height", (screenHeight-bitmap.getHeight())+"");
		Log.v("minus_heightTop", (screenHeight-bitmap.getHeight())/2+"");

		mContext = c;
		setFocusable(true);
		setFocusableInTouchMode(true);
		paint = new Paint(Paint.ANTI_ALIAS_FLAG);
		paint.setStyle(Paint.Style.STROKE);
		paint.setPathEffect(new DashPathEffect(new float[] { 7, 14 }, 0));
		paint.setStrokeWidth(3);
		paint.setColor(lighter(Color.RED, 0.1f));
		this.setOnTouchListener(this);
		points = new ArrayList<Point>();
		finalPoints = new ArrayList<Point>();
		bfirstpoint = false;
		setFinalPaint();
	}

	boolean isFinal = false;
	Canvas finalcanvas;

	public void onDraw(Canvas canvas) {

		if (height < (screenHeight)) {
			pointx = (getWidth() / 2 - bitmap.getWidth() / 2);
			pointy = (getHeight() / 2 - bitmap.getHeight() / 2);
		} else {
			pointx = 0;
			pointy = 0;
		}


		canvas.drawBitmap(bitmap, pointx, pointy, null);
		Path path = new Path();
		boolean first = true;

		for (int i = 0; i < points.size(); i += 2) {
			Point point = points.get(i);
			if (first) {
				first = false;				
				path.moveTo(point.x, point.y);
			} else if (i < points.size() - 1) {
				Point next = points.get(i + 1);
				path.quadTo(point.x, point.y, next.x, next.y);
			} else {
				mlastpoint = points.get(i);
				path.lineTo(point.x, point.y);
				if (isFinal) {
					path.lineTo(points.get(0).x, points.get(0).y);
				}
			}
		}

		if (isFinal)
			canvas.drawPath(path, fPaint);
		else
			canvas.drawPath(path, paint);



	}

	public boolean onTouch(View view, MotionEvent event) {
		Point point = new Point();
		point.x = (int) event.getX();
		point.y = (int) event.getY();

		if (flgPathDraw) {
			if (bfirstpoint) {
				if (comparepoint(mfirstpoint, point)) {
					// points.add(point);
					points.add(mfirstpoint);
					finalPoints.add(new Point(point.x, (point.y - pointy)));
					flgPathDraw = false;
					isFinal = true;
					showcropdialog();
				} else {
					points.add(point);
					finalPoints.add(new Point(point.x, (point.y - pointy)));
				}
			} else {
				points.add(point);
				finalPoints.add(new Point(point.x, (point.y - pointy)));
			}
			if (!(bfirstpoint)) {

				mfirstpoint = point;
				bfirstpoint = true;
			}
		}
		invalidate();
		Log.e("Hi  ==>", "Size: " + point.x + " " + point.y);
		if (event.getAction() == MotionEvent.ACTION_UP) {

			mlastpoint = point;
			if (flgPathDraw) {
				if (points.size() > 12) {
					if (!comparepoint(mfirstpoint, mlastpoint)) {
						flgPathDraw = false;
						points.add(mfirstpoint);
						finalPoints.add(new Point(point.x, (point.y - pointy)));
						isFinal = true;

						showcropdialog();
					}
				}
			}
		}

		return true;
	}

	private boolean comparepoint(Point first, Point current) {
		int left_range_x = (int) (current.x - 6);
		int left_range_y = (int) (current.y - 6);

		int right_range_x = (int) (current.x + 6);
		int right_range_y = (int) (current.y + 6);

		if ((left_range_x < first.x && first.x < right_range_x)
				&& (left_range_y < first.y && first.y < right_range_y)) {
			if (points.size() < 10) {
				return false;
			} else {
				return true;
			}
		} else {
			return false;
		}

	}

	public void fillinPartofPath() {
		Point point = new Point();
		point.x = points.get(0).x;
		point.y = points.get(0).y;

		points.add(point);
		invalidate();
	}

	public void resetView() {
		flgPathDraw = true;
		mfirstpoint = null;
		bfirstpoint = false;
		mlastpoint = null;
		points.clear();
		finalPoints.clear();
		paint.setColor(lighter(Color.RED, 0.2f));
		paint.setStyle(Paint.Style.STROKE);
		flgPathDraw = true;
		isFinal = false;
		invalidate();
	}

	private void showcropdialog() {
		DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {

				switch (which) {
				case DialogInterface.BUTTON_POSITIVE:

					final Intent intent;

					if (fromWish == 0) {
						intent = new Intent(mContext,
								UploadWardribeActivity.class);
					} else {
						intent = new Intent(mContext,
								UploadWishListActivity.class);
						// UploadOptionWishListActivity.act.finish();
					}

					new AsyncTask<String, String, String>(){

						ProgressDialog progressDialog;

						@Override
						protected void onPreExecute() {
							super.onPreExecute();

							progressDialog = new ProgressDialog(mContext);
							progressDialog.setMessage("Wait...");

							progressDialog.show();

						}

						@Override
						protected String doInBackground(String... params) {


							/*SomeView.this.setDrawingCacheEnabled(true);
							Bitmap fullBitmap = SomeView.this.getDrawingCache();*/
							/*Utils.write("fullBitmap========"+fullBitmap);
							Utils.write("fullBitmap==width======"+fullBitmap.getWidth());
							Utils.write("fullBitmap===height====="+fullBitmap.getHeight());*/

							intent.putExtra("crop", true);
							intent.putExtra("myPath", imgPath);
							intent.putExtra("height", bitmap.getHeight());
							intent.putExtra("width", bitmap.getWidth());
							intent.putExtra("rotateCount", rotateCount);
							intent.putExtra("ratio", ratio);
							intent.putExtra("categoryId", categoryId);
							intent.putExtra("categoryName", categoryName);


							return null;
						}


						@Override
						protected void onPostExecute(String s) {
							super.onPostExecute(s);

							progressDialog.dismiss();

							mContext.startActivity(intent);
							((CroppingActivity) mContext).finish();

						}
					}.execute(null, null, null);



					break;
				case DialogInterface.BUTTON_NEGATIVE:

					// rotateCount=0;
					resetView();

					break;
				}
			}
		};

		AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
		builder.setMessage("Do you Want to  Re-crop image or continue?")
				.setPositiveButton("Continue", dialogClickListener)
				.setNegativeButton("Re-crop", dialogClickListener).show()
				.setCancelable(false);
	}

	public static int lighter(int color, float factor) {

		float[] hsv = new float[3];
		Color.colorToHSV(color, hsv);
		hsv[2] *= hsv[2] = 1.0f - 0.8f * (1.0f - hsv[2]);
		color = Color.HSVToColor(hsv);
		return color;
	}

//	@SuppressLint("NewApi")
//	private void getScreenDimention(Context context) {
//
//		if (Build.VERSION.SDK_INT >= 7) {
//			Point size = new Point();
//			try {
//				((Activity) context).getWindowManager().getDefaultDisplay()
//						.getRealSize(size);
//				screenWidth = size.x;
//				screenHeight = size.y;
//			} catch (NoSuchMethodError e) {
//			}
//
//		} else {
//			DisplayMetrics metrics = new DisplayMetrics();
//			((Activity) context).getWindowManager().getDefaultDisplay()
//					.getMetrics(metrics);
//			screenWidth = metrics.widthPixels;
//			screenHeight = metrics.heightPixels;
//		}
//
//	}

}