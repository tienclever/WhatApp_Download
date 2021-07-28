package com.example.fbdownload.ui.adapter

import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.widget.Button
import android.widget.ImageView
import android.widget.RelativeLayout
import androidx.recyclerview.widget.RecyclerView
import com.example.fbdownload.R
import com.example.fbdownload.data.model.Status
import com.example.fbdownload.utils.Common
import com.squareup.picasso.Picasso

class ImageAdapter(imagesList: List<Status>, container: RelativeLayout) : RecyclerView.Adapter<ImageAdapter.ItemViewHolder>() {
    private val imagesList: List<Status> = imagesList
    private lateinit var context: Context
    private val container: RelativeLayout = container

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder {
        context = parent.context
        val view: View = LayoutInflater.from(context).inflate(R.layout.item_status, parent, false)
        return ItemViewHolder(view)
    }

    class ItemViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var save: Button = itemView.findViewById(R.id.save)
        var share: Button = itemView.findViewById(R.id.share)
        var imageView: ImageView = itemView.findViewById(R.id.ivThumbnail)
    }

    override fun onBindViewHolder(holder: ItemViewHolder, position: Int) {
        val status: Status = imagesList[position]
        Picasso.get().load(status.file).into(holder.imageView)
        holder.save.setOnClickListener { Common.copyFile(status, context, container) }
        holder.share.setOnClickListener {
            val shareIntent = Intent(Intent.ACTION_SEND)
            shareIntent.type = "image/jpg"
            shareIntent.putExtra(Intent.EXTRA_STREAM, Uri.parse("file://" + status.file.getAbsolutePath()))
            context!!.startActivity(Intent.createChooser(shareIntent, "Share image"))
        }
        holder.imageView.setOnClickListener {
            val alertD = AlertDialog.Builder(context)
            val inflater = LayoutInflater.from(context)
            val view: View = inflater.inflate(R.layout.view_image_full_screen, null)
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

    override fun getItemCount(): Int {
        return imagesList.size
    }
}