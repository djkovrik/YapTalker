package com.sedsoftware.yaptalker.base

import android.os.Bundle
import com.arellomobile.mvp.MvpAppCompatActivity
import com.sedsoftware.yaptalker.base.events.ActivityLifecycle
import io.reactivex.Maybe
import io.reactivex.subjects.BehaviorSubject

abstract class BaseActivity : MvpAppCompatActivity() {

  protected abstract val layoutId: Int

  // Activity lifecycle events channel
  private val lifecycle: BehaviorSubject<Long> = BehaviorSubject.create()

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContentView(layoutId)

    lifecycle.onNext(ActivityLifecycle.CREATE)
  }

  override fun onDestroy() {
    super.onDestroy()
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

  protected fun event(@ActivityLifecycle.Event event: Long): Maybe<*> {
    return lifecycle.filter({ e -> e == event }).firstElement()
  }
}
