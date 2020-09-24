package com.jpdickerson.exoplayerkotlin

import android.content.Context
import android.media.session.MediaSession
import android.media.session.PlaybackState
import android.net.ConnectivityManager
import android.net.NetworkInfo
import android.net.Uri
import android.os.Build
import android.os.Bundle
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import com.google.android.exoplayer2.MediaItem
import com.google.android.exoplayer2.SimpleExoPlayer
import com.google.android.exoplayer2.source.DefaultMediaSourceFactory
import com.google.android.exoplayer2.source.MediaSource
import com.google.android.exoplayer2.source.ProgressiveMediaSource
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory
import com.google.android.exoplayer2.util.Util
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {
    private lateinit var mediaUri: Uri
    private lateinit var mediaItem: MediaItem
    private lateinit var mediaSource: MediaSource
    private lateinit var userAgent: DefaultDataSourceFactory

    @RequiresApi(Build.VERSION_CODES.M)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        exoplayer()
    }

    @RequiresApi(Build.VERSION_CODES.M)
    private fun exoplayer() {
//        val trackSelector = DefaultTrackSelector() // old way
//        val trackSelector = DefaultTrackSelector(baseContext) // requires a context, un-used
//        val exoPlayer = ExoPlayerFactory.newSimpleInstance(baseContext, trackSelector) // old way
        val exoPlayer =
            SimpleExoPlayer.Builder(baseContext).build() // uses builder instead of factory
        epPlayerView?.player = exoPlayer
        mediaSessionPlayback()

        if (internet()) {
            mediaUri = Uri.parse(baseContext.getString(R.string.fireside_s)) // via server
            userAgent = DefaultDataSourceFactory(
                baseContext,
                Util.getUserAgent(baseContext, R.string.jd_musicplayer.toString())
            )
            /*New way of setting mediaSource via mediaItem, using the builder pattern*/
            val mediaItem = MediaItem.Builder()
                .setUri(mediaUri)
//                    sets clipping of media item
                .setClipStartPositionMs(9000)
                .setClipEndPositionMs(23500)
                .build()
            mediaSource = DefaultMediaSourceFactory(userAgent).createMediaSource(mediaItem)
            exoPlayer.setMediaSource(mediaSource)
            exoPlayer.prepare()

        } else {
            mediaUri = Uri.parse(AssetUriConstants.fireside) // via file
            userAgent = DefaultDataSourceFactory(
                baseContext,
                Util.getUserAgent(baseContext, R.string.jd_musicplayer.toString())
            )
//            Simple mediaItem from a single Uri
            mediaItem = MediaItem.fromUri(mediaUri)
            mediaSource = ProgressiveMediaSource.Factory(userAgent).createMediaSource(mediaItem)
            exoPlayer.setMediaSource(mediaSource)
            exoPlayer.prepare()
        }
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

    @RequiresApi(Build.VERSION_CODES.M)
    private fun internet(): Boolean {
        val cm = baseContext.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val networkActive: NetworkInfo? = cm.activeNetworkInfo // deprecated api 29
        return networkActive?.isConnectedOrConnecting == true // deprecated api 29
    }
}