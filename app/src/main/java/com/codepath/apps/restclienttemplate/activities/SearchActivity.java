package com.codepath.apps.restclienttemplate.activities;

import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.codepath.apps.restclienttemplate.R;
import com.codepath.apps.restclienttemplate.TwitterApp;
import com.codepath.apps.restclienttemplate.TwitterClient;
import com.codepath.apps.restclienttemplate.fragments.SearchListFragment;
import com.codepath.apps.restclienttemplate.fragments.TweetsFragment;

import butterknife.Bind;
import butterknife.ButterKnife;

public class SearchActivity extends AppCompatActivity {


        @Bind(R.id.toolbar)
        Toolbar toolbar;
     /*   @Bind(R.id.toolbarTitle)
        TextView toolbarTitle;*/
        private TwitterClient client;
        String queryText;
        private TweetsFragment tweetsListFragment;


        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_search);
            ButterKnife.bind(this);
            client = TwitterApp.getRestClient();
            // Make sure the toolbar exists in the activity and is not null

            setSupportActionBar(toolbar);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setTitle(R.string.search);


            if (savedInstanceState == null) {
                if (Intent.ACTION_SEARCH.equals(getIntent().getAction())) {
                    String query = getIntent().getStringExtra(SearchManager.QUERY);
                    Toast.makeText(this, "Searching for " + query, Toast.LENGTH_LONG ).show();
                    loadFragment(query);
                }else {
                    loadFragment(getIntent().getStringExtra("q"));
                }
            }

        }


        private void loadFragment(String query){

            SearchListFragment fragmentUserTimeline = SearchListFragment.newInstance(query);
            //display user fragment dynamically within this activity
            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            ft.replace(R.id.flContainerSearch, fragmentUserTimeline);
            ft.commit();

        }



        @Override
        public boolean onCreateOptionsMenu(Menu menu) {
            // Inflate the menu; this adds items to the action bar if it is present.
            getMenuInflater().inflate(R.menu.menu_timeline, menu);
            MenuItem searchItem = menu.findItem(R.id.action_search);
            // Get the SearchView and set the searchable configuration
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
                    queryText = query;
                    // perform query here
                    searchView.clearFocus();
                    loadFragment(query);
                    return true;
                }

                @Override
                public boolean onQueryTextChange(String newText) {
                    return false;
                }
            });

            return super.onCreateOptionsMenu(menu);
        }

}


