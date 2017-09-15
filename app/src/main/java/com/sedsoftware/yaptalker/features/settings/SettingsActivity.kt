package com.sedsoftware.yaptalker.features.settings

import android.content.res.Configuration
import android.os.Bundle
import android.preference.PreferenceActivity
import android.support.annotation.LayoutRes
import android.support.annotation.Nullable
import android.support.v7.app.ActionBar
import android.support.v7.app.AppCompatDelegate
import android.support.v7.widget.Toolbar
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import com.sedsoftware.yaptalker.R

// TODO() Replace with controller maybe?
// TODO() Add dynamic summary setup
class SettingsActivity : PreferenceActivity() {

  private val delegate by lazy {
    AppCompatDelegate.create(this, null)
  }

  override fun onCreate(savedInstanceState: Bundle?) {
    delegate.installViewFactory()
    delegate.onCreate(savedInstanceState)
    super.onCreate(savedInstanceState)

    addPreferencesFromResource(R.xml.preferences)
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
  }

  override fun invalidateOptionsMenu() {
    delegate.invalidateOptionsMenu()
  }
}