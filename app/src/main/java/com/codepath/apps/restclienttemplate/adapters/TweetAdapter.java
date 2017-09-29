package com.codepath.apps.restclienttemplate.adapters;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.codepath.apps.restclienttemplate.R;
import com.codepath.apps.restclienttemplate.activities.ProfileActivity;
import com.codepath.apps.restclienttemplate.models.Entity;
import com.codepath.apps.restclienttemplate.models.Media;
import com.codepath.apps.restclienttemplate.models.Tweet;
import com.codepath.apps.restclienttemplate.models.User;

import org.parceler.Parcels;

import java.util.ArrayList;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.Bind;

import static com.codepath.apps.restclienttemplate.R.string.tweet;

/**
 * Created by gretel on 9/25/17.
 */

public class TweetAdapter extends RecyclerView.Adapter<TweetAdapter.ViewHolder>{



    private List<Tweet> mTweets;
    Context mContext;

    //pass in the Tweets array in the constructor
    public TweetAdapter(Context context, List<Tweet> tweets){
        mContext = context;
        mTweets = tweets;
    }

    private Context getmContext() { return mContext;}

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
        holder.tvBody.setText(tweet.getBody());
        holder.tvFullName.setText(tweet.getUser().getName());
        holder.tvRetweetCount.setText(String.valueOf(tweet.getRetweetCount()));
        holder.tvTweetAge.setText(tweet.getCreatedAt());

        Boolean fav = tweet.getFavorited();
        Boolean tweeted = tweet.getRetweeted();

        holder.ivProfileImage.setImageResource(android.R.color.transparent);
        Glide.with(mContext).load(tweet.getUser().getProfileImage()).into(holder.ivProfileImage);
        holder.ivProfileImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getmContext(), ProfileActivity.class);
                intent.putExtra("user", tweet.getUser());
                Log.i("USER PASED",tweet.getUser().toString());
                getmContext().startActivity(intent);
               /* User user = (User) view.getTag();
                mListener.onUserClick(user);*/
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

        if(mediaUrl != null) {
            Glide.with(getmContext()).load(mediaUrl).into(holder.ivMedia);
            holder.ivMedia.setVisibility(View.VISIBLE);
        } else {
            holder.ivMedia.setVisibility(View.GONE);
        }

        holder.tvFavoriteCount.setText(String.valueOf(tweet.getFavoriteCount()));


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
        TextView tvBody;
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
        @Bind(R.id.btnFavorite)
        Button btnFavorite;

        public ViewHolder (View itemView){
            super(itemView);

            ButterKnife.bind(this, itemView);
        }



    }


}
