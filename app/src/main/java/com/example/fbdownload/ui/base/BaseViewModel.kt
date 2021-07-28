package com.example.fbdownload.ui.base

import androidx.lifecycle.ViewModel
import com.example.fbdownload.data.DataManager
import io.reactivex.Scheduler

open abstract class BaseViewModel : ViewModel() {
    lateinit var fragmentCallback: IFragmentCallback
    lateinit var navigation: Navigators
    lateinit var dataManager: DataManager
    lateinit var io: Scheduler
    lateinit var ui: Scheduler

    fun initData(dataManager: DataManager) {
        this.dataManager = dataManager
    }


    override fun onCleared() {
        super.onCleared()
    }

    fun onDestroyView() {
    }
}