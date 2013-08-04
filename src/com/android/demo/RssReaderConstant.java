package com.android.demo;

import android.net.Uri;

public class RssReaderConstant{
	
	public static class Intent {
		public static final String INTENT_RSSSERVICE_BOOT_COMPLETED = "com.motorola.mmsp.rssgen.RSSSERVICE_BOOT_COMPLETED";
		public static final String INTENT_RSSSERVICE_START = "com.motorola.mmsp.rssgen.RSSSERVICE_START";
		public static final String INTENT_RSSWIDGET_UPDATE = "com.motorola.mmsp.rssgen.RSSWIDGET_UPDATE";
		public static final String INTENT_RSSWIDGET_REFRESH = "com.motorola.mmsp.rssgen.RSSWIDGET_START_RFRESH";
		public static final String INTENT_FEED_SUBSCRIBE = "com.motorola.mmsp.rssgen.FEED_SUBSCRIBE";
		public static final String INTENT_NETWORK_NOTAVAILABLE = "com.motorola.mmsp.rssgen.NETWORK_NOTAVAILABLE";
		public static final String INTENT_RSSSERVICE_AUTOREFRESH = "com.motorola.mmsp.rssgen.RSSSERVICE_AUTOREFRESH";
		public static final String INTENT_RSSSERVICE_MANUALREFRESH = "com.motorola.mmsp.rssgen.RSSSERVICE_MANUALREFRESH";
		public static final String INTENT_RSSSERVICE_STARTREFRESH = "com.motorola.mmsp.rssgen.RSSSERVICE_STARTREFRESH";
		public static final String INTENT_RSSSERVICE_ADDFEED = "com.motorola.mmsp.rssgen.RSSSERVICE_ADDFEED";
		public static final String INTENT_RSSSERVICE_VALIDITY_CHECKING = "com.motorola.mmsp.rssgen.RSSSERVICE_VALIDITY_CHECKING";
		public static final String INTENT_RSSWIDGET_DELETE = "com.motorola.mmsp.rssgen.RSSWIDGET_DELETE";
		public static final String INTENT_RSSWIDGET_CONFIG = "com.motorola.mmsp.rssgen.intent.action.APPWIDGET_CONFIGURE";
		public static final String INTENT_RSSAPP_SETTINGS = "com.motorola.mmsp.rssgen.intent.action.SETTINGS";
		public static final String INTENT_RSSAPP_UPDATE_ALARM = "com.motorola.mmsp.rssgen.intent.action.RSSAPP_UPDATE_ALARM";
		public static final String INTENT_RSSAPP_DELETE_FEED = "com.motorola.mmsp.rssgen.intent.action.RSSAPP_DELETE_FEED";
		public static final String INTENT_RSSAPP_ADD_ALARM = "com.motorola.mmsp.rssgen.intent.action.RSSAPP_ADD_ALARM";
		public static final String INTENT_RSSAPP_DELETE_ALARM = "com.motorola.mmsp.rssgen.intent.action.RSSAPP_DELETE_ALARM";
		public static final String INTENT_RSSAPP_SUBSCRIBE_FINISHED = "com.motorola.mmsp.rssgen.intent.action.RSSAPP_SUBSCRIBE_FINISHED";
		public static final String INTENT_RSSAPP_UPDATE_FINISHED = "com.motorola.mmsp.rssgen.intent.action.RSSAPP_UPDATE_FINISHED";
		public static final String INTENT_RSSAPP_FEED_ADDED_FROM_EMPTY_FINISHED = "com.motorola.mmsp.rssgen.intent.action.RSSAPP_FEED_ADDED_FROM_EMPTY_FINISHED";
		public static final String INTENT_RSSAPP_OPENARTICLE_DETAIL = "com.android.rssgen.intent.action.OPEN_ARTICLE_DETAIL";
		public static final String INTENT_RSSAPP_OPENARTICLE_DETAIL_FROMLIST = "com.android.rssgen.intent.action.OPEN_ARTICLE_DETAIL_FROMLIST";
		public static final String INTENT_RSSAPP_CHECK_URL = "com.motorola.mmsp.rssgen.intent.action.CHECK_URL";
		public static final String INTENT_RSSAPP_CHECK_URL_COMPLETE = "com.motorola.mmsp.rssgen.intent.action.CHECK_URL_COMPLETE";
		public static final String INTENT_RSSAPP_EXITED = "com.motorola.mmsp.rssgen.intent.action.RSSAPP_EXITED";
		public static final String INTENT_RSSAPP_STOP_UPDATING = "com.motorola.mmsp.rssgen.intent.STOP_UPDATING";
		public static final String INTENT_RSSAPP_UPDATE_ITEM = "com.motorola.mmsp.rssgen.intent.UPDATE_ITEM";
		public static final String INTENT_RSSAPP_NOTIFY_WIDGET_UPDATE = "com.motorola.mmsp.rssgen.intent.NOTIFY_WIDGET_UPDATE";
	}
	
	public static class Content {
		public static final int DB_VERSION = 1;
		public static final String AUTHORITY = "com.motorola.mmsp.rssgen.database.RssContentProvider";
		public static final String DB_NAME = "rsswidget.db";
		public static final String TABLE_WIDGETS = "widget_table";
		public static final String TABLE_FEEDS = "feed_table";
		public static final String TABLE_ITEMS = "item_table";
		public static final String TABLE_FEED_INDEXS = "feed_index_table";
		public static final String TABLE_ITEM_INDEXS = "item_index_table";
		public static final String TABLE_HISTORYS = "history_table";
		public static final String VIEW_QUERY_ITEM = "view_query_item";
		public static final String VIEW_QUERY_FEED = "view_query_feed";
		public static final String TABLE_ACTIVITYCOUNT = "activitycount_table";
		public static final String VIEW_LIMITED_ITEM = "view_limited_item";
		
		public static final Uri WIDGET_URI = Uri.parse("content://" + AUTHORITY + "/" + TABLE_WIDGETS);
		public static final Uri FEED_URI = Uri.parse("content://" + AUTHORITY + "/" + TABLE_FEEDS);
		public static final Uri ITEM_URI = Uri.parse("content://" + AUTHORITY + "/" + TABLE_ITEMS);
		public static final Uri FEED_INDEX_URI = Uri.parse("content://" + AUTHORITY + "/" + TABLE_FEED_INDEXS);
		public static final Uri ITEM_INDEX_URI = Uri.parse("content://" + AUTHORITY + "/" + TABLE_ITEM_INDEXS);
		public static final Uri HISTORY_URI = Uri.parse("content://" + AUTHORITY + "/" + TABLE_HISTORYS);
		public static final Uri ACTIVITYCOUNT_URI = Uri.parse("content://" + AUTHORITY + "/" + TABLE_ACTIVITYCOUNT);
		public static final Uri VIEW_FEED_URI = Uri.parse("content://" + AUTHORITY + "/" + VIEW_QUERY_FEED);
		public static final Uri VIEW_ITEM_URI = Uri.parse("content://" + AUTHORITY + "/" + VIEW_QUERY_ITEM);
		public static final Uri VIEW_LIMITED_ITEM_URI = Uri.parse("content://" + AUTHORITY + "/" + VIEW_LIMITED_ITEM);

		public static final String WIDGET_CONTENT_TYPE = "vnd.android.cursor.item/vnd.rssgen.items";
		public static final String FEED_CONTENT_TYPE = "vnd.android.cursor.item/vnd.rssgen.feeds";
		public static final String ITEM_CONTENT_TYPE = "vnd.android.cursor.item/vnd.rssgen.items";
		public static final String INDEX_CONTENT_TYPE = "vnd.android.cursor.item/vnd.rssgen.indexs";
		public static final String HISTORY_CONTENT_TYPE = "vnd.android.cursor.item/vnd.rssgen.items";
		public static final String ACTIVITY_COUNT_CONTENT_TYPE = "vnd.android.cursor.item/vnd.rssgen.items";
		
		public static final String WIDGET_CONTENT_ITEM_TYPE = "vnd.android.cursor.item/vnd.rssgen.item";
		public static final String FEED_CONTENT_ITEM_TYPE = "vnd.android.cursor.item/vnd.rssgen.feed";
		public static final String ITEM_CONTENT_ITEM_TYPE = "vnd.android.cursor.item/vnd.rssgen.item";
		public static final String INDEX_CONTENT_ITEM_TYPE = "vnd.android.cursor.item/vnd.rssgen.item";
		public static final String HISTORY_CONTENT_ITEM_TYPE = "vnd.android.cursor.item/vnd.rssgen.item";
		
		public static final String _ID = "_id";
		
		public static final String WIDGET_ID = "widget_id";
		public static final String FEED_ID = "feed_id";
		public static final String ITEM_ID = "item_id";
		public static final String DELETED = "deleted";
		public static final String FOO = "foo";
		
		public static final String WIDGET_TITLE = "widget_title";
		public static final String WIDGET_UPDATE_FREQUENCY = "widget_update_frequency";
		public static final String WIDGET_VALIDITY_TIME = "widget_validaty_time";
		
		public static final String FEED_TITLE = "feed_title";		
		public static final String FEED_URL = "feed_url";
		public static final String FEED_AUTHOR = "feed_author";
		public static final String FEED_IS_BUNDLE = "feed_is_bundle";
		public static final String FEED_ICON = "feed_icon";
		public static final String FEED_PUBDATE = "feed_pubdate";
		public static final String FEED_GUID = "feed_guid";
		
		public static final String ITEM_TITLE = "item_title";
		public static final String ITEM_URL = "item_url";
		public static final String ITEM_DESCRIPTION = "item_description";
		public static final String ITEM_GUID = "item_guid";
		public static final String ITEM_STATE = "item_state";
		public static final String ITEM_AUTHOR = "item_author";
		public static final String ITEM_PUBDATE = "item_pubdate";
		
		public static final String HISTORY_TITLE = "title";
		public static final String HISTORY_URL = "url";
		public static final String HISTORY_TIMES = "times";
		public static final String HISTORY_DATE = "date";
		
		public static final String ACTIVITY_COUNT = "count";
		
		
		public static final String VIEW_WIDGET_ID = "view_widget_id";
		public static final String VIEW_FEED_ID = "view_feed_id";
		public static final String VIEW_FEED_TITLE = "view_feed_title";
		
		public static final String VIEW_ITEM_ID = "view_item_id";
		public static final String VIEW_ITEM_TITLE = "view_item_title";
		public static final String VIEW_ITEM_URL = "view_item_url";
		public static final String VIEW_ITEM_DESCRIPTION = "view_item_description";
		public static final String VIEW_ITEM_AUTHOR = "view_item_author";
		public static final String VIEW_ITEM_STATE = "view_item_state";
		public static final String VIEW_ITEM_PUBDATE = "view_item_pubdate";
				
		/**
		 *Update frequency 
		 */
		public static final int UPDATE_FRE_NEVER = 0;
		public static final int UPDATE_FRE_ONE_HOUR = 1;
		public static final int UPDATE_FRE_THREE_HOURS = 2;
		public static final int UPDATE_FRE_SIX_HOURS = 3;
		public static final int UPDATE_FRE_TWELVE_HOURS = 4;
		public static final int UPDATE_FRE_ONE_DAY = 5;
		
		/**
		 *Show item for 
		 */
		public static final int SHOW_ITEMS_ONE_DAY = 0;
		public static final int SHOW_ITEMS_TWO_DAYS = 1;
		public static final int SHOW_ITEMS_THREE_DAYS = 2;
		public static final int SHOW_ITEMS_ONE_WEEK = 3;
		public static final int SHOW_ITEMS_ONE_MONTH = 4;
		
		
		public static final String SHARED_PREFERENCE_NAME = "rss_widget_shared_preference";
	}
	
	public static class Flag{
		public static final int FEED_REQUESTCODE = 0;
		public static final int FEEDBUNDLELIST_RESULECODE = 1;
		public static final int FEEDCUSTOMADDED_RESULECODE = 2;
		public static final int FEEDCUSTOMCONFIRM_RESULECODE = 3;
		public static final int FEEDADDBYCUSTOM_RESULECODE = 4;
		public static final int FEEDADDBYBUNDLE_RESULECODE = 5;
		public static final int FEEDADDBYHISTORY_RESULECODE = 6;
		public static final int FEEDHISTORYLIST_RESULECODE = 7;
		public static final int WIDGETCONFIG_NEWADDED_RESULTCODE = 8;
		public static final int WIDGETCONFIG_NOADDED_RESULTCODE = 9;
		
		
		public static final int ADD_RSS_NEWS = 1;
		public static final int UPDATE_RSS_NEWS = 2;
		public static final int CHECK_RSS_ADDRESS = 3;
		public static final String ITEM_POSITION = "item_position";
		
		public static final int WIDGET_NOFEED = 0;
		public static final int WIDGET_NOFEED_ITEMSTATE = 0;
	}
	
	public static class State{
		public static final int STATE_ITEM_UNREAD = 0;
		public static final int STATE_ITEM_READ = 1;
		
		public static final int STATE_UNDELETED = 0;
		public static final int STATE_DELETED = 1;
		
		public static final int STATE_NO_BUNDLE = 0;
		public static final int STATE_BUNDLE = 1;
		
		public static final int STATE_SUCCESS = 0;
		
		public static final int STATE_NETWORK_NOT_AVAILABLE = 1;
		
		public static final int STATE_PARSE_XML_FAILURE = 2;
		
		public static final int STATE_URL_NOT_AVAILABLE = 3;
		
		public static final int STATE_CONNECTY_FAILURE = 4;
		
		public static final int STATE_NO_PARSER = 6;
	}	
	
	public static class Key{
		public static final String KEY_WIDGET_ID = "key_widget_id";
		public static final String KEY_FEED_ID = "key_feed_id";
		public static final String KEY_ITEM_ID = "key_item_id";		
		public static final String KEY_CHECK_URL = "key_check_url";
	}
	
	public static class Value{
		public static final long ONE_HOUR = 1 * 3600 * 1000l;
		public static final long THREE_HOURS = 3 * 3600 * 1000l;
		public static final long SIX_HOURS = 6 * 3600 * 1000l;
		public static final long TWELVE_HOURS = 12 * 3600 * 1000l;
		public static final long ONE_DAY = 24 * 3600 * 1000l;
		public static final long TWO_DAYS = 2 * 24 * 3600 * 1000l;
		public static final long THREE_DAYS = 3 * 24 * 3600 * 1000l;
		public static final long ONE_WEEK = 7 * 24 * 3600 * 1000l;
		public static final long ONE_MONTH = 30 * 24 * 3600 * 1000l;
		
		public static final int MAX_FEED_ADDEDNUM  = 50;
		public static final int MAX_HISTORY = 50;
	}
	
}
