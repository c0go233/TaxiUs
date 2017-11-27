package com.mad.taxius.database.joineduserrepository;

import android.content.Context;

import com.mad.taxius.application.ContextModule;
import com.mad.taxius.application.MobileServiceClientModule;
import com.mad.taxius.database.journeyrepository.JourneyRepository;
import com.microsoft.windowsazure.mobileservices.MobileServiceClient;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

/**
 * Module class that provides joined user repository for dependency injection
 */

@Module(includes = {ContextModule.class, MobileServiceClientModule.class})
public class JoinedUserRepositoryModule {

    @Provides
    @Singleton
    JoinedUserRepository provideJoinedUserRepository(Context context, MobileServiceClient client) {
        return new JoinedUserRepository(context, client);
    }
}