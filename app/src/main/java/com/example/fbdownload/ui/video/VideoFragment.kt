package com.example.fbdownload.ui.video

import android.os.StrictMode
import android.os.StrictMode.VmPolicy
import android.view.View
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.GridLayoutManager
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout.OnRefreshListener
import com.example.fbdownload.R
import com.example.fbdownload.data.model.Status
import com.example.fbdownload.databinding.FragmentVideosBinding
import com.example.fbdownload.ui.adapter.VideoAdapter
import com.example.fbdownload.ui.base.BaseFragment
import com.example.fbdownload.utils.Common
import java.io.File
import java.util.Arrays

class VideoFragment: BaseFragment<VideoViewModel, FragmentVideosBinding>() {
    override fun createViewModel(): Class<VideoViewModel> = VideoViewModel::class.java

    override fun getResourceLayout(): Int = R.layout.fragment_videos

    override fun initView() {
        viewModel.recyclerView = requireView().findViewById(R.id.recyclerViewVideo)
        viewModel.progressBar = requireView().findViewById(R.id.prgressBarVideo)
        viewModel.container = requireView().findViewById(R.id.videos_container)
        viewModel.swipeRefreshLayout = requireView().findViewById(R.id.swipeRefreshLayout)
        viewModel.messageTextView = requireView().findViewById(R.id.messageTextVideo)

        val builder = VmPolicy.Builder()
        StrictMode.setVmPolicy(builder.build())

        viewModel.swipeRefreshLayout!!.setColorSchemeColors(
            ContextCompat.getColor(requireActivity(), android.R.color.holo_orange_dark),
            ContextCompat.getColor(requireActivity(), android.R.color.holo_green_dark),
            ContextCompat.getColor(requireActivity(), R.color.colorPrimary),
            ContextCompat.getColor(requireActivity(), android.R.color.holo_blue_dark)
        )

        viewModel.swipeRefreshLayout!!.setOnRefreshListener(OnRefreshListener { this.getStatus() })

        viewModel.recyclerView!!.setHasFixedSize(true)
        viewModel.recyclerView!!.setLayoutManager(GridLayoutManager(getActivity(), Common.GRID_COUNT))

        getStatus()
    }

    override fun bindViewModel() {
    }
    private fun getStatus() {
        if (Common.STATUS_DIRECTORY.exists()) {
            execute(Common.STATUS_DIRECTORY)
        } else if (Common.STATUS_DIRECTORY_NEW.exists()) {
            execute(Common.STATUS_DIRECTORY_NEW)
        } else {
            viewModel.messageTextView?.setVisibility(View.VISIBLE)
            viewModel.messageTextView?.setText(R.string.cant_find_whatsapp_dir)
            Toast.makeText(getActivity(), getString(R.string.cant_find_whatsapp_dir), Toast.LENGTH_SHORT).show()
            viewModel.swipeRefreshLayout?.setRefreshing(false)
        }
    }
    private fun execute(waFolder: File) {
        Thread {
            val statusFiles = waFolder.listFiles()
            viewModel.videoList.clear()
            if (statusFiles != null && statusFiles.size > 0) {
                Arrays.sort(statusFiles)
                for (file in statusFiles) {
                    val status = Status(file, file.name, file.absolutePath)
                    if (status.isVideo) {
                        viewModel.videoList.add(status)
                    }
                }
                viewModel.handler.post(Runnable {
                    if (viewModel.videoList.size <= 0) {
                        viewModel.messageTextView?.setVisibility(View.VISIBLE)
                        viewModel.messageTextView?.setText(R.string.no_files_found)
                    } else {
                        viewModel.messageTextView?.setVisibility(View.GONE)
                        viewModel.messageTextView?.setText("")
                    }
                    viewModel.videoAdapter = viewModel.container?.let { VideoAdapter(viewModel.videoList, it) }
                    viewModel.recyclerView?.setAdapter(viewModel.videoAdapter)
                    viewModel.videoAdapter?.notifyDataSetChanged()
                    viewModel.progressBar?.setVisibility(View.GONE)
                })
            } else {
                viewModel.handler.post(Runnable {
                    viewModel.progressBar?.setVisibility(View.GONE)
                    viewModel.messageTextView?.setVisibility(View.VISIBLE)
                    viewModel.messageTextView?.setText(R.string.no_files_found)
                    Toast.makeText(getActivity(), getString(R.string.no_files_found), Toast.LENGTH_SHORT).show()
                })
            }
            viewModel.swipeRefreshLayout?.setRefreshing(false)
        }.start()
    }
}