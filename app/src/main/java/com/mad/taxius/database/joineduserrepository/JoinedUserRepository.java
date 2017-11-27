package com.mad.taxius.database.joineduserrepository;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import com.mad.taxius.constant.Constant;
import com.mad.taxius.model.JoinedUser;
import com.mad.taxius.model.Journey;
import com.microsoft.windowsazure.mobileservices.MobileServiceClient;
import com.microsoft.windowsazure.mobileservices.http.ServiceFilterResponse;
import com.microsoft.windowsazure.mobileservices.table.MobileServiceTable;
import com.microsoft.windowsazure.mobileservices.table.TableDeleteCallback;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

import javax.inject.Inject;

/**
 * Created by kisungTae on 19/10/2017.
 */

public class JoinedUserRepository {

    private static final String COLUMN_JOURNEY_ID = "journeyId";
    private static final String COLUMN_DELETED = "deleted";

    private MobileServiceClient mClient;
    private MobileServiceTable<JoinedUser> mJoinedUserTable;
    private Context mContext;

    @Inject
    public JoinedUserRepository(Context context, MobileServiceClient client) {
        this.mClient = client;
        this.mContext = context;
        this.mJoinedUserTable = mClient.getTable(JoinedUser.class);
    }

    /**
     * Remove the corresponding joined user data in the database
     *
     * @param joinedUserId is the id of joined user
     * @param callback     is the callback interface
     */
    public void removeJoinedUser(String joinedUserId, JoinedUserRepositoryContract.RemoveJoinedUserCallback callback) {
        new RemoveJoinedUserAsync(callback).execute(joinedUserId);
    }

    /**
     * Async task to remove the joined user data in the database
     */
    private class RemoveJoinedUserAsync extends AsyncTask<String, Void, Boolean> {

        private JoinedUserRepositoryContract.RemoveJoinedUserCallback mCallback;

        public RemoveJoinedUserAsync(JoinedUserRepositoryContract.RemoveJoinedUserCallback callback) {
            this.mCallback = callback;
        }

        @Override
        protected Boolean doInBackground(String... params) {
            try {
                //Delete the joined user object in database
                mJoinedUserTable.delete(params[0]);
                return true;
            } catch (RuntimeException e) {
                return false;
            }
        }

        @Override
        protected void onPostExecute(Boolean aBoolean) {
            if (aBoolean) mCallback.onSucceedToRemoveJoinedUser();
            else mCallback.onFailToRemoveJoinedUser();
        }
    }

    /**
     * Get joined user on a chat room from database
     *
     * @param journeyId is the chat room's journey id
     * @param callback  is the callback interface
     */
    public void getJoinedUsers(String journeyId, JoinedUserRepositoryContract.GetJoinedUsersCallback callback) {
        new GetJoinedUsersAsync(callback).execute(journeyId);
    }

    /**
     * Async task to get joined users from database
     */
    private class GetJoinedUsersAsync extends AsyncTask<String, Void, ArrayList<JoinedUser>> {

        private JoinedUserRepositoryContract.GetJoinedUsersCallback mCallback;

        public GetJoinedUsersAsync(JoinedUserRepositoryContract.GetJoinedUsersCallback callback) {
            this.mCallback = callback;
        }

        @Override
        protected ArrayList<JoinedUser> doInBackground(String... params) {
            try {
                Log.d(Constant.Debug.TAG, "Try to get joined users from Database");
                return getJoinedUserFromDatabase(params[0]);
            } catch (ExecutionException | InterruptedException e) {
                Log.e(Constant.Debug.TAG, "Error from getting joined users from Database");
                return null;
            }
        }

        @Override
        protected void onPostExecute(ArrayList<JoinedUser> joinedUsers) {
            if (joinedUsers == null) mCallback.onFailToGetJoinedUsers();
            else mCallback.onSucceedToGetJoinedUsers(joinedUsers);
        }
    }

    /**
     * Actual query of getting joined user from database
     *
     * @param journeyId is the journeyId of chatroom
     * @return a list of joined user objects
     * @throws ExecutionException
     * @throws InterruptedException
     */
    private ArrayList<JoinedUser> getJoinedUserFromDatabase(String journeyId) throws ExecutionException, InterruptedException {
        //Get joined users from database
        ArrayList<JoinedUser> users = mJoinedUserTable.where().
                field(COLUMN_JOURNEY_ID).eq(journeyId).and().
                field(COLUMN_DELETED).eq(false).execute().get();
        return users;
    }

    /**
     * Save new joined user data to the database
     *
     * @param journeyId is the id of chatroom's journey
     * @param name      is the name of joined user
     * @param email     is the email of joined user
     * @param callback  is the callback interface
     */
    public void saveNewJoinedUser(String journeyId, String name, String email,
                                  JoinedUserRepositoryContract.SaveJoinedUserCallback callback) {
        new JoinedUserAddAsync(callback).execute(new JoinedUser(name, email, journeyId));
    }

    /**
     * Async task to save new joined user to the database
     */
    private class JoinedUserAddAsync extends AsyncTask<JoinedUser, Void, String> {

        private JoinedUserRepositoryContract.SaveJoinedUserCallback mCallback;

        public JoinedUserAddAsync(JoinedUserRepositoryContract.SaveJoinedUserCallback callback) {
            this.mCallback = callback;
        }

        @Override
        protected String doInBackground(JoinedUser... params) {
            try {
                Log.d(Constant.Debug.TAG, "Try to add joined user in Database");
                return addJoinedUserInTable(params[0]);
            } catch (ExecutionException | InterruptedException e) {
                Log.e(Constant.Debug.TAG, "Error from matching adding joined user in database");
                return null;
            }
        }

        @Override
        protected void onPostExecute(String joinedUserId) {
            if (joinedUserId != null) mCallback.onSucceedToSaveNewJoinedUser(joinedUserId);
            else mCallback.onFailToSaveNewJoinedUser();
        }
    }

    /**
     * Actualy query to save the joined user to the database
     *
     * @param joinedUser is the joined user object to be saved
     * @return joined user Id
     * @throws ExecutionException
     * @throws InterruptedException
     */
    private String addJoinedUserInTable(JoinedUser joinedUser) throws ExecutionException, InterruptedException {
        //Save new joined user in the database
        JoinedUser joinedUserInDatabase = mJoinedUserTable.insert(joinedUser).get();
        if (joinedUserInDatabase == null) return null;
        else return joinedUserInDatabase.getId();
    }
}
