package com.mad.taxius.journeyadd;

import com.mad.taxius.journeysearch.JourneySearchActivity;
import com.mad.taxius.journeysearch.JourneySearchPresenterModule;

import javax.inject.Singleton;

import dagger.Component;

/**
 * Component class for journeyAddActivity that informs required module class for dependency injection
 */
@Singleton
@Component(modules = JourneyAddPresenterModule.class)
public interface JourneyAddComponent {

    void inject(JourneyAddActivity journeyAddActivity);
}
