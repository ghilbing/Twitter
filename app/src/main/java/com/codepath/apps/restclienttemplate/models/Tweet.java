package com.codepath.apps.restclienttemplate.models;

import android.os.Parcel;
import android.os.Parcelable;
import android.support.annotation.Nullable;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import com.codepath.apps.restclienttemplate.MyDatabase;
import com.codepath.apps.restclienttemplate.R;
import com.codepath.apps.restclienttemplate.TwitterClient;
import com.raizlabs.android.dbflow.annotation.Column;
import com.raizlabs.android.dbflow.annotation.ForeignKey;
import com.raizlabs.android.dbflow.annotation.PrimaryKey;
import com.raizlabs.android.dbflow.annotation.Table;
import com.raizlabs.android.dbflow.sql.language.Select;
import com.raizlabs.android.dbflow.structure.BaseModel;

import butterknife.Bind;

import static android.R.attr.id;

/**
 * Created by gretel on 9/25/17.
 */
@Table(database = MyDatabase.class)

public class Tweet extends BaseModel implements Parcelable{

    //list out the attributes
    @PrimaryKey
    @Column
    Long id_tweet;

    @Column
    private String body;

    @Column
    private String createdAt;

    @Column
    @ForeignKey(saveForeignKeyModel = true)
    private User user;

    @Nullable
    private Entity entity;

    @Column
    private int retweetCount;

    @Column
    private int favoriteCount;

    @Column
    private boolean isRetweeted;

    @Column
    private boolean isFavorited;



    public Tweet() {

    }

    public static Long lastTweetId;


    public Long getId() {
        return id_tweet;
    }

    public void setId(Long id) {
        this.id_tweet = id;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public String getCreatedAt() {
        return TwitterClient.getTimeAgo(createdAt);
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    public int getRetweetCount() {
        return retweetCount;
    }

    public void setRetweetCount(int retweetCount) {
        this.retweetCount = retweetCount;
    }

    public Boolean getRetweeted() {
        return isRetweeted;
    }

    public void setRetweeted(Boolean retweeted) {
        isRetweeted = retweeted;
    }

    public Boolean getFavorited() {
        return isFavorited;
    }

    public void setFavorited(Boolean favorited) {
        isFavorited = favorited;
    }

    public boolean isRetweeted() {
        return isRetweeted;
    }

    public Boolean isFavorited() {

        return isFavorited;

    }

    @Nullable
    public Entity getEntity() {
        return entity;
    }

    public void setEntity(@Nullable Entity entity) {
        this.entity = entity;
    }

    public int getFavoriteCount() {
        return favoriteCount;
    }

    public void setFavoriteCount(int favoriteCount) {
        this.favoriteCount = favoriteCount;
    }

    public static Tweet fromJson(JSONObject json) {
        Tweet tweet = new Tweet();
        try {
            tweet.id_tweet = json.getLong("id_str");
            tweet.body = json.getString("text");
            tweet.createdAt = json.getString("created_at");
            tweet.retweetCount = json.getInt("retweet_count");
            tweet.user = User.fromJson(json.getJSONObject("user"));

            JSONObject entities = json.optJSONObject("entities");
            tweet.entity = entities == null ? null : Entity.fromJSON(entities);

            tweet.retweetCount = json.getInt("retweet_count");
            tweet.favoriteCount = json.getInt("favorite_count");

            tweet.isRetweeted = json.getBoolean("retweeted");
            tweet.isFavorited = json.getBoolean("favorited");
            if(tweet.getBody().startsWith("RT")){
                tweet.isFavorited = false;
                tweet.favoriteCount = 0;


            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
        return tweet;
    }

    public static ArrayList<Tweet> fromJson(JSONArray json) {
        ArrayList<Tweet> tweets = new ArrayList<Tweet>(json.length());
        for (int i = 0; i < json.length(); i++) {
            try {
                Tweet tweet = fromJson(json.getJSONObject(i));
                if(tweet != null) {
                    tweets.add(tweet);
                }
            } catch (JSONException e) {
                e.printStackTrace();
                continue;
            }
        }
        return tweets;
    }

    protected Tweet(Parcel in) {
        id_tweet = in.readLong();
        body = in.readString();
        createdAt = in.readString();
        retweetCount = in.readInt();
        user = (User) in.readValue(User.class.getClassLoader());
        entity = (Entity) in.readValue(Entity.class.getClassLoader());
        retweetCount = in.readInt();
        favoriteCount = in.readInt();

    }

    public static Tweet byId(long id){
        return new Select().from(Tweet.class).where(Tweet_Table.id_tweet.eq(id)).querySingle();
    }

    public static List<Tweet> recentItems(){
        return new Select().from(Tweet.class).orderBy(Tweet_Table.id_tweet, false).limit(300).queryList();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeLong(id);
        dest.writeString(body);
        dest.writeString(createdAt);
        dest.writeInt(retweetCount);
        dest.writeValue(user);
        dest.writeValue(entity);
        dest.writeInt(retweetCount);
        dest.writeInt(favoriteCount);
    }

    @SuppressWarnings("unused")
    public static final Parcelable.Creator<Tweet> CREATOR = new Parcelable.Creator<Tweet>() {
        @Override
        public Tweet createFromParcel(Parcel in) {
            return new Tweet(in);
        }

        @Override
        public Tweet[] newArray(int size) {
            return new Tweet[size];
        }
    };



}
