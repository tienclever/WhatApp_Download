package com.example.fbdownload.ui.main

import android.Manifest
import com.example.fbdownload.ui.base.BaseViewModel

class MainViewModel : BaseViewModel() {

     var REQUEST_PERMISSIONS = 1234
     var PERMISSIONS = arrayOf(
        Manifest.permission.READ_EXTERNAL_STORAGE,
        Manifest.permission.WRITE_EXTERNAL_STORAGE
    )
     var MANAGE_EXTERNAL_STORAGE_PERMISSION = "android:manage_external_storage"
}