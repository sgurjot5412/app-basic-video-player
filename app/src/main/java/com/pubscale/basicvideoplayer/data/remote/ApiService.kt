package com.pubscale.basicvideoplayer.data.remote

import com.pubscale.basicvideoplayer.data.model.VideoResponse
import retrofit2.http.GET

interface ApiService {
    @GET("greedyraagava/test/refs/heads/main/video_url.json")
    suspend fun getVideoUrl(): VideoResponse
}