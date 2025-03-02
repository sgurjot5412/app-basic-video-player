package com.pubscale.basicvideoplayer.ui

import android.app.PictureInPictureParams
import android.content.res.Configuration
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.util.Rational
import android.view.View
import android.widget.ProgressBar
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.media3.common.MediaItem
import androidx.media3.exoplayer.ExoPlayer
import androidx.media3.ui.PlayerView
import com.pubscale.basicvideoplayer.R
import com.pubscale.basicvideoplayer.domain.model.VideoState
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    private var player: ExoPlayer? = null
    private var playerView: PlayerView? = null
    private val viewModel: VideoViewModel by viewModels()
    private lateinit var progress: ProgressBar

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        // initially adding a progress bar signifying that network call is still going
        progress = findViewById(R.id.progressbar)

        // launched a coroutine for getting video url
        lifecycleScope.launch {
            viewModel.videoState.collect { state ->
                when (state) {
                    is VideoState.Loading -> {
                        progress.visibility = View.VISIBLE
                    }
                    // if the case is success it will set the video url it got from the internet
                    is VideoState.Success -> {
                        progress.visibility = View.GONE
                        val mediaItem = MediaItem.fromUri(Uri.parse(state.url))
                        setupExoPLayer(mediaItem)
                    }
                    // if not it will set the default video being played in the video
                    is VideoState.Error -> {
                        progress.visibility = View.GONE
                        val videoUri =
                            Uri.parse("android.resource://" + packageName + "/" + R.raw.sample_video)
                        val mediaItem = MediaItem.fromUri(videoUri)
                        setupExoPLayer(mediaItem)
                        Toast.makeText(
                            applicationContext,
                            "Something went wrong",
                            Toast.LENGTH_SHORT
                        )
                    }
                }
            }
        }

        // call to fetch the video url
        viewModel.fetchVideoUrl()
    }

    private fun setupExoPLayer(mediaItem: MediaItem) {
        playerView = findViewById(R.id.player_view)
        playerView?.visibility = View.VISIBLE
        player = ExoPlayer.Builder(this).build()
        playerView?.player = player
        player?.setMediaItem(mediaItem)
        player?.prepare()
        player?.playWhenReady = true
    }

    override fun onUserLeaveHint() {
        super.onUserLeaveHint()
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            // Puts the activity in picture-in-picture mode
            enterPictureInPictureMode(
                PictureInPictureParams.Builder()
                    .setAspectRatio(Rational(16, 9)) // Set aspect ratio (16:9 for video)
                    .build()
            )
        }
    }

    // gets triggered when pip state changes
    override fun onPictureInPictureModeChanged(
        isInPiP: Boolean,
        newConfig: Configuration
    ) {
        super.onPictureInPictureModeChanged(isInPiP, newConfig)
        if (isInPiP) {
            // Hide UI elements when in PiP mode
            playerView?.useController = false
        } else {
            // Restore UI elements when exiting PiP mode
            playerView?.useController = true
        }
    }

    // if activity gets paused due to pip still play the video else stop the video
    override fun onPause() {
        super.onPause()
        if (isInPictureInPictureMode) {
            // Continue video playback in PiP mode
            player?.playWhenReady = true
        } else {
            player?.pause()
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        player?.release()
        player = null
    }

    override fun onRestart() {
        super.onRestart()
        player?.play()
    }

    override fun onStop() {
        super.onStop()
        player?.pause()
    }
}