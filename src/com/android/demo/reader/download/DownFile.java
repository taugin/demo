package com.android.demo.reader.download;
import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.StatusLine;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.util.EntityUtils;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Environment;
import android.util.Log;

import com.android.demo.reader.CustomerHttpClient;


public class DownFile {
	String TAG = "DownFile";
	private String mStoreDir;
	private String mFileDir;
	private String mHtmlFile;
	private Context mContext;
	public DownFile(Context context){
		mContext = context;
		initStoreDir();
	}
	/**
	 * 下载附加文件
	 * 
	 * @param httpUrl
	 * @param urlPath
	 */
	public void getHtmlExtraFile(String httpUrl, String urlPath) {
		BufferedInputStream in;
		FileOutputStream file;
		try {
			String filePath = mStoreDir + getFilesPath(urlPath);
			System.out.println(filePath);
			String url = httpUrl + urlPath;
			// create it if not exists
			File uploadFilePath = new File(mFileDir);
			if (uploadFilePath.exists() == false) {
				uploadFilePath.mkdirs();
			}
			System.out.println("url = " + url);
			System.out.println("httpUrl = " + httpUrl);
			System.out.println("filePath = " + filePath);
			in = new BufferedInputStream(getStream(url));
			File f = new File(filePath);
			file = new FileOutputStream(f);
			int t;
			while ((t = in.read()) != -1) {
				file.write(t);
			}
			file.close();
			in.close();
		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	/*
	private InputStream getStream(String httpUrl){
		InetAddress addr;
		try {
			addr = InetAddress.getByName("10.11.19.242");
			InetSocketAddress sa = new InetSocketAddress(addr, 3128);
			Proxy proxy = new Proxy(Proxy.Type.HTTP, sa);
			java.net.Authenticator.setDefault(new java.net.Authenticator() {
				protected java.net.PasswordAuthentication getPasswordAuthentication() {
					      return new java.net.PasswordAuthentication("liuzhao.wei", new String("").toCharArray());
				     }
				});
			URL url = new URL(httpUrl);
			
			URLConnection c = url.openConnection(proxy);
			c.setConnectTimeout(5000);
			return c.getInputStream();
		} catch (UnknownHostException e) {
			e.printStackTrace();
			return null;
		}catch (IOException e) {
			e.printStackTrace();
			return null;
		} 
	}*/

	private InputStream getStream(String httpUrl){
		HttpClient client = CustomerHttpClient.getHttpClient();
		InputStream inputStream = null;
		HttpGet httpGet = new HttpGet(httpUrl);
		HttpResponse httpResponse = null;
		int statusCode = 0;
		HttpEntity httpEntity = null;
		StatusLine statusLine = null;
		try {
			httpResponse = client.execute(httpGet);
			if(httpResponse != null){
				statusLine = httpResponse.getStatusLine();
				if(statusLine != null){
					statusCode = statusLine.getStatusCode();
				}
				Log.d(TAG, "statusCode = " + statusCode);
				httpEntity = httpResponse.getEntity();
				Log.d(TAG, "coding = " + EntityUtils.getContentCharSet(httpEntity));
				if(httpEntity != null && httpEntity.isStreaming()){
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
			Log.d(TAG, "executeGet : finally");
//			getHttpClient().getConnectionManager().shutdown();
			client.getConnectionManager().closeExpiredConnections();
		}
	}
	/**
	 * 获得网页html代码
	 * 
	 * @param httpUrl
	 * @return
	 * @throws IOException
	 */
	public String getHtmlCode(String httpUrl) throws IOException {
		String content = "";
		URL uu = new URL(httpUrl); // 创建URL类对象
		BufferedReader ii = new BufferedReader(new InputStreamReader(
				getStream(httpUrl))); // //使用openStream得到一输入流并由此构造一个BufferedReader对象
		String input;
		while ((input = ii.readLine()) != null) { // 建立读取循环，并判断是否有读取值
			content += input;
		}
		ii.close();
		return content;
	}
	private String getFilesPath(String path){
		String localPath = null;
		int index = path.lastIndexOf('/');
		if(index != -1){
			String sub = path.substring(index + 1);
			System.out.println("sub = " + sub);
			localPath = "files/" + sub;
		}else{
			localPath = "files/" + path;
		}
		return localPath;
	}
	private String handleImageFile(String content){
		System.out.println("handleImageFile~~~~~~~~~~~");
		String searchImgReg = "(src|SRC|background|BACKGROUND)=('|\")(http://[^\\s+]*?/?/)([^\\s+]*?.(jpg|JPG|png|PNG|gif|GIF|swf|SWF))('|\")";
		Pattern pattern = Pattern.compile(searchImgReg);
		Matcher matcher = pattern.matcher(content);
		while (matcher.find()) {
			for(int i = 0;i<matcher.groupCount();i++){
				System.out.println("matcher.group(" + i + ") = " + matcher.group(i));
			}
			getHtmlExtraFile(matcher.group(3), matcher.group(4));
		}
		// Modify the url of picture
		pattern = Pattern.compile(searchImgReg);
		matcher = pattern.matcher(content);
		StringBuffer replaceStr = new StringBuffer();
		while (matcher.find()) {
			for(int i = 0;i<matcher.groupCount();i++){
				System.out.println("matcher.group(" + i + ") : " + matcher.group(i));
			}
			matcher.appendReplacement(replaceStr, matcher.group(1)
					+ "=\"" + getFilesPath(matcher.group(4)) + "\"");
		}
		matcher.appendTail(replaceStr);
		return replaceStr.toString();
	}
	private String handleJsFile(String content){
		System.out.println("handleImageFile~~~~~~~~~~~");
		String searchJsReg = "(src|SRC|background|BACKGROUND)=('|\")(http://[^\\s+]*?/?/)([^\\s+]*?.(JS|js))('|\")";
		Pattern pattern = Pattern.compile(searchJsReg);
		Matcher matcher = pattern.matcher(content);
		while (matcher.find()) {
			for(int i = 0;i<matcher.groupCount();i++){
				System.out.println("matcher.group(" + i + ") = " + matcher.group(i));
			}
			getHtmlExtraFile(matcher.group(3), matcher.group(4));
		}
		// Modify the url of picture
		pattern = Pattern.compile(searchJsReg);
		matcher = pattern.matcher(content);
		StringBuffer replaceStr = new StringBuffer();
		while (matcher.find()) {
			for(int i = 0;i<matcher.groupCount();i++){
				System.out.println("matcher.group(" + i + ") : " + matcher.group(i));
			}
			matcher.appendReplacement(replaceStr, matcher.group(1)
					+ "=\"" + getFilesPath(matcher.group(4)) + "\"");
		}
		matcher.appendTail(replaceStr);
		return replaceStr.toString();
	}
	private String handleCssFile(String content){
		System.out.println("handleImageFile~~~~~~~~~~~");
		String searchCssReg = "(src|SRC|background|BACKGROUND)=('|\")(http://[^\\s+]*?/?/)([^\\s+]*?.(css|CSS))('|\")";
		Pattern pattern = Pattern.compile(searchCssReg);
		Matcher matcher = pattern.matcher(content);
		while (matcher.find()) {
			for(int i = 0;i<matcher.groupCount();i++){
				System.out.println("matcher.group(" + i + ") = " + matcher.group(i));
			}
			getHtmlExtraFile(matcher.group(3), matcher.group(4));
		}
		// Modify the url of picture
		pattern = Pattern.compile(searchCssReg);
		matcher = pattern.matcher(content);
		StringBuffer replaceStr = new StringBuffer();
		while (matcher.find()) {
			for(int i = 0;i<matcher.groupCount();i++){
				System.out.println("matcher.group(" + i + ") : " + matcher.group(i));
			}
			matcher.appendReplacement(replaceStr, matcher.group(1)
					+ "=\"" + getFilesPath(matcher.group(4)) + "\"");
		}
		matcher.appendTail(replaceStr);
		return replaceStr.toString();
	}
	/**
	 * 下载网页代码及图片并替换图片链接地址
	 * 
	 * @param url
	 * @throws IOException
	 */
	public String downloadHtml(String url) throws IOException {
		String content = getHtmlCode(url);
		if((content == null) || (content != null && content.trim().equals(""))){
			return null;
		}
		System.out.println(content);
		
		
		content = handleImageFile(content);
		content = handleJsFile(content);
		content = handleCssFile(content);
		if(content != null && mHtmlFile != null){
			File f = new File(mHtmlFile);
			FileOutputStream fos = new FileOutputStream(f);
			fos.write(content.getBytes());
			fos.close();
			
			SharedPreferences p = mContext.getSharedPreferences("RssWidget", Context.MODE_PRIVATE);
			p.edit().putString(url, mHtmlFile).commit();
		}else{
			Log.d(TAG, "Error write file !");
		}
		
		return mHtmlFile;
	}
	public static String getUUID() {
		UUID uuid = UUID.randomUUID();
		String str = uuid.toString();
		str = str.replaceAll("-", "");
		System.out.println(str);
		return str;
	}
	
	private void initStoreDir(){
		File directory = null;
		String uuid = DownFile.getUUID();
		String state = Environment.getExternalStorageState();
		String dir = null;
		if(Environment.MEDIA_MOUNTED.equals(state)){
			Log.d(TAG, "MEDIA_MOUNTED");
			File external = Environment.getExternalStorageDirectory();
			if(external != null){
				dir = external.getAbsolutePath() + "/" + "RssWidget" + "/";
			}
		}
		Log.d(TAG, "dir = " + dir);
		if(dir == null){
			dir = mContext.getFilesDir().getAbsolutePath() + "/";
		}
		
		directory = new File(dir);
		directory.mkdirs();
		
		
		mStoreDir = dir + uuid + "/";
		directory = new File(mStoreDir);
		directory.mkdirs();
		
		mFileDir = mStoreDir + "files" + "/";
		directory = new File(mFileDir);
		directory.mkdirs();
		
		Log.d(TAG, "htmlDir = " + mStoreDir);
		Log.d(TAG, "filesDir = " + mFileDir);
		
		mHtmlFile = mStoreDir + uuid + ".html";
	}
}
