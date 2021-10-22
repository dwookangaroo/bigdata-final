package com.kimjjing1004.seoulapplication.main.home

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.kimjjing1004.seoulapplication.R

class ImageAdapter( val context: Context) : RecyclerView.Adapter<ImageAdapter.ViewHolder>() {

    var images = mutableListOf<Image>()


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.activity_recyclerview, parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int = images.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(images[position])
    }

    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {

        private val resultTitle: TextView = itemView.findViewById(R.id.landmarkTitle)
        private val resultImage: ImageView = itemView.findViewById(R.id.landmarkImg)

        fun bind(item: Image) {
            resultTitle.text = item.title
            Glide.with(itemView).load(item.img).into(resultImage)

        }

    }
}
