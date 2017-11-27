package com.mad.taxius.model;

import com.google.gson.annotations.SerializedName;

/**
 * Location type class that indicates the type of location, departure or destination
 */

public class LocationType {

    @SerializedName("id")
    private String mId;
    @SerializedName("name")
    private String mName;

    public LocationType() {

    }

    public String getName() {
        return mName;
    }

    public void setName(String name) {
        this.mName = name;
    }

    public String getId() {
        return mId;
    }

    public void setId(String id) {
        this.mId = id;
    }
}
