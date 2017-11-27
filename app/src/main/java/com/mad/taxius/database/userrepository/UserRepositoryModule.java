package com.mad.taxius.database.userrepository;

import android.content.Context;

import com.mad.taxius.application.ContextModule;
import com.mad.taxius.application.MobileServiceClientModule;
import com.microsoft.windowsazure.mobileservices.MobileServiceClient;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

/**
 * Module class that provides User repository object for dependency injection
 */
@Module(includes = {ContextModule.class, MobileServiceClientModule.class})
public class UserRepositoryModule {

    @Provides
    @Singleton
    UserRepository provideUserRepository(Context context, MobileServiceClient client) {
        return new UserRepository(context, client);
    }
}
