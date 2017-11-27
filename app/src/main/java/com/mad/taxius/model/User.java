package com.mad.taxius.model;

import com.google.gson.annotations.SerializedName;

import java.util.Date;

/**
 * User class that contains information about user
 */

public class User {
    @SerializedName("id")
    private String mId;
    @SerializedName("name")
    private String mName;
    @SerializedName("email")
    private String mEmail;
    @SerializedName("password")
    private String mPassword;
    @SerializedName("isSocialLogin")
    private boolean mIsSocialLogin;
    @SerializedName("isCanceled")
    private boolean mIsCanceled;
    @SerializedName("createdAt")
    private Date mCreatedAt;

    public User() {
    }

    public User(String email, String password) {
        this.mEmail = email;
        this.mPassword = password;
    }

    public User(String name, String email, String password, boolean isSocialLogin) {
        this.mName = name;
        this.mEmail = email;
        this.mPassword = password;
        this.mIsSocialLogin = isSocialLogin;
        this.mIsCanceled = false;
    }

    public String getId() {
        return mId;
    }

    public void setId(String mId) {
        this.mId = mId;
    }

    public String getName() {
        return mName;
    }

    public void setName(String mName) {
        this.mName = mName;
    }

    public String getEmail() {
        return mEmail;
    }

    public void setEmail(String mEmail) {
        this.mEmail = mEmail;
    }

    public String getPassword() {
        return mPassword;
    }

    public void setPassword(String mPassword) {
        this.mPassword = mPassword;
    }

    public boolean isIsSocialLogin() {
        return mIsSocialLogin;
    }

    public void setIsSocialLogin(boolean mIsSocialLogin) {
        this.mIsSocialLogin = mIsSocialLogin;
    }

    public boolean isIsCanceled() {
        return mIsCanceled;
    }

    public void setIsCanceled(boolean mIsCanceled) {
        this.mIsCanceled = mIsCanceled;
    }

    public Date getCreatedAt() {
        return mCreatedAt;
    }

    public void setCreatedAt(Date mCreatedAt) {
        this.mCreatedAt = mCreatedAt;
    }
}
