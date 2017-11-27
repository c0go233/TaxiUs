package com.mad.taxius.setting;

import dagger.Component;

/**
 * Component class for SettingActivity that informs required module class for dependency injection
 */

@Component(modules = SettingPresenterModule.class)
public interface SettingComponent {

    void inject(SettingActivity settingActivity);
}
