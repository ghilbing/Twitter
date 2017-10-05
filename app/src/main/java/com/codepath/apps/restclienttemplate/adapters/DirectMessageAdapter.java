package com.codepath.apps.restclienttemplate.adapters;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.codepath.apps.restclienttemplate.R;
import com.codepath.apps.restclienttemplate.TwitterApp;
import com.codepath.apps.restclienttemplate.TwitterClient;
import com.codepath.apps.restclienttemplate.activities.TimelineActivity;
import com.codepath.apps.restclienttemplate.models.DirectMessage;
import com.codepath.apps.restclienttemplate.models.User;
import com.codepath.apps.restclienttemplate.utils.DividerItemDecoration;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import cz.msebera.android.httpclient.Header;

import static com.bumptech.glide.Glide.with;

/**
 * Created by gretel on 10/4/17.
 */

public class DirectMessageAdapter extends RecyclerView.Adapter<DirectMessageAdapter.ViewHolder> {


    private List<DirectMessage> mMessages;
    private Context mContext;


    public DirectMessageAdapter(Context context, List<DirectMessage> messages) {
        mMessages = messages;
        mContext = context;
    }


    private Context getContext() {
        return mContext;
    }

    @Override
    public DirectMessageAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();


        LayoutInflater inflater = LayoutInflater.from(context);
        View tweetView = inflater.inflate(R.layout.item_message, parent, false);


        ViewHolder viewHolder = new ViewHolder(tweetView);


        return viewHolder;

    }

    //bind data to the viewholder
    @Override
    public void onBindViewHolder(DirectMessageAdapter.ViewHolder viewHolder, int position) {
        final DirectMessage message = mMessages.get(position);
        User otherUser;
        String text;
        if(!message.getRecipientUser().getScreenName().equals(TimelineActivity.loggedUserScreenName)){
            text = new String("You: ");
            otherUser = message.getRecipientUser();
        }else{
            text = new String();
            otherUser = message.getSenderUser();
        }

        TextView tvUserName = viewHolder.tvvUserName;
        TextView tvScreenName = viewHolder.tvScreenName;
        TextView tvRelativeTime = viewHolder.tvRelativeTime;

        TextView tvText = viewHolder.tvText;
        ImageView ivProfileImg = viewHolder.ivProfileImg;

        tvUserName.setText(otherUser.getName());
        tvScreenName.setText(otherUser.getScreenName());
        tvRelativeTime.setText(message.getRelativeDate());
        tvText.setText(text + message.getText());
        ivProfileImg.setImageResource(android.R.color.transparent);
//
//        ivProfileImg.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent intent = new Intent(getContext(), ProfileActivity.class);
//                intent.putExtra("user", Parcels.wrap(user));
//                getContext().startActivity(intent);
//            }
//        });
        Glide.with(getContext()).load(otherUser.getProfileImage()).into(ivProfileImg);
    }

    @Override
    public int getItemCount() {
        return mMessages.size();
    }


    public static class ViewHolder extends RecyclerView.ViewHolder {

        @Bind(R.id.tvUserName)
        TextView tvvUserName;
        @Bind(R.id.tvText)
        TextView tvText;
        @Bind(R.id.tvScreenName)
        TextView tvScreenName;
        @Bind(R.id.tvRelativeTime)
        TextView tvRelativeTime;
        @Bind(R.id.ivProfileImg)
        ImageView ivProfileImg;



        public ViewHolder(View itemView) {

            super(itemView);

           ButterKnife.bind(this, itemView);

        }
    }


}
