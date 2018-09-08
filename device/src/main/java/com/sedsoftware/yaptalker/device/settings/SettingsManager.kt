package com.sedsoftware.yaptalker.device.settings

import android.content.Context
import android.content.SharedPreferences
import android.os.Build
import android.support.annotation.StringRes
import com.sedsoftware.yaptalker.device.R
import com.sedsoftware.yaptalker.domain.device.Settings
import java.util.Date
import javax.inject.Inject

class SettingsManager @Inject constructor(
    private val context: Context,
    private val preferences: SharedPreferences
) : Settings {

    companion object {
        private const val TEXT_SIZE_OFFSET = 2f
        private const val MESSAGES_PER_PAGE_DEFAULT = 25
        private const val TOPICS_PER_PAGE_DEFAULT = 30
    }

    private val defaultCategories by lazy {
        context.resources.getStringArray(R.array.pref_categorizer_values).toSet()
    }

    override fun getSingleCookie(): String =
        getStringPref(R.string.pref_key_cookie, "")

    override fun getStartingPage(): String {
        val current = getStringPref(R.string.pref_key_start_page, "")
        val forums = context.resources.getString(R.string.pref_general_start_page_value_forums)
        val topics = context.resources.getString(R.string.pref_general_start_page_value_active_topics)
        val incubator = context.resources.getString(R.string.pref_general_start_page_value_incubator)

        return when (current) {
            forums -> DefaultHomeScreen.FORUMS
            topics -> DefaultHomeScreen.ACTIVE_TOPICS
            incubator -> DefaultHomeScreen.INCUBATOR
            else -> DefaultHomeScreen.MAIN
        }
    }

    override fun getAvatarSize(): Int =
        getStringPref(
            R.string.pref_key_avatar_size,
            context.resources.getString(R.string.pref_appearance_avatar_size_value_small)
        ).toInt()

    override fun getNormalFontSize(): Float =
        getStringPref(
            R.string.pref_key_font_size,
            context.getString(R.string.pref_appearance_font_size_value_14)
        ).toFloat()

    override fun getBigFontSize(): Float =
        getStringPref(
            R.string.pref_key_font_size,
            context.getString(R.string.pref_appearance_font_size_value_16)
        ).toFloat() + TEXT_SIZE_OFFSET

    override fun getSmallFontSize(): Float =
        getStringPref(
            R.string.pref_key_font_size,
            context.getString(R.string.pref_appearance_font_size_value_12)
        ).toFloat() - TEXT_SIZE_OFFSET

    override fun getMessagesPerPage(): Int =
        getIntPref(R.string.pref_key_messages_per_page, MESSAGES_PER_PAGE_DEFAULT)

    override fun getTopicsPerPage(): Int =
        getIntPref(R.string.pref_key_topics_per_page, TOPICS_PER_PAGE_DEFAULT)

    override fun getNewsCategories(): Set<String> =
        getStringSetPref(R.string.pref_key_categorizer, defaultCategories)

    override fun getLastUpdateCheckDate(): Long =
        getLongPref(R.string.pref_key_last_update_check, 0L)

    override fun getCurrentTheme(): String =
        getStringPref(R.string.pref_key_theme, "")

    override fun isNsfwEnabled(): Boolean =
        getBooleanPref(R.string.pref_key_nswf, false)

    override fun isExternalCoubPlayer(): Boolean =
        getBooleanPref(R.string.pref_key_coub_external, false)

    override fun isScreenAlwaysOnEnabled(): Boolean =
        getBooleanPref(R.string.pref_key_screen_always_on, false)

    @Suppress("MagicNumber")
    override fun isHttpsEnabled(): Boolean {
        val default = Build.VERSION.SDK_INT !in 19..20
        return getBooleanPref(R.string.pref_key_https_enabled, default)
    }

    override fun isInTwoPaneMode(): Boolean =
        getBooleanPref(R.string.pref_key_two_pane_mode, context.resources.getBoolean(R.bool.two_pane_mode))

    override fun isVideoLandscapeEnabled(): Boolean =
        getBooleanPref(R.string.pref_key_video_landscape, true)

    override fun saveSingleCookie(cookie: String) {
        preferences.edit().putString(context.resources.getString(R.string.pref_key_cookie), cookie).apply()
    }

    override fun saveMessagesPerPagePref(value: Int) {
        preferences.edit().putInt(context.resources.getString(R.string.pref_key_messages_per_page), value).apply()
    }

    override fun saveTopicsPerPagePref(value: Int) {
        preferences.edit().putInt(context.resources.getString(R.string.pref_key_topics_per_page), value).apply()
    }

    override fun saveLastUpdateCheckDate() {
        val currentTime = Date().time
        preferences
            .edit()
            .putLong(context.resources.getString(R.string.pref_key_last_update_check), currentTime)
            .apply()
    }

    private fun getStringPref(@StringRes key: Int, default: String): String =
        preferences.getString(context.resources.getString(key), default)

    private fun getStringSetPref(@StringRes key: Int, default: Set<String>): Set<String> =
        preferences.getStringSet(context.resources.getString(key), default)

    private fun getBooleanPref(@StringRes key: Int, default: Boolean): Boolean =
        preferences.getBoolean(context.resources.getString(key), default)

    private fun getIntPref(@StringRes key: Int, default: Int): Int =
        preferences.getInt(context.resources.getString(key), default)

    private fun getLongPref(@StringRes key: Int, default: Long): Long =
        preferences.getLong(context.resources.getString(key), default)
}
