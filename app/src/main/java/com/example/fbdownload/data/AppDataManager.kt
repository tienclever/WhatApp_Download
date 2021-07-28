package com.example.fbdownload.data

import com.example.fbdownload.data.local.IPreferenceHelper
import javax.inject.Inject

class AppDataManager @Inject constructor(
    private val mPreferencesHelper: IPreferenceHelper
) : DataManager{
    override var token: String
        get() = mPreferencesHelper.token
        set(value) {
            mPreferencesHelper.token = value
        }

}