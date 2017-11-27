package com.mad.taxius.base;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import com.mad.taxius.constant.Constant;
import com.mad.taxius.model.User;
import com.mad.taxius.signup.SignUpContract;
import com.mad.taxius.util.UserDetailValidator;

/**
 * Created by kisungTae on 24/09/2017.
 */

public class BaseAccountPresenter {

    /**
     * Validate login details that users enter for login
     *
     * @param email    user input of email
     * @param password user input of password
     * @param contract callback interface
     * @return boolean for whether or not the login details are valid
     */
    protected boolean validateLoginDetails(String email, String password, BaseAccountContract contract) {
        boolean isValid = true;
        if (!UserDetailValidator.isEmailValid(email)) {
            isValid = false;
            contract.showEmailErrorMsg();
        }
        if (!UserDetailValidator.isPasswordValid(password)) {
            isValid = false;
            contract.showPasswordErrorMsg();
        }
        return isValid;
    }

    /**
     * Validate signup detils that users enter for signup
     *
     * @param name          user input of name
     * @param email         user input of email
     * @param password      user input of password
     * @param publishToView callback interface
     * @return boolean for whether or not the signup details are valid
     */
    protected boolean validateSignUpDetails(String name, String email,
                                            String password, SignUpContract.PublishToView publishToView) {
        boolean isValid = validateLoginDetails(email, password, publishToView);
        if (!UserDetailValidator.isNameValid(name)) {
            isValid = false;
            publishToView.showNameErrorMsg();
        }
        return isValid;
    }

    /**
     * Save the logined user's basic information of email, name and login status
     *
     * @param loginedUser logined user object
     * @param logined     login status
     * @param context     context for preferences
     */
    protected void saveLoginedState(User loginedUser, boolean logined, Context context) {
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = sp.edit();
        editor.putString(Constant.PreferenceKey.LOGINED_EMAIL, loginedUser.getEmail());
        editor.putString(Constant.PreferenceKey.NAME, loginedUser.getName());
        editor.putBoolean(Constant.PreferenceKey.LOGINED, logined);
        editor.commit();
    }
}
