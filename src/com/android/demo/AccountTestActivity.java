package com.android.demo;

import java.io.IOException;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.accounts.AuthenticatorDescription;
import android.accounts.AuthenticatorException;
import android.accounts.OperationCanceledException;
import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.view.Window;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.widget.Toast;

import com.android.demo.accounts.AccountChooser;
import com.android.demo.accounts.GoogleReaderClient;

public class AccountTestActivity extends Activity {

	private static final String TAG = "AccountTestActivity";
	private AccountManager mAccountManager;
	private String mAuth;
	private static final int REQUEST_CODE_ACCOUNT_CHOOSEER = 0;
	private static final int REQUEST_CODE_AUTHENTICATE = 1;
	private static final String KEY_NAME = "name";
	private static final String KEY_TYPE = "type";
	private Account mAccount;
	private WebView mWebView;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		requestWindowFeature(Window.FEATURE_INDETERMINATE_PROGRESS);
		super.onCreate(savedInstanceState);
		setContentView(R.layout.display_result);
		mWebView = (WebView) findViewById(R.id.display_result);
		mWebView.setWebChromeClient(mWebChromeClient);
		
		mAccountManager = AccountManager.get(this);
		if(mAuth == null){
			Intent intent = new Intent(this, AccountChooser.class);
			startActivityForResult(intent, REQUEST_CODE_ACCOUNT_CHOOSEER);
		}
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
	class TaskAuth extends AsyncTask<String, Void, Void>{

		@Override
		protected Void doInBackground(String... params) {
			String auth = null;
			try {
				Bundle bundle = mAccountManager.getAuthToken(mAccount, "reader", true, null, null).getResult();
				Log.d(TAG, "bundle = " + bundle);
				Intent intent = null;
				if(bundle.containsKey(AccountManager.KEY_INTENT)){
					intent = (Intent) bundle.get(AccountManager.KEY_INTENT);
					Log.d(TAG, "intent = " + intent);
					int flags = intent.getFlags();
                    flags &= ~Intent.FLAG_ACTIVITY_NEW_TASK;        //clear new task
                    intent.setFlags(flags);
                    startActivityForResult(intent, REQUEST_CODE_AUTHENTICATE);
				}else if(bundle.containsKey(AccountManager.KEY_AUTHTOKEN)){
					mAuth = bundle.getString(AccountManager.KEY_AUTHTOKEN);
					Log.d(TAG, "auth = " + mAuth);
				}
			} catch (OperationCanceledException e) {
				e.printStackTrace();
			} catch (AuthenticatorException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
//			String auth = GoogleAccountAuthorization.authenticate("GOOGLE", "weiliuzhao@gmail.com", "wlz+-19870426", "reader", null, null);
			if(mAuth != null){
				int end = mAccount.name.lastIndexOf('@');
				String user = mAccount.name.substring(0, end);
				Log.d(TAG, "user = " + user);
				GoogleReaderClient client = new GoogleReaderClient(AccountTestActivity.this, mAuth, user);
				final String temp = client.listAllNews();
				mWebView.post(new Runnable() {
					@Override
					public void run() {
						mWebView.loadDataWithBaseURL(null, temp, "text/html", "utf-8", null);		
					}
				});
			}
			/*
			FileInputStream fis  = null;
			try {
				fis = AccountTestActivity.this.openFileInput("GetResult.html");
				byte buf[] = new byte[1024];
				String temp = "";
				while(fis.read(buf) > 0){
					temp += new String(buf);
				}
				Log.d(TAG, temp);				
			} catch (FileNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch(IOException e){
				e.printStackTrace();
			}*/
			return null;
		}

		@Override
		protected void onPostExecute(Void result) {
			setProgressBarIndeterminateVisibility(false);
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
			}
		}
	}
	
	
	
}
