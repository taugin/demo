package com.android.demo.reader;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import com.android.demo.R;


public class AccountChooser extends Activity implements OnItemClickListener{

	private static final String TAG = "AccountChooser";
	private static final int REQUEST_CODE_AUTHENTICATE = 1;
	private static final String KEY_NAME = "name";
	private static final String KEY_TYPE = "type";
	private static final String KEY_ACCOUNT = "account";
	private ListView mAccountList = null;
	private Account mAccount = null;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.account_chooser);
		mAccountList = (ListView) findViewById(R.id.list_account);
		mAccountList.setOnItemClickListener(this);
	}
	
	
	@Override
	protected void onResume() {
		super.onResume();
		accountList();
	}


	private void accountList(){
		List<HashMap<String, String>> data = new ArrayList<HashMap<String,String>>();
		HashMap<String, String> hashMap = null;
		AccountManager manager = AccountManager.get(this);
//		Account accounts[] = manager.getAccountsByType("com.google");
		Account accounts[] = manager.getAccounts();
		for(Account account : accounts){
			hashMap = new HashMap<String, String>();
			hashMap.put("email", account.name);
			hashMap.put("type", account.type);
			data.add(hashMap);
			Log.d(TAG, "ACCOUNTNAME = " + account.name);
		}
		manager.getAuthenticatorTypes();
		SimpleAdapter adapter = new SimpleAdapter(this, data, android.R.layout.simple_list_item_single_choice, new String[]{"email"}, new int[]{android.R.id.text1});
		mAccountList.setChoiceMode(ListView.CHOICE_MODE_SINGLE);
		mAccountList.setAdapter(adapter);
	}
	public void onClick(View view){
		startActivity(new Intent(Settings.ACTION_ADD_ACCOUNT));
	}
	@Override
	public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
		@SuppressWarnings("unchecked")
		HashMap<String, String> hashMap = (HashMap<String, String>) arg0.getItemAtPosition(arg2);
		String email = null;
		String type = null;
		if(hashMap != null && !hashMap.isEmpty()){
			email = hashMap.get("email");
			type = hashMap.get("type");
		}
		Log.d(TAG,"email = " + email + " , type = " + type);
		if(email == null || type == null){
			return ;
		}
		Intent intent = new Intent();
		intent.putExtra(KEY_NAME, email);
		intent.putExtra(KEY_TYPE, type);
		setResult(RESULT_OK, intent);
		finish();
	}
}
