package com.mad.taxius.timerservice;

import android.app.KeyguardManager;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.graphics.PixelFormat;
import android.os.CountDownTimer;
import android.os.IBinder;
import android.os.PowerManager;
import android.support.annotation.IntDef;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Toast;

import com.mad.taxius.R;
import com.mad.taxius.constant.Constant;
import com.mad.taxius.util.DateConverter;

/**
 * Timer service contains the methods to count the departure time and
 * remind users
 */

public class TimerService extends Service {

    private CountDownTimer mCountDownTimer;

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.d(Constant.Debug.TAG, "Timer service starts");
        long count = DateConverter.getTimeToCount(intent.getStringExtra(Constant.TimerService.TIME_KEY));
        if (count != -1) startCount(count);
        return super.onStartCommand(intent, flags, startId);
    }

    /**
     * Start the count for the journey's departure time
     * @param count is the seconds to count
     */
    private void startCount(long count) {
        mCountDownTimer = new CountDownTimer(count, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
            }

            @Override
            public void onFinish() {
                KeyguardManager myKM = (KeyguardManager) getBaseContext().getSystemService(Context.KEYGUARD_SERVICE);
                if (myKM.inKeyguardRestrictedInputMode()) showDialog();
                else
                    Toast.makeText(getBaseContext(), getBaseContext().getString(R.string.all_its_time_for_journey),
                            Toast.LENGTH_LONG).show();
            }
        };
        mCountDownTimer.start();
    }

    /**
     * Show dialog to the locked screen
     */
    private void showDialog() {
        WindowManager mWindowManager = (WindowManager) getSystemService(WINDOW_SERVICE);
        LayoutInflater layoutInflater = (LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE);
        View mView = layoutInflater.inflate(R.layout.dialog_timer, null);

        WindowManager.LayoutParams mLayoutParams = new WindowManager.LayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT, 0, 0,
                WindowManager.LayoutParams.TYPE_SYSTEM_OVERLAY,
                WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED
                        | WindowManager.LayoutParams.FLAG_DISMISS_KEYGUARD
                        | WindowManager.LayoutParams.FLAG_TURN_SCREEN_ON,
                PixelFormat.RGBA_8888);

        mWindowManager.addView(mView, mLayoutParams);
    }

    @Override
    public void onDestroy() {
        mCountDownTimer.cancel();
        super.onDestroy();
    }

    @Override
    public void onCreate() {
        super.onCreate();
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}
