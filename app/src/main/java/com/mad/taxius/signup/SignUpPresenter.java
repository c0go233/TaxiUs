package com.mad.taxius.signup;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Network;
import android.preference.PreferenceManager;
import android.util.Log;

import com.mad.taxius.base.BaseAccountPresenter;
import com.mad.taxius.constant.Constant;
import com.mad.taxius.database.userrepository.UserRepository;
import com.mad.taxius.database.userrepository.UserRepositoryContract;
import com.mad.taxius.journeysearch.JourneySearchActivity;
import com.mad.taxius.model.User;
import com.mad.taxius.util.NetworkChecker;
import com.mad.taxius.util.UserDetailValidator;

import javax.inject.Inject;

/**
 * Presenter class for the Signup activity to perform signup function
 */

public class SignUpPresenter extends BaseAccountPresenter implements SignUpContract.Login,
        UserRepositoryContract.SignUpCallback {

    private final SignUpContract.PublishToView mPublishToViewInteractor;
    private final UserRepository mUserRepository;
    private final Context mContext;

    @Inject
    public SignUpPresenter(SignUpContract.PublishToView publishToView,
                           UserRepository userRepository, Context context) {
        this.mPublishToViewInteractor = publishToView;
        this.mUserRepository = userRepository;
        this.mContext = context;
    }

    /**
     * Signup with user inputs by calling registerNewUser function of user repository
     *
     * @param name     is the user input of name
     * @param email    is the user input of email
     * @param password is the user input of password
     */
    @Override
    public void signUp(String name, String email, String password) {
        mPublishToViewInteractor.hideErrorMsg();
        boolean isValid = super.validateSignUpDetails(name, email, password, mPublishToViewInteractor);
        if (isValid) {
            if (!NetworkChecker.isNetworkAvailable(mContext)) {
                mPublishToViewInteractor.showNetworkErrorMsg();
            } else {
                mPublishToViewInteractor.showProgressDialog();
                mUserRepository.registerNewUser(new User(name, email, password, false), this);
            }
        }
    }

    /**
     * Called when signup process is successful
     *
     * @param user is the newly saved user object
     */
    @Override
    public void onSignUpSuccess(User user) {
        super.saveLoginedState(user, true, mContext);
        mPublishToViewInteractor.hideProgressDialog();
        mPublishToViewInteractor.startJourneySearchActivity();
        mPublishToViewInteractor.finishActivity();
    }

    /**
     * Called when signup process failed
     */
    @Override
    public void onSignUpFail() {
        mPublishToViewInteractor.hideProgressDialog();
        mPublishToViewInteractor.showSignUpFailMsg();
    }
}
