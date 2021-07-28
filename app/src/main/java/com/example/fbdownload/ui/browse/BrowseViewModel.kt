package com.example.fbdownload.ui.browse

import android.os.Handler
import android.widget.ProgressBar
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.example.fbdownload.data.model.Status
import com.example.fbdownload.ui.adapter.FilesAdapter
import com.example.fbdownload.ui.base.BaseViewModel
import java.util.ArrayList

class BrowseViewModel: BaseViewModel() {
    var recyclerView: RecyclerView? = null
    var progressBar: ProgressBar? = null
    var savedFilesList: ArrayList<Status> = ArrayList<Status>()
    var handler = Handler()
    var filesAdapter: FilesAdapter? = null
    var swipeRefreshLayout: SwipeRefreshLayout? = null
    var no_files_found: TextView? = null
}