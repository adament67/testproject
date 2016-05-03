package com.stylist.rssfeed;

import com.constants.BaseActivity;
import com.stylist.R;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;




public class DisPlayWebPageActivity extends BaseActivity {
	
	WebView webview;
	private ProgressBar progress;

	String page_url;
	String title;

	public static DisPlayWebPageActivity sDisPlayWebPageActivity;
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.webview);
		backClick();

		//init
		initialization();

	}

	private void initialization() {

		//get intent values
		Intent in = getIntent();
		page_url = in.getStringExtra("page_url");
		title=in.getStringExtra("title");

		Log.v("URL", page_url);
		Log.v("Title", title);

		setHeading(title);

		//activity reference
		sDisPlayWebPageActivity=this;

		webview = (WebView) findViewById(R.id.webView);
		webview.setWebViewClient(new MyWebViewClient());

		progress = (ProgressBar) findViewById(R.id.progressBar);
		progress.setMax(100);

		webview.getSettings().setJavaScriptEnabled(true);
		webview.loadUrl(page_url);

		DisPlayWebPageActivity.this.progress.setProgress(0);

	}


	protected void setHeading(String heading) {
		TextView headerTitle = (TextView) this.findViewById(R.id.headerName);
		headerTitle.setText(heading.toUpperCase());

	}


	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
	    if ((keyCode == KeyEvent.KEYCODE_BACK) && webview.canGoBack()) {
	    	webview.goBack();
	        return true;
	    }
	    return super.onKeyDown(keyCode, event);
	}

	private class MyWebViewClient extends WebViewClient {

		@Override
		public void onPageStarted(WebView view, String url, Bitmap favicon) {
			// TODO Auto-generated method stub
			super.onPageStarted(view, url, favicon);
		}

		@Override
		public boolean shouldOverrideUrlLoading(WebView view, String url) {
			// TODO Auto-generated method stub
			progress.setVisibility(View.VISIBLE);
			view.loadUrl(url);
			return true;

		}

		@Override
		public void onPageFinished(WebView view, String url) {
			// TODO Auto-generated method stub
			super.onPageFinished(view, url);

			progress.setVisibility(View.GONE);
		}

	}


	public void setValue(int progress) {
		this.progress.setProgress(progress);
	}

}


