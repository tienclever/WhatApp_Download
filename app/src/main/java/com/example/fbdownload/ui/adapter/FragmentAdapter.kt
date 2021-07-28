package com.example.fbdownload.ui.adapter

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.example.fbdownload.ui.browse.BrowseFragment
import com.example.fbdownload.ui.facebook.FacebookFragment
import com.example.fbdownload.ui.video.VideoFragment

class FragmentAdapter(fragmentManager: FragmentManager, lifecycle: Lifecycle) :
    FragmentStateAdapter(fragmentManager, lifecycle) {
    override fun createFragment(position: Int): Fragment {
        return when (position) {
            0 -> FacebookFragment()
            1 -> VideoFragment()
            2 -> BrowseFragment()
            else -> FacebookFragment()
        }
    }

    override fun getItemCount(): Int {
        return 3
    }
}