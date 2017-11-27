package com.mad.taxius.journeychat;

import android.content.Intent;

import java.util.Date;

/**
 * Contains interfaces used between journeyChat activity and presenter
 */
public interface JourneyChatContract {

    /**
     * Interface that communicates to the journeyChatactivity from journeyChatpresenter
     */
    interface PublishToView {
        void setChatRecyclerView(JourneyChatListAdapter adapter);

        void moveToLastElementInRecyclerView(int lastElementIndex);

        void onFailToCreateChatRoom();

        void showFailToReceiveMessageError();

        void showFailToSendMessageError();

        void showNetworkErrorMessage();

        void resetMessageEt();

        void onSetJoinedUserRecyclerView(JoinedUserListAdapter adapter);

        void setJourneyInfoTextView(String departureTime, String departurePlaceName, String destinationPlaceName);

        void showCantLoadLayout();

        void showProgressLayout();

        void showJoinedUserList();

        void finishActivity();

        void startTimerService(String departureTime);

        void stopTimerService();
    }

    /**
     * Interface that communicates to the presenter from activity to perform
     * operations related to chatting service
     */
    interface Chat {
        void connectToChatRoom(String callingActivity, String journeyId, String departureTime,
                               String departurePlaceName, String destinationPlaceName);

        void sendMessage(String messageValue);

        void setJoinedUserRecyclerView();

        void onJoinedUserLeft();
    }
}
