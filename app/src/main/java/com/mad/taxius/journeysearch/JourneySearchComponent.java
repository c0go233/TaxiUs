package com.mad.taxius.journeysearch;


import javax.inject.Singleton;

import dagger.Component;

/**
 * Component class for journeySearchActivity that informs required module class for dependency injection
 */

@Singleton
@Component(modules = JourneySearchPresenterModule.class)
public interface JourneySearchComponent {

    void inject(JourneySearchActivity journeySearchActivity);
}
