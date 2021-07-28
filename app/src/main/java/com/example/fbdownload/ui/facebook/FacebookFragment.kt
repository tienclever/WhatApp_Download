package com.example.fbdownload.ui.facebook

import android.os.StrictMode
import android.os.StrictMode.VmPolicy
import android.view.View
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.GridLayoutManager
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout.OnRefreshListener
import com.example.fbdownload.R
import com.example.fbdownload.data.model.Status
import com.example.fbdownload.databinding.FragmentFacebookBinding
import com.example.fbdownload.ui.adapter.ImageAdapter
import com.example.fbdownload.ui.base.BaseFragment
import com.example.fbdownload.utils.Common
import java.io.File
import java.util.Arrays

class FacebookFragment : BaseFragment<FacebookViewModel, FragmentFacebookBinding>() {
    override fun createViewModel(): Class<FacebookViewModel> = FacebookViewModel::class.java
    override fun getResourceLayout(): Int = R.layout.fragment_images

    override fun initView() {
        viewModel.recyclerView = requireView().findViewById(R.id.recyclerViewImage)
        viewModel.progressBar = requireView().findViewById(R.id.prgressBarImage)
        viewModel.container = requireView().findViewById(R.id.image_container)
        viewModel.swipeRefreshLayout = requireView().findViewById(R.id.swipeRefreshLayout)
        viewModel.messageTextView = requireView().findViewById(R.id.messageTextImage)
        val builder = VmPolicy.Builder()
        StrictMode.setVmPolicy(builder.build())

        viewModel.swipeRefreshLayout?.setColorSchemeColors(
            ContextCompat.getColor(requireActivity(), android.R.color.holo_orange_dark),
            ContextCompat.getColor(requireActivity(), android.R.color.holo_green_dark),
            ContextCompat.getColor(requireActivity(), R.color.colorPrimary),
            ContextCompat.getColor(requireActivity(), android.R.color.holo_blue_dark)
        )
        viewModel.swipeRefreshLayout?.setOnRefreshListener(OnRefreshListener { this.getStatus() })
        viewModel.recyclerView?.setHasFixedSize(true)
        viewModel.recyclerView?.setLayoutManager(GridLayoutManager(getActivity(), Common.GRID_COUNT))
        getStatus()
    }

    override fun bindViewModel() {
    }

    private fun getStatus() {
        when {
            Common.STATUS_DIRECTORY.exists() -> {
                execute(Common.STATUS_DIRECTORY)
            }
            Common.STATUS_DIRECTORY_NEW.exists() -> {
                execute(Common.STATUS_DIRECTORY_NEW)
            }
            else -> {
                viewModel.messageTextView!!.visibility = View.VISIBLE
                viewModel.messageTextView?.text = getString(R.string.cant_find_whatsapp_dir)
                Toast.makeText(getActivity(), getString(R.string.cant_find_whatsapp_dir), Toast.LENGTH_SHORT).show()
                viewModel.swipeRefreshLayout!!.isRefreshing = false
            }
        }
    }

    private fun execute(wAFolder: File) {
        Thread {
            var statusFiles = wAFolder.listFiles()
            viewModel.imagesList.clear()
            if (statusFiles != null && statusFiles.isNotEmpty()) {
                Arrays.sort(statusFiles)
                for (file in statusFiles) {
                    val status = Status(file, file.name, file.absolutePath)
                    if (!status.isVideo && status.title.endsWith(".jpg")) {
                        viewModel.imagesList.add(status)
                    }
                }
                viewModel.handler.post {
                    if (viewModel.imagesList.size <= 0) {
                        viewModel.messageTextView!!.visibility = View.VISIBLE
                        viewModel.messageTextView?.setText(R.string.no_files_found)
                    } else {
                        viewModel.messageTextView!!.visibility = View.GONE
                        viewModel.messageTextView!!.text = ""
                    }
                    viewModel.imageAdapter = ImageAdapter(viewModel.imagesList, viewModel.container!!)
                    viewModel.recyclerView!!.adapter = viewModel.imageAdapter
                    viewModel.imageAdapter?.notifyDataSetChanged()
                    viewModel.progressBar!!.visibility = View.GONE
                }
            } else {
                viewModel.handler.post {
                    viewModel.progressBar!!.visibility = View.GONE
                    viewModel.messageTextView!!.visibility = View.VISIBLE
                    viewModel.messageTextView?.setText(R.string.no_files_found)
                    Toast.makeText(getActivity(), getString(R.string.no_files_found), Toast.LENGTH_SHORT).show()
                }
            }
            viewModel.swipeRefreshLayout!!.isRefreshing = false
        }.start()
    }
}
