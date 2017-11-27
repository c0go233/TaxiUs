package com.mad.taxius.model;

import com.google.android.gms.maps.model.LatLng;
import com.google.gson.annotations.SerializedName;

/**
 * Location class that contains information about selected places of journey
 */

public class Location {

    @SerializedName("id")
    private String mId;
    @SerializedName("name")
    private String mName;
    @SerializedName("latitude")
    private double mLatitude;
    @SerializedName("longitude")
    private double mLongitude;
    @SerializedName("journeyId")
    private String mJourneyId;
    @SerializedName("locationType")
    private String mLocationType;
    @SerializedName("distanceDifference")
    private double mDistanceDifference;

    public Location() {
    }

    public Location(String name, double latitude, double longitude, String locationType) {
        this.mName = name;
        this.mLatitude = latitude;
        this.mLongitude = longitude;
        this.mLocationType = locationType;
    }

    public String getName() {
        return mName;
    }

    public void setName(String name) {
        this.mName = name;
    }

    public double getLatitude() {
        return mLatitude;
    }

    public void setLatitude(double latitude) {
        this.mLatitude = latitude;
    }

    public double getLongitude() {
        return mLongitude;
    }

    public void setLongitude(double longitude) {
        this.mLongitude = longitude;
    }

    public String getLocationType() {
        return mLocationType;
    }

    public void setLocationType(String locationType) {
        this.mLocationType = locationType;
    }

    public String getId() {
        return mId;
    }

    public void setId(String id) {
        this.mId = id;
    }

    public String getJourneyId() {
        return mJourneyId;
    }

    public void setJourneyId(String journeyId) {
        this.mJourneyId = journeyId;
    }

    public double getDistanceDifference() {
        return mDistanceDifference;
    }

    public void setDistanceDifference(double distanceDifference) {
        this.mDistanceDifference = distanceDifference;
    }

    public LatLng getLatLng() {
        return new LatLng(this.mLatitude, this.mLongitude);
    }
}
