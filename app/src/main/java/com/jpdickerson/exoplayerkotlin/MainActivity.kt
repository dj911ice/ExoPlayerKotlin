package com.jpdickerson.exoplayerkotlin

import android.media.session.MediaSession
import android.media.session.PlaybackState
import android.net.Uri
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.google.android.exoplayer2.ExoPlayerFactory
import com.google.android.exoplayer2.source.ProgressiveMediaSource
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory
import com.google.android.exoplayer2.util.Util
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val trackSelector = DefaultTrackSelector()
        val exoPlayer = ExoPlayerFactory.newSimpleInstance(baseContext,trackSelector)

        epPlayerView?.player = exoPlayer

        val mediaSession = MediaSession(baseContext, "MainActivity")

        val playbackStateBuilder = PlaybackState.Builder()

        playbackStateBuilder.setActions(PlaybackState.ACTION_PLAY)
        playbackStateBuilder.setActions(PlaybackState.ACTION_PAUSE)
        playbackStateBuilder.setActions(PlaybackState.ACTION_FAST_FORWARD)

        mediaSession.setPlaybackState(playbackStateBuilder.build())
        mediaSession.isActive = true

        val mediaUri = Uri.parse(AssetUriConstants.fireside)

        val userAgent = DefaultDataSourceFactory(baseContext,
            Util.getUserAgent(baseContext, "ExoPlayer"))

        val mediaSource = ProgressiveMediaSource.Factory(userAgent).createMediaSource(mediaUri)

        exoPlayer?.prepare(mediaSource)
    }
}