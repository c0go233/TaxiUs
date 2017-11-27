package com.mad.taxius.login;

import android.content.Intent;

import com.mad.taxius.base.BaseAccountContract;


public interface LoginContract {

    /**
     * Interface that communicates to the Loginactivity from Loginpresenter
     */
    interface PublishToView extends BaseAccountContract {
        void showLoginFailMsg();

        void startGoogleLoginIntent(Intent intent, int requestCode);

        void showGoogleLoginFailMsg();

        void startJourneySearchActivity();
    }

    /**
     * Interface that communicates to the presenter from activity to perform
     * operations related to login functions
     */
    interface Login {
        void loginWithUserAccount(String email, String password);

        boolean isLogined();
    }

    /**
     * Interface that communicates to the presenter from activity to perform
     * operations related to google signin function
     */
    interface LoginWithGoogle {
        void loginWithGoogleAccount(LoginActivity loginActivity);

        void onGoogleLoginActivityResult(Intent data);
    }
}
