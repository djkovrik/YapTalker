package com.sedsoftware.yaptalker.features.base

import android.os.Bundle
import android.view.ViewGroup
import com.arellomobile.mvp.MvpAppCompatActivity
import com.bluelinelabs.conductor.Conductor
import com.bluelinelabs.conductor.Controller
import com.bluelinelabs.conductor.Router
import com.bluelinelabs.conductor.RouterTransaction
import com.evernote.android.state.StateSaver

abstract class BaseActivity: MvpAppCompatActivity() {

  protected lateinit var router: Router

  protected abstract val layoutId: Int
  protected abstract val contentFrame: ViewGroup
  protected abstract val rootController: Controller

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    StateSaver.restoreInstanceState(this, savedInstanceState)
    setContentView(layoutId)

    router = Conductor.attachRouter(this, contentFrame, savedInstanceState)
    if (!router.hasRootController()) {
      router.setRoot(RouterTransaction.with(rootController))
    }
  }

  override fun onSaveInstanceState(outState: Bundle) {
    super.onSaveInstanceState(outState)
    StateSaver.saveInstanceState(this, outState)
  }
}
