package com.android.demo;

import java.security.PublicKey;
import java.util.ArrayList;

import android.app.Activity;
import android.database.Cursor;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Html;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ScrollView;
import android.widget.TextView;

public class TestRssActivity extends Activity implements OnClickListener{

	private static final String TAG = "TestRssActivity";
	
	private Button mStartButton;
	private TextView mTestResult;
	private TextView mTestProcess;
	private ScrollView mScrollView;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.testrss);
		mStartButton = (Button) findViewById(R.id.start_test);
		mTestResult = (TextView) findViewById(R.id.test_result);
		mTestProcess = (TextView) findViewById(R.id.test_process);
		mScrollView = (ScrollView)findViewById(R.id.scrollview);
		mStartButton.setOnClickListener(this);
	}
	@Override
	public void onClick(View v) {
		BackgroundTask task = new BackgroundTask();
		task.execute();
		mStartButton.setEnabled(false);
	}

	class BackgroundTask extends AsyncTask<Void, String, String>{

		ArrayList<ViewItems> list = null;
		ArrayList<ViewItems> resultList = null;
		@Override
		protected void onPreExecute() {
			list = new ArrayList<ViewItems>();
		}

		@Override
		protected String doInBackground(Void... params) {
			String sortOrder = RssReaderConstant.Content.ITEM_PUBDATE + " asc";
			Cursor c = null;
			ViewItems item = null;
			try{
				c = getContentResolver().query(RssReaderConstant.Content.VIEW_ITEM_URI, null, null, null, sortOrder);
				if(c != null){
					if(c.moveToFirst()){
						publishProgress("Get data from RssWidget application ...." + "");
						do{
							item = new ViewItems();
							item.widgetId = c.getInt(c.getColumnIndex(RssReaderConstant.Content.WIDGET_ID));
							item.itemTitle = c.getString(c.getColumnIndex(RssReaderConstant.Content.ITEM_TITLE));
							item.feedTitle = c.getString(c.getColumnIndex(RssReaderConstant.Content.FEED_TITLE));
							item.itemUrl = c.getString(c.getColumnIndex(RssReaderConstant.Content.ITEM_URL));
							item.feedId = c.getInt(c.getColumnIndex(RssReaderConstant.Content.FEED_ID));
							item.itemId = c.getInt(c.getColumnIndex(RssReaderConstant.Content.ITEM_ID));
							list.add(item);
						}while(c.moveToNext());
					}
				}
			}catch (Exception e) {
				e.printStackTrace();
			}finally{
				if(c != null){
					c.close();
				}
			}
			publishProgress("There are " + list.size() + " items to be detected ... " + "\n");
			publishProgress("Start detecting duplicate data ..." + "\n");
			resultList = new ArrayList<ViewItems>();
			int size = list.size();
			int i = 0;
			int j = 0;
			String info = "";
			boolean equal = false;
			ViewItems item1 = null;
			for(i = 0; i < size; i++){
				item = list.get(i);
				equal = false;
				for(j = 0; j<size; j++){
					if(i != j){
						item1 = list.get(j);
						if(item.itemTitle.equals(item1.itemTitle) 
								&& item.feedTitle.equals(item1.feedTitle) 
								&& item.widgetId == item1.widgetId 
								/*&& item.itemUrl.equals(item1.itemUrl)*/) {
							if(i < j){
								resultList.add(item);
								resultList.add(item1);
								info += "WidgetId = " + item.widgetId 
										+ "\nFeedId = " + item.feedId 
										+ "\nItemId = " + item.itemId 
										+ "\nFeedTitle = " + item.feedTitle 
										+ "\nItemTitle = " + item.itemTitle 
										+ "\nItemUrl = " + Html.fromHtml("<a href=" + item.itemUrl + ">" +  item.itemUrl + "</a>") 
										+ "\n";
								info += "--------------------------\n";
								info += "WidgetId = " + item1.widgetId 
										+ "\nFeedId = " + item1.feedId 
										+ "\nItemId = " + item1.itemId 
										+ "\nFeedTitle = " + item1.feedTitle 
										+ "\nItemTitle = " + item1.itemTitle 
										+ "\nItemUrl = " + Html.fromHtml("<a href=" + item1.itemUrl + ">" +  item1.itemUrl + "</a>")
										+ "\n\n";
								info += "==========================\n";
							}
							equal = true;
						}
					}
				}
				publishProgress("" + i + "/" + size + " is detecting , itemTitle is " + item.itemTitle);
			}
			publishProgress("Detect completely ..." + "\n");
			String result = "There is " + resultList.size() + " items equaled " + "\n\n" + info;
			return result;
		}

		@Override
		protected void onPostExecute(String result) {
			int size = resultList.size();
			String temp = "";
			ViewItems item = null;
			for(int i=0;i<size;i++){
				item = resultList.get(i);
				temp += "WidgetId = " + item.widgetId + "\nFeedId = " + item.feedId + "\nItemId = " + item.itemId + "\nFeedTitle = " + item.feedTitle + "\nItemTitle = " + item.itemTitle + "\n\n";
			}
			mTestResult.setText(result);
			mStartButton.setEnabled(true);
		}

		@Override
		protected void onProgressUpdate(String... values) {
//			Log.d(TAG, "values = " + values[0]);
//			String oldText = mTestProcess.getText().toString();
//			mTestProcess.setText(oldText + values[0]);
			mTestProcess.setText(values[0]);
			mScrollView.scrollTo(0, 9800);
		}
		  
	}
}
