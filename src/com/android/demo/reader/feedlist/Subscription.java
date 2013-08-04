package com.android.demo.reader.feedlist;


public class Subscription {
	public String type;
	public String id;
	public String title;
	public Categories categories;
	public String sortId;
	public long firstItemmesc;
	public String htmlUrl;
	
	
	public String toString() {
		String className = "<li>"; 
		String output = "<ul>";
		output  = className + "<h3>" + title + "</h3>"
				+ className + "<a href=" + id + ">Detail</a>" 
//				+ className + " categories : " + categories
	//			+ className + " sortId : " + sortId
		//		+ className + " firstItemmesc : " + firstItemmesc
			//	+ className + " htmlUrl : " + htmlUrl
				+ "<hr>";
		return output;
				
	}
	
}
