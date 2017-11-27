package com.mad.taxius.login;

import javax.inject.Singleton;

import dagger.Component;

/**
 * Component class for LoginActivity that informs required module class for dependency injection
 */

@Singleton
@Component(modules = LoginPresenterModule.class)
public interface LoginComponent {

    void inject(LoginActivity loginActivity);
}
