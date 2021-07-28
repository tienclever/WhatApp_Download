package com.example.fbdownload.ui.home

import android.content.Intent
import android.widget.Toast
import androidx.core.view.GravityCompat
import androidx.viewpager2.widget.ViewPager2
import com.example.fbdownload.R
import com.example.fbdownload.databinding.FragmentHomeBinding
import com.example.fbdownload.ui.adapter.FragmentAdapter
import com.example.fbdownload.ui.base.BaseFragment
import com.example.fbdownload.ui.main.AboutUs
import com.example.fbdownload.ui.main.PrivacyPolicy
import com.google.android.material.tabs.TabLayout

class HomeFragment : BaseFragment<HomeViewModel, FragmentHomeBinding>() {
    var adapter: FragmentAdapter? = null
    override fun createViewModel(): Class<HomeViewModel> {
        return HomeViewModel::class.java
    }

    override fun getResourceLayout(): Int {
        return R.layout.fragment_home
    }

    override fun initView() {
    }

    override fun bindViewModel() {
        adapter = FragmentAdapter(childFragmentManager, lifecycle)
        dataBinding.viewpager.adapter = adapter

        dataBinding.tabs.let {
            it.addTab(it.newTab().setText("Image"))
            it.addTab(it.newTab().setText("Video"))
            it.addTab(it.newTab().setText("Save file"))
            it.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
                override fun onTabSelected(tab: TabLayout.Tab?) {
                    dataBinding.viewpager.currentItem = tab?.position!!
                }

                override fun onTabUnselected(tab: TabLayout.Tab?) {
                }

                override fun onTabReselected(tab: TabLayout.Tab?) {
                }
            })
        }
        dataBinding.viewpager.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                super.onPageSelected(position)
                dataBinding.tabs.selectTab(dataBinding.tabs.getTabAt(position))
            }
        })

        dataBinding.toolbar.setNavigationOnClickListener {
            dataBinding.drawerLayout.openDrawer(GravityCompat.START)
        }
        dataBinding.menu1.setOnClickListener {
            Toast.makeText(context, "Rate Us", Toast.LENGTH_SHORT).show()
        }
        dataBinding.menu2.setOnClickListener {
            val shareIntent = Intent(Intent.ACTION_SEND)
            shareIntent.type = "text/plain"
            val app_url = "https://github.com/GauthamAsir/WhatsApp_Status_Saver/releases"
            shareIntent.putExtra(
                Intent.EXTRA_TEXT,
                "Hey check out my app at \n\n$app_url"
            )
            shareIntent.putExtra(Intent.EXTRA_SUBJECT, "WhatsApp Status Saver")
            startActivity(Intent.createChooser(shareIntent, "Share via"))

        }
        dataBinding.menu3.setOnClickListener {
            startActivity(Intent(context, PrivacyPolicy::class.java))
        }
        dataBinding.menu4.setOnClickListener {
            startActivity(Intent(context, AboutUs::class.java))
        }
        dataBinding.menu5.setOnClickListener {
            Toast.makeText(context, "About Us", Toast.LENGTH_SHORT).show()
        }
    }
}