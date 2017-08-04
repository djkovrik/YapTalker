package com.sedsoftware.yaptalker.features.base

import android.os.Bundle
import com.arellomobile.mvp.MvpAppCompatActivity
import com.evernote.android.state.StateSaver

abstract class BaseActivity: MvpAppCompatActivity() {

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    StateSaver.restoreInstanceState(this, savedInstanceState)
  }

  override fun onSaveInstanceState(outState: Bundle) {
    super.onSaveInstanceState(outState)
    StateSaver.saveInstanceState(this, outState)
  }
}