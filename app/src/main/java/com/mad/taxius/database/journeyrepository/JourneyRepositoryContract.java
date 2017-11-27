package com.mad.taxius.database.journeyrepository;

import com.mad.taxius.model.Journey;

import java.util.ArrayList;

/**
 * Contract class that contains methods' declarations for journey repository operation
 */
public interface JourneyRepositoryContract {

    /**
     * Callback interface for adding a journey
     */
    interface JourneyAddCallback {
        void onSucceedToAddJourney(Journey journey);
        void onFailToAddJourney();
    }

    /**
     * Callback interface for searching journeys
     */
    interface JourneySearchCallback {
        void onSucceedToGetJourney(ArrayList<Journey> journeys);
        void onFailToGetJourney();
    }
}
