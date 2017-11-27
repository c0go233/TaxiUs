package com.mad.taxius.journeychat;

import android.content.Context;

import com.mad.taxius.database.joineduserrepository.JoinedUserRepository;
import com.mad.taxius.database.journeychatrepository.JourneyChatRepository;
import com.mad.taxius.database.journeyrepository.JourneyRepositoryModule;
import com.mad.taxius.model.JoinedUser;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

/**
 * Created by kisungTae on 5/10/2017.
 */

/**
 * Module class that provides dependency injection for journeyChatPresenter
 */
@Module
public class JourneyChatPresenterModule {

    private Context mContext;
    private JourneyChatContract.PublishToView mPublishToView;
    private JoinedUserRepository mJoinedUserRepository;

    public JourneyChatPresenterModule(Context context, JourneyChatContract.PublishToView publishToView,
                                      JoinedUserRepository joinedUserRepository) {
        this.mContext = context;
        this.mPublishToView = publishToView;
        this.mJoinedUserRepository = joinedUserRepository;
    }

    @Provides
    Context provideContext() {
        return mContext;
    }

    @Provides
    JourneyChatContract.PublishToView provideJourneyChatContractPublishToView() {
        return mPublishToView;
    }

    @Provides
    JourneyChatRepository provideJourneyChatRepository() {
        return new JourneyChatRepository();
    }

    @Singleton
    @Provides
    JoinedUserRepository provideJoinedUserRepository() {
        return mJoinedUserRepository;
    }
}
