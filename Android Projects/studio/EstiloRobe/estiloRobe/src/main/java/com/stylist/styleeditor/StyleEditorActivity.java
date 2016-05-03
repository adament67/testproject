package com.stylist.styleeditor;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

import com.constants.BaseActivity;
import com.constants.Constants;
import com.constants.CustomTextView;
import com.constants.LocalDatabaseHandler;
import com.constants.Utils;
import com.constants.WebImageLoader;

import com.parse.ParseUser;
import com.stylist.CroppingActivity;
import com.stylist.R;
import com.stylist.Selector;
import com.stylist.SelectorStyleEditor;
import com.stylist.lookbook.LookBookActivity;
import com.stylist.lookbook.UploadLookBook;

import android.app.Dialog;
import android.content.ContentValues;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.PointF;
import android.graphics.Typeface;
import android.os.Bundle;
import android.provider.MediaStore.Images;
import android.provider.MediaStore.Images.Media;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Toast;

public class StyleEditorActivity extends BaseActivity implements OnTouchListener {
	com.constants.CustomTextView headerName, dotted_textView, saveText;
	//RelativeLayout imageRelLayout,/* imageLayout,*/ saveRelLayout;
	RelativeLayout imageLayout,saveRelLayout;
	LinearLayout nextOrDelButton;
	LinearLayout singleButton, doubleButton;//zoomButLayout,  delImgView;
	com.constants.CustomTextView skinColorButton, browseButton, uploadLookBut;
	Boolean isUplodClicked = false;
	int totalImages = 0;
	String showImagePath;
	ImageView favouriteImage, imagViewToAdd, nextOrDel, selectedImg, myImageViewforOri;
		/* 	delete_img, plus_img, min_img */;
	Boolean isTouched = false;
	ArrayList<ImageView> imagesViewToAdd = new ArrayList<ImageView>();
	Bitmap lookBookBitmap;
	Dialog uploadLook;
	int firsttimePicnch = 1;
	Boolean goToNet = false, goToColourSelection = false, goToBrowse = false;;
	public static String url;
	public static StyleEditorActivity act;
	ArrayList<RelativeLayout> LayoutimageViewList = new ArrayList<RelativeLayout>();
	ArrayList<ImageView> imageViewList = new ArrayList<ImageView>();
	ArrayList<OnTouchListener> onTouchListenerList = new ArrayList<OnTouchListener>();
	ArrayList<OnClickListener> OnClickListenerList = new ArrayList<OnClickListener>();
	HashMap<View, String> hashmap = new HashMap<View, String>();
	HashMap<View, Integer> hashmapWidth = new HashMap<View, Integer>();
	HashMap<View, Integer> hashmapHeight = new HashMap<View, Integer>();
	HashMap<View, Boolean> hashmapFirsttime = new HashMap<View, Boolean>();
	// HashMap<View, RelativeLayout> hashmapLayout = new HashMap<View,
	// RelativeLayout>();
	float dist0 = 1, distCurrent = 1;
	private Matrix matrixn = new Matrix();
	private float scale = 1f;
	private float lastscale = 1f;
	ArrayList<ClipArt> cClipArt;
	// private ScaleGestureDetector SGD;
int count=1000;
ClipArt ca=null;
public void onStart() {
		super.onStart();
		//.onStartSession(this, getResources().getString(R.string.flurryKey));
	}

	public void onStop() {
		super.onStop();
		//.onEndSession(this);
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_style_editor);

		setHeading("STYLE EDITOR");
		backClick();
		act = this;
		nextOrDelButton = (LinearLayout) findViewById(R.id.nextOrDelButton);
		nextOrDel = (ImageView) findViewById(R.id.nextOrDel);
		singleButton = (LinearLayout) findViewById(R.id.single_button);
		doubleButton = (LinearLayout) findViewById(R.id.doubleButton);
		singleButton.setVisibility(View.GONE);
		doubleButton.setVisibility(View.VISIBLE);

		nextOrDelButton.setVisibility(View.VISIBLE);
		nextOrDelButton.setOnClickListener(this);
		saveRelLayout = (RelativeLayout) findViewById(R.id.saveRelLayout);

		saveRelLayoutListener();


		saveText = (com.constants.CustomTextView) findViewById(R.id.saveText);

		imageLayout = (RelativeLayout) findViewById(R.id.imageLayoutSkinColour);

		mWebImageLoader = new WebImageLoader(context);
		browseButton = (com.constants.CustomTextView) findViewById(R.id.browseButton);
		browseButton.setOnClickListener(this);
		skinColorButton = (com.constants.CustomTextView) findViewById(R.id.skinColorButton);
		skinColorButton.setOnClickListener(this);
		mRrootLayout = (ViewGroup) findViewById(R.id.root);



		dotted_textView = (com.constants.CustomTextView) findViewById(R.id.dotted_textView);
		dotted_textView.setOnClickListener(this);



		//making invisible drag on parent layout touch
		imageLayout.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {

				if(cClipArt!=null)
				{
					for(int c=0;c<cClipArt.size();c++)
					{
						cClipArt.get(c).btnscl.setVisibility(View.GONE);
						cClipArt.get(c).btndel.setVisibility(View.GONE);
						ca.isScalevisible=false;
						cClipArt.get(c).imgring.setBackgroundResource(0);
					}
				}
			}
		});

		cClipArt=new ArrayList<ClipArt>();
		imageLayout.setOnTouchListener(new OnTouchListener() {

			@Override
			public boolean onTouch(View v, MotionEvent event) {
				// TODO Auto-generated method stub
				saveText.setText("Save");
				return false;
			}
		});
	}


	//boolean isDialogVisible = false;
	private void saveRelLayoutListener() {

		saveRelLayout.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {


				if(ca!=null)
				{
					ca.btnscl.setVisibility(View.GONE);
					ca.btndel.setVisibility(View.GONE);
					ca.isScalevisible=false;
					ca.imgring.setBackgroundResource(0);
				}
				if(cClipArt!=null)
				{
					for(int c=0;c<cClipArt.size();c++)
					{
						cClipArt.get(c).btnscl.setVisibility(View.GONE);
						cClipArt.get(c).btndel.setVisibility(View.GONE);
						ca.isScalevisible=false;
						cClipArt.get(c).imgring.setBackgroundResource(0);
					}
				}
				//saveRelLayout.setClickable(false);
				setPrefrenceBool(Constants.isUpdated, true);
				if (isTouched == true) {
					selectedImg.setBackgroundResource(NONE);
				}
				imageLayout.setDrawingCacheEnabled(true);
				lookBookBitmap = imageLayout.getDrawingCache();
				if (lookBookBitmap != null) {

					uploadDialog();




				}

			}
		});


	}

	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
		saveText.setText("Save");
	}


	@Override
	public void onClick(View v) {

		switch (v.getId()) {

		case R.id.browseButton:
			Intent i3 = new Intent(context, SelectorStyleEditor.class);
			i3.putExtra("fromEditor", 1);
			startActivityForResult(i3, BrowseFromResult);
			goToBrowse = true;
			overridePendingTransition(R.anim.slide_in_up, R.anim.slide_out_up);

			break;

		case R.id.skinColorButton:
			goToColourSelection = true;
			Intent i = new Intent(this, ColourPickerActivity.class);
			startActivityForResult(i, colorPickerRequestCode);
			break;
		case R.id.saveRelLayout:

			break;
		case R.id.dotted_textView:
			Intent i4 = new Intent(context, SelectorStyleEditor.class);
			i4.putExtra("fromEditor", 1);

			startActivityForResult(i4, BrowseFromResult);
			goToBrowse = true;
			overridePendingTransition(R.anim.slide_in_up, R.anim.slide_out_up);
			break;

		default:
			break;
		}

	}






	Dialog uploadDialog;
	String name,ocassion,comments;
	private void uploadDialog() {
		try {

			uploadDialog = addDialog(R.layout.activity_upload_look_book);
			CustomTextView saveText = (CustomTextView)uploadDialog. findViewById(R.id.bottomText);
			final EditText nameText = (EditText) uploadDialog. findViewById(R.id.nameText);
			final	EditText ocassionText = (EditText) uploadDialog. findViewById(R.id.ocassionText);
			final	EditText commentsText = (EditText)uploadDialog. findViewById(R.id.commentsText);
			saveText.setText("Upload");
			Typeface face= Typeface.createFromAsset(getAssets(), "fonts/lvnm.ttf");
			nameText.setTypeface(face);
			ocassionText.setTypeface(face);
			commentsText.setTypeface(face);

			saveText.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View arg0) {
					name=nameText.getText().toString().trim();
					ocassion=ocassionText.getText().toString().trim();
					comments=commentsText.getText().toString().trim();
					saveImage();

					//isDialogVisible = false;

					upLoadLookbook() ;
				}
			});

			uploadDialog.show();

		} catch (Exception e) {
			// TODO: handle exception
		}
	}





	LocalDatabaseHandler dbHelper;


	public void upLoadLookbook() {
		dbHelper = new LocalDatabaseHandler(context);
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		if (name.equals(""))
			showToastS("Please enter name.");
//		else
//		if (ocassion.equals(""))
//			showToastS("Please enter ocassion.");
//		else
//		if (comments.equals(""))
//			showToastS("Please enter comments.");
		else{
			long lng = dbHelper.insertLookbookItem(ParseUser.getCurrentUser().getObjectId(),
					name, ocassion,comments,
					showImagePath, format.format(new Date()));
			uploadDialog.dismiss();

			//isDialogVisible = false;

			checkAndCloseActivity(StyleEditorActivity.act);
			gotoActivityWithfinish(LookBookActivity.class, null);

		}

	}














	public static final int BrowseFromResult = 1;
	public static final int BrowseFromWishlistActivityResult = 2;
	public static final int BrowseFromWardrobeActivityResult = 3;
	public static final int colorPickerRequestCode = 121;
	WebImageLoader mWebImageLoader;

	@Override
	protected void onActivityResult(int requestCode, int responseCode, Intent data) {
		// TODO Auto-generated method stub
		super.onActivityResult(requestCode, responseCode, data);
		System.out.println("onActivityResult=====");
		if (requestCode == colorPickerRequestCode && responseCode == RESULT_OK) {
			int r = data.getIntExtra("r", 0);
			int g = data.getIntExtra("g", 0);
			int b = data.getIntExtra("b", 0);
			int alpha = data.getIntExtra("alpha", 0);
			System.out.println("red========" + r);
			imageLayout.setBackgroundColor(Color.argb(alpha, r, g, b));
		}
		if (requestCode == BrowseFromResult && responseCode == RESULT_OK) {
			int index = Integer.parseInt(data.getStringExtra("intent_data"));
			switch (index) {
			case 1:
				Intent i3 = new Intent(context, BrowseFromWarMain.class);
				startActivityForResult(i3, BrowseFromWardrobeActivityResult);
				overridePendingTransition(R.anim.animation_enter_from_right, R.anim.animation_leave_out_to_left);
				break;

			case 2:
				Intent i4 = new Intent(context, BrowseFromWishListActivity.class);
				startActivityForResult(i4, BrowseFromWishlistActivityResult);
				overridePendingTransition(R.anim.animation_enter_from_right, R.anim.animation_leave_out_to_left);
				break;

			default:
				break;
			}

		}
		if (requestCode == BrowseFromWardrobeActivityResult && responseCode == RESULT_OK) {
			url = data.getStringExtra("intent_data");
			System.out.println("total else    images=    url====" + url);
			loadImage();
			dotted_textView.setVisibility(View.GONE);
		} else if (requestCode == BrowseFromWishlistActivityResult && responseCode == RESULT_OK) {
			url = data.getStringExtra("intent_data");
			loadImage();
			dotted_textView.setVisibility(View.GONE);
		}
	}

	private ViewGroup mRrootLayout;

//	private int _xDelta;
//	private int _yDelta;
//	Matrix matrix = new Matrix();
//	Matrix savedMatrix = new Matrix();

	static final int NONE = 0;

	// Remember some things for zooming
//	PointF start = new PointF();
//	PointF mid = new PointF();
//	float oldDist = 1f;
//	String savedItemClicked;

	// ImageView selectedImg = null;
	//ImageView myImageView;
	Bitmap bitmap;
	int bmpWidth, bmpHeight;

	// Touch event related variables
//	int touchState;
//	final int IDLE = 0;
//	final int TOUCH = 1;
//	final int PINCH = 2;
	int newHeight, newWidth;

//	private void drawMatrix(View view, float curScale) {
//
//		int height = (int) (newHeight * curScale);
//		int width = (int) (newWidth * curScale);
//
//		System.out.println("height=========" + height);
//		System.out.println("width=========" + width);
//		newHeight = height;
//		newWidth = width;
//
//		System.out.println("newHeight=========" + newHeight);
//		System.out.println("newWidth=========" + newWidth);
//
//		if (hashmapFirsttime.get(view) != null) {
//			/*
//			 * if(hashmapFirsttime.get(view)==false) {
//			 */
//			newHeight = hashmapHeight.get(view);
//			newWidth = hashmapWidth.get(view);
//			hashmapFirsttime.put(view, true);
//			// }
//		}
//
//		if (newWidth < 200) {
//			newWidth = 200;
//			newHeight = (int) (200 * (bmpHeight / (float) bmpWidth));
//		}
//		if (newWidth > 1200) {
//			newWidth = 1200;
//			newHeight = (int) (1200 * (bmpWidth / (float) bmpWidth));
//		}
//
//		resizedBitmap = Bitmap.createScaledBitmap(bitmap, newWidth, newHeight, false);
//		((ImageView) view).setImageBitmap(resizedBitmap);
//	}

	//float prevDistCurrent = 0f;

	public void loadImage() {
//		myImageViewforOri = new ImageView(context);
//		myImageViewforOri.setImageBitmap(BitmapFactory.decodeFile(url));
//		myImageViewforOri.setPadding(1, 1, 1, 1);
//		myImageViewforOri.setScaleType(ScaleType.FIT_CENTER);
//		final RelativeLayout relativewithImage = new RelativeLayout(context);
//		RelativeLayout.LayoutParams layout_description = new RelativeLayout.LayoutParams(
//				RelativeLayout.LayoutParams.FILL_PARENT, RelativeLayout.LayoutParams.FILL_PARENT);
//		RelativeLayout.LayoutParams layout_description_fix = new RelativeLayout.LayoutParams(
//				300, 400);
//		
//		relativewithImage.setLayoutParams(layout_description);
//		 myImageViewforOri.setLayoutParams(layout_description_fix);
//		relativewithImage.setGravity(Gravity.CENTER);
//		myImageView = new ImageView(context);
//		 myImageView.setLayoutParams(layout_description_fix);
//		imageViewList.add(myImageView);
//		myImageView.setScaleType(ScaleType.FIT_CENTER);
//		imageViewList.get(imageViewList.size() - 1).setImageBitmap(BitmapFactory.decodeFile(url));
//		(imageViewList.get(imageViewList.size() - 1)).setPadding(1, 1, 1, 1);
//		(imageViewList.get(imageViewList.size() - 1)).setScaleType(ScaleType.FIT_CENTER);
//		relativewithImage.addView(myImageView);
//		LayoutimageViewList.add(relativewithImage);
//		onTouchListenerList.add(new OnTouchListener() {
//
//			@Override
//			public boolean onTouch(View view, MotionEvent event) {
//				// TODO Auto-generated method stub
//
//				isTouched = true;
//				if (hashmap.get(view) != null) {
//
//					myImageViewforOri.setImageBitmap(BitmapFactory.decodeFile(hashmap.get(view)));
//				} else {
//					hashmap.put(view, url);
//				}
//				/*
//				 * if (hashmapLayout.get(view) != null) {
//				 * 
//				 * hashmapLayout.get(view).bringToFront(); } else {
//				 * hashmapLayout.put(view,relativewithImage); }
//				 */
//
//				final int X = (int) event.getRawX();
//				final int Y = (int) event.getRawY();
//				if (selectedImg != null) {
//					selectedImg.setBackgroundResource(NONE);
//				}
//				selectedImg = (ImageView) view;
//				view.setBackgroundDrawable(getResources().getDrawable(R.drawable.border_selection_thick_width));
//				float distx, disty;
//				bitmap = ((BitmapDrawable) (myImageViewforOri).getDrawable()).getBitmap();
//		     	bmpWidth = bitmap.getWidth();
//			    bmpHeight = bitmap.getHeight();
//
//				
//				
//				view.bringToFront();
//
//				switch (event.getAction() & MotionEvent.ACTION_MASK) {
//
//				case MotionEvent.ACTION_DOWN: // single touch
//					zoomButLayout.setVisibility(View.VISIBLE);
//					zoomButLayout.bringToFront();
//					if (hashmapHeight.get(view) != null) {
//						newWidth = hashmapWidth.get(view);
//						newHeight = hashmapHeight.get(view);
//					} else {
//						newWidth = bmpWidth;
//						newHeight = bmpHeight;
//					}
//					RelativeLayout.LayoutParams lParams = (RelativeLayout.LayoutParams) view.getLayoutParams();
//					System.out.println(" lParams.leftMargin==========" + lParams.leftMargin);
//					_xDelta = X - lParams.leftMargin;
//					_yDelta = Y - lParams.topMargin;
//					touchState = TOUCH;
//
//					break;
////				case MotionEvent.ACTION_POINTER_DOWN: // multi touch
////					// load previous matrix
////					oldDist = spacing(event);
////					touchState = PINCH;
////					savedMatrix.set(matrix);
////					midPoint(mid, event);
////					distx = event.getX(0) - event.getX(1);
////					disty = event.getY(0) - event.getY(1);
////					System.out.println("distx==========" + distx);
////					System.out.println("disty==========" + disty);
////					dist0 = (float) Math.sqrt(distx * distx + disty * disty);
////
////					break;
//				case MotionEvent.ACTION_MOVE: // pinch zoom
//					if (touchState == TOUCH) {
//
//						RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) view.getLayoutParams();
//						layoutParams.leftMargin = X - _xDelta;
//						layoutParams.topMargin = Y - _yDelta;
//
//						view.setLayoutParams(layoutParams);
//					} else if (touchState == PINCH) {
//
//						distx = event.getX(0) - event.getX(1);
//						disty = event.getY(0) - event.getY(1);
//
//						distCurrent = (float) Math.sqrt(distx * distx + disty * disty);
//						System.out.println("distCurrent========" + distCurrent);
//						System.out.println("prevDistCurrent========" + prevDistCurrent);
//
//						if (distCurrent != prevDistCurrent) {
//							if (distCurrent >= dist0)
//								drawMatrix(view, 1.05f);
//							else
//								drawMatrix(view, 0.95f);
//
//				
//						}
//						prevDistCurrent = distCurrent;
//						return true;
//					}
//
//					break;
//				case MotionEvent.ACTION_UP:
//					// A pressed gesture has finished.
//					/*
//					 * RelativeLayout.LayoutParams layout_description_wrap = new
//					 * RelativeLayout.LayoutParams(
//					 * RelativeLayout.LayoutParams.WRAP_CONTENT,
//					 * RelativeLayout.LayoutParams.WRAP_CONTENT);
//					 * view.setLayoutParams(layout_description_wrap);
//					 */
//					touchState = IDLE;
//					// view.setBackgroundResource(NONE);
//
//					break;
////				case MotionEvent.ACTION_POINTER_UP:
////					// A non-primary pointer has gone up.
////					  hashmapHeight.put(view, newHeight);
////					 hashmapWidth.put(view, newWidth); 
////					 touchState = IDLE;
////					  hashmapFirsttime.put(view, false);
////					 
////					// firstTime=false;
////					// view.setBackgroundResource(NONE);
////
////					break;
//				}
//				mRrootLayout.invalidate();
//				return false;
//			}
//		});
//
//		imageViewList.get(imageViewList.size() - 1)
//				.setOnTouchListener(onTouchListenerList.get(onTouchListenerList.size() - 1));
//		OnClickListenerList.add(new OnClickListener() {
//			@Override
//			public void onClick(View arg0) {
//				// TODO Auto-generated method stub
//			}
//		});
//		imageViewList.get(imageViewList.size() - 1)
//				.setOnClickListener(OnClickListenerList.get(OnClickListenerList.size() - 1));
//		imageLayout.addView(LayoutimageViewList.get(LayoutimageViewList.size() - 1));
//		switch (totalImages) {
//		}
//
//		totalImages++;
//
//	
		
//		Constants.url=url;
//		bubble_childView=getLayoutInflater().inflate(R.layout.addsticker, null);
//		RelativeLayout.LayoutParams lp=new RelativeLayout.LayoutParams(LayoutParams.WRAP_CONTENT,LayoutParams.WRAP_CONTENT);
//		bubble_childView.setLayoutParams(lp);
//		imageLayout.addView(bubble_childView);
//	    sticker_view =(SingleFingerStickerView)bubble_childView.findViewById(R.id.sticker_view);
//

		System.out.println("url====" + url);
		if(ca!=null)
		{
			ca.btndel.setVisibility(View.GONE);
			ca.btnscl.setVisibility(View.GONE);
			ca.isScalevisible=false;
			ca.imgring.setBackgroundResource(0);
		}
		
		 ca = new ClipArt(StyleEditorActivity.this,url);
		 cClipArt.add(ca);
		imageLayout.addView(ca);
		ca.setId(count++);

	}	
		

	public void saveImage() {
		if (lookBookBitmap != null && !lookBookBitmap.isRecycled()) {
			FileOutputStream out = null;
			File imageFile = Utils.getOutputMediaFileLook();
			try {
				out = new FileOutputStream(imageFile);
				Boolean imageSaved = lookBookBitmap.compress(Bitmap.CompressFormat.PNG, 90, out);
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
				String imageName = "IMG_" + System.currentTimeMillis() + ".png";
				ContentValues values = new ContentValues(3);
				values.put(Media.TITLE, imageName);
				values.put(Media.MIME_TYPE, "image/png");
				values.put("_data", imageFile.getAbsolutePath());
				getContentResolver().insert(Media.EXTERNAL_CONTENT_URI, values);
				showImagePath = imageFile.getAbsolutePath();
			} catch (Exception e) {
			}
		}

	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		//saveRelLayout.setClickable(true);

		if (imageLayout.getChildCount() > 0) {

			// shareButton.setVisibility(View.VISIBLE);
		}
		if (goToColourSelection == true) {
			goToColourSelection = false;

			// getPrefrence("categoryName")
		}
		if (goToBrowse == true) {
			saveRelLayout.setVisibility(View.VISIBLE);
			goToColourSelection = false;
			// getPrefrence("categoryName")
		}

		if (goToNet) {
			imageLayout.setBackgroundColor(Color.TRANSPARENT);
			goToNet = false;
		}
	}

	@Override
	public boolean onTouch(View arg0, MotionEvent arg1) {
		// TODO Auto-generated method stub
		return false;
	}


}
