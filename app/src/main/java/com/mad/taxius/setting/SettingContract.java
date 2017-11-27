package com.mad.taxius.setting;

/**
 * Created by kisungTae on 13/10/2017.
 */

public interface SettingContract {

    /**
     * Interface that communicates to the Settingactivity from Settingpresenter
     */
    interface PublishToView {
        void setDistanceRangeProgress(int progress);
        void notifySettingSaved();
    }

    /**
     * Interface that communicates to the presenter from activity to perform
     * operations related to setting function
     */
    interface Setting {
        void saveSetting(int distanceRange);

        void onSetUpDistanceRangeSeekBar();

        void onDistanceRangeChanged(int progress);
    }
}
