package com.mad.taxius.setting;

import android.os.Bundle;
import android.os.StrictMode;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.mad.taxius.R;
import com.mad.taxius.constant.Constant;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Activity class for setting function
 */
public class SettingActivity extends AppCompatActivity implements
        SettingContract.PublishToView, View.OnClickListener {

    @BindView(R.id.setting_activity_toolbar)
    Toolbar mToolbar;
    @BindView(R.id.setting_activity_range_seek_bar)
    SeekBar mRangeSeekBar;
    @BindView(R.id.setting_activity_range_tv)
    TextView mRangeTv;
    @BindView(R.id.setting_activity_save_btn)
    Button mSaveBtn;

    @Inject
    SettingPresenter mSettingPresenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);
        setUp();
    }

    /**
     * Set up basic settings of this activity
     */
    private void setUp() {
        ButterKnife.bind(this);
        DaggerSettingComponent.builder()
                .settingPresenterModule(new SettingPresenterModule(this, this))
                .build()
                .inject(this);
        setUpToolbar();
        setUpSeekBar();
        setUpListeners();
        mSettingPresenter.onSetUpDistanceRangeSeekBar();
    }

    /**
     * Set up listeners for widgets used in this activity
     */
    private void setUpListeners() {
        mSaveBtn.setOnClickListener(this);
    }

    private void setUpToolbar() {
        mToolbar.setNavigationIcon(R.drawable.ic_left_arrow);
        mToolbar.setTitle("");
        setSupportActionBar(mToolbar);
        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    /**
     * Set up listeners for the seek bar for the distance range selection
     */
    private void setUpSeekBar() {
        mRangeSeekBar.setMax(Constant.Setting.DISTANCE_RANGE_MAX);
        mRangeSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                mSettingPresenter.onDistanceRangeChanged(seekBar.getProgress());
            }
        });
    }

    /**
     * Set up the selected distance range on seek bar and EditText view
     *
     * @param progress
     */
    @Override
    public void setDistanceRangeProgress(int progress) {
        mRangeSeekBar.setProgress(progress);
        mRangeTv.setText(String.valueOf(progress));
    }

    @Override
    public void notifySettingSaved() {
        Toast.makeText(this, getString(R.string.setting_activity_saved_setting), Toast.LENGTH_LONG).show();
    }

    @Override
    public void onClick(View v) {
        int viewId = v.getId();
        switch (viewId) {
            case R.id.setting_activity_save_btn:
                mSettingPresenter.saveSetting(mRangeSeekBar.getProgress());
                break;
        }
    }


}
