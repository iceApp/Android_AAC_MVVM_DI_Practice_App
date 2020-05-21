package com.example.meditation.view.main

import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.ServiceConnection
import android.os.Bundle
import android.os.IBinder
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.activity.viewModels
import androidx.lifecycle.Observer
import com.example.meditation.R
import com.example.meditation.service.MusicService
import com.example.meditation.service.MusicServiceHelper
import com.example.meditation.view.dialog.LevelSelectDialog
import com.example.meditation.view.dialog.ThemeSelectDialog
import com.example.meditation.view.dialog.TimeSelectDialog
import com.example.meditation.viewmodel.MainViewModel
import kotlinx.android.synthetic.main.activity_main.*
import com.example.meditation.util.FragmentTag
import com.example.meditation.util.PlayStatus

class MainActivity : AppCompatActivity() {

    private val viewModel: MainViewModel by viewModels()
    private var musicServiceHelper: MusicServiceHelper? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        setTheme(R.style.AppTheme)
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        if (savedInstanceState == null){
            supportFragmentManager.beginTransaction()
                .replace(
                    R.id.screen_container,
                    MainFragment()
                )
                .commit()
        }

        observeViewModel()

        btmNavi.setOnNavigationItemSelectedListener { item ->
            when (item.itemId) {
                R.id.item_select_level -> {
                    LevelSelectDialog().show(supportFragmentManager, FragmentTag.LEVEL_SELECT.name)
                    true
                }
                R.id.item_select_theme -> {
                    ThemeSelectDialog().show(supportFragmentManager, FragmentTag.THEME_SELECT.name)
                    true
                }

                R.id.item_select_time -> {
                    TimeSelectDialog().show(supportFragmentManager, FragmentTag.TIME_SELECT.name)
                    true
                }
                else -> {false}
            }
        }

        musicServiceHelper = MusicServiceHelper(this)
        musicServiceHelper?.bindService()

    }


    override fun onBackPressed() {
        super.onBackPressed()
        musicServiceHelper?.stopBgm()
        finish()
    }

    private fun observeViewModel() {
        viewModel.playStatus.observe(this, Observer { status ->
            // TODO: パターンをまとめる
            when (status){
                PlayStatus.BEFORE_START -> {
                    btmNavi.visibility = View.VISIBLE
                }
                PlayStatus.ON_START -> {
                    btmNavi.visibility = View.INVISIBLE
                }
                PlayStatus.RUNNING -> {
                    btmNavi.visibility = View.INVISIBLE
                    musicServiceHelper?.stopBgm()
                }
                PlayStatus.PAUSE -> {
                    btmNavi.visibility = View.INVISIBLE
                    musicServiceHelper?.startBgm()
                }
                PlayStatus.RE_RUNNING -> {
                    btmNavi.visibility = View.INVISIBLE
                    musicServiceHelper?.startBgm()
                }
                PlayStatus.END -> {
                    musicServiceHelper?.stopBgm()
                    musicServiceHelper?.ringFinalGong()
                }
            }
        })
    }
}
