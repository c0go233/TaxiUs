package com.mad.taxius.application;

import com.microsoft.windowsazure.mobileservices.MobileServiceClient;

import dagger.Module;
import dagger.Provides;

/**
 * Module class that provides Azure database service client
 */
@Module
public class MobileServiceClientModule {

    private final MobileServiceClient mClient;

    MobileServiceClientModule(MobileServiceClient client) {
        this.mClient = client;
    }

    /**
     * Provides Azure database service client for dependency injection
     *
     * @return Azure mobile service client
     */
    @Provides
    MobileServiceClient provideMobileServiceClient() {
        return mClient;
    }

}
