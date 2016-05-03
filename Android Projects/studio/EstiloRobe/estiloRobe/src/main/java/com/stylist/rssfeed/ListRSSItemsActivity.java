package com.stylist.rssfeed;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import com.constants.CustomTextView;
import com.constants.Utils;
import com.constants.WebImageLoader;
import com.stylist.R;

import android.app.Activity;
import android.app.ListActivity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

public class ListRSSItemsActivity extends ListActivity {

	// Progress Dialog
	private ProgressDialog pDialog;

	// Array list for list view
	ArrayList<HashMap<String, String>> rssItemList = new ArrayList<HashMap<String, String>>();

	RSSParser rssParser = new RSSParser();

	RssImage adapter;

	// TextView h_home, h_back;

	List<RSSItem> rssItems = new ArrayList<RSSItem>();

	RSSFeed rssFeed;

	private static String TAG_TITLE = "title";
	private static String TAG_LINK = "link";
	private static String TAG_DESRIPTION = "description";
	private static String TAG_PUB_DATE = "pubDate";
	private static String TAG_GUID = "guid";
	private static String TAG_IMAGE = "image";// not used
	public static Activity ctx;
	WebImageLoader imageLoader;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.rss_item_list);
		ctx = ListRSSItemsActivity.this;
		backClick();
		CustomTextView headerName=(CustomTextView)findViewById(R.id.headerName);
		imageLoader = new WebImageLoader(this);
		
		final Intent i = getIntent();
		System.out.println("site_id========================" + i.getStringExtra("id"));
		// SQLite Row id
		Integer fromList=i.getIntExtra("fromList",0);

		headerName.setText(i.getStringExtra("title"));

		if(fromList==0)
		{
		//setHeading(i.getStringExtra("title"));
		Integer site_id = Integer.parseInt(i.getStringExtra("id"));
		System.out.println("site_id========================" + site_id);
		// Getting Single website from SQLite
		RSSDatabaseHandler rssDB = new RSSDatabaseHandler(getApplicationContext());
		WebSite site = rssDB.getSite(site_id);
		String rss_link = site.getRSSLink();
		/**
		 * Calling a backgroung thread will loads recent articles of a website
		 * 
		 * @param rss
		 *            url of website
		 */
		new loadRSSFeedItems().execute(rss_link);
		}
		
		// selecting single ListView item
		ListView lv = getListView();

		// Launching new screen on Selecting Single ListItem
	lv.setOnItemClickListener(new OnItemClickListener() {

		public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
			Intent in = new Intent(getApplicationContext(), DisPlayWebPageActivity.class);

			// getting page url
			String page_url = ((TextView) view.findViewById(R.id.page_url)).getText().toString();
			//String title = ((TextView) view.findViewById(R.id.title)).getText().toString();
			in.putExtra("page_url", page_url);
			in.putExtra("title", i.getStringExtra("title"));
			startActivity(in);
			// Intent browserIntent = new Intent(Intent.ACTION_VIEW,
			// Uri.parse(page_url));
			// startActivity(Intent.createChooser(browserIntent,
			// "Chose browser"));
			// startActivity(browserIntent);
		}
	});


	}
	protected void setHeading(String heading) {
		TextView headerTitle = (TextView) this.findViewById(R.id.headerName);
		headerTitle.setText(heading.toUpperCase());

	}

	protected void backClick() {
		ImageView backBtn = (ImageView) this.findViewById(R.id.backButtonImage);
		backBtn.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				finishActivity();

			}
		});
	}

	
	
	protected void finishActivity() {

		Utils.hideSoftKeyboard(this);
		this.finish();
		overridePendingTransition(R.anim.exit_animation_enter_from_right,
				R.anim.exit_animation_leave_to_right);
	}
	
	
	
	
	
	
	
	/**
	 * Background Async Task to get RSS Feed Items data from URL
	 */
	class loadRSSFeedItems extends AsyncTask<String, String, String> {

		/**
		 * Before starting background thread Show Progress Dialog
		 */
		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			pDialog = new ProgressDialog(ListRSSItemsActivity.this);
			pDialog.setMessage("Loading recent articles...");
			pDialog.setIndeterminate(false);
			pDialog.setCancelable(false);
			pDialog.show();
		}

		/**
		 * getting all recent articles and showing them in listview
		 */
		@Override
		protected String doInBackground(String... args) {
			// rss link url
			String rss_url = args[0];

			// list of rss items
			rssItems = rssParser.getRSSFeedItems(rss_url);

			// looping through each item
			for (RSSItem item : rssItems) {
				// creating new HashMap
				HashMap<String, String> map = new HashMap<String, String>();

				// adding each child node to HashMap key => value
				map.put(TAG_TITLE, item.getTitle());
				map.put(TAG_LINK, item.getLink());
				map.put(TAG_PUB_DATE, item.getPubdate());

				map.put(TAG_IMAGE, item.get_image());// If you want parse the 51
														// date\

				System.out.println("Image Tag URL imagessssssssssssss:" + item.get_image());
				System.out.println("Image Tag title:" + item.getTitle());
				System.out.println("Image Tag link:" + item.getLink());
				System.out.println("Image Tag pub:" + item.getPubdate());
				System.out.println("Image Tag decp:" + item.getDescription());
				System.out.println("Image Tag guid:" + item.getGuid());

				String description = item.getDescription();
				// taking only 200 chars from description
				if (description.length() > 100) {
					description = description.substring(0, 97) + "..";
				}
				map.put(TAG_DESRIPTION, description);

				// adding HashList to ArrayList
				rssItemList.add(map);
			}

			// updating UI from Background Thread
			runOnUiThread(new Runnable() {
				public void run() {
					/**
					 * Updating parsed items into listview
					 */

					// aquery.image(TAG_IMAGE,true,true);
					System.out.println("");

					adapter = new RssImage(getApplicationContext(), rssItemList);

					// updating listview
					setListAdapter(adapter);
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

	private class RssImage extends BaseAdapter {

		Context context;
		ArrayList<HashMap<String, String>> rssItemList = new ArrayList<HashMap<String, String>>();

		public RssImage(Context context, ArrayList<HashMap<String, String>> rssItemList) {
			this.context = context;
			this.rssItemList = rssItemList;

		}

		private class ViewHolder {
			CustomTextView page_url, title, pub_date, link;

			WebView imgWebView;

			// CheckBox chk_id;
			//ImageView img;
			// boolean ischk;
		}

		@Override
		public int getCount() {

			return rssItemList.size();
		}

		@Override
		public Object getItem(int arg0) {

			return null;
		}

		@Override
		public long getItemId(int arg0) {

			return 0;
		}

		@Override
		public View getView(final int position, View convertView, ViewGroup arg2) {
			final ViewHolder holder;

			if (convertView == null) {

				LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
				convertView = inflater.inflate(R.layout.row_blooger, null);

				holder = new ViewHolder();
				holder.page_url = (CustomTextView) convertView.findViewById(R.id.page_url);
				holder.title = (CustomTextView) convertView.findViewById(R.id.title);
				holder.pub_date = (CustomTextView) convertView.findViewById(R.id.pub_date);
				holder.link = (CustomTextView) convertView.findViewById(R.id.link);
				holder.imgWebView = (WebView) convertView.findViewById(R.id.imageWebView);


				convertView.setTag(holder);

			} else {
				holder = (ViewHolder) convertView.getTag();
			}

			try {


				for (HashMap<String, String> map :
				rssItemList) {

					for ( String key : map.keySet() ) {
						System.out.println( "Key: "+key );

						System.out.println( "Value: "+map.get(key) );
					}

				}


				holder.title.setText(rssItemList.get(position).get(TAG_TITLE));
				holder.page_url.setText(rssItemList.get(position).get(TAG_LINK));
				holder.pub_date.setText(rssItemList.get(position).get(TAG_PUB_DATE));
				holder.link.setText("Read More");

				holder.imgWebView	.loadUrl(rssItemList.get(position).get("image"));

				Log.v("Image", rssItemList.get(position).get("image"));

			//	imageLoader.DisplayImage(rssItemList.get(position).get(TAG_IMAGE), holder.img);
				
			} catch (Exception e) {
				e.printStackTrace();
			}

			return convertView;
		}

	}

}
