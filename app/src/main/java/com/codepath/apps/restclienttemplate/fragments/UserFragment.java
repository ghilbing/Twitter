package com.codepath.apps.restclienttemplate.fragments;

import android.os.Bundle;
import android.widget.Toast;

import com.codepath.apps.restclienttemplate.TwitterApp;
import com.codepath.apps.restclienttemplate.TwitterClient;
import com.codepath.apps.restclienttemplate.models.Tweet;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

import cz.msebera.android.httpclient.Header;

import static com.codepath.apps.restclienttemplate.R.id.swipeContainer;
import static java.util.Collections.addAll;

/**
 * Created by gretel on 9/26/17.
 */

public class UserFragment extends TweetsFragment {
    TwitterClient client = TwitterApp.getRestClient();


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
       // username = getArguments().getString("user");
    }

    public static UserFragment newInstance(String screenName) {
        UserFragment f = new UserFragment();
        Bundle args = new Bundle();
        args.putString("screen_name", screenName);
        f.setArguments(args);
        return f;
    }

    public void populateTimeline(String maxId) {

        String username = getArguments().getString("screen_name");

        client.getUserTimeline(username, maxId, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONArray response) {
                System.out.println(response);
                ArrayList<Tweet> tweets = Tweet.fromJson(response);
                addAll(tweets);
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                System.out.println("user timeline failed");
                System.out.println(errorResponse);
                Toast.makeText(getActivity().getApplicationContext(),
                        "Couldn't get Tweets :(", Toast.LENGTH_SHORT).show();
                swipeContainer.setRefreshing(false);
            }
        });
    }
}
