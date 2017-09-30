package com.codepath.apps.restclienttemplate.fragments;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.codepath.apps.restclienttemplate.R;
import com.codepath.apps.restclienttemplate.TwitterClient;
import com.codepath.apps.restclienttemplate.adapters.TweetAdapter;
import com.codepath.apps.restclienttemplate.models.Tweet;
import com.codepath.apps.restclienttemplate.models.User;
import com.codepath.apps.restclienttemplate.utils.EndlessRecyclerViewScrollListener;
import com.codepath.apps.restclienttemplate.utils.Utils;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.raizlabs.android.dbflow.sql.language.Delete;
import com.raizlabs.android.dbflow.sql.language.Select;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import cz.msebera.android.httpclient.Header;

import static com.codepath.apps.restclienttemplate.R.id.fabCompose;
import static com.codepath.apps.restclienttemplate.R.id.toolbar;

/**
 * Created by gretel on 9/25/17.
 */

public abstract class TweetsFragment extends Fragment {

    @Bind(R.id.swipeContainer)
    SwipeRefreshLayout swipeContainer;

    @Bind(R.id.rvTweet)
    RecyclerView mRecycler;

    @Bind(R.id.toolbar)
    Toolbar toolbar;

    TweetAdapter mAdapter;
    TwitterClient client;
    private ArrayList<Tweet> tweetList;





    public TweetsFragment() {

    }

    /*public void populateMoreTimeline(){
        if (Tweet.lastTweetId != null){
            populateTimeLine("max_id", Tweet.lastTweetId);
        }
    }*/

    public abstract void populateTimeline(String maxId);

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState){
        super.onActivityCreated(savedInstanceState);
        if (isNetworkAvailable()){
            populateTimeline(null);

        }else {
            Toast.makeText(getActivity().getApplicationContext(), "No Internet connection", Toast.LENGTH_LONG).show();
        }

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){

        View view = inflater.inflate(R.layout.fragment_tweets, container, false);

        ButterKnife.bind(this, view);



        mAdapter = new TweetAdapter(getActivity(), new ArrayList<Tweet>());
        mRecycler.setAdapter(mAdapter);
     /*   mRecycler.ItemDecoration itemDecoration = new DividerItemDecoration(this, DividerItemDecoration.VERTICAL_LIST);
        mRecycler.addItemDecoration(itemDecoration);*/
        LinearLayoutManager layoutManager;
        layoutManager = new LinearLayoutManager(getContext());

        mRecycler.setLayoutManager(layoutManager);
       /*mRecycler.addOnScrollListener(new EndlessRecyclerViewScrollListener() {
            @Override
            public void onLoadMore(int page, int totalItemsCount, RecyclerView view) {
               // Log.i( + (mAdapter.getItemCount()));
                if(mAdapter.getItemCount() == 0){
                    populateTimeline(null);
                } else if (mAdapter.getItemCount() >= TwitterClient.T_X_PAGE){
                    Tweet oldest = mAdapter.getItem(mAdapter.getItemCount()-1);
                    populateTimeline(oldest.getId().toString());
                }
            }
        });*/

        swipeContainer.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                mAdapter.clear();
                mAdapter.notifyDataSetChanged();
                populateTimeline(null);
            }
        });

        return view;


    }


    /*private void populateTimeline(final String sinceOrMaxId, long count) {

        client.getHomeTimeline(new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONArray response) {
                Log.d("DEBUG", response.toString());
                Boolean clearOfflineTweets = Boolean.FALSE;
                if (sinceOrMaxId.equals("since_id")) {
                    tweetList.clear();
                    new Delete().from(Tweet.class).execute(); // all records
                    new Delete().from(User.class).execute(); // all records
                }
                tweetList.addAll(Tweet.fromJson(response));
                mAdapter.notifyDataSetChanged();
                swipeContainer.setRefreshing(false);

            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                Log.d("DEBUG", "onFailure" + errorResponse.toString());
                swipeContainer.setRefreshing(false);
            }
        }, sinceOrMaxId, count);
    }*/

    protected boolean isNetworkAvailable(){
        ConnectivityManager cm = (ConnectivityManager) getActivity().getApplicationContext().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = cm.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnectedOrConnecting();
    }

    public void add(Tweet tweet){
        mAdapter.add(0, tweet);
        mAdapter.notifyDataSetChanged();

    }

    public void addAll(ArrayList<Tweet> tweets){
        mAdapter.addAll(tweets);
        mAdapter.notifyDataSetChanged();
        swipeContainer.setRefreshing(false);
    }

    public void addAll(ArrayList<Tweet> tweets, Boolean clearTweetListBeforeAdd){
        if(clearTweetListBeforeAdd){
            tweetList.clear();
        }
        tweetList.addAll(tweets);
        mAdapter.notifyDataSetChanged();
    }

    protected void onFinishLoadMore(){
        mRecycler.setVisibility(View.VISIBLE);
        swipeContainer.setRefreshing(false);
    }

   /* @Override
    public void onResume() {
        super.onResume();
        if (!Utils.checkForInternet()) {
            swipeContainer.setEnabled(false);
            List<Tweet> queryResults = new Select().from(Tweet.class)
                    .orderBy("id DESC").execute();
            // Load the result into the adapter using `addAll`

            tweetList.clear();
            tweetList.addAll(queryResults);
            mAdapter.notifyDataSetChanged();

        } else {
            //fabCompose.setVisibility(View.VISIBLE);
            //get timeline here
            populateTimeline("since_id", (long) 1);
            //setup swipe to refresh

            wipeToRefreshView();
        }
    }*/



}
