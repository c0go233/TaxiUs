package com.mad.taxius.login;

import android.content.Intent;
import android.content.SharedPreferences;
import android.icu.text.DateFormat;
import android.icu.text.SimpleDateFormat;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.mad.taxius.R;
import com.mad.taxius.application.TaxiUsApplication;
import com.mad.taxius.base.BaseActivity;
import com.mad.taxius.constant.Constant;
import com.mad.taxius.database.journeychatrepository.JourneyChatRepositoryContract;
import com.mad.taxius.journeychat.JourneyChatActivity;
import com.mad.taxius.journeysearch.JourneySearchActivity;
import com.mad.taxius.journeysearch.JourneySearchContract;
import com.mad.taxius.setting.SettingActivity;
import com.mad.taxius.signup.SignUpActivity;
import com.mad.taxius.signup.SignUpPresenter;
import com.mad.taxius.timerservice.TimerService;
import com.mad.taxius.util.DateConverter;
import com.mad.taxius.util.NetworkChecker;

import java.util.Date;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Activity class for login function
 */
public class LoginActivity extends BaseActivity implements
        View.OnClickListener, LoginContract.PublishToView {

    @BindView(R.id.login_activity_sign_up_btn)
    Button mSignUpBtn;
    @BindView(R.id.login_activity_login_btn)
    Button mLoginBtn;
    @BindView(R.id.login_activity_forgot_password_btn)
    Button mForgotPasswordBtn;
    @BindView(R.id.login_activity_email_et)
    EditText mEmailEt;
    @BindView(R.id.login_activity_password_et)
    EditText mPasswordEt;
    @BindView(R.id.login_activity_email_error_tv)
    TextView mEmailErrorTv;
    @BindView(R.id.login_activity_password_error_tv)
    TextView mPasswordErrorTv;
    @BindView(R.id.login_activity_general_error_tv)
    TextView mGeneralErrorTv;
    @BindView(R.id.login_activity_general_error_ll)
    LinearLayout mGeneralErrorLl;
    @BindView(R.id.login_activity_login_with_google_btn)
    Button mLoginWithGoogleBtn;

    @Inject
    LoginPresenter mLoginPresenter;

    private TextView[] mErrorTextViews;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        setUp();

        //If user is logged in then directly go to journey search page
        if (mLoginPresenter.isLogined()) {
            startJourneySearchActivity();
            this.finish();
        }
    }

    /**
     * Set up basic settings of this activity
     */
    private void setUp() {
        ButterKnife.bind(this);
        TaxiUsApplication taxiUsApplication = (TaxiUsApplication) getApplication();
        DaggerLoginComponent.builder()
                .loginPresenterModule(new LoginPresenterModule(this, this, taxiUsApplication.getUserRepository()))
                .build()
                .inject(this);
        registerListeners();

        //Initialize error message list in the activity
        mErrorTextViews = new TextView[]{mEmailErrorTv, mPasswordErrorTv, mGeneralErrorTv};
    }

    /**
     * Register listeners of views in the activity
     */
    private void registerListeners() {
        mLoginBtn.setOnClickListener(this);
        mSignUpBtn.setOnClickListener(this);
        mForgotPasswordBtn.setOnClickListener(this);
        mLoginWithGoogleBtn.setOnClickListener(this);
    }

    /**
     * Call login function of its presenter with user inputs
     */
    private void login() {
        String email = mEmailEt.getText().toString();
        String password = mPasswordEt.getText().toString();
        mLoginPresenter.loginWithUserAccount(email, password);
    }

    /**
     * Show email error message to the screen
     */
    @Override
    public void showEmailErrorMsg() {
        mEmailErrorTv.setText(getString(R.string.all_invalid_email_error_msg));
    }

    /**
     * Show password error message to the screen
     */
    @Override
    public void showPasswordErrorMsg() {
        String errorMsg = getString(R.string.all_password_error_msg,
                Constant.UserDetail.MINIMUM_PASSWORD_LENGTH,
                Constant.UserDetail.MAXIMUM_PASSWORD_LENGTH);
        mPasswordErrorTv.setText(errorMsg);
    }

    @Override
    public void finishActivity() {
        this.finish();
    }

    /**
     * Hide all the error messages shown in the screen
     */
    @Override
    public void hideErrorMsg() {
        super.resetErrorMsgs(mErrorTextViews);
        mGeneralErrorLl.setVisibility(View.INVISIBLE);
    }

    /**
     * Show login fail message to the screen
     */
    @Override
    public void showLoginFailMsg() {
        mGeneralErrorTv.setText(getString(R.string.login_activity_login_fail_msg));
        mGeneralErrorLl.setVisibility(View.VISIBLE);
    }

    /**
     * Start the google login intent
     *
     * @param intent      is the google signin intent
     * @param requestCode is the request code for google signin
     */
    @Override
    public void startGoogleLoginIntent(Intent intent, int requestCode) {
        startActivityForResult(intent, requestCode);
    }

    /**
     * Show google signin fail message to the screen
     */
    @Override
    public void showGoogleLoginFailMsg() {
        mGeneralErrorTv.setText(getString(R.string.login_activity_login_fail_with_google));
        mGeneralErrorLl.setVisibility(View.VISIBLE);
    }

    /**
     * Start the journey search activity after login
     */
    @Override
    public void startJourneySearchActivity() {
        Intent intent = new Intent(this, JourneySearchActivity.class);
        startActivity(intent);
    }

    /**
     * Called after the google signin activity is done
     *
     * @param requestCode is the request code for the activity
     * @param resultCode  is the result code of the activity
     * @param data        is retrieved account information
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK && requestCode == Constant.GoogleSignin.REQUEST_CODE) {
            mLoginPresenter.onGoogleLoginActivityResult(data);
        }
    }

    @Override
    public void showProgressDialog() {
        super.showIndeterminateProgressDialog(LoginActivity.this,
                getString(R.string.login_activity_login_in_porcess));
    }

    @Override
    public void hideProgressDialog() {
        super.hideIndeterminateProgressDialog();
    }

    @Override
    public void showNetworkErrorMsg() {
        super.showToastMessage(getString(R.string.all_network_error_msg), this);
    }

    @Override
    public void onClick(View v) {
        int viewId = v.getId();
        switch (viewId) {
            case R.id.login_activity_login_btn:
                login();
                break;
            case R.id.login_activity_sign_up_btn:
                startActivity(new Intent(this, SignUpActivity.class));
                break;
            case R.id.login_activity_login_with_google_btn:
                mLoginPresenter.loginWithGoogleAccount(this);
                break;
            case R.id.login_activity_forgot_password_btn:
                break;
        }
    }
}
