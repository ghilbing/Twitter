package com.codepath.apps.restclienttemplate.adapters;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.codepath.apps.restclienttemplate.R;
import com.codepath.apps.restclienttemplate.TwitterApp;
import com.codepath.apps.restclienttemplate.TwitterClient;
import com.codepath.apps.restclienttemplate.activities.DetailActivity;
import com.codepath.apps.restclienttemplate.activities.ProfileActivity;
import com.codepath.apps.restclienttemplate.activities.SearchActivity;
import com.codepath.apps.restclienttemplate.fragments.ReplyTweetFragment;
import com.codepath.apps.restclienttemplate.models.Entity;
import com.codepath.apps.restclienttemplate.models.Media;
import com.codepath.apps.restclienttemplate.models.Tweet;
import com.codepath.apps.restclienttemplate.models.User;
import com.codepath.apps.restclienttemplate.utils.LinkifieldTextView;
import com.codepath.apps.restclienttemplate.utils.PatternEditableBuilder;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.json.JSONException;
import org.json.JSONObject;
import org.parceler.Parcels;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

import butterknife.ButterKnife;
import butterknife.Bind;
import cz.msebera.android.httpclient.Header;

import static com.codepath.apps.restclienttemplate.R.id.btnFavorite;
import static com.codepath.apps.restclienttemplate.R.id.btnRetweet;
import static com.codepath.apps.restclienttemplate.R.id.fill;
import static com.codepath.apps.restclienttemplate.R.id.tvFavoriteCount;
import static com.codepath.apps.restclienttemplate.R.string.tweet;

/**
 * Created by gretel on 9/25/17.
 */

public class TweetAdapter extends RecyclerView.Adapter<TweetAdapter.ViewHolder> {


    private List<Tweet> mTweets;
    Context mContext;

    TwitterClient client;

    FragmentManager mFragmentManager;


    //pass in the Tweets array in the constructor
    public TweetAdapter(Context context, List<Tweet> tweets, FragmentManager fragmentManager) {
        mContext = context;
        mTweets = tweets;
        mFragmentManager = fragmentManager;
        client = TwitterApp.getRestClient();
    }

    private Context getmContext() {
        return mContext;
    }

    private OnUserClickListener mListener;

    public interface OnUserClickListener {
        void onUserClick(User user);
    }


    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        mContext = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(mContext);

        View tweetView = inflater.inflate(R.layout.item_tweet, parent, false);
        ViewHolder viewHolder = new ViewHolder(tweetView);
        return viewHolder;
    }


    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        //Get the data according to position
        final Tweet tweet = mTweets.get(position);

        //populate the views according to this data
        holder.tvUserName.setText("@" + tweet.getUser().getScreenName().toString());
       // holder.tvBody.setText(tweet.getBody());
        holder.tvFullName.setText(tweet.getUser().getName());
        holder.tvRetweetCount.setText(String.valueOf(tweet.getRetweetCount()));
        holder.tvTweetAge.setText(tweet.getCreatedAt());


        holder.ivProfileImage.setImageResource(android.R.color.transparent);
        Glide.with(mContext).load(tweet.getUser().getProfileImage()).into(holder.ivProfileImage);
        holder.ivProfileImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getmContext(), ProfileActivity.class);
                intent.putExtra("user", tweet.getUser());
                getmContext().startActivity(intent);

            }
        });

        ImageView ivProfileImg = holder.ivProfileImage;

        ivProfileImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getmContext(), ProfileActivity.class);
                intent.putExtra("user", Parcels.wrap(tweet.getUser()));
                getmContext().startActivity(intent);
            }
        });


        holder.ivMedia.setImageResource(android.R.color.transparent);
        String mediaUrl = mediaUrl(tweet);

        if (mediaUrl != null) {
            Glide.with(getmContext()).load(mediaUrl).into(holder.ivMedia);
            holder.ivMedia.setVisibility(View.VISIBLE);
        } else {
            holder.ivMedia.setVisibility(View.GONE);
        }

        holder.tvFavoriteCount.setText(String.valueOf(tweet.getFavoriteCount()));

        final TextView tvFavoriteCount = holder.tvFavoriteCount;
        final Button btnFavorite = holder.btnFavorite;
        final TextView tvRetweetCount = holder.tvRetweetCount;
        final Button btnRetweet = holder.btnRetweet;

        LinkifieldTextView tvBody = holder.tvBody;

        tvBody.setText(tweet.getBody());

        new PatternEditableBuilder().
                addPattern(Pattern.compile("\\@(\\w+)"), getmContext().getResources().getColor(R.color.TwitterBlue),
                        new PatternEditableBuilder.SpannableClickedListener() {
                            @Override
                            public void onSpanClicked(String screenName) {

                                Intent intent = new Intent(mContext, ProfileActivity.class);
                                intent.putExtra("from_user_span", true);
                                intent.putExtra("screen_name", screenName.substring(1));
                                getmContext().startActivity(intent);
                                Toast.makeText(getmContext(), "Clicked username: " + screenName,
                                        Toast.LENGTH_SHORT).show();
                            }
                        }).into(tvBody);

        new PatternEditableBuilder().
                addPattern(Pattern.compile("\\#(\\w+)"), getmContext().getResources().getColor(R.color.TwitterBlue),
                        new PatternEditableBuilder.SpannableClickedListener() {
                            @Override
                            public void onSpanClicked(String hashtag) {

                                Intent intent = new Intent(getmContext(), SearchActivity.class);
                                intent.putExtra("q", hashtag);
                                getmContext().startActivity(intent);
                                Toast.makeText(getmContext(), "Clicked hashtag: " + hashtag,
                                        Toast.LENGTH_SHORT).show();
                            }
                        }).into(tvBody);

        tvRetweetCount.setText("");
        tvFavoriteCount.setText("");












        if (tweet.getFavorited()) {
            holder.btnFavorite.setBackgroundResource(R.drawable.ic_favorite_on);
        }

        if (tweet.getRetweeted())

        {
            holder.btnRetweet.setBackgroundResource(R.drawable.ic_retweet_on);
        }

        btnFavorite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                favorTweet(tweet, tvFavoriteCount, btnFavorite);
            }

        });

        btnRetweet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                reTweet(tweet, tvRetweetCount, btnRetweet);
            }
        });

    }




    private void favorTweet(final Tweet tweet, final TextView tvFavoriteCount, final Button btnFavorite) {

        client.favoriteTweet(new JsonHttpResponseHandler(){
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                Log.d("DEBUG", "favorited" + response.toString());
                try {
                    if(response.getBoolean("favorited")){
                        tweet.setFavoriteCount(Integer.parseInt(response.getString("favorite_count")));
                        btnFavorite.setBackground(getmContext().getResources().getDrawable(R.drawable.ic_favorite_on));
                    }else{
                        btnFavorite.setBackground(getmContext().getResources().getDrawable(R.drawable.ic_favorite));
                    }
                    tweet.setFavorited(response.getBoolean("favorited"));
                    if(response.getLong("favorite_count") > 0) {
                        tvFavoriteCount.setText(String.valueOf(response.getLong("favorite_count")));
                    }else{
                        tvFavoriteCount.setText("");

                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                Log.d("DEBUG", responseString);

            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                Log.d("DEBUG", errorResponse.toString());

            }
        }, tweet.getFavorited(), tweet.getId());

    }


    private void reTweet(final Tweet tweet, final TextView tvRetweetCount, final Button btnRetweet) {

        client.reTweet(new JsonHttpResponseHandler(){
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                Log.d("DEBUG", "retweeted" + response.toString());
                try {
                    if(response.getBoolean("retweeted")){
                        tweet.setRetweetCount(Integer.parseInt(response.getString("retweet_count")));
                        btnRetweet.setBackground(getmContext().getResources().getDrawable(R.drawable.ic_retweet_on));
                    }else{
                        btnRetweet.setBackground(getmContext().getResources().getDrawable(R.drawable.ic_retweet));
                    }
                    tweet.setRetweeted(response.getBoolean("retweeted"));
                    if(response.getLong("retweet_count") > 0) {
                        tvRetweetCount.setText(String.valueOf(response.getLong("retweet_count")));
                    }else{
                        tvRetweetCount.setText("");

                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                Log.d("DEBUG", responseString);

            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                Log.d("DEBUG", errorResponse.toString());

            }
        }, tweet.getRetweeted(), tweet.getId());

    }



    @Nullable
    private static String mediaUrl(Tweet tweet) {
        Entity entity = tweet.getEntity();
        if (entity != null) {
            List<Media> media = entity.getMedia();
            if (!media.isEmpty()) {
                return media.get(0).getMediaUrl();
            }
        }

        return null;
    }

    public void clear() {
        int size = this.mTweets.size();
        this.mTweets.clear();
        notifyItemRangeRemoved(0, size);
    }

    public void add(int position, Tweet tweet){
        mTweets.add(position, tweet);
        notifyItemInserted(position);
    }

    public void addAll(ArrayList<Tweet> tweets){
        mTweets.addAll(tweets);
        notifyDataSetChanged();
    }

    public Tweet getItem(int position){
        return mTweets.get(position);
    }






    @Override
    public int getItemCount() {
        return mTweets.size();
    }

    //for each row, inflate the layout and cache the references into ViewHolder



    public class ViewHolder extends RecyclerView.ViewHolder{

        @Bind(R.id.ivProfileImage)
        ImageView ivProfileImage;
        @Bind(R.id.tvUserName)
        TextView tvUserName;
        @Bind(R.id.tvBody)
        LinkifieldTextView tvBody;
        @Bind(R.id.tvTweetFullName)
        TextView tvFullName;
        @Bind(R.id.tvTweetAge)
        TextView tvTweetAge;
        @Bind(R.id.tvRetweetCount)
        TextView tvRetweetCount;
        @Bind(R.id.ivMedia)
        ImageView ivMedia;
        @Bind(R.id.tvFavoriteCount)
        TextView tvFavoriteCount;
        @Bind(R.id.btnRetweet)
        Button btnRetweet;
        @Bind(R.id.btnReply)
        Button btnReply;
        @Bind(R.id.btnFavorite)
        Button btnFavorite;

        public ViewHolder (View itemView){
            super(itemView);

            ButterKnife.bind(this, itemView);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int position = getLayoutPosition();

                    Tweet tweet = mTweets.get(position);
                    Intent intent = new Intent(mContext, DetailActivity.class);
                    intent.putExtra("tweet", Parcels.wrap(tweet));
                    intent.putExtra("user", Parcels.wrap(tweet.getUser()));
                    intent.putExtra("entity", Parcels.wrap(tweet.getEntity()));
                    mContext.startActivity(intent);
                }
            });

        }


    }


}
