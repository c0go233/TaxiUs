package com.mad.taxius.journeychat;

import javax.inject.Singleton;

import dagger.Component;

/**
 * Component class for journeyChatActivity that informs required module class for dependency injection
 */

@Singleton
@Component(modules = JourneyChatPresenterModule.class)
public interface JourneyChatComponent {

    void inject(JourneyChatActivity journeyChatActivity);
}