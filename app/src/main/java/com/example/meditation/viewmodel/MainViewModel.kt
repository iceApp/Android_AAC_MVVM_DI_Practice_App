package com.example.meditation.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import com.example.meditation.R
import com.example.meditation.data.ThemeData
import com.example.meditation.model.UserSettings
import com.example.meditation.model.UserSettingsRepository
import com.example.meditation.util.PlayStatus
import org.koin.core.KoinComponent
import org.koin.core.inject
import java.util.*
import kotlin.concurrent.schedule

class MainViewModel(private val context: Application): AndroidViewModel(context), KoinComponent {

    val msgUpperSmall = MutableLiveData<String>()
    val msgLowerLarge = MutableLiveData<String>()
    var themePicFileResId = MutableLiveData<Int>()
    var txtTheme = MutableLiveData<String>()
    var txtLevel = MutableLiveData<String>()
    var levelId = MutableLiveData<Int>()

    var timeId = MutableLiveData<Int>()
    var remainedTimeSeconds = MutableLiveData<Int>()
    var displayTimeSeconds = MutableLiveData<String>()
    
    val playStatus = MutableLiveData<Int>()

    var volume = MutableLiveData<Int>()

    //private val userSettingRepository = UserSettingsRepository()
    private val userSettingRepository: UserSettingsRepository by inject()

    private lateinit var userSettings: UserSettings

    // 呼吸間隔
    private val inhaleInterval = 4
    private var holdInterval = 0
    private var exhaleInterval = 0
    private var totalInterval = 0

    var arrayInterval: ArrayList<Int> = arrayListOf()
    var arrayIntervalIndex: Int = 0
    var totalIntervalSecond: Int = 0
    var remaindIntervalSecond: Int = 0
    private var timerMediation: Timer? = null

    fun initParameters() {
        userSettings = userSettingRepository.loadUserSettings()
        msgUpperSmall.value = ""
        msgLowerLarge.value = ""
        themePicFileResId.value = userSettings.themeResId
        levelId.value = userSettings.levelId
        txtTheme.value = userSettings.themeName
        txtLevel.value = userSettings.levelName
        timeId.value = userSettings.timeId
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
        levelId.value = selectedItemId
        txtLevel.value = userSettingRepository.setLevel(selectedItemId)
    }

    fun setTime(selectedItemId: Int) {
        timeId.value = selectedItemId
        remainedTimeSeconds.value = userSettingRepository.setTime(selectedItemId) * 60
        displayTimeSeconds.value = changeTimerFormat(remainedTimeSeconds.value!!)
    }

    fun setTheme(themeData: ThemeData) {
        userSettingRepository.setTheme(themeData)
        txtTheme.value = userSettingRepository.loadUserSettings().themeName
        themePicFileResId.value = userSettingRepository.loadUserSettings().themeResId
    }

    fun setVolume(process: Int){
        volume.value = process
    }

    fun changeStatus() {
        when (playStatus.value) {
            PlayStatus.BEFORE_START -> playStatus.value = PlayStatus.ON_START
            PlayStatus.ON_START -> playStatus.value = PlayStatus.RUNNING
            PlayStatus.RUNNING -> playStatus.value = PlayStatus.PAUSE
            PlayStatus.PAUSE -> playStatus.value = PlayStatus.RE_RUNNING
            PlayStatus.RE_RUNNING -> playStatus.value = PlayStatus.PAUSE

        }
    }

    fun countDownBeforeStart() {
        msgUpperSmall.value = context.getString(R.string.starts_in)
        var timeRemained = 3
        msgLowerLarge.value = timeRemained.toString()
        val timer = Timer()
        timer.schedule(1000,1000) {
            if(timeRemained > 1){
                timeRemained -= 1
                msgLowerLarge.postValue(timeRemained.toString())

            } else {
                playStatus.postValue(PlayStatus.RUNNING)
                timeRemained = 0
                timer.cancel()
            }
        }
    }

    fun startMeditation() {
        holdInterval = setholdInterval()
        exhaleInterval = setExhaleInterval()
        totalInterval = inhaleInterval + holdInterval + exhaleInterval
        arrayInterval = arrayListOf(inhaleInterval, holdInterval, exhaleInterval)
        arrayIntervalIndex = 0
        totalIntervalSecond = arrayInterval[arrayIntervalIndex]

        remainedTimeSeconds.value = adjustRemainedTIme(remainedTimeSeconds.value, totalInterval
        )
        displayTimeSeconds.value = changeTimerFormat(remainedTimeSeconds.value!!)
        msgUpperSmall.value = context.getString(R.string.inhale)
        msgLowerLarge.value = inhaleInterval.toString()

        clockMeditation()
    }

    fun clockMeditation() {
        timerMediation = Timer()
        timerMediation?.schedule(1000, 1000){
            val tempTime = remainedTimeSeconds.value!! - 1
            remainedTimeSeconds.postValue(tempTime)
            displayTimeSeconds.postValue(changeTimerFormat(tempTime))

            if(remainedTimeSeconds.value!! <= 0){
                msgUpperSmall.postValue("")
                msgLowerLarge.postValue(context.resources.getString(R.string.meiso_finish))
                playStatus.postValue(PlayStatus.END)
                cancelTimer()
                return@schedule
            }
            // 経過時間に応じて呼吸パターンの文言を切り変える
            setMediationCycle()
        }
    }

    private fun setMediationCycle() {
        if (remaindIntervalSecond < totalIntervalSecond) {
            var  intervalSecondsLeft = totalIntervalSecond - remaindIntervalSecond
            msgLowerLarge.postValue( intervalSecondsLeft.toString())
        } else {
            if (arrayIntervalIndex++ < 2) arrayIntervalIndex else arrayIntervalIndex = 0
            totalIntervalSecond = arrayInterval[arrayIntervalIndex]
            remaindIntervalSecond = 0
            msgLowerLarge.postValue(totalIntervalSecond.toString())
        }

        remaindIntervalSecond++
        when (arrayIntervalIndex){
            0 -> msgUpperSmall.postValue(context.resources.getString(R.string.inhale))
            1 -> msgUpperSmall.postValue(context.resources.getString(R.string.hold))
            2 -> msgUpperSmall.postValue(context.resources.getString(R.string.exhale))
        }
    }

    private fun cancelTimer() {
        timerMediation?.cancel()
    }

    private fun adjustRemainedTIme(remainedTime: Int?, totalInterval: Int): Int? {
        val remainder = remainedTime!! % totalInterval
        return if (remainder > (totalInterval / 2 )){
            remainedTime + (totalInterval - remainder)
        } else {
            remainedTime - remainder
        }
    }

    private fun setExhaleInterval(): Int {
        return when (userSettingRepository.loadUserSettings().levelId){
            0 -> 4
            1 -> 8
            2 -> 8
            3 -> 8
            else -> 0
        }
    }

    private fun setholdInterval(): Int {
        return when (userSettingRepository.loadUserSettings().levelId){
            0 -> 4
            1 -> 4
            2 -> 8
            3 -> 16
            else -> 0
        }
    }

    fun poseMeditation() {
        cancelTimer()
    }

    fun finishMeditation(){
        cancelTimer()
        playStatus.value = PlayStatus.BEFORE_START
        remainedTimeSeconds.value = userSettingRepository.loadUserSettings().time * 60
        displayTimeSeconds.value = changeTimerFormat(remainedTimeSeconds.value!!)
        msgUpperSmall.value = ""
        msgLowerLarge.value = context.resources.getString(R.string.meiso_finish)
    }

    override fun onCleared() {
        super.onCleared()
        cancelTimer()
    }
}