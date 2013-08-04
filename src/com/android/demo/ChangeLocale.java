package com.android.demo;

import java.util.Locale;

import android.app.Activity;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.View;

public class ChangeLocale extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.change_locale);
	}
	
	
	public void onClick(View view){
		int id = view.getId();
		Resources resources = getResources();
		Configuration config = resources.getConfiguration();
        DisplayMetrics dm = resources .getDisplayMetrics();
		switch(id){
		case R.id.change_english:
			config.locale = Locale.ENGLISH;
			break;
		case R.id.change_chinese:
			config.locale = Locale.CHINESE;
			break;
		default:
			break;
		}
		resources.updateConfiguration(config, dm);
		finish();
	}
	
}
