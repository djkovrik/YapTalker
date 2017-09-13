package com.sedsoftware.yaptalker.features.base

import android.os.Bundle
import android.view.ViewGroup
import com.arellomobile.mvp.MvpAppCompatActivity
import com.bluelinelabs.conductor.Conductor
import com.bluelinelabs.conductor.Controller
import com.bluelinelabs.conductor.ControllerChangeHandler
import com.bluelinelabs.conductor.Router
import com.bluelinelabs.conductor.RouterTransaction

abstract class BaseActivity : MvpAppCompatActivity(), ControllerChangeHandler.ControllerChangeListener {

  protected lateinit var router: Router

  protected abstract val layoutId: Int
  protected abstract val contentFrame: ViewGroup
  protected abstract val rootController: Controller

  protected abstract fun onControllerChanged(target: Controller?)

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContentView(layoutId)

    router = Conductor.attachRouter(this, contentFrame, savedInstanceState)
    if (!router.hasRootController()) {
      router.setRoot(RouterTransaction.with(rootController))
    }

    router.addChangeListener(this)
  }

  override fun onDestroy() {
    super.onDestroy()
    router.removeChangeListener(this)
  }

  override fun onChangeStarted(to: Controller?, from: Controller?, isPush: Boolean,
      container: ViewGroup, handler: ControllerChangeHandler) {
  }

  override fun onChangeCompleted(to: Controller?, from: Controller?, isPush: Boolean,
      container: ViewGroup, handler: ControllerChangeHandler) {
    onControllerChanged(to)
  }
}
