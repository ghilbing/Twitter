package com.codepath.apps.restclienttemplate.activities;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.NavUtils;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
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
import com.codepath.apps.restclienttemplate.models.Media;
import com.codepath.apps.restclienttemplate.models.Tweet;
import com.codepath.apps.restclienttemplate.models.User;
import com.codepath.apps.restclienttemplate.utils.LinkifieldTextView;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.json.JSONException;
import org.json.JSONObject;
import org.parceler.Parcels;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import cz.msebera.android.httpclient.Header;

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
    @Bind(R.id.btnRetweet)
    Button btnRetweet;
    @Bind(R.id.btnFavorite)
    Button btnFavorite;



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
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle(R.string.details);
        tweet = Parcels.unwrap(getIntent().getParcelableExtra("tweet"));
        user = Parcels.unwrap(getIntent().getParcelableExtra("user"));
        entity = Parcels.unwrap(getIntent().getParcelableExtra("entity"));

        if (tweet != null) {
            Log.i("Tweetdetail", user.getName());
            Log.i("Tweetentity", tweet.getEntity().getMedia().toString());
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
       // tvFavoriteCount.setText(String.valueOf(tweet.getFavoriteCount()));
       // tvRetweetCount.setText(String.valueOf(tweet.getRetweetCount()));
        Glide.with(this).load(user.getProfileImage()).into(ivProfileImg);

        ivPhoto.setImageResource(0);
      /* if (entity != null && !entity.getMedia().isEmpty()) {

            Glide.with(this).load(tweet.getEntity().getMedia()).into(ivPhoto);
            Log.i("PHOTO", tweet.getEntity().getMedia().toString());
        }*/

        String mediaUrl = mediaUrl(tweet);

        if (entity != null && mediaUrl != null) {
            Glide.with(this).load(mediaUrl).into(ivPhoto);
            ivPhoto.setVisibility(View.VISIBLE);
        } else {
            ivPhoto.setVisibility(View.GONE);
        }



        tvFavoriteCount.setText("");
        tvRetweetCount.setText("");

        if (tweet.getRetweetCount() > 0) {
            tvRetweetCount.setText(String.valueOf(tweet.getRetweetCount()));
        }


        if (tweet.getFavoriteCount() > 0) {
            tvFavoriteCount.setText(String.valueOf(tweet.getFavoriteCount()));

        }

        if (tweet.getFavorited()) {
            btnFavorite.setBackgroundResource(R.drawable.ic_favorite_on);
        }

        if (tweet.getRetweeted())

        {
            btnRetweet.setBackgroundResource(R.drawable.ic_retweet_on);
        }

        btnFavorite.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    favorTweet(tweet, tvFavoriteCount, btnFavorite);
                    Log.i("FAVDETAIL", "WORKS");
                }
        });

        btnRetweet.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    reTweet(tweet, tvRetweetCount, btnRetweet);
                    Log.i("RETWEETDETAIL", "WORKS");
                }
        });

    }

    @Nullable
    private static String mediaUrl(Tweet tweet) {
        Entity entity = tweet.getEntity();
        if (entity != null) {
            List<Media> media = entity.getMedia();
            if (!media.isEmpty()) {
                Log.i("MEDIA", media.get(0).getMediaUrl().toString());
                return media.get(0).getMediaUrl();

            }
        }

        return null;
    }


    public void replyToTweet(View view) {

        ReplyTweetFragment myDialog = ReplyTweetFragment.newInstance(true, tweet, user);
        FragmentManager fm = getSupportFragmentManager();
        myDialog.show(fm, "reply tweet");
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_compose, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        switch (id){
            case android.R.id.home:
                NavUtils.navigateUpFromSameTask(this);
                return true;

            case R.id.logout:
            TwitterApp.getRestClient().clearAccessToken();
            Intent i = new Intent(this, LoginActivity.class);
            startActivity(i);
        }

        return super.onOptionsItemSelected(item);
    }


    private void favorTweet(final Tweet tweet, final TextView tvFavoriteCount, final Button btnFavorite) {

        client.favoriteTweet(new JsonHttpResponseHandler(){
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                Log.d("DEBUG", "favorited" + response.toString());
                try {
                    if(response.getBoolean("favorited")){
                        tweet.setFavoriteCount(Integer.parseInt(response.getString("favorite_count")));
                        btnFavorite.setBackground(getResources().getDrawable(R.drawable.ic_favorite_on));
                    }else{
                        btnFavorite.setBackground(getResources().getDrawable(R.drawable.ic_favorite));
                    }
                    tweet.setFavorited(response.getBoolean("favorited"));
                    if(response.getLong("favorite_count") > 0) {
                        tvFavoriteCount.setText(String.valueOf(response.getLong("favorite_count")));
                    }else{
                        tvFavoriteCount.setText("");

                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                Log.d("DEBUG", responseString);

            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                Log.d("DEBUG", errorResponse.toString());

            }
        }, tweet.getFavorited(), tweet.getId());

    }


    private void reTweet(final Tweet tweet, final TextView tvRetweetCount, final Button btnRetweet) {

        client.reTweet(new JsonHttpResponseHandler(){
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                Log.d("DEBUG", "retweeted" + response.toString());
                try {
                    if(response.getBoolean("retweeted")){
                        tweet.setRetweetCount(Integer.parseInt(response.getString("retweet_count")));
                        btnRetweet.setBackground(getResources().getDrawable(R.drawable.ic_retweet_on));
                    }else{
                        btnRetweet.setBackground(getResources().getDrawable(R.drawable.ic_retweet));
                    }
                    tweet.setRetweeted(response.getBoolean("retweeted"));
                    if(response.getLong("retweet_count") > 0) {
                        tvRetweetCount.setText(String.valueOf(response.getLong("retweet_count")));
                    }else{
                        tvRetweetCount.setText("");

                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                Log.d("DEBUG", responseString);

            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                Log.d("DEBUG", errorResponse.toString());

            }
        }, tweet.getRetweeted(), tweet.getId());

    }
}
