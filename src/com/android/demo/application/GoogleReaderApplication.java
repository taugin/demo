package com.android.demo.application;

import java.util.HashMap;

import com.android.demo.reader.CustomerHttpClient;
import com.android.demo.reader.GoogleReaderClient;

import android.app.Application;
import android.util.Log;

public class GoogleReaderApplication extends Application {

	private final static String TAG = "GoogleReaderApplication";
	private HashMap<String, GoogleReaderClient> mClient = null;	
	private String mCurrentUserEmail = null;
	@Override
	public void onCreate() {
		super.onCreate();
		mClient = new HashMap<String, GoogleReaderClient>();
		CustomerHttpClient.getHttpClient();//init the HttpClient object
		Log.d(TAG, "onCreate~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~");
	}
	
	@Override
	public void onTerminate() {
		super.onTerminate();
		CustomerHttpClient.shutdownHttpClient();
		Log.d(TAG, "onTerminate~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~");
	}
	
	public GoogleReaderClient getStoredClient(String userEmail){
		Log.d(TAG, "size = " + mClient.size());
		if(mClient.containsKey(userEmail)){
			return mClient.get(userEmail);
		}
		return null;
	}
	public void storeClient(String userEmail, GoogleReaderClient client){
		Log.d(TAG, " setUserInfo userName = " + client.getUserInfo().userName);
		//getSaved userinfo
		if(mClient.containsKey(userEmail)){
			return ;
		}
		mClient.put(userEmail, client);
	}
	public void setCurrentUser(String userEmail){
		Log.d(TAG, "setCurrentUser = " + userEmail);
		Log.d(TAG, "mClient = " + mClient);
		mCurrentUserEmail = userEmail;
	}
	public String getCurrentUser(){
		Log.d(TAG, "getCurrentUser");
		Log.d(TAG, "mClient = " + mClient);
		return mCurrentUserEmail;
	}
}
