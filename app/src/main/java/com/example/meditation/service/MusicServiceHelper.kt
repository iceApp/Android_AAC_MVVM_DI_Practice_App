package com.example.meditation.service

import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.ServiceConnection
import android.os.IBinder

class MusicServiceHelper(private val context: Context) {

    private var musicService: MusicService? = null

    fun bindService(){
        val serviceConnection = object : ServiceConnection {

            override fun onServiceConnected(name: ComponentName?, service: IBinder?) {
                var binder = service as MusicService.MusicBinder
                musicService = binder.getService()
            }
            override fun onServiceDisconnected(name: ComponentName?) {
            }
        }

        var intent = Intent(context, MusicService::class.java)
        context.bindService(intent, serviceConnection,Context.BIND_AUTO_CREATE)
    }

    fun startBgm(){
        musicService?.startBgm()
    }

    fun stopBgm(){
        musicService?.startBgm()
    }

    fun setVolume(){
        musicService?.setVolume()
    }

    fun ringFinalGong(){
        musicService?.ringFinalGong()
    }

}