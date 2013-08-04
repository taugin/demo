package com.android.demo.accounts;

import android.app.Activity;
import android.os.Bundle;
import android.view.Window;
import android.webkit.WebChromeClient;
import android.webkit.WebView;

import com.android.demo.R;

public class DisplayResult extends Activity {

	private WebView mWebView;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		requestWindowFeature(Window.FEATURE_PROGRESS);
		super.onCreate(savedInstanceState);
		setContentView(R.layout.display_result);
		mWebView = (WebView) findViewById(R.id.display_result);
		mWebView.setWebChromeClient(mWebChromeClient);
		setProgressBarIndeterminate(false);
		setProgress(0);
		
	}
	
	private WebChromeClient mWebChromeClient = new WebChromeClient(){

		@Override
		public void onProgressChanged(WebView view, int newProgress) {
			setProgress(newProgress);
		}

		@Override
		public void onReceivedTitle(WebView view, String title) {
			setTitle(title);
		}
		
	};
}
