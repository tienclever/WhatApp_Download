package com.example.fbdownload.ui.video

import android.os.Handler
import android.widget.ProgressBar
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.example.fbdownload.data.model.Status
import com.example.fbdownload.ui.adapter.VideoAdapter
import com.example.fbdownload.ui.base.BaseViewModel
import java.util.ArrayList

class VideoViewModel: BaseViewModel() {
    var recyclerView: RecyclerView? = null
    var progressBar: ProgressBar? = null
    var videoList: MutableList<Status> = ArrayList<Status>()
    var handler = Handler()
    var videoAdapter: VideoAdapter? = null
    var container: RelativeLayout? = null
    var swipeRefreshLayout: SwipeRefreshLayout? = null
    var messageTextView: TextView? = null
}