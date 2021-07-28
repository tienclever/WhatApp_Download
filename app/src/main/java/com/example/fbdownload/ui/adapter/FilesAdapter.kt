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
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.MediaController
import android.widget.Toast
import android.widget.VideoView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.fbdownload.R
import com.example.fbdownload.data.model.Status
import com.squareup.picasso.Picasso

class FilesAdapter(imagesList: List<Status>) : RecyclerView.Adapter<ItemViewHolder>() {
    private val imagesList: MutableList<Status> = imagesList as MutableList<Status>
    private var context: Context? = null
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder {
        context = parent.context
        val view: View = LayoutInflater.from(context).inflate(R.layout.item_saved_files, parent, false)
        return ItemViewHolder(view)
    }

    override fun onBindViewHolder(holder: ItemViewHolder, position: Int) {
        holder.save.background = (ContextCompat.getDrawable(context!!, R.drawable.ic_baseline_delete_24))
        holder.share.visibility = View.VISIBLE
        holder.save.visibility = View.VISIBLE
        val status: Status = imagesList[position]
        if (status.isVideo) Glide.with(context!!).asBitmap().load(status.file).into(holder.imageView) else Picasso.get()
            .load(status.file).into(holder.imageView)
        holder.save.setOnClickListener {
            if (status.file.delete()) {
                imagesList.removeAt(position)
                notifyDataSetChanged()
                Toast.makeText(context, "File Deleted", Toast.LENGTH_SHORT).show()
            } else Toast.makeText(context, "Unable to Delete File", Toast.LENGTH_SHORT).show()
        }
        holder.share.setOnClickListener {
            val shareIntent = Intent(Intent.ACTION_SEND)
            if (status.isVideo) shareIntent.type = "image/mp4" else shareIntent.type = "image/jpg"
            shareIntent.putExtra(Intent.EXTRA_STREAM, Uri.parse("file://" + status.file.getAbsolutePath()))
            context!!.startActivity(Intent.createChooser(shareIntent, "Share image"))
        }
        val inflater = LayoutInflater.from(context)
        val view1: View = inflater.inflate(R.layout.view_video_full_screen, null)
        holder.imageView.setOnClickListener {
            if (status.isVideo) {
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
            } else {
                val alertD = AlertDialog.Builder(context)
                val inflater1 = LayoutInflater.from(context)
                val view: View = inflater1.inflate(R.layout.view_image_full_screen, null)
                alertD.setView(view)
                val imageView = view.findViewById<ImageView>(R.id.img)
                Picasso.get().load(status.file).into(imageView)
                val alert = alertD.create()
                alert.window!!.attributes.windowAnimations = R.style.SlidingDialogAnimation
                alert.requestWindowFeature(Window.FEATURE_NO_TITLE)
                alert.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
                alert.show()
            }
        }
    }

    override fun getItemCount(): Int {
        return imagesList.size
    }
}