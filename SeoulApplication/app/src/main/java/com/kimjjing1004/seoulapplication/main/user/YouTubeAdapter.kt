package com.kimjjing1004.seoulapplication.main.user

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup


import androidx.recyclerview.widget.RecyclerView
import com.kimjjing1004.seoulapplication.R

import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.YouTubePlayer
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.listeners.AbstractYouTubePlayerListener



class YouTubeAdapter(contents: List<YouTubeContent>) :
    RecyclerView.Adapter<YouTubeViewHolder>() {
    private val contents: List<YouTubeContent>?
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): YouTubeViewHolder {
        val view: View = LayoutInflater.from(parent.context)
            .inflate(R.layout.card_youtube_content, parent, false)
        return YouTubeViewHolder(view)
    }

    override fun onBindViewHolder(holder: YouTubeViewHolder, @SuppressLint("RecyclerView") position: Int) {
        val result = holder.youTubePlayerView
        result.enableAutomaticInitialization = false
//        enableAutomaticInitializationnable(false)
        result.initialize(object : AbstractYouTubePlayerListener() {
            override fun onReady(youTubePlayer: YouTubePlayer) {
                youTubePlayer.loadVideo(contents!![position].videoId, 0f)
            }
        }, false)
    }

    override fun getItemCount(): Int {
        return contents?.size ?: 0
    }

    init {
        this.contents = contents
    }
}