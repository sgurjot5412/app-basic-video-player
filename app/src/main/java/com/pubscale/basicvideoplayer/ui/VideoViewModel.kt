package com.pubscale.basicvideoplayer.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.pubscale.basicvideoplayer.domain.model.VideoState
import com.pubscale.basicvideoplayer.domain.usecase.VideoUseCase.GetVideoUrlUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class VideoViewModel @Inject constructor(
    private val getVideoUrlUseCase: GetVideoUrlUseCase
) : ViewModel() {
    private val _videoState = MutableStateFlow<VideoState>(VideoState.Loading)
    val videoState: StateFlow<VideoState> get() = _videoState

    // fetching video url following clean architecture refering here to usecase
    fun fetchVideoUrl() {
        viewModelScope.launch {
            _videoState.value = getVideoUrlUseCase()
        }
    }
}