package com.mad.taxius.constant;

/**
 * Constant class that contains all the constant values used in the application
 * Each class inside contains its corresponding constant values
 */
public class Constant {

    public class TimerService {
        public static final String TIME_KEY = "Time";
    }

    public class Setting {
        public static final int DEFAULT_DISTANCE_RANGE = 500;
        public static final int DISTANCE_RANGE_MAX = 2000;
    }

    public class Activity {
        public static final String KEY = "activity";
        public static final String JOURNEY_SEARCH = "journeySearchActivity";
        public static final String JOURNEY_ADD = "journeyAddActivityKey";
    }

    public class UserDetail {
        public static final int MINIMUM_NAME_LENGTH = 1;
        public static final int MAXIMUM_NAME_LENGTH = 50;
        public static final int MINIMUM_PASSWORD_LENGTH = 8;
        public static final int MAXIMUM_PASSWORD_LENGTH = 20;
    }

    public class PreferenceKey {
        public static final String LOGINED_EMAIL = "userEmail";
        public static final String LOGINED = "logined";
        public static final String NAME = "name";
        public static final String DISTANCE_RANGE = "distanceRange";
    }

    public class GoogleSignin {
        public static final int REQUEST_CODE = 99;
    }

    public class Database {
        public static final String CONNECTION_URL = "https://taxius.azurewebsites.net";
        public static final String FIREBASE_TABLE_JOURNEY_CHAT = "journeychatrooms";
    }

    public class LocationType {
        public static final String DEPARTURE = "departure";
        public static final String DESTINATION = "destination";
    }

    public class Journey {
        public static final String JOURNEY_ID_KEY = "journeyId";
        public static final String JOURNEY_DEPARTURE_TIME_KEY = "departureTime";
    }

    public class Place {
        public static final int DEPARTURE_AUTO_ADDRESS_REQUEST_CODE = 100;
        public static final int DESTINATION_AUTO_ADDRESS_REQUEST_CODE = 101;
        public static final int IS_DEPARTURE = 0;
        public static final int IS_DESTINATION = 1;
        public static final int IS_JOURNEYS = 2;
        public static final String DEPARTURE_KEY = "departure";
        public static final String DESTINATION_KEY = "destination";
    }

    public class Debug {
        public static final String TAG = "TaxiUs";
    }
}
