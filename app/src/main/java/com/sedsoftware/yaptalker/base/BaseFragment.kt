package com.sedsoftware.yaptalker.base

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.arellomobile.mvp.MvpAppCompatFragment
import com.jakewharton.rxrelay2.BehaviorRelay
import com.sedsoftware.yaptalker.base.events.FragmentLifecycle
import io.reactivex.Maybe

abstract class BaseFragment : MvpAppCompatFragment() {

  protected abstract val layoutId: Int

  // Local fragment lifecycle events channel
  private val lifecycle: BehaviorRelay<Long> = BehaviorRelay.create()

  override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
    return inflater.inflate(layoutId, container, false)
  }

  @Suppress("EmptyFunctionBlock")
  protected open fun subscribeViews() {
  }

  override fun onAttach(context: Context?) {
    super.onAttach(context)
    lifecycle.accept(FragmentLifecycle.ATTACH)
  }

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    lifecycle.accept(FragmentLifecycle.CREATE)
  }

  override fun onStart() {
    super.onStart()
    lifecycle.accept(FragmentLifecycle.START)
    subscribeViews()
  }

  override fun onResume() {
    super.onResume()
    lifecycle.accept(FragmentLifecycle.RESUME)
  }

  override fun onPause() {
    super.onPause()
    lifecycle.accept(FragmentLifecycle.PAUSE)
  }

  override fun onStop() {
    super.onStop()
    lifecycle.accept(FragmentLifecycle.STOP)
  }

  override fun onDestroy() {
    super.onDestroy()
    lifecycle.accept(FragmentLifecycle.DESTROY)
  }

  override fun onDetach() {
    super.onDetach()
    lifecycle.accept(FragmentLifecycle.DETACH)
  }

  protected fun event(@FragmentLifecycle.Event event: Long): Maybe<*> {
    return lifecycle.filter({ e -> e == event }).firstElement()
  }
}
