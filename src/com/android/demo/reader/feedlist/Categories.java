package com.android.demo.reader.feedlist;

import java.util.List;

public class Categories{	
	public String type;
	public List<Category> categories;
	@Override
	public String toString() {
		int size = categories.size();
		Category c = null;
		String out = "";
		for(int i = 0;i < size;i++){
			c = categories.get(i);
			out += c.toString();
		}
		return out;
	}
}