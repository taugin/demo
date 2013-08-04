package com.android.demo.reader;

import java.io.IOException;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.accounts.AuthenticatorException;
import android.accounts.OperationCanceledException;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;

import com.android.demo.R;
import com.android.demo.application.GoogleReaderApplication;

public class AuthenticationActivity extends Activity {

	private static final String TAG = "AuthenticationActivity";
	private static final int REQUEST_CODE_AUTHENTICATE = 1;
	private static final int REQUEST_CODE_CHOOSEACCOUNT = 2;
	private static final int MSG_AUTHENTICATE = 1;
	private static final int MSG_STORE_AUTH = 2;
	private static final int MSG_GET_USERINFO = 3;
	private static final String KEY_NAME = "name";
	private static final String KEY_TYPE = "type";
	private static final String KEY_AUTH = "auth";
	private static final String KEY_ACCOUNT = "account";
	private Account mAccount = null;
	private String mAuthenricate = null; 
	private ProgressBar mProgressBar;
	private AccountManager mAccountManager;
	private boolean mCanFinish = false;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.auth_progress);
		mProgressBar = (ProgressBar) findViewById(R.id.auth_progress);
		Log.d(TAG, "savedInstanceState");
		mAccountManager = AccountManager.get(this);
		Intent intent = getIntent();
		if(intent != null){
			String name = intent.getStringExtra(KEY_NAME);
			String type = intent.getStringExtra(KEY_TYPE);
			String auth = intent.getStringExtra(KEY_AUTH);
			Log.d(TAG, "name = " + name + " , type = " + type + " ,auth = " + auth);
			boolean exist = accountExist(name, type);
			if(!exist){
				Intent i = new Intent(this, AccountChooser.class);
				startActivityForResult(i, REQUEST_CODE_CHOOSEACCOUNT);
			}else{
				if((auth == null) || (auth != null && auth.trim().equals(""))){
					mAccount = new Account(name, type);
					new AuthenricateTask().execute();
				}else{
					mAccountManager.invalidateAuthToken(auth, type);
					mAuthenricate = null;
					new AuthenricateTask().execute();
				}
				
			}
		}
	}
	private boolean accountExist(String name, String type){
		if(name == null || type == null){
			return false;
		}
		AccountManager manager = AccountManager.get(this);
		Account[] allAccounts = manager.getAccountsByType(type);
		for(Account account : allAccounts){
			if(name.equals(account.name) && type.equals(account.type)){
				return true;
			}
		}
		return false;
	}
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if(requestCode == REQUEST_CODE_AUTHENTICATE){
			Log.d(TAG, "REQUEST_CODE_AUTHENTICATE");
			if(resultCode == RESULT_OK){
				Log.d(TAG, "data = " + data);
				new AuthenricateTask().execute();
			}else{
				
			}
		}else if(requestCode == REQUEST_CODE_CHOOSEACCOUNT){
			Log.d(TAG, "REQUEST_CODE_CHOOSEACCOUNT");
			if(resultCode == RESULT_OK){
				String name = data.getStringExtra(KEY_NAME);
				String type = data.getStringExtra(KEY_TYPE);
				mAccount = new Account(name, type);
				new AuthenricateTask().execute();
			}
		}
	}
	class AuthenricateTask extends AsyncTask<Void, Void, Void>{
		@Override
		protected void onPreExecute() {
			mProgressBar.setVisibility(View.VISIBLE);
		}

		@Override
		protected Void doInBackground(Void ...params) {
			if(mAuthenricate == null){
				mAuthenricate = authenticate();				
			}
			if(mAuthenricate != null){
				GoogleReaderClient client = new GoogleReaderClient(AuthenticationActivity.this, mAuthenricate);
				GoogleReaderApplication app = (GoogleReaderApplication) getApplication();
				app.storeClient(mAccount.name, client);
				app.setCurrentUser(mAccount.name);
				SharedPreferences p = getSharedPreferences("GoogleReaderClient", MODE_PRIVATE);
				p.edit().putString("userEmail", mAccount.name).commit();
				p.edit().putString("userType", mAccount.type).commit();
				p.edit().putString("userAuth", mAuthenricate).commit();
				Log.d(TAG, "user = " + mAccount.name);
			}
			return null;
		}
		@Override
		protected void onPostExecute(Void result) {
			if(mCanFinish){
				finish();
			}
		}
		private String authenticate(){
			Log.d(TAG, "authenticate  mAccount = " + mAccount);
			String auth = null;
			try {
				Bundle bundle = mAccountManager.getAuthToken(mAccount, "reader", true, null, null).getResult();
				Log.d(TAG, "bundle = " + bundle);
				Intent intent = null;
				if(bundle.containsKey(AccountManager.KEY_INTENT)){
					mCanFinish = false;
					intent = (Intent) bundle.get(AccountManager.KEY_INTENT);
					Log.d(TAG, "intent = " + intent);
					int flags = intent.getFlags();
	                flags &= ~Intent.FLAG_ACTIVITY_NEW_TASK;        //clear new task
	                intent.setFlags(flags);
	                startActivityForResult(intent, REQUEST_CODE_AUTHENTICATE);
				}else if(bundle.containsKey(AccountManager.KEY_AUTHTOKEN)){
					mCanFinish = true;
					auth = bundle.getString(AccountManager.KEY_AUTHTOKEN);
					SharedPreferences p = getPreferences(MODE_PRIVATE);
					p.edit().putString("auth", auth).commit();
					Log.d(TAG, "auth = " + auth);
				}
				return auth;
			} catch (OperationCanceledException e) {
				e.printStackTrace();
				return null;
			} catch (AuthenticatorException e) {
				e.printStackTrace();
				return null;
			} catch (IOException e) {
				e.printStackTrace();
				return null;
			}
		}

	}
}
