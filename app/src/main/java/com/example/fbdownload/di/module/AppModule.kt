package com.example.fbdownload.di.module

import android.app.Application
import android.content.Context
import com.example.fbdownload.data.AppDataManager
import com.example.fbdownload.data.DataManager
import com.example.fbdownload.data.local.IPreferenceHelper
import com.example.fbdownload.data.local.PreferenceInfo
import com.example.fbdownload.data.local.PreferencesHelper
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class AppModule {
    @Provides
    @PreferenceInfo
    internal fun providePreferenceName(): String {
        return "KeyPreference"
    }

    @Provides
    @Singleton
    internal fun provideContext(application: Application): Context {
        return application
    }

    @Provides
    @Singleton
    internal fun providePreferenceHelper(preferencesHelper: PreferencesHelper): IPreferenceHelper {
        return preferencesHelper
    }

    @Provides
    @Singleton
    internal fun provideDataManager(appDataManager: AppDataManager): DataManager {
        return appDataManager
    }

}