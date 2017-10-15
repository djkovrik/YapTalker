package com.sedsoftware.yaptalker.base

import android.os.Bundle
import android.view.ViewGroup
import com.arellomobile.mvp.MvpAppCompatActivity
import com.bluelinelabs.conductor.Conductor
import com.bluelinelabs.conductor.Controller
import com.bluelinelabs.conductor.ControllerChangeHandler
import com.bluelinelabs.conductor.Router
import com.bluelinelabs.conductor.RouterTransaction
import com.sedsoftware.yaptalker.base.events.ActivityLifecycle
import io.reactivex.Maybe
import io.reactivex.subjects.BehaviorSubject

abstract class BaseControllerActivity : MvpAppCompatActivity(), ControllerChangeHandler.ControllerChangeListener {

  protected lateinit var router: Router

  protected abstract val layoutId: Int
  protected abstract val contentFrame: ViewGroup
  protected abstract val rootController: Controller

  protected abstract fun onControllerChanged(target: Controller?)

  // Presenter lifecycle events channel
  private val lifecycle: BehaviorSubject<Long> = BehaviorSubject.create()

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContentView(layoutId)

    router = Conductor.attachRouter(this, contentFrame, savedInstanceState)
    if (!router.hasRootController()) {
      router.setRoot(RouterTransaction.with(rootController))
    }

    router.addChangeListener(this)

    lifecycle.onNext(ActivityLifecycle.CREATE)
  }

  override fun onDestroy() {
    super.onDestroy()
    router.removeChangeListener(this)
    lifecycle.onNext(ActivityLifecycle.DESTROY)
  }

  override fun onStart() {
    super.onStart()
    lifecycle.onNext(ActivityLifecycle.START)
  }

  override fun onStop() {
    super.onStop()
    lifecycle.onNext(ActivityLifecycle.STOP)
  }

  override fun onResume() {
    super.onResume()
    lifecycle.onNext(ActivityLifecycle.RESUME)
  }

  override fun onPause() {
    super.onPause()
    lifecycle.onNext(ActivityLifecycle.PAUSE)
  }

  override fun onChangeStarted(to: Controller?, from: Controller?, isPush: Boolean,
                               container: ViewGroup, handler: ControllerChangeHandler) {
  }

  override fun onChangeCompleted(to: Controller?, from: Controller?, isPush: Boolean,
                                 container: ViewGroup, handler: ControllerChangeHandler) {
    onControllerChanged(to)
  }

  protected fun event(@ActivityLifecycle.Event event: Long): Maybe<*> {
    return lifecycle.filter({ e -> e == event }).firstElement()
  }
}
