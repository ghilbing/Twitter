package com.codepath.apps.restclienttemplate.activities;

import android.content.Context;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.codepath.apps.restclienttemplate.R;
import com.codepath.apps.restclienttemplate.TwitterApp;
import com.codepath.apps.restclienttemplate.TwitterClient;
import com.codepath.apps.restclienttemplate.fragments.ReplyTweetFragment;
import com.codepath.apps.restclienttemplate.models.Entity;
import com.codepath.apps.restclienttemplate.models.Tweet;
import com.codepath.apps.restclienttemplate.models.User;
import com.codepath.apps.restclienttemplate.utils.LinkifieldTextView;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.json.JSONException;
import org.json.JSONObject;
import org.parceler.Parcels;

import butterknife.Bind;
import butterknife.ButterKnife;

import static com.bumptech.glide.Glide.with;

public class DetailActivity extends AppCompatActivity {

    @Bind(R.id.toolbar)
    Toolbar toolbar;
    @Bind(R.id.ivProfileImg)
    ImageView ivProfileImg;
    @Bind(R.id.tvUserName)
    TextView tvUserName;
    @Bind(R.id.tvScreenName)
    TextView tvScreenName;
    @Bind(R.id.tvBody)
    LinkifieldTextView tvBody;
    @Bind(R.id.ivPhoto)
    ImageView ivPhoto;
    @Bind(R.id.tvTweetTime)
    TextView tvTweetTime;
    @Bind(R.id.tvFavoriteCount)
    TextView tvFavoriteCount;
    @Bind(R.id.tvRetweetCount)
    TextView tvRetweetCount;



    TwitterClient client;

    private Tweet tweet;
    private User user;
    private Entity entity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        ButterKnife.bind(this);
        client = TwitterApp.getRestClient();

        // Make sure the toolbar exists in the activity and is not null
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setTitle("Details");
        tweet = Parcels.unwrap(getIntent().getParcelableExtra("tweet"));
        user = Parcels.unwrap(getIntent().getParcelableExtra("user"));
        entity = Parcels.unwrap(getIntent().getParcelableExtra("entity"));

        if (tweet != null) {
            Log.i("Tweetdetail", user.getName());
            loadViewItems(tweet, user, entity);
        } else {
            Log.i("Tweetdetail", " tweet is null!");

        }
    }

    private void loadViewItems(final Tweet tweet, User user, Entity entity) {
        tvUserName.setText(user.getName());
        tvScreenName.setText(user.getScreenName());
        tvBody.setText(tweet.getBody());
        tvTweetTime.setText(tweet.getCreatedAt());
        Glide.with(this).load(user.getProfileImage()).into(ivProfileImg);

        ivPhoto.setImageResource(0);
        if (entity != null && !entity.getMedia().isEmpty()) {

            Glide.with(this).load(tweet.getEntity().getMedia()).into(ivPhoto);
            Log.i("PHOTO", tweet.getEntity().getMedia().toString());
        }

        if (tweet.getRetweetCount() > 0) {
            tvRetweetCount.setText(String.valueOf(tweet.getRetweetCount()));
        }

        if (tweet.getFavoriteCount() > 0) {
            tvFavoriteCount.setText(String.valueOf(tweet.getFavoriteCount()));

        }
    }



    public void replyToTweet(View view) {

        ReplyTweetFragment myDialog = ReplyTweetFragment.newInstance(true, tweet, user);
        FragmentManager fm = getSupportFragmentManager();
        myDialog.show(fm, "reply tweet");
    }
}
