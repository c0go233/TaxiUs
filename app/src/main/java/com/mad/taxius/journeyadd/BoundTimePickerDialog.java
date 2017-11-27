package com.mad.taxius.journeyadd;

import android.app.TimePickerDialog;
import android.content.Context;
import android.widget.TimePicker;

import java.util.Calendar;

/**
 * Extended timepicker dialog that implements the logic to accept only the time after current time
 */
public class BoundTimePickerDialog extends TimePickerDialog {

    private int mMinHour = -1, mMinMinute = -1;
    private int mCurrentHour, mCurrentMinute;

    public BoundTimePickerDialog(Context context, OnTimeSetListener callBack, int hourOfDay, int minute) {
        super(context, callBack, hourOfDay, minute, true);
        setCurrentTimeAsMin(hourOfDay, minute);
    }

    /**
     * Set the minimum time for selection
     *
     * @param minHour   is value of minimum hour
     * @param minMinute if value of minimum minute
     */
    private void setCurrentTimeAsMin(int minHour, int minMinute) {
        this.mCurrentHour = minHour;
        this.mCurrentMinute = minMinute;
        this.mMinHour = minHour;
        this.mMinMinute = minMinute;
    }

    /**
     * Called when time changed and it only set the time if it is more than minimum time
     *
     * @param view      is time picker in which time change occurred
     * @param hourOfDay is the selected hour
     * @param minute    is the selected minute
     */
    @Override
    public void onTimeChanged(TimePicker view, int hourOfDay, int minute) {
        super.onTimeChanged(view, hourOfDay, minute);
        boolean isTimeValid;
        if (hourOfDay < mMinHour) isTimeValid = false;
        else if (hourOfDay == mMinHour) isTimeValid = minute >= mMinMinute;
        else isTimeValid = true;

        if (isTimeValid) setCurrentTimeWith(hourOfDay, minute);
        else updateTime(mCurrentHour, mCurrentMinute);
    }

    /**
     * Set current time with selected time
     *
     * @param hourOfDay is the value of selected hour
     * @param minute    is the value of selected minute
     */
    private void setCurrentTimeWith(int hourOfDay, int minute) {
        mCurrentHour = hourOfDay;
        mCurrentMinute = minute;
    }
}
