package com.android.demo.fragmenttest;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.Window;

import com.android.demo.R;

public class FragmentTestActivity extends FragmentActivity{

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		super.onCreate(savedInstanceState);
		setContentView(R.layout.fragment_test);
		getSupportFragmentManager().beginTransaction().add(R.id.parent, new FragmentTest()).commit();
	}
}
