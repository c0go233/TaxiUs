package com.mad.taxius.journeyadd;

import android.content.Intent;

import com.mad.taxius.base.BaseContract;
import com.mad.taxius.model.ParcelablePlace;

import java.util.Date;

/**
 * Contract class that contains method declarations among journeyAdd activity and presenter
 */
public interface JourneyAddContract {

    /**
     * Interface for communicating to the journeyAddactivity from journeyAddpresenter
     */
    interface PublishToView extends BaseContract {
        void displayTime(String time);

        void displaySelectedPlace(String departurePlace, String destinationPlace);

        void displayDepartureTimeErrorMsg();

        void displayJourneyAddFailMessage();

        void startJourneyChatActivity(String journeyId, String departureTime,
                                      String departurePlaceName, String destinationPlaceName);
    }

    /**
     * Interface that communicates to the presenter from activity to add a new journey
     */
    interface JourneyAdd {
        void createJourney();

        void onDepartureTimeSet(int hourOfDay, int minute);

        void setUpSelectedPlaces(ParcelablePlace departurePlace, ParcelablePlace destinationPlace);
    }
}
