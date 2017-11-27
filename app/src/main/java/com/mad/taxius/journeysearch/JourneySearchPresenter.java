package com.mad.taxius.journeysearch;

import android.content.Context;
import android.content.Intent;
import android.content.Loader;
import android.content.SharedPreferences;
import android.os.Parcel;
import android.os.Parcelable;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.common.api.Api;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlaceAutocomplete;
import com.google.android.gms.maps.model.LatLng;
import com.mad.taxius.R;
import com.mad.taxius.constant.Constant;
import com.mad.taxius.database.joineduserrepository.JoinedUserRepository;
import com.mad.taxius.database.journeyrepository.JourneyRepository;
import com.mad.taxius.database.journeyrepository.JourneyRepositoryContract;
import com.mad.taxius.eventbusmessage.PlaceSelectedMessageEvent;
import com.mad.taxius.journeyadd.JourneyAddActivity;
import com.mad.taxius.journeychat.JourneyChatActivity;
import com.mad.taxius.model.Journey;
import com.mad.taxius.model.ParcelablePlace;
import com.mad.taxius.util.DateConverter;
import com.mad.taxius.util.NetworkChecker;

import org.greenrobot.eventbus.EventBus;

import java.io.FileDescriptor;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Date;
import java.util.concurrent.TimeUnit;

import javax.inject.Inject;

/**
 * Presenter class for the journeySearching activity to perform searching journey functions
 */
public class JourneySearchPresenter implements JourneySearchContract.JourneySearch,
        JourneyRepositoryContract.JourneySearchCallback, JourneySearchContract.OnJourneyClickListener,
        JourneySearchContract.NavigationMenu {

    private final JourneySearchContract.PublishToView mPublishToViewInteractor;
    private final Context mContext;
    private final JourneyRepository mJourneyRepository;


    private Place mDeparturePlace;
    private Place mDestinationPlace;
    private JourneyAdapter mJourneyAdapter;
    private ArrayList<Journey> mJourneys;

    @Inject
    public JourneySearchPresenter(JourneySearchContract.PublishToView publishToView,
                                  Context context, JourneyRepository journeyRepository) {
        this.mPublishToViewInteractor = publishToView;
        this.mContext = context;
        this.mJourneyRepository = journeyRepository;
        this.mJourneys = new ArrayList<>();
    }

    /**
     * Called when the auto-complete address activity is done to add selected place to its fields
     *
     * @param data        is the selected placed in the activity
     * @param requestCode is the request code for the activty done
     */
    @Override
    public void onAutoCompleteAddressActivityResult(Intent data, int requestCode) {
        Place place = PlaceAutocomplete.getPlace(mContext, data);
        if (requestCode == Constant.Place.DEPARTURE_AUTO_ADDRESS_REQUEST_CODE) {
            mPublishToViewInteractor.setDepartureEt(place.getName().toString());
            mDeparturePlace = place;
            EventBus.getDefault().post(new PlaceSelectedMessageEvent(place.getLatLng(), Constant.Place.IS_DEPARTURE));
        } else if (requestCode == Constant.Place.DESTINATION_AUTO_ADDRESS_REQUEST_CODE) {
            mPublishToViewInteractor.setDestinationEt(place.getName().toString());
            mDestinationPlace = place;
            EventBus.getDefault().post(new PlaceSelectedMessageEvent(place.getLatLng(), Constant.Place.IS_DESTINATION));
        }
        if (mDeparturePlace != null && mDestinationPlace != null) findMatchedJourney();
    }

    /**
     * Perform the logic based on the availability of google play service
     */
    @Override
    public void checkGooglePlayServiceAvailable() {
        if (isGooglePlayServiceAvailable()) mPublishToViewInteractor.onGooglePlayServiceAvailable();
        else mPublishToViewInteractor.onGooglePlayServiceUnavailable();
    }

    /**
     * Change the journeySearching activity to searching mode
     */
    @Override
    public void onCancelJourneySearchedMode() {
        mPublishToViewInteractor.changeToJourneySearchingMode();
    }

    /**
     * Called when the journey add flaoting button is clicked to create a new journey
     */
    @Override
    public void onAddFabClicked() {
        if (mDeparturePlace == null || mDestinationPlace == null) {
            mPublishToViewInteractor.showPlaceNotSelectedErrorMsg();
        } else {
            mPublishToViewInteractor.startJourneyAddActivity(new ParcelablePlace(mDeparturePlace),
                    new ParcelablePlace(mDestinationPlace));
        }
    }

    /**
     * Find the journeys that are in the specific range of user's desired journey
     */
    @Override
    public void findMatchedJourney() {
        if (NetworkChecker.isNetworkAvailable(mContext)) {
            mPublishToViewInteractor.showProgressDialog();
            mJourneyRepository.getJourneyInRange(getDistanceRange(), mDeparturePlace, mDestinationPlace, this);
            Log.d(Constant.Debug.TAG, "Selected distance range: " + String.valueOf(getDistanceRange()));
        } else {
            mPublishToViewInteractor.showNetworkErrorMsg();
        }
    }

    /**
     * Get selected distance range for search for journeys
     *
     * @return selected distance range
     */
    private int getDistanceRange() {
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(mContext);
        return sp.getInt(Constant.PreferenceKey.DISTANCE_RANGE, Constant.Setting.DEFAULT_DISTANCE_RANGE);
    }

    /**
     * Checks if google service is available on a mobile phone
     *
     * @return
     */
    private boolean isGooglePlayServiceAvailable() {
        GoogleApiAvailability api = GoogleApiAvailability.getInstance();
        int isAvailable = api.isGooglePlayServicesAvailable(mContext);
        if (isAvailable == ConnectionResult.SUCCESS) return true;
        return false;
    }

    /**
     * Called when journeys are retrieved from the database and display them to the screen
     * via journeySearching activity
     *
     * @param journeys is the list of journeys searched
     */
    @Override
    public void onSucceedToGetJourney(ArrayList<Journey> journeys) {
        mPublishToViewInteractor.hideProgressDialog();
        mPublishToViewInteractor.changeToJourneySearchedMode();
        if (journeys.get(0) == null) mPublishToViewInteractor.displayNoJourneyResult();
        else showSearchedJourneyList(journeys);
    }

    /**
     * Show the list view for the searched journeys
     *
     * @param journeys is the list of journeys searched
     */
    private void showSearchedJourneyList(ArrayList<Journey> journeys) {
        fillSearchedJourneys(journeys);
        if (mJourneyAdapter == null) {
            mJourneyAdapter = new JourneyAdapter(mContext, mJourneys, this);
            mPublishToViewInteractor.setJourneyRecyclerView(mJourneyAdapter);
        } else mJourneyAdapter.notifyDataSetChanged();
        mPublishToViewInteractor.showJourneyRecyclerView();
        EventBus.getDefault().post(new PlaceSelectedMessageEvent(getLocations()));
    }

    /**
     * Fill the searched journeys in its list field
     *
     * @param journeys is the list of journeys searched
     */
    private void fillSearchedJourneys(ArrayList<Journey> journeys) {
        this.mJourneys.clear();
        this.mJourneys.addAll(journeys);
    }

    /**
     * Get locations of places of journeys as list
     *
     * @return list of locations of places
     */
    private ArrayList<LatLng> getLocations() {
        ArrayList<LatLng> locations = new ArrayList<>();
        for (int i = 0; i < mJourneys.size(); i++) {
            Journey currentJourney = mJourneys.get(i);
            locations.add(currentJourney.getDepartureLocation().getLatLng());
            locations.add(currentJourney.getDestinationLocation().getLatLng());
        }
        return locations;
    }

    /**
     * Called when failed to get journeys from the database
     */
    @Override
    public void onFailToGetJourney() {
        mPublishToViewInteractor.hideProgressDialog();
        mPublishToViewInteractor.showFailToGetJourneyErrorMsg();
    }

    /**
     * Called when journey is clicked in the list to move to its chatroom
     *
     * @param journey is the clicked journey item
     */
    @Override
    public void onJourneyItemClicked(Journey journey) {
        Date date = journey.getDepartureTime();
        mPublishToViewInteractor.startJourneyChatActivity(journey.getId(),
                DateConverter.getLocalDateAsStringFromUtc(journey.getDepartureTime()),
                journey.getDepartureLocation().getName(),
                journey.getDestinationLocation().getName());
    }

    /**
     * Sign out logged in user by removing user profiles in preferences
     */
    @Override
    public void signOut() {
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(mContext);
        SharedPreferences.Editor editor = sp.edit();
        editor.remove(Constant.PreferenceKey.LOGINED_EMAIL);
        editor.remove(Constant.PreferenceKey.NAME);
        editor.putBoolean(Constant.PreferenceKey.LOGINED, false);
        editor.commit();
        mPublishToViewInteractor.startLoginActivity();
        mPublishToViewInteractor.finishActivity();
    }

    /**
     * Set user profile in the navigation view
     */
    @Override
    public void setUserProfile() {
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(mContext);
        mPublishToViewInteractor.onSetUserProfile(
                sp.getString(Constant.PreferenceKey.LOGINED_EMAIL, ""),
                sp.getString(Constant.PreferenceKey.NAME, ""));
    }
}
