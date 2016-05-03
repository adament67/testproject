package com.stylist;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import com.constants.BaseActivity;
import com.constants.CustomEditText;
import com.constants.CustomTextView;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.http.SslError;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.webkit.SslErrorHandler;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class PayUMoneyActivity extends BaseActivity {
/*public static PayUMoneyActivity pPayUMoneyActivity;
	private ArrayList<String> post_val = new ArrayList<String>();
	private String post_Data = "";
	WebView webView;
	final Activity activity = this;
	private String tag = "PayUMoneyActivity";
	private String hash, hashSequence;

	*//*
	 * String merchant_key="JBZaLc"; String salt="GQs7yium";
	 *//*

	String merchant_key = "gtKFFx";
	String salt = "eCwWELxi";

	String action1 = "";
	String base_url = "https://test.payu.in";
	int error = 0;
	String hashString = "";
	Map<String, String> params;
	String txnid = "";

	Handler mHandler = new Handler();
	CustomEditText card_no, expiryDate, cvv;
	CustomTextView payment;

	@SuppressLint("JavascriptInterface")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_pay_umoney);
		setHeading("PayU Detail");
		backClick();
		// payUMethod();
		card_no = (CustomEditText) findViewById(R.id.card_no);
		expiryDate = (CustomEditText) findViewById(R.id.expiry_date);
		cvv = (CustomEditText) findViewById(R.id.cvv);
		payment = (CustomTextView) findViewById(R.id.payment);
		pPayUMoneyActivity=this;
		payment.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if (card_no.getText().toString().isEmpty())
					showToastS("Please Enter Card Number !");
				else if (card_no.getText().toString().length() != 16)
					showToastS("Please Enter Card Number of 16 digit!");
				else if (expiryDate.getText().toString().isEmpty())
					showToastS("Please Enter Date!");
				else if (cvv.getText().toString().isEmpty()) {
					showToastS("Please Enter CVV!");
				} else
					gotoActivityWithfinish(PaymentSuccessActivity.class, null);
			}
		});
	}

	public void payUMethod() {
		final ProgressDialog progressDialog = new ProgressDialog(activity);
		getWindow().requestFeature(Window.FEATURE_PROGRESS);
		webView = new WebView(this);
		setContentView(webView);
		post_val.add("key");
		post_val.add("JBZaLc");
		post_val.add("txnid");
		post_val.add("5d59dae66618a14f5020");
		post_val.add("amount");
		post_val.add("100.00");
		post_val.add("productinfo");

		post_val.add("{paymentParts:[{name:abc,description:abcd,value:500,isRequired:true,settlementEvent:EmailConfirmation}], paymentIdentifiers:[{field:CompletionDate, value:25/06/2015}]}");
		post_val.add("firstname");
		post_val.add("pqrs");
		post_val.add("email");
		post_val.add("xxxx@xxxmail.com");
		post_val.add("phone");
		post_val.add("xxxxxxxxxx");
		post_val.add("surl");
		post_val.add("https://www.google.com");
		post_val.add("furl");
		post_val.add("https://www.gmail.com");
		post_val.add("hash");
		post_val.add("");
		post_val.add("provider");
		post_val.add("payu_paisa");
		Log.d(tag, "post_val: " + post_val);
		params = new HashMap<String, String>();
		for (int i = 0; i < post_val.size();) {
			params.put(post_val.get(i), post_val.get(i + 1));

			i += 2;
		}

		if (empty(params.get("txnid"))) {
			Random rand = new Random();
			String rndm = Integer.toString(rand.nextInt())
					+ (System.currentTimeMillis() / 1000L);
			txnid = hashCal("SHA-256", rndm).substring(0, 20);
			popup("txnid : " + txnid);
			System.out.println("......txnid...... " + txnid);
		} else {
			txnid = params.get("txnid");
			System.out.println("....else.......txnid...... " + txnid);
		}

		// String udf2 = txnid;
		String txn = "abcd";
		hash = "";
		String hashSequence = "key|txnid|amount|productinfo|firstname|email|udf1|udf2|udf3|udf4|udf5|udf6|udf7|udf8|udf9|udf10";
		if (empty(params.get("hash")) && params.size() > 0) {
			if (empty(params.get("key")) || empty(params.get("txnid"))
					|| empty(params.get("amount"))
					|| empty(params.get("firstname"))
					|| empty(params.get("email")) || empty(params.get("phone"))
					|| empty(params.get("productinfo"))
					|| empty(params.get("surl")) || empty(params.get("furl"))
					|| empty(params.get("service_provider"))

			) {
				String[] hashVarSeq = hashSequence.split("\\|");
				for (int i = 0; i < hashVarSeq.length; i++) {
					System.out.println("<<<<<>>>>>>>> " + hashVarSeq[i]);
				}

				for (String part : hashVarSeq) {
					hashString = (empty(params.get(part))) ? hashString
							.concat("") : hashString.concat(params.get(part));
					hashString = hashString.concat("|");
					System.out.println("??????? " + hashString);
				}
				hashString = hashString.concat(salt);

				hash = hashCal("SHA-512", hashString);
				System.out.println(hashString + " /////~~~~~~ " + hash);
				action1 = base_url.concat("/_payment");
			}
		} else if (!empty(params.get("hash"))) {
			hash = params.get("hash");
			action1 = base_url.concat("/_payment");
			System.out.println("!!!!!!!!HASHHHHHHH!!!!!! " + hash);
		}

		webView.setWebViewClient(new WebViewClient() {

			@Override
			public void onReceivedError(WebView view, int errorCode,
					String description, String failingUrl) {
				// TODO Auto-generated method stub
				System.out
						.println(">>>>>>>>>>>>>>onReceivedError>>>>>>>>>>>>>>>>>>");
				Toast.makeText(activity, "Oh no! " + description,
						Toast.LENGTH_SHORT).show();
			}

			@Override
			public void onReceivedSslError(WebView view,
					SslErrorHandler handler, SslError error) {
				// TODO Auto-generated method stub
				System.out
						.println(">>>>>>>>>>>>>>onReceivedSslError>>>>>>>>>>>>>>>>>>");
				Toast.makeText(activity, "SslError! " + error,
						Toast.LENGTH_SHORT).show();
				handler.proceed();
			}

			@Override
			public boolean shouldOverrideUrlLoading(WebView view, String url) {
				// TODO Auto-generated method stub
				System.out
						.println(">>>>>>>>>>>>>>shouldOverrideUrlLoading>>>>>>>>>>>>>>>>>>");
				return super.shouldOverrideUrlLoading(view, url);
			}

			@Override
			public void onPageFinished(WebView view, String url) {
				// TODO Auto-generated method stub
				super.onPageFinished(view, url);
				System.out
						.println(">>>>>>>>>>>>>>onPageFinished>>>>>>>>>>>>>>>>>>");
			}

		});

		webView.setVisibility(0);
		webView.getSettings().setBuiltInZoomControls(true);
		webView.getSettings().setCacheMode(2);
		webView.getSettings().setDomStorageEnabled(true);
		webView.clearHistory();
		webView.clearCache(true);
		webView.getSettings().setJavaScriptEnabled(true);
		webView.getSettings().setSupportZoom(true);
		webView.getSettings().setUseWideViewPort(false);
		webView.getSettings().setLoadWithOverviewMode(false);

		*//*
		 * webView.addJavascriptInterface(new PayUJavaScriptInterface(activity),
		 * "PayUMoney");
		 *//*
		Map<String, String> mapParams = new HashMap<String, String>();
		mapParams.put("key", merchant_key);
		mapParams.put("hash", PayUMoneyActivity.this.hash);
		mapParams.put("txnid",
				(empty(PayUMoneyActivity.this.params.get("txnid"))) ? ""
						: PayUMoneyActivity.this.params.get("txnid"));
		Log.d(tag, "txnid: " + PayUMoneyActivity.this.params.get("txnid"));
		mapParams.put("service_provider", "payu_paisa");

		mapParams.put("amount",
				(empty(PayUMoneyActivity.this.params.get("amount"))) ? ""
						: PayUMoneyActivity.this.params.get("amount"));
		mapParams.put("firstname",
				(empty(PayUMoneyActivity.this.params.get("firstname"))) ? ""
						: PayUMoneyActivity.this.params.get("firstname"));
		mapParams.put("email",
				(empty(PayUMoneyActivity.this.params.get("email"))) ? ""
						: PayUMoneyActivity.this.params.get("email"));
		mapParams.put("phone",
				(empty(PayUMoneyActivity.this.params.get("phone"))) ? ""
						: PayUMoneyActivity.this.params.get("phone"));

		mapParams.put("productinfo",
				(empty(PayUMoneyActivity.this.params.get("productinfo"))) ? ""
						: PayUMoneyActivity.this.params.get("productinfo"));
		mapParams.put("surl",
				(empty(PayUMoneyActivity.this.params.get("surl"))) ? ""
						: PayUMoneyActivity.this.params.get("surl"));
		mapParams.put("furl",
				(empty(PayUMoneyActivity.this.params.get("furl"))) ? ""
						: PayUMoneyActivity.this.params.get("furl"));
		mapParams.put("lastname",
				(empty(PayUMoneyActivity.this.params.get("lastname"))) ? ""
						: PayUMoneyActivity.this.params.get("lastname"));

		mapParams.put("address1",
				(empty(PayUMoneyActivity.this.params.get("address1"))) ? ""
						: PayUMoneyActivity.this.params.get("address1"));
		mapParams.put("address2",
				(empty(PayUMoneyActivity.this.params.get("address2"))) ? ""
						: PayUMoneyActivity.this.params.get("address2"));
		mapParams.put("city",
				(empty(PayUMoneyActivity.this.params.get("city"))) ? ""
						: PayUMoneyActivity.this.params.get("city"));
		mapParams.put("state",
				(empty(PayUMoneyActivity.this.params.get("state"))) ? ""
						: PayUMoneyActivity.this.params.get("state"));

		mapParams.put("country",
				(empty(PayUMoneyActivity.this.params.get("country"))) ? ""
						: PayUMoneyActivity.this.params.get("country"));
		mapParams.put("zipcode",
				(empty(PayUMoneyActivity.this.params.get("zipcode"))) ? ""
						: PayUMoneyActivity.this.params.get("zipcode"));
		mapParams.put("udf1",
				(empty(PayUMoneyActivity.this.params.get("udf1"))) ? ""
						: PayUMoneyActivity.this.params.get("udf1"));
		mapParams.put("udf2",
				(empty(PayUMoneyActivity.this.params.get("udf2"))) ? ""
						: PayUMoneyActivity.this.params.get("udf2"));

		mapParams.put("udf3",
				(empty(PayUMoneyActivity.this.params.get("udf3"))) ? ""
						: PayUMoneyActivity.this.params.get("udf3"));
		mapParams.put("udf4",
				(empty(PayUMoneyActivity.this.params.get("udf4"))) ? ""
						: PayUMoneyActivity.this.params.get("udf4"));
		mapParams.put("udf5",
				(empty(PayUMoneyActivity.this.params.get("udf5"))) ? ""
						: PayUMoneyActivity.this.params.get("udf5"));
		mapParams.put("pg",
				(empty(PayUMoneyActivity.this.params.get("pg"))) ? ""
						: PayUMoneyActivity.this.params.get("pg"));
		webview_ClientPost(webView, action1, mapParams.entrySet());

	}

	public class PayUJavaScriptInterface {
		Context mContext;

		*//** Instantiate the interface and set the context *//*
		PayUJavaScriptInterface(Context c) {
			mContext = c;
		}

		public void success(long id, final String paymentId) {

			mHandler.post(new Runnable() {

				public void run() {

					mHandler = null;

					Intent intent = new Intent(PayUMoneyActivity.this,
							BuyTokenActivity.class);

					intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP
							| Intent.FLAG_ACTIVITY_SINGLE_TOP);

					intent.putExtra("result", "success");

					intent.putExtra("paymentId", paymentId);

					startActivity(intent);

					finish();

				}

			});

		}

	}

	public void webview_ClientPost(WebView webView, String url,
			Collection<Map.Entry<String, String>> postData) {
		StringBuilder sb = new StringBuilder();

		sb.append("<html><head></head>");
		sb.append("<body onload='form1.submit()'>");
		sb.append(String.format("<form id='form1' action='%s' method='%s'>",
				url, "post"));
		for (Map.Entry<String, String> item : postData) {
			sb.append(String.format(
					"<input name='%s' type='hidden' value='%s' />",
					item.getKey(), item.getValue()));
		}
		sb.append("</form></body></html>");
		Log.d(tag, "webview_ClientPost called");
		webView.loadData(sb.toString(), "text/html", "utf-8");
	}

	public void success(long id, final String paymentId) {
	}

	public boolean empty(String s) {
		if (s == null || s.trim().equals(""))
			return true;
		else
			return false;
	}

	public String hashCal(String type, String str) {
		byte[] hashseq = str.getBytes();
		StringBuffer hexString = new StringBuffer();
		try {
			MessageDigest algorithm = MessageDigest.getInstance(type);
			algorithm.reset();
			algorithm.update(hashseq);
			byte messageDigest[] = algorithm.digest();

			for (int i = 0; i < messageDigest.length; i++) {
				String hex = Integer.toHexString(0xFF & messageDigest[i]);
				if (hex.length() == 1)
					hexString.append("0");
				hexString.append(hex);
			}

		} catch (NoSuchAlgorithmException nsae) {
		}

		return hexString.toString();

	}

	public void popup(String msg) {
		Toast.makeText(PayUMoneyActivity.this, "" + msg, Toast.LENGTH_LONG)
				.show();
	}

	*//*

    *//**//**
	 * Adding WebView as setContentView
	 *//*
	*//*
	 * WebView webView;
	 *//**//**
	 * Context for Activity
	 *//*
	*//*
	 * Context activity;
	 *//**//**
	 * Order Id To Request for Updating Payment Status if Payment
	 * Successfully Done
	 *//*
	*//*
	 * int mId; //Getting from Previous Activity
	 *//**//**
	 * Required Fields
	 *//*
	*//*
	 * // Test Variables
	 * 
	 * private String mMerchantKey = "gtKFFx"; private String mSalt =
	 * "eCwWELxi"; // private String mBaseURL = "https://test.payu.in";
	 * 
	 * STEP 1 Complete a successful test transaction using the test credit card
	 * on PayU�s test server. Merchant ID: gtKFFx SALT: eCwWELxi PayU Test URL:
	 * test.payu.in/_payment Below are the test card details for doing a test
	 * transaction in the testing mode Card Name � Any name Card Number:
	 * 5123456789012346 CVV: 123 Expiry: may 2017 STEP 2 Once you have done the
	 * test transaction successfully. Merchant Key: XYZ SALT: go to -->
	 * payu.in/index?returnUrl=salt# The production URL is:
	 * secure.payu.in/_payment For Integration Kit: github.com/payu-india �
	 * Jaydeep Goswami Apr 29 '15 at 4:50
	 * 
	 * // Final Variables private String mMerchantKey = "Your Merchant Key";
	 * private String mSalt = "Salt"; private String mBaseURL =
	 * "https://secure.payu.com";
	 * 
	 * 
	 * private String mAction = ""; // For Final URL private String mTXNId; //
	 * This will create below randomly private String mHash; // This will create
	 * below randomly private String mProductInfo = "Food Items"; //Passing
	 * String only private String mFirstName; // From Previous Activity private
	 * String mEmailId; // From Previous Activity private double mAmount; //
	 * From Previous Activity private String mPhone; // From Previous Activity
	 * private String mServiceProvider = "payu_paisa"; private String
	 * mSuccessUrl = "your success URL"; private String mFailedUrl =
	 * "Your Failure URL";
	 * 
	 * 
	 * boolean isFromOrder;
	 *//**//**
	 * Handler
	 *//*
	*//*
	 * Handler mHandler = new Handler();
	 *//**//**
	 * @param savedInstanceState
	 *//*
	*//*
	 * @SuppressLint({"AddJavascriptInterface", "SetJavaScriptEnabled",
	 * "JavascriptInterface"})
	 * 
	 * @Override protected void onCreate(Bundle savedInstanceState) {
	 * getWindow().requestFeature(Window.FEATURE_PROGRESS);
	 * super.onCreate(savedInstanceState);
	 *//**//**
	 * Setting WebView to Screen
	 *//*
	*//*
	 * setContentView(R.layout.activity_pay_umoney);
	 *//**//**
	 * Creating WebView
	 *//*
	*//*
	 * webView = (WebView) findViewById(R.id.help_webview);
	 *//**//**
	 * Context Variable
	 *//*
	*//*
	 * activity = getApplicationContext();
	 *//**//**
	 * Actionbar Settings
	 *//*
	*//*
	 * Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
	 * setSupportActionBar(toolbar);
	 * 
	 * ActionBar ab = getSupportActionBar(); ab.setDisplayHomeAsUpEnabled(true);
	 * // enabling action bar app icon and behaving it as toggle button
	 * ab.setHomeButtonEnabled(true);
	 * ab.setTitle(getString(R.string.title_activity_online_payment));
	 *//**//**
	 * Getting Intent Variables...
	 *//*
	*//*
	 * Bundle bundle = getIntent().getExtras(); if (bundle != null) {
	 * 
	 * mFirstName = bundle.getString("name"); mEmailId =
	 * bundle.getString("email"); mAmount = bundle.getDouble("amount"); mPhone =
	 * bundle.getString("phone"); mId = bundle.getInt("id"); isFromOrder =
	 * bundle.getBoolean("isFromOrder");
	 * 
	 * // Log.i(TAG, "" + mFirstName + " : " + mEmailId + " : " + mAmount +
	 * " : " + mPhone);
	 *//**//**
	 * Creating Transaction Id
	 *//*
	*//*
	 * Random rand = new Random(); String randomString =
	 * Integer.toString(rand.nextInt()) + (System.currentTimeMillis() / 1000L);
	 * mTXNId = hashCal("SHA-256", randomString).substring(0, 20);
	 * 
	 * mAmount = new BigDecimal(mAmount).setScale(0,
	 * RoundingMode.UP).intValue();
	 *//**//**
	 * Creating Hash Key
	 *//*
	*//*
	 * mHash = hashCal("SHA-512", mMerchantKey + "|" + mTXNId + "|" + mAmount +
	 * "|" + mProductInfo + "|" + mFirstName + "|" + mEmailId + "|||||||||||" +
	 * mSalt);
	 *//**//**
	 * Final Action URL...
	 *//*
	*//*
	 * mAction = mBaseURL.concat("/_payment");
	 *//**//**
	 * WebView Client
	 *//*
	*//*
	 * webView.setWebViewClient(new WebViewClient() {
	 * 
	 * @Override public void onReceivedError(WebView view, WebResourceRequest
	 * request, WebResourceError error) { super.onReceivedError(view, request,
	 * error); Toast.makeText(activity, "Oh no! " + error,
	 * Toast.LENGTH_SHORT).show(); }
	 * 
	 * @Override public void onReceivedSslError(WebView view, SslErrorHandler
	 * handler, SslError error) { Toast.makeText(activity, "SSL Error! " +
	 * error, Toast.LENGTH_SHORT).show(); handler.proceed(); }
	 * 
	 * @Override public boolean shouldOverrideUrlLoading(WebView view, String
	 * url) { return super.shouldOverrideUrlLoading(view, url); }
	 * 
	 * @Override public void onPageFinished(WebView view, String url) {
	 * 
	 * if (url.equals(mSuccessUrl)) { Intent intent = new
	 * Intent(PayUMoneyActivity.this, PaymentStatusActivity.class);
	 * intent.putExtra("status", true); intent.putExtra("transaction_id",
	 * mTXNId); intent.putExtra("id", mId); intent.putExtra("isFromOrder",
	 * isFromOrder); intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
	 * startActivity(intent); } else if (url.equals(mFailedUrl)) { Intent intent
	 * = new Intent(PayUMoneyActivity.this, PaymentStatusActivity.class);
	 * intent.putExtra("status", false); intent.putExtra("transaction_id",
	 * mTXNId); intent.putExtra("id", mId); intent.putExtra("isFromOrder",
	 * isFromOrder); intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
	 * startActivity(intent); } super.onPageFinished(view, url); } });
	 * 
	 * webView.setVisibility(View.VISIBLE);
	 * webView.getSettings().setBuiltInZoomControls(true);
	 * webView.getSettings().setCacheMode(2);
	 * webView.getSettings().setDomStorageEnabled(true); webView.clearHistory();
	 * webView.clearCache(true);
	 * webView.getSettings().setJavaScriptEnabled(true);
	 * webView.getSettings().setSupportZoom(true);
	 * webView.getSettings().setUseWideViewPort(false);
	 * webView.getSettings().setLoadWithOverviewMode(false);
	 * webView.addJavascriptInterface(new
	 * PayUJavaScriptInterface(PayUMoneyActivity.this), "PayUMoney");
	 *//**//**
	 * Mapping Compulsory Key Value Pairs
	 *//*
	*//*
	 * Map<String, String> mapParams = new HashMap<String, String>();
	 * 
	 * mapParams.put("key", mMerchantKey); mapParams.put("txnid", mTXNId);
	 * mapParams.put("amount","1000"); mapParams.put("productinfo", "abc");
	 * mapParams.put("firstname","abc"); mapParams.put("email",
	 * "dilip.trivialworks@gmail.com"); mapParams.put("phone", "7309728081");
	 * mapParams.put("surl", mSuccessUrl); mapParams.put("furl", mFailedUrl);
	 * mapParams.put("hash", mHash); mapParams.put("service_provider",
	 * mServiceProvider);
	 * 
	 * webViewClientPost(webView, mAction, mapParams.entrySet()); } else {
	 * Toast.makeText(activity, "Something went wrong, Try again.",
	 * Toast.LENGTH_LONG).show(); } }
	 *//**//**
	 * Posting Data on PayUMoney Site with Form
	 * 
	 * @param webView
	 * @param url
	 * @param postData
	 *//*
	*//*
	 * public void webViewClientPost(WebView webView, String url,
	 * Collection<Map.Entry<String, String>> postData) { StringBuilder sb = new
	 * StringBuilder();
	 * 
	 * sb.append("<html><head></head>");
	 * sb.append("<body onload='form1.submit()'>");
	 * sb.append(String.format("<form id='form1' action='%s' method='%s'>", url,
	 * "post"));
	 * 
	 * for (Map.Entry<String, String> item : postData) {
	 * sb.append(String.format("<input name='%s' type='hidden' value='%s' />",
	 * item.getKey(), item.getValue())); } sb.append("</form></body></html>");
	 * 
	 * Log.d("TAG", "webViewClientPost called: " + sb.toString());
	 * webView.loadData(sb.toString(), "text/html", "utf-8"); }
	 *//**//**
	 * Hash Key Calculation
	 * 
	 * @param type
	 * @param str
	 * @return
	 *//*
	*//*
	 * public String hashCal(String type, String str) { byte[] hashSequence =
	 * str.getBytes(); StringBuffer hexString = new StringBuffer(); try {
	 * MessageDigest algorithm = MessageDigest.getInstance(type);
	 * algorithm.reset(); algorithm.update(hashSequence); byte messageDigest[] =
	 * algorithm.digest();
	 * 
	 * for (int i = 0; i < messageDigest.length; i++) { String hex =
	 * Integer.toHexString(0xFF & messageDigest[i]); if (hex.length() == 1)
	 * hexString.append("0"); hexString.append(hex); } } catch
	 * (NoSuchAlgorithmException NSAE) { } return hexString.toString(); }
	 * 
	 * @Override public boolean onOptionsItemSelected(MenuItem item) {
	 * if(item.getItemId() == android.R.id.home) { onPressingBack(); } return
	 * super.onOptionsItemSelected(item); }
	 * 
	 * @Override public void onBackPressed() { onPressingBack(); }
	 *//**//**
	 * On Pressing Back Giving Alert...
	 *//*
	*//*
	 * private void onPressingBack() {
	 * 
	 * final Intent intent;
	 * 
	 * if(isFromOrder) intent = new Intent(PayUMoneyActivity.this,
	 * ProductInCartList.class); else intent = new
	 * Intent(PayUMoneyActivity.this, MainActivity.class);
	 * 
	 * intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
	 * 
	 * AlertDialog.Builder alertDialog = new
	 * AlertDialog.Builder(PayUMoneyActivity.this);
	 * 
	 * // Setting Dialog Title alertDialog.setTitle("Warning");
	 * 
	 * // Setting Dialog Message
	 * alertDialog.setMessage("Do you cancel this transaction?");
	 * 
	 * // On pressing Settings button alertDialog.setPositiveButton("Yes", new
	 * DialogInterface.OnClickListener() { public void onClick(DialogInterface
	 * dialog, int which) { finish(); startActivity(intent); } });
	 * 
	 * // on pressing cancel button alertDialog.setNegativeButton("No", new
	 * DialogInterface.OnClickListener() { public void onClick(DialogInterface
	 * dialog, int which) { dialog.dismiss(); } });
	 * 
	 * // Showing Alert Message alertDialog.show(); }
	 * 
	 * public class PayUJavaScriptInterface { Context mContext;
	 *//**//**
	 * Instantiate the interface and set the context
	 *//*
	*//*
	 * PayUJavaScriptInterface(Context c) { mContext = c; }
	 * 
	 * public void success(long id, final String paymentId) { mHandler.post(new
	 * Runnable() {
	 * 
	 * public void run() { mHandler = null;
	 * Toast.makeText(PayUMoneyActivity.this, "Payment Successfully.",
	 * Toast.LENGTH_SHORT).show(); } }); } }
	 */
}