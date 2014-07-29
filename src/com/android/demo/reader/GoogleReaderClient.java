package com.android.demo.reader;

import java.io.BufferedReader;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import android.content.Context;
import android.util.Log;

import com.android.demo.reader.atom.News;
import com.android.demo.reader.feedlist.Subscriptions;
import com.android.demo.reader.info.UserInfo;
//http://www.google.com/reader/atom/feed/http://www.naivix.com/rss.xml?n=17&ck=10101010=user/weiliuzhao/state/com.google/read
public final class GoogleReaderClient {
    private static final String TAG = "GoogleReaderClient";
    
    private static final String PARAMS_QUICK_ADD = "quickadd";
    private static final String PARAMS_TOKEN = "T";
    
    private static final String BASE_URL = "http://www.google.com/reader/";
    private static final String USER_INFO_URL = "api/0/user-info";
    private static final String TOKEN = "api/0/token";
    private static final String FEED_LIST = "api/0/subscription/list";
    private static final String ATOM = "atom/";
    private static final String FEED = "feed/";
    private static final String QUICK_ADD_FEED = "api/0/subscription/quickadd";
    private static final String TEMP = "http://www.google.com/reader/atom/user/0/pref/com.google/subscriptions";
    
    public static final int HTTP_REQUEST_TIMEOUT_MS = 30 * 1000;
    
    private String mAuthorization = null;
    private Context mContext = null;
    private UserInfo mUserInfo = null;
    public GoogleReaderClient(Context context, String authorization){
        mContext = context;
        mAuthorization = authorization;
        mUserInfo = requestUserInfo();
    }
    public UserInfo getUserInfo(){
        return mUserInfo;
    }
    public String listAllNews(){
        String feedUrl = "http://www.naivix.com/rss.xml";
        int n = 5;
        String count = "n=" + n;
        long time = System.currentTimeMillis();
        String timeStamp = "&ck=" + time; 
//        String allNewUrl = BASE_URL + ATOM + "feed/http://www.naivix.com/rss.xml?n=1&ck=10101010=user/weiliuzhao/state/com.google/read";
//        String allNewUrl = BASE_URL + ATOM + FEED + feedUrl + "?" + count + timeStamp + "=user/" + mCurrentUser + "/state/com.google/read";
        String allNewUrl = BASE_URL + ATOM + "?" + count + timeStamp + "=user/" + mUserInfo.userName + "/state/com.google/read";
        Log.d(TAG, "allNewUrl = " + allNewUrl);
        InputStream in = CustomerHttpClient.executeGet(allNewUrl, mAuthorization);
        String temp = writeResultToFile(in);
        XmlParser parser = XmlParser.getXmlParser(mContext);
        Log.d(TAG, "temp = = " + temp);
        News news = parser.parseNews(temp);
        temp = news != null ? news.toString() : "Network Error";
        try {
            in.close();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return temp;
    }
    public String listGReaderFeeds(){
        String listfeed = BASE_URL + FEED_LIST + "?" + "output=xml";
        InputStream in = CustomerHttpClient.executeGet(listfeed, mAuthorization);
        if(in == null){
            return "Nerwork Error";
        }
        InputStreamReader isr = new InputStreamReader(in);
        BufferedReader br = new BufferedReader(isr);
        String temp;
        String result = "";;
        try {
            while((temp = br.readLine()) != null){
                result += temp;
            }
            Log.d(TAG, "result = " + result);
            br.close();
            isr.close();
            in.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        result = result.replaceAll("&lt;", "<");
        result = result.replaceAll("&gt;", ">");
        result = result.replaceAll("&amp;", "&");
        XmlParser parser = XmlParser.getXmlParser(mContext);
        Subscriptions xx = parser.parseSubscription(result); 
        if(xx == null){
            return null;
        }
        return xx.toString();
//        return writeResultToFile(in);
        /*
        InputStreamReader isr = new InputStreamReader(in);
        BufferedReader br = new BufferedReader(isr);
        String temp;
        String result = "";;
        try {
            while((temp = br.readLine()) != null){
                result += temp;
            }
            Log.d(TAG, "result = " + result);
            br.close();
            isr.close();
            in.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return result;*/
    }
    private String writeResultToFile(InputStream in){
        String result = "";
        String temp = "";
        try {
            FileOutputStream f = mContext.openFileOutput("GetResult.html", 0);
            if(in == null){
                return "Network Error";
            }
            InputStreamReader isr = new InputStreamReader(in);
            BufferedReader br = new BufferedReader(isr);
            while((temp = br.readLine()) != null){
                result += temp;
            }
//            result = result.replaceAll("&lt;", "<");
//            result = result.replaceAll("&gt;", ">");
//            result = result.replaceAll("&amp;", "&");
            Log.d(TAG, result);
            f.write(result.getBytes());
            br.close();
            isr.close();
            in.close();
            f.close();
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
        return result;
    }
    
    public UserInfo requestUserInfo(){
        UserInfo userInfo = null;
        long time = System.currentTimeMillis();
        String timeStamp = "&ck=" + time; 
        String appName = "&client=greader";
        String output = "&output=xml";
        String requestUserInfo = BASE_URL + USER_INFO_URL + "?" + timeStamp + appName + output;
        Log.d(TAG, "requestUserInfo = " + requestUserInfo);
        InputStream in = CustomerHttpClient.executeGet(requestUserInfo, mAuthorization);
        if(in != null){
            InputStreamReader isr = new InputStreamReader(in);
            BufferedReader br = new BufferedReader(isr);
            String temp;
            String result = "";;
            try {
                while((temp = br.readLine()) != null){
                    result += temp;
                }
                Log.d(TAG, "result = " + result);
                br.close();
                isr.close();
                in.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
            XmlParser parser = XmlParser.getXmlParser(mContext);
            userInfo = parser.parseUserInfo(result);
            result = userInfo.toString();
        }
        return userInfo;
    }
    
    public String getToken(){
        String token = null;
        long time = System.currentTimeMillis();
        String timeStamp = "&ck=" + time; 
        String appName = "&client=greader";
        String tokenUrl = BASE_URL + TOKEN + "?" + timeStamp + appName;
        InputStream in = CustomerHttpClient.executeGet(tokenUrl, mAuthorization);
        if(in != null){
            InputStreamReader isr = new InputStreamReader(in);
            BufferedReader br = new BufferedReader(isr);
            String temp = null;
            try {
                while((temp = br.readLine()) != null){
                    token = temp;
                }
                br.close();
                isr.close();
                in.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return token;
    }
    
    public int quickAddFeed(String feedUrl, String token){
        int result = 0;
        String status = null;
        final ArrayList<NameValuePair> params = new ArrayList<NameValuePair>();
        params.add(new BasicNameValuePair(PARAMS_QUICK_ADD, feedUrl));
        params.add(new BasicNameValuePair(PARAMS_TOKEN, token));
        String quickAddUrl = BASE_URL + QUICK_ADD_FEED + "?" + "output=xml";
        InputStream in = CustomerHttpClient.executePost(quickAddUrl, mAuthorization, params);
        if(in != null){
            InputStreamReader isr = new InputStreamReader(in);
            BufferedReader br = new BufferedReader(isr);
            String temp = null;
            try {
                while((temp = br.readLine()) != null){
                    status = temp;
                }
                br.close();
                isr.close();
                in.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        Log.d(TAG, "status = " + status);
        return result;
    }
    
    public String listFeedItem(String url){
        Log.d(TAG, "url = " + url);
        int n = 5;
        String count = "n=" + n;
        long time = System.currentTimeMillis();
        String timeStamp = "&ck=" + time; 
        String feedUrl = BASE_URL + ATOM + FEED + url + "?" + count + timeStamp + "=user/" + mUserInfo.userName + "/state/com.google/read";
        Log.d(TAG, "allNewUrl = " + feedUrl);
        InputStream in = CustomerHttpClient.executeGet(feedUrl, mAuthorization);
        String temp = writeResultToFile(in);
        XmlParser parser = XmlParser.getXmlParser(mContext);
        Log.d(TAG, "temp = = " + temp);
        News news = parser.parseNews(temp);
        temp = news != null ? news.toString() : "Network Error";
        try {
            in.close();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return temp;
    }
}
