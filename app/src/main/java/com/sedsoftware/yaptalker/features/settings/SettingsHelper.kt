package com.sedsoftware.yaptalker.features.settings

import android.content.Context
import android.support.annotation.StringRes
import com.sedsoftware.yaptalker.R
import com.sedsoftware.yaptalker.base.navigation.NavigationScreens
import com.sedsoftware.yaptalker.commons.extensions.stringRes
import org.jetbrains.anko.defaultSharedPreferences

class SettingsHelper(val context: Context) {

  companion object {
    private const val TEXT_SIZE_OFFSET = 2f
  }

  private val preferences by lazy {
    context.defaultSharedPreferences
  }

  private val defaultCategories by lazy {
    context.resources.getStringArray(R.array.pref_categorizer_values).toSet()
  }

  fun getStartingPage(): String {
    val current = getStringPref(R.string.pref_key_start_page, "")
    val forums = context.stringRes(R.string.pref_general_start_page_value_forums)

    return when (current) {
      forums -> NavigationScreens.FORUMS_LIST_SCREEN
      else -> NavigationScreens.NEWS_SCREEN
    }
  }

  fun getAvatarSize() = getStringPref(
      R.string.pref_key_avatar_size,
      context.stringRes(R.string.pref_appearance_avatar_size_value_small)).toInt()

  fun getNormalFontSize() = getStringPref(
      R.string.pref_key_font_size,
      context.getString(R.string.pref_appearance_font_size_value_14)).toFloat()

  fun getBigFontSize() = getStringPref(
      R.string.pref_key_font_size,
      context.getString(R.string.pref_appearance_font_size_value_14)).toFloat() + TEXT_SIZE_OFFSET

  fun getSmallFontSize() = getStringPref(
      R.string.pref_key_font_size,
      context.getString(R.string.pref_appearance_font_size_value_14)).toFloat() - TEXT_SIZE_OFFSET

  fun getNewsCategories() = getStringSetPref(R.string.pref_key_categorizer, defaultCategories)

  fun isNsfwEnabled() = getBooleanPref(R.string.pref_key_nswf, false)

  fun isEulaAccepted() = getBooleanPref(R.string.pref_key_eula_accepted, false)

  fun isScreenAlwaysOnEnabled() = getBooleanPref(R.string.pref_key_screen_always_on, false)

  fun markEulaAccepted() {
    preferences.edit().putBoolean(context.stringRes(R.string.pref_key_eula_accepted), true).apply()
  }

  private fun getStringPref(@StringRes key: Int, default: String): String =
      preferences.getString(context.stringRes(key), default)

  private fun getStringSetPref(@StringRes key: Int, default: Set<String>): Set<String> =
      preferences.getStringSet(context.stringRes(key), default)

  private fun getBooleanPref(@StringRes key: Int, default: Boolean): Boolean =
      preferences.getBoolean(context.stringRes(key), default)
}
