package com.example.fbdownload.ui.main

import android.Manifest
import android.app.ActivityManager
import android.app.AppOpsManager
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.provider.Settings
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.databinding.DataBindingUtil
import com.example.fbdownload.R
import com.example.fbdownload.databinding.ActivitySplashBinding
import java.util.Objects

class SplashActivity : AppCompatActivity() {
    lateinit var binding : ActivitySplashBinding
    private val handler = Handler()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_splash)
        if (!arePermissionDenied()) {
            next()
            return
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && arePermissionDenied()) {

            // If Android 11 Request for Manage File Access Permission
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
                val intent = Intent(Settings.ACTION_MANAGE_ALL_FILES_ACCESS_PERMISSION)
                startActivityForResult(intent, REQUEST_PERMISSIONS)
                return
            }
            requestPermissions(PERMISSIONS, REQUEST_PERMISSIONS)
        }
    }

    override fun onResume() {
        super.onResume()
        if (!arePermissionDenied()) {
            next()
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.R)
    fun checkStorageApi30(): Boolean {
        val appOps = applicationContext.getSystemService(AppOpsManager::class.java)
        val mode = appOps.unsafeCheckOpNoThrow(
            MANAGE_EXTERNAL_STORAGE_PERMISSION,
            applicationContext.applicationInfo.uid,
            applicationContext.packageName
        )
        return mode != AppOpsManager.MODE_ALLOWED
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == REQUEST_PERMISSIONS && grantResults.size > 0) {
            if (arePermissionDenied()) {
                // Clear Data of Application, So that it can request for permissions again
                (Objects.requireNonNull(this.getSystemService(ACTIVITY_SERVICE)) as ActivityManager).clearApplicationUserData()
                recreate()
            } else {
                next()
            }
        }
    }

    private operator fun next() {
        handler.postDelayed({
            startActivity(Intent(this@SplashActivity, MainActivity::class.java))
            finish()
        }, 1000)
    }

    private fun arePermissionDenied(): Boolean {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            return checkStorageApi30()
        }
        for (permissions in PERMISSIONS) {
            if (ActivityCompat.checkSelfPermission(applicationContext, permissions) != PackageManager.PERMISSION_GRANTED) {
                return true
            }
        }
        return false
    }

    companion object {
        private const val REQUEST_PERMISSIONS = 1234
        private val PERMISSIONS = arrayOf(
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE
        )
        private const val MANAGE_EXTERNAL_STORAGE_PERMISSION = "android:manage_external_storage"
    }
}
