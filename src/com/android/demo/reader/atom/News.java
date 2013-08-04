package com.android.demo.reader.atom;

import java.util.ArrayList;
import java.util.HashMap;

public class News {
	public String type;
	public HashMap<String, String> nameSpace;
	public ArrayList<Attr> itemAttrs;
	public Generator generator;
	public String id;
	public String title;
	
	public Author author;
	public String updated;
	public ArrayList<Entry> entrys;
	
	
	@Override
	public String toString() {
		String prefix = "<p>";
		String temp = "";
		String bookmark = "<ol>";
		for(Entry entry : entrys){
			temp += entry.toString();
			bookmark += "<li>";
			bookmark += "<a href=#" + entry.id.content + ">" + entry.title.content + "(" + entry.source.title.content + ")" + "</a>";
			bookmark += "</li>";
			bookmark += "</br>";
		}
		bookmark += "</ol>";
		String out = prefix + "<h1>" + title + "</h1>"
				+ "<p align=right>" + "<font size=+3>" + (author != null ? author.name : "") +"</font>"
				+ bookmark
				+ temp;
		return out;
	}
}
