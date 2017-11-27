package com.mad.taxius.signup;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.mad.taxius.R;
import com.mad.taxius.application.TaxiUsApplication;
import com.mad.taxius.base.BaseActivity;
import com.mad.taxius.constant.Constant;
import com.mad.taxius.journeysearch.JourneySearchActivity;
import com.mad.taxius.journeysearch.JourneySearchContract;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Activity class for signup function
 */
public class SignUpActivity extends BaseActivity implements
        SignUpContract.PublishToView, View.OnClickListener {

    @BindView(R.id.sign_up_activity_tool_bar)
    Toolbar mToolbar;
    @BindView(R.id.sign_up_activity_email_et)
    EditText mEmailEt;
    @BindView(R.id.sign_up_activity_name_et)
    EditText mNameEt;
    @BindView(R.id.sign_up_activity_password_et)
    EditText mPasswordEt;
    @BindView(R.id.sign_up_activity_sign_up_btn)
    Button mSignUpBtn;
    @BindView(R.id.sign_up_activity_name_error_tv)
    TextView mNameErrorTv;
    @BindView(R.id.sign_up_activity_email_error_tv)
    TextView mEmailErrorTv;
    @BindView(R.id.sign_up_activity_password_error_tv)
    TextView mPasswordErrorTv;
    @BindView(R.id.sign_up_activity_general_error_ll)
    LinearLayout mGeneralErrorLl;
    @BindView(R.id.sign_up_activity_general_error_tv)
    TextView mGeneralErrorTv;

    @Inject
    SignUpPresenter mSignUpPresenter;

    private TextView[] mErrorTextViews;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        setUp();

    }

    /**
     * Set up basic settings of this activity
     */
    private void setUp() {
        ButterKnife.bind(this);
        TaxiUsApplication taxiUsApplication = (TaxiUsApplication) getApplication();
        DaggerSignUpComponent.builder()
                .signUpPresenterModule(new SignUpPresenterModule(this, taxiUsApplication.getUserRepository(), this))
                .build()
                .inject(this);
        mSignUpBtn.setOnClickListener(this);
        mErrorTextViews = new TextView[]{mNameErrorTv, mEmailErrorTv, mPasswordErrorTv, mGeneralErrorTv};
        setToolbar();
    }

    /**
     * Set toolbar without title and with navigation icon
     */
    private void setToolbar() {
        mToolbar.setNavigationIcon(R.drawable.ic_left_arrow);
        mToolbar.setTitle("");
        setSupportActionBar(mToolbar);
        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    /**
     * Call signup function of signup presenter with user inputs
     */
    private void signUp() {
        String name = mNameEt.getText().toString();
        String email = mEmailEt.getText().toString();
        String password = mPasswordEt.getText().toString();
        mSignUpPresenter.signUp(name, email, password);
    }

    /**
     * Show error message for wrong email input
     */
    @Override
    public void showEmailErrorMsg() {
        mEmailErrorTv.setText(getString(R.string.all_invalid_email_error_msg));
    }

    /**
     * Show error message for wrong name input
     */
    @Override
    public void showNameErrorMsg() {
        String errorMsg = getString(R.string.sign_up_activity_name_error_msg,
                Constant.UserDetail.MINIMUM_NAME_LENGTH,
                Constant.UserDetail.MAXIMUM_NAME_LENGTH);
        mNameErrorTv.setText(errorMsg);
    }

    /**
     * Show error message for wrong password input
     */
    @Override
    public void showPasswordErrorMsg() {
        String errorMsg = getString(R.string.all_password_error_msg,
                Constant.UserDetail.MINIMUM_PASSWORD_LENGTH,
                Constant.UserDetail.MAXIMUM_PASSWORD_LENGTH);
        mPasswordErrorTv.setText(errorMsg);
    }

    /**
     * Hide error messages show in the screen
     */
    @Override
    public void hideErrorMsg() {
        super.resetErrorMsgs(mErrorTextViews);
        mGeneralErrorLl.setVisibility(View.INVISIBLE);
    }

    /**
     * Show error message for failing to sign up
     */
    @Override
    public void showSignUpFailMsg() {
        mGeneralErrorTv.setText(getString(R.string.sign_up_activity_sign_up_fail_error_msg));
        mGeneralErrorLl.setVisibility(View.VISIBLE);
    }

    /**
     * Start journey search activity after sign up
     */
    @Override
    public void startJourneySearchActivity() {
        Intent intent = new Intent(this, JourneySearchActivity.class);
        startActivity(intent);
    }

    @Override
    public void finishActivity() {
        this.finish();
    }

    @Override
    public void onClick(View v) {
        int viewId = v.getId();
        switch (viewId) {
            case R.id.sign_up_activity_sign_up_btn:
                signUp();
        }
    }

    @Override
    public void showProgressDialog() {
        super.showIndeterminateProgressDialog(SignUpActivity.this,
                getString(R.string.sign_up_activity_progress_msg));
    }

    @Override
    public void hideProgressDialog() {
        super.hideIndeterminateProgressDialog();
    }

    @Override
    public void showNetworkErrorMsg() {
        super.showToastMessage(getString(R.string.all_network_error_msg), this);
    }
}
