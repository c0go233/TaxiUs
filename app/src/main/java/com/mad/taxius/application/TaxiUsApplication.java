package com.mad.taxius.application;

import android.app.Application;

import com.mad.taxius.constant.Constant;
import com.mad.taxius.database.joineduserrepository.JoinedUserRepository;
import com.mad.taxius.database.joineduserrepository.JoinedUserRepositoryModule;
import com.mad.taxius.database.journeyrepository.JourneyRepository;
import com.mad.taxius.database.journeyrepository.JourneyRepositoryModule;
import com.mad.taxius.database.userrepository.UserRepository;
import com.mad.taxius.database.userrepository.UserRepositoryModule;
import com.mad.taxius.model.JoinedUser;
import com.microsoft.windowsazure.mobileservices.MobileServiceClient;
import com.microsoft.windowsazure.mobileservices.http.OkHttpClientFactory;
import com.squareup.okhttp.OkHttpClient;

import java.net.MalformedURLException;
import java.util.concurrent.TimeUnit;

/**
 * Extended application class in this application contains
 * application level components such as database client
 */
public class TaxiUsApplication extends Application {

    private MobileServiceClient mClient;
    private UserRepository mUserRepository;
    private JourneyRepository mJourneyRepository;
    private JoinedUserRepository mJoinedUserRepository;
    private static final int DEFAULT_TIME_OUT = 20;

    @Override
    public void onCreate() {
        super.onCreate();
        setMobileServiceClient();
        setUp();
    }

    /**
     * Set up basic settings of dependency injection and initializing fields
     */
    private void setUp() {
        //Perform dependency injection
        TaxiUsApplicationComponent taxiUsApplicationComponent =
                DaggerTaxiUsApplicationComponent.builder()
                        .contextModule(new ContextModule(this))
                        .mobileServiceClientModule(new MobileServiceClientModule(mClient))
                        .userRepositoryModule(new UserRepositoryModule())
                        .journeyRepositoryModule(new JourneyRepositoryModule())
                        .joinedUserRepositoryModule(new JoinedUserRepositoryModule())
                        .build();
        mUserRepository = taxiUsApplicationComponent.getUserRepository();
        mJourneyRepository = taxiUsApplicationComponent.getJourneyRepository();
        mJoinedUserRepository = taxiUsApplicationComponent.getJoinedUserRepository();
    }

    /**
     * Initialize the Azure database service client
     */
    private void setMobileServiceClient() {
        try {
            if (mClient == null) {
                mClient = new MobileServiceClient(Constant.Database.CONNECTION_URL, this);
                mClient.setAndroidHttpClientFactory(new OkHttpClientFactory() {
                    @Override
                    public OkHttpClient createOkHttpClient() {
                        OkHttpClient client = new OkHttpClient();
                        client.setReadTimeout(DEFAULT_TIME_OUT, TimeUnit.SECONDS);
                        client.setWriteTimeout(DEFAULT_TIME_OUT, TimeUnit.SECONDS);
                        return client;
                    }
                });
            }
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
    }

    /**
     * Provide user repository for other components in the application
     *
     * @return user repository
     */
    public UserRepository getUserRepository() {
        return mUserRepository;
    }

    /**
     * Provide journey repository for other components in the application
     *
     * @return journey repository
     */
    public JourneyRepository getJourneyRepository() {
        return mJourneyRepository;
    }

    public JoinedUserRepository getJoinedUserRepository() {
        return mJoinedUserRepository;
    }

}
