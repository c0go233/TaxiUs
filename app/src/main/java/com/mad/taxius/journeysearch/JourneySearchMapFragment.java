package com.mad.taxius.journeysearch;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.mad.taxius.R;
import com.mad.taxius.constant.Constant;
import com.mad.taxius.eventbusmessage.PlaceSelectedMessageEvent;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 */
public class JourneySearchMapFragment extends Fragment implements
        OnMapReadyCallback {

    private static final int ZOOM_IN = 14;

    private GoogleMap mMap;
    private Marker mDepartureMarker;
    private Marker mDestinationMarker;
    private ArrayList<Marker> mJourneyLocationMarkers = new ArrayList<>();


    public JourneySearchMapFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_journey_search_map, container, false);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        SupportMapFragment mapFragment = (SupportMapFragment) getChildFragmentManager().
                findFragmentById(R.id.journey_search_fragment_map);
        mapFragment.getMapAsync(this);
        EventBus.getDefault().register(this);
    }

    /**
     * Called when the google map is ready to use
     *
     * @param googleMap is the google map
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        LatLng sydney = new LatLng(-34, 151);
        mMap.addMarker(new MarkerOptions().position(sydney).title("Marker in Sydney"));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));
    }

    /**
     * Listener for the event bus message
     *
     * @param event is the event passed
     */
    @Subscribe
    public void onEvent(PlaceSelectedMessageEvent event) {
        int placeCode = event.getSelectedPlaceCode();
        switch (placeCode) {
            case Constant.Place.IS_DEPARTURE:
                updateDepartureMarker(event.getPlaces().get(0));
                break;
            case Constant.Place.IS_DESTINATION:
                updateDestinationMarker(event.getPlaces().get(0));
                break;
            case Constant.Place.IS_JOURNEYS:
                updateJourneyLocationMarker(event.getPlaces());
                break;
        }
    }

    /**
     * Update searched journey's location markers on map
     *
     * @param journeyLocations is the location list of journeys searched
     */
    private void updateJourneyLocationMarker(ArrayList<LatLng> journeyLocations) {
        clearJourneyLocationMarker();
        int iconResourceId;
        for (int i = 0; i < journeyLocations.size(); i++) {
            if ((i % 2) == 0) iconResourceId = R.drawable.ic_departure_location;
            else iconResourceId = R.drawable.ic_flag;
            LatLng location = journeyLocations.get(i);
            mJourneyLocationMarkers.add(mMap.addMarker(new MarkerOptions()
                    .icon(BitmapDescriptorFactory.fromResource(iconResourceId))
                    .position(location)));
        }
    }

    /**
     * Clear added markers on a map
     */
    private void clearJourneyLocationMarker() {
        for (int i = 0; i < mJourneyLocationMarkers.size(); i++) {
            mJourneyLocationMarkers.get(i).remove();
        }
    }

    /**
     * Update departure place marker on a map
     *
     * @param position is the location of the selected departure place
     */
    private void updateDepartureMarker(LatLng position) {
        if (mDepartureMarker == null) {
            mDepartureMarker = mMap.addMarker(new MarkerOptions()
                    .icon(BitmapDescriptorFactory.fromResource(R.drawable.ic_departure_location_yellow))
                    .position(position));
        } else mDepartureMarker.setPosition(position);
        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(position, ZOOM_IN));
    }

    /**
     * Update destination marker on a map
     *
     * @param position is the location of selected destination place
     */
    private void updateDestinationMarker(LatLng position) {
        if (mDestinationMarker == null) {
            mDestinationMarker = mMap.addMarker(new MarkerOptions()
                    .icon(BitmapDescriptorFactory.fromResource(R.drawable.ic_flag_yellow))
                    .position(position));
        } else mDestinationMarker.setPosition(position);
        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(position, ZOOM_IN));
    }


}
