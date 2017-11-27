package com.mad.taxius.database.journeychatrepository;


import android.util.Log;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.mad.taxius.constant.Constant;
import com.mad.taxius.journeychat.JourneyChatContract;
import com.mad.taxius.model.ChatMessage;

import java.util.ArrayList;

/**
 * Repository class for journey chatting room
 */
public class JourneyChatRepository implements JourneyChatRepositoryContract.JourneyChat {

    private FirebaseDatabase mFirebaseDatabase;
    private DatabaseReference mChatRoomRootRef;
    private DatabaseReference mChatRoomRef;
    private JourneyChatRepositoryContract.MessageTransceiveCallback mCallback;

    public JourneyChatRepository() {
        this.mFirebaseDatabase = FirebaseDatabase.getInstance();
        this.mChatRoomRootRef = mFirebaseDatabase.getReference(Constant.Database.FIREBASE_TABLE_JOURNEY_CHAT);
    }

    /**
     * Create new chat room in the Firebase Database
     *
     * @param journeyChatId journey id for which chatting room is created
     * @param callback      callback interface
     */
    @Override
    public void createChatRoom(String journeyChatId, JourneyChatRepositoryContract.MessageTransceiveCallback callback) {
        mCallback = callback;
        Log.d(Constant.Debug.TAG, "Create chat room in firebase database");
        mChatRoomRootRef.child(journeyChatId).setValue(journeyChatId, new DatabaseReference.CompletionListener() {
            @Override
            public void onComplete(DatabaseError databaseError, DatabaseReference databaseReference) {
                if (databaseError != null) mCallback.onFailToCreateJourneyChatRoom();
                else {
                    mChatRoomRef = databaseReference;
                    setUpListenerForChatRoom();
                }
            }
        });
    }

    /**
     * Connect to the existing chatting room in the Firebase database based on journeyId
     *
     * @param journeyChatId journey id for which chatting room is created
     * @param callback      callback interface
     */
    @Override
    public void connectToExistingChatRoom(String journeyChatId, JourneyChatRepositoryContract.MessageTransceiveCallback callback) {
        mCallback = callback;
        this.mChatRoomRef = mChatRoomRootRef.child(journeyChatId);
        setUpListenerForChatRoom();
    }

    /**
     * Set up Firebase child event listener to receive the message sent to the chat room connected
     * The message received is sent through the callback interface to presenter
     */
    private void setUpListenerForChatRoom() {
        mChatRoomRef.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                ChatMessage chatMessage = dataSnapshot.getValue(ChatMessage.class);
                mCallback.onReceiveMessage(chatMessage);
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.w(Constant.Debug.TAG, "Failed to receive a chat message from firebase database");
                mCallback.onFailToReceiveMessage();
            }
        });
    }

    /**
     * Send the message that users enter to the chat room
     *
     * @param chatMessage user input of message for chat room
     */
    public void sendMessage(ChatMessage chatMessage) {
        DatabaseReference newMessage = mChatRoomRef.push();
        newMessage.setValue(chatMessage, new DatabaseReference.CompletionListener() {
            @Override
            public void onComplete(DatabaseError databaseError, DatabaseReference databaseReference) {
                if (databaseError != null) mCallback.onFailToSendMessage();
                else mCallback.onSucceedToSendMessage();
            }
        });
    }
}
