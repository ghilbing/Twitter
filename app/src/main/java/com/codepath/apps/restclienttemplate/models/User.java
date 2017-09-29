package com.codepath.apps.restclienttemplate.models;

import android.os.Parcel;
import android.os.Parcelable;
import android.support.annotation.Nullable;
import android.util.Log;

import com.codepath.apps.restclienttemplate.MyDatabase;
import com.raizlabs.android.dbflow.annotation.Column;
import com.raizlabs.android.dbflow.annotation.PrimaryKey;
import com.raizlabs.android.dbflow.annotation.Table;
import com.raizlabs.android.dbflow.sql.language.Select;
import com.raizlabs.android.dbflow.structure.BaseModel;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

/**
 * Created by gretel on 9/25/17.
 */
@Table(database = MyDatabase.class)
public class User extends BaseModel implements Parcelable{

    //List the attributes
    @PrimaryKey
    @Column Long id_user;

    //private String uid;
    @Column
    private String name;

    @Column
    private String profileImage;

    @Column
    private String screenName;

    @Column
    private String profileBackgroundImageUrl;

    @Column
    @Nullable
    private String profileBannerUrl;

    @Column
    private String tagline;

    @Column
    private int followersCount;

    @Column
    private int followingCount;


    public User() {

    }

    public Long getId() {
        return id_user;
    }

    public void setId(Long uid) {
        this.id_user = uid;
    }

    public String getName() {
        return name;
    }

    public void setName(String userName) {
        this.name = userName;
    }

    public String getProfileImage() {
        return profileImage;
    }

    public void setProfileImage(String userProfileImage) {
        this.profileImage = userProfileImage;
    }

    public String getScreenName() {
        return screenName;
    }

    public void setScreenName(String userScreenName) {
        this.screenName = userScreenName;
    }

    public String getProfileBackgroundImageUrl() {
        return profileBackgroundImageUrl;
    }

    public void setProfileBackgroundImageUrl(String profileBackgroundImageUrl) {
        this.profileBackgroundImageUrl = profileBackgroundImageUrl;
    }

    @Nullable
    public String getProfileBannerUrl() {
        return profileBannerUrl;
    }

    public void setProfileBannerUrl(@Nullable String profileBannerUrl) {
        this.profileBannerUrl = profileBannerUrl;
    }

    public String getTagline() {
        return tagline;
    }

    public void setTagline(String tagline) {
        this.tagline = tagline;
    }

    public int getFollowersCount() {
        return followersCount;
    }

    public void setFollowersCount(int followersCount) {
        this.followersCount = followersCount;
    }

    public int getFollowingCount() {
        return followingCount;
    }

    public void setFollowingCount(int followingCount) {
        this.followingCount = followingCount;
    }

    public static User fromJson(JSONObject json) {
        User user = new User();
        try {
            user.id_user = json.getLong("id_str");
            user.name = json.getString("name");
            user.profileImage = json.getString("profile_image_url");
            user.screenName = json.getString("screen_name");
            user.profileBackgroundImageUrl = json.getString("profile_background_image_url");
            user.profileBannerUrl = json.optString("profile_banner_url");
            user.tagline = json.getString("description");
            user.followersCount = json.getInt("followers_count");
            user.followingCount = json.getInt("friends_count");

        } catch (JSONException e) {
            e.printStackTrace();
        }
        return user;
    }

    public static User byId(long id){
        return new Select().from(User.class).where(User_Table.id_user.eq(id)).querySingle();
    }

    public static List<User> recentItems(){
        return new Select().from(User.class).orderBy(User_Table.id_user, false).limit(300).queryList();
    }

    protected User(Parcel in) {
        id_user = in.readLong();
        name = in.readString();
        profileImage = in.readString();
        screenName = in.readString();
        profileBannerUrl = in.readString();
        profileBackgroundImageUrl = in.readString();
        tagline = in.readString();
        followersCount = in.readInt();
        followingCount = in.readInt();
    }

    public static ArrayList<User> fromJSONArray(JSONArray jsonArray){

        ArrayList<User> result = new ArrayList<>();
        for(int i=0; i< jsonArray.length(); i++){

            try {
                Log.i("FOLLOW", jsonArray.getJSONObject(i).toString());
                User user = fromJson(jsonArray.getJSONObject(i));
                result.add(user);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        return result;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeLong(id_user);
        dest.writeString(name);
        dest.writeString(profileImage);
        dest.writeString(screenName);
        dest.writeString(profileBannerUrl);
        dest.writeString(profileBackgroundImageUrl);
        dest.writeString(tagline);
        dest.writeInt(followersCount);
        dest.writeInt(followingCount);
    }

    @SuppressWarnings("unused")
    public static final Parcelable.Creator<User> CREATOR = new Parcelable.Creator<User>() {
        @Override
        public User createFromParcel(Parcel in) {
            return new User(in);
        }

        @Override
        public User[] newArray(int size) {
            return new User[size];
        }
    };
}
