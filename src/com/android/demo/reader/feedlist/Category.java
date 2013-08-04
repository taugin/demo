package com.android.demo.reader.feedlist;
public class Category{	
		public String type;
		public String id;
		public String label;
		@Override
		public String toString() {
			String className = "<p>&nbsp;&nbsp;&nbsp;&nbsp;"; 
			String output = null;
			output =  className + " id : " + id 
					+ className + " label : " + label;
			return output;
		}
	}