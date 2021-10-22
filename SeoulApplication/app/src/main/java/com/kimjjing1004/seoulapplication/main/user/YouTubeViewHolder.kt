package com.kimjjing1004.seoulapplication.main.user

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.kimjjing1004.seoulapplication.R
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.views.YouTubePlayerView

class YouTubeViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    @JvmField
    var youTubePlayerView: YouTubePlayerView

    init {
        youTubePlayerView = itemView.findViewById(R.id.card_content_player_view)
    }
}