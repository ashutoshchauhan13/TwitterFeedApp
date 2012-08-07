package com.sixthsense.twitterfeed.twitter;

public class TweetDetails {
	
	public TweetDetails(String tweet, String userName) {
		this.tweet = tweet;
		this.userName = userName;
	}
	
	public TweetDetails(String tweet) {
		this.tweet = tweet;
	}
	
	private String tweet;
	private String userName;
	public String getTweet() {
		return tweet;
	}
	public void setTweet(String tweet) {
		this.tweet = tweet;
	}
	public String getUserName() {
		return userName;
	}
	public void setUserName(String userName) {
		this.userName = userName;
	}
}
