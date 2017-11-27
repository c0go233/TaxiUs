package com.mad.taxius.journeyadd;

import android.app.AlertDialog;
import android.app.TimePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.mad.taxius.R;
import com.mad.taxius.application.TaxiUsApplication;
import com.mad.taxius.base.BaseActivity;
import com.mad.taxius.constant.Constant;
import com.mad.taxius.journeychat.JourneyChatActivity;
import com.mad.taxius.model.ParcelablePlace;


import org.w3c.dom.Text;

import java.util.Calendar;
import java.util.Date;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Activity class for adding a journey
 */
public class JourneyAddActivity extends BaseActivity
        implements View.OnClickListener, TimePickerDialog.OnTimeSetListener, JourneyAddContract.PublishToView {

    @BindView(R.id.journey_add_activity_toolbar)
    Toolbar mToolbar;
    @BindView(R.id.journey_add_activity_departure_tv)
    TextView mDepartureTv;
    @BindView(R.id.journey_add_activity_destination_tv)
    TextView mDestinationTv;
    @BindView(R.id.journey_add_activity_departure_time_tv)
    TextView mDepartureTimeTv;
    @BindView(R.id.journey_add_activity_journey_create_btn)
    Button mCreateJourneyBtn;
    @BindView(R.id.journey_add_activity_departure_time_error_tv)
    TextView mDepartureTimeErrorTv;

    @Inject
    JourneyAddPresenter mJourneyAddPresenter;

    private TextView[] mErrorTextViews;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_journey_add);
        setUp();
    }

    /**
     * Set up basic settings of dependency injection, listeners, and initializing fields
     */
    private void setUp() {
        ButterKnife.bind(this);
        TaxiUsApplication taxiUsApplication = (TaxiUsApplication) getApplication();
        DaggerJourneyAddComponent.builder()
                .journeyAddPresenterModule(new JourneyAddPresenterModule(this, this, taxiUsApplication.getJourneyRepository()))
                .build()
                .inject(this);
        setToolbar();
        setListeners();
        passSelectedPlaces();
        mErrorTextViews = new TextView[]{mDepartureTimeErrorTv};
    }

    /**
     * Pass the selected places for journey to the presenter
     */
    private void passSelectedPlaces() {
        Intent intent = getIntent();
        ParcelablePlace departurePlace = intent.getParcelableExtra(Constant.Place.DEPARTURE_KEY);
        ParcelablePlace destinationPlace = intent.getParcelableExtra(Constant.Place.DESTINATION_KEY);
        mJourneyAddPresenter.setUpSelectedPlaces(departurePlace, destinationPlace);
    }

    /**
     * Set listeners for widgets used in this activity
     */
    private void setListeners() {
        mDepartureTimeTv.setOnClickListener(this);
        mCreateJourneyBtn.setOnClickListener(this);
    }

    /**
     * Set toolbar to have no title and navigation arrow
     */
    private void setToolbar() {
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
     * Show timepicker dialog for departure time selection
     */
    private void showTimePicker() {
        final Calendar c = Calendar.getInstance();
        BoundTimePickerDialog timePickerDialog =
                new BoundTimePickerDialog(this, this, c.get(Calendar.HOUR_OF_DAY), c.get(Calendar.MINUTE));
        timePickerDialog.show();
    }

    /**
     * Called when the time selected in the timepicker dialog
     *
     * @param view      is the timepicker dialog
     * @param hourOfDay is the selected hour
     * @param minute    is the selected minute
     */
    @Override
    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
        if (view.isShown()) mJourneyAddPresenter.onDepartureTimeSet(hourOfDay, minute);
    }

    /**
     * Display selected time to the screen
     *
     * @param time is the selected time as string
     */
    @Override
    public void displayTime(String time) {
        mDepartureTimeTv.setText(time);
    }

    /**
     * Display the selected place to the screen
     *
     * @param departurePlace   is the name of the selected departure place
     * @param destinationPlace is the name of the selected destination place
     */
    @Override
    public void displaySelectedPlace(String departurePlace, String destinationPlace) {
        mDepartureTv.setText(departurePlace);
        mDestinationTv.setText(destinationPlace);
    }

    /**
     * Display the error message when departure time is not selected
     */
    @Override
    public void displayDepartureTimeErrorMsg() {
        mDepartureTimeErrorTv.setText(getString(R.string.journey_add_activity_departure_time_error_msg));
    }

    /**
     * Display fail message for adding a new journey
     */
    @Override
    public void displayJourneyAddFailMessage() {
        Toast.makeText(this,
                getString(R.string.journey_add_activity_add_fail_message),
                Toast.LENGTH_SHORT).show();
    }

    /**
     * Move to the journey chat room after a journey is created
     *
     * @param journeyId            is the journey id for which the chat room is created
     * @param departureTime        is the selected departure time
     * @param departurePlaceName   is the selected departure place name
     * @param destinationPlaceName is the selected desitnation place name
     */
    @Override
    public void startJourneyChatActivity(String journeyId, String departureTime,
                                         String departurePlaceName, String destinationPlaceName) {
        Intent intent = new Intent(this, JourneyChatActivity.class);
        intent.putExtra(Constant.Activity.KEY, Constant.Activity.JOURNEY_ADD);
        intent.putExtra(Constant.Journey.JOURNEY_ID_KEY, journeyId);
        intent.putExtra(Constant.Journey.JOURNEY_DEPARTURE_TIME_KEY, departureTime);
        intent.putExtra(Constant.Place.DEPARTURE_KEY, departurePlaceName);
        intent.putExtra(Constant.Place.DESTINATION_KEY, destinationPlaceName);
        startActivity(intent);
    }

    /**
     * Hide all error messages shown in the screen
     */
    @Override
    public void hideErrorMsg() {
        super.resetErrorMsgs(mErrorTextViews);
    }

    /**
     * Show progress dialog to the screen
     */
    @Override
    public void showProgressDialog() {
        super.showIndeterminateProgressDialog(JourneyAddActivity.this,
                getString(R.string.journey_add_activity_progress_msg));
    }

    /**
     * Hide progress dialog shown in the screen
     */
    @Override
    public void hideProgressDialog() {
        super.hideIndeterminateProgressDialog();
    }

    @Override
    public void showNetworkErrorMsg() {
        super.showToastMessage(getString(R.string.all_network_error_msg), this);
    }

    @Override
    public void onClick(View v) {
        int viewId = v.getId();
        switch (viewId) {
            case R.id.journey_add_activity_departure_time_tv:
                showTimePicker();
                break;
            case R.id.journey_add_activity_journey_create_btn:
                mJourneyAddPresenter.createJourney();
                break;
        }
    }
}
