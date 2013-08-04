package com.android.demo.reader.atom;

import java.util.ArrayList;

public class Entry {
	public String type;
	public ArrayList<Attr> entryAttrs;
	public Id id;
	public ArrayList<AtomCategory> categories;
	public Title title;
	public String published;
	public String updated;
	public Link link;
	public Summary summary;
	public Author author;
	public Source source;
	
	@Override
	public String toString() {
		String prefix = "<p>";
		String link = getLink("href");
		String out = prefix + "<hr>"
				+ prefix + "<h2><font color=red><a name=" + id.content + ">" + title.content + "</a></font></h2>"
				+ "<p align=right>" + "<font color=green>" + source.title.content +"</font>"
				+ prefix + "<a href=" + link + ">" + "View original" + "</a>"
				+ prefix + (summary != null ? summary.content : "No news");
		return out;
	}
	
	private String getLink(String key){
		int size = link.linkAttrs.size();
		for(int i = 0; i < size; i++){
			Attr temp = link.linkAttrs.get(i);
			if(temp != null){
				if(key.equals(temp.attrName)){
					return temp.attrVaue;
				}
			}
		}
		return null;
	}
}
