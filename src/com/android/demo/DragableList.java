package com.android.demo;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;

import com.android.demo.TouchInterceptor.DragListener;
import com.android.demo.TouchInterceptor.DropListener;
import com.android.demo.TouchInterceptor.RemoveListener;

public class DragableList extends Activity{

	private static final String TAG = "DragableList";
	private TouchInterceptor mListView;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.draglist);
		mListView = (TouchInterceptor)findViewById(R.id.dragable_list);
		mListView.setDragListener(new DragListener() {
			
			@Override
			public void drag(int from, int to) {
				
			}
		});
		mListView.setDropListener(new DropListener() {
			
			@Override
			public void drop(int from, int to) {
				
			}
		});
		mListView.setRemoveListener(new RemoveListener() {
			
			@Override
			public void remove(int which) {
				ListAdapter adapter = mListView.getAdapter();
				ArrayAdapter<String> arrayAdapter = (ArrayAdapter<String>)adapter;
				String item = arrayAdapter.getItem(which);
//				arrayAdapter.remove(item);
				Log.d(TAG, "remove = " + which + ", item = " + item );
			}
		});
	}
}
