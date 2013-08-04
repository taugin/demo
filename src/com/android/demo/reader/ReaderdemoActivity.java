package com.android.demo.reader;

import java.net.URL;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.android.demo.R;
import com.android.demo.application.GoogleReaderApplication;
import com.android.demo.reader.info.UserInfo;

public class ReaderdemoActivity extends Activity implements OnClickListener{

	private static final String TAG = "AccountTestActivity";
	private AccountManager mAccountManager;
	private String mAuth;
	private static final int REQUEST_CODE_ACCOUNT_CHOOSEER = 0;
	private static final int REQUEST_CODE_AUTHENTICATE = 1;
	private static final int REQUEST_CODE_AUTHENTICATE_COMPLETE = 2;
	private static final String KEY_NAME = "name";
	private static final String KEY_TYPE = "type";
	private static final String KEY_AUTH = "auth";
	private Account mAccount;
	private WebView mWebView;
	private View mReturnTop;
	private ProgressBar mProgressBar;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		requestWindowFeature(Window.FEATURE_INDETERMINATE_PROGRESS);
		super.onCreate(savedInstanceState);
		setContentView(R.layout.display_result);
		mWebView = (WebView) findViewById(R.id.display_result);
		mWebView.setWebChromeClient(mWebChromeClient);
		mWebView.setWebViewClient(mWebViewClient);
		mWebView.getSettings().setCacheMode(WebSettings.LOAD_NORMAL);
		mWebView.setOnClickListener(this);
		mReturnTop = findViewById(R.id.return_top);
		mProgressBar = (ProgressBar) findViewById(R.id.load_progress);
		
		GoogleReaderApplication app = (GoogleReaderApplication) getApplication();
		String user = app.getCurrentUser();
		Log.d(TAG, "userEmail = " + user);
		GoogleReaderClient client = app.getStoredClient(user);
		Log.d(TAG, "client = " + client);
		if(user == null || client == null){
			SharedPreferences p = getSharedPreferences("GoogleReaderClient", MODE_PRIVATE);
			String userEmail = p.getString("userEmail", null);
			String userType = p.getString("userType", null);
//			String auth = p.getString("userAuth", null);
			Intent intent = new Intent(this, AuthenticationActivity.class);
			intent.putExtra(KEY_NAME, userEmail);
			intent.putExtra(KEY_TYPE, userType);
//			intent.putExtra(KEY_AUTH, auth);
			startActivityForResult(intent, REQUEST_CODE_AUTHENTICATE_COMPLETE);
		}else{
			if(client != null){
				new TaskAuth().execute("feed");
			}
		}
	}
	
	@Override
	public void onBackPressed() {
		if(mWebView.canGoBack()){
			mWebView.goBackOrForward(-1);
		}else{
			super.onBackPressed();
		}		
	}

	@Override
	protected void onResume() {
		super.onResume();
	}
	private WebChromeClient mWebChromeClient = new WebChromeClient(){

		@Override
		public void onProgressChanged(WebView view, int newProgress) {
			Log.d(TAG, "newProgress = " + newProgress);
			setProgress(newProgress);
			mProgressBar.setProgress(newProgress);
			if(mProgressBar.getProgress() >= 100){
				mProgressBar.setVisibility(View.GONE);
			}
		}

		@Override
		public void onReceivedTitle(WebView view, String title) {
			setTitle(title);
		}
		
	};
	
	private WebViewClient mWebViewClient = new WebViewClient(){

		@Override
		public boolean shouldOverrideUrlLoading(WebView view, String url) {
			Log.d(TAG, "URL = " + url);
			url.replaceAll("'?'", "%3F");
			if(url.startsWith("feed/")){
				new TaskAuth().execute("onefeed", url);
				return true;
			}
			return super.shouldOverrideUrlLoading(view, url);
		}
		
	};
	class TaskAuth extends AsyncTask<String, Void, Void>{

		@Override
		protected Void doInBackground(String... params) {
			String auth = null;
//				GoogleReaderClient client = new GoogleReaderClient(ReaderdemoActivity.this, mAuth);
//				GoogleReaderApplication app = (GoogleReaderApplication)getApplication();
//				app.storeClient(mAccount.name, client);
//				app.setCurrentUser(mAccount.name);
			GoogleReaderApplication app = (GoogleReaderApplication)getApplication();
			String user =  app.getCurrentUser();
			GoogleReaderClient client = app.getStoredClient(user);
			Log.d(TAG, "user = " + user + " , client = " + client);
			if(client == null){
				return null;
			}
			String result = null;
			if(params[0].equals("")|| params[0].equalsIgnoreCase("userinfo")){
				UserInfo info = client.getUserInfo();
				result = info.toString();
			}
			if(params[0].equalsIgnoreCase("item")){
				result = client.listAllNews();
			}
			if(params[0].equalsIgnoreCase("feed")){
				result = client.listGReaderFeeds();
			}
			if(params[0].equalsIgnoreCase("onefeed")){
				String url = params[1].substring("feed/".length());
				result = client.listFeedItem(url);
			}
			final String temp = result;
			mWebView.post(new Runnable() {
				
				@Override
				public void run() {
					// TODO Auto-generated method stub
					mWebView.loadDataWithBaseURL(null, temp, "text/html", "utf-8", null);
					mProgressBar.setVisibility(View.VISIBLE);
//						mWebView.loadData(temp, "text/html", "utf-8");
				}
			});
			return null;
		}

		@Override
		protected void onPostExecute(Void result) {
			setProgressBarIndeterminateVisibility(false);
		}
		private UserInfo getUserInfo(){
			UserInfo user = null;
			
			return user;
		}
		
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		Log.d(TAG, "requestCode = " + requestCode + " , resultCode = " + requestCode + " , action = " + (data != null ? data.getAction() : null));
		String name = null;
		String type = null;
		if(requestCode == REQUEST_CODE_ACCOUNT_CHOOSEER){
			if(resultCode == Activity.RESULT_OK){
				if(data != null){
					name = data.getStringExtra(KEY_NAME);
					type = data.getStringExtra(KEY_TYPE);
					if(name != null && type != null){
						Log.d(TAG, "REQUEST_CODE_ACCOUNT_CHOOSEER");
						mAccount = new Account(name, type);
						new TaskAuth().execute("");
					}
				}
			}
		}else if(requestCode == REQUEST_CODE_AUTHENTICATE){
			if(resultCode == Activity.RESULT_OK){
				Log.d(TAG, "REQUEST_CODE_AUTHENTICATE----------RESULT_OK");
				Toast.makeText(this, "Authenticate!", Toast.LENGTH_SHORT).show();
				new TaskAuth().execute("");
			}else if(resultCode == Activity.RESULT_CANCELED){
				Log.d(TAG, "REQUEST_CODE_AUTHENTICATE----------RESULT_CANCELED");
				Toast.makeText(this, "No Authenticate!", Toast.LENGTH_SHORT).show();
				mWebView.loadData("Authenticate Failure !", "text/html", "utf-8");
			}
		}else if(requestCode == REQUEST_CODE_AUTHENTICATE_COMPLETE){
			GoogleReaderApplication app = (GoogleReaderApplication) getApplication();
			String user = app.getCurrentUser();
			Log.d(TAG, "userEmail = " + user);
			GoogleReaderClient client = app.getStoredClient(user);
			Log.d(TAG, "client = " + client);
			if(client != null){
				new TaskAuth().execute("feed");
			}
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		menu.add(0, 0, 0, "AddFeed");
		menu.add(0, 1, 0, "UserInfo");
		menu.add(0, 2, 0, "Feed");
		menu.add(0, 3, 0, "Item");
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		int id = item.getItemId();
		switch(id){
		case 0:
			Intent intent = new Intent(this, AddFeed.class);
			startActivity(intent);
			break;
		case 1:
			new TaskAuth().execute("userinfo");
			break;
		case 2:
			new TaskAuth().execute("feed");
			break;
		case 3:
			new TaskAuth().execute("item");
			break;
		default:
			break;
		}
		return true;
	}
	public void onClick(View view){
		int id = view.getId();
		Log.d(TAG,"onClick view = " + view);
		switch(id){
		case R.id.display_result:
			mReturnTop.setVisibility(View.VISIBLE);
			mHandler.postDelayed(new Timer(), 1000);
			break;
		case R.id.return_top:
			mWebView.scrollTo(0, 0);
			break;
		default:
			break;
		}
	}
	private class Timer implements Runnable{
		@Override
		public void run() {
			mReturnTop.setVisibility(View.GONE);
		}
	}
	Handler mHandler = new Handler();
}
