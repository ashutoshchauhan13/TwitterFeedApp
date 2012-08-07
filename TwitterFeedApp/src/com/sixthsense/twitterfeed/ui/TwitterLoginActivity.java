package com.sixthsense.twitterfeed.ui;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;

import com.sixthsense.twitterfeed.R;

public class TwitterLoginActivity extends Activity implements OnClickListener {

    private static final String TAG = "TwitterLoginActivity";
    private EditText userNameField = null;
    private EditText passwordField = null;

    private String userName = null;
    private String password = null;
    public static String PASSWORD_INTENT = "password";
    public static String USER_INTENT = "user";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.twitter_login);
        Button LoginButton = (Button) findViewById(R.id.twitter_login_button);
        LoginButton.setOnClickListener(this);
        userNameField = (EditText) findViewById(R.id.twitter_login_username);
        passwordField = (EditText) findViewById(R.id.twitter_login_password);
    }

    @Override
    protected void onPause() {
        super.onPause();

    }

    public void onClick(View v) {
        if (v.getId() == R.id.twitter_login_button) {
            userName = userNameField.getText().toString();
            password = passwordField.getText().toString();
            if (password.equals(" ") && userName.equals(" ")) {
                return;
            } else {
                Intent intent = new Intent(getApplicationContext(), TwitterFeedActivity.class);
                intent.putExtra(USER_INTENT, userName);
                intent.putExtra(PASSWORD_INTENT, password);
                startActivity(intent);
                TwitterLoginActivity.this.finish();

            }
        }
    }

}
