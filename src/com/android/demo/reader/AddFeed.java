package com.android.demo.reader;

import com.android.demo.R;
import com.android.demo.application.GoogleReaderApplication;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;

public class AddFeed extends Activity {

	private static final String TAG = "AddFeed";
	private EditText mFeedUrl = null;
	private String mEmail = null;
	private GoogleReaderClient mGoogleReaderClient;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.add_feed);
		mFeedUrl = (EditText) findViewById(R.id.feed_url_input);
	}
	
	public void onClick(View view){
		final String url = mFeedUrl.getEditableText().toString();
		Log.d(TAG, "url = " + url);
		GoogleReaderApplication app = (GoogleReaderApplication)getApplication();
		mEmail = app.getCurrentUser();
		mGoogleReaderClient = ((GoogleReaderApplication)getApplication()).getStoredClient(mEmail);	
		Log.d(TAG, "email = " + mEmail);
		
		new Thread(){

			@Override
			public void run() {
				String token = mGoogleReaderClient.getToken();
				Log.d(TAG, "token = " + token);	
				if(url != null && !url.trim().equals("")){
					mGoogleReaderClient.quickAddFeed(url, token);
				}
			}
			
		}.start();
	}
}
