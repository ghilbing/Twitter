package com.codepath.apps.restclienttemplate.models;

import android.os.Parcel;
import android.os.Parcelable;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by gretel on 9/28/17.
 */

public class Media implements Parcelable {

    private long id;
    private String mediaUrl;
    private String url;

    protected Media(Parcel in) {
        id = in.readLong();
        mediaUrl = in.readString();
        url = in.readString();
    }

    public static final Creator<Media> CREATOR = new Creator<Media>() {
        @Override
        public Media createFromParcel(Parcel in) {
            return new Media(in);
        }

        @Override
        public Media[] newArray(int size) {
            return new Media[size];
        }
    };

    public Media() {

    }

    public long getId() {
        return id;
    }

    public String getMediaUrl() {
        return mediaUrl;
    }

    public String getUrl() {
        return url;
    }

    public static Media fromJSON(JSONObject object) {
        Media media = new Media();
        try {
            media = new Media();
            media.id = object.getLong("id");
            media.mediaUrl = object.getString("media_url");
            media.url = object.getString("url");
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return media;
    }


    public static List<Media> fromJSONArray(JSONArray array) {
        List<Media> medias = new ArrayList<>();
        for (int index = 0; index < array.length(); ++index) {
            try {
                Media media = fromJSON(array.getJSONObject(index));
                if (media != null) {
                    medias.add(media);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return medias;
    }




    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeLong(id);
        parcel.writeString(mediaUrl);
        parcel.writeString(url);
    }
}
