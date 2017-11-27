package com.mad.taxius.signup;

import com.mad.taxius.base.BaseAccountContract;

/**
 * Created by kisungTae on 23/09/2017.
 */

public interface SignUpContract extends BaseAccountContract {

    /**
     * Interface that communicates to the Signupactivity from Signuppresenter
     */
    interface PublishToView extends BaseAccountContract {
        void showNameErrorMsg();

        void showSignUpFailMsg();

        void startJourneySearchActivity();
    }

    /**
     * Interface that communicates to the presenter from activity to perform
     * operations related to signup functions
     */
    interface Login {
        void signUp(String name, String email, String password);
    }
}
