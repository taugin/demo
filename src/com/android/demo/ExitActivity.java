package com.android.demo;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

public class ExitActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.exit_layout);
		Intent intent = new Intent(this, AndroidDemoActivity.class);
		startActivity(intent);
	}
	
}
