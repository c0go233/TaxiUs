package com.mad.taxius.model;

import android.net.Uri;
import android.os.Parcel;
import android.os.Parcelable;

import com.google.android.gms.location.places.Place;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;

import java.util.List;
import java.util.Locale;

/**
 * ParcelablePlace class is used to move location object between activities via intent
 */

public class ParcelablePlace implements Parcelable {
    private LatLng mLatLng;
    private String mName;

    public ParcelablePlace(Place place) {
        this.mLatLng = place.getLatLng();
        this.mName = place.getName().toString();
    }

    protected ParcelablePlace(Parcel in) {
        mLatLng = in.readParcelable(LatLng.class.getClassLoader());
        mName = in.readString();
    }

    public static final Creator<ParcelablePlace> CREATOR = new Creator<ParcelablePlace>() {
        @Override
        public ParcelablePlace createFromParcel(Parcel in) {
            return new ParcelablePlace(in);
        }

        @Override
        public ParcelablePlace[] newArray(int size) {
            return new ParcelablePlace[size];
        }
    };

    public LatLng getLatLng() {
        return mLatLng;
    }

    public void setLatLng(LatLng latLng) {
        this.mLatLng = latLng;
    }

    public String getName() {
        return mName;
    }

    public void setName(String mName) {
        this.mName = mName;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeParcelable(mLatLng, flags);
        dest.writeString(mName);
    }
}
