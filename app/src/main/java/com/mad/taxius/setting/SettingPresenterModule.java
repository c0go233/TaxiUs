package com.mad.taxius.setting;

import android.content.Context;

import javax.inject.Inject;

import dagger.Module;
import dagger.Provides;

/**
 * Module class that provides required objects for dependency injection of
 * Setting presenter and activity
 */

@Module
public class SettingPresenterModule {

    private Context mContext;
    private SettingContract.PublishToView mPublishToView;


    public SettingPresenterModule(Context context, SettingContract.PublishToView publishToView) {
        this.mContext = context;
        this.mPublishToView = publishToView;
    }

    @Provides
    Context provideContext() {
        return mContext;
    }

    @Provides
    SettingContract.PublishToView provideSettingContractPublishToView() {
        return mPublishToView;
    }
}
