package com.pubscale.basicvideoplayer.data.repository

import com.pubscale.basicvideoplayer.data.remote.ApiService
import com.pubscale.basicvideoplayer.domain.model.VideoState
import com.pubscale.basicvideoplayer.domain.repository.VideoRepository.VideoRepo
import javax.inject.Inject

class VideoRepositoryImpl @Inject constructor(
    private val apiService: ApiService
) : VideoRepo {
    override suspend fun fetchVideoUrl(): VideoState {
        return try {
            val response = apiService.getVideoUrl()
            VideoState.Success(response.url)
        } catch (e: Exception) {
            VideoState.Error("Network error: ${e.message}")
        }
    }
}