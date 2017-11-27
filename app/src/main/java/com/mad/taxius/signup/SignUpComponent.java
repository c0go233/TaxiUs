package com.mad.taxius.signup;

import javax.inject.Singleton;

import dagger.Component;

/**
 * Component class for SignupActivity that informs required module class for dependency injection
 */

@Singleton
@Component(modules = SignUpPresenterModule.class)
public interface SignUpComponent {

    void inject(SignUpActivity signUpActivity);
}
