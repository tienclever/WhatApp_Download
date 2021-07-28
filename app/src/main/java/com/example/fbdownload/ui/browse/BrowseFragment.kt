package com.example.fbdownload.ui.browse

import android.os.Handler
import android.os.StrictMode
import android.os.StrictMode.VmPolicy
import android.view.View
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.GridLayoutManager
import com.example.fbdownload.R
import com.example.fbdownload.data.model.Status
import com.example.fbdownload.databinding.FragmentBrowseBinding
import com.example.fbdownload.ui.adapter.FilesAdapter
import com.example.fbdownload.ui.base.BaseFragment
import com.example.fbdownload.ui.main.MainActivity
import com.example.fbdownload.utils.Common
import java.io.File
import java.util.Arrays

class BrowseFragment : BaseFragment<BrowseViewModel, FragmentBrowseBinding>() {
    override fun createViewModel(): Class<BrowseViewModel> = BrowseViewModel::class.java
    override fun getResourceLayout(): Int = R.layout.fragment_browse

    override fun initView() {
        viewModel.recyclerView = requireView().findViewById(R.id.recyclerViewFiles)
        viewModel.swipeRefreshLayout = requireView().findViewById(R.id.swipeRefreshLayoutFiles)
        viewModel.progressBar = requireView().findViewById(R.id.progressBar)
        viewModel.no_files_found = requireView().findViewById(R.id.no_files_found)

        val builder = VmPolicy.Builder()
        StrictMode.setVmPolicy(builder.build())

        viewModel.swipeRefreshLayout?.setColorSchemeColors(
            ContextCompat.getColor(requireActivity(), android.R.color.holo_orange_dark),
            ContextCompat.getColor(requireActivity(), android.R.color.holo_green_dark),
            ContextCompat.getColor(requireActivity(), R.color.colorPrimary),
            ContextCompat.getColor(requireActivity(), android.R.color.holo_blue_dark)
        )

        viewModel.swipeRefreshLayout?.setOnRefreshListener { this.getFiles() }

        viewModel.recyclerView?.setHasFixedSize(true)
        viewModel.recyclerView?.layoutManager = GridLayoutManager(getActivity(), Common.GRID_COUNT)

        getFiles()
    }

    override fun bindViewModel() {
    }

    private fun getFiles() {
        val app_dir = File(Common.APP_DIR)
        if (app_dir.exists()) {
            viewModel.no_files_found?.visibility = View.GONE
            Thread {
                val savedFiles: Array<File>? = app_dir.listFiles()
                viewModel.savedFilesList.clear()
                if (savedFiles != null && savedFiles.isNotEmpty()) {
                    Arrays.sort(savedFiles)
                    for (file in savedFiles) {
                        val status = Status(file, file.name, file.absolutePath)
                        viewModel.savedFilesList.add(status)
                    }
                    viewModel.handler.post(Runnable {
                        viewModel.filesAdapter = FilesAdapter(viewModel.savedFilesList)
                        viewModel.recyclerView?.adapter = viewModel.filesAdapter
                        viewModel.filesAdapter?.notifyDataSetChanged()
                        viewModel.progressBar?.visibility = View.GONE
                    })
                } else {
                    viewModel.handler.post(Runnable {
                        viewModel.progressBar?.visibility = View.GONE
                        viewModel.no_files_found?.visibility = View.VISIBLE
                    })
                }
                viewModel.swipeRefreshLayout?.isRefreshing = false
            }.start()
        } else {
            viewModel.no_files_found?.visibility = View.VISIBLE
            viewModel.progressBar?.visibility = View.GONE
        }
    }
}