package com.sedsoftware.yaptalker.features.settings

import android.content.Context
import android.support.annotation.StringRes
import com.github.salomonbrys.kodein.Kodein
import com.github.salomonbrys.kodein.bind
import com.github.salomonbrys.kodein.instance
import com.github.salomonbrys.kodein.singleton
import com.sedsoftware.yaptalker.commons.extensions.stringRes
import org.jetbrains.anko.defaultSharedPreferences

class SettingsHelper(val context: Context) {

  private val preferences by lazy {
    context.defaultSharedPreferences
  }

  fun getString(@StringRes key: Int): String {
    return preferences.getString(context.stringRes(key), "")
  }

  fun getStringSet(@StringRes key: Int): Set<String>? {
    return preferences.getStringSet(context.stringRes(key), HashSet<String>())
  }

  fun getBoolean(@StringRes key: Int): Boolean {
    return preferences.getBoolean(context.stringRes(key), false)
  }
}

val settingsModule = Kodein.Module {
  bind<SettingsHelper>() with singleton {
    SettingsHelper(instance())
  }
}