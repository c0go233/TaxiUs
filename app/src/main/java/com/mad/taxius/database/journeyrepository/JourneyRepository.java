package com.mad.taxius.database.journeyrepository;

import android.content.Context;
import android.os.AsyncTask;

import java.util.Calendar;

import android.util.Log;
import android.util.Pair;

import com.google.android.gms.location.places.Place;
import com.google.android.gms.maps.model.LatLng;
import com.google.common.util.concurrent.FutureCallback;
import com.google.common.util.concurrent.Futures;
import com.google.common.util.concurrent.ListenableFuture;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.mad.taxius.constant.Constant;
import com.mad.taxius.model.Journey;
import com.mad.taxius.model.Location;
import com.mad.taxius.util.DateConverter;
import com.mad.taxius.util.GsonConverter;
import com.microsoft.windowsazure.mobileservices.ApiJsonOperationCallback;
import com.microsoft.windowsazure.mobileservices.ApiOperationCallback;
import com.microsoft.windowsazure.mobileservices.MobileServiceClient;
import com.microsoft.windowsazure.mobileservices.MobileServiceList;
import com.microsoft.windowsazure.mobileservices.http.ServiceFilterResponse;
import com.microsoft.windowsazure.mobileservices.table.MobileServiceTable;

import java.util.ArrayList;
import java.util.concurrent.ExecutionException;

import javax.inject.Inject;

/**
 * Repository class for journey that contains methods for save and retrieve journey data
 */
public class JourneyRepository {

    private MobileServiceClient mClient;
    private MobileServiceTable<Journey> mJourneyTable;
    private Context mContext;

    private final static String PARAMETER_DISTANCE_RANGE = "distanceRange";
    private final static String PARAMETER_DEPARTURE_LATITUDE = "departureLatitude";
    private final static String PARAMETER_DEPARTURE_LONGITUDE = "departureLongitude";
    private final static String PARAMETER_DESTINATION_LATITUDE = "destinationLatitude";
    private final static String PARAMETER_DESTINATION_LONGITUDE = "destinationLongitude";

    private final static int INDEX_RANGE = 0;
    private final static int INDEX_DEPARTURE_LATITUDE = 1;
    private final static int INDEX_DEPARTURE_LONGITUDE = 2;
    private final static int INDEX_DESTINATION_LATITUDE = 3;
    private final static int INDEX_DESTINATION_LONGITUDE = 4;

    @Inject
    public JourneyRepository(Context context, MobileServiceClient client) {
        this.mClient = client;
        this.mContext = context;
        setUp();
    }

    /**
     * Set up reference for journey table in Azure database
     */
    private void setUp() {
        if (mClient != null) mJourneyTable = mClient.getTable(Journey.class);
        mClient.registerSerializer(Location.class, new GsonConverter<Location>());
    }

    /**
     * Get journeys created that are in certain range
     *
     * @param rangeInMeter     the range value defined by user
     * @param departurePlace   departure place of journey
     * @param destinationPlace destination place of journey
     * @param callback         callback interface
     */
    public void getJourneyInRange(int rangeInMeter, Place departurePlace, Place destinationPlace,
                                  JourneyRepositoryContract.JourneySearchCallback callback) {
        LatLng departureLatLng = departurePlace.getLatLng();
        LatLng destinationLatLng = destinationPlace.getLatLng();
        new GetJourneyInRangeAsyncTask(callback).execute((double) rangeInMeter, departureLatLng.latitude,
                departureLatLng.longitude, destinationLatLng.latitude, destinationLatLng.longitude);
    }

    /**
     * Async class that retrieves the journeys that are in rage
     */
    private class GetJourneyInRangeAsyncTask extends AsyncTask<Double, Void, ArrayList<Journey>> {

        private JourneyRepositoryContract.JourneySearchCallback mCallback;

        public GetJourneyInRangeAsyncTask(JourneyRepositoryContract.JourneySearchCallback callback) {
            this.mCallback = callback;
        }

        /**
         * Get the journeys that are in rage defined by user on another thread
         *
         * @param params the parameters used to filter journeys
         * @return journeys retrieved in the database
         */
        @Override
        protected ArrayList<Journey> doInBackground(Double... params) {
            try {
                //Get journeys within a range defined by user
                Log.d(Constant.Debug.TAG, "Get journeys in the range from Azure Database");
                ArrayList<String> parameters = getParametersAsString(params);
                ArrayList<Journey> journeys = mJourneyTable.where().parameter(PARAMETER_DISTANCE_RANGE, parameters.get(INDEX_RANGE))
                        .parameter(PARAMETER_DEPARTURE_LATITUDE, parameters.get(INDEX_DEPARTURE_LATITUDE))
                        .parameter(PARAMETER_DEPARTURE_LONGITUDE, parameters.get(INDEX_DEPARTURE_LONGITUDE))
                        .parameter(PARAMETER_DESTINATION_LATITUDE, parameters.get(INDEX_DESTINATION_LATITUDE))
                        .parameter(PARAMETER_DESTINATION_LONGITUDE, parameters.get(INDEX_DESTINATION_LONGITUDE))
                        .execute().get();
                return journeys;
            } catch (ExecutionException | InterruptedException e) {
                Log.e(Constant.Debug.TAG, "Error from getting journey in the range from Azure Database");
                return null;
            }
        }

        /**
         * Perform logic based on the result of retrieving journeys on main thread
         *
         * @param journeys journeys retrieved
         */
        @Override
        protected void onPostExecute(ArrayList<Journey> journeys) {
            if (journeys == null) mCallback.onFailToGetJourney();
            else mCallback.onSucceedToGetJourney(journeys);
        }
    }

    /**
     * Get the latitude and longitude values as string to be used in the URL parameter
     *
     * @param parametersAsDouble latitude and longitude values as double
     * @return string values of latitude and longitude
     */
    private ArrayList<String> getParametersAsString(Double[] parametersAsDouble) {
        ArrayList<String> parametersAsString = new ArrayList<>();
        for (double parameter : parametersAsDouble) {
            parametersAsString.add(String.valueOf(parameter));
        }
        return parametersAsString;
    }

    /**
     * Save journey created by user to the database
     *
     * @param journey  journey created
     * @param callback callback interface
     */
    public void saveJourney(Journey journey, JourneyRepositoryContract.JourneyAddCallback callback) {
        new JourneyAddAsyncTask(callback).execute(journey);
    }

    /**
     * Async class to ass the journey created to the database
     */
    private class JourneyAddAsyncTask extends AsyncTask<Journey, Void, Journey> {

        private JourneyRepositoryContract.JourneyAddCallback mCallback;

        public JourneyAddAsyncTask(JourneyRepositoryContract.JourneyAddCallback callback) {
            mCallback = callback;
        }

        /**
         * Save the journey created to the database on another thread
         *
         * @param params journey created
         * @return saved journey or null
         */
        @Override
        protected Journey doInBackground(Journey... params) {
            try {
                Log.d(Constant.Debug.TAG, "Save new journey to Azure database");
                return mJourneyTable.insert(params[0]).get();
            } catch (ExecutionException | InterruptedException e) {
                Log.e(Constant.Debug.TAG, "Error from saving new journey to Azure Database");
                return null;
            }
        }

        /**
         * Perform login based on the results of saving journey to database on main thread
         *
         * @param journey journey saved
         */
        @Override
        protected void onPostExecute(Journey journey) {
            if (journey == null) mCallback.onFailToAddJourney();
            else mCallback.onSucceedToAddJourney(journey);
        }
    }
}
