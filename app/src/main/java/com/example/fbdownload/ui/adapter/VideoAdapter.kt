package com.example.fbdownload.ui.adapter

import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.media.MediaPlayer
import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.widget.Button
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.MediaController
import android.widget.RelativeLayout
import android.widget.VideoView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.fbdownload.R
import com.example.fbdownload.data.model.Status
import com.example.fbdownload.utils.Common

class VideoAdapter(videoList: List<Status>, container: RelativeLayout) : RecyclerView.Adapter<ItemViewHolder>() {
    private val videoList: List<Status> = videoList
    private var context: Context? = null
    private val container: RelativeLayout = container

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder {
        context = parent.context
        val view: View = LayoutInflater.from(context).inflate(R.layout.item_status, parent, false)
        return ItemViewHolder(view)
    }

    override fun onBindViewHolder(holder: ItemViewHolder, position: Int) {
        val status: Status = videoList[position]
        Glide.with(context!!).asBitmap().load(status.file).into(holder.imageView)
        holder.share.setOnClickListener {
            val shareIntent = Intent(Intent.ACTION_SEND)
            shareIntent.type = "image/mp4"
            shareIntent.putExtra(Intent.EXTRA_STREAM, Uri.parse("file://" + status.file.absolutePath))
            context!!.startActivity(Intent.createChooser(shareIntent, "Share image"))
        }
        val inflater = LayoutInflater.from(context)
        val view1: View = inflater.inflate(R.layout.view_video_full_screen, null)
        holder.imageView.setOnClickListener {
            val alertDg = AlertDialog.Builder(context)
            val mediaControls = view1.findViewById<FrameLayout>(R.id.videoViewWrapper)
            if (view1.parent != null) {
                (view1.parent as ViewGroup).removeView(view1)
            }
            alertDg.setView(view1)
            val videoView = view1.findViewById<VideoView>(R.id.video_full)
            val mediaController = MediaController(context, false)
            videoView.setOnPreparedListener { mp: MediaPlayer ->
                mp.start()
                mediaController.show(0)
                mp.isLooping = true
            }
            videoView.setMediaController(mediaController)
            mediaController.setMediaPlayer(videoView)
            videoView.setVideoURI(Uri.fromFile(status.file))
            videoView.requestFocus()
            (mediaController.parent as ViewGroup).removeView(mediaController)
            if (mediaControls.parent != null) {
                mediaControls.removeView(mediaController)
            }
            mediaControls.addView(mediaController)
            val alert2 = alertDg.create()
            alert2.window!!.attributes.windowAnimations = R.style.SlidingDialogAnimation
            alert2.requestWindowFeature(Window.FEATURE_NO_TITLE)
            alert2.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
            alert2.show()
        }
        holder.save.setOnClickListener { Common.copyFile(status, context!!, container) }
    }

    override fun getItemCount(): Int {
        return videoList.size
    }
}
class ItemViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    var save: Button = itemView.findViewById(R.id.save)
    var share: Button = itemView.findViewById(R.id.share)
    var imageView: ImageView = itemView.findViewById(R.id.ivThumbnail)
}