package com.mad.taxius.eventbusmessage;

import com.google.android.gms.maps.model.LatLng;
import com.mad.taxius.constant.Constant;

import java.util.ArrayList;

/**
 * Event bus message class to pass the event that a place selected from the
 * searching page
 */
public class PlaceSelectedMessageEvent {

    private ArrayList<LatLng> mPlaces = new ArrayList<>();
    private int mSelectedPlaceCode;

    public PlaceSelectedMessageEvent(LatLng place, int selectedPlaceCode) {
        mPlaces.add(place);
        this.mSelectedPlaceCode = selectedPlaceCode;
    }

    public PlaceSelectedMessageEvent(ArrayList<LatLng> places) {
        this.mPlaces = places;
        mSelectedPlaceCode = Constant.Place.IS_JOURNEYS;
    }

    public ArrayList<LatLng> getPlaces() {
        return mPlaces;
    }

    public int getSelectedPlaceCode() {
        return mSelectedPlaceCode;
    }
}
