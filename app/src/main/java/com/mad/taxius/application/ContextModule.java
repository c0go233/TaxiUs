package com.mad.taxius.application;

import android.content.Context;

import dagger.Module;
import dagger.Provides;

/**
 * Module class that provides context dependency injection for application context
 */
@Module
public final class ContextModule {

    private final Context mContext;

    ContextModule(Context context) {
        mContext = context;
    }

    /**
     * provide application context for dependency injection
     *
     * @return application context
     */
    @Provides
    Context provideContext() {
        return mContext;
    }
}