package com.mad.taxius.journeyadd;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.maps.model.LatLng;
import com.mad.taxius.constant.Constant;
import com.mad.taxius.database.journeyrepository.JourneyRepository;
import com.mad.taxius.database.journeyrepository.JourneyRepositoryContract;
import com.mad.taxius.model.Journey;
import com.mad.taxius.model.Location;
import com.mad.taxius.model.ParcelablePlace;
import com.mad.taxius.util.DateConverter;
import com.mad.taxius.util.NetworkChecker;

import java.util.Date;

import javax.inject.Inject;

/**
 * Presenter class for adding journey to perform adding journey functions
 */
public class JourneyAddPresenter implements JourneyAddContract.JourneyAdd,
        JourneyRepositoryContract.JourneyAddCallback {

    private final JourneyAddContract.PublishToView mPublishToViewInteractor;
    private final Context mContext;
    private final JourneyRepository mJourneyRepository;
    private static final int DEFAULT_SECOND = 0;

    private Date mDepartureTime;
    private ParcelablePlace mDeparturePlace;
    private ParcelablePlace mDestinationPlace;

    @Inject
    public JourneyAddPresenter(JourneyAddContract.PublishToView publishToView,
                               Context context, JourneyRepository journeyRepository) {
        this.mPublishToViewInteractor = publishToView;
        this.mContext = context;
        this.mJourneyRepository = journeyRepository;
    }

    /**
     * Set the private fields of places to selected places
     *
     * @param departurePlace   is the selected departure place
     * @param destinationPlace is the selected destination place
     */
    @Override
    public void setUpSelectedPlaces(ParcelablePlace departurePlace, ParcelablePlace destinationPlace) {
        mDeparturePlace = departurePlace;
        mDestinationPlace = destinationPlace;
        mPublishToViewInteractor.displaySelectedPlace(mDeparturePlace.getName(), mDestinationPlace.getName());
    }

    /**
     * Called when the departure time is set in the time dialog from its activity
     *
     * @param hourOfDay is the value of selected hour
     * @param minute    is the value of selected minute
     */
    @Override
    public void onDepartureTimeSet(int hourOfDay, int minute) {
        mDepartureTime = DateConverter.getDateWithTimeSetTo(hourOfDay, minute, DEFAULT_SECOND);
        mPublishToViewInteractor.displayTime(DateConverter.getTimePortionAsStringFrom(mDepartureTime));
    }

    /**
     * Create journey by calling saving new journey method in journey repository
     */
    @Override
    public void createJourney() {
        mPublishToViewInteractor.hideErrorMsg();
        if (!NetworkChecker.isNetworkAvailable(mContext)) {
            mPublishToViewInteractor.showNetworkErrorMsg();
        } else if (mDepartureTime != null) {
            mPublishToViewInteractor.showProgressDialog();
            mJourneyRepository.saveJourney(getJourneyToSave(), this);
        } else mPublishToViewInteractor.displayDepartureTimeErrorMsg();
    }

    /**
     * Get journey selected to save to the database
     *
     * @return journey object to be saved
     */
    private Journey getJourneyToSave() {
        Location departureLocation = getLocationToSave(mDeparturePlace.getName(),
                mDeparturePlace.getLatLng().latitude, mDeparturePlace.getLatLng().longitude,
                Constant.LocationType.DEPARTURE);
        Location destinationLocation = getLocationToSave(mDestinationPlace.getName(),
                mDestinationPlace.getLatLng().latitude, mDestinationPlace.getLatLng().longitude,
                Constant.LocationType.DESTINATION);
        return new Journey(DateConverter.convertToUtcTime(mDepartureTime),
                true, departureLocation, destinationLocation);
    }

    /**
     * Get location object to be saved along with new journey
     *
     * @param name         is the name of selected location, or place
     * @param latitude     is the latitude value of selected location, or place
     * @param longitude    is the longitude value of selected location, or place
     * @param locationType is the type of location
     * @return location object to be saved
     */
    private Location getLocationToSave(String name, double latitude, double longitude, String locationType) {
        return new Location(name, latitude, longitude, locationType);
    }

    /**
     * Called when adding a new journey to the database is successful
     *
     * @param journey is the journey object saved
     */
    @Override
    public void onSucceedToAddJourney(Journey journey) {
        mPublishToViewInteractor.hideProgressDialog();
        mPublishToViewInteractor.startJourneyChatActivity(journey.getId(),
                DateConverter.getLocalDateAsStringFromUtc(journey.getDepartureTime()),
                journey.getDepartureLocation().getName(),
                journey.getDestinationLocation().getName());
    }

    /**
     * Called when failed to add journey data to the database
     */
    @Override
    public void onFailToAddJourney() {
        mPublishToViewInteractor.hideProgressDialog();
        mPublishToViewInteractor.displayJourneyAddFailMessage();
    }
}
