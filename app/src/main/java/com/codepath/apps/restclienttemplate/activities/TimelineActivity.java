package com.codepath.apps.restclienttemplate.activities;

import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.MenuItemCompat;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.astuetz.PagerSlidingTabStrip;
import com.bumptech.glide.Glide;
import com.codepath.apps.restclienttemplate.R;
import com.codepath.apps.restclienttemplate.TwitterApp;
import com.codepath.apps.restclienttemplate.TwitterClient;
import com.codepath.apps.restclienttemplate.fragments.ComposeFragment;
import com.codepath.apps.restclienttemplate.fragments.MentionsFragment;
import com.codepath.apps.restclienttemplate.fragments.TimelineFragment;
import com.codepath.apps.restclienttemplate.interfaces.IDataCallback;
import com.codepath.apps.restclienttemplate.interfaces.ISearch;
import com.codepath.apps.restclienttemplate.models.Tweet;
import com.codepath.apps.restclienttemplate.models.User;
import com.codepath.apps.restclienttemplate.utils.SmartFragmentStatePagerAdapter;
import com.codepath.apps.restclienttemplate.utils.Utils;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.raizlabs.android.dbflow.sql.language.Select;

import org.json.JSONObject;
import org.parceler.Parcels;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import cz.msebera.android.httpclient.Header;

import static android.R.attr.data;
import static android.icu.lang.UCharacter.GraphemeClusterBreak.T;
import static com.codepath.apps.restclienttemplate.R.id.fabCompose;
import static com.codepath.apps.restclienttemplate.R.id.ivProfileImage;
import static com.codepath.apps.restclienttemplate.R.id.ivProfilePhoto;
import static com.codepath.apps.restclienttemplate.R.id.swipeContainer;
import static com.codepath.apps.restclienttemplate.R.string.tweet;
import static java.util.Collections.addAll;

public class TimelineActivity extends AppCompatActivity {


    @Bind(R.id.viewPager)
    ViewPager viewPager;
    @Bind(R.id.toolbar)
    Toolbar toolbar;
    public static String loggedUserScreenName;
    TweetPagerAdapter pagerAdapter;
    @Bind(R.id.tabs)
    PagerSlidingTabStrip tabStrip;
    @Bind(R.id.fabCompose)
    FloatingActionButton fabCompose;
  /*  @Bind(R.id.ivAirplaneMode)
    ImageView ivAirplaneMode;*/

    private TwitterClient client;
    /*TweetAdapter mTweetAdapter;
    ArrayList<Tweet> mTweets;

    @Bind(R.id.rvTweet)
    RecyclerView mRecycler;*/



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_timeline);
        ButterKnife.bind(this);

        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setLogo(R.drawable.ic_logo);
        getSupportActionBar().setDisplayUseLogoEnabled(true);

        client = TwitterApp.getRestClient();

        pagerAdapter = new TweetPagerAdapter(getSupportFragmentManager());

        viewPager.setAdapter(pagerAdapter);

        tabStrip.setViewPager(viewPager);


        fabCompose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ComposeFragment composeFragment = new ComposeFragment();
                FragmentManager fm = getSupportFragmentManager();
                composeFragment.show(fm, "new tweet");
            }
        });



            /*@Override
            public boolean isViewFromObject(View view, Object object) {
                return false;
            }*/
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_timeline, menu);

        MenuItem searchItem = menu.findItem(R.id.action_search);

        SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        final SearchView searchView = (SearchView) menu.findItem(R.id.action_search).getActionView();
        searchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));
        searchView.setIconifiedByDefault(false); // Do not iconify the widget; expand it by default

        // Customize searchview text and hint colors
        int searchEditId = android.support.v7.appcompat.R.id.search_src_text;
        EditText et = (EditText) searchView.findViewById(searchEditId);
        et.setTextColor(Color.BLACK);
        et.setHintTextColor(Color.GRAY);
        et.setHint("Search tweets");

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                // perform query here
                searchView.clearFocus();
                Log.i("query", query);
                Intent intent = new Intent(getApplicationContext(), SearchActivity.class);
                intent.putExtra("q", query);
                startActivity(intent);
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });

        return super.onCreateOptionsMenu(menu);
    }

       // return true;


    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        int id = item.getItemId();

        switch (id){
            /*case R.id.action_compose:
                Intent intent = new Intent(this, ComposeActivity.class);
                startActivityForResult(intent, ComposeActivity.REQUEST_CODE);
                break;
            case R.id.action_profile:
                Intent intent1 = new Intent(this, ProfileActivity.class);
                startActivity(intent1);
                break;*/
            case R.id.logout:
                TwitterApp.getRestClient().clearAccessToken();
                Intent i = new Intent(this, LoginActivity.class);
                startActivity(i);

        }

        return super.onOptionsItemSelected(item);
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(requestCode == ComposeActivity.REQUEST_CODE && resultCode == ComposeActivity.REQUEST_CODE){
            Tweet tweet = data.getParcelableExtra("tweet");
            TimelineFragment timelineFragment = (TimelineFragment) pagerAdapter.getRegisteredFragment(0);
            timelineFragment.add(tweet);
        }
    }


    //Returns order of fragments
    public class TweetPagerAdapter extends SmartFragmentStatePagerAdapter {

        private String tabTitles[] = {"Home", "@ Mentions"};


        public TweetPagerAdapter(FragmentManager fragmentManager) {
            super(fragmentManager);

        }

        @Override
        public CharSequence getPageTitle(int position) {return tabTitles[position];}

        @Override
        public Fragment getItem(int position) {

            switch (position){
                case 0:

                    return new TimelineFragment();
                case 1:
                    return new MentionsFragment();
                default:
                    return new TimelineFragment();
            }

        }

        @Override
        public int getCount() {
            return tabTitles.length;
        }
    }

    public void onTweet(View view) {
        FragmentManager fm = getSupportFragmentManager();
        ComposeFragment composeFragment = ComposeFragment.newInstance();
        composeFragment.show(fm, "fragment_edit_name");
    }



    //@Override
    public void onTweet(Tweet tweet) {
        ((ComposeFragment.ComposeTweetFragmentListener) pagerAdapter.getRegisteredFragment(0))
                .onTweet(tweet);
    }

    public void composeNewTweet(View view){
        ComposeFragment composeFragment = new ComposeFragment();
        FragmentManager fm = getSupportFragmentManager();
        composeFragment.show(fm, "test");
    }


}



   /*client = TwitterApp.getRestClient();


        mTweets = new ArrayList<>();
        mTweetAdapter = new TweetAdapter(mTweets);
        mRecycler.setLayoutManager(new LinearLayoutManager(this));
        mRecycler.setAdapter(mTweetAdapter);
        populateTimeline();*/





    /*private void populateTimeline() {
        client.getHomeTimeline(new JsonHttpResponseHandler() {

            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                Log.d("TwitterClient", response.toString());
            }

            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONArray response) {
                Log.d("TwitterClient", response.toString());

                for (int i=0; i<response.length(); i++){
                    Tweet tweet = null;
                    try {
                        tweet = Tweet.fromJSON(response.getJSONObject(i));
                        mTweets.add(tweet);
                        mTweetAdapter.notifyItemInserted(mTweets.size()-1);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                }

            }


            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                Log.d("TwitterClient", responseString);
                throwable.printStackTrace();
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                Log.d("TwitterClient", errorResponse.toString());
                throwable.printStackTrace();
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONArray errorResponse) {
                Log.d("TwitterClient", errorResponse.toString());
                throwable.printStackTrace();
            }
        });
    }*/

