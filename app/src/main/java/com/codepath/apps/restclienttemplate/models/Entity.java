package com.codepath.apps.restclienttemplate.models;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by gretel on 9/28/17.
 */

public class Entity {
    public List<Media> media = new ArrayList<>();

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
}
