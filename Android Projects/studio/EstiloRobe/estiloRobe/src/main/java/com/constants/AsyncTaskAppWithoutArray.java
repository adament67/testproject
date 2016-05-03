package com.constants;

import java.util.Map;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;

import com.parse.FunctionCallback;
import com.parse.ParseCloud;
import com.parse.ParseException;

public class AsyncTaskAppWithoutArray {
	String url = null;
	Context context;
	AsynctaskListnerwithoutArray callBackListner;
	Map<String, Object> bundleData;
	public ProgressDialog progressDialog;
	boolean dialogShow;

	public AsyncTaskAppWithoutArray(boolean dialogShow,
			Map<String, Object> bundleData,
			AsynctaskListnerwithoutArray callBackListner, Context context,
			String url) {
		Utils.write("action         " + url);
		this.callBackListner = callBackListner;
		this.context = context;
		this.url = url;
		this.bundleData = bundleData;
		this.dialogShow = dialogShow;

		if (dialogShow) {
			if (progressDialog != null) {
				dismissDialog();
			}
			progressDialog = new ProgressDialog(context);
			progressDialog.setCancelable(false);
			progressDialog.setMessage("Please Wait...");
			if (!progressDialog.isShowing()) {
				progressDialog.show();
			}
		}
		getdatafromserver();
	}

	protected void getdatafromserver() {
		if (Utils.checkNetworkConnection(context)) {
			ParseCloud.callFunctionInBackground(url, bundleData,
					new FunctionCallback<String>() {
						@Override
						public void done(String res, ParseException e) {

							if (e == null) {
								try {
									onPostExecute(res);
								}catch (Error error)
								{
									error.printStackTrace();
								}
								catch (Exception e2) {
									e2.printStackTrace();
								}
							} else {
								e.printStackTrace();
							}
							dismissDialog();
						}
					});
		} else {
			dismissDialog();
			Utils.showToastShort(Constants.networkMsg, context);
		}
	};

	public interface AsynctaskListnerwithoutArray {
		public void AsynctaskResultwithoutArray(String arr, String url,
				Map<String, Object> par);

	}

	protected void onPostExecute(String result) {

		if (callBackListner != null)
			callBackListner.AsynctaskResultwithoutArray(result, url, bundleData);

		dismissDialog();
	}

	private void dismissDialog() {
		try {
			if (dialogShow) {
				progressDialog.dismiss();
			}

		} catch (Error error)
		{
			error.printStackTrace();
		}catch (Exception e) {
			e.printStackTrace();

		}
	}
}