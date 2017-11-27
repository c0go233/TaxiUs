package com.mad.taxius.database.journeychatrepository;

import com.mad.taxius.model.ChatMessage;

import java.util.ArrayList;

/**
 * Interface classes that contain methods and callback methods
 * for the chatting service operation
 */
public interface JourneyChatRepositoryContract {

    interface JourneyChat {
        void createChatRoom(String journeyChatId, MessageTransceiveCallback callback);

        void connectToExistingChatRoom(String journeyChatId, MessageTransceiveCallback callback);
    }

    interface MessageTransceiveCallback {
        void onFailToSendMessage();

        void onReceiveMessage(ChatMessage receivedMessage);

        void onFailToReceiveMessage();

        void onFailToCreateJourneyChatRoom();

        void onSucceedToSendMessage();
    }
}
