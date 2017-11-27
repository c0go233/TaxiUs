package com.mad.taxius.util;

import android.util.Log;

import com.mad.taxius.constant.Constant;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

/**
 * Class is for converting Date to various format
 */

public class DateConverter {

    private static final String TIME_FORMAT = "HH:mm";
    private static final String DATE_FORMAT = "dd-MM-yyyy HH:mm:ss";
    private static final String UTC_TIME_ZONE = "UTC";

    /**
     * Get the date object set to the values passed through parameters
     *
     * @param hourOfDay is the value of hour to which Date object is set
     * @param minute    is the value of minute to which Date object is set
     * @param second    the value of second to which Date object is set
     * @return set date object
     */
    public static Date getDateWithTimeSetTo(int hourOfDay, int minute, int second) {
        Date date = new Date();
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.set(Calendar.HOUR_OF_DAY, hourOfDay);
        calendar.set(Calendar.MINUTE, minute);
        calendar.set(Calendar.SECOND, second);
        return calendar.getTime();
    }

    /**
     * Get the time portion as string from Date object
     *
     * @param date is the source Date object
     * @return the time portion of the date object as string
     */
    public static String getTimePortionAsStringFrom(Date date) {
        SimpleDateFormat dateFormat = new SimpleDateFormat(TIME_FORMAT);
        return dateFormat.format(date.getTime());
    }

    /**
     * Convert Date object from local to UTC
     *
     * @param date is the local Date object
     * @return converted date object
     */
    public static Date convertToUtcTime(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeZone(TimeZone.getTimeZone(UTC_TIME_ZONE));
        calendar.setTime(date);
        return calendar.getTime();
    }

    /**
     * Convert from UTC date to local date
     *
     * @param utcDate is the UTC date
     * @return local date object
     */
    public static String getLocalDateAsStringFromUtc(Date utcDate) {
        SimpleDateFormat dateFormat = new SimpleDateFormat(DATE_FORMAT);
        return dateFormat.format(utcDate);
    }

    /**
     * Get subtraction in seconds between now and target journey's departure time
     * @param time is the journey departure time
     * @return subtracted time
     */
    public static long getTimeToCount(String time) {
        SimpleDateFormat dateFormat = new SimpleDateFormat(DATE_FORMAT);
        try {
            Date targetDate = dateFormat.parse(time);
            return (targetDate.getTime() - new Date().getTime());
        } catch (ParseException e) {
            return -1;
        }
    }
}
