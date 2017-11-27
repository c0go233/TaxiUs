package com.mad.taxius.signup;

import android.content.Context;

import com.mad.taxius.database.userrepository.UserRepository;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

/**
 * Module class that provides required objects for dependency injection of
 * Signup presenter and activity
 */

@Module
public class SignUpPresenterModule {

    private final SignUpContract.PublishToView mPublishToView;
    private final UserRepository mUserRepository;
    private final Context mContext;

    public SignUpPresenterModule(SignUpContract.PublishToView publishToView,
                                 UserRepository userRepository, Context context) {
        mPublishToView = publishToView;
        mUserRepository = userRepository;
        mContext = context;
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
    SignUpContract.PublishToView provideSignUpContractPublishToView() {
        return mPublishToView;
    }
}
