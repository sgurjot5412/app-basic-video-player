package com.pubscale.basicvideoplayer.domain.repository.VideoRepository

import com.pubscale.basicvideoplayer.domain.model.VideoState

interface VideoRepo {
    suspend fun fetchVideoUrl(): VideoState
}