package com.stylist;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import com.bumptech.glide.util.Util;
import com.constants.BaseActivity;
import com.constants.Constants;
import com.constants.CustomTextView;
import com.constants.LocalDatabaseHandler;
import com.constants.Utils;

import com.parse.ParseFile;
import com.parse.ParseUser;
import com.viewpagerindicator.CirclePageIndicator;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.Dialog;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.provider.MediaStore.Images;
import android.provider.MediaStore.Images.Media;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.Window;
import android.view.inputmethod.EditorInfo;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import de.hdodenhof.circleimageview.CircleImageView;

public class ProfileActivity extends BaseActivity {
	LocalDatabaseHandler dbHelper;
	Button registerButton;
	private static final int CAPTURE_IMG = 8;
	String showImagePath = null;
	Uri imageUri;
	Bitmap bitmap;
	RadioButton extraSmall, small, medium, larg, extraLarg, doubleLarg;
	public static final int GALLERY_INTENT_CALLED = 054;
	public static final int GALLERY_KITKAT_INTENT_CALLED = 045;
	CircleImageView profile_photo;
	TextView headerName, height, weight,nameTextView;

	EditText  emailEditText/* , bodyStatsEditText */, bust, waist, hips, occupationEditText,
			bottom_waist_size, skinComplexion, characteristics, lovingColor, prefBrand, styleIcon, outfits,
			about_yourself;
	CustomTextView changePhoto, viewPhoto;
	// RelativeLayout profilePicLay;
	LinearLayout backButtonLayout, nextOrDelButton;
	// Button submit;
	TextView style, shape, styleDescrip;
	ImageView nextOrDel;
	TextView birthday, size;
	ImageView ok1, cance1;
	Dialog bodyShapeDialog;
	int selectedBodyShape = -1;
	ArrayList<String> heightType = new ArrayList<String>();
	ArrayList<String> heightType1 = new ArrayList<String>();
	ArrayList<String> weightType = new ArrayList<String>();
	public static ProfileActivity pProfileActivity;
	String profPicPath;

	public void onStart() {
		super.onStart();
		//.onStartSession(this, getResources().getString(R.string.flurryKey));
	}

	public void onStop() {
		super.onStop();
		//.onEndSession(this);
	}

	int[] imageArray = { R.drawable.img5, R.drawable.img1, R.drawable.img4, R.drawable.img2, R.drawable.img3 };

	int[] imageArraystyle = { R.drawable.indian, R.drawable.indo, R.drawable.western };
	String[] titlestyle = { "Indian wear", "Indo western wear", "Western wear" };
	String[] title = { "ROUND (APPLE)", "TRIANGLE(PEAR)", "RECTANGLE", "INVERTED TRIANGLE", "HOURGLASS" };
	String[] descriptionarraystyle = {};
	String[] descriptionarray = {
			"Waist/chest measures larger than shoulder and hips  rounded shoulders, wide hips, and full upper thighs",
			"Hips measure larger than shoulders and waist",
			"Shoulders, waist and hips are of almost same measurement, very little definition and flat bottom at hips",
			"Shoulders are broader than waist and hips measurement",
			"Shoulders and hips are the somewhat equal in measurement with a well-defined waist" };

	RadioGroup sizeGroup;
	RadioButton radio1, radio2, radio3, radio4, radio5, radio6, radioE, radioC, radioF; // male,
																						// female,

	@SuppressLint("ResourceAsColor")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_profile);
		setHeading("PROFILE");
		backClick();
		Utils.setupUI(findViewById(R.id.toplinLay), this);
		/*
		 * profilePicLay = (RelativeLayout) findViewById(R.id.profilePicLay);
		 * profilePicLay.setOnClickListener(this);
		 */
		pProfileActivity = this;
		nextOrDelButton = (LinearLayout) findViewById(R.id.nextOrDelButton);
		nextOrDel = (ImageView) findViewById(R.id.nextOrDel);
		nextOrDelButton.setVisibility(View.VISIBLE);
		nextOrDelButton.setOnClickListener(this);
		nameTextView = (TextView) findViewById(R.id.name);
		nameTextView.setText(ParseUser.getCurrentUser().getUsername());
		shape = (TextView) findViewById(R.id.shape);
		profile_photo = (CircleImageView) findViewById(R.id.profile_photo);
		profile_photo.setOnClickListener(this);
		profile_photo.setImageResource(R.drawable.img);
		occupationEditText = (EditText) findViewById(R.id.occupation);
		bust = (EditText) findViewById(R.id.bust);
		waist = (EditText) findViewById(R.id.waist);

		hips = (EditText) findViewById(R.id.hips);

		height = (TextView) findViewById(R.id.height);
		height.setOnClickListener(this);
		weight = (TextView) findViewById(R.id.weight);
		weight.setOnClickListener(this);
		bottom_waist_size = (EditText) findViewById(R.id.bottom_waist_size);
		skinComplexion = (EditText) findViewById(R.id.skinComplexion);
		characteristics = (EditText) findViewById(R.id.characteristics);
		lovingColor = (EditText) findViewById(R.id.lovingcolor);
		prefBrand = (EditText) findViewById(R.id.clothbrand);
		styleIcon = (EditText) findViewById(R.id.styleIcon);
		// fashion_type=(TextView)findViewById(R.id.fashion_type);
		outfits = (EditText) findViewById(R.id.outfits);
		styleDescrip = (TextView) findViewById(R.id.styleDescrip);
		about_yourself = (EditText) findViewById(R.id.about_yourself);
		registerButton = (Button) findViewById(R.id.registerButton);
		registerButton.setOnClickListener(this);

		radio1 = (RadioButton) findViewById(R.id.radio1);
		radio2 = (RadioButton) findViewById(R.id.radio2);
		radio3 = (RadioButton) findViewById(R.id.radio3);
		radio4 = (RadioButton) findViewById(R.id.radio4);
		radio5 = (RadioButton) findViewById(R.id.radio5);
		radio6 = (RadioButton) findViewById(R.id.radio6);
		radioC = (RadioButton) findViewById(R.id.radioC);
		radioE = (RadioButton) findViewById(R.id.radioE);
		radioF = (RadioButton) findViewById(R.id.radioF);

		extraSmall = (RadioButton) findViewById(R.id.extraSmall);
		small = (RadioButton) findViewById(R.id.small);
		medium = (RadioButton) findViewById(R.id.medium);
		larg = (RadioButton) findViewById(R.id.larg);
		extraLarg = (RadioButton) findViewById(R.id.extraLarg);
		doubleLarg = (RadioButton) findViewById(R.id.doubleLarg);

		style = (TextView) findViewById(R.id.style);
		shape = (TextView) findViewById(R.id.shape);
		birthday = (TextView) findViewById(R.id.birthday);
		size = (TextView) findViewById(R.id.size);
		sizeGroup = (RadioGroup) findViewById(R.id.sizeGroup);

		dbHelper = new LocalDatabaseHandler(context);
		
		occupationEditText.setOnEditorActionListener(new TextView.OnEditorActionListener() {
			@Override
			public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {

				if (actionId == EditorInfo.IME_ACTION_NEXT) {
					dateTimeDialog();
					return true;
				}
				return false;
			}
		});
		
		bust.setOnEditorActionListener(new TextView.OnEditorActionListener() {
			@Override
			public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {

				if (actionId == EditorInfo.IME_ACTION_NEXT) {
					waist.requestFocus();
					return true;
				}
				return false;
			}
		});

		
		waist.setOnEditorActionListener(new TextView.OnEditorActionListener() {
			@Override
			public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {

				if (actionId == EditorInfo.IME_ACTION_NEXT) {
					hips.requestFocus();
					return true;
				}
				return false;
			}
		});
		
		hips.setOnEditorActionListener(new TextView.OnEditorActionListener() {
			@Override
			public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {

				if (actionId == EditorInfo.IME_ACTION_NEXT) {
                   heightDialog();	
                   return true;
				}
				return false;
			}
		});
		
		Typeface font = Typeface.createFromAsset(context.getAssets(), "fonts/lvnm.ttf");

		radio1.setTypeface(font);
		radio2.setTypeface(font);
		radio3.setTypeface(font);
		radio4.setTypeface(font);

		radio5.setTypeface(font);

		radio6.setTypeface(font);

		radioC.setTypeface(font);

		radioE.setTypeface(font);
		radioF.setTypeface(font);

		birthday.setOnClickListener(this);

		style.setOnClickListener(this);
		shape.setOnClickListener(this);

		radioF.setOnClickListener(this);

		heightType.add("1");
		heightType.add("2");
		heightType.add("3");
		heightType.add("4");
		heightType.add("5");
		heightType.add("6");
		heightType.add("7");
		heightType.add("8");
		heightType.add("9");
		heightType.add("10");

		heightType1.add("0");
		heightType1.add("1");
		heightType1.add("2");
		heightType1.add("3");
		heightType1.add("4");
		heightType1.add("5");
		heightType1.add("6");
		heightType1.add("7");
		heightType1.add("8");
		heightType1.add("9");
		heightType1.add("10");
		heightType1.add("11");
		heightType1.add("12");

		weightType.add("20-25");
		weightType.add("26-30");
		weightType.add("31-35");
		weightType.add("36-40");
		weightType.add("41-45");
		weightType.add("46-50");
		weightType.add("51-55");
		weightType.add("56-60");
		weightType.add("61-65");
		weightType.add("66-70");
		weightType.add("71-75");
		weightType.add("76-80");
		weightType.add("81-85");
		weightType.add("86-90");
		weightType.add("91-95");
		weightType.add("96-100");

		changePhoto = (CustomTextView) findViewById(R.id.change_photo);
		viewPhoto = (CustomTextView) findViewById(R.id.view_photo1);
		changePhoto.setOnClickListener(this);
		viewPhoto.setOnClickListener(this);

		radioC.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			@Override
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
				// System.out.println("rad1 out checked");
				if (isChecked) {
					// //System.out.println("rad1 checked");
					radioC.setTextColor(getResources().getColor(R.color.redish_text_color));
					radioE.setChecked(false);
					radioF.setChecked(false);
					fashion_type = "c";

				} else {
					radioC.setTextColor(getResources().getColor(R.color.grayTextColor));
				}
			}
		});

		radioE.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			@Override
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
				// System.out.println("rad1 out checked");
				if (isChecked) {
					// System.out.println("rad1 checked");
					radioC.setTextColor(getResources().getColor(R.color.grayTextColor));
					radioE.setTextColor(getResources().getColor(R.color.redish_text_color));
					radioC.setChecked(false);
					radioF.setChecked(false);

					fashion_type = "e";
				} else {
					radioE.setTextColor(getResources().getColor(R.color.grayTextColor));
				}
			}
		});

		radioF.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			@Override
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
				// System.out.println("rad1 out checked");
				if (isChecked) {
					// System.out.println("rad1 checked");
					radioC.setTextColor(getResources().getColor(R.color.grayTextColor));
					radioF.setTextColor(getResources().getColor(R.color.redish_text_color));
					radioC.setChecked(false);
					radioE.setChecked(false);
					fashion_type = "f";

				} else {
					radioF.setTextColor(getResources().getColor(R.color.grayTextColor));
				}
			}
		});

		radio1.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			@Override
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
				// System.out.println("rad1 out checked");
				if (isChecked) {
					// System.out.println("rad1 checked");
					radio1.setTextColor(getResources().getColor(R.color.redish_text_color));
					radio2.setChecked(false);
					radio3.setChecked(false);
					radio4.setChecked(false);
					radio5.setChecked(false);
					radio6.setChecked(false);
					your_style = "Classic";
				} else {
					radio1.setTextColor(getResources().getColor(R.color.grayTextColor));
				}
			}
		});

		radio2.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			@Override
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
				// System.out.println("rad1 out checked");
				if (isChecked) {
					radio2.setTextColor(getResources().getColor(R.color.redish_text_color));
					radio1.setTextColor(getResources().getColor(R.color.grayTextColor));
					radio1.setChecked(false);
					radio3.setChecked(false);
					radio4.setChecked(false);
					radio5.setChecked(false);
					radio6.setChecked(false);
					your_style = "Casual";
				} else {
					radio2.setTextColor(getResources().getColor(R.color.grayTextColor));
				}
			}
		});

		radio3.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			@Override
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
				// System.out.println("rad1 out checked");
				if (isChecked) {
					radio3.setTextColor(getResources().getColor(R.color.redish_text_color));
					radio1.setTextColor(getResources().getColor(R.color.grayTextColor));
					// System.out.println("rad1 checked");
					radio2.setChecked(false);
					radio1.setChecked(false);
					radio4.setChecked(false);
					radio5.setChecked(false);
					radio6.setChecked(false);
					your_style = "Masculine";
				} else {
					radio3.setTextColor(getResources().getColor(R.color.grayTextColor));
				}
			}
		});

		radio4.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			@Override
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
				// System.out.println("rad1 out checked");
				if (isChecked) {
					radio4.setTextColor(getResources().getColor(R.color.redish_text_color));
					radio1.setTextColor(getResources().getColor(R.color.grayTextColor));
					// System.out.println("rad1 checked");
					radio2.setChecked(false);
					radio3.setChecked(false);
					radio1.setChecked(false);
					radio5.setChecked(false);
					radio6.setChecked(false);
					your_style = "Sexy";
				} else {
					radio4.setTextColor(getResources().getColor(R.color.grayTextColor));
				}
			}
		});

		radio5.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			@Override
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
				// System.out.println("rad1 out checked");
				if (isChecked) {
					radio5.setTextColor(getResources().getColor(R.color.redish_text_color));
					radio1.setTextColor(getResources().getColor(R.color.grayTextColor));
					radio2.setChecked(false);
					radio3.setChecked(false);
					radio4.setChecked(false);
					radio1.setChecked(false);
					your_style = "Eclectic";
					radio6.setChecked(false);
				} else {
					radio5.setTextColor(getResources().getColor(R.color.grayTextColor));
				}
			}
		});

		radio6.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			@Override
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

				if (isChecked) {
					radio6.setTextColor(getResources().getColor(R.color.redish_text_color));
					radio1.setTextColor(getResources().getColor(R.color.grayTextColor));
					radio2.setChecked(false);
					radio3.setChecked(false);
					radio4.setChecked(false);
					radio5.setChecked(false);
					radio1.setChecked(false);
					your_style = "Trendy";
				} else {
					radio6.setTextColor(getResources().getColor(R.color.grayTextColor));
				}
			}
		});

		sizeGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {

			@Override
			public void onCheckedChanged(RadioGroup group, int checkedId) {
				// TODO Auto-generated method stub
				if (checkedId == R.id.extraSmall) {
					size.setText("XS");

				} else if (checkedId == R.id.small) {
					size.setText("S");
				}
				if (checkedId == R.id.medium) {
					size.setText("M");
				} else if (checkedId == R.id.larg) {
					size.setText("L");
				}
				if (checkedId == R.id.extraLarg) {
					size.setText("XL");
				} else if (checkedId == R.id.doubleLarg) {
					size.setText("XXL");
				}
			}
		});

		/*
		 * if(Constants.fbProfileUrl!=null) {
		 * System.out.println("Constants.fbProfileUrl=====setttt=========="+
		 * Constants.fbProfileUrl); new
		 * WebImageLoader(context).DisplayImageCircle(Constants.fbProfileUrl,
		 * profile_photo); }
		 */
		setContent();
	}


	/*private void backClickProfileAct() {

		ImageView backBtn = (ImageView) this.findViewById(R.id.backButtonImage);
		backBtn.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {

				Intent intent=new Intent();
				intent.putExtra("IMAGE_PATH", "");
				setResult(9,intent);
				finish();

				finishActivity();

			}
		});
	}*/

	/*public void saveOnShare(Bitmap sourceBitmap) {
		if (sourceBitmap != null && !sourceBitmap.isRecycled()) {
			FileOutputStream out = null;
			File imageFile = Utils.getOutputMediaFileProfile();
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
				profPicPath = imageFile.getAbsolutePath();
			} catch (Exception e) {
			}
		}

	}
*/
//	private void saveOnParse() {
//		ParseUser parseUser = ParseUser.getCurrentUser();
//		parseUser.setUsername(nameEditText.getText().toString());
//		parseUser.put("bust", bust.getText().toString());
//		parseUser.put("waist", waist.getText().toString());
//		parseUser.put("hips", hips.getText().toString());
//		parseUser.put("Shape", shape.getText().toString());
//		parseUser.put("Height", height.getText().toString());
//		parseUser.put("Weight", weight.getText().toString());
//		parseUser.put("Size", size.getText().toString());
//		parseUser.put("BottomWaistSize", bottom_waist_size.getText().toString());
//		
//		if(showImagePath!=null)
//		{
//		ByteArrayOutputStream stream = new ByteArrayOutputStream();
//		BitmapFactory.decodeFile(showImagePath)
//				.compress(Bitmap.CompressFormat.PNG, 100, stream);
//		
//		ParseFile file = new ParseFile("image.png", (byte[]) stream.toByteArray());
//		parseUser.put("ProfilePicture", file);
//		}
//		try {
//			parseUser.put("BirthDate", format11.parse(birthday.getText().toString()));
//		} catch (Exception e) {
//			// TODO: handle exception
//		}
//		parseUser.put("SkinComplexion", skinComplexion.getText().toString());
//		parseUser.put("Characteristics", characteristics.getText().toString());
//		parseUser.put("ColorPrefered", lovingColor.getText().toString());
//		parseUser.put("BrandPrefered", prefBrand.getText().toString());
//		parseUser.put("StyleIcon", styleIcon.getText().toString());
//		parseUser.put("FashionType", fashion_type);
//		parseUser.put("Outfits", outfits.getText().toString());
//		parseUser.put("StyleDescription", your_style);
//		parseUser.put("AboutYourself", about_yourself.getText().toString());
//		parseUser.put("occupation", occupationEditText.getText().toString());
//		parseUser.saveEventually();
//		showToastL("Profile has been updated.");
//		
//		finish();
//	}

	SimpleDateFormat format11 = new SimpleDateFormat("MMMM dd, yyyy");

	String fashion_type = "c", your_style = "Classic";// , selectedSize = "";
	int index = 0;
	private CirclePageIndicator circlepageindicator;
	private ViewPager viewPager;
	public static final int BrowseFromResult = 1;
	int prevPos = 0;
	int swipeIndex = 0;

	// String gender;

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.registerButton:
			//Toast.makeText(ProfileActivity.this, "Hi", Toast.LENGTH_SHORT).show();
             if(bitmap!=null) {
				 saveOnShare();
			 }
            else {
				 saveUserinDatabase(showImagePath);
			 }

			
//
//			try {


//				if (isValidFields()) {
//					SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
//					Cursor cUser = dbHelper.getUser(ParseUser.getCurrentUser().getObjectId());
//
//					if (cUser.moveToFirst()) {
//						Cursor cUserDel = dbHelper.deleteUser(ParseUser.getCurrentUser().getObjectId());
//						System.out.println("cUserDel=======" + cUserDel);
//						setPrefrenceBool(Constants.isUpdated, true);
//						dbHelper.insertUser(ParseUser.getCurrentUser().getObjectId(), nameEditText.getText().toString(),
//								showImagePath, "male", occupationEditText.getText().toString(),
//								birthday.getText().toString(),
//								// bodyStatsEditText.getText().toString(),
//								bust.getText().toString(), waist.getText().toString(), hips.getText().toString(),


//								height.getText().toString(), weight.getText().toString(), size.getText().toString(),
//								bottom_waist_size.getText().toString(), skinComplexion.getText().toString(),
//								characteristics.getText().toString(), lovingColor.getText().toString(),
//								prefBrand.getText().toString(), /* style_icon */styleIcon.getText().toString(),
//								fashion_type, outfits.getText().toString(),
//								your_style /*
//											 * styleDescrip.getText().toString()
//											 */, about_yourself.getText().toString(), style.getText().toString(),
//								shape.getText().toString(), format.format(new Date()));
//					} else {
//						System.out.println("in else");
//						setPrefrenceBool(Constants.isUpdated, true);
//						dbHelper.insertUser(ParseUser.getCurrentUser().getObjectId(), nameEditText.getText().toString(),
//								showImagePath, "male", occupationEditText.getText().toString(),
//								birthday.getText().toString(),
//								/* bodyStatsEditText.getText().toString() */bust.getText().toString(),
//								waist.getText().toString(), hips.getText().toString(), height.getText().toString(),
//								weight.getText().toString(), size.getText().toString(),
//								bottom_waist_size.getText().toString(), skinComplexion.getText().toString(),
//								characteristics.getText().toString(), lovingColor.getText().toString(),
//								prefBrand.getText().toString(), styleIcon.getText().toString(), fashion_type,
//								outfits.getText().toString(),
//								your_style /*
//											 * styleDescrip . getText ()
//											 * .toString ()
//											 */, about_yourself.getText().toString(), style.getText().toString(),
//								shape.getText().toString(), format.format(new Date()).toString());
//
//					}
//					setPrefrence("username", nameEditText.getText().toString());
//					setPrefrence("profilePath", showImagePath);
//
//				//	saveOnParse();
//
//				}
//			} catch (Exception e) {
//				// TODO: handle exception
//			}
//			saveUserinDatabase();

			break;
		case R.id.height:
			heightDialog();
			break;
		case R.id.weight:
			weightDialog();
			break;
		case R.id.birthday:
			dateTimeDialog();
			break;
		case R.id.profile_photo:


			if(getPrefrenceBool("KEY_FB")){

				Toast.makeText(ProfileActivity.this, "You are not permitted to change your facebook profile image :)", Toast.LENGTH_SHORT).show();

			}else{

				Intent i4 = new Intent(ProfileActivity.this, Selector.class);
				i4.putExtra("fromEditor", 0);
				startActivityForResult(i4, 9);
				overridePendingTransition(R.anim.slide_in_up, R.anim.slide_out_up);

			}


			break;
		case R.id.change_photo:

			if(getPrefrenceBool("KEY_FB")){

				Toast.makeText(ProfileActivity.this, "You are not permitted to change your facebook profile image :)", Toast.LENGTH_SHORT).show();

			}else {

				Intent intent = new Intent(ProfileActivity.this, Selector.class);
				intent.putExtra("fromEditor", 0);
				startActivityForResult(intent, 9);// Activity is started with requestCode 2

				overridePendingTransition(R.anim.slide_in_up, R.anim.slide_out_up);

			}

			break;
		case R.id.view_photo1:
			viewProfileDialog();

			break;
		case R.id.ok:
			selectedBodyShape = viewPager.getCurrentItem();
			// System.out.println("selectedBodyShape " + selectedBodyShape);
			bodyShapeDialog.dismiss();

			style.setText(titlestyle[selectedBodyShape]);
			break;
		case R.id.cancel:
			dateTimeDialog();
			break;

		case R.id.style:
			index = 0;
			prevPos = 0;
			swipeIndex = 0;
			bodyShapeDialog = addDialog(R.layout.body_shape_dialog);

			final ViewPager viewPager1 = (ViewPager) bodyShapeDialog.findViewById(R.id.horPager);

			final CirclePageIndicator circlepageindicator = (CirclePageIndicator) bodyShapeDialog
					.findViewById(R.id.circlepageindicator);

			ImagePagerAdapter adapet1 = null;
			adapet1 = new ImagePagerAdapter(imageArraystyle, titlestyle, descriptionarraystyle, false);

			viewPager1.setAdapter(adapet1);
			circlepageindicator.setViewPager(viewPager1);
			bodyShapeDialog.show();

			final ImageView next1 = (ImageView) bodyShapeDialog.findViewById(R.id.next);
			final ImageView prev1 = (ImageView) bodyShapeDialog.findViewById(R.id.prev);
			viewPager1.addOnPageChangeListener(new OnPageChangeListener() {

				@Override
				public void onPageSelected(int position) {
					// TODO Auto-generated method stub
					System.out.println("position========" + position);
					System.out.println("prevPos========" + prevPos);

					if (position >= prevPos) {
						swipeIndex++;
						prevPos = position;
						index = swipeIndex;
					} else if (position < prevPos) {
						swipeIndex--;
						prevPos = position;
						index = swipeIndex;
					}
					if (position == 0) {
						prev1.setVisibility(View.INVISIBLE);
					} else {
						prev1.setVisibility(View.VISIBLE);
					}

					if (position == (titlestyle.length - 1)) {
						next1.setVisibility(View.INVISIBLE);
					} else {
						next1.setVisibility(View.VISIBLE);
					}
					circlepageindicator.setViewPager(viewPager1, position);
				}

				@Override
				public void onPageScrolled(int arg0, float arg1, int arg2) {
					// TODO Auto-generated method stub

				}

				@Override
				public void onPageScrollStateChanged(int arg0) {
					// TODO Auto-generated method stub

				}
			});

			ok1 = (ImageView) bodyShapeDialog.findViewById(R.id.ok);
			cance1 = (ImageView) bodyShapeDialog.findViewById(R.id.cancel);

			next1.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View arg0) { // TODO Auto-generated method
													// stub

					prev1.setVisibility(View.VISIBLE);
					if (index < (imageArraystyle.length - 1)) {
						index++;
						System.out.println("index  " + index);
						viewPager1.setCurrentItem(index);
					}
					if (index == (imageArraystyle.length - 1))
						next1.setVisibility(View.INVISIBLE);

				}
			}
			/* this */);
			prev1.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View arg0) { // TODO Auto-generated method
													// stub
					next1.setVisibility(View.VISIBLE);
					if (index > 0) {
						index--;
						viewPager1.setCurrentItem(index);
					}
					if (index == 0)
						prev1.setVisibility(View.INVISIBLE);
				}
			}
			/* this */);

			ok1.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View arg0) {
					// TODO Auto-generated method stub
					selectedBodyShape = viewPager1.getCurrentItem();

					bodyShapeDialog.dismiss();

					style.setText(titlestyle[selectedBodyShape]);
					selectedStyle = titlestyle[selectedBodyShape];
				}
			});
			cance1.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View arg0) {
					// TODO Auto-generated method stub
					bodyShapeDialog.dismiss();
				}
			});

			break;

		case R.id.shape:
			index = 0;
			prevPos = 0;
			swipeIndex = 0;
			bodyShapeDialog = addDialog(R.layout.body_shape_dialog);

			viewPager = (ViewPager) bodyShapeDialog.findViewById(R.id.horPager);

			final CirclePageIndicator circlepageindicator1 = (CirclePageIndicator) bodyShapeDialog
					.findViewById(R.id.circlepageindicator);

			ImagePagerAdapter adapet = null;
			adapet = new ImagePagerAdapter(imageArray, title, descriptionarray, true);

			viewPager.setAdapter(adapet);
			circlepageindicator1.setViewPager(viewPager);
			bodyShapeDialog.show();
			ok1 = (ImageView) bodyShapeDialog.findViewById(R.id.ok);
			cance1 = (ImageView) bodyShapeDialog.findViewById(R.id.cancel);

			final ImageView next2 = (ImageView) bodyShapeDialog.findViewById(R.id.next);
			final ImageView prev2 = (ImageView) bodyShapeDialog.findViewById(R.id.prev);
			next2.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View arg0) {
					// TODO Auto-generated method stub
					System.out.println("onclickkkkkkkkkkkkkkkk");
					prev2.setVisibility(View.VISIBLE);
					if (index < (imageArray.length - 1)) {
						index++;
						viewPager.setCurrentItem(index);

					}
					if (index == (imageArray.length - 1))
						next2.setVisibility(View.INVISIBLE);
				}
			});
			prev2.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View arg0) {
					// TODO Auto-generated method stub
					System.out.println("onclickkkkkkkkkkkkkkkk prevvvvvvvvv");

					next2.setVisibility(View.VISIBLE);
					if (index > 0) {
						index--;
						viewPager.setCurrentItem(index);
					}
					if (index == 0)
						prev2.setVisibility(View.INVISIBLE);
				}
			});

			ok1.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View arg0) {
					// TODO Auto-generated method stub
					selectedBodyShape = viewPager.getCurrentItem();
					// System.out.println("selectedBodyShape "
					// + selectedBodyShape);
					bodyShapeDialog.dismiss();
					shape.setText(title[selectedBodyShape]);
					selectedSape = title[selectedBodyShape];
				}
			});
			cance1.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View arg0) {
					// TODO Auto-generated method stub
					bodyShapeDialog.dismiss();
				}
			});

			viewPager.addOnPageChangeListener(new OnPageChangeListener() {

				@Override
				public void onPageSelected(int position) {
					// TODO Auto-generated method stub
					System.out.println("onviewpagerrrrrrrrrrrrrrrrrrr");

					if (position > prevPos) {
						swipeIndex++;
						prevPos = position;
						index = swipeIndex;
						System.out.println("greater than");
					} else if (position < prevPos) {
						swipeIndex--;
						System.out.println("l than");
						index = swipeIndex;
						prevPos = position;
					}
					if (position == 0) {
						prev2.setVisibility(View.INVISIBLE);
					} else {
						prev2.setVisibility(View.VISIBLE);
					}
					if (position == (title.length - 1)) {
						next2.setVisibility(View.INVISIBLE);
					} else {
						next2.setVisibility(View.VISIBLE);
					}
					circlepageindicator1.setViewPager(viewPager, position);

				}

				@Override
				public void onPageScrolled(int arg0, float arg1, int arg2) {
					// TODO Auto-generated method stub

				}

				@Override
				public void onPageScrollStateChanged(int arg0) {
					// TODO Auto-generated method stub

				}
			});

			break;

		default:
			break;
		}
	}

	String dateString1;

	String imgPath = "";

	@SuppressLint("NewApi")
	@Override
	protected void onActivityResult(int requestCode, int responseCode, Intent data) {
		// TODO Auto-generated method stub
		super.onActivityResult(requestCode, responseCode, data);

		if (requestCode == 9 && responseCode == RESULT_OK) {

			Log.v("111111111", "yessssssssss");

			Selector.dumpIntent(data);

		}else{

			if(data != null){

				Log.v("111111111", "Noo");
				Bundle bundle = data.getExtras();
				String imgPath= (String) bundle.get("IMAGE_PATH");
				Utils.write("imgPath above dumpIntent===" + imgPath);
				showImagePath =imgPath;

				System.out.println("showImgPath ca[p=======" + showImagePath);
				Bitmap b  = Utils.orientedBitmap(showImagePath);
				if(b!=null) {
					float dx = (float) b.getHeight() / b.getWidth();
					bitmap = Utils.getResizedBitmap(b, 200, (int) (200 * dx));
					//circle image
					bitmap = getRoundedCornerBitmap(bitmap, 140);
					System.out.println("Capture===bitmap====" + bitmap);
					profile_photo.setImageBitmap(bitmap);
				}
				Selector.dumpIntent(data);
			}else{

				Log.v("Result_aaa", "exit");
			}


		}



		/*if (requestCode == 7 && responseCode == RESULT_OK) {
			int index = Integer.parseInt(data.getStringExtra("intent_data"));


			for (String key : data.getExtras().keySet()) {
				Object value = data.getExtras().get(key);
				Log.d("Log", String.format("%s %s (%s)", key,
						value.toString(), value.getClass().getName()));
			}*/

       // Utils.write(""+filePath);
			/*switch (index) {
			case 1:
				if (Build.VERSION.SDK_INT < 19) {
					Intent intent = new Intent();
					intent.setType("image/jpeg");
					intent.setAction(Intent.ACTION_GET_CONTENT);
					startActivityForResult(Intent.createChooser(intent, "Select Image"), GALLERY_INTENT_CALLED);
				} else {
					Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
					intent.addCategory(Intent.CATEGORY_OPENABLE);
					intent.setType("image/jpeg");
					startActivityForResult(intent, GALLERY_KITKAT_INTENT_CALLED);
				}
				break;

			case 2:
				ContentValues values = new ContentValues();
				values.put(Media.TITLE, "New Picture");
				values.put(Media.DESCRIPTION, "From your Camera");
				imageUri = getContentResolver().insert(Media.EXTERNAL_CONTENT_URI, values);
				Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
				intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
				startActivityForResult(intent, CAPTURE_IMG);
				break;

			default:
				break;
			}*/

		//}

		/*if (requestCode == CAPTURE_IMG && responseCode == RESULT_OK) {
			try {

				for (String key : data.getExtras().keySet()) {
					Object value = data.getExtras().get(key);
					Log.d("Log", String.format("%s %s (%s)", key,
							value.toString(), value.getClass().getName()));
				}


//				if (imageUri != null) {
//					showImagePath = getRealPathFromURI(imageUri);
//
//					System.out.println("Capture=======" + imageUri.toString());
//
//					System.out.println("showImgPath ca[p=======" + showImagePath);
//
//						Bitmap b  = Utils.orientedBitmap(showImagePath);
//
//					float dx = (float)b.getHeight()/b.getWidth();
//
//					bitmap = Utils.getResizedBitmap(b, 200, (int)(200*dx));
//
//
//					//circle image
//					bitmap = getRoundedCornerBitmap(bitmap, 140);
//
//
//
//					System.out.println("Capture===bitmap====" + bitmap);
//					profile_photo.setImageBitmap(bitmap);
//					System.out.println("Capture=======" + showImagePath);
//					//saveOnShare();
//					//saveUserinDatabase();
//
//					ParseApplication appLevel = (ParseApplication)getApplicationContext();
//
//					appLevel.setProfileImage(bitmap);
//
//
//				} else {
//					showToastS("Image Capture failed!");
//					finish();
//				}

			} catch (Exception e) {
				// TODO: handle exception
			}
		}*/

		/*else if (data != null && requestCode == GALLERY_INTENT_CALLED || requestCode == GALLERY_KITKAT_INTENT_CALLED) {

			if (null == data)
				return;
			Uri originalUri = null;
			if (requestCode == GALLERY_INTENT_CALLED) {
				originalUri = data.getData();
			} else if (requestCode == GALLERY_KITKAT_INTENT_CALLED) {
				originalUri = data.getData();
				final int takeFlags = data.getFlags()
						& (Intent.FLAG_GRANT_READ_URI_PERMISSION | Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
				// Check for the freshest data.
				getContentResolver().takePersistableUriPermission(originalUri, takeFlags);
			}
			showImagePath = getPath(this, originalUri);

			System.out.println("showImgPath==gal=====" + showImagePath);

			*//*bitmap = Utils.getResizedBitmap(Utils.orientedBitmap(showImagePath), 200, 200) ;
			System.out.println("gallery=======" + showImagePath);

			profile_photo.setImageBitmap(bitmap);*//*


			Bitmap b = Utils.orientedBitmap(showImagePath);
			Utils.write("b==orientedBitmap=="+b);
			if (b == null) {
				showToastS("This image is corrupted,failed to build image matrix !");
			} else {
				float dx = (float) b.getHeight() / b.getWidth();

				bitmap = Utils.getResizedBitmap(b, 200, (int) (200 * dx));


				//circle image
				bitmap = getRoundedCornerBitmap(bitmap, 140);


				System.out.println("Capture===bitmap====" + bitmap);
				profile_photo.setImageBitmap(bitmap);


				ParseApplication appLevel = (ParseApplication) getApplicationContext();

				appLevel.setProfileImage(bitmap);

				//saveOnShare();

				//saveUserinDatabase();
			}

			System.out.println("showImgPath=======" + showImagePath);

			//saveOnParse();
		}*/
		}

	@TargetApi(Build.VERSION_CODES.KITKAT)
	@SuppressLint("NewApi")

	/*public String getPath(final Context context, final Uri uri) {

		final boolean isKitKat = Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT;

		if (isKitKat && DocumentsContract.isDocumentUri(context, uri)) {

			if (isExternalStorageDocument(uri)) {
				final String docId = DocumentsContract.getDocumentId(uri);
				final String[] split = docId.split(":");
				final String type = split[0];

				if ("primary".equalsIgnoreCase(type)) {
					return Environment.getExternalStorageDirectory() + "/" + split[1];
				}

			}
			// DownloadsProvider
			else if (isDownloadsDocument(uri)) {

				final String id = DocumentsContract.getDocumentId(uri);
				final Uri contentUri = ContentUris.withAppendedId(Uri.parse("content://downloads/public_downloads"),
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
					contentUri = Media.EXTERNAL_CONTENT_URI;
				} else if ("video".equals(type)) {
					contentUri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI;
				} else if ("audio".equals(type)) {
					contentUri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
				}

				final String selection = "_id=?";
				final String[] selectionArgs = new String[] { split[1] };

				return getDataColumn(context, contentUri, selection, selectionArgs);
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
	}*/

	/*public String getDataColumn(Context context, Uri uri, String selection, String[] selectionArgs) {

		Cursor cursor = null;
		final String column = "_data";
		final String[] projection = { column };

		try {
			cursor = context.getContentResolver().query(uri, projection, selection, selectionArgs, null);
			if (cursor != null && cursor.moveToFirst()) {
				final int column_index = cursor.getColumnIndexOrThrow(column);
				return cursor.getString(column_index);
			}
		} finally {
			if (cursor != null)
				cursor.close();
		}
		return null;
	}*/

	/*public boolean isExternalStorageDocument(Uri uri) {
		return "com.android.externalstorage.documents".equals(uri.getAuthority());
	}

	public boolean isDownloadsDocument(Uri uri) {
		return "com.android.providers.downloads.documents".equals(uri.getAuthority());
	}*/

	/*public boolean isMediaDocument(Uri uri) {
		return "com.android.providers.media.documents".equals(uri.getAuthority());
	}*/

	public String getRealPathFromURI(Uri contentUri) {
		String[] proj = { Media.DATA };
		Cursor cursor = managedQuery(contentUri, proj, null, null, null);
		int column_index = cursor.getColumnIndexOrThrow(Media.DATA);
		cursor.moveToFirst();
		return cursor.getString(column_index);
	}

	private void dateTimeDialog() {
		final Dialog dateTimeDialogg = new Dialog(context);
		dateTimeDialogg.requestWindowFeature(Window.FEATURE_NO_TITLE);
		dateTimeDialogg.setContentView(R.layout.date_timepicker_dialog);
		dateTimeDialogg.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
		dateTimeDialogg.show();
		final TextView sel_datetime = (TextView) dateTimeDialogg.findViewById(R.id.sel_datetime);
		final DatePicker datePicker = (DatePicker) dateTimeDialogg.findViewById(R.id.datePicker);
		final TextView close = (TextView) dateTimeDialogg.findViewById(R.id.close);
		datePicker.setMaxDate(System.currentTimeMillis() - 1000);
		datePicker.setDescendantFocusability(DatePicker.FOCUS_BLOCK_DESCENDANTS);
		datePicker.init(datePicker.getYear() - 21, datePicker.getMonth(), datePicker.getDayOfMonth(), null);

		close.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				dateTimeDialogg.dismiss();
			}
		});

		sel_datetime.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {

				dateString1 = formatDateto(datePicker.getDayOfMonth(), datePicker.getYear(),
						(datePicker.getMonth() + 1));

				birthday.setText(dateString1);
				bust.requestFocus();
				dateTimeDialogg.dismiss();
			}
		});

	}

	private class ImagePagerAdapter extends PagerAdapter {

		int[] imageArray;
		String[] titlearr;
		String[] descriptionarray;
		boolean is_bodyShape;

		public ImagePagerAdapter(int[] imageArray, String[] titlearr, String[] descriptionarray, boolean is_bodyShape) {
			this.imageArray = imageArray;
			this.titlearr = titlearr;
			this.is_bodyShape = is_bodyShape;
			this.descriptionarray = descriptionarray;
		}

		@Override
		public Object instantiateItem(ViewGroup container, final int position) {
			View view = null;
			view = View.inflate(context, R.layout.body_shape_viewpager_layout, null);
			((ViewPager) container).addView(view, 0);
			final ImageView main = (ImageView) view.findViewById(R.id.main);
			ImageView selected = (ImageView) view.findViewById(R.id.selected);
			final TextView title = (TextView) view.findViewById(R.id.title);

			System.out.println("vvvvvvv  " + titlearr[position]);
			final TextView description = (TextView) view.findViewById(R.id.description);
			main.setImageResource(imageArray[position]);
			title.setText(titlearr[position]);
			if (is_bodyShape)
				description.setText(descriptionarray[position]);
			else
				description.setVisibility(View.GONE);

			if (titlearr[position].equals(selectedSape) || titlearr[position].equals(selectedStyle))
				selected.setVisibility(View.VISIBLE);
			else
				selected.setVisibility(View.GONE);

			return view;
		}

		@Override
		public void destroyItem(View arg0, int arg1, Object arg2) {
			((ViewPager) arg0).removeView((View) arg2);
		}

		@Override
		public boolean isViewFromObject(View arg0, Object arg1) {
			return arg0 == ((View) arg1);
		}

		@Override
		public int getCount() {
			return imageArray.length;
		}
	}

	private class ImagePagerAdapterStyle extends PagerAdapter {

		int[] imageArray;
		String[] titlearr;
		String[] descriptionarray;
		boolean is_bodyShape;

		public ImagePagerAdapterStyle(int[] imageArray, String[] titlearr, String[] descriptionarray,
				boolean is_bodyShape) {
			this.imageArray = imageArray;
			this.titlearr = titlearr;
			this.is_bodyShape = is_bodyShape;
			this.descriptionarray = descriptionarray;
		}

		@Override
		public Object instantiateItem(ViewGroup container, final int position) {
			View view = null;
			view = View.inflate(context, R.layout.body_shape_viewpager_layout, null);
			((ViewPager) container).addView(view, 0);
			final ImageView main = (ImageView) view.findViewById(R.id.main);
			ImageView selected = (ImageView) view.findViewById(R.id.selected);
			final TextView title = (TextView) view.findViewById(R.id.title);

			System.out.println("vvvvvvv  " + titlearr[position]);

			final TextView description = (TextView) view.findViewById(R.id.description);
			main.setImageResource(imageArray[position]);
			title.setText(titlearr[position]);
			if (is_bodyShape)
				description.setText(descriptionarray[position]);
			else
				description.setVisibility(View.GONE);

			if (titlearr[position].equals(selectedSape) || titlearr[position].equals(selectedStyle))
				selected.setVisibility(View.VISIBLE);
			else
				selected.setVisibility(View.GONE);

			return view;
		}

		@Override
		public void destroyItem(View arg0, int arg1, Object arg2) {
			((ViewPager) arg0).removeView((View) arg2);
		}

		@Override
		public boolean isViewFromObject(View arg0, Object arg1) {
			return arg0 == ((View) arg1);
		}

		@Override
		public int getCount() {
			return imageArray.length;
		}
	}

	SimpleDateFormat sdf = new SimpleDateFormat("MMM dd,yyyy");
	SimpleDateFormat sdf1 = new SimpleDateFormat("dd/mm/yyyy");
	SimpleDateFormat parseFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");

	public void saveOnShare() {

		String imageName;

		if (bitmap != null && !bitmap.isRecycled()) {
			FileOutputStream out = null;
			File imageFile = Utils.getOutputMediaFileProfile();
			try {

				Log.d("ok", "111");

				out = new FileOutputStream(imageFile);
				Boolean imageSaved = bitmap.compress(Bitmap.CompressFormat.PNG, 90, out);

			} catch (Exception e) {
				e.printStackTrace();
				Utils.write("excep1");

			} finally {

				Log.d("ok", "333");

				if (out != null) {
					try {

						Log.d("ok", "444");

						out.flush();
					} catch (IOException e) {
						// TODO Auto-generated catch block
						Utils.write("excep2");
						e.printStackTrace();
					}
					try {
						Log.d("ok", "555");
						out.close();
					} catch (IOException e) {
						// TODO Auto-generated catch block
						Utils.write("excep3");
						e.printStackTrace();
					}
				}else{

					Log.d("ok", "else 444");
				}
			}
			try {

				Log.d("ok", "666");

				imageName = "IMG_" + System.currentTimeMillis() + ".png";
				ContentValues values = new ContentValues(3);
				values.put(Media.TITLE, imageName);
				values.put(Media.MIME_TYPE, "image/png");
				values.put("_data", imageFile.getAbsolutePath());
				getContentResolver().insert(Media.EXTERNAL_CONTENT_URI, values);
				showImagePath = imageFile.getAbsolutePath();
				System.out.println("showImagePath=======" + showImagePath);
				saveUserinDatabase(showImagePath);
				
			} catch (Exception e) {
e.printStackTrace();
				Utils.write("excep4");
				Log.d("ok", "777");

			}
		}else{

			Log.d("ok", "222");
		}

	}

	String selectedSape = "", selectedStyle = "";

	private void setContent() {

		Cursor cUser = dbHelper.getUser(ParseUser.getCurrentUser().getObjectId());
		System.out.println("ParseUser objId========"+ParseUser.getCurrentUser().getObjectId());
		 System.out.println("ParseUser========"+cUser.getCount());
		if (cUser.moveToFirst()) {
			System.out.println("cUser=is not null=======" + cUser.getCount());
            if(!(cUser.getString(cUser.getColumnIndex(LocalDatabaseHandler.USER_NAME)).equals("") )&& cUser.getString(cUser.getColumnIndex(LocalDatabaseHandler.USER_NAME))!=null)

          {
	            nameTextView.setText(cUser.getString(cUser.getColumnIndex(LocalDatabaseHandler.USER_NAME)));
           }
				shape.setText(cUser.getString(cUser.getColumnIndex(LocalDatabaseHandler.SHAPE)));

				bust.setText(cUser.getString(cUser.getColumnIndex(LocalDatabaseHandler.BUST)));
				waist.setText(cUser.getString(cUser.getColumnIndex(LocalDatabaseHandler.WAIST)));
				hips.setText(cUser.getString(cUser.getColumnIndex(LocalDatabaseHandler.HIPS)));

				Utils.write("parse user OCCUPATION===" + cUser.getString(cUser.getColumnIndex(LocalDatabaseHandler.OCCUPATION)));
				occupationEditText.setText(cUser.getString(cUser.getColumnIndex(LocalDatabaseHandler.OCCUPATION)));

				birthday.setText(cUser.getString(cUser.getColumnIndex(LocalDatabaseHandler.BIRTH_DATE)));
				height.setText(cUser.getString(cUser.getColumnIndex(LocalDatabaseHandler.HEIGHT)));
				weight.setText(cUser.getString(cUser.getColumnIndex(LocalDatabaseHandler.WEIGHT)));
				bottom_waist_size
						.setText(cUser.getString(cUser.getColumnIndex(LocalDatabaseHandler.BOTTOM_WAIST_SIZE)));
				characteristics.setText(cUser.getString(cUser.getColumnIndex(LocalDatabaseHandler.CHARACTERISTIC)));
				lovingColor.setText(cUser.getString(cUser.getColumnIndex(LocalDatabaseHandler.LOVE_COLOR)));
				prefBrand.setText(cUser.getString(cUser.getColumnIndex(LocalDatabaseHandler.PREF_BRAND)));
				styleIcon.setText(cUser.getString(cUser.getColumnIndex(LocalDatabaseHandler.STYLE_ICON)));
				outfits.setText(cUser.getString(cUser.getColumnIndex(LocalDatabaseHandler.OUTFITS)));
				about_yourself.setText(cUser.getString(cUser.getColumnIndex(LocalDatabaseHandler.ABOUT_YOURSELF)));

			Utils.write("style===="+cUser.getString(cUser.getColumnIndex(LocalDatabaseHandler.STYLE)));
			style.setText(cUser.getString(cUser.getColumnIndex(LocalDatabaseHandler.STYLE)));
				shape.setText(cUser.getString(cUser.getColumnIndex(LocalDatabaseHandler.SHAPE)));

				selectedSape = cUser.getString(cUser.getColumnIndex(LocalDatabaseHandler.SHAPE));
				selectedStyle = cUser.getString(cUser.getColumnIndex(LocalDatabaseHandler.STYLE));
				//styleDescrip.setText(cUser.getString(cUser.getColumnIndex(LocalDatabaseHandler.STYLE_DESCRIPTION)));

				switch (cUser.getString(cUser.getColumnIndex(LocalDatabaseHandler.SIZE))) {
				case "XS":
					extraSmall.setChecked(true);
					break;

				case "S":
					small.setChecked(true);
					break;

				case "L":
					larg.setChecked(true);
					break;
				case "M":
					medium.setChecked(true);
					break;

				case "XL":
					extraLarg.setChecked(true);
					break;

				case "XXL":
					doubleLarg.setChecked(true);
					break;

				default:
					System.out.println("deafult    >>>>>>>>>>>>");
					medium.setChecked(true);
					break;
				}

				switch (cUser.getString(cUser.getColumnIndex(LocalDatabaseHandler.STYLE_DESCRIPTION))) {
				case "Trendy":
					setCheckedRafiobox(radio6);
					break;

				case "Eclectic":
					setCheckedRafiobox(radio5);
					break;

				case "Sexy":
					setCheckedRafiobox(radio4);
					break;

				case "Masculine":
					setCheckedRafiobox(radio3);
					break;

				case "Casual":
					setCheckedRafiobox(radio2);
					break;

				case "Classic":
					setCheckedRafiobox(radio1);
					break;

				default:
					setCheckedRafiobox(radio1);
					break;
				}

				System.out.println(">>>>>>>>>>   cccccccccc  "
						+ cUser.getString(cUser.getColumnIndex(LocalDatabaseHandler.FASHION_TYPE)));
				switch (cUser.getString(cUser.getColumnIndex(LocalDatabaseHandler.FASHION_TYPE))) {
				case "c":
					setCheckedRafiobox(radioC);
					break;

				case "e":
					setCheckedRafiobox(radioE);
					break;

				case "f":
					setCheckedRafiobox(radioF);
					break;

				default:
					setCheckedRafiobox(radioC);
					break;
				}

				skinComplexion.setText(cUser.getString(cUser.getColumnIndex(LocalDatabaseHandler.SKIN_COMPLEXION)));
				System.out
						.println("parse user PROFILE_PIC path    " + cUser.getString(cUser.getColumnIndex(LocalDatabaseHandler.PROFILE_PIC)));
				if ((cUser.getString(cUser.getColumnIndex(LocalDatabaseHandler.PROFILE_PIC)) != null) && !(cUser.getString(cUser.getColumnIndex(LocalDatabaseHandler.PROFILE_PIC)).equals(""))) {
					profile_photo.setImageBitmap(BitmapFactory
							.decodeFile(cUser.getString(cUser.getColumnIndex(LocalDatabaseHandler.PROFILE_PIC))));

					showImagePath = cUser.getString(cUser.getColumnIndex(LocalDatabaseHandler.PROFILE_PIC));
				} else {
					profile_photo.setImageResource(R.drawable.img);
					System.out.println("parse user deffffffffffff=======" + showImagePath);
				}


		} else {
			System.out.println("cUser is null========"+cUser.getCount());
			try {
//				if (ParseUser.getCurrentUser().getDate("BirthDate") != null)
//					birthday.setText(
//							sdf.parseObject(parseFormat.format(ParseUser.getCurrentUser().getString("BirthDate")))
//									.toString());
//				if (getPrefrence("username") != null && !getPrefrence("username").equals("")) {
//					nameTextView.setText(getPrefrence("username"));
//				}
//				if (getPrefrence("profilePath") != null && getPrefrence("profilePath") != "") {
//					profile_photo.setImageBitmap(BitmapFactory.decodeFile(getPrefrence("profilePath")));
//				}
			} catch (Exception e) {
				e.printStackTrace();
			}
			System.out.println("in onr else" + ParseUser.getCurrentUser().getUsername());
			medium.setChecked(true);
			setCheckedRafiobox(radio1);
			setCheckedRafiobox(radioC);
		}
	}

	private void setCheckedRafiobox(RadioButton b) {
		b.setChecked(true);
		b.setTextColor(getResources().getColor(R.color.redish_text_color));
	}

	protected void onPause() {
		super.onPause();
		// handler1.close();
	};

	// Check Validation for mandetory fileds

	private boolean isValidFields() {

//		if (nameTextView.getText().toString().trim().isEmpty()) {
//			showToastS("Please enter your name.");
//			return false;
//		}
		if (occupationEditText.getText().toString().trim().isEmpty()) {
			showToastS("Please enter profession/occupation.");
			occupationEditText.requestFocus();
			return false;
		}
		else if (birthday.getText().toString().trim().isEmpty()) {
			showToastS("Please select your birth date.");
			birthday.requestFocus();
			return false;
		}
		/*
		 * if (bodyStatsEditText.getText().toString().trim().isEmpty()) {
		 * showToastS("Please enter your body stats.");
		 * bodyStatsEditText.requestFocus(); return false; }
		 */
		else if (skinComplexion.getText().toString().trim().isEmpty()) {
			showToastS("Please enter skin complexion.");
			skinComplexion.requestFocus();
			return false;
		}
		/*
		 * if (characteristics.getText().toString().trim().isEmpty()) {
		 * showToastS("Please enter personality traits/ characteristics.");
		 * characteristics.requestFocus();
		 * 
		 * return false; }
		 */
		else if (prefBrand.getText().toString().trim().isEmpty()) {
			showToastS("Please enter your preferred brand.");
			prefBrand.requestFocus();

			return false;
		}
		else if (style.getText().toString().trim().isEmpty()) {
			showToastS("Please select your style.");
			style.requestFocus();

			return false;
		}
		else if (bust.getText().toString().trim().isEmpty()) {
			showToastS("Please fill size of bust.");
			bust.requestFocus();

			return false;
		}
		else if (waist.getText().toString().trim().isEmpty()) {
			showToastS("Please fill size of waist.");
			waist.requestFocus();

			return false;
		}
		else if (hips.getText().toString().trim().isEmpty()) {
			showToastS("Please fill size of hip.");
			hips.requestFocus();

			return false;
		}
		return true;
	}

	private void weightDialog() {
		LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		View viewList = inflater.inflate(R.layout.weight_dialog, null);
		final Dialog dialogMarketList = new Dialog(this);
		dialogMarketList.requestWindowFeature(Window.FEATURE_NO_TITLE);
		dialogMarketList.setContentView(viewList);

		dialogMarketList.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
		final TextView weight1 = (TextView) viewList.findViewById(R.id.weight);
		final TextView ok = (TextView) viewList.findViewById(R.id.ok);
		final TextView cancel = (TextView) viewList.findViewById(R.id.cancel);
		ListView lvForDialog = (ListView) viewList.findViewById(R.id.listView_dialog);
		Value_Adapter_Dialog_Array valueAdapter = new Value_Adapter_Dialog_Array(context, weightType);
		lvForDialog.setAdapter(valueAdapter);

		lvForDialog.setOnItemClickListener(new AdapterView.OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
				// TODO Auto-generated method stub
				// dialogMarketList.cancel();
				weight1.setText(weightType.get(arg2) + " Kg");
			}
		});

		ok.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				weight.setText(weight1.getText().toString());
				bottom_waist_size.requestFocus();
				dialogMarketList.cancel();

			}
		});
		cancel.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				dialogMarketList.cancel();
			}
		});
		dialogMarketList.show();
	}

	private void heightDialog() {
		LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		View viewList = inflater.inflate(R.layout.spinner_show_dialog, null);
		final Dialog dialogMarketList = new Dialog(this);
		dialogMarketList.requestWindowFeature(Window.FEATURE_NO_TITLE);
		dialogMarketList.setContentView(viewList);

		dialogMarketList.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
		final TextView feet = (TextView) viewList.findViewById(R.id.feet);
		final TextView inches = (TextView) viewList.findViewById(R.id.inch);
		final TextView ok = (TextView) viewList.findViewById(R.id.ok);
		final TextView cancel = (TextView) viewList.findViewById(R.id.cancel);
		ListView lvForDialog = (ListView) viewList.findViewById(R.id.listView_dialog);
		Value_Adapter_Dialog_Array valueAdapter = new Value_Adapter_Dialog_Array(context, heightType);
		lvForDialog.setAdapter(valueAdapter);

		lvForDialog.setOnItemClickListener(new AdapterView.OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
				// TODO Auto-generated method stub
				// dialogMarketList.cancel();
				feet.setText(heightType.get(arg2) + " feet");
			}
		});

		ListView lvForDialog1 = (ListView) viewList.findViewById(R.id.listView_dialog1);
		Value_Adapter_Dialog_Array valueAdapter1 = new Value_Adapter_Dialog_Array(context, heightType1);
		lvForDialog1.setAdapter(valueAdapter1);

		lvForDialog1.setOnItemClickListener(new AdapterView.OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
				// TODO Auto-generated method stub
				inches.setText(heightType1.get(arg2) + " inches");
			}
		});

		ok.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				height.setText(feet.getText().toString() + " " + inches.getText().toString());
				weightDialog();
				dialogMarketList.cancel();

			}
		});
		cancel.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				dialogMarketList.cancel();
			}
		});
		dialogMarketList.show();
	}

	class Value_Adapter_Dialog_Array extends BaseAdapter {
		// private HashMap<String, String> mStringList;
		// private ArrayList<JSONObject> mStringFilterList;
		private LayoutInflater mInflater;
		ArrayList<String> dataRef;

		// private ValueFilter valueFilter;
		public Value_Adapter_Dialog_Array(Context context, ArrayList<String> data) {
			// this.mStringFilterList = mStringList;
			mInflater = LayoutInflater.from(context);
			// getFilter();
			dataRef = data;
		}

		// How many items are in the data set represented by this Adapter.
		@Override
		public int getCount() {
			return dataRef.size();
		}

		// Get the data item associated with the specified position in the data
		// set.
		@Override
		public Object getItem(int position) {
			return dataRef.get(position);
		}

		// Get the row id associated with the specified position in the list.
		@Override
		public long getItemId(int position) {
			return position;
		}

		// Get a View that displays the data at the specified position in the
		// data
		// set.
		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			Holder viewHolder;
			if (convertView == null) {
				viewHolder = new Holder();
				convertView = mInflater.inflate(R.layout.spinner_item_view, null);
				viewHolder.tvSpinnerItem = (TextView) convertView.findViewById(R.id.tvSpinnerItem);
				convertView.setTag(viewHolder);
			} else {
				viewHolder = (Holder) convertView.getTag();
			}
			viewHolder.tvSpinnerItem.setText(dataRef.get(position));
			return convertView;
		}

		class Holder {
			TextView tvSpinnerItem;
		}
	}

	
	
	
	
	private void saveUserinDatabase(String showImagePath)
	{
		try {
			System.out.println("isvalid fields=============="+isValidFields());
			if (isValidFields()) {
				Log.d("ok", "666 validate");
				SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
				Cursor cUser = dbHelper.getUser(ParseUser.getCurrentUser().getObjectId());
                     System.out.println("count============" + cUser.getCount());
				int updateId = -1;
					Log.d("ok", "777 move first");

					//Boolean iscUserDel = dbHelper.deleteUser(ParseUser.getCurrentUser().getObjectId());
					//System.out.println("cUserDel=======" + iscUserDel);
Utils.write("parse user showImagePath=="+showImagePath);
if(checkNetworkConnection()) {
	if (cUser.moveToFirst()) {

	updateId = dbHelper.updateUserProfile(ParseUser.getCurrentUser().getObjectId(), nameTextView.getText().toString(),
			showImagePath, "male", occupationEditText.getText().toString(),
			birthday.getText().toString(),
			// bodyStatsEditText.getText().toString(),
			bust.getText().toString(), waist.getText().toString(), hips.getText().toString(),
			height.getText().toString(), weight.getText().toString(), size.getText().toString(),
			bottom_waist_size.getText().toString(), skinComplexion.getText().toString(),
			characteristics.getText().toString(), lovingColor.getText().toString(),
			prefBrand.getText().toString(), /* style_icon */styleIcon.getText().toString(),
			fashion_type, outfits.getText().toString(),
			your_style , about_yourself.getText().toString(), style.getText().toString(),
			shape.getText().toString(), format.format(new Date()),"true");

		Utils.write("shape===="+shape.getText().toString());


	Log.v("PROFILE_UPDATE", updateId+"");
		System.out.println("showImgPath update database=======" + showImagePath);
		setPrefrence("profilePath", showImagePath);
} else {

	Log.d("ok", "777");

	System.out.println("in else");

	dbHelper.insertUser(ParseUser.getCurrentUser().getObjectId(), nameTextView.getText().toString(),
			showImagePath, "male", occupationEditText.getText().toString(),
			birthday.getText().toString(),
							/* bodyStatsEditText.getText().toString() */bust.getText().toString(),
			waist.getText().toString(), hips.getText().toString(), height.getText().toString(),
			weight.getText().toString(), size.getText().toString(),
			bottom_waist_size.getText().toString(), skinComplexion.getText().toString(),
			characteristics.getText().toString(), lovingColor.getText().toString(),
			prefBrand.getText().toString(), styleIcon.getText().toString(), fashion_type,
			outfits.getText().toString(),
			your_style /*
										 * styleDescrip . getText ()
										 * .toString ()
										 */, about_yourself.getText().toString(), style.getText().toString(),
			shape.getText().toString(), format.format(new Date()).toString(), "true");
		System.out.println("showImgPath insert database=======" + showImagePath);
		setPrefrence("profilePath", showImagePath);
}
	saveUserProfile(ParseUser.getCurrentUser().getObjectId(), nameTextView.getText().toString(),
			showImagePath, "male", occupationEditText.getText().toString(),
			birthday.getText().toString(),
			// bodyStatsEditText.getText().toString(),
			bust.getText().toString(), waist.getText().toString(), hips.getText().toString(),
			height.getText().toString(), weight.getText().toString(), size.getText().toString(),
			bottom_waist_size.getText().toString(), skinComplexion.getText().toString(),
			characteristics.getText().toString(), lovingColor.getText().toString(),
			prefBrand.getText().toString(), /* style_icon */styleIcon.getText().toString(),
			fashion_type, outfits.getText().toString(),
			your_style, about_yourself.getText().toString(), style.getText().toString(), shape.getText().toString(), format.format(new Date()));
	setPrefrence("profilePath", showImagePath);
}
				else
{
	setPrefrenceBool(Constants.isUpdated,true);
	if (cUser.moveToFirst()) {



		updateId = dbHelper.updateUserProfile(ParseUser.getCurrentUser().getObjectId(), nameTextView.getText().toString(),
				showImagePath, "male", occupationEditText.getText().toString(),
				birthday.getText().toString(),
				// bodyStatsEditText.getText().toString(),
				bust.getText().toString(), waist.getText().toString(), hips.getText().toString(),
				height.getText().toString(), weight.getText().toString(), size.getText().toString(),
				bottom_waist_size.getText().toString(), skinComplexion.getText().toString(),
				characteristics.getText().toString(), lovingColor.getText().toString(),
				prefBrand.getText().toString(), /* style_icon */styleIcon.getText().toString(),
				fashion_type, outfits.getText().toString(),
				your_style , about_yourself.getText().toString(), style.getText().toString(),
				shape.getText().toString(), format.format(new Date()),"false");
		Log.v("PROFILE_UPDATE", updateId+"");
		System.out.println("showImgPath net not avail updateupdate database=======" + showImagePath);
		setPrefrence("profilePath", showImagePath);

	} else {

		Log.d("ok", "777");

		System.out.println("in else");

		dbHelper.insertUser(ParseUser.getCurrentUser().getObjectId(), nameTextView.getText().toString(),
				showImagePath, "male", occupationEditText.getText().toString(),
				birthday.getText().toString(),
							/* bodyStatsEditText.getText().toString() */bust.getText().toString(),
				waist.getText().toString(), hips.getText().toString(), height.getText().toString(),
				weight.getText().toString(), size.getText().toString(),
				bottom_waist_size.getText().toString(), skinComplexion.getText().toString(),
				characteristics.getText().toString(), lovingColor.getText().toString(),
				prefBrand.getText().toString(), styleIcon.getText().toString(), fashion_type,
				outfits.getText().toString(),
				your_style /*
										 * styleDescrip . getText ()
										 * .toString ()
										 */, about_yourself.getText().toString(), style.getText().toString(),
				shape.getText().toString(), format.format(new Date()).toString(), "false");
		System.out.println("showImgPath net not avail inser database=======" + showImagePath);
		setPrefrence("profilePath", showImagePath);
	}
}
//				setPrefrence("username", nameTextView.getText().toString());
//				setPrefrence("profilePath", showImagePath);
			//	saveOnParse();


				showToastL("Profile has been updated.");
                finish();

//				if(updateId != -1){
//
//					finish();
//				}else{
//
//					Log.v("PROFILE_UPDATE", "fail to update");
//
//					//fail to update
//				}


				}
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			Utils.write("excep6");
			Log.d("ok", "777 "+e.getMessage());
		}
}


	private void saveUserProfile(String user_id, String user_name, String profile_pic_path, String gender, String occupation,
								 String birth_date, String bust, String waist, String hips, String height, String weight,
								 String size, String bottom_waist_size, String skin_coplexion, String characteristics, String love_color,
								 String pref_brand, String style_icon, String fashin_type, String outfits, String style_desription,
								 String about_yourself, String style, String shape, String created_at) {
		System.out.println("call to saveUserProfile");
		try {
				ParseUser parseUser = ParseUser.getCurrentUser();
				parseUser.setUsername(user_name);
				parseUser.put("Gender", gender);
				parseUser.put("Bust", bust);
				parseUser.put("Waist", waist);
				parseUser.put("Hips", hips);
				parseUser.put("Shape", shape);
				parseUser.put("Height", height);
				parseUser.put("Weight", weight);
				parseUser.put("Size", size);
			    parseUser.put("Style", style);
			    parseUser.put("Waist", style);
				parseUser.put("BottomWaistSize", bottom_waist_size);
				parseUser.put("SkinComplexion", skin_coplexion);
				parseUser.put("Characteristics",characteristics);
				parseUser.put("ColorPrefered", love_color);
				parseUser.put("BrandPrefered", pref_brand);
				parseUser.put("StyleIcon", style_icon);
				parseUser.put("FashionType", fashin_type);
				parseUser.put("Outfits", outfits);
				parseUser.put("StyleDescription",
						style_desription);
				parseUser.put("AboutYourself", about_yourself);

				if (profile_pic_path != null) {
					ByteArrayOutputStream stream = new ByteArrayOutputStream();
					BitmapFactory.decodeFile(profile_pic_path)
							.compress(Bitmap.CompressFormat.PNG, 100, stream);
					ParseFile file = new ParseFile("image.png", stream.toByteArray());
					parseUser.put("ProfilePicture", file);
				}
				parseUser.put("occupation",occupation);
				parseUser.saveInBackground();


			}
		catch (Exception e)
		{
			e.printStackTrace();
		}



}


	private void viewProfileDialog() {
		try {
			System.out.println("in viewProfile====" + showImagePath);
			final Dialog viewProfile = addDialog(R.layout.view_profile);
			final ImageView imageViewProfile = (ImageView) viewProfile.findViewById(R.id.imageViewProfile);
			final ImageView imageViewCross = (ImageView) viewProfile.findViewById(R.id.imageViewCross);
			try {
				if (showImagePath != null && !showImagePath.equals("")) {
					bitmap = Utils.orientedBitmap(showImagePath);
					imageViewProfile.setImageBitmap(bitmap);
				} else {
					imageViewProfile.setImageResource(R.drawable.img);

				}
			} catch (Exception e) {
				// TODO: handle exception
				e.printStackTrace();
			}
			imageViewCross.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					viewProfile.dismiss();
				}
			});
			viewProfile.show();

		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();}
	}
	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		
		if(dbHelper != null){
			
			dbHelper.close();
		}
	}


	//round image
	public static Bitmap getRoundedCornerBitmap(Bitmap bitmap, int pixels) {
		Bitmap output = Bitmap.createBitmap(bitmap.getWidth(), bitmap.getHeight(), Bitmap.Config.ARGB_8888);
		Canvas canvas = new Canvas(output);

		final int color = 0xff424242;
		final Paint paint = new Paint();
		final Rect rect = new Rect(0, 0, bitmap.getWidth(), bitmap.getHeight());
		final RectF rectF = new RectF(rect);
		final float roundPx = pixels;

		paint.setAntiAlias(true);
		canvas.drawARGB(0, 0, 0, 0);
		paint.setColor(color);
		canvas.drawRoundRect(rectF, roundPx, roundPx, paint);

		paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
		canvas.drawBitmap(bitmap, rect, rect, paint);

		return output;
	}




	//get image path
	@TargetApi(Build.VERSION_CODES.KITKAT)
	public static String getPath(final Context context, final Uri uri) {

		final boolean isKitKat = Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT;

		// DocumentProvider
		if (isKitKat && DocumentsContract.isDocumentUri(context, uri)) {
			// ExternalStorageProvider
			if (isExternalStorageDocument(uri)) {
				final String docId = DocumentsContract.getDocumentId(uri);
				final String[] split = docId.split(":");
				final String type = split[0];

				if ("primary".equalsIgnoreCase(type)) {
					return Environment.getExternalStorageDirectory() + "/" + split[1];
				}

				// TODO handle non-primary volumes
			}
			// DownloadsProvider
			else if (isDownloadsDocument(uri)) {

				final String id = DocumentsContract.getDocumentId(uri);
				final Uri contentUri = ContentUris.withAppendedId(
						Uri.parse("content://downloads/public_downloads"), Long.valueOf(id));

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
				final String[] selectionArgs = new String[] {
						split[1]
				};

				return getDataColumn(context, contentUri, selection, selectionArgs);
			}
		}
		// MediaStore (and general)
		else if ("content".equalsIgnoreCase(uri.getScheme())) {

			// Return the remote address
			if (isGooglePhotosUri(uri))
				return uri.getLastPathSegment();

			return getDataColumn(context, uri, null, null);
		}
		// File
		else if ("file".equalsIgnoreCase(uri.getScheme())) {
			return uri.getPath();
		}

		return null;
	}



	/**
	 * Get the value of the data column for this Uri. This is useful for
	 * MediaStore Uris, and other file-based ContentProviders.
	 *
	 * @param context The context.
	 * @param uri The Uri to query.
	 * @param selection (Optional) Filter used in the query.
	 * @param selectionArgs (Optional) Selection arguments used in the query.
	 * @return The value of the _data column, which is typically a file path.
	 */
	public static String getDataColumn(Context context, Uri uri, String selection,
									   String[] selectionArgs) {

		Cursor cursor = null;
		final String column = "_data";
		final String[] projection = {
				column
		};

		try {
			cursor = context.getContentResolver().query(uri, projection, selection, selectionArgs,
					null);
			if (cursor != null && cursor.moveToFirst()) {
				final int index = cursor.getColumnIndexOrThrow(column);
				return cursor.getString(index);
			}
		} finally {
			if (cursor != null)
				cursor.close();
		}
		return null;
	}


	public static boolean isMediaDocument(Uri uri) {
		return "com.android.providers.media.documents".equals(uri.getAuthority());
	}

	public static boolean isGooglePhotosUri(Uri uri) {
		return "com.google.android.apps.photos.content".equals(uri.getAuthority());
	}


	/**
	 * @param uri The Uri to check.
	 * @return Whether the Uri authority is ExternalStorageProvider.
	 */
	public static boolean isExternalStorageDocument(Uri uri) {
		return "com.android.externalstorage.documents".equals(uri.getAuthority());
	}

	/**
	 * @param uri The Uri to check.
	 * @return Whether the Uri authority is DownloadsProvider.
	 */
	public static boolean isDownloadsDocument(Uri uri) {
		return "com.android.providers.downloads.documents".equals(uri.getAuthority());
	}



}
