package com.crop;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.DashPathEffect;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Point;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.os.AsyncTask;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Display;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.constants.Constants;
import com.constants.Utils;
import com.stylist.CroppingActivity;
import com.stylist.R;
import com.stylist.wardrobe.UploadWardribeActivity;
import com.stylist.wishlist.UploadWishListActivity;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by adament on 28/4/16.
 */
public class FreeHandCropView extends View implements View.OnTouchListener {

    private Paint paint;
    public static List<Point> points;
    boolean flgPathDraw = true;
    Point mfirstpoint = null;
    boolean bfirstpoint = false;
    Point mlastpoint = null;
    Bitmap bitmap = null;
    Context mContext;
    String sourcepath = null;
    int rotateCount, fromWish;
    String  categoryName,categoryId;
    Context context;
    int minusHeight;

    public FreeHandCropView(Context c, String imagePath, int fromWish, String categoryName, int rotateCount,String categoryId) {
        super(c);
        sourcepath = imagePath;
        context = c;
        this.rotateCount=rotateCount;
        this.fromWish=fromWish;
        this.categoryName=categoryName;
        this.categoryId=categoryId;

        DisplayMetrics display = new DisplayMetrics();
       ((CroppingActivity)c).getWindowManager().getDefaultDisplay().getMetrics(display);

        int screenWidth = display.widthPixels;
        int  screenHeight = display.heightPixels;

        float ratio=(screenHeight/(float)screenWidth);

        bitmap =  BitmapFactory.decodeFile(imagePath);
        bitmap= Utils.rotateImage(bitmap, 90 * rotateCount);

        int scaledHeight=((int)(screenWidth*ratio));

        minusHeight=screenHeight-scaledHeight;

        bitmap = Bitmap.createScaledBitmap(bitmap, screenWidth, scaledHeight, true);

        mContext = c;
        setFocusable(true);
        setFocusableInTouchMode(true);

        paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        paint.setStyle(Paint.Style.STROKE);
        paint.setPathEffect(new DashPathEffect(new float[]{10, 20}, 0));
        paint.setStrokeWidth(5);
        paint.setColor(Color.WHITE);

        this.setOnTouchListener(this);
        points = new ArrayList<Point>();
        bfirstpoint = false;
    }

    public FreeHandCropView(Context context, AttributeSet attrs) {
        super(context, attrs);
        mContext = context;
        setFocusable(true);
        setFocusableInTouchMode(true);
        paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeWidth(2);
        paint.setColor(Color.WHITE);
        this.setOnTouchListener(this);
        points = new ArrayList<Point>();
        bfirstpoint = false;

    }

    public void onDraw(Canvas canvas) {

        canvas.drawBitmap(bitmap, 0, 0, null);
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
            }
        }
        canvas.drawPath(path, paint);
    }

    public boolean onTouch(View view, MotionEvent event) {
        // if(event.getAction() != MotionEvent.ACTION_DOWN)
        // return super.onTouchEvent(event);

        Point point = new Point();
        point.x = (int) event.getX();
        point.y = (int) event.getY();

        if (flgPathDraw) {
            if (bfirstpoint) {
                if (comparepoint(mfirstpoint, point)) {
                    // points.add(point);
                    points.add(mfirstpoint);
                    flgPathDraw = false;
                    showcropdialog();
                }
                else
                {
                    points.add(point);
                }
            }
            else
            {
                points.add(point);
            }

            if (!(bfirstpoint)) {

                mfirstpoint = point;
                bfirstpoint = true;
            }
        }

        invalidate();
        Log.e("Hi  ==>", "Size: " + point.x + " " + point.y);

        if (event.getAction() == MotionEvent.ACTION_UP) {
            Log.d("Action up*******>>>>", "called");
            mlastpoint = point;
            if (flgPathDraw) {
                if (points.size() > 12) {
                    if (!comparepoint(mfirstpoint, mlastpoint)) {
                        flgPathDraw = false;
                        points.add(mfirstpoint);
                        showcropdialog();
                    }
                }
            }
        }

        return true;
    }

    private boolean comparepoint(Point first, Point current) {
        int left_range_x = (int) (current.x - 3);
        int left_range_y = (int) (current.y - 3);

        int right_range_x = (int) (current.x + 3);
        int right_range_y = (int) (current.y + 3);

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
        points.clear();
        paint.setColor(Color.WHITE);
        paint.setStyle(Paint.Style.STROKE);
        flgPathDraw = true;
        invalidate();
    }


    private void showcropdialog() {
        DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                switch (which) {
                    case DialogInterface.BUTTON_POSITIVE:

                        final  Intent intent = new Intent(mContext, UploadWardribeActivity.class );
                        final Bitmap cropedImage =  getCropedBitmap();

                        new AsyncTask<String, String, String>(){

                            ProgressDialog progressDialog = new ProgressDialog(mContext);

                            @Override
                            protected void onPreExecute() {
                                super.onPreExecute();

                                progressDialog.setMessage("Wait...");
                                progressDialog.show();

                            }

                            @Override
                            protected String doInBackground(String... params) {



                                Log.v("CP_width", cropedImage.getWidth()+"");
                                Log.v("CP_height", cropedImage.getHeight() + "");

                                intent.putExtra("crop", true);
                                intent.putExtra("myPath", sourcepath);
                                intent.putExtra("height", bitmap.getHeight());
                                intent.putExtra("width", bitmap.getWidth());
                                intent.putExtra("rotateCount", rotateCount);
                                intent.putExtra("categoryId", categoryId);
                                intent.putExtra("categoryName", categoryName);

                                return null;
                            }

                            @Override
                            protected void onPostExecute(String s) {
                                super.onPostExecute(s);

                                progressDialog.dismiss();

                                String saveBitmapPath = saveOnShare(cropedImage);

                                intent.putExtra("BITMAP_PATH", saveBitmapPath);

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


    //crop image helper methods

    int leftmostPoint, rightmostPoint, topmostPoint, bottommostPoint,croppedImageheight,croppedImageWidth;

    private void getRecttPoint() {
        if(FreeHandCropView.points.size()>0)
        {
            leftmostPoint = FreeHandCropView.points.get(0).x;
            rightmostPoint = FreeHandCropView.points.get(0).x;
            topmostPoint = FreeHandCropView.points.get(0).y;
            bottommostPoint = FreeHandCropView.points.get(0).y;


            for (int i = 0; i < FreeHandCropView.points.size(); i++) {
                if (leftmostPoint > FreeHandCropView.points.get(i).x)
                    leftmostPoint = FreeHandCropView.points.get(i).x;
                if (rightmostPoint < FreeHandCropView.points.get(i).x)
                    rightmostPoint = FreeHandCropView.points.get(i).x;
                if (topmostPoint > FreeHandCropView.points.get(i).y)
                    topmostPoint = FreeHandCropView.points.get(i).y;
                if (bottommostPoint < FreeHandCropView.points.get(i).y)
                    bottommostPoint = FreeHandCropView.points.get(i).y;
            }

            croppedImageWidth = (rightmostPoint - leftmostPoint);
            croppedImageheight = (bottommostPoint - topmostPoint);

            Log.v("Top", topmostPoint+"" );
            Log.v("bottom", bottommostPoint+"" );
            Log.v("right", rightmostPoint+"" );
            Log.v("left", leftmostPoint+"" );

            Log.v("c_image_width", croppedImageWidth+"" );
            Log.v("c_image_height", croppedImageheight+"" );


        }
    }



    public Bitmap getCropedBitmap() {

        Boolean crop=true;
        DisplayMetrics display = new DisplayMetrics();
        ((CroppingActivity)context).getWindowManager().getDefaultDisplay().getMetrics(display);
        int screenWidth = display.widthPixels;
        int screenHeight = display.heightPixels;

        Log.v("Screen_width", screenWidth + "");
        Log.v("Screen_height", screenHeight + "");

        float ratio = (screenHeight / (float) screenWidth);

        int scaledHeight=((int)(screenWidth*ratio));
        Bitmap resultingImage = Bitmap.createScaledBitmap(BitmapFactory.decodeFile(sourcepath), screenWidth, scaledHeight, true);

        Log.v("width_orignal", resultingImage.getWidth() + "");
        Log.v("height_orignal", resultingImage.getHeight() + "");

        Canvas canvas = new Canvas(resultingImage);
        Paint paint = new Paint();
        paint.setAntiAlias(true);

        Path path = new Path();
        for (int i = 0; i < FreeHandCropView.points.size(); i++) {
            path.lineTo(FreeHandCropView.points.get(i).x, FreeHandCropView.points.get(i).y);
        }
        canvas.drawPath(path, paint);

        // to show cropped area
        if(crop) {
            Utils.write("free hand crop in           " + crop);
            paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));


        }
        else
        {
            Utils.write("free hand crop out           "+crop);
            paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_OUT));

        }

        canvas.drawBitmap(resultingImage, 0, 0, paint);

       /* Log.v("width", resultingImage.getWidth() + "");
        Log.v("height", resultingImage.getHeight() + "");
*/
        getRecttPoint();

        Utils.write("minusHeight===================="+minusHeight);

        Bitmap cropedBitmap = Bitmap.createBitmap(resultingImage, leftmostPoint, topmostPoint, croppedImageWidth, (croppedImageheight));

        //saveOnShare(cropedBitmap);

       // compositeImageView.setImageBitmap(cropedBitmap);

        Log.v("croped_width", cropedBitmap.getWidth() + "");
        Log.v("croped_height", cropedBitmap.getHeight() + "");

        return cropedBitmap;
    }





    private Bitmap croppedBitmap(Boolean crop){

        int widthOfscreen = 0;
        int heightOfScreen = 0;

        DisplayMetrics dm = new DisplayMetrics();
        try {
            ((CroppingActivity)mContext).getWindowManager().getDefaultDisplay().getMetrics(dm);
        } catch (Exception ex) {
        }
        widthOfscreen = dm.widthPixels;
        heightOfScreen = dm.heightPixels;

        //compositeImageView = (ImageView) findViewById(R.id.our_imageview);


        DisplayMetrics display = new DisplayMetrics();
        ((CroppingActivity)mContext).getWindowManager().getDefaultDisplay().getMetrics(display);
        int screenWidth = display.widthPixels;
        int screenHeight = display.heightPixels;

        Log.v("Screen_width", screenWidth + "");
        Log.v("Screen_height", screenHeight + "");

        float ratio = (screenHeight / (float) screenWidth);


        Bitmap bitmap2 = BitmapFactory.decodeFile(sourcepath);


        bitmap2 = Bitmap.createScaledBitmap(bitmap2, screenWidth, ((int) (screenWidth * ratio)), true);
        // bitmap2 = Bitmap.createScaledBitmap(bitmap2, screenWidth/2, screenHeight/2, true);

        Log.v("width_orignal", bitmap2.getWidth() + "");
        Log.v("height_orignal", bitmap2.getHeight() + "");


        Bitmap resultingImage = Bitmap.createBitmap(widthOfscreen,
                heightOfScreen, bitmap2.getConfig());

        Canvas canvas = new Canvas(resultingImage);
        Paint paint = new Paint();
        paint.setAntiAlias(true);

        Path path = new Path();
        for (int i = 0; i < FreeHandCropView.points.size(); i++) {
            path.lineTo(FreeHandCropView.points.get(i).x, FreeHandCropView.points.get(i).y);
        }
        canvas.drawPath(path, paint);
        if (crop) {
            paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));

        } else {
            paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_OUT));
        }
        canvas.drawBitmap(bitmap2, 0, 0, paint);

        Log.v("width", resultingImage.getWidth() + "");
        Log.v("height", resultingImage.getHeight() + "");

        getRecttPoint();

        Bitmap cropedBitmap = Bitmap.createBitmap(resultingImage, leftmostPoint, topmostPoint, croppedImageWidth, croppedImageheight);

        return cropedBitmap;

//        saveOnShare(cropedBitmap);

        //compositeImageView.setImageBitmap(cropedBitmap);


    }







    public String saveOnShare(Bitmap sourceBitmap) {

        String  tempSavedFilePath = null;

        if (sourceBitmap != null && !sourceBitmap.isRecycled()) {


            FileOutputStream out = null;

            tempSavedFilePath  =getTempImgBgFilename1();
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
                mContext.getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);

            } catch (Exception e) {
                e.printStackTrace();
            }

        } else
        {
            Utils.write("bitmap is nullllllllll");
        }


        return tempSavedFilePath;
    }



    public String getTempImgBgFilename1() {

        File file = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES), Constants.IMAGE_DIRECTORY_NAME+"/"+Constants.IMAGE_TEMP_CROPED);
        if (!file.exists()) {
            file.mkdirs();
        }
        String uriSting = (file.getAbsolutePath() + "/" + "temp.jpg");
        System.out.println("uriSting-----------------" + uriSting);
        return uriSting;

    }

}


