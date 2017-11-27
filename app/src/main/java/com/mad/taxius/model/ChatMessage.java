package com.mad.taxius.model;

import com.mad.taxius.journeychat.JourneyChatContract;
import com.mad.taxius.util.DateConverter;

import java.util.Date;

/**
 * Chat message class for the message used in chat room
 */

public class ChatMessage {

    private String mSenderName;
    private String mMessageValue;
    private String mTimestamp;

    public ChatMessage() {
    }

    public ChatMessage(String senderName, String messageValue, String timestamp) {
        this.mSenderName = senderName;
        this.mMessageValue = messageValue;
        this.mTimestamp = timestamp;
    }

    public String getSenderName() {
        return mSenderName;
    }

    public void setSenderName(String senderName) {
        this.mSenderName = senderName;
    }

    public String getMessageValue() {
        return mMessageValue;
    }

    public void setMessageValue(String messageValue) {
        this.mMessageValue = messageValue;
    }

    public String getTimestamp() {
        return mTimestamp;
    }

    public void setTimestamp(String timestamp) {
        this.mTimestamp = timestamp;
    }
}
