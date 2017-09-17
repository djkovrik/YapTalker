package com.sedsoftware.yaptalker.features.settings

import android.content.SharedPreferences
import android.content.res.Configuration
import android.os.Bundle
import android.preference.Preference
import android.preference.PreferenceActivity
import android.preference.PreferenceCategory
import android.preference.PreferenceGroup
import android.support.annotation.LayoutRes
import android.support.annotation.Nullable
import android.support.v7.app.ActionBar
import android.support.v7.app.AppCompatDelegate
import android.support.v7.widget.Toolbar
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import com.afollestad.materialdialogs.prefs.MaterialListPreference
import com.sedsoftware.yaptalker.R

// TODO() Replace with controller maybe?
// TODO() Investigate deprecation
class SettingsActivity : PreferenceActivity(), SharedPreferences.OnSharedPreferenceChangeListener {

  private val delegate by lazy {
    AppCompatDelegate.create(this, null)
  }

  private val sharedPreferences by lazy {
    preferenceScreen.sharedPreferences
  }

  private val prefScreen by lazy {
    preferenceScreen
  }

  override fun onCreate(savedInstanceState: Bundle?) {
    delegate.installViewFactory()
    delegate.onCreate(savedInstanceState)
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

  override fun onOptionsItemSelected(item: MenuItem): Boolean {
    return when (item.itemId) {
      android.R.id.home -> {
        finish()
        true
      }
      else -> super.onOptionsItemSelected(item)
    }
  }

  override fun onPostCreate(savedInstanceState: Bundle?) {
    super.onPostCreate(savedInstanceState)
    delegate.onPostCreate(savedInstanceState)
  }

  private fun getSupportActionBar(): ActionBar? {
    return delegate.supportActionBar
  }

  private fun setSupportActionBar(@Nullable toolbar: Toolbar) {
    delegate.setSupportActionBar(toolbar)
  }

  override fun getMenuInflater(): MenuInflater {
    return delegate.menuInflater
  }

  override fun setContentView(@LayoutRes layoutResID: Int) {
    delegate.setContentView(layoutResID)
  }

  override fun setContentView(view: View) {
    delegate.setContentView(view)
  }

  override fun setContentView(view: View, params: ViewGroup.LayoutParams) {
    delegate.setContentView(view, params)
  }

  override fun addContentView(view: View, params: ViewGroup.LayoutParams) {
    delegate.addContentView(view, params)
  }

  override fun onPostResume() {
    super.onPostResume()
    delegate.onPostResume()
  }

  override fun onTitleChanged(title: CharSequence, color: Int) {
    super.onTitleChanged(title, color)
    delegate.setTitle(title)
  }

  override fun onConfigurationChanged(newConfig: Configuration) {
    super.onConfigurationChanged(newConfig)
    delegate.onConfigurationChanged(newConfig)
  }

  override fun onStop() {
    super.onStop()
    delegate.onStop()
  }

  override fun onDestroy() {
    super.onDestroy()
    delegate.onDestroy()
    sharedPreferences.unregisterOnSharedPreferenceChangeListener(this)
  }

  override fun invalidateOptionsMenu() {
    delegate.invalidateOptionsMenu()
  }
}
