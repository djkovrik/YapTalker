package com.sedsoftware.yaptalker.features.settings

import android.content.Context
import android.support.annotation.StringRes
import com.sedsoftware.yaptalker.R
import com.sedsoftware.yaptalker.commons.extensions.stringRes
import com.sedsoftware.yaptalker.features.base.BaseController
import com.sedsoftware.yaptalker.features.forumslist.ForumsController
import com.sedsoftware.yaptalker.features.news.NewsController
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

  private val defaultCookies by lazy {
    setOf("SID=deleted", "expires=Thu, 01-Jan-1970 00:00:01 GMT", "path=/", "domain=.yaplakal.com",
        "remote_authorised=deleted")
  }

  fun getStartingPage(): BaseController {
    val current = getString(R.string.pref_key_start_page, "")
    val forums = context.stringRes(R.string.pref_general_start_page_value_forums)

    return when (current) {
      forums -> ForumsController()
      else -> NewsController()
    }
  }

  fun getNormalFontSize() = getString(R.string.pref_key_font_size, "14").toFloat()
  fun getBigFontSize() = getString(R.string.pref_key_font_size, "16").toFloat() + TEXT_SIZE_OFFSET
  fun getSmallFontSize() = getString(R.string.pref_key_font_size, "12").toFloat() - TEXT_SIZE_OFFSET
  fun getNewsCategories() = getStringSet(R.string.pref_key_categorizer, defaultCategories)
  fun isNsfwEnabled() = getBoolean(R.string.pref_key_nswf, false)

  private fun getString(@StringRes key: Int, default: String): String {
    return preferences.getString(context.stringRes(key), default)
  }

  private fun getStringSet(@StringRes key: Int, default: Set<String>): Set<String> {
    return preferences.getStringSet(context.stringRes(key), default)
  }

  private fun getBoolean(@StringRes key: Int, default: Boolean): Boolean {
    return preferences.getBoolean(context.stringRes(key), default)
  }
}
