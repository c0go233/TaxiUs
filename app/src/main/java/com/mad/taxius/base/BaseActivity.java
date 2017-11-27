package com.mad.taxius.base;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Rect;
import android.support.v7.app.AppCompatActivity;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.mad.taxius.R;
import com.mad.taxius.signup.SignUpActivity;

/**
 * Base Activity class that contains common methods used among activities
 */
public class BaseActivity extends AppCompatActivity {

    protected ProgressDialog mProgressDialog;

    /**
     * Reset the error messages shown in the screen
     *
     * @param errorTextViews error message text views
     */
    protected void resetErrorMsgs(TextView[] errorTextViews) {
        for (TextView errorTextView : errorTextViews) {
            errorTextView.setText("");
        }
    }

    /**
     * Set and show the indeterminate progress dialog
     *
     * @param context is the context for showing dialog
     * @param msg     is the message inserted into dialog
     */
    protected void showIndeterminateProgressDialog(Context context, String msg) {
        mProgressDialog = new ProgressDialog(context);
        mProgressDialog.setMessage(msg);
        mProgressDialog.setIndeterminate(true);
        mProgressDialog.setCancelable(true);
        mProgressDialog.show();
    }

    protected void hideIndeterminateProgressDialog() {
        if (mProgressDialog != null) mProgressDialog.dismiss();
    }

    protected void showToastMessage(String msg, Context context) {
        Toast.makeText(context, msg, Toast.LENGTH_LONG).show();
    }

    /**
     * Dismiss the keyboard when touched outside EditTextViews
     *
     * @param ev motion event that occured
     * @return boolean
     */
    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        if (ev.getAction() == MotionEvent.ACTION_DOWN) {
            View view = getCurrentFocus();
            if (view instanceof EditText) {
                Rect outRect = new Rect();
                view.getGlobalVisibleRect(outRect);
                if (!outRect.contains((int) ev.getRawX(), (int) ev.getRawY())) {
                    view.clearFocus();
                    InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                    inputMethodManager.hideSoftInputFromWindow(view.getWindowToken(), 0);
                }
            }
        }
        return super.dispatchTouchEvent(ev);
    }
}