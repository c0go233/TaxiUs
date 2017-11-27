package com.mad.taxius.journeychat;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Network;
import android.preference.PreferenceManager;
import android.util.Log;

import com.mad.taxius.constant.Constant;
import com.mad.taxius.database.joineduserrepository.JoinedUserRepository;
import com.mad.taxius.database.joineduserrepository.JoinedUserRepositoryContract;
import com.mad.taxius.database.journeychatrepository.JourneyChatRepository;
import com.mad.taxius.database.journeychatrepository.JourneyChatRepositoryContract;
import com.mad.taxius.model.ChatMessage;
import com.mad.taxius.model.JoinedUser;
import com.mad.taxius.util.DateConverter;
import com.mad.taxius.util.NetworkChecker;

import java.util.ArrayList;
import java.util.Date;

import javax.inject.Inject;

/**
 * Presenter class for journey chat room to perform chatting functions
 */

public class JourneyChatPresenter implements JourneyChatContract.Chat,
        JourneyChatRepositoryContract.MessageTransceiveCallback, JoinedUserRepositoryContract.SaveJoinedUserCallback,
        JoinedUserRepositoryContract.GetJoinedUsersCallback, JoinedUserRepositoryContract.RemoveJoinedUserCallback {

    private Context mContext;
    private JourneyChatContract.PublishToView mPublishToView;
    private JourneyChatRepository mJourneyChatRepository;
    private JoinedUserRepository mJoinedUserRepository;
    private ArrayList<ChatMessage> mChatMessages;
    private ArrayList<JoinedUser> mJoinedUsers;
    private JourneyChatListAdapter mJourneyChatListAdapter;
    private JoinedUserListAdapter mJoinedUserListAdapter;
    private String mJourneyId;
    private String mLoginedUserEmail;
    private String mLoginedUserName;
    private String mCallingActivity;
    private String mJoinedUserId;

    @Inject
    public JourneyChatPresenter(Context context, JourneyChatContract.PublishToView publishToView,
                                JourneyChatRepository journeyChatRepository, JoinedUserRepository joinedUserRepository) {
        this.mContext = context;
        this.mPublishToView = publishToView;
        this.mJourneyChatRepository = journeyChatRepository;
        this.mJoinedUserRepository = joinedUserRepository;
        mChatMessages = new ArrayList<>();
        mJoinedUsers = new ArrayList<>();
    }

    /**
     * Set logged-in user info
     */
    private void setLoginedUserInfo() {
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(mContext);
        this.mLoginedUserName = sp.getString(Constant.PreferenceKey.NAME, "");
        this.mLoginedUserEmail = sp.getString(Constant.PreferenceKey.LOGINED_EMAIL, "");
    }

    /**
     * Set up before connecting to the chatroom
     *
     * @param callingActivity      is the activity calling this method
     * @param journeyId            is the journey id for which chat room is created
     * @param departureTime        is the departure time of journey
     * @param departurePlaceName   is the name of departure place of the journey
     * @param destinationPlaceName is the name of destination place of the journey
     */
    @Override
    public void connectToChatRoom(String callingActivity, String journeyId,
                                  String departureTime, String departurePlaceName, String destinationPlaceName) {
        setLoginedUserInfo();
        setChatListAdapter();
        mPublishToView.setJourneyInfoTextView(departureTime, departurePlaceName, destinationPlaceName);
        this.mJourneyId = journeyId;
        this.mCallingActivity = callingActivity;
        mPublishToView.startTimerService(departureTime);
        logJoinedUser();
    }

    /**
     * Initialize the adapter for chat message list view
     */
    private void setChatListAdapter() {
        mJourneyChatListAdapter = new JourneyChatListAdapter(mContext, mChatMessages);
        mPublishToView.setChatRecyclerView(mJourneyChatListAdapter);
    }

    /**
     * Save new joined user to the database for this journey chat room
     */
    private void logJoinedUser() {
        if (!NetworkChecker.isNetworkAvailable(mContext)) {
            onFailToCreateJourneyChatRoom();
        } else {
            mJoinedUserRepository.saveNewJoinedUser(this.mJourneyId, this.mLoginedUserName,
                    this.mLoginedUserEmail, this);
        }
    }

    @Override
    public void onFailToSaveNewJoinedUser() {
        onFailToCreateJourneyChatRoom();
    }

    /**
     * Called when joined user is added to the database and connect to chatroom
     *
     * @param joinedUserId
     */
    @Override
    public void onSucceedToSaveNewJoinedUser(String joinedUserId) {
        this.mJoinedUserId = joinedUserId;
        connectToChatRoomInDatabase(mCallingActivity, mJourneyId);
    }

    /**
     * Connect to the chat room created in the database
     *
     * @param callingActivity is the calling activity
     * @param journeyId       is the journey id of the chat room
     */
    private void connectToChatRoomInDatabase(String callingActivity, String journeyId) {
        if (callingActivity.equals(Constant.Activity.JOURNEY_ADD)) {
            mJourneyChatRepository.createChatRoom(journeyId, this);
        } else if (callingActivity.equals(Constant.Activity.JOURNEY_SEARCH)) {
            mJourneyChatRepository.connectToExistingChatRoom(journeyId, this);
        }
    }

    /**
     * Send a message entered to the database
     *
     * @param messageValue is the value of message entered
     */
    @Override
    public void sendMessage(String messageValue) {
        if (!NetworkChecker.isNetworkAvailable(mContext)) {
            mPublishToView.showNetworkErrorMessage();
        } else if (messageValue.length() > 0) {
            ChatMessage chatMessage = new ChatMessage(mLoginedUserName,
                    messageValue, DateConverter.getTimePortionAsStringFrom(new Date()));
            mJourneyChatRepository.sendMessage(chatMessage);
        }
    }

    /**
     * Initialize the adapter for joined user list view
     */
    @Override
    public void setJoinedUserRecyclerView() {
        if (mJoinedUserListAdapter == null) {
            mJoinedUserListAdapter = new JoinedUserListAdapter(mContext, mJoinedUsers);
            mPublishToView.onSetJoinedUserRecyclerView(mJoinedUserListAdapter);
        }

        if (!NetworkChecker.isNetworkAvailable(mContext)) {
            mPublishToView.showCantLoadLayout();
        } else {
            mPublishToView.showProgressLayout();
            mJoinedUserRepository.getJoinedUsers(mJourneyId, this);
        }
    }

    /**
     * Called when users back button or exit button in the chatroom to leave
     * and it removes joined user in the database
     */
    @Override
    public void onJoinedUserLeft() {
        if (!NetworkChecker.isNetworkAvailable(mContext)) {
            mPublishToView.showNetworkErrorMessage();
        } else if (mJoinedUserId != null) {
            mJoinedUserRepository.removeJoinedUser(mJoinedUserId, this);
        }
    }

    /**
     * Receive the message sent to the database
     *
     * @param receivedMessage is the value of message sent to the chat room
     */
    @Override
    public void onReceiveMessage(ChatMessage receivedMessage) {
        mChatMessages.add(receivedMessage);
        int lastIndex = (mChatMessages.size() - 1);
        mJourneyChatListAdapter.notifyItemInserted(lastIndex);
        mPublishToView.moveToLastElementInRecyclerView(lastIndex);
    }

    /**
     * Show error message for failing to receive a message from chatroom
     */
    @Override
    public void onFailToReceiveMessage() {
        mPublishToView.showFailToReceiveMessageError();
    }

    /**
     * Show error message for failing to create journey chat room in the database
     */
    @Override
    public void onFailToCreateJourneyChatRoom() {
        mPublishToView.onFailToCreateChatRoom();
    }

    /**
     * Reset the message edit text on success to send a message
     */
    @Override
    public void onSucceedToSendMessage() {
        mPublishToView.resetMessageEt();
    }

    /**
     * Show error message for failing to send a message to the chat room in the database
     */
    @Override
    public void onFailToSendMessage() {
        mPublishToView.showFailToSendMessageError();
    }

    /**
     * Called when get joined users from database to diplay them in the drawer
     *
     * @param joinedUsers is a list of retrieved joined user objects
     */
    @Override
    public void onSucceedToGetJoinedUsers(ArrayList<JoinedUser> joinedUsers) {
        mPublishToView.showJoinedUserList();
        mJoinedUsers.clear();
        mJoinedUsers.addAll(joinedUsers);
        mJoinedUserListAdapter.notifyDataSetChanged();
    }

    @Override
    public void onFailToGetJoinedUsers() {
        mPublishToView.showCantLoadLayout();
    }

    @Override
    public void onSucceedToRemoveJoinedUser() {
        mPublishToView.stopTimerService();
        mPublishToView.finishActivity();
    }

    @Override
    public void onFailToRemoveJoinedUser() {
        mPublishToView.showNetworkErrorMessage();
    }
}
