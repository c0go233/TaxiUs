package com.mad.taxius.journeychat;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.mad.taxius.R;
import com.mad.taxius.application.TaxiUsApplication;
import com.mad.taxius.base.BaseActivity;
import com.mad.taxius.constant.Constant;
import com.mad.taxius.timerservice.TimerService;


import java.util.Date;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Activity class for journey chat room
 */
public class JourneyChatActivity extends BaseActivity implements
        JourneyChatContract.PublishToView, View.OnClickListener {

    @BindView(R.id.journey_chat_activity_recycler_view)
    RecyclerView mChatRecyclerView;
    @BindView(R.id.journey_chat_activity_chat_message_et)
    EditText mChatMessageEt;
    @BindView(R.id.journey_chat_activity_send_btn)
    Button mSendBtn;
    @BindView(R.id.journey_chat_activity_navigation_view)
    NavigationView mNavigationView;
    @BindView(R.id.journey_chat_activity_toolbar)
    Toolbar mToolbar;
    @BindView(R.id.journey_chat_activity_drawer_layout)
    DrawerLayout mDrawerLayout;
    @BindView(R.id.journey_chat_nav_departure_time_tv)
    TextView mDepartureTimeTv;
    @BindView(R.id.journey_chat_nav_departure_place_tv)
    TextView mDeparturePlaceTv;
    @BindView(R.id.journey_chat_nav_destination_place_tv)
    TextView mDestinationPlaceTv;
    @BindView(R.id.journey_chat_nav_exit_btn)
    Button mExitBtn;
    @BindView(R.id.journey_chat_nav_joined_user_recycler_view)
    RecyclerView mJoinedUserRecyclerView;
    @BindView(R.id.journey_chat_nav_cant_load_linear_layout)
    LinearLayout mCantLoadLayout;
    @BindView(R.id.journey_chat_nav_refresh_btn)
    Button mRefreshBtn;
    @BindView(R.id.journey_chat_nav_progress_linear_layout)
    LinearLayout mProgreeLayout;

    private Intent mTimerServiceIntent;

    @Inject
    JourneyChatPresenter mJourneyChatPresenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_journey_chat);
        setUp();
        connectToChatRoom();
    }

    /**
     * Connect to the chat room created based on the selected journey Id
     */
    private void connectToChatRoom() {
        Intent intent = getIntent();
        mJourneyChatPresenter.connectToChatRoom(intent.getStringExtra(Constant.Activity.KEY),
                intent.getStringExtra(Constant.Journey.JOURNEY_ID_KEY),
                intent.getStringExtra(Constant.Journey.JOURNEY_DEPARTURE_TIME_KEY),
                intent.getStringExtra(Constant.Place.DEPARTURE_KEY),
                intent.getStringExtra(Constant.Place.DESTINATION_KEY));
    }

    /**
     * Set the recyclerview for messages shown in the chat room
     *
     * @param adapter is the adapter for the recyclerview of chat message
     */
    @Override
    public void setChatRecyclerView(JourneyChatListAdapter adapter) {
        setLayoutManagerForRecyclerView(mChatRecyclerView);
        mChatRecyclerView.setAdapter(adapter);
    }

    /**
     * Set the joined user recycler view with adapter
     *
     * @param adapter is the adapter to be set with recyclerview
     */
    @Override
    public void onSetJoinedUserRecyclerView(JoinedUserListAdapter adapter) {
        setLayoutManagerForRecyclerView(mJoinedUserRecyclerView);
        mJoinedUserRecyclerView.setAdapter(adapter);
    }

    /**
     * Show the can't load layout to the drawer when network error occured
     */
    @Override
    public void showCantLoadLayout() {
        mJoinedUserRecyclerView.setVisibility(View.GONE);
        mProgreeLayout.setVisibility(View.GONE);
        mCantLoadLayout.setVisibility(View.VISIBLE);
    }

    /**
     * Show progress layout and hide recycler view and can't load layout
     */
    @Override
    public void showProgressLayout() {
        mJoinedUserRecyclerView.setVisibility(View.GONE);
        mCantLoadLayout.setVisibility(View.GONE);
        mProgreeLayout.setVisibility(View.VISIBLE);
    }

    /**
     * Show the joined user recycler view in the drawer and
     * hide progresslayout and can't load page if present
     */
    @Override
    public void showJoinedUserList() {
        mJoinedUserRecyclerView.setVisibility(View.VISIBLE);
        mProgreeLayout.setVisibility(View.GONE);
        mCantLoadLayout.setVisibility(View.GONE);
    }

    @Override
    public void finishActivity() {
        this.finish();
    }

    /**
     * Start the timer service on the background
     * @param departureTime is the departure time to count
     */
    @Override
    public void startTimerService(String departureTime) {
        mTimerServiceIntent = new Intent(this, TimerService.class);
        mTimerServiceIntent.putExtra(Constant.TimerService.TIME_KEY, departureTime);
        startService(mTimerServiceIntent);
    }

    @Override
    public void stopTimerService() {
        Log.d("Debuga", "stopTimerService");
        stopService(mTimerServiceIntent);
    }

    /**
     * Set layoutmanager for recycler view
     *
     * @param recyclerView sets its layoutmanager
     */
    private void setLayoutManagerForRecyclerView(RecyclerView recyclerView) {
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(layoutManager);
    }

    private void showToastMsg(String msg) {
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onClick(View v) {
        int viewId = v.getId();
        switch (viewId) {
            case R.id.journey_chat_activity_send_btn:
                mJourneyChatPresenter.sendMessage(mChatMessageEt.getText().toString());
                break;
            case R.id.journey_chat_nav_exit_btn:
                mJourneyChatPresenter.onJoinedUserLeft();
                break;
            case R.id.journey_chat_nav_refresh_btn:
                mJourneyChatPresenter.setJoinedUserRecyclerView();
                break;
        }
    }

    /**
     * Set up basic settings of dependency injection, setting toolbar, and listeners
     */
    private void setUp() {
        ButterKnife.bind(this);
        TaxiUsApplication taxiUsApplication = (TaxiUsApplication) getApplication();
        DaggerJourneyChatComponent.builder()
                .journeyChatPresenterModule(new JourneyChatPresenterModule(this, this, taxiUsApplication.getJoinedUserRepository()))
                .build()
                .inject(this);
        setUpToolBar();
        setUpToggle();
        setUpListeners();
    }

    private void setUpListeners() {
        mSendBtn.setOnClickListener(this);
        mExitBtn.setOnClickListener(this);
        mRefreshBtn.setOnClickListener(this);
    }

    private void setUpToolBar() {
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
    }

    private void setUpToggle() {
        ActionBarDrawerToggle toggle = getDrawerToggle();
        mDrawerLayout.addDrawerListener(toggle);
        toggle.syncState();
    }

    /**
     * Set up journey info in the drawer
     *
     * @param departureTime        is the departure time of journey
     * @param departurePlaceName   is the departure place of journey
     * @param destinationPlaceName is the destination place of journey
     */
    @Override
    public void setJourneyInfoTextView(String departureTime, String departurePlaceName, String destinationPlaceName) {
        mDepartureTimeTv.setText(departureTime);
        mDeparturePlaceTv.setText(departurePlaceName);
        mDestinationPlaceTv.setText(destinationPlaceName);
    }

    /**
     * Move to the focus of the recyclerview of chat message to the last message
     *
     * @param lastElementIndex
     */
    @Override
    public void moveToLastElementInRecyclerView(int lastElementIndex) {
        mChatRecyclerView.smoothScrollToPosition(lastElementIndex);
    }

    /**
     * Display error message for failing to create new journey chat room
     */
    @Override
    public void onFailToCreateChatRoom() {
        showToastMsg(getString(R.string.journey_chat_activity_fail_to_create_chat_room_message));
        finish();
    }

    /**
     * Display error message for receiving chat message from database
     */
    @Override
    public void showFailToReceiveMessageError() {
        showToastMsg(getString(R.string.journey_chat_activity_fail_to_receive_message_error_message));
    }

    /**
     * Show error message for failing to send a message to the database
     */
    @Override
    public void showFailToSendMessageError() {
        showToastMsg(getString(R.string.journey_chat_activity_fail_to_send_message_error_message));
    }

    @Override
    public void showNetworkErrorMessage() {
        super.showToastMessage(getString(R.string.all_network_error_msg), this);
    }

    @Override
    public void resetMessageEt() {
        mChatMessageEt.setText("");
    }

    /**
     * Set and return drawer toggle with listener for joined user list
     *
     * @return drawer toggle
     */
    private ActionBarDrawerToggle getDrawerToggle() {
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this,
                mDrawerLayout, mToolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close) {
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
                mJourneyChatPresenter.setJoinedUserRecyclerView();
            }
        };
        return toggle;
    }

    @Override
    public void onBackPressed() {
        if (mDrawerLayout.isDrawerOpen(GravityCompat.START)) {
            mDrawerLayout.closeDrawer(GravityCompat.START);
        } else {
            mJourneyChatPresenter.onJoinedUserLeft();
        }
    }

}
