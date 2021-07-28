package com.example.fbdownload.ui.main

import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.view.View
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.cardview.widget.CardView
import androidx.databinding.DataBindingUtil
import com.example.fbdownload.R
import com.example.fbdownload.databinding.ActivityAboutUsBinding

class AboutUs : AppCompatActivity() {
    lateinit var binding : ActivityAboutUsBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_about_us)
        val contact = findViewById<CardView>(R.id.contact)
        contact.setOnClickListener { v: View? ->
            val sendtelegram = Intent(Intent.ACTION_VIEW, Uri.parse(getString(R.string.telegram_link)))
            startActivity(sendtelegram)
        }
        val email = findViewById<CardView>(R.id.email)
        email.setOnClickListener { v: View? ->
            val sendEmail = Intent(Intent.ACTION_SENDTO)
            val mail = "mailto:" + getString(R.string.mail_id)
            sendEmail.data = Uri.parse(mail)
            startActivity(sendEmail)
        }
        val app_version = findViewById<TextView>(R.id.app_version)
        app_version.text = appVersion
    }

    private val appVersion: String
        private get() {
            var result = ""
            try {
                result = applicationContext.packageManager.getPackageInfo(applicationContext.packageName, 0).versionName
                result = result.replace("[a-zA-Z]|-".toRegex(), "")
            } catch (e: PackageManager.NameNotFoundException) {
                e.printStackTrace()
            }
            return result
        }
}