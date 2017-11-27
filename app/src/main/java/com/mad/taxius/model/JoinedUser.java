package com.mad.taxius.model;

import com.google.gson.annotations.SerializedName;

/**
 * POJO class that contains information about joined user
 */

public class JoinedUser {

    @SerializedName("id")
    private String mId;
    @SerializedName("name")
    private String mName;
    @SerializedName("email")
    private String mEmail;
    @SerializedName("journeyId")
    private String mJourneyId;

    public JoinedUser(String name, String email, String journeyId) {
        this.mName = name;
        this.mEmail = email;
        this.mJourneyId = journeyId;
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

    public String getJourneyId() {
        return mJourneyId;
    }

    public void setJourneyId(String journeyId) {
        this.mJourneyId = journeyId;
    }
}
