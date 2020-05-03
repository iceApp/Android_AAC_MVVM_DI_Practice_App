package com.example.meditation.model

import android.content.Context
import android.content.SharedPreferences
import android.util.Log
import com.example.meditation.MyApplication
import com.example.meditation.R
import net.minpro.meditation.LevelId

class UserSettingsRepository {
    private val context: Context =
        MyApplication.appContext
    private val pref: SharedPreferences = context.getSharedPreferences(UserSettings.PREF_USERSETTINGS_NAME,Context.MODE_PRIVATE)
    private val editor: SharedPreferences.Editor = pref.edit()

    fun loadUserSettings(): UserSettings {
        return UserSettings(
            levelId = pref.getInt(
                UserSettingsPrefkey.LEVEL_ID.name,
                LevelId.EASY
            ),
            levelName = context.getString(
                pref.getInt(
                    UserSettingsPrefkey.LEVEL_NAME_STR_ID.name,
                    R.string.level_easy_header
                )
            ),
            themeId = pref.getInt(
                UserSettingsPrefkey.THEME_ID.name,
                0
            ),
            themeName = context.getString(
                pref.getInt(
                    UserSettingsPrefkey.THEME_NAME_STR_ID.name,
                    R.string.theme_silent
                )
            ),
            themeResId = pref.getInt(
                UserSettingsPrefkey.THEME_RES_ID.name,
                R.drawable.pic_nobgm
            ),
            themeSoundId = pref.getInt(
                UserSettingsPrefkey.THEME_SOUND_ID.name,
                0
            ),

            timeSelectId = pref.getInt(
                UserSettingsPrefkey.TIME_SELECT_ID.name,
                4
            ),
            time = pref.getInt(
                UserSettingsPrefkey.TIME.name,
                30
            )
        )
    }

    fun setLevel(selectedItemId: Int): String {
        editor.putInt(UserSettingsPrefkey.LEVEL_ID.name,selectedItemId).commit()
        val levelNameStrId = when (selectedItemId){
            0 -> R.string.level_easy_header
            1 -> R.string.level_normal_header
            2 -> R.string.level_mid_header
            3 -> R.string.level_high_header
            else -> {0}
        }
        editor.putInt(UserSettingsPrefkey.LEVEL_NAME_STR_ID.name, levelNameStrId).commit()
        return loadUserSettings().levelName
    }

    fun getTimeId(): Int{
        return loadUserSettings().timeSelectId
    }

    fun setTime(selectedItemId: Int): Int{
        editor.putInt(UserSettingsPrefkey.TIME_SELECT_ID.name, selectedItemId).commit()
        val selectedTime: Int = when(selectedItemId){
            0 -> 5
            1 -> 10
            2 -> 15
            3 -> 20
            4 -> 30
            5 -> 45
            6 -> 60
            else -> 30
        }
        editor.putInt(UserSettingsPrefkey.TIME.name, selectedTime).commit()
        return loadUserSettings().time
    }
}