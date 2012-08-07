package com.sixthsense.twitterfeed.twitter;

import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.sixthsense.twitterfeed.R;

public class TweetListAdapter extends BaseAdapter {

    private static final String TAG = "OfferDealsAdapter";

    private final LayoutInflater layoutInflater;
    private Context context;
    private List<TweetDetails> tweetDetailsList;

    public TweetListAdapter(Context context, List<TweetDetails> tweetList) {
        this.context = context;
        this.layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.tweetDetailsList = tweetList;
    }

    public int getCount() {
        if (tweetDetailsList != null) {
            return tweetDetailsList.size();
        }
        return 0;
    }

    public TweetDetails getItem(int position) {
        if (tweetDetailsList != null) {
            return tweetDetailsList.get(position);
        }
        return null;
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        View tweetFeedListItemView;
        if (convertView == null) {
            tweetFeedListItemView = layoutInflater.inflate(R.layout.tweed_feed_items, parent, false);
        } else {
            tweetFeedListItemView = convertView;
        }
        TweetDetails tweetDetails = getItem(position);
        if (tweetDetails == null) {
            throw new IllegalStateException("couldn't move get item at position " + position);
        }
        TextView tweet = (TextView) tweetFeedListItemView.findViewById(R.id.tweet_feed_message);
        tweet.setText(tweetDetails.getTweet());
        return tweetFeedListItemView;
    }

    public long getItemId(int position) {
        // TODO Auto-generated method stub
        return 0;
    }

}
