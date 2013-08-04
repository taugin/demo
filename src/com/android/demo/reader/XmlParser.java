package com.android.demo.reader;

import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.Stack;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import android.content.Context;
import android.util.Log;
import android.util.Xml;

import com.android.demo.reader.atom.AtomCategory;
import com.android.demo.reader.atom.Attr;
import com.android.demo.reader.atom.Author;
import com.android.demo.reader.atom.Entry;
import com.android.demo.reader.atom.Generator;
import com.android.demo.reader.atom.Id;
import com.android.demo.reader.atom.Link;
import com.android.demo.reader.atom.News;
import com.android.demo.reader.atom.Source;
import com.android.demo.reader.atom.Summary;
import com.android.demo.reader.atom.Title;
import com.android.demo.reader.feedlist.Categories;
import com.android.demo.reader.feedlist.Category;
import com.android.demo.reader.feedlist.Subscription;
import com.android.demo.reader.feedlist.Subscriptions;
import com.android.demo.reader.info.Constant;
import com.android.demo.reader.info.UserInfo;

public final class XmlParser {
	
	private final static String TAG = "XmlParser";
	
	private final static String OBJECT = "object";
	private final static String STRING = "string";
	private final static String NUMBER = "number";
	private final static String BOOLEAN = "boolean";
	private final static String LIST = "list";
	private Context mContext;
	
	private Stack<Object> mStack;
	private static XmlParser gXmlParser = null;
	public static XmlParser getXmlParser(Context context){
		if(gXmlParser == null){
			gXmlParser = new XmlParser(context);
		}
		return gXmlParser;
	}
	private XmlParser(Context context){
		mContext = context;
		mStack = new Stack<Object>();
	}
	private void entureStackNormal(){
		if(mStack == null){
			mStack = new Stack<Object>();
		}
		if(!mStack.isEmpty()){
			mStack.clear();
		}
	}
	private void pushObject(Object object){
		mStack.push(object);
	}
	private void peekObject(){
		Object object = mStack.pop();
	}
	
	public Subscriptions parseSubscription(String xml){
		Subscriptions subscriptions = null;
		XmlPullParser xpp = Xml.newPullParser();
		StringReader sr = new StringReader(xml);
		String attr = null;
		String value = null;
		String text = null;
		int eventType;
		try {
			xpp.setInput(sr);
			eventType = xpp.getEventType();
			Log.d(TAG, "eventType = " + eventType);
			while (eventType != XmlPullParser.END_DOCUMENT) {
				String tag = xpp.getName();
				Log.d(TAG, "tag = " + tag);
				if (eventType == XmlPullParser.START_TAG) {
					if(tag.equals(OBJECT)){
						Object object = null;
						if(!mStack.isEmpty()){
							object = mStack.peek();
						}
						if(object == null){
							
						}else{
							if(object instanceof Subscriptions){
								Log.d(TAG, "OBJECT ------------- Subscription");
								Subscription subscription = new Subscription();
								subscription.type = "Subscription";
								((Subscriptions)object).subscriptions.add(subscription);
								mStack.add(subscription);
							}else if(object instanceof Categories){
								Log.d(TAG, "OBJECT ------------- Category");
								Category category = new Category();
								category.type = "Category";
								((Categories)object).categories.add(category);
								mStack.add(category);
							}
						}
					}else if(tag.equals(STRING)){
						int count = xpp.getAttributeCount();
						Log.d(TAG, "count = " + count);
						Object parent = null;
						if(!mStack.isEmpty()){
							parent = mStack.peek();
						}
						for(int i = 0;i < count;i++){
							attr = xpp.getAttributeName(i);
							value = xpp.getAttributeValue(i);
							text = xpp.nextText();
							Log.d(TAG, "attr = " + attr + " , value = " + value + " , text = " + text);
							if(value != null && value.equals(Constant.ID)){
								if(parent instanceof AtomCategory){
									((Category)parent).id = text;
									Log.d(TAG,"~~~~~~~~~~~~~~~~~~~~~~~~~~Category ID");
								}else if(parent instanceof Subscription){
									Log.d(TAG,"~~~~~~~~~~~~~~~~~~~~~~~~~~Subscription ID");
									((Subscription)parent).id = text;
								}
							}else if(value != null && value.equals(Constant.TITLE)){
								if(parent instanceof Subscription){
									((Subscription)parent).title = text;
								}
							}else if(value != null && value.equals(Constant.SORT_ID)){
								if(parent instanceof Subscription){
									((Subscription)parent).sortId = text;
								}
							}else if(value != null && value.equals(Constant.HTML_URL)){
								if(parent instanceof Subscription){
									((Subscription)parent).htmlUrl = text;
								}
							}else if(value != null && value.equals(Constant.LABEL)){
								if(parent instanceof AtomCategory){
									Log.d(TAG,"~~~~~~~~~~~~~~~~~~~~~~~~~~Category TITLE");
									((Category)parent).label = text;
								}
							}
						}
					}else if(tag.equals(NUMBER)){
						int count = xpp.getAttributeCount();
						Object parent = null;
						if(!mStack.isEmpty()){
							parent = mStack.peek();
						}
						Log.d(TAG, "count = " + count);
						for(int i = 0;i < count;i++){
							attr = xpp.getAttributeName(i);
							value = xpp.getAttributeValue(i);
							text = xpp.nextText();
							Log.d(TAG, "attr = " + attr + " , value = " + value + " , text = " + text);
							if(value != null && value.equals(Constant.FIRST_ITEMMSEC)){
								if(parent instanceof Subscription){
									((Subscription)parent).firstItemmesc = Long.parseLong(text);
								}
							}
						}
					}else if(tag.equals(LIST)){
						int count = xpp.getAttributeCount();
						Log.d(TAG, "count = " + count);
//						text = xpp.nextText();
						Object parent = null;
						if(!mStack.isEmpty()){
							parent = mStack.peek();
						}
						for(int i = 0;i < count; i++){
							attr = xpp.getAttributeName(i);
							value = xpp.getAttributeValue(i);
							Log.d(TAG, "attr = " + attr + " , value = " + value + " , text = " + text);
							if(value != null && value.equals(Constant.SUBSCRIPTIONS)){
								subscriptions = new Subscriptions();
								subscriptions.subscriptions = new ArrayList<Subscription>();
								subscriptions.type = "Subscriptions";
								mStack.add(subscriptions);
							}else if(value != null && value.equals(Constant.CATEGORIES)){
								Categories categories = new Categories();
								if(parent != null && parent instanceof Subscription){
									((Subscription)parent).categories = categories;
								}
								categories.type = "Categories";
								categories.categories = new ArrayList<Category>();
								mStack.add(categories);
							}
						}
					}
				}else if(eventType == XmlPullParser.END_TAG){
					if(tag.equals(OBJECT)){
						if(!mStack.isEmpty()){
							mStack.pop();
						}
					}else if(tag.equals(STRING)){
						
					}else if(tag.equals(NUMBER)){
						
					}else if(tag.equals(LIST)){
						if(!mStack.isEmpty()){
							mStack.pop();
						}
					}
				}
				eventType = xpp.next();
			}
		} catch (XmlPullParserException e) {
			e.printStackTrace();
		} catch (IOException e){
			e.printStackTrace();
		}
		return subscriptions;
	}
	
	public UserInfo parseUserInfo(String xmlUserInfo){
		Log.d(TAG, "parseUserInfo");
		UserInfo userInfo = null;
		XmlPullParser xpp = Xml.newPullParser();
		StringReader sr = new StringReader(xmlUserInfo);
		String attr = null;
		String value = null;
		String text = null;
		int eventType;
		try {
			xpp.setInput(sr);
			eventType = xpp.getEventType();
			Log.d(TAG, "eventType = " + eventType);
			while (eventType != XmlPullParser.END_DOCUMENT) {
				String tag = xpp.getName();
				Log.d(TAG, "tag = " + tag);
				if (eventType == XmlPullParser.START_TAG) {
					if(tag.equals(OBJECT)){
						userInfo = new UserInfo();
					}else if(tag.equals(STRING)){
						int count = xpp.getAttributeCount();
						Log.d(TAG, "count = " + count);
						if(count > 0){
							attr = xpp.getAttributeName(0);
							value = xpp.getAttributeValue(0);
							text = xpp.nextText();
							Log.d(TAG, "attr = " + attr + " , value = " + value + " , text = " + text);
							if(value != null && value.equals(UserInfo.USER_ID)){
								userInfo.userId = text;
							}else if(value != null && value.equals(UserInfo.USER_NAME)){
								userInfo.userName = text;
							}else if(value != null && value.equals(UserInfo.USER_PROFILE_ID)){
								userInfo.userProfileId = text;
							}else if(value != null && value.equals(UserInfo.USER_EMAIL)){
								userInfo.userEmail = text;
							}
						}
					}else if(tag.equals(NUMBER)){
						int count = xpp.getAttributeCount();
						Log.d(TAG, "count = " + count);
						if(count > 0){
							attr = xpp.getAttributeName(0);
							value = xpp.getAttributeValue(0);
							text = xpp.nextText();
							Log.d(TAG, "attr = " + attr + " , value = " + value + " , text = " + text);
							if(value != null && value.equals(UserInfo.SIGNUPTIME_SPEC)){
								userInfo.signupTimeSec = Long.parseLong(text);
							}
						}
					}else if(tag.equals(BOOLEAN)){
						int count = xpp.getAttributeCount();
						Log.d(TAG, "count = " + count);
						if(count > 0){
							attr = xpp.getAttributeName(0);
							value = xpp.getAttributeValue(0);
							text = xpp.nextText();
							Log.d(TAG, "attr = " + attr + " , value = " + value + " , text = " + text);
							if(value != null && value.equals(UserInfo.BLOGGER_USER)){
								userInfo.isBloggerUser = Boolean.parseBoolean(text);
							}else if(value != null && value.equals(UserInfo.MULTI_LOGIN_ENABLED)){
								userInfo.isMultiLoginEnabled = Boolean.parseBoolean(text);
							}
						}
					}
				}else if(eventType == XmlPullParser.END_TAG){
					if(tag.equals(OBJECT)){
						
					}
				}
				eventType = xpp.next();
			}
		} catch (XmlPullParserException e) {
			e.printStackTrace();
		} catch (IOException e){
			e.printStackTrace();
		}
		
		return userInfo;
	}
	
	public News parseNews(String xml){
		if((xml == null) || (xml != null && xml.trim().equals(""))){
			Log.d(TAG, "xml string is not available !");
			return null;
		}
		News items = null;
		XmlPullParser xpp = Xml.newPullParser();
		StringReader sr = new StringReader(xml);
		Entry entry = null;
		int depth = -1;
		String tag = "";
		int eventType;
		try {
			xpp.setInput(sr);
			eventType = xpp.getEventType();
			Log.d(TAG, "eventType = " + eventType);
			while (eventType != XmlPullParser.END_DOCUMENT) {
				tag = xpp.getName();
				depth = xpp.getDepth();
				Log.d(TAG, "tag = " + tag + " , depth = " + depth);	
				if (eventType == XmlPullParser.START_TAG) {
					if(Constant.News.FEED.equals(tag)){
						items = new News();
						//
						int count = xpp.getAttributeCount();
						ArrayList<Attr> attrList = new ArrayList<Attr>();
						Attr a = null;
						for(int i = 0; i < count; i++){
							a = new Attr();
							a.nameSpace = xpp.getAttributeNamespace(i);
							a.attrName = xpp.getAttributeName(i);
							a.attrVaue = xpp.getAttributeValue(i);
							attrList.add(a);
						}
						//
						if(attrList.size() > 0){
							items.itemAttrs = attrList;
						}else {
							items.itemAttrs = null;
						}
						
						ArrayList<Entry> entryList = new ArrayList<Entry>();
						items.entrys = entryList;
					}else if(Constant.News.GENERATOR.equals(tag)){
						Generator generator = new Generator();
						
						int count = xpp.getAttributeCount();
						ArrayList<Attr> attrList = new ArrayList<Attr>();
						Attr a = null;
						for(int i = 0; i < count; i++){
							a = new Attr();
							a.nameSpace = xpp.getAttributeNamespace(i);
							a.attrName = xpp.getAttributeName(i);
							a.attrVaue = xpp.getAttributeValue(i);
							attrList.add(a);
						}
						if(attrList.size() > 0){
							generator.generatorAttrs = attrList;
						}else {
							generator.generatorAttrs = null;
						}
						String s = xpp.nextText();
					}else if(Constant.News.ID.equals(tag) && depth == 2){
						items.id = xpp.nextText();
					}else if(Constant.News.TITLE.equals(tag) &&  depth == 2){
						items.title = xpp.nextText();
					}else if(Constant.News.LINK.equals(tag) && depth == 2){
						Link link = new Link();
						int count = xpp.getAttributeCount();
						ArrayList<Attr> attrList = new ArrayList<Attr>();
						Attr a = null;
						for(int i = 0; i < count; i++){
							a = new Attr();
							a.nameSpace = xpp.getAttributeNamespace(i);
							a.attrName = xpp.getAttributeName(i);
							a.attrVaue = xpp.getAttributeValue(i);
							attrList.add(a);
						}
						if(attrList.size() > 0){
							link.linkAttrs = attrList;
						}else {
							link.linkAttrs = null;
						}
						
					}else if(Constant.News.AUTHOR.equals(tag) && depth == 2){
						Author author = new Author();
						items.author = author;
					}else if(Constant.News.NAME.equals(tag) && depth == 3){
						items.author.name = xpp.nextText();
					}else if(Constant.News.UPDATED.equals(tag) && depth == 2){
						items.updated = xpp.nextText();
					}else if(Constant.News.ENTRY.equals(tag)){
						entry = new Entry();
						
						int count = xpp.getAttributeCount();
						ArrayList<Attr> attrList = new ArrayList<Attr>();
						Attr a = null;
						for(int i = 0; i < count; i++){
							a = new Attr();
							a.nameSpace = xpp.getAttributeNamespace(i);
							a.attrName = xpp.getAttributeName(i);
							a.attrVaue = xpp.getAttributeValue(i);
							attrList.add(a);
						}
						entry.entryAttrs = attrList;
						entry.categories = new ArrayList<AtomCategory>();
					}else if(Constant.News.ID.equals(tag) && depth == 3){
						Id id = new Id();
						int count = xpp.getAttributeCount();
						ArrayList<Attr> attrList = new ArrayList<Attr>();
						Attr a = null;
						for(int i = 0; i < count; i++){
							a = new Attr();
							a.nameSpace = xpp.getAttributeNamespace(i);
							a.attrName = xpp.getAttributeName(i);
							a.attrVaue = xpp.getAttributeValue(i);
							attrList.add(a);
						}
						if(attrList.size() > 0){
							id.idAttrs = attrList;
						}else {
							id.idAttrs = null;
						}
						id.content = xpp.nextText();
						entry.id = id;
					}else if(Constant.News.CATEGORY.equals(tag)){
						AtomCategory category = new AtomCategory();
						int count = xpp.getAttributeCount();
						ArrayList<Attr> attrList = new ArrayList<Attr>();
						Attr a = null;
						for(int i = 0; i < count; i++){
							a = new Attr();
							a.nameSpace = xpp.getAttributeNamespace(i);
							a.attrName = xpp.getAttributeName(i);
							a.attrVaue = xpp.getAttributeValue(i);
							attrList.add(a);
						}
						if(attrList.size() > 0){
							category.categoryAttr = attrList;
						}else {
							category.categoryAttr = null;
						}
						entry.categories.add(category);
					}else if(Constant.News.TITLE.equals(tag) && depth == 3){
						Title title = new Title();
						int count = xpp.getAttributeCount();
						ArrayList<Attr> attrList = new ArrayList<Attr>();
						Attr a = null;
						for(int i = 0; i < count; i++){
							a = new Attr();
							a.nameSpace = xpp.getAttributeNamespace(i);
							a.attrName = xpp.getAttributeName(i);
							a.attrVaue = xpp.getAttributeValue(i);
							attrList.add(a);
						}
						if(attrList.size() > 0){
							title.titleAttr = attrList;
						}else {
							title.titleAttr = null;
						}
						title.content = xpp.nextText();
						entry.title = title;
					}else if(Constant.News.PUBLISHED.equals(tag) && depth == 3){
						entry.published = xpp.nextText();
					}else if(Constant.News.UPDATED.equals(tag) && depth == 3){
						entry.updated = xpp.nextText();
					}else if(Constant.News.LINK.equals(tag) && depth == 3){
						Link link = new Link();
						int count = xpp.getAttributeCount();
						ArrayList<Attr> attrList = new ArrayList<Attr>();
						Attr a = null;
						for(int i = 0; i < count; i++){
							a = new Attr();
							a.nameSpace = xpp.getAttributeNamespace(i);
							a.attrName = xpp.getAttributeName(i);
							a.attrVaue = xpp.getAttributeValue(i);
							attrList.add(a);
						}
						if(attrList.size() > 0){
							link.linkAttrs = attrList;
						}else {
							link.linkAttrs = null;
						}
						entry.link = link;
					}else if(Constant.News.SUMMARY.equals(tag) && depth == 3){
						Summary summary = new Summary();
						int count = xpp.getAttributeCount();
						ArrayList<Attr> attrList = new ArrayList<Attr>();
						Attr a = null;
						for(int i = 0; i < count; i++){
							a = new Attr();
							a.nameSpace = xpp.getAttributeNamespace(i);
							a.attrName = xpp.getAttributeName(i);
							a.attrVaue = xpp.getAttributeValue(i);
							attrList.add(a);
						}
						if(attrList.size() > 0){
							summary.summaryAttrs = attrList;
						}else {
							summary.summaryAttrs = null;
						}
						summary.content = xpp.nextText();
						entry.summary = summary;
					}else if(Constant.News.AUTHOR.equals(tag) && depth == 3){
						Author author = new Author();
						entry.author = author;
					}else if(Constant.News.NAME.equals(tag) && depth == 4){
						entry.author.name = xpp.nextText();
					}else if(Constant.News.SOURCE.equals(tag)){
						Source source = new Source();
						int count = xpp.getAttributeCount();
						ArrayList<Attr> attrList = new ArrayList<Attr>();
						Attr a = null;
						for(int i = 0; i < count; i++){
							a = new Attr();
							a.nameSpace = xpp.getAttributeNamespace(i);
							a.attrName = xpp.getAttributeName(i);
							a.attrVaue = xpp.getAttributeValue(i);
							attrList.add(a);
						}
						if(attrList.size() > 0){
							source.sourceAttrs = attrList;
						}else {
							source.sourceAttrs = null;
						}
						entry.source = source;
					}else if(Constant.News.ID.equals(tag) && depth == 4){
						entry.source.id = xpp.nextText();
					}else if(Constant.News.TITLE.equals(tag) && depth == 4){
						Title title = new Title();
						int count = xpp.getAttributeCount();
						ArrayList<Attr> attrList = new ArrayList<Attr>();
						Attr a = null;
						for(int i = 0; i < count; i++){
							a = new Attr();
							a.nameSpace = xpp.getAttributeNamespace(i);
							a.attrName = xpp.getAttributeName(i);
							a.attrVaue = xpp.getAttributeValue(i);
							attrList.add(a);
						}
						if(attrList.size() > 0){
							title.titleAttr = attrList;
						}else {
							title.titleAttr = null;
						}
						title.content = xpp.nextText();
						entry.source.title = title;
					}else if(Constant.News.LINK.equals(tag) && depth == 4){
						Link link = new Link();
						int count = xpp.getAttributeCount();
						ArrayList<Attr> attrList = new ArrayList<Attr>();
						Attr a = null;
						for(int i = 0; i < count; i++){
							a = new Attr();
							a.nameSpace = xpp.getAttributeNamespace(i);
							a.attrName = xpp.getAttributeName(i);
							a.attrVaue = xpp.getAttributeValue(i);
							attrList.add(a);
						}
						if(attrList.size() > 0){
							link.linkAttrs = attrList;
						}else {
							link.linkAttrs = null;
						}
						entry.source.link = link;
					}
					
				}else if(eventType == XmlPullParser.END_TAG){
					if(Constant.News.ENTRY.equals(tag)){
						items.entrys.add(entry); 
					}
				}
				eventType = xpp.next();
			}
		}catch (XmlPullParserException e) {
			e.printStackTrace();
			return null;
		}catch (IOException e){
			e.printStackTrace();
			return null;
		}
		return items;
	}
}
