package com.mad.taxius.journeysearch;

import android.content.Context;

import com.mad.taxius.database.joineduserrepository.JoinedUserRepository;
import com.mad.taxius.database.journeyrepository.JourneyRepository;

import javax.inject.Inject;
import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

/**
 * Module class that provides required objects for dependency injection of
 * journeySearch presenter and activity
 */

@Module
public class JourneySearchPresenterModule {

    private final JourneySearchContract.PublishToView mPublishToViewInteractor;
    private final Context mContext;
    private final JourneyRepository mJourneyRepository;


    public JourneySearchPresenterModule(JourneySearchContract.PublishToView publishToView,
                                        Context context, JourneyRepository journeyRepository) {
        this.mPublishToViewInteractor = publishToView;
        this.mContext = context;
        this.mJourneyRepository = journeyRepository;
    }

    @Provides
    Context provideContext() {
        return mContext;
    }

    @Provides
    JourneySearchContract.PublishToView provideJourneySearchContractPublishToView() {
        return mPublishToViewInteractor;
    }

    @Provides
    @Singleton
    JourneyRepository provideJourneyRepository() {
        return mJourneyRepository;
    }

}
