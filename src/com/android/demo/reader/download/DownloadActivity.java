package com.android.demo.reader.download;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.UUID;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.webkit.WebView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;

import com.android.demo.ItemInfo;
import com.android.demo.R;
import com.android.demo.RssConstant;
import com.android.demo.RssReaderConstant;

public class DownloadActivity extends Activity {
	private static final String TAG = "DownloadActivity";
	String mWebUrl = null;
	WebView mWebView;
	Button mDownload = null;
	Button mShowHtml = null;
	EditText mUrlText = null;
	private ArrayList<ItemInfo> items = null;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.download);
		mUrlText = (EditText) findViewById(R.id.url);
		mWebView = (WebView) findViewById(R.id.webview);
		mDownload = (Button) findViewById(R.id.download_html);
		mShowHtml = (Button) findViewById(R.id.showhtml);
//		mShowHtml.setEnabled(false);
		items = new ArrayList<ItemInfo>();
	}
	public void onClick(View view){
		int id = view.getId();
		switch(id){
		case R.id.download_html:
			Log.d(TAG, "onClick R.id.download_html");
			mDownload.setEnabled(false);
//			mShowHtml.setEnabled(false);
			new Thread(){
	
				@Override
				public void run() {
					String text = mUrlText.getEditableText().toString();
					String url = "http://wap.ifeng.com";
					if(!text.startsWith("http://")){
						url = "http://" + text;
					}else{
						url = text;
					}
					try {
						mWebUrl = new DownFile(DownloadActivity.this).downloadHtml(url);
					} catch (IOException e) {
						e.printStackTrace();
					}
					mDownload.post(new Runnable() {
						@Override
						public void run() {
							// TODO Auto-generated method stub
							mDownload.setEnabled(true);
							mShowHtml.setEnabled(true);
						}
					});
				}
				
			}.start();
			break;
		case R.id.showhtml:
			Log.d(TAG, "onClick R.id.showhtml");
			startUpBrowser(mWebUrl);
			Log.d(TAG, "mWebUrl = " + mWebUrl);
			mWebView.loadUrl("file://" + mWebUrl);
			break;
		case R.id.rss_news:
			String sortOrder = RssConstant.Content.ITEM_PUBDATE + " asc";
			Cursor c = null;
			ItemInfo item = null;
			try{
				c = getContentResolver().query(RssConstant.Content.ITEM_URI, null, null, null, sortOrder);
				Log.d(TAG, "c = " + c);
				if(c != null){
					if(c.moveToFirst()){
						do{
							item = new ItemInfo();
							item.itemTitle = c.getString(c.getColumnIndex(RssConstant.Content.ITEM_TITLE));
							item.itemUrl = c.getString(c.getColumnIndex(RssConstant.Content.ITEM_URL));
//							Log.d(TAG,"item = " + item);
							items.add(item);
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
			ArrayAdapter<ItemInfo> adapter = new ArrayAdapter<ItemInfo>(this, android.R.layout.simple_list_item_1, android.R.id.text1, items);
			new AlertDialog.Builder(this).
			setAdapter(adapter, new OnClickListener() {
				
				@Override
				public void onClick(DialogInterface dialog, int which) {
					String url = items.get(which).itemUrl;
					mUrlText.setText(url);
					SharedPreferences p = DownloadActivity.this.getSharedPreferences("RssWidget", Context.MODE_PRIVATE);
					String fileName = p.getString(url, null);
					Log.d(TAG, "fileName = " + fileName);
					if(fileName != null){
						mWebView.loadUrl("file://" + fileName);
					}
				}
			}).create().show();
			break;
		}
	}
	private void startUpBrowser(String filePath){
		Intent intent=new Intent(); 
		intent.setAction("android.intent.action.VIEW"); 
		Uri CONTENT_URI_BROWSERS = Uri.parse("file://" + filePath); 
		intent.setData(CONTENT_URI_BROWSERS); 
		intent.setClassName("com.android.browser", "com.android.browser.BrowserActivity"); 
		try{
			startActivity(intent);
		}catch(ActivityNotFoundException e){
			e.printStackTrace();
		}
	}
}
