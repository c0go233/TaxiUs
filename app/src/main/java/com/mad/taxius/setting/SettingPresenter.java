package com.mad.taxius.setting;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import com.mad.taxius.constant.Constant;

import javax.inject.Inject;

/**
 * Presenter class for the Setting activity to perform setting function
 */

public class SettingPresenter implements SettingContract.Setting {

    private Context mContext;
    private SettingContract.PublishToView mPublishToView;

    @Inject
    public SettingPresenter(Context context, SettingContract.PublishToView publishToView) {
        this.mContext = context;
        this.mPublishToView = publishToView;
    }

    /**
     * Set up distance range to the one saved in preferences by user
     */
    @Override
    public void onSetUpDistanceRangeSeekBar() {
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(mContext);
        int savedDistanceRange = sp.getInt(Constant.PreferenceKey.DISTANCE_RANGE, Constant.Setting.DEFAULT_DISTANCE_RANGE);
        mPublishToView.setDistanceRangeProgress(savedDistanceRange);
    }

    /**
     * Called when distance range on seek bar changed to update selected distance range
     *
     * @param progress is the selected distance
     */
    @Override
    public void onDistanceRangeChanged(int progress) {
        int roundedDistanceRange = roundUpDistanceRange(progress);
        mPublishToView.setDistanceRangeProgress(roundedDistanceRange);
    }

    /**
     * Round up the selected distance range
     *
     * @param progress is the selected distance range
     * @return rounded distance range
     */
    private int roundUpDistanceRange(int progress) {
        double dividedNumber = progress / 100;
        int roundedNumber = (int) Math.round(dividedNumber);
        return roundedNumber * 100;
    }

    /**
     * Save the selected distance range to the preferences
     *
     * @param distanceRange is the selected distance range
     */
    @Override
    public void saveSetting(int distanceRange) {
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(mContext);
        SharedPreferences.Editor editor = sp.edit();
        editor.putInt(Constant.PreferenceKey.DISTANCE_RANGE, distanceRange);
        editor.commit();
        mPublishToView.notifySettingSaved();
    }
}
