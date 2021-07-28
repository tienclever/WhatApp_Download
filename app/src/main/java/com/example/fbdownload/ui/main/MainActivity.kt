package com.example.fbdownload.ui.main

import android.app.AppOpsManager
import android.content.pm.PackageManager
import android.content.res.Resources
import android.os.Build
import android.os.Bundle
import android.os.Environment
import androidx.annotation.RequiresApi
import androidx.core.app.ActivityCompat
import com.example.fbdownload.R
import com.example.fbdownload.databinding.ActivityMainBinding
import com.example.fbdownload.ui.base.BaseActivity
import com.example.fbdownload.ui.base.BaseFragment
import com.example.fbdownload.ui.browse.BrowseFragment
import com.example.fbdownload.ui.facebook.FacebookFragment
import com.example.fbdownload.ui.home.HomeFragment
import com.example.fbdownload.ui.video.VideoFragment
import com.example.fbdownload.utils.Common
import java.io.File
import kotlin.reflect.KClass

open class MainActivity : BaseActivity<MainViewModel, ActivityMainBinding>() {
    lateinit var currentFragment: BaseFragment<*, *>
    override fun createViewModel(): Class<MainViewModel> {
        return MainViewModel::class.java
    }

    override fun getContentView(): Int {
        return R.layout.activity_main
    }

    override fun initAction() {
    }

    override fun initData() {
        switchFragment(HomeFragment::class)
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        currentFragment.onRequestPermissionsResult(requestCode, permissions, grantResults)
    }

    override fun onFragmentResumed(fragment: BaseFragment<*, *>) {
        val showButtonBack = supportFragmentManager.backStackEntryCount > 1
        supportActionBar?.setDisplayHomeAsUpEnabled(showButtonBack)
        supportActionBar?.setDisplayHomeAsUpEnabled(showButtonBack)
        currentFragment = fragment
    }

    override fun switchFragment(fragment: KClass<*>, bundle: Bundle?, addToBackStack: Boolean) {
        val instanceFragment = when (fragment) {
            HomeFragment::class -> HomeFragment()
            FacebookFragment::class -> FacebookFragment()
            VideoFragment::class -> VideoFragment()
            BrowseFragment::class -> BrowseFragment()
            else -> {
                throw Resources.NotFoundException("Fragment not fount, please check again")
            }
        }

        bundle?.let {
            instanceFragment.arguments = it
        }
        val ft = supportFragmentManager.beginTransaction()
        val tag = instanceFragment.TAG
        ft.replace(R.id.main_nav_fragment, instanceFragment, tag)
        if (addToBackStack) {
            ft.addToBackStack(tag)
        }
        ft.commit()
    }

    @RequiresApi(api = Build.VERSION_CODES.R)
    open fun checkStorageApi30(): Boolean {
        val appOps = applicationContext.getSystemService(AppOpsManager::class.java)
        val mode = appOps.unsafeCheckOpNoThrow(
            mDataBinding.viewModel!!.MANAGE_EXTERNAL_STORAGE_PERMISSION,
            applicationContext.applicationInfo.uid,
            applicationContext.packageName
        )
        return mode != AppOpsManager.MODE_ALLOWED
    }

    fun arePermissionDenied(): Boolean {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            return checkStorageApi30()
        }
        for (permissions in mDataBinding.viewModel!!.PERMISSIONS
        ) {
            if (ActivityCompat.checkSelfPermission(applicationContext, permissions) != PackageManager.PERMISSION_GRANTED) {
                return true
            }
        }
        return false
    }

    override fun onResume() {
        super.onResume()
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && arePermissionDenied()) {
            requestPermissions(mDataBinding.viewModel!!.PERMISSIONS, mDataBinding.viewModel!!.REQUEST_PERMISSIONS)
            return
        }
        Common.APP_DIR = Environment.getExternalStorageDirectory().path +
            File.separator + "StatusDownloader"
    }

    override fun onBackPressed() {
        super.onBackPressed()
        finishAffinity()
    }
}