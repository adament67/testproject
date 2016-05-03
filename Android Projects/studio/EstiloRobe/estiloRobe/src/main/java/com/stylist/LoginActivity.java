package com.stylist;

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URL;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;
import java.util.Calendar;
import java.util.List;
import java.util.Map;
import java.util.Random;

import org.json.JSONObject;

import com.constants.BaseActivity;
import com.constants.Constants;
import com.constants.GetDeviceTokenId;
import com.constants.GetDeviceTokenId.AsynctaskListner;
import com.constants.LocalDatabaseHandler;
import com.constants.Utils;
import com.facebook.AccessToken;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;

import com.parse.LogInCallback;
import com.parse.ParseException;
import com.parse.ParseFacebookUtils;
import com.parse.ParseFile;
import com.parse.ParseObject;
import com.parse.ParseUser;
import com.parse.SaveCallback;

import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.ContentValues;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class LoginActivity extends BaseActivity implements AsynctaskListner {
	protected static Activity act;
	EditText username;
	EditText password;
	String usernametxt;
	String passwordtxt;
	Button loginbutton;
	String msg = "";
	AsynctaskListner callbacklistner;
	TextView fbLogin, forgotPasTextView;
	private Dialog progressDialog;
	String profPicPath;
	LocalDatabaseHandler dbHelper;

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
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		act = this;
		callbacklistner = LoginActivity.this;
		setContentView(R.layout.activity_login);
		username = (EditText) findViewById(R.id.userNameEditText);
		password = (EditText) findViewById(R.id.passwordEditText);
		username.requestFocus();
		loginbutton = (Button) findViewById(R.id.loginButton);
		// signup = (Button) findViewById(R.id.signUpButton);
		fbLogin = (TextView) findViewById(R.id.loginFacebook);
		forgotPasTextView = (TextView) findViewById(R.id.forgotPasTextView);
		forgotPasTextView.setOnClickListener(this);
		loginbutton.setOnClickListener(this);
		fbLogin.setOnClickListener(this);
		// signup.setOnClickListener(this);
		Utils.setupUI(findViewById(R.id.linLayout1), this);
		dbHelper = new LocalDatabaseHandler(this);
	}

	private void validation() {
		if (username.getText().toString().trim().trim().equals("") && password.getText().toString().trim().trim().equals(""))
			msg = "Please Enter User Name and Password";
		// showToastS("Please Enter User Name and Password");
		else if (username.getText().toString().trim().trim().equals(""))
			msg = "Please Enter User Name";
		// showToastS("Please Enter User Name");
		else if (password.getText().toString().trim().trim().equals("") || password.getText().toString().trim().trim().length() < 6)
			msg = "Please Enter Password of minimum six character";
		// showToastS("Please Enter Password");
	}

	ProgressDialog dialog;
	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		super.onClick(v);
		switch (v.getId()) {
		case R.id.loginButton:
			Utils.hideSoftKeyboard(context);
			if (!(msg.equals(""))) {
				showToastS(msg);
			} else if (checkNetworkConnection()) {
				setPrefrenceBool(Constants.isFbLogin,false);
				dialog = showDialog("Logging in...");
				usernametxt = username.getText().toString().trim();
				passwordtxt = password.getText().toString().trim();

				ParseUser.logInInBackground(usernametxt, passwordtxt, new LogInCallback() {
					public void done(ParseUser user, ParseException e) {
						if (user != null) {

							 	//ParseUser.getCurrentUser().getParseFile("ProfilePicture");

//							dbHelper.updateUserInLocal(ParseUser.getCurrentUser().getObjectId(),
//									ParseUser.getCurrentUser().getUsername(),"", "", "", "", "", "", "", "", "",
//									"", "", "", "", "", "", "", "", "", "", "", "", "", "","");

							System.out.println("ParseUser objId========" + ParseUser.getCurrentUser().getObjectId());
							Cursor cUser = dbHelper.getUser(ParseUser.getCurrentUser().getObjectId());
							// System.out.println("cUser========"+cUser.getCount());
							if (cUser.moveToFirst()) {

								// if (cUser.moveToFirst()) {
								if (!(cUser.getString(cUser.getColumnIndex(LocalDatabaseHandler.USER_NAME)).equals("")) && cUser.getString(cUser.getColumnIndex(LocalDatabaseHandler.USER_NAME)) != null)

								{
									setPrefrence("profilePath", (cUser.getString(cUser.getColumnIndex(LocalDatabaseHandler.PROFILE_PIC))));
								}

							}


							checkAndCloseActivity(SplashScreenActivity.act);
							gotoActivityWithfinish(HomeActivity.class, null);


						//	new GetDeviceTokenId(context, callbacklistner).execute();
						} else {
							username.setText("");
							password.setText("");
							showToastS("User Name Or Password Is Incorrect...");
							dialog.dismiss();
							username.requestFocus();
							e.printStackTrace();
						}
					}
				});
			} else {
				showToastS(Constants.networkMsg);
			}
			msg = "";
			break;
		case R.id.loginFacebook:
			if (checkNetworkConnection()) {
				setPrefrenceBool(Constants.isFbLogin,true);
				progressDialog = ProgressDialog.show(context, "", "Logging in...", true);
				List<String> permissions = Arrays.asList("public_profile", "email", "user_posts", "user_photos",
						"user_birthday", "user_friends");
				ParseFacebookUtils.logInWithReadPermissionsInBackground(LoginActivity.this, permissions,
						new LogInCallback() {
							@Override
							public void done(ParseUser user, ParseException err) {
								progressDialog.dismiss();
								if (user == null) {
//									Toast.makeText(LoginActivity.this, " The user cancelled the Facebook login",
//											Toast.LENGTH_LONG).show();
								} else {
									makeMeRequest();
									new GetDeviceTokenId(context, callbacklistner).execute();
									System.out.println("fb user========" + user);
									Toast.makeText(LoginActivity.this, "User logged in Successfully", Toast.LENGTH_LONG)
											.show();
								}
							}
						});

			} else {
				showToastS(Constants.networkMsg);
			}
			break;

		case R.id.forgotPasTextView:
			System.out.println("forgot passwierd");
			goToActivity(ForgotPasswordActivity.class, null);
			break;
		default:
			break;
		}
	}

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		ParseFacebookUtils.onActivityResult(requestCode, resultCode, data);
	}

	private void messageDigest() {
		PackageInfo info;
		try {

			info = getPackageManager().getPackageInfo(getPackageName(), PackageManager.GET_SIGNATURES);
			for (android.content.pm.Signature signature : info.signatures) {
				MessageDigest md;
				md = MessageDigest.getInstance("SHA");
				md.update(signature.toByteArray());
				Log.d("===== KeyHash:1 ", Base64.encodeToString(md.digest(), Base64.DEFAULT));
				System.out.println("===== dilip KeyHash:2 "
						+ Base64.encodeToString(md.digest(), Base64.DEFAULT));
			}
		} catch (NameNotFoundException e) {
			e.printStackTrace();
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	URL image_path;

	private void makeMeRequest() {
		GraphRequest request = GraphRequest.newMeRequest(AccessToken.getCurrentAccessToken(),
				new GraphRequest.GraphJSONObjectCallback() {
					@Override
					public void onCompleted(JSONObject jsonObject, GraphResponse graphResponse) {
						System.out.println("jsonObject===============" + jsonObject);
						if (jsonObject != null) {
							System.out.println("jsonObject===============" + jsonObject);
							System.out.println("jsonObject===============" + jsonObject.toString());
							ParseUser currentUser = ParseUser.getCurrentUser();
							try {
								String random_code = generateRandom();

								currentUser.put("UserPromoCode", random_code);
								currentUser.put("FBKey", jsonObject.getLong("id") + "");

								if (jsonObject.getString("gender") != null)
									currentUser.put("Gender", jsonObject.getString("gender"));

								if (jsonObject.getString("email") != null)
									currentUser.setEmail(jsonObject.getString("email"));
								// Save the user profile info in a user property

								currentUser.setUsername(jsonObject.getString("name"));

								currentUser.put("IsActive", true);

								try {
									/*
									 * if (jsonObject.has("birthday")) {
									 * 
									 * String[] userBirthDate =
									 * jsonObject.getString("birthday").split(
									 * "/"); currentUser.put("BirthDate",
									 * format.parse(jsonObject.getString(
									 * "birthday"))); currentUser.put("Age",
									 * getAge(Integer.parseInt(userBirthDate[2])
									 * , Integer.parseInt(userBirthDate[1]),
									 * Integer.parseInt(userBirthDate[0])));
									 * 
									 * }
									 */
//									URL url = new URL(encodeURL("https://graph.facebook.com/" + jsonObject.getLong("id")
//											+ "/picture?type=large"));
//									Bitmap image = BitmapFactory.decodeStream(url.openConnection().getInputStream());
//
//									Utils.write("image   "+image);


									new DownloadImage(jsonObject, currentUser)
											.execute(encodeURL("https://graph.facebook.com/" + jsonObject.getLong("id")
													+ "/picture?type=large"));

									Utils.write("image_path===" + image_path);

								} catch (Exception e) {
									e.printStackTrace();
								}

							} catch (Exception e) {
								e.printStackTrace();
							}
						} else if (graphResponse.getError() != null) {
							switch (graphResponse.getError().getCategory()) {
							case LOGIN_RECOVERABLE:
								break;
							case TRANSIENT:
								break;
							case OTHER:
								break;
							}
						}
					}
				});
		Bundle parameters = new Bundle();
		parameters.putString("fields", "id,email,gender,name,birthday");
		request.setParameters(parameters);
		request.executeAsync();
	}

	public static Bitmap getFacebookProfilePicture(String url) {
		URL facebookProfileURL;
		try {
			facebookProfileURL = new URL(url);
			Bitmap bitmap = BitmapFactory.decodeStream(facebookProfileURL.openConnection().getInputStream());
			return bitmap;
		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		return null;

	}

	private String getAge(int year, int month, int day) {
		Calendar dob = Calendar.getInstance();
		Calendar today = Calendar.getInstance();
		dob.set(year, month, day);
		int age = today.get(Calendar.YEAR) - dob.get(Calendar.YEAR);
		if (today.get(Calendar.DAY_OF_YEAR) < dob.get(Calendar.DAY_OF_YEAR)) {
			age--;
		}
		Integer ageInt = new Integer(age);
		String ageS = ageInt.toString();
		return ageS;
	}

	@Override
	public void AsynctaskResultwithoutArray(String arr, String url, Map<String, Object> par) {
		// TODO Auto-generated method stub
		super.AsynctaskResultwithoutArray(arr, url, par);

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

	public void saveInstalledUser() {
		ParseObject obj = new ParseObject("UserToken");
		obj.put("TokenID", getPrefrence(Constants.googledeviceid));
		obj.put("UserID", ParseUser.getCurrentUser());
		obj.saveInBackground();
	}
	public static Bitmap getBitmapFromURL(String src) {
		try {
			URL url = new URL(src);
			HttpURLConnection connection = (HttpURLConnection) url.openConnection();
			connection.setDoInput(true);
			connection.connect();
			InputStream input = connection.getInputStream();
			Bitmap myBitmap = BitmapFactory.decodeStream(input);
			return myBitmap;
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		}
	}
	@Override
	public void AsynctaskResult(String result, boolean action) {
		// TODO Auto-generated method stub
		System.out.println("AsynctaskResult action    " + action);
		setPrefrence(Constants.googledeviceid, result);
		saveInstalledUser();


	//	gotoActivityWithfinish(HomeActivity.class, null);


		checkAndCloseActivity(SplashScreenActivity.act);
		// setPrefrence("username", usernametxt);
		// setPrefrence("profilePath", "");

		if (dialog != null)
			dialog.dismiss();
	}

	// asynctask
	Bitmap bRef;

	private class DownloadImage extends AsyncTask<String, Void, Bitmap> {

		JSONObject object = null;
		ParseUser currentuser = null;

		public DownloadImage(JSONObject jsonObject, ParseUser currentUser) {
			object = jsonObject;
			currentuser = currentUser;
			Utils.write("currentuser=========" + currentuser);

		}

		@Override
		protected void onPreExecute() {
			super.onPreExecute();

		}

		@Override
		protected Bitmap doInBackground(String... args) {

Utils.write("do in background url==="+args[0]);
			Bitmap bmp =getBitmapFromURL(args[0]);

//			String imageURL = args[0];
//			System.out.println("imageURL=facebook======" + imageURL);
//			Bitmap bitmap = null;
//			try {
//				URL url = new URL(imageURL);
//				System.out.println("url facebook==========="+url);
//				InputStream is = url.openConnection().getInputStream();
//				System.out.println("is===========" + is);
//				final BitmapFactory.Options options = new BitmapFactory.Options();
//				options.inJustDecodeBounds = true;
//				BufferedInputStream buffer = new BufferedInputStream(is);
//				BitmapFactory.decodeStream(buffer, null, options);
//				buffer.reset();
//				// Calculate inSampleSize
//				options.inSampleSize = 4;
//				// Decode bitmap with inSampleSize set
//				//options.inJustDecodeBounds = false;
//				Bitmap bmp = BitmapFactory.decodeStream(buffer, null, options);
//				saveOnShare(bmp);
//				Utils.write("bmp==gfaesdsgesd fgacebook=======" + bmp);
//				return bmp;
//			} catch (MalformedURLException e) {
//				// TODO Auto-generated catch block
//				e.printStackTrace();
//			} catch (IOException e) {
//				// TODO Auto-generated catch block
//				e.printStackTrace();
//			} catch (Exception e) {
//				// TODO: handle exception
//				e.printStackTrace();
//			}
			return bmp;
		}

		@Override
		protected void onPostExecute(final Bitmap myBitmap) {

			Utils.write("myBitmap======>" + myBitmap);

			if (myBitmap != null) {
				ByteArrayOutputStream stream = new ByteArrayOutputStream();
				myBitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
				byte[] image = stream.toByteArray();
				ParseFile file = new ParseFile("fbuser.png", image);
				currentuser.put("ProfilePicture", file);
			}
			/// put for savebackground

			currentuser.saveInBackground(new SaveCallback() {
				public void done(ParseException e) {
					// Handle success or failure
					// here ...
					if (e != null) {
						e.printStackTrace();
					} else {
						saveOnShare(myBitmap);
//						setPrefrence("username", ParseUser.getCurrentUser().getUsername());
//						setPrefrence("profilePath", profPicPath);
//						dbHelper.insertUser(ParseUser.getCurrentUser().getObjectId(),
//								ParseUser.getCurrentUser().getUsername(), profPicPath, "", "", "", "", "", "", "", "",
//								"", "", "", "", "", "", "", "", "", "", "", "", "", "");

//						new GetDeviceTokenId(context, callbacklistner).execute();
//						//gotoActivityWithfinish(HomeActivity.class, null);
//						checkAndCloseActivity(SplashScreenActivity.act);
					}
				}

			});

		}
	}

	public static String encodeURL(String urlStr) {

		URL url = null;
		try {
			// System.out.println("image url=="+urlStr);
			if (urlStr != null) {
				if (urlStr.length() > 4) {
					if (urlStr.startsWith("http") || urlStr.contains("http://")) {
					} else
						urlStr = "http://" + urlStr;
					url = new URL(urlStr);
					URI uri = new URI(url.getProtocol(), url.getUserInfo(), url.getHost(), url.getPort(), url.getPath(),
							url.getQuery(), url.getRef());
					url = uri.toURL();
					return url.toString().replaceAll("&amp;", "&");
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		return urlStr;
	}

	public void saveOnShare(Bitmap sourceBitmap) {
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

				//check if user exst in local
				Cursor cUser = dbHelper.getUser(ParseUser.getCurrentUser().getObjectId());
				System.out.println("ParseUser objId========" + ParseUser.getCurrentUser().getObjectId());
				System.out.println("ParseUser========" + cUser.getCount());
				if (cUser.moveToFirst()) {
					dbHelper.updateUserInLocalfb(ParseUser.getCurrentUser().getObjectId(),
							ParseUser.getCurrentUser().getUsername(), profPicPath);
				}
				else {
					dbHelper.updateUserInLocal(ParseUser.getCurrentUser().getObjectId(),
							ParseUser.getCurrentUser().getUsername(), profPicPath, "", "", "", "", "", "", "", "",
							"", "", "", "", "", "", "", "", "", "", "", "", "", "", "");
				}
				setPrefrence("profilePath", profPicPath);
				new GetDeviceTokenId(context, callbacklistner).execute();
				//gotoActivityWithfinish(HomeActivity.class, null);

				//set fb preference
				setPrefrenceBool("KEY_FB", true);



				Intent iii = new Intent(LoginActivity.this, HomeActivity.class);
				iii.putExtra("KEY_PROFILE_IMAGE", sourceBitmap);
				startActivity(iii);

				checkAndCloseActivity(SplashScreenActivity.act);
			} catch (Exception e) {
			}
		}

	}
	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		
		if(dbHelper != null){
			
			dbHelper.close();
		}
	}
	
}
