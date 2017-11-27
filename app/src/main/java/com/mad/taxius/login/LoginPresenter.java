package com.mad.taxius.login;

import android.app.Instrumentation;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.util.Log;

import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.mad.taxius.base.BaseAccountPresenter;
import com.mad.taxius.constant.Constant;
import com.mad.taxius.database.userrepository.UserRepository;
import com.mad.taxius.database.userrepository.UserRepositoryContract;
import com.mad.taxius.journeysearch.JourneySearchActivity;
import com.mad.taxius.model.User;
import com.mad.taxius.util.NetworkChecker;

import javax.inject.Inject;

/**
 * Presenter class for LoginActivity to perform login functions
 */

public class LoginPresenter extends BaseAccountPresenter implements
        LoginContract.Login, UserRepositoryContract.LoginCallback, LoginContract.LoginWithGoogle,
        GoogleApiClient.OnConnectionFailedListener {

    private final LoginContract.PublishToView mPublishToViewInteractor;
    private final UserRepository mUserRepository;
    private final Context mContext;
    private GoogleApiClient googleApiClient;

    @Inject
    public LoginPresenter(LoginContract.PublishToView publishToView,
                          UserRepository userRepository, Context context) {
        this.mPublishToViewInteractor = publishToView;
        this.mUserRepository = userRepository;
        this.mContext = context;
    }

    /**
     * Called when login inputs are valid to start the next activity, journeySearchActivity
     *
     * @param user is the logined user object
     */
    @Override
    public void onLoginSuccess(User user) {
        super.saveLoginedState(user, true, mContext);
        mPublishToViewInteractor.hideProgressDialog();
        mPublishToViewInteractor.startJourneySearchActivity();
        mPublishToViewInteractor.finishActivity();
    }

    /**
     * Called when user inputs for login are not valid to show login fail message
     */
    @Override
    public void onLoginFail() {
        mPublishToViewInteractor.hideProgressDialog();
        mPublishToViewInteractor.showLoginFailMsg();
    }

    /**
     * Login with the inputs that users manually enter
     *
     * @param email    is the user input of email
     * @param password is the user input of password
     */
    @Override
    public void loginWithUserAccount(String email, String password) {
        mPublishToViewInteractor.hideErrorMsg();
        boolean isValid = super.validateLoginDetails(email, password, mPublishToViewInteractor);
        if (isValid) {
            if (!NetworkChecker.isNetworkAvailable(mContext)) {
                mPublishToViewInteractor.showNetworkErrorMsg();
            } else {
                mPublishToViewInteractor.showProgressDialog();
                mUserRepository.loginWithDetails(email, password, this);
            }
        }
    }

    /**
     * Checks if a user is logged in the application
     *
     * @return boolean for whether a user is logged in
     */
    @Override
    public boolean isLogined() {
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(mContext);
        return sp.getBoolean(Constant.PreferenceKey.LOGINED, false);
    }

    /**
     * Start google signin activity to verify the user's google account
     *
     * @param loginActivity is the callback activity
     */
    @Override
    public void loginWithGoogleAccount(LoginActivity loginActivity) {
        if (!NetworkChecker.isNetworkAvailable(mContext)) {
            mPublishToViewInteractor.showNetworkErrorMsg();
        } else {
            Intent googleSigninIntent = getGoogleLoginIntent(loginActivity);
            mPublishToViewInteractor.startGoogleLoginIntent(googleSigninIntent,
                    Constant.GoogleSignin.REQUEST_CODE);
        }
    }

    /**
     * Called when the google signin activity is done
     *
     * @param data is the google account information from google signin
     */
    @Override
    public void onGoogleLoginActivityResult(Intent data) {
        GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
        if (result.isSuccess()) loginWithVerifiedGoogleAccount(result.getSignInAccount());
        else mPublishToViewInteractor.showGoogleLoginFailMsg();
    }

    /**
     * Login with verified google account's email and name
     *
     * @param signInAccount is the google account verified
     */
    private void loginWithVerifiedGoogleAccount(GoogleSignInAccount signInAccount) {
        mPublishToViewInteractor.showProgressDialog();
        String email = signInAccount.getEmail();
        String name = signInAccount.getDisplayName();
        if (email.isEmpty() || email == null) onLoginFail();
        else mUserRepository.loginWithGoogle(email, name, this);
    }

    /**
     * Get google api client for google signin activity
     *
     * @param loginActivity is the callback activity
     * @return google api client
     */
    private GoogleApiClient getGoogleApiClient(LoginActivity loginActivity) {
        if (googleApiClient == null) {
            googleApiClient = new GoogleApiClient.Builder(mContext).enableAutoManage(loginActivity, this)
                    .addApi(Auth.GOOGLE_SIGN_IN_API, getGoogleSignInOptions()).build();
        }
        return googleApiClient;
    }

    /**
     * Get the google signin options
     *
     * @return google signin options
     */
    private GoogleSignInOptions getGoogleSignInOptions() {
        return new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail().build();
    }

    /**
     * Get the google signin intent to start the google signin activity
     *
     * @param loginActivity is the callback activity
     * @return Google signin intent
     */
    private Intent getGoogleLoginIntent(LoginActivity loginActivity) {
        return Auth.GoogleSignInApi.getSignInIntent(getGoogleApiClient(loginActivity));
    }

    /**
     * Called when connection to the google signin API
     *
     * @param connectionResult is the connection result information
     */
    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        onLoginFail();
    }
}
