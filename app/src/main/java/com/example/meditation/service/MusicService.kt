package com.example.meditation.service

import android.app.Service
import android.content.Intent
import android.os.Binder
import android.os.IBinder
import com.example.meditation.R
import com.example.meditation.model.UserSettingsRepository
import com.example.meditation.util.NO_BGM
import com.google.android.exoplayer2.ExoPlayerFactory
import com.google.android.exoplayer2.Player
import com.google.android.exoplayer2.SimpleExoPlayer
import com.google.android.exoplayer2.extractor.mp3.Mp3Extractor
import com.google.android.exoplayer2.source.ExtractorMediaSource
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector
import com.google.android.exoplayer2.upstream.DataSource
import com.google.android.exoplayer2.upstream.DataSpec
import com.google.android.exoplayer2.upstream.RawResourceDataSource

class MusicService : Service() {

    private val binder: IBinder = MusicBinder()

    inner class MusicBinder: Binder(){
        fun getService(): MusicService {
            return this@MusicService
        }
    }

    private var bellsPlayer: SimpleExoPlayer? = null
    private var bgmPlayer: SimpleExoPlayer? = null

    private var rawResourceDataSourceBgm: RawResourceDataSource? = null
    private var rawResourceDataSourceBell: RawResourceDataSource? = null

    override fun onBind(intent: Intent): IBinder {
        bellsPlayer = ExoPlayerFactory.newSimpleInstance(this, DefaultTrackSelector())
        bgmPlayer = ExoPlayerFactory.newSimpleInstance(this, DefaultTrackSelector())
        rawResourceDataSourceBgm = RawResourceDataSource(this)
        rawResourceDataSourceBell = RawResourceDataSource(this)

        return binder
    }

    override fun onUnbind(intent: Intent?): Boolean {
        return super.onUnbind(intent)
    }

    fun startBgm(){
        val userSettingsRepository = UserSettingsRepository()
        val bellsSoundId = when(userSettingsRepository.loadUserSettings().levelId){
            0 -> R.raw.bells_easy
            1 -> R.raw.bells_normal
            2 -> R.raw.bells_mid
            3 -> R.raw.bells_advanced
            else -> 0
        }
        val themeSoundId = userSettingsRepository.loadUserSettings().themeSoundId

        bellsPlayer?.let { startSound(bellsPlayer!!, bellsSoundId, rawResourceDataSourceBell!!) }
        if( themeSoundId != NO_BGM ) bgmPlayer?.let { startSound(bgmPlayer!!, themeSoundId, rawResourceDataSourceBgm!!)}
    }

    private fun startSound(exoPlayer: SimpleExoPlayer, soundId: Int, rawResourceDataSource: RawResourceDataSource){
        val dataSourceUri = RawResourceDataSource.buildRawResourceUri(soundId)
        val dataSpec = DataSpec(dataSourceUri)
        rawResourceDataSource.open(dataSpec)
        val dataSourceFactory = DataSource.Factory { return@Factory rawResourceDataSource }
        val mediaSource = ExtractorMediaSource.Factory(dataSourceFactory)
            .setExtractorsFactory(Mp3Extractor.FACTORY)
            .createMediaSource(dataSourceUri)
        exoPlayer.apply {
            prepare(mediaSource)
            repeatMode = Player.REPEAT_MODE_ALL
            playWhenReady = true
        }
    }

    fun stopBgm(){

    }

    fun setVolume(){

    }

    fun ringFinalGong(){

    }

    private fun releaseExoPlayer(){
        bgmPlayer?.release()
        bellsPlayer?.release()
        rawResourceDataSourceBgm?.close()
        rawResourceDataSourceBell?.close()

    }
}
