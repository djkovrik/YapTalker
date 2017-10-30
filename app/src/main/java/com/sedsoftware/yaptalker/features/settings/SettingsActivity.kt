package com.sedsoftware.yaptalker.features.settings

import android.content.Context
import android.content.Intent
import android.os.Bundle
import com.arellomobile.mvp.MvpAppCompatActivity

class SettingsActivity : MvpAppCompatActivity() {

  companion object {
    fun getIntent(ctx: Context) = Intent(ctx, SettingsActivity::class.java)
  }

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    fragmentManager.beginTransaction()
        .replace(android.R.id.content, SettingsFragment())
        .commit()
  }
}
