package com.stylist.rssfeed;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.UnknownHostException;
import java.util.ArrayList;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.client.utils.URLEncodedUtils;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;

public class SimpleHTTPConnection {
	
	
	private static final String TAG = "SimpleHTTPConnection";
	private static final String ERROR = "Simple HTTP Connection Error";
	
	public static String sendByGET (String url)
	{
		InputStream is;
		StringBuilder sb;
		String result = ERROR;
		
		try
		{
			HttpClient httpclient = new DefaultHttpClient ();
			HttpGet httpget = new HttpGet (url);
			HttpResponse response = httpclient.execute (httpget);
			HttpEntity entity = response.getEntity ();
			is = entity.getContent ();
			
			BufferedReader reader = new BufferedReader (new InputStreamReader (is, "iso-8859-1"), 20);
			sb = new StringBuilder ();
			sb.append (reader.readLine ());
			String line = null;
			while ((line = reader.readLine ()) != null) 
			{
				sb.append (line);
				
			}
			is.close ();
			result = sb.toString ();
		} 
		catch (UnsupportedEncodingException e) 
		{
			Log.i (TAG, e.getMessage ());
			e.printStackTrace ();
		} 
		catch (ClientProtocolException e) 
		{
			Log.i (TAG, e.getMessage ());
			e.printStackTrace ();
		} 
		catch (IOException e) 
		{
			Log.i (TAG, e.getMessage ());
			e.printStackTrace ();
		}
		
		return result;
	}
	
	public static boolean checkNetworkConnection(Context context){
		final ConnectivityManager connMgr = (ConnectivityManager) context
				.getSystemService(Context.CONNECTIVITY_SERVICE);

		final android.net.NetworkInfo wifi = connMgr
				.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
		// final android.net.NetworkInfo mobile
		// =connMgr.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
		ConnectivityManager conMgr = (ConnectivityManager) context
				.getSystemService(Context.CONNECTIVITY_SERVICE);

		NetworkInfo info = conMgr.getActiveNetworkInfo();

		// System.out.println("wifi"+wifi.isAvailable());
		// System.out.println("info"+info);

		if (info != null && info.isConnected()) {
			return true;
		} else {
			if (wifi.isAvailable()) // ||mobile.isAvailable()
				return true;
			else
				return false;
		}
	}

	public static String sendByPOST (HttpPost httppost)//(String url, ArrayList<NameValuePair> data)
	{
		InputStream is;
		StringBuilder sb;
		String result = ERROR;		
		try
		{
			HttpClient httpclient = new DefaultHttpClient ();
//			HttpPost httppost = new HttpPost (url);
//			httppost.setEntity (new UrlEncodedFormEntity(data));
			HttpResponse response = httpclient.execute (httppost);
			HttpEntity entity = response.getEntity ();
			
			is = entity.getContent ();
			
			BufferedReader reader = new BufferedReader (new InputStreamReader (is, "iso-8859-1"), 20);
			sb = new StringBuilder ();
			sb.append (reader.readLine ());
			String line = null;
			while ((line = reader.readLine ()) != null) 
			{
				sb.append (line);
				
			}
			is.close ();
			result = sb.toString ();
		} 
		catch (UnsupportedEncodingException e) 
		{
			Log.i (TAG, e.getMessage ());
			e.printStackTrace ();
		} 
		catch (ClientProtocolException e) 
		{
			Log.i (TAG, e.getMessage ());
			e.printStackTrace ();
		} 
		catch (IOException e) 
		{
			Log.i (TAG, e.getMessage ());
			e.printStackTrace ();
		}
		
		return result;
	}
	// With image upload using put
	public static String sendByPUT (HttpPut httpput)//(String url, ArrayList<NameValuePair> data)
	{
		InputStream is;
		StringBuilder sb;
		String result = ERROR;		
		try
		{
			HttpClient httpclient = new DefaultHttpClient ();
//			HttpPost httppost = new HttpPost (url);
//			httppost.setEntity (new UrlEncodedFormEntity(data));
			HttpResponse response = httpclient.execute (httpput);
			HttpEntity entity = response.getEntity ();
			
			is = entity.getContent ();
			
			BufferedReader reader = new BufferedReader (new InputStreamReader (is, "iso-8859-1"), 20);
			sb = new StringBuilder ();
			sb.append (reader.readLine ());
			String line = null;
			while ((line = reader.readLine ()) != null) 
			{
				sb.append (line);
				
			}
			is.close ();
			result = sb.toString ();
		} 
		catch (UnsupportedEncodingException e) 
		{
			Log.i (TAG, e.getMessage ());
			e.printStackTrace ();
		} 
		catch (ClientProtocolException e) 
		{
			Log.i (TAG, e.getMessage ());
			e.printStackTrace ();
		} 
		catch (IOException e) 
		{
			Log.i (TAG, e.getMessage ());
			e.printStackTrace ();
		}
		
		return result;
	}
	
	public static String sendByPOST(String url, ArrayList<NameValuePair> data)
	{
		InputStream is;
		StringBuilder sb;
		String result = ERROR;		
		try
		{
			
			//System.out.println("sendByPOST URL="+url);
			
			HttpClient httpclient = new DefaultHttpClient();
			HttpPost httppost = new HttpPost (url);
			httppost.setEntity (new UrlEncodedFormEntity(data));
			HttpResponse response = httpclient.execute (httppost);
			HttpEntity entity = response.getEntity ();
			
			is = entity.getContent ();
			
			BufferedReader reader = new BufferedReader (new InputStreamReader (is, "iso-8859-1"), 20);
			sb = new StringBuilder ();
			sb.append (reader.readLine ());
			String line = null;
			while ((line = reader.readLine ()) != null) 
			{
				sb.append (line);
				
			}
			is.close ();
			result = sb.toString ();
		} catch (IllegalStateException e) {
			
			e.printStackTrace ();
			return null;
		} catch (UnknownHostException e) {
			
			e.printStackTrace ();
			return null;
		}
		catch (UnsupportedEncodingException e) 
		{
			e.printStackTrace ();
			return null;
		} 
		catch (ClientProtocolException e) 
		{
			e.printStackTrace ();
			return null;
		} 
		catch (IOException e) 
		{
			e.printStackTrace ();
			return null;
		}
		catch (Exception e) 
		{
			e.printStackTrace ();
			return null;
		}
		
		return result;
	}
	
	public static String sendByPUT(String url, ArrayList<NameValuePair> data){
		InputStream is;
		StringBuilder sb;
		String result = ERROR;
		
		try {
			HttpClient httpclient = new DefaultHttpClient();
			HttpPut httpput= new HttpPut(url);
			httpput.setEntity (new UrlEncodedFormEntity (data));
			HttpResponse response = httpclient.execute (httpput);
			HttpEntity entity = response.getEntity ();
			is = entity.getContent ();
			
			BufferedReader reader = new BufferedReader (new InputStreamReader (is, "iso-8859-1"), 20);
			sb = new StringBuilder ();
			sb.append (reader.readLine ());
			String line = null;
			while ((line = reader.readLine ()) != null) 
			{
				sb.append (line);
				
			}
			is.close ();
			result = sb.toString ();
			
		} 
		catch (UnsupportedEncodingException e) 
		{
			Log.i (TAG, e.getMessage ());
			e.printStackTrace ();
		} 
		catch (ClientProtocolException e) 
		{
			Log.i (TAG, e.getMessage ());
			e.printStackTrace ();
		} 
		catch (IOException e) 
		{
			Log.i (TAG, e.getMessage ());
			e.printStackTrace ();
		}
		
		return result;
		
	}
	
	public static ArrayList<NameValuePair> generateParams (String[] keys, String[] values)
	{
		ArrayList<NameValuePair> params = new ArrayList<NameValuePair> ();
		for (int i = 0; i < keys.length; i++)
		{
			params.add (new BasicNameValuePair (keys[i], values[i]));
		}
		
		return params;
	}
	
	public static String buildGetUrl (String url, String[] keys, String[] values)
	{
		if (!url.endsWith ("?"))
			url += "?";
		url += URLEncodedUtils.format (generateParams (keys, values), "utf-8");
		
		return url;
	}

}
