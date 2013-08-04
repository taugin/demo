package com.android.demo.reader.info;

public class UserInfo {
	public final static String USER_ID = "userId";
	public final static String USER_NAME = "userName";
	public final static String USER_PROFILE_ID = "userProfileId";
	public final static String USER_EMAIL = "userEmail";
	public final static String BLOGGER_USER = "isBloggerUser";
	public final static String SIGNUPTIME_SPEC = "signupTimeSec";
	public final static String MULTI_LOGIN_ENABLED = "isMultiLoginEnabled";
	
	public String userId;
	public String userName;
	public String userProfileId;
	public String userEmail;
	public boolean isBloggerUser;
	public long signupTimeSec;
	public boolean isMultiLoginEnabled;
	private String HTML = "<p>";
	@Override
	public String toString() {
		String output = null;
		output =  HTML + "userId = " + userId + "\n"
				+ HTML + "userName = " + userName + "\n"
				+ HTML + "userProfileId = " + userProfileId + "\n"
				+ HTML + "userEmail = " + userEmail + "\n"
				+ HTML + "isBloggerUser = " + isBloggerUser + "\n"
				+ HTML + "signupTimeSec = " + signupTimeSec + "\n"
				+ HTML + "isMutliLoginEnabled = " + isMultiLoginEnabled;
		return output;
	}
	
	
	
}
