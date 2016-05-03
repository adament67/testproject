package com.stylist.rssfeed;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;

import com.constants.Constants;
import com.stylist.R;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.ContextMenu;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.SimpleAdapter;
import android.widget.TextView;

//import com.poolbutler.Constants;
//import com.poolbutler.R;
//import com.poolbutler.SimpleHTTPConnection;
//import com.poolbutler.table.NewsHome;
//import com.poolbutler.table.ReviewOrder;
//import com.poolbutler.table.TableHome;

public class AndroidRSSReaderApplicationActivity extends Activity {
	// Progress Dialog
	private ProgressDialog pDialog;
	ArrayList<String> data = new ArrayList<String>();
	// Array list for list view
	ArrayList<HashMap<String, String>> rssFeedList;
	RSSParser rssParser = new RSSParser();
	RSSFeed rssFeed;
//	TextView h_home, h_back;
	// array to trace sqlite ids
	String[] sqliteIds;
	public static String TAG_ID = "id";
	public static String TAG_TITLE = "title";
	public static String TAG_LINK = "link";
	 String url1="http://rss.cnn.com/rss/cnn_topstories.rss";
	 String url2="http://news.yahoo.com/rss";
	// List view
	ListView lv;
	public static Activity ctx;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.rss_site_list); 

		// Hashmap for ListView
		ctx = AndroidRSSReaderApplicationActivity.this;
		rssFeedList = new ArrayList<HashMap<String, String>>();
//		findViewById(R.id.h_text).setVisibility(View.GONE);
//		findViewById(R.id.h_waiter).setVisibility(View.GONE);
		 data.add(url1);
		 data.add(url2);
		//
		 if (data.size()>0) {
		 new loadRSSFeed().execute(data.get(0));
		 }
//		new GetUrlData(AndroidRSSReaderApplicationActivity.this).execute();

		/**
		 * Calling a background thread which will load web sites stored in
		 * SQLite database
		 * */

//		h_home = (TextView) findViewById(R.id.h_home);
//		h_back = (TextView) findViewById(R.id.h_back);

//		h_home.setOnClickListener(new View.OnClickListener() {
//
//			@Override
//			public void onClick(View v) {
////				Intent i1 = new Intent(AndroidRSSReaderApplicationActivity.this, TableHome.class);
////
////				startActivity(i1);
//				finish();
//			}
//		});
//
//		h_back.setOnClickListener(new View.OnClickListener() {
//
//			@Override
//			public void onClick(View v) {
//				Intent i1 = new Intent(AndroidRSSReaderApplicationActivity.this, NewsHome.class);
//
//				startActivity(i1);
//				finish();
//			}
//		});

		// selecting single ListView item
		lv = (ListView) findViewById(R.id.list);

		// Launching new screen on Selecting Single ListItem
		lv.setOnItemClickListener(new OnItemClickListener() {

			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				// getting values from selected ListItem
				String sqlite_id = ((TextView) view
						.findViewById(R.id.sqlite_id)).getText().toString();

				// Starting new intent
				Intent in = new Intent(getApplicationContext(),
						ListRSSItemsActivity.class);
				// passing sqlite row id
				in.putExtra(TAG_ID, sqlite_id);
				startActivity(in);
			}
		});

		/**
		 * Add new website button click event listener
		 * */

		// btnAddSite.setOnClickListener(new View.OnClickListener() {
		//
		// public void onClick(View v) {
		// Intent i = new Intent(getApplicationContext(),
		// AddNewSiteActivity.class);
		// // starting new activity and expecting some response back
		// // depending on the result will decide whether new website is
		// // added to SQLite database or not
		// startActivityForResult(i, 100);
		// }
		// });
	}

	/**
	 * Response from AddNewSiteActivity.java if response is 100 means new site
	 * is added to sqlite reload this activity again to show newly added website
	 * in listview
	 * */
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
	 * */
	@Override
	public void onCreateContextMenu(ContextMenu menu, View v,
			ContextMenuInfo menuInfo) {
		if (v.getId() == R.id.list) {
			menu.setHeaderTitle("Delete");
			menu.add(Menu.NONE, 0, 0, "Delete Feed");
		}
	}

	/**
	 * Responding to context menu selected option
	 * */
	@Override
	public boolean onContextItemSelected(MenuItem item) {
		AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) item
				.getMenuInfo();
		int menuItemIndex = item.getItemId();
		// check for selected option
		if (menuItemIndex == 0) {
			// user selected delete
			// delete the feed
			RSSDatabaseHandler rssDb = new RSSDatabaseHandler(
					getApplicationContext());
			WebSite site = new WebSite();
			site.setId(Integer.parseInt(sqliteIds[info.position]));
			rssDb.deleteSite(site);
			// reloading same activity again
			Intent intent = getIntent();
			finish();
			startActivity(intent);
		}

		return true;
	}

//	private class GetUrlData extends AsyncTask<String, Void, String> {
//		private Context context;
//
//		GetUrlData(Context ctx) {
//			context = ctx;
//			pDialog = new ProgressDialog(context);
//		}
//
//		@Override
//		protected void onPreExecute() {
//
//			super.onPreExecute();
//
//			pDialog.setMessage("Please wait...");
//			pDialog.setCancelable(false);
//			pDialog.show();
//
//		}
//
//		@Override
//		protected String doInBackground(String... params) {
////System.out.println("111111111111111      "+Constants.getPrefrence(AndroidRSSReaderApplicationActivity.this, "local_url")+ Constants.get_url_data);
//			return SimpleHTTPConnection.sendByGET(Constants.getPrefrence(AndroidRSSReaderApplicationActivity.this, "local_url")+ Constants.get_url_data);
//		}
//
//		@Override
//		protected void onPostExecute(String result) {
//
//			System.out.println("result " + result);
//			pDialog.dismiss();
//
//			try {
//
//				JSONArray news_rss = new JSONArray(result);
//				// JSONArray subclasses = obj.getJSONArray("subclasses");
//				for (int i = 0; i < news_rss.length(); i++) {
//					data.add(news_rss.getJSONObject(i).getString("URL"));
//				}
//				System.out.println(">>>>>>DATA >>>>" + data);
//
//				if (data.size() > 0) {
//					new loadRSSFeed().execute(data.get(0));
//				}
//
//			} catch (JSONException e) {
//				e.printStackTrace();
//
//			} catch (Exception e) {
//			}
//
//		}
//	}

	class loadRSSFeed extends AsyncTask<String, String, String> {

		/**
		 * Before starting background thread Show Progress Dialog
		 * */
		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			pDialog = new ProgressDialog(
					AndroidRSSReaderApplicationActivity.this);
			pDialog.setMessage("Fetching RSS Information ...");
			pDialog.setIndeterminate(false);
			pDialog.setCancelable(false);
			pDialog.show();
		}

		/**
		 * getting Inbox JSON
		 * */
		@Override
		protected String doInBackground(String... args) {
			String url = args[0];
			rssFeed = rssParser.getRSSFeed(url);
			Log.d("rssFeed", " " + rssFeed);
			if (rssFeed != null) {
				Log.e("RSS URL",
						rssFeed.getTitle() + "" + rssFeed.getLink() + ""
								+ rssFeed.getDescription() + ""
								+ rssFeed.getLanguage());
				RSSDatabaseHandler rssDb = new RSSDatabaseHandler(
						getApplicationContext());
				WebSite site = new WebSite(rssFeed.getTitle(),
						rssFeed.getLink(), rssFeed.getRSSLink(),
						rssFeed.getDescription(), rssFeed.get_image());
				rssDb.addSite(site);

				// Intent i = getIntent();
				// // send result code 100 to notify about product update
				// setResult(100, i);
				// finish();
			} else {
				// updating UI from Background Thread
				runOnUiThread(new Runnable() {
					public void run() {
						// lblMessage.setText("Rss url not found. Please check the url or try again");
					}
				});
			}
			return null;
		}

		/**
		 * After completing background task Dismiss the progress dialog
		 * **/
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
	 * */
	class loadStoreSites extends AsyncTask<String, String, String> {

		/**
		 * Before starting background thread Show Progress Dialog
		 * */
		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			pDialog = new ProgressDialog(
					AndroidRSSReaderApplicationActivity.this);
			pDialog.setMessage("Loading websites ...");
			pDialog.setIndeterminate(false);
			pDialog.setCancelable(false);
			pDialog.show();
		}

		/**
		 * getting all stored website from SQLite
		 * */
		@Override
		protected String doInBackground(String... args) {
			// updating UI from Background Thread
			runOnUiThread(new Runnable() {
				public void run() {
					RSSDatabaseHandler rssDb = new RSSDatabaseHandler(
							getApplicationContext());

					// listing all websites from SQLite
					List<WebSite> siteList = rssDb.getAllSites();

					sqliteIds = new String[siteList.size()];

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
						sqliteIds[i] = s.getId().toString();
					}
					/**
					 * Updating list view with websites
					 * */
					ListAdapter adapter = new SimpleAdapter(
							AndroidRSSReaderApplicationActivity.this,
							rssFeedList, R.layout.rss_site_list_row,
							new String[] { TAG_ID, TAG_TITLE, TAG_LINK },
							new int[] { R.id.sqlite_id, R.id.title, R.id.link });
					// updating listview
					lv.setAdapter(adapter);
					registerForContextMenu(lv);

				}
			});
			return null;
		}

		/**
		 * After completing background task Dismiss the progress dialog
		 * **/
		protected void onPostExecute(String args) {
			// dismiss the dialog after getting all products
			pDialog.dismiss();

		}

	}

}