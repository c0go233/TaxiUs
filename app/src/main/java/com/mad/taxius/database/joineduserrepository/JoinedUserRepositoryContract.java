package com.mad.taxius.database.joineduserrepository;

import com.mad.taxius.model.JoinedUser;

import java.util.ArrayList;

/**
 * Contract class that contains methods' declarations for operation of joinedUser repository
 */

public interface JoinedUserRepositoryContract {

    /**
     * Callback interface for saving joined user to the database
     */
    interface SaveJoinedUserCallback {
        void onFailToSaveNewJoinedUser();

        void onSucceedToSaveNewJoinedUser(String joinedUserId);
    }

    /**
     * Callback interface for getting joined user from database
     */
    interface GetJoinedUsersCallback {
        void onSucceedToGetJoinedUsers(ArrayList<JoinedUser> joinedUsers);

        void onFailToGetJoinedUsers();
    }

    /**
     * Callback interface for removing joined user in the database
     */
    interface RemoveJoinedUserCallback {
        void onSucceedToRemoveJoinedUser();

        void onFailToRemoveJoinedUser();
    }
}
