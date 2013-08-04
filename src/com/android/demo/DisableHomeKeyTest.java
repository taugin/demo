package com.android.demo;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.Toast;

public class DisableHomeKeyTest extends Activity {
    /** Called when the activity is first created. */
	private static final String TAG = "ActivityTestActivity";
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.disable_home_key);
        setTitle(getLocalClassName());
    }
	@Override
	protected void onDestroy() {
		Log.d(TAG, "onDestroy");
		super.onDestroy();
	}

	@Override
	protected void onRestart() {
		Log.d(TAG, "onRestart");
		super.onRestart();
	}

	@Override
	protected void onResume() {
		Log.d(TAG, "onResume");
		super.onResume();
	}

	@Override
	protected void onStart() {
		Log.d(TAG, "onStart");
		super.onStart();
	}

	@Override
	protected void onStop() {
		Log.d(TAG, "onStop");
		super.onStop();
	}
	@Override
	protected void onPause() {
		Log.d(TAG, "onPause");
		super.onPause();
	}
    
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		//Toast.makeText(this, "You pressed the " + KeyEvent.keyCodeToString(keyCode) + " key !", Toast.LENGTH_SHORT).show();
		return super.onKeyDown(keyCode, event);
	}
	@Override
	public void onAttachedToWindow() {	
		getWindow().setType(WindowManager.LayoutParams.TYPE_KEYGUARD);
		super.onAttachedToWindow();
	}
	
}