/*
 * Copyright (C) 2010 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 */

package com.android.demo.accounts;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.conn.params.ConnManagerParams;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.apache.http.protocol.HTTP;

import android.util.Log;

/**
 * Provides utility methods for communicating with the server.
 */
final public class GoogleAccountAuthorization  {
    /** The tag used to log to adb console. */
    private static final String TAG = "NetworkUtilities";
    public static final String PARAM_SERVICE = "service";
    
    public static final String PARAM_ACCOUNT_TYPE = "accountType";
    /** POST parameter name for the user's account name */
    public static final String PARAM_EMAIL = "Email";
    /** POST parameter name for the user's password */
    public static final String PARAM_PASSWORD = "Passwd";
    public static final String PARAM_SOURCE = "source";
    public static final String PARAM_CONTINUE = "continue";
    /** POST parameter name for the user's authentication token */
    public static final String PARAM_AUTH_TOKEN = "authtoken";
    /** POST parameter name for the client's last-known sync state */
    public static final String PARAM_SYNC_STATE = "syncstate";
    /** POST parameter name for the sending client-edited contact info */
    public static final String PARAM_CONTACTS_DATA = "contacts";
    /** Timeout (in ms) we specify for each http request */
    public static final int HTTP_REQUEST_TIMEOUT_MS = 30 * 1000;
    /** Base URL for the v2 Sample Sync Service */
    public static final String BASE_URL = "https://samplesyncadapter2.appspot.com";
    /** URI for authentication service */
    public static final String AUTH_URI = BASE_URL + "/auth";
    /** URI for sync service */
    public static final String SYNC_CONTACTS_URI = BASE_URL + "/sync";
    
    public static final String LOGIN_URL = "https://www.google.com/accounts/ClientLogin";
    private GoogleAccountAuthorization() {
    }

    /**
     * Configures the httpClient to connect to the URL provided.
     */
    /*
    public static DefaultHttpClient getHttpClient() {
    	DefaultHttpClient httpClient = new DefaultHttpClient();
        final HttpParams params = httpClient.getParams();
        HttpConnectionParams.setConnectionTimeout(params, HTTP_REQUEST_TIMEOUT_MS);
        HttpConnectionParams.setSoTimeout(params, HTTP_REQUEST_TIMEOUT_MS);
        ConnManagerParams.setTimeout(params, HTTP_REQUEST_TIMEOUT_MS);
        return httpClient;
    }
	*/
    /**
     * Connects to the SampleSync test server, authenticates the provided
     * username and password.
     *
     * @param email The server account username
     * @param password The server account password
     * @return String The authentication token returned by the server (or null)
     */
    public static String authenticate(String type, String email, String password, String service, String source, String continueTo) {

    	HttpClient client = CustomerHttpClient.getHttpClient();
        final HttpResponse resp;
        final ArrayList<NameValuePair> params = new ArrayList<NameValuePair>();
        params.add(new BasicNameValuePair(PARAM_ACCOUNT_TYPE, type));
        params.add(new BasicNameValuePair(PARAM_EMAIL, email));
        params.add(new BasicNameValuePair(PARAM_PASSWORD, password));
        if(service != null){
        	params.add(new BasicNameValuePair(PARAM_SERVICE, service));
        }
        if(source != null){
        	params.add(new BasicNameValuePair(PARAM_SOURCE, source));
        }
        if(continueTo != null){
        	params.add(new BasicNameValuePair(PARAM_CONTINUE, continueTo));
        }
        final HttpEntity entity;
        try {
            entity = new UrlEncodedFormEntity(params, HTTP.UTF_8);
        } catch (final UnsupportedEncodingException e) {
            // this should never happen.
            throw new IllegalStateException(e);
        }
        
        Log.i(TAG, "Authenticating to: " + LOGIN_URL);
        final HttpPost post = new HttpPost(LOGIN_URL);        
        post.addHeader(entity.getContentType());
        post.setEntity(entity);
        try {
            resp = client.execute(post);
            String authToken = null;
            if (resp.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
            	HttpEntity entiry = resp.getEntity();
                InputStream istream = (entiry != null) ? entiry.getContent()
                        : null;
                if (istream != null) {
                    BufferedReader ireader = new BufferedReader(new InputStreamReader(istream));
                    String temp = null;
                    while((temp = ireader.readLine()) != null){
                    	if(temp.startsWith("Auth=")){
                    		authToken = temp.substring("Auth=".length());
                    		break;
                    	}
                    }
                }
            }
            if ((authToken != null) && (authToken.length() > 0)) {
                Log.v(TAG, "Successful authentication = " + authToken);
                return authToken;
            } else {
                Log.e(TAG, "Error authenticating" + resp.getStatusLine());                
                return null;
            }
        } catch (final IOException e) {
            Log.e(TAG, "IOException when getting authtoken", e);
            return null;
        } finally {
            Log.v(TAG, "getAuthtoken completing");
            
        }
    }
    private static void show(InputStream in){
    	byte buf[] = new byte[1024];
    	String temp = "";
    	try {
			while(in.read(buf)>0){
				temp += new String(buf);
			}
			Log.d(TAG, temp);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    }
}
