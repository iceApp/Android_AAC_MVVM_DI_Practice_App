package com.example.meditation.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.meditation.model.UserSettings
import com.example.meditation.model.UserSettingsRepository

class MainViewModel: ViewModel() {


    val msgUpperSmall = MutableLiveData<String>()
    val msgLowerLarge = MutableLiveData<String>()
    var themePicFileResId = MutableLiveData<Int>()
    var txtTheme = MutableLiveData<String>()
    var txtLevel = MutableLiveData<String>()

    var remainedTimeSeconds = MutableLiveData<Int>()
    var displayTimeSeconds = MutableLiveData<String>()
    
    val playStatus = MutableLiveData<Int>()

    private val userSettingRepository =
        UserSettingsRepository()
    private lateinit var userSettings: UserSettings
    fun initParameters() {
        userSettings = userSettingRepository.loadUserSettings()
        msgUpperSmall.value = ""
        msgLowerLarge.value = ""
        themePicFileResId.value = userSettings.themeResId
        txtTheme.value = userSettings.themeName
        txtLevel.value = userSettings.levelName
        remainedTimeSeconds.value = userSettings.time * 60
        displayTimeSeconds.value = changeTimerFormat(remainedTimeSeconds.value!!)
        playStatus.value = 0
    }

    private fun changeTimerFormat(timeSeconds: Int): String {

        val mm = timeSeconds / 60; // 分
        val ss = timeSeconds % 60; // 秒
        return String.format("%1$02d:%2$02d", mm, ss)
//        return  TimeUnit.MINUTES.convert(timeSeconds.toLong(),TimeUnit.MILLISECONDS).toString()
    }

    fun setLevel(selectedItemId: Int) {
        txtLevel.value = userSettingRepository.setLevel(selectedItemId)
    }

    fun setTime(selectedItemId: Int) {
        remainedTimeSeconds.value = userSettingRepository.setTime(selectedItemId) * 60
        displayTimeSeconds.value = changeTimerFormat(remainedTimeSeconds.value!!)

    }
}