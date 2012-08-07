package com.sixthsense.twitterfeed.ui;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URI;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpResponse;
import org.apache.http.auth.AuthScope;
import org.apache.http.auth.Credentials;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.ListActivity;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.Menu;
import android.view.Window;
import android.widget.Toast;

import com.sixthsense.twitterfeed.R;
import com.sixthsense.twitterfeed.twitter.TweetDetails;
import com.sixthsense.twitterfeed.twitter.TweetListAdapter;
import com.sixthsense.twitterfeed.twitter.TwitterFeedConstanst;

public class TwitterFeedActivity extends ListActivity {

    public static final String TAG = "TwitterFeedActivity";
    private List<TweetDetails> tweetDetailsList = new ArrayList<TweetDetails>();
    private Context context = TwitterFeedActivity.this;
    private TweetListAdapter tweetListAdapter;
    private String titterUserName = null;
    private String titterPassword = null;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTitleProgressBar();
        setContentView(R.layout.activity_twitter_feed);
        extractUserNameAndPassword();
        startBackgroundTwitterFeedTask();
        setListAdapter();
        makeTitleProgressBarVisible();
    }

    private void extractUserNameAndPassword() {
        Bundle bundleData = new Bundle();
        bundleData = getIntent().getExtras();

        if (bundleData != null) {
            titterUserName = bundleData.getString(TwitterLoginActivity.USER_INTENT);
            titterPassword = bundleData.getString(TwitterLoginActivity.PASSWORD_INTENT);
        }
    }

    private void startBackgroundTwitterFeedTask() {
        if (titterUserName != null && titterPassword != null)
            new requestTweetDataAndPopulateList().execute();
        else {
            hideTitleProgressBar();
            Toast.makeText(context, "Invalid userName and Passwords", Toast.LENGTH_SHORT).show();
        }
    }

    private void setListAdapter() {
        tweetListAdapter = new TweetListAdapter(context, tweetDetailsList);
        if (null != tweetListAdapter)
            setListAdapter(tweetListAdapter);
    }

    private void makeTitleProgressBarVisible() {
        setProgressBarIndeterminateVisibility(true);
        setProgressBarVisibility(true);
    }

    private void setTitleProgressBar() {
        requestWindowFeature(Window.FEATURE_INDETERMINATE_PROGRESS);
        requestWindowFeature(Window.FEATURE_PROGRESS);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_twitter_feed, menu);
        return true;
    }

    private class requestTweetDataAndPopulateList extends AsyncTask<Void, Void, Void> {
        private ProgressDialog progressDialog;

        @Override
        protected void onPreExecute() {
            progressDialog = ProgressDialog.show(context, "", getString(R.string.twitter_feed_loading_msg), true);
        };

        @SuppressWarnings("finally")
        @Override
        protected Void doInBackground(Void... params) {
            InputStream inputStream = null;
            try {
                DefaultHttpClient client = new DefaultHttpClient();
                Credentials creds = new UsernamePasswordCredentials(titterUserName, titterPassword);
                client.getCredentialsProvider().setCredentials(new AuthScope(AuthScope.ANY_HOST, AuthScope.ANY_PORT),
                        creds);
                HttpGet request = new HttpGet();
                request.setURI(new URI(TwitterFeedConstanst.getTweeterFeedUrlForBanking()));
                HttpResponse response = client.execute(request);
                inputStream = response.getEntity().getContent();
                BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
                parseTweetAndDisplay(reader);

            } catch (Exception e) {
                Log.e(TAG, "Exception in doInBackground" + e.toString());
            } finally {
                if (inputStream != null) {
                    try {
                        inputStream.close();
                    } catch (IOException e) {

                    }
                }
                return null;
            }

        }

        private void parseTweetAndDisplay(BufferedReader reader) {
            try {
                String line = "";
                do {
                    line = reader.readLine();
                    JSONObject tweetObject = null;
                    try {
                        tweetObject = new JSONObject(line);
                    } catch (JSONException jE) {
                        continue;
                    }
                    if (tweetObject.has(TwitterFeedConstanst.TWITTER_FEED_JSON_FIELD_TEXT)) {
                        tweetDetailsList.add(new TweetDetails(tweetObject
                                .getString(TwitterFeedConstanst.TWITTER_FEED_JSON_FIELD_TEXT)));
                        if (tweetDetailsList.size() == TwitterFeedConstanst.LAST_TWEET) {
                            UiListUpdater.sendEmptyMessage(0);
                            return;
                        }

                    }
                    if (tweetDetailsList.size() == TwitterFeedConstanst.FIRST_TWEET) {
                        dismissDialog();
                    }
                    UiListUpdater.sendEmptyMessage(0);
                } while (tweetDetailsList.size() != TwitterFeedConstanst.LAST_TWEET);
            } catch (Exception e) {
                Log.e(TAG, "exception =" + e.getMessage());
            }
        }

        private void dismissDialog() {
            if (progressDialog != null) {
                progressDialog.dismiss();
            }
        }

        protected void onPostExecute(Void unused) {
            dismissDialog();
            Toast.makeText(context, getString(R.string.twitter_feed_completion_msg), Toast.LENGTH_SHORT).show();
            hideTitleProgressBar();

        }

    }

    private void hideTitleProgressBar() {
        setProgressBarIndeterminateVisibility(false);
        setProgressBarVisibility(false);
    }

    private Handler UiListUpdater = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            tweetListAdapter.notifyDataSetChanged();
        }
    };

}