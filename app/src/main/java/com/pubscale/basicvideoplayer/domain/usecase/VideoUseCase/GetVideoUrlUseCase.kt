package com.pubscale.basicvideoplayer.domain.usecase.VideoUseCase

import com.pubscale.basicvideoplayer.domain.model.VideoState
import com.pubscale.basicvideoplayer.domain.repository.VideoRepository.VideoRepo
import javax.inject.Inject

// this is an additional layer over MVVM architecture which ensures that the buisness logic stays at a different level
class GetVideoUrlUseCase @Inject constructor(
    private val repository: VideoRepo
) {
    suspend operator fun invoke(): VideoState {
        return repository.fetchVideoUrl()
    }
}