package com.stylist;

import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

import net.simonvt.menudrawer.MenuDrawer;
import net.simonvt.menudrawer.Position;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.constants.BaseActivity;
import com.constants.Constants;
import com.constants.LocalDatabaseHandler;
import com.constants.Utils;

import com.parse.ParseAnalytics;
import com.parse.ParseUser;
import com.stylist.askstylist.AskStylistActivity;
import com.stylist.askstylist.AskStylistRequestListActivity;
import com.stylist.askstylist.MyRequestActivity;
import com.stylist.lookbook.LookBookActivity;
import com.stylist.lookbook.LookBookItemActivity;
import com.stylist.lookbook.LooksByStylistActivity;
import com.stylist.lookbook.UploadFromLookbook;
import com.stylist.lookbook.UploadLookBook;
import com.stylist.planner.AddLook;
import com.stylist.planner.PlannerActivity;
import com.stylist.planner.PlannerDetailItem;
import com.stylist.planner.PlannerDetailList;
import com.stylist.rssfeed.AndroidRSSReaderApplicationActivity;
import com.stylist.rssfeed.DisPlayWebPageActivity;
import com.stylist.rssfeed.ListRSSItemsActivity;
import com.stylist.styleeditor.BrowseFromWarMain;
import com.stylist.styleeditor.BrowseFromWishListActivity;
import com.stylist.styleeditor.ColourPickerActivity;
import com.stylist.styleeditor.SelectWardrobeCategoryDetail;
import com.stylist.styleeditor.SelectWishListItemActivity;
import com.stylist.styleeditor.StyleEditorActivity;
import com.stylist.wardrobe.CategoryDetailActivity;
import com.stylist.wardrobe.ItemDetailActivity;
import com.stylist.wardrobe.MyClosetActivity;
import com.stylist.wardrobe.UploadOptionWardrobeActivity;
import com.stylist.wardrobe.UploadWardribeActivity;
import com.stylist.wardrobe.UploadWardrobeWithDescriptionPop;
import com.stylist.wishlist.UploadOptionWishListActivity;
import com.stylist.wishlist.UploadWishListActivity;
import com.stylist.wishlist.UploadWishListDescriptionActivity;
import com.stylist.wishlist.WishListActivity;
import com.stylist.wishlist.WishListItemActivity;
import com.stylist.wishlist.WishListItemDetail;

public class HomeActivity extends BaseActivity {

	LinearLayout myClosetLinearLayout, styleEditorLinearLayout, lookbookLinearLayout, trendingLinearLayout,
			myprofileLinearLayout, wishlistLinearLayout, stylistLinearLayout, plannLinearLayout;
	int logoutClicked = 1;
	ImageView imageViewSettingst, nextOrDel;
	com.constants.CustomTextView headerName;
	ArrayList<Integer> backGroundImgIds = new ArrayList<Integer>();
	ArrayList<String> titles = new ArrayList<String>();
	ArrayList<Integer> imgIds = new ArrayList<Integer>();
	int m = 0;
	LinearLayout nextOrDelButton;
	SettingsActivity mSettingsActivity;
	public static HomeActivity act;;
	MenuDrawer settingMenu;
	String CurrentUserId;
    LocalDatabaseHandler dbHelper;
	Bitmap profBitmap;
	public void onStart() {
		super.onStart();
		Constants.isHomeOpened = true;
		Utils.write("working====1");
		//.onStartSession(this, getResources().getString(R.string.flurryKey));
		closeAllactivity();
	}


	//public static Bitmap proImage = null;
	public static String userName = null;

	private Bitmap getUserProfilePic()
	{
		if(dbHelper != null){

			Cursor cursor = dbHelper.getUser(ParseUser.getCurrentUser().getObjectId());

			if(cursor != null && cursor.getCount()>0){
				Log.v("IMAGE_PATH", ""+cursor.getCount());

				cursor.moveToFirst();
				String imagePath = cursor.getString(cursor.getColumnIndex(LocalDatabaseHandler.PROFILE_PIC));
				userName= cursor.getString(cursor.getColumnIndex(LocalDatabaseHandler.USER_NAME));
                Utils.write("userName==="+userName);
				if(imagePath != null  && !imagePath.equals("")){

					Log.v("IMAGE_PATH", imagePath);
					Bitmap b  = Utils.orientedBitmap(imagePath);
					if(b!=null) {
						float dx = (float) b.getHeight() / b.getWidth();

						b = Utils.getResizedBitmap(b, 200, (int) (200 * dx));

						//circle image
						b = ProfileActivity.getRoundedCornerBitmap(b, 140);

						//proImage = b;
//
						System.out.println("proImageCapture===bitmap====" + b);
						//System.out.println("proImageCapture===bitmap====" + proImage);
						//profile_photo.setImageBitmap(b);
						return b;
					}
				}else{


					return  null;
				}


			}else{
				Log.v("CURSOR", "NULL");

			}

		}else{

			Log.v("DB_HELPER", "NULL");
		}
		return null;
	}
	/*public void onStop() {
		super.onStop();
		Constants.isHomeOpened = false;
		Utils.write("working====2");
		//.onEndSession(this);

	}*/

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		Log.v("232323", "Resume home");

		try{

			mSettingsActivity = null;

			System.out.println("resume     mSettingsActivity " + mSettingsActivity);
			if (settingMenu != null && settingMenu.getMenuView() != null && mSettingsActivity == null) {

				Bitmap image = getIntent().getParcelableExtra("KEY_PROFILE_IMAGE");

				mSettingsActivity = new SettingsActivity(context,settingMenu,profBitmap,image);


				if(image!=null)
					mSettingsActivity.setProfileImage(image);


			}

		}catch(Error e){

			e.printStackTrace();
		}

		catch(Exception e){

			e.printStackTrace();

		}

}



	/*
	try{


		}catch(Error e){

e.printStackTrace();

		}

		catch(Exception e){

e.printStackTrace();

		}
	 */


	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_home);

		try{

			ParseAnalytics.trackAppOpened(getIntent());

			settingMenu = MenuDrawer.attach(this, MenuDrawer.Type.BEHIND, Position.LEFT, MenuDrawer.MENU_DRAG_WINDOW);
			act = this;
			settingMenu.setContentView(R.layout.activity_home);
			settingMenu.setMenuView(R.layout.activity_settings);
			CurrentUserId = ParseUser.getCurrentUser().getObjectId();
			titles.add("My Closet");
			titles.add("Style Editor");
			titles.add("Look Book");
			titles.add("Trending");
			titles.add("Planner");
			titles.add("Wishlist");
			titles.add("Profile");
			titles.add("Ask Stylist");
			dbHelper=new LocalDatabaseHandler(this);
			nextOrDelButton = (LinearLayout) findViewById(R.id.nextOrDelButton);
			nextOrDel = (ImageView) findViewById(R.id.nextOrDel);
			nextOrDelButton.setVisibility(View.VISIBLE);
			nextOrDel.setImageResource(R.drawable.logout_btn);
			backGroundImgIds.add(R.drawable.no_image);
			backGroundImgIds.add(R.drawable.no_image);
			backGroundImgIds.add(R.drawable.no_image);
			backGroundImgIds.add(R.drawable.no_image);
			backGroundImgIds.add(R.drawable.no_image);
			backGroundImgIds.add(R.drawable.no_image);
			backGroundImgIds.add(R.drawable.no_image);
			backGroundImgIds.add(R.drawable.no_image);

			imgIds.add(R.drawable.back28);
			imgIds.add(R.drawable.back28);
			imgIds.add(R.drawable.back28);
			imgIds.add(R.drawable.back28);
			imgIds.add(R.drawable.back28);
			imgIds.add(R.drawable.back28);
			imgIds.add(R.drawable.back28);
			imgIds.add(R.drawable.back28);

			myClosetLinearLayout = (LinearLayout) findViewById(R.id.myClosetLinearLayout);
			styleEditorLinearLayout = (LinearLayout) findViewById(R.id.styleEditorLinearLayout);
			lookbookLinearLayout = (LinearLayout) findViewById(R.id.lookbookLinearLayout);
			trendingLinearLayout = (LinearLayout) findViewById(R.id.trendingLinearLayout);
			myprofileLinearLayout = (LinearLayout) findViewById(R.id.myprofileLinearLayout);
			wishlistLinearLayout = (LinearLayout) findViewById(R.id.wishlistLinearLayout);
			stylistLinearLayout = (LinearLayout) findViewById(R.id.stylistLinearLayout);
			plannLinearLayout = (LinearLayout) findViewById(R.id.plannLinearLayout);

			imageViewSettingst = (ImageView) findViewById(R.id.backButtonImage);
			headerName = (com.constants.CustomTextView) findViewById(R.id.headerName);
			headerName.setText("HOME");
			imageViewSettingst.setImageResource(R.drawable.menu);

			nextOrDelButton.setOnClickListener(this);
			imageViewSettingst.setOnClickListener(this);
			myClosetLinearLayout.setOnClickListener(this);
			styleEditorLinearLayout.setOnClickListener(this);
			lookbookLinearLayout.setOnClickListener(this);
			trendingLinearLayout.setOnClickListener(this);
			myprofileLinearLayout.setOnClickListener(this);
			wishlistLinearLayout.setOnClickListener(this);
			stylistLinearLayout.setOnClickListener(this);
			plannLinearLayout.setOnClickListener(this);

			//syncServiceWardAndUser();
			profBitmap=getUserProfilePic();
			//Utils.write("profBitmap==="+profBitmap);
			cancelAlarm();
			setAlarm();

		}catch (Error e){

			e.printStackTrace();
		}

		catch(Exception e){

			e.printStackTrace();

		}


	}
//
//private void showSyncDialog()
//{
//
//		Utils.createSimpleDialog(context, "Alert",
//				"You have some data to sync.Do you want to sync?",
//				"Sync", "Cancel", new Utils.Method() {
//					@Override
//					public void execute() {
//						// TODO Auto-generated method stub
//
//
//
//						if(checkNetworkConnection()) {
//							Log.v("Service", "Started");
//							startSyncservice(HomeActivity.this, 0);
//						}
//						else
//						{
//							showToastS("Network connection is not available!");
//						}
//
//					}
//				}, new Utils.Method() {
//					@Override
//					public void execute() {
//						// TODO Auto-generated method stub
//
//					}
//				});
//	}


//	private void syncServiceWardAndUser()
//	{
//		final Cursor cNew = dbHelper.returnNewCategory(ParseUser.getCurrentUser().getObjectId());
//      if(cNew.getCount()>0)
//      {
//		  showSyncDialog();
//		  Utils.write("cNew");
//      }
//		else
//	  {
//		  final Cursor updatedC = dbHelper.returnUpdatedCategory(ParseUser.getCurrentUser().getObjectId());
//           if(updatedC.getCount()>0)
//		   {
//			   showSyncDialog();
//			   Utils.write("updatedC");
//
//		   }
//		  else
//		   {
//			   final Cursor cDeleted = dbHelper.returnDeletedCategory(ParseUser.getCurrentUser().getObjectId());
//			   if(cDeleted.getCount()>0)
//			   {
//				   showSyncDialog();
//				   Utils.write("cDeleted");
//			   }
//			   else
//			   {
//				   final Cursor wNew = dbHelper.returnNewWardrobe(ParseUser.getCurrentUser().getObjectId());
//				   if(wNew.getCount()>0)
//				   {
//					   showSyncDialog();
//					   Utils.write("wNew");
//				   }
//				   else
//				   {
//					   final Cursor wUpdated = dbHelper.returnUpdatedWardrobe(ParseUser.getCurrentUser().getObjectId());
//
//					   if(wUpdated.getCount()>0)
//					   {
//						   showSyncDialog();
//						   Utils.write("wUpdated");
//					   }
//					   else
//					   {
//						   final Cursor wDeleted = dbHelper.returnDeletedWardrobe(ParseUser.getCurrentUser().getObjectId());
//						   if(wDeleted.getCount()>0)
//						   {
//							   showSyncDialog();
//							   Utils.write("wDeleted");
//						   }
//						   else
//						   {
//							   final Cursor cUserSync = dbHelper.getUser(ParseUser.getCurrentUser().getObjectId());
//							   if(cUserSync.moveToFirst())
//							   {
//								   String isUsersync=cUserSync.getString(cUserSync.getColumnIndex(LocalDatabaseHandler.IS_SYNCED));
//								   if(isUsersync.trim().equalsIgnoreCase("false"))
//								   {
//									   showSyncDialog();
//									   Utils.write("cUserSync");   }
//							   }
//						   }
//					   }
//				   }
//			   }
//		   }
//	  }
//	}

	private void setAlarm() {

		Intent intent = new Intent(getBaseContext(), MyBroadcastReceiver.class);
		intent.putExtra("fromService", false);
		intent.putExtra("fromQuery", false);
		intent.putExtra("fromHome", true);
		PendingIntent pendingIntent = PendingIntent.getBroadcast(getBaseContext(), 3, intent,
				PendingIntent.FLAG_UPDATE_CURRENT);
		AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
		alarmManager.set(AlarmManager.RTC_WAKEUP, System.currentTimeMillis() + (72 * 60 * 60 * 1000), pendingIntent);

	}

	private void cancelAlarm() {
		Intent intent = new Intent(getBaseContext(), MyBroadcastReceiver.class);
		intent.putExtra("fromService", false);
		intent.putExtra("fromHome", true);
		PendingIntent pendingIntent = PendingIntent.getBroadcast(getBaseContext(), 3, intent, 0);
		AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
		alarmManager.cancel(pendingIntent);

	}

	private void handleRightButtonClick() {
		boolean drawerOpen = settingMenu.isSelected();
		System.out.println("drawerOpen   " + drawerOpen);
		if (drawerOpen) {
			settingMenu.closeMenu();
		} else {
			settingMenu.openMenu();
		}
	}


	@Override
	public void onClick(View v) {
		switch (v.getId()) {

		case R.id.nextOrDelButton:
			logoutClicked = 1;

			logout();
			break;

		case R.id.backButtonImage:
			handleRightButtonClick();
			break;

		case R.id.myClosetLinearLayout:
			goToActivity(MyClosetActivity.class, null);
			break;

		case R.id.styleEditorLinearLayout:
			goToActivity(StyleEditorActivity.class, null);
			break;

		case R.id.lookbookLinearLayout:
			goToActivity(LookBookActivity.class, null);
			break;

		case R.id.trendingLinearLayout:
			goToActivity(TrendingActivity.class, null);
			break;

		case R.id.plannLinearLayout:
			goToActivity(PlannerActivity.class, null);
			break;

		case R.id.wishlistLinearLayout:
			goToActivity(WishListActivity.class, null);
			break;

		case R.id.stylistLinearLayout:
			goToActivity(AskStylistRequestListActivity.class, null);
			break;

		case R.id.myprofileLinearLayout:
			goToActivity(ProfileActivity.class, null);
			break;
		// case R.id.settingLinearLayout:
		// handleRightButtonClick();
		// break;

		default:
			break;
		}
	}

	boolean firstbackpresse = false;

	@Override
	public void onBackPressed() {
		if (settingMenu.isMenuVisible())
			settingMenu.closeMenu();
		else {
			// ExitAlert();

			System.out.println("????????????????????????   " + firstbackpresse);
			if (!firstbackpresse) {
				firstbackpresse = true;
				showToastS("Press again to exit.");
				Timer timer = new Timer();
				timer.schedule(new TimerTask() {

					public void run() {

						context.runOnUiThread(new Runnable() {
							public void run() {
								System.out.println("????????? afetr counter ???????????????   ");
								firstbackpresse = false;
								// ExitAlert();

							}
						});

					}

				}, 1500);
			} else {
				context.finish();
			}

		}

	}

	public class MyHomeItemAdapter extends BaseAdapter {
		private LayoutInflater mInflater;
		ArrayList<Integer> backGroundImgIds;
		ArrayList<String> titles;
		ArrayList<Integer> imgIds;

		public MyHomeItemAdapter(ArrayList<Integer> backGroundImgIds, ArrayList<String> titles,
				ArrayList<Integer> imgIds) {
			this.backGroundImgIds = backGroundImgIds;
			this.titles = titles;
			this.imgIds = imgIds;
		}

		@Override
		public int getCount() {
			return titles.size();
		}

		@Override
		public Object getItem(int position) {
			return titles.get(position);
		}

		@Override
		public long getItemId(int position) {
			return position;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			ViewHolder holder;
			if (convertView == null) {
				holder = new ViewHolder();
				convertView = View.inflate(context, R.layout.home_screen_item, null);
				holder.mytitle = (com.constants.CustomTextView) convertView.findViewById(R.id.mytitle);
				holder.backGrngImg = (ImageView) convertView.findViewById(R.id.backGrndImageView);
				holder.img = (ImageView) convertView.findViewById(R.id.frontImageView);
				convertView.setTag(holder);
			} else {
				holder = (ViewHolder) convertView.getTag();
			}
			holder.mytitle.setText(titles.get(position));
			if (m < 8) {
				holder.backGrngImg.setId(backGroundImgIds.get(m++));
				holder.img.setId(imgIds.get(m++));
			}
			return convertView;
		}

		private class ViewHolder {
			ImageView backGrngImg, img;
			com.constants.CustomTextView mytitle;
		}
	}

	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();

		stopService(new Intent(getBaseContext(), BackServiceForCloset.class));

		Log.v("Service", "Stop");

//		if (BackServiceForCloset.getInstance() != null)
//			BackServiceForCloset.getInstance().stopSelf();

		// Intent i = new Intent(context,BackServiceForCloset.class);
		// i.putExtra("userId", CurrentUserId);
		// startService(i);
	}

	
	@Override
	protected void onNewIntent(Intent intent) {
		// TODO Auto-generated method stub
		super.onNewIntent(intent);
		System.out.println("intent   " + intent);
		showToastS("new intent   " + intent);
	}

	private void closeAllactivity() {
		checkAndCloseActivity(AboutUs.aAboutUs);
		checkAndCloseActivity(BuyTokenActivity.buyTokenActivity);
		checkAndCloseActivity(CroppingActivity.cCroppingActivity);
		checkAndCloseActivity(LooksbyStylistDetailActivity.llooksbyStylistDetailActivity);
		checkAndCloseActivity(PaymentSuccessActivity.pPaymentSuccessActivity);
		//checkAndCloseActivity(PayUMoneyActivity.pPayUMoneyActivity);
		checkAndCloseActivity(ProfileActivity.pProfileActivity);
		checkAndCloseActivity(ReferAppActivity.rReferAppActivity);
		checkAndCloseActivity(Selector.sSelector);
		checkAndCloseActivity(SettingsActivity.sSettingsActivity);
		checkAndCloseActivity(ShareActivity.sShareActivity);
		checkAndCloseActivity(SyncDataActivity.sSyncDataActivity);
		checkAndCloseActivity(Transaction.tTransaction);
		checkAndCloseActivity(TrendingActivity.tTrendingActivity);
		checkAndCloseActivity(TrendingActivity.tTrendingActivity);
		checkAndCloseActivity(AskStylistActivity.aAskStylistActivity);
		checkAndCloseActivity(AskStylistRequestListActivity.aAskStylistRequestListActivity);
		checkAndCloseActivity(MyRequestActivity.mMyRequestActivity);
		checkAndCloseActivity(LookBookActivity.lLookBookActivity);
		checkAndCloseActivity(LookBookItemActivity.lLookBookItemActivity);
		checkAndCloseActivity(LooksByStylistActivity.lLooksByStylistActivity);
		checkAndCloseActivity(UploadFromLookbook.uUploadFromLookbook);
		checkAndCloseActivity(UploadLookBook.uUploadLookBook);
		checkAndCloseActivity(AddLook.aAddLook);
		checkAndCloseActivity(PlannerActivity.pPlannerActivity);
		checkAndCloseActivity(PlannerDetailItem.pPlannerDetailItem);
		checkAndCloseActivity(PlannerDetailList.pPlannerDetailList);
		checkAndCloseActivity(AndroidRSSReaderApplicationActivity.ctx);
		checkAndCloseActivity(DisPlayWebPageActivity.sDisPlayWebPageActivity);
		checkAndCloseActivity(ListRSSItemsActivity.ctx);
		checkAndCloseActivity(BrowseFromWarMain.ctx);
		checkAndCloseActivity(BrowseFromWishListActivity.ctx);
		checkAndCloseActivity(ColourPickerActivity.ctx);
		checkAndCloseActivity(SelectWardrobeCategoryDetail.ctx);
		checkAndCloseActivity(SelectWishListItemActivity.ctx);
		checkAndCloseActivity(StyleEditorActivity.act);
		checkAndCloseActivity(CategoryDetailActivity.act);
		checkAndCloseActivity(ItemDetailActivity.act);
		checkAndCloseActivity(MyClosetActivity.act);
		checkAndCloseActivity(UploadOptionWardrobeActivity.act);
		checkAndCloseActivity(UploadWardribeActivity.act);
		checkAndCloseActivity(UploadWardrobeWithDescriptionPop.act);
		checkAndCloseActivity(UploadOptionWishListActivity.act);
		checkAndCloseActivity(UploadWishListActivity.act);
		checkAndCloseActivity(UploadWishListDescriptionActivity.act);
		checkAndCloseActivity(WishListActivity.act);
		checkAndCloseActivity(WishListItemActivity.act);
		checkAndCloseActivity(WishListItemDetail.act);

	}
}
