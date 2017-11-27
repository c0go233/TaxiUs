package com.mad.taxius.journeyadd;

import android.content.Context;

import com.mad.taxius.database.journeyrepository.JourneyRepository;
import com.mad.taxius.journeysearch.JourneySearchContract;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

/**
 * Module class that provides required objects for dependency injection of JourneyAdd presenter and activity
 */
@Module
public class JourneyAddPresenterModule {
    private final JourneyAddContract.PublishToView mPublishToViewInteractor;
    private final Context mContext;
    private final JourneyRepository mJourneyRepository;

    public JourneyAddPresenterModule(JourneyAddContract.PublishToView publishToView,
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
    JourneyAddContract.PublishToView provideJourneySearchContractPublishToView() {
        return mPublishToViewInteractor;
    }

    @Provides
    @Singleton
    JourneyRepository provideJourneyRepository() {
        return mJourneyRepository;
    }
}
