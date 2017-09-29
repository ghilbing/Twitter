package com.codepath.apps.restclienttemplate.fragments;

import android.widget.Toast;

import com.codepath.apps.restclienttemplate.TwitterApp;
import com.codepath.apps.restclienttemplate.TwitterClient;
import com.codepath.apps.restclienttemplate.models.Tweet;
import com.loopj.android.http.JsonHttpResponseHandler;
import cz.msebera.android.httpclient.Header;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;


/**
 * Created by gretel on 9/25/17.
 */

public class MentionsFragment extends TweetsFragment {

    private TwitterClient client = TwitterApp.getRestClient();

    @Override
    public void populateTimeline(String maxId) {
        client.getMentions(maxId, new JsonHttpResponseHandler() {

            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONArray response) {
                System.out.println("mentions response: " + response);
                ArrayList<Tweet> tweets = Tweet.fromJson(response);
                addAll(tweets);
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                System.out.println("mentions timeline failed");
                System.out.println(errorResponse);
                Toast.makeText(getActivity().getApplicationContext(),
                        "Couldn't get Tweets :(", Toast.LENGTH_SHORT).show();
                swipeContainer.setRefreshing(false);

            }

        });
    }
}
