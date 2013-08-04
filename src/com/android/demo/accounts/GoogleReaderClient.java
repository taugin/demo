package com.android.demo.accounts;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.Charset;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpGet;

import android.content.Context;
import android.util.Log;
//http://www.google.com/reader/atom/feed/http://www.naivix.com/rss.xml?n=17&ck=10101010=user/weiliuzhao/state/com.google/read
public final class GoogleReaderClient {
	private static final String TAG = "GoogleReaderClient";
	private static final String BASE_URL = "http://www.google.com/reader/";
	private static final String ATOM = "atom/";
	private static final String TEMP = "http://www.google.com/reader/atom/user/0/pref/com.google/subscriptions";
	
	public static final int HTTP_REQUEST_TIMEOUT_MS = 30 * 1000;
	
	
	private String mAuthorization = null;
	private Context mContext = null;
	private String mCurrentUser = null;
	public GoogleReaderClient(Context context, String authorization, String currentUser){
		mContext = context;
		mAuthorization = authorization;
		mCurrentUser = currentUser;
	}
	
	public String listAllNews(){
		String allNewUrl = BASE_URL + ATOM + "feed/http://www.naivix.com/rss.xml?n=1&ck=10101010=user/weiliuzhao/state/com.google/read";
		Log.d(TAG, "allNewUrl = " + allNewUrl);
		InputStream in = CustomerHttpClient.executeGet(allNewUrl, mAuthorization);
		String temp = writeResultToFile(in);
		try {
			in.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return temp;
	}
	public void listGReaderFeeds(){
	}
	private String writeResultToFile(InputStream in){
		byte buf[] = new byte[1024];
		String temp = "";
		int len = 0;
		int total = 0;
		try {
			FileOutputStream f = mContext.openFileOutput("GetResult.html", 0);
			while((len = in.read(buf)) > 0){	
				temp += new String(buf);
				total += len;
			}
			Log.d(TAG, temp);
			temp = temp.replaceAll("&lt;", "<");
			temp = temp.replaceAll("&gt;", ">");
			temp = temp.replaceAll("&amp;", "&");
			f.write(temp.getBytes());
			Log.d(TAG, "TEMP LEN = " + total);
			f.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return temp;
	}
	
}
