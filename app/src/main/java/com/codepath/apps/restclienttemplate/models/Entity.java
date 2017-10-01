package com.codepath.apps.restclienttemplate.models;

import android.os.Parcel;
import android.os.Parcelable;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by gretel on 9/28/17.
 */

public class Entity implements Parcelable {


    public List<Media> media = new ArrayList<>();

    protected Entity(Parcel in) {
    }

    public static final Creator<Entity> CREATOR = new Creator<Entity>() {
        @Override
        public Entity createFromParcel(Parcel in) {
            return new Entity(in);
        }

        @Override
        public Entity[] newArray(int size) {
            return new Entity[size];
        }
    };

    public Entity(){}

    public List<Media> getMedia() {
        return media;
    }

    public static Entity fromJSON(JSONObject object) {
        Entity entity = new Entity();
        JSONArray media = object.optJSONArray("media");
        if (media != null) {
            entity.getMedia().addAll(Media.fromJSONArray(media));
        }
        return entity;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
    }
}
