package com.codepath.apps.restclienttemplate.fragments;

import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomSheetDialogFragment;
import android.support.v4.app.DialogFragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.codepath.apps.restclienttemplate.R;
import com.codepath.apps.restclienttemplate.TwitterApp;
import com.codepath.apps.restclienttemplate.TwitterClient;
import com.codepath.apps.restclienttemplate.activities.TimelineActivity;
import com.codepath.apps.restclienttemplate.models.Tweet;
import com.codepath.apps.restclienttemplate.utils.Utils;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.json.JSONObject;

import butterknife.Bind;
import butterknife.ButterKnife;
import cz.msebera.android.httpclient.Header;

import static android.R.attr.editable;
import static com.codepath.apps.restclienttemplate.R.id.tvCharCount;

/**
 * Created by gretel on 9/28/17.
 */

public class ComposeFragment extends BottomSheetDialogFragment {

    private static final int TWEET_CHARS = 140;

    private TwitterClient client;

    @Bind(R.id.etTweet)
    EditText etTweet;
    @Bind(R.id.btnCancel)
    Button btnCancel;
    @Bind(R.id.btnTweet)
    Button btnTweet;
    @Bind(R.id.tvCharactersRemaining)
    TextView tvCharsRemaining;


    public ComposeFragment() {
        // Empty constructor is required for DialogFragment
    }

    public static ComposeFragment newInstance() {
        ComposeFragment frag = new ComposeFragment();
        return frag;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {


        return inflater.inflate(R.layout.fragment_compose, container);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        client = TwitterApp.getRestClient();

        ButterKnife.bind(this, view);

        getDialog().setTitle("Hello!");
        // Show soft keyboard automatically and request focus to field
        etTweet.requestFocus();
        getDialog().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);

        etTweet.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
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
            public void afterTextChanged(Editable editable) {



            }
        });

        btnTweet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

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
                }, etTweet.getText().toString(), false, 0);
            }

        });



                if (!Utils.checkForInternet()) {
                    Toast.makeText(getContext(), "Can't connect right now", Toast.LENGTH_LONG).show();
                    Log.i("TWEET", etTweet.getText().toString());
                    return;
                }

                client.tweet(etTweet.getText().toString(), new JsonHttpResponseHandler() {
                    @Override
                    public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                        Tweet tweet = Tweet.fromJson(response);

                        ComposeTweetFragmentListener listener = (ComposeTweetFragmentListener) getActivity();
                        listener.onTweet(tweet);

                        dismiss();
                    }

                    @Override
                    public void onFailure(int statusCode, Header[] headers, Throwable throwable,
                                          JSONObject errorResponse) {
                        super.onFailure(statusCode, headers, throwable, errorResponse);
                    }
                });


        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismiss();
            }
        });

        setCharsRemaining(TWEET_CHARS);
    }

    public interface ComposeTweetFragmentListener {
        void onTweet(Tweet tweet);
    }

    private void setCharsRemaining(int charsRemaining) {
        Resources res = getContext().getResources();
        tvCharsRemaining.setText(String.format(res.getString(R.string.number), charsRemaining));
    }

}
