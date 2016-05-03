package com.stylist;

import java.text.SimpleDateFormat;
import java.util.ArrayList;


import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.http.NameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.constants.AsyncTaskAppWithoutArray;
import com.constants.BaseActivity;
import com.constants.Constants;
import com.constants.CustomTextView;
import com.constants.WebImageLoader;

import com.stylist.rssfeed.DisPlayWebPageActivity;
import com.stylist.rssfeed.ListRSSItemsActivity;
import com.stylist.rssfeed.RSSDatabaseHandler;
import com.stylist.rssfeed.RSSFeed;
import com.stylist.rssfeed.RSSItem;
import com.stylist.rssfeed.RSSParser;
import com.stylist.rssfeed.SimpleHTTPConnection;
import com.stylist.rssfeed.WebSite;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.view.ContextMenu;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Toast;

public class TrendingActivity extends BaseActivity {

	public static TrendingActivity tTrendingActivity;
	ListView listAll, listArtist;
	WebImageLoader webImgLoader;
	CustomTextView headerName;
	LinearLayout backButtonLayout;
	CustomTextView button_All, button_artist;
	// GridView grid;
	public ArrayList<TrendingItemModelClass> trendingItem = new ArrayList<TrendingItemModelClass>();

	private ProgressDialog pDialog;
	ArrayList<String> data = new ArrayList<String>();
	// Array list for list view
	ArrayList<HashMap<String, String>> rssFeedList;
	RSSParser rssParser = new RSSParser();
	RSSFeed rssFeed;
	// TextView h_home, h_back;
	// array to trace sqlite ids
	public ArrayList<String> sqliteIds = new ArrayList<String>();
	// String[] sqliteIds;
	public static String TAG_ID = "id";
	public static String TAG_TITLE = "title";
	public static String TAG_LINK = "link";

	Boolean firstTime = true;
	public static Activity ctx;
	Boolean artistClick = false;
	SimpleDateFormat outformat = new SimpleDateFormat("dd MMM yyyy");
	SimpleDateFormat informat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
	ArrayList<HashMap<String, String>> rssItemList = new ArrayList<HashMap<String, String>>();
	RssImage adapter;
	List<RSSItem> rssItems = new ArrayList<RSSItem>();
	private static String TAG_DESRIPTION = "description";
	private static String TAG_PUB_DATE = "pubDate";


	private static String TAG_GUID = "guid";
	private static String TAG_IMAGE = "image";// not used
	WebImageLoader imageLoader;
	ArrayList<TrendingItemFromServiceModelClass> tTrendingItemFromServiceModelClass = new ArrayList<TrendingItemFromServiceModelClass>();

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
		setContentView(R.layout.activity_trending);
		setHeading("TRENDING");
		backClick();
		webImgLoader = new WebImageLoader(context);
		listAll = (ListView) findViewById(R.id.listViewAll);
		listArtist = (ListView) findViewById(R.id.listViewArtist);
		button_All = (CustomTextView) findViewById(R.id.button_all);
		button_All.setOnClickListener(this);
		button_artist = (CustomTextView) findViewById(R.id.button_artist);
		button_artist.setOnClickListener(this);
		button_artist.setBackgroundResource(R.drawable.eo_btn_selector_left);
		button_artist.setTextColor(getResources().getColor(R.color.whiteColor));
		button_artist.setTextColor(getResources().getColor(R.color.app_color));
		button_artist.setBackgroundResource(0);
		button_All.setVisibility(View.VISIBLE);
		button_ALLClick();
		ctx = TrendingActivity.this;
		rssFeedList = new ArrayList<HashMap<String, String>>();
		listAll.setVisibility(View.VISIBLE);
		tTrendingActivity = this;

		listArtist.setOnItemClickListener(new OnItemClickListener() {

			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

				/*Toast.makeText(TrendingActivity.this, "Hi", Toast.LENGTH_SHORT).show();*/
				
				// getting values from selected ListItem
				String sqlite_id = sqliteIds.get(position);
				System.out.println("sqlite_id==============" + sqlite_id);
				// Starting new intent
				Intent in = new Intent(getApplicationContext(), ListRSSItemsActivity.class);
				// passing sqlite row id
				in.putExtra(TAG_ID, sqlite_id);
				in.putExtra("title", trendingItem.get(position).source);

				in.putExtra("fromList", 0);
				startActivity(in);

			}
		});

		listAll.setOnItemClickListener(new OnItemClickListener() {

			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				// getting values from selected ListItem
				System.out.println("listall item click==============");
				System.out.println("listall item click link ==============" + tTrendingItemFromServiceModelClass.get(position).link);
				Intent in = new Intent(getApplicationContext(), DisPlayWebPageActivity.class);
				in.putExtra("page_url", tTrendingItemFromServiceModelClass.get(position).url);
				in.putExtra("title", tTrendingItemFromServiceModelClass.get(position).title);



				startActivity(in);
			}
		});

		if (checkNetworkConnection()) {
			new GetMyEvents().execute();

		} else {
			showToastS(Constants.networkMsg);
		}
	}

	private void getTrendings() {
		Map<String, Object> params1 = new HashMap<String, Object>();
		new AsyncTaskAppWithoutArray(true, params1, TrendingActivity.this, context, "getRssfeed");
	}

	@Override
	public void AsynctaskResultwithoutArray(String arr, String url, Map<String, Object> par) {
		// TODO Auto-generated method stub
		super.AsynctaskResultwithoutArray(arr, url, par);
		System.out.println("arr==========" + arr);
		if (url.equals("getRssfeed"))
			trendingItem.clear();
		try {
			int index = 0;
			JSONArray jArray = new JSONArray(arr);
			System.out.println("jArray========" + jArray);
			int length = jArray.length();
			while (index < length) {
				JSONObject obj = jArray.getJSONObject(index);
				trendingItem.add(new TrendingItemModelClass(obj.getString("Source"),
						obj.getJSONObject("LookImage").getString("url"), obj.getString("URL"),
						obj.getString("ShortSummary"), obj.getString("Title"),
						obj.getJSONObject("PublishedDate").getString("iso")) ); //ShortSummary

				System.out.println("url         " + obj.getString("URL"));
				data.add("" + obj.getString("URL"));

				index++;
			}

			if (data.size() > 0) {
				System.out.println("url      entry   ");
				new loadRSSFeed().execute(data.get(0));
			}

		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		// if result code 100
		if (resultCode == 100) {
			// reload this screen again
			Intent intent = getIntent();
			finish();
			startActivity(intent);
		}
	}

	/**
	 * Building a context menu for listview Long press on List row to see
	 * context menu
	 */
	@Override
	public void onCreateContextMenu(ContextMenu menu, View v, ContextMenuInfo menuInfo) {
		if (v.getId() == R.id.list) {
			menu.setHeaderTitle("Delete");
			menu.add(Menu.NONE, 0, 0, "Delete Feed");
		}
	}

	/**
	 * Responding to context menu selected option
	 */
	@Override
	public boolean onContextItemSelected(MenuItem item) {
		AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
		int menuItemIndex = item.getItemId();
		// check for selected option
		if (menuItemIndex == 0) {
			// user selected delete
			// delete the feed
			RSSDatabaseHandler rssDb = new RSSDatabaseHandler(getApplicationContext());
			WebSite site = new WebSite();
			site.setId(Integer.parseInt(sqliteIds.get(info.position)));
			rssDb.deleteSite(site);
			// reloading same activity again
			Intent intent = getIntent();
			finish();
			startActivity(intent);
		}

		return true;
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		super.onClick(v);
		switch (v.getId()) {
		case R.id.button_all:
			button_ALLClick();
			artistClick = false;
			listArtist.setVisibility(View.GONE);
			listAll.setVisibility(View.VISIBLE);

			break;
		case R.id.button_artist:
			artistClick = true;
			if (firstTime) {

				if (checkNetworkConnection()) {
					getTrendings();
				} else {
					showToastS(Constants.networkMsg);
				}
				firstTime = false;
			}
			/*
			 * if (data.size() > 0) { System.out.println("url      entry   ");
			 * new loadRSSFeed().execute(data.get(0)); }
			 */
			listArtist.setVisibility(View.VISIBLE);
			listAll.setVisibility(View.GONE);
			button_ABCClick();
			break;
		default:
			break;
		}
	}

	private void button_ABCClick() {
		System.out.println("button_nonEoClick");
		button_artist.setBackgroundResource(R.drawable.eo_btn_selector_right);
		button_artist.setTextColor(getResources().getColor(R.color.whiteColor));
		button_All.setTextColor(getResources().getColor(R.color.app_color));
		button_All.setBackgroundResource(0);
	}

	private void button_ALLClick() {
		System.out.println("button_Click");
		button_All.setBackgroundResource(R.drawable.eo_btn_selector_left);
		button_All.setTextColor(getResources().getColor(R.color.whiteColor));
		button_artist.setTextColor(getResources().getColor(R.color.app_color));
		button_artist.setBackgroundResource(0);
	}

	class loadRSSFeed extends AsyncTask<String, String, String> {

		/**
		 * Before starting background thread Show Progress Dialog
		 */
		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			pDialog = new ProgressDialog(TrendingActivity.this);
			pDialog.setMessage("Fetching RSS Information ...");
			pDialog.setIndeterminate(false);
			pDialog.setCancelable(false);
			pDialog.show();
		}

		/**
		 * getting Inbox JSON
		 */
		@Override
		protected String doInBackground(String... args) {
			System.out.println("url in background==========" + args[0]);
			String url = args[0];
			rssFeed = rssParser.getRSSFeed(url);
			if (rssFeed != null) {
				RSSDatabaseHandler rssDb = new RSSDatabaseHandler(getApplicationContext());
				WebSite site = new WebSite(rssFeed.getTitle(), rssFeed.getLink(), rssFeed.getRSSLink(),
						rssFeed.getDescription(), rssFeed.get_image());
				rssDb.addSite(site);

			} else {
				// updating UI from Background Thread
				runOnUiThread(new Runnable() {
					public void run() {
						// lblMessage.setText("Rss url not found. Please check
						// the url or try again");
					}
				});
			}
			return null;
		}

		/**
		 * After completing background task Dismiss the progress dialog
		 **/
		protected void onPostExecute(String args) {
			// dismiss the dialog after getting all products

			System.out.println("onpost");
			pDialog.dismiss();
			// updating UI from Background Thread
			if (data.size() > 0) {
				data.remove(0);
			}
			System.out.println(">>>>>>DATA SIZE>>>>" + data);
			if (data.size() > 0) {
				new loadRSSFeed().execute(data.get(0));
			} else {
				runOnUiThread(new Runnable() {
					public void run() {
						if (rssFeed != null) {
							new loadStoreSites().execute();

						}

					}
				});
			}

		}

	}

	/**
	 * Background Async Task to get RSS data from URL
	 */
	class loadStoreSites extends AsyncTask<String, String, String> {

		/**
		 * Before starting background thread Show Progress Dialog
		 */
		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			pDialog = new ProgressDialog(TrendingActivity.this);
			pDialog.setMessage("Loading websites ...");
			pDialog.setIndeterminate(false);
			pDialog.setCancelable(false);
			pDialog.show();
		}

		/**
		 * getting all stored website from SQLite
		 */
		@Override
		protected String doInBackground(String... args) {
			// updating UI from Background Thread
			runOnUiThread(new Runnable() {
				public void run() {
					RSSDatabaseHandler rssDb = new RSSDatabaseHandler(getApplicationContext());

					// listing all websites from SQLite
					List<WebSite> siteList = rssDb.getAllSites();

					System.out.println("siteList=====" + siteList);

					sqliteIds = new ArrayList<String>();

					// loop through each website
					for (int i = 0; i < siteList.size(); i++) {
						// for (int i = 0; i < 1; i++) {
						WebSite s = siteList.get(i);

						// creating new HashMap
						HashMap<String, String> map = new HashMap<String, String>();

						// adding each child node to HashMap key => value
						map.put(TAG_ID, s.getId().toString());
						map.put(TAG_TITLE, s.getTitle());
						map.put(TAG_LINK, s.getLink());

						// adding HashList to ArrayList
						rssFeedList.add(map);
						System.out.println(">>>>>>>>>>" + i);

						// add sqlite id to array
						// used when deleting a website from sqlite
						sqliteIds.add(s.getId().toString());
					}
					/**
					 * Updating list view with websites
					 */
					if (artistClick == true) {
						System.out.println("artistClick=========" + artistClick);
						listArtist.setAdapter(new ListAdapter(trendingItem));

						registerForContextMenu(listArtist);
						artistClick = false;
					} else {
						for (int i = 0; i < sqliteIds.size(); i++) {
							String sqlite_id = sqliteIds.get(i);
							System.out.println("sqlite_id==============" + sqlite_id);
							Intent in = new Intent(getApplicationContext(), ListRSSItemsActivity.class);
							in.putExtra(TAG_ID, sqlite_id);
							startActivity(in);
						}

					}
				}
			});
			return null;
		}

		/**
		 * After completing background task Dismiss the progress dialog
		 **/
		protected void onPostExecute(String args) {
			// dismiss the dialog after getting all products
			pDialog.dismiss();

		}

	}


	//blogger adapter
	class ListAdapter extends BaseAdapter {

		ArrayList<TrendingItemModelClass> trendingItem;

		public ListAdapter(ArrayList<TrendingItemModelClass> trendingItem) {
			// TODO Auto-generated constructor stub
			this.trendingItem = trendingItem;
		}

		private class ViewHolder {
			CustomTextView name, title, date, link;
			ImageView fash_img;
		}

		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			return trendingItem.size();
		}

		@Override
		public Object getItem(int arg0) {
			// TODO Auto-generated method stub
			return trendingItem.get(arg0);
		}

		@Override
		public long getItemId(int arg0) {
			// TODO Auto-generated method stub
			return arg0;
		}

		@Override
		public View getView(final int position, View convertView, ViewGroup parent) {
			ViewHolder holder;
			if (convertView == null) {
				holder = new ViewHolder();
				convertView = View.inflate(context, R.layout.rss_site_list_row, null);
				holder.name = (com.constants.CustomTextView) convertView.findViewById(R.id.name);
				holder.title = (com.constants.CustomTextView) convertView.findViewById(R.id.title);
				holder.date = (com.constants.CustomTextView) convertView.findViewById(R.id.date);
				holder.link = (com.constants.CustomTextView) convertView.findViewById(R.id.link);
				holder.fash_img = (ImageView) convertView.findViewById(R.id.fash_img);
				convertView.setTag(holder);
			} else {
				holder = (ViewHolder) convertView.getTag();
			}

			holder.title.setText(trendingItem.get(position).title);
			holder.name.setText(Html.fromHtml(trendingItem.get(position).shortSummary));
			//holder.name.setText(trendingItem.get(position).source);
			webImgLoader.DisplayImage(trendingItem.get(position).photoUrl, holder.fash_img);



			try {
				holder.date.setText(outformat.format(informat.parse(trendingItem.get(position).publishedDate)));
			} catch (Exception e) {
				e.printStackTrace();
			}
			return convertView;
		}

	}

	private class GetMyEvents extends AsyncTask<String, Void, String> {
		@Override
		protected String doInBackground(String... params) {
			ArrayList<NameValuePair> nameValuePair = new ArrayList<NameValuePair>();
			//http://mobileappsdev.net/estilorobe/webservices/Index.php/rsslink
			return SimpleHTTPConnection.sendByPOST("http://54.201.113.167/admin/webservices/Index.php/rsslink",
					nameValuePair);
		}

		@Override
		protected void onPostExecute(String result) {
			System.out.println("Result get records -->  " + result);
			try {
				if (result != null) {
					System.out.println("result==============="+result);
					JSONArray jArr = new JSONArray(result);

					if (jArr.length() > 0) {

						

						for (int i = 0; i < jArr.length(); i++) {
							for (int j = 0; j < jArr.getJSONArray(i).length(); j++) {
								tTrendingItemFromServiceModelClass.add(new TrendingItemFromServiceModelClass(
										jArr.getJSONArray(i).getJSONObject(j).getString("title"),
										jArr.getJSONArray(i).getJSONObject(j).getString("link"),
										jArr.getJSONArray(i).getJSONObject(j).getString("pubDate"),
										jArr.getJSONArray(i).getJSONObject(j).getString("image")));							
								System.out.println("linkkkkkkkkkkkkkkkkkkkkkkkkkkkk=============="
										+ jArr.getJSONArray(i).getJSONObject(j).getString("link"));								
								System.out.println("image============================================="
										+ jArr.getJSONArray(i).getJSONObject(j).getString("image"));


							}

						}
						System.out.println("tTrendingItemFromServiceModelClass================="
								+ tTrendingItemFromServiceModelClass.size());
						listAll.setAdapter(new RssImage(context, tTrendingItemFromServiceModelClass));

					} else {
						pDialog.cancel();
					}
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
			pDialog.cancel();
		}

		@Override
		protected void onPreExecute() {
			pDialog = new ProgressDialog(TrendingActivity.this);
			pDialog.setMessage("Fetching RSS Information ...");
			pDialog.setIndeterminate(false);
			pDialog.setCancelable(false);
			pDialog.show();
			super.onPreExecute();

		}
	}


	//rss all adapter
	private class RssImage extends BaseAdapter {

		Context context;
		// ArrayList<HashMap<String, String>> rssItemList = new
		// ArrayList<HashMap<String, String>>();

		private ArrayList<TrendingItemFromServiceModelClass> dataSet;

		public RssImage(Context context, ArrayList<TrendingItemFromServiceModelClass> dataSet) {

			RssImage.this.dataSet = dataSet;

			this.context = context;
			// this.rssItemList = rssItemList;

		}

		private class ViewHolder {
			CustomTextView page_url, title, pub_date, link;
			//ImageView img;
			  WebView imageWebView;
		}

		@Override
		public int getCount() {

			return dataSet.size();
		}

		@Override
		public Object getItem(int arg0) {

			return dataSet.get(arg0);
		}

		@Override
		public long getItemId(int arg0) {

			return dataSet.indexOf(dataSet.get(arg0));
		}

		@Override
		public View getView(final int position, View convertView, ViewGroup arg2) {
			final ViewHolder holder;

			if (convertView == null) {

				LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
				convertView = inflater.inflate(R.layout.rss_item_list_row, null);

				holder = new ViewHolder();
				holder.page_url = (CustomTextView) convertView.findViewById(R.id.page_url);
				holder.title = (CustomTextView) convertView.findViewById(R.id.title);
				holder.pub_date = (CustomTextView) convertView.findViewById(R.id.pub_date);
				holder.link = (CustomTextView) convertView.findViewById(R.id.link);
				holder.imageWebView = (WebView) convertView.findViewById(R.id.imageWebView);

				convertView.setTag(holder);

			} else {
				holder = (ViewHolder) convertView.getTag();
			}

			try {

				holder.title.setText(dataSet.get(position).title);
				holder.page_url.setText(dataSet.get(position).url);
				holder.pub_date.setText(dataSet.get(position).publishedDate);
				
				//holder.link.setText(dataSet.get(position).link);

				holder.link.setText("Read More");

				
				try {

					System.out.println("trending link============"+dataSet.get(position).link);

					holder.imageWebView	.loadUrl(dataSet.get(position).link);

				} catch (Error e) {
					
					pDialog.dismiss();
				} catch (Exception e) {
					// TODO: handle exception
					e.printStackTrace();
					pDialog.dismiss();
				}

				// aquery.id(holder.img).image(
				// rssItemList.get(position).get(TAG_IMAGE), true, true,
				// 0, R.drawable.logo,
				// aquery.getCachedImage(R.drawable.logo), AQuery.FADE_IN);

			} catch (Exception e) {
				e.printStackTrace();
			}

			return convertView;
		}

	}

}
