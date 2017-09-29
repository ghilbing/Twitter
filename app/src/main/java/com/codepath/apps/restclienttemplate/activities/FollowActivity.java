package com.codepath.apps.restclienttemplate.activities;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.codepath.apps.restclienttemplate.R;
import com.codepath.apps.restclienttemplate.TwitterApp;
import com.codepath.apps.restclienttemplate.TwitterClient;
import com.codepath.apps.restclienttemplate.adapters.FollowAdapter;
import com.codepath.apps.restclienttemplate.models.User;
import com.codepath.apps.restclienttemplate.utils.DividerItemDecoration;
import com.codepath.apps.restclienttemplate.utils.ItemClickSupport;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.json.JSONException;
import org.json.JSONObject;
import org.parceler.Parcels;

import java.util.ArrayList;

import butterknife.Bind;
import butterknife.ButterKnife;
import cz.msebera.android.httpclient.Header;

public class FollowActivity extends AppCompatActivity {

    @Bind(R.id.toolbar)
    Toolbar toolbar;
    @Nullable
    @Bind(R.id.rvFollows)
    RecyclerView rvFollows;
    private FollowAdapter followAdapter;
    private ArrayList<User> followList;
    private User user;
    private TwitterClient client;
    @Bind(R.id.toolbarTitle)
    TextView toolbarTitle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_follow);
        ButterKnife.bind(this);

        client = TwitterApp.getRestClient();
        // Make sure the toolbar exists in the activity and is not null
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        user = Parcels.unwrap(getIntent().getParcelableExtra("user"));
        //non referencing view jobs go here
        followList = new ArrayList<>();
        followAdapter = new FollowAdapter(this, followList);
    }



}
