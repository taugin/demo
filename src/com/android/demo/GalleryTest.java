package com.android.demo;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.Gallery;
import android.widget.SimpleAdapter;
import android.widget.TextView;

public class GalleryTest extends Activity implements OnItemSelectedListener{

	private TextView mTextView = null;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.gallery);
		HashMap<String, String> hashMap = null;
		List<HashMap<String, String>> list = new ArrayList<HashMap<String, String>>();
		for(int i = 0;i < 10; i++){
			hashMap = new HashMap<String, String>();
			hashMap.put("title", "Item " + i);
			hashMap.put("index", String.valueOf(i));
			list.add(hashMap);
		}
		mTextView = (TextView)findViewById(R.id.index_show);
		Gallery gallery = (Gallery)findViewById(R.id.gallery);
		SimpleAdapter adapter = new SimpleAdapter(this, list, R.layout.gallery_item, new String[]{"title"}, new int[]{R.id.gallery_item});
		gallery.setAdapter(adapter);
		gallery.setOnItemSelectedListener(this);
		
	}

	@Override
	public void onItemSelected(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
		@SuppressWarnings("unchecked")
		HashMap<String, String> hashMap = (HashMap<String, String>) arg0.getItemAtPosition(arg2);
		String index = hashMap.get("index");
		mTextView.setText(index);
	}

	@Override
	public void onNothingSelected(AdapterView<?> arg0) {
		
	}
	
}
