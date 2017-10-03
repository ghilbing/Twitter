package com.codepath.apps.restclienttemplate.fragments;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomSheetDialogFragment;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.codepath.apps.restclienttemplate.R;
import com.codepath.apps.restclienttemplate.TwitterApp;
import com.codepath.apps.restclienttemplate.TwitterClient;
import com.codepath.apps.restclienttemplate.activities.TimelineActivity;
import com.codepath.apps.restclienttemplate.models.Tweet;
import com.codepath.apps.restclienttemplate.models.User;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.json.JSONObject;
import org.parceler.Parcels;

import butterknife.Bind;
import butterknife.ButterKnife;
import cz.msebera.android.httpclient.Header;


public class ReplyTweetFragment extends BottomSheetDialogFragment {

    private static ReplyTweetFragment instance = null;

    @Bind(R.id.etNewTweet)
    EditText etNewTweet;
    @Bind(R.id.btnCancel)
    Button btnCancel;
    @Bind(R.id.btnTweet)
    Button btnTweet;
    @Bind(R.id.tvCharactersRemaining)
    TextView tvCharsRemaining;
    @Bind(R.id.tvReplyTo)
    TextView tvReplyTo;

    Boolean isReply;
    Tweet tweet;
    User user;


    TwitterClient client;
    //    TextView tvCharCount;
    private static final int TWEET_CHARS = 140;


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_reply_tweet, container);
        ButterKnife.bind(this, view);
        tweet = Parcels.unwrap(getArguments().getParcelable("tweet"));
        user = Parcels.unwrap(getArguments().getParcelable("user"));
        isReply = (Boolean) getArguments().get("is_reply");
        return view;
    }

    public static ReplyTweetFragment newInstance(Boolean isReply, Tweet tweet, User user) {
        ReplyTweetFragment f = new ReplyTweetFragment();

        // Supply num input as an argument.
        Bundle args = new Bundle();
        args.putBoolean("is_reply", isReply);
        args.putParcelable("user", Parcels.wrap(user));
        args.putParcelable("tweet", Parcels.wrap(tweet));
        f.setArguments(args);

        return f;
    }


    @Override
    public void onViewCreated(final View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        //get singleton rest client
        client = TwitterApp.getRestClient();
        btnTweet.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {

                client.composeTweet(new JsonHttpResponseHandler(){
                    @Override
                    public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                        Log.d("DEBUG", "onsuccess" + response.toString());
                        Intent intent = new Intent(getActivity(), TimelineActivity.class);
                        startActivity(intent);
                    }

                    @Override
                    public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                        dismiss();
                    }
                }, etNewTweet.getText().toString(), isReply, tweet.getId());
            }


        });

        etNewTweet.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                tvCharsRemaining.setText(String.valueOf(TWEET_CHARS - charSequence.length()));
                if(charSequence.length() > 0 && charSequence.length() <= TWEET_CHARS){
                    btnTweet.setEnabled(true);
                }else{
                    btnTweet.setEnabled(false);

                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        if(isReply){
            tvReplyTo.setVisibility(View.VISIBLE);
            tvReplyTo.setText("In reply to " + user.getScreenName());
            etNewTweet.setText("@" + user.getScreenName() + " ");
            Log.i("REPLY", etNewTweet.toString());
            etNewTweet.requestFocus();
        }

        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismiss();
            }
        });

    }


    @Override public void onDestroyView() {
        super.onDestroyView();
      
    }


}
