package com.sedsoftware.yaptalker.data

import android.content.Context
import android.content.SharedPreferences
import android.support.annotation.StringRes
import com.sedsoftware.yaptalker.R
import com.sedsoftware.yaptalker.presentation.base.enums.navigation.NavigationScreen
import javax.inject.Inject

/**
 * Supporting class for Preferences management.
 *
 * @param context Context instance.
 * @param preferences Default SharedPreferences instance.
 */
class SettingsManager @Inject constructor(
    private val context: Context,
    private val preferences: SharedPreferences) {

  companion object {
    private const val TEXT_SIZE_OFFSET = 2f
  }

  private val defaultCategories by lazy {
    context.resources.getStringArray(R.array.pref_categorizer_values).toSet()
  }

  fun getStartingPage(): String {
    val current = getStringPref(R.string.pref_key_start_page, "")
    val forums = context.resources.getString(R.string.pref_general_start_page_value_forums)

    return when (current) {
      forums -> NavigationScreen.FORUMS_LIST_SCREEN
      else -> NavigationScreen.NEWS_SCREEN
    }
  }

  fun getAvatarSize(): Int =
      getStringPref(
          R.string.pref_key_avatar_size,
          context.resources.getString(R.string.pref_appearance_avatar_size_value_small)).toInt()

  fun getNormalFontSize(): Float =
      getStringPref(
          R.string.pref_key_font_size,
          context.getString(R.string.pref_appearance_font_size_value_14)).toFloat()

  fun getBigFontSize(): Float =
      getStringPref(
          R.string.pref_key_font_size,
          context.getString(R.string.pref_appearance_font_size_value_14)).toFloat() + TEXT_SIZE_OFFSET

  fun getSmallFontSize(): Float =
      getStringPref(
          R.string.pref_key_font_size,
          context.getString(R.string.pref_appearance_font_size_value_14)).toFloat() - TEXT_SIZE_OFFSET

  fun getNewsCategories(): Set<String> =
      getStringSetPref(R.string.pref_key_categorizer, defaultCategories)

  fun isNsfwEnabled(): Boolean = getBooleanPref(R.string.pref_key_nswf, false)

  fun isEulaAccepted(): Boolean =
      getBooleanPref(R.string.pref_key_eula_accepted, false)

  fun isScreenAlwaysOnEnabled(): Boolean =
      getBooleanPref(R.string.pref_key_screen_always_on, false)

  fun markEulaAccepted() {
    preferences.edit().putBoolean(context.resources.getString(R.string.pref_key_eula_accepted), true).apply()
  }

  private fun getStringPref(@StringRes key: Int, default: String): String =
      preferences.getString(context.resources.getString(key), default)

  private fun getStringSetPref(@StringRes key: Int, default: Set<String>): Set<String> =
      preferences.getStringSet(context.resources.getString(key), default)

  private fun getBooleanPref(@StringRes key: Int, default: Boolean): Boolean =
      preferences.getBoolean(context.resources.getString(key), default)
}
