package com.android.demo.reader.feedlist;

import java.util.List;

import android.util.Log;

public class Subscriptions {
	public String type;
	public List<Subscription> subscriptions;
	
	public String toString() {
		int size = subscriptions.size();
		Subscription c = null;
		String out = "";
		Log.d("Subscriptions", "size = " + size);
		for(int i = 0;i < size;i++){
			c = subscriptions.get(i);
			out += c.toString();
		}
		return out;
	}
}
