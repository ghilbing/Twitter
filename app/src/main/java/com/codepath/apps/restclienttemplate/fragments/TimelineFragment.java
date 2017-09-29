package com.codepath.apps.restclienttemplate.fragments;

import android.widget.Toast;

import com.codepath.apps.restclienttemplate.TwitterApp;
import com.codepath.apps.restclienttemplate.TwitterClient;
import com.codepath.apps.restclienttemplate.models.Tweet;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

import cz.msebera.android.httpclient.Header;
import cz.msebera.android.httpclient.client.ResponseHandler;

/**
 * Created by gretel on 9/25/17.
 */

public class TimelineFragment extends TweetsFragment {

    private TwitterClient client = TwitterApp.getRestClient();

    @Override
    public void populateTimeline(String maxId) {
        client.getHomeTimeline(maxId, new JsonHttpResponseHandler() {

            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONArray response){
                ArrayList<Tweet> tweets = Tweet.fromJson(response);
                addAll(tweets);

            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse){
                System.out.println("Timeline failed");
                System.out.println(errorResponse);
                Toast.makeText(getActivity().getApplicationContext(), "Couldn't get Tweets :(", Toast.LENGTH_LONG).show();
                swipeContainer.setRefreshing(false);
            }

        });



    }
}
