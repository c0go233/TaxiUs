package com.mad.taxius.journeysearch;

import android.content.Intent;

import com.mad.taxius.base.BaseContract;
import com.mad.taxius.model.Journey;
import com.mad.taxius.model.ParcelablePlace;

import java.lang.reflect.ParameterizedType;
import java.util.ArrayList;

public interface JourneySearchContract {


    interface OnJourneyClickListener {
        void onJourneyItemClicked(Journey journey);
    }

    /**
     * Interface that communicates to the journeySearchactivity from journeySearchpresenter
     */
    interface PublishToView extends BaseContract {
        void onGooglePlayServiceAvailable();

        void onGooglePlayServiceUnavailable();

        void setDepartureEt(String departure);

        void setDestinationEt(String destination);

        void changeToJourneySearchedMode();

        void changeToJourneySearchingMode();

        void showJourneyRecyclerView();

        void setJourneyRecyclerView(JourneyAdapter journeyAdapter);

        void displayNoJourneyResult();

        void startJourneyChatActivity(String journeyId, String departureTime,
                                      String departurePlaceName, String destinationPlaceName);

        void startJourneyAddActivity(ParcelablePlace departurePlace, ParcelablePlace destinationPlace);

        void finishActivity();

        void onSetUserProfile(String email, String name);

        void showPlaceNotSelectedErrorMsg();

        void showFailToGetJourneyErrorMsg();

        void startLoginActivity();
    }

    /**
     * Interface that communicates to the presenter from activity to perform
     * operations related to searching journeys
     */
    interface JourneySearch {
        void checkGooglePlayServiceAvailable();

        void onAutoCompleteAddressActivityResult(Intent data, int requestCode);

        void onCancelJourneySearchedMode();

        void onAddFabClicked();

        void findMatchedJourney();
    }

    interface NavigationMenu {
        void signOut();

        void setUserProfile();
    }
}
