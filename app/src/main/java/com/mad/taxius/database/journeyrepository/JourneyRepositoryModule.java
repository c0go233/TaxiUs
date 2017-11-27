package com.mad.taxius.database.journeyrepository;

import android.content.Context;

import com.mad.taxius.application.ContextModule;
import com.mad.taxius.application.MobileServiceClientModule;
import com.mad.taxius.database.userrepository.UserRepository;
import com.microsoft.windowsazure.mobileservices.MobileServiceClient;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

/**
 * Module class that provides journey repository for dependency injection
 */
@Module(includes = {ContextModule.class, MobileServiceClientModule.class})
public class JourneyRepositoryModule {

    @Provides
    @Singleton
    JourneyRepository provideJourneyRepository(Context context, MobileServiceClient client) {
        return new JourneyRepository(context, client);
    }
}
