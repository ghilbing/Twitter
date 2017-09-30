package com.codepath.apps.restclienttemplate.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.codepath.apps.restclienttemplate.TwitterApp;
import com.codepath.apps.restclienttemplate.TwitterClient;
import com.codepath.apps.restclienttemplate.models.Tweet;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import cz.msebera.android.httpclient.Header;

/**
 * Created by gretel on 9/29/17.
 */

public class SearchListFragment extends TweetsFragment {

    private TwitterClient client;

    public static SearchListFragment newInstance(String query) {

        Bundle args = new Bundle();

        SearchListFragment fragment = new SearchListFragment();
        args.putString("q", query);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.i("HomeTimelineFragment", "oncreate");
        client  = TwitterApp.getRestClient();
    }



    @Override
    public void populateTimeline(String maxId) {
        Log.i("hometime", "populateTimeline");
        String query = getArguments().getString("q");


        client.searchPopularTweets(new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                Log.d("DEBUG getHomeTimeline", response.toString());

                try {
                    addAll(Tweet.fromJson(response.getJSONArray("statuses")));
                    Log.i("SEARCH", response.toString());
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                onFinishLoadMore();

            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                Log.d("DEBUG", "onFailure" + responseString);
                onFinishLoadMore();
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                Log.d("DEBUG", "onFailure" + errorResponse.toString());

            }
        }, maxId, query);
    }
}


