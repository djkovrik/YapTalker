package com.sedsoftware.yaptalker.features.settings

import android.content.Context
import android.support.annotation.StringRes
import com.github.salomonbrys.kodein.Kodein
import com.github.salomonbrys.kodein.bind
import com.github.salomonbrys.kodein.instance
import com.github.salomonbrys.kodein.singleton
import com.sedsoftware.yaptalker.R
import com.sedsoftware.yaptalker.commons.extensions.stringRes
import com.sedsoftware.yaptalker.features.base.BaseController
import com.sedsoftware.yaptalker.features.forumslist.ForumsController
import com.sedsoftware.yaptalker.features.news.NewsController
import org.jetbrains.anko.defaultSharedPreferences

class SettingsReader(val context: Context) {

  companion object {
    private const val TEXT_SIZE_OFFSET = 2f
  }

  private val preferences by lazy {
    context.defaultSharedPreferences
  }

  fun getStartingPage(): BaseController {
    val current = getString(R.string.pref_key_start_page)
    val main = context.stringRes(R.string.pref_general_start_page_value_main)

    return when (current) {
      main -> NewsController()
      else -> ForumsController()
    }
  }

  fun getNormalFontSize() = getString(R.string.pref_key_font_size).toFloat()
  fun getBigFontSize() = getString(R.string.pref_key_font_size).toFloat() + TEXT_SIZE_OFFSET
  fun getSmallFontSize() = getString(R.string.pref_key_font_size).toFloat() - TEXT_SIZE_OFFSET
  fun getNewsCategories() = getStringSet(R.string.pref_key_categorizer)

  private fun getString(@StringRes key: Int): String {
    return preferences.getString(context.stringRes(key), "")
  }

  private fun getStringSet(@StringRes key: Int): Set<String> {
    return preferences.getStringSet(context.stringRes(key), HashSet<String>())
  }

  private fun getBoolean(@StringRes key: Int): Boolean {
    return preferences.getBoolean(context.stringRes(key), false)
  }
}

val settingsModule = Kodein.Module {
  bind<SettingsReader>() with singleton {
    SettingsReader(instance())
  }
}
