package com.mad.taxius.journeysearch;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.FragmentManager;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.location.places.ui.PlaceAutocomplete;
import com.mad.taxius.R;
import com.mad.taxius.application.TaxiUsApplication;
import com.mad.taxius.base.BaseActivity;
import com.mad.taxius.constant.Constant;
import com.mad.taxius.journeyadd.JourneyAddActivity;
import com.mad.taxius.journeychat.JourneyChatActivity;
import com.mad.taxius.login.LoginActivity;
import com.mad.taxius.model.Journey;
import com.mad.taxius.model.ParcelablePlace;
import com.mad.taxius.setting.SettingActivity;
import com.mad.taxius.util.DateConverter;

import java.util.ArrayList;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Activity class for searching journeys
 */
public class JourneySearchActivity extends BaseActivity
        implements NavigationView.OnNavigationItemSelectedListener, View.OnFocusChangeListener,
        JourneySearchContract.PublishToView, View.OnClickListener {

    @BindView(R.id.journey_search_activity_toolbar)
    Toolbar mToolbar;
    @BindView(R.id.journey_search_activity_drawer_layout)
    DrawerLayout mDrawerLayout;
    @BindView(R.id.journey_search_activity_navigation_view)
    NavigationView mNavigationView;
    @BindView(R.id.journey_search_activity_departure_et)
    EditText mDepartureEt;
    @BindView(R.id.journey_search_activity_destination_et)
    EditText mDestinationEt;
    @BindView(R.id.journey_search_activity_floating_action_btn)
    FloatingActionButton mAddFab;
    @BindView(R.id.journey_search_activity_location_input_linear_layout)
    LinearLayout mLocationInputLinearLayout;
    @BindView(R.id.journey_search_activity_recycler_view_linear_layout)
    LinearLayout mRecyclerViewLinearLayout;
    @BindView(R.id.journey_search_activity_recycler_view)
    RecyclerView mRecyclerView;
    @BindView(R.id.journey_search_activity_no_result_linear_layout)
    LinearLayout mNoResultLinearLayout;
    @BindView(R.id.journey_search_activity_retry_btn)
    Button mRetryButton;

    private TextView mNavigationNameTv;
    private TextView mNavigationEmailTv;
    private ActionBarDrawerToggle toggle;
    private boolean mIsSearchingMode = true;

    @Inject
    JourneySearchPresenter mJourneySearchPresenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_journey_search);
        setUp();
    }

    /**
     * Set up basic settings
     */
    private void setUp() {
        ButterKnife.bind(this);
        TaxiUsApplication taxiUsApplication = (TaxiUsApplication) getApplication();
        DaggerJourneySearchComponent.builder()
                .journeySearchPresenterModule(new JourneySearchPresenterModule(this, this, taxiUsApplication.getJourneyRepository()))
                .build()
                .inject(this);
        setUpToolbar();
        setUpToggle();
        setUpListeners();
        registerAccountViewsInNavigationView();
        mJourneySearchPresenter.setUserProfile();
        mJourneySearchPresenter.checkGooglePlayServiceAvailable();
    }

    /**
     * Register the views related account in the navigation view
     */
    private void registerAccountViewsInNavigationView() {
        View headerView = mNavigationView.getHeaderView(0);
        mNavigationNameTv = (TextView) headerView.findViewById(R.id.nav_header_user_name_tv);
        mNavigationEmailTv = (TextView) headerView.findViewById(R.id.nav_header_user_email_tv);
    }

    /**
     * Set up toolbar without title
     */
    private void setUpToolbar() {
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
    }

    /**
     * Set up and sync toggle button
     */
    private void setUpToggle() {
        toggle = new ActionBarDrawerToggle(this, mDrawerLayout, mToolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        mDrawerLayout.addDrawerListener(toggle);
        toggle.syncState();
    }

    /**
     * Set up listeners of widgets used in the activity
     */
    private void setUpListeners() {
        mNavigationView.setNavigationItemSelectedListener(this);
        mDepartureEt.setOnFocusChangeListener(this);
        mDestinationEt.setOnFocusChangeListener(this);
        mAddFab.setOnClickListener(this);
        mRetryButton.setOnClickListener(this);
    }

    /**
     * Add google map fragment to the activity
     */
    private void addGoogleMap() {
        JourneySearchMapFragment journeySearchMapFragment = new JourneySearchMapFragment();
        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction().replace(R.id.journey_search_activity_map_linear_layout,
                journeySearchMapFragment).commit();
    }

    /**
     * Called when back button is pressed and close the drawer
     * or cancel journey search mode
     */
    @Override
    public void onBackPressed() {
        if (mDrawerLayout.isDrawerOpen(GravityCompat.START)) {
            mDrawerLayout.closeDrawer(GravityCompat.START);
        } else if (!mIsSearchingMode) {
            mJourneySearchPresenter.onCancelJourneySearchedMode();
        } else {

            super.onBackPressed();
        }
    }

    /**
     * Called when focus of Edittext changes to start autocomplete address activity
     *
     * @param v        is the view that has changed focus
     * @param hasFocus is whether focused or not
     */
    @Override
    public void onFocusChange(View v, boolean hasFocus) {
        if (hasFocus) {
            int viewId = v.getId();
            switch (viewId) {
                case R.id.journey_search_activity_departure_et:
                    startAutoCompleteAddressIntent(v, Constant.Place.DEPARTURE_AUTO_ADDRESS_REQUEST_CODE);
                    break;
                case R.id.journey_search_activity_destination_et:
                    startAutoCompleteAddressIntent(v, Constant.Place.DESTINATION_AUTO_ADDRESS_REQUEST_CODE);
                    break;
            }
        }
    }

    /**
     * Start auto complete address activity
     *
     * @param view        is the view focused
     * @param requestCode is the request code of autocomplete address activity
     */
    private void startAutoCompleteAddressIntent(View view, int requestCode) {
        view.clearFocus();
        try {
            Intent intent = new PlaceAutocomplete.IntentBuilder(PlaceAutocomplete.MODE_FULLSCREEN).build(this);
            startActivityForResult(intent, requestCode);
        } catch (GooglePlayServicesRepairableException | GooglePlayServicesNotAvailableException e) {
            this.onGooglePlayServiceUnavailable();
        }
    }

    /**
     * Called when the autocomplete address activity finishes
     *
     * @param requestCode is the request code of the activity finished
     * @param resultCode  is whether the activity succeeds or not
     * @param data        selected place data from the activity
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK && (requestCode == Constant.Place.DEPARTURE_AUTO_ADDRESS_REQUEST_CODE
                || requestCode == Constant.Place.DESTINATION_AUTO_ADDRESS_REQUEST_CODE)) {
            mJourneySearchPresenter.onAutoCompleteAddressActivityResult(data, requestCode);
        }
    }

    /**
     * Start journey add activity to add new journey
     *
     * @param departurePlace   is the selected departure place
     * @param destinationPlace is the destination place
     */
    @Override
    public void startJourneyAddActivity(ParcelablePlace departurePlace, ParcelablePlace destinationPlace) {
        Intent intent = new Intent(this, JourneyAddActivity.class);
        intent.putExtra(Constant.Place.DEPARTURE_KEY, departurePlace);
        intent.putExtra(Constant.Place.DESTINATION_KEY, destinationPlace);
        startActivity(intent);
    }

    /**
     * Show the journey recycler view
     */
    @Override
    public void showJourneyRecyclerView() {
        mNoResultLinearLayout.setVisibility(View.GONE);
        mRecyclerViewLinearLayout.setVisibility(View.VISIBLE);
    }

    /**
     * Set the journey recycler view with adapter created
     *
     * @param journeyAdapter is the adapter set to the recycler view
     */
    @Override
    public void setJourneyRecyclerView(JourneyAdapter journeyAdapter) {
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
        mRecyclerView.setLayoutManager(layoutManager);
        mRecyclerView.setAdapter(journeyAdapter);
    }

    /**
     * Display the view saying no result for searched journeys
     */
    @Override
    public void displayNoJourneyResult() {
        mRecyclerViewLinearLayout.setVisibility(View.GONE);
        mNoResultLinearLayout.setVisibility(View.VISIBLE);
    }

    /**
     * Start the journey chat activity to go to chat room
     *
     * @param journeyId            is the journey id for which chat room is created
     * @param departureTime        is the departure time of the journey
     * @param departurePlaceName   is the departure place of the journey
     * @param destinationPlaceName is the destination place of the journey
     */
    @Override
    public void startJourneyChatActivity(String journeyId, String departureTime,
                                         String departurePlaceName, String destinationPlaceName) {
        Intent intent = new Intent(this, JourneyChatActivity.class);
        intent.putExtra(Constant.Activity.KEY, Constant.Activity.JOURNEY_SEARCH);
        intent.putExtra(Constant.Journey.JOURNEY_ID_KEY, journeyId);
        intent.putExtra(Constant.Journey.JOURNEY_DEPARTURE_TIME_KEY, departureTime);
        intent.putExtra(Constant.Place.DEPARTURE_KEY, departurePlaceName);
        intent.putExtra(Constant.Place.DESTINATION_KEY, destinationPlaceName);
        startActivity(intent);
    }

    /**
     * Change the activity to journey searched mode
     */
    @Override
    public void changeToJourneySearchedMode() {
        this.mIsSearchingMode = false;
        toggle.setDrawerIndicatorEnabled(false);
        mToolbar.setNavigationIcon(R.drawable.ic_left_arrow);
        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mJourneySearchPresenter.onCancelJourneySearchedMode();
            }
        });
        mLocationInputLinearLayout.setVisibility(View.GONE);
    }

    /**
     * Change the activity to journey searching mode
     */
    @Override
    public void changeToJourneySearchingMode() {
        this.mIsSearchingMode = true;
        mRecyclerViewLinearLayout.setVisibility(View.GONE);
        mNoResultLinearLayout.setVisibility(View.GONE);
        mLocationInputLinearLayout.setVisibility(View.VISIBLE);
        setUpToggle();
    }

    /**
     * Called when the availability of google service is checked
     */
    @Override
    public void onGooglePlayServiceAvailable() {
        addGoogleMap();
    }

    /**
     * Set the text of departure edittext to selected place's name
     *
     * @param departure is the name of selected place
     */
    @Override
    public void setDepartureEt(String departure) {
        mDepartureEt.setText(departure);
    }

    /**
     * Set the text of destination edittext to selected place's name
     *
     * @param destination is the name of selected place
     */
    @Override
    public void setDestinationEt(String destination) {
        mDestinationEt.setText(destination);
    }

    /**
     * Called when google play service is not available to add google map
     */
    @Override
    public void onGooglePlayServiceUnavailable() {
        Toast.makeText(this, getString(R.string.journey_search_activity_no_google_service), Toast.LENGTH_LONG).show();
    }

    /**
     * Called when navigation item in drawer selected
     *
     * @param item is the item clicked
     * @return boolean
     */
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        int itemId = item.getItemId();
        switch (itemId) {
            case R.id.nav_menu_setting:
                startActivity(new Intent(this, SettingActivity.class));
                break;
            case R.id.nav_menu_sign_out:
                mJourneySearchPresenter.signOut();
                break;
        }
        mDrawerLayout.closeDrawer(GravityCompat.START);
        return true;
    }

    /**
     * Show progress dialog
     */
    @Override
    public void showProgressDialog() {
        super.showIndeterminateProgressDialog(this, getString(R.string.journey_search_activity_journey_search_progress_msg));
    }

    /**
     * Hide progress dialog shown in the screen
     */
    @Override
    public void hideProgressDialog() {
        super.hideIndeterminateProgressDialog();
    }

    /**
     * Set up user profile in the drawer
     *
     * @param email is the email of logged in user
     * @param name  is the name of logged in user
     */
    @Override
    public void onSetUserProfile(String email, String name) {
        mNavigationEmailTv.setText(email);
        mNavigationNameTv.setText(name);
    }

    @Override
    public void showNetworkErrorMsg() {
        super.showToastMessage(getString(R.string.all_network_error_msg), this);
    }


    @Override
    public void showPlaceNotSelectedErrorMsg() {
        super.showToastMessage(getString(R.string.journey_search_activity_select_place_msg), this);
    }

    @Override
    public void showFailToGetJourneyErrorMsg() {
        super.showToastMessage(getString(R.string.journey_search_activity_fail_to_get_journey), this);
    }

    @Override
    public void startLoginActivity() {
        startActivity(new Intent(this, LoginActivity.class));
    }

    @Override
    public void onClick(View v) {
        int viewId = v.getId();
        switch (viewId) {
            case R.id.journey_search_activity_floating_action_btn:
                mJourneySearchPresenter.onAddFabClicked();
                break;
            case R.id.journey_search_activity_retry_btn:
                mJourneySearchPresenter.findMatchedJourney();
                break;
        }
    }

    @Override
    public void finishActivity() {
        this.finish();
    }

    @Override
    public void hideErrorMsg() {
    }

}
