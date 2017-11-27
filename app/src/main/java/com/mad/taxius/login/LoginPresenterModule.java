package com.mad.taxius.login;

import android.content.Context;

import com.mad.taxius.database.userrepository.UserRepository;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

/**
 * Module class that provides required objects for dependency injection of
 * Login presenter and activity
 */

@Module
public class LoginPresenterModule {

    private final LoginContract.PublishToView mPublishToViewInteractor;
    private final UserRepository mUserRepository;
    private final Context mContext;

    public LoginPresenterModule(LoginContract.PublishToView publishToView,
                                Context context, UserRepository userRepository) {
        this.mPublishToViewInteractor = publishToView;
        this.mUserRepository = userRepository;
        this.mContext = context;
    }

    @Provides
    Context provideContext() {
        return mContext;
    }

    @Provides
    @Singleton
    UserRepository provideUserRepository() {
        return mUserRepository;
    }

    @Provides
    LoginContract.PublishToView provideLoginContractPubilshToView() {
        return mPublishToViewInteractor;
    }
}
