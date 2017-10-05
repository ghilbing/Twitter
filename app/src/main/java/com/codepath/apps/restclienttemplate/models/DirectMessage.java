package com.codepath.apps.restclienttemplate.models;

import android.os.Parcel;
import android.os.Parcelable;
import android.util.Log;

import com.codepath.apps.restclienttemplate.MyDatabase;
import com.codepath.apps.restclienttemplate.TwitterClient;
import com.codepath.apps.restclienttemplate.utils.Utils;
import com.raizlabs.android.dbflow.annotation.Column;
import com.raizlabs.android.dbflow.annotation.ForeignKey;
import com.raizlabs.android.dbflow.annotation.PrimaryKey;
import com.raizlabs.android.dbflow.annotation.Table;
import com.raizlabs.android.dbflow.sql.language.Select;
import com.raizlabs.android.dbflow.structure.BaseModel;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import static com.codepath.apps.restclienttemplate.models.Tweet_Table.body;
import static com.codepath.apps.restclienttemplate.models.Tweet_Table.createdAt;
import static com.codepath.apps.restclienttemplate.models.Tweet_Table.favoriteCount;
import static com.codepath.apps.restclienttemplate.models.Tweet_Table.id_tweet;
import static com.codepath.apps.restclienttemplate.models.Tweet_Table.retweetCount;

/**
 * Created by gretel on 10/4/17.
 */



public class DirectMessage extends BaseModel implements Parcelable {

    //list out the attributes

    Long id_msg;


    User recipientUser;

    User senderUser;

    String text;

    String relativeDate;


    public Long getId_msg() {
        return id_msg;
    }

    public void setId_msg(Long id_msg) {
        this.id_msg = id_msg;
    }

    public User getRecipientUser() {
        return recipientUser;
    }

    public void setRecipientUser(User recipientUser) {
        this.recipientUser = recipientUser;
    }

    public User getSenderUser() {
        return senderUser;
    }

    public void setSenderUser(User senderUser) {
        this.senderUser = senderUser;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getRelativeDate() {
        return relativeDate;
    }

    public void setRelativeDate(String relativeDate) {
        this.relativeDate = relativeDate;
    }


    public DirectMessage() {}

    public static DirectMessage fromJson(JSONObject jsonObject){
        DirectMessage message = new DirectMessage();
        try{
            message.text = jsonObject.getString("text");
            message.relativeDate = TwitterClient.getTimeAgo(jsonObject.getString("created_at"));
            message.recipientUser = User.fromJson(jsonObject.getJSONObject("recipient"));
            message.senderUser = User.fromJson(jsonObject.getJSONObject("sender"));
        }catch (JSONException e){
            e.printStackTrace();
        }
        return message;
    }

    public static ArrayList<DirectMessage> fromJsonArray(JSONArray jsonArray){
        ArrayList<DirectMessage> result = new ArrayList<>();
        for(int i=0; i<jsonArray.length(); i++){
            try{
                DirectMessage message = fromJson(jsonArray.getJSONObject(i));
                result.add(message);
            }catch (JSONException e){
                e.printStackTrace();
            }

        }
        return result;
    }




    protected DirectMessage(Parcel in) {
        id_msg = in.readLong();
        recipientUser = (User) in.readValue(User.class.getClassLoader());
        senderUser = (User) in.readValue(User.class.getClassLoader());
        text = in.readString();
        relativeDate = in.readString();

    }




    public static final Creator<DirectMessage> CREATOR = new Creator<DirectMessage>() {
        @Override
        public DirectMessage createFromParcel(Parcel in) {
            return new DirectMessage(in);
        }

        @Override
        public DirectMessage[] newArray(int size) {
            return new DirectMessage[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeLong(id_msg);
        parcel.writeString(text);
        parcel.writeString(relativeDate);
        parcel.writeValue(recipientUser);
        parcel.writeValue(senderUser);

    }
}
