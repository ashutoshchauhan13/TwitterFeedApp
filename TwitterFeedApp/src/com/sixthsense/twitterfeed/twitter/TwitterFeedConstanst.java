package com.sixthsense.twitterfeed.twitter;

public class TwitterFeedConstanst {

	public static String BASE_TWEETER_STREAM_FILTER_URL = "https://stream.twitter.com/1/statuses/filter.json?track=";
	public static String BANKING_KEYWORD = "Banking";	
	public static String TWITTER_FEED_JSON_FIELD_TEXT = "text";	
	public static int NO_OF_TWEETS = 10;
	public static int FIRST_TWEET = 1;
	public static int LAST_TWEET = 10;
    public static String getTweeterFeedUrl(String keyword) {
        return BASE_TWEETER_STREAM_FILTER_URL+keyword;
    }

    public static String getTweeterFeedUrlForBanking() {
        return getTweeterFeedUrl(BANKING_KEYWORD);
    }

	private TwitterFeedConstanst() {
	}

}
