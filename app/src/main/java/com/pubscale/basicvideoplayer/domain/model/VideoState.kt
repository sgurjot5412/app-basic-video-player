package com.pubscale.basicvideoplayer.domain.model

sealed class VideoState {
    object Loading : VideoState()
    data class Success(var url: String) : VideoState()
    data class Error(val message: String) : VideoState()
}