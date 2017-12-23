package com.sedsoftware.yaptalker.presentation.features.settings

import android.content.SharedPreferences
import android.os.Bundle
import android.preference.Preference
import android.preference.PreferenceCategory
import android.preference.PreferenceFragment
import android.preference.PreferenceGroup
import com.afollestad.materialdialogs.prefs.MaterialListPreference
import com.sedsoftware.yaptalker.R

class SettingsFragment : PreferenceFragment(), SharedPreferences.OnSharedPreferenceChangeListener {

  private val sharedPreferences by lazy {
    preferenceScreen.sharedPreferences
  }

  private val prefScreen by lazy {
    preferenceScreen
  }

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)

    addPreferencesFromResource(R.xml.preferences)

    val count = prefScreen.preferenceCount

    for (i in 0 until count) {

      val category = prefScreen.getPreference(i)

      if (category is PreferenceCategory) {
        val group = category as PreferenceGroup
        val groupCount = group.preferenceCount

        (0 until groupCount)
            .map { group.getPreference(it) }
            .forEach { setListPreferenceSummary(sharedPreferences, it) }
      }
    }

    sharedPreferences.registerOnSharedPreferenceChangeListener(this)
  }

  override fun onSharedPreferenceChanged(sharedPreferences: SharedPreferences, key: String) {
    val preference = findPreference(key)
    setListPreferenceSummary(sharedPreferences, preference)
  }

  private fun setListPreferenceSummary(sharedPreferences: SharedPreferences, pref: Preference) {
    if (pref is MaterialListPreference) {
      val value = sharedPreferences.getString(pref.getKey(), "")
      val index = pref.findIndexOfValue(value)
      if (index >= 0) {
        pref.setSummary(pref.entries[index])
      }
    }
  }
}
