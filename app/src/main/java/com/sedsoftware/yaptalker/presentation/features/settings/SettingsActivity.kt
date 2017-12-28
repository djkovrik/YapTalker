package com.sedsoftware.yaptalker.presentation.features.settings

import android.content.Context
import android.content.Intent
import android.os.Bundle
import com.sedsoftware.yaptalker.R
import com.sedsoftware.yaptalker.presentation.base.BaseActivity
import kotlinx.android.synthetic.main.include_main_appbar.toolbar

class SettingsActivity : BaseActivity() {

  companion object {
    fun getIntent(ctx: Context) = Intent(ctx, SettingsActivity::class.java)
  }

  override val layoutId: Int
    get() = R.layout.activity_settings

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)

    setSupportActionBar(toolbar)
    supportActionBar?.setDisplayHomeAsUpEnabled(true)

    fragmentManager.beginTransaction()
        .replace(R.id.settings_container, SettingsFragment())
        .commit()
  }
}
