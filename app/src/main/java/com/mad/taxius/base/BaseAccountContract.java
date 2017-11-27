package com.mad.taxius.base;

import android.content.Intent;

/**
 * Contract class that contains common methods between account activities
 * such as login and sign up
 */
public interface BaseAccountContract extends BaseContract {
    void showEmailErrorMsg();

    void showPasswordErrorMsg();

    void finishActivity();
}
