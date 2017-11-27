package com.mad.taxius.util;

/**
 * This class is for converting distance in various unit of measurements and format
 */

public class DistanceConverter {

    private static final String METER = "m";
    private static final String KILO_METER = "km";

    /**
     * Get distance as string with them formatted in meters and kilometers units
     *
     * @param distance is the distance value
     * @return string of the distance passed
     */
    public static String getDistanceAsString(double distance) {
        int distanceInMeter = (int) Math.round(distance);
        if (distanceInMeter > 999) {
            double distanceInKm = distanceInMeter / 1000;
            return String.valueOf(distanceInKm) + KILO_METER;
        }
        return String.valueOf(distanceInMeter) + METER;
    }
}
