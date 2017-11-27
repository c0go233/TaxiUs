package com.mad.taxius.database.userrepository;

import com.mad.taxius.model.User;

/**
 * Created by kisungTae on 24/09/2017.
 */

public interface UserRepositoryContract {

    /**
     * Callback interface for signup
     */
    interface SignUpCallback {
        void onSignUpSuccess(User user);

        void onSignUpFail();
    }

    /**
     * Callback interface for login
     */
    interface LoginCallback {
        void onLoginSuccess(User user);

        void onLoginFail();
    }

}
