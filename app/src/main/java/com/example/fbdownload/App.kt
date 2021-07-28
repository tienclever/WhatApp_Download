package com.example.fbdownload

import android.app.Application
import com.example.fbdownload.data.AppDataManager
import com.example.fbdownload.data.DataManager
import com.example.fbdownload.data.local.IPreferenceHelper
import com.example.fbdownload.data.local.PreferencesHelper
import com.example.fbdownload.ui.base.BaseActivity
import com.example.fbdownload.ui.base.ViewModelFactory

class App : Application() {

    private lateinit var preferencesHelper: IPreferenceHelper
    lateinit var dataManager: DataManager
    lateinit var factory: ViewModelFactory
    override fun onCreate() {
        super.onCreate()
        preferencesHelper =
            PreferencesHelper(
                this,
                "BasePreference"
            )
        dataManager = AppDataManager(preferencesHelper)
        factory = ViewModelFactory(
            dataManager,
        )
    }

    fun requestInject(activity: BaseActivity<*, *>){
        activity.inject(factory, dataManager)
    }
}