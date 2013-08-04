package com.android.demo.accounts;

import java.io.IOException;
import java.io.InputStream;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpVersion;
import org.apache.http.StatusLine;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.conn.ClientConnectionManager;
import org.apache.http.conn.params.ConnManagerParams;
import org.apache.http.conn.scheme.PlainSocketFactory;
import org.apache.http.conn.scheme.Scheme;
import org.apache.http.conn.scheme.SchemeRegistry;
import org.apache.http.conn.ssl.SSLSocketFactory;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.conn.tsccm.ThreadSafeClientConnManager;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.apache.http.params.HttpProtocolParams;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;

import android.util.Log;

public class CustomerHttpClient {
	
	private static final String TAG = "CustomerHttpClient";
    private static final String CHARSET = HTTP.UTF_8;
    public static final int HTTP_REQUEST_TIMEOUT_MS = 30 * 1000;
    public static final int HTTP_SO_TIMEOUT_MS = 60 * 1000;
    private static HttpClient customerHttpClient;

    private static final String AUTH_NAME = "Authorization";
	private static final String AUTH_VALUE = "GoogleLogin auth=";
    private CustomerHttpClient() {
    }

    public static synchronized HttpClient getHttpClient() {
        if (null == customerHttpClient) {
            HttpParams params = new BasicHttpParams();
            // base params
            HttpProtocolParams.setVersion(params, HttpVersion.HTTP_1_1);
            HttpProtocolParams.setContentCharset(params,
                    CHARSET);
            HttpProtocolParams.setUseExpectContinue(params, true);
            HttpProtocolParams
                    .setUserAgent(
                            params,
                            "Mozilla/5.0(Linux;U;Android 2.2.1;en-us;Nexus One Build.FRG83) "
                                    + "AppleWebKit/553.1(KHTML,like Gecko) Version/4.0 Mobile Safari/533.1");
            // Timeout setting
            /* Time for connection from connection Pool  */
            ConnManagerParams.setTimeout(params, HTTP_REQUEST_TIMEOUT_MS);
            /* Time for connection */
            HttpConnectionParams.setConnectionTimeout(params, HTTP_REQUEST_TIMEOUT_MS);
            /* Time for request */
            HttpConnectionParams.setSoTimeout(params, HTTP_SO_TIMEOUT_MS);
          
            // HttpClient suport two model such as http and https
            SchemeRegistry schReg = new SchemeRegistry();
            schReg.register(new Scheme("http", PlainSocketFactory
                    .getSocketFactory(), 80));
            schReg.register(new Scheme("https", SSLSocketFactory
                    .getSocketFactory(), 443));

            // Create HttpClient using security of thread manager
            ClientConnectionManager conMgr = new ThreadSafeClientConnManager(
                    params, schReg);
            customerHttpClient = new DefaultHttpClient(conMgr, params);
        }
        return customerHttpClient;
    }
    
    private void shutdownHttpClient() {
        if (getHttpClient() != null && getHttpClient().getConnectionManager() != null) {
        	getHttpClient().getConnectionManager().shutdown();
        	customerHttpClient = null;
        }
    }
    public static InputStream executeGet(String url, String auth){
    	InputStream inputStream = null;
		HttpGet httpGet = new HttpGet(url);
		HttpResponse httpResponse = null;
		int statusCode = 0;
		HttpEntity httpEntity = null;
		StatusLine statusLine = null;
		httpGet.addHeader(AUTH_NAME, AUTH_VALUE + auth);		
		try {
			httpResponse = getHttpClient().execute(httpGet);
			if(httpResponse != null){
				statusLine = httpResponse.getStatusLine();
				if(statusLine != null){
					statusCode = statusLine.getStatusCode();
				}
				Log.d(TAG, "statusCode = " + statusCode);
				httpEntity = httpResponse.getEntity();
				Log.d(TAG, "coding = " + EntityUtils.getContentCharSet(httpEntity));
				if(httpEntity != null){
					inputStream = httpEntity.getContent();
				}
			}
			Log.d(TAG, "inputStream = " + inputStream);
			return inputStream;
		} catch (ClientProtocolException e) {
			e.printStackTrace();
			return null;
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		}finally{
//			getHttpClient().getConnectionManager().shutdown();
		}
    }
}
