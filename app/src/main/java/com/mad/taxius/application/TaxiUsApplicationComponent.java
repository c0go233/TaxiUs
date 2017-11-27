package com.mad.taxius.application;

import com.mad.taxius.database.joineduserrepository.JoinedUserRepository;
import com.mad.taxius.database.joineduserrepository.JoinedUserRepositoryModule;
import com.mad.taxius.database.journeyrepository.JourneyRepository;
import com.mad.taxius.database.journeyrepository.JourneyRepositoryModule;
import com.mad.taxius.database.userrepository.UserRepository;
import com.mad.taxius.database.userrepository.UserRepositoryModule;

import javax.inject.Singleton;

import dagger.Component;


/**
 * Component class that informs required module classes for dependency injection
 * of application class
 */
@Singleton
@Component(modules = {UserRepositoryModule.class, JourneyRepositoryModule.class,
        JoinedUserRepositoryModule.class})
public interface TaxiUsApplicationComponent {

    UserRepository getUserRepository();

    JourneyRepository getJourneyRepository();

    JoinedUserRepository getJoinedUserRepository();
}
