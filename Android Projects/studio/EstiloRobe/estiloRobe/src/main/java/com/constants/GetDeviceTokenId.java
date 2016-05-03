package com.constants;

import java.io.IOException;

import com.google.android.gms.gcm.GoogleCloudMessaging;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

public class GetDeviceTokenId  extends AsyncTask<Bundle, Void, String>{
	GoogleCloudMessaging gcm;
	 String regId;
	 Context context;
	 AsynctaskListner callbacklistner;	  
	 public GetDeviceTokenId(Context context,AsynctaskListner callbacklistner) {
		// TODO Auto-generated constructor stub
		this.context=context;
		this.callbacklistner=callbacklistner;
	}
	@Override
	protected String doInBackground(Bundle... arg0) {
		System.out.println("GetDeviceTokenId");
		   String msg = "";
		    try {
		     if (gcm == null) {
		      gcm = GoogleCloudMessaging.getInstance(context);
		     }
		     regId = gcm.register("437115293798");
		     Log.d("RegisterActivity", "registerInBackground - regId: "
		       + regId);
		     msg =regId;
		    } catch (Error error)
			{
				error.printStackTrace();
			}catch (IOException ex) {
		     msg = "Error :" + ex.getMessage();
		     Log.d("RegisterActivity", "Error: " + msg);
		    }
		    Log.d("RegisterActivity", "AsyncTask completed: " + msg);
		    return msg;
	}

	
	
	@Override
	protected void onPostExecute(String result) {
		super.onPostExecute(result);
		Utils.write("result     " + result);
	   
	    if (callbacklistner != null)
			callbacklistner.AsynctaskResult(result, true);

	}

	
	
	public interface AsynctaskListner {
		public void AsynctaskResult(String arr, boolean action);
	}
	/*GoogleCloudMessaging gcm;
	 String regId;
	 Context context;
	 public GetDeviceTokenId(Context context) {
		// TODO Auto-generated constructor stub
		this.context=context;
	}
	private void registerInBackground() {

		  new AsyncTask<String, String, String>() {

		   @Override
		   protected String doInBackground(String... params) {
		    String msg = "";
		    try {
		     if (gcm == null) {
		      gcm = GoogleCloudMessaging.getInstance(context);
		     }
		     regId = gcm.register("437115293798");
		     Log.d("RegisterActivity", "registerInBackground - regId: "
		       + regId);
		     msg = "Device registered, registration ID=" + regId;
		     Constants.googledeviceid=regId;
		     //     storeRegistrationId(context, regId);
		    } catch (IOException ex) {
		     msg = "Error :" + ex.getMessage();
		     Log.d("RegisterActivity", "Error: " + msg);
		    }
		    Log.d("RegisterActivity", "AsyncTask completed: " + msg);
		    return msg;
		   }

		   @Override
		   protected void onPostExecute(String result) {
		    super.onPostExecute(result);
		    System.out.println("result    "+result);
		    
		    GetDeviceTokenIdResult
		    
		
		   // checkUserSession();
		    
		   }

		  }.execute(null, null, null);

		 }
	public interface AsyncGetDeviceTokenIdResult {
		public void GetDeviceTokenIdResult(String arr, boolean flag);

	}*/
	
}
