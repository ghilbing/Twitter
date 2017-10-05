package com.codepath.apps.restclienttemplate.activities;

import android.content.Context;
import android.content.Intent;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.net.Uri;
import android.provider.ContactsContract;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.test.suitebuilder.TestMethod;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.TextPaint;
import android.text.style.AbsoluteSizeSpan;
import android.text.style.ForegroundColorSpan;
import android.text.style.MetricAffectingSpan;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.codepath.apps.restclienttemplate.R;
import com.codepath.apps.restclienttemplate.TwitterApp;
import com.codepath.apps.restclienttemplate.TwitterClient;
import com.codepath.apps.restclienttemplate.fragments.UserFragment;
import com.codepath.apps.restclienttemplate.models.User;
import com.codepath.apps.restclienttemplate.utils.Utils;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.json.JSONException;
import org.json.JSONObject;
import org.parceler.Parcels;

import butterknife.Bind;
import butterknife.ButterKnife;
import cz.msebera.android.httpclient.Header;
import jp.wasabeef.glide.transformations.RoundedCornersTransformation;

import static com.codepath.apps.restclienttemplate.R.id.toolbar;
import static com.codepath.apps.restclienttemplate.R.id.tvDescription;
import static com.codepath.apps.restclienttemplate.R.id.tvRetweetCount;
import static com.codepath.apps.restclienttemplate.R.id.tvScreenName;
import static com.codepath.apps.restclienttemplate.R.id.tvTagline;
import static com.codepath.apps.restclienttemplate.R.id.tvUserName;
import static com.codepath.apps.restclienttemplate.models.User_Table.screenName;

public class ProfileActivity extends AppCompatActivity {


    @Bind(R.id.ivBackground)
    ImageView ivBackground;
    @Bind(R.id.toolbar)
    Toolbar toolbar;
    @Bind(R.id.ivProfileImage)
    ImageView ivProfileImage;
    @Bind(R.id.tvProfileName)
    TextView tvProfileName;
    @Bind(R.id.tvUserName)
    TextView tvUserName;
    @Bind(R.id.tvTweetCount)
    TextView tvTweetCount;
    @Bind(R.id.tvUserFollowerCount)
    TextView tvFollowerCount;
    @Bind(R.id.tvUserFollowingCount)
    TextView tvFollowingCount;
    @Bind(R.id.tvTagline)
    TextView tagline;

    public enum Follow {
        Following, Follower;
    }


    TwitterClient client = TwitterApp.getRestClient();

    User user;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        ButterKnife.bind(this);

        user = Parcels.unwrap(getIntent().getParcelableExtra("user"));

        String username = user.getScreenName();



        if (savedInstanceState == null) {


            UserFragment userFragment = UserFragment.newInstance(username);
            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
            transaction.replace(R.id.profileFragment, userFragment);
            transaction.commit();
        }

        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        populateUser(username);

    }

    private void populateUser(String username) {



            if (username != null) {
                Log.i("UserName", username.toString());
                client.getUser(username, handler);
            } else {
                client.getCredentials(handler);
            }


    }

    private JsonHttpResponseHandler handler = new JsonHttpResponseHandler() {
        @Override
        public void onSuccess(int statusCode, Header[] headers, JSONObject response) {

            try {
                int friends = response.getInt("friends_count");
                int followers = response.getInt("followers_count");
                int statuses = response.getInt("statuses_count");

                String fullName = response.getString("name");
                String screenName = response.getString("screen_name");
                String description = response.getString("description");
                String profileImage = response.getString("profile_image_url");
                String backgroundImage = response.getString("profile_background_image_url");

                tvProfileName.setText(fullName);
                tvUserName.setText("@" + screenName);
                tagline.setText(description);
                tvFollowerCount.setText(String.valueOf(followers) + " Followers");
                tvFollowingCount.setText(String.valueOf(friends) + " Following");
                tvTweetCount.setText(String.valueOf(statuses) + " Tweets");

                Glide.with(getApplicationContext())
                        .load(Uri.parse(profileImage))
                        .bitmapTransform(new RoundedCornersTransformation(getApplicationContext(), 10,0))
                        .into(ivProfileImage);
                Glide.with(getApplicationContext()).load(Uri.parse(backgroundImage)).into(ivBackground);

                getSupportActionBar().setTitle("@" + screenName);

            } catch (JSONException e) {

                e.printStackTrace();
            }
        }
    };







    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_user, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.logout) {
            TwitterApp.getRestClient().clearAccessToken();
            Intent i = new Intent(this, LoginActivity.class);
            startActivity(i);
        }

        return super.onOptionsItemSelected(item);
    }







    public void onFollowingCountClick(View view) {

        User user = Parcels.unwrap(getIntent().getParcelableExtra("user"));

        Intent intent = new Intent(this, FollowActivity.class);
        intent.putExtra("user", Parcels.wrap(user));
        intent.putExtra("Follow", Follow.Following);
        startActivity(intent);
    }

    public void onFollowersCountClick(View view) {

        User user = Parcels.unwrap(getIntent().getParcelableExtra("user"));

        Intent intent = new Intent(this, FollowActivity.class);
        intent.putExtra("user", Parcels.wrap(user));
        intent.putExtra("Follow", Follow.Follower);
        startActivity(intent);
    }
}




