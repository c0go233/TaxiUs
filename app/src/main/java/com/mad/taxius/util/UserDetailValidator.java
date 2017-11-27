package com.mad.taxius.util;

import android.util.Patterns;

import com.mad.taxius.constant.Constant;

/**
 * This class is for validating user inputs for signup or login
 */

public class UserDetailValidator {

    /**
     * Checks if user input of email is in valid format
     *
     * @param email is user input of email
     * @return boolean for whether input is valid
     */
    public static boolean isEmailValid(String email) {
        return Patterns.EMAIL_ADDRESS.matcher(email).matches();
    }

    /**
     * Checks if user input of name is in valid format
     *
     * @param name is user input of name
     * @return boolean for whether input is valid
     */
    public static boolean isNameValid(String name) {
        return isStringLengthBetween(name, Constant.UserDetail.MINIMUM_NAME_LENGTH,
                Constant.UserDetail.MAXIMUM_NAME_LENGTH);
    }

    /**
     * Checks if user input of password is in valid format
     *
     * @param password is user input of password
     * @return boolean for whether input is valid
     */
    public static boolean isPasswordValid(String password) {
        return isStringLengthBetween(password, Constant.UserDetail.MINIMUM_PASSWORD_LENGTH,
                Constant.UserDetail.MAXIMUM_PASSWORD_LENGTH);
    }

    /**
     * Checks if a string matches with certain length passed through parameter
     *
     * @param stringToValidate is the string to be validated
     * @param min              is the minimum length
     * @param max              is the maximum length
     * @return
     */
    private static boolean isStringLengthBetween(String stringToValidate, int min, int max) {
        int stringLength = stringToValidate.length();
        if (stringLength < min || stringLength > max) return false;
        return true;
    }
}
