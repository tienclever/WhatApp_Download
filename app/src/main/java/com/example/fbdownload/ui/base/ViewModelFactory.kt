package com.example.fbdownload.ui.base

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.fbdownload.data.DataManager
import com.example.fbdownload.ui.browse.BrowseViewModel
import com.example.fbdownload.ui.facebook.FacebookViewModel
import com.example.fbdownload.ui.home.HomeViewModel
import com.example.fbdownload.ui.main.MainViewModel
import com.example.fbdownload.ui.video.VideoViewModel
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ViewModelFactory @Inject constructor(
    private var mDataManager: DataManager,
) : ViewModelProvider.NewInstanceFactory() {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        val viewModel = when{
            modelClass.isAssignableFrom(MainViewModel::class.java) -> MainViewModel() as T
            modelClass.isAssignableFrom(HomeViewModel::class.java) -> HomeViewModel() as T
            modelClass.isAssignableFrom(FacebookViewModel::class.java) -> FacebookViewModel() as T
            modelClass.isAssignableFrom(VideoViewModel::class.java) -> VideoViewModel() as T
            modelClass.isAssignableFrom(BrowseViewModel::class.java) -> BrowseViewModel() as T
            else -> throw IllegalArgumentException("Unknown ViewModel class: " + modelClass.name)
        }
        if (viewModel is BaseViewModel) {
            viewModel.initData(mDataManager)
        }
        return viewModel
    }
}