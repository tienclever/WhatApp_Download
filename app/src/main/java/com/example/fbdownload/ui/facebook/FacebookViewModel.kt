package com.example.fbdownload.ui.facebook

import android.os.Handler
import android.widget.ProgressBar
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.example.fbdownload.data.model.Status
import com.example.fbdownload.ui.adapter.ImageAdapter
import com.example.fbdownload.ui.base.BaseViewModel
import java.util.ArrayList

class FacebookViewModel: BaseViewModel() {
    var recyclerView: RecyclerView? = null
    var progressBar: ProgressBar? = null
    var imagesList: MutableList<Status> = ArrayList()
    var handler = Handler()
    var imageAdapter: ImageAdapter? = null
    var container: RelativeLayout? = null
    var swipeRefreshLayout: SwipeRefreshLayout? = null
    var messageTextView: TextView? = null
}