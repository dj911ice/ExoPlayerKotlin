package com.jpdickerson.exoplayerkotlin

import android.media.session.MediaSession
import android.media.session.PlaybackState
import android.net.Uri
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.google.android.exoplayer2.SimpleExoPlayer
import com.google.android.exoplayer2.source.ProgressiveMediaSource
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory
import com.google.android.exoplayer2.util.Util
import kotlinx.android.synthetic.main.activity_main.*


class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)


        exoplayer()
    }

    private fun exoplayer(){
//        val trackSelector = DefaultTrackSelector() // old way
//        val trackSelector = DefaultTrackSelector(baseContext) // requires a context, un-used
//        val exoPlayer = ExoPlayerFactory.newSimpleInstance(baseContext, trackSelector) // old way
        val exoPlayer = SimpleExoPlayer.Builder(baseContext).build() // uses builder instead of factory
        epPlayerView?.player = exoPlayer
        mediaSessionPlayback()

//        val mediaUri = Uri.parse(AssetUriConstants.fireside) // via file
        val mediaUri = Uri.parse(baseContext.getString(R.string.fireside_s)) // via server
        val userAgent = DefaultDataSourceFactory(
            baseContext,
            Util.getUserAgent(baseContext, R.string.jd_musicplayer.toString())
        )

        val mediaSource = ProgressiveMediaSource.Factory(userAgent).createMediaSource(mediaUri)

        exoPlayer.prepare(mediaSource)


    }
    private fun mediaSessionPlayback(){
        val mediaSession = MediaSession(baseContext, "MainActivity")

        val playbackStateBuilder = PlaybackState.Builder()

        playbackStateBuilder.setActions(PlaybackState.ACTION_PLAY)
        playbackStateBuilder.setActions(PlaybackState.ACTION_PAUSE)
        playbackStateBuilder.setActions(PlaybackState.ACTION_FAST_FORWARD)

        mediaSession.setPlaybackState(playbackStateBuilder.build())
        mediaSession.isActive = true
    }
}