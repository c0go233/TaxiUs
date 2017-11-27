package com.mad.taxius.base;

import android.content.Intent;

/**
 * Base Contract class that contains methods declarations that are used among other activities
 */
public interface BaseContract {
    void hideErrorMsg();

    void showProgressDialog();

    void hideProgressDialog();

    void showNetworkErrorMsg();
}

