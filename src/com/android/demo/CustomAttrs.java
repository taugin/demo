package com.android.demo;

import android.app.Activity;
import android.content.res.TypedArray;
import android.content.res.XmlResourceParser;
import android.os.Bundle;
import android.util.AttributeSet;
import android.util.Log;
import android.util.Xml;
import android.view.View;

public class CustomAttrs extends Activity {
	private static final String TAG = "CustomAttrs";
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.custom_attrs);
	}
	
	public void onClick(View view){
		XmlResourceParser parser = getResources().getXml(R.xml.test);
		
		Log.d(TAG, "parser = " + parser.getAttributeCount());
		AttributeSet set = Xml.asAttributeSet(parser);
		Log.d(TAG, "set = " + set.getAttributeCount());
		TypedArray a = obtainStyledAttributes(set, R.styleable.Test);
		Log.d(TAG, "a = " + a.getString(R.styleable.Test_abc));
	}
}
