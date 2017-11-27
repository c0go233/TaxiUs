package com.mad.taxius.model;

import com.google.gson.annotations.SerializedName;

import java.util.Date;

/**
 * Journey class that contains information about journey
 */

public class Journey {

    @SerializedName("id")
    private String mId;
    @SerializedName("deleted")
    private boolean mDeleted;
    @SerializedName("departureTime")
    private Date mDepartureTime;
    @SerializedName("isActive")
    private boolean mIsActive;
    @SerializedName("departureLocation")
    private Location mDepartureLocation;
    @SerializedName("destinationLocation")
    private Location mDestinationLocation;

    public Journey() {
    }

    public Journey(Date departureTime, boolean isActive, Location departureLocation, Location destinationLocation) {
        this.mDepartureTime = departureTime;
        this.mIsActive = isActive;
        this.mDepartureLocation = departureLocation;
        this.mDestinationLocation = destinationLocation;
    }

    public String getId() {
        return mId;
    }

    public void setId(String mId) {
        this.mId = mId;
    }

    public boolean isDeleted() {
        return mDeleted;
    }

    public void setDeleted(boolean mDeleted) {
        this.mDeleted = mDeleted;
    }

    public boolean isActive() {
        return mIsActive;
    }

    public void setActive(boolean active) {
        mIsActive = active;
    }

    public Date getDepartureTime() {
        return mDepartureTime;
    }

    public void setDepartureTime(Date departureTime) {
        this.mDepartureTime = departureTime;
    }

    public Location getDepartureLocation() {
        return mDepartureLocation;
    }

    public void setDepartureLocation(Location departureLocation) {
        this.mDepartureLocation = departureLocation;
    }

    public Location getDestinationLocation() {
        return mDestinationLocation;
    }

    public void setDestinationLocation(Location destinationLocation) {
        this.mDestinationLocation = destinationLocation;
    }
}
